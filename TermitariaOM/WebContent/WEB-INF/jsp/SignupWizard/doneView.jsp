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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="/Termitaria/OM/js/cs/cs_formValidateAndSubmit.js" type="text/javascript"></script>
<title>Signup wizard</title>
</head>
<body>

<%@ include file="../NoAuthHeader.jsp" %> 

	<div id="LOCAL_MESSAGES_CONTENT">
			<%@ include	file="../Messages.jsp"%> 
	</div>
	<br/>
	<h1>You have registered successfully! </h1><br/>
<p>You can now <a href='SignupWizard.htm'>create a new account</a> or <a href="/Termitaria/OM/">sign on</a>!</p>
<br/><br/><br/><br/>
</body>
<%@ include file="../Footer.jsp" %>
</html>
