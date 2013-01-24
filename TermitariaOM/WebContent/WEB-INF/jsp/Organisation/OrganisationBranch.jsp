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

<div id="branchDiv" class="branchScroll">

<form:form id="branchForm" name="branchForm" commandName="searchOrganisationBean">
	
	<!-- Fields for displaying the initial organization id and name -->
	
			<table class="branch_list_results" cellspacing="0">
		
				<c:set var="i" value="0"/>
				<c:set var="cssClass" value="even_row"/>
				
				<c:if test="${ROOT_NAME ne null}">
					<tr class="${cssClass}">
						<td  Width="17px">
						<a href="#" onClick="submitAndShowInBranchPannel('OrganisationBranch.htm?ORG=-1&ALL_OPTIONS=${ALL_OPTIONS}', 'branchForm', YAHOO.om.showBranches);"> 
			 	 						 	<spring:message code="organisation.branch.back"/></a>	
						</td>
						<td>
						</td>
						<td Width="300px">
						</td>
					</tr>
				</c:if>
					
				<c:if test="${cssClass == 'even_row'}">
					<c:set var="cssClass" value="odd_row"/>
				</c:if>
				<c:choose>
							<c:when test="${PARENT_NAME ne null}">
								<tr class="${cssClass}">
										<c:set var="i" value="1"/>
									<td>
										<a href="#" onClick="submitAndShowInBranchPannel('OrganisationBranch.htm?ORG=${PARENT}&ALL_OPTIONS=${ALL_OPTIONS}', 'branchForm', YAHOO.om.showBranches);"> <spring:message code="organisation.branch.back"/> </a>
									</td>
									<td>
									</td>
									<td Width="300px">
									</td>
								</tr>
							</c:when>
								<c:when test="${ALL_OPTIONS eq true and ROOT_NAME eq null}">
								<tr class="${cssClass}">
									<c:set var="i" value="1"/>
									<td>
									</td>
									<td>
									</td>
									<td Width="300px">
										<a href="#" title="<spring:message code="organisation.tree.all" />" onClick="useBranch('set', 'branchSelection', '<spring:message code="organisation.branch.back"/>'); 
										 	 		useBranch('set', 'organisationId', -1);
										 	 		destroyBranchPanel();"> <i><spring:message code="organisation.tree.all.search"/></i> </a>
									</td>
								</tr>
					</c:when>
				</c:choose>
		
				<c:forEach var="bean" items="${SEARCH_ORGS}">
						
					<c:set var="i" value="${i + 1}"/>
					<c:choose>
						<c:when test="${i % 2 == 0}">
							<c:set var="cssClass" value="even_row"/>
						</c:when>
						<c:otherwise>
							<c:set var="cssClass" value="odd_row"/>
						</c:otherwise>
					</c:choose>
					<tr class="${cssClass}">
			
						<c:choose>
							<c:when test="${bean.hasBranches eq true}">
								<td  Width="17px">
									<a href="#" onClick="submitAndShowInBranchPannel('OrganisationBranch.htm?ORG=${bean.organisationId}&ALL_OPTIONS=${ALL_OPTIONS}', 'branchForm', YAHOO.om.showBranches);" >
									<img src="<spring:theme code="folder"/>"/></a>
								</td>
							</c:when>
							<c:otherwise>
								<td>
								</td>
							</c:otherwise>
						</c:choose>
						<td  Width="17px">
							<c:choose> 
								<c:when test="${bean.status == 0}">
									<img src="images/buttons/action_disable.png"/>
								</c:when>
								<c:otherwise>
										<img src="images/buttons/action_enable.png"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<a href="#" onClick="useBranch('set', 'branchSelection', '${bean.name}'); 
												 useBranch('set', 'organisationId', '${bean.organisationId}');
												 destroyBranchPanel();" > ${bean.name}</a>
						</td>
						
					</tr>
				</c:forEach>
			</table>
		
</form:form>

	</div>

