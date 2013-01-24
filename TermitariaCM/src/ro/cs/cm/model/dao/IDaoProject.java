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
package ro.cs.cm.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.MessageSource;

import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.SearchProjectBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;


/**
 * Dao Interface, implemented by DaoProjectImpl
 * @author Coni
 * @author Adelina
 *
 */
public interface IDaoProject {

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
	public List<Project> getFromSearch(SearchProjectBean searchProjectBean, Set<Byte> statusIds, Set<Integer> clientIds, boolean isDeleteAction, MessageSource messageSource);
	
	/**
	 * Get a project with manager
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getWithManger(Integer projectId);
	
	/**
	 * Get a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getWithAll (Integer projectId);
	
	/**
	 * Add a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 */
	public Project addAll(Project project);
	
	/**
	 * Update a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 */	
	public Project updateAll(Project project);
	
	/**
	 * Get the clients for certain projects
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<Client> getClientsForProjects(Integer organizationId);
	
	/**
	 * Finish the project
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @return
	 */
	public Project finishProject(Integer projectId);
	
	/**
	 * Activate the project
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @return
	 */
	public Project activateProject(Integer projectId);
	
	/**
	 * Abort the project
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @return
	 */
	public Project abortProject(Integer projectId);
	
	/**
	 * Get a project for delete
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getForDelete(Integer projectId);
	
	/**
	 * Delete a project with all data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project deleteAll(Integer projectId);

	/**
	 * Update the status of the Project
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project updateStatus(Integer projectId);
	
	/**
	 * Get the Project for the view display
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getForView(Integer projectId);
	
	/**
	 * Get the projects identified by the clientId
	 * 
	 * @author Adelina
	 * 
	 * @param clientId
	 * @return
	 */
	public List<Project> getProjectsByClientId(Integer clientId);
	
	/**
	 * Get the projects for the client
	 * 
	 * @author Adelina
	 * 
	 * @param clientId
	 * @return
	 */
	public List<Project> getClientsProjects(Integer clientId);
	
	/**
	 * Get all the projects by organizationId
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param isFinishedAndAbandoned
	 * @return
	 */
	public List<Project> getAllProjects(Integer organizationId, boolean isFinishedAndAbandoned);
	
	/**
	 * Get the project for a certain manager
	 * 
	 * @author Adelina
	 * 
	 * @param managerId
	 * @return
	 */
	public List<Project> getProjectsByManager(Integer managerId, boolean isFinishedAndAbandoned);
	
	/**
	 * Gets the projects whose project teams include the person with the specified id
	 * @param personId
	 * @return
	 * @throws BusinessException
	 */
	public List<Project> getProjectsByTeamMember(Integer personId);
	
	/**
	 * Returns a list of projects for the corresponding team members
	 * @author Coni
	 * @param teamMembers
	 * @return
	 */
	public ArrayList<Project> getProjectsByTeamMembers(ArrayList<TeamMember> teamMembers, boolean isFinishedAndAbandoned);
	
	/**
	 * Gets a Project with the project team by its projectId
	 * @author Coni
	 * @param projectId
	 * @return
	 */
	public Project getWithProjectTeamByProjectId(int projectId, boolean isNotDeleted);
	
	/**
	 * Gets a list of projects with the corresponding project ids
	 * @author Coni
	 * @param projectIds
	 * @return
	 * @throws BusinessException
	 */
	public List<Project> getSimpleByProjectIds(Set<Integer> projectIds);
	
	/**
	 * Get projects by person
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public List<Project> getProjectsByPerson(Integer personId, boolean isFinishedAndAbandoned);
	
	/**
	 * Get a project identified by id if it is not deleted
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public Project getWithStatus(Integer projectId);
	
}
