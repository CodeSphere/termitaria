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
package ro.cs.om.model.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.web.entity.JobWeb;
import ro.cs.om.entity.SearchJobBean;
import ro.cs.om.entity.Job;

/**
 * @author mitziuro
 *
 */
public interface IDaoJob {

	/**
	 * Adding a job
	 * 
	 * @author mitziuro
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(JobWeb jobWeb);
	/**
	 * Remove a given job
	 * 
	 * @author mitziuro
	 */

	/**
	 * Get a Job from id
	 */
	public Job get(int jobId);
	
	@Transactional (rollbackFor=Exception.class)
	public void delete(int idJob);
	
	/**
	 * Delete job identifed by it's id with all the components
	 * 
	 * Return the Job that has been deleted
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public Job deleteAll(Integer jobId);
	/**
	 * Change the status
	 * 
	 * @author mitziuro
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateStatus(int jobId);
	/**
	 * Search for jobs
	 * 
	 * @author mitziuro
	 */
	public List<Job> getJobBeanFromSearch(SearchJobBean searchJobBean, boolean isDeleteAction);
	/**
	 * Return the list of active jobs for an Organisation
	 * 
	 * @author mitziuro
	 */
	public List<Job> getActiveJobsByOrganisationId(int organisationId);
	
	/**
	 * Update a job
	 *
	 * @author mitziuro
	 *
	 * @param jobWeb
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(JobWeb jobWeb);
	
	/**
	 * 
	 * Get a jobWeb by it's id
	 *
	 * @author mitziuro
	 *
	 * @param jobId
	 * @return
	 */
	public JobWeb getJobWeb(int jobId);
	
	/**
	 * Checks if a job has a associated person
	 * 
	 * @author Adelina
	 * 
	 * @param jobId
	 * @return String
	 */
	public String hasJobAssociatePerson(Integer jobId);
	
	/**
	 * 
	 * Return the list of jobs for an organization
	 * 
	 * @author Adelina
	 *
	 * @param organisationId
	 * @return
	 */
	public List<Job> getJobsByOrganisationId(int organisationId);
	
}
