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


<!-- ###################################  DEPARTMENT  ############################################### -->

<%@ include file="../../Messages.jsp" %>

<c:choose>
		<c:when test="${empty SEARCH_RESULTS}">
			<div class="noSearchResults"><spring:message code="department.search.no.results"/></div>
			
		</c:when>
		<c:otherwise>
			
			<fieldset style="width: 800px;">
				<legend><spring:message code="department.title.listing" /></legend>
					<form:form id="paginationForm" name="paginationForm" commandName="searchDepartmentBean">
						<c:if test="${ALL_ACTIONS eq true}">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptDelete}">
								<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
							</security:authorize>							
						</c:if>
					
					<table class="list_results" style="width:800px" id="listingTable">
						<tr class="list_results_header">
							<c:if test="${ALL_ACTIONS eq true}">
								<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptDelete}">
									<th style="width: 5px;" class="checkBox">
										<input type="checkbox" id="selectAllDepartments">
									</th>
								</security:authorize>							
							</c:if>	
							<th style="width: 200px;"> 
								<a href="#" class="sortLink" id="sortLinkNameId" >
									 <spring:message code="department.name"/>			
								</a>			
							</th>
							<th style="width: 160px;">  
								<a href="#" class="sortLink" id="sortLinkManagerId" >
									<spring:message code="department.manager.name"/>	
								</a>			
							</th>
							<th style="width: 200px;"> <spring:message code="department.parent.department.name"/>	</th>
							<th style="width: 100px;"> <spring:message code="actions"/></th>
						</tr>
						<c:set var="i" value="0"/>
						<c:set var="cssClass" value=""/>
						
						<c:forEach var="bean" items="${SEARCH_RESULTS}">
							<c:set var="i" value="${i + 1}"/>
							<c:choose>
								<c:when test="${i % 2 == 0}">
									<c:set var="cssClass" value="even_row"/>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="odd_row"/>
								</c:otherwise>
							</c:choose>
							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.departmentId, document.paginationForm.selectAllDepartments)">
								<c:if test="${ALL_ACTIONS eq true}">
									<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptDelete}">
										<td class="checkBox">
											<input id="departmentId" name="departmentId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.departmentId}"/>
											<input id="hasPersonId" name="hasPerson" type="hidden" value="${bean.hasPerson}"/>
										</td>
									</security:authorize>
								</c:if>	
								<td>
									<a href="#" onClick="displayInfoPanel('DepartmentView.htm?departmentId=${bean.departmentId}', '${bean.panelHeaderName}', '${bean.name}', '400px')" title="<spring:message code="department.view"/>">
										${bean.name}
									</a>
								</td>
								<td>${bean.manager.firstName} ${bean.manager.lastName}</td>		
								<c:choose>
									<c:when test="${bean.parentDepartment.status == 2}">
										 <td>&nbsp;</td> 
									</c:when>
									<c:otherwise>
										 <td>${bean.parentDepartment.name}</td> 
									</c:otherwise>
								</c:choose>
								<td>
									<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
											<tr>													
												<c:if test="${ALL_ACTIONS eq true}">
													<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}">
														<td>
															<a href="#" onClick="getContentFromUrlDirect('DepartmentForm.htm?ACTION=GET&departmentId=${bean.departmentId}', 'MAIN_CONTENT')" title="<spring:message code="edit"/>"><img src="images/buttons/action_edit.png"/></a>
														</td>
													</security:authorize> 	
												</c:if>												
												<td>	
													<a href="#" onClick="displayInfoPanel('DepartmentView.htm?departmentId=${bean.departmentId}', '${bean.panelHeaderName}', '${bean.name}',  '400px')" title="<spring:message code="department.view"/>"><img src="images/buttons/action_view.png"/></a>
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
						<!-- HIDDEN FIELDS - SEARCH CRITERION -->
						<form:hidden path="name"/>
						<form:hidden path="managerFirstName"/>
						<form:hidden path="managerLastName"/>					
						<form:hidden path="parentDepartmentId"/>						 
						<form:hidden path="organisationId"/>						
						<form:hidden path="branch"/>
						
					<c:import url="../../Pagination.jsp">
					   <c:param name="currentPage" value="${searchDepartmentBean.currentPage}"/>
					   <c:param name="nbrOfPages" value="${searchDepartmentBean.nbrOfPages}"/>
					   <c:param name="firstPage" value="${searchDepartmentBean.lowerLimit}"/>
					   <c:param name="lastPage" value="${searchDepartmentBean.upperLimit}"/>
					   <c:param name="baseURL" value="DepartmentSearch.htm"/>
					   <c:param name="divResults" value="LIST_RESULTS_CONTENT"/>
					</c:import>
					
					</form:form>
			</fieldset>			
			<c:if test="${ALL_ACTIONS eq true}">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptDelete}">
					<script>					
						YAHOO.util.Event.addListener("deleteAll", "click", submitDepartmentDeleteWithConfirmation, {url: 'DepartmentSearch.htm?action=DELETE_ALL',
						formId: "paginationForm", container: "LIST_RESULTS_CONTENT", confirmationMessage1: '<spring:message code="department.delete.confirmationMessage"/>', confirmationMessage2: '<spring:message code="department.single.select.hasPersons.delete.confirmationMessage"/>',
						confirmationMessage3: '<spring:message code="department.multiple.select.hasPersons.delete.confirmationMessage"/>',
						selectedForDeletionElementsName: 'departmentId', noSelectedItemsMessage: '<spring:message code="department.delete.selectDepartmentMessage"/>', hasPersons: 'hasPerson', panelTitle: '<spring:message code="confirm.delete"/>'}, true);	
							
						YAHOO.util.Event.addListener("selectAllDepartments", "click", selectAllCheckbox, {checkboxId: 'selectAllDepartments', ids: document.paginationForm.departmentId, table: document.getElementById('listingTable')}, true);					
					</script>	
				</security:authorize>	
			</c:if>				
		</c:otherwise>
</c:choose>		

<script>
	//------------------------------------------------------ORDER BY NAME---------------------------------------------
	
	YAHOO.util.Event.addListener("sortLinkNameId", "click", order, { url : 'DepartmentSearch.htm?action=sort', newSortParam : 'name', 
	oldSortParam : '${searchDepartmentBean.sortParam}', sortDirection : '${searchDepartmentBean.sortDirection}', formId : 'searchForm', 
	divId : 'LIST_RESULTS_CONTENT', withContext : true}, true);

	//------------------------------------------------------ORDER BY MANAGER NAME---------------------------------------------
	
	YAHOO.util.Event.addListener("sortLinkManagerId", "click", order, { url : 'DepartmentSearch.htm?action=sort', newSortParam : 'manager.firstName', 
	oldSortParam : '${searchDepartmentBean.sortParam}', sortDirection : '${searchDepartmentBean.sortDirection}', formId : 'searchForm', 
	divId : 'LIST_RESULTS_CONTENT', withContext : true}, true);	
	
</script>
