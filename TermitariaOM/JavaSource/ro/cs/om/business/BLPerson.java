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
package ro.cs.om.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;

import org.quartz.Scheduler;
import org.springframework.context.MessageSource;

import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchPersonBean;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoAuthorization;
import ro.cs.om.model.dao.IDaoModule;
import ro.cs.om.model.dao.IDaoOOO;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.om.model.dao.IDaoSetting;
import ro.cs.om.utils.mail.MailUtils;
import ro.cs.om.web.security.UserAuth;
import ro.cs.tools.Tools;


/**
 * Person - Business Layer
 *
 * @author dd
 */
public class BLPerson extends BusinessLogic {
	
	//singleton implementation
    private static BLPerson theInstance = null;
    private IDaoPerson personDao 				= DaoBeanFactory.getInstance().getDaoPerson();
    private IDaoAuthorization authorizationDao	= DaoBeanFactory.getInstance().getDaoAuthorization();
    private IDaoModule moduleDao 				= DaoBeanFactory.getInstance().getDaoModule();
    private IDaoOrganisation organisationDao 	= DaoBeanFactory.getInstance().getDaoOrganisation();
    private IDaoSetting settingDao 				= DaoBeanFactory.getInstance().getDaoSetting();
    private static IDaoOOO outOfOfficeDao		= DaoBeanFactory.getInstance().getDaoOOO();
    
    private BLPerson(){};
    static {
        theInstance = new BLPerson();
    }
    public static BLPerson getInstance() {
    	return theInstance;
    }

