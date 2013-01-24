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
 * @author alu
 *
 */
public class PermissionConstant {
	
	//////////////////////////////// OM ////////////////////////////////
	
	// the basic permission from the organisation management module
	private final String OM_Basic							= "OM_Basic";
	
	// gives permission to  see all the branches in an organisation
	private final String OM_ShowBranchSelect 				= "OM_ShowBranchSelect";
	
	//------------------------------ORGANISATION----------------------------------
	
	// gives permission to update an organisation
	private final String OM_OrgUpdate						= "OM_OrgUpdate";
	
	// gives permission to add a child organisation for a specific organisation
	private final String OM_OrgAddChild						= "OM_OrgAddChild";
			
	// gives permission to create an admin user for a specific organisation
	private final String OM_OrgCreateAdmin					= "OM_OrgCreateAdmin";
		
	// gives permission to view the information for a child from organisation
	private final String OM_OrgChildView					= "OM_OrgChildView";
	
	// gives permission to delete a child from organisation
	private final String OM_OrgChildDelete					= "OM_OrgChildDelete";
	
	// gives permission to activate or dezactivate a child from an organisation
	private final String OM_OrgChildChangeStatus			= "OM_OrgChildChangeStatus";		
						
	// gives permission to add the CEO for a specific organistion
	private final String OM_OrgAddCeo						= "OM_OrgAddCeo";
	
	// gives permission to view the tree of the organisation
	private final String OM_ViewTreeComplete				= "OM_ViewTreeComplete";	
	
	
	//------------------------------DEPARTMENT----------------------------------
	
	// gives permission to add and delete a department to the organisation
	private final String OM_DeptAdd							= "OM_DeptAdd";
	
	// gives permission to delete a department from the organisation
	private final String OM_DeptDelete						= "OM_DeptDelete";
	
	// gives permission to update a department from the organisation
	private final String OM_DeptUpdate						= "OM_DeptUpdate";
	
	// gives permission to change the persons from department
	private final String OM_ChangePersons					= "OM_ChangePersons";	
	
	//------------------------------PERSON----------------------------------
	
	// gives permission to edit the username of the person
	private final String OM_EditUsername					= "OM_EditUsername";
	
	// gives permission to add a person to an organisation
	private final String OM_PersonAdd						= "OM_PersonAdd";
		
	// gives permission to delete a person from the organisation
	private final String OM_PersonDelete					= "OM_PersonDelete";
	
	// gives permission to update a person from the organisation
	private final String OM_PersonUpdate					="OM_PersonUpdate";
	
	// gives permission to view person's informations about roles, modules and jobs 
	private final String OM_ViewPersonRoleDepartmentDetails = "OM_ViewPersonRoleDepartmentDetails";
	
	// gives permission to reset the password
	private final String OM_ResetPassword					= "OM_ResetPassword";
	
	// gives permission to dezactivate or activate the status of a person
	private final String OM_PersonChangeStatus				= "OM_PersonChangeStatus";
		
	
	//------------------------------ROLE----------------------------------
	
	// gives permission to add a role
	private final String OM_RoleAdd							= "OM_RoleAdd";
	
	// gives the permission to delete a role
	private final String OM_RoleDelete						= "OM_RoleDelete";
		
	// gives permission to edit a role
	private final String OM_RoleUpdate						= "OM_RoleUpdate";
	
	// gives permission  to search the roles
	private final String OM_RoleSearch						= "OM_RoleSearch";	
	
	//-----------------------------USERGROUP--------------------------------
	
	// gives the permission to add an user group
	private final String OM_AddUserGroup					= "OM_AddUserGroup";
	
	// gives the permission to delete an user group
	private final String OM_DeleteUserGroup					= "OM_DeleteUserGroup";
	
	// gives the permission to edit an user group
	private final String OM_UpdateUserGroup					= "OM_UpdateUserGroup";
	
	//------------------------------PERMISSION----------------------------------
	
	// gives the permission to view the permission
	private final String OM_PermissionView					= "OM_PermissionView";
	
	//------------------------------OUTOFOFFICE----------------------------------
	
	// gives permission to add an out of office profile
	private final String OM_OutOfOfficeAddToAll				= "OM_OutOfOfficeAddToAll";

	// gives permission to search out of office profile
	private final String OM_OutOfOfficeSearch				= "OM_OutOfOfficeSearch";
	
	//------------------------------CALENDAR---------------------------------------
	
	// gives permission to update the calendar
	private final String OM_CalendarUpdate					= "OM_CalendarUpdate";
	
	//------------------------------FREEDAY---------------------------------------
	
	// gives permission to add a freeday
	private final String OM_FreedayAdd						= "OM_FreedayAdd";
	
	// gives permission to update a freeday
	private final String OM_FreedayUpdate					= "OM_FreedayUpdate";
	
	// gives permission to delete a freeday 
	private final String OM_FreedayDelete					= "OM_FreedayDelete";
	
	//------------------------------JOBS---------------------------------------
	
	// gives permission to add a job
	private final String OM_JobAdd							= "OM_JobAdd";
	
	// gives permission to delete a job
	private final String OM_JobDelete						= "OM_JobDelete";
	
	// gives permission to update a job
	private final String OM_JobUpdate						= "OM_JobUpdate";
	
	// gives permission to change jobs status, to dezactivate or activate the specific status
	private final String OM_JobChangeStatus					= "OM_JobChangeStatus";
	
	// gives permission to search the jobs
	private final String OM_JobSearch						= "OM_JobSearch";
	
	//------------------------------SETTINGS----------------------------------
	
