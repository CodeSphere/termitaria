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

<c:choose>
	<c:when test="${!(empty SEARCH_RESULTS)}">
		<fieldset style="width: 840px;">
			<legend><spring:message code="audit.om.title.listing" /></legend>
			<form:form id="paginationForm" name="paginationForm" commandName="searchAuditOmBean">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Delete}">
					<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />	
				</security:authorize>
				
				<table class="list_results" style="width:840px" id="listingTable">
					<tr class="list_results_header">
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Delete}">
							<th style="width: 5px;" class="checkBox">
								<input type="checkbox" id="selectAllAudits">
							</th>
						</security:authorize>
						<th style="width:200px;">						
							<spring:message code="audit.om.date" />							
						</th>
						<th style="width:160px;">						
							<spring:message code="audit.om.user" />							
						</th>
						<th style="width:160px;">
							<spring:message code="audit.om.event" />
						</th>
						<th style="width:300px;">
							<spring:message code="audit.om.message" />
						</th>											
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					<c:forEach var="auditBean" items="${SEARCH_RESULTS}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
						<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Delete}">				
								<td class="checkBox" onClick="selectUnSelectRow(this, document.paginationForm.auditId, document.paginationForm.selectAllAudits)">
									<input id="auditId" name="auditId" type="checkbox" onClick="cancelCheckEvent(this)" value="${auditBean.auditId}"/>
								</td>
							</security:authorize>
							<td>
								<fmt:formatDate value="${auditBean.date}" pattern="dd-MM-yyyy HH:mm"/>
							</td>
							
							<td>
								${auditBean.firstName}&nbsp;${auditBean.lastName} 
							</td>
							
							<td>
								<spring:message code="${auditBean.event}"/>
							</td>			 				
							
							<td>
								<c:if test="${LOCALE eq 'ro'}">
									${auditBean.messageRO}
								</c:if>
								<c:if test="${LOCALE eq 'en'}">
									${auditBean.messageEN}
								</c:if>
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
				
				<form:hidden path="message"/>
				<form:hidden path="event" />
				<form:hidden path="moduleId"/>
				<form:hidden path="organisationId"/>
				<form:hidden path="personId"/>
							
				<spring:bind path="startDate">
					<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd-MM-yyyy HH:mm'/>"/>
				</spring:bind>
				<spring:bind path="endDate">
					<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd-MM-yyyy HH:mm'/>"/>
				</spring:bind>
			
			<c:import url="../../Pagination.jsp">
			   <c:param name="currentPage" value="${searchAuditOmBean.currentPage}"/>
			   <c:param name="nbrOfPages" value="${searchAuditOmBean.nbrOfPages}"/>
			   <c:param name="firstPage" value="${searchAuditOmBean.lowerLimit}"/>
			   <c:param name="lastPage" value="${searchAuditOmBean.upperLimit}"/>
			   <c:param name="baseURL" value="AuditOmSearch.htm"/>
			   <c:param name="divResults" value="RESULTS"/>
			</c:import>
			
			</form:form>
						
			</fieldset>
			<script>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Delete}">
					YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'AuditOmSearch.htm?action=DELETE_ALL', formId: "paginationForm",
					container: "RESULTS", confirmationMessage: '<spring:message code="audit.om.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'auditId',
					noSelectedItemsMessage: '<spring:message code="audit.om.delete.selectAuditMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);

					//------------------------------------------------------SELECT ALL-------------------------------------------------
					YAHOO.util.Event.addListener("selectAllAudits", "click", selectAllCheckbox, {checkboxId: 'selectAllAudits', ids: document.paginationForm.auditId, 
						table: document.getElementById('listingTable')}, true);
				</security:authorize>	
			</script>	
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="audit.om.no.results.message"/></div>
	</c:otherwise>
</c:choose>
		
