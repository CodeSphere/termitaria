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

import ro.cs.cm.common.ApplicationObjectSupport;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.entity.Client;

public class ClientComparator extends ApplicationObjectSupport {
	
	private static ClientComparator theInstance = null;
	
	static{
		theInstance = new ClientComparator();
	}
	
	private ClientComparator() {}
	
	public static ClientComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two client after their name
	 * @author Coni
	 * @return
	 */
	public Comparator clientNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Client object1 = (Client) o1;
				Client object2 = (Client) o2;
				String name1 = null;
				String name2 = null;
				
				if (object1.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					name1 = object1.getC_name().toUpperCase();
				} else if (object1.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
					name1 = object1.getP_firstName().concat(" ").concat(object1.getP_lastName()).toUpperCase();
				}
				
				if (object2.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					name2 = object2.getC_name().toUpperCase();
				} else if (object2.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
					name2 = object2.getP_firstName().concat(" ").concat(object1.getP_lastName()).toUpperCase();
				}
				return name1.compareTo(name2);
			}
		};
	}
}
