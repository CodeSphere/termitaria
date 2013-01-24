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

public class SearchExchangeBean extends PaginationBean {

	private Integer[] exchangeId;
	private Integer projectId;
	private Integer firstCurrencyId;
	private Integer secondCurrencyId;
	private Integer organizationId;
	private Float rate;
	
	public Integer[] getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(Integer[] exchangeId) {
		this.exchangeId = exchangeId;
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
	public Float getRate() {
		return rate;
	}
	public void setRate(Float rate) {
		this.rate = rate;
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
		
}
