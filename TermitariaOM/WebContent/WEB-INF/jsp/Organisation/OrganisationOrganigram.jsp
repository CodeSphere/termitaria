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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title><spring:message code="title.organigram"/></title>
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet"/>"/>
	<script type="text/javascript" src="js/cs/cs_common.js"></script>
	<script type="text/JavaScript" src="js/cs/cs_organigram.js"></script>
</head>
<body>
<script type="text/javascript" src="js/wz/wz_tooltip.js"></script>
<script type="text/javascript" src="js/wz/wz_jsgraphics.js"></script>
<script type="text/javascript" src="js/wz/tip_followscroll.js"></script>


<div id="OrganigramCanvas" style="position: relative"></div>
<c:choose>
	<c:when test="${JSON_TREE eq null}">
		<br/>
		<br/>
		<div class="organisationOverviewTreeNull"><spring:message code="organigram.nullStructure"/></div>
		<script>
			self.resizeTo(400, 200);
		</script>
	</c:when>
	<c:otherwise>
		<script type="text/JavaScript">
			drawOrganigramCompleete(${JSON_TREE}, '<spring:theme code="organigram.organizationPicture"/>', '<spring:theme code="organigram.departmentPicture"/>',  '<spring:message code="organigram.viewEmployees"/>',  "PDF", '<spring:message code="refresh"/>' );
		</script>
	</c:otherwise>
</c:choose>
</body>


</html>
