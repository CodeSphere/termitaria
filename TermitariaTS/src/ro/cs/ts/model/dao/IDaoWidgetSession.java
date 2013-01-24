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
package ro.cs.ts.model.dao;

import java.util.List;
import ro.cs.ts.entity.WidgetSession;
import ro.cs.ts.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoWidgetSessionImpl
 * @author Andreea
 *
 */
public interface IDaoWidgetSession {

	/**
	 * Returns a widgetSession containing the basic info
	 * @author Andreea
	 * @param widgetSessionId
	 * @return
	 */
	public WidgetSession get(int widgetSessionId);
	
	/**
	 * Gets a widgetSessionId identified by sessionId
	 * 
	 * @author Andreea
	 * @param sessionId
	 * @return
	 * @throws BusinessException
	 */
	public WidgetSession getBySessionId(String sessionId);
	
	/**
	 * Gets all the widgetSessions identified by userId
	 * 
	 * @author Andreea
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public List<WidgetSession> getByUserId(int userId);
	
	
	/**
	 * Adds a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 */
	public void add(WidgetSession widgetSession);
	
	/**
	 * Updates a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 */
	public void update(WidgetSession widgetSession);
	

	/**
	 * Deletes a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 * @return
	 */
	public WidgetSession delete(WidgetSession widgetSession);
	

}
