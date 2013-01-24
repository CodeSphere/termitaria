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
<%@ include file="../Taglibs.jsp"%>

<c:choose>
	<c:when test="${personBean.personId > 0}">
		<c:set var="cssClass" value=""/>		
	</c:when>
	<c:otherwise>
		<c:set var="cssClass" value="inactive"/>			
	</c:otherwise>
</c:choose>


<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->
<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0" onsubmit="return false;">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">		
				<a href="#" id="home"><spring:message code="home" /></a>
				<c:if test="${SESS_ORGANISATION_ID != '-1'}">
					<c:if test="${MY_PROFILE eq null}">
						<a href="#" id="back"><spring:message code="back"/></a>
					</c:if>
				</c:if>
				<c:choose>  
					<c:when test="${IS_CREATE_ADMIN == 'false'}">
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}"> 							
							<a href="#" id="addNewPerson" title="<spring:message code="person.submeniu.add.title"/>"><spring:message code='add'/></a>								
						</security:authorize>
						
						<c:if test="${MY_PROFILE eq null}">							
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonDelete}">
								<a href="#" id="delete" class="${cssClass}" title="<spring:message code="person.submeniu.delete.title"/>"><spring:message code="delete"/></a>
							</security:authorize>							
						</c:if>
												
						<a href="#" id="changePassword" class="${cssClass}" title="<spring:message code="person.submeniu.changePassword.title"/>"><spring:message code="person.change.password" /></a>
						
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ResetPassword}">
							<a href="#" id="resetPassword" class="${cssClass}" title="<spring:message code="person.submeniu.resetPassword.title"/>"><spring:message code="person.reset.password" /></a>
						</security:authorize>						
						
					</c:when>
				</c:choose>				
			</div>
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
        	<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

		<c:choose>
			<c:when test="${personBean.personId <= 0 and departmentId ne null}">
				<span class="section_title"><img src="images/titles/PersonAdd.jpg"/><spring:message
					code="person.add.toDepartment.section.title" /></span>
			</c:when>
			<c:when test="${personBean.personId <= 0 and departmentId eq null}">
				<c:choose>
					<c:when test="${IS_CREATE_ADMIN eq 'true'}">					
						<span class="section_title"><img src="images/titles/PersonAdd.jpg"/><spring:message
						code="person.add.admin.section.title" /></span>
					</c:when>
					<c:when test="${MY_PROFILE eq null}">
						<span class="section_title"><img src="images/titles/PersonAdd.jpg"/><spring:message
						code="person.add.section.title" /></span>
					</c:when>
					<c:otherwise>
						<span class="section_title"><img src="images/titles/PersonEdit.jpg"/><spring:message code="person.myProfile.title" /></span>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${IS_CREATE_ADMIN eq 'true'}">
						<span class="section_title"><img src="images/titles/PersonEdit.jpg"/><spring:message
						code="person.update.admin.section.title" /></span>
					</c:when>
					<c:when test="${MY_PROFILE eq null}">
						<span class="section_title"><img src="images/titles/PersonEdit.jpg"/><spring:message
						code="person.update.section.title" /></span>
					</c:when>
					<c:otherwise>
						<span class="section_title"><img src="images/titles/PersonEdit.jpg"/><spring:message code="person.myProfile.title" /></span>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		
		<div id="LOCAL_MESSAGES_CONTENT">
			<%@ include	file="../Messages.jsp"%> 
		</div>
		
			<form:form method="post" commandName="personBean" id="personForm" name="personForm" enctype="multipart/form-data">
				<form:hidden id="personId" path="personId" />			
				<table cellpadding="3" class="tableAdd">
				<tr><td class="tableAddSpacer">&nbsp;</td></tr>
				<!-- ====================================FIRST NAME======================================================= -->	
				<tr> 
					<td class="labelTd"> 
				 		<spring:message	code="person.firstName" /><span id="required">*</span> 
					</td> 
					<td>				
						<form:input id="personForm_firstName" path="firstName"  tabindex="1" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);" /> 						
					</td> 				
				<!-- ====================================PICTURE========================================================== -->		
					<td rowspan="5" align="center"> 
					<c:if test="${IS_CREATE_ADMIN ne 'true'}">
						<c:choose>
							<c:when test="${personBean.personId gt 0}">							
								<div id="PICTURE_CONTENT">
									<input type="hidden" name="pictureId" value="${personBean.picture.pictureId}"/>									
									<c:choose>
										<c:when test="${personBean.picture.pictureId > 0}">
											<div id="person_picture"> 
												<img src="servlet/ImageServlet?picture=${personBean.picture.pictureId}" />
											</div>
											<div style="text-align: center"> 
												<a href="#" tabindex="3" onClick="changePersonsPhoto('PersonUploadPicture.htm?personId=${personBean.personId}&pictureId=${personBean.picture.pictureId}', '<spring:message code="person.update.photo"/>')">
													<spring:message code="update" />| 
												<a href="#"	tabindex="4" onClick="getContentFromUrlDirect('PersonRenderPicture.htm?personId=' + ${personBean.personId} + '&DELETE', 'PICTURE_CONTENT')">
													<spring:message code="delete" /> </a>
											</div>
										</c:when>
										
										<c:otherwise>
											<div id="person_picture"> 
												<img src="servlet/ImageServlet?picture=ANONIM" /> 
											</div>
											<div style="text-align: center"> 
												<c:choose>
													<c:when test="${personBean.personId > 0}">											
														<a href="#"	tabindex="3" onClick="changePersonsPhoto('PersonUploadPicture.htm?personId=${personBean.personId}', '<spring:message code="person.upload.photo"/>')">
															<spring:message code="person.upload.photo" /> </a>
													</c:when>
													<c:otherwise>
							<!-- 						<spring:message code="person.upload.photo" />    -->	
													</c:otherwise>
												</c:choose> 
											</div>
										</c:otherwise>
										
									</c:choose> 
								</div>
							</c:when> 
							<c:otherwise>																			
								<div class="fileinputs">
								<spring:message code="person.upload.photo" />
									<spring:bind path="personBean.file"><br />
										<input type="file" class="file" name="file" tabindex="3" accept="image/jpeg" tabindex="3"/> 
									</spring:bind>
								</div>														
							</c:otherwise>
						</c:choose>
					</c:if>
					</td> 					
				</tr> 
							
				<!-- ====================================LAST NAME======================================================== -->
				<tr> 
					<td class="labelTd"> 
						<spring:message	code="person.lastName" /><span id="required">*</span> 
					</td> 
					<td colspan="2">						
						<form:input id="personForm_lastName" path="lastName"  tabindex="2" maxlength="100" cssClass="formField required" onkeypress="return restrictSpecialCharactersForName(event);" /> 						
					</td> 
				</tr> 
				
				<!-- ====================================USERNAME======================================================== -->
				<c:if test="${IS_CREATE_ADMIN ne 'true'}">
					<c:if test="${personBean.personId > 0}">					
						<tr> 
							<td class="labelTd"> 
								<spring:message code="person.username"/>
							</td> 
							<td colspan="2">															
								<form:input id="personForm_username" path="username" cssClass="formField required" maxlength="50" readonly="true"/>								
							</td>						
						</tr> 
					</c:if>
				</c:if>
								
				<!-- ====================================SEX============================================================== -->
				<c:if test="${IS_CREATE_ADMIN ne 'true'}">
					<tr> 
						<td class="labelTd"> 
							<spring:message code="person.sex" />
						</td> 
						<td colspan="2"> 
							<font class="labelTd"><spring:message code="person.sex.m" /></font> 
								<form:radiobutton path="sex" value="M" tabindex="5"/> 
							<font class="labelTd"><spring:message code="person.sex.f" /></font>
								<form:radiobutton path="sex" value="F" tabindex="6"/> 
						</td> 
					</tr> 
				</c:if>
				<!-- ====================================BIRTH DATE======================================================= -->
				<c:if test="${IS_CREATE_ADMIN ne 'true'}">
					<tr> 
						<td class="labelTd"> 
							<spring:message code="person.birthDate" /> 
						</td> 
						<td colspan="2"> 								
							<form:select path="year" items="${Years}" tabindex="7" onchange="PERSON_BIRTH_YEAR = this.options[selectedIndex].value; javascript:getContentFromUrlDirect('PersonGetNumberOfDays.htm?month='+ PERSON_BIRTH_MONTH+'&year='+ this.value, 'DAYS');"/>
							<form:select path="month" items="${Months}" itemLabel="monthName" itemValue="monthId" tabindex="8" onchange="PERSON_BIRTH_MONTH = this.options[selectedIndex].value; javascript:getContentFromUrlDirect('PersonGetNumberOfDays.htm?month='+this.value+'&year='+ PERSON_BIRTH_YEAR, 'DAYS');"/>							
							<div id="DAYS" style="display:inline">
								<form:select path="day" items="${Days}" tabindex="9"/> 
							</div>																										
						</td> 
					</tr> 
				</c:if>		
				<!-- ====================================ADDRESS========================================================== -->
				<c:if test="${IS_CREATE_ADMIN ne 'true'}">
					<tr> 
						<td class="labelTd"> 
							<spring:message	code="person.address" />
						</td> 
						<td colspan="2">							
							<form:textarea path="address" id="personForm_address" tabindex="10" rows="3" cssClass="formField" onkeypress="return restrictCharactersForAddress(event);"/>
							<div class="remainingCharactersAddress" style="display:none" >
								<div id="showRemainingCharactersAddress" />
							</div>								
						</td> 
					</tr> 
				</c:if>							
				<!-- ====================================PHONE============================================================ -->
				<c:if test="${IS_CREATE_ADMIN ne 'true'}">
					<tr> 
						<td class="labelTd"> 
							<spring:message code="person.phone" />
						</td> 
						<td colspan="2"> 
							<form:input id="personForm_phone" path="phone"  tabindex="11" maxlength="30" cssClass="formField" onkeypress="return restrictCharactersForPhone(event);" /> 
						</td> 
					</tr> 
				</c:if>
				<!-- ====================================EMAIL============================================================ -->
				<c:choose>
					<c:when test="${IS_CREATE_ADMIN ne 'true'}">
						<tr> 
							<td class="labelTd"> 
								<spring:message code="person.email" /><span id="required">*</span> 
							</td> 
							<td colspan="2"> 
								<form:input id="personForm_email" path="email"  tabindex="12" maxlength="50" cssClass="formField required validate-email"/> 
							</td> 
						</tr> 
					</c:when>
					<c:otherwise>
						<tr> 
							<td class="labelTd"> 
								<spring:message code="person.email" /><span id="required">*</span> 
							</td> 
							<td colspan="2">								
								<form:input id="personForm_email" path="email"  tabindex="12" maxlength="50" cssClass="formField required validate-email" />									
							</td> 
						</tr> 
					</c:otherwise>
				</c:choose>
				<c:if test="${personBean.personId == -1}">
						<!-- ====================================USERNAME========================================================= -->
						<tr> 
							<td class="labelTd"> 
								<spring:message code="person.username" /><span id="required">*</span> 
							</td> 
							<td> 
								<form:input id="personForm_username" path="username" tabindex="13" maxlength="50" cssClass="formField required personForm_address" onblur="verifyUniqueness('PersonVerifyUsernameUniqueness.htm?198341='+this.value, 'personForm_username', 'personForm_username_unique', this.value, null, null, null)" />
								<div id="personForm_usernameDIV2" style="display: inline"></div> 
							</td> 
						</tr>
						<c:choose>
							<c:when test="${IS_CREATE_ADMIN ne 'true'}">
						<!-- ====================================PASSWORD========================================================= -->
								<tr> 
									<td class="labelTd"> 
										<spring:message code="person.password" /><span id="required">*</span> 
									</td> 
									<td> 
										<form:password	path="password" id="personForm_password" tabindex="14" cssClass="formField required" /> 
									</td> 
								</tr>
								<tr> 
									<td class="labelTd"> 
										<spring:message code="person.password.confirm" /><span id="required">*</span> 
									</td> 
									<td>
										<form:password path="passwordConfirm" id="personForm_passwordConfirm"  tabindex="15" cssClass="formField required" /> 
									</td> 
									<td>
										<div id="passwordDontMatchDiv" style="display: none;">
										<font class="error_msg"><spring:message code="person.password.error" /></font></div>
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr> 
									<td class="labelTd"> 
										<spring:message code="person.password" /><span id="required">*</span> 
									</td>
									<td class="passwordGenerateSentByEmail">
										<spring:message code="person.mail.generate" />
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
				</c:if> 
				<c:if test="${personBean.personId gt 0 and IS_CREATE_ADMIN eq true}">
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_EditUsername}">
						<tr> 
							<td class="labelTd"> 
								<spring:message code="person.username" /><span id="required">*</span> 
							</td> 
							<td> 
								<form:input path="username" maxlength="50" tabindex="16" cssClass="formField required" readonly="true"/>		 
							</td> 
						</tr>
					</security:authorize>
				</c:if>
				<!-- ====================================OBSERVATION====================================================== -->
				<c:if test="${IS_CREATE_ADMIN ne 'true'}">
					<tr> 
						<td class="labelTd"> 
							<spring:message code="person.observation" /> 
						</td> 
						<td> 
							<form:textarea  path="observation"  id="personForm_observation" tabindex="17" cssClass="formField" onkeypress="return restrictSpecialCharactersForObs(event);"/>
							<div class="remainingCharactersPersonObs" style="display:none" >
								<div id="showRemainingCharactersPersonObs"/>
							</div>
						</td> 
					</tr> 	
				</c:if>			
							
				<c:if test="${IS_ADMIN_IT ne 'true'}">
				<c:choose>								
					<c:when test="${IS_CREATE_ADMIN ne 'true'}">
						<!-- ====================================ROLES============================================================ -->
						<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">
							<tr><td colspan="4" height="20">&nbsp;</td></tr>															
							<tr> 
								<td class="labelTd"> 
									<spring:message code="person.roles" />
								</td> 																																												
								<td colspan="3">
									<table style="border: 1px solid #d7d7d7; width:500px;">			
										<tr><td style="line-height:10px;">&nbsp;</td></tr>					
										<tr>
										<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
										
										<td	id="allRoles"> 
												<div class="selectTitle"><spring:message code="person.select.all.roles"/></div>
												<br/>	
												<select id="changeRolesForm_select1" name="changeRolesForm_select1" tabindex="21" multiple="multiple" size="10" class="formField "> 
													<c:forEach var="module"	items="${MODULES_ALL_ROLES}">
														<optgroup id="${module.moduleId}" label="${module.name}"> 
															<c:forEach var="role" items="${module.roles}">
																<option title="${role.name}" id="${role.roleId}" value="${role.roleId}">${role.name}</option>
															</c:forEach> 
														</optgroup>
													</c:forEach> 
												</select> 
											</td>																															
												 		
											<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/>											
												<div id="addRoles" tabindex="19" class="addButton" title="<spring:message code="person.change.roles.addRole"/>" ></div><br/><br/>
												<div id="removeRoles" tabindex="20" class="removeButton" title="<spring:message code="person.change.roles.removeRole"/>" ></div> 
											</td> 
											
											<td>
												<div class="selectTitle"><spring:message code="person.select.current.roles"/></div>
												<br/>	
												<select id="changeRolesForm_select2" name="roles" tabindex="18" multiple="multiple" size="10" class="formField "> 
													<c:forEach	var="module" items="${PERSON_ROLES}">
														<optgroup id="${module.moduleId}" label="${module.name}"> 
															<c:forEach	var="role" items="${module.roles}">
																<option title="${role.name}" id="${role.roleId}" value="${role.roleId}">${role.name}</option>
															</c:forEach> 
														</optgroup>
													</c:forEach> 
			
												</select>
											</td> 	
											
											
											<td width="100%">&nbsp;</td>																				
										</tr>
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
									</table> 
								</td> 
							</tr> 
						</security:authorize>
						
						<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">
							<tr><td colspan="4" height="20">&nbsp;</td></tr>
							<tr>
								<td class="labelTd"> 
									<spring:message code="person.roles" />
								</td> 	
								<td colspan="3">
									<table style="border: 1px solid #d7d7d7; width:300px;">
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
										<tr>
											<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>	
											<td>	
												<c:if test="${!(empty  personBean.roles)}">	
													<div class="selectTitle"><spring:message code="person.select.current.roles"/></div>
													<br/>							
													<select id="roleForm" name="roles" tabindex="18" multiple="multiple" size="10" class="formField "> 
														<c:forEach	var="module" items="${PERSON_ROLES}">
															<optgroup id="${module.moduleId}" label="${module.name}"> 
																<c:forEach	var="role" items="${module.roles}">
																	<option title="${role.name}" id="${role.roleId}" value="${role.roleId}">${role.name}</option>
																</c:forEach> 
															</optgroup>
														</c:forEach> 
				
													</select>																
												</c:if>			
											</td>
											<td width="100%">&nbsp;</td>
										</tr>	
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
									</table>
								</td>	
							</tr>
						</security:authorize>
			
						<!-- ====================================DEPARTMENTS====================================================== -->
						<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">
							<tr><td colspan="4" height="20">&nbsp;</td></tr>		
							<tr> 
								<td class="labelTd"> 
									<spring:message code="person.depts" />
								</td> 
								<input type="hidden" name="depts" value="${FAKE_DEPARTMENT_ID}" /> 
																																			
								<td colspan="3">
									<table style="border: 1px solid #d7d7d7; width:500px; ">
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
										<tr>
											<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
											
											<td id="allDepartments"> 
												<div class="selectTitle"><spring:message code="person.select.all.depts"/></div>
												<br/>
												<select	id="changeDepartmentsForm_select1"	name="changeDepartmentsForm_select1" tabindex="25" multiple="multiple" size="10" class="formField "> 
													<c:forEach var="dept" items="${ALL_DEPARTMENTS}">
														<option title="${dept.name}" id="${dept.departmentId}"	value="${dept.departmentId}">${dept.name}</option>
													</c:forEach> 
												</select> 
											</td> 														
											
											<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>
												<div id="addDepartment" tabindex="23" class="addButton" title="<spring:message code="person.change.departments.addDepartment"/>"></div><br /><br />
												<div id="removeDepartment" tabindex="24" class="removeButton"	title="<spring:message code="person.change.departments.removeDepartment"/>"></div>
											</td>
											
											<td>
												<div class="selectTitle"><spring:message code="person.select.current.depts"/></div>
												<br/>	
												<select id="changeDepartmentsForm_select2" name="depts" tabindex="22" multiple="multiple" size="10" class="formField "> 
													<c:forEach var="dept" items="${personBean.depts}">
														<option title="${dept.name}" id="${dept.departmentId}" value="${dept.departmentId}">${dept.name}</option>
													</c:forEach> 
												</select>
											</td> 
											
											<td width="100%">&nbsp;</td>
										</tr> 
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
									</table> 
								</td>								 	
							</tr> 
						</security:authorize>
						
						<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">
							<tr><td colspan="4" height="20">&nbsp;</td></tr>
							<tr>
								<td class="labelTd"> 
									<spring:message code="person.depts" />
								</td> 
								<td colspan="3">	
									<c:choose>
										<c:when test="${!(empty personBean.depts)}">
											<table style="border: 1px solid #d7d7d7; width:300px; ">
												<tr><td style="line-height:10px;">&nbsp;</td></tr>
												<tr>
													<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
													<td> 
														<div class="selectTitle"><spring:message code="person.select.current.depts"/></div>
														<br/>
														<select id="deptForm" name="departments" name="depts" tabindex="22" multiple="multiple" size="10" class="formField ">
															<c:forEach var="dept" items="${personBean.depts}">		
																<option title="${dept.name}">${dept.name}</option>									
															</c:forEach>	
														</select>
													</td>	
													<td width="100%">&nbsp;</td>
												</tr>	
												<tr><td style="line-height:10px;">&nbsp;</td></tr>
											</table>										
										</c:when>				
										<c:otherwise>
											<div class="noFindResults"><spring:message code="person.not.in.department"/></div>
										</c:otherwise>	
									</c:choose>		
								</td>
							</tr>	
						</security:authorize>						
												
						<!-- ====================================JOBS IN DEPARTMENTS====================================================== -->
						<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">	
							<tr><td colspan="4" height="20">&nbsp;</td></tr>	
							<tr>
								<td class="labelTd">
									<div id="JOBS_TITLE" class="labelJobs" > </div>										
								</td> 
								<td colspan="3">
									<table style="border: 1px solid #d7d7d7; width:527px; ">
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
										<tr>
											<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
											<td><div id="jobsContainer"></div></td>																																																																																													
																																																																																																				
											<td width="100%" valign="top" align="right">
												<c:if test="${IS_ADMIN_IT ne 'true'}">
													<c:if test="${IS_CREATE_ADMIN ne 'true'}">
														<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobAdd}, ${PERMISSION_CONSTANT.OM_JobUpdate} ">																	
																<input type="button" class="button"  id="jobPanel" tabindex="31" value="<spring:message code="person.job.add"/>"/>																															
														</security:authorize>
													</c:if>
												</c:if>	
											</td>
										</tr> 
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
									</table> 								
								</td> 
							</tr> 	
						</security:authorize>
						
						<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">
							<tr> 
								<td class="labelTd"> 
									<spring:message code="person.jobs" />
								</td>
								<td colspan="3"> 	
									<c:choose>
										<c:when test="${!(empty PERSON_JOBS)}">										
												<c:forEach var="jobDept" items="${DEPT_JOBS}">
													${jobDept}<br/>
												</c:forEach>																									
										</c:when>						
										<c:otherwise>
											<div class="noFindResults"><spring:message code="person.not.have.job"/></div>
										</c:otherwise>	
									</c:choose>											
								</td>
							</tr>
						</security:authorize>																							
				
					<!-- ===========================================PERSON'S USER GROUPS======================================================= -->
					<c:if test="${MY_PROFILE eq null}">
						<tr><td colspan="4" height="20">&nbsp;</td></tr>
						<tr>
							<td class="labelTd"> 
								<spring:message code="person.usergroups"/>						
							</td>
							<input type="hidden" name="userGroups" value="${DEFAULT_USER_GROUP_ID}"/>
							<td colspan="3">
								<div id="PERSON_USER_GROUPS">
									<table style="border: 1px solid #d7d7d7; width:500px; ">
										<tr><td style="line-height:10px;">&nbsp;</td></tr>
										<tr>
											<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
											
											<td id="allOrganizationUserGroups">
												<div class="selectTitle"><spring:message code="person.select.all.usergroups"/></div>
												<br/>
												<select	id="changePersonUserGroups_select1"	name="changePersonUserGroups_select1" tabindex="29" multiple="multiple" size="10" class="formField">
													<c:forEach var="userGroup" items="${ALL_USER_GROUPS}">
														<option title="${userGroup.name}" value="${userGroup.userGroupId}" id="${userGroup.userGroupId}">${userGroup.name}</option>
													</c:forEach>
												</select>
											</td>				
																				
											<td id="commands" style="padding: 0px 8px 0px 5px;"><br/><br/><br/><br/><br/>										
												<div id="addUserGroups" tabindex="27" class="addButton" title="<spring:message code="person.change.userGroups.addUserGroups"/>"></div><br/><br/>
												<div id="removeUserGroups" tabindex="28" class="removeButton"	title="<spring:message code="person.change.userGroups.removeUserGroups"/>"></div>
											</td>
											
											<td>
												<div class="selectTitle"><spring:message code="person.select.current.usergroups"/></div>
												<br/>	
												<select id="changePersonUserGroups_select2" name="userGroups" tabindex="26" multiple="multiple" size="10" class="formField">
													<c:forEach var="userGroup" items="${personBean.userGroups}">
														<option title="${userGroup.name}" value="${userGroup.userGroupId}" id="${userGroup.userGroupId}">${userGroup.name}</option>
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
					</c:if>
				
				</c:when>
					<c:otherwise>							
						<input type="hidden" name="depts" value="${FAKE_DEPARTMENT_ID}" />
						<input type="hidden" name="roles" value="${ADMIN_ROLE_ID}"/>
						<div id="jobsContainer"></div>						
					</c:otherwise>
				</c:choose>
				
				</c:if>
				<tr>
					<td colspan="5" class="formActions" align="right">
						<c:choose> 
							<c:when test="${IS_CREATE_ADMIN eq 'true'}">																
									<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_PersonAdd}, ${PERMISSION_CONSTANT.OM_PersonUpdate}">									
										<input type="button" class="button" id="saveAdmin" tabindex="30" value="<spring:message code="save" />"/>
									</security:authorize>																	
							</c:when>
							<c:otherwise>
								<input id="savePerson" type="button" class="button" tabindex="30" value="<spring:message code="save"/>"/>								
							</c:otherwise>
						</c:choose>
						
						<c:choose> 
							<c:when test="${IS_CREATE_ADMIN eq 'true'}">									
								<input type="button" class="button" id="cancel" tabindex="32" value="<spring:message code="cancel"/>" />								
							</c:when>
							<c:otherwise>								
								<input type="button" class="button" id="cancel" tabindex="32" value="<spring:message code="cancel"/>" />							
							</c:otherwise>
						</c:choose>											
					</td>
				</tr>
			</table>
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

		document.personForm.firstName.focus(); 
		<c:if test="${IS_ADMIN_IT ne 'true'}">
			<c:choose>
					<c:when test="${IS_CREATE_ADMIN ne 'true'}">
					
						<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_ViewPersonRoleDepartmentDetails}">	
																				
								//The handler to deal with job selection in a department
								var handleChooseJobInDepartment = new HandleChooseJobInDepartment('jobsContainer', ${PERSON_JOBS}, ${JOBS}, 'JOBS_TITLE', '<spring:message code="person.jobs" />');
								handleChooseJobInDepartment.displayPersonJobs();	
								selectOption();								
								SELECT_DEPT_JOB = new Array();								
								
							<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_JobAdd}, ${PERMISSION_CONSTANT.OM_JobUpdate} ">
								
									var submitJobPanel = ObjSubmitJobPanel('Job.htm?GET_FROM_PANEL=true','<spring:message code="person.job.add"/>', handleChooseJobInDepartment); 
									YAHOO.util.Event.addListener("jobPanel", "click", displayAddJobPanel, submitJobPanel, true);
								
							</security:authorize>
						
							
															
								//Add event on Add Department Button
								YAHOO.util.Event.addListener('addDepartment', 'click', function addDepartment() {
									moveSelectOptionsSourceDestDepartment('changeDepartmentsForm_select1', 'changeDepartmentsForm_select2', null, handleChooseJobInDepartment);
								}, null, false);
							
								//Add event on Remove Department Button
								YAHOO.util.Event.addListener('removeDepartment', 'click', function removeDepartment() {
									moveSelectOptionsSourceDestDepartment('changeDepartmentsForm_select2', 'changeDepartmentsForm_select1', true, handleChooseJobInDepartment);
								}, null, false);
															
						
			
							<c:choose>
								<c:when test="${ADMIN_IT eq 'true'}">

									//Add event on Add Roles Button
									YAHOO.util.Event.addListener('addRoles', 'click', function addRoles() {
										moveSelectOptionsOptGroupSourceDest('changeRolesForm_select1', 'changeRolesForm_select2', true, true);
									}, null, false);
		
									//Add event on Remove Roles Button
									YAHOO.util.Event.addListener('removeRoles', 'click', function removeRoles() {
										moveSelectOptionsOptGroupSourceDest('changeRolesForm_select2', 'changeRolesForm_select1', true, true);
									}, null, false);
								</c:when>
							
								<c:otherwise>
								//Add event on Add Roles Button
								YAHOO.util.Event.addListener('addRoles', 'click', function addRoles() {
									moveSelectOptionsOptGroupSourceDest('changeRolesForm_select1', 'changeRolesForm_select2', true, false);
								}, null, false);
	
								//Add event on Remove Roles Button
								YAHOO.util.Event.addListener('removeRoles', 'click', function removeRoles() {
									moveSelectOptionsOptGroupSourceDest('changeRolesForm_select2', 'changeRolesForm_select1', true, false);
								}, null, false);
								</c:otherwise>
							</c:choose>
																
							<c:if test="${SESS_ORGANISATION_ID ne null}">
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonAdd}">																			
									//Add event to Add New Button
									submitObject = {url: "PersonForm.htm?ACTION=NEW", container: "MAIN_CONTENT", panelTitle: '<spring:message code="person.add.section.title"/>'};
									YAHOO.util.Event.addListener("addNewPerson", "click", getContentFromUrl, submitObject, true);						
								</security:authorize>
	
								//------------ DELETE PERSON ---------------------------------------------------------------
								<c:if test="${personBean.personId != -1}">
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_PersonDelete}">
										submitObject = new ObjSubmitWithConfirmation("PersonSearch.htm?ACTION=DELETE_FROM_PERSON_FORM&personId=${personBean.personId}", "", "MAIN_CONTENT", '<spring:message code="person.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');
										YAHOO.util.Event.addListener("delete", "click", deleteWithConfirmation, submitObject, true);		
									</security:authorize>
								</c:if>
							</c:if>
						</security:authorize>			
						
						//------------------------------------- OPERATIONS ON SEPARATED FIELDS -----------------------
						<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_PersonAdd}, ${PERMISSION_CONSTANT.OM_PersonUpdate}">						
							var validateObservationField = new ObjFieldRemaining("personForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersPersonObs');
							YAHOO.util.Event.addListener("personForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
							YAHOO.util.Event.addListener("personForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
							YAHOO.util.Event.addListener("personForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);


							var validateAddressField = new ObjFieldRemaining("personForm_address", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersAddress');
							YAHOO.util.Event.addListener("personForm_address", "focus", showInfoOnClickInField, validateAddressField, true);
							YAHOO.util.Event.addListener("personForm_address", "blur", hideInfoOnBlurInField, validateAddressField, true);
							YAHOO.util.Event.addListener("personForm_address", "keyup", showInfoOnKeyUpInField, validateAddressField, true);	
							
						</security:authorize>
						//---------------------------------- END OPERATIONS ON SEPARATED FIELDS -----------------------												
				</c:when>
			</c:choose>
		</c:if>
		
		<c:choose>
			<c:when test="${MY_PROFILE eq null}">
				<c:choose>
					<c:when test="${IS_CREATE_ADMIN ne 'true'}">							
							//Add event to the SAVE BUTTON	
							<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_PersonAdd}, ${PERMISSION_CONSTANT.OM_PersonUpdate}">				
								submitObject = new ObjSubmitSavePerson('PersonForm.htm?ACTION=SAVE', "personForm", "MAIN_CONTENT", 
											'personForm_username_unique', 'personForm_password', 'personForm_passwordConfirm', 'passwordDontMatchDiv',
										 	'changeDepartmentsForm_select2', 'changeRolesForm_select2', 'changePersonUserGroups_select2');
								YAHOO.util.Event.addListener("savePerson", "click", submitSavePerson, submitObject, true);
							</security:authorize>	

							<c:if test="${personBean.personId > 0}">
								//Add event to Change Password Button
								YAHOO.util.Event.addListener('changePassword', 'click', changePersonPassword, 
										{url: 'PersonForm.htm?personId=${personBean.personId}&ACTION=CHANGE_PASSWORD', panelTitle: '<spring:message code="person.change.password"/>',
										 submitButtonLabel: '<spring:message code="update"/>', 
										 cancelButtonLabel: '<spring:message code="cancel"/>'}, true);
						
							
								//Add event to Reset Password Button	
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ResetPassword}">													
										YAHOO.util.Event.addListener('resetPassword', 'click', resetPersonPassword, 
											{url: 'PersonForm.htm?personId=${personBean.personId}&ACTION=RESET_PASSWORD', panelTitle: '<spring:message code="person.reset.password"/>',
											 submitButtonLabel: '<spring:message code="reset"/>', 
											 cancelButtonLabel: '<spring:message code="cancel"/>',
											 confirmationMessage: "<spring:message code="person.reset.password.confirmationMessage"/>"}, true);									
								</security:authorize>
					
							</c:if>	 
					</c:when>
					<c:otherwise>								
						//Add event to the SAVE BUTTON
						<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_PersonAdd}, ${PERMISSION_CONSTANT.OM_PersonUpdate}">
							submitObject = new ObjSubmitSavePerson('CreateAdmin.htm?ACTION=SAVEADMIN&ORG='.concat('${ORG}'), "personForm", "MAIN_CONTENT", 
										'personForm_username_unique', 'personForm_password', 'personForm_passwordConfirm', 'passwordDontMatchDiv',
									 	'changeDepartmentsForm_select2', 'changeRolesForm_select2', 'changePersonUserGroups_select2');
							YAHOO.util.Event.addListener("saveAdmin", "click", submitSavePerson, submitObject, true);
						</security:authorize>										
					</c:otherwise>
				</c:choose>
			</c:when>
			
			<c:otherwise>				
			//---------------------------------- END OPERATIONS ON SEPARATED FIELDS -----------------------				
				<c:choose>
					<c:when test="${IS_ADMIN_IT eq 'true'}">
						submitObject = new ObjSubmitSaveAdminIt('PersonMyProfile.htm?ACTION=SAVE', "personForm", "MAIN_CONTENT", 
								'personForm_username_unique', 'personForm_password', 'personForm_passwordConfirm', 'passwordDontMatchDiv');
						YAHOO.util.Event.addListener("savePerson", "click", submitSaveAdminIt, submitObject, true);				
					</c:when>
	
					<c:otherwise>
						//Add event to the SAVE BUTTON			
						submitObject = new ObjSubmitSavePerson('PersonMyProfile.htm?ACTION=SAVE', "personForm", "MAIN_CONTENT", 
									'personForm_username_unique', 'personForm_password', 'personForm_passwordConfirm', 'passwordDontMatchDiv',
								 	'changeDepartmentsForm_select2', 'changeRolesForm_select2', 'changePersonUserGroups_select2');
						YAHOO.util.Event.addListener("savePerson", "click", submitSavePerson, submitObject, true);			
					</c:otherwise>

				</c:choose>
				
				<c:choose>
					<c:when test="${IS_CREATE_ADMIN ne 'true'}">												
						<c:if test="${personBean.personId > 0}">
							//Add event to Change Password Button
							YAHOO.util.Event.addListener('changePassword', 'click', changePersonMyProfilePassword, 
									{url: 'PersonMyProfile.htm?personId=${personBean.personId}&ACTION=CHANGE_PASSWORD', panelTitle: '<spring:message code="person.change.password"/>',
									 submitButtonLabel: '<spring:message code="update"/>', 
									 cancelButtonLabel: '<spring:message code="cancel"/>'}, true);

							//Add event to Reset Password Button	
							<security:authorize ifAllGranted="${PERMISSION_CONSTANT.OM_ResetPassword}">													
									YAHOO.util.Event.addListener('resetPassword', 'click', resetPersonPassword, 
										{url: 'PersonForm.htm?personId=${personBean.personId}&ACTION=RESET_PASSWORD', panelTitle: '<spring:message code="person.reset.password"/>',
										 submitButtonLabel: '<spring:message code="reset"/>', 
										 cancelButtonLabel: '<spring:message code="cancel"/>',
										 confirmationMessage: "<spring:message code="person.reset.password.confirmationMessage"/>"}, true);									
							</security:authorize>
							
						</c:if>
					</c:when>
				</c:choose>
				//------------------------------------- OPERATIONS ON SEPARATED FIELDS -----------------------
				<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_PersonAdd}, ${PERMISSION_CONSTANT.OM_PersonUpdate}">
					var validateObservationField = new ObjFieldRemaining("personForm_observation", '<spring:message code="observation.message"/>', 2000, 'showRemainingCharactersPersonObs');
					YAHOO.util.Event.addListener("personForm_observation", "focus", showInfoOnClickInField, validateObservationField, true);
					YAHOO.util.Event.addListener("personForm_observation", "blur", hideInfoOnBlurInField, validateObservationField, true);
					YAHOO.util.Event.addListener("personForm_observation", "keyup", showInfoOnKeyUpInField, validateObservationField, true);


					var validateAddressField = new ObjFieldRemaining("personForm_address", '<spring:message code="observation.message"/>', 1000, 'showRemainingCharactersAddress');
					YAHOO.util.Event.addListener("personForm_address", "focus", showInfoOnClickInField, validateAddressField, true);
					YAHOO.util.Event.addListener("personForm_address", "blur", hideInfoOnBlurInField, validateAddressField, true);
					YAHOO.util.Event.addListener("personForm_address", "keyup", showInfoOnKeyUpInField, validateAddressField, true);	
					
				</security:authorize>					
			</c:otherwise>
			
		</c:choose>

		<c:if test="${ORGANISATION_SESSION != '-1'}">			
			<c:choose>			
				<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewPersonAdmin}">															
					YAHOO.util.Event.addListener("back", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
					container : "MAIN_CONTENT", withContext : true}, true);							
				</c:when>				
				<c:otherwise>
					<c:choose>			
						<c:when test="${ACTION == BACK_CONSTANT.OM_Back_SavePerson}">							
							<c:choose>
								<c:when test="${IS_CREATE_ADMIN eq 'true'}">	
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
										//alert("IS_CREATE_ADMIN true- super");
										var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
										YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
									</security:authorize>
	
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
										//alert("IS_CREATE_ADMIN true -  not super");
										YAHOO.util.Event.addListener("back", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
										container : "MAIN_CONTENT", withContext : true}, true);
									</security:authorize>
								</c:when>
	
								<c:otherwise>	
									//alert("IS_CREATE_ADMIN - not true");						
									getObject = new ObjSubmit("PersonSearch.htm", "", "MAIN_CONTENT");
									YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
								</c:otherwise>
							</c:choose>																				
						</c:when>	
						<c:otherwise>
							<c:choose>
								<c:when test="${ACTION == BACK_CONSTANT.OM_Back_SavePersonAdmin}">
									<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">
										//alert("SAVE ADMIN - super");
										var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
										YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);
									</security:authorize>
	
									<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">
										//alert("SAVE ADMIN - not super");
										YAHOO.util.Event.addListener("back", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
										container : "MAIN_CONTENT", withContext : true}, true);
									</security:authorize>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewPersonFromOrganisation}">	
											//alert("NEW PERSON FROM ORGANISATION");						
											getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");
											YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);				
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewPersonFromDepartment || ACTION == BACK_CONSTANT.OM_BACK_EditPersonFromDepartment}">
													<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}">
														//alert("NEW PERSON FROM DEPARTMENT");														
														getObject = new ObjSubmit("DepartmentForm.htm?ACTION=GET&departmentId=${departmentId}", "", "MAIN_CONTENT");
														YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);																			
													</security:authorize>							
												</c:when>
												<c:otherwise>													
													//alert("ALTFEL");																									
													getObject = new ObjSubmit("PersonSearch.htm", "", "MAIN_CONTENT");
													YAHOO.util.Event.addListener("back", "click", getContentFromUrl, getObject, true);																		
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>	
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>


			//------------ CANCEL PERSON ---------------------------------------------------------------			
			<c:choose>			
			<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewPersonAdmin}">															
				YAHOO.util.Event.addListener("cancel", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
				container : "MAIN_CONTENT", withContext : true}, true);							
			</c:when>				
			<c:otherwise>
				<c:choose>			
					<c:when test="${ACTION == BACK_CONSTANT.OM_Back_SavePerson}">							
						<c:choose>
							<c:when test="${IS_CREATE_ADMIN eq 'true'}">	
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">								
									var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
									YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);
								</security:authorize>

								<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">									
									YAHOO.util.Event.addListener("cancel", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
									container : "MAIN_CONTENT", withContext : true}, true);
								</security:authorize>
							</c:when>

							<c:otherwise>														
								getObject = new ObjSubmit("PersonSearch.htm", "", "MAIN_CONTENT");
								YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);
							</c:otherwise>
						</c:choose>																				
					</c:when>	
					<c:otherwise>
						<c:choose>
							<c:when test="${ACTION == BACK_CONSTANT.OM_Back_SavePersonAdmin}">
								<security:authorize ifAllGranted="${PERMISSION_CONSTANT._Super}">									
									var getObject = new ObjSubmit("OrganisationSearch.htm", "", "MAIN_CONTENT");
									YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);
								</security:authorize>

								<security:authorize ifNotGranted="${PERMISSION_CONSTANT._Super}">									
									YAHOO.util.Event.addListener("cancel", "click", getContentFromUrlDirectWithUpdatingOrgTreePanel, { url : "OrganisationOverview.htm?ACTION=GET&organisationId=${SESS_ORGANISATION_ID}", 
									container : "MAIN_CONTENT", withContext : true}, true);
								</security:authorize>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewPersonFromOrganisation}">																
										getObject = new ObjSubmit("OrganisationOverview.htm?organisationId=${SESS_ORGANISATION_ID}", "", "MAIN_CONTENT");
										YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);				
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${ACTION == BACK_CONSTANT.OM_Back_NewPersonFromDepartment || ACTION == BACK_CONSTANT.OM_BACK_EditPersonFromDepartment}">
												<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_DeptUpdate}">																										
													getObject = new ObjSubmit("DepartmentForm.htm?ACTION=GET&departmentId=${departmentId}", "", "MAIN_CONTENT");
													YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);																			
												</security:authorize>							
											</c:when>
											<c:otherwise>																																														
												getObject = new ObjSubmit("PersonSearch.htm", "", "MAIN_CONTENT");
												YAHOO.util.Event.addListener("cancel", "click", getContentFromUrl, getObject, true);																		
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>	
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>		
	</c:if>
		
		<security:authorize ifAnyGranted=" ${PERMISSION_CONSTANT.OM_PersonAdd}, ${PERMISSION_CONSTANT.OM_PersonUpdate}">
		
			//----------------------------------------- PERSON ENTER EVENT ON REQUESTED TAGS ---------------------------------------
			var keyListener = null;
			var submitObject = new ObjSubmitSavePerson('PersonMyProfile.htm?ACTION=SAVE', "personForm", "MAIN_CONTENT", 
						'personForm_username_unique', 'personForm_password', 'personForm_passwordConfirm', 'passwordDontMatchDiv',
					 	'changeDepartmentsForm_select2', 'changeRolesForm_select2', 'changePersonUserGroups_select2');
			
		 	if(document.getElementById('personForm_firstName')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_firstName", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}

		 	if(document.getElementById('personForm_lastName')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_lastName", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}

		 	if(document.getElementById('personForm_address')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_address", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}

		 	if(document.getElementById('personForm_phone')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_phone", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}

		 	if(document.getElementById('personForm_email')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_email", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}

		 	if(document.getElementById('personForm_username')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_username", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}

		 	if(document.getElementById('personForm_password')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_password", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}

		 	if(document.getElementById('personForm_passwordConfirm')) {
		 		keyListener = new YAHOO.util.KeyListener("personForm_passwordConfirm", { keys:13 },  { fn:submitSavePerson, scope: submitObject, correctScope:true}, null ); 
				keyListener.enable();
			}
			
		</security:authorize>
		<c:if test="${MY_PROFILE eq null}">
			
				//-------------------------------------- ADD USER GROUPS ------------------------------------------------------
				YAHOO.util.Event.addListener('addUserGroups', 'click', function addPersonUserGroups() {
				moveSelectOptionsSourceDest('changePersonUserGroups_select1', 'changePersonUserGroups_select2');
				}, null, false);
		
				//------------------------------------- REMOVE USER GROUPS ----------------------------------------------------
				YAHOO.util.Event.addListener('removeUserGroups', 'click', function removePersonUserGroups() {
				moveSelectOptionsSourceDest('changePersonUserGroups_select2', 'changePersonUserGroups_select1', true);
				}, null, false);	
			
		</c:if>

		//initialize the file uploads
		initFileUploads('<spring:message code="person.file" />');

		<c:if test="${IS_CREATE_ADMIN ne 'true'}">
			PERSON_BIRTH_YEAR = ${CURRENT_YEAR};
			PERSON_BIRTH_MONTH = ${CURRENT_MONTH};	
			PERSON_BIRTH_DAY = ${CURRENT_DAY};		
	
			getContentFromUrlDirect('PersonGetNumberOfDays.htm?month='+ PERSON_BIRTH_MONTH+'&year='+ PERSON_BIRTH_YEAR+'&day='+PERSON_BIRTH_DAY, 'DAYS');
		</c:if>
		} 	
	
</script>
