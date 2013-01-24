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

import ro.cs.ts.common.IConstant;

/**
 * 
 * @author Coni
 *
 */
public class CostSheet {
	
	private int costSheetId;
	private Integer teamMemberDetailId;
	private Integer projectDetailId;
	private Integer personDetailId;
	private String activityName;
	private Date date;
	private Float costPrice;
	private Integer costPriceCurrencyId;   // currency id
	private Float billingPrice;
	private Integer billingPriceCurrencyId;  // currency id
	private Character billable;
	private String observation;
	private String description;
	private int organizationId;
	private String costSheetReporterName;  //the name of the person that reported the cost
	private String panelHeaderName;	// header name for the info panel
	private String projectName;
	private Integer userId;
	private Integer projectId;
	private ProjectDetails projectDetails;
	private TeamMemberDetail teamMemberDetail;
	private PersonDetail personDetail;
	private byte status;
	private Currency costPriceCurrency;
	private Currency billingPriceCurrency;
	private Integer projectManagerId;
	
	public CostSheet() {
		this.costSheetId = -1;
		this.status = IConstant.NOM_COST_SHEET_STATUS_ACTIVE;
		this.billable = new Character('Y');
	}

	public int getCostSheetId() {
		return costSheetId;
	}

	public void setCostSheetId(int costSheetId) {
		this.costSheetId = costSheetId;
	}

	public Integer getTeamMemberDetailId() {
		return teamMemberDetailId;
	}

	public void setTeamMemberDetailId(Integer teamMemberDetailId) {
		this.teamMemberDetailId = teamMemberDetailId;
	}

	public Integer getProjectDetailId() {
		return projectDetailId;
	}

	public void setProjectDetailId(Integer projectDetailId) {
		this.projectDetailId = projectDetailId;
	}

	public Integer getPersonDetailId() {
		return personDetailId;
	}

	public void setPersonDetailId(Integer personDetailId) {
		this.personDetailId = personDetailId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Float costPrice) {
		this.costPrice = costPrice;
	}

	public Float getBillingPrice() {
		return billingPrice;
	}

	public void setBillingPrice(Float billingPrice) {
		this.billingPrice = billingPrice;
	}

	public Character getBillable() {
		return billable;
	}

	public void setBillable(Character billable) {
		this.billable = billable;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public String getCostSheetReporterName() {
		return costSheetReporterName;
	}

	public void setCostSheetReporterName(String costSheetReporterName) {
		this.costSheetReporterName = costSheetReporterName;
	}

	public String getPanelHeaderName() {
		return panelHeaderName;
	}

	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public ProjectDetails getProjectDetails() {
		return projectDetails;
	}

	public void setProjectDetails(ProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}

	public TeamMemberDetail getTeamMemberDetail() {
		return teamMemberDetail;
	}

	public void setTeamMemberDetail(TeamMemberDetail teamMemberDetail) {
		this.teamMemberDetail = teamMemberDetail;
	}

	public PersonDetail getPersonDetail() {
		return personDetail;
	}

	public void setPersonDetail(PersonDetail personDetail) {
		this.personDetail = personDetail;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Integer getCostPriceCurrencyId() {
		return costPriceCurrencyId;
	}

	public void setCostPriceCurrencyId(Integer costPriceCurrencyId) {
		this.costPriceCurrencyId = costPriceCurrencyId;
	}

	public Integer getBillingPriceCurrencyId() {
		return billingPriceCurrencyId;
	}

	public void setBillingPriceCurrencyId(Integer billingPriceCurrencyId) {
		this.billingPriceCurrencyId = billingPriceCurrencyId;
	}

	public Currency getCostPriceCurrency() {
		return costPriceCurrency;
	}

	public void setCostPriceCurrency(Currency costPriceCurrency) {
		this.costPriceCurrency = costPriceCurrency;
	}

	public Currency getBillingPriceCurrency() {
		return billingPriceCurrency;
	}

	public void setBillingPriceCurrency(Currency billingPriceCurrency) {
		this.billingPriceCurrency = billingPriceCurrency;
	}

	/**
	 * @return the projectManagerId
	 */
	public Integer getProjectManagerId() {
		return projectManagerId;
	}

	/**
	 * @param projectManagerId the projectManagerId to set
	 */
	public void setProjectManagerId(Integer projectManagerId) {
		this.projectManagerId = projectManagerId;
	}	
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("costSheetId = ")						.append(costSheetId)					.append(", ");
		sb.append("teamMemberDetailId = ")				.append(teamMemberDetailId)				.append(", ");		
		sb.append("personDetailId = ")					.append(personDetailId)					.append(", ");				
		sb.append("projectDetailId = ")					.append(projectDetailId)				.append(", ");			
		sb.append("activityName = ")					.append(activityName)					.append(", ");
		sb.append("date = ")							.append(date)							.append(", ");		
		sb.append("costPrice = ")						.append(costPrice)						.append(", ");	
		sb.append("costPriceCurrencyId = ")				.append(costPriceCurrencyId)			.append(", ");	
		sb.append("billingPrice = ")					.append(billingPrice)					.append(", ");	
		sb.append("billingPriceCurrencyId = ")			.append(billingPriceCurrencyId)			.append(", ");	
		sb.append("billable = ")						.append(billable)						.append(", ");	
		sb.append("costSheetReporterName = ")			.append(costSheetReporterName)			.append(", ");	
		sb.append("projectName  = ")					.append(projectName)					.append(", ");	
		sb.append("userId = ")							.append(userId)							.append(", ");	
		sb.append("projectId = ")						.append(projectId)						.append(", ");	
		sb.append("teamMemberDetail = ")				.append(teamMemberDetail)				.append(", ");	
		sb.append("personDetail = ")					.append(personDetail)					.append(", ");	
		sb.append("projectDetails = ")					.append(projectDetails)					.append(", ");	
		sb.append("projectId = ")						.append(projectId)						.append(" ] ");		
		
		return sb.toString();
	}

	
}
