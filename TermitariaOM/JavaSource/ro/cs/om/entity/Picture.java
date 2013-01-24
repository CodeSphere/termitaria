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
public class Picture {

	private int pictureId;
	byte[] picture;
	private String name;
	private String extension;
	private Timestamp dateCreated;
	private Timestamp dateModified;
	private int width;
	private int height;
	private int personId;
	
	/**
	 * @return the pictureId
	 */
	public int getPictureId() {
		return pictureId;
	}
	/**
	 * @param pictureId the pictureId to set
	 */
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
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
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
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
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("pictureId = ")      		.append(pictureId)      .append(", ");
		sb.append("picture (size bytes)= ")	.append(picture.length) .append(", ");
		sb.append("name = ")           		.append(name)           .append(", ");
		sb.append("extension = ")      		.append(extension)      .append(", ");
		sb.append("dateCreated = ")    		.append(dateCreated)    .append(", ");
		sb.append("dateModified = ")   		.append(dateModified)   .append(", ");
		sb.append("width = ")          		.append(width)          .append(", ");
		sb.append("height = ")         		.append(height)         .append("]");
		return sb.toString();
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int getPersonId() {
		return personId;
	}

	
	
	
}
