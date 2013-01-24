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
package ro.cs.cm.model.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.IModelConstant;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.SearchClientBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.model.dao.DaoBeanFactory;
import ro.cs.cm.model.dao.IDaoClient;
import ro.cs.cm.model.dao.IDaoProject;


/**
 * Dao class for Client Entity
 * 
 * @author Coni
 */
public class DaoClientImpl extends HibernateDaoSupport implements IDaoClient {
		
	private static IDaoProject projectDao = DaoBeanFactory.getInstance().getDaoProject();
	
	/**
	 * Searches for clients using the criterion defined in searchClientBean
	 * @author Coni
	 * @param searchClientBean
	 * @param isDeleteAction
	 * @return
	 */
	public List<Client> getClientBeanFromSearch(SearchClientBean searchClientBean, boolean isDeleteAction) {
		logger.debug("getClientBeanFromSearch - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.clientForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.clientForListingEntity);
		
		if (searchClientBean.getOrganizationId() != -1){
			dc.add(Restrictions.eq("organizationId", searchClientBean.getOrganizationId()));
			dcCount.add(Restrictions.eq("organizationId", searchClientBean.getOrganizationId()));
		}
		
		//if the c_name property is set it means it is a client of type firm and the p_firstName and
		//p_lastName properties of a person won't be set; there is a third case when the client search is first displayed, 
		//with no client type being selected 
		if (searchClientBean.getC_name() != null && !"".equals(searchClientBean.getC_name()) && searchClientBean.getType() == IConstant.NOM_CLIENT_TYPE_FIRM){
			dc.add(Restrictions.ilike("c_name", "%".concat(searchClientBean.getC_name().concat("%"))));
			dcCount.add(Restrictions.ilike("c_name", "%".concat(searchClientBean.getC_name().concat("%"))));
		} else if (searchClientBean.getC_name() != null && !"".equals(searchClientBean.getC_name()) && searchClientBean.getType() == -1){ 			//no client type is selected, but the name field isn't empty
			//we consider the firstname and lastname that might have been entered are split by " "; if no space is entered, we consider it as the firstName
			String firstName = null;
			String lastName = null;
			if (searchClientBean.getC_name().lastIndexOf(" ") != -1) {
				firstName = searchClientBean.getC_name().substring(0, searchClientBean.getC_name().lastIndexOf(" "));
				lastName = searchClientBean.getC_name().substring(searchClientBean.getC_name().lastIndexOf(" ") + 1);
			} else {
				firstName = searchClientBean.getC_name();
			}
			
			if (firstName != null && !"".equals(firstName) && lastName != null && !"".equals(lastName)) {
				dc.add(Restrictions.or(Restrictions.ilike("c_name", "%".concat(searchClientBean.getC_name().concat("%")))
						, Restrictions.and(Restrictions.ilike("p_firstName", "%".concat(firstName).concat("%")), Restrictions.ilike("p_lastName", "%".concat(lastName).concat("%")))));
			} else if (firstName != null && !"".equals(firstName) ){
				dc.add(Restrictions.or(Restrictions.ilike("c_name", "%".concat(searchClientBean.getC_name().concat("%")))
						, Restrictions.ilike("p_firstName", "%".concat(firstName).concat("%"))));
			} else if (lastName != null && !"".equals(lastName)) {
				dc.add(Restrictions.or(Restrictions.ilike("c_name", "%".concat(searchClientBean.getC_name().concat("%")))
						, Restrictions.ilike("p_lastName", "%".concat(lastName).concat("%"))));
			}
		} else {
			if (searchClientBean.getP_firstName() != null && !"".equals(searchClientBean.getP_firstName())){
				dc.add(Restrictions.ilike("p_firstName", "%".concat(searchClientBean.getP_firstName().concat("%"))));
				dcCount.add(Restrictions.ilike("p_firstName", "%".concat(searchClientBean.getP_firstName().concat("%"))));
			}
			if (searchClientBean.getP_lastName() != null && !"".equals(searchClientBean.getP_lastName())){
				dc.add(Restrictions.ilike("p_lastName", "%".concat(searchClientBean.getP_lastName().concat("%"))));
				dcCount.add(Restrictions.ilike("p_lastName", "%".concat(searchClientBean.getP_lastName().concat("%"))));
			}
		}
		
		if (searchClientBean.getEmail() != null && !"".equals(searchClientBean.getEmail())){
			dc.add(Restrictions.ilike("email", "%".concat(searchClientBean.getEmail().concat("%"))));
			dcCount.add(Restrictions.ilike("email", "%".concat(searchClientBean.getEmail().concat("%"))));
		}
		
		if (searchClientBean.getAddress() != null && !"".equals(searchClientBean.getAddress())){
			dc.add(Restrictions.ilike("address", "%".concat(searchClientBean.getAddress().concat("%"))));
			dcCount.add(Restrictions.ilike("address", "%".concat(searchClientBean.getAddress().concat("%"))));
		}
		
		if (searchClientBean.getType() != -1) {
			dc.add(Restrictions.eq("type", searchClientBean.getType()));
			dcCount.add(Restrictions.eq("type", searchClientBean.getType()));
		}
		
		if (searchClientBean.getStatus() != -1) {
			dc.add(Restrictions.eq("status", searchClientBean.getStatus()));
			dcCount.add(Restrictions.eq("status", searchClientBean.getStatus()));
		}
		
		// check if I have to order the results
		if(searchClientBean.getSortParam() != null && !"".equals(searchClientBean.getSortParam()) && !IConstant.NOM_CLIENT_SORT_PARAM_NAME.equals(searchClientBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchClientBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchClientBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchClientBean.getSortParam()));
			}
		}
 
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchClientBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if (searchClientBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("clientId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchClientBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchClientBean.getResultsPerPage() == 0) {
				searchClientBean.setNbrOfPages(nbrOfResults / searchClientBean.getResultsPerPage());
			} else {
				searchClientBean.setNbrOfPages(nbrOfResults / searchClientBean.getResultsPerPage() + 1);
			}
			// after a client is deleted, the same page has to be displayed;
			//only when all the client from last page are deleted, the previous page will be shown 
			if (isDeleteAction && (searchClientBean.getCurrentPage() > searchClientBean.getNbrOfPages()) ){
				searchClientBean.setCurrentPage( searchClientBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchClientBean.setCurrentPage(1);
			}

		}
		
		List<Client> res = (List<Client>)getHibernateTemplate().findByCriteria(dc, (searchClientBean.getCurrentPage()-1) * searchClientBean.getResultsPerPage(), searchClientBean.getResultsPerPage());				
		logger.debug("getClientBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;				
	}
	
	/**
	 * Deletes the client with the specified id
	 * @author Coni
	 * @param clientId
	 * @return
	 * @throws BusinessException
	 */
	public Client delete(int clientId) {
		logger.debug("delete - START");
		Client client = getForDelete(clientId);
		// set the status of the client deleted
		client.setStatus(IConstant.NOM_CLIENT_STATUS_DELETED);		
		// get the projects for the client
		Set<Project> projects = client.getProjects();
		logger.debug("projects = " + projects);
		
		if(projects != null && projects.size() > 0) {
			for(Project project : projects) {
				if(project != null) {
					//projectDao.deleteAll(project.getProjectId());
					// set the status for each project deleted
					project.setStatus(IConstant.NOM_PROJECT_STATUS_DELETED);
					ProjectTeam projectTeam = project.getProjectTeam();
					if(projectTeam != null) {
						logger.debug("projectTeam = " + projectTeam);
						// set the status for each project team deleted
						projectTeam.setStatus(IConstant.NOM_PROJECT_TEAM_STATUS_DELETED);
						logger.debug("projectTeam status = " + projectTeam.getStatus());
						Set<TeamMember> members = projectTeam.getTeamMembers();
						for(TeamMember member : members) {
							// set the status for each member of the project team deleted
							member.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_DELETED);
						}
					}
				}
			}
		}
		
		logger.debug("Deleting the client : " + clientId);
		getHibernateTemplate().update(IModelConstant.clientWithProjectsEntity, client);
		logger.debug("The client : " + client + " has been deleted");
		logger.debug("delete - END");
		return client;
	}
	
