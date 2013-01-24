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
package ro.cs.cm.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.context.MessageSource;

import ro.cs.cm.common.Tools;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.SearchProjectBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.ICodeException;
import ro.cs.cm.model.dao.DaoBeanFactory;
import ro.cs.cm.model.dao.IDaoProject;
import ro.cs.cm.model.dao.IDaoTeamMember;

public class BLProject extends BusinessLogic{
	
	private IDaoProject projectDao = DaoBeanFactory.getInstance().getDaoProject();
	private IDaoTeamMember teamMemberDao = DaoBeanFactory.getInstance().getDaoTeamMember();

	// singleton implementation
	private static BLProject theInstance = null;
	
	private BLProject() {};
	
	static {
		theInstance = new BLProject();
	}
	
	public static BLProject getInstance() {
		return theInstance;
	}
	
	/**
	 * Searches for Projects after criterion from searchProjectBean.
	 * 
	 * @author Adelina
	 * 
	 * @param searchProjectBean
	 * @return
	 * @throws BusinessException 
	 */
	public List<Project> getFromSearch(SearchProjectBean searchProjectBean, Set<Byte> statusIds, Set<Integer> clientIds, boolean isDeleteAction, MessageSource messageSource) throws BusinessException {
		logger.debug("getFromSearch - START");
    	
    	List<Project> res = new ArrayList<Project>();    	
    	try {    		
			res = projectDao.getFromSearch(searchProjectBean, statusIds, clientIds, isDeleteAction, messageSource);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_SEARCH, e);
    	}
    	
