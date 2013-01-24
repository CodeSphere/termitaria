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
	<div id="errorsPanelContainer" class="errorPanelMessagesDiv">
		<table class="errorMessagesTable">
				<tr>
					<td>
					</td>
					<td>
	
						<div class="hd1">
							<div id="closePanelMessages" class="messagesCloseButon"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="bd1">
							<div style="width:320px">
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
		<div class="ft1">&nbsp;</div>
	</div>
<script>
if(typeof(YAHOO.widget.Module) != "undefined") { 
	YAHOO.ts.errorsContainer = new YAHOO.widget.Module("errorsPanelContainer", {visible:true} );
	YAHOO.ts.errorsContainer.render();
	YAHOO.ts.errorsContainer.show();
	YAHOO.util.Event.addListener("closePanelMessages", "click", function () {		
		YAHOO.ts.errorsContainer.hide();
		YAHOO.ts.errorsContainer.destroy();
		}, YAHOO.ts.errorsContainer, true);
}
</script>	
</c:if>

<c:if test="${WARNINGS ne null}">
	<input id="WARNINGS_CONTAINER" type="hidden" value="${WARNINGS}" />
	
	<!--  tags special for tree -->
	<div id="warningPanelContainer" class="warningPanelMessagesDiv">
		<table class="warningMessagesTable">
				<tr>
					<td>
					</td>
					<td>
	
						<div class="hd1">
							<div id="closeMessages" class="messagesCloseButon"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="bd1">
							<div style="width:320px">
								<c:forEach var="warning" items="${WARNINGS}" >
									${warning}<br/>
								</c:forEach>
							</div>
						</div>
					</td>
					<td>
					</td>
				</tr>
			</table>
		<div class="ft1">&nbsp;</div>
	</div>
<script>
if(typeof(YAHOO.widget.Module) != "undefined") { 
	YAHOO.ts.warningsContainer = new YAHOO.widget.Module("warningPanelContainer", {visible:true} );
	YAHOO.ts.warningsContainer.render();
	YAHOO.ts.warningsContainer.show();
	YAHOO.util.Event.addListener("closeMessages", "click", function () {		
		YAHOO.ts.warningsContainer.hide();
		YAHOO.ts.warningsContainer.destroy();
		}, YAHOO.ts.warningsContainer, true);
}
</script>	
</c:if>

<c:if test="${MESSAGES ne null}">
		<!--  tags special for tree -->
		<input id="DELETE_TYPE" type="hidden" value="${DELETE_MOVE_TO_ROOT}"/>
			
		<div id="messagesPanelContainer" class="successPanelMessagesDiv">
			<table class="successMessagesTable">
				<tr>
					<td>
					</td>
					<td>
						<div class="hd1">
							<div id="closePanelMessages" class="messagesCloseButon"></div>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="bd1">
							<div style="width:320px">
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
			<div class="ft1">&nbsp;</div>
		</div>
<script>

if(typeof(YAHOO.widget.Module) != "undefined") {	

	YAHOO.ts.messagesContainer = new YAHOO.widget.Module("messagesPanelContainer", {visible:false} );	
	YAHOO.ts.messagesContainer.render();	
	YAHOO.ts.messagesContainer.show();
	YAHOO.util.Event.addListener("closePanelMessages", "click", function() {		
		YAHOO.ts.messagesContainer.hide();
		YAHOO.ts.messagesContainer.destroy();
		}, YAHOO.ts.messagesContainer, true);
}
</script>
</c:if>
