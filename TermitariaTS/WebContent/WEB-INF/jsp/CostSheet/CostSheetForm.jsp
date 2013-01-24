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
	<c:when test="${costSheetBean.costSheetId > 0}">
		<c:set var="cssClass" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>		
	</c:otherwise>
</c:choose>
<c:set var="activityNameTabIndex" value="2"/>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>		-->	
				<c:choose>
					<c:when test="${BACK_URL eq 'Home.htm'}">
						<a href="#" id="back" onclick="ENTITY = getCostSheetData();check_add('${BACK_URL}', 'MAIN_CONTENT');"><spring:message code="back"/></a>
					</c:when>
					<c:otherwise>
						<a href="#" id="back" onclick="ENTITY = getCostSheetData();check_add('${BACK_URL}&projectId=${PROJECT_ID}&hasProjectDetail=true', 'MAIN_CONTENT');"><spring:message code="back"/></a>
					</c:otherwise>	
				</c:choose>	
				
				<!--  <a href="#" id="addNewCostSheet" title="<spring:message code="costsheet.submenu.add.title"/>"><spring:message code='add'/> </a>  -->
				<a href="#" id="delete" class="${cssClass}" title="<spring:message code="costsheet.submenu.delete.title"/>"><spring:message code='delete'/> </a>
				<!--  <a href="#" id="submenuSearch" title="<spring:message code="search"/>"><spring:message code="search"/></a>-->
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${costSheetBean.costSheetId > 0}">
		<span class="section_title"><img src="images/titles/CostSheetEdit.jpg"><spring:message code="costsheet.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/CostSheetAdd.jpg"><spring:message code="costsheet.add.section.title"/></span>	
	</c:otherwise>
</c:choose>


<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include file="../Messages.jsp" %>
	<br/>
