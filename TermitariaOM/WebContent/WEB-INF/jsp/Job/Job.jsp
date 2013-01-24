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

<c:if test="${GET_FROM_PANEL eq null}">

<c:choose>
	<c:when test="${jobWebBean.jobId > 0}">
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
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobAdd}">
					<a href="#" id="addNewJob" title="<spring:message code="job.submeniu.add.title"/>"><spring:message code='add'/> </a>
				</security:authorize>
								
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobDelete}">						
					<a href="#" id="delete" class="${cssClass}" title="<spring:message code="job.submeniu.delete.title"/>"><spring:message code="delete" /></a>							
				</security:authorize>		
				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${jobWebBean.jobId > 0}">
		<span class="section_title"><img src="images/titles/JobEdit.jpg"/><spring:message code="job.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/JobAdd.jpg"/><spring:message code="job.section.title"/></span>		
	</c:otherwise>
</c:choose>

</c:if>

<div id="MESSAGES">
	<%@ include file="../Messages.jsp" %>
</div>

<c:if test="${GET_FROM_PANEL eq true and  ONSUBMIT eq true}">
	<form:form name="jobFormResult" id="jobFormResult" commandName="jobWebBean" onsubmit="return false;" >	
		<form:hidden id="JOBFORM_ID" path="jobId" />
		<form:hidden id="JOBFORM_NAME" path="name"/>
		<form:hidden path="jobId" />
		<form:hidden path="status" />
	</form:form>
</c:if>

<c:if test="${GET_FROM_PANEL eq null or ONSUBMIT eq null}">	
	<c:choose>
		<c:when test="${GET_FROM_PANEL eq true}">
			<c:set var="i" value="Panel"/>
		</c:when>
		<c:otherwise>
			<c:set var="i" value=""/>
		</c:otherwise>
	</c:choose>
	<form:form name="jobForm${i}" id="jobForm${i}" commandName="jobWebBean" onsubmit="return false;" >
	<form:hidden path="jobId" />
	<form:hidden path="status" />	
	<c:choose>
		<c:when test="${GET_FROM_PANEL eq null}">
			<table cellpadding="3" class="tableAdd">				
		</c:when>
		<c:otherwise>
			<table cellpadding="3" class="tableAdd">
		</c:otherwise>
	</c:choose>
		<tr><td class="tableAddSpacer">&nbsp;</td></tr>
		<tr>
			<td class="labelTd">
				<spring:message code="job.name"/><span id="required">*</span> 
			</td>
			<td>
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<form:input id="JOBFORM_NAME_PANEL" path="name" tabindex="1" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharacters(event);"/>
					</c:when>
					<c:otherwise>
						<form:input id="JOBFORM_NAME" path="name" tabindex="1" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharacters(event);"/>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="labelTd">
				<spring:message code="job.description"/>
			</td>
			<td>
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<form:textarea id="JOBFORM_DESCRIPTION_PANEL" path="description" tabindex="2" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
						<div class="remainingCharactersJobDescriptionPanel" style="display:none" >
							<div id="showRemainingCharactersDescriptionPanel"></div>
						</div>		
					</c:when>
					<c:otherwise>
						<form:textarea id="JOBFORM_DESCRIPTION" path="description" tabindex="2" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
						<div class="remainingCharactersJobDescription" style="display:none" >
							<div id="showRemainingCharactersDescription"></div>
						</div>		
					</c:otherwise>
				</c:choose>							
						
			</td>
		</tr>
		<tr>
			<td class="labelTd">
				<spring:message code="job.observation"/>
			</td>
			<td>
				<c:choose>
					<c:when test="${GET_FROM_PANEL eq true}">
						<form:textarea id="JOBFORM_OBSERVATION_PANEL" path="observation" tabindex="3" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>	
						<div class="remainingCharactersJobObsPanel" style="display:none" >
							<div id="showRemainingCharactersObservationPanel"></div>
						</div>
					</c:when>
					<c:otherwise>
						<form:textarea id="JOBFORM_OBSERVATION" path="observation"  tabindex="3" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
						<div class="remainingCharactersJobObs" style="display:none" >
							<div id="showRemainingCharactersObservation"></div>
						</div>
					</c:otherwise>
				</c:choose>	
			</td>
		</tr>
		<tr>
			<td class="formActions" colspan="2" align="right">
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobAdd}, ${PERMISSION_CONSTANT.OM_JobUpdate} ">
					<c:choose>
						<c:when test="${GET_FROM_PANEL eq true}">						
							<input type="button" class="button" id="saveJobPanel" tabindex="4" value="<spring:message code="job.save"/>"/>							
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${jobWebBean.jobId gt 0}">
									<input type="button" class="button" id="saveJob" tabindex="4" onclick="validateAndSubmitForm('Job.htm?action=update', 'jobForm', 'MAIN_CONTENT');" value="<spring:message code="job.save"/>"/>																
								</c:when>
								<c:otherwise>
									<input type="button" class="button" id="saveJob"  tabindex="4" onclick="validateAndSubmitForm('Job.htm?action=add', 'jobForm', 'MAIN_CONTENT');" value="<spring:message code="job.save"/>"/>				
								</c:otherwise>
							</c:choose>		
							<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobSearch}">
								<input type="button" class="button" id="cancelJob" tabindex="5" value="<spring:message code="cancel"/>"/>
							</security:authorize>							
						</c:otherwise>
					</c:choose>	
				</security:authorize>
			</td>
		</tr>
	</table>
		<br/>
	
	</form:form>

