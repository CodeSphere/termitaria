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

<c:choose>
	<c:when test="${recordBean.recordId > 0}">
		<c:set var="cssClass" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>		
	</c:otherwise>
</c:choose>
<c:set var="showPrices" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}"> 
	<c:set var="showPrices" value="true"/>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">
	<c:if test="${IS_MANAGER}">
		<c:set var="showPrices" value="true"/>
	</c:if>
</security:authorize>
<c:set var="activityNameTabIndex" value="3"/>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->				
				<a href="#" id="back" onclick="ENTITY = getRecordData();check_add('${BACK_URL}', 'MAIN_CONTENT');"><spring:message code="back"/></a>
				<!--  <a href="#" id="back" ><spring:message code="back" /></a> -->
				<!--  <a href="#" id="addNewRecord" title="<spring:message code="record.submenu.add.title"/>"><spring:message code='add'/> </a> -->
				<a href="#" id="delete" class="${cssClass}" title="<spring:message code="record.submenu.delete.title"/>"><spring:message code='delete'/> </a>
				<!--  <a href="#" id="submenuSearch" title="<spring:message code="search"/>"><spring:message code="search"/></a> -->
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${recordBean.recordId > 0}">
		<span class="section_title"><img src="images/titles/RecordEdit.jpg"><spring:message code="record.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/RecordAdd.jpg"><spring:message code="record.add.section.title"/></span>	
	</c:otherwise>
</c:choose>



<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
	<br/>
