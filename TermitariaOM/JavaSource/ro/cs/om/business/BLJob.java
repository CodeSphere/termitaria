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
package ro.cs.om.business;

import java.util.List;

import ro.cs.om.entity.Job;
import ro.cs.om.entity.SearchJobBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoJob;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.web.entity.JobWeb;

/**
 * Singleton which expose business methods for Job item
 * 
 * @author matti_joona
 */

public class BLJob extends BusinessLogic {
	private IDaoJob jobDao = DaoBeanFactory.getInstance().getDaoJob();
	private IDaoOrganisation organisationDao = DaoBeanFactory.getInstance().getDaoOrganisation();
	
	// singleton implementation
	private static BLJob theInstance = null;

	private BLJob() {
	};

	static {
		theInstance = new BLJob();
	}

	public static BLJob getInstance() {
		return theInstance;
	}

	public void add(Job job) {
	}

	/**
	 * Add the given job
	 *
	 * @author mitziuro
	 *
	 * @param jobWeb
	 * @throws BusinessException
	 */
	public void add(JobWeb jobWeb) throws BusinessException {
		logger.debug("add - START");
		
		try {
			DaoBeanFactory.getInstance().getDaoJob().add(jobWeb);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.JOB_ADD, e);
		}
		
		logger.debug("add - END");
	}
	
	/**
	 * 
	 * Update a job
	 * @author mitziuro
	 * @param jobWeb
	 * @throws BusinessException
	 */
	public void update(JobWeb jobWeb) throws BusinessException {
		logger.debug("update - START");
		
		try {
			DaoBeanFactory.getInstance().getDaoJob().update(jobWeb);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.JOB_UPDATE, e);
		}
		
		logger.debug("update - END");
	}
	
	/**
	 * 
	 * Get a jobweb by it's id
	 *
	 * @author mitziuro
	 *
	 * @param jobId
	 * @return
	 * @throws BusinessException
	 */
	public JobWeb getJobWeb(int jobId) throws BusinessException{
		logger.debug("getJobWeb - START - jobId: ".concat(String.valueOf(jobId)));
    	
		JobWeb jw = null;
    	try{
        	jw = jobDao.getJobWeb(jobId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.JOB_WEB_GET, bexc);
    	}
    	
    	logger.debug("getJobWeb - END");
    	return jw;
	}
	
	/**
	 * 
	 *
	 * @author mitziuro
	 *
	 * @param idJob
	 * @throws BusinessException
	 */
	public void updateStatus(int idJob) throws BusinessException {
		logger.debug("update - START - id:".concat(String.valueOf(idJob)));
	   
		try{
	    	DaoBeanFactory.getInstance().getDaoJob().updateStatus(idJob);
	    } catch(Exception e){
	    	throw new BusinessException(ICodeException.JOB_UPDATE_STATUS, e);
	    }
	    
	    logger.debug("update - END");
	}
	
	/**
	* 
	*
	* @author mitziuro
	*
 	* @param idJob
 	* @throws BusinessException
 	*/
	public void delete(int idJob) throws BusinessException{
		logger.debug("delete - START - id:".concat(String.valueOf(idJob)));
	    
		try{
	    	DaoBeanFactory.getInstance().getDaoJob().delete(idJob);
	    } catch(Exception e){
	    	throw new BusinessException(ICodeException.JOB_DELETE, e);
	    }
	    
	    logger.debug("delete - END");
	}
	
	/**
	 * Delete job identifed by it's id with all the components
	 * 
	 * Return the Job that has been deleted
	 * @author Adelina
	 * @throws BusinessException 
	 */
	
	public Job deleteAll(Integer jobId) throws BusinessException{
		logger.debug("deleteAll - START");
		
		Job job = null;
		try{
			job = DaoBeanFactory.getInstance().getDaoJob().deleteAll(jobId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.JOB_DELETE, e);
		}
		
		logger.debug("deleteAll - END");
		return job;
		
	}
	
	/**
	 * 
	 * Get a job by it's id
	 * @author mitziuro
	 *
	 * @param jobId
	 * @return
	 * @throws BusinessException
	 */
	public Job get(int jobId) throws BusinessException{
		logger.debug("get - START - jobId: ".concat(String.valueOf(jobId)));
    	
		Job job = null;
    	try{
        	job = jobDao.get(jobId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.JOB_GET, bexc);
    	}
    	
    	logger.debug("get - END");
    	return job;
	}

	/**
	 * Returns a list of jobs for the given search criterion
	 *
	 * @author mitziuro
	 *
	 * @param searchJobBean
	 * @param isChangeAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Job> getResultsForSearch(SearchJobBean searchJobBean,
			boolean isChangeAction) throws BusinessException {
		logger.debug("getResultsForSearch - START");
		
		List<Job> res = null;
		try {
			res = DaoBeanFactory.getInstance().getDaoJob()
					.getJobBeanFromSearch(searchJobBean, isChangeAction);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.JOB_GET_RESULTS, e);
		}
		
		logger.debug("getResultsForSearch - END");
		return res;
	}
	
	/**
	 * 
	 * Getting the active jobs by organisationId
	 * @author mitziuro
	 * @param idOrganisation
	 * @return
	 * @throws BusinessException
	 */
	public List<Job> getActiveJobsByOrganisationId(int organisationId) throws BusinessException {
		logger.debug("getByOrganisation - START");
		
		List<Job> res = null;
		try {
			res = DaoBeanFactory.getInstance().getDaoJob()
					.getActiveJobsByOrganisationId(organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.JOB_GET_RESULTS_BY_ID, e);
		}
		
		logger.debug("getResultsForSearch - END");
		return res;
	}

	public List<Job> getByOrganisation(int idOrganisation) {
		return null;
	}

	public List<Job> getByDepartment(int idOrganisation) {
		return null;
	}
	
	/**
	 * Checks if a job has a associated person
	 * 
	 * @author Adelina
	 * 
	 * @param jobId
	 * @return String
	 */
	 public String hasJobAssociatePerson(Integer jobId) throws BusinessException{
		 logger.debug("hasJobAssociatePerson - START");
		 
		 String hasPerson = null;
		 try{
			hasPerson = jobDao.hasJobAssociatePerson(jobId);
		 } catch(Exception e) {
			 throw new BusinessException(ICodeException.JOB_HAS_PERSON, e);
		 }		 
		 logger.debug("hasJobAssociatePerson - END");
		 
		 return hasPerson;
	 }

 	/**
	 * 
	 * Return the list of jobs for an organization
	 * 
	 * @author Adelina
	 *
	 * @param organisationId
	 * @return
	 * @throws BusinessException 
	 */
	 public List<Job> getJobsByOrganisationId(int organisationId) throws BusinessException{
		logger.debug("getJobsByOrganisationId - START");
		
		List<Job> res = null;
		try {
			res = DaoBeanFactory.getInstance().getDaoJob()
					.getJobsByOrganisationId(organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.JOB_GET_BY_ORG, e);
		}
		
		logger.debug("getJobsByOrganisationId - END");
		return res;
	}

}
