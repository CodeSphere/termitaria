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
package ro.cs.ts.web.servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.utils.messages.ReportsMessageTools;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.ws.client.reports.ReportsWebServiceClient;
import ro.cs.ts.ws.client.reports.entity.ReportParams;



/**
 * Servlet used to receive requests for reports and return it as binary data
 * 
 * @author alu
 *
 */
public class ReportServlet extends HttpServlet {
	
	protected Log logger = LogFactory.getLog(this.getClass());
	private static final String CREATE_ERROR = "report.create.error";
	private MessageSource messageSource = (MessageSource) TSContext.getApplicationContext().getBean("messageSource");
	private static final String ATTACHMENT 						= "attachment";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("doPost START");
		ServletOutputStream sos = response.getOutputStream();
		try {//retrieve the params from the context
//			***********************************************************************
			//Servlet's OutputStream
			//get the report type
			String reportType = request.getParameter(IConstant.REPORT_TYPE);
			logger.debug("Report type: "+reportType);
			
			String uid = (String)request.getParameter(IConstant.REPORT_UID);
			logger.debug("Retrieved UID in  report servlet: "+uid);	
			Map<String,ReportParams> reportParamsMap = (Map<String,ReportParams>) TSContext.getFromContext(IConstant.REPORT_PARAM_MAP);
			ReportParams reportParams = reportParamsMap.get(uid);
			
			String format = (String)reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_FORMAT);
			// we set the is embeddable attribute
			if (format.toLowerCase().equals("html") && ServletRequestUtils.getBooleanParameters(request, ATTACHMENT) == null){
				logger.debug("The report is HTML embeddable");
				reportParams.setProperty(IConstant.TS_PROJECT_REPORT_IS_EMBEDDABLE, true);
			}else {
				reportParams.setProperty(IConstant.TS_PROJECT_REPORT_IS_EMBEDDABLE, false);
			}
			
			logger.debug(ReportsMessageTools.viewReportParams(reportParams));
		
			DataHandler reportFileReceived = null;
			String reportTitle = null;
			if(reportType.equals(IConstant.REPORT_TYPE_PROJECT)){
				logger.debug("Retrieving project report:  ");
				reportFileReceived = ReportsWebServiceClient.getInstance().getProjectReport(reportParams);
				reportTitle = (String) reportParams.getProperties().get(IConstant.TS_PROJECT_REPORT_REPORT_TITLE_PARAM);
			}else if (reportType.equals(IConstant.REPORT_TYPE_TIME_SHEET)){
				logger.debug("Retrieving time sheet report:  ");
				reportFileReceived = ReportsWebServiceClient.getInstance().getTimeSheetReport(reportParams);
				reportTitle = (String) reportParams.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_REPORT_TITLE_PARAM);
			}else {
				logger.error("We do not have a report type on the request!!!!");
				throw new BusinessException(ICodeException.REPORT_CREATE, null);
			}
			
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
			String exceptionCode = ICodeException.REPORT_CREATE;

			sos.write(
					("<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
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
								"YAHOO.ts.errorsContainer = new YAHOO.widget.Module(\"errorsContainer\", {visible:true} ); " +
								"YAHOO.ts.errorsContainer.render() ;" +
								"YAHOO.ts.errorsContainer.show();" +
								"YAHOO.util.Event.addListener(\"closeErrors\", \"click\", function () {	" +	
								"YAHOO.ts.errorsContainer.hide();" +
								"YAHOO.ts.errorsContainer.destroy(); " +
								"}, YAHOO.ts.errorsContainer, true);" +
								"}" +
							"</script> </body></html>").getBytes());
		} finally {
			if (sos != null) {
				//Flushing and Closing OutputStream
				sos.flush();
				sos.close();
				logger.debug("**** servlet output stream closed.");
			}
		}
		logger.debug("doPost END");
	}
}

