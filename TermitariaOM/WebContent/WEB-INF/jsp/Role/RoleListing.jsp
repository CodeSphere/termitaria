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

<%@ include file="../Messages.jsp" %>

<c:choose>
	<c:when test="${!(empty ROLES)}">		
		
		
		<fieldset style="width: 890px;">
			<legend><spring:message code="role.title.listing" /></legend>
			
				<form:form id="paginationForm" name="paginationForm" commandName="searchRoleBean">
					<c:if test="${ALL_ACTIONS eq true}">
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleDelete}">
							<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />							
						</security:authorize>						
					</c:if>
				<table class="list_results" id="listingTable">
					<tr class="list_results_header">	
						<c:if test="${ALL_ACTIONS eq true}">
							<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_RoleDelete}">
								<th style="width: 5px;" class="checkBox">
									<input type="checkbox" id="selectAllRoles">
								</th>
							</security:authorize>						
						</c:if>
							
						<th align="center" style="width:300px;">
							<a class="sortLink" id="sortLinkId" href="#">
								<spring:message code="role.name"/>
							</a>
						</th>
						<th align="center" style="width:450px;">
							<spring:message code="role.description"/>
						</th>
						<th align="center" style="width:300px;">
							<spring:message code="role.module"/>
						</th>																				
						<c:if test="${ALL_ACTIONS eq true}">
							<th align="center" style="width:120px;">
								<spring:message code="role.actions"/>
							</th>
						</c:if>	
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					
					<c:forEach var="role" items="${ROLES}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
												
						<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.roleId, document.paginationForm.selectAllRoles)">
							<c:if test="${ALL_ACTIONS eq true}">
								<c:choose>
									<c:when test="${role.isDefault ne true}">
										<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_RoleDelete}">
											<td class="checkBox" >
												<input id="roleId" name="roleId" type="checkbox" onclick="cancelCheckEvent(this)" value="${role.roleId}"/></td>						
											</td>
										</security:authorize>
									</c:when>
									<c:otherwise>																				
										<td>
											<input id="roleId" type="hidden" type="checkbox"/>											
										</td>										
									</c:otherwise>
								</c:choose>
							</c:if>
							
							<td align="center">
								<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_RoleUpdate}">
									<a href="#" onClick="getContentFromUrlDirect('Role.htm?action=edit&id=${role.roleId}','MAIN_CONTENT');" title="<spring:message code="role.edit"/>">
										${role.name}
									</a>
								</security:authorize>
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_RoleUpdate}">
									${role.name}
								</security:authorize>
							</td>
							<c:choose>
								<c:when test="${fn:length(role.sketch) ne 0 and role.tokenizedSketch ne role.truncatedTokenizedSketch}">
									<td align="center" onmouseover="Tip('${role.tokenizedSketch}' , BGCOLOR, getCSSRule('.toolTipBody').style.backgroundColor, FONTWEIGHT, getCSSRule('.toolTipBody').style.fontWeight, FONTCOLOR, getCSSRule('.toolTipBody').style.color, BORDERCOLOR, getCSSRule('.toolTipBody').style.borderColor, FADEIN, 800, FADEOUT, 800);" onmouseout="UnTip();">
										${role.truncatedTokenizedSketch}
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										${role.truncatedTokenizedSketch}
									</td>
								</c:otherwise>
							</c:choose>
							<td align="center">
								${role.module.name}
							</td>																						
							<c:if test="${ALL_ACTIONS eq true}">
								<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_RoleUpdate}">
									<td align="center">
									 	<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
											<tr>
												<td>	
									 				<a href="#" onClick="getContentFromUrlDirect('Role.htm?action=edit&id=${role.roleId}','MAIN_CONTENT');" title="<spring:message code="role.edit"/>"><img src="images/buttons/action_edit.png"/></a>
									 			</td>
									 		</tr>
									 	</table>	
									</td>
								</security:authorize>
							</c:if>	
						</tr>					
					</c:forEach>
				</table>
				<br/>
				<!-- THIS FIELD IS MANDATORY (NUMBER OF RESULTS/PAGE)-->
				<form:hidden path="resultsPerPage"/>
				<form:hidden path="currentPage"/>
				<form:hidden path="nbrOfPages"/>
				<form:hidden path="nbrOfResults"/>
				<form:hidden path="sortParam" />	
				<form:hidden path="sortDirection" />
				<form:hidden path="name" />
				<form:hidden path="moduleId" />
				<form:hidden path="organisationId" />
				<form:hidden path="branch"/>
		
				<c:import url="../Pagination.jsp">
				   <c:param name="currentPage" value="${searchRoleBean.currentPage}"/>
				   <c:param name="nbrOfPages" value="${searchRoleBean.nbrOfPages}"/>
				   <c:param name="firstPage" value="${searchRoleBean.lowerLimit}"/>
				   <c:param name="lastPage" value="${searchRoleBean.upperLimit}"/>
				   <c:param name="baseURL" value="SearchRole.htm"/>
				   <c:param name="divResults" value="RESULTS"/>
				</c:import>
		
				</form:form>
		</fieldset>		
		<c:if test="${ALL_ACTIONS eq true}">
			<script>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleDelete}">
					YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'SearchRole.htm?action=DELETE_ALL', formId: "paginationForm",
					container: "RESULTS", confirmationMessage: '<spring:message code="role.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'roleId', 
					noSelectedItemsMessage: '<spring:message code="role.delete.selectRoleMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
					
					YAHOO.util.Event.addListener("selectAllRoles", "click", selectAllCheckbox, {checkboxId: 'selectAllRoles', ids: document.paginationForm.roleId, table: document.getElementById('listingTable')}, true);
				</security:authorize>
			</script>	
		</c:if>					
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="role.no.results.message"/></div>
	</c:otherwise>
</c:choose>

<script>
	//----------------------------------------------------ORDER----------------------------------------------------------------
	YAHOO.util.Event.addListener("sortLinkId", "click", order, {url : 'SearchRole.htm?action=sort', newSortParam : 'name', 
	oldSortParam : '${searchRoleBean.sortParam}', sortDirection : '${searchRoleBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
	withContext : true}, true);
	//-------------------------------------------------------------------------------------------------------------------------
	
</script>
