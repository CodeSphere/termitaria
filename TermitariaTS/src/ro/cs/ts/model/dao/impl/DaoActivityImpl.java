/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.model.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.context.MessageSource;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.common.Tools;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.SearchActivityBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoActivity;
import ro.cs.ts.model.dao.IDaoProjectDetails;
import ro.cs.ts.utils.ActivityComparator;
import ro.cs.ts.web.security.UserAuth;

/**
 * Dao class for Activity Entity
 * 
 * @author Coni
 * @author Adelina
 */
public class DaoActivityImpl extends HibernateDaoSupport implements IDaoActivity {

	private static IDaoProjectDetails projectDetailsDao = DaoBeanFactory.getInstance().getDaoProjectDetails();
	
	private static final String PROJECT_FROM_ORG		= "project.from.organization";	
	
		/**
	 * Search for activities
	 * 
	 * @author Adelina
	 * 
	 * @param searchActivityBean
	 * @param projectIds
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException 
	 */
	public List<Activity> getFromSearch(SearchActivityBean searchActivityBean, Set<Integer> projectIds, Integer organizationId, boolean isDeleteAction, MessageSource messageSource) throws BusinessException {
		logger.debug("getFromSearch - START ");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.activityForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.activityForListingEntity);		
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		
		// organization		
		if (searchActivityBean.getOrganizationId() != null) {
			dc.add(Restrictions.eq("organizationId", searchActivityBean.getOrganizationId()));
			dcCount.add(Restrictions.eq("organizationId", searchActivityBean.getOrganizationId()));
		}
					
