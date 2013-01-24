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

import ro.cs.ts.common.IConstant;

/**
 * 
 * @author Coni
 *
 */
public class Exchange {
	
	private int exchangeId;
	private Integer firstCurrencyId;
	private Integer secondCurrencyId;
	private Integer projectDetailId;
	private Float rate;
	private String formattedRate;
	private byte status;
	private String panelHeaderName;
	private String projectName;
	private Integer projectId;
	private Integer organizationId;
	
	private Currency firstCurrency;
	private Currency secondCurrency;
	private ProjectDetails projectDetail;
	
	public Exchange() {
		this.exchangeId = -1;
		this.status = IConstant.NOM_EXCHANGE_STATUS_ACTIVE;
	}

	public int getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(int exchangeId) {
		this.exchangeId = exchangeId;
	}

	public String getPanelHeaderName() {
		return panelHeaderName;
	}

	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}

	public Integer getFirstCurrencyId() {
		return firstCurrencyId;
	}

	public void setFirstCurrencyId(Integer firstCurrencyId) {
		this.firstCurrencyId = firstCurrencyId;
	}

	public Integer getSecondCurrencyId() {
		return secondCurrencyId;
	}

	public void setSecondCurrencyId(Integer secondCurrencyId) {
		this.secondCurrencyId = secondCurrencyId;
	}

	public Integer getProjectDetailId() {
		return projectDetailId;
	}

	public void setProjectDetailId(Integer projectDetailId) {
		this.projectDetailId = projectDetailId;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Currency getFirstCurrency() {
		return firstCurrency;
	}

	public void setFirstCurrency(Currency firstCurrency) {
		this.firstCurrency = firstCurrency;
	}

	public Currency getSecondCurrency() {
		return secondCurrency;
	}

	public void setSecondCurrency(Currency secondCurrency) {
		this.secondCurrency = secondCurrency;
	}

	public ProjectDetails getProjectDetail() {
		return projectDetail;
	}

	public void setProjectDetail(ProjectDetails projectDetail) {
		this.projectDetail = projectDetail;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getFormattedRate() {
		return formattedRate;
	}

	public void setFormattedRate(String formattedRate) {
		this.formattedRate = formattedRate;
	}

}
