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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLNotificationSettings;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.NotificationSettingsFormBean;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;

/**
 * Responsible for the Notification Settings view
 * @author alexandru.dobre
 *
 */

public class NotificationSettingsController extends RootSimpleFormController {
	
	private static final String FORM_VIEW 							= "Notification_Settings";
	private static final String SUCCESS_VIEW 						= "Notification_Settings";
	private static final String NOTIFICATION_SETTINGS_FORM_BEAN 	= "notificationSettingsFormBean";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String GENERAL_ERROR				= "notification.settings.general.error";	
	private static final String PROJECT_LIST_ERROR 			= "notification.settings.project.list.error";
	private static final String UPDATE_ERROR				= "notification.settings.update.error";
	private static final String UPDATE_SUCCESS				= "notification.settings.update.success";
	private static final String GET_PROJECT_DETAILS_ERROR   = "project.details.get.error";
	private static final String GET_PROJECT_ERROR			= "project.get.error";	
	
	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String AVAILABLE_PROJECTS 				= "AVAILABLE_PROJECTS";
	private static final String PROJECT_ID						= "PROJECT_ID";
	
	
	public NotificationSettingsController (){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(NOTIFICATION_SETTINGS_FORM_BEAN);
		setCommandClass(NotificationSettingsFormBean.class);
	}
	
	
    @Override
    protected Object formBackingObject(HttpServletRequest request)
    		throws Exception {
    	logger.debug("formBackingObject - START");
    	ArrayList<String> errorMessages  	= new ArrayList<String>();
    	NotificationSettingsFormBean formBean = new NotificationSettingsFormBean();
    	
    	UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	formBean.setUserId(userAuth.getPersonId());
    	formBean.setOrganizationId(userAuth.getOrganisationId());
    	
    	if (request.getParameter(PROJECT_ID) != null){
    		Integer projectId= new Integer (request.getParameter(PROJECT_ID));
    		formBean.setProjectId(projectId);
    		if (projectId.intValue() != -2){
    			if (projectId.intValue() != -1){//not from organization
    				formBean.setProjectDetailId( manageProjectDetails(projectId, request, errorMessages)); 
    			}			
    			populateSettings(formBean,request,errorMessages); 			
    		}
    	}else {
    		formBean.setProjectId(-2);
    	}
    	
    	setErrors(request, errorMessages);
    	logger.debug("formBackingObject - END");
    	return formBean;
    }
    
    @SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
    	
    	logger.debug("referenceData - START");
    	
    	Map map = new HashMap();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		//adding available projects
		map.put(AVAILABLE_PROJECTS,buildAuthorizedProjectList(request, errorMessages));
		
		setErrors(request, errorMessages);
			
		logger.debug("referenceData - END");
    	return map;
    }
    
    
    @Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		
		// for model and view
		ModelAndView mav = new ModelAndView(getSuccessView());
		//for locale
		Locale locale = RequestContextUtils.getLocale(request);
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		NotificationSettingsFormBean formBean = (NotificationSettingsFormBean)command;
		
		logger.debug(formBean);
		
		if (formBean.getProjectId().intValue() != -2){		
			mav = handleUpdate(request, response, formBean, errors, errorMessages, infoMessages, locale);
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
	 * Updates the settings
	 * 
	 * @author alexandru.dobre
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
	private ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, NotificationSettingsFormBean formBean, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject(getCommandName(), formBean);
		
		try {
			
			BLNotificationSettings.getInstance().updateSettings(formBean);
			infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS,null,locale));
			
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
    
    private List <Project> buildAuthorizedProjectList (HttpServletRequest request, List<String> errorMessages){
    	
    	List <Project> authorizedProjectList = new ArrayList<Project>();
    	
    try{	
    	logger.debug("buildAuthorizedProjectList START>");
    	
    	UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	//if user has the reports permission all projects from organization are available
    	if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_NotificationReceive())){
    		logger.debug("User has getTS_NotificationReceive privilege, returning all projects in organisation");
    		authorizedProjectList = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), true);
    	}else {//otherwise only the projects for which he is PM are available
    		logger.debug("User is PM returning his projects");
    		authorizedProjectList =  BLProject.getInstance().getProjectsByManager(userAuth.getPersonId(), true, true);
    	}  	
    	
    } catch (BusinessException be) {
		logger.error("", be);			
		errorMessages.add(messageSource.getMessage(PROJECT_LIST_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));			
	} catch (Exception e) {
		logger.error("", e);
		errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
	}
		logger.debug("buildAuthorizedProjectList <END");
    	return authorizedProjectList;
    }
    
    private Integer manageProjectDetails (Integer projectId , HttpServletRequest request, ArrayList<String> errorMessages){
    	logger.debug("manageProjectDetails - START");
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
		logger.debug("manageProjectDetails - END");
		return projectDetailId;
    }
    
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
			errorMessages.add(messageSource.getMessage(GET_PROJECT_DETAILS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
									        	
		logger.debug("handleAddProjectDetails - END");
		return projectDetailId;
	}
	
	private void populateSettings (NotificationSettingsFormBean formBean, HttpServletRequest request, ArrayList<String> errorMessages){
		logger.debug("populateSettings - START");
		try {
			if (BLNotificationSettings.getInstance().hasSettings(formBean.getProjectDetailId(), formBean.getUserId(), formBean.getOrganizationId())){
				BLNotificationSettings.getInstance().getNotificationSettings(formBean);
			}else {
				boolean isPm;
				if (formBean.getProjectDetailId() == null){
					isPm =false;
				}else {
					UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Project project = BLProject.getInstance().getSimpleProject(formBean.getProjectId());				
					Integer managerId = project.getManagerId();
					
					if (userAuth.getPersonId() == managerId.intValue()){
						isPm = true;
					}else {
						isPm = false;
					}

				}
				
				BLNotificationSettings.getInstance().addDefaultSettings(formBean.getProjectDetailId(), formBean.getUserId(), formBean.getOrganizationId(),isPm);
				BLNotificationSettings.getInstance().getNotificationSettings(formBean);
	    		
			}
			
		}catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PROJECT_DETAILS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
	
		
		
		logger.debug("populateSettings - END");
	}

}
