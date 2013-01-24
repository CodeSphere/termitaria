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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLJob;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;

/**
 * Used for displaying a view for changing manager
 * 
 * @author matti_joona
 */
public class ChangeDepartmentPropertiesController extends RootAbstractController{

	//-----------------MESSAGES KEY---------------------------------------------
	private final String ROOT_KEY									= "department.";
	private final String UPDATEPERSONS_SUCCESS						= ROOT_KEY.concat("updatepersons.success");
	private final String UPDATEPERSONS_ERROR						= ROOT_KEY.concat("updatepersons.error");
	private final String UPDATEPERSONSJOBS_ERROR					= ROOT_KEY.concat("updatepersonsjobs.error");
	private final String PERSONSLISTFROMDEPARTMENT_ERROR			= ROOT_KEY.concat("listpersons.error");
	private final String SUBDEPARTMETSLISTFROMDEPARTMENT_ERROR		= ROOT_KEY.concat("listsubdepartments.error");
	
	//-----------------COMMANDS-------------------------------------------------
	private final String CMD_CHANGE_DEPARTMENT_PERSONS				= "CHANGE_DEPARTMENT_PERSONS";
	private final String CMD_CHANGE_DEPARTMENT_PERSONS_UPDATE		= "UPDATEPERSONS";
	private final String CMD_PERSONS_FROM_DEPARTMENT_LIST	     	= "LIST_DEPARTMENT_PERSONS";
	private final String CMD_SUBDEPARTMENTS_FROM_DEPARTMENT_LIST 	= "LIST_DEPARTMENT_SUBDEPARTMENTS";

	//-----------------USED VIEWS-----------------------------------------------
	private final String VIEW_CHANGE_PERSONS 						= "Department_ChangePersons";
	private final String VIEW_PERSONS_LIST							= "Department_ListPersons";
	private final String VIEW_SUBDEPARTMENTS_LIST					= "Department_ListSubdepartments";
	private final String VIEW_DEPARTMENT	 						= "DepartmentForm";
	
	//------------------------ATTRIBUTES----------------------------------------
	private final String DEPARTMENT_ID								= "departmentId";
	private final String MODEL_ALL_PERSONS							= "ALL_PERSONS";
	private final String MODEL_DEPARTMENT_PERSONS  					= "personList";
	private final String MODEL_DEPARTMENT_SUBDEPARTMENTS			= "subdepartmentList";
	private final String ACTION										= "ACTION";
	private final String DEPARTMENT_COMMAND_BEAN					= "departmentBean";	
	private final String DEPARTMENT_PERSONS							= "DEPARTMENT_PERSONS";
	private static final String NEW_PERSONS 						= "changePersonsForm_select1";
	private final String PERSON_JOB									= "personJob";
	
	//------------------------MODEL----------------------------------------------
	private final String MODEL_POTENTIAL_PARENT_DEPARTMENTS			= "POTENTIAL_PARENT_DEPARTMENTS";
	private final String MODEL_MANAGER_NAME							= "MANAGER_NAME";
	private final String MODEL_JOBS									= "JOBS";
	private final String MODEL_PERSONS_WITH_JOBS_ARRAY				= "PERSONS_WITH_JOBS_ARRAY";	
	
	public ChangeDepartmentPropertiesController() {
		setView(VIEW_DEPARTMENT);
	}
	
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();		
		
		Locale locale = RequestContextUtils.getLocale(request);
		
		ModelAndView mav = null;		
		String action = ServletRequestUtils.getStringParameter(request, ACTION);
		
		if(CMD_CHANGE_DEPARTMENT_PERSONS.equals(action)){
	    	mav = handleChangeDepartmentPersons(request, infoMessages, errorMessages, locale);
	    } else if(CMD_CHANGE_DEPARTMENT_PERSONS_UPDATE.equals(action)){
	    	mav = handleChangeDepartmentPersonsUpdate(request, infoMessages, errorMessages, locale);
		} else if(CMD_PERSONS_FROM_DEPARTMENT_LIST.equals(action)){
	    	mav = handleGetPersons(request, infoMessages, errorMessages, locale);
		} else if(CMD_SUBDEPARTMENTS_FROM_DEPARTMENT_LIST.equals(action)){
    		mav = handleGetSubdepartments(request, infoMessages, errorMessages, locale);
		} 
		
	    //Publish messages/errors
	    setErrors(request, errorMessages);
	    setMessages(request, infoMessages);
	    
