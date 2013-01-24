/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.cm.web.controller.general;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLClient;
import ro.cs.cm.business.BLProject;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootAbstractController;
import ro.cs.cm.web.security.UserAuth;



public class ProjectListForClientController extends RootAbstractController {
	
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "project.";	
	private final String GET_PROJECTS_FOR_CLIENT_ERROR			= ROOT_KEY.concat("get.projects.for.client.error");	
	private final String GENERAL_ERROR							= ROOT_KEY.concat("general.error");	
	private final String GET_CLIENT_ERROR						= "client.get.error";
	private final String FINISH_PROJECT_SUCCESS					= ROOT_KEY.concat("finish.success");
	private final String FINISH_PROJECT_ERROR 					= ROOT_KEY.concat("finish.error");
	private final String ABORT_PROJECT_SUCCESS					= ROOT_KEY.concat("abort.success");
	private final String ABORT_PROJECT_ERROR 					= ROOT_KEY.concat("abort.error");
	private final String ACTIVATE_PROJECT_SUCCESS				= ROOT_KEY.concat("activate.success");
	private final String ACTIVATE_PROJECT_ERROR 				= ROOT_KEY.concat("activate.error");
		
	//------------------------VIEW------------------------------------------------------------------	
	private static final String VIEW							= "Project_ListForClient";
	private static final String PROJECTS						= "PROJECTS";
	private static final String CLIENT_TYPE  					= "CLIENT_TYPE";
	private static final String CLIENT_NAME						= "CLIENT_NAME";
	private static final String CLIENT_FIRST_NAME				= "CLIENT_FIRST_NAME";	
	private static final String CLIENT_LAST_NAME			    = "CLIENT_LAST_NAME";
	private static final String CLIENT_WHOLE_NAME				= "CLIENT_WHOLE_NAME";
	private static final String CLIENT_ID						= "clientId";	
	private static final String USER_ID							= "USER_ID";
	private final String CMD_FINISH_PROJECT_LISTING				= "FINISH_PROJECT_LISTING";
	private final String CMD_ABORT_PROJECT_LISTING				= "ABORT_PROJECT_LISTING";
	private final String CMD_ACTIVATE_PROJECT_LISTING			= "ACTIVATE_PROJECT_LISTING";
	private final String PROJECT_ID								= "projectId";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 				= "BACK_URL";
	private static final String NEXT_BACK_URL			= "NEXT_BACK_URL";	
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 45;
			
	public ProjectListForClientController() {
		setView(VIEW);
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START ");
		
		ModelAndView mav = new ModelAndView(getView());
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = " + action);
						
		Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
		
		Integer clientId = ServletRequestUtils.getIntParameter(request,CLIENT_ID);
		logger.debug("Client Id = ".concat(String.valueOf(clientId)));
		
		if(action != null) {			
			logger.debug("action clientId = " + clientId);
			if(CMD_FINISH_PROJECT_LISTING.equals(action)) {
				handleFinishProject(request, projectId);
			} else if(CMD_ABORT_PROJECT_LISTING.equals(action)) {
				handleAbortProject(request, projectId);
			} else if(CMD_ACTIVATE_PROJECT_LISTING.equals(action)) {
				handleActivateProject(request, projectId);
			}
		}
		
		request.setAttribute(IConstant.REQ_ACTION, action);		
				
		//put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
		String servletPath = request.getServletPath();
		
		String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");

		logger.debug("BACK_URL = " + backUrl);
		logger.debug("NEXT_BACK_URL = " + nextBackUrl);
		
		request.setAttribute(BACK_URL, backUrl);		
		request.setAttribute(NEXT_BACK_URL, nextBackUrl);		
		
		request.setAttribute(CLIENT_ID, clientId);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		if(clientId != null) {
			// get all the projects for the client
			List<Project> projects = new ArrayList<Project>();
			try{				
				projects = BLProject.getInstance().getProjectsByClientId(clientId);				
				
				for(Project project : projects) {
					String panelHeaderName = ControllerUtils.getInstance().truncateName(project.getName(), NR_CHARS_PANEL_HEADER);
					project.setPanelHeaderName(panelHeaderName);
				}	
			} catch(BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_PROJECTS_FOR_CLIENT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}
			
			// put on mav the projects that belongs to that client
			mav.addObject(PROJECTS, projects);
			if(projects != null && projects.size() > 0) {
				mav.addObject(CLIENT_TYPE, projects.get(0).getClient().getType());
				if(projects.get(0).getClient().getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					mav.addObject(CLIENT_NAME, projects.get(0).getClient().getC_name());
				} else {
					mav.addObject(CLIENT_FIRST_NAME, projects.get(0).getClient().getP_firstName());
					mav.addObject(CLIENT_LAST_NAME, projects.get(0).getClient().getP_lastName());
					mav.addObject(CLIENT_WHOLE_NAME, projects.get(0).getClient().getP_firstName().concat(" ").concat(projects.get(0).getClient().getP_lastName()));
				}
				
			} else {
				Client client = new Client();
				try{
					client = BLClient.getInstance().get(clientId);
				} catch(BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(GET_CLIENT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}
				mav.addObject(CLIENT_TYPE, client.getType());
				if(client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					mav.addObject(CLIENT_NAME, client.getC_name());
				} else {
					mav.addObject(CLIENT_FIRST_NAME, client.getP_firstName());
					mav.addObject(CLIENT_LAST_NAME, client.getP_lastName());
					mav.addObject(CLIENT_WHOLE_NAME, client.getP_firstName().concat(" ").concat(client.getP_lastName()));
				}
			}
		}
		
		
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);	
		
		logger.debug("handleRequestInternal - END");
		return mav;
	}
	
	/**
	 * Finish a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param projectId
	 * @throws Exception
	 */
	private void handleFinishProject(HttpServletRequest request, Integer projectId) throws Exception {
		logger.debug("handleFinishProject - START");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();	
						
		try{
			Project project = BLProject.getInstance().finishProject(projectId);
			String projectName = project.getName();
			infoMessages.add(messageSource.getMessage(FINISH_PROJECT_SUCCESS, new Object[] {projectName} , RequestContextUtils
					.getLocale(request)));
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(FINISH_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleFinishProject - END");			
	}
	
	/**
	 * Abort a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param projectId
	 * @throws Exception
	 */
	private void handleAbortProject(HttpServletRequest request, Integer projectId) throws Exception {
		logger.debug("handleAbortProject - START");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		try{
			Project project = BLProject.getInstance().abortProject(projectId);
			String projectName = project.getName();
			infoMessages.add(messageSource.getMessage(ABORT_PROJECT_SUCCESS, new Object[] {projectName} , RequestContextUtils.getLocale(request)));
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ABORT_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
				
		logger.debug("handleAbortProject - END");			
	}
	
	/**
	 * Activate a project
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param projectId
	 * @throws Exception
	 */
	private void handleActivateProject(HttpServletRequest request, Integer projectId) throws Exception {
		logger.debug("handleActivateProject - START");
			
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		try{
			Project project = BLProject.getInstance().activateProject(projectId);
			String projectName = project.getName();
			infoMessages.add(messageSource.getMessage(ACTIVATE_PROJECT_SUCCESS, new Object[] {projectName} , RequestContextUtils.getLocale(request)));
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ACTIVATE_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleActivateProject - END");		
	}

}
