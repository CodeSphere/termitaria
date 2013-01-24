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
 * @author mitziuro
 *
 */
public class SettingNom {

	private String code;
	private String value;
	
	/**
	 * set code
	 * @autho mitziuro
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * get code
	 * @author mitziuro
	 * @return
	 */
	public String getCode() {
		return code;
	}
	/**
	 * set value
	 * @author mitziuro
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * get value
	 * @author mitziuro
	 * @return
	 */
	public String getValue() {
		return value;
	}
}
