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


/**
 * @author dd
 * 
 */
public class SearchOrganisationBean extends PaginationBean {

	//private int organisationId;
	private Integer[] organisationId;
	private String name;
	private String address;
	private String phone;
	private String fax;
	private String email;
	private String observation;
	private byte type;
	
	/**
	 * @return the organisationId
	 */
	public Integer[] getOrganisationId() {
		return organisationId;
	}
	/**
	 * @param organisationId the organisationId to set
	 */
	public void setOrganisationId(Integer[] organisationId) {
		this.organisationId = organisationId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}
	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}	
	/**
	 * 
	 * @param type
	 */
	public void setType(byte type){
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public byte getType(){
		return type;
	}
	
	/**
	 * @author Adelina
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("organisationId = ")	.append(organisationId)	.append(", ");		
		sb.append("name = ")			.append(name)			.append(", ");
		sb.append("address = ")			.append(address)		.append(", ");
		sb.append("phone = ")			.append(phone)			.append(", ");
		sb.append("fax = ")				.append(fax)			.append(", ");
		sb.append("email = ")			.append(email)			.append(", ");
		sb.append("observation = ")		.append(observation)	.append(", ");
		
		sb.append("type = ")			.append(type)			.append("]");
		return sb.toString();
	}

}
