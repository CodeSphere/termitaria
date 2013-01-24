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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Module;
import ro.cs.om.model.dao.IDaoModule;
import ro.cs.tools.Tools;

/**
 * This class implements all the model methods specifics for a module of the product
 *
 * @author matti_joona
 */
public class DaoModuleImpl extends HibernateDaoSupport implements IDaoModule{

	/**
	 * Add a module
	 * 
	 * @author matti_joona
	 */
	public void add(Module module) {
		logger.debug("Add module");
		getHibernateTemplate().save(IModelConstant.moduleEntity, module);
		logger.debug("module added".concat(" with id=").concat(String.valueOf(module.getModuleId())));
	}
	
	/**
	 * Get a module
	 * 
	 * @author matti_joona
	 */
	public Module get(Integer moduleId){
		logger.debug("Getting module with id=".concat(moduleId.toString()));
		Module module = (Module) getHibernateTemplate().get(IModelConstant.moduleEntity, new Integer(moduleId));
		return module;
	}
	
	/**
	 * Get a module by name
	 * 
	 * @author matti_joona
	 */
	public Module get(String name){
		logger.debug("Getting module with name=".concat(name));
		String hquery = "from Module where name='".concat(name).concat("'");
		List<Module> listModules = getHibernateTemplate().find(hquery);
		if(listModules != null && listModules.size() > 0){
			return listModules.get(0);	
		} else{
			return null;
		}
		
	}

	/**
	 * Delete a list of modules
	 * 
	 * @author matti_joona
	 */
	public void delete(HashSet<Module> modules) {
		logger.debug("Deleting settings!");
		StringBuffer hql = new StringBuffer();
		hql.append("DELETE FROM ");
		hql.append(IModelConstant.settingEntity);
		hql.append(" WHERE organisationId IN (");
		Iterator<Module> it = modules.iterator();
		while(it.hasNext()){
			hql.append(it.next().getModuleId());
			hql.append(",");
		}
		//remove the last "," and add the last ")"
		String hqlQuery = hql.substring(0, hql.length()-1).concat(")");
		//execute query against database
		logger.debug("Executing query: ".concat(hqlQuery));
		Session session = getSessionFactory().openSession();
		Query query = session.createQuery(hqlQuery);
		query.executeUpdate();
		session.close();
		logger.debug("Connectio has been deleted");
	}
	
	/**
	 * Get a module with all it's roles.
	 * 
	 * @author dd
	 */
	public Module getWithRoles(Integer moduleId){
		logger.debug("Getting modules with Roles with id=".concat(moduleId.toString()));
		Module module = (Module) getHibernateTemplate().get(IModelConstant.moduleWithRolesEntity, new Integer(moduleId));
		return module;
	}
	
	/**
	 * Returns all the modules each one with it's roles.
	 * 
	 * @author dd
	 */
	public List<Module> listWithRoles(){
		logger.debug("Returns modules list with Roles");
		return (List<Module>) getHibernateTemplate().find("from ".concat(IModelConstant.moduleWithRolesEntity));
	}
	
	/**
	 * Returns all the modules.
	 * 
	 * @author dd
	 */
	public List<Module> getAllModules() {
		logger.debug("Returns modules list with Roles");
		return (List<Module>) getHibernateTemplate().find("from ".concat(IModelConstant.moduleEntity).concat(" order by moduleId ASC"));
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
	public List<Module> getModulesWithoutOMForOrganisation(Integer organisationId) {
		logger.debug("getModulesWithoutOM - START - organisationId ".concat(String.valueOf(organisationId)));
			
		List<Module> modules = null;
		
		StringBuffer hql = new StringBuffer("select m from ");				
		hql.append(IModelConstant.moduleWithOrganisationsEntity);
		hql.append(" as m where m.name != '").append(IConstant.OM_MODULE).append("'");
		hql.append(" and m not in (select m.moduleId from m as om inner join om.organisations as o where o.organisationId=");				
		hql.append(organisationId).append(")");				
				
		logger.debug(hql.toString());
		modules = (List<Module>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, modules);
		
		logger.debug("getModulesWithoutOM - END -".concat(String.valueOf(modules.size())));	
		return modules;
	}		
			
	/**
	 * Returns all the modules ids for an organisation
	 * @author Coni
	 * @param organisationId
	 * @return
	 */
	public List<Module> listModulesIdsByOrganisation(Integer organisationId){
		logger.debug("listModulesIdsByOrganisation - START - organisationId: ".concat(String.valueOf(organisationId)));
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.moduleWithOrganisationsEntity);
		dc.createCriteria("organisations").add(Restrictions.eq("organisationId", organisationId));
		List<Module> modules = getHibernateTemplate().findByCriteria(dc);
		logger.debug("listModulesIdsByOrganisation - END");
		return modules;
	}
	
	/**
	 * Returns all the modules without the OrganisationManagement Module
	 * hat belongs to the given organisation
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @return List<Module>
	 */
	public List<Module> getModulesForOrganisationWithoutOM(Integer organisationId, Integer parentId) {
		logger.debug("getModulesForOrganisationWithoutOM - START - organisationId ".concat(String.valueOf(organisationId)));
			
		List<Module> modules = null;
		
		StringBuffer hql = new StringBuffer("select m from ");				
		hql.append(IModelConstant.moduleWithOrganisationsEntity);
		hql.append(" as m where m.name != '").append(IConstant.OM_MODULE).append("'");
		hql.append(" and m in (select m.moduleId from m as om inner join om.organisations as o where o.organisationId=");				
		hql.append(parentId).append(")");	
		hql.append(" and m not in (select m.moduleId from m as om inner join om.organisations as o where o.organisationId=");				
		hql.append(organisationId).append(")");	
				
		logger.debug(hql.toString());
		modules = (List<Module>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, modules);
		
		logger.debug("getModulesForOrganisationWithoutOM - END -".concat(String.valueOf(modules.size())));	
		return modules;
	}		
}
