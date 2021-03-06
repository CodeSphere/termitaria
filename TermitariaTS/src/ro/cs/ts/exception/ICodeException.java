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
package ro.cs.ts.exception;

/**
 * @author alu
 *
 */
public interface ICodeException {
	
	String MODULE_NAME										= "TS";
	
	String SECURITY_AUTHENTICATE							= MODULE_NAME.concat(":SECURITY:0");
	
	// BLProject
	String PROJECT_GET_ALL_PROJECTS							= MODULE_NAME.concat(":PROJECT:0");
	String PROJECT_GET_BY_MANAGER							= MODULE_NAME.concat(":PROJECT:1");
	String PROJECT_LIST										= MODULE_NAME.concat(":PROJECT:2");
	String PROJECT_GET_ALL_CLIENTS							= MODULE_NAME.concat(":PROJECT:3");
	String PROJECT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON	= MODULE_NAME.concat(":PROJECT:4");
	String PROJECT_GET										= MODULE_NAME.concat(":PROJECT:5");
	String PROJECT_GET_PROJECTS_BY_PERSON					= MODULE_NAME.concat(":PROJECT:6");
	String PROJECT_GET_SIMPLE								= MODULE_NAME.concat(":PROJECT:7");
	String PROJECT_GET_SIMPLE_BY_IDS						= MODULE_NAME.concat(":PROJECT:8");
	String PROJECT_GET_WITH_ALL								= MODULE_NAME.concat(":PROJECT:9");
	
	// BLActivity
	String ACTIVITY_GET										= MODULE_NAME.concat(":ACTIVITY:0");
	String ACTIVITY_GET_BY_ORGANIZATION						= MODULE_NAME.concat(":ACTIVITY:1");
	String ACTIVITY_GET_WITH_ALL							= MODULE_NAME.concat(":ACTIVITY:2");
	String ACTIVITY_ADD										= MODULE_NAME.concat(":ACTIVITY:3");
	String ACTIVITY_UPDATE									= MODULE_NAME.concat(":ACTIVITY:4");
	String ACTIVITY_GET_BY_PROJECT_ID						= MODULE_NAME.concat(":ACTIVITY:5");
	String ACTIVITY_SEARCH									= MODULE_NAME.concat(":ACTIVITY:6");
	String ACTIVITY_GET_WITH_PROJECT_DETAIL					= MODULE_NAME.concat(":ACTIVITY:7");
	String ACTIVITY_DELETE									= MODULE_NAME.concat(":ACTIVITY:8");
	String ACTIVITY_HAS_ASSOCIATED_RECORD					= MODULE_NAME.concat(":ACTIVITY:9");
	String ACTIVITY_GET_BY_CURRENCY_ID						= MODULE_NAME.concat(":ACTIVITY:10");
	String ACTIVITY_GET_BY_PROJECT_DETAIL_ID				= MODULE_NAME.concat(":ACTIVITY:11");
	
	// BLPerson
	String PERSON_LIST										= MODULE_NAME.concat(":PERSON:0");
	String PERSON_GET_PERSON_FROM_SEARCH					= MODULE_NAME.concat(":PERSON:1");
	String PERSON_GET_TEAM_MEMBER_FROM_SEARCH				= MODULE_NAME.concat(":PERSON:2");
	
	// BLCalendar
	String CALENDAR_GET_BY_ORGANIZATION						= MODULE_NAME.concat(":CALENDAR:0");
	
