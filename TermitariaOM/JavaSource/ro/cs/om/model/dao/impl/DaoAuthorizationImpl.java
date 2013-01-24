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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IModelConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.Setting;
import ro.cs.om.model.dao.IDaoAuthorization;
import ro.cs.om.utils.encryption.EncryptionUtils;

/**
 * Dao class for Authorisation functionalities
 *
 * @author dd
 */
public class DaoAuthorizationImpl extends HibernateDaoSupport implements IDaoAuthorization{


	/**
	 * Finds a person for a given user name and password
	 * 
	 * @author dd
	 * @param username
	 * @param password
	 * @return Person
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws DataAccessException 
	 */
	public Person authentify(String username, String password) throws DataAccessException, NoSuchAlgorithmException, UnsupportedEncodingException {
		logger.debug("authentify");
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.personWithCredentialsEntity);
		hql.append(" where username = :p1 and password = :p2");
		List<Person> persons = (List<Person>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1", "p2"}, new String[] {username, EncryptionUtils.getInstance().getSHA1Hash(password)});
		if (persons.size() == 1) {
			return persons.get(0);
		}else {
			return null;
		}
	}

	
	/**
	 * Returns a list with the Termitaria suite's modules to which the user
	 * has access to.
	 * 
	 * @author dd
	 */
	public List<Module> getAllowedModules(Integer userPersonId) {
		logger.debug("getAllowedModules");
		StringBuffer hql = new StringBuffer("select distinct(m) from ");
		hql.append(IModelConstant.personWithRolesEntity);
		hql.append(" as p inner join p.roles as r ");
		hql.append("inner join r.module as m where p.personId=? order by m.moduleId asc");
		List<Module> modules = (List<Module>)getHibernateTemplate().find(hql.toString(), new Integer(userPersonId));
		return modules;
	}

	/**
	 * Returns a list with all the Termitaria Suite's modules
	 * has access to.
	 * 
	 * @author dd
	 */
	public List<Module> getAllModules() {
		logger.debug("getAllowedModules");
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.moduleEntity);
		List<Module> modules = (List<Module>)getHibernateTemplate().find(hql.toString());
		return modules;
	}
	
	/**
	 * Returns a list with the Termitaria suite's modules to which the user
	 * has access to. The modules contains user roles.
	 * 
	 * @author dd
	 */
	public List<Module> getAllowedModulesWithRoles(Integer userPersonId) {
		logger.debug("getAllowedModules");
		StringBuffer hql = new StringBuffer("select distinct(m) from ");
		hql.append(IModelConstant.personWithRolesEntity);
		hql.append(" as p inner join p.roles as r ");
		hql.append("inner join r.module as m where p.personId=?");
		List<Module> modules = (List<Module>)getHibernateTemplate().find(hql.toString(), new Integer(userPersonId));
		return modules;
	}
	
	/**
	 * Returns a list with user's roles for the specified module.
	 * 
	 * @author dd
	 * @param userId
	 * @param moduleId
	 * @return
	 */
	public List<Role> getUserRolesForModule(Integer userPersonId, Integer moduleId) {
		logger.debug("getUserRolesForModule");
		StringBuffer hql = new StringBuffer("select r from ");
		hql.append(IModelConstant.personWithRolesEntity);
		hql.append(" as p inner join p.roles as r inner join r.module as m where p.personId = ? and m.moduleId = ?");
		List<Role> roles = (List<Role>)getHibernateTemplate().find(hql.toString(),new Object[] {new Integer(userPersonId), new Integer(moduleId)});
		return roles;
	}
	
	
	/**
	 * Returns a list with user's roles for ADMIN IT
	 * 
	 * @author dd
	 * @param userId
	 * @return
	 */
	public List<Role> getUserRolesForAdminIT(Integer userPersonId) {
		logger.debug("getUserRolesForModule");
		StringBuffer hql = new StringBuffer("select r from ");
		hql.append(IModelConstant.personAdmintITWithRolesEntity);
		hql.append(" as p inner join p.roles as r where p.personId = ?");
		List<Role> roles = (List<Role>)getHibernateTemplate().find(hql.toString(),new Object[] {new Integer(userPersonId)});
		return roles;
	}

	/**
	 * Returns a list with Persmissions for given List of Roles
	 * 
	 * @author dd
	 */
	public List<Permission> getPermissionsForRoles(List<Role> roles) {
		logger.debug("getPermissionsForRoles");
		List<Permission> permissions = null;
		if (roles != null && roles.size() > 0) { 
			StringBuffer hql = new StringBuffer("select distinct(p) from ");
			hql.append(IModelConstant.roleWithPermissionsEntity);
			hql.append(" as r inner join r.permissions as p where r.roleId in (");
			if (roles.size() > 1) {
				for(int i = 0; i < roles.size() - 1; i++) {
					hql.append(roles.get(i).getRoleId());
					hql.append(", ");
				}
				hql.append(roles.get(1).getRoleId());
			}else {
				hql.append(roles.get(0).getRoleId());
			}
			hql.append(")");
			logger.debug("Hq: ".concat(hql.toString()));
			permissions = getHibernateTemplate().find(hql.toString());
		}
		return permissions;
	}
	
	
	/**
	 * Returns a list with Settings specific for an organisation
	 * 
	 * @author dd
	 * @param roles
	 * @return
	 */
	public List<Setting>  getSettingsForOrganisation(Integer organisationId) {
		logger.debug("getSettingsForOrganisation");
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.settingAllEntity);
		hql.append(" as s inner join s.organisation as o where o.organisationId = ?");
		return getHibernateTemplate().find(hql.toString(), new Integer(organisationId));
	}
	
	/**
	 * Verifies if a certaing Person has an ADMIN_IT authority in this application.
	 * For a Person to have this authority it have to has an ADMIN_IT role.
	 * @param personId
	 * @return true if this person really is an ADMIN_IT
	 */
	public boolean isUserAdminIT(Integer personId){
		logger.debug("isUserAdminIT begin personId: ".concat(String.valueOf(personId)));
		boolean answer = false;
		StringBuffer hql = new StringBuffer("select pms from ");
		hql.append(IModelConstant.personAdmintITWithRolesEntity);
		hql.append(" as p inner join p.roles as r inner join r.permissions as pms where p.personId = ?");
		List<Permission> permissions = (List<Permission>)getHibernateTemplate().find(hql.toString(), new Object[] {personId});
		logger.debug("Permissions size " + permissions.size());
		for(int i =0; i < permissions.size() && !answer; answer = PermissionConstant.getInstance().get_Super().equals(permissions.get(i++).getName()));
		logger.debug("isUserAdminIT end answer: ".concat(String.valueOf(answer)));
		return answer;
	}
}
