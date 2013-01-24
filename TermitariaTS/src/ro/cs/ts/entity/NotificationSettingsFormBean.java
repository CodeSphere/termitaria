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

public class NotificationSettingsFormBean {
	
	private Integer projectId;
	private Integer projectDetailId;
	private Integer userId;
	private Integer organizationId;
	private boolean recordAdd;
	private boolean recordDelete;
	private boolean recordUpdate;
	private boolean costAdd;
	private boolean costDelete;
	private boolean costUpdate;
	private boolean teamMemberDetailUpdate;
	private boolean personDetailUpdate;
	private boolean budgetOverflow;
	private boolean percentageOverflow;
	private boolean activityAdd;
	private boolean activityDelete;
	private boolean activityUpdate;
	private boolean exchangeAdd;
	private boolean exchangeDelete;
	private boolean exchangeUpdate;
	private boolean projectDetailUpdate;
	
	
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("projectId = ")				.append(projectId)		    	.append(", ");
		sb.append("projectDetailId = ")			.append(projectDetailId)		.append(", ");
		sb.append("userId = ")					.append(userId)		    		.append(", ");
		sb.append("organizationId = ")			.append(organizationId)		    .append(", ");
		
		sb.append("recordAdd = ")				.append(recordAdd)		    	.append(", ");
		sb.append("recordDelete = ")			.append(recordDelete)		    .append(", ");
		sb.append("recordUpdate = ")			.append(recordUpdate)		    .append(", ");
		
		sb.append("costAdd = ")					.append(costAdd)		   		.append(", ");
		sb.append("costDelete = ")				.append(costDelete)		   		.append(", ");
		sb.append("costUpdate = ")			    .append(costUpdate)		    	.append(", ");
		
		sb.append("teamMemberDetailUpdate = ")	.append(teamMemberDetailUpdate)	.append(", ");
		sb.append("personDetailUpdate = ")		.append(personDetailUpdate)		.append(", ");
		sb.append("budgetOverflow = ")			.append(budgetOverflow)		    .append(", ");
		sb.append("percentageOverflow = ")		.append(percentageOverflow)		.append(", ");
		sb.append("activityAdd = ")				.append(activityAdd)		    .append(", ");
		sb.append("activityDelete = ")			.append(activityDelete)		    .append(", ");
		sb.append("activityUpdate = ")			.append(activityUpdate)		    .append(", ");
		sb.append("exchangeAdd = ")				.append(exchangeAdd)		    .append(", ");
		sb.append("exchangeDelete = ")			.append(exchangeDelete)		    .append(", ");
		sb.append("exchangeUpdate = ")			.append(exchangeUpdate)		    .append(", ");
		sb.append("projectDetailUpdate = ")		.append(projectDetailUpdate)	.append("] ");
		
		return sb.toString();
	}
	
	
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getProjectDetailId() {
		return projectDetailId;
	}
	public void setProjectDetailId(Integer projectDetailId) {
		this.projectDetailId = projectDetailId;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public boolean isRecordAdd() {
		return recordAdd;
	}
	public void setRecordAdd(boolean recordAdd) {
		this.recordAdd = recordAdd;
	}
	public boolean isRecordDelete() {
		return recordDelete;
	}
	public void setRecordDelete(boolean recordDelete) {
		this.recordDelete = recordDelete;
	}
	public boolean isRecordUpdate() {
		return recordUpdate;
	}
	public void setRecordUpdate(boolean recordUpdate) {
		this.recordUpdate = recordUpdate;
	}
	public boolean isCostAdd() {
		return costAdd;
	}
	public void setCostAdd(boolean costAdd) {
		this.costAdd = costAdd;
	}
	public boolean isCostDelete() {
		return costDelete;
	}
	public void setCostDelete(boolean costDelete) {
		this.costDelete = costDelete;
	}
	public boolean isCostUpdate() {
		return costUpdate;
	}
	public void setCostUpdate(boolean costUpdate) {
		this.costUpdate = costUpdate;
	}
	public boolean isTeamMemberDetailUpdate() {
		return teamMemberDetailUpdate;
	}
	public void setTeamMemberDetailUpdate(boolean teamMemberDetailUpdate) {
		this.teamMemberDetailUpdate = teamMemberDetailUpdate;
	}
	public boolean isPersonDetailUpdate() {
		return personDetailUpdate;
	}
	public void setPersonDetailUpdate(boolean personDetailUpdate) {
		this.personDetailUpdate = personDetailUpdate;
	}
	public boolean isBudgetOverflow() {
		return budgetOverflow;
	}
	public void setBudgetOverflow(boolean budgetOverflow) {
		this.budgetOverflow = budgetOverflow;
	}
	public boolean isPercentageOverflow() {
		return percentageOverflow;
	}
	public void setPercentageOverflow(boolean percentageOverflow) {
		this.percentageOverflow = percentageOverflow;
	}


	public boolean isActivityAdd() {
		return activityAdd;
	}


	public void setActivityAdd(boolean activityAdd) {
		this.activityAdd = activityAdd;
	}


	public boolean isActivityDelete() {
		return activityDelete;
	}


	public void setActivityDelete(boolean activityDelete) {
		this.activityDelete = activityDelete;
	}


	public boolean isActivityUpdate() {
		return activityUpdate;
	}


	public void setActivityUpdate(boolean activityUpdate) {
		this.activityUpdate = activityUpdate;
	}


	public boolean isExchangeAdd() {
		return exchangeAdd;
	}


	public void setExchangeAdd(boolean exchangeAdd) {
		this.exchangeAdd = exchangeAdd;
	}


	public boolean isExchangeDelete() {
		return exchangeDelete;
	}


	public void setExchangeDelete(boolean exchangeDelete) {
		this.exchangeDelete = exchangeDelete;
	}


	public boolean isExchangeUpdate() {
		return exchangeUpdate;
	}


	public void setExchangeUpdate(boolean exchangeUpdate) {
		this.exchangeUpdate = exchangeUpdate;
	}


	public boolean isProjectDetailUpdate() {
		return projectDetailUpdate;
	}


	public void setProjectDetailUpdate(boolean projectDetailUpdate) {
		this.projectDetailUpdate = projectDetailUpdate;
	}
	

}
