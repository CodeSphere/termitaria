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
package ro.cs.ts.cm;

import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.exception.BusinessException;

public class TeamMember {
	
	private int memberId;
	private int projectTeamId;
	private int personId;
	private String firstName;
	private String lastName;
	private String email;
	private byte status;
	private boolean hasTeamMemberDetail;
	private ProjectTeam projectTeam;
	
	private String panelHeaderName;	// header name for the info panel

	/**
	 * @return the memberId
	 */
	public int getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(int memberId) {
		this.memberId = memberId;
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

	@Override
	public String toString() {
		return "TeamMember [firstName=" + firstName
				+ ", lastName=" + lastName + ", memberId=" + memberId
				+ ", personId=" + personId
				+ ", projectTeamId=" + projectTeamId
				+ ", status=" + status + "]";
	}
	
	@Override
    public boolean equals (Object obj){
    	if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TeamMember other = (TeamMember) obj;			
		if (this.memberId != other.memberId) {
			return false;
		}			
		return true;
	}
		
	 @Override
	public int hashCode(){		
		int hash = 5;
		hash = 67 * hash + this.memberId;						
		return hash;
	}		

}
