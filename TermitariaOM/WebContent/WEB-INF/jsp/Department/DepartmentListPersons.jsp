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

<td>

						
	<c:choose>
		<c:when test="${empty personList}">
			&nbsp <div class="noSearchResults"><spring:message code="department.no.persons"/></div>
		</c:when>
		<c:otherwise>
			<fieldset style="width: 890px;">
				<legend><spring:message code="person.title.listing" /></legend>
				<br/>
					<table class="list_results">
						<tr class="list_results_header">
							<th style="width:200px"><spring:message code="person.firstName"/></th>
							<th style="width:200px"><spring:message code="person.lastName"/></th>
							<th style="width:100px"><spring:message code="person.email"/></th>
							<th style="width:500px"><spring:message code="person.address"/></th>
							<th style="width:100px"><spring:message code="person.phone"/></th>
							<th style="width:300px"><spring:message code="person.job"/></th>
							<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PersonUpdate}">
								<th style="width:60px"><spring:message code="actions"/></th>
							</security:authorize>
						</tr>
						<c:set var="i" value="1"></c:set>
						<c:set var="cssClass" value=""/>
						<c:forEach var="per" items="${personList}" varStatus="status">							
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
								<td>
									<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PersonUpdate}">
										<a href="#" onClick="getContentFromUrlDirect('PersonForm.htm?ACTION=GET_FROM_DEPARTMENT&personId=${per.personId}&departmentId=${departmentId}','MAIN_CONTENT')" title="<spring:message code="edit"/>">
											${per.firstName}
										</a>
									</security:authorize>
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_PersonUpdate}">
										${per.firstName}
									</security:authorize>
								</td>
								
								<td>
									<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PersonUpdate}">
										<a href="#" onClick="getContentFromUrlDirect('PersonForm.htm?ACTION=GET_FROM_DEPARTMENT&personId=${per.personId}&departmentId=${departmentId}','MAIN_CONTENT')" title="<spring:message code="edit"/>">
											${per.lastName}
										</a>
									</security:authorize>
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_PersonUpdate}">
										${per.lastName}
									</security:authorize>
								</td>
								
								<td>${per.email}</td>
								<td>${per.address}</td>
								<td>${per.phone}</td>								
								<td>${per.jobsFromDepts}</td>
								<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_PersonUpdate}">
									<td>
										<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
											<tr>	
												<td>
													<a href="#" onClick="getContentFromUrlDirect('PersonForm.htm?ACTION=GET_FROM_DEPARTMENT&personId=${per.personId}&departmentId=${departmentId}','MAIN_CONTENT')" title="<spring:message code="edit"/>"><img src="images/buttons/action_edit.png"/></a>
												</td>
											</tr>
										</table>			
									</td>
								</security:authorize>
							</tr>
							<c:set var="i" value="${i+1}"></c:set>
						</c:forEach>
					</table>
			</fieldset>
		</c:otherwise>
	</c:choose>

</td>
