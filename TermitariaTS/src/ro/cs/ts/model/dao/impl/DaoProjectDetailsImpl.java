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
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.IDaoProjectDetails;

/**
 * Dao class for ProjectDetails Entity
 * 
 * @author Coni
 * @author Adelina
 */
public class DaoProjectDetailsImpl extends HibernateDaoSupport implements IDaoProjectDetails{

	/**
	 * Returns a ProjectDetails entity for the corresponding projectId
	 * @author Coni
	 * @param projectId
	 * @return
	 */
	public ProjectDetails getByProjectId(int projectId) {
		logger.debug("getByProjectId - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectDetailsEntity);
		List<ProjectDetails> result = null;
		
		dc.add(Restrictions.eq("projectId", projectId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_DETAILS_STATUS_DELETED));
		
		result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByProjectId - END");
		if (result.size() > 0) {			
			return result.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns a ProjectDetails entity for the corresponding projectDetailsId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectDetails get(int projectDetailsId) {
		logger.debug("get - START");
		ProjectDetails projectDetails = (ProjectDetails) getHibernateTemplate().get(IModelConstant.projectDetailsEntity, projectDetailsId);
		logger.debug("get - END");
		return projectDetails;
	}
	
	/**
	 * Checks if the project has project details associated
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean hasProjectDetails(Integer projectId) {
		logger.debug("hasProjectDetails - START");
		
		List<ProjectDetails> projectDetails = new ArrayList<ProjectDetails>();
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectDetailsSimpleEntity);
		dc.add(Restrictions.eq("projectId", projectId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_DETAILS_STATUS_DELETED));
		
		projectDetails = getHibernateTemplate().findByCriteria(dc);		
		logger.debug("hasProjectDetails - END");
		if (projectDetails.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Add the projectDetails to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetails
	 * @return
	 */
	public Integer add (ProjectDetails projectDetails) {
		logger.debug("add - START");
		Integer projectDetailId = (Integer)getHibernateTemplate().save(IModelConstant.projectDetailsEntity, projectDetails);
		logger.debug("add - END");			
		return projectDetailId;
	}
	
	/**
	 * Updates the projectDetails to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetails
	 */
	public void update (ProjectDetails projectDetails) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.projectDetailsForUpdateEntity, projectDetails);
		logger.debug("update - END");
	}		
	
	/**
	 * Returns a ProjectDetails entity for the corresponding projectId
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails getForChangeStatus(Integer projectId) {
		logger.debug("getForChangeStatus - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectDetailsSimpleEntity);
		List<ProjectDetails> result = null;
		
		dc.add(Restrictions.eq("projectId", projectId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_DETAILS_STATUS_DELETED));
		
		result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getForChangeStatus - END");
		if (result.size() > 0) {			
			return result.get(0);
		} else {
			return null;
		}
	}	
	
	/**
	 * Deletes the ProjectDetail, identified by a projectId (update the status to deleted);
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails deleteProjectDetails(Integer projectId) {
		logger.debug("deleteProjectDetails - START id : ".concat(String.valueOf(projectId)));		
		ProjectDetails projectDetails = getForChangeStatus(projectId);	
		
		if(projectDetails != null && projectDetails.getProjectDetailId() != -1) {
			projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_DELETED);					
			getHibernateTemplate().update(IModelConstant.projectDetailsSimpleEntity, projectDetails);
		}
		
		logger.debug("deleteProjectDetails - END");
		return projectDetails;
	}	
	
	/**
	 * Finishes the ProjectDetails, identified by a projectId (update the status to finished)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails finishProjectDetails(Integer projectId) {
		logger.debug("finishProjectDetails - START id : ".concat(String.valueOf(projectId)));
		ProjectDetails projectDetails = getForChangeStatus(projectId);
		
		if(projectDetails != null && projectDetails.getProjectDetailId() != -1) {
			projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_FINISHED);						
			getHibernateTemplate().update(IModelConstant.projectDetailsSimpleEntity, projectDetails);
		}
		
		logger.debug("finishProjectDetails - END");
		return projectDetails;
	}
	
	/**
	 * Abort the ProjectDetails, identified by a projectId (update the status to aborted)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails abortProjectDetails(Integer projectId) {
		logger.debug("abortProjectDetails - START id : ".concat(String.valueOf(projectId)));
		
		ProjectDetails projectDetails = getForChangeStatus(projectId);		
		
		if(projectDetails != null && projectDetails.getProjectDetailId() != -1) {
			projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_ABORTED);					
			getHibernateTemplate().update(IModelConstant.projectDetailsSimpleEntity, projectDetails);
		}
		
		logger.debug("abortProjectDetails - END");
		return projectDetails;
	}		
	
	/**
	 * Open the ProjectDetails, identified by a projectId (update the status to opened)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails openProjectDetails(Integer projectId) {
		logger.debug("openProjectDetails - START id : ".concat(String.valueOf(projectId)));
		
		ProjectDetails projectDetails = getForChangeStatus(projectId);		
		
		if(projectDetails != null && projectDetails.getProjectDetailId() != -1) {
			projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);					
			getHibernateTemplate().update(IModelConstant.projectDetailsSimpleEntity, projectDetails);
		}
		
		logger.debug("openProjectDetails - END");
		return projectDetails;
	}
	
	/**
	 * Returns all opened projects details
	 *
	 * @author alu
	 * @return
	 */
	public List<ProjectDetails> getAllOpened(){
		logger.debug("getAllOpened - START");
		
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.projectDetailsEntity).append(" where status = ?");
		
		List<ProjectDetails> projectsList = (List<ProjectDetails>) getHibernateTemplate().find(query.toString(), IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);
		
		logger.debug("getAllOpened - END");
		return projectsList;
	}
	
	/**
	 * Returns the budgets currency id for projectDetailsId
	 *
	 * @author alu
	 * @param projectDetailsId
	 * @return
	 */
	public Integer getBudgetCurrencyId(Integer projectDetailsId){
		logger.debug("getBudgetCurrencyId - START");
		
		Integer budgetCurrencyId = null;
		List<Integer> result = null;
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectDetailsSimpleWithBudgetCurrencyId);
		
		dc.add(Restrictions.eq("projectDetailId", projectDetailsId));
		dc.setProjection(Projections.property("budgetCurrencyId"));
		
		result = getHibernateTemplate().findByCriteria(dc);
		
		if ((result != null) && (result.size() > 0)){
			budgetCurrencyId = result.get(0);
		}
		
		logger.debug("getBudgetCurrencyId - END");
		return budgetCurrencyId;
	}
	
	/**
	 * Updates the notification status of the projectDetails entity
	 *
	 * @author alu
	 * @param projectDetails
	 */
	public void updateNotificationStatus(ProjectDetails projectDetails){
		logger.debug("updateNotificationStatus - START");
		getHibernateTemplate().update(IModelConstant.projectDetailsUpdateNotificationStatus, projectDetails);
		logger.debug("updateNotificationStatus - END");
	}
	
}
