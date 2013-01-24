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

import java.lang.reflect.InvocationTargetException;
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

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCostSheet;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLExchange;
import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.SearchActivityBean;
import ro.cs.ts.entity.SearchCostSheetBean;
import ro.cs.ts.entity.SearchExchangeBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;


public class ProjectDetailsController extends RootSimpleFormController {
	
	private static final String SUCCESS_VIEW 				= "ProjectDetails";
	private static final String FORM_VIEW 					= "ProjectDetails";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY 					= "project.details.";	
	private final String ADD_ERROR							= ROOT_KEY.concat("add.error");
	private final String GET_ERROR							= ROOT_KEY.concat("get.error");	
	private final String UPDATE_SUCCESS						= ROOT_KEY.concat("update.success");
	private final String UPDATE_ERROR						= ROOT_KEY.concat("update.error");
	private final String GENERAL_ERROR						= ROOT_KEY.concat("general.error");	
	private final String GET_ORG_CURRENCIES_ERROR			= ROOT_KEY.concat("get.org.currencies.error");
	private final String GET_PROJECT_ERROR					= "project.get.error";
	
	
	//------------------------MESSAGES---------------------------------------------------------------
	private static final String DELETE_ERROR_EXCHANGE		= "exchange.delete.error";
	private static final String DELETE_SUCCESS_EXCHANGE		= "exchange.delete.success";
	private static final String DELETE_ERROR_COSTSHEET		= "costsheet.delete.error";
	private static final String DELETE_SUCCESS_MULTIPLE		= "costsheet.delete.success.multiple";
	private static final String DELETE_SUCCESS_SINGLE		= "costsheet.delete.success.single";
	private static final String DELETE_ERROR_ACTIVITY		= "activity.delete.error";
	private static final String DELETE_SUCCESS_ACTIVITY		= "activity.delete.success";	
	private static final String DELETE_NOT_ACTIVITY			= "activity.delete.not.activity.error";	
	private static final String HAS_ASSOCIATED_RECORD		= "activity.has.associated.record.error";
	
	
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private final String PROJECT_ID							= "projectId";	
	private final String HAS_PROJECT_DETAIL					= "hasProjectDetail";	
	private static final String DELETE_FROM_ACTIVITY_FORM 	= "DELETE_FROM_ACTIVITY_FORM";
	private static final String DELETE_FROM_COST_SHEET_FORM = "DELETE_FROM_COSTSHEET_FORM";
	private static final String DELETE_FROM_EXCHANGE_FORM 	= "DELETE_FROM_EXCHANGE_FORM";
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------
	private final static String MODEL_PROJECT				= "PROJECT";	
	private final static String IS_MANAGER					= "IS_MANAGER";
	private final static String ORG_CURRENCIES				= "ORG_CURRENCIES";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 					= "BACK_URL";	
	private static final String NEXT_BACK_URL				= "NEXT_BACK_URL";	
		
	
	public ProjectDetailsController() {
		setCommandName("projectDetailsBean");
		setCommandClass(ProjectDetails.class);
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
		ProjectDetails projectDetails = new ProjectDetails();
						
		try{
			// backing object id
			Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
			logger.debug("projectId = ".concat(String.valueOf(projectId)));
			
			Boolean hasProjectDetail = ServletRequestUtils.getBooleanParameter(request, HAS_PROJECT_DETAIL);
			logger.debug("hasProjectDetail = ".concat(String.valueOf(hasProjectDetail)));
			
			// redirecting regarding the operation
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			logger.debug("action = ".concat(action));
									
			if(action != null) {
				// check if i have to edit a project
				if(IConstant.CMD_GET.equals(action) ) {
					logger.debug("formBackingObject: Get");									
					projectDetails = handleGet(projectId, hasProjectDetail, request, errorMessages, infoMessages, locale);
					projectDetails.setOldBudgetCurrencyId(projectDetails.getBudgetCurrencyId());
				} 
			}
			else {
				projectDetails = handleGet(projectId, hasProjectDetail, request, errorMessages, infoMessages, locale);
			}
		} catch (Exception e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		
		Integer exchangeId = ServletRequestUtils.getIntParameter(request, "exchangeId");
		SearchExchangeBean searchExchangeBean = new SearchExchangeBean();
		
		// deletes an exchange if the request comes from exchange form
		if (action != null && DELETE_FROM_EXCHANGE_FORM.equals(action) && exchangeId != null) {
			handleDeleteFromExchangeForm(request, searchExchangeBean, exchangeId, infoMessages, errorMessages);
		}
		
		
		Integer costSheetId = ServletRequestUtils.getIntParameter(request, "costSheetId");
		SearchCostSheetBean searchCostSheetBean = new SearchCostSheetBean();
		
		// deletes a cost if the request comes from cost sheet form
		if(action != null && DELETE_FROM_COST_SHEET_FORM.equals(action) && costSheetId != null) {		
			handleDeleteFromCostSheetForm(request, searchCostSheetBean, costSheetId, infoMessages, errorMessages);
		}
		
		Integer activityId = ServletRequestUtils.getIntParameter(request, "activityId");
		SearchActivityBean searchActivityBean = new SearchActivityBean();
		
		// deletes an activity if the request comes from activity form
		if(action != null && DELETE_FROM_ACTIVITY_FORM.equals(action) && activityId != null) {
			handleDeleteFromActivityForm(request, searchActivityBean, activityId);
		}
		
		
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command ".concat(projectDetails.toString()));
		
		return projectDetails;
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
		ProjectDetails projectDetails = (ProjectDetails) command;
		logger.debug("projectDetails = " + projectDetails);
		
		// redirecting regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = ".concat(action));
		
		if(action != null) {
			if(IConstant.CMD_SAVE.equals(action)) {
				if(projectDetails.getProjectDetailId() > 0) {
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
	 * Adds the project details
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @throws Exception
	 * @return
	 */
	private Integer handleAdd(Integer projectId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleAdd - START");
		
		Integer projectDetailId = null;
		
		try{
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			boolean isManager = isManager(projectId, userAuth.getPersonId(), request, errorMessages);
			
			//****************** Security *******************************			
			// if the user has permission TS_ProjectAddUpdateProjectDetails, can add the project's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAddUpdateProjectDetails()) || isManager){
				ProjectDetails projectDetails = handleGetNew(projectId);				
				// adding the project details to the database
				projectDetailId = BLProjectDetails.getInstance().add(projectDetails);	
			}  else {
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
		return projectDetailId;
	}
	
	/**
	 * Updates the project details
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
		
		ProjectDetails projectDetails = (ProjectDetails) command;
		
		Float oldBudget = new Float(0);
		Short oldNotificationPercentage = 0;
								
		try{
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();					
			boolean isManager = isManager(projectDetails.getProjectId(), userAuth.getPersonId(), request, errorMessages);
			
			//****************** Security *******************************			
			// if the user has permission TS_ProjectAddUpdateProjectDetails, can update the project's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAddUpdateProjectDetails()) || isManager){
				
				mav.addObject(getCommandName(), projectDetails);
				
				logger.debug("projectDetails from command = " + projectDetails);
				ProjectDetails prjDetail = BLProjectDetails.getInstance().get(projectDetails.getProjectDetailId());
				logger.debug("prjDetail from database = " + prjDetail);
															
				// if the budget from the form is different from that from that from the database, set the old budget
				if(projectDetails.getBudget() != null && prjDetail.getBudget() != null && !(projectDetails.getBudget().equals(prjDetail.getBudget()))) {
					oldBudget = prjDetail.getBudget();					
				} else {
					oldBudget = new Float(0);
				}
				
				logger.debug("old budget = " + oldBudget);
				
				// if the notificationPercentage from the form is different from that from the database, set the old notification percentage
				if(projectDetails.getNotificationPercentage() != null && prjDetail.getNotificationPercentage() != null && !(projectDetails.getNotificationPercentage().equals(prjDetail.getNotificationPercentage()))) {
					oldNotificationPercentage = prjDetail.getNotificationPercentage();					
				} else {
					oldNotificationPercentage = 0;
				}
				
				logger.debug("old notification percentage = " + oldNotificationPercentage);
												
				if(prjDetail.getNotificationStatus() != null && prjDetail.getNotificationStatus() == IConstant.NOM_PROJECT_NOTIFICATION_STATUS_BUDGET_OVERFLOW) {
					if(oldBudget > 0) {						
						projectDetails.setNotificationStatus(IConstant.NOM_PROJECT_NOTIFICATION_STATUS_NOT_SENT);
					}
				} else if(prjDetail.getNotificationStatus() != null && prjDetail.getNotificationStatus() == IConstant.NOM_PROJECT_NOTIFICATION_STATUS_NOTIFICATION_PERCENTAGE) {
					if(oldBudget > 0 || oldNotificationPercentage > 0) {												
						projectDetails.setNotificationStatus(IConstant.NOM_PROJECT_NOTIFICATION_STATUS_NOT_SENT);
					}
				}
				
				logger.debug("projectDetails = " + projectDetails);
				
				// updating the project details to the database
				BLProjectDetails.getInstance().update(projectDetails);
				
				Integer projectId = projectDetails.getProjectId();
				Project project = handleGetProject(projectId, false, request, errorMessages);
						
				String projectName = ControllerUtils.getInstance().tokenizeField(project.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {projectName}, locale));
				
				// send the notification regarding the update of the project details update
				
				sendNotificationProjectDetailUpdate(projectId,projectDetails.getProjectDetailId(),userAuth.getOrganisationId(), 
						IConstant.NOTIFICATION_MESSAGE_PROJECT_DETAIL_UPDATE, new Object[]{projectName, userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_PROJECT_DETAIL_UPDATE, new Object[]{null});
				
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PROJECT_DETAIL_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_DETAIL_UPDATE_MESSAGE, new Object[] {projectName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_PROJECT_DETAIL_UPDATE_MESSAGE, new Object[] {projectName}, new Locale("ro")),  
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
	 * Gets a project details
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private ProjectDetails handleGet(Integer projectId, Boolean hasProjectDetail, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START");
						
		if(!hasProjectDetail) {								
			handleAdd(projectId, request, errorMessages);
		}
						
		ProjectDetails projectDetails = null;
		
		try{
			// call the get method from the business layer
			projectDetails = BLProjectDetails.getInstance().getByProjectId(projectId);	
			
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
		return projectDetails;
	}	
	
	/**
	 * Gets a new project detail
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	private ProjectDetails handleGetNew(Integer projectId) {
		logger.debug("handleGetNew - START");
		
		ProjectDetails projectDetails = new ProjectDetails();
		projectDetails.setProjectId(projectId);
		projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);
		
		logger.debug("handleGetNew - END");
		return projectDetails;
	}	
	
	/**
	 * Get a project, identified by the projectId
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	private Project handleGetProject(Integer projectId, boolean withAll, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleGetProject - START");
				
		// get the project by projectId
		Project project = new Project();
		try{
			if(withAll) {
				project = BLProject.getInstance().getProjectWithAll(projectId);
			} else {
				project = BLProject.getInstance().getSimpleProject(projectId);
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
		
		logger.debug("handleGetProject - END, with project = " + project);
		return project;					
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
		
		Project project = handleGetProject(projectId, false, request, errorMessages);
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
		Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
		logger.debug("projectId = ".concat(String.valueOf(projectId)));
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
		boolean isManager = isManager(projectId, userAuth.getPersonId(), request, errorMessages);
		map.put(IS_MANAGER, isManager);
							
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		map.put(IConstant.REQ_ACTION, action);		
		
		//put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
		
		String servletPath = request.getServletPath();		
		String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");

		logger.debug("BACK_URL = " + backUrl);
		logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
		
		map.put(BACK_URL, backUrl);		
		map.put(NEXT_BACK_URL, nextBackUrl);	
					
		Project project = handleGetProject(projectId, true, request, errorMessages);
		
		// adding to model the project name
		if(project != null && project.getProjectId() > 0) {
			map.put(MODEL_PROJECT, project);
		}
		
		
		
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
					
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * Send the notification when the project detail is updated
	 * 
	 * @author alexandru.dobre
	 * 
	 * @param projectId
	 * @param projectDetailId
	 * @param organizationId
	 * @param messageKey
	 * @param messageObjects
	 * @param subjectKey
	 * @param subjectObjects
	 */
	public void sendNotificationProjectDetailUpdate (Integer projectId,Integer projectDetailId, Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects) {
		logger.debug("sendNotificationProjectDetailUpdate - START, projectId = ".concat(String.valueOf(projectId)));
				
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
			
											
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageKey, messageObjects, subjectKey, subjectObjects,IConstant.NOTIFICATION_SETTING_PROJECT_DETAIL_UPDATE,messageSource));
			thread.start();				
		} catch (Exception e) {
			logger.debug(e);
		}
		logger.debug("sendNotificationProjectDetailUpdate - END");
	}
	
	private void handleDeleteFromExchangeForm(HttpServletRequest request, SearchExchangeBean searchExchangeBean, Integer exchangeId, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException{						
		logger.debug("handleDeleteFromExchangeForm - START - ");
		
		Integer[] exchangeIds = new Integer[1];
		exchangeIds[0] = exchangeId;			
		searchExchangeBean.setExchangeId(exchangeIds);
		handleDeleteAllSimpleExchange(request, searchExchangeBean, infoMessages, errorMessages);
				
		logger.debug("handleDeleteFromExchangeForm - END - ");
	}
	
	private void handleDeleteAllSimpleExchange(HttpServletRequest request, SearchExchangeBean searchExchangeBean, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Exchange exchange = null;
		
		for (int i = 0; i < searchExchangeBean.getExchangeId().length; i++) {
			logger.debug("Delete exchange : " + searchExchangeBean.getExchangeId()[i]);	
			boolean isDeleted = true;
 		
			try {
				exchange = BLExchange.getInstance().delete(searchExchangeBean.getExchangeId()[i]);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(DELETE_ERROR_EXCHANGE, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
				isDeleted = false;
			}
					
			if (isDeleted) {
				//send notification
				String projectName = null;
				String message = null;	
				Integer projectId = null;
				
				if(exchange != null) {
					if(exchange.getProjectDetail()!=  null ) {
						Project project = BLProject.getInstance().get(exchange.getProjectDetail().getProjectId(), true);
						projectId = exchange.getProjectDetail().getProjectId();
						if(project != null) {
							projectName = project.getName();
						}					
						message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_PROJECT_DELETE;
					} else {
						projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
						message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_ORG_DELETE;
					}
				}
				
				// send a notification regarding the deleting of the exchange
				sendNotificationExchangeDelete(projectId,exchange.getProjectDetailId(),userAuth.getOrganisationId(),
						message,new Object[]{exchange.getFirstCurrency().getName(),exchange.getSecondCurrency().getName(), projectName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_EXCHANGE_DELETE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_EXCHANGE_DELETE);
				
				
				infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_EXCHANGE, new Object[] { exchange.getFirstCurrency().getInitials().concat(" - ").concat(exchange.getSecondCurrency().getInitials()) }, RequestContextUtils.getLocale(request)));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (exchange.getProjectDetailId() == null) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							Project project = BLProject.getInstance().get(exchange.getProjectId(), true);
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName(), project.getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName(), project.getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			}
		}
		logger.debug("handleDeleteAllSimple - END ");
	}

	public void sendNotificationExchangeDelete(Integer projectId,Integer projectDetailId, Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects, Byte setting ) {
		logger.debug("sendNotificationExchangeDelete - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
						
		try{										
			if(projectId != null) {
				
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
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageKey, messageObjects, subjectKey, subjectObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationExchangeDelete - END");
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
	
	private void handleDeleteFromActivityForm(HttpServletRequest request, SearchActivityBean searchActivityBean, Integer activityId) throws BusinessException {
		logger.debug("handleDeleteFromActivityForm - START");
		
		Integer[] activities = new Integer[1];
		activities[0] = activityId;
		searchActivityBean.setActivityId(activities);		
		handleDeleteAllSimpleActivity(request, searchActivityBean);
		
		logger.debug("handleDeleteFromActivityForm - END");
	}
	
	private void handleDeleteAllSimpleActivity(HttpServletRequest request, SearchActivityBean searchActivityBean) throws BusinessException{
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		ArrayList<String> warningMessages = new ArrayList<String>();
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		logger.debug("searchActivityBean = " + searchActivityBean);
		logger.debug("start deleting " + searchActivityBean.getActivityId().length + " activities.");
		
		Activity activity = null;
		boolean hasAssociatedRecord = false;
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		//****************** Security *******************************			
		// if the user has permission TS_ActivityDelete, can deletes an activity
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ActivityDelete()) || isManagerForAtLeasOneProject) {
			for(int i = 0; i < searchActivityBean.getActivityId().length; i++) {
				logger.debug("Delete activity : " + searchActivityBean.getActivityId()[i]);
				boolean isDeleted = true;
				
				try{
					hasAssociatedRecord = BLActivity.getInstance().hasActivityAssociateRecord(searchActivityBean.getActivityId()[i]);
					logger.debug("hasAssociatedRecord = " + hasAssociatedRecord);
					
				} catch(BusinessException be){
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(HAS_ASSOCIATED_RECORD, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
					isDeleted = false;
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}	
				
				if(hasAssociatedRecord == false) {						
					
					try {
						activity = BLActivity.getInstance().deleteAll(searchActivityBean.getActivityId()[i]); 																					
					} catch(BusinessException be){
						logger.error("", be);
						errorMessages.add(messageSource.getMessage(DELETE_ERROR_ACTIVITY, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
						isDeleted = false;
					} catch (Exception e){
						logger.error("", e);
						errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
								.getLocale(request)));
					}	
					if(isDeleted){
						//send notification
						String projectName = null;
						String message = null;	
						Integer projectId = null;
						
						if(activity != null) {
							if(activity.getProjectDetailId() != null) {
								ProjectDetails projectDetails = BLProjectDetails.getInstance().get(activity.getProjectDetailId());
								Project project = BLProject.getInstance().get(projectDetails.getProjectId(), true);
								projectId = projectDetails.getProjectId();
								if(project != null) {
									projectName = project.getName();
								}					
								message = IConstant.NOTIFICATION_MESSAGE_ACTIVITY_PROJECT_DELETE;
							} else {
								projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
								message = IConstant.NOTIFICATION_MESSAGE_ACTIVITY_ORG_DELETE;
							}
						}
						// send a notification regarding the deleting of the activity
						sendNotificationActivityDelete(projectId,activity.getProjectDetailId(),userAuth.getOrganisationId(),
								message,new Object[]{activity.getName(), projectName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
								IConstant.NOTIFICATION_SUBJECT_ACTIVITY_DELETE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_ACTIVITY_DELETE);
						
						
						String activityNameMessage = ControllerUtils.getInstance().tokenizeField(activity.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_ACTIVITY, new Object[] {activityNameMessage}, RequestContextUtils.getLocale(request)));
						
						//add the new audit event only if the user is not AdminIT
						try {
							if (!userAuth.isAdminIT()){
								//if the projectDetailId is null, it means it is an activity for organization
								if (activity.getProjectDetailId() == null) { 
									BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ACTIVITY_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
											messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_ORG_DELETE_MESSAGE, new Object[] {activityNameMessage}, new Locale("en")),
											messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_ORG_DELETE_MESSAGE, new Object[] {activityNameMessage}, new Locale("ro")),  
											ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
								} else {
									Project project = BLProject.getInstance().get(BLProjectDetails.getInstance().get(activity.getProjectDetailId()).getProjectId(), true);
									BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ACTIVITY_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
											messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_PROJECT_DELETE_MESSAGE, new Object[] {activityNameMessage, project.getName()}, new Locale("en")),
											messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_PROJECT_DELETE_MESSAGE, new Object[] {activityNameMessage, project.getName()}, new Locale("ro")),  
											ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
								}
							}
						} catch (Exception exc) {
							logger.error("", exc);
						}
					}	
				} else {
					warningMessages.add(messageSource.getMessage(DELETE_NOT_ACTIVITY, new Object[] {BLActivity.getInstance().get(searchActivityBean.getActivityId()[i]).getName()}, RequestContextUtils.getLocale(request)));
				}	
			}
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}
			
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		setWarnings(request, warningMessages);
		
		logger.debug("handleDeleteAllSimple - END -");
	}
	
	public void sendNotificationActivityDelete(Integer projectId,Integer projectDetailId, Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects, Byte setting ) {
		logger.debug("sendNotificationActivityDelete - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
						
		try{										
			if(projectId != null) {
				
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
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageKey, messageObjects, subjectKey, subjectObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationActivityDelete - END");
	}
	
	private boolean isManagerForAtLeastOneProject(Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("isManagerForAtLeastOneProject - START");
		
		boolean isManagerForProject = false;
				
		List<Project> projects = null; 
		try{
			projects = BLProject.getInstance().getProjectsByManager(personId, true, true);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		if(projects != null && projects.size() > 0) {
			isManagerForProject = true;
		}
		
		logger.debug("isManagerForAtLeastOneProject - END");
		
		return isManagerForProject;
	}
	
}

