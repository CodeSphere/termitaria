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

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchRoleBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.entity.RoleWeb;

/**
 * @author matti_joona
 *
 */
public interface IDaoRole {

	/**
	 * Adds given role
	 * 
	 * @author alu
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(RoleWeb roleWeb) throws Exception;
	
	/**
	 * Get a role
	 * 
	 * @author matti_joona
	 */
	public Role get(Integer roleId);
	
	/**
	 * Return a role with permissions only(without module and organisation)
	 * 
	 * @author alu
	 */
	public Role getWithPermissions(Integer roleId);
	
	/**
	 * Return a role with all components
	 * 
	 * @author alu
	 */
	public Role getAll(Integer roleId);
	
	/**
	 * Returns a role in RoleWeb format
	 * 
	 * @author alu
	 */
	public RoleWeb getRoleWeb(Integer roleId);
	
	/**
	 * Get a role with associated Module
	 * 
	 * @author dd
	 */
	public Role getWithModule(Integer roleId);
	
	/**
	 * Searches for roles after criterion from searchRoleBean
	 * @author alu
	 * @return A list of role beans 
	 * @throws ParseException 
	 */
	public List<Role> getRoleBeanFromSearch(SearchRoleBean searchRoleBean, boolean isDeleteAction, Set<Integer> moduleIds) throws ParseException;
	
	/**
     * Returns a default Role for a Module identified by it's name.
     * 
     * @author dan.damian
     */
	public RoleWeb getDefaultRoleByModule(Integer moduleId, String name);
	
	/**
	 * Delete role identifed by it's id with all the components
	 * 
	 * Return the Role that has been deleted
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public Role deleteAll(Integer roleId);
	
	/**
	 * Deletes role identified by its id
     *
	 * @author alu
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(int roleId);
	
	/**
     * Updates given role
     *
     * @author alu
     * 
     * @param rw
     * @throws BusinessException
     */
	@Transactional (rollbackFor=Exception.class)
    public void updateRoleWeb(RoleWeb rw);
    
    /**
     * Updates given role
     *
     * @author alu
     * 
     * @param rolr
     * @throws BusinessException
     */
	@Transactional (rollbackFor=Exception.class)
    public void update(Role role);
    
    
    /**
     * Gets the default roles (those that have organisationId = -999)
     * @author Adelina
     * @return List<Role>
     */

    public List<RoleWeb> getDefaultRolesByModule(Integer moduleId);
   
    
    /**
     * Add default roles to a given organisation, by an organisationId
     * @param organisationId
     */
    @Transactional (rollbackFor=Exception.class)
    public void addDefaultRoles(Integer organisationId, Integer moduleId);
            
    /**
     * Get roles by organisation and module
     * @author Adelina
     * @param organisationId
     * @param moduleId
     * @return List<Role>
     */

	public List<RoleWeb> getRolesByModuleAndOrg(Integer organisationId, Integer moduleId);
	/**
	 * 
	 * Add default roles to a given organisation, using the existent hibernate session
	 * @author mihai
	 * @param organisationId
	 * @param moduleId
	 * @param session
	 */
	@Transactional (rollbackFor=Exception.class)
	public void addDefaultRoles(Integer organisationId, Integer moduleId, Session session);
	/**
	 * 
	 * Returns an organisation with the given name
	 * @author mitziuro
	 * @param organisationId
	 * @param name
	 * @return
	 */
	public Role getRoleByNameAndOrg(Integer organisationId, String name);
		
    /**
     * Get roles by organisation and module
     * @author coni
     * @param organisationId
     * @param moduleId
     * @return List<Role>
     */
	public List<Role> getRolesByModuleAndOrganisation(Integer organisationId, Integer moduleId, Person person);	
	/**
	 * 
	 * Delete all the roles for an organisation
	 * @author mitziuro
	 *
	 * @param organisationId
	 */
	public void deleteByOrganisationId(int organisationId);
	
	/**
	 * Return the roles with the given names in the organisation if present
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param count
	 * @param name 
	 * @return List<Role> 
	 */
	public List<Role> getRolesByNamesAndOrg(Integer organisationId, int count, String... name);
	
	/**
	 * Checks if a role has a associated person
	 * 
	 * @author Adelina
	 * 
	 * @param roleId
	 * @return boolean
	 */
	 public boolean hasRoleAssociatePerson(Integer roleId);
	 
	 
	 /**
     * Gets the default roles (those that have status = 2)
     * 
     * @author Adelina
     * 
     * @return List<Role>
     */	    
	public List<Role> getDefaultRoles(Integer moduleId);
	
	 /**
     *   
     * Add the default roles for a given module, to an organization,
     * if that organization doesn't have those roles
     * 
     * @author Adelina
     * 
     * @param moduleId
     * @param organisationId
     */

	public void addDefaultRolesToOrganisation(Integer moduleId, Integer organisationId);
	
	/**
     * Gets the list of persons that has a specific permission
     * 
     * @author Adelina
     * 
     * @param permissionName
     * @param organizationId
     * @return
     */
    public Set<Person> getPersonsByRole(String permissionName, Integer organizationId);
		
}
