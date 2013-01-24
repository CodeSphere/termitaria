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
<%int random = (int) (Math.random() * 10000 % 1000); %>
<div id="CLIENT_LOGO_CONTENT">	
         	<img src="<spring:theme code="siad"/>" style="position:absolute; right:220px; top:10px; margin-right: 40px;" />
</div>
<div id="userbox">
	<div id="organisationName">
	 	<c:choose>
			<c:when test="${SESS_ORGANISATION_NAME ne null and SESS_ORGANISATION_ID ne null}">
				${SESS_ORGANISATION_NAME}
			</c:when>
		</c:choose>
	</div>
	<div id="userDetails">
		<security:authentication property="principal.firstName"/> <security:authentication property="principal.lastName"/>
	</div> 
		
	<div id="clock"></div>
</div>

<script>
	var clock = new Clock('clock', '${locale}' , true, false);
	setInterval("clock.update()", 1000);
</script>
	