</div>
<form:form commandName="recordBean" name="recordForm" id="recordForm" onsubmit="return false;">	
	<form:hidden path="recordId" id="recordId"/>
	<form:hidden path="userId" id="userId"/>
	<form:hidden path="organizationId"/>
	<form:hidden path="teamMemberDetailId"/>	
	
	
	
	<table>
		<tr>
			<td>
			<table cellpadding="3" class="tableAdd">
				<tr>
					<td class="tableAddSpacer">&nbsp;</td>
					<td></td>
				</tr>
				<tr>
					<td>
						<table>
							<!-- ====================================RECORD ID====================================================== -->
							<c:if test="${recordBean.recordId > 0}">
								<tr>
									<td class="labelTd" style="width:130px">
										<spring:message code="record.id"/>
									</td>
									<td>
										${recordBean.recordId}
									</td>
								</tr>
							</c:if>
							
							<tr>
								<!-- ====================================PROJECT====================================================== -->
								<td class="labelTd" style="width:130px">
									<spring:message code="record.project.slash.org"/>
									<c:if test="${recordBean.projectId eq null}">
										<span id="required">*</span>
									</c:if>
								</td>
								<td style="width:200px">
									<div title='<spring:message code="record.choose.project"/>' id="recordFormProjectsId">			
										
										
										<c:choose>
											<c:when test="${recordBean.projectId ne null}">
												<c:forEach var="project" items="${USER_PROJECTS}">
													<c:if test="${recordBean.projectId == project.projectId}">
														<form:hidden path="projectId" id="recordForm_project"/>
														${project.name}
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>	
										
										<form:select  path="projectId"  tabindex="1" id="recordForm_project" cssClass="formField required" disabled="${disabled}" onchange="manageProjectSelectInRecordForm(this.options[selectedIndex].value, 'recordForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', null, '${USER_NAME}', false, ${recordBean.recordId}); changeTitle(this.options[selectedIndex].title, 'recordFormProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'recordForm_project', 'recordForm_project1');">
											
											<option value="-2" class="chooseProject" title='<spring:message code="record.choose.project" />'><spring:message code="record.choose.project" /></option>
											<option value="-1" class="fromOrganization" title='<spring:message code="record.for.organization" />'><spring:message code="record.for.organization" /></option>
											<c:forEach var="project" items="${USER_PROJECTS}">
												
												<c:choose>
													<c:when test="${project.clientId != -1}">
														<c:set var="client" value="hasClient"/>	
														
														<c:choose>
															<c:when test="${project.status == 1}">
																<c:set var="prj" value="${project.name}"/>	
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
													<c:when test="${recordBean.projectId == project.projectId}">
														<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>
													</c:when>
													<c:otherwise>
														<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" >${project.name}</option>
													</c:otherwise>
												</c:choose>										
											</c:forEach>
										</form:select>
										</c:otherwise>
										</c:choose>
										<div id="project_message"></div>
									</div>
									
									<div id="recordForm_project1">
										<span class="chooseProject"></span>	
										<span class="fromOrganization"></span>																			
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
								<td>
								</td>
							</tr>
							
							<tr>
								<!-- ====================================ACTIVITY====================================================== -->
								
								<td class="labelTd">
									<spring:message code="record.activity"/>
									<c:if test="${recordBean.activityId eq null}">
										<span id="required">*</span>
									</c:if>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${recordBean.activityId > 0}">
											<span id="label_activity"></span>
											<form:hidden path="activityId" id="recordForm_activity"/>
										</c:when>
										<c:otherwise>
											<form:select  path="activityId" tabindex="${activityNameTabIndex}" id="recordForm_activity" cssClass="formField required" disabled="${disabled}">	
												<form:option value="-1" title='<spring:message code="record.choose.project" />'><spring:message code="record.choose.project" /></form:option>
											</form:select>
										</c:otherwise>
									</c:choose>
									
									<div id="activity_message"></div>
								</td>
								<td>
								</td>
							</tr>
																					
							<tr>
								<!-- ====================================WORK HOURS RECORD CHECK BOX=================================== -->
								<td class="labelTd">
									<spring:message code="record.work.hours"/>
								</td>
								<td>
									<form:checkbox path="workHoursRecord" tabindex="${activityNameTabIndex + 3}" id="recordForm_workHoursRecord" onclick="manageWorkHoursInputDisplay(this, 'workHours', null, '${TODAY_DATE}')"/>
									<div id="workHoursRecord_message"></div>
								</td>
								<td>
								</td>
							</tr>
							
							<tr>
								<!-- ====================================WORK HOURS START TIME=================================== -->
								<td class="labelTd workHours">
									<spring:message code="record.start.date"/> <span id="startTimeRequired" class="requiredIndicator">*</span>
								</td>
								<td class="workHours">
									<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${recordBean.startTime}" var="startTime"/>
									<input name="startTime" id="recordForm_startTime" value="${startTime}" readonly="readonly" class="formField" tabindex="${activityNameTabIndex + 5}" />
									<div id="startTime_message"></div> 
								</td>
								<td>
									<div id="startCalendar"></div>
									<div id="startTime"></div>
								</td>
							</tr>
							
							<tr>
								<!-- ====================================WORK HOURS END TIME=================================== -->
								<td class="labelTd workHours">
									<spring:message code="record.end.date"/> <span id="endTimeRequired" class="requiredIndicator">*</span>
								</td>
								<td class="workHours">
									<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${recordBean.endTime}" var="endTime"/>
									<input name="endTime" id="recordForm_endTime" readonly="readonly" value="${endTime}" class="formField" tabindex="${activityNameTabIndex + 7}" />
									<div id="endTime_message"></div> 
								</td>		
								<td>
									<div id="endCalendar"></div>
									<div id="endTime"></div>
								</td>								
							</tr>
							
							<!-- =========================================TIME======================================================== -->
							<tr>
								<td class="labelTd workHours" >
									<spring:message code="record.time"/> <span id="timeRequired" class="requiredIndicator">*</span>
								</td>
								<td class="workHours">
									<form:input id="recordForm_time" path="time" cssClass="formField validate-time" maxlength="7"/>	
									<span class="hint"><spring:message code="record.format.time"/><span class="hint-pointer">&nbsp;</span></span>			
									<div id="time_message"></div> 					
								</td>
								<td>
								</td>
							</tr>	
							
							<!-- ====================================BILLABLE====================================================== -->
							<c:set var="nextTabIndex" value="${activityNameTabIndex + 9}"/>
							<c:set var="billableDisplay" value="none"/>
							<c:choose>
								<c:when test="recordBean.recordId > 0">
									<c:if test="${SHOW_BILLABLE eq true}">
										<c:set var="nextTabIndex" value="${nextTabIndex + 4}"/>
										<c:set var="billableDisplay" value="table-cell"/>
									</c:if>
								</c:when>
						 	</c:choose>
							
							<tr>
								<td class="labelTd billable workHours" style="display:${billableDisplay}">
									<spring:message code="record.billable"/>
								</td>
								<td class="billable workHours" style="display:${billableDisplay}">
									<font class="labelTd"><spring:message code="record.billable.yes" /></font>
										<form:radiobutton path="billable" value="Y" tabindex="${activityNameTabIndex + 9}" id="recordForm_billable_yes"/> 
									<font class="labelTd"><spring:message code="record.billable.no" /></font>
										<form:radiobutton path="billable" value="N" tabindex="${activityNameTabIndex + 10}" id="recordForm_billable_no"/> 
								</td>
								<td class="billable" style="display:${billableDisplay}">
								</td>
							</tr>
							
							<c:if test="${showPrices eq true}">
								<c:if test="${recordBean.recordId != -1 && recordBean.workHoursRecord eq true}">
									<c:choose>
										<c:when test="${recordBean.projectDetails != null && recordBean.projectId != -1}">
											<!-- ====================================TEAM MEMBER COST PRICE========================================================= -->
											<tr>
												<td class="labelTd workHours">
													<spring:message code="record.cost.price.team.member"/>
												</td>			
												<td class="workHours">								
													${COST_PRICE_TEAM_MEMBER}																									
												</td>
											</tr>
											<!-- ====================================TEAM MEMBER BILLING PRICE========================================================= -->
											<tr>
												<td class="labelTd workHours">
													<spring:message code="record.billing.price.team.member"/>
												</td>			
												<td class="workHours">								
													${BILLING_PRICE_TEAM_MEMBER}																									
												</td>
											</tr>		
										</c:when>
										<c:otherwise>
											<!-- ====================================PERSON COST PRICE========================================================= -->
											<tr>
												<td class="labelTd workHours">
													<spring:message code="record.cost.price.person"/>
												</td>			
												<td class="workHours">								
													${COST_PRICE_PERSON}																									
												</td>
											</tr>	
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:if>
							
							<tr>
								<td class="tableAddSpacer">&nbsp;</td>
							</tr>
							
							<tr>
								<!-- ====================================OVERTIME RECORD CHECK BOX=================================== -->
								<td class="labelTd">
									<spring:message code="record.overtime"/>
								</td>
								<td>
									<form:checkbox path="overtimeRecord" tabindex="${activityNameTabIndex + 4}" id="recordForm_overtimeRecord" onclick="manageOverTimeInputDisplay(this, 'overTime', null, '${TODAY_DATE}')" />
									<div id="overtimeRecord_message"></div>
								</td>
								<td>
								</td>
							</tr>
							
							<tr>
								<!-- ====================================OVERTIME START TIME=================================== -->
								<td class="labelTd overTime labelTdInactive">
									<spring:message code="record.start.date"/><span id="overTimeStartTimeRequired" class="requiredIndicator" style="display:none">*</span>
								</td>
								<td class="overTime inactive">
									<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${recordBean.overTimeStartTime}" var="overTimeStartTime"/>
									<input name="overTimeStartTime" id="recordForm_overTimeStartTime" value="${overTimeStartTime}" readonly="readonly" class="formField" tabindex="${activityNameTabIndex + 6}" /> 
									<div id="overTimeStartTime_message"></div> 
								</td>
								<td>
									<div id="overTimeStartCalendar"></div>
									<div id="overTimeStartTime"></div>
								</td>
							</tr>
							
							<tr>			
								<!-- ====================================OVERTIME END TIME=================================== -->
								<td class="labelTd overTime labelTdInactive">
									<spring:message code="record.end.date"/><span id="overTimeEndTimeRequired" class="requiredIndicator" style="display:none">*</span>
								</td>
								<td class="overTime inactive">
									<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${recordBean.overTimeEndTime}" var="overTimeEndTime"/>
									<input name="overTimeEndTime" id="recordForm_overTimeEndTime" value="${overTimeEndTime}" readonly="readonly" class="formField" tabindex="${activityNameTabIndex + 8}" /> 
									<div id="overTimeEndTime_message"></div> 
								</td>
								<td>
									<div id="overTimeEndCalendar"></div>
									<div id="overTimeEndTime"></div>
								</td>
							</tr>
							
							<!-- =========================================OVERTIME TIME======================================================== -->
							<tr>
								<td class="labelTd overTime labelTdInactive">
									<spring:message code="record.overtime.time"/><span id="overTimeTimeRequired" class="requiredIndicator" style="display:none">*</span>
								</td>
								<td class="overTime inactive">
									<form:input id="recordForm_overtimeTime" path="overTimeTime" cssClass="formField validate-time" maxlength="7"/>
									<span class="hint"><spring:message code="record.format.time"/><span class="hint-pointer">&nbsp;</span></span>
									<div id="overTimeTime_message"></div> 								
								</td>
								<td>
								</td>
							</tr>	
							
							<tr>				
								<td class="labelTd billable overTime labelTdInactive" style="display:${billableDisplay}">
									<spring:message code="record.billable.overtime"/>
								</td>
								<td class="billable overTime" style="display:${billableDisplay}">
									<font class="labelTd"><spring:message code="record.billable.yes" /></font>
										<form:radiobutton path="overTimeBillable" value="Y" tabindex="${activityNameTabIndex + 11}" id="recordForm_overTime_billable_yes"/> 
									<font class="labelTd"><spring:message code="record.billable.no" /></font>
										<form:radiobutton path="overTimeBillable" value="N" tabindex="${activityNameTabIndex + 12}" id="recordForm_overTime_billable_no"/> 
								</td>
								<td class="billable" style="display:${billableDisplay}">
								</td>
							</tr>
							<c:if test="${showPrices eq true}">
								<c:if test="${recordBean.recordId != -1 && recordBean.overtimeRecord eq true}">
									<c:choose>
										<c:when test="${recordBean.projectDetails != null && recordBean.projectId != -1}">
											<!-- ====================================TEAM MEMBER OVERTIME COST PRICE========================================================= -->
											<tr>
												<td class="labelTd overTime">
													<spring:message code="record.overtime.cost.price.team.member"/>
												</td>			
												<td class="overTime">								
													${OVERTIME_COST_PRICE_TEAM_MEMBER}																									
												</td>
											</tr>	
											<!-- ====================================TEAM MEMBER OVERTIME BILLING PRICE========================================================= -->
											<tr>
												<td class="labelTd overTime">
													<spring:message code="record.overtime.billing.price.team.member"/>
												</td>			
												<td class="overTime">								
													${OVERTIME_BILLING_PRICE_TEAM_MEMBER}																									
												</td>
											</tr>	
										</c:when>
										<c:otherwise>
											<!-- ====================================PERSON OVERTIME COST PRICE========================================================= -->
											<tr>
												<td class="labelTd overTime">
													<spring:message code="record.overtime.cost.price.person"/>
												</td>			
												<td class="overTime">								
													${OVERTIME_COST_PRICE_PERSON}																									
												</td>
											</tr>	
										</c:otherwise>
									</c:choose>	
								</c:if>
							</c:if>
													
						</table>
					</td>
					
					<td>
						<table cellpadding="3">
							
							
							
							<tr>
								<!-- ====================================TITLE=================================== -->
								<td></td>
								<td class="labelTd" style="width:100px">
									<spring:message code="record.title"/> <span id="titleRequired" class="requiredIndicator">*</span>
								</td>
								<td style="width:250px">
									<form:input path="title" id="recordForm_title" cssClass="formField" tabindex="${activityNameTabIndex  }" onkeypress="return restrictSpecialCharactersForObs(event);"/>
									<div id="title_message"></div>
									<div class="remainingCharactersRecordDesc" style="display:none" >
										<div id="showRemainingCharactersTitle"/>
									</div>
									
								</td>								
							</tr>
							
							<tr>
								<!-- ====================================USER====================================================== -->
								<td style="height:45px"></td>
								<td class="labelTd recordUser" style="display:none;width:100px">
									<spring:message code="record.user"/><span id="required">*</span>
								</td>
								<td class="recordUser" style="display:none;width:220px">
									<div id="userAutoComplete" class="autoComplete">
										<form:input id="recordForm_user" tabindex="2" cssClass="formField required" path="recordOwnerName"/>
										<div id="userAutoCompleteContainer"></div>
									</div>									
									<div id="user_message"></div>
								</td>								
							</tr>
							
							<tr>
								<!-- ====================================DESCRIPTION=================================== -->
								<td></td>
								<td class="labelTd" style="width:100px">
									<spring:message code="record.description"/> <span id="descriptionRequired" class="requiredIndicator">*</span>
								</td>
								<td style="width:250px">
									<form:textarea path="description" id="recordForm_description" cssClass="formField" tabindex="${activityNameTabIndex +1}" cols="40" rows="3" onkeypress="return restrictSpecialCharactersForObs(event);"/>
									<div id="description_message"></div>
									<div class="remainingCharactersRecordDesc" style="display:none" >
										<div id="showRemainingCharactersDescription"/>
									</div>
									
								</td>								
							</tr>
																				
							<tr>
								<!-- ====================================OBSERVATION=================================== -->
								<td></td>
								<td class="labelTd" style="width:100px">
									<spring:message code="record.observation"/>
								</td>
								<td style="width:250px">
									<form:textarea path="observation" id="recordForm_observation" cssClass="formField" tabindex="${activityNameTabIndex + 2}" cols="40" rows="3" onkeypress="return restrictSpecialCharactersForObs(event);"/>
									<div class="remainingCharactersRecordObs" style="display:none" >
										<div id="showRemainingCharactersObservation"/>
									</div>
								</td>								
							</tr>
							
							
							
							
							
							<c:if test="${showPrices eq true}">
								<c:if test="${recordBean.recordId != -1}">
									<tr>
										<td></td>
										<td colspan="2">
											<fieldset style="width:350px">
												<legend>
													<spring:message code="record.activity.price.details"/>
												</legend>
												<table style="width:100%">
													<c:if test="${recordBean.projectId > 0 && SHOW_BILLABLE eq true}">
														<!-- ====================================ACTIVITY BILLABLE========================================================= -->
														<tr>
															<td class="labelTd" style="width:120px">
																<spring:message code="record.activity.billable"/>
															</td>			
															<td>								
																<spring:message code="record.billable.${ACTIVITY.billable}"/>																									
															</td>
														</tr>
													</c:if>
													<!-- ====================================ACTIVITY COST PRICE========================================================= -->
													<tr>
														<td class="labelTd">
															<spring:message code="record.cost.price.activity"/>
														</td>			
														<td>													
															${COST_PRICE_ACTIVITY}																																												
														</td>
													</tr>
													<c:if test="${recordBean.projectDetails != null && recordBean.projectId != -1}">
														<!-- ====================================ACTIVITY BILLING PRICE========================================================= -->								
														<tr>
															<td class="labelTd">
																<spring:message code="record.billing.price.activity"/>
															</td>			
															<td>								
																${BILLING_PRICE_ACTIVITY}																									
															</td>
														</tr>
													</c:if>
												</table>
											</fieldset>
										</td>
									</tr>
								</c:if>																							
							</c:if>											
						</table>						
					</td>
				</tr>
					
				<tr>
					<td colspan="5" class="formActions" align="right">
						<input type="button" class="button" id="save" tabindex="${nextTabIndex}" value="<spring:message code="save"/>"/>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">		
							<input type="button" class="button" id="activityPanel" tabindex="${nextTabIndex + 1}" value="<spring:message code="record.add.activity"/>"/>
						</security:authorize>	
					
						<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">		
							<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
								<input type="button" class="button" id="activityPanel" tabindex="${nextTabIndex + 1}" value="<spring:message code="record.add.activity"/>"/>
							</c:if>
						</security:authorize>
						<input type="button" class="button" id="cancel" tabindex="${nextTabIndex + 2}" value="<spring:message code="cancel"/>" onclick="ENTITY = getRecordData();check_add('${BACK_URL}', 'MAIN_CONTENT');"/>						
					</td>
				</tr>			
			</table>
			</td>							
		</tr>
	</table>
