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
package ro.cs.om.common;



/**
 * Used for general constant of the system
 *
 * @author matti_joona
 */
public interface IConstant {

	//general stuff
    String APP_NAME 							= "OM";
    String APP_CONTEXT 							= "Termitaria/OM";
    String REDIRECT_URL 						= "/Termitaria/OM/SignOn.htm";
    String APP_VERSION 							= "1.7";
    String APP_RELEASE_DATE 					= "2010";
    String APP_RELEASE_YEAR 					= "2009";
    String APP_START_PAGE 						= "Main.htm";
    String PERMISSION_CONSTANT 					= "PERMISSION_CONSTANT";
    String EXCEPTION_CONSTANT					= "EXCEPTION_CONSTANT";
    String BACK_CONSTANT						= "BACK_CONSTANT";
    int    MODULE_ID							= 1;
    int	   AUDIT_MODULE_ID						= 3;
    Integer TS_MODULE_ID						= 6;
    int    MODULE_ID_FAKE						= -999;
    int    ORGANIZATION_ID_FAKE					= -999;
    int    ORGANISATION_ID_NOT_PERSISTENT		=	-1;
    String OM_ADMIN								= "OM_ADMIN";
    String OM_USER								= "OM_USER";  
    String TS_USER_ALL							= "TS_USER_ALL";
    String APP_CONTEXT_MESSAGE_SOURCE_BEAN		= "messageSource";
    String APP_CONTEXT_ADMIN_ORGANISATION_ID    = "APP_CONTEXT_ADMIN_ORGANISATION_ID";    
    
    //Default module that will be put in every organisation 
    //when u create it
    String OM_MODULE							= "Organization Management";
    
	//http inspector and interceptor constants
    String HTTP_INSPECTOR_SPECIAL_ATRIBUTE_REQUEST 	= "AFISEAZA_ATRIBUTE_SPECIALE_REQUEST";
	String HTTP_INSPECTOR_ATRIBUTE_REQUEST 		= "AFISEAZA_ATRIBUTE_REQUEST";
	String HTTP_INSPECTOR_PARAMETRII_REQUEST 	= "AFISEAZA_PARAMETRII_REQUEST";
	String HTTP_INSPECTOR_ATRIBUTE_SESSION 		= "AFISEAZA_ATRIBUTE_SESIUNE";
	String HTTP_INTERCEPTOR_ON 					= "HTTP_INTERCEPTOR_ON";

	//errors and informations labels
    String REQ_ATTR_ERRORS 						= "ERRORS";
    String REQ_ATTR_MSGS 						= "MESSAGES";
 
    //request commands
    String CMD_UPDATE 							= "UPDATE";
    String CMD_ADD 								= "ADD";
	String CMD_GET 								= "GET";
	String CMD_DELETE 							= "DELETE";
	String CMD_NEW 								= "NEW";
	String CMD_SAVE 							= "SAVE";
	String REQ_ACTION 							= "ACTION";
	
	//general attributes	
	String ACCEPTED_LANGUAGES					= "ACCEPTED_LANGUAGES";
	String LANGUAGE_ATTRIBUTE					= "siteLanguage";
	
	//general Session Attributes
	String SESS_ORGANISATION_ID					= "SESS_ORGANISATION_ID";
	String SESS_ORGANISATION_NAME				= "SESS_ORGANISATION_NAME";
	String SESS_ORGANISATION_STATUS 			= "SESS_ORGANISATION_STATUS";
	String SESS_ORGANISATION_ADDRESS			= "SESS_ORGANISATION_ADDRESS";
	
	//all organizations trees
	String ORGANISATION_TREES					= "ORGANISATION_TREES";
	
	//map specifying the organisations that have the audit module
	String HAS_AUDIT_CONTEXT_MAP				= "HAS_AUDIT_CONTEXT_MAP";
	
	//Mail messages keys
	String MAIL_SEND_PASSWORD_TO_ADMIN_SUBJECT	= "mail.admin.sendPassword.subject";
	String MAIL_SEND_PASSWORD_TO_ADMIN_MESSAGE	= "mail.admin.sendPassword.message";
	String MAIL_SEND_PASSWORD_TO_USER_SUBJECT	= "mail.user.sendPassword.subject";
	String MAIL_SEND_PASSWORD_TO_USER_MESSAGE	= "mail.user.sendPassword.message";
	
