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
/**
 * Validate messages that are displayed using internationalisation! 
 * 
 * @author matti_joona
 */
var language_ro = {
		
		YES:													 'Da',
		NO:													 	 'Nu',
		
		LOCALE:													 'RO',
		//------------------------------- Validate messages that are displayed using internationalisation!--------------------------- 
		//YUI TABLE 
		NO_RECORDS:												'Nu sunt inregistrari!',
		
		//ADD QUESTION
		CONFIRM_ADD:											'Veti pierde modificarile nesalvate. Continuati?',
		
		// ACTIVITY
		
		ACTIVITYFORM_NAME_ERROR:								'Obligatoriu!',
		ACTIVITYFORM_PROJECT_ERROR:								'Alegeti un proiect!',
		ACTIVITYFORM_COSTPRICECURRENCY_ERROR:					'Alegeti moneda!',
		ACTIVITYFORM_COSTTIMEUNIT_ERROR:						'Alegeti unitatea de timp!',
		ACTIVITYFORM_BILLINGPRICECURRENCY_ERROR:				'Alegeti moneda!',
		ACTIVITYFORM_BILLINGTIMEUNIT_ERROR:						'Alegeti unitatea de timp!',
		
		ACTIVITYFORMPANEL_NAME_ERROR:							'Obligatoriu!',
		ACTIVITYFORMPANEL_PROJECT_ERROR:						'Alegeti un proiect!',
		ACTIVITYFORMPANEL_COSTPRICECURRENCY_ERROR:				'Alegeti moneda!',
		ACTIVITYFORMPANEL_COSTTIMEUNIT_ERROR:					'Alegeti unitatea de timp!',
		ACTIVITYFORMPANEL_BILLINGPRICECURRENCY_ERROR:			'Alegeti moneda!',
		ACTIVITYFORMPANEL_BILLINGTIMEUNIT_ERROR:				'Alegeti unitatea de timp!',
		
		// PROJECT DETAIL
	  	
		PROJECTDETAILSFORM_BUDGETCURRENCY_ERROR:				'Alegeti moneda!',
		
		// RECORD
		
		RECORDFORM_PROJECT_ERROR:								'Obligatoriu!',
		RECORDFORM_ACTIVITY_ERROR:								'Obligatoriu!',
		RECORDFORM_USER_ERROR:									'Obligatoriu!',
		RECORDFORM_TITLE_ERROR:									'Obligatoriu!',
		RECORDFORM_DESCRIPTION_ERROR:							'Obligatoriu!',
		RECORDFORM_TIME_CHECK_AT_LEAST_ONE_ERROR:				'Orele de lucru sau cele suplimentare trebuie specificate!',
		RECORDFORM_START_TIME_ERROR:							'Obligatoriu!',
		RECORDFORM_END_TIME_ERROR:								'Obligatoriu!',
		RECORDFORM_OVERTIME_START_TIME_ERROR:					'Obligatoriu!',	
		RECORDFORM_OVERTIME_END_TIME_ERROR:						'Obligatoriu!',
		RECORD_CHOOSE_PROJECT:									'Selectati proiectul/organizatia!',
		RECORD_CHOOSE_ACTIVITY:									'Selectati activitatea!',
		RECORDFORM_OVERTIME_TIME_ERROR:							'Introduceti corect durata',
		RECORDFORM_OVERTIME_TIME_RANGE_EXCEED_ERROR:			'Ati selectat un interval prea mare',
		RECORDFORM_TIME_ERROR:									'Introduceti corect durata',
		RECORDFORM_TIME_RANGE_EXCEED_ERROR:						'Ati selectat un interval prea mare',
		RECORDFORM_TIME_REQUIRED_ERROR:							'Obligatoriu!',
		RECORDFORM_OVERTIME_TIME_REQUIRED_ERROR:				'Obligatoriu!',
		
		// COST SHEET
		COSTSHEETFORM_PROJECT_ERROR:							'Obligatoriu!',
		COSTSHEETFORM_USER_ERROR: 								'Obligatoriu!',
		COSTSHEETFORM_ACTIVITYNAME_ERROR:						'Obligatoriu!',
		COSTSHEETFORM_DATE_ERROR:								'Obligatoriu!',
		COSTSHEETFORM_COST_PRICE_ERROR:							'Obligatoriu!',
		COSTSHEETFORM_COST_PRICE_CURRENCY_ERROR:				'Obligatoriu!',
		COSTSHEETFORM_BILLING_PRICE_CURRENCY_ERROR:				'Obligatoriu!',
		
		//CURRENCY
		CURRENCYFORM_NAME_ERROR:								'Obligatoriu!',
		CURRENCYFORM_INITIALS_ERROR:							'Obligatoriu!',
		CURRENCYFORM_NAME_UNIQUE_ERROR:							'Acest nume exista deja!',
		CURRENCYFORM_INITIALS_UNIQUE_ERROR:						'Aceste initiale exista deja!',
		CURRENCYFORMPANEL_NAME_UNIQUE_ERROR:					'Acest nume exista deja!',
		CURRENCYFORMPANEL_INITIALS_UNIQUE_ERROR:				'Aceste initiale exista deja!',
		//GENERAL ERROR FOR VERIFICATION
		SERVER_ERROR:											'Eroare de Server: Nu s-a putut verifica unicitatea !',
		GENERAL_SERVER_ERROR:									'Eroare de server!',	
		
		// TEAM MEMBER DETAIL 
		TEAMMEMBERDETAILFORM_COSTPRICECURRENCY_ERROR:			'Alegeti moneda!',
		TEAMMEMBERDETAILFORM_COSTTIMEUNIT_ERROR:				'Alegeti unitatea de timp!',
		TEAMMEMBERDETAILFORM_BILLINGPRICECURRENCY_ERROR:		'Alegeti moneda!',
		TEAMMEMBERDETAILFORM_BILLINGTIMEUNIT_ERROR:				'Alegeti unitatea de timp!',
		TEAMMEMBERDETAILFORM_OVERTIMECOSTCURRENCY_ERROR:		'Alegeti moneda!',
		TEAMMEMBERDETAILFORM_OVERTIMECOSTTIMEUNIT_ERROR:		'Alegeti unitatea de timp!',
		TEAMMEMBERDETAILFORM_OVERTIMEBILLINGCURRENCY_ERROR:		'Alegeti moneda!',
		TEAMMEMBERDETAILFORM_OVERTIMEBILLINGTIMEUNIT_ERROR:		'Alegeti unitatea de timp!',
		
		//TIME PANEL TITLE
		TIMEPANELTITLE:											'Timp',
		
		//EXCHANGE
		EXCHANGEFORM_CONFIRM_UPDATE_EXISTING_EXCHANGE:			'Exista un schimb valutar definit pentru cele doua monede. Suprascrieti rata de schimb?',
		EXCHANGEFORM_RATE_ERROR:								'Obligatoriu!',
		EXCHANGEFORM_FIRSTCURRENCY_ERROR:						'Obligatoriu!',
		EXCHANGEFORM_SECONDCURRENCY_ERROR:						'Obligatoriu!',
		EXCHANGEFORM_PROJECT_ERROR:								'Obligatoriu!',
		EXCHANGEFORM_SECONDCURRENCY_EQUAL_FIRSTCURRENCY_ERROR:	'Alegeti doua monede diferite',
			
		//REPORTS
		REPORTTITLE_ERROR:										'Obligatoriu!',	
		REPORTPROJECTFORM_REPORTENDDATE_ERROR:					'Obligatoriu!',
		REPORTPROJECTFORM_REPORTSTARTDATE_ERROR:				'Obligatoriu!',
		REPORTTIMESHEETFORM_REPORTSTARTDATE_ERROR:				'Obligatoriu!',
		REPORTTIMESHEETFORM_REPORTENDDATE_ERROR:				'Obligatoriu!', 
		REPORTTIMESHEETFORM_PERSONSELECT_ERROR:					'Obligatoriu!',
		COLUMNINPUT_RECORDOWNERNAME_ERROR:						'Obligatoriu!',
		COLUMNINPUT_RECORDTIME_ERROR:							'Obligatoriu!',
		COLUMNINPUT_RECORDENDTIME_ERROR:						'Obligatoriu!',
		COLUMNINPUT_RECORDBILLABLE_ERROR:						'Obligatoriu!',
		COLUMNINPUT_RECORDOVERTIMETIME_ERROR:					'Obligatoriu!',
		COLUMNINPUT_RECORDOVERTIMEENDTIME_ERROR:				'Obligatoriu!',		
		COLUMNINPUT_RECORDOVERTIMEBILLABLE_ERROR:				'Obligatoriu!',
		COLUMNINPUT_RECORDACTIVITYNAME_ERROR:					'Obligatoriu!',
		COLUMNINPUT_RECORDSTARTTIME_ERROR:						'Obligatoriu!',
		COLUMNINPUT_RECORDCOSTPRICE_ERROR:						'Obligatoriu!',
		COLUMNINPUT_RECORDBILLINGPRICE_ERROR:					'Obligatoriu!',
		COLUMNINPUT_RECORDOVERTIMESTARTTIME_ERROR:				'Obligatoriu!',
		COLUMNINPUT_RECORDOVERTIMECOSTPRICE_ERROR:				'Obligatoriu!',
		COLUMNINPUT_RECORDOVERTIMEBILLINGPRICE_ERROR:			'Obligatoriu!',
		COLUMNINPUT_COSTSHEETREPORTERNAME_ERROR:				'Obligatoriu!',
		COLUMNINPUT_COSTSHEETACTIVITYNAME_ERROR:				'Obligatoriu!',
		COLUMNINPUT_COSTSHEETDATE_ERROR:						'Obligatoriu!',
		COLUMNINPUT_COSTSHEETCOSTPRICE_ERROR:					'Obligatoriu!',
		COLUMNINPUT_COSTSHEETBILLINGPRICE_ERROR:				'Obligatoriu!',
		COLUMNINPUT_COSTSHEETBILLABLE_ERROR:					'Obligatoriu!',
		TOTALCOSTPRICE_ERROR:									'Obligatoriu!',
		TOTALBILLINGPRICE_ERROR:								'Obligatoriu!',
		
		//NOTIFICATION
		CALENDAR_TITLE:											'Data'
		
  };

