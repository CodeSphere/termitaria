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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form method="post" id="personPictureUploadForm" enctype="multipart/form-data">
		<input type="hidden" name="personId" value="${personId}"/> 
	<table align="center">
		<tr>
			<td class="existent_picture_container">
				<div id="ACTUAL_PERSON_PICTURE_CONTENT">
					<c:choose>
						<c:when test="${pictureId > 0}">
							<img src="servlet/ImageServlet?picture=${pictureId}"/>
						</c:when>
						<c:otherwise>
							<img src="<spring:theme code="no_person_picture"/>"/>	
						</c:otherwise>
					</c:choose>
				</div>
			</td>
		</tr>
		<tr>
		 	<td>
				<input type="button" class="button" onClick="upload('PersonUploadPicture.htm', 'personPictureUploadForm', 'ACTUAL_PERSON_PICTURE_CONTENT')" value="<spring:message code="upload"/>"/>
				<spring:bind path="pictureBean.file">
					<input type="file" name="${status.expression}" value="${status.value}" accept="image/jpeg"/>
				</spring:bind>
				<input type="button" class="button" onClick="applyChangePersonsPhoto('PersonRenderPicture.htm?personId=${personId}', 'PICTURE_CONTENT')" value="<spring:message code="apply"/>"/>		
			</td>
		</tr>
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td height="5px"></td>
		</tr>
		<tr>
			<td colspan="2">
				<spring:message code="person.upload.photo.alt"/>
			</td>
		</tr>
	</table>

</form>
