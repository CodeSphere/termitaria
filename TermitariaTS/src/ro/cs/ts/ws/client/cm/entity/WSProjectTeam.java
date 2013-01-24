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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsTeamMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsTeamMember">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="memberId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="projectId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parentId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="observation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="teamMembers" type="{http://www.w3.org/2001/XMLSchema}tns:wsTeamMember"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsProjectTeam", propOrder = {

})
public class WSProjectTeam {
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private int projectTeamId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private int projectId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String name;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private int parentId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String observation;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String description;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private byte status;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Set<WSTeamMember> teamMembers;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private WSProject project;

	public int getProjectTeamId() {
		return projectTeamId;
	}

	public void setProjectTeamId(int projectTeamId) {
		this.projectTeamId = projectTeamId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Set<WSTeamMember> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(Set<WSTeamMember> teamMembers) {
		this.teamMembers = teamMembers;
	}

	public WSProject getProject() {
		return project;
	}

	public void setProject(WSProject project) {
		this.project = project;
	}
	
}
