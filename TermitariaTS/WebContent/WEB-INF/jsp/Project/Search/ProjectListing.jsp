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

<c:choose>
	<c:when test="${!(empty SEARCH_RESULTS)}">	
		<fieldset style="width: 890px;">
			<legend><spring:message code="project.title.listing" /></legend>
				<form:form id="paginationForm" name="paginationForm" commandName="searchProjectBean">		
																									
					<table class="list_results" style="width:890px" id="listingTable">
						<tr class="list_results_header">	
																																							
							<th style="width:250px;">
								<a class="sortLink" id="sortNameLinkId" href="#">
									<spring:message code="project.name"/>
								</a>
							</th>
							
							<th style="width:250px;">
								<a class="sortLink" id="sortClientLinkId" href="#">
									<spring:message code="project.client"/>
								</a>
							</th>
							
											
							<th style="width: 350px;"> 
								<spring:message code="project.manager.name"/>	
							</th>	
							
							
							<th style="width: 100px;"> 
								<a class="sortLink" id="sortStatusLinkId" href="#">
									<spring:message code="project.status"/>	
								</a>
							</th>
																																			
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
							<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">									
								<td>
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">																																																														
										<c:choose>
											<c:when test="${bean.status != 2 && bean.status != 3}">
												<a href="#" onClick="getContentFromUrlDirect('ProjectDetails.htm?ACTION=GET&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="project.edit.detail"/>">
													${bean.name}			
												</a>				
											</c:when>
											<c:otherwise>
												<a href="#" onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '${bean.panelHeaderName}', '${bean.name}',  '450px')" title="<spring:message code="project.view"/>">
													${bean.name}
												</a>
											</c:otherwise>		
										</c:choose>																																																					
									</security:authorize>	
									
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">																																	
										<c:choose>
											<c:when test="${USER_ID eq bean.managerId && bean.status != 2 && bean.status != 3}">		
												<a href="#" onClick="getContentFromUrlDirect('ProjectDetails.htm?ACTION=GET&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="project.edit.detail"/>">
													${bean.name}			
												</a>	
											</c:when>
											<c:otherwise>
												<a href="#" onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '${bean.panelHeaderName}', '${bean.name}',  '450px')" title="<spring:message code="project.view"/>">
													${bean.name}
												</a>
											</c:otherwise>
										</c:choose>
									</security:authorize>
																																		
								</td>		
								
								<td>	
									<c:choose>
										<c:when test="${bean.client.type == 2}">											
											${bean.client.p_firstName} ${bean.client.p_lastName}											
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${bean.client.type == 1}">											
													${bean.client.c_name}											
												</c:when>
												<c:otherwise>																						
													<spring:message code="project.from.organization"/>	
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>								
								</td>
																							
								<td>
									${bean.manager.firstName} ${bean.manager.lastName}											
								</td>											
								
								<td>
									<c:choose>
										<c:when test="${bean.status == 1}">
											<spring:message code="project.status.opened" />					
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${bean.status == 2}">
													<spring:message code="project.status.finished" />	
												</c:when>
												<c:otherwise>
													<spring:message code="project.status.aborted" />	
												</c:otherwise>		
											</c:choose>	
										</c:otherwise>
									</c:choose>												
								</td>
																																																			
								<td>
									<table class="actionsTable" align="left" cellpadding="0" cellspacing="0">
										<tr>													
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">			
												<c:if test="${bean.status != 2 && bean.status != 3}">																														
													<td>												
														<a href="#" onClick="getContentFromUrlDirect('ProjectDetails.htm?ACTION=GET&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="project.edit.detail"/>"><img src="images/buttons/action_edit_project_detail.png"/></a>																							
													</td>	
												</c:if>
											</security:authorize>
																																																					
											<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">																																	
												<c:if test="${USER_ID eq bean.managerId && bean.status != 2 && bean.status != 3}">															
													<td>												
														<a href="#" onClick="getContentFromUrlDirect('ProjectDetails.htm?ACTION=GET&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="project.edit.detail"/>"><img src="images/buttons/action_edit_project_detail.png"/></a>																							
													</td>	
												</c:if>
											</security:authorize>
																																
											<td>
												<a href="#" onClick="getContentFromUrlDirect('PersonSearch.htm?ACTION=LIST_TEAM_MEMBERS_FROM_PROJECT&projectId=${bean.projectId}&projectName=${bean.name}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="project.team"/>"><img src="images/buttons/action_edit_project_team.png"/></a>
											</td>																					
																						
											<td>
												<a href="#" onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '${bean.panelHeaderName}', '${bean.name}',  '450px')" title="<spring:message code="project.view"/>"><img src="images/buttons/action_view.png"/></a>
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
		 <form:hidden path="name" />	
		 <form:hidden path="status" />
		 <form:hidden path="clientId" />	
		 <form:hidden path="managerId" />	
		 <form:hidden path="organizationId"/>		

		<c:import url="../../Pagination.jsp">
		   <c:param name="currentPage" value="${searchProjectBean.currentPage}"/>
		   <c:param name="nbrOfPages" value="${searchProjectBean.nbrOfPages}"/>
		   <c:param name="firstPage" value="${searchProjectBean.lowerLimit}"/>
		   <c:param name="lastPage" value="${searchProjectBean.upperLimit}"/>
		   <c:param name="baseURL" value="ProjectSearch.htm"/>
		   <c:param name="divResults" value="RESULTS"/>
		</c:import>

		</form:form>	
		</fieldset>		
		<script>				
				
		//----------------------------------------------------ORDER BY NAME----------------------------------------------------------------
		YAHOO.util.Event.addListener("sortNameLinkId", "click", order, {url : 'ProjectSearch.htm?action=sort', newSortParam : 'name', 
		oldSortParam : '${searchProjectBean.sortParam}', sortDirection : '${searchProjectBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
		withContext : true}, true);

		//----------------------------------------------------ORDER BY CLIENT----------------------------------------------------------------
		YAHOO.util.Event.addListener("sortClientLinkId", "click", order, {url : 'ProjectSearch.htm?action=sort', newSortParam : 'clientName', 
		oldSortParam : '${searchProjectBean.sortParam}', sortDirection : '${searchProjectBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
		withContext : true}, true);

		//----------------------------------------------------ORDER BY STATUS----------------------------------------------------------------
		YAHOO.util.Event.addListener("sortStatusLinkId", "click", order, {url : 'ProjectSearch.htm?action=sort', newSortParam : 'status', 
		oldSortParam : '${searchProjectBean.sortParam}', sortDirection : '${searchProjectBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
		withContext : true}, true);
		
		</script>				
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="project.no.results.message"/></div>
	</c:otherwise>
</c:choose>
