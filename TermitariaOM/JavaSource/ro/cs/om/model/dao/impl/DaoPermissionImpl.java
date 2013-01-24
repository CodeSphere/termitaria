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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.SearchPermissionBean;
import ro.cs.om.model.dao.IDaoPermission;
import ro.cs.tools.Tools;

/**
 * @author matti_joona
 *
 * This class implements all the model methods specifics for permission
 */
public class DaoPermissionImpl extends HibernateDaoSupport implements IDaoPermission{
	
	/**
	 * Add a permission
	 * 
	 * @author matti_joona
	 */
	public void add(Permission permission) {
		logger.debug("Add permission");
		getHibernateTemplate().save(IModelConstant.permissionEntity, permission);
		logger.debug("Permission added");
	}
	
	/**
	 * Get a permission
	 * 
	 * @author matti_joona
	 */
	public Permission get(Integer permissionId){
		logger.debug("Getting permission with id=".concat(String.valueOf(permissionId)));
		Permission permission = (Permission) getHibernateTemplate().get(IModelConstant.permissionEntity, permissionId);
		return permission;
	}
	
	/**
	 * Get a permission by name
	 * 
	 * @author Adelina
	 * 
	 * @param name
	 * @return
	 */
	public Permission getByName(String name) {
		logger.debug("getByName - START".concat(name));
		String hquery = "from Permission where name='".concat(name).concat("'");
		List<Permission> permissions = getHibernateTemplate().find(hquery);
		logger.debug("getByName - END");
		if(permissions != null && permissions.size() > 0) {
			return permissions.get(0);
		} else {
			return null;
		}		
		
	}
	
	/**
	 * Return a Permission with all components
	 * @author Adelina
	 * 
	 * @param permissionId
	 * @return Permission
	 */
	public Permission getAll(Integer permissionId){
		logger.debug("Getting permission with id=".concat(String.valueOf(permissionId)));
		Permission permission = (Permission) getHibernateTemplate().get(IModelConstant.permissionWithRolesEntity, permissionId);
		return permission;
	}

	/**
	 * Returns all the permissions
	 * 
	 * @author alu
	 */
	public List<Permission> getAllPermissions(){
		logger.debug("getAllPermissions - START");
		List<Permission> permissions = getHibernateTemplate().find("from ".concat(IModelConstant.permissionEntity));
		logger.debug("getAllPermissions - END - size:".concat(String.valueOf(permissions.size())));
		return permissions;
	}

	/**
	 * Deletes permission identified by its id
     *
	 * @author alu
	 */
	public void delete(int permissionId) {
		logger.debug("delete - START id: ".concat(String.valueOf(permissionId)));
		Permission perm = new Permission();
		perm.setPermissionId(permissionId);
		getHibernateTemplate().delete(IModelConstant.permissionWithRolesEntity, perm);
		logger.debug("delete - END");
	}
	
	/**
	 * Deletes a permission with all components identified by its id
	 * 
	 * @author Adelina
	 * @param permissionId
	 * @return Permission
	 */
	
	public Permission deleteAll(Integer permissionId){
		logger.debug("deleteAll DAO IMPL - START");
		
		logger.debug("Deleting permission with id: ".concat(String.valueOf(permissionId)));
		Permission permission = getAll(permissionId);
		logger.debug("Deleting the permission : " + permission);
		getHibernateTemplate().delete(IModelConstant.permissionAllEntity, permission);
		logger.debug("Permission : " + permission + " has been deleted");
	
		logger.debug("deleteAll DAO IMPL - END");
		return permission;
		
	}
	
	
	/**
	 * Updates permission
	 *
	 * @author alu
	 * 
	 * @param permission
	 */
	public void update(Permission permission){
		logger.debug("update - START");		
		getHibernateTemplate().update(IModelConstant.permissionEntity, permission);		
		logger.debug("update - END");
	}
	
	/**
	 * Searches for permissions after criterion from searchPermissionBean
	 * @author alu
	 * @return A list of permission beans 
	 * @throws ParseException 
	 */
	public List getPermissionBeanFromSearch(SearchPermissionBean searchPermissionBean, boolean isDeleteAction, boolean isSuper, Set<Integer> modulesIds) throws ParseException{
		
		logger.debug("getPermissionBeanFromSearch - START");

		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.permissionForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.permissionForListingEntity);	
		
