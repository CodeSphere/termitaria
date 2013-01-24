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
package ro.cs.logaudit.web.controller.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.logaudit.business.BLOrganisation;
import ro.cs.logaudit.common.ConfigParametersProvider;
import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.context.AuditContext;
import ro.cs.logaudit.entity.AuditOmReportCriteriaBean;
import ro.cs.logaudit.exception.ICodeException;
import ro.cs.logaudit.web.controller.root.ControllerUtils;
import ro.cs.logaudit.web.controller.root.RootSimpleFormController;
import ro.cs.logaudit.web.security.UserAuth;
import ro.cs.logaudit.ws.client.reports.IReportsWsClientConstant;

/**
 * 
 * @author coni
 *
 */
public class AuditOmReportsController extends RootSimpleFormController{

	private static final String FORM_VIEW = "AuditOmReports";
	private static final String SUCCESS_VIEW = "AuditReportsRender";
	private static final String ORGANIZATIONS_ALL = "ORGANIZATIONS_ALL";
	private static final String MODULE_ID = "moduleId";
	private static final String AUDIT_OM_REPORTS_CRITERIA_BEAN = "auditOmReportCriteriaBean";
	private static final String CREATE_ERROR = "audit.om.report.create.error";
	private static final String IS_ADMIN_IT = "IS_ADMIN_IT";
	private static final String REPORT_SERVLET_URL_VALUE = ConfigParametersProvider.getConfigString("audit.report.servlet.url");
	private static final String REQUEST_URL = "REQUEST_URL";
	private static final String JSON_PERSONS = "JSON_PERSONS";
	private static final String AUDIT_OM_REPORTS_SHOW_CRITERIA = "audit.om.report.show.criteria";
	private static final String REPORT_SERVLET_URL = "REPORT_SERVLET_URL";
	private static final String FORMAT = "FORMAT";
	private static final String TODAY_DATE = "TODAY_DATE";
	
	public AuditOmReportsController(){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(AUDIT_OM_REPORTS_CRITERIA_BEAN);
		setCommandClass(AuditOmReportCriteriaBean.class);
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder START" );
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));
		
		logger.debug("initBinder END" );
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit START");
		
		//used as a container for info/error messages
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		try {
			AuditOmReportCriteriaBean reportRequestParamBean = (AuditOmReportCriteriaBean) command;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

			//Retrieve the report organisationId param for the report request
			Integer organisationId = null;
			if (reportRequestParamBean.getOrganisationId() != null){
				organisationId = reportRequestParamBean.getOrganisationId();
			} else {
				organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			}
			
			//Retrieve the start date param for the report request
			String startDate = "";
			if (reportRequestParamBean.getStartDate() != null && !"".equals(reportRequestParamBean.getStartDate())) {
				startDate = sdf.format(reportRequestParamBean.getStartDate());
			}
			
			//Retrieve the end date param for the report request			
			String endDate = "";
			if (reportRequestParamBean.getEndDate() != null && !"".equals(reportRequestParamBean.getEndDate())) {
				endDate = sdf.format(reportRequestParamBean.getEndDate());
			}
			
			//Retrieve the person ID param for the report request
			String personId = "";
			if ( reportRequestParamBean.getPersonId() != null) {
				personId =  String.valueOf(reportRequestParamBean.getPersonId());
			}			
			
			String reportServletRequest = REPORT_SERVLET_URL_VALUE.concat("?").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_START_DATE_PARAM).concat("=").concat(startDate)
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_END_DATE_PARAM).concat("=").concat(endDate)
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PERSON_ID_PARAM).concat("=").concat(personId)
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MESSAGE_PARAM).concat("=").concat(reportRequestParamBean.getMessage())
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_EVENT_PARAM).concat("=").concat(reportRequestParamBean.getEvent())
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM).concat("=").concat(String.valueOf(IConstant.NOM_MODULE_OM_LABEL_KEY))
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PARAM_REPORT_TITLE).concat("=").concat(reportRequestParamBean.getReportTitle())
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ORIENTATION_PARAM).concat("=").concat(reportRequestParamBean.getOrientation())
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_FORMAT_PARAM).concat("=").concat(reportRequestParamBean.getFormat().toLowerCase())
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ORGANISATION_ID_PARAM).concat("=").concat(String.valueOf(organisationId))
											.concat("&").concat(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM).concat("=").concat(RequestContextUtils.getLocale(request).toString().toLowerCase().substring(0, 2));
			
			mav.addObject(REQUEST_URL, reportServletRequest);
			mav.addObject(FORMAT, reportRequestParamBean.getFormat().toLowerCase());
		} catch (Exception exc) {
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES_NEW_WINDOW);
			logger.error("", exc);
			errorMessages.add(messageSource.getMessage(CREATE_ERROR, new Object[] {ICodeException.AUDITOM_REPORT_CREATE, ControllerUtils.getInstance().getFormattedCurrentTime()}
			, RequestContextUtils.getLocale(request)));
		}
		
		//setting all messages on response
		setErrors(request, errorMessages);
	
		logger.debug("onSubmit END");
		return mav;
	}

	protected Map referenceData( HttpServletRequest request) throws Exception {
		logger.debug("referenceData START");
		Map map = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		try{
			// add all organizations
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			map.put(IS_ADMIN_IT, userAuth.isAdminIT());
			if (userAuth.isAdminIT()){
				map.put(ORGANIZATIONS_ALL, BLOrganisation.getInstance().getAllOrganisations());
			}
		
			//add the om module id
			map.put(MODULE_ID, IConstant.NOM_MODULE_OM_LABEL_KEY);
		
			//add all report format orientations available
			map.put(IConstant.NOM_AUDIT_REPORT_ORIENTATIONS, IConstant.AUDIT_REPORT_ORIENTATIONS);
	
			//add all report formats available
			map.put(IConstant.NOM_AUDIT_REPORT_FORMATS, IConstant.AUDIT_REPORT_FORMATS);
			
			//add all the OM audit event types
			map.put(IConstant.NOM_AUDIT_EVENTS_OM, AuditContext.getFromContext(IConstant.NOM_AUDIT_EVENTS_OM));
			
			//add the report servlet url 
			map.put(REPORT_SERVLET_URL, REPORT_SERVLET_URL_VALUE);
			
			//add the current date
			map.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
		
			// add all the persons from user's organization for autoComplete
			if (!userAuth.isAdminIT()){
				map.put(JSON_PERSONS, 
						ControllerUtils.getInstance().getPersonsFromOrgAsJSON((ControllerUtils.getInstance().getOrganisationIdFromSession(request)), RequestContextUtils.getLocale(request), errorMessages, messageSource));
			}
		} catch (Exception exc) {
			errorMessages.add(messageSource.getMessage(AUDIT_OM_REPORTS_SHOW_CRITERIA, new Object[] {}, RequestContextUtils.getLocale(request)));
		}

		setErrors(request, errorMessages);
		logger.debug("referenceData END");
		return map;		
	}

}
