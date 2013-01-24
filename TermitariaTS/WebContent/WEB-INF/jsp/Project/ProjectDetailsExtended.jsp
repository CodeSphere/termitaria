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


<fieldset >
		
	<table cellpadding="1" colspan="1" border="0">				

		<!--------------- PROJECT TEAM NAME -------------------->	
		<tr>
			<td>
				<div class="labelTitle"><spring:message code="project.team"/></div>
				<br/>
		
		<table border="0">	
	
		<c:choose>
			<c:when test="${TEAM_MEMBERS eq null}">
				<div class="noFindResultsSmall"><spring:message code="project.no.project.team.message"/></div>
			</c:when>
			<c:otherwise>								            	
				<c:if test="${!(empty TEAM_MEMBERS)}">
					<c:forEach var="person" items="${TEAM_MEMBERS}">	
						<c:if test="${person.status != 2}">	
							<tr>
								<td width = "300px">${person.firstName} ${person.lastName} (${person.email})</td>
								<td>
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">																								
										<c:choose>
											<c:when test="${person.memberId > 0}">
												<c:if test="${PROJECT.status != 2 && PROJECT.status != 3}">																							
													<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${person.memberId}&hasTeamMemberDetail=${person.hasTeamMemberDetail}&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>"><img src="images/buttons/action_edit_teammember_details.png"/></a>																							
												</c:if>
													<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${person.memberId}&projectId=${PROJECT_ID}', '${person.firstName} ${person.lastName}', '${person.firstName} ${person.lastName}',  '440px')" title="<spring:message code="person.view"/>"><img src="images/buttons/action_view.png"/></a>	
											</c:when>
											<c:otherwise>
												<c:if test="${bean.personId > 0}">
													<c:if test="${PROJECT.status != 2 && PROJECT.status != 3}">												
														<a href="#" onClick="getContentFromUrlDirect('PersonDetail.htm?ACTION=GET&personId=${person.personId}&hasPersonDetail=${person.hasPersonDetail}&projectId=${PROJECT.projectId}&BACK_URL=ProjectDetails.htm', 'MAIN_CONTENT')" title="<spring:message code="person.edit.detail"/>"><img src="images/buttons/action_edit_person_detail.png"/></a>																																					
													</c:if>	
														<a href="#" onClick="displayInfoPanel('PersonView.htm?personId=${person.personId}', '${person.firstName} ${person.lastName}', '${person.firstName} ${person.lastName}',  '440px')" title="<spring:message code="team.member.information"/>"><img src="images/buttons/action_view.png"/></a>												
												</c:if>	
											</c:otherwise>
										</c:choose>																																										
									</security:authorize>
									</td>
								</tr>
							</c:if>									
						</c:forEach>	
					</c:if>								
				</c:otherwise>
			</c:choose>			
		</table>
		</td>
		</tr>
		<!--------------- PROJECT TEAM NAME END ------------------>
		
		

		<!----------------- ACTIVITY --------------------->
		<tr>
			<td>
				<br/>
				
		<div class="labelTitle">
			<spring:message code="activity.title.listing" />
			<span class="addActionStyle" >(<img src="images/buttons/action_add_small_button.png"/>  
				<a href="#"  style="color:black;" onClick="getContentFromUrlDirect('Activity.htm?ACTION=ADD&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')">
				<spring:message code="activity.add.section.title"/></a>)
			</span>	
		</div>
		<br/>	
				
		<table  border="0">
			<c:choose>
				<c:when test="${ACTIVITIES eq null or empty(ACTIVITIES)}">
					<div class="noFindResultsSmall"><spring:message code="activity.no.activities"/></div>
				</c:when>
				<c:otherwise>								            	
					<c:if test="${!(empty ACTIVITIES)}">
						<c:forEach var="activity" items="${ACTIVITIES}">	
							<tr>
								<td width="150px" >${activity.name}</td>
								<td>
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">																																	
										<c:if test="${PROJECT.status != 2 && PROJECT.status != 3}">																		
											<a href="#" onClick="getContentFromUrlDirect('Activity.htm?ACTION=GET&activityId=${activity.activityId}&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">
												<img src="images/buttons/action_edit.png"/>
											</a>			
										</c:if>											
									</security:authorize>
									
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">																																													
										<c:if test="${USER_ID eq PROJECT.projectManagerId && PROJECT.status != 2 && PROJECT.status != 3}">															
											<a href="#" onClick="getContentFromUrlDirect('Activity.htm?ACTION=GET&activityId=${activity.activityId}&projectId=${PROJECT.projectId}&BACK_URL=ActivitySearch.htm', 'MAIN_CONTENT')" title="<spring:message code="edit"/>">
												<img src="images/buttons/action_edit.png"/>
											</a>																									
										</c:if>																											
									</security:authorize>
								
									<a href="#" onClick="displayInfoPanel('ActivityView.htm?activityId=${activity.activityId}', '${activity.name}', '${activity.name}',  '450px')" title="<spring:message code="activity.view"/>"><img src="images/buttons/action_view.png"/></a>
								</td>	
							</tr>								
						</c:forEach>	
					</c:if>								
				</c:otherwise>
			</c:choose>			
		</table>
		</td>
	</tr>
	<!----------------- ACTIVITY END -------------------->
		
		
		
	<!-------------------  COSTS ------------------------>
	<tr>
		<td>
			<br/>
			
	<div class="labelTitle">
		<spring:message code="costsheet" />
		<span class="addActionStyle">(<img src="images/buttons/action_add_small_button.png"/>  
			<a href="#"  style="color: black;" onClick="getContentFromUrlDirect('CostSheetForm.htm?ACTION=ADD&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')">
			<spring:message code="costsheet.add.section.title"/></a>, 
			<img src="images/buttons/action_search_small_button.png"/>  
			<a href="#"  style="color: black;" onClick="getContentFromUrlDirect('CostSheetSearch.htm?&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')">
			<spring:message code="costsheet.search"/></a>)
		</span>
	</div>
	<br/>
	
	<table  width="600px" border="0">
		<c:choose>
			<c:when test="${empty COSTS}">
				<tr>
					<td colspan="3">
						<div class="noFindResultsSmall"><spring:message code="costsheet.no.costsheets"/></div>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach var="costSheetBean" items="${COSTS}">
					<tr>								
						<td width="400px">
							${costSheetBean.costSheetReporterName}, ${costSheetBean.activityName}, <span style="font-size: 0.8em">
							<fmt:formatDate value="${costSheetBean.date}" type="both" dateStyle="short" timeStyle="short"/></span>,  
							<strong>${costSheetBean.costPrice} ${costSheetBean.costPriceCurrency.initials}</strong>
						</td>
						<td width="50px">
							<table class="actionsTable" align="left" cellpadding="0" cellspacing="0">
								<tr>
									<c:if test="${costSheetBean.projectDetails.status != 2 && costSheetBean.projectDetails.status != 3}">	
										<td>
											<a href="#" onClick="getContentFromUrlDirect('CostSheetForm.htm?ACTION=GET&costSheetId=${costSheetBean.costSheetId}&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')" title="<spring:message code='costsheet.edit'/>"><img src="images/buttons/action_edit.png"/></a>
										</td>
									</c:if>
									<td>
										<a href="#" onClick="displayInfoPanel('CostSheetView.htm?costSheetId=${costSheetBean.costSheetId}', '<spring:message code="costsheet.information"/>', '<spring:message code="costsheet.information"/>', '490px')" title="<spring:message code="costsheet.view"/>"><img src="images/buttons/action_view.png"/></a>									
									</td>
								</tr>
							</table>
						</td>
					</tr>	
				</c:forEach>
			</c:otherwise>
		</c:choose>	
	</table>
	</td>
	</tr>
	<!-------------------  COSTS END ---------------------->
	
	
												
	<!-------------------  EXCHANGES -------------------->
	<tr>
		<td>
			<br/>
			
	<div class="labelTitle">
		<spring:message code="exchange" />
		<span class="addActionStyle">(<img src="images/buttons/action_add_small_button.png"/>  
			<a href="#"  style="color: black;" onClick="getContentFromUrlDirect('ExchangeForm.htm?ACTION=ADD&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')">
			<spring:message code="exchange.add.section.title"/></a>)
		</span>
	</div>
	<br/>
	
	<table  border="0">
		<c:choose>
			<c:when test="${empty EXCHANGES}">
				<tr>
					<td colspan="3">
						<div class="noFindResultsSmall"><spring:message code="exchange.no.exchanges"/></div>
					</td>
				</tr>
			</c:when>
			<c:otherwise>	
				<c:forEach var="exchangeBean" items="${EXCHANGES}">
					
					<tr>
						<td width="100px">
							${exchangeBean.firstCurrency.name} -> ${exchangeBean.secondCurrency.name} 
						</td>
						<td width="80px" align="center">
							${exchangeBean.rate}
						</td>
						<td width="15px" align="right">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeUpdateAll}">
								<c:if test="${exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">	
									<a href="#" onClick="getContentFromUrlDirect('ExchangeForm.htm?ACTION=GET&exchangeId=${exchangeBean.exchangeId}&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')" title="<spring:message code='exchange.edit'/>"><img src="images/buttons/action_edit.png"/></a>
								</c:if>
							</security:authorize>
							<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeUpdateAll}">
								<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true && exchangeBean.projectDetail.status != 2 && exchangeBean.projectDetail.status != 3}">
									<a href="#" onClick="getContentFromUrlDirect('ExchangeForm.htm?ACTION=GET&exchangeId=${exchangeBean.exchangeId}&projectId=${PROJECT_ID}&BACK_URL=ProjectDetails.htm?ACTION=GET', 'MAIN_CONTENT')" title="<spring:message code='exchange.edit'/>"><img src="images/buttons/action_edit.png"/></a>
								</c:if>
							</security:authorize>	
						</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		</table>
		</td>
		</tr>
		<!-------------------  EXCHANGES END -------------------->
		
		
	</table>				
</fieldset>
