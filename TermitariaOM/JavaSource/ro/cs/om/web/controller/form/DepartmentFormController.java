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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * SimpleFormController used to manage a Department entity.
 * 
 * @author matti_joona
 */
public class DepartmentFormController extends RootSimpleFormController {

	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "department.";
	private final String ADD_SUCCESS							= ROOT_KEY.concat("add.success");
	private final String UPDATE_SUCCESS							= ROOT_KEY.concat("update.success");		
		
	private final String ADD_ERROR								= ROOT_KEY.concat("add.error");
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	private final String UPDATE_ERROR							= ROOT_KEY.concat("update.error");
	private final String GENERAL_ERROR							= ROOT_KEY.concat("general.error");	
	
	//------------------------ATTRIBUTES--------------------------------------------------------------
	private final String DEPARTMENT_ID							= "departmentId";	
	
	//------------------------MODEL------------------------------------------------------------------	
	private final String MODEL_POTENTIAL_PARENT_DEPARTMENTS		= "POTENTIAL_PARENT_DEPARTMENTS";
	private final String MODEL_ALL_SUBDEPARTMENTS				= "ALL_SUBDEPARTMENTS";
	private final String MODEL_DEFAULT_DEPARTMENT_ID			= "DEFAULT_DEPARTMENT_ID";
	private final String MODEL_MANAGER_NAME						= "MANAGER_NAME";
	private final String MODEL_JSON_PERSONS						= "JSON_PERSONS";	
	
	private final String ALL_PERSONS 							= "ALL_PERSONS";
	private static final String GET_ACTION 						= "GET_ACTION";	
	private static final String PARENT_DEPARTMENT				= "PARENT_DEPARTMENT";	
	private static final String MANAGER_ID						= "MANAGER_ID";
	
