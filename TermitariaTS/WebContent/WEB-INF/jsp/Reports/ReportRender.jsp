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

<html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<head>
	<title><spring:message code="create.report"/></title>
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet"/>"/>
	<link rel="stylesheet" type="text/css" href="<spring:theme code="styleSheet.container"/>" />
	<script type="text/javascript" src="js/cs/cs_common.js"></script>
</head>

<!-- DD: Body has this class because without it yui panels' css won't work -->
<body class="yui-skin-sam">
<div id="page" style="display:none">
	<div id="toolbarDiv">
		<table class="reportActionsToolbar_table">
			<tr>
				<td>
					<a href="${REQUEST_URL}&attachment=true" title="<spring:message code='download' />"><img src="images/buttons/action_save.png"/></a> 
				</td>
				<td>
					<a href="javascript:manageDisplayReport('${REQUEST_URL}', 'MAIN_CONTENT', 'page')" title="<spring:message code='refresh'/>"><img src='images/buttons/action_refresh.png'/></a>
				</td>
			</tr>
		</table>
	</div>
	<div id="MAIN_CONTENT" class="reportWindowMainContent">
	</div>
</div>


<!--This script has to be inside body -->
<script>

	// Audit application namespace for YUI widgets
	YAHOO.namespace("ts");

	function init() {
		// ------------- small loading ------------------------------------------------------------------------------
		
		if (!YAHOO.ts.smallLoading) {
	        // The Loading panel
	        YAHOO.ts.smallLoading = new YAHOO.widget.Panel("smallLoading", { width: "30px", x: 0, y: 3, close: false, 
	                                                  		 draggable: false, zindex: 1, modal: true,
	                                                  		 visible: false, underlay: false});	      
	        YAHOO.ts.smallLoading.setBody("<img src=\"images/ajaxLoader/ajax-loader_snake.gif\"/>");
		    YAHOO.ts.smallLoading.render(document.body);
    	}

		// ------------- wait while creating report -----------------------------------------------------------------
		
    	if (!YAHOO.ts.creatingReport) {
		    // The Loading panel
		    YAHOO.ts.creatingReport = new YAHOO.widget.Panel("CreatingReportPanel", { width: "200px", fixedcenter:true,  
		                                              		 draggable: false, zindex:1, modal: true, close: false,
		                                              		 visible: false, underlay: false});
		    YAHOO.ts.creatingReport.setBody('<spring:message code="report.creating.pleaseWait"/>');
		    YAHOO.ts.creatingReport.render(document.body);
		}
	}
	

    init();
    
    if ("${FORMAT}" == "pdf") {
        YAHOO.ts.smallLoading.show();
        YAHOO.ts.creatingReport.show();
        window.location = "${REQUEST_URL}";
    } else {
    	manageDisplayReport("${REQUEST_URL}", "MAIN_CONTENT", "page");
    }    
	
</script>



</body>
</html>
