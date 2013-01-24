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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSSearchTeamMemberBean", propOrder = {

})
public class WSSearchTeamMemberBean {

	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String 	firstName;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String 	lastName;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer organisationId;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer projectId;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Boolean withDeleted = false;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer personId;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Boolean hasPermissionToSeeAllProjects;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Boolean includeFinishedAndAbandonedProjects;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private byte resultsPerPage;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer currentPage;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer nbrOfPages;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer nbrOfResults = -1;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer lowerLimit;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer upperLimit;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private String sortParam;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Integer sortDirection = 1;
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private Boolean onlyManager;
	
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
	public Integer getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Boolean getWithDeleted() {
		return withDeleted;
	}
	public void setWithDeleted(Boolean withDeleted) {
		this.withDeleted = withDeleted;
	}
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public Boolean getHasPermissionToSeeAllProjects() {
		return hasPermissionToSeeAllProjects;
	}
	public void setHasPermissionToSeeAllProjects(
			Boolean hasPermissionToSeeAllProjects) {
		this.hasPermissionToSeeAllProjects = hasPermissionToSeeAllProjects;
	}
	public Boolean getIncludeFinishedAndAbandonedProjects() {
		return includeFinishedAndAbandonedProjects;
	}
	public void setIncludeFinishedAndAbandonedProjects(
			Boolean includeFinishedAndAbandonedProjects) {
		this.includeFinishedAndAbandonedProjects = includeFinishedAndAbandonedProjects;
	}
	public byte getResultsPerPage() {
		return resultsPerPage;
	}
	public void setResultsPerPage(byte resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getNbrOfPages() {
		return nbrOfPages;
	}
	public void setNbrOfPages(Integer nbrOfPages) {
		this.nbrOfPages = nbrOfPages;
	}
	public Integer getNbrOfResults() {
		return nbrOfResults;
	}
	public void setNbrOfResults(Integer nbrOfResults) {
		this.nbrOfResults = nbrOfResults;
	}
	public Integer getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(Integer lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public Integer getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(Integer upperLimit) {
		this.upperLimit = upperLimit;
	}
	public String getSortParam() {
		return sortParam;
	}
	public void setSortParam(String sortParam) {
		this.sortParam = sortParam;
	}
	public Integer getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(Integer sortDirection) {
		this.sortDirection = sortDirection;
	}
	public Boolean getOnlyManager() {
		return onlyManager;
	}
	public void setOnlyManager(Boolean onlyManager) {
		this.onlyManager = onlyManager;
	}
		
}
