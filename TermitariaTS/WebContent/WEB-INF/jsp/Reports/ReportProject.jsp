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

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">				
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->				
				
				<!-- PROJECT REPORT -->
				<c:if test="${reportsView eq true}">
					<a href="#" id="createProjectReport" ><spring:message code='report.submenu.project'/></a>
				</c:if>	
				
				<!-- TIME SHEET REPORT -->
				<c:if test="${reportsView eq true}">
					<a href="#" id="createTimeSheetReport" ><spring:message code="report.submenu.time.sheet"/></a>
				</c:if>									
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<div id="CONTENT">
	<span class="section_title"><img src="images/titles/Reports.jpg"><spring:message code="report.submenu.project"/></span>			
	<%@ include file="../Messages.jsp" %>
<c:if test="${ERRORS eq null and WARNINGS eq null}">
	<form:form id="reportProjectForm" name="reportProjectForm" commandName="reportProjectCriteria">

<table style="width: 940px" class="tableAdd"><tr><td>

<!-- //////////////////////////////////MAIN CRITERIA////////////////////////////////////////// -->
			<fieldset style="width: 720px; margin : 20px"><legend><spring:message
				code="report.project.main.criteria" /></legend>	
			<table style="width: 720px">
			<tr>
			   <td class="labelTd" style="width: 100px"><spring:message code='report.project.form.title' /><span id="required">*</span></td>
			   <td><form:input path="reportTitle" id="reportTitle" tabindex="1" cssClass="formFieldSearch required" onkeypress="OLD_FILE_NAME = this.value" onkeyup="restrictCharactersForFileName('reportTitle')" maxlength="150"/></td>
			   <td></td>
			   <td class="labelTd" style="width: 100px"><spring:message code='report.project.form.project' /></td>
			   <td>	<div title='<spring:message code="report.project.form.project.choose"/>' id="reportProjectSearchProjectsId">
			   		<form:select path="projectId" id="reportProjectForm_projectId" tabindex="2" cssClass="formFieldSearch"  onchange="javascript:changeTitle(this.options[selectedIndex].title, 'reportProjectSearchProjectsId');this.style.color = getStyleTextColor(this.selectedIndex, 'reportProjectForm_projectId', 'reportProjectForm_projectId1');">
									<c:forEach var="project" items="${AVAILABLE_PROJECTS}">
										<c:choose>
											<c:when test="${project.clientId != -1}">
												<c:set var="client" value="hasClient" />
												<c:choose>
													<c:when test="${project.status == 1}">
														<c:set var="prj" value="" />
													</c:when>
													<c:otherwise>
														<spring:message code="project.for.client" var="prj" />
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:set var="client" value="noClient" />
												<spring:message code="project.for.organization" var="prj" />
											</c:otherwise>
										</c:choose>

										<c:choose>
											<c:when test="${project.status == 1}">
												<c:set var="type" value="Opened" />
												<spring:message code="project.opened" var="Type" />
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${project.status == 2}">
														<c:set var="type" value="Closed" />
														<spring:message code="project.closed" var="Type" />
													</c:when>
													<c:otherwise>
														<c:if test="${project.status == 3}">
															<c:set var="type" value="Aborted" />
															<spring:message code="project.abandoned" var="Type" />
														</c:if>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
										<option title="${project.name} ${prj}${Type}"
											class="${client}${type}" value="${project.projectId}"
											onclick="assignValue ('reportProjectForm_projectName', '${project.name}')">${project.name}</option>
									</c:forEach>
					</form:select></div>
					<div id="reportProjectForm_projectId1">															
									<c:forEach var="project" items="${AVAILABLE_PROJECTS}">	
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
					<form:hidden path="projectName" id="reportProjectForm_projectName" />  </td>
				<td class="labelTd"><spring:message code='report.project.file.type' /></td> 
				<td><form:select path="reportFileType"  id="reportProjectForm_reportFileType" tabindex="3" cssClass="formFieldSearch">
					    <c:forEach var="format" items="${FORMATS}">
								<form:option value="${format}">${format}</form:option>
						</c:forEach>
					</form:select> </td>	
			</tr>
			<tr>
				<td class="labelTd"><spring:message code="report.project.form.start.date"/></td>
				<td ><form:input path="reportStartDate" id="reportProjectForm_reportStartDate" tabindex="4" readonly="true" cssClass="formFieldSearch" onclick="document.getElementById('startCalendar').style.zIndex = 1;instantiateStartEndCalendars('startCalendar','reportProjectForm_reportStartDate', '${TODAY_DATE}', 'startTime', true, 'reportProjectStartSearch', document.getElementById('reportProjectForm_reportEndDate').value, false, '1');"/></td>
				<td><div id="startCalendar"></div>
					<div id="startTime"></div></td>

				<td class="labelTd" > 
					<spring:message code="report.project.language"/>
				</td>	
	 			<td>
	 				<form:select path="reportLanguage" cssClass="formFieldSearch" tabindex="${tab + 1}" id="reportProjectForm_reportLanguage" onchange="populateDefaultValuesProjectReport ('${DEFAULT_VALUES_JSON}',this.value)" >
					     <c:forEach var="language" items="${NOM_SUPPORTED_LANGUAGES}">
							<option value="${language}" title="${language}"  >${language}</option>
						</c:forEach>
				     </form:select>
				</td>
	 				
	 			<td class="labelTd" style="width: 100px"><spring:message code='price.compute.label' /></td>
			    <td><form:select path="priceCompute" id="reportProjectForm_priceCompute" tabindex="6" cssClass="formFieldSearch" >
					<c:forEach var="priceCompute" items="${NOM_PRICES_COMPUTE_TYPES}">
						<option value="${priceCompute.value}" title='<spring:message code="${priceCompute.label}"/>' > <spring:message code="${priceCompute.label}"/></option>
					</c:forEach>
					</form:select></td>				     
			</tr>
			<tr>
				<td class="labelTd"><spring:message code="report.project.form.end.date"/></td>
				<td ><form:input path="reportEndDate" id="reportProjectForm_reportEndDate" tabindex="7" cssClass="formFieldSearch" readonly="true" onclick="document.getElementById('endCalendar').style.zIndex = 1;instantiateStartEndCalendars('endCalendar','reportProjectForm_reportEndDate', '${TODAY_DATE}', 'endTime', false, 'reportProjectEndSearch', document.getElementById('reportProjectForm_reportStartDate').value, false, '1');"/></td>
				<td><div id="endCalendar"></div>
					 <div id="endTime"></div></td>
				<td class="labelTd" ><spring:message code="report.project.interval"/></td>
				<td ><form:select path="subtotalPeriod" id="activityForm_subtotalPeriod" tabindex="8" cssClass="formFieldSearch" >									
							<c:forEach var="subtotalInterval" items="${NOM_SUBTOTAL_PERIOD}">																								
								<option title='<spring:message code="${subtotalInterval.label}"/>' value="${subtotalInterval.value}"><spring:message code="${subtotalInterval.label}"/></option>										
							</c:forEach>							
					</form:select>	</td>	
				<td class="labelTd" ><spring:message code="report.project.billable"/></td>
				<td >
					<c:set var="tab" value="8"/>
					<c:forEach var="billable" items="${NOM_BILLABLE}">	
						<c:set var="tab" value="${tab + 1}"/>																							
						<font class="labelTd"><spring:message code="${billable.label}"/></font>
						<form:radiobutton path="billable" tabindex="${tab}" value="${billable.value}" />										
					</c:forEach></td>	
			</tr>
			<tr>
				<td class="labelTd">
					<spring:message code="report.project.form.currency"/>
				</td>
				<td>
					<form:select  path="currencyId" id="reportProjectForm_currencyId" tabindex="5" cssClass="formFieldSearch" >	 						
	 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
							<option value="${currency.value}" title="${currency.label}" onclick="assignValue ('reportProjectForm_currencyName', '${currency.label}')">${currency.label}</option>
	 					</c:forEach>
	 				</form:select>
	 				<form:hidden path="currencyName" id="reportProjectForm_currencyName" />
	 			</td>
				<td></td><td colspan="5"></td>
			</tr>
			</table>	
					
			</fieldset>
