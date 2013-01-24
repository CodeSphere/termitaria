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
package ro.cs.ts.model.dao;

import java.util.List;
import java.util.Set;

import org.springframework.context.MessageSource;

import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.SearchActivityBean;
import ro.cs.ts.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoActivityImpl
 * @author Coni
 * @author Adelina
 *
 */
public interface IDaoActivity {

	/**
	 * Returns an activity containing the basic info
	 * @author Coni
	 * @param activityId
	 * @return
	 */
	public Activity get(int activityId);
	
	/**
	 * Gets all an organization's activities
	 * 
	 * @author Coni
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public List<Activity> getByOrganization(int organizationId);
	
	/**
	 * Returns an activity containing all the info
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public Activity getWithAll(Integer activityId);
	
	/**
	 * Adds an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 */
	public void add(Activity activity);
	
	/**
	 * Updates an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 */
	public void update(Activity activity);
	
	/**
	 * Gets all a project activities
	 * 
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException
	 */
	public List<Activity> getByProjectId(int projectId);
	
		/**
	 * Search for activities
	 * 
	 * @author Adelina
	 * 
	 * @param searchActivityBean
	 * @param projectIds
	 * @param isDeleteAction
	 * @return
	 */
	public List<Activity> getFromSearch(SearchActivityBean searchActivityBean, Set<Integer> projectIds, Integer organizationId, boolean isDeleteAction, MessageSource messageSource) throws BusinessException;
	
	/**
	 * Returns an activity containing with project details
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public Activity getWithProjectDetail(Integer activityId);
	
	/**
	 * Deletes an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public Activity deleteAll(Integer activityId);
	
	/**
	 * Cheks if an activity has associated a records
	 *
	 * @author Adelina
	 * 
	 * @param activityId
	 * @return
	 */
	public boolean hasActivityAssociateRecord(Integer activityId); 
	
	/**
	 * Returns Activity entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Activity> getByCurrencyId(int currencyId);
	
	/**
	 * Deletes an activity
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @return
	 */
	public Activity delete(Activity activity);
	
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
	public List<Activity> getByProjectDetailId(Integer projectDetailId);
}
