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


<c:if test="${!(empty DEPARTMENT)}">
	
	<table>				
		<!-- NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="department.name"/>
			</td>
			<td>
				 ${DEPARTMENT.name}
			</td>
		</tr>
		
		<!-- MANAGER -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="department.manager.name"/>
			</td>
			<td>
				 ${DEPARTMENT.manager.firstName} ${DEPARTMENT.manager.lastName}	
			</td>
		</tr>
		
		
		<!-- PARENT DEPARTMENT -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="department.parent.department.name"/>
			</td>
			<td>
				<c:if test="${(DEPARTMENT.parentDepartment.name) != '' && DEPARTMENT.parentDepartment.status != 2}">
						${DEPARTMENT.parentDepartment.name}
				</c:if>				
			</td>
		</tr>
		
		<!-- OBSERVATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="department.observation"/>
			</td>
			<td>				
				<c:if test="${(DEPARTMENT.observation != '') && !(empty DEPARTMENT.observation) }">
						${DEPARTMENT.observation}
				</c:if>			
			</td>
		</tr>
		
		<!-- PERSONS -->
		<c:if test="${!(empty DEPARTMENT.persons)}">
			<tr>
				<td class="labelInfoTd">
					<spring:message code="department.list.persons"/>
				</td>
				<td>
					<select id="userGroup_persons" name="userGroup_persons" multiple="multiple" size="10" style="width: 200px;" >				
						<c:forEach var="pers" items="${DEPARTMENT.persons}">
							<option title="${pers.firstName} ${pers.lastName}" id="${pers.personId}">${pers.firstName} ${pers.lastName}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</c:if>					
	</table>	
</c:if>
</div>
