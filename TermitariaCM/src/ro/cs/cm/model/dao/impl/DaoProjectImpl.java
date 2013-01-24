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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.MessageSource;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.cm.business.BLClient;
import ro.cs.cm.business.BLPerson;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.IModelConstant;
import ro.cs.cm.common.PermissionConstant;
import ro.cs.cm.common.Tools;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.SearchProjectBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.model.dao.DaoBeanFactory;
import ro.cs.cm.model.dao.IDaoProject;
import ro.cs.cm.model.dao.IDaoProjectTeam;
import ro.cs.cm.model.dao.IDaoTeamMember;
import ro.cs.cm.om.Person;
import ro.cs.cm.utils.ProjectComparator;
import ro.cs.cm.web.security.UserAuth;


/**
 * Dao class for Project Entity
 * 
 * @author Coni
 * @author Adelina
 */
public class DaoProjectImpl extends HibernateDaoSupport implements IDaoProject {
	
	private static IDaoProjectTeam projectTeamDao = DaoBeanFactory.getInstance().getDaoProjectTeam();
	private static IDaoTeamMember teamMemberDao = DaoBeanFactory.getInstance().getDaoTeamMember();
	
	private static final String CLIENT_FROM_ORG		= "project.from.organization";	
	
	/**
	 * Searches for Projects after criterion from searchProjectBean.
	 * 
	 * @author Adelina
	 * 
	 * @param searchProjectBean
	 * @param statusIds
	 * @param isDeleteAction
	 * @return
	 */
	public List<Project> getFromSearch(SearchProjectBean searchProjectBean, Set<Byte> statusIds, Set<Integer> clientIds, boolean isDeleteAction, MessageSource messageSource) {
		logger.debug("getFromSearch - START - ");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
							
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);		
		
		if (searchProjectBean.getOrganizationId() != -1){
			dc.add(Restrictions.eq("organizationId", searchProjectBean.getOrganizationId()));
			dcCount.add(Restrictions.eq("organizationId", searchProjectBean.getOrganizationId()));
		}
						
		if(!userAuth.hasAuthority(PermissionConstant.getInstance().getCM_ProjectAdvancedSearch())){				
			// search only for projects that the user belongs to (such as manager or member)			
				
			List<TeamMember> members = new ArrayList<TeamMember>();
			
			List<ProjectTeam>projectTeams = new ArrayList<ProjectTeam>();
			
			List<Integer> projectIds = new ArrayList<Integer>();
									
			DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
			dc1.add(Restrictions.eq("personId", userAuth.getPersonId()));
			dc1.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
			members = getHibernateTemplate().findByCriteria(dc1);
			logger.debug("members = " + members);
																					
			if(members != null && members.size() > 0) {
				for(TeamMember user: members) {
					logger.debug("user = " + user);
					DetachedCriteria dcProjTeam = DetachedCriteria.forEntityName(IModelConstant.projectTeamEntity);
					dcProjTeam.add(Restrictions.eq("projectTeamId",user.getProjectTeamId()));	
					dcProjTeam.add(Restrictions.ne("status",IConstant.NOM_PROJECT_TEAM_STATUS_DELETED));
					projectTeams = getHibernateTemplate().findByCriteria(dcProjTeam);
					logger.debug("project teams = " + projectTeams);	
					if(projectTeams != null && projectTeams.size() > 0) {
						for(ProjectTeam projectTeam : projectTeams) {
							logger.debug("project team = " + projectTeam);						
							projectIds.add(projectTeam.getProjectId());
						}
					} 
				}								
			}
			
			if(projectTeams == null || projectTeams.size() == 0) {
				projectIds.add(new Integer(-1));
			}
								
			logger.debug("projectIds = " + projectIds);
			if(projectIds != null && projectIds.size() > 0) {
				dc.add(Expression.or(Expression.eq("managerId",userAuth.getPersonId()),Restrictions.in("projectId", projectIds)));
				dcCount.add(Expression.or(Expression.eq("managerId",userAuth.getPersonId()),Restrictions.in("projectId", projectIds)));
			}
		}													
											
