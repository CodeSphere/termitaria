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

import ro.cs.logaudit.entity.AuditCmBean;
import ro.cs.logaudit.entity.SearchAuditCmBean;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;

/**
 * 
 * @author Coni
 *
 */
public interface IDaoAuditCm {

	/**
	 * Adds a new CM audit event
	 * 
	 * @author coni
	 * @param auditTsBean
	 */
	public void add(AuditCmBean auditCmBean);
	
	/**
	 * Searches for CM audit events using the criterion from searchAuditCmBean
	 * 
	 * @author coni
	 * @return A list of audit beans 
	 */
	public List<AuditCmBean> getAuditBeanFromSearch(SearchAuditCmBean searchAuditCmBean, boolean isDeleteAction, Locale locale) throws ParseException;	

    /**
     * @author coni
     * @param getDataCriteria
     * Gets data for the Audit Events report data source request
     */
	public List<AuditCmBean> getAuditBeanForAuditEventsReportDataSource(AuditEventsReportGetDataCriteria getDataCriteria);
	
	/**
	 * Delete cm audit event identifed by it's id with all the components
	 * 
	 * Return the Audit cm bean that has been deleted
	 * @author coni
	 */
	public AuditCmBean delete(Integer auditId);

}
