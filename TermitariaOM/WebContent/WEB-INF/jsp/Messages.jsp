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
<%@ include file="Taglibs.jsp" %>

<c:if test="${ERRORS ne null}">
	<div id="errorsContainer" class="errorMessagesDiv">
		<div class="hd">
				<div id="closeErrors" class="messagesCloseButon"></div>
		</div>
		<div class="bd">
			<c:forEach var="error" items="${ERRORS}" >
						${error}<br/>
			</c:forEach>
		</div>
		<div class="ft">&nbsp;</div>
	</div>
<script>
if(typeof(YAHOO) != "undefined") { 
	YAHOO.om.errorsContainer = new YAHOO.widget.Module("errorsContainer", {visible:false} );
	YAHOO.om.errorsContainer.render();
	YAHOO.om.errorsContainer.show();
	YAHOO.util.Event.addListener("closeErrors", "click", function () {		
		YAHOO.om.errorsContainer.hide();
		YAHOO.om.errorsContainer.destroy();
		}, YAHOO.om.errorsContainer, true);
}
</script>	
</c:if>

<c:if test="${MESSAGES ne null}">
		<div id="messagesContainer" class="successMessagesDiv">
			<div class="hd">
				<div id="closeMessages" class="messagesCloseButon"></div>
			</div>
			<div class="bd">
					<c:forEach var="message" items="${MESSAGES}">
						${message}<br/>
					</c:forEach>
			</div>
			<div class="ft">&nbsp;</div>
		</div>
<script>
if(typeof(YAHOO) != "undefined") { 
	YAHOO.om.messagesContainer = new YAHOO.widget.Module("messagesContainer", {visible:false} );
	YAHOO.om.messagesContainer.render();
	YAHOO.om.messagesContainer.show();
	YAHOO.util.Event.addListener("closeMessages", "click", function() {
		YAHOO.om.messagesContainer.hide();
		YAHOO.om.messagesContainer.destroy();
		}, YAHOO.om.messagesContainer, true);
}
</script>
</c:if>


