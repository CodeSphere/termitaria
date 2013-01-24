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

public class Currency {

	private int currencyId;
	private String name;
	private String initials;
	private Integer organizationId;
	private byte status;
	private String panelHeaderName;	// header name for the info panel
	
	private Set<TeamMemberDetail> teamMemberDetails;
	private Set<PersonDetail> personDetails;
	private Set<Activity> activities;
	private Set<Exchange> exchanges;
	private Set<CostSheet> costs;
	
	public Currency() {
		this.currencyId = -1;
		this.status = IConstant.NOM_CURRENCY_STATUS_ACTIVE;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public String getName() {
		return name;
	}

	public String getPanelHeaderName() {
		return panelHeaderName;
	}

	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Set<TeamMemberDetail> getTeamMemberDetails() {
		return teamMemberDetails;
	}

	public void setTeamMemberDetails(Set<TeamMemberDetail> teamMemberDetails) {
		this.teamMemberDetails = teamMemberDetails;
	}

	public Set<PersonDetail> getPersonDetails() {
		return personDetails;
	}

	public void setPersonDetails(Set<PersonDetail> personDetails) {
		this.personDetails = personDetails;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<Exchange> getExchanges() {
		return exchanges;
	}

	public void setExchanges(Set<Exchange> exchanges) {
		this.exchanges = exchanges;
	}

	public Set<CostSheet> getCosts() {
		return costs;
	}

	public void setCosts(Set<CostSheet> costs) {
		this.costs = costs;
	}
	
	 @Override
		public String toString() {
			StringBuffer sb = new StringBuffer("[");
			
			sb.append(this.getClass().getSimpleName());
			sb.append(": ");
			sb.append("currencyId = ")					.append(currencyId)				.append(", ");
			sb.append("name = ")						.append(name)					.append(", ");		
			sb.append("initials = ")					.append(initials)				.append(", ");	
			sb.append("status = ")						.append(status)		.append(", ");
			sb.append("organisationId = ")				.append(organizationId)			.append("] ");
						
			return sb.toString();
		}

}