	/**
	 * Contstructor - Has the basic settings for this controller.
	 */
	public DepartmentFormController(){
		setCommandName("departmentBean");
		setCommandClass(Department.class);
		setFormView("DepartmentForm");
		setSuccessView("DepartmentForm");
	}
	
	
	/**
	 * Bindings for request parameters
	 * 
	 * @author Adelina
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("start - initBinder");
		
		binder.registerCustomEditor(Set.class, "persons", new CustomCollectionEditor(Set.class){
			protected Object convertElement(Object element){
				if(element != null){
					Integer personId = new Integer((String)element);
					logger.debug("person : convertElement for Id " + personId);
					Person person = null;
					try{
						person =  BLPerson.getInstance().get(personId);				
					} catch(BusinessException e){						
						logger.debug("There was an error");
					}
					logger.debug("start - initBinder");
					return person;
				}
				logger.debug("start - initBinder");
				return null;
			}			
		});		
	}
	
	/**
	 * It runs at every cycle
	 * 
	 * @author matti_joona
	 * @author dan.damian
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject  BEGIN");
		Locale locale = RequestContextUtils.getLocale(request);
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		//Backing Object
		Department department = new Department();
		try {
			//Backing Object Id
			Integer departmentId = ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID);
									
			// check if i have to edit a department
			if((request.getParameter(IConstant.REQ_ACTION) != null) && (IConstant.CMD_GET.equals(request.getParameter(IConstant.REQ_ACTION)))){
				logger.debug("formBackingObject: Get");		
				logger.debug(ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID));				
				department = handleGet(departmentId, errorMessages, infoMessages, locale, request);
			}	 else {
				logger.debug("formBackingObject: New");
				department = handleAddNew();	
			}
		} catch (Exception e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command ".concat(department.toString()));
		
		return department;
	}
	

	/**
	 * It runs on every submit (request Method = POST)
	 * It detects the case by action parameter and dispatch to and appropriate handler.
	 * 
	 * @author matti_joona
	 * @author dan.damian
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit  BEGIN");
		Locale locale = RequestContextUtils.getLocale(request);
		ModelAndView mav = null;
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		Department department = (Department) command;
		//redirect regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		
		if (IConstant.CMD_SAVE.equals(action)) {
			if(department.getDepartmentId() > 0 ){
				mav = handleUpdate(request, response, command, errors, errorMessages, infoMessages, locale);
			} else {
				mav = handleAdd(request, response, command, errors, errorMessages, infoMessages, locale);
			}
		}		

		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		//Add referenceData objects to model
		mav.addAllObjects(referenceData(request, command, errors));
		
		logger.debug("onSubmit END");
		
		return mav;
	}
		
	/**
	 * Handles the GetDepartment case
	 * 
	 * @author matti_joona 
	 */
	private Department handleGet(int departmentId, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale, HttpServletRequest request) {
		logger.debug("== GET OPERATION ==");
		Department department = null;
		try{
			//call get method from the business layer
			department = BLDepartment.getInstance().getWithPersons(departmentId);	
			
			if(department != null) {
				Integer parentDeptId = department.getParentDepartmentId();
				Department parentDept = BLDepartment.getInstance().get(parentDeptId);
				request.setAttribute(PARENT_DEPARTMENT, parentDept);
			}
											
			//Setting a blank Manager if it hasn't any
			if (department.getManager() == null) {
				department.setManager(new Person());
			}
			//Setting a blank Parent Department if it hasn't any
			//This situation theoretically must not occur
			if (department.getParentDepartment() == null) {
				department.setParentDepartment(new Department());
			} 
								
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("== END GET OPERATION ==");
		request.setAttribute(GET_ACTION, true);
		// never returns null
		return department;
	}
	
	
	/**
	 * Handles the AddDepartment case
	 * 
	 * @author matti_joona 
	 */
	private ModelAndView handleAdd(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("== ADD OPERATION ==");
		ModelAndView mav = new ModelAndView(getSuccessView());
		//call new method from the business layer
		try{
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
			Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			Department department = (Department) command;			

			//****************** Security *******************************
			// if the user has permission OM_DeptAdd, can add a department for an organisation
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_DeptAdd())){
				Organisation organisation = new Organisation();
				organisation.setOrganisationId(organisationId);
				department.setOrganisation(organisation);
				
				mav.addObject(getCommandName(), department);
				
				//setting parent department 
				department.setParentDepartment(BLDepartment.getInstance().get(department.getParentDepartmentId()));
				
				//setting department status (active)
				department.setStatus(IConstant.NOM_DEPARTMENT_ACTIVE);
				
				if(department.getManagerId() != null && department.getManagerId() > 0) {
					//setting the department manager
					Person manager = BLPerson.getInstance().get(department.getManagerId());
					
					//if the department manager hasn't been added in the department persons list, I have to add it by default
					if(manager != null) {
						department.setManager(manager);
						department.getPersons().add(manager);
					}
				}				
															
				//adding department to the database
				BLDepartment.getInstance().addAll(department);
				
				String departmentNameMessage = ControllerUtils.getInstance().tokenizeField(department.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
									
				infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {departmentNameMessage}, locale));
				//Update all the organizations structures that include this department as a node
				BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(department.getOrganisationId());
				
				//add the new audit event
				try {
					Organisation org = BLOrganisation.getInstance().get(organisationId);
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_DEPARTMENT_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_DEPARTMENT_ADD_MESSAGE, new Object[] {department.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_DEPARTMENT_ADD_MESSAGE, new Object[] {department.getName(), org.getName()}, new Locale("ro"))
							, org.getOrganisationId(), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				// else, it has not the right to add a department
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}	
		logger.debug("== END ADD OPERATION ==");
		return mav;
	}
	
