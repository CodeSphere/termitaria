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
package ro.cs.ts.ws.server.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetRecordDetailsBySessionIdRequest element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="GetRecordIdBySessionIdRequest">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="sessionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="projectId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="activityId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="organizationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *           &lt;element name="billable" type="{http://www.w3.org/2001/XMLSchema}character"/>
 *           &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *           &lt;element name="teamMemberId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
})

@XmlRootElement(name = "GetRecordDetailsBySessionIdRequest")
/**
 * Gets from TS all the record details for a sessionId
 * @author Andreea
 */
public class GetRecordDetailsBySessionIdRequest {
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected String sessionId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected int userId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected int projectId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected int activityId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected Date startTime;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected int organizationId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected Character billable;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected byte status;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required=true)
    protected int teamMemberId;
	

	/**
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the activityId
	 */
	public int getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the organizationId
	 */
	public int getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the billable
	 */
	public Character getBillable() {
		return billable;
	}

	/**
	 * @param billable the billable to set
	 */
	public void setBillable(Character billable) {
		this.billable = billable;
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
	 * @return the teamMemberId
	 */
	public int getTeamMemberId() {
		return teamMemberId;
	}

	/**
	 * @param teamMemberId the teamMemberId to set
	 */
	public void setTeamMemberId(int teamMemberId) {
		this.teamMemberId = teamMemberId;
	}


	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	
}
