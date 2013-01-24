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
	<c:when test="${projectProjectTeamBean.project.projectId > 0 && projectProjectTeamBean.project.status == 1}">
		<c:set var="cssClass" value=""/>		
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>		
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${projectProjectTeamBean.project.projectId > 0}">
		<c:set var="cssDeleteClass" value=""/>
		<c:set var="tableWidth" value="420px"/>
	</c:when>
	<c:otherwise>
		<c:set var="cssDeleteClass" value="inactive"/>	
		<c:set var="tableWidth" value="350px"/>	
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${projectProjectTeamBean.project.status == 1}">
		<c:set var="readOnly" value="false"/>
	</c:when>
	<c:otherwise>
		<c:set var="readOnly" value="true"/>		
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${projectProjectTeamBean.project.status == 1}">
		<c:set var="disabled" value="false"/>
	</c:when>
	<c:otherwise>
		<c:set var="disabled" value="true"/>		
	</c:otherwise>
</c:choose>

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">				
				<a href="#" id="home" ><spring:message code="home" /></a>			
				<a href="#" id="back" onclick="ENTITY = getProjectData();check_add('${BACK_URL}', 'MAIN_CONTENT');">
				<spring:message code="back"/>
				</a>
				
				<a href="#" id="addNewProject" title="<spring:message code="project.submeniu.add.title"/>"><spring:message code='add'/></a>			
											
				<a href="#" id="delete" class="${cssDeleteClass}" title="<spring:message code="project.submeniu.delete.title"/>"><spring:message code="delete"/></a>		
				<a href="#" id="submenuSearch" title="<spring:message code="project.submeniu.search.title"/>"><spring:message code="search"/></a>	
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<c:choose>
	<c:when test="${projectProjectTeamBean.project.projectId > 0}">
		<span class="section_title"><img src="images/titles/ProjectEdit.jpg"><spring:message code="project.update.section.title"/></span>
	</c:when>
	<c:otherwise>
		<span class="section_title"><img src="images/titles/ProjectAdd.jpg"><spring:message code="project.add.section.title"/></span>
	</c:otherwise>
</c:choose>


<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include file="../Messages.jsp" %>
</div>

