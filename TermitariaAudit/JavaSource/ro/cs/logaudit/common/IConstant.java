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
package ro.cs.logaudit.common;

/**
 * 
 * @author matti_joona
 * @author Adelina
 *
 * Used for general constant of the system
 */
public interface IConstant {

	//general stuff
    public static String APP_NAME 										= "LOGAUDIT";
    public static String APP_CONTEXT 									= "Termitaria/Audit";
    public static String OM_APP_CONTEXT 								= "Termitaria/OM";
    public static String REDIRECT_URL 									= "/Termitaria/OM/SignOn.htm";
    public static String APP_VERSION 									= "1.5";
    public static String APP_RELEASE_DATE 								= "2010";
    public static String APP_RELEASE_YEAR 								= "2009";
    public static String APP_START_PAGE									= "Main.htm";
    public static int 	 MODULE_ID										= 3;
    
    public static String PERMISSION_CONSTANT 							= "PERMISSION_CONSTANT";
    public static String EXCEPTION_CONSTANT								= "EXCEPTION_CONSTANT";
    	
    //http inspector and interceptor constants
    public static String HTTP_INSPECTOR_SPECIAL_ATRIBUTE_REQUEST 		= "AFISEAZA_ATRIBUTE_SPECIALE_REQUEST";
    public static String HTTP_INSPECTOR_ATRIBUTE_REQUEST 				= "AFISEAZA_ATRIBUTE_REQUEST";
    public static String HTTP_INSPECTOR_PARAMETRII_REQUEST 				= "AFISEAZA_PARAMETRII_REQUEST";
    public static String HTTP_INSPECTOR_ATRIBUTE_SESSION 				= "AFISEAZA_ATRIBUTE_SESIUNE";
    public static String HTTP_INTERCEPTOR_ON 							= "HTTP_INTERCEPTOR_ON";

	//atribute pentru mesaje si erori
    public static String REQ_ATTR_ERRORS 								= "ERRORS";
    public static String REQ_ATTR_MSGS 									= "MESSAGES";
    
    public static String BANNED_URLs 									= "bannedURLs";
    
    //general attributes	
	public static String ACCEPTED_LANGUAGES								= "ACCEPTED_LANGUAGES";
	public static String LANGUAGE_ATTRIBUTE								= "siteLanguage";
    
    //SESSION_PARAMETERS
    public static String SESS_ORGANISATION_ID							= "SESS_ORGANISATION_ID";
    public static String SESS_ORGANISATION_NAME							= "SESS_ORGANISATION_NAME";
    public static String SESS_MESSAGES									= "SESS_MESSAGES";
			 
    public static String SECURITY_NO_RIGHTS								= "security.norights";
    
    //Standard Theme code
	public static String STANDARD_THEME									= "standard";
	
	////////////////////////////CLASSIFIED LISTS - NOMENCLATOARE//////////////////////////////////////////
	public static String NOM_MODULES									= "MODULES";
	public static String NOM_RESULTS_PER_PAGE							= "RESULTS_PER_PAGE";
	public static String NOM_AUDIT_EVENTS_OM							= "AUDIT_EVENTS_OM";
	public static String NOM_AUDIT_EVENTS_DM							= "AUDIT_EVENTS_DM";
	public static String NOM_AUDIT_EVENTS_CM							= "AUDIT_EVENTS_CM";
	public static String NOM_AUDIT_EVENTS_TS							= "AUDIT_EVENTS_TS";
	public static String NOM_AUDIT_REPORT_ORIENTATIONS					= "ORIENTATIONS";
	public static String NOM_AUDIT_REPORT_FORMATS						= "FORMATS";		
	
	//Results per page
	public static int[] RESULTS_PER_PAGE								= {5,10,50};
	
	//message view for info/errors
	public static String FORM_VIEW_MESSAGES								= "Messages";
	public static String FORM_VIEW_MESSAGES_NEW_WINDOW					= "MessagesNewWindow";
	
	//general messages key
	public static String GENERAL_ERROR									= "generalError";
	
	 //Sort Directions
    byte ASCENDING 														= -1;
    byte DESCENDING														=  1;       
    
    //Audit Web Service
    public static String AUDIT_WS_SERVER_NAMESPACE						= "http://localhost:8080/Audit/services/schemas/messages";
    
    //Modules' nomenclator
    public static String NOM_MODULE_OM									= "om";
    public static String NOM_MODULE_DM									= "dm";
    public static String NOM_MODULE_CM									= "cm";
    public static String NOM_MODULE_TS									= "ts";
    
    //Label keys for modules' nomenclator
    public static int NOM_MODULE_OM_LABEL_KEY							= 1;
    public static int NOM_MODULE_DM_LABEL_KEY							= 2;
    public static int NOM_MODULE_CM_LABEL_KEY							= 5;
    public static int NOM_MODULE_TS_LABEL_KEY							= 6;
    
