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

import java.util.List;

import ro.cs.ts.entity.WidgetSession;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoWidgetSession;

/**
 * 
 * BLWidgetSession
 * 
 * @author Andreea
 *
 */
public class BLWidgetSession extends BusinessLogic{

	//singleton implementation
	private static BLWidgetSession theInstance = null;
	
	private BLWidgetSession(){};
	static {
		theInstance = new BLWidgetSession();
	}
	public static BLWidgetSession getInstance(){
		return theInstance;
	}
	
	private static IDaoWidgetSession widgetSessionDao = DaoBeanFactory.getInstance().getDaoWidgetSession();
	
	/**
	 * Returns a widgetSession containing the basic info
	 * @author Andreea
	 * @param widgetSessionId
	 * @return
	 */
	public WidgetSession get(int widgetSessionId) throws BusinessException {
		logger.debug("get - START");
		WidgetSession widgetSession =  null;
		try {
			widgetSession  = widgetSessionDao.get(widgetSessionId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_GET, e);
		}
		logger.debug("get - END");
		return widgetSession;
	}
	
	/**
	 * Gets all widgetSessions identified by a userId
	 * 
	 * @author Andreea
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public List<WidgetSession> getByUserId(int userId) throws BusinessException {
		logger.debug("getByUserId - START");
		List<WidgetSession> widgetSessions = null;
		try {
			widgetSessions = widgetSessionDao.getByUserId(userId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_GET_BY_USER_ID, e);
		}
		logger.debug("getByOrganization - END");
		return widgetSessions;
	}
	
	/**
	 * Gets the widgetSession identified by sessionId
	 * 
	 * @author Andreea
	 * @param sessionId
	 * @return
	 * @throws BusinessException
	 */
	public WidgetSession getBySessionId(String sessionId) throws BusinessException {
		logger.debug("getBySessionId - START");
		WidgetSession widgetSession = null;
		System.out.println(sessionId);
		try {
			widgetSession = widgetSessionDao.getBySessionId(sessionId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_GET_BY_SESSION_ID, e);
		}
		logger.debug("getBySessionId - END");
		return widgetSession;
	}
	
	
	
	/**
	 * Adds a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 * @throws BusinessException 
	 */
	public void add(WidgetSession widgetSession) throws BusinessException {
		logger.debug("add - START");
		try{
			widgetSessionDao.add(widgetSession);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_ADD, e);
		}
				
		logger.debug("add - END");
	}
	
	/**
	 * Updates a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 * @throws BusinessException 
	 */
	public void update(WidgetSession widgetSession) throws BusinessException {
		logger.debug("update - START");
		
		try{
			widgetSessionDao.update(widgetSession);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_UPDATE, e);
		}
		
		logger.debug("update - END");
	}	
	
	
	
	/**
	 * Deletes a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 * @return
	 * @throws BusinessException 
	 */
	public WidgetSession delete(WidgetSession widgetSession) throws BusinessException {
		logger.debug("delete - START");
		logger.debug("Deleting widgetSession with id: ".concat(String.valueOf(widgetSession.getWidgetSessionId())));
			
		try{
			widgetSession = widgetSessionDao.delete(widgetSession);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_DELETE, e);
		}
		
		logger.debug("Deleting the activity : " + widgetSession);		
		logger.debug("WidgetSession " + widgetSession + " has been deleted");
		logger.debug("delete  - END");
		return widgetSession;
	}	
	
}
