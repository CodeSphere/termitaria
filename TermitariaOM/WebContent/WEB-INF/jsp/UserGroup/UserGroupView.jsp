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
<div class="yuiInfoPanel">
<%@ include file="../Taglibs.jsp" %>
<%@ include file="../Messages.jsp" %>

<c:if test="${!(empty USER_GROUP)}">	

	<table>
		<!-- USER GROUP NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="usergroup.name"/>
			</td>
			<td>
				${USER_GROUP.name}
			</td>
		</tr>
		<tr>
			<td class="labelInfoTd">
				<spring:message code="usergroup.description"/>
			</td>
			<td>
				
				${USER_GROUP.description}
			
			</td>
		</tr>
		
		<!-- PERSONS -->
		<c:if test="${!(empty USER_GROUP.persons)}">
			<tr>
				<td class="labelInfoTd">
					<spring:message code="usergroup.persons"/>
				</td>
				<td>
					<select id="changePersonsForm_select1" name="changePersonsForm_select1" multiple="multiple" size="10" tabindex="1" style="width:200px">
						<c:forEach var="pers" items="${USER_GROUP.persons}">
							<option title="${pers.firstName} ${pers.lastName}" id="${pers.personId}">${pers.firstName} ${pers.lastName}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</c:if>	
	</table>
	
</c:if>
</div>
