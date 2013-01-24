/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.om;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.cm.Project;
import ro.cs.ts.exception.BusinessException;

/**
 * Person entity
 * 
 * @author Adelina
 *
 */
public class Person implements Serializable{	
	
	private int personId;			// the person Id
	private Integer memberId;		// if the person belongs to a project
	private String firstName;		// the person's firstName
	private String lastName;		// the person's lastName			
	private String email;			// the email of the person
	private Set<Integer> projectIds;// the ids of the projects the user belongs to
	private List<Project> projects;	// the projects the person belongs to
	private Project project; 		// the certain project a person belongs to	
	private String projectName;		// the name of the project, the person belongs to
	private Integer projectId;		// the certain project id a person belongs to
	boolean hasPersonDetail;		// the person detail
	boolean hasTeamMemberDetail;	// the team member details, if the person belongs to a project	
	boolean hasManager;				// if the person has a manager
	byte status; 
	
	private String panelHeaderName;	// header name for the info panel
	
	public Person() {
		
	}

	/**
	 * @return the personId
	 */
	public int getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
		
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}	

	/**
	 * @return the projectIds
	 */
	public Set<Integer> getProjectIds() {
		return projectIds;
	}

	/**
	 * @param projectIds the projectIds to set
	 */
	public void setProjectIds(Set<Integer> projectIds) {
		this.projectIds = projectIds;
	}			

	/**
	 * @return the projects
	 */
	public List<Project> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
		
	/**
	 * @return the hasPersonDetail
	 */
	public boolean isHasPersonDetail() {
		try{
			hasPersonDetail = BLPersonDetail.getInstance().hasPersonDetail(getPersonId());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return hasPersonDetail;
	}
		
	/**
	 * @param hasPersonDetail the hasPersonDetail to set
	 */
	public void setHasPersonDetail(boolean hasPersonDetail) {
		this.hasPersonDetail = hasPersonDetail;
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
	 * @return the memberId
	 */
	public Integer getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the hasTeamMemberDetail
	 */
	public boolean isHasTeamMemberDetail() {
		try{
			hasTeamMemberDetail = BLTeamMemberDetail.getInstance().hasTeamMemberDetail(getMemberId());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return hasTeamMemberDetail;
	}

	/**
	 * @param hasTeamMemberDetail the hasTeamMemberDetail to set
	 */
	public void setHasTeamMemberDetail(boolean hasTeamMemberDetail) {
		this.hasTeamMemberDetail = hasTeamMemberDetail;
	}	

	/**
	 * @return the panelHeaderName
	 */
	public String getPanelHeaderName() {
		return panelHeaderName;
	}

	/**
	 * @param panelHeaderName the panelHeaderName to set
	 */
	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}
	
	/**
	 * @return the hasManager
	 */
	public boolean isHasManager() {
		return hasManager;
	}

	/**
	 * @param hasManager the hasManager to set
	 */
	public void setHasManager(boolean hasManager) {
		this.hasManager = hasManager;
	}
	
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("personId = ")		.append(personId)			.append(", ");
		sb.append("firstName = ")		.append(firstName)			.append(", ");		
		sb.append("lastName = ")		.append(lastName)			.append(", ");	
		sb.append("projectId = ")		.append(projectId)			.append(", ");		
		sb.append("project = ")			.append(project)			.append(", ");
		sb.append("status = ")			.append(status)				.append(", ");
		sb.append("projectIds = ")		.append(projectIds)			.append("] ");				
		return sb.toString();
	}
    
    @Override
    public boolean equals (Object obj){
    	if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Person other = (Person) obj;
		if (this.personId != other.personId) {
			return false;
		}
		if ((this.projectId == null) ? (other.projectId != null)
				: !this.projectId.equals(other.projectId)) {
			return false;
		}	
			
		return true;
	}
	
	@Override
	public int hashCode(){
		int hash = 5;
		hash = 67 * hash + this.personId;	
		hash = 67 * hash + (this.projectId != null ? this.projectId.hashCode() : 0);
		return hash;
	}	
    
      
}
