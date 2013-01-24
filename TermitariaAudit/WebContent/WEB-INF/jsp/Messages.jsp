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
	<input id="ERRORS_CONTAINER" type="hidden" value="${ERRORS}" />
	
	<!--  tags special for tree -->
	<div id="errorsContainer" class="errorMessagesDiv">
		<table class="errorMessagesTable">
				<tr>
					<td>
					</td>
					<td>
	
						<div class="hd">
								<div id="closeErrors" class="messagesCloseButon"></div>
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
	YAHOO.audit.errorsContainer = new YAHOO.widget.Module("errorsContainer", {visible:true} );
	YAHOO.audit.errorsContainer.render();
	YAHOO.audit.errorsContainer.show();
	YAHOO.util.Event.addListener("closeErrors", "click", function () {		
		YAHOO.audit.errorsContainer.hide();
		YAHOO.audit.errorsContainer.destroy();
		}, YAHOO.audit.errorsContainer, true);
}
</script>	
</c:if>

<c:if test="${MESSAGES ne null}">
		<!--  tags special for tree -->
		<input id="DELETE_TYPE" type="hidden" value="${DELETE_MOVE_TO_ROOT}"/>
			
		<div id="messagesContainer" class="successMessagesDiv">
			<table class="successMessagesTable">
				<tr>
					<td>
					</td>
					<td>
						<div class="hd">
							<div id="closeMessages" class="messagesCloseButon"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="bd">
							<div style="width:470px">
								<c:forEach var="message" items="${MESSAGES}">
										${message}<br/>
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

	YAHOO.audit.messagesContainer = new YAHOO.widget.Module("messagesContainer", {visible:false} );	
	YAHOO.audit.messagesContainer.render();	
	YAHOO.audit.messagesContainer.show();
	YAHOO.util.Event.addListener("closeMessages", "click", function() {		
		YAHOO.audit.messagesContainer.hide();
		YAHOO.audit.messagesContainer.destroy();
		}, YAHOO.audit.messagesContainer, true);
}
</script>
</c:if>


