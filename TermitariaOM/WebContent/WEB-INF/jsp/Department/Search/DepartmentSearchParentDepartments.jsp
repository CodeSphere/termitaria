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
<%@include file="../../Taglibs.jsp" %>
<c:choose>
	<c:when test="${JSON_DEPARTMENTS ne null}">											
		<input type="text" id="myParentDepartmentInput" class="formFieldSearch"/>			
		<div id="myParentDepartmentContainer"></div>
	</c:when>
	<c:otherwise>
		<input type="text" id="myParentDepartmentInput" disabled="disabled" class="formFieldSearch" value='<spring:message code="person.department.no.results"/>'/>
		<div id="myParentDepartmentContainer"></div>			
	</c:otherwise>
</c:choose>
				
<script>	
	//----------------------------------------AUTOCOMPLETE PARENT DEPARTMENT-------------------------------				
	instantiateParentDepartmentAutoComplete(new YAHOO.util.LocalDataSource(${JSON_DEPARTMENTS}), 'myParentDepartmentInput', 'myParentDepartmentContainer', 'parentDepartmentId');	
</script>