	//BLRecord
	String RECORD_SEARCH									= MODULE_NAME.concat(":RECORD:0");
	String RECORD_ADD										= MODULE_NAME.concat(":RECORD:1");
	String RECORD_UPDATE									= MODULE_NAME.concat(":RECORD:2");
	String RECORD_HANDLE_DEPENDENCIES						= MODULE_NAME.concat(":RECORD:3");
	String RECORD_GET_ALL									= MODULE_NAME.concat(":RECORD:4");
	String RECORD_DELETE									= MODULE_NAME.concat(":RECORD:5");
	String RECORD_GET_BY_PROJECT_DETAIL_ID					= MODULE_NAME.concat(":RECORD:6");
	String RECORD_CALCULATE_BILLING_PRICE_FOR_PROJECT		= MODULE_NAME.concat(":RECORD:7");
	String RECORD_ADD_RECORD_BILLING_PRICE_TO_PROJECT		= MODULE_NAME.concat(":RECORD:8");
	String RECORD_HAS_IDENTICAL_FOR_PERSON					= MODULE_NAME.concat(":RECORD:9");
	String RECORD_GET_SIMPLE								= MODULE_NAME.concat(":RECORD:10");
	String RECORD_GET										= MODULE_NAME.concat(":RECORD:11");
	
	//BLProjectDetails
	String PROJECT_DETAILS_GET_BY_PROJECT_ID					= MODULE_NAME.concat(":PROJECTDETAILS:0");
	String PROJECT_DETAILS_GET									= MODULE_NAME.concat(":PROJECTDETAILS:1");
	String PROJECT_DETAILS_FOR_PROJECT							= MODULE_NAME.concat(":PROJECTDETAILS:2");
	String PROJECT_DETAILS_ADD									= MODULE_NAME.concat(":PROJECTDETAILS:3");
	String PROJECT_DETAILS_UPDATE								= MODULE_NAME.concat(":PROJECTDETAILS:4");	
	String PROJECT_DETAILS_ADD_BY_PROJECT						= MODULE_NAME.concat(":PROJECTDETAILS:5");
	String PROJECT_DETAILS_DELETE								= MODULE_NAME.concat(":PROJECTDETAILS:6");
	String PROJECT_DETAILS_FINISH								= MODULE_NAME.concat(":PROJECTDETAILS:7");
	String PROJECT_DETAILS_ABORT								= MODULE_NAME.concat(":PROJECTDETAILS:8");
	String PROJECT_DETAILS_OPEN									= MODULE_NAME.concat(":PROJECTDETAILS:9");
	String PROJECT_DETAILS_CALCULATE_COST						= MODULE_NAME.concat(":PROJECTDETAILS:10");
	String PROJECT_DETAILS_GET_ALL_OPENED						= MODULE_NAME.concat(":PROJECTDETAILS:11");
	String PROJECT_DETAILS_MARK_SENDING_NOTIFICATION_PERCENTAGE	= MODULE_NAME.concat(":PROJECTDETAILS:12");
	String PROJECT_DETAILS_MARK_SENDING_BUDGET_OVERFLOW			= MODULE_NAME.concat(":PROJECTDETAILS:13");	
	
	//BLTeamMemberDetail
	String TEAM_MEMBER_DETAIL_GET_BY_TEAM_MEMBER_ID			= MODULE_NAME.concat(":TEAMMEMBERDETAIL:0");
	String TEAM_MEMBER_DETAIL_GET							= MODULE_NAME.concat(":TEAMMEMBERDETAIL:1");
	String TEAM_MEMBER_DETAIL_FOR_MEMBER					= MODULE_NAME.concat(":TEAMMEMBERDETAIL:2");
	String TEAM_MEMBER_DETAIL_ADD							= MODULE_NAME.concat(":TEAMMEMBERDETAIL:3");
	String TEAM_MEMBER_DETAIL_UPDATE						= MODULE_NAME.concat(":TEAMMEMBERDETAIL:4");
	String TEAM_MEMBER_GET_BY_CURRENCY_ID					= MODULE_NAME.concat(":TEAMMEMBERDETAIL:5");
	String TEAM_MEMEBER_DETAIL_GET_WITH_ALL					= MODULE_NAME.concat(":TEAMMEMBERDETAIL:6");
	String TEAM_MEMBER_DETAIL_DELETE						= MODULE_NAME.concat(":TEAMMEMBERDETAIL:7");
	
