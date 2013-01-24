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
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobAdd}">
				<a href="#" id="addNewJob" title="<spring:message code="job.submeniu.add.title"/>"><spring:message code='add'/> </a>
			</security:authorize>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Job.jpg"/><spring:message code="job.section.title"/></span>

<table>
	<tr>
		<td>
			<form:form id="searchForm" name="searchForm" commandName="searchJobBean" onsubmit="validateAndSubmitFormWithParams('SearchJob.htm','searchForm','RESULTS'); return false;">
			<form:hidden path="organisationId" /> 
	
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<tr>
					<td class="labelTd">
						<spring:message code="job.name"/>
					</td>
					<td>
						<form:input path="name" id="searchForm_name" tabindex="1" maxlength="30" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
					</td>
					<td class="labelTd">
						<spring:message code="job.results"/>
					</td>
					<td>
						<form:select path="resultsPerPage" tabindex="4">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ShowBranchSelect}">
					<c:if test="${BRANCH_DISPLAY eq true}">
						<tr>
							<td class="labelTd">
								<spring:message code="organisation.branch.title"/>
							</td>
							<td>
								<form:input id="branchSelection" readonly="true" path="branch" tabindex="2" cssClass="formFieldSearch"/>
							</td>
						</tr>
					</c:if>
				</security:authorize>
				<tr>
					<td class="labelTd">
						<spring:message code="job.status"/>
					</td>
					<td>
						<form:select path="status" tabindex="3" cssClass="formFieldSearch">
							<form:option value="-1"><spring:message code="please.select" /></form:option>
							<form:option value="1"><spring:message code="job.enabled" /></form:option>
							<form:option value="0"><spring:message code="job.disabled" /></form:option>
						</form:select> 		
					</td>
				</tr>
				<tr>
					<td colspan="3"></td>
					<td>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobSearch}">
							<input type="button" class="button" id="searchJob" tabindex="5" value="<spring:message code="job.search"/>"/>
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

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script language="text/javascript">

	document.searchForm.name.focus();	

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>

	//----------------------------------------------------------SEARCH JOB--------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobSearch}">
		YAHOO.util.Event.addListener("searchJob", "click", validateAndSubmitForm, { url : "SearchJob.htm", formId : "searchForm", container : "RESULTS",
		withContext : true}, true);
	</security:authorize>
	//----------------------------------------------------------------------------------------------------------------------------------

	//----------------------------------------------------------ADD NEW JOB-------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobAdd}">
		var submitObject = new ObjSubmit("Job.htm?", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewJob", "click", getContentFromUrl, submitObject, true);
	</security:authorize>
	//----------------------------------------------------------------------------------------------------------------------------------
	
	//-----------------------------------------------------------SUBMIT FORM------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobSearch}">
		submitForm('SearchJob.htm','searchForm','RESULTS');
	</security:authorize>
	//----------------------------------------------------------------------------------------------------------------------------------
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
