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
 * User for constant of the model layer
 * 
 * @author matti_joona
 */
public interface IModelConstant {

	//Hibernate entities'name
	//--------------------------------------ORGANISATION-------------------------------------
	String organisationEntity									= "Organisation";
	String organisationAllEntity 								= "OrganisationAll";
	String organisationWithDepartmentsEntity					= "OrganisationWithDepartments";
	String organisationWithPersonsEntity						= "OrganisationWithPersons";
	String organisationWithCalendarEntity						= "OrganisationWithCalendar";
	String organisationWithRolesEntity							= "OrganisationWithRoles";
	String organisationWithSettingsEntity						= "OrganisationWithSettings";
	String organisationWithModulesEntity						= "OrganisationWithModules";
	String organisationForNomEntity								= "OrganisationForNom";
	String organisationWithModulesForNomEntity					= "OrganisationWithModulesForNom";
	String organisationForExpImpEntity							= "OrganisationForExpImp";
	String organisationForDeleteEntity							= "OrganisationForDelete";
	
	//--------------------------------------DEPARTMENT---------------------------------------
	String departmentSimpleEntity								= "DepartmentSimple";
	String departmentEntity										= "Department";
	String departmentAllEntity 									= "DepartmentAll";
	String departmentWithPersonsEntity 							= "DepartmentWithPersons";
	String departmentWithManagerEntity 							= "DepartmentWithManager";
	String departmentWithOrganisationEntity						= "DepartmentWithOrganisation";
	String departmentWithParentEntity 							= "DepartmentWithParent";
	String departmentForExpImpEntity		 					= "DepartmentForExpImp";
	String departmentForExpImp1Entity		 					= "DepartmentForExpImp1";
	String departmentForExpImp2Entity		 					= "DepartmentForExpImp2";
	String departmentFakeFromImp1Entity		 					= "DepartmentFakeFromImp1";
	String departmentFakeFromImp2Entity		 					= "DepartmentFakeFromImp2";
	String departmentForUpdateEntity		 					= "DepartmentForUpdate";
	String departmentWithPersonsAndJobsEntity 					= "DepartmentWithPersonsAndJobs";
	String departmentSimpleWithPersonsEntity					= "DepartmentSimpleWithPersons";
	String departmentForUpdateManagerEntity						= "DepartmentForUpdateManager";
	
	//--------------------------------------PERSON-------------------------------------------
	String personEntity			 								= "Person";
	String personWithPictureEntity								= "PersonWithPicture";
	String personSimpleEntity									= "PersonSimple";	
	String personAllEntity										= "PersonAll";
	String personForUpdateEntity								= "PersonForUpdate";
	String personForAddEntity									= "PersonForAdd";
	String personWithCredentialsEntity							= "PersonWithCredentials";
	String personWithDepartmentsEntity							= "PersonWithDepartments";
	String personWithRolesEntity	    						= "PersonWithRoles";
	String personForAuthorizationEntity							= "PersonForAuthorization";
	String personForUserDetailsEntity							= "PersonUserDetails";
	String personForListingEntity								= "PersonForListing";
	String personForExpImpEntity								= "PersonForExpImp";
	String personForExpImp1Entity								= "PersonForExpImp1";
	String personAdmintITWithRolesEntity						= "PersonAdmintITWithRoles";
	String personFromOrganisationEntity							= "PersonFromOrganisation";
	String personWithUserGroupsEntity							= "PersonWithUserGroups";
	String personChageStatusEntity								= "PersonChangeStatus";		
		
	//--------------------------------------CALENDAR-----------------------------------------
	String calendarEntity 										= "Calendar";
	
	//--------------------------------------FREEDAY-----------------------------------------
	String freeDayEntity 										= "FreeDay";

	//--------------------------------------LOCALIZATION-----------------------------------------
	String localizationEntity 									= "Localization";
	
	//--------------------------------------ROLE---------------------------------------------
	String roleEntity 											= "Role";
	String roleWebEntity										= "RoleWeb";
	String roleAllEntity										= "RoleAll";
	String roleWithPermissionsEntity							= "RoleWithPermissions";
	String roleWithModuleEntity									= "RoleWithModule";
	String roleForListingEntity									= "RoleForListing";
	String roleForOrganisation									= "RoleForOrganisation";
	String roleForDeleteEntity									= "RoleForDelete";
	String roleForAdminEntity									= "RoleForAdmin";
	
	//--------------------------------------USERGROUP----------------------------------------
	String userGroupAllEntity									= "UserGroupAll";
	String userGroupForListingEntity							= "UserGroupForListing";
	String userGroupForUpdateEntity								= "UserGroupForUpdate";
	String userGroupSimpleEntity								= "UserGroupSimple";
	
	//--------------------------------------PERMISSION---------------------------------------
	String permissionEntity										= "Permission";
	String permissionAllEntity									= "PermissionAll";
	String permissionForListingEntity							= "PermissionForListing";
	String permissionWithRolesEntity							= "PermissionWithRoles";
		
	//--------------------------------------OOO----------------------------------------------
	String oooEntity											= "OutOfOffice";
	String oooAllEntity											= "OutOfOfficeAll";
	String oooWebEntity											= "OutOfOfficeWeb";
	
	//--------------------------------------SETTING-------------------------------------------
	String settingEntity										= "Setting";
	String settingAllEntity										= "SettingAll";
	
	//--------------------------------------JOB-----------------------------------------------
	String jobWebEntity											= "JobWeb";
	String jobForListingEntity									= "JobForListing";
	String jobEntity											= "Job";
	String jobAllEntity											= "JobAll";
	String jobForDelete											= "JobForDelete";
	String jobWithPersons										= "JobWithPersons";
	
	//--------------------------------------MODULE--------------------------------------------
	String moduleEntity											= "Module";
	String moduleWithRolesEntity								= "ModuleWithRoles";
	String moduleWithOrganisationsEntity						= "ModuleWithOrganisations";
	
	//--------------------------------------PICTURE-------------------------------------------
	String pictureEntity										= "Picture";


	//--------------------------------------THEME---------------------------------------------
	String themeEntity											= "Theme";

	//--------------------------------------LOGO----------------------------------------------
	String logoEntity											= "Logo";

	//--------------------------------------SETTINGNOM----------------------------------------
	String settingNomThemeEntity								= "SettingNomTheme";
	
	//AUDIT BEAN PROPERTIES
	public static final String auditDate 						= "date";
	public static final String auditPersonId					= "personId";
	public static final String auditMessageRO					= "messageRO";
	public static final String auditMessageEN					= "messageEN";
	public static final String auditEvent 						= "event";
	public static final String auditOrganisationId				= "organisationId";	
	public static final String auditModuleId					= "moduleId";
	public static final String auditFirstName					= "firstName";
	public static final String auditLastName					= "lastName";
	
}
