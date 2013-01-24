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

<!-- ////////////////////////////SUBMENU START/////////////////////////////////// -->

<table id="mainContentTable" border="0" cellpadding="0" cellspacing="0">
	<tr>
	    <td id="submenuCell">
			<div id="submenu">
			
				<!-- HOME -->
				<!--   <a href="#" id="home" onclick="ENTITY = getNotificationSettingsData();check_add('Home.htm', 'MAIN_CONTENT');" ><spring:message code="home" /></a>	-->
				
				<!-- NOTIFICATION SEARCH -->
				<!--  
				<div id="active_link">
					<a href="#" title="<spring:message code="notification.search.submenu.title"/>" id="notificationSearch" onclick="ENTITY = getNotificationSettingsData();check_add('SearchNotification.htm', 'MAIN_CONTENT');" ><spring:message code='notification.search'/></a>
				</div>	
				-->
				<!-- NOTIFICATION SETTINGS -->

				<div id="active_link">
					<a href="#" title="<spring:message code="notification.settings.submenu.title"/>" id="notificationSettings" onclick="ENTITY = getNotificationSettingsData();check_add('NotificationSettings.htm', 'MAIN_CONTENT');" ><spring:message code='notification.settings.section.title'/></a>
				</div>		
				
				<!-- NOTIFICATION LANGUAGE -->
				<div id="active_link">
					<a href="#" title="<spring:message code="notification.language.submenu.title"/>" id="notificationLanguage" onclick="ENTITY = getNotificationSettingsData();check_add('NotificationLanguage.htm', 'MAIN_CONTENT');"><spring:message code='notification.language.section.title'/></a>
				</div>	
			</div>
				
			<div id="submenu_footer"></div>
        </td>
        <td id="contentCell">
			<div id="CONTENT">
<!-- //////////////////////////////////////////////////////////////////////////// -->

<span class="section_title"><img src="images/titles/NotificationSettings.jpg"/><spring:message code="notification.settings.section.title" /></span>

<div id="LOCAL_MESSAGES_CONTENT">
	<%@ include	file="../Messages.jsp"%> 
</div>

<table class="tableSearch" cellpadding="0" cellspacing="0"><tr><td>

<form:form id="notificationSettingsForm" name="notificationSettingsForm" commandName="notificationSettingsFormBean">

<table width="500px"><tr><td align="left">

