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
<!-- ###################################  PERSON  ############################################### -->


<c:choose>
		<c:when test="${empty SEARCH_RESULTS}">
			<div class="noSearchResults"><spring:message code="person.search.no.results"/></div>
		</c:when>
		<c:otherwise>		
			<fieldset style="width: 890px;">
				<legend><spring:message code="person.title.listing" /></legend>	
					<form:form id="paginationForm" name="paginationForm" commandName="searchPersonBean">					
						<c:if test="${ALL_ACTIONS eq true}">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonDelete}">								
								<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
							</security:authorize>
						</c:if>
							
						<table class="list_results" id="listingTable">
						<tr class="list_results_header">					
							<c:if test="${ALL_ACTIONS eq true}">
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonDelete}">
									<th style="width: 5px;" class="checkBox">
										<input type="checkbox" id="selectAllPersons">
									</th>
								</security:authorize>
							</c:if>
							<th style="width: 300px;">
								<a href="#" class="sortLink" id="sortLinkLastNameId" >
									<spring:message code="person.lastName"/>
								</a>
							</th>
							<th style="width: 300px;">
								<a href="#" class="sortLink" id="sortLinkFirstNameId" >
									<spring:message code="person.firstName"/>
								</a>
							</th>	
							<th style="width: 300px;"><spring:message code="person.username"/></th>							
							<th style="width: 300px;"><spring:message code="person.email"/></th>
							<th style="width: 500px;"><spring:message code="person.jobs"/></th>
							<th style="width: 300px;"><spring:message code="actions"/></th>
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
							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.personId, document.paginationForm.selectAllPersons)">				
								<c:if test="${ALL_ACTIONS eq true}">
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonDelete}">
										<c:choose>
											<c:when test="${bean.modify eq true}">
												<c:choose>
													<c:when test="${bean.isAdmin == true && ADMIN_IT ne true}">
														<td>
															<input id="personId" type="hidden" type="checkbox"/>
														</td>		
													</c:when>
													<c:otherwise>
														<td class="checkBox">
															<input id="personId" name="personId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.personId}"/>			
														</td>
													</c:otherwise>
												</c:choose>	
											</c:when>
											<c:otherwise>
												<td></td>
											</c:otherwise>	
										</c:choose>
									</security:authorize>																	
								</c:if>
								<td>
									<a href="#" onClick="displayInfoPanel('PersonView.htm?personId=${bean.personId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}', '490px')" title="<spring:message code="person.view"/>">
										${bean.lastName}
									</a>
									<c:if test="${bean.isAdmin == true}">
									<div id="isAdmin" title="<spring:message code="person.isAdmin"/>"/>
								</c:if>
								</td>
								<td>
									<a href="#" onClick="displayInfoPanel('PersonView.htm?personId=${bean.personId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}', '490px')" title="<spring:message code="person.view"/>">
										${bean.firstName}
									</a>
								</td>		
								<td>${bean.username}</td>														
								<td>${bean.email}</td>
								<td>${bean.jobsFromDepts}</td>
								<td>										
										<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
											<tr>
											<c:choose>
											
												<c:when test="${ALL_ACTIONS eq true}">
																									
													<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PersonUpdate}">
														<td>
															<c:if test="${bean.modify eq true}">
																<a href="#" onClick="getContentFromUrlDirect('PersonForm.htm?ACTION=GET&personId=${bean.personId}','MAIN_CONTENT')" title="<spring:message code="edit"/>"><img src="images/buttons/action_edit.png"/></a>
															</c:if>
														</td>
													</security:authorize>
																										
													<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PersonChangeStatus}">
														<td>
															<c:choose>
																<c:when test="${bean.enabled eq 0}">
																	<a href="#" onClick="submitFormWithConfirmation('PersonSearch.htm?action=ACTIVATE&personId=${bean.personId}', 'paginationForm','LIST_RESULTS_CONTENT', '<spring:message code="person.enable.one.confirmationMessage"/>', '<spring:message code="confirm.activate"/>');" title="<spring:message code="organisation.enable"/>">
																		<img src="images/buttons/action_enable.png"/>
																	</a>
																</c:when>
																<c:otherwise>
																<a href="#" onClick="submitFormWithConfirmation('PersonSearch.htm?action=DEACTIVATE&personId=${bean.personId}', 'paginationForm','LIST_RESULTS_CONTENT', '<spring:message code="person.disable.one.confirmationMessage"/>', '<spring:message code="confirm.deactivate"/>');" title="<spring:message code="organisation.disable"/>">
																	<img src="images/buttons/action_disable.png"/>
																</a>									
																</c:otherwise>
															</c:choose>
														</td>
													</security:authorize>													
												</c:when>
												<c:otherwise>
													<td></td><td></td>
												</c:otherwise>
												
											</c:choose>
											<td>
												<a href="#" onClick="displayInfoPanel('PersonView.htm?personId=${bean.personId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}', '490px')" title="<spring:message code="person.view"/>"><img src="images/buttons/action_view.png"/></a>									
											</td>											
											<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">											
												<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ResetPassword}">
													<td>
														<c:if test="${bean.isAdmin == true}">
															<a href="#" id="resetPassword" title="<spring:message code="person.reset"/>"><img src="images/buttons/action_reset.png"/></a>
														</c:if>
													</td>
												</security:authorize>												
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
						<form:hidden path="firstName"/>
						<form:hidden path="lastName"/>	
						<form:hidden path="organisationId"/>
						<form:hidden path="departmentId"/>	
						<form:hidden path="sex"/>	
						<form:hidden path="branch"/>
					
					<c:import url="../../Pagination.jsp">
					   <c:param name="currentPage" value="${searchPersonBean.currentPage}"/>
					   <c:param name="nbrOfPages" value="${searchPersonBean.nbrOfPages}"/>
					   <c:param name="firstPage" value="${searchPersonBean.lowerLimit}"/>
					   <c:param name="lastPage" value="${searchPersonBean.upperLimit}"/>
					   <c:param name="baseURL" value="PersonSearch.htm"/>
					   <c:param name="divResults" value="LIST_RESULTS_CONTENT"/>
					</c:import>
					
					</form:form>	
			</fieldset>				
			
			<c:if test="${ALL_ACTIONS eq true}">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonDelete}">
					<script>
						YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'PersonSearch.htm?action=DELETE_ALL', formId: "paginationForm",
						container: "LIST_RESULTS_CONTENT", confirmationMessage: '<spring:message code="person.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'personId', 
						noSelectedItemsMessage: '<spring:message code="person.delete.selectPersonMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
							
						YAHOO.util.Event.addListener("selectAllPersons", "click", selectAllCheckbox, {checkboxId: 'selectAllPersons', ids: document.paginationForm.personId, table: document.getElementById('listingTable')}, true);						
					</script>
				</security:authorize>
			</c:if>			
	</c:otherwise>