</form:form>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->
	
<script type="text/javascript">	


	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	

	<c:if test="${recordBean.activityId > 0 && recordBean.projectId > 0}">
		manageProjectSelectInRecordForm("${recordBean.projectId}", 'recordForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', "${recordBean.activityId}", '${USER_NAME}', false, '${recordBean.recordId}'); 
	</c:if>

	
	//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
	document.recordForm.recordForm_project.focus();
	<c:choose>
		<c:when test="${recordBean.activityId > 0 && recordBean.projectId > 0}">
			var submitObject = new ObjSubmit("RecordForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "recordForm", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("save", "click", submitSaveRecordWithoutActivityAndProjectValidation, submitObject, true);
		</c:when>
		<c:otherwise>
			var submitObject = new ObjSubmit("RecordForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "recordForm", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("save", "click", submitSaveRecord, submitObject, true);
		</c:otherwise>
	</c:choose>

	//----------------------------------------------- DELETE RECORD ------------------------------------------------
	<c:if test="${recordBean.recordId > 0}">
		submitObject = new ObjSubmitWithConfirmation("RecordSearch.htm?ACTION=DELETE_FROM_RECORD_FORM&recordId=${recordBean.recordId}", "", "MAIN_CONTENT", '<spring:message code="record.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
		YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);
	</c:if>	

	//----------------------------------------------- ADD NEW RECORD -----------------------------------------------
	var	getObject = new ObjSubmit("RecordForm.htm?ACTION=ADD&BACK_URL=RecordSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("addNewRecord", "click", getContentFromUrl, getObject, true);

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS -----------------------
	
	//---------------------------------------- TITLE ---------------------------------------------
	//var validateTitleField = new ObjFieldRemaining("recordForm_title", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersTitle');
	//YAHOO.util.Event.addListener("recordForm_title", "focus", showInfoOnClickInField, validateTitleField, true);
	//YAHOO.util.Event.addListener("recordForm_title", "blur", hideInfoOnBlurInField, validateTitleField, true);
	//YAHOO.util.Event.addListener("recordForm_title", "keyup", showInfoOnKeyUpInField, validateTitleField, true);	

	//------------------------------------- DESCRIPTION ------------------------------------------
	var validateDescriptionField = new ObjFieldRemaining("recordForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersDescription');
	YAHOO.util.Event.addListener("recordForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("recordForm_description", "blur", hideInfoOnBlurInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("recordForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);		
	
	//------------------------------------- OBSERVATION ------------------------------------------
	var validateObservationField = new ObjFieldRemaining("recordForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("recordForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("recordForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("recordForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);	

	
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS -----------------------
	
	//----------------------------------- ENTER EVENT ON ALL FIELDS---------------------------------------
	var keyListner = null; 
	var submitObject = new ObjSubmit("RecordForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "recordForm", "MAIN_CONTENT");
	
	keyListener = new YAHOO.util.KeyListener("recordForm_user", { keys:13 },  { fn:submitSaveRecord, scope: submitObject, correctScope:true}, null ); 
	keyListener.enable();
	
	//----------------------------------- SELECT THE PROJECT AND INSTANTIATE THE USER AUTO COMPLETE AND ACTIVITY SELECT WHEN EDITING ------------------------------
	
	<c:choose>
		<c:when test="${recordBean.recordId > 0}">
			<c:choose>
				
				<c:when test="${recordBean.projectId != null and recordBean.projectId != -1}">
					manageProjectSelectInRecordForm('${recordBean.projectId}', 'recordForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', '${recordBean.activityId}', '${USER_NAME}', true, ${recordBean.recordId});
				</c:when>
				<c:otherwise>
					manageProjectSelectInRecordForm(-1, 'recordForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', '${recordBean.activityId}', '${USER_NAME}', true, ${recordBean.recordId});
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:if test="${SAME_ACTIVITY eq true}">			
				manageProjectSelectInRecordForm('${recordBean.projectId}', 'recordForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', '${recordBean.activityId}', '${USER_NAME}', true, ${recordBean.recordId});
			</c:if>			
		</c:otherwise>
	</c:choose>	

	//-------------------- FIRST TIME CHECK/UNCHECK WORK HOURS AND OVERTIME START DATE AND END DATE CALENDAR------
	<c:choose>
		<c:when test="${recordBean.recordId > 0}">
			<c:choose>
				<c:when test="${recordBean.workHoursRecord}">					
					manageWorkHoursInputDisplay(document.getElementById('recordForm_workHoursRecord'), 'workHours', true, '${TODAY_DATE}');
				</c:when>
				<c:otherwise>
					manageWorkHoursInputDisplay(document.getElementById('recordForm_workHoursRecord'), 'workHours', false, '${TODAY_DATE}');
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${recordBean.overtimeRecord}">
					manageOverTimeInputDisplay(document.getElementById('recordForm_overtimeRecord'), 'overTime', true, '${TODAY_DATE}');
				</c:when>
				<c:otherwise>
					manageOverTimeInputDisplay(document.getElementById('recordForm_overtimeRecord'), 'overTime', false, '${TODAY_DATE}');
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>			
			<c:choose>
				<c:when test="${SAME_ACTIVITY eq true}">				
					manageWorkHoursInputDisplay(document.getElementById('recordForm_workHoursRecord'), 'workHours', document.getElementById('recordForm_workHoursRecord').checked, '${TODAY_DATE}');
					manageOverTimeInputDisplay(document.getElementById('recordForm_overtimeRecord'), 'overTime', document.getElementById('recordForm_overtimeRecord').checked, '${TODAY_DATE}');								
				</c:when>
				<c:otherwise>
					manageWorkHoursInputDisplay(document.getElementById('recordForm_workHoursRecord'), 'workHours', true, '${TODAY_DATE}');
					manageOverTimeInputDisplay(document.getElementById('recordForm_overtimeRecord'), 'overTime', false, '${TODAY_DATE}');
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>	

	//-------------------- SEARCH RECORD --------------------------------------------------------------
	var getObject = new ObjSubmit("RecordSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);	

	//-------------------- RECORD ADD ACTIVITY --------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">		
		var submitActivityPanel = ObjSubmitActivityPanel('Activity.htm?ACTION=ADD&GET_FROM_PANEL=true', "<spring:message code="record.add.activity"/>");		
		YAHOO.util.Event.addListener("activityPanel", "click", displayAddActivityPanel, submitActivityPanel, true);
	</security:authorize>	

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">		
		<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
			var submitActivityPanel = ObjSubmitActivityPanel('Activity.htm?ACTION=ADD&GET_FROM_PANEL=true', "<spring:message code="record.add.activity"/>");		
			YAHOO.util.Event.addListener("activityPanel", "click", displayAddActivityPanel, submitActivityPanel, true);
		</c:if>
	</security:authorize>
	

	//-------------------- CHECK/UNCHECK BILLABLE -----------------------------------------------------
	<c:if test="${recordBean.recordId > 0}">
		manageCheckBillableForRecordBean("${recordBean.billable}", "${recordBean.overTimeBillable}");
	</c:if>

	manageWorkHoursPeriod("recordForm_startTime", "recordForm_endTime", "recordForm_time");

	var validateTimeField = new ObjFieldTime("recordForm_time", 'recordForm_startTime', 'recordForm_endTime');	
	YAHOO.util.Event.addListener("recordForm_time", "keyup", hideOnBlurInFieldTime,validateTimeField, true);		

	<c:if test="${recordBean.overtimeRecord eq true}">		
		manageOvertimeWorkHoursPeriod("recordForm_overTimeStartTime", "recordForm_overTimeEndTime", "recordForm_overtimeTime");	
	</c:if>

	var validateOverTimeField = new ObjFieldTime("recordForm_overtimeTime", 'recordForm_overTimeStartTime', 'recordForm_overTimeEndTime');	
	YAHOO.util.Event.addListener("recordForm_overtimeTime", "keyup", hideOnBlurInFieldTime,validateOverTimeField, true);
	
	<c:if test="${recordBean.projectId <= 0}">
		selectOption("recordForm_project", "recordForm_project1");
	</c:if>
	
	//-------------------- OLD RECORD ENTITY ----------------------------------------------------------
		
	OLD_ENTITY = getRecordData();

	<c:if test="${recordBean.activityId > 0}">
	OLD_ENTITY.activityId = ${recordBean.activityId};
	</c:if>
	
</script>
