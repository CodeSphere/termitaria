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
	<c:when test="${teamMemberDetailBean.teamMemberDetailId > 0}">
		<c:set var="cssClass" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>		
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${teamMemberDetailBean.costPrice ne null}">
		<c:set var="cssCostClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssCostClass" value="formField"/>		
	</c:otherwise>
</c:choose>	

<c:choose>
	<c:when test="${teamMemberDetailBean.billingPrice ne null}">
		<c:set var="cssBillingClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssBillingClass" value="formField"/>		
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${teamMemberDetailBean.overtimeCostPrice ne null}">
		<c:set var="cssOvertimeCostClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssOvertimeCostClass" value="formField"/>		
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${teamMemberDetailBean.overtimeBillingPrice ne null}">
		<c:set var="cssOvertimeBillingClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssOvertimeBillingClass" value="formField"/>		
	</c:otherwise>
</c:choose>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">				
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->		
				<a href="#" id="back" onclick="ENTITY = getTeamMemberDetailData();check_add('${BACK_URL}&projectId=${projectId}&hasProjectDetail=true', 'MAIN_CONTENT');">
					<spring:message code="back"/>
				</a>									
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${teamMemberDetailBean.teamMemberDetailId > 0}">
		<span class="section_title"><img src="images/titles/TeamMemberDetailEdit.jpg"/><spring:message code="team.member.detail.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/TeamMemberDetailAdd.jpg"/><spring:message code="team.member.detail.add.section.title"/></span>
	</c:otherwise>
</c:choose>


<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include file="../Messages.jsp" %>
</div>

