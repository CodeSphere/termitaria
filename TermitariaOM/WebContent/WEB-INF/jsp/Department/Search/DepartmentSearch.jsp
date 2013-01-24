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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptAdd}">				
					<a href="#" id="addNewDepartment" title="<spring:message code="department.submeniu.add.title"/>"><spring:message code='add'/></a>	
				</security:authorize>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Department.jpg"/><spring:message code="department.list.section.title"/></span>

<table>
	<tr>
		<td>
			<form:form commandName="searchDepartmentBean" id="searchForm" name="searchForm">
			<form:hidden id="organisationId" path="organisationId" /> 
			
			<table class="tableSearch">
				<tr>
					<td colspan="6" class="tableAddSpacer">&nbsp;</td>
				</tr>
				<tr>
					<td class="labelTd">
						<spring:message code="department.name"/>
					</td>
					<td>
						<form:input path="name" id="searchForm_name" tabindex="1" maxlength="50" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
					</td>
					<td class="labelTd">
						<spring:message code="department.manager.name"/>
					</td>
					<td>
						<form:hidden path="managerFirstName" id="managerFirstNameId"/>
						<form:hidden path="managerLastName" id="managerLastNameId"/>
						<div id="myManagerAutoComplete" class="autoComplete">
							<input type="text" id="myManagerInput" class="formFieldSearch" tabindex="4"/>
							<div id="myManagerContainer"></div>
						</div>
					</td>
					<td class="labelTd">
						<spring:message code="pagination.resultsPerPage"/>
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
						<spring:message code="department.parent.department.name"/>
					</td>
					<td>
						<form:hidden path="parentDepartmentId" id="parentDepartmentId"/>								
						<div id="myParentDepartmentAutoComplete" class="autoComplete">
							<input type="text" id="myParentDepartmentInput" tabindex="2" class="formFieldSearch"/>			
							<div id="myParentDepartmentContainer"></div>
						</div>									
					</td>
					<td colspan="4"></td>								
				</tr>
										
				<tr>
					<c:if test="${BRANCH_DISPLAY eq true}">
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ShowBranchSelect}">
							<td class="labelTd">
								<spring:message code="organisation.branch.title"/>
							</td>
							<td>
								<form:input id="branchSelection" readonly="true" path="branch" cssClass="formFieldSearch" tabindex="3"/>
							</td>
							<td colspan="4"></td>		
						</security:authorize> 
					</c:if>
				</tr>
				<tr>
					<td colspan="5"></td>
					<td >
						<input type="button" class="button" id="searchDepartment" tabindex="6" value="<spring:message code="search"/>"/>
					</td>
				</tr>
			</table>
			</form:form>
			<br/>
		</td>
	</tr>	
	<tr>
		<td>
			<div id="LOCAL_MESSAGES_CONTENT">
				<%@ include file="../../Messages.jsp" %>
			</div>
			<div id="LIST_RESULTS_CONTENT"></div>
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

	document.searchForm.name.focus();
	//----------------------------------------AUTOCOMPLETE MANAGER-------------------------------			
	instantiateManagerAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), 'myManagerInput', 'myManagerContainer', 'managerFirstNameId', 'managerLastNameId');

	//----------------------------------------AUTOCOMPLETE PARENT DEPARTMENT-------------------------------			
	instantiateParentDepartmentAutoComplete(new YAHOO.util.LocalDataSource(${JSON_DEPARTMENTS}), 'myParentDepartmentInput', 'myParentDepartmentContainer', 'parentDepartmentId');

	<c:if test="${SESS_ORGANISATION_ID ne null}">		
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptAdd}">	
			var submitObject = new ObjSubmit("DepartmentForm.htm?ACTION=ADD", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("addNewDepartment", "click", getContentFromUrl, submitObject, true);
		</security:authorize>		
	</c:if>

	YAHOO.util.Event.addListener("searchDepartment", "click", submitDepartmentForm, {url : 'DepartmentSearch.htm', formId : 'searchForm', 
	container : 'LIST_RESULTS_CONTENT', withContext : true, managerFirstNameHiddenFieldId : 'managerFirstNameId', managerLastNameHiddenFieldId : 'managerLastNameId', managerInput : 'myManagerInput', 
	parentDepartmentNameHiddenFieldId: 'parentDepartmentId', parentDepartmentInput: 'myParentDepartmentInput'}, true);
	
	//For automatic loading the first search results
	submitForm('DepartmentSearch.htm?ia_lista_de_persoane','searchForm','LIST_RESULTS_CONTENT');	

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>
	
</script>
 
<c:if test="${BRANCH_DISPLAY eq true}">
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ShowBranchSelect}">
		<script>
			//------------------------------SEARCH BRANCHES------------------------------------------------------
			var branchObject = new ObjBranch('OrganisationBranch.htm?action=BRANCHES', '<spring:message code="organisation.branch.title"/>', 'LIST_BRANCHES', 400, 200);
			YAHOO.util.Event.addListener("branchSelection", "click", manageBranches, branchObject, true);		
		</script>
	</security:authorize>		
</c:if>
