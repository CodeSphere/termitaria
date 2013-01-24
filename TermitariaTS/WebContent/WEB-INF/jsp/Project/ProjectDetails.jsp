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

<c:choose>
	<c:when test="${projectDetailsBean.projectDetailId > 0}">
		<c:set var="cssClass" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>		
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${projectDetailsBean.budget ne null}">
		<c:set var="cssBudgetClass" value="formField validate-not-first"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssBudgetClass" value="formField"/>		
	</c:otherwise>
</c:choose>	

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">				
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->	
				<a href="#" id="back" onclick="ENTITY = getProjectDetailData();check_add('${BACK_URL}', 'MAIN_CONTENT');">
					<spring:message code="back"/>
				</a>					
				<!--  <a href="#" id="submenuSearch" title="<spring:message code="project.details.submeniu.search.title"/>"><spring:message code="search"/></a> -->
				<!--  <a href="#" id="projectTeam" title="<spring:message code="project.team"/>"><spring:message code="project.team"/></a>		-->			
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${projectDetailsBean.projectDetailId > 0}">
		<span class="section_title"><img src="images/titles/ProjectDetailEdit.jpg"><spring:message code="project.details.title"/> ${PROJECT.name}</span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/ProjectDetailAdd.jpg"><spring:message code="project.details.add.section.title"/></span>
	</c:otherwise>
</c:choose>


<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include file="../Messages.jsp" %>
</div>

<form:form method="post" commandName="projectDetailsBean" id="projectDetailsForm" name="projectDetailsForm" onsubmit="return false;">
	
	<form:hidden path="projectDetailId" id="projectDetailId" />	
	<form:hidden path="projectId" id="projectId"/>	
	<form:hidden path="notificationStatus" id="notificationStatus"/>	
	<form:hidden path="oldBudgetCurrencyId"/>					
		
	<table>						            	                             
        <tr>                                              
           <td>                                                       
				<table cellpadding="3" class="tableAdd"  width="450px">			
					<tr><td class="tableAddSpacer">&nbsp;</td></tr>		
					
					<!-- ====================================PROJECT========================================================= -->
					<tr>
						<td class="labelTd">
							<spring:message code="project.details.project"/>
						</td>			
						<td>								
							${PROJECT.name}																										
						</td>
					</tr>
					
					<tr>					
						<td class="labelTd">			
							<spring:message code="project.client"/>								
						</td>	
						
						<td>						
							<c:choose>
								<c:when test="${PROJECT.client.type == 2}">
									${PROJECT.client.p_firstName} ${bean.client.p_lastName}											
								</c:when>
								<c:otherwise>											
									<c:choose>
										<c:when test="${PROJECT.client.type == 1}">											
											${PROJECT.client.c_name}											
										</c:when>
										<c:otherwise>																						
											<spring:message code="project.from.organization"/>	
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>								
						</td>					
					</tr>
															
					<tr>					
						<td class="labelTd">						
							<spring:message code="project.manager.name"/>								
						</td>	
						
						<td>							
							<c:choose>
								<c:when test="${PROJECT.manager.status == 2}">
									
								</c:when>
								<c:otherwise>
									${PROJECT.manager.firstName} ${PROJECT.manager.lastName}	
								</c:otherwise>
							</c:choose>											
						</td>			
					</tr>	
																																																																										 	
					<!-- ====================================BUDGET=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="project.details.budget"/>
						</td>
						<td>
							<form:input path="budget" id="projectDetailsForm_budget" tabindex="2" cssClass="formField " onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('projectDetailsForm_budget')"/>
						</td>	
					</tr>
					
					<!-- ====================================BUDGET CURRENCY=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="project.details.budget.currency"/> <c:choose><c:when test="${projectDetailsBean.budget eq null}"><span id="budgetCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="budgetCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
						</td>
						<td>
							<form:select path="budgetCurrencyId" id="projectDetailsForm_budgetCurrency" tabindex="3" cssClass="${cssBudgetClass}">
	 							<form:option value="-1" title='<spring:message code="project.details.choose.budget.currency"/>'><spring:message code="project.details.choose.budget.currency"/></form:option>
			 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
			 						<c:choose>
			 							<c:when test="${currency.value == projectDetailsBean.budgetCurrencyId}">
	 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
		 							</c:when>
		 							<c:otherwise>
		 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
		 							</c:otherwise>
			 						</c:choose>
			 					</c:forEach>
	 						</form:select>	
						</td>																		
					</tr>
					
					
					<!-- ====================================NOTIFICATION_PERCENTAGE=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="project.details.notification.percentage"/>
						</td>
						<td>
							<form:input path="notificationPercentage" id="projectDetailsForm_notificationPercentage"  tabindex="3" cssClass="formField" onkeypress="registerPercentage (this.value)" onkeyup="validatePercentage(this);" />
							<span class="hint"><spring:message code="project.details.notification.percentage.hint"/><span class="hint-pointer">&nbsp;</span></span>
						</td>	
					</tr>
					
					<!-- ====================================COMPLETENESS_PERCENTAGE=================================================== -->
					<tr>			
						<td class="labelTd">
							<spring:message code="project.details.completeness.percentage"/>
						</td>
						<td>
							<form:input path="completenessPercentage" id="projectDetailsForm_completenessPercentage" tabindex="4" cssClass="formField " onkeypress="registerPercentage (this.value)" onkeyup="validatePercentage(this);" />
							<span class="hint"><spring:message code="project.details.completeness.percentage.hint"/><span class="hint-pointer">&nbsp;</span></span>
						</td>	
					</tr>
					
					<!-- ====================================OBSERVATION====================================================== -->
					<tr>
						<td class="labelTd">
							<spring:message code="project.details.observation"/>
						</td>
						<td>
							<form:textarea path="observation" id="projectDetailsForm_observation" tabindex="5" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
							<div class="remainingCharactersProjectDetailsObs" style="display:none" >
								<div id="showRemainingCharactersObservation" />			
							</div>				
						</td>
					</tr>
																																															
					<tr>
						<td colspan="5" class="formActions" align="right">		
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">		
								<input type="button" class="button" id="save" tabindex="6" value="<spring:message code="save"/>" />	
							</security:authorize>		
							<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">		
								<c:if test="${IS_MANAGER eq true}">
									<input type="button" class="button" id="save" tabindex="6" value="<spring:message code="save"/>" />
								</c:if>
							</security:authorize>	
							<!-- 
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
								<input type="button" class="button"  id="currencyPanel" tabindex="7" value="<spring:message code="project.details.add.currecy"/>"/>
							</security:authorize>	
							 -->	
							<input type="button" class="button" id="cancel" onclick="ENTITY = getProjectDetailData();check_add('${BACK_URL}', 'MAIN_CONTENT');" tabindex="8" value="<spring:message code="cancel"/>" />				
						</td>
					</tr>												
				</table>
			</td>
		<td>	
		<div id="AUXILIAR_INFO" style="margin-left: 20px;"></div>
		</td>
		</tr>	
	</table>
