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
package ro.cs.om.exception;

/**
 * Interface containing codes for all our Application exceptions
 * 
 * @author dd
 *
 */
public interface ICodeException {

	String MODULE_NAME										= "OM";

	//BLSecurity exception's code
	String SECURITY_AUTHENTIFY_EC							= MODULE_NAME.concat(":SECURITY:0");
	String SECURITY_AUTHORISATION_EC						= MODULE_NAME.concat(":SECURITY:1");
	
	//BLOrganisation exception's code
	String ORG_ADD											= MODULE_NAME.concat(":ORGANISATION:0"); 
	String ORG_DEL											= MODULE_NAME.concat(":ORGANISATION:1");
	String ORG_UPDATE										= MODULE_NAME.concat(":ORGANISATION:2");
	String ORG_GET											= MODULE_NAME.concat(":ORGANISATION:3");
	String ORG_GETDEPTS										= MODULE_NAME.concat(":ORGANISATION:4");
	String ORG_GETNODEPTS									= MODULE_NAME.concat(":ORGANISATION:5");
	String ORG_GETROLES										= MODULE_NAME.concat(":ORGANISATION:6");
	String ORG_GETPERSONS									= MODULE_NAME.concat(":ORGANISATION:7");
	String ORG_GETSETTINGS									= MODULE_NAME.concat(":ORGANISATION:8");
	String ORG_GET_DEFAULT_DEPT								= MODULE_NAME.concat(":ORGANISATION:9");
	String ORG_LIST											= MODULE_NAME.concat(":ORGANISATION:10");
	String ORG_GET_MODULES									= MODULE_NAME.concat(":ORGANISATION:11");
	String ORG_GET_FOR_NOM									= MODULE_NAME.concat(":ORGANISATION:12");
	String ORG_GET_FOR_NOM_WITHOUT_CALENDAR					= MODULE_NAME.concat(":ORGANISATION:13");
	String ORG_SEARCH										= MODULE_NAME.concat(":ORGANISATION:14");
	String ORG_WITH_MODULES									= MODULE_NAME.concat(":ORGANISATION:15");
	String ORG_GET_BY_NAME									= MODULE_NAME.concat(":ORGANISATION:16");
	String ORG_UPDATE_ORG_TREE_THREAD						= MODULE_NAME.concat(":ORGANISATION:17");
	String ORG_GET_BY_PARENTID_FOR_TREE						= MODULE_NAME.concat(":ORGANISATION:18");
	String ORG_GET_BY_PARENT_ID								= MODULE_NAME.concat(":ORGANISATION:19");
	String ORG_GET_BRANCH_IDS								= MODULE_NAME.concat(":ORGANISATION:20");
	String ORG_GET_BY_MODULE_ID								= MODULE_NAME.concat(":ORGANISATION:21");
	String ORG_DELETE_DEFAULT_USER_GROUP					= MODULE_NAME.concat(":ORGANISATION:22");
	String ORG_GET_WITH_MODULE_FOR_NOM						= MODULE_NAME.concat(":ORGANISATION:23");
	String ORG_HAS_AUDIT_MODULE								= MODULE_NAME.concat(":ORGANISATION:24");
	String ORG_DEL_OTHER_MODULE								= MODULE_NAME.concat(":ORGANISATION:25");

	//BLOrganigram exception's code
	String ORGANIGRAM_GET_ROOTS								= MODULE_NAME.concat(":ORGANIGRAM:0");
	String ORGANIGRAM_GET_SUBDEPARTMENTS					= MODULE_NAME.concat(":ORGANIGRAM:1");
	String ORGANIGRAM_GET_DEPARTMENTSMANAGER				= MODULE_NAME.concat(":ORGANIGRAM:2");
	String ORGANIGRAM_GET_INFERIORS							= MODULE_NAME.concat(":ORGANIGRAM:3");
	String ORGANIGRAM_GET_SUPERIORS							= MODULE_NAME.concat(":ORGANIGRAM:4");
	String ORGANIGRAM_GET_STRUCT_TREE						= MODULE_NAME.concat(":ORGANIGRAM:5");
	String ORGANIGRAM_GET_DEPT_TREE							= MODULE_NAME.concat(":ORGANIGRAM:6");
	String ORGANIGRAM_GET_NODES_CHILDREN					= MODULE_NAME.concat(":ORGANIGRAM:7");
	
