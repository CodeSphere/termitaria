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

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchJobBean;
import ro.cs.om.model.dao.IDaoJob;
import ro.cs.om.web.entity.JobWeb;
import ro.cs.tools.Tools;

/**
 * @author mitziuro
 * 
 *         This class implements all the model methods specifics for a job
 */
public class DaoJobImpl extends HibernateDaoSupport implements IDaoJob {

	/**
	 * Adds given job
	 * 
	 * @author mitziuro
	 */
	public void add(JobWeb jobWeb) {
		logger.debug("add - START");
		
		// set job's status to active
		jobWeb.setStatus(IConstant.NOM_JOB_ACTIVE);
		getHibernateTemplate().save(IModelConstant.jobWebEntity, jobWeb);
		logger.debug("Job added".concat(" with id=").concat(
				String.valueOf(jobWeb.getJobId())));
		
		logger.debug("add - END");
	}

	/**
	 * Update a given job
	 * 
	 * @author mitziuro
	 */
	public void update(JobWeb jobWeb) {
		logger.debug("update - START");
		
		getHibernateTemplate().update(IModelConstant.jobWebEntity, jobWeb);
		logger.debug("Job updated".concat(" with id=").concat(
				String.valueOf(jobWeb.getJobId())));
		
		logger.debug("update - END");
	}
	
	/**
	 * Remove a job
	 * 
	 * @author mitziuro
	 */
	public void delete(int jobId) {
 		logger.debug("delete - START - id: ".concat(String.valueOf(jobId)));

		Job job = new Job();
		job.setJobId(jobId);
		getHibernateTemplate().delete(IModelConstant.jobEntity, job);
		
		logger.debug("delete - END");
	}
	
	/**
	 * 
	 * Getting a jobWeb by id
	 * @author mitziuro
	 *
	 * @param jobId
	 * @return
	 */
	public JobWeb getJobWeb(int jobId) {
		logger.debug("getJobWeb - START - id=".concat(String.valueOf(jobId)));
		
		JobWeb jw = (JobWeb) getHibernateTemplate().get(IModelConstant.jobWebEntity,
				jobId);
		logger.debug("Geted id=".concat(String.valueOf(jw.getJobId())));
		
		logger.debug("getJobWeb - END");
		return jw;
	}
	
	/**
	 * Delete job identifed by it's id with all the components
	 * 
	 * Return the Job that has been deleted
	 * @author Adelina
	 */
	
	public Job deleteAll(Integer jobId){
		logger.debug("deleteAll - START");
		
		logger.debug("Deleting job with id: ".concat(String.valueOf(jobId)));
		Job job = getForDelete(jobId);
		logger.debug("Deleting the job : " + job);
		getHibernateTemplate().delete(IModelConstant.jobAllEntity, job);
		logger.debug("Job : " + job + " has been deleted");
	
		logger.debug("deleteAll - END");
		return job;
		
	}
	