</td></tr>
<!-- //////////////////////////////////RECORD COLUMNS////////////////////////////////////////// -->
<tr><td><fieldset style="width: 890px; align: center; margin : 20px" ><legend><spring:message
				code="report.project.time.sheet.criteria" /></legend>	
<table style="margin-bottom:  20 px">
	<tr><td style="line-height:10px;">&nbsp;</td></tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
											
	<td > 
		<div class="selectTitle"><spring:message code="report.column.absent"/></div>
		<br/>
		<select	id="recordColumnsForm_select1"	name="recordColumnsForm_select1" tabindex="${tab + 2}" multiple="multiple" size="10" class="formField "> 
			<c:forEach var="columnKey" items="${ABSENT_RECORD_COLUMNS_KEY_LIST}">
				<option id="${columnKey}" >?</option>
			</c:forEach> 
		</select> 
	</td> 														
											
	<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
		<div id="addRecordColumn" class="addButton" title="<spring:message code="report.column.add"/>"></div><br /><br />
		<div id="removeRecordColumn" class="removeButton"	title="<spring:message code="report.column.remove"/>"></div>
	</td>
											
	<td>
		<div class="selectTitle"><spring:message code="report.column.present"/></div>
		<br/>	
		<select id="recordColumnsForm_select2" name="present" tabindex="${tab + 3}"  multiple="multiple" size="10" class="formField "> 
			<c:forEach var="columnKey" items="${PRESENT_RECORD_COLUMNS_KEY_LIST}">
				<option id="${columnKey}" >?</option>
			</c:forEach> 
		</select>
	</td> 
											
	<td width="100%">&nbsp;</td>
	</tr> 
	<tr>
	<td style="line-height:20px;">&nbsp;</td>
	</tr>
