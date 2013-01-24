/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
/**
 * HISTORY:
 * To be completed when a stable version of the file is modified.
*/

//========================================================================================================
/**
 * COMMON METHODS
 */

//========================================================================================================
/**
 * Loads all the JavaScript
 */
//CS
document.write('\n\t<script type="text/javascript" src="cs/cs_globals.js"></script>');

//YUI
document.write('\n\t<script type="text/javascript" src="js/yui/yahoo-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/event-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/connection-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/translator.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/yahoo-dom-event.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/datasource-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/dragdrop-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/json-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/utilities.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/container-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/element-beta-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/treeview-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/button-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/animation-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/dom-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/autocomplete-min.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/yui/calendar-min.js"></script>');

document.write('\n\t<script type="text/javascript" src="js/data.js"></script>');

//CS
document.write('\n\t<script type="text/javascript" src="js/cs/cs_globals.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/cs/cs_business.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/cs/cs_formValidateAndSubmit.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/cs/cs_messages_ro.js"></script>');
document.write('\n\t<script type="text/javascript" src="js/cs/cs_messages_en.js"></script>');


//========================================================================================================
/**
 * Handles the exired session event
 * @author mitziuro
 * @return
 */
function sessionExpired(oResponse){
	 if (YAHOO.ts.sessionExpired && oResponse.responseText == SESSION_EXPIRED){
		YAHOO.ts.sessionExpired.show();
		setTimeout('window.location="SignOn.htm"', 1500); 
	}
}

//========================================================================================================

/**
 * Displays the content received from server into a div.
 * 
 * @author matti_joona
 */
