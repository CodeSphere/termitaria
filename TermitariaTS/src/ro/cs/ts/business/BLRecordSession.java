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

import ro.cs.ts.entity.RecordSession;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoRecordSession;

/**
 * 
 * BLRecordSession
 * 
 * @author Andreea
 *
 */
public class BLRecordSession extends BusinessLogic{

	//singleton implementation
	private static BLRecordSession theInstance = null;
	
	private BLRecordSession(){};
	static {
		theInstance = new BLRecordSession();
	}
	public static BLRecordSession getInstance(){
		return theInstance;
	}
	
	private static IDaoRecordSession recordSessionDao = DaoBeanFactory.getInstance().getDaoRecordSession();
	
	/**
	 * Returns a recordSession containing the basic info
	 * @author Andreea
	 * @param recordSessionId
	 * @return
	 */
	public RecordSession get(int recordSessionId) throws BusinessException {
		logger.debug("get - START");
		RecordSession recordSession =  null;
		try {
			recordSession  = recordSessionDao.get(recordSessionId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_SESSION_GET, e);
		}
		logger.debug("get - END");
		return recordSession;
	}
	
	/**
	 * Gets the recordSession identified by recordId
	 * 
	 * @author Andreea
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public RecordSession getByRecordId(int recordId) throws BusinessException {
		logger.debug("getByUserId - START");
		RecordSession recordSession = null;
		try {
			recordSession = recordSessionDao.getByRecordId(recordId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_SESSION_GET_BY_RECORD_ID, e);
		}
		logger.debug("getByUserId - END");
		return recordSession;
	}
	
	/**
	 * Gets the recordSession identified by sessionId
	 * 
	 * @author Andreea
	 * @param sessionId
	 * @return
	 * @throws BusinessException
	 */
	public RecordSession getBySessionId(String sessionId) throws BusinessException {
		logger.debug("getBySessionId - START");
		RecordSession recordSession = null;
		System.out.println(sessionId);
		try {
			recordSession = recordSessionDao.getBySessionId(sessionId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_GET_BY_SESSION_ID, e);
		}
		logger.debug("getBySessionId - END");
		return recordSession;
	}
	
	
	
	/**
	 * Adds a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 * @throws BusinessException 
	 */
	public void add(RecordSession recordSession) throws BusinessException {
		logger.debug("add - START");
		try{
			recordSessionDao.add(recordSession);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_ADD, e);
		}
				
		logger.debug("add - END");
	}
	
	/**
	 * Updates a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 * @throws BusinessException 
	 */
	public void update(RecordSession recordSession) throws BusinessException {
		logger.debug("update - START");
		
		try{
			recordSessionDao.update(recordSession);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_UPDATE, e);
		}
		
		logger.debug("update - END");
	}	
	
	
	
	/**
	 * Deletes a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 * @return
	 * @throws BusinessException 
	 */
	public RecordSession delete(RecordSession recordSession) throws BusinessException {
		logger.debug("delete - START");
		logger.debug("Deleting recordSession with id: ".concat(String.valueOf(recordSession.getRecordSessionId())));
			
		try{
			recordSession = recordSessionDao.delete(recordSession);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.WIDGET_SESSION_DELETE, e);
		}
		
		logger.debug("Deleting the activity : " + recordSession);		
		logger.debug("RecordSession " + recordSession + " has been deleted");
		logger.debug("delete  - END");
		return recordSession;
	}	
	
}
