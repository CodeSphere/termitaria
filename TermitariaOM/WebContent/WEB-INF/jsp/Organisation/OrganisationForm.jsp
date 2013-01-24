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
	<c:when test="${organisationBean.organisationId ge 0}">
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
				<a href="#" id="home"><spring:message code="home" /></a>						
				<a href="#" id="back"><spring:message code="back"/></a>		
				
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">	
					<a href="#" id="addNewOrganisation" title="<spring:message code="organization.submeniu.add.title"/>"><spring:message code="add" /></a>					
				</security:authorize>	
				
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ChangeLogo}">					
					<a href="#" id="logoChange" class="${cssClass}" title="<spring:message code="organization.submeniu.addLogo.title"/>"><spring:message code='organisation.logo'/> </a>				
				</security:authorize>			
										
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="organization.submeniu.delete.title"/>"><spring:message code="delete"/></a>
				</security:authorize>				
				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${organisationBean.organisationId ge 0}">
		<span class="section_title"><img src="images/titles/OrganizationEdit.jpg"/><spring:message code="organisation.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/OrganizationAdd.jpg"/><spring:message code="organisation.section.title"/></span>
	</c:otherwise>
</c:choose>


<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
</div>

	<form:form method="post" commandName="organisationBean" id="organisationForm" name="organisationForm">
		
		<spring:bind path="organisationId">
			<input type="hidden" name="${status.expression}" value="${status.value}"/>
		</spring:bind>
		<table cellpadding="3" class="tableAdd">
			<tr><td class="tableAddSpacer">&nbsp;</td></tr>
			<!-- ====================================NAME====================================================== -->
			<tr>
				<td class="labelTd" width="120"> 
					<spring:message code="organisation.name"/> <span id="required">*</span> 
				</td>
				<td width="230">
					<form:input path="name" id="organisationForm_name" tabindex="1" cssClass="formField required" maxlength="100" onkeypress="return restrictSpecialCharacters(event);" onblur="verifyUniqueness('OrganisationVerifyNameUniqueness.htm?198341='+this.value, 'organisationForm_name', 'organisationForm_name_unique', this.value, null, '${organisationBean.name}', null );"/>
						<div id="organisationForm_nameDIV2" style="display: inline"></div> 
					
				</td>
				
			</tr>	
			<!-- ====================================ADDRESS=================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.address"/><span id="required">*</span>
				</td>
				<td>
					<form:textarea path="address" id="organisationForm_address" tabindex="2" rows="3" cssClass="required formField" onkeypress="return restrictCharactersForAddress(event);"/>
					<div id="organisationForm_addressDIV" style="display: inline; color: red;"></div>
					<div class="remainingCharactersAddress" style="display:none" >
						<div id="showRemainingCharactersAddress" />
					</div>											
				</td>				
			</tr>
			<!-- ====================================PHONE===================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.phone"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="phone" id="organisationForm_phone" tabindex="3" cssClass="formField required" maxlength="30" onkeypress="return restrictCharactersForPhone(event);"/>
				</td>
			</tr>
			
			<!-- ====================================FAX======================================================= -->			
			<tr>			
				<td class="labelTd">
					<spring:message code="organisation.fax"/>
				</td>
				<td>
					<form:input path="fax" id="organisationForm_fax" tabindex="4" cssClass="formField" maxlength="30" onkeypress="return restrictCharactersForPhone(event);"/>
				</td>
			<!-- ====================================CUI======================================================= -->
				<td class="labelTd">				
					<spring:message code="organisation.cui"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="cui" id="organisationForm_cui" maxlength="70" tabindex="8" cssClass="formField required" onkeypress="return restrictCharactersForIdentificationNumbers(event);"/>
				</td>
			</tr>
			<!-- ====================================EMAIL===================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.email"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="email" id="organisationForm_email" tabindex="5" cssClass="formField required validate-email" maxlength="50" />
				</td>		
			<!-- ====================================CAPITAL=================================================== -->
				<td class="labelTd">
					<spring:message code="organisation.capital"/>
				</td>
				<td>
					<form:input path="capital" id="organisationForm_capital" tabindex="9" cssClass="formField " maxlength="30" onkeypress="return restrictCharactersForCapital(event);"/>
				</td>				
			</tr>			
			<tr>	
			<!-- ====================================IBAN====================================================== -->
				<td class="labelTd">
					<spring:message code="organisation.iban"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="iban" id="organisationForm_iban" tabindex="6" cssClass="formField required" maxlength="70" onkeypress="return restrictCharactersForAccount(event);"/>
				</td>		
			<!-- ====================================J========================================================= -->			
				<td class="labelTd">			
					<spring:message code="organisation.j"/><span id="required">*</span>
				</td>
				<td>
					<form:input path="j" id="organisationForm_j" maxlength="70" tabindex="10" cssClass="formField required" onkeypress="return restrictCharactersForIdentificationNumbers(event);"/>
				</td>					
			</tr>
			<!-- ====================================LOCATION================================================== -->
			<tr>
				<td class="labelTd">
					<spring:message code="organisation.location"/><span id="required">*</span>
				</td>
				<td>
					<form:textarea path="location" id="organisationForm_location" cols="40" rows="3" tabindex="7" cssClass="formField required" onkeypress="return restrictCharactersForAddress(event);"/>
					<div id="organisationForm_locationDIV" style="display: inline; color: red;"></div>
					<div class="remainingCharactersOrgLocation" style="display:none" >
						<div id="showRemainingCharactersLocation"/>
					</div>
				</td>
			<!-- ====================================OBSERVATION=============================================== -->
				<td class="labelTd">
					<spring:message code="organisation.observation"/>
				</td>
				<td>
					<form:textarea path="observation" id="organisationForm_observation" tabindex="11" cols="40" rows="3" cssClass="formField "/>
					<div class="remainingCharactersOrgObs" style="display:none" >
						<div id="showRemainingCharacters"/>
					</div>
				</td>
			</tr>
			<!-- ====================================TYPE=============================================== -->
			<c:choose>
				<c:when test="${organisationBean.organisationId gt 0}">
					<form:hidden path="type" id="organisationForm_type"/>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${organisationBean.parentId <= 0}">						
							<tr>
								<td class="labelTd"> 
									<spring:message code="organisation.select.type" /><span id="required">*</span>
								</td>	
								<td>			
									<div title='<spring:message code="organisation.choose.type"/>' id="organisationFormTypesId">			
										<form:select path="type" id="organisationForm_type" tabindex="12" cssClass="formField validate-not-first" onchange="changeTitle(this.options[selectedIndex].text, 'organisationFormTypesId')">
											<form:option value="-1"><spring:message code="organisation.choose.type"></spring:message></form:option>
											<c:choose>
												<c:when test="${organisationBean != null}">						
													<c:forEach var="type" items="${ORGANISATION_TYPE}">
														<!-- not for branch -->
														<c:if test="${type.value != 4}">
															<c:choose>
																<c:when test="${type.value == organisationBean.type}">
																	<option title='<spring:message code="${type.label}"/>'  value="${type.value}" SELECTED><spring:message code="${type.label}"/></option>
																</c:when>
																<c:otherwise>
																	<option title='<spring:message code="${type.label}"/>' value="${type.value}"><spring:message code="${type.label}"/></option>
																</c:otherwise>
															</c:choose>	
														</c:if>						
													</c:forEach>
												</c:when>
												<c:otherwise>
													<c:forEach var="type" items="${ORGANISATION_TYPE}">
														<form:option title="${type.label}"  value="${type.value}"><spring:message code="${type.label}"/></form:option>
													</c:forEach>
												</c:otherwise>
											</c:choose>
										</form:select>	
									</div>	
								</td>										
							</tr>	
						</c:when>
						<c:otherwise>
							<c:choose>												
								<c:when test="${organisationBean.type == 0}">	
								<tr>
									<td class="labelTd">
										<spring:message code="organisation.select.type" />
									</td>	
									<td>
										<div title='<spring:message code="organisation.choose.type"/>' id="organisationFormTypesId">
											<form:select path="type" id="organisationForm_type" tabindex="12" cssClass="formField validate-not-first" onchange="changeTitle(this.options[selectedIndex].text, 'organisationFormTypesId')">
												<form:option value="-1"><spring:message code="organisation.choose.type"></spring:message></form:option>
												<c:choose>
													<c:when test="${organisationBean != null}">						
														<c:forEach var="type" items="${GROUP_COMPANIES_TYPE}">
															<c:choose>
																<c:when test="${type.value == organisationBean.type}">
																	<option title='<spring:message code="${type.label}"/>'  value="${type.value}" SELECTED><spring:message code="${type.label}"/></option>
																</c:when>
																<c:otherwise>
																	<option title='<spring:message code="${type.label}"/>'  value="${type.value}"><spring:message code="${type.label}"/></option>
																</c:otherwise>
															</c:choose>							
														</c:forEach>
													</c:when>
													<c:otherwise>												
														<c:forEach var="type" items="${GROUP_COMPANIES_TYPE}">
															<form:option value="${type.value}"><spring:message code="${type.label}"/></form:option>
														</c:forEach>
													</c:otherwise>
												</c:choose>
											</form:select>
										</div>
									</td>
								</tr>
								</c:when>
								<c:otherwise>	
									<form:hidden path="type" id="organisationForm_type"/>
								</c:otherwise>	
							</c:choose>
						</c:otherwise>		
					</c:choose>
				</c:otherwise>
			</c:choose>			
			<!-- ====================================PARENT=============================================== -->		
			<tr>
				<c:choose>
					<c:when test="${organisationBean.parentId > 0}">
						<td class="labelTd">
							<spring:message code="organisation.parent" />&nbsp;&nbsp;
						</td>	
						<form:hidden path="parentId" id="organisationForm_parentId"/>																	
						<td>	
							${PARENT}																														
						</td>	
					</c:when>	
				</c:choose>																															
			</tr>				
						
			<!-- ====================================MODULES=================================================== -->		
			<c:choose>			
				<c:when test="${IS_ADMIN_IT eq 'true' or ACTION == 'ADDCOMPANY' or ACTION == 'ADDBRANCH'}">										
					<tr><td colspan="4" height="20">&nbsp;</td></tr>
			 		<tr>
						<td class="labelTd"> 
							<spring:message code="organisation.list.modules"/><span id="required">*</span>
						</td>	
								
						<td colspan="3">
							<table style="border: 1px solid #d7d7d7; width:550px !important; ">
								<tr><td style="line-height:10px;">&nbsp;</td></tr>
								<tr>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
									
									<td id="allModules">
										<div class="selectTitle"><spring:message code="organisation.select.all.modules"/></div>
										<br/>
										<select id="changeModulesForm_select1" name="changeModulesForm_select1" multiple="multiple" size="10" tabindex="13" class="formField">
											<c:forEach var="module" items="${ALL_MODULES}">
												<option title="${module.name}" id="${module.moduleId}" value="${module.moduleId}">${module.name}</option>
											</c:forEach>
										</select>
									</td>
									
									<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/>
										<div class="addButton" tabindex="14" title="<spring:message code="organisation.change.modules.addModule"/>" onClick="moveSelectOptionsSourceDest('changeModulesForm_select1', 'changeModulesForm_select2', true)"></div><br/><br/>
										<div class="removeButton" tabindex="15" title="<spring:message code="organisation.change.modules.removeModule"/>" onClick="moveSelectOptionsSourceDest('changeModulesForm_select2', 'changeModulesForm_select1', true)"></div>
									</td>
									
									<td> 													
										<div class="selectTitle"><spring:message code="organisation.select.current.modules"/></div>
										<br/>													
										<select id="changeModulesForm_select2" name="modules" multiple="multiple" size="10" tabindex="16" class="formField validate-length">
											<c:forEach var="module" items="${organisationBean.modules}">
												<option title="${module.name}" id="${module.moduleId}" value="${module.moduleId}">${module.name}</option>
											</c:forEach>
										</select>
									</td>							
									
									<td width="100%">&nbsp;</td>
								</tr>
								<tr><td style="line-height:10px;">&nbsp;</td></tr>
							</table>
						</td>
					</tr>
				</c:when>
				<c:otherwise>
				
				</c:otherwise>	
			</c:choose>
			<tr>
				<td colspan="5" class="formActions" align="right">
					<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_OrgUpdate}, ${PERMISSION_CONSTANT.OM_OrgAddChild}">
						<input type="button" class="button" id="save" tabindex="17" value="<spring:message code="save"/>"/>						
						<input type="button" class="button" id="cancel" tabindex="18" value="<spring:message code="cancel"/>" />						
					</security:authorize>
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

