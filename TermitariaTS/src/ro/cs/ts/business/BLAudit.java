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

import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.thread.AuditThread;

public class BLAudit extends BusinessLogic{

	//singleton implementation
    private static BLAudit theInstance = null;
    private BLAudit(){};
    static {
        theInstance = new BLAudit();
    }
    public static BLAudit getInstance() {
        return theInstance;
    }
    
    /**
     * @author coni
     * Adds a new audit event by sending a new request to the AUDIT module
     * @throws BusinessException 
     */
    public void add(String event, String firstName, String lastName, String messageEN, String messageRO, int organisationId, int personId) throws BusinessException{
    	logger.debug("add - START");
    	try {
			new AuditThread(event, firstName, lastName, messageEN, messageRO, organisationId, personId).run();
    	} catch (Exception exc){
    		throw new BusinessException(ICodeException.AUDIT_ADD, exc);
    	}
    	logger.debug("add - END");
    }
}
