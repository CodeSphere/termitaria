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

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import ro.cs.ts.common.Tools;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.ReportTimeSheetCriteria;
import ro.cs.ts.entity.SearchPersonBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.utils.messages.ReportsMessageTools;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.reports.entity.ReportParams;

/**
 * Responsible for the Report Time Sheet View
 * @author alexandru.dobre
 *
 */
public class ReportTimeSheetController extends RootSimpleFormController{
	
	private static final String SUCCESS_VIEW 				= "Reports_TS_Succes";
	private static final String FORM_VIEW 					= "Reports_TS";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String GENERAL_ERROR				= "report.time.sheet.general.error";	
	private static final String PROJECT_LIST_ERROR 			= "report.time.sheet.project.list.error";
	private static final String GET_ORG_CURRENCIES_ERROR    = "report.time.sheet.get.currency.list.error";
	private static final String CREATE_ERROR 				= "report.time.sheet.create.error";
	
	private static final String REPORT_CURRENCY_EMPTY		= "report.currency.empty";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 						= "BACK_URL";
	private static final String NEXT_BACK_URL					= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 				= "ENCODE_BACK_URL";
	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String AVAILABLE_PROJECTS 				= "AVAILABLE_PROJECTS";
	private static final String TODAY_DATE						= "TODAY_DATE";
	private static final String ORG_CURRENCIES					= "ORG_CURRENCIES";
	private static final String DEFAULT_VALUES_JSON				= "DEFAULT_VALUES_JSON";
	private static final String RECORD_COLUMNS_KEY_LIST			= "RECORD_COLUMNS_KEY_LIST";
	private static final String PRESENT_RECORD_COLUMNS_KEY_LIST	= "PRESENT_RECORD_COLUMNS_KEY_LIST";
	private static final String ABSENT_RECORD_COLUMNS_KEY_LIST	= "ABSENT_RECORD_COLUMNS_KEY_LIST";
	private static final String IS_PM_FOR_AT_LEAST_ONE_PROJECT 	= "IS_PM_FOR_AT_LEAST_ONE_PROJECT";
	private static final String REQUEST_URL 					= "REQUEST_URL";
	private static final String REPORT_SERVLET_URL_VALUE 		= ConfigParametersProvider.getConfigString("report.servlet.url");
	private static final String REPORT_SERVLET_URL 				= "REPORT_SERVLET_URL";
	private static final String FORMAT 							= "FORMAT";
	private static final String ATTACHMENT 						= "attachment";
	
