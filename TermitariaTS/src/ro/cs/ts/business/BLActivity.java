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
package ro.cs.ts.business;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.MessageSource;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.SearchActivityBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoActivity;

/**
 * 
 * @author Coni
 * @author Adelina
 *
 */
public class BLActivity extends BusinessLogic{

	//singleton implementation
	private static BLActivity theInstance = null;
	
	private BLActivity(){};
	static {
		theInstance = new BLActivity();
	}
	public static BLActivity getInstance(){
		return theInstance;
	}
	
	private static IDaoActivity activityDao = DaoBeanFactory.getInstance().getDaoActivity();
	
	/**
	 * Returns an activity containing the basic info
	 * @author Coni
	 * @param activityId
	 * @return
	 */
	public Activity get(int activityId) throws BusinessException {
		logger.debug("get - START");
		Activity activity =  null;
		try {
			activity  = activityDao.get(activityId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_GET, e);
		}
		logger.debug("get - END");
		return activity;
	}
	
	/**
	 * Gets all an organization's activities( only the activities that are not specific to a project)
	 * 
	 * @author Coni
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public List<Activity> getByOrganization(int organizationId) throws BusinessException {
		logger.debug("getByOrganization - START");
		List<Activity> activities = null;
		try {
			activities = activityDao.getByOrganization(organizationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_GET_BY_ORGANIZATION, e);
		}
		logger.debug("getByOrganization - END");
		return activities;
	}
	
	/**
	 * Gets all a project activities
	 * 
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException
	 */
	public List<Activity> getByProjectId(int projectId) throws BusinessException {
		logger.debug("getByProjectId - START");
		List<Activity> activities = null;
		try {
			activities = activityDao.getByProjectId(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_GET_BY_PROJECT_ID, e);
		}
		logger.debug("getByProjectId - END");
		return activities;
	}
	
	/**
	 * Returns an activity containing all the info
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 * @throws BusinessException 
	 */
	public Activity getWithAll(Integer activityId) throws BusinessException {		
		logger.debug("get - START - activity with id =".concat(String.valueOf(activityId)));		
		
		Activity activity = null;
		try{
			activity = activityDao.getWithAll(activityId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_GET_WITH_ALL, e);
		}
		
		logger.debug("get - END");		
		return activity;
	}
	
	/**
	 * Adds an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @throws BusinessException 
	 */
	public void add(Activity activity) throws BusinessException {
		logger.debug("add - START");
		try{
			activityDao.add(activity);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_ADD, e);
		}
				
		logger.debug("add - END");
	}
	
	/**
	 * Updates an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @throws BusinessException 
	 */
	public void update(Activity activity) throws BusinessException {
		logger.debug("update - START");
		
		try{
			activityDao.update(activity);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_UPDATE, e);
		}
		
		logger.debug("update - END");
	}	
	
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
		logger.debug("getFromSearch - START");
		List<Activity> res = null;
		try{
			res = activityDao.getFromSearch(searchActivityBean, projectIds, organizationId, isDeleteAction, messageSource);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_SEARCH, e);
		}
		logger.debug("getFromSearch - END");
		return res;
	}
	
	/**
	 * Returns an activity containing with project details
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 * @throws BusinessException 
	 */
	public Activity getWithProjectDetail(Integer activityId) throws BusinessException {
		
		logger.debug("get - START - activity with id =".concat(String.valueOf(activityId)));		
		Activity activity = null;
		try{
			activity = activityDao.getWithProjectDetail(activityId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_GET_WITH_PROJECT_DETAIL, e);
		}
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
	 * @throws BusinessException 
	 */
	public Activity deleteAll(Integer activityId) throws BusinessException {
		logger.debug("deleteAll - START");
		logger.debug("Deleting activity with id: ".concat(activityId.toString()));
		
		Activity activity = null;
		try{
			activity = activityDao.deleteAll(activityId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_DELETE, e);
		}
		
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
	 * @throws BusinessException 
	 */
	public boolean hasActivityAssociateRecord(Integer activityId) throws BusinessException {
		logger.debug("hasActivityAssociateRecord - START");
		
		boolean hasActivityAssociateRecord = false;
		try{
			hasActivityAssociateRecord = activityDao.hasActivityAssociateRecord(activityId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_HAS_ASSOCIATED_RECORD, e);
		}
		
		logger.debug("hasActivityAssociateRecord - END");
		return hasActivityAssociateRecord;
	}	
	
	/**
	 * Returns Activity entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Activity> getByCurrencyId(int currencyId) throws BusinessException {
		logger.debug("getByCurrencyId - START");
		List<Activity> res = null;
		try {
			res = activityDao.getByCurrencyId(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_GET_BY_CURRENCY_ID, e);
		}
		logger.debug("getByTeamMemberId - END");
		return res;
	}
	
	/**
	 * Deletes an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @return
	 * @throws BusinessException 
	 */
	public Activity delete(Activity activity) throws BusinessException {
		logger.debug("delete - START");
		logger.debug("Deleting activity with id: ".concat(String.valueOf(activity.getActivityId())));
			
		try{
			activity = activityDao.delete(activity);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_DELETE, e);
		}
		
		logger.debug("Deleting the activity : " + activity);		
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
	 * @throws BusinessException 
	 * @throws  
	 * @throws BusinessException
	 */
	public List<Activity> getByProjectDetailId(Integer projectDetailId) throws BusinessException {
		logger.debug("getByProjectDetailId - START");
		List<Activity> activities = null;
		try {
			activities = activityDao.getByProjectDetailId(projectDetailId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.ACTIVITY_GET_BY_PROJECT_DETAIL_ID, e);
		}
		logger.debug("getByProjectDetailId - END");
		return activities;
	}
}
