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
package ro.cs.cm.entity;

import java.util.HashSet;
import java.util.Set;

import ro.cs.cm.om.Person;

/**
 * 
 * @author Coni
 * @author Adelina
 */
public class ProjectTeam {
	
	private int projectTeamId;
	private Integer projectId;			
	private String observation;
	private String description;
	private Project project;
	private byte status;
	private Set<Person> persons;
	private Set<TeamMember> teamMembers;	// the working group for the project
	
	public ProjectTeam() {
		this.projectTeamId = -1;	
		this.teamMembers = new HashSet<TeamMember>();
		this.persons = new HashSet<Person>();
		this.status = 1;
	}
			
	/**
	 * @return the projectTeamId
	 */
	public int getProjectTeamId() {
		return projectTeamId;
	}

	/**
	 * @param projectTeamId the projectTeamId to set
	 */
	public void setProjectTeamId(int projectTeamId) {
		this.projectTeamId = projectTeamId;
	}

	/**
	 * @return the projectId
	 */
	public Integer getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * @return the persons
	 */
	public Set<Person> getPersons() {
		return persons;
	}

	/**
	 * @param persons the persons to set
	 */
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	/**
	 * @return the teamMembers
	 */
	public Set<TeamMember> getTeamMembers() {
		return teamMembers;
	}

	/**
	 * @param teamMembers the teamMembers to set
	 */
	public void setTeamMembers(Set<TeamMember> teamMembers) {
		this.teamMembers = teamMembers;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "ProjectTeam [description=" + description + "," 
				+ ", observation=" + observation 
				+ ", projectId=" + projectId + ", projectTeamId="
				+ projectTeamId + ", status=" + status + "]";
	}	
	
}
