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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.SearchUserGroupBean;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.model.dao.IDaoUserGroup;
import ro.cs.tools.Tools;
/*
 * This class implements all the model methods specific for an user group
 * @author coni
 */
public class DaoUserGroupImpl extends HibernateDaoSupport implements IDaoUserGroup{

	/*
	 * Returns an user group with all its components
	 * @author coni
	 */
	public UserGroup getAll(int userGroupId){
			logger.debug("DaoUserGroupImpl - getAll - START getting user group with id: ".concat(String.valueOf(userGroupId)));
			logger.debug("Getting the group of users");
			UserGroup userGroup = (UserGroup) getHibernateTemplate().get(IModelConstant.userGroupAllEntity, userGroupId);
			logger.debug("DaoUserGroupImpl - getAll - END getting user group with id: ".concat(String.valueOf(userGroupId)));
			return userGroup;
		}
	
	/*
	 * Returns an user group with only its basic components
	 * @param userGroupId
	 * @author coni
	 */
    public UserGroup get(int userGroupId){
		logger.debug("DaoUserGroupImpl - get - START getting user group with id: ".concat(String.valueOf(userGroupId)));
		logger.debug("Getting the group of users");
		UserGroup userGroup = (UserGroup) getHibernateTemplate().get(IModelConstant.userGroupSimpleEntity, userGroupId);
		logger.debug("DaoUserGroupImpl - get - END getting user group with id: ".concat(String.valueOf(userGroupId)));
		return userGroup;
    }
	/*
	 * Deletes the user group specified by userGroupId 
	 * Returns the user group deleted
	 * @author coni
	 */
	public UserGroup deleteAll(int userGroupId){
		logger.debug("DaoUserGroupImpl - deleteAll - START deleting user group with id: ".concat(String.valueOf(userGroupId)));
		logger.debug("Getting the group of users");
		UserGroup userGroup = getAll(userGroupId);
		logger.debug("Deleting the group of users");
		getHibernateTemplate().delete(IModelConstant.userGroupAllEntity, userGroup);
		logger.debug("User group with id ".concat(String.valueOf(userGroupId)).concat(" deleted."));
		logger.debug("DaoUserGroupImpl - deleteAll - END deleting user group with id: ".concat(String.valueOf(userGroupId)));
		return userGroup;
	}
	
	/*
	 * Gets the results for search
	 * @param searchUserGroupBean
	 * @throws BusinessException
	 * @author coni
	 */
	
	public List<UserGroup> getUserGroupBeanFromSearch(SearchUserGroupBean searchUserGroupBean, boolean isDeleteAction){
		logger.debug("DaoUserGroupImpl - getUserGroupBeanFromSearch - START - name: ".concat(String.valueOf(searchUserGroupBean.getName())).
				concat(" - organisationId: ".concat(String.valueOf(searchUserGroupBean.getOrganisationId()))));
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
			
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.userGroupForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.userGroupForListingEntity);
		
