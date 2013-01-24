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
<%@include file="../Taglibs.jsp" %>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
	<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td id="submenuCell">
				<div id="submenu">			
					<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->										
					<a href="#" id="back" onclick="getContentFromUrl('${BACK_URL}','MAIN_CONTENT');">
						<spring:message code="back"/>
					</a>																				
					<a href="#" id="submenuSearch" title="<spring:message code="project.team.members.submeniu.search.title"/>"><spring:message code="search"/></a>								
				</div>
				<div id="submenu_footer"></div>
	        </td>    
	        <td id="contentCell">
	        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/TeamMember.jpg"><spring:message code="project.team.members.section.title"/></span>
<span title="${PROJECT_NAME}" class="userName">${PROJECT_NAME}</span>

<%@ include file="../Messages.jsp" %>

<c:choose>
	<c:when test="${!(empty TEAM_MEMBERS)}">
	
	<fieldset style="width: 600px;">
		<legend><spring:message code="project.team.members.title.listing" /></legend>
				<table class="list_results">
					<tr class="list_results_header">
						
						<th style="width: 300px;">
							<spring:message code="person.lastName"/>								
						</th>
						
						<th style="width: 300px;">
							<spring:message code="person.firstName"/>							
						</th>																
						<th style="width: 80px;"><spring:message code="actions"/></th>											
					</tr>
																								
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					
					<c:forEach var="member" items="${TEAM_MEMBERS}">
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
							<td align="center">	
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">																		
									<c:choose>
										<c:when test="${project.status != 2 && project.status != 3}">
											<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${member.memberId}&hasTeamMemberDetail=${member.hasTeamMemberDetail}&projectId=${project.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
												${member.lastName}	  
											</a>		
										</c:when>
										<c:otherwise>
											<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${member.memberId}&projectId=${project.projectId}', '${member.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '450px')" title="<spring:message code="person.view"/>">
												${member.lastName}
											</a>
										</c:otherwise>		
									</c:choose>																																												
								</security:authorize>
								
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">
									<c:choose>
										<c:when test="${IS_MANAGER eq true && project.status != 2 && project.status != 3}">																						
											<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${member.memberId}&hasTeamMemberDetail=${member.hasTeamMemberDetail}&projectId=${project.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
												${member.lastName}	
											</a>																																		
										</c:when>
										<c:otherwise>
											<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${member.memberId}&projectId=${project.projectId}', '${member.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '450px')" title="<spring:message code="person.view"/>">
													${member.lastName}	
											</a>
										</c:otherwise>
									</c:choose>															
								</security:authorize>		
														
							</td>
							<td align="center">	
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">																		
									<c:choose>
										<c:when test="${project.status != 2 && project.status != 3}">
											<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${member.memberId}&hasTeamMemberDetail=${member.hasTeamMemberDetail}&projectId=${project.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
												${member.firstName}	
											</a>			
										</c:when>
										<c:otherwise>
											<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${member.memberId}&projectId=${project.projectId}', '${member.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '450px')" title="<spring:message code="person.view"/>">
												${member.firstName}
											</a>
										</c:otherwise>		
									</c:choose>																																																																									
								</security:authorize>
								
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">
									<c:choose>
										<c:when test="${IS_MANAGER eq true && project.status != 2 && project.status != 3}">																						
											<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${member.memberId}&hasTeamMemberDetail=${member.hasTeamMemberDetail}&projectId=${project.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>">
												${member.firstName}	
											</a>																																		
										</c:when>
										<c:otherwise>
											<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${member.memberId}&projectId=${project.projectId}', '${member.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '450px')" title="<spring:message code="person.view"/>">
												${member.firstName}	
											</a>
										</c:otherwise>
									</c:choose>															
								</security:authorize>	
																			
							</td>
																										
							<td align="center">	
								<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
									<tr>																				
										<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">
											<c:if test="${project.status != 2 && project.status != 3}">
												<td>												
													<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${member.memberId}&hasTeamMemberDetail=${member.hasTeamMemberDetail}&projectId=${project.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>"><img src="images/buttons/action_edit_teammember_details.png"/></a>																							
												</td>	
											</c:if>															
										</security:authorize>	
										
										<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">
											<c:if test="${IS_MANAGER eq true && project.status != 2 && project.status != 3}">			
												<td>												
													<a href="#" onClick="getContentFromUrlDirect('TeamMemberDetail.htm?ACTION=GET&teamMemberId=${member.memberId}&hasTeamMemberDetail=${member.hasTeamMemberDetail}&projectId=${project.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="team.member.edit.detail"/>"><img src="images/buttons/action_edit_teammember_detail.jpg"/></a>																							
												</td>	
											</c:if>															
										</security:authorize>	
										<td>
											<a href="#" onClick="displayInfoPanel('TeamMemberView.htm?memberId=${member.memberId}&projectId=${project.projectId}', '${member.panelHeaderName}', '${bean.firstName} ${bean.lastName}',  '450px')" title="<spring:message code="person.view"/>"><img src="images/buttons/action_view.png"/></a>
										</td>																																																																																			
									</tr>
								</table>									
							</td>								
						</tr>						
					</c:forEach>
				</table>
		</fieldset>		
	</c:when>	
	<c:otherwise>
		<spring:message code="project.team.members.no.results.message"/>
	</c:otherwise>
</c:choose>	

	<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
				</div><!-- end CONTENT div -->
			</td>
			<td id="afterContentCell"></td>
		</tr>
	</table>
	<!-- ///////////////////////////////////////////////////////////////////////////// -->
	
<script language="text/javascript">

	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);	

	//------------ PROJECT SEARCH ---------------------------------------------------------------		
	var getObject = new ObjSubmit("ProjectList.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);	
	
</script>
