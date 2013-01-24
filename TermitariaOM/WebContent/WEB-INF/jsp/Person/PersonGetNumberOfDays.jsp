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
<%@ include file="../Taglibs.jsp"%>

<select name="day" id="days" tabindex="7">
	<c:forEach var="day" items="${Days}">
		<c:choose>
			<c:when test="${SELECTED_DAY ne null and SELECTED_DAY eq day}">
				<option value="${day}" SELECTED>${day}</option>
			</c:when>
			<c:otherwise>
				<option value="${day}">${day}</option>
			</c:otherwise>
		</c:choose>
		
	</c:forEach>
</select>

