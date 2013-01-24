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
package ro.cs.logaudit.entity;

import java.util.Date;

import ro.cs.logaudit.common.IConstant;


/**
 * @author alu
 * @author Adelina
 * @author coni
 *
 */
public class SearchAuditOmBean extends PaginationBean {
		
	private Integer personId;
	private String message;
	private String event;	
	private int moduleId = IConstant.NOM_MODULE_OM_LABEL_KEY;
	private int organisationId = -1;
	private Date startDate;
	private Date endDate;
	private Integer[] auditId;

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the organisationId
	 */
	public int getOrganisationId() {
		return organisationId;
	}

	/**
	 * @param organisationId the organisationId to set
	 */
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}	

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	
	public Integer[] getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer[] auditOmId) {
		this.auditId = auditOmId;
	}

	/**
	 * @author Adelina
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("personId = ")		.append(personId)		.append(", ");		
		sb.append("message = ")			.append(message)		.append(", ");
		sb.append("event = ")			.append(event)			.append(", ");
		sb.append("moduleId = ")		.append(moduleId)		.append(", ");
		sb.append("organisationId = ")	.append(organisationId)	.append(", ");
		sb.append("startDate = ")		.append(startDate)		.append(", ");
		sb.append("endDate = ")			.append(endDate)		.append("] ");
		
		return sb.toString();		
	}
}
