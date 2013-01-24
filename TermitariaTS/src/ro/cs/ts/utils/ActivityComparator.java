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

import ro.cs.ts.entity.Activity;

public class ActivityComparator {
			
	private static ActivityComparator theInstance = null;
	
	static {
		theInstance = new ActivityComparator();
	}
	
	private ActivityComparator() {}
	
	public static ActivityComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two activities after their projectName
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public Comparator activityProjectNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {								
				String name1 = ((Activity) o1).getProjectName().toUpperCase();
				String name2 = ((Activity) o2).getProjectName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two activities after their name
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public Comparator activityNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				String name1 = ((Activity) o1).getName().toUpperCase();
				String name2 = ((Activity) o2).getName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two activities after if they are billable or not
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public Comparator activityBillableComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				String billable1 = ((Activity) o1).getBillable().toString().toUpperCase();
				String billable2 = ((Activity) o2).getBillable().toString().toUpperCase();
				return billable2.compareTo(billable1);
			}
		};
	}

}
