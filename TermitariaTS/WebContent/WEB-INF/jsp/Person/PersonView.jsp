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
				
		<!-- FIRST NAME -->
		<tr>
			<td class="labelInfoTd" style="width:190px">
				<spring:message code="person.firstName"/>
			</td>
			<td>
				 ${PERSON.firstName}
			</td>
		</tr>
		
		<!-- LAST NAME -->
		<tr>
			<td class="labelInfoTd" style="width:190px">
				<spring:message code="person.lastName"/>
			</td>
			<td>
				 ${PERSON.lastName}
			</td>
		</tr>
		
		<!-- EMAIL -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.email"/>
			</td>
			<td>
				 ${PERSON.email}
			</td>
		</tr>
		
		<!-- PROJECT -->
		<tr>
			<td class="labelInfoTd">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonAdvancedView}">
					<spring:message code="person.project.slash.org"/>
				</security:authorize>
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_PersonAdvancedView}">
					<spring:message code="person.project"/>
				</security:authorize>
			</td>
			<td>
				<spring:message code="project.from.organization"/>	
			</td>
		</tr>
								
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_PersonAdvancedView}">						
			<c:if test="${!(empty PERSON_DETAIL)}">										
				<!-- COST PRICE -->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="person.detail.cost.price"/>
					</td>
					<td>
						<c:choose>
							<c:when test="${PERSON_DETAIL.costPrice != null}">
								${PERSON_DETAIL.costPrice}				
							</c:when>
							<c:otherwise>
								<div class="notDefined"><spring:message code="not.defined"/></div>
							</c:otherwise>
						</c:choose>														
					</td>
				</tr>
				
				<!-- COST PRICE CURRENCY-->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="person.detail.cost.price.currency"/>
					</td>
					<td>	
						<c:choose>
							<c:when test="${PERSON_DETAIL.costPriceCurrency != null and PERSON_DETAIL.costPriceCurrency.currencyId != -1}">
								${PERSON_DETAIL.costPriceCurrency.name}
							</c:when>
							<c:otherwise>
								<div class="notDefined"><spring:message code="not.defined"/></div>
							</c:otherwise>														
						</c:choose>							
					</td>
				</tr>
				
				<!-- COST TIME UNIT -->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="person.detail.cost.time.unit"/>
					</td>
					<td>
						<c:choose>
							<c:when test="${PERSON_DETAIL.costTimeUnit eq 1}">
								<spring:message code="time.unit.hour"/>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${PERSON_DETAIL.costTimeUnit eq 2}">
										<spring:message code="time.unit.day"/>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${PERSON_DETAIL.costTimeUnit eq 3}">
												<spring:message code="time.unit.week"/>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${PERSON_DETAIL.costTimeUnit eq 4}">
														<spring:message code="time.unit.month"/>
													</c:when>
													<c:otherwise>
														<div class="notDefined"><spring:message code="not.defined"/></div>
													</c:otherwise>
												</c:choose>												
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>						
					</td>
				</tr>
				
				<tr>
					<td colspan="2">
						<fieldset style="width:395px">
							<legend>
								<spring:message code="person.overtime"/>
							</legend>
								<table>
									<!-- OVERTIME COST PRICE -->
									<tr>
										<td class="labelInfoTd" style="width:175px">
											<spring:message code="person.detail.overtime.cost.price"/>
										</td>
										<td>		
											<c:choose>
												<c:when test="${PERSON_DETAIL.costPrice != null}">
													${PERSON_DETAIL.costPrice}				
												</c:when>
												<c:otherwise>
													<div class="notDefined"><spring:message code="not.defined"/></div>
												</c:otherwise>
											</c:choose>																										
										</td>
									</tr>
								
									<!-- OVERTIME COST PRICE CURRENCY-->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="person.detail.overtime.cost.currency"/>
										</td>
										<td>
											<c:choose>
												<c:when test="${PERSON_DETAIL.overtimeCostCurrency != null and PERSON_DETAIL.overtimeCostCurrency.currencyId != -1}">
													${PERSON_DETAIL.overtimeCostCurrency.name}
												</c:when>
												<c:otherwise>
													<div class="notDefined"><spring:message code="not.defined"/></div>
												</c:otherwise>														
											</c:choose>																			
										</td>
									</tr>
									
									<!-- OVERTIME COST TIME UNIT -->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="person.detail.overtime.cost.time.unit"/>
										</td>
										<c:choose>
										<c:when test="${PERSON_DETAIL.overtimeCostTimeUnit eq 1}">
											<spring:message code="time.unit.hour"/>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${PERSON_DETAIL.overtimeCostTimeUnit eq 2}">
													<spring:message code="time.unit.day"/>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${PERSON_DETAIL.overtimeCostTimeUnit eq 3}">
															<spring:message code="time.unit.week"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${PERSON_DETAIL.overtimeCostTimeUnit eq 4}">
																	<spring:message code="time.unit.month"/>
																</c:when>
																<c:otherwise>
																	<div class="notDefined"><spring:message code="not.defined"/></div>
																</c:otherwise>
															</c:choose>	
														</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>			
								</tr>			
							</table>
						</fieldset>
					</td>
				</tr>
				
				<tr>
					<td class="tableAddSpacer">&nbsp;</td>
				</tr>
																									
				<!-- OBSERVATION -->
				<tr>
					<td class="labelInfoTd">
						<spring:message code="person.detail.observation"/>
					</td>
					<td>				
						<c:if test="${(PERSON_DETAIL.observation != '') && !(empty PERSON_DETAIL.observation) }">
								${PERSON_DETAIL.observation}
						</c:if>			
					</td>
				</tr>
				
			</c:if>
		</security:authorize>																
	</table>	
</c:if>
</div>
