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

import java.util.HashSet;
import java.util.List;

import ro.cs.om.entity.Module;

/**
 * @author matti_joona
 *
 */
public interface IDaoModule {

	/**
	 * Add a module
	 * 
	 * @author matti_joona
	 */
	public void add(Module module);
	
	/**
	 * Get a module
	 * 
	 * @author matti_joona
	 */
	public Module get(Integer moduleId);
	
	/**
	 * Get a module by name
	 * 
	 * @author matti_joona
	 */
	public Module get(String name);
	
	/**
	 * Delete a list of modules
	 * 
	 * @author matti_joona
	 */
	public void delete(HashSet<Module> modules);
	
	/**
	 * Get a module with all it's roles.
	 * 
	 * @author dd
	 */
	public Module getWithRoles(Integer moduleId);
	
	/**
	 * Returns all the modules each one with it's roles.
	 * 
	 * @author dd
	 */
	public List<Module> listWithRoles();
	
	/**
	 * Returns all the modules.
	 * 
	 * @author dd
	 */
	public List<Module> getAllModules();
	
	/**
	 * Returns all the modules without the OrganisationManagement Module
	 * and wihout those that belongs to the given organisation
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @return List<Module>
	 */
	public List<Module> getModulesWithoutOMForOrganisation(Integer organisationId);
	
	/**
	 * Returns all the modules ids for an organisation
	 * @author Coni
	 * @param organisationId
	 * @return
	 */
	public List<Module> listModulesIdsByOrganisation(Integer organisationId);
	
	/**
	 * Returns all the modules without the OrganisationManagement Module
	 * hat belongs to the given organisation
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @return List<Module>
	 */
	public List<Module> getModulesForOrganisationWithoutOM(Integer organisationId, Integer parentId);
}
