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
<body onload="setFocus();"></body>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
				<a href="#" id="home"><spring:message code="home" /></a>	
				<a href="#" id="back"><spring:message code="back"/></a>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><spring:message code="organisation.ceo.section.title"/></span>
<br/>

<%@ include file="../Messages.jsp" %>

<form:form commandName="departmentBean" id="CEOForm" name="CEOForm">
<form:hidden path="departmentId" id="departmentId" />
<table class="tableAdd">
	<tr>
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>
		<td class="labelTd">
			<spring:message code="person.select.ceo" /><span id="required">*</span>
		</td>		
		
		<!-- ====================================MANANGER========================================================= -->							
		<td>
			<form:hidden path="managerId" id="managerId" />
			<div id="myAutoComplete" class="formField autoComplete">
				<c:choose>
					<c:when test="${MANAGER_NAME != null}">
						<input id="managerAutoComplete" type="text" value="${MANAGER_NAME}" tabindex="1" cssClass="formField  validate-not-first validation-failed-sel" />
					</c:when>
					<c:otherwise>
						<input id="managerAutoComplete" type="text" tabindex="1" cssClass="formField  validate-not-first validation-failed-sel" />
					</c:otherwise>
				</c:choose>
				<div id="autoCompleteContainer"></div>
			</div>		
			<div id="ceo_message"></div>
		</td>							
	</tr>	
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="formActions" colspan="2" align="right">
			<security:authorize ifAllGranted=" ${PERMISSION_CONSTANT.OM_OrgAddCeo}">
				<input type="button" class="button" id="save"  tabindex="2" value="<spring:message code="organisation.ceo.save"/>"/>
			</security:authorize>
			<input type="button" class="button" id="cancel" tabindex="3" value="<spring:message code="cancel"/>"/>							
		</td>
	</tr>
</table>
<br/>

</form:form>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);

	getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);
	
	// ------------------------------------ VALIDATION AND SUBMIT --------------------------------
	<security:authorize ifAllGranted=" ${PERMISSION_CONSTANT.OM_OrgAddCeo}">

		// ----------------------------------------AUTOCOMPLETE CEO-------------------------------		
		instantiatePersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), 'managerAutoComplete', 'autoCompleteContainer', 'managerId');		
		
		var submitObject = new ObjSubmit("OrganisationCeo.htm?", "CEOForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitSaveCeo, submitObject, true);	
	</security:authorize>
	//---------------------------------- END VALIDATION AND SUBMIT -------------------------------	
	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>
	
	
</script>
