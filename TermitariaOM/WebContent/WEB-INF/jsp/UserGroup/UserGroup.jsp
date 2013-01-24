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
	<c:when test="${userGroupBean.userGroupId > 0}">
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
				<a href="#" id="home"><spring:message code='home'/> </a>
				<a href="#" id="back"><spring:message code="back"/></a>
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_AddUserGroup}">
					<a href="#" id="addNewUserGroup" title="<spring:message code="usergroup.submeniu.add.title"/>"><spring:message code='add'/> </a>
				</security:authorize>				
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeleteUserGroup}">
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="usergroup.submeniu.delete.title"/>"><spring:message code='delete'/> </a>
				</security:authorize>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${userGroupBean.userGroupId > 0}">
		<span class="section_title"><img src="images/titles/UserGroupEdit.jpg"/><spring:message code="usergroup.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/UserGroupAdd.jpg"/><spring:message code="usergroup.section.title"/></span>	
	</c:otherwise>
</c:choose>

<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
	<br/>
</div>

	<form:form commandName="userGroupBean" name="userGroupForm" id="userGroupForm" onsubmit="return false;">
		<form:hidden path="status"/>
		<form:hidden path="userGroupId"/>
		
		<table cellpadding="3" class="tableAdd">
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>
			<tr>
				<td class="labelTd">				
					<spring:message code="usergroup.name"/><span id="required">*</span>
				</td>
				<td>																									
					<form:input id="userGroupForm_name" path="name" tabindex="1" maxlength="100" cssClass="formField required"  onblur="verifyUniqueness('UserGroupVerifyNameUniqueness.htm?198341='+ encodeURIComponent(this.value) +'&198342=${SESS_ORGANISATION_ID}', 'userGroupForm_name', 'userGroupForm_name_unique', this.value, '${SESS_ORGANISATION_ID}', '${userGroupBean.name}', '${userGroupBean.organisation.organisationId}' );" onkeypress="return restrictSpecialCharacters(event);"/>
					<div id="userGroupForm_nameDIV2" style="display: inline"></div> 
				</td>
			</tr>
			
			
			<tr>
				<td class="labelTd">
					<spring:message code="usergroup.description"/>
				</td>
				<td>
					<form:textarea path="description" id="userGroupForm_description" tabindex="2" cssClass="formField"  onkeypress="return restrictSpecialCharactersForObs(event);"/>
					<div class="remainingCharactersUserGroupDescription" style="display:none" >
						<div id="showRemainingCharactersUserGroupDescription"/>
					</div>				
				</td>
			</tr>																																		
			<tr><td colspan="4" height="20">&nbsp;</td></tr>
	 		<tr>
				<td class="labelTd"> 
					<spring:message code="usergroup.persons"/>
				</td>
				<td colspan="3">
					<div id="USER_GROUP_PERSONS">
						<table style="border: 1px solid #d7d7d7; width:500px; ">
							<tr><td style="line-height:10px;">&nbsp;</td></tr>
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
															
								<td id="allOrganizationPersons">
									<div class="selectTitle"><spring:message code="usergroup.select.all.persons"/></div>
									<br/>
									<select	id="changeUserGroupPersons_select1"	name="changeUserGroupPersons_select1" tabindex="3" multiple="multiple" size="10" style="width: 200px;">
										<c:forEach var="pers" items="${ALL_PERSONS}">
											<option title="${pers.firstName} ${pers.lastName}" value="${pers.personId}" id="${pers.personId}">${pers.firstName} ${pers.lastName}</option>
										</c:forEach>
									</select>
								</td>
								
								<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>									
									<div id="addPersons"  tabindex="4" class="addButton" title="<spring:message code="usergroup.change.persons.addPerson"/>"></div><br/><br/>
									<div id="removePersons" tabindex="5" class="removeButton"	title="<spring:message code="usergroup.change.persons.removePerson"/>"></div>																	
								</td>
								
								<td>
									<div class="selectTitle"><spring:message code="usergroup.select.current.persons"/></div>
									<br/>	
									<select id="changeUserGroupPersons_select2" name="persons" tabindex="6" multiple="multiple" size="10" style="width: 200px;" >
										<c:forEach var="pers" items="${userGroupBean.persons}">
											<option title="${pers.firstName} ${pers.lastName}" value="${pers.personId}" id="${pers.personId}">${pers.firstName} ${pers.lastName}</option>
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
					<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_AddUserGroup}, ${PERMISSION_CONSTANT.OM_UpdateUserGroup}">
						<input type="button" class="button" id="save" tabindex="7" value="<spring:message code="save"/>"/>						
						<input type="button" class="button" id="cancel" tabindex="8" value="<spring:message code="cancel"/>"/>						
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
	if(typeof(YAHOO) != "undefined") {	

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>

	document.userGroupForm.name.focus();		
		
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_AddUserGroup}, ${PERMISSION_CONSTANT.OM_UpdateUserGroup}">		
		
		//-------------------------------------- ADD PERSONS ----------------------------------------------------------	
		YAHOO.util.Event.addListener('addPersons', 'click', function addUserGroupPersons() {
		moveSelectOptionsSourceDest('changeUserGroupPersons_select1', 'changeUserGroupPersons_select2', true);
		}, null, false);
	
		//------------------------------------- REMOVE PERSONS --------------------------------------------------------
		YAHOO.util.Event.addListener('removePersons', 'click', function removeUserGroupPersons() {
		moveSelectOptionsSourceDest('changeUserGroupPersons_select2', 'changeUserGroupPersons_select1');
		}, null, false);	

		//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------
		var validateDescriptionField = new ObjFieldRemaining("userGroupForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersUserGroupDescription');
		YAHOO.util.Event.addListener("userGroupForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
		YAHOO.util.Event.addListener("userGroupForm_description", "blur", hideInfoOnBlurInField, validateDescriptionField, true);
		YAHOO.util.Event.addListener("userGroupForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);
						
		//------------------------------------- SUBMIT SAVE USER GROUP ------------------------------------------------
		var submitGroup = new ObjSubmitSaveUserGroup("UserGroup.htm", "userGroupForm", "MAIN_CONTENT", 'changeUserGroupPersons_select2', 'userGroupForm_name_unique', '${SESS_ORGANISATION_ID}');
		YAHOO.util.Event.addListener("save", "click", submitSaveUserGroup, submitGroup, true);
			
		//------------------------------------- USER GROUP ENTER EVENT ------------------------------------------------
		var keyListner = null; 
		var submitUserGroup = new ObjSubmitSaveUserGroup("UserGroup.htm", "userGroupForm", "MAIN_CONTENT", 'changeUserGroupPersons_select2', 'userGroupForm_name_unique', '${SESS_ORGANISATION_ID}');		

		if(document.getElementById('userGroupForm_name')) {
			keyListener = new YAHOO.util.KeyListener("userGroupForm_name", { keys:13 },  { fn:submitSaveUserGroup, scope: submitUserGroup, correctScope:true}, null ); 
			keyListener.enable();
		}
		
	</security:authorize>

	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_AddUserGroup}">	
		//------------------------------------- ADD GROUP BUTTON ------------------------------------------------------
		YAHOO.util.Event.addListener('addNewUserGroup', 'click', getContentFromUrl, {url: "UserGroup.htm", container: "MAIN_CONTENT"}, true);			
	</security:authorize>

	//------------ DELETE USER_GROUP ---------------------------------------------------------------
	<c:if test="${userGroupBean.userGroupId != -1}">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeleteUserGroup}">
			submitObject = new ObjSubmitWithConfirmation("SearchUserGroup.htm?ACTION=DELETE_FROM_USER_GROUP_FORM&userGroupId=${userGroupBean.userGroupId}", "", "MAIN_CONTENT", '<spring:message code="usergroup.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);			
		</security:authorize>		
	</c:if>

	//-------------------------------------- BACK BUTTON ----------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		getObject = new ObjSubmit("SearchUserGroup.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);	
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		getObject = new ObjSubmit("SearchUserGroup.htm?organisationId=${ORGANISATION_ID}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);	
	</security:authorize>

	//------------------------------------- CANCEL USER GROUP ------------------------------------------------		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, {url: "SearchUserGroup.htm", container: "MAIN_CONTENT"} , true); 
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, {url: "SearchUserGroup.htm?organisationId=${ORGANISATION_ID}",
		 container: "MAIN_CONTENT"} , true);
	</security:authorize>
	
	
	}
</script>
			
			

		
		
	
		

		
