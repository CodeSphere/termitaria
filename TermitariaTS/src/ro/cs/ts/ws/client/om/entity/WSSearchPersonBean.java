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
package ro.cs.ts.ws.client.om.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSSearchPersonBean", propOrder = {

})
public class WSSearchPersonBean {

	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private String 	firstName;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private String 	lastName;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private String username;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int organisationId;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int departmentId;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private Character sex = new Character(' ');
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private Integer[] personId;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private byte resultsPerPage;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int currentPage;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int nbrOfPages;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int nbrOfResults = -1;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int lowerLimit;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int upperLimit;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private String sortParam;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private int sortDirection = 1;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
	private boolean withDeleted = false;
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public Character getSex() {
		return sex;
	}
	public void setSex(Character sex) {
		this.sex = sex;
	}
	public Integer[] getPersonId() {
		return personId;
	}
	public void setPersonId(Integer[] personId) {
		this.personId = personId;
	}
	public byte getResultsPerPage() {
		return resultsPerPage;
	}
	public void setResultsPerPage(byte resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNbrOfPages() {
		return nbrOfPages;
	}
	public void setNbrOfPages(int nbrOfPages) {
		this.nbrOfPages = nbrOfPages;
	}
	public int getNbrOfResults() {
		return nbrOfResults;
	}
	public void setNbrOfResults(int nbrOfResults) {
		this.nbrOfResults = nbrOfResults;
	}
	public int getLowerLimit() {
		return lowerLimit;
	}
	public void setLowerLimit(int lowerLimit) {
		this.lowerLimit = lowerLimit;
	}
	public int getUpperLimit() {
		return upperLimit;
	}
	public void setUpperLimit(int upperLimit) {
		this.upperLimit = upperLimit;
	}
	public String getSortParam() {
		return sortParam;
	}
	public void setSortParam(String sortParam) {
		this.sortParam = sortParam;
	}
	public int getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}
	public boolean isWithDeleted() {
		return withDeleted;
	}
	public void setWithDeleted(boolean withDeleted) {
		this.withDeleted = withDeleted;
	}
		
}
