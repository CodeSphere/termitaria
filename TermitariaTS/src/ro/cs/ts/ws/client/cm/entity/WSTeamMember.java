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
 *         &lt;element name="projectTeamId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="personId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="observation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "wsTeamMember", propOrder = {

})
public class WSTeamMember {

	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private int memberId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private int projectTeamId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private int personId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String firstName;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String lastName;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String email;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String address;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String phone;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String observation;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String description;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private byte status;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private WSProjectTeam projectTeam;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getProjectTeamId() {
		return projectTeamId;
	}

	public void setProjectTeamId(int projectTeamId) {
		this.projectTeamId = projectTeamId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	/**
	 * @return the projectTeam
	 */
	public WSProjectTeam getProjectTeam() {
		return projectTeam;
	}

	/**
	 * @param projectTeam the projectTeam to set
	 */
	public void setProjectTeam(WSProjectTeam projectTeam) {
		this.projectTeam = projectTeam;
	}
	
}
