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
	<c:when test="${departmentBean.departmentId > 0}">
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
				<a href="#" id="home" ><spring:message code="home" /></a>			
				<a href="#" id="back"><spring:message code="back"/></a>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptAdd}">					
					<a href="#" id="addNewDepartment" title="<spring:message code="department.submeniu.add.title"/>"><spring:message code='add'/></a>					
				</security:authorize>
								
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}">				 	
				 	<a href="#" id="addNewPerson" class="${cssClass}" title="<spring:message code="department.submeniu.addPerson.title"/>"><spring:message code="department.person.add.new"/></a>					
				</security:authorize>	
																 
			 	<a href="#" id="listPersons" class="${cssClass}" title="<spring:message code="department.submeniu.listPersons.title"/>"><spring:message code="department.listPersons"/></a>
		
			 	<a href="#" id="listSubdepartments" class="${cssClass}" title="<spring:message code="department.submeniu.listSubdepartments.title"/>"><spring:message code="department.listSubDepartments"/></a>
				
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptDelete}">
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="department.submeniu.delete.title"/>"><spring:message code="delete"/></a>
				</security:authorize>					
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${departmentBean.departmentId > 0}">
		<span class="section_title"><img src="images/titles/DepartmentEdit.jpg"/><spring:message code="department.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/DepartmentAdd.jpg"/><spring:message code="department.add.section.title"/></span>
	</c:otherwise>
</c:choose>

<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include file="../Messages.jsp" %>
</div>


