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
<%@page
	import="org.apache.taglibs.standard.lang.jstl.test.PageContextImpl"%>
<%@page import="org.springframework.util.StringUtils"%>
<%@page import="ro.cs.om.web.security.UserAuth"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page
	import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@include file="Taglibs.jsp"%>

<head>
<title><spring:message code="title" /></title>

<link rel="shortcut icon" href="images/favicon/favicon.png">

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

	<!-- JavaScript Section -->
	<script type="text/javascript" src="js/cs/cs_common.js"></script>

	<!-- CSS Section -->
	<link rel="stylesheet" type="text/css"
		href="<spring:theme code="styleSheet"/>" />
	<script>
		whichBrsVersion();
	</script>
	<link rel="stylesheet" type="text/css"
		href="<spring:theme code="styleSheet.calendar"/>" />
	<link rel="stylesheet" type="text/css"
		href="<spring:theme code="styleSheet.fonts-min"/>" />
	<link rel="stylesheet" type="text/css"
		href="<spring:theme code="styleSheet.container"/>" />
	<link rel="stylesheet" type="text/css"
		href="<spring:theme code="styleSheet.button"/>" />
	<link rel="stylesheet" type="text/css"
		href="<spring:theme code="styleSheet.treeview"/>" />
	<link rel="stylesheet" type="text/css"
		href="<spring:theme code="styleSheet.autoComplete"/>" />


	<%
		Locale locale = (Locale) session
				.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		String language = (locale != null ? locale.getLanguage() : "en");
		pageContext.setAttribute("locale", locale, PageContext.PAGE_SCOPE);
	%>
	<script type="text/javascript">
		//------------------------------------------INTERNATIONALISATION---------------------------------
		
		var param = "<%=language%>";
		var $T = YAHOO.plugin.Translator;
		switch (param) {
		case 'ro':
			$T.set('COMMON', language_ro);
			break;
		case 'en':
			$T.set('COMMON', language_en);
			break;
		}
		//------------------------------------------END INTERNATIONALISATION-----------------------------
	</script>
</head>

<!-- DD: Body has this class because without it yui panels' css won't work -->
<body class="yui-skin-sam">

<div id="top">
	<a id="logo"><img src="images/logos/logo.jpg"/></a>
	<div id="topmenu">
	<a href="/Termitaria/OM/" id="settings"><spring:message code="login"/></a>
	</div>	
</div>

<!-- ============================================================================================================== -->
	<!-- This script has to be inside body because otherwise the tooltips won't work -->
	<script type="text/javascript" src="js/wz/wz_tooltip.js"></script>

	<!-- DD: This div has to be outside div with id=page, in order to work completly as a modal dialog on
both IE and Mozilla -->
	<!--This script has to be inside body -->
	<script>
		// OM application namespace for YUI widgets 
		YAHOO.namespace("om");

		function init() {
			// ------------- small loading ------------------------------------------------------------------------------
			if (!YAHOO.om.smallLoading) {
				// The Loading panel
				YAHOO.om.smallLoading = new YAHOO.widget.Panel("smallLoading",
						{
							width : "35px",
							x : 0,
							y : 3,
							close : false,
							draggable : false,
							zindex : 1,
							modal : true,
							visible : false,
							underlay : false
						});

				YAHOO.om.smallLoading
						.setBody("<img src=\"images/ajaxLoader/ajax-loader_snake.gif\"/>");
				YAHOO.om.smallLoading.render(document.body);
			}

			// ------------- expired session ------------------------------------------------------------------------

			// ------------- tree update ------------------------------------------------------------------------
			if (!YAHOO.om.orgTreeUpdating) {
				// The Loading panel
				YAHOO.om.orgTreeUpdating = new YAHOO.widget.Panel(
						"OrganisationTreeUpdatingPanel", {
							width : "200px",
							fixedcenter : true,
							draggable : false,
							zindex : 1,
							modal : true,
							close : false,
							visible : false,
							underlay : false
						});
				YAHOO.om.orgTreeUpdating
						.setBody('<spring:message code="organization.tree.updating.pleaseWait"/>');
				YAHOO.om.orgTreeUpdating.render(document.body);
			}
		}

		// -------------OOO CALENDAR CONTAINERS ------------------------------------------------------------------------------

		// -------------OOO SEARCH START CALENDAR ------------------------------------------------------------------------------
		if (!YAHOO.om.oooSearchStartPanelContainer) {
			// The Loading panel
			if (whichBrs() == 'Internet Explorer') {
				YAHOO.om.oooSearchStartPanelContainer = new YAHOO.widget.Panel(
						"oooSearchStartPanelContainer", {
							width : "150px",
							height : "169px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zIndex : 1,
							modal : true,
							visible : false,
							underlay : false
						});
			} else {
				YAHOO.om.oooSearchStartPanelContainer = new YAHOO.widget.Panel(
						"oooSearchStartPanelContainer", {
							width : "163px",
							height : "170px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zindex : 1,
							modal : true,
							visible : false,
							underlay : false
						});
			}
			YAHOO.om.oooSearchStartPanelContainer.render(document.body);
		}

		// -------------OOO SEARCH END CALENDAR ------------------------------------------------------------------------------
		if (!YAHOO.om.oooSearchEndPanelContainer) {
			// The Loading panel
			if (whichBrs() == 'Internet Explorer') {
				YAHOO.om.oooSearchEndPanelContainer = new YAHOO.widget.Panel(
						"oooSearchEndPanelContainer", {
							width : "150px",
							height : "169px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zIndex : 2,
							modal : true,
							visible : false,
							underlay : false
						});
			} else {
				YAHOO.om.oooSearchEndPanelContainer = new YAHOO.widget.Panel(
						"oooSearchEndPanelContainer", {
							width : "163px",
							height : "170px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zindex : 2,
							modal : true,
							visible : false,
							underlay : false
						});
			}
			YAHOO.om.oooSearchEndPanelContainer.render(document.body);
		}

		// -------------OOO START CALENDAR ------------------------------------------------------------------------------
		if (!YAHOO.om.oooStartPanelContainer) {
			// The Loading panel
			if (whichBrs() == 'Internet Explorer') {
				YAHOO.om.oooStartPanelContainer = new YAHOO.widget.Panel(
						"oooStartPanelContainer", {
							width : "150px",
							height : "169px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zIndex : 1,
							modal : true,
							visible : false,
							underlay : false
						});
			} else {
				YAHOO.om.oooStartPanelContainer = new YAHOO.widget.Panel(
						"oooStartPanelContainer", {
							width : "163px",
							height : "170px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zIndex : 1,
							modal : true,
							visible : false,
							underlay : false
						});
			}
			YAHOO.om.oooStartPanelContainer.render(document.body);
		}

		// -------------OOO END CALENDAR ------------------------------------------------------------------------------
		if (!YAHOO.om.oooEndPanelContainer) {
			// The Loading panel
			if (whichBrs() == 'Internet Explorer') {
				YAHOO.om.oooEndPanelContainer = new YAHOO.widget.Panel(
						"oooEndPanelContainer", {
							width : "150px",
							height : "169px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zIndex : 2,
							modal : true,
							visible : false,
							underlay : false
						});
			} else {
				YAHOO.om.oooEndPanelContainer = new YAHOO.widget.Panel(
						"oooEndPanelContainer", {
							width : "163px",
							height : "170px",
							x : 0,
							y : 0,
							close : false,
							draggable : false,
							zIndex : 2,
							modal : true,
							visible : false,
							underlay : false
						});
			}
			YAHOO.om.oooEndPanelContainer.render(document.body);
		}

		init();
	</script>

	</body>
	</html>
	
