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
package ro.cs.cm.om;

import java.io.Serializable;

/**
 * Person entity
 * 
 * @author Adelina
 *
 */
public class Person implements Serializable {
	
	private int personId;			// the person Id
	private String firstName;		// the person's firstName
	private String lastName;		// the person's lastName		
	private boolean enabled;		// if the person is enabled or not
	private String email;			// the email of the person	
	private byte status;
	
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
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		sb.append("email = ")			.append(email)				.append(", ");
		sb.append("status = ")			.append(status)				.append(", ");
		sb.append("enabled = ")			.append(enabled)			.append("] ");				
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
			
		return true;
	}
	
	@Override
	public int hashCode(){
		int hash = 5;
		hash = 67 * hash + this.personId;				
		return hash;
	}			
}
