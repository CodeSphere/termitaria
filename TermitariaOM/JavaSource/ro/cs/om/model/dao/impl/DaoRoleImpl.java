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
package ro.cs.om.model.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.business.BLPermission;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Localization;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchRoleBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoLocalization;
import ro.cs.om.model.dao.IDaoPermission;
import ro.cs.om.model.dao.IDaoRole;
import ro.cs.om.web.entity.RoleWeb;
import ro.cs.tools.Tools;

/**
 * @author matti_joona
 *
 * This class implements all the model methods specifics for a role
 */
public class DaoRoleImpl extends HibernateDaoSupport implements IDaoRole{
	
	private static IDaoLocalization localizationDao = DaoBeanFactory.getInstance().getDaoLocalization();
	private static IDaoPermission permissionDao = DaoBeanFactory.getInstance().getDaoPermission();
	
	/**
	 * Adds given role
	 * 
	 * @author alu
	 * @throws BusinessException 
	 */	
	public void add(RoleWeb roleWeb)  {
		logger.debug("Add role");
					
		int moduleId = roleWeb.getModuleId();
		logger.debug("add moduleId = " + moduleId);
		try{
			Permission permission = BLPermission.getInstance().getDefaultPermissionByModule(moduleId);
			int permissionId = permission.getPermissionId();						
			Set<Integer> permissions = roleWeb.getPermissions();
			
			if(permissions != null){
				logger.debug("add permissionId !=  null");
				permissions.add(new Integer(permissionId));
				roleWeb.setPermissions(permissions);
			} else {
				logger.debug("add permissionId ==  null");
				Set<Integer> newPermissions = new HashSet<Integer>();
				newPermissions.add(new Integer(permissionId));
				roleWeb.setPermissions(newPermissions);
			}
			
		}catch (BusinessException be){
			be.getStackTrace();
		}	
		
		//set normal status for the role
		roleWeb.setStatus((int)IConstant.ROLE_STATUS_NORMAL);
		getHibernateTemplate().save(IModelConstant.roleWebEntity, roleWeb);
				
		logger.debug("Role added".concat(" with id=").concat(String.valueOf(roleWeb.getRoleId())));
	}
	
	/**
	 * Get a role
	 * 
	 * @author matti_joona
	 */
	public Role get(Integer roleId){
		logger.debug("Getting role with id=".concat(String.valueOf(roleId)));
		Role role = (Role) getHibernateTemplate().get(IModelConstant.roleEntity, new Integer(roleId));
		return role;
	}
	
	/**
	 * Return a role with permissions only(without module and organisation)
	 * 
	 * @author alu
	 */
	public Role getWithPermissions(Integer roleId){
		logger.debug("Getting role with id=".concat(String.valueOf(roleId)));
		Role role = (Role) getHibernateTemplate().get(IModelConstant.roleWithPermissionsEntity, roleId);
		return role;
	}
	
	/**
	 * Returns a role with permissions, without the default permissions for the module
	 * 
	 * @author Adelina
	 */
	//public Role getPermissionsWithoutDefault(Integer roleId,)
	
	
	
	/**
	 * Return a role with all components
	 * 
	 * @author alu
	 */
	public Role getAll(Integer roleId){
		logger.debug("Getting role with id=".concat(String.valueOf(roleId)));
		Role role = (Role) getHibernateTemplate().get(IModelConstant.roleAllEntity, roleId);
		return role;
	}
	
	/**
	 * Returns a role in RoleWeb format
	 * 
	 * @author alu
	 */
	public RoleWeb getRoleWeb(Integer roleId){
		logger.debug("Getting role with id=".concat(String.valueOf(roleId)));
		RoleWeb rw = (RoleWeb) getHibernateTemplate().get(IModelConstant.roleWebEntity, roleId);
		return rw;
	}
	
