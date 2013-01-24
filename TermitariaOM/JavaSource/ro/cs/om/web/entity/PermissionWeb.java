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

import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Localization;
import ro.cs.om.entity.Module;
import ro.cs.om.utils.common.StringUtils;

/**
 * @author alu
 *
 */
public class PermissionWeb {
	
	private int permissionId = -1;
	private String name;
	private Localization description;
	private Module module;
	private String sketch;
	
	public PermissionWeb(){
		description = new Localization();
	}
	
	public int getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
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
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	
	public String getSketch(){
		return sketch;
	}
	
	public void setSketch(String sketch){
		this.sketch = sketch;
	}
	
	public String getTokenizedSketch() {
		return StringUtils.getInstance().tokenizedString(this.sketch, IConstant.PERMISSION_TEXT_AREA_ROW_SIZE);
	}

	public String getTruncatedTokenizedSketch() {
		return StringUtils.getInstance().truncatedString(this.sketch, IConstant.PERMISSION_TEXT_AREA_ROW_SIZE, IConstant.PERMISSION_TEXT_AREA_SIZE);
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
        final PermissionWeb other = (PermissionWeb) obj;
        if (this.permissionId != other.permissionId) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }      
        return true;
    }
    
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("permissionId = ")	.append(permissionId)						.append(", ");
		sb.append("name = ")			.append(name)								.append(", ");
		//sb.append("localization = ")	.append(localization.getLocalizationId())	.append(", ");		
		return sb.toString();
    }


}
