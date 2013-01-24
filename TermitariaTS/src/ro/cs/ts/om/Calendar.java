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
package ro.cs.ts.om;

public class Calendar {

	private int calendarId;
	private String startWork;
	private String endWork;	
	private int organisationId;
	
	public Calendar() {
		
	}

	/**
	 * @return the calendarId
	 */
	public int getCalendarId() {
		return calendarId;
	}

	/**
	 * @param calendarId the calendarId to set
	 */
	public void setCalendarId(int calendarId) {
		this.calendarId = calendarId;
	}

	/**
	 * @return the startWork
	 */
	public String getStartWork() {
		return startWork;
	}

	/**
	 * @param startWork the startWork to set
	 */
	public void setStartWork(String startWork) {
		this.startWork = startWork;
	}

	/**
	 * @return the endWork
	 */
	public String getEndWork() {
		return endWork;
	}

	/**
	 * @param endWork the endWork to set
	 */
	public void setEndWork(String endWork) {
		this.endWork = endWork;
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
	
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("calendarId = ")			.append(calendarId)			.append(", ");
		sb.append("organizationId = ")		.append(organisationId)		.append(", ");		
		sb.append("startWork = ")			.append(startWork)			.append(", ");		
		sb.append("endWork = ")				.append(endWork)			.append("] ");	
		
		return sb.toString();
	}
    
}
