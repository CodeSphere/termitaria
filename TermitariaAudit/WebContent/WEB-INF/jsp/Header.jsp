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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.Locale"%>
<%@page import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>

<%@page import="org.springframework.util.StringUtils"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@include file="Taglibs.jsp" %>

<head>
	<title><spring:message code="title"/></title>

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

	<link rel="shortcut icon" href="images/favicon/favicon.png" >


<!-- CSS Section -->
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet"/>"/>
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.calendar"/>"/>
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.fonts-min"/>" />
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.container"/>" />
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.button"/>" />
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.autoComplete"/>" />
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.treeview"/>" />
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.paginator"/>" />
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.datatable"/>" />
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.tabview"/>" />

<!-- JavaScript Section -->
	<script type="text/javascript" src="js/cs/cs_common.js"></script>
	<%Locale locale = (Locale) session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
	  String language = (locale != null ? locale.getLanguage() : "en");
	  pageContext.setAttribute("locale", locale, PageContext.PAGE_SCOPE);
	%>
	<script type="text/javascript">
		//------------------------------------------INTERNATIONALISATION---------------------------------
		var param = '<%=language%>'; 
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

<!-- DD: Body has this class because without it yui panels' css won't work -->
<body class="yui-skin-sam">

<!-- DD: This div has to be outside div with id=page, in order to work completly as a modal dialog on
both IE and Mozilla -->
<!--This script has to be inside body -->
<script>

	// Audit application namespace for YUI widgets
	YAHOO.namespace("audit");

	function init() {
		// ------------- small loading ------------------------------------------------------------------------------
		if (!YAHOO.audit.smallLoading) {
	        // The Loading panel
	        YAHOO.audit.smallLoading = new YAHOO.widget.Panel("smallLoading", { width: "35px", x: 0, y: 3, close: false, 
	                                                  		 draggable: false, zindex: 1, modal: true,
	                                                  		 visible: false, underlay: false});

	        YAHOO.audit.smallLoading.setBody("<img src=\"images/ajaxLoader/ajax-loader_snake.gif\"/>");
	        YAHOO.audit.smallLoading.render(document.body);
    	}

		// ------------- wait while creating report -----------------------------------------------------------------
		
    	if (!YAHOO.audit.creatingReport) {
		    // The Loading panel
		    YAHOO.audit.creatingReport = new YAHOO.widget.Panel("CreatingReportPanel", { width: "200px", fixedcenter:true,   
		                                              		 draggable: false, zindex:1, modal: true, close: false,
		                                              		 visible: false, underlay: false});
		    YAHOO.audit.creatingReport.setBody('<spring:message code="report.creating.pleaseWait"/>');
		    YAHOO.audit.creatingReport.render(document.body);
		}
		// ------------- expired session ------------------------------------------------------------------------
		if (!YAHOO.audit.sessionExpired) {
			// The Loading panel
		    YAHOO.audit.sessionExpired = new YAHOO.widget.Panel("SessionExpiredPanel", { width: "200px", fixedcenter:true,   
		                                              		 draggable: false, zindex:1, modal: true, close: false,
		                                              		 visible: false, underlay: false});
		    YAHOO.audit.sessionExpired.setBody('<spring:message code="session.expired"/> <img border=\"0\" src=\"images/ajaxLoader/ajax-loader-session-expired.gif\"/>');
		    YAHOO.audit.sessionExpired.render(document.body);
		}

		// ------------- calendar container ------------------------------------------------------------------------------
		
		if (!YAHOO.audit.calendarContainer) {
	        // The Loading panel
	        
	        var _width = "150px";
	        var _height = "161px"; 

	        //special case for IE
	        if(whichBrs() == 'Internet Explorer'){
		        _width = "152px";
		        _height = "163px";
	        }
	        
	        YAHOO.audit.calendarContainer = new YAHOO.widget.Panel("calendarContainer", {zIndex:200, width: _width, height: _height, x: 0, y: 0, close: false, 
	                                                  		 draggable: false, modal: true,
	                                                  		 visible: false, underlay: true});
	      
	        YAHOO.audit.calendarContainer.render(document.body);
    	}
	
	}

	YAHOO.util.Event.addListener(window, "load", init);

	init();
	
</script>
	<div id="page">
    	<div id="top">
        	 <span><img src="<spring:theme code="logo"/>"/></span>
			<span id="topmenu">
            	<a href="${OM_MODULES}" id="modules" class="first"><spring:message code="modules"/></a>
            	<a href="#"><spring:message code="help"/></a>
            	<a href="j_spring_security_logout"><spring:message code="signout"/></a>
            </span><!-- end div: topmenu -->	
            <span id="userAndOrganizationSpecInfo">
				<%@include file="UserAndOrganizationSpecInfo.jsp"%>
			</span>		
        </div><!-- end div: top -->
       <div id="menu">
			<%@include file="Menu.jsp"%>
	   </div>
