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
import ro.cs.ts.entity.RecordSession;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.IDaoRecordSession;

/**
 * Dao class for RecordSession Entity
 * 
 * @author Andreea
 */
public class DaoRecordSessionImpl extends HibernateDaoSupport implements IDaoRecordSession {

	
	
	
	/**
	 * Returns a recordSession containing the basic info
	 * @author Andreea
	 * @param recordSessionId
	 * @return
	 */
	public RecordSession get(int recordSessionId) {
		logger.debug("get - START - recordSession with id =".concat(String.valueOf(recordSessionId)));
		RecordSession recordSession = (RecordSession) getHibernateTemplate().get(IModelConstant.recordSessionSimpleEntity, recordSessionId);
		logger.debug("get - END");
		return recordSession;
	}
	
	/**
	 * Gets a recordSession identified by recordId
	 * 
	 * @author Andreea
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public RecordSession getByRecordId(int recordId) {
		logger.debug("get - START - recordSession with recordId =".concat(String.valueOf(recordId)));
		RecordSession recordSession = (RecordSession) getHibernateTemplate().get(IModelConstant.recordSessionSimpleEntity, recordId);
		logger.debug("get - END");
		return recordSession;
	}
	
	/**
	 * Returns a recordSession identified by sessionId
	 * 
	 * @author Andreea
	 * 
	 * @param sessionId
	 * @return
	 */
	public RecordSession getBySessionId(String sessionId) {
		logger.debug("getBySessionId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.recordSessionSimpleEntity);
		dc.add(Restrictions.eq("sessionId", sessionId));
		//dc.add(Restrictions.ne("status", IConstant.NOM_ACTIVITY_STATUS_DELETED));
		//dc.add(Restrictions.isNull("projectDetailId"));
		List<RecordSession> result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getBySessionId - END");
		return result.get(0);
	}
	
	/**
	 * Adds a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 */
	public void add(RecordSession recordSession) {
		logger.debug("add - START");
		
		getHibernateTemplate().save(IModelConstant.recordSessionEntity, recordSession);
		
		logger.debug("add - END");
	}
	
	/**
	 * Updates a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 */
	public void update(RecordSession recordSession) {
		logger.debug("update - START");
		
		getHibernateTemplate().update(IModelConstant.recordSessionForUpdate, recordSession);
		
		logger.debug("update - END");
	}	
	

	
	/**
	 * Deletes a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 * @return
	 */
	public RecordSession delete(RecordSession recordSession) {
		logger.debug("delete - START");
		logger.debug("Deleting recordSession with id: ".concat(String.valueOf(recordSession.getRecordSessionId())));
			
		
		logger.debug("Deleting the recordSession : " + recordSession);
		getHibernateTemplate().update(IModelConstant.recordSessionSimpleEntity, recordSession);
		logger.debug("RecordSession " + recordSession + " has been deleted");
		logger.debug("delete  - END");
		return recordSession;
	}		
		
	
	
}
