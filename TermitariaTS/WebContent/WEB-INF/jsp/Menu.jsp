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
<%@include file="Taglibs.jsp"%>     
<c:set var="display" value="TableCell"/>
<img src="<spring:theme code="menu_left"/>" style="position:absolute; left:0px; top:0px" />
	<table id="menuTable" border="0" cellpadding="0" cellspacing="0">
		<tr>
       		<td>
       			<img src="<spring:theme code="menu_tab_left"/>"/>
       		</td>
       		
       		<td class="first" >
       			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">
	 	 			<a href="#" id="home_id"><img src="images/buttons/home.png"/></a>
	       		</security:authorize>
	       	</td>
	       	
       		<td class="menuDisplay${display}" >
       			<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">
	 	 			<a href="#" id="records"><spring:message code="record"/></a>
	       		</security:authorize>
	       	</td>
       		      	
	       	<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">      
					<a href="#" id="projects"><spring:message code="project"/></a>
				</security:authorize>
			</td>
			
			<!--  
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">      
					<a href="#" id="activity"><spring:message code="activity"/></a>
				</security:authorize>
			</td>
			-->
			<!--   
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">      
					<a href="#" id="costSheets"><spring:message code="costsheet"/></a>
				</security:authorize>
			</td> 
			-->
			<!-- 
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencySearch}">      
					<a href="#" id="currencies"><spring:message code="currency"/></a>
				</security:authorize>
			</td>
			 -->
			 <!--  
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">      
					<a href="#" id="exchanges"><spring:message code="exchange"/></a>
				</security:authorize>
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">
					<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
						<a href="#" id="exchanges"><spring:message code="exchange"/></a>
					</c:if>
				</security:authorize>
			</td>  
			-->
			<!--  
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">      
					<a href="#" id="person"><spring:message code="person"/></a>
				</security:authorize>
			</td>
			-->
			
			<td class="menuDisplay${display}">
				<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_NotificationReceive}">
					<a href="#" id="notification"><spring:message code="notification"/></a> 	   
				</security:authorize>
	       		<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_NotificationReceive}">
	       			<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
						<a href="#" id="notification"><spring:message code="notification"/></a> 
					</c:if>      
				</security:authorize>
			</td>      
			
			<td class="menuDisplay${display}">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ReportsView}">      
					<a href="#" id="reports"><spring:message code="reports"/></a>
				</security:authorize>
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ReportsView}">   
					<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">   
						<a href="#" id="reports"><spring:message code="reports"/></a>
					</c:if>
				</security:authorize>
			</td>
			
			<td class="menuDisplayTableCell hasSubmenu" id="menu">
	       		<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ReportsView}">      
					<a href="#" id="settings"><spring:message code="settings"/></a>
					<ul class="menuTableSubmenu"  id="main_submenu_settings">
						<li><a href="#" id="currencies"><spring:message code="currency"/></a></li>
						<li><a href="#" id="notification_settings"><spring:message code="notification"/></a></li>			
					</ul>
				</security:authorize>
				<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ReportsView}">   
					<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">   
						<a href="#" id="settings"><spring:message code="settings"/></a>
					</c:if>
				</security:authorize>
				
			</td>
			
	       	<td>
	       		<img src="<spring:theme code="menu_tab_right"/>" />
	       	</td>
		</tr>
	</table>


<script>

	var getObject = null;
	
	//--------------------------------------------------- < Home >
    <security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
    	getObject = new ObjSubmit("Home.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("home_id", "click", getContentFromUrl, getObject, true);
   	</security:authorize>
	
	//--------------------------------------------------- < Records >
    <security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
		getObject = new ObjSubmit("RecordSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("records", "click", getContentFromUrl, getObject, true);
   	</security:authorize>

	//--------------------------------------------------- < Projects >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
		getObject = new ObjSubmit("ProjectSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("projects", "click", getContentFromUrl, getObject, true);	
	</security:authorize>

	//--------------------------------------------------- < Activity >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
		getObject = new ObjSubmit("ActivitySearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("activity", "click", getContentFromUrl, getObject, true);	
	</security:authorize>

	//--------------------------------------------------- < Persons >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
		getObject = new ObjSubmit("PersonSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("person", "click", getContentFromUrl, getObject, true);	
	</security:authorize>	

	//--------------------------------------------------- < Notifications >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
		getObject = new ObjSubmit("SearchNotification.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("notification", "click", getContentFromUrl, getObject, true);	
	</security:authorize>	
	
	//--------------------------------------------------- < Notifications Settings >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
		getObject = new ObjSubmit("NotificationSettings.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("notification_settings", "click", getContentFromUrl, getObject, true);	
	</security:authorize>

	//--------------------------------------------------- <Cost Sheets>
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_Basic}">  
		getObject = new ObjSubmit("CostSheetSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("costSheets", "click", getContentFromUrl, getObject, true);	
	</security:authorize>	

	//--------------------------------------------------- <Currencies>
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_CurrencySearch}">  
		getObject = new ObjSubmit("CurrencySearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("currencies", "click", getContentFromUrl, getObject, true);	
	</security:authorize>	

	//--------------------------------------------------- <Exchange>
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">  
		getObject = new ObjSubmit("ExchangeSearch.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("exchanges", "click", getContentFromUrl, getObject, true);	
	</security:authorize>	
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ExchangeSearchAll}">  
		<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
			getObject = new ObjSubmit("ExchangeSearch.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("exchanges", "click", getContentFromUrl, getObject, true);	
		</c:if>
	</security:authorize>	

	//--------------------------------------------------- < Reports >
	<security:authorize ifAllGranted="${PERMISSION_CONSTANT.TS_ReportsView}">  
		getObject = new ObjSubmit("ReportProject.htm", "", "MAIN_CONTENT");
		YAHOO.util.Event.addListener("reports", "click", getContentFromUrl, getObject, true);	
	</security:authorize>
	<security:authorize ifNotGranted="${PERMISSION_CONSTANT.TS_ReportsView}">   
		<c:if test="${IS_PM_FOR_AT_LEAST_ONE_PROJECT eq true}">
			getObject = new ObjSubmit("ReportProject.htm", "", "MAIN_CONTENT");
			YAHOO.util.Event.addListener("reports", "click", getContentFromUrl, getObject, true);	
		</c:if>
	</security:authorize>  
	
	 
	

	
	
	var menuListing = {

		init : function() {	  
			menu = YAHOO.util.Dom.getElementsByClassName('menuDisplayTableCell hasSubmenu');
			submenu = YAHOO.util.Dom.getElementsByClassName('menuTableSubmenu');

			YAHOO.util.Event.addListener(menu, 'mouseover', menuListing.roll);
			YAHOO.util.Event.addListener(menu, 'mouseout', menuListing.out);
			YAHOO.util.Event.addListener(submenu, 'mouseout', menuListing.out);
		},

		roll : function() {	  
			menu = YAHOO.util.Dom.getElementsByClassName('menuTableSubmenu');
			YAHOO.util.Dom.addClass(menu, 'menuTableSubmenuHover');
		}, 
			  
		out : function() {	  
			meniu = YAHOO.util.Dom.getElementsByClassName('menuTableSubmenu');
			YAHOO.util.Dom.removeClass(menu, 'menuTableSubmenuHover');
		}
	};

	YAHOO.util.Event.on(window, 'load', menuListing.init);

</script>
