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
	<c:when test="${exchangeBean.exchangeId > 0}">
		<c:set var="cssClass" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>		
	</c:otherwise>
</c:choose>

<c:set var="showDelete" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeDeleteAll}">
	<c:set var="showDelete" value="true"/>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeDeleteAll}">
	<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
		<c:set var="showDelete" value="true"/>
	</c:if>
</security:authorize>	

<c:set var="showAdd" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
	<c:set var="showAdd" value="true"/>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
	<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
		<c:set var="showAdd" value="true"/>
	</c:if>
</security:authorize>	

<c:set var="showSearch" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
	<c:set var="showSearch" value="true"/>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
	<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
		<c:set var="showSearch" value="true"/>
	</c:if>
</security:authorize>	

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->	
				<a href="#" id="back" onclick="ENTITY = getExchangeData();check_add('${BACK_URL}&projectId=${PROJECT_ID}&hasProjectDetail=true', 'MAIN_CONTENT');"><spring:message code="back"/></a>
				<c:if test="${showAdd eq true}">
					<a href="#" id="addNewExchange" title="<spring:message code="exchange.submenu.add.title"/>"><spring:message code='add'/> </a>
				</c:if>
				<c:if test="${showDelete eq true}">
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="exchange.submenu.delete.title"/>"><spring:message code='delete'/> </a>
				</c:if>
				<!--  
				<c:if test="${showSearch eq true}">
					<a href="#" id="submenuSearch" title="<spring:message code="search"/>"><spring:message code="search"/></a>
				</c:if>
				-->
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${exchangeBean.exchangeId > 0}">
		<span class="section_title"><img src="images/titles/ExchangeEdit.jpg"><spring:message code="exchange.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/ExchangeAdd.jpg"><spring:message code="exchange.add.section.title"/></span>	
	</c:otherwise>
</c:choose>


<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include file="../Messages.jsp" %>
	<br/>
