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
import java.text.SimpleDateFormat;

import ro.cs.logaudit.common.ConfigParametersProvider;

/**
 * @author coni
 *
 */
public class AuditOmBean {
	
	private int auditId = -1;
	private Date date;
	private String messageRO;
	private String messageEN;
	private String event;
	private Integer organisationId;
	private Integer personId;
	private String firstName;
	private String lastName;

	public AuditOmBean(){
	}
	
	public String getMessageRO() {
		return messageRO;
	}
	public void setMessageRO(String messageRO) {
		this.messageRO = messageRO;
	}
	public String getMessageEN() {
		return messageEN;
	}
	public void setMessageEN(String messageEN) {
		this.messageEN = messageEN;
	}
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the auditId
	 */
	public int getAuditId() {
		return auditId;
	}
	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(int auditId) {
		this.auditId = auditId;
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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	
	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public String getDateString(){
		if (date != null){
			SimpleDateFormat sd = new SimpleDateFormat(ConfigParametersProvider.getConfigString("date.format"));
			return sd.format(date);
		} else return "";
	}
	@Override
	public String toString() {
		return "AuditOmBean [auditId=" + auditId + ", date=" + date
				+ ", event=" + event + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", message_en=" + messageEN
				+ ", message_ro=" + messageRO + ", organisationId="
				+ organisationId + ", personId=" + personId + "]";
	}
	
}
