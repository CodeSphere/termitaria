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
package ro.cs.logaudit.exception;

/**
 * @author alu
 *
 */
public interface ICodeException {
	
	String MODULE_NAME										= "AUDIT";
	
	String SECURITY_AUTHENTICATE							= MODULE_NAME.concat(":SECURITY:0");
	
	//Endpoint
	String ENDPOINT_ADD_AUDIT_EVENT							= MODULE_NAME.concat(":ENDPOINT:0");
	String ENDPOINT_GET_AUDIT_EVENTS_REPORT_DATA			= MODULE_NAME.concat(":ENDPOINT:1");
	
	//Reports WS Client 
	String REPORTS_WSCLIENT_CREATE_AXIS_CLIENT				= MODULE_NAME.concat("REPORTS_WSCLIENT:0");
	String REPORTS_WSCLIENT_SIGN_REQUEST_MESSAGE			= MODULE_NAME.concat("REPORTS_WSCLIENT:1");
	String REPORTS_WSCLIENT_DATE_TO_CALENDAR				= MODULE_NAME.concat("REPORTS_WSCLIENT:2");
	String REPORTS_WSCLIENT_CREATE_MAP_ENTRIES				= MODULE_NAME.concat("REPORTS_WSCLIENT:3");
	String REPORTS_WSCLIENT_AUDIT_EVENTS_REPORT_REQEUST		= MODULE_NAME.concat("REPORTS_WSCLIENT:4");
	
	//BLAuditOm exception codes
	String AUDITOM_ADD										= MODULE_NAME.concat(":AUDITOM:0");
	String AUDITOM_SEARCH									= MODULE_NAME.concat(":AUDITOM:1");
	String AUDITOM_DELETE									= MODULE_NAME.concat(":AUDITOM:2");
	String AUDITOM_REPORT_CREATE							= MODULE_NAME.concat(":AUDITOM:3");
	
	//BLAuditDm exception codes
	String AUDITDM_ADD										= MODULE_NAME.concat(":AUDITDM:0");
	String AUDITDM_SEARCH									= MODULE_NAME.concat(":AUDITDM:1");
	String AUDITDM_DELETE									= MODULE_NAME.concat(":AUDITDM:2");
	String AUDITDM_REPORT_CREATE							= MODULE_NAME.concat(":AUDITOM:3");
	
	//BLAuditTs exception codes
	String AUDITTS_ADD										= MODULE_NAME.concat(":AUDITTS:0");
	String AUDITTS_SEARCH									= MODULE_NAME.concat(":AUDITTS:1");
	String AUDITTS_DELETE									= MODULE_NAME.concat(":AUDITTS:2");
	String AUDITTS_REPORT_CREATE							= MODULE_NAME.concat(":AUDITTS:3");
	
	//BLAuditCm exception codes
	String AUDITCM_ADD										= MODULE_NAME.concat(":AUDITCM:0");
	String AUDITCM_SEARCH									= MODULE_NAME.concat(":AUDITCM:1");
	String AUDITCM_DELETE									= MODULE_NAME.concat(":AUDITCM:2");
	String AUDITCM_REPORT_CREATE							= MODULE_NAME.concat(":AUDITCM:3");
	
	//BLOrganisation exception codes	
	String ORGANISATION_GET_ALL								= MODULE_NAME.concat(":ORGANISATION:0");
	
	//BLReportsDataSource exception codes
	String REPORTS_DATASOURCE_AUDIT_EVENTS_REPORT			= MODULE_NAME.concat(":REPORTS_DATASOURCE:0");
	
	//BLUser
	String USER_GET_BY_SECURITY_TOKEN						= MODULE_NAME.concat(":USER:0");
	String USER_GET_BY_ORGANIZATION_ID						= MODULE_NAME.concat(":USER:1");
}
