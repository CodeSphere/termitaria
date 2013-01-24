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
package ro.cs.cm.exception;

/**
 * @author alu
 *
 */
public interface ICodeException {
	
	String MODULE_NAME										= "CM";
	
	String SECURITY_AUTHENTICATE							= MODULE_NAME.concat(":SECURITY:0");
	
	//BLUser
	String USER_GET_BY_SECURITY_TOKEN						= MODULE_NAME.concat(":USER:0");
	String USER_GET_BY_ORGANIZATION_ID						= MODULE_NAME.concat(":USER:1");
	
	// BLPERSON
	String PERSON_GET										= MODULE_NAME.concat(":PERSON:0");
	String PERSON_GET_FROM_SEARCH							= MODULE_NAME.concat(":PERSON:1");
	
	// BLPROJECT
	String PROJECT_SEARCH									= MODULE_NAME.concat(":PROJECT:0"); 	
	String PROJECT_ADD_WITH_ALL								= MODULE_NAME.concat(":PROJECT:1");	
	String PROJECT_UPDATE_WITH_ALL							= MODULE_NAME.concat(":PROJECT:2");	
	String PROJECT_GET_WITH_ALL								= MODULE_NAME.concat(":PROJECT:3");	
	String PROJECT_DELETE_ALL								= MODULE_NAME.concat(":PROJECT:4");	
	String PROJECT_GET_WITH_CLIENT							= MODULE_NAME.concat(":PROJECT:5");
	String PROJECT_GET_CLIENTS								= MODULE_NAME.concat(":PROJECT:6");
	String PROJECT_GET_WITH_MANAGER							= MODULE_NAME.concat(":PROJECT:7");
	String PROJECT_FINISH									= MODULE_NAME.concat(":PROJECT:8");
	String PROJECT_UPDATE_STATUS							= MODULE_NAME.concat(":PROJECT:9");
	String PROJECT_GET_BY_CLIENT_ID							= MODULE_NAME.concat(":PROJECT:10");
	String PROJECT_GET_FOR_CLIENT							= MODULE_NAME.concat(":PROJECT:11");
	String PROJECT_ACTIVATE									= MODULE_NAME.concat(":PROJECT:12");
	String PROJECT_ABORT									= MODULE_NAME.concat(":PROJECT:13");
	String PROJECT_GET_ALL									= MODULE_NAME.concat(":PROJECT:14");
	String PROJECT_GET_BY_MANAGER							= MODULE_NAME.concat(":PROJECT:15");
	String PROJECT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON  = MODULE_NAME.concat(":PROJECT:16");
	String PROJECT_GET_BY_TEAM_MEMBER						= MODULE_NAME.concat(":PROJECT:17");
	String PROJECT_GET_WITH_PROJECT_TEAM_BY_PROJECT_ID		= MODULE_NAME.concat(":PROJECT:18");
	String PROJECT_GET_BY_PROJECT_IDS						= MODULE_NAME.concat(":PROJECT:19");
	String PROJECT_GET_WITH_STATUS							= MODULE_NAME.concat(":PROJECT:20");
	String PROJECT_GET_BY_PERSON							= MODULE_NAME.concat(":PROJECT:21");
	
	// BLTEAMMEMBER
	String TEAMMEMBER_GET									= MODULE_NAME.concat(":PROJECT:0");
	String TEAMMEMBER_ADD									= MODULE_NAME.concat(":PROJECT:1");
	String TEAMMEMBER_GET_NEW_FOR_PROJECT_TEAM				= MODULE_NAME.concat(":PROJECT:2");
	String TEAMMEMBER_DELETE								= MODULE_NAME.concat(":PROJECT:3");
	String TEAMMEMBER_GET_BY_ID								= MODULE_NAME.concat(":PROJECT:4");
	String TEAMMEMBER_UPDATE								= MODULE_NAME.concat(":PROJECT:5");
	String TEAMMEMBER_GET_FROM_SEARCH_SIMPLE				= MODULE_NAME.concat(":PROJECT:6");
	String TEAMMEMBER_GET_BY_IDS							= MODULE_NAME.concat(":PROJECT:7");
	String TEAMMEMBER_CHANGE_STATUS_TO_DELETE				= MODULE_NAME.concat(":PROJECT:8");
	String TEAMMEMBER_GET_EXTERNAL_FOR_PROJECT				= MODULE_NAME.concat(":PROJECT:9");
	String TEAMEMBER_GET_SIMPLE_BY_MEMBER_ID				= MODULE_NAME.concat(":PROJECT:10");
	String TEAMMEMBER_GET_FROM_SEARCH_SIMPLE_WITH_PAGINATION= MODULE_NAME.concat(":PROJECT:11");
	
	// BLPROJECTTEAM	
	String PROJECTTEAM_GET								    = MODULE_NAME.concat(":PROJECT:0"); 		
	String PROJECTTEAM_ADD									= MODULE_NAME.concat(":PROJECT:1"); 	
	String PROJECTTEAM_UPDATE                               = MODULE_NAME.concat(":PROJECT:2"); 	
	String PROJECTTEAM_FOR_PROJECT							= MODULE_NAME.concat(":PROJECT:3");
	String PROJECTTEAM_DELETE								= MODULE_NAME.concat(":PROJECT:4");
	
	//BLClient
	String CLIENT_SEARCH									= MODULE_NAME.concat(":CLIENT:0");
	String CLIENT_DELETE									= MODULE_NAME.concat(":CLIENT:1");
	String CLIENT_GET										= MODULE_NAME.concat(":CLIENT:2");
	String CLIENT_UPDATE									= MODULE_NAME.concat(":CLIENT:3");
	String CLIENT_ADD										= MODULE_NAME.concat(":CLIENT:4");
	String CLIENT_GET_BY_ORGANIZATION_ID					= MODULE_NAME.concat(":CLIENT:5");
	
	//BLAudit
	String AUDIT_ADD										= MODULE_NAME.concat(":AUDIT:0");
		
	//EndPoint Exception
	String ENDPOINT_GET_ALL_PROJECTS					 	= MODULE_NAME.concat(":ENDPOINT:0");
	String ENDPOINT_GET_PROJECTS_BY_MANAGER					= MODULE_NAME.concat(":ENDPOINT:1");
	String ENDPOINT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON = MODULE_NAME.concat(":ENDPOINT:2");
	String ENDPOINT_GET_TEAM_MEMBER_FROM_SEARCH				= MODULE_NAME.concat(":ENDPOINT:3");
	String ENDPOINT_GET_CLIENTS_FOR_PROJECT					= MODULE_NAME.concat(":ENDPOINT:4");
	String ENDPOINT_GET_PROJECT								= MODULE_NAME.concat(":ENDPOINT:5");
	String ENDPOINT_GET_TEAM_MEMBER_BY_MEMBER_ID			= MODULE_NAME.concat(":ENDPOINT:6");
	String ENDPOINT_GET_BY_PROJECT_IDS						= MODULE_NAME.concat(":ENDPOINT:7");
	String ENDPOINT_GET_EXTERNAL_TEAM_MEMBER_BY_PROJECT_IDS	= MODULE_NAME.concat(":ENDPOINT:8");
}	
