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
package ro.cs.om.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;



/**
 * @author matti_joona
 * @author dan.damian
 *
 */
//Exported in XML in a <department></department> element.
@XmlType(name = "department")
public class Department implements IExportImportAble, Serializable {

	private static final long serialVersionUID = 1L;
	private int departmentId;
	private String xmlID = null;
	private int organisationId;
	private Organisation organisation;
	private String name;
	private Integer managerId;
	private Person manager;
	private int parentDepartmentId;
	private String observation;
	private Set<Person> persons;
	private Department parentDepartment;
	private byte status;
	private Map<Department, Job> deptWithJob;
	private Map<Person, Job> personWithJob;
	private String panelHeaderName;
	private Boolean hasPerson = null;
	
	
	public Department(){
		this.persons = new HashSet<Person>();		
	}
	
	//It's not saved in the xml
	@XmlTransient
	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	//Modeling relation one-to-one to an Organization by having a reference to 
	//Organization (it's xmlID).
	@XmlIDREF
	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	//Modeling department - persons (on-to-many) relation
	//using a reference to a person. A reference to person by 
	//knowing it's xmlID.
	@XmlElement(name="person")
	@XmlIDREF
	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
	
	public int getParentDepartmentId() {
		return parentDepartmentId;
	}

	public void setParentDepartmentId(int parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}

	//Modeling an one-to-one relation with it's Parent Department by having a reference to 
	//it, using it's xmlID. 
	@XmlIDREF
	public Department getParentDepartment() {
		return parentDepartment;
	}

	public void setParentDepartment(Department parentDepartment) {
		this.parentDepartment = parentDepartment;
	}

	//Modeling an one-to-one relation with it's Manager by having a reference to 
	//it, using it's xmlID. 
	@XmlIDREF
	public Person getManager() {
		return manager;
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}

	
	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public int getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
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
	 * 
	 * @param deptXjob
	 */
	public void setPersonWithJob(Map<Person, Job> personWithJob) {
		this.personWithJob = personWithJob;
	}

	public Map<Person, Job> getPersonWithJob() {
		return personWithJob;
	}
	
	/**
	 * 
	 * @param personXjob
	 */
	public void setDeptWithJob(Map<Department, Job> deptWithJob) {
		this.deptWithJob = deptWithJob;
	}

	public Map<Department, Job> getDeptWithJob() {
		return deptWithJob;
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
	 * @return the hasPerson
	 */
	public boolean getHasPerson() {
		if(this.getPersons() != null && this.getPersons().size() > 0) {
			hasPerson = true;
		} else {
			hasPerson = false;
		}
		
		return hasPerson;
	}
	
	/*
	 * (non-Javadoc)
	 * @see ro.cs.om.entity.IExportImportAble#getXmlID()
	 */
	@XmlID
	public String getXmlID() {
		if (xmlID == null) {
			xmlID = this.getClass().getName().concat(Integer.toString(departmentId));
		}
		return xmlID;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ro.cs.om.entity.IExportImportAble#setXmlID(java.lang.String)
	 */
	public void setXmlID(String xmlID) {
		this.xmlID = xmlID;
	}

	/**
	 * @author dd
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" [");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("departmentId = ")	.append(departmentId)		.append(", ");
		sb.append("name = ")			.append(name)				.append(", ");
		sb.append("xmlID = ")			.append(getXmlID())			.append(", ");
		
		sb.append("manager = ")			.append(manager)			.append(", ");
		sb.append("observation = ")		.append(observation)		.append(", ");
		sb.append("status = ")			.append(status)				.append(", ");
		sb.append("persons = ")			.append((persons != null?persons.size():"0"))		.append(",");
		sb.append("parent Department = ").append(parentDepartment)	.append(", ");
		sb.append("organisation = ")	.append(organisation)		.append("]");
		
		return sb.toString();
	}
	
	public boolean equals (Object o){
		if ( (o instanceof Department) && ( this.getDepartmentId() == ((Department)o).getDepartmentId()) ){
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode(){
		return this.getDepartmentId();
	}
	
}