</c:if>


<c:if test="${GET_FROM_PANEL eq null}">

<!-- /////////////////////////////////SUBMENU END///////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
<!-- ///////////////////////////////////////////////////////////////////////////// -->

<script>

	document.jobForm${i}.name.focus();	
	
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
		var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	</security:authorize>
	
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
		YAHOO.util.Event.addListener("home", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
		container : "MAIN_CONTENT", withContext : true}, true);
	</security:authorize>
	
	getObject = new ObjSubmit("SearchJob.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);

	//------------ JOB DELETE ---------------------------------------------------------------
	<c:if test="${jobWebBean.jobId != -1}">
		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobDelete}">		
			submitObject = new ObjSubmitWithConfirmation("SearchJob.htm?ACTION=DELETE_FROM_JOB_FORM&jobId=${jobWebBean.jobId}", "", "MAIN_CONTENT", '<spring:message code="job.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
			YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);			
		</security:authorize>
	</c:if>
	
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobAdd}, ${PERMISSION_CONSTANT.OM_JobUpdate} ">
		
		<c:choose>
			<c:when test="${jobWebBean.jobId gt 0}">
				getObject = new ObjSubmit("Job.htm?action=update", "jobForm", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("saveJob", "click", validateAndSubmitForm, getObject, true);									
			</c:when>
			<c:otherwise>
				getObject = new ObjSubmit("Job.htm?action=add", "jobForm", "MAIN_CONTENT");
				YAHOO.util.Event.addListener("saveJob", "click", validateAndSubmitForm, getObject, true);				
			</c:otherwise>
		</c:choose>		

		//------------------------------------- CANCEL JOB ------------------------------------------------			
		<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobSearch}">						
			getObject = new ObjSubmit("SearchJob.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("cancelJob", "click", getContentFromUrl, getObject, true);	
		</security:authorize>			
	</security:authorize>

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_JobAdd}">
		var submitObject = new ObjSubmit("Job.htm?", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("addNewJob", "click", getContentFromUrl, submitObject, true);
	</security:authorize>	

	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
	var validateDescriptionField = new ObjFieldRemaining("JOBFORM_DESCRIPTION", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersDescription');
	YAHOO.util.Event.addListener("JOBFORM_DESCRIPTION", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("JOBFORM_DESCRIPTION", "blur", hideInfoOnBlurInField,validateDescriptionField, true);
	YAHOO.util.Event.addListener("JOBFORM_DESCRIPTION", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);
	
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
	var validateObservationField = new ObjFieldRemaining("JOBFORM_OBSERVATION", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("JOBFORM_OBSERVATION", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("JOBFORM_OBSERVATION", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("JOBFORM_OBSERVATION", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
	
	
	<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobAdd}, ${PERMISSION_CONSTANT.OM_JobUpdate} ">

	submitObject = new ObjSubmitJob('Job.htm?action=add&GET_FROM_PANEL=${GET_FROM_PANEL}', 'jobForm', "LOCAL_MESSAGES_CONTENT", this.handleChooseJobInDepartment);
	//adding the key listener
	var keyListener = new YAHOO.util.KeyListener("JOBFORM_NAME", { keys:13 },  { fn: submitAndValidateJobPanelForm, scope: submitObject, correctScope:true}, null ); 
	keyListener.enable();
		
	</security:authorize>
	
</script>

</c:if>

<c:if test="${GET_FROM_PANEL eq null or ONSUBMIT eq null}">
	<script>			
			<c:if test="${GET_FROM_PANEL eq true}">							
				document.jobForm${i}.name.focus();		
				<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobAdd}, ${PERMISSION_CONSTANT.OM_JobUpdate} ">
					submitObject = new ObjSubmitJob('Job.htm?action=add&GET_FROM_PANEL=${GET_FROM_PANEL}', 'jobFormPanel', "LOCAL_MESSAGES_CONTENT", this.handleChooseJobInDepartment);
					YAHOO.util.Event.addListener("saveJobPanel", "click", submitAndValidateJobPanelForm, submitObject, true);	
		
					//adding the key listener
					var keyListener = new YAHOO.util.KeyListener("JOBFORM_NAME_PANEL", { keys:13 },  { fn: submitAndValidateJobPanelForm, scope: submitObject, correctScope:true}, null ); 
					keyListener.enable();
				</security:authorize>
				//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
				var validateDescriptionField = new ObjFieldRemaining("JOBFORM_DESCRIPTION_PANEL", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersDescriptionPanel');
				YAHOO.util.Event.addListener("JOBFORM_DESCRIPTION_PANEL", "focus", showInfoOnClickInField, validateDescriptionField, true);
				YAHOO.util.Event.addListener("JOBFORM_DESCRIPTION_PANEL", "blur", hideInfoOnBlurInField,validateDescriptionField, true);
				YAHOO.util.Event.addListener("JOBFORM_DESCRIPTION_PANEL", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);
				
				//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
				var validateObservationField = new ObjFieldRemaining("JOBFORM_OBSERVATION_PANEL", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservationPanel');
				YAHOO.util.Event.addListener("JOBFORM_OBSERVATION_PANEL", "focus", showInfoOnClickInField, validateObservationField, true);
				YAHOO.util.Event.addListener("JOBFORM_OBSERVATION_PANEL", "blur", hideInfoOnBlurInField, validateObservationField, true);
				YAHOO.util.Event.addListener("JOBFORM_OBSERVATION_PANEL", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
				//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------		
			</c:if>
			
	</script>
</c:if>
