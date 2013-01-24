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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class is a container for OrganisatonContainers.
 * It's only purpose is to map to xml root element <organisationContainers></organisationContianers>
 * 
 * @author dan.damian
 *
 */
@XmlRootElement
@XmlType(name="organisationContainers")
public class OrganisationContainers {
	
	List<OrganisationContainer> orgCs = null;

	/**
	 * @return the orgCs
	 */
	@XmlElement(name="organisationContainer")
	public List<OrganisationContainer> getOrgCs() {
		return orgCs;
	}

	/**
	 * @param orgCs the orgCs to set
	 */
	public void setOrgCs(List<OrganisationContainer> orgCs) {
		//Setted durin IMPORT Unmarshall phase
		this.orgCs = orgCs;
	}
	
	

}
