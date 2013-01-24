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
<%@include file="../Taglibs.jsp" %>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
	<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
		<tr>
		    <td id="submenuCell">
				<div id="submenu">			
					<a href="#" id="home"><spring:message code="home" /></a>
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">					
						<a href="#" id="back"><spring:message code="back"/></a>
					</security:authorize>												
					<a id="submenuNew" href="#" title="<spring:message code="ooo.submeniu.add.title"/>"><spring:message code="ooo.new"/></a>				
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeSearch}">
						<a id="submenuSearch" href="#" title="<spring:message code="ooo.submeniu.search.title"/>"><spring:message code="ooo.search"/></a>
					</security:authorize>					
				</div>
				<div id="submenu_footer"></div>
	        </td>    
	        <td id="contentCell">
	        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/OutOfOfficeAdd.jpg"/><spring:message code="ooo.profiles.message"/></span>
<span title="${NAME_TITLE}" class="userName"> ${FIRST_NAME}&nbsp;${LAST_NAME} </span>

<%@ include file="../Messages.jsp" %>

<c:choose>
	<c:when test="${!(empty OOO_PROFILES)}">
	
	<fieldset style="width: 890px;">
		<legend><spring:message code="ooo.title.listing" /></legend>
				<table class="list_results">
					<tr class="list_results_header">
						<th style="width:160px;"><spring:message code="ooo.replacement" /></th>
						<th style="width:160px;"><spring:message code="ooo.startDate" /></th>
						<th style="width:160px;"><spring:message code="ooo.endDate" /></th>
						<th style="width:300px;"><spring:message code="ooo.observation" /></th>						
						<th style="width:120px;"><spring:message code="ooo.actions" /></th>						
					</tr>
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					<c:forEach var="ooo" items="${OOO_PROFILES}">
						<c:set var="i" value="${i + 1}"/>
						<c:choose>
							<c:when test="${i % 2 == 0}">
								<c:set var="cssClass" value="even_row"/>
							</c:when>
							<c:otherwise>
								<c:set var="cssClass" value="odd_row"/>
							</c:otherwise>
						</c:choose>	
						<tr class="${cssClass}" onMouseOver="hover(this)" onMouseOut="changeToOldStyle(this)" id="${i}">
						
							<td>
								<a href="#" onClick="javascript:getContentFromUrlDirect('OutOfOffice.htm?action=EDIT_OOO_FROM_LIST_FOR_PERSON&oooId=${ooo.outOfOfficeId}','MAIN_CONTENT');" title="<spring:message code="ooo.edit"/>">
									${ooo.personReplacement.firstName}&nbsp;${ooo.personReplacement.lastName}
								</a>
							</td>
			 				<td><fmt:formatDate value="${ooo.startPeriod}" pattern="dd-MM-yyyy HH:mm"/></td>
							<td><fmt:formatDate value="${ooo.endPeriod}" pattern="dd-MM-yyyy HH:mm"/></td>
							<td>${ooo.observation}</td>
							<td>	
							<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
								<tr>												
									<td>										
										<a href="#" onClick="javascript:getContentFromUrlDirect('OutOfOffice.htm?action=EDIT_OOO_FROM_LIST_FOR_PERSON&oooId=${ooo.outOfOfficeId}','MAIN_CONTENT');" title="<spring:message code="ooo.edit"/>"><img src="images/buttons/action_edit.png"/></a>																				
									</td>
									<td>														
										
										<a href="#" onClick="deleteWithConfirmation('OutOfOfficeListForPerson.htm?action=delete&personId=${ooo.person.personId}&oooId=${ooo.outOfOfficeId}', '', 'MAIN_CONTENT', '<spring:message code="ooo.cancel.confirmationMessage"/>', '<spring:message code="confirm.cancel"/>');" title="<spring:message code="ooo.cancel"/>"> <img src="images/buttons/action_delete.png"/></a>										
									</td>									
								</tr>
							</table>		
							</td>
							
						</tr>						
					</c:forEach>
				</table>
		</fieldset>		
	</c:when>
	<c:otherwise>
		<spring:message code="ooo.no.results.for.user.message"/>
	</c:otherwise>
</c:choose>

	<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
				</div><!-- end CONTENT div -->
			</td>
			<td id="afterContentCell"></td>
		</tr>
	</table>
	<!-- ///////////////////////////////////////////////////////////////////////////// -->
	
<script language="text/javascript">
	//-----------------------------------------SUBMENU NEW OOO------------------------------------------------------------	
		YAHOO.util.Event.addListener("submenuNew", "click", getContentFromUrl, {url : 'OutOfOffice.htm?action=NEW_OOO_FROM_LIST_FOR_PERSON', container : 'MAIN_CONTENT',
	 	withContext : true}, true);	
	//--------------------------------------------------------------------------------------------------------------------

	//-----------------------------------------SUBMENU SEARCH OOO------------------------------------------------------------
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_OutOfOfficeSearch}">
		YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, {url : 'SearchOutOfOffice.htm', container : 'MAIN_CONTENT',
	 	withContext : true}, true);
	</security:authorize>
	//--------------------------------------------------------------------------------------------------------------------

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("SearchOutOfOffice.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
	</security:authorize>	
</script>