	    return mav;
    }
    
    
	/**
	 * Handles the update person list for a department
	 * 
	 * @author matti_joona
	 * @author Adelina
	 */
	private ModelAndView handleChangeDepartmentPersonsUpdate(HttpServletRequest request, ArrayList<String> infoMessages, ArrayList<String> errorMessages, Locale locale) throws BusinessException, ServletRequestBindingException {
		logger.debug("== SAVE PERSON LIST IN DEPARTMENT OPERATION ==");
		
		ModelAndView mav = new ModelAndView(getView());
		try {
			// get the departmentId from the request
			Integer departmentId = ServletRequestUtils.getRequiredIntParameter(request, DEPARTMENT_ID);
			logger.debug("handleChangeDepartmentPersonsUpdate " + departmentId);
			
			Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			//get all info about department
			Department department = BLDepartment.getInstance().getAll(departmentId);	
			
			//get ids of persons
			int[] personIds = ServletRequestUtils.getIntParameters(request, NEW_PERSONS);
			logger.debug("personIds: " + personIds.length);
			Set<Person> persons = new HashSet<Person>();
			//build the list of persons
			for(int i = 0; i < personIds.length; i++) {
				Person person = BLPerson.getInstance().getAll(personIds[i]);
				logger.debug("persn = " + person);
				persons.add(person);
			}
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ChangePersons())){
				//update the persons jobs
				persons = updatePersonsJobInDept(persons, request, department, infoMessages, errorMessages, locale);				
				//set to see in interface
				department.setPersons(persons);
													
				//update in database the selected info					
				BLDepartment.getInstance().updatePersons(department, persons);
				
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
			mav.addObject(DEPARTMENT_COMMAND_BEAN, department);
			
			mav.addObject(MODEL_POTENTIAL_PARENT_DEPARTMENTS, 
					BLDepartment.getInstance().listPotentialParenDepartments(department.getDepartmentId(), organisationId));
			
			//we put the manager name
			if(department.getManager() != null) {
				mav.addObject(MODEL_MANAGER_NAME,	department.getManager().getFirstName().concat(" ").
						concat(department.getManager().getLastName()));
				//setting the manager id
				department.setManagerId(department.getManager().getPersonId());
			}
			
			//setting ids to work in interface
			department.setParentDepartmentId(department.getParentDepartment().getDepartmentId());
			
			infoMessages.add(messageSource.getMessage(UPDATEPERSONS_SUCCESS, new Object[] {null}, locale));
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(UPDATEPERSONS_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		logger.debug("== END SAVE PERSON LIST IN DEPARTMENT OPERATION ==");

		return mav;
	}
	
	
	/**
	 * Gets all persons from an organization, all its jobs, all persons from a department and their job in that department
	 * 
	 * @author matti_joona
	 * @author Adelina
	 * @throws ServletRequestBindingException 
	 */
	private ModelAndView handleChangeDepartmentPersons(HttpServletRequest request, ArrayList<String> infoMessages, ArrayList<String> errorMessages, Locale locale) throws BusinessException, ServletRequestBindingException{
		logger.debug("== LISTPERSONS FOR PERSONS LIST OF DEPARTMENT OPERATION ==");
		ModelAndView mav = new ModelAndView(VIEW_CHANGE_PERSONS);		
		try {
			// All the persons from the organization
			Integer organizationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);	
			Set<Person> persons = BLOrganisation.getInstance().getPersons(organizationId);			
			mav.addObject(MODEL_ALL_PERSONS, persons);
			
			// All the persons from the department
			Integer departmentId = ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID);								
			Department department = BLDepartment.getInstance().getAll(departmentId);	
			Set<Person> departmentPersons = department.getPersons();
			if(departmentPersons != null){
				mav.addObject(DEPARTMENT_PERSONS, departmentPersons);
			}
			else{ 
				mav.addObject(DEPARTMENT_PERSONS, new HashSet<Person>());
			}
			mav.addObject(DEPARTMENT_ID, departmentId);
			// Add a JSONArray containing all the department persons jobs and their jobs in the department
			mav.addObject(MODEL_PERSONS_WITH_JOBS_ARRAY, ControllerUtils.getInstance().getDepartmentPersonsJobs(departmentPersons, department));
			// Add all the existing jobs for the department organization
			mav.addObject(MODEL_JOBS, ControllerUtils.getInstance().getJobs(organizationId));
			infoMessages.add(messageSource.getMessage(UPDATEPERSONS_SUCCESS, new Object[] {null}, locale));
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(UPDATEPERSONS_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("== END LISTPERSONS FOR PERSONS LIST OF DEPARTMENT OPERATION ==");
		
		return mav;
	}

	
	/**
	 * Get all persons from department
	 * 
	 * @author matti_joona
	 * @throws ServletRequestBindingException 
	 */
	private ModelAndView handleGetPersons(HttpServletRequest request, ArrayList<String> infoMessages, ArrayList<String> errorMessages, Locale locale) throws BusinessException, ServletRequestBindingException{
		logger.debug("== LIST PERSONS FROM DEPARTMENT OPERATION ==");
		
		ModelAndView mav = new ModelAndView(VIEW_PERSONS_LIST);
		
		Integer departmentId = ServletRequestUtils.getRequiredIntParameter(request, DEPARTMENT_ID);
		Department dept = BLDepartment.getInstance().get(departmentId);		
		try {			
			Integer[] departmentIds = new Integer[1];
			departmentIds[0] = departmentId;
			List<Person> listPersons = BLPerson.getInstance().getFromDepartments(departmentIds);		
			getPersonJobsFromDept(listPersons, dept);	
			request.setAttribute(MODEL_DEPARTMENT_PERSONS, listPersons);
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(PERSONSLISTFROMDEPARTMENT_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
			
		logger.debug("== END LIST PERSONS FROM DEPARTMENT OPERATION ==");
		mav.addObject(DEPARTMENT_ID, departmentId);
		return mav;
	}
	
	/**
	 * Get all subdepartments from department
	 * 
	 * @author matti_joona
	 * @throws ServletRequestBindingException 
	 */
	private ModelAndView handleGetSubdepartments(HttpServletRequest request, ArrayList<String> infoMessages, ArrayList<String> errorMessages, Locale locale) throws BusinessException, ServletRequestBindingException{
		logger.debug("== LIST SUBDEPARTMENTS FROM DEPARTMENT OPERATION ==");
		
		ModelAndView mav = new ModelAndView(VIEW_SUBDEPARTMENTS_LIST);		
		try {
			Integer departmentId = ServletRequestUtils.getRequiredIntParameter(request, DEPARTMENT_ID);
			List<Department> listDepts = BLDepartment.getInstance().listSubDepartments(departmentId);
			
			request.setAttribute(MODEL_DEPARTMENT_SUBDEPARTMENTS, listDepts);
		} catch(BusinessException bexc){
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(SUBDEPARTMETSLISTFROMDEPARTMENT_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}

		logger.debug("== END LIST SUBDEPARTMENTS FROM DEPARTMENT OPERATION ==");
		return mav;
	}
		
	/**
	 * updates the persons jobs in departments
	 * 
	 * @param persons
	 * @author coni
	 *
	 */
	private Set<Person> updatePersonsJobInDept( Set<Person> persons, HttpServletRequest request, Department department,  ArrayList<String> infoMessages, ArrayList<String> errorMessages, Locale locale) {
		Map<Department, Job> deptWithJob = new HashMap<Department, Job>();
		Integer jobId;
		for ( Person pers : persons){
			deptWithJob = pers.getDeptWithJob();
			try{
				jobId = ServletRequestUtils.getIntParameter(request, PERSON_JOB.concat(Integer.toString(pers.getPersonId())));
				if (jobId > 0){
					deptWithJob.put(department, BLJob.getInstance().get(jobId));
					pers.setDeptWithJob(deptWithJob);
				} else {
					Job j = new Job();
					j.setJobId(-1);
					deptWithJob.put(department, j);
					pers.setDeptWithJob(deptWithJob);
				}
			} catch ( BusinessException bexc){
				logger.error(bexc.getMessage(), bexc);
				errorMessages.add(messageSource.getMessage(UPDATEPERSONSJOBS_ERROR, new Object[] { bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
			} catch ( ServletRequestBindingException exc){
				logger.error(exc.getMessage(), exc);
				errorMessages.add(messageSource.getMessage(UPDATEPERSONSJOBS_ERROR, new Object[] { exc.getMessage(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
			}
		}
		return persons;
	}
	
	/**
	 * Get the jobs for persons from a given department
	 * 
	 * @author Adelina
	 * 
	 * @param person
	 * @param dept
	 * 
	 */	
	private void getPersonJobsFromDept(List<Person> persons, Department dept)	{		
		
		logger.debug("getPersonWithJobs - START -");
							
		for(Person person : persons) {				
			if(person.getPersonId() > 0){
				if ( person.getDeptWithJob().get(dept) != null ){
					person.setJobsFromDepts(person.getDeptWithJob().get(dept).getName());								
				}
			}			
		}
		
		logger.debug("getPersonWithJobs - END -");			
	}
}