<form:form method="post" commandName="departmentBean" id="departmentForm" name="departmentForm" onsubmit="return false;">
	
	<form:hidden path="departmentId" id="departmentId" />
	
	<input type="hidden" name="organisationId" value="${SESS_ORGANISATION_ID}" id="organisationId"/>

	<table cellpadding="3" class="tableAdd">
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>
		<!-- =========================================NAME======================================================== -->
		<tr>
			<td class="labelTd">
				<spring:message code="department.name"/> <span id="required">*</span> 
			</td>
			<td>
				<form:input id="departmentForm_name" path="name" tabindex="1" cssClass="formField required" maxlength="100" onkeypress="return restrictSpecialCharacters(event);"/>
			</td>
		</tr>
		
		<!-- ====================================MANANGER========================================================= -->
		<tr>
			<td class="labelTd">
				<spring:message code="department.manager.name"/>
			</td>
			<td>
				<form:hidden path="managerId" id="managerId"/>
				<div id="myAutoComplete" class="formField autoComplete">
					<c:choose>
						<c:when test="${MANAGER_NAME != null}">
							<input id="managerAutoComplete" type="text" value="${MANAGER_NAME}" tabindex="2" class="formField"/>
						</c:when>
						<c:otherwise>
							<input id="managerAutoComplete" type="text" tabindex="2" class="formField"/>
						</c:otherwise>
					</c:choose>
					<div id="autoCompleteContainer"></div>
				</div>		
			</td>
		</tr>
		<!-- ====================================PARENT DEPARTMENT================================================ -->
		<c:choose>
			<c:when test="${POTENTIAL_PARENT_DEPARTMENTS != null}">
				<tr>
					<td class="labelTd">
						<spring:message code="department.parent.department.name"/> 
					</td>
					<td>
						<c:choose>
							<c:when test = "${PARENT_DEPARTMENT eq null or PARENT_DEPARTMENT.departmentId eq DEFAULT_DEPARTMENT_ID}">
								<div title='<spring:message code="please.select"/>' id="departmentParentDepartmentsId">
									<form:select path="parentDepartmentId" cssClass="formField" tabindex="3" onchange="changeTitle(this.options[selectedIndex].text, 'departmentParentDepartmentsId')">>
										<option value="${DEFAULT_DEPARTMENT_ID}"><spring:message code="please.select"/></option>
										<c:forEach items="${POTENTIAL_PARENT_DEPARTMENTS}" var="dept"> 
											<c:if test="${dept.departmentId != departmentBean.departmentId}">
												<c:choose>
													<c:when test="${departmentBean.parentDepartmentId == dept.departmentId}">
														<option title="${dept.name}" value="${dept.departmentId}" selected="selected">${dept.name}</option>
													</c:when>
													<c:otherwise>
														<option title="${dept.name}" value="${dept.departmentId}">${dept.name}</option>
													</c:otherwise>
												</c:choose>
											</c:if>									
										</c:forEach>
									</form:select>
								</div>							
							</c:when>
							<c:otherwise>
								<div title='${PARENT_DEPARTMENT.name}' id="departmentParentDepartmentsId">
									<form:select path="parentDepartmentId" cssClass="formField" tabindex="3" onchange="changeTitle(this.options[selectedIndex].text, 'departmentParentDepartmentsId')">>
										<option value="${DEFAULT_DEPARTMENT_ID}"><spring:message code="please.select"/></option>
										<c:forEach items="${POTENTIAL_PARENT_DEPARTMENTS}" var="dept"> 
											<c:if test="${dept.departmentId != departmentBean.departmentId}">
												<c:choose>
													<c:when test="${departmentBean.parentDepartmentId == dept.departmentId}">
														<option title="${dept.name}" value="${dept.departmentId}" selected="selected">${dept.name}</option>
													</c:when>
													<c:otherwise>
														<option title="${dept.name}" value="${dept.departmentId}">${dept.name}</option>
													</c:otherwise>
												</c:choose>
											</c:if>									
										</c:forEach>
									</form:select>
								</div>				
							</c:otherwise>
						</c:choose>						
					</td>		
				</tr>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="parentDepartmentId" tabindex="3" value="${DEFAULT_DEPARTMENT_ID}"/>
			</c:otherwise>
		</c:choose>
	 	
	 	<!-- ====================================OBSERVATION====================================================== -->
		<tr>
			<td class="labelTd">
				<spring:message code="department.observation"/>
			</td>
			<td>
				<form:textarea path="observation" id="departmentForm_observation" tabindex="4" cssClass="formField"/>
				<div class="remainingCharactersDeptObs" style="display:none" >
					<div id="showRemainingCharacters" />			
				</div>				
			</td>
		</tr>
		
		<!-- ====================================PERSONS====================================================== -->		
			<tr><td colspan="4" height="20">&nbsp;</td></tr>
			<tr>
				<td class="labelTd">
					<spring:message code="department.list.persons"/>
				</td>
				<td colspan="3">
					<table style="border: 1px solid #d7d7d7; width:500px; ">
						<tr><td style="line-height:10px;">&nbsp;</td></tr>
						<tr>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							
							<td id="allPersons">
								<div class="selectTitle"><spring:message code="department.select.all.persons"/></div>
								<br/>
								<select id="changePersonsForm_select1" name="changePersonsForm_select1" multiple="multiple" size="10" tabindex="5" style="width:200px">
									<c:forEach var="person" items="${ALL_PERSONS}">
										<option title="${person.firstName} ${person.lastName}" id="${person.personId}" value="${person.personId}">${person.firstName} ${person.lastName}</option>
									</c:forEach>
								</select>
							</td>							
							
							<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
								<a href="#" id="addPersonId" tabindex="6" ><div class="addButton" title="<spring:message code="department.addPerson"/>"></div></a><br/><br/>
								<a href="#" id="removePersonId" tabindex="7" ><div class="removeButton" title="<spring:message code="department.removePerson" />" ></div></a>
							</td>
							
							<td>
								<div class="selectTitle"><spring:message code="department.select.current.persons"/></div>
								<br/>
								<select id="changePersonsForm_select2" name="persons" multiple="multiple" size="10" tabindex="8" style="width:200px">
									<c:forEach var="person" items="${departmentBean.persons}">
										<option title="${person.firstName} ${person.lastName}" id="${person.personId}" value="${person.personId}">${person.firstName} ${person.lastName}</option>
									</c:forEach>
								</select>
							</td>
							
							<td width="100%">&nbsp;</td>						
						
						</tr>
						<tr><td style="line-height:10px;">&nbsp;</td></tr>
					</table>
				</td>				
			</tr>							
		<tr>
			<td colspan="5" class="formActions" align="right">
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}, ${PERMISSION_CONSTANT.OM_DeptAdd}">
					<input type="button" class="button" id="save" tabindex="9" value="<spring:message code="save"/>" />
				</security:authorize>				
				<input type="button" class="button" id="cancel" tabindex="10" value="<spring:message code="cancel"/>" />				
			</td>
		</tr>		
	</table>
