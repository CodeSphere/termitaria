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
package ro.cs.ts.common;

/**
 * 
 * @author matti_joona
 * @author Coni
 *
 * Used for general constant of the system
 */
public interface IConstant {

	//general stuff
    String APP_NAME 													= "Time Sheet";
    String APP_CONTEXT 													= "Termitaria/TS";
    String OM_APP_CONTEXT 													= "Termitaria/OM";
    String APP_VERSION 													= "2.0";
    String APP_RELEASE_DATE 											= "2012";
    String APP_RELEASE_YEAR 											= "2012";
    String APP_START_PAGE												= "Main.htm";
    int 	 MODULE_ID													= 6;
    	
    String PERMISSION_CONSTANT 											= "PERMISSION_CONSTANT";
    String EXCEPTION_CONSTANT											= "EXCEPTION_CONSTANT";
    	
    //http inspector and interceptor constants
    String HTTP_INSPECTOR_SPECIAL_ATRIBUTE_REQUEST 						= "AFISEAZA_ATRIBUTE_SPECIALE_REQUEST";
    String HTTP_INSPECTOR_ATRIBUTE_REQUEST 								= "AFISEAZA_ATRIBUTE_REQUEST";
    String HTTP_INSPECTOR_PARAMETRII_REQUEST 							= "AFISEAZA_PARAMETRII_REQUEST";
    String HTTP_INSPECTOR_ATRIBUTE_SESSION 								= "AFISEAZA_ATRIBUTE_SESIUNE";
    String HTTP_INTERCEPTOR_ON 											= "HTTP_INTERCEPTOR_ON";

	//atribute pentru mesaje si erori
    String REQ_ATTR_ERRORS 												= "ERRORS";
    String REQ_ATTR_MSGS 												= "MESSAGES";
    String REQ_ATTR_WARN												= "WARNINGS";
    
    String BACK_CONSTANT												= "BACK_CONSTANT";
    
    //request commands
    String CMD_UPDATE 													= "UPDATE";
    String CMD_ADD 														= "ADD";
	String CMD_GET 														= "GET";
	String CMD_DELETE 													= "DELETE";
	String CMD_NEW 														= "NEW";
	String CMD_SAVE 													= "SAVE";
	String REQ_ACTION 													= "ACTION";
	
    String BANNED_URLs 													= "bannedURLs";
    
    //general attributes	
	String ACCEPTED_LANGUAGES											= "ACCEPTED_LANGUAGES";
	String LANGUAGE_ATTRIBUTE											= "siteLanguage";
	String KEY															= "key:";
	String FROM_ORGANIZATION											= "from.organization";	
    
    //SESSION_PARAMETERS
    String SESS_ORGANISATION_ID											= "SESS_ORGANISATION_ID";
    String SESS_ORGANISATION_NAME										= "SESS_ORGANISATION_NAME";
    String SESS_MESSAGES												= "SESS_MESSAGES";
			 
    String SECURITY_NO_RIGHTS											= "security.norights";
            
    //Standard Theme code
	String STANDARD_THEME												= "standard";
	
	Integer STANDARD_NR_OF_WORK_DAYS 									= 21;
	
	// Number of characters that fit in a line, for display messages,
    // if there are big words, without spaces
	Integer NR_CHARS_PER_LINE_MESSAGE = 64;	
	
	//message view for info/errors
	String FORM_VIEW_MESSAGES											= "Messages";
	String FORM_VIEW_MESSAGES_NEW_WINDOW								= "MessagesNewWindow";
	String FORM_PANEL_VIEW_MESSAGES										= "MessagesPanel";
	
	//general messages key
	String GENERAL_ERROR												= "generalError";
	
	//projects billing price
	String PROJECTS_BILLING_PRICE										= "PROJECTS_BILLING_PRICE";
	
	 //Sort Directions
    byte ASCENDING 														= -1;
    byte DESCENDING														=  1;
    
    Character BILLABLE_YES												= 'Y';
    
    //TS Web Service
    String TS_WS_SERVER_NAMESPACE										= "http://localhost:8080/TS/services/schemas/messages";
	
	////////////////////////////CLASSIFIED LISTS - NOMENCLATOARE//////////////////////////////////////////
	String NOM_RESULTS_PER_PAGE											= "RESULTS_PER_PAGE";
	 String NOM_EXTENSION_TYPE											= "EXTENSION_NOMENCLATOR";
	//Results per page
	int[] RESULTS_PER_PAGE												= {5,10,50};
	
	//Record status
	String NOM_RECORD_STATUS											= "NOM_RECORD_STATUS";
	
