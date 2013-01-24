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
<%@include file="Taglibs.jsp"%>     
<c:set var="display" value="TableCell"/>
<c:if test="${SESS_ORGANISATION_ID eq null}">
		<c:set var="display" value="None"/>
</c:if> 

<img src="<spring:theme code="menu_left"/>" style="position:absolute; left:0px; top:0px" />
	<table id="menuTable" border="0" cellpadding="0" cellspacing="0">
		<tr>
       		<td><img src="<spring:theme code="menu_tab_left"/>"/></td>
       		<td class="first">
		 	 	<a href="#" id="organisations"><spring:message code="organisation.organization"/></a>
	       	</td>
			<td class="menuDisplay${display}">
				<a href="#" id="departments"><spring:message code="department.department"/></a>
			</td>
			<td class="menuDisplay${display}">	
				<a href="#" id="persons"><spring:message code="person.employees"/></a>
			</td>
			<td class="menuDisplay${display}">	
				<a href="#" id="outofoffices"><spring:message code="ooo.ooo"/></a>
			</td>
			<td class="menuDisplay${display}">					
				<a href="#" id="calendars"><spring:message code="calendar.calendar"/></a>				
			</td>
			<td class="menuDisplay${display}">	
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_RoleSearch}">
					<a href="#" id="roles"><spring:message code="role.role"/></a>
				</security:authorize>	
			</td>
			<td class="menuDisplay${display}">	
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PermissionView}">
					<a href="#" id="permissions"><spring:message code="permission.permission"/></a>
				</security:authorize>
			</td>
			<td class="menuDisplay${display}">	
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobSearch}">					
					<a href="#" id="jobs"><spring:message code="job.job"/></a>
				</security:authorize>
			</td>
			<td class="menuDisplay${display}">	
				<a href="#" id="usergroups"><spring:message code="usergroup.usergroup"/></a>
			</td>			
	       	<td><img src="<spring:theme code="menu_tab_right"/>" /></td>
		</tr>
	</table>
	
<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">	
	<script>
		getContentFromUrlDirect('OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}', 'MAIN_CONTENT');
	</script>			
</security:authorize>	
<script>	
		//--------------------------------------------------- < Organisation >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("organisations", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("organisations", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?organisationId=${ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>
	

	//--------------------------------------------------- < Departments >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = { url: "DepartmentSearch.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("departments", "click", getContentFromUrl, parameters, true);			
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = { url: "DepartmentSearch.htm?organisationId=${ORGANISATION_ID}", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("departments", "click", getContentFromUrl, parameters, true);
	</security:authorize>

	
	
	//--------------------------------------------------- < Employees >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		parameters = {url: "PersonSearch.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("persons", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		parameters = {url: "PersonSearch.htm?organisationId=${ORGANISATION_ID}", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("persons", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	
	//--------------------------------------------------- < Out of Office >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = {url: "SearchOutOfOffice.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("outofoffices", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = {url: "OutOfOfficeListForPerson.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("outofoffices", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	
	//--------------------------------------------------- < Calendar >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_CalendarUpdate}">
		var parameters = {url: "Calendar.htm?action=edit", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("calendars", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_CalendarUpdate}">
		var parameters = {url: "CalendarView.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("calendars", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	
	
	//--------------------------------------------------- < Roles >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = {url: "SearchRole.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("roles", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = {url: "SearchRole.htm?organisationId=${ORGANISATION_ID}", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("roles", "click", getContentFromUrl, parameters, true);
	</security:authorize>
		
	//--------------------------------------------------- < Permissions >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = {url: "SearchPermission.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("permissions", "click", getContentFromUrl, parameters, true);	
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		var parameters = {url: "SearchPermission.htm?organisationId=${ORGANISATION_ID}", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("permissions", "click", getContentFromUrl, parameters, true);	
	</security:authorize>

	//--------------------------------------------------- < Jobs > ----------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobSearch}">
		var parameters = {url: "SearchJob.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("jobs", "click", getContentFromUrl, parameters, true);
	</security:authorize>
	
	//--------------------------------------------------- < User Groups >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("usergroups", "click", getContentFromUrl, {url: "SearchUserGroup.htm", container: "MAIN_CONTENT"} , true); 
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("usergroups", "click", getContentFromUrl, {url: "SearchUserGroup.htm?organisationId=${ORGANISATION_ID}",
		 container: "MAIN_CONTENT"} , true);
	</security:authorize>	

</script>

