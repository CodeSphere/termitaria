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

<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">				
	<c:set var="cssValidateClass" value="formField"/>
</security:authorize>
<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">				 
	<c:set var="cssValidateClass" value="formField validate-not-first"/>
</security:authorize>

<c:choose>
	<c:when test="${activityBean.costPrice ne null}">
		<c:set var="cssCostClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssCostClass" value="formField"/>		
	</c:otherwise>
</c:choose>	

<c:choose>
	<c:when test="${activityBean.billingPrice ne null}">
		<c:set var="cssBillingClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssBillingClass" value="formField"/>		
	</c:otherwise>
</c:choose>

<c:if test="${GET_FROM_PANEL eq null}">

	<c:choose>
		<c:when test="${activityBean.activityId > 0}">
			<c:set var="cssClass" value=""/>
		</c:when>
		<c:otherwise>
			<c:set var="cssClass" value="inactive"/>		
		</c:otherwise>
	</c:choose>
	
	<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

	<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td id="submenuCell">
				<div id="submenu">				
					<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->		
						
					<a href="#" id="back" onclick="ENTITY = getActivityData();check_add('${BACK_URL}&projectId=${projectId}&hasProjectDetail=true', 'MAIN_CONTENT');">
						<spring:message code="back"/>
					</a>	
						
					<!-- ADD NEW ACTIVITY -->
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
						<a href="#" id="addNewActivity" title="<spring:message code="activity.submeniu.add.title"/>"><spring:message code='add'/></a>
					</security:authorize>	
					<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
						<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">		
							<a href="#" id="addNewActivity" title="<spring:message code="activity.submeniu.add.title"/>"><spring:message code='add'/></a>
						</c:if>
					</security:authorize>							
					
					<!-- DELETE ACTIVITY -->
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">
						<a href="#" id="delete" class="${cssClass}" title="<spring:message code="activity.submeniu.delete.title"/>"><spring:message code="delete"/></a>
					</security:authorize>	
					<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">
						<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">		
							<a href="#" id="delete" class="${cssClass}" title="<spring:message code="activity.submeniu.delete.title"/>"><spring:message code="delete"/></a>
						</c:if>
					</security:authorize>
					
					<!-- SEARCH ACTIVITY 
					<a href="#" id="submenuSearch" title="<spring:message code="activity.submeniu.search.title"/>"><spring:message code="search"/></a>
						-->					
				</div>
				<div id="submenu_footer"></div>
	        </td>
	        <td id="contentCell">
	        	<div id="CONTENT">
	<!-- //////////////////////////////////////////////////////////////////////////// -->
	
	<c:choose>
		<c:when test="${activityBean.activityId > 0}">
			<span class="section_title"><img src="images/titles/ActivityEdit.jpg"><spring:message code="activity.update.section.title"/></span>
		</c:when>
		<c:otherwise>
			<span class="section_title"><img src="images/titles/ActivityAdd.jpg"><spring:message code="activity.add.section.title"/></span>
		</c:otherwise>
	</c:choose>

</c:if>

<c:choose>
	<c:when test="${GET_FROM_PANEL eq false}">
		<div id="LOCAL_MESSAGES_CONTENT">
			<%@ include file="../Messages.jsp" %>
		</div>
	</c:when>
	<c:otherwise>		
		<div id="LOCAL_MESSAGES_CONTENT"/>
			<%@ include file="../MessagesPanel.jsp" %>
		</div>				
	</c:otherwise>
</c:choose>

<c:if test="${GET_FROM_PANEL eq true and  ONSUBMIT eq true}">
	<form:form name="activityFormResult" id="activityFormResult" commandName="activityBean" onsubmit="return false;" >	
		<form:hidden id="ACTIVITYFORM_ID" path="activityId" />
		<form:hidden id="ACTIVITYFORM_NAME" path="name"/>
		<form:hidden id="ACTIVITYFORM_PROJECT" path="projectId"/>
		<form:hidden path="activityId" />	
		<form:hidden path="organizationId" />	
	</form:form>
</c:if>