	byte NOM_RECORD_STATUS_ACTIVE										= 1;
	byte NOM_RECORD_STATUS_DELETED										= 2;
	
	byte NOM_COST_SHEET_STATUS_ACTIVE									= 1;
	byte NOM_COST_SHEET_STATUS_DELETED									= 2;
	
	byte NOM_CURRENCY_STATUS_ACTIVE										= 1;
	byte NOM_CURRENCY_STATUS_DELETED									= 2;
	
	byte NOM_EXCHANGE_STATUS_ACTIVE										= 1;
	byte NOM_EXCHANGE_STATUS_DELETED									= 2;
	
	String PROJECT_FROM_ORGANIZATION 									= "project.from.org";
	
    //project details status nomenclator
	String NOM_PROJECT_DETAILS_STATUS									= "NOM_PROJECT_DETAILS_STATUS";

	byte NOM_PROJECT_DETAILS_STATUS_OPENED								= 1;
	byte NOM_PROJECT_DETAILS_STATUS_FINISHED							= 2;
	byte NOM_PROJECT_DETAILS_STATUS_ABORTED								= 3;
	byte NOM_PROJECT_DETAILS_STATUS_DELETED								= 4;	
		
	//Record status
	String NOM_TEAM_MEMBER_DETAIL_STATUS								= "NOM_TEAM_MEMBER_DETAIL_STATUS";
	
	byte NOM_TEAM_MEMBER_DETAIL_STATUS_ACTIVE							= 1;
	byte NOM_TEAM_MEMBER_DETAIL_STATUS_DELETED							= 2;
	
	//PersonDetail status nomenclator
	byte NOM_PERSON_DETAIL_STATUS_ACTIVE								= 1;
	byte NOM_PERSON_DETAIL_STATUS_DELETED								= 2;
	
    //project's status nomenclator STATUS	
	byte NOM_PROJECT_STATUS_OPENED										= 1;
	byte NOM_PROJECT_STATUS_CLOSED										= 2;
	byte NOM_PROJECT_STATUS_ABORTED										= 3;
	byte NOM_PROJECT_STATUS_DELETED										= 4;
	
	// notificationPercentage status
	byte NOM_PROJECT_NOTIFICATION_STATUS_NOT_SENT						= 0;
	byte NOM_PROJECT_NOTIFICATION_STATUS_NOTIFICATION_PERCENTAGE		= 1;
	byte NOM_PROJECT_NOTIFICATION_STATUS_BUDGET_OVERFLOW				= 2;	
	
	//labels keys for project's nomenclator for status
	String NOM_PROJECT_STATUS_OPENED_LABEL_KEY	  		 				= "project.status.opened";	
	String NOM_PROJECT_STATUS_CLOSED_LABEL_KEY	  		 				= "project.status.finished";	
	String NOM_PROJECT_STATUS_ABORTED_LABEL_KEY							= "project.status.aborted";
	String NOM_PROJECT_STATUS											= "PROJECT_STATUS";
	
	//Client's types nomenclator
	public byte NOM_CLIENT_TYPE_FIRM									= 1;//the client is a firm
	public byte NOM_CLIENT_TYPE_PERSON									= 2;//the client is a person
	
	//Billable property nomenclator
	String NOM_BILLABLE													= "NOM_BILLABLE";
	
	Character NOM_BILLABLE_YES											= 'Y';
	Character NOM_BILLABLE_NO											= 'N';		
	
	//BILLABLE label keys
	String NOM_BILLABLE_YES_LABEL_KEY									= "billable.yes";
	String NOM_BILLABLE_NO_LABEL_KEY									= "billable.no";
	
	//RECORD SEARCH - PROJECT OPTION - NOMENClATOR
	byte NOM_RECORD_SEARCH_ALL											= -2;
	byte NOM_RECORD_SEARCH_FOR_ORG										= -1;
	byte NOM_RECORD_SEARCH_IN_ALL_PROJECTS								= 0;
	
	//COST SHEET SEARCH - PROJECT OPTION - NOMENClATOR
	byte NOM_COST_SHEET_SEARCH_ALL										= -2;
	byte NOM_COST_SHEET_SEARCH_FOR_ORG									= -1;
	byte NOM_COST_SHEET_SEARCH_IN_ALL_PROJECTS							= 0;
	
	//EXCHANGE SEARCH/ADD - PROJECT OPTION - NOMENClATOR
	byte NOM_EXCHANGE_SEARCH_ALL										= -2;
	byte NOM_EXCHANGE_SEARCH_FOR_ORG									= -1;
	byte NOM_EXCHANGE_SEARCH_IN_ALL_PROJECTS							= 0;
	
