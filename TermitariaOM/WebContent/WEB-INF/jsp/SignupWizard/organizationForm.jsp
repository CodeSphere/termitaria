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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ include file="../Taglibs.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="/Termitaria/OM/js/cs/cs_formValidateAndSubmit.js" type="text/javascript"></script>
<title>Signup wizard</title>
</head>
<body> 

<%@ include file="../NoAuthHeader.jsp" %> 

<c:choose>
	<c:when test="${organisationBean.organisationId ge 0}">
		<c:set var="cssClass" value=""/> 
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>	
	</c:otherwise>
</c:choose>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->
    
<span class="section_title"><img src="images/titles/OrganizationAdd.jpg"/><spring:message code="organisation.section.title"/></span>

	<form:form method="post" action="${flowExecutionUrl }" commandName="organisationBean" id="organisationForm" name="organisationForm" >

	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
	
		<spring:bind path="organisationId">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>
		<table cellpadding="3" class="tableAdd">
			<tr><td class="tableAddSpacer">&nbsp;</td></tr>
			<!-- ====================================NAME====================================================== -->
			<tr>
				<td class="labelTd" width="120"> 
					<spring:message code="organisation.name"/> <span id="required">*</span> 
				</td>
				<td width="230">
					<form:input path="name" id="organisationForm_name" tabindex="1" cssClass="formField required" maxlength="100" onkeypress="return restrictSpecialCharacters(event);" onblur="verifyUniqueness('OrganisationVerifyNameUniqueness.htm?198341='+this.value, 'organisationForm_name', 'organisationForm_name_unique', this.value, null, '${organisationBean.name}', null );" />
						<div id="organisationForm_nameDIV2" style="display: inline; color: red;"><form:errors path="name"/></div> 
				</td>
				
			</tr>	
			<!-- ====================================ADDRESS=================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.address"/><span id="required">*</span>
				</td>
				<td>
					<form:textarea path="address" id="organisationForm_address" tabindex="2" rows="3" cssClass="required formField" onkeypress="return restrictCharactersForAddress(event);"/>
					<div id="organisationForm_addressDIV" style="display: inline; color: red;"><form:errors path="address"/></div>
					<div class="remainingCharactersAddress" style="display:none" >
						<div id="showRemainingCharactersAddress" />
					</div>											
				</td>				
			</tr>
			<!-- ====================================PHONE===================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.phone"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="phone" id="organisationForm_phone" tabindex="3" cssClass="formField required" maxlength="30" onkeypress="return restrictCharactersForPhone(event);"/>
					<div id="organisationForm_phoneDIV" style="display: inline; color: red;"><form:errors path="phone"/></div>
				</td>
			</tr>
			
			<!-- ====================================FAX======================================================= -->			
			<tr>			
				<td class="labelTd">
					<spring:message code="organisation.fax"/>
				</td>
				<td>
					<form:input path="fax" id="organisationForm_fax" tabindex="4" cssClass="formField" maxlength="30" onkeypress="return restrictCharactersForPhone(event);"/>
				</td>
			<!-- ====================================CUI======================================================= -->
				<td class="labelTd">				
					<spring:message code="organisation.cui"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="cui" id="organisationForm_cui" maxlength="70" tabindex="8" cssClass="formField required" onkeypress="return restrictCharactersForIdentificationNumbers(event);"/>
					<div id="organisationForm_cuiDIV2" style="display: inline; color: red;"><form:errors path="cui"/></div> 
				</td>
			</tr>
			<!-- ====================================EMAIL===================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.email"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="email" id="organisationForm_email" tabindex="5" cssClass="formField required validate-email" maxlength="50" />
					<div id="organisationForm_emailDIV2" style="display: inline; color: red;"><form:errors path="email"/></div> 
				</td>		
			<!-- ====================================CAPITAL=================================================== -->
				<td class="labelTd">
					<spring:message code="organisation.capital"/>
				</td>
				<td>
					<form:input path="capital" id="organisationForm_capital" tabindex="9" cssClass="formField " maxlength="30" onkeypress="return restrictCharactersForCapital(event);"/>
				</td>				
			</tr>			
			<tr>	
			<!-- ====================================IBAN====================================================== -->
				<td class="labelTd">
					<spring:message code="organisation.iban"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="iban" id="organisationForm_iban" tabindex="6" cssClass="formField required" maxlength="70" onkeypress="return restrictCharactersForAccount(event);"/>
					<div id="organisationForm_ibanDIV2" style="display: inline; color: red;"><form:errors path="iban"/></div> 
				</td>		
			<!-- ====================================J========================================================= -->			
				<td class="labelTd">			
					<spring:message code="organisation.j"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="j" id="organisationForm_j" maxlength="70" tabindex="10" cssClass="formField required" onkeypress="return restrictCharactersForIdentificationNumbers(event);"/>
					<div id="organisationForm_jDIV2" style="display: inline; color: red;"><form:errors path="j"/></div> 
				</td>					
			</tr>
			<!-- ====================================LOCATION================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.location"/><span id="required">*</span>
				</td>
				<td>
					<form:textarea path="location" id="organisationForm_location" cols="40" rows="3" tabindex="7" cssClass="formField required" onkeypress="return restrictCharactersForAddress(event);"/>
					<div id="organisationForm_locationDIV" style="display: inline; color: red;"><form:errors path="location"/></div>
					<div class="remainingCharactersOrgLocation" style="display:none" >
						<div id="showRemainingCharactersLocation"/>
					</div>
				</td>
			<!-- ====================================OBSERVATION=============================================== -->
				<td class="labelTd">
					<spring:message code="organisation.observation"/>
				</td>
				<td>
					<form:textarea path="observation" id="organisationForm_observation" tabindex="11" cols="40" rows="3" cssClass="formField "/>
					<div class="remainingCharactersOrgObs" style="display:none" >
						<div id="showRemainingCharacters"/>
					</div>
				</td>
			</tr>
			<!-- ====================================TYPE=============================================== -->
			<c:choose>
				<c:when test="${organisationBean.organisationId gt 0}">
					<form:hidden path="type" id="organisationForm_type"/>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${organisationBean.parentId <= 0}">						
							<tr>
								<td class="labelTd"> 
									<spring:message code="organisation.select.type" /><span id="required">*</span>
								</td>	
								<td>			
									<div title='<spring:message code="organisation.choose.type"/>' id="organisationFormTypesId">			
										<form:select path="type" id="organisationForm_type" tabindex="12" cssClass="formField validate-not-first" onchange="changeTitle(this.options[selectedIndex].text, 'organisationFormTypesId')">
											<form:option value="-1"><spring:message code="organisation.choose.type"></spring:message></form:option>
												 			
													<c:forEach var="type" items="${ORGANISATION_TYPE}">
														<!-- not for branch -->
														<c:if test="${type.value != 4}">
															<c:choose>
																<c:when test="${type.value == organisationBean.type}">
																	<option title='<spring:message code="${type.label}"/>'  value="${type.value}" SELECTED><spring:message code="${type.label}"/></option>
																</c:when>
																<c:otherwise>
																	<option title='<spring:message code="${type.label}"/>' value="${type.value}"><spring:message code="${type.label}"/></option>
																</c:otherwise>
															</c:choose>	
														</c:if>						
													</c:forEach>
										</form:select>	
									</div>	
									<div id="organisationForm_typeDIV" style="display: inline; color: red;"><form:errors path="type"/></div>
								</td>										
							</tr>	
						</c:when>
						<c:otherwise>
							<c:choose>												
								<c:when test="${organisationBean.type == 0}">	
								<tr>
									<td class="labelTd">
										<spring:message code="organisation.select.type" />
									</td>	
									<td>
										<div title='<spring:message code="organisation.choose.type"/>' id="organisationFormTypesId">
											<form:select path="type" id="organisationForm_type" tabindex="12" cssClass="formField validate-not-first" onchange="changeTitle(this.options[selectedIndex].text, 'organisationFormTypesId')">
												<form:option value="-1"><spring:message code="organisation.choose.type"></spring:message></form:option>
												 	
														<c:forEach var="type" items="${GROUP_COMPANIES_TYPE}">
															<c:choose>
																<c:when test="${type.value == organisationBean.type}">
																	<option title='<spring:message code="${type.label}"/>'  value="${type.value}" SELECTED><spring:message code="${type.label}"/></option>
																</c:when>
																<c:otherwise>
																	<option title='<spring:message code="${type.label}"/>'  value="${type.value}"><spring:message code="${type.label}"/></option>
																</c:otherwise>
															</c:choose>							
														</c:forEach>
													 
											</form:select>
										</div>
										<div id="organisationForm_typeDIV" style="display: inline; color: red;"><form:errors path="type"/></div>
									</td>
								</tr>
								</c:when>
								<c:otherwise>	
									<form:hidden path="type" id="organisationForm_type"/>
								</c:otherwise>	
							</c:choose>
						</c:otherwise>		
					</c:choose>
				</c:otherwise>
			</c:choose>			
			<!-- ====================================PARENT=============================================== -->		
			<tr>
				<c:choose>
					<c:when test="${organisationBean.parentId > 0}">
						<td class="labelTd">
							<spring:message code="organisation.parent" />&nbsp;&nbsp;
						</td>	
						<form:hidden path="parentId" id="organisationForm_parentId"/>																	
						<td>	
							${PARENT}																														
						</td>	
					</c:when>	
				</c:choose>	
			</tr>				
						
			<!-- ====================================MODULES=================================================== -->		
					<tr><td colspan="4" height="20">&nbsp;</td></tr>
			 		<tr>
						<td class="labelTd"> 
							<spring:message code="organisation.list.modules"/><span id="required">*</span>
						</td>	
								
						<td colspan="3">
							<table style="border: 1px solid #d7d7d7; width:550px !important; ">
								<tr><td style="line-height:10px;">&nbsp;</td></tr>
								<tr>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									
									<td id="allModules">
										<div class="selectTitle"><spring:message code="organisation.select.all.modules"/></div>
										<br/>
										<select id="changeModulesForm_select1" name="changeModulesForm_select1" multiple="multiple" size="10" tabindex="13" class="formField">
											<c:forEach var="module" items="${ALL_MODULES}">
												<option title="${module.name}" id="${module.moduleId}" value="${module.moduleId}">${module.name}</option>
											</c:forEach>
										</select>
									</td>
									
									<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/>
										<div class="addButton" tabindex="14" title="<spring:message code="organisation.change.modules.addModule"/>" onClick="moveSelectOptionsSourceDest('changeModulesForm_select1', 'changeModulesForm_select2', true)"></div><br/><br/>
										<div class="removeButton" tabindex="15" title="<spring:message code="organisation.change.modules.removeModule"/>" onClick="moveSelectOptionsSourceDest('changeModulesForm_select2', 'changeModulesForm_select1', true)"></div>
									</td>
									
									<td> 													
										<div class="selectTitle"><spring:message code="organisation.select.current.modules"/></div>
										<br/>													
										<form:select id="changeModulesForm_select2" path="moduleIDs" name="moduleIDs" multiple="true" size="10" tabindex="16" class="formField validate-length">
											<c:forEach var="module" items="${organisationBean.modules}">
												<form:option title="${module.name}" id="${module.moduleId}" value="${module.moduleId}">${module.name}</form:option>
											</c:forEach>
										</form:select>
									</td>							
									
									<td width="100%">&nbsp;</td>
								</tr>
								<tr><td style="line-height:10px;">&nbsp;</td></tr>
							</table>
						</td>
					</tr>
			<tr>
				<td colspan="5" class="formActions" align="right">
						<input type="submit" name="_eventId_save" style="visibility:hidden" id="submitButton" class="button" tabindex="17" value="<spring:message code="save"/>"/>					
						<input type="button" onclick="validateOrgForm();" class="button" id="save" tabindex="17" value="<spring:message code="save"/>"/>						
						<input type="submit" name="_eventId_cancel" class="button" id="cancel" tabindex="18" value="<spring:message code="cancel"/>" />						
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

