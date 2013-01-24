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
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${bean.projectDetails.status != 2 && bean.projectDetails.status != 3}">		
			<c:set var="displayCheckAll" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true && bean.projectDetails.status != 2 && bean.projectDetails.status != 3 && bean.projectDetails.projectDetailId > 0 && USER_ID eq bean.projectManagerId}">
			<c:set var="displayCheckAll" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>

<c:set var="showBillable" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${bean.projectId != -1}">		
			<c:set var="showBillable" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${USER_ID eq bean.projectManagerId}">
			<c:set var="showBillable" value="true"/>
		</c:if> 
	</c:forEach>
</security:authorize>

<c:choose>
	<c:when test="${!(empty SEARCH_RESULTS)}">	
		<fieldset style="width: 890px;">
			<legend><spring:message code="activity.title.listing" /></legend>
				<form:form id="paginationForm" name="paginationForm" commandName="searchActivityBean">		
						
					<c:if test="${displayCheckAll eq true}">		
						<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
					</c:if>
															
					<table class="list_results" style="width:890px" id="listingTable">
						<tr class="list_results_header">	
																		
							<c:if test="${displayCheckAll eq true}">
								<th style="width: 5px;" class="checkBox">
									<input type="checkbox" id="selectAllActivities">
								</th>
							</c:if>
																																				
							<th style="width:250px;">
								<a class="sortLink" id="sortLinkNameId" href="#">
									<spring:message code="activity.name"/>
								</a>
							</th>
							
							<th style="width:250px;">	
								<a class="sortLink" id="sortLinkProjectId" href="#">			
									<spring:message code="activity.project"/>
								</a>								
							</th>							
							
							<c:if test="${showBillable eq true}">
								<th style="width:60px;">	
									<a class="sortLink" id="sortLinkBillableId" href="#">							
										<spring:message code="activity.billable"/>		
									</a>						
								</th>	
							</c:if>														
																																																																							
							<th style="width:50px;"> <spring:message code="actions"/></th>			
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

							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.activityId, document.paginationForm.selectAllActivities)">
							
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">	
								<c:choose>
									<c:when test="${bean.projectDetails.status != 2 && bean.projectDetails.status != 3}">																							
										<td class="checkBox">								
											<input id="activityId" name="activityId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.activityId}"/>														
										</td>
									</c:when>
									<c:otherwise>
										<c:if test="${displayCheckAll eq true}">
											<td>
												<input id="activityId" type="hidden" type="checkbox"/>										
											</td>
										</c:if>																		
									</c:otherwise>
								</c:choose>
							</security:authorize>
							
							<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">
								<c:choose>
									<c:when test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true && bean.projectDetails.status != 2 && bean.projectDetails.status != 3 && bean.projectDetails.projectDetailId > 0 && USER_ID eq bean.projectManagerId}">																				
										<td class="checkBox">								
											<input id="activityId" name="activityId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.activityId}"/>														
										</td>
									</c:when>
									<c:otherwise>										
										<c:if test="${displayCheckAll eq true}">
											<td>
												<input id="activityId" type="hidden" type="checkbox"/>										
											</td>
										</c:if>										
									</c:otherwise>
								</c:choose>
							</security:authorize>	
							
							<c:choose>
								<c:when test="${bean.projectId == -1}">																																											
									<spring:message code="project.from.organization" var="prj"/>		
									<c:set var="Type" value=""/>							
								</c:when>
								<c:otherwise>	
									<spring:message code="project" var="prj"/>										
									<c:choose>
										<c:when test="${bean.projectDetails.status == 1}">											
											<spring:message code="project.listing.opened" var="Type"/>	
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${bean.projectDetails.status == 2}">													
													<spring:message code="project.closed" var="Type"/>		
												</c:when>
												<c:otherwise>
													<c:if test="${bean.projectDetails.status == 3}">														
														<spring:message code="project.abandoned" var="Type"/>																
													</c:if>																														
												</c:otherwise>	
											</c:choose>														
										</c:otherwise>	
									</c:choose>												
								</c:otherwise>	
							</c:choose>	
																										
								<td align="center">		
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">																																	
										
										<c:choose>
											<c:when test="${bean.projectDetails.status != 2 && bean.projectDetails.status != 3}">
												<a href="#" onClick="getContentFromUrlDirect('Activity.htm?ACTION=GET&activityId=${bean.activityId}&projectId=${bean.projectId}&BACK_URL=ActivitySearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">
													${bean.name}
												</a>					
											</c:when>
											<c:otherwise>
												<a href="#" onClick="displayInfoPanel('ActivityView.htm?activityId=${bean.activityId}', '${bean.panelHeaderName}', '${bean.name}',  '450px')" title="<spring:message code="activity.view"/>">
													${bean.name}
												</a>
											</c:otherwise>		
										</c:choose>																																	
									</security:authorize>
									
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
										<c:choose>																														
											<c:when test="${USER_ID eq bean.projectManagerId && bean.projectDetails.status != 2 && bean.projectDetails.status != 3}">			
												<a href="#" onClick="getContentFromUrlDirect('Activity.htm?ACTION=GET&activityId=${bean.activityId}&projectId=${bean.projectId}&BACK_URL=ActivitySearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">
													${bean.name}
												</a>		
											</c:when>
											<c:otherwise>
												<a href="#" onClick="displayInfoPanel('ActivityView.htm?activityId=${bean.activityId}', '${bean.panelHeaderName}', '${bean.name}',  '450px')" title="<spring:message code="activity.view"/>">
													${bean.name}
												</a>
											</c:otherwise>
										</c:choose>											
									</security:authorize>
																																						
								</td>	
																							
								<td align="center" title="${prj} ${Type}">												
									${bean.projectName}																														
								</td>	
										
								<c:if test="${showBillable eq true}">								
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">									
										<td align="center" >
											<c:if test="${bean.projectId != -1}">									
												<c:if test="${(bean.billable != '') && !(empty bean.billable )}">												
													<spring:message code="billable.${bean.billable}" />															
												</c:if>				
											</c:if>
										</td>							
									</security:authorize>
								
								
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}"> 									
										
										<c:choose>
											<c:when test="${USER_ID eq bean.projectManagerId}">	
												<td align="center" >																			
													<c:if test="${(bean.billable != '') && !(empty bean.billable )}">	
														<spring:message code="billable.${bean.billable}" />			
													</c:if>		
												</td>	
											</c:when>
											<c:otherwise>
												<td align="center" ></td>
											</c:otherwise>
										</c:choose>																				
																		
									</security:authorize>
								</c:if>
																																																																																																																																																
								<td>
									<table class="actionsTable" align="left" cellpadding="0" cellspacing="0">
										<tr>																																														
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">																																	
												<c:if test="${bean.projectDetails.status != 2 && bean.projectDetails.status != 3}">																		
													<td>
														<a href="#" onClick="getContentFromUrlDirect('Activity.htm?ACTION=GET&activityId=${bean.activityId}&projectId=${bean.projectId}&BACK_URL=ActivitySearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">
															<img src="images/buttons/action_edit.png"/>
														</a>
													</td>		
												</c:if>											
											</security:authorize>
											
											<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">																																													
												<c:if test="${USER_ID eq bean.projectManagerId && bean.projectDetails.status != 2 && bean.projectDetails.status != 3}">															
													<td>
														<a href="#" onClick="getContentFromUrlDirect('Activity.htm?ACTION=GET&activityId=${bean.activityId}&projectId=${bean.projectId}&BACK_URL=ActivitySearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">
															<img src="images/buttons/action_edit.png"/>
														</a>
													</td>																									
												</c:if>																											
											</security:authorize>
																						
											<td>
												<a href="#" onClick="displayInfoPanel('ActivityView.htm?activityId=${bean.activityId}', '${bean.panelHeaderName}', '${bean.name}',  '450px')" title="<spring:message code="activity.view"/>"><img src="images/buttons/action_view.png"/></a>
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
		<form:hidden path="billable"/>									
		<form:hidden path="projectId"/>

		<c:import url="../../Pagination.jsp">
		   <c:param name="currentPage" value="${searchActivityBean.currentPage}"/>
		   <c:param name="nbrOfPages" value="${searchActivityBean.nbrOfPages}"/>
		   <c:param name="firstPage" value="${searchActivityBean.lowerLimit}"/>
		   <c:param name="lastPage" value="${searchActivityBean.upperLimit}"/>
		   <c:param name="baseURL" value="ActivitySearch.htm"/>
		   <c:param name="divResults" value="RESULTS"/>
		</c:import>

		</form:form>	
		</fieldset>	
		
		<script>

			//----------------------------------------------------ORDER BY NAME----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortLinkNameId", "click", order, {url : 'ActivitySearch.htm?action=sort', newSortParam : 'name', 
			oldSortParam : '${searchActivityBean.sortParam}', sortDirection : '${searchActivityBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);
		
			//----------------------------------------------------ORDER BY BILLABLE----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortLinkBillableId", "click", order, {url : 'ActivitySearch.htm?action=sort', newSortParam : 'billable', 
			oldSortParam : '${searchActivityBean.sortParam}', sortDirection : '${searchActivityBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);
		
			//----------------------------------------------------ORDER BY PROJECT NAME----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortLinkProjectId", "click", order, {url : 'ActivitySearch.htm?action=sort', newSortParam : 'projectName', 
			oldSortParam : '${searchActivityBean.sortParam}', sortDirection : '${searchActivityBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);

			//-------------------------------------------------------------------------------------------------------------------------
			
			<c:if test="${displayCheckAll eq true}">
				//----------------------------------------------------DELETE ALL----------------------------------------------------------------
				YAHOO.util.Event.addListener("deleteAll", "click", submitWithConfirmation, {url: 'ActivitySearch.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="activity.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'activityId', 
				noSelectedItemsMessage: '<spring:message code="activity.delete.selectActivityMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
				
				YAHOO.util.Event.addListener("selectAllActivities", "click", selectAllCheckbox, {checkboxId: 'selectAllActivities', ids: document.paginationForm.activityId, table: document.getElementById('listingTable')}, true);
				//-------------------------------------------------------------------------------------------------------------------------
			</c:if>							
				
		</script>
								
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="activity.no.results.message"/></div>
	</c:otherwise>
</c:choose>
