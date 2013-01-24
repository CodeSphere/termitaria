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
package ro.cs.ts.utils.messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.cs.ts.common.ConfigParametersProvider;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.TextTable;
import ro.cs.ts.ws.client.reports.entity.ReportParams;

/**
 * Provides methods to handle the reports columns default values specified in resource files
 * @author alexandru.dobre
 *
 */
public class ReportsMessageTools {
	
	static Log logger = LogFactory.getLog(ReportsMessageTools.class);
	
	private static final String BUNDLE_NAME = "config.messages";
	
	//-------------------------------------- PROJECT REPORT ----------------------------------
	private static final String REPORT_PROJECT_KEY_PREFIX = "report.project.default.";
	private static final String REPORT_PROJECT_KEY_ARRAY_KEY ="report.project.key.array";
	private static final String REPORT_PROJECT_RECORD_TABLE_KEYS ="report.project.record.table.keys";
	private static final String REPORT_PROJECT_COST_TABLE_KEYS ="report.project.cost.table.keys";
	
	//-------------------------------------- TIME SHEET REPORT -------------------------------
	private static final String REPORT_TIME_SHEET_KEY_PREFIX = "report.time.sheet.default.";
	
	private static ResourceBundle RESOURCE_BUNDLE = null;
	
	
	/**
	 * Retrieves the default parameters key list from the constants class 
	 * for the project report
	 * @return 
	 */
	public static String[] getReportProjectKeyArray(){
		return IConstant.REPORT_PROJECT_KEY_ARRAY;
	}
	
	/**
	 * Provides the subset of keys from the getReportProjectKeyArray that are for the Record columns
	 * @return
	 */
	public static List<String> getRecordColumnsKeyList(){
		
		
		int startIndex = IConstant.REPORT_PROJECT_RECORD_TABLE_KEYS_START;
		int endIndex = IConstant.REPORT_PROJECT_RECORD_TABLE_KEYS_END;
		
		String[] reportKeyAraray = getReportProjectKeyArray();
		List<String> recordColumnSubset = new ArrayList<String>();
		for (int i=startIndex; i <= endIndex; i++){
			recordColumnSubset.add(reportKeyAraray[i]);
			//System.out.println("Added: "+ reportKeyAraray[i]);
		}
		
		return recordColumnSubset;
	}
	
	/**
	 * Provides the subset of keys for the default present record columns
	 */
	public static List<String> getDefaultPresentRecordColumnsKeyList() {
		List<String> keys = (List<String>) Arrays.asList(IConstant.REPORT_PROJECT_PRESENT_RECORDS_KEY_ARRAY);
		return keys;
	}
	
	/**
	 * Provides the subset of keys for the default present record columns
	 */
	public static List<String> getDefaultAbsentRecordColumnsKeyList() {
		List<String> presentKeys = (List<String>) Arrays.asList(IConstant.REPORT_PROJECT_PRESENT_RECORDS_KEY_ARRAY);
		List<String> absentKeys = new ArrayList<String>();
		List<String> allKeys = getRecordColumnsKeyList();
		for (String key : allKeys) {
			if (!presentKeys.contains(key)) {
				absentKeys.add(key);
			}
		}
		return absentKeys;
	}
	
	public static List<String> getCostColumnsKeyList(){

		int startIndex = IConstant.REPORT_PROJECT_COST_TABLE_KEYS_START;
		int endIndex = IConstant.REPORT_PROJECT_COST_TABLE_KEYS_END;
		
		String[] reportKeyAraray = getReportProjectKeyArray();
		List<String> projectColumnSubset = new ArrayList<String>();
		for (int i=startIndex; i <= endIndex; i++){
			projectColumnSubset.add(reportKeyAraray[i]);
			//System.out.println("Added: "+ reportKeyAraray[i]);
		}
		
		return projectColumnSubset;
		
	}
	
	/**
	 * Provides the subset of keys for the default present cost sheet columns
	 */
	public static List<String> getDefaultPresentCostSheetColumnsKeyList() {
		List<String> keys = (List<String>) Arrays.asList(IConstant.REPORT_PROJECT_PRESENT_COST_SHEETS_KEY_ARRAY);
		return keys;
	}
	
	/**
	 * Provides the subset of keys for the default present cost sheet columns
	 */
	public static List<String> getDefaultAbsentCostSheetColumnsKeyList() {
		List<String> presentKeys = (List<String>) Arrays.asList(IConstant.REPORT_PROJECT_PRESENT_COST_SHEETS_KEY_ARRAY);
		List<String> absentKeys = new ArrayList<String>();
		List<String> allKeys = getCostColumnsKeyList();
		for (String key : allKeys) {
			if (!presentKeys.contains(key)) {
				absentKeys.add(key);
			}
		}
		return absentKeys;
	}
	
