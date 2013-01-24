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
<%@ include file="../Messages.jsp" %>

<c:choose>
	<c:when test="${!(empty FREEDAYS)}">
		<fieldset style="width: 600px;">
			<legend><spring:message code="freeday.title.listing" /></legend>
				<table class="list_results" >
					<tr class="list_results_header">		
						<th style="width:150px"><spring:message code="freeday.day" /></th>
						<th style="width:450px"><spring:message code="freeday.observation" /></th>	
						<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_FreedayUpdate}, ${PERMISSION_CONSTANT.OM_FreedayDelete}">
							<th style="width:120px"><spring:message code="freeday.actions" /></th>
						</security:authorize>					
					</tr>		
					<c:set var="i" value="0"/>
					<c:set var="cssClass" value=""/>
					
					<c:forEach var="freeDay" items="${FREEDAYS}">
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
						<td align="center">
							<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_FreedayUpdate}">
								<a href="#" onClick="manageFreeday('FreeDay.htm?action=edit&freedayId=${freeDay.freeDayId}', '<spring:message code="freeday.edit.title"/>');" title="<spring:message code="freeday.edit"/>">
									<fmt:formatDate value="${freeDay.day}" dateStyle="long"/>
								</a>
							</security:authorize>
							<security:authorize ifNotGranted="${PERMISSION_CONSTANT.OM_FreedayUpdate}">
								<fmt:formatDate value="${freeDay.day}" dateStyle="long"/>
							</security:authorize>
						</td>
						<c:choose>
								<c:when test="${fn:length(freeDay.observation) ne 0}">
									<td align="center" onmouseover="Tip('${freeDay.tokenizedObservation}', BGCOLOR, getCSSRule('.toolTipBody').style.backgroundColor, FONTWEIGHT, getCSSRule('.toolTipBody').style.fontWeight, FONTCOLOR, getCSSRule('.toolTipBody').style.color, BORDERCOLOR, getCSSRule('.toolTipBody').style.borderColor, FADEIN, 800, FADEOUT, 800);") onmouseout="UnTip();">
										${freeDay.truncatedTokenizedObservation}
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										${freeDay.truncatedTokenizedObservation}
									</td>
								</c:otherwise>
							</c:choose>	
						<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_FreedayUpdate}, ${PERMISSION_CONSTANT.OM_FreedayDelete}">
							<td align="center">	
								<table class="actionsTable" align="center" cellpadding="0" cellspacing="0">
									<tr>									
										<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_FreedayUpdate}">		
											<td>						
												<a href="#" onClick="manageFreeday('FreeDay.htm?action=edit&freedayId=${freeDay.freeDayId}', '<spring:message code="freeday.edit.title"/>');" title="<spring:message code="freeday.edit"/>"><img src="images/buttons/action_edit.png"/></a>
											</td>
										</security:authorize>
																				
										<security:authorize ifAnyGranted="${PERMISSION_CONSTANT.OM_FreedayDelete}">
											<td>					
												<a href="#" onClick="deleteFreedayWithConfirmation('FreeDayListing.htm?action=delete&freedayId=${freeDay.freeDayId}', 'calendarForm','FREEDAYS', '<spring:message code="freeday.delete.confirmationMessage"/>', '<spring:message code="confirm.delete"/>');" title="<spring:message code="freeday.delete"/>">																																				
												<img src="images/buttons/action_delete.png"/></a>
											</td>
										</security:authorize>																					
									</tr>
								</table>									
							</td>		
						</security:authorize>
						</tr>						
					</c:forEach>
				</table>
		</fieldset>		
	</c:when>
	<c:otherwise>
		<div class="noFindResults"><spring:message code="calendar.freeday.no.results"/></div>
	</c:otherwise>
</c:choose>	
<br/></br>
