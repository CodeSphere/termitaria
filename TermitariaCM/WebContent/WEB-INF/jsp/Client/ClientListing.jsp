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
	<c:when test="${!(empty SEARCH_RESULTS)}">
		<fieldset style="width: 1000px;">
			<legend><spring:message code="client.title.listing" /></legend>
			<form:form id="paginationForm" name="paginationForm" commandName="searchClientBean">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientDelete}">
					<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
				</security:authorize>
				<table class="list_results" style="width:1000px" id="listingTable">
					<tr class="list_results_header">					
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientDelete}">
							<th style="width: 5px;" class="checkBox">
								<input type="checkbox" id="selectAllClients">
							</th>
						</security:authorize>
						<th style="width: 300px">
							<a class="sortLink" id="nameSortLinkId" href="#">
								<spring:message code="client.name"/>
							</a>
						</th>
						<th style="width: 150px">
							<a class="sortLink" href="#" id="typeSortLinkId">
								<spring:message code="client.type"/>
							</a>
						</th>
						<th style="width: 300px">
							<spring:message code="client.email"/>
						</th>
						<th style="width: 100px">
							<spring:message code="actions"/>
						</th>
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					<c:forEach var="clientBean" items="${SEARCH_RESULTS}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
						<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}" onClick="selectUnSelectRow(this, document.paginationForm.clientId, document.paginationForm.selectAllClients)">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientDelete}">
								<td class="checkBox">
									<input id="clientId" name="clientId" type="checkbox" onClick="cancelCheckEvent(this)" value="${clientBean.clientId}"/>
								</td>
							</security:authorize>
							<td>
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientUpdate}">
									<a href="#" onClick="getContentFromUrlDirect('ClientForm.htm?ACTION=GET&clientId=${clientBean.clientId}&BACK_URL=ClientSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">
										<c:choose>
											<c:when test="${CLIENT_TYPE_FIRM == clientBean.type}">
												${clientBean.c_name}
											</c:when>
											<c:otherwise>
												${clientBean.p_firstName}&nbsp;${clientBean.p_lastName}
											</c:otherwise>
										</c:choose>
									</a>
								</security:authorize>
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ClientUpdate}">
									<a href="#" onClick="displayInfoPanel('ClientView.htm?clientId=${clientBean.clientId}', '${clientBean.panelHeaderName}', '${clientBean.panelHeaderName}', '490px')" title="<spring:message code="client.view"/>">
										<c:choose>
											<c:when test="${CLIENT_TYPE_FIRM == clientBean.type}">
												${clientBean.c_name}
											</c:when>
											<c:otherwise>
												${clientBean.p_firstName}&nbsp;${clientBean.p_lastName}
											</c:otherwise>
										</c:choose>
									</a>
								</security:authorize>
							</td>								
							<td>
								<c:choose>
									<c:when test="${CLIENT_TYPE_FIRM == clientBean.type}">
										<spring:message code="client.type.firm"/>
									</c:when>
									<c:otherwise>
										<spring:message code="client.type.person"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								${clientBean.email}
							</td>
							<td>
								<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
									<tr>
										<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientUpdate}">
											<td>
												<a href="#" onClick="getContentFromUrlDirect('ClientForm.htm?ACTION=GET&clientId=${clientBean.clientId}&BACK_URL=ClientSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>"><img src="images/buttons/action_edit.png"/></a>
											</td>
										</security:authorize>
										<td>
											<a href="#" onClick="displayInfoPanel('ClientView.htm?clientId=${clientBean.clientId}', '${clientBean.panelHeaderName}', '${clientBean.panelHeaderName}', '490px')" title="<spring:message code="client.view"/>"><img src="images/buttons/action_view.png"/></a>									
										</td>
										<td>
											<c:choose>
												<c:when test="${CLIENT_TYPE_FIRM == clientBean.type}">
													<a href="#" onClick="getContentFromUrlDirect('ProjectSearch.htm?ACTION=LIST_PROJECTS_FROM_CLIENT&clientId=${clientBean.clientId}&clientName=${clientBean.c_name}', 'MAIN_CONTENT')" title="<spring:message code="client.project.listing"/>"><img src="images/buttons/action_list_projects.png"/></a>													
												</c:when>
												<c:otherwise>
													<a href="#" onClick="getContentFromUrlDirect('ProjectSearch.htm?ACTION=LIST_PROJECTS_FROM_CLIENT&clientId=${clientBean.clientId}&clientName=${clientBean.p_firstName} ${clientBean.p_lastName}', 'MAIN_CONTENT')" title="<spring:message code="client.project.listing"/>"><img src="images/buttons/action_list_projects.png"/></a>													
												</c:otherwise>
											</c:choose>											
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:forEach>
				</table>
				<br/>
				
				<!-- Show pagination area only if there is more than 1 page -->
	
				<!-- THIS FIELD IS MANDATORY (NUMBER OF RESULTS/PAGE)-->
				<form:hidden path="resultsPerPage"/>
				<form:hidden path="currentPage"/>
				<form:hidden path="nbrOfPages"/>
				<form:hidden path="nbrOfResults"/>
				<form:hidden path="sortParam" />
				<form:hidden path="sortDirection" />	
				
				<form:hidden path="address"/>
				<form:hidden path="email"/>							
				<form:hidden path="type"/>	
				<form:hidden path="status"/>
				<form:hidden path="organizationId"/>
				<form:hidden path="c_name"/>
				<form:hidden path="p_firstName"/>
				<form:hidden path="p_lastName"/>
				
				<c:import url="../Pagination.jsp">
				   <c:param name="currentPage" value="${searchClientBean.currentPage}"/>
				   <c:param name="nbrOfPages" value="${searchClientBean.nbrOfPages}"/>
				   <c:param name="firstPage" value="${searchClientBean.lowerLimit}"/>
				   <c:param name="lastPage" value="${searchClientBean.upperLimit}"/>
				   <c:param name="baseURL" value="ClientSearch.htm"/>
				   <c:param name="divResults" value="RESULTS"/>
				</c:import>
			</form:form>
		</fieldset>
		<script>
			//----------------------------------------------------ORDER----------------------------------------------------------------
			YAHOO.util.Event.addListener("nameSortLinkId", "click", order, {url : 'ClientSearch.htm?action=sort', newSortParam : 'name', 
				oldSortParam : '${searchClientBean.sortParam}', sortDirection : '${searchClientBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
				withContext : true}, true);

			YAHOO.util.Event.addListener("typeSortLinkId", "click", order, {url : 'ClientSearch.htm?action=sort', newSortParam : 'type', 
				oldSortParam : '${searchClientBean.sortParam}', sortDirection : '${searchClientBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
				withContext : true}, true);					


			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientDelete}">
				//------------------------------------------------------DELETE ALL -----------------------------------------------
				YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'ClientSearch.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="client.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'clientId',
				noSelectedItemsMessage: '<spring:message code="client.delete.selectClientMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
	
				//------------------------------------------------------SELECT ALL-------------------------------------------------
				YAHOO.util.Event.addListener("selectAllClients", "click", selectAllCheckbox, {checkboxId: 'selectAllClients', ids: document.paginationForm.clientId, 
				table: document.getElementById('listingTable')}, true);
			</security:authorize>
		</script>	

	</c:when>
		<c:otherwise>
		<div class="noSearchResults"><spring:message code="client.no.results.message"/></div>
	</c:otherwise>
</c:choose>
							
