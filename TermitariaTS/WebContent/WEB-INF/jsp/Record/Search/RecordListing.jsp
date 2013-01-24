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
<%@ include file="../../Messages.jsp" %>

<c:set var="displayCheckAll" value="false"/>
	
<c:forEach var="recordBean" items="${SEARCH_RESULTS}"> 
	<c:if test="${recordBean.projectDetails.status != 2 && recordBean.projectDetails.status != 3}">
		<c:set var="displayCheckAll" value="true"/>
	</c:if>
</c:forEach>

<c:set var="showBillable" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${(bean.projectName ne null) && (bean.projectName != '')}">
			<c:set var="showBillable" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">
	<c:forEach var="bean" items="${SEARCH_RESULTS}">
		<c:if test="${USER_ID eq bean.projectManagerId}">
			<c:set var="showBillable" value="true"/>
		</c:if>
	</c:forEach>
</security:authorize>

<c:set var="showWorkHours" value="false"/>
<c:forEach var="bean" items="${SEARCH_RESULTS}">
	<c:if test="${bean.startTime ne null}">
		<c:set var="showWorkHours" value="true"/>
	</c:if>
</c:forEach>

<c:set var="showOvertimeWorkHours" value="false"/>
<c:forEach var="bean" items="${SEARCH_RESULTS}">
	<c:if test="${bean.overTimeStartTime ne null}">
		<c:set var="showOvertimeWorkHours" value="true"/>
	</c:if>
</c:forEach>

