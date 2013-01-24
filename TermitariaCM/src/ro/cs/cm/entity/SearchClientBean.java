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
package ro.cs.cm.entity;

import ro.cs.cm.entity.PaginationBean;

/**
 * 
 * @author Coni
 *
 */
public class SearchClientBean extends PaginationBean{

	private Integer[] clientId;
	private int organizationId = -1;
	private String c_name;
	private String address;
	private String email;
	private byte type;
	private byte status;
	private String p_firstName;
	private String p_lastName;
	
	public Integer[] getClientId() {
		return clientId;
	}
	public void setClientId(Integer[] clientId) {
		this.clientId = clientId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String cName) {
		c_name = cName;
	}
	public String getP_firstName() {
		return p_firstName;
	}
	public void setP_firstName(String pFirstName) {
		p_firstName = pFirstName;
	}
	public String getP_lastName() {
		return p_lastName;
	}
	public void setP_lastName(String pLastName) {
		p_lastName = pLastName;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("c_name = ")    		.append(c_name)                 .append(", ");
		sb.append("p_firstName = ")    	.append(p_firstName)            .append(", ");
		sb.append("p_lastName = ")    	.append(p_lastName)             .append(", ");
		sb.append("address = ") 		.append(address)                .append(", ");
		sb.append("email = ")   		.append(email)                  .append(", ");
		sb.append("organizationId = ")  .append(organizationId)			.append(", ");
		sb.append("status = ")			.append(status)					.append(", ");
		sb.append("type = ")    .append(String.valueOf(type))   .append("]");
		return sb.toString();
	}
}