	/**
	 * Retrieves the subset of keys for the record columns in the time sheet report
	 * @return
	 */
	public static List<String> getReportTimeSheetRecordColumnsKeyList() {
		String[] reportParamsKeyArray = IConstant.TIME_SHEET_REPORT_PARAMS_KEY_ARRAY;
		int recordTableColumnsStartIndex = IConstant.TIME_SHEET_REPORT_RECORD_TABLE_START_INDEX;
		List<String> recordColumnsKeyList = new ArrayList<String>();
		for (int i = recordTableColumnsStartIndex; i < reportParamsKeyArray.length; i++) {
			recordColumnsKeyList.add(reportParamsKeyArray[i]);
		}
		return recordColumnsKeyList;
	}

	/**
	 * Provides the subset of keys for the default present cost sheet columns
	 */
	public static List<String> getTSReportDefaultPresentRecordColumnsKeyList() {
		List<String> keys = (List<String>) Arrays.asList(IConstant.TIME_SHEET_REPORT_PRESENT_RECORDS_KEY_ARRAY);
		return keys;
	}
	
	/**
	 * Provides the subset of keys for the default present cost sheet columns
	 */
	public static List<String> getTSReportAbsentRecordColumnsKeyList() {
		List<String> presentKeys = (List<String>) Arrays.asList(IConstant.TIME_SHEET_REPORT_PRESENT_RECORDS_KEY_ARRAY);
		List<String> absentKeys = new ArrayList<String>();
		List<String> allKeys = getReportTimeSheetRecordColumnsKeyList();
		for (String key : allKeys) {
			if (!presentKeys.contains(key)) {
				absentKeys.add(key);
			}
		}
		return absentKeys;
	}
	
	/**
	 * Retrieves from the messages file all the default values for the Project report
	 * for the given language	
	 * @param language the ISO language code  
	 * @return the keys are retreved from the configuration file, the values are from the message file
	 */
	public static Map<String,String> getReportProjectDefaultValues(String language){
		return getDefaultValues(REPORT_PROJECT_KEY_PREFIX, IConstant.REPORT_PROJECT_KEY_ARRAY, language.toLowerCase());
	}
	
	/**
	 * Retrieves from the messages file all the default values for the Time Sheet report
	 * for the given language	
	 * @param language the ISO language code  
	 * @return the keys are retrieved from IConstant, the values are from the messages files
	 */
	public static Map<String,String> getReportTimeSheetDefaultValues(String language){
		return getDefaultValues(REPORT_TIME_SHEET_KEY_PREFIX, IConstant.TIME_SHEET_REPORT_PARAMS_KEY_ARRAY, language.toLowerCase());
	}
	
	/**
	 * Looks inside the resource bundle to 
	 * @param keyPrefix the key prefix used by all values in the configuration file that are to be retrieved
	 * @param keyEndArray the key sufixes used by all values in the configuration file that are to be retrieved
	 * @param language the ISO language code 
	 * @return
	 */
	public static Map<String,String> getDefaultValues (String keyPrefix,String[] keyEndArray,String language){
		
		Map<String,String> defalutValues = new HashMap<String,String> ();
		logger.debug("getDefaultValues START>");
		
		RESOURCE_BUNDLE= ResourceBundle.getBundle(BUNDLE_NAME, new Locale(language));
		
		
		String completeKey = null;
		String value = null;
		for (String keyEnd : keyEndArray ){
			completeKey = keyPrefix.concat(keyEnd.trim());
			value = RESOURCE_BUNDLE.getString(completeKey);
			//logger.debug("Adding default value: "+keyEnd+"_"+value);
			defalutValues.put(keyEnd.trim(), value);
		}
		logger.debug("getDefaultValues <END ");
		
		return defalutValues;
			
	}
	
	
	public static String viewReportParams (ReportParams reportParams){
		
		StringBuffer sb = new StringBuffer("Viewing report params:\n");
		if (reportParams == null || reportParams.getProperties().isEmpty()) return sb.toString()+" Empty";
		
		TextTable ttParams = new TextTable("Key#Value#Type", TextTable.ALIGN_CENTER);
			
		
		for (Entry<String,Object> entry : reportParams.getProperties().entrySet()){
			ttParams.addRow(entry.getKey().concat("#").concat(""+entry.getValue()).concat("#").concat(reportParams.getPropertiesTypes().get(entry.getKey())), TextTable.ALIGN_LEFT);
			//sb.append("key: "+entry.getKey()+" value: "+entry.getValue()+" type: "+reportParams.getPropertiesTypes().get(entry.getKey())+"\n");
		}
		sb.append(ttParams.getTable());
		return sb.toString();
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testing method
		//getDefaultValues(REPORT_PROJECT_KEY_PREFIX, getKeyEndArray(REPORT_PROJECT_KEY_ARRAY_KEY), "EN");
		getReportProjectDefaultValues("EN");
		//getRecordColumnsKeyList();
		//getCostColumnsKeyList();
	}

}
