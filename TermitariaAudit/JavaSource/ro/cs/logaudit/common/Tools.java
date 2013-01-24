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
package ro.cs.logaudit.common;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

/**
 * @author Adelina
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
	
	public double getDoubleValue(Object obj){
		if (obj != null){
			return ((Double) obj).doubleValue();
		}else {
			return -1.0;
		}
	}
	
	public String getStringValue(Object obj){
		if (obj != null){
			return (String) obj;
		}else return "";
	}
		
	
	public int getIntValue(Object obj){
		if (obj != null){
			return ((Integer) obj).intValue();
		}else{
			return -1;
		}
	}
	
	public long getLongValue(Object obj){
		if (obj != null){
			return ((Long) obj).longValue();
		}else{
			return -1;
		}
	}
	
	public short getShortValue(Object obj){
		if (obj != null){
			return ((Short) obj).shortValue();
		}else {
			return (short)-1;
		}
	}
	
	public String getDoubleWithoutComma(double number) {
		Locale localeRo = new Locale("ro","RO","");
		NumberFormat nf = NumberFormat.getInstance(localeRo);
		nf.setMaximumFractionDigits(-1);
		nf.setGroupingUsed(false);
		String s = "000";
		s = nf.format(number);
		return s;
	}
	
	public String getDoubleWithoutComma(Double number) {
		if (number != null) {
			return getDoubleWithoutComma(number.doubleValue());
		}else {
			return "000";
		}
	}
	
	public Double extractNumber(String param1){
		String validChars = "0123456789";
	    StringBuffer rez = new StringBuffer();
	    boolean isNumber = true;
	    for (int i = 0; i < param1.length() && isNumber; i++){ 
	      char c = param1.charAt(i);
	      if (validChars.indexOf(c) == -1){
	        isNumber = false;
	      } else {
	        isNumber = true;
	        rez.append(c);
	      }
	    }
	    return new Double(rez.toString());
	}
	
	public String replaceSpecialChars(String field) {
		if (field != null) {
			return field.replaceAll("\"", "&#34;").replaceAll("'", "&#39;");
		}else {
			return "";
		}
	}	
	
	public int getNumberOfPages(long totalRecordsNo, int recordsPerPage) {
		if (recordsPerPage == 0) return -1;
		int numberOfPages = new Long(totalRecordsNo / recordsPerPage).intValue();
		if (totalRecordsNo % recordsPerPage > 0) {
			numberOfPages++;
		}
		return numberOfPages;
	}
	
	public static void printGettersSetters(Class aClass){
		Method[] methods = aClass.getMethods();

		for(int i =0; i < methods.length; i++) {
			if(isGetter(methods[i])) System.out.println("getter: " + methods[i]);
		    if(isSetter(methods[i])) System.out.println("setter: " + methods[i]);
		 }
	}

	public static boolean isGetter(Method method){
		if(!method.getName().startsWith("get"))      return false;
		if(method.getParameterTypes().length != 0)   return false;  
		if(void.class.equals(method.getReturnType())) return false;
			return true;
	}

	public static boolean isSetter(Method method){
	  if(!method.getName().startsWith("set")) return false;
	  if(method.getParameterTypes().length != 1) return false;
	  return true;
	}
	
	public java.sql.Date getSqlDate(String dateString, String dateFormat) {
		java.util.Date utilDate = null;
		java.sql.Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			utilDate = sdf.parse(dateString);
		} catch (ParseException e) {
		}
		if (utilDate != null) {
			result = new java.sql.Date(utilDate.getTime());
		}
		return result;
	}
	
	public Timestamp getTimestamp(String dateString, String dateFormat) throws ParseException {
		java.util.Date utilDate = null;
		Timestamp result = null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			utilDate = sdf.parse(dateString);
			result = new Timestamp(utilDate.getTime());
		} catch (ParseException e) {
			throw e;
		}
		return result;
	}
	
	public String getRandomCNP() {
		StringBuffer sb = new StringBuffer();
		sb.append( (int) (Math.random()* 10 % 2 + 1)); //sex
		sb.append( (int) (50 + Math.random() * 100 % 30)); // an
		int luna = (int) (1 + Math.random() * 100 % 12);
		if (luna < 10) sb.append("0");
		sb.append(luna); // luna
		int zi = (int) (1 + Math.random() * 100 % 30);
		if (zi < 10) sb.append("0");
		sb.append(zi); // zi
		sb.append( (int) (1 + Math.random() * 100 % 40));
		sb.append( (int) (1 + Math.random() * 100 % 40));
		sb.append( (int) (1 + Math.random() * 100 % 40));
		return sb.toString();
	}
	
	public int getRandomNumber(int lowerLimit, int upperLimit) {
		if (upperLimit < lowerLimit) return 0;
		int dif = upperLimit - lowerLimit;
		return lowerLimit + (int)(Math.random() * 1000000 % dif);
	}
	
	
       
    public static Object getRandomFromArray(Object[] array) {
    	if (array == null) return null;
    	return array[(int) (Math.random() * 100000 % array.length)];
    }
	
}
