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
package ro.cs.cm.ws.client.om.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetPersonFromSearchResponse element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="GetPersonFromSearchResponse">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="persons" type="{http://localhost:8080/OM/services/schemas/messages}UserSimple"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */

/**
 * 
 * @author Coni
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
})
@XmlRootElement(name = "GetPersonFromSearchResponse")
public class GetPersonFromSearchResponse {
	
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    private List<UserSimple> persons;
    
    private WSSearchPersonBean wsSearchPersonBean;

	public List<UserSimple> getPersons() {
		return persons;
	}

	public void setPersons(List<UserSimple> persons) {
		this.persons = persons;
	}

	public WSSearchPersonBean getWsSearchPersonBean() {
		return wsSearchPersonBean;
	}

	public void setWsSearchPersonBean(WSSearchPersonBean wsSearchPersonBean) {
		this.wsSearchPersonBean = wsSearchPersonBean;
	}
    
}