</table>
<table>
	<tr>
		<td>
			<div id="recordInputs">Input message:</div>
		</td>
	</tr>
</table> 		
</fieldset>								
</td></tr>
<!-- //////////////////////////////////COST COLUMNS////////////////////////////////////////// -->
<tr><td><fieldset style="width: 890px; align: center; margin : 20px" ><legend><spring:message
				code="report.project.cost.criteria" /></legend>
<table>
	<tr><td style="line-height:10px;">&nbsp;</td></tr>
	<tr>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
											
	<td id="allDepartments"> 
		<div class="selectTitle"><spring:message code="report.column.absent"/></div>
		<br/>
		<select	id="costColumnsForm_select1" name="costColumnsForm_select1" tabindex="${tab + 18}" multiple="multiple" size="10" class="formField ">
			<c:forEach var="columnKey" items="${ABSENT_COST_COLUMNS_KEY_LIST}">
				<option id="${columnKey}" >cst</option>
			</c:forEach>  
		</select> 
	</td> 														
											
	<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
		<div id="addCostColumn" class="addButton" title="<spring:message code="report.column.add"/>"></div><br /><br />
		<div id="removeCostColumn" class="removeButton"	title="<spring:message code="report.column.remove"/>"></div>
	</td>
											
	<td>
		<div class="selectTitle"><spring:message code="report.column.present"/></div>
		<br/>	
		<select id="costColumnsForm_select2" name="present" tabindex="${tab + 19}" multiple="multiple" size="10" class="formField "> 
			<c:forEach var="columnKey" items="${PRESENT_COST_COLUMNS_KEY_LIST}">
				<option id="${columnKey}" >cst</option>
			</c:forEach> 
		</select>
	</td> 
											
	<td width="100%">&nbsp;</td>
	</tr> 
	<tr><td style="line-height:20px;">&nbsp;</td></tr>
</table>
<table>
	<tr>
		<td>
			<div id="costInputs">Input message:</div>
		</td>
	</tr>
</table> 						
								
</fieldset></td></tr>

<!-- ///////////////////////////////////TOTAL//////////////////////////////////////////////// -->
<tr><td><fieldset style="width: 890px; align: center; margin : 20px" ><legend><spring:message
				code="report.project.total.criteria" /></legend>