<table>
	<!-- //////////////////////////////////PROJECTS////////////////////////////////////////// -->
	<tr>
		<td class="labelTd">
			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_NotificationReceive}">									
				<spring:message code='notification.settings.project.organization' />
			</security:authorize>
			<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_NotificationReceive}">									
				<spring:message code='notification.settings.project' />
			</security:authorize>
		</td>
		<td>	
			<div title='<spring:message code="notification.settings.project.select"/>' id="notificationSettingsFormSearchProjectsId">
			<form:select path="projectId" id="notificationSettingsForm_projectId"
						tabindex="2" cssClass="formFieldSearch" onfocus="javascript:changeTitle(this.options[selectedIndex].title, 'notificationSettingsFormSearchProjectsId');this.style.color = getStyleTextColor(this.selectedIndex, 'notificationSettingsForm_projectId', 'notificationSettingsForm_projectId1')"
						onchange="javascript:changeTitle(this.options[selectedIndex].title, 'notificationSettingsFormSearchProjectsId');this.style.color = getStyleTextColor(this.selectedIndex, 'notificationSettingsForm_projectId', 'notificationSettingsForm_projectId1');ENTITY = getNotificationSettingsData();check_add('NotificationSettings.htm?PROJECT_ID=' + this.options[selectedIndex].value,'MAIN_CONTENT')">
						<option value="-2" class="chooseProject" title='<spring:message code="notification.settings.project.select"/>' ><spring:message code="notification.settings.project.select"/></option>
						<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_NotificationReceive}">		
							<option value="-1" class="chooseProject" title='<spring:message code="notification.settings.from.organization"/>' ${notificationSettingsFormBean.projectId == -1 ? 'selected' : ''} ><spring:message code="notification.settings.from.organization"/></option>	
						</security:authorize>
						<c:forEach var="project" items="${AVAILABLE_PROJECTS}">
							<c:choose>
								<c:when test="${project.clientId != -1}">
									<c:set var="client" value="hasClient" />
									<c:choose>
										<c:when test="${project.status == 1}">
											<c:set var="prj" value="" />
										</c:when>
										<c:otherwise>
											<spring:message code="project.for.client" var="prj" />
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:set var="client" value="noClient" />
									<spring:message code="project.for.organization" var="prj" />
								</c:otherwise>
							</c:choose>

							<c:choose>
								<c:when test="${project.status == 1}">
									<c:set var="type" value="Opened" />
									<spring:message code="project.opened" var="Type" />
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${project.status == 2}">
											<c:set var="type" value="Closed" />
											<spring:message code="project.closed" var="Type" />
										</c:when>
										<c:otherwise>
											<c:if test="${project.status == 3}">
												<c:set var="type" value="Aborted" />
												<spring:message code="project.abandoned" var="Type" />
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<option title="${project.name} ${prj}${Type}" class="${client}${type}" value="${project.projectId}" ${notificationSettingsFormBean.projectId == project.projectId ? 'selected' : ''} >${project.name}</option>
						</c:forEach>
					</form:select></div>
					<div id="notificationSettingsForm_projectId1">
					<span class="chooseProject"></span>	
					<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_NotificationReceive}">		
						<span class="chooseProject"></span>		
					</security:authorize>
					<c:forEach var="project" items="${AVAILABLE_PROJECTS}">
						<c:choose>
							<c:when test="${project.clientId != -1}">
								<c:set var="client" value="hasClient" />
							</c:when>
							<c:otherwise>
								<c:set var="client" value="noClient" />
							</c:otherwise>
						</c:choose>

						<c:choose>
							<c:when test="${project.status == 1}">
								<c:set var="type" value="Opened" />
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${project.status == 2}">
										<c:set var="type" value="Closed" />
									</c:when>
									<c:otherwise>
										<c:if test="${project.status == 3}">
											<c:set var="type" value="Aborted" />
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						<span class="${client}${type}"></span>
					</c:forEach>
					</div>
			<form:hidden path="projectDetailId" id="notificationSettingsForm_projectDetailId"/>		
			<form:hidden path="userId" id="notificationSettingsForm_userId"/>	
			<form:hidden path="organizationId" id="notificationSettingsForm_organizationId"/>			
		</td>
	</tr>
	<!-- //////////////////////////////////PROJECTS END////////////////////////////////////////// -->
</table>
</td></tr>
<tr><td>
<br></br>
<c:if test="${notificationSettingsFormBean.projectId != -2}">	
	<table width="100%">
		<!-- //////////////////////////////////RECORD ADD/DELETE/UPDATE////////////////////// -->
		<tr>			
			<td class="labelTd" > 
				<spring:message code="notification.settings.record.add"/>
			</td>
			<td>
				<form:checkbox path="recordAdd" id="notificationSettingsForm_recordAdd"  value="true"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.record.delete"/>
			</td>
			<td>
				<form:checkbox path="recordDelete" id="notificationSettingsForm_recordDelete" value="false"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.record.update"/>
			</td>
			<td>
				<form:checkbox path="recordUpdate" id="notificationSettingsForm_recordUpdate" />
			</td>
		</tr>
		<!-- //////////////////////////////////COST ADD/DELETE/UPDATE////////////////////// -->
		<tr>		
			<td class="labelTd" > 
				<spring:message code="notification.settings.cost.add"/>
			</td>
			<td>
				<form:checkbox path="costAdd" id="notificationSettingsForm_costAdd" value="false"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.cost.delete"/>
			</td>
			<td>
				<form:checkbox path="costDelete" id="notificationSettingsForm_costDelete" value="false"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.cost.update"/>
			</td>
			<td>
				<form:checkbox path="costUpdate" id="notificationSettingsForm_costUpdate" />
			</td>
		</tr>
		<!-- //////////////////////////////////ACTIVITY ADD/DELETE/UPDATE////////////////////// -->
		<tr>		
			<td class="labelTd" > 
				<spring:message code="notification.settings.activity.add"/>
			</td>
			<td>
				<form:checkbox path="activityAdd" id="notificationSettingsForm_activityAdd" value="false"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.activity.delete"/>
			</td>
			<td>
				<form:checkbox path="activityDelete" id="notificationSettingsForm_activityDelete" value="false"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.activity.update"/>
			</td>
			<td>
				<form:checkbox path="activityUpdate" id="notificationSettingsForm_activityUpdate" />
			</td>
		</tr>
		<!-- //////////////////////////////////EXCHANGE ADD/DELETE/UPDATE////////////////////// -->
		<tr>		
			<td class="labelTd" > 
				<spring:message code="notification.settings.exchange.add"/>
			</td>
			<td>
				<form:checkbox path="exchangeAdd" id="notificationSettingsForm_exchangeAdd" value="false"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.exchange.delete"/>
			</td>
			<td>
				<form:checkbox path="exchangeDelete" id="notificationSettingsForm_exchangeDelete" value="false"/>
			</td>
			<td class="labelTd" > 
				<spring:message code="notification.settings.exchange.update"/>
			</td>
			<td>
				<form:checkbox path="exchangeUpdate" id="notificationSettingsForm_exchangeUpdate" />
			</td>
		</tr>
	</table>
	<br></br>
