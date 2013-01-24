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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ include file="Taglibs.jsp" %>
<%@page import="ro.cs.logaudit.web.controller.root.ControllerUtils"%>

<head>
	<title><spring:message code="title"/></title>

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<!-- CSS Section -->
	<link rel="stylesheet" type="text/css" href="themes/standard/css/style.css"/>

</head>



<body>
<center>

     <div id="loginContainer">
        <div id="login">
        <img src="<spring:theme code="logo"/>" style="displaylock" />
        </div>

		<div id="loginContent">
			<div id="MODULES_DIV">
			 
			<form id="SIGNON_FORM" name="f" action="<c:url value='j_spring_security_check'/>" method="post">
						
				<table id="loginTable">
			                <tr>
								<td class="loginLabel">
									<spring:message code="username"/>
								</td>
								<td class="loginInput">
									<input type='text' name='j_username' value='<c:if test="${not empty param.login_error}">${SPRING_SECURITY_LAST_USERNAME}</c:if>'/>
								</td>
		                    </tr>
		                    <tr>
								<td class="loginLabel">
									<spring:message code="password"/>
								</td>
								<td class="loginInput">
									<input type='password' name='j_password'/>
								</td>
		                    </tr>		                 
		                    <!-- ==================================START SELECTED LANGUAGE========================================= -->
							<tr>
								<td class="loginLabel">
									<spring:message code="language"/>
								</td>
								<td class="loginInput">
									<select name="siteLanguage" id="siteLanguage">				
										<option value="en" > EN </option>					
										<option value="ro" selected > RO </option>															
									</select>
								</td>
		                    </tr>
		        			<tr>		        			  
		        			<!-- ==================================END SELECTED LANGUAGE========================================= -->
								<td class="loginLabel">
									<input type="checkbox" name="_spring_security_remember_me">
								</td>
								<td class="loginInput">
									<spring:message code="rememberMe"/>
								</td>
							</tr>														
												
							<c:if test="${not empty param.login_error}">
				      			<td></td>
								<td class="loginInput">
								<font color="red">
				        			<div>						        				
			        					<c:if test="${EXCEPTION_CONSTANT.OM_BAD_CREDENTIALS == sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}">
			        						<spring:message code="unsuccessfullLoginAtemptMessage"/>
			        						<spring:message code="reason.bad.credentials"/>
			        					</c:if>
			        					
			        					<c:if test="${EXCEPTION_CONSTANT.OM_SESSION_EXCEED == sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}">
			        						<spring:message code="unsuccessfullLoginAtemptMessage"/>
			        						<spring:message code="reason.session.exceed"/>
			        					</c:if>	
			        					
			        					<c:if test="${EXCEPTION_CONSTANT.OM_SESSION_EXCEED == sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}">
			        						<spring:message code="unsuccessfullLoginAtemptMessage"/>
			        						<spring:message code="reason.user.disabled"/>
			        					</c:if>	
			        					
				        			</div>
				      			</font>
								</td>
							</c:if>
							<tr>
							<td></td><td class="loginInput"><div style="align: right"><input type="submit" id="loginButton" value="<spring:message code="loginButtonLabel"/>"/></div></td>
							<td></td><td class="loginInput"><div style="align: right"><input type="button" onclick="window.location.href='/Termitaria/OM/SignupWizard.htm'" id="signupButton" value="<spring:message code="signupButtonLabel"/>"/></div></td>
							</tr>	
		                </table> 
		    	</form>
			</div> <!-- end MODULES_DIV -->
		
		</div><!-- end loginContent -->
	</div>
<script type="text/JavaScript">
	document.f.j_username.focus();
</script>
</center>
</body>
</html>
