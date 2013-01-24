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
<%@ include file="Header.jsp" %> 
 
<div id="MAIN_CONTENT" class="mainContent"></div>

<%@ include file="Footer.jsp" %>

<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">

<c:choose>
	<c:when test="${ORGANISATION_REDIRECT ne null}">
		<script>	
			getContentFromUrlDirect('OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}', 'MAIN_CONTENT');
		</script>
	</c:when>
	<c:otherwise>

		<c:if test="${(THEME_REDIRECT eq null) and (RESET_REDIRECT eq null)}">
			<c:choose>
				<c:when test="${DISPLAY_ORGANIZATION_DELETED_MESSAGE ne null}">
					<script>					
						getContentFromUrlDirect('OrganisationSearch.htm?DISPLAY_ORGANIZATION_DELETED_MESSAGE=${DISPLAY_ORGANIZATION_DELETED_MESSAGE}', 'MAIN_CONTENT');
					</script>
				</c:when>
				<c:otherwise>
					<script>
					
						getContentFromUrlDirect('OrganisationSearch.htm', 'MAIN_CONTENT');
					</script>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:otherwise>
</c:choose>

</security:authorize>


<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
	<c:choose>
		<c:when test="${THEME_REDIRECT ne null}">
			<script>
				getContentFromUrlDirect('Setting.htm?id_redir=${THEME_REDIRECT}', 'MAIN_CONTENT');
			</script>
		</c:when>
		<c:when test="${RESET_REDIRECT ne null}">
			<script>
				getContentFromUrlDirect('SettingList.htm?reset=1', 'MAIN_CONTENT');
			</script>
		</c:when>
	</c:choose>
</security:authorize>