<script type="text/JavaScript">
	<c:choose>
		<c:when test="${(ACTION == BACK_CONSTANT.OM_Back_Get_Organisation or ACTION == BACK_CONSTANT.OM_Back_AddOrganisationCompany or ACTION == BACK_CONSTANT.OM_Back_AddOrganisationBranch) and SESS_ORGANISATION_ID ne null}">
			getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");									  
			YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
		</c:when>
		<c:otherwise>						
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
				getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);			
			</security:authorize>												
		</c:otherwise>
	</c:choose>		

	//------------------------------------- CANCEL ORGANISATION ------------------------------------------------			
	<c:choose>
	<c:when test="${(ACTION == BACK_CONSTANT.OM_Back_Get_Organisation or ACTION == BACK_CONSTANT.OM_Back_AddOrganisationCompany or ACTION == BACK_CONSTANT.OM_Back_AddOrganisationBranch) and SESS_ORGANISATION_ID ne null}">
			getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");									  
			YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);
		</c:when>
		<c:otherwise>						
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
				getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);			
			</security:authorize>												
		</c:otherwise>
	</c:choose>			
	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>	

	//------------------------------------- VALIDATION AND SUBMIT --------------------------------		
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT._Super}, ${PERMISSION_CONSTANT.OM_OrgUpdate}, ${PERMISSION_CONSTANT.OM_OrgAddChild}">
		document.organisationForm.name.focus();	
		var submitObject = new ObjSubmitSaveOrganisation("OrganisationForm.htm?ACTION=SAVE", "organisationForm", "MAIN_CONTENT", 'changeModulesForm_select2', 'organisationForm_name_unique');
		YAHOO.util.Event.addListener("save", "click", submitSaveOrganisation, submitObject, true);					
	</security:authorize>

	//------------ DELETE ORGANISATION ---------------------------------------------------------------
	<c:if test="${organisationBean.organisationId != -1}">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
			submitObject = new ObjSubmitWithConfirmation("OrganisationSearch.htm?ACTION=DELETE_FROM_ORGANISATION_FORM&organisationId=${organisationBean.organisationId}", "", "MAIN_CONTENT", '<spring:message code="organisation.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);
		</security:authorize>
	</c:if>

</script>

<c:if test="${organisationBean.organisationId ge 0}">
	<script>
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ChangeLogo}">	
			var submitObject = new ObjSubmit('ClientLogoChange.htm?ORG=${organisationBean.organisationId}', "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("logoChange", "click", getContentFromUrl, submitObject, true);
		</security:authorize>
	</script>
</c:if>	

<script type="text/JavaScript">
	//---------------------------------- END VALIDATION ANOD SUBMIT -------------------------------				
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT._Super},${PERMISSION_CONSTANT.OM_OrgUpdate}, ${PERMISSION_CONSTANT.OM_OrgAddChild}">
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS -----------------------
		var validateObservationField = new ObjFieldRemaining("organisationForm_observation", '<spring:message code="observation.message"/>', 2000);
		YAHOO.util.Event.addListener("organisationForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
		YAHOO.util.Event.addListener("organisationForm_observation", "blur", hideInfoOnBlurInField);
		YAHOO.util.Event.addListener("organisationForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);	
	
		var validateLocationField = new ObjFieldRemaining("organisationForm_location", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersLocation');
		YAHOO.util.Event.addListener("organisationForm_location", "focus", showInfoOnClickInField, validateLocationField, true);
		YAHOO.util.Event.addListener("organisationForm_location", "blur", hideInfoOnBlurInField, validateLocationField, true);
		YAHOO.util.Event.addListener("organisationForm_location", "keyup", showInfoOnKeyUpInField, validateLocationField, true);


		var validateAddressField = new ObjFieldRemaining("organisationForm_address", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersAddress');
		YAHOO.util.Event.addListener("organisationForm_address", "focus", showInfoOnClickInField, validateAddressField, true);
		YAHOO.util.Event.addListener("organisationForm_address", "blur", hideInfoOnBlurInField, validateAddressField, true);
		YAHOO.util.Event.addListener("organisationForm_address", "keyup", showInfoOnKeyUpInField, validateAddressField, true);	

		var submitObject = new ObjSubmit("OrganisationForm.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewOrganisation", "click", getContentFromUrl, submitObject, true);	
		
		//---------------------------------- END OPERATIONS ON SEPARATED FIELDS -----------------------
		
		//---------------------------------- EXAMPLE VALIDATION DIRECT ON FIELDS -----------------------
		//var field1 = new ObjValidation("orgName", "blur");
		//var field2 = new ObjValidation("orgPhone", "blur");
		//var field3 = new ObjValidation("orgFax", "blur");
		//arrayResult = new Array(field1, field2, field3);
		//add listeners to these fields for field validation not submit of form
		//YAHOO.util.Event.addListener(window, "load", addListenersToFields, arrayResult, true);
		//---------------------------------- END EXAMPLE VALIDATION DIRECT ON FIELDS -----------------------
	
		
		//----------------------------------- ORGANISATION ENTER EVENT ON ALL FIELDS---------------------------------------
		var keyListner = null; 
		var submitObject = new ObjSubmitSaveOrganisation("OrganisationForm.htm?ACTION=SAVE", "organisationForm", "MAIN_CONTENT", 'changeModulesForm_select2', 'organisationForm_name_unique');

		keyListener = new YAHOO.util.KeyListener("organisationForm_name", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_address", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_phone" , { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_fax", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_email", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_j", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_cui", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_iban", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		keyListener = new YAHOO.util.KeyListener("organisationForm_capital", { keys:13 },  { fn:submitSaveOrganisation, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
		
	</security:authorize>
	
</script>