	// Activity search - project option - nomenclator
	byte NOM_ACTIVITY_SEARCH_ALL										= -1;	
	byte NOM_ACTIVITY_SEARCH_IN_ORGANIZATION							= 0;
	
	byte NOM_PERSON_SEARCH_IN_ORGANIZATION								= -1;
	byte NOM_PERSON_SEARCH_ALL_PROJECTS									= 0;
	
	//RECORD FORM - PROJECT SELECT - RECORD FOR ORGANIZATION OPTION
	int NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION						= -1;
	
	//COST SHEET FORM - PROJECT SELECT - COST SHEET FOR ORGANIZATION OPTION
	int NOM_COST_SHEET_FORM_PROJECT_SELECT_ORG_OPTION					= -1;
	
	//EXCHANGE FORM - PROJECT SELECT - EXCHANGE FOR ORGANIZATION OPTION
	int NOM_EXCHANGE_FORM_PROJECT_SELECT_ORG_OPTION						= -1;
	
	//Activity status nomenclator
	byte NOM_ACTIVITY_STATUS_ACTIVE										= 1;
	byte NOM_ACTIVITY_STATUS_DELETED									= 2;
	
	//project's TIME UNIT nomenclator	
	byte NOM_TIME_UNIT_HOUR												= 1;
	byte NOM_TIME_UNIT_DAY												= 2;
	byte NOM_TIME_UNIT_WEEK												= 3;
	byte NOM_TIME_UNIT_MONTH											= 4;
	
	//labels keys for time unit's nomenclator
	String NOM_TIME_UNIT_HOUR_LABEL_KEY									= "time.unit.hour";
	String NOM_TIME_UNIT_DAY_LABEL_KEY									= "time.unit.day";
	String NOM_TIME_UNIT_WEEK_LABEL_KEY									= "time.unit.week";
	String NOM_TIME_UNIT_MONTH_LABEL_KEY								= "time.unit.month";
	String NOM_TIME_UNIT												= "NOM_TIME_UNIT";
	
	// notification subjects 
	String NOTIFICATION_SUBJECT_NOTIFICATION_PERCENTAGE 				= "notification.subject.notification.percentage";
	String NOTIFICATION_SUBJECT_BUDGET_OVERFLOW 						= "notification.subject.budget.overflow";
	
	String NOTIFICATION_SUBJECT_TEAM_MEMBER_DETAIL_UPDATE				= "notification.subject.team.member.detail.update";	
	String NOTIFICATION_SUBJECT_PERSON_DETAIL_UPDATE					= "notification.subject.person.detail.update";
	String NOTIFICATION_SUBJECT_PROJECT_DETAIL_UPDATE					= "notification.subject.project.detail.update";
	
	String NOTIFICATION_SUBJECT_RECORD_UPDATE							= "notification.subject.record.update";
	String NOTIFICATION_SUBJECT_RECORD_DELETE							= "notification.subject.record.delete";
	String NOTIFICATION_SUBJECT_RECORD_ADD								= "notification.subject.record.add";
	
	String NOTIFICATION_SUBJECT_COST_UPDATE								= "notification.subject.cost.update";
	String NOTIFICATION_SUBJECT_COST_DELETE								= "notification.subject.cost.delete";
	String NOTIFICATION_SUBJECT_COST_ADD								= "notification.subject.cost.add";
	
	String NOTIFICATION_SUBJECT_ACTIVITY_UPDATE							= "notification.subject.activity.update";
	String NOTIFICATION_SUBJECT_ACTIVITY_DELETE							= "notification.subject.activity.delete";
	String NOTIFICATION_SUBJECT_ACTIVITY_ADD							= "notification.subject.activity.add";
	
	String NOTIFICATION_SUBJECT_EXCHANGE_UPDATE							= "notification.subject.exchange.update";
	String NOTIFICATION_SUBJECT_EXCHANGE_DELETE							= "notification.subject.exchange.delete";
	String NOTIFICATION_SUBJECT_EXCHANGE_ADD							= "notification.subject.exchange.add";
	
	// notification messages
	String NOTIFICATION_MESSAGE_NOTIFICATION_PERCENTAGE					= "notification.message.notification.percentage";
	String NOTIFICATION_MESSAGE_BUDGET_OVERFLOW							= "notification.message.budget.overflow";
	
	String NOTIFICATION_MESSAGE_TEAM_MEMBER_DETAIL_UPDATE				= "notification.message.team.member.detail.update";	
	String NOTIFICATION_MESSAGE_PERSON_DETAIL_UPDATE					= "notification.message.person.detail.update";
	String NOTIFICATION_MESSAGE_PROJECT_DETAIL_UPDATE					= "notification.message.project.detail.update";
	
