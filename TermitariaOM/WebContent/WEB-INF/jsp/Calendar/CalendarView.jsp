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
<%@ include file="../Messages.jsp" %>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
				<a href="#" id="home"><spring:message code="home" /></a>					
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:if test="${!(empty CALENDAR)}">
	
	<span class="section_title"><img src="images/titles/Calendar.jpg"/><spring:message code="calendar.section.title"/></span>
	<table>
		<!-- STARTWORK -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="calendar.startwork"/>
			</td>			
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp; ${CALENDAR.startWork}
			</td>
		</tr>	
		
		<!-- ENDWORK -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="calendar.endwork"/>
			</td>
			<td>
				&nbsp;&nbsp;&nbsp;&nbsp; ${CALENDAR.endWork}
			</td>
		</tr>
		
		<!-- OBSERVATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="calendar.observation"/>
			</td>
			<td>
				
				&nbsp;&nbsp;&nbsp;&nbsp; ${CALENDAR.observation}
					
			</td>
		</tr>
		
		<!-- FREEDAYS -->			
	</table>
</c:if>

<br/><br/>
<div id="FREEDAYS">		
	
</div>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
	container : "MAIN_CONTENT", withContext : true}, true);

	//---------------------------------- LIST THE FREEDAYS FOR THE CALENDAR ----------------------
	// freedays listing				
	getContentFromUrlDirect('FreeDayListing.htm?action=get&calendarId=${CALENDAR.calendarId}','FREEDAYS');


</script>
