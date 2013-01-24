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
				<a href="#" id="home"><spring:message code="home" /></a>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}">					
					<a href="#" id="addNewPerson" title="<spring:message code="person.submeniu.add.title"/>"><spring:message code='add'/></a>					
				</security:authorize>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->
		
<span class="section_title"><img src="images/titles/Person.jpg"/><spring:message code="employees.section.title"/></span>

<table>
	<tr>
		<td>
			<form:form commandName="searchPersonBean" id="searchForm" name="searchForm" onsubmit="validateAndSubmitFormWithParams('PersonSearch.htm','searchForm','LIST_RESULTS_CONTENT'); return false;">
			<form:hidden path="organisationId" /> 
			
			<table class="tableSearch" border="0">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<tr>
					<td class="labelTd">
						<spring:message code="person.lastName"/>
					</td>
					<td>
						<form:input path="lastName" id="searchForm_lastName" tabindex="1" maxlength="30" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharactersForName(event);"/>
					</td>
					<td class="labelTd">
						<spring:message code="person.firstName"/>
					</td>
					<td>
						<form:input path="firstName" id="searchForm_firstName" tabindex="2" maxlength="30" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharactersForName(event);"/>
					</td>					
					<td class="labelTd">
						<spring:message code="pagination.resultsPerPage"/>
					</td>
					<td colspan="6">
						<form:select path="resultsPerPage" tabindex="9">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					
				</tr>
				<tr> 		
					<c:choose>							
						<c:when test="${BRANCH_DISPLAY eq true}">
							<tr>
								<td class="labelTd">
									<spring:message code="person.username"/>
								</td>
							
								<td>
									<form:input path="username" id="searchForm_userName" tabindex="3" maxlength="30" cssClass="formFieldSearch"/>
								</td>
							</tr>
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ShowBranchSelect}">
								<td class="labelTd">
									<spring:message code="organisation.branch.title"/>
								</td>
								<td>
									<form:input id="branchSelection" readonly="true" path="branch" tabindex="4" cssClass="formFieldSearch" />
								</td>
							</security:authorize>
						</c:when>
						<c:otherwise>
							<td class="labelTd">
								<spring:message code="person.username"/>
							</td>
						
							<td>
								<form:input path="username" id="searchForm_userName" tabindex="3" cssClass="formFieldSearch"/>
							</td>
						</c:otherwise>
					</c:choose>
																		
					<td class="labelTd">
						<spring:message code="person.department"/>
					</td>
					<td>										
						<form:hidden path="departmentId" id="departmentId"/>								
						<div id="myDepartmentAutoComplete" class="autoComplete">
							<input type="text" id="myDepartmentInput" tabindex="5" class="formFieldSearch"/>			
							<div id="myDepartmentContainer"></div>
						</div>				
					</td>
					<td></td><td></td>
				</tr>
				<tr>
					<td class="labelTd">
						<spring:message code="person.sex"/>
					</td>
					<td>
						<font class="labelTd"><spring:message code="person.sex.both"/></font><form:radiobutton path="sex" tabindex="6" value=' '/>
						<font class="labelTd"><spring:message code="person.sex.m"/></font><form:radiobutton path="sex" tabindex="7" value="M"/> 
						<font class="labelTd"><spring:message code="person.sex.f"/></font><form:radiobutton path="sex" tabindex="8" value="F"/>
					</td>		
					<td colspan="6" align="right">
						<input type="button" class="button" id="searchPersonId" tabindex="10" value="<spring:message code="search"/>"/>						
					</td>
				</tr>
			</table>
			</form:form>
			<br/>
		</td>
	</tr>
	
	<tr>
		<td>
			<%@ include file="../../Messages.jsp" %>
			<div id="LIST_RESULTS_CONTENT" ></div>
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

<script>

	//----------------------------------------AUTOCOMPLETE DEPARTMENT-------------------------------			
	instantiatePersonDepartmentAutoComplete(new YAHOO.util.LocalDataSource(${JSON_DEPARTMENTS}), 'myDepartmentInput', 'myDepartmentContainer', 'departmentId');

	document.searchForm.lastName.focus();

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>

	<c:if test="${SESS_ORGANISATION_ID ne null}">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}">				
			submitObject = {url: "PersonForm.htm?ACTION=NEW", container: "MAIN_CONTENT", panelTitle: '<spring:message code="person.add.section.title"/>'};
			YAHOO.util.Event.addListener("addNewPerson", "click", getContentFromUrl, submitObject, true);			
		</security:authorize>
	</c:if>
	
//	var objGetContentFromUrl = new ObjSubmit("PersonSearchGetDepartmentsSelect.htm?organisationId=" + 
//			document.getElementById('organisationId').options[document.getElementById('organisationId').selectedIndex].value, "departmentsSelectContainer", false);
//	alert("Add Listener " + YAHOO.util.Event.addListener("organisationId", "onchange", getContentFromUrl, objGetContentFromUrl, true));
	//----------------------------------------------------SEARCH PERSON(NOT WORKING)---------------------------------------------------------------
	//alert(document.getElementById('LIST_RESULTS_CONTENT'));
	//YAHOO.util.Event.addListener("searchPersonId", "click", submitForm,  {url: 'PersonSearch.htm', formId : 'searchForm', container : 'LIST_RESULTS_CONTENT',
	//withContext : true}, true);
	//--------------------------------------------------------------------------------------------------------------------------------
	
	YAHOO.util.Event.addListener("searchPersonId", "click", submitPersonForm, {url : 'PersonSearch.htm', formId : 'searchForm', 
	container : 'LIST_RESULTS_CONTENT', withContext : true,
	departmentHiddenFieldId: 'departmentId', departmentInput: 'myDepartmentInput'}, true);
		
	//For automatic loading the first search results	
	submitForm('PersonSearch.htm?ia_lista_de_persoane','searchForm','LIST_RESULTS_CONTENT');
</script>


<c:if test="${BRANCH_DISPLAY eq true}">
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ShowBranchSelect}">
		<script>
			//------------------------------SEARCH BRANCHES------------------------------------------------------
			var branchObject = new ObjBranch('OrganisationBranch.htm?action=BRANCHES', '<spring:message code="organisation.branch.title"/>', 'LIST_BRANCHES', 400, 200, '${SESS_ORGANISATION_ID}', '${SESS_ORGANISATION_NAME}');
			YAHOO.util.Event.addListener("branchSelection", "click", manageBranches, branchObject, true);
		</script>
	</security:authorize>
</c:if>
	
	

