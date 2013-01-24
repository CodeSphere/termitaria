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
package ro.cs.om.signupWizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLJob;
import ro.cs.om.business.BLModule;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLRole;
import ro.cs.om.business.BLUserGroup;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.RandomDataProvider;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;

public class SignupWizardActions implements Serializable {

	public transient static Logger logger = Logger.getLogger(SignupWizardActions.class);
	private static final long serialVersionUID = 1L;
	private static ArrayList<String> mandatoryModules;

	/**
	 * @author tekin
	 */
	
	//  ----------------------ORGANISATION METHODS--------------------------------
	static {
		logger.debug("Initializing the list of mandatory modules.");
		mandatoryModules = new ArrayList<String>();
		mandatoryModules.add("Organization Management");
		mandatoryModules.add("OM_Basic");
		mandatoryModules.add("DM_Basic");
	}

	public List<Module> getAllModules() {

		logger.debug("getAllModules()");
		List<Module> moduleList = new ArrayList<Module>();
		try {
			for(Module m : BLModule.getInstance().getAllModules())
				if (!mandatoryModules.contains(m.getName())) {
					moduleList.add(m) ;
				}
			
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return moduleList;
	}

	public List getOrganisationTypes() {
		
		logger.debug("getOrganisationTypes()");
		List organisationTypes = (List) OMContext
				.getFromContext(IConstant.NOM_ORGANISATION_TYPE);
		System.out.println("organisationTypes=" + organisationTypes);

		return organisationTypes;

	}

	public List getCompanieTypes() {
		
		logger.debug("getCompanieTypes()");
		List companieTypes = (List) OMContext
				.getFromContext(IConstant.NOM_GROUP_COMPANIES_TYPE);
		System.out.println("companieTypes=" + companieTypes);

		return companieTypes;
	}

	public Set getExistingModules() {

		logger.debug("getExistingModules()");
		Set<Module> existingModules = new HashSet();
		try {
			for (Module m : BLModule.getInstance().getAllModules()) {
				if (mandatoryModules.contains(m.getName())) {
					existingModules.add(m);
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return existingModules;
	}

	public void saveOrganisation(Organisation org, ArrayList<String> messages) {
	 
		logger.debug("saveOrganisation() with id=" + org.getOrganisationId());
		
		org.setParentId(-1);
		
		try {   	
			Set<Module> organisationModules = new HashSet<Module>();

			for(int moduleId : org.getModuleIDs()) {
				organisationModules.add(BLModule.getInstance().get(moduleId));
			}
			org.setModules(organisationModules);
			
			logger.debug("--Selected modules: " + org.getModules());
			
			if(org.getModules() == null){
				Module module = BLModule.getInstance().get(IConstant.OM_MODULE);		
				organisationModules.add(module);
				org.setModules(organisationModules);
			}
			BLOrganisation.getInstance().add(org);
			logger.debug("organisation id = " + org.getOrganisationId());
			//Update the context map that specifyies if this organisation has the audit module
			BLOrganisation.getInstance().updateHasAuditContextMap(org.getOrganisationId(), org.getModules());
			
			//add the audit event
			BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ORGANISATION_ADD_TYPE, "", "", "", ""
					, BLOrganisation.getInstance().getOrgByName(org.getName()).getOrganisationId(), -1);
			
			messages.add("Organisation " + org.getName() + " has been saved!");
			logger.debug("***Organisation has been saved!");
		} catch (BusinessException e) {
			e.printStackTrace();
		}		
		logger.debug(" -- messages are: " + messages.toString());
	}
	
	/**
	 * @author tekin
	 */
	//  --------------------------ADMIN METHODS----------------------------
	
	public void saveAdmin(Person person, Organisation org, ArrayList<String> messages){	

		int organizationId = org.getOrganisationId();
	
		// ---------------- COMPOSE ADMIN -----------
		person.setPersonId(-1);
		person.setSex('M');
		person.setBirthDate(null);
		Set<UserGroup> userGroups = new HashSet<UserGroup>();
		person.setUserGroups(userGroups);
		
		try {
			Department dept = BLDepartment.getInstance().getFakeForOrganization(organizationId);
			logger.debug("dept =  " + dept);
			Set<Department> depts = new HashSet<Department>();
			depts.add(dept);
			person.setDepts(depts);
			
			person.setDeptWithJob(getMap(person.getDepts(), dept.getDepartmentId()));	
			
			String password = RandomDataProvider.getInstance().generatePassword();
			
			//Applying the password to the person
	        person.setPassword(password.toString());
	        person.setPasswordConfirm(password.toString());		
	        
	        ArrayList<Role> roles = (ArrayList<Role>) BLRole.getInstance().getRolesByNamesAndOrg(organizationId, 2, IConstant.OM_ADMIN, IConstant.OM_USER);
			Set<Role> personRoles = new HashSet<Role>(roles);				
			person.setRoles(personRoles);
			logger.debug("roles = " + roles);
			
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
					userGroups = new HashSet<UserGroup>();
					userGroups.add(defaultUserGroup);
					person.setUserGroups(userGroups);
				}
			}							
		
			//adding the person
			BLPerson.getInstance().add(person);
			
			BLPerson.getInstance().excludeDefaultUserGroup(person);		
			
			BLPerson.getInstance().sendPasswordByEmailToAdmin(person, password, null);
			
			String lastName = new String("");
			
			BLAudit.getInstance().add(IConstant.AUDIT_EVENT_ADMIN_ADD_TYPE, "", lastName, "", "", organizationId, person.getPersonId());
			
			messages.clear();
			messages.add("Admin " + person.getFirstName() + " " + person.getLastName() + " has been added to organisation " + org.getName()+ ".");
			messages.add("The password has been sent to your e-mail address: " + person.getEmail()+".");
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	private Map<Department, Job> getMap(Set<Department> depts, int deptId) throws BusinessException{
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
				jobId = deptId;				
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
		return deptWithJob;
	}
}
