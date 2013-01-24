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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLJob;
import ro.cs.om.business.BLModule;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLPicture;
import ro.cs.om.business.BLRole;
import ro.cs.om.business.BLUserGroup;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.common.RandomDataProvider;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Picture;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.utils.file.FileUtils;
import ro.cs.om.utils.image.ImageUtils;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;


/**
 * SimpleFormController used to manage a Person entity.
 * 
 * @author dan.damian
 *
 */
public class PersonFormController extends RootSimpleFormController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY							= "person.";
	private final String ADD_SUCCESS						= ROOT_KEY.concat("add.success");
	private final String UPDATE_SUCCESS						= ROOT_KEY.concat("update.success");
	private final String UPDATE_PASSWORD_SUCCESS			= ROOT_KEY.concat("update.password.success");
	private final String RESET_PASSWORD_SUCCESS				= ROOT_KEY.concat("reset.password.success");
	private final String MAIL_SEND_SUCCESS					= ROOT_KEY.concat("mail.success");
	
	private final String ADD_ERROR							= ROOT_KEY.concat("add.error");
	private final String GET_ERROR							= ROOT_KEY.concat("get.error");
	private final String UPDATE_ERROR						= ROOT_KEY.concat("update.error");	
	private final String UPDATE_PASSWORD_ERROR				= ROOT_KEY.concat("update.password.error");
	private final String GENERAL_ERROR						= ROOT_KEY.concat("general.error");	
	private final String PICTURE_ADD_ERROR					= ROOT_KEY.concat("picture.error");	
	
	
	//------------------------ATTRIBUTES--------------------------------------------------------------
	private final String PERSON_ID							= "personId";
	private final String DEPARTMENT_ID						= "departmentId";	
	private final String PERSON_PASSWORD					= "password";
	private final String PERSON_PASSWORD_CONFIRM			= "passwordConfirm";
	private final String CMD_UPDATE_PASSWORD				= "UPDATE_PASSWORD";
	private final String CMD_RESET_PASSWORD					= "RESET_PASSWORD";			

	//------------------------MODEL-------------------------------------------------------------------
	private final String MODEL_PERSON_ROLES 					= "PERSON_ROLES";
	private final String MODEL_MONTHS 							= "Months";
	private final String MODEL_DAYS 							= "Days";
	private final String MODEL_YEARS							= "Years";
	private final String MODEL_DEPARTMENT_NAME					= "DEPARTMENT_NAME";	
	private final String MODEL_ALL_DEPARTMENTS					= "ALL_DEPARTMENTS";
	private final String MODEL_ALL_ROLES 						= "MODULES_ALL_ROLES";
	private final String MODEL_FAKE_DEPARTMENT_ID				= "FAKE_DEPARTMENT_ID";
	private final String MODEL_JOBS								= "JOBS";
	private final String PERSON_JOBS							= "PERSON_JOBS";
	private final String MODEL_ALL_USER_GROUPS					= "ALL_USER_GROUPS";
	private final String MODEL_DEFAULT_USER_GROUP_ID			= "DEFAULT_USER_GROUP_ID";
	
	
	//---------------------COLATERAL-VIEWS------------------------------------------------------------
	private final String  VIEW_CHANGE_PASSWORD					= "Person_ChangePassword";
	private final String  VIEW_MESSAGES 					    = "Messages";					
		
	//---------------------SECURITY ------------------------------------------------------------------
	private final String URL_MY_PROFILE							= "PersonMyProfile.htm";
	//private final String URL_PERSON	 							= "URL_PERSON";
	private final String NEW_ADMIN								= "NEWADMIN";
	private final String SAVE_ADMIN								= "SAVEADMIN";
	private final String FAKE_DEPARTMENT_ID 					= "FAKE_DEPARTMENT_ID";
	private final String ADMIN_ROLE_ID							= "ADMIN_ROLE_ID";
	private static final String ORGANISATION 					= "ORG";	
	private static final String ORGANISATION_SESSION            = "ORGANISATION_SESSTION";	
	private final String URL_CREATE_ADMIN						= "/".concat(IConstant.APP_CONTEXT).concat("/CreateAdmin.htm");
	private final String IS_CREATE_ADMIN						= "IS_CREATE_ADMIN";
	private final String IS_ADMIN_IT							= "IS_ADMIN_IT";	
	private final String ADMIN_IT								= "ADMIN_IT";
	private final String CURRENT_YEAR							= "CURRENT_YEAR";
	private final String CURRENT_MONTH							= "CURRENT_MONTH";
	private final String CURRENT_DAY							= "CURRENT_DAY";
	private static final String DEPT_JOBS						= "DEPT_JOBS";
	private static final String GET_ACTION 						= "GET_ACTION";
		
	
	/**
	 * Constructor - Has the basic settings for this controller.
	 */
	public PersonFormController(){
		setCommandName("personBean");
		setCommandClass(Person.class);
		setFormView("PersonForm");
		setSuccessView("PersonForm");
	}
	
	/**
	 * Registering Custom Editors to this controller for 
	 * better binding request parameters.
	 * (More details on each registering)
	 * 
	 * @author dan.damian
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
			throws ServletException {
		logger.debug("start - initBinder");
							
		// Custom editor for bind String[] to Set<Role> for roles property
		binder.registerCustomEditor(Set.class, "roles", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   Integer roleId = new Integer((String)element);
	                   logger.debug("roles: convertElement for Id " + roleId);
	                   Role role = null;
						try {
							role = BLRole.getInstance().getWithModule(roleId);
						} catch (BusinessException e) {
							logger.debug("Ups there was an error");
						}
	                   return role;
	               }
	               return null;
	           }
	     });

		// Custom editor for bind String[] to Set<Department> for depts property		
		binder.registerCustomEditor(Set.class, "depts", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   Integer deptId = new Integer((String)element);
	                   logger.debug("depts: convertElement for Id " + deptId);
	                   Department dept = null;
						try {
							dept = BLDepartment.getInstance().get(deptId);
						} catch (BusinessException e) {
							logger.debug("Ups there was an error");
						}
	                   return dept;	               
	               }
	               return null;
	           }
	     });
		
		// Custom editor for bind String[] to Set<UserGroup> for userGroups property		
		binder.registerCustomEditor(Set.class, "userGroups", new CustomCollectionEditor(Set.class) {
	           protected Object convertElement(Object element) {
	               if (element != null) {
	                   Integer userGroupId = new Integer((String)element);
	                   logger.debug("userGroups: convertElement for Id " + userGroupId);
	                   UserGroup userGroup = null;
						try {
							userGroup = BLUserGroup.getInstance().get(userGroupId);
						} catch (BusinessException e) {
							logger.debug("Ups there was an error");
						}
	                   return userGroup;	               
	               }
	               return null;
	           }
	     });
		
		// to actually be able to convert Multipart instance to byte[]
		// we have to register a custom editor
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());

		// now Spring knows how to handle multipart object and convert them
		
		}	
	
	
	/**
	 * Runs at every cycle.
	 * Creates the formBackingObject for this form.
	 * Rquest parameters will be bind on this object.
	 * 
	 * @author dan.damian
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject  BEGIN");
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		Locale locale = RequestContextUtils.getLocale(request);
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
						
		String URI = request.getRequestURI();
		logger.debug("URI = " + URI);
		
		boolean isCreateAdmin;
					
		if(URI.equals(URL_CREATE_ADMIN)){
			logger.debug("create admin");
			isCreateAdmin = true;
		} else {
			logger.debug("not create admin");
			isCreateAdmin = false;
		}
					
		if(isCreateAdmin){
			if(NEW_ADMIN.equals(action) || SAVE_ADMIN.equals(action)){
				Integer organisationId;
				
				if(request.getParameter(ORGANISATION) != null) {				
					organisationId = Integer.valueOf(request.getParameter(ORGANISATION));					
				} else {
					organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);					
				}
				
				logger.debug("formBackingObject organisationId = " + organisationId);
											
				request.setAttribute(ORGANISATION, organisationId);
			}
		} 
		
		//FormBackingObject
		Person person = new Person();
	
		try {
			//Person's ID
			Integer personId = ServletRequestUtils.getIntParameter(request, PERSON_ID);
			//Check to see how to create formBackingObject
			if( personId != null && personId > 0) { 
				//Retrieved form DB
				logger.debug("formBackingObject: Get");
				person = handleGet(personId, request, errorMessages, infoMessages, locale);
			} else {
				//Create a new one
				logger.debug("formBackingObject: New");
				if(NEW_ADMIN.equals(action) || SAVE_ADMIN.equals(action) ){
					// if we have to create a new person that is an admin
					logger.debug("action new admin = " + action);
					person = handleAddNewAdmin(request, errorMessages, infoMessages, locale);
				} else {
					// if we have to create a new regular person
					logger.debug("action new = " + action);
					person = handleAddNew(request, errorMessages, infoMessages, locale);									
				}
			}
			
		} catch (Exception ex) {
			logger.error("formBackingObject", ex);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command: ".concat((person!=null ? person.toString(): "null")));		
		return person;
	}
	

	/**
	 * It runs on every submit (request Method = POST)
	 * It detects the case by action parameter and dispatch to and appropriate handler.
	 * 
	 * @author dan.damian
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit  BEGIN");
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		Person person = (Person) command;	
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);		
		
		//redirect regarding the operation
		if (IConstant.CMD_SAVE.equals(action)){
			if (person.getPersonId() > 0) {
				mav = handleUpdate(request, command, errorMessages, infoMessages, locale);	
			}else {
				mav = handleAdd(request, command, errorMessages, infoMessages, locale);
			}
		} else if(action.equals(SAVE_ADMIN)){
			logger.debug("save admin");	
			if(person.getPersonId() > 0) {
				mav = handleUpdateAdmin(request, command, errorMessages, infoMessages, locale);
			} else {
				mav = handleAddAdmin(request, command, errorMessages, infoMessages, locale);
			}
		} else if (CMD_UPDATE_PASSWORD.equals(action)) {
			mav = handleUpdatePassword(request, response, command, errors, errorMessages, infoMessages, locale);
		} else if(CMD_RESET_PASSWORD.equals(action)){
			mav = handleResetPassword(request, response, command, errors, errorMessages, infoMessages, locale);
		}
		
		//Add referenceData objects to model
		mav.addAllObjects(referenceData(request, command, errors));
		
		request.getAttribute(PERSON_JOBS);
		
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("onSubmit END");
		
		return mav;
		
	}
		
	/**
	 * Runs in formBackingObject Handles Add New Person.
	 * @author dan.damian 
	 */
	private Person handleAddNew(HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws ServletRequestBindingException {
		logger.debug("== ADD NEW  BEGIN ==");
		Person person = new Person();
		try{	
			
			person.setPersonId(-1);
			person.setSex('M');
			//adaug persoana la un departament anume
			Set<Department> depts = new HashSet<Department>();
			
			//add the department to the person's list of departments
			Integer departmentId = ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID);
			Department dept =  null;
			if (departmentId != null) {
				dept = BLDepartment.getInstance().get(departmentId);
				request.setAttribute(DEPARTMENT_ID, departmentId);
				request.setAttribute(MODEL_DEPARTMENT_NAME, dept.getName());
				depts.add(dept);
			} 
								
			person.setDepts(depts);
			
			Set<Role> roles = new HashSet<Role>();	
			
			// when a person is created, it is created with OM_BASIC role
			Role role = BLRole.getInstance().getRoleByNameAndOrg(ControllerUtils.getInstance().getOrganisationIdFromSession(request), IConstant.OM_USER);				
			roles.add(role);
							
			person.setRoles(roles);
			
			//we set the intial userGroups
			Set<UserGroup> userGroups = new HashSet<UserGroup>();
			person.setUserGroups(userGroups);
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex){
			logger.error("", ex);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
		
		logger.debug("== ADD NEW END ==");
		// never returns null
		return person;
	}
	
	/**
	 * 
	 * @author dan.damian 
	 * @since Mar 24, 2009
	 * @param request
	 * @param response
	 * @param command
	 * @param arg3
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ModelAndView handleAdd(HttpServletRequest request, Object command, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("== ADD  BEGIN ==");
		ModelAndView mav = new ModelAndView(getFormView());
		Person person = (Person) command;
		
		// if we have a fake department and at least one other department,
		// the fake department must be removed from the set of department
		// otherwise the person remains in the fake department 		
		boolean isFake = false;
		Department fake = new Department();
		Set<Department> depts = person.getDepts();				
		if(depts != null && depts.size() > 1) {
			for(Department dept : depts) {
				if(dept.getStatus() == IConstant.NOM_DEPARTMENT_FAKE) {
					isFake = true;
					fake = dept;
					break;
				}
			}										
		}
		
		if(isFake){
			Set<Department> d = person.getDepts();
			d.remove(fake);			
			person.setDepts(d);			
		}		
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_PersonAdd())){			
				// Adds person to db
				person.setDeptWithJob(getMap(person.getDepts(), request));
				
				Set<Role> roles = person.getRoles();
				for(Role role : roles) {
					logger.debug("role = " + role);
				}
				// when a person is added, adds with the OM_BASIC role
				Role role = BLRole.getInstance().getRoleByNameAndOrg(ControllerUtils.getInstance().getOrganisationIdFromSession(request), IConstant.OM_USER);				
				roles.add(role);
											
				person.setRoles(roles);
												
				//setting the default organization user group (all persons are assigned to the default user group)
				UserGroup defaultUserGroup = BLUserGroup.getInstance().getDefaultUserGrup(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
				boolean isDefaultUserGroup =  false;
				for (UserGroup userGroup : person.getUserGroups()){
					if (userGroup.getStatus() == IConstant.NOM_USER_GROUP_DEFAULT){
						isDefaultUserGroup = true;
					}
				}
				if ( defaultUserGroup != null && !isDefaultUserGroup){
					if (person.getUserGroups() != null){
						person.getUserGroups().add(defaultUserGroup);
					} else {
						Set<UserGroup> userGroups = new HashSet<UserGroup>();
						userGroups.add(defaultUserGroup);
						person.setUserGroups(userGroups);
					}
				}
											
				//adding the person
				BLPerson.getInstance().add(person);

				//adding the picture
				addPicture(request, command, errorMessages, infoMessages, locale);
				BLPerson.getInstance().excludeDefaultUserGroup(person);							
				
				// Add command object to model
				mav.addObject(getCommandName(), person);
				
				String personFirstNameMessages = ControllerUtils.getInstance().tokenizeField(person.getFirstName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				String personLastNameMessages = ControllerUtils.getInstance().tokenizeField(person.getLastName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {personFirstNameMessages, personLastNameMessages}, locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_ADD_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_ADD_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
				
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error("", bexc);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex){
			logger.error("", ex);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
	
		logger.debug("== ADD  BEGIN ==");
		return mav;
	}
	
	
	/**
	 * 
	 * @author dan.damian 
	 * @since Mar 24, 2009
	 * @param personId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private Person handleGet(Integer personId, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("== GET  BEGIN ==");
		Person person = null;
		try{
			if (ControllerUtils.getInstance().fromThisUrl(URL_MY_PROFILE, request)) {
				logger.debug("url = " + ControllerUtils.getInstance().fromThisUrl(URL_MY_PROFILE, request));
				personId = ((UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPersonId();
				boolean isAdminIT = ((UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isAdminIT();								
				logger.debug("isAdminIT = " + isAdminIT);
				// for Admin It
				if(isAdminIT) {
					logger.debug("admin it");
					person = BLPerson.getInstance().getWithPicture(personId);							
					logger.debug("== GET END ==");					
					return person;
				}
			}					
			person = BLPerson.getInstance().getAll(personId);							
			//exclude the default user group from the list of user groups
			BLPerson.getInstance().excludeDefaultUserGroup(person);
			
			// if the form is submited and the person != null, initialize the roles and user groups of the person
//			if (isFormSubmission(request) && person != null) {
//				person.setRoles(new HashSet<Role>());
//				person.setUserGroups(new HashSet<UserGroup>());
//			}
			
			Integer departmentId = ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID);			
			request.setAttribute(DEPARTMENT_ID, departmentId);				
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex){
			logger.error("", ex);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		if (person == null) {
			person = new Person();
			person.setFirstName("-");
			person.setLastName("-");
			person.setDepts(new HashSet<Department>());
			person.setRoles(new HashSet<Role>());
		}
		request.setAttribute(GET_ACTION, true);
		logger.debug("== GET END ==");
		// never returns null
		return person;
	}
	
	
	/**
	 * 
	 * @author dan.damian 
	 * @since Mar 24, 2009
	 * @param request
	 * @param response
	 * @param command
	 * @param arg3
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private ModelAndView handleUpdate(HttpServletRequest request, Object command, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("== UPDATE  BEGIN ==");
		ModelAndView mav = new ModelAndView(getFormView());
		Person person = (Person) command;		
		
		if (ControllerUtils.getInstance().fromThisUrl(URL_MY_PROFILE, request)) {
			boolean isAdminIT = ((UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isAdminIT();								
					
			// for Admin It and User
			if(isAdminIT || (!person.getIsAdmin())) {
				try{
					BLPerson.getInstance().updateSimple(person);		
					
					String personFirstNameMessages = ControllerUtils.getInstance().tokenizeField(person.getFirstName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
					
					String personLastNameMessages = ControllerUtils.getInstance().tokenizeField(person.getLastName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
					
					infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {personFirstNameMessages, personLastNameMessages}, locale));				
				} catch(BusinessException bexc){
					logger.error(bexc.getMessage(), bexc);
					errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
				} catch(Exception ex){
					logger.error("", ex);
					errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
				}
				return mav;
			} 
		}
		
		
			// if we have a fake department and at least one other department,
			// the fake department must be removed from the set of department
			// otherwise the person remains in the fake department 		
			boolean isFake = false;
			Department fake = new Department();
			Set<Department> depts = person.getDepts();				
			if(depts != null && depts.size() > 1) {
				for(Department dept : depts) {
					if(dept.getStatus() == IConstant.NOM_DEPARTMENT_FAKE) {						
						isFake = true;
						fake = dept;
						break;
					}
				}										
			}
			
			if(isFake){
				Set<Department> d = person.getDepts();
				d.remove(fake);			
				person.setDepts(d);			
			}		
			
			try{
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
				
				//When user edits his profile and has the permission OM_PersonUpdate
				//he can change all available attributes.
				boolean canEditOwnProfileAdvancedMode = false;
				
				
				// ****************** Security *******************************
				if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_PersonUpdate())){
					canEditOwnProfileAdvancedMode = true;
				}
				
				if (ControllerUtils.getInstance().fromThisUrl(URL_MY_PROFILE, request)) {	
					if (canEditOwnProfileAdvancedMode) {
						person.setDeptWithJob(getMap(person.getDepts(), request));					
					} 								
				} else {
					if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_PersonUpdate()) ) {
						person.setDeptWithJob(getMap(person.getDepts(), request));					
					} else {
						errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
					}
				}
				
				//setting the default user group (all persons are assigned to the default user group)
				UserGroup defaultUserGroup = BLUserGroup.getInstance().getDefaultUserGrup(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
				boolean isDefaultUserGroup =  false;
				for (UserGroup userGroup : person.getUserGroups()){
					if (userGroup.getStatus() == IConstant.NOM_USER_GROUP_DEFAULT){
						isDefaultUserGroup = true;
					}
				}
				if ( defaultUserGroup != null && !isDefaultUserGroup ){
					if (person.getUserGroups() != null){
						person.getUserGroups().add(defaultUserGroup);
					} else {
						Set<UserGroup> userGroups = new HashSet<UserGroup>();
						userGroups.add(defaultUserGroup);
						person.setUserGroups(userGroups);
					}
				}
				Set<Role> roles = person.getRoles();
				for(Role role : roles) {
					logger.debug("role = " + role);
				}			
							
				// when a person is added, adds with the OM_BASIC role
				Role role = BLRole.getInstance().getRoleByNameAndOrg(ControllerUtils.getInstance().getOrganisationIdFromSession(request), IConstant.OM_USER);				
				roles.add(role);
				
				person.setRoles(roles);
							
				logger.debug(person);	
				
				
				BLPerson.getInstance().update(ControllerUtils.getInstance().getOrganisationIdFromSession(request), person);
				//Exclude Fake Department for presentation
				BLPerson.getInstance().excludeFakeDepartment(person);
				//Exclude the default user group from the list of user groups
				BLPerson.getInstance().excludeDefaultUserGroup(person);
				// Add command object to model
				mav.addObject(getCommandName(), person);
				// Add person's roles grouped by modules to model
				request.setAttribute(MODEL_PERSON_ROLES, BLPerson.getInstance().groupRolesByModules(person.getRoles()));
				
								String personFirstNameMessages = ControllerUtils.getInstance().tokenizeField(person.getFirstName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				String personLastNameMessages = ControllerUtils.getInstance().tokenizeField(person.getLastName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {personFirstNameMessages, personLastNameMessages}, locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
				
			} catch(BusinessException bexc){
				logger.error(bexc.getMessage(), bexc);
				errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
			} catch(Exception ex){
				logger.error("", ex);
				errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
			}
			logger.debug("== UPDATE END ==");		
		
			return mav;

	}
				
	/**
	 * 
	 * @author Adelina
	 * @param request
	 * @param response
	 * @param command
	 * @param bindEx
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return ModelAndView
	 */	
	private ModelAndView handleResetPassword(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindEx, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale){
		logger.debug("handleResetPassword - START-");	
		ModelAndView mav = new ModelAndView(VIEW_MESSAGES);
		Person person = (Person) command;
		try{
			
			//the user that is authenticated
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission OM_ResetPassword, can reset the password
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ResetPassword())) {			
				logger.debug("Sending mail to Person with id: ".concat(String.valueOf(person.getPersonId())));
				mav.addObject(getCommandName(), person);
	    		String password = BLPerson.getInstance().resetPassword(person);

	    		BLPerson.getInstance().sendPasswordByEmailToUser(person, password, locale);
	    		
	    		String personEmailMessages = ControllerUtils.getInstance().tokenizeField(person.getEmail(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
	    		
    			infoMessages.add(messageSource.getMessage(MAIL_SEND_SUCCESS, new Object[] {personEmailMessages}, locale));
    			
    			String personFirstNameMessages = ControllerUtils.getInstance().tokenizeField(person.getFirstName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				String personLastNameMessages = ControllerUtils.getInstance().tokenizeField(person.getLastName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(RESET_PASSWORD_SUCCESS, new Object[] {personFirstNameMessages, personLastNameMessages}, locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else{ 
				// else, it has not right to reset the password
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(UPDATE_PASSWORD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex) {
			logger.error("referenceData", ex);
			errorMessages.add(messageSource.getMessage(UPDATE_PASSWORD_ERROR, new Object[] {ex.getMessage(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("handleResetPassword - END -");
	
		return mav;
	}
	
	/**
	 * 
	 * @author dan.damian 
	 * @since Mar 24, 2009
	 * @param request
	 * @param response
	 * @param command
	 * @param arg3
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private ModelAndView handleUpdatePassword(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("== CHANGE PASSWORD  BEGIN ==");
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		Person person = (Person) command;
		try{
			
			logger.debug(person);
				
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			// Get password
	    	String password = ServletRequestUtils.getRequiredStringParameter(request, PERSON_PASSWORD);
	    	// Get password confirmation
	    	String passwordConfirm = ServletRequestUtils.getRequiredStringParameter(request, PERSON_PASSWORD_CONFIRM);
	    	
	    	if (StringUtils.hasLength(password) && password.equals(passwordConfirm)) {
				person.setPassword(password);
				person.setPasswordConfirm(passwordConfirm);
				// Committing changes to the db
				BLPerson.getInstance().updateCredentials(person);
			}
			
			String personFirstNameMessages = ControllerUtils.getInstance().tokenizeField(person.getFirstName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
			
			String personLastNameMessages = ControllerUtils.getInstance().tokenizeField(person.getLastName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
			
			infoMessages.add(messageSource.getMessage(UPDATE_PASSWORD_SUCCESS, new Object[] {personFirstNameMessages, personLastNameMessages}, locale));
			
			//add the new audit event
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
						, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
						, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (BusinessException exc){
				logger.error(exc);
			}
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(UPDATE_PASSWORD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			errorMessages.add(messageSource.getMessage(UPDATE_PASSWORD_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
	
		logger.debug("== CHANGE PASSWORD END ==");
		return mav;
	}
	
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		logger.debug("referenceData  BEGIN");
		
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();	
		
		Map<String, Object> modelAux = new HashMap<String, Object>();
		
		Person person = (Person) command;
		
		Date date = new Date();
		SimpleDateFormat simpleDateYearFormat = new SimpleDateFormat("yyyy");	
		SimpleDateFormat simpleDateMonthFormat = new SimpleDateFormat("MM");	
		SimpleDateFormat simpleDateDayFormat = new SimpleDateFormat("dd");
			//if the person is an adminidtrator, then the birthdate may be null
		if(person.getPersonId() == -1 || person.getBirthDate() == null) {		
			modelAux.put(CURRENT_YEAR, Integer.valueOf(simpleDateYearFormat.format(date)));
			modelAux.put(CURRENT_MONTH, new Integer(0));
			modelAux.put(CURRENT_DAY, new Integer(1));
		} else {			
			modelAux.put(CURRENT_YEAR, Integer.valueOf(simpleDateYearFormat.format(person.getBirthDate())));
			modelAux.put(CURRENT_MONTH, Integer.valueOf(simpleDateMonthFormat.format(person.getBirthDate())) - 1);			
			modelAux.put(CURRENT_DAY, Integer.valueOf(simpleDateDayFormat.format(person.getBirthDate())));
		}
		
		int personId = person.getPersonId();		
						
		boolean isAdminIT = ((UserAuth)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isAdminIT();
		
		modelAux.put(ADMIN_IT, isAdminIT);			
		
		if (ControllerUtils.getInstance().fromThisUrl(URL_MY_PROFILE, request)) {			
			// for Admin It
			if(isAdminIT){		
				logger.debug("is admin it");				
				List<List> result = ControllerUtils.getInstance().getMonthsAndDaysAndYears(request, messageSource);
				modelAux.put(IS_ADMIN_IT, isAdminIT);
				modelAux.put(MODEL_DAYS, result.get(0));
				modelAux.put(MODEL_MONTHS, result.get(1));	
				modelAux.put(MODEL_YEARS, result.get(2));
				
				//putting the commandBean
				modelAux.put(getCommandName(), person);	
				
				//Set flag to know if a person is editing his profile
				if (ControllerUtils.getInstance().fromThisUrl(URL_MY_PROFILE, request)) {
					modelAux.put("MY_PROFILE", new Object());
				}		
				
				modelAux.put(ORGANISATION_SESSION, ControllerUtils.getInstance().getOrganisationIdFromSession(request));
			
				return modelAux;
			}
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			
			// ****************** Security *******************************
			if(!userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ViewPersonRoleDepartmentDetails())){
				List<String> jobs = new ArrayList<String>();
				StringBuffer sb = new StringBuffer();	
				Map<Department,Job> deptWithJob = person.getDeptWithJob();
				if(deptWithJob == null) {
					sb.append("-");
					jobs.add(sb.toString());	
				} else {					
					Set<Department> departments = deptWithJob.keySet();
					for(Department d : departments) {
						sb = new StringBuffer();
						sb.append(deptWithJob.get(d).getName().concat(" - ").concat(d.getName()).concat(" "));
						jobs.add(sb.toString());						
					}
				}
				modelAux.put(DEPT_JOBS, jobs);				
			}
		} 	
												
		String URI = request.getRequestURI();
				
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);			
		modelAux.put(IConstant.REQ_ACTION, action);
				
		boolean isCreateAdmin;
						
		if(URI.equals(URL_CREATE_ADMIN)){
			isCreateAdmin = true;
		} else {
			isCreateAdmin = false;
			//retrieving all the organization's user groups except the default one
			List<UserGroup> allOrganizationUserGroups = BLUserGroup.getInstance().getUserGroupByOrganizationAndPerson(ControllerUtils.getInstance().getOrganisationIdFromSession(request), personId);				
			modelAux.put(MODEL_ALL_USER_GROUPS, allOrganizationUserGroups);
		}
		
		//add the department to the person's list of departments
		Integer departmentId = ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID);
		Department department =  null;
		if (departmentId != null) {
			department = BLDepartment.getInstance().get(departmentId);
			modelAux.put(MODEL_DEPARTMENT_NAME, department.getName());			
		} 
		
		modelAux.put(IS_CREATE_ADMIN, isCreateAdmin);
		
		try {						
			
			//putting the commandBean
			modelAux.put(getCommandName(), person);			
			
			Integer organisationId = null;
			//If we modify a person from a branch we set the organisationId
			if(request.getParameter(ORGANISATION) != null){
				organisationId = Integer.valueOf(request.getParameter(ORGANISATION));
				//if we have organisation we put it on view for the case when the save fails
				modelAux.put(ORGANISATION, request.getParameter(ORGANISATION));
			} else {
				organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			}
			
			modelAux.put(MODEL_DEFAULT_USER_GROUP_ID, BLUserGroup.getInstance().getDefaultUserGrup(organisationId).getUserGroupId());
			
			logger.debug("default usergroupId = " + BLUserGroup.getInstance().getDefaultUserGrup(organisationId).getUserGroupId());
			
			if(!isCreateAdmin){
				
				BLPerson.getInstance().excludeFakeDepartment(person);
				// Add to person's 0 roles grouped by modules
				modelAux.put(MODEL_PERSON_ROLES, BLPerson.getInstance().groupRolesByModulesAndOrganization(person.getRoles(), organisationId));
														
				modelAux.put(MODEL_FAKE_DEPARTMENT_ID, BLDepartment.getInstance().
							getFakeForOrganization(organisationId).getDepartmentId());
				
				//we put all the jobs and the person's job
				modelAux.put(MODEL_JOBS, ControllerUtils.getInstance().getJobs(organisationId));
				modelAux.put(PERSON_JOBS,ControllerUtils.getInstance().getPersonJobs(person));
								
				// Get all available departments for user's organization, without those that belongs to the user
				List<Department> depts = BLDepartment.getInstance().getDepartmentByOrganizationAndPerson(organisationId, personId);
				
				// Adding to model all Organisation's Departments
				modelAux.put(MODEL_ALL_DEPARTMENTS, depts);				
				
				// Get all available roles, grouped by Modules
				List<Module> modules = BLModule.getInstance().listModulesWithRolesByOrganisation(organisationId, person);							
	
				// Adding all available roles
				modelAux.put(MODEL_ALL_ROLES, modules);
				
				//Set flag to know if a person is editing his profile
				if (ControllerUtils.getInstance().fromThisUrl(URL_MY_PROFILE, request)) {
					modelAux.put("MY_PROFILE", new Object());
				}								
			} else {
				//add the admin at the organisation's fake department
				Department dept = BLDepartment.getInstance().getFakeForOrganization(organisationId);
				modelAux.put(FAKE_DEPARTMENT_ID, dept.getDepartmentId());
				
				//Set<Role> roles = new HashSet<Role>();
				Role role = BLRole.getInstance().getRoleByNameAndOrg(organisationId, IConstant.OM_ADMIN);
				modelAux.put(ADMIN_ROLE_ID, role.getRoleId());
			}			
			List<List> result = ControllerUtils.getInstance().getMonthsAndDaysAndYears(request, messageSource);

			modelAux.put(MODEL_DAYS, result.get(0));
			modelAux.put(MODEL_MONTHS, result.get(1));	
			modelAux.put(MODEL_YEARS, result.get(2));			
			
		} catch(BusinessException bexc) {
			logger.error("referenceData", bexc);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception ex) {
			logger.error("referenceData", ex);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
								
		setErrors(request, errorMessages);
		
		logger.debug("referenceData END");
		return modelAux;
	}
	
	/*
	 * Make a Map with <Department, Job>
	 */
	private Map<Department, Job> getMap(Set<Department> depts,HttpServletRequest request) throws BusinessException{
		logger.debug("getMap for person - START personId="+Integer.parseInt((String) request.getParameter("personId")));
		final String prefix = "jobInDept"; 
		int jobId;
		Job job;
		int size = depts.size();
		logger.debug("size = " + size);
		
		Iterator<Department> iterator = depts.iterator();
		Map<Department, Job> deptWithJob = new HashMap<Department, Job>();
		Department dept;
		
		for(int i=0; i<size; i++){
			dept = iterator.next();
			logger.debug("dept = " + dept);
			if(dept.getStatus()!= (int) IConstant.NOM_DEPARTMENT_FAKE){
				logger.debug("departmentId = " + Integer.toString(dept.getDepartmentId()));
				logger.debug("parameter = " + (""+prefix+Integer.toString(dept.getDepartmentId())));
				jobId = Integer.parseInt((String) request.getParameter((prefix+Integer.toString(dept.getDepartmentId()))));				
				logger.debug("jobId = " + jobId);
				if(jobId > 0){
					job = BLJob.getInstance().get(jobId);
					deptWithJob.put(dept,job);
				}
			} else {
				job = new Job();
				job.setJobId(-1);
				deptWithJob.put(dept,job);
			}
		}
		logger.debug("getMap for person - END personId="+Integer.parseInt((String) request.getParameter("personId")));
		return deptWithJob;
	}
	
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException, java.util.Map)
	 */
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {
		logger.debug("showForm  - START - ");
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		if ("CHANGE_PASSWORD".equals(action)) {			
			logger.debug("showForm  - END - ");
			return super.showForm(request, errors, VIEW_CHANGE_PASSWORD, controlModel);
		} 
		logger.debug("showForm  - END - ");
		return super.showForm(request, errors, getFormView(), controlModel);
	}
	
	/**
	 * Runs in formBackingObject Handles Add New Admin.
	 * @author Adelina
	 * 
	 */
	private Person handleAddNewAdmin(HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws ServletRequestBindingException {
		logger.debug("handleAddNewAdmin - START");
		Person person = new Person();
		try{	
			person = composeAdmin(request);			
			logger.debug("person admin = " + person);
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex){
			logger.error("", ex);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
		
		logger.debug("handleAddNewAdmin - END");
		// never returns null
		return person;
	}
	
	/**
	 * Creates an admin.
	 * @author Adelina 
	 * @param request
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws BusinessException
	 */	
	private Person composeAdmin(HttpServletRequest request) throws ServletRequestBindingException, BusinessException {
		logger.debug("composeAdmin - START");
		
		// Add an empty command object to model
		Person person = new Person();
		person.setPersonId(-1);
		person.setSex('M');
		person.setBirthDate(null);
		Set<UserGroup> userGroups = new HashSet<UserGroup>();
		person.setUserGroups(userGroups);
		
		Integer organisationId;
		if(request.getParameter(ORGANISATION) != null) {			
			organisationId = ServletRequestUtils.getIntParameter(request, ORGANISATION);				
		} else {
			organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);			
		}		
							
		Department dept = BLDepartment.getInstance().getFakeForOrganization(organisationId);
		logger.debug("dept =  " + dept);
		Set<Department> depts = new HashSet<Department>();
		depts.add(dept);
		person.setDepts(depts);
		request.setAttribute(FAKE_DEPARTMENT_ID, dept.getDepartmentId());		
		
		Role role = BLRole.getInstance().getRoleByNameAndOrg(organisationId, IConstant.OM_ADMIN);
		request.setAttribute(ADMIN_ROLE_ID, role.getRoleId());	
		
		logger.debug("composeAdmin - END");
		return person;
	}
	
	/**
	 * Adds an admin to the organisation
	 * @author Adelina
	 * @param request
	 * @param response
	 * @param command
	 * @param arg3
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ModelAndView handleAddAdmin(HttpServletRequest request, Object command, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleAddAdmin - START");
		ModelAndView mav = new ModelAndView(getFormView());		
		int organizationId = ServletRequestUtils.getRequiredIntParameter(request, ORGANISATION);
		logger.debug("organisationId = " + organizationId);
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgCreateAdmin())){
				Person person = (Person) command;		
				logger.debug("person depts = " + person.getDepts());
				person.setDeptWithJob(getMap(person.getDepts(), request));		
				String password = RandomDataProvider.getInstance().generatePassword();
				
				//Applying the password to the person
		        person.setPassword(password.toString());
		        person.setPasswordConfirm(password.toString());		
		        
		        // add to admin the OM_USER role and OM_ADMIN role
				ArrayList<Role> roles = (ArrayList<Role>) BLRole.getInstance().getRolesByNamesAndOrg(organizationId, 2, IConstant.OM_ADMIN, IConstant.OM_USER);
				Set<Role> personRoles = new HashSet<Role>(roles);				
				person.setRoles(personRoles);
				logger.debug("roles = " + roles);
		        
				//setting the default user group (all persons are assigned to the default user group)		       
				UserGroup defaultUserGroup = BLUserGroup.getInstance().getDefaultUserGrup(organizationId);
				boolean isDefaultUserGroup =  false;

				for (UserGroup userGroup : person.getUserGroups()){
					if (userGroup.getStatus() == IConstant.NOM_USER_GROUP_DEFAULT){
						isDefaultUserGroup = true;
					}
				}
				if ( defaultUserGroup != null && !isDefaultUserGroup){
					if (person.getUserGroups() != null){
						person.getUserGroups().add(defaultUserGroup);
					} else {
						Set<UserGroup> userGroups = new HashSet<UserGroup>();
						userGroups.add(defaultUserGroup);
						person.setUserGroups(userGroups);
					}
				}	 						
			
				//adding the person
				BLPerson.getInstance().add(person);
				
				BLPerson.getInstance().excludeDefaultUserGroup(person);						
				
				// Add command object to model
				mav.addObject(getCommandName(), person); 
												
				String personFirstNameMessages = ControllerUtils.getInstance().tokenizeField(person.getFirstName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				String personLastNameMessages = ControllerUtils.getInstance().tokenizeField(person.getLastName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {personFirstNameMessages, personLastNameMessages}, locale));													
				
				BLPerson.getInstance().sendPasswordByEmailToAdmin(person, password, locale);
				
				String personEmailMessages = ControllerUtils.getInstance().tokenizeField(person.getEmail(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(MAIL_SEND_SUCCESS, new Object[] {personEmailMessages}, locale));
				
				//add the new audit event
				try {
					//if the admin is added by the adminIT, the audit event organisationId will be the added organisation id and
					//no first and last name will be sent
					String lastName;
					if (userAuth.isAdminIT()){
						lastName = new String("");
					} else {
						lastName = userAuth.getLastName();
					}
					Organisation org = BLOrganisation.getInstance().get(organizationId);
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ADMIN_ADD_TYPE, userAuth.getFirstName(), lastName
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ADMIN_ADD_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName()), org.getName()}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_ADMIN_ADD_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName()), org.getName()}, new Locale("ro"))
						, organizationId, userAuth.getPersonId());
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error("", bexc);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex){
			logger.error("", ex);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
		
		logger.debug("handleAddAdmin - END");
		return mav;
	}
	
	/**
	 * Updates  an admin to the organisation, after it is saved
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param arg3
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ModelAndView handleUpdateAdmin(HttpServletRequest request, Object command, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleUpdateAdmin - START");
		
		ModelAndView mav = new ModelAndView(getFormView());				
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OrgCreateAdmin())){
				Person person = (Person) command;		
				
				person.setDeptWithJob(getMap(person.getDepts(), request));		
												
				Set<Role> roles = person.getRoles();				
							
				// when a person is added, adds with the OM_BASIC role
				Role role = BLRole.getInstance().getRoleByNameAndOrg(ControllerUtils.getInstance().getOrganisationIdFromSession(request), IConstant.OM_USER);				
				roles.add(role);
				
				person.setRoles(roles);
						        		       		        					       
				//updating the person
				BLPerson.getInstance().update( ControllerUtils.getInstance().getOrganisationIdFromSession(request) ,person);
				
				//Exclude the default user group from the list of user groups
				BLPerson.getInstance().excludeDefaultUserGroup(person);
				
				// Add command object to model
				mav.addObject(getCommandName(), person);			
												
				String personFirstNameMessages = ControllerUtils.getInstance().tokenizeField(person.getFirstName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				String personLastNameMessages = ControllerUtils.getInstance().tokenizeField(person.getLastName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {personFirstNameMessages, personLastNameMessages}, locale));																			
								
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error("", bexc);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception ex){
			logger.error("", ex);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {0, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
		
		logger.debug("handleUpdateAdmin - END");
		return mav;
	}
	
	/**
	 * 
	 * Add the picture for a person when we add for the first time the person
	 * @author mitziuro
	 *
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @throws Exception
	 */
	private void addPicture(HttpServletRequest request, Object command, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("managePicture - START");
		
		Person person = (Person) command;
		Picture picture = new Picture();
		
		try{
			
			if (person.getFile() != null) {
				logger.debug("Size: " + person.getFile().getSize());
				
				//if is the corect picture
				String fileExtension = FileUtils.getInstance().getFileExtension(person.getFile().getOriginalFilename());
				if ("JPG".equals(fileExtension.toUpperCase()) || "PNG".equals(fileExtension.toUpperCase()) || "GIF".equals(fileExtension.toUpperCase()) || "BMP".equals(fileExtension.toUpperCase())) {
					
					picture.setExtension(fileExtension);
					picture.setPicture(ImageUtils.getInstance().createResizedCopy(person.getFile().getBytes(),
							IConstant.PERSON_PICTURE_WIDTH, IConstant.PERSON_PICTURE_HEIGHT));
					picture.setDateCreated(new Timestamp(System.currentTimeMillis()));
					picture.setDateModified(null);
					picture.setName(person.getFile().getOriginalFilename());
					picture.setWidth(IConstant.PERSON_PICTURE_WIDTH);
					picture.setHeight(IConstant.PERSON_PICTURE_HEIGHT);
				
					picture.setPersonId(person.getPersonId());
					
					//adding the picture
					BLPicture.getInstance().add(picture);
					
					//adding the picture to the person
					person.setPicture(picture);
				}
			}
		} catch(BusinessException bexc) {
			logger.error("pictureAdd", bexc);
			errorMessages.add(messageSource.getMessage(PICTURE_ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}

		logger.debug("managePicture - END");
	}
	
	/**
	 * See if the current organisation has branches.
	 * If it has the we display the branch select
	 *
	 * @author mitziuro
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	private boolean manageBranches(HttpServletRequest request) throws BusinessException{
		logger.debug("manageBranches START");
		boolean display = false;
		
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		
		Organisation organisation = BLOrganisation.getInstance().get(organisationId);
		//If the organisation is company there are no branches
		if(organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_COMPANY) {
			display = false;
		} else {
			List <Organisation> branchList = BLOrganisation.getInstance().getAllOrganisationsForParentId(organisationId);
			if(branchList.size() > 0){
				display = true;
			}
		}
		
		logger.debug("manageBranches END display:".concat(String.valueOf(display)));
		return display;
		
	}
       
}
