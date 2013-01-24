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

<c:if test="${IS_MANAGER_FOR_PROJECT}">
							
	<td colspan="2">
		<font class="labelTd"><spring:message code="billable.yes" /></font>
			<input type="radio" name="billable" value="Y" tabindex="3" id="activitySearchForm_billable_yes"/> 
		<font class="labelTd"><spring:message code="billable.no" /></font>
			<input type="radio" name="billable" value="N" tabindex="4" id="activitySearchForm_billable_no"/> 
	</td>																							
		
</c:if>

<script>
	<c:choose>
		<c:when test="${IS_MANAGER_FOR_PROJECT}">
			document.getElementById('billableId').style.display='block';
		</c:when>
		<c:otherwise>
			document.getElementById('billableId').style.display='none';
		</c:otherwise>
	</c:choose>
</script>
