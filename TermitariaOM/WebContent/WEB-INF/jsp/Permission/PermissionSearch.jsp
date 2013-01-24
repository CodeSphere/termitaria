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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
					<a href="#" id="addNewPermission" title="<spring:message code="permission.submeniu.add.title"/>"><spring:message code='add'/> </a>
				</security:authorize>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->


<span class="section_title"><img src="images/titles/Permission.jpg"/><spring:message code="permission.section.title"/></span>

<table>
	<tr>
		<td>
			<form:form id="PERMISSION_FORM" name="searchForm" commandName="searchPermissionBean" onsubmit="validateAndSubmitFormWithParams('SearchPermission.htm','PERMISSION_FORM','RESULTS'); return false;">
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<tr>
					<td class="labelTd">
						<spring:message code="permission.name"/>
					</td>
					<td>
						<form:input path="name" id="PERMISSION_FORM_NAME" tabindex="1" maxlength="30" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
					</td>
					<td class="labelTd">
						<spring:message code="permission.results"/>
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
					<td class="labelTd">
						<spring:message code="permission.module"/>
					</td>
					<td>
						<div title='<spring:message code="permission.choose.module"/>' id="permissionSearchModulesId">
							<form:select path="moduleId" tabindex="2" cssClass="formFieldSearch" onchange="changeTitle(this.options[selectedIndex].text, 'permissionSearchModulesId')">
								<form:option value="-1"><spring:message code="permission.choose.module" /></form:option>
								<c:forEach var="module" items="${MODULES}">
									<form:option title="${module.name}" value="${module.moduleId}">${module.name}</form:option>
								</c:forEach>
							</form:select>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="3"></td>
					<td>
						<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PermissionView}">	
							<input type="button" class="button" id="searchPermissionId" tabindex="4" value="<spring:message code="permission.search"/>"/>
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
			<%@ include file="../Messages.jsp" %>
			<div id="RESULTS"></div>
		</td>
	</tr>
	
</table>
	<script language="text/javascript">

		document.searchForm.name.focus();
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
			var submitObject = new ObjSubmit("Permission.htm?", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("addNewPermission", "click", getContentFromUrl, submitObject, true);
		</security:authorize>

		submitForm('SearchPermission.htm','PERMISSION_FORM','RESULTS');
	</script>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->


<script>
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PermissionView}">	
		YAHOO.util.Event.addListener("searchPermissionId", "click", validateAndSubmitForm,  {url: 'SearchPermission.htm', formId : 'PERMISSION_FORM', container : 'RESULTS',
		withContext : true}, true);
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>

	
	
	function changeTitle(title){		

	 	alert("xxx");
		var requestParameterName = 'permissionModule';				
		document.getElementById(requestParameterName).title = title;
		
	}

</script>
