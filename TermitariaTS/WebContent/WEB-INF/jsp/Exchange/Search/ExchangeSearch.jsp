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


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">		
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->
				<a href="#" id="back" onclick="ENTITY = getActivityData();check_add('${BACK_URL}', 'MAIN_CONTENT');">
						<spring:message code="back"/>
				</a>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
					<a href="#" id="addNewExchange" ><spring:message code="add" /></a>	
				</security:authorize>
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
					<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
						<a href="#" id="addNewExchange" ><spring:message code="add" /></a>	
					</c:if>
				</security:authorize>												
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<span class="section_title"><img src="images/titles/Exchange.jpg"><spring:message code="exchange.section.title"/></span>

<table>
	<tr>
		<td>
			<form:form id="searchForm" name="searchForm" commandName="searchExchangeBean">
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				
				<!-- first line -->
				<tr>
					<!-- FIRST CURRENCY -->
					<td class="labelTd" style="width:100px">
						<spring:message code="exchange.first.currency"/>
					</td>
					<td>
						<form:select path="firstCurrencyId" tabindex="1" id="exchangeSearchForm_firstCurrency" cssClass="formFieldSearch">
							<option value="-1" title='<spring:message code="select" />'><spring:message code="select" /></option>
							<c:forEach var="currency" items="${ORG_CURRENCIES}">
								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
							</c:forEach>
						</form:select>
					</td>
					
					<!-- SECOND CURRENCY -->
					<td class="labelTd" style="width:100px">
						<spring:message code="exchange.second.currency"/>
					</td>
					<td>
						<form:select path="secondCurrencyId" tabindex="2" id="exchangeSearchForm_secondCurrency" cssClass="formFieldSearch">
							<option value="-1" title='<spring:message code="select" />'><spring:message code="select" /></option>
							<c:forEach var="currency" items="${ORG_CURRENCIES}">
								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
							</c:forEach>
						</form:select>
					</td>
					
				<!-- NR OF RESULTS PER PAGE -->
					<td class="labelTd" style="width:140px">
						<spring:message code="exchange.results"/>
					</td>
					<td>
						<form:select path="resultsPerPage" tabindex="3">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				
				
				<!-- second line  -->
				<tr>
				<!-- PROJECT -->
					<td class="labelTd" style="width:100px">
						<spring:message code="exchange.project"/>
					</td>
					<td>
						<div title='<spring:message code="select"/>' id="exchangeSearchProjectsId">
							<form:select path="projectId" tabindex="4" id="exchangeSearchForm_project" cssClass="formFieldSearch" onchange="javascript:changeTitle(this.options[selectedIndex].title, 'exchangeSearchProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'exchangeSearchForm_project', 'exchangeSearchForm_project1');">
								<option value="-2" class="chooseProject" title='<spring:message code="select" />'><spring:message code="select" /></option>
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
									<option value="-1" class="fromOrganization" title='<spring:message code="exchange.for.organization" />'><spring:message code="exchange.for.organization" /></option>
								</security:authorize>
								<c:forEach var="project" items="${USER_PROJECTS}">
									<c:choose>
										<c:when test="${project.clientId != -1}">
											<c:set var="client" value="hasClient"/>														
											<c:choose>
												<c:when test="${project.status == 1}">
													<c:set var="prj" value=""/>	
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
									<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>
								</c:forEach>
							</form:select>						
						</div>
						
						<div id="exchangeSearchForm_project1">
							<span class="chooseProject"></span>		
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
								<span class="fromOrganization"></span>			
							</security:authorize>														
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
				</tr>
				
				<tr>
					<td colspan="5"></td>
					<td>			
						<input type="button" class="button" id="searchExchange" value="<spring:message code="search"/>" tabindex="5"/>			
					</td>				
				</tr>
			</table>
			</form:form>
			<br/>
		</td>
	</tr>
	<tr>
		<td>
			<div id="MESSAGES">
				<%@ include file="../../Messages.jsp" %>	
			</div>
			<div id="RESULTS"></div>
		</td>
	</tr>
</table>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script type="text/javascript">

	// -------------------------------HOME MENU BUTTON----------------------------------------------------
	//var getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//--------------------------------ADD EXCHANGE BUTTON---------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
		var getObject = new ObjSubmit("ExchangeForm.htm?ACTION=ADD&BACK_URL=ExchangeSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewExchange", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
		<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
			var getObject = new ObjSubmit("ExchangeForm.htm?ACTION=ADD&BACK_URL=ExchangeSearch.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("addNewExchange", "click", getContentFromUrl, getObject, true);
		</c:if>
	</security:authorize>	
	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
		// -------------------------------SUBMIT SEARCH FORM---------------------------------------------------
		submitForm('ExchangeSearch.htm', 'searchForm', 'RESULTS');
		// ------------------------------- SEARCH COST SHEET ----------------------------------------------------
		YAHOO.util.Event.addListener("searchExchange", "click", submitForm, { url : "ExchangeSearch.htm", formId : "searchForm", container : "RESULTS", 
			withContext : true}, true);
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
		<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
			// -------------------------------SUBMIT SEARCH FORM---------------------------------------------------
			submitForm('ExchangeSearch.htm', 'searchForm', 'RESULTS');
			// ------------------------------- SEARCH COST SHEET ----------------------------------------------------
			YAHOO.util.Event.addListener("searchExchange", "click", submitForm, { url : "ExchangeSearch.htm", formId : "searchForm", container : "RESULTS", 
			withContext : true}, true);
		</c:if>
	</security:authorize>	
