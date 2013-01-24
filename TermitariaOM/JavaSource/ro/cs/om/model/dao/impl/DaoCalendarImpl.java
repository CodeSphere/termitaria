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
package ro.cs.om.model.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Calendar;
import ro.cs.om.model.dao.IDaoCalendar;

/**
 * @author matti_joona
 * 
 */
public class DaoCalendarImpl extends HibernateDaoSupport implements IDaoCalendar {
	
	

	/**
	 * Add a Calendar
	 * 
	 * @author Adelina
	 * @param calendar
	 */
	public void add(Calendar calendar) {
		logger.debug("add - START");
		getHibernateTemplate().save(IModelConstant.calendarEntity, calendar);
		logger.debug("add - END - id: ".concat(String.valueOf(calendar.getCalendarId())));
	}

	/**
	 * Update a calendar
	 * 
	 * @author matti_joona
	 * @param calendar
	 */
	public void update(Calendar calendar) {
		logger.debug("Updating calendar");
		getHibernateTemplate().update(IModelConstant.calendarEntity, calendar);
		logger.debug("Calendar updated, id:".concat(String.valueOf(calendar
				.getCalendarId())));
	}

	/**
	 * Delete a calendar indetified by it's id
	 * 
	 * @author Adelina
	 * @param calendar
	 */
	public void delete(int calendarId) {
		logger.debug("START - Deleting calendar id:".concat(String
				.valueOf(calendarId)));
		Calendar calendar = get(calendarId);
		getHibernateTemplate().delete(IModelConstant.calendarEntity, calendar);
		logger.debug("END - Calendar deleted");
	}

	/**
	 * Get the information about a calendar
	 * 
	 * @author Adelina
	 * @param calendarId
	 * @return the Calendar
	 */
	public Calendar get(Integer calendarId) {
		logger.debug("Getting CALENDAR with id=".concat(calendarId.toString()));
		Calendar calendar = (Calendar) getHibernateTemplate().get(
				IModelConstant.calendarEntity, new Integer(calendarId));
		return calendar;
	}

	/**
	 * Get the calendar for an organisation, by the organisation's id
	 * 
	 * @author Adelina
	 * @param organisationId
	 * @return the Calendar for the organisation
	 */
	@SuppressWarnings("unchecked")
	public Calendar getCalendarByOrganisation(Integer organisationId) {
		logger.debug("getCalendarByOrganisation - START");		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.calendarEntity);		
		dc.add(Restrictions.eq("organisationId", organisationId));	
		List<Calendar> calendar = getHibernateTemplate().findByCriteria(dc);		
		logger.debug("getCalendarByOrganisation - END ");		
		return calendar.get(0);
	
	}	
}
