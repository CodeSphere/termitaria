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

<c:if test="${!(empty PERSON)}">	
	<table>	
		<tr>			
			<td rowspan="15" valign="top">
				<!--  PERSON PHOTO -->
					<c:if test="${PERSON.picture.pictureId > 0}">
						<div id="person_picture">						
							<img src="servlet/ImageServlet?picture=${PERSON.picture.pictureId}"/>
						</div>										
					</c:if>	
			</td>
		</tr>	
		
		<!-- PERSON FIRST NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.firstName"/>
			</td>			
			<td>
				${PERSON.firstName}
			</td>
		</tr>	
		
		<!-- PERSON LAST NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.lastName"/>
			</td>
			<td>
				${PERSON.lastName}
			</td>
		</tr>
		
		<!-- PERSON USERNAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.username"/>
			</td>			
			<td>
				${PERSON.username}
			</td>
		</tr>
		<!-- PERSON SEX -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.sex"/>
			</td>
			<td>
				 ${PERSON.sex}	
			</td>
		</tr>
		
		
		<!-- PERSON BIRTH DATE-->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.birthDate"/>
			</td>
			<td>
				<fmt:formatDate value="${PERSON.birthDate}" dateStyle="long"/>							
			</td>
		</tr>
		
		<!-- PERSON OBSERVATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.address"/>
			</td>
			<td>				
				<c:if test="${(PERSON.address != '') && !(empty PERSON.address) }">
						${PERSON.address}
				</c:if>			
			</td>
		</tr>
		
		<!-- PERSON PHONE -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.phone"/>
			</td>
			<td>				
				<c:if test="${(PERSON.phone != '') && !(empty PERSON.phone) }">
					 ${PERSON.phone}
				</c:if>			
			</td>
		</tr>				
		
		<!-- PERSON EMAIL -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.email"/>
			</td>
			<td>				
				<c:if test="${(PERSON.email != '') && !(empty PERSON.email) }">
						${PERSON.email}
				</c:if>			
			</td>
		</tr>				
		
		<!-- PERSON OBSERVATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.observation"/>
			</td>
			<td>				
				<c:if test="${(PERSON.observation != '') && !(empty PERSON.observation) }">
					 ${PERSON.observation}
				</c:if>			
			</td>
		</tr>	
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">
		
			<!-- PERSON ROLES -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="person.roles"/>
				</td>
				<td>	
					<c:if test="${!(empty PERSON.roles)}">
						<select id="viewRoles" name="roles" multiple="multiple" size="5" style="width:200px;">
							<c:forEach var="role" items="${PERSON.roles}">		
								<option title="${role.name}">${role.name}</option>									
							</c:forEach>	
						</select>					
					</c:if>			
				</td>
			</tr>			
			
		</security:authorize>
		
		<!-- PERSON DEPARTMENTS -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.depts"/>
			</td>
			<td>	
				<c:choose>
					<c:when test="${!(empty PERSON.depts)}">
						<select id="viewDepartments" name="departments" multiple="multiple" size="5" style="width:200px;">
							<c:forEach var="dept" items="${PERSON.depts}">		
								<option title="${dept.name}">${dept.name}</option>									
							</c:forEach>	
						</select>						
					</c:when>				
					<c:otherwise>
						<div class="noFindResults"><spring:message code="person.not.in.department"/></div>
					</c:otherwise>	
				</c:choose>		
			</td>
		</tr>	
		
		<!-- PERSON USER GROUPS -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.usergroups"/>
			</td>
			<td>	
				<c:choose>
					<c:when test="${!(empty PERSON.userGroups)}">
						<select id="viewUserGroups" name="usergroups" multiple="multiple" size="5" style="width:200px;">
							<c:forEach var="usergroup" items="${PERSON.userGroups}">		
								<option title="${usergroup.name}">${usergroup.name}</option>									
							</c:forEach>	
						</select>						
					</c:when>				
					<c:otherwise>
						<div class="noFindResults"><spring:message code="person.not.in.usergroup"/></div>
					</c:otherwise>	
				</c:choose>		
			</td>
		</tr>	
		
		<!-- PERSON JOBS -->
		<tr> 
			<td class="labelInfoTd">
				<spring:message code="person.jobs" />
			</td>
			<td> 	
				<c:choose>
					<c:when test="${!(empty PERSON_JOBS)}">										
							<c:forEach var="jobDept" items="${DEPT_JOBS}">
								${jobDept}<br/>
							</c:forEach>																									
					</c:when>						
					<c:otherwise>
						<div class="noFindResults"><spring:message code="person.not.have.job"/></div>
					</c:otherwise>	
				</c:choose>											
			</td>
		</tr>
				
		<!-- PERSON OUT-OF-OFFICE -->
		<tr>
			<c:choose>
				<c:when test="${!(empty PERSON.outOfOffice)}">
					<td class="labelInfoTd">
						<spring:message code="person.outOfOffice"/>
					</td>
					<td>					
						<select id="viewOOO" name="ooo" multiple="multiple" size="5" style="500px;">
							<c:forEach var="outOfOffice" items="${PERSON.outOfOffice}">		
								<option title="${outOfOffice.observation}"><spring:message code="ooo.startDate"/> <fmt:formatDate value="${outOfOffice.startPeriod}" dateStyle="long"/>	-								
									<spring:message code="ooo.endDate"/> <fmt:formatDate value="${outOfOffice.endPeriod}" dateStyle="long"/>										
									</option>																																		
							</c:forEach>	
						</select>	
					</td>	
				</c:when>
				<c:otherwise>
					<td class="labelInfoTd">
						<spring:message code="person.inOffice"/>
					</td>
					<td>
						<div class="noFindResults"><spring:message code="person.not.outofoffice"/></div>
					</td>
				</c:otherwise>
			</c:choose>			
		</tr>			
	</table>		
</c:if>
</div>
