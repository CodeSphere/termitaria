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
	<c:when test="${!(empty SEARCH_RESULTS)}">	
		<fieldset style="width: 700px;">
			<legend><spring:message code="person.title.listing" /></legend>
				<form:form id="paginationForm" name="paginationForm" commandName="searchPersonBean">		
					<input type="button" class="button" id="addAll" value="<spring:message code="add"/>" />																			
					<table class="list_results" style="width:700px" id="listingTable">
						<tr class="list_results_header">	
							<th style="width: 5px;" class="checkBox">
								<input type="checkbox" id="selectAllPersons">
							</th>
																																							
							<th style="width:200px;">
								<a class="sortLink" id="reportSortLastNameLinkId" href="#">
									<spring:message code="person.lastName"/>										
								</a>
							</th>
							
							<th style="width:200px;">	
								<a class="sortLink" id="reportSortFirstNameLinkId" href="#">							
									<spring:message code="person.firstName"/>	
								</a>						
							</th>
							
							<th style="width: 300px;">								
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">
									<c:choose>
										<c:when test="${searchPersonBean.projectId == 0}">
											<a class="sortLink" id="reportSortProjectNameLinkId" href="#">
												<spring:message code="person.project.slash.org"/>
											</a>
										</c:when>
										<c:otherwise>
											<spring:message code="person.project.slash.org"/>
										</c:otherwise>	
									</c:choose>
								</security:authorize>
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">
									<c:choose>
										<c:when test="${searchPersonBean.projectId == 0}">
											<a class="sortLink" id="sortProjectNameLinkId" href="#">
												<spring:message code="person.project"/>
											</a>
										</c:when>
										<c:otherwise>
											<spring:message code="person.project"/>
										</c:otherwise>	
									</c:choose>
								</security:authorize>
							</th>			
						</tr>
						
						<c:set var="i" value="0"/>
						<c:set var="membersNr" value="0"/>						
						<c:set var="personsNr" value="0"/>
						<c:set var="i" value="0"/>
						<c:set var="cssClass" value=""/>
						
						<c:forEach var="bean" items="${SEARCH_RESULTS}">			
							<c:set var="i" value="${i + 1}"/>												
							<c:choose>
								<c:when test="${i % 2 == 0}">
									<c:set var="cssClass" value="even_row"/>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="odd_row"/>
								</c:otherwise>
							</c:choose>
							
								<c:choose>
									<c:when test="${bean.memberId > 0}">
										<c:set var="membersNr" value="${membersNr + 1}"/>	
										<tr class="${cssClass}" id='${membersNr}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.memberId, document.paginationForm.selectAllPersons)">
											<td class="checkBox">
												<input id="personOrMemberId" name="memberId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.memberId}"/>
											</td>
									</c:when>
									<c:otherwise>
										<c:set var="personsNr" value="${personsNr + 1}"/>	
										<tr class="${cssClass}" id='${personsNr}' onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" onClick="selectUnSelectRow(this, document.paginationForm.personId, document.paginationForm.selectAllPersons)">
											<td class="checkBox">
												<input id="personOrMemberId" name="personId" type="checkbox" onClick="cancelCheckEvent(this)" value="${bean.personId}"/>
											</td>
									</c:otherwise>
								</c:choose>
									
								<td>													
									${bean.lastName}									
								</td>		
								
								<td>													
									${bean.firstName}									
								</td>	
								
								<td>			
									<c:choose>
										<c:when test="${bean.project.name ne null}">
											 ${bean.project.name} 		
										</c:when>
										<c:otherwise>
											<spring:message code="project.from.organization"/>	
										</c:otherwise>
									</c:choose>																								
								</td>					
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
				 <!-- HIDDEN FIELDS - SEARCH CRITERION -->	
				 <form:hidden path="lastName" />		 
				 <form:hidden path="firstName" />		 
				 <form:hidden path="projectId" />	
				 <form:hidden path="withDeleted" />
				 <form:hidden path="organizationId" />				
		
				<c:import url="../Pagination.jsp">
				   <c:param name="currentPage" value="${searchPersonBean.currentPage}"/>
				   <c:param name="nbrOfPages" value="${searchPersonBean.nbrOfPages}"/>
				   <c:param name="firstPage" value="${searchPersonBean.lowerLimit}"/>
				   <c:param name="lastPage" value="${searchPersonBean.upperLimit}"/>
				   <c:param name="baseURL" value="ReportTimeSheetPersonSearch.htm"/>
				   <c:param name="divResults" value="SEARCH_RESULTS"/>
				</c:import>
			</form:form>	
		</fieldset>		
		<script>				
				
			//----------------------------------------------------ORDER BY LAST NAME----------------------------------------------------------------
			YAHOO.util.Event.addListener("reportSortLastNameLinkId", "click", order, {url : 'ReportTimeSheetPersonSearch.htm?action=sort', newSortParam : 'lastName', 
			oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'personSearchForm', divId : 'SEARCH_RESULTS', 
			withContext : true}, true);

			//----------------------------------------------------ORDER BY FIRST NAME----------------------------------------------------------------
			YAHOO.util.Event.addListener("reportSortFirstNameLinkId", "click", order, {url : 'ReportTimeSheetPersonSearch.htm?action=sort', newSortParam : 'firstName', 
			oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'personSearchForm', divId : 'SEARCH_RESULTS', 
			withContext : true}, true);

			//----------------------------------------------------ORDER BY PROJECT/ ----------------------------------------------------------------
			YAHOO.util.Event.addListener("reportSortProjectNameLinkId", "click", order, {url : 'ReportTimeSheetPersonSearch.htm?action=sort', newSortParam : 'projectName', 
			oldSortParam : '${searchPersonBean.sortParam}', sortDirection : '${searchPersonBean.sortDirection}', formId : 'personSearchForm', divId : 'SEARCH_RESULTS', 
			withContext : true}, true);

			//------------------------------------------------------ADD -----------------------------------------------
			YAHOO.util.Event.addListener("addAll", "click", manageAddPersons, {personsSelectId: 'reportTimeSheetForm_personSelect', teamMembersDiv: "teamMembers",
			personsDiv: "persons", noSelectedItemsMessage: '<spring:message code="report.time.sheet.selectedPersonsMessage"/>', memberId: 'memberId',
			personId: 'personId', forOrganizationLabel: '<spring:message code="report.time.sheet.for.organization"/>', searchResults: '${SEARCH_RESULTS_JSON}',
			panelTitle:'<spring:message code="report.time.sheet.select.persons"/>', checkBoxes: document.paginationForm.personOrMemberId, teamMembersHiddenInputsName: 'teamMemberIds',
			personsHiddenInputsName: 'personIds'}, true);

			//------------------------------------------------------SELECT ALL-------------------------------------------------
			YAHOO.util.Event.addListener("selectAllPersons", "click", selectAllCheckbox, {checkboxId: 'selectAllPersons', ids: document.paginationForm.personOrMemberId, 
			table: document.getElementById('listingTable')}, true);
			
		</script>				
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="person.no.results.message"/></div>
	</c:otherwise>
</c:choose>
