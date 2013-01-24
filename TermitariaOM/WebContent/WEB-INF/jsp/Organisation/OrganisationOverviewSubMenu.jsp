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
<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
	<a href="#" id="home"><spring:message code="home" /></a>
	<a href="#" id="back"><spring:message code="back" /></a>		
</security:authorize>

<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgUpdate}">
	<a href="#" id="editOrganisation" title="<spring:message code="organization.submeniu.edit.title"/>"><spring:message code="edit" /></a>
</security:authorize>

<a href="#" id="organigram" title="<spring:message code="organization.submeniu.organigram.title"/>"><spring:message code="overview.organigram" /></a>

<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptAdd}">
	<a href="#" id="addDepartment" title="<spring:message code="organization.submeniu.addDepartment.title"/>"><spring:message code="overview.add.department" /></a>
</security:authorize>

<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}">
	<a href="#" id="addPerson" title="<spring:message code="organization.submeniu.addPerson.title"/>"><spring:message code="overview.add.person" /></a>
</security:authorize>

<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgAddCeo}">
	<a href="#" id="addCeo" title="<spring:message code="organization.submeniu.selectCEO.title"/>"><spring:message code="person.select.ceo"/></a>
</security:authorize>

<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgAddChild}">						
	<c:choose>
		<c:when test="${param.type == 2}">
			<a href="#" id="addCompany"><spring:message code="organisation.add.company"/></a>					
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${param.type == 3 || param.type == 4}">
					<a href="#" id="addBranch" title="<spring:message code="organization.submeniu.addChild.title"/>"><spring:message code="organisation.add.branch"/></a>								
				</c:when>
				<c:otherwise>		
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</security:authorize>

<c:if test="${HAS_ADMIN == '0'}">
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgCreateAdmin}">
		<a href="#" id="addAdmin" title="<spring:message code="organization.submeniu.addAdmin.title"/>"><spring:message code='organisation.admin'/> </a>
	</security:authorize>					
</c:if>

<script>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);		

		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);		
	</security:authorize>
			
		YAHOO.util.Event.addListener("organigram", "click", openOrganigram, { orgId : ${param.orgId}}, true);
	
		
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgUpdate}">
		getObject = new ObjSubmit("OrganisationForm.htm?ACTION=GET&organisationId=${param.orgId}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("editOrganisation", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_DeptAdd}">
		var submitObject = new ObjSubmit("DepartmentForm.htm?ACTION=ADDDEPARTMENTFROMORGANISATION&organisationId=${param.orgId}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addDepartment", "click", getContentFromUrl, submitObject, true);
	</security:authorize>
	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}">
		getObject = new ObjSubmit("PersonForm.htm?ACTION=NEWPERSONFROMORGANISATION&organisationId=${param.orgId}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addPerson", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgAddCeo}">
		getCeo = new ObjSubmit("OrganisationCeo.htm?organisationId=${param.orgId}", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addCeo", "click", getContentFromUrl, getCeo, true);
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgAddChild}">
		var submitObject = new ObjSubmit("OrganisationForm.htm?ACTION=ADDCOMPANY", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addCompany", "click", getContentFromUrl, submitObject, true);
	
		var submitObject = new ObjSubmit("OrganisationForm.htm?ACTION=ADDBRANCH", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addBranch", "click", getContentFromUrl, submitObject, true);	
	</security:authorize>		

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OrgCreateAdmin}">
		var submitObject = new ObjSubmit('CreateAdmin.htm?ACTION=NEWADMIN', "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addAdmin", "click", getContentFromUrl, submitObject, true);
	</security:authorize>	
	
</script>
