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
<%@ include file="../Taglibs.jsp" %>
<%@ include file="../Messages.jsp" %>

<c:choose>
	<c:when test="${!(empty TEAM_MEMBERS)}">
		<fieldset style="width: 600px;">
			<legend><spring:message code="person.title.listing" /></legend>
				<table class="list_results" >
					<tr class="list_results_header">		
						
						<th style="width: 300px;">
							<spring:message code="person.lastName"/>								
						</th>
						<th style="width: 300px;">
							<spring:message code="person.firstName"/>							
						</th>										
						<th style="width: 300px;"><spring:message code="person.email"/></th>						
						<th style="width: 300px;"><spring:message code="actions"/></th>									
					</tr>		
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					
					<c:forEach var="member" items="${TEAM_MEMBERS}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
						<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">		
							
							<td align="center">	
								${member.firstName}							
							</td>
							<td align="center">	
								${member.lastName}							
							</td>
							<td align="center">	
								${member.email}							
							</td>
																					
							<td align="center">	
								<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
									<tr>																				
										<td>						
											<c:choose>
												<c:when test="${member.memberId > 0}">
													<a href="#" onClick="manageTeamMember('TeamMember.htm?ACTION=GET&memberId=${member.memberId}&projectId=${projectId}', '<spring:message code="person.edit"/>', ${member.memberId});" title="<spring:message code="person.edit"/>"><img src="images/buttons/action_edit.png"/></a>
												</c:when>
												<c:otherwise>
													<a href="#" onClick="manageExternalTeamMember('TeamMember.htm?ACTION=GET_FROM_ADD&memberId=${member.memberId}&projectId=${projectId}', '<spring:message code="person.edit"/>', ${member.idExternal}, ${member.memberId});" title="<spring:message code="person.edit"/>"><img src="images/buttons/action_edit.png"/></a>
												</c:otherwise>
											</c:choose>
										</td>	
										<td>											
											<a href="#" onClick="deleteWithConfirmationForAddExternal('${member.firstName}','${member.lastName}',${member.idExternal}, ${projectId}, ${member.memberId}, 'TEAM_MEMBERS', '<spring:message code="person.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');" title="<spring:message code="delete"/>"><img src="images/buttons/action_delete.png"/></a>																																																						
										</td>																																																																										
									</tr>
								</table>									
							</td>								
						</tr>						
					</c:forEach>
				</table>
		</fieldset>		
	</c:when>	
</c:choose>	
<br/></br>
