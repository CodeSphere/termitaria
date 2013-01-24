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
package ro.cs.om.model.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchPersonBean;
import ro.cs.om.web.security.UserAuth;

/**
 * Dao Interface, implemented by PersonDaoImpl
 * 
 * @author dd
 *
 */
public interface IDaoPerson {

	/**
	 * Adds a person.
	 * 
	 * @author dd
	 * @param person
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(Person person)  throws NoSuchAlgorithmException, UnsupportedEncodingException;
	
	
	/**
	 * Add a Person that has just been imported.
	 * References:
	 * Has not to: Departments
	 * 
	 * @author dan.damian
	 */
	@Transactional (rollbackFor=Exception.class)
	public void addFromImport(Person person);

	
	/**
	 * Updates a person.
	 * 
	 * @author dd
	 * @param person
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(Person person);

	
	/**
	 * Updates a person.
	 * 
	 * @author dd
	 * @param person
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateSimple(Person person);
	
	
	/**
	 * Updates the UserAuth attributes form Person
	 * 
	 * @author dan.damian
	 * 
	 * @param username
	 * @return
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateUserAuth(UserAuth userAuth);
	
	
	/**
	 * Updates a person's credentials.
	 * 
	 * @author dd
	 * @param person
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateCredentials(Person person) throws NoSuchAlgorithmException, UnsupportedEncodingException;
	
	/**
	 * Update person's credentials by reseting the password
	 * @author Adelina
	 * @param Person
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	@Transactional (rollbackFor=Exception.class)
	public String resetPassword(Person person) throws NoSuchAlgorithmException, UnsupportedEncodingException;
	
	
	/**
	 * Updates a person. Person with roles.
	 * 
	 * @author dd
	 * @param person
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateWithRoles(Person person);

	
	/**
	 * Updates a person. Person with departments.
	 * 
	 * @author dd
	 * @param person
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateWithDepartments(Person person);
	
	/**
	 * Deletes a person identified by it's id
	 * 
	 * @author dd
	 * @param personId
	 */
	@Transactional (rollbackFor=Exception.class)
	public Person delete(Integer personId, Integer organizationId);
	

	/**
	 * Returns a person identified by it's id. Person contains general info attributes
	 * 
	 * @author dd
	 * @param personId
	 */
	public Person get(Integer personId);
	
	
	/**
	 * Returns a UserAuth by it's personId
	 * 
	 * @author dan.damian
	 * 
	 * @param username
	 * @return
	 */
	public UserAuth getUserAuthByPersonId(int personId);

		
	/**
	 * Returns a Persons for Athorisation purposes
	 *
	 * @author dan.damian 
	 */
	public Person getForAuthorization(Integer personId);
	
	/**
	 * Returns a person itentified by it's username. Person contains general info attributes.
	 * 
	 * @author dd
	 */
	public Person getByUsername(String username);
	
	/**
	 * Returns a list of Persons identified by their's user name. 
	 * Person contains general info attributes.
	 * 
	 * @author dd
	 */
	public List<Person> getByUsername(String[] usernames, boolean isNotDeleted);
	
	/**
	 * Returns a person identified by it's id. Person contains general info attributes and it's roles.
	 * 
	 * @author dd
	 * @param personId
	 */
	public Person getWithRoles(Integer personId);
	
	/**
	 * Returns a person identified by it's id. Person contains general info attributes and it's departments.
	 * 
	 * @author dd
	 * @param personId
	 */
	public Person getWithDepartments(Integer personId);
	
	/**
	 * Returns a person identified by it's id. Person contains general info attributes and 
	 * all related entities.
	 * 
	 * @author dd
	 * @param personId
	 */
	public Person getAll(Integer personId);
	
	/**
	 * Returns all persons in the data base. 
	 * all related entities.
	 * 
	 * @author dd
	 */
	public List<Person> list();

	/**
	 * Searches for persons after criterion from searchPersonBean.
	 * 
	 * @author alu
	 * @author dd
	 *
	 * @return A list of log beans 
	 * @throws ParseException 
	 */
	public List<Person> getFromSearch(SearchPersonBean searchPersonBean, boolean isDeleteAction , List<Integer> orgs) throws ParseException;
	
	/**
	 * Returns a Person that is the Manager of this Department
	 * @author dd 
	 * @param departmentId this Department id
	 * @return a Person
	 */
	public Person getManagerOfDepartment(Integer departmentId);
	