	//BLPerson exception's code
	String PERSON_ADD										= MODULE_NAME.concat(":PERSON:0"); 
	String PERSON_DEL										= MODULE_NAME.concat(":PERSON:1");
	String PERSON_UPDATE									= MODULE_NAME.concat(":PERSON:2");
	String PERSON_GET										= MODULE_NAME.concat(":PERSON:3");
	String PERSON_GET_ALL									= MODULE_NAME.concat(":PERSON:4");
	String PERSON_LIST										= MODULE_NAME.concat(":PERSON:5");
	String PERSON_GET_ALLOWED_MODULES						= MODULE_NAME.concat(":PERSON:6");
	String PERSON_GET_ALLOWED_MODULES_WITH_ROLES			= MODULE_NAME.concat(":PERSON:7");
	String PERSON_GET_WITH_ROLES							= MODULE_NAME.concat(":PERSON:8");
	String PERSON_GET_WITH_DEPARTMENTS						= MODULE_NAME.concat(":PERSON:9");
	String PERSON_UPDATE_WITH_ROLES							= MODULE_NAME.concat(":PERSON:10");
	String PERSON_UPDATE_WITH_DEPARTMENTS					= MODULE_NAME.concat(":PERSON:11");
	String PERSON_NO_DEPARTMENTS							= MODULE_NAME.concat(":PERSON:12");
	String PERSON_SEARCH									= MODULE_NAME.concat(":PERSON:13");
	String PERSON_GET_BY_USERNAME							= MODULE_NAME.concat(":PERSON:14");
	String PERSON_GET_USERNAME								= MODULE_NAME.concat(":PERSON:15");
	String PERSON_GET_BY_ORGANIZATION						= MODULE_NAME.concat(":PERSON:16");
	String PERSON_GET_FROM_DEPARTMENT						= MODULE_NAME.concat(":PERSON:17");
	String PERSON_COUNT_FOR_ORGANISATION					= MODULE_NAME.concat(":PERSON:18");
	String PERSON_UPDATE_JOBS								= MODULE_NAME.concat(":PERSON:19");
	String PERSON_GET_WITH_JOBS								= MODULE_NAME.concat(":PERSON:20");
	String PERSON_UPDATE_USER_AUTH							= MODULE_NAME.concat(":PERSON:21");
	String PERSON_GET_USER_AUTH								= MODULE_NAME.concat(":PERSON:22");
	String PERSON_SEND_PASSWORD_BY_EMAIL_TO_ADMIN			= MODULE_NAME.concat(":PERSON:23");
	String PERSON_SEND_PASSWORD_BY_EMAIL_TO_USER			= MODULE_NAME.concat(":PERSON:24");
	String PERSON_IS_ADMIN									= MODULE_NAME.concat(":PERSON:25");	
	String PERSON_ADMIN										= MODULE_NAME.concat(":PERSON:26");	
	String PERSON_GET_BY_ORGANIZATION_NOT_USERGROUP			= MODULE_NAME.concat(":PERSON:27");
	String PERSON_GET_BY_ORGANIZATION_NOT_DEPT				= MODULE_NAME.concat(":PERSON:28");
	String PERSON_GET_USER_AUTH_BY_USERNAME					= MODULE_NAME.concat(":PERSON:29");
	String PERSON_GET_BY_ORGANISATION						= MODULE_NAME.concat(":PERSON:30");	
	String PERSON_GET_WITH_PICTURE							= MODULE_NAME.concat(":PERSON:31");
	String PERSON_GET_BY_ONLY_ONE_DEPT						= MODULE_NAME.concat(":PERSON:32");
	String PERSON_GET_BY_ORGANISATION_AND_MODULE			= MODULE_NAME.concat(":PERSON:33");
	String PERSON_GET_BY_ROLE_NAME_AND_ORGANISATION			= MODULE_NAME.concat(":PERSON:34");
	String PERSON_GET_BY_USER_GROUP_ID						= MODULE_NAME.concat(":PERSON:35");
	String PERSON_GET_BY_PERSON_ID							= MODULE_NAME.concat(":PERSON:36");
	String PERSON_GET_FROM_SEARCH_SIMPLE					= MODULE_NAME.concat(":PERSON:37");
	String PERSON_DISABLE_ALL								= MODULE_NAME.concat(":PERSON:38");
	String PERSON_GET_ACTIVATED								= MODULE_NAME.concat(":PERSON:39");
	String PERSON_GET_FROM_SEARCH_SIMPLE_WITH_PAGINATION	= MODULE_NAME.concat(":PERSON:40");
	
