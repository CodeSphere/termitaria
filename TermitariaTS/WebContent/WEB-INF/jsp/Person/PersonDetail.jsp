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
	<c:when test="${personDetailBean.personDetailId > 0}">
		<c:set var="cssClass" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>		
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${personDetailBean.costPrice ne null}">
		<c:set var="cssCostClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssCostClass" value="formField"/>		
	</c:otherwise>
</c:choose>	

<c:choose>
	<c:when test="${personDetailBean.overtimeCostPrice ne null}">
		<c:set var="cssOvertimeCostClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssOvertimeCostClass" value="formField"/>		
	</c:otherwise>
</c:choose>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">				
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->	
				<a href="#" id="back" onclick="ENTITY = getPersonDetailData();check_add('${BACK_URL}&projectId=${PROJECT_ID}&hasProjectDetail=true', 'MAIN_CONTENT');">
					<spring:message code="back"/>
				</a>														
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${personDetailBean.personDetailId > 0}">
		<span class="section_title"><img src="images/titles/PersonDetailEdit.jpg"/><spring:message code="person.detail.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/PersonDetailAdd.jpg"/><spring:message code="person.detail.add.section.title"/></span>
	</c:otherwise>
</c:choose>

<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include file="../Messages.jsp" %>
</div>

<form:form method="post" commandName="personDetailBean" id="personDetailForm" name="personDetailForm" onsubmit="return false;">
	
	<form:hidden path="personDetailId" id="personDetailId" />				
	
	<table class="tableAdd">
		<tr>
			<td width="370px">
				<table cellpadding="3">
		
					<tr><td class="tableAddSpacer" colspan="2">&nbsp;</td></tr>		
					
					<!-- ====================================TEAM MEMBER========================================================= -->
					<tr>
						<td class="labelTd">
							<spring:message code="person.detail.person"/> 
						</td>			
						<td>
							<form:hidden path="personId" id="personId"/>					
							<input id="personDetailForm_member" name="name" type="text" value="${PERSON_NAME}" tabindex="1" class="formField" readonly="readonly"/>	
																													
						</td>
					</tr>
																																																											 			
					<!-- ====================================COST PRICE=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="person.detail.cost.price"/>
						</td>
						<td>
							<form:input path="costPrice" id="personDetailForm_costPrice" tabindex="2" cssClass="formField " onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('personDetailForm_costPrice')"/>
						</td>	
					</tr>
					
					<!-- ====================================COST PRICE CURRENCY=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="person.detail.cost.price.currency"/> <c:choose><c:when test="${personDetailBean.costPrice eq null}"><span id="costPriceCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="costPriceCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
						</td>
						<td>
							<form:select path="costPriceCurrencyId" id="personDetailForm_costPriceCurrency" tabindex="3" cssClass="${cssCostClass}">
	 							<form:option value="-1" title='<spring:message code="person.choose.cost.price.currency"/>'><spring:message code="person.choose.cost.price.currency"/></form:option>
			 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
			 						<c:choose>
			 							<c:when test="${currency.value == personDetailBean.costPriceCurrencyId}">
	 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
		 							</c:when>
		 							<c:otherwise>
		 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
		 							</c:otherwise>
			 						</c:choose>
			 					</c:forEach>
	 						</form:select>	
						</td>																		
					</tr>
					
					<!-- ====================================COST TIME UNIT=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="person.detail.cost.time.unit"/> <c:choose><c:when test="${personDetailBean.costPrice eq null}"><span id="costTimeUnitRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="costTimeUnitRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
						</td>
						<td>
							<div title='<spring:message code="person.choose.costTimeUnit"/>' id="personDetailFormCostTimeUnitsId">			
								<form:select path="costTimeUnit" id="personDetailForm_costTimeUnit" tabindex="4" cssClass="${cssCostClass}" onchange="changeTitle(this.options[selectedIndex].text, 'personDetailFormCostTimeUnitsId')">
									<form:option value="-1"><spring:message code="person.choose.costTimeUnit"></spring:message></form:option>									
										<c:forEach var="timeUnit" items="${NOM_TIME_UNIT}">																
											<c:choose>
												<c:when test="${timeUnit.value == personDetailBean.costTimeUnit}">
													<option title='<spring:message code="${timeUnit.label}"/>'  value="${timeUnit.value}" SELECTED><spring:message code="${timeUnit.label}"/></option>
												</c:when>
												<c:otherwise>
													<option title='<spring:message code="${timeUnit.label}"/>' value="${timeUnit.value}"><spring:message code="${timeUnit.label}"/></option>
												</c:otherwise>
											</c:choose>									
										</c:forEach>							
								</form:select>	
							</div>			
						</td>	
					</tr>
											
					<!-- ====================================OBSERVATION====================================================== -->
					<tr>
						<td class="labelTd">
							<spring:message code="person.detail.observation"/>
						</td>
						<td>
							<form:textarea path="observation" id="personDetailForm_observation" tabindex="5" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
							<div class="remainingCharactersPersonDetailObs" style="display:none" >
								<div id="showRemainingCharactersObservation" />			
							</div>				
						</td>
					</tr>
																					
				</table>
			</td>	
					
			<!-- =========================================OVERTIME======================================================= -->
			
			<td>
				<table>
					<tr>
						<td class="tableAddSpacer">&nbsp;</td>
					</tr>
				</table>
				
				<fieldset>
					<legend>
						<spring:message code="person.overtime" />
					</legend>
					<table cellpadding="3">					
						<tr>
							<td class="tableAddSpacer" colspan="2">&nbsp;</td>
						</tr>
						
						<!-- ====================================OVERTIME COST PRICE=================================================== -->
						<tr>			
							<td class="labelTd">
								<spring:message code="person.detail.overtime.cost.price"/>
							</td>
							<td>
								<form:input path="overtimeCostPrice" id="personDetailForm_overtimeCostPrice" tabindex="6" cssClass="formField " onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('personDetailForm_overtimeCostPrice')"/>
							</td>	
						</tr>
						
						<!-- ====================================OVERTIME COST CURRENCY=================================================== -->
						<tr>			
							<td class="labelTd">
								<spring:message code="person.detail.overtime.cost.currency"/> <c:choose><c:when test="${personDetailBean.overtimeCostPrice eq null}"><span id="overtimeCostCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="overtimeCostCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
							</td>
							<td>
								<form:select path="overtimeCostCurrencyId" id="personDetailForm_overtimeCostCurrency" tabindex="7" cssClass="${cssOvertimeCostClass}">
		 							<form:option value="-1" title='<spring:message code="person.choose.overtime.cost.currency"/>'><spring:message code="person.choose.overtime.cost.currency"/></form:option>
				 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
				 						<c:choose>
				 							<c:when test="${currency.value == personDetailBean.overtimeCostCurrencyId}">
		 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
			 							</c:when>
			 							<c:otherwise>
			 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
			 							</c:otherwise>
				 						</c:choose>
				 					</c:forEach>
	 							</form:select>																	
							</td>	
						</tr>
						
						<!-- ====================================OVERTIME COST TIME UNIT=================================================== -->
						<tr>			
							<td class="labelTd">
								<spring:message code="person.detail.overtime.cost.time.unit"/> <c:choose><c:when test="${personDetailBean.overtimeCostPrice eq null}"><span id="overtimeCostTimeUnitRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="overtimeCostTimeUnitRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
							</td>
							<td>
								<div title='<spring:message code="person.choose.overtimeCostTimeUnit"/>' id="personDetailFormOvertimeCostTimeUnitsId">			
									<form:select path="overtimeCostTimeUnit" id="personDetailForm_overtimeCostTimeUnit" tabindex="8" cssClass="${cssOvertimeCostClass}" onchange="changeTitle(this.options[selectedIndex].text, 'personDetailFormOvertimeCostTimeUnitsId')">
										<form:option value="-1"><spring:message code="person.choose.overtimeCostTimeUnit"></spring:message></form:option>									
											<c:forEach var="timeUnit" items="${NOM_TIME_UNIT}">																
												<c:choose>
													<c:when test="${timeUnit.value == personDetailBean.overtimeCostTimeUnit}">
														<option title='<spring:message code="${timeUnit.label}"/>'  value="${timeUnit.value}" SELECTED><spring:message code="${timeUnit.label}"/></option>
													</c:when>
													<c:otherwise>
														<option title='<spring:message code="${timeUnit.label}"/>' value="${timeUnit.value}"><spring:message code="${timeUnit.label}"/></option>
													</c:otherwise>
												</c:choose>									
											</c:forEach>							
									</form:select>	
								</div>			
							</td>	
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="formActions" align="right">	
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">		
					<input type="button" class="button" id="save" tabindex="9" value="<spring:message code="save"/>" />	
				</security:authorize>		
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">		
					<c:if test="${hasManager eq true}">
						<input type="button" class="button" id="save" tabindex="9" value="<spring:message code="save"/>" />
					</c:if>
				</security:authorize>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
					<input type="button" class="button"  id="currencyPanel" tabindex="10" value="<spring:message code="person.detail.add.currecy"/>"/>
				</security:authorize>																				
				<input type="button" class="button" id="cancel" onclick="ENTITY = getPersonDetailData();check_add('${BACK_URL}&projectId=${PROJECT_ID}&hasProjectDetail=true', 'MAIN_CONTENT');" tabindex="11" value="<spring:message code="cancel"/>" />				
			</td>
		</tr>
		
	</table>
	
	
