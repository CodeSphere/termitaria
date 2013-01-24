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
package ro.cs.ts.web.controller.form;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCostSheet;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntObj;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class CostSheetFormController extends RootSimpleFormController {

	public static final String FORM_VIEW						= "CostSheetForm";
	public static final String SUCCESS_VIEW						= "CostSheetForm";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ID 								= "costSheetId";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String GET_ERROR 						= "costsheet.get.error";
	private static final String UPDATE_MESSAGE 					= "costsheet.update.message";
	private static final String UPDATE_ERROR 					= "costsheet.update.error";
	private static final String ADD_MESSAGE						= "costsheet.add.message";
	private static final String ADD_ERROR						= "costsheet.add.error";
	private static final String GENERAL_ERROR					= "costsheet.general.form.error";
	private static final String GET_USER_PROJECTS_ERROR 		= "costsheet.get.user.projects.error";
	private static final String GET_ORG_CURRENCIES_ERROR 		= "costsheet.get.org.currencies.error";	
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 						= "BACK_URL";
	private static final String NEXT_BACK_URL					= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 				= "ENCODE_BACK_URL";
	
	//--------------------MODEL-----------------------------------------------------------------------
	private static final String USER_PROJECTS					= "USER_PROJECTS";
	private static final String USER_PROJECTS_IS_PM_AND_MEMBERS	= "USER_PROJECTS_IS_PM_AND_MEMBERS";
	private static final String PERSON_ID						= "PERSON_ID";
	private static final String PROJECT_ID						= "PROJECT_ID";
	private static final String IS_USER_ALL						= "IS_USER_ALL";
	private static final String SHOW_BILLABLE					= "SHOW_BILLABLE";
	private static final String ORG_CURRENCIES 					= "ORG_CURRENCIES";
	private static final String TODAY_DATE						= "TODAY_DATE";
	private static final String USER_NAME						= "USER_NAME";	
		
	public CostSheetFormController(){
		setCommandName("costSheetBean");
		setCommandClass(CostSheet.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * @author Coni
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));

		logger.debug("initBinder - END");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		CostSheet costSheet = null;
		
		try {
			//check if i have to edit the cost sheet
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION) != null &&
					IConstant.CMD_GET.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION))){
				logger.debug("formBackingObject: GET");
				
				//get the cost sheet with the specified costSheetId
				costSheet = handleGet(ServletRequestUtils.getIntParameter(request, ID).intValue(), errorMessages, request);
				if (costSheet == null){
					costSheet = new CostSheet();
				}
			} else {
				logger.debug("formBackingObject: ADD");
				costSheet = new CostSheet();
				//if (IConstant.CMD_ADD.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION))) {
				costSheet.setProjectId(ServletRequestUtils.getIntParameter(request, "projectId"));

				
				//setting the organizationId for the new cost sheet
				Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
				if (organizationId != null){
					costSheet.setOrganizationId(organizationId);
				}
			}
			
			if (costSheet.getUserId() == null) {
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				costSheet.setUserId(userAuth.getPersonId());
			}
			
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		
		logger.debug("formBackingObject - END");
		return costSheet;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		CostSheet costSheet = (CostSheet) command;
		
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		// check if the cost sheet has a costSheetId
		if (costSheet.getCostSheetId() != -1){
			// if I have costSheetId, it means that I have "update" action
			logger.debug("onSubmit - handleUpdate");
			mav =  handleUpdate(costSheet, request, locale, errorMessages, infoMessages);
		} else {
			// if I don't have costSheetId, it means that I have "add" action
			logger.debug("onSubmit - handleAdd");
			mav =  handleAdd(costSheet, request, locale, errorMessages, infoMessages);
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addAllObjects(referenceData(request, costSheet, errors));
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * Gets a CostSheet with all its components
	 * 
	 * @author Coni
	 * @param costSheetId
	 * @param errorMessages
	 * @param request
	 * @return
	 */
	public CostSheet handleGet(int costSheetId, ArrayList<String> errorMessages, HttpServletRequest request) {
		logger.debug("handleGet - START");
		CostSheet costSheet = null;
		try {
			costSheet = BLCostSheet.getInstance().getAll(costSheetId);
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (costSheet != null) {
				if (costSheet.getProjectDetails() != null) {
					costSheet.setProjectId(costSheet.getProjectDetails().getProjectId());
				}
				if (costSheet.getTeamMemberDetail() != null) {
					costSheet.setUserId(costSheet.getTeamMemberDetail().getTeamMemberId());
					costSheet.setTeamMemberDetailId(costSheet.getTeamMemberDetail().getTeamMemberDetailId());
					TeamMember member = BLTeamMember.getInstance().getTeamMember(costSheet.getTeamMemberDetail().getTeamMemberId(), false);
					costSheet.setCostSheetReporterName(member.getFirstName().concat(" ").concat(member.getLastName()));					
				} else if (costSheet.getPersonDetail() != null) {
					costSheet.setUserId(costSheet.getPersonDetail().getPersonId());
					costSheet.setPersonDetailId(costSheet.getPersonDetail().getPersonDetailId());
					if (costSheet.getPersonDetail().getPersonId() == userAuth.getPersonId()) {
						costSheet.setCostSheetReporterName(userAuth.getFirstName().concat(" ").concat(userAuth.getLastName()));						
					} else {
						String[] personIds = new String[1];
						personIds[0] = String.valueOf(costSheet.getPersonDetail().getPersonId());
						List<UserSimple> users = BLUser.getInstance().getUsersByPersonId(personIds);
						costSheet.setCostSheetReporterName(users.get(0).getFirstName().concat(" ").concat(users.get(0).getLastName()));						
					}
					
				}
			}
			
			
		} catch (BusinessException be) {
			logger.error("handleGet", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("handleGet - START");
		return costSheet;
	}
	
	/**
	 * Updates the cost sheet with the specified id
	 * 
	 * @author Coni
	 * @param cost sheet
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleUpdate(CostSheet costSheet, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		try {
			
			// the organization id from the session
			Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (costSheet.getCostSheetReporterName() == null || costSheet.getCostSheetReporterName().isEmpty()){
				costSheet.setCostSheetReporterName(userAuth.getFirstName().concat(" ").concat(userAuth.getLastName()));
			}
			
			//add the command object
			mav.addObject(getCommandName(), costSheet);
					
			// if the expense is added for the organization
			// the expense is not billable
			if(costSheet.getProjectId() == -1) {
				costSheet.setBillable(IConstant.NOM_BILLABLE_NO);
			}
			
			//update the cost sheet
			BLCostSheet.getInstance().update(costSheet);
			
			String projectName = null;
			String message = null;
			
			if(costSheet.getProjectId() != -1) {
				Project project = BLProject.getInstance().get(costSheet.getProjectId(), true);
				if(project != null) {
					projectName = project.getName();
				}
				message = IConstant.NOTIFICATION_MESSAGE_COST_PROJECT_UPDATE;
			} else {
				projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
				message = IConstant.NOTIFICATION_MESSAGE_COST_UPDATE;
			}
			
			// send a notification regarding the updating of the cost
			sendNotificationCost(costSheet.getProjectId(),costSheet.getProjectDetailId(),organizationId,
					message,new Object[]{costSheet.getCostSheetId(), projectName, costSheet.getDate(), costSheet.getCostSheetReporterName(),userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
					IConstant.NOTIFICATION_SUBJECT_COST_UPDATE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_COST_UPDATE);
			//sendNotificationCost(costSheet.getProjectId(), organizationId, messageSource.getMessage(
			//		message, new Object[]{costSheet.getCostSheetId(), projectName, costSheet.getDate(), costSheet.getCostSheetReporterName()}
			//, new Locale("ro")), messageSource.getMessage(
			//		IConstant.NOTIFICATION_SUBJECT_COST_UPDATE, new Object[]{null}, 
			//		new Locale("ro")));
			
			infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, new Object[] {null}, locale));
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					if (costSheet.getProjectId().equals(IConstant.NOM_COST_SHEET_FORM_PROJECT_SELECT_ORG_OPTION)) { 
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_UPDATE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName()}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_UPDATE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName()}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					} else {
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName(), projectName}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName(), projectName}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("Exception while updating cost sheet with id: ".concat(String.valueOf(costSheet.getCostSheetId())), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error("Exception while updating cost sheet with id: ".concat(String.valueOf(costSheet.getCostSheetId())), e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} 
		
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	/**
	 * Adds a new cost sheet
	 * 
	 * @author Coni
	 * @param costSheet
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleAdd(CostSheet costSheet, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		try {
			
			// the organization id from the session
			Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (costSheet.getCostSheetReporterName() == null || costSheet.getCostSheetReporterName().isEmpty()){
				costSheet.setCostSheetReporterName(userAuth.getFirstName().concat(" ").concat(userAuth.getLastName()));
			}

			//setting the new cost sheet status to enabled
			costSheet.setStatus(IConstant.NOM_COST_SHEET_STATUS_ACTIVE);
			
			// if the expense is added for the organization
			// the expense is not billable
			if(costSheet.getProjectId() == -1) {
				costSheet.setBillable(IConstant.NOM_BILLABLE_NO);
			}
			
			costSheet = BLCostSheet.getInstance().add(costSheet);
			
			String projectName = null;
			String message = null;
			
			if(costSheet.getProjectId() != -1) {
				Project project = BLProject.getInstance().get(costSheet.getProjectId(), true);
				if(project != null) {
					projectName = project.getName();
				}
				message = IConstant.NOTIFICATION_MESSAGE_COST_PROJECT_ADD;
			} else {
				projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
				message = IConstant.NOTIFICATION_MESSAGE_COST_ADD;
			}
			
			// send a notification regarding the adding of the cost
			sendNotificationCost(costSheet.getProjectId(),costSheet.getProjectDetailId(),organizationId,
					message,new Object[]{costSheet.getCostSheetId(), projectName, costSheet.getDate(), costSheet.getCostSheetReporterName(),userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
					IConstant.NOTIFICATION_SUBJECT_COST_ADD, new Object[]{null}, IConstant.NOTIFICATION_SETTING_COST_ADD);
			//sendNotificationCost(costSheet.getProjectId(), organizationId, messageSource.getMessage(
			//		message, new Object[]{costSheet.getCostSheetId(), projectName, costSheet.getDate(), costSheet.getCostSheetReporterName()},
			//		new Locale("ro")), messageSource.getMessage(
			//		IConstant.NOTIFICATION_SUBJECT_COST_ADD, new Object[]{null},
			//		new Locale("ro")));
				
			infoMessages.add(messageSource.getMessage(ADD_MESSAGE, new Object[] {null}, locale));
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					if (costSheet.getProjectId().equals(IConstant.NOM_COST_SHEET_FORM_PROJECT_SELECT_ORG_OPTION)) { 
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_ADD_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName()}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_ADD_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName()}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					} else {
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_ADD_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName(), projectName}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_ADD_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheet.getCostSheetReporterName(), projectName}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("Exception while adding cost sheet " , be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("Exception while adding cost sheet " , e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		//add the command object
		mav.addObject(getCommandName(), costSheet);
		logger.debug("handleAdd - END");
		return mav;
	}
	
	/**
	 * Adds to model required nomenclators and data
	 * @author Coni
	 */
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
		logger.debug("referenceData - start");
		Map model = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CostSheet costSheet = (CostSheet) command;
						
		try {
			// adding to model the action from the request
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			model.put(IConstant.REQ_ACTION, action);
			
			//put the back url
			String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);

			
			String servletPath = request.getServletPath();
			String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");
			
			logger.debug("BACK_URL = " + backUrl);
			logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
			
			model.put(BACK_URL, backUrl);		
			model.put(NEXT_BACK_URL, nextBackUrl);
			if (backUrl != null) {
				model.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));	
			}
				
			//adding to model the billable nomenclator
			model.put(IConstant.NOM_BILLABLE, TSContext.getFromContext(IConstant.NOM_BILLABLE));
			
			//adding the personId
			model.put(PERSON_ID, userAuth.getPersonId());
			
			//adding the projectId
			Integer projectId = ServletRequestUtils.getIntParameter(request, "projectId");
			model.put(PROJECT_ID, projectId);
			
			//is USER_ALL
			if (userAuth.hasAuthority(PermissionConstant.getTheInstance().getTS_CostSheetAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetUpdateAll())) {
				model.put(IS_USER_ALL, true);
			} else {
				model.put(IS_USER_ALL, false);
			}
			
			//add the current date
			model.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
			model.put(USER_NAME, userAuth.getFirstName().concat(" ").concat(userAuth.getLastName()));
		} catch (Exception e) {
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		try {
			//adding the user's available project for recording a cost sheet
			//if the user has the USER_ALL role (has the permission to add or update cost sheets for all the users and projects),
			//all its organization's projects will be available
			List<Project> projects = null;
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetUpdateAll())) {
				projects = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), false);
			}
			
			//get the the projects where the user is a team member and the associated team members
			HashMap<Project,TeamMember> projectsAndMembers = BLProject.getInstance().getProjectsAndTeamMembersByPerson(userAuth.getPersonId(), true, false);		
			List<Project> userProjects = null;
			if (projectsAndMembers != null && !projectsAndMembers.isEmpty()) {
				userProjects = new ArrayList<Project>(projectsAndMembers.keySet());
			}
			
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordAddAll()) && !userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordUpdateAll())) {
				if (userProjects != null) {
					//if the user is not USER_ALL, the available projects for recording a time sheet are the projects where the user is a team member
					projects = userProjects;
				}
			}			
			
			//needed to display the billable section for edit record action, when the user is the project PM
			//or USER_ALL and the record is for a project (not per organization)
			boolean showBillable = false;
						
			List<IntObj> projectsIsPm = new ArrayList<IntObj>();
			
			if (projects != null) {
				for (Project project : projects) {										
					IntObj obj = new IntObj();
					obj.setValue(project.getProjectId());
					if (project.getManagerId().equals(userAuth.getPersonId())) {
						obj.setLabel(true);
					} else {
						obj.setLabel(false);
					}
					projectsIsPm.add(obj);
				}
				
				//if the action is to edit and the cost sheet has a projectId > -1 (not per organization cost sheet)
				if (costSheet.getProjectId() != null && !costSheet.getProjectId().equals(-1)) {
					//if the user has the USER_ALL role, i must display the billable section, 
					//otherwise only if he is the selected project PM
					if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetUpdateAll())) {
						showBillable = true;
					} else {
						for (IntObj obj : projectsIsPm) {
							if (costSheet.getProjectId().equals(obj.getValue()) && ((Boolean) obj.getLabel()).equals(true)) {
								showBillable = true;
							}
						}
					}
				}
			}
			model.put(USER_PROJECTS, projects);
			model.put(SHOW_BILLABLE, showBillable);
			
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObj = new JSONObject();
			for (IntObj obj : projectsIsPm) {
				jsonObj.accumulate("projectId", obj.getValue());
				jsonObj.accumulate("isPm", (Boolean) obj.getLabel());
				//set the member id
				if (projectsAndMembers != null) {					
					Iterator it = projectsAndMembers.entrySet().iterator();
					while (it.hasNext()) {						
						Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();						
						if (entry.getKey().getProjectId() == obj.getValue()) {
							jsonObj.accumulate("memberId", entry.getValue().getMemberId());							
						}
					}
				}									
				jsonArray.add(jsonObj);
				jsonObj.clear();
			}
			model.put(USER_PROJECTS_IS_PM_AND_MEMBERS, jsonArray.toString());
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(GET_USER_PROJECTS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
						
		try {
			//add the organization available currencies
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			if (currencies != null) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for (Currency currency : currencies) {
					IntString entry = new IntString();
					entry.setValue(currency.getCurrencyId());
					entry.setLabel(currency.getName());
					nomCurrencies.add(entry);
				}
				model.put(ORG_CURRENCIES, nomCurrencies);
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);			
			errorMessages.add(messageSource.getMessage(GET_ORG_CURRENCIES_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - end");
		return model;
	}
	
	/**
	 * Send the notification when the cost is added or updated
	 * 
	 * @author Adelina
	 * 
	 * @param action
	 * @param projectId
	 * @param organizationId
	 */
	public void sendNotificationCost(Integer projectId,Integer projectDetailId, Integer organizationId, String messageCostKey,Object[] messageCostObjects , String subjectCostKey, Object[] subjectCostObjects, Byte setting ) {
		logger.debug("sendNotificationCost - START, projectId = ".concat(String.valueOf(projectId)));
				
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
		logger.debug("sendNotificationCost - END");
	}
	
}
