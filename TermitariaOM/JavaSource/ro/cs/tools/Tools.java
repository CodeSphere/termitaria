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
package ro.cs.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

/**
 * @author dan.damian
 *
 */
public class Tools {

	
	
	private Tools() {};
	
	
	private static Tools instance;
	
	static {
		
		instance = new Tools();
	}
	
	public static Tools getInstance() {
		return instance;
	}

	
	public void printList(Log logger, List list) {
		if (logger != null) {
			logger.debug("------------------ printing list ---start----------------");
			if (list != null) {
				for(int i = 0; i < list.size(); i++) {
					logger.debug(": " + list.get(i));
				}
			} else {
				logger.debug("null");
			}
			logger.debug("------------------ printing list ---end------------------");
		} else {
			System.out.println("------------------ printing list ---start----------------");
			if (list != null) {
				for(int i = 0; i < list.size(); i++) {
					System.out.println(": " + list.get(i));
				}
			} else {
				System.out.println("null");
			}
			System.out.println("------------------ printing list ---end------------------");
		}
	}
	

	public void printSet(Log logger, Set set) {
		if (logger != null) {
			logger.debug("------------------ printing set ---start----------------");
			if (set != null) {
				Iterator it = set.iterator();
				while(it.hasNext()) {
					logger.debug(": " + it.next());
				}
			} else {
				logger.debug("null");
			}
			logger.debug("------------------ printing set ---end------------------");
		} else {
			System.out.println("------------------ printing set ---start----------------");
			if (set != null) {
				Iterator it = set.iterator();
				while(it.hasNext()) {
					System.out.println(": " + it.next());
				}
			} else {
				System.out.println("null");
			}
			System.out.println("------------------ printing set ---end------------------");
		}
	}
	
	
	public void printHash(Log logger, Map hashMap) {
		if (logger != null) {
			logger.debug("------------------ printing set ---start----------------");
			if (hashMap != null) {
				Set entrySet = hashMap.entrySet();
				Iterator it = entrySet.iterator();
				while(it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					logger.debug(": <" + entry.getKey() + ", " + entry.getValue() + ">");
				}
			} else {
				logger.debug("null");
			}
			logger.debug("------------------ printing set ---end------------------");
		} else {
			System.out.println("------------------ printing set ---start----------------");
			if (hashMap != null) { 
				Set entrySet = hashMap.entrySet();
				Iterator it = entrySet.iterator();
				while(it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					System.out.println(": <" + entry.getKey() + ", " + entry.getValue() + ">");
				}
			} else {
				System.out.println("null");
			}
			System.out.println("------------------ printing set ---end------------------");
		}
	}
	

	/**
	 * Transforms a int[] into Set<Integer>. Returns null if I have a zero length array.
	 *
	 * @author alu
	 * 
	 * @param array
	 * @return
	 */
	public Set<Integer> getSetFromArray(int[] array){
		if (array.length == 0){
			return null;
		}
		Set<Integer> set = new HashSet<Integer>();
		for(int i = 0; i < array.length; i++){
			set.add(array[i]);
		}
		return set;
	}
	
	public boolean voidListCondition(List list) {
		if (list == null || (list != null && list.size() ==0)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getStringConcatened(Object[] obj) {
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < obj.length; i++) {
			sb.append(obj[i]);
		}
		return sb.toString();
	}
	
	public boolean stringNotEmpty(String s) {
		if (s != null && s.length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public void breakOut(Log logger, String message) {
		if (logger != null) {
			logger.debug("---------- Press any key to continue ! ---------------");
			logger.debug(message);
		} else {
			System.out.println("---------- Press any key to continue ! ---------------");
			System.out.println(message);
		}
		
		
    	try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (logger != null) {
			logger.debug("---------- continue...-------------");
		} else {
			System.out.println("---------- continue...-------------");
		}
	}
	
	public void breakOut(String message) {
		breakOut(null, message);
	}
	
	public void breakOut() {
		breakOut(null, "");
	}
	
}