<script>

function validateOrgForm(){
	selectAll('changeModulesForm_select2');
	validateAndSubmitForm2('organisationForm', 'submitButton' );
}
document.onkeypress = stopRKey;

var validateObservationField = new ObjFieldRemaining("organisationForm_observation", '<spring:message code="observation.message"/>', 2000);
YAHOO.util.Event.addListener("organisationForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
YAHOO.util.Event.addListener("organisationForm_observation", "blur", hideInfoOnBlurInField);
YAHOO.util.Event.addListener("organisationForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);	

var validateLocationField = new ObjFieldRemaining("organisationForm_location", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersLocation');
YAHOO.util.Event.addListener("organisationForm_location", "focus", showInfoOnClickInField, validateLocationField, true);
YAHOO.util.Event.addListener("organisationForm_location", "blur", hideInfoOnBlurInField, validateLocationField, true);
YAHOO.util.Event.addListener("organisationForm_location", "keyup", showInfoOnKeyUpInField, validateLocationField, true);


var validateAddressField = new ObjFieldRemaining("organisationForm_address", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersAddress');
YAHOO.util.Event.addListener("organisationForm_address", "focus", showInfoOnClickInField, validateAddressField, true);
YAHOO.util.Event.addListener("organisationForm_address", "blur", hideInfoOnBlurInField, validateAddressField, true);
YAHOO.util.Event.addListener("organisationForm_address", "keyup", showInfoOnKeyUpInField, validateAddressField, true);	

</script>
</body>
<%@ include file="../Footer.jsp" %>
</html>