	String NOTIFICATION_MESSAGE_RECORD_UPDATE							= "notification.message.record.update";
	String NOTIFICATION_MESSAGE_RECORD_DELETE							= "notification.message.record.delete";
	String NOTIFICATION_MESSAGE_RECORD_ADD								= "notification.message.record.add";
	
	String NOTIFICATION_MESSAGE_COST_UPDATE								= "notification.message.cost.update";
	String NOTIFICATION_MESSAGE_COST_DELETE								= "notification.message.cost.delete";
	String NOTIFICATION_MESSAGE_COST_ADD								= "notification.message.cost.add";	
	
	String NOTIFICATION_MESSAGE_RECORD_PROJECT_UPDATE					= "notification.message.record.project.update";
	String NOTIFICATION_MESSAGE_RECORD_PROJECT_DELETE					= "notification.message.record.project.delete";
	String NOTIFICATION_MESSAGE_RECORD_PROJECT_ADD						= "notification.message.record.project.add";
	
	String NOTIFICATION_MESSAGE_COST_PROJECT_UPDATE						= "notification.message.cost.project.update";
	String NOTIFICATION_MESSAGE_COST_PROJECT_DELETE						= "notification.message.cost.project.delete";
	String NOTIFICATION_MESSAGE_COST_PROJECT_ADD						= "notification.message.cost.project.add";
	
	String NOTIFICATION_MESSAGE_ACTIVITY_PROJECT_UPDATE					= "notification.message.activity.project.update";
	String NOTIFICATION_MESSAGE_ACTIVITY_PROJECT_DELETE					= "notification.message.activity.project.delete";
	String NOTIFICATION_MESSAGE_ACTIVITY_PROJECT_ADD					= "notification.message.activity.project.add";
	
	String NOTIFICATION_MESSAGE_ACTIVITY_ORG_UPDATE						= "notification.message.activity.org.update";
	String NOTIFICATION_MESSAGE_ACTIVITY_ORG_DELETE						= "notification.message.activity.org.delete";
	String NOTIFICATION_MESSAGE_ACTIVITY_ORG_ADD						= "notification.message.activity.org.add";
	
	String NOTIFICATION_MESSAGE_EXCHANGE_PROJECT_UPDATE					= "notification.message.exchange.project.update";
	String NOTIFICATION_MESSAGE_EXCHANGE_PROJECT_DELETE					= "notification.message.exchange.project.delete";
	String NOTIFICATION_MESSAGE_EXCHANGE_PROJECT_ADD					= "notification.message.exchange.project.add";
	
	String NOTIFICATION_MESSAGE_EXCHANGE_ORG_UPDATE						= "notification.message.exchange.org.update";
	String NOTIFICATION_MESSAGE_EXCHANGE_ORG_DELETE						= "notification.message.exchange.org.delete";
	String NOTIFICATION_MESSAGE_EXCHANGE_ORG_ADD						= "notification.message.exchange.org.add";
	
	//notification setting status
	byte NOTIFICATION_SETTING_TRUE										= 1;
	byte NOTIFICATION_SETTING_FALSE										= 0;
	
	//notification setting nomenclator
	byte NOTIFICATION_SETTING_RECORD_ADD								= 1;
	byte NOTIFICATION_SETTING_RECORD_DELETE								= 2;
	byte NOTIFICATION_SETTING_RECORD_UPDATE								= 3;
	byte NOTIFICATION_SETTING_COST_ADD									= 4;
	byte NOTIFICATION_SETTING_COST_DELETE								= 5;
	byte NOTIFICATION_SETTING_COST_UPDATE								= 6;
	byte NOTIFICATION_SETTING_TEAM_MEMBER_DETAIL_UPDATE					= 7;
	byte NOTIFICATION_SETTING_BUDGET_OVERFLOW							= 8;
	byte NOTIFICATION_SETTING_PERCENTAGE_OVERFLOW						= 9;
	byte NOTIFICATION_SETTING_PERSON_DETAIL_UPDATE						= 10;
	byte NOTIFICATION_SETTING_ACTIVITY_ADD								= 13;
	byte NOTIFICATION_SETTING_ACTIVITY_DELETE							= 14;
	byte NOTIFICATION_SETTING_ACTIVITY_UPDATE							= 15;
	byte NOTIFICATION_SETTING_EXCHANGE_ADD								= 16;
	byte NOTIFICATION_SETTING_EXCHANGE_DELETE							= 17;
	byte NOTIFICATION_SETTING_EXCHANGE_UPDATE							= 18;
	byte NOTIFICATION_SETTING_PROJECT_DETAIL_UPDATE						= 19;
	
