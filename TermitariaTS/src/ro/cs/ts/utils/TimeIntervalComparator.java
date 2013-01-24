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

import java.util.Comparator;

import ro.cs.ts.common.ApplicationObjectSupport;

public class TimeIntervalComparator extends ApplicationObjectSupport {

	private static TimeIntervalComparator theInstance = null;
		
	static{
		theInstance = new TimeIntervalComparator();
	}
	
	private TimeIntervalComparator() {}
	
	public static TimeIntervalComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two hours
	 * 
	 * @author Adelina
	 * @return
	 */
	public Comparator hoursComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Long hours1 = ((TimeInterval)o1).getHours();
				Long hours2 = ((TimeInterval)o2).getHours();
				return hours1.compareTo(hours2);
			}
		};
	}
	
	/**
	 * Compares two hours
	 * 
	 * @author Adelina
	 * @return
	 */
	public Comparator minutesComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Long minutes1 = ((TimeInterval)o1).getMinutes();
				Long minutes2 = ((TimeInterval)o2).getMinutes();
				return minutes1.compareTo(minutes2);
			}
		};
	}

}
