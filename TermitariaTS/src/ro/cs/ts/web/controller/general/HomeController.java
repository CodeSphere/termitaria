/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.web.controller.general;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCostSheet;
import ro.cs.ts.business.BLNotification;
import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.ConfigParametersProvider;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Notification;
import ro.cs.ts.entity.SearchCostSheetBean;
import ro.cs.ts.entity.SearchNotificationBean;
import ro.cs.ts.entity.SearchProjectBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.utils.ProjectComparator;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;


public class HomeController extends RootAbstractController{
	
	//------------------------VIEW------------------------------------------------------------------	
	private static final String VIEW							= "Home";
	
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY						= "project.";		
	private static final String GENERAL_ERROR					= ROOT_KEY.concat("general.error");	
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------	
	private static final String PROJECTS 						= "PROJECTS";
	private static final String NOTIFICATIONS 					= "NOTIFICATIONS";
	private static final String ACTIVITIES	 					= "ACTIVITIES";
	private static final String OM_MODULES_URL 					= ConfigParametersProvider.getConfigString("modules.url");
	private static final String OM_MODULES						= "OM_MODULES";	
	private static final String USER_ID							= "USER_ID";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String DELETE_FROM_COST_SHEET_FORM = "DELETE_FROM_COSTSHEET_FORM";
		