	//message view for info/errors
	String FORM_VIEW_MESSAGES					= "Messages";
	
	//general messages key
	String GENERAL_ERROR						= "generalError";
	
	//department's nomenclator
	byte  	NOM_DEPARTMENT_ACTIVE				= 1; // active department (default)	
	byte  	NOM_DEPARTMENT_FAKE					= 2; // fake department - one/organization	
	String 	NOM_DEPARTMENT_SATUS				= "DEPARTMENT_STATUS";
	
	//person's nomenclator
	int NOM_PERSON_INACTIVE						= 0; // inactive person
	int NOM_PERSON_ACTIVE						= 1; // active person (default)
	
	//organisation's type nomenclator
	byte NOM_ORGANISATION_TYPE_COMPANY         		= 1; // the organization is a company	
	byte NOM_ORGANISATION_TYPE_GROUP_COMPANIES     	= 2; // the organization is a group of companies
	byte NOM_ORGANISATION_TYPE_HQ              		= 3; // the organization is a head quarter
	byte NOM_ORGANISATION_TYPE_BRANCH          		= 4; // the organization is a branch	
	
	// organisation's juridic type nomenclator
	// the comments for the juridic types for the organisations are in romanian
	byte NOM_ORGANISATION_JURIDIC_TYPE_SNC			= 1; // societate in nume colectiv (SNC)
	byte NOM_ORGANISATION_JURIDIC_TYPE_SCS			= 2; // societate in comandita simpla (SCS)
	byte NOM_ORGANISATION_JURIDIC_TYPE_SA			= 3; // societate pe actiuni (SA)
	byte NOM_ORGANISATION_JURIDIC_TYPE_SCA			= 4; // societate in comandita pe actiuni (SCA)
	byte NOM_ORGANISATION_JURIDIC_TYPE_SRL			= 5; // societate cu raspundere limitata (SRL)
	
	
	// organisation's financial type nomenclator
	// the comments for the financial types for the organisations are in romanian
	byte NOM_ORGANISATION_TYPE_FINANCIAL_VERY_BIG	= 1; // intreprindere foarte mare
	byte NOM_ORGANISATION_TYPE_FINANCIAL_BIG		= 2; // intreprindere mare
	byte NOM_ORGANISATION_TYPE_FINANCIAL_MEDIUM		= 3; // intreprindere mijlocie
	byte NOM_ORGANISATION_TYPE_FINANCIAL_SMALL		= 4; // intreprindere mica
	byte NOM_ORGANISATION_TYPE_FINANCIAL_MICRO		= 5; // microintreprindere
	
	// labels keys for organisation's nomenclator
	String NOM_ORGANISATION_TYPE_COMPANY_LABEL_KEY	   			= "organisation.type.company";
	String NOM_ORGANISATION_TYPE_GROUP_COMPANIES_LABEL_KEY	   	= "organisation.type.group.companies";
	String NOM_ORGANISATION_TYPE_HQ_LABEL_KEY	   				= "organisation.type.hq";
	String NOM_ORGANISATION_TYPE_BRANCH_LABEL_KEY	   			= "organisation.type.branch";
	String NOM_ORGANISATION_TYPE								= "ORGANISATION_TYPE";
	String NOM_GROUP_COMPANIES_TYPE								= "GROUP_COMPANIES_TYPE";
	
	//Organisation tree nodes types nomenclator
	byte NOM_TREE_NODE_TYPE_COMPANY         		= 1; // the node is a company	
	byte NOM_TREE_NODE_TYPE_GROUP_COMPANIES     	= 2; // the node is a group of companies
	byte NOM_TREE_NODE_TYPE_HQ              		= 3; // the node is a head quarter
	byte NOM_TREE_NODE_TYPE_BRANCH          		= 4; // the node is a branch	
	byte NOM_TREE_NODE_TYPE_PERSON          		= 5; // the node is a person
	byte NOM_TREE_NODE_TYPE_DEPARTMENT          	= 6; // the node is a department
	
	// setting's nomenclators
	
	String NOM_SETTING_THEME					= "SETTING_THEME";
	
	//job's status
	byte NOM_JOB_INACTIVE						= 0;
	byte NOM_JOB_ACTIVE							= 1;
		
