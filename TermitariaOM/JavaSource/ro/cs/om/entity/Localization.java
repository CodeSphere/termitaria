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

import java.io.Serializable;

/**
 * Bean for localization
 * 
 * @author Adelina
 * 
 */
public class Localization implements Serializable {

	private static final long serialVersionUID = 1L;
	/** the identifier for the localization bean */
	private int localizationId;
	/** the description in romanian */
	private String ro;
	/** the description in english */
	private String en;

	/**
	 * Constructors...
	 */
	public Localization() {

	}

	public int getLocalizationId() {
		return localizationId;
	}

	public void setLocalizationId(int localizationId) {
		this.localizationId = localizationId;
	}

	public String getRo() {
		return ro;
	}

	public void setRo(String ro) {
		this.ro = ro;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("localizationId = ")			.append(localizationId)			.append(", ");
		sb.append("en = ")						.append(en)						.append(", ");
		sb.append("ro = ")						.append(ro)						.append("] ");
		
		return sb.toString();
	}

}
