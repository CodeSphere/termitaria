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
	<c:when test="${!(empty NOTIFICATIONS)}">
		<fieldset style="width: 890px;">
			<legend><spring:message code="notification.title.listing" /></legend>
			
			<form:form id="paginationForm" name="paginationForm" commandName="notificationSearchBean">
				<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
				
				<div class="spacing_div"></div>
				
				<table id="listingTable" class="list_results" >
					<tr class="list_results_header">
						<th style="width: 5px;" class="checkBox">
							<input type="checkbox" id="selectAllNotifications">
						</th>
						<th align="center" style="width:240px;">
							<a class="sortLink" id="sortLinkId1" href="#">
								<spring:message code="notification.list.subject"/>
							</a>
						</th>	
						<th align="center" style="width:140px;">
							<a class="sortLink" id="sortLinkId2" href="#">
								<spring:message code="notification.list.issuedDate"/>
							</a>
						</th>														
						<th align="center" style="width:700px;">
							<spring:message code="notification.list.message"/>
						</th>	
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					
					<c:forEach var="notification" items="${NOTIFICATIONS}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
						<tr class="${cssClass}"  onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id='${i}' onClick="selectUnSelectRow(this, document.paginationForm.notificationId, document.paginationForm.selectAllNotifications)">
							<td class="checkBox">
								<input id="notificationId" name="notificationId" type="checkbox" onclick="cancelCheckEvent(this)" value="${notification.notificationId}"/>
							</td>
							<td align="center">
								${notification.subject}
							</td>		
							<td align="center">
								<fmt:formatDate value="${notification.issuedDate}" pattern="dd/MM/yyyy  HH:mm"/>
							</td>	
							<td align="center">
								${notification.message}
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
				<spring:bind path="startDate">
					<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd/MM/yyyy HH:mm'/>"/>
				</spring:bind>
				<spring:bind path="endDate">
					<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd/MM/yyyy HH:mm'/>"/>
				</spring:bind>
				<form:hidden path="message" />
				<form:hidden path="subject" />
				
				<c:import url="../../Pagination.jsp">
				   <c:param name="currentPage" value="${notificationSearchBean.currentPage}"/>
				   <c:param name="nbrOfPages" value="${notificationSearchBean.nbrOfPages}"/>
				   <c:param name="firstPage" value="${notificationSearchBean.lowerLimit}"/>
				   <c:param name="lastPage" value="${notificationSearchBean.upperLimit}"/>
				   <c:param name="baseURL" value="SearchNotification.htm"/>
				   <c:param name="divResults" value="RESULTS"/>
				   <c:param name="formName" value="paginationForm"/>
				</c:import>
			</form:form>
		</fieldset>	
		<script>
			// if I have results, I must have deleteAll option available
			document.getElementById("active_link").style.display = "inline";
			document.getElementById("inactive_link").style.display = "none";

			YAHOO.util.Event.addListener("deleteAll", "click", submitDeleteWithConfirmation, {url: 'SearchNotification.htm?action=DELETE', formId: "paginationForm",
			container: "RESULTS", confirmationMessage: '<spring:message code="delete.confirmationMessage"/>', selectedForDeletionElementsName: 'notificationId',
			noSelectedItemsMessage: '<spring:message code="delete.selectMessage"/>', selectionRequired: true}, true);

			YAHOO.util.Event.addListener("selectAllNotifications", "click", selectAllCheckbox, {checkboxId: 'selectAllNotifications', ids: document.paginationForm.notificationId, table: document.getElementById('listingTable')}, true);
			
			//----------------------------------------------------ORDER----------------------------------------------------------------
		
			YAHOO.util.Event.addListener("sortLinkId1", "click", order, {url : 'SearchNotification.htm?action=sort', newSortParam : 'subject', 
			oldSortParam : '${notificationSearchBean.sortParam}', sortDirection : '${notificationSearchBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS',
			withContext : true}, true);
					
			YAHOO.util.Event.addListener("sortLinkId2", "click", order, {url : 'SearchNotification.htm?action=sort', newSortParam : 'issuedDate', 
			oldSortParam : '${notificationSearchBean.sortParam}', sortDirection : '${notificationSearchBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS',
			withContext : true}, true);

		</script>						
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="notification.no.results.message"/></div>
		
		<script>
			document.getElementById("active_link").style.display = "none";
			document.getElementById("inactive_link").style.display = "inline";
		</script>
		
	</c:otherwise>
</c:choose>