	/**
	 * Get a role with associated Module
	 * 
	 * @author dd
	 */
	public Role getWithModule(Integer roleId){
		logger.debug("Getting role with id=".concat(String.valueOf(roleId)));
		Role role = (Role) getHibernateTemplate().get(IModelConstant.roleWithModuleEntity, new Integer(roleId));
		return role;
	}
	
	/**
	 * Get a role by name
	 * 
	 * @author matti_joona
	 */
	public Role get(String name){
		logger.debug("Getting role by name...".concat(name));
		String hquery = "from Role where name='".concat(name).concat("'");
		List<Role> listRoles = getHibernateTemplate().find(hquery);
		if (listRoles != null && listRoles.size() > 0) {
			return listRoles.get(0);	
		} else {
			return null;
		}
		
	}
	
	/**
	 * Searches for roles after criterion from searchRoleBean
	 * @author alu
	 * @return A list of role beans 
	 * @throws ParseException 
	 */
	public List<Role> getRoleBeanFromSearch(SearchRoleBean searchRoleBean, boolean isDeleteAction, Set<Integer> modulesIds) throws ParseException{
		
		logger.debug("getRoleBeanFromSearch - START - name:".concat(searchRoleBean.getName()).concat(" moduleId - ").concat(String.valueOf(searchRoleBean.getModuleId())).concat(" orgId - ").concat(String.valueOf(searchRoleBean.getOrganisationId())));
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
		
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.roleForListingEntity);		
				
