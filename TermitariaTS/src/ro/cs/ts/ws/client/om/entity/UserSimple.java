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
package ro.cs.ts.ws.client.om.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *  UserSimple
 *	
 *  @author dan.damian
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userSimple", propOrder = {

})
public class UserSimple {
	
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private int userId;

	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private String username;
	
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private String firstName;
	
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private String lastName;
	
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private String email;
	
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private byte status;
	
		
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
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
		sb.append("id = ")				.append(userId)		  	.append(", ");	
		sb.append("status = ")			.append(status)		  	.append(", ");	
		sb.append("firstName = ")		.append(firstName)		.append(", ");	
		sb.append("lastName = ")        .append(lastName)		.append(", ");		
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
		final UserSimple other = (UserSimple) obj;
		if (this.userId != other.userId) {
			return false;
		}				
		return true;
	}
	
	@Override
	public int hashCode(){
		int hash = 5;
		hash = 67 * hash + this.userId;		
		return hash;
	}			
	
	
}
