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

import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoProjectDetailsImpl
 * 
 * @author Coni
 * @author Adelina
 *
 */
public interface IDaoProjectDetails {
	
	/**
	 * Returns a ProjectDetails entity for the corresponding projectId
	 * @author Coni
	 * @param projectId
	 * @return
	 */
	public ProjectDetails getByProjectId(int projectId);
	
	/**
	 * Returns a ProjectDetails entity for the corresponding projectDetailsId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectDetails get(int projectDetailsId);
	
		/**
	 * Checks if the project has project details associated
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean hasProjectDetails(Integer projectId);
	
	/**
	 * Add the projectDetails to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetails
	 * @return
	 */
	public Integer add (ProjectDetails projectDetails);
	
	/**
	 * Updates the projectDetails to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetails
	 */
	public void update (ProjectDetails projectDetails);
	
	/**
	 * Deletes the ProjectDetail, identified by a projectId (update the status to deleted);
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails deleteProjectDetails(Integer projectId);
	
	/**
	 * Returns a ProjectDetails entity for the corresponding projectId
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails getForChangeStatus(Integer projectId);
	
	/**
	 * Finishes the ProjectDetails, identified by a projectId (update the status to finished)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails finishProjectDetails(Integer projectId);
	
	/**
	 * Abort the ProjectDetails, identified by a projectId (update the status to aborted)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails abortProjectDetails(Integer projectId);
	
	/**
	 * Open the ProjectDetails, identified by a projectId (update the status to opened)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails openProjectDetails(Integer projectId);
	
	/**
	 * Returns all opened projects details
	 *
	 * @author alu
	 * @return
	 */
	public List<ProjectDetails> getAllOpened();
	
	/**
	 * Returns the budgets currency id for projectDetailsId
	 *
	 * @author alu
	 * @param projectDetailsId
	 * @return
	 */
	public Integer getBudgetCurrencyId(Integer projectDetailsId);
	
	/**
	 * Updates the notification status of the projectDetails entity
	 *
	 * @author alu
	 * @param projectDetails
	 */
	public void updateNotificationStatus(ProjectDetails projectDetails);

}