</div>
	<form:form commandName="exchangeBean" name="exchangeForm" id="exchangeForm" onsubmit="return false;">		
		<form:hidden path="exchangeId" id="exchangeId"/>
		<table cellpadding="3" class="tableAdd">
			<tr><td class="tableAddSpacer">&nbsp;</td>
			</tr>
			<tr>
				<!-- ====================================PROJECT====================================================== -->
				<td class="labelTd labelTdInactive">
					<spring:message code="exchange.project"/>
				</td>
				<c:choose>
					<c:when test="${exchangeBean.projectId ne null }">
						<form:hidden path="projectId" id="exchangeForm_project"/>
						<c:forEach var="project" items="${USER_PROJECTS}">
							<c:if test="${project.projectId == exchangeBean.projectId}">
								<td>
								${project.name}	
								</td>
							</c:if>
						</c:forEach>
					</c:when>
				<c:otherwise>
				<c:choose>
					<c:when test="${exchangeBean.exchangeId > 0}">
						<td class="labelTd labelTdInactive">
							<spring:message code="exchange.project"/>
						</td>

						<td class="inactive">
							<form:select path="projectId" tabindex="1" id="exchangeForm_project" cssClass="formField required" disabled="true">
								<option value="-2" title='<spring:message code="exchange.choose.project" />'><spring:message code="exchange.choose.project" /></option>
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
									<c:if test="${exchangeBean.projectDetailId eq null}">
										<option value="-1" class="fromOrganization" title='<spring:message code="exchange.for.organization"/>' SELECTED><spring:message code="exchange.for.organization" /></option>
									</c:if>	
								</security:authorize>
								<c:forEach var="project" items="${USER_PROJECTS}">
									<c:if test="${exchangeBean.projectId == project.projectId}">
										<option value="${project.projectId}" title="${project.name}" selected="selected">${project.name}</option>
									</c:if>
								</c:forEach>
							</form:select>
							<div id="project_message"></div>
						</td>
					</c:when>
					<c:otherwise>
						<td class="labelTd">
							<spring:message code="exchange.project"/><span id="required">*</span>
						</td>
						<td>
							<div title='<spring:message code="exchange.choose.project"/>' id="exchangeSelectProjectsId">
								<form:select path="projectId" tabindex="1" id="exchangeForm_project" cssClass="formField required" onchange="javascript:changeTitle(this.options[selectedIndex].title, 'exchangeSelectProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'exchangeForm_project', 'exchangeForm_project1');">
									<option value="-2" class="chooseProject" title='<spring:message code="exchange.choose.project" />'><spring:message code="exchange.choose.project" /></option>
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
										<c:choose>
											<c:when test="${exchangeBean.projectId == -1}">
												<option value="-1" class="fromOrganization" title='<spring:message code="exchange.for.organization" />' SELECTED><spring:message code="exchange.for.organization" /></option>
											</c:when>
											<c:otherwise>
												<option value="-1" class="fromOrganization" title='<spring:message code="exchange.for.organization" />'><spring:message code="exchange.for.organization" /></option>
											</c:otherwise>
										</c:choose>							
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
									
										<c:choose>
											<c:when test="${exchangeBean.projectId == project.projectId}">
												<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>
											</c:when>
											<c:otherwise>
												<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>
											</c:otherwise>
										</c:choose>																
									</c:forEach>
								</form:select>							
								<div id="project_message"></div>
							</div>
							
							<div id="exchangeForm_project1">
								<span class="chooseProject"></span>		
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeAddAll}">
									<c:if test="${exchangeBean.projectDetailId eq null}">
										<span class="fromOrganization"></span>		
									</c:if>
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
					</c:otherwise>
				</c:choose>
			</c:otherwise>
			</c:choose>
			</tr>
			
			<tr>
				<!-- ====================================FIRST CURRENCY====================================================== -->
				<c:choose>
					<c:when test="${exchangeBean.exchangeId > 0}">
						<td class="labelTd labelTdInactive">
							<spring:message code="exchange.first.currency"/>
						</td>
						<td class="inactive">
							<form:select path="firstCurrencyId" tabindex="2" id="exchangeForm_firstCurrency" cssClass="formField required" disabled="true">
								<option value="-1" title='<spring:message code="exchange.choose.first.currency" />'><spring:message code="exchange.choose.first.currency" /></option>
								<c:forEach var="currency" items="${ORG_CURRENCIES}">
									<c:if test="${exchangeBean.firstCurrencyId == currency.value}">
										<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
									</c:if>
								</c:forEach>
							</form:select>
							<div id="firstCurrency_message"></div>
						</td>
					</c:when>
					<c:otherwise>
						<td class="labelTd">
							<spring:message code="exchange.first.currency"/><span id="required">*</span>
						</td>
						<td>
							<form:select path="firstCurrencyId" tabindex="2" id="exchangeForm_firstCurrency" cssClass="formField required">
								<option value="-1" title='<spring:message code="exchange.choose.first.currency" />'><spring:message code="exchange.choose.first.currency" /></option>
								<c:forEach var="currency" items="${ORG_CURRENCIES}">
									<c:choose>
										<c:when test="${exchangeBean.firstCurrencyId == currency.value}">
											<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
										</c:when>
										<c:otherwise>
											<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
							<div id="firstCurrency_message"></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			
			<tr>
				<!-- ====================================SECOND CURRENCY====================================================== -->
				<c:choose>
					<c:when test="${exchangeBean.exchangeId > 0}">
						<td class="labelTd labelTdInactive">
							<spring:message code="exchange.second.currency"/>
						</td>
						<td class="inactive">
							<form:select path="secondCurrencyId" tabindex="3" id="exchangeForm_secondCurrency" cssClass="formField required" disabled="true">
								<option value="-1" title='<spring:message code="exchange.choose.second.currency" />'><spring:message code="exchange.choose.second.currency" /></option>
								<c:forEach var="currency" items="${ORG_CURRENCIES}">
									<c:if test="${exchangeBean.secondCurrencyId == currency.value}">
										<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
									</c:if>
								</c:forEach>
							</form:select>
							<div id="secondCurrency_message"></div>
						</td>
					</c:when>
					<c:otherwise>
						<td class="labelTd">
							<spring:message code="exchange.second.currency"/><span id="required">*</span>
						</td>
						<td>
							<form:select path="secondCurrencyId" tabindex="3" id="exchangeForm_secondCurrency" cssClass="formField required">
								<option value="-1" title='<spring:message code="exchange.choose.second.currency" />'><spring:message code="exchange.choose.second.currency" /></option>
								<c:forEach var="currency" items="${ORG_CURRENCIES}">
									<c:choose>
										<c:when test="${exchangeBean.secondCurrencyId == currency.value}">
											<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
										</c:when>
										<c:otherwise>
											<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
							<div id="secondCurrency_message"></div>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			
			<tr>
				<!-- ====================================RATE============================================================= -->
				<td class="labelTd">
					<spring:message code="exchange.rate"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="rate" id="exchangeForm_rate" tabindex="4" cssClass="formField required" onfocus="this.parentNode.getElementsByTagName('span')[0].style.display = 'inline';" onkeyup="restrictCharactersForExchangeRate('exchangeForm_rate');" onblur="computeReverseRate(this.value, 'exchangeForm_reverseRate'); setExchangeRateFormat('exchangeForm_rate'); this.parentNode.getElementsByTagName('span')[0].style.display = 'none';"/>
					<span class="hint"><spring:message code="exchange.rate.hint"/><span class="hint-pointer">&nbsp;</span></span>
					<div id="rate_message"></div>
				</td>
			</tr>
			
			<tr>
				<!-- ====================================REVERSE RATE====================================================== -->
				<td class="labelTd">
					<spring:message code="exchange.reverse.rate"/>
				</td>
				<td>
					<input type="text" id="exchangeForm_reverseRate" class="formField" readonly="readonly"/>
				</td>
			</tr>
			
			<tr>
				<td colspan="2" class="formActions" align="right">
						<input type="button" class="button" id="save" tabindex="5" value="<spring:message code="save"/>"/>	
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
							<c:if test="${exchangeBean.exchangeId < 0}">
								<input type="button" class="button"  id="currencyPanel" tabindex="6" value="<spring:message code="activity.add.currecy"/>"/>
							</c:if>
						</security:authorize>					
						<input type="button" class="button" id="cancel" tabindex="7" value="<spring:message code="cancel"/>" onclick="ENTITY = getExchangeData();check_add('${BACK_URL}&projectId=${PROJECT_ID}&hasProjectDetail=true', 'MAIN_CONTENT');"/>						
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
	
	
	document.exchangeForm.exchangeForm_project.focus();

	//----------------------------------------- ACTIVITY ADD CURRENCY ---------------------------------------	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
		var submitCurrencyPanel = ObjSubmitCurrencyPanel('CurrencyForm.htm?GET_FROM_PANEL=true&FROM_EXCHANGE=true', "<spring:message code="activity.add.currecy"/>");		
		YAHOO.util.Event.addListener("currencyPanel", "click", displayAddCurrencyPanel, submitCurrencyPanel, true);
	</security:authorize>
	
	<c:if test="${showAdd eq true}">
		//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
		var submitObject = new ObjSubmit("ExchangeForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "exchangeForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitSaveExchange, submitObject, true);

		//----------------------------------- ENTER EVENT ON ALL FIELDS---------------------------------------
		var keyListner = null; 
	
		keyListener = new YAHOO.util.KeyListener("exchangeForm_rate", { keys:13 },  { fn:submitSaveExchange, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
	</c:if>

	//----------------------------------------------- DELETE EXCHANGE ------------------------------------------------
	<c:if test="${exchangeBean.exchangeId > 0}">
		<c:if test="${showDelete eq true}">
		
			//submitObject = new ObjSubmitWithConfirmation("ExchangeSearch.htm?ACTION=DELETE_FROM_EXCHANGE_FORM&exchangeId=${exchangeBean.exchangeId}", "", "MAIN_CONTENT", '<spring:message code="exchange.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			submitObject = new ObjSubmitWithConfirmation("ProjectDetails.htm?ACTION=DELETE_FROM_EXCHANGE_FORM&exchangeId=${exchangeBean.exchangeId}&projectId=${PROJECT_ID}", "", "MAIN_CONTENT", '<spring:message code="exchange.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);
		</c:if>
	</c:if>	

	//----------------------------------------------- ADD NEW EXCHANGE-----------------------------------------------
	<c:if test="${showAdd eq true}">
		var	getObject = new ObjSubmit("ExchangeForm.htm?ACTION=ADD&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewExchange", "click", getContentFromUrl, getObject, true);
	</c:if>

	//-------------------- RATE AND REVERSE RATE DISPLAY AND FORMAT----------------------------------------------------------------
	<c:if test="${exchangeBean.rate ne null}">
		setExchangeRateFormat('exchangeForm_rate');
		computeReverseRate(${exchangeBean.rate}, 'exchangeForm_reverseRate');
	</c:if>

	//-------------------- OLD EXCHANGE ENTITY ----------------------------------------------------------
	//this must be after setExchangeRateFormat since that function alters one of the input values
	OLD_ENTITY = getExchangeData();

	<c:if test="${showSearch eq true}">
		//-------------------- SEARCH EXCHANGE --------------------------------------------------------------
		var getObject = new ObjSubmit("ExchangeSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);		
	</c:if>		

	<c:if test="${EXISTING_EXCHANGE_ID ne null}">
		showExistingExchangePanel("ExchangeForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}&EXISTING_EXCHANGE_ID=${EXISTING_EXCHANGE_ID}", "exchangeForm", "MAIN_CONTENT");
	</c:if>

	OLD_RATE = document.getElementById('exchangeForm_rate').value;
	
</script>
