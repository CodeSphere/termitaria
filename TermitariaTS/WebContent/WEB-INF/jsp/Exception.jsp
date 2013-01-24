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
<div style="color: #D8000C; background-color: #FFBABA; background-image: url('images/messages/error.png'); border: 1px solid; margin: 10px 0px; padding:15px 10px 15px 50px; background-repeat: no-repeat; background-position: 10px center;">
<table style="font-family:Verdana, Arial, Helvetica, sans-serif; font-size: 11px;">
<tr><td style="font-size: 12px; font-weight: bold;">
Exception
</td></tr>
<tr>
	<%Exception ex = (Exception) request.getAttribute("exception");%>
	<td>Class: <%=ex.getClass()%></td>
</tr>
<tr>
	<td>Message: <%=ex.getMessage()%></td>
</tr>
</table>
</div>
