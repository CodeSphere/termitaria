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
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<c:set var="language" value="<%= RequestContextUtils.getLocale(request).getLanguage() %>" />
<!--  LIST PERMISSIONSS --> 		
<table>	
	<tr>
		<td>
			<table style="border: 1px solid #d7d7d7; width:500px;">
				<tr><td style="line-height:10px;">&nbsp;</td></tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					
					<td id="allModules">
						<div class="selectTitle">
										<spring:message code="role.select.all.permissions"/>
										<c:choose>
												<c:when test ="${SELECTED_MODULE_NAME != null}">
													${SELECTED_MODULE_NAME}
												</c:when>
												<c:otherwise>
													<spring:message code="role.select.all.module"/>
												</c:otherwise>
										</c:choose>
						</div>
						<br/>
						<select id="changeRolesPermissionsForm_select1" name="changeRolesPermissionsForm_select1" tabindex="9" multiple="multiple" size="10" style="width: 200px;" >
							<c:forEach var="permission" items="${ALL_PERMISSIONS}">
								<c:choose>
									<c:when test="${language == 'en'}">
										<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
									</c:when>
									<c:when test="${language == 'ro'}">
										<option title="${permission.description.ro}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
									</c:when>
									<c:otherwise>
										<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</td>
																			
					<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
						<div class="addButton" tabindex="7" title="<spring:message code="role.addPermission"/>" onClick="moveSelectOptionsSourceDest('changeRolesPermissionsForm_select1', 'changeRolesPermissionsForm_select2'); "></div><br/><br/>
						<div class="removeButton" tabindex="8"  title="<spring:message code="role.removePermission"/>" onClick="moveSelectOptionsSourceDest('changeRolesPermissionsForm_select2', 'changeRolesPermissionsForm_select1', true); "></div>
					</td>
					
					<td>
						<div class="selectTitle"><spring:message code="role.select.current.permissions"/></div>
						<br/>
						<select id="changeRolesPermissionsForm_select2" name="permissions" tabindex="6" multiple="multiple" size="10" style="width: 200px;" >
							<c:forEach var="permission" items="${PERMISSIONS}">
								<c:choose>
										<c:when test="${language == 'en'}">  
										<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
									</c:when>
									<c:when test="${language == 'ro'}">
										<option title="${permission.description.ro}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
									</c:when>
									<c:otherwise>
										<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
									</c:otherwise>	 
								</c:choose>
							</c:forEach>
						</select>
					</td>
					
					<td width="100%">&nbsp;</td>
				</tr>
				<tr><td style="line-height:10px;">&nbsp;</td></tr>
			</table>
		</td>		
	</tr>
</table>
