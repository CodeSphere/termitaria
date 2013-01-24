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
									
<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">				
				<c:import url="./OrganisationOverviewSubMenu.jsp">
	   				<c:param name="orgId" value="${ORGANIZATION.organisationId}"/>
	   				<c:param name="type" value="${ORGANIZATION.type}"/>	
				</c:import>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:set var="disabled" value="0"/>
							
<c:if test="${organizationChanged ne null}">
	<script>
		getContentFromUrlDirect('UserAndOrganizationSpecInfo.htm','top');
	</script>
</c:if>
<span class="section_title"><img src="images/titles/Organization.jpg"/>
<spring:message code="organisation.section.overview.title"/></span>
<fieldset>
				<div style="text-align: left">
					<div id="organizationOverview" >
							<!-- <h2><spring:message code="organisation.overview.generalInfo"/>:</h2> -->
							<table>
								<!--  NAME -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.name"/>
									</td>
									<c:choose>										
										<c:when test="${ORGANIZATION.status == disabled}">											
											<td>
												<p class="organisationDisabled">${ORGANIZATION.name}</p>
												<p class="organisationDisabledMessage"><spring:message code="organisation.disabled.message"/></p>
											</td>																			
										</c:when>
										<c:otherwise>
											<td>
												${ORGANIZATION.name}
											</td>
										</c:otherwise>
									</c:choose>
								</tr>
								
								<!-- CEO  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.ceo"/>
									</td>
									<td>
										${CEO.firstName} ${CEO.lastName}
									</td>
								</tr>
								
																															
								<!-- ADDRESS  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.address"/>
									</td>
									<td>
										${ORGANIZATION.address}
									</td>
								</tr>
								
								<!-- PHONE  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.phone"/>
									</td>
									<td>
										${ORGANIZATION.phone}
									</td>
								</tr>
								
								<!-- FAX  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.fax"/>
									</td>
									<td>
										${ORGANIZATION.fax}
									</td>
								</tr>
								
								<!-- EMAIL  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.email"/>
									</td>
									<td>
										${ORGANIZATION.email}
									</td>
								</tr>
								
								<!-- OBSERVATION  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.observation"/>
									</td>
									<td>
										${ORGANIZATION.observation}
									</td>
								</tr>
								
								<!-- REGISTERY NUMBER  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.j"/>
									</td>
									<td>
										${ORGANIZATION.j}
									</td>
								</tr>			
								
								<!-- Tax Identification Number  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.cui"/>
									</td>
									<td>
										${ORGANIZATION.cui}
									</td>
								</tr>
								
								<!-- Account  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.iban"/>
									</td>
									<td>
										${ORGANIZATION.iban}
									</td>
								</tr>
								
								<!-- Social Capital  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.capital"/>
									</td>
									<td>
										${ORGANIZATION.capital}
									</td>
								</tr>
								
								<!-- LOCATION  -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.location"/>
									</td>
									<td>
										${ORGANIZATION.location}
									</td>
								</tr>
								
								<!-- TYPE -->
								<c:if test="${ORGANIZATION.type != 1}">
									<tr>
										<td class="labelInfoTd">
											<spring:message code="organisation.type"/>
										</td>
										<td>	
											<c:choose>
												<c:when test="${ORGANIZATION.type == 2}">
													<spring:message code="organisation.type.group.companies"/>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${ORGANIZATION.type == 3}">
															<spring:message code="organisation.type.hq"/>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${ORGANIZATION.type == 4}">
																	<spring:message code="organisation.type.branch"/>
																</c:when>															
															</c:choose>
														</c:otherwise>
													</c:choose>
												</c:otherwise>							
											</c:choose>							
																												
										</td>
									</tr>	
								</c:if>
								
								<c:if test="${ORGANISATION_PARENT ne null}">
									<!-- PARENT  -->
									<tr>
										<td class="labelInfoTd">
											<spring:message code="organisation.parent"/>
										</td>
										<td>
											${ORGANISATION_PARENT.name}
										</td>
									</tr>
								</c:if>
								
								<!-- MODULES -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.list.modules"/>
									</td>
									<td>														
										<c:if test="${!(empty ORGANISATION_MODULES)}">
											<c:forEach var="module" items="${ORGANISATION_MODULES}">					
												${module.name}
												<br/>
											</c:forEach>												
										</c:if>																				
									</td>
								</tr>																					
																				
								<!--  NUMBER OF EMPLOYEES -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.overview.employees"/>
									</td>
									<td>
										${COUNT_EMPLOYEES}
									</td>
								</tr>
								
								<!--  NUMBER OF DEPARTMENTS -->
								<tr>
									<td class="labelInfoTd">
										<spring:message code="organisation.overview.departments"/>
									</td>
									<td>
										${COUNT_DEPARTMENTS}
									</td>
								</tr>
							</table>
						</div>
						<div id="organizationStructure" >	
							<div id="organizationStructure"></div>
						</div>						
					</div>	
</fieldset>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>
	displayMenuOptions();
	getContentFromUrlDirect('OrganisationTreeStructure.htm', 'organizationStructure');
</script>