<c:choose>
	<c:when test="${!(empty SEARCH_RESULTS)}">
		<fieldset style="width: 1000px;">
			<legend><spring:message code="record.title.listing" /></legend>
			<form:form id="paginationForm" name="paginationForm" commandName="searchRecordBean">				
				<c:if test="${displayCheckAll eq true}">
					<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
				</c:if>
					
					<table class="list_results" style="width:1000px" id="listingTable">
						<tr class="list_results_header">
																											
							<c:if test="${displayCheckAll eq true}">
								<th style="width: 5px;" class="checkBox">
									<input type="checkbox" id="selectAllRecords">
								</th>
							</c:if>
							
							<!-- 
							<th style="width: 50px">
								<a class="sortLink" id="recordIdSortLink" href="#">
									<spring:message code="record.id"/>
								</a>
							</th>
							 -->
							 
							<th style="width: 250px">
								<spring:message code="record.owner"/>
							</th>
							
							<th style="width: 150px">
								<a class="sortLink" id="activitySortLink" href="#">
									<spring:message code="record.activity"/>
								</a>
							</th>
							<th style="width: 150px">
								<spring:message code="record.title"/>
							</th>
							
							<!-- 
							<th style="width: 250px">
								<spring:message code="record.project.slash.org"/>
							</th>
							 
							
							<c:if test="${showBillable eq true}">
								<th style="width: 80px">
									<spring:message code="record.billable"/>
								</th>
							</c:if>
							-->
								
							
							<c:if test="${showWorkHours eq true}">													
								<th style="width: 100px">
									<spring:message code="record.time.hours"/>
								</th>
							</c:if>
								
							<c:if test="${showWorkHours eq true}">													
								<th style="width: 240px">
									<spring:message code="record.range.work.hours"/>
								</th>
							</c:if>
							
							<c:if test="${showOvertimeWorkHours eq true}">	
								<th style="width: 220px">
									<spring:message code="record.range.overtime.hours"/>
								</th>
							</c:if>
							
							<th style="width: 80px">
								<spring:message code="actions"/>
							</th>
						</tr>
						<c:set var="i" value="0"/>							
						<c:set var="cssClass" value=""/>
						
						<c:forEach var="recordBean" items="${SEARCH_RESULTS}">
							<c:set var="i" value="${i + 1}"/>
							<c:choose>
								<c:when test="${i % 2 == 0}">
									<c:set var="cssClass" value="even_row"/>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="odd_row"/>
								</c:otherwise>
							</c:choose>
							
							<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.recordId, document.paginationForm.selectAllRecords)">	
																																	
							<c:choose>
								<c:when test="${recordBean.projectDetails.status != 2 && recordBean.projectDetails.status != 3}">									
									<td class="checkBox">
										<input id="recordId" name="recordId" type="checkbox" onClick="cancelCheckEvent(this)" value="${recordBean.recordId}"/>
									</td>	
								</c:when>
								<c:otherwise>									
									<c:if test="${displayCheckAll eq true}">
										<td>
											<input id="recordId" type="hidden" type="checkbox"/>										
										</td>
									</c:if>									
								</c:otherwise>
							</c:choose>
							
							<c:choose>
								<c:when test="${(recordBean.projectName ne null) && (recordBean.projectName != '')}">																				
									<spring:message code="project" var="prj"/>										
									<c:choose>
										<c:when test="${recordBean.projectDetails.status == 1}">											
											<spring:message code="project.listing.opened" var="Type"/>		
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${recordBean.projectDetails.status == 2}">													
													<spring:message code="project.closed" var="Type"/>		
												</c:when>
												<c:otherwise>
													<c:if test="${recordBean.projectDetails.status == 3}">														
														<spring:message code="project.abandoned" var="Type"/>																
													</c:if>																														
												</c:otherwise>	
											</c:choose>														
										</c:otherwise>	
									</c:choose>																																				
								</c:when>
								<c:otherwise>									
									<spring:message code="record.for.organization" var="prj"/>	
									<c:set var="Type" value=""/>	
								</c:otherwise>	
							</c:choose>	
							
								<!--  
								<td align="center">
									${recordBean.recordId}
								</td>
								-->
																																																				
								<td align="center" >
									${recordBean.recordOwnerName}
									<div class="smallExplanatoryText">
									(${recordBean.recordId} <sup title="ID">[?]</sup> 
									<c:if test="${showBillable eq true}">
										<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">									
												<c:if test="${(recordBean.projectName ne null) && (recordBean.projectName != '')}">								
													<c:if test="${recordBean.isBillable == true}">
														,<spring:message code="record.billable.abreviation"/>
														<sup title="<spring:message code="record.billable"/>">[?]</sup>
													</c:if>		
												</c:if>							
										</security:authorize>
										
										<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">																																													
											<c:if test="${USER_ID eq recordBean.projectManagerId}">																				
												<c:if test="${recordBean.isBillable == true}">
													<spring:message code="record.billable.abreviation"/>
													<sup title="<spring:message code="record.billable"/>">[?]</sup>
												</c:if>									
											</c:if>																																			
										</security:authorize>
									</c:if>
									)</div>
								</td>
								
								<td align="center" >
									${recordBean.activity.name}	
									<div class="smallExplanatoryText" style="font-size: 0.8em;">
									(${recordBean.projectName})	
									</div>
								</td>
								
								<td align="center" title="<spring:message code="record.description"/>: ${recordBean.description} &#013;<spring:message code="record.observation"/>: ${recordBean.observation}" >
									${recordBean.title}
								</td>
								
								<!--  
								<td title="${prj} ${Type}" align="center">								
									<c:choose>
										<c:when test="${(recordBean.projectName ne null) && (recordBean.projectName != '')}">										
											${recordBean.projectName}									
										</c:when>
										<c:otherwise>									
											<spring:message code="record.for.organization"/>									
										</c:otherwise>
									</c:choose>									
								</td>
								
														
								<c:if test="${showBillable eq true}">
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">									
										<td align="center" >
											<c:if test="${(recordBean.projectName ne null) && (recordBean.projectName != '')}">								
												<c:choose>
													<c:when test="${recordBean.isBillable == true}">
														<spring:message code="record.billable.yes"/>
													</c:when>
													<c:otherwise>
														<spring:message code="record.billable.no"/>
													</c:otherwise>
												</c:choose>			
											</c:if>
										</td>							
									</security:authorize>
									
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">																																													
										<c:choose>
											<c:when test="${USER_ID eq recordBean.projectManagerId}">	
												<td align="center" >																			
												<c:choose>
													<c:when test="${recordBean.isBillable == true}">
														<spring:message code="record.billable.yes"/>
													</c:when>
													<c:otherwise>
														<spring:message code="record.billable.no"/>
													</c:otherwise>
												</c:choose>		
												</td>									
											</c:when>	
											<c:otherwise>
												<td align="center" ></td>		
											</c:otherwise>	
										</c:choose>																																			
									</security:authorize>
								</c:if>																			
								-->	
								
								<td align="center">
									${recordBean.time}
								</td>
								
								<c:if test="${showWorkHours eq true}">
									<td align="center" >
										<c:if test="${recordBean.startTime ne null}">
											<span style="color:#568A02;"><fmt:formatDate value="${recordBean.startTime}" pattern="dd/MM/yyyy  HH:mm" type="both" dateStyle="long" timeStyle="short"/></span> 
											- <span style="color:#D20606;"><fmt:formatDate value="${recordBean.endTime}" pattern="dd/MM/yyyy  HH:mm" type="both" dateStyle="long" timeStyle="short"/></span>
										</c:if>
									</td>
								</c:if>
								
								
								
								<c:if test="${showOvertimeWorkHours eq true}">													
									<td align="center" >
										<c:if test="${recordBean.overTimeStartTime ne null}">
											<fmt:formatDate value="${recordBean.overTimeStartTime}" pattern="dd/MM/yyyy  HH:mm" type="both" dateStyle="long" timeStyle="short"/> - <fmt:formatDate value="${recordBean.overTimeEndTime}" type="both" dateStyle="long" timeStyle="short"/>
										</c:if>
									</td>
								</c:if>
									
																															
								<td>
									<table class="actionsTable" align="left" cellpadding="0" cellspacing="0">
										<tr>	
											<!-- edit the record only for those that have opened projects -->										
											<c:if test="${recordBean.projectDetails.status != 2 && recordBean.projectDetails.status != 3}">
												<td>
													<a href="#" onClick="getContentFromUrlDirect('RecordForm.htm?ACTION=GET&recordId=${recordBean.recordId}&BACK_URL=RecordSearch.htm', 'MAIN_CONTENT')" title="<spring:message code='record.edit'/>"><img src="images/buttons/action_edit.png"/></a>
												</td>
											</c:if>																				
											<td>
												<a href="#" onClick="displayInfoPanel('RecordView.htm?recordId=${recordBean.recordId}', '${recordBean.panelHeaderName}', '${recordBean.panelHeaderName}', '440px')" title="<spring:message code="record.view"/>"><img src="images/buttons/action_view.png"/></a>									
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</c:forEach>
					</table>
					<br/>
					
					<!-- Show pagination area only if there is more than 1 page -->
	
					<!-- THIS FIELD IS MANDATORY (NUMBER OF RESULTS/PAGE)-->
					<form:hidden path="resultsPerPage"/>
					<form:hidden path="currentPage"/>
					<form:hidden path="nbrOfPages"/>
					<form:hidden path="nbrOfResults"/>
					<form:hidden path="sortParam" />
					<form:hidden path="sortDirection" />
					
					<form:hidden path="teamMemberId"/>
					<form:hidden path="projectId"/>
					<form:hidden path="activityId"/>
					<spring:bind path="startDate">
						<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd/MM/yyyy HH:mm'/>"/>
					</spring:bind>
					<spring:bind path="endDate">
						<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd/MM/yyyy HH:mm'/>"/>
					</spring:bind>
					<form:hidden path="billable"/>
					<form:hidden path="firstName"/>
					<form:hidden path="lastName"/>	
					<form:hidden path="organizationId"/>
					<form:hidden path="id"/>										
					
					<c:import url="../../Pagination.jsp">
					<c:param name="currentPage" value="${searchRecordBean.currentPage}"/>
					<c:param name="nbrOfPages" value="${searchRecordBean.nbrOfPages}"/>
					<c:param name="firstPage" value="${searchRecordBean.lowerLimit}"/>
					<c:param name="lastPage" value="${searchRecordBean.upperLimit}"/>
					<c:param name="baseURL" value="RecordSearch.htm"/>
					<c:param name="divResults" value="RESULTS"/>
				</c:import>
			</form:form>
		</fieldset>
		<script>
			//----------------------------------------------------ORDER----------------------------------------------------------------
			YAHOO.util.Event.addListener("recordIdSortLink", "click", order, {url : 'RecordSearch.htm?action=sort', newSortParam : 'recordId', 
				oldSortParam : '${searchRecordBean.sortParam}', sortDirection : '${searchRecordBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
				withContext : true}, true);
			
			YAHOO.util.Event.addListener("activitySortLink", "click", order, {url : 'RecordSearch.htm?action=sort', newSortParam : 'activityName', 
				oldSortParam : '${searchRecordBean.sortParam}', sortDirection : '${searchRecordBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
				withContext : true}, true);

			<c:if test="${displayCheckAll eq true}">
				//------------------------------------------------------DELETE ALL -----------------------------------------------
				YAHOO.util.Event.addListener("deleteAll", "click", submitWithConfirmation, {url: 'RecordSearch.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="record.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'recordId',
				noSelectedItemsMessage: '<spring:message code="record.delete.selectRecordMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
	
				//------------------------------------------------------SELECT ALL-------------------------------------------------
				YAHOO.util.Event.addListener("selectAllRecords", "click", selectAllCheckbox, {checkboxId: 'selectAllRecords', ids: document.paginationForm.recordId, 
				table: document.getElementById('listingTable')}, true);
			</c:if>			

		</script>
		</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="record.no.results.message"/></div>
	</c:otherwise>
</c:choose>