    //Modules names nomenclator
    public static String NOM_MODULE_OM_NAME								= "Organization Management";
    public static String NOM_MODULE_DM_NAME								= "Document Management";
    public static String NOM_MODULE_CM_NAME								= "Client Management";
    public static String NOM_MODULE_TS_NAME								= "Time Sheet";
    
    //OM Audit Event types nomenclator
    public static String[] AUDIT_EVENTS_OM 								= {"audit.event.organisation.update.type", "audit.event.organisation.add.type", "audit.event.organisation.enable.type", "audit.event.organisation.disable.type", "audit.event.organisation.delete.type", 
    																	   "audit.event.department.add.type", "audit.event.department.update.type", "audit.event.department.delete.type", 
    																	   "audit.event.person.add.type", "audit.event.person.update.type", "audit.event.person.enable.type", "audit.event.person.disable.type", "audit.event.person.delete.type", "audit.event.admin.add.type", 
    																	   "audit.event.ooo.update.type", "audit.event.ooo.add.type", "audit.event.ooo.delete.type", 
    																	   "audit.event.calendar.update.type", "audit.event.freeday.update.type", "audit.event.freeday.add.type", "audit.event.freeday.delete.type",
    																	   "audit.event.role.add.type", "audit.event.role.update.type", "audit.event.role.delete.type", 
    																	   "audit.event.permission.add.type", "audit.event.permission.update.type", "audit.event.permission.delete.type",
    																	   "audit.event.job.add.type", "audit.event.job.update.type", "audit.event.job.delete.type", "audit.event.job.enable.type", "audit.event.job.disable.type",
    																	   "audit.event.usergroup.add.type", "audit.event.usergroup.update.type", "audit.event.usergroup.delete.type"};
    
    //DM Audit Event types nomenclator
    public static String[] AUDIT_EVENTS_DM 								= {"audit.event.document.update.type", "audit.event.document.add.type", "audit.event.document.delete.type", "audit.event.document.content.accessed.type", "audit.event.document.lock.type", "audit.event.document.unlock.type",
    																	   "audit.event.category.delete.type", "audit.event.category.create.type", "audit.event.category.update.type", "audit.event.category.move.type", 
    																	   "audit.event.collection.zone.import.type", "audit.event.collection.zone.upload.type", "audit.event.collection.zone.delete.type"};
    
    //CM Audit Event types nomenclator
    public static String[] AUDIT_EVENTS_CM								= {"audit.event.client.add.type", "audit.event.client.update.type", "audit.event.client.delete.type", "audit.event.project.add.type", "audit.event.project.update.type", 
    																	   "audit.event.project.delete.type", "audit.event.project.finish.type", "audit.event.project.abort.type", "audit.event.project.activate.type"};
    
    //TS Audit Event types nomenclator
    public static String[] AUDIT_EVENTS_TS								= {"audit.event.record.add.type", "audit.event.record.update.type", "audit.event.record.delete.type", "audit.event.project.detail.update.type", "audit.event.activity.add.type", 
    																	   "audit.event.activity.update.type", "audit.event.activity.delete.type", "audit.event.cost.sheet.add.type", "audit.event.cost.sheet.update.type", "audit.event.cost.sheet.delete.type", 
    																	   "audit.event.currency.add.type", "audit.event.currency.update.type", "audit.event.currency.delete.type", "audit.event.exchange.add.type", "audit.event.exchange.update.type",
    																	   "audit.event.exchange.delete.type", "audit.event.person.detail.update.type", "audit.event.team.member.detail.update.type"};
       
    //AUDIT REPORT ORIENTATION nomenclator
    public static String[] AUDIT_REPORT_ORIENTATIONS					= {"landscape", "portrait"};
    
    //AUDIT REPORT FORMAT nomenclator
    public static String[] AUDIT_REPORT_FORMATS							= {"HTML", "PDF", "DOC", "XLS"};
    
    //Reports servlet key
    public static String AUDIT_REPORT_SERVLET 							= "AUDIT_REPORT_SERVLET";
    
	//AUDIT BEANS PROPERTIES
	public static final String AUDIT_DATE 							= "date";
	public static final String AUDIT_MESSAGE_RO						= "messageRO";
	public static final String AUDIT_MESSAGE_EN						= "messageEN";
	public static final String AUDIT_EVENT 							= "event";
	public static final String AUDIT_ORGANISATION_ID				= "organisationId";
	public static final String AUDIT_MODULE_ID						= "moduleId";
	public static final String AUDIT_FIRTSNAME						= "firstName";	
	public static final String AUDIT_LASTNAME						= "lastName";
	public static final String AUDIT_PERSON_ID						= "personId";

}
