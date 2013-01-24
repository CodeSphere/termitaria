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
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.cs.om.common.IConstant;
import ro.cs.om.utils.common.StringUtils;
import ro.cs.tools.Tools;

/**
 * @author matti_joona
 *
 */
public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());

	private int jobId;
	private String name;
	private String jobLevel;
	private String description;
	private String observation;
	private int status;
	private Set<Department> deptWithPerson;
	private Set<Person> persons;
	private Organisation organisation;
	private String panelConfirmationName;
	
	/**
	 * @return the jobId
	 */
	public int getJobId() {
		return jobId;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(int jobId) {
		this.jobId = jobId;
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
	 * @return the jobLevel
	 */
	public String getJobLevel() {
		return jobLevel;
	}
	/**
	 * @param jobLevel the jobLevel to set
	 */
	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}
	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	public Organisation getOrganisation() {
		return organisation;
	}
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	public void setOrganistionId(Integer organisationId) {
		if(this.organisation != null){
			this.organisation.setOrganisationId(organisationId);		
		}
	}
	public int getOrganistionId() {
		if(this.organisation != null){
			return this.organisation.getOrganisationId();		
		}
		return -1;
	}
	public void setDeptWithPerson(Set<Department> deptWithPerson) {
		this.deptWithPerson = deptWithPerson;
	}
	public Set<Department> getDeptWithPerson() {
		return deptWithPerson;
	}
	/**
	 * @return the persons
	 */
	public Set<Person> getPersons() {
		return persons;
	}
	/**
	 * @param persons the persons to set
	 */
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
	
	/**
	 * @return the panelConfirmationName
	 */
	public String getPanelConfirmationName() {
		return panelConfirmationName;
	}

	/**
	 * @param panelConfirmationName the panelConfirmationName to set
	 */
	public void setPanelConfirmationName(String panelConfirmationName) {
		this.panelConfirmationName = panelConfirmationName;
	}

	public String getTokenizedDescription() {
		return StringUtils.getInstance().tokenizedString(this.description, IConstant.JOB_TEXT_AREA_ROW_SIZE);
	}
	
	public String getTruncatedTokenizedDescription() {
		return StringUtils.getInstance().truncatedString(this.description, IConstant.JOB_TEXT_AREA_ROW_SIZE, IConstant.JOB_TEXT_AREA_SIZE);
	}
	
	
	/**
	 * @author Adelina
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("jobId = ").append(jobId).append(", ");
		sb.append("name = ").append(name).append(", ");
		sb.append("jobLevel = ").append(jobLevel).append(", ");
		sb.append("description = ").append(description).append(", ");
		sb.append("observation = ").append(observation).append(", ");		
		sb.append("status = ").append(status).append("] ");		
		logger.debug("before persons");
		Tools.getInstance().printSet(logger, persons);
		logger.debug("after persons");		

		return sb.toString();
	}
	
}
