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

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import ro.cs.logaudit.entity.AuditDmBean;
import ro.cs.logaudit.entity.SearchAuditDmBean;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;

public interface IDaoAuditDm {
	
	
	/**
	 * Searches for DM audit after criterion from searchAuditOmBean
	 * 
	 * @author coni
	 * @return A list of audit beans 
	 */
	public List<AuditDmBean> getAuditBeanFromSearch(SearchAuditDmBean searchAuditDmBean, boolean isDeleteAction, Locale locale) throws ParseException;	
	
	/**
	 * Adds a new DM audit event
	 * 
	 * @author coni
	 * @param auditDmBean
	 */
	public void add(AuditDmBean auditDmBean);
	
    /**
     * @author coni
     * @param getDataCriteria
     * Gets data for the Audit Events report data source request
     */
	public List<AuditDmBean> getAuditBeanForAuditEventsReportDataSource(AuditEventsReportGetDataCriteria getDataCriteria);
	
	/**
	 * Delete dm audit event identifed by it's id with all the components
	 * 
	 * Return the Audit dm bean that has been deleted
	 * @author coni
	 */
	public AuditDmBean delete(Integer auditId);

}
