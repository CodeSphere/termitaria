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
<%@ include file="../../Taglibs.jsp" %>
 

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">			
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">			
					<a href="#" id="addNewOrganisation" title="<spring:message code="organization.submeniu.add.title"/>"><spring:message code='add'/> </a>
					<c:if test="${SESS_ORGANISATION_NAME ne null and SESS_ORGANISATION_ID ne null}">
						<a href="#" id="overview" title="<spring:message code="organization.submeniu.currentOrganization.title"/>"><spring:message code="organisation.button.currentOrganization"/></a>
					</c:if>					
				</security:authorize>
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->
<span class="section_title"><img src="images/titles/Organization.jpg"/><spring:message code="organisation.section.title"/></span>	

<table>
	<tr>
		<td>
			<form:form commandName="searchOrganisationBean" id="searchForm" name="searchForm" onsubmit="validateAndSubmitFormWithParams('OrganisationSearch.htm','searchForm','LIST_RESULTS_CONTENT','true'); return false;">
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<tr>
					<td class="labelTd">
						<spring:message code="organisation.name" />
					</td>
					<td>
						<form:input path="name" id="searchForm_name" maxlength="50" tabindex="1" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
					</td>
					<td class="labelTd">
						<spring:message code="organisation.address"/>
					</td>
					<td>
						<form:input path="address" tabindex="2" maxlength="100" cssClass="formFieldSearch" onkeypress="return restrictCharactersForAddress(event);"/>
					</td>
					<td class="labelTd">
						<spring:message code="pagination.resultsPerPage"/>
					</td>
					<td>
						<form:select path="resultsPerPage" tabindex="5">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>		
					<td class="labelTd">
						<spring:message code="organisation.select.type" />
					</td>
					<td>
						<div title='<spring:message code="organisation.choose.type"/>' id="organisationSearchTypesId">
							<form:select path="type"  tabindex="4" cssClass="formFieldSearch" onchange="changeTitle(this.options[selectedIndex].text, 'organisationSearchTypesId')">								
								<form:option value="-1"><spring:message code="organisation.choose.type"></spring:message></form:option>								
												
									<c:forEach var="type" items="${ORGANISATION_TYPE}">											
										<option title='<spring:message code="${type.label}"/>' value="${type.value}"><spring:message code="${type.label}"/></option>																
									</c:forEach>
									
							</form:select>		
						</div>
					</td>				
					<td class="labelTd">
						<spring:message code="organisation.email" />
					</td>					
					<td>
						<form:input path="email" id="searchForm_email" tabindex="3" maxlength="50" cssClass="formFieldSearch" />
					</td>																															
				</tr>
				
				<tr>
					<td colspan="5"></td>
					<td >	 		 
						<input type="button" class="button" id="searchOrganisation"  tabindex="6" onclick="validateAndSubmitFormWithParams('OrganisationSearch.htm', 'searchForm', 'LIST_RESULTS_CONTENT');" value="<spring:message code="search"/>" />
					</td>
				</tr>
			</table>
			</form:form>
			<br/>
		</td>
	</tr>
	
	<tr>
		<td>
			<%@ include file="../../Messages.jsp" %>
			<div id="LIST_RESULTS_CONTENT"></div>
		</td>
	</tr>
	
</table>

<c:choose>
	<c:when test="${DISPLAY_ORGANIZATION_DELETED_MESSAGE ne null}">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
			<script>	
				document.searchForm.name.focus();			
				submitForm('OrganisationSearch.htm?ia_lista_de_persoane&DISPLAY_ORGANIZATION_DELETED_MESSAGE=${DISPLAY_ORGANIZATION_DELETED_MESSAGE}','searchForm','LIST_RESULTS_CONTENT');
			</script>
		</security:authorize>
	</c:when>
	<c:otherwise>
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
			<script>
				document.searchForm.name.focus();
				submitForm('OrganisationSearch.htm?ia_lista_de_persoane','searchForm','LIST_RESULTS_CONTENT');
			</script>
		</security:authorize>
	</c:otherwise>
</c:choose>

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
	<script>						
		//---------------------------------------------------ADD NEW ORGANISATION---------------------------------------------------------	
		var submitObject = new ObjSubmit("OrganisationForm.htm?", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewOrganisation", "click", getContentFromUrl, submitObject, true);	

		<c:if test="${SESS_ORGANISATION_NAME ne null and SESS_ORGANISATION_ID ne null}">
			var getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("overview", "click", getContentFromUrl, getObject, true);
		</c:if>
		//--------------------------------------------------------------------------------------------------------------------------------
		
		//-----------------------------------------------------SEARCH ORGANISATION(NOT WORKING)--------------------------------------------------------
		//YAHOO.util.Event.addListener("searchOrganisation", "click", submitForm, {url : 'OrganisationSearch.htm', formId : 'searchForm', 
		//container : 'LIST_RESULTS_CONTENT', withContext : true}, true);
		//--------------------------------------------------------------------------------------------------------------------------------
		
	</script>
</security:authorize>
