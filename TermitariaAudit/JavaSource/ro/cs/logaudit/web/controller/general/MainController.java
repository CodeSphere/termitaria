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
package ro.cs.logaudit.web.controller.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.logaudit.common.ConfigParametersProvider;
import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.web.controller.root.RootAbstractController;
import ro.cs.logaudit.web.security.UserAuth;

/**
 *  MainController
 * 
 * @author Adelina
 *
 */
public class MainController extends RootAbstractController {

	private final String THEME			= "THEME";
	
	//------------------------MODEL-----------------------------------------------------------------
	private static String OM_MODULES_URL 							= ConfigParametersProvider.getConfigString("modules.url");
	private static String OM_MODULES								= "OM_MODULES";
	
	public MainController() {
		setView("Audit");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getView());		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//if there aren't parameters we put it
		if(request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID) == null){		
			request.getSession().setAttribute(IConstant.SESS_ORGANISATION_ID, new Integer(userAuth.getOrganisationId()));
			request.getSession().setAttribute(IConstant.SESS_ORGANISATION_NAME, userAuth.getOrganisationName());
		}
			
		mav.addObject(THEME, userAuth.getThemeCode());
		
		//Setting organisation id and name for view
		mav.addObject(IConstant.SESS_ORGANISATION_ID, request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID));
		mav.addObject(IConstant.SESS_ORGANISATION_NAME, request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME));
		
		StringBuilder omModules = new StringBuilder();
		omModules.append("http://").append(request.getServerName()).append(":").append(request.getServerPort())
		.append("/").append(IConstant.OM_APP_CONTEXT).append("/Modules.htm");
		mav.addObject(OM_MODULES, omModules.toString());
		
		return mav;
	}
}

