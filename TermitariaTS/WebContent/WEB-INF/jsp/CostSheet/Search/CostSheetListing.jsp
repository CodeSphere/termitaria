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
<%@ include file="../../Messages.jsp" %>

<c:set var="displayCheckAll" value="false"/>
	
<c:forEach var="costSheetBean" items="${SEARCH_RESULTS}">
	<c:if test="${costSheetBean.projectDetails.status != 2 && costSheetBean.projectDetails.status != 3}">	
		<c:set var="displayCheckAll" value="true"/>
	</c:if>
</c:forEach>

<c:set var="showBillable" value="false"/>
<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.TS_CostSheetAddAll}, ${PERMISSION_CONSTANT.TS_CostSheetUpdateAll}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${(bean.projectName ne null) && (bean.projectName != '')}">		
			<c:set var="showBillable" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>

<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_CostSheetAddAll}, ${PERMISSION_CONSTANT.TS_CostSheetUpdateAll}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${USER_ID eq bean.projectManagerId}">	
			<c:set var="showBillable" value="true"/>
		</c:if> 
	</c:forEach>
</security:authorize>

<c:choose>
	<c:when test="${!(empty SEARCH_RESULTS)}">
		<fieldset style="width: 1000px;">
			<legend><spring:message code="costsheet.title.listing" /></legend>
			<form:form id="paginationForm" name="paginationForm" commandName="searchCostSheetBean">					
					<c:if test="${displayCheckAll eq true}">
						<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" /> 
					</c:if>
					
					<table class="list_results" style="width:1000px" id="listingTable">
						<tr class="list_results_header">
							
							<c:if test="${displayCheckAll eq true}">
								<th style="width: 5px;" class="checkBox">
									<input type="checkbox" id="selectAllCostSheets">
								</th>
							</c:if>
							
							<th style="width: 50px">
								<a class="sortLink" id="costSheetIdLink" href="#">
									<spring:message code="costsheet.id"/>
								</a>
							</th>
														
							<th style="width: 200px">
								<spring:message code="costsheet.reporter"/>
							</th>
							<th style="width: 200px">
								<spring:message code="costsheet.activity"/>
							</th>
								
							<th style="width: 150px">
								<spring:message code="costsheet.project"/>
							</th>		
												
							<c:if test="${showBillable eq true}">
								<th style="width: 100px">
									<spring:message code="costsheet.billable"/>
								</th>
							</c:if>							
							
							<th style="width: 130px">
								<a class="sortLink" id="dateSortLink" href="#">
									<spring:message code="costsheet.date"/>
								</a>
							</th>
							<th style="width: 150px">
								<spring:message code="costsheet.value"/>
							</th>
							<th style="width: 80px">
								<spring:message code="actions"/>
							</th>
						</tr>
						<c:set var="i" value="0"/>						
						<c:set var="cssClass" value=""/>
						
						<c:forEach var="costSheetBean" items="${SEARCH_RESULTS}">
							<c:set var="i" value="${i + 1}"/>
							<c:choose>
								<c:when test="${i % 2 == 0}">
									<c:set var="cssClass" value="even_row"/>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="odd_row"/>
								</c:otherwise>
							</c:choose>
							
							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.costSheetId, document.paginationForm.selectAllCostSheets)">
							
							<c:choose>
								<c:when test="${costSheetBean.projectDetails.status != 2 && costSheetBean.projectDetails.status != 3}">									
									<td class="checkBox">
										<input id="costSheetId" name="costSheetId" type="checkbox" onClick="cancelCheckEvent(this)" value="${costSheetBean.costSheetId}"/>
									</td>	
								</c:when>
								<c:otherwise>
									<c:if test="${displayCheckAll eq true}">
										<td>
											<input id="costSheetId" type="hidden" type="checkbox"/>										
										</td>
									</c:if>
								</c:otherwise>
							</c:choose>	
							
							<c:choose>
								<c:when test="${(costSheetBean.projectName ne null) && (costSheetBean.projectName != '')}">																			
									<spring:message code="project" var="prj"/>										
									<c:choose>
										<c:when test="${costSheetBean.projectDetails.status == 1}">											
											<spring:message code="project.listing.opened" var="Type"/>		
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${costSheetBean.projectDetails.status == 2}">													
													<spring:message code="project.closed" var="Type"/>		
												</c:when>
												<c:otherwise>
													<c:if test="${costSheetBean.projectDetails.status == 3}">														
														<spring:message code="project.abandoned" var="Type"/>																
													</c:if>																														
												</c:otherwise>	
											</c:choose>														
										</c:otherwise>	
									</c:choose>																																				
								</c:when>
								<c:otherwise>									
									<spring:message code="costsheet.for.organization" var="prj"/>	
									<c:set var="Type" value=""/>	
								</c:otherwise>	
							</c:choose>	
							
								<td>
									${costSheetBean.costSheetId}
								</td>												
								<td>
									${costSheetBean.costSheetReporterName}
								</td>
								<td>
									${costSheetBean.activityName}
								</td>
								<td title="${prj} ${Type}">		
									<c:choose>
										<c:when test="${(costSheetBean.projectName ne null) && (costSheetBean.projectName != '')}">
											${costSheetBean.projectName}
										</c:when>
										<c:otherwise>
											<spring:message code="costsheet.for.organization"/>
										</c:otherwise>
									</c:choose>
								</td>
								
								<c:if test="${showBillable eq true}">
									<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.TS_CostSheetAddAll}, ${PERMISSION_CONSTANT.TS_CostSheetUpdateAll}">
										<td align="center" >
											<c:if test="${(costSheetBean.projectName ne null) && (costSheetBean.projectName != '')}">		
												<c:if test="${(costSheetBean.billable != '') && !(empty costSheetBean.billable )}">											
													<spring:message code="costsheet.billable.${costSheetBean.billable}"/>											
												</c:if>
											</c:if>
										</td>
									</security:authorize>
									
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_CostSheetAddAll}, ${PERMISSION_CONSTANT.TS_CostSheetUpdateAll}">
										<c:choose>									
											<c:when test="${USER_ID eq costSheetBean.projectManagerId}">
												<td align="center" >					
													<c:if test="${(costSheetBean.billable != '') && !(empty costSheetBean.billable )}">										
														<spring:message code="costsheet.billable.${costSheetBean.billable}"/>										
													</c:if>
												</td>
											</c:when>
											<c:otherwise>
												<td align="center" > </td>
											</c:otherwise>
										</c:choose>
									</security:authorize>
								</c:if>
																																					
								<td>
									<fmt:formatDate value="${costSheetBean.date}" type="both" dateStyle="long" timeStyle="short"/> 
								</td>
								<td>
									${costSheetBean.costPrice} ${costSheetBean.costPriceCurrency.initials}
								</td>
								<td>
									<table class="actionsTable" align="left" cellpadding="0" cellspacing="0">
										<tr>
											<c:if test="${costSheetBean.projectDetails.status != 2 && costSheetBean.projectDetails.status != 3}">	
												<td>
													<a href="#" onClick="getContentFromUrlDirect('CostSheetForm.htm?ACTION=GET&costSheetId=${costSheetBean.costSheetId}&BACK_URL=CostSheetSearch.htm', 'MAIN_CONTENT')" title="<spring:message code='costsheet.edit'/>"><img src="images/buttons/action_edit.png"/></a>
												</td>
											</c:if>
											<td>
												<a href="#" onClick="displayInfoPanel('CostSheetView.htm?costSheetId=${costSheetBean.costSheetId}', '${costSheetBean.panelHeaderName}', '${costSheetBean.panelHeaderName}', '490px')" title="<spring:message code="costsheet.view"/>"><img src="images/buttons/action_view.png"/></a>									
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
					
					<form:hidden path="teamMemberId"/>
					<form:hidden path="projectId"/>
					<form:hidden path="activityName"/>
					<spring:bind path="startDate">
						<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd/MM/yyyy HH:mm'/>"/>
					</spring:bind>
					<spring:bind path="endDate">
						<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd/MM/yyyy HH:mm'/>"/>
					</spring:bind>
					<form:hidden path="billable"/>
					<form:hidden path="firstName"/>
					<form:hidden path="lastName"/>	
					<form:hidden path="organizationId"/>
					<form:hidden path="costPriceMin"/>
					<form:hidden path="costPriceMax"/>
					<form:hidden path="costPriceCurrencyId"/>										
					
					<c:import url="../../Pagination.jsp">
					<c:param name="currentPage" value="${searchCostSheetBean.currentPage}"/>
					<c:param name="nbrOfPages" value="${searchCostSheetBean.nbrOfPages}"/>
					<c:param name="firstPage" value="${searchCostSheetBean.lowerLimit}"/>
					<c:param name="lastPage" value="${searchCostSheetBean.upperLimit}"/>
					<c:param name="baseURL" value="CostSheetSearch.htm"/>
					<c:param name="divResults" value="RESULTS"/>
				</c:import>
			</form:form>
		</fieldset>
		<script>
			//----------------------------------------------------ORDER----------------------------------------------------------------
			YAHOO.util.Event.addListener("costSheetIdLink", "click", order, {url : 'CostSheetSearch.htm?action=sort', newSortParam : 'costSheetId', 
				oldSortParam : '${searchCostSheetBean.sortParam}', sortDirection : '${searchCostSheetBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
				withContext : true}, true);
			
			YAHOO.util.Event.addListener("dateSortLink", "click", order, {url : 'CostSheetSearch.htm?action=sort', newSortParam : 'date', 
				oldSortParam : '${searchCostSheetBean.sortParam}', sortDirection : '${searchCostSheetBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
				withContext : true}, true);

			<c:if test="${displayCheckAll eq true}">
				//------------------------------------------------------DELETE ALL -----------------------------------------------
				YAHOO.util.Event.addListener("deleteAll", "click", submitWithConfirmation, {url: 'CostSheetSearch.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="costsheet.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'costSheetId',
				noSelectedItemsMessage: '<spring:message code="costsheet.delete.selectCostsheetMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
	
				//------------------------------------------------------SELECT ALL-------------------------------------------------
				YAHOO.util.Event.addListener("selectAllCostSheets", "click", selectAllCheckbox, {checkboxId: 'selectAllCostSheets', ids: document.paginationForm.costSheetId, 
				table: document.getElementById('listingTable')}, true);
			</c:if>

		</script>
		</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="costsheet.no.results.message"/></div>
	</c:otherwise>
</c:choose>
