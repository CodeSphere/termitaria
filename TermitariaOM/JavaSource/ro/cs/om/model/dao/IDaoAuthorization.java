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
import java.util.List;

import org.springframework.dao.DataAccessException;

import ro.cs.om.entity.Module;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.Setting;

/**
 * Dao Interface, implemented by AuthorisationDaoImpl
 * 
 * @author dd
 */
public interface IDaoAuthorization {

	/**
	 * Authentifies a person.
	 * 
	 * @author dd
 	 */ 
	public Person authentify(String username, String password) throws DataAccessException, NoSuchAlgorithmException, UnsupportedEncodingException;
	
	/**
	 * Returns a list with the Suite's modules to which the user
	 * has access to.
	 * 
	 * @author dd
	 */
	public List<Module> getAllowedModules(Integer userPersonId);
	

	/**
	 * Returns a list with all the Termitaria Suite's modules
	 * has access to.
	 * 
	 * @author dd
	 */
	public List<Module> getAllModules();
	
	/**
	 * Returns a list with the Termitaria suite's modules to which the user
	 * has access to. The modules contains user roles.
	 * 
	 * @author dd
	 */
	public List<Module> getAllowedModulesWithRoles(Integer userPersonId);

	
	
	/**
	 * Returns a list with user's roles for the specified module.
	 * 
	 * @author dd
	 * @param userId
	 * @param moduleId
	 * @return
	 */
	public List<Role> getUserRolesForModule(Integer userId, Integer moduleId);
	
	
	/**
	 * Returns a list with user's roles for ADMIN IT
	 * 
	 * @author dd
	 * @param userId
	 * @return
	 */
	public List<Role> getUserRolesForAdminIT(Integer userPersonId);

		
	/**
	 * Returns a list with permissions for given list of roles.
	 * 
	 * @author dd
	 * @param roles
	 * @return
	 */
	public List<Permission>  getPermissionsForRoles(List<Role> roles);
	
	
	/**
	 * Returns a list with settings specific for an organization.
	 * 
	 * @author dd
	 * @param roles
	 * @return
	 */
	public List<Setting>  getSettingsForOrganisation(Integer organisationId);
	
	/**
	 * Verifies if a certaing Person has an ADMIN_IT authority in this application.
	 * For a Person to have this authority it have to has an ADMIN_IT role.
	 * @param personId
	 * @return true if this person really is an ADMIN_IT
	 */
	public boolean isUserAdminIT(Integer personId);
}
