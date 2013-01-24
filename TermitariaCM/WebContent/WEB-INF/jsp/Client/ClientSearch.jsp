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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientAdd}">
					<a href="#" id="addNewClient"><spring:message code="add" /></a>	
				</security:authorize>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Client.jpg"><spring:message code="client"/></span>

<table>
<tr>
	<td>
<form:form commandName="searchClientBean" id="searchForm">

<!-- ========================================= ACTIVE CLIENT STATUS ================================== -->
<input type="hidden" name="status" value="${CLIENT_STATUS_ACTIVE}" />

<table class="tableSearch" >
	<tr><td class="tableAddSpacer">&nbsp;</td></tr>
	<tr>
		<!-- ================================================== ADDRESS ====================================== -->
		<td class="labelTd" style="width:60px">
			<spring:message code="client.address"/>
		</td>
		<td>
			<form:input path="address" id="clientSearchForm_address" cssClass="formFieldSearch"/>
		</td>
		
		<!-- ================================================== EMAIL ======================================== -->
		<td class="labelTd" style="width:60px">
			<spring:message code="client.email"/>
		</td>
		<td>
			<form:input path="email" id="clientSearchForm_email" cssClass="formFieldSearch"/>
		</td>
		
		<!-- ================================================== RESULTS ======================================== -->
		<td class="labelTd" style="width:60px">
			<spring:message code="client.results"/>
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
		<!-- ================================================== CLIENT TYPE ======================================== -->
		<td class="labelTd">
			<spring:message code="client.type"/>
		</td>
		<td>
			<form:select path="type" id="clientSearchForm_type" cssClass="formFieldSearch" onchange="displayPersonOrFirmClientFields(this.options[selectedIndex].value, 'firmClientInfo', 'personClientInfo')">
				<form:option value="-1" title="<spring:message code='select'/>"><spring:message code='select'/></form:option>
				<c:forEach var="type" items="${CLIENT_TYPE}">
					<option title='<spring:message code="${type.label}"/>' value="${type.value}"><spring:message code="${type.label}"/></option>
				</c:forEach>
			</form:select>
		</td>
		
		<!-- ================================================== FIRM CLIENT NAME ======================================== -->
		<td class="labelTd firmClientInfo">
			<spring:message code="client.firm.c_name"/>
		</td>
		<td class="firmClientInfo">
			<form:input path="c_name" id="clientSearchForm_c_name" cssClass="formFieldSearch firmClientInfo"/>
		</td>
	
		<!-- ================================================== PERSON CLIENT NAME ======================================== -->
		<td class="labelTd personClientInfo" style="display:none">
			<spring:message code="client.person.p_firstName"/>
		</td>
		<td style="display:none" class="personClientInfo">
			<form:input path="p_firstName" id="clientSearchForm_p_firstName" cssClass="formFieldSearch personClientInfo"/>
		</td>
	</tr>
	
	<tr>
		<td class="labelTd personClientInfo" style="display:none">
			<spring:message code="client.person.p_lastName"/>
		</td>
		<td style="display:none" class="personClientInfo">
			<form:input path="p_lastName" id="clientSearchForm_p_lastName" cssClass="formFieldSearch personClientInfo"/>
		</td>
	</tr>
	<tr>
		<td colspan="7"></td>
		<td>			
			<input type="button" class="button" id="searchClient" value="<spring:message code="search"/>" onClick="javascript:validateAndSubmitFormWithParams('ClientSearch.htm','searchForm','RESULTS')"/>			
		</td>				
	</tr>
</table>
</form:form>
		<br/>
	</td>
	</tr>
	<tr>
		<td>
			<div id="MESSAGES">
				<%@ include file="../Messages.jsp" %>	
			</div>
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

	// -------------------------------SUBMIT SEARCH FORM---------------------------------------------------
	submitForm('ClientSearch.htm', 'searchForm', 'RESULTS');

	// ------------------------------- ADD MENU BUTTON ----------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientAdd}">
		var	getObject = new ObjSubmit("ClientForm.htm?ACTION=ADD&BACK_URL=ClientSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewClient", "click", getContentFromUrl, getObject, true);
	</security:authorize>	

	// ------------------------------- HOME ----------------------------------------------------------------
	var getObject = new ObjSubmit("ClientSearch.htm", "searchForm", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	
</script>
