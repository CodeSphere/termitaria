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

<input id="utilizationCase" type="hidden" value="${CASE}"/>

<c:choose>
	<c:when test="${ERRORS != null}">
		<div id="localMessages">
			<table cellpadding="0" cellspacing="0">
				<c:forEach var="error" items="${ERRORS}" >
					<tr>
						<td>
							<font class="error_msg">${error}</font>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:when>
	<c:when test="${DEFAULT != null}">
		<select path="moduleId" class="formFieldSearch">
			<option value="-1"><spring:message code="role.choose.org.first" /></option>
		</select>
	</c:when>
	<c:when test="${MODULES != null}">	
		<select name="moduleId" id="roleForm_module" class="formFieldSearch validate-not-first">
			<option value="-1"><spring:message code="role.choose.module"/></option>
			
			<c:forEach var="module" items="${MODULES}">
				<option value="${module.moduleId}">${module.name}</option>
			</c:forEach>
		</select>
	</c:when>
	<c:otherwise>
		<div id="NO_MODULES">
			<spring:message code="role.no.module"></spring:message>
		</div>
		<div id="MESSAGE">
		</div>
	</c:otherwise>
</c:choose>

<c:if test="${ERRORS eq null}">
	<script>
			var utilizationCase = document.getElementById('utilizationCase').value;
			if ( 'FROM_FORM' == utilizationCase){
				YAHOO.util.Event.addListener('roleForm_module', 'change', function loadPermissions() {
					getContentFromUrlDirect('RolePermissionsForModule.htm?moduleId=' + document.getElementById('roleForm_module').value, 
							'ALL_PERMISSIONS');
				}, null, false); 
				
			} 
		</script>
</c:if>