	//BLDepartment exception's code
	String DEPARTMENT_ADD									= MODULE_NAME.concat(":DEPARTMENT:0"); 
	String DEPARTMENT_DEL									= MODULE_NAME.concat(":DEPARTMENT:1");
	String DEPARTMENT_UPDATE								= MODULE_NAME.concat(":DEPARTMENT:2");
	String DEPARTMENT_GET									= MODULE_NAME.concat(":DEPARTMENT:3");
	String DEPARTMENT_GET_ALL								= MODULE_NAME.concat(":DEPARTMENT:4");
	String DEPARTMENT_LIST									= MODULE_NAME.concat(":DEPARTMENT:5");
	String DEPARTMENT_UPDATEPERSONS							= MODULE_NAME.concat(":DEPARTMENT:6");
	String DEPARTMENT_UPDATEMANAGER							= MODULE_NAME.concat(":DEPARTMENT:7");
	String DEPARTMENT_LIST_BY_ORGANISATION					= MODULE_NAME.concat(":DEPARTMENT:8");
	String DEPARTMENT_GET_FOR_MANAGER						= MODULE_NAME.concat(":DEPARTMENT:9");
	String DEPARTMENT_LIST_SUBDEPARTMENTS					= MODULE_NAME.concat(":DEPARTMENT:10");
	String DEPARTMENT_GET_FAKE_FOR_ORGANIZATION				= MODULE_NAME.concat(":DEPARTMENT:11");
	String DEPARTMENT_GET_WITH_MANAGER_AND_PARENT			= MODULE_NAME.concat(":DEPARTMENT:12");
	String DEPARTAMENT_COUNT_FOR_ORGANISATION				= MODULE_NAME.concat(":DEPARTMENT:13");
	String DEPARTMENT_LIST_ALL_SUBDEPARTMENTS				= MODULE_NAME.concat(":DEPARTMENT:14");
	String DEPARTMENT_LIST_POTENTIAL_PARENT_DEPARTMENTS		= MODULE_NAME.concat(":DEPARTMENT:15");
	String DEPARTMENT_GET_FROM_PERSON						= MODULE_NAME.concat(":DEPARTMENT:16");
	String DEPARTMENT_UPDATE_MANAGER_ID						= MODULE_NAME.concat(":DEPARTMENT:17");

	//BLModule exception's code
	String MODULE_GET										= MODULE_NAME.concat(":MODULE:0");
	String MODULE_LIST										= MODULE_NAME.concat(":MODULE:1");
	String MODULE_LIST_WITH_ROLES							= MODULE_NAME.concat(":MODULE:2");
	String MODULE_LIST_WITH_ROLES_BY_ORG					= MODULE_NAME.concat(":MODULE:3");
	String MODULE_GET_NAME									= MODULE_NAME.concat(":MODULE:4");
	String MODULE_GET_WITHOUT_OM_FOR_ORG					= MODULE_NAME.concat(":MODULE:5");
	String MODULE_LIST_BY_ORG								= MODULE_NAME.concat(":MODULE:6");
	String MODULE_GET_FOR_ORG_WITHOUT_OM					= MODULE_NAME.concat(":MODULE:7");
	
