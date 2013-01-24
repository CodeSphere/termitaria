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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ro.cs.om.entity.Permission;
import ro.cs.om.entity.SearchPermissionBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.web.entity.PermissionWeb;


/**
 * Singleton which expose business methods for Permission item
 *
 * @author matti_joona
 */
public class BLPermission extends BusinessLogic{

	//singleton implementation
    private static BLPermission theInstance = null;
    private BLPermission(){};
    static {
        theInstance = new BLPermission();
    }
    public static BLPermission getInstance() {
        return theInstance;
    }
        
    /**
     * Returns all permissions
     *
     * @author alu
     * 
     * @throws BusinessException
     */
    public List<Permission> getAllPermissions() throws BusinessException{
    	logger.debug("getAllPermissions - START");
    	List<Permission> list = null;
    	try{
    		list = DaoBeanFactory.getInstance().getDaoPermission().getAllPermissions();
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_LIST, e);
    	}
    	logger.debug("getAllPermissions - END");
    	return list;
    }
    
    /**
     * Returns all permissions except the ones from the Set(permissions). If a permission from the set is not found in the db, throw exception
     *
     * @author alu
     * 
     * @param permissions A Set of permissions - probably from a role :)
     * @return
     * @throws BusinessException
     */
    
    public List<Permission> getAllPermissionsNotInSet(Set<Permission> permissions) throws BusinessException{
    	logger.debug("getAllPermissionsNotInSet - START");
    	List<Permission> list = null;
    	try{
    		Permission permissionFromRole = null;
    		Permission permissionFromList = null;
    		// get all permissions
    		list = DaoBeanFactory.getInstance().getDaoPermission().getAllPermissions();
    		// check if I have permissions
    		if (permissions != null) {    		
	    		Iterator<Permission> it = permissions.iterator();
	    		while(it.hasNext()){
	    			// for every permission, remove it from my list
	    			permissionFromRole = (Permission) it.next();
	    			boolean foundPermission = false;
	    			// first, we have to find it
	    			for (int i = 0; i < list.size(); i++){
	    				permissionFromList = list.get(i);
	    				if (permissionFromList.getPermissionId() == permissionFromRole.getPermissionId()){
	    					foundPermission = true;
	    					break;
	    				}
	    			}
	    			if (foundPermission){
	    				list.remove(permissionFromList);
	    			} else {
	    				// it means that a permission from my set doesn't exist in the db, so throw exception
	    				throw new Exception();
	    			}
	    		}
    		}
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_LIST, e);
    	}
    	logger.debug("getAllPermissionsNotInSet - END");
    	return list;
    }
    
    /**
     * Deletes permission identified by its id
     *
     * @author alu
     * 
     * @param idPermission
     * @throws BusinessException
     */
    public void delete(int idPermission) throws BusinessException{
    	logger.debug("delete - START id:".concat(String.valueOf(idPermission)));   
    	try{
    		DaoBeanFactory.getInstance().getDaoPermission().delete(idPermission);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_DELETE, e);
    	}
    	logger.debug("delete - END");    
    }
    
    /**
	 * Deletes a permission with all components identified by its id
	 * 
	 * @author Adelina
	 * @param permissionId
	 * @return Permission
     * @throws BusinessException 
	 */
	
	public Permission deleteAll(Integer permissionId) throws BusinessException{
		logger.debug("deleteAll - START id:".concat(String.valueOf(permissionId)));   
		Permission permission = null;
    	try{
    		permission = DaoBeanFactory.getInstance().getDaoPermission().deleteAll(permissionId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_DELETE, e);
    	}
    	logger.debug("deleteAll - END");
    	return permission;
	}
    
    
    /**
     * Updates given permission
     *
     * @author alu
     * 
     * @param permission
     * @throws BusinessException
     */
    public void update(Permission permission) throws BusinessException{
    	logger.debug("update - START ");
    	try{
    		DaoBeanFactory.getInstance().getDaoPermission().update(permission);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_UPDATE, e);
    	}
    	logger.debug("update - END");
    }
    
    /**
     * Adds given permission
     *
     * @author alu
     * 
     * @param permission
     * @throws BusinessException
     */
    public void add(Permission permission) throws BusinessException{
    	logger.debug("add - START ");
    	try{
    		DaoBeanFactory.getInstance().getDaoPermission().add(permission);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_ADD, e);
    	}
    	logger.debug("add - END");
    }
    
    /**
     * Returns the permission identified by idPermission
     *
     * @author alu
     * 
     * @param idPermission
     * @throws BusinessException
     */
    public Permission get(int idPermission) throws BusinessException{
    	logger.debug("get- START id:".concat(String.valueOf(idPermission)));
    	Permission permission = null;
    	try{
    		permission = DaoBeanFactory.getInstance().getDaoPermission().get(idPermission);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_GET, e);
    	}
    	logger.debug("get- END");
    	return permission;
    }
    
    /**
     * Searches for permissions after criterion from searchPermissionBean
     * @author alu
     * 
     * @param searchPermissionBean - Bean that contains the search criterion
     * @return A list of permission beans
     * @throws BusinessException
     */
        
    public List<PermissionWeb> getResultsForSearch(SearchPermissionBean searchPermissionBean, boolean isDeleteAction, Set<Integer> modulesIds) throws BusinessException{
    	logger.debug("getResultsForSearch - START");
    	List<PermissionWeb> res = null;
    	Permission permission = null;    	
    	try {
    		res = DaoBeanFactory.getInstance().getDaoPermission().getPermissionBeanFromSearch(searchPermissionBean, isDeleteAction, true, modulesIds);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PERMISSION_GET_RESULTS, e);
    	}
    	logger.debug("getResultsForSearch - END");
    	return res;
    }
    
	/**
	 * Get the list of permissions for a module without the default permission for the module
	 * 
	 * @author Adelina
	 * 
	 * @param moduleId
	 * 
	 * @return List<Permission>, a list of permissions
	 * @throws BusinessException 
	 */
	
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissionByModule(Integer moduleId) throws BusinessException{
		
		logger.debug("getPermissionByModule BL IMPL - START - ");
	
    	List<Permission> permissions = null;
    	try{
    		permissions = DaoBeanFactory.getInstance().getDaoPermission().getPermissionByModule(moduleId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_GET_BY_MODULE, e);
    	}
    	logger.debug("getPermissionByModule BL IMPL - END - ");		
    	
		return permissions;	

	}

	/**
	 * Get the default permission for a module
	 * 
	 * @author Adelina
	 * 
	 * @param moduleId
	 * @param permissionName
	 * @return the default permission for a module
	 * @throws BusinessException 
	 */
	public Permission getDefaultPermissionByModule(Integer moduleId) throws BusinessException{
		logger.debug("getDefaultPermissionByModule BL IMPL - START - ");
		
    	Permission permission = null;
    	try{    		
    		permission = DaoBeanFactory.getInstance().getDaoPermission().getDefaultPermissionByModule(moduleId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.PERMISSION_DEFAULT_GET_BY_MODULE, e);
    	}
    	logger.debug("getDefaultPermissionByModule BL IMPL - END - ");		
    	
		return permission;	
	}
	
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
	public List<Permission> getPermissionByModuleAndRole(Integer moduleId, Integer roleId) throws BusinessException{
		logger.debug("BL getPermissionByModuleAndRole - START -");
		
		List<Permission> permissions = null;
		try{
			permissions = DaoBeanFactory.getInstance().getDaoPermission().getPermissionByModuleAndRole(moduleId, roleId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PERMISSION_GET_BY_MODULE_NOT_ROLE, e);
		}
		
		logger.debug("BL getPermissionByModuleAndRole - END -");
		return permissions;
	}      
}