	//------------------------MESSAGES---------------------------------------------------------------
	private static final String DELETE_ERROR_COSTSHEET			= "costsheet.delete.error";
	private static final String DELETE_SUCCESS_MULTIPLE			= "costsheet.delete.success.multiple";
	private static final String DELETE_SUCCESS_SINGLE			= "costsheet.delete.success.single";

	
	public HomeController() {
		setView(VIEW);
	}
	
	
	/**
	 * @author Andreea
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse result) throws Exception {
		logger.debug("onHandleRequestInternal - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = null;
		
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer costSheetId = ServletRequestUtils.getIntParameter(request, "costSheetId");
		SearchCostSheetBean searchCostSheetBean = new SearchCostSheetBean();
		
		// deletes a cost if the request comes from cost sheet form
		if(action != null && DELETE_FROM_COST_SHEET_FORM.equals(action) && costSheetId != null) {		
			handleDeleteFromCostSheetForm(request, searchCostSheetBean, costSheetId, infoMessages, errorMessages);
		}
		
		try {
			mav = handleProjectsAndActivities(request, errorMessages);
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);

		logger.debug("onHandleRequestInternal - END");
		
		return mav;
	}
	
	/**
	 * 
	 * Handle the search projects and activities operation
	 * 
	 * @author Andreea
	 * 
	 * @param request
	 * @param errorMessages
	 * @return
	 * @throws BusinessException 
	 */
	private ModelAndView handleProjectsAndActivities(HttpServletRequest request, ArrayList<String> errorMessages) throws BusinessException {
		logger.debug("handleProjectsAndActivities - START");

		ModelAndView mav = new ModelAndView(getView());		
		SearchProjectBean searchProjectBean = new SearchProjectBean();
		SearchNotificationBean searchNotificationBean = new SearchNotificationBean();
		List<Notification> notifications = null;
		List<Project> projects = null;
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer userId = userAuth.getPersonId();
		Integer organizationId = userAuth.getOrganisationId();
		
		searchNotificationBean.setCurrentPage(1);
		searchNotificationBean.setResultsPerPage((byte)5);
		searchNotificationBean.setSortParam("id");
		searchNotificationBean.setSortDirection(1);
		
		notifications = BLNotification.getInstance().getNotificationsFromSearch(searchNotificationBean, false);

		projects  = BLProject.getInstance().listProjects(searchProjectBean, userId, organizationId, messageSource);		
		Collections.sort(projects, ProjectComparator.getInstance().nameComparator());	
				
		
		List<List<Activity>> activitiesList = new LinkedList<List<Activity>>();
		for (int i = 0; i < projects.size(); i++) {
			List<Activity> activities = null;
			
			activities = BLActivity.getInstance().getByProjectId(projects.get(i).getProjectId());
			activitiesList.add(activities);
		}
		
		mav.addObject(NOTIFICATIONS, notifications);
		mav.addObject(PROJECTS, projects);
		mav.addObject(ACTIVITIES, activitiesList);

		mav.addObject(OM_MODULES, OM_MODULES_URL);
		mav.addObject(USER_ID, userAuth.getPersonId());
		
		logger.debug("handleProjectsAndActivities - END ");
		return mav;
	}
	
	
	private void handleDeleteFromCostSheetForm(HttpServletRequest request, SearchCostSheetBean searchCostSheetBean, Integer costSheetId, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException{						
		logger.debug("handleDeleteFromRecordForm - START - ");
		
		Integer[] costSheetIds = new Integer[1];
		costSheetIds[0] = costSheetId;			
		searchCostSheetBean.setCostSheetId(costSheetIds);
		handleDeleteAllSimpleCostSheetForm(request, searchCostSheetBean, infoMessages, errorMessages);
				
		logger.debug("handleDeleteFromRecordForm - END - ");
	}
	
	
	private void handleDeleteAllSimpleCostSheetForm(HttpServletRequest request, SearchCostSheetBean searchCostSheetBean, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		// the organization id from the session
		Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
		int deletedCostSheets = 0;

		for (int i = 0; i < searchCostSheetBean.getCostSheetId().length; i++) {
			logger.debug("Delete cost sheet : " + searchCostSheetBean.getCostSheetId()[i]);	
			CostSheet cost = BLCostSheet.getInstance().getAll(searchCostSheetBean.getCostSheetId()[i]);			
			
			String projectName = null;
			String message = null;			
			
			if(cost != null) {
				if(cost.getProjectDetails() != null && cost.getProjectDetails().getProjectId() != -1) {
					Project project = BLProject.getInstance().get(cost.getProjectDetails().getProjectId(), true);
					if(project != null) {
						projectName = project.getName();
					}					
					message = IConstant.NOTIFICATION_MESSAGE_COST_PROJECT_DELETE;
				} else {
					projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
					message = IConstant.NOTIFICATION_MESSAGE_COST_DELETE;
				}
			}
			
			logger.debug("cost = " + cost);
			
			String costSheetOwnerName = null;
			try {
				CostSheet costSheet = BLCostSheet.getInstance().delete(searchCostSheetBean.getCostSheetId()[i]);
				logger.debug("costSheet = " + costSheet);
				// send notification regarding the deletion of a cost				
				if (costSheet.getProjectDetailId() == null) { 
					String[] personIds = new String[1];
					personIds[0] = BLPersonDetail.getInstance().getWithAll(costSheet.getPersonDetailId()).getPersonId().toString();
					UserSimple costSheetOwner = BLUser.getInstance().getUsersByPersonId(personIds).get(0);
					costSheetOwnerName = costSheetOwner.getFirstName().concat(" ").concat(costSheetOwner.getLastName());
					
					sendNotificationCostDelete(costSheet.getProjectId(),costSheet.getProjectDetailId(),organizationId,
							message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName, userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
							IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_COST_DELETE);
					
					//sendNotificationCostDelete(IConstant.NOM_COST_SHEET_FORM_PROJECT_SELECT_ORG_OPTION, organizationId, messageSource.getMessage(
					//		message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName}, 
					//		new Locale("ro")), messageSource.getMessage(
					//				IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null}, new Locale("ro")));
				} else {
					TeamMember costSheetOwner = BLTeamMember.getInstance().getTeamMember(BLTeamMemberDetail.getInstance().get(costSheet.getTeamMemberDetailId()).getTeamMemberId(), false);
					costSheetOwnerName = costSheetOwner.getFirstName().concat(" ").concat(costSheetOwner.getLastName());
					
					sendNotificationCostDelete(costSheet.getProjectId(),costSheet.getProjectDetailId(),organizationId,
							message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
							IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_COST_DELETE);
					
					
					//sendNotificationCostDelete(cost.getProjectDetails().getProjectId(), organizationId, messageSource.getMessage(
					//		message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName},
					//		new Locale("ro")), messageSource.getMessage(
					//				IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null},
					//				new Locale("ro")));
				}
													
				deletedCostSheets++;
												
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (costSheet.getProjectDetailId() == null) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName, projectName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName, projectName}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(DELETE_ERROR_COSTSHEET, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
			}
		}
		
		if (deletedCostSheets == searchCostSheetBean.getCostSheetId().length && searchCostSheetBean.getCostSheetId().length > 1) {
			infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_MULTIPLE, new Object[] { null }, RequestContextUtils.getLocale(request)));
		} else if (deletedCostSheets == searchCostSheetBean.getCostSheetId().length && searchCostSheetBean.getCostSheetId().length == 1) {
			infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_SINGLE, new Object[] { null }, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("handleDeleteAllSimple - END ");
	}
	
	
	public void sendNotificationCostDelete(Integer projectId,Integer projectDetailId, Integer organizationId, String messageCostKey,Object[] messageCostObjects , String subjectCostKey, Object[] subjectCostObjects, Byte setting ) {
		logger.debug("sendNotificationCostDelete - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
		
		try{									
			if(projectId != null && projectId != -1) {
				
				// get the project identified by it's projectId
				Project project = BLProject.getInstance().getSimpleProject(projectId);				
				logger.debug("project = " + project);
				Integer managerId = project.getManagerId();
				logger.debug("managerId = " + managerId);
				
				//1. I have to send a notification to the manager of the project					
				userIds.add(String.valueOf(managerId));	
				userIdsMap.put(String.valueOf(managerId), true);
			} 
			
			// 2. I have to send a notification to the users, that have the permission TS_NotificationReceive
			Set<UserSimple> users = OMWebServiceClient.getInstance().getPersonsFromRole(PermissionConstant.getTheInstance().getTS_NotificationReceive(), organizationId);
			logger.debug("users = " + users);
			if(users != null && users.size() > 0) {							
				for(UserSimple user : users) {
					if (userIds.add(String.valueOf(user.getUserId()))){
						userIdsMap.put(String.valueOf(user.getUserId()), false);
					}
				}
			}			
											
			// send the notification										
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageCostKey, messageCostObjects, subjectCostKey, subjectCostObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationCostDelete - END");
	}

}
