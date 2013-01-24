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
public class PersonDetail {
	
	private int personDetailId;
	private Integer personId;
	private Float costPrice;
	private Integer costPriceCurrencyId;
	private Currency costPriceCurrency;
	private Short costTimeUnit;
	private Float overtimeCostPrice;
	private Integer overtimeCostCurrencyId;
	private Currency overtimeCostCurrency;
	private Short overtimeCostTimeUnit;		
	private String observation;
	private byte status;
	
	public PersonDetail() {
		
	}

	/**
	 * @return the personDetailId
	 */
	public int getPersonDetailId() {
		return personDetailId;
	}

	/**
	 * @param personDetailId the personDetailId to set
	 */
	public void setPersonDetailId(int personDetailId) {
		this.personDetailId = personDetailId;
	}

	/**
	 * @return the personId
	 */
	public Integer getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(Integer personId) {
		this.personId = personId;
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

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("personDetailId = ")				.append(personDetailId)			.append(", ");
		sb.append("personId = ")					.append(personId)				.append(", ");
		sb.append("costPrice = ")					.append(costPrice)					.append(", ");		
		sb.append("costPriceCurrencyId = ")			.append(costPriceCurrencyId)		.append(", ");	
		sb.append("costTimeUnit = ")				.append(costTimeUnit)				.append(", ");		
		sb.append("overtimeCostPrice = ")			.append(overtimeCostPrice)			.append(", ");		
		sb.append("overtimeCostCurrencyId = ")		.append(overtimeCostCurrencyId)		.append(", ");
		sb.append("overtimeCostTimeUnit = ")		.append(overtimeCostTimeUnit)		.append(", ");		
		sb.append("status = ")						.append(status)						.append(", ");
		sb.append("observation = ")					.append(observation)				.append("] ");	
		
		return sb.toString();
					
	}  
	
}
