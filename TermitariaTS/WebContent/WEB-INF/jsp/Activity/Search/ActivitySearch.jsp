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


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">			
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->		
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
					<a href="#" id="addNewActivity" title="<spring:message code="activity.submeniu.add.title"/>"><spring:message code='add'/></a>
				</security:authorize>	
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
					<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">		
						<a href="#" id="addNewActivity" title="<spring:message code="activity.submeniu.add.title"/>"><spring:message code='add'/></a>
					</c:if>
				</security:authorize>															
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<span class="section_title"><img src="images/titles/Activity.jpg"><spring:message code="activity.section.title"/></span>

<table>
	<tr>
		<td>	
			<form:form id="searchForm" name="searchForm" commandName="searchActivityBean" onsubmit="validateAndSubmitFormWithParams('ActivitySearch.htm','searchForm','RESULTS', 'true'); return false;">			
			
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				
				<!-- first line  -->
				<tr>
					<!-- ACTIVITY NAME -->
					<td class="labelTd">
						<spring:message code="activity.name"/> 			
					</td>		
								
					<td>
						<form:input path="name" id="searchForm_name" maxlength="200" tabindex="1" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
					</td>
								
					<!-- PROJECT-->
						
					<td class="labelTd">
						<spring:message code="activity.project"/>						
					</td>					
					
					<td>
						<c:choose>
							<c:when test="${PROJECTS ne null}">		
								<div title='<spring:message code="activity.choose.project"/>' id="activitySearchProjectsId">																													
								<form:select path="projectId" tabindex="2" id="searchForm_project" cssClass="formFieldSearch" onchange="javascript:getContentFromUrlDirect('ActivityBillable.htm?projectId=' + this.value, 'BILLABLE');changeTitle(this.options[selectedIndex].title, 'activitySearchProjectsId');this.style.color = getStyleTextColor(this.selectedIndex, 'searchForm_project', 'searchForm_project1'); showBillable(this.options[selectedIndex].value, 'billableId');">																										
									<option title='<spring:message code="activity.choose.project"/>' class="chooseProject" value="-1"><spring:message code="activity.choose.project"/></option>												
										<spring:message code="activity.from.organization" var="fromOrg"/>	
										<option value="0" class="fromOrganization" title="${fromOrg}"><spring:message code="activity.from.organization"/></option>											
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
									<span class="fromOrganization"></span>									
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
							<c:when test="${PROJECTS ne null and empty PROJECTS}">
								<div id="NO_PROJECTS">
									<spring:message code="activity.no.projects"></spring:message>
								</div>
								<div id="MESSAGE">
								</div>
							</c:when>
						</c:choose>
					</td>															
																					
					<!-- NR OF RESULTS PER PAGE -->
					<td class="labelTd">
						<spring:message code="activity.results"/>
					</td>
					<td>
						<form:select path="resultsPerPage" tabindex="5">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
						
				<!-- second line  -->
				<!-- BILLABLE -->
				<tr>							
			 		<td class="labelTd" id="billableId" style="display:none">		 		 					
						<spring:message code="activity.billable" />									
					</td>
					<td>
						<div id="BILLABLE">
						</div>
					</td>
				</tr>					
								
				<tr>					
					<td colspan="5"></td>
																						
					<td>												
						<input type="button" class="button" id="searchActivity" tabindex="6" value="<spring:message code="search"/>"/>						
					</td>
				</tr>				
			</table>
			</form:form>
			<br/>
		</td>
	</tr>

	<tr>
		<td>
			<div id="MESSAGES">
				<%@ include file="../../Messages.jsp" %>	
			</div>
			<div id="RESULTS"></div>
		</td>
	</tr>
		
</table>


<script language="text/javascript">				

	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);		

	document.searchForm.name.focus();
	
	submitForm('ActivitySearch.htm','searchForm','RESULTS');

	//----------------------------------------ADD A NEW ACTIVITY-------------------------------		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		var submitObject = new ObjSubmit("Activity.htm?ACTION=ADD&BACK_URL=ActivitySearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewActivity", "click", getContentFromUrl, submitObject, true);
	</security:authorize>		

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">	
			var submitObject = new ObjSubmit("Activity.htm?ACTION=ADD&BACK_URL=ActivitySearch.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("addNewActivity", "click", getContentFromUrl, submitObject, true);
		</c:if>
	</security:authorize>	

	//----------------------------------------SEARCH ACTIVITY-------------------------------
	YAHOO.util.Event.addListener("searchActivity", "click", submitActivityForm , {url : 'ActivitySearch.htm', formId : 'searchForm', 
	container : 'RESULTS', withContext : true}, true);

</script>


<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->
