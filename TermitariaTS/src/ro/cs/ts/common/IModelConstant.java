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
package ro.cs.ts.common;

/**
 * User for constant of the model layer
 * 
 * @author coni
 * @author Adelina 
 * 
 */

public interface IModelConstant {
	//Hibernate entities'name
	
	//---------------------------------------- ACTIVITY ---------------------------------------
	String activityEntity													= "Activity";
	String activitySimpleEntity												= "ActivitySimple";
	String activityForListingEntity											= "ActivityForListing";
	String activityWithRecordsEntity										= "ActivityWithRecords";	
	String activityWithCurrenciesEntity										= "ActivityWithCurrencies";	
	String activityForUpdateEntity											= "ActivityForUpdate";
	
	//---------------------------------------- RECORD -----------------------------------------
	String recordEntity														= "Record";
	String recordSimpleEntity												= "RecordSimple";
	String recordForListingEntity											= "RecordForListing";
	String recordWithTeamMemberDetailEntity									= "RecordWithTeamMemberDetail";
	String recordWithPersonDetailEntity										= "RecordWithPersonDetail";
	String recordWithProjectAndTeamMemberDetailsEntity						= "RecordWithProjectAndTeamMemberDetails";
	String recordWithProjectDetailsEntity									= "RecordWithProjectDetails";
	String recordForReportsEntity											= "RecordForReports";
	String recordForUpdateEntity											= "RecordForUpdate";
	String recordForPriceCalculation										= "RecordForPriceCalculation";
	
	//---------------------------------------- PROJECT DETAILS --------------------------------
	String projectDetailsSimpleEntity										= "ProjectDetailsSimple";
	String projectDetailsEntity												= "ProjectDetails";
	String projectDetailsForUpdateEntity									= "ProjectDetailsForUpdate";
	String projectDetailsSimpleWithBudgetCurrencyId							= "ProjectDetailsSimpleWithBudgetCurrencyId";
	String projectDetailsUpdateNotificationStatus							= "ProjectDetailsUpdateNotificationStatus";
	
	//---------------------------------------- TEAM MEMBER DETAIL -----------------------------
	String teamMemberDetailSimpleEntity										= "TeamMemberDetailSimple";
	String teamMemberDetailEntity											= "TeamMemberDetail";
	String teamMemberDetailWithCurrenciesEntity								= "TeamMemberDetailWithCurrencies";
	String teamMemberDetailWithAllEntity									= "TeamMemberDetailWithAll";
	String teamMemberDetailForUpdateEntity									= "TeamMemberDetailForUpdate";
	
	//---------------------------------------- PERSON DETAIL ----------------------------------
	String personDetailSimpleEntity											= "PersonDetailSimple";
	String personDetailEntity												= "PersonDetail";
	String personDetailWithCurrencyEntity									= "PersonDetailWithCurrency";
	String personDetailForUpdateEntity										= "PersonDetailForUpdate";
	
	//---------------------------------------- NOTIFICATION -----------------------------------
	String notificationEntity												= "Notification";
	String notificationId													= "notificationId";
	
	//---------------------------------------- NOTIFICATION SETTING ---------------------------
	String notificationSettingEntity										= "NotificationSetting";
	String notificationSettingsId											= "notificationSettingsId";
	
	//---------------------------------------- CURRENCY -----------------------------------
	String currencyEntity													= "Currency";
	String currencyForUpdateEntity											= "CurrencyForUpdate";
	
	//---------------------------------------- COST SHEET ------------------------------------
	String costSheetEntity													= "CostSheet";
	String costSheetForListingEntity										= "CostSheetForListing";
	String costSheetWithTeamMemberDetailEntity								= "CostSheetWithTeamMemberDetail";
	String costSheetWithPersonDetailEntity									= "CostSheetWithPersonDetail";
	String costSheetWithProjectDetailsEntity								= "CostSheetWithProjectDetails";
	String costSheetWithProjectAndTeamMemberDetailsEntity					= "CostSheetWithProjectAndTeamMemberDetails";
	String costSheetSimpleEntity											= "CostSheetSimple";
	String costSheetWithCurrenciesEntity									= "CostSheetWithCurrencies";
	String costSheetWithAllEntity											= "CostSheetWithAll";
	String costSheetForReportsEntity										= "CostSheetForReports";
	String costSheetForUpdateEntity											= "CostSheetForUpdate";
	
	//---------------------------------------- EXCHANGE --------------------------------------
	String exchangeEntity													= "Exchange";
	String exchangeSimpleEntity												= "ExchangeSimple";
	String exchangeForListingEntity											= "ExchangeForListing";
	String exchangeForDeleteEntity											= "ExchangeForDelete";
	String exchangeForUpdate												= "ExchangeForUpdate";	
	
	//---------------------------------------- WIDGET SESSION --------------------------------------
	String widgetSessionEntity											= "WidgetSession";
	String widgetSessionSimpleEntity									= "WidgetSessionSimple";
	String widgetSessionForListingEntity								= "WidgetSessionForListing";
	String widgetSessionForDeleteEntity									= "WidgetSessionForDelete";
	String widgetSessionForUpdate										= "WidgetSessionForUpdate";
		
	//---------------------------------------- RECORD SESSION --------------------------------------
	String recordSessionEntity											= "RecordSession";
	String recordSessionSimpleEntity									= "RecordSessionSimple";
	String recordSessionForListingEntity								= "RecordSessionForListing";
	String recordSessionForDeleteEntity									= "RecordSessionForDelete";
	String recordSessionForUpdate										= "RecordSessionForUpdate";
	
}
