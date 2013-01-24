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
package ro.cs.logaudit.dao;

import ro.cs.logaudit.context.AuditContext;


/**
 * @author matti_joona
 * @author Adelina
 * @author coni
 *
 * Factory for getting a spring bean from context
 */
public class DaoBeanFactory {
	
	//singleton implementation
	private static DaoBeanFactory theInstance = null;
	private DaoBeanFactory(){};
	
	static {		
		theInstance = new DaoBeanFactory();
	}
	public static DaoBeanFactory getInstance() {
		return theInstance;
	}
	
	private static final String auditOmDaoBean = "auditOmDao";
	private static final String auditDmDaoBean = "auditDmDao";
	private static final String auditTsDaoBean = "auditTsDao";
	private static final String auditCmDaoBean = "auditCmDao";
	
	public IDaoAuditOm getDaoAuditOm(){
		return (IDaoAuditOm)(AuditContext.getApplicationContext().getBean(auditOmDaoBean));
	}
	
	public IDaoAuditDm getDaoAuditDm(){
		return (IDaoAuditDm)(AuditContext.getApplicationContext().getBean(auditDmDaoBean));
	}
	
	public IDaoAuditTs getDaoAuditTs(){
		return (IDaoAuditTs)(AuditContext.getApplicationContext().getBean(auditTsDaoBean));
	}
	
	public IDaoAuditCm getDaoAuditCm(){
		return (IDaoAuditCm)(AuditContext.getApplicationContext().getBean(auditCmDaoBean));
	}

}
