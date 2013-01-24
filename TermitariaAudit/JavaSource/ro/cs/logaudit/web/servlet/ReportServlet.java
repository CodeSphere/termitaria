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
package ro.cs.logaudit.web.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.context.AuditContext;
import ro.cs.logaudit.exception.ICodeException;
import ro.cs.logaudit.web.controller.root.ControllerUtils;
import ro.cs.logaudit.ws.client.reports.IReportsWsClientConstant;
import ro.cs.logaudit.ws.client.reports.ReportsWebServiceClient;
import ro.cs.logaudit.ws.client.reports.entity.AuditEventsReportParams;
/**
 * Servlet used to receive requests for reports and return it as binary data
 * 
 * @author alu
 *
 */
public class ReportServlet extends HttpServlet {
	
	protected Log logger = LogFactory.getLog(this.getClass());
	private static final String CREATE_ERROR = "audit.om.report.create.error";
	private MessageSource messageSource = (MessageSource) AuditContext.getApplicationContext().getBean("messageSource");
	private static final String ATTACHMENT = "attachment";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("doPost START");
		ServletOutputStream sos = null;
		StopWatch sw = new StopWatch();	
		sw.start("Retrieve report");
		try {
			//create the bean containing the report parameters which will be passed to the Report Web Service Client
			AuditEventsReportParams reportParams = new AuditEventsReportParams();
			
			//Retrieve the start date param for the report request
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date startDate = sdf.parse(ServletRequestUtils.getStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_START_DATE_PARAM), new ParsePosition(0));
			if (startDate != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_START_DATE_PARAM, startDate);
			}
			
			//Retrieve the end date param for the report request
			Date endDate = sdf.parse(ServletRequestUtils.getStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_END_DATE_PARAM), new ParsePosition(0));
			if (endDate != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_END_DATE_PARAM, endDate);
			}

			//Retrieve the personId param for the report request
			String personId = ServletRequestUtils.getStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PERSON_ID_PARAM);
			if (personId != null && personId != ""){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PERSON_ID_PARAM, Integer.valueOf(personId));
			}

			//Retrieve the message param for the report request
			String message = ServletRequestUtils.getStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MESSAGE_PARAM);
			if (message != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MESSAGE_PARAM, message);
			}
			
			//Retrieve the event param for the report request
			String event = ServletRequestUtils.getStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_EVENT_PARAM);
			if (event != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_EVENT_PARAM, event);
			}
			
			//Retrieve the moduleId param for the report request
			Integer moduleId = ServletRequestUtils.getIntParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM);
			if (moduleId != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM, moduleId);
			}
			
			//Retrieve the reportTitle param for the report request
			String reportTitle = ServletRequestUtils.getStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PARAM_REPORT_TITLE);
			if (reportTitle != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PARAM_REPORT_TITLE, reportTitle);
			}
			
			//Retrieve the orientation param for the report request
			String orientation = ServletRequestUtils.getRequiredStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ORIENTATION_PARAM);
			if (orientation != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ORIENTATION_PARAM, orientation);
			}
			
			//Retrieve the report format param for the report request
			String format = ServletRequestUtils.getRequiredStringParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_FORMAT_PARAM);
			if (format != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_FORMAT_PARAM, format.toLowerCase());
			}
			
			//Retrieve the organisationId param for the report request
			Integer organisationId = ServletRequestUtils.getIntParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ORGANISATION_ID_PARAM);
			if (organisationId != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ORGANISATION_ID_PARAM, organisationId);
			}
			
			reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM, request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME).toString().toLowerCase().substring(0, 2));
			
			//if the attachment param exists on the request, it means that the generated report must be a whole html page with head and body tags, 
			//otherwise the report must be embeddable in an existent html page(no head and body tags)
			if (ServletRequestUtils.getBooleanParameters(request, ATTACHMENT) != null){
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_HTML_IS_EMBEDDABLE, false);
			} else {
				reportParams.setProperty(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_HTML_IS_EMBEDDABLE, true);
			}

			//Servlet's OutputStream
			sos = response.getOutputStream();

			//get the requested report
			DataHandler reportFileReceived = ReportsWebServiceClient.getInstance().getAuditEventsReport(reportParams);
			
			//set the response content type
			if (format.toLowerCase().equals("html")) {
				response.setContentType("text/html");
				if (ServletRequestUtils.getBooleanParameters(request, ATTACHMENT) != null){
					response.setHeader("Content-Disposition","attachment; filename=\"".concat(reportTitle).concat(".html\""));
				} else {
					response.setHeader("Content-Disposition","inline; filename=\"".concat(reportTitle).concat(".html\""));
				}
			} else if (format.toLowerCase().equals("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","inline; filename=\"".concat(reportTitle).concat(".pdf\""));
			} else if (format.toLowerCase().equals("doc")) {
				response.setContentType("application/msword");
				response.setHeader("Content-Disposition","attachment; filename=\"".concat(reportTitle).concat(".doc\""));
			} else if (format.toLowerCase().equals("xls")) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition","attachment; filename=\"".concat(reportTitle).concat(".xls\""));
			}
			
			//write the received report bytes stream to response output stream
			byte buffer[] = new byte[4096];
			BufferedInputStream bis = new BufferedInputStream(reportFileReceived.getInputStream());
			int size = 0;
			int i;
			while ((i = bis.read(buffer, 0, 4096)) != -1) {
				sos.write(buffer, 0, i);
				size +=i;
			}

			if (size == 0) {
				response.setContentType("text/plain");
				sos.write("No content !".getBytes());
			}
					
			bis.close();
			response.setContentLength(size);
			
			logger.debug("**** report transfer completed !");
		} catch (Exception ex) {
			logger.error("", ex);
			response.setContentType("text/html");
			String exceptionCode = null;;
			if (((Integer) ServletRequestUtils.getIntParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM)).equals(new Integer(IConstant.NOM_MODULE_OM_LABEL_KEY))) {
				exceptionCode = ICodeException.AUDITOM_REPORT_CREATE;
			} else if (((Integer) ServletRequestUtils.getIntParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM)).equals(new Integer(IConstant.NOM_MODULE_DM_LABEL_KEY))) {
				exceptionCode = ICodeException.AUDITDM_REPORT_CREATE;
			} else if (((Integer) ServletRequestUtils.getIntParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM)).equals(new Integer(IConstant.NOM_MODULE_CM_LABEL_KEY))) {
				exceptionCode = ICodeException.AUDITCM_REPORT_CREATE;
			} else if (((Integer) ServletRequestUtils.getIntParameter(request, IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM)).equals(new Integer(IConstant.NOM_MODULE_TS_LABEL_KEY))) {
				exceptionCode = ICodeException.AUDITTS_REPORT_CREATE;
			}
			response.getWriter().write(
					"<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
						"<head>	<script type=\"text/javascript\" src=\"js/cs/cs_common.js\"></script>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"themes/standard/css/style.css\"/> "+
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"themes/standard/css/yui/fonts-min.css\" /> " +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"themes/standard/css/yui/container.css\" /> </head> " +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"themes/standard/css/yui/button.css\" />" +
						"<body> <div id=\"errorsContainer\" class=\"errorMessagesDiv\"> " +
							"<table class=\"errorMessagesTable\">" +
								"<tr>" +
									"<td>" +
									"</td>" +
									"<td>" +
										"<div class=\"hd\">" +
											"<div id=\"closeErrors\" class=\"messagesCloseButon\"></div>" +
										"</div>" +
									"</td>" +
								"</tr>" +
								"<tr>" +
									"<td>" +
										"<div class=\"bd\">" +
											"<div style=\"width:470px\"> " +
											messageSource.getMessage(CREATE_ERROR, new Object[] {exceptionCode, ControllerUtils.getInstance().getFormattedCurrentTime()}
											, (Locale) request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME)) + "<br/> " +
											"</div>" +
										"</div>" +
									"</td>" +
									"<td>" +
									"</td>" +
								"</tr>" +
							"</table>" +
							"<div class=\"ft\">&nbsp;</div>" +
							"</div>" +
							"<script> " +
								"if(typeof(YAHOO.widget.Module) != \"undefined\") { " + 
								"YAHOO.audit.errorsContainer = new YAHOO.widget.Module(\"errorsContainer\", {visible:true} ); " +
								"YAHOO.audit.errorsContainer.render() ;" +
								"YAHOO.audit.errorsContainer.show();" +
								"YAHOO.util.Event.addListener(\"closeErrors\", \"click\", function () {	" +	
								"YAHOO.audit.errorsContainer.hide();" +
								"YAHOO.audit.errorsContainer.destroy(); " +
								"}, YAHOO.audit.errorsContainer, true);" +
								"}" +
							"</script> </body></html>");
			response.getWriter().flush();
		} finally {
			if (sos != null) {
				//Flushing and Closing OutputStream
				sos.flush();
				sos.close();
				logger.debug("**** servlet output stream closed.");
			}
		}
		logger.debug("doPost END");
		//list all the tasks performed
		logger.debug(sw.prettyPrint());
		sw.stop();
	}
}