	byte NOTIFICATION_LANGUAGE_RO										= 11;
	byte NOTIFICATION_LANGUAGE_EN										= 12;
	
	//--------------------------------------------------------------- REPORTS RELATED START --------------------------------------------------
	
	//report subtotal interval nomenclator
	byte NOM_SUBTOTAL_NONE												= 0;
	byte NOM_SUBTOTAL_DAILY												= 1;
	byte NOM_SUBTOTAL_WEEKLY											= 2;
	byte NOM_SUBTOTAL_MONTHLY											= 3;
	byte NOM_SUBTOTAL_QUARTERLY											= 4;
	
	
	//report price compute type
	int NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE						= 0;
	int NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY						= 1;
	
	//label keys for price compute type
	String NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE_LABEL_KEY 		= "price.compute.per.resource";
	String NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY_LABEL_KEY 		= "price.compute.per.activity";
	String NOM_PRICES_COMPUTE_TYPES = "NOM_PRICES_COMPUTE_TYPES";
		
	
	//label keys for report subtotal nomenclator
	String NOM_SUBTOTAL_NONE_LABEL_KEY									= "report.subtotal.interval.none";
	String NOM_SUBTOTAL_DAILY_LABEL_KEY									= "report.subtotal.interval.daily";
	String NOM_SUBTOTAL_WEEKLY_LABEL_KEY								= "report.subtotal.interval.weekly";
	String NOM_SUBTOTAL_MONTHLY_LABEL_KEY								= "report.subtotal.interval.monthly";
	String NOM_SUBTOTAL_QUARTERLY_LABEL_KEY								= "report.subtotal.interval.quarterly";
	String NOM_SUBTOTAL_PERIOD											= "NOM_SUBTOTAL_PERIOD";
	
	//label for the supported language codes 
	String SUPPORTED_LANGUAGE_TOTAL_KEY									= "accepted.language.total";
	String LANGUAGE_CODE_KEY_PREFIX 									= "accepted.language.code.";
	String NOM_SUPPORTED_LANGUAGES				 						= "NOM_SUPPORTED_LANGUAGES";
	
	//labels for report formats nomenclators
	String[] REPORT_FORMATS												= {"HTML", "PDF", "XLS"};
	String NOM_REPORT_FORMATS											= "FORMATS";
	String REPORT_UID													= "REPORT_UID";
	String REPORT_TYPE													= "REPORT_TYPE";
	String REPORT_TYPE_PROJECT											= "PRJ";
	String REPORT_TYPE_TIME_SHEET										= "TS";
	String REPORT_PARAM_MAP								= "REPORT_PARAM_MAP";
	
	
	//PROJECT REPORT PARAMS MAP KEYS
	String TS_PROJECT_REPORT_LOCALE_PARAM								= "locale";
	String TS_PROJECT_REPORT_REPORT_TITLE_PARAM							= "reportTitle";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_START_DATE					= "startDate";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_END_DATE					= "endDate";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_NAME				= "projectName";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_ID					= "projectId";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_CURRENCY_NAME				= "currencyName";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_CURRENCY_ID				= "currencyId";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_BILLABLE					= "billable";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_FORMAT						= "format";
	String TS_PROJECT_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE	= "priceComputeType";
	
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OWNER_NAME				= "recordOwnerName";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_ACTIVITY_NAME			= "recordActivityName";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_TIME					= "recordTime";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_START_TIME				= "recordStartTime";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_END_TIME				= "recordEndTime";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_COST_PRICE				= "recordCostPrice";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_BILLABLE				= "recordBillable";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE			= "recordBillingPrice";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_TIME			= "recordOverTimeTime";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_START_TIME	= "recordOverTimeStartTime";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_END_TIME		= "recordOverTimeEndTime";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE 	= "recordOverTimeCostPrice";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLABLE		= "recordOverTimeBillable";
	String TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE = "recordOverTimeBillingPrice";
	
	String TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_REPORTER_NAME		= "costSheetReporterName";
	String TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_ACTIVITY_NAME		= "costSheetActivityName";
	String TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_DATE				= "costSheetDate";
	String TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_COST_PRICE			= "costSheetCostPrice";
	String TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_BILLING_PRICE		= "costSheetBillingPrice";
	String TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_BILLABLE			= "costSheetBillable";
	String TS_PROJECT_REPORT_COLUMN_LABEL_TOTAL_COST_PRICE				= "totalCostPrice";
	String TS_PROJECT_REPORT_COLUMN_LABEL_TOTAL_BILLING_PRICE			= "totalBillingPrice";
	String TS_PROJECT_REPORT_SUBTOTAL_INTERVAL							= "subtotalInterval";
	String TS_PROJECT_REPORT_IS_EMBEDDABLE								= "isEmbeddable";  
	