<c:if test="${GET_FROM_PANEL eq null or ONSUBMIT eq null}">	
	<c:choose>
		<c:when test="${GET_FROM_PANEL eq true}">
			<c:set var="i" value="Panel"/>
		</c:when>
		<c:otherwise>
			<c:set var="i" value=""/>
		</c:otherwise>
	</c:choose>

	<form:form method="post" commandName="activityBean" id="activityForm${i}" name="activityForm${i}" onsubmit="return false;">	
	<form:hidden path="activityId" id="activityId" />		
	<input type="hidden" name="organizationId" value="${ORGANIZATION_ID}" />
	
		<c:choose>
			<c:when test="${GET_FROM_PANEL eq null}">
				<table cellpadding="3" class="tableAdd" width="430px">				
			</c:when>
			<c:otherwise>
				<table cellpadding="3" class="tableAdd" width="430px">
			</c:otherwise>
		</c:choose>
		
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>	
																																																
		<!-- =========================================NAME======================================================== -->
		<tr>
			<td class="labelTd" style="width: 180px" >
				<spring:message code="activity.name"/> <span id="required">*</span>
			</td>
			<td>			
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<form:input id="activityFormPanel_name" path="name" tabindex="1" cssClass="formField required" maxlength="200" onkeypress="return restrictSpecialCharacters(event);"/>
					</c:when>
					<c:otherwise>
						<form:input id="activityForm_name" path="name" tabindex="1" cssClass="formField required" maxlength="200" onkeypress="return restrictSpecialCharacters(event);"/>							
					</c:otherwise>
				</c:choose>														
			</td>
		</tr>		
		
		<!-- ====================================PROJECT========================================================= -->
		<tr>
			<td class="labelTd">				
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
					<spring:message code="activity.project"/> 
				</security:authorize>
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
					<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">		
						<spring:message code="activity.project.pm"/> 			
					</c:if>
				</security:authorize>
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAdvancedSearch}">				 
					<span id="required">*</span>
				</security:authorize>
			</td>			
			<td>		
			<c:choose>
				<c:when test="${activityBean.projectId ne null }">
						<form:hidden path="projectId" id="activityForm_project"/>
						<c:forEach var="project" items="${PROJECTS}">
							<c:if test="${project.projectId == activityBean.projectId}">
								${project.name}	
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<c:choose>
							<c:when test="${PROJECTS ne null}">																													
								<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">		
									<div title='<spring:message code="activity.choose.project"/>' id="activityFormPanelProjectsId">	
										<select name="projectId" id="activityFormPanel_project"  tabindex="2" class="${cssValidateClass}" onchange="javascript:changeTitle(this.options[selectedIndex].title, 'activityFormPanelProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'activityFormPanel_project', 'activityFormPanel_project1');">
											<option value="-1" class="chooseProject" title='<spring:message code="activity.choose.project"/>' ><spring:message code="activity.choose.project"/></option>															
											<c:forEach var="project" items="${PROJECTS}">
											
												
												<c:choose>
													<c:when test="${project.clientId != -1}">
														<c:set var="client" value="hasClient"/>	
														
														<c:choose>
															<c:when test="${project.status == 1}">
																<c:set var="prj" value="${project.name}"/>	
															</c:when>
															<c:otherwise>
																<spring:message code="project.for.client" var="prj"/>	
															</c:otherwise>
														</c:choose>																									
													</c:when>
													<c:otherwise>
														<c:set var="client" value="noClient"/>	
														<spring:message code="project.for.organization" var="prj"/>	
													</c:otherwise>	
												</c:choose>	
										
												<c:choose>
													<c:when test="${project.status == 1}">
														<c:set var="type" value="Opened"/>	
														<spring:message code="project.opened" var="Type"/>		
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${project.status == 2}">
																<c:set var="type" value="Closed"/>	
																<spring:message code="project.closed" var="Type"/>		
															</c:when>
															<c:otherwise>
																<c:if test="${project.status == 3}">
																	<c:set var="type" value="Aborted"/>	
																	<spring:message code="project.abandoned" var="Type"/>																
																</c:if>																														
															</c:otherwise>	
														</c:choose>														
													</c:otherwise>	
												</c:choose>	
											
												<c:choose>
													<c:when test="${activityBean.projectId == project.projectId}">
														<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>
													</c:when>
													<c:otherwise>
														<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>																						
										</select>
										
									</div>
									
									<div id="activityFormPanel_project1">
										<span class="chooseProject"></span>																		
										<c:forEach var="project" items="${PROJECTS}">	
											<c:choose>
												<c:when test="${project.clientId != -1}">
													<c:set var="client" value="hasClient"/>																																																		
												</c:when>
												<c:otherwise>
													<c:set var="client" value="noClient"/>													
												</c:otherwise>	
											</c:choose>	
										
											<c:choose>
												<c:when test="${project.status == 1}">
													<c:set var="type" value="Opened"/>												
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${project.status == 2}">
															<c:set var="type" value="Closed"/>														
														</c:when>
														<c:otherwise>
															<c:if test="${project.status == 3}">
																<c:set var="type" value="Aborted"/>																									
															</c:if>																														
														</c:otherwise>	
													</c:choose>														
												</c:otherwise>	
											</c:choose>																																										
											<span class="${client}${type}"></span>												
										</c:forEach>		
									</div>
										
									
								</security:authorize>
									
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
									<div title='<spring:message code="activity.from.organization"/>' id="activityFormPanelProjectsId">	
										<select name="projectId" id="activityFormPanel_project"  tabindex="2" class="${cssValidateClass}" onchange="javascript:getContentFromUrlDirect('ActivityBillingInformations.htm?GET_FROM_PANEL=true', 'BILLING_PANEL_INFORMATIONS');showRow('BILLING_PANEL_INFORMATIONS', this.options[selectedIndex].value, 'billingPriceId', 'billingPriceCurrencyId', 'billingTimeUnitId'); changeTitle(this.options[selectedIndex].title, 'activityFormPanelProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'activityFormPanel_project', 'activityFormPanel_project1');">																						
											<option value="-1" class="fromOrganization" title='<spring:message code="activity.from.organization"/>' ><spring:message code="activity.from.organization"/></option>			
											<c:forEach var="project" items="${PROJECTS}">											
												<c:choose>
													<c:when test="${project.clientId != -1}">
														<c:set var="client" value="hasClient"/>	
														
														<c:choose>
															<c:when test="${project.status == 1}">
																<c:set var="prj" value="${project.name}"/>	
															</c:when>
															<c:otherwise>
																<spring:message code="project.for.client" var="prj"/>	
															</c:otherwise>
														</c:choose>																									
													</c:when>
													<c:otherwise>
														<c:set var="client" value="noClient"/>	
														<spring:message code="project.for.organization" var="prj"/>	
													</c:otherwise>	
												</c:choose>	
										
												<c:choose>
													<c:when test="${project.status == 1}">
														<c:set var="type" value="Opened"/>	
														<spring:message code="project.opened" var="Type"/>		
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${project.status == 2}">
																<c:set var="type" value="Closed"/>	
																<spring:message code="project.closed" var="Type"/>		
															</c:when>
															<c:otherwise>
																<c:if test="${project.status == 3}">
																	<c:set var="type" value="Aborted"/>	
																	<spring:message code="project.abandoned" var="Type"/>																
																</c:if>																														
															</c:otherwise>	
														</c:choose>														
													</c:otherwise>	
												</c:choose>												
											
												<c:choose>
													<c:when test="${activityBean.projectId == project.projectId}">
														<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>
													</c:when>
													<c:otherwise>
														<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>
													</c:otherwise>
												</c:choose>
												
											</c:forEach>																						
										</select>
									</div>	
									
									<div id="activityFormPanel_project1">
										<span class="fromOrganization"></span>																		
										<c:forEach var="project" items="${PROJECTS}">	
											<c:choose>
												<c:when test="${project.clientId != -1}">
													<c:set var="client" value="hasClient"/>																																																		
												</c:when>
												<c:otherwise>
													<c:set var="client" value="noClient"/>													
												</c:otherwise>	
											</c:choose>	
										
											<c:choose>
												<c:when test="${project.status == 1}">
													<c:set var="type" value="Opened"/>												
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${project.status == 2}">
															<c:set var="type" value="Closed"/>														
														</c:when>
														<c:otherwise>
															<c:if test="${project.status == 3}">
																<c:set var="type" value="Aborted"/>																									
															</c:if>																														
														</c:otherwise>	
													</c:choose>														
												</c:otherwise>	
											</c:choose>																																										
											<span class="${client}${type}"></span>												
										</c:forEach>		
									</div>
										
								</security:authorize>																				
							</c:when>
							<c:when test="${PROJECTS ne null  and empty PROJECTS}">
								<div id="NO_PROJECTS">
									<spring:message code="activity.no.projects"></spring:message>
								</div>
								<div id="MESSAGE">
								</div>
							</c:when>
						</c:choose>	
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${PROJECTS ne null}">																				
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
										<div title='<spring:message code="activity.choose.project"/>' id="activityFormProjectsId">			
											<select name="projectId" id="activityForm_project"  tabindex="2" class="${cssValidateClass}" onchange="javascript:changeTitle(this.options[selectedIndex].title, 'activityFormProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'activityForm_project', 'activityForm_project1');">
												<option value="-1" class="chooseProject" title='<spring:message code="activity.choose.project"/>' ><spring:message code="activity.choose.project"/></option>															
												<c:forEach var="project" items="${PROJECTS}">												
													<c:choose>
														<c:when test="${project.clientId != -1}">
															<c:set var="client" value="hasClient"/>	
															
															<c:choose>
																<c:when test="${project.status == 1}">
																	<c:set var="prj" value="${project.name}"/>	
																</c:when>
																<c:otherwise>
																	<spring:message code="project.for.client" var="prj"/>	
																</c:otherwise>
															</c:choose>																									
														</c:when>
														<c:otherwise>
															<c:set var="client" value="noClient"/>	
															<spring:message code="project.for.organization" var="prj"/>	
														</c:otherwise>	
													</c:choose>	
											
													<c:choose>
														<c:when test="${project.status == 1}">
															<c:set var="type" value="Opened"/>	
															<spring:message code="project.opened" var="Type"/>		
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${project.status == 2}">
																	<c:set var="type" value="Closed"/>	
																	<spring:message code="project.closed" var="Type"/>		
																</c:when>
																<c:otherwise>
																	<c:if test="${project.status == 3}">
																		<c:set var="type" value="Aborted"/>	
																		<spring:message code="project.abandoned" var="Type"/>																
																	</c:if>																														
																</c:otherwise>	
															</c:choose>														
														</c:otherwise>	
													</c:choose>	
													
													<c:choose>
														<c:when test="${activityBean.projectId == project.projectId}">
															<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>
														</c:when>
														<c:otherwise>
															<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>																						
											</select>
										</div>
										
										<div id="activityForm_project1">
											<span class="chooseProject"></span>																		
											<c:forEach var="project" items="${PROJECTS}">	
												<c:choose>
													<c:when test="${project.clientId != -1}">
														<c:set var="client" value="hasClient"/>																																																		
													</c:when>
													<c:otherwise>
														<c:set var="client" value="noClient"/>													
													</c:otherwise>	
												</c:choose>	
											
											<c:choose>
												<c:when test="${project.status == 1}">
													<c:set var="type" value="Opened"/>												
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${project.status == 2}">
															<c:set var="type" value="Closed"/>														
														</c:when>
														<c:otherwise>
															<c:if test="${project.status == 3}">
																<c:set var="type" value="Aborted"/>																									
															</c:if>																														
														</c:otherwise>	
													</c:choose>														
												</c:otherwise>	
											</c:choose>																																										
											<span class="${client}${type}"></span>												
										</c:forEach>		
									</div>
										
									</security:authorize>
																									
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
										<div title='<spring:message code="activity.from.organization"/>' id="activityFormProjectsId">	
											<select name="projectId" id="activityForm_project"  tabindex="2" class="${cssValidateClass}" onchange="javascript:getContentFromUrlDirect('ActivityBillingInformations.htm', 'BILLING_INFORMATIONS'); showRow('BILLING_INFORMATIONS', this.options[selectedIndex].value, 'billingPriceId', 'billingPriceCurrencyId', 'billingTimeUnitId'); changeTitle(this.options[selectedIndex].title, 'activityFormProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'activityForm_project', 'activityForm_project1');">																						
												<option value="-1" class="fromOrganization" title='<spring:message code="activity.from.organization"/>'><spring:message code="activity.from.organization"/></option>			
												<c:forEach var="project" items="${PROJECTS}">													
													<c:choose>
														<c:when test="${project.clientId != -1}">
															<c:set var="client" value="hasClient"/>	
															
															<c:choose>
																<c:when test="${project.status == 1}">
																	<c:set var="prj" value="${project.name}"/>	
																</c:when>
																<c:otherwise>
																	<spring:message code="project.for.client" var="prj"/>	
																</c:otherwise>
															</c:choose>																									
														</c:when>
														<c:otherwise>
															<c:set var="client" value="noClient"/>	
															<spring:message code="project.for.organization" var="prj"/>	
														</c:otherwise>	
													</c:choose>	
											
													<c:choose>
														<c:when test="${project.status == 1}">
															<c:set var="type" value="Opened"/>	
															<spring:message code="project.opened" var="Type"/>		
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${project.status == 2}">
																	<c:set var="type" value="Closed"/>	
																	<spring:message code="project.closed" var="Type"/>		
																</c:when>
																<c:otherwise>
																	<c:if test="${project.status == 3}">
																		<c:set var="type" value="Aborted"/>	
																		<spring:message code="project.abandoned" var="Type"/>																
																	</c:if>																														
																</c:otherwise>	
															</c:choose>														
														</c:otherwise>	
													</c:choose>	
												
													<c:choose>
														<c:when test="${activityBean.projectId == project.projectId}">
															<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>
														</c:when>
														<c:otherwise>
															<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>																						
											</select>
										</div>
										
										<div id="activityForm_project1">
											<span class="fromOrganization"></span>																		
											<c:forEach var="project" items="${PROJECTS}">	
												<c:choose>
													<c:when test="${project.clientId != -1}">
														<c:set var="client" value="hasClient"/>																																																		
													</c:when>
													<c:otherwise>
														<c:set var="client" value="noClient"/>													
													</c:otherwise>	
												</c:choose>	
											
											<c:choose>
												<c:when test="${project.status == 1}">
													<c:set var="type" value="Opened"/>												
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${project.status == 2}">
															<c:set var="type" value="Closed"/>														
														</c:when>
														<c:otherwise>
															<c:if test="${project.status == 3}">
																<c:set var="type" value="Aborted"/>																									
															</c:if>																														
														</c:otherwise>	
													</c:choose>														
												</c:otherwise>	
											</c:choose>																																										
											<span class="${client}${type}"></span>												
										</c:forEach>		
									</div>
										
									</security:authorize>								
															
							</c:when>
							<c:when test="${PROJECTS ne null  and empty PROJECTS}">
								<div id="NO_PROJECTS">
									<spring:message code="activity.no.projects"></spring:message>
								</div>
								<div id="MESSAGE">
								</div>
							</c:when>
						</c:choose>											
					</c:otherwise>
				</c:choose>	
			</c:otherwise>
			</c:choose>																					
			</td>
		</tr>
					
		<!-- ====================================COST PRICE=================================================== -->
		<tr>			
			<td class="labelTd">
				<spring:message code="activity.costPrice"/>
			</td>
			<td>				
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<form:input path="costPrice" id="activityFormPanel_costPrice" tabindex="5" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('activityFormPanel_costPrice')"/>
					</c:when>
					<c:otherwise>
						<form:input path="costPrice" id="activityForm_costPrice" tabindex="5" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('activityForm_costPrice')"/>	
					</c:otherwise>
				</c:choose>							
			</td>	
		</tr>
								
		<!-- ====================================COST PRICE CURRENCY=================================================== -->
		<tr>			
			<td class="labelTd">
				<spring:message code="activity.costPriceCurrency"/> <c:choose><c:when test="${activityBean.costPrice eq null}"><span id="costPriceCurrencyRequiredId${i}" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="costPriceCurrencyRequiredId${i}" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
			</td>
			<td>	
			
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<form:select path="costPriceCurrencyId" id="activityFormPanel_costPriceCurrency" cssClass="${cssCostClass}" tabindex="6">
	 						<form:option value="-1" title='<spring:message code="activity.choose.cost.price.currency"/>'><spring:message code="activity.choose.cost.price.currency"/></form:option> 				
		 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
								<c:choose>
									<c:when test="${currency.value == activityBean.costPriceCurrencyId}">
										<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
									</c:when>
									<c:otherwise>
										<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>	
		 				</form:select>	
					</c:when>
					<c:otherwise>
						<form:select path="costPriceCurrencyId" id="activityForm_costPriceCurrency" cssClass="${cssCostClass}" tabindex="6">
	 						<form:option value="-1" title='<spring:message code="activity.choose.cost.price.currency"/>'><spring:message code="activity.choose.cost.price.currency"/></form:option> 				
		 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
								<c:choose>
									<c:when test="${currency.value == activityBean.costPriceCurrencyId}">
										<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
									</c:when>
									<c:otherwise>
										<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>	
		 				</form:select>	
					</c:otherwise>
				</c:choose>																			
			</td>	
		</tr>	
				
		<!-- ====================================COST TIME UNIT=================================================== -->
		<tr>			
			<td class="labelTd">
				<spring:message code="activity.costTimeUnit"/> <c:choose><c:when test="${activityBean.costPrice eq null}"><span id="costTimeUnitRequiredId${i}" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="costTimeUnitRequiredId${i}" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>				
			</td>
			<td>	
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<div title='<spring:message code="activity.choose.costTimeUnit"/>' id="activityFormPanelCostTimeUnitsId">			
							<form:select path="costTimeUnit" id="activityFormPanel_costTimeUnit" tabindex="7" cssClass="${cssCostClass}" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormPanelCostTimeUnitsId')">
								<form:option value="-1"><spring:message code="activity.choose.costTimeUnit"></spring:message></form:option>									
									<c:forEach var="timeUnit" items="${NOM_TIME_UNIT}">																
										<c:choose>
											<c:when test="${timeUnit.value == activityBean.costTimeUnit}">
												<option title='<spring:message code="${timeUnit.label}"/>'  value="${timeUnit.value}" SELECTED><spring:message code="${timeUnit.label}"/></option>
											</c:when>
											<c:otherwise>
												<option title='<spring:message code="${timeUnit.label}"/>' value="${timeUnit.value}"><spring:message code="${timeUnit.label}"/></option>
											</c:otherwise>
										</c:choose>									
									</c:forEach>							
							</form:select>	
						</div>
					</c:when>
					<c:otherwise>
						<div title='<spring:message code="activity.choose.costTimeUnit"/>' id="activityFormCostTimeUnitsId">			
							<form:select path="costTimeUnit" id="activityForm_costTimeUnit" tabindex="7" cssClass="${cssCostClass}" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormCostTimeUnitsId')">
								<form:option value="-1"><spring:message code="activity.choose.costTimeUnit"></spring:message></form:option>									
									<c:forEach var="timeUnit" items="${NOM_TIME_UNIT}">																
										<c:choose>
											<c:when test="${timeUnit.value == activityBean.costTimeUnit}">
												<option title='<spring:message code="${timeUnit.label}"/>'  value="${timeUnit.value}" SELECTED><spring:message code="${timeUnit.label}"/></option>
											</c:when>
											<c:otherwise>
												<option title='<spring:message code="${timeUnit.label}"/>' value="${timeUnit.value}"><spring:message code="${timeUnit.label}"/></option>
											</c:otherwise>
										</c:choose>									
									</c:forEach>							
							</form:select>	
						</div>			
					</c:otherwise>
				</c:choose>																													
			</td>
		</tr>
		
		<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
			
			<!-- ====================================BILLABLE========================================================= -->
			<tr> 
				<td class="labelTd"> 
					<spring:message code="activity.billable" />
				</td> 
				<td colspan="2"> 
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">
							<font class="labelTd"><spring:message code="billable.yes" /></font>
								<form:radiobutton path="billable" value="Y" tabindex="4" id="activityFormPanel_billable"/>
							<font class="labelTd"><spring:message code="billable.no" /></font> 								 
								<form:radiobutton path="billable" value="N" tabindex="3" id="activityFormPanel_billable"/> 
						</c:when>
						<c:otherwise>
							<font class="labelTd"><spring:message code="billable.yes" /></font>
								<form:radiobutton path="billable" value="Y" tabindex="4" id="activityForm_billable"/>
							<font class="labelTd"><spring:message code="billable.no" /></font> 								 	
								<form:radiobutton path="billable" value="N" tabindex="3" id="activityForm_billable"/> 		
						</c:otherwise>
					</c:choose>											
				</td>		
			</tr> 
																
			<!-- ====================================BILLING PRICE=================================================== -->
			<tr>			
				<td class="labelTd">
					<spring:message code="activity.billingPrice"/> 
				</td>
				<td>
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">
							<form:input path="billingPrice" id="activityFormPanel_billingPrice" tabindex="8" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('activityFormPanel_billingPrice')"/>
						</c:when>
						<c:otherwise>
							<form:input path="billingPrice" id="activityForm_billingPrice" tabindex="8" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('activityForm_billingPrice')"/>		
						</c:otherwise>
					</c:choose>						
				</td>	
			</tr>
			
			<!-- ====================================BILLING PRICE CURRENCY=================================================== -->
			<tr>			
				<td class="labelTd">
					<spring:message code="activity.billingPriceCurrency"/> <c:choose><c:when test="${activityBean.billingPrice eq null}"><span id="billingPriceCurrencyRequiredId${i}" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="billingPriceCurrencyRequiredId${i}" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>					
				</td>
				<td>			
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">
							<form:select path="billingPriceCurrencyId" id="activityFormPanel_billingPriceCurrency" cssClass="${cssBillingClass}" tabindex="9">
			 					<form:option value="-1" title='<spring:message code="activity.choose.billing.price.currency"/>'><spring:message code="activity.choose.billing.price.currency"/></form:option>
				 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
			 						<c:choose>
			 							<c:when test="${currency.value == activityBean.billingPriceCurrencyId}">
			 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
			 							</c:when>
			 							<c:otherwise>
			 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
			 							</c:otherwise>
			 						</c:choose>
			 					</c:forEach>
			 				</form:select>
						</c:when>
						<c:otherwise>
							<form:select path="billingPriceCurrencyId" id="activityForm_billingPriceCurrency" cssClass="${cssBillingClass}" tabindex="9">
			 					<form:option value="-1" title='<spring:message code="activity.choose.billing.price.currency"/>'><spring:message code="activity.choose.billing.price.currency"/></form:option>
				 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
			 						<c:choose>
			 							<c:when test="${currency.value == activityBean.billingPriceCurrencyId}">
			 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
			 							</c:when>
			 							<c:otherwise>
			 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
			 							</c:otherwise>
			 						</c:choose>
			 					</c:forEach>
			 				</form:select>
						</c:otherwise>
					</c:choose>									
				</td>	
			</tr>
					
			<!-- ====================================BILLING TIME UNIT=================================================== -->
			<tr>			
				<td class="labelTd">
					<spring:message code="activity.billingTimeUnit"/> <c:choose><c:when test="${activityBean.billingPrice eq null}"><span id="billingTimeUnitRequiredId${i}" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="billingTimeUnitRequiredId${i}" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>					
				</td>
				<td>						
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">
							<div title='<spring:message code="activity.choose.billingTimeUnit"/>' id="activityFormPanelBillingTimeUnitsId">			
								<form:select path="billingTimeUnit" id="activityFormPanel_billingTimeUnit" tabindex="10" cssClass="${cssBillingClass}" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormPanelBillingTimeUnitsId')">
									<form:option value="-1"><spring:message code="activity.choose.billingTimeUnit"></spring:message></form:option>									
										<c:forEach var="billingTimeUnit" items="${NOM_TIME_UNIT}">																
											<c:choose>
												<c:when test="${billingTimeUnit.value == activityBean.billingTimeUnit}">
													<option title='<spring:message code="${billingTimeUnit.label}"/>'  value="${billingTimeUnit.value}" SELECTED><spring:message code="${billingTimeUnit.label}"/></option>
												</c:when>
												<c:otherwise>
													<option title='<spring:message code="${billingTimeUnit.label}"/>' value="${billingTimeUnit.value}"><spring:message code="${billingTimeUnit.label}"/></option>
												</c:otherwise>
											</c:choose>									
										</c:forEach>							
								</form:select>	
							</div>		
						</c:when>
						<c:otherwise>
							<div title='<spring:message code="activity.choose.billingTimeUnit"/>' id="activityFormBillingTimeUnitsId">			
								<form:select path="billingTimeUnit" id="activityForm_billingTimeUnit" tabindex="10" cssClass="${cssBillingClass}" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormBillingTimeUnitsId')">
									<form:option value="-1"><spring:message code="activity.choose.billingTimeUnit"></spring:message></form:option>									
										<c:forEach var="billingTimeUnit" items="${NOM_TIME_UNIT}">																
											<c:choose>
												<c:when test="${billingTimeUnit.value == activityBean.billingTimeUnit}">
													<option title='<spring:message code="${billingTimeUnit.label}"/>'  value="${billingTimeUnit.value}" SELECTED><spring:message code="${billingTimeUnit.label}"/></option>
												</c:when>
												<c:otherwise>
													<option title='<spring:message code="${billingTimeUnit.label}"/>' value="${billingTimeUnit.value}"><spring:message code="${billingTimeUnit.label}"/></option>
												</c:otherwise>
											</c:choose>									
										</c:forEach>							
								</form:select>	
							</div>				
						</c:otherwise>
					</c:choose>																			
				</td>				
			</tr>					
		</security:authorize>
		
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">		
			
			<tr>
				<td colspan="2" class="noPaddingTd">	
					
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">
							<div id="BILLING_PANEL_INFORMATIONS" style="display:none">		
						</c:when>
						<c:otherwise>
							<div id="BILLING_INFORMATIONS" style="display:none">	
						</c:otherwise>											
					</c:choose>
						<table >
							<tr>
								<td class="labelTd" style="width: 180px">
									<spring:message code="activity.billable" />
								</td>	
								<td> 
								<c:choose>
									<c:when test="${GET_FROM_PANEL eq true}">
										<font class="labelTd"><spring:message code="billable.yes" /></font>
											<form:radiobutton path="billable" value="Y" tabindex="4" id="activityFormPanel_billable"/>
										<font class="labelTd"><spring:message code="billable.no" /></font> 											 
											<form:radiobutton path="billable" value="N" tabindex="3" id="activityFormPanel_billable"/> 	
									</c:when>
									<c:otherwise>
										<font class="labelTd"><spring:message code="billable.yes" /></font>	
											<form:radiobutton path="billable" value="Y" tabindex="4" id="activityForm_billable"/>									
										<font class="labelTd"><spring:message code="billable.no" /></font> 										
											<form:radiobutton path="billable" value="N" tabindex="3" id="activityForm_billable"/>  		
									</c:otherwise>
								</c:choose>		
																	
								</td>	
							</tr>
						
							<!-- ====================================BILLING PRICE=================================================== -->
							<tr>
								<td class="labelTd">
									<spring:message code="activity.billingPrice"/> 
								</td>
								<td>
									<c:choose>
										<c:when test="${GET_FROM_PANEL eq true}">
											<form:input path="billingPrice" id="activityFormPanel_billingPrice" tabindex="8" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('activityFormPanel_billingPrice')"/>
										</c:when>
										<c:otherwise>
											<form:input path="billingPrice" id="activityForm_billingPrice" tabindex="8" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('activityForm_billingPrice')"/>		
										</c:otherwise>
									</c:choose>																		
								</td>	
							</tr>

							<!-- ====================================BILLING PRICE CURRENCY=================================================== -->
							<tr>
								<td class="labelTd">
									<spring:message code="activity.billingPriceCurrency"/> <c:choose><c:when test="${activityBean.billingPrice eq null}"><span id="billingPriceCurrencyRequiredId${i}" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="billingPriceCurrencyRequiredId${i}" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
								</td>		
								<td>
									<c:choose>
										<c:when test="${GET_FROM_PANEL eq true}">
											<form:select path="billingPriceCurrencyId" id="activityFormPanel_billingPriceCurrency" cssClass="${cssBillingClass}" tabindex="9">
							 					<form:option value="-1" title='<spring:message code="activity.choose.billing.price.currency"/>'><spring:message code="activity.choose.billing.price.currency"/></form:option>
								 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
							 						<c:choose>
							 							<c:when test="${currency.value == activityBean.billingPriceCurrencyId}">
							 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
							 							</c:when>
							 							<c:otherwise>
							 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
							 							</c:otherwise>
							 						</c:choose>
							 					</c:forEach>
							 				</form:select>
										</c:when>
										<c:otherwise>
											<form:select path="billingPriceCurrencyId" id="activityForm_billingPriceCurrency" cssClass="${cssBillingClass}" tabindex="9">
							 					<form:option value="-1" title='<spring:message code="activity.choose.billing.price.currency"/>'><spring:message code="activity.choose.billing.price.currency"/></form:option>
								 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
							 						<c:choose>
							 							<c:when test="${currency.value == activityBean.billingPriceCurrencyId}">
							 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
							 							</c:when>
							 							<c:otherwise>
							 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
							 							</c:otherwise>
							 						</c:choose>
							 					</c:forEach>
							 				</form:select>
										</c:otherwise>
									</c:choose>											
								</td>	
							</tr>
	
							<!-- ====================================BILLING TIME UNIT=================================================== -->
							<tr>
								<td class="labelTd">
									<spring:message code="activity.billingTimeUnit"/> <c:choose><c:when test="${activityBean.billingPrice eq null}"><span id="billingTimeUnitRequiredId${i}" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="billingTimeUnitRequiredId${i}" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
								</td>	
								<td>								
									<c:choose>
										<c:when test="${GET_FROM_PANEL eq true}">
											<div title='<spring:message code="activity.choose.billingTimeUnit"/>' id="activityFormPanelBillingTimeUnitsId">			
												<form:select path="billingTimeUnit" id="activityFormPanel_billingTimeUnit" tabindex="10" cssClass="${cssBillingClass}" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormPanelBillingTimeUnitsId')">
													<form:option value="-1"><spring:message code="activity.choose.billingTimeUnit"></spring:message></form:option>									
														<c:forEach var="billingTimeUnit" items="${NOM_TIME_UNIT}">																
															<c:choose>
																<c:when test="${billingTimeUnit.value == activityBean.billingTimeUnit}">
																	<option title='<spring:message code="${billingTimeUnit.label}"/>'  value="${billingTimeUnit.value}" SELECTED><spring:message code="${billingTimeUnit.label}"/></option>
																</c:when>
																<c:otherwise>
																	<option title='<spring:message code="${billingTimeUnit.label}"/>' value="${billingTimeUnit.value}"><spring:message code="${billingTimeUnit.label}"/></option>
																</c:otherwise>
															</c:choose>									
														</c:forEach>							
												</form:select>	
											</div>		
										</c:when>
										<c:otherwise>
											<div title='<spring:message code="activity.choose.billingTimeUnit"/>' id="activityFormBillingTimeUnitsId">			
												<form:select path="billingTimeUnit" id="activityForm_billingTimeUnit" tabindex="10" cssClass="${cssBillingClass}" onchange="changeTitle(this.options[selectedIndex].text, 'activityFormBillingTimeUnitsId')">
													<form:option value="-1"><spring:message code="activity.choose.billingTimeUnit"></spring:message></form:option>									
														<c:forEach var="billingTimeUnit" items="${NOM_TIME_UNIT}">																
															<c:choose>
																<c:when test="${billingTimeUnit.value == activityBean.billingTimeUnit}">
																	<option title='<spring:message code="${billingTimeUnit.label}"/>'  value="${billingTimeUnit.value}" SELECTED><spring:message code="${billingTimeUnit.label}"/></option>
																</c:when>
																<c:otherwise>
																	<option title='<spring:message code="${billingTimeUnit.label}"/>' value="${billingTimeUnit.value}"><spring:message code="${billingTimeUnit.label}"/></option>
																</c:otherwise>
															</c:choose>									
														</c:forEach>							
												</form:select>	
											</div>				
										</c:otherwise>
									</c:choose>											
								</td>
							</tr>		
						</table>						
					</div>
					
				</td>						
			</tr> 
					
		</security:authorize>
	
		<!-- ====================================DESCRIPTION====================================================== -->
		<tr>
			<td class="labelTd">
				<spring:message code="activity.description"/>
			</td>
			<td>		
			
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<form:textarea path="description" id="activityFormPanel_description" tabindex="11" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
						<div class="remainingCharactersActivityDescriptionPanel" style="display:none" >
							<div id="showRemainingCharactersDescriptionPanel" />						
						</div> 
					</c:when>
					<c:otherwise>
						<form:textarea path="description" id="activityForm_description" tabindex="11" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
						<div class="remainingCharactersActivityDescription" style="display:none" >
							<div id="showRemainingCharactersDescription" />						
						</div> 		
					</c:otherwise>
				</c:choose>								
			</td>
		</tr>		
			
		<!-- ====================================FORM ACTIONS : SAVE & CANCEL====================================================== -->
							 									
		<tr>
			<td colspan="5" class="formActions" align="right">		
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">											
						<input type="button" class="button" id="saveActivityPanel" tabindex="12" value="<spring:message code="save"/>"/>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
							<input type="button" class="button"  id="currencyPanelFromActivityPanel" tabindex="13" value="<spring:message code="activity.add.currecy"/>"/>
						</security:authorize>								
					</c:when>
					<c:otherwise>
						<input type="button" class="button" id="save" tabindex="12" value="<spring:message code="save"/>" />
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
							<input type="button" class="button"  id="currencyPanel" tabindex="13" value="<spring:message code="activity.add.currecy"/>"/>
						</security:authorize>				
						<input type="button" class="button" id="cancel" onclick="ENTITY = getActivityData();check_add('${BACK_URL}&projectId=${projectId}&hasProjectDetail=true', 'MAIN_CONTENT');" tabindex="14" value="<spring:message code="cancel"/>" />				
					</c:otherwise>
				</c:choose>																					
			</td>
		</tr>		
	</table>
	<br/>
