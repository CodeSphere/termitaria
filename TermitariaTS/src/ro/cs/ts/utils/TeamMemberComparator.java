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

import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.ApplicationObjectSupport;

public class TeamMemberComparator extends ApplicationObjectSupport{
	
	private static TeamMemberComparator theInstance = null;
	
	static{
		theInstance = new TeamMemberComparator();
	}
	
	private TeamMemberComparator() {}
	
	public static TeamMemberComparator getInstance() {
		return theInstance;
	}

	/**
	 * Compares two persons after their last name
	 * @author Adelina
	 * @return
	 */
	public Comparator<TeamMember> lastNameComparator() {
		return new Comparator<TeamMember>() {
			public int compare(TeamMember o1, TeamMember o2) {
				String lastName1 = o1.getLastName().toUpperCase();
				String lastName2 = o2.getLastName().toUpperCase();
				return lastName1.compareTo(lastName2);
			}
		};
	}

}