	/**
	 * Returns a list of Persons within those Departments.
	 * @author dd
	 */
	public List<Person> getFromDepartments(Integer[] departmentIds);
	
	/**
	 * It is used in OMUserDetails - a Spring Security Component
	 * 
	 * @author dan.damian
	 * 
	 * @param username
	 * @return
	 */
	public UserAuth getUserAuthByUsername(String username);
	
	/**
	 * Return a list with all the persons from the given organisation
	 * 
	 * @author alu
	 */
	public List<Person> getPersonsByOrganizationId(int organizationId);
	
	/**
	 * Return the number of persons from the given organisation
	 * 
	 * @author Adelina
	 */
	public long getPersonsCountByOrganization(Integer organizationId);
	
	/** 
	 * Checks if a given organisation has an admin
	 * 
	 * @author Adelina
	 */
	public boolean hasAdminByOrganisation(Integer organizationId);		
	
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
	public List<Person> getPersonsByOrganizationAndUserGroup(int organizationId, int userGroupId);
	
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
	public List<Person> getPersonsByOrganizationAndDepartment(Integer organizationId, Integer departmentId);
	
	/**
	 * Return a list with all the persons from the given organisation
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<Person> getPersonsSimpleByOrganizationId(int organizationId, boolean isNotDeleted);
	
	/**
	 * Return a list with all the deactivated persons from the given organisation
	 * 
	 * @author Liviu
	 * 
	 * @param organizationId
	 * @return
	 */
	//public List<Person> getDeactivatedPersonsSimpleByOrganizationId(int organizationId);
	
	
	/**
	 * Return a list with all the deactivated PersonsIds from the given Organization and Module
	 * 
	 * @author Liviu
	 * 
	 * @param organizationId
	 * @param moduleId
	 * @return
	 */
	@Transactional (rollbackFor=Exception.class)
	public Entry<List<Integer>, List<Person>> getDeletedPersonsIdsByOrganizationIdAndModuleId(int organizationId, int moduleId);
	
	/**
	 * Return a list with all the PersonsIds from the given Organization and Module
	 * 
	 * @author mitziuro
	 * 
	 * @param organizationId
	 * @param moduleId
	 * @return
	 */
	@Transactional (rollbackFor=Exception.class)
	public Entry<List<Integer>, List<Person>> getPersonsIdsByOrganizationIdAndModuleId(int organizationId, int moduleId);
	
	
	
	/**
	 * Returns a person itentified by it's id. Person contains general info attributes and picture
	 * 
	 * @author Adelina
	 *
	 * @param personId
	 * @return
	 */
	public Person getWithPicture(Integer personId);
	
	/**
	 * Return a list of persons that belongs to a given department, 
	 * and not other departments
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param departmentId
	 * @return List<Person>
	 */
	public List<Person> getPersonByOnlyOneDepartment(Integer organisationId, Integer departmentId);
	
	/**
	 * Return a list of person from a given organization,
	 * that has a certain role
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param roleName
	 * @return
	 */
	public List<Person> getPersonsWithRoleByOrganisation(Integer organisationId, String roleName);
	
	/**
	 * Returns a list of the persons from the user group with the specified id
	 * 
	 * @author Coni
	 * 
	 * @param userGroupId
	 * @return
	 */
	public List<Person> getByUserGroupId(Integer userGroupId);
	
    /**
     * Returns persons identified by personId. Person contains general info attributes.
     * 
     * @author Coni
     */
	
	public List<Person> getByPersonId(Integer[] personIds);
	
    /**
     * Retrieves from search a list of person with basic info 
     * 
     * @author Coni
     * @param searchPersonBean
     * @return
     */
    public List<Person> getFromSearchSimple(SearchPersonBean searchPersonBean, boolean withDeleted);
    
    /**
     * Disables all persons from all organisations
     *
     * @author alu
     */
    public void disableAll();
    
    /**
	 * Returns a person identified by it's id. Person constains only the status.
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public Person getWithStatus(Integer personId);
	
	/**
	 * Returns a list with all the persons from the given organisation that are active( without those that are deleted, and those that are disabled)
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param departmentId
	 * @return
	 */
	public List<Person> getPersonsActivated(Integer organizationId);
	
    /**
     * Retrieves from search a list of person with basic info and pagination
     * 
     * @author Coni
     * @param searchPersonBean
     * @return
     */
    public List<Person> getFromSearchSimpleWithPagination(SearchPersonBean searchPersonBean, boolean withDeleted);
}
