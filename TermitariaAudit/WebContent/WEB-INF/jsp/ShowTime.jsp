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
<%@ include file="Taglibs.jsp" %>
<form>
<table>
	<tr>
		<td>
			<select id="hour" onChange="populateTime('hour', 'minutes', ${showAllMinutes}, ${pairNr})" style="width:50px;">
				<c:forEach begin="${start_h}" var="hour" end="${end_h}" items="${hours}">
					<option value="${hour}">${hour}</option>
				</c:forEach>
			</select>
		</td>
		<td>
			<select id="minutes" style="width:50px;">
				<c:forEach begin="${start_m}" var="minute" end="${end_m}" items="${minutes}">
					<option value="${minute}">${minute}</option>
				</c:forEach>
			</select>
		</td>		
		<td>
			<c:choose>				
				<c:when test="${panel eq 'auditOmStartSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditOmEndSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'auditDmStartSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditDmEndSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'auditCmStartSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditCmEndSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'auditTsStartSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditTsEndSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('auditSearchForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'auditOmStartReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditOmEndReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'auditDmStartReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditDmEndReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'auditCmStartReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditCmEndReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'auditTsStartReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'auditTsEndReport'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
			</c:choose>
		</td>
	</tr>
</table>
</form>
<br/>

<script>
	populateTime('hour', 'minutes', ${showAllMinutes}, ${pairNr});
</script>


