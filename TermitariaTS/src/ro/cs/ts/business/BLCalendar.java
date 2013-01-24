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
package ro.cs.ts.business;

import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.om.Calendar;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.WSCalendar;

/**
 * Singleton which expose business methods for Calendar item
 * 
 * @author Adelina
 *
 */
public class BLCalendar extends BusinessLogic{
	// singleton implementation
	private static BLCalendar theInstance = null;
	
	private BLCalendar() {
		
	}
	
	static {
		theInstance = new BLCalendar();
	}
	
	public static BLCalendar getInstance() {
		return theInstance;
	}
	
	public Calendar getCalendarByOrganization(Integer organizationId) throws BusinessException {
		logger.debug("getCalendarByOrganization  - START");
		Calendar calendar = new Calendar();
		try {
			WSCalendar wsCalendar = new WSCalendar();
			wsCalendar = OMWebServiceClient.getInstance().getCalendarByOrganization(organizationId);
			logger.debug("wsCalendar = " + wsCalendar);
			calendar.setCalendarId(wsCalendar.getCalendarId());
			calendar.setOrganisationId(wsCalendar.getOrganisationId());
			calendar.setStartWork(wsCalendar.getStartWork());
			calendar.setEndWork(wsCalendar.getEndWork());
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CALENDAR_GET_BY_ORGANIZATION, e);
		}
		logger.debug("getCalendarByOrganization - END");
		return calendar;
	}
}

	
