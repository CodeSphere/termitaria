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
package ro.cs.ts.cm;


public class Client {
	
	private int clientId = -1;	
	private byte type;	
	private byte status;	
	private String p_firstName;
	private String p_lastName;		
	private String c_name;

	public Client() {
		
	}

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
	
	@Override
	public String toString() {
		return "Client [c_name=" + c_name + ", clientId=" + clientId			
				+ ", p_firstName=" + p_firstName
				+ ", p_lastName=" + p_lastName + ", p_sex=" + 
				", status=" + status + ", type=" + type
				+ "]";
	}
	

}
