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
package ro.cs.logaudit.ws.client.reports;

/**
 * 
 * @author coni
 * Used for Reports Web Service client constants 
 *
 */
public interface IReportsWsClientConstant {
	
	//Reports Web Service Namespace
	public static String REPORTS_WEB_SERVICE_NAMESPACE								= "http://localhost:8080/Reports/services/schemas/messages";
	
	//Axis Fault Detail local parts
	public static String AXIS_FAULT_DETAIL_CODE_LOCALPART							= "code";
	public static String AXIS_FAULT_DETAIL_MESSAGE_LOCALPART						= "message";
	
	//Audit Events Report Request local parts
	public static String AUDIT_EVENTS_REPORT_ROOT_PAYLOAD_LOCALPART					= "AuditEventsReportRequest";
	public static String AUDIT_EVENTS_REPORT_PARAMS_LOCALPART						= "auditEventsReportParams";
	public static String AUDIT_EVENTS_REPORT_PROPERTIES_LOCALPART					= "properties";
	
	//Audit Events Report Response local parts
	public static String AUDIT_EVENTS_REPORT_RESPONSE_REPORT_FILE_NAME_LOCALPART	= "reportFileName";
	public static String AUDIT_EVENTS_REPORT_RESPONSE_ATTACHMENT_ID_LOCALPART		= "reportFileAttachmentId";
	
	//AXIS HashMap elements local parts
	public static String MAP_ENTRY_LOCALPART										= "entry";
	public static String MAP_KEY_LOCALPART											= "key";
	public static String MAP_VALUE_LOCALPART										= "value";
	
	//Audit Events Report Parameters names
	public static String AUDIT_EVENTS_REPORT_START_DATE_PARAM 						= "startDate";
	public static String AUDIT_EVENTS_REPORT_END_DATE_PARAM							= "endDate";
	public static String AUDIT_EVENTS_REPORT_PERSON_ID_PARAM						= "personId";
	public static String AUDIT_EVENTS_REPORT_MESSAGE_PARAM							= "message";
	public static String AUDIT_EVENTS_REPORT_EVENT_PARAM							= "event";
	public static String AUDIT_EVENTS_REPORT_MODULE_ID_PARAM						= "moduleId";
	public static String AUDIT_EVENTS_REPORT_PARAM_REPORT_TITLE						= "reportTitle";
	public static String AUDIT_EVENTS_REPORT_ORIENTATION_PARAM						= "orientation";
	public static String AUDIT_EVENTS_REPORT_LOCALE_PARAM							= "locale";
	public static String AUDIT_EVENTS_REPORT_FORMAT_PARAM							= "format";
	public static String AUDIT_EVENTS_REPORT_ORGANISATION_ID_PARAM					= "organisationId";
	public static String AUDIT_EVENTS_REPORT_HTML_IS_EMBEDDABLE						= "isEmbeddable";
	
}
