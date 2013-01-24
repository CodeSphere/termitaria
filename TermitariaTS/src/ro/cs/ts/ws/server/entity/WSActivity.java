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
 * &lt;complexType name="organisationSimple">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="activityId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="projectDetailId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="billable" type="{http://www.w3.org/2001/XMLSchema}character"/>
 *         &lt;element name="organizationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsActivity", propOrder = {

})

public class WSActivity {
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required = true)
	protected int activityId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required = true)
	protected String name;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private int projectDetailId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages", required = true)
	private Character billable;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private int organizationId;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private byte status;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the projectDetailId
	 */
	public int getProjectDetailId() {
		return projectDetailId;
	}

	/**
	 * @param projectDetailId the projectDetailId to set
	 */
	public void setProjectDetailId(int projectDetailId) {
		this.projectDetailId = projectDetailId;
	}



}
