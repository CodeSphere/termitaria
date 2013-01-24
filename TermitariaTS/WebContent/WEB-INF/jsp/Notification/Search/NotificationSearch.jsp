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
				<!-- HOME -->
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->
				
				<!-- DELETE ALL -->
				<div id="active_link">
					<a href="#" title="<spring:message code="notification.submeniu.deleteAll.title"/>" id="deleteAllNotifications"><spring:message code='notification.dismissAll'/></a>
				</div>
				<div id="inactive_link" style="display: none;">
					<a class="inactive" href="#" title="<spring:message code="notification.submeniu.deleteAll.title"/>"><spring:message code='notification.dismissAll'/></a>
				</div>
				  
				<!-- NOTIFICATION SETTINGS -->
				<!--
				<div id="active_link">
					<a href="#" title="<spring:message code="notification.settings.submenu.title"/>" id="notificationSettings"><spring:message code='notification.settings.section.title'/></a>
				</div>		
				-->
				 
				<!-- NOTIFICATION LANGUAGE -->
				<!--
				<div id="active_link">
					<a href="#" title="<spring:message code="notification.language.submenu.title"/>" id="notificationLanguage"><spring:message code='notification.language.section.title'/></a>
				</div>	
				-->
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Notification.jpg"/><spring:message code="notification.section.title" /></span>

<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include	file="../../Messages.jsp"%> 
</div>

<table>
	<tr>
		<td width="700px">
			<form:form id="searchForm" commandName="notificationSearchBean" onsubmit="return false;">
				<table class="tableSearch">
					<tr>
						<td class="tableAddSpacer" colspan="7">&nbsp;</td>
					</tr>
					<tr>
						<td class="labelTd">
							<spring:message code="notification.startDate"/>
						</td>
						<td>
							<form:input path="startDate" id="searchForm_startDate" cssClass="formFieldSearch" onclick="document.getElementById('startCalendar').style.zIndex = 1;instantiateStartEndCalendars('startCalendar','searchForm_startDate', '${TODAY_DATE}', 'startTime', true, 'notificationStartSearch', document.getElementById('searchForm_endDate').value, false, '1');"/>
						</td>
						<td>
							<div id="startCalendar"></div>
							<div id="startTime"></div>
						</td>
						<td class="labelTd">
							<spring:message code="notification.message"/>
						</td>
						<td>
							<form:input path="message" id="searchForm_message" cssClass="formFieldSearch"/>
						</td>
						<td class="labelTd">
							<spring:message code="pagination.resultsPerPage"/>
						</td>
						<td>
							<form:select path="resultsPerPage">
								<c:forEach var="val" items="${RESULTS_PER_PAGE}">
									<form:option value="${val}" />
								</c:forEach>
							</form:select>
						</td>
					</tr>
					<tr>
						<td class="labelTd">
							<spring:message code="notification.endDate"/>
						</td>
						<td>
							<form:input path="endDate" id="searchForm_endDate" cssClass="formFieldSearch" onclick="document.getElementById('endCalendar').style.zIndex = 1;instantiateStartEndCalendars('endCalendar','searchForm_endDate', '${TODAY_DATE}', 'endTime', false, 'notificationEndSearch', document.getElementById('searchForm_startDate').value, false, '1');"/>
						</td>
						<td>
							<div id="endCalendar"></div>
							<div id="endTime"></div>
						</td>
						<td class="labelTd">
							<spring:message code="notification.subject"/>
						</td>
						<td>
							<form:input path="subject" id="searchForm_subject" cssClass="formFieldSearch"/>
						</td>						
						<td colspan="2"></td>
					</tr>
					<tr>
						<td align="right" colspan="7"> 		 
							<input type="button" class="button" id="searchNotification" value="<spring:message code="search"/>" onclick="submitForm('SearchNotification.htm','searchForm','RESULTS');"/>		
						</td>
					</tr>
				</table>
			</form:form>
		</td>
	</tr>
	<tr>
		<td class="tableAddSpacer">&nbsp;</td>
	</tr>
	<tr>
		<td>
			<div id="RESULTS"></div>
		</td>
	</tr>	
</table>


<script>

	submitForm('SearchNotification.htm','searchForm','RESULTS');

	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//------------ NOTIFICATION SETTINGS ---------------------------------------------------------------	

	var submitObject = new ObjSubmit("NotificationSettings.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("notificationSettings", "click", getContentFromUrl, submitObject, true);

	//------------ NOTIFICATION LANGUAGE ---------------------------------------------------------------	

	var submitObject = new ObjSubmit("NotificationLanguage.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("notificationLanguage", "click", getContentFromUrl, submitObject, true);
		
	//----------------------------------------- ENTER EVENT ON REQUESTED TAGS ---------------------------------------
	var keyListener = null;
	var submitObject = new ObjSubmit('SearchNotification.htm','searchForm','RESULTS');
	if(document.getElementById('searchForm_message')) {
		keyListener = new YAHOO.util.KeyListener("searchForm_message", { keys:13 },  { fn:submitForm, scope: submitObject, correctScope:true }, null );
		keyListener.enable();
	}
	if(document.getElementById('searchForm_subject')) {
		keyListener = new YAHOO.util.KeyListener("searchForm_subject", { keys:13 },  { fn:submitForm, scope: submitObject, correctScope:true }, null );
		keyListener.enable();
	}

	YAHOO.util.Event.addListener("deleteAllNotifications", "click", submitDeleteWithConfirmation, {url: "SearchNotification.htm?action=DELETE_ALL", formId: 'searchForm',
		container: "RESULTS", confirmationMessage: '<spring:message code="delete.confirmationMessage"/>', selectedForDeletionElementsName: null,
		noSelectedItemsMessage: '<spring:message code="delete.selectMessage"/>', selectionRequired: false}, true);
	
</script>


<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->
