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
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_RecordSearchAll}">
	<c:set var="isUserAll" value="true"/>
</security:authorize>


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">	
				<c:if test="${searchRecordBean.projectId gt 0}">
					<a href="#" id="back" onclick="getContentFromUrlDirect('Home.htm', 'MAIN_CONTENT');"><spring:message code="back"/></a>
				</c:if>		
					
				<a href="#" id="addNewRecord" ><spring:message code="add" /></a>													
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<span class="section_title"><img src="images/titles/Record.jpg"><spring:message code="record.section.title"/></span>


<table>
	<tr>
		<td>
			<form:form id="searchForm" name="searchForm" commandName="searchRecordBean" onsubmit="validateAndSubmitRecordSearchForm('RecordSearch.htm','searchForm','RESULTS', 'recordSearchForm_teamMemberId', 'teamMemberId'); return false;">
			<form:hidden path="teamMemberId" id="teamMemberId"/>
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>

				<!-- first line  -->
				<tr>
					<!-- PROJECT -->
					<td class="labelTd" style="width:100px">
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_RecordSearchAll}">
							<spring:message code="record.project.slash.org"/>
						</security:authorize>
						<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_RecordSearchAll}">
							<spring:message code="record.project.slash.org"/>
						</security:authorize>
					</td>
					<td>
						<div title='<spring:message code="select"/>' id="recordSearchProjectsId">	
	
							<form:select path="projectId" tabindex="1" id="recordSearchForm_project" cssClass="formFieldSearch" onchange="manageProjectSelectInRecordSearchForm(this.options[selectedIndex].value, 'recordSearchForm_firstName', 'recordSearchForm_lastName', 'recordSearchForm_teamMemberId', 'personRecordOwner', 'teamMemberRecordOwner', '${DISPLAY_NAME_INPUT}', 'teamMemberAutoCompleteContainer', 'teamMemberId', '${isUserAll}'); getContentFromUrlDirect('RecordBillable.htm?projectId=' + this.value, 'BILLABLE'); changeTitle(this.options[selectedIndex].title, 'recordSearchProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'recordSearchForm_project', 'recordSearchForm_project1','${searchRecordBean.activityId}');">
								<option value="-2" class="chooseProject" title='<spring:message code="select" />'><spring:message code="select" /></option>
								<option value="-1" class="fromOrganization" title='<spring:message code="record.for.organization" />'><spring:message code="record.for.organization" /></option>
								<option value="0" class="allProjects" title='<spring:message code="record.all.projects" />'><spring:message code="record.all.projects" /></option>
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
										<c:when test="${searchRecordBean.projectId == project.projectId}">
											<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>	
										</c:when>
										<c:otherwise>
											<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>	
										</c:otherwise>
									</c:choose>										
								</c:forEach>
							</form:select>
						</div>
						
						<div id="recordSearchForm_project1">
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
					
					<!-- RECORD OWNER - TEAM MEMBER-->
	 				<td class="labelTd teamMemberRecordOwner" style="display:none">
	 					<spring:message code="record.owner"/>
	 				</td>
	 				<td class="teamMemberRecordOwner" style="display:none">
	 					<div id="teamMemberAutoComplete" class="autoComplete">
	 						<input id="recordSearchForm_teamMemberId" type="text" tabindex="8" class="formFieldSearch" onkeypress="return restrictSpecialCharactersForName(event);"/>
	 						<div id="teamMemberAutoCompleteContainer"/>
	 					</div>
	 				</td>
	 			
	 				<c:if test="${DISPLAY_NAME_INPUT eq true}">
		 				<!-- RECORD OWNER - FIRST NAME-->
		 				<td class="labelTd personRecordOwner">
		 					<spring:message code="record.owner.firstName"/>
		 					<sup title="<spring:message code="record.owner.firstName.title"/>">[?]</sup>
		 				</td>
		 				<td class="personRecordOwner">
		 					<form:input path="firstName" id="recordSearchForm_firstName" cssClass="formFieldSearch" tabindex="8" onkeypress="return restrictSpecialCharactersForName(event);"/>
		 				</td>

		 			</c:if>
					
					<!-- NR OF RESULTS PER PAGE -->
					<td class="labelTd" style="width:100px">
						<spring:message code="record.results"/>
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
						<spring:message code="record.activity"/>
					</td>
					<td>
						<div title='<spring:message code="record.choose.project" />' id="recordSearchForm_activity_titleDiv">
							<form:select path="activityId" tabindex="4" id="recordSearchForm_activity" cssClass="formFieldSearch" onchange="changeTitle(this.options[selectedIndex].text, 'recordSearchForm_activity_titleDiv')">
								<c:choose>
									<c:when test='${searchRecordBean.activityId > 0}'>
										<option value="${searchRecordBean.activityId}"><spring:message code="record.choose.project" /></option>
									</c:when>
									<c:otherwise>
										<option value="-1"><spring:message code="record.choose.project" /></option>
									</c:otherwise>
								</c:choose>
							</form:select>
						</div>
					</td>
					
					<c:if test="${DISPLAY_NAME_INPUT eq true}">
		 				
		 				<!-- RECORD OWNER - LAST NAME-->
		 				<td class="labelTd personRecordOwner">
		 					<spring:message code="record.owner.lastName"/>
		 					<sup title="<spring:message code="record.owner.lastName.title"/>">[?]</sup>
		 				</td>
		 				<td class="personRecordOwner">
		 					<form:input path="lastName" id="recordSearchForm_lastName" cssClass="formFieldSearch" tabindex="9" onkeypress="return restrictSpecialCharactersForName(event);"/>
		 				</td>
		 			</c:if>
					
	 			</tr>
	 				
	 			<!-- third line-->
	 			<tr>
	 				<!-- START DATE -->
					<td class="labelTd" style="width:100px">
						<spring:message code="record.start.date"/>
						<sup title="<spring:message code="record.start.date.title"/>">[?]</sup>
					</td>
					<td>
						<form:input path="startDate" id="recordSearchForm_startDate" readonly="true" cssClass="formFieldSearch" tabindex="2" onclick="document.getElementById('startCalendar').style.zIndex = 1;instantiateStartEndCalendars('startCalendar','recordSearchForm_startDate', '${TODAY_DATE}', 'startTime', true, 'recordStartSearch', document.getElementById('recordSearchForm_endDate').value, true, '1'); clearWeekAndMonthSelect();"/> 
						<div id="startCalendar"></div>
						<div id="startTime"></div>
					</td>
					
					<!-- END DATE -->
					<td class="labelTd">
						<spring:message code="record.end.date"/>
						<sup title="<spring:message code="record.end.date.title"/>">[?]</sup>
					</td>
					
					<td>
						<form:input path="endDate" id="recordSearchForm_endDate" readonly="true" cssClass="formFieldSearch" tabindex="5" onclick="document.getElementById('endCalendar').style.zIndex = 1;instantiateStartEndCalendars('endCalendar','recordSearchForm_endDate', '${TODAY_DATE}', 'endTime', false, 'recordEndSearch', document.getElementById('recordSearchForm_startDate').value, true, '1'); clearWeekAndMonthSelect();"/> 
						<div id="endCalendar"></div>
						<div id="endTime"></div>
					</td>
					
		 			
		 			<!-- BILLABLE -->
					<td class="labelTd" id="billableId" style="display:none">	
						<spring:message code="record.billable"/>
					</td>
	 				<td>
	 					<div id="BILLABLE">
						</div>
	 				</td>	
		 			 			
	 			</tr>
	 			
	 			<tr>
	 				<!-- RECORD ID -->
	 				<!--  
	 				<td class="labelTd">
		 				<spring:message code="record.id"/>
		 			</td>
	 				<td>
	 					<form:input path="id" id="recordSearchForm_id" cssClass="formFieldSearch" tabindex="11" onkeypress="return restrictCharactersOnlyForNumbers(event);" onkeyup="restrictToMaxInteger('recordSearchForm_id');"/>
	 				</td>
	 				-->
	 				
	 				<!-- WEEK -->
					<td class="labelTd">
						<spring:message code="record.week"/>
					</td>
					<td>
						<div title='<spring:message code="record.choose.week" />' id="recordSearchForm_week_titleDiv">
							<form:select path="week" id="recordSearchForm_week" cssClass="formFieldSearch" onchange="clearDateInputsAndSelect('recordSearchForm_month')">
								<option value="-1"><spring:message code="record.choose.week"/></option>
								<option value="0"><spring:message code="record.choose.week.current" /></option>
								<option value="1"><spring:message code="record.choose.week.current-1" /></option>
								<option value="2"><spring:message code="record.choose.week.current-2" /></option>	
							</form:select>
						</div>
					</td>
					
					<!-- MONTH -->
					<td class="labelTd">
						<spring:message code="record.month"/>
					</td>
					<td>
						<div title='<spring:message code="record.choose.month" />' id="recordSearchForm_month_titleDiv">
							<form:select path="month" id="recordSearchForm_month" cssClass="formFieldSearch" onchange="clearDateInputsAndSelect('recordSearchForm_week')">
								<option value="-1"><spring:message code="record.choose.month" /></option>
								<option value="0"><spring:message code="record.choose.month.current" /></option>
								<option value="1"><spring:message code="record.choose.month.current-1" /></option>
								<option value="2"><spring:message code="record.choose.month.current-2" /></option>
							</form:select>
						</div>
					</td>
	 			</tr>
	 				 			
	 			<tr>
					<td colspan="6"></td>
					<td>			
						<input type="button" class="button" id="searchRecord" value="<spring:message code="search"/>" tabindex="12"/>			
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

	<c:if test="${searchRecordBean.activityId ne null}">
		manageProjectSelectInRecordSearchForm(document.getElementById('recordSearchForm_project').options[document.getElementById('recordSearchForm_project').selectedIndex].value, 'recordSearchForm_firstName', 'recordSearchForm_lastName', 'recordSearchForm_teamMemberId', 'personRecordOwner', 'teamMemberRecordOwner', '${DISPLAY_NAME_INPUT}', 'teamMemberAutoCompleteContainer', 'teamMemberId', '${isUserAll}','${searchRecordBean.activityId}');
	</c:if>

	// -------------------------------HOME MENU BUTTON----------------------------------------------------
	//var getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//--------------------------------ADD RECORD BUTTON---------------------------------------------------
	var getObject = new ObjSubmit("RecordForm.htm?ACTION=ADD&BACK_URL=RecordSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("addNewRecord", "click", getContentFromUrl, getObject, true);

	//recordSearchForm_id
//	YAHOO.util.Event.addListener("recordSearchForm_id", "paste", 
//			function onPaste (event){
				
//				if (event.preventDefault){ // WC3/Firefox
//					event.preventDefault();
//				}else {//IE
//					event.returnValue = false;
//				}

				//var data="";    
	   			//data=window.clipboardData.getData("Text");
	   			//alert("Clipboard: "+data);
	   			//alert("Clipboard data: "+event.clipboardData);
				
//				alert("Paste: "+document.getElementById('recordSearchForm_id').value+" event: "+event.type+" which: "+event.preventDefault);
//			}, null, true);

	// -------------------------------SUBMIT SEARCH FORM---------------------------------------------------
	submitForm('RecordSearch.htm', 'searchForm', 'RESULTS');

	// ------------------------------- SEARCH RECORD ----------------------------------------------------
	YAHOO.util.Event.addListener("searchRecord", "click", validateAndSubmitRecordSearchForm, { url : "RecordSearch.htm", formId : "searchForm", container : "RESULTS", 
		withContext : true, teamMemberInput : "recordSearchForm_teamMemberId", teamMemberInputHiddenFieldId: "teamMemberId"}
				, true);

	var submitObject = new ObjValidateAndSubmitRecordSearch('RecordSearch.htm', 'searchForm', 'RESULTS', 'recordSearchForm_teamMemberId', 'teamMemberId');

	keyListener = new YAHOO.util.KeyListener("recordSearchForm_teamMemberId", { keys:13 },  { fn:validateAndSubmitRecordSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("recordSearchForm_firstName", { keys:13 },  { fn:validateAndSubmitRecordSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("recordSearchForm_lastName", { keys:13 },  { fn:validateAndSubmitRecordSearchForm, scope: submitObject, correctScope:true }, null );
	keyListener.enable();

	
	
	
	//  ------------------------------ SELECT THE LAST PROJECT ----------------------------------------------
	<c:if test="${lastProject ne null}">
		manageProjectSelectInRecordSearchForm(document.getElementById('recordSearchForm_project').options[document.getElementById('recordSearchForm_project').selectedIndex].value, 'recordSearchForm_firstName', 'recordSearchForm_lastName', 'recordSearchForm_teamMemberId', 'personRecordOwner', 'teamMemberRecordOwner', '${DISPLAY_NAME_INPUT}', 'teamMemberAutoCompleteContainer', 'teamMemberId', '${isUserAll}','${searchRecordBean.activityId}');
		getContentFromUrlDirect('RecordBillable.htm?projectId=' + document.getElementById('recordSearchForm_project').value, 'BILLABLE');
		changeTitle(document.getElementById('recordSearchForm_project').options[document.getElementById('recordSearchForm_project').selectedIndex].title, 'recordSearchProjectsId');
		document.getElementById('recordSearchForm_project').style.color = getStyleTextColor(document.getElementById('recordSearchForm_project').selectedIndex, 'recordSearchForm_project', 'recordSearchForm_project1');
	</c:if>

	
</script>
