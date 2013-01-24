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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html> 
<head> 
<meta http-equiv="content-type" content="text/html; charset=utf-8"> 
<title>Basic Example</title> 

<style type="text/css">
    body {
        margin:0;
    	padding:0;
    }
    #paginated { 
2	    text-align: center; 
3	} 
4	#paginated table { 
5	    margin-left:auto; margin-right:auto; 
6	} 
7	#paginated .yui-pg-container a { 
8	    color: #00d; 
9	} 
10	#paginated .yui-pg-pages a { 
11	    text-decoration: underline; 
12	} 
13	#paginated, #paginated .yui-dt-loading { 
14	    text-align: center; background-color: transparent; 
15	} 
</style> 

<link rel="stylesheet" type="text/css" href="yui/css/fonts-min.css" /> 
<link rel="stylesheet" type="text/css" href="yui/css/datatable.css" />

<script type="text/javascript" src="yui/yahoo-dom-event.js"></script>
<script type="text/javascript" src="yui/element-beta-min.js"></script>
<script type="text/javascript" src="yui/datasource-beta-min.js"></script>
<script type="text/javascript" src="yui/datatable-beta-min.js"></script>
<script type="text/javascript" src="yui/json-min.js"></script>
<script type="text/javascript" src="yui/connection-min.js"></script>

<style type="text/css">
	.yui-skin-sam .yui-dt-liner { 
		white-space:nowrap;
	} 
</style> 

</head> 
 
<body class="yui-skin-sam"> 
 
<h1>Basic Example</h1> 
 
<div class="exampleIntro"> 
	<p>A demonstration of the DataTable's basic feature set.</p>			
</div> 
 
<div id="paging"></div> 
<div id="dt"></div

<script type="text/javascript"> 

YAHOO.util.Event.addListener(window, "load", function() {
    YAHOO.example.Basic = new function() {
        var myColumnDefs = [
		    {key:"name", label:"Dog's Name"},
		    {key:"breed", label:"Dog's Breed"},
		    {key:"age", label:"Dog's Age (in Weeks)"}
		];

		var myDataSource = new YAHOO.util.DataSource("http://localhost:8080/OM/Test.htm?action=getData&");
	    myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        myDataSource.responseSchema = {
        	resultsList: "Results",
        	fields: [ 
		        { key: "name" }, 
		        { key: "breed" }, 
		        { key: "age" } 
		    ],
		    metaFields : { 
	            totalRecords: 'totalRecords' 
	        }
        };
        
        // A custom function to translate the js paging request into a query 
		// string sent to the XHR DataSource 
		var buildQueryString = function (state,dt) { 
		    return "startIndex=" + state.pagination.recordOffset + 
		           "&results=" + state.pagination.rowsPerPage; 
		};
		
		// Set up the Paginator instance.   
		var myPaginator = new YAHOO.widget.Paginator({
		    pageLinks          : 3, 
		    rowsPerPage        : 5, 
		    rowsPerPageOptions : [5,10,15],
		    template           : "{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink} {RowsPerPageDropdown}"
		});
		
		var myTableConfig = { 
		    initialRequest         : 'startIndex=0&results=5', 
		    generateRequest        : buildQueryString, 
		    paginator              : myPaginator, 
		    paginationEventHandler : YAHOO.widget.DataTable.handleDataSourcePagination 
		};
        
        var myDataTable = new YAHOO.widget.DataTable("dt", myColumnDefs, myDataSource, myTableConfig);
        
        var callback1 = {
            success: myDataTable.onDataReturnInitializeTable,
            failure: myDataTable.onDataReturnAppendRows,
            scope  : myDataTable
        };
        myDataSource.sendRequest("", callback1);
    };
});
</script>

</body> 
</html>
