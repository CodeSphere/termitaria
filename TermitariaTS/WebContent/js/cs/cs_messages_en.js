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
var language_en = {
	
		YES:													 'Yes',
		NO:													 	 'No',
		
		LOCALE:													 'EN',
		//------------------------------- Validate messages that are displayed using internationalisation!--------------------------- 
		 
		//YUI TABLE 
		NO_RECORDS:												'No records found!',
		
		//ADD QUESTION
		CONFIRM_ADD:											'You will lose any unsaved data. Proceed?',
		
		// ACTIVITY
		
		ACTIVITYFORM_NAME_ERROR:								'Required!',
		ACTIVITYFORM_PROJECT_ERROR:								'Choose a project!',
		ACTIVITYFORM_COSTPRICECURRENCY_ERROR:					'Choose currency!',
		ACTIVITYFORM_COSTTIMEUNIT_ERROR:						'Choose a time unit!',
		ACTIVITYFORM_BILLINGPRICECURRENCY_ERROR:				'Choose currency!',
		ACTIVITYFORM_BILLINGTIMEUNIT_ERROR:						'Choose a time unit!',
		
		ACTIVITYFORMPANEL_NAME_ERROR:							'Required!',
		ACTIVITYFORMPANEL_PROJECT_ERROR:						'Choose a project!',
		ACTIVITYFORMPANEL_COSTPRICECURRENCY_ERROR:				'Choose currency!',
		ACTIVITYFORMPANEL_COSTTIMEUNIT_ERROR:					'Choose a time unit!',
		ACTIVITYFORMPANEL_BILLINGPRICECURRENCY_ERROR:			'Choose currency!',
		ACTIVITYFORMPANEL_BILLINGTIMEUNIT_ERROR:				'Choose a time unit!',
		
		// PROJECT DETAIL
	  	
		PROJECTDETAILSFORM_BUDGETCURRENCY_ERROR:				'Choose currency!',
		
		// RECORD
		
		RECORDFORM_PROJECT_ERROR:								'Required!',
		RECORDFORM_ACTIVITY_ERROR:								'Required!',
		RECORDFORM_USER_ERROR:									'Required!',
		RECORDFORM_TITLE_ERROR:									'Required!',
		RECORDFORM_DESCRIPTION_ERROR:							'Required!',
		RECORDFORM_TIME_CHECK_AT_LEAST_ONE_ERROR:				'Work hours or overtime must be specified', 
		RECORDFORM_START_TIME_ERROR:							'Required!',
		RECORDFORM_END_TIME_ERROR:								'Required!',
		RECORDFORM_OVERTIME_START_TIME_ERROR:					'Required!',		
		RECORDFORM_OVERTIME_END_TIME_ERROR:						'Required!',
		RECORD_CHOOSE_PROJECT:									'Select project/organization!',
		RECORD_CHOOSE_ACTIVITY:									'Select activity!',
		RECORDFORM_OVERTIME_TIME_ERROR:							'Enter the correct time',
		RECORDFORM_OVERTIME_TIME_RANGE_EXCEED_ERROR:			'You have selected a range too high',
		RECORDFORM_TIME_ERROR:									'Enter the correct time',
		RECORDFORM_TIME_RANGE_EXCEED_ERROR:						'You have selected a range too high',		
		RECORDFORM_TIME_REQUIRED_ERROR:							'Required!',
		RECORDFORM_OVERTIME_TIME_REQUIRED_ERROR:				'Required!',
		
		// COST SHEET
		COSTSHEETFORM_PROJECT_ERROR:							'Required!',
		COSTSHEETFORM_USER_ERROR: 								'Required!',
		COSTSHEETFORM_ACTIVITYNAME_ERROR:						'Required!',
		COSTSHEETFORM_DATE_ERROR:								'Required!',
		COSTSHEETFORM_COST_PRICE_ERROR:							'Required!',
		COSTSHEETFORM_COST_PRICE_CURRENCY_ERROR:				'Required!',
		COSTSHEETFORM_BILLING_PRICE_CURRENCY_ERROR:				'Required!',
		
		//CURRENCY
		CURRENCYFORM_NAME_ERROR:								'Required!',
		CURRENCYFORM_INITIALS_ERROR:							'Required!',
		CURRENCYFORM_NAME_UNIQUE_ERROR:							'This name is already in use!',
		CURRENCYFORM_INITIALS_UNIQUE_ERROR:						'These initials are already in use!',  	
		CURRENCYFORMPANEL_NAME_UNIQUE_ERROR:					'This name is already in use!',
		CURRENCYFORMPANEL_INITIALS_UNIQUE_ERROR:				'These initials are already in use!',
		//GENERAL ERROR FOR VERIFICATION
		SERVER_ERROR:											'Server error: Could not verify uniquness !',
		GENERAL_SERVER_ERROR:									'Server error!',	
		
		// TEAM MEMBER DETAIL 
		TEAMMEMBERDETAILFORM_COSTPRICECURRENCY_ERROR:			'Choose currency!',
		TEAMMEMBERDETAILFORM_COSTTIMEUNIT_ERROR:				'Choose a time unit!',
		TEAMMEMBERDETAILFORM_BILLINGPRICECURRENCY_ERROR:		'Choose currency!',
		TEAMMEMBERDETAILFORM_BILLINGTIMEUNIT_ERROR:				'Choose a time unit!',
		TEAMMEMBERDETAILFORM_OVERTIMECOSTCURRENCY_ERROR:		'Choose currency!',
		TEAMMEMBERDETAILFORM_OVERTIMECOSTTIMEUNIT_ERROR:		'Choose a time unit!',
		TEAMMEMBERDETAILFORM_OVERTIMEBILLINGCURRENCY_ERROR:		'Choose currency!',
		TEAMMEMBERDETAILFORM_OVERTIMEBILLINGTIMEUNIT_ERROR:		'Choose a time unit!',
		
		//EXCHANGE
		EXCHANGEFORM_CONFIRM_UPDATE_EXISTING_EXCHANGE:			'There is an exchange already defined for the selected currencies. Do you want to overwrite the rate?',
		EXCHANGEFORM_RATE_ERROR:								'Required!',
		EXCHANGEFORM_FIRSTCURRENCY_ERROR:						'Required!',
		EXCHANGEFORM_SECONDCURRENCY_ERROR:						'Required!',
		EXCHANGEFORM_PROJECT_ERROR:								'Required!',
		EXCHANGEFORM_SECONDCURRENCY_EQUAL_FIRSTCURRENCY_ERROR:	'Select two distinct currencies',
		
		//TIME PANEL TITLE
		TIMEPANELTITLE:											'Time',
		
		//REPORTS
		REPORTTITLE_ERROR:										'Required!',	
		REPORTPROJECTFORM_REPORTENDDATE_ERROR:					'Required!',
		REPORTPROJECTFORM_REPORTSTARTDATE_ERROR:				'Required!',
		REPORTTIMESHEETFORM_REPORTSTARTDATE_ERROR:				'Required!',
		REPORTTIMESHEETFORM_REPORTENDDATE_ERROR:				'Required!', 
		REPORTTIMESHEETFORM_PERSONSELECT_ERROR:					'Required!',
		COLUMNINPUT_RECORDOWNERNAME_ERROR:						'Required!',
		COLUMNINPUT_RECORDTIME_ERROR:							'Required!',
		COLUMNINPUT_RECORDENDTIME_ERROR:						'Required!',
		COLUMNINPUT_RECORDBILLABLE_ERROR:						'Required!',
		COLUMNINPUT_RECORDOVERTIMETIME_ERROR:					'Required!',
		COLUMNINPUT_RECORDOVERTIMEENDTIME_ERROR:				'Required!',		
		COLUMNINPUT_RECORDOVERTIMEBILLABLE_ERROR:				'Required!',
		COLUMNINPUT_RECORDACTIVITYNAME_ERROR:					'Required!',
		COLUMNINPUT_RECORDSTARTTIME_ERROR:						'Required!',
		COLUMNINPUT_RECORDCOSTPRICE_ERROR:						'Required!',
		COLUMNINPUT_RECORDBILLINGPRICE_ERROR:					'Required!',
		COLUMNINPUT_RECORDOVERTIMESTARTTIME_ERROR:				'Required!',
		COLUMNINPUT_RECORDOVERTIMECOSTPRICE_ERROR:				'Required!',
		COLUMNINPUT_RECORDOVERTIMEBILLINGPRICE_ERROR:			'Required!',
		COLUMNINPUT_COSTSHEETREPORTERNAME_ERROR:				'Required!',
		COLUMNINPUT_COSTSHEETACTIVITYNAME_ERROR:				'Required!',
		COLUMNINPUT_COSTSHEETDATE_ERROR:						'Required!',
		COLUMNINPUT_COSTSHEETCOSTPRICE_ERROR:					'Required!',
		COLUMNINPUT_COSTSHEETBILLINGPRICE_ERROR:				'Required!',
		COLUMNINPUT_COSTSHEETBILLABLE_ERROR:					'Required!',
		TOTALCOSTPRICE_ERROR:									'Required!',
		TOTALBILLINGPRICE_ERROR:								'Required!',
		
		//NOTIFICATION
		CALENDAR_TITLE:											'Data'
	
  };
	



	
