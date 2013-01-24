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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLModule;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPermission;
import ro.cs.om.business.BLRole;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Permission;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.entity.RoleWeb;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 *
 */
public class RoleController extends RootSimpleFormController{
	
	private static final String ACTION 					= "action";
	private static final String EDIT 					= "edit";
	private static final String ID 						= "id";
	private static final String GET_ERROR 				= "role.getRoleWebById.error";
	private static final String UPDATE_ERROR 			= "role.updateRoleWeb.error";
	private static final String ADD_ERROR 				= "role.add.error";
	private static final String GENERAL_ERROR			= "role.general.error";	
	private final String MODULE_GET_ERROR				= "module.get.error";
	
	private static final String ADD_MESSAGE 			= "role.add.message";
	private static final String UPDATE_MESSAGE 			= "role.updateRoleWeb.message";
	private static final String FORM_VIEW				= "Role";
	private static final String SUCCESS_VIEW			= "Role";
	private static final String MODULES 				= "MODULES";
	private static final String ORGANISATIONS 			= "ORGANISATIONS";	
	
	//------------------------MODEL-------------------------------------------------------------------
	
	private static final String PERMISSIONS 						= "PERMISSIONS";	
	private static final String ALL_PERMISSIONS 					= "ALL_PERMISSIONS";	
	private static final String SELECTED_MODULE_NAME				= "SELECTED_MODULE_NAME";
	private static final String GET_ACTION							= "GET_ACTION";	
	private static final String ROLE_MODULE							= "ROLE_MODULE";
	
	
	public RoleController(){
		setCommandName("roleWebBean");
		setCommandClass(RoleWeb.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
		
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages 	= new ArrayList<String>();
		
		try {
			//check if I have to EDIT a role
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, ACTION) != null &&
					EDIT.equals(ServletRequestUtils.getStringParameter(request, ACTION))){
				
				logger.debug("formBackingObject: EDIT");
				request.setAttribute(ORGANISATIONS, BLOrganisation.getInstance().getAllOrganisationsForNom());
				logger.debug("ORAGANISATIONS CLASSIFIED LIST LOADED");
				
				RoleWeb rw = handleGet(request); 
				if (rw == null) {
					rw = new RoleWeb();
				}
				
				// if I have the edit action, I have to put on request all the modules for the role's organization
				Set<Module> modules = BLOrganisation.getInstance().getModules(rw.getOrganisationId());
				if ((modules != null) && (modules.size() != 0)){
					request.setAttribute(MODULES, modules);
				} 
		
				return rw;		
			}
		} catch (BusinessException be) {
			logger.error("formBackingObject", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("formBackingObject: ADD");
		request.setAttribute(ORGANISATIONS, BLOrganisation.getInstance().getAllOrganisationsForNom());
		logger.debug("ORAGANISATIONS CLASSIFIED LIST LOADED");
		
		//if any user except admin_IT is logged on, I have to retrieve the organisationId ( and its modules )in order to permit operations only on roles
		//from that specific organisation 
		RoleWeb rw = new RoleWeb();
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		if ( organisationId != null ){
			//request.setAttribute( ORGANISATION_ID, organisationId.intValue() );
			rw.setOrganisationId(organisationId.intValue());
			logger.debug("Setting Organisation ID for add new role " + organisationId);
			Set<Module> modules = BLOrganisation.getInstance().getModules( organisationId );
			if ((modules != null) && (modules.size() != 0)){
				request.setAttribute(MODULES, modules);
			} 
		}
		
		setErrors(request, errorMessages);
		logger.debug("formBackingObject - END");
		return rw;
	
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ModelAndView mav = new ModelAndView();
		RoleWeb roleWebBean = (RoleWeb) command;
		
		//we set the session organisationId
		Integer organisationId  = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		roleWebBean.setOrganisationId(organisationId);
		
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		
		if (IConstant.CMD_SAVE.equals(action)) {
			// check if I have role id
			if (roleWebBean.getRoleId() != -1){
				// if I have role id, it means that I have "update" action
				logger.debug("handleUpdate");
				mav =  handleUpdate(roleWebBean, request, locale, errorMessages, infoMessages);
			} else {
				
				// if I don't have role id, it means that I have "add" action
				logger.debug("handleAdd");
				mav =  handleAdd(roleWebBean, locale, errorMessages, infoMessages, request);
			}
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addAllObjects(referenceData(request, command, errors));
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	private ModelAndView handleUpdate(RoleWeb rw, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getFormView());	
		try{	
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_RoleUpdate())){			
				mav.addObject(getCommandName(), rw);
				BLRole.getInstance().updateRoleWeb(rw);
				infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, null, locale));
				
				// if I have the update action, I have to put on mav all the modules for the role's organization
				Set<Module> modules = BLOrganisation.getInstance().getModules(rw.getOrganisationId());
				if ((modules != null) && (modules.size() != 0)){
					mav.addObject(MODULES, modules);
				}
				
				//add the new audit event
				try {
					Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ROLE_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_ROLE_UPDATE_MESSAGE, new Object[] {rw.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_ROLE_UPDATE_MESSAGE, new Object[] {rw.getName(), org.getName()}, new Locale("ro"))
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
		
		return mav;
	}
	
	private RoleWeb handleGet(HttpServletRequest request) throws ServletRequestBindingException, BusinessException{
		logger.debug("handleGet - START");
		request.setAttribute(GET_ACTION, true);
		RoleWeb rw = BLRole.getInstance().getRoleWeb(ServletRequestUtils.getIntParameter(request, ID).intValue());
		if(rw != null) {
			Integer moduleId = rw.getModuleId();
			Module module = BLModule.getInstance().get(moduleId);
			request.setAttribute(ROLE_MODULE, module);
		}
		logger.debug("Role web module id = " + rw);
		
		logger.debug("handleGet - END");
		return rw;
	}
	
	private ModelAndView handleAdd(RoleWeb roleWeb, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request){
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getFormView());
		
		try{	
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_RoleAdd())){		
				mav.addObject(getCommandName(), roleWeb);				

				BLRole.getInstance().add(roleWeb);
				infoMessages.add(messageSource.getMessage(ADD_MESSAGE, null, locale));
				
				// if I have the add action, I have to put on mav all the modules for the role's organization
				Set<Module> modules = BLOrganisation.getInstance().getModules(roleWeb.getOrganisationId());
				if ((modules != null) && (modules.size() != 0)){
					mav.addObject(MODULES, modules);
				} 
				
				//add the new audit event
				try {
					Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ROLE_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_ROLE_ADD_MESSAGE, new Object[] {roleWeb.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_ROLE_ADD_MESSAGE, new Object[] {roleWeb.getName(), org.getName()}, new Locale("ro"))
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
	
		return mav;
	}
	
	
	/**
	 * Adds to model required nomenclators
	 * @author Adelina
	 */
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
		logger.debug("referenceData - start");
		ArrayList errorMessages = new ArrayList();
		Map modelO = new HashMap();
		try{					
			RoleWeb role = (RoleWeb) command;				
				
			if(role != null) {
				
				// get all permissions for the given role
				int roleId = role.getRoleId();			
				Set<Permission> newPermissions = BLRole.getInstance().gePermissionsWithDefault(roleId);
				modelO.put(PERMISSIONS, newPermissions);
				if(role.getModuleId() != -1) {
					modelO.put(ALL_PERMISSIONS, BLPermission.getInstance().getPermissionByModuleAndRole(role.getModuleId(), roleId));
				} else {
					modelO.put(ALL_PERMISSIONS, new HashSet<Permission>());
				}
				
				
				// get the module for the given role
				int moduleId = role.getModuleId();	
				if(moduleId != -1) {
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
						modelO.put(SELECTED_MODULE_NAME, moduleName);
					}
				}
			}
			
		} catch(BusinessException bexc) {
			logger.error("referenceData", bexc);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception ex) {
			logger.error("referenceData", ex);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - end");
		
		return modelO;
	}	
}
