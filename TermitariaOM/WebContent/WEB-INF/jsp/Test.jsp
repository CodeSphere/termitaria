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
Test.jsp<hr/>
<br/>

<a href="#" id="Butonul_1">Buton 1</a>
<a href="#" id="Butonul_2">Buton 2</a>
<a href="#" id="Butonul_3">Buton 3</a>

<br/>

<%int randomNumberId = (int)(Math.random() * 10000 % 1000);%>
RandomNumberId = <%=randomNumberId%>
<%if (randomNumberId % 2 == 0)  {%>
	<a href="#" id="problematicul">[Problematicul]</a>
<%} else {%>
	[Problematicul]
<%} %>
<hr/>
<script>
	
	getObject = new ObjSubmit("Am apasat butonul 1", "", "MAIN_CONTENT");
	alert('addListener to Butonul_1: ' + YAHOO.util.Event.addListener("Butonul_1", "click", pressButton, getObject, true));

	getObject = new ObjSubmit("Am apasat butonul 2", "", "MAIN_CONTENT");
	alert('addListener Butonul_2: ' + YAHOO.util.Event.addListener("Butonul_2", "click", pressButton, getObject, true));

	getObject = new ObjSubmit("Am apasat butonul 3 randomId = <%=randomNumberId%>", "", "MAIN_CONTENT");
	alert('addListener Butonul_3: ' + YAHOO.util.Event.addListener("Butonul_3", "click", pressButton, getObject, true));

	getObject = new ObjSubmit("Am apasat Problematicul 3 randomId = <%=randomNumberId%>", "", "MAIN_CONTENT");
	alert('addListener problematicul: ' + YAHOO.util.Event.addListener("problematicul", "click", pressButton, getObject, true));
	
</script>
