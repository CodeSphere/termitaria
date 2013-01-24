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
			<legend><spring:message code="person.title.listing" /></legend>
				<form:form id="paginationForm" name="paginationForm" commandName="searchPersonBean">		
																									
					<table class="list_results" style="width:890px" id="listingTable">
						<tr class="list_results_header">	
																																							
							<th style="width:250px;">
								<a class="sortLink" id="sortLastNameLinkId" href="#">
									<spring:message code="person.lastName"/>										
								</a>
							</th>
							
							<th style="width:250px;">	
								<a class="sortLink" id="sortFirstNameLinkId" href="#">							
									<spring:message code="person.firstName"/>	
								</a>						
							</th>
							
							<th style="width: 350px;">							
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">
									<c:choose>
										<c:when test="${searchPersonbean.project.projectId == 0}">
											<a class="sortLink" id="sortProjectNameLinkId" href="#">
												<spring:message code="person.project.slash.org"/>
											</a>
										</c:when>
										<c:otherwise>
											<spring:message code="person.project.slash.org"/>
										</c:otherwise>	
									</c:choose>
								</security:authorize>
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">
									<c:choose>
										<c:when test="${searchPersonbean.project.projectId == 0}">
											<a class="sortLink" id="sortProjectNameLinkId" href="#">
												<spring:message code="person.project"/>
											</a>
										</c:when>
										<c:otherwise>
											<spring:message code="person.project"/>
										</c:otherwise>	
									</c:choose>
								</security:authorize>
							</th>	
							
							<th style="width:250px;">								
								<spring:message code="person.email"/>							
							</th>
																																																						
							<th style="width: 80px;"> <spring:message code="actions"/></th>			
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
							
							<c:choose>
									<c:when test="${bean.project.name ne null}">										
									<spring:message code="project" var="prj"/>										
									<c:choose>
										<c:when test="${bean.project.status == 1}">											
											<spring:message code="project.listing.opened" var="Type"/>		
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${bean.project.status == 2}">													
													<spring:message code="project.closed" var="Type"/>		
												</c:when>
												<c:otherwise>
													<c:if test="${bean.project.status == 3}">														
														<spring:message code="project.abandoned" var="Type"/>																
													</c:if>																														
												</c:otherwise>	
											</c:choose>														
										</c:otherwise>	
									</c:choose>																																				
								</c:when>
								<c:otherwise>									
									<spring:message code="project.from.organization" var="prj"/>	
									<c:set var="Type" value=""/>	
								</c:otherwise>	
							</c:choose>	
							
							<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">									
								<td>
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">												
										<c:choose>
											<c:when test="${bean.memberId > 0}">
																																													
												<c:choose>
													<c:when test="${bean.project.status != 2 && bean.project.status != 3}">
														<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${bean.memberId}&hasTeamMemberDetail=${bean.hasTeamMemberDetail}&projectId=${bean.project.projectId}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
															${bean.lastName}				
														</a>				
													</c:when>
													<c:otherwise>
														<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${bean.memberId}&projectId=${bean.project.projectId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>">
															${bean.lastName}		
														</a>	
													</c:otherwise>		
												</c:choose>																																														
																									
											</c:when>
											<c:otherwise>
												<c:if test="${bean.personId > 0}">
														
													<c:choose>
														<c:when test="${bean.project.status != 2 && bean.project.status != 3}">
															<a href="#" onClick="getContentFromUrlDirect('PersonDetail.htm?ACTION=GET&personId=${bean.personId}&hasPersonDetail=${bean.hasPersonDetail}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="person.edit.detail"/>">
																${bean.lastName}	
															</a>																																									
														</c:when>
														<c:otherwise>
															<a href="#" onClick="displayInfoPanel('PersonView.htm?personId=${bean.personId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>">
																${bean.lastName}	
															</a>
														</c:otherwise>		
													</c:choose>																																																																													
												</c:if>	
											</c:otherwise>
										</c:choose>																																															
									</security:authorize>	
									
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">		
										<c:choose>
											<c:when test="${bean.hasManager eq true && bean.project.status != 2 && bean.project.status != 3}">													
												<c:if test="${bean.memberId > 0}">																								
													<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${bean.memberId}&hasTeamMemberDetail=${bean.hasTeamMemberDetail}&projectId=${bean.project.projectId}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
														${bean.lastName}			
													</a>																																																	
												</c:if>												
											</c:when>
											<c:otherwise>												
												<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${bean.memberId}&projectId=${bean.project.projectId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>">
													${bean.lastName}	
												</a>												
											</c:otherwise>
										</c:choose>
									</security:authorize>																																																						
								</td>		
								
								<td>			
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">												
										<c:choose>
											<c:when test="${bean.memberId > 0}">																						
												<c:choose>
													<c:when test="${bean.project.status != 2 && bean.project.status != 3}">
														<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${bean.memberId}&hasTeamMemberDetail=${bean.hasTeamMemberDetail}&projectId=${bean.project.projectId}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
															${bean.firstName}				
														</a>				
													</c:when>
													<c:otherwise>
														<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${bean.memberId}&projectId=${bean.project.projectId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>">
															${bean.firstName}		
														</a>	
													</c:otherwise>		
												</c:choose>																									
																									
											</c:when>
											<c:otherwise>
												<c:if test="${bean.personId > 0}">
																								
													<c:choose>
														<c:when test="${bean.project.status != 2 && bean.project.status != 3}">
															<a href="#" onClick="getContentFromUrlDirect('PersonDetail.htm?ACTION=GET&personId=${bean.personId}&hasPersonDetail=${bean.hasPersonDetail}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="person.edit.detail"/>">
																${bean.firstName}	
															</a>																																									
														</c:when>
														<c:otherwise>
															<a href="#" onClick="displayInfoPanel('PersonView.htm?personId=${bean.personId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>">
																${bean.firstName}	
															</a>
														</c:otherwise>		
													</c:choose>																								
																																																					
												</c:if>	
											</c:otherwise>
										</c:choose>																																															
									</security:authorize>	
									
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">		
										<c:if test="${bean.memberId > 0}">			
											<c:choose>
												<c:when test="${bean.hasManager eq true && bean.project.status != 2 && bean.project.status != 3}">																																													
													<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${bean.memberId}&hasTeamMemberDetail=${bean.hasTeamMemberDetail}&projectId=${bean.project.projectId}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
														${bean.firstName}	
													</a>																																																																						
												</c:when>
												<c:otherwise>																								
													<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${bean.memberId}&projectId=${bean.project.projectId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>">
														${bean.firstName}
													</a>																						
												</c:otherwise>
											</c:choose>
										</c:if>
									</security:authorize>																										
								</td>	
								
								<td title="${prj} ${Type}">					
									<c:choose>
										<c:when test="${bean.project.name ne null}">
											 ${bean.project.name} 		
										</c:when>
										<c:otherwise>
											<spring:message code="project.from.organization"/>	
										</c:otherwise>
									</c:choose>																								
								</td>		
								
								<td>													
									${bean.email}									
								</td>								
																																																																																																						
								<td>
									<table class="actionsTable" align="left" cellpadding="0" cellspacing="0">
										<tr>		
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">																								
													<c:choose>
														<c:when test="${bean.memberId > 0}">
															<c:if test="${bean.project.status != 2 && bean.project.status != 3}">			
																<td>												
																	<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${bean.memberId}&hasTeamMemberDetail=${bean.hasTeamMemberDetail}&projectId=${bean.project.projectId}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>"><img src="images/buttons/action_edit_teammember_details.png"/></a>																							
																</td>	
															</c:if>
															<td>
																<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${bean.memberId}&projectId=${bean.project.projectId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>"><img src="images/buttons/action_view.png"/></a>
															</td>	
														</c:when>
														<c:otherwise>
															<c:if test="${bean.personId > 0}">
																<c:if test="${bean.project.status != 2 && bean.project.status != 3}">			
																	<td>												
																		<a href="#" onClick="getContentFromUrlDirect('PersonDetail.htm?ACTION=GET&personId=${bean.personId}&hasPersonDetail=${bean.hasPersonDetail}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="person.edit.detail"/>"><img src="images/buttons/action_edit_person_detail.png"/></a>																							
																	</td>															
																</c:if>	
																<td>
																	<a href="#" onClick="displayInfoPanel('PersonView.htm?personId=${bean.personId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>"><img src="images/buttons/action_view.png"/></a>
																</td>													
															</c:if>	
														</c:otherwise>
													</c:choose>				
																																																					
											</security:authorize>
											
											<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">		
												<c:choose>
													<c:when test="${bean.hasManager eq true}">													
														<c:if test="${bean.memberId > 0}">
															<c:if test="${bean.project.status != 2 && bean.project.status != 3}">			
																<td>												
																	<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${bean.memberId}&hasTeamMemberDetail=${bean.hasTeamMemberDetail}&projectId=${bean.project.projectId}&BACK_URL=PersonSearch.htm', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>"><img src="images/buttons/action_edit_teammember_details.png"/></a>																							
																</td>	
															</c:if>
															<td>
																<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${bean.memberId}&projectId=${bean.project.projectId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>"><img src="images/buttons/action_view.png"/></a>
															</td>
														</c:if>												
													</c:when>
													<c:otherwise>
														<td>
															<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${bean.memberId}&projectId=${bean.project.projectId}', '${bean.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '440px')" title="<spring:message code="person.view"/>"><img src="images/buttons/action_view.png"/></a>
														</td>
													</c:otherwise>
												</c:choose>
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
		 <form:hidden path="lastName" />		 
		 <form:hidden path="firstName" />		 
		 <form:hidden path="projectId" />
		 <form:hidden path="withDeleted" />
		 <form:hidden path="organizationId" />				

		<c:import url="../../Pagination.jsp">
		   <c:param name="currentPage" value="${searchPersonBean.currentPage}"/>
		   <c:param name="nbrOfPages" value="${searchPersonBean.nbrOfPages}"/>
		   <c:param name="firstPage" value="${searchPersonBean.lowerLimit}"/>
		   <c:param name="lastPage" value="${searchPersonBean.upperLimit}"/>
		   <c:param name="baseURL" value="PersonSearch.htm"/>
		   <c:param name="divResults" value="RESULTS"/>
		</c:import>

		</form:form>	
		</fieldset>		
		<script>				
				
			//----------------------------------------------------ORDER BY LAST NAME----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortLastNameLinkId", "click", order, {url : 'PersonSearch.htm?action=sort', newSortParam : 'lastName', 
			oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);

			//----------------------------------------------------ORDER BY FIRST NAME----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortFirstNameLinkId", "click", order, {url : 'PersonSearch.htm?action=sort', newSortParam : 'firstName', 
			oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);

			//----------------------------------------------------ORDER BY PROJECT----------------------------------------------------------------
			YAHOO.util.Event.addListener("sortProjectNameLinkId", "click", order, {url : 'PersonSearch.htm?action=sort', newSortParam : 'projectName', 
			oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
			withContext : true}, true);
			
			

		</script>				
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="person.no.results.message"/></div>
	</c:otherwise>
</c:choose>
