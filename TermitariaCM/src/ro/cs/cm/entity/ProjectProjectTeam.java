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

/**
 * Bean for the Project with Project Team 
 * 
 * @author Adelina
 *
 */
public class ProjectProjectTeam {
	
	private Project project;
	private ProjectTeam projectTeam;
	
	public ProjectProjectTeam() {
		project = new Project();
		projectTeam = new ProjectTeam();
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return the projectTeam
	 */
	public ProjectTeam getProjectTeam() {
		return projectTeam;
	}

	/**
	 * @param projectTeam the projectTeam to set
	 */
	public void setProjectTeam(ProjectTeam projectTeam) {
		this.projectTeam = projectTeam;
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("project = ")				.append(project)		.append(", ");	
		sb.append("projectTeam = ")			.append(projectTeam)	.append("] ");	
		
		return sb.toString();
	}

}
