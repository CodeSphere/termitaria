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

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLOrganisation;
import ro.cs.om.entity.Module;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * @author alu
 *
 */
public class RoleGetModulesController extends RootAbstractController{
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	public static final String GET_MODULES_ERROR = "role.get.modules.error";
	
	//------------------------ATTRIBUTES--------------------------------------------------------------
	public static final String ORG 	= "org";
	public static final String CASE = "CASE";
	
	//------------------------MODEL-------------------------------------------------------------------
	public static final String MODULES = "MODULES";
	public static final String DEFAULT = "DEFAULT";
	
	public RoleGetModulesController(){
		setView("Role_ModulesForOrganisation");
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse res) throws Exception {
		logger.debug("handleRequestInternal - START");
		ModelAndView mav = new ModelAndView(getView());
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		try {
			int orgId = ServletRequestUtils.getIntParameter(request, ORG);
			
			if (orgId == -1){
				// if the default option has been chosen, I have to put the "select organization first" message in the modules drop-down
				mav.addObject(DEFAULT, "true");
			} else {
				Set<Module> modules = BLOrganisation.getInstance().getModules(orgId);
				// I have to put the modules on MAV(if I have 0 modules, I have to put "null")
				if ((modules != null) && (modules.size() == 0)){
					mav.addObject(MODULES, null);
				} else {
					mav.addObject(MODULES, modules);
				}
			}
			
			//passing utilization case
			mav.addObject(CASE, request.getParameter(CASE));
			
		} catch(BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(GET_MODULES_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_MODULES_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		
		logger.debug("handleRequestInternal - END");
		return mav;
	}
}
