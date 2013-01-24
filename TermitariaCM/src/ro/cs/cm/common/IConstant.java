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
package ro.cs.cm.common;

/**
 * 
 * @author matti_joona
 * @author Adelina
 *
 * Used for general constant of the system
 */
public interface IConstant {

	//general stuff
    String APP_NAME 													= "Clients Management";
    String APP_CONTEXT 													= "Termitaria/CM";
    String OM_APP_CONTEXT 													= "Termitaria/OM";
    String REDIRECT_URL 												= "/Termitaria/OM/SignOn.htm";
    String APP_VERSION 													= "1.1";
    String APP_RELEASE_DATE 											= "2010";
    String APP_RELEASE_YEAR 											= "2009";
    String APP_START_PAGE												= "Main.htm";
    int 	 MODULE_ID													= 5;
    
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
    
    //SESSION_PARAMETERS
	String SESS_ORGANISATION_ID											= "SESS_ORGANISATION_ID";
	String SESS_ORGANISATION_NAME										= "SESS_ORGANISATION_NAME";
	String SESS_MESSAGES												= "SESS_MESSAGES";
			 
	String SECURITY_NO_RIGHTS											= "security.norights";
    
    //////////////////////QUARTZ////////////////////////////////////////////////////////
	String SCHEDULER													= "SCHEDULER";
    String SCHEDULER_CONFIG_FILE_LOCATION								= "quartzSchedulerConfiguartionFileLocation";
    
    //Standard Theme code
	String STANDARD_THEME												= "standard";
	
	// Number of characters that fit in a line, for display messages,
    // if there are big words, without spaces
	Integer NR_CHARS_PER_LINE_MESSAGE 									= 64;	
	
	////////////////////////////CLASSIFIED LISTS - NOMENCLATOARE//////////////////////////////////////////
	//Label key for number of results per page nomenclator
	String NOM_RESULTS_PER_PAGE											= "RESULTS_PER_PAGE";
	
	//Label key for clients types nomenclator
	String NOM_CLIENT_TYPE												= "NOM_CLIENT_TYPE";
	
	//Label key for clients status nomenclator
	String NOM_CLIENT_STATUS											= "NOM_CLIENT_STATUS";
	
	//Client's types nomenclator
	byte NOM_CLIENT_TYPE_FIRM											= 1;//the client is a firm
	byte NOM_CLIENT_TYPE_PERSON											= 2;//the client is a person
	
	//Client's status nomenclator
	byte NOM_CLIENT_STATUS_ACTIVE										= 1;//the client is active
	byte NOM_CLIENT_STATUS_DELETED										= 2;//the client is deleted
	
	byte NOM_PERSON_STATUS_ACTIVATED 									= 1;
	byte NOM_PERSON_STATUS_DELETED										= 2;
	
	//Label keys for client's types nomenclator
	public String NOM_CLIENT_TYPE_PERSON_LABEL_KEY						= "client.type.person";
	public String NOM_CLIENT_TYPE_FIRM_LABEL_KEY						= "client.type.firm";
	
	//Label keys for client's status nomenclator
	public String NOM_CLIENT_STATUS_ACTIVE_LABEL_KEY					= "client.status.active";
	public String NOM_CLIENT_STATUS_DELETED_LABEL_KEY					= "client.status.deleted";
	
	//Nomenclator for TS person search option 
	byte NOM_TS_PERSON_SEARCH_IN_ORGANIZATION							= -1;
	byte NOM_TS_PERSON_SEARCH_ALL_PROJECTS								= 0;
	
	//Results per page
	 int[] RESULTS_PER_PAGE												= {5,10,50};
	
	//message view for info/errors
	 String FORM_VIEW_MESSAGES											= "Messages";
	
	//general messages key
	 String GENERAL_ERROR												= "generalError";
	
	 //Sort Directions
    byte ASCENDING 														= -1;
    byte DESCENDING														=  1;       
    
    //project's status nomenclator STATUS	
	byte NOM_PROJECT_STATUS_OPENED										= 1;
	byte NOM_PROJECT_STATUS_CLOSED										= 2;
	byte NOM_PROJECT_STATUS_ABORTED										= 3;
	byte NOM_PROJECT_STATUS_DELETED										= 4;
	
	
	//labels keys for project's nomenclator for status
	String NOM_PROJECT_STATUS_OPENED_LABEL_KEY	  		 				= "project.status.activated";	
	String NOM_PROJECT_STATUS_CLOSED_LABEL_KEY	  		 				= "project.status.finished";	
	String NOM_PROJECT_STATUS_ABORTED_LABEL_KEY							= "project.status.aborted";
	String NOM_PROJECT_STATUS											= "PROJECT_STATUS";
	
