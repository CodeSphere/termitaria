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

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
        <td id="contentCell">
        	<div id="CONTENT">
        	
<span class="section_title"><img src="images/titles/PersonAdd.jpg"/></span>
	<div id="LOCAL_MESSAGES_CONTENT">
			<%@ include	file="../Messages.jsp"%> 
	</div>
		
<form:form method="post" commandName="personBean" id="personForm" name="personForm" >
		
				<form:hidden id="personId" path="personId" />		
				<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>	
				<table cellpadding="3" class="tableAdd">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<!-- ====================================FIRST NAME======================================================= -->	
				<tr> 
					<td class="labelTd"> 
				 		<spring:message	code="person.firstName" /><span id="required">*</span> 
					</td> 
					<td>				
						<form:input id="personForm_firstName" path="firstName"  tabindex="1" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);" /> 						
						<div id="personForm_firstNameDIV2" style="display: inline; color: red;"><form:errors path="firstName"/></div> 
					</td> 				
 				<!-- ====================================LAST NAME======================================================== -->
				<tr> 
					<td class="labelTd"> 
						<spring:message	code="person.lastName" /><span id="required">*</span> 
					</td> 
					<td colspan="2">						
						<form:input id="personForm_lastName" path="lastName"  tabindex="2" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);" /> 						
						<div id="personForm_lastNameDIV2" style="display: inline; color: red;"><form:errors path="lastName"/></div> 
					</td> 
				</tr> 
				<!-- ====================================EMAIL============================================================ -->
						<tr> 
							<td class="labelTd"> 
								<spring:message code="person.email" /><span id="required">*</span> 
							</td> 
							<td colspan="2">								
								<form:input id="personForm_email" path="email"  tabindex="12" maxlength="50" cssClass="formField required validate-email" />									
									<div id="personForm_emailDIV2" style="display: inline; color: red;"><form:errors path="email"/></div> 
							</td> 
						</tr> 
						<!-- ====================================USERNAME========================================================= -->
						<tr> 
							<td class="labelTd"> 
								<spring:message code="person.username" /><span id="required">*</span> 
							</td> 
							<td> 
								<form:input id="personForm_username" path="username" tabindex="13" maxlength="50" cssClass="formField required personForm_address" onblur="verifyUniqueness('PersonVerifyUsernameUniqueness.htm?198341='+this.value, 'personForm_username', 'personForm_username_unique', this.value, null, null, null)" />
								<div id="personForm_usernameDIV2" style="display: inline; color: red;"><form:errors path="username"/></div> 
							</td> 
						</tr>
						 
						<!-- ====================================PASSWORD========================================================= -->
								<tr> 
									<td class="labelTd"> 
										<spring:message code="person.password" /><span id="required">*</span> 
									</td>
									<td class="passwordGenerateSentByEmail">
										<spring:message code="person.mail.generate" />
									</td>
								</tr>
				 
				<tr>
					<td colspan="5" class="formActions" align="right">
						<input type="submit" name="_eventId_save" style="visibility:hidden" id="submitButton" class="button" tabindex="17" value="<spring:message code="save"/>"/>					
						<input type="button" onclick="validateAndSubmitForm2('personForm', 'submitButton' );" class="button" id="save" tabindex="17" value="<spring:message code="save"/>"/>						
						<input type="submit" name="_eventId_cancel" class="button" id="cancel" tabindex="18" value="<spring:message code="cancel"/>" />						
					</td>
				</tr>
			</table>
			</form:form>	

			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>

<script>
document.onkeypress = stopRKey;
</script>
</body>
<%@ include file="../Footer.jsp" %>
</html>