</form:form>

<div style="display: ${display}">
</div>

<br/>

<div id="SECONDARY_CONTENT"></div>


<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>
						
	//------------ HOME ---------------------------------------------------------------	
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);	

	// focus on name input
	document.personDetailForm.name.focus();		
	
	//------------ SAVE PERSON DETAIL---------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">		
		var submitObject = new ObjSubmit("PersonDetail.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "personDetailForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitSavePersonDetail, submitObject, true);		
	</security:authorize>	

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonDetailAddUpdate}">		
		<c:if test="${hasManager eq true}">
			var submitObject = new ObjSubmit("PersonDetail.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "personDetailForm", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("save", "click", submitSavePersonDetail, submitObject, true);
		</c:if>		
	</security:authorize>	
			
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
	var validateObservationField = new ObjFieldRemaining("personDetailForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("personDetailForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("personDetailForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("personDetailForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
		
	
	//----------------------------------------- PERSON DETAIL ENTER EVENT ---------------------------------------
	
	OLD_ENTITY = getPersonDetailData();

	//----------------------------------------- PERSON DETAIL ADD CURRENCY ---------------------------------------	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
		var submitCurrencyPanel = ObjSubmitCurrencyPanel('CurrencyForm.htm?GET_FROM_PANEL=true&FROM_PERSON_DETAIL=true', "<spring:message code="person.detail.add.currecy"/>");		
		YAHOO.util.Event.addListener("currencyPanel", "click", displayAddCurrencyPanel, submitCurrencyPanel, true);
	</security:authorize>
		
</script>
