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


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">			
				<a href="${OM_MODULES}"><spring:message code="modules"/></a>
				<a href=""><spring:message code="help"/></a>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<table>
	<!---------- ADD RECORD IN TIMESHEET -------------->
	<tr>
		<td width="300">
			<span class="section_title" style="border-bottom: 1px solid #F2EDEF"><img src="images/titles/ProjectDetailEdit.jpg"><spring:message code="record.home.fast.timesheet"/></span>

	
	<table border="0" width="300">	
		<tr>
			<td>
				<div id="MESSAGES">
					<%@ include file="../Messages.jsp" %>	
				</div>
				
				<c:choose>
					<c:when test="${!(empty PROJECTS)}">	
						<form:form id="paginationForm" name="paginationForm" commandName="searchProjectBean">																						
							<table id="listingTable">
								<c:set var="i" value="0"/>						
								<c:set var="cssClass" value=""/>
								
								<c:forEach var="bean" items="${PROJECTS}">			
									<c:set var="i" value="${i + 1}"/>												
									<tr class="${cssClass}"  onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">									
										<td  class="subsection_title">
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">																																																														
												<c:choose>
													<c:when test="${bean.status != 2 && bean.status != 3}">
														<a href="#" onClick="getContentFromUrlDirect('ProjectDetails.htm?ACTION=GET&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="project.edit.detail"/>">
															${bean.name}			
														</a>	
														<span class="addActionStyle">(<img src="images/buttons/action_add_small_button.png"/> 
															<a href="#" style="color: black;" onClick="getContentFromUrlDirect('CostSheetForm.htm?ACTION=ADD&BACK_URL=Home.htm&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="costsheet.add.section.title"/>"><spring:message code="costsheet.add.section.title"/></a>)
														</span>
														
													</c:when>
													<c:otherwise>
														<a href="#"  onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '<spring:message code="project.details.project"/>', '${bean.name}',  '450px')" title="<spring:message code="project.view"/>">
															${bean.name}
														</a>
														<span class="addActionStyle">(<img src="images/buttons/action_add_small_button.png"/> 
															<a href="#" style="color: black;" onClick="getContentFromUrlDirect('CostSheetForm.htm?ACTION=ADD&BACK_URL=Home.htm&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="costsheet.add.section.title"/>"><spring:message code="costsheet.add.section.title"/></a>)
														</span>
											
													</c:otherwise>		
												</c:choose>																																																					
											</security:authorize>	
											
											<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">																																	
												<c:choose>
													<c:when test="${USER_ID eq bean.managerId && bean.status != 2 && bean.status != 3}">		
														<a href="#" onClick="getContentFromUrlDirect('ProjectDetails.htm?ACTION=GET&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="project.edit.detail"/>">
															${bean.name}			
														</a>
														<span class="addActionStyle">(<img src="images/buttons/action_add_small_button.png"/> 
															<a href="#" style="color: black;" onClick="getContentFromUrlDirect('CostSheetForm.htm?ACTION=ADD&BACK_URL=Home.htm&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="costsheet.add.section.title"/>"><spring:message code="costsheet.add.section.title"/></a>)
														</span>	
													</c:when>
													<c:otherwise>
														<a href="#" onClick="displayInfoPanel('ProjectView.htm?projectId=${bean.projectId}', '<spring:message code="project.details.project"/>', '${bean.name}',  '450px')" title="<spring:message code="project.view"/>">
															${bean.name}
														</a>
														<span class="addActionStyle">(<img src="images/buttons/action_add_small_button.png"/> 
															<a href="#" style="color: black;" onClick="getContentFromUrlDirect('CostSheetForm.htm?ACTION=ADD&BACK_URL=Home.htm&projectId=${bean.projectId}&hasProjectDetail=${bean.hasProjectDetail}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="costsheet.add.section.title"/>"><spring:message code="costsheet.add.section.title"/></a>)
														</span>
													</c:otherwise>
												</c:choose>
											</security:authorize>																												
										</td>					
									</tr>
											
									<c:set var="j" value="0"/>						
									<c:set var="cssClass" value=""/>
									
									<c:forEach var="activities" items="${ACTIVITIES}">
										<c:set var="j" value="${j + 1}"/>
										<c:forEach var="beanActivity" items="${activities}" >		
										<c:choose>
											<c:when test="${j  == i}">
												<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${j}">									
													<td>
														&nbsp &nbsp &nbsp &nbsp &nbsp
														<a href="#" onClick="getContentFromUrlDirect('RecordForm.htm?ACTION=ADD&projectId=${bean.projectId}&activityId=${beanActivity.activityId}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="record.add.activity.in"/> ${beanActivity.name}">
															${beanActivity.name}			
														</a>																																																														
													</td>					
												</tr>
											</c:when>
										</c:choose>											
									</c:forEach>				
								</c:forEach>					
							</c:forEach>				
						</table>
					</form:form>				
				</c:when>
				
				<c:otherwise>
					<div class="noSearchResults"><spring:message code="project.no.results.message"/></div>
				</c:otherwise>
			</c:choose>	
		</td>
		</tr>	
	</table>
	</td>
	<!---------- ADD RECORD IN TIMESHEET END ------------->
	
	
	
	<td width="20" align="center">
		<div style="border-right: 1px dotted #F2EDEF; width:0px; height:500px;"></div>
	</td>
	
	<!---------- SEARCH TIMESHEET -------------->
	<td width="300">
		<span class="section_title" style="border-bottom: 1px solid #F2EDEF"><img src="images/titles/Record.jpg"><spring:message code="record.home.search"/></span>
		
		
	<table border="0" width="300">	
		<tr>
			<td>
				<div id="MESSAGES">
					<%@ include file="../Messages.jsp" %>	
				</div>
			
			<c:choose>
				<c:when test="${!(empty PROJECTS)}">	
					<form:form id="paginationForm" name="paginationForm" commandName="searchProjectBean">																						
						<table id="listingTable">
							<c:set var="i" value="0"/>						
							<c:set var="cssClass" value=""/>
							<c:forEach var="bean" items="${PROJECTS}">			
								<c:set var="i" value="${i + 1}"/>												
								<tr class="${cssClass}"  onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">									
									<td  class="subsection_title">																																																														
											
										<a href="#" onClick="getContentFromUrlDirect('RecordSearch.htm?projectId=${bean.projectId}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="record.view.project"/>">
											${bean.name}
										</a>																																																								
																												
									</td>					
								</tr>
										
								<c:set var="j" value="0"/>						
								<c:set var="cssClass" value=""/>
								<c:forEach var="activities" items="${ACTIVITIES}">
									<c:set var="j" value="${j + 1}"/>	
									<c:forEach var="beanActivity" items="${activities}" >		
									<c:choose>
										<c:when test="${j  == i}">
											<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${j}">									
												<td>
													&nbsp &nbsp &nbsp &nbsp &nbsp
													<a href="#" onClick="getContentFromUrlDirect('RecordSearch.htm?projectId=${bean.projectId}&activityId=${beanActivity.activityId}&BACK_URL=Home.htm', 'MAIN_CONTENT')" title="<spring:message code="record.view.project.activity"/> ${beanActivity.name}">
														${beanActivity.name}			
													</a>																																																														
												</td>					
											</tr>
										</c:when>
									</c:choose>											
								</c:forEach>				
							</c:forEach>					
						</c:forEach>				
					</table>
				</form:form>				
			</c:when>
			<c:otherwise>
				<div class="noSearchResults"><spring:message code="project.no.results.message"/></div>
			</c:otherwise>
		</c:choose>
			
		</td>
		</tr>
		
	</table>
	</td>
	<!---------- SEARCH TIMESHEET END ------------>


	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">
		<td width="20" align="center">
			<div style="border-right: 1px dotted #F2EDEF; width:0px; height:500px;" ></div>
		</td>
		
		<!---------- NOTIFICATIONS -------------->
		<td width="300">
			<span class="section_title" style="border-bottom: 1px solid #F2EDEF"><img src="images/titles/Notification.jpg"><spring:message code="notification.home"/></span>
	
			<c:choose>
				<c:when test="${!(empty NOTIFICATIONS)}">
					<table border="0" width="300">
						<tr>
							<td>	
								<c:forEach var="notification" items="${NOTIFICATIONS}">
									<tr>
										<td align="left" >
											${notification.subject}<br/>
											<span class="smallExplanatoryText">(<fmt:formatDate value="${notification.issuedDate}" pattern="dd/MM/yyyy"/>)</span>
										</td>
										<td align="center">
											
										</td>	
									</tr>
									<tr>
										<td align="left" style="padding-bottom:30px">
											${notification.message}
										</td>
									</tr>
								</c:forEach>
							</td>
						</tr>
					</table>
				</c:when>
			</c:choose>	
		</td>	
		<!---------- NOTIFICATIONS END -------------->
	</security:authorize>
	</tr>
</table>


<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->
