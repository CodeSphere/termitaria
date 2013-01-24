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

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.Department;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchDepartmentBean;

/**
 * @author matti_joona
 * 
 */
public interface IDaoDepartment {

	/**
	 * Adds a Department
	 * 
	 * @author matti_joona
	 * @param Department
	 */
	@Transactional (rollbackFor=Exception.class)
	public int add(Department dept);

	/**
	 * Adds all data to a Department
	 * 
	 * @author Adelina
	 * @param Department
	 */
	@Transactional (rollbackFor=Exception.class)
	public void addAll(Department dept);

	/**
	 * Add a department that had just been imported. References: Has to: Parent
	 * Department Has not to: Persons, Department Manager
	 * 
	 * @author dan.damian
	 */
	@Transactional (rollbackFor=Exception.class)
	public int addFromImport(Department dept);

	/**
	 * Adds Fake Department when it's Imported from an XML. References: Has to:
	 * Organization Has not to: Parent Department, Persons, Department Manager
	 * 
	 * @author dan.damian
	 */
	@Transactional (rollbackFor=Exception.class)
	public int addFakeFromImport(Department dept);

	/**
	 * Update a department
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(Department dept);

	/**
	 * Updates a Department that has just been imported. References: Has to:
	 * Organization, Parent Department, Persons, Department Manager
	 * 
	 * @author dan.damian
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateFromImport(Department dept);

	/**
	 * Update a department that had just been imported. References: Has to:
	 * Parent Department Has not to: Persons, Department Manager
	 * 
	 * @author dan.damian
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateFromImport1(Department dept);

	/**
	 * Updates a Fake Department that has just been imported. References: Has
	 * to: Persons, Department Mananger Has not to: Parent Department
	 * 
	 * @author dan.damian
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateFakeFromImport(Department dept);

	/**
	 * Update all the data for a department
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateAll(Department dept);

	/**
	 * Delete a department only the common attributes.
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public Department delete(Integer departmentId);

	/**
	 * Update a persons list.
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updatePersons(Department dept, Set<Person> persons);

	/**
	 * Update the manager of the department.
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateManager(Department department);

	/**
	 * Update the CEO manager
	 * 
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateCEO(Department department);

	/**
	 * Update the parent department of the department
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateParent(Department dept);

	/**
	 * Returns a department itentified by it's id.
	 * 
	 * @author matti_joona
	 */
	public Department get(Integer departmentId);

	/**
	 * Returns all data related to a department itentified by it's id.
	 * 
	 * @author matti_joona
	 */
	public Department getAll(Integer departmentId);

	/**
	 * Returns a Department knowing it's Manager
	 * 
	 * @author dd
	 */
	public List<Department> getForManager(Integer managerPersonId);

	/**
	 * Returns Fake Department for one Organization
	 * 
	 * @author dd
	 */
	public Department getFakeForOrganisation(Integer organisationId);

	/**
	 * Searches for Departments after criterion from searchDepartmentBean.
	 * 
	 * @author alu
	 * @author dan.damian
	 */
	public List<Department> getFromSearch(
			SearchDepartmentBean searchDepartmentBean, boolean isDeleteAction)
			throws ParseException;

	/**
	 * Gets the number of departments for an organisation (without the fake
	 * departments)
	 * 
	 * @author Adelina
	 * @parm organisationId
	 * @return number of departments from an organisation
	 */
	public int getCountDepartments(Integer organisationId);

	/**
	 * Lists all Potential Parent Departments for this Department.
	 * 
	 * @author dan.damian
	 */
	public List<Department> listPotentialParentDepartments(
			Integer departmentId, Integer organisationId);

	/**
	 * Lists all departments within an organization. A department contains only
	 * the general info attributes.
	 * 
	 * @author dd
	 */
	public List<Department> listByOrganisation(Integer organisationId);

	/**
	 * Lists all Root Departments of this Organization. A department contains
	 * only the general info attributes.
	 * 
	 * @author dd
	 */
	public List<Department> listRootByOrganisation(Integer organisationId);

	/**
	 * List all SubDepartments of this Department
	 * 
	 * @author dd
	 * @param departmentId
	 *            this Department's id
	 * @return
	 */
	public List<Department> listSubDepartments(Integer departmentId);

	/**
	 * List of persons from a specified department
	 * 
	 * @author matti_joona
	 */
	public Set<Person> listPersons(Integer departmentId, Integer organisationId);

	/**
	 * Returns all Sub Departments of this Departments. It's recursive, and
	 * practically returns all Departments subtree found under this department.
	 * 
	 * @author dan.damian
	 * @return
	 */
	public List<Department> listAllSubDepartments(Integer departmentId);
	
	/**
	 * List the first level SubDepartments of this Department, each of them containing only the basic information 
	 * 
	 * @author coni
	 * @param departmentId this Department's id 
	 * @return
	 */
	public List<Department> listFirstLevelSubDepartments(Integer departmentId);
	
	/**
	 * Returns all data related to a department itentified by it's id, including manager and persons list
	 * 
	 * @author coni
	 */
	public Department getWithPersonsOnly(Integer departmentId);
	
	/**
	 * Returns a department(with persons) itentified by it's id.
	 * 
	 * @author Adelina
	 */
	public Department getWithPersons(Integer departmentId);
	
	/**
	 * Return a list with the department from the given organization, without 
	 * those that belongs to a given person
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param personId
	 * @return List<Department>
	 */
	public List<Department> getDepartmentByOrganizationAndPerson(Integer organizationId, Integer personId);
	
	/**
	 * Update a department manager id
	 * 
	 * @author Adelina
	 */
	public void updateManagerId(Department dept);
	
}
