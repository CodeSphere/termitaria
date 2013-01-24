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

<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedDelete}">
	<c:set var="displayCheckAll" value="true"/>
</security:authorize>

<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedDelete}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${USER_ID eq bean.managerId}">
			<c:set var="displayCheckAll" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>

<c:choose>
	<c:when test="${!(empty SEARCH_RESULTS)}">	
		<fieldset style="width: 890px;">
			<legend><spring:message code="project.title.listing" /></legend>
				<form:form id="paginationForm" name="paginationForm" commandName="searchProjectBean">		
					
					<c:if test="${displayCheckAll eq true}">
						<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
					</c:if>
																	
					<table class="list_results" style="width:890px" id="listingTable">
						<tr class="list_results_header">	
							
						
							<c:if test="${displayCheckAll eq true}">
								<th style="width: 5px;" class="checkBox">
									<input type="checkbox" id="selectAllProjects">
								</th>
							</c:if>
																						
							<th style="width:250px;">
								<a class="sortLink" id="sortLinkNameId" href="#">
									<spring:message code="project.name"/>
								</a>
							</th>
							
							<th style="width:250px;">
								<a class="sortLink" id="sortLinkClientId" href="#">
									<spring:message code="project.client"/>
								</a>
							</th>																			
											
							<th style="width: 350px;"> 
								<spring:message code="project.manager.name"/>	
							</th>	
							
							
							<th style="width: 80px;"> 
								<a class="sortLink" id="sortLinkStatusId" href="#">
									<spring:message code="project.status"/>	
								</a>
							</th>
																	
							<th style="width: 150px;"> <spring:message code="actions"/></th>			
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
								
							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.projectId, document.paginationForm.selectAllProjects)">	
								
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedDelete}">												
									<td class="checkBox">								
										<input id="projectId" name="projectId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.projectId}"/>														
									</td>
								</security:authorize>
							
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedDelete}">
									<c:choose>
										<c:when test="${USER_ID eq bean.managerId}">
											<td class="checkBox">								
												<input id="projectId" name="projectId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.projectId}"/>												
											</td>
										</c:when>
										<c:otherwise>											
											<c:if test="${displayCheckAll eq true}">
												<td>
													<input id="projectId" type="hidden" type="checkbox"/>										
												</td>
											</c:if>																						
										</c:otherwise>
									</c:choose>
								</security:authorize>	
								
								<td>	
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedSearch}">
										<a href="#" onClick="getContentFromUrlDirect('Project.htm?ACTION=GET&projectId=${bean.projectId}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">																								
											${bean.name}		
										</a>
									</security:authorize>
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedSearch}">
										<c:choose>
											<c:when test="${USER_ID eq bean.managerId}">
												<a href="#" onClick="getContentFromUrlDirect('Project.htm?ACTION=GET&projectId=${bean.projectId}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">																								
													${bean.name}		
												</a>
											</c:when>
											<c:otherwise>
												<a href="#" onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '${bean.panelHeaderName}', '${bean.name}',  '400px')" title="<spring:message code="project.view"/>">
													${bean.name}
												</a>
											</c:otherwise>
										</c:choose>
									</security:authorize>						
								</td>	
																							
								<td>	
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientUpdate}">		
										<a href="#" onClick="getContentFromUrlDirect('ClientForm.htm?ACTION=GET&clientId=${bean.clientId}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">						
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
										</a>
									</security:authorize>
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ClientUpdate}">
										<a href="#" onClick="displayInfoPanel('ClientView.htm?clientId=${bean.client.clientId}', '${bean.client.panelHeaderName}', '${bean.client.panelHeaderName}', '490px')" title="<spring:message code="client.view"/>">
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
										</a>
									</security:authorize>									
								</td>
																							
								<td>
									${bean.manager.firstName} ${bean.manager.lastName}											
								</td>									
								
								<td>
									<c:choose>
										<c:when test="${bean.status == 1}">
											<spring:message code="project.status.activated" />					
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
											<td>
												<a href="#" onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '${bean.panelHeaderName}', '${bean.name}',  '400px')" title="<spring:message code="project.view"/>">
													<img src="images/buttons/action_view.png"/>
												</a>
											</td>											
																																																																																																			
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedSearch}">
												<td>																																									
													<a href="#" onClick="getContentFromUrlDirect('Project.htm?ACTION=GET&projectId=${bean.projectId}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT');" title="<spring:message code="edit"/>">
														<img src="images/buttons/action_edit.png"/>
													</a>																																																									
												</td>
												<c:choose>
												<c:when test="${bean.status == 1}">                                                																						
													<td>
														<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=FINISH_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS' ,'<spring:message code="project.finish.confirmationMessage"/>', '${bean.panelHeaderName}', '<spring:message code="confirm.finish"/>');" title="<spring:message code="project.finish" />">
															<img src="images/buttons/action_finish_project.png"/>
														</a> 
													</td>
													<td>	
														<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=ABORT_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS' ,'<spring:message code="project.abort.confirmationMessage"/>', '${bean.panelHeaderName}', '<spring:message code="confirm.abort"/>');" title="<spring:message code="project.abort" />">
															<img src="images/buttons/action_abandon_project.png"/>
														</a>
													</td> 														
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${bean.status == 2}">
																<td>
																	<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS', '<spring:message code="project.open.one.confirmationMessage"/>' , '${bean.panelHeaderName}', '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.open" />">							
																		<img src="images/buttons/action_open_project.png"/>
																	</a>
																</td>		
																<td>
																</td>														
															</c:when>
															<c:otherwise>
																<td>
																	<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS', '<spring:message code="project.open.one.confirmationMessage"/>' , '${bean.panelHeaderName}', '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.activate" />">							
																		<img src="images/buttons/action_open_project.png"/>
																	</a>
																</td>
																<td>
																</td>
															</c:otherwise>
														</c:choose>
														
													</c:otherwise>
												</c:choose>
											</security:authorize>
												
											<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedSearch}">
												<c:if test="${USER_ID eq bean.managerId}">
													<td>																																									
														<a href="#" onClick="getContentFromUrlDirect('Project.htm?ACTION=GET&projectId=${bean.projectId}&BACK_URL=ProjectSearch.htm', 'MAIN_CONTENT');" title="<spring:message code="edit"/>">
															<img src="images/buttons/action_edit.png"/>
														</a>																																																									
													</td>
													<c:choose>
													<c:when test="${bean.status == 1}">
														<td>
															<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=FINISH_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS' ,'<spring:message code="project.finish.confirmationMessage"/>', '${bean.panelHeaderName}', '<spring:message code="confirm.finish"/>');" title="<spring:message code="project.finish" />">
																<img src="images/buttons/action_finish_project.png"/>
															</a> 
														</td>
														<td>	
															<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=ABORT_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS' ,'<spring:message code="project.abort.confirmationMessage"/>', '${bean.panelHeaderName}', '<spring:message code="confirm.abort"/>');" title="<spring:message code="project.abort" />">
																<img src="images/buttons/action_abandon_project.png"/>
															</a>
														</td> 														
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${bean.status == 2}">
																<td>
																	<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS', '<spring:message code="project.open.one.confirmationMessage"/>' , '${bean.panelHeaderName}', '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.activate" />">							
																		<img src="images/buttons/action_open_project.png"/>
																	</a>
																</td>																	
															</c:when>
															<c:otherwise>
																<td>
																	<a href="#" onClick="submitProjectFormWithConfirmation('ProjectSearch.htm?action=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}', 'paginationForm','RESULTS', '<spring:message code="project.open.one.confirmationMessage"/>' , '${bean.panelHeaderName}', '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.activate" />">							
																		<img src="images/buttons/action_open_project.png"/>
																	</a>
																</td>
															</c:otherwise>
														</c:choose>
														
													</c:otherwise>
												</c:choose>
												</c:if>
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
		<form:hidden path="status"/>												
		<form:hidden path="clientId"/>
		<form:hidden path="organizationId"/>
		<form:hidden path="managerId"/>
		<form:hidden path="name"/>
						
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
			YAHOO.util.Event.addListener("sortLinkNameId", "click", order, {url : 'ProjectSearch.htm?action=sort', newSortParam : 'name', 
			oldSortParam : '${searchProjectBean.sortParam}', sortDirection : '${searchProjectBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);
	
			//----------------------------------------------------ORDER BY CLIENT----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortLinkClientId", "click", order, {url : 'ProjectSearch.htm?action=sort', newSortParam : 'clientName', 
			oldSortParam : '${searchProjectBean.sortParam}', sortDirection : '${searchProjectBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);
	
			//----------------------------------------------------ORDER BY STATUS----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortLinkStatusId", "click", order, {url : 'ProjectSearch.htm?action=sort', newSortParam : 'status', 
			oldSortParam : '${searchProjectBean.sortParam}', sortDirection : '${searchProjectBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);
				
			//-------------------------------------------------------------------------------------------------------------------------
			
			<c:if test="${displayCheckAll eq true}">
				//----------------------------------------------------DELETE ALL----------------------------------------------------------------
				YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'ProjectSearch.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="project.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'projectId', 
				noSelectedItemsMessage: '<spring:message code="project.delete.selectProjectMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
				
				YAHOO.util.Event.addListener("selectAllProjects", "click", selectAllCheckbox, {checkboxId: 'selectAllProjects', ids: document.paginationForm.projectId, table: document.getElementById('listingTable')}, true);
				//-------------------------------------------------------------------------------------------------------------------------
			</c:if>
									
		</script>				
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="project.no.results.message"/></div>
	</c:otherwise>
</c:choose>