</div>
	<form:form commandName="costSheetBean" name="costSheetForm" id="costSheetForm" onsubmit="return false;">		
		<form:hidden path="costSheetId" id="costSheetId"/>
		<form:hidden path="userId" id="userId"/>
		<table cellpadding="3" class="tableAdd" width="770px">
			<tr><td class="tableAddSpacer">&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table>
						<!-- ====================================COST SHEET ID====================================================== -->
						<c:if test="${costSheetBean.costSheetId > 0}">
							<tr>
								<td class="labelTd" style="width:130px">
									<spring:message code="costsheet.id"/>
								</td>
								<td>
									${costSheetBean.costSheetId}
								</td>
							</tr>
						</c:if>
						<tr>
							<!-- ====================================PROJECT====================================================== -->
							<td class="labelTd" style="width:135px">
								<spring:message code="costsheet.project"/>
							</td>
							<td style="width:200px">
								
								<div title='<spring:message code="costsheet.choose.project"/>' id="costSheetSelectProjectsId">
									<c:choose>
										<c:when test="${costSheetBean.projectId == PROJECT_ID}">
											<form:hidden path="projectId" id="costSheetForm_project" />
											<c:forEach var="project" items="${USER_PROJECTS}">
												<c:if test="${project.projectId == PROJECT_ID}">
													${project.name}	
												</c:if>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<form:select path="projectId" tabindex="1" id="costSheetForm_project" cssClass="formField required" onchange="manageProjectSelectInCostSheetForm(this.options[selectedIndex].value, 'costSheetForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', '${USER_NAME}', false, ${costSheetBean.costSheetId}); changeTitle(this.options[selectedIndex].title, 'costSheetSelectProjectsId'); this.style.color = getStyleTextColor(this.selectedIndex, 'costSheetForm_project', 'costSheetForm_project1');">
												<option value="-2" class="chooseProject" title='<spring:message code="costsheet.choose.project" />'><spring:message code="costsheet.choose.project" /></option>
												<option value="-1" class="fromOrganization" title='<spring:message code="costsheet.for.organization" />'><spring:message code="costsheet.for.organization" /></option>
												<c:forEach var="project" items="${USER_PROJECTS}">												
													<c:choose>
														<c:when test="${project.clientId != -1}">
															<c:set var="client" value="hasClient"/>														
															<c:choose>
																<c:when test="${project.status == 1}">
																	<c:set var="prj" value=""/>	
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
														<c:when test="${costSheetBean.projectId == project.projectId}">
															<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" SELECTED>${project.name}</option>
														</c:when>
														<c:otherwise>
															<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}">${project.name}</option>
														</c:otherwise>
													</c:choose>		
													
												</c:forEach>
											</form:select>
										</c:otherwise>
									</c:choose>	
									
									
									<div id="project_message"></div>
								</div>
								
								<div id="costSheetForm_project1">
									<span class="chooseProject"></span>		
									<span class="fromOrganization"></span>																		
									<c:forEach var="project" items="${USER_PROJECTS}">	
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
								
							</td>													
						</tr>
						
						<tr>
							<!-- ================================DATE====================================================== -->
							<td class="labelTd">
								<spring:message code="costsheet.date"/><span id="required">*</span>
							</td>
							<td>
								<fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${costSheetBean.date}" var="date"/>
								<input name="date" id="costSheetForm_date" value="${date}" readonly="readonly" class="formField" tabindex="2" onclick="document.getElementById('calendar').style.zIndex = 1;instantiateCalendar('calendar','costSheetForm_date', '${TODAY_DATE}', 'time', 'costSheetForm', true);"/>
								<div id="date_message"></div> 
							</td>
							<td>
								<div id="calendar"></div>
								<div id="time"></div>
							</td>
						</tr>
						
						<tr>
							<!-- ================================COST PRICE============================================= -->
							<td class="labelTd">
								<spring:message code="costsheet.cost.price"/><span id="required">*</span>
							</td>
							<td>
								<form:input path="costPrice" id="costSheetForm_costPrice" tabindex="3" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('costSheetForm_costPrice')"/>
								<div id="costPrice_message"></div> 
							</td>							
						</tr>
						
						<tr>
							<!-- ================================COST PRICE CURRENCY==================================== -->
				 			<td class="labelTd">
				 				<spring:message code="costsheet.cost.price.currency"/><span id="required">*</span>
				 			</td>
				 			<td>
				 				<form:select path="costPriceCurrencyId" id="costSheetForm_costPriceCurrency" cssClass="formField" tabindex="4">
				 					<form:option value="-1" title='<spring:message code="costsheet.choose.cost.price.currency"/>'><spring:message code="costsheet.choose.cost.price.currency"/></form:option>
				 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
				 						<c:choose>
				 							<c:when test="${currency.value == costSheetBean.costPriceCurrencyId}">
				 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
				 							</c:when>
				 							<c:otherwise>
				 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
				 							</c:otherwise>
				 						</c:choose>
				 					</c:forEach>
				 				</form:select>
				 				<div id="costPriceCurrency_message"></div> 
				 			</td>				 			
						</tr>
																							
						<tr class="costSheetUser" style="display:none">
							<!-- ====================================USER====================================================== -->
							<td class="labelTd">
								<spring:message code="costsheet.user"/><span id="required">*</span>
							</td>
							<td>
								<div id="userAutoComplete" class="autoComplete">
									<form:input id="costSheetForm_user" cssClass="formField required" tabindex="5" path="costSheetReporterName"/>
									<div id="userAutoCompleteContainer"></div>
								</div>
								<div id="user_message"></div>
							</td>							
						</tr>
						
						<tr>
							<!-- ================================ACTIVITY NAME============================================= -->
							<td class="labelTd">
								<spring:message code="costsheet.activity"/><span id="required">*</span>
							</td>
							<td width="250px">
								<form:textarea path="activityName" id="costSheetForm_activityName" tabindex="6" cssClass="formField required" onkeypress="return restrictSpecialCharactersForObs(event);"/>
								<div id="activityName_message"></div>
								<div class="remainingCharactersCostSheetActivityName" style="display:none" >
									<div id="showRemainingCharactersActivityName"/>
								</div>
							</td>
						</tr>
												
						<!-- ====================================BILLABLE====================================================== -->
						<c:set var="nextTabIndex" value="6"/>
						<c:set var="billableDisplay" value="none"/>
						<c:set var="nextTabIndex" value="${nextTabIndex + 4}"/>
						<c:set var="billableDisplay" value="table-cell"/>
						<!--<c:choose>
							<c:when test="costSheetBean.costSheetId > 0">
								<c:if test="${SHOW_BILLABLE eq true}">
									<c:set var="nextTabIndex" value="${nextTabIndex + 4}"/>
									<c:set var="billableDisplay" value="table-cell"/>
								</c:if>
							</c:when>
						</c:choose>-->
						<tr>
							<td class="labelTd billable" style="display:${billableDisplay}">
								<spring:message code="costsheet.billable"/>
							</td>							
							<td class="billable" style="display:${billableDisplay}">
								<font class="labelTd"><spring:message code="costsheet.billable.yes" /></font>
									<form:radiobutton path="billable" value="Y" tabindex="7" id="costSheetForm_billable_yes"/> 
								<font class="labelTd"><spring:message code="costsheet.billable.no" /></font>
									<form:radiobutton path="billable" value="N" tabindex="8" id="costSheetForm_billable_no"/> 
							</td>
							<td class="billable" style="display:${billableDisplay}">
							</td>
						</tr>
						
						<tr>
							<!-- ====================================BILLING PRICE====================================================== -->
							<td class="labelTd billable" style="display:${billableDisplay}">
								<spring:message code="costsheet.billing.price"/> 
							</td>
							<td class="billable" style="display:${billableDisplay}">
								<form:input path="billingPrice" id="costSheetForm_billingPrice" tabindex="9" cssClass="formField" onkeypress="return restrictCharactersForReal(event);OLD_VALUE = this.value" onkeyup="restrictToFloatNumber('costSheetForm_billingPrice')"/>
							</td>
							<td class="billable" style="display:${billableDisplay}">
							</td>
						</tr>
						
						<tr>
							<!-- ================================BILLING PRICE CURRENCY================================================= -->
				 			<td class="labelTd billable" style="display:${billableDisplay}">
				 				<spring:message code="costsheet.billing.price.currency"/><c:choose><c:when test="${costSheetBean.billingPrice eq null}"><span id="billingPriceCurrencyRequiredId" class="hiddenRequiredAsterix">*</span></c:when><c:otherwise><span id="billingPriceCurrencyRequiredId" class="visibleRequiredAsterix">*</span></c:otherwise></c:choose>
				 			</td>
				 			<td class="billable" style="display:${billableDisplay}">
				 				<form:select path="billingPriceCurrencyId" id="costSheetForm_billingPriceCurrency" cssClass="formField" tabindex="10">
				 					<form:option value="-1" title='<spring:message code="costsheet.choose.billing.price.currency"/>'><spring:message code="costsheet.choose.billing.price.currency"/></form:option>
				 					<c:forEach var="currency" items="${ORG_CURRENCIES}">
				 						<c:choose>
				 							<c:when test="${currency.value == costSheetBean.billingPriceCurrencyId}">
				 								<option value="${currency.value}" title="${currency.label}" selected="selected">${currency.label}</option>
				 							</c:when>
				 							<c:otherwise>
				 								<option value="${currency.value}" title="${currency.label}">${currency.label}</option>
				 							</c:otherwise>
				 						</c:choose>
				 					</c:forEach>
				 				</form:select>
				 				<div id="billingPriceCurrency_message"></div>
				 			</td>
				 			<td class="billable" style="display:${billableDisplay}">
				 			</td>
						</tr>																		
					</table>
				</td>
				
				<td>
					<table>									
						<tr>
							<!-- ====================================DESCRIPTION=================================== -->
							<td class="labelTd">
								<spring:message code="costsheet.description"/>
							</td>
							<td>
								<form:textarea path="description" id="costSheetForm_description" cssClass="formField" tabindex="${nextTabIndex + 2}" cols="40" rows="3" onkeypress="return restrictSpecialCharactersForObs(event);"/>
								<div class="remainingCharactersCostSheetDesc" style="display:none" >
									<div id="showRemainingCharactersDescription"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<!-- ====================================OBSERVATION=================================== -->
							<td class="labelTd">
								<spring:message code="costsheet.observation"/>
							</td>
							<td>
								<form:textarea path="observation" id="costSheetForm_observation" cssClass="formField" tabindex="${nextTabIndex + 3}" cols="40" rows="3" onkeypress="return restrictSpecialCharactersForObs(event);"/>
								<div class="remainingCharactersCostSheetObs" style="display:none" >
									<div id="showRemainingCharactersObservation"/>
								</div>
							</td>
						</tr>
						
					</table>
				</td>
			</tr>

			<tr>
				<td colspan="4" class="formActions" align="right">
						<input type="button" class="button" id="save" tabindex="${nextTabIndex + 4}" value="<spring:message code="save"/>"/>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
							<input type="button" class="button"  id="currencyPanel" tabindex="${nextTabIndex + 5}" value="<spring:message code="costsheet.add.currecy"/>"/>
						</security:authorize>	
						
						<c:choose>
							<c:when test="${BACK_URL eq 'Home.htm'}">
								<input type="button" class="button" id="cancel" tabindex="${nextTabIndex + 6}" value="<spring:message code="cancel"/>" onclick="ENTITY = getCostSheetData();check_add('${BACK_URL}', 'MAIN_CONTENT');"/>						
							</c:when>
							<c:otherwise>
								<input type="button" class="button" id="cancel" tabindex="${nextTabIndex + 6}" value="<spring:message code="cancel"/>" onclick="ENTITY = getCostSheetData();check_add('${BACK_URL}&projectId=${PROJECT_ID}&hasProjectDetail=true', 'MAIN_CONTENT');"/>						
							</c:otherwise>	
						</c:choose>							
						</td>
			</tr>
			
		</table>
	</form:form>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

	
