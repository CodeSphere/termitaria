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

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

/**
 * 
 * @author Adelina
 *
 */

public class ActivityController extends RootSimpleFormController {
	
	private static final String SUCCESS_VIEW 				= "Activity";
	private static final String FORM_VIEW 					= "Activity";
				
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY 					= "activity.";
	private final String ADD_SUCCESS						= ROOT_KEY.concat("add.success");
	private final String ADD_ERROR							= ROOT_KEY.concat("add.error");
	private final String GET_ERROR							= ROOT_KEY.concat("get.error");	
	private final String UPDATE_SUCCESS						= ROOT_KEY.concat("update.success");
	private final String UPDATE_ERROR						= ROOT_KEY.concat("update.error");
	private final String GENERAL_ERROR						= ROOT_KEY.concat("general.error");		
	private final String GET_PROJECTS_FROM_ORG_ERROR		= ROOT_KEY.concat("get.projects.from.organization.error");
	private final String GET_PROJECTS_FOR_MANGER_ERROR		= ROOT_KEY.concat("get.projects.for.manager.error");
	private static final String GET_ORG_CURRENCIES_ERROR 	= ROOT_KEY.concat("get.org.currencies.error");
	private final String GET_PROJECT_ERROR					= "project.get.error";
	private final String GET_PROJECT_DETAILS_ERROR			= "project.details.get.error";	
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private final String ACTIVITY_ID						= "activityId";	
	private final String PROJECT_ID							= "projectId";		
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------			
	private static final String PROJECTS 					= "PROJECTS";		
	private static final String ORGANIZATION_ID				= "ORGANIZATION_ID";	
	private static final String IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT = "IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT";
	private static final String ORG_CURRENCIES 				= "ORG_CURRENCIES";
	private static final String CURRENCIES					= "CURRENCIES";
	
	// Billable property nomenclator
	String NOM_BILLABLE										= "NOM_BILLABLE";
	
	// Time unit property nomenclator
	String NOM_TIME_UNIT									= "NOM_TIME_UNIT";
	
	//--------------------PARAMETERS-------------------------------------------------------------
	private static final String GET_FROM_PANEL				= "GET_FROM_PANEL";
	private static final String ONSUBMIT					= "ONSUBMIT";
	
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 				= "BACK_URL";
	private static final String NEXT_BACK_URL			= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 		= "ENCODE_BACK_URL";
		
