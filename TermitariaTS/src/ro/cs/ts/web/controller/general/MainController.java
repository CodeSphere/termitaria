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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.ConfigParametersProvider;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;

/**
 *  MainController
 * 
 * @author Coni
 *
 */
public class MainController extends RootAbstractController {

	private static String MODULES = "modules";
	//------------------------VIEW------------------------------------------------------------------
	private final String THEME			= "THEME";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String USER_GET_PROJECTS_ERROR 		= "user.get.projects.error";
	private static final String GENERAL_ERROR					= "login.general.error";
	
	//------------------------MODEL-----------------------------------------------------------------
	private static final String IS_PM_FOR_AT_LEAST_ONE_PROJECT  = "IS_PM_FOR_AT_LEAST_ONE_PROJECT";
	private static String OM_MODULES_URL 							= ConfigParametersProvider.getConfigString("modules.url");
	private static String OM_MODULES								= "OM_MODULES";
	
	public MainController() {
		setView("TS");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("Main Controller...");
		ModelAndView mav = new ModelAndView(getView());		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ArrayList<String> errorMessages = new ArrayList<String>();
		try {
			
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(
						userAuth,
						SecurityContextHolder.getContext().getAuthentication().getCredentials(),
						userAuth.getAuthorities())
					);
			
			//if there aren't parameters we put it
			if(request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID) == null){		
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_ID, new Integer(userAuth.getOrganisationId()));
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_NAME, userAuth.getOrganisationName());
			}
				
			mav.addObject(THEME, userAuth.getThemeCode());
			
			//Setting organisation id and name for view
			mav.addObject(IConstant.SESS_ORGANISATION_ID, request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID));
			mav.addObject(IConstant.SESS_ORGANISATION_NAME, request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME));
			
			mav.addObject(IS_PM_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages));
			
			StringBuilder omModules = new StringBuilder();
			omModules.append("http://").append(request.getServerName()).append(":").append(request.getServerPort())
			.append("/").append(IConstant.OM_APP_CONTEXT).append("/Modules.htm");
			mav.addObject(OM_MODULES, omModules.toString());
			
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		if (!errorMessages.isEmpty()) {
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			setErrors(request, errorMessages);
		}
		
		return mav;
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
			errorMessages.add(messageSource.getMessage(USER_GET_PROJECTS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
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

