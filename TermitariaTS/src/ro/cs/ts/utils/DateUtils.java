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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.exception.WSClientException;

public class DateUtils {
	
	private static DateUtils theInstance = null;
	static{
		theInstance = new DateUtils();
	}
	private DateUtils() {}
	
	public static DateUtils getInstance() {
		return theInstance;
	}
	
	public XMLGregorianCalendar dateToXmlGregorianCalendar(Date date) throws WSClientException {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		XMLGregorianCalendar xgcalendar = null;
		try {
			xgcalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			throw new WSClientException(ICodeException.REPORTS_WSCLIENT_DATE_TO_CALENDAR, e);
		}
		return xgcalendar;
	}
	
	
	/**
	 * Returns the date interval for a week
	 * 
	 * @author Andreea
	 */
	public DateInterval getDateIntervalForWeek(int week) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -(7 * week));


	    // calculate the start date of the week
	    Calendar first = (Calendar) cal.clone();
	    first.add(Calendar.DAY_OF_WEEK, 
	              first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

	    // and add six days to the end date
	    Calendar last = (Calendar) first.clone();
	    last.add(Calendar.DAY_OF_YEAR, 6);
	    
	    DateInterval dateInterval = new DateInterval(first.getTime(), last.getTime());
	    return dateInterval;
	}
	
	
	/**
	 * Returns the date interval for a month
	 * 
	 * @author Andreea
	 */
	public DateInterval getDateIntervalForMonth(int month) {
		
		Calendar cal = Calendar.getInstance();
		int monthOfYear = cal.get(Calendar.MONTH);

		int year = cal.get(Calendar.YEAR);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		
		monthOfYear = monthOfYear - month;
		if (monthOfYear == 0) {
			monthOfYear = 11;
			year --;
		}
		cal.set(year, monthOfYear, dayOfMonth);

		int numOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
	    // calculate the start date of the month
	    Calendar first = (Calendar) cal.clone();
	    first.set(year, monthOfYear, 1);
	    first.set(Calendar.HOUR_OF_DAY, 0);
	    first.set(Calendar.MINUTE, 0);
	    
	    // calculate the end date of the month
	    Calendar last = (Calendar) cal.clone();
	    last.set(year, monthOfYear, numOfDaysInMonth);
	    last.set(Calendar.HOUR_OF_DAY, 23);
	    last.set(Calendar.MINUTE, 59);
	    
	    DateInterval dateInterval = new DateInterval(first.getTime(), last.getTime());
	    
	    return dateInterval;
	}
	
	

}
