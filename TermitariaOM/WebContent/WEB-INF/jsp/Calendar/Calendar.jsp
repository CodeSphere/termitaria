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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_FreedayAdd}">
					<a href="#" title="<spring:message code="calendar.submeniu.addAFreeDay.title"/>" onClick="manageFreeday('FreeDay.htm?action=add&calendarId=${calendarBean.calendarId}', '<spring:message code="freeday.new.title"/>');"/><spring:message code="freeday.new"/></a>
				</security:authorize>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/Calendar.jpg"/><spring:message code="calendar.section.title"/></span>

<%@ include file="../Messages.jsp" %>

<form:form commandName="calendarBean" id="calendarForm" name="calendarForm" onsubmit="return false;">

	<spring:bind path="calendarId">
		<input type="hidden" name="${status.expression}" value="${status.value}"/>
	</spring:bind>
		<table cellpadding="3" class="tableAdd">
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>
		<!-- STARTWORK -->
		<tr>
			<td class="labelTd">
				<spring:message code="calendar.startwork"/><span id="required">*</span>
			</td>
			<td>
				<form:input path="startWork" id="calendarForm_startwork" readonly="true" tabindex="1" cssClass="formField required"/>
			</td>	
		</tr>
		<!-- ENDWORK -->
		<tr>
			<td class="labelTd">
				<spring:message code="calendar.endwork"/><span id="required">*</span>
			</td>
			<td>
				<form:input path="endWork" id="calendarForm_endwork" readonly="true" tabindex="2" cssClass="formField required"/>
			</td>
		</tr>
		<!-- OBSERVATION -->
		<tr>
			<td class="labelTd">
				<spring:message code="calendar.observation"/>
			</td>
			<td style="width:10">
				<form:textarea path="observation" tabindex="3" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>	
				<div id="showRemainingCharacters" style="display:none" /> 			
			</td>
		</tr>
		<tr>
			<td colspan="2" class="formActions" align="right">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_CalendarUpdate}">					
					<input type="button" class="button" id="save" tabindex="4" value="<spring:message code="calendar.save"/>"/>												
				</security:authorize>
			</td>
		</tr>			
	</table>
	
</form:form>

	<br/>
	<script>			
	</script>
	<div id="FREEDAYS">		
	
	</div>
	
	
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

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_CalendarUpdate}">

		document.calendarForm.startWork.focus();
	
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION --------------------------
		var validateObservationField = new ObjFieldRemaining("observation", '<spring:message code="observation.message"/>', 2000);
		YAHOO.util.Event.addListener("observation", "focus", showInfoOnClickInField, validateObservationField, true);
		YAHOO.util.Event.addListener("observation", "blur", hideInfoOnBlurInField);
		YAHOO.util.Event.addListener("observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
		//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ------------------------------------------
			
		// ------------------------------------ VALIDATION AND SUBMIT --------------------------------
		
		var submitObject = new ObjSubmit("Calendar.htm?action=save", "calendarForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitValidateAndSaveForm, submitObject, true);		
	
		//---------------------------------- END VALIDATION AND SUBMIT -------------------------------			
		
		//---------------------------------- LIST THE FREEDAYS FOR THE CALENDAR ----------------------
		// freedays listing				
		getContentFromUrlDirect('FreeDayListing.htm?action=get&calendarId=${calendarBean.calendarId}','FREEDAYS');
		
		//----------------------------------------- CALENDAR ENTER EVENT ---------------------------------------
	
		var keyListener = null;
		var submitObject = new ObjSubmit("Calendar.htm?action=save", "calendarForm", "MAIN_CONTENT");
		keyListener = new YAHOO.util.KeyListener("calendarForm_startwork", { keys:13 },  { fn:submitValidateAndSaveForm, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("calendarForm_endwork", { keys:13 },  { fn:submitValidateAndSaveForm, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();

		// Time panel	
		instantiateCalendarStartWork('<spring:message code="calendar.time"/>');
		instantiateCalendarEndWork('<spring:message code="calendar.time"/>');	
		
	</security:authorize>

</script>



