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

<table>
	<tr>		
		<td>
			<select id="hour" onChange="populateMinutesOnChangeHour()" style="width:50px;">
				<c:forEach begin="${start_h}" var="hour" items="${hours}" end="${end_h}">
					<option value="${hour}">${hour}</option>
				</c:forEach>
			</select>
		</td>
		<td>
			:
		</td>
		<td>
			<select id="minutes" style="width:50px;">
				<c:forEach begin="${start_m}" var="minute" items="${minutes}" end="${end_m}">
					<option value="${minute}">${minute}</option>
				</c:forEach>
			</select>
		</td>
		<td></td><td></td>
		<td>
			<c:choose>
				<c:when test="${panel eq 'start'}">
					<input type="button" class="buttonTime" onclick="hideCalendarStartWorkTimePanel();" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'end'}">		
					<input type="button" class="buttonTime" onclick="hideCalendarEndWorkTimePanel();" value="<spring:message code="ok"/>"/>
				</c:when>				
			</c:choose>
		</td>
	</tr>		
</table>

