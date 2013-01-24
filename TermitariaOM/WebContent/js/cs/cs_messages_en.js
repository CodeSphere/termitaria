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
/**
 * @author matti_joona
 */
var language_en = {
	_CREATEBUTTON: 'Create',
	LOCALE:														'EN',
	 //------------------------------- Validate messages that are displayed using internationalisation!--------------------------- 
	//VALIDATION PANEL
	YES:														'Yes',
	NO:															'No',
	
	//ORGANISATION
	ORGANISATIONFORM_NAME_ERROR:  								'required',
	ORGANISATIONFORM_PHONE_ERROR:  								'required',
	ORGANISATIONFORM_ADDRESS_ERROR:  							'required',
	ORGANISATIONFORM_FAX_ERROR:  								'required',
	ORGANISATIONFORM_J_ERROR:									'required',
	ORGANISATIONFORM_CUI_ERROR:									'required',
	ORGANISATIONFORM_IBAN_ERROR:								'required',
	ORGANISATIONFORM_LOCATION_ERROR:							'required',
	ORGANISATIONFORM_EMAIL_ERROR: 								'incorrect format',
	ORGANISATIONFORM_OBSERVATION_ERROR:							'required',
	ORGANISATIONFORM_CAPITAL_ERROR:								'required',
	ORGANISATIONFORM_NAME_UNIQUE_ERROR:							'This name is already in use!',
	ORGANISATIONFORM_TYPE_ERROR:								'Choose a type',
	CHANGEMODULESFORM_SELECT1_ERROR:							'Choose at least one module',
	ORGANISATIONFORM_LOCATION_ERROR:							'required',
	ORGANISATIONFORM_OBSERVATION_ERROR:							'required',
		
	//DEPARTMENT
	DEPARTMENTFORM_NAME_ERROR:  								'required',	
	DEPARTMENTFORM_OBSERVATION_ERROR:							'required',
	DEPARTMENTFORM_NO_JOB_SELECTED:								'Choose a job',
	DEPARTMENTFORM_MANAGERID_ERROR:								'Choose a chief executive officer',
	DEPARTMENTFORM_OBSERVATION_ERROR:							'Only digits, letters and spaces can be typed in',
		
	//PERSON
	PERSONFORM_FIRSTNAME_ERROR: 								'required',
	PERSONFORM_LASTNAME_ERROR: 									'required',
	PERSONFORM_ADDRESS_ERROR:									'required',
	PERSONFORM_USERNAME_ERROR: 									'required',
	PERSONFORM_PASSWORD_ERROR: 									'required',
	PERSONFORM_PASSWORDCONFIRM_ERROR: 							'required',
	PERSONFORM_YEAR_ERROR:										'only digits',
	PERSONFORM_EMAIL_ERROR:										'Invalid email',
	PERSONFORM_NO_JOB_SELECTED:									'Choose a job',
	PERSON_JOBS_SELECT_ERROR:									'Choose a job',
	PERSONFORM_USERNAME_UNIQUE_ERROR:							'This username is already in use!',
	CHANGEROLESFORM_SELECT1_ERROR:								'Choose at least on role',
	PERSONFORM_OBSERVATION_ERROR:								'Only digits, letters and spaces can be typed in',
	PERSONFORM_JOB_DISABLED_ERROR:								'Disabled',
	
	//CALENDAR
	CALENDARFORM_STARTWORK_ERROR:								'Invalid time format!',
	CALENDARFORM_ENDWORK_ERROR:									'Invalid time format!',
	CALENDARFORM_ORGANISATIONID_ERROR:							'Select an Organization!',
	
	//ROLE
	ROLEFORM_NAME_ERROR:										'required',
	ROLEFORM_ORGANISATION_ERROR:								'Choose an Organization',
	ROLEFORM_MODULE_ERROR:										'Choose a Module',
	NO_MODULES_ERROR:											'Choose an Organization with Modules',
	ROLEFORM_NAME_UNIQUE_ERROR:									'This role is already in use',
	ROLEFORM_DESCRIPTION_EN_ERROR:								'Only digits, letters and spaces can be typed in',
	ROLEFORM_DESCRIPTION_RO_ERROR:								'Only digits, letters and spaces can be typed in',
	ROLEFORM_OBSERVATION_ERROR:									'Only digits, letters and spaces can be typed in',
	
	//PERMISSION
	PERMISSIONFORM_NAME_ERROR:									'required',
	PERMISSIONFORM_MODULEID_ERROR:								'Choose a module',
	PERMISSIONFORM_DESCRIPTION_EN_ERROR:						'Only digits, letters and spaces can be typed in',
	PERMISSIONFORM_DESCRIPTION_RO_ERROR:						'Only digits, letters and spaces can be typed in',
	PERMISSION_FORM_NAME_ERROR:									'Only digits, letters and spaces can be typed in',
	
	//OOO
	NO_REPLACEMENT_ERROR:										'Select a replacement person',
	NO_OWNER_ERROR:												'Select a person',
	OOOFORM_ENDPERIOD_ERROR:									'in dd-MM-yyyy HH:mm format!',
	OOOFORM_STARTPERIOD_ERROR:									'in dd-MM-yyyy HH:mm format!',
	OOOFORM_OBSERVATION_ERROR:									'Only digits, letters and spaces can be typed in',
	ANOTHER_REPLACEMENT_ERROR:									'Select another replacement person',
	
	//FREEDAY
	FREEDAYFORM_DAY_ERROR:										'in dd-MM-yyyy format!',
	FREEDAYFORM_OBSERVATION_ERROR:								'Only digits, letters and spaces can be typed in',
		
	//JOB
	JOBFORM_NAME_ERROR:											'required',
	JOBFORM_NAME_PANEL_ERROR:									'required',
	JOBFORM_ORGANISATIONID_ERROR:								'Select an Organization!',
	JOBFORM_DESCRIPTION_ERROR:									'Only digits, letters and spaces can be typed in',
	JOBFORM_OBSERVATION_ERROR:									'Only digits, letters and spaces can be typed in',
  	
	//USERGROUP
	USERGROUPFORM_NAME_ERROR:									'required',
	USERGROUPFORM_DESCRIPTION_ERROR:							'Only digits, letters and spaces can be typed in',
	USERGROUPFORM_NAME_UNIQUE_ERROR:							'This user group is already in use',
	
	//GENERAL ERROR FOR VERIFICATION
	SERVER_ERROR:												'Server error: Could not verify uniquness !',
	
	//GENERAL SEARCH FORM ERRORS
	SEARCHFORM_NAME_ERROR:										'Only digits, letters and spaces can be typed in',
	SEARCHFORM_FIRSTNAME_ERROR:									'Only digits, letters and spaces can be typed in',
	SEARCHFORM_LASTNAME_ERROR:									'Only digits, letters and spaces can be typed in',
	SEARCHFORM_MANAGERNAME_ERROR:								'Only digits, letters and spaces can be typed in',
	SEARCHFORM_PARENTDEPARTMENTNAME_ERROR:						'Only digits, letters and spaces can be typed in',
	SEARCHFORM_EMAIL_ERROR:										'Incorect format',
	SEARCHFORM_FAX_ERROR:										'Incorect format',
	
	//----------------------------------------------------COMMON MESSAGES----------------------------------------------------------
	
	SUBMIT_DELETE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE:		'Are you sure you want to execute the delete action?',
	SUBMIT_ENABLE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE:		'Are you sure you want to enable the organization?',
	SUBMIT_DISABLE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE:		'Are you sure you want to disable the organization?'
  };