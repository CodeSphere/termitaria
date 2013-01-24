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
package ro.cs.ts.entity;

/**
 * 
 * @author Adelina
 *
 */
public class SearchActivityBean extends PaginationBean {
	
	private Integer[] activityId;
	private String name;
	private Integer projectId;
	private Integer organizationId;
	private Character billable = new Character(' ');
	/**
	 * @return the activityId
	 */
	public Integer[] getActivityId() {
		return activityId;
	}
	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(Integer[] activityId) {
		this.activityId = activityId;
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
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	/**
	 * @return the billable
	 */
	public Character getBillable() {
		return billable;
	}
	/**
	 * @param billable the billable to set
	 */
	public void setBillable(Character billable) {
		this.billable = billable;
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
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("activityId = ")				.append(activityId)				.append(", ");	
		sb.append("name = ")					.append(name)					.append(", ");
		sb.append("projectId = ")				.append(projectId)				.append(", ");
		sb.append("organizationId = ")			.append(organizationId)			.append(", ");
		sb.append("billable = ")				.append(billable)				.append("] ");		
		
		return sb.toString();		
	}
	
}
