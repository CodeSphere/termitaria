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

import java.util.List;

import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.TeamMember;


/**
 * Dao Interface, implemented by DaoProjectTeamImpl
 * @author Coni
 * @author Adelina
 *
 */
public interface IDaoProjectTeam {

	/**
	 * Get the projectTeam identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectTeam get(Integer projectId, boolean withExternalPerson);
	
	/**
	 * Adds a projectTeam to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeam
	 */
	public void add(ProjectTeam projectTeam, List<TeamMember> externalMembers);
		
	/**
	 * Updates a projectTeam to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeam
	 */
	public void update(ProjectTeam projectTeam, List<TeamMember> externalMembers);
	
	/**
	 * Checks if a project has associated a project team
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean hasProjectTeam(Integer projectId);
	
	/**
	 * Get the projectTeam for the delete action
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeamId
	 * @return
	 */
	public ProjectTeam getForDelete(Integer projectTeamId);
	

	/**
	 * Deletes a project team with all the data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeamId
	 * @return
	 */
	public ProjectTeam deleteAll(Integer projectTeamId);
	
	
	/**
	 * Gets all the project teams that have as a team member the person with the specified id 
	 * @author Coni
	 * @param personId
	 * @return
	 */
	public List<ProjectTeam> getByPerson(Integer personId);
	
	/**
	 * Get the projectTeam identified by it's id with team members from inside and outside of the organization
	 * 
	 * @author Coni
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectTeam getWithAllTeamMembers(Integer projectId, boolean isNotDeleted);
	
	/**
	 * Gets a list of project teams identified by the corresponding projectTeamIds
	 * @author Coni
	 * @param projectTeamIds
	 * @return
	 */
	public List<ProjectTeam> getByProjectTeamIds(List<Integer> projectTeamIds);
}
