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
package ro.cs.ts.ws.client.cm.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ro.cs.ts.ws.client.om.entity.UserSimple;

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
 *         &lt;element name="projectId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="managerId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="organizationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="clientId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="projectTeam" type="{http://www.w3.org/2001/XMLSchema}tns:wsProjectTeam"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsProject", propOrder = {

})

public class WSProject {
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	protected int projectId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	protected String name;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer managerId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer organizationId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer clientId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private byte status;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private WSProjectTeam projectTeam;
			
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private WSClient client;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private UserSimple manager;

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
	 * @return the managerId
	 */
	public Integer getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId the managerId to set
	 */
	public void setManagerId(Integer managerId) {
		this.managerId = managerId;
	}

	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
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
	 * @return the clientId
	 */
	public Integer getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public WSProjectTeam getProjectTeam() {
		return projectTeam;
	}

	public void setProjectTeam(WSProjectTeam projectTeam) {
		this.projectTeam = projectTeam;
	}	
	
		/**
	 * @return the client
	 */
	public WSClient getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(WSClient client) {
		this.client = client;
	}

	/**
	 * @return the manager
	 */
	public UserSimple getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(UserSimple manager) {
		this.manager = manager;
	}	
}
