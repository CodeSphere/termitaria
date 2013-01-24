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
<c:set var="showPrices" value="false"/>
<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">	
	<c:set var="showPrices" value="true"/>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_RecordViewCosts}">
	<c:if test="${IS_MANAGER}">
		<c:set var="showPrices" value="true"/>
	</c:if>
</security:authorize>

<c:if test="${!(empty RECORD)}">
	<table>
		<!-- RECORD ID -->
		<tr>
			<td class="labelInfoTd" style="width:190px">
				<spring:message code="record.id"/>
			</td>
			<td>
				${RECORD.recordId}
			</td>
		</tr>
		<!-- RECORD OWNER NAME -->
		<tr>
			<td class="labelInfoTd" style="width:190px">
				<spring:message code="record.owner"/>
			</td>
			<td>
				${RECORD.recordOwnerName} 
			</td>
		</tr>
		
		<!-- PROJECT / ORGANIZATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="record.project.slash.org"/>
			</td>
			<td>
				<c:choose>
					<c:when test="${RECORD.projectDetails ne null}">
						<c:if test="${(RECORD.projectName != '') && !(empty RECORD.projectName )}">
							${RECORD.projectName}
						</c:if>
					</c:when>
					<c:otherwise>
						<spring:message code="record.for.organization"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<c:if test="${RECORD.projectDetails ne null}">
			<!-- PROJECT STATUS -->		
			<tr>
				<td class="labelInfoTd">
					<spring:message code="project.status.general"/>
				</td>
				<td>								
					<c:choose>
						<c:when test="${RECORD.projectDetails.status == 1}">											
							<spring:message code="project.listing.opened"/>		
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${RECORD.projectDetails.status == 2}">													
									<spring:message code="project.closed"/>		
								</c:when>
								<c:otherwise>
									<c:if test="${RECORD.projectDetails.status == 3}">														
										<spring:message code="project.abandoned"/>																
									</c:if>																														
								</c:otherwise>	
							</c:choose>														
						</c:otherwise>	
					</c:choose>							
				</td>
			</tr>
		</c:if>		
		
		<!-- ACTIVITY -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="record.activity"/>
			</td>
			<td>
				<c:if test="${(RECORD.activity.name!= '') && !(empty RECORD.activity.name )}">
					${RECORD.activity.name} 
				</c:if>
			</td>
		</tr>
		
		<!-- DESCRIPTION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="record.description"/>
			</td>
			<td>
				<c:if test="${(RECORD.description != '') && !(empty RECORD.description) }">
					${RECORD.description}
				</c:if>
			</td>
		</tr>
		
		<!-- OBSERVATION -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="record.observation"/>
			</td>
			<td>
				<c:if test="${(RECORD.observation != '') && !(empty RECORD.observation) }">
					${RECORD.observation}
				</c:if>
			</td>
		</tr>
		<c:if test="${RECORD.workHoursRecord eq true}">
			<tr>
				<td colspan="2">
					<fieldset style="width:395px">
						<legend>
							<spring:message code="record.work.hours"/>
						</legend>
							<table>
								<c:if test="${(RECORD.time != '') and !(empty RECORD.time)}">
									<c:if test="${(RECORD.startTime != '') and !(empty RECORD.startTime) and (RECORD.endTime != '') and !(empty RECORD.endTime)}">
										<!-- START TIME -->
										<tr>
											<td class="labelInfoTd" style="width:175px">
												<spring:message code="record.start.date"/>
											</td>
											<td>
												<c:if test="${(RECORD.startTime != '') && !(empty RECORD.startTime )}">
													<fmt:formatDate value="${RECORD.startTime}" type="both" dateStyle="long" timeStyle="short"/>
												</c:if>
											</td>
										</tr>
										
										<!-- END TIME -->
										<tr>
											<td class="labelInfoTd">
												<spring:message code="record.end.date"/>
											</td>
											<td>
												<c:if test="${(RECORD.endTime != '') && !(empty RECORD.endTime )}">
													<fmt:formatDate value="${RECORD.endTime}" type="both" dateStyle="long" timeStyle="short"/>
												</c:if>
											</td>
										</tr>
									</c:if>
									
									<!-- TIME -->
									<tr>
										<td class="labelInfoTd" style="width:175px">
											<spring:message code="record.time"/>
										</td>
										<td>																
											${RECORD.time}					
										</td>
									</tr>
									
								</c:if>					
																																								
								<c:if test="${showPrices eq true}">
								
									<c:if test="${RECORD.projectDetails ne null}">
										<!-- BILLABLE -->
										<c:if test="${(RECORD.billable != '') && !(empty RECORD.billable )}">
											<tr>
												<td class="labelInfoTd" style="width:175px">
													<spring:message code="record.billable"/>
												</td>
												<td>						
													<spring:message code="record.billable.${RECORD.billable}"/>							
												</td>
											</tr>
										</c:if>
									</c:if>
								
									<c:if test="${RECORD.workHoursRecord eq true}">
										<c:choose>
											<c:when test="${RECORD.projectDetails != null && RECORD.projectId != -1}">
												<!-- ====================================TEAM MEMBER COST PRICE========================================================= -->
												<tr>
													<td class="labelInfoTd">
														<spring:message code="record.cost.price.team.member"/>
													</td>			
													<td>								
														${COST_PRICE_TEAM_MEMBER}																									
													</td>
												</tr>
												<!-- ====================================TEAM MEMBER BILLING PRICE========================================================= -->
												<tr>
													<td class="labelInfoTd">
														<spring:message code="record.billing.price.team.member"/>
													</td>			
													<td>								
														${BILLING_PRICE_TEAM_MEMBER}																									
													</td>
												</tr>		
											</c:when>
											<c:otherwise>
												<!-- ====================================PERSON COST PRICE========================================================= -->
												<tr>
													<td class="labelInfoTd">
														<spring:message code="record.cost.price.person"/>
													</td>			
													<td>								
														${COST_PRICE_PERSON}																									
													</td>
												</tr>	
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:if>
							</table>
					</fieldset>
				</td>
			</tr>
		
			<tr>
				<td class="tableAddSpacer">&nbsp;</td>
			</tr>
		</c:if>

		<c:if test="${RECORD.overtimeRecord eq true}">
			<tr>
				<td colspan="2">
					<fieldset>	
						<legend>
							<spring:message code="record.overtime"/>
						</legend>
						<table style="width:395px">
						
							<c:if test="${(RECORD.overTimeTime != '') and !(empty RECORD.overTimeTime)}">
								<c:if test="${(RECORD.overTimeStartTime != '') and !(empty RECORD.overTimeStartTime) and (RECORD.overTimeEndTime != '') and !(empty RECORD.overTimeEndTime)}">
									<!-- OVERTIME START TIME -->
									<tr>
										<td class="labelInfoTd" style="width:175px">
											<spring:message code="record.start.date"/>
										</td>
										<td>
											<c:if test="${(RECORD.overTimeStartTime != '') && !(empty RECORD.overTimeStartTime )}">
												<fmt:formatDate value="${RECORD.overTimeStartTime}" type="both" dateStyle="long" timeStyle="short"/>
											</c:if>
										</td>
									</tr>
													
									<!-- OVERTIME END TIME -->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="record.end.date"/>
										</td>
										<td>
											<c:if test="${(RECORD.overTimeEndTime != '') && !(empty RECORD.overTimeEndTime )}">
												<fmt:formatDate value="${RECORD.overTimeEndTime}" type="both" dateStyle="long" timeStyle="short"/>
											</c:if>	
										</td>
									</tr>
								</c:if>
								
								<!-- OVERTIMETIME -->
								<tr>
									<td class="labelInfoTd" style="width:175px">
										<spring:message code="record.overtime.time"/>
									</td>
									<td>								
										${RECORD.overTimeTime}									
									</td>
								</tr>
														
							</c:if>
																													
							<c:if test="${showPrices eq true}">
							
								<!-- OVERTIME BILLABLE -->
								<c:if test="${RECORD.projectDetails ne null}">
									<c:if test="${(RECORD.overTimeBillable != '') && !(empty RECORD.overTimeBillable )}">
										<tr>
											<td class="labelInfoTd" style="width:175px">
												<spring:message code="record.billable.overtime"/>
											</td>
											<td>							
												<spring:message code="record.billable.${RECORD.overTimeBillable}"/>
											</td>
										</tr>
									</c:if>
								</c:if>
								
								<c:if test="${RECORD.overtimeRecord eq true}">
									<c:choose>
										<c:when test="${RECORD.projectDetails != null && RECORD.projectId != -1}">
											<!-- ====================================TEAM MEMBER OVERTIME COST PRICE========================================================= -->
											<tr>
												<td class="labelInfoTd">
													<spring:message code="record.overtime.cost.price.team.member"/>
												</td>			
												<td>								
													${OVERTIME_COST_PRICE_TEAM_MEMBER}																									
												</td>
											</tr>	
											<!-- ====================================TEAM MEMBER OVERTIME BILLING PRICE========================================================= -->
											<tr>
												<td class="labelInfoTd">
													<spring:message code="record.overtime.billing.price.team.member"/>
												</td>			
												<td>								
													${OVERTIME_BILLING_PRICE_TEAM_MEMBER}																									
												</td>
											</tr>	
										</c:when>
										<c:otherwise>
											<!-- ====================================PERSON OVERTIME COST PRICE========================================================= -->
											<tr>
												<td class="labelInfoTd">
													<spring:message code="record.overtime.cost.price.person"/>
												</td>			
												<td>								
													${OVERTIME_COST_PRICE_PERSON}																									
												</td>
											</tr>	
										</c:otherwise>
									</c:choose>	
								</c:if>
							</c:if>
						</table>
					</fieldset>
				</td>
			</tr>
			<tr>
				<td class="tableAddSpacer">&nbsp;</td>
			</tr>
		</c:if>
		
		<tr>
			<td colspan="2">
				<c:if test="${showPrices eq true}">
					<fieldset style="width:395px">
						<legend>
							<spring:message code="record.activity.price.details"/>
						</legend>
						<table style="width:100%">
							<!-- ====================================ACTIVITY BILLABLE========================================================= -->
							<tr>
								<td class="labelInfoTd" style="width:175px">
									<spring:message code="record.activity.billable"/>
								</td>			
								<td>								
									<spring:message code="record.billable.${ACTIVITY.billable}"/>																									
								</td>
							</tr>
							<!-- ====================================ACTIVITY COST PRICE========================================================= -->
							<tr>
								<td class="labelInfoTd">
									<spring:message code="record.cost.price.activity"/>
								</td>			
								<td>													
									${COST_PRICE_ACTIVITY}																																												
								</td>
							</tr>	
							<!-- ====================================ACTIVITY BILLING PRICE========================================================= -->								
							<tr>
								<td class="labelInfoTd">
									<spring:message code="record.billing.price.activity"/>
								</td>			
								<td>								
									${BILLING_PRICE_ACTIVITY}																									
								</td>
							</tr>
						</table>
					</fieldset>
				</c:if>
			</td>
		</tr>
	</table>
</c:if>
</div>
