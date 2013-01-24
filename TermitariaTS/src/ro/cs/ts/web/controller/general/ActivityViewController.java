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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;

public class ActivityViewController extends RootAbstractController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "activity.";		
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "ActivityView";	

	//------------------------MODEL------------------------------------------------------------------	
	private static final String ACTIVITY_ID						= "activityId";
	private static final String ACTIVITY						= "ACTIVITY";		
	private static final String USER_ID							= "USER_ID";
	private final static String IS_MANAGER						= "IS_MANAGER";
	private static final String IS_USER_ALL						= "IS_USER_ALL";
	
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 40;	   
	
    /**
	 * Contstructor - with the basic settings for the controller.
	 * @author Adelina
	 */
	public ActivityViewController() {
		setView(VIEW);		
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse result) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		// used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
				
		ModelAndView mav = new ModelAndView();
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		Activity activity = new Activity();
		
		try{
			Integer activityId = ServletRequestUtils.getIntParameter(request, ACTIVITY_ID);
			logger.debug("Activity Id = ".concat(activityId.toString()));
			
			if(activityId != null) {
				// call the get method from the business layer
				activity = BLActivity.getInstance().getWithProjectDetail(activityId);
				
				if(activity.getProjectDetails() != null && activity.getProjectDetails().getProjectId() != -1) {
					Project project = BLProject.getInstance().get(activity.getProjectDetails().getProjectId(), true);
					activity.setProjectName(ControllerUtils.getInstance().tokenizeField(project.getName(), NR_CHARS_PER_LINE));
					
					boolean isManager = false;
					
					logger.debug("project manager id = " +  project.getManagerId());
					logger.debug("personId = " + userAuth.getPersonId());
					
					if(userAuth.getPersonId() == project.getManagerId()) {
						isManager = true;
					}					
					logger.debug("isManager = " + isManager);					
					
					mav.addObject(IS_MANAGER, isManager);
				}		
																	
				// tokenize fields
				activity.setName(ControllerUtils.getInstance().tokenizeField(activity.getName(), NR_CHARS_PER_LINE));
				if(activity.getDescription() != null) {
					activity.setDescription(ControllerUtils.getInstance().tokenizeField(activity.getDescription(), NR_CHARS_PER_LINE));
				}				
				mav.addObject(ACTIVITY, activity);
			}
						
			mav.addObject(USER_ID, userAuth.getPersonId());							
			
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
				
		boolean isUserAll = false;
		//is USER_ALL
		if (userAuth.hasAuthority(PermissionConstant.getTheInstance().getTS_ActivityAdvancedAddUpdate())) {
			isUserAll = true;			
		} else {
			isUserAll = false;
		}
		
		logger.debug("isUserAll = " + isUserAll);			
		mav.addObject(IS_USER_ALL, isUserAll);
		
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);		
		
		logger.debug("handleRequestInternal - END");
		
		return mav;
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
	boolean isManager(Project project, Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("isManager - START");
		
		boolean isManager = false;
				
		if(personId == project.getManagerId()) {
			isManager = true;
		}
		
		logger.debug("isManager - END, with isManager = " + isManager);
		return isManager;		

	}	
}