		if (searchPermissionBean.getName() != null && !"".equals(searchPermissionBean.getName())){
			dc.add(Restrictions.ilike("name", "%".concat(searchPermissionBean.getName()).concat("%")));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchPermissionBean.getName()).concat("%")));			
		}
		if (searchPermissionBean.getModuleId() != -1){
			dc.add(Restrictions.eq("module.moduleId", searchPermissionBean.getModuleId()));	
			dcCount.add(Restrictions.eq("module.moduleId", searchPermissionBean.getModuleId()));			
		} else {
			dc.add(Restrictions.in("module.moduleId", modulesIds));
			dcCount.add(Restrictions.in("module.moduleId", modulesIds));			
		}
		if(!isSuper){
			dc.add(Restrictions.ne("module.moduleId", IConstant.MODULE_ID_FAKE));
			dcCount.add(Restrictions.ne("module.moduleId", IConstant.MODULE_ID_FAKE));			
		}
		
		// check if I have to order the results
		if(searchPermissionBean.getSortParam() != null && !"".equals(searchPermissionBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchPermissionBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchPermissionBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchPermissionBean.getSortParam()));
			}
		}
		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchPermissionBean.getNbrOfResults() == -1){

			boolean isSearch = false;
			if ( searchPermissionBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction
			dcCount.setProjection(Projections.countDistinct("permissionId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount, 0, 0).get(0)).intValue();
			searchPermissionBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchPermissionBean.getResultsPerPage() == 0) {
				searchPermissionBean.setNbrOfPages(nbrOfResults / searchPermissionBean.getResultsPerPage());
			} else {
				searchPermissionBean.setNbrOfPages(nbrOfResults / searchPermissionBean.getResultsPerPage() + 1);
			}
			// after a permission is deleted, the same page has to be displayed;
			//only when all the permissions from last page are deleted, the previous page will be shown 
			if ( isDeleteAction && (searchPermissionBean.getCurrentPage() > searchPermissionBean.getNbrOfPages()) ){
				searchPermissionBean.setCurrentPage( searchPermissionBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchPermissionBean.setCurrentPage(1);
			}
		}

		List res = getHibernateTemplate().findByCriteria(dc, (searchPermissionBean.getCurrentPage()-1) * searchPermissionBean.getResultsPerPage(), searchPermissionBean.getResultsPerPage());
	
	
		logger.debug("getPermissionBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
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
	 */
	
	@SuppressWarnings("unchecked")
	public List<Permission> getPermissionByModule(Integer moduleId){		
		logger.debug("getPermissionByModule DAO IMPL - START - ");
	
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		PermissionConstant permCt = PermissionConstant.getInstance();
		
		map.put(new Integer(1), permCt.getOM_Basic());
		map.put(new Integer(2), permCt.getDM_Basic());
		
		String permissionName = (String)map.get(new Integer(moduleId));
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.permissionForListingEntity);		
		dc.createCriteria("module").add(Restrictions.eq("moduleId", moduleId));	
		if (permissionName != null) {
			dc.add(Restrictions.ne("name", permissionName));
		}
		List permissions = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getPermissionByModule DAO IMPL - END - ".concat(String.valueOf(permissions.size())));		
		
		return permissions;	

	}
	
	/**
	 * Get the default permission for a module
	 * 
	 * @author Adelina
	 * 
	 * @param moduleId
	 * 
	 * @param permissionName
	 * 
	 * @return the default permission for a module
	 *  
	 */
	public Permission getDefaultPermissionByModule(Integer moduleId){
		logger.debug("getDefaultPermissionByModule DAO IMPL - START - ");
				
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		PermissionConstant permCt = PermissionConstant.getInstance();
		
		map.put(new Integer(1), permCt.getOM_Basic());
		map.put(new Integer(2), permCt.getDM_Basic());
		map.put(new Integer(3), permCt.getAUDIT_Basic());
		map.put(new Integer(4), permCt.getIR_Basic());
		map.put(new Integer(5), permCt.getCM_Basic());
		map.put(new Integer(6), permCt.getTS_Basic());
		map.put(new Integer(7), permCt.getRM_Basic());
		
		String permissionName = (String)map.get(new Integer(moduleId));
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.permissionEntity);		
		dc.add(Restrictions.eq("moduleId", moduleId));	
		dc.add(Restrictions.eq("name", permissionName));
		List permissions = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getDefaultPermissionByModule DAO IMPL - END - ".concat(String.valueOf(permissions.size())));		
		
		return (Permission)permissions.get(0);	
	}
	
	/**
	 * Return a list with the permissions for the given module, without the basic one and
	 * those that are for a given role
	 * 
	 * @author Adelina
	 * 
	 * @param moduleId
	 * @param roleId
	 * @return List<Permission>
	 */
	public List<Permission> getPermissionByModuleAndRole(Integer moduleId, Integer roleId){
		
		logger.debug("getPermissionByModuleAndRole - START - moduleId:".concat(String.valueOf(moduleId)).concat("roleId = ").concat(String.valueOf(roleId)));
		List<Permission> permissions = null;
		
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		PermissionConstant permCt = PermissionConstant.getInstance();
		
		map.put(new Integer(1), permCt.getOM_Basic());
		map.put(new Integer(2), permCt.getDM_Basic());
				
		String permissionName = (String)map.get(new Integer(moduleId));	
		
		StringBuffer hql = new StringBuffer("select p from ");		
		hql.append(IModelConstant.permissionAllEntity);
		hql.append(" as p where p.moduleId=");
		hql.append(moduleId);
		hql.append(" and p.name != '");
		hql.append(permissionName).append("'");
		hql.append(" and p not in (select rp.permissionId from p as rp inner join rp.roles as r where r.roleId=");
		hql.append(roleId).append(")");		
				
		logger.debug(hql.toString());
		permissions = (List<Permission>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, permissions);
						
		logger.debug("getPermissionByModuleAndRole - END - size:".concat(permissions != null? String.valueOf(permissions.size()) : "null"));
		return permissions;
	}
		
}
