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

<td>
	
	<c:choose>
		<c:when test="${empty subdepartmentList}">
			&nbsp <div class="noSearchResults"><spring:message code="department.no.subdepartments"/></div>
		</c:when>
		<c:otherwise>
			<fieldset style="width: 360px;">
				<legend><spring:message code="subdepartment.title.listing" /></legend>
				<br/>
					<table class="list_results" >
					<tr class="list_results_header">
						<th style="width: 200px;"><spring:message code="department.name"/></th>
						<th style="width: 160px;"><spring:message code="department.manager.name"/></th>
					</tr>
					<c:set var="i" value="0"></c:set>
					<c:set var="cssClass" value=""/>
					<c:forEach var="dept" items="${subdepartmentList}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>
						<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">
							<td>${dept.name}</td>
							<td>${dept.manager.firstName} ${dept.manager.lastName}</td>
						</tr>
					</c:forEach>
				</table>
			</fieldset>
		</c:otherwise>
	</c:choose>

</td>
