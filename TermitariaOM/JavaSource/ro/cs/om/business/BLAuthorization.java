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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ro.cs.om.common.ConfigParametersProvider;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoAuthorization;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.om.web.security.UserAuth;
import ro.cs.tools.Tools;

/**
 * 
 * Security - Business Layer
 * 
 * @author dd
 */
public class BLAuthorization extends BusinessLogic  {

	
	//singleton implementation
    private static BLAuthorization theInstance = null;
    private BLAuthorization(){};
    static {
        theInstance = new BLAuthorization();
    }
    public static BLAuthorization getInstance() {
        return theInstance;
    }
    
    
    private IDaoAuthorization authorizationDao = DaoBeanFactory.getInstance().getDaoAuthorization();
    private IDaoPerson personDao = DaoBeanFactory.getInstance().getDaoPerson();

    
    /**
     * Returns a list containing the modules to which this user has access to.
     * @author dan.damian 
     * @return
     */
    public List<Module> getAccessModules(int personId) {
    	logger.debug("getAccessModules START");
		// retrieve Modules to which this user has access to
		List<Module> modules = authorizationDao.getAllowedModules(personId);
		// populate other modules properties that are kept in configuration files
		String moduleUrlKey = "module.<id>.url";
		String moduleAltKey = "module.<id>.alt";
		for(int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);
			m.setUrl(ConfigParametersProvider.
						getConfigString(moduleUrlKey.replace("<id>", Integer.toString(m.getModuleId()))));
			m.setAlt(ConfigParametersProvider.
					getConfigString(moduleAltKey.replace("<id>", Integer.toString(m.getModuleId()))));
		}
		logger.debug("getAccessModules END");
		return modules;
    }
    
    /**
     * Returns a list containing all Termitaria Suite modules
     * @author dan.damian 
     * @return
     */
    public List<Module> getAllModules() {
    	logger.debug("getAllModules START");
		// retrieve Modules to which this user has access to
		List<Module> modules = authorizationDao.getAllModules();
		// populate other modules properties that are kept in configuration files
		String moduleUrlKey = "module.<id>.url";
		String moduleAltKey = "module.<id>.alt";
		for(int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);
			m.setUrl(ConfigParametersProvider.
						getConfigString(moduleUrlKey.replace("<id>", Integer.toString(m.getModuleId()))));
			m.setAlt(ConfigParametersProvider.
					getConfigString(moduleAltKey.replace("<id>", Integer.toString(m.getModuleId()))));
		}
		logger.debug("getAllModules END");
		return modules;
    }
    
    /**
     * Authorizes user.
     * @author dan.damian 
     */
    public void authorize(UserAuth userAuth, int moduleId) throws BusinessException {
    	logger.info("authorize START");
    	try{
	    	// retrieve person 
	    	Person person = personDao.getForAuthorization(userAuth.getPersonId());
	    	if (person != null) {
	    		// if person exists populate UserAuth bean
	    		userAuth.setAddress(person.getAddress());
	    		userAuth.setBirthDate(person.getBirthDate());
	    		userAuth.setEmail(person.getEmail());
	    		userAuth.setPhone(person.getPhone());
	    		userAuth.setObservation(person.getObservation());
	    		logger.debug("Setting Departments...");
	    		if (person.getDepts() != null && person.getDepts().size() > 0) {
	    			Tools.getInstance().printSet(logger, person.getDepts());
	    			userAuth.setDepartments(person.getDepts());
	    			logger.debug("Setting Organisation...");
	    			Organisation org = ((Department)person.getDepts().iterator().next()).getOrganisation();
	    			userAuth.setOrganisationId(org.getOrganisationId());
	    			userAuth.setOrganisationName(org.getName());
	    			userAuth.setOrganisationAddress(org.getAddress());
	    			userAuth.setOrganisationStatus(org.getStatus());
	    			logger.debug("Organisation Id = ".concat(Integer.toString(userAuth.getOrganisationId())));
	    		}
	    		logger.info("Setting OM Roles...");
	    		List<Role> roles = null;
	    		if (userAuth.isAdminIT()) {
	    			roles = authorizationDao.getUserRolesForAdminIT(userAuth.getPersonId());
	    		} else {
	    			roles = authorizationDao.getUserRolesForModule(userAuth.getPersonId(), moduleId);	
	    		}
	    		Tools.getInstance().printList(logger, roles);
	    		userAuth.setRoles(roles);
	    		
	    		logger.info("Setting Permissions...");
	    		userAuth.setPermissions(authorizationDao.getPermissionsForRoles(userAuth.getRoles()));
	    		
	    		logger.info("Setting OM Authorities...");
	    		userAuth.setAuthorities(getAuthorities(userAuth.getPermissions()));
	    		
	    		logger.info("Setting User's Groups...");
	    		String[] userGroups = null;
	    		if (person.getUserGroups() != null) {
	    			
	    			userGroups = new String[person.getUserGroups().size()];
		    		Iterator<UserGroup> it = person.getUserGroups().iterator();
		    		for(int i =0; i < person.getUserGroups().size(); i++) {
		    			UserGroup userGroup = it.next();
		    			if(userGroup.getName().equals(IConstant.DEFAULT_USER_GROUP_NAME)){
		    				userGroups[i] = IConstant.DEFAULT_USER_GROUP_NAME;
		    			} else {
		    				userGroups[i] = String.valueOf(userGroup.getUserGroupId());
		    			}
		    		}
		    	} else {
	    			logger.info("This Person doesn't belong to a specific group!");
	    			userGroups = new String[1];
	    			userGroups[0] = IConstant.DEFAULT_USER_GROUP_NAME;
	    		}
	    		for(int i =0; i < userGroups.length; i ++) {
	    			logger.debug("\tGroup: " + userGroups[i]);
	    		}
	    		userAuth.setUserGroups(userGroups);
	    		logger.info("Setting Organisation Settings...");
	    		userAuth.setSettings(authorizationDao.getSettingsForOrganisation(userAuth.getOrganisationId()));
	    		
	    		logger.info("Setting Out Of Office...");
	    		userAuth.setOutOfOffice(person.getOutOfOffice());
	    				
	    	}
    	}catch(Exception ex){
    		throw new BusinessException(ICodeException.SECURITY_AUTHORISATION_EC, ex);
    	}
    	logger.info("authorize END");
    }

    /**
     * Adds Granted Authorities to User
     * @author dan.damian 
     */
    private GrantedAuthority[] getAuthorities(List<Permission> permissions){
    	logger.info("getAuthorities START");
    	GrantedAuthority[] authorities = null;
    	if (permissions != null) {
	    	authorities = new GrantedAuthority[permissions.size()];
	    	for(int i = 0; i < permissions.size(); i++) {
	    		authorities[i] =  new SimpleGrantedAuthority(permissions.get(i).getName());
	    		logger.debug("AUTHORITY: " + authorities[i]);
	    	}
    	} else {
    		logger.debug("This user has no Permissions !");
    	}
    	logger.info("getAuthorities END");
    	return authorities;
    }
    
    /**
     * @author coni
     * @param personId
     * Verifies if the user is AdminIT
     */
    public boolean isUserAdminIT(int personId){
    	logger.debug("isUserAdminIT START");
    	boolean isAdminIT = false;
    	try{
			//Setting Modules to which this user has access to
			if (authorizationDao.isUserAdminIT(personId)) {
				logger.debug("User is ADMIN IT");
				isAdminIT = true;
			} else {
				logger.debug("User is not ADMIN IT");
			}
    	} catch(Exception ex) {
			logger.error("", ex);
		}
		logger.debug("isUserAdminIT END");
		return isAdminIT;
    }

}
