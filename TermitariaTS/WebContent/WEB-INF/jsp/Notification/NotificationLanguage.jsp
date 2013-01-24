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
			
				<!-- HOME -->
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->
				
				<!-- NOTIFICATION SEARCH -->
				<!--  
				<div id="active_link">
					<a href="#" title="<spring:message code="notification.search.submenu.title"/>" id="notificationSearch"><spring:message code='notification.search'/></a>
				</div>	
				-->
				<!-- NOTIFICATION SETTINGS -->

				<div id="active_link">
					<a href="#" title="<spring:message code="notification.settings.submenu.title"/>" id="notificationSettings"><spring:message code='notification.settings.section.title'/></a>
				</div>		
				
				<!-- NOTIFICATION LANGUAGE -->
				<div id="active_link">
					<a href="#" title="<spring:message code="notification.language.submenu.title"/>" id="notificationLanguage"><spring:message code='notification.language.section.title'/></a>
				</div>	
			</div>
				
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->




<span class="section_title"><img src="images/titles/NotificationLanguage.jpg"/><spring:message code="notification.language.section.title" /></span>

<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include	file="../Messages.jsp"%> 
</div>


<form:form id="notificationLanguageForm" name="notificationLanguageForm" commandName="notificationLanguageBean">
<table class="tableSearch" cellpadding="0" cellspacing="0"><tr><td>
<table>
<!-- //////////////////////////////////LANGUAGES////////////////////////////////////////// -->
	<tr>		
		<td class="labelTd" > 
			<spring:message code="notification.language"/>
		</td>
		<td>
			<form:select path="language" cssClass="formFieldSearch" tabindex="1" id="notificationLanguageForm_language">
				<c:forEach var="lang" items="${NOM_SUPPORTED_LANGUAGES}">
					<c:choose>
						<c:when test="${notificationLanguageBean.language eq lang}">
							<option value="${lang}" title="${lang}"  selected="selected">${lang}</option>
						</c:when>
						<c:otherwise>
							<option value="${lang}" title="${lang}" >${lang}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</form:select>
		</td>		
	</tr>
</table>
</td></tr>
<tr>
	<td class="formActions" align="right">
		<c:if test="${ERRORS eq null}">	
			<input type="button" class="button" id="notificationLanguageForm_save"  value="<spring:message code="save"/>"/>					
		</c:if>
	</td>
</tr>	
</table>
</form:form>	

<script>
	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject,true);

	//------------ NOTIFICATION SEARCH ---------------------------------------------------------------	

	var submitObject = new ObjSubmit("SearchNotification.htm", "","MAIN_CONTENT");
	YAHOO.util.Event.addListener("notificationSearch", "click",getContentFromUrl, submitObject, true);

	//------------ NOTIFICATION SETTINGS ---------------------------------------------------------------	

	var submitObject = new ObjSubmit("NotificationSettings.htm", "","MAIN_CONTENT");
	YAHOO.util.Event.addListener("notificationSettings", "click",getContentFromUrl, submitObject, true);

	//------------ NOTIFICATION LANGUAGE ---------------------------------------------------------------	

	var submitObject = new ObjSubmit("NotificationLanguage.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("notificationLanguage", "click", getContentFromUrl, submitObject, true);


</script>

<c:if test="${notificationSettingsFormBean.projectId != -2 && ERRORS eq null}">	
	<script>
		//------------ SAVE LANGUAGE ---------------------------------------------------------------

		var submitObject = new ObjSubmit("NotificationLanguage.htm", "notificationLanguageForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("notificationLanguageForm_save", "click", validateAndSubmitForm, submitObject, true);

	</script>
</c:if>
<!-- //////////////////////////////////////////////////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
