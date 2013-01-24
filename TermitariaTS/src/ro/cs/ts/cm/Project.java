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
package ro.cs.ts.cm;

import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.om.Person;



/**
 * 
 * @author Adelina
 */
public class Project {

	private int projectId;
	private String name;
	private Integer clientId;	
	private Integer managerId;	
	private Integer organizationId;	
	private Client client;
	private String clientName;
	private Person manager;
	private byte status;
	private boolean hasProjectDetail;	
	private ProjectTeam projectTeam;
	
	private String panelHeaderName;	// header name for the info panel
			
	public Project() {		
	
	}

	public ProjectTeam getProjectTeam() {
		return projectTeam;
	}

	public void setProjectTeam(ProjectTeam projectTeam) {
		this.projectTeam = projectTeam;
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
	 * @return the hasProjectDetail
	 */
	public boolean isHasProjectDetail() {		
		try {
			hasProjectDetail = BLProjectDetails.getInstance().hasProjectDetails(getProjectId());
		} catch (BusinessException e) {			
			e.printStackTrace();
		}		
		return hasProjectDetail;
	}


	/**
	 * @param hasProjectDetail the hasProjectDetail to set
	 */
	public void setHasProjectDetail(boolean hasProjectDetail) {
		this.hasProjectDetail = hasProjectDetail;
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

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("projectId = ")			.append(projectId)		.append(", ");
		sb.append("name = ")				.append(name)			.append(", ");		
		sb.append("organisationId = ")		.append(organizationId)	.append(", ");
		sb.append("clientId = ")			.append(clientId)		.append(", ");		
		sb.append("status = ")				.append(status)			.append(", ");
		sb.append("managerId = ")			.append(managerId)		.append("] ");	
		
		return sb.toString();
	}
    
    @Override
    public boolean equals (Object obj){
    	if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Project other = (Project) obj;
		if (this.projectId != other.projectId) {
			return false;
		}
		if ((this.name == null) ? (other.name != null)
				: !this.name.equals(other.name)) {
			return false;
		}				
		return true;
	}
	
	@Override
	public int hashCode(){
		int hash = 5;
		hash = 67 * hash + this.projectId;
		hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);			
		return hash;
	}			
	
}
