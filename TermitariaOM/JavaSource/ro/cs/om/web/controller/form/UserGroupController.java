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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLUserGroup;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * 
 * @author Coni
 *
 */
public class UserGroupController extends RootSimpleFormController{
	
	public static final String FORM_VIEW		= "UserGroup";
	public static final String SUCCESS_VIEW		= "UserGroup";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String GET_ERROR 		= "usergroup.get.error";
	private static final String UPDATE_MESSAGE 	= "usergroup.update.message";
	private static final String UPDATE_ERROR 	= "usergroup.update.error";
	private static final String ADD_MESSAGE 	= "usergroup.add.message";
	private static final String ADD_ERROR 		= "usergroup.add.error";
	private static final String GENERAL_ERROR	= "usergroup.general.error";	
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ACTION 			= "action";
	private static final String EDIT 			= "edit";
	private static final String ID 				= "id";
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------
	private static final String PERSONS			= "ALL_PERSONS";
	private static final String GET_ACTION 		= "GET_ACTION";	
	
	public UserGroupController(){
		setCommandName("userGroupBean");
		setCommandClass(UserGroup.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * Registering Custom Editors to this controller for 
	 * better binding request parameters.
	 * (More details on each registering)
	 * 
	 * @author coni
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{
		logger.debug("initBinder - START");

		// Custom editor for bind String[] to Set<Person> for persons property
		binder.registerCustomEditor(Set.class, "persons", new CustomCollectionEditor(Set.class){
			protected Object convertElement( Object element ){
				if (element != null){
					Integer personId = new Integer((String)element);
					logger.debug("persons: convertElement for person: " + personId);
					Person person = null;
					try {
						person = BLPerson.getInstance().get(personId);
					} catch (BusinessException bexc) {
						logger.debug("Error while retrieving the person with id: ".concat(String.valueOf(personId)));
					}
					return person;
				}
				return null;
			}
		});
		logger.debug("initBinder - END");
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception{
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages 	= new ArrayList<String>();
		Integer organisationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		UserGroup userGroup = null;
		
		try{
			//check if I have to EDIT a role
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, ACTION) != null &&
					EDIT.equals(ServletRequestUtils.getStringParameter(request, ACTION))){
				logger.debug("formBackingObject: EDIT");
			
				//get the group of users with the specified userGroupId
				userGroup = handleGet(ServletRequestUtils.getIntParameter(request, ID).intValue(), request);
				if (userGroup == null){
					userGroup = new UserGroup();
				}
				logger.debug("formBackingObject - END");
			} else {
				logger.debug("formBackingObject: ADD");
				userGroup = new UserGroup();

				//setting the organization for the new user group
				if (organisationId != null){
					userGroup.setOrganisation(BLOrganisation.getInstance().get(organisationId));
				}
				//get all the organisation's persons
				logger.debug("formBackingObject - END");	
			}											
		} catch (BusinessException be) {
			logger.error("formBackingObject", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		return userGroup;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		
		ModelAndView mav = new ModelAndView();
		UserGroup userGroup = (UserGroup) command;
		
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		// check if the user group object has userGroupId
		if (userGroup.getUserGroupId() != -1){
			// if I have userGroupId, it means that I have "update" action
			logger.debug("onSubmit - handleUpdate");
			mav =  handleUpdate(userGroup, request, locale, errorMessages, infoMessages);
		} else {
			// if I don't have userGroupId, it means that I have "add" action
			logger.debug("onSubmit - handleAdd");
			mav =  handleAdd(userGroup, locale, errorMessages, infoMessages, request);
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addAllObjects(referenceData(request, command, errors));
		
		logger.debug("onSubmit - END");
		return mav;
		
	}
	
	/**
	 * retrieves the group of users with the specified id
	 * 
	 * @author Coni
	 * @param userGroupId
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	public UserGroup handleGet(int userGroupId, HttpServletRequest request) throws BusinessException{
		logger.debug("handleGet - START");
		UserGroup userGroup = null;
		try{
			userGroup = BLUserGroup.getInstance().getAll(userGroupId);
		} catch (Exception bexc) {
			throw new BusinessException(ICodeException.USERGROUP_GET, bexc);
		}
		request.setAttribute(GET_ACTION, true);
		return userGroup;
	}
	
	
	/**
	 * updates the group of users
	 * @author Coni
	 * @param userGroup
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleUpdate(UserGroup userGroup, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getFormView());
		try{
			//add the command object
			mav.addObject(getCommandName(), userGroup);
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_UpdateUserGroup())) {
				//update the user group in the database
				BLUserGroup.getInstance().updateUserGroup(userGroup);			
				String userGroupNameMessage = ControllerUtils.getInstance().tokenizeField(userGroup.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);			
				infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, new Object[] {userGroupNameMessage}, locale));									
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_USERGROUP_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_USERGROUP_UPDATE_MESSAGE, new Object[] {userGroup.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_USERGROUP_UPDATE_MESSAGE, new Object[] {userGroup.getName(), org.getName()}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
		} catch (BusinessException be) {
			logger.error("Exception while updating user group with id: ".concat(String.valueOf(userGroup.getUserGroupId())), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error("Exception while updating user group with id: ".concat(String.valueOf(userGroup.getUserGroupId())), e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	/**
	 * adds a group of users
	 * 
	 * @author Coni
	 * @param userGroup
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @param request
	 * @return
	 */
	private ModelAndView handleAdd(UserGroup userGroup, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request){
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getFormView());
		try{			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			//add the command object
			mav.addObject(getCommandName(), userGroup);

			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_AddUserGroup())) {
				//setting the user group status to normal
				userGroup.setStatus(IConstant.NOM_USER_GROUP_NORMAL);
				//insert the user group in the database
				BLUserGroup.getInstance().addUserGroup(userGroup);
				String userGroupNameMessage = ControllerUtils.getInstance().tokenizeField(userGroup.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);					
				infoMessages.add(messageSource.getMessage(ADD_MESSAGE, new Object[] {userGroupNameMessage}, locale));								
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_USERGROUP_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_USERGROUP_ADD_MESSAGE, new Object[] {userGroup.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_USERGROUP_ADD_MESSAGE, new Object[] {userGroup.getName(), org.getName()}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
		} catch (BusinessException be) {
			logger.error("Exception while adding user group" , be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("Exception while adding user group" , e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		logger.debug("handleUpdate - END");
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
			UserGroup userGroup = (UserGroup) command;									
			Integer orgId = userGroup.getOrganisation().getOrganisationId();
			//get all the organisation's persons that are not in a user group 
			List<Person> allAvailablePersons = null;
			if (orgId != null){
				allAvailablePersons = BLPerson.getInstance().getPersonsByOrganizationAndUserGroup(orgId, userGroup.getUserGroupId());
			}			
			
			//loading the persons list
			modelO.put(PERSONS, allAvailablePersons);
			
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
