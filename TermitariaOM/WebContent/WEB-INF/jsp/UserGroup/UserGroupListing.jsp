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
	<c:when test="${!(empty USER_GROUPS)}">	
		<fieldset style="width: 600px;">
			<legend><spring:message code="usergroup.title.listing" /></legend>
				<form:form id="paginationForm" name="paginationForm" commandName="searchUserGroupBean">
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeleteUserGroup}">
						<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
					</security:authorize>
					<table class="list_results" id="listingTable">
						<tr class="list_results_header">	
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeleteUserGroup}">
								<th style="width: 5px;" class="checkBox">
									<input type="checkbox" id="selectAllUserGroups">
								</th>			
							</security:authorize>
					
							<th align="center" style="width:160px;">
								<a class="sortLink" id="sortLinkId" href="#">
								<spring:message code="usergroup.name"/>
								</a>
							</th>
							<c:if test="${BRANCH_DISPLAY eq true}">
							<th align="center" style="width:80px;">
									<spring:message code="usergroup.organisation"/>
							</th>
							</c:if>
							<th align="center" style="width:300px;">
								<spring:message code="usergroup.description"/>
							</th>
							<th align="center" style="width:120px;">
								<spring:message code="usergroup.actions"/>
							</th>
						</tr>
						<c:set var="i" value="0"/>						
						<c:set var="cssClass" value=""/>
						<c:forEach var="usergroup" items="${USER_GROUPS}">			
							<c:set var="i" value="${i + 1}"/>												
							<c:choose>
								<c:when test="${i % 2 == 0}">
									<c:set var="cssClass" value="even_row"/>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="odd_row"/>
								</c:otherwise>
							</c:choose>
							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, paginationForm.userGroupId, document.paginationForm.selectAllUserGroups)">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeleteUserGroup}">
								<td class="checkBox">
									<input id="userGroupId" name="userGroupId" type="checkbox" onclick="cancelCheckEvent(this)" value="${usergroup.userGroupId}"/></td>						
								</td>
							</security:authorize>
							<td align="center">
								<a href="#" onClick="displayInfoPanel('UserGroupView.htm?userGroupId=${usergroup.userGroupId}', '${usergroup.panelHeaderName}', '${usergroup.name}', '320px');" title="<spring:message code="usergroup.view"/>">
									${usergroup.name}
								</a>
							</td>
							<c:if test="${BRANCH_DISPLAY eq true}">
								<td align="center">
									${usergroup.organisation.name}
								</td>
							</c:if>
							<c:choose>
								<c:when test="${fn:length(usergroup.description) ne 0 and usergroup.tokenizedDescription ne usergroup.truncatedTokenizedDescription}">
									<td align="center" onmouseover="Tip('${usergroup.tokenizedDescription}' , BGCOLOR, getCSSRule('.toolTipBody').style.backgroundColor, FONTWEIGHT, getCSSRule('.toolTipBody').style.fontWeight, FONTCOLOR, getCSSRule('.toolTipBody').style.color, BORDERCOLOR, getCSSRule('.toolTipBody').style.borderColor, FADEIN, 800, FADEOUT, 800);" onmouseout="UnTip();">
										${usergroup.truncatedTokenizedDescription}
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										${usergroup.truncatedTokenizedDescription}
									</td>
								</c:otherwise>
							</c:choose>		
							<td align="center">
								<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
									<tr>
										<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_UpdateUserGroup}">
											<td>											
												 <a href="#" onClick="getContentFromUrlDirect('UserGroup.htm?action=edit&id=${usergroup.userGroupId}','MAIN_CONTENT');" title="<spring:message code="usergroup.edit"/>"><img src="images/buttons/action_edit.png"/></a>										
											</td>
										</security:authorize>
										<td>	
											<a href="#" onClick="displayInfoPanel('UserGroupView.htm?userGroupId=${usergroup.userGroupId}', '${usergroup.panelHeaderName}', '${usergroup.name}', '320px');" title="<spring:message code="usergroup.view"/>"><img src="images/buttons/action_view.png"/></a>
										</td>
									</tr>
								</table>			
							</td>
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
		<form:hidden path="branch"/>

		<c:import url="../Pagination.jsp">
		   <c:param name="currentPage" value="${searchUserGroupBean.currentPage}"/>
		   <c:param name="nbrOfPages" value="${searchUserGroupBean.nbrOfPages}"/>
		   <c:param name="firstPage" value="${searchUserGroupBean.lowerLimit}"/>
		   <c:param name="lastPage" value="${searchUserGroupBean.upperLimit}"/>
		   <c:param name="baseURL" value="SearchUserGroup.htm"/>
		   <c:param name="divResults" value="RESULTS"/>
		</c:import>

		</form:form>	
		</fieldset>
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeleteUserGroup}">
			<script>
					submitObject = new ObjSubmitWithConfirmation();
					YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'SearchUserGroup.htm?action=DELETE_ALL', formId:  "paginationForm",
					container: "RESULTS", confirmationMessage: '<spring:message code="usergroup.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'userGroupId', 
					noSelectedItemsMessage: '<spring:message code="usergroup.delete.selectUsergroupMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
					
					YAHOO.util.Event.addListener("selectAllUserGroups", "click", selectAllCheckbox, {checkboxId: 'selectAllUserGroups', ids: document.paginationForm.userGroupId, 
							table: document.getElementById('listingTable')}, true);
			</script>	
		</security:authorize>				
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="usergroup.no.results.message"/></div>
	</c:otherwise>
</c:choose>

<script>
	//----------------------------------------------------ORDER----------------------------------------------------------------
	YAHOO.util.Event.addListener("sortLinkId", "click", order, {url : 'SearchUserGroup.htm?action=sort', newSortParam : 'name', 
	oldSortParam : '${searchUserGroupBean.sortParam}', sortDirection : '${searchUserGroupBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
	withContext : true}, true);
	//-------------------------------------------------------------------------------------------------------------------------
	
</script>
