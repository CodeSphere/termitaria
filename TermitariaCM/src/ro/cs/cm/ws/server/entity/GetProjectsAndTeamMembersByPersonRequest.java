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
package ro.cs.cm.ws.server.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetProjectsAndTeamMembersByPersonRequest element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="GetProjectsAndTeamMembersByPersonRequest">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="personId" type="{http://www.w3.org/2001/XMLSchema}int"/>
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

@XmlRootElement(name = "GetProjectsAndTeamMembersByPersonRequest")
/**
 * Gets from CM all the projects and team members ids for a person 
 * @author Coni
 */
public class GetProjectsAndTeamMembersByPersonRequest {

	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required=true)
    protected int personId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required=true)
	private boolean onlyProjects = false;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required=true)
	private boolean isNotDeleted;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required=true)
	private boolean isFinishedAndAbandoned;

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public boolean isOnlyProjects() {
		return onlyProjects;
	}

	public void setOnlyProjects(boolean onlyProjects) {
		this.onlyProjects = onlyProjects;
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

	/**
	 * @return the isFinishedAndAbandoned
	 */
	public boolean isFinishedAndAbandoned() {
		return isFinishedAndAbandoned;
	}

	/**
	 * @param isFinishedAndAbandoned the isFinishedAndAbandoned to set
	 */
	public void setFinishedAndAbandoned(boolean isFinishedAndAbandoned) {
		this.isFinishedAndAbandoned = isFinishedAndAbandoned;
	}	
	
	
	
}