	// names of the serialization files
	String FILE_NAME_SERIALIZATION_FOR_DELETE_PROJECT_DETIALS 			= "DeletePrjSel.ser"; 
	String FILE_NAME_SERIALIZATION_FOR_FINISH_PROJECT_DETIALS			= "FinishPrjSel.ser";  
	String FILE_NAME_SERIALIZATION_FOR_ABORT_PROJECT_DETIALS			= "AbortPrjSel.ser";  
	String FILE_NAME_SERIALIZATION_FOR_OPEN_PROJECT_DETIALS				= "OpenPrjSel.ser";  
	String FILE_NAME_SERIALIZATION_FOR_DELETE_MEMBER_DETIALS			= "DeleteMemberSel.ser";  
	
	//project team's status nomenclator STATUS	
	byte NOM_PROJECT_TEAM_STATUS_ACTIVATED								= 1;
	byte NOM_PROJECT_TEAM_STATUS_DELETED								= 2;							
	
	//labels keys for project team's nomenclator for status
	String NOM_PROJECT_TEAM_STATUS_ACTIVATED_LABEL_KEY	  			 	= "project.team.activated";	
	String NOM_PROJECT_TEAM_STATUS_DELETED_LABEL_KEY	  			 	= "project.team.deleted";	
	String NOM_PROJECT_TEAM_STATUS								 		= "PROJECT_TEAM_STATUS";
    
	//team member's status nomenclator STATUS	
	byte NOM_TEAM_MEMBER_STATUS_DISABLED								= 0;
	byte NOM_TEAM_MEMBER_STATUS_ACTIVATED								= 1;
	byte NOM_TEAM_MEMBER_STATUS_DELETED									= 2;		
	
	//labels keys for project team member's nomenclator for status
	String NOM_TEAM_MEMBER_STATUS_ACTIVATED_LABEL_KEY	  				= "team.member.activated";	
	String NOM_TEAM_MEMBER_STATUS_DELETED_LABEL_KEY	  		 	 		= "team.member.deleted";	
	String NOM_TEAM_MEMBER_STATUS										= "TEAM_MEMBER_STATUS";
	
	//client sort param nomenclator
	String NOM_CLIENT_SORT_PARAM_NAME								 	= "name";
	String NOM_CLIENT_SORT_PARAM_TYPE							 		= "type";
	
	//-------------------------------------------------- AUDIT RELATED START ----------------------------------------------------------
	
    //AUDIT Web Service Clients config parameters
    String AUDIT_WS_SERVER_NAMESPACE									= "http://localhost:8080/Audit/services/schemas/messages";
    
	//AUDIT BEAN PROPERTIES
	String auditDate 													= "date";
	String auditPersonId												= "personId";
	String auditMessageRO												= "messageRO";
	String auditMessageEN												= "messageEN";
	String auditEvent 													= "event";
	String auditOrganisationId											= "organisationId";	
	String auditModuleId												= "moduleId";
	String auditFirstName												= "firstName";
	String auditLastName												= "lastName";
	
	//AUDIT EVENT TYPES
	String AUDIT_EVENT_CLIENT_ADD_TYPE									= "audit.event.client.add.type";
	String AUDIT_EVENT_CLIENT_UPDATE_TYPE								= "audit.event.client.update.type";
	String AUDIT_EVENT_CLIENT_DELETE_TYPE								= "audit.event.client.delete.type";
	String AUDIT_EVENT_PROJECT_ADD_TYPE									= "audit.event.project.add.type";
	String AUDIT_EVENT_PROJECT_UPDATE_TYPE								= "audit.event.project.update.type";
	String AUDIT_EVENT_PROJECT_DELETE_TYPE								= "audit.event.project.delete.type";
	String AUDIT_EVENT_PROJECT_FINISH_TYPE								= "audit.event.project.finish.type";
	String AUDIT_EVENT_PROJECT_ABORT_TYPE								= "audit.event.project.abort.type";
	String AUDIT_EVENT_PROJECT_ACTIVATE_TYPE							= "audit.event.project.activate.type";
	
	//AUDIT EVENT MESSAGES	
	String AUDIT_EVENT_CLIENT_ADD_MESSAGE								= "audit.event.client.add.message";
	String AUDIT_EVENT_CLIENT_UPDATE_MESSAGE							= "audit.event.client.update.message";
	String AUDIT_EVENT_CLIENT_DELETE_MESSAGE							= "audit.event.client.delete.message";
	String AUDIT_EVENT_PROJECT_ADD_MESSAGE								= "audit.event.project.add.message";
	String AUDIT_EVENT_PROJECT_UPDATE_MESSAGE							= "audit.event.project.update.message";
	String AUDIT_EVENT_PROJECT_DELETE_MESSAGE							= "audit.event.project.delete.message";
	String AUDIT_EVENT_PROJECT_FINISH_MESSAGE							= "audit.event.project.finish.message";
	String AUDIT_EVENT_PROJECT_ABORT_MESSAGE							= "audit.event.project.abort.message";
	String AUDIT_EVENT_PROJECT_ACTIVATE_MESSAGE							= "audit.event.project.activate.message";
	//-------------------------------------------------- AUDIT RELATED END ------------------------------------------------------------
    
}
