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

import org.springframework.context.MessageSource;

import ro.cs.ts.cm.Project;
import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.om.Person;

public class PersonComparator extends ApplicationObjectSupport {
	
	private static PersonComparator theInstance = null;
	
	private static final String CLIENT_FROM_ORG		= "project.from.organization";	
	
	static{
		theInstance = new PersonComparator();
	}
	
	private PersonComparator() {}
	
	public static PersonComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two persons after their last name
	 * @author Adelina
	 * @return
	 */
	public Comparator lastNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String lastName1 = ((Person)o1).getLastName().toUpperCase();
				String lastName2 = ((Person)o2).getLastName().toUpperCase();
				return lastName1.compareTo(lastName2);
			}
		};
	}
	
	/**
	 * Compares two persons after their first names
	 * @author Adelina
	 * @return
	 */
	public Comparator firstNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String firstName1 = ((Person)o1).getFirstName().toUpperCase();
				String firstName2 = ((Person)o2).getFirstName().toUpperCase();
				return firstName1.compareTo(firstName2);
			}
		};
	}
	
	/**
	 * Compares two person after their project's names
	 * @author Adelina
	 * @return
	 */
	public Comparator projectNameComparator(final MessageSource messageSource) {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				
				Project project1 = ((Person)o1).getProject();
				Project project2 = ((Person)o2).getProject();
				
				if(((Person)o1).getProjectId() != 0) {
					((Person)o1).setProjectName(project1.getName());
				} else {
					((Person)o1).setProjectName(messageSource.getMessage(CLIENT_FROM_ORG, null, null));
				}
				
				if(((Person)o2).getProjectId() != 0) {
					((Person)o2).setProjectName(project2.getName());
				} else {
					((Person)o2).setProjectName(messageSource.getMessage(CLIENT_FROM_ORG, null, null));
				}
								
				String projectName1 = ((Person)o1).getProjectName().toUpperCase();
				String projectName2 = ((Person)o2).getProjectName().toUpperCase();
				return projectName1.compareTo(projectName2);
			}
		};
	}
}

