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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.cs.om.entity.Module;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchRoleBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.model.dao.IDaoRole;
import ro.cs.om.web.entity.RoleWeb;


/**
 * Singleton which expose business methods for Role item
 * 
 * @author matti_joona
 */
public class BLRole extends BusinessLogic {

	private IDaoRole roleDao = DaoBeanFactory.getInstance().getDaoRole();
	private IDaoOrganisation organisationDao = DaoBeanFactory.getInstance().getDaoOrganisation();
	//singleton implementation
    private static BLRole theInstance = null;
    private BLRole(){};
    static {
        theInstance = new BLRole();
    }
    public static BLRole getInstance() {
        return theInstance;
    }

    /**
     * Adds given role
     *
     * @author alu
     * 
     * @param roleWeb
     * @throws BusinessException
     */
    public void add(RoleWeb roleWeb) throws BusinessException{
    	logger.debug("add - START ");
    	try{
    		DaoBeanFactory.getInstance().getDaoRole().add(roleWeb);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.ROLE_ADD, e);
    	}
    	logger.debug("add - END");
    }
    
    /**
     * Updates given role
     *
     * @author alu
     * 
     * @param rw
     * @throws BusinessException
     */
    public void updateRoleWeb(RoleWeb rw) throws BusinessException{
    	logger.debug("update - START ");
    	try{
    		DaoBeanFactory.getInstance().getDaoRole().updateRoleWeb(rw);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.ROLE_WEB_UPDATE, e);
    	}
    	logger.debug("update - END"); 
    }
    
    /**
     * Updates given role
     *
     * @author alu
     * 
     * @param role
     * @throws BusinessException
     */
    public void update(Role role) throws BusinessException{
    	logger.debug("update - START ");
    	try{
    		DaoBeanFactory.getInstance().getDaoRole().update(role);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.ROLE_UPDATE, e);
    	}
    	logger.debug("update - END");
    }
    
    /**
	 * Delete role identifed by it's id with all the components
	 * 
	 * Return the Role that has been deleted
	 * @author Adelina
     * @throws BusinessException 
	 */
	
	public Role deleteAll(Integer roleId) throws BusinessException{
		logger.debug("deleteALL BL - START");
		Role role = null;
		try{
			role = roleDao.deleteAll(roleId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.ROLE_DELETE, e);
		}
		logger.debug("deleteALL BL - END");
		return role;
	}
    
    public void delete(int idRole) throws BusinessException{
    	logger.debug("delete - START id:".concat(String.valueOf(idRole)));
    	try{
    		DaoBeanFactory.getInstance().getDaoRole().delete(idRole);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.ROLE_DELETE, e);
    	}
    	logger.debug("delete - END");
    }
    
    public Role get(Integer roleId) throws BusinessException{
    	logger.debug("get - START - roleId: ".concat(String.valueOf(roleId)));
    	Role role = null;
    	try{
        	role = roleDao.get(roleId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ROLE_GET, bexc);
    	}
    	logger.debug("get - END");
    	return role;
    }
    
    /**
     * Gets a role with the given id
     *
     * @author alu
     * 
     * @param roleId
     * @return The role in roleWeb format
     * @throws BusinessException
     */
    public RoleWeb getRoleWeb(Integer roleId) throws BusinessException{
    	logger.debug("getRoleWeb - START");
    	RoleWeb rw = null;
    	try{
        	rw = roleDao.getRoleWeb(roleId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.ROLE_WEB_GET, e);
    	}
    	logger.debug("getRoleWeb - END - roleId:".concat(String.valueOf(rw.getRoleId())));
    	return rw;
    }
    
    public Role getWithModule(Integer roleId) throws BusinessException{
    	Role role = null;
    	try{
        	role = roleDao.getWithModule(roleId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ROLE_GET, bexc);
    	}
    	return role;
    }
        
    /**
     * Returns all the permissions for a given role
     *
     * @author alu
     * 
     * @param roleId
     * @return
     */
    public Set<Permission> getPermissions(int roleId) throws BusinessException{
    
    	logger.debug("getPermissions - START - roleId:".concat(String.valueOf(roleId)));
		Set<Permission> permissions = null; 
		try {
			Role role = roleDao.getWithPermissions(roleId);
			permissions = role.getPermissions();
		} catch(Exception e) {
			throw new BusinessException(ICodeException.ROLE_GET_PERMISSIONS, e);
		}
		logger.debug("getPermissions - END - size:" .concat(permissions != null? String.valueOf(permissions.size()) : ""));
		return permissions;
    }
    
    /**
     * Returns all the permissions for a given role, without the permission default for the given module
     *
     * @author Adelina
     * 
     * @param roleId
     * 
     * @return Set<Permission> the list of permissions
     */
    
    public Set<Permission> gePermissionsWithoutDefault(int roleId) throws BusinessException{
      	logger.debug("gePermissionsWithoutDefault - START - roleId:".concat(String.valueOf(roleId)));
		
      	Set<Permission> permissions = new HashSet<Permission>(); 	
		
		try {
			Role role = roleDao.getAll(roleId);
			if(role != null) {
				int moduleId = role.getModule().getModuleId();
			
				permissions = role.getPermissions();
				logger.debug("permissions "+ permissions.size());
				try{
					if(permissions != null){
						logger.debug("permissions != null " + permissions.size());
						Permission permission = BLPermission.getInstance().getDefaultPermissionByModule(moduleId);				
						permissions.remove(permission);
						logger.debug("after remove permission " + permissions.size());
						return permissions;
					}
				} catch(BusinessException be){
					be.getStackTrace();
				}
			}
		} catch(Exception e) {
			throw new BusinessException(ICodeException.ROLE_GET_PERMISSIONS, e);
		}
		logger.debug("gePermissionsWithoutDefault - END - size:" .concat(permissions != null? String.valueOf(permissions.size()) : ""));
		logger.debug("permissions == null");
		return permissions;
    }
    
    /**
     * Returns all the permissions for a given role, with the permission default for the given module
     *
     * @author Adelina
     * 
     * @param roleId
     * 
     * @return Set<Permission> the list of permissions
     */
    
    public Set<Permission> gePermissionsWithDefault(int roleId) throws BusinessException{
      	logger.debug("gePermissionsWithDefault - START - roleId:".concat(String.valueOf(roleId)));
		
      	Set<Permission> permissions = new HashSet<Permission>(); 	
		
		try {
			Role role = roleDao.getAll(roleId);
			if(role != null) {						
				permissions = role.getPermissions();
				logger.debug("permissions "+ permissions.size());
			}
		} catch(Exception e) {
			throw new BusinessException(ICodeException.ROLE_GET_PERMISSIONS, e);
		}
		logger.debug("gePermissionsWithDefault - END - size:" .concat(permissions != null? String.valueOf(permissions.size()) : ""));		
		return permissions;
    }
           
    /**
     * 
     * Get the results for search
     * @author mitziuro
     *
     * @param searchRoleBean
     * @param isDeleteAction
     * @return
     * @throws BusinessException
     */
    public List<Role> getResultsForSearch(SearchRoleBean searchRoleBean, boolean isDeleteAction) throws BusinessException{
    	logger.debug("getResultsForSearch - START");
    	List<Role> res = null;
    	logger.debug("searachRoleBean = " + searchRoleBean.getOrganisationId());
    	try {
			Set<Module> modules = BLOrganisation.getInstance().getModules(searchRoleBean.getOrganisationId());
			Set<Integer> modulesIds = new HashSet();
			for ( Module module : modules){
				modulesIds.add(module.getModuleId());
			}
    		res = roleDao.getRoleBeanFromSearch(searchRoleBean, isDeleteAction, modulesIds);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.ROLE_GET_RESULTS, e);
    	}
    	logger.debug("getResultsForSearch - END");
    	return res;
    }
    
    /**
     * Gets the default roles (those that have organisationId = -999)
     * @author Adelina
     * @return List<Role>
     * @throws BusinessException 
     */

    @SuppressWarnings("unchecked")
	public List<RoleWeb> getDefaultRolesByModule(Integer moduleId) throws BusinessException{
    	logger.debug("getDefaultRoles BL - START - ");	
    	
    	List<RoleWeb> roles = null;
    	try{
    		roles = roleDao.getDefaultRolesByModule(moduleId);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.ROLE_GET, e);
    	}
    	for(RoleWeb role:roles){
    		logger.debug("Role name = " + role.getName());
    	}
    	logger.debug("getDefaultRoles BL - END - ");
    	
    	return roles;    	    
    }  
    
    /**
     * Add default roles to a given organization, by an organisationId
     * @param organisationId
     */
    public void addDefaultRoles(Integer organisationId, Integer moduleId) throws BusinessException{
    	logger.debug("Add default roles - START- ");
    	try{
    		roleDao.addDefaultRoles(organisationId, moduleId);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.ROLE_ADD, e);
		}
    	logger.debug("Add default roles  - END- ");
    }
    
    /**
     * Get roles by organisation and module
     * @author Adelina
     * @param organisationId
     * @param moduleId
     * @return List<Role>
     * @throws BusinessException 
     */

	@SuppressWarnings("unchecked")
	public List<RoleWeb> getRolesByModuleAndOrg(Integer organisationId, Integer moduleId) throws BusinessException{
		logger.debug("getRolesByModuleAndOrg BL - START");
		List<RoleWeb> roles = null;
		try{
			roles = roleDao.getRolesByModuleAndOrg(organisationId, moduleId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.ROLE_GET, e);
		}
		
		for(RoleWeb role:roles){
    		logger.debug("Role name = " + role.getName());
    	}
    	logger.debug("getRolesByModuleAndOrg BL - END - ");
    	
    	return roles;    	
	}
	/**
	 * 
	 * Return a role with the given name in the organisation if present
	 * @author mitziuro
	 * @param organisationId
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	public Role getRoleByNameAndOrg(Integer organisationId, String name) throws BusinessException{
		logger.debug("UniqueRoleByNameAndOrg BL - START");
    	Role role = null;
		try{
        	role = roleDao.getRoleByNameAndOrg(organisationId, name);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ROLE_GET_BY_NAME, bexc);
    	}
    	return role;
    }
	
    /**
     * Get roles by organisation and module
     * @author coni
     * @param organisationId
     * @param moduleId
     * @return List<Role>
     */
	public List<Role> getRolesByModuleAndOrganisation(Integer organisationId, Integer moduleId, Person person) throws BusinessException{
		logger.debug("getRolesByModuleAndOrganisation - BL ROLE - START");
		List<Role> roles = null;
		try{
			roles = roleDao.getRolesByModuleAndOrganisation(organisationId, moduleId, person);
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.ROLE_GET, bexc);
		}
		logger.debug("getRolesByModuleAndOrganisation - BL ROLE - END");
		return roles;
	}
	
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
	public List<Role> getRolesByNamesAndOrg(Integer organisationId, int count, String... name) throws BusinessException {
		logger.debug("getRolesByNameAndOrg - BL ROLE - START");
		List<Role> roles = null;
		try{
			roles = roleDao.getRolesByNamesAndOrg(organisationId, count, name);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.ROLES_GET_BY_NAMES, e);
		}
		logger.debug("getRolesByNameAndOrg - BL ROLE - END");
		return roles;
	}
	
	/**
	 * Checks if a role has a associated person
	 * 
	 * @author Adelina
	 * 
	 * @param roleId
	 * @return boolean
	 */
	 public boolean hasRoleAssociatePerson(Integer roleId) throws BusinessException{
		 logger.debug("hasRoleAssociatePerson - BL ROLE - START");
		 
		 boolean hasPerson = false;
		 try{
			hasPerson = roleDao.hasRoleAssociatePerson(roleId);
		 } catch(Exception e) {
			 throw new BusinessException(ICodeException.ROLE_HAS_PERSON, e);
		 }		 
		 logger.debug("hasRoleAssociatePerson - BL ROLE - END");
		 
		 return hasPerson;
	 }
	 
	

	/**
     * Gets the default roles (those that have status = 2)
     * 
     * @author Adelina
     * 
     * @return List<Role>
     * @throws BusinessException 
     */    
	public List<Role> getDefaultRoles(Integer moduleId) throws BusinessException{
		logger.debug("getDefaultRoles BL - START - ");	
		
		List<Role> roles = null;
		try{
			roles = roleDao.getDefaultRoles(moduleId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ROLE_GET, e);
		}
		for(Role role:roles){
			logger.debug("Role name = " + role.getName());
		}
		logger.debug("getDefaultRoles BL - END - ");
	
	return roles;    	    
    }  
	
	 /**
     *   
     * Add the default roles for a given module, to an organization,
     * if that organization doesn't have those roles
     * 
     * @author Adelina
     * 
     * @param moduleId
     * @param organisationId
	 * @throws BusinessException 
     */
    @SuppressWarnings("unchecked")
	public void addDefaultRolesToOrganisation(Integer moduleId, Integer organisationId) throws BusinessException {
		logger.debug("getDefaultRoles BL - START - ");	
				
		try{
			roleDao.addDefaultRolesToOrganisation(moduleId, organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ROLE_GET, e);
		}
		
		logger.debug("getDefaultRoles BL - END - ");			
	
	}
    
    /**
     * Gets the list of persons that has a specific permission
     * 
     * @author Adelina
     * 
     * @param permissionName
     * @return
     * @throws BusinessException 
     */
    public Set<Person> getPersonsByRole(String permissionName, Integer organizationId) throws BusinessException {
    	    
    	logger.debug("getPersonsByRole - START");
    	
    	Set<Person> persons = new HashSet<Person>();
    	
    	try{
    		persons = roleDao.getPersonsByRole(permissionName, organizationId);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.ROLE_GET, e);
		}
    	
    	logger.debug("getPersonBYRole - END");
    	return persons;    	
    	    
    }    
	
}
