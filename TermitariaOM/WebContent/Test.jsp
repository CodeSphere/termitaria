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
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>


<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>

<title><spring:message code="title"/></title>

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<!-- CSS Section -->
	<link rel="stylesheet" type="text/css" href="themes/standard/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="themes/standard/css/yui/calendar.css"/>
	<link rel="stylesheet" type="text/css" href="themes/standard/css/yui/fonts-min.css" />
	<link rel="stylesheet" type="text/css" href="themes/standard/css/yui/container.css" />
	<link rel="stylesheet" type="text/css" href="themes/standard/css/yui/button.css" />
	<link rel="stylesheet" type="text/css" href="themes/standard/css/yui/treeview.css" />
	<link rel="stylesheet" type="text/css" href="themes/standard/css/yui/autocomplete.css" />

<!-- JavaScript Section -->
	<script type="text/javascript" src="js/cs/cs_common.js"></script>

	<script type="text/javascript">
		//------------------------------------------INTERNATIONALISATION---------------------------------
		var param = "en";
		var $T = YAHOO.plugin.Translator;
			switch(param){
				case 'ro':
					$T.set ('COMMON', language_ro);
					break;	
				case 'en':
					$T.set ('COMMON', language_en);
					break;	
			}
		//------------------------------------------END INTERNATIONALISATION-----------------------------
	</script>

</head>
<body class="yui-skin-sam">

<!--This script has to be inside body -->
<script>

	// OM application namespace for YUI widgets
	YAHOO.namespace("om");

	function init() {
		// ------------- small loading ------------------------------------------------------------------------------
		if (!YAHOO.om.smallLoading) {
	        // The Loading panel
	        YAHOO.om.smallLoading = new YAHOO.widget.Panel("SmallLoadingPanel", { width: "30px", x: 0, y: 3, close: false, 
	                                                  		 draggable: false, zindex: 1, modal: false,
	                                                  		 visible: false, underlay: false});
	        YAHOO.om.smallLoading.setBody("<img src=\"images/ajaxLoader/ajax-loader_snake.gif\"/>");
	        YAHOO.om.smallLoading.render(document.body);
    	}
		// ------------- expired session ------------------------------------------------------------------------
		if (!YAHOO.om.sessionExpired) {
		    // The Loading panel
		    YAHOO.om.sessionExpired = new YAHOO.widget.Panel("SessionExpiredPanel", { width: "200px", fixedcenter:true,   
		                                              		 draggable: false, zindex:1, modal: true, close: false,
		                                              		 visible: false, underlay: false});
		    YAHOO.om.sessionExpired.setBody("Session has expired. Redirecting... <img border=\"0\" src=\"images/ajaxLoader/ajax-loader-session-expired.gif\"/>");
		    YAHOO.om.sessionExpired.render(document.body);
		}
	}
	YAHOO.util.Event.addListener(window, "load", init);

</script>

<br/><br/>

<a href="#" id="testFunction" onClick="getContentFromUrlDirect('Test.htm', 'MAIN_CONTENT')">Test Function</a>

<div id="MAIN_CONTENT"></div>
<script>
	//submitObject = {url: "Test.htm", container: "", panelTitle: 'Person Add new Testing purposes'};
	//YAHOO.util.Event.addListener("testFunction", "click", testFunction, submitObject, true);
		
	//For automatic loading the first search results
</script>
</body>
</html>
