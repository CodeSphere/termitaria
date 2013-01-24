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

<form method="post" id="changeRolesPermissionsForm">

<input type="hidden" name="roleId" value="${roleId}"/>

<table>
	<tr>
		<td align="center"><spring:message code="role.all.permissions"/></td>
		<td></td>
		<td align="center"><spring:message code="role.permissions"/></td>
	</tr>
	<tr>
		<td>
			<select id="changeRolesPermissionsForm_select1" name="changeRolesPermissionsForm_select1" multiple="multiple" size="10" style="width: 300px;">
				<c:forEach var="perm" items="${ALL_PERMISSIONS}">
					<option id="${perm.permissionId}" value="${perm.permissionId}">
						${perm.name}
					</option>
				</c:forEach>
			</select>
		</td>
		<td>
			<div class="addButton" title="<spring:message code="role.removePermission"/>" onClick="moveSelectOptionsSourceDest('changeRolesPermissionsForm_select2', 'changeRolesPermissionsForm_select1')" />
			<br/><br/>
			<div class="removeButton" title="<spring:message code="role.addPermission"/>" onClick="moveSelectOptionsSourceDest('changeRolesPermissionsForm_select1', 'changeRolesPermissionsForm_select2', true)" />
		</td>
		<td>
			<select id="changeRolesPermissionsForm_select2" name="changeRolesPermissionsForm_select2" multiple="multiple" size="10" style="width: 300px;">
				<c:forEach var="perm" items="${PERMISSIONS}">
					<option id="${perm.permissionId}" value="${perm.permissionId}">
							${perm.name}
					</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="3" align="right">
			<input type="button" onclick="submitChangeRolesPermissions('ChangeRolesPermissions.htm?UPDATE', 'changeRolesPermissionsForm', 'MESSAGES', YAHOO.om.changeRolesPermissionsPanel, 'changeRolesPermissionsForm_select2')"
					value="<spring:message code="role.update.permissions"/>" />
</table>

</form>