		logger.debug("managerId = " + searchProjectBean.getManagerId());
				
		// manager		
		if(searchProjectBean.getManagerId() != null && searchProjectBean.getManagerId() != -1) {							
			dc.add(Restrictions.eq("managerId", searchProjectBean.getManagerId()));
			dcCount.add(Restrictions.eq("managerId", searchProjectBean.getManagerId()));
			logger.debug("ManagerId Id: " + searchProjectBean.getManagerId());				
		} 
				
		// name
		if(Tools.getInstance().stringNotEmpty(searchProjectBean.getName())) {
			dc.add(Restrictions.ilike("name", "%".concat(searchProjectBean.getName().concat("%"))));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchProjectBean.getName().concat("%"))));
			logger.debug("name = " + searchProjectBean.getName());
		}	
		
		logger.debug("clientId = " + searchProjectBean.getClientId());
		logger.debug("clientsIds = " + clientIds);
		
		// client
		if(searchProjectBean.getClientId() != null && searchProjectBean.getClientId() != 0) {
			dc.add(Restrictions.like("clientId", searchProjectBean.getClientId()));
			dcCount.add(Restrictions.like("clientId", searchProjectBean.getClientId()));
		} else {
			dc.add(Restrictions.in("clientId", clientIds));
			dcCount.add(Restrictions.in("clientId", clientIds));
		}
					
		//If the user didn't select at least one status then we'll make a search on all of these criterions
		// Status
		if(searchProjectBean.getStatus() != -1) {
			dc.add(Restrictions.eq("status", searchProjectBean.getStatus()));
			dcCount.add(Restrictions.eq("status", searchProjectBean.getStatus()));
		} else {
			dc.add(Restrictions.in("status", statusIds));
			dcCount.add(Restrictions.in("status", statusIds));
		}
		
		
		List<Project> res = null;
		
		//the order can be done while retrieving the data from the database only if the sort parameter is the project name or status;
		//this cannot be done if the results must be ordered by the client name
		if (searchProjectBean.getSortParam() != null && !"".equals(searchProjectBean.getSortParam()) && !"clientName".equals(searchProjectBean.getSortParam())) {
			// check if I have to order the results
			// if I have to, check if I have to order them ascending or descending
			if (searchProjectBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchProjectBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchProjectBean.getSortParam()));
			}
			
			// if the request didn't come from the pagination area, 
			// it means that I have to set the number of results and pages
			if (isDeleteAction || searchProjectBean.getNbrOfResults() == -1){
				boolean isSearch = false;
				if (searchProjectBean.getNbrOfResults() == -1 ) {
					isSearch = true;
				}
				// set the count(*) restriction			
				dcCount.setProjection(Projections.countDistinct("projectId"));
				
				//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
				//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
				int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
				logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
				searchProjectBean.setNbrOfResults(nbrOfResults);
				
				// get the number of pages
				if (nbrOfResults % searchProjectBean.getResultsPerPage() == 0) {
					searchProjectBean.setNbrOfPages(nbrOfResults / searchProjectBean.getResultsPerPage());
				} else {
					searchProjectBean.setNbrOfPages(nbrOfResults / searchProjectBean.getResultsPerPage() + 1);
				}
				// after a project is deleted, the same page has to be displayed;
				//only when all the client from last page are deleted, the previous page will be shown 
				if (isDeleteAction && (searchProjectBean.getCurrentPage() > searchProjectBean.getNbrOfPages()) ){
					searchProjectBean.setCurrentPage( searchProjectBean.getNbrOfPages() );
				} else if ( isSearch ) {
					searchProjectBean.setCurrentPage(1);
				}
			}
			
			res = (List<Project>) getHibernateTemplate().findByCriteria(dc, (searchProjectBean.getCurrentPage()-1) * searchProjectBean.getResultsPerPage(), searchProjectBean.getResultsPerPage());
		} else {
			res = (List<Project>)getHibernateTemplate().findByCriteria(dc);
			// if the request didn't come from the pagination area, 
			// it means that I have to set the number of results and pages
			if (isDeleteAction || searchProjectBean.getNbrOfResults() == -1){
				boolean isSearch = false;
				if (searchProjectBean.getNbrOfResults() == -1 ) {
					isSearch = true;
				}
				int nbrOfResults = res.size();
				logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
				searchProjectBean.setNbrOfResults(nbrOfResults);
				
				// get the number of pages
				if (nbrOfResults % searchProjectBean.getResultsPerPage() == 0) {
					searchProjectBean.setNbrOfPages(nbrOfResults / searchProjectBean.getResultsPerPage());
				} else {
					searchProjectBean.setNbrOfPages(nbrOfResults / searchProjectBean.getResultsPerPage() + 1);
				}
				// after an exchange is deleted, the same page has to be displayed;
				//only when all the exchanges from last page are deleted, the previous page will be shown 
				if (isDeleteAction && (searchProjectBean.getCurrentPage() > searchProjectBean.getNbrOfPages()) ){
					searchProjectBean.setCurrentPage( searchProjectBean.getNbrOfPages() );
				} else if ( isSearch ) {
					searchProjectBean.setCurrentPage(1);
				}
			}
		}
		
		List<Project> newRes = new ArrayList<Project>();

		if(res != null && res.size() > 0) {
			// for every project
			for(Project project : res) {				
				Project projectNew = getWithAll(project.getProjectId());				
				if(projectNew.getClientId() != null && projectNew.getClientId() > 0) {
					Client client = projectNew.getClient();
					if(client != null) {
						if(client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
							projectNew.setClientName(client.getC_name());
						} else {
							projectNew.setClientName(client.getP_firstName().concat(" ").concat(client.getP_lastName()));
						}
					}
				} else {
					projectNew.setClientName(messageSource.getMessage(CLIENT_FROM_ORG, null, null));
				}					
				
				newRes.add(projectNew);				
			}
		}	
		
		List<Project> projects = new ArrayList<Project>();
		//sorting the exchanges list
		if(searchProjectBean.getSortParam() != null && searchProjectBean.getSortParam().equals("clientName")){
			Collections.sort(newRes, ProjectComparator.getInstance().projectClientNameComparator());
			
			//ascending or descending
			if(searchProjectBean.getSortDirection() == IConstant.DESCENDING){
				Collections.reverse(newRes);
			}
							
			long start = (searchProjectBean.getCurrentPage() - 1) * searchProjectBean.getResultsPerPage();
			long end  = searchProjectBean.getCurrentPage() * searchProjectBean.getResultsPerPage();
			
			
			
			// go over the entries		
			if(newRes.size() > 0) {
				for(int i = (int)start ;i < end; i++) {
					if(i >= newRes.size()) {
						continue;
					}
					projects.add(newRes.get(i));
				}
			}
		} else {
			projects = newRes;
		}
				
		logger.debug("getFromSearch - END - results size : ".concat(String.valueOf(projects.size())));
		
		return projects;			
	}
	
	/**
	 * Get the projects identified by the clientId
	 * 
	 * @author Adelina
	 * 
	 * @param clientId
	 * @return
	 */
	public List<Project> getProjectsByClientId(Integer clientId) {
		logger.debug("getProjectsByClientId - START , clientId : ".concat(clientId.toString()));
		List<Project> projects = null;
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectWithClientEntity);
		dc.add(Restrictions.eq("client.clientId", clientId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
		projects = getHibernateTemplate().findByCriteria(dc);
		for(Project project : projects) {
			Person manager = new Person();
			try{
				manager = BLPerson.getInstance().get(project.getManagerId());
			} catch (BusinessException be) {
				logger.error("", be);					
			} catch (Exception e){
				logger.error("", e);					
			}
			// set the manager
			project.setManager(manager);
		}
		Tools.getInstance().printList(logger, projects);
		logger.debug("getProjectsByClientId - END");
		return projects;
	}
	
	@Override
	public Project addAll(Project project) {
		logger.debug("addAll - START");
				
		// set the manager for the project
		Integer managerId = project.getManagerId();
		logger.debug("managerId = " + managerId);
		Person manager = new Person();
		try{
			manager = BLPerson.getInstance().get(managerId);
		} catch (BusinessException be) {
			logger.error("", be);					
		} catch (Exception e){
			logger.error("", e);					
		}
		logger.debug("manager = " + manager);
			
		// set the manager
		project.setManager(manager);
			
		getHibernateTemplate().save(IModelConstant.projectAllEntity, project);	
		return project;
	}
	
	/**
	 * Get a project with manager
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getWithManger(Integer projectId) {
		logger.debug("getWithManger - START");
		
		Project project = (Project)getHibernateTemplate().get(IModelConstant.projectWithManagerEntity, new Integer(projectId));
		
		Integer managerId = project.getManagerId();		
		logger.debug("managerId = " + managerId);
		
		logger.debug("getWithManger - END - project with id =".concat(Integer.toString(projectId)));
		
		return project;
	}
	
	/**
	 * Get a project identified by id if it is not deleted
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getWithStatus(Integer projectId){
		logger.debug("getWithStatus - START");		
		
		List<Project> projects = new ArrayList<Project>();
		Project project = null;
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectForUpdateStatusEntity);
		dc.add(Restrictions.eq("projectId", projectId));		
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));	
		
		projects = getHibernateTemplate().findByCriteria(dc);
		if(projects != null && projects.size() > 0) {
			project = projects.get(0);
		}
			
		logger.debug("getWithStatus - END");
		return project;
	}

	@Override
	public Project getWithAll(Integer projectId) {
		logger.debug("getWithAll - START");
		Project project = (Project)getHibernateTemplate().get(IModelConstant.projectForDeleteEntity, new Integer(projectId));
		
		Integer managerId = project.getManagerId();
		
		logger.debug("managerId = " + managerId);
		Person manager = new Person();
		try{
			manager = BLPerson.getInstance().get(managerId);
		} catch (BusinessException be) {
			logger.error("", be);					
		} catch (Exception e){
			logger.error("", e);					
		}
		
		if(manager != null && manager.getStatus() != IConstant.NOM_PERSON_STATUS_DELETED) {
			// set the manager
			project.setManager(manager);
		}
		
		ProjectTeam projectTeam = project.getProjectTeam();		
		if(projectTeam != null) {
			Set<TeamMember> teamMembers = projectTeam.getTeamMembers();
			logger.debug("teamMembers = " + teamMembers);
			for(TeamMember member : teamMembers) {
				if(member.getStatus() == IConstant.NOM_TEAM_MEMBER_STATUS_DISABLED && member.getPersonId() > 0) {				
					boolean changeStatusToActivate = false;
					try {						
						Person person = BLPerson.getInstance().get(member.getPersonId());
						logger.debug("person = " + person);
						Byte activated = new Byte(IConstant.NOM_PERSON_STATUS_ACTIVATED);							
						if(person != null && activated.equals(person.getStatus())) {
							changeStatusToActivate = true;
						}
					} catch (BusinessException e) {			
						e.printStackTrace();
					}	
					logger.debug("changeStatusToActivate = " + changeStatusToActivate);
					if(changeStatusToActivate) {
						teamMemberDao.changeStatusToOpen(member);						
					}									
				} 
			}				
		}
		
		Integer clientId = project.getClientId();
		if(clientId != null && clientId > 0) {
			Client client = new Client();
			try{
				client = BLClient.getInstance().get(clientId);
			} catch (BusinessException be) {
				logger.error("", be);					
			} catch (Exception e){
				logger.error("", e);					
			}
			project.setClient(client);
		}		
			
		logger.debug("getWithAll - END - project with id =".concat(Integer.toString(projectId)));
		
		return project;
	}

	@Override
	public Project updateAll(Project project) {
		logger.debug("updateAll - START - of project with id ".concat(String.valueOf(project.getProjectId())));
		
		// set the manager for the project
		Integer managerId = project.getManagerId();
		logger.debug("managerId = " + managerId);
		Person manager = new Person();
		try{
			manager = BLPerson.getInstance().get(managerId);
		} catch (BusinessException be) {
			logger.error("", be);					
		} catch (Exception e){
			logger.error("", e);					
		}
		
		// set the manager
		project.setManager(manager);
							
		// update the project entity
		getHibernateTemplate().update(IModelConstant.projectAllEntity, project);
				
		logger.debug("updateAll - END - of project: ".concat(String.valueOf(project.getProjectId())));
		return project;
		
	}
	
	/**
	 * Get the clients for certain projects
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<Client> getClientsForProjects(Integer organizationId) {
		logger.debug("getClients - START - ");
			
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		
		List<Client> clients = new ArrayList<Client>();
					
		List<Project> projects = new ArrayList<Project>();	
		dc.add(Restrictions.eq("organizationId", organizationId));		
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));	
				
		projects = getHibernateTemplate().findByCriteria(dc);
		
		Set<Integer> clientIds = new HashSet<Integer>();
		if(projects != null && projects.size() > 0) {
			for(Project project : projects) {
				clientIds.add(project.getClientId());
			}		
			logger.debug("clientIds = " + clientIds);
			DetachedCriteria dcClient = DetachedCriteria.forEntityName(IModelConstant.clientEntity);	
			dcClient.add(Restrictions.in("clientId", clientIds));		
			dcClient.add(Restrictions.ne("status",IConstant.NOM_CLIENT_STATUS_DELETED));
			clients = getHibernateTemplate().findByCriteria(dcClient);
			logger.debug("clients = " + clients);
		}
		
		logger.debug("getClients - END - ");
		return clients;									
	}
	
	/**
	 * Finish the project
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @return
	 */
	public Project finishProject(Integer projectId) {
		logger.debug("finishProject - START ".concat(String.valueOf(projectId)));
		Project project = getWithManger(projectId);
		project.setStatus(IConstant.NOM_PROJECT_STATUS_CLOSED);
		getHibernateTemplate().update(IModelConstant.projectForUpdateStatusEntity, project);
		logger.debug("finishProject - END");
		return project;
	}
	
	/**
	 * Activate the project
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @return
	 */
	public Project activateProject(Integer projectId) {
		logger.debug("activateProject - START ".concat(String.valueOf(projectId)));
		Project project = getWithManger(projectId);
		project.setStatus(IConstant.NOM_PROJECT_STATUS_OPENED);
		getHibernateTemplate().update(IModelConstant.projectForUpdateStatusEntity, project);
		logger.debug("activateProject - END");
		return project;
	}
	
	/**
	 * Abort the project
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @return
	 */
	public Project abortProject(Integer projectId) {
		logger.debug("abortProject - START ".concat(String.valueOf(projectId)));
		Project project = getWithManger(projectId);
		project.setStatus(IConstant.NOM_PROJECT_STATUS_ABORTED);
		getHibernateTemplate().update(IModelConstant.projectForUpdateStatusEntity, project);
		logger.debug("abortProject - END");
		return project;
	}
	
	/**
	 * Update the status of the Project
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project updateStatus(Integer projectId) {
		logger.debug("updateStatus - START id : ".concat(String.valueOf(projectId)));
		
		Project project = getWithManger(projectId);
		if(project.getStatus() == IConstant.NOM_PROJECT_STATUS_OPENED) {
			project.setStatus(IConstant.NOM_PROJECT_STATUS_CLOSED);
		} else  {
			project.setStatus(IConstant.NOM_PROJECT_STATUS_OPENED);
		}
		
		getHibernateTemplate().update(IModelConstant.projectEntity, project);
		
		logger.debug("updateStatus - END");
		return project;
	}
			
	/**
	 * Get a project for delete
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getForDelete(Integer projectId) {
		logger.debug("getForDelete - START");
		
		Project project = (Project)getHibernateTemplate().get(IModelConstant.projectForDeleteEntity, new Integer(projectId));
						
		logger.debug("getForDelete - END - project with id =".concat(Integer.toString(projectId)));
		
		return project;
	}
	
	/**
	 * Delete a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project deleteAll(Integer projectId) {
		logger.debug("deleteAll - START");
		logger.debug("Deleting project with id: ".concat(projectId.toString()));
		Project project = getForDelete(projectId);
		project.setStatus(IConstant.NOM_PROJECT_STATUS_DELETED);
		ProjectTeam projectTeam = project.getProjectTeam();
		if(projectTeam != null) {
			logger.debug("projectTeam = " + projectTeam);
			projectTeam.setStatus(IConstant.NOM_PROJECT_TEAM_STATUS_DELETED);
			logger.debug("projectTeam status = " + projectTeam.getStatus());
			Set<TeamMember> members = projectTeam.getTeamMembers();
			for(TeamMember member : members) {
				member.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_DELETED);
			}
		}
		logger.debug("Deleting the project : " + project);
		getHibernateTemplate().update(IModelConstant.projectForDeleteEntity, project);
		logger.debug("Project " + project + " has been deleted");
		logger.debug("deleteAll  - END");
		return project;
	}	
	
	public Project getForView(Integer projectId) {
		logger.debug("getForView - START");		

		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectForDeleteEntity);
		
		List<Project> projects = new ArrayList<Project>();
							
		dc.add(Restrictions.eq("projectId", projectId));		
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));	
		projects = getHibernateTemplate().findByCriteria(dc);		
		
		Project project = projects.get(0);
		
		ProjectTeam projectTeam = projectTeamDao.get(projectId, true);
		project.setProjectTeam(projectTeam);
					
		Integer managerId = project.getManagerId();
		
		logger.debug("managerId = " + managerId);
		Person manager = new Person();
		try{
			manager = BLPerson.getInstance().get(managerId);
		} catch (BusinessException be) {
			logger.error("", be);					
		} catch (Exception e){
			logger.error("", e);					
		}
		// set the manager
		project.setManager(manager);
		
		Integer clientId = project.getClientId();
		if(clientId != null && clientId > 0) {
			Client client = new Client();
			try{
				client = BLClient.getInstance().get(clientId);
			} catch (BusinessException be) {
				logger.error("", be);					
			} catch (Exception e){
				logger.error("", e);					
			}
			// set the client
			project.setClient(client);
		}		
			
		logger.debug("getForView - END - project with id =".concat(Integer.toString(projectId)));
		
		return project;
	}
	
	/**
	 * Get the projects for the client
	 * 
	 * @author Adelina
	 * 
	 * @param clientId
	 * @return
	 */
	public List<Project> getClientsProjects(Integer clientId) {
		logger.debug("getClientsProjects - START , clientId : ".concat(clientId.toString()));
		List<Project> projects = null;
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectForDeleteEntity);
		dc.add(Restrictions.eq("clientId", clientId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
		projects = getHibernateTemplate().findByCriteria(dc);	
		Tools.getInstance().printList(logger, projects);
		logger.debug("getClientsProjects - END");
		return projects;
	}	
	
	/**
	 * Get all the projects by organizationId
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param isFinishedAndAbandoned
	 * @return
	 */
	public List<Project> getAllProjects(Integer organizationId, boolean isFinishedAndAbandoned) {
		logger.debug("getProjects - START");
		List<Project> projects = new ArrayList<Project>();
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		dc.add(Restrictions.eq("organizationId", organizationId));
		if(isFinishedAndAbandoned) {
			dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
		} else {			
			dc.add(Restrictions.eq("status", IConstant.NOM_PROJECT_STATUS_OPENED));
		}
		List<Project> res = getHibernateTemplate().findByCriteria(dc);
		if(res != null && res.size() > 0) {
			// for every project
			for(Project project : res) {
				Project projectNew = getWithAll(project.getProjectId());				
				projects.add(projectNew);
			}
		}
		logger.debug("getProjects - END");
		return projects;
	}
	
	/**
	 * Get projects by person
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public List<Project> getProjectsByPerson(Integer personId, boolean isFinishedAndAbandoned){
		logger.debug("getProjectsByPerson - START");
		
		logger.debug("personId = " + personId);
		
		List<Project> projects = new ArrayList<Project>();

		List<TeamMember> members = new ArrayList<TeamMember>();
		
		List<ProjectTeam>projectTeams = new ArrayList<ProjectTeam>();
		
		List<Integer> projectIds = new ArrayList<Integer>();
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
		dc.add(Restrictions.eq("personId", personId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
		members = getHibernateTemplate().findByCriteria(dc);
		logger.debug("members = " + members);
		
		if(members != null && members.size() > 0) {
			for(TeamMember user: members) {
				logger.debug("user = " + user);
				DetachedCriteria dcProjTeam = DetachedCriteria.forEntityName(IModelConstant.projectTeamEntity);
				dcProjTeam.add(Restrictions.eq("projectTeamId",user.getProjectTeamId()));	
				dcProjTeam.add(Restrictions.ne("status",IConstant.NOM_PROJECT_TEAM_STATUS_DELETED));
				projectTeams = getHibernateTemplate().findByCriteria(dcProjTeam);
				logger.debug("project teams = " + projectTeams);	
				if(projectTeams != null && projectTeams.size() > 0) {
					for(ProjectTeam projectTeam : projectTeams) {
						logger.debug("project team = " + projectTeam);						
						projectIds.add(projectTeam.getProjectId());
					}
				} 
			}
			if(projectTeams == null || projectTeams.size() == 0) {
				projectIds.add(new Integer(-1));
			}
			
		} else {			
			projectIds.add(new Integer(-1));			
		}
							
		logger.debug("projectIds = " + projectIds);
		DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		dc1.add(Expression.or(Expression.eq("managerId",personId),Restrictions.in("projectId", projectIds)));	
		
		if(isFinishedAndAbandoned) {
			dc1.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
		} else {			
			dc1.add(Restrictions.eq("status", IConstant.NOM_PROJECT_STATUS_OPENED));
		}			
						
		List<Project> res = getHibernateTemplate().findByCriteria(dc1);
		if(res != null && res.size() > 0) {
			// for every project
			for(Project project : res) {
				logger.debug("project = " + project);
				Project projectNew = getWithAll(project.getProjectId());				
				projects.add(projectNew);
			}
		}
	
		logger.debug("getProjectsByPerson - END");
		return projects;
		
	}
	
	/**
	 * Get the project for a certain manager
	 * 
	 * @author Adelina
	 * 
	 * @param managerId
	 * @return
	 */
	public List<Project> getProjectsByManager(Integer managerId, boolean isFinishedAndAbandoned) {
		logger.debug("getProjectsByManager - START");
		List<Project> projects = new ArrayList<Project>();
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		dc.add(Restrictions.eq("managerId", managerId));
		if(isFinishedAndAbandoned) {
			dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
		} else {			
			dc.add(Restrictions.eq("status", IConstant.NOM_PROJECT_STATUS_OPENED));
		}	
		List<Project> res = getHibernateTemplate().findByCriteria(dc);
		if(res != null && res.size() > 0) {
			// for every project
			for(Project project : res) {
				Project projectNew = getWithAll(project.getProjectId());				
				projects.add(projectNew);
			}
		}
		logger.debug("getProjectsByManager - END");
		return projects;
	}
			
	/**
	 * Gets the projects whose project teams include the person with the specified id
	 * 
	 * @author Coni
	 * 
	 * @param personId
	 * @return
	 * @throws BusinessException
	 */
	public List<Project> getProjectsByTeamMember(Integer personId) {
		logger.debug("getProjectsByTeamMember - START");
		List<Project> projects = null;
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectWithProjectTeamEntity);
		List<ProjectTeam> projectTeams = projectTeamDao.getByPerson(personId);
		
		if (projectTeams != null) {
			List<Integer> projectIds = new ArrayList<Integer>();
			for (ProjectTeam projectTeam : projectTeams) {
				projectIds.add(new Integer(projectTeam.getProjectId()));
			}
			dc.add(Restrictions.in("projectId", projectIds));
			dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
			projects = getHibernateTemplate().findByCriteria(dc);
		}
		logger.debug("getProjectsByTeamMember - END");
		return projects;
	}
	
	/**
	 * Returns a list of projects for the corresponding team members
	 * @author Coni
	 * @param teamMembers
	 * @return
	 */
	public ArrayList<Project> getProjectsByTeamMembers(ArrayList<TeamMember> teamMembers, boolean isFinishedAndAbandoned) {
		logger.debug("getProjectsByTeamMembers - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		ArrayList<Project> projects = null;
		
		if (teamMembers != null && teamMembers.size() > 0) {
			List<Integer> projectTeamsIds = new ArrayList<Integer>();
			for (TeamMember member : teamMembers) {
				projectTeamsIds.add(member.getProjectTeamId());
			}
			if(projectTeamsIds.size() > 0) {
				List<ProjectTeam> projectTeams = projectTeamDao.getByProjectTeamIds(projectTeamsIds);
				List<Integer> projectIds = new ArrayList<Integer>();
				for (ProjectTeam projectTeam : projectTeams) {
					projectIds.add(projectTeam.getProjectId());
				}
				if (!projectIds.isEmpty()) {
					dc.add(Restrictions.in("projectId", projectIds));
					
					if(isFinishedAndAbandoned) {
						dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
					} else {			
						dc.add(Restrictions.eq("status", IConstant.NOM_PROJECT_STATUS_OPENED));
					}					
		
					projects = (ArrayList<Project>) getHibernateTemplate().findByCriteria(dc);
					for (Project project : projects) {
						for (ProjectTeam projectTeam : projectTeams) {
							if (project.getProjectId() == projectTeam.getProjectId()) {
								project.setProjectTeam(projectTeam);
								break;
							}
						}
					}
				}
			}
		}
				
		logger.debug("getProjectsByTeamMembers - END");
		return projects;
	}
	
	/**
	 * Gets a Project with the project team by its projectId
	 * 
	 * @author Coni
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getWithProjectTeamByProjectId(int projectId, boolean isNotDeleted) {
		logger.debug("getWithProjectTeamByProjectId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		List<Project> projects = null;
		dc.add(Restrictions.eq("projectId", projectId));		
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
		projects = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getWithProjectTeamByProjectId - END , projects size = " + projects.size());
		if (projects.size() > 0) {
			Project project = projects.get(0);
			project.setProjectTeam(projectTeamDao.getWithAllTeamMembers(project.getProjectId(), isNotDeleted));
			
			Integer managerId = project.getManagerId();
			
			logger.debug("managerId = " + managerId);
			Person manager = new Person();
			try{
				manager = BLPerson.getInstance().get(managerId);
			} catch (BusinessException be) {
				logger.error("", be);					
			} catch (Exception e){
				logger.error("", e);					
			}
			// set the manager
			project.setManager(manager);
			
			Integer clientId = project.getClientId();
			if(clientId != null && clientId > 0) {
				Client client = new Client();
				try{
					client = BLClient.getInstance().get(clientId);
				} catch (BusinessException be) {
					logger.error("", be);					
				} catch (Exception e){
					logger.error("", e);					
				}
				project.setClient(client);
			}		
			
			return project;
		} else {
			return null;
		}
	}
	
	/**
	 * Gets a list of projects with the corresponding project ids
	 * @author Coni
	 * @param projectIds
	 * @return
	 * @throws BusinessException
	 */
	public List<Project> getSimpleByProjectIds(Set<Integer> projectIds) {
		logger.debug("getSimpleByProjectIds - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectAllEntity);
		
		dc.add(Restrictions.in("projectId", projectIds));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_STATUS_DELETED));
		
		List<Project> res = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getSimpleByProjectIds - END");
		return res;
	}
}
