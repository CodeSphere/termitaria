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
<%@ include file="./Taglibs.jsp" %>

<%
int currentPage = Integer.parseInt(request.getParameter("currentPage"));
int nbrOfPages = Integer.parseInt(request.getParameter("nbrOfPages"));

pageContext.setAttribute("currentPage", currentPage);
pageContext.setAttribute("nbrOfPages", nbrOfPages);
%>


<c:if test="${param.nbrOfPages != 0}">

<div id="cs_pagination">
	<table border="0"><tr>
		<c:if test="${currentPage > 1}">
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=first','paginationForm','${param.divResults}')"><img src="<spring:theme code="pagination.button.first"/>"/></a>
		</td>
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=prev','paginationForm','${param.divResults}')"><img src="<spring:theme code="pagination.button.prev"/>"/></a>
		</td>
		</c:if>
		<c:if test="${currentPage <= 1}">
		<td>
				<img src="<spring:theme code="pagination.button.firstDisabled"/>"/>
		</td>
		<td>
				<img src="<spring:theme code="pagination.button.prevDisabled"/>"/>
		</td>
		</c:if>


		<% for (int i = Integer.parseInt(request.getParameter("firstPage")); i <= Integer.parseInt(request.getParameter("lastPage")); i++){ %>
			<% if (Integer.parseInt(request.getParameter("currentPage")) == i){%>
		<td>
				<span class="number active"><%= i %></span>
		</td>
			<% } else {%>
		<td>
				<a href="#" class="number" onClick="submitForm('${param.baseURL}?action=pagination&page=nbr&pagenbr=<%= i %>','paginationForm','${param.divResults}')"><%= i %></a>
		</td>
			<% } %>
		<% } %>
		
		<c:if test="${currentPage < nbrOfPages}">
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=next','paginationForm','${param.divResults}')"><img src="<spring:theme code="pagination.button.next"/>"/></a>
		</td>
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=last','paginationForm','${param.divResults}')"><img src="<spring:theme code="pagination.button.last"/>"/></a>
		</td>
		</c:if>
		<c:if test="${currentPage >= nbrOfPages}">
		<td>
				<img src="<spring:theme code="pagination.button.nextDisabled"/>"/>
		</td>
		<td>
				<img src="<spring:theme code="pagination.button.lastDisabled"/>"/>
		</td>
		</c:if>
		<td>
			
		<select name="pagenbr" onchange="javascript:submitForm('${param.baseURL}?action=pagination&page=nbr','paginationForm','${param.divResults}')">
				<% for (int i = 1; i <= Integer.parseInt(request.getParameter("nbrOfPages")); i++){
					if (Integer.parseInt(request.getParameter("currentPage")) == i){%>
						<option value="<%= i %>" selected><%= i %>/${param.nbrOfPages} </option>
					<%} else {%>
						<option value="<%= i %>" ><%= i %>/${param.nbrOfPages} </option>
					<% } %>
				<% } %>
			</select>
		</td>
		</tr></table>
	</div>
</c:if>