	// person's sex
	Character NOM_PERSON_SEX_F						= 'F';
	Character NOM_PERSON_SEX_M 						= 'M';
	
	// organisations's status
	byte NOM_ORGANISATION_INACTIVE				= 0;	// inactive organisation 
	byte NOM_ORGANISATION_ACTIVE 				= 1;	// active organisation (default)
	
	//User group status
	int NOM_USER_GROUP_DEFAULT					= 0;	//the default user group assigned to all users
	int NOM_USER_GROUP_NORMAL					= 1;	//normal user group
	
	//Default user group name
	String DEFAULT_USER_GROUP_NAME				= "All Users";
	
	byte PERSON_PICTURE_WIDTH					= 100; //pixels
	byte PERSON_PICTURE_HEIGHT					= 120; //pixels
	
	byte NOM_PERSON_STATUS_DISABLED				= 0;
	byte NOM_PERSON_STATUS_ACTIVATED 			= 1;
	byte NOM_PERSON_STATUS_DELETED				= 2;
	
	short LOGO_WIDTH							= 145; //pixels
	short LOGO_HEIGHT							= 55; //pixels

	//DME WORKSPACE
	String DME_WORKSPACE_PREFIX					= "DMC_Workspace_";
	
	//Setting status for default and normal
	byte SETTING_STATUS_DEFAULT					= 1;
	byte SETTING_STATUS_NORMAL					= 2;
	
	//Role status for default and normal
	byte ROLE_STATUS_ADMIN_IT					= 1;
	byte ROLE_STATUS_DEFAULT					= 2;
	byte ROLE_STATUS_NORMAL						= 3;
	
	// Number of characters that fit in a line, for display messages,
    // if there are big words, without spaces
	public static final Integer NR_CHARS_PER_LINE_MESSAGE = 64;		
	
	//Standard Theme code
	String STANDARD_THEME						= "standard";
	
	//Theme parameter name in setting
	String SETTING_THEME						= "theme";
	
	//Results per page
	int[] RESULTS_PER_PAGE						= {5,10,50};
	
	////////////////////////////CLASSIFIED LISTS - NOMENCLATOARE//////////////////////////////////////////
	String NOM_MODULES							= "MODULES";
	String NOM_RESULTS_PER_PAGE					= "RESULTS_PER_PAGE";
	
	String AUTHORITY_SUPER						= "SUPER";
	
	////////////////////// QUARTZ////////////////////////////////////////////////////////
	String SCHEDULER							= "SCHEDULER";
    String SCHEDULER_CONFIG_FILE_LOCATION		= "quartzSchedulerConfiguartionFileLocation";
    
    String SECURITY_NO_RIGHTS					= "security.norights";
    
    Integer OOO_TEXT_AREA_SIZE								= 3;
    Integer OOO_TEXT_AREA_ROW_SIZE							= 40;
    
    Integer FREEDAY_TEXT_AREA_SIZE							= 3;
    Integer FREEDAY_TEXT_AREA_ROW_SIZE						= 65;
    
    Integer ROLE_TEXT_AREA_SIZE								= 3;
    Integer ROLE_TEXT_AREA_ROW_SIZE							= 40;
    
    Integer PERMISSION_TEXT_AREA_SIZE						= 3;
    Integer PERMISSION_TEXT_AREA_ROW_SIZE					= 40;
    
    Integer JOB_TEXT_AREA_SIZE								= 3;
    Integer JOB_TEXT_AREA_ROW_SIZE							= 45;
    
    Integer USERGROUP_TEXT_AREA_SIZE						= 3;
    Integer USERGROUP_TEXT_AREA_ROW_SIZE					= 40;
    
    //Person status
    byte SEARCH_ALL_BRANCH_PERSONS 				= -1;
    byte PERSON_NEW								= -1;       
    
    //Actions for persons to display
    boolean PERSON_ALL_ACTIONS						=  true;
    boolean PERSON_NO_ACTION						=  false;
    
    //Sort Directions
    byte ASCENDING 								= -1;
    byte DESCENDING								=  1;
        
    
    //Security configuration parameters
    String SECURITY_TOKEN_AVAILABILITY			= "security.token.availabilty";
    String SECURITY_TOKEN_REPOSITORY			= "SECURITY_TOKEN_REPOSITORY";
    
