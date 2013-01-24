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
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.web.WebAttributes" %>

<div class="accessDenied">
			<font style="font-family:Verdana, Arial, Helvetica, sans-serif; font-size: 13px; color: red; font-weight: bold;">Sorry, access is denied!</font>
			
			
			<p style="font-size: 10px;">
			<%
				Object springSecurityAccessDeniedExceptionKey = request.getAttribute(WebAttributes.ACCESS_DENIED_403);
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				int nulls = 0;
				if (springSecurityAccessDeniedExceptionKey == null) {
					nulls ++;
				}
				if (auth == null) {
					nulls ++;
				}
			%>	
			
			
			</p>
			<br/>
			
			<%if (nulls == 2) {%>
				<a href="SignOn.htm">Sign In</a>
			<%} else {%>
				<a href="accessDenied.jsp?clearSession=?">Clear Session</a>
			<%}%>
			
			<% if (request.getParameter("clearSession") != null) {
				request.getSession().invalidate();
			}%>
</div>