	//BLRole exception's code
	String ROLE_GET											= MODULE_NAME.concat(":ROLE:0");
	String ROLE_GET_RESULTS									= MODULE_NAME.concat(":ROLE:1");
	String ROLE_DELETE										= MODULE_NAME.concat(":ROLE:2");
	String ROLE_ADD											= MODULE_NAME.concat(":ROLE:3");
	String ROLE_WEB_GET										= MODULE_NAME.concat(":ROLE:4");
	String ROLE_WEB_UPDATE									= MODULE_NAME.concat(":ROLE:5");
	String ROLE_UPDATE										= MODULE_NAME.concat(":ROLE:6");
	String ROLE_GET_PERMISSIONS								= MODULE_NAME.concat(":ROLE:7");
	String ROLE_GET_BY_NAME									= MODULE_NAME.concat(":ROLE:8");
	String ROLES_GET_BY_NAMES								= MODULE_NAME.concat(":ROLE:9");
	String ROLE_HAS_PERSON									= MODULE_NAME.concat(":ROLE:10");
	String ROLE_GET_ALL_PERMISSIONS							= MODULE_NAME.concat(":ROLE:11");
	String ROLE_GET_PERSONS									= MODULE_NAME.concat(":ROLE:12");
	
	//BLUserGroup exception's code
	String USERGROUP_DELETE									= MODULE_NAME.concat(":USERGROUP:0");
	String USERGROUP_GET 									= MODULE_NAME.concat(":USERGROUP:1");
	String USERGROUP_GET_RESULTS 							= MODULE_NAME.concat(":USERGROUP:2");
	String USERGROUP_UPDATE 								= MODULE_NAME.concat(":USERGROUP:3");
	String USERGROUP_ADD	 								= MODULE_NAME.concat(":USERGROUP:4");
	String USERGROUP_GET_ALL 								= MODULE_NAME.concat(":USERGROUP:5");
	String USERGROUP_GET_DEFAULT_USER_GROUP					= MODULE_NAME.concat(":USERGROUP:6");
	String USERGROUP_GET_ALL_ORGANIZATION_USER_GROUPS		= MODULE_NAME.concat(":USERGROUP:7");
	String USERGROUP_GET_FROM_PERSON						= MODULE_NAME.concat(":USERGROUP:8");
	String USERGROUP_GET_FOR_PERSON							= MODULE_NAME.concat(":USERGROUP:9");
	String USERGROUP_GET_BY_NAME_AND_ORG					= MODULE_NAME.concat(":USERGROUP:10");
	String USERGROUP_GET_BY_USER							= MODULE_NAME.concat(":USERGROUP:11");
	
	//BLPermission exception's code
	String PERMISSION_LIST									= MODULE_NAME.concat(":PERMISSION:0");
	String PERMISSION_DELETE								= MODULE_NAME.concat(":PERMISSION:1");
	String PERMISSION_GET									= MODULE_NAME.concat(":PERMISSION:2");
	String PERMISSION_UPDATE								= MODULE_NAME.concat(":PERMISSION:3");
	String PERMISSION_ADD									= MODULE_NAME.concat(":PERMISSION:4");
	String PERMISSION_GET_RESULTS							= MODULE_NAME.concat(":PERMISSION:5");
	String PERMISSION_GET_BY_MODULE							= MODULE_NAME.concat(":PERMISSION:6");
	String PERMISSION_DEFAULT_GET_BY_MODULE					= MODULE_NAME.concat(":PERMISSION:7");
	String PERMISSION_GET_BY_MODULE_NOT_ROLE				= MODULE_NAME.concat(":PERMISSION:8");
		
	//BLPersonPicture's code
	String PICTURE_GET										= MODULE_NAME.concat(":PICTURE:0");
	String PICTURE_ADD										= MODULE_NAME.concat(":PICTURE:1");
	String PICTURE_UPDATE									= MODULE_NAME.concat(":PICTURE:2");
	String PICTURE_DELETE									= MODULE_NAME.concat(":PICTURE:3");
	String PICTURE_GET_BY_NAME								= MODULE_NAME.concat(":PICTURE:4");
	String PICTURE_GET_ID_BY_ID								= MODULE_NAME.concat(":PICTURE:5");
	String PICTURE_GET_BY_ID								= MODULE_NAME.concat(":PICTURE:6");
	String PICTURE_DELETE_PERSON_ID							= MODULE_NAME.concat(":PICTURE:7");

