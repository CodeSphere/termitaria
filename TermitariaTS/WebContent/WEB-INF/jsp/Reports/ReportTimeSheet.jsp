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
				<!--<a href="#" id="back" onclick="ENTITY = getTimeSheetReportData();check_add('${BACK_URL}', 'MAIN_CONTENT');"><spring:message code="back"/></a>-->
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
			<span class="section_title"><img src="images/titles/Reports.jpg"><spring:message code="report.time.sheet.section.title"/></span>
			<%@ include file="../Messages.jsp" %>
<c:if test="${ERRORS eq null and MESSAGES eq null}">
			
			<form:form id="reportTimeSheetForm" name="reportTimeSheetForm" commandName="reportTimeSheetCriteria">			
				<table style="width: 940px" class="tableAdd">
					<tr>
						<td>
							<!-- //////////////////////////////REPORT MAIN SEARCH CRITERIA/////////////////////////// -->
							<fieldset style="width: 720px; align: center; margin : 20px">
								<legend>
									<spring:message	code="report.time.sheet.main.criteria" />
								</legend>	
								<table style="width: 720px">
									<tr>
										<!-- ====================================REPORT TITLE====================================================== -->
			   							<td class="labelTd" style="width: 100px">
			   								<spring:message code='report.time.sheet.title'/><span id="required">*</span>
			   							</td>
			   							<td style="width: 130px">
			   								<form:input path="reportTitle" id="reportTitle" cssClass="formFieldSearch required" tabindex="1" onkeypress="OLD_FILE_NAME = this.value" onkeyup="restrictCharactersForFileName('reportTitle')" maxlength="150"/>
			   							</td>
			   							<td>
			   							</td>
			   							<!-- ====================================REPORT CURRENCY====================================================== -->
										<td class="labelTd">
											<spring:message code="report.time.sheet.form.currency"/>
										</td>
										<td>
											<form:select  path="currencyId" id="reportTimeSheetForm_currencyId" cssClass="formFieldSearch" tabindex="2">	 						
	 											<c:forEach var="currency" items="${ORG_CURRENCIES}">
													<option value="${currency.value}" title="${currency.label}" onclick="document.getElementById('reportTimeSheetForm_currencyName').value = this.text">${currency.label}</option>
	 											</c:forEach>
	 										</form:select>
	 										<form:hidden path="currencyName" id="reportTimeSheetForm_currencyName" />
	 									</td>
			   							<!-- ====================================REPORT FORMAT====================================================== -->
			   							<td class="labelTd">
			   								<spring:message code='report.time.sheet.file.type'/>
			   							</td> 
										<td>
											<form:select path="reportFileType"  id="reportTimeSheetForm_reportFileType" cssClass="formFieldSearch" tabindex="3">
					    						<c:forEach var="format" items="${FORMATS}">
													<form:option value="${format}">${format}</form:option>
												</c:forEach>
											</form:select>
										</td>
																				
	 								</tr>
	 								
	 								<tr>
	 									<!-- ====================================REPORT START DATE====================================================== -->
	 									<td class="labelTd">
	 										<spring:message code="report.time.sheet.start.date"/>
	 									</td>
										<td>
											<form:input path="reportStartDate" id="reportTimeSheetForm_reportStartDate" cssClass="formFieldSearch" tabindex="4" onfocus="document.getElementById('startCalendar').style.zIndex = 1;instantiateStartEndCalendars('startCalendar','reportTimeSheetForm_reportStartDate', '${TODAY_DATE}', 'startTime', true, 'reportTimeSheetStart', document.getElementById('reportTimeSheetForm_reportEndDate').value, false, '1');"/>
										</td>
										<td>
											<div id="startCalendar"></div>
											<div id="startTime"></div>
										</td>
										
										<!-- ====================================PRICE COMPUTE TYPE====================================================== -->
										<td class="labelTd">
											<spring:message code='report.time.sheet.price.compute' />
										</td>
			    						<td>
			    							<form:select path="priceCompute" id="reportTimeSheetForm_priceCompute" cssClass="formFieldSearch" tabindex="5">
												<c:forEach var="priceCompute" items="${NOM_PRICES_COMPUTE_TYPES}">
													<option value="${priceCompute.value}" title='<spring:message code="${priceCompute.label}"/>' > <spring:message code="${priceCompute.label}"/></option>
												</c:forEach>
											</form:select>
										</td>
										
										<!-- ====================================SUBTOTAL INTERVAL====================================================== -->
										<td class="labelTd" >
											<spring:message code="report.time.sheet.subtotal.interval"/>
										</td>
										<td>
											<form:select path="subtotalPeriod" id="reportTimeSheetForm_subtotalPeriod" cssClass="formFieldSearch" tabindex="6">									
												<c:forEach var="subtotalInterval" items="${NOM_SUBTOTAL_PERIOD}">																								
													<option title='<spring:message code="${subtotalInterval.label}"/>' value="${subtotalInterval.value}"><spring:message code="${subtotalInterval.label}"/></option>										
												</c:forEach>							
											</form:select>
										</td>		
	 								</tr>
	 								
	 								<tr>
	 									<!-- ======================================REPORT END DATE====================================================== -->
	 									<td class="labelTd">
	 										<spring:message code="report.time.sheet.end.date"/>
	 									</td>
										<td >
											<form:input path="reportEndDate" id="reportTimeSheetForm_reportEndDate" cssClass="formFieldSearch" tabindex="7" onfocus="document.getElementById('endCalendar').style.zIndex = 1;instantiateStartEndCalendars('endCalendar','reportTimeSheetForm_reportEndDate', '${TODAY_DATE}', 'endTime', false, 'reportTimeSheetEnd', document.getElementById('reportTimeSheetForm_reportStartDate').value, false, '1');"/>
										</td>
										<td>
											<div id="endCalendar"></div>
					 						<div id="endTime"></div>
					 					</td>
					 					
					 					<!-- ======================================BILLABLE====================================================== -->
					 					<td class="labelTd">
					 						<spring:message code="report.time.sheet.billable"/>
					 					</td>
										<td>
						 					<font class="labelTd"><spring:message code="report.time.sheet.billable.yes" /></font>
						 						<form:radiobutton path="billable" value="Y" tabindex="8" id="reportTimeSheetForm_billable_yes"/> 
											<font class="labelTd"><spring:message code="report.time.sheet.billable.no" /></font>
												<form:radiobutton path="billable" value="N" tabindex="9" id="reportTimeSheetForm_billable_no"/> 
										</td>
										
										<!-- ======================================REPORT LANGUAGE====================================================== -->
										<td class="labelTd">
											<spring:message code="report.time.sheet.language"/>
										</td>	
	 									<td>
	 										<form:select path="reportLanguage" tabindex="10" cssClass="formFieldSearch" id="reportTimeSheetForm_reportLanguage" onchange="populateDefaultValuesTimeSheetReport('${DEFAULT_VALUES_JSON}',this.value)" >
										    	<c:forEach var="language" items="${NOM_SUPPORTED_LANGUAGES}">
													<option value="${language}" title="${language}">${language}</option>
												</c:forEach>
										    </form:select>
										</td>	
	 								</tr>
								</table>
							</fieldset>
						</td>
					</tr>
					
					
					<!-- //////////////////////////////////PERSONS AND TEAM MEMBERS////////////////////////////////////////// -->
					<tr>
						<td colspan="2" height="20">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							<fieldset style="width: 620px; align: center; margin : 20px">
								<legend>
									<spring:message	code="report.time.sheet.persons"/>
								</legend>
								<table >
									<tr>
										<td>
											<div style="margin-left: 50px">
												<select id="reportTimeSheetForm_personSelect" style="width: 260px;" tabindex="11" multiple="multiple" size="10" class="validate-length"/>
												</select>
												<div id="teamMembers"></div>
												<div id="persons"></div>
											</div>
										</td>
										<td width="100">
											
										</td>
										<td>
											<table>
												<tr>
													<td height="65">
														<div style="margin-left: 10px; margin-right: 10px; margin-top: 15px">
															<input type="button" class="button" id="addPersons" tabindex="12" value="<spring:message code="report.time.sheet.add.persons"/>" onclick="displayInfoPanel('ReportTimeSheetPersonSearch.htm', '<spring:message code='report.time.sheet.person.search'/>', '<spring:message code='report.time.sheet.person.search'/>', '760px')"/>
														</div>
													</td>
												</tr>
												<tr>
													<td height="65">
														<div style="margin-left: 10px; margin-right: 10px; margin-bottom: 10px">
															<input type="button" class="button" id="deletePersons" tabindex="13" value="<spring:message code="report.time.sheet.delete.persons"/>" />
														</div>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr style="height: 20px">
										<td >
											&nbsp;
										</td>
									</tr>	
								</table>	
							</fieldset>
						</td>
					</tr>
					
					<!-- //////////////////////////////////RECORD TABLE COLUMNS////////////////////////////////////////// -->
					<tr>
						<td colspan="2" height="20">
							&nbsp;
						</td>
					</tr>	
					<tr>
						<td>
							<fieldset style="width: 890px; align: center; margin : 20px">
								<legend>
									<spring:message	code="report.time.sheet.record.table.columns"/>
								</legend>	
								<table >
									<tr>
										<td style="line-height:10px;">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</td>
										<td > 
											<div class="selectTitle">
												<spring:message code="report.column.absent"/>
											</div>
											<br/>
											<select	id="reportTimeSheetForm_select1" name="reportTimeSheetForm_select1" tabindex="14" multiple="multiple" size="10" class="formField ">
												<c:forEach var="columnKey" items="${ABSENT_RECORD_COLUMNS_KEY_LIST}">
													<option id="${columnKey}"></option>
												</c:forEach>  
											</select> 
										</td> 														
										<td id="commands" style="padding: 0px 8px 0px 5px;">
											<br/><br/><br/><br/><br/>
											<div id="addRecordsTableColumn" class="addButton" title="<spring:message code="report.column.add"/>"></div>
											<br/><br/>
											<div id="removeRecordsTableColumn" class="removeButton" title="<spring:message code="report.column.remove"/>"></div>
										</td>									
										<td>
											<div class="selectTitle">
												<spring:message code="report.column.present"/>
											</div>
											<br/>	
											<select id="reportTimeSheetForm_select2" name="reportTimeSheetForm_select2"  tabindex="15" multiple="multiple" size="10" class="formField "> 
												<c:forEach var="columnKey" items="${PRESENT_RECORD_COLUMNS_KEY_LIST}">
													<option id="${columnKey}" ></option>
												</c:forEach> 
											</select>
										</td> 
										<td width="100%">
											&nbsp;
										</td>
									</tr> 
									<tr>
										<td style="line-height:20px;">
											&nbsp;
										</td>
									</tr>
								</table>
								<table>
									<tr>
										<td>
											<div id="recordsColumnNamesInputs"></div>
										</td>
									</tr>
								</table>
						 	</fieldset>								
						</td>
					</tr>
					<!-- /////////////////////////////////ACTION BUTTONS///////////////////////////////////////// -->				
					<tr>
						<td colspan="2" class="formActions" align="right">
							<input type="hidden" id = "uniqueId" name="REPORT_UID"/>
							<input type="button" class="button" tabindex="30" id="create" value="<spring:message code="create.report"/>" />
						</td>
					</tr>
				</table>
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
	
	var submitProjectReportObject = new ObjSubmit("ReportProject.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("createProjectReport", "click", getContentFromUrl, submitProjectReportObject, true);

	//------------ TIME SHEET REPORT ---------------------------------------------------------------	
	var submitTSReportObject = new ObjSubmit("ReportTimeSheet.htm?ACTION=ADD&BACK_URL=ReportTimeSheet.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("createTimeSheetReport", "click", getContentFromUrl, submitTSReportObject, true);
</script>

<c:if test="${ERRORS eq null and MESSAGES eq null}">	
<script>
	//------------ ADD/REMOVE RECORD COLUMNS ---------------------------------------------------------------	
	
	//Add event on Add Records Table Column Button
	YAHOO.util.Event.addListener('addRecordsTableColumn', 'click', function addRecordsTableColumn() {
		moveSelectOptionsSourceDestColumn('reportTimeSheetForm_select1', 'reportTimeSheetForm_select2', null, 'recordsColumnNamesInputs');
	}, null, false);
								
	//Add event on Remove Records Table Column Button
	YAHOO.util.Event.addListener('removeRecordsTableColumn', 'click', function removeRecordsTableColumn() {
		moveSelectOptionsSourceDestColumn('reportTimeSheetForm_select2', 'reportTimeSheetForm_select1', true, 'recordsColumnNamesInputs');
	}, null, false);

	//--------------assign the default selected currency name to the hidden param----------
	if (document.getElementById("reportTimeSheetForm_currencyId").options[0] != null) {
		document.getElementById('reportTimeSheetForm_currencyName').value = document.getElementById("reportTimeSheetForm_currencyId").options[0].text;
	}
	//--------------DELETE PERSONS------------------------------------------------------------------------
	YAHOO.util.Event.addListener('deletePersons', 'click', manageDeletePersons, {selectId: "reportTimeSheetForm_personSelect", teamMembersDiv: "teamMembers",
		personsDiv: "persons", teamMembersHiddenInputsName: "teamMemberIds", personsHiddenInputsName: "personIds" , 
		noSelectedItemsMessage: '<spring:message code="report.time.sheet.selectedPersonsMessage"/>', panelTitle: '<spring:message code="report.time.sheet.select.persons"/>'}
		, true);

	//------------ CREATE REPORT ---------------------------------------------------------------	

	//var submitObject = new ObjSubmit("ReportTimeSheet.htm", "reportTimeSheetForm", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("create", "click", validateAndSubmitForm, submitObject, true);	

	YAHOO.util.Event.addListener('create', 'click', function createTSReport() {
		manageOpenReport("ReportTimeSheet.htm", '${REPORT_SERVLET_URL}',"reportTimeSheetForm", "MAIN_CONTENT","uniqueId","reportTimeSheetForm_reportFileType","TS",'<spring:message code="report.submenu.time.sheet"/>');
	}, null, false);

	//--------------POPULATE DEFAULT VALUES-----------------------------------------------------------
	window.onload = populateDefaultValuesTimeSheetReport('${DEFAULT_VALUES_JSON}',document.getElementById('reportTimeSheetForm_reportLanguage').value,true);
	
</script>	
</c:if>

	
