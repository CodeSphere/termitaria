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
package ro.cs.om.entity;

/**
 * @author dd
 * 
 */
public class SearchPersonBean extends PaginationBean {

	private String 	firstName;
	private String 	lastName;
	private String username;
	private int organisationId;
	private int departmentId;
	private Character sex = new Character(' ');
	private Integer[] personId;
	private String branch;
	
	/**
	 * @return the organisationId
	 */
	public int getOrganisationId() {
		return organisationId;
	}
	/**
	 * @param organisationId the organisationId to set
	 */
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}
	/**
	 * @return the sex
	 */
	public Character getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(Character sex) {
		this.sex = sex;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the personId
	 */
	public Integer[] getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(Integer[] personId) {
		this.personId = personId;
	}
	/**
	 * @return the departmentId
	 */
	public int getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getBranch() {
		return branch;
	}	
	
	/**
	 * @return the userName
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @author Adelina
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("firstName = ")		.append(firstName)			.append(", ");		
		sb.append("lastName = ")		.append(lastName)			.append(", ");
		sb.append("username = ")		.append(username)			.append(", ");
		sb.append("organisationId = ")	.append(organisationId)		.append(", ");
		sb.append("departmentId = ")	.append(departmentId)		.append(", ");
		sb.append("sex = ")				.append(sex)				.append(", ");
		sb.append("personId = ")		.append(personId)			.append(", ");
		sb.append("branch = ")			.append(branch)				.append("] ");	
		
		return sb.toString();
	}
}
