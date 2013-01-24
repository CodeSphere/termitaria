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

<c:if test="${!(empty COST_SHEET)}">
	<table border="0">
		<!-- COST SHEET ID -->
		<tr>
			<td class="labelInfoTd" style="width:190px">
				<spring:message code="costsheet.id"/>
			</td>
			<td>
				${COST_SHEET.costSheetId}
			</td>
		</tr>
		<!-- COST SHEET REPORTER NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="costsheet.reporter"/>
			</td>
			<td>
				${COST_SHEET.costSheetReporterName} 
			</td>
		</tr>
		
		<!-- PROJECT / ORGANIZATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="costsheet.project"/> 										
			</td>
			<td>
				<c:choose>
					<c:when test="${COST_SHEET.projectDetails ne null}">
						<c:if test="${(COST_SHEET.projectName != '') && !(empty COST_SHEET.projectName )}">
							${COST_SHEET.projectName}
						</c:if>
					</c:when>
					<c:otherwise>
						<spring:message code="costsheet.for.organization"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<c:if test="${COST_SHEET.projectDetails ne null}">
			<!-- PROJECT STATUS -->		
			<tr>
				<td class="labelInfoTd">
					<spring:message code="project.status.general"/>
				</td>
				<td>								
					<c:choose>
						<c:when test="${COST_SHEET.projectDetails.status == 1}">											
							<spring:message code="project.listing.opened"/>		
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${COST_SHEET.projectDetails.status == 2}">													
									<spring:message code="project.closed"/>		
								</c:when>
								<c:otherwise>
									<c:if test="${COST_SHEET.projectDetails.status == 3}">														
										<spring:message code="project.abandoned"/>																
									</c:if>																														
								</c:otherwise>	
							</c:choose>														
						</c:otherwise>	
					</c:choose>							
				</td>
			</tr>
		</c:if>		
		
		<!-- ACTIVITY NAME-->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="costsheet.activity"/>
			</td>
			<td>
				<c:if test="${(COST_SHEET.activityName!= '') && !(empty COST_SHEET.activityName )}">
					${COST_SHEET.activityName} 
				</c:if>
			</td>
		</tr>
		
		<!-- DATE -->
		<c:if test="${COST_SHEET.date ne null}">
			<tr>
				<td class="labelInfoTd">
					<spring:message code="costsheet.date"/>
				</td>
				<td>
					<fmt:formatDate value="${COST_SHEET.date}" dateStyle="long"/>
				</td>
			</tr>	
		</c:if>
		
		<!-- DESCRIPTION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="costsheet.description"/>
			</td>
			<td>
				<c:if test="${(COST_SHEET.description != '') && !(empty COST_SHEET.description) }">
					${COST_SHEET.description}
				</c:if>
			</td>
		</tr>
		
		<!-- OBSERVATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="costsheet.observation"/>
			</td>
			<td>
				<c:if test="${(COST_SHEET.observation != '') && !(empty COST_SHEET.observation) }">
					${COST_SHEET.observation}
				</c:if>
			</td>
		</tr>
		
		<!-- COST PRICE -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="costsheet.cost.price"/>
			</td>
			<td>
			<c:if test="${(COST_SHEET.costPrice != '') && !(empty COST_SHEET.costPrice) }">
				${COST_SHEET.costPrice} ${COST_SHEET.costPriceCurrency.initials}
			</c:if>
			</td>
		</tr>
		
		<c:if test="${(IS_USER_ALL eq true || IS_MANAGER eq true) && (HAS_PROJECT eq true)}">
			<!-- BILLABLE -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="costsheet.billable"/>
				</td>
				<td>
					<c:if test="${(COST_SHEET.billable != '') && !(empty COST_SHEET.billable )}">
						<spring:message code="costsheet.billable.${COST_SHEET.billable}"/>
					</c:if>
				</td>
			</tr>
			
			<!-- BILLING PRICE -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="costsheet.billing.price"/>
				</td>
				<td>
				<c:if test="${(COST_SHEET.billingPrice != '') && !(empty COST_SHEET.billingPrice) }">
					${COST_SHEET.billingPrice} ${COST_SHEET.billingPriceCurrency.initials}
				</c:if>
				</td>
			</tr>
		</c:if>
	</table>
</c:if>
</div>
