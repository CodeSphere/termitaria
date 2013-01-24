/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.om.entity;

import java.util.Date;

/**
 * @author alu
 *
 */
public class SearchOOOBean extends PaginationBean {
	
	
	private String ownerFirstName;
	private String ownerLastName;
	private String replacementFirstName;
	private String replacementLastName;
	private Date startPeriod;
	private Date endPeriod;
	private int organisationId = -1;
	private Integer[] outOfOfficeId;
	private String observation;

	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public Date getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	public Date getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}
	public int getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}
	public Integer[] getOutOfOfficeId() {
		return outOfOfficeId;
	}
	public void setOutOfOfficeId(Integer[] outOfOfficeId) {
		this.outOfOfficeId = outOfOfficeId;
	}
	public String getOwnerFirstName() {
		return ownerFirstName;
	}
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}
	public String getOwnerLastName() {
		return ownerLastName;
	}
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
	public String getReplacementFirstName() {
		return replacementFirstName;
	}
	public void setReplacementFirstName(String replacementFirstName) {
		this.replacementFirstName = replacementFirstName;
	}
	public String getReplacementLastName() {
		return replacementLastName;
	}
	public void setReplacementLastName(String replacementLastName) {
		this.replacementLastName = replacementLastName;
	}

	
}
