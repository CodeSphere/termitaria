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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleAdd}">
					<a href="#" id="addNewRole" title="<spring:message code="role.submeniu.add.title"/>"><spring:message code='add'/> </a>
				</security:authorize>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Role.jpg"/><spring:message code="role.section.title"/></span>		

<table>
	<tr>
		<td>	
			<form:form id="searchForm" name="searchForm" commandName="searchRoleBean" onsubmit="validateAndSubmitFormWithParams('SearchRole.htm','searchForm','RESULTS', 'true'); return false;">
			<form:hidden path="organisationId" /> 
			
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<tr>
					<td class="labelTd">
						<spring:message code="role.name"/>
					</td>
					<td>
						<form:input path="name" id="searchForm_name" maxlength="30" tabindex="1" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
					</td>
					<td class="labelTd">
						<spring:message code="role.results"/>
					</td>
					<td>
						<form:select path="resultsPerPage" tabindex="4">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<c:if test="${BRANCH_DISPLAY eq true}">
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ShowBranchSelect}">
						<tr>
							<td class="labelTd">
								<spring:message code="organisation.branch.title"/>
							</td>
							<td>
								<form:input id="branchSelection" readonly="true" path="branch" tabindex="2" cssClass="formFieldSearch"/>
							</td>
						</tr>	
					</security:authorize>
				</c:if>
				
				<tr>
				<td class="labelTd">
						<spring:message code="role.module"/>
					</td>
					<td>
						<div id="MODULES">
							<c:choose>
								<c:when test="${searchRoleBean.organisationId ne null}">
									<c:choose>
										<c:when test="${MODULES ne null}">
											<div title='<spring:message code="role.choose.module"/>' id="roleSearchModulesId">
												<select name="moduleId" id="roleForm_module" class="formFieldSearch" tabindex="3" onchange="changeTitle(this.options[selectedIndex].text, 'roleSearchModulesId')">
													<option value="-1"><spring:message code="role.choose.module"/></option>
													<c:forEach var="module" items="${MODULES}">
														<option title="${module.name}" value="${module.moduleId}">${module.name}</option>
													</c:forEach>																						
												</select>
											</div>
										</c:when>
										<c:when test="${MODULES ne null  and empty MODULES}">
											<div id="NO_MODULES">
												<spring:message code="role.no.module"></spring:message>
											</div>
											<div id="MESSAGE">
											</div>
										</c:when>
									</c:choose>
								</c:when>
								<c:otherwise>
									<form:select path="moduleId" cssClass="formFieldSearch" tabindex="3">
										<form:option value="-1"><spring:message code="role.choose.org.first"/></form:option>
									</form:select>
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="3"></td>	
					<td>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleSearch}">
							<input type="button" class="button" tabindex="5" onClick="javascript:validateAndSubmitFormWithParams('SearchRole.htm','searchForm','RESULTS')" value="<spring:message code="role.search"/>"/>
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
			<div id="MESSAGES">
				<%@ include file="../Messages.jsp" %>	
			</div>
			<div id="RESULTS"></div>
		</td>
	</tr>
		
</table>

	<script language="text/javascript">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleAdd}">
			var submitObject = new ObjSubmit("Role.htm?", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("addNewRole", "click", getContentFromUrl, submitObject, true);
		</security:authorize>
		
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleSearch}">
			document.searchForm.name.focus();
			submitForm('SearchRole.htm','searchForm','RESULTS');
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



<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<c:if test="${BRANCH_DISPLAY eq true}">
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ShowBranchSelect}">
		<script>
			//------------------------------SEARCH BRANCHES------------------------------------------------------
			var branchObject = new ObjBranch('OrganisationBranch.htm?action=BRANCHES', '<spring:message code="organisation.branch.title"/>', 'LIST_BRANCHES', 400, 200);
			YAHOO.util.Event.addListener("branchSelection", "click", manageBranches, branchObject, true);
		</script>
	</security:authorize>
</c:if>
	
