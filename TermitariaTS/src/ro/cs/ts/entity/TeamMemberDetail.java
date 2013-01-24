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
 * @author Coni
 * @author Adelina
 *
 */
public class TeamMemberDetail {

	private int teamMemberDetailId;
	private Integer teamMemberId;
	private Float costPrice;
	private Integer costPriceCurrencyId;
	private Currency costPriceCurrency;
	private Float billingPrice;
	private Integer billingPriceCurrencyId;
	private Currency billingPriceCurrency;
	private Short costTimeUnit;
	private Short billingTimeUnit;
	private Float overtimeCostPrice;
	private Integer overtimeCostCurrencyId;
	private Currency overtimeCostCurrency;
	private Float overtimeBillingPrice;
	private Integer overtimeBillingCurrencyId;
	private Currency overtimeBillingCurrency;	
	private Short overtimeCostTimeUnit;
	private Short overtimeBillingTimeUnit;	
	private String observation;
	private byte status;

	public TeamMemberDetail() {
		
	}
	
	/**
	 * @return the teamMemberDetailId
	 */
	public int getTeamMemberDetailId() {
		return teamMemberDetailId;
	}

	/**
	 * @param teamMemberDetailId the teamMemberDetailId to set
	 */
	public void setTeamMemberDetailId(int teamMemberDetailId) {
		this.teamMemberDetailId = teamMemberDetailId;
	}

	/**
	 * @return the teamMemberId
	 */
	public Integer getTeamMemberId() {
		return teamMemberId;
	}

	/**
	 * @param teamMemberId the teamMemberId to set
	 */
	public void setTeamMemberId(Integer teamMemberId) {
		this.teamMemberId = teamMemberId;
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
	 * @return the costPriceCurrencyId
	 */
	public Integer getCostPriceCurrencyId() {
		return costPriceCurrencyId;
	}

	/**
	 * @param costPriceCurrencyId the costPriceCurrencyId to set
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
	 * @return the billingPriceCurrencyId
	 */
	public Integer getBillingPriceCurrencyId() {
		return billingPriceCurrencyId;
	}

	/**
	 * @param billingPriceCurrencyId the billingPriceCurrencyId to set
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
	 * @return the overtimeCostPrice
	 */
	public Float getOvertimeCostPrice() {
		return overtimeCostPrice;
	}

	/**
	 * @param overtimeCostPrice the overtimeCostPrice to set
	 */
	public void setOvertimeCostPrice(Float overtimeCostPrice) {
		this.overtimeCostPrice = overtimeCostPrice;
	}
	
	/**
	 * @return the overtimeBillingPrice
	 */
	public Float getOvertimeBillingPrice() {
		return overtimeBillingPrice;
	}

	/**
	 * @param overtimeBillingPrice the overtimeBillingPrice to set
	 */
	public void setOvertimeBillingPrice(Float overtimeBillingPrice) {
		this.overtimeBillingPrice = overtimeBillingPrice;
	}	

	/**
	 * @return the overtimeCostCurrencyId
	 */
	public Integer getOvertimeCostCurrencyId() {
		return overtimeCostCurrencyId;
	}

	/**
	 * @param overtimeCostCurrencyId the overtimeCostCurrencyId to set
	 */
	public void setOvertimeCostCurrencyId(Integer overtimeCostCurrencyId) {
		this.overtimeCostCurrencyId = overtimeCostCurrencyId;
	}

	/**
	 * @return the overtimeBillingCurrencyId
	 */
	public Integer getOvertimeBillingCurrencyId() {
		return overtimeBillingCurrencyId;
	}

	/**
	 * @param overtimeBillingCurrencyId the overtimeBillingCurrencyId to set
	 */
	public void setOvertimeBillingCurrencyId(Integer overtimeBillingCurrencyId) {
		this.overtimeBillingCurrencyId = overtimeBillingCurrencyId;
	}

	/**
	 * @return the overtimeCostTimeUnit
	 */
	public Short getOvertimeCostTimeUnit() {
		return overtimeCostTimeUnit;
	}

	/**
	 * @param overtimeCostTimeUnit the overtimeCostTimeUnit to set
	 */
	public void setOvertimeCostTimeUnit(Short overtimeCostTimeUnit) {
		this.overtimeCostTimeUnit = overtimeCostTimeUnit;
	}

	/**
	 * @return the overtimeBillingTimeUnit
	 */
	public Short getOvertimeBillingTimeUnit() {
		return overtimeBillingTimeUnit;
	}

	/**
	 * @param overtimeBillingTimeUnit the overtimeBillingTimeUnit to set
	 */
	public void setOvertimeBillingTimeUnit(Short overtimeBillingTimeUnit) {
		this.overtimeBillingTimeUnit = overtimeBillingTimeUnit;
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

	/**
	 * @return the overtimeCostCurrency
	 */
	public Currency getOvertimeCostCurrency() {
		return overtimeCostCurrency;
	}

	/**
	 * @param overtimeCostCurrency the overtimeCostCurrency to set
	 */
	public void setOvertimeCostCurrency(Currency overtimeCostCurrency) {
		this.overtimeCostCurrency = overtimeCostCurrency;
	}

	/**
	 * @return the overtimeBillingCurrency
	 */
	public Currency getOvertimeBillingCurrency() {
		return overtimeBillingCurrency;
	}

	/**
	 * @param overtimeBillingCurrency the overtimeBillingCurrency to set
	 */
	public void setOvertimeBillingCurrency(Currency overtimeBillingCurrency) {
		this.overtimeBillingCurrency = overtimeBillingCurrency;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("teamMemberDetailId = ")			.append(teamMemberDetailId)			.append(", ");
		sb.append("teamMemberId = ")				.append(teamMemberId)				.append(", ");
		sb.append("costPrice = ")					.append(costPrice)					.append(", ");		
		sb.append("costPriceCurrencyId = ")			.append(costPriceCurrencyId)		.append(", ");
		sb.append("billingPrice = ")				.append(billingPrice)				.append(", ");		
		sb.append("billingPriceCurrencyId = ")		.append(billingPriceCurrencyId)		.append(", ");
		sb.append("costTimeUnit = ")				.append(costTimeUnit)				.append(", ");
		sb.append("billingTimeUnit = ")				.append(billingTimeUnit)			.append(", ");
		sb.append("overtimeCostPrice = ")			.append(overtimeCostPrice)			.append(", ");		
		sb.append("overtimeCostCurrencyId = ")		.append(overtimeCostCurrencyId)		.append(", ");
		sb.append("overtimeBillingPrice = ")		.append(overtimeBillingPrice)		.append(", ");		
		sb.append("overtimeBillingCurrencyId = ")	.append(overtimeBillingCurrencyId)	.append(", ");
		sb.append("overtimeCostTimeUnit = ")		.append(overtimeCostTimeUnit)		.append(", ");
		sb.append("overtimeBillingTimeUnit = ")		.append(overtimeBillingTimeUnit)	.append(", ");
		sb.append("status = ")						.append(status)						.append(", ");
		sb.append("observation = ")					.append(observation)				.append("] ");	
		
		return sb.toString();
					
	}  
}
