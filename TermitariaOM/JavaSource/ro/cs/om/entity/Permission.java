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

/**
 * @author matti_joona
 *
 */
public class Permission {

	private int permissionId = -1;
	private String name;
	private Localization description;
	private int moduleId;
	private String sketch;
	
	/**
	 * It is populated when a permission is deleted
	 */
	public Permission() {
		description = new Localization();
	}
	private Set<Role> roles;
	
	/**
	 * @return the permissionId
	 */
	public int getPermissionId() {
		return permissionId;
	}
	/**
	 * @param permissionId the permissionId to set
	 */
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
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
	
	public Localization getDescription() {
		return description;
	}
	
	public void setDescription(Localization description) {
		this.description = description;
	}
	/** 
	 * @return the module id
	 */
	public int getModuleId() {
		return moduleId;
	}
	/** 
	 * @param moduleId the module id to set
	 */
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}	
	
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getSketch(){
		return sketch;
	}
	
	public void setSketch(String sketch){
		this.sketch = sketch;
	}
	
	@Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + this.permissionId;
        hash = 61 * hash + (this.name != null ? this.name.hashCode() : 0);       
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Permission other = (Permission) obj;
        if (this.permissionId != other.permissionId) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }       
        return true;
    }
	    
	    /* (non-Javadoc)
	     * @see java.lang.Object#toString()
	     */
	    @Override
	    public String toString() {
	    	StringBuffer sb = new StringBuffer("[");
			sb.append(this.getClass().getSimpleName());
			sb.append(": ");
			sb.append("permissionId = ")	.append(permissionId)	.append(", ");
			sb.append("name = ")			.append(name)			.append(", ");
			//sb.append("localizationId = ")	.append(description.getLocalizationId())	.append(", ");
			sb.append("moduleId = ")		.append(moduleId)		.append("] ");
			return sb.toString();
	    }
}
