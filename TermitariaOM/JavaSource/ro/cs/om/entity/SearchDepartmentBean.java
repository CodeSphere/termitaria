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
public class SearchDepartmentBean extends PaginationBean {

	private int 	organisationId = -1;	
	private String 	name;
	private String 	managerFirstName;
	private String  managerLastName;
	private String 	parentDepartmentName;
	private int parentDepartmentId = -1;
	private byte 	status;
	private Integer[] departmentId;
	private String branch;
	
	/**
	 * @return the parentDepartmentName
	 */
	public String getParentDepartmentName() {
		return parentDepartmentName;
	}
	/**
	 * @param parentDepartmentName the parentDepartmentName to set
	 */
	public void setParentDepartmentName(String parentDepartmentName) {
		this.parentDepartmentName = parentDepartmentName;
	}
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}	
	public String getManagerFirstName() {
		return managerFirstName;
	}
	public void setManagerFirstName(String managerFirstName) {
		this.managerFirstName = managerFirstName;
	}
	public String getManagerLastName() {
		return managerLastName;
	}
	public void setManagerLastName(String managerLastName) {
		this.managerLastName = managerLastName;
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
	 * @return the departmentId
	 */
	public Integer[] getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Integer[] departmentId) {
		this.departmentId = departmentId;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getBranch() {
		return branch;
	}
		
	/**
	 * @return the parentDepartmentId
	 */
	public int getParentDepartmentId() {
		return parentDepartmentId;
	}
	/**
	 * @param parentDepartmentId the parentDepartmentId to set
	 */
	public void setParentDepartmentId(int parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}
	
	/**
	 * @author Adelina
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("organisationId = ")		.append(organisationId)			.append(", ");		
		sb.append("name = ")				.append(name)					.append(", ");
		sb.append("managerFirstName = ")	.append(managerFirstName)		.append(", ");
		sb.append("managerLastName = ")		.append(managerLastName)		.append(", ");
		sb.append("parentDepartmentName = ").append(parentDepartmentName)	.append(", ");
		sb.append("status = ")				.append(status)					.append(", ");
		sb.append("departmentId = ")		.append(departmentId)			.append(", ");	
		sb.append("parentDepartmentId = ")	.append(parentDepartmentId)		.append(", ");
		sb.append("branch = ")				.append(branch)					.append("]");
		
		return sb.toString();
	}
}
