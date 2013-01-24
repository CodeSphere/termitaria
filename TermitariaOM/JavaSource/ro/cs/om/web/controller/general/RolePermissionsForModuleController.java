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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLModule;
import ro.cs.om.business.BLPermission;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Permission;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

public class RolePermissionsForModuleController extends RootAbstractController {
	
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "permission.";	
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");
	private final String MODULE_GET_ERROR						= "module.get.error";
	private final String GENERAL_ERROR							= "role.general.error";
		
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "RolePermissionsForModule";	
	private static final String ALL_PERMISSIONS					= "ALL_PERMISSIONS";
	private static final String PERMISSIONS						= "PERMISSIONS";
	private static final String SELECTED_MODULE_NAME			= "SELECTED_MODULE_NAME";
	
	private static final String MODULE_ID						= "moduleId";
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 * 
	 * @author Adelina
	 */
	public RolePermissionsForModuleController() {
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
		
		try{
			Integer moduleId = ServletRequestUtils.getIntParameter(request, MODULE_ID);
			logger.debug("Module id: ".concat(moduleId.toString()));
			mav.addObject(MODULE_ID, moduleId);
														
			if(moduleId != null){					
				logger.debug("are module id");										
				// get all available permissions for a module		
				List<Permission> permissions = BLPermission.getInstance().getPermissionByModule(moduleId);				
				mav.addObject(ALL_PERMISSIONS, permissions);	
				
				Set<Permission> newPermissions = new HashSet<Permission>(); 
				if(moduleId != -1) {
					Permission permission = BLPermission.getInstance().getDefaultPermissionByModule(moduleId);				
					newPermissions.add(permission);
				}
				mav.addObject(PERMISSIONS, newPermissions);
				
				Module module = null;
				
				try{
					module = BLModule.getInstance().get(moduleId);
				}catch (BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(MODULE_GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				}
				
				if(module != null) {
					String moduleName = module.getName();
					mav.addObject(SELECTED_MODULE_NAME, moduleName);
				}
			} 
			
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}	
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);		
		
		logger.debug("handleRequestInternal - END");
		
		return mav;
	}
}
