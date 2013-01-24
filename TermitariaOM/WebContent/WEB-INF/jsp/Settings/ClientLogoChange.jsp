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
<%@page import="ro.cs.om.common.IConstant;"%>


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
				<a href="#" id="back"><spring:message code="back"/></a>	
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><spring:message code="clientlogo.change.subsection.title"/></span>

<%@ include file="../Messages.jsp" %>
<%int random = (int) (Math.random() * 10000 % 1000); %>
<form method="post" enctype="multipart/form-data" id="changeClientLogoForm" action="ClientLogoChange.htm">
<table class="tableAdd">
	<tr><td class="tableAddSpacer">&nbsp;</td></tr>
	<tr>
		<td width="10px" rowspan="5">&nbsp;</td>
		<td>
			<div id="LOGO_PICTURE_CONTENT">
			<c:choose>
				<c:when test="${clientLogoBean.logoId > 0}">
					<img src="servlet/LogoServlet?random=<%=random%>"/>
				</c:when>
				<c:otherwise>
					<img src="<spring:theme code="no_client_logo"/>"/>
				</c:otherwise>
			</c:choose>
			</div>
		</td>
		<td width="10px" rowspan="5">&nbsp;</td>
	</tr>
	<tr>   	
	 	<td>
	 		
			<spring:bind path="clientLogoBean.logoId">	
				<input type="hidden" name="${status.expression}" value="${status.value}"/>
			</spring:bind>
			<spring:bind path="clientLogoBean.file">	
				<input type="file" name="${status.expression}" value="${status.value}" accept="image/jpeg"/>
			</spring:bind>
			<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ChangeLogo}">
				<input type="button" class="button" onClick="upload('ClientLogoChange.htm?ORG=${ORG}', 'changeClientLogoForm', 'LOGO_PICTURE_CONTENT')" value="<spring:message code="upload"/>"/>
			</security:authorize>
			<c:if test="${NOAPPLY ne null}">
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ChangeLogo}">				
					<input type="button" class="button" onClick="getContentFromUrlDirect('ClientLogoApply.htm', 'CLIENT_LOGO_CONTENT')" value="<spring:message code="apply"/>"/>
				</security:authorize>
			</c:if>	
		</td>
	</tr>
	<tr>
		<td>
			
		</td>
	</tr>
	<tr><td height="5px"></td></tr>
	<tr><td><spring:message code="settings.upload.logo.alt"/></td></tr>
	<tr><td class="tableAddSpacer">&nbsp;</td></tr>
</table>

</form>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	
	getObject = new ObjSubmit("OrganisationForm.htm?ACTION=GET&organisationId=${ORG}", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
	
</script>