	//TIME SHEET REPORT PARAMS MAP KEYS
	String TS_TIME_SHEET_REPORT_LOCALE_PARAM							= "locale";
	String TS_TIME_SHEET_REPORT_REPORT_TITLE_PARAM						= "reportTitle";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_START_DATE				= "startDate";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_END_DATE				= "endDate";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_CURRENCY_ID				= "currencyId";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_CURRENCY_NAME			= "currencyName";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_BILLABLE				= "billable";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_FORMAT					= "format";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE	= "priceComputeType";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_TEAM_MEMBER_IDS			= "teamMemberIds"; 
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_PERSON_IDS				= "personIds";
	String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_ORGANIZATION_ID			= "organizationId";
	
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OWNER_NAME			= "recordOwnerName";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_PROJECT_NAME		= "recordProjectName";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_ACTIVITY_NAME		= "recordActivityName";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_TIME				= "recordTime";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_START_TIME			= "recordStartTime";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_END_TIME			= "recordEndTime";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_COST_PRICE			= "recordCostPrice";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_BILLABLE			= "recordBillable";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE		= "recordBillingPrice";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_TIME		= "recordOverTimeTime";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_START_TIME= "recordOverTimeStartTime";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_END_TIME	= "recordOverTimeEndTime";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE  = "recordOverTimeCostPrice";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLABLE	= "recordOverTimeBillable";
	String TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE = "recordOverTimeBillingPrice";
	String TS_TIME_SHEET_REPORT_SUBTOTAL_INTERVAL							= "subtotalInterval";
	String TS_TIME_SHEET_REPORT_IS_EMBEDDABLE								= "isEmbeddable";
	
	//PROJECT REPORT RECORDS TABLE COLUMNS KEY ARRAY
	String[] REPORT_PROJECT_KEY_ARRAY 									= {"reportTitle","recordOwnerName","recordActivityName","recordTime","recordStartTime","recordEndTime","recordCostPrice","recordBillable","recordBillingPrice","recordOverTimeTime","recordOverTimeStartTime","recordOverTimeEndTime","recordOverTimeCostPrice","recordOverTimeBillable","recordOverTimeBillingPrice","costSheetReporterName","costSheetActivityName","costSheetDate","costSheetCostPrice","costSheetBillingPrice","costSheetBillable","totalCostPrice","totalBillingPrice"};
	String[] REPORT_PROJECT_PRESENT_RECORDS_KEY_ARRAY					= {"recordOwnerName", "recordActivityName", "recordTime", "recordBillable", "recordBillingPrice", "recordOverTimeTime", "recordOverTimeBillable", "recordOverTimeBillingPrice"};
	String[] REPORT_PROJECT_PRESENT_COST_SHEETS_KEY_ARRAY				= {"costSheetReporterName", "costSheetActivityName", "costSheetDate", "costSheetBillable", "costSheetBillingPrice"};
	int REPORT_PROJECT_RECORD_TABLE_KEYS_START							=  1;
	int REPORT_PROJECT_RECORD_TABLE_KEYS_END							=  14;
	int REPORT_PROJECT_COST_TABLE_KEYS_START							=  15;
	int REPORT_PROJECT_COST_TABLE_KEYS_END								=  20;

	
	//TIME SHEET REPORT RECORDS TABLE COLUMNS KEY ARRAY
	String[] TIME_SHEET_REPORT_PARAMS_KEY_ARRAY							= { "reportTitle", "recordOwnerName", "recordProjectName", "recordActivityName", "recordTime",
																						 	"recordStartTime", "recordEndTime", "recordCostPrice", "recordBillable", 
																						 	"recordBillingPrice", "recordOverTimeTime", "recordOverTimeStartTime", 
																						 	"recordOverTimeEndTime", "recordOverTimeCostPrice", "recordOverTimeBillable",
																						 	"recordOverTimeBillingPrice"};
	String[] TIME_SHEET_REPORT_PRESENT_RECORDS_KEY_ARRAY				= {"recordOwnerName", "recordProjectName", "recordActivityName", "recordTime", "recordBillable", "recordBillingPrice", "recordOverTimeTime", "recordOverTimeBillable", "recordOverTimeBillingPrice"};
	int TIME_SHEET_REPORT_RECORD_TABLE_START_INDEX						= 1;
	
	//Reports Web Service Namespace
	String REPORTS_WEB_SERVICE_NAMESPACE								= "http://localhost:8080/Reports/services/schemas/messages";
	
