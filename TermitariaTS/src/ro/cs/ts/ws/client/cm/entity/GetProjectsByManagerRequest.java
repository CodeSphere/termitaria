/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.ws.client.cm.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for GetProjectsByManagerRequest element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="GetProjectsByManagerRequest">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="managerId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
})

@XmlRootElement(name = "GetProjectsByManagerRequest")
public class GetProjectsByManagerRequest {

	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages")
    protected int managerId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required=true)
	private boolean onlyManager = false;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required=true)
	private boolean isFinishedAndAbandoned;

	/**
	 * @return the managerId
	 */
	public int getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId the managerId to set
	 */
	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	/**
	 * @return the onlyManager
	 */
	public boolean isOnlyManager() {
		return onlyManager;
	}

	/**
	 * @param onlyManager the onlyManager to set
	 */
	public void setOnlyManager(boolean onlyManager) {
		this.onlyManager = onlyManager;
	}

	/**
	 * @return the isFinishedAndAbandoned
	 */
	public boolean isFinishedAndAbandoned() {
		return isFinishedAndAbandoned;
	}

	/**
	 * @param isFinishedAndAbandoned the isFinishedAndAbandoned to set
	 */
	public void setFinishedAndAbandoned(boolean isFinishedAndAbandoned) {
		this.isFinishedAndAbandoned = isFinishedAndAbandoned;
	}	
}
