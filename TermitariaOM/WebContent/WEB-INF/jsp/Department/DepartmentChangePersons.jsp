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


<form method="post" id="changeDepartmentPersons">
<input type="hidden" name="departmentId" value="${departmentId}"/>
<table cellpadding="3" class="tableAdd">
	<tr><td colspan="4" height="20">&nbsp;</td></tr>
	<tr>
		<td>
			<spring:message code="department.list.persons"/>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table style="border: 1px solid #d7d7d7; width:500px; ">
				<tr><td style="line-height:10px;">&nbsp;</td></tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					
					<td>					
						<div class="selectTitle"><spring:message code="department.select.current.persons"/></div>
						<br/>
						<select id="changePersonsForm_select1" name="changePersonsForm_select1" multiple="multiple" size="10" style="width:200px">			
								<c:forEach var="person" items="${DEPARTMENT_PERSONS}">
									<option title="${person.firstName} ${person.lastName}" id="${person.personId}" value="${person.personId}">${person.firstName} ${person.lastName}</option>
								</c:forEach>				
						</select>
					</td>											

					<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
						<div id ="addButton" class="addButton" title="<spring:message code="department.addPerson"/>" ></div><br/><br/>
						<div id="removeButton" class="removeButton" title="<spring:message code="department.removePerson"/>" ></div>
					</td>
					
					<td id="allPersons">
						<div class="selectTitle"><spring:message code="department.select.all.persons"/></div>
						<br/>
						<select id="changePersonsForm_select2" name="changePersonsForm_select2" multiple="multiple" size="10" style="width:200px">
							<c:forEach var="person" items="${ALL_PERSONS}">
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
	<tr><td colspan="3">&nbsp;</td></tr>
	<tr>
		<td colspan="3"  class="labelTd">
			<div id="jobsContainer"> </div>
		</td>
	</tr>
	<tr>
		<td colspan="5" class="formActions" align="right">
			<input type="button" class="button" id="updatePersons" value="<spring:message code="update"/>"/>
			&nbsp;
			<input type="button" class="button" id="cancelUpdatePersons" value="<spring:message code="cancel"/>"/>
		</td>
	</tr>
</table>
</form>

<script>
	//The handler to deal with job selection for a person
	var handleChooseDepartmentPersonJob = new HandleChooseDepartmentPersonJob('jobsContainer', ${PERSONS_WITH_JOBS_ARRAY}, ${JOBS});
	handleChooseDepartmentPersonJob.displayDepartmentPersonsJobs();
	
	//-------------------------------------------------------------ADD PERSON-----------------------------------------
	YAHOO.util.Event.addListener('addButton', 'click', function addPerson() {
		moveSelectOptionsSourceDestPerson('changePersonsForm_select2', 'changePersonsForm_select1', null, handleChooseDepartmentPersonJob);
	}, null, false);

	//------------------------------------------------------------REMOVE PERSON---------------------------------------
	YAHOO.util.Event.addListener('removeButton', 'click', function removePerson() {
		moveSelectOptionsSourceDestPerson('changePersonsForm_select1', 'changePersonsForm_select2', true, handleChooseDepartmentPersonJob);
	}, null, false);
	
	//----------------------------------------------------------UPDATE PERSONS----------------------------------------
	YAHOO.util.Event.addListener('updatePersons', 'click', submitUpdateDepartmentPersons, { 
		url : 'ChangeDepartment.htm?ACTION=UPDATEPERSONS', formId : 'changeDepartmentPersons', container : 'MAIN_CONTENT',
		 changePersonsContainerId : 'SECONDARY_CONTENT', personsSelectId : 'changePersonsForm_select1'}
		 , true); 
	 	
		//--------------------------------------------------------CANCEL UPDATE PERSONS-------------------------------
	YAHOO.util.Event.addListener('cancelUpdatePersons', 'click', function cancelUpdatePersons() {
		document.getElementById('SECONDARY_CONTENT').innerHTML = '';} , null, false);
</script>	



