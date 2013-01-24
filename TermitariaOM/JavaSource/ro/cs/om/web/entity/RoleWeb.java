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
package ro.cs.om.web.entity;

import java.util.Set;

import ro.cs.om.entity.Localization;

/**
 * @author alu
 *
 */
public class RoleWeb {
	
	private int roleId = -1;
	private String name;
	private Localization description;	
	private String observation;
	private Set<Integer> permissions;
	private int moduleId = -1;
	private int organisationId = -1;
	private String sketch;
	private int status;
	
	public RoleWeb(){
		description = new Localization();
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Localization getDescription() {
		return description;
	}

	public void setDescription(Localization description) {
		this.description = description;
	}
	
	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Set getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Integer> permissions) {
		this.permissions = permissions;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public int getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	public String getSketch(){
		return sketch;
	}
	
	public void setSketch(String sketch){
		this.sketch = sketch;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("roleId = ")			.append(roleId)			.append(", ");
		sb.append("name = ")			.append(name)			.append(", ");
		sb.append("localizationId = ")	.append(description.getLocalizationId())	.append(", ");
		sb.append("observation = ")		.append(observation)	.append(", ");
		sb.append("moduleId = ")		.append(moduleId)		.append(", ");
		sb.append("organisationId = ")	.append(organisationId) .append(", ");
		sb.append("permissions = ")		.append((permissions != null ? permissions.size():" null"));
		if (permissions != null) {
			for(Integer i : permissions) {
				sb.append("permisssion " + i);
			}
		}
		return sb.toString();
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
