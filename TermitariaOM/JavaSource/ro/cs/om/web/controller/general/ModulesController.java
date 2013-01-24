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
package ro.cs.om.web.controller.general;

import java.net.InetAddress;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import ro.cs.om.business.BLModule;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Module;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author dan.damian
 *
 */
public class ModulesController extends RootAbstractController {

	
	private static String MODULES = "modules";
	private static String OM_MODULE_ID = "OM_MODULE_ID";
	private static String IS_ADMIN_IT = "IS_ADMIN_IT";
	
	public ModulesController() {
		setView("Modules");
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("===============================================");
		logger.debug("=========== MODULES CONTROLLER ================");
		logger.debug("===============================================");
		ModelAndView mav = new ModelAndView(getView());
		
		
		InetAddress.getLocalHost().getHostAddress();
		
		UserAuth userAuth = (UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mav.addObject("username", userAuth.getUsername());
		
//		mav.addObject(IS_ADMIN_IT, userAuth.isAdminIT());
//		mav.addObject(MODULES, userAuth.getModules());
		request.getSession().setAttribute(IS_ADMIN_IT, userAuth.isAdminIT());
		request.getSession().setAttribute(MODULES, userAuth.getModules());
		
		Locale locale = (Locale)request.getSession().getAttribute(
				SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		if(locale != null){
			mav.addObject(IConstant.LANGUAGE_ATTRIBUTE, (locale.getLanguage()));
		}else{
			mav.addObject(IConstant.LANGUAGE_ATTRIBUTE, ("ro"));
		}
		
		Module omModule = BLModule.getInstance().get(IConstant.OM_MODULE);
		mav.addObject(OM_MODULE_ID, omModule.getModuleId());
		return mav;
	}

}