<form:form method="post" commandName="projectProjectTeamBean" id="projectForm" name="projectForm" onsubmit="return false;">	
	<form:hidden path="project.projectId" id="projectId" />		
	
	<spring:bind path="project.organizationId">
		<input type="hidden" name="project.organizationId" value="${ORGANIZATION_ID}" />
	</spring:bind>	
	
	<spring:bind path="project.status">
		<input type="hidden" name="project.status" value="${STATUS}" />
	</spring:bind>	
							
	<form:hidden path="projectTeam.projectTeamId" id="projectTeamId" />			
	
	<spring:bind path="projectTeam.status">
		<input type="hidden" name="projectTeam.status" value="${STATUS_PROJECT_TEAM}" />	
	</spring:bind>	
															
	<table class="tableAdd">
		<tr>
			<td width="450px">
			<table cellpadding="5">
				<tr><td class="tableAddSpacer" colspan="2">&nbsp;</td></tr>		
								
				<tr>
					<!-- ====================================CLIENT CHECK BOX=================================== -->
					<td class="labelTd">
						<spring:message code="project.no.client"/>
					</td>
						<td>							
							<c:choose>
								<c:when test="${projectProjectTeamBean.project.status == 1}">	
									<form:checkbox path="project.projectNoClient" tabindex="1" id="projectForm_projectNoClient" onclick="manageClientInputDisplay(this, 'hasClient', null, ${JSON_CLIENTS}, ${projectProjectTeamBean.project.status})"/>
								</c:when>	
								<c:otherwise>									
									<input id="projectForm_projectNoClient" type="checkbox" name="project.projectNoClient" DISABLED/>									
								</c:otherwise>
							</c:choose>			
						</td>
					<td>
					</td>
				</tr>
						
				<!-- ====================================CLIENT========================================================= -->
				<tr>
					<td class="labelTd hasClient">
						<spring:message code="project.client"/> <span id="clientRequired" class="requiredIndicator">*</span>					
					</td>			
					<td class="hasClient">						
						<form:hidden path="project.clientId" id="clientId"/>							
						<c:choose>
							<c:when test="${projectProjectTeamBean.project.status != 1}">	
								<input id="projectForm_client" type="text" value="${CLIENT_NAME}" tabindex="2" class="formField required" readonly="readonly"/>	
							</c:when>
							<c:otherwise>
								<div id="myClientAutoComplete" class="formField autoComplete">														
									<c:choose>							
										<c:when test="${CLIENT_NAME != null}">							
											<input id="projectForm_client" type="text" value="${CLIENT_NAME}" tabindex="2" class="formField required" />									
										</c:when>		
										<c:otherwise>
											<input id="projectForm_client" type="text" tabindex="2" class="formField required" />									
										</c:otherwise>
									</c:choose>																						
									<div id="autoCompleteClientContainer"></div>						
								</div>								
							</c:otherwise>
						</c:choose>
						<div id="client_message"></div>																								
					</td>
				</tr>
																																															
				<!-- =========================================NAME======================================================== -->
				<tr>
					<td class="labelTd">
						<spring:message code="project.name"/> <span id="required">*</span>
					</td>
					<td>
						<form:input id="projectForm_name"  path="project.name" tabindex="3" readonly="${readOnly}" cssClass="formField required" maxlength="100" onkeypress="return restrictSpecialCharacters(event);"/>
						<div id="name_message"></div>
					</td>
				</tr>		
				
				<!-- ====================================MANANGER========================================================= -->
				<tr>
					<td class="labelTd">
						<spring:message code="project.manager.name"/> <span id="required">*</span>
					</td>			
					<td>
						<form:hidden path="project.managerId" id="managerId"/>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedAdd}">						
							<c:choose>
								<c:when test="${projectProjectTeamBean.project.status != 1}">	
									<input id="projectForm_manager" type="text" value="${MANAGER_NAME}"  tabindex="4"class="formField required"  readonly="readonly"/>
									<div id="manager_message"></div>
								</c:when>
								<c:otherwise>
									<div id="myAutoComplete" class="formField autoComplete">								
										<c:choose>
											<c:when test="${MANAGER_NAME != null}">
												<input id="projectForm_manager" type="text" value="${MANAGER_NAME}" tabindex="4" class="formField required""/>
											</c:when>		
											<c:otherwise>
												<input id="projectForm_manager" type="text" tabindex="4" class="formField required""/>
											</c:otherwise>
										</c:choose>																						
										<div id="autoCompleteContainer"></div>						
									</div>						
									<div id="manager_message"></div>
								</c:otherwise>
							</c:choose>					
						</security:authorize>
						<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedAdd}">
							<input id="projectForm_manager" type="text" value="${MANAGER_NAME}"  tabindex="4"class="formField required"  readonly="readonly"/>
							<div id="manager_message"></div>
						</security:authorize>
					</td>
				</tr>
				
				<!-- ====================================DESCRIPTION====================================================== -->
				<tr>
					<td class="labelTd">
						<spring:message code="project.description"/>
					</td>
					<td>					
						<form:textarea path="project.description" id="projectForm_description" tabindex="5" readonly="${readOnly}" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
						<div class="remainingCharactersProjectDescription" style="display:none" >
							<div id="showRemainingCharactersDescription" />						
						</div> 
					</td>
				</tr>
						
			 	<!-- ====================================OBSERVATION====================================================== -->
				<tr>
					<td class="labelTd">
						<spring:message code="project.observation"/>
					</td>
					<td>
						<form:textarea path="project.observation" id="projectForm_observation" tabindex="6" readonly="${readOnly}" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
						<div class="remainingCharactersProjectObs" style="display:none" >
							<div id="showRemainingCharactersObservation" />			
						</div>				
					</td>
				</tr>
		
				<tr>
					<td>				
					</td>
				</tr>
		
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedAdd}">
					<c:if test="${projectProjectTeamBean.project.projectId > 0}">
						<c:choose>
							<c:when test="${projectProjectTeamBean.project.status == 1}">
								<!-- ====================================FINISH PROJECT====================================================== -->
								<tr>   
						        	<td class="labelTd">
										<spring:message code="project.finish"/>				
									</td>
									<td>
										<input type="button" class="buttonStatus" tabindex="6" id="finishProject" title="<spring:message code="project.finish.button"/>" value="<spring:message code="project.finish.button"/>" />
									</td>
								</tr>		
								
								<!-- ====================================ABORT PROJECT====================================================== -->
								<tr>   
						        	<td class="labelTd">
										<spring:message code="project.abort"/>				
									</td>
									<td>
										<input type="button" class="buttonStatus" tabindex="7" id="abortProject" title="<spring:message code="project.abort.button"/>" value="<spring:message code="project.abort.button"/>" />
									</td>
								</tr>					
							</c:when>
							<c:otherwise>
								<!-- ====================================ACTIVATE PROJECT====================================================== -->
								<tr>   
						        	<td class="labelTd">
										<spring:message code="project.activate"/>				
									</td>
									<td>
										<input type="button" class="buttonStatus" tabindex="6" id="activateProject" title="<spring:message code="project.activate.button"/>" value="<spring:message code="project.activate.button"/>" />
									</td>
								</tr>
						
							</c:otherwise>
						</c:choose>		
					</c:if>			
				</security:authorize>
		
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedAdd}">
					<c:if test="${projectProjectTeamBean.project.projectId > 0}">
						<c:choose>
							<c:when test="${USER_ID eq projectProjectTeamBean.project.managerId}">
								<c:choose>
									<c:when test="${projectProjectTeamBean.project.status == 1}">
										<!-- ====================================FINISH PROJECT====================================================== -->
										<tr>   
								        	<td class="labelTd">
												<spring:message code="project.finish"/>				
											</td>
											<td>
												<input type="button" class="buttonStatus" tabindex="7" id="finishProject" title="<spring:message code="project.finish.button"/>" value="<spring:message code="project.finish.button"/>" />
											</td>
										</tr>		
										
										<!-- ====================================ABORT PROJECT====================================================== -->
										<tr>   
								        	<td class="labelTd">
												<spring:message code="project.abort"/>				
											</td>
											<td>
												<input type="button" class="buttonStatus" tabindex="8" id="abortProject" title="<spring:message code="project.abort.button"/>" value="<spring:message code="project.abort.button"/>" />
											</td>
										</tr>
									</c:when>
									<c:otherwise>
										<!-- ====================================ACTIVATE PROJECT====================================================== -->
										<tr>   
								        	<td class="labelTd">
												<spring:message code="project.activate"/>				
											</td>
											<td>
												<input type="button" class="buttonStatus" tabindex="9" id="activateProject" title="<spring:message code="project.activate.button"/>" value="<spring:message code="project.activate.button"/>" />
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</c:when>						
						</c:choose>
					</c:if>
				</security:authorize>													
				</table>
			</td>	
							
			<td width="430px">
				<table>
					<tr>
						<td class="tableAddSpacer">&nbsp;</td>
					</tr>
				</table>
				
				<fieldset>
					<legend>
						<spring:message code="project.team" />
					</legend>
					<table cellpadding="3">											
						<tr>
							<td>
								<div id="PROJECT_TEAM">									
									
										<table cellpadding="3">					
											<!-- ====================================TEAM MEMBERS====================================================== -->       											
									         <tr>
									             <td class="labelTd">
									                 <spring:message code="project.team.list.persons"/>
									             </td>
									             <td colspan="3">
									                 <table style="border: 1px solid #d7d7d7; width:500px; ">
									                  	<tr><td style="line-height:10px;">&nbsp;</td></tr>                   	                              
									                     <tr>                                              
									                         <td>
										                         <table> 
											                         <tr>
												                         <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
												                         <td id="allPersons">
												                             <div class="selectTitle"><spring:message code="project.team.select.all.persons"/></div>
												                             <br/>
												                             <select id="changePersonsForm_select1" name="changePersonsForm_select1" multiple="multiple" size="10" tabindex="10" style="width:200px">
												                                 <c:forEach var="person" items="${ALL_PERSONS}">
												                                     <option title="${person.firstName} ${person.lastName}" id="${person.userId}" value="${person.userId}">${person.firstName} ${person.lastName}</option>
												                                 </c:forEach>
												                             </select>
												                         </td>                           
												                        
												                         <td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
												                             <a href="#" id="addPersonId" tabindex="11" ><div class="addButton" title="<spring:message code="project.team.addPerson"/>"></div></a><br/><br/>
												                             <a href="#" id="removePersonId" tabindex="12" ><div class="removeButton" title="<spring:message code="project.team.removePerson" />" ></div></a>
												                         </td>
												                        
												                         <td>
												                             <div class="selectTitle"><spring:message code="project.team.select.current.persons"/></div>
												                             <br/>
												                             <select id="changePersonsForm_select2" name="projectTeam.persons" multiple="multiple" size="10" tabindex="13" style="width:200px">
												                                 <c:forEach var="person" items="${projectProjectTeamBean.projectTeam.persons}">
												                                 	 <c:if test="${person ne null}">                                 
												                                     	<option title="${person.firstName} ${person.lastName}" id="${person.personId}" value="${person.personId}">${person.firstName} ${person.lastName}</option>
												                                     </c:if>
												                                 </c:forEach>
												                             </select>
												                         </td>
												                                                                         
												                         <td width="100%">&nbsp;</td>
											                         </tr>
										                         </table> 
									                         </td>                                                                                                              
									                     </tr>                                                    	
									                     	 <!-- =========================================EXTERNAL PERSON======================================================== -->                     	                   	                   
									                     <tr>   
									                     	<td>
									                         	<table>  
									                         	 <tr>
										                         	 <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
										                         	 <td>
																		<spring:message code="project.team.external.person"/>																		
																		<input type="button" name="externalPerson" id="externalPerson" class="button" tabindex="14" title="<spring:message code="person.add"/>" value="<spring:message code="person.add"/>" onClick="manageTeamMember('TeamMember.htm?ACTION=ADD&projectId=${projectProjectTeamBean.project.projectId}', '<spring:message code="person.add"/>', 0);"/>
																		<div id="TEAM_MEMBERS">		
												
																		</div>
																	</td>
																</tr>
																</table>
															</td>
														</tr>	 
															 																			 
									                   <tr><td style="line-height:10px;">&nbsp;</td></tr>    
									                                   
									                 </table>
									             </td>               
									         </tr> 
									         
									         <!-- ====================================DESCRIPTION====================================================== -->
											<tr>
												<td class="labelTd">
													<spring:message code="project.team.description"/>
												</td>
												<td>					
													<form:textarea path="projectTeam.description" id="projectTeamForm_description" readonly="${readOnly}" tabindex="15" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
													<div class="remainingCharactersProjectTeamDescription" style="display:none" >
														<div id="showRemainingCharactersProjectTeamDescription" />						
													</div> 
												</td>
											</tr>
															
										 	<!-- ====================================OBSERVATION====================================================== -->
											<tr>
												<td class="labelTd">
													<spring:message code="project.team.observation"/>
												</td>
												<td>
													<form:textarea path="projectTeam.observation" id="projectTeamForm_observation" readonly="${readOnly}" tabindex="16" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
													<div class="remainingCharactersProjectTeamObs" style="display:none" >
														<div id="showRemainingCharactersProjectTeamObservation" />			
													</div>				
												</td>
											</tr>
									                      						                 	   		
										</table>								
								</div>								
							</td>
						</tr>																																												
					</table>
				</fieldset>
			</td>	
	</tr>
	<tr>
		<td colspan="2" class="formActions" align="right">				
			<input type="button" class="button" id="save" tabindex="17" value="<spring:message code="save"/>" />				
				<input type="button" class="button" id="cancel" onclick="ENTITY = getProjectData();check_add('${BACK_URL}', 'MAIN_CONTENT');" tabindex="18" value="<spring:message code="cancel"/>" />				
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

	document.projectForm.projectForm_projectNoClient.focus();

	//------------ HOME ---------------------------------------------------------------		
	getObject = new ObjSubmit("ClientSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("home", "click", getContentFromUrl, getObject, true);
	
	//------------ SAVE PROJECT ---------------------------------------------------------------			
	var submitObject = new ObjSubmitSaveProject("Project.htm?ACTION=SAVE&BACK_URL=${ENCODE_BACK_URL}&projectId=${projectId}", "projectForm", "MAIN_CONTENT", "changePersonsForm_select2");	
	YAHOO.util.Event.addListener("save", "click", submitSaveProject, submitObject, true);			

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedAdd}">
		//------------ AUTOCOMPLETE MANAGER---------------------------------------------------------------
		<c:if test="${projectProjectTeamBean.project.status == 1}">			
			instantiateManagerAutoComplete(${JSON_PERSONS}, 'projectForm_manager', 'autoCompleteContainer', 'managerId');			
		</c:if>
	</security:authorize>
	
	//------------ AUTOCOMPLETE CLIENT---------------------------------------------------------------	
	<c:if test="${projectProjectTeamBean.project.status == 1}">					
		instantiateClientAutoComplete(new YAHOO.util.LocalDataSource(${JSON_CLIENTS}), 'projectForm_client', 'autoCompleteClientContainer', 'clientId', null);					
	</c:if>	

	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedAdd}">
		<c:if test="${projectProjectTeamBean.project.projectId > 0}">
			<c:choose>
				<c:when test="${projectProjectTeamBean.project.status == 1}">
					//------------FINISH PROJECT---------------------------------------------------------------		
					submitObject = new ObjSubmitWithConfirmation("ProjectSearch.htm?ACTION=FINISH_PROJECT&projectId=${projectProjectTeamBean.project.projectId}", "", "MAIN_CONTENT", '<spring:message code="project.finish.confirmationMessage"/>', '<spring:message code="project.finish"/>');
					YAHOO.util.Event.addListener("finishProject", "click", deleteWithConfirmation, submitObject, true);
	
					//------------ABORT PROJECT---------------------------------------------------------------	
					submitObject = new ObjSubmitWithConfirmation("ProjectSearch.htm?ACTION=ABORT_PROJECT&projectId=${projectProjectTeamBean.project.projectId}", "", "MAIN_CONTENT", '<spring:message code="project.abort.confirmationMessage"/>', '<spring:message code="project.abort"/>');
					YAHOO.util.Event.addListener("abortProject", "click", deleteWithConfirmation, submitObject, true);		
				</c:when>
				<c:otherwise>
					//------------ACTIVATE PROJECT---------------------------------------------------------------	
					submitObject = new ObjSubmitWithConfirmation("ProjectSearch.htm?ACTION=ACTIVATE_PROJECT&projectId=${projectProjectTeamBean.project.projectId}", "", "MAIN_CONTENT", '<spring:message code="project.activate.confirmationMessage"/>', '<spring:message code="project.activate"/>');
					YAHOO.util.Event.addListener("activateProject", "click", deleteWithConfirmation, submitObject, true);		
				</c:otherwise>				
			</c:choose>
		</c:if>
	</security:authorize>

	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.CM_ProjectAdvancedAdd}">
		<c:if test="${projectProjectTeamBean.project.projectId > 0}">
			<c:choose>
				<c:when test="${USER_ID eq projectProjectTeamBean.project.managerId}">
					<c:choose>
						<c:when test="${projectProjectTeamBean.project.status == 1}">
							//------------FINISH PROJECT---------------------------------------------------------------		
							submitObject = new ObjSubmitWithConfirmation("ProjectSearch.htm?ACTION=FINISH_PROJECT&projectId=${projectProjectTeamBean.project.projectId}", "", "MAIN_CONTENT", '<spring:message code="project.finish.confirmationMessage"/>', '<spring:message code="project.finish"/>');
							YAHOO.util.Event.addListener("finishProject", "click", deleteWithConfirmation, submitObject, true);
			
							//------------ABORT PROJECT---------------------------------------------------------------	
							submitObject = new ObjSubmitWithConfirmation("ProjectSearch.htm?ACTION=ABORT_PROJECT&projectId=${projectProjectTeamBean.project.projectId}", "", "MAIN_CONTENT", '<spring:message code="project.abort.confirmationMessage"/>', '<spring:message code="project.abort"/>');
							YAHOO.util.Event.addListener("abortProject", "click", deleteWithConfirmation, submitObject, true);
						</c:when>
						<c:otherwise>
							//------------ACTIVATE PROJECT---------------------------------------------------------------	
							submitObject = new ObjSubmitWithConfirmation("ProjectSearch.htm?ACTION=ACTIVATE_PROJECT&projectId=${projectProjectTeamBean.project.projectId}", "", "MAIN_CONTENT", '<spring:message code="project.activate.confirmationMessage"/>', '<spring:message code="project.activate"/>');
							YAHOO.util.Event.addListener("activateProject", "click", deleteWithConfirmation, submitObject, true);		
						</c:otherwise>			
					</c:choose>				
				</c:when>						
			</c:choose>
		</c:if>
	</security:authorize>
		
	//---------------------------------------- DELETE PROJECT ---------------------------------------------------------------
	<c:if test="${projectProjectTeamBean.project.projectId > 0}">		
		submitObject = new ObjSubmitWithConfirmation("ProjectSearch.htm?ACTION=DELETE_FROM_PROJECT_FORM&projectId=${projectProjectTeamBean.project.projectId}", "", "MAIN_CONTENT", '<spring:message code="project.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
		YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);				
	</c:if>	

	//----------------------------------------ADD A NEW PROJECT-------------------------------	
		
	var submitObject = new ObjSubmit("Project.htm?ACTION=ADD&BACK_URL=${NEXT_BACK_URL}", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("addNewProject", "click", getContentFromUrl, submitObject, true);	
		
	//------------ PROJECT SEARCH ---------------------------------------------------------------		
	var getObject = new ObjSubmit("ProjectSearch.htm", "", "MAIN_CONTENT");
	YAHOO.util.Event.addListener("submenuSearch", "click", getContentFromUrl, getObject, true);	
	
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
	var validateDescriptionField = new ObjFieldRemaining("projectForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersDescription');
	YAHOO.util.Event.addListener("projectForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("projectForm_description", "blur", hideInfoOnBlurInField,validateDescriptionField, true);
	YAHOO.util.Event.addListener("projectForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);
	
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
	var validateObservationField = new ObjFieldRemaining("projectForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersObservation');
	YAHOO.util.Event.addListener("projectForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("projectForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("projectForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);
	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
		
	//----------------------------------------- PROJECT ENTER EVENT ---------------------------------------
	var submitObject = new ObjSubmitSaveProject("Project.htm?ACTION=SAVE", "projectForm", "MAIN_CONTENT");
	var keyListener = new YAHOO.util.KeyListener("projectForm_name", { keys:13 },  {fn: submitSaveProject, scope: submitObject, correctScope:true}, null ); 
	keyListener.enable();		
	
	OLD_ENTITY = getProjectData();

	//---------------------------------- LIST THE TEAM MEMBERS FOR THE PROJECT TEAM ----------------------
	// team members listing
	<c:if test="${projectProjectTeamBean.projectTeam.projectTeamId > 0}">
		getContentFromUrlDirect('TeamMemberListing.htm?ACTION=GET&projectTeamId=${projectProjectTeamBean.projectTeam.projectTeamId}&projectId=${projectProjectTeamBean.project.projectId}', 'TEAM_MEMBERS');
	</c:if>	

	<c:if test="${projectProjectTeamBean.project.status == 1}">
		// ----------------------------------------ADD PERSONS INLINE-------------------------------
		YAHOO.util.Event.addListener("addPersonId", "click", function addPerson() {
			moveSelectOptionsSourceDest('changePersonsForm_select1', 'changePersonsForm_select2', ${PROJECT_TEAM_MANAGERID}, false);sortSelect(document.getElementById("changePersonsForm_select2"));}, null, false);
		
		// ----------------------------------------REMOVE PERSONS INLINE-------------------------------
		YAHOO.util.Event.addListener("removePersonId", "click", function removePerson() {
			moveSelectOptionsSourceDest('changePersonsForm_select2', 'changePersonsForm_select1', ${PROJECT_TEAM_MANAGERID}, true);sortSelect(document.getElementById("changePersonsForm_select1"));}, null, false);	
	</c:if>
	
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR DESCRIPTION -----------------------	
	var validateDescriptionField = new ObjFieldRemaining("projectTeamForm_description", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersProjectTeamDescription');
	YAHOO.util.Event.addListener("projectTeamForm_description", "focus", showInfoOnClickInField, validateDescriptionField, true);
	YAHOO.util.Event.addListener("projectTeamForm_description", "blur", hideInfoOnBlurInField,validateDescriptionField, true);
	YAHOO.util.Event.addListener("projectTeamForm_description", "keyup", showInfoOnKeyUpInField, validateDescriptionField, true);
	
	//------------------------------------- OPERATIONS ON SEPARATED FIELDS FOR OBSERVATION -----------------------
	var validateObservationField = new ObjFieldRemaining("projectTeamForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersProjectTeamObservation');
	YAHOO.util.Event.addListener("projectTeamForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
	YAHOO.util.Event.addListener("projectTeamForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
	YAHOO.util.Event.addListener("projectTeamForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);

	//---------------------------------- END OPERATIONS ON SEPARATED FIELDS ---------------------------------------
	<c:if test="${projectProjectTeamBean.project.status != 1}">
		document.projectForm.externalPerson.disabled=true;		
	</c:if>

	//-------------------- FIRST TIME CHECK/UNCHECK CLIENT------
	
	<c:choose>
		<c:when test="${projectProjectTeamBean.project.projectId > 0}">
			<c:choose>
				<c:when test="${projectProjectTeamBean.project.projectNoClient}">					
					manageClientInputDisplay(document.getElementById('projectForm_projectNoClient'), 'hasClient', true, ${JSON_CLIENTS}, ${projectProjectTeamBean.project.status});
				</c:when>
				<c:otherwise>
					manageClientInputDisplay(document.getElementById('projectForm_projectNoClient'), 'hasClient', false, ${JSON_CLIENTS}, ${projectProjectTeamBean.project.status});
				</c:otherwise>
			</c:choose>			
		</c:when>
		<c:otherwise>		
			manageClientInputDisplay(document.getElementById('projectForm_projectNoClient'), 'hasClient', false, ${JSON_CLIENTS}, ${projectProjectTeamBean.project.status});			
		</c:otherwise>
	</c:choose>	

	JSON_STR = "";
	TEAM_MEMBER = 0;
	DELETED_MEMBER_ID = 0;	
	ID_JSON_EXTERNAL_MEMEBER = null;
	
	sortSelect(document.getElementById("changePersonsForm_select1"));	
	sortSelect(document.getElementById("changePersonsForm_select2"));	

</script>
