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
package ro.cs.om.business;

import ro.cs.om.entity.Calendar;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoCalendar;

/**
 * Singleton which expose business methods for Calendar item
 * 
 * @author Adelina
 */
public class BLCalendar extends BusinessLogic {

	// the dao used
	private IDaoCalendar calendarDao = DaoBeanFactory.getInstance()
			.getDaoCalendar();
	// singleton implementation
	private static BLCalendar theInstance = null;

	private BLCalendar() {
	};

	static {
		theInstance = new BLCalendar();
	}

	public static BLCalendar getInstance() {
		return theInstance;
	}

	/**
	 * Add calendar.
	 * 
	 * @author Adelina
	 * @param calendar
	 * @throws BusinessException
	 */

	public void add(Calendar calendar) throws BusinessException {
		logger.debug("add - START");
		try {
			calendarDao.add(calendar);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CALENDAR_ADD, e);
		}
		logger.debug("add - END");
	}

	/**
	 * Update a calendar.
	 * 
	 * @author Adelina
	 * @param calendar
	 * @throws BusinessException
	 */

	public void update(Calendar calendar) throws BusinessException {
		logger.debug("update - START");
		try {
			calendarDao.update(calendar);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CALENDAR_UPDATE, e);
		}
		logger.debug("update - END");
	}

	/**
	 * Delete a calendar.
	 * 
	 * @author Adelina
	 * @param calendar
	 * @throws BusinessException
	 */

	public void delete(int calendarId) throws BusinessException {
		logger.debug("delete START");
		try {
			calendarDao.delete(calendarId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CALENDAR_DELETE, e);
		}
		logger.debug("delete END");
	}

	/**
	 * Get the calendar
	 * 
	 * @author Adelina
	 * @param calendarId
	 * @return the Calendar
	 * @throws BusinessException
	 */

	public Calendar getCalendar(Integer calendarId) throws BusinessException {
		Calendar calendar = null;
		try {
			calendar = calendarDao.get(calendarId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CALENDAR_GET, e);
		}
		return calendar;
	}

	/**
	 * Get the calendar for an organisation, by the organisation's id
	 * 
	 * @author Adelina
	 * @param organisationId
	 * @return the Calendar for the organisation
	 * @throws BusinessException
	 */

	public Calendar getCalendarForOrganisation(Integer organisationId)
			throws BusinessException {
		Calendar calendar = null;
		try {
			calendar = calendarDao.getCalendarByOrganisation(organisationId);
		} catch (Exception e) {
			throw new BusinessException(
					ICodeException.CALENDAR_GET_BY_ORGANISATION, e);
		}
		return calendar;
	}
}