	//BLTheme's code
	String THEME_GET										= MODULE_NAME.concat(":THEME:0");
	String THEME_ADD										= MODULE_NAME.concat(":THEME:1");
	String THEME_UPDATE										= MODULE_NAME.concat(":THEME:2");
	String THEME_DELETE										= MODULE_NAME.concat(":THEME:3");
	String THEME_GET_BY_CODE								= MODULE_NAME.concat(":THEME:4");
	String THEME_LIST										= MODULE_NAME.concat(":THEME:5");

	//BLLogo's code
	String LOGO_GET											= MODULE_NAME.concat(":LOGO:0");
	String LOGO_ADD											= MODULE_NAME.concat(":LOGO:1");
	String LOGO_UPDATE										= MODULE_NAME.concat(":LOGO:2");
	String LOGO_DELETE										= MODULE_NAME.concat(":LOGO:3");
	String LOGO_GET_BY_ORGANISATION_ID						= MODULE_NAME.concat(":LOGO:4");
	
	//BLooo's code
	String OOO_GET_BY_PERSON_ID								= MODULE_NAME.concat(":OOO:0");
	String OOO_GET_OOOWEB_BY_ID								= MODULE_NAME.concat(":OOO:1");
	String OOO_UPDATE										= MODULE_NAME.concat(":OOO:2");
	String OOO_ADD											= MODULE_NAME.concat(":OOO:3");
	String OOO_GET_RESULTS									= MODULE_NAME.concat(":OOO:4");
	String OOO_DELETE										= MODULE_NAME.concat(":OOO:5");
	String OOO_GET_BY_PERSON_REPLACEMENT_ID					= MODULE_NAME.concat(":OOO:6");
	String OOO_GET_ALL										= MODULE_NAME.concat(":OOO:7");
	
	String EXPORT_IMPORT_IMPORT								= MODULE_NAME.concat(":EXPIMP:0");
	String EXPORT_IMPORT_EXPORT								= MODULE_NAME.concat(":EXPIMP:1");
	
	//BLCalendar's code
	String CALENDAR_ADD										= MODULE_NAME.concat(":CALENDAR:0");
	String CALENDAR_UPDATE									= MODULE_NAME.concat(":CALENDAR:1");
	String CALENDAR_DELETE									= MODULE_NAME.concat(":CALENDAR:2");
	String CALENDAR_GET										= MODULE_NAME.concat(":CALENDAR:3");
	String CALENDAR_GET_BY_ORGANISATION						= MODULE_NAME.concat(":CALENDAR:4");	
	
	//BLFreeday's code
	String FREEDAY_GET_BY_CALENDAR							= MODULE_NAME.concat(":FREEDAY:0");
	String FREEDAY_ADD										= MODULE_NAME.concat(":FREEDAY:1");
	String FREEDAY_GET										= MODULE_NAME.concat(":FREEDAY:2");
	String FREEDAY_DELETE									= MODULE_NAME.concat(":FREEDAY:3");
	String FREEDAY_UPDATE									= MODULE_NAME.concat(":FREEDAY:4");
	String FREEDAY_GET_BY_ORGANISATION						= MODULE_NAME.concat(":FREEDAY:5");
	
	//BLJob's code
	String JOB_ADD											= MODULE_NAME.concat(":JOB:0");
	String JOB_GET_RESULTS									= MODULE_NAME.concat(":JOB:1");
	String JOB_DELETE										= MODULE_NAME.concat(":JOB:2");
	String JOB_UPDATE										= MODULE_NAME.concat(":JOB:3");
	String JOB_GET											= MODULE_NAME.concat(":JOB:4");
	String JOB_GET_RESULTS_BY_ID							= MODULE_NAME.concat(":JOB:5");
	String JOB_UPDATE_STATUS								= MODULE_NAME.concat(":JOB:6");
	String JOB_WEB_GET										= MODULE_NAME.concat(":JOB:7");
	String JOB_HAS_PERSON									= MODULE_NAME.concat(":JOB:8");
	String JOB_GET_BY_ORG									= MODULE_NAME.concat(":JOB:9"); 
	
