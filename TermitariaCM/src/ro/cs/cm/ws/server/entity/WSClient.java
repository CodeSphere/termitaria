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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for organisationSimple complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="organisationSimple">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="clientId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="c_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="p_firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="p_lastName" type="{http://www.w3.org/2001/XMLSchema}string"/> *        
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsClient", propOrder = {

})

public class WSClient {
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private int clientId;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private byte type;	
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private byte status;	
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private String p_firstName;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private String p_lastName;
	
	@XmlElement(namespace = "http://localhost:8080/CM/services/schemas/messages", required = true)
	private String c_name;

	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * @return the p_firstName
	 */
	public String getP_firstName() {
		return p_firstName;
	}

	/**
	 * @param name the p_firstName to set
	 */
	public void setP_firstName(String name) {
		p_firstName = name;
	}

	/**
	 * @return the p_lastName
	 */
	public String getP_lastName() {
		return p_lastName;
	}

	/**
	 * @param name the p_lastName to set
	 */
	public void setP_lastName(String name) {
		p_lastName = name;
	}

	/**
	 * @return the c_name
	 */
	public String getC_name() {
		return c_name;
	}

	/**
	 * @param c_name the c_name to set
	 */
	public void setC_name(String c_name) {
		this.c_name = c_name;
	}
	
	
}
