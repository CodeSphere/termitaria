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
				<!--  <a href="#" id="home" ><spring:message code="home" /></a>			
				<a href="#" id="costs" ><spring:message code="costsheet" /></a>	
				<a href="#" id="exchange" ><spring:message code="exchange" /></a>	
				<a href="#" id="addNewActivity" ><spring:message code="activity.add.section.title" /></a>																						
				-->
			</div>
			<div id="submenu_footer"></div>
        </td>
        
        <td id="contentCell">
        	<div id="CONTENT">
        	
<!-- //////////////////////////////////////////////////////////////////////////// -->
<span class="section_title"><img src="images/titles/Project.jpg"><spring:message code="project.section.title"/></span>

<table>
	<tr>
		<td>	
			<form:form id="searchForm" name="searchForm" commandName="searchProjectBean" onsubmit="validateAndSubmitFormWithParams('ProjectSearch.htm','searchForm','RESULTS', 'true'); return false;">			
			
			<table class="tableSearch">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				
				<!-- first line  -->
				<tr>
					<!-- PROJECT NAME -->
					<td class="labelTd">
						<spring:message code="project.name"/>
					</td>					
					<td>
						<form:input path="name" id="searchForm_name" maxlength="1000" tabindex="1" cssClass="formFieldSearch" onkeypress="return restrictSpecialCharacters(event);"/>
					</td>
										
					<!-- PROJECT MANAGER NAME -->
						
					<td class="labelTd">
						<spring:message code="project.manager.name"/>
					</td>					
					<td>
						<form:hidden path="managerId" id="managerId"/>							
						<div id="myManagerAutoComplete" class="autoComplete">
							<input type="text" id="myManagerInput" class="formFieldSearch" tabindex="2"/>
							<div id="myManagerContainer"></div>
						</div>
					</td>					
																															
					<!-- NR OF RESULTS PER PAGE -->
					<td class="labelTd">
						<spring:message code="project.results"/>
					</td>
					<td>
						<form:select path="resultsPerPage" tabindex="5">
							<c:forEach var="val" items="${RESULTS_PER_PAGE}">
								<form:option value="${val}" />
							</c:forEach>
						</form:select>
					</td>
				</tr>
						
				<!-- second line  -->
				<tr>					
					<!-- STATUS -->
					<td class="labelTd">
						<spring:message code="project.status" />
					</td>
					<td>
						<div title='<spring:message code="project.choose.status"/>' id="projectSearchStatusId">
							<form:select path="status"  tabindex="3" cssClass="formFieldSearch" onchange="changeTitle(this.options[selectedIndex].text, 'projectSearchStatusId')">								
								<form:option value="-1"><spring:message code="project.choose.status"></spring:message></form:option>																				
									<c:forEach var="status" items="${PROJECT_STATUS}">											
										<option title='<spring:message code="${status.label}"/>' value="${status.value}"><spring:message code="${status.label}"/></option>																
									</c:forEach>								
							</form:select>		
						</div>
					</td>							
						
					<!-- CLIENT -->		
					<td class="labelTd">
						<spring:message code="project.client"/>
					</td>					
					<td>
						<form:hidden path="clientId" id="clientId"/>							
						<div id="myClientAutoComplete" class="autoComplete">
							<input type="text" id="myClientInput" class="formFieldSearch" tabindex="4"/>
							<div id="myClientContainer"></div>
						</div>
					</td>			
										
				</tr>			
							
				<tr>					
					<td colspan="5"></td>
																						
					<td>												
						<input type="button" class="button" id="searchProject" tabindex="6" value="<spring:message code="search"/>"/>						
					</td>
				</tr>				
			</table>
			</form:form>
			<br/>
		</td>
	</tr>

	<tr>
		<td>
			<div id="MESSAGES">
				<%@ include file="../../Messages.jsp" %>	
			</div>
			<div id="RESULTS"></div>
		</td>
	</tr>
		
</table>
<script language="text/javascript">				

	//------------ HOME ---------------------------------------------------------------			
	//getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
	//YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);

	//------------ COSTS ---------------------------------------------------------------			
	getObject = new ObjSubmit("CostSheetSearch.htm?BACK_URL=ProjectSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("costs", "click", getContentFromUrl, getObject, true);

	//------------ EXCHANGE ---------------------------------------------------------------			
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		var submitObject = new ObjSubmit("ExchangeSearch.htm?BACK_URL=ProjectSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("exchange", "click", getContentFromUrl, submitObject, true);
	</security:authorize>		

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">	
			var submitObject = new ObjSubmit("ExchangeSearch.htm?BACK_URL=ProjectSearch.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("exchange", "click", getContentFromUrl, submitObject, true);
		</c:if>
	</security:authorize>

	//------------ ADD ACTIVITY ---------------------------------------------------------------			
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		var submitObject = new ObjSubmit("Activity.htm?ACTION=ADD&BACK_URL=ProjectSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewActivity", "click", getContentFromUrl, submitObject, true);
	</security:authorize>		

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ActivityAdvancedAddUpdate}">	
		<c:if test="${IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT eq true}">	
			var submitObject = new ObjSubmit("Activity.htm?ACTION=ADD&BACK_URL=ProjectSearch.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("addNewActivity", "click", getContentFromUrl, submitObject, true);
		</c:if>
	</security:authorize>	

	document.searchForm.name.focus();

	submitForm('ProjectSearch.htm','searchForm','RESULTS');
				
	//----------------------------------------AUTOCOMPLETE MANAGER-------------------------------					
	instantiateManagerAutoComplete(${JSON_PERSONS}, 'myManagerInput', 'myManagerContainer', 'managerId');		
	//----------------------------------------AUTOCOMPLETE CLIENT-------------------------------
					
	instantiateClientAutoComplete(new YAHOO.util.LocalDataSource(${JSON_CLIENTS}), 'myClientInput', 'myClientContainer', 'clientId');	

	//----------------------------------------SEARCH PROJECT-------------------------------
	YAHOO.util.Event.addListener("searchProject", "click", submitProjectForm , {url : 'ProjectSearch.htm', formId : 'searchForm', 
	container : 'RESULTS', withContext : true, managerNameHiddenFieldId : 'managerId', managerInput: 'myManagerInput', clientNameHiddenFieldId : 'clientId', clientInput: 'myClientInput', isManager: false}, true);	
		
</script>


<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->