    //OM Web Service Clients config parameters
    String AUDIT_WS_SERVER_NAMESPACE			= "http://localhost:8080/Audit/services/schemas/messages";
    
    //AUDIT EVENTS TYPES
    String AUDIT_EVENT_ORGANISATION_UPDATE_TYPE		= "audit.event.organisation.update.type";
    String AUDIT_EVENT_ORGANISATION_ADD_TYPE		= "audit.event.organisation.add.type";
    String AUDIT_EVENT_ORGANISATION_ENABLE_TYPE		= "audit.event.organisation.enable.type";
    String AUDIT_EVENT_ORGANISATION_DISABLE_TYPE	= "audit.event.organisation.disable.type";
    String AUDIT_EVENT_ORGANISATION_DELETE_TYPE		= "audit.event.organisation.delete.type";
    
    String AUDIT_EVENT_DEPARTMENT_ADD_TYPE			= "audit.event.department.add.type";
    String AUDIT_EVENT_DEPARTMENT_UPDATE_TYPE		= "audit.event.department.update.type";
    String AUDIT_EVENT_DEPARTMENT_DELETE_TYPE		= "audit.event.department.delete.type";
    
    String AUDIT_EVENT_PERSON_ADD_TYPE				= "audit.event.person.add.type";
    String AUDIT_EVENT_PERSON_UPDATE_TYPE			= "audit.event.person.update.type";
    String AUDIT_EVENT_PERSON_ENABLE_TYPE			= "audit.event.person.enable.type";
    String AUDIT_EVENT_PERSON_DISABLE_TYPE			= "audit.event.person.disable.type";
    String AUDIT_EVENT_PERSON_DELETE_TYPE			= "audit.event.person.delete.type";
    String AUDIT_EVENT_ADMIN_ADD_TYPE				= "audit.event.admin.add.type";
    
    String AUDIT_EVENT_OOO_UPDATE_TYPE				= "audit.event.ooo.update.type";
    String AUDIT_EVENT_OOO_ADD_TYPE					= "audit.event.ooo.add.type";
    String AUDIT_EVENT_OOO_DELETE_TYPE				= "audit.event.ooo.delete.type";
    
    String AUDIT_EVENT_CALENDAR_UPDATE_TYPE			= "audit.event.calendar.update.type";
    String AUDIT_EVENT_FREEDAY_UPDATE_TYPE			= "audit.event.freeday.update.type";
    String AUDIT_EVENT_FREEDAY_ADD_TYPE				= "audit.event.freeday.add.type";
    String AUDIT_EVENT_FREEDAY_DELETE_TYPE			= "audit.event.freeday.delete.type";
    
    String AUDIT_EVENT_ROLE_ADD_TYPE				= "audit.event.role.add.type";
    String AUDIT_EVENT_ROLE_UPDATE_TYPE				= "audit.event.role.update.type";
    String AUDIT_EVENT_ROLE_DELETE_TYPE				= "audit.event.role.delete.type";
    
    String AUDIT_EVENT_PERMISSION_ADD_TYPE			= "audit.event.permission.add.type";
    String AUDIT_EVENT_PERMISSION_UPDATE_TYPE		= "audit.event.permission.update.type";
    String AUDIT_EVENT_PERMISSION_DELETE_TYPE		= "audit.event.permission.delete.type";
    
    String AUDIT_EVENT_JOB_ADD_TYPE					= "audit.event.job.add.type";
    String AUDIT_EVENT_JOB_UPDATE_TYPE				= "audit.event.job.update.type";
    String AUDIT_EVENT_JOB_DELETE_TYPE				= "audit.event.job.delete.type";
    String AUDIT_EVENT_JOB_ENABLE_TYPE				= "audit.event.job.enable.type";
    String AUDIT_EVENT_JOB_DISABLE_TYPE				= "audit.event.job.disable.type";
    
    String AUDIT_EVENT_USERGROUP_ADD_TYPE			= "audit.event.usergroup.add.type";
    String AUDIT_EVENT_USERGROUP_UPDATE_TYPE		= "audit.event.usergroup.update.type";
    String AUDIT_EVENT_USERGROUP_DELETE_TYPE		= "audit.event.usergroup.delete.type";
    
