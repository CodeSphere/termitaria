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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.SearchActivityBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class ActivitySearchController extends RootSimpleFormController{
	
		
	//------------------------VIEW------------------------------------------------------------------	
	private static final String FORM_VIEW						= "Activity_Search";
	private static final String SUCCESS_VIEW					= "Activity_Listing";
		
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY						= "activity.";		
	private static final String GENERAL_ERROR					= ROOT_KEY.concat("general.error");	
	private static final String SEARCH_ERROR 					= ROOT_KEY.concat("search.error");		
	private static final String DELETE_ERROR					= ROOT_KEY.concat("delete.error");
	private static final String DELETE_SUCCESS					= ROOT_KEY.concat("delete.success");	
	private static final String GET_PROJECTS_FROM_ORG_ERROR		= ROOT_KEY.concat("get.projects.from.organisation.error");	
	private static final String GET_PROJECTS_FOR_MANGER_ERROR	= ROOT_KEY.concat("get.projects.for.manager.error");
	private static final String DELETE_NOT_ACTIVITY				= ROOT_KEY.concat("delete.not.activity.error");	
	private static final String HAS_ASSOCIATED_RECORD			= ROOT_KEY.concat("has.associated.record.error");
	private final String GET_PROJECT_ERROR						= "project.get.error";
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------	
	private static final String SEARCH_RESULTS 					= "SEARCH_RESULTS";
	private static final String PAGES 							="pagination.pages";		
	private static final String PAGINATION_ERROR 				= ROOT_KEY.concat("pagination.error");
	private static final String SEARCH_ACTIVITY_BEAN 			= "searchActivityBean";	
	private static final String COMMAND 						= "command";
	private static final String PROJECTS 						= "PROJECTS";	
	private static final String USER_ID							= "USER_ID";	
	private static final String ACTIVITY_ID						= "activityId";
	private static final String IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT = "IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String PAGINATION 					= "pagination";
	private static final String ACTION 						= "action";
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";		
	
	private static final String DELETE_FROM_ACTIVITY_FORM 	= "DELETE_FROM_ACTIVITY_FORM";
	private static final String DELETE_ALL					= "DELETE_ALL";
	
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 45;
	
			
	public ActivitySearchController() {
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(SEARCH_ACTIVITY_BEAN);
		setCommandClass(SearchActivityBean.class);
	}
	
	/**
	 * @author Adelina
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception{
		logger.debug("formBackingObject - START");
		SearchActivityBean searchActivityBean = new SearchActivityBean();
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//we set the initial search parameters
		searchActivityBean.setSortDirection(IConstant.ASCENDING);
		searchActivityBean.setSortParam("name");
		
		searchActivityBean.setOrganizationId(userAuth.getOrganisationId());
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer activityId = ServletRequestUtils.getIntParameter(request, ACTIVITY_ID);
		
		// deletes an activity if the request comes from activity form
		if(action != null && DELETE_FROM_ACTIVITY_FORM.equals(action) && activityId != null) {
			handleDeleteFromActivityForm(request, searchActivityBean, activityId);
		}
							
		logger.debug("formBackingObject - END");
		return searchActivityBean;
		
	}
	
	/**
	 * @author Adelina
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		logger.debug("onSubmit - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		boolean isDeleteAction = false;
		
		// the organizationId
		Integer organizationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		logger.debug("organizationId = " + organizationId);	
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
		
		//get the projects from organisation		
		List<Project> projects = new ArrayList<Project>();				
					
		if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAdvancedSearch())) {	
			logger.debug("getAllProjects");
			try{
				projects = BLProject.getInstance().getAllProjects(organizationId, true);
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
			logger.debug("getProjectsByManager");
			try{
				projects = BLProject.getInstance().getProjectsByPerson(userAuth.getPersonId());
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
		
		logger.debug("nr of projects = " + projects.size() + ", projects = " + projects);
		
		Set<Integer> projectIds = new HashSet<Integer>();
		if(projects != null && projects.size() > 0) {
			for(Project project : projects) {
				projectIds.add(project.getProjectId());
			}		
			projectIds.add(new Integer(-1));
		} else {
			projectIds.add(new Integer(-1));
		}	
		
		try {	
			
			// checks if i have a delete action
			if(request.getParameter(ACTION) != null && DELETE_ALL.equals(request.getParameter(ACTION))) {
				mav = handleDeleteAll(request, command);
				isDeleteAction = true;
			}
			
			if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
				mav = handlePagination(request, errorMessages, command, projectIds, organizationId);
			} else {
				mav = handleSearch(request, errorMessages, command, isDeleteAction, projectIds, organizationId);
			}	
			
			
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("onSubmit - END");
		
		return mav;
	}
	
	/**
	 * Search for the requested parameters
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param errorMessages
	 * @param command
	 * @param isDeleteAction
	 * @param projectIds
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request,
			ArrayList<String> errorMessages, Object command, boolean isDeleteAction, Set<Integer> projectIds, Integer organizationId)
			throws BusinessException {
		logger.debug("handleSearch - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchActivityBean searchActivityBean = (SearchActivityBean) command;
		
		List<Activity> res = null;
		
		try{
			
			res = BLActivity.getInstance().getFromSearch(searchActivityBean, projectIds, organizationId, isDeleteAction, messageSource);
			
			for(Activity activity : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(activity.getName(), NR_CHARS_PANEL_HEADER);
				activity.setPanelHeaderName(panelHeaderName);
			}
					
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);

			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);

			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));

		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		
		logger.debug("res = " + res);
		mav.addObject(SEARCH_RESULTS, res);
				
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchActivityBean, PAGES);

		mav.addObject(SEARCH_ACTIVITY_BEAN, searchActivityBean);
		mav.addObject(COMMAND, command);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		// adding to mav
		mav.addObject(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
		
		logger.debug("handleSearch - END");
		
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request,
			ArrayList<String> errorMessages, Object command, Set<Integer> projectIds, Integer organizationId)
			throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchActivityBean searchActivityBean = (SearchActivityBean) command;
		logger.debug("search project bean = " + searchActivityBean);

		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchActivityBean.setCurrentPage(searchActivityBean
							.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchActivityBean.setCurrentPage(searchActivityBean
							.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchActivityBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchActivityBean.setCurrentPage(searchActivityBean
							.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null
							&& !"".equals(request.getParameter(PAGE_NBR))) {
						searchActivityBean.setCurrentPage(Integer
								.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchActivityBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchActivityBean.setCurrentPage(-1);
		}

		List<Activity> res = null;

		try {
			res = BLActivity.getInstance().getFromSearch(searchActivityBean, projectIds, organizationId, false, messageSource);
			
			for(Activity activity : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(activity.getName(), NR_CHARS_PANEL_HEADER);
				activity.setPanelHeaderName(panelHeaderName);
			}	
			
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);

			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);

			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));

		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, res);
				
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchActivityBean, PAGES);		

		mav.addObject(SEARCH_ACTIVITY_BEAN, searchActivityBean);
		mav.addObject(COMMAND, command);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		// adding to model
		mav.addObject(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
		
		logger.debug("handlePagination - END");
		return mav;
	}	
	
	/**
	 * Deletes an activity
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchActivityBean
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchActivityBean searchActivityBean) throws BusinessException{
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
						errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
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
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] {activityNameMessage}, RequestContextUtils.getLocale(request)));
						
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
	

	/**
	 * Deletets an activity or all the activities
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleDeleteAll(HttpServletRequest request, Object command) throws BusinessException {
		
		logger.debug("handleDeleteAll - START");		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchActivityBean searchActivityBean = (SearchActivityBean) command;
		
		handleDeleteAllSimple(request, searchActivityBean);
		
		logger.debug("Results per page = " + searchActivityBean.getResultsPerPage());
		
		logger.debug("handleDeleteAll - END");
		return mav;
	}
	
	/**
	 * Deletes an activity that comes from an activity form
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchActivityBean
	 * @param activityId
	 * @throws BusinessException
	 */
	private void handleDeleteFromActivityForm(HttpServletRequest request, SearchActivityBean searchActivityBean, Integer activityId) throws BusinessException {
		logger.debug("handleDeleteFromActivityForm - START");
		
		Integer[] activities = new Integer[1];
		activities[0] = activityId;
		searchActivityBean.setActivityId(activities);		
		handleDeleteAllSimple(request, searchActivityBean);
		
		logger.debug("handleDeleteFromActivityForm - END");
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
			
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception{
		logger.debug("referenceData - START");
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Map map = new HashMap();	
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
		Integer personId = userAuth.getPersonId();
		
		//load the list of possible results per page
		map.put(IConstant.NOM_RESULTS_PER_PAGE, TSContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(personId, request, errorMessages);	
		
		// adding to model
		map.put(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
				
		Integer organizationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		logger.debug("organizationId = " + organizationId);
							
		//get the projects from organisation		
		List<Project> projects = new ArrayList<Project>();				
		
		if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAdvancedSearch())) {	
			logger.debug("getAllProjects");
			try{
				projects = BLProject.getInstance().getAllProjects(organizationId, true);
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
			logger.debug("getProjectsByManager");
			try{
				projects = BLProject.getInstance().getProjectsByManager(personId, false, true);
				logger.debug("projects =  " + projects);
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
		
		// adding to model the projects from organization
		map.put(PROJECTS, projects);	
							
		// adding to model the billable nomenclator
		map.put(IConstant.NOM_BILLABLE, TSContext.getFromContext(IConstant.NOM_BILLABLE));			
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		
		return map;
	}
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException, java.util.Map)
	 */
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {	
		return super.showForm(request, errors, getFormView(), controlModel);
	}
	
	/**
	 * Sends the notification when the activity is deleted
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
	
}