	/**
	 * Returns a job in Job format
	 * 
	 * @author mitziuro
	 */
	public Job getForDelete(int jobId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(jobId)));
		
		Job job = (Job) getHibernateTemplate().get(IModelConstant.jobAllEntity,
				jobId);
		logger.debug("Geted id=".concat(String.valueOf(job.getJobId())));
		
		logger.debug("getForDelete - END");
		return job;
	}

	/**
	 * Returns a job in Job format
	 * 
	 * @author mitziuro
	 */
	public Job get(int jobId) {
		logger.debug("get - START - id=".concat(String.valueOf(jobId)));
		
		Job jw = (Job) getHibernateTemplate().get(IModelConstant.jobEntity,
				jobId);
		
		logger.debug("get - END - id=".concat(String.valueOf(jw.getJobId())));
		
		return jw;
	}

	/**
	 * Change a status
	 * 
	 * @author mitziuro
	 */
	public void updateStatus(int jobId) {
		logger.debug("updateStatus - START id: ".concat(String.valueOf(jobId)));

		Job job = get(jobId);
		if (job.getStatus() == IConstant.NOM_JOB_ACTIVE) {
			job.setStatus(IConstant.NOM_JOB_INACTIVE);
		} else {
			job.setStatus(IConstant.NOM_JOB_ACTIVE);
		}
		getHibernateTemplate().update(IModelConstant.jobEntity, job);
		
		logger.debug("updateStatus - END");
	}
	
	/**
	 * Return the list of active jobs for an Organisation
	 * 
	 * @author mitziuro
	 */
	public List<Job> getActiveJobsByOrganisationId(int organisationId) {
		logger.debug("getJobsForOrganisation - START :".concat(" orgId - ").concat(String.valueOf(organisationId)));		
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria
				.forEntityName(IModelConstant.jobEntity);

		if (organisationId != -1) {
			dc.add(Restrictions.eq("organisation.organisationId", organisationId));
		}
		
		dc.add(Restrictions.eq("status", ((int)IConstant.NOM_JOB_ACTIVE)));
		
		List<Job> res = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getJobsForOrganisation - END results size : ".concat(String.valueOf(res.size())));
		return res;
	}	

	/**
	 * Returns a list of jobs
	 * 
	 * @author mitziuro
	 */
	public List<Job> getJobBeanFromSearch(SearchJobBean searchJobBean,
			boolean isChangeAction) {
		logger.debug("getJobBeanFromSearch - START - name:".concat(
				searchJobBean.getName()).concat(" orgId - ").concat(
				String.valueOf(searchJobBean.getOrganisationId())));
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
		
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria
				.forEntityName(IModelConstant.jobForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria
				.forEntityName(IModelConstant.jobForListingEntity);		

		if (searchJobBean.getName() != null
				&& !"".equals(searchJobBean.getName())) {
			dc.add(Restrictions.ilike("name", "%"
					.concat(searchJobBean.getName()).concat("%")));
			dcCount.add(Restrictions.ilike("name", "%"
					.concat(searchJobBean.getName()).concat("%")));			
		}

		if (searchJobBean.getOrganisationId() != -1) {
			logger.debug("getJobBeanFromSearch - START - name:"
					+ searchJobBean.getOrganisationId());
			dc.add(Restrictions.eq("organisation.organisationId", searchJobBean
					.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisation.organisationId", searchJobBean
					.getOrganisationId()));			
		}

		if (searchJobBean.getStatus() != -1) {
			dc.add(Restrictions.eq("status", searchJobBean.getStatus()));
			dcCount.add(Restrictions.eq("status", searchJobBean.getStatus()));			
		}

		// check if I have to order the results
		if (searchJobBean.getSortParam() != null
				&& !"".equals(searchJobBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or
			// descending
			if (searchJobBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchJobBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchJobBean.getSortParam()));
			}
		}

		// if the request didn't come from the pagination area,
		// it means that I have to set the number of results and pages
		if (isChangeAction || searchJobBean.getNbrOfResults() == -1) {
			boolean isSearch = false;
			if ( searchJobBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			
			// set the count(*) restriction
			dcCount.setProjection(Projections.countDistinct("jobId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection
			int nbrOfResults = ((Integer) getHibernateTemplate()
					.findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: "
					.concat(String.valueOf(nbrOfResults)));
			searchJobBean.setNbrOfResults(nbrOfResults);

			// get the number of pages
			if (nbrOfResults % searchJobBean.getResultsPerPage() == 0) {
				searchJobBean.setNbrOfPages(nbrOfResults
						/ searchJobBean.getResultsPerPage());
			} else {
				searchJobBean.setNbrOfPages(nbrOfResults
						/ searchJobBean.getResultsPerPage() + 1);
			}

			// after a job is deleted, the same page has to be displayed;
			//only when all the jobs from last page are deleted, the previous page will be shown 
			if ( isChangeAction && (searchJobBean.getCurrentPage() > searchJobBean.getNbrOfPages()) ){
				searchJobBean.setCurrentPage( searchJobBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchJobBean.setCurrentPage(1);
			}
		}
		List<Job> res = (List<Job>) getHibernateTemplate().findByCriteria(
				dc,	(searchJobBean.getCurrentPage() - 1) * searchJobBean.getResultsPerPage(),
				searchJobBean.getResultsPerPage());
		
		logger.debug("getJobBeanFromSearch - END results size : ".concat(String
				.valueOf(res.size())));
		return res;
	}

	/**
	 * Checks if a job has a associated person
	 * 
	 * @author Adelina
	 * 
	 * @param jobId
	 * @return String
	 */
	public String hasJobAssociatePerson(Integer jobId){		
		logger.debug("hasJobAssociatePerson - START");
		
		StringBuffer hql = new StringBuffer("select j from ");
		hql.append(IModelConstant.jobWithPersons);
		hql.append(" as j inner join j.persons as p where j.jobId=");
		hql.append(jobId);		
		logger.debug(hql.toString());
		
		List<Job> jobs = getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, jobs);
		logger.debug("size = " + jobs.size());
		if(jobs != null && jobs.size() > 0) {
			Job job = jobs.get(0);
			logger.debug("Job = " + job);
			Set<Person> persons = job.getPersons();
			logger.debug("size  persons = " + persons);
			if(persons != null && persons.size() > 0) {
				for(Person person : persons) {
					logger.debug("person =  ".concat(person.getLastName().concat(" ").concat(person.getFirstName())));
					return person.getLastName().concat(" ").concat(person.getFirstName());
				}
			}
			return null;
		} 
		
		logger.debug("hasJobAssociatePerson - END");
		return null;
	}
	
	/**
	 * 
	 * Return the list of jobs for an organization
	 * 
	 * @author Adelina
	 *
	 * @param organisationId
	 * @return
	 */
	public List<Job> getJobsByOrganisationId(int organisationId) {
		logger.debug("getJobsByOrganisationId - START :".concat(" orgId - ").concat(String.valueOf(organisationId)));		
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria
				.forEntityName(IModelConstant.jobEntity);

		if (organisationId != -1) {
			dc.add(Restrictions.eq("organisation.organisationId", organisationId));
		}
						
		List<Job> res = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getJobsByOrganisationId - END results size : ".concat(String.valueOf(res.size())));
		return res;
	}
	
}