    /**
     * Add person.
     * 
     * @author dd
     * @param person
     * @throws BusinessException
     */
    public void add(Person person) throws BusinessException{
    	try{    		
    		personDao.add(person);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_ADD, bexc);
    	}
    }
        
    /**
     * Update person.
     * 
     * @author dd
     * @param person
     * @throws BusinessException
     */
    public void update(Integer organisationId ,Person person) throws BusinessException{
    	try{
    		personDao.update(person);
    		// Get all available departments for user's organization, without those that belongs to the user
    		List<Department> depts = BLDepartment.getInstance().getDepartmentByOrganizationAndPerson(organisationId, person.getPersonId());
    		for (Department dept : depts){
    			if ((dept.getManagerId() != null && dept.getManagerId() == person.getPersonId()) || 
    				(dept.getManager() != null && dept.getManager().getPersonId() == person.getPersonId())){
    				dept.setManagerId(-1);
    				dept.setManager(null);
    				
    				BLDepartment.getInstance().updateManager(dept);
    				
    			}
    		}
    		
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_UPDATE, bexc);
    	}
    }
    
    /**
     * Update person.
     * 
     * @author dd
     * @param person
     * @throws BusinessException
     */
    public void updateSimple(Person person) throws BusinessException{
    	try{
    		personDao.updateSimple(person);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_UPDATE, bexc);
    	}
    }
    
    
    /**
     * Returns the UserAuth from Person table identified by personId
     * @param personId
     * @return
     * @throws BusinessException
     */
    public UserAuth getUserAuthByPersonId(int personId) throws BusinessException{
    	UserAuth userAuth = null;
    	try{
    		userAuth = personDao.getUserAuthByPersonId(personId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_USER_AUTH, bexc);
    	}
    	return userAuth;
    }
    
    
    
    /**
     * Returns the UserAuth from Person table identified by personId
     * @param personId
     * @return
     * @throws BusinessException
     */
    public UserAuth getUserAuthByUsername(String username) throws BusinessException{
    	UserAuth userAuth = null;
    	
    	try{
    		userAuth = personDao.getUserAuthByUsername(username);
    		
    		Integer organisationId = ((Department)(new ArrayList(userAuth.getDepts()).get(0))).getOrganisationId();
    		userAuth.setThemeCode(settingDao.getSettingValue(organisationId, IConstant.SETTING_THEME));
    	} catch(Exception bexc){
    		
    		//exception at getting the theme
    		if(userAuth != null){
    			userAuth.setThemeCode(IConstant.STANDARD_THEME);
    		} else {
    			throw new BusinessException(ICodeException.PERSON_GET_USER_AUTH_BY_USERNAME, bexc);
    		}
    	}
    	return userAuth;
    }
    
    
    /**
     * Update the UserAuth set of attributes from Person table
     * 
     * @author dd
     * @param person
     * @throws BusinessException
     */
    public void updateUserAuth(UserAuth userAuth) throws BusinessException{
    	try{
    		personDao.updateUserAuth(userAuth);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_UPDATE, bexc);
    	}
    }
    
    /**
     * Update person's credentials.
     * 
     * @author dd
     * @param person
     * @throws BusinessException
     */
    public void updateCredentials(Person person) throws BusinessException{
    	try{
    		personDao.updateCredentials(person);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_UPDATE, bexc);
    	}
    }
    
    /**
	 * Update person's credentials by reseting the password
	 * @author Adelina
	 * @param Person
     * @throws BusinessException 
	 *
	 */
    public String resetPassword(Person person) throws BusinessException{
    	String password = null;
    	try{
    		password = personDao.resetPassword(person);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERSON_UPDATE, e);
    	}
    	return password;
    }
    
    /**
     * Update person. Person with roles.
     * 
     * @author dd
     * @param person
     * @throws BusinessException
     */
    public void updateWithRoles(Person person) throws BusinessException{
    	try{
    		personDao.updateWithRoles(person);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_UPDATE_WITH_ROLES, bexc);
    	}
    }
    
    /**
     * Update person. Person with departments.
     * 
     * @author dd
     * @param person
     * @throws BusinessException
     */
    public void updateWithDepartments(Person person) throws BusinessException{
    	try{
    		personDao.updateWithDepartments(person);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_UPDATE_WITH_DEPARTMENTS, bexc);
    	}
    }
    
    
    /**
     * Delete person.
     * 
     * @author dd
     * @param personId
     * @throws BusinessException
     */
    public Person delete(Integer personId, Integer organizationId) throws BusinessException {
    	logger.debug("delete - START , personId = ".concat(String.valueOf(personId)));
    	Person person = null;
    	Scheduler sched = (Scheduler)OMContext.getFromContext(IConstant.SCHEDULER);
    	try{
    		person = personDao.delete(personId, organizationId);
    		String username = person.getUsername();
    		logger.debug("out of office = " + person.getOutOfOffice());
    		Iterator<OutOfOffice> iterator = person.getOutOfOffice().iterator();
    		String jobName = null;
    		//delete all the jobs related with the person in this scheduler
    		while(iterator.hasNext()){
    			OutOfOffice ooo = (OutOfOffice) iterator.next();    		
    			jobName = username.concat("_").concat(String.valueOf(ooo.getOutOfOfficeId())); 
    			sched.deleteJob(jobName, username.concat("_Group"));
    		}
    		
    		List<OutOfOffice> oooList = outOfOfficeDao.getByPersonID(personId);
    		for(OutOfOffice ooo : oooList) {
    			outOfOfficeDao.delete(ooo.getOutOfOfficeId());
    		}
    		
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_DEL, bexc);
    	}    
    	logger.debug("delete - END , person = " + person);
    	return person;
    }

    /**
     * Return person identified by id. Person contains general info attributes.
     * 
     * @author dd
     */
    public Person get(Integer idPerson) throws BusinessException{
      	Person person = null;
    	try{
        	person = personDao.get(idPerson);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET, bexc);
    	}
    	return person;
    }
    
    /**
     * Returns person's username identified by idPerson
     *
     * @author alu
     * 
     * @param idPerson
     * @return
     * @throws BusinessException
     */
    public String getUsernameById(Integer idPerson) throws BusinessException{
      	String username = null;
    	try{
    		Person person = personDao.get(idPerson);
    		if (person != null)
    			username = person.getUsername();
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERSON_GET_USERNAME, e);
    	}
    	return username;
    }
    
    /**
     * Return person identified by id. Person contains general info attributes.
     * 
     * @author dd
     */
    public Person getByUsername(String username) throws BusinessException{
      	Person person = null;
    	try{
        	person = personDao.getByUsername(username);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_BY_USERNAME, bexc);
    	}
    	return person;
    }
    
    /**
     * Return person identified by id. Person contains general info attributes.
     * 
     * @author dd
     */
    public List<Person> getByUsername(String[] usernames, boolean isNotDeleted) throws BusinessException{
      	List<Person> persons = null;
    	try{
        	persons = personDao.getByUsername(usernames, isNotDeleted);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_BY_USERNAME, bexc);
    	}
    	return persons;
    }
    
    /**
     * Return person identified by id. Person contains general info attributes and it's roles.
     * 
     * @author dd
     */
    public Person getWithRoles(Integer personId) throws BusinessException{
      	Person person = null;
    	try{
        	person = personDao.getWithRoles(personId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_WITH_ROLES, bexc);
    	}
    	return person;
    }
    
    /**
     * Return person identified by id. Person contains general info attributes and it's departments.
     * 
     * @author dd
     */
    public Person getWithDepartments(Integer personId) throws BusinessException{
      	Person person = null;
    	try{
        	person = personDao.getWithDepartments(personId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_WITH_DEPARTMENTS, bexc);
    	}
    	return person;
    }
 
    
    /**
     * Return person identified by id. Person contains general info attributes and related 
     * entities.
     * 
     * @author dd
     */
    public Person getAll(Integer idPerson) throws BusinessException{
      	Person person = null;
    	try{
        	person = personDao.getAll(idPerson);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET, bexc);
    	}
    	return person;
    }
    
    /**
     * Returns all persons in the the database. 
     * entities.
     * 
     * @author dd
     */
    public List<Person> list() throws BusinessException{
      	List<Person> list = null;
    	try{
    		list = personDao.list();
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_LIST, bexc);
    	}
    	return list;
    }
    
    /**
     * Returns a list of modules to which the user has access to. 
     * 
     * @author dd
     */
    public List<Module> getAllowedModules(Integer personId) throws BusinessException{
      	List<Module> list = null;
    	try{
    		list = authorizationDao.getAllowedModules(personId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_ALLOWED_MODULES, bexc);
    	}
    	return list;
    }
    
    /**
     * Groups a person's roles by module 
     * 
     * @author dd
     * 
     * @param Set<Role>roles  A Person's Set of roles
     * @return List<Module>  A list with <i>all</i> the Suite's Modules, each Module containing 
     * associated roles, extracted from the Person's Set of roles
     */
    public List<Module> groupRolesByModules(Set<Role> roles) throws BusinessException{
    	// List containing all Suite's Modules (each Module doesn't contain any role)
    	List<Module> modules = null; 
    	try{
    		// Get modules
    		modules = moduleDao.getAllModules();
    		// Map containing pairs <ModuleId, Module's Set of roles>
    		HashMap<Integer, HashSet<Role>> modulesRoles = new HashMap<Integer, HashSet<Role>>();
    		// Initializing Map
    		for(int i = 0; i < modules.size(); i++) {
    			modulesRoles.put(new Integer(modules.get(i).getModuleId()), new HashSet<Role>());
    		}
    		// Going through the Set of roles
    		Iterator<Role> it =  roles.iterator();
    		Role role = null;
    		while (it.hasNext()) {
    			role = it.next();    			
    			// Add role to associated Module's Set of roles
    			logger.debug("modulesRoles " + modulesRoles);
    			logger.debug("role " + role);
    			logger.debug("role.getModule " + role.getModule());
    			if(role.getModule() != null){
    				modulesRoles.get(new Integer(role.getModule().getModuleId())).add(role);
    			}
    		}
    		// Grouping Roles on Modules
    		for(int i = 0; i < modules.size(); i++) {
    			modules.get(i).setRoles(modulesRoles.get(modules.get(i).getModuleId()));
    		}
    		
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_ALLOWED_MODULES_WITH_ROLES, bexc);
    	}
    	return modules;
    }
    
    /**
     * Groups a person's roles by an organisation modules
     * 
     * @author coni
     * 
     * @param Set<Role>roles  A Person's Set of roles
     * @return List<Module>  A list with <i>all</i> the Suite's Modules, each Module containing 
     * associated roles, extracted from the Person's Set of roles
     */
    public List<Module> groupRolesByModulesAndOrganization(Set<Role> roles, int organisationId) throws BusinessException{
    	
    	logger.debug("BL groupRolesByModulesAndOrganization - START - ");
    	
    	// List containing all Suite's Modules (each Module doesn't contain any role)
    	List<Module> modules = new ArrayList<Module>(); 
    	try{
    		// Get modules
    		for( Module tempModule : BLOrganisation.getInstance().getModules(organisationId)){
    			modules.add(tempModule);
    		}
    		// Map containing pairs <ModuleId, Module's Set of roles>
    		HashMap<Integer, HashSet<Role>> modulesRoles = new HashMap<Integer, HashSet<Role>>();
    		// Initializing Map
    		for(int i = 0; i < modules.size(); i++) {
    			modulesRoles.put(new Integer(modules.get(i).getModuleId()), new HashSet<Role>());
    		}
    		// Going through the Set of roles
    		for(Role role: roles){  			
    			// Add role to associated Module's Set of roles
    			if(role == null) continue;
    			logger.debug("modulesRoles " + modulesRoles);
    			logger.debug("role " + role);
    			logger.debug("role.getModule " + role.getModule());
    			//if there isn't a module we go back
    			if(role.getModule() != null){
    				if ( modulesRoles.get(new Integer(role.getModule().getModuleId())) != null ){    				
            			modulesRoles.get(new Integer(role.getModule().getModuleId())).add(role);
        			}
    			}
    		}    		
    		// Grouping Roles on Modules
    		for(int i = 0; i < modules.size(); i++) {
    			modules.get(i).setRoles(modulesRoles.get(modules.get(i).getModuleId()));
    		}
    		
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_ALLOWED_MODULES_WITH_ROLES, bexc);
    	}
    	
    	logger.debug("BL groupRolesByModulesAndOrganization - END - ");
    	
    	return modules;
    }
    
    /**
     * Excludes form a person's set of departments the fake department.
     * @author dd
     * @param depts
     * @return
     * @throws BusinessException 
     */
    public void excludeFakeDepartment(Person person) throws BusinessException {
    	logger.debug("excludeFakeDepartment for person with id: ".concat(Integer.toString(person.getPersonId())));
    	if (person != null) {
	    	Set<Department> depts = person.getDepts();
	    	if (depts != null) {
		    	Set<Department> newDepts = new HashSet<Department>();
		    	Department d = null;
		    	for(Iterator<Department> i = depts.iterator(); i.hasNext();) {
		    		d = i.next();
		    		logger.debug("-> " + d);
		    		if (d.getStatus() != IConstant.NOM_DEPARTMENT_FAKE) {
		    			newDepts.add(d);
		    		}
		    	}
		    	person.setDepts(newDepts);
	    	}
    	} else {
    		logger.debug("Person null!");
    	}
    	logger.debug("fake department excluded");
    }
    
    /*
     * Excludes the default user group from the person's set of user groups
     * @param person
     * @author coni
     */
    public void excludeDefaultUserGroup(Person person){
    	logger.debug("START - excludeDefaultUserGroup for person with id: ".concat(Integer.toString(person.getPersonId())));
    	if (person != null){
    		Set<UserGroup> userGroups = person.getUserGroups();
    		if (userGroups != null){
        		Set<UserGroup> newUserGroups = new HashSet<UserGroup>();
        		for (UserGroup userGroup : userGroups) {
        			if (userGroup.getStatus() == IConstant.NOM_USER_GROUP_NORMAL) {
        				newUserGroups.add(userGroup);
        			}
        		}
        		person.setUserGroups(newUserGroups);
    		}
    	} else {
    		logger.debug("Person null");
    	}
    	logger.debug("END - excludeDefaultUserGroup for person with id: ".concat(Integer.toString(person.getPersonId())));
    }
    
    /**
     * Searches for persons after criterion from searchPersonBean
     * @author alu
     * @author dd
     * 
     * @param searchPersonBean - Bean that contains the search criterion
     * @return A list of person beans
     * @throws BusinessException
     */
    public List<Person> getFromSearch(SearchPersonBean searchPersonBean, boolean isDeleteAction, Integer sessionOrgId) throws BusinessException{
    	logger.debug("getFromSearch - START");
    	List<Person> res = null;
    	try {
    		//if we search in all organisations
    		if(searchPersonBean.getOrganisationId() != IConstant.SEARCH_ALL_BRANCH_PERSONS)
    		{
    			res = personDao.getFromSearch(searchPersonBean, isDeleteAction, null);
    		//we search after session orgId and in all branches
    		} else {
    			searchPersonBean.setOrganisationId(sessionOrgId);
    			List<Integer> branchIds = organisationDao.getAllBranchIdsForOrganisation(searchPersonBean.getOrganisationId());
    			res = personDao.getFromSearch(searchPersonBean, isDeleteAction, branchIds);
    			searchPersonBean.setOrganisationId(IConstant.SEARCH_ALL_BRANCH_PERSONS);
    		}
    		
    		Iterator<Department> iterator = null;
    		
    		for(Person person : res){
    			
    			iterator = person.getDepts().iterator();
    			
    			//for every person we set the actions flag if we have actions or no
    			if(iterator.next().getOrganisationId() == sessionOrgId.intValue()){
    				person.setModify(IConstant.PERSON_ALL_ACTIONS);
    			} else {
    				person.setModify(IConstant.PERSON_NO_ACTION);
    			}
    			  
    			//for every person we set the jobsFromDepts atribute
				StringBuilder  sb = new StringBuilder();
				
				Set<Department> departments = person.getDeptWithJob().keySet();
				
				Tools.getInstance().printSet(logger, departments);
				
				if(departments != null && departments.size() != 0) {
					for(Department dept : departments) {
						if(person.getPersonId() > 0){
							if ( person.getDeptWithJob().get(dept) != null ){
								sb.append(person.getDeptWithJob().get(dept).getName().concat(" - ").concat(dept.getName()));										
							}
						}	
						sb.append("<br/>");					
					}
					person.setJobsFromDepts(sb.toString());
				} else {
					sb.append("");
					person.setJobsFromDepts(sb.toString());
				}
				sb = null;
			}
 
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERSON_SEARCH, e);
    	}
    	logger.debug("getFromSearch - END");
    	return res;
    }
    
    /**
	 * Return a list with all the persons from the given organization
	 * 
	 * @author alu
	 */
    public List<Person> getPersonsByOrganizationId(int organizationId) throws BusinessException{
    	logger.debug("getPersonsByOrganizationId - START - organizationId:".concat(String.valueOf(organizationId)));
    	List<Person> persons = null;
    	try {
    		persons = personDao.getPersonsByOrganizationId(organizationId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANIZATION, e);
    	}
    	logger.debug("getPersonsByOrganizationId - END");
    	return persons;
    }
    
    /**
	 * Returns a list of Persons within those Departments.
	 * @author dd
     * @throws BusinessException 
	 */
	public List<Person> getFromDepartments(Integer[] departmentIds) throws BusinessException {
		logger.debug("getFromDepartments - START");
    	List<Person> persons = null;
    	try {
    		persons = personDao.getFromDepartments(departmentIds);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERSON_GET_FROM_DEPARTMENT, e);
    	}
    	logger.debug("getFromDepartments - END");
    	return persons;
	}
	
	/**
	 * Return the number of persons from the given organisation
	 * 
	 * @author Adelina
	 * @param organisationId
	 * @return number of persons from an organisation
	 * @throws BusinessException 
	 */
	public long getPersonsCountByOrganization(Integer organizationId) throws BusinessException{
		logger.debug("getPersonsCountByOrganization - START");
    	long countEmpl = 0;
    	try {
    		countEmpl = personDao.getPersonsCountByOrganization(organizationId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERSON_COUNT_FOR_ORGANISATION, e);
    	}
    	logger.debug("getPersonsCountByOrganization - END");
    	
    	return countEmpl;		
	}
	
	/**
	 * Checks if the organisation has an admin
	 * 
	 * @author Adelina
	 * @param organisationId
	 * @return true if the organisation has an admin
	 * @throws BusinessException 
	 */
	public boolean hasAdminByOrganisation(Integer organizationId) throws BusinessException{
		logger.debug("hasAdminByOrganisation - START");
    	boolean isAdmin = false;
    	try {
    		isAdmin = personDao.hasAdminByOrganisation(organizationId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERSON_IS_ADMIN, e);
    	}
    	logger.debug("hasAdminByOrganisation - END");
    	
    	return isAdmin;
	}
	
	
	/**
	 * After creating a new Organization's Administrator, it send the username and password
	 * by email.
	 * 
	 * @author dan.damian
	 * 
	 * @param person
	 * @param password
	 * @param locale
	 * @throws BusinessException
	 */
	public void sendPasswordByEmailToAdmin(final Person person, final String password, final Locale locale) throws BusinessException {
		final Exception[] exception = new Exception[1];
		
		class SendPasswordAdminRunnable implements Runnable {				
		    public void run() {			
		    	logger.debug("sendPasswordByEmailToAdmin BEGIN");
				logger.debug(person);				
	    		try{
					
	    			String personName = person.getFirstName().concat(" ").concat(person.getLastName());					
					Iterator<Department> deptIterator = person.getDepts().iterator();
					Integer organisationId = deptIterator.next().getOrganisationId();
					
					String organisationName = BLOrganisation.getInstance().get(organisationId).getName();
					
					MessageSource messageSource = (MessageSource) OMContext.getApplicationContext().
										getBean(IConstant.APP_CONTEXT_MESSAGE_SOURCE_BEAN);
					
					String subject = messageSource.getMessage(IConstant.MAIL_SEND_PASSWORD_TO_ADMIN_SUBJECT, null, locale);
					
					String message = messageSource.getMessage(IConstant.MAIL_SEND_PASSWORD_TO_ADMIN_MESSAGE, new Object[] {personName, organisationName, 
						person.getUsername(), password}, locale);
					
					//we send the mail to the designated address
					MailUtils.getInstance().sendMail(person.getEmail(), subject, message);		    			    	
	    		
	    		} catch (Exception e) {					
	    			exception[0] = e;
				}
				logger.debug("sendPasswordByEmailToAdmin END");		
		    } 		
		}
		
		// starts a new thread for sending the mail to admin
		Thread thread = new Thread(new SendPasswordAdminRunnable());		
		thread.start();
		
		if(exception[0] != null) {
			throw new BusinessException(ICodeException.PERSON_SEND_PASSWORD_BY_EMAIL_TO_ADMIN, exception[0]);
		}
		
	}
	
	
	/**
	 * After Administrator resets one User's password and email will be sent automatically to the user
	 * containing the new password.
	 * 
	 * @author Adelina
	 * 
	 * @param person
	 * @param password
	 * @param locale
	 * @throws BusinessException 
	 */
	public void sendPasswordByEmailToUser(final Person person, final String password, final Locale locale) throws BusinessException  {
		
		final Exception[] exception = new Exception[1];
		
		class SendPasswordUserRunnable implements Runnable {				
		    public void run() {			
		    	logger.debug("sendPasswordByEmailToUser BEGIN");
				logger.debug(person);
				try {
					String personName = person.getFirstName().concat(" ").concat(person.getLastName());

					MessageSource messageSource = (MessageSource) OMContext.getApplicationContext().
										getBean(IConstant.APP_CONTEXT_MESSAGE_SOURCE_BEAN);
					
					String subject = messageSource.getMessage(IConstant.MAIL_SEND_PASSWORD_TO_USER_SUBJECT, null, locale);
					
					String message = messageSource.getMessage(IConstant.MAIL_SEND_PASSWORD_TO_USER_MESSAGE, new Object[] {personName, 
							person.getUsername(), password}, locale);
					
					//we send the mail to the designated address
					MailUtils.getInstance().sendMail(person.getEmail(), subject, message);			    	
	    		
	    		} catch (Exception e) {					
	    			exception[0] = e;
				}
	    		logger.debug("sendPasswordByEmailToUser END");
		    } 		
		}
		
		// start a new thread for sending the mail to user
		Thread thread = new Thread(new SendPasswordUserRunnable());		
		thread.start();	
		
		if(exception[0] != null) {
			throw new BusinessException(ICodeException.PERSON_SEND_PASSWORD_BY_EMAIL_TO_USER, exception[0]);
		}
											
	}		
	
	/**
	 * Return a list with the persons from the given organisation, without 
	 * those that are in the given usergroup
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param userGroupId
	 * @return
	 */
	public List<Person> getPersonsByOrganizationAndUserGroup(int organizationId, int userGroupId)throws BusinessException{
		logger.debug("getPersonsByOrganizationAndUserGroup - START - organizationId:".concat(String.valueOf(organizationId)));
    	List<Person> persons = null;
    	try {
    		persons = personDao.getPersonsByOrganizationAndUserGroup(organizationId, userGroupId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANIZATION_NOT_USERGROUP, e);
    	}
    	logger.debug("getPersonsByOrganizationAndUserGroup - END");
    	return persons;
	}
	
	/**
	 * Return a list with the persons from the given organization, without 
	 * those that are in the given department
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param departmentId
	 * @return
	 */
	public List<Person> getPersonsByOrganizationAndDepartment(Integer organizationId, Integer departmentId) throws BusinessException{
		logger.debug("getPersonsByOrganizationAndDepartment - START - organizationId:".concat(String.valueOf(organizationId)));
		
    	List<Person> persons = null;
    	try {
    		persons = personDao.getPersonsByOrganizationAndDepartment(organizationId, departmentId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANIZATION_NOT_DEPT, e);
    	}
    	
    	logger.debug("getPersonsByOrganizationAndDepartment - END");
    	return persons;
	}
	
	/**
	 * Return a list with all the Persons from the given Organization
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<Person> getPersonsSimpleByOrganizationId(int organizationId, boolean notDeleted) throws BusinessException {
		logger.debug("getPersonsFromOrganization START: organizationId = ".concat(String.valueOf(organizationId)));
		
		List<Person> persons = null;
		try {
			persons = personDao.getPersonsSimpleByOrganizationId(organizationId, notDeleted);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANISATION, 	e);
		}
		
		logger.debug("getPersonsFromOrganization - END");
		return persons;		
	}
	
	/**
	 * Return a list with all the Persons from the given Organization and Module and all the perosons from organisation
	 * 
	 * @author mitziuro
	 * 
	 * @param organizationId
	 * @param moduleId
	 * @return
	 */
	public Entry<List<Integer>, List<Person>> getPersonsSimpleByOrganizationIdAndModule(int organizationId, int moduleId) throws BusinessException {
		logger.debug("getPersonsSimpleByOrganizationIdAndModule START: organizationId = ".concat(String.valueOf(organizationId)).concat(" moduleId = ").concat(String.valueOf(moduleId)));
		
		Entry<List<Integer>, List<Person>> persons = null;
		try {
			persons = personDao.getPersonsIdsByOrganizationIdAndModuleId(organizationId, moduleId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANISATION_AND_MODULE, e);
		}
		
		logger.debug("getPersonsSimpleByOrganizationIdAndModule - END");
		return persons;		
	}
	
	/**
	 * Return a list with all the deleted Persons from the given Organization and Module and all the perosons from organisation
	 * 
	 * @author mitziuro
	 * 
	 * @param organizationId
	 * @param moduleId
	 * @return
	 */
	public Entry<List<Integer>, List<Person>> getDeletedPersonsSimpleByOrganizationIdAndModule(int organizationId, int moduleId) throws BusinessException {
		logger.debug("getDeletedPersonsSimpleByOrganizationIdAndModule START: organizationId = ".concat(String.valueOf(organizationId)).concat(" moduleId = ").concat(String.valueOf(moduleId)));
		
		Entry<List<Integer>, List<Person>> persons = null;
		try {
			persons = personDao.getDeletedPersonsIdsByOrganizationIdAndModuleId(organizationId, moduleId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANISATION_AND_MODULE, e);
		}
		
		logger.debug("getDeletedPersonsSimpleByOrganizationIdAndModule - END");
		return persons;		
	}
	
	/**
	 * Return a list of persons that belongs to a given department, 
	 * and not other departments
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param departmentId
	 * @return List<Person>
	 * @throws BusinessException 
	 */
	public List<Person> getPersonByOnlyOneDepartment(Integer organisationId, Integer departmentId) throws BusinessException {
		logger.debug("getPersonByOnlyOneDepartment START: departmentId = ".concat(String.valueOf(departmentId)));
		List<Person> persons = null;
		
		try{
			persons = personDao.getPersonByOnlyOneDepartment(organisationId, departmentId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_ONLY_ONE_DEPT, e);
		}
		
		logger.debug("getPersonByOnlyOneDepartment - END");
		return persons;
	}
	
	/**
	 * Returns a person itentified by it's id. Person contains general info attributes and picture
	 * 
	 * @author Adelina
	 *
	 * @param personId
	 * @return
	 * @throws BusinessException 
	 */
	public Person getWithPicture(Integer personId) throws BusinessException {
		logger.debug("getWithPicture - START: pictureId = ".concat(Integer.toString(personId)));
		Person person = null;
		try{
			person = personDao.getWithPicture(personId);
		} catch (Exception bexc) {
			throw new BusinessException(ICodeException.PERSON_GET_WITH_PICTURE, bexc);
		}		
		logger.debug("getWithPicture - END");
		return person;
	}
	
	/**
	 * Return a list of person from a given organization,
	 * that has a certain role
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param roleName
	 * @return
	 * @throws BusinessException 
	 */
	public List<Person> getPersonsWithRoleByOrganisation(Integer organisationId, String roleName) throws BusinessException {
		logger.debug("getPersonsWithRoleByOrganisation - START: ".concat(String.valueOf(organisationId)));
		
		List<Person> persons = null;
		try{
			persons = personDao.getPersonsWithRoleByOrganisation(organisationId, roleName);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_ROLE_NAME_AND_ORGANISATION, e);
		}
		
		logger.debug("getPersonsWithRoleByOrganisation - END");
		return persons;
	}
	
	/**
	 * Returns a list of the persons from the user group with the specified id
	 * 
	 * @author Coni
	 * 
	 * @param userGroupId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Person> getByUserGroupId(Integer userGroupId) throws BusinessException {
		logger.debug("getByUserGroupId - START");
		List<Person> persons = null;
		try {
			persons = personDao.getByUserGroupId(userGroupId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_USER_GROUP_ID, e);
		}
		logger.debug("getByUserGroupId - END");
		return persons;
	}
	
    /**
     * Returns persons identified by personId. Person contains general info attributes.
     * 
     * @author Coni
     * @param personIds
     */
    public List<Person> getByPersonId(Integer[] personIds) throws BusinessException{
    	logger.debug("getByPersonId - START");
      	List<Person> persons = null;
    	try{
        	persons = personDao.getByPersonId(personIds);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PERSON_GET_BY_PERSON_ID, bexc);
    	}
    	logger.debug("getByPersonId - END");
    	return persons;
    }
    
    /**
     * Retrieves from search a list of person with basic info 
     * 
     * @author Coni
     * @param searchPersonBean
     * @return
     */
    public List<Person> getFromSearchSimple(SearchPersonBean searchPersonBean, boolean withDeleted) throws BusinessException{
    	logger.debug("getFromSearchSimple - START");
    	List<Person> persons = null;
    	try {
    		persons = personDao.getFromSearchSimple(searchPersonBean, withDeleted);
    	} catch(Exception bexc) {
    		throw new BusinessException(ICodeException.PERSON_GET_FROM_SEARCH_SIMPLE, bexc);
    	}
    	logger.debug("getFromSearchSimple - END");
    	return persons;
    }
    
    /**
     * Retrieves from search a list of person with basic info and pagination
     * 
     * @author Coni
     * @param searchPersonBean
     * @return
     */
    public List<Person> getFromSearchSimpleWithPagination(SearchPersonBean searchPersonBean, boolean withDeleted) throws BusinessException{
    	logger.debug("getFromSearchSimpleWithPagination - START");
    	List<Person> persons = null;
    	try {
    		persons = personDao.getFromSearchSimpleWithPagination(searchPersonBean, withDeleted);
    	} catch(Exception bexc) {
    		throw new BusinessException(ICodeException.PERSON_GET_FROM_SEARCH_SIMPLE_WITH_PAGINATION, bexc);
    	}
    	logger.debug("getFromSearchSimpleWithPagination - END");
    	return persons;
    }
    
    /**
     * Disables all persons from all organisations
     *
     * @author alu
     * @throws BusinessException 
     */
    public void disableAll() throws BusinessException{
    	logger.debug("disableAll - START");
    	
    	try {
    		personDao.disableAll();
    	} catch(Exception bexc) {
    		throw new BusinessException(ICodeException.PERSON_DISABLE_ALL, bexc);
    	}
    	
    	logger.debug("disableAll - END");
    	
    }
    
    /**
	 * Return a list with all the deactivated Persons from the given Organization
	 * 
	 * @author Liviu
	 * 
	 * @param organizationId
	 * @return
	 
	public List<Person> getDeactivatedPersonsSimpleByOrganizationId(int organizationId) throws BusinessException {
		logger.debug("getDeactivatedPersonsFromOrganization START: organizationId = ".concat(String.valueOf(organizationId)));
		
		List<Person> persons = null;
		try {
			persons = personDao.getDeactivatedPersonsSimpleByOrganizationId(organizationId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANISATION, 	e);
		}
		
		logger.debug("getDeactivatedPersonsFromOrganization - END");
		return persons;		
	}
    */
    
    /**
	 * Returns a list with all the persons from the given organisation that are active( without those that are deleted, and those that are disabled)
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param departmentId
	 * @return
     * @throws BusinessException 
	 */
	public List<Person> getPersonsActivated(Integer organizationId) throws BusinessException {
		logger.debug("getPersonsActivated - START");
    	List<Person> persons = null;
    	try {
    		persons = personDao.getPersonsActivated(organizationId);
    	} catch(Exception bexc) {
    		throw new BusinessException(ICodeException.PERSON_GET_ACTIVATED, bexc);
    	}
    	logger.debug("getPersonsActivated - END");
    	return persons;
	}
}
