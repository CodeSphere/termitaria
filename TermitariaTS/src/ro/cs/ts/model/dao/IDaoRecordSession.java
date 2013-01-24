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

import ro.cs.ts.entity.RecordSession;
import ro.cs.ts.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoRecordSessionImpl
 * @author Andreea
 *
 */
public interface IDaoRecordSession {

	/**
	 * Returns a recordSession containing the basic info
	 * @author Andreea
	 * @param recordSessionId
	 * @return
	 */
	public RecordSession get(int recordSessionId);
	
	/**
	 * Gets a recordSession identified by sessionId
	 * 
	 * @author Andreea
	 * @param sessionId
	 * @return
	 * @throws BusinessException
	 */
	public RecordSession getBySessionId(String sessionId);
	
	
	/**
	 * Gets a recordSession identified by recordId
	 * 
	 * @author Andreea
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public RecordSession getByRecordId(int recordId);
	
	
	/**
	 * Adds a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 */
	public void add(RecordSession recordSession);
	
	/**
	 * Updates a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 */
	public void update(RecordSession recordSession);
	
	
	/**
	 * Deletes a recordSession
	 * 
	 * @author Andreea
	 * 
	 * @param recordSession
	 * @return
	 */
	public RecordSession delete(RecordSession recordSession);
	
	
	
	
	

}