function getContentFromUrl(_url, _divId){
	var url = this.url != null ? this.url : _url;
	var divId = this.container != null ? this.container: _divId;
	if (this.withConfirmation && YAHOO.ts.confirmation){
		//The panel it's hard coded as it cannot be
		//passed as a parameter. 
		YAHOO.ts.confirmation.hide();
	}

	var loadObject = { 
		handleSucess:function (o){
			if(o.responseText !== undefined){
				sessionExpired(o);
				var divElem = document.getElementById(divId);
				divElem.innerHTML = o.responseText;
				//this is for executing javascripts elements
				YAHOO.ts.smallLoading.hide();
				
				var scriptTags = divElem.getElementsByTagName('SCRIPT');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				
				if (this.displayLocalMessages) {
					addFadeEffectOnBox('localMessages', 4);
				}
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.ts.smallLoading.hide();

	    },
	    startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', url, callbackForLoad); 
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show Small Loading
	YAHOO.ts.smallLoading.show();
	//start transaction
	loadObject.startRequest();
}

//========================================================================================================
	
 /**
  * The same as getContentFromUrl, only that it receives params.
  * Displays the content received from server into a div.
  * 
  * @author dd
  */
function getContentFromUrlDirect(url, container){

	var loadObject = { 
		handleSucess:function (o){
			if(o.responseText !== undefined){
				sessionExpired(o);
				var divElem = document.getElementById(container);
				divElem.innerHTML = o.responseText; 
				YAHOO.ts.smallLoading.hide();
				//this is for executing javascripts elements
				var scriptTags = divElem.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
					 
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.ts.smallLoading.hide();
		},
		startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', url, callbackForLoad); 
		}
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show Small Loading
	YAHOO.ts.smallLoading.show();
	loadObject.startRequest();
}

//========================================================================================================

/**
 * Sets a YUI Panel's body to display the content received form server.
 * 
 * @author dd
 */
function returnContentFromUrlToPanelAndShowPannel(url,panel){
	var loadObject = { 
		handleSucess:function (o){
			if(o.responseText !== undefined){
				sessionExpired(o);
				panel.setBody(o.responseText);
				panel.render(document.body);
				
				panel.show();
				
				var browser = whichBrs();
				if (browser == "Internet Explorer" || browser == "Chrome" || browser == "Safari" || browser == "Opera" ) {
					//for some browsers we need to evaluate the response
					var scriptTags = panel.body.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
				}
									
				YAHOO.ts.smallLoading.hide();
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.ts.smallLoading.hide();
	    },
	    startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', url, callbackForLoad); 
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show the Panel
	YAHOO.ts.smallLoading.show();
    loadObject.startRequest();
}

//========================================================================================================
/**
 * Selects all options in a select.
 * field parameter it's document.<form name>.<check box name>
 * 
 * @author dd
 * @param field
 * @return
 */
function selectAll(field) {
	for (var i = 0; i < field.options.length; i++)
		field.options[i].selected = 'selected';
}

//========================================================================================================
/**
 * Selects all check boxes in a form.
 * field parameter it's document.<form name>.<check box name>
 * 
 * @author dd
 * @param field
 * @return
 */
function unselectAll(field) {
	for (var i = 0; i < field.length; i++)
		field[i].selected = false ;
}
//========================================================================================================
/**
 * Uploads a file using Connection Manager
 * (based on information form this website http://thecodecentral.com/2007/09/04/asynchronous-file-upload-yuis-approach)
 * @author dd
 * 
 * @param _url the url where the uploaded file it's handled
 * @param _formId form's Id contiang the file input (enctype="multipart/form-data")
 * @param _responseContainer div containg the response from server
 */
function upload(_url, _formId, _responseContainer) {
	//this method is called when upload button is clicked
	YAHOO.util.Connect.setForm(_formId, true);
	var uploadHandler = {
		
		//handle upload case. This function will be invoked after file upload is finished.
		upload: function(response) {
			var divElem = document.getElementById(_responseContainer);
			divElem.innerHTML = response.responseText;
		}
	};
	YAHOO.util.Connect.asyncRequest('POST', _url, uploadHandler);
}

//========================================================================================================
/**
 * Handles the "select all/ deselect all" checkbox from the listitngs
 * 
 * @author dd
 * @author alu
 */
function selectAllCheckbox() {
	var checkboxId = this.checkboxId;
	var ids = this.ids;
	var table = this.table;
	if (document.getElementById(checkboxId).checked == true) {
		checkAll(ids);
		selectAllRows(table);
	} else {
		uncheckAll(ids);
		unselectAllRows(table);
	}
}


function selectAllRows(table) {
	for(var i=1; i<table.rows.length;i++) {
		for(var t=0;t<table.rows[i].childNodes.length;t++){
			if(table.rows[i].childNodes[t].nodeName == 'TD'){
				for(var x=0;x<table.rows[i].childNodes[t].childNodes.length;x++){
					if(table.rows[i].childNodes[t].childNodes[x].type == 'checkbox'){
						table.rows[i].className = "selected_row";
					}
				}
			}
		}
	}
}

function unselectAllRows(table) {
	var id;
	for(var i=1; i<table.rows.length;i++) {
		if (table.rows[i].id % 2 == 0) {
			table.rows[i].className = "even_row";	
		} else {
			table.rows[i].className = "odd_row";
		}
		
		
	}
}

//========================================================================================================
/**
 * Selects all check boxes in a form.
 * field parameter it's document.<form name>.<check box name>
 * 
 * @author dd
 * @param field
 * @return
 */
function checkAll(field) {
	if (field.length != undefined) {
		for (var i = 0; i < field.length; i++)
			field[i].checked = true ;
	}  else {
		field.checked = true; 
	}
}

//========================================================================================================
/**
 * Selects all check boxes in a form.
 * field parameter it's document.<form name>.<check box name>
 * 
 * @author dd
 * @param field
 * @return
 */
function uncheckAll(field) {
	if (field.length != undefined) {
		for (var i = 0; i < field.length; i++)
			field[i].checked = false ;
	} else {
		field.checked = false; 
	}
}

//========================================================================================================
/**
 * Displays client date and time using one of the defined locales.
 * Currently it has only ro and en locales.
 * 
 * @author dan.damian
 * 
 * @param divId the Div where the clock will be displayed
 * @param locale the Locales depending on the date will be formatted
 * @return
 */ 
 function Clock(divId, locale, AmPm, showClock) {
		
		this.divId = divId;
		this.locale = locale;
		this.AmPm = AmPm;
		this.showClock = showClock;
		
		
		//Locale EN Months of the Year
		this.en_months = new Array(12);
		this.en_months[0]  = "January";
		this.en_months[1]  = "February";
		this.en_months[2]  = "March";
		this.en_months[3]  = "April";
		this.en_months[4]  = "May";
		this.en_months[5]  = "June";
		this.en_months[6]  = "July";
		this.en_months[7]  = "August";
		this.en_months[8]  = "September";
		this.en_months[9]  = "October";
		this.en_months[10] = "November";
		this.en_months[11] = "December";
		
		//Locale EN Days of the Week
		this.en_dayOfTheWeek = new Array(7);
		this.en_dayOfTheWeek[0] = "Sunday";	
		this.en_dayOfTheWeek[1] = "Monday";
		this.en_dayOfTheWeek[2] = "Tuesday";
		this.en_dayOfTheWeek[3] = "Wednesday";
		this.en_dayOfTheWeek[4] = "Thursday";
		this.en_dayOfTheWeek[5] = "Friday";
		this.en_dayOfTheWeek[6] = "Saturday";
			
		//Locale RO Months of the Year
		this.ro_months = new Array(12);
		this.ro_months[0]  = "Ianuarie";
		this.ro_months[1]  = "Februarie";
		this.ro_months[2]  = "Martie";
		this.ro_months[3]  = "Aprilie";
		this.ro_months[4]  = "Mai";
		this.ro_months[5]  = "Iunie";
		this.ro_months[6]  = "Iulie";
		this.ro_months[7]  = "August";
		this.ro_months[8]  = "Septemberie";
		this.ro_months[9]  = "Octombrie";
		this.ro_months[10] = "Noiembrie";
		this.ro_months[11] = "Decemberie";
		
		//Locale RO Days of the Week
		this.ro_dayOfTheWeek = new Array(7);
		this.ro_dayOfTheWeek[0] = "Duminica";	
		this.ro_dayOfTheWeek[1] = "Luni";
		this.ro_dayOfTheWeek[2] = "Marti";
		this.ro_dayOfTheWeek[3] = "Miercuri";
		this.ro_dayOfTheWeek[4] = "Joi";
		this.ro_dayOfTheWeek[5] = "Vineri";
		this.ro_dayOfTheWeek[6] = "Sambata";   	
			
		
		
		
		
		//Defining Functions
		this.update = update;
		
		
		/**
		 * Update function - updates a div with the current time and date.
		 * @return
		 */
		function update() {
			
			//The main Date Object
			var d = new Date();
			var c_hour 			= d.getHours();
			var c_min 			= d.getMinutes();
			var c_sec			= d.getSeconds();
			var c_month 		= d.getMonth();
			var c_dayOfWeek		= d.getDay();
			var c_dayOfMonth 	= d.getDate();
			var c_year			= d.getFullYear();
			
			//Formatted current Time
			var formattedTime = "";
			
			if (this.AmPm) {
				var ap = "AM";
					if (c_hour   > 11) { ap = "PM";             }
					if (c_hour   > 12) { c_hour = c_hour - 12;      }
					if (c_hour   == 0) { c_hour = 12;             }
					if (c_hour   < 10) { c_hour   = "0" + c_hour;   }
					if (c_min < 10) { c_min = "0" + c_min; }
					if (c_sec < 10) { c_sec = "0" + c_sec; }
					formattedTime = c_hour + ':' +  c_min + ':' + c_sec + " " + ap;
			} else {
				if (c_hour   < 10) { c_hour   = "0" + c_hour;   }
				if (c_min < 10) { minute = "0" + c_min; }
				if (c_sec < 10) { second = "0" + c_sec; }
				formattedTime = c_hour + ':' +  c_min + ':' + c_sec;
			}	
			
			//Formatted current Date
			var formattedDate = "";
			
			if (this.locale.toUpperCase() == "EN") {
				formattedDate = this.en_dayOfTheWeek[c_dayOfWeek] + ", " + this.en_months[c_month] + " " + c_dayOfMonth  + ", " + c_year;
			} else if (this.locale.toUpperCase() == "RO") {
				formattedDate = this.ro_dayOfTheWeek[c_dayOfWeek] + ", " + c_dayOfMonth + " " + this.ro_months[c_month] + ", " + c_year;
			}
			
			var time = formattedDate;
			if (this.showClock) {
				time = time + " " + formattedTime;
			}
			
			document.getElementById(this.divId).innerHTML = time; 
		}
	} 
 

//========================================================================================================
/**
 * Checking what browser it is
 * @author mitziuro
 */
function whichBrs() {
	var agt=navigator.userAgent.toLowerCase();
	if (agt.indexOf("opera") != -1) return 'Opera';
	if (agt.indexOf("firefox") != -1) return 'Firefox';
	if (agt.indexOf("chrome") != -1) return 'Chrome';
	if (agt.indexOf("safari") != -1) return 'Safari';
	if (agt.indexOf("msie") != -1) return 'Internet Explorer';
	if (agt.indexOf("netscape") != -1) return 'Netscape';
	if (agt.indexOf("mozilla/5.0") != -1) return 'Mozilla';
	return "";
}

/*
addEvent function from http://www.quirksmode.org/blog/archives/2005/10/_and_the_winner_1.html
*/
function addEvent( obj, type, fn )
{
	if (obj.addEventListener)
		obj.addEventListener( type, fn, false );
	else if (obj.attachEvent)
	{
		obj["e"+type+fn] = fn;
		obj[type+fn] = function() { obj["e"+type+fn]( window.event ); }
		obj.attachEvent( "on"+type, obj[type+fn] );
	}
}

function removeEvent( obj, type, fn )
{
	if (obj.removeEventListener)
		obj.removeEventListener( type, fn, false );
	else if (obj.detachEvent)
	{
		obj.detachEvent( "on"+type, obj[type+fn] );
		obj[type+fn] = null;
		obj["e"+type+fn] = null;
	}
}

/*
Create the new window
*/
function openInNewWindow() {
	alert("open");
	// Change "_blank" to something like "newWindow" to load all links in the same new window
	alert("this.getAttribute('href') = " + document.getElementById("fileName").getAttribute('href'));
    var newWindow = window.open("", '_blank');    
    alert('dfasdfasd');
    return false;
}

/*
Add the openInNewWindow function to the onclick event of links with a class name of "new-window"
*/
function getNewWindowLinks() {	
	// Check that the browser is DOM compliant
	if (document.getElementById && document.createElement && document.appendChild) {
		// Change this to the text you want to use to alert the user that a new window will be opened
		var strNewWindowAlert = " (opens in a new window)";
		// Find all links
		var links = document.getElementsByTagName('a');
		var objWarningText;
		var strWarningText;
		var link;
		for (var i = 0; i < links.length; i++) {
			link = links[i];				
				// Create an em element containing the new window warning text and insert it after the link text
				objWarningText = document.createElement("em");
				strWarningText = document.createTextNode(strNewWindowAlert);
				objWarningText.appendChild(strWarningText);
				link.appendChild(objWarningText);
				link.onclick = openInNewWindow;			
		}
		objWarningText = null;
	}
}

//addEvent(window, 'load', getNewWindowLinks);

//========================================================================================================
/**
 * Calculates the coordinate for yui according to 
 * browser type
 * @author mitziuro
 */
function Coordinate(_x, _y) {
	
	var browserType = whichBrs();
	var result = new Object();
	result.x = _x;
	result.y = _y;
	
	if(browserType == "Firefox") {
		result.x = _x;
		result.y = _y;
	}
	if(browserType == "Internet Explorer") {
		result.x = _x - 37;
		result.y = _y - 30;
	}
	if(browserType == "Chrome") {
		result.x = _x;
		result.y = _y - 30;
	}
	if(browserType == "Safari") {
		result.x = _x +7;
		result.y = _y - 28;
	}
	if(browserType == "Opera") {
		result.x = _x - 4;
		result.y = _y - 38;
	}
	return result;
}

//YUI MODULE JAVASCRIPT PAGINATOR START

/**
 * Setting the array of notification modules on the context
 * @author mitziuro
 */
function moduleArrayObject(module_array){
	this.module_array = module_array;
}
/**
 * Creating pagination for an array of yui modules
 * @author mitziuro
 * @param module_array
 * @param divId the html element where the pagination will come
 * @param selectId the div where the select will come
 * @return
 */
function setYUIModulePagination(module_array, divId, selectId){
	
	var selectedModules = []; 
	
	//creating the select
	var select = '<select id="pagenbr" onchange="selectPageFromPagination(this.selectedIndex)">';
	
	//how many lines we display
	var select_lines = (Math.floor(module_array.length / (nbResultsPerPage))) + ((module_array.length % (nbResultsPerPage)) == 0 ? 0 : 1);
	
	//we create the options
	for(i=1;i<=select_lines;i++){
		select += '<option value="' + i +'">' + i +'/' + select_lines +'</option>';
	}
	
	//we close the select
	select += '</select>';
	document.getElementById(selectId).innerHTML = select;

	//creating the paginator
	var paginator = new YAHOO.widget.Paginator({
	    totalRecords : module_array.length,
	    containers : [divId],
	    pageLinks: nbOfPagesPerPanel - 1,
	    rowsPerPage: nbResultsPerPage, 
		firstPageLinkLabel : "&nbsp&nbsp&nbsp&nbsp",
	    previousPageLinkLabel : "&nbsp&nbsp&nbsp&nbsp",
	    nextPageLinkLabel :"&nbsp&nbsp&nbsp&nbsp",
	    lastPageLinkLabel : "&nbsp&nbsp&nbsp&nbsp"
	});
	
	//first time render
	 var handlePagination = function (state) {
	    // Gather the content for the requested page
	    var startIndex = state.recordOffset,
	        recs = module_array.slice(startIndex, startIndex + state.rowsPerPage);
		
	    //set the correct option in the select element
	    document.getElementById('pagenbr').options[(startIndex/nbResultsPerPage)].selected = true;
	    
	  //hide all modules
	    for(i = 0; i < selectedModules.length ;i++){
	    	selectedModules[i].hide();
	    }
	    
	  //show the ones in the page
	    for(i = 0; i<recs.length ;i++){
		    recs[i].show();
		    selectedModules[i] = recs[i];
	    }
	    
	    // Confirm state change with the Paginator
	    paginator.setState(state);
	};
	
	paginator.subscribe('changeRequest', handlePagination);

	paginator.render();

	// To populate the list initially, call the handler directly passing
	// the Paginator's current state
	handlePagination(paginator.getState());
	
	return paginator;
}

/**
 * function for changing the current page of the yui paginator from the context
 * @author mitziuro
 * @param number
 * @return
 */
function selectPageFromPagination(pageNumber){
	//the paginator it's hardcoded
	var paginator = YAHOO.ts.notificationPaginator;
	//the page number it's always 1 higher then the selected option index
	paginator.setPage(pageNumber + 1,false);
}

//YUI MODULE JAVASCRIPT PAGINATOR END

function selectUnSelectRow(row, checkboxes, selectAllCheckBox) {
	
	var mainCheckBox = null;
	var end = false;
	
	//we check if this row has a check box on the first column (if it hasn't we do nothing)
	for(var i=0; i<row.childNodes.length && !end; i++){
		if(row.childNodes[i].nodeName == 'TD' || row.childNodes[i].nodeName == 'td'){
			for(var j=0; j<row.childNodes[i].childNodes.length && !end; j++){
				if(row.childNodes[i].childNodes[j].nodeName == 'INPUT' || row.childNodes[i].childNodes[j].nodeName == 'input'){
					if(row.childNodes[i].childNodes[j].type != 'HIDDEN' && row.childNodes[i].childNodes[j].type != 'hidden'){
						end = true;
					} else {
						return;
					}
				}
			}
		}
	}
	
	//we take the row list
	var rowList = row.parentNode.childNodes;
	
	//we take the main checkbox element
	var tBody = row.parentNode;
	var thList = null;
	var thCheckBox = null;
	end = false;
	
	for(var i=0; i<rowList.length && !end; i++){ 
		if(rowList[i].nodeName == 'TR' || rowList[i].nodeName == 'tr'){
			thList = rowList[i].childNodes;
			for(var j=0; j<thList.length && !end; j++){ 
				if(thList[j].nodeName == 'TH' || thList[j].nodeName == 'th'){
					thCheckBox = thList[j].childNodes;
					for(var h=0; h<thCheckBox.length && !end; h++){
						if(thCheckBox[h].nodeName == 'INPUT' || thCheckBox[h].nodeName == 'input'){
							mainCheckBox = thCheckBox[h];
							end = true;
						}
					}
				}
			}
		}
	}
							
	if (checkboxes != undefined) {
		var checkBox;
		
		if (checkboxes.length != undefined) {
			checkBox = checkboxes[row.id - 1];
		} else {
			checkBox = checkboxes;
		}
		
		if (checkBox.checked) {
			checkBox.checked = false;
			if (row.id % 2 == 0) {
				row.className = 'even_row';
			} else {
				row.className = 'odd_row';
			}
			selectAllCheckBox.checked=false;
		} else {
			checkBox.checked = true;
			row.className = 'selected_row';
		}
		
		var allSelected = true;
		var atLeastOne = false;
		
		var goNext = false;
		var tdList  = null;
		var tdCheckBox = null;
		
		//test if all checkboxes are selected
		for(var i=0; i<rowList.length; i++){ 
			if(rowList[i].nodeName == 'TR' || rowList[i].nodeName == 'tr'){
				tdList = rowList[i].childNodes;
				for(var j=0; j<tdList.length && !goNext; j++){ 
					if(tdList[j].nodeName == 'TD' || tdList[j].nodeName == 'td'){
						tdCheckBox = tdList[j].childNodes;
						for(var h=0; h<tdCheckBox.length && !goNext; h++){
							if(tdCheckBox[h].nodeName == 'INPUT' || tdCheckBox[h].nodeName == 'input'){
								if(tdCheckBox[h].type != 'hidden' && tdCheckBox[h].type != 'HIDDEN'){
									if(!tdCheckBox[h].checked){
										allSelected = false;
									} 
									atLeastOne = true;
								} 
								
								goNext = true;
							}
						}
					}
				}
				
				//we go to the next row
				goNext = false;
			}
		}
		
		if(allSelected && atLeastOne){
			//if all the checkboxes are selected we check the main check box
			mainCheckBox.checked = true;
		} else {
			//we uncheck the checkbox
			mainCheckBox.checked = false;
		}
	}	
} 

function cancelCheckEvent(e) {
	if (e.checked) {
		e.checked = false;
	} else {
		e.checked = true;
	}
}
function hover(tr) {
	if (whichBrs() == 'Internet Explorer' && tr.className != 'selected_row') {
		tr.className = "hover_tr";
	}
}

function changeToOldStyle(tr) {
	if (whichBrs() == 'Internet Explorer' && tr.className != 'selected_row') {
		if (tr.id % 2 == 0) {
			tr.className = 'even_row';
		} else {
			tr.className = 'odd_row';
		}
	}
}


/**
 * Setting the calendar for the availability date
 * @author mitziuro
 * @param _divId
 * @param targetId
 * @param todayDate
 * @return
 */
function instantiateDocumentAvailabilityCalendar(_divId, targetId, todayDate){
	
	var day = todayDate.substring(0,2);
	var month = todayDate.substring(3,5);
	var year = todayDate.substring(6,10);
	
	YAHOO.ts.availabilityCalendar = new YAHOO.widget.Calendar("availabiltyCalendar", _divId ,{title: "DOCUMENTFORM_CALENDAR_TITLE".translate('COMMON') ,close:true, mindate: month + "/" + day + "/" + year});
	
	//function for handling select date
	function selectHandler(type,args,obj) {
		var date = args[0][0];
		document.getElementById(targetId).value = date[2] + '-' +date[1] + '-' + date[0];
		YAHOO.ts.availabilityCalendar.hide();
	};
	
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
	YAHOO.ts.availabilityCalendar.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
	YAHOO.ts.availabilityCalendar.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
	
	YAHOO.ts.availabilityCalendar.selectEvent.subscribe(selectHandler, YAHOO.ts.availabilityCalendar, true); 
	YAHOO.ts.availabilityCalendar.render();
	YAHOO.ts.availabilityCalendar.show();
}
 
 /**
  * Set the parameters for the calendar
  * @author mitziuro
  * @param locale 'RO' or 'EN'
  * @return
  */
function getCalendarParametersForLocale(locale){
	var setting_array = [];
	if(locale == 'RO'){
		setting_array[0] = ["Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"];
		setting_array[1] = ["Du", "Lu", "Ma", "Mi", "Jo", "Vi", "Sa"];
	}
		 
	if(locale == 'EN'){
			setting_array[0] = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
			setting_array[1] = ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"];
	}
		 
	return setting_array;
}
  
var endHours = "";
var endMinutes = "";
  
/**
* Function for hiding a div if the display style is block
* 
* @author Adelina
*/

function hideMessageDiv(divClass)
{	
	var hellos = getElementsByClass(divClass, null, "div");		
	var newDisplay;	
		
	for(var i = 0; i < hellos.length; i++){
		if(hellos[i].style.display == 'block')
		{			
			newDisplay = 'none';
			hellos[i].style.display = newDisplay;		
		} 
	}
}

//========================================================================================================

/**
* Function for showing a div
* 
* @author Adelina
*/

function showMessageDiv(divClass)
{			
	var hellos = getElementsByClass(divClass, null, "div");		
	var newDisplay;	
		
	for(var i = 0; i < hellos.length; i++){

		if(hellos[i].style.display == 'none')
		{			
			newDisplay = 'block';
			hellos[i].style.display = newDisplay;		
		} 
	}	
}

//========================================================================================================

/**
 * Function for getting a class from a div
 * 
 * @author Adelina
 */
  
function getElementsByClass(searchClass, domNode, tagName) {
	if (domNode == null) domNode = document;
 	if (tagName == null) tagName = '*';
 	var el = new Array();
 	var tags = domNode.getElementsByTagName(tagName);
 	var tcl = " "+searchClass+" ";
 	for(i=0,j=0; i<tags.length; i++) {
 		var test = " " + tags[i].className + " ";
 		if (test.indexOf(tcl) != -1)
 		el[j++] = tags[i];
 	}
 	return el;
 }

/**
 * Restricts the input value to the max float number with no more than 4 decimals
 * @author Coni
 * @param inputId
 * @return
 */
function restrictToFloatNumber(inputId) {
	var valueTillNow = document.getElementById(inputId).value;
	if (valueTillNow != "") {
		valueTillNow = parseFloat(valueTillNow);
		if (valueTillNow >= MAX_FLOAT) {
			var maxFloat = new String(MAX_FLOAT);
			var exponent = parseInt(maxFloat.substring(maxFloat.indexOf("+") + 1));
			var decimalsNr = maxFloat.substring(maxFloat.indexOf(".") + 1, maxFloat.indexOf("e")).length;
			var value = maxFloat.substring(0, 1).concat(maxFloat.substring(maxFloat.indexOf(".") + 1, maxFloat.indexOf("e")));
			for (i = 0; i < exponent - decimalsNr; i++) {
				value = value.concat("0");
			}
			document.getElementById(inputId).value = value;
			OLD_VALUE = value;
		} else {
			if (!document.getElementById(inputId).value.match(/^[0-9]+([.][0-9]{0,4})?$/)) {
				document.getElementById(inputId).value = OLD_VALUE;
			} else {
				OLD_VALUE = document.getElementById(inputId).value;
			}
		}
	}
}

/**
 * Restricts the Integer type values to the maximum Integer value
 * @author Coni
 * @param inputId
 * @return
 */
function restrictToMaxInteger(inputId) {
	var valueTillNow = document.getElementById(inputId).value;
	if (valueTillNow != "") {
		valueTillNow = parseInt(valueTillNow);
		if (valueTillNow > MAX_INTEGER) {
			document.getElementById(inputId).value = MAX_INTEGER;
		}
	}
}

/**
* Restricts the user to input special characters.
* The characters that are alowed are : . - _ a-zA-Z S/
* 
* @author Adelina
* 
* @param e
* @return
*/
function restrictSpecialCharacters(e)
{
	var key;
	var keychar;
	var check;	
	if(window.event) // IE
	{
		key = e.keyCode;
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		key = e.which;
	}
	keychar = String.fromCharCode(key);
	check = new RegExp(/[!@#$%^&*~`()\[\]+=<>{}?:,;/\"'\\|]/);
	return !check.test(keychar);
}

/**
* Restricts the user to input special characters.
* The characters that are alowed are : - a-zA-Z S/
* 
* @author Adelina
* 
* @param e
* @return
*/
function restrictSpecialCharactersForName(e)
{
	var key;
	var keychar;
	var check;	
	if(window.event) // IE
	{
		key = e.keyCode;
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		key = e.which;
	}
	keychar = String.fromCharCode(key);
	check = new RegExp(/[!@#$%^&*~`()\[\]+=<>{}?:._,;/\"'\\|]/);
	return !check.test(keychar);
}

/**
* The characters that are NOT alowed are : ~, <, >, ,\, |, /
* 
* @author Adelina
* 
* @param e
* @return
*/
function restrictSpecialCharactersForObs(e)
{
	var key;
	var keychar;
	var check;	
	if(window.event) // IE
	{
		key = e.keyCode;
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		key = e.which;
	}
	keychar = String.fromCharCode(key);
	check = new RegExp(/[~<>/\\|]/);
	return !check.test(keychar);
}

/**
* Restricts the user to input only numbers and '.', 
* 
* @author Adelina
* 
* @param e
* @return
*/
function restrictCharactersForReal(e)
{
	var keynum;
	var keychar;
	var numcheck;
	
	if(window.event) // IE
	{
		keynum = e.keyCode;
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		keynum = e.which;
	}
	keychar = String.fromCharCode(keynum);
	numcheck = new RegExp(/[a-zA-Z!@#$%^&*~`()\[\]+ =<>{}?:_,\-;/\"'\\|]/);
	return !numcheck.test(keychar);
}

/**
* Restricts the user to input only numbers usable on the onkeypress event
* 
* @author Adelina
* 
* @param e the event
* @param value the current field value
* @param the maximum length allowed
* @return
*/
function restrictCharactersOnlyForNumbers(e)
{
	var keynum;
	var keychar;
	var numcheck;	
	
	if(window.event) // IE
	{
		keynum = e.keyCode;
	}
	else if(e.which) // Netscape/Firefox/Opera
	{
		keynum = e.which;
	}
	keychar = String.fromCharCode(keynum);
	numcheck = new RegExp(/[a-zA-Z!@#$%^&*~`()\[\]+ =<>{}?:._,\-;/\"'\\|]/);
	return !numcheck.test(keychar);
}

/**
 * Assigns to the global variable OLD_PERCENT the the current percentage value in the field
 * Used for percentage validation
 * @param prc the percentage value of the field
 * @return
 */
function registerPercentage (prc){
	//check if percentage is ok
	if (validateStringPercentage (prc)){
	    OLD_RATE = prc;
	}
}

/**
 * Assigns to a field a previous value after a validation failure
 * @param field the field to be rolled back
 * @return
 */
function rollBackPercentage(field){
	field.value = OLD_RATE;
}



/**
* Validates a value of a field to be a percentage between 0 and 100 or ""
* if validation fails rollBackPercentage() is called
* 
* @author Alexandru Dobre
* 
* @param field the field to be validated
* @return
*/
function validatePercentage(field)
{
	var str = field.value;
	if (!validateStringPercentage (str)){
		rollBackPercentage(field);
	}
	
}

/**
* Validates a string to be a percentage between 0 and 100 or ""
* if validation fails rollBackPercentage() is called
* 
* @author Alexandru Dobre
* 
* @param string the string to be validated
* @return true if the string is valid, false otherwise 
*/
function validateStringPercentage (str){
	
	if (str.length ==0) return true ;
	//check if the string contains only numbers
	if (str.search(/^[0-9]+$/) != 0) {
		return false ;
	}
	var x = parseInt(str);
	if ( x < 0 || x > 100) {
		return false;
	}
	return true;
}


//========================================================================================================
/**
 *	This function is used for ordering the results of a search
 *
 * @author alu
 *  @param url The url used for submitting the sorting action
 *  @param newSortParam The ordering column
 *  @param oldSortParam The last column for which the ordering took place, or null if there was no ordering before
 *  @param sortDirection How was the last ordering: asc or desc
 *  @param formId The form that is submitted
 *  @param divId The div that contains the results
 */
function order(url, newSortParam, oldSortParam, sortDirection,formId, divId) {
		if (this.withContext){
			url = this.url;
			newSortParam = this.newSortParam;
			oldSortParam = this.oldSortParam;
			sortDirection = this.sortDirection;
			formId = this.formId;
			divId = this.divId;
		}
		// add to the url the two parameters from PaginationBean
		url = url + '&sortParam=' + newSortParam + '&sortDirection=';
		// set sortDirection
		if (oldSortParam == newSortParam) {
			url = url + (-sortDirection);
		} else {
			url = url + (-1);
		}
		submitForm(url, formId, divId);
	} 

/*
* It is used for displaying the manager autocomplete and setting the firstName and lastName in the form hidden fields
* @author adelina
*/

function instantiateManagerAutoComplete(oResults, inputId, autoCompleteContainer, managerNameHiddenFieldId){	
	// Use a LocalDataSource
	oDS = new YAHOO.util.LocalDataSource(oResults);	
    oDS.responseSchema = {fields : ["name", "id"]};
 
    oResults.sort(function (obj1, obj2) {
		return obj1.name < obj2.name ? -1 :
		(obj1.name > obj2.name ? 1 : 0);
		});
	    
    // Instantiate the AutoComplete 
    var oAC = new YAHOO.widget.AutoComplete(inputId, autoCompleteContainer, oDS); 
    oAC.resultTypeList = false; 
    
    // enable string input contained
    oAC.queryMatchContains = true;
    
    // Show more results, scrolling is enabled via CSS
    oAC.maxResultsDisplayed = 100;
    
    // Enable force selection 
	oAC.forceSelection = true;

	//oAC.prehighlightClassName = "yui-ac-prehighlight"; 
    oAC.useShadow = true; 
     
    // Define an event handler to populate a hidden form field 
    // when an item gets selected 
    var myManagerNameHiddenField = YAHOO.util.Dom.get(managerNameHiddenFieldId);      
    var myHandler = function(sType, aArgs) { 
        var myAC = aArgs[0]; // reference back to the AC instance 
        var elLI = aArgs[1]; // reference to the selected LI element 
        var oData = aArgs[2]; // object literal of selected item's result data 
         
        // update hidden form field with the selected item's ID
        myManagerNameHiddenField.value = oData.id;           
    }; 
    oAC.itemSelectEvent.subscribe(myHandler);
    
    YAHOO.util.Event.addListener(inputId, "click", function(){
    	setTimeout(function() { // For IE 
       	    oAC.sendQuery(" "); 
       	},0);
    });     
}

/*
* It is used for displaying the manager autocomplete and setting the firstName and lastName in the form hidden fields
* @author adelina
*/

function instantiateClientAutoComplete(oDS, inputId, autoCompleteContainer, clientNameHiddenFieldId){       
	 // Use a LocalDataSource
    oDS.responseSchema = {fields : ["name", "id"]};  
	       
    // Instantiate the AutoComplete 
	   
    var oConfigs = {
 		   useShadow: true,
 		   queryDelay: 0,
 		   minQueryLength: 0,
 		   animVert: .01
 		   };
    
    var oAC = new YAHOO.widget.AutoComplete(inputId, autoCompleteContainer, oDS, oConfigs); 
    oAC.resultTypeList = false; 
    
    // enable string input contained
    oAC.queryMatchContains = true;
    
    // Show more results, scrolling is enabled via CSS
    oAC.maxResultsDisplayed = 100;
    
    // Enable force selection 
	   oAC.forceSelection = false;
	   
    oAC.useShadow = true; 
    
    document.getElementById(inputId).focus();              
     
    // Define an event handler to populate a hidden form field 
    // when an item gets selected 
    var myClientNameHiddenField = YAHOO.util.Dom.get(clientNameHiddenFieldId);      
    var myHandler = function(sType, aArgs) { 
        var myAC = aArgs[0]; // reference back to the AC instance 
        var elLI = aArgs[1]; // reference to the selected LI element 
        var oData = aArgs[2]; // object literal of selected item's result data 
         
        // update hidden form field with the selected item's ID
        myClientNameHiddenField.value = oData.id;               
    }; 
    oAC.itemSelectEvent.subscribe(myHandler);
              
    YAHOO.util.Event.addListener(inputId, "click", function(){
 	oAC.getInputEl().focus();
    	setTimeout(function() { 
    		// For IE 
       	oAC.sendQuery(''); 
       	}, 1);
    });  
}

/**
* Changes the title of a div
* @author Adelina
* @param title
* @param divId
* @return
*/
function changeTitle(title, divId){		 				
	document.getElementById(divId).title = title;	
}

/**
 * Get the text color for the selected index option
 * 
 * @author Adelina
 * 
 * @param selectedIndex
 * @param select
 * @param select1
 * @return
 */
function getStyleTextColor(selectedIndex, select, select1){
	var sel = document.getElementById(select);
	var sel1 = document.getElementById(select1);		
    var elem = sel1.children[selectedIndex];   
    
    if (elem.currentStyle){//IE
    	return elem.currentStyle.color;
    }
	else if (document.defaultView && document.defaultView.getComputedStyle){ //Firefox
		return document.defaultView.getComputedStyle(elem, "").color;
	}
	else //try and get inline style
	  return elem.style.color;
}


/**
*	Used to open a panel containing a project
*	@author Adelina
*/
function displayInfoPanel(_url, _panelName, _panelTitle, _width_size) {
	var url = (this.url != null ? this.url : _url),
	panelName = (this.panelName != null ? this.panelName : _panelName),	
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),	
	width_size = (this.width_size != null ? this.width_size : _width_size);			
	
	if (YAHOO.ts.infoPanel != null){		
		YAHOO.ts.infoPanel.destroy();
	}
		
	YAHOO.ts.infoPanel = new YAHOO.widget.Panel("InfoPanel", { fixedcenter:true, width: width_size, visible: false, constraintoviewport: true, close: true, draggable:true,
		 effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},  modal: true, zindex:1, iframe: true});
	YAHOO.ts.infoPanel.setHeader('<span title=\'' + panelTitle +'\'>' + panelName + '</span>'); 	
	
	YAHOO.ts.infoPanel.render(document.body);
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.ts.infoPanel);
	
} 

/**
* Show row
* 
* @author Adelina
* 
* @param id
* @return
*/
function showRow(divId, projectId, billingPriceId, billingPriceCurrencyId, billingTimeUnitId) {				
	if(document.getElementById(billingPriceId) != null && document.getElementById(billingPriceCurrencyId) != null && document.getElementById(billingTimeUnitId) != null) {
		 document.getElementById(billingPriceId).style.display='none';		
		 document.getElementById(billingPriceCurrencyId).style.display='none'; 
		 document.getElementById(billingTimeUnitId).style.display='none'; 
	 }	
	 
	if(projectId != -1) {	
		document.getElementById(divId).style.display='block';		
	} else {		
		document.getElementById(divId).style.display='none';		
	}	
}

/**
 * Show billable
 * 
 * @author Adelina
 * 
 * @param projectId
 * @param billableId
 * @return
 */
function showBillable(projectId, billableId) {
	if(projectId < 0) {
		document.getElementById(billableId).style.display='none';	
	}
}


/**
 * Instantiates the start/end calendars for the availability date and time
 * @author mitziuro
 * @author alu
 * @author coni
 * 
 * @param _timeDivId
 * @param _targetId
 * @param _todayDate
 * @param _timePanel
 * @param _start
 * @param _location
 * @param _twin
 * @param showAllMinutes
 * @param pairNr
 * @return
 */
 function instantiateStartEndCalendars(_timeDivId, _targetId, _todayDate, _timePanel, _start, _location, _twin, showAllMinutes, pairNr){
 	
 	var timeDivId = this.timeDivId != null ? this.timeDivId : _timeDivId,
 		targetId = this.targetId != null ? this.targetId : _targetId,
 		todayDate = this.todayDate != null ? this.todayDate : _todayDate,
 		timePanel = this.timePanel != null ? this.timePanel : _timePanel,
 		start = this.start != null ? this.start : _start,
 		location = _location == null ? 'availability' : _location,
 		twin = _twin == null ? this.twin : _twin;
 	 	 	
 	var day = todayDate.substring(0,2);
 	var month = todayDate.substring(3,5);
 	var year = todayDate.substring(6,10);
 	
 	var today_date =  month + "/" + day + "/" + year; 
 	var today_date_for_calendar = today_date;
 	var today_date_for_compare = day + "/" + month + "/" + year; 
 	 	 	
 	if(targetId == "recordForm_startTime" || targetId == "recordForm_endTime") { 	 		
 		document.getElementById("recordForm_time").value = "";
 	}
 	
 	if(targetId == "recordForm_overTimeStartTime" || targetId == "recordForm_overTimeEndTime") { 	 		
 		document.getElementById("recordForm_overtimeTime").value = "";
 	}
 	 	
 	//set the local variables for start and end hour and minutes
 	var START_HOUR_CALENDAR = null;
 	var START_MINUTE_CALENDAR = null;
 	var END_HOUR_CALENDAR = null;
 	var END_MINUTE_CALENDAR = null;
 	
 	var minDate = new Date();
 	minDate.setDate(minDate.getDate() - 416); 	  	
 	
 	if(start != null){
 		if(start){
 			if(twin == ''){
 				var max_date = today_date;
 			} else {
 				
 				 var currentTagTokens = twin.split( " " );
 				 
 				 //date
 				 var currentEndDate = currentTagTokens[0].split( "/" );
 				 
 				 var compareEndDate = currentEndDate[2] + "/" +  currentEndDate[1] + "/" +  currentEndDate[0];
 				 var compareTodayDate = year + "/" +  month + "/" +  day;
 				 
 				 if(compareEndDate < compareTodayDate) {
 					var max_date = currentEndDate[1] + "/" +  currentEndDate[0] + "/" +  currentEndDate[2];
 				 } else {
 					 var max_date = today_date;
 					 
 				 }
 			}
 			
 		} else {
 			if(twin == ''){
 				var min_date = null;
 			} else {
 				
 				 var currentTagTokens = twin.split( " " );
 				 
 				 //date
 				 var currentEndDate = currentTagTokens[0].split( "/" );
 				 
 				 var min_date = currentEndDate[1] + "/" +  currentEndDate[0] + "/" +  currentEndDate[2];
 				 
 			}
 		}
 	} else {
 		start = false;
 		today_date_for_calendar = null;
 		var min_date = today_date;
 	}
 	
 	var x = 0, y = 0;
 		
 	var element = timeDivId;
 	element = document.getElementById(element);
 	element.style.zIndex = 2;
 	
 	//get the offset
 	while(element){
 		x += element.offsetLeft;
 		y += element.offsetTop;
 		element = element.offsetParent;
 	}
 	

 	//set the offset
 	YAHOO.ts.calendarContainer.moveTo(x, y);
 	
 	if(whichBrs() == 'Internet Explorer'){
 		YAHOO.ts.calendarContainer.setBody('<div style="position:relative;left:-10px;top:-10px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
 	} else {
 		YAHOO.ts.calendarContainer.setBody('<div style="position:relative;left:-11px;top:-11px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
 	}
 	
 	YAHOO.ts.calendarContainer.render(document.body);
 	
 	if(start){
 		YAHOO.ts.generalCalendar = new YAHOO.widget.Calendar("generalCalendar", timeDivId + '_calendarContainer', {title: "CALENDAR_TITLE".translate('COMMON') ,close:true, maxdate: max_date,  mindate: minDate});
 	} else {
 		YAHOO.ts.generalCalendar = new YAHOO.widget.Calendar("generalCalendar", timeDivId + '_calendarContainer', {title: "CALENDAR_TITLE".translate('COMMON') , close:true, mindate: min_date, maxdate: today_date_for_calendar});
 	}
 	
 	 //show the container
 	 YAHOO.ts.calendarContainer.show();
 	 	
 	
 	//function for handling select date
 	function selectHandler(type,args,obj) {
 		var date = args[0][0];
 		
 		var padding_day = (date[2] < 10 ? '0' : '' );
 		var padding_month = (date[1] < 10 ? '0' : '' );
 		 	 	
 		YAHOO.ts.calendarContainer.hide();

 		document.getElementById(targetId).value = padding_day + date[2] + '/' + padding_month + date[1] + '/' + date[0];
 		
 		var request = '';
 		
 		var currentTagTokens = document.getElementById(targetId).value.split( " " );
 		 
 		//date
 		var currentDate = currentTagTokens[0].split( "/" );
 		 
 		var todayTime = todayDate.split( " " );
 		var todayTimeElements = todayTime[1].split( ":" );
 		//case only from today on
 		if(today_date_for_calendar == null) {
 			if(day == currentDate[0] && month == currentDate[1] && year == currentDate[2]){
 				 request += '&start_h=' + todayTimeElements[0] + '&start_m=' + todayTimeElements[1];
 				 START_HOUR_CALENDAR = todayTimeElements[0];
 				 START_MINUTE_CALENDAR = todayTimeElements[1];
 				 END_HOUR_CALENDAR = null;
 				 END_MINUTE_CALENDAR = null;
 			} else {
 				 START_HOUR_CALENDAR = null;
 				 START_MINUTE_CALENDAR = null;
 				 END_HOUR_CALENDAR = null;
 				 END_MINUTE_CALENDAR = null;
 			}
 		} else {
 			if(start){
 				if(twin == ''){
 					 START_HOUR_CALENDAR = null;
 					 START_MINUTE_CALENDAR = null;
 					 if(day == currentDate[0] && month == currentDate[1] && year == currentDate[2]) {
 						 END_HOUR_CALENDAR = todayTimeElements[0];
 						 END_MINUTE_CALENDAR = todayTimeElements[1];
 						 request += '&end_h=' + todayTimeElements[0] + '&end_m=' + todayTimeElements[1];
 					 } else {
 						 END_HOUR_CALENDAR = null;
 						 END_MINUTE_CALENDAR = null;
 					 }
 				} else {
 					 START_HOUR_CALENDAR = null;
 					 START_MINUTE_CALENDAR = null;
 					 
 					var endTime = twin.split( " " );
 					 //it's not the same date
 					if(document.getElementById(targetId).value != endTime[0]){
 						END_HOUR_CALENDAR = null;
 						END_MINUTE_CALENDAR = null;
 					} else {
 						var endTimeElements = endTime[1].split( ":" );
 						request += '&end_h=' + endTimeElements[0] + '&end_m=' + endTimeElements[1];
 						END_HOUR_CALENDAR = endTimeElements[0];
 						END_MINUTE_CALENDAR = endTimeElements[1];
 					}
 				}
 				
 			} else {
 				if(twin == ''){
 					 START_HOUR_CALENDAR = null;
 					 START_MINUTE_CALENDAR = null;
 					 if(day == currentDate[0] && month == currentDate[1] && year == currentDate[2]) {
 						 END_HOUR_CALENDAR = todayTimeElements[0];
 						 END_MINUTE_CALENDAR = todayTimeElements[1];
 						 request += '&end_h=' + todayTimeElements[0] + '&end_m=' + todayTimeElements[1];
 					 } else {
 						 END_HOUR_CALENDAR = null;
 						 END_MINUTE_CALENDAR = null;
 					 }
 				} else {
 					if(day == currentDate[0] && month == currentDate[1] && year == currentDate[2]) {
 						 END_HOUR_CALENDAR = todayTimeElements[0];
 						 END_MINUTE_CALENDAR = todayTimeElements[1];
 						 request += '&end_h=' + todayTimeElements[0] + '&end_m=' + todayTimeElements[1];
 					 } else {
 						 END_HOUR_CALENDAR = null;
 						 END_MINUTE_CALENDAR = null;
 					 }
 					
 					 var startTime = twin.split( " " );
 					 //it's not the same date
 					if(document.getElementById(targetId).value != startTime[0]){
 						 START_HOUR_CALENDAR = null;
 						 START_MINUTE_CALENDAR = null;
 					} else {
 						var startTimeElements = startTime[1].split( ":" );
 						request += '&start_h=' + startTimeElements[0] + '&start_m=' + startTimeElements[1];
 						START_HOUR_CALENDAR = startTimeElements[0];
 						START_MINUTE_CALENDAR = startTimeElements[1];
 					}
 				}
 			}
 		}
 		
 		//set the values of the global variables for start and and hours and minutes depending on the calendars pair
 		if (pairNr == 1) {
 			START_HOUR_CALENDAR_FIRST_PAIR = START_HOUR_CALENDAR;
 			START_MINUTE_CALENDAR_FIRST_PAIR = START_MINUTE_CALENDAR;
 			END_HOUR_CALENDAR_FIRST_PAIR = END_HOUR_CALENDAR;
 			END_MINUTE_CALENDAR_FIRST_PAIR = END_MINUTE_CALENDAR;
 		} else if (pairNr == 2) {
 			START_HOUR_CALENDAR_SECOND_PAIR = START_HOUR_CALENDAR;
 			START_MINUTE_CALENDAR_SECOND_PAIR = START_MINUTE_CALENDAR;
 			END_HOUR_CALENDAR_SECOND_PAIR = END_HOUR_CALENDAR;
 			END_MINUTE_CALENDAR_SECOND_PAIR = END_MINUTE_CALENDAR;
 		}
 		
 		manageTimePanel('ShowTime.htm?showAllMinutes=' + showAllMinutes + '&pairNr=' + pairNr +'&panel=' + location + request, timePanel);
 		
 		YAHOO.ts.generalCalendar.destroy();
 	};
 	
 	//function for handling hide calendar
 	function hideHandler(type,args,obj) { 		
 		//hide the container
 		YAHOO.ts.calendarContainer.hide();
 		document.getElementById(targetId).value = '';
 		
 		manageDisplayRequiredAsterix("recordForm_startTime", "recordForm_endTime", "startTimeRequired", "endTimeRequired", "startTime_message", "endTime_message");
 		manageDisplayRequiredAsterix("recordForm_overTimeStartTime", "recordForm_overTimeEndTime", "overTimeStartTimeRequired", "overTimeEndTimeRequired", "overTimeStartTime_message", "overTimeEndTime_message"); 	
 		
 		YAHOO.ts.generalCalendar.destroy(); 		
 	}
 	
 	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
 	YAHOO.ts.generalCalendar.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
 	YAHOO.ts.generalCalendar.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
 	
 	YAHOO.ts.generalCalendar.selectEvent.subscribe(selectHandler, YAHOO.ts.generalCalendar, true); 
 	YAHOO.ts.generalCalendar.hideEvent.subscribe(hideHandler, YAHOO.ts.generalCalendar, true); 
 	YAHOO.ts.generalCalendar.render();
 	YAHOO.ts.generalCalendar.show();

 }
 
 /**
  * Manage the display of the required asterix when a calendar is closed
  * 
  * @author Adelina
  * 
  * @param startTime
  * @param endTime
  * @param startTimeRequired
  * @param endTimeRequired
  * @return
  */
 function manageDisplayRequiredAsterix(startTime, endTime, startTimeRequired, endTimeRequired, startMessage, endMessage) {
	 var start = document.getElementById(startTime);
	 var end = document.getElementById(endTime);
	 var startRequired = document.getElementById(startTimeRequired);	 
	 var endRequired = document.getElementById(endTimeRequired);
	 var startM = document.getElementById(startMessage);
	 var endM = document.getElementById(endMessage);
	 
	 if(start != null && end != null) {		 
		 if(start.value == '' && end.value == '') {	 			
			 startRequired.style.display = "none";
			 endRequired.style.display = "none";
			 startM.innerHTML = "";
			 endM.innerHTML = "";
		}	
	 }
 }

 function manageTimePanel(url, container){
 	 //destroy what is left
 	if (YAHOO.ts.showTime != null) {
 		YAHOO.ts.showTime.destroy();
 		YAHOO.ts.showTime = null;
 	}
 		
 	var x = 0, y = 0;
 	var _width = null;
 	
 	var element = container;
 	element = document.getElementById(element);
 	
 	//get the offset
 	while(element)
 	{
 		x += element.offsetLeft;
 		y += element.offsetTop;
 		element = element.offsetParent;
 	}
 	
 	if(whichBrs() == "Internet Explorer"){
 		container = document.body;
 		_width = "150px";
 	}
 	
 	container = document.body;

 	//creating the panel if it isn't there
 	if(YAHOO.ts.showTime == null){
 			
 		var width = null;
 			
 		YAHOO.ts.showTime = new YAHOO.widget.Panel("TimePanel", {zIndex: 100, modal: true, x:x, y:y, width:_width, visible: false, autofillheight: "body", overflow: "auto", visible:false, constraintoviewport: true, close: false,
 			draggable: true, effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
 		YAHOO.ts.showTime.setHeader('<span>' + "TIMEPANELTITLE".translate('COMMON') + '</span>');	
 	}
 	
 	//Because responseText in IE, Safari and Chrome is not working I have to render manual
 	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.ts.showTime, document.getElementById(container));
 	
 }
 
 /**
  * 
  * To populate minutes on hour change
  * @author mitziuro
  * @author Coni
  * @return
  */
function populateTime(select_hour_id, select_minutes_id, showAllMinutes, pairNr){
	
 	//set the local varibles for start and end hour and minutes
 	var START_HOUR_CALENDAR = null;
 	var START_MINUTE_CALENDAR = null;
 	var END_HOUR_CALENDAR = null;
 	var END_MINUTE_CALENDAR = null;
 	
	//set the local variables value
	if (pairNr != null && pairNr == 1) {
 		START_HOUR_CALENDAR = START_HOUR_CALENDAR_FIRST_PAIR;
 		START_MINUTE_CALENDAR = START_MINUTE_CALENDAR_FIRST_PAIR;
 		END_HOUR_CALENDAR = END_HOUR_CALENDAR_FIRST_PAIR;
 		END_MINUTE_CALENDAR = END_MINUTE_CALENDAR_FIRST_PAIR;
 	} else if (pairNr != null && pairNr == 2) {
 		START_HOUR_CALENDAR = START_HOUR_CALENDAR_SECOND_PAIR;
 		START_MINUTE_CALENDAR = START_MINUTE_CALENDAR_SECOND_PAIR;
 		END_HOUR_CALENDAR = END_HOUR_CALENDAR_SECOND_PAIR;
 		END_MINUTE_CALENDAR = END_MINUTE_CALENDAR_SECOND_PAIR;
 	}
	
	var myMinutes = new Array();
	// check if I have to show all minutes or for 15 to 15
	if (showAllMinutes != null && showAllMinutes == true){		
		//special case for hour > 23:59 (no options left)
		if(document.getElementById(select_minutes_id).options.length == 0){
			document.getElementById(select_hour_id).options[0] = new Option(23, 23, true);
			document.getElementById(select_minutes_id).options[0] = new Option(59, 59, true);
			return;
		}
		
		for ( var int = 0; int < 10; int++) {
			myMinutes[int] = '0' + int;			
		}
		for ( var int = 10; int < 60; int++) {
			myMinutes[int] = '' + int;			
		}
	} else {
		//special case for hour > 23:45 (no options left)
		if(document.getElementById(select_hour_id).options.length == 0){
			document.getElementById(select_hour_id).options[0] = new Option(23, 23, true);
			document.getElementById(select_minutes_id).options[0] = new Option(45, 45, true);
			return;
		}		 
		
		myMinutes[0] = "00";
		myMinutes[1] = "15";
		myMinutes[2] = "30";
		myMinutes[3] = "45";
	}
	
	//greater then
	if(document.getElementById(select_hour_id).value == START_HOUR_CALENDAR){
		 
		for(var i=0; i<myMinutes.length; i++){
			if(myMinutes[i] < START_MINUTE_CALENDAR){
				myMinutes[i] = null; 
			}
		}
	 }
	 
	//lower then
	if(document.getElementById(select_hour_id).value == END_HOUR_CALENDAR){
	
		for(var i=0; i<myMinutes.length; i++){
			if(myMinutes[i] > END_MINUTE_CALENDAR){
				myMinutes[i] = null; 
			}
		}
	 }
	
	 //repopulate the minutes
	 for(var i=0; i<myMinutes.length; i++){
		 document.getElementById(select_minutes_id).options[0] = null;
	 }

	 var positionId = 0;
	 for(var i=0; i<myMinutes.length; i++){
		if(myMinutes[i] != null){ 
			//set the new options
			document.getElementById(select_minutes_id).options[positionId] = new Option(myMinutes[i], myMinutes[i], true);
			positionId++;
		} 
	 }
	 
	 //no elements we repopulate the select
	 if(document.getElementById(select_minutes_id).options.length == 0){
	 	document.getElementById(select_hour_id).options[0] = null;
		 populateTime(select_hour_id, select_minutes_id);
	 }
	 
	 //select the first one
	 document.getElementById(select_minutes_id).options[0].selected = true;
	
 }

 /**
  * Writes the time in inputId and hides the time panel
  * @author coni
  * @author alu
  * @param inputId
  * @return
  */
function hideTimePanel(inputId){
	// create the date input with the previous selected date and appended with the selected time
	document.getElementById(inputId).value = document.getElementById(inputId).value + ' ' + document.getElementById('hour').value  + ':' + document.getElementById('minutes').value;
	// now hide the time panel
	YAHOO.ts.showTime.hide();
}

/**
 * Instantiates
 * @author Coni
 * @param _timeDivId
 * @param _targetId
 * @param _todayDate
 * @param _timePanel
 * @param _location
 * @param showAllMinutes
 * @return
 */
function instantiateCalendar(_timeDivId, _targetId, _todayDate, _timePanel, _location, showAllMinutes){
	 
 	var timeDivId = this.timeDivId != null ? this.timeDivId : _timeDivId,
 		targetId = this.targetId != null ? this.targetId : _targetId,
 		todayDate = this.todayDate != null ? this.todayDate : _todayDate,
 		timePanel = this.timePanel != null ? this.timePanel : _timePanel,
 		location = _location == null ? 'availability' : _location;
 	  	
 	var day = todayDate.substring(0,2); 	
 	var month = todayDate.substring(3,5); 	
 	var year = todayDate.substring(6,10); 	

 	var today_date =  month + "/" + day + "/" + year; 
 	var today_date_for_calendar = today_date;
 	var today_date_for_compare = day + "/" + month + "/" + year; 
 	
	var max_date = today_date;	
	
 	var x = 0, y = 0;
 		
 	var element = timeDivId;
 	element = document.getElementById(element);
 	element.style.zIndex = 2;
 	
 	//get the offset
 	while(element){
 		x += element.offsetLeft;
 		y += element.offsetTop;
 		element = element.offsetParent;
 	}
 	

 	//set the offset
 	YAHOO.ts.calendarContainer.moveTo(x, y);
 	
 	if(whichBrs() == 'Internet Explorer'){
 		YAHOO.ts.calendarContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
 	} else {
 		YAHOO.ts.calendarContainer.setBody('<div style="position:relative;left:-11px;top:-11px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
 	}
 	
 	YAHOO.ts.calendarContainer.render(document.body);
 	
	YAHOO.ts.generalCalendar = new YAHOO.widget.Calendar("generalCalendar", timeDivId + '_calendarContainer', {title: "CALENDAR_TITLE".translate('COMMON') ,close:true, maxdate: max_date});
 	
 	//show the container
 	YAHOO.ts.calendarContainer.show();
 	
 	//function for handling select date
 	function selectHandler(type,args,obj) {
 		var date = args[0][0];
 		
 		var padding_day = (date[2] < 10 ? '0' : '' );
 		var padding_month = (date[1] < 10 ? '0' : '' );
 		
 		YAHOO.ts.calendarContainer.hide();

 		document.getElementById(targetId).value = padding_day + date[2] + '/' + padding_month + date[1] + '/' + date[0];
 		
 		manageTimePanel('ShowTime.htm?showAllMinutes=' + showAllMinutes +'&panel=' + location + '&pairNr=1', timePanel);
 		
 		YAHOO.ts.generalCalendar.destroy();
 	};
 	
 	//function for handling hide calendar
 	function hideHandler(type,args,obj) {
 		
 		//hide the container
 		YAHOO.ts.calendarContainer.hide();
 		document.getElementById(targetId).value = '';
 		YAHOO.ts.generalCalendar.destroy();
 	}
 	
 	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
 	YAHOO.ts.generalCalendar.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
 	YAHOO.ts.generalCalendar.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
 	
 	YAHOO.ts.generalCalendar.selectEvent.subscribe(selectHandler, YAHOO.ts.generalCalendar, true); 
 	YAHOO.ts.generalCalendar.hideEvent.subscribe(hideHandler, YAHOO.ts.generalCalendar, true); 
 	YAHOO.ts.generalCalendar.render();
 	YAHOO.ts.generalCalendar.show();

 }
 
 /**
  * For hints
  * 
  * @author Adelina
  * 
  * @return
  */
 function prepareInputsForHints() {		
		var inputs = document.getElementsByTagName("input");
		for (var i=0; i<inputs.length; i++){						
			// test to see if the hint span exists first
			if (inputs[i].parentNode.getElementsByTagName("span")[0]) {			
				// the span exists!  on focus, show the hint
				inputs[i].onfocus = function () {										
					this.parentNode.getElementsByTagName("span")[0].style.display = "inline";
				}
				// when the cursor moves away from the field, hide the hint
				inputs[i].onblur = function () {
					this.parentNode.getElementsByTagName("span")[0].style.display = "none";
				}
			}
		}	
	}
  
  /**
   * Sort the options from a select
   * 
   * @param selElem
   * @return
   */
  function sortSelect(selElem) {
      var tmpAry = new Array();
      for (var i=0;i<selElem.options.length;i++) {
              tmpAry[i] = new Array();
              tmpAry[i][0] = selElem.options[i].text;
              tmpAry[i][1] = selElem.options[i].value;
      }
      tmpAry.sort();
      while (selElem.options.length > 0) {
          selElem.options[0] = null;
      }
      for (var i=0;i<tmpAry.length;i++) {
              var op = new Option(tmpAry[i][0], tmpAry[i][1]);
              selElem.options[i] = op;
      }
      return;
}

 

//=============================================== END OF cs_common.js ====================================