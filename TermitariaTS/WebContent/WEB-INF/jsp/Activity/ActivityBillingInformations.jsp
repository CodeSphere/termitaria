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
	<c:when test="${GET_FROM_PANEL eq true}">
		<c:set var="j" value="Panel"/>
	</c:when>
	<c:otherwise>
		<c:set var="j" value=""/>
	</c:otherwise>
	</c:choose>
	<!-- ====================================BILLABLE========================================================= -->	
<table>
 <tr>
	<td class="labelTd" style="width: 180px">
		<spring:message code="activity.billable" />
	</td>	
	<td > 
		<c:choose>
			<c:when test="${GET_FROM_PANEL eq true}">
				<font class="labelTd"><spring:message code="billable.yes" /></font> 
					<input type="radio" name="billable" value="Y" tabindex="4" id="activityFormPanel_billable" checked/> 	
				<font class="labelTd"><spring:message code="billable.no" /></font>
					<input type="radio" name="billable" value="N" tabindex="3" id="activityFormPanel_billable"/> 				
			</c:when>
			<c:otherwise>
				<font class="labelTd"><spring:message code="billable.yes" /></font> 
					<input type="radio" name="billable" value="Y" tabindex="4" id="activityForm_billable" checked/> 
				<font class="labelTd"><spring:message code="billable.no" /></font>
					<input type="radio" name="billable" value="N" tabindex="3" id="activityForm_billable"/> 						
			</c:otherwise>
		</c:choose>		
	</td>	
</tr>							
	<!-- ====================================BILLING PRICE=================================================== -->	
<tr>
	<td class="labelTd">
		<spring:message code="activity.billingPrice"/> 
	</td>
	<td>
		<c:choose>
			<c:when test="${GET_FROM_PANEL eq true}">
				<input name="billingPrice" id="activityFormPanel_billingPrice" tabindex="8" class="formField " onkeypress="return restrictCharactersForReal(event);"/>
			</c:when>
			<c:otherwise>
				<input name="billingPrice" id="activityForm_billingPrice" tabindex="8" class="formField " onkeypress="return restrictCharactersForReal(event);"/>	
			</c:otherwise>
		</c:choose>			
	</td>	
</tr>

	<!-- ====================================BILLING PRICE CURRENCY=================================================== -->
<tr>
	<td class="labelTd">
		<spring:message code="activity.billingPriceCurrency"/> <span id="billingPriceCurrencyRequiredId${j}" class="hiddenRequiredAsterix">*</span>
	</td>				
	<td>
		<c:choose>
			<c:when test="${GET_FROM_PANEL eq true}">
				<select name="billingPriceCurrencyId" id="activityFormPanel_billingPriceCurrency" class="formField" tabindex="9">
					<option value="-1" title='<spring:message code="activity.choose.billing.price.currency"/>'><spring:message code="activity.choose.billing.price.currency"/></option>
					<c:forEach var="currency" items="${ORG_CURRENCIES}">
						<c:choose>
							<c:when test="${currency.value == activityBean.billingPriceCurrencyId}">
								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
							</c:when>
							<c:otherwise>
								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
			 	</select>
			</c:when>
			<c:otherwise>
				<select name="billingPriceCurrencyId" id="activityForm_billingPriceCurrency" class="formField" tabindex="9">
					<option value="-1" title='<spring:message code="activity.choose.billing.price.currency"/>'><spring:message code="activity.choose.billing.price.currency"/></option>
					<c:forEach var="currency" items="${ORG_CURRENCIES}">
						<c:choose>
							<c:when test="${currency.value == activityBean.billingPriceCurrencyId}">
								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
							</c:when>
							<c:otherwise>
								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
			 	</select>
			</c:otherwise>
		</c:choose>				
	</td>	
</tr>
		
	<!-- ====================================BILLING TIME UNIT=================================================== -->
<tr>
	<td class="labelTd">
		<spring:message code="activity.billingTimeUnit"/> <span id="billingTimeUnitRequiredId${j}" class="hiddenRequiredAsterix">*</span>
	</td>		
	<td>		
		<c:choose>
			<c:when test="${GET_FROM_PANEL eq true}">
				<div title='<spring:message code="activity.choose.billingTimeUnit"/>' id="activityFormPanelBillingTimeUnitsId">			
					<select name="billingTimeUnit" id="activityFormPanel_billingTimeUnit" tabindex="10" class="formField" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormPanelBillingTimeUnitsId')">
						<option value="-1"><spring:message code="activity.choose.billingTimeUnit"></spring:message></option>									
							<c:forEach var="billingTimeUnit" items="${NOM_TIME_UNIT}">																
								<c:choose>
									<c:when test="${billingTimeUnit.value == activityBean.billingTimeUnit}">
										<option title='<spring:message code="${billingTimeUnit.label}"/>'  value="${billingTimeUnit.value}" SELECTED><spring:message code="${billingTimeUnit.label}"/></option>
									</c:when>
									<c:otherwise>
										<option title='<spring:message code="${billingTimeUnit.label}"/>' value="${billingTimeUnit.value}"><spring:message code="${billingTimeUnit.label}"/></option>
									</c:otherwise>
								</c:choose>									
							</c:forEach>							
					</select>	
				</div>			
			</c:when>
			<c:otherwise>
				<div title='<spring:message code="activity.choose.billingTimeUnit"/>' id="activityFormBillingTimeUnitsId">			
					<select name="billingTimeUnit" id="activityForm_billingTimeUnit" tabindex="10" class="formField" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormBillingTimeUnitsId')">
						<option value="-1"><spring:message code="activity.choose.billingTimeUnit"></spring:message></option>									
							<c:forEach var="billingTimeUnit" items="${NOM_TIME_UNIT}">																
								<c:choose>
									<c:when test="${billingTimeUnit.value == activityBean.billingTimeUnit}">
										<option title='<spring:message code="${billingTimeUnit.label}"/>'  value="${billingTimeUnit.value}" SELECTED><spring:message code="${billingTimeUnit.label}"/></option>
									</c:when>
									<c:otherwise>
										<option title='<spring:message code="${billingTimeUnit.label}"/>' value="${billingTimeUnit.value}"><spring:message code="${billingTimeUnit.label}"/></option>
									</c:otherwise>
								</c:choose>									
							</c:forEach>							
					</select>	
				</div>				
			</c:otherwise>
		</c:choose>																
	</td>		
</tr>
</table>	
	<script>

		<c:choose>
			<c:when test="${GET_FROM_PANEL eq true}">
				var validateBillingPriceFieldPanel = new ObjFieldPrice("activityFormPanel_billingPrice", 'billingPriceCurrencyRequiredIdPanel', 'billingTimeUnitRequiredIdPanel', 'activityFormPanel_billingPriceCurrency', 'activityFormPanel_billingTimeUnit');	
				YAHOO.util.Event.addListener("activityFormPanel_billingPrice", "blur", hideOnBlurInField, validateBillingPriceFieldPanel, true);	
			</c:when>
			<c:otherwise>
				var validateBillingPriceField = new ObjFieldPrice("activityForm_billingPrice", 'billingPriceCurrencyRequiredId', 'billingTimeUnitRequiredId', 'activityForm_billingPriceCurrency', 'activityForm_billingTimeUnit');	
				YAHOO.util.Event.addListener("activityForm_billingPrice", "blur", hideOnBlurInField, validateBillingPriceField, true);
			</c:otherwise>
		</c:choose>
		
	</script>
