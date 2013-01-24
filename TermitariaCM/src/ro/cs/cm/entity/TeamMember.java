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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 
 * @author Coni
 * @author Adelina
 *
 */
public class TeamMember implements Comparable {
			
	private int memberId;		
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String phone;
	private String observation;
	private String description;
	private byte status;
	private int projectTeamId;
	private int personId;
	private ProjectTeam projectTeam;
	private int idExternal;	
		
	/**
	 * @return the idExternal
	 */
	public int getIdExternal() {
		return idExternal;
	}

	/**
	 * @param idExternal the idExternal to set
	 */
	public void setIdExternal(int idExternal) {
		this.idExternal = idExternal;
	}

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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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

	@Override
	public String toString() {
		return "TeamMember [address=" + address + ", description="
				+ description + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", memberId=" + memberId
				+ ", observation=" + observation + ", personId=" + personId
				+ ", phone=" + phone + ", projectTeamId=" + projectTeamId
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
			if (this.status != other.status) {
				return false;
			}			
			return true;
		}
		
	 @Override
	public int hashCode(){		
		int hash = 5;
		hash = 67 * hash + this.memberId;				
		hash = 67 * hash + byteToInt(this.status);			
		return hash;
	}		
	 
	 public int byteToInt(byte b) {
		 return (int) b & 0xFF;
	 }	
	 
	 // In this method we compare all the two teamMembers objects.
     public int compareTo(Object obj) {         
         TeamMember teamMember = (TeamMember) obj;        
         if (this.memberId == teamMember.getMemberId() && this.firstName.equals(teamMember.getFirstName()) && this.lastName.equals(teamMember.getLastName())
        		 && this.email.equals(teamMember.getEmail()) && this.address.equals(teamMember.getAddress())
        		 && this.observation.equals(teamMember.getObservation()) && this.description.equals(teamMember.getDescription())  
        		 && this.phone.equals(teamMember.getPhone()) && this.status == teamMember.getStatus())
             return 0;
         else return -1;
     }   
}
