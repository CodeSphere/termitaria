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

import ro.cs.ts.cm.Client;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.common.IConstant;

/**
 * 
 * @author Adelina
 *
 */
public class ProjectComparator extends ApplicationObjectSupport {
	
	private static ProjectComparator theInstance = null;
	
	private static final String CLIENT_FROM_ORG		= "project.from.organization";	
	
	static{
		theInstance = new ProjectComparator();
	}
	
	private ProjectComparator() {}
	
	public static ProjectComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two projects after their name
	 * @author Adelina
	 * @return
	 */
	public Comparator nameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1 = ((Project)o1).getName().toUpperCase();
				String name2 = ((Project)o2).getName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
	
	

	/**
	 * Compares two projects after their name
	 * @author Adelina
	 * @return
	 */
	public Comparator clientNameComparator(final MessageSource messageSource) {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Client client1 = ((Project) o1).getClient();
				Client client2 = ((Project) o2).getClient();				
				
				if(client1.getClientId() != 0) {
					if(client1.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
						((Project) o1).setClientName(client1.getC_name());
					} if (client1.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
						((Project) o1).setClientName(client1.getP_firstName().concat(" ").concat(client1.getP_lastName()));						
					}
				} else {
					((Project) o1).setClientName(messageSource.getMessage(CLIENT_FROM_ORG, null, null));
				}
				
				if(client2.getClientId() != 0) {
					if(client2.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
						((Project) o2).setClientName(client2.getC_name());
					} if (client2.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
						((Project) o2).setClientName(client2.getP_firstName().concat(" ").concat(client2.getP_lastName()));						
					}
				} else {
					((Project) o2).setClientName(messageSource.getMessage(CLIENT_FROM_ORG, null, null));
				}
				
				String name1 = ((Project) o1).getClientName().toUpperCase();
				String name2 = ((Project) o2).getClientName().toUpperCase();
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
	public Comparator statusComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				String status1 = String.valueOf(((Project) o1).getStatus()).toUpperCase();
				String status2 = String.valueOf(((Project) o2).getStatus()).toUpperCase();
				return status1.compareTo(status2);
			}
		};
	}
	
	/**
	 * Compares two projects after their projectId
	 * 
	 * @author coni
	 * 
	 * @return
	 */
	public Comparator projectIdComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {				
				Integer id1 = Integer.valueOf(((Project) o1).getProjectId());
				Integer id2 = Integer.valueOf(((Project) o2).getProjectId());
				return id1.compareTo(id2);
			}
		};
	}


}
