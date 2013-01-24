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
package ro.cs.cm.utils;

import java.util.Comparator;

import ro.cs.cm.entity.Project;

public class ProjectComparator {

	private static ProjectComparator theInstance = null;
	
	static {
		theInstance = new ProjectComparator();
	}
	
	private ProjectComparator() {}
	
	public static ProjectComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two client after their name
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public Comparator projectClientNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1 = ((Project) o1).getClientName().toUpperCase();
				String name2 = ((Project) o2).getClientName().toUpperCase();
				return name1.compareTo(name2);

			}
		};
	}
	
	/**
	 * Compares two projects after their name
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public Comparator projectNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				String name1 = ((Project) o1).getName().toUpperCase();
				String name2 = ((Project) o2).getName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	/**
	 * Compares two projects after their status
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public Comparator projectStatusComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				String status1 = String.valueOf(((Project) o1).getStatus()).toUpperCase();
				String status2 = String.valueOf(((Project) o2).getStatus()).toUpperCase();
				return status1.compareTo(status2);
			}
		};
	}

}