    //AUDIT MESSAGE KEYS
    String AUDIT_EVENT_ORGANISATION_UPDATE_MESSAGE	= "audit.event.organisation.update.message";
    String AUDIT_EVENT_ORGANISATION_ADD_MESSAGE		= "audit.event.organisation.add.message";
    String AUDIT_EVENT_ORGANISATION_ENABLE_MESSAGE	= "audit.event.organisation.enable.message";
    String AUDIT_EVENT_ORGANISATION_DISABLE_MESSAGE	= "audit.event.organisation.disable.message";
    String AUDIT_EVENT_ORGANISATION_DELETE_MESSAGE	= "audit.event.organisation.delete.message";
    
    String AUDIT_EVENT_DEPARTMENT_ADD_MESSAGE		= "audit.event.department.add.message";
    String AUDIT_EVENT_DEPARTMENT_UPDATE_MESSAGE	= "audit.event.department.update.message";
    String AUDIT_EVENT_DEPARTMENT_DELETE_MESSAGE	= "audit.event.department.delete.message";
    
    String AUDIT_EVENT_PERSON_ADD_MESSAGE			= "audit.event.person.add.message";
    String AUDIT_EVENT_PERSON_UPDATE_MESSAGE		= "audit.event.person.update.message";
    String AUDIT_EVENT_PERSON_ENABLE_MESSAGE		= "audit.event.person.enable.message";
    String AUDIT_EVENT_PERSON_DISABLE_MESSAGE		= "audit.event.person.disable.message";
    String AUDIT_EVENT_PERSON_DELETE_MESSAGE		= "audit.event.person.delete.message";
    String AUDIT_EVENT_ADMIN_ADD_MESSAGE			= "audit.event.admin.add.message";
    
    String AUDIT_EVENT_OOO_UPDATE_MESSAGE			= "audit.event.ooo.update.message";
    String AUDIT_EVENT_OOO_ADD_MESSAGE				= "audit.event.ooo.add.message";
    String AUDIT_EVENT_OOO_DELETE_MESSAGE			= "audit.event.ooo.delete.message";
    
    String AUDIT_EVENT_CALENDAR_UPDATE_MESSAGE		= "audit.event.calendar.update.message";
    String AUDIT_EVENT_FREEDAY_UPDATE_MESSAGE		= "audit.event.freeday.update.message";
    String AUDIT_EVENT_FREEDAY_ADD_MESSAGE			= "audit.event.freeday.add.message";
    String AUDIT_EVENT_FREEDAY_DELETE_MESSAGE		= "audit.event.freeday.delete.message";
    
    String AUDIT_EVENT_ROLE_ADD_MESSAGE				= "audit.event.role.add.message";
    String AUDIT_EVENT_ROLE_UPDATE_MESSAGE			= "audit.event.role.update.message";
    String AUDIT_EVENT_ROLE_DELETE_MESSAGE			= "audit.event.role.delete.message";
    
    String AUDIT_EVENT_PERMISSION_ADD_MESSAGE		= "audit.event.permission.add.message";
    String AUDIT_EVENT_PERMISSION_UPDATE_MESSAGE	= "audit.event.permission.update.message";
    String AUDIT_EVENT_PERMISSION_DELETE_MESSAGE	= "audit.event.permission.delete.message";
    
    String AUDIT_EVENT_JOB_ADD_MESSAGE				= "audit.event.job.add.message";
    String AUDIT_EVENT_JOB_UPDATE_MESSAGE			= "audit.event.job.update.message";
    String AUDIT_EVENT_JOB_DELETE_MESSAGE			= "audit.event.job.delete.message";
    String AUDIT_EVENT_JOB_ENABLE_MESSAGE			= "audit.event.job.enable.message";
    String AUDIT_EVENT_JOB_DISABLE_MESSAGE			= "audit.event.job.disable.message";
    
    String AUDIT_EVENT_USERGROUP_UPDATE_MESSAGE		= "audit.event.usergroup.update.message";
    String AUDIT_EVENT_USERGROUP_ADD_MESSAGE		= "audit.event.usergroup.add.message";
    String AUDIT_EVENT_USERGROUP_DELETE_MESSAGE		= "audit.event.usergroup.delete.message";
}