<form:form method="post" commandName="teamMemberDetailBean" id="teamMemberDetailForm" name="teamMemberDetailForm" onsubmit="return false;">
	
	<form:hidden path="teamMemberDetailId" id="teamMemberDetailId" />				
	
	<table class="tableAdd">
		<tr>
			<td width="450px">
				<table cellpadding="3">
		
					<tr><td class="tableAddSpacer" colspan="2">&nbsp;</td></tr>		
					
					<!-- ====================================PROJECT========================================================= -->
					<tr>
						<td class="labelTd">
							<spring:message code="project.details.project"/>
						</td>			
						<td>								
							${PROJECT.name}																										
						</td>
					</tr>
					
					<!-- ====================================TEAM MEMBER========================================================= -->
					<tr>
						<td class="labelTd">
							<spring:message code="team.member.detail.member"/>
						</td>			
						<td>
							<form:hidden path="teamMemberId" id="teamMemberId"/>					
							<input id="teamMemberDetailForm_member" name="name" type="text" value="${TEAM_MEMBER_NAME}" tabindex="1" class="formField" readonly="readonly"/>	
																													
						</td>
					</tr>
																																																											 			
					<!-- ====================================COST PRICE=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="team.member.detail.cost.price"/>
						</td>
						<td>
							<form:input path="costPrice" id="teamMemberDetailForm_costPrice" tabindex="2" cssClass="formField " onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('teamMemberDetailForm_costPrice')"/>
						</td>	
					</tr>
					
					<!-- ====================================COST PRICE CURRENCY=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="team.member.detail.cost.price.currency"/> <c:choose><c:when test="${teamMemberDetailBean.costPrice eq null}"><span id="costPriceCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="costPriceCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
						</td>
						<td>
							<form:select path="costPriceCurrencyId" id="teamMemberDetailForm_costPriceCurrency" tabindex="3" cssClass="${cssCostClass}">
	 							<form:option value="-1" title='<spring:message code="team.member.choose.cost.price.currency"/>'><spring:message code="team.member.choose.cost.price.currency"/></form:option>
			 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
			 						<c:choose>
			 							<c:when test="${currency.value == teamMemberDetailBean.costPriceCurrencyId}">
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
							<spring:message code="team.member.detail.cost.time.unit"/> <c:choose><c:when test="${teamMemberDetailBean.costPrice eq null}"><span id="costTimeUnitRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="costTimeUnitRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
						</td>
						<td>
							<div title='<spring:message code="team.member.choose.costTimeUnit"/>' id="teamMemberDetailFormCostTimeUnitsId">			
								<form:select path="costTimeUnit" id="teamMemberDetailForm_costTimeUnit" tabindex="4" cssClass="${cssCostClass}" onchange="changeTitle(this.options[selectedIndex].text, 'teamMemberDetailFormCostTimeUnitsId')">
									<form:option value="-1"><spring:message code="team.member.choose.costTimeUnit"></spring:message></form:option>									
										<c:forEach var="timeUnit" items="${NOM_TIME_UNIT}">																
											<c:choose>
												<c:when test="${timeUnit.value == teamMemberDetailBean.costTimeUnit}">
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
							
					<!-- ====================================BILLING PRICE=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="team.member.detail.billing.price"/>
						</td>
						<td>
							<form:input path="billingPrice" id="teamMemberDetailForm_billingPrice" tabindex="5" cssClass="formField " onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('teamMemberDetailForm_billingPrice')"/>
						</td>	
					</tr>
					
					<!-- ====================================BILLING PRICE CURRENCY=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="team.member.detail.billing.price.currency"/> <c:choose><c:when test="${teamMemberDetailBean.billingPrice eq null}"><span id="billingPriceCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="billingPriceCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
						</td>
						<td>
							<form:select path="billingPriceCurrencyId" id="teamMemberDetailForm_billingPriceCurrency" tabindex="6" cssClass="${cssBillingClass}" >
	 							<form:option value="-1" title='<spring:message code="team.member.choose.billing.price.currency"/>'><spring:message code="team.member.choose.billing.price.currency"/></form:option>
			 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
			 						<c:choose>
			 							<c:when test="${currency.value == teamMemberDetailBean.billingPriceCurrencyId}">
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
					
					<!-- ====================================BILLING TIME UNIT=================================================== -->
					<tr>			
						<td class="labelTd" width="170px">
							<spring:message code="team.member.detail.billing.time.unit"/> <c:choose><c:when test="${teamMemberDetailBean.billingPrice eq null}"><span id="billingTimeUnitRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="billingTimeUnitRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
						</td>
						<td>
							<div title='<spring:message code="team.member.choose.billingTimeUnit"/>' id="teamMemberDetailFormBillingTimeUnitsId">			
								<form:select path="billingTimeUnit" id="teamMemberDetailForm_billingTimeUnit" tabindex="7" cssClass="${cssBillingClass}" onchange="changeTitle(this.options[selectedIndex].text, 'teamMemberDetailFormBillingTimeUnitsId')">
									<form:option value="-1"><spring:message code="team.member.choose.billingTimeUnit"></spring:message></form:option>									
										<c:forEach var="billingTimeUnit" items="${NOM_TIME_UNIT}">																
											<c:choose>
												<c:when test="${billingTimeUnit.value == teamMemberDetailBean.billingTimeUnit}">
													<option title='<spring:message code="${billingTimeUnit.label}"/>'  value="${billingTimeUnit.value}" SELECTED><spring:message code="${billingTimeUnit.label}"/></option>
												</c:when>
												<c:otherwise>
													<option title='<spring:message code="${billingTimeUnit.label}"/>' value="${billingTimeUnit.value}"><spring:message code="${billingTimeUnit.label}"/></option>
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
							<spring:message code="team.member.detail.observation"/>
						</td>
						<td>
							<form:textarea path="observation" id="teamMemberDetailForm_observation" tabindex="8" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
							<div class="remainingCharactersTeamMemberDetailObs" style="display:none" >
								<div id="showRemainingCharactersObservation" />			
							</div>				
						</td>
					</tr>
																					
				</table>
			</td>	
					
			<!-- =========================================OVERTIME======================================================= -->
			
			<td width="430px">
				<table>
					<tr>
						<td class="tableAddSpacer">&nbsp;</td>
					</tr>
				</table>
				
				<fieldset>
					<legend>
						<spring:message code="team.member.overtime" />
					</legend>
					<table cellpadding="3">					
						<tr>
							<td class="tableAddSpacer" colspan="2">&nbsp;</td>
						</tr>
						
						<!-- ====================================OVERTIME COST PRICE=================================================== -->
						<tr>			
							<td class="labelTd">
								<spring:message code="team.member.detail.overtime.cost.price"/>
							</td>
							<td>
								<form:input path="overtimeCostPrice" id="teamMemberDetailForm_overtimeCostPrice" tabindex="9" cssClass="formField " onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('teamMemberDetailForm_overtimeCostPrice')"/>
							</td>	
						</tr>
						
						<!-- ====================================OVERTIME COST CURRENCY=================================================== -->
						<tr>			
							<td class="labelTd">
								<spring:message code="team.member.detail.overtime.cost.currency"/> <c:choose><c:when test="${teamMemberDetailBean.overtimeCostPrice eq null}"><span id="overtimeCostCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="overtimeCostCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
							</td>
							<td>
								<form:select path="overtimeCostCurrencyId" id="teamMemberDetailForm_overtimeCostCurrency" tabindex="10" cssClass="${cssOvertimeCostClass}">
		 							<form:option value="-1" title='<spring:message code="team.member.choose.overtime.cost.currency"/>'><spring:message code="team.member.choose.overtime.cost.currency"/></form:option>
				 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
				 						<c:choose>
				 							<c:when test="${currency.value == teamMemberDetailBean.overtimeCostCurrencyId}">
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
								<spring:message code="team.member.detail.overtime.cost.time.unit"/> <c:choose><c:when test="${teamMemberDetailBean.overtimeCostPrice eq null}"><span id="overtimeCostTimeUnitRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="overtimeCostTimeUnitRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
							</td>
							<td>
								<div title='<spring:message code="team.member.choose.overtimeCostTimeUnit"/>' id="teamMemberDetailFormOvertimeCostTimeUnitsId">			
									<form:select path="overtimeCostTimeUnit" id="teamMemberDetailForm_overtimeCostTimeUnit" tabindex="11" cssClass="${cssOvertimeCostClass}" onchange="changeTitle(this.options[selectedIndex].text, 'teamMemberDetailFormOvertimeCostTimeUnitsId')">
										<form:option value="-1"><spring:message code="team.member.choose.overtimeCostTimeUnit"></spring:message></form:option>									
											<c:forEach var="timeUnit" items="${NOM_TIME_UNIT}">																
												<c:choose>
													<c:when test="${timeUnit.value == teamMemberDetailBean.overtimeCostTimeUnit}">
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
								
						<!-- ====================================OVERTIME BILLING PRICE=================================================== -->
						<tr>			
							<td class="labelTd">
								<spring:message code="team.member.detail.overtime.billing.price"/>
							</td>
							<td>
								<form:input path="overtimeBillingPrice" id="teamMemberDetailForm_overtimeBillingPrice" tabindex="12" cssClass="formField " onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('teamMemberDetailForm_overtimeBillingPrice')"/>
							</td>	
						</tr>
						
						<!-- ====================================OVERTIME BILLING CURRENCY=================================================== -->
						<tr>			
							<td class="labelTd">
								<spring:message code="team.member.detail.overtime.billing.currency"/> <c:choose><c:when test="${teamMemberDetailBean.overtimeBillingPrice eq null}"><span id="overtimeBillingCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="overtimeBillingCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
							</td>
							<td>
								<form:select path="overtimeBillingCurrencyId" id="teamMemberDetailForm_overtimeBillingCurrency" tabindex="13" cssClass="${cssOvertimeBillingClass}">
		 							<form:option value="-1" title='<spring:message code="team.member.choose.overtime.billing.currency"/>'><spring:message code="team.member.choose.overtime.billing.currency"/></form:option>
				 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
				 						<c:choose>
				 							<c:when test="${currency.value == teamMemberDetailBean.overtimeBillingCurrencyId}">
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
						
						<!-- ====================================OVERTIME BILLING TIME UNIT=================================================== -->
						<tr>			
							<td class="labelTd" width="170px">
								<spring:message code="team.member.detail.overtime.billing.time.unit"/> <c:choose><c:when test="${teamMemberDetailBean.overtimeBillingPrice eq null}"><span id="overtimeBillingTimeUnitRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="overtimeBillingTimeUnitRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
							</td>
							<td>
								<div title='<spring:message code="team.member.choose.overtimeBillingTimeUnit"/>' id="teamMemberDetailFormOvertimeBillingTimeUnitsId">			
									<form:select path="overtimeBillingTimeUnit" id="teamMemberDetailForm_overtimeBillingTimeUnit" tabindex="14" cssClass="${cssOvertimeBillingClass}" onchange="changeTitle(this.options[selectedIndex].text, 'teamMemberDetailFormOvertimeBillingTimeUnitsId')">
										<form:option value="-1"><spring:message code="team.member.choose.overtimeBillingTimeUnit"></spring:message></form:option>									
											<c:forEach var="overtimeBillingTimeUnit" items="${NOM_TIME_UNIT}">																
												<c:choose>
													<c:when test="${overtimeBillingTimeUnit.value == teamMemberDetailBean.overtimeBillingTimeUnit}">
														<option title='<spring:message code="${overtimeBillingTimeUnit.label}"/>'  value="${overtimeBillingTimeUnit.value}" SELECTED><spring:message code="${overtimeBillingTimeUnit.label}"/></option>
													</c:when>
													<c:otherwise>
														<option title='<spring:message code="${overtimeBillingTimeUnit.label}"/>' value="${overtimeBillingTimeUnit.value}"><spring:message code="${overtimeBillingTimeUnit.label}"/></option>
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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">
					<input type="button" class="button" id="save" tabindex="15" value="<spring:message code="save"/>" />
				</security:authorize>	
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">
					<c:if test="${IS_MANAGER eq true}">
						<input type="button" class="button" id="save" tabindex="15" value="<spring:message code="save"/>" />
					</c:if>
				</security:authorize>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
					<input type="button" class="button"  id="currencyPanel" tabindex="16" value="<spring:message code="team.member.detail.add.currecy"/>"/>
				</security:authorize>				
				<input type="button" class="button" id="cancel" onclick="ENTITY = getTeamMemberDetailData();check_add('${BACK_URL}&projectId=${projectId}&hasProjectDetail=true', 'MAIN_CONTENT');" tabindex="17" value="<spring:message code="cancel"/>" />				
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
	document.teamMemberDetailForm.name.focus();	
			
	
	//------------ SAVE TEAM MEMBER DETAIL---------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">		
		var submitObject = new ObjSubmit("TeamMemberDetail.htm?ACTION=SAVE&projectId=${projectId}&BACK_URL=${ENCODE_BACK_URL}", "teamMemberDetailForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitSaveTeamMemberDetail, submitObject, true);		
	</security:authorize>		

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateTeamMemberDetails}">		
		<c:if test="${IS_MANAGER eq true}">
			var submitObject = new ObjSubmit("TeamMemberDetail.htm?ACTION=SAVE&projectId=${projectId}&BACK_URL=${ENCODE_BACK_URL}", "teamMemberDetailForm", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("save", "click", submitSaveTeamMemberDetail, submitObject, true);		
		</c:if>
	</security:authorize>	
			
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
	var validateObservationField = new ObjFieldRemaining("teamMemberDetailForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("teamMemberDetailForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("teamMemberDetailForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("teamMemberDetailForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
	
	var validateCostPriceField = new ObjFieldPrice("teamMemberDetailForm_costPrice", 'costPriceCurrencyRequiredId', 'costTimeUnitRequiredId', 'teamMemberDetailForm_costPriceCurrency', 'teamMemberDetailForm_costTimeUnit');	
	YAHOO.util.Event.addListener("teamMemberDetailForm_costPrice", "blur", hideOnBlurInField, validateCostPriceField, true);	

	var validateBillingPriceField = new ObjFieldPrice("teamMemberDetailForm_billingPrice", 'billingPriceCurrencyRequiredId', 'billingTimeUnitRequiredId', 'teamMemberDetailForm_billingPriceCurrency', 'teamMemberDetailForm_billingTimeUnit');	
	YAHOO.util.Event.addListener("teamMemberDetailForm_billingPrice", "blur", hideOnBlurInField, validateBillingPriceField, true);	

	var validateOvertimeCostPriceField = new ObjFieldPrice("teamMemberDetailForm_overtimeCostPrice", 'overtimeCostCurrencyRequiredId', 'overtimeCostTimeUnitRequiredId', 'teamMemberDetailForm_overtimeCostCurrency', 'teamMemberDetailForm_overtimeCostTimeUnit');	
	YAHOO.util.Event.addListener("teamMemberDetailForm_overtimeCostPrice", "blur", hideOnBlurInField, validateOvertimeCostPriceField, true);	

	var validateOvertimeBillingPriceField = new ObjFieldPrice("teamMemberDetailForm_overtimeBillingPrice", 'overtimeBillingCurrencyRequiredId', 'overtimeBillingTimeUnitRequiredId', 'teamMemberDetailForm_overtimeBillingCurrency', 'teamMemberDetailForm_overtimeBillingTimeUnit');	
	YAHOO.util.Event.addListener("teamMemberDetailForm_overtimeBillingPrice", "blur", hideOnBlurInField, validateOvertimeBillingPriceField, true);
	
	//----------------------------------------- TEAM MEMBER DETAIL ENTER EVENT ---------------------------------------
	
	OLD_ENTITY = getTeamMemberDetailData();

	//----------------------------------------- TEAM MEMBER DETAIL ADD CURRENCY ---------------------------------------	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
		var submitCurrencyPanel = ObjSubmitCurrencyPanel('CurrencyForm.htm?GET_FROM_PANEL=true&FROM_TEAM_MEMBER_DETAIL=true', "<spring:message code="team.member.detail.add.currecy"/>");		
		YAHOO.util.Event.addListener("currencyPanel", "click", displayAddCurrencyPanel, submitCurrencyPanel, true);
	</security:authorize>
		
</script>
