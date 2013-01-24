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
<%@page import="ro.cs.om.common.IConstant;"%>


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
				<a href="#" id="home"><spring:message code="home" /></a>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        <!--  <div id="CONTENT"> -->
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><spring:message code="setting.section.title"/></span>


<%@ include file="../Messages.jsp" %>

<fieldset style="width: 400px;">
	<legend><spring:message code="setting.title.listing" /></legend>	
		<input type="button" class="button" id="resetAll" value="<spring:message code="setting.reset"/>"/>
		<table class="list_results">
			<tr class="list_results_header">
				<th align="center" style="width:100px;">
					<spring:message code="setting.parameter"/>
				</th>
				<th align="center" style="width:100px;">
					<spring:message code="setting.description"/>
				</th>
				<th align="center" style="width:100px;">
					<spring:message code="setting.value"/>
				</th>
				<th align="center" style="width:100px;">
					<spring:message code="setting.actions"/>
				</th>
			</tr>
			<c:set var="i" value="0"/>
			<c:set var="cssClass" value=""/>
			<c:forEach var="setting" items="${SETTINGS}">
				<c:set var="i" value="${i + 1}"/>
					<c:choose>
						<c:when test="${i % 2 == 0}">
							<c:set var="cssClass" value="even_row"/>
						</c:when>
						<c:otherwise>
							<c:set var="cssClass" value="odd_row"/>
						</c:otherwise>
					</c:choose>	
					<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">
						<td>
							${setting.parameter}
						</td>
						<td>
							${setting.description}
						</td>
						<td>
							${setting.value}
						</td>
						<td>
							<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
								<tr>
									<td>	
										<a href="#" onClick="getContentFromUrlDirect('Setting.htm?id=${setting.settingId}', 'MAIN_CONTENT')" title="<spring:message code="setting.modify"/>"><img src="images/buttons/action_edit.png"/></a>
									</td>
								</tr>
							</table>			
						</td>
					</tr>			
			</c:forEach>
		</table>
</fieldset>		
<!-- ///////////////////////////////////////////////////////////////////////////// -->



<script>

	var parameters = {url: "SettingList.htm?action=reset", container: "MAIN_CONTENT"};
	YAHOO.util.Event.addListener("resetAll", "click", getContentFromUrl, parameters, true);

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>
	
</script>
