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

<c:if test="${GET_FROM_PANEL eq null}">

	<c:choose>
		<c:when test="${currencyBean.currencyId > 0}">
			<c:set var="cssClass" value=""/>
		</c:when>
		<c:otherwise>
			<c:set var="cssClass" value="inactive"/>		
		</c:otherwise>
	</c:choose>
	
	<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
	<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td id="submenuCell">
				<div id="submenu">
					<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->		
					<a href="#" id="back" onclick="ENTITY = getCurrencyData();check_add('${BACK_URL}', 'MAIN_CONTENT');"><spring:message code="back"/></a>
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">
						<a href="#" id="addNewCurrency" title="<spring:message code="currency.submenu.add.title"/>"><spring:message code='add'/></a>
					</security:authorize>
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyDelete}">
						<a href="#" id="delete" class="${cssClass}" title="<spring:message code="currency.submenu.delete.title"/>"><spring:message code='delete'/> </a>
					</security:authorize>
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencySearch}">
						<a href="#" id="submenuSearch" title="<spring:message code="search"/>"><spring:message code="search"/></a>
					</security:authorize>
				</div>
				<div id="submenu_footer"></div>
	        </td>
	        <td id="contentCell">
	        	<div id="CONTENT">
	<!-- //////////////////////////////////////////////////////////////////////////// -->
	
	<c:choose>
		<c:when test="${currencyBean.currencyId > 0}">
			<span class="section_title"><img src="images/titles/CurrencyEdit.jpg"><spring:message code="currency.update.section.title"/></span>
		</c:when>
		<c:otherwise>
			<span class="section_title"><img src="images/titles/CurrencyAdd.jpg"><spring:message code="currency.add.section.title"/></span>		
		</c:otherwise>
	</c:choose>

</c:if>

<c:choose>
	<c:when test="${GET_FROM_PANEL eq true}">
		<div id="MESSAGES">
			<%@ include file="../MessagesPanel.jsp" %>
		</div>
	</c:when>
	<c:otherwise>
		<div id="MESSAGES">
			<%@ include file="../Messages.jsp" %>
		</div>
	</c:otherwise>
</c:choose>

<c:if test="${GET_FROM_PANEL eq true and  ONSUBMIT eq true}">
	<form:form name="currencyFormResult" id="currencyFormResult" commandName="currencyBean" onsubmit="return false;" >	
		<form:hidden id="CURRENCYFORM_ID" path="currencyId" />
		<form:hidden id="CURRENCYFORM_NAME" path="name"/>
		<form:hidden path="currencyId" />		
	</form:form>
</c:if>

<c:if test="${GET_FROM_PANEL eq null or ONSUBMIT eq null}">	
	<c:choose>
		<c:when test="${GET_FROM_PANEL eq true}">
			<c:set var="i" value="Panel"/>
		</c:when>
		<c:otherwise>
			<c:set var="i" value=""/>
		</c:otherwise>
	</c:choose>
	<form:form commandName="currencyBean" name="currencyForm${i}" id="currencyForm${i}" onsubmit="return false;">
		<form:hidden path="currencyId" id="currencyId"/>		
			<c:choose>
				<c:when test="${GET_FROM_PANEL eq null}">
					<table cellpadding="3" class="tableAdd">				
				</c:when>
				<c:otherwise>
					<table cellpadding="3" class="tableAdd">
				</c:otherwise>
			</c:choose>
			<tr><td class="tableAddSpacer">&nbsp;</td>
			</tr>
			<tr>
				<td class="labelTd" valign="top">
					<spring:message code="currency.name"/><span id="required">*</span>
				</td>
				<td>
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">
							<form:input path="name" id="currencyFormPanel_name" tabindex="1" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);"/>					
						</c:when>
						<c:otherwise>
							<form:input path="name" id="currencyForm_name" tabindex="1" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);"/>													
						</c:otherwise>
					</c:choose>											
					<div id="name_message"><form:errors path="name" cssClass="validationErrorMessage"/></div>
				</td>			
			</tr>
			
			<tr>
				<td class="labelTd" valign="top">
					<spring:message code="currency.initials"/><span id="required">*</span>
				</td>
				<td>
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">
							<form:input path="initials" id="currencyFormPanel_initials" tabindex="2" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);"/>
						</c:when>
						<c:otherwise>
							<form:input path="initials" id="currencyForm_initials" tabindex="2" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);"/>							
						</c:otherwise>
					</c:choose>						
					<div id="initials_message"><form:errors path="initials" cssClass="validationErrorMessage"/></div>
				</td>			
			</tr>
			
			<tr>
				<td colspan="3" class="formActions" align="right">																																		
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">						
							<input type="button" class="button" id="saveCurrencyPanel" tabindex="3" value="<spring:message code="save"/>"/>							
						</c:when>
						<c:otherwise>
							<input type="button" class="button" id="save" tabindex="3" value="<spring:message code="save"/>"/>							
							<input type="button" class="button" id="cancel" onclick="ENTITY = getCurrencyData();check_add('${BACK_URL}', 'MAIN_CONTENT');" tabindex="4" value="<spring:message code="cancel"/>" />							
						</c:otherwise>
					</c:choose>														
				</td>
			</tr>	
			
		</table>
		<br/>
	</form:form>
</c:if>


<c:if test="${GET_FROM_PANEL eq null}">

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->
	
