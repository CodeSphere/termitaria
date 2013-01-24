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
		<fieldset style="width: 850px;">
			<legend><spring:message code="currency.title.listing" /></legend>
			<form:form id="paginationForm" name="paginationForm" commandName="searchCurrencyBean">
				<input type="button" class="button" id="deleteAll" value="<spring:message code="delete"/>" />
					<table class="list_results" style="width: 850px" id="listingTable">
						<tr class="list_results_header">
							<th style="width: 5px;" class="checkBox">
								<input type="checkbox" id="selectAllCurrencies">
							</th>
							<th style="width: 500px">
								<a class="sortLink" id="nameSortLink" href="#">
									<spring:message code="currency.name"/>
								</a>
							</th>
							<th style="width: 250px">
								<spring:message code="currency.initials"/>
							</th>
							<th style="width: 100px">
								<spring:message code="actions"/>
							</th>
						</tr>
						<c:set var="i" value="0"/>
						<c:set var="cssClass" value=""/>
						<c:forEach var="currencyBean" items="${SEARCH_RESULTS}">
							<c:set var="i" value="${i + 1}"/>
							<c:choose>
								<c:when test="${i % 2 == 0}">
									<c:set var="cssClass" value="even_row"/>
								</c:when>
								<c:otherwise>
									<c:set var="cssClass" value="odd_row"/>
								</c:otherwise>
							</c:choose>
							<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}" onClick="selectUnSelectRow(this, document.paginationForm.currencyId, document.paginationForm.selectAllCurrencies)">
								<td class="checkBox">
									<input id="currencyId" name="currencyId" type="checkbox" onClick="cancelCheckEvent(this)" value="${currencyBean.currencyId}"/>
								</td>
								<td>
									<a href="#" onClick="getContentFromUrlDirect('CurrencyForm.htm?ACTION=GET&currencyId=${currencyBean.currencyId}&BACK_URL=CurrencySearch.htm', 'MAIN_CONTENT')" title="<spring:message code='currency.edit'/>">
										${currencyBean.name}
									</a>
								</td>
								<td>
									${currencyBean.initials}
								</td>
								<td>
									<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
										<tr>
											<td>
												<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyUpdate}">
													<a href="#" onClick="getContentFromUrlDirect('CurrencyForm.htm?ACTION=GET&currencyId=${currencyBean.currencyId}&BACK_URL=CurrencySearch.htm', 'MAIN_CONTENT')" title="<spring:message code='currency.edit'/>"><img src="images/buttons/action_edit.png"/></a>
												</security:authorize>
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
					
					<form:hidden path="initials"/>
					<form:hidden path="name"/>	
					<form:hidden path="organizationId"/>										
					
					<c:import url="../../Pagination.jsp">
					<c:param name="currentPage" value="${searchCurrencyBean.currentPage}"/>
					<c:param name="nbrOfPages" value="${searchCurrencyBean.nbrOfPages}"/>
					<c:param name="firstPage" value="${searchCurrencyBean.lowerLimit}"/>
					<c:param name="lastPage" value="${searchCurrencyBean.upperLimit}"/>
					<c:param name="baseURL" value="CurrencySearch.htm"/>
					<c:param name="divResults" value="RESULTS"/>
				</c:import>
			</form:form>
		</fieldset>
		<script>
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencySearch}">
				//----------------------------------------------------ORDER----------------------------------------------------------------
				YAHOO.util.Event.addListener("nameSortLink", "click", order, {url : 'CurrencySearch.htm?action=sort', newSortParam : 'name', 
					oldSortParam : '${searchCurrencyBean.sortParam}', sortDirection : '${searchCurrencyBean.sortDirection}', formId : 'searchForm', divId : 'RESULTS', 
					withContext : true}, true);
			</security:authorize>	

			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencySearch}">
				//------------------------------------------------------DELETE ALL -----------------------------------------------
				YAHOO.util.Event.addListener("deleteAll", "click", submitWithConfirmation, {url: 'CurrencySearch.htm?action=DELETE_ALL', formId: "paginationForm",
				container: "RESULTS", confirmationMessage: '<spring:message code="currency.delete.confirmationMessage"/>', selectedForDeletionElementsName: 'currencyId',
				noSelectedItemsMessage: '<spring:message code="currency.delete.selectCurrencyMessage"/>', panelTitle: '<spring:message code="confirm.delete"/>'}, true);
			</security:authorize>				

			//------------------------------------------------------SELECT ALL-------------------------------------------------
			YAHOO.util.Event.addListener("selectAllCurrencies", "click", selectAllCheckbox, {checkboxId: 'selectAllCurrencies', ids: document.paginationForm.currencyId, 
			table: document.getElementById('listingTable')}, true);

		</script>
		</c:when>
	<c:otherwise>
		<div class="noSearchResults"><spring:message code="currency.no.results.message"/></div>
	</c:otherwise>
</c:choose>
