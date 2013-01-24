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

import ro.cs.ts.entity.Exchange;

public class ExchangeComparator {

	private static ExchangeComparator theInstance = null;
	
	static {
		theInstance = new ExchangeComparator();
	}
	
	private ExchangeComparator() {}
	
	public static ExchangeComparator getInstance() {
		return theInstance;
	}
	
	/**
	 * Compares two exchanges after their projectName
	 * @author Coni
	 * @return
	 */
	public Comparator exchangeProjectNameComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String name1 = ((Exchange) o1).getProjectName().toUpperCase();
				String name2 = ((Exchange) o2).getProjectName().toUpperCase();
				return name1.compareTo(name2);
			}
		};
	}
}
