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

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">	
					<a href="#" id="home"><spring:message code="home" /></a>							
					<a href="#" id="back"><spring:message code="back"/></a>																																							
				<a href="#" id="addNewOOO" title="<spring:message code="ooo.submeniu.add.title"/>"><spring:message code="ooo.new"/></a>					
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${oooBean.outOfOfficeId > 0}">
		<span class="section_title"><img src="images/titles/OutOfOfficeEdit.jpg"/><spring:message code="ooo.ooo"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/OutOfOfficeAdd.jpg"/><spring:message code="ooo.ooo"/></span>
	</c:otherwise>
</c:choose>

<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
</div>


	<form:form commandName="oooBean" id="oooForm" onsubmit="return false;">
	<form:hidden path="outOfOfficeId"/>
	<form:hidden path="personReplacementId" id="personReplacementId"/>
	<form:hidden path="personId" id="personId"/>	
	
	<table cellpadding="3" class="tableAdd">
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeAddToAll}">
			<tr>
				<td class="labelTd">
					<spring:message code="ooo.owner"/><span id="required">*</span>
				</td>
				<td width="100">
					<div id="myOwnerAutoComplete" class="autoComplete">			
						<input id="oooForm_owner" type="text" tabindex="1" class="formField"/>
						<div id="ownerAutoCompleteContainer"></div>
					</div>
					<div id="owner_message"></div>
				</td>
			</tr>
		</security:authorize>
		
		<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeAddToAll}">				
			<input id="oooForm_owner" type="hidden" value="<security:authentication property="principal.firstName"/> <security:authentication property="principal.lastName"/>"/>						
		</security:authorize>		
		
		<tr>
			<td class="labelTd">
				<spring:message code="ooo.replacement"/><span id="required">*</span>
			</td>
			<td>
				<div id="myAutoComplete" class="autoComplete">
					<input id="oooForm_replacement" type="text" tabindex="2" class="formField"/>
					<div id="replacementAutoCompleteContainer"></div>
				</div>
				<div id="replacement_message"></div>
			</td>
		</tr>
		<tr>
			<td class="labelTd">
				<spring:message code="ooo.startDate"/><span id="required">*</span>
			</td>
			<td>
				<form:input path="startPeriod" id="oooForm_startPeriod" readonly="true" tabindex="3" cssClass="formField required"/>
			</td>
			<td>
				<div id="oooStartCalContainer"></div>
			</td>
		</tr>
		<tr>
			<td class="labelTd">
				<spring:message code="ooo.endDate"/><span id="required">*</span>
			</td>
			<td>
				<form:input path="endPeriod" id="oooForm_endPeriod" readonly="true" tabindex="4" cssClass="formField required"/>
			</td>
			<td>
				<div id="oooEndCalContainer"></div>
			</td>
		</tr>
		<tr>
			<td class="labelTd">
				<spring:message code="ooo.observation"/>
			</td>
			<td>
				<form:textarea path="observation" id="oooForm_observation" tabindex="5" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
				<div class="remainingCharactersOutOfOfficeObs" style="display:none" >
					<div id="showRemainingCharacters"/> 
				</div>
			</td>
		</tr>
		<tr>
			<td class="formActions" align="right" colspan="2">		
				<input type="button" class="button" id="saveOutOfOffice" tabindex="6" value="<spring:message code="ooo.save"/>"/>			
				<input type="button" class="button" id="cancel" tabindex="7" value="<spring:message code="cancel"/>"/>				
			</td>
		</tr>
	</table>
	
	<br/>

	</form:form>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>

	//------------------------------------- BACK OOO ------------------------------------------------			
	<c:choose>			
		<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewOO0FromSearch || ACTION == BACK_CONSTANT.OM_Back_EditOOOFromSearch}">
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeSearch}">			
				getObject = new ObjSubmit("SearchOutOfOffice.htm", "", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);	
			</security:authorize>		
		</c:when>				
		<c:otherwise>
			<c:choose>			
				<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewOOOFromListForPerson}">							
					getObject = new ObjSubmit("OutOfOfficeListForPerson.htm", "", "MAIN_CONTENT");
					YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);							
				</c:when>	
				<c:otherwise>	
					<c:choose>
						<c:when test="${ACTION == BACK_CONSTANT.OM_Back_EditOOOFromListForPerson}">	
							getObject = new ObjSubmit("OutOfOfficeListForPerson.htm?personId=${oooBean.personId}", "", "MAIN_CONTENT");
							YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);	
						</c:when>
						<c:otherwise>
							getObject = new ObjSubmit("OutOfOffice.htm", "", "MAIN_CONTENT");
							YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
						</c:otherwise>
					</c:choose>																										
				</c:otherwise>	
			</c:choose>					
		</c:otherwise>
	</c:choose>			

	//------------------------------------- CANCEL OOO ------------------------------------------------			
	<c:choose>			
		<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewOO0FromSearch || ACTION == BACK_CONSTANT.OM_Back_EditOOOFromSearch}">							
			getObject = new ObjSubmit("SearchOutOfOffice.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);			
		</c:when>				
		<c:otherwise>
			<c:choose>			
				<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewOOOFromListForPerson}">							
					getObject = new ObjSubmit("OutOfOfficeListForPerson.htm", "", "MAIN_CONTENT");
					YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);							
				</c:when>	
				<c:otherwise>	
					<c:choose>
						<c:when test="${ACTION == BACK_CONSTANT.OM_Back_EditOOOFromListForPerson}">	
							getObject = new ObjSubmit("OutOfOfficeListForPerson.htm?personId=${oooBean.personId}", "", "MAIN_CONTENT");
							YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);	
						</c:when>
						<c:otherwise>
							getObject = new ObjSubmit("OutOfOffice.htm", "", "MAIN_CONTENT");
							YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);
						</c:otherwise>
					</c:choose>																										
				</c:otherwise>	
			</c:choose>					
		</c:otherwise>
	</c:choose>		

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------	
		var validateObservationField = new ObjFieldRemaining("oooForm_observation", '<spring:message code="observation.message"/>', 2000);
		YAHOO.util.Event.addListener("oooForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
		YAHOO.util.Event.addListener("oooForm_observation", "blur", hideInfoOnBlurInField);
		YAHOO.util.Event.addListener("oooForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
	
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS --------------------------------------
			
		var submitObject = new ObjSubmit("OutOfOffice.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewOOO", "click", getContentFromUrl, submitObject, true);

		<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeAddToAll} ">
			<c:if test="${oooBean.personId != null}">
				setPersonNameForID('${JSON_PERSONS}', ${oooBean.personId}, 'oooForm_owner');
			</c:if>
		</security:authorize>

		<c:if test="${oooBean.personReplacementId != -1}">
			setPersonNameForID('${JSON_PERSONS}', ${oooBean.personReplacementId}, 'oooForm_replacement');
		</c:if>	

	// -------------------------------------CALENDAR----------------------------------------------	
		instantiateOOOStartCal('<spring:message code="ooo.time"/>');
		instantiateOOOEndCal('<spring:message code="ooo.time"/>');		

	instantiatePersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), 'oooForm_replacement', 'replacementAutoCompleteContainer', 'personReplacementId', false);		
	// -------------------------------------AUTOCOMPLETE------------------------------------------
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeAddToAll} ">
		instantiatePersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), 'oooForm_owner', 'ownerAutoCompleteContainer', 'personId', true);
	</security:authorize>

	//--------------------------------------LISTENERS---------------------------------------------		
	<c:choose>
		<c:when test="${ADD == 'true'}">								
			YAHOO.util.Event.addListener("saveOutOfOffice", "click", validateAndSubmitOOOForm, {hasPermission:"true", url:"OutOfOffice.htm?action=add", formId:"oooForm", container:"MAIN_CONTENT"}, true);			
		</c:when>
		<c:otherwise>				
			YAHOO.util.Event.addListener("saveOutOfOffice", "click", validateAndSubmitOOOForm, {hasPermission:"true", url:"OutOfOffice.htm?action=add", formId:"oooForm", container:"MESSAGES"}, true);			
		</c:otherwise>
	</c:choose>		
	
</script>
