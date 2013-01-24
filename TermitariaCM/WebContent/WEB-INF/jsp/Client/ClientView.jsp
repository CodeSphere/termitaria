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
<div class="yuiInfoPanel">
<%@ include file="../Taglibs.jsp" %>
<%@ include file="../Messages.jsp" %>

<c:if test="${!(empty CLIENT)}">
	<table style="width:100%">
		<!-- CLIENT TYPE -->
		<tr>
			<td class="labelInfoTd">
				<spring:message code="client.type"/>
			</td>	
			<td>
				<spring:message code="client.type.${CLIENT.type}"/>
			</td>
		</tr>
		
			<!-- FIRM CLIENT C_NAME -->
			<tr class="firmClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.firm.c_name"/>
				</td>
				<td>
					${CLIENT.c_name}
				</td>
			</tr>
			
			<!-- PERSON CLIENT NAME -->
			<tr class="personClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.name"/>
				</td>
				<td>
					${CLIENT.p_firstName} ${CLIENT.p_lastName}
				</td>
			</tr>
			
			<!-- PERSON CLIENT SEX -->
			<tr class="personClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.person.p_sex"/>
				</td>
				<td>
					${CLIENT.p_sex}
				</td>
			</tr>
			
			<!-- PERSON CLIENT BIRTH DATE -->
			<tr class="personClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.person.p_birthdate"/>
				</td>
				<td>
					<fmt:formatDate value="${CLIENT.p_birthDate}" dateStyle="long"/>
				</td>
			</tr>
			
			<!-- CLIENT ADDRESS -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="client.address"/>
				</td>
				<td>
					<c:if test="${(CLIENT.address != '') && !(empty CLIENT.address) }">
						${CLIENT.address}
					</c:if>
				</td>
			</tr>
			
			<!-- CLIENT PHONE -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="client.phone"/>
				</td>
				<td>
					<c:if test="${(CLIENT.phone != '') && !(empty CLIENT.phone) }">
						${CLIENT.phone}
					</c:if>
				</td>
			</tr>
			
			<!-- CLIENT FAX -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="client.fax"/>
				</td>
				<td>
					<c:if test="${(CLIENT.fax != '') && !(empty CLIENT.fax) }">
						${CLIENT.fax}
					</c:if>
				</td>
			</tr>
			
			<!-- CLIENT DESCRIPTION -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="client.description"/>
				</td>
				<td>
					<c:if test="${(CLIENT.description != '') && !(empty CLIENT.description) }">
						${CLIENT.description}
					</c:if>
				</td>
			</tr>
			
			<!-- FIRM CLIENT C_CUI -->
			<tr class="firmClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.firm.c_cui"/>
				</td>
				<td>
					<c:if test="${(CLIENT.c_cui != '') && !(empty CLIENT.c_cui) }">
						${CLIENT.c_cui}
					</c:if>
				</td>
			</tr>
			
			<!-- FIRM CLIENT C_IBAN -->
			<tr class="firmClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.firm.c_iban"/>
				</td>
				<td>
					<c:if test="${(CLIENT.c_iban != '') && !(empty CLIENT.c_iban) }">
						${CLIENT.c_iban}
					</c:if>
				</td>
			</tr>
			
			<!-- FIRM CLIENT C_CAPITAL -->
			<tr class="firmClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.firm.c_capital"/>
				</td>
				<td>
					<c:if test="${(CLIENT.c_capital != '') && !(empty CLIENT.c_capital) }">
						${CLIENT.c_capital}
					</c:if>
				</td>
			</tr>
			
			<!-- FIRM CLIENT C_LOCATION -->
			<tr class="firmClientInfo">
				<td class="labelInfoTd">
					<spring:message code="client.firm.c_location"/>
				</td>
				<td>
					<c:if test="${(CLIENT.c_location != '') && !(empty CLIENT.c_location) }">
						${CLIENT.c_location}
					</c:if>
				</td>
			</tr>
			
			<!-- CLIENT OBSERVATION -->
			<tr>
				<td class="labelInfoTd">
					<spring:message code="client.observation"/>
				</td>
				<td>
					<c:if test="${(CLIENT.observation != '') && !(empty CLIENT.observation) }">
						${CLIENT.observation}
					</c:if>
				</td>
			</tr>
	</table>
	
<script>
	displayPersonOrFirmClientFields(${CLIENT.type}, 'firmClientInfo', 'personClientInfo');
</script>

</c:if>
</div>
