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
<html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="Taglibs.jsp" %>

<head>
	<title><spring:message code="create.report"/></title>
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet"/>"/>
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.container"/>" />
	<script type="text/javascript" src="js/cs/cs_common.js"></script>
</head>

<!-- DD: Body has this class because without it yui panels' css won't work -->
<body class="yui-skin-sam">

<c:if test="${ERRORS ne null}">
	<input id="ERRORS_CONTAINER" type="hidden" value="${ERRORS}" />
	
	<!--  tags special for tree -->
	<div id="errorsContainer" class="errorMessagesDiv">
		<table class="errorMessagesTable">
				<tr>
					<td>
					</td>
					<td>
	
						<div class="hd">
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="bd">
							<div style="width:470px">
								<c:forEach var="error" items="${ERRORS}" >
									${error}<br/>
								</c:forEach>
							</div>
						</div>
					</td>
					<td>
					</td>
				</tr>
			</table>
		<div class="ft">&nbsp;</div>
	</div>
<script>
if(typeof(YAHOO.widget.Module) != "undefined") { 
	YAHOO.ts.errorsContainer = new YAHOO.widget.Module("errorsContainer", {visible:true} );
	YAHOO.ts.errorsContainer.render();
	YAHOO.ts.errorsContainer.show();
	YAHOO.util.Event.addListener("closeErrors", "click", function () {		
		YAHOO.ts.errorsContainer.hide();
		YAHOO.ts.errorsContainer.destroy();
		}, YAHOO.ts.errorsContainer, true);
}
</script>	
</c:if>

</body>
</html>
