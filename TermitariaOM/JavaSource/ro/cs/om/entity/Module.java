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

/**
 * Bean used for displaying Suite's Modules in the user login interface 
 * @author dd
 */
public class Module implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int moduleId;
	private String name;
	private String url;
	private String alt;
	private Set<Role> roles;
	private Set<Organisation> organisations;
	
	/**
	 * @return the moduleId
	 */
	public int getModuleId() {
		return moduleId;
	}
	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the alt
	 */
	public String getAlt() {
		return alt;
	}
	/**
	 * @param alt the alt to set
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
		
	/**
	 * @return the organisation
	 */
	public Set<Organisation> getOrganisations() {
		return organisations;
	}
	/**
	 * @param organisation the organisations to set
	 */
	public void setOrganisations(Set<Organisation> organisations) {
		this.organisations = organisations;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("moduleId = ")	.append(moduleId)	.append(", ");
		sb.append("name = ")		.append(name)		.append(", ");
		sb.append("url = ")			.append(url)		.append(", ");
		sb.append("alt = ")			.append(alt)		.append("]");
		return sb.toString();
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Module other = (Module) obj;
        if (this.moduleId != other.moduleId) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.moduleId;
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);        
        return hash;
    }

	
}