</c:choose>

<script>
	
		YAHOO.util.Event.addListener('resetPassword', 'click', resetAdminPassword, 
		{url: 'PersonSearch.htm?action=RESET_PASSWORD&personId=${ADMIN_ID}', panelTitle: '<spring:message code="person.reset.password"/>',
		 submitButtonLabel: '<spring:message code="reset"/>', 
		 cancelButtonLabel: '<spring:message code="cancel"/>',
		 confirmationMessage: "<spring:message code="person.reset.password.confirmationMessage"/>", person : '${ADMIN_ID}'}, true);
	//------------------------------------------------------ORDER BY FIRST NAME---------------------------------------------
	
	YAHOO.util.Event.addListener("sortLinkLastNameId", "click", order, { url : 'PersonSearch.htm?action=sort', newSortParam : 'lastName', 
	oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'searchForm', 
	divId : 'LIST_RESULTS_CONTENT', withContext : true}, true);

	//------------------------------------------------------ORDER BY LAST NAME---------------------------------------------
	
	YAHOO.util.Event.addListener("sortLinkFirstNameId", "click", order, { url : 'PersonSearch.htm?action=sort', newSortParam : 'firstName', 
	oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'searchForm', 
	divId : 'LIST_RESULTS_CONTENT', withContext : true}, true);
	
	
</script>
	