	// gives permission to change the settings for an organisation
	private final String OM_ChangeSettings					= "OM_ChangeSettings";
	
	// gives permission to change the logo for an organisation
	private final String OM_ChangeLogo						= "OM_ChangeLogo";
												
	//////////////////////////////// DM ////////////////////////////////
	private final String DM_Basic							= "DM_Basic";	
	
	//////////////////////////////// IR ////////////////////////////////
	private final String IR_Basic							= "IR_Basic";	
	
	//////////////////////////////// AUDIT ////////////////////////////////
	private final String AUDIT_Basic						= "AUDIT_Basic";	
	
	//////////////////////////////// CM // ////////////////////////////////
	private final String CM_Basic							= "CM_Basic";	
	
	//////////////////////////////// TS // ////////////////////////////////
	private final String TS_Basic							= "TS_Basic";	

	//////////////////////////////// TS // ////////////////////////////////
	private final String RM_Basic							= "RM_Basic";	
	
	/////////////////////////////// SUPER //////////////////////////////
	private final String _Super								= "Super";

	//singleton implementation
    private static PermissionConstant theInstance = null;
    static {
        theInstance = new PermissionConstant();
    }
    public static PermissionConstant getInstance() {
        return theInstance;
    }
	
    // getters(for jsp calls)
	private PermissionConstant(){
	}
	
	public String getOM_OrgChildView() {
		return OM_OrgChildView;
	}

	public String getOM_OrgChildDelete() {
		return OM_OrgChildDelete;
	}

	public String getOM_OrgChildChangeStatus() {
		return OM_OrgChildChangeStatus;
	}

	public String getOM_Basic() {
		return OM_Basic;
	}

	public String getOM_OutOfOfficeAddToAll() {
		return OM_OutOfOfficeAddToAll;
	}

	public String getDM_Basic() {
		return DM_Basic;
	}
	
	public String getIR_Basic() {
		return IR_Basic;
	}

	public String get_Super() {
		return _Super;
	}

	public String getOM_PersonAdd() {
		return OM_PersonAdd;
	}
	
	public String getOM_PersonDelete() {
		return OM_PersonDelete;
	}

	public String getOM_ChangeSettings() {
		return OM_ChangeSettings;
	}
	
	public String getOM_OrgAddChild(){
		return OM_OrgAddChild;
	}
	
	public String getOM_OrgCreateAdmin(){
		return OM_OrgCreateAdmin;
	}

	public String getOM_ViewTreeComplete() {
		return OM_ViewTreeComplete;
	}

	public String getOM_ViewPersonRoleDepartmentDetails() {
		return OM_ViewPersonRoleDepartmentDetails;
	}

	public String getOM_EditUsername() {
		return OM_EditUsername;
	}

	public String getOM_JobAdd() {
		return OM_JobAdd;		
	}
	
	public String getOM_JobUpdate() {
		return OM_JobUpdate;		
	}
	
	public String getOM_JobDelete() {
		return OM_JobDelete;		
	}
	
		
	public String getOM_OrgUpdate(){
		return OM_OrgUpdate;
	}
	
	public String getOM_OrgAddCeo(){
		return OM_OrgAddCeo;
	}
	
	public String getOM_DeptAdd(){
		return OM_DeptAdd;
	}
	
	public String getOM_DeptUpdate() {
		return OM_DeptUpdate;
	}
	
	public String getOM_DeptDelete() {
		return OM_DeptDelete;
	}
	
	public String getOM_RoleAdd() {
		return OM_RoleAdd;
	}
	
	public String getOM_RoleUpdate() {
		return OM_RoleUpdate;
	}
	
	public String getOM_RoleDelete() {
		return OM_RoleDelete;
	}
	
	public String getOM_ShowBranchSelect() {
		return OM_ShowBranchSelect;
	}
			
	public String getOM_PermissionView() {
		return OM_PermissionView;
	}
	
	public String getOM_ChangeLogo() {
		return OM_ChangeLogo;
	}
	
	public String getOM_ChangePersons() {
		return OM_ChangePersons;
	}
	
	public String getOM_PersonUpdate() {
		return OM_PersonUpdate;
	}
		
	public String getOM_OutOfOfficeSearch() {
		return OM_OutOfOfficeSearch;
	}
	
	public String getOM_ResetPassword() {
		return OM_ResetPassword;
	}
	
	public String getOM_RoleSearch() {
		return OM_RoleSearch;
	}
	
	public String getOM_JobChangeStatus() {
		return OM_JobChangeStatus;
	}
	
	public String getOM_JobSearch() {
		return OM_JobSearch;
	}
	
	public String getOM_PersonChangeStatus() {
		return OM_PersonChangeStatus;
	}
	
	public String getOM_CalendarUpdate() {
		return OM_CalendarUpdate;
	}
	
	public String getOM_FreedayAdd() {
		return OM_FreedayAdd;
	}
	
	public String getOM_FreedayUpdate() {
		return OM_FreedayUpdate;
	}
	
	public String getOM_FreedayDelete() {
		return OM_FreedayDelete;
	}

	public String getOM_UpdateUserGroup() {
		return OM_UpdateUserGroup;
	}

	public String getOM_AddUserGroup() {
		return OM_AddUserGroup;
	}

	public String getOM_DeleteUserGroup() {
		return OM_DeleteUserGroup;
	}

	public String getAUDIT_Basic() {
		return AUDIT_Basic;
	}

	public String getCM_Basic() {
		return CM_Basic;
	}

	public String getTS_Basic() {
		return TS_Basic;
	}
	
	public String getRM_Basic() {
		return RM_Basic;
	}
}
