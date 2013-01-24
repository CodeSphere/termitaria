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

<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeSearch}">
<c:choose>
	<c:when test="${!(empty OOO_PROFILES)}">
		<fieldset style="width: 890px;">
			<legend><spring:message code="ooo.title.listing" /></legend>
		
				<table class="list_results">
					<tr class="list_results_header">
						<th style="width:160px;">
							<a class="sortLink" id="sortLinkPersonFirstName" href="#">
								<spring:message code="ooo.person" />
							</a>
						</th>
						<th style="width:160px;">
							<a class="sortLink" id="sortLinkReplacementFirstName" href="#">
								<spring:message code="ooo.replacement" />
							</a>
						</th>
						<th style="width:160px;"><spring:message code="ooo.startDate" /></th>
						<th style="width:160px;"><spring:message code="ooo.endDate" /></th>			
						<th style="width:300px;">							
							<spring:message code="ooo.observation" />							
						</th>						
						<th style="width:120px;"><spring:message code="ooo.actions" /></th>					
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					<c:forEach var="ooo" items="${OOO_PROFILES}">
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
							<td>
								<a href="#" onClick="javascript:getContentFromUrlDirect('OutOfOffice.htm?action=EDIT_OOO_FROM_SEARCH&oooId=${ooo.outOfOfficeId}','MAIN_CONTENT');" title="<spring:message code="ooo.edit"/>">
									${ooo.person.firstName}&nbsp;${ooo.person.lastName}
								</a>
							</td>
							<td>${ooo.personReplacement.firstName}&nbsp;${ooo.personReplacement.lastName}</td>
			 				<td><fmt:formatDate value="${ooo.startPeriod}" pattern="dd-MM-yyyy HH:mm"/></td>
							<td><fmt:formatDate value="${ooo.endPeriod}" pattern="dd-MM-yyyy HH:mm"/></td>
							<c:choose>
								<c:when test="${fn:length(ooo.observation) ne 0}">
									<td align="center" onmouseover="Tip('${ooo.tokenizedObservation}', BGCOLOR, getCSSRule('.toolTipBody').style.backgroundColor, FONTWEIGHT, getCSSRule('.toolTipBody').style.fontWeight, FONTCOLOR, getCSSRule('.toolTipBody').style.color, BORDERCOLOR, getCSSRule('.toolTipBody').style.borderColor, FADEIN, 800, FADEOUT, 800);") onmouseout="UnTip();">
										${ooo.truncatedTokenizedObservation}
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										${ooo.truncatedTokenizedObservation}
									</td>
								</c:otherwise>
							</c:choose>				
							<td>
								<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
									<tr>											
										<td>														
											<a href="#" onClick="javascript:getContentFromUrlDirect('OutOfOffice.htm?action=EDIT_OOO_FROM_SEARCH&oooId=${ooo.outOfOfficeId}','MAIN_CONTENT');" title="<spring:message code="ooo.edit"/>"><img src="images/buttons/action_edit.png"/></a>										
										</td>
										<td>										
											<a href="#" onClick="submitFormWithConfirmation('SearchOutOfOffice.htm?action=delete&personId=${ooo.person.personId}&oooId=${ooo.outOfOfficeId}', 'paginationForm', 'RESULTS', '<spring:message code="ooo.cancel.confirmationMessage"/>', '<spring:message code="confirm.cancel"/>');" title="<spring:message code="ooo.cancel"/>"> <img src="images/buttons/action_delete.png"/></a>																				
										</td>								
									</tr>
								</table>		
							</td>									
						</tr>						
					</c:forEach>
				</table>
			</fieldset>	
	</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="ooo.no.results.message"/></div>
	</c:otherwise>
</c:choose>
<!-- Show pagination area only if there is more than 1 page -->

	<form:form id="paginationForm" commandName="searchOOOBean">
	<!-- THIS FIELD IS MANDATORY (NUMBER OF RESULTS/PAGE)-->
		<form:hidden path="resultsPerPage"/>
		<form:hidden path="currentPage"/>
		<form:hidden path="nbrOfPages"/>
		<form:hidden path="nbrOfResults"/>
		<form:hidden path="sortParam" />
		<form:hidden path="sortDirection" />
		<form:hidden path="ownerFirstName" />
		<form:hidden path="ownerLastName"/>
		<form:hidden path="replacementFirstName" />
		<form:hidden path="replacementLastName"/>
		<form:hidden path="organisationId"/>
		<spring:bind path="startPeriod">
			<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd-MM-yyyy HH:mm'/>"/>
		</spring:bind>
		<spring:bind path="endPeriod">
			<input type="hidden" name="${status.expression}" value="<fmt:formatDate value='${status.value}' pattern='dd-MM-yyyy HH:mm'/>"/>
		</spring:bind>
	
	<c:import url="../Pagination.jsp">
	   <c:param name="currentPage" value="${searchOOOBean.currentPage}"/>
	   <c:param name="nbrOfPages" value="${searchOOOBean.nbrOfPages}"/>
	   <c:param name="firstPage" value="${searchOOOBean.lowerLimit}"/>
	   <c:param name="lastPage" value="${searchOOOBean.upperLimit}"/>
	   <c:param name="baseURL" value="SearchOutOfOffice.htm"/>
	   <c:param name="divResults" value="RESULTS"/>
	</c:import>
	
	</form:form>
	
	<script>

	//------------------------------------------------------ORDER BY PERSON FIRST NAME---------------------------------------------
	
	YAHOO.util.Event.addListener("sortLinkPersonFirstName", "click", order, { url : 'SearchOutOfOffice.htm?action=sort', newSortParam : 'person.firstName', 
	oldSortParam : '${searchOOOBean.sortParam}', sortDirection : '${searchOOOBean.sortDirection}', formId : 'oooSearchForm', 
	divId : 'RESULTS', withContext : true}, true);


	//------------------------------------------------------ORDER BY PERSON REPLACEMENT FIRST NAME---------------------------------------------
	YAHOO.util.Event.addListener("sortLinkReplacementFirstName", "click", order, { url : 'SearchOutOfOffice.htm?action=sort', newSortParam : 'person.firstName', 
	oldSortParam : '${searchOOOBean.sortParam}', sortDirection : '${searchOOOBean.sortDirection}', formId : 'oooSearchForm', 
	divId : 'RESULTS', withContext : true}, true);

</script>
</security:authorize>
