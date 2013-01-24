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
package ro.cs.ts.entity;


/**
 * 
 * @author Andreea
 *
 */

public class RecordSession {

	private int recordSessionId;
	private int recordId;
	private String sessionId;
	
	public RecordSession() {
		
	}
	
	/**
	 * @return the recordSessionId
	 */
	public int getRecordSessionId() {
		return recordSessionId;
	}
	
	/**
	 * @param recordSessionId the recordSessionId to set
	 */
	public void setRecordSessionId(int recordSessionId) {
		this.recordSessionId = recordSessionId;
	}
	
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/**
	 * @return the recordId
	 */
	public int getRecordId() {
		return recordId;
	}
	
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	
	
}
