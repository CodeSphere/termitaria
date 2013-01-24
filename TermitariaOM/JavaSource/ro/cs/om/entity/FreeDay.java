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

import java.util.Date;

import ro.cs.om.common.IConstant;
import ro.cs.om.utils.common.StringUtils;

/**
 * @author matti_joona
 *
 */
public class FreeDay {

	private int freeDayId = -1;
	private Calendar calendar;
	private Date day;
	private String observation;
	/**
	 * Constructors
	 * @author Adelina
	 */
	public FreeDay(){
		this.freeDayId = -1;
		this.calendar = new Calendar();	
	}
	
	/**
	 * @return the freeDayId
	 */
	public int getFreeDayId() {
		return freeDayId;
	}
	/**
	 * @param freeDayId the freeDayId to set
	 */
	public void setFreeDayId(int freeDayId) {
		this.freeDayId = freeDayId;
	}
	/**
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}
	/**
	 * @param calendar the calendar to set
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	/**
	 * @return the day
	 */
	public Date getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(Date day) {
		this.day = day;
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
	
	public String getTokenizedObservation() {
		return StringUtils.getInstance().tokenizedString(this.observation, IConstant.FREEDAY_TEXT_AREA_ROW_SIZE);
	}

	public String getTruncatedTokenizedObservation() {
		return StringUtils.getInstance().truncatedString(this.observation, IConstant.FREEDAY_TEXT_AREA_ROW_SIZE, IConstant.FREEDAY_TEXT_AREA_SIZE);
	}
	

}
