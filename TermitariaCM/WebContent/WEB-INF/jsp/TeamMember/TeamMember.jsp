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

<form:form commandName="teamMemberBean" id="personForm" name="personForm">
<form:hidden path="memberId" id="memberId"/>

<table cellpadding="3" class="tableAdd">
	<tr><td class="tableAddSpacer">&nbsp;</td></tr>	
	
		<!-- ====================================FIRST NAME======================================================= -->	
		<tr> 
			<td class="labelTd"> 
		 		<spring:message	code="person.firstName" /><span id="required">*</span>
			</td> 
			<td>				
				<form:input id="personForm_firstName" path="firstName" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);" /> 						
			</td>
		</tr>
		
		<!-- ====================================LAST NAME======================================================== -->
		<tr> 
			<td class="labelTd"> 
				<spring:message	code="person.lastName" /><span id="required">*</span> 
			</td> 
			<td colspan="2">						
				<form:input id="personForm_lastName" path="lastName" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);" /> 						
			</td> 
		</tr> 
		
		<!-- ====================================EMAIL============================================================ -->
		
		<tr> 
			<td class="labelTd"> 
				<spring:message code="person.email" />
			</td> 
			<td colspan="2"> 
				<form:input id="personForm_email" path="email" maxlength="50" cssClass="formField validate-email"/> 
			</td> 
		</tr> 
			
		
		<!-- ====================================PHONE============================================================ -->	
		<tr> 
			<td class="labelTd"> 
				<spring:message code="person.phone" />
			</td> 
			<td colspan="2"> 
				<form:input id="personForm_phone" path="phone" maxlength="30" cssClass="formField" onkeypress="return restrictCharactersForPhone(event);" /> 
			</td> 
		</tr> 
		
		<!-- ====================================ADDRESS=================================================== -->
		<tr>
			<td class="labelTd">
				<spring:message code="person.address"/>
			</td>
			<td>
				<form:textarea path="address" id="personForm_address" cssClass="formField" onkeypress="return restrictCharactersForAddress(event);"/>
				<div class="remainingCharactersPersonAddress" style="display:none" >
					<div id="showRemainingCharactersPersonAddress" />						
				</div> 															
			</td>				
		</tr>
		
		<!-- ====================================DESCRIPTION====================================================== -->
		<tr>
			<td class="labelTd">
				<spring:message code="person.description"/>
			</td>
			<td>					
				<form:textarea path="description" id="personForm_description" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
				<div class="remainingCharactersPersonDescription" style="display:none" >
					<div id="showRemainingCharactersPersonDescription" />						
				</div> 
			</td>
		</tr>
						
	 	<!-- ====================================OBSERVATION====================================================== -->
		<tr>
			<td class="labelTd">
				<spring:message code="person.observation"/>
			</td>
			<td>
				<form:textarea path="observation" id="personForm_observation" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
				<div class="remainingCharactersPersonObs" style="display:none" >
					<div id="showRemainingCharactersPersonObservation" />			
				</div>				
			</td>
		</tr>
				
		<tr>
		<td class="formActions" align="right" colspan="2">
			<c:choose>
                <c:when test="${teamMemberBean.memberId > 0}">
                    <input type="button" class="button" id="saveExternal" value="<spring:message code="save"/>" onclick="submitAddTeamMember('personForm', 'TEAM_MEMBERS', '${ACTION}', true, ${projectId}, ${teamMemberBean.memberId});"/>
                </c:when>
                <c:otherwise>
                    <input type="button" class="button" id="saveExternal" value="<spring:message code="save"/>" onclick="submitAddTeamMember('personForm', 'TEAM_MEMBERS', '${ACTION}', false, ${projectId}, ${teamMemberBean.memberId});"/>
                </c:otherwise>   
            </c:choose>    																		 
 		</td>
 	</tr>			 	 			 	
</table>
</form:form>


<script>

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
	var validateDescriptionField = new ObjFieldRemaining("personForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersPersonDescription');
	YAHOO.util.Event.addListener("personForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("personForm_description", "blur", hideInfoOnBlurInField,validateDescriptionField, true);
	YAHOO.util.Event.addListener("personForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);
	
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
	var validateObservationField = new ObjFieldRemaining("personForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersPersonObservation');
	YAHOO.util.Event.addListener("personForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("personForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("personForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR ADDRESS -----------------------
	var validateAddressField = new ObjFieldRemaining("personForm_address", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersPersonAddress');
	YAHOO.util.Event.addListener("personForm_address", "focus", showInfoOnClickInField, validateAddressField, true);
	YAHOO.util.Event.addListener("personForm_address", "blur", hideInfoOnBlurInField, validateAddressField, true);
	YAHOO.util.Event.addListener("personForm_address", "keyup", showInfoOnKeyUpInField, validateAddressField, true);
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
	
	//----------------------------------------- TEAM MEMBER ENTER EVENT ---------------------------------------	
</script>
