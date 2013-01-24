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
package ro.cs.ts.web.controller.form;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.ConfigParametersProvider;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.ReportProjectCriteria;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.utils.messages.ReportsMessageTools;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.reports.entity.ReportParams;

/**
 * Responsible for the Project Report view
 * @author alexandru.dobre
 *
 */

public class ReportProjectController extends RootSimpleFormController{
	
	private static final String SUCCESS_VIEW 				= "Reports_Project_Succes";
	private static final String FORM_VIEW 					= "Reports_Project";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY					= "report.project.";		
	private static final String GENERAL_ERROR				= ROOT_KEY.concat("general.error");	
	private static final String PROJECT_LIST_ERROR 			= ROOT_KEY.concat("project.list.error");
	private static final String GET_ORG_CURRENCIES_ERROR    = ROOT_KEY.concat("get.currency.list.error");
	private static final String CREATE_ERROR 				= ROOT_KEY.concat("create.error");
	
	private static final String REPORT_CURRENCY_EMPTY		= "report.currency.empty";
	private static final String REPORT_PROJECT_EMPTY		= "report.project.empty";

	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String AVAILABLE_PROJECTS 				= "AVAILABLE_PROJECTS";
	private static final String TODAY_DATE						= "TODAY_DATE";
	private static final String ORG_CURRENCIES					= "ORG_CURRENCIES";
	private static final String DEFAULT_VALUES_JSON				= "DEFAULT_VALUES_JSON";
	private static final String RECORD_COLUMNS_KEY_LIST			= "RECORD_COLUMNS_KEY_LIST";
	private static final String PRESENT_RECORD_COLUMNS_KEY_LIST	= "PRESENT_RECORD_COLUMNS_KEY_LIST";
	private static final String ABSENT_RECORD_COLUMNS_KEY_LIST	= "ABSENT_RECORD_COLUMNS_KEY_LIST";
	private static final String COST_COLUMNS_KEY_LIST			= "COST_COLUMNS_KEY_LIST";
	private static final String PRESENT_COST_COLUMNS_KEY_LIST	= "PRESENT_COST_COLUMNS_KEY_LIST";	
	private static final String ABSENT_COST_COLUMNS_KEY_LIST	= "ABSENT_COST_COLUMNS_KEY_LIST";	
	private static final String IS_PM_FOR_AT_LEAST_ONE_PROJECT 	= "IS_PM_FOR_AT_LEAST_ONE_PROJECT";
	private static final String REQUEST_URL 					= "REQUEST_URL";
	private static final String REPORT_SERVLET_URL_VALUE 		= ConfigParametersProvider.getConfigString("report.servlet.url");
	private static final String REPORT_SERVLET_URL 				= "REPORT_SERVLET_URL";
	private static final String FORMAT 							= "FORMAT";
	private static final String ATTACHMENT 						= "attachment";
	
	
	
	
	// Time unit property nomenclator
	String NOM_TIME_UNIT									= "NOM_TIME_UNIT";
	
	
	
    public ReportProjectController() {
		
		setCommandName("reportProjectCriteria");
		setCommandClass(ReportProjectCriteria.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
    
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(sdf, true));
		
        logger.debug("initBinder - END");
	}
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
    		throws Exception {
    	logger.debug("formBackingObject - START");
    	ReportProjectCriteria repCriteria = new ReportProjectCriteria();
    	
    	logger.debug("formBackingObject - END");
    	return repCriteria;
    }
    
   /**
    * It builds the available projects list gets the available languages and 
    */
    @SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
    	
    	logger.debug("referenceData - START");
       	
    	UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	ReportProjectCriteria criteria = (ReportProjectCriteria)command;
    	
    	Map map = new HashMap();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		ArrayList<String> warningMessages 	= new ArrayList<String>();
		
		List<Project> projectList = buildAuthorizedProjectList(request, errorMessages, warningMessages);
				
		if (projectList != null && projectList.size() > 0) {
			//we set default values for the project
			if(criteria.getProjectName() == null || criteria.getProjectName().isEmpty() ){
				criteria.setProjectName(projectList.get(0).getName());
				criteria.setProjectId(projectList.get(0).getProjectId());
			}		
		} 
		
