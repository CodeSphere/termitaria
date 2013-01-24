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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLModule;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.business.BLRole;
import ro.cs.om.business.BLUserGroup;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * Form controller which operates on 'Organization' entity
 * 
 * @author matti_joona
 */
public class OrganisationFormController extends RootSimpleFormController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "organisation.";
	private final String ADD_SUCCESS							= ROOT_KEY.concat("add.success");
	private final String UPDATE_SUCCESS							= ROOT_KEY.concat("update.success");	
	private final String ADD_ERROR								= ROOT_KEY.concat("add.error");
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	private final String UPDATE_ERROR							= ROOT_KEY.concat("update.error");
	private final String GENERAL_ERROR							= ROOT_KEY.concat("general.error");
	private final String GET_FAKE_DEPARTMENT_ERROR				= ROOT_KEY.concat("get_fake_department.error");
	private final String GET_ROLE_BY_NAME_AND_ORG_ERROR			= ROOT_KEY.concat("get_role_by_name_and_org.error");
	private final String GET_MODULES_WITHOUT_OM_ERROR			= ROOT_KEY.concat("get_modules_without_om.error");

	//------------------------ATRIBUTE------------------------------------------------------------------
	private final String ORGANISATION_ID						= "organisationId";
	private final String ORG									= "ORG"; 	
	private final String FAKE_DEPARTMENT_ID 					= "FAKE_DEPARTMENT_ID";
	private final String ADMIN_ROLE_ID							= "ADMIN_ROLE_ID";
	private final String IS_CREATE_ADMIN						= "IS_CREATE_ADMIN";
	private final String MODEL_DEFAULT_USER_GROUP_ID			= "DEFAULT_USER_GROUP_ID";
	
	//------------------------MODEL-------------------------------------------------------------------
	
	private final String ALL_MODULES							= "ALL_MODULES";
	private final String ORGANISATION_TYPE						= "ORGANISATION_TYPE";	
	private final String GROUP_COMPANIES_TYPE					= "GROUP_COMPANIES_TYPE";
	private final String ADD_COMPANY							= "ADDCOMPANY";
	private final String ADD_BRANCH								= "ADDBRANCH";	
	private final String PARENT 								= "PARENT";
	private static final String GET_ACTION 						= "GET_ACTION";
	private static final String IS_ADMIN_IT						= "IS_ADMIN_IT";
	
	//-----------------------OPTION--------------------------------------------------------------------
	private final String ORGANISATION							= "organisation";
	private final String PERSON									= "person";
	
	

	
	/**
	 * Constructor for setting of the form
	 *
	 * @author matti_joona
	 */
	public OrganisationFormController(){
		setCommandName("organisationBean");
		setCommandClass(Organisation.class);
		setFormView("OrganisationForm");
		setSuccessView("OrganisationForm");		
	}
	
	/**
	 * Bindings for request parameters
	 * 
	 * @author Adelina
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("start - initBinder");
		
		binder.registerCustomEditor(Set.class, "modules", new CustomCollectionEditor(Set.class){
			protected Object convertElement(Object element){
				if(element != null){
					Integer moduleId = new Integer((String)element);
					logger.debug("modules : convertElement for Id " + moduleId);
					Module module = null;
					try{
						module = BLModule.getInstance().get(moduleId);						
					} catch(BusinessException e){						
						logger.debug("There was an error");
					}
					logger.debug("end - initBinder");
					return module;
				}
				logger.debug("end - initBinder");
				return null;
			}			
		});		
	}
		
	/**
	 * Method which is called at every request
	 *
	 * @author matti_joona
	 */		
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		// logger debug
		logger.debug("formBackingObject - START");
			
		Locale locale = RequestContextUtils.getLocale(request);		
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();		
		
		//setting the view
		setViewSetting(ORGANISATION);
		
		Organisation org = null;				
		try{
			Integer organisationId = ServletRequestUtils.getIntParameter(request, ORGANISATION_ID);
			Integer organisationIdSession = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);	
			logger.debug("action = " + action);
			if( organisationId != null && organisationId > 0){
				logger.debug("formBackingObject: Get");
				org = handleGet(organisationId, errorMessages, infoMessages, locale, request);				
			} else {
				logger.debug("formBackingObject: New");		
				if(organisationIdSession != null && organisationIdSession > 0){							
					if(ADD_BRANCH.equals(action)){
						logger.debug("compose branch");
						org = composeNewBranch(organisationIdSession, errorMessages, infoMessages, locale);
					} else if(ADD_COMPANY.equals(action)){
						logger.debug("compose company");
						org = composeNewCompany(organisationIdSession, errorMessages, infoMessages, locale);
					} else {
						logger.debug("compose new organisation");
						org = new Organisation();
					}
				} else {
					org = new Organisation();
				}
			} 		
		} catch (ServletRequestBindingException e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
			
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject - END ".concat((org != null ? org.toString(): "null")));
		
	
		return org;
	}		
	
	/**
	 * Method which is called when submitting the form
	 *
	 * @author matti_joona
	 */	
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView();
		Locale locale =  RequestContextUtils.getLocale(request);
			
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
	
		Organisation organisation = (Organisation)command;
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		
		if (IConstant.CMD_SAVE.equals(action)){
			if (organisation.getOrganisationId() > 0) {			
				mav = handleUpdate(request, response, command, errors, errorMessages, infoMessages, locale);
			} else {		
				logger.debug("organisation parent = " + organisation.getParentId());
				if((organisation.getParentId() <= 0) && ((organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_COMPANY) || (organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_GROUP_COMPANIES) || (organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_HQ))){
					// add a simple organisation

					mav = handleAdd(request, response, command, errors, errorMessages, infoMessages, locale);
				} else if((organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_COMPANY  || organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_HQ)&& organisation.getParentId() != -1){
					// add a company (or a head quarter) to the organisation
					mav = handleAddCompany(request, response, command, errors, errorMessages, infoMessages, locale);
				} else if(organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_BRANCH) {
					// add a branch to the organisation
					mav = handleAddBranch(request, response, command, errors, errorMessages, infoMessages, locale);
				}
			}
		} 
					
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
	
		mav.addAllObjects(referenceData(request, command, errors));
		
		logger.debug("onSubmit - END");

		return mav;
	}
	
	/**
	 * Method for add request
	 *
	 * @author matti_joona
	 */	

	private ModelAndView handleAdd(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("== ADD OPERATION==");
		
		//this view is used only for showing messages/errors
		ModelAndView mav = new ModelAndView(getFormView());
		
		// the command class that is put on mav
		Organisation org = (Organisation)command;	
		//call add method from the business layer
		
		try{		
			//the user that is authenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission Super, can add an organisation
			if (userAuth.hasAuthority(PermissionConstant.getInstance().get_Super())) {
				mav.addObject(getCommandName(), org);	
				org.setParentId(-1);
				
				
				//The organisation id changes here:
				BLOrganisation.getInstance().add(org);			
				
				String organisationNameMessage = ControllerUtils.getInstance().tokenizeField(org.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {organisationNameMessage}, locale));	
				//Update all the organizations structures that include this one as a child node
				logger.debug("(4)organisationId = " + org.getOrganisationId());
				BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(org.getOrganisationId());
				
				//Update the context map that specifyies if this organisation has the audit module
				BLOrganisation.getInstance().updateHasAuditContextMap(org.getOrganisationId(), org.getModules());
					
				//add the new audit event
				try {
					//if the organisation is added by the adminIT, the audit event organisationId will be the added organisation id and
					//no first and last name will be sent
					String lastName;
					if (userAuth.isAdminIT()){
						lastName = new String("");
					} else {
						lastName = userAuth.getLastName();
					}
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_ADD_TYPE, userAuth.getFirstName(), lastName
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
						, BLOrganisation.getInstance().getOrgByName(org.getName()).getOrganisationId(), userAuth.getPersonId());
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				// else, it has not right to add an organisation
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			org.setOrganisationId(IConstant.ORGANISATION_ID_NOT_PERSISTENT);
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
		logger.debug("== END ADD OPERATION==");
		
		//Adding the admin
		if(errorMessages.size() == 0){
			mav = handleAdminAdd(request, org);		
			}
		
		return mav;
	}
	
	/**
	 * Method for add branch
	 *
	 * @author Adelina
	 */	
	private ModelAndView handleAddBranch(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleAddBranch - START");
		//this view is used only for showing messages/errors
		ModelAndView mav = new ModelAndView(getFormView());
		Organisation org = (Organisation)command;
		//call add method from the business layer
		try{
			// Get the organisation from the session
			Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			Organisation parent = BLOrganisation.getInstance().get(organisationId);
			
			//the user that is authenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission OM_OrgAddChild, can add a child organisation
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgAddChild())){
				mav.addObject(getCommandName(), org);		
				org.setParentId(parent.getOrganisationId());			
				org.setType(IConstant.NOM_ORGANISATION_TYPE_BRANCH);				
				BLOrganisation.getInstance().add(org);		
				
				String organisationNameMessage = ControllerUtils.getInstance().tokenizeField(org.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {organisationNameMessage}, locale));	
				//Update all the organizations structures that include this organization as a node
				BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(org.getOrganisationId());
				
				//Update the context map that specifyies if this organisation has the audit module
				BLOrganisation.getInstance().updateHasAuditContextMap(org.getOrganisationId(), org.getModules());
				
				//add the new audit event
				try {
					//if the organisation is added by the adminIT, the audit event organisationId will be the added organisation id and
					//no first and last name will be sent
					String lastName;
					if (userAuth.isAdminIT()){
						lastName = new String("");
					} else {
						lastName = userAuth.getLastName();
					}
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_ADD_TYPE, userAuth.getFirstName(), lastName
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
						, BLOrganisation.getInstance().getOrgByName(org.getName()).getOrganisationId(), userAuth.getPersonId());
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				// else, it has not the right to add a child organisation
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
			org.setOrganisationId(IConstant.ORGANISATION_ID_NOT_PERSISTENT);
		}	
		
		//Adding the admin
		if(errorMessages.size() == 0){
			mav = handleAdminAdd(request, org);
			}
		
		logger.debug("handleAddBranch - END");
		return mav;
	}

	/**
	 * Method for add company
	 *
	 * @author Adelina
	 */	
	private ModelAndView handleAddCompany(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleAddCompany - START");
		//this view is used only for showing messages/errors
		ModelAndView mav = new ModelAndView(getFormView());
		Organisation org = (Organisation)command;
		//call add method from the business layer
		
		try{
			// Get the organisation from the session
			Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			Organisation parent = BLOrganisation.getInstance().get(organisationId);
			
			//the user that is authenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission OM_OrgAddChild, can add a child organisation
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgAddChild())){
						
				mav.addObject(getCommandName(), org);
				org.setParentId(parent.getOrganisationId());
				org.setCalendar(parent.getCalendar());
				BLOrganisation.getInstance().add(org);
				
				String organisationNameMessage = ControllerUtils.getInstance().tokenizeField(org.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {organisationNameMessage}, locale));	
				//Update all the organizations structures that include this organization as a node
				BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(org.getOrganisationId());
				
					
				//Update the context map that specifyies if this organisation has the audit module
				BLOrganisation.getInstance().updateHasAuditContextMap(org.getOrganisationId(), org.getModules());
				
				//add the new audit event
				try {
					//if the organisation is added by the adminIT, the audit event organisationId will be the added organisation id and
					//no first and last name will be sent
					String lastName;
					if (userAuth.isAdminIT()){
						lastName = new String("");
					} else {
						lastName = userAuth.getLastName();
					}
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_ADD_TYPE, userAuth.getFirstName(), lastName
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
						, BLOrganisation.getInstance().getOrgByName(org.getName()).getOrganisationId(), userAuth.getPersonId());
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				// else, it has not the right to add a child organisation
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
			org.setOrganisationId(IConstant.ORGANISATION_ID_NOT_PERSISTENT);
		}	
		
		//Adding the admin
		if(errorMessages.size() == 0){
			mav = handleAdminAdd(request, org);
		}
		
		logger.debug("handleAddCompany - END");
		return mav;
	}
	
	/**
	 * Method for get an organisation with modules
	 *
	 * @author Adelina
	 */
	private Organisation handleGet(Integer organisationId, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale, HttpServletRequest request) {
		logger.debug("handleGet - START");
		Organisation org = null;
		try{
			logger.debug("Id organisation: ".concat(organisationId.toString()));
			org = BLOrganisation.getInstance().getOrganisationWithModules(organisationId);		
			logger.debug("organisation = " + org);
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} 
		request.setAttribute(GET_ACTION, true);
		logger.debug("handleGet - END");
		return org;
	}
			
	/**
	 * Method for compose a branch
	 *
	 * @author Adelina
	 */	
	private Organisation composeNewBranch(Integer organisationId, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("composeNewBranch - START");		
		
		Organisation organisation = new Organisation();			
		organisation.setParentId(organisationId);			
		organisation.setType(IConstant.NOM_ORGANISATION_TYPE_BRANCH);		
		
		logger.debug("composeNewBranch - END");
		return organisation;
	}
		
	
	/**
	 * Method for compose new company
	 *
	 * @author Adelina
	 */
	private Organisation composeNewCompany(Integer organisationId, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("composeNewCompany - START");		
		
		Organisation organisation = new Organisation();			
		organisation.setParentId(organisationId);		
		
		logger.debug("composeNewCompany - END");
		return organisation;
	}
	
	/**
	 * Method for update operation
	 *
	 * @author matti_joona
	 * @throws ServletRequestBindingException 
	 */
	private ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws ServletRequestBindingException {
		logger.debug("== UPDATE OPERATION ==");
		ModelAndView mav = new ModelAndView(getFormView());
		Organisation org = (Organisation) command;
		mav.addObject(getCommandName(), org);				
		try{			
			//the user that is authenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission OM_OrgUpdate, can add a child organization
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgUpdate())){
				BLOrganisation.getInstance().updateAll(org);	
				String organisationNameMessage = ControllerUtils.getInstance().tokenizeField(org.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {organisationNameMessage}, locale));
				//Update all the organizations structures that include this organization as a node
				BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(org.getOrganisationId());
				
				//Update the context map that specifyies if this organisation has the audit module
				BLOrganisation.getInstance().updateHasAuditContextMap(org.getOrganisationId(), org.getModules());
			
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_ORGANISATION_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
								, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				// else, it has not the right to update an organization
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("== END UPDATE OPERATION==");
		return mav;
	}
	
	/**
	 * Adds to model required nomenclators
	 * @author Adelina
	 */
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
		logger.debug("referenceData - start");
		
		// used for error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		Organisation organisation = (Organisation) command;		
		Map modelO = new HashMap();
		
		boolean isAdminIT = ((UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isAdminIT();
							
		modelO.put(IS_ADMIN_IT, isAdminIT);		
		
		List<Module> modules = new ArrayList<Module>();
										
		// when an organisation is created, it is created with Organisation Management module
		if(organisation.getModules() == null){
			Module module = BLModule.getInstance().get(IConstant.OM_MODULE);		
			Set<Module> organisationModules = new HashSet();
			organisationModules.add(module);
			organisation.setModules(organisationModules);
		}
		
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		modelO.put(IConstant.REQ_ACTION, action);
				
		Organisation parentNew = null;
		Organisation parent = new Organisation();
		// get the organisationId from the session
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		// if we have an organisationId on the sesseion, that is != -1
		if(organisationId != -1){
			// we get the organisation identifed by the organisationId
			parent = BLOrganisation.getInstance().get(organisationId);
			// if the parentId of this organisaion is != -1
			if(parent.getParentId() != -1){
				// we create the parent organisation
				parentNew = BLOrganisation.getInstance().get(parent.getParentId());
			}
		}
		try{									
			// Adding to model the organisation's type
			modelO.put(ORGANISATION_TYPE, OMContext.getFromContext(IConstant.NOM_ORGANISATION_TYPE));
			// Adding to model the companies group type
			modelO.put(GROUP_COMPANIES_TYPE, OMContext.getFromContext(IConstant.NOM_GROUP_COMPANIES_TYPE));
			
			// if the organisationId from the session, is != -1 and equals the organisaionId of the organisation
			if(organisationId == organisation.getOrganisationId() && organisationId != -1){
				if(parentNew != null){			
					// we put on view parentNew
					modelO.put(PARENT, parentNew.getName());
				}
			} else {
				// else, we put on view parent
				modelO.put(PARENT, parent.getName());
			}
			
			Integer orgId = organisation.getOrganisationId();
			Integer parentId = null;
			if(orgId > 0) {
				parentId = organisation.getParentId();
			} else {
				parentId = parent.getOrganisationId();
			}
								
			// the child company must inherit the parent's modules from where it has to choose
			if((parentId > 0 && orgId > 0) || (ADD_BRANCH.equals(action) || ADD_COMPANY.equals(action))) {					
				try{
					modules = BLModule.getInstance().getModulesForOrganisationWithoutOM(orgId, parentId);					
					logger.debug("modules = " + modules);
				} catch (BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(GET_MODULES_WITHOUT_OM_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}
			} else {				
				// Get all available modules for organisation, without the OM module
				modules = BLModule.getInstance().getModulesWithoutOMForOrganisation(organisation.getOrganisationId());
			}
			
			// Adding to model the organisation models
			modelO.put(ALL_MODULES, modules);
						
		} catch(Exception e){
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - end");
		return modelO;
	}
	/**
	 * 
	 *
	 * @author mitziuro
	 * @handle add a new admin
	 * @param request
	 * @param org
	 * @return
	 * @throws BusinessException
	 */	
	private ModelAndView handleAdminAdd(HttpServletRequest request, Organisation org)throws BusinessException{

		//setting the view
		setViewSetting(PERSON);
		
		// used for error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		Locale locale = request.getLocale();
				
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		//create a new person with the new organisationId
		mav.addObject(ORG, org.getOrganisationId());
		Person person = new Person();
		person.setPersonId(IConstant.PERSON_NEW);
		
		//setting the fake department for the new admin
		try{
			Department dept = BLDepartment.getInstance().getFakeForOrganization(org.getOrganisationId());
			request.setAttribute(FAKE_DEPARTMENT_ID, dept.getDepartmentId());		
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(GET_FAKE_DEPARTMENT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		//setting the admin role
		try{
			Role role = BLRole.getInstance().getRoleByNameAndOrg(org.getOrganisationId(), IConstant.OM_ADMIN);
			request.setAttribute(ADMIN_ROLE_ID, role.getRoleId());
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(GET_ROLE_BY_NAME_AND_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
				
		//we add the admin so we use only parts of the personForm
		mav.addObject(IS_CREATE_ADMIN, true);		
		mav.addObject(MODEL_DEFAULT_USER_GROUP_ID, BLUserGroup.getInstance().getDefaultUserGrup(org.getOrganisationId()).getUserGroupId());
		mav.addObject(getCommandName(), person);
		return mav;
	}
	
	/**
	 * Setting the rendering options for the controller 
	 * If we have added sucessfuly the organisation we display the admin form
	 * Else we display the errors and the organisationForm
	 *
	 * @author mitziuro
	 * @param option
	 */
	private void setViewSetting(String option){
		//if we have the organisation view or the personForm for adding the admin in case of sucess
		if(option.compareTo(ORGANISATION) == 0){
			setCommandClass(Organisation.class);
			setCommandName("organisationBean");
			setSuccessView("OrganisationForm");
		} else {
			setCommandClass(Person.class);
			setSuccessView("PersonForm");
			setCommandName("personBean");
		}		
	}

}