	//BLTeamMember
	String TEAM_MEMBER_GET_FROM_SEARCH						= MODULE_NAME.concat(":TEAMMEMBER:0");
	String TEAM_MEMBER_GET_BY_IDS							= MODULE_NAME.concat(":TEAMMEMBER:1");
	String TEAM_MEMBER_GET									= MODULE_NAME.concat(":TEAMMEMBER:2");
	String TEAM_MEMBER_EXTERNAL_GET_BY_PROJECT_IDS			= MODULE_NAME.concat(":TEAMMEMBER:3");
	
	//BLUser
	String USER_GET_BY_SECURITY_TOKEN						= MODULE_NAME.concat(":USER:0");
	String USER_GET_BY_ORGANIZATION_ID						= MODULE_NAME.concat(":USER:1");
	String USER_GET_BY_PERSON_IDS							= MODULE_NAME.concat(":USER:2");
	String USER_GET_FROM_SEARCH								= MODULE_NAME.concat(":USER:3");
	
	//BLNotification
	String NOTIFICATION_SEARCH								= MODULE_NAME.concat(":NOTIFICATION:0");
	String NOTIFICATION_DELETE								= MODULE_NAME.concat(":NOTIFICATION:1");
	String NOTIFICATION_DELETE_ALL							= MODULE_NAME.concat(":NOTIFICATION:2");
	String NOTIFICATION_ADD									= MODULE_NAME.concat(":NOTIFICATION:3");
	String NOTIFICATION_SEND								= MODULE_NAME.concat(":NOTIFICATION:4");
	
	//BLNotificationSettings
	String NOTIFICATION_SETTINGS_HAS_SETTINGS				= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:0");
	String NOTIFICATION_SETTINGS_ADD_DEFAULT				= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:1");
	String NOTIFICATION_SETTINGS_UPDATE_SETTINGS			= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:2");
	String NOTIFICATION_SETTINGS_GET						= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:3");
	String NOTIFICATION_LANGUAGE_GET						= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:4");
	String NOTIFICATION_LANGUAGE_UPDATE						= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:5");
	String NOTIFICATION_LANGUAGE_ADD						= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:6");
	String NOTIFICATION_GET_STATUS_FOR_SETTING				= MODULE_NAME.concat(":NOTIFICATIONSETTINGS:7");
	
	//BLPersonDetail
	String PERSON_DETAIL_ADD								= MODULE_NAME.concat(":PERSONDETAIL:0");
	String PERSON_DETAIL_GET								= MODULE_NAME.concat(":PERSONDETAIL:1");
	String PERSON_DETAIL_GET_BY_CURRENCY_ID					= MODULE_NAME.concat(":PERSONDETAIL:2");
	String PERSON_DETAIL_UPDATE								= MODULE_NAME.concat(":PERSONDETAIL:3");
	String PERSON_DETAIL_HAS_DETAILS						= MODULE_NAME.concat(":PERSONDETAIL:4");
	String PERSON_DETAIL_GET_WITH_CURRENCIES				= MODULE_NAME.concat(":PERSONDETAIL:5");
	String PERSON_DETAIL_GET_WITH_ALL						= MODULE_NAME.concat(":PERSONDETAIL:6");
	
	
	//BLCurrency
	String CURRENCY_GET_BY_ORGANIZATION_ID					= MODULE_NAME.concat(":CURRENCY:0");
	String CURRENCY_GET_FROM_SEARCH							= MODULE_NAME.concat(":CURRENCY:1");
	String CURRENCY_DELETE									= MODULE_NAME.concat(":CURRENCY:2");
	String CURRENCY_ADD										= MODULE_NAME.concat(":CURRENCY:3");
	String CURRENCY_UPDATE									= MODULE_NAME.concat(":CURRENCY:4");
	String CURRENCY_GET_ALL									= MODULE_NAME.concat(":CURRENCY:5");
	String CURRENCY_IS_USED_IN_PROJETS						= MODULE_NAME.concat(":CURRENCY:6");
	String CURRENCY_GET_BY_NAME								= MODULE_NAME.concat(":CURRENCY:7");
	String CURRENCY_GET_BY_INITIALS							= MODULE_NAME.concat(":CURRENCY:8");
	String CURRENCY_GET_BY_NAME_OR_INITIALS					= MODULE_NAME.concat(":CURRENCY:9");
	
