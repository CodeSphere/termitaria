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
package ro.cs.cm.ws.server.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Retrieves a list of team members for specific search criteria
 * @author Coni
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
})
@XmlRootElement(name = "GetTeamMemberFromSearchRequest")
public class GetTeamMemberFromSearchRequest {

	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
	private WSSearchTeamMemberBean wsSearchTeamMemberBean;

	public WSSearchTeamMemberBean getWsSearchTeamMemberBean() {
		return wsSearchTeamMemberBean;
	}

	public void setWsSearchTeamMemberBean(
			WSSearchTeamMemberBean wsSearchTeamMemberBean) {
		this.wsSearchTeamMemberBean = wsSearchTeamMemberBean;
	}
	
}
