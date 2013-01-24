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
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.cs.om.business.BLRole;
import ro.cs.om.common.IConstant;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.utils.common.StringUtils;


/**
 * @author matti_joona
 *
 */
public class Role implements Serializable{

	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());
	
	private int roleId;
	private String name;	
	private Localization description;
	private String observation;
	private Set<Permission> permissions = new HashSet<Permission>();
	private Set<Person> persons;
	private Module module;
	private Organisation organisation;	
	private String sketch;
	private Boolean isDefault = null;
	private int status;
	private int moduleId;
	
	public Role(){
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
	public Set<Permission> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
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
	
	public String getSketch(){
		return sketch;
	}
	
	public void setSketch(String sketch){
		this.sketch = sketch;
	}
		
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}


	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public int getModuleId() {
		return moduleId;
	}	
	
	public String getTokenizedSketch() {
		return StringUtils.getInstance().tokenizedString(this.sketch, IConstant.ROLE_TEXT_AREA_ROW_SIZE);
	}

	public String getTruncatedTokenizedSketch() {
		return StringUtils.getInstance().truncatedString(this.sketch, IConstant.ROLE_TEXT_AREA_ROW_SIZE, IConstant.ROLE_TEXT_AREA_SIZE);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("roleId = ")		.append(roleId)		.append(", ");
		sb.append("name = ")		.append(name)		.append(", ");		
		sb.append("observation = ")	.append(observation).append(", ");
		sb.append("permissions = ")	.append((permissions != null ? permissions.size():" null")) .append(", ");
		return sb.toString();
	}
	
	/**
	 * Override equals(Object obj) method
	 * 
	 * @author Adelina
	 * param Object
	 * return boolean
	 */
	 @Override
	    public boolean equals(Object obj) {
	        if (obj == null) {
	            return false;
	        }
	        if (getClass() != obj.getClass()) {
	            return false;
	        }
	        final Role other = (Role) obj;
	        	            		        
	        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
	            return false;
	        }	        	        
	        return true;
	    }

	 /**
	  * Override hashCode() method
	  * 
	  * @author Adelina
	  * return int
	  */
	    @Override
	    public int hashCode() {
	        int hash = 5;	        
	        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);	        	       
	        return hash;
	    }

		public void setPersons(Set<Person> persons) {
			this.persons = persons;
		}

		public Set<Person> getPersons() {
			return persons;
		}
		
	
		/**
		 * Checks if a role is a default 
		 * 
		 * @author Adelina
		 * 
		 * @return boolean
		 */
		public boolean getIsDefault() {
			logger.debug("getIsDefault - START -");		
						
			try{
				List<Role> roles = BLRole.getInstance().getDefaultRoles(getModule().getModuleId());
				if(isDefault != null) {
					return isDefault;
				} else if(roles != null && roles.size() > 0) {
					for(Role role : roles) {
						if(getName().equals(role.getName())) {
							isDefault = true;
							break;
						} else {
							isDefault = false;
						}
					}				
				} else {
					isDefault = false;
				}
			} catch(BusinessException bexc){ 
				logger.error("", bexc);
			}
			catch(Exception e) {
				logger.error("", e);
			}
			
			logger.debug("getIsDefault - END -");
			
			return isDefault;
		}
}
