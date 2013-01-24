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
package ro.cs.ts.utils;


public class TimeInterval {
	
	private Long hours;
	private Long minutes;
	
	public TimeInterval(Long hours, Long minutes) {
		this.hours = hours;
		this.minutes = minutes;		
	}

	/**
	 * @return the hours
	 */
	public Long getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(Long hours) {
		this.hours = hours;
	}

	/**
	 * @return the minutes
	 */
	public Long getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(Long minutes) {
		this.minutes = minutes;
	}
	
	@Override
    public boolean equals (Object obj){
    	if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TimeInterval other = (TimeInterval) obj;
		if (this.hours != other.hours) {
			return false;
		}
		if (this.minutes != other.minutes) {
			return false;
		}
			
		return true;
	}
	
	@Override
	public int hashCode(){
		int hash = 5;		
		hash = 67 * hash + this.hours.intValue();
		hash = 67 * hash + this.minutes.intValue();
		return hash;
	}			
	

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("hours = ")					.append(hours)					.append(", ");
		sb.append("minutes = ")					.append(minutes)				.append("] ");				
			
		return sb.toString();
	}
	
}
