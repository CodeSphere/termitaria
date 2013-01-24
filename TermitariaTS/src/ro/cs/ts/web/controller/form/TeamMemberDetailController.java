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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class TeamMemberDetailController extends RootSimpleFormController {
	
	private static final String SUCCESS_VIEW 				= "TeamMemberDetail";
	private static final String FORM_VIEW 					= "TeamMemberDetail";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY 					= "team.member.detail.";	
	private final String ADD_ERROR							= ROOT_KEY.concat("add.error");
	private final String GET_ERROR							= ROOT_KEY.concat("get.error");		
	private final String UPDATE_SUCCESS						= ROOT_KEY.concat("update.success");
	private final String UPDATE_ERROR						= ROOT_KEY.concat("update.error");
	private final String GET_ORG_CURRENCIES_ERROR			= ROOT_KEY.concat("get.org.currencies.error");
	private final String GENERAL_ERROR						= ROOT_KEY.concat("general.error");		
	private final String GET_PROJECT_ERROR					= "project.get.error";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private final String TEAM_MEMBER_ID						= "teamMemberId";	
	private final String HAS_TEAM_MEMBER_DETAIL				= "hasTeamMemberDetail";
	private final String PROJECT_ID							= "projectId";
	private final String IS_MANAGER							= "IS_MANAGER";
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------
	private final String MODEL_TEAM_MEMBER_NAME				= "TEAM_MEMBER_NAME";	
	private final static String ORG_CURRENCIES				= "ORG_CURRENCIES";
	private final static String PROJECT						= "PROJECT";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private final static String BACK_URL 				= "BACK_URL";
	private final static String ENCODE_BACK_URL	 		= "ENCODE_BACK_URL";
	
	public TeamMemberDetailController() {
		setCommandName("teamMemberDetailBean");
		setCommandClass(TeamMemberDetail.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * It runs at every cycle
	 * 
	 * @author Adelina
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
				
		// used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		// locale 
		Locale locale = RequestContextUtils.getLocale(request);
		
		// backing object
		TeamMemberDetail teamMemberDetail = new TeamMemberDetail();
						
		try{
			// backing object id
			Integer memberId = ServletRequestUtils.getIntParameter(request, TEAM_MEMBER_ID);			
			logger.debug("memberId = ".concat(String.valueOf(memberId)));
			
			Boolean hasTeamMemberDetail = ServletRequestUtils.getBooleanParameter(request, HAS_TEAM_MEMBER_DETAIL);
			logger.debug("hasTeamMemberDetail = ".concat(String.valueOf(hasTeamMemberDetail)));
			
			Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
			logger.debug("projectId = ".concat(String.valueOf(projectId)));
			
			request.setAttribute(PROJECT_ID, projectId);
			
			// redirecting regarding the operation
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			logger.debug("action = ".concat(action));
									
			if(action != null) {
				// check if i have to edit a team member
				if(IConstant.CMD_GET.equals(action)) {
					logger.debug("formBackingObject: Get");									
					teamMemberDetail = handleGet(memberId, projectId, hasTeamMemberDetail, request, errorMessages, infoMessages, locale);
				} 
			}
		} catch (Exception e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command ".concat(teamMemberDetail.toString()));
		
		return teamMemberDetail;
	}
	
	/**
	 * It runs on every submit (request Method = POST)
	 * It detects the case by action parameter and dispatch to and appropriate handler.
	 * 
	 * @author Adelina
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		
		// for model and view
		ModelAndView mav = new ModelAndView();
		//for locale
		Locale locale = RequestContextUtils.getLocale(request);
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		// the command class
		TeamMemberDetail teamMemberDetail = (TeamMemberDetail) command;
		logger.debug("teamMemberDetail = " + teamMemberDetail);
		
		// redirecting regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = ".concat(action));
		
		if(action != null) {
			if(IConstant.CMD_SAVE.equals(action)) {
				if(teamMemberDetail.getTeamMemberDetailId() > 0) {
					mav = handleUpdate(request, response, command, errors, errorMessages, infoMessages, locale);
				} 
			} 
		}
				
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		//Add referenceData objects to model
		mav.addAllObjects(referenceData(request, command, errors));
		
		logger.debug("onSubmit - END");
		
		return mav;
	}
	

	/**
	 * Adds the team member details
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	private Integer handleAdd(Integer memberId, Integer projectId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleAdd - START");
				
		Integer teamMemberDetailId = null;
									
		try{	
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
			boolean isManager = isManager(projectId, userAuth.getPersonId(), request, errorMessages);
			
			// if the user has permission TS_ProjectAddUpdateTeamMemberDetails, can add the project's team member's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAddUpdateTeamMemberDetails()) || isManager){
				TeamMemberDetail teamMemberDetail = handleGetNew(memberId);
				// adding the team member details to the database
				teamMemberDetailId = BLTeamMemberDetail.getInstance().add(teamMemberDetail);								
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
									        	
		logger.debug("handleAdd - END");
		return teamMemberDetailId;
	}
	
	/**
	 * Updates the team member details
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleUpdate - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
									
		try{
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
			logger.debug("projectId = ".concat(String.valueOf(projectId)));
			boolean isManager = isManager(projectId, userAuth.getPersonId(), request, errorMessages);
			
			// the organization id from the session
			Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			//****************** Security *******************************			
			// if the user has permission TS_ProjectAddUpdateTeamMemberDetails, can update the project's team member's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAddUpdateTeamMemberDetails()) || isManager){
			
				TeamMemberDetail teamMemberDetail = (TeamMemberDetail) command;										
				mav.addObject(getCommandName(), teamMemberDetail);
				
				// updating the team member details to the database			
				BLTeamMemberDetail.getInstance().update(teamMemberDetail);
												
				Integer memberId = teamMemberDetail.getTeamMemberId();
			
				TeamMember teamMember = new TeamMember();					
				
				try{
					teamMember = BLTeamMember.getInstance().getTeamMember(memberId, false);					
				} catch (BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}
				
				Project project = new Project();
				Integer projectDetailsId = null;
				
				try{
					project = BLProject.getInstance().get(projectId, true);	
					if (!project.isHasProjectDetail()) {
						ProjectDetails projectDetails = new ProjectDetails();
						projectDetails.setProjectId(projectId);
						projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);
						projectDetailsId = BLProjectDetails.getInstance().add(projectDetails);
						project.setHasProjectDetail(true);
					}else {
						projectDetailsId = BLProjectDetails.getInstance().getByProjectId(project.getProjectId()).getProjectDetailId();
					}
				} catch (BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}
					
				String teamMemberName = ControllerUtils.getInstance().tokenizeField(teamMember.getFirstName().concat(" ").concat(teamMember.getLastName()), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				
				// send the notification regarding the update of the teamMember's details
				if (projectDetailsId != null)
				sendNotificationTeamMemberUpdate(projectId,projectDetailsId, organizationId, 
						IConstant.NOTIFICATION_MESSAGE_TEAM_MEMBER_DETAIL_UPDATE, 
						new Object[]{teamMemberName, project.getName(),userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_TEAM_MEMBER_DETAIL_UPDATE, new Object[]{null});
				
				//sendNotificationTeamMemberUpdate(projectId, organizationId, messageSource.getMessage(IConstant.NOTIFICATION_MESSAGE_TEAM_MEMBER_DETAIL_UPDATE, new Object[]{teamMemberName, project.getName()}, new Locale("ro")), messageSource.getMessage(IConstant.NOTIFICATION_SUBJECT_TEAM_MEMBER_DETAIL_UPDATE, new Object[]{null}, new Locale("ro")));
											
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {teamMemberName}, locale));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){						
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_TEAM_MEMBER_DETAIL_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_TEAM_MEMBER_DETAIL_UPDATE_MESSAGE, new Object[] {teamMemberName, project.getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_TEAM_MEMBER_DETAIL_UPDATE_MESSAGE, new Object[] {teamMemberName, project.getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleUpdate - END");
		
		return mav;
	}
	
	
	/**
	 * Gets the details for a team member
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private TeamMemberDetail handleGet(Integer memberId, Integer projectId, Boolean hasTeamMemberDetail, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START");
						
		if(!hasTeamMemberDetail) {								
			handleAdd(memberId, projectId, request, errorMessages);
		}
						
		TeamMemberDetail teamMemberDetail = null;
		
		try{
			// call the get method from the business layer
			teamMemberDetail = BLTeamMemberDetail.getInstance().getByTeamMemberId(memberId);				
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleGet - END");
		return teamMemberDetail;
	}	
	
	/**
	 * Gets a new team member detail
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	private TeamMemberDetail handleGetNew(Integer memberId) {
		logger.debug("handleGetNew - START");
		
		TeamMemberDetail teamMemberDetail = new TeamMemberDetail();
		teamMemberDetail.setTeamMemberId(memberId);
		teamMemberDetail.setStatus(IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_ACTIVE);			
		
		logger.debug("handleGetNew - END");
		return teamMemberDetail;
	}	
	
	/**
	 * Get a team member, identified by the memberId
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	private TeamMember handleGetTeamMember(Integer memberId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleGetTeamMember - START");
				
		// get the teamMember by memberId
		TeamMember teamMember = new TeamMember();		
		try{
			teamMember = BLTeamMember.getInstance().getTeamMember(memberId, false);			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		logger.debug("handleGetTeamMember - END, with team Member = " + teamMember);
		return teamMember;		
	}
	
	/**
	 * Checks if a specific person is the manager for a specific project
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param personId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	boolean isManager(Integer projectId, Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("isManager - START");
		
		boolean isManager = false;
		
		Project project =  null;
				
		try{
			project =  BLProject.getInstance().get(projectId, true);		
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		if(personId == project.getManagerId()) {
			isManager = true;
		}
		
		logger.debug("isManager - END, with isManager = " + isManager);
		return isManager;		

	}
	
	
	/*/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		logger.debug("referenceData - START");
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Map map = new HashMap();	
							
		// backing object id
		Integer teamMemberId = ServletRequestUtils.getIntParameter(request, TEAM_MEMBER_ID);	
		logger.debug("teamMemberId = " + teamMemberId);
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
		logger.debug("projectId = ".concat(String.valueOf(projectId)));
		boolean isManager = isManager(projectId, userAuth.getPersonId(), request, errorMessages);
		map.put(IS_MANAGER, isManager);
					
		map.put(PROJECT_ID, projectId);
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		map.put(IConstant.REQ_ACTION, action);		
		
		//put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);			
		logger.debug("BACK_URL = " + backUrl);		
		
		map.put(BACK_URL, backUrl);				
		map.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));
		
		TeamMember teamMember = handleGetTeamMember(teamMemberId, request, errorMessages);			
		
		// adding to model the team member name
		if(teamMember != null && teamMember.getMemberId() > 0) {
			map.put(MODEL_TEAM_MEMBER_NAME, teamMember.getFirstName().concat(" ").concat(teamMember.getLastName()));
		}
		
		// adding to model the time unit nomenclator
		map.put(IConstant.NOM_TIME_UNIT, TSContext.getFromContext(IConstant.NOM_TIME_UNIT));
		
		// add the available currencies
		try{
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			if(currencies != null && currencies.size() > 0) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for(Currency currency : currencies) {
					IntString entry = new IntString();
					entry.setValue(currency.getCurrencyId());
					entry.setLabel(currency.getName());
					nomCurrencies.add(entry);
				}
				map.put(ORG_CURRENCIES, nomCurrencies);
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);			
			errorMessages.add(messageSource.getMessage(GET_ORG_CURRENCIES_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		try{
			Project project = BLProject.getInstance().get(projectId, true);	
			map.put(PROJECT, project);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
							
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * Send the notification when the team member is updated
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param organizationId
	 */
	public void sendNotificationTeamMemberUpdate (Integer projectId,Integer projectDetailId, Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects) {
		logger.debug("sendNotificationTeamMemberUpdate - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
		
		try{		
			if(projectId != null && projectId > 0) {
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
			
											
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageKey, messageObjects, subjectKey, subjectObjects,IConstant.NOTIFICATION_SETTING_TEAM_MEMBER_DETAIL_UPDATE,messageSource));
			thread.start();				
		} catch (Exception e) {
			logger.debug(e);
		}
		logger.debug("sendNotificationTeamMemberUpdate - END");
	}
	
}


