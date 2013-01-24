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

<c:if test="${!(empty TEAM_MEMBER)}">
	
	<table>				
				
		<!-- FIRST NAME -->
		<tr>
			<td class="labelInfoTd" style="width:190px">
				<spring:message code="person.firstName"/>
			</td>
			<td>
				 ${TEAM_MEMBER.firstName}
			</td>
		</tr>
		
		<!-- LAST NAME -->
		<tr>
			<td class="labelInfoTd" style="width:190px">
				<spring:message code="person.lastName"/>
			</td>
			<td>
				 ${TEAM_MEMBER.lastName}
			</td>
		</tr>
						
		<!-- PROJECT -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="person.project"/>
			</td>
			<td>
				 ${PROJECT.name}
			</td>
		</tr>
		
		
		<!-- PROJECT STATUS -->		
		<tr>
			<td class="labelInfoTd">
				<spring:message code="project.status.general"/>
			</td>
			<td>								
				<c:choose>
					<c:when test="${PROJECT.status == 1}">											
						<spring:message code="project.listing.opened"/>		
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${PROJECT.status == 2}">													
								<spring:message code="project.closed"/>		
							</c:when>
							<c:otherwise>
								<c:if test="${PROJECT.status == 3}">														
									<spring:message code="project.abandoned"/>																
								</c:if>																														
							</c:otherwise>	
						</c:choose>														
					</c:otherwise>	
				</c:choose>							
			</td>
		</tr>	
								
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_TeamMemberView}">		
			<c:if test="${!(empty TEAM_MEMBER_DETAIL)}">					
				<tr>
					<td colspan="2">
						<fieldset style="width:395px">
							<legend>
								<spring:message code="team.member.detail.cost.price"/>
							</legend>
								<table>
									<!-- COST PRICE -->
									<tr>
										<td class="labelInfoTd" style="width:175px">
											<spring:message code="team.member.detail.cost.price"/>
										</td>
										<td>			
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.costPrice != null}">
													${TEAM_MEMBER_DETAIL.costPrice}				
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
											<spring:message code="team.member.detail.cost.price.currency"/>
										</td>
										<td>	
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.costPriceCurrency != null and TEAM_MEMBER_DETAIL.costPriceCurrency.currencyId != -1}">
													${TEAM_MEMBER_DETAIL.costPriceCurrency.name}
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
											<spring:message code="team.member.detail.cost.time.unit"/>
										</td>
										<td>
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 1}">
													<spring:message code="time.unit.hour"/>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 2}">
															<spring:message code="time.unit.day"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 3}">
																	<spring:message code="time.unit.week"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 4}">
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
								</table>
						</fieldset>
					</td>
				</tr>			
								
				<tr>
					<td colspan="2">
						<fieldset style="width:395px">
							<legend>
								<spring:message code="team.member.detail.billing.price"/>
							</legend>
								<table>
									<!-- BILLING PRICE -->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="team.member.detail.billing.price"/>
										</td>
										<td>	
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.billingPrice != null}">
													${TEAM_MEMBER_DETAIL.billingPrice}				
												</c:when>
												<c:otherwise>
													<div class="notDefined"><spring:message code="not.defined"/></div>
												</c:otherwise>
											</c:choose>																		
										</td>
									</tr>
									
									<!-- BILLING PRICE CURRENCY-->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="team.member.detail.billing.price.currency"/>
										</td>
										<td>	
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.billingPriceCurrency != null and TEAM_MEMBER_DETAIL.billingPriceCurrency.currencyId != -1}">
													${TEAM_MEMBER_DETAIL.billingPriceCurrency.name}
												</c:when>
												<c:otherwise>
													<div class="notDefined"><spring:message code="not.defined"/></div>
												</c:otherwise>														
											</c:choose>							
										</td>
									</tr>
				
									<!-- BILLIN TIME UNIT -->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="team.member.detail.billing.time.unit"/>
										</td>
										<td>
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 1}">
													<spring:message code="time.unit.hour"/>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 2}">
															<spring:message code="time.unit.day"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 3}">
																	<spring:message code="time.unit.week"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 4}">
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
								</table>
						</fieldset>
					</td>
				</tr>
				
				<tr>
					<td class="tableAddSpacer">&nbsp;</td>
				</tr>	
								
				<tr>
					<td colspan="2">
						<fieldset style="width:395px">
							<legend>
								<spring:message code="person.overtime"/>
							</legend>
								<table>
									<tr>
										<td colspan="2">
											<fieldset style="width:350px">
												<legend>
													<spring:message code="team.member.detail.overtime.cost.price"/>
												</legend>
													<table>
														<!-- OVERTIME COST PRICE -->
														<tr>
															<td class="labelInfoTd" style="width:175px">
																<spring:message code="team.member.detail.overtime.cost.price"/>
															</td>
															<td>																		
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostPrice != null}">
																		${TEAM_MEMBER_DETAIL.overtimeCostPrice}				
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
																<spring:message code="team.member.detail.overtime.cost.currency"/>
															</td>
															<td>
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostCurrency != null and TEAM_MEMBER_DETAIL.overtimeCostCurrency.currencyId != -1}">
																		${TEAM_MEMBER_DETAIL.overtimeCostCurrency.name}
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
																<spring:message code="team.member.detail.overtime.cost.time.unit"/>
															</td>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 1}">
																	<spring:message code="time.unit.hour"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 2}">
																			<spring:message code="time.unit.day"/>
																		</c:when>
																		<c:otherwise>
																			<c:choose>
																				<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 3}">
																					<spring:message code="time.unit.week"/>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 4}">
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
										<td colspan="2">
											<fieldset style="width:350px">
												<legend>
													<spring:message code="team.member.detail.overtime.billing.price"/>
												</legend>
													<table>
														<!-- OVERTIME BILLING PRICE -->
														<tr>
															<td class="labelInfoTd" style="width:175px">
																<spring:message code="team.member.detail.overtime.billing.price"/>
															</td>
															<td>																			
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingPrice != null}">
																		${TEAM_MEMBER_DETAIL.overtimeBillingPrice}				
																	</c:when>
																	<c:otherwise>
																		<div class="notDefined"><spring:message code="not.defined"/></div>
																	</c:otherwise>
																</c:choose>								
															</td>
														</tr>
								
														<!-- OVERTIME BILLING PRICE CURRENCY-->
														<tr>
															<td class="labelInfoTd">
																<spring:message code="team.member.detail.overtime.billing.currency"/>
															</td>
															<td>
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingCurrency != null and TEAM_MEMBER_DETAIL.overtimeBillingCurrency.currencyId != -1}">
																		${TEAM_MEMBER_DETAIL.overtimeBillingCurrency.name}
																	</c:when>
																	<c:otherwise>
																		<div class="notDefined"><spring:message code="not.defined"/></div>
																	</c:otherwise>														
																</c:choose>																			
															</td>
														</tr>
								
														<!-- OVERTIME BILLING TIME UNIT -->
														<tr>
															<td class="labelInfoTd">
																<spring:message code="team.member.detail.overtime.billing.time.unit"/>
															</td>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 1}">
																	<spring:message code="time.unit.hour"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 2}">
																			<spring:message code="time.unit.day"/>
																		</c:when>
																		<c:otherwise>
																			<c:choose>
																				<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 3}">
																					<spring:message code="time.unit.week"/>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 4}">
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
						<spring:message code="team.member.detail.observation"/>
					</td>
					<td>				
						<c:if test="${(TEAM_MEMBER_DETAIL.observation != '') && !(empty TEAM_MEMBER_DETAIL.observation) }">
								${TEAM_MEMBER_DETAIL.observation}
						</c:if>			
					</td>
				</tr>
				
			</c:if>
		</security:authorize>
		
		<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_TeamMemberView}">		
			<c:if test="${IS_MANAGER eq true}">				
				<c:if test="${!(empty TEAM_MEMBER_DETAIL)}">	
						
				<tr>
					<td colspan="2">
						<fieldset style="width:395px">
							<legend>
								<spring:message code="team.member.detail.cost.price"/>
							</legend>
								<table>
									<!-- COST PRICE -->
									<tr>
										<td class="labelInfoTd" style="width:175px">
											<spring:message code="team.member.detail.cost.price"/>
										</td>
										<td>			
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.costPrice != null}">
													${TEAM_MEMBER_DETAIL.costPrice}				
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
											<spring:message code="team.member.detail.cost.price.currency"/>
										</td>
										<td>	
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.costPriceCurrency != null and TEAM_MEMBER_DETAIL.costPriceCurrency.currencyId != -1}">
													${TEAM_MEMBER_DETAIL.costPriceCurrency.name}
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
											<spring:message code="team.member.detail.cost.time.unit"/>
										</td>
										<td>
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 1}">
													<spring:message code="time.unit.hour"/>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 2}">
															<spring:message code="time.unit.day"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 3}">
																	<spring:message code="time.unit.week"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.costTimeUnit eq 4}">
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
								</table>
						</fieldset>
					</td>
				</tr>
											
				<tr>
					<td colspan="2">
						<fieldset style="width:395px">
							<legend>
								<spring:message code="team.member.detail.billing.price"/>
							</legend>
								<table>
									<!-- BILLING PRICE -->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="team.member.detail.billing.price"/>
										</td>
										<td>	
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.billingPrice != null}">
													${TEAM_MEMBER_DETAIL.billingPrice}				
												</c:when>
												<c:otherwise>
													<div class="notDefined"><spring:message code="not.defined"/></div>
												</c:otherwise>
											</c:choose>																		
										</td>
									</tr>
									
									<!-- BILLING PRICE CURRENCY-->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="team.member.detail.billing.price.currency"/>
										</td>
										<td>	
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.billingPriceCurrency != null and TEAM_MEMBER_DETAIL.billingPriceCurrency.currencyId != -1}">
													${TEAM_MEMBER_DETAIL.billingPriceCurrency.name}
												</c:when>
												<c:otherwise>
													<div class="notDefined"><spring:message code="not.defined"/></div>
												</c:otherwise>														
											</c:choose>							
										</td>
									</tr>
				
									<!-- BILLIN TIME UNIT -->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="team.member.detail.billing.time.unit"/>
										</td>
										<td>
											<c:choose>
												<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 1}">
													<spring:message code="time.unit.hour"/>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 2}">
															<spring:message code="time.unit.day"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 3}">
																	<spring:message code="time.unit.week"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.billingTimeUnit eq 4}">
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
								</table>
						</fieldset>
					</td>
				</tr>
				
				<tr>
					<td class="tableAddSpacer">&nbsp;</td>
				</tr>	
								
				<tr>
					<td colspan="2">
						<fieldset style="width:395px">
							<legend>
								<spring:message code="person.overtime"/>
							</legend>
								<table>
									<tr>
										<td colspan="2">
											<fieldset style="width:350px">
												<legend>
													<spring:message code="team.member.detail.overtime.cost.price"/>
												</legend>
													<table>
														<!-- OVERTIME COST PRICE -->
														<tr>
															<td class="labelInfoTd" style="width:175px">
																<spring:message code="team.member.detail.overtime.cost.price"/>
															</td>
															<td>																		
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostPrice != null}">
																		${TEAM_MEMBER_DETAIL.overtimeCostPrice}				
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
																<spring:message code="team.member.detail.overtime.cost.currency"/>
															</td>
															<td>
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostCurrency != null and TEAM_MEMBER_DETAIL.overtimeCostCurrency.currencyId != -1}">
																		${TEAM_MEMBER_DETAIL.overtimeCostCurrency.name}
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
																<spring:message code="team.member.detail.overtime.cost.time.unit"/>
															</td>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 1}">
																	<spring:message code="time.unit.hour"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 2}">
																			<spring:message code="time.unit.day"/>
																		</c:when>
																		<c:otherwise>
																			<c:choose>
																				<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 3}">
																					<spring:message code="time.unit.week"/>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${TEAM_MEMBER_DETAIL.overtimeCostTimeUnit eq 4}">
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
										<td colspan="2">
											<fieldset style="width:350px">
												<legend>
													<spring:message code="team.member.detail.overtime.billing.price"/>
												</legend>
													<table>
														<!-- OVERTIME BILLING PRICE -->
														<tr>
															<td class="labelInfoTd" style="width:175px">
																<spring:message code="team.member.detail.overtime.billing.price"/>
															</td>
															<td>																			
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingPrice != null}">
																		${TEAM_MEMBER_DETAIL.overtimeBillingPrice}				
																	</c:when>
																	<c:otherwise>
																		<div class="notDefined"><spring:message code="not.defined"/></div>
																	</c:otherwise>
																</c:choose>								
															</td>
														</tr>
								
														<!-- OVERTIME BILLING PRICE CURRENCY-->
														<tr>
															<td class="labelInfoTd">
																<spring:message code="team.member.detail.overtime.billing.currency"/>
															</td>
															<td>
																<c:choose>
																	<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingCurrency != null and TEAM_MEMBER_DETAIL.overtimeBillingCurrency.currencyId != -1}">
																		${TEAM_MEMBER_DETAIL.overtimeBillingCurrency.name}
																	</c:when>
																	<c:otherwise>
																		<div class="notDefined"><spring:message code="not.defined"/></div>
																	</c:otherwise>														
																</c:choose>																			
															</td>
														</tr>
								
														<!-- OVERTIME BILLING TIME UNIT -->
														<tr>
															<td class="labelInfoTd">
																<spring:message code="team.member.detail.overtime.billing.time.unit"/>
															</td>
															<c:choose>
																<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 1}">
																	<spring:message code="time.unit.hour"/>
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 2}">
																			<spring:message code="time.unit.day"/>
																		</c:when>
																		<c:otherwise>
																			<c:choose>
																				<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 3}">
																					<spring:message code="time.unit.week"/>
																				</c:when>
																				<c:otherwise>
																					<c:choose>
																						<c:when test="${TEAM_MEMBER_DETAIL.overtimeBillingTimeUnit eq 4}">
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
						<spring:message code="team.member.detail.observation"/>
					</td>
					<td>				
						<c:if test="${(TEAM_MEMBER_DETAIL.observation != '') && !(empty TEAM_MEMBER_DETAIL.observation) }">
								${TEAM_MEMBER_DETAIL.observation}
						</c:if>			
					</td>
				</tr>
				</c:if>
			</c:if>
		</security:authorize>
																	
	</table>	
</c:if>
</div>
