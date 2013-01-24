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
package ro.cs.om.ws.client.dme.entity;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author mitziuro
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DMWorkspaces", propOrder = {

})
public class DMWorkspaces {
	
	@XmlElement(namespace = ObjectFactory.NAMESPACE)
	private ArrayList<String> workspaces = new ArrayList<String>();

	public void setWorkspaces(ArrayList<String> workspaces) {
		this.workspaces = workspaces;
	}

	public ArrayList<String> getWorkspaces() {
		return workspaces;
	}	
}