		map.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
		map.put(AVAILABLE_PROJECTS, projectList);
		// adding to model the subtotal time period
		map.put(IConstant.NOM_SUBTOTAL_PERIOD, TSContext.getFromContext(IConstant.NOM_SUBTOTAL_PERIOD));
		map.put(IConstant.NOM_BILLABLE, TSContext.getFromContext(IConstant.NOM_BILLABLE));
		
		//adding the report file format types
		map.put(IConstant.NOM_REPORT_FORMATS, IConstant.REPORT_FORMATS);
		//adding supported languages
		map.put(IConstant.NOM_SUPPORTED_LANGUAGES, TSContext.getFromContext(IConstant.NOM_SUPPORTED_LANGUAGES));
		
		//price compute type
		map.put(IConstant.NOM_PRICES_COMPUTE_TYPES, TSContext.getFromContext(IConstant.NOM_PRICES_COMPUTE_TYPES));
		
		//adding default values JSON
		map.put(DEFAULT_VALUES_JSON, buildDefaultValuesJSON().toString());
		
		//adding record column keys and expense column keys
		map.put(RECORD_COLUMNS_KEY_LIST, ReportsMessageTools.getRecordColumnsKeyList());
		map.put(PRESENT_RECORD_COLUMNS_KEY_LIST, ReportsMessageTools.getDefaultPresentRecordColumnsKeyList());
		map.put(ABSENT_RECORD_COLUMNS_KEY_LIST, ReportsMessageTools.getDefaultAbsentRecordColumnsKeyList());
		map.put(COST_COLUMNS_KEY_LIST, ReportsMessageTools.getCostColumnsKeyList());
		map.put(PRESENT_COST_COLUMNS_KEY_LIST, ReportsMessageTools.getDefaultPresentCostSheetColumnsKeyList());
		map.put(ABSENT_COST_COLUMNS_KEY_LIST, ReportsMessageTools.getDefaultAbsentCostSheetColumnsKeyList());
		
		//add the report servlet url 
		map.put(REPORT_SERVLET_URL, REPORT_SERVLET_URL_VALUE);
		
