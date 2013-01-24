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

import java.util.Set;

import ro.cs.om.common.IConstant;
import ro.cs.om.utils.common.StringUtils;

public class UserGroup {
	private int userGroupId = -1;
	private Organisation organisation;
	private String name;
	private int status;
	private String description;
	private Set<Person> persons;
	private String panelHeaderName;
	
	public Set<Person> getPersons() {
		return persons;
	}
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
	public int getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	public String getTokenizedDescription() {
		return StringUtils.getInstance().tokenizedString(this.description, IConstant.USERGROUP_TEXT_AREA_ROW_SIZE);
	}
	
	public String getTruncatedTokenizedDescription() {
		return StringUtils.getInstance().truncatedString(this.description, IConstant.USERGROUP_TEXT_AREA_ROW_SIZE, IConstant.USERGROUP_TEXT_AREA_SIZE);
	}
	
	/**
	 * @author Adelina
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" [");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("userGroupId = ")		.append(userGroupId)		.append(", ");
		sb.append("name = ")			.append(name)				.append(", ");
		sb.append("status = ")			.append(status)				.append(", ");
		
		sb.append("description = ")		.append(description)		.append(", ");	
		sb.append("status = ")			.append(status)				.append(", ");
		sb.append("persons = ")			.append((persons != null?persons.size():"0"))		.append("] ");		
		
		return sb.toString();
	}
	
}
