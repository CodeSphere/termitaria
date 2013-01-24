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

import java.util.Set;

import ro.cs.ts.common.IConstant;

/**
 * 
 * @author Coni
 * @author Adelina
 *
 */
public class Activity {
	
	private int activityId;
	private String name;
	private Integer projectDetailId;
	private Integer projectId;
	private Character billable;
	private Float costPrice;
	private Integer costPriceCurrencyId;
	private Currency costPriceCurrency;
	private Float billingPrice;
	private Integer billingPriceCurrencyId;
	private Currency billingPriceCurrency;
	private Short costTimeUnit;
	private Short billingTimeUnit;
	private Byte status;
	private Integer organizationId;
	private String description;
	private ProjectDetails projectDetails;
	private String projectName;	// the name of the project
	private Integer projectManagerId;
	
	private Set<Record> records;
	
	private String panelHeaderName;	

	public Activity() {
		this.activityId = new Integer(-1);
		this.organizationId = new Integer(-1);		
		this.projectDetailId = new Integer(-1);
		this.billable = 'Y';
		this.status = IConstant.NOM_ACTIVITY_STATUS_ACTIVE;
	}

	/**
	 * @return the activityId
	 */
	public int getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(int activityId) {
		this.activityId = activityId;
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

	/**
	 * @return the projectDetailId
	 */
	public Integer getProjectDetailId() {
		return projectDetailId;
	}

	/**
	 * @param projectDetailId the projectDetailId to set
	 */
	public void setProjectDetailId(Integer projectDetailId) {
		this.projectDetailId = projectDetailId;
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
	 * @return the costPrice
	 */
	public Float getCostPrice() {
		return costPrice;
	}

	/**
	 * @param costPrice the costPrice to set
	 */
	public void setCostPrice(Float costPrice) {
		this.costPrice = costPrice;
	}

	/**
	 * @return the costPriceCurrency
	 */
	public Integer getCostPriceCurrencyId() {
		return costPriceCurrencyId;
	}

	/**
	 * @param costPriceCurrency the costPriceCurrency to set
	 */
	public void setCostPriceCurrencyId(Integer costPriceCurrencyId) {
		this.costPriceCurrencyId = costPriceCurrencyId;
	}

	/**
	 * @return the billingPrice
	 */
	public Float getBillingPrice() {
		return billingPrice;
	}

	/**
	 * @param billingPrice the billingPrice to set
	 */
	public void setBillingPrice(Float billingPrice) {
		this.billingPrice = billingPrice;
	}

	/**
	 * @return the billingPriceCurrency
	 */
	public Integer getBillingPriceCurrencyId() {
		return billingPriceCurrencyId;
	}

	/**
	 * @param billingPriceCurrency the billingPriceCurrency to set
	 */
	public void setBillingPriceCurrencyId(Integer billingPriceCurrencyId) {
		this.billingPriceCurrencyId = billingPriceCurrencyId;
	}

	/**
	 * @return the costTimeUnit
	 */
	public Short getCostTimeUnit() {
		return costTimeUnit;
	}

	/**
	 * @param costTimeUnit the costTimeUnit to set
	 */
	public void setCostTimeUnit(Short costTimeUnit) {
		this.costTimeUnit = costTimeUnit;
	}

	/**
	 * @return the billingTimeUnit
	 */
	public Short getBillingTimeUnit() {
		return billingTimeUnit;
	}

	/**
	 * @param billingTimeUnit the billingTimeUnit to set
	 */
	public void setBillingTimeUnit(Short billingTimeUnit) {
		this.billingTimeUnit = billingTimeUnit;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
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
	 * @return the panelHeaderName
	 */
	public String getPanelHeaderName() {
		return panelHeaderName;
	}

	/**
	 * @param panelHeaderName the panelHeaderName to set
	 */
	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}
		
	/**
	 * @return the projectDetails
	 */
	public ProjectDetails getProjectDetails() {
		return projectDetails;
	}

	/**
	 * @param projectDetails the projectDetails to set
	 */
	public void setProjectDetails(ProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}
		

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
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
		

	/**
	 * @return the records
	 */
	public Set<Record> getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(Set<Record> records) {
		this.records = records;
	}
		
	/**
	 * @return the costPriceCurrency
	 */
	public Currency getCostPriceCurrency() {
		return costPriceCurrency;
	}

	/**
	 * @param costPriceCurrency the costPriceCurrency to set
	 */
	public void setCostPriceCurrency(Currency costPriceCurrency) {
		this.costPriceCurrency = costPriceCurrency;
	}

	/**
	 * @return the billingPriceCurrency
	 */
	public Currency getBillingPriceCurrency() {
		return billingPriceCurrency;
	}

	/**
	 * @param billingPriceCurrency the billingPriceCurrency to set
	 */
	public void setBillingPriceCurrency(Currency billingPriceCurrency) {
		this.billingPriceCurrency = billingPriceCurrency;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("activityId = ")					.append(activityId)				.append(", ");
		sb.append("name = ")						.append(name)					.append(", ");		
		sb.append("projectId = ")					.append(projectId)				.append(", ");	
		sb.append("managerId = ")					.append(projectManagerId)		.append(", ");
		sb.append("organisationId = ")				.append(organizationId)			.append(", ");
		sb.append("projectDetailId = ")				.append(projectDetailId)		.append(", ");		
		sb.append("billable = ")					.append(billable)				.append(", ");	
		sb.append("costPrice = ")					.append(costPrice)				.append(", ");	
		sb.append("costPriceCurrencyId = ")			.append(costPriceCurrencyId)	.append(", ");	
		sb.append("billingPrice = ")				.append(billingPrice)			.append(", ");	
		sb.append("billingPriceCurrencyId = ")		.append(billingPriceCurrencyId)	.append(", ");	
		sb.append("costTimeUnit = ")				.append(costTimeUnit)			.append(", ");	
		sb.append("billingTimeUnit = ")				.append(billingTimeUnit)		.append(", ");			
		sb.append("status = ")						.append(status)					.append("] ");
			
		return sb.toString();
	}
	
	
}
