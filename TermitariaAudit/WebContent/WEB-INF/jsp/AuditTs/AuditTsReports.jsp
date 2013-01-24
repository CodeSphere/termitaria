<!--
This file is part of Termitaria, a project management tool 
 Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
  
 Termitaria is free software; you can redistribute it and/or 
 modify it under the terms of the GNU Affero General Public License 
 as published by the Free Software Foundation; either version 3 of 
 the License, or (at your option) any later version.
 
 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License 
 along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
-->
<%@ include file="../Taglibs.jsp" %>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
			<a href="#" id="search"><spring:message code="search" /></a>	
			<a href="#" id="reports"><spring:message code="audit.ts.reports" /></a>					
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->
<c:if test="${ERRORS ne null}">
	<%@ include file="./../Messages.jsp" %>
</c:if>
<c:if test="${ERRORS eq null}">
<span class="section_title"><img src="images/titles/Reports.jpg"><spring:message code="audit.ts.report"/></span>

<table>
<tr>	
	<td>
		<form:form id="reportForm" name="reportForm" commandName="auditTsReportCriteriaBean">
			<form:hidden id="reportForm_personId" path="personId"/>
			<table class="tableSearch" cellpadding="3" width="435px">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<tr>
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.reports.title" /> <span id="required">*</span> 
					</td>
					
					<td>
						<form:input id="reportForm_title" cssClass="required formField" path="reportTitle" tabindex="1"/>
					</td>	
				</tr>
				<tr>
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.startDate" />
					</td>
					<td style="padding-left: 1px">
						<table cellpadding="0">
							<tr>					
								<td>
									<form:input path="startDate" id="reportForm_startDate" readonly="true" cssClass="formField" tabindex="2" onclick="document.getElementById('startCalendar').style.zIndex = 1;instantiateStartEndCalendars('startCalendar','reportForm_startDate', '${TODAY_DATE}', 'startTime', true, 'auditTsStartReport', document.getElementById('reportForm_endDate').value, false, '1');"/>
								</td>
								<td>
									<div id="startCalendar"></div>
									<div id="startTime"></div>
								</td>
							</tr>
						</table>
					</td>									
				</tr>
				<tr>
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.endDate"/>
					</td>
					<td style="padding-left: 1px">
						<table cellpadding="0">
							<tr>		
								<td>
									<form:input path="endDate" id="reportForm_endDate" readonly="true" cssClass="formField" tabindex="3" onclick="document.getElementById('endCalendar').style.zIndex = 1;instantiateStartEndCalendars('endCalendar','reportForm_endDate', '${TODAY_DATE}', 'endTime', false, 'auditTsEndReport', document.getElementById('reportForm_startDate').value, false, '1');"/>
								</td>
								<td>
									<div id="endCalendar"></div>
									<div id="endTime"></div>
								</td>
							</tr>
						</table>
					</td>								
				</tr>
				<tr>	
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.user" />
					</td>
					
					<td>
						<div id="userAutoComplete" class="autoComplete">			
							<input id="reportForm_user" type="text" tabindex="4" class="formField"/>
							<div id="userAutoCompleteContainer"></div>
						</div>
					</td>
				</tr>
				<tr>			
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.message"/>
					</td>
			
					<td>
						<form:input id="reportForm_message" cssClass="formField" path="message" tabindex="5"/>
					</td>
				</tr>	
				<tr>
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.event" />
					</td>
					
					<td>
						<form:select id="reportForm_event" cssClass="formField" path="event" tabindex="6">
							<form:option value="-1"><spring:message code="audit.ts.choose.event" /></form:option>
								<c:forEach var="event" items="${AUDIT_EVENTS_TS}">
									<form:option value="${event}"><spring:message code="${event}"/></form:option>
								</c:forEach>
						</form:select>
					</td>	
				</tr>
				<c:set var="orientationTabIndex" value="7"/>
				<tr>	
					<c:choose>	
						<c:when test="${IS_ADMIN_IT == true}">	
							<c:set var="orientationTabIndex" value="8"/>		
							<td class="labelTdReportsForm">
								<spring:message code="audit.ts.organisation"/>
							</td>
				
							<td>
								<form:select cssClass="formField" id="reportForm_organisationId" path="organisationId" onchange="instantiatePersonAutoCompleteForOrganisation(this.options[selectedIndex].value, 'reportForm_user', 'userAutoCompleteContainer', 'reportForm_personId')" tabindex="7">
									<form:option value="-1"><spring:message code="audit.ts.choose.organisation" /></form:option>
									<c:forEach var="org" items="${ORGANIZATIONS_ALL}">
										<form:option value="${org.organisationId}">${org.organisationName}</form:option>
									</c:forEach>
								</form:select>
							</td>						
						</c:when>
						<c:otherwise>
							<input type="hidden" name="organisationId"  id="reportForm_organisationId" value="${SESS_ORGANISATION_ID}"/>
						</c:otherwise>
					</c:choose>				
				</tr>
				<tr>
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.reports.orientation" />
					</td>
					
					<td>
						<form:select id="reportForm_orientation" cssClass="formField" path="orientation" tabindex="${orientationTabIndex}">
							<c:forEach var="orientation" items="${ORIENTATIONS}">
								<form:option value="${orientation}">${orientation}</form:option>
							</c:forEach>
						</form:select>
					</td>	
				</tr>
				<tr>
					<td class="labelTdReportsForm">
						<spring:message code="audit.ts.reports.format" />
					</td>
					
					<td>
						<form:select id="reportForm_format" cssClass="formField" path="format" tabindex="${orientationTabIndex  + 1}">
							<c:forEach var="format" items="${FORMATS}">
								<form:option value="${format}">${format}</form:option>
							</c:forEach>
						</form:select>
					</td>	
				</tr>
				<tr>
					<td colspan="2" class="formActions" align="right">	
						<input type="button" class="button" id="createAuditTsReport" value="<spring:message code="audit.ts.reports.create"/>" tabindex="${orientationTabIndex + 2}"
							onClick="manageOpenReport('AuditTsReports.htm', '${REPORT_SERVLET_URL}', 'reportForm', '<spring:message code='audit.ts.report'/>', 'reportForm_user', 'reportForm_personId', 'reportForm_startDate', 'reportForm_endDate', 'reportForm_message', 'reportForm_event', 'reportForm_moduleId', 'reportForm_title', 'reportForm_orientation', 'reportForm_format', 'reportForm_organisationId')"/>				
					</td>				
				</tr>
			</table>
			<input type="hidden" name="reportForm_moduleId" id="reportForm_moduleId" value="${moduleId}"/>	
		</form:form>
	</td>								
</tr>
</table>
</c:if>
<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script type="text/javascript">

	// -------------------------------REPORTS MENU BUTTON-------------------------------------------------
	var	getObject = new ObjSubmit("AuditTsReports.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("reports", "click", getContentFromUrl, getObject, true);

	// -------------------------------HOME MENU BUTTON----------------------------------------------------
	var getObject = new ObjSubmit("AuditTsSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("search", "click", getContentFromUrl, getObject, true);

	// ------------------------------- USER AUTOCOMPLETE --------------------------------------------------
	instantiatePersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), 'reportForm_user', 'userAutoCompleteContainer', 'reportForm_personId');

</script>	