    	logger.debug("getFromSearch - END");
    	return res;
	}
	
	/**
	 * Get a project with manager
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public Project getWithManger(Integer projectId) throws BusinessException {
		logger.debug("getWithManger - START");
		
		Project project = null;
		try{
			project = projectDao.getWithManger(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_WITH_MANAGER, e);
		}
		logger.debug("getWithManger - END - project with id =".concat(Integer.toString(projectId)));
		
		return project;
	}
		
	/**
	 * Get a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public Project getWithAll(Integer projectId) throws BusinessException {
		logger.debug("getWithAll - START - project with id =".concat(Integer.toString(projectId)));
		
		Project project = null;
		try{
			project = projectDao.getWithAll(projectId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_WITH_ALL, e);
		}
		
		logger.debug("getWithAll - END - project with id =".concat(Integer.toString(projectId)));
		
		return project;
	}
	
	/**
	 * Get a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public Project getForView(Integer projectId) throws BusinessException {
		logger.debug("getWithAll - START - project with id =".concat(Integer.toString(projectId)));
		
		Project project = null;
		try{
			project = projectDao.getForView(projectId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_WITH_ALL, e);
		}
		
		logger.debug("getWithAll - END - project with id =".concat(Integer.toString(projectId)));
		
		return project;
	}
	
		
	/**
	 * Adds a project with all necessary data
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @throws BusinessException 
	 */
	public Project addAll(Project project) throws BusinessException {
		logger.debug("addAll - START");			
		try{
			project = projectDao.addAll(project);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_ADD_WITH_ALL, e);
		}
		
		logger.debug("addAll - END - of project: ".concat(String.valueOf(project.getProjectId())));		
		return project;
	}	
	
	/**
	 * Updates a project with all necessary data
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @throws BusinessException 
	 */
	public Project updateAll(Project project) throws BusinessException {
		logger.debug("updateAll - START");
		try{
			project = projectDao.updateAll(project);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_UPDATE_WITH_ALL, e);
		}
		
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
	public List<Client> getClientsForProjects(Integer organizationId) throws BusinessException {
		logger.debug("getClients - START - ");
		List<Client> clients = new ArrayList<Client>();
		
		try{
			clients = projectDao.getClientsForProjects(organizationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_CLIENTS, e);
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
	 * @throws BusinessException 
	 */
	public Project finishProject(Integer projectId) throws BusinessException {
		logger.debug("finishProject - START ".concat(String.valueOf(projectId)));
		Project project = null;
		try{
			project = projectDao.finishProject(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_FINISH, e);
		}
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
	 * @throws BusinessException 
	 */
	public Project activateProject(Integer projectId) throws BusinessException {
		logger.debug("activateProject - START ".concat(String.valueOf(projectId)));
		Project project = null;
		try{
			project = projectDao.activateProject(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_ACTIVATE, e);
		}
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
	 * @throws BusinessException 
	 */
	public Project abortProject(Integer projectId) throws BusinessException {
		logger.debug("abortProject - START ".concat(String.valueOf(projectId)));
		Project project = null;
		try{
			project = projectDao.abortProject(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_ABORT, e);
		}
		logger.debug("abortProject - END");
		return project;
	}
		
	/**
	 * Delete a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public Project deleteAll(Integer projectId) throws BusinessException {
		logger.debug("deleteAll - START");		
		Project project = null;
		try{
			project = projectDao.deleteAll(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DELETE_ALL, e);
		}
		logger.debug("Project " + project + " has been deleted");
		logger.debug("deleteAll  - END");
		return project;
	}	
	
	/**
	 * Update the status of the Project
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param return
	 * @throws BusinessException 
	 */
	public Project updateStatus(Integer projectId) throws BusinessException {
		logger.debug("updateStatus - START id : ".concat(String.valueOf(projectId)));
		
		Project project  = null;
		
		try{
			project = projectDao.updateStatus(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_UPDATE_STATUS, e);
		}
		
		logger.debug("updateStatus - END");
		return project;
	}
	
	/**
	 * Get the projects identified by the clientId
	 * 
	 * @author Adelina
	 * 
	 * @param clientId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Project> getProjectsByClientId(Integer clientId) throws BusinessException {
		logger.debug("listProjectsForClient - START , clientId : ".concat(clientId.toString()));
		List<Project> projects = null;
		try{
			projects = projectDao.getProjectsByClientId(clientId);
		}catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_BY_CLIENT_ID, e);
		}
		logger.debug("listProjectsForClient - END");
		return projects;
	}
	
	/**
	 * Get the projects for the client
	 * 
	 * @author Adelina
	 * 
	 * @param clientId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Project> getClientsProjects(Integer clientId) throws BusinessException {
		logger.debug("getClientsProjects - START , clientId : ".concat(clientId.toString()));
		List<Project> projects = null;
		try{
			projects = projectDao.getClientsProjects(clientId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_FOR_CLIENT, e);
		}
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
	public List<Project> getAllProjects(Integer organizationId, boolean isFinishedAndAbandoned) throws BusinessException {
		logger.debug("getProjects - START");
		List<Project> projects = null;
		try{
			projects = projectDao.getAllProjects(organizationId, isFinishedAndAbandoned);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_ALL, e);
		}
		logger.debug("getProjects - END");
		return projects;
	}
	
	/**
	 * Get the project for a certain manager
	 * 
	 * @author Adelina
	 * 
	 * @param managerId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Project> getProjectsByManager(Integer managerId, boolean isFinishedAndAbandoned) throws BusinessException {
		logger.debug("getProjectsByManager - START");
		List<Project> projects = null;
		try{
			projects = projectDao.getProjectsByManager(managerId, isFinishedAndAbandoned);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_BY_MANAGER, e);
		}
		logger.debug("getProjectsByManager - END");
		return projects;
	}
	
	/**
	 * Get the projects for a certain person
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Project> getProjectsByPerson(Integer personId, boolean isFinishedAndAbandoned) throws BusinessException {
		logger.debug("getProjectsByPerson - START");
		List<Project> projects = null;
		try{
			projects = projectDao.getProjectsByPerson(personId, isFinishedAndAbandoned);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_BY_PERSON, e);
		}
		logger.debug("getProjectsByPerson - END");
		return projects;
	}
	
	/**
	 * Gets the projects whose project teams include the person with the specified id
	 * @param personId
	 * @return
	 * @throws BusinessException
	 */
	public List<Project> getProjectsByTeamMember(Integer personId) throws BusinessException {
		logger.debug("getProjectsByTeamMember - START");
		List<Project> projects = null;
		try {
			projects = projectDao.getProjectsByTeamMember(personId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_BY_TEAM_MEMBER, e);
		}
		logger.debug("getProjectsByTeamMember - END");
		return projects;
	}
	
	/**
	 * Gets the projects and corresponding team members for a person 
	 * @param personId
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<Project, TeamMember> getProjectsAndTeamMembersByPerson(Integer personId, boolean isNotDeleted, boolean isFinishedAndAbandoned) throws BusinessException {
		logger.debug("getProjectsAndTeamMembersByPerson - START");
		HashMap<Project, TeamMember> projectsAndTeamMembers = new HashMap<Project, TeamMember>();
		try {
			ArrayList<TeamMember> teamMembers = (ArrayList<TeamMember>) teamMemberDao.getByPersonId(personId, isNotDeleted);
			logger.debug("team member  = " + teamMembers);
			ArrayList<Project> projects = projectDao.getProjectsByTeamMembers(teamMembers, isFinishedAndAbandoned);
//			if (teamMembers.size() != projects.size()) {
//				throw new BusinessException(ICodeException.PROJECT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON, new Exception());
//			} else {
				if(projects != null && projects.size() > 0) {
					for (Project project : projects) {
						for (TeamMember teamMember : teamMembers) {
							if (project.getProjectTeam().getProjectTeamId() == teamMember.getProjectTeamId()) {
								projectsAndTeamMembers.put(project, teamMember);
							}
						}
					}
				}
//			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON, e);
		}
		logger.debug("getProjectsAndTeamMembersByPerson - END");
		return projectsAndTeamMembers;
	}
	
	/**
	 * Gets a Project with the project team by its projectId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public Project getWithProjectTeamByProjectId(int projectId, boolean isNotDeleted) throws BusinessException {
		logger.debug("getWithProjectTeamByProjectId - START");
		Project project = null;
		try {
			project = projectDao.getWithProjectTeamByProjectId(projectId, isNotDeleted);
		} catch (Exception e ) {
			throw new BusinessException(ICodeException.PROJECT_GET_WITH_PROJECT_TEAM_BY_PROJECT_ID, e);
		}
		logger.debug("getWithProjectTeamByProjectId - END");
		return project;
	}
	
	/**
	 * Gets a list of projects with the corresponding project ids
	 * @author Coni
	 * @param projectIds
	 * @return
	 * @throws BusinessException
	 */
	public List<Project> getSimpleByProjectIds(Set<Integer> projectIds) throws BusinessException {
		logger.debug("getSimpleByProjectIds - START");
		List<Project> projects = null;
		try {
			projects = projectDao.getSimpleByProjectIds(projectIds);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_BY_PROJECT_IDS, e);
		}
		logger.debug("getSimpleByProjectIds - END");
		return projects;
	}
	
	/**
	 * Get a project identified by id if it is not deleted
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public Project getWithStatus(Integer projectId) throws BusinessException{
		logger.debug("getWithStatus - START");		
				
		Project project = null;
		
		try{
			project = projectDao.getWithStatus(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_WITH_STATUS, e);
		}
						
		logger.debug("getWithStatus - END");
		return project;
	}
	
}