		if (projectList != null && projectList.size() > 0) {
			map.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, true);
		} else {
			map.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, false);
		}		
		
		try {
			//add the organization available currencies
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			//currencies = null;
			if (currencies != null && !currencies.isEmpty()) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				if (criteria.getCurrencyName()== null || criteria.getCurrencyName().isEmpty()){
					criteria.setCurrencyName(currencies.get(0).getName());
					criteria.setCurrencyId(currencies.get(0).getCurrencyId());
				}
				for (Currency currency : currencies) {
					IntString entry = new IntString();
					entry.setValue(currency.getCurrencyId());
					entry.setLabel(currency.getName());
					nomCurrencies.add(entry);
				}
				map.put(ORG_CURRENCIES, nomCurrencies);
			}else {
				warningMessages.add(messageSource.getMessage(REPORT_CURRENCY_EMPTY, null, RequestContextUtils.getLocale(request)));
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);			
			errorMessages.add(messageSource.getMessage(GET_ORG_CURRENCIES_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		setWarnings(request, warningMessages);
		
		logger.debug("referenceData - END");
    	return map;
    }
    
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	
    	logger.debug("onSubmit - START");
    	logger.debug("onSubmit(command) command: " + command);  	
    	ModelAndView mav = new ModelAndView(getSuccessView());
    	
		ArrayList<String> errorMessages  	= new ArrayList<String>();
    	
		try {
			ReportProjectCriteria criteria = (ReportProjectCriteria) command;

			logger.debug("*********************Building report project criteria******************************");
			populateCriteria(request, criteria);
			logger.debug("populated criteria: " + criteria);
			
			//we build the web service params based on the command
			ReportParams reportParams = buildReportParams (criteria,request);
			
			
			//we put the report params on the context with the UID
			Map<String,ReportParams> reportParamsMap = (Map<String,ReportParams>) TSContext.getFromContext(IConstant.REPORT_PARAM_MAP);
			reportParamsMap.put(criteria.getUniqueId(), reportParams);
			
			//if the format is not HTML or PDF we stop here
			if (!criteria.getReportFileType().equalsIgnoreCase("HTML") && !criteria.getReportFileType().equalsIgnoreCase("PDF")){
				return null;
			}
			
			//we build the servlet request
			String reportServletRequest = REPORT_SERVLET_URL_VALUE.concat("?").concat(IConstant.REPORT_UID).concat("=").concat(criteria.getUniqueId())
																			  .concat("&").concat(IConstant.REPORT_TYPE).concat("=").concat(IConstant.REPORT_TYPE_PROJECT);
			logger.debug("Report servlet request: "+reportServletRequest);

			mav.addObject(REQUEST_URL, reportServletRequest);
			mav.addObject(FORMAT, criteria.getReportFileType().toLowerCase());
		} catch (Exception exc) {
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES_NEW_WINDOW);
			logger.error("", exc);
			errorMessages.add(messageSource.getMessage(CREATE_ERROR, new Object[] {ICodeException.REPORT_PROJECT_CREATE, ControllerUtils.getInstance().getFormattedCurrentTime()}
			, RequestContextUtils.getLocale(request)));
		}
    	
    	//Publish Info/Error messages
		setErrors(request, errorMessages);
    	
    	logger.debug("onSubmit - END");
    	return mav;
    }
    
    private List <Project> buildAuthorizedProjectList (HttpServletRequest request, List<String> errorMessages, ArrayList<String> warningMessages  ){
    	
    	List <Project> authorizedProjectList = new ArrayList<Project>();
    	
    try{	
    	//logger.debug("Context: "+ SecurityContextHolder.getContext());
    	//logger.debug("Autenttif: "+ SecurityContextHolder.getContext().getAuthentication());
    	logger.debug("buildAuthorizedProjectList START>");
    	
    	UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	//if user has the reports permission all projects from organization are available
    	if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ReportsView())){
    		logger.debug("User has getTS_ReportsView privilege, returning all projects in organisation");
    		authorizedProjectList = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), true);
    	}else {//otherwise only the projects for which he is PM are available
    		logger.debug("User is PM returning his projects");
    		authorizedProjectList =  BLProject.getInstance().getProjectsByManager(userAuth.getPersonId(), true, true);
    	}
    	//authorizedProjectList.clear();
    	if (authorizedProjectList.isEmpty()){
    		warningMessages.add(messageSource.getMessage(REPORT_PROJECT_EMPTY, null, RequestContextUtils.getLocale(request)));
    	}
    	
    } catch (BusinessException be) {
		logger.error("", be);			
		errorMessages.add(messageSource.getMessage(PROJECT_LIST_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));			
	} catch (Exception e) {
		logger.error("", e);
		errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
	}
		logger.debug("buildAuthorizedProjectList <END");
    	return authorizedProjectList;
    }
    
    private JSONArray buildDefaultValuesJSON (){
    	
    	JSONArray languagePacks = new JSONArray();
    	
    	//getting all supported languages
    	List<String> supportedLanguages = (List<String>) TSContext.getFromContext(IConstant.NOM_SUPPORTED_LANGUAGES);
		
    	Map<String,String> defaultValuesMap = null;
    	for (String language:supportedLanguages){
    		JSONObject languagePack = new JSONObject();
    		languagePack.accumulate("language", language);
    		//get all default values for the language
    		defaultValuesMap = ReportsMessageTools.getReportProjectDefaultValues(language);
    		JSONArray defaultValues = new JSONArray();
    		//iterate through values and form the JSON array
    		for (Map.Entry<String, String> entry : defaultValuesMap.entrySet()){
    			JSONObject defaultValue = new JSONObject();
    			defaultValue.accumulate("key", entry.getKey());
    			defaultValue.accumulate("value", entry.getValue());
    			defaultValues.add(defaultValue);
    		}
    		languagePack.accumulate("defaultValues", defaultValues); 	
    		languagePacks.add(languagePack);
    	}
    	logger.debug("JSON Language packs created: "+languagePacks);
    	   	
    	
    	return languagePacks;
    }
    
    /**
     * Inspects the request for the values of the default columns
     * @param request
     * @param criteria
     */
    private void populateCriteria (HttpServletRequest request, ReportProjectCriteria criteria ){
    	//get the record and cost key lists
    	final String prefix = "columnInput_"; 
    	
    	List <String> recordColumnsKeyList = ReportsMessageTools.getRecordColumnsKeyList();
    	List <String> costColumnsKeyList = ReportsMessageTools.getCostColumnsKeyList();
    	
    	if (criteria.getRecordColumns() == null) criteria.setRecordColumns(new HashMap<String, String>());
    	for (String key : recordColumnsKeyList){
    		String columnLabel = request.getParameter(prefix.concat(key));
    		criteria.getRecordColumns().put(key, columnLabel);
    		logger.debug("Added record column label: "+key+", "+columnLabel);
    	}
    	if (criteria.getCostColumns() == null) criteria.setCostColumns(new HashMap<String, String>());
    	for (String key : costColumnsKeyList){
    		String columnLabel = request.getParameter(prefix.concat(key));
    		criteria.getCostColumns().put(key, columnLabel);
    		logger.debug("Added cost column label: "+key+", "+columnLabel);
    	}
    	
    	String uid = request.getParameter(IConstant.REPORT_UID);
		logger.debug("Retrieved UID : "+uid);
		criteria.setUniqueId(uid);
    
    }
    
    /**
     * Builds the reportParams necessary for the generation of the report by inspecting the command object
     * @param criteria
     * @param request
     * @return
     * @throws BusinessException
     */
    
    private ReportParams buildReportParams (ReportProjectCriteria criteria, HttpServletRequest request) throws BusinessException{
    	ReportParams reportParams = new ReportParams();//create the bean containing the report parameters which will be passed to the Report Web Service Client
			
		
    	//public static String TS_PROJECT_REPORT_LOCALE_PARAM								= "locale";
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_LOCALE_PARAM, criteria.getReportLanguage());
    	//public static String TS_PROJECT_REPORT_REPORT_TITLE_PARAM						= "reportTitle";
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_REPORT_TITLE_PARAM, criteria.getReportTitle());
    	//public static String TS_PROJECT_REPORT_SEARCH_CRITERIA_START_DATE				= "startDate";
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_START_DATE, criteria.getReportStartDate());
    	//public static String TS_PROJECT_REPORT_SEARCH_CRITERIA_END_DATE					= "endDate";
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_END_DATE, criteria.getReportEndDate());
    	//public static String TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_NAME				= "projectName";
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_NAME, criteria.getProjectName());
    	//public static String TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_ID				= "projectId";
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_ID, criteria.getProjectId());
    	// we retrieve the currency name, ID and initials
    	Currency currency = BLCurrency.getInstance().getAll(criteria.getCurrencyId());
    	String currencyInitials = " ("+currency.getInitials()+")";
    	
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_CURRENCY_NAME, currency.getName());
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_CURRENCY_ID, currency.getCurrencyId());
    	
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_BILLABLE, ""+criteria.getBillable());
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_FORMAT, criteria.getReportFileType());
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE, criteria.getPriceCompute());
    	
    	for (String key: ReportsMessageTools.getRecordColumnsKeyList()){
    		if (criteria.getRecordColumns().get(key) != null) {
    			reportParams.setProperty(key,(String)criteria.getRecordColumns().get(key));
    		}
    	}
    	//we put the currency initials in the tables
    	String priceLabel = (String )reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_COST_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_COST_PRICE,priceLabel);
    	}
    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE,priceLabel);
    	}
    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE,priceLabel);
    	}
    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE,priceLabel);
    	}
    	
    	for (String key: ReportsMessageTools.getCostColumnsKeyList()){
    		if (criteria.getCostColumns().get(key) != null) {
    			reportParams.setProperty(key,(String)criteria.getCostColumns().get(key));
    		}
    	}

    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_COST_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_COST_PRICE,priceLabel);
    	}
    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_BILLING_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_BILLING_PRICE,priceLabel);
    	}
    	
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_TOTAL_COST_PRICE, criteria.getTotalCostPrice()+currencyInitials);
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_TOTAL_BILLING_PRICE, criteria.getTotalBillingPrice()+currencyInitials);
    	
    	
    	reportParams.setProperty(IConstant.TS_PROJECT_REPORT_SUBTOTAL_INTERVAL, criteria.getSubtotalPeriod());
    	
    	   	
    	//if the attachment param exists on the request, it means that the generated report must be a whole html page with head and body tags, 
		//otherwise the report must be embeddable in an existent html page(no head and body tags)
		if (ServletRequestUtils.getBooleanParameters(request, ATTACHMENT) != null){
			reportParams.setProperty(IConstant.TS_PROJECT_REPORT_IS_EMBEDDABLE, false);
		} else {
			reportParams.setProperty(IConstant.TS_PROJECT_REPORT_IS_EMBEDDABLE, true);
		}
    	 	
    	return reportParams;
    }
}
