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

import ro.cs.om.entity.Permission;
import ro.cs.om.entity.SearchPermissionBean;

/**
 * @author matti_joona
 *
 */
public interface IDaoPermission {

	/**
	 * @author matti_joona
	 * 
	 * Add a permission
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(Permission permission);

	/**
	 * @author matti_joona
	 * 
	 * Get a permission
	 */
	public Permission get(Integer permissionId);
	
	/**
	 * Return a Permission with all components
	 * @author Adelina
	 * 
	 * @param permissionId
	 * @return Permission
	 */
	public Permission getAll(Integer permissionId);
	
	/**
	 * Returns all the permissions
	 * 
	 * @author alu
	 */
	public List<Permission> getAllPermissions();
	
	/**
	 * Deletes permission identified by its id
     *
	 * @author alu
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(int permissionId);
	
	/**
	 * Deletes a permission with all components identified by its id
	 * 
	 * @author Adelina
	 * @param permissionId
	 * @return Permission
	 */
	@Transactional (rollbackFor=Exception.class)
	public Permission deleteAll(Integer permissionId);
	
	/**
	 * Updates permission
	 *
	 * @author alu
	 * 
	 * @param permission
	 */		
	@Transactional (rollbackFor=Exception.class)
	public void update(Permission permission);
	
	/**
	 * Searches for permissions after criterion from searchPermissionBean
	 * @author alu
	 * @return A list of permission beans 
	 * @throws ParseException 
	 */
	public List getPermissionBeanFromSearch(SearchPermissionBean searchPermissionBean, boolean isDeleteAction, boolean isSuper, Set<Integer> modulesIds) throws ParseException;
	
	/**
	 * Get the list of permissions for a module without the default permission for the module
	 * 
	 * @author Adelina
	 * 
	 * @param moduleId
	 * @param permissionName
	 * 
	 * @return a list of permissions
	 */
	
	public List<Permission> getPermissionByModule(Integer moduleId);
	
	/**
	 * Get the default permission for a module
	 * 
	 * @author Adelina
	 * 
	 * @param moduleId
	 * @param permissionName
	 * @return the default permission for a module
	 */
	public Permission getDefaultPermissionByModule(Integer moduleId);
	
	
	/**
	 * Return a list with the permissions for the given module, without 
	 * those that are for a given role
	 * 
	 * @author Adelina
	 * 
	 * @param moduleId
	 * @param roleId
	 * @return List<Permission>
	 */
	public List<Permission> getPermissionByModuleAndRole(Integer moduleId, Integer roleId);
	
	/**
	 * Get a permission by name
	 * 
	 * @author Adelina
	 * 
	 * @param name
	 * @return
	 */
	public Permission getByName(String name);
			
}
