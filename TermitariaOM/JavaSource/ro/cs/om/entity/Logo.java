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

import java.sql.Timestamp;

/**
 * @author dan.damian
 *
 */
public class Logo {

	private int logoId;
	byte[] picture;
	private Timestamp dateCreated;
	private Timestamp dateModified;
	private int organisationId;
	private String extension;
	
	
	/**
	 * @return the logoId
	 */
	public int getLogoId() {
		return logoId;
	}



	/**
	 * @param logoId the logoId to set
	 */
	public void setLogoId(int logoId) {
		this.logoId = logoId;
	}



	/**
	 * @return the picture
	 */
	public byte[] getPicture() {
		return picture;
	}



	/**
	 * @param picture the picture to set
	 */
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}



	/**
	 * @return the dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}



	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}



	/**
	 * @return the dateModified
	 */
	public Timestamp getDateModified() {
		return dateModified;
	}



	/**
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}



	/**
	 * @return the organisationId
	 */
	public int getOrganisationId() {
		return organisationId;
	}



	/**
	 * @param organisationId the organisationId to set
	 */
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}



	
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}



	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}



	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("logoId = ")      		.append(logoId)	      	.append(", ");
		sb.append("picture (size bytes)= ")	.append(picture.length) .append(", ");
		sb.append("extension = ")			.append(extension) .append(", ");
		sb.append("organisationId = ")  	.append(organisationId)	.append("]");
		return sb.toString();
	}

	
	
	
}
