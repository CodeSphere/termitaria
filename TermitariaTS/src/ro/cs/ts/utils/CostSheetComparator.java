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
import java.util.Date;

import ro.cs.ts.entity.CostSheet;

public class CostSheetComparator {

	private static CostSheetComparator theInstance = null;
	
	static {
		theInstance = new CostSheetComparator();
	}
	
	private CostSheetComparator() {}
	
	public static CostSheetComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two cost sheets after their costSheetReporterName property
	 * @author Coni
	 * @return
	 */
	public Comparator costSheetReporterNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1 = ((CostSheet) o1).getCostSheetReporterName().toUpperCase();
				String name2 = ((CostSheet) o2).getCostSheetReporterName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two cost sheets after their activity property
	 * @author Coni
	 * @return
	 */
	public Comparator activityNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String activity1 = ((CostSheet) o1).getActivityName().toUpperCase();
				String activity2 = ((CostSheet) o2).getActivityName().toUpperCase();
				return activity1.compareTo(activity2);
			}
		};
	}
	
	/**
	 * Compares two cost sheets after their projectName property
	 * @author Coni
	 * @return
	 */
	public Comparator projectNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1 = ((CostSheet) o1).getProjectName().toUpperCase();
				String name2 = ((CostSheet) o2).getProjectName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two cost sheets after their billable property
	 * @author Coni
	 * @return
	 */
	public Comparator billableComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Character billable1 = ((CostSheet) o1).getBillable();
				Character billable2 = ((CostSheet) o2).getBillable();
				return billable1.compareTo(billable2);
			}
		};
	}
	
	/**
	 * Compares two cost sheets after their costSheetId property
	 * @author Coni
	 * @return
	 */
	public Comparator costSheetIdComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Integer id1 = ((CostSheet) o1).getCostSheetId();
				Integer id2 = ((CostSheet) o2).getCostSheetId();
				return id1.compareTo(id2);
			}
		};
	}
	
	/**
	 * Compares two cost sheets after their costSheetId property
	 * @author Coni
	 * @return
	 */
	public Comparator dateComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Date date1 = ((CostSheet) o1).getDate();
				Date date2 = ((CostSheet) o2).getDate();
				return date1.compareTo(date2);
			}
		};
	}
}