	public ReportTimeSheetController (){
		
		setCommandName("reportTimeSheetCriteria");
		setCommandClass(ReportTimeSheetCriteria.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(sdf, true));
		
        logger.debug("initBinder - END");
	}
	

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	
    	logger.debug("formBackingObject - START");
    	ReportTimeSheetCriteria repCriteria = new ReportTimeSheetCriteria();
    	logger.debug("formBackingObject - END");
    	return repCriteria;
    }
    
    @Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
    	logger.debug("onSubmit - START");
    	ModelAndView mav = new ModelAndView(getSuccessView());
    	
    	ReportTimeSheetCriteria criteria = (ReportTimeSheetCriteria) command;
    	
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		try {		
			logger.debug("*********************Building report time sheet criteria******************************");
			populateCriteria(request,criteria);
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
																		  .concat("&").concat(IConstant.REPORT_TYPE).concat("=").concat(IConstant.REPORT_TYPE_TIME_SHEET);
			logger.debug("Report servlet request: "+reportServletRequest);

			mav.addObject(REQUEST_URL, reportServletRequest);
			mav.addObject(FORMAT, criteria.getReportFileType().toLowerCase());
	} catch (Exception exc) {
		mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES_NEW_WINDOW);
		logger.error("", exc);
		errorMessages.add(messageSource.getMessage(CREATE_ERROR, new Object[] {ICodeException.REPORT_TIME_SHEET_CREATE, ControllerUtils.getInstance().getFormattedCurrentTime()}
		, RequestContextUtils.getLocale(request)));
	}
	
	//Publish Info/Error messages
	setMessages(request, infoMessages);
	setErrors(request, errorMessages);
	
	logger.debug("onSubmit - END");
	return mav;
}
	
	/**
	 * Adds to model required data
	 * @author Coni
	 */
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		logger.debug("referenceData - START");
	       	
	   	UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    ReportTimeSheetCriteria criteria = (ReportTimeSheetCriteria) command;
	    	
	    Map map = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages  	= new ArrayList<String>();
			
		List<Project> projectList = buildAuthorizedProjectList(request, errorMessages);
				
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
			
		//adding record column keys

		map.put(RECORD_COLUMNS_KEY_LIST, ReportsMessageTools.getReportTimeSheetRecordColumnsKeyList());
		map.put(PRESENT_RECORD_COLUMNS_KEY_LIST, ReportsMessageTools.getTSReportDefaultPresentRecordColumnsKeyList());
		map.put(ABSENT_RECORD_COLUMNS_KEY_LIST, ReportsMessageTools.getTSReportAbsentRecordColumnsKeyList());
		
		//add the report servlet url 
		map.put(REPORT_SERVLET_URL, REPORT_SERVLET_URL_VALUE);
		
		if (projectList != null && projectList.size() > 0) {
			map.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, true);
		} else {
			map.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, false);
		}
		
		//put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
		
		String servletPath = request.getServletPath();
		String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");
		
		logger.debug("BACK_URL = " + backUrl);
		logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
		
		map.put(BACK_URL, backUrl);		
		map.put(NEXT_BACK_URL, nextBackUrl);
		if (backUrl != null) {
			map.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));	
		}
		SearchPersonBean spb = new SearchPersonBean();
		map.put("searchPersonBean", spb);
			
		try {
			//add the organization available currencies
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			if (currencies != null && !currencies.isEmpty()) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for (Currency currency : currencies) {
					IntString entry = new IntString();
					entry.setValue(currency.getCurrencyId());
					entry.setLabel(currency.getName());
					nomCurrencies.add(entry);
				}
				map.put(ORG_CURRENCIES, nomCurrencies);
			}else {
				infoMessages.add(messageSource.getMessage(REPORT_CURRENCY_EMPTY, null, RequestContextUtils.getLocale(request)));
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);			
			errorMessages.add(messageSource.getMessage(GET_ORG_CURRENCIES_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
			
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		logger.debug("referenceData - END");
    	return map;
    }
	
	
	/**
	 * 
	 * @param request
	 * @param errorMessages
	 * @return
	 */
    private List <Project> buildAuthorizedProjectList (HttpServletRequest request, List<String> errorMessages){
    	
    	List <Project> authorizedProjectList = new ArrayList<Project>();
    	
	    try{	
	    	UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	
	    	//if user has the reports permission all projects from organization are available
	    	if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ReportsView())){
	    		authorizedProjectList = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), true);
	    	}else {//otherwise only the projects for which he is PM are available
	    		authorizedProjectList =  BLProject.getInstance().getProjectsByManager(userAuth.getPersonId(), true, true);
	    	}
	    } catch (BusinessException be) {
			logger.error("", be);			
			errorMessages.add(messageSource.getMessage(PROJECT_LIST_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));			
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
    	return authorizedProjectList;
    }
    
    /**
     * Builds a JSON Array containing JSON Object-s with the following structure:
     * -language
     * -JSON Array of JSON Object-s with key-value pairs, where key it is one report 
     * parameter key (like tables column headers, report title, etc)
     * and value it is default value for that parameter and the current language
     * @return
     */
    private JSONArray buildDefaultValuesJSON (){
    	
    	JSONArray languagePacks = new JSONArray();
    	
    	//getting all supported languages
    	List<String> supportedLanguages = (List<String>) TSContext.getFromContext(IConstant.NOM_SUPPORTED_LANGUAGES);
		
    	//the map containing default column names for the report records table
    	Map<String,String> defaultValuesMap = null;
    	for (String language : supportedLanguages){
    		JSONObject languagePack = new JSONObject();
    		languagePack.accumulate("language", language);
    		
    		//get all default values for the language
    		defaultValuesMap = ReportsMessageTools.getReportTimeSheetDefaultValues(language);
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
    private void populateCriteria (HttpServletRequest request, ReportTimeSheetCriteria criteria ){
    	//get the record and cost key lists
    	final String prefix = "columnInput_"; 
    	
    	List <String> recordColumnsKeyList = ReportsMessageTools.getReportTimeSheetRecordColumnsKeyList();
    	
    	if (criteria.getRecordColumns() == null) {
    		criteria.setRecordColumns(new HashMap<String, String>());
    	}
    	for (String key : recordColumnsKeyList){
    		String columnLabel = request.getParameter(prefix.concat(key));
    		if (columnLabel != null) {
        		criteria.getRecordColumns().put(key, columnLabel);
    		}
    		logger.debug("Added record column label: " + key + ", " + columnLabel);
    	}
    	
    	String uid = request.getParameter(IConstant.REPORT_UID);
		logger.debug("Retrieved UID : "+uid);
		criteria.setUniqueId(uid);
		
		String[] teamMemberIds = request.getParameterValues("teamMemberIds");
		//casting from string to integer
		criteria.setTeamMemberIds(Tools.getInstance().castStringToIntegerArray(teamMemberIds));
		String[] personIds = request.getParameterValues("personIds");
		criteria.setPersonIds(Tools.getInstance().castStringToIntegerArray(personIds));
		
		criteria.setOrganizationId(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
    }
    
    /**
     * Builds the reportParams necessary for the generation of the report by inspecting the command object
     * @param criteria
     * @param request
     * @return
     * @throws BusinessException
     */
    
    private ReportParams buildReportParams (ReportTimeSheetCriteria criteria, HttpServletRequest request) throws BusinessException{
    	ReportParams reportParams = new ReportParams();//create the bean containing the report parameters which will be passed to the Report Web Service Client
			
		
    	
    	
    	//TIME SHEET REPORT PARAMS MAP KEYS
    	//String TS_TIME_SHEET_REPORT_LOCALE_PARAM							= "locale";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_LOCALE_PARAM, criteria.getReportLanguage());
    	//String TS_TIME_SHEET_REPORT_REPORT_TITLE_PARAM						= "reportTitle";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_REPORT_TITLE_PARAM, criteria.getReportTitle());
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_START_DATE				= "startDate";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_START_DATE, criteria.getReportStartDate());
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_END_DATE				= "endDate";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_END_DATE, criteria.getReportEndDate());
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_CURRENCY_ID				= "currencyId";
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_CURRENCY_NAME			= "currencyName";
    	
    	// we retrieve the currency name, ID and initials
    	Currency currency = BLCurrency.getInstance().getAll(criteria.getCurrencyId());
    	String currencyInitials = " ("+currency.getInitials()+")";
    	
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_CURRENCY_NAME, currency.getName());
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_CURRENCY_ID, currency.getCurrencyId());
    	
    	
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_BILLABLE				= "billable";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_BILLABLE, ""+criteria.getBillable());
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_FORMAT					= "format";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_FORMAT, criteria.getReportFileType());
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE	= "priceComputeType";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE, criteria.getPriceCompute());
    	
    	if (criteria.getTeamMemberIds() != null && criteria.getTeamMemberIds().length > 0) {
    		reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_TEAM_MEMBER_IDS, Arrays.asList(criteria.getTeamMemberIds()));
    	}
    	if (criteria.getPersonIds() != null && criteria.getPersonIds().length > 0) {
    		reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_PERSON_IDS, Arrays.asList(criteria.getPersonIds()));
    	}
    	
    	//String TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_ORGANIZATION_ID			= "organizationId";
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_ORGANIZATION_ID, criteria.getOrganizationId());
    	
    	for (String key: ReportsMessageTools.getReportTimeSheetRecordColumnsKeyList()){
    		if (criteria.getRecordColumns().get(key) != null) {
    			reportParams.setProperty(key,(String) criteria.getRecordColumns().get(key));
    		}
    	}
    	
    	//we put the currency initials in the tables
    	String priceLabel = (String )reportParams.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_COST_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_COST_PRICE,priceLabel);
    	}
    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE,priceLabel);
    	}
    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE,priceLabel);
    	}
    	priceLabel = (String )reportParams.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE);
    	if (priceLabel != null && !priceLabel.isEmpty()){
    		priceLabel+=currencyInitials;
    		reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE,priceLabel);
    	}
    	
    	reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_SUBTOTAL_INTERVAL, criteria.getSubtotalPeriod());
    	
    	//if the attachment param exists on the request, it means that the generated report must be a whole html page with head and body tags, 
		//otherwise the report must be embeddable in an existent html page(no head and body tags)
		if (ServletRequestUtils.getBooleanParameters(request, ATTACHMENT) != null){
			reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_IS_EMBEDDABLE, false);
		} else {
			reportParams.setProperty(IConstant.TS_TIME_SHEET_REPORT_IS_EMBEDDABLE, true);
		}
   
    	return reportParams;
    }

}