		if (searchUserGroupBean.getName() != null && !"".equals(searchUserGroupBean.getName())){
			dc.add(Restrictions.ilike("name", "%".concat(searchUserGroupBean.getName()).concat("%")));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchUserGroupBean.getName()).concat("%")));
		}
		if (searchUserGroupBean.getOrganisationId() != -1){
			dc.add(Restrictions.eq("organisation.organisationId", searchUserGroupBean.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisation.organisationId", searchUserGroupBean.getOrganisationId()));
		}
		//the organization default user group mustn't be displayed
		dc.add(Restrictions.eq("status", IConstant.NOM_USER_GROUP_NORMAL));
		dcCount.add(Restrictions.eq("status", IConstant.NOM_USER_GROUP_NORMAL));
		
		// check if I have to order the results
		if(searchUserGroupBean.getSortParam() != null && !"".equals(searchUserGroupBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchUserGroupBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchUserGroupBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchUserGroupBean.getSortParam()));
			}
		}
		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchUserGroupBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if ( searchUserGroupBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("userGroupId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchUserGroupBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchUserGroupBean.getResultsPerPage() == 0) {
				searchUserGroupBean.setNbrOfPages(nbrOfResults / searchUserGroupBean.getResultsPerPage());
			} else {
				searchUserGroupBean.setNbrOfPages(nbrOfResults / searchUserGroupBean.getResultsPerPage() + 1);
			}
			// after an user group is deleted, the same page has to be displayed;
			//only when all the user groups from last page are deleted, the previous page will be shown 
			if ( isDeleteAction && (searchUserGroupBean.getCurrentPage() > searchUserGroupBean.getNbrOfPages()) ){
				searchUserGroupBean.setCurrentPage( searchUserGroupBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchUserGroupBean.setCurrentPage(1);
			}

		}
		
		List<UserGroup> res = (List<UserGroup>)getHibernateTemplate().findByCriteria(dc, (searchUserGroupBean.getCurrentPage()-1) * searchUserGroupBean.getResultsPerPage(), searchUserGroupBean.getResultsPerPage());
		logger.debug("DaoUserGroupImpl - getUserGroupBeanFromSearch - END");
		return res;
	}
	
	/*
	 * updates the group of users
	 * @param userGroup
	 * @author coni
	 */
	public void updateAll(UserGroup userGroup){
		logger.debug("DaoUserGroupImpl - updateAll - START");		
		getHibernateTemplate().update(IModelConstant.userGroupForUpdateEntity, userGroup);
		logger.debug("DaoUserGroupImpl - updateAll - END");
	}
	
	/*
	 * adds the specified group of users
	 * @param userGroup
	 * @author coni
	 */
	public void addAll(UserGroup userGroup){
		logger.debug("DaoUserGroupImpl - addAll - START");
		getHibernateTemplate().save(IModelConstant.userGroupAllEntity, userGroup);
		logger.debug("DaoUserGroupImpl - addAll - END");
	}
	
	/*
	 * returns the default user group for all organizations
	 * @author coni
	 */
	public UserGroup getDefaultUserGrup(int organizationId){
		logger.debug("DaoUserGroupImpl - getDefaultUserGrup - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.userGroupSimpleEntity);
		//user group  status must be default
		dc.add(Restrictions.eq("status", IConstant.NOM_USER_GROUP_DEFAULT));
		dc.add(Restrictions.eq("organisation.organisationId", organizationId));
		UserGroup defaultUserGroup = null;
		List userGroups = getHibernateTemplate().findByCriteria(dc);
		if (userGroups != null && userGroups.size() > 0) {
			defaultUserGroup = (UserGroup) userGroups.get(0);
		}
		logger.debug("DaoUserGroupImpl - getDefaultUserGrup - END");
		return defaultUserGroup;
	}
	
	/*
	 * returns all an organization user groups except the default one
	 * @author coni
	 */
	public List<UserGroup> getAllOrganizationUserGroups(int organizationId){
		logger.debug("getAllOrganizationUserGroups START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.userGroupSimpleEntity);
		//user group status must be normal
		dc.add(Restrictions.eq("status", IConstant.NOM_USER_GROUP_NORMAL));
		dc.add(Restrictions.eq("organisation.organisationId", organizationId));
		List<UserGroup> res = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getAllOrganizationUserGroups END size:".concat((res != null ? Integer.toString(res.size()): "null")));
		return res;
	}
	
	/**
	 * 
	 * Delete all the usergroups for an organisation
	 * @author mitziuro
	 *
	 * @param organisationId
	 */
	public void deleteByOrganisationId(int organisationId){
		logger.debug("DaoUserGroupImpl - deleteByOrganisationId - START ORG: ".concat(String.valueOf(organisationId)));
		
		String hquery = "from ".concat(IModelConstant.userGroupSimpleEntity).concat(" where organisationId=").concat(Integer.toString(organisationId));
		
		List<UserGroup> userGroups = getHibernateTemplate().find(hquery);
		for(UserGroup userGroup : userGroups){
			getHibernateTemplate().delete(userGroup);
		}
		
		logger.debug("DaoUserGroupImpl - deleteByOrganisationId - END");
	}
	
	/**
	 * Return a list with the user groups from the given organization, without 
	 * those that belongs to a given person
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param personId
	 * @return List<UserGroup>
	 */
	public List<UserGroup> getUserGroupByOrganizationAndPerson(Integer organizationId, Integer personId){
		
		logger.debug("getUserGroupByOrganizationAndPerson - START - organizationId:".concat(String.valueOf(organizationId)).concat("personId = ").concat(String.valueOf(personId)));
		List<UserGroup> userGroups = new ArrayList<UserGroup>();
		
		StringBuffer hql = new StringBuffer("select distinct u from ");		
		hql.append(IModelConstant.userGroupAllEntity);
		hql.append(" as u inner join u.organisation as org where org.organisationId=");
		hql.append(organizationId);
		//user group status must be normal
		hql.append(" and u.status =").append(IConstant.NOM_USER_GROUP_NORMAL);
		hql.append(" and u not in (select u.userGroupId from u as up inner join up.persons as p where p.personId=");
		hql.append(personId).append(")");
						
		logger.debug(hql.toString());
		userGroups = (List<UserGroup>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, userGroups);
		
		logger.debug("getUserGroupByOrganizationAndPerson - END - size:".concat(userGroups != null? String.valueOf(userGroups.size()) : "null"));
		return userGroups;
	}	
	
	/**
	 * Returns an usergroup with the given name
	 * 
	 * @author Adelina
	 * 
	 * @param name
	 * @return
	 */
	public UserGroup getUserGroupByNameAndOrg(String name, Integer organisationId) {
		logger.debug("getUserGroupByNameAndOrg - START - user group by name=".concat(name));
		
		logger.debug("name = ".concat(name));
    	logger.debug("organisationId = ".concat(String.valueOf(organisationId)));
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.userGroupSimpleEntity);		
    	dc.add(Restrictions.eq("name", name));
    	dc.add(Restrictions.eq("organisation.organisationId", organisationId));				
		
		List<UserGroup> userGroups = (List<UserGroup>)getHibernateTemplate().findByCriteria(dc);
		
		Tools.getInstance().printList(logger, userGroups);
		
		logger.debug("getUserGroupByNameAndOrg - END");
		
		if(userGroups.size() > 0) {
			return userGroups.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns a list of user groups that contain the user with the specified id
	 * 
	 * @author Coni
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserGroup> getUserGroupsByUser(int userId) {
		logger.debug("getUserGroupsByUser - START - userId: ".concat(String.valueOf(userId)));
		
		StringBuffer hql = new StringBuffer("select distinct u from ");
		hql.append(IModelConstant.userGroupForUpdateEntity);
		hql.append(" as u inner join u.persons as p where p.personId=");
		hql.append(userId);
		
		logger.debug(hql.toString());
		
		List<UserGroup> userGroups = (List<UserGroup>)getHibernateTemplate().find(hql.toString());
		
		Tools.getInstance().printList(logger, userGroups);
		logger.debug("getUserGroupsByUser - END");
		return userGroups;
	}

}