	//BLSetting's code
	String SETTING_UPDATE									= MODULE_NAME.concat(":SETTING:0");
	String SETTING_GET										= MODULE_NAME.concat(":SETTING:1");
	String SETTING_GET_VALUE								= MODULE_NAME.concat(":SETTING:2");
	String SETTING_GET_BY_ORGID								= MODULE_NAME.concat(":SETTING:3");
	String SETTING_GET_DEFAULT								= MODULE_NAME.concat(":SETTING:4");
	String SETTING_GET_DEFAULT_VALUE						= MODULE_NAME.concat(":SETTING:5");

	
	//BLLocalization's code
	String LOCALIZATION_GET									= MODULE_NAME.concat(":LOCALIZATION:0");	
	String LOCALIZATION_ADD									= MODULE_NAME.concat(":LOCALIZATION:1");
	String LOCALIZATION_UPDATE								= MODULE_NAME.concat(":LOCALIZATION:2");
	String LOCALIZATION_DELETE								= MODULE_NAME.concat(":LOCALIZATION:3");
	
	//BLAudit
	String AUDIT_ADD										= MODULE_NAME.concat(":AUDIT:0");
	
	//EndPoint Exception
	String ENDPOINT_GET_ALL_ORGANISATIONS					= MODULE_NAME.concat(":ENDPOINT:0");
	String ENDPOINT_ORG_GET									= MODULE_NAME.concat(":ENDPOINT:1");
	String ENDPOINT_GET_USERS_SIMPLE						= MODULE_NAME.concat(":ENDPOINT:2");
	String ENDPOINT_GET_USERAUTH_BY_USERNAME				= MODULE_NAME.concat(":ENDPOINT:3");
	String ENDPOINT_GET_USERAUTH_BY_SECURITY_TOKEN			= MODULE_NAME.concat(":ENDPOINT:4");
	String ENDPOINT_GET_USERS_AND_GROUPS_BY_ORGANIZATION_ID	= MODULE_NAME.concat(":ENDPOINT:5");
	String ENDPOINT_LOGO_GET								= MODULE_NAME.concat(":ENDPOINT:6");
	String ENDPOINT_GET_FREEDAYS							= MODULE_NAME.concat(":ENDPOINT:7");
	String ENDPOINT_GET_ORGANISATIONS_BY_MODULE				= MODULE_NAME.concat(":ENDPOINT:8");
	String ENDPOINT_GET_USERS_BY_ORGANIZATION_ID			= MODULE_NAME.concat(":ENDPOINT:9");
	String ENDPOINT_GET_USERS_BY_ROLE_NAME_AND_ORGANISATION = MODULE_NAME.concat(":ENDPOINT:10");
	String ENDPOINT_ORGANISATION_HAS_AUDIT_MODULE			= MODULE_NAME.concat(":ENDPOINT:11");
	String ENDPOINT_GET_PERSON_BY_PERSONID					= MODULE_NAME.concat(":ENDPOINT:12");
	String ENDPOINT_GET_USER_GROUP_BY_ID					= MODULE_NAME.concat(":ENDPOINT:13");
	String ENDPOINT_GET_USER_GROUPS_BY_USER					= MODULE_NAME.concat(":ENDPOINT:14");
	String ENDPOINT_GET_USERS_BY_USER_GROUP					= MODULE_NAME.concat(":ENDPOINT:15");
	String ENDPOINT_GET_PERSON_FROM_SEARCH					= MODULE_NAME.concat(":ENDPOINT:16");
	String ENDPOINT_GET_CALENDAR_BY_ORGANIZATION			= MODULE_NAME.concat(":ENDPOINT:17");
	String ENDPOINT_GET_PERSONS_BY_ROLE						= MODULE_NAME.concat(":ENDPOINT:18");	

}