	//Axis Fault Detail local parts
	String AXIS_FAULT_DETAIL_CODE_LOCALPART								= "code";
	String AXIS_FAULT_DETAIL_MESSAGE_LOCALPART							= "message";
	
	//Project Report Request local parts
	String PROJECT_REPORT_ROOT_PAYLOAD_LOCALPART						= "TSProjectReportRequest";
	String PROJECT_REPORT_PARAMS_LOCALPART								= "tsProjectReportParams";
	String PROJECT_REPORT_PROPERTIES_LOCALPART							= "properties";
	
	//Time Sheet Report Request local parts
	String TIME_SHEET_REPORT_ROOT_PAYLOAD_LOCALPART						= "TSTimeSheetReportRequest";
	String TIME_SHEET_REPORT_PARAMS_LOCALPART							= "tsTimeSheetReportParams";
	String TIME_SHEET_REPORT_PROPERTIES_LOCALPART						= "properties";
	
	//Report Response local parts
	String REPORT_RESPONSE_REPORT_FILE_NAME_LOCALPART					= "reportFileName";
	String REPORT_RESPONSE_ATTACHMENT_ID_LOCALPART						= "reportFileAttachmentId";
	
	//AXIS HashMap and List elements local parts
	String MAP_ENTRY_LOCALPART											= "entry";
	String MAP_KEY_LOCALPART											= "key";
	String MAP_VALUE_LOCALPART											= "value";
	String LIST_ENTRY													= "listEntry";
	
	//--------------------------------------------------------------- REPORTS RELATED END --------------------------------------------------
	
	
	//--------------------------------------------------------------- AUDIT RELATED START --------------------------------------------------
	
    //AUDIT Web Service Clients config parameters
    String AUDIT_WS_SERVER_NAMESPACE									= "http://localhost:8080/Audit/services/schemas/messages";
	
	//AUDIT BEAN PROPERTIES
	public static final String auditDate 								= "date";
	public static final String auditPersonId							= "personId";
	public static final String auditMessageRO							= "messageRO";
	public static final String auditMessageEN							= "messageEN";
	public static final String auditEvent 								= "event";
	public static final String auditOrganisationId						= "organisationId";	
	public static final String auditModuleId							= "moduleId";
	public static final String auditFirstName							= "firstName";
	public static final String auditLastName							= "lastName";
	
	//AUDIT EVENT TYPES
	String AUDIT_EVENT_RECORD_ADD_TYPE									= "audit.event.record.add.type";
	String AUDIT_EVENT_RECORD_UPDATE_TYPE								= "audit.event.record.update.type";
	String AUDIT_EVENT_RECORD_DELETE_TYPE								= "audit.event.record.delete.type";
	String AUDIT_EVENT_PROJECT_DETAIL_UPDATE_TYPE						= "audit.event.project.detail.update.type";
	String AUDIT_EVENT_ACTIVITY_ADD_TYPE								= "audit.event.activity.add.type";
	String AUDIT_EVENT_ACTIVITY_UPDATE_TYPE								= "audit.event.activity.update.type";
	String AUDIT_EVENT_ACTIVITY_DELETE_TYPE								= "audit.event.activity.delete.type";
	String AUDIT_EVENT_COST_SHEET_ADD_TYPE								= "audit.event.cost.sheet.add.type";
	String AUDIT_EVENT_COST_SHEET_UPDATE_TYPE							= "audit.event.cost.sheet.update.type";
	String AUDIT_EVENT_COST_SHEET_DELETE_TYPE							= "audit.event.cost.sheet.delete.type";
	String AUDIT_EVENT_CURRENCY_ADD_TYPE								= "audit.event.currency.add.type";
	String AUDIT_EVENT_CURRENCY_UPDATE_TYPE								= "audit.event.currency.update.type";
	String AUDIT_EVENT_CURRENCY_DELETE_TYPE								= "audit.event.currency.delete.type";
	String AUDIT_EVENT_EXCHANGE_ADD_TYPE								= "audit.event.exchange.add.type";
	String AUDIT_EVENT_EXCHANGE_UPDATE_TYPE								= "audit.event.exchange.update.type";
	String AUDIT_EVENT_EXCHANGE_DELETE_TYPE								= "audit.event.exchange.delete.type";
	String AUDIT_EVENT_PERSON_DETAIL_UPDATE_TYPE						= "audit.event.person.detail.update.type";
	String AUDIT_EVENT_TEAM_MEMBER_DETAIL_UPDATE_TYPE					= "audit.event.team.member.detail.update.type";
	
