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


<c:if test="${!(empty ACTIVITY)}">
	
	<table>				
				
		<!-- NAME -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="activity.name"/>
			</td>
			<td>
				 ${ACTIVITY.name}
			</td>
		</tr>
						
		<!-- PROJECT -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="activity.project"/>
			</td>
			<td>
				<c:choose>
					<c:when test="${ACTIVITY.projectDetails ne null}">
						<c:if test="${(ACTIVITY.projectName != '') && !(empty ACTIVITY.projectName )}">
							${ACTIVITY.projectName}
						</c:if>
					</c:when>
					<c:otherwise>
						<spring:message code="project.from.organization"/>
					</c:otherwise>
				</c:choose>				
			</td>
		</tr>
		
		<c:if test="${ACTIVITY.projectDetails ne null}">
			<!-- PROJECT STATUS -->		
			<tr>
				<td class="labelInfoTd">
					<spring:message code="project.status.general"/>
				</td>
				<td>								
					<c:choose>
						<c:when test="${ACTIVITY.projectDetails.status == 1}">											
							<spring:message code="project.listing.opened"/>		
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${ACTIVITY.projectDetails.status == 2}">													
									<spring:message code="project.closed"/>		
								</c:when>
								<c:otherwise>
									<c:if test="${ACTIVITY.projectDetails.status == 3}">														
										<spring:message code="project.abandoned"/>																
									</c:if>																														
								</c:otherwise>	
							</c:choose>														
						</c:otherwise>	
					</c:choose>							
				</td>
			</tr>
		</c:if>		
											
		<c:if test="${(IS_USER_ALL eq true) || (IS_MANAGER eq true)}">
			
			<c:if test="${ACTIVITY.projectDetails ne null}">
				<!-- BILLABLE -->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="activity.billable"/>
					</td>
					<td>
						<c:if test="${(ACTIVITY.billable != '') && !(empty ACTIVITY.billable )}">	
							<spring:message code="billable.${ACTIVITY.billable}" />			
						</c:if>						
					</td>
				</tr>
			</c:if>
		
			<!-- COST PRICE -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="activity.costPrice"/>
				</td>
				<td>
					${ACTIVITY.costPrice}		
				</td>
			</tr>
			
			<!-- COST PRICE CURRENCY -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="activity.costPriceCurrency"/>
				</td>
				<td>
					<c:choose>
						<c:when test="${ACTIVITY.costPriceCurrency != null and ACTIVITY.costPriceCurrency.currencyId != -1}">
							${ACTIVITY.costPriceCurrency.name}
						</c:when>
						<c:otherwise>
						
						</c:otherwise>														
					</c:choose>		
				</td>
			</tr>
			
			<!-- COST TIME UNIT -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="activity.costTimeUnit"/>
				</td>
				<td>
					<c:choose>
						<c:when test="${ACTIVITY.costTimeUnit eq 1}">
							<spring:message code="time.unit.hour"/>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${ACTIVITY.costTimeUnit eq 2}">
									<spring:message code="time.unit.day"/>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${ACTIVITY.costTimeUnit eq 3}">
											<spring:message code="time.unit.week"/>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${ACTIVITY.costTimeUnit eq 4}">
													<spring:message code="time.unit.month"/>
												</c:when>
											</c:choose>											
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>						
				</td>
			</tr>
			
			<c:if test="${ACTIVITY.projectDetails.projectId ne null}">	
				<!-- BILLING PRICE -->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="activity.billingPrice"/>
					</td>
					<td>
						${ACTIVITY.billingPrice}		
					</td>
				</tr>
				
				<!-- BILLING PRICE CURRENCY -->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="activity.billingPriceCurrency"/>
					</td>
					<td>
						<c:choose>
							<c:when test="${ACTIVITY.billingPriceCurrency != null and ACTIVITY.billingPriceCurrency.currencyId != -1}">
								${ACTIVITY.billingPriceCurrency.name}
							</c:when>
							<c:otherwise>
							
							</c:otherwise>														
						</c:choose>				
					</td>
				</tr>
				
				<!-- BILLING TIME UNIT -->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="activity.billingTimeUnit"/>
					</td>
					<td>
						<c:choose>
							<c:when test="${ACTIVITY.billingTimeUnit eq 1}">
								<spring:message code="time.unit.hour"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${ACTIVITY.billingTimeUnit eq 2}">
										<spring:message code="time.unit.day"/>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${ACTIVITY.billingTimeUnit eq 3}">
												<spring:message code="time.unit.week"/>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${ACTIVITY.billingTimeUnit eq 4}">
														<spring:message code="time.unit.month"/>
													</c:when>
												</c:choose>	
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>						
					</td>
				</tr>
			</c:if>	
		</c:if>	
		
		<!-- DESCRIPTION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="activity.description"/>
			</td>
			<td>
				${ACTIVITY.description}		
			</td>
		</tr>		
						
	</table>	
</c:if>
</div>
