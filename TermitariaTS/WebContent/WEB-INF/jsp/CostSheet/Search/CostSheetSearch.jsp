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

<c:set var="isUserAll" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CostSheetSearchAll}">
	<c:set var="isUserAll" value="true"/>
</security:authorize>


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">	
				<a href="#" id="back" onclick="getContentFromUrlDirect('ProjectDetails.htm?ACTION=GET&projectId=${PROJECT_ID}&hasProjectDetail=true', 'MAIN_CONTENT');"><spring:message code="back"/></a>
				<!--  <a href="#" id="addNewCostSheet" ><spring:message code="add" /></a>	 -->												
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<span class="section_title"><img src="images/titles/CostSheet.jpg"><spring:message code="costsheet.section.title"/></span>

<table>
	<tr>
		<td>
			<form:form id="searchForm" name="searchForm" commandName="searchCostSheetBean" onsubmit="validateAndSubmitCostSheetSearchForm('CostSheetSearch.htm','searchForm','RESULTS', 'costSheetSearchForm_teamMemberId', 'teamMemberId'); return false;">
			<form:hidden path="teamMemberId" id="teamMemberId"/>
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				
				<!-- first line  -->
				<tr>
					<!-- PROJECT -->
					<td class="labelTd" style="width:100px">
						<spring:message code="costsheet.project"/>
					</td>
					<td>
					<div title='<spring:message code="select"/>' id="costSheetSearchProjectsId">
						<form:select path="projectId" tabindex="1" id="costSheetSearchForm_project" cssClass="formFieldSearch" onchange="manageProjectSelectInCostSheetSearchForm(this.options[selectedIndex].value, 'costSheetSearchForm_firstName', 'costSheetSearchForm_lastName', 'costSheetSearchForm_teamMemberId', 'personCostSheetReporter', 'teamMemberCostSheetReporter', '${DISPLAY_NAME_INPUT}', 'teamMemberAutoCompleteContainer', 'teamMemberId', '${isUserAll}'); getContentFromUrlDirect('CostSheetBillable.htm?projectId=' + this.value, 'BILLABLE'); changeTitle(this.options[selectedIndex].title, 'costSheetSearchProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'costSheetSearchForm_project', 'costSheetSearchForm_project1');">
							<option value="-2" class="chooseProject" title='<spring:message code="select" />'><spring:message code="select" /></option>
							<option value="-1" class="fromOrganization" title='<spring:message code="costsheet.for.organization" />'><spring:message code="costsheet.for.organization" /></option>
							<option value="0" class="allProjects" title='<spring:message code="costsheet.all.projects" />'><spring:message code="costsheet.all.projects" /></option>
							<c:forEach var="project" items="${USER_PROJECTS}">
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
								<c:choose>
									<c:when test="${searchCostSheetBean.projectId == project.projectId}">
										<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>	
									</c:when>
									<c:otherwise>
										<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>	
									</c:otherwise>
								</c:choose>	
							</c:forEach>
						</form:select>
					</div>
					
					<div id="costSheetSearchForm_project1">
						<span class="chooseProject"></span>
						<span class="fromOrganization"></span>	
						<span class="allProjects"></span>												
						<c:forEach var="project" items="${USER_PROJECTS}">	
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
					
					</td>
					
					<!-- START DATE -->
					<td class="labelTd" style="width:100px">
						<spring:message code="costsheet.start.date"/>
					</td>
					<td>
						<form:input path="startDate" id="costSheetSearchForm_startDate" readonly="true" cssClass="formFieldSearch" tabindex="2" onclick="document.getElementById('startCalendar').style.zIndex = 1;instantiateStartEndCalendars('startCalendar','costSheetSearchForm_startDate', '${TODAY_DATE}', 'startTime', true, 'costSheetStartSearch', document.getElementById('costSheetSearchForm_endDate').value, true, '1');"/> 
					</td>
					<td>
						<div id="startCalendar"></div>
						<div id="startTime"></div>
					</td>
					
					<!-- NR OF RESULTS PER PAGE -->
					<td class="labelTd" style="width:140px">
						<spring:message code="costsheet.results"/>
					</td>
					<td>
						<form:select path="resultsPerPage" tabindex="3">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				
				<!-- second line  -->
				<tr>			
					<!-- ACTIVITY -->
					<td class="labelTd">
						<spring:message code="costsheet.activity"/>
					</td>
					<td>
						<form:input path="activityName" tabindex="4" id="costSheetSearchForm_activityName" cssClass="formFieldSearch" maxlength="200" onkeypress="return restrictSpecialCharactersForObs(event);"/>
					</td>
					
					<!-- END DATE -->
					<td class="labelTd">
						<spring:message code="costsheet.end.date"/>
					</td>
					<td>
						<form:input path="endDate" id="costSheetSearchForm_endDate" readonly="true" cssClass="formFieldSearch" tabindex="5" onclick="document.getElementById('endCalendar').style.zIndex = 1;instantiateStartEndCalendars('endCalendar','costSheetSearchForm_endDate', '${TODAY_DATE}', 'endTime', false, 'costSheetEndSearch', document.getElementById('costSheetSearchForm_startDate').value, true, '1');"/> 
					</td>
					<td>
						<div id="endCalendar"></div>
						<div id="endTime"></div>
					</td>
					
	 			</tr>
	 			
	 			<!-- third line -->
	 			<tr>
	 				<!-- COST PRIC MIN -->
	 				<td class="labelTd">
	 					<spring:message code="costsheet.cost.price.min"/>
	 				</td>
	 				<td>
	 					<form:input path="costPriceMin" id="costSheetSearchForm_costPriceMin" cssClass="formFieldSearch" tabindex="6" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('costSheetSearchForm_costPriceMin')"/>
	 				</td>
	 				<!-- COST SHEET REPORTER - TEAM MEMBER-->
	 				<td class="labelTd teamMemberCostSheetReporter" style="display:none">
	 					<spring:message code="costsheet.reporter"/>
	 				</td>
	 				<td class="teamMemberCostSheetReporter" style="display:none">
	 					<div id="teamMemberAutoComplete" class="autoComplete">
	 						<input id="costSheetSearchForm_teamMemberId" type="text" tabindex="7" class="formFieldSearch" onkeypress="return restrictSpecialCharactersForName(event);"/>
	 						<div id="teamMemberAutoCompleteContainer"/>
	 					</div>
	 				</td>
	 			
	 				<c:if test="${DISPLAY_NAME_INPUT eq true}">
		 				<!-- COST SHEET REPORTER - FIRST NAME-->
		 				<td class="labelTd personCostSheetReporter">
		 					<spring:message code="costsheet.reporter.firstName"/>
		 				</td>
		 				<td class="personCostSheetReporter">
		 					<form:input path="firstName" id="costSheetSearchForm_firstName" cssClass="formFieldSearch" tabindex="7" onkeypress="return restrictSpecialCharactersForName(event);"/>
		 				</td>
		 			</c:if>
	 				<td>
	 				</td>
	 				
	 			</tr>
	 			
	 			<!-- fourth line-->
	 			<tr>
	 				<!-- COST PRIC MAX -->
	 				<td class="labelTd">
	 					<spring:message code="costsheet.cost.price.max"/>
	 				</td>
	 				<td>
	 					<form:input path="costPriceMax" id="costSheetSearchForm_costPriceMax" cssClass="formFieldSearch" tabindex="8" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('costSheetSearchForm_costPriceMax')"/>
	 				</td>
		 				
		 			<c:if test="${DISPLAY_NAME_INPUT eq true}">
		 				<!-- COST SHEET REPORTER - LAST NAME-->
		 				<td class="labelTd personCostSheetReporter">
		 					<spring:message code="costsheet.reporter.lastName"/>
		 				</td>
		 				<td class="personCostSheetReporter">
		 					<form:input path="lastName" id="costSheetSearchForm_lastName" cssClass="formFieldSearch" tabindex="9" onkeypress="return restrictSpecialCharactersForName(event);"/>
		 				</td>
		 			</c:if>
		 			<!-- BILLABLE -->
												
			 		<td class="labelTd" id="billableId" style="display:none">		 		 					
						<spring:message code="costsheet.billable"/>					
					</td>
					<td>
						<div id="BILLABLE">
						</div>
					</td>
	 			</tr>
	 			
	 			<!-- fifth line -->
	 			<tr>
	 				<!-- COST PRICE CURRENCY -->
	 				<td class="labelTd">
	 					<spring:message code="costsheet.cost.price.currency"/>
	 				</td>
	 				<td>
	 					<form:select path="costPriceCurrencyId" id="costSheetSearchForm_costPriceCurrency" cssClass="formFieldSearch" tabindex="11">
	 						<form:option value="-1" title='<spring:message code="select"/>'><spring:message code="select"/></form:option>
	 						<c:forEach var="currency" items="${ORG_CURRENCIES}">
								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
	 						</c:forEach>
	 					</form:select>
	 				</td>
	 				
	 				<!-- COST SHEET ID -->
	 				<td class="labelTd">
		 				<spring:message code="costsheet.id"/>
		 			</td>
	 				<td>
	 					<form:input path="id" id="costSheetSearchForm_id" cssClass="formFieldSearch" tabindex="12" onkeypress="return restrictCharactersOnlyForNumbers(event);" onkeyup="restrictToMaxInteger('costSheetSearchForm_id');"/>
	 				</td>
	 				
	 			</tr>
	 			<tr>
					<td colspan="6"></td>
					<td>			
						<input type="button" class="button" id="searchCostSheet" value="<spring:message code="search"/>" tabindex="13"/>			
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

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script type="text/javascript">

	// -------------------------------HOME MENU BUTTON----------------------------------------------------
	//var getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//--------------------------------ADD COST SHEET BUTTON---------------------------------------------------
	var getObject = new ObjSubmit("CostSheetForm.htm?ACTION=ADD&BACK_URL=CostSheetSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("addNewCostSheet", "click", getContentFromUrl, getObject, true);

	// -------------------------------SUBMIT SEARCH FORM---------------------------------------------------
	submitForm('CostSheetSearch.htm', 'searchForm', 'RESULTS');

	// ------------------------------- SEARCH COST SHEET ----------------------------------------------------
	YAHOO.util.Event.addListener("searchCostSheet", "click", validateAndSubmitCostSheetSearchForm, { url : "CostSheetSearch.htm", formId : "searchForm", container : "RESULTS", 
		withContext : true, teamMemberInput : "costSheetSearchForm_teamMemberId", teamMemberInputHiddenFieldId: "teamMemberId"}
				, true);

	var submitObject = new ObjValidateAndSubmitCostSheetSearch('CostSheetSearch.htm', 'searchForm', 'RESULTS', 'costSheetSearchForm_teamMemberId', 'teamMemberId');

	keyListener = new YAHOO.util.KeyListener("costSheetSearchForm_costPriceMin", { keys:13 },  { fn:validateAndSubmitCostSheetSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("costSheetSearchForm_costPriceMax", { keys:13 },  { fn:validateAndSubmitCostSheetSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("costSheetSearchForm_teamMemberId", { keys:13 },  { fn:validateAndSubmitCostSheetSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("costSheetSearchForm_firstName", { keys:13 },  { fn:validateAndSubmitCostSheetSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("costSheetSearchForm_lastName", { keys:13 },  { fn:validateAndSubmitCostSheetSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	//  ------------------------------ SELECT THE LAST PROJECT ----------------------------------------------
	<c:if test="${lastProject ne null}">
		manageProjectSelectInCostSheetSearchForm(document.getElementById('costSheetSearchForm_project').options[document.getElementById('costSheetSearchForm_project').selectedIndex].value, 'costSheetSearchForm_firstName', 'costSheetSearchForm_lastName', 'costSheetSearchForm_teamMemberId', 'personCostSheetReporter', 'teamMemberCostSheetReporter', '${DISPLAY_NAME_INPUT}', 'teamMemberAutoCompleteContainer', 'teamMemberId', '${isUserAll}'); 
		getContentFromUrlDirect('CostSheetBillable.htm?projectId=' + document.getElementById('costSheetSearchForm_project').value, 'BILLABLE'); 
		changeTitle(document.getElementById('costSheetSearchForm_project').options[document.getElementById('costSheetSearchForm_project').selectedIndex].title, 'costSheetSearchProjectsId'); 
		document.getElementById('costSheetSearchForm_project').style.color = getStyleTextColor(document.getElementById('costSheetSearchForm_project').selectedIndex, 'costSheetSearchForm_project', 'costSheetSearchForm_project1');
	</c:if>
</script>
