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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



/**
 * This class is a container for all the information regarding an Organization that needs to
 * be exported and imported to/from an xml file.
 * 
 * @author dan.damian
 *
 */
@XmlRootElement
@XmlType(name="organisationContainer", propOrder = {"organisation", "departments", "persons"})
public class OrganisationContainer {
	
	/**
	 * The Organization
	 */
	private Organisation organisation = null;
	
	/**
	 * Organisation's Departments
	 */
	private List<Department> departments = null;
	
	/**
	 * Organisation's Persons
	 */
	private List<Person> persons = null;

	/**
	 * @return the Organization
	 */
	public Organisation getOrganisation() {
		return organisation;
	}

	/**
	 * @param organisation the Organizations to set
	 */
	public void setOrganisation(Organisation organisation) {
		//Setted during IMPORT Unmarshall phase. 
		this.organisation = organisation;
	}

	/**
	 * OrganisatonContainer contains a list of Departments, each Department
	 * being represented by a <department></department> element.
	 * @return the departments
	 */
	@XmlElement(name="department")
	public List<Department> getDepartments() {
		if (departments == null) {
			//We are on the EXPORT case.
			if (organisation != null && organisation.getDepartments() != null) {
				departments = new ArrayList<Department>(organisation.getDepartments());
			}
		}
		//If Departments are != null, means that they have been already setted
		//during IMPORT Unmarshall phase.
		return departments;
	}

	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(List<Department> departments) {
		//Departments are setted during IMPORT Unmarshall phase
		this.departments = departments;
	}

	/**
	 * OrganisatonContainer contains a list of Persons, each Person
	 * being represented by a <person></person> element.
	 * @return the persons
	 */
	@XmlElement(name="person")
	public List<Person> getPersons() {
		if (persons == null) {
			//We are on EXPORT case.
			if (organisation != null && organisation.getDepartments() != null) {
				Set<Person> uniquePersons = new TreeSet<Person>();
				for(Department d : organisation.getDepartments()) {
					if (d.getPersons() != null) {
						uniquePersons.addAll(d.getPersons());
					}
				}
				persons = new ArrayList<Person>(uniquePersons);
			}
		}
		//If Persons are != null, means they have been already setted
		//during IMPORT Unmarshall phase.
		return persons;
	}

	/**
	 * @param persons the persons to set
	 */
	public void setPersons(List<Person> persons) {
		//Persons are setted during IMPORT Unmarshall phase
		this.persons = persons;
	}

	
	
}
