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
package ro.cs.ts.ws.client.om.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for organisationSimple complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="calendar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *      	 &lt;element name="calendarId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         	 &lt;element name="organisationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="startWork" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="endWork" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsCalendar", propOrder = {

})
public class WSCalendar {
	
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected int calendarId;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	protected int organisationId;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String startWork;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String endWork;
    
	/**
	 * @return the calendarId
	 */
	public int getCalendarId() {
		return calendarId;
	}
	/**
	 * @param calendarId the calendarId to set
	 */
	public void setCalendarId(int calendarId) {
		this.calendarId = calendarId;
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
	 * @return the startWork
	 */
	public String getStartWork() {
		return startWork;
	}
	/**
	 * @param startWork the startWork to set
	 */
	public void setStartWork(String startWork) {
		this.startWork = startWork;
	}
	/**
	 * @return the endWork
	 */
	public String getEndWork() {
		return endWork;
	}
	/**
	 * @param endWork the endWork to set
	 */
	public void setEndWork(String endWork) {
		this.endWork = endWork;
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("calendarId = ")			.append(calendarId)			.append(", ");
		sb.append("organizationId = ")		.append(organisationId)		.append(", ");		
		sb.append("startWork = ")			.append(startWork)			.append(", ");		
		sb.append("endWork = ")				.append(endWork)			.append("] ");	
		
		return sb.toString();
	}
    
	
     
}


