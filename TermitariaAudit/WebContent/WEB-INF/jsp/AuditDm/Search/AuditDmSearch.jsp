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
<%@ include file="../../Taglibs.jsp" %>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
			<a href="#" id="search"><spring:message code="search" /></a>
			<a href="#" id="reports"><spring:message code="audit.dm.reports" /></a>											
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Audit.jpg"><spring:message code="audit.dm"/></span>

<table>
<tr>
	<td>
<form:form commandName="searchAuditDmBean" id="searchForm">

<form:hidden path="personId" id="personId"/>

<table class="tableSearch" >
	<tr><td class="tableAddSpacer">&nbsp;</td></tr>
	<tr>
		<td class="labelTd">
			<spring:message code="audit.dm.user" />
		</td>
		<td>
			<div id="userAutoComplete" class="autoComplete">			
				<input id="auditSearchForm_user" type="text" tabindex="1" class="formFieldSearch"/>
				<div id="userAutoCompleteContainer"></div>
			</div>
		</td>
	
		<td class="labelTd">
			<spring:message code="audit.dm.startDate"/>
		</td>
		
		<td>
			<form:input path="startDate" id="auditSearchForm_startDate" readonly="true" cssClass="formFieldSearch" tabindex="2" onclick="document.getElementById('startCalendar').style.zIndex = 1;instantiateStartEndCalendars('startCalendar','auditSearchForm_startDate', '${TODAY_DATE}', 'startTime', true, 'auditDmStartSearch', document.getElementById('auditSearchForm_endDate').value, false, '1');"/>
		</td>
		<td>
			<div id="startCalendar"></div>
			<div id="startTime"></div>
		</td>
		
		<td class="labelTd">
			<spring:message code="audit.dm.results"/>
		</td>
		<td>
			<form:select path="resultsPerPage" tabindex="3">
				<c:forEach var="val" items="${RESULTS_PER_PAGE}">
					<form:option value="${val}" />
				</c:forEach>
			</form:select>
		</td>		
	</tr>
	
	<tr>			
		<td class="labelTd">
			<spring:message code="audit.dm.message"/>
		</td>
		<td>
			<form:input path="message" id="auditSearchForm_message" readonly="false" cssClass="formFieldSearch" tabindex="4"/>
		</td>
		
		<td class="labelTd">
			<spring:message code="audit.dm.endDate"/>
		</td>
		
		<td>
			<form:input path="endDate" id="auditSearchForm_endDate" readonly="true" cssClass="formFieldSearch" tabindex="5" onclick="document.getElementById('endCalendar').style.zIndex = 1;instantiateStartEndCalendars('endCalendar','auditSearchForm_endDate', '${TODAY_DATE}', 'endTime', false, 'auditDmEndSearch', document.getElementById('auditSearchForm_startDate').value, false, '1');"/>
		</td>
		<td>
			<div id="endCalendar"></div>
			<div id="endTime"></div>
		</td>			
	</tr>
	
	<tr>
		<td class="labelTd">
			<spring:message code="audit.dm.event" />
		</td>
		<td>
			<form:select path="event" id="auditSearchForm_event" cssClass="formFieldSearch" tabindex="6">
				<form:option value="-1"><spring:message code="audit.dm.choose.event" /></form:option>
				<c:forEach var="event" items="${AUDIT_EVENTS_DM}">
					<form:option value="${event}"><spring:message code="${event}"/></form:option>
				</c:forEach>
			</form:select>
		</td>	
		
		<c:set var="searchButtonTabIndex" value="7"/>									
		<c:if test="${IS_ADMIN_IT == true}">	
			<c:set var="searchButtonTabIndex" value="8"/>				
			<td class="labelTd">
				<spring:message code="audit.dm.organisation"/>
			</td>
			<td>
				<form:select path="organisationId" cssClass="formFieldSearch" onchange="instantiatePersonAutoCompleteForOrganisation(this.options[selectedIndex].value, 'auditSearchForm_user', 'userAutoCompleteContainer', 'personId')" tabindex="7">
					<form:option value="-1"><spring:message code="audit.dm.choose.organisation" /></form:option>
					<c:forEach var="org" items="${ORGANIZATIONS_ALL}">
						<form:option value="${org.organisationId}">${org.organisationName}</form:option>
					</c:forEach>
				</form:select>
			</td>						
		</c:if>				
		<td></td>
	</tr>
	<tr>
		<td colspan="5"></td>
		<td>			
			<input type="button" class="button" id="searchAuditDm" value="<spring:message code="search"/>" tabindex="${searchButtonTabIndex}"/>			
		</td>				
	</tr>
</table>
<input type="hidden" name="moduleId" value="${moduleId}"/>
</form:form>
<br/>
	</td>
</tr>
<tr>
	<td>
		<div id="RESULTS"></div>
	</td>
</tr>
</table>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script type="text/javascript">

	// -------------------------------REPORTS MENU BUTTON-------------------------------------------------
	var	getObject = new ObjSubmit("AuditDmReports.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("reports", "click", getContentFromUrl, getObject, true);

	// -------------------------------HOME MENU BUTTON----------------------------------------------------
	var getObject = new ObjSubmit("AuditDmSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("search", "click", getContentFromUrl, getObject, true);

	// -------------------------------SUBMIT SEARCH FORM---------------------------------------------------
	submitForm('AuditDmSearch.htm', 'searchForm', 'RESULTS');

	// ------------------------------- SEARCH AUDIT DM ----------------------------------------------------
	YAHOO.util.Event.addListener("searchAuditDm", "click", validateAndSubmitAuditSearchForm, { url : "AuditDmSearch.htm", formId : "searchForm", container : "RESULTS", userHiddenFormElementId: "personId", userInputAutoCompleteId: "auditSearchForm_user"}
				, true);

	// ------------------------------- USER AUTOCOMPLETE --------------------------------------------------
	instantiatePersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), 'auditSearchForm_user', 'userAutoCompleteContainer', 'personId');
</script>
