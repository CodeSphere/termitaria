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
       			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">
       				<c:if test="${HAS_OM_MODULE eq true}">      		       			
		 	 			<a href="#" id="auditOm"><spring:message code="audit.om"/></a>
		 	 		</c:if> 
	       		</security:authorize>
	       	</td>
       		      	
	       	<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">      
	       		    <c:if test="${HAS_DM_MODULE eq true}">  	
						<a href="#" id="auditDm"><spring:message code="audit.dm"/></a>
					</c:if>
				</security:authorize>
			</td>   
			
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">      
	       		    <c:if test="${HAS_CM_MODULE eq true}">  	
						<a href="#" id="auditCm"><spring:message code="audit.cm"/></a>
					</c:if>
				</security:authorize>
			</td>   
			
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">      
	       		    <c:if test="${HAS_TS_MODULE eq true}">  	
						<a href="#" id="auditTs"><spring:message code="audit.ts"/></a>
					</c:if>
				</security:authorize>
			</td>   
	
	       	<td>
	       		<img src="<spring:theme code="menu_tab_right"/>" />
	       	</td>
		</tr>
	</table>


<c:if test="${HAS_OM_MODULE eq true}"> 
<script>

	var getObject = null;
	//--------------------------------------------------- < Audit OM>
    <security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">  
		getObject = new ObjSubmit("AuditOmSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("auditOm", "click", getContentFromUrl, getObject, true);
   	</security:authorize>
</script>
</c:if>

<c:if test="${HAS_DM_MODULE eq true}"> 
<script>
	//--------------------------------------------------- < Audit DM >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">  
		getObject = new ObjSubmit("AuditDmSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("auditDm", "click", getContentFromUrl, getObject, true);	
	</security:authorize>		
	
</script>
</c:if>

<c:if test="${HAS_CM_MODULE eq true}"> 
<script>
	//--------------------------------------------------- < Audit CM >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">  
		getObject = new ObjSubmit("AuditCmSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("auditCm", "click", getContentFromUrl, getObject, true);	
	</security:authorize>		
	
</script>
</c:if>

<c:if test="${HAS_TS_MODULE eq true}"> 
<script>
	//--------------------------------------------------- < Audit TS >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.AUDIT_Basic}">  
		getObject = new ObjSubmit("AuditTsSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("auditTs", "click", getContentFromUrl, getObject, true);	
	</security:authorize>		
	
</script>
</c:if>