</form:form>

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

	//------------ CANCEL DEPARTMENT ---------------------------------------------------------------	
	<c:choose>
		<c:when test="${ACTION == BACK_CONSTANT.OM_Back_AddDepartmentFromOrganisation}">					
			getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");		
			YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);		
		</c:when>
		<c:otherwise>										
			getObject = new ObjSubmit("DepartmentSearch.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);											
		</c:otherwise>
	</c:choose>	
		
	<c:choose>
		<c:when test="${ACTION == BACK_CONSTANT.OM_Back_AddDepartmentFromOrganisation}">					
			getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");		
			YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);		
		</c:when>
		<c:otherwise>										
			getObject = new ObjSubmit("DepartmentSearch.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);											
		</c:otherwise>
	</c:choose>
	
	//------------ SAVE DEPARTMENT ---------------------------------------------------------------	
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}, ${PERMISSION_CONSTANT.OM_DeptAdd}">	
		var submitObject = new ObjSubmitSaveDepartment("DepartmentForm.htm?ACTION=SAVE&org=${org}&orgId=${orgId}", "departmentForm", "MAIN_CONTENT", 'changePersonsForm_select2');
		YAHOO.util.Event.addListener("save", "click", submitSaveDepartment, submitObject, true);
	</security:authorize>
	
	<c:if test="${departmentBean.departmentId > 0}">	
	
		//------------ DELETE DEPARTMENT ---------------------------------------------------------------
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptDelete}">	
			submitObject = new ObjSubmitWithConfirmation("DepartmentSearch.htm?ACTION=DELETE_FROM_DEPARTMENT_FORM&departmentId=${departmentBean.departmentId}", "", "MAIN_CONTENT", '<spring:message code="department.delete.confirmationMessage"/>' , '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);
		</security:authorize>	
	
		//------------ LIST PERSONS ------------------------------------------------------------------
		getObject = new ObjSubmit("ChangeDepartment.htm?ACTION=LIST_DEPARTMENT_PERSONS&organisationId=${departmentBean.organisation.organisationId}&departmentId=${departmentBean.departmentId}", "", "SECONDARY_CONTENT");
		YAHOO.util.Event.addListener("listPersons", "click", getContentFromUrl, getObject, true);
	
		//------------ LIST SUBDEPARTMENTS -----------------------------------------------------------
		getObject = new ObjSubmit("ChangeDepartment.htm?ACTION=LIST_DEPARTMENT_SUBDEPARTMENTS&departmentId=${departmentBean.departmentId}", "", "SECONDARY_CONTENT");
		YAHOO.util.Event.addListener("listSubdepartments", "click", getContentFromUrl, getObject, true);
	
		//------------ ADD NEW PERSONS TO THIS DEPARTMENT --------------------------------------------	
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}">			
			submitObject = new ObjSubmit('PersonForm.htm?ACTION=NEWPERSONFROMDEPARTMENT&departmentId=${departmentBean.departmentId}', "", "MAIN_CONTENT", '<spring:message code="department.person.add.new"/>');
			YAHOO.util.Event.addListener("addNewPerson", "click", getContentFromUrl, submitObject, true);			
		</security:authorize>

	</c:if>
	
	// ----------------------------------------AUTOCOMPLETE MANAGER-------------------------------
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}, ${PERMISSION_CONSTANT.OM_DeptAdd}">		
		instantiatePersonAutoComplete(new YAHOO.util.LocalDataSource(${JSON_PERSONS}), 'managerAutoComplete', 'autoCompleteContainer', 'managerId');
		document.departmentForm.name.focus();
	</security:authorize>
	
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}, ${PERMISSION_CONSTANT.OM_DeptAdd}">	
		// ----------------------------------------ADD PERSONS INLINE-------------------------------
		YAHOO.util.Event.addListener("addPersonId", "click", function addPerson() {
			moveSelectOptionsSourcePersonsFromDepartment('changePersonsForm_select1', 'changePersonsForm_select2', ${MANAGER_ID}, false); sortSelect(document.getElementById("changePersonsForm_select2"));}, null, false);		
		
		// ----------------------------------------REMOVE PERSONS INLINE-------------------------------
		YAHOO.util.Event.addListener("removePersonId", "click", function removePerson() {
			moveSelectOptionsSourcePersonsFromDepartment('changePersonsForm_select2', 'changePersonsForm_select1', ${MANAGER_ID}, true); sortSelect(document.getElementById("changePersonsForm_select1"));}, null, false);
	</security:authorize>
		
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS -----------------------
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}, ${PERMISSION_CONSTANT.OM_DeptAdd}">
		var validateObservationField = new ObjFieldRemaining("departmentForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharacters');
		YAHOO.util.Event.addListener("departmentForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
		YAHOO.util.Event.addListener("departmentForm_observation", "blur", hideInfoOnBlurInField);
		YAHOO.util.Event.addListener("departmentForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);		
	</security:authorize>	
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS -----------------------
	
									
		<c:if test="${SESS_ORGANISATION_ID ne null}">
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptAdd}">					
				var submitObject = new ObjSubmit("DepartmentForm.htm?ACTION=ADD", "", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("addNewDepartment", "click", getContentFromUrl, submitObject, true);	
			</security:authorize>
		</c:if>
	
	//----------------------------------------- DEPARTMENT ENTER EVENT ---------------------------------------
		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}, ${PERMISSION_CONSTANT.OM_DeptAdd}">	
		var submitObject = new ObjSubmitSaveDepartment("DepartmentForm.htm?ACTION=SAVE&org=${org}&orgId=${orgId}", "departmentForm", "MAIN_CONTENT", 'changePersonsForm_select2');
		var keyListener = new YAHOO.util.KeyListener("departmentForm_name", { keys:13 },  { fn:submitSaveDepartment, scope: submitObject, correctScope:true}, null ); 
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
	
	
</script>
