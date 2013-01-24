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
	<c:when test="${permissionBean.permissionId > 0}">
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
					<a href="#" id="addNewPermission" title="<spring:message code="permission.submeniu.add.title"/>"><spring:message code='add'/> </a>												
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="permission.submeniu.delete.title"/>"><spring:message code='delete'/> </a>					
				</security:authorize>	
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${permissionBean.permissionId > 0}">
		<span class="section_title"><img src="images/titles/PermissionEdit.jpg"/><spring:message code="permission.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/PermissionAdd.jpg"/><spring:message code="permission.section.title"/></span>		
	</c:otherwise>
</c:choose>

<br/>

<%@ include file="../Messages.jsp" %>

	<form:form commandName="permissionBean" id="permissionForm" name="permissionForm" onsubmit="return false;">
	<form:hidden path="permissionId"/>
	<form:hidden path="description.localizationId"/>
	<table cellpadding="3" class="tableAdd">
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>
		<tr>
			<td class="labelTd">
				<spring:message code="permission.name" /><span id="required">*</span> 
			</td>
			<td width="200">
				<form:input path="name" id="permissionForm_name" tabindex="1" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharacters(event);"/>
			</td>
		</tr>	
		<tr>
			<td class="labelTd">
				<spring:message code="description.en"/>
			</td>
			<td>
				<form:textarea id="permissionForm_description_en" path="description.en" tabindex="2" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
				<div class="remainingCharactersPermissionDescriptionEn" style="display:none" >
					<div id="showRemainingCharactersEn" />
				</div> 
			</td>
		</tr>
		<tr>
			<td class="labelTd">
				<spring:message code="description.ro"/>
			</td>
			<td>
				<form:textarea id="permissionForm_description_ro" path="description.ro" tabindex="3" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
				<div class="remainingCharactersPermissionDescriptionRo" style="display:none" >
					<div id="showRemainingCharactersRo" /> 
				</div>
			</td>
		</tr>
		<tr>
			<td class="labelTd">
				<spring:message code="permission.module"/><span id="required">*</span>
			</td>
			<td>		
				<c:choose>
					<c:when test = "${PERMISSION_MODULE eq null}">
						<div title='<spring:message code="permission.choose.module"/>' id="permissionFormModulesId">
							<form:select path="moduleId" id="permissionForm_moduleId" cssClass="formField validate-not-first" tabindex="4" onchange="changeTitle(this.options[selectedIndex].text, 'permissionFormModulesId')">
								<form:option value="-1"><spring:message code="permission.choose.module"></spring:message></form:option>
								<c:choose>
									<c:when test="${permissionBean != null}">
										<c:forEach var="module" items="${MODULES}">
											<c:choose>
												<c:when test="${module.moduleId == permissionBean.moduleId}">
													<option title="${module.name}" value="${module.moduleId}" SELECTED>${module.name}</option>
												</c:when>
												<c:otherwise>
													<option title="${module.name}" value="${module.moduleId}">${module.name}</option>
												</c:otherwise>
											</c:choose>							
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach var="module" items="${MODULES}">
											<form:option title="${module.name}" value="${module.moduleId}">${module.name}</form:option>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</form:select>
						</div>
					</c:when>
					<c:otherwise>
						<div title='${PERMISSION_MODULE.name}' id="permissionFormModulesId">
							<form:select path="moduleId" id="permissionForm_moduleId" cssClass="formField validate-not-first" tabindex="4" onchange="changeTitle(this.options[selectedIndex].text, 'permissionFormModulesId')">
								<form:option value="-1"><spring:message code="permission.choose.module"></spring:message></form:option>
								<c:choose>
									<c:when test="${permissionBean != null}">
										<c:forEach var="module" items="${MODULES}">
											<c:choose>
												<c:when test="${module.moduleId == permissionBean.moduleId}">
													<option title="${module.name}" value="${module.moduleId}" SELECTED>${module.name}</option>
												</c:when>
												<c:otherwise>
													<option title="${module.name}" value="${module.moduleId}">${module.name}</option>
												</c:otherwise>
											</c:choose>							
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:forEach var="module" items="${MODULES}">
											<form:option title="${module.name}" value="${module.moduleId}">${module.name}</form:option>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</form:select>
						</div>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="formActions" align="right" colspan="2">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
					<input type="button" class="button" id="save" tabindex="5" value="<spring:message code="permission.save"/>" />					
					<input type="button" class="button" id="cancel" tabindex="6" value="<spring:message code="cancel"/>" />					
				</security:authorize>
			</td>
		</tr>
	</table>
	
	<br/>
	
	
	</form:form>


<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		document.permissionForm.name.focus();
	</security:authorize>

	getObject = new ObjSubmit("SearchPermission.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR EN DESCRIPTION -----------------------
		var validateDescriptionFieldEn = new ObjFieldRemaining("permissionForm_description_en", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersEn');
		YAHOO.util.Event.addListener("permissionForm_description_en", "focus", showInfoOnClickInField,  validateDescriptionFieldEn, true);
		YAHOO.util.Event.addListener("permissionForm_description_en", "blur", hideInfoOnBlurInField,  validateDescriptionFieldEn, true);
		YAHOO.util.Event.addListener("permissionForm_description_en", "keyup", showInfoOnKeyUpInField,  validateDescriptionFieldEn, true);
	
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR RO DESCRIPTION -----------------------
		var validateDescriptionFieldRo = new ObjFieldRemaining("permissionForm_description_ro", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersRo');
		YAHOO.util.Event.addListener("permissionForm_description_ro", "focus", showInfoOnClickInField,  validateDescriptionFieldRo, true);
		YAHOO.util.Event.addListener("permissionForm_description_ro", "blur", hideInfoOnBlurInField, validateDescriptionFieldRo, true);
		YAHOO.util.Event.addListener("permissionForm_description_ro", "keyup", showInfoOnKeyUpInField, validateDescriptionFieldRo, true);
		//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ------------------------------------------
		
		//------------------------------------- CANCEL PERMISSION ------------------------------------------------				
		getObject = new ObjSubmit("SearchPermission.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);
			
		// ------------------------------------ VALIDATION AND SUBMIT --------------------------------
		var submitObject = new ObjSubmit("Permission.htm?ACTION=SAVE", "permissionForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("save", "click", submitValidateAndSaveForm, submitObject, true);	

		//---------------------------------- END VALIDATION AND SUBMIT -------------------------------
	
	
		var submitObject = new ObjSubmit("Permission.htm?ACTION=SAVE", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewPermission", "click", getContentFromUrl, submitObject, true);

		//------------ DELETE PERMISSION ---------------------------------------------------------------	
		<c:if test="${permissionBean.permissionId != -1}">
			submitObject = new ObjSubmitWithConfirmation("SearchPermission.htm?ACTION=DELETE_FROM_PERMISSION_FORM&permissionId=${permissionBean.permissionId}", "", "MAIN_CONTENT", '<spring:message code="permission.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);			
		</c:if>	
	
		//----------------------------------------- PERMISION ENTER EVENT ---------------------------------------

		var submitObject = new ObjSubmit("Permission.htm?ACTION=SAVE", "permissionForm", "MAIN_CONTENT");
		var keyListener = new YAHOO.util.KeyListener("permissionForm_name" , { keys:13 },  { fn: submitValidateAndSaveForm, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
	
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>
	
</script>
