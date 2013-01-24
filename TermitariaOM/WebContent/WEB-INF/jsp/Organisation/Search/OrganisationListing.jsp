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


<!-- ###################################  ORGANISATION  ############################################### -->
<c:choose>
		<c:when test="${empty SEARCH_RESULTS}">
			<div class="noSearchResults"><spring:message code="organisation.search.no.results"/></div>
		</c:when>
		<c:otherwise>
			<fieldset style="width: 890px;">
				<legend><spring:message code="organisation.title.listing" /></legend>
						<form:form id="paginationForm" name="paginationForm" commandName="searchOrganisationBean">
							
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
							<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
						</security:authorize>			
						<table class="list_results" id="listingTable">
							<tr class="list_results_header">
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
									<th style="width: 5px;" class="checkBox">
										<input type="checkbox" id="selectAllOrganisations"/>
									</th>
								</security:authorize>					
								<th style="width: 350px;">
									<a href="#" class="sortLink" id="sortLinkNameId" >
										<spring:message code="organisation.name"/>
									</a>
								</th>		
								<th style="width: 210px;">
									<a href="#" class="sortLink" id="sortLinkTypeId" >
										<spring:message code="organisation.select.type" />
									</a>
								</th>						
								<th style="width: 300px;"><spring:message code="organisation.email"/></th>								
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
									<th style="width: 100px;"><spring:message code="organisation.status"/></th>
								</security:authorize>								
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
									<th style="width: 120px;"> <spring:message code="actions"/></th>
								</security:authorize>
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
								<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.organisationId, document.paginationForm.selectAllOrganisations)">						
									<td class="checkBox">
										<input id="organisationId" name="organisationId" type="checkbox" onclick="cancelCheckEvent(this)" value="${bean.organisationId}"/>
									</td>
									<td align="center" >	
										<a href="#" onClick="getContentFromUrlDirectWithUpdatingOrgTreePanel('OrganisationOverview.htm?ACTION=GET&organisationId=${bean.organisationId}','MAIN_CONTENT');" title="<spring:message code="edit"/>" >${bean.name}</a>
									</td>	
									<td align="center" >							
										<c:choose>
											<c:when test="${bean.type == 1}">
												<spring:message code="organisation.type.company" />								
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${bean.type == 2}">
														<spring:message code="organisation.type.group.companies" />								
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${bean.type == 3}">
																<spring:message code="organisation.type.hq" />								
															</c:when>
															<c:otherwise>
																<spring:message code="organisation.type.branch" />
															</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</td>									
									<td>${bean.email}</td>									
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
										<td align="center">
											<c:choose>
												<c:when test="${bean.status == 0}">
													<spring:message code="organisation.disabled" />								
												</c:when>
												<c:otherwise>
													<spring:message code="organisation.enabled" />
												</c:otherwise>
											</c:choose>
										</td>		
									</security:authorize>
									<td>
										<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
											<tr>	
												<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
													<td>																					
														<c:choose>
															<c:when test="${bean.status == 0}">
															<a href="#" onClick="submitOrganisationFormWithConfirmation('OrganisationSearch.htm?action=updatestatus&organisationId=${bean.organisationId}', 'paginationForm','LIST_RESULTS_CONTENT', '<spring:message code="organisation.enable.one.confirmationMessage"/>' , '${bean.panelConfirmationName}', '<spring:message code="confirm.activate"/>');" title="<spring:message code="organisation.enable"/>">
																<img src="images/buttons/action_enable.png"/>
															</a>
															</c:when>
															<c:otherwise>
															<a href="#" onClick="submitOrganisationFormWithConfirmation('OrganisationSearch.htm?action=updatestatus&organisationId=${bean.organisationId}', 'paginationForm','LIST_RESULTS_CONTENT', '<spring:message code="organisation.disable.one.confirmationMessage"/>', '${bean.panelConfirmationName}', '<spring:message code="confirm.deactivate"/>');" title="<spring:message code="organisation.disable"/>">
																<img src="images/buttons/action_disable.png"/>
															</a>									
															</c:otherwise>
														</c:choose>																																							
													</td>
													<td>
														<a href="#" onClick="getContentFromUrlDirect('OrganisationForm.htm?ACTION=GET_FROM_SEARCH&organisationId=${bean.organisationId}', 'MAIN_CONTENT')" title="<spring:message code="edit"/>"><img src="images/buttons/action_edit.png"/></a>
													</td>
												</security:authorize>
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
							<form:hidden path="address"/>
							<form:hidden path="email"/>							
							<form:hidden path="type"/>				
							
						<c:import url="../../Pagination.jsp">
						   <c:param name="currentPage" value="${searchOrganisationBean.currentPage}"/>
						   <c:param name="nbrOfPages" value="${searchOrganisationBean.nbrOfPages}"/>
						   <c:param name="firstPage" value="${searchOrganisationBean.lowerLimit}"/>
						   <c:param name="lastPage" value="${searchOrganisationBean.upperLimit}"/>
						   <c:param name="baseURL" value="OrganisationSearch.htm"/>
						   <c:param name="divResults" value="LIST_RESULTS_CONTENT"/>
						</c:import>
					</form:form>
		</fieldset>	
		</c:otherwise>
</c:choose>	

<c:if test="${not empty SEARCH_RESULTS}">
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		<script>	
			YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'OrganisationSearch.htm?action=DELETE_ALL', formId: "paginationForm",
			container: "LIST_RESULTS_CONTENT",confirmationMessage: '<spring:message code="organisation.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'organisationId',
			noSelectedItemsMessage: '<spring:message code="organisation.delete.selectOrganizationMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
		
			YAHOO.util.Event.addListener("selectAllOrganisations", "click", selectAllCheckbox, {checkboxId: 'selectAllOrganisations', ids: document.paginationForm.organisationId, table: document.getElementById('listingTable')}, true);		
		</script>
	</security:authorize>
</c:if>



<script>
	//------------------------------------------------------ORDER BY NAME---------------------------------------------
	
	YAHOO.util.Event.addListener("sortLinkNameId", "click", order, { url : 'OrganisationSearch.htm?action=sort', newSortParam : 'name', 
	oldSortParam : '${searchOrganisationBean.sortParam}', sortDirection : '${searchOrganisationBean.sortDirection}', formId : 'searchForm', 
	divId : 'LIST_RESULTS_CONTENT', withContext : true}, true);

	// ORDER BY TYPE
	
	YAHOO.util.Event.addListener("sortLinkTypeId", "click", order, { url : 'OrganisationSearch.htm?action=sort', newSortParam : 'type', 
	oldSortParam : '${searchOrganisationBean.sortParam}', sortDirection : '${searchOrganisationBean.sortDirection}', formId : 'searchForm', 
	divId : 'LIST_RESULTS_CONTENT', withContext : true}, true);
	
	
</script>
