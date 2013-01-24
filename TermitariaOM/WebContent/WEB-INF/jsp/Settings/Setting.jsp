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
				<a href="#" id="home"><spring:message code="home" /></a>
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
					<a href="#" id="back"><spring:message code="back" /></a>	
				</security:authorize>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><spring:message code="setting.section.title"/></span>

<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
</div>


<form:form name= "settingForm" id="settingForm" commandName="settingBean">
<table class="tableAdd">
	<tr>
		<td class="labelTd">
			<spring:message code="setting.parameter"/>
		</td>
		<td>
			<form:input path="parameter" readonly="true" cssClass="formField"/>
		</td>
	</tr>
	<tr>
		<td class="labelTd">
			<spring:message code="setting.value"/>
		</td>
		<td>
			<form:select path="value" id="settingForm_value" cssClass="formField">
				<c:forEach var="opt" items="${OPTIONS}">
							<c:choose>
								<c:when test="${SEL == opt.code}">
									<option value="${opt.code}" SELECTED>${opt.value}</option>
								</c:when>
								<c:otherwise>
									<option value="${opt.code}">${opt.value}</option>
								</c:otherwise>
							</c:choose>
				</c:forEach>
			</form:select>
		</td>
	</tr>
	<tr>
		<td class="formActions" colspan="2" align="right">
			<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
				<input type="button" class="button" id="saveSetting" value="<spring:message code="setting.save"/>"/>
			</security:authorize>
		</td>
	</tr>
</table>
	<br/>
	<form:hidden path="settingId" />
	<form:hidden path="localizationId" />
	<form:hidden path="status" />

</form:form>




<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
	
		
		getObject = new ObjSubmit("SettingList.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);				
	
	
		getObject = new ObjSubmit("Setting.htm?action=save", "settingForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("saveSetting", "click", validateAndSubmitForm, getObject, true);
	
	
		//----------------------------------------- PERMISION ENTER EVENT ---------------------------------------
	
		var submitObject = new ObjSubmit("Setting.htm?action=save", "settingForm", "MAIN_CONTENT");
		var keyListener = new YAHOO.util.KeyListener("settingForm", { keys:13 },  { fn: validateAndSubmitForm, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();	
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>

</script>
