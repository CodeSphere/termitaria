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
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">					
					<a href="#" id="back"><spring:message code="back"/></a>
				</security:authorize>	
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeAddToAll}">
					<a href="#" id="submenuNew" title="<spring:message code="ooo.submeniu.add.title"/>"><spring:message code="ooo.new"/></a>
				</security:authorize>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/OutOfOffice.jpg"/><spring:message code="ooo.ooo"/></span>

<table>
<tr>
	<td>
<form:form commandName="searchOOOBean" id="oooSearchForm" name="oooSearchForm">
<table class="tableSearch" >
	<tr><td class="tableAddSpacer">&nbsp;</td></tr>
	<tr>
		<td class="labelTd">
			<spring:message code="ooo.owner" />
		</td>
		<td>
			<form:hidden path="ownerFirstName" id="ownerFirstNameId"/>
			<form:hidden path="ownerLastName" id="ownerLastNameId"/>
			<div id="myOwnerAutoComplete" class="autoComplete">
				<input type="text" id="myOwnerInput" tabindex="1" class="formFieldSearch"/>
			    <div id="myOwnerContainer"></div>
			</div>			
		</td>
		
		<td class="labelTd">
			<spring:message code="ooo.startDate"/>
		</td>
		<td>
			<form:input path="startPeriod" id="oooSearchForm_startPeriod" readonly="true" tabindex="3" cssClass="required formFieldSearch"/>			
		</td>
		<td>
			<div id="oooSearchStartCalContainer"></div>
		</td>
		
		<td class="labelTd">
			<spring:message code="ooo.results"/>
		</td>
		<td>
			<form:select path="resultsPerPage" tabindex="5">
				<c:forEach var="val" items="${RESULTS_PER_PAGE}">
					<form:option value="${val}" />
				</c:forEach>
			</form:select>
		</td>
		
	</tr>
	<tr>
		<td class="labelTd">
			<spring:message code="ooo.replacement" />
		</td>
		<td>
			<form:hidden path="replacementFirstName" id="replacementFirstNameId"/>
			<form:hidden path="replacementLastName" id="replacementLastNameId"/>
			<div id="myReplacementAutoComplete" class="autoComplete">
				<input type="text" id="myReplacementInput" tabindex="2" class="formFieldSearch"/>
		    	<div id="myReplacementContainer"></div>
			</div>
		</td>
				
		<td class="labelTd">
			<spring:message code="ooo.endDate"/>
		</td>
		<td>
			<form:input path="endPeriod" id="oooSearchForm_endPeriod" readonly="true" tabindex="4" cssClass="required formFieldSearch"/>			
		</td>
		<td>
			<div id="oooSearchEndCalContainer"></div>
		</td>
		<td></td>
	</tr>
	<tr>
		<td colspan="6"></td>
		<td>
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeSearch}">	
				<input type="button" class="button" id="searchOOO" tabindex="6" value="<spring:message code="ooo.search"/>"/>
			</security:authorize>
		</td>				
	</tr>
</table>


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
	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>
	//-------------------------------SUBMENU NEW OOO---------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeAddToAll}">
		YAHOO.util.Event.addListener("submenuNew", "click", getContentFromUrl, {url : 'OutOfOffice.htm?action=NEW_OO0_FROM_SEARCH', container : 'MAIN_CONTENT',
		withContext : true}, true);
	</security:authorize>	
	//-------------------------------------------------------------------------------------

	//--------------------------------SEARCH OOO-------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeSearch}">
		YAHOO.util.Event.addListener("searchOOO", "click", submitOOOForm, {url : 'SearchOutOfOffice.htm', formId : 'oooSearchForm', 
		container : 'RESULTS', withContext : true, ownerFirstNameHiddenFieldId : 'ownerFirstNameId', ownerLastNameHiddenFieldId : 'ownerLastNameId',
		ownerInput : 'myOwnerInput', replacementFirstNameHiddenFieldId : 'replacementFirstNameId', replacementLastNameHiddenFieldId : 'replacementLastNameId',
		replacementInput : 'myReplacementInput'}, true);
	</security:authorize>	
	//-------------------------------------------------------------------------------------
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">	
		getObject = new ObjSubmit("OutOfOfficeListForPerson.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	// -------------------------------CALENDAR---------------------------------------------
	instantiateOOOSearchStartCal('<spring:message code="ooo.time"/>');
	instantiateOOOSearchEndCal('<spring:message code="ooo.time"/>');

	// -------------------------------AUTOCOMPLETE-----------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeSearch}">	
		instantiateOOOSearchPersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), "myOwnerInput", "myOwnerContainer", 
		"ownerFirstNameId", "ownerLastNameId");
		instantiateOOOSearchPersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), "myReplacementInput", "myReplacementContainer",
		 "replacementFirstNameId", "replacementLastNameId");	
		//instantiateOOOSearchOwnerAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}));
		//instantiateOOOSearchReplacementAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}));

		// -------------------------------SUBMIT SEARCH FORM------------------------------------
			submitForm('SearchOutOfOffice.htm', 'oooSearchForm', 'RESULTS');

		//--------------------------------OOO ENTER EVENT ---------------------------------------

		var keyListener = new YAHOO.util.KeyListener("oooForm", { keys:13 },  { fn:submitForm, scope: {url:'SearchOutOfOffice.htm', formId:'oooSearchForm', divId:'RESULTS'}, correctScope:true}, null ); 
		keyListener.enable();
	</security:authorize>
</script> 
