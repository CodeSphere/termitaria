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

<c:set var="reportsView" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ReportsView}">
	<c:set var="reportsView" value="true"/>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ReportsView}">
	<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">		
		<c:set var="reportsView" value="true"/>
	</c:if>
</security:authorize>

<div id="PANEL_CONTENT">
	<table>
		<tr>
			<td>
			<!-- //////////////////////////////PERSONS SEARCH/////////////////////////////// -->
			<fieldset style="width: 700px">
				<legend>
					<spring:message	code="report.time.sheet.person.search" />
				</legend>
				<form:form id="personSearchForm" name="searchForm" commandName="searchPersonBean">
					<table style="width: 700px">
						<!-- first line -->
						<tr>	
							<!-- LAST NAME -->
							<td class="labelTd">
								<spring:message code="report.time.sheet.person.search.person.lastName"/> 
							</td>					
							<td>
								<form:input path="lastName" id="searchForm_lastName" maxlength="200" tabindex="1" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
							</td>
										
							<!-- FIRST NAME -->
							<td class="labelTd">
								<spring:message code="report.time.sheet.person.search.person.firstName"/>
							</td>					
							<td>
								<form:input path="firstName" id="searchForm_firstName" maxlength="200" tabindex="2" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
							</td>
												
							<!-- NR OF RESULTS PER PAGE -->
							<td class="labelTd">
								<spring:message code="person.results"/>
							</td>
							<td>
								<form:select path="resultsPerPage" tabindex="3">
									<c:forEach var="val" items="${RESULTS_PER_PAGE}">
										<form:option value="${val}" />
									</c:forEach>
								</form:select>
							</td>
						</tr>
				
						<!-- second line -->										
						<tr>
							<td class="labelTd">
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ReportsView}">
									<spring:message code="report.time.sheet.person.search.project.slash.org"/>
								</security:authorize>
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ReportsView}">
									<spring:message code="report.time.sheet.person.search.project"/>
								</security:authorize>
							</td>
												
							<td>
								<c:choose>
									<c:when test="${PROJECTS ne null}">		
										<div title='<spring:message code="report.time.sheet.person.search.choose.project"/>' id="personSearchProjectsId">
											<form:select path="projectId" tabindex="4" id="searchForm_project" cssClass="formFieldSearch" onchange="changeTitle(this.options[selectedIndex].title, 'personSearchProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'searchForm_project', 'searchForm_project1');">							
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">
												<option value="-1" class="fromOrganization" title='<spring:message code="person.from.organization"/>'><spring:message code="person.from.organization"/></option>																																										
											</security:authorize>
											<option value="0" class="allProjects" title='<spring:message code="person.all.projects"/>'><spring:message code="person.all.projects"/></option>
											<c:forEach var="project" items="${PROJECTS}">																						
												<c:choose>
													<c:when test="${project.clientId != -1}">
														<c:set var="client" value="hasClient"/>														
														<c:choose>
																<c:when test="${project.status == 1}">
																<c:set var="prj" value=""/>	
															</c:when>
															<c:otherwise>
																<spring:message code="project.for.client" var="prj"/>	
															</c:otherwise>
														</c:choose>																									
													</c:when>
														<c:otherwise>
														<c:set var="client" value="noClient"/>	
														<spring:message code="project.for.organization" var="prj"/>	
													</c:otherwise>	
												</c:choose>	
															
												<c:choose>
													<c:when test="${project.status == 1}">
														<c:set var="type" value="Opened"/>	
														<spring:message code="project.opened" var="Type"/>		
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${project.status == 2}">
																<c:set var="type" value="Closed"/>	
																<spring:message code="project.closed" var="Type"/>		
															</c:when>
															<c:otherwise>
																<c:if test="${project.status == 3}">
																	<c:set var="type" value="Aborted"/>	
																	<spring:message code="project.abandoned" var="Type"/>																
																</c:if>																														
															</c:otherwise>	
														</c:choose>														
													</c:otherwise>	
												</c:choose>		
												<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>												
											</c:forEach>																												
											</form:select>
										</div>
															
										<div id="searchForm_project1">
											<span class="chooseProject"></span>		
												<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ReportsView}">
													<span class="fromOrganization"></span>	
												</security:authorize>																	
												<c:forEach var="project" items="${PROJECTS}">	
													<c:choose>
														<c:when test="${project.clientId != -1}">
															<c:set var="client" value="hasClient"/>																																																		
														</c:when>
														<c:otherwise>
															<c:set var="client" value="noClient"/>													
														</c:otherwise>	
													</c:choose>	
															
													<c:choose>
														<c:when test="${project.status == 1}">
															<c:set var="type" value="Opened"/>												
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${project.status == 2}">
																	<c:set var="type" value="Closed"/>														
																</c:when>
																<c:otherwise>
																	<c:if test="${project.status == 3}">
																		<c:set var="type" value="Aborted"/>																									
																	</c:if>																														
																</c:otherwise>	
															</c:choose>														
														</c:otherwise>	
													</c:choose>																																										
													<span class="${client}${type}"></span>												
												</c:forEach>		
											</div>
															
										</c:when>
										<c:when test="${PROJECTS ne null  and empty PROJECTS}">
											<div id="NO_PROJECTS">
												<spring:message code="person.no.projects"></spring:message>
											</div>
											<div id="MESSAGE">
											</div>
										</c:when>
									</c:choose>
								</td>
								
								<!-- WITH DELETED -->
								<td class="labelTd">
									<spring:message code="persons.has.been.deleted"/>
								</td>					
								<td>
									<form:checkbox path="withDeleted" tabindex="5" id="personForm_withDeleted"/>
								</td>
								
							</tr>
											
							<tr>					
								<td colspan="5"></td>																	
								<td>												
									<input type="button" class="button" onClick="submitTimeSheetReportPersonForm('ReportTimeSheetPersonSearch.htm','personSearchForm','SEARCH_RESULTS','PANEL_CONTENT')" id="reportSearchPerson" tabindex="6" value="<spring:message code="search"/>*"/>						
								</td>
							</tr>
						</table>
					</form:form>	
				</fieldset>
			</td>
		</tr>
				
		<tr>
			<td>
				<div id="SEARCH_RESULTS"></div>
			</td>
		</tr>
	</table>
</div>
<script>		
	//----------------------------------------SEARCH PERSON-------------------------------
	//YAHOO.util.Event.addListener("reportSearchPerson", "click", submitTimeSheetReportPersonForm , {url : 'ReportTimeSheetPersonSearch.htm', formId : 'personSearchForm', 
	//container : 'SEARCH_RESULTS', panelDiv : 'PANEL_CONTENT', withContext : true}, true);

	var submitObject = new ObjSubmit('ReportTimeSheetPersonSearch.htm', 'personSearchForm', 'SEARCH_RESULTS');

	keyListener = new YAHOO.util.KeyListener("searchForm_lastName", { keys:13 },  { fn:submitTimeSheetReportPersonForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("searchForm_firstName", { keys:13 },  { fn:submitTimeSheetReportPersonForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	
	
	
	
</script>