<table style="width: 880px"><tr>
			   <td class="labelTd" style="width: 190px"><spring:message code='report.project.total.cost.price' /></td>
			   <td style="width: 230 px"><form:input path="totalCostPrice" id="totalCostPrice" tabindex="${tab + 26}" cssClass="formField required" maxlength="40"/></td>
			   
			   <td class="labelTd" style="width: 190px"><spring:message code='report.project.total.billing.price' /></td>
			   <td style="width: 230 px"><form:input path="totalBillingPrice" id="totalBillingPrice" tabindex="${tab + 27}" cssClass="formField required" maxlength="40"/></td>
			   
			   
		</tr>
</table>
				
</fieldset></td></tr>	
<!-- /////////////////////////////////////////////////////////////////////////////////////// -->				
<tr><td colspan="2" class="formActions" align="right">
			<input type="hidden" id = "uniqueId" name="REPORT_UID"/>	
			
			<input type="button" class="button" id="create" tabindex="${tab + 32}" value="<spring:message code="create.report"/>" />
			
</td></tr></table>	
						
	</form:form>
</c:if>	
			

</div>
</td>
		
	
<!-- //////////////////////////////////////////////////////////////////////////// -->		
<td id="afterContentCell"></td>
		
		
		

	</tr>
</table>
<script>

//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	
//------------ PROJECT REPORT ---------------------------------------------------------------	
	
	var submitReportObject = new ObjSubmit("ReportProject.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("createProjectReport", "click", getContentFromUrl, submitReportObject, true);

//------------ TIME SHEET REPORT ---------------------------------------------------------------	
	
	var submitReportObject = new ObjSubmit("ReportTimeSheet.htm?ACTION=ADD&BACK_URL=ReportProject.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("createTimeSheetReport", "click", getContentFromUrl, submitReportObject, true);	

</script>

<c:if test="${ERRORS eq null and WARNINGS eq null}">
<script>	
//------------ ADD/REMOVE RECORD COLUMNS ---------------------------------------------------------------	
	
	//Add event on Add Record Column Button
	YAHOO.util.Event.addListener('addRecordColumn', 'click', function addRecordColumn() {
		moveSelectOptionsSourceDestColumn('recordColumnsForm_select1', 'recordColumnsForm_select2', null, 'recordInputs');
	}, null, false);
								
	//Add event on Remove Record Column Button
	YAHOO.util.Event.addListener('removeRecordColumn', 'click', function removeRecordColumn() {
		moveSelectOptionsSourceDestColumn('recordColumnsForm_select2', 'recordColumnsForm_select1', true, 'recordInputs');
	}, null, false);

//------------ ADD/REMOVE COST COLUMNS ---------------------------------------------------------------	
	
	//Add event on Add Cost Column Button
	YAHOO.util.Event.addListener('addCostColumn', 'click', function addCostColumn() {
		moveSelectOptionsSourceDestColumn('costColumnsForm_select1', 'costColumnsForm_select2', null, 'costInputs');
	}, null, false);
								
	//Add event on Remove Record Column Button
	YAHOO.util.Event.addListener('removeCostColumn', 'click', function removeCostColumn() {
		moveSelectOptionsSourceDestColumn('costColumnsForm_select2', 'costColumnsForm_select1', true, 'costInputs');
	}, null, false);	

//------------ CREATE REPORT ---------------------------------------------------------------	
	
	//var submitObject = new ObjSubmit("ReportProject.htm", "reportProjectForm", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("create", "click", validateAndSubmitForm, submitObject, true);	

	YAHOO.util.Event.addListener('create', 'click', function createProjectReport() {
		manageOpenReport("ReportProject.htm", '${REPORT_SERVLET_URL}',"reportProjectForm", "MAIN_CONTENT","uniqueId","reportProjectForm_reportFileType","PRJ",'<spring:message code="report.submenu.project"/>');
	}, null, false);
	
//--------------POPULATE DEFAULT VALUES-----------------------------------------------------------
window.onload = populateDefaultValuesProjectReport('${DEFAULT_VALUES_JSON}',document.getElementById('reportProjectForm_reportLanguage').value,true);	
document.getElementById('reportProjectForm_projectId').style.color = getStyleTextColor(0, 'reportProjectForm_projectId', 'reportProjectForm_projectId1');
</script>	
</c:if>
	
