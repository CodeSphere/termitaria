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
import ro.cs.ts.entity.Record;

/**
 * 
 * @author Coni
 *
 */
public class RecordComparator extends ApplicationObjectSupport {

	private static RecordComparator theInstance = null;
	
	static{
		theInstance = new RecordComparator();
	}
	
	private RecordComparator() {}
	
	public static RecordComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two records after their owner name
	 * @author Coni
	 * @return
	 */
	public Comparator recordOwnerNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				String name1 = ((Record)o1).getRecordOwnerName().toUpperCase();
				String name2 = ((Record)o2).getRecordOwnerName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two records after their activity name
	 * @author Coni
	 * @return
	 */
	public Comparator activityNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1 = ((Record)o1).getActivity().getName().toUpperCase();
				String name2 = ((Record)o2).getActivity().getName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two records after their project name
	 * @author Coni
	 * @return
	 */
	public Comparator projectNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1 = ((Record)o1).getProjectName().toUpperCase();
				String name2 = ((Record)o2).getProjectName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two records after their isBillable property
	 * @author Coni
	 * @return
	 */
	public Comparator isBillableComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Boolean isBillable1 = ((Record)o1).getIsBillable();
				Boolean isBillable2 = ((Record)o2).getIsBillable();
				return isBillable1.compareTo(isBillable2);
			}
		};
	}
	
	/**
	 * Compares two records after their recordId
	 * @author Coni
	 * @return
	 */
	public Comparator recordIdComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				Integer id1 = ((Record)o1).getRecordId();
				Integer id2 = ((Record)o2).getRecordId();
				return id1.compareTo(id2);
			}
		};
	}
}