</form:form>
</c:if>

<c:if test="${GET_FROM_PANEL eq null}">
<div style="display: ${display}">
</div>

<br/>

<div id="SECONDARY_CONTENT"></div>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	


	// focus on name input
	document.activityForm${i}.name.focus();	
		
	//------------ SAVE ACTIVTY ---------------------------------------------------------------		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">				
		var submitObject = new ObjSubmit("Activity.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "activityForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitSaveActivity, submitObject, true);		
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">			
		<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">
			var submitObject = new ObjSubmit("Activity.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "activityForm", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("save", "click", submitSaveActivity, submitObject, true);
		</c:if>		
	</security:authorize>

	//---------------------------------------- DELETE ACTIVITY ---------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">	
		<c:if test="${activityBean.activityId > 0}">		
			//submitObject = new ObjSubmitWithConfirmation("ActivitySearch.htm?ACTION=DELETE_FROM_ACTIVITY_FORM&activityId=${activityBean.activityId}", "", "MAIN_CONTENT", '<spring:message code="activity.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			submitObject = new ObjSubmitWithConfirmation("ProjectDetails.htm?ACTION=DELETE_FROM_ACTIVITY_FORM&activityId=${activityBean.activityId}&projectId=${projectId}", "", "MAIN_CONTENT", '<spring:message code="activity.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);				
		</c:if>	
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityDelete}">	
		<c:if test="${activityBean.activityId > 0 and IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">		
			//submitObject = new ObjSubmitWithConfirmation("ActivitySearch.htm?ACTION=DELETE_FROM_ACTIVITY_FORM&activityId=${activityBean.activityId}", "", "MAIN_CONTENT", '<spring:message code="activity.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			submitObject = new ObjSubmitWithConfirmation("ProjectDetails.htm?ACTION=DELETE_FROM_ACTIVITY_FORM&activityId=${activityBean.activityId}&projectId=${projectId}", "", "MAIN_CONTENT", '<spring:message code="activity.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);				
		</c:if>	
	</security:authorize>
				
	//----------------------------------------ADD A NEW ACTIVITY-------------------------------		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		var submitObject = new ObjSubmit("Activity.htm?ACTION=ADD&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewActivity", "click", getContentFromUrl, submitObject, true);
	</security:authorize>		

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">	
			var submitObject = new ObjSubmit("Activity.htm?ACTION=ADD&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("addNewActivity", "click", getContentFromUrl, submitObject, true);
		</c:if>
	</security:authorize>	
		
	//------------ ACTIVITY SEARCH ---------------------------------------------------------------
	var getObject = new ObjSubmit("ActivitySearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);				
	
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
	var validateDescriptionField = new ObjFieldRemaining("activityForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersDescription');
	YAHOO.util.Event.addListener("activityForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("activityForm_description", "blur", hideInfoOnBlurInField,validateDescriptionField, true);
	YAHOO.util.Event.addListener("activityForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
	
	var validateCostPriceField = new ObjFieldPrice("activityForm_costPrice", 'costPriceCurrencyRequiredId', 'costTimeUnitRequiredId', 'activityForm_costPriceCurrency', 'activityForm_costTimeUnit');	
	YAHOO.util.Event.addListener("activityForm_costPrice", "blur", hideOnBlurInField,validateCostPriceField, true);	

	var validateBillingPriceField = new ObjFieldPrice("activityForm_billingPrice", 'billingPriceCurrencyRequiredId', 'billingTimeUnitRequiredId', 'activityForm_billingPriceCurrency', 'activityForm_billingTimeUnit');	
	YAHOO.util.Event.addListener("activityForm_billingPrice", "blur", hideOnBlurInField, validateBillingPriceField, true);	
	
	//-------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
	
	//----------------------------------------- ACTIVITY ADD CURRENCY ---------------------------------------	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
		var submitCurrencyPanel = ObjSubmitCurrencyPanel('CurrencyForm.htm?GET_FROM_PANEL=true', "<spring:message code="activity.add.currecy"/>");		
		YAHOO.util.Event.addListener("currencyPanel", "click", displayAddCurrencyPanel, submitCurrencyPanel, true);
	</security:authorize>
		
	//----------------------------------------- ACTIVITY ENTER EVENT ---------------------------------------	
	
	OLD_ENTITY = getActivityData();

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
		<c:if test="${activityBean.activityId != -1 && activityBean.projectId != -1}">	
			document.getElementById("BILLING_INFORMATIONS").style.display='block';						
		</c:if>
	</security:authorize>

	selectOption("activityForm_project", "activityForm_project1");
	
</script>
</c:if>

<c:if test="${GET_FROM_PANEL eq null or ONSUBMIT eq null}">
		<c:if test="${GET_FROM_PANEL eq true}">
			<script>	
			
				selectOption("activityFormPanel_project", "activityFormPanel_project1");
								
				// focus on name input
				document.activityForm${i}.name.focus();	

				//------------ SAVE ACTIVTY ---------------------------------------------------------------		
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">				
					var submitObject = new ObjSubmit("Activity.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "activityFormPanel", "MESSAGES");
					YAHOO.util.Event.addListener("saveActivityPanel", "click", submitAndValidateActivityPanelForm, submitObject, true);		
				</security:authorize>
						
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">			
					<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">
						var submitObject = new ObjSubmit("Activity.htm?ACTION=SAVE&GET_FROM_PANEL=${GET_FROM_PANEL}", "activityFormPanel", "MESSAGES");
						YAHOO.util.Event.addListener("saveActivityPanel", "click", submitAndValidateActivityPanelForm, submitObject, true);		
					</c:if>		
				</security:authorize>

				//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
				var validateDescriptionField = new ObjFieldRemaining("activityFormPanel_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersDescriptionPanel');
				YAHOO.util.Event.addListener("activityFormPanel_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
				YAHOO.util.Event.addListener("activityFormPanel_description", "blur", hideInfoOnBlurInField,validateDescriptionField, true);
				YAHOO.util.Event.addListener("activityFormPanel_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);

				//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
				
				var validateCostPriceFieldPanel = new ObjFieldPrice("activityFormPanel_costPrice", 'costPriceCurrencyRequiredIdPanel', 'costTimeUnitRequiredIdPanel', 'activityFormPanel_costPriceCurrency', 'activityFormPanel_costTimeUnit');	
				YAHOO.util.Event.addListener("activityFormPanel_costPrice", "blur", hideOnBlurInField,validateCostPriceFieldPanel, true);	

				var validateBillingPriceFieldPanel = new ObjFieldPrice("activityFormPanel_billingPrice", 'billingPriceCurrencyRequiredIdPanel', 'billingTimeUnitRequiredIdPanel', 'activityFormPanel_billingPriceCurrency', 'activityFormPanel_billingTimeUnit');	
				YAHOO.util.Event.addListener("activityFormPanel_billingPrice", "blur", hideOnBlurInField, validateBillingPriceFieldPanel, true);	
				
				//-------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
				
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">
					<c:if test="${activityBean.activityId != -1 && activityBean.projectId != -1}">	
						document.getElementById("BILLING_INFORMATIONS").style.display='block';		
					</c:if>
				</security:authorize>

				//----------------------------------------- ACTIVITY ADD CURRENCY ---------------------------------------	
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
					var submitCurrencyPanel = ObjSubmitCurrencyPanel('CurrencyForm.htm?GET_FROM_PANEL=true&FROM_ACTIVITY_PANEL=true', "<spring:message code="activity.add.currecy"/>");		
					YAHOO.util.Event.addListener("currencyPanelFromActivityPanel", "click", displayAddCurrencyPanel, submitCurrencyPanel, true);
				</security:authorize>
				
			</script>
		</c:if>
	</c:if>
