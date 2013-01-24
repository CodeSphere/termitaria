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
				<c:when test="${panel eq 'recordStartSearch'}">
					<input type="button" class="buttonTime" onclick="hideTimePanel('recordSearchForm_startDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'recordEndSearch'}">
					<input type="button" class="buttonTime" onclick="hideTimePanel('recordSearchForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'recordFormStartWorkHours'}">
					<input type="button" class="buttonTime" onclick="hideTimePanel('recordForm_startTime'); manageWorkHoursPeriod('recordForm_startTime', 'recordForm_endTime', 'recordForm_time')" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'recordFormStartOverTime'}">
					<input type="button" class="buttonTime" onclick="hideTimePanel('recordForm_overTimeStartTime'); manageOvertimeWorkHoursPeriod('recordForm_overTimeStartTime', 'recordForm_overTimeEndTime', 'recordForm_overtimeTime')" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'recordFormEndWorkHours'}">
					<input type="button" class="buttonTime" onclick="hideTimePanel('recordForm_endTime'); manageWorkHoursPeriod('recordForm_startTime', 'recordForm_endTime', 'recordForm_time')" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'recordFormEndOverTime'}">
					<input type="button" class="buttonTime" onclick="hideTimePanel('recordForm_overTimeEndTime'); manageOvertimeWorkHoursPeriod('recordForm_overTimeStartTime', 'recordForm_overTimeEndTime', 'recordForm_overtimeTime')" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'costSheetStartSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('costSheetSearchForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'costSheetEndSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('costSheetSearchForm_endDate');" value="<spring:message code="ok"/>"/>
				</c:when>				
				<c:when test="${panel eq 'reportProjectStartSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportProjectForm_reportStartDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'reportProjectEndSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportProjectForm_reportEndDate');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'reportTimeSheetStart'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportTimeSheetForm_reportStartDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'reportTimeSheetEnd'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('reportTimeSheetForm_reportEndDate');" value="<spring:message code="ok"/>"/>
				</c:when>				
				<c:when test="${panel eq 'costSheetForm'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('costSheetForm_date');" value="<spring:message code="ok"/>"/>
				</c:when>
				<c:when test="${panel eq 'notificationStartSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('searchForm_startDate');" value="<spring:message code="ok"/>"/>											  
				</c:when>
				<c:when test="${panel eq 'notificationEndSearch'}" >
					<input type="button" class="buttonTime"	onclick="hideTimePanel('searchForm_endDate');" value="<spring:message code="ok"/>"/>
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


