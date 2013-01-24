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

public class SearchCostSheetBean extends PaginationBean {

	private Integer[] costSheetId;
	private Integer id;
	private int teamMemberId;
	private int projectId;
	private String firstName;
	private String lastName;
	private Integer organizationId;
	private String activityName;
	private Date startDate;
	private Date endDate;
	private Character billable = new Character(' ');
	private Float costPriceMin;
	private Float costPriceMax;
	private Integer costPriceCurrencyId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
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
	public Float getCostPriceMin() {
		return costPriceMin;
	}
	public void setCostPriceMin(Float costPriceMin) {
		this.costPriceMin = costPriceMin;
	}
	public Float getCostPriceMax() {
		return costPriceMax;
	}
	public void setCostPriceMax(Float costPriceMax) {
		this.costPriceMax = costPriceMax;
	}
	public Integer[] getCostSheetId() {
		return costSheetId;
	}
	public void setCostSheetId(Integer[] costSheetId) {
		this.costSheetId = costSheetId;
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
	
	public Integer getCostPriceCurrencyId() {
		return costPriceCurrencyId;
	}
	public void setCostPriceCurrencyId(Integer costPriceCurrencyId) {
		this.costPriceCurrencyId = costPriceCurrencyId;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("costSheetId = ")    .append(costSheetId)    .append(", ");
		sb.append("id = ")             .append(id)             .append(", ");
		sb.append("teamMemberId = ")   .append(teamMemberId)   .append(", ");
		sb.append("projectId = ")      .append(projectId)      .append(", ");
		sb.append("firstName = ")      .append(firstName)      .append(", ");
		sb.append("lastName = ")       .append(lastName)       .append(", ");
		sb.append("organizationId = ") .append(organizationId) .append(", ");
		sb.append("activityName = ")   .append(activityName)   .append(", ");
		sb.append("startDate = ")      .append(startDate)      .append(", ");
		sb.append("endDate = ")        .append(endDate)        .append(", ");
		sb.append("billable = ")       .append(billable)       .append(", ");
		sb.append("costPriceMin = ")   .append(costPriceMin)   .append(", ");
		sb.append("costPriceMax = ")   .append(costPriceMax)   .append(", ");
		sb.append("costPriceCurrencyId = ").append(costPriceCurrencyId).append("]");
	return sb.toString();
	}
	
}
