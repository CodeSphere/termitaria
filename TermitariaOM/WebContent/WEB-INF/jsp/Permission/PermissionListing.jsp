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
	<c:when test="${!(empty PERMISSIONS)}">
		
		<fieldset style="width: 750px;">
			<legend><spring:message code="permission.title.listing" /></legend>
			
				<form:form id="paginationForm" name="paginationForm" commandName="searchPermissionBean">
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">				
						<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
					</security:authorize>
				<table class="list_results" id="listingTable">	
					<tr class="list_results_header">
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
							<th style="width: 5px;" class="checkBox">
								<input type="checkbox" id="selectAllPermissions">
							</th>
						</security:authorize>							
						<th align="center" style="width:160px;">
							<a class="sortLink" id="sortLinkNameId" href="#">
								<spring:message code="permission.name"/>
							</a>
						</th>
						<th align="center" style="width:400px;">
							<spring:message code="permission.description"/>
						</th>
						<th align="center" style="width:250px;">							
							<spring:message code="permission.module"/>							
						</th>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
							<th align="center" style="width:120px;">
								<spring:message code="permission.actions"/>
							</th>
						</security:authorize>
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					
					<c:forEach var="permission" items="${PERMISSIONS}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
						<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.permissionId, document.paginationForm.selectAllPermissions)">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
								<td class="checkBox">
									<input id="permissionId" name="permissionId" type="checkbox" onClick="cancelCheckEvent(this)" value="${permission.permissionId}"/></td>
								</td>
								<td align="center">
									<a href="#" onClick="getContentFromUrlDirect('Permission.htm?action=edit&id=${permission.permissionId}','MAIN_CONTENT');" title="<spring:message code="permission.edit"/>">
										${permission.name}
									</a>
								</td>
							</security:authorize>
							<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
								<td align="center" onClick="selectUnSelectRow(this, document.paginationForm.permissionId, document.paginationForm.selectAllPermissions)">								
									${permission.name}							
								</td>	
							</security:authorize>
							<c:choose>
								<c:when test="${fn:length(permission.sketch) ne 0 and permission.tokenizedSketch ne permission.truncatedTokenizedSketch}">
									<td align="center" onmouseover="Tip('${permission.tokenizedSketch}' , BGCOLOR, getCSSRule('.toolTipBody').style.backgroundColor, FONTWEIGHT, getCSSRule('.toolTipBody').style.fontWeight, FONTCOLOR, getCSSRule('.toolTipBody').style.color, BORDERCOLOR, getCSSRule('.toolTipBody').style.borderColor, FADEIN, 800, FADEOUT, 800);" onmouseout="UnTip();">
										${permission.truncatedTokenizedSketch}
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										${permission.truncatedTokenizedSketch}
									</td>
								</c:otherwise>
							</c:choose>											
							<td align="center">
								${permission.module.name}
							</td>
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
								<td align="center">						
									<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
											<tr>
												<td>	
													<a href="#" onClick="getContentFromUrlDirect('Permission.htm?action=edit&id=${permission.permissionId}','MAIN_CONTENT');" title="<spring:message code="permission.edit"/>"><img src="images/buttons/action_edit.png"/></a>
												</td>
											</tr>
										</table>			
								</td>
							</security:authorize>
						</tr>					
					</c:forEach>
				</table>
				<br/>
		
				<!-- THIS FIELD IS MANDATORY (NUMBER OF RESULTS/PAGE)-->
				<form:hidden path="resultsPerPage"/>
				<form:hidden path="currentPage"/>
				<form:hidden path="nbrOfPages"/>
				<form:hidden path="sortParam" />
				<form:hidden path="sortDirection" />	
				<form:hidden path="nbrOfResults"/>
				<form:hidden path="name"/>
				<form:hidden path="moduleId" />
			
				<c:import url="../Pagination.jsp">
				   <c:param name="currentPage" value="${searchPermissionBean.currentPage}"/>
				   <c:param name="nbrOfPages" value="${searchPermissionBean.nbrOfPages}"/>
				   <c:param name="firstPage" value="${searchPermissionBean.lowerLimit}"/>
				   <c:param name="lastPage" value="${searchPermissionBean.upperLimit}"/>
				   <c:param name="baseURL" value="SearchPermission.htm"/>
				   <c:param name="divResults" value="RESULTS"/>
				</c:import>	
				</form:form>
		</fieldset>
		<script>
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">	
				YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'SearchPermission.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="permission.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'permissionId',
				noSelectedItemsMessage: '<spring:message code="permission.delete.selectPermissionMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
				
				YAHOO.util.Event.addListener("selectAllPermissions", "click", selectAllCheckbox, {checkboxId: 'selectAllPermissions', ids: document.paginationForm.permissionId, table: document.getElementById('listingTable')}, true);
			</security:authorize>
		</script>
	
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="permission.no.results.message"/></div>
	</c:otherwise>
</c:choose>

<script>
	//----------------------------------------------------ORDER NAME----------------------------------------------------------------
	YAHOO.util.Event.addListener("sortLinkNameId", "click", order, {url : 'SearchPermission.htm?action=sort', newSortParam : 'name', 
	oldSortParam : '${searchPermissionBean.sortParam}', sortDirection : '${searchPermissionBean.sortDirection}', formId : 'PERMISSION_FORM', divId : 'RESULTS', 
	withContext : true}, true);
	//-------------------------------------------------------------------------------------------------------------------------	
	
</script>