	public ActivityController() {
		
		setCommandName("activityBean");
		setCommandClass(Activity.class);
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
		Activity activity = new Activity();
		
		try{
			
			// backing object id
			Integer activityId = ServletRequestUtils.getIntParameter(request, ACTIVITY_ID);						
			
			Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);						
			
			// redirecting regarding the operation
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			logger.debug("action = ".concat(action));
			
			if(action != null) {
				// check if i have to edit an activity
				if(IConstant.CMD_GET.equals(action)) {
					logger.debug("formBackingObject : Get");
					activity = handleGet(activityId, projectId, request, errorMessages, infoMessages, locale);
				} else if(IConstant.CMD_ADD.equals(action)) { // or add a new activity
					logger.debug("formBackingObject : New");
					activity = new Activity();
					activity.setProjectId(ServletRequestUtils.getIntParameter(request, "projectId"));
				}
			}
			
			//if the view will be rendered in a panel we display only some fields
			request.setAttribute(GET_FROM_PANEL, request.getParameter(GET_FROM_PANEL));
			
		} catch (Exception e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command ".concat(activity.toString()));
				
		return activity;
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
		Activity activity = (Activity) command;
					
		// redirecting regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = ".concat(action));
		
		if(action != null) {
			if(IConstant.CMD_SAVE.equals(action)) {
				if(activity.getActivityId() > 0) {
					mav = handleUpdate(request, response, command, errors, errorMessages, infoMessages, locale);
				} else {
					mav = handleAdd(request, response, command, errors, errorMessages, infoMessages, locale);
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
	 * Adds an activity
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
	private ModelAndView handleAdd(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getSuccessView());			
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					
		boolean isManagerForProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);			
		
		try{				
			//****************** Security *******************************			
			// if the user has permission TS_ActivityAdvancedAddUpdate, can add the activity
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ActivityAdvancedAddUpdate()) || isManagerForProject){
				
				Activity activity = manageProjectDetails((Activity) command, request, errorMessages);
				mav.addObject(getCommandName(), activity);
				
				if(activity.getProjectId() == -1) {
					activity.setBillable(IConstant.NOM_BILLABLE_NO);
				}
				
				// adding the activity to the database
				BLActivity.getInstance().add(activity);
							
				String activityName = ControllerUtils.getInstance().tokenizeField(activity.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {activityName}, locale));
					
				//send the notification
				String projectName = null;
				String message = null;
				
				if(activity.getProjectId() != -1) {
					Project project = BLProject.getInstance().get(activity.getProjectId(), true);
					if(project != null) {
						projectName = project.getName();
					}
					message = IConstant.NOTIFICATION_MESSAGE_ACTIVITY_PROJECT_ADD;
				} else {
					projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
					message = IConstant.NOTIFICATION_MESSAGE_ACTIVITY_ORG_ADD;
				}
				
				// send a notification regarding the adding of the activity
				sendNotificationActivity(activity.getProjectId(),activity.getProjectDetailId(),userAuth.getOrganisationId(),
						message,new Object[]{activity.getName(), projectName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_ACTIVITY_ADD, new Object[]{null}, IConstant.NOTIFICATION_SETTING_ACTIVITY_ADD);
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						//if the projectId is equal to -1, it means it is an activity for organization
						if (activity.getProjectId() == -1) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ACTIVITY_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_ORG_ADD_MESSAGE, new Object[] {activityName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_ORG_ADD_MESSAGE, new Object[] {activityName}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							Project project = BLProject.getInstance().get(activity.getProjectId(), true);
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ACTIVITY_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_PROJECT_ADD_MESSAGE, new Object[] {activityName, project.getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_PROJECT_ADD_MESSAGE, new Object[] {activityName, project.getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
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
		
		//the view came from onsubmit
		//we need this for displaying only the messages when adding the activity
		mav.addObject(ONSUBMIT, true);
		
		return mav;
	}
	
	/**
	 * Updates an activity
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
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
		boolean isManagerForProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);		
						
		try{
			
			//****************** Security *******************************			
			// if the user has permission TS_ActivityAdvancedAddUpdate, can update the activity
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ActivityAdvancedAddUpdate()) || isManagerForProject){
			
				Activity activity = manageProjectDetails((Activity) command, request, errorMessages);								
				mav.addObject(getCommandName(), activity);
				
				if(activity.getProjectId() == -1) {
					activity.setBillable(IConstant.NOM_BILLABLE_NO);
				}
				
				// updating the activity to the database
				BLActivity.getInstance().update(activity);
							
				String activityName = ControllerUtils.getInstance().tokenizeField(activity.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {activityName}, locale));
				
				//send the notification
				String projectName = null;
				String message = null;
				
				if(activity.getProjectId() != -1) {
					Project project = BLProject.getInstance().get(activity.getProjectId(), true);
					if(project != null) {
						projectName = project.getName();
					}
					message = IConstant.NOTIFICATION_MESSAGE_ACTIVITY_PROJECT_UPDATE;
				} else {
					projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
					message = IConstant.NOTIFICATION_MESSAGE_ACTIVITY_ORG_UPDATE;
				}
				
				// send a notification regarding the updating of the activity
				sendNotificationActivity(activity.getProjectId(),activity.getProjectDetailId(),userAuth.getOrganisationId(),
						message,new Object[]{activityName, projectName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_ACTIVITY_UPDATE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_ACTIVITY_UPDATE);
											
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						//if the projectId is equal to -1, it means it is an activity for organization
						if (activity.getProjectId() == -1) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ACTIVITY_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_ORG_UPDATE_MESSAGE, new Object[] {activityName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_ORG_UPDATE_MESSAGE, new Object[] {activityName}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							Project project = BLProject.getInstance().get(activity.getProjectId(), true);
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ACTIVITY_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {activityName, project.getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_ACTIVITY_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {activityName, project.getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
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
	 * Gets an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private Activity handleGet(Integer activityId, Integer projectId, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START");
		Activity activity = null;
		try{
			// call the get method from the business layer
			activity = BLActivity.getInstance().getWithAll(activityId);			
			if(projectId != null) {
				activity.setProjectId(projectId);
			}
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
		return activity;
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
	private Integer handleAddProjectDetails(Integer projectId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleAddProjectDetails - START");
					
		Integer projectDetailId = null;
						
		try{		

			// first create the project details entity
			ProjectDetails projectDetails = new ProjectDetails();
			projectDetails.setProjectId(projectId);
			projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);
			
			// then, add the project details to the database
			projectDetailId = BLProjectDetails.getInstance().add(projectDetails);						

		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
									        	
		logger.debug("handleAddProjectDetails - END");
		return projectDetailId;
	}	
	
	/**
	 * Manage projectDetails
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	private Activity manageProjectDetails(Activity activity , HttpServletRequest request, ArrayList<String> errorMessages) {
		
		Integer projectId = activity.getProjectId();		
		
		Integer projectDetailId = null;
		
		// if there is a project selected
		// this means that the activity is added for a given project
		// if not, the activity is added inside the company
		if(projectId != -1) {
			Project project = new Project();
			try{
				project = BLProject.getInstance().get(projectId, true);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}
			
			// if it hasn't a project detail associated, create one
			if(!project.isHasProjectDetail()) {
				projectDetailId = handleAddProjectDetails(projectId, request, errorMessages); 
			} else { // else, get the project detail that exists, that has a association with the given project
				try{
					projectDetailId = BLProjectDetails.getInstance().getByProjectId(projectId).getProjectDetailId();
				} catch (BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(GET_PROJECT_DETAILS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}
			}			
		}
										
		// set the project Detail id
		activity.setProjectDetailId(projectDetailId);
		
		return activity;
	}		
	
	/**
	 * Checks if a person is manager for at least one project
	 * 
	 * @author Adelina
	 *  
	 * @param personId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	private boolean isManagerForAtLeastOneProject(Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("isManagerForAtLeastOneProject - START");
		
		boolean isManagerForProject = false;
				
		List<Project> projects = null; 
		try{
			projects = BLProject.getInstance().getProjectsByManager(personId, true, false);
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
			
	
	/*/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		logger.debug("referenceData - START");
			
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Map map = new HashMap();
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer personId = userAuth.getPersonId();
				
		Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
		map.put(PROJECT_ID, projectId);		
		logger.debug("project id = ".concat(String.valueOf(projectId)));
						
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(personId, request, errorMessages);	
		
		// adding to model
		map.put(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
					
		
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		map.put(IConstant.REQ_ACTION, action);
		
		// adding to model the billable nomenclator
		map.put(IConstant.NOM_BILLABLE, TSContext.getFromContext(IConstant.NOM_BILLABLE));
		
		// adding to model the time unit nomenclator
		map.put(IConstant.NOM_TIME_UNIT, TSContext.getFromContext(IConstant.NOM_TIME_UNIT));
		
		//put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
		
		String servletPath = request.getServletPath();		
		String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");

		logger.debug("BACK_URL = " + backUrl);
		logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
		
		map.put(BACK_URL, backUrl);		
		map.put(NEXT_BACK_URL, nextBackUrl);	
		if(backUrl != null) {
			map.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));
		}
		
		// the id of the organization
		Integer organizationId = (Integer)request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		map.put(ORGANIZATION_ID, organizationId);
																	
		List<Project> projects = new ArrayList<Project>();
		
		// get the projects
		
		if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ActivityAdvancedAddUpdate())) {			
			try{
				projects = BLProject.getInstance().getAllProjects(organizationId, false);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_PROJECTS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}
		} else {
			try{
				projects = BLProject.getInstance().getProjectsByManager(personId, true, false);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_PROJECTS_FOR_MANGER_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}
		}
		
		map.put(PROJECTS, projects);	
		
		try {
			//add the organization available currencies
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			logger.debug("currencies = " + currencies);
			if (currencies != null && currencies.size() > 0) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for (Currency currency : currencies) {
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
	 * Sends the notification when the activity is added or updated
	 * @author alexandru.dobre
	 * 
	 * @param projectId
	 * @param projectDetailId
	 * @param organizationId
	 * @param messageCostKey
	 * @param messageCostObjects
	 * @param subjectCostKey
	 * @param subjectCostObjects
	 * @param setting
	 */
	
	public void sendNotificationActivity(Integer projectId,Integer projectDetailId, Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects, Byte setting ) {
		logger.debug("sendNotificationActivity - START, projectId = ".concat(String.valueOf(projectId)));
				
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
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageKey, messageObjects, subjectKey, subjectObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationActivity - END");
	}
	
}