	/**
	 * Handles the UpdateDepartment case
	 * 
	 * @author matti_joona 
	 */
	private ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException arg3, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("== UPDATE OPERATION ==");
		ModelAndView mav = new ModelAndView(getSuccessView());
		try{			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			// if the user has permission OM_DeptUpdate, can update a department for an organisation
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_DeptUpdate())){
				Department department = (Department) command;			
				mav.addObject(getCommandName(), department);
				
				//I have to retrieve the old persons list and update it if the manager has been changed
//				Set<Person> persons = BLDepartment.getInstance().listPersons(department.getDepartmentId(), ControllerUtils.getInstance().getOrganisationIdFromSession(request));
				
				Set<Person> persons = department.getPersons();									
				
				//get the new manager person								
				//if there is a manager put it in department
				if(department.getManagerId() != null &&  department.getManagerId() > 0){
					Person manager = BLPerson.getInstance().get(department.getManagerId());
					//add the manager to the department persons list
					
					persons.add(manager);
				}
				
				department.setPersons(persons);
				
				//update in database the selected info					
				BLDepartment.getInstance().update(department);
				
				String departmentNameMessage = ControllerUtils.getInstance().tokenizeField(department.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {departmentNameMessage}, locale));
				//Update all the organizations structures that include this department as a node
				BLOrganisationStructure.getInstance().updateAllOrganisationsStructure(department.getOrganisationId());
				
				//add the new audit event
				try {
					Organisation org = BLOrganisation.getInstance().get(department.getOrganisationId());
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_DEPARTMENT_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_DEPARTMENT_UPDATE_MESSAGE, new Object[] {department.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_DEPARTMENT_UPDATE_MESSAGE, new Object[] {department.getName(), org.getName()}, new Locale("ro"))
							, org.getOrganisationId(), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				// else, it has not the right to update a department for an organisation
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("== END UPDATE OPERATION ==");
		return mav;
	}
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map modelAux = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
				
		Department department = (Department) command;
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		modelAux.put(IConstant.REQ_ACTION, action);
		
		List<Person> persons = BLPerson.getInstance().getPersonsByOrganizationAndDepartment(organisationId, department.getDepartmentId());				
		if(persons != null){
			modelAux.put(ALL_PERSONS, persons);
		} else {
			modelAux.put(ALL_PERSONS, new HashSet<Person>());
		}
		
		logger.debug("managerId = ".concat(String.valueOf(department.getManagerId())));
		if(department.getManagerId() != null && department.getManagerId() != -1) {
			modelAux.put(MANAGER_ID, department.getManagerId());
		} else {
			modelAux.put(MANAGER_ID, new Integer(0));
		}
						
		try {	
			List<Department> potentialParentDepartments = BLDepartment.getInstance().listPotentialParenDepartments(department.getDepartmentId(), organisationId);
			if ( potentialParentDepartments!= null && potentialParentDepartments.size() != 0){
				modelAux.put(MODEL_POTENTIAL_PARENT_DEPARTMENTS, potentialParentDepartments);
			}
			
			modelAux.put(MODEL_ALL_SUBDEPARTMENTS, BLDepartment.getInstance().listAllSubDepartments(department.getDepartmentId()));
			
			modelAux.put(MODEL_DEFAULT_DEPARTMENT_ID, BLDepartment.getInstance().getFakeForOrganization(organisationId).getDepartmentId());
			
			if (department.getManagerId() != null) {
				Person departmentManager = BLPerson.getInstance().get(department.getManagerId());
				if (departmentManager != null) {
					modelAux.put(MODEL_MANAGER_NAME, departmentManager.getFirstName().concat(" ").
							concat(departmentManager.getLastName()));
				}
			}		
			modelAux.put(MODEL_JSON_PERSONS, ControllerUtils.getInstance().
					getPersonsActivated(organisationId, RequestContextUtils.getLocale(request), 
							errorMessages, messageSource));
							
		} catch(BusinessException bexc) {
			logger.error("referenceData", bexc);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception ex) {
			logger.error("referenceData", ex);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}	
					
		setErrors(request, errorMessages);
		
		return modelAux;
	}
	
	private Department handleAddNew() throws BusinessException {
		Department department = new Department();
		department.setDepartmentId(-1);
		department.setManagerId(-1);
	
		return department;
	}	
	
}
