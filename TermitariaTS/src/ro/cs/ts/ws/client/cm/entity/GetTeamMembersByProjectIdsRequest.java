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

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetProjectsByManagerRequest element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="GetProjectsByManagerRequest">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="projectId" type="{http://www.w3.org/2001/XMLSchema}int"/>
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

@XmlRootElement(name = "GetTeamMembersByProjectIdsRequest")
public class GetTeamMembersByProjectIdsRequest {

	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
    protected Set<Integer> projectIds;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private boolean isExternal = false;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private boolean isNotDeleted;

	/**
	 * @return the projectIds
	 */
	public Set<Integer> getProjectIds() {
		return projectIds;
	}

	/**
	 * @param projectIds the projectIds to set
	 */
	public void setProjectIds(Set<Integer> projectIds) {
		this.projectIds = projectIds;
	}

	/**
	 * @return the isExternal
	 */
	public boolean isExternal() {
		return isExternal;
	}

	/**
	 * @param isExternal the isExternal to set
	 */
	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}

	/**
	 * @return the isNotDeleted
	 */
	public boolean isNotDeleted() {
		return isNotDeleted;
	}

	/**
	 * @param isNotDeleted the isNotDeleted to set
	 */
	public void setNotDeleted(boolean isNotDeleted) {
		this.isNotDeleted = isNotDeleted;
	}	
	
}
