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
	<c:when test="${clientBean.clientId > 0}">
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
				<a href="#" id="home" ><spring:message code="home" /></a>			
				<a href="#" id="back" onclick="ENTITY = getClientData();check_add('${BACK_URL}', 'MAIN_CONTENT');"><spring:message code="back"/></a>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientAdd}">
					<a href="#" id="addNewClient" title="<spring:message code="client.submenu.add.title"/>"><spring:message code='add'/> </a>
				</security:authorize>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientDelete}">
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="client.submenu.delete.title"/>"><spring:message code='delete'/> </a>
				</security:authorize>
				<a href="#" id="addProject" class="${cssClass}" title="<spring:message code="client.submenu.add.project.title"/>"><spring:message code='client.submenu.add.project.title'/> </a>
				<a href="#" id="listProjects" class="${cssClass}" title="<spring:message code="client.project.listing"/>"><spring:message code='client.project.listing'/> </a>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${clientBean.clientId > 0}">
		<span class="section_title"><img src="images/titles/ClientEdit.jpg"><spring:message code="client.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/ClientAdd.jpg"><spring:message code="client.add.section.title"/></span>		
	</c:otherwise>
</c:choose>


<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
	<br/>
</div>

	<form:form commandName="clientBean" name="clientForm" id="clientForm" onsubmit="return false;">
		<form:hidden path="status" id="status"/>
		<form:hidden path="clientId" id="clientId"/>
		<table cellpadding="3" class="tableAdd">
			<tr><td class="tableAddSpacer">&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="3">
						<tr>
							<!-- ====================================TYPE====================================================== -->
							<td class="labelTd" style="width:100px">
								<spring:message code="client.type"/>
							</td>
							<td style="width:250px">
								<form:select path="type" id="clientForm_type" tabindex="1" onchange="displayPersonOrFirmClientFields(this.options[selectedIndex].value, 'firmClientInfo', 'personClientInfo')">
									<c:forEach var="type" items="${CLIENT_TYPE}">
										<c:choose>
											<c:when test="${(clientBean.clientId > 0) && (type.value == clientBean.type)}">
												<option title='<spring:message code="${type.label}"/>' value="${type.value}" SELECTED><spring:message code="${type.label}"/></option>
											</c:when>
											<c:otherwise>
												<option title='<spring:message code="${type.label}"/>' value="${type.value}"><spring:message code="${type.label}"/></option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</form:select>
							</td>
						</tr>
												
						<tr>
							<!-- ====================================C_NAME==================================================== -->
							<td class="labelTd firmClientInfo" style="width:155px">
								<spring:message code="client.firm.c_name"/><span id="required">*</span>
							</td>
							<td class="firmClientInfo" style="width:250px">
								<form:input id="clientForm_c_name" path="c_name" tabindex="2" maxlength="100" cssClass="formField required firmClientInfo" onkeypress="return restrictSpecialCharacters(event);"/>
								<div id="c_name_message"></div>
			 				</td>

							<!-- ====================================P_FIRST_NAME================================================ -->
			 				<td class="labelTd personClientInfo" style="display:none; width:155px">
			 					<spring:message code="client.person.p_firstName"/><span id="required">*</span>
			 				</td>
			 				<td style="display:none; width:250px" class="personClientInfo">
			 					<form:input id="clientForm_p_firstName" path="p_firstName" tabindex="2" maxlength="100" cssClass="formField required personClientInfo" onkeypress="return restrictSpecialCharactersForName(event);"/>
			 					<div id="p_firstName_message"></div>
			 				</td>	
						</tr>
												
						<tr>
							<!-- ====================================C_CUI===================================================== -->
			 				<td class="labelTd firmClientInfo" />
			 					<spring:message code="client.firm.c_cui"/><span id="required">*</span>
			 				</td>
			 				<td class="firmClientInfo">
			 					<form:input id="clientForm_c_cui" path="c_cui" tabindex="3" maxlength="70" cssClass="formField required firmClientInfo" onkeypress="return restrictCharactersForIdentificationNumbers(event);"/>
			 					<div id="c_cui_message"></div>
			 				</td>
 				
 							<!-- ====================================P_LAST_NAME================================================ -->
			 				<td class="labelTd personClientInfo" style="display:none">
			 					<spring:message code="client.person.p_lastName"/><span id="required">*</span>
			 				</td>
			 				<td style="display:none" class="personClientInfo">
			 					<form:input id="clientForm_p_lastName" path="p_lastName" tabindex="3" maxlength="100" cssClass="formField required personClientInfo" onkeypress="return restrictSpecialCharactersForName(event);"/>
			 					<div id="p_lastName_message"></div>
			 				</td>
						</tr>
						
						<tr>						
			 				<!-- ====================================P_SEX================================================ -->
			 				<td class="labelTd personClientInfo" style="display:none">
			 					<spring:message code="client.person.p_sex"/>
			 				</td>
			 				<td colspan="2" style="display:none" class="personClientInfo">
			 					<font class="labelTd"><spring:message code="client.person.p_sex.m" /></font>
			 						<form:radiobutton path="p_sex" value="M" tabindex="4" id="clientForm_p_sex_m" cssClass="personClientInfo"/> 
								<font class="labelTd"><spring:message code="client.person.p_sex.f" /></font>
									<form:radiobutton path="p_sex" value="F" tabindex="4" id="clientForm_p_sex_f" cssClass="personClientInfo"/> 
			 				</td>
						</tr>
						
						<tr>
							<!-- ====================================C_IBAN================================================ -->
							<td class="labelTd firmClientInfo" />
			 					<spring:message code="client.firm.c_iban"/><span id="required">*</span>
			 				</td>
			 				<td class="firmClientInfo">
			 					<form:input id="clientForm_c_iban" path="c_iban" tabindex="5" cssClass="formField required firmClientInfo" maxlength="70" onkeypress="return restrictCharactersForAccount(event);"/>
			 					<div id="c_iban_message"></div>
			 				</td>
			 				
			 				<!-- ====================================P_BIRTHDATE================================================ -->
							<td class="labelTd personClientInfo" style="display:none">
			 					<spring:message code="client.person.p_birthdate"/>
			 				</td>
			 				<td colspan="2" style="display:none" class="personClientInfo"> 																
								<form:select path="year" items="${Years}" tabindex="5" id="clientForm_p_birthdate_year" cssClass="personClientInfo" onchange="PERSON_BIRTH_YEAR = this.options[selectedIndex].value; javascript:getContentFromUrlDirect('ClientGetNumberOfDays.htm?month='+ PERSON_BIRTH_MONTH+'&year='+ this.value, 'DAYS');"/>
								<form:select path="month" items="${Months}" id="clientForm_p_birthdate_month" itemLabel="monthName" itemValue="monthId" cssClass="personClientInfo" tabindex="6" onchange="PERSON_BIRTH_MONTH = this.options[selectedIndex].value; javascript:getContentFromUrlDirect('ClientGetNumberOfDays.htm?month='+this.value+'&year='+ PERSON_BIRTH_YEAR, 'DAYS');"/>							
								<div id="DAYS" style="display:inline">
									<form:select path="day" items="${Days}" id="clientForm_p_birthdate_day" tabindex="7" cssClass="personClientInfo"/> 
								</div>	
									 
							</td> 
						
						</tr>
																	
						<tr>
							<!-- ====================================PHONE===================================================== -->	
							<td class="labelTd">
								<spring:message code="client.phone"/><span id="required">*</span>
							</td>
							<td>
								<form:input id="clientForm_phone" path="phone" tabindex="8" cssClass="formField required" maxlength="30" onkeypress="return restrictCharactersForPhone(event);"/>
								<div id="phone_message"></div>
							</td>
						</tr>
						
						<tr>
							<!-- ====================================EMAIL================================================ -->
							<td class="labelTd">
								<spring:message code="client.email"/><span id="required">*</span>
							</td>
							<td>
								<form:input id="clientForm_email" path="email" tabindex="9" cssClass="formField required validate-email" maxlength="50"/>
								<div id="email_message"></div>
							</td>
						</tr>	
																					
						<tr>
							<!-- ====================================ADDRESS=================================================== -->
							<td class="labelTd">
								<spring:message code="client.address"/><span id="required">*</span>
							</td>
							<td>
								<form:textarea id="clientForm_address" path="address" tabindex="10" cssClass="required formField" onkeypress="return restrictCharactersForAddress(event);"/>
								<div id="address_message"></div>
								<div class="remainingCharactersAddress" style="display:none" >
									<div id="showRemainingCharactersAddress" />
								</div>
							</td>
						</tr>
						
						<tr>
							<!-- ====================================C_LOCATION=================================================== -->	
							<td class="labelTd firmClientInfo" "/>
			 					<spring:message code="client.firm.c_location"/><span id="required">*</span>
			 				</td>
			 				
			 				<td class="firmClientInfo">
								<form:textarea path="c_location" id="clientForm_c_location" tabindex="11" cssClass="formField required firmClientInfo" onkeypress="return restrictCharactersForAddress(event);"/>
								<div id="c_location_message"></div>
								<div class="remainingCharactersClientLocation" style="display:none" >
									<div id="showRemainingCharactersLocation"/>
								</div>
							</td>
						</tr>
						
						
					</table>	
				</td>
			
				<td>
					<table cellpadding="3">
						
						<tr>
							<!-- ====================================C_CAPITAL================================================ -->
							<td class="labelTd firmClientInfo" />
			 					<spring:message code="client.firm.c_capital"/>
			 				</td>
			 				<td class="firmClientInfo">
			 					<form:input id="clientForm_c_capital" path="c_capital" tabindex="12" maxlength="30" cssClass="formField firmClientInfo" onkeypress="return restrictCharactersForCapital(event);"/>
			 				</td>
			 				
						</tr>
						
						
						<tr>
							<!-- ====================================FAX================================================ -->
							<td class="labelTd">
								<spring:message code="client.fax"/>
							</td>
							<td>
								<form:input id="clientForm_fax" path="fax" tabindex="13" cssClass="formField" maxlength="30" onkeypress="return restrictCharactersForPhone(event);"/>
							</td>
						</tr>
					
						<tr>
							<!-- ====================================DESCRIPTION=================================================== -->
							<td class="labelTd">
								<spring:message code="client.description"/>
							</td>
							<td style="width:250px">
								<form:textarea path="description" id="clientForm_description" tabindex="14" cols="40" rows="3" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
								<div class="remainingCharactersClientDesc" style="display:none" >
									<div id="showRemainingCharactersDescription"/>
								</div>
							</td>
						</tr>
						
						<!-- ====================================OBSERVATION=================================================== -->
						<tr>
							<td class="labelTd">
								<spring:message code="client.observation"/>
							</td>
							<td style="width:250px">
								<form:textarea path="observation" id="clientForm_observation" tabindex="15" cols="40" rows="3" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
								<div class="remainingCharactersClientObs" style="display:none" >
									<div id="showRemainingCharactersObservation"/>
								</div>
							</td>
						</tr>																							
					</table>
				</td>
			</tr>
	
			<tr>
				<td colspan="2" class="formActions" align="right">
						<input type="button" class="button" id="save" tabindex="16" value="<spring:message code="save"/>"/>						
						<input type="button" class="button" id="cancel" tabindex="17" value="<spring:message code="cancel"/>" onclick="ENTITY = getClientData();check_add('${BACK_URL}', 'MAIN_CONTENT');"/>						
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
	getObject = new ObjSubmit("ClientSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
	document.clientForm.type.focus();
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.CM_ClientAdd}, {PERMISSION_CONSTANT.CM_ClientUpdate}">	
		var submitObject = new ObjSubmitSaveClient("ClientForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "clientForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitSaveClient, submitObject, true);
	</security:authorize>

	//----------------------------------------------- DELETE CLIENT ------------------------------------------------
	<c:if test="${clientBean.clientId > 0}">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientDelete}">
			submitObject = new ObjSubmitWithConfirmation("ClientSearch.htm?ACTION=DELETE_FROM_CLIENT_FORM&clientId=${clientBean.clientId}", "", "MAIN_CONTENT", '<spring:message code="client.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);
		</security:authorize>

		//----------------------------------------ADD PROJECT------------------------------- 	
		var submitObject = new ObjSubmit("Project.htm?ACTION=ADD_PROJECT_FROM_CLIENT_FORM&clientId=${clientBean.clientId}&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addProject", "click", getContentFromUrl, submitObject, true);	
	
		//----------------------------------------LIST PROJECT------------------------------- 	
		<c:choose>
			<c:when test="${clientBean.type == 1}">
				var submitObject = new ObjSubmit("ProjectSearch.htm?ACTION=LIST_PROJECTS_FROM_CLIENT&clientId=${clientBean.clientId}&clientName=${clientBean.c_name}","", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("listProjects", "click", getContentFromUrl, submitObject, true);														
			</c:when>
			<c:otherwise>
				var submitObject = new ObjSubmit("ProjectSearch.htm?ACTION=LIST_PROJECTS_FROM_CLIENT&clientId=${clientBean.clientId}&clientName=${clientBean.p_firstName} ${clientBean.p_lastName}","", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("listProjects", "click", getContentFromUrl, submitObject, true);															
			</c:otherwise>
		</c:choose>		
	</c:if>		
	
	

	//----------------------------------------------- ADD NEW CLIENT -----------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ClientAdd}">
		var	getObject = new ObjSubmit("ClientForm.htm?ACTION=ADD&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewClient", "click", getContentFromUrl, getObject, true);
	</security:authorize>		

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS -----------------------
	var validateObservationField = new ObjFieldRemaining("clientForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("clientForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("clientForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("clientForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);	

	var validateLocationField = new ObjFieldRemaining("clientForm_c_location", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersLocation');
	YAHOO.util.Event.addListener("clientForm_c_location", "focus", showInfoOnClickInField, validateLocationField, true);
	YAHOO.util.Event.addListener("clientForm_c_location", "blur", hideInfoOnBlurInField, validateLocationField, true);
	YAHOO.util.Event.addListener("clientForm_c_location", "keyup", showInfoOnKeyUpInField, validateLocationField, true);

	var validateAddressField = new ObjFieldRemaining("clientForm_address", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersAddress');
	YAHOO.util.Event.addListener("clientForm_address", "focus", showInfoOnClickInField, validateAddressField, true);
	YAHOO.util.Event.addListener("clientForm_address", "blur", hideInfoOnBlurInField, validateAddressField, true);
	YAHOO.util.Event.addListener("clientForm_address", "keyup", showInfoOnKeyUpInField, validateAddressField, true);

	var validateDescriptionField = new ObjFieldRemaining("clientForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersDescription');
	YAHOO.util.Event.addListener("clientForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("clientForm_description", "blur", hideInfoOnBlurInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("clientForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);	
	
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS -----------------------
	
	//----------------------------------- ORGANISATION ENTER EVENT ON ALL FIELDS---------------------------------------
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.CM_ClientAdd}, {PERMISSION_CONSTANT.CM_ClientUpdate}">
		var keyListner = null; 
		var submitObject = new ObjSubmitSaveClient("ClientForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "clientForm", "MAIN_CONTENT");
	
		keyListener = new YAHOO.util.KeyListener("clientForm_c_name", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_p_firstName", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_p_lastName", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_phone" , { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_fax", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_email", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_c_cui", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_c_iban", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("clientForm_c_capital", { keys:13 },  { fn:submitSaveClient, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();	
	</security:authorize>

	OLD_ENTITY = getClientData();	

	displayPersonOrFirmClientFields(${clientBean.type}, 'firmClientInfo', 'personClientInfo');	
	
	<c:if test="${CLIENT_TYPE_PERSON == clientBean.type}">
		PERSON_BIRTH_YEAR = ${CURRENT_YEAR};
		PERSON_BIRTH_MONTH = ${CURRENT_MONTH};	
		PERSON_BIRTH_DAY = ${CURRENT_DAY};		
	
		getContentFromUrlDirect('ClientGetNumberOfDays.htm?month='+ PERSON_BIRTH_MONTH+'&year='+ PERSON_BIRTH_YEAR+'&day='+PERSON_BIRTH_DAY, 'DAYS');
	</c:if>	
</script>

			
