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
package ro.cs.cm.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SearchTeamMemberBean extends PaginationBean {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private String firstName;
	private String lastName;	
	private Integer projectId;
	private Integer organizationId;
	private boolean withDeleted = false;
	private Integer personId;
	private boolean hasPermissionToSeeAllProjects;
	private boolean includeFinishedAndAbandonedProjects;
	private boolean onlyManager;
		
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public boolean isHasPermissionToSeeAllProjects() {
		return hasPermissionToSeeAllProjects;
	}
	public void setHasPermissionToSeeAllProjects(
			boolean hasPermissionToSeeAllProjects) {
		this.hasPermissionToSeeAllProjects = hasPermissionToSeeAllProjects;
	}
	public boolean isIncludeFinishedAndAbandonedProjects() {
		return includeFinishedAndAbandonedProjects;
	}
	public void setIncludeFinishedAndAbandonedProjects(
			boolean includeFinishedAndAbandonedProjects) {
		this.includeFinishedAndAbandonedProjects = includeFinishedAndAbandonedProjects;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
		
	/**
	 * @return the projectId
	 */
	public Integer getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}	
		
	/**
	 * @return the withDeleted
	 */
	public boolean isWithDeleted() {
		return withDeleted;
	}
	/**
	 * @param withDeleted the withDeleted to set
	 */
	public void setWithDeleted(boolean withDeleted) {
		this.withDeleted = withDeleted;
	}
	
	public boolean isOnlyManager() {
		return onlyManager;
	}
	public void setOnlyManager(boolean onlyManager) {
		this.onlyManager = onlyManager;
	}
	
}
