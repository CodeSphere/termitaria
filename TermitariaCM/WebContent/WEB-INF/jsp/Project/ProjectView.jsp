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
<div class="yuiInfoPanel">
<%@ include file="../Taglibs.jsp" %>
<%@ include file="../Messages.jsp" %>


<c:if test="${!(empty PROJECT)}">
	
	<table>				
	
		<!-- CLIENT -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="project.client"/>
			</td>
			<td>
				<c:choose>
					<c:when test="${PROJECT.client.type == 2}">
					 	 ${PROJECT.client.p_firstName} ${PROJECT.client.p_lastName}	
					</c:when>
					<c:otherwise>
					 <c:choose>
							<c:when test="${PROJECT.client.type == 1}">
						 		${PROJECT.client.c_name}
							</c:when>
							<c:otherwise>
								<spring:message code="project.from.organization"/>
							</c:otherwise>
					 	</c:choose>
					</c:otherwise>
				</c:choose>				
			</td>
		</tr>
	
		<!-- NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="project.name"/>
			</td>
			<td>
				 ${PROJECT.name}
			</td>
		</tr>
		
		<!-- MANAGER -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="project.manager.name"/>
			</td>
			<td>
				<c:choose>
					<c:when test="${PROJECT.manager.status == 2}">
						
					</c:when>
					<c:otherwise>
						${PROJECT.manager.firstName} ${PROJECT.manager.lastName}	
					</c:otherwise>
				</c:choose>				 
			</td>
		</tr>
		
		<!-- DESCRIPTION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="project.description"/>
			</td>
			<td>				
				<c:if test="${(PROJECT.description != '') && !(empty PROJECT.description) }">
						${PROJECT.description}
				</c:if>			
			</td>
		</tr>	
					
		<!-- OBSERVATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="project.observation"/>
			</td>
			<td>				
				<c:if test="${(PROJECT.observation != '') && !(empty PROJECT.observation) }">
						${PROJECT.observation}
				</c:if>			
			</td>
		</tr>
		
		<!-- PROJECT TEAM NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="project.team.project"/>
			</td>
			<td colspan="3">
				<c:choose>
					<c:when test="${PROJECT.projectTeam eq null}">
						<div class="noFindResults"><spring:message code="project.no.project.team.message"/></div>
					</c:when>
					<c:otherwise>					
			            		
						<c:if test="${!(empty PROJECT.projectTeam.teamMembers)}">
							<select id="viewPersons" name="persons" multiple="multiple" size="5" style="width:200px;">
								<c:forEach var="person" items="${PROJECT.projectTeam.teamMembers}">	
									<c:if test="${person.status != 2}">	
										<option title="${person.firstName} ${person.lastName}">${person.firstName} ${person.lastName}</option> 
									</c:if>									
								</c:forEach>	
							</select>					
						</c:if>			
									             		           
					</c:otherwise>
				</c:choose>			
			 </td>			
		</tr>																
	</table>	
</c:if>
</div>

<script>
	sortSelect(document.getElementById("viewPersons"));
</script>
