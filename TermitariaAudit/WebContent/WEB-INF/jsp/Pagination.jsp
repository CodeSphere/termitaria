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

<c:choose>
	<c:when test="${param.formName ne null}">
		<c:set var="form" value="${param.formName}" />
	</c:when>
	<c:otherwise>
		<c:set var="form" value="paginationForm" />
	</c:otherwise>
</c:choose>		
		
<div id="cs_pagination">
	<table border="0"><tr>
		<c:if test="${currentPage > 1}">
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=first','${form}','${param.divResults}')"><img src="themes/standard/images/buttons/first.jpg"/></a>
		</td>
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=prev','${form}','${param.divResults}')"><img src="themes/standard/images/buttons/prev.jpg"/></a>
		</td>
		</c:if>
		<c:if test="${currentPage <= 1}">
		<td>
				<img src="themes/standard/images/buttons/first_disabled.jpg"/>
		</td>
		<td>
				<img src="themes/standard/images/buttons/prev_disabled.jpg"/>
		</td>
		</c:if>


		<% for (int i = Integer.parseInt(request.getParameter("firstPage")); i <= Integer.parseInt(request.getParameter("lastPage")); i++){ %>
			<% if (Integer.parseInt(request.getParameter("currentPage")) == i){%>
		<td>
				<span class="number active"><%= i %></span>
		</td>
			<% } else {%>
		<td>
				<a href="#" class="number" onClick="submitForm('${param.baseURL}?action=pagination&page=nbr&pagenbr=<%= i %>','${form}','${param.divResults}')"><%= i %></a>
		</td>
			<% } %>
		<% } %>
		
		<c:if test="${currentPage < nbrOfPages}">
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=next','${form}','${param.divResults}')"><img src="themes/standard/images/buttons/next.jpg"/></a>
		</td>
		<td>
				<a href="#" class="imgButton" onClick="submitForm('${param.baseURL}?action=pagination&page=last','${form}','${param.divResults}')"><img src="themes/standard/images/buttons/last.jpg"/></a>
		</td>
		</c:if>
		<c:if test="${currentPage >= nbrOfPages}">
		<td>
				<img src="themes/standard/images/buttons/next_disabled.jpg"/>
		</td>
		<td>
				<img src="themes/standard/images/buttons/last_disabled.jpg"/>
		</td>
		</c:if>
		<td>
	
		<select  onchange="javascript:submitForm('${param.baseURL}?action=pagination&page=nbr&pagenbr=' + this.value,'${form}','${param.divResults}')">
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
