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

<div id="person_picture">
	<img src="servlet/ImageServlet?picture=ANONIM"/>
</div>
<div style="text-align: center"><a href="#"
	onClick="uploadPhoto('PersonUploadPicture.htm?personId=${personId}', 'PICTURE_UPLOAD_CONTENT', '<spring:message code="person.upload.photo"/>')">
	<spring:message code="person.upload.photo"/></a>
</div>	
