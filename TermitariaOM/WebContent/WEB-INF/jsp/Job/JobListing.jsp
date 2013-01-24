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

<%@ include file="../Messages.jsp" %>

<c:choose>
	<c:when test="${!(empty JOBS)}">
		<fieldset style="width: 600px;">
			<legend><spring:message code="job.title.listing" /></legend>
		
				<form:form id="paginationForm" name="paginationForm" commandName="searchJobBean">
					<c:if test="${ALL_ACTIONS eq true}">
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobDelete}">
							<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />	
						</security:authorize>						
					</c:if>	
				<table class="list_results" id="listingTable">
					
					<tr class="list_results_header">
						<c:if test="${ALL_ACTIONS eq true}">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobDelete}">
								<th style="width: 5px;" class="checkBox">
										<input type="checkbox" id="selectAllJobs">
								</th>
							</security:authorize>
						</c:if>
						<th align="center" style="width:160px;">
							<a href="#" class="sortLink" id="sortLinkId" >
								<spring:message code="job.name"/>
							</a>
						</th>								
						<th align="center" style="width:300px;">
							<spring:message code="job.description"/>
						</th>					
						<th align="center" style="width:80px;">
							<spring:message code="job.status"/>
						</th>						
						<c:if test="${ALL_ACTIONS eq true}">
							<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobUpdate}, ${PERMISSION_CONSTANT.OM_JobChangeStatus}">
								<th align="center" style="width:120px;">
									<spring:message code="job.actions"/>
								</th>
							</security:authorize>
						</c:if>	
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					
					<c:forEach var="job" items="${JOBS}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
						<tr class="${cssClass}" id='${i}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.jobId, document.paginationForm.selectAllJobs)">
							<c:if test="${ALL_ACTIONS eq true}">
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobDelete}">
									<td class="checkBox">
										<input id="jobId" name="jobId" type="checkbox" onClick="cancelCheckEvent(this)" value="${job.jobId}"/></td>
									</td>
								</security:authorize>
							</c:if>
							<td align="center">
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobUpdate}">								
									<a href="#" onClick="submitForm('Job.htm?action=get&jobId=${job.jobId}', 'searchForm','MAIN_CONTENT');" title="<spring:message code="job.edit" />">
										${job.name}
									</a>
								</security:authorize>
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_JobUpdate}">
									${job.name}
								</security:authorize>	
							</td>						
							<c:choose>
								<c:when test="${fn:length(job.description) ne 0 and job.tokenizedDescription ne job.truncatedTokenizedDescription}">
									<td align="center" onmouseover="Tip('${job.tokenizedDescription}' , BGCOLOR, getCSSRule('.toolTipBody').style.backgroundColor, FONTWEIGHT, getCSSRule('.toolTipBody').style.fontWeight, FONTCOLOR, getCSSRule('.toolTipBody').style.color, BORDERCOLOR, getCSSRule('.toolTipBody').style.borderColor, FADEIN, 800, FADEOUT, 800);" onmouseout="UnTip();">
										${job.truncatedTokenizedDescription}
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										${job.truncatedTokenizedDescription}
									</td>
								</c:otherwise>
							</c:choose>												
							<td align="center">
								<c:choose>
									<c:when test="${job.status == 1}">
										<spring:message code="job.enabled" />
									</c:when>
									<c:otherwise>
										<spring:message code="job.disabled" />
									</c:otherwise>
								</c:choose>
							</td>							
							<c:if test="${ALL_ACTIONS eq true}">		
							<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobUpdate}, ${PERMISSION_CONSTANT.OM_JobChangeStatus}">
								<td align="center">								
									<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
										<tr>											
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobUpdate}">		
												<td>						
													<a href="#" onClick="submitForm('Job.htm?action=get&jobId=${job.jobId}', 'searchForm','MAIN_CONTENT');" title="<spring:message code="job.edit" />">
														<img src="images/buttons/action_edit.png"/>
													</a>
												</td>																	
											</security:authorize>											
											<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobChangeStatus}">									
												<td>
													<c:choose>
														<c:when test="${job.status == 1}">
															<a href="#" onClick="submitJobFormWithConfirmation('SearchJob.htm?action=updatestatus&id=${job.jobId}', 'paginationForm','RESULTS' ,'<spring:message code="job.disable.one.confirmationMessage"/>', '${job.panelConfirmationName}', '<spring:message code="confirm.deactivate"/>');" title="<spring:message code="job.disable" />">
															<img src="images/buttons/action_disable.png"/>
															</a> 
														</c:when>
														<c:otherwise>
															<a href="#" onClick="submitJobFormWithConfirmation('SearchJob.htm?action=updatestatus&id=${job.jobId}', 'paginationForm','RESULTS', '<spring:message code="job.enable.one.confirmationMessage"/>' , '${job.panelConfirmationName}', '<spring:message code="confirm.activate"/>');" title="<spring:message code="job.enable" />">							
															<img src="images/buttons/action_enable.png"/>
															</a>
														</c:otherwise>
													</c:choose>
												</td>	
																		
											</security:authorize>
										</tr>
									</table>
								</td>
							</security:authorize>													
							</c:if>	
						</tr>					
					</c:forEach>
				</table>
				<br/>
				
				<!-- THIS FIELD IS MANDATORY (NUMBER OF RESULTS/PAGE)-->
				<form:hidden path="resultsPerPage"/>
				<form:hidden path="currentPage"/>
				<form:hidden path="nbrOfPages"/>
				<form:hidden path="nbrOfResults"/>
				<form:hidden path="sortParam" />	
				<form:hidden path="sortDirection" />
				<form:hidden path="name" />
				<form:hidden path="organisationId" />
				<form:hidden path="status" />
				<form:hidden path="branch"/>
			
				<c:import url="../Pagination.jsp">
				   <c:param name="currentPage" value="${searchJobBean.currentPage}"/>
				   <c:param name="nbrOfPages" value="${searchJobBean.nbrOfPages}"/>
				   <c:param name="firstPage" value="${searchJobBean.lowerLimit}"/>
				   <c:param name="lastPage" value="${searchJobBean.upperLimit}"/>
				   <c:param name="baseURL" value="SearchJob.htm"/>
				   <c:param name="divResults" value="RESULTS"/>
				</c:import>
			
				</form:form>
		</fieldset>	
		<script>
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobDelete}">
				YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'SearchJob.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="job.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'jobId',
				noSelectedItemsMessage: '<spring:message code="job.delete.selectJobMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
			</security:authorize>
		</script>
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="job.no.results.message"/></div>
	</c:otherwise>
</c:choose>

<script>
	//------------------------------------------------------ORDER BY NAME---------------------------------------------
	
	YAHOO.util.Event.addListener("sortLinkId", "click", order, { url : 'SearchJob.htm?action=sort', newSortParam : 'name', 
	oldSortParam : '${searchJobBean.sortParam}', sortDirection : '${searchJobBean.sortDirection}', formId : 'searchForm', 
	divId : 'RESULTS', withContext : true}, true);
	
	//------------------------------------------------------SELECT ALL-------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobDelete}">
		YAHOO.util.Event.addListener("selectAllJobs", "click", selectAllCheckbox, {checkboxId: 'selectAllJobs', ids: document.paginationForm.jobId, 
			table: document.getElementById('listingTable')}, true);	
	</security:authorize>
</script>
