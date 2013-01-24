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
/*Created on 05.03.2008*/

package ro.cs.om.model.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.FreeDay;

public interface IDaoFreeDay {
	
	

	/**
	 * Add a FreeDay
	 * 
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(FreeDay freeday);
	
	/**
	 * Update an existing FreeDay
	 * 
	 * @author Adelina
	 * @param FreeDay
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(FreeDay freeday);
	
	/**
	 * Get a freeday
	 * 
	 * @author Adelina
	 */
	public FreeDay get(Integer freedayId);
	
	/**
	 * Deletes a freeday identified by its id
     *
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(int freedayId);
	
	
	/**
	 * Gets the list of free days for a calendar with a calendarId
	 * 
	 * @author Adelina
	 * @parm calendarId
	 * @return List<FreeDay>
	 */
	public List<FreeDay> getFreeDaysByCalendar(Integer calendarId);
	
}
