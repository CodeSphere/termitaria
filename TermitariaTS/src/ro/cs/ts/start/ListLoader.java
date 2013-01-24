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
package ro.cs.ts.start;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.common.ConfigParametersProvider;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.nom.IntString;


/**
 * Clasa folosita pentru incarcarea nomenclatoarelor
 *
 * @author matti_joona
 */
public class ListLoader extends ApplicationObjectSupport {

	private static ListLoader theInstance = null;
	
	static {
		theInstance = new ListLoader();
	}
	private ListLoader() {}
	
	public static ListLoader getInstance() {
		return theInstance;
	}
				
	/**
	 * This is a classified list for results per page(search)
	 *
	 * @author alu
	 *
	 */
	public void load_nom_resultsPerPage() {
		try{
			int[] nom = IConstant.RESULTS_PER_PAGE;
         	TSContext.storeOnContext(IConstant.NOM_RESULTS_PER_PAGE, nom);
         	logger.debug(IConstant.NOM_RESULTS_PER_PAGE.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating results per page classified list!", ex);
		}
	}	
	
	/**
	 * This is a classified list for Project's status
	 *
	 * @author Adelina 
	 * 
	 */
	public void load_nom_projectStatus() {
		try{
			List<IntString> nom = new ArrayList<IntString>(3);
			IntString el = new IntString(IConstant.NOM_PROJECT_STATUS_OPENED, IConstant.NOM_PROJECT_STATUS_OPENED_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_PROJECT_STATUS_CLOSED, IConstant.NOM_PROJECT_STATUS_CLOSED_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_PROJECT_STATUS_ABORTED, IConstant.NOM_PROJECT_STATUS_ABORTED_LABEL_KEY);
			nom.add(el);
			TSContext.storeOnContext(IConstant.NOM_PROJECT_STATUS, nom);
			logger.debug(IConstant.NOM_PROJECT_STATUS.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating Project's Status classified list!", ex);
		}
	}
	
	public void load_nom_billable() {
		try {
			JSONArray nom = new JSONArray();
			
			JSONObject billable = new JSONObject();
			billable.accumulate("value", IConstant.NOM_BILLABLE_YES);
			billable.accumulate("label", IConstant.NOM_BILLABLE_YES_LABEL_KEY);
			nom.add(billable);
			
			billable = new JSONObject();
			billable.accumulate("value", IConstant.NOM_BILLABLE_NO);
			billable.accumulate("label", IConstant.NOM_BILLABLE_NO_LABEL_KEY);
			nom.add(billable);
			
			TSContext.storeOnContext(IConstant.NOM_BILLABLE, nom);
			logger.debug(IConstant.NOM_BILLABLE.concat(" LOADED"));
		} catch (Exception e) {
			logger.error("Error at creating billable clasified list", e);
		}
	}
	
	/**
	 * This is a classified list for Time Unit
	 *
	 * @author Adelina 
	 * 
	 */
	public void load_nom_timeUnit() {
		try{
			List<IntString> nom = new ArrayList<IntString>(5);
			IntString el = new IntString(IConstant.NOM_TIME_UNIT_HOUR, IConstant.NOM_TIME_UNIT_HOUR_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_TIME_UNIT_DAY, IConstant.NOM_TIME_UNIT_DAY_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_TIME_UNIT_WEEK, IConstant.NOM_TIME_UNIT_WEEK_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_TIME_UNIT_MONTH, IConstant.NOM_TIME_UNIT_MONTH_LABEL_KEY);
			nom.add(el);
			TSContext.storeOnContext(IConstant.NOM_TIME_UNIT, nom);
			logger.debug(IConstant.NOM_TIME_UNIT.concat(" LOADED"));
		} catch(Exception ex){
			logger.error("Error at creating Project's Status classified list!", ex);
		}		
	}
	
	/**
	 * Looks in the configuration file and loads the supported language codes
	 */
	
	public void load_nom_supportedLanguages(){
		try{
		List<String> languagesList = new ArrayList<String>();
		int totalLanguages= Integer.parseInt(ConfigParametersProvider.getConfigString(IConstant.SUPPORTED_LANGUAGE_TOTAL_KEY));
		
		String language = null;		
		for (int i=1; i<=totalLanguages;i++){
			language = ConfigParametersProvider.getConfigString(IConstant.LANGUAGE_CODE_KEY_PREFIX.concat(""+i));
			logger.debug("Supported language: "+language);
			languagesList.add(language.toUpperCase());
		} 
		
		TSContext.storeOnContext(IConstant.NOM_SUPPORTED_LANGUAGES, languagesList);
		logger.debug(IConstant.NOM_SUPPORTED_LANGUAGES.concat(" LOADED"));
		} catch (Exception e) {
			logger.error("Error at creating supported languages list", e);
		}
		
	}
	
	/**
	 * This loads the possible subtotal intervals for a report
	 *
	 * @author alexandru.dobre
	 * 
	 */
	
	public void load_nom_reportSubtotalInterval(){
		
		try{
			List<IntString> nom = new ArrayList<IntString>(5);
			IntString el = new IntString(IConstant.NOM_SUBTOTAL_NONE, IConstant.NOM_SUBTOTAL_NONE_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_SUBTOTAL_DAILY, IConstant.NOM_SUBTOTAL_DAILY_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_SUBTOTAL_WEEKLY, IConstant.NOM_SUBTOTAL_WEEKLY_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_SUBTOTAL_MONTHLY, IConstant.NOM_SUBTOTAL_MONTHLY_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_SUBTOTAL_QUARTERLY, IConstant.NOM_SUBTOTAL_QUARTERLY_LABEL_KEY);
			nom.add(el);
			TSContext.storeOnContext(IConstant.NOM_SUBTOTAL_PERIOD, nom);
			logger.debug(IConstant.NOM_SUBTOTAL_PERIOD.concat(" LOADED"));
			
		} catch (Exception ex){
			logger.error("Error creating report subtotal interval list", ex);
		}
		
	}
	
	/**
	 * This loads the total price compute types
	 */
	
	public void load_nom_pricesComputeType(){
		
		try{
			List<IntString> nom = new ArrayList<IntString>(2);
			IntString el = new IntString(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY, IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE, IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE_LABEL_KEY);
			nom.add(el);
			
			TSContext.storeOnContext(IConstant.NOM_PRICES_COMPUTE_TYPES, nom);
			logger.debug(IConstant.NOM_PRICES_COMPUTE_TYPES.concat(" LOADED"));
			
		} catch (Exception ex){
			logger.error("Error creating prices compute type list", ex);
		}
		
	}

}