	//BLCostSheet
	String COST_SHEET_GET_FROM_SEARCH						= MODULE_NAME.concat(":COSTSHEET:0");
	String COST_SHEET_DELETE								= MODULE_NAME.concat(":COSTSHEET:1");
	String COST_SHEET_ADD									= MODULE_NAME.concat(":COSTSHEET:2");
	String COST_SHEET_UPDATE								= MODULE_NAME.concat(":COSTSHEET:3");
	String COST_SHEET_HANDLE_DEPENDENCIES					= MODULE_NAME.concat(":COSTSHEET:4");
	String COST_SHEET_GET_ALL								= MODULE_NAME.concat(":COSTSHEET:5");
	String COST_SHEET_GET_BY_CURRENCY_ID					= MODULE_NAME.concat(":COSTSHEET:6");
	String COST_SHEET_GET_FOR_VIEW							= MODULE_NAME.concat(":COSTSHEET:7");
	String COST_SHEET_GET_BY_PROJECT_DETAIL_ID				= MODULE_NAME.concat(":COSTSHEET:8");
	String COST_SHEET_CALCULATE_BILLING_PRICE_FOR_PROJECT	= MODULE_NAME.concat(":COSTSHEET:9");
	
	//BLExchange
	String EXCHANGE_GET_BY_CURRENCY_ID						= MODULE_NAME.concat(":EXCHANGE:0");
	String EXCHANGE_GET_FROM_SEARCH							= MODULE_NAME.concat(":EXCHANGE:1");
	String EXCHANGE_DELETE									= MODULE_NAME.concat(":EXCHANGE:2");
	String EXCHANGE_GET_PROJECT_EXCHANGE_BY_CURRENCIES		= MODULE_NAME.concat(":EXCHANGE:3");
	String EXCHANGE_GET_ALL									= MODULE_NAME.concat(":EXCHANGE:4");
	String EXCHANGE_HANDLE_DEPENDENCIES						= MODULE_NAME.concat(":EXCHANGE:5");
	String EXCHANGE_UPDATE									= MODULE_NAME.concat(":EXCHANGE:6");
	String EXCHANGE_ADD										= MODULE_NAME.concat(":EXCHANGE:7");
	String EXCHANGE_GET_ORGANIZATION_EXCHANGE_BY_CURRENCIES	= MODULE_NAME.concat(":EXCHANGE:8");
	String EXCHANGE_GET_BY_PROJECT_DETAIL_ID				= MODULE_NAME.concat(":EXCHANGE:9");
	
