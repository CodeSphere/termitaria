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
					<a href="#" id="home"><spring:message code="home" /></a>									
					<a href="#" id="back" onclick="getContentFromUrl('${BACK_URL}','MAIN_CONTENT');">
						<spring:message code="back"/>
					</a>
					<a href="#" id="submenuSearch" title="<spring:message code="project.submeniu.search.title"/>"><spring:message code="search"/></a>																										
				</div>
				<div id="submenu_footer"></div>
	        </td>    
	        <td id="contentCell">
	        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Project.jpg"><spring:message code="project.section.title"/></span>
<c:choose>
	<c:when test="${CLIENT_TYPE == 1}">
		<span title="${CLIENT_NAME}" class="userName">${CLIENT_NAME}</span>
	</c:when>
	<c:otherwise>
		<span title="${CLIENT_WHOLE_NAME}" class="userName"> ${CLIENT_FIRST_NAME}&nbsp;${CLIENT_LAST_NAME} </span>
	</c:otherwise>
</c:choose>

<%@ include file="../Messages.jsp" %>

<c:choose>
	<c:when test="${!(empty PROJECTS)}">
	
	<fieldset style="width: 890px;">
			<legend><spring:message code="project.title.listing" /></legend>
				<table class="list_results">
					<tr class="list_results_header">																																															
						
						<th style="width:250px;">
							<a class="sortLink" id="sortLinkId" href="#">
								<spring:message code="project.name"/>
							</a>
						</th>
						
						<th style="width:250px;">
							<a class="sortLink" id="sortLinkId" href="#">
								<spring:message code="project.client"/>
							</a>
						</th>
						
									
						<th style="width: 350px;"> 
							<spring:message code="project.manager.name"/>	
						</th>								
						
						<th style="width: 80px;"> 
							<spring:message code="project.status"/>	
						</th>
																
						<th style="width: 180px;"> <spring:message code="actions"/></th>			
						</tr>
						
						<c:set var="i" value="0"/>						
						<c:set var="cssClass" value=""/>
						
						<c:forEach var="bean" items="${PROJECTS}">			
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
									${bean.name}									
								</td>		
								
								<td>	
									<c:choose>
										<c:when test="${bean.client.type == 2}">
											${bean.client.p_firstName} ${bean.client.p_lastName}											
										</c:when>
										<c:otherwise>											
											${bean.client.c_name}
										</c:otherwise>
									</c:choose>												
																		
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
									<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
										<tr>																																			
											<td>																																									
												<a href="#" onClick="getContentFromUrlDirect('Project.htm?ACTION=GET&projectId=${bean.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="edit"/>"><img src="images/buttons/action_edit.png"/></a>																																																									
											</td>
											<td>
																				
												<a href="#" onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '${bean.panelHeaderName}', '${bean.name}',  '400px')" title="<spring:message code="project.view"/>">
													<img src="images/buttons/action_view.png"/>
												</a>
																								
											</td>
											<td>
												<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedSearch}">
													<c:if test="${bean.hasProjectTeam eq true and bean.status == 1}">
														<a href="#" onClick="getContentFromUrlDirect('ProjectTeam.htm?ACTION=GET_PROJECT_TEAM_FROM_PROJECT_LISTING&projectId=${bean.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="project.team"/>"><img src="images/buttons/action_edit_team.png"/></a>
													</c:if>		
												</security:authorize>	
												<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedSearch}">
													<c:if test="${bean.hasProjectTeam eq true and bean.status == 1 and USER_ID eq bean.managerId}">
														<a href="#" onClick="getContentFromUrlDirect('ProjectTeam.htm?ACTION=GET_PROJECT_TEAM_FROM_PROJECT_LISTING&projectId=${bean.projectId}&BACK_URL=${NEXT_BACK_URL}', 'MAIN_CONTENT')" title="<spring:message code="project.team"/>"><img src="images/buttons/action_edit_team.png"/></a>
													</c:if>		
												</security:authorize>										
											</td>
																																																						
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedSearch}">
												<c:choose>
													<c:when test="${bean.status == 1}">                                                																						
														<td>
															<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=FINISH_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT' ,'<spring:message code="project.finish.confirmationMessage"/>', '<spring:message code="confirm.finish"/>');" title="<spring:message code="project.finish" />">
																<img src="images/buttons/action_disable.png"/>
															</a> 
														</td>
														<td>	
															<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=ABORT_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT' ,'<spring:message code="project.abort.confirmationMessage"/>', '<spring:message code="confirm.abort"/>');" title="<spring:message code="project.abort" />">
																<img src="images/buttons/action_disable.png"/>
															</a>
														</td> 														
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${bean.status == 2}">
																<td>
																	<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT', '<spring:message code="project.open.one.confirmationMessage"/>' , '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.open" />">							
																		<img src="images/buttons/action_enable.png"/>
																	</a>
																</td>		
																<td>
																</td>														
															</c:when>
															<c:otherwise>
																<td>
																	<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT', '<spring:message code="project.open.one.confirmationMessage"/>', '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.activate" />">							
																		<img src="images/buttons/action_abort.gif"/>
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
													<c:choose>
														<c:when test="${bean.status == 1}">                                                																						
															<td>
																<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=FINISH_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT' ,'<spring:message code="project.finish.confirmationMessage"/>', '<spring:message code="confirm.finish"/>');" title="<spring:message code="project.finish" />">
																	<img src="images/buttons/action_disable.png"/>
																</a> 
															</td>
															<td>	
																<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=ABORT_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT' ,'<spring:message code="project.abort.confirmationMessage"/>', '<spring:message code="confirm.abort"/>');" title="<spring:message code="project.abort" />">
																	<img src="images/buttons/action_disable.png"/>
																</a>
															</td> 														
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${bean.status == 2}">
																	<td>
																		<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT', '<spring:message code="project.open.one.confirmationMessage"/>' , '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.open" />">							
																			<img src="images/buttons/action_enable.png"/>
																		</a>
																	</td>		
																	<td>
																	</td>														
																</c:when>
																<c:otherwise>
																	<td>
																		<a href="#" onClick="deleteWithConfirmation('ProjectListForClient.htm?ACTION=ACTIVATE_PROJECT_LISTING&projectId=${bean.projectId}&clientId=${clientId}', '','MAIN_CONTENT', '<spring:message code="project.open.one.confirmationMessage"/>', '<spring:message code="confirm.activate"/>');" title="<spring:message code="project.activate" />">							
																			<img src="images/buttons/action_abort.gif"/>
																		</a>
																	</td>
																	<td>
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
		</fieldset>		
	</c:when>
	<c:otherwise>	
		<spring:message code="project.no.results.message"/>
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
	getObject = new ObjSubmit("ClientSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//------------ PROJECT SEARCH ---------------------------------------------------------------		
	var getObject = new ObjSubmit("ProjectSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);
	
</script>

