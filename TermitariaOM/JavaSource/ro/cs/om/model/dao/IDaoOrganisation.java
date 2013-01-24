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
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.Calendar;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchOrganisationBean;
import ro.cs.om.entity.Setting;
import ro.cs.om.entity.UserGroup;


/**
 * @author matti_joona
 *
 */
public interface IDaoOrganisation {

	/**
	 * Add an organisation
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(Organisation org);
	

	/**
	 * Add an Organization containing only it's simple attributes
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void addOnlyWithSimpleAttrs(Organisation org);

	/**
	 * Get an organisation
	 * 
	 * @author matti_joona
	 */
	public Organisation get(Integer organisationId);

	/**
	 * Get common attrs for an organisation
	 * 
	 * @author matti_joona
	 */
	public Organisation getAll(Integer organisationId);
	
	
	/**
	 * Get an Organization to be exported.
	 * 
	 * @author dan.damian
	 */
	public Organisation getForExport(Integer organisationId);

	/**
	 * Get general info and calendar about an organisation
	 * 
	 * @author matti_joona
	 */
	public Organisation getWithCalendar(Integer organisationId);
	
	/**
	 * Returns all the organisations for classified lists
	 * 
	 * @author alu
	 */
	public List<Organisation> getAllOrganisationsForNom();
	
	/**
	 * Returns all the organisations for hasAudit context map 
	 * 
	 * @author coni
	 */
	public List<Organisation> getAllOrganisationsWithModulesForNom();
	
	/**
	 * Get a list of organisations that has not a calendar defined
	 * @author Adelina 
	 * 
	 * @return List<Organisation>
	 */
	public List<Organisation> getAllOrganisationsForNomWithoutCalendar();
	
	/**
	 * Get the 'fake'/'default' department of organisation
	 * 
	 * @author matti_joona
	 */
	public Department getDefaultDepartment(Integer organisationId);

	
	/**
	 * Returns the list of the 'fake'/'default' Departments for each Organization that is a child of this
	 * Parent Organization identified by parentId.
	 *
	 * @param parentId Parent Organization's Id
	 * 
	 * @author dan.damian
	 */
	public List<Department> getDefaultDepartmentsForParentId(Integer parentId);

	
	/**
	 * Get settings of an organisation
	 * 
	 * @author matti_joona
	 */
	public Set<Setting> getWithSettings(Integer organisationId);
	
	/**
	 * Returns the modules for an organization
	 * 
	 * @author alu
	 */
	public Set<Module> getWithModules(Integer organisationId);
	
	/**
	 * Returns an organisation with credentials and modules
	 * 
	 * @author Adelina
	 */
	public Organisation getOrganisationWithModules(Integer organisationId);
	
	/**
	 * Get roles of an organisation
	 * 
	 * @author matti_joona
	 */
	public Set<Role> getWithRoles(Integer organisationId);
	
	/**
	 * Get all person for an organisation
	 * 
	 * @author matti_joona
	 */
	public Set<Person> getWithPersons(Integer organisationId);
		
	/**
	 * Get all departments for an organisation
	 * 
	 * @author matti_joona
	 */
	public Set<Department> getWithDepartments(Integer organisationId);

	/**
	 * Update an organization.
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(Organisation org);
	
	/**
	 * Update all info for an organization.
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateAll(Organisation org);

	/**
	 * Get no of departments for a organization without
	 * retrieving entities.
	 * 
	 * @author matti_joona 
	 */
	public int getNoOfDepartments(Integer organisationId);
		
	/**
	 * Returns all the organizations from the database
	 * 
	 * @author dd 
	 */
	public List<Organisation> list();
	
	/**
	 * Searches for organisations after criterion from searchOrganisationBean.
	 * 
	 * @author alu
	 * @author dan.damian
	 * @author Adelina
	 * 
	 * @param searchOrganisationBean
	 * @param isChanged
	 * @param typeIds
	 * @return
	 * @throws ParseException
	 */	
	public List<Organisation> getFromSearch(SearchOrganisationBean searchOrganisationBean, boolean isChanged, Set<Byte> typeIds) throws ParseException;
	/**
	 * 
	 * saves a new organisation with roles
	 * @author mihai
	 *
	 * @param organisation
	 */
	@Transactional (rollbackFor=Exception.class)
	public void addOrganisationWithAll(Organisation organisation, Calendar calendar, Department dept, List<Setting> listSettings, UserGroup defaultUserGroup) throws Exception;
	
	/**
	 * Changes the status for the organisation.
	 * If the status is active = all the persons from the organisation are active
	 * If the status is inactive = all the persons from the organisation are inactive
	 * 
	 * @param Integer organisationId
	 * @param List<Person> persons
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateStatus(Integer organisationId, List<Person> persons);
	/**
	 * 
	 * returns an organisation with the given name
	 * @author mitziuro
	 * @param name
	 * @return
	 */
	public Organisation getOrgByName(String name);
	
	/**
	 * Get all info about an organisation
	 * 
	 * @author mitziuro
	 */
	public Organisation getForDelete(Integer organisationId);
	
	/**
	 * Get all level 1 branches for an organisation  
	 * @author mihai
	 * @param parentId
	 * @return
	 */
	public List<Organisation> getAllOrganisationsForParentId(Integer parentId);
	
	/**
	 * Get all level 1 branches for an organisation for listing 
	 * @author mihai
	 * @param parentId
	 * @return
	 */
	public List<Organisation> getAllSimpleOrganisationsForParentId(Integer parentId);
	/**
	 * Get the list of Organisation for a parentId, retrieving only the basic info about an organisation
	 * 
	 * @author coni
	 * @return List<Organisation>
	 */
	public List<Organisation> getAllOrganisationsByParentIdForOrgTree(Integer parentId);
	/**
	 * 
	 * Get all the branches Ids for an organisation
	 * 
	 * @author mitziuro
	 * @param parentId
	 * @return
	 */
	public List<Integer> getAllBranchIdsForOrganisation(Integer parentId);
	
	/**
	 * Deletes an organization and all its children, returning a map with their names and IDs
	 * @author coni
	 */
	@Transactional (rollbackFor=Exception.class)
	public Map<Integer, String> deleteOrgs(Organisation organisation) throws Exception;
	
	/**
	 * 
	 * Get all orgainsationIds that have a module identified by id
	 * @author mitziuro
	 * @param moduleId
	 * @return
	 */
	public List<Integer> getOrganisationIdsByModule(Integer moduleId);
	
}
