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
package ro.cs.om.model.dao;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.Calendar;

/**
 * @author matti_joona
 * 
 */
public interface IDaoCalendar {

	/**
	 * Adds a Calendar
	 * 
	 * @author Adelina
	 * @param calendar
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(Calendar calendar);

	/**
	 * Update an existing calendar
	 * 
	 * @author matti_joona
	 * @param calendar
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(Calendar calendar);

	/**
	 * Delete an existing calendar
	 * 
	 * @author Adelina
	 * @param calendarId
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(int calendarId);

	/**
	 * Get a calendar
	 * 
	 * @author Adelina
	 * @parm calendarId
	 * @return the Calendar by the id
	 */
	public Calendar get(Integer calendarId);

	/**
	 * Get the calendar for an organisation
	 * 
	 * @author Adelina
	 * @param organisationId
	 * @return the Calendar for the organisation
	 */

	public Calendar getCalendarByOrganisation(Integer organisationId);

}
