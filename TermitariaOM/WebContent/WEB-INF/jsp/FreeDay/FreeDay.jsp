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

<div id="calendarFreedayCalContainer" class=""></div>

<form:form commandName="freedayBean" id="freedayForm" name="freedayForm">
<form:hidden path="freeDayId"/>
<form:hidden path="calendar.calendarId"/>

<table cellpadding="3" class="tableAdd">
	<tr><td class="tableAddSpacer">&nbsp;</td></tr>
	<!-- ====================================FREEDAY======================================================= -->
	<tr>
		<td class="labelTd">
			<spring:message code="freeday.day"/><span id="required">*</span>
		</td>
		
		<td width="100">
		<form:input id="freedayForm_day" path="day" tabindex="1" readonly="true" onclick="instantiateCalendarFreedayCal();" cssClass="formField validate-date"/>
		
		</td>
	</tr>
	
	<!-- ====================================OBSERVATION====================================================== -->
	<tr>
		<td class="labelTd">
			<spring:message code="freeday.observation"/>
		</td>
		
		<td>
			<form:textarea id="fredayForm_observation" path="observation" tabindex="2" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>		
			<div class="remainingCharactersFreeDayObs" style="display:none" >	
				<div id="showRemainingCharacters"/>
			</div>
		</td>
		
	</tr>
	<tr>
		<td class="formActions" align="right" colspan="2">
			<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_FreedayUpdate}, ${PERMISSION_CONSTANT.OM_FreedayAdd}">				
				<input type="button" tabindex="4" class="button" onclick="submitSaveFreeday('FreeDay.htm?action=save', 'freedayForm', 'FREEDAYS', 'freedayForm_day');" id="saveFreeday" value="<spring:message code="save"/>"/>
				<c:if test="${freedayBean.freeDayId > 0 and GET_ACTION == 'true'}">
					<input type="button" tabindex="3" class="button" id="cancel" onClick="hideFreedayPanel();" value="<spring:message code="cancel"/>"> 
				</c:if>	 
	 		</security:authorize>
 		</td>
 	</tr>		
</table>
</form:form>

<script type="text/javascript">	
	// ---------------------------------------CALENDAR--------------------------------------------
	
	//instantiateCalendarFreedayCal();
		
	// ---------------------------------------END CALENDAR--------------------------------------------
	
	// ------------------------------------ VALIDATION AND SUBMIT(NOT WORKING IN IE, SAFARI, CHROME) ---------------
	//
	//var submitObject = new ObjSubmitFreeday("FreeDay.htm?action=save", "freedayForm", "FREEDAYS", 'freedayForm_day');
	//YAHOO.util.Event.addListener("saveFreeday", "click", submitSaveFreeday, submitObject, true);	
	//
	//---------------------------------- END VALIDATION AND SUBMIT -------------------------------
		

	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_FreedayUpdate}, ${PERMISSION_CONSTANT.OM_FreedayAdd}">
		document.freedayForm.day.focus();		
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION --------------------------
		var validateObservationField = new ObjFieldRemaining("observation", '<spring:message code="observation.message"/>', 2000);
		YAHOO.util.Event.addListener("fredayForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
		YAHOO.util.Event.addListener("fredayForm_observation", "blur", hideInfoOnBlurInField);
		YAHOO.util.Event.addListener("fredayForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
		//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ------------------------------------------			
	</security:authorize>			
</script>



