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
var language_ro = {
	_CREATEBUTTON: 'Creeaza',
	LOCALE:														'RO',
	
	 //-------------------------------Validate messages that are displayed using internationalisation!-------------------------------------------- 
 
	//VALIDATION PANEL
	YES:														'Da',
	NO:															'Nu',
	
	//ORGANISATION
	ORGANISATIONFORM_NAME_ERROR:  							'obligatoriu',
	ORGANISATIONFORM_PHONE_ERROR: 							'obligatoriu',
	ORGANISATIONFORM_ADDRESS_ERROR: 						'obligatoriu',
	ORGANISATIONFORM_J_ERROR:								'obligatoriu',
	ORGANISATIONFORM_CUI_ERROR:								'obligatoriu',
	ORGANISATIONFORM_IBAN_ERROR:							'obligatoriu',
	ORGANISATIONFORM_LOCATION_ERROR:						'obligatoriu',
	ORGANISATIONFORM_FAX_ERROR:  							'obligatoriu',
	ORGANISATIONFORM_EMAIL_ERROR: 							'email incorect',
	ORGANISATIONFORM_OBSERVATION_ERROR:						'obligatorii',
	ORGANISATIONFORM_CAPITAL_ERROR:							'obligatoriu',
	ORGANISATIONFORM_NAME_UNIQUE_ERROR:						'Acest nume exista deja!',
	ORGANISATIONFORM_TYPE_ERROR:							'Alegeti tipul',
	CHANGEMODULESFORM_SELECT1_ERROR:						'Alegeti cel putin un modul',
	ORGANISATIONFORM_OBSERVATION_ERROR:						'obligatoriu',
	ORGANISATIONFORM_LOCATION_ERROR:						'obligatoriu',
	
	//DEPARTMENT
	DEPARTMENTFORM_NAME_ERROR:  							'obligatoriu',
	DEPARTMENTFORM_OBSERVATION_ERROR:						'obligatorii',
	DEPARTMENTFORM_NO_JOB_SELECTED:							'Alegeti functia',
	DEPARTMENTFORM_MANAGERID_ERROR:							'Alegeti director general',
	DEPARTMENTFORM_OBSERVATION_ERROR:						'Textul poate contine doar cifre, litere si spatii',
	
	//PERSON
	PERSONFORM_FIRSTNAME_ERROR: 							'obligatoriu',
	PERSONFORM_LASTNAME_ERROR: 								'obligatoriu',
	PERSONFORM_USERNAME_ERROR: 								'obligatoriu',
	PERSONFORM_ADDRESS_ERROR:								'obligatoriu',
	PERSONFORM_PASSWORD_ERROR: 								'obligatoriu',
	PERSONFORM_PASSWORDCONFIRM_ERROR: 						'obligatoriu',
	PERSONFORM_YEAR_ERROR:									'doar cifre',
	PERSONFORM_EMAIL_ERROR:									'email incorect',
	PERSONFORM_NO_JOB_SELECTED:								'Alegeti functia',
	PERSON_JOBS_SELECT_ERROR:								'Alegeti functia',
	PERSONFORM_USERNAME_UNIQUE_ERROR:						'Acest username exista deja!',
	CHANGEROLESFORM_SELECT1_ERROR:							'Alegeti cel putin un rol',
	PERSONFORM_OBSERVATION_ERROR:							'Textul poate contine doar cifre, litere si spatii',
	PERSONFORM_JOB_DISABLED_ERROR:							'Inactiv',
	
	//CALENDAR
	CALENDARFORM_STARTWORK_ERROR:							'Format incorect!',
	CALENDARFORM_ENDWORK_ERROR:								'Format incorect!',
	CALENDARFORM_ORGANISATIONID_ERROR:						'Selectati o organizatie',
	
	//ROLE
	ROLEFORM_NAME_ERROR:									'obligatoriu',
	ROLEFORM_ORGANISATION_ERROR:							'Alegeti organizatia',
	ROLEFORM_MODULEID_ERROR:								'Alegeti un modul',
	NO_MODULES_ERROR:										'Alegeti o organizatie cu module',
	ROLEFORM_NAME_UNIQUE_ERROR:								'Acest rol deja exista!',
	ROLEFORM_MODULE_ERROR:									'Alegeti un modul',
	ROLEFORM_DESCRIPTION_EN_ERROR:							'Textul poate contine doar cifre, litere si spatii',
	ROLEFORM_DESCRIPTION_RO_ERROR:							'Textul poate contine doar cifre, litere si spatii',
	ROLEFORM_OBSERVATION_ERROR:								'Textul poate contine doar cifre, litere si spatii',
	
	//PERMISSION
	PERMISSIONFORM_NAME_ERROR:								'obligatoriu',
	PERMISSIONFORM_MODULEID_ERROR:							'Alegeti un modul',
	PERMISSIONFORM_DESCRIPTION_EN_ERROR:					'Textul poate contine doar cifre, litere si spatii',
	PERMISSIONFORM_DESCRIPTION_RO_ERROR:					'Textul poate contine doar cifre, litere si spatii',
	PERMISSION_FORM_NAME_ERROR:								'Textul poate contine doar cifre, litere si spatii',
	
	//OOO
	NO_REPLACEMENT_ERROR:									'Alegeti un inlocuitor',
	NO_OWNER_ERROR:											'Selectati o persoana',
	OOOFORM_ENDPERIOD_ERROR:								'in formatul dd-MM-yyyy HH:mm',
	OOOFORM_STARTPERIOD_ERROR:								'in formatul dd-MM-yyyy HH:mm',
	OOOFORM_OBSERVATION_ERROR:								'Textul poate contine doar cifre, litere si spatii',
	ANOTHER_REPLACEMENT_ERROR:								'Alegeti un alt inlocuitor',
	
	//FREEDAY
	FREEDAYFORM_DAY_ERROR:									'in formatul dd-MM-yyyy',
	FREEDAYFORM_OBSERVATION_ERROR:							'Textul poate contine doar cifre, litere si spatii',
		
	//JOB
	JOBFORM_NAME_ERROR:										'obligatoriu',
	JOBFORM_NAME_PANEL_ERROR:								'obligatoriu',
	JOBFORM_ORGANISATIONID_ERROR:							'Alegeti o organizatie',
	JOBFORM_DESCRIPTION_ERROR:								'Textul poate contine doar cifre, litere si spatii',
	JOBFORM_OBSERVATION_ERROR:								'Textul poate contine doar cifre, litere si spatii',
	
	//USERGROUP
	USERGROUPFORM_NAME_ERROR:								'obligatoriu',
	USERGROUPFORM_DESCRIPTION_ERROR:						'Textul poate contine doar cifre, litere si spatii',
	USERGROUPFORM_NAME_UNIQUE_ERROR:						'Acest grup de utilizatori deja exista',
	
	//GENERAL ERROR FOR VERIFICATION
	SERVER_ERROR:											'Eroare de Server: Nu s-a putut verifica unicitatea !',
	
	//GENERAL SEARCH FORM ERRORS
	SEARCHFORM_NAME_ERROR:									'Textul poate contine doar cifre, litere si spatii',
	SEARCHFORM_FIRSTNAME_ERROR:								'Textul poate contine doar cifre, litere si spatii',
	SEARCHFORM_LASTNAME_ERROR:								'Textul poate contine doar cifre, litere si spatii',
	SEARCHFORM_MANAGERNAME_ERROR:							'Textul poate contine doar cifre, litere si spatii',
	SEARCHFORM_PARENTDEPARTMENTNAME_ERROR:					'Textul poate contine doar cifre, litere si spatii',
	SEARCHFORM_EMAIL_ERROR:									'Email incorect',
	SEARCHFORM_FAX_ERROR:									'Fax incorect',
	
	//----------------------------------------------------COMMON MESSAGES----------------------------------------------------------
	
	SUBMIT_DELETE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE:	'Sunteti sigur ca vreti sa efectuati stergerea?',
	SUBMIT_ENABLE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE:	'Sunteti sigur ca vreti sa activati organizatia?',
	SUBMIT_DISABLE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE:	'Sunteti sigur ca vreti sa dezactivati organizatia?'
	
  };