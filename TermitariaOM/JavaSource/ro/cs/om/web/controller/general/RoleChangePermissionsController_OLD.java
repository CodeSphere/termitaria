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
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLPermission;
import ro.cs.om.business.BLRole;
import ro.cs.om.entity.Permission;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.entity.RoleWeb;
import ro.cs.tools.Tools;

/**
 * @author alu
 *
 */
public class RoleChangePermissionsController_OLD extends RootAbstractController{
	
	private static final String PERMISSIONS = "PERMISSIONS";
	private static final String ALL_PERMISSIONS = "ALL_PERMISSIONS";
	private static final String ROLEID = "roleId";
	private static final String NEW_PERMISSIONS = "changeRolesPermissionsForm_select2";
	private static final String UPDATE = "UPDATE";
	private static final String UPDATE_ERROR = "role.update.permissions.error";
	private static final String GET_PERMISSIONS_ERROR = "role.get.permissions.error";
	private static final String UPDATE_MESSAGE = "role.updateRoleWeb.message";
	private static final String MESSAGE_VIEW = "Messages";
	
	public RoleChangePermissionsController_OLD() {
		setView("Role_ChangePermissions_OLD");
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		ModelAndView mav = null; 
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		if (req.getParameter(UPDATE) != null) {
			mav = handleUpdatePermissions(req, res, errorMessages, infoMessages);
    	} else {
    		mav = handleGetPermissions(req, res, errorMessages, infoMessages);
    	}
		
		setErrors(req, errorMessages);
		setMessages(req, infoMessages);
		
		logger.debug("handleRequestInternal - END");
		return mav;
	}
	
	private ModelAndView handleUpdatePermissions(HttpServletRequest req, HttpServletResponse res, ArrayList<String> errorMessages, ArrayList<String> infoMessages) throws ServletRequestBindingException, BusinessException{
		logger.debug("handleUpdatePermissions - START");
		
		ModelAndView mav = new ModelAndView(MESSAGE_VIEW);
		try {
			RoleWeb rw = BLRole.getInstance().getRoleWeb(ServletRequestUtils.getIntParameter(req, ROLEID));
			int[] permsIds = ServletRequestUtils.getIntParameters(req, NEW_PERMISSIONS);
			rw.setPermissions(Tools.getInstance().getSetFromArray(permsIds));			
			
			BLRole.getInstance().updateRoleWeb(rw);
			
			infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, null, RequestContextUtils.getLocale(req)));
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(req)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(req)));
		}		
				
		logger.debug("handleUpdatePermissions - END");
		return mav;
	}
	
	private ModelAndView handleGetPermissions(HttpServletRequest req, HttpServletResponse res, ArrayList<String> errorMessages, ArrayList<String> infoMessages) throws ServletRequestBindingException, BusinessException{
		logger.debug("handleGetPermissions - START");
		
		ModelAndView mav = new ModelAndView(getView());		
		try {
			int roleId = ServletRequestUtils.getIntParameter(req, ROLEID);		
			// get all permissions for the given role
			Set<Permission> permissions = BLRole.getInstance().getPermissions(roleId);
			// get all permissions, except those which contained in my role
			List<Permission> allPermissions = BLPermission.getInstance().getAllPermissionsNotInSet(permissions);
			
			mav.addObject(PERMISSIONS, permissions);
			mav.addObject(ALL_PERMISSIONS, allPermissions);
			mav.addObject(ROLEID, roleId);			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PERMISSIONS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(req)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_PERMISSIONS_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(req)));
		}	
		
		logger.debug("handleGetPermissions - END");
		return mav;
	}
	
}
