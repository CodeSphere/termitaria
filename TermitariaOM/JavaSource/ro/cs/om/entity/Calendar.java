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

import java.util.Set;


/**
 * @author matti_joona
 *
 */
public class Calendar {

	private int calendarId = -1;
	private String startWork;
	private String endWork;
	private String observation;
	private int organisationId ;
	private Set<FreeDay> freeDay;
	
	/**
	 * Constructor for Calendar class
	 * @author Adelina
	 */
	public Calendar(){
		this.calendarId = -1;
		this.startWork = "";
		this.endWork = "";
		this.observation = "";
		this.organisationId = -1;	
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
	public int getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}
	
	public static void main(String[] args) {
		for(int i = 779; i <= 1520; i++) {
			System.out.println("(" + i + ",' SRL','','M','-.; -.','@.ro'),");
		}
	}

	public void setFreeDay(Set<FreeDay> freeDay) {
		this.freeDay = freeDay;
	}

	public Set<FreeDay> getFreeDay() {
		return freeDay;
	}
}
