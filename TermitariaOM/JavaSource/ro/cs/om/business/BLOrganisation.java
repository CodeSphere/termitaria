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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cs.om.common.ConfigParametersProvider;
import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Calendar;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchOrganisationBean;
import ro.cs.om.entity.Setting;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.model.dao.IDaoRole;




/**
 * Singleton which expose business methods for Organisation item
 *
 * @author matti_joona
 */
public class BLOrganisation extends BusinessLogic {
	
    private static BLOrganisation theInstance = null;
    private static IDaoOrganisation orgDao = DaoBeanFactory.getInstance().getDaoOrganisation();
    private static IDaoRole roleDao = DaoBeanFactory.getInstance().getDaoRole();
    
    //singleton implementation
    private BLOrganisation(){};
    static {
        theInstance = new BLOrganisation();
    }
    public static BLOrganisation getInstance() {
        return theInstance;
    }

    /**
     * Add an organisation to the system. Also must create all the default configuration
     * 
     * @author matti_joona
     */
    public void add(Organisation org) throws BusinessException{
    	logger.debug("Organisation add - START");
    	
    	try{  	
    	 		
    		Calendar calendar = new Calendar();   
    		Department dept = new Department();
    		List<Setting> listSettings = new ArrayList<Setting>();
    		Setting setting = new Setting();    		
    		     
    		if(org.getParentId() <= 0){    	
    			//add a default calendar for organisation
		    	calendar.setStartWork(ConfigParametersProvider.getString("business", "default.calendar.startWork"));
		    	calendar.setEndWork(ConfigParametersProvider.getString("business", "default.calendar.endWork"));		    	
		   
		    	listSettings = BLSetting.getInstance().getDefaultSettings();
    		} else {
    			Organisation parent = BLOrganisation.getInstance().get(org.getParentId());
    			int parentId = parent.getOrganisationId();
    			// add the calendar of the parent
    			calendar = BLCalendar.getInstance().getCalendarForOrganisation(parentId);
    			logger.debug("calendar = " + calendar);
    			// add the settings of the parent
    			listSettings = BLSetting.getInstance().getByOrganisationId(parentId);    		    			
    		}
    		
    		//create a fake department and set the common info
	    	dept.setName(ConfigParametersProvider.getString("business", "default.department.fake.name"));
	    	dept.setStatus(IConstant.NOM_DEPARTMENT_FAKE);
	    	dept.setParentDepartmentId(-1);
	    	
	    	//create the default user group for the new organisation
			UserGroup defaultUserGroup = new UserGroup();
			defaultUserGroup.setName(IConstant.DEFAULT_USER_GROUP_NAME);
			defaultUserGroup.setStatus(IConstant.NOM_USER_GROUP_DEFAULT);
			defaultUserGroup.setOrganisation(org);
			
		   	//add the organisation to the database
    		orgDao.addOrganisationWithAll(org, calendar, dept, listSettings, defaultUserGroup);
    	    		
    		} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_ADD, bexc);
    	}
    	
    	logger.debug("Organisation add - END ID=".concat(String.valueOf(org.getOrganisationId())));
    }
     
    /**
     * Update an organization, just general info without calendar, into the database.
     * 
     * @author matti_joona
     */
    public void update(Organisation org) throws BusinessException{
    	logger.debug("update - START - organisationId = ".concat(String.valueOf(org.getOrganisationId())));
    	
    	try{
        	orgDao.update(org);    		
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_ADD, bexc);
    	}
    	
    	logger.debug("update - END");
    }
    
    /**
     * Update an organisation with all the infos
     * 
     * @author matti_joona
     */
    public void updateAll(Organisation org) throws BusinessException{
    	logger.debug("updateAll - START - organisationId = ".concat(String.valueOf(org.getOrganisationId())));
    	
    	try{
        	orgDao.updateAll(org);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_ADD, bexc);
    	}
    	
    	logger.debug("updateAll - END");
    }
    
	/**
	 * Deletes an organization and all its children, returning a map with their names and IDs
	 * @author coni
	 */
	public Map<Integer, String> deleteOrgs(Integer organisationId) throws BusinessException{
		logger.debug("deleteOrgs - START - organisationId = ".concat(String.valueOf(organisationId)));
		
		Map<Integer, String> map = null; 
    	try{
    		
    		//first must obtain the organization
    		Organisation org = orgDao.getForDelete(organisationId);
    		if(org != null){
    			map = orgDao.deleteOrgs(org);
    		}
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_DEL, bexc);
    	}
    	
    	//TODO Resurrect when ready
    	/*
    	try{
    		
    		List<Integer> allBranches = orgDao.getAllBranchIdsForOrganisation(organisationId);
    		
    		//Delete the DM Workspaces
    		DMEWebServiceClient.getInstance().deleteWorkspaces(allBranches);
    	}catch (Exception e) {
    		throw new BusinessException(ICodeException.ORG_DEL_OTHER_MODULE, e);
		}
    	*/
    	logger.debug("deleteOrgs - END");
    	return map;
	}
    
    /**
     * Get an organisation by 'id' from the database, only the common attributes
     * 
     * @author matti_joona
     */
    public Organisation get(Integer organisationId) throws BusinessException{
    	logger.debug("get - START - organisationId = ".concat(String.valueOf(organisationId)));
    	
    	Organisation orgResult = null;
    	try{
    		orgResult = orgDao.get(organisationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GET, bexc);
    	}
    	
    	logger.debug("get - END");
    	return orgResult;
    }
    
    
    /**
     * Get an organisation by 'id' from the database. Return only the general info about organisation
     * without any other related elements
     * 
     * @author matti_joona
     */
    public Organisation getAll(Integer idOrganisation) throws BusinessException{
    	logger.debug("getAll - START - organisationId = ".concat(String.valueOf(idOrganisation)));
    	
    	Organisation orgResult = null;
    	try{
    		orgResult = orgDao.getAll(idOrganisation);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GET, bexc);
    	}
    	
    	logger.debug("getAll - END");
    	return orgResult;
    }
    
    /**
	 * Returns all the organisations for classified lists
	 * 
	 * @author alu
	 */
	public List<Organisation> getAllOrganisationsForNom() throws BusinessException{
		logger.debug("getAllOrganisationsForNom - START");
		
		List<Organisation> orgs = null;
		
		try {
			orgs = orgDao.getAllOrganisationsForNom();    		
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.ORG_GET_FOR_NOM, e);
    	}
    	
    	logger.debug("getAllOrganisationsForNom - END - size:".concat(orgs != null? String.valueOf(orgs.size()) : ""));
    	return orgs;
	}
	
    /**
	 * Returns all the organisations for the hasAudit context map
	 * 
	 * @author coni
	 */
	public List<Organisation> getAllOrganisationsWithModulesForNom() throws BusinessException{
		logger.debug("getAllOrganisationsWithModulesForNom - START");
		
		List<Organisation> orgs = null;
		
		try {
			orgs = orgDao.getAllOrganisationsWithModulesForNom();    		
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.ORG_GET_WITH_MODULE_FOR_NOM, e);
    	}
    	
    	logger.debug("getAllOrganisationsWithModulesForNom - END - size:".concat(orgs != null? String.valueOf(orgs.size()) : ""));
    	return orgs;
	}
	
	/**
	 * Get the list of Organisation that has not a calendar defined
	 * 
	 * @author Adelina	
	 * @return List<Organisation>
	 */
	public List<Organisation> getAllOrganisationsForNomWithoutCalendar() throws BusinessException{
		logger.debug("getAllOrganisationsForNomWithoutCalendar - START");
		
		List<Organisation> organisations = null;		
		try{			
			organisations = orgDao.getAllOrganisationsForNomWithoutCalendar();
		}catch(Exception e){
			throw new BusinessException(ICodeException.ORG_GET_FOR_NOM_WITHOUT_CALENDAR, e);
		}
		
		logger.debug("getAllOrganisationsForNomWithoutCalendar - END");
		return organisations;
	}
    
    /**
     * List all departments of an organisation
     * 
     * @author matti_joona
     */
	public Set<Department> getDepartments(Integer organisationId) throws BusinessException{
		logger.debug("getDepartments - START - organisationId = ".concat(String.valueOf(organisationId)));
		
		Set<Department> depts = null;
    	try{
        	depts = orgDao.getWithDepartments(organisationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GETDEPTS, bexc);
    	}
    	
    	logger.debug("getDepartments - END");
    	return depts;
    }
    
	
    /**
     * Get default department for an organisation
     * 
     * @author matti_joona
     */
    public Department getDefaultDepartment(Integer organisationId) throws BusinessException{
    	logger.debug("getDefaultDepartment - START - organisationId = ".concat(String.valueOf(organisationId)));
    	
    	Department department = null;
    	try{
    		department = orgDao.getDefaultDepartment(organisationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GET_DEFAULT_DEPT, bexc);
    	}
    	
    	logger.debug("getDefaultDepartment - END");
    	return department;
    }
	
	
    /**
     * Get no. of departments for an organisation
     * 
     * @author matti_joona
     */
    public int getNumberOfDepartments(Integer organisationId) throws BusinessException{
    	logger.debug("getNumberOfDepartments - START - organisationId = ".concat(String.valueOf(organisationId)));
    	
    	int resultNoOfDepts = 0;
    	try{
    		resultNoOfDepts = orgDao.getNoOfDepartments(organisationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GETNODEPTS, bexc);
    	}
    	
    	logger.debug("getNumberOfDepartments - END");
    	return resultNoOfDepts;
    }
    
    /**
     * Get roles of an organisation
     * 
     * @author matti_joona
     */
    public Set<Role> getRoles(Integer organisationId) throws BusinessException{
    	logger.debug("getRoles - START - organisationId = ".concat(String.valueOf(organisationId)));
    	
    	Set<Role> listRoles = null;
    	try{
        	listRoles = orgDao.getWithRoles(organisationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GETROLES, bexc);
    	}
    	
    	logger.debug("getRoles - END");
    	return listRoles;
    }
    
    /**
     * List persons from an organisation
     * 
     * @author matti_joona
     */
    public Set<Person> getPersons(Integer organisationId) throws BusinessException{
    	logger.debug("getPersons - START - organisationId = ".concat(String.valueOf(organisationId)));
    	
    	Set<Person> persons = null;
    	try{
        	 persons = orgDao.getWithPersons(organisationId);
    	} catch(Exception exc){
    		throw new BusinessException(ICodeException.ORG_GETPERSONS, exc);
    	}
    	
    	logger.debug("getPersons - END");
    	return persons;
    }
    
    /**
     * List settings for an organization
     * 
     * @author matti_joona
     */
    public Set<Setting> getSettings(Integer organisationId) throws BusinessException{
    	logger.debug("getSettings - START - organisationId = ".concat(String.valueOf(organisationId)));
    	
    	Set<Setting> listSettings = null;
    	try{
    		listSettings = orgDao.getWithSettings(organisationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GETSETTINGS, bexc);
    	}    
    	
    	logger.debug("getSettings - END");
    	return listSettings;
    }
    
    /**
     * Returns the modules for an organization
     * 
     * @author alu
     */
    public Set<Module> getModules(Integer organisationId) throws BusinessException{
    	logger.debug("getModules - START - id:".concat(organisationId.toString()));
		
    	Set<Module> modules = null;
    	try{
    		modules = orgDao.getWithModules(organisationId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.ORG_GET_MODULES, e);
    	}
    	
    	logger.debug("getModules - END");
    	return modules;
    }
    
    /**
	 * Returns an organisation with credentials and modules
	 * 
	 * @author Adelina
     * @throws BusinessException 
	 */
	
    public Organisation getOrganisationWithModules(Integer organisationId) throws BusinessException{
    	logger.debug("getOrganisationWithModules - START - id:".concat(organisationId.toString()));
    	
    	Organisation organisation = null;
    	try{
    		organisation = orgDao.getOrganisationWithModules(organisationId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.ORG_WITH_MODULES, e);
    	}
    	
    	logger.debug("getOrganisationWithModules - END");
    	return organisation;
    }
    
	/*public Organisation getOrganisationWithModules(Integer organisationId){
		logger.debug("getOrganisationWithModules - START - id:".concat(organisationId.toString()));
		Organisation organisation = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithModulesEntity, new Integer(organisationId));
		logger.debug("getOrganisationWithModules - END");
		return organisation;
	}*/

    /**
     * Returns all the organisations from the database
     * 
     * @author dd
     */
    public List<Organisation> list() throws BusinessException{
    	logger.debug("list - START");
    	
    	List<Organisation> organisations = null;
    	try{
    		organisations = orgDao.list();
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_LIST, bexc);
    	}    
    	
    	logger.debug("list - START");
    	return organisations;
    }
    
   /**
    * Searches for Organizations after criterion from searchOrganisationBean
    * 
    * @author alu
    * @author dan.damian
    * @author Adelina 
    * 
    * @param searchOrganisationBean - Bean that contains the search criterion
    * @param isChanged
    * @param typeIds
    * @return A list of Organizations beans
    * @throws BusinessException
    */
      
    public List<Organisation> getFromSearch(SearchOrganisationBean searchOrganisationBean, boolean isChanged, Set<Byte> typeIds) throws BusinessException{
    	logger.debug("getFromSearch - START");
    	
    	List<Organisation> res = null;
    	try {
    		res = orgDao.getFromSearch(searchOrganisationBean, isChanged, typeIds);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.ORG_SEARCH, e);
    	}
    	
    	logger.debug("getFromSearch - END");
    	return res;
    }
    
    /**
	 * Changes the status for the organisation.
	 * 	 
	 * @param organisationId 
	 * @author Adelina
     * @throws BusinessException 
	 */
	
	public void updateStatus(Integer organisationId) throws BusinessException{
		logger.debug("updateStatus - START id: ".concat(String.valueOf(organisationId)));
		
		List<Person> persons = null;
		try{
			persons = BLPerson.getInstance().getPersonsByOrganizationId(organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_GET_BY_ORGANIZATION, e);
		}
		
	    try{
	    	orgDao.updateStatus(organisationId, persons);
	    } catch(Exception e){
	    	throw new BusinessException(ICodeException.ORG_UPDATE, e);
	    }
		
	    logger.debug("updateStatus - END");
	}
	/**
	 * 
	 * Get an organisation by her name
	 * @author mitziuro
	 * @param name
	 * @return
	 * @throws BusinessException
	 */
	public Organisation getOrgByName(String name) throws BusinessException{
		logger.debug("getOrgByName UniqueOrgByName - START for organisation ".concat(name));
    	
		Organisation org = null;
		try{
        	org = orgDao.getOrgByName(name);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GET_BY_NAME, bexc);
    	}
    	
    	logger.debug("getOrgByName UniqueOrgByName - END");
    	return org;
    }
	
	/**
	 * 
	 * Get all organisations that have the parentId
	 * @author mitziuro
	 * @param parentId
	 * @return
	 * @throws BusinessException
	 */
	public List<Organisation> getAllOrganisationsForParentId(Integer parentId) throws BusinessException{
		logger.debug("getAllOrganisationsForParentId - START for organisation with id:".concat(String.valueOf(parentId)));
    	
		List<Organisation> orgs = new ArrayList<Organisation>();
		try{
        	orgs = orgDao.getAllSimpleOrganisationsForParentId(parentId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORG_GET_BY_PARENT_ID, bexc);
    	}
    	
    	logger.debug("getAllOrganisationsForParentId  - END SIZE: ".concat(String.valueOf(orgs.size())));
    	return orgs;
    }
    
   	
	public List<Organisation> getAllOrganisationsByParentIdForOrgTree(Integer parentId) throws BusinessException{
		logger.debug("getAllOrganisationsByParentIdForOrgTree - START for organisation with id:".concat(String.valueOf(parentId)));
		
		List<Organisation> list = new ArrayList<Organisation>();
		try {
			list  = orgDao.getAllOrganisationsByParentIdForOrgTree(parentId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.ORG_GET_BY_PARENTID_FOR_TREE, e);
		}
		
		logger.debug("getAllOrganisationsByParentIdForOrgTree - END for organisation with id:".concat(String.valueOf(parentId)));
		return list;
	}
	
	/**
	 * Gets a list with the organisationIds that have the requested module enabled
	 * 
	 * 
	 * @author mitziuro
	 * @param moduleId
	 * @return
	 * @throws BusinessException
	 */
	public List<Integer> getOrganisationIdsByModule(Integer moduleId) throws BusinessException{
		logger.debug("getOrganisationIdsByModule - START for moduleid:".concat(String.valueOf(moduleId)));
		
		List<Integer> list = new ArrayList<Integer>();
		try {
			list  = orgDao.getOrganisationIdsByModule(moduleId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.ORG_GET_BY_MODULE_ID, e);
		}
		
		logger.debug("getOrganisationIdsByModule - END ");
		return list;
	}
	
	/**
	 * Updates the context map that specifies if an organisation has the audit module
	 * @author coni
	 * @author liviu
	 * @param organisationId 
	 * @param modules
	 */
	public void updateHasAuditContextMap(int organisationId, Set<Module> modules) {
		logger.debug("updateHasAuditContextMap - START");
			
		try {
			Boolean hasAuditModule = false;
			Iterator<Module> organisationModules = modules.iterator();
			while (organisationModules.hasNext()) {
				if (organisationModules.next().getModuleId() == IConstant.AUDIT_MODULE_ID){
					hasAuditModule = true;
				}
			}
			Map<Integer, Boolean> hasAudit = (Map<Integer, Boolean>) OMContext.getFromContext(IConstant.HAS_AUDIT_CONTEXT_MAP);
			hasAudit.put(organisationId, hasAuditModule);
			
			logger.debug("updateHasAuditContextMap - END");
		} catch( Exception e){
			logger.error("ERROR while updating the audit context map for the organisation with id :".concat(String.valueOf(organisationId)), e);
		}
		
	}
	
	public Boolean hasAuditModule(int organisationId) throws BusinessException {
		logger.debug("hasAuditModule - START");
		Boolean hasAuditModule;
		try {
			hasAuditModule = false;
			Iterator<Module> organisationModules = orgDao.getWithModules(organisationId).iterator();
			while (organisationModules.hasNext()) {
				if (organisationModules.next().getModuleId() == IConstant.AUDIT_MODULE_ID){
					hasAuditModule = true;
				}
			}
		} catch (Exception e){
			throw new BusinessException(ICodeException.ORG_HAS_AUDIT_MODULE, e);
		}
		logger.debug("hasAuditModule - END");
		return hasAuditModule;
	}

}

	
