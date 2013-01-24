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

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetProjectsAndTeamMembersByPersonResponse element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="GetProjectsAndTeamMembersByPersonResponse">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="projectsAndTeamMembers" type="{http://localhost:8080/CM/services/schemas/messages}wsProject"/>
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
@XmlRootElement(name = "GetProjectsAndTeamMembersByPersonResponse")
public class GetProjectsAndTeamMembersByPersonResponse {

	/*
	 * Holds pairs of key - value where the key is a Project and value is its corresponding teamMemberId for a specific person
	 */
	@XmlElementWrapper(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private HashMap<WSProject, WSTeamMember> projectsAndTeamMembers;
	
	@XmlElementWrapper(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private List<WSProject> projects;	

	public HashMap<WSProject, WSTeamMember> getProjectsAndTeamMembers() {
		return projectsAndTeamMembers;
	}

	public void setProjectsAndTeamMembers(
			HashMap<WSProject, WSTeamMember> projectsAndTeamMembers) {
		this.projectsAndTeamMembers = projectsAndTeamMembers;
	}

	public List<WSProject> getProjects() {
		return projects;
	}

	public void setProjects(List<WSProject> projects) {
		this.projects = projects;
	}

}