<c:if test="${notificationSettingsFormBean.projectId != -1}">								
				
	<table width="100%">
		<!-- //////////////////////////////////TEAM MEMBER/PROJECT DETAIL UPDATE////////////////////// -->
		<tr>
			<td class="labelTd" >
				<spring:message code="notification.settings.team.member.detail.update"/>
			</td>
			<td>
				<form:checkbox path="teamMemberDetailUpdate" id="notificationSettingsForm_teamMemberDetailUpdate" />
			</td>
			<td class="labelTd" >
				<spring:message code="notification.settings.project.detail.update"/>
			</td>
			<td>
				<form:checkbox path="projectDetailUpdate" id="notificationSettingsForm_projectDetailUpdate" />
			</td>
		</tr>
		<!-- //////////////////////////////////BUDGET/PERCENTAGE OVERFLOW////////////////////// -->
		<tr>	
			<td class="labelTd" >
				<spring:message code="notification.settings.percentage.overflow"/>
			</td>
			<td>
				<form:checkbox path="percentageOverflow" id="notificationSettingsForm_percentageOverflow" />
			</td>
			<td class="labelTd" >
				<spring:message code="notification.settings.budget.overflow"/>
			</td>
			<td>
				<form:checkbox path="budgetOverflow" id="notificationSettingsForm_budgetOverflow" />
			</td>
		</tr>		
	</table>

</c:if>		
<c:if test="${notificationSettingsFormBean.projectId == -1}">
	<table width="100%">
		<!-- //////////////////////////////////TEAM MEMBER/PERSON DETAIL UPDATE////////////////////// -->
		<tr>
			<td class="labelTd" >
				<spring:message code="notification.settings.person.detail.update"/>
			</td>
			<td>
				<form:checkbox path="personDetailUpdate" id="notificationSettingsForm_personDetailUpdate" />
			</td>
		</tr>
	</table>
</c:if>
<br></br>
</c:if>
</td></tr>
<tr>
	<td colspan="4" class="formActions" align="right">
		<c:if test="${notificationSettingsFormBean.projectId != -2 && ERRORS eq null}">	
			<input type="button" class="button" id="notificationSettingsForm_save"  value="<spring:message code="save"/>"/>					
		</c:if>
		<input type="button" class="button" id="notificationSettingsForm_cancel"  value="<spring:message code="cancel"/>" onclick="ENTITY = getNotificationSettingsData();check_add('NotificationSettings.htm', 'MAIN_CONTENT');" />						
					
	</td>
</tr>
</table>
</form:form>
</td></tr> </table>



<script>
	document.getElementById('notificationSettingsForm_projectId').focus();
	OLD_ENTITY = getNotificationSettingsData();
</script>

<c:if test="${notificationSettingsFormBean.projectId != -2 && ERRORS eq null}">	
	<script>
		//------------ SAVE SETTINGS ---------------------------------------------------------------

		var submitObject = new ObjSubmit("NotificationSettings.htm", "notificationSettingsForm", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("notificationSettingsForm_save", "click", validateAndSubmitForm, submitObject, true);

	</script>
</c:if>
<!-- //////////////////////////////////////////////////////////////////////////// -->
			</div><!-- end CONTENT div -->
		</td>
		<td id="afterContentCell"></td>
	</tr>
</table>
