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

import java.util.Date;

public class SearchRecordBean extends PaginationBean {

	private Integer[] recordId; //used for delete purposes
	private Integer id; //the recordId
	private int teamMemberId;
	private int projectId;
	private int activityId;
	private Date startDate; 	//start time for activities performed during work hours or over time
	private Date endDate;		//end time for activities performed during work hours or over time
	private Character billable = new Character(' ');
	private String firstName;
	private String lastName;
	private Integer organizationId;
	private Integer week;
	private Integer month;
	
	
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer[] getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer[] recordId) {
		this.recordId = recordId;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Character getBillable() {
		return billable;
	}
	public void setBillable(Character billable) {
		this.billable = billable;
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
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public int getTeamMemberId() {
		return teamMemberId;
	}
	public void setTeamMemberId(int teamMemberId) {
		this.teamMemberId = teamMemberId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @author Coni
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("recordId = ")				.append(recordId)				.append(", ");
		sb.append("id = ")						.append(id)						.append(", ");	
		sb.append("teamMemberId = ")			.append(teamMemberId)			.append(", ");
		sb.append("projectId = ")				.append(projectId)				.append(", ");
		sb.append("activityId = ")				.append(activityId)				.append(", ");
		sb.append("startDate = ")				.append(startDate)				.append(", ");
		sb.append("endDate = ")					.append(endDate)				.append(", ");
		sb.append("firstName = ")				.append(firstName)				.append(", ");
		sb.append("lastName = ")				.append(lastName)				.append(", ");
		sb.append("organizationId = ")			.append(organizationId)			.append(", ");
		sb.append("billable = ")				.append(billable)				.append("] ");
		
		return sb.toString();		
	}
	
}
