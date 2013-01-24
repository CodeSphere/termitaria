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
package ro.cs.logaudit.utils.date;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ro.cs.logaudit.exception.ICodeException;
import ro.cs.logaudit.exception.WSClientException;


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

}