</form:form>	





<div style="display: ${display}">
</div>

<br/>


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
	getContentFromUrl("ProjectDetailsExtended.htm?projectId=${PROJECT.projectId}", "AUXILIAR_INFO");
	
	
	//------------ SAVE PROJECT DETAIL---------------------------------------------------------------		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">
		var submitObject = new ObjSubmit("ProjectDetails.htm?ACTION=SAVE&BACK_URL=${BACK_URL}", "projectDetailsForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitSaveProjectDetails, submitObject, true);	
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ProjectAddUpdateProjectDetails}">
		<c:if test="${IS_MANAGER eq true}">
			var submitObject = new ObjSubmit("ProjectDetails.htm?ACTION=SAVE&BACK_URL=${BACK_URL}", "projectDetailsForm", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("save", "click", submitSaveProjectDetails, submitObject, true);
		</c:if>	
	</security:authorize>
		
	//------------ PROJECT SEARCH ---------------------------------------------------------------		
	var getObject = new ObjSubmit("ProjectList.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);	

	//------------ PROJECT TEAM ---------------------------------------------------------------		
	var getObject = new ObjSubmit("PersonSearch.htm?ACTION=LIST_TEAM_MEMBERS_FROM_PROJECT&projectId=${projectDetailsBean.projectId}&projectName=${PROJECT.name}&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("projectTeam", "click", getContentFromUrl, getObject, true);
				
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
	var validateObservationField = new ObjFieldRemaining("projectDetailsForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("projectDetailsForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("projectDetailsForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("projectDetailsForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
		
	//----------------------------------------- PROJECT ENTER EVENT ---------------------------------------
	var submitObject = new ObjSubmit("ProjectDetails.htm?ACTION=SAVE", "projectDetailsForm", "MAIN_CONTENT");
	var keyListener = new YAHOO.util.KeyListener("projectDetailsForm_project", { keys:13 },  {fn: submitSaveProjectDetails, scope: submitObject, correctScope:true}, null ); 
	keyListener.enable();	


	var validateBudgetField = new ObjFieldPrice("projectDetailsForm_budget", 'budgetCurrencyRequiredId', null, 'projectDetailsForm_budgetCurrency', null);	
	YAHOO.util.Event.addListener("projectDetailsForm_budget", "blur", hideOnBlurInField, validateBudgetField, true);	

	OLD_ENTITY = getProjectDetailData();
	
	prepareInputsForHints();

	OLD_PERCENT = "";	

	//----------------------------------------- TEAM MEMBER DETAIL ADD CURRENCY ---------------------------------------	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
		var submitCurrencyPanel = ObjSubmitCurrencyPanel('CurrencyForm.htm?GET_FROM_PANEL=true&FROM_PROJECT_DETAIL=true', "<spring:message code="project.details.add.currecy"/>");		
		YAHOO.util.Event.addListener("currencyPanel", "click", displayAddCurrencyPanel, submitCurrencyPanel, true);
	</security:authorize>
	
</script>