		// name		
		if(Tools.getInstance().stringNotEmpty(searchActivityBean.getName())) {
			dc.add(Restrictions.ilike("name", "%".concat(searchActivityBean.getName().concat("%"))));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchActivityBean.getName().concat("%"))));
			logger.debug("name = " + searchActivityBean.getName());
		}			
						
		// project	
		if(searchActivityBean.getProjectId() != null) {
			if(searchActivityBean.getProjectId() != IConstant.NOM_ACTIVITY_SEARCH_ALL && searchActivityBean.getProjectId() != IConstant.NOM_ACTIVITY_SEARCH_IN_ORGANIZATION) {			
				//set the selected projectId as search criteria					
				dc.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchActivityBean.getProjectId()));
				dcCount.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchActivityBean.getProjectId()));			
			} else if(searchActivityBean.getProjectId() == IConstant.NOM_ACTIVITY_SEARCH_ALL) {
				if(!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ActivityAdvancedSearch())) {
					DetachedCriteria subquery = DetachedCriteria.forEntityName(IModelConstant.activityForListingEntity);
					subquery.createCriteria("projectDetails").add(Restrictions.in("projectId", projectIds));		
					subquery.add(Restrictions.disjunction());
					subquery.setProjection(Projections.id());																
					dc.add(Expression.or(Subqueries.propertyIn("activityId", subquery), Restrictions.isNull("projectDetails")));																
					dcCount.add(Expression.or(Subqueries.propertyIn("activityId", subquery), Restrictions.isNull("projectDetails")));
				}				
			} else if(searchActivityBean.getProjectId() == IConstant.NOM_ACTIVITY_SEARCH_IN_ORGANIZATION) {
				dc.add(Restrictions.isNull("projectDetails"));
				dcCount.add(Restrictions.isNull("projectDetails"));
			}		
		}
							
		// Status
		dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		dcCount.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		
		// Billable
		if (searchActivityBean.getBillable() != null ) {
			if (searchActivityBean.getBillable().equals(IConstant.NOM_BILLABLE_YES) || searchActivityBean.getBillable().equals(IConstant.NOM_BILLABLE_NO)) {
				dc.add(Restrictions.eq("billable", searchActivityBean.getBillable()));
				dcCount.add(Restrictions.eq("billable", searchActivityBean.getBillable()));
			}
		}
		
		//the results list
		List<Activity> res = null;
		
		//the order can be done while retrieving the data from the database only if the sort parameter is the activity name or billable;
		//this cannot be done if the results must be ordered by project name
		if (searchActivityBean.getSortParam() != null && !"".equals(searchActivityBean.getSortParam()) && !"projectName".equals(searchActivityBean.getSortParam())) {
			// check if I have to order the results
			// if I have to, check if I have to order them ascending or descending
			if (searchActivityBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchActivityBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchActivityBean.getSortParam()));
			}
			
			// if the request didn't come from the pagination area, 
			// it means that I have to set the number of results and pages
			if (isDeleteAction || searchActivityBean.getNbrOfResults() == -1){
				boolean isSearch = false;
				if (searchActivityBean.getNbrOfResults() == -1 ) {
					isSearch = true;
				}
				// set the count(*) restriction			
				dcCount.setProjection(Projections.countDistinct("activityId"));
				
				//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
				//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
				int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
				logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
				searchActivityBean.setNbrOfResults(nbrOfResults);
				
				// get the number of pages
				if (nbrOfResults % searchActivityBean.getResultsPerPage() == 0) {
					searchActivityBean.setNbrOfPages(nbrOfResults / searchActivityBean.getResultsPerPage());
				} else {
					searchActivityBean.setNbrOfPages(nbrOfResults / searchActivityBean.getResultsPerPage() + 1);
				}
				// after an activity is deleted, the same page has to be displayed;
				//only when all the client from last page are deleted, the previous page will be shown 
				if (isDeleteAction && (searchActivityBean.getCurrentPage() > searchActivityBean.getNbrOfPages()) ){
					searchActivityBean.setCurrentPage( searchActivityBean.getNbrOfPages() );
				} else if ( isSearch ) {
					searchActivityBean.setCurrentPage(1);
				}
			}
			
			res = (List<Activity>) getHibernateTemplate().findByCriteria(dc, (searchActivityBean.getCurrentPage()-1) * searchActivityBean.getResultsPerPage(), searchActivityBean.getResultsPerPage());
		} else {
			res = (List<Activity>)getHibernateTemplate().findByCriteria(dc);
			// if the request didn't come from the pagination area, 
			// it means that I have to set the number of results and pages
			if (isDeleteAction || searchActivityBean.getNbrOfResults() == -1){
				boolean isSearch = false;
				if (searchActivityBean.getNbrOfResults() == -1 ) {
					isSearch = true;
				}
				int nbrOfResults = res.size();
				logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
				searchActivityBean.setNbrOfResults(nbrOfResults);
				
				// get the number of pages
				if (nbrOfResults % searchActivityBean.getResultsPerPage() == 0) {
					searchActivityBean.setNbrOfPages(nbrOfResults / searchActivityBean.getResultsPerPage());
				} else {
					searchActivityBean.setNbrOfPages(nbrOfResults / searchActivityBean.getResultsPerPage() + 1);
				}
				// after an exchange is deleted, the same page has to be displayed;
				//only when all the exchanges from last page are deleted, the previous page will be shown 
				if (isDeleteAction && (searchActivityBean.getCurrentPage() > searchActivityBean.getNbrOfPages()) ){
					searchActivityBean.setCurrentPage( searchActivityBean.getNbrOfPages() );
				} else if ( isSearch ) {
					searchActivityBean.setCurrentPage(1);
				}
			}
		}
		
		List<Project> projects = new ArrayList<Project>();
		
		if (projectIds.size() > 0) {
			projects = BLProject.getInstance().getProjectsSimpleByProjectIds(projectIds);
			logger.debug("projects = " + projects);
		}
		
		// setting the project name 
		for(Activity activity : res) {
			if(activity.getProjectDetails() != null && activity.getProjectDetails().getProjectId() != null) {
				if(projects != null && projects.size() > 0) {
					for(Project project : projects) {						
						if(activity.getProjectDetails().getProjectId().equals(project.getProjectId())) {
							activity.setProjectName(project.getName());
							activity.setProjectId(project.getProjectId());
							activity.setProjectManagerId(project.getManagerId());
							logger.debug("activity = " + activity);
							break;
						}
					}
				}						
			} else {
				activity.setProjectId(-1);				
				activity.setProjectName(messageSource.getMessage(PROJECT_FROM_ORG, null, null));
			}
			
			logger.debug("projectName = " + activity.getProjectName());
		}		
		
		List<Activity> activities = new ArrayList<Activity>();
		if (searchActivityBean.getSortParam() != null && "projectName".equals(searchActivityBean.getSortParam())) {
			//sorting the exchanges list
			//------sort the list
			if(searchActivityBean.getSortParam().equals("projectName")){
				Collections.sort(res, ActivityComparator.getInstance().activityProjectNameComparator());
			}

			//ascending or descending
			if(searchActivityBean.getSortDirection() == IConstant.DESCENDING){
				Collections.reverse(res);
			}
			long start = (searchActivityBean.getCurrentPage() - 1) * searchActivityBean.getResultsPerPage();
			long end  = searchActivityBean.getCurrentPage() * searchActivityBean.getResultsPerPage();
			
			// go over the entries				
			for(int i = (int)start ;i < end; i++) {
				if(i >= res.size()) {
					continue;
				}
				activities.add(res.get(i));
			}
		} else {
			activities = res;
		}
		
		logger.debug("getFromSearch - END");
		return activities;
	}
	
	/**
	 * Returns an activity containing the basic info
	 * @author Coni
	 * @param activityId
	 * @return
	 */
	public Activity get(int activityId) {
		logger.debug("get - START - activity with id =".concat(String.valueOf(activityId)));
		Activity activity = (Activity) getHibernateTemplate().get(IModelConstant.activitySimpleEntity, activityId);
		logger.debug("get - END");
		return activity;
	}
	
	/**
	 * Gets all an organization's activities
	 * 
	 * @author Coni
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public List<Activity> getByOrganization(int organizationId) {
		logger.debug("getByOrganization - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.activitySimpleEntity);
		dc.add(Restrictions.eq("organizationId", organizationId));
		dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		dc.add(Restrictions.isNull("projectDetailId"));
		List<Activity> result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByOrganization - END");
		return result;
	}
	
	/**
	 * Returns an activity containing all the info
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public Activity getWithAll(Integer activityId) {
		
		logger.debug("get - START - activity with id =".concat(String.valueOf(activityId)));		
		Activity activity = (Activity) getHibernateTemplate().get(IModelConstant.activityEntity, activityId);		
		logger.debug("get - END");
		
		return activity;
	}
	
	/**
	 * Adds an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 */
	public void add(Activity activity) {
		logger.debug("add - START");
		
		getHibernateTemplate().save(IModelConstant.activityEntity, activity);
		
		logger.debug("add - END");
	}
	
	/**
	 * Updates an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 */
	public void update(Activity activity) {
		logger.debug("update - START");
		
		getHibernateTemplate().update(IModelConstant.activityForUpdateEntity, activity);
		
		logger.debug("update - END");
	}	
	
	/**
	 * Gets all a project activities
	 * 
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws  
	 * @throws BusinessException
	 */
	public List<Activity> getByProjectId(int projectId) {
		logger.debug("getByProjectId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.activitySimpleEntity);
		ProjectDetails projectDetails = projectDetailsDao.getByProjectId(projectId);
		
		dc.add(Restrictions.eq("projectDetailId", projectDetails.getProjectDetailId()));
		dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		
		List<Activity> res = (List<Activity>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByProjectId - END");
		return res;
	}
	
		/**
	 * Returns an activity containing with project details
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public Activity getWithProjectDetail(Integer activityId) {
		
		logger.debug("get - START - activity with id =".concat(String.valueOf(activityId)));		
		Activity activity = (Activity) getHibernateTemplate().get(IModelConstant.activityForListingEntity, activityId);		
		logger.debug("get - END");
		
		return activity;
	}
	
	/**
	 * Deletes an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public Activity deleteAll(Integer activityId) {
		logger.debug("deleteAll - START");
		logger.debug("Deleting activity with id: ".concat(activityId.toString()));
		
		Activity activity = get(activityId);
		activity.setStatus(IConstant.NOM_ACTIVITY_STATUS_DELETED);
		
		logger.debug("Deleting the activity : " + activity);
		getHibernateTemplate().update(IModelConstant.activitySimpleEntity, activity);
		logger.debug("Activity " + activity + " has been deleted");
		logger.debug("deleteAll  - END");
		return activity;
	}		
	
	/**
	 * Cheks if an activity has associated a records
	 *
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public boolean hasActivityAssociateRecord(Integer activityId) {
		logger.debug("hasActivityAssociateRecord - START");
		
		StringBuffer hql = new StringBuffer("select a from ");
		hql.append(IModelConstant.activityWithRecordsEntity);
		hql.append(" as a inner join a.records as r where a.activityId=");
		hql.append(activityId);		
		logger.debug(hql.toString());
		
		List<Activity> activities = getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, activities);
		logger.debug("size = " + activities.size());
		if(activities != null && activities.size() > 0) {
			Activity activity = activities.get(0);
			logger.debug("Activity = " + activity);
			Set<Record> records = activity.getRecords();
			logger.debug("size  records = " + records.size());
			if(records != null && records.size() > 0) {
				return true;
			}
			return false;
		} 
		
		logger.debug("hasActivityAssociateRecord - END");
		return false;
	}
	
	/**
	 * Returns Activity entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Activity> getByCurrencyId(int currencyId) {
		logger.debug("getByCurrencyId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.activityWithCurrenciesEntity);
		dc.add(Restrictions.or(Restrictions.eq("costPriceCurrencyId", currencyId), Restrictions.eq("billingPriceCurrencyId", currencyId)));
		dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		
		List<Activity> res = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByCurrencyId - END");
		return res;
	}
	
	/**
	 * Deletes an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @return
	 */
	public Activity delete(Activity activity) {
		logger.debug("delete - START");
		logger.debug("Deleting activity with id: ".concat(String.valueOf(activity.getActivityId())));
			
		activity.setStatus(IConstant.NOM_ACTIVITY_STATUS_DELETED);
		
		logger.debug("Deleting the activity : " + activity);
		getHibernateTemplate().update(IModelConstant.activitySimpleEntity, activity);
		logger.debug("Activity " + activity + " has been deleted");
		logger.debug("delete  - END");
		return activity;
	}		
	
	
	/**
	 * Gets the activities for a projectDetail
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetailId
	 * @return
	 * @throws  
	 * @throws BusinessException
	 */
	public List<Activity> getByProjectDetailId(Integer projectDetailId) {
		logger.debug("getByProjectDetailId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.activitySimpleEntity);		
		
		dc.add(Restrictions.eq("projectDetailId", projectDetailId));
		dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		
		List<Activity> res = (List<Activity>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByProjectDetailId - END");
		return res;
	}
	
	
	
}