<script type="text/javascript">	
	document.currencyForm${i}.name.focus();

	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">
		//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
		var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "currencyForm", "MAIN_CONTENT", "currencyForm_name", "currencyForm_initials");
		YAHOO.util.Event.addListener("save", "click", submitSaveCurrency, submitObject, true);

		//----------------------------------- ENTER EVENT ON ALL FIELDS---------------------------------------
		var keyListner = null; 
		
		keyListener = new YAHOO.util.KeyListener("currencyForm_name", { keys:13 },  { fn:submitSaveCurrency, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();

		keyListener = new YAHOO.util.KeyListener("currencyForm_initials", { keys:13 },  { fn:submitSaveCurrency, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
	</security:authorize>		

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyDelete}">
		//----------------------------------------------- DELETE CURRENCY ------------------------------------------------
		<c:if test="${currencyBean.currencyId > 0}">
			submitObject = new ObjSubmitWithConfirmation("CurrencySearch.htm?ACTION=DELETE_FROM_CURRENCY_FORM&currencyId=${currencyBean.currencyId}", "", "MAIN_CONTENT", '<spring:message code="currency.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);
		</c:if>	
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">
		//----------------------------------------------- ADD NEW CURRENCY -----------------------------------------------
		var	getObject = new ObjSubmit("CurrencyForm.htm?ACTION=ADD&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewCurrency", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	//-------------------- OLD CURRENCY ENTITY ----------------------------------------------------------
	OLD_ENTITY = getCurrencyData();

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencySearch}">
		//-------------------- SEARCH CURRENCY --------------------------------------------------------------
		var getObject = new ObjSubmit("CurrencySearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);
	</security:authorize>	
	
</script>
</c:if>

	<c:if test="${GET_FROM_PANEL eq null or ONSUBMIT eq null}">
		<c:if test="${GET_FROM_PANEL eq true}">
			<script>	
				document.currencyForm${i}.name.focus();	
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">			

					<c:choose>						
						<c:when test="${FROM_EXCHANGE eq true}">
							//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
							var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "currencyFormPanel", "LOCAL_MESSAGES_CONTENT", "currencyFormPanel_name", "currencyFormPanel_initials", "exchangeForm_firstCurrency", "exchangeForm_secondCurrency");
							YAHOO.util.Event.addListener("saveCurrencyPanel", "click", submitAndValidateCurrencyPanelForm, submitObject, true);
						</c:when>
						
						<c:when test="${FROM_COST_SHEET eq true}">
							//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
							var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "currencyFormPanel", "LOCAL_MESSAGES_CONTENT", "currencyFormPanel_name", "currencyFormPanel_initials", "costSheetForm_costPriceCurrency", "costSheetForm_billingPriceCurrency");
							YAHOO.util.Event.addListener("saveCurrencyPanel", "click", submitAndValidateCurrencyPanelForm, submitObject, true);
						</c:when>
						
						<c:when test="${FROM_TEAM_MEMBER_DETAIL eq true}">
							//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
							var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "currencyFormPanel", "LOCAL_MESSAGES_CONTENT", "currencyFormPanel_name", "currencyFormPanel_initials", "teamMemberDetailForm_costPriceCurrency", "teamMemberDetailForm_billingPriceCurrency", "teamMemberDetailForm_overtimeCostCurrency", "teamMemberDetailForm_overtimeBillingCurrency");
							YAHOO.util.Event.addListener("saveCurrencyPanel", "click", submitAndValidateCurrencyPanelForm, submitObject, true);
						</c:when>
						
						<c:when test="${FROM_PERSON_DETAIL eq true}">
							//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
							var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "currencyFormPanel", "LOCAL_MESSAGES_CONTENT", "currencyFormPanel_name", "currencyFormPanel_initials", "personDetailForm_costPriceCurrency", "personDetailForm_overtimeCostCurrency");
							YAHOO.util.Event.addListener("saveCurrencyPanel", "click", submitAndValidateCurrencyPanelForm, submitObject, true);
						</c:when>
									
						<c:when test="${FROM_PROJECT_DETAIL eq true}">
							//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
							var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "currencyFormPanel", "LOCAL_MESSAGES_CONTENT", "currencyFormPanel_name", "currencyFormPanel_initials", "projectDetailsForm_budgetCurrency");
							YAHOO.util.Event.addListener("saveCurrencyPanel", "click", submitAndValidateCurrencyPanelForm, submitObject, true);
						</c:when>

						<c:when test="${FROM_ACTIVITY_PANEL eq true}">
							//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
							var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}&FROM_ACTIVITY_PANEL=true", "currencyFormPanel", "LOCAL_MESSAGES_CONTENT", "currencyFormPanel_name", "currencyFormPanel_initials", "activityFormPanel_costPriceCurrency", "activityFormPanel_billingPriceCurrency");
							YAHOO.util.Event.addListener("saveCurrencyPanel", "click", submitAndValidateCurrencyPanelForm, submitObject, true);
						</c:when>
															
						<c:otherwise>							
							//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
							var submitObject = new ObjSubmitCurrency("CurrencyForm.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "currencyFormPanel", "LOCAL_MESSAGES_CONTENT", "currencyFormPanel_name", "currencyFormPanel_initials", "activityForm_costPriceCurrency", "activityForm_billingPriceCurrency");
							YAHOO.util.Event.addListener("saveCurrencyPanel", "click", submitAndValidateCurrencyPanelForm, submitObject, true);
						</c:otherwise>
					</c:choose>			
						
											
					//----------------------------------- ENTER EVENT ON ALL FIELDS---------------------------------------
					var keyListner = null; 
					
					keyListener = new YAHOO.util.KeyListener("currencyFormPanel_name", { keys:13 },  { fn:submitAndValidateCurrencyPanelForm, scope: submitObject, correctScope:true}, null ); 
					keyListener.enable();
			
					keyListener = new YAHOO.util.KeyListener("currencyFormPanel_initials", { keys:13 },  { fn:submitAndValidateCurrencyPanelForm, scope: submitObject, correctScope:true}, null ); 
					keyListener.enable();
				</security:authorize>	
										
			</script>
		</c:if>
	</c:if>
