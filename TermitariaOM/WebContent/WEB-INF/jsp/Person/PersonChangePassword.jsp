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
<!-- 
	View for Changing a Person's Departments
 -->
<%@ include file="../Taglibs.jsp" %>
<%@ page import="java.util.List" %>

<form:form method="post" commandName="personBean" id="changePasswordForm">
<form:hidden id="personId" path="personId"/>
<table align="center">
<!-- ====================================USERNAME========================================================= -->
	<tr>
		<td class="labelTd">
			<spring:message code="person.username"/>
		</td>
		<td>
			<form:input id="username" path="username" cssClass="required" maxlength="50" readonly="true"/>
		</td>
	</tr>
	<!-- ====================================PASSWORD========================================================= -->
	<tr>
		<td class="labelTd">
			<spring:message code="person.password"/>
		</td>
		<td>
			<form:password showPassword="false" path="password" id="password" cssClass="required" maxlength="100"/>
	</td>
	</tr>
	<tr id="passwordConfirmTd" class="labelTd">
		<td class="labelTd">
			<spring:message code="person.password.confirm"/>
		</td>
		<td>
			<form:password id="passwordConfirmInput" path="passwordConfirm" cssClass="required" maxlength="100"/>
			
		</td>
	</tr>
	<tr><td colspan="3"><div id="change_password_error_message_div" ><spring:message code="person.change.password.notmatch"/></div></td></tr>
</table>
</form:form>