	//BLWidgetSession
	String WIDGET_SESSION_GET								= MODULE_NAME.concat(":WIDGET_SESSION:0");
	String WIDGET_SESSION_GET_BY_USER_ID					= MODULE_NAME.concat(":WIDGET_SESSION:1");
	String WIDGET_SESSION_GET_BY_SESSION_ID					= MODULE_NAME.concat(":WIDGET_SESSION:2");
	String WIDGET_SESSION_DELETE							= MODULE_NAME.concat(":WIDGET_SESSION:3");
	String WIDGET_SESSION_UPDATE							= MODULE_NAME.concat(":WIDGET_SESSION:4");
	String WIDGET_SESSION_ADD								= MODULE_NAME.concat(":WIDGET_SESSION:5");

	
	//BLRecordSession
	String RECORD_SESSION_GET								= MODULE_NAME.concat(":RECORD_SESSION:0");
	String RECORD_SESSION_GET_BY_RECORD_ID					= MODULE_NAME.concat(":RECORD_SESSION:1");
	String RECORD_SESSION_GET_BY_SESSION_ID					= MODULE_NAME.concat(":RECORD_SESSION:2");
	String RECORD_SESSION_DELETE							= MODULE_NAME.concat(":RECORD_SESSION:3");
	String RECORD_SESSION_UPDATE							= MODULE_NAME.concat(":RECORD_SESSION:4");
	String RECORD_SESSION_ADD								= MODULE_NAME.concat(":RECORD_SESSION:5");

	
	//BLReportsDataSource
	String REPORTS_DS_PROJECT_REPORT_RECORDS				= MODULE_NAME.concat(":REPORTSDS:0");
	String REPORTS_DS_PROJECT_REPORT_COST_SHEETS			= MODULE_NAME.concat(":REPORTSDS:1");
	String REPORTS_DS_TIME_SHEET_REPORT_RECORDS				= MODULE_NAME.concat(":REPORTSDS:1");
	
	//BLReports
	String REPORT_CREATE									= MODULE_NAME.concat(":REPORT:0");
	
	String REPORT_PROJECT_CREATE							= MODULE_NAME.concat(":REPORTPROJECT:0");
	
	String REPORT_TIME_SHEET_CREATE							= MODULE_NAME.concat(":REPORTTIMESHEET:0");
	
	//BLAudit
	String AUDIT_ADD										= MODULE_NAME.concat(":AUDIT:0");
	
	//Endpoint
	String ENDPOINT_GET_PROJECT_REPORT_DATA					= MODULE_NAME.concat(":ENDPOINT:0");
	String ENDPOINT_GET_TIME_SHEET_REPORT_DATA				= MODULE_NAME.concat(":ENDPOINT:1");
	String ENDPOINT_DELETE_PROJECT_DETAILS					= MODULE_NAME.concat(":ENDPOINT:2");
	String ENDPOINT_FINISH_PROJECT_DETAILS					= MODULE_NAME.concat(":ENDPOINT:3");
	String ENDPOINT_ABORT_PROJECT_DETAILS					= MODULE_NAME.concat(":ENDPOINT:4");
	String ENDPOINT_OPEN_PROJECT_DETAILS					= MODULE_NAME.concat(":ENDPOINT:5");
	String ENDPOINT_DELETE_TEAM_MEMBER_DETAIL				= MODULE_NAME.concat(":ENDPOINT:6");
	String ENDPOINT_GET_ACTIVITIES							= MODULE_NAME.concat(":ENDPOINT:7");
	String ENDPOINT_GET_RECORD_DETAILS						= MODULE_NAME.concat(":ENDPOINT:8");
	String ENDPOINT_SET_RECORD_TIMESHEET					= MODULE_NAME.concat(":ENDPOINT:9");
	
	//Reports WS Client 
	String REPORTS_WSCLIENT_CREATE_AXIS_CLIENT				= MODULE_NAME.concat("REPORTS_WSCLIENT:0");
	String REPORTS_WSCLIENT_SIGN_REQUEST_MESSAGE			= MODULE_NAME.concat("REPORTS_WSCLIENT:1");
	String REPORTS_WSCLIENT_DATE_TO_CALENDAR				= MODULE_NAME.concat("REPORTS_WSCLIENT:2");
	String REPORTS_WSCLIENT_CREATE_MAP_ENTRIES				= MODULE_NAME.concat("REPORTS_WSCLIENT:3");
	String REPORTS_WSCLIENT_PROJECT_REPORT_REQEUST			= MODULE_NAME.concat("REPORTS_WSCLIENT:4");
	String REPORTS_WSCLIENT_TIME_SHEET_REPORT_REQEUST		= MODULE_NAME.concat("REPORTS_WSCLIENT:5");
	
	
}