	//AUDIT EVENT MESSAGES
	String AUDIT_EVENT_RECORD_FOR_PROJECT_ADD_MESSAGE					= "audit.event.record.for.project.add.message";
	String AUDIT_EVENT_RECORD_FOR_PROJECT_UPDATE_MESSAGE				= "audit.event.record.for.project.update.message";
	String AUDIT_EVENT_RECORD_FOR_PROJECT_DELETE_MESSAGE				= "audit.event.record.for.project.delete.message";
	String AUDIT_EVENT_RECORD_FOR_ORG_ADD_MESSAGE						= "audit.event.record.for.org.add.message";
	String AUDIT_EVENT_RECORD_FOR_ORG_UPDATE_MESSAGE					= "audit.event.record.for.org.update.message";
	String AUDIT_EVENT_RECORD_FOR_ORG_DELETE_MESSAGE					= "audit.event.record.for.org.delete.message";
	String AUDIT_EVENT_PROJECT_DETAIL_UPDATE_MESSAGE					= "audit.event.project.detail.update.message";
	String AUDIT_EVENT_ACTIVITY_FOR_PROJECT_ADD_MESSAGE					= "audit.event.actvity.for.project.add.message";
	String AUDIT_EVENT_ACTIVITY_FOR_PROJECT_UPDATE_MESSAGE				= "audit.event.actvity.for.project.update.message";
	String AUDIT_EVENT_ACTIVITY_FOR_PROJECT_DELETE_MESSAGE				= "audit.event.actvity.for.project.delete.message";
	String AUDIT_EVENT_ACTIVITY_FOR_ORG_ADD_MESSAGE						= "audit.event.actvity.for.org.add.message";
	String AUDIT_EVENT_ACTIVITY_FOR_ORG_UPDATE_MESSAGE					= "audit.event.actvity.for.org.update.message";
	String AUDIT_EVENT_ACTIVITY_FOR_ORG_DELETE_MESSAGE					= "audit.event.actvity.for.org.delete.message";
	String AUDIT_EVENT_COST_SHEET_FOR_PROJECT_ADD_MESSAGE				= "audit.event.cost.sheet.for.project.add.message";
	String AUDIT_EVENT_COST_SHEET_FOR_PROJECT_UPDATE_MESSAGE			= "audit.event.cost.sheet.for.project.update.message";
	String AUDIT_EVENT_COST_SHEET_FOR_PROJECT_DELETE_MESSAGE			= "audit.event.cost.sheet.for.project.delete.message";
	String AUDIT_EVENT_COST_SHEET_FOR_ORG_ADD_MESSAGE					= "audit.event.cost.sheet.for.org.add.message";
	String AUDIT_EVENT_COST_SHEET_FOR_ORG_UPDATE_MESSAGE				= "audit.event.cost.sheet.for.org.update.message";
	String AUDIT_EVENT_COST_SHEET_FOR_ORG_DELETE_MESSAGE				= "audit.event.cost.sheet.for.org.delete.message";
	String AUDIT_EVENT_CURRENCY_ADD_MESSAGE								= "audit.event.currency.add.message";
	String AUDIT_EVENT_CURRENCY_UPDATE_MESSAGE							= "audit.event.currency.update.message";
	String AUDIT_EVENT_CURRENCY_DELETE_MESSAGE							= "audit.event.currency.delete.message";
	String AUDIT_EVENT_EXCHANGE_FOR_PROJECT_ADD_MESSAGE					= "audit.event.exchange.for.project.add.message";
	String AUDIT_EVENT_EXCHANGE_FOR_PROJECT_UPDATE_MESSAGE				= "audit.event.exchange.for.project.update.message";
	String AUDIT_EVENT_EXCHANGE_FOR_PROJECT_DELETE_MESSAGE				= "audit.event.exchange.for.project.delete.message";
	String AUDIT_EVENT_EXCHANGE_FOR_ORG_ADD_MESSAGE						= "audit.event.exchange.for.org.add.message";
	String AUDIT_EVENT_EXCHANGE_FOR_ORG_UPDATE_MESSAGE					= "audit.event.exchange.for.org.update.message";
	String AUDIT_EVENT_EXCHANGE_FOR_ORG_DELETE_MESSAGE					= "audit.event.exchange.for.org.delete.message";
	String AUDIT_EVENT_PERSON_DETAIL_UPDATE_MESSAGE						= "audit.event.person.detail.update.message";
	String AUDIT_EVENT_TEAM_MEMBER_DETAIL_UPDATE_MESSAGE				= "audit.event.team.member.detail.update.message";
	
	//--------------------------------------------------------------- AUDIT RELATED END
}
