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

<c:set var="showDelete" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeDeleteAll}">
	<c:forEach var="exchangeBean" items="${SEARCH_RESULTS}">
		<c:if test="${exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">		
			<c:set var="showDelete" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeDeleteAll}">
	<c:forEach var="exchangeBean" items="${SEARCH_RESULTS}">
		<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true && exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">
			<c:set var="showDelete" value="true"/>
		</c:if>
	</c:forEach>	
</security:authorize>

<c:choose>
	<c:when test="${!(empty SEARCH_RESULTS)}">
		<fieldset style="width: 1000px;">
			<legend><spring:message code="exchange.title.listing" /></legend>
			<form:form id="paginationForm" name="paginationForm" commandName="searchExchangeBean">
					
					<c:if test="${showDelete eq true}">
						<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>"/>
					</c:if>
					
					<table class="list_results" style="width:1000px" id="listingTable">
						<tr class="list_results_header">
							
							<c:if test="${showDelete eq true}">							
								<th style="width: 5px;" class="checkBox">
									<input type="checkbox" id="selectAllExchanges">
								</th>
							</c:if>
							
							<th style="width: 300px">
								<a class="sortLink" id="projectNameSortLink" href="#">
									<spring:message code="exchange.project"/>
								</a>
							</th>
							<th style="width: 200px">
								<spring:message code="exchange.first.currency"/>
							</th>
							<th style="width: 200px">
								<spring:message code="exchange.second.currency"/>
							</th>
							<th style="width: 200px">
								<spring:message code="exchange.rate"/>
							</th>
							<th style="width: 100px">
								<spring:message code="actions"/>
							</th>
						</tr>
						<c:set var="i" value="0"/>						
						<c:set var="cssClass" value=""/>
						
						<c:forEach var="exchangeBean" items="${SEARCH_RESULTS}">
							<c:set var="i" value="${i + 1}"/>
							<c:choose>
								<c:when test="${i % 2 == 0}">
									<c:set var="cssClass" value="even_row"/>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="odd_row"/>
								</c:otherwise>
							</c:choose>
							
							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.exchangeId, document.paginationForm.selectAllExchanges)">
														
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeDeleteAll}">	
								<c:choose>
									<c:when test="${exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">																						
										<td class="checkBox">
											<input id="exchangeId" name="exchangeId" type="checkbox" onClick="cancelCheckEvent(this)" value="${exchangeBean.exchangeId}"/>
										</td>
									</c:when>
									<c:otherwise>
										<c:if test="${showDelete eq true}">
											<td>
												<input id="exchangeId" type="hidden" type="checkbox"/>										
											</td>
										</c:if>
									</c:otherwise>
								</c:choose>
							</security:authorize>
							
							<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeDeleteAll}">
								<c:choose>
									<c:when test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true && exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">
										<td class="checkBox">
											<input id="exchangeId" name="exchangeId" type="checkbox" onClick="cancelCheckEvent(this)" value="${exchangeBean.exchangeId}"/>
										</td>
									</c:when>
									<c:otherwise>
										<c:if test="${showDelete eq true}">
											<td>
												<input id="exchangeId" type="hidden" type="checkbox"/>										
											</td>
										</c:if>
									</c:otherwise>
								</c:choose>
							</security:authorize>	
							
							<c:choose>
								<c:when test="${(exchangeBean.projectName ne null) && (exchangeBean.projectName != '')}">											
									<spring:message code="project" var="prj"/>										
									<c:choose>
										<c:when test="${exchangeBean.projectDetail.status == 1}">											
											<spring:message code="project.listing.opened" var="Type"/>		
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${exchangeBean.projectDetail.status == 2}">													
													<spring:message code="project.closed" var="Type"/>		
												</c:when>
												<c:otherwise>
													<c:if test="${exchangeBean.projectDetail.status == 3}">														
														<spring:message code="project.abandoned" var="Type"/>																
													</c:if>																														
												</c:otherwise>	
											</c:choose>														
										</c:otherwise>	
									</c:choose>																																				
								</c:when>
								<c:otherwise>									
									<spring:message code="exchange.for.organization" var="prj"/>	
									<c:set var="Type" value=""/>	
								</c:otherwise>	
							</c:choose>	
																					
								<td title="${prj} ${Type}">		
									<c:choose>
										<c:when test="${(exchangeBean.projectName ne null) && (exchangeBean.projectName != '')}">
											${exchangeBean.projectName}
										</c:when>
										<c:otherwise>
											<spring:message code="exchange.for.organization"/>
										</c:otherwise>
									</c:choose>
								</td>
								
								<td>
									${exchangeBean.firstCurrency.name} (${exchangeBean.firstCurrency.initials})
								</td>
								<td>
									${exchangeBean.secondCurrency.name} (${exchangeBean.secondCurrency.initials})
								</td>
								<td>
									${exchangeBean.formattedRate}
								</td>
								<td>
									<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
										<tr>
											<td>
												<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeUpdateAll}">
													<c:if test="${exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">	
														<a href="#" onClick="getContentFromUrlDirect('ExchangeForm.htm?ACTION=GET&exchangeId=${exchangeBean.exchangeId}&BACK_URL=ExchangeSearch.htm', 'MAIN_CONTENT')" title="<spring:message code='exchange.edit'/>"><img src="images/buttons/action_edit.png"/></a>
													</c:if>
												</security:authorize>
												<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeUpdateAll}">
													<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true && exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">
														<a href="#" onClick="getContentFromUrlDirect('ExchangeForm.htm?ACTION=GET&exchangeId=${exchangeBean.exchangeId}&BACK_URL=ExchangeSearch.htm', 'MAIN_CONTENT')" title="<spring:message code='exchange.edit'/>"><img src="images/buttons/action_edit.png"/></a>
													</c:if>
												</security:authorize>	
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
					
					<form:hidden path="projectId"/>
					<form:hidden path="firstCurrencyId"/>
					<form:hidden path="secondCurrencyId"/>
					<form:hidden path="organizationId"/>
					
					<c:import url="../../Pagination.jsp">
					<c:param name="currentPage" value="${searchExchangeBean.currentPage}"/>
					<c:param name="nbrOfPages" value="${searchExchangeBean.nbrOfPages}"/>
					<c:param name="firstPage" value="${searchExchangeBean.lowerLimit}"/>
					<c:param name="lastPage" value="${searchExchangeBean.upperLimit}"/>
					<c:param name="baseURL" value="ExchangeSearch.htm"/>
					<c:param name="divResults" value="RESULTS"/>
				</c:import>
			</form:form>
		</fieldset>
		<script>
			//----------------------------------------------------ORDER----------------------------------------------------------------
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
				YAHOO.util.Event.addListener("projectNameSortLink", "click", order, {url : 'ExchangeSearch.htm?action=sort', newSortParam : 'projectName', 
					oldSortParam : '${searchExchangeBean.sortParam}', sortDirection : '${searchExchangeBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
					withContext : true}, true);
			</security:authorize >	
			<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
				<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
				YAHOO.util.Event.addListener("projectNameSortLink", "click", order, {url : 'ExchangeSearch.htm?action=sort', newSortParam : 'projectName', 
					oldSortParam : '${searchExchangeBean.sortParam}', sortDirection : '${searchExchangeBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
					withContext : true}, true);
				</c:if>
			</security:authorize >	

			<c:if test="${showDelete eq true}">
				//------------------------------------------------------DELETE ALL -----------------------------------------------
				YAHOO.util.Event.addListener("deleteAll", "click", submitWithConfirmation, {url: 'ExchangeSearch.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="exchange.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'exchangeId',
				noSelectedItemsMessage: '<spring:message code="exchange.delete.selectExchangeMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);

				//------------------------------------------------------SELECT ALL-------------------------------------------------
				YAHOO.util.Event.addListener("selectAllExchanges", "click", selectAllCheckbox, {checkboxId: 'selectAllExchanges', ids: document.paginationForm.exchangeId, 
				table: document.getElementById('listingTable')}, true);				
			</c:if>
			
		</script>
		</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="exchange.no.results.message"/></div>
	</c:otherwise>
</c:choose>
