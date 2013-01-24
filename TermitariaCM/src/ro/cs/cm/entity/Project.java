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
package ro.cs.cm.entity;

import java.io.Serializable;

import ro.cs.cm.business.BLProjectTeam;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.om.Person;


/**
 * 
 * @author Coni
 * @author Adelina
 */
public class Project implements Serializable{

	private int projectId;
	private String name;
	private Integer clientId;
	private Client client;
	private String clientName;
	private Integer managerId;
	private Person manager;
	private Integer organizationId;
	private String observation;
	private String description;
	private byte status;
	private ProjectTeam projectTeam;
	private boolean hasProjectTeam;
	private boolean projectNoClient;
	
	private String panelHeaderName;	// header name for the info panel
	
	public Project() {
		this.projectId = -1;
		this.managerId = -1;
		this.organizationId = -1;
		this.clientId = -1;
		this.status = IConstant.NOM_PROJECT_STATUS_OPENED;
		this.hasProjectTeam = false;
	}
		
	
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
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}


	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the panelHeaderName
	 */
	public String getPanelHeaderName() {
		return panelHeaderName;
	}


	/**
	 * @param panelHeaderName the panelHeaderName to set
	 */
	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}
	

	/**
	 * @return the manager
	 */
	public Person getManager() {
		return manager;
	}


	/**
	 * @param manager the manager to set
	 */
	public void setManager(Person manager) {
		this.manager = manager;
	}
	

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}


	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
		
	/**
	 * @return the hasProjectTeam
	 */
	public boolean isHasProjectTeam() {		
		// checks if the project has associated a project team
		try {
			hasProjectTeam = BLProjectTeam.getInstance().hasProjectTeam(getProjectId());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return hasProjectTeam;
	}

	/**
	 * @param hasProjectTeam the hasProjectTeam to set
	 */
	public void setHasProjectTeam(boolean hasProjectTeam) {
		this.hasProjectTeam = hasProjectTeam;
	}
		

	/**
	 * @return the projectTeam
	 */
	public ProjectTeam getProjectTeam() {
		return projectTeam;
	}


	/**
	 * @param projectTeam the projectTeam to set
	 */
	public void setProjectTeam(ProjectTeam projectTeam) {
		this.projectTeam = projectTeam;
	}	

	/**
	 * @return the clientName
	 */
	public String getClientName() {
		return clientName;
	}


	/**
	 * @param clientName the clientName to set
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
		
	/**
	 * @return the projectNoClient
	 */
	public boolean isProjectNoClient() {
		return projectNoClient;
	}


	/**
	 * @param projectNoClient the projectNoClient to set
	 */
	public void setProjectNoClient(boolean projectNoClient) {
		this.projectNoClient = projectNoClient;
	}


	@Override
	public String toString() {
		return "Project [clientId=" + clientId + ", description=" + description
				+ ", name=" + name + ", observation=" + observation
				+ ", organizationId=" + organizationId + ", projectId="
				+ projectId + ", status=" + status + ", projectNoClient=" + projectNoClient+ "]";
	}
	
}
