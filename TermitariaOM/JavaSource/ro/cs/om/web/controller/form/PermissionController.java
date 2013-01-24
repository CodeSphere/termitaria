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
package ro.cs.om.web.controller.form;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLModule;
import ro.cs.om.business.BLPermission;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Permission;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 * 
 */
public class PermissionController extends RootSimpleFormController {

	private static final String ACTION = "action";
	private static final String EDIT = "edit";
	private static final String ID = "id";
	private static final String GET_ERROR = "permission.getById.error";
	private static final String UPDATE_ERROR = "permission.update.error";
	private static final String ADD_ERROR = "permission.add.error";
	private static final String ADD_MESSAGE = "permission.add.message";
	private static final String UPDATE_MESSAGE = "permission.update.message";
	private static final String FORM_VIEW = "Permission";
	private static final String SUCCESS_VIEW = "Permission";		
	private static final String GET_ACTION = "GET_ACTION";	
	private static final String PERMISSION_MODULE = "PERMISSION_MODULE";
		
	public PermissionController() {
		setCommandName("permissionBean");
		setCommandClass(Permission.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject - START");
		ArrayList<String> errorMessages = new ArrayList<String>();		
		try {
			request.setAttribute(IConstant.NOM_MODULES, OMContext.getFromContext(IConstant.NOM_MODULES));
			logger.debug("MODULES CLASSIFIED LIST LOADED");			
			//check if I have to EDIT a permission
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, ACTION) != null &&
					EDIT.equals(ServletRequestUtils.getStringParameter(request, ACTION))){
				return handleGet(request);
			} 

		} catch (BusinessException be) {
			logger.error("formBackingObject", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
						
		logger.debug("formBackingObject - END");
		return super.formBackingObject(request);
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ModelAndView mav = new ModelAndView();
		Permission permission = (Permission) command;
			
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		
		if (IConstant.CMD_SAVE.equals(action)) {
			// check if I have permission id
			if (permission.getPermissionId() != -1){
				// if I have permission id, it means that I have "update" action					
				handleUpdate(permission, locale, errorMessages, infoMessages, request);				
			} else {
				// if I don't have permission id, it means that I have "add" action
				handleAdd(permission, locale, errorMessages, infoMessages, request);				
			}
		}

		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addObject(getCommandName(), permission);
		logger.debug("onSubmit - END");
		
		return mav;
	}
	
	private Permission handleGet(HttpServletRequest request) throws ServletRequestBindingException, BusinessException{
		logger.debug("handleGet - START");				
		Permission permission = BLPermission.getInstance().get(ServletRequestUtils.getIntParameter(request, ID).intValue());
		if(permission != null) {
			Integer moduleId = permission.getModuleId();
			Module module = BLModule.getInstance().get(moduleId);
			request.setAttribute(PERMISSION_MODULE, module);
		}
	
		request.setAttribute(GET_ACTION, true);
		logger.debug("handleGet - END");
		return permission;
	}
		
	private void handleUpdate(Permission permission, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request){
		logger.debug("handleUpdate - START");		
		try{			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().get_Super())){
				BLPermission.getInstance().update(permission);							
				infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, null, locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERMISSION_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERMISSION_UPDATE_MESSAGE, new Object[] {permission.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERMISSION_UPDATE_MESSAGE, new Object[] {permission.getName()}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("handleUpdate - END");
	}
	
	private void handleAdd(Permission permission, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request){
		logger.debug("handleAdd - START");
		
		try{		
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().get_Super())){
				BLPermission.getInstance().add(permission);
				infoMessages.add(messageSource.getMessage(ADD_MESSAGE, null, locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERMISSION_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERMISSION_ADD_MESSAGE, new Object[] {permission.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERMISSION_ADD_MESSAGE, new Object[] {permission.getName()}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("handleAdd - END");
	}
}
