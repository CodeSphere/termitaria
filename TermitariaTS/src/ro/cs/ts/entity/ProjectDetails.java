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

import java.io.Serializable;


/**
 * The details for the project
 * 
 * @author coni
 * @author Adelina
 *
 */
public class ProjectDetails implements Serializable {
	
	private int projectDetailId;
	private Integer projectId;	
	private Float budget;
	private Float oldBudget;
	private Integer budgetCurrencyId;	
	private Short notificationPercentage;
	private Short oldNotificationPercentage;
	private Short completenessPercentage;
	private String observation;
	private byte status;
	private Integer oldBudgetCurrencyId;
	private Byte notificationStatus;
	
	public ProjectDetails() {
		
	}
		
	/**
	 * @return the projectDetailId
	 */
	public int getProjectDetailId() {
		return projectDetailId;
	}

	/**
	 * @param projectDetailId the projectDetailId to set
	 */
	public void setProjectDetailId(int projectDetailId) {
		this.projectDetailId = projectDetailId;
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
	 * @return the budget
	 */
	public Float getBudget() {
		return budget;
	}

	/**
	 * @param budget the budget to set
	 */
	public void setBudget(Float budget) {
		this.budget = budget;
	}

	/**
	 * @return the notificationPercentage
	 */
	public Short getNotificationPercentage() {
		return notificationPercentage;
	}

	/**
	 * @param notificationPercentage the notificationPercentage to set
	 */
	public void setNotificationPercentage(Short notificationPercentage) {
		this.notificationPercentage = notificationPercentage;
	}

	/**
	 * @return the completenessPercentage
	 */
	public Short getCompletenessPercentage() {
		return completenessPercentage;
	}

	/**
	 * @param completenessPercentage the completenessPercentage to set
	 */
	public void setCompletenessPercentage(Short completenessPercentage) {
		this.completenessPercentage = completenessPercentage;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
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
	 * @return the budgetCurrencyId
	 */
	public Integer getBudgetCurrencyId() {
		return budgetCurrencyId;
	}

	/**
	 * @param budgetCurrencyId the budgetCurrencyId to set
	 */
	public void setBudgetCurrencyId(Integer budgetCurrencyId) {
		this.budgetCurrencyId = budgetCurrencyId;
	}	
	
	public Integer getOldBudgetCurrencyId() {
		return oldBudgetCurrencyId;
	}

	public void setOldBudgetCurrencyId(Integer oldBudgetCurrencyId) {
		this.oldBudgetCurrencyId = oldBudgetCurrencyId;
	}
	
	public Byte getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(Byte notificationStatus) {
		this.notificationStatus = notificationStatus;
	}
		

	/**
	 * @return the oldBudget
	 */
	public Float getOldBudget() {
		return oldBudget;
	}

	/**
	 * @param oldBudget the oldBudget to set
	 */
	public void setOldBudget(Float oldBudget) {
		this.oldBudget = oldBudget;
	}

	/**
	 * @return the oldNotificationPercentage
	 */
	public Short getOldNotificationPercentage() {
		return oldNotificationPercentage;
	}

	/**
	 * @param oldNotificationPercentage the oldNotificationPercentage to set
	 */
	public void setOldNotificationPercentage(Short oldNotificationPercentage) {
		this.oldNotificationPercentage = oldNotificationPercentage;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("projectDetailId = ")				.append(projectDetailId)				.append(", ");
		sb.append("projectId = ")					.append(projectId)						.append(", ");
		sb.append("budget = ")						.append(budget)							.append(", ");		
		sb.append("oldBudget = ")					.append(oldBudget)						.append(", ");
		sb.append("notificationPercentage = ")		.append(notificationPercentage)			.append(", ");
		sb.append("oldNotificationPercentage = ")   .append(oldNotificationPercentage)      .append(", ");
		sb.append("completenessPercentage = ")		.append(completenessPercentage)			.append(", ");		
		sb.append("status = ")						.append(status)							.append(", ");
		sb.append("budgetCurrencyId = ")			.append(budgetCurrencyId)				.append(", ");
		sb.append("observation = ")					.append(observation)					.append(", ");
		sb.append("notificationStatus = ")			.append(notificationStatus)				.append("] ");
		
		return sb.toString();
					
	}

}