<script type="text/javascript">	

	//------------ HOME ---------------------------------------------------------------		
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//----------------------------------------------- DELETE COST SHEET ------------------------------------------------
	<c:if test="${costSheetBean.costSheetId > 0}">
		//submitObject = new ObjSubmitWithConfirmation("CostSheetSearch.htm?ACTION=DELETE_FROM_COST_SHEET_FORM&costSheetId=${costSheetBean.costSheetId}", "", "MAIN_CONTENT", '<spring:message code="costsheet.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
		<c:choose>
			<c:when test="${BACK_URL eq 'Home.htm'}">
				submitObject = new ObjSubmitWithConfirmation("Home.htm?ACTION=DELETE_FROM_COST_SHEET_FORM&costSheetId=${costSheetBean.costSheetId}", "", "MAIN_CONTENT", '<spring:message code="costsheet.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			</c:when>
			<c:otherwise>
				submitObject = new ObjSubmitWithConfirmation("ProjectDetails.htm?ACTION=DELETE_FROM_COST_SHEET_FORM&costSheetId=${costSheetBean.costSheetId}&projectId=${PROJECT_ID}", "", "MAIN_CONTENT", '<spring:message code="costsheet.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			</c:otherwise>
		</c:choose>
		YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);
	</c:if>	

	//----------------------------------------------- ADD NEW COST SHEET -----------------------------------------------
	var	getObject = new ObjSubmit("CostSheetForm.htm?ACTION=ADD&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("addNewCostSheet", "click", getContentFromUrl, getObject, true);

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS -----------------------
	
	//------------------------------------- OBSERVATION ------------------------------------------
	var validateObservationField = new ObjFieldRemaining("costSheetForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("costSheetForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("costSheetForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("costSheetForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);	

	//------------------------------------- DESCRIPTION ------------------------------------------
	var validateDescriptionField = new ObjFieldRemaining("costSheetForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersDescription');
	YAHOO.util.Event.addListener("costSheetForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("costSheetForm_description", "blur", hideInfoOnBlurInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("costSheetForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);		

	//------------------------------------- ACTIVITY NAME ----------------------------------------
	var validateActivityNameField = new ObjFieldRemaining("costSheetForm_activityName", '<spring:message code="observation.message"/>', 500, 'showRemainingCharactersActivityName');
	YAHOO.util.Event.addListener("costSheetForm_activityName", "focus", showInfoOnClickInField, validateActivityNameField, true);
	YAHOO.util.Event.addListener("costSheetForm_activityName", "blur", hideInfoOnBlurInField, validateActivityNameField, true);
	YAHOO.util.Event.addListener("costSheetForm_activityName", "keyup", showInfoOnKeyUpInField, validateActivityNameField, true);	
	
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS -----------------------
	
	//----------------------------------- ENTER EVENT ON ALL FIELDS---------------------------------------
	var keyListner = null; 
	var submitObject = new ObjSubmit("CostSheetForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "costSheetForm", "MAIN_CONTENT");
	
	keyListener = new YAHOO.util.KeyListener("costSheetForm_user", { keys:13 },  { fn:submitSaveCostSheet, scope: submitObject, correctScope:true}, null ); 
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("costSheetForm_costPrice", { keys:13 },  { fn:submitSaveCostSheet, scope: submitObject, correctScope:true}, null ); 
	keyListener.enable();

	keyListener = new YAHOO.util.KeyListener("costSheetForm_billingPrice", { keys:13 },  { fn:submitSaveCostSheet, scope: submitObject, correctScope:true}, null ); 
	keyListener.enable();
	
	//----------------------------------- SELECT THE PROJECT AND INSTANTIATE THE USER AUTO COMPLETE WHEN EDITING ------------------------------
	<c:choose>
		<c:when test="${costSheetBean.costSheetId > 0}">
			<c:choose>
				<c:when test="${costSheetBean.projectId != null}">
					manageProjectSelectInCostSheetForm('${costSheetBean.projectId}', 'costSheetForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', '${USER_NAME}', true, ${costSheetBean.costSheetId});
				</c:when>
				<c:otherwise>
					manageProjectSelectInCostSheetForm(-1, 'costSheetForm_user', 'userAutoCompleteContainer', 'userId', '${USER_PROJECTS_IS_PM_AND_MEMBERS}', '${PERSON_ID}', '${IS_USER_ALL}', '${USER_NAME}', true, ${costSheetBean.costSheetId});
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>	

	//-------------------- SEARCH COST SHEET --------------------------------------------------------------
	var getObject = new ObjSubmit("CostSheetSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);		

	//-------------------- SHOW/HIDE REQUIRED STAR FOR BILLING CURRENCY SELECT ----------------------------
	var validateBillingCurrencyField = new ObjFieldPrice("costSheetForm_billingPrice", 'billingPriceCurrencyRequiredId', null, 'costSheetForm_billingPriceCurrency', null);	
	YAHOO.util.Event.addListener("costSheetForm_billingPrice", "blur", hideOnBlurInField, validateBillingCurrencyField, true);	

	//-------------------- OLD COST SHEET ENTITY ----------------------------------------------------------
	OLD_ENTITY = getCostSheetData();

	//selectOption("costSheetForm_project", "costSheetForm_project1");
			
	<c:choose>
		<c:when test="${IS_USER_ALL eq true || IS_MANAGER eq true}">
			<c:set var="IS_USER_ALL_OR_PM" value="true"/>
		</c:when>
		<c:otherwise>
			<c:set var="IS_USER_ALL_OR_PM" value="false"/>
		</c:otherwise>
	</c:choose>	

	
	
	//---------------------------------------------- VALIDATION AND SUBMIT -----------------------------------------
	document.costSheetForm.costSheetForm_project.focus();
	var submitObject = new ObjSubmitCostSheet("CostSheetForm.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}", "costSheetForm", "MAIN_CONTENT", ${IS_USER_ALL_OR_PM});
	YAHOO.util.Event.addListener("save", "click", submitSaveCostSheet, submitObject, true);

	//----------------------------------------- COST SHEET ADD CURRENCY ---------------------------------------	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencyAdd}">		
		var submitCurrencyPanel = ObjSubmitCurrencyPanel('CurrencyForm.htm?GET_FROM_PANEL=true&FROM_COST_SHEET=true', "<spring:message code="costsheet.add.currecy"/>");		
		YAHOO.util.Event.addListener("currencyPanel", "click", displayAddCurrencyPanel, submitCurrencyPanel, true);
	</security:authorize>
	
</script>