	/**
	 * Returns a client
	 * 
	 * @author coni
	 */
	public Client getForDelete(int clientId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(clientId)));
		
		Client  client = (Client) getHibernateTemplate().get(IModelConstant.clientWithProjectsEntity, clientId);
		List<Project> projects = projectDao.getClientsProjects(clientId);
		Set<Project> projectSet = new HashSet<Project>(projects);
		client.setProjects(projectSet);
		logger.debug("getForDelete - END");
		return client;
	}
	
	/**
	 * Gets a client with all its components
	 * 
	 * @author Coni
	 * @param clientId
	 * @return
	 */
	public Client getAll(int clientId) {
		logger.debug("get - START");
		Client client = (Client) getHibernateTemplate().get(IModelConstant.clientEntity, clientId);
		logger.debug("get - END");
		return client;
		
	}
	
	/**
	 * Updates the client with the specified id
	 * 
	 * @author Coni
	 * @param client
	 * @throws BusinessException
	 */
	public void update(Client client) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.clientEntity, client);
		logger.debug("update - END");
	}
	
		
	/**
	 * Adds a new client
	 * 
	 * @author Coni
	 * @param client
	 * @throws BusinessException
	 */
	public void add(Client client) {
		logger.debug("add - START");
		getHibernateTemplate().save(IModelConstant.clientEntity, client);
		logger.debug("add - END");
	}
	
	/** Get Clients by OrganizationId
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<Client> getClientsByOrganizationId(Integer organizationId) {
		logger.debug("getClientsByOrganizationId - START , organizationid = ".concat(String.valueOf(organizationId)));		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.clientEntity);						
		dc.add(Restrictions.eq("organizationId", organizationId));
		dc.add(Restrictions.ne("status", IConstant.NOM_CLIENT_STATUS_DELETED));
		List<Client> clients = (List<Client>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getClientsByOrganizationId -  END");
		return clients;
	}	
	
	/**
	 * Returns a client
	 * 
	 * @author Adelina
	 */
	public Client get(int clientId) {
		logger.debug("get - START - id=".concat(String.valueOf(clientId)));		
		Client  client = (Client) getHibernateTemplate().get(IModelConstant.clientForDeleteEntity, clientId);		
		logger.debug("get - END");
		return client;
	}
}
