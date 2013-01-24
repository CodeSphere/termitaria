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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoModule;
import ro.cs.om.web.security.UserAuth;


/**
 * Module - Business Layer
 *
 * @author dd
 */
public class BLModule extends BusinessLogic {

	//singleton implementation
    private static BLModule theInstance = null;
    private IDaoModule moduleDao = DaoBeanFactory.getInstance().getDaoModule();
    
    private BLModule(){};
    static {
        theInstance = new BLModule();
    }
    public static BLModule getInstance() {
    	return theInstance;
    }

    /**
     * Returns All Modules. A module contains it's related Roles. 
     * entities.
     * 
     * @author dd
     */
    public List<Module> listWithRoles() throws BusinessException{
      	List<Module> list = null;
    	try{
    		list = moduleDao.listWithRoles();
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.MODULE_LIST_WITH_ROLES, bexc);
    	}
    	return list;
    }
    
    
    /**
     * Returns All Modules 
     * entities.
     * 
     * @author dd
     */
    public List<Module> getAllModules() throws BusinessException{
      	List<Module> list = null;
    	try{
    		list = moduleDao.getAllModules();
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.MODULE_LIST, bexc);
    	}
    	return list;
    }
    
    /**
     * Get general data for a module
     * @author Adelina
     */
    public Module get(Integer moduleId) throws BusinessException{
    	Module module = null;
    	try{
    		module = moduleDao.get(moduleId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.MODULE_GET, e);
    	}
    	return module;
    }  
    
    /**
     * Returns a list with all an organisation modules and their roles only from
     * that organisation
     * 
     * @author coni
     * @author Adelina
     */
    public List<Module> listModulesWithRolesByOrganisation( int organisationId, Person person) throws BusinessException{
    	logger.debug("listModulesWithRolesByOrganisation - START ");
      	List<Module> modules = new ArrayList<Module>();
      	UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      	boolean hasAdmin = false;
    	try{
    		for ( Module orgModule : BLOrganisation.getInstance().getModules(organisationId)){
    			Set<Role> roles =  new HashSet<Role>();
    			List<Role> moduleRoles = BLRole.getInstance().getRolesByModuleAndOrganisation(organisationId, orgModule.getModuleId(), person);    			
    			for ( int i=0; i<moduleRoles.size(); i++ ){
    				if(userAuth.hasAuthority(PermissionConstant.getInstance().get_Super())){    					
    					try{
    						hasAdmin = BLPerson.getInstance().hasAdminByOrganisation(organisationId);
    					}catch(BusinessException be){
    						logger.error(be.getMessage(), be);			
    					} catch(Exception e){
    						logger.error("", e);			
    					}
    					if(hasAdmin) {
    						if(IConstant.OM_USER.equals(moduleRoles.get(i).getName()) || IConstant.OM_ADMIN.equals(moduleRoles.get(i).getName())){
    	    					continue;
    						}
    					} else if(IConstant.OM_USER.equals(moduleRoles.get(i).getName())){
	    					continue;
	    				}
    				} else if(IConstant.OM_USER.equals(moduleRoles.get(i).getName()) || IConstant.OM_ADMIN.equals(moduleRoles.get(i).getName())){
	    					continue;
	    			}     		
    				roles.add(moduleRoles.get(i));
    			}
    			orgModule.setRoles(roles);
    			logger.debug(" module = " + orgModule.getName() + ", roles = " + roles);
    			modules.add(orgModule);
    		}
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.MODULE_LIST_WITH_ROLES_BY_ORG, bexc);
    	}
    	return modules;
    }
    
    /**
     * Get a module by it's name
     * @author mitziuro
     */
    public Module get(String moduleName) throws BusinessException{
    	logger.debug("get - START NAME: ".concat(moduleName));
    	Module module = null;
    	try{
    		module = moduleDao.get(moduleName);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.MODULE_GET_NAME, e);
    	}
    	logger.debug("get - START");
    	return module;
    }  
    
    /**
	 * Returns all the modules without the OrganisationManagement Module
	 * and wihout those that belongs to the given organisation
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @return List<Module>
	 */
	public List<Module> getModulesWithoutOMForOrganisation(Integer organisationId) throws BusinessException {
		logger.debug("BL getModulesWithoutOM - START -");
		List<Module> modules = null;
		try{
			modules = moduleDao.getModulesWithoutOMForOrganisation(organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.MODULE_GET_WITHOUT_OM_FOR_ORG, e);
		}
		
		logger.debug("BL getModulesWithoutOM - END -");
		return modules;
	}
		
	/**
	 * @author Coni
	 * Returns all the modules ids for an organisation
	 * @param organisationId
	 * @return
	 * @throws BusinessException
	 */
	public List<Integer> listModulesIdsByOrganisation(Integer organisationId) throws BusinessException {
		logger.debug("getModulesIdsByOrganisation START");
		List<Integer> modulesIds = new ArrayList<Integer>();
		try {
			List<Module> modules = moduleDao.listModulesIdsByOrganisation(organisationId);
			for (Module m : modules){
				modulesIds.add(m.getModuleId());
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.MODULE_LIST_BY_ORG, e);
		}
		logger.debug("getModulesIdsByOrganisation END");
		return modulesIds;
	}
	
	/**
	 * Returns all the modules without the OrganisationManagement Module
	 * hat belongs to the given organisation
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @return List<Module>
	 * @throws BusinessException 
	 */
	public List<Module> getModulesForOrganisationWithoutOM(Integer organisationId, Integer parentId) throws BusinessException {
		logger.debug("getModulesForOrganisationWithoutOM - START -");
		List<Module> modules = null;
		try{
			modules = moduleDao.getModulesForOrganisationWithoutOM(organisationId, parentId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.MODULE_GET_FOR_ORG_WITHOUT_OM, e);
		}
		
		logger.debug("getModulesForOrganisationWithoutOM - END -");
		return modules;
	}
}
