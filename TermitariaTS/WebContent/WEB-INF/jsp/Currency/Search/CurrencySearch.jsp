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
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">
					<a href="#" id="addNewCurrency" ><spring:message code="add" /></a>	
				</security:authorize>												
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<span class="section_title"><img src="images/titles/Currency.jpg"><spring:message code="currency.section.title"/></span>

<table>
	<tr>
		<td>
			<form:form id="searchForm" name="searchForm" commandName="searchCurrencyBean" onsubmit="validateAndSubmitFormWithParams('CurrencySearch.htm','searchForm','RESULTS'); return false;">
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				
				<!-- first line  -->
				<tr>
					<!-- NAME -->
					<td class="labelTd">
						<spring:message code="currency.name"/>
					</td>
					<td>
						<form:input path="name" id="currencySearchForm_name" tabindex="1" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharactersForName(event);"/>
					</td>
					
					<!-- INITIALS -->
					<td class="labelTd">
						<spring:message code="currency.initials"/>
					</td>
					<td>
						<form:input path="initials" id="currencySearchForm_initials" tabindex="2" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharactersForName(event);"/>
					</td>
					
					<!-- NR OF RESULTS PER PAGE -->
					<td class="labelTd" style="width:100px">
						<spring:message code="currency.results"/>
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
					<td colspan="5"></td>
					<td>			
						<input type="button" class="button" id="searchCurrency" value="<spring:message code="search"/>" tabindex="4"/>			
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
				<%@ include file="../../Messages.jsp" %>	
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

	// -------------------------------HOME MENU BUTTON----------------------------------------------------
	//var getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//--------------------------------ADD CURRENCY BUTTON---------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">
		var getObject = new ObjSubmit("CurrencyForm.htm?ACTION=ADD&BACK_URL=CurrencySearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewCurrency", "click", getContentFromUrl, getObject, true);
	</security:authorize>	

		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencySearch}">
		// -------------------------------SUBMIT SEARCH FORM---------------------------------------------------
		submitForm('CurrencySearch.htm', 'searchForm', 'RESULTS');

		// ------------------------------- SEARCH CURRENCY ----------------------------------------------------
		YAHOO.util.Event.addListener("searchCurrency", "click", validateAndSubmitForm, { url : "CurrencySearch.htm", formId : "searchForm", container : "RESULTS", 
			withContext : true}, true);
	
		var submitObject = new ObjSubmit('CurrencySearch.htm', 'searchForm', 'RESULTS');
	
		keyListener = new YAHOO.util.KeyListener("currencySearchForm_name", { keys:13 },  { fn:validateAndSubmitForm, scope: submitObject, correctScope:true }, null );
		keyListener.enable();
	
		keyListener = new YAHOO.util.KeyListener("currencySearchForm_initials", { keys:13 },  { fn:validateAndSubmitForm, scope: submitObject, correctScope:true }, null );
		keyListener.enable();
	</security:authorize>	
</script>
