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

<!-- if the user is ADMIN IT -->
<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">				
	<c:set var="cssLogoDisplayClass" value="logoDisplayAdmin"/>
</security:authorize>

<!-- if the user is not ADMIN IT -->
<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
	<!-- but has the change settings permission -->	
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
		<c:set var="cssLogoDisplayClass" value="logoDisplayAdmin"/>
	</security:authorize>	
	
	<!-- doesn't have the change settings permission -->
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
		<c:set var="cssLogoDisplayClass" value="logoDisplay"/>
	</security:authorize>		 	
</security:authorize>

<%int random = (int) (Math.random() * 10000 % 1000); %>
			
			<c:set var="disabled" value="0"/>
			<a id="logo"><img src="images/logos/logo.jpg" /></a>
			<div id="topmenu">
				<a href="${OM_MODULES}" id="modules" class="first"><spring:message code="modules"/></a>
				<a href="#" id="editMyPerson"><spring:message code='person.myProfile'/></a>
				
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
					<c:choose>
						<c:when test="${SESS_ORGANISATION_NAME ne null and SESS_ORGANISATION_ID ne null}">
							<a href="#" id="settings"><spring:message code="settings"/></a>
							<a href="#"><spring:message code="help"/></a>    
            				<a href="j_spring_security_logout"><spring:message code="signout"/></a>					    
	            		</c:when>
	            		<c:otherwise>
	            			<a href="#"><spring:message code="help"/></a>    
            				<a href="j_spring_security_logout"><spring:message code="signout"/></a>  
	            		</c:otherwise>   
	            	</c:choose>         		            		
				</security:authorize> 			
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">											      
            		<a href="#"><spring:message code="help"/></a>   
            		<a href="j_spring_security_logout"><spring:message code="signout"/></a> 
				</security:authorize>		            	            	     	      
            </div><!-- end div: topmenu -->
													
			<div id="userAndOrganizationSpecInfo">
			
				<div id="CLIENT_LOGO_CONTENT">	
	         		<img src="servlet/LogoServlet?random=<%=random%>" class="${cssLogoDisplayClass}" title="logo"/>
				</div>									
				
				<div id="userbox">
					<div id="userDetails">
						<strong><span title="<security:authentication property="principal.fullName"/>"> <security:authentication property="principal.firstNameTruncate"/> <security:authentication property="principal.lastNameTruncate"/> </span></strong>								
								
						<span title="<security:authentication property="principal.jobDepartmentPairs"/>">
							<security:authentication property="principal.jobDepartmentPairsTruncate"/>
						</span>									
					</div>																
				</div> <!-- end userbox -->
				<div id="clock"></div>		
			</div> <!-- end userAndOrganizationSpecInfo -->


<script>

	var clock = new Clock('clock', '${locale}' , true);
	setInterval("clock.update()", 1000);

	//--------------------------------------------------- < Settings >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
		var parameters = {url: "SettingList.htm", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("settings", "click", getContentFromUrl, parameters, true);	
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_ChangeSettings}">
		var parameters = {url: "SettingList.htm?organisationId=${ORGANISATION_ID}", container: "MAIN_CONTENT"};
		YAHOO.util.Event.addListener("settings", "click", getContentFromUrl, parameters, true);
	</security:authorize>

	YAHOO.util.Event.addListener("editMyPerson", "click", getContentFromUrl, {url: "PersonMyProfile.htm?MY_PROFILE=true&ACTION=GET&personId=<security:authentication property="principal.personId"/>", 
		container: "MAIN_CONTENT"}, true);	
</script>
