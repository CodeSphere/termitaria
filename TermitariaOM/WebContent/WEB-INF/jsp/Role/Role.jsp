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
	<c:when test="${roleWebBean.roleId > 0}">
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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleAdd}">					
					<a href="#" id="addNewRole" title="<spring:message code="role.submeniu.add.title"/>"><spring:message code='add'/> </a>
				</security:authorize>								
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleDelete}">
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="role.submeniu.delete.title"/>"><spring:message code="delete"/></a>
				</security:authorize>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${roleWebBean.roleId > 0}">
		<span class="section_title"><img src="images/titles/RoleEdit.jpg"/><spring:message code="role.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/RoleAdd.jpg"/><spring:message code="role.section.title"/></span>		
	</c:otherwise>
</c:choose>

<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
	<%@ page import="org.springframework.web.servlet.support.RequestContextUtils"%>
	<br/>
</div>

<c:set var="language" value="<%= RequestContextUtils.getLocale(request).getLanguage()%>"/>

	<form:form commandName="roleWebBean" name="roleForm" id="roleForm" onsubmit="return false;">
	<form:hidden path="roleId"/>
	<form:hidden path="status"/>
	<form:hidden path="description.localizationId"/>
	
		<table cellpadding="3" class="tableAdd">
			<tr><td class="tableAddSpacer">&nbsp;</td></tr>
			<tr>
				<td class="labelTd">
					<spring:message code="role.name"/><span id="required">*</span>
				</td>
				<td>								
					<form:input path="name" id="roleForm_name"  maxlength="100" tabindex="1" cssClass="formField required" onblur="verifyUniqueness('RoleVerifyNameUniqueness.htm?198341='+encodeURIComponent(this.value)+'&198342=${SESS_ORGANISATION_ID}', 'roleForm_name', 'roleForm_name_unique', this.value, '${SESS_ORGANISATION_ID}', '${roleWebBean.name}', '${roleWebBean.organisationId}' );"  onkeypress="return restrictSpecialCharacters(event);"/>																																							
						<div id="roleForm_nameDIV2" style="display: inline"></div> 
				</td>
			</tr>
			<tr>
				<td class="labelTd">
					<spring:message code="description.en"/><span id="required">*</span>
				</td>
				<td>
					<form:textarea path="description.en" id="roleForm_description_en" tabindex="2" cssClass="formField required " onkeypress="return restrictSpecialCharactersForObs(event);"/>
					<div class="remainingCharactersDescriptionEn" style="display:none" >
						<div id="showRemainingCharactersEn"/> 
					</div> 
				</td>
			</tr>
			<tr>
				<td class="labelTd">
					<spring:message code="description.ro"/><span id="required">*</span>
				</td>
				<td>					
					<form:textarea path="description.ro" id="roleForm_description_ro" tabindex="3" cssClass="formField required " onkeypress="return restrictSpecialCharactersForObs(event);"/>
					<div class="remainingCharactersDescriptionRo" style="display:none" >
						<div id="showRemainingCharactersRo" />						
					</div> 
				</td>
			</tr>
			<tr>
				<td class="labelTd">
					<spring:message code="role.observation"/>
				</td>
				<td>
					<form:textarea path="observation" id="roleForm_observation" tabindex="4" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>					
						<div class="remainingCharactersRoleObs" style="display:none" >
							<div id="showRemainingCharacters"/>							
						</div> 				
				</td>
			</tr>
		<!--  LIST MODULES --> 
		<tr>
			<td class="labelTd">
				<spring:message code="role.module"/><span id="required">*</span>
			</td>
			<td>
				<div id="MODULES">
					<c:choose>
						<c:when test="${MODULES ne null}">
							<c:choose>
								<c:when test = "${ROLE_MODULE eq null}">
									<div title='<spring:message code="role.choose.module"/>' id="roleFormModulesId">
										<select name="moduleId" id="roleForm_module"  tabindex="5" class="formField validate-not-first" onChange="javascript:getContentFromUrlDirect('RolePermissionsForModule.htm?moduleId=' + this.value, 'ALL_PERMISSIONS');changeTitle(this.options[selectedIndex].text, 'roleFormModulesId')">
											<option value="-1"><spring:message code="role.choose.module"/></option>							
											<c:forEach var="module" items="${MODULES}">
												<c:choose>
													<c:when test="${roleWebBean.moduleId == module.moduleId}">
														<option title="${module.name}" value="${module.moduleId}" SELECTED>${module.name}</option>
													</c:when>
													<c:otherwise>
														<option title="${module.name}" value="${module.moduleId}">${module.name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>																						
										</select>
									</div>
								</c:when>
								<c:otherwise>
									<div title='${ROLE_MODULE.name}' id="roleFormModulesId">
										<select name="moduleId" id="roleForm_module"  tabindex="5" class="formField validate-not-first" onChange="javascript:getContentFromUrlDirect('RolePermissionsForModule.htm?moduleId=' + this.value, 'ALL_PERMISSIONS');changeTitle(this.options[selectedIndex].text, 'roleFormModulesId')">
											<option value="-1"><spring:message code="role.choose.module"/></option>							
											<c:forEach var="module" items="${MODULES}">
												<c:choose>
													<c:when test="${roleWebBean.moduleId == module.moduleId}">
														<option title="${module.name}" value="${module.moduleId}" SELECTED>${module.name}</option>
													</c:when>
													<c:otherwise>
														<option title="${module.name}" value="${module.moduleId}">${module.name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>																						
										</select>
									</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${MODULES ne null  and empty MODULES}">
							<div id="NO_MODULES">
								<spring:message code="role.no.module"></spring:message>
							</div>
							<div id="MESSAGE">
							</div>
						</c:when>
						<c:otherwise>
							<form:select path="moduleId" tabindex="5">
								<form:option value="-1"><spring:message code="role.choose.org.first"/></form:option>
							</form:select>
						</c:otherwise>
					</c:choose>
				</div>			
			</td>
		</tr>
		<!--  LIST PERMISSIONSS --> 
		<tr><td colspan="4" height="20">&nbsp;</td></tr>
	 		<tr>				
				<td class="labelTd">
					<spring:message code="role.list.permissions"/>
				</td>		
				<td colspan="3">
					<div id="ALL_PERMISSIONS">		
						<table style="border: 1px solid #d7d7d7; width:500px;">
							<tr><td style="line-height:10px;">&nbsp;</td></tr>
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>					
								<td id="allModules">
									<div class="selectTitle">
										<spring:message code="role.select.all.permissions"/>
										<c:choose>
												<c:when test ="${SELECTED_MODULE_NAME != null}">
													${SELECTED_MODULE_NAME}
												</c:when>
												<c:otherwise>
													<spring:message code="role.select.all.module"/>
												</c:otherwise>
										</c:choose>
									</div>
									<br/>
									<select id="changeRolesPermissionsForm_select1" name="changeRolesPermissionsForm_select1" tabindex="6" multiple="multiple" size="10" style="width: 200px;" >
										<c:forEach var="permission" items="${ALL_PERMISSIONS}">
											<c:choose>
												<c:when test="${language == 'en'}">
													<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
												</c:when>
												<c:when test="${language == 'ro'}">
													<option title="${permission.description.ro}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
												</c:when>
												<c:otherwise>
													<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</td>
																						
								<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
									<div class="addButton" tabindex="7" title="<spring:message code="role.addPermission"/>" onClick="moveSelectOptionsSourceDest('changeRolesPermissionsForm_select1', 'changeRolesPermissionsForm_select2'); "></div><br/><br/>
									<div class="removeButton" tabindex="8"  title="<spring:message code="role.removePermission"/>" onClick="moveSelectOptionsSourceDest('changeRolesPermissionsForm_select2', 'changeRolesPermissionsForm_select1', true); "></div>
								</td>
								
								<td>
									<div class="selectTitle"><spring:message code="role.select.current.permissions"/></div>
									<br/>
									<select id="changeRolesPermissionsForm_select2" name="permissions" tabindex="10" multiple="multiple" size="10" style="width: 200px;" >
										<c:forEach var="permission" items="${PERMISSIONS}">
											<c:choose>
	 											<c:when test="${language == 'en'}">  
													<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
												</c:when>
												<c:when test="${language == 'ro'}">
													<option title="${permission.description.ro}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
												</c:when>
												<c:otherwise>
													<option title="${permission.description.en}" id="${permission.permissionId}" value="${permission.permissionId}">${permission.name}</option>
												</c:otherwise>	 
											</c:choose>
										</c:forEach>
									</select>
								</td>		
																														
								<td width="100%">&nbsp;</td>
							</tr>
							<tr><td style="line-height:10px;">&nbsp;</td></tr>
						</table>								
					</div>
				</td>		
			</tr>
			<tr>
				<td colspan="5" class="formActions" align="right">
					<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_RoleAdd}, ${PERMISSION_CONSTANT.OM_RoleUpdate}">
						<input type="button" class="button" id="save" tabindex="10" value="<spring:message code="save"/>"/>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleSearch}">							
							<input type="button" class="button" id="cancel" tabindex="11" value="<spring:message code="cancel"/>"/>							
						</security:authorize>
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
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleSearch}">
		getObject = new ObjSubmit("SearchRole.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	
	<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_RoleAdd}, ${PERMISSION_CONSTANT.OM_RoleUpdate}">		
		document.roleForm.name.focus();
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR EN DESCRIPTION -----------------------
		var validateDescriptionFieldEn = new ObjFieldRemaining("roleForm_description_en", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersEn');
		YAHOO.util.Event.addListener("roleForm_description_en", "focus", showInfoOnClickInField, validateDescriptionFieldEn, true);
		YAHOO.util.Event.addListener("roleForm_description_en", "blur", hideInfoOnBlurInField, validateDescriptionFieldEn, true);
		YAHOO.util.Event.addListener("roleForm_description_en", "keyup", showInfoOnKeyUpInField, validateDescriptionFieldEn, true);
		
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR RO DESCRIPTION -----------------------
		var validateDescriptionFieldRo = new ObjFieldRemaining("roleForm_description_ro", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersRo');
		YAHOO.util.Event.addListener("roleForm_description_ro", "focus", showInfoOnClickInField, validateDescriptionFieldRo, true);
		YAHOO.util.Event.addListener("roleForm_description_ro", "blur", hideInfoOnBlurInField, validateDescriptionFieldRo, true);
		YAHOO.util.Event.addListener("roleForm_description_ro", "keyup", showInfoOnKeyUpInField, validateDescriptionFieldRo, true);
	
		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION --------------------------
		var validateObservationField = new ObjFieldRemaining("roleForm_observation", '<spring:message code="observation.message"/>', 2000);
		YAHOO.util.Event.addListener("roleForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
		YAHOO.util.Event.addListener("roleForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
		YAHOO.util.Event.addListener("roleForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
		//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ------------------------------------------
			
		//------------------------------------- VALIDATION AND SUBMIT --------------------------------	
		var submitObject = new ObjSubmitSaveRole("Role.htm?ACTION=SAVE", "roleForm", "MAIN_CONTENT", 'changeRolesPermissionsForm_select2', 'roleForm_name_unique', '${SESS_ORGANISATION_ID}');
		YAHOO.util.Event.addListener("save", "click", submitSaveRole, submitObject, true);

		//------------------------------------- CANCEL ROLE ------------------------------------------------						
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleSearch}">	
			getObject = new ObjSubmit("SearchRole.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);				
		</security:authorize>		
	</security:authorize>
		

	//---------------------------------- END VALIDATION AND SUBMIT -------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleAdd}">
		var submitObject = new ObjSubmit("Role.htm?ACTION=SAVE", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewRole", "click", getContentFromUrl, submitObject, true);
	</security:authorize>

	//----------------------------------------- ROLE ENTER EVENT ---------------------------------------
	<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_RoleAdd}, ${PERMISSION_CONSTANT.OM_RoleUpdate}">
		var submitObject = new ObjSubmitSaveRole("Role.htm?ACTION=SAVE", "roleForm", "MAIN_CONTENT", 'changeRolesPermissionsForm_select2', 'roleForm_name_unique');
		var keyListener = new YAHOO.util.KeyListener("roleForm_name", { keys:13 },  { fn: submitSaveRole, scope: submitObject, correctScope:true}, null ); 
		keyListener.enable();
	</security:authorize>


	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>	

	//------------ DELETE ROLE ---------------------------------------------------------------
	<c:if test="${roleWebBean.roleId != -1}">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_RoleDelete}">
			submitObject = new ObjSubmitWithConfirmation("SearchRole.htm?ACTION=DELETE_FROM_ROLE_FORM&roleId=${roleWebBean.roleId}", "", "MAIN_CONTENT", '<spring:message code="role.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);			
		</security:authorize>		
	</c:if>
</script>