		if (searchRoleBean.getName() != null && !"".equals(searchRoleBean.getName())){
			dc.add(Restrictions.ilike("name", "%".concat(searchRoleBean.getName()).concat("%")));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchRoleBean.getName()).concat("%")));			
		}
		if (searchRoleBean.getModuleId() != -1){
			dc.add(Restrictions.eq("module.moduleId", searchRoleBean.getModuleId()));
			dcCount.add(Restrictions.eq("module.moduleId", searchRoleBean.getModuleId()));			
		} else {
				dc.add(Restrictions.in("module.moduleId", modulesIds));
				dcCount.add(Restrictions.in("module.moduleId", modulesIds));
		}
		if (searchRoleBean.getOrganisationId() != -1){
			dc.add(Restrictions.eq("organisation.organisationId", searchRoleBean.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisation.organisationId", searchRoleBean.getOrganisationId()));			
		}
		
		// check if I have to order the results
		if(searchRoleBean.getSortParam() != null && !"".equals(searchRoleBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchRoleBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchRoleBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchRoleBean.getSortParam()));
			}
		}
				
	
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchRoleBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if ( searchRoleBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("roleId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchRoleBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchRoleBean.getResultsPerPage() == 0) {
				searchRoleBean.setNbrOfPages(nbrOfResults / searchRoleBean.getResultsPerPage());
			} else {
				searchRoleBean.setNbrOfPages(nbrOfResults / searchRoleBean.getResultsPerPage() + 1);
			}
			// after a role is deleted, the same page has to be displayed;
			//only when all the roles from last page are deleted, the previous page will be shown 
			if ( isDeleteAction && (searchRoleBean.getCurrentPage() > searchRoleBean.getNbrOfPages()) ){
				searchRoleBean.setCurrentPage( searchRoleBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchRoleBean.setCurrentPage(1);
			}

		}
		
		List<Role> res = (List<Role>)getHibernateTemplate().findByCriteria(dc, (searchRoleBean.getCurrentPage()-1) * searchRoleBean.getResultsPerPage(), searchRoleBean.getResultsPerPage());				
		logger.debug("getRoleBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;
	}
		
	/**
	 * Delete role identifed by it's id with all the components
	 * 
	 * Return the Role that has been deleted
	 * @author Adelina
	 */
	
	public Role deleteAll(Integer roleId){
		logger.debug("deleteAll DAO IMPL - START");
		
		logger.debug("Deleting role with id: ".concat(String.valueOf(roleId)));
		Role role = getAll(roleId);
		logger.debug("Deleting the role : " + role);		
		getHibernateTemplate().delete(IModelConstant.roleAllEntity, role);
		logger.debug("Role : " + role + " has been deleted");
	
		logger.debug("deleteAll DAO IMPL - END");
		return role;
		
	}
	
	
	/**
	 * Deletes role identified by it's id
     *
	 * @author alu
	 */
	public void delete(int roleId) {
		logger.debug("delete - START id: ".concat(String.valueOf(roleId)));
		Role role = new Role();
		role.setRoleId(roleId);
		getHibernateTemplate().delete(IModelConstant.roleWithPermissionsEntity, role);
		logger.debug("delete - END");
	}
		
	/**
     * Updates given role
     *
     * @author alu
     * 
     * @param rw
     * @throws BusinessException
     */
    public void updateRoleWeb(RoleWeb roleWeb){
    	logger.debug("update role - START");	
    	    	
		int moduleId = roleWeb.getModuleId();
		logger.debug("add moduleId = " + moduleId);
		try{
			Permission permission = BLPermission.getInstance().getDefaultPermissionByModule(moduleId);
			int permissionId = permission.getPermissionId();						
			Set<Integer> permissions = roleWeb.getPermissions();
			
			if(permissions != null){
				logger.debug("add permissionId !=  null");
				permissions.add(new Integer(permissionId));
				roleWeb.setPermissions(permissions);
			} else {
				logger.debug("add permissionId ==  null");
				Set<Integer> newPermissions = new HashSet<Integer>();
				newPermissions.add(new Integer(permissionId));
				roleWeb.setPermissions(newPermissions);
			}
			
		}catch (BusinessException be){
			be.getStackTrace();
		}					
		
		getHibernateTemplate().update(IModelConstant.roleWebEntity, roleWeb);
		logger.debug("update role - END ".concat(" with id=").concat(String.valueOf(roleWeb.getRoleId())));
    }
    
    /**
     * Updates given role
     *
     * @author alu
     * 
     * @param role
     * @throws BusinessException
     */
    public void update(Role role){
    	logger.debug("update - START");		
		getHibernateTemplate().update(IModelConstant.roleEntity, role);
		logger.debug("update - END");
    }

    /**
     * Gets the default roles (those that have status = 2)
     * @author Adelina
     * @return List<RoleWeb>
     */
    @SuppressWarnings("unchecked")
	public List<RoleWeb> getDefaultRolesByModule(Integer moduleId){
    	logger.debug("getDefaultRoles DAO IMPL - START - ");		
    	    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleWebEntity);		
    	dc.add(Restrictions.eq("status", (int)IConstant.ROLE_STATUS_DEFAULT));
    	dc.add(Restrictions.eq("moduleId", moduleId));
		List roles = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getDefaultRoles DAO IMPL - END - ".concat(String.valueOf(roles.size())));
		
		return roles;	
    }       
        
    /**
     * Returns a default Role for a Module identified by it's name.
     * 
     * @author dan.damian
     */
    public RoleWeb getDefaultRoleByModule(Integer moduleId, String name){
    	logger.debug("getDefaultRoleByModule START moduleId: ".concat(String.valueOf(moduleId)).
    			concat(", name: ").concat(name));		
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleWebEntity);		
    	dc.add(Restrictions.eq("status", (int)IConstant.ROLE_STATUS_DEFAULT));
    	dc.add(Restrictions.eq("moduleId", moduleId));
    	dc.add(Restrictions.eq("name", name));
		List<RoleWeb> roles = getHibernateTemplate().findByCriteria(dc);
 		logger.debug("getDefaultRole END ".concat(String.valueOf(roles.size())));

 		if (roles.size() > 0) {
			return roles.get(0);
		} else {
			return null;
		}
		
    }
    
    /**
     * Add default roles to a given Organization, by an organisationId
     * @author Adelina
     * @param organisationId
     */
    public void addDefaultRoles(Integer organisationId, Integer moduleId){
    	logger.debug("Add default roles DAO IMPL - START- ");
    	getHibernateTemplate().setAllowCreate(false);
    	List<RoleWeb> roles = getDefaultRolesByModule(moduleId); 
    	RoleWeb roleForAdd = null;
    	for(RoleWeb role:roles){
    		
    		roleForAdd = new RoleWeb();
    		roleForAdd.setName(role.getName());
    		
    		// Replicate the permissions
    		Set <Integer> permissions = role.getPermissions();
    		Set <Integer>newPermissions = new HashSet<Integer>();
    		for(Integer permission : permissions) {
    			newPermissions.add(new Integer(permission));
    		}
    		roleForAdd.setPermissions(newPermissions);
    		
    		roleForAdd.setOrganisationId(organisationId);
    		roleForAdd.setModuleId(moduleId);  
    		
    		// Replicate the description
    		Localization description = new Localization();
        	description.setLocalizationId(0);
        	description.setEn(role.getDescription().getEn());
        	description.setRo(role.getDescription().getRo());    	
    		logger.debug("description role = " + role.getDescription());
    		getHibernateTemplate().save(IModelConstant.localizationEntity, description); 
    		roleForAdd.setDescription(description);
    		roleForAdd.setStatus(IConstant.ROLE_STATUS_NORMAL);    		    		
    		logger.debug("Role " + role);
    		
    		getHibernateTemplate().save(IModelConstant.roleWebEntity, roleForAdd);
    		logger.debug("�fter save " + role);
    	
    	}
    	getHibernateTemplate().setAllowCreate(true);
    	logger.debug("Add default roles DAO IMPL - END- ");
    }
    
    /**
     * same thing as addDefaultRoles(Integer organisationId, Integer moduleId) but using the same session
     */
    public void addDefaultRoles(Integer organisationId, Integer moduleId, Session session){
    	logger.debug("Add default roles DAO IMPL - START- ");
    	List<RoleWeb> roles = getDefaultRolesByModule(moduleId);
    	for(RoleWeb role:roles){    		
    		role.setOrganisationId(organisationId);
    		role.setModuleId(moduleId);    	
    		Localization description = new Localization();
        	description.setLocalizationId(0);
        	description.setEn(role.getDescription().getEn());
        	description.setRo(role.getDescription().getRo());    	
    		logger.debug("description role = " + role.getDescription());
    		session.save(IModelConstant.localizationEntity, description);
    		role.setDescription(description);
    		//session.evict(IModelConstant.roleWebEntity);
    		//logger.debug("After evict " + role);
    		role.setRoleId(0);
    		logger.debug("Role " + role);
    		session.replicate(IModelConstant.roleWebEntity, role, ReplicationMode.IGNORE);
    		logger.debug("�fter save " + role);
    	}
    	logger.debug("Add default roles DAO IMPL - END- ");
    }
    
    
    /**
     * Get roles by organisation and module
     * @author Adelina
     * @param organisationId
     * @param moduleId
     * @return List<RoleWeb>
     */

	@SuppressWarnings("unchecked")
	public List<RoleWeb> getRolesByModuleAndOrg(Integer organisationId, Integer moduleId){
		
    	logger.debug("getRolesByModuleAndOrg DAO IMPL - START - ");		
    	    	
    	logger.debug("organisationId = " + organisationId);
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleWebEntity);		
    	dc.add(Restrictions.eq("organisationId", organisationId));
    	dc.add(Restrictions.eq("moduleId", moduleId));
		List roles = getHibernateTemplate().findByCriteria(dc);
	
		logger.debug("getRolesByModuleAndOrg DAO IMPL - END - ".concat(String.valueOf(roles.size())));
		
		return roles;	
    }  
	/**
	 * 
	 * Return the role with the given name in the organisation if present
	 * @author mitziuro
	 * @param organisationId
	 * @param name
	 * @return
	 */
	public Role getRoleByNameAndOrg(Integer organisationId, String name) {
		logger.debug("getRoleByNameAndOrg DAO IMPL - START - ");		
		logger.debug("name = ".concat(name));
    	logger.debug("organisationId = ".concat(String.valueOf(organisationId)));
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleForListingEntity);		
    	dc.add(Restrictions.eq("name", name));
    	dc.add(Restrictions.eq("organisation.organisationId", organisationId));
		List<Role> roles = getHibernateTemplate().findByCriteria(dc);
    	
		/*String hquery = "from Role where name='".concat(name).concat("'").concat(" and organisationId=").concat(String.valueOf(organisationId));
		List<Role> roles = getHibernateTemplate().find(IModelConstant.roleForListingEntity, hquery);*/
	
		if(roles.size()>0) {
			return (Role) roles.get(0);
		}
		return null;				
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
	public List<Role> getRolesByNamesAndOrg(Integer organisationId, int count, String... name) {
		
		logger.debug("getRoleByNameAndOrg DAO IMPL - START - ");	
		for(int i = 0; i < count; i++) {
			logger.debug("name = ".concat(name[i]));
		}
    	logger.debug("organisationId = ".concat(String.valueOf(organisationId)));
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleForListingEntity);

    	Disjunction disjunction = Restrictions.disjunction();
		for(int i = 0; i < count; i++) {		    
		    disjunction.add(Restrictions.eq("name", name[i]));
		}
		dc.add(disjunction);
    	dc.add(Restrictions.eq("organisation.organisationId", organisationId));
		List<Role> roles = getHibernateTemplate().findByCriteria(dc);
    				
		return roles;				
	}	
	
	
    /**
     * Get roles by organisation and module
     * @author coni
     * @param organisationId
     * @param moduleId
     * @return List<Role>
     */

	public List<Role> getRolesByModuleAndOrganisation(Integer organisationId, Integer moduleId, Person person){
		
    	logger.debug("getRolesByModuleAndOrganisation DAO IMPL - START - ");		
    	    	
    	logger.debug("organisationId = " + organisationId);
    	logger.debug("moduleId = " + moduleId);
    	
    	Set<Role> roles = person.getRoles();
    	Integer[] roleIds = new Integer[roles.size()];
    	int i = 0;
    	for(Role role : roles) {
    		roleIds[i] = role.getRoleId();
    		i++;
    	}
    	     	    		  
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleForListingEntity);		
    	dc.add(Restrictions.eq("organisation.organisationId", organisationId));
    	dc.add(Restrictions.eq("module.moduleId", moduleId));
    	dc.add(Restrictions.not( Restrictions.in("roleId", roleIds)));
		List<Role> res = getHibernateTemplate().findByCriteria(dc);
	
		logger.debug("getRolesByModuleAndOrganisation DAO IMPL - END - ".concat(String.valueOf(roles.size())));
		
		return res;	
    }  	
	
	/**
	 * 
	 * Delete all the roles for an organisation
	 * @author mitziuro
	 *
	 * @param organisationId
	 */
	public void deleteByOrganisationId(int organisationId){
		logger.debug("DaoRoleImpl - deleteByOrganisationId - START ORG: ".concat(String.valueOf(organisationId)));
		
		String hquery = "from ".concat(IModelConstant.roleForDeleteEntity).concat(" where organisation.organisationId=").concat(Integer.toString(organisationId));
		
		List<Role> roles = getHibernateTemplate().find(hquery);
		for(Role role : roles){
			getHibernateTemplate().delete(role);
		}
		
		logger.debug("DaoRoleImpl - deleteByOrganisationId - END");
	}
	
	/**
	 * Checks if a role has a associated person
	 * 
	 * @author Adelina
	 * 
	 * @param roleId
	 * @return boolean
	 */
	public boolean hasRoleAssociatePerson(Integer roleId){
		
		logger.debug("DaoRoleImpl - hasRoleAssociatePerson - START -");
		
		StringBuffer hql = new StringBuffer("select distinct r from ");
		hql.append(IModelConstant.roleAllEntity);
		hql.append(" as r inner join r.persons as p where r.roleId=");
		hql.append(roleId);
		
		logger.debug(hql.toString());
		
		List<Role> roles = getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, roles);
		
		if(roles != null && roles.size() > 0) {
			return true;
		} 
		
		logger.debug("DaoRoleImpl - hasRoleAssociatePerson - END -");
		return false;
	}
	
	/**
     * Gets the default roles (those that have status = 2)
     * 
     * @author Adelina
     * 
     * @return List<Role>
     */
    @SuppressWarnings("unchecked")
	public List<Role> getDefaultRoles(Integer moduleId){
    	logger.debug("getDefaultRoles DAO IMPL - START - ");		
    	    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleForListingEntity);		
    	dc.add(Restrictions.eq("status", (int)IConstant.ROLE_STATUS_DEFAULT));
    	dc.add(Restrictions.eq("module.moduleId", moduleId));
		List roles = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getDefaultRoles DAO IMPL - END - ".concat(String.valueOf(roles.size())));
		
		return roles;	
    }
    
 
    /**
     *   
     * Add the default roles for a given module, to an organization,
     * when that organization updates the modules
     * 
     * @author Adelina
     * 
     * @param moduleId
     * @param organisationId
     */
    @SuppressWarnings("unchecked")
	public void addDefaultRolesToOrganisation(Integer moduleId, Integer organisationId){
    	
    	logger.debug("getDefaultRolesToOrganisation - START - ");		
    	    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleForListingEntity);		
    	dc.add(Restrictions.ne("status", (int)IConstant.ROLE_STATUS_DEFAULT));
    	dc.add(Restrictions.eq("organisation.organisationId", organisationId));
    	dc.add(Restrictions.eq("module.moduleId", moduleId));         	
    	    		
    	List<Role> roles = getHibernateTemplate().findByCriteria(dc);
    	
    	if(roles != null && roles.size() == 0) {
    		addDefaultRoles(organisationId, moduleId);
    	}
        
		logger.debug("getDefaultRolesToOrganisation- ".concat(String.valueOf(roles.size())));
		
    }
    
    /**
     * Gets the list of persons that has a specific permission
     * 
     * @author Adelina
     * 
     * @param permissionName
     * @return
     */
    public Set<Person> getPersonsByRole(String permissionName, Integer organizationId) {
    	    
    	logger.debug("getPersonsByRole - START");
    	
    	Set<Person> persons = new HashSet<Person>();
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.roleAllEntity);
    	dc.add(Restrictions.eq("name", IConstant.TS_USER_ALL));
    	dc.add(Restrictions.eq("module.moduleId", IConstant.TS_MODULE_ID));
    	dc.add(Restrictions.eq("organisation.organisationId", organizationId));
    	
    	Permission permission = permissionDao.getByName(permissionName);
    	logger.debug("permission = " + permission);
    	
    	if(permission == null) {
    		return new HashSet<Person>();
    	}
    	
    	dc.createCriteria("permissions").add(Restrictions.eq("permissionId", permission.getPermissionId()));
    	    	
    	List<Role> roles = getHibernateTemplate().findByCriteria(dc);
    	logger.debug("roles = " + roles);
    	if(roles != null && roles.size() > 0) {
    		for(Role role : roles) {
    			logger.debug("role = " + role);
    			Set<Person> rolesPersons = role.getPersons();
    			logger.debug("rolesPersons = " + rolesPersons);
    			if(rolesPersons != null && rolesPersons.size() > 0) {
    				for(Person person : rolesPersons) {
    					persons.add(person);
    				}
    			}
    		}
    	} else {
    		logger.debug("getPersonBYRole - END");
    		return new HashSet<Person>();
    	}
    	
    	logger.debug("getPersonBYRole - END");
    	return persons;    	
    	    
    }    
}
