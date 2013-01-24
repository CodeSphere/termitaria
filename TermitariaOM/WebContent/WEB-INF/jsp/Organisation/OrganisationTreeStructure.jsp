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
<!-- Used to display the organization tree structure after deleting an organization
or modifying its status directly from the node tree-->
<%@ include file="../Taglibs.jsp" %>
<c:set var="viewChildOrgInfo" value="false"/>
<security:authorize ifAllGranted="OM_OrgChildView">
	<c:set var="viewChildOrgInfo" value="true"/>
</security:authorize>
<c:set var="deleteChildOrg" value="false"/>
<security:authorize ifAllGranted="OM_OrgChildDelete">
	<c:set var="deleteChildOrg" value="true"/>
</security:authorize>
<c:set var="updateChildOrgStatus" value="false"/>
<security:authorize ifAllGranted="OM_OrgChildChangeStatus">
	<c:set var="updateChildOrgStatus" value="true"/>
</security:authorize>
<security:authorize ifAllGranted="OM_OrgChildChangeStatus">
	<c:set var="updateChildOrgStatus" value="true"/>
</security:authorize>
<c:set var="addChildOrgAdmin" value="false"/>
<security:authorize ifAllGranted="OM_OrgCreateAdmin">
	<c:set var="addChildOrgAdmin" value="true"/>
</security:authorize>

<c:choose>
	<c:when test="${FIRST_LEVEL_NODES == null}">
		<h3><font class="organisationOverviewTreeNull"><spring:message code="organisation.overview.tree.null"/></font></h3>
	</c:when>
	<c:otherwise>
		<h3><spring:message code="organisation.overview.structure"/>:</h3>
		<%@ include file="./../Messages.jsp" %>
		<font class="topTreeFormat">${ORGANIZATION.name}</font>		
		<!-- Organization Dynamic Tree -->			
		<div id="treeDiv1">		
		
		</div>
		<script type="text/JavaScript">			
			buildOrganisationTree('${FIRST_LEVEL_NODES}', "treeDiv1", ${viewChildOrgInfo}, ${deleteChildOrg}, ${updateChildOrgStatus}, ${addChildOrgAdmin}, 
			'<spring:message code="organisation.addAdmin"/>', '<spring:message code="organisation.disable"/>', '<spring:message code="organisation.enable"/>', '<spring:message code="organisation.delete"/>', 
			'<spring:message code="organisation.viewGeneralInfo"/>', '<spring:message code="confirm.delete"/>', '<spring:message code="confirm.deactivate"/>', '<spring:message code="confirm.activate"/>' );					
		</script>
		</c:otherwise>
</c:choose>
