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
<c:set var="display" value="TableCell"/>
<img src="<spring:theme code="menu_left"/>" style="position:absolute; left:0px; top:0px" />
	<table id="menuTable" border="0" cellpadding="0" cellspacing="0">
		<tr>
       		<td>
       			<img src="<spring:theme code="menu_tab_left"/>"/>
       		</td>
       		<td class="first" >
       			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_Basic}">
	 	 			<a href="#" id="clients"><spring:message code="client"/></a>
	       		</security:authorize>
	       	</td>
       		      	
	       	<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_Basic}">      
					<a href="#" id="projects"><spring:message code="project"/></a>
				</security:authorize>
			</td>   
	
	       	<td>
	       		<img src="<spring:theme code="menu_tab_right"/>" />
	       	</td>
		</tr>
	</table>


<script>

	var getObject = null;
	//--------------------------------------------------- < Clients >
    <security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_Basic}">  
		getObject = new ObjSubmit("ClientSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("clients", "click", getContentFromUrl, getObject, true);
   	</security:authorize>
</script>

<script>
//--------------------------------------------------- < Projects >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_Basic}">  
		getObject = new ObjSubmit("ProjectSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("projects", "click", getContentFromUrl, getObject, true);	
	</security:authorize>		
	
</script>
