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
package ro.cs.ts.model.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.WidgetSession;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.IDaoWidgetSession;

/**
 * Dao class for WidgetSession Entity
 * 
 * @author Andreea
 *
 */
public class DaoWidgetSessionImpl extends HibernateDaoSupport implements IDaoWidgetSession {

	/**
	 * Returns a widgetSession containing the basic info
	 * @author Andreea
	 * @param widgetSessionId
	 * @return
	 */
	public WidgetSession get(int widgetSessionId) {
		logger.debug("get - START - widgetSession with id =".concat(String.valueOf(widgetSessionId)));
		WidgetSession widgetSession = (WidgetSession) getHibernateTemplate().get(IModelConstant.widgetSessionSimpleEntity, widgetSessionId);
		logger.debug("get - END");
		return widgetSession;
	}
	
	/**
	 * Gets all the widgetSessions identified by userId
	 * 
	 * @author Andreea
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	public List<WidgetSession> getByUserId(int userId) {
		logger.debug("getByUserId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.widgetSessionSimpleEntity);
		dc.add(Restrictions.eq("userId", userId));
		//dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		//dc.add(Restrictions.isNull("projectDetailId"));
		List<WidgetSession> result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByUserId - END");
		return result;
	}
	
	/**
	 * Returns a widgetSession identified by sessionId
	 * 
	 * @author Andreea
	 * 
	 * @param sessionId
	 * @return
	 */
	public WidgetSession getBySessionId(String sessionId) {
		logger.debug("getBySessionId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.widgetSessionSimpleEntity);
		dc.add(Restrictions.eq("sessionId", sessionId));
		//dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		//dc.add(Restrictions.isNull("projectDetailId"));
		List<WidgetSession> result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getBySessionId - END");
		return result.get(0);
	}
	
	/**
	 * Adds a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 */
	public void add(WidgetSession widgetSession) {
		logger.debug("add - START");
		
		getHibernateTemplate().save(IModelConstant.widgetSessionEntity, widgetSession);
		
		logger.debug("add - END");
	}
	
	/**
	 * Updates a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 */
	public void update(WidgetSession widgetSession) {
		logger.debug("update - START");
		
		getHibernateTemplate().update(IModelConstant.widgetSessionForUpdate, widgetSession);
		
		logger.debug("update - END");
	}	
	

	
	/**
	 * Deletes a widgetSession
	 * 
	 * @author Andreea
	 * 
	 * @param widgetSession
	 * @return
	 */
	public WidgetSession delete(WidgetSession widgetSession) {
		logger.debug("delete - START");
		logger.debug("Deleting widgetSession with id: ".concat(String.valueOf(widgetSession.getWidgetSessionId())));
			
		
		logger.debug("Deleting the widgetSession : " + widgetSession);
		getHibernateTemplate().update(IModelConstant.widgetSessionSimpleEntity, widgetSession);
		logger.debug("WidgetSession " + widgetSession + " has been deleted");
		logger.debug("delete  - END");
		return widgetSession;
	}		
		
	
	
}
