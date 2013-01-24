/*
 *    This file is part of Termitaria, a project management tool 
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
 */
/**
 * 
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
 * Displays the content received from server into a div.
 * 
 * @author matti_joona
 */
function getContentFromUrl(_url, _divId){
	var url = this.url != null ? this.url : _url;
	var divId = this.container != null ? this.container: _divId;
	if (this.withConfirmation && YAHOO.audit.confirmation){
		//The panel it's hard coded as it cannot be
		//passed as a parameter. 
		YAHOO.audit.confirmation.hide();
	}

	var loadObject = { 
		handleSucess:function (o){
			if(o.responseText !== undefined){
				sessionExpired(o);
				var divElem = document.getElementById(divId);
				divElem.innerHTML = o.responseText;
				//this is for executing javascripts elements
				YAHOO.audit.smallLoading.hide();
				
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
			YAHOO.audit.smallLoading.hide();

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
	YAHOO.audit.smallLoading.show();
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
				//this is for executing javascripts elements
				var scriptTags = divElem.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				YAHOO.audit.smallLoading.hide();
					 
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.audit.smallLoading.hide();
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
	YAHOO.audit.smallLoading.show();
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
				panel.setBody(o.responseText);
				panel.render(document.body);
				
				panel.show();
				
				var browser = whichBrs();
				if (browser == "Internet Explorer" || browser == "Chrome" || browser == "Safari" || browser == "Opera") {
					//for some browsers we need to evaluate the response
					var scriptTags = panel.body.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
				}
					
				
				YAHOO.audit.smallLoading.hide();
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.audit.smallLoading.hide();
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
	YAHOO.audit.smallLoading.show();
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
			table.rows[i].className = "selected_row";
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
	var paginator = YAHOO.audit.notificationPaginator;
	//the page number it's always 1 higher then the selected option index
	paginator.setPage(pageNumber + 1,false);
}

 /**
  * Delete a Notification and do the pagination again
  * @author mitziuro
  * @param notification
  * @param module_array
  * @param pagination_container
  * @param cs_pagination
  * @return
  */
function deleteNotification(notification, module_array, pagination_container, cs_pagination, noEntryMessage){
	
	//hiding all modules
	for(var i=0;i< module_array.length; i++){
		module_array[i].hide();
	}

	//if it;s only one notification that will be deleted we display the no results
	//message
	if(module_array.length == 1){
		document.getElementById(pagination_container).className = "noSearchResults";
		document.getElementById(pagination_container).innerHTML = noEntryMessage;
		document.getElementById(cs_pagination).innerHTML = '';
		return;
	}
	
	//we search the notification in the notification array
	for(var i=0;i< module_array.length; i++){
		if(notification == module_array[i]){
			//we eliminate it
			for(var j = i + 1 ;j < module_array.length; j++){
				 module_array[j-1] =  module_array[j];
			}
		}
	}
	
	module_array = module_array.slice(0,module_array.length - 1);	
	
	//the current page
	var returnPage = YAHOO.audit.notificationPaginator.getCurrentPage();
	YAHOO.audit.notificationPaginator = setYUIModulePagination(module_array, pagination_container, cs_pagination);

	//if on the last page there aren't any results we go back one page 
	if(YAHOO.audit.notificationPaginator.getTotalPages() < returnPage){
		YAHOO.audit.notificationPaginator.setPage(returnPage - 1);
	} else {
		YAHOO.audit.notificationPaginator.setPage(returnPage);
	}
	
	return module_array;
	
}

/**
 * Sending a request for deleting a notification
 * and do the actions after the response has come
 * @author mitziuro
 * @param url
 * @param divId
 * @param notification
 * @param pagination_container
 * @param cs_pagination
 * @return
 */
function deleteNotificationRequest(url, divId, notification, pagination_container, cs_pagination, noEntryMessage){
	var module_array = this.module_array;
	var loadObject = { 
			handleSucess:function (o){
				if(o.responseText !== undefined){
					sessionExpired(o);
					var divElem = document.getElementById(divId);
					divElem.innerHTML = o.responseText; 
					
					//evaluating the java scripts
					var scriptTags = divElem.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
					
					//if we have errors we do nothing
					if(document.getElementById('ERRORS_CONTAINER') != null){
						return;
					}
					
					//we get the modified array and we put it on context
					module_array = deleteNotification(notification, module_array, pagination_container, cs_pagination, noEntryMessage);
					moduleArrayObject(module_array);
					YAHOO.audit.smallLoading.hide();
						 
				}
			},
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
				YAHOO.audit.smallLoading.hide();
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
		YAHOO.audit.smallLoading.show();
		loadObject.startRequest();
}

//YUI MODULE JAVASCRIPT PAGINATOR END

 
function selectUnSelectRow(cel, checkboxes, selectAllCheckBox) {
	row = cel.parentNode;
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
	
	YAHOO.audit.availabilityCalendar = new YAHOO.widget.Calendar("availabiltyCalendar", _divId ,{title: "DOCUMENTFORM_CALENDAR_TITLE".translate('COMMON') ,close:true, mindate: month + "/" + day + "/" + year});
	
	//function for handling select date
	function selectHandler(type,args,obj) {
		var date = args[0][0];
		document.getElementById(targetId).value = date[2] + '-' +date[1] + '-' + date[0];
		YAHOO.audit.availabilityCalendar.hide();
	};
	
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
	YAHOO.audit.availabilityCalendar.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
	YAHOO.audit.availabilityCalendar.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
	
	YAHOO.audit.availabilityCalendar.selectEvent.subscribe(selectHandler, YAHOO.audit.availabilityCalendar, true); 
	YAHOO.audit.availabilityCalendar.render();
	YAHOO.audit.availabilityCalendar.show();
}
 
/**
 * Handles the exired session event
 * @author mitziuro
 * @return
 */
function sessionExpired(oResponse){
	 if (YAHOO.audit.sessionExpired && oResponse.responseText == SESSION_EXPIRED){
		YAHOO.audit.sessionExpired.show();
		setTimeout('window.location="SignOn.htm"', 1500); 
	}
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
 * To populate minutes on hour change for out of office
 * @return
 */
function populateMinutes(){
	
  	var hourlist=document.getElementById("hour");
	var hour = hourlist.options[hourlist.selectedIndex].text;	
	var minuteslist = document.getElementById("minutes");

	var myMinutes = new Array();
	  myMinutes[0] = "00";
	  myMinutes[1] = "15";
	  myMinutes[2] = "30";
	  myMinutes[3] = "45";
  
	var minlistsize = minuteslist.options.length;
	
	if(hour == endHours){		
		if(endMinutes == myMinutes[0]){			
			for(var j = 0; j < 1; j++){
				minuteslist.remove(0);
			}
		}
		if(endMinutes == myMinutes[1]){					
			for(var j = 0; j < 2; j++){
				minuteslist.remove(0);
			}			
		}
		if(endMinutes == myMinutes[2]){
			for(var j = 0; j < 3; j++){
				minuteslist.remove(0);
			}
		}
		if(endMinutes == myMinutes[3]){					
			for(var j = 0; j < 3; j++){
				minuteslist.remove(0);
			}
		}
				
	} else {		
	      var i = 0;
	      // delete all from list
	          for (i = 0; i < minlistsize; i++) {
	              minuteslist.remove(0);
	          }
	          for (i = 0; i < myMinutes.length; i++) {
	              minuteslist.options[i] = new Option(myMinutes[i], myMinutes[i]);
	          }		
	  	}		
  }

//========================================================================================================

function instantiatePersonAutoComplete(oDS, inputId, autoCompleteContainer, hiddenFieldId){
	if (oDS != null){
		// Use a LocalDataSource
		oDS.responseSchema = {fields : ["name", "id"]};
		
		// Instantiate the AutoComplete 
		YAHOO.audit.personsAutoComplete = new YAHOO.widget.AutoComplete(inputId, autoCompleteContainer, oDS); 
    
		YAHOO.audit.personsAutoComplete._bItemSelected = (YAHOO.audit.personsAutoComplete._elTextbox.value != '');       
    
		YAHOO.audit.personsAutoComplete.resultTypeList = false; 
    
		// enable string input contained
		YAHOO.audit.personsAutoComplete.queryMatchContains = true;
    
		// Show more results, scrolling is enabled via CSS
		YAHOO.audit.personsAutoComplete.maxResultsDisplayed = 100;
    
		// Enable force selection 
		YAHOO.audit.personsAutoComplete.forceSelection = true;

		//oAC.prehighlightClassName = "yui-ac-prehighlight"; 
		YAHOO.audit.personsAutoComplete.useShadow = true; 
      
		//document.getElementById(inputId).focus();
 
		// Define an event handler to populate a hidden form field 
		// when an item gets selected 
		var myHiddenField = YAHOO.util.Dom.get(hiddenFieldId); 
		var myHandler = function(sType, aArgs) { 
			var myAC = aArgs[0]; // reference back to the AC instance 
			var elLI = aArgs[1]; // reference to the selected LI element 
			var oData = aArgs[2]; // object literal of selected item's result data 
         
			// update hidden form field with the selected item's ID
			myHiddenField.value = oData.id;
		};     
		YAHOO.audit.personsAutoComplete.itemSelectEvent.subscribe(myHandler);
    
		YAHOO.util.Event.addListener(inputId, "click", function(){
			setTimeout(function() { // For IE 
				YAHOO.audit.personsAutoComplete.sendQuery(" "); 
			},0);
		});
	}   
}

function instantiatePersonAutoCompleteForOrganisation(organisationId, inputId, autoCompleteContainer, hiddenFieldId){
	if (organisationId == -1){
		var oDS = null;
		instantiatePersonAutoComplete(oDS, inputId, autoCompleteContainer, hiddenFieldId);
	} else {
		var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					sessionExpired(o);
					oResults = eval("(" + o.responseText + ")");
					YAHOO.audit.personsAutoComplete.destroy();
					instantiatePersonAutoComplete(new YAHOO.util.LocalDataSource(oResults), inputId, autoCompleteContainer, hiddenFieldId);
				}
			}, 
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
		    },
			startRequest:function() {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', 'GetJsonPersons.htm?organisationId='+organisationId, callback); 
		    }
		}; 
		var callback = {
			success:AjaxObject.handleSuccess, 
			failure:AjaxObject.handleFailure 
		};
		//start transaction
		AjaxObject.startRequest();
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
 	 	
 	//set the local variables for start and end hour and minutes
 	var START_HOUR_CALENDAR = null;
 	var START_MINUTE_CALENDAR = null;
 	var END_HOUR_CALENDAR = null;
 	var END_MINUTE_CALENDAR = null;
 	
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
 	YAHOO.audit.calendarContainer.moveTo(x, y);
 	
 	if(whichBrs() == 'Internet Explorer'){
 		YAHOO.audit.calendarContainer.setBody('<div style="position:relative;left:-10px;top:-10px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
 	} else {
 		YAHOO.audit.calendarContainer.setBody('<div style="position:relative;left:-11px;top:-11px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
 	}
 	
 	YAHOO.audit.calendarContainer.render(document.body);
 	
 	if(start){
 		YAHOO.audit.generalCalendar = new YAHOO.widget.Calendar("generalCalendar", timeDivId + '_calendarContainer', {title: "CALENDAR_TITLE".translate('COMMON') ,close:true, maxdate: max_date});
 	} else {
 		YAHOO.audit.generalCalendar = new YAHOO.widget.Calendar("generalCalendar", timeDivId + '_calendarContainer', {title: "CALENDAR_TITLE".translate('COMMON') , close:true, mindate: min_date, maxdate: today_date_for_calendar});
 	}
 	
 	 //show the container
 	 YAHOO.audit.calendarContainer.show();
 	 	
 	
 	//function for handling select date
 	function selectHandler(type,args,obj) {
 		var date = args[0][0];
 		
 		var padding_day = (date[2] < 10 ? '0' : '' );
 		var padding_month = (date[1] < 10 ? '0' : '' );
 		 	 	
 		YAHOO.audit.calendarContainer.hide();

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
 		
 		//set the values of the global variables for start and end hours and minutes depending on the calendars pair
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
 		
 		YAHOO.audit.generalCalendar.destroy();
 	};
 	
 	//function for handling hide calendar
 	function hideHandler(type,args,obj) { 		
 		//hide the container
 		YAHOO.audit.calendarContainer.hide();
 		document.getElementById(targetId).value = '';
 		
 		YAHOO.audit.generalCalendar.destroy(); 		
 	}
 	
 	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
 	YAHOO.audit.generalCalendar.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
 	YAHOO.audit.generalCalendar.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
 	
 	YAHOO.audit.generalCalendar.selectEvent.subscribe(selectHandler, YAHOO.audit.generalCalendar, true); 
 	YAHOO.audit.generalCalendar.hideEvent.subscribe(hideHandler, YAHOO.audit.generalCalendar, true); 
 	YAHOO.audit.generalCalendar.render();
 	YAHOO.audit.generalCalendar.show();

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
  	YAHOO.audit.calendarContainer.moveTo(x, y);
  	
  	if(whichBrs() == 'Internet Explorer'){
  		YAHOO.audit.calendarContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
  	} else {
  		YAHOO.audit.calendarContainer.setBody('<div style="position:relative;left:-11px;top:-11px;width:150px;" id="' + timeDivId + '_calendarContainer" />');
  	}
  	
  	YAHOO.audit.calendarContainer.render(document.body);
  	
 	YAHOO.audit.generalCalendar = new YAHOO.widget.Calendar("generalCalendar", timeDivId + '_calendarContainer', {title: "CALENDAR_TITLE".translate('COMMON') ,close:true, maxdate: max_date});
  	
  	//show the container
  	YAHOO.audit.calendarContainer.show();
  	
  	//function for handling select date
  	function selectHandler(type,args,obj) {
  		var date = args[0][0];
  		
  		var padding_day = (date[2] < 10 ? '0' : '' );
  		var padding_month = (date[1] < 10 ? '0' : '' );
  		
  		YAHOO.audit.calendarContainer.hide();

  		document.getElementById(targetId).value = padding_day + date[2] + '/' + padding_month + date[1] + '/' + date[0];
  		
  		manageTimePanel('ShowTime.htm?showAllMinutes=' + showAllMinutes +'&panel=' + location + '&pairNr=1', timePanel);
  		
  		YAHOO.audit.generalCalendar.destroy();
  	};
  	
  	//function for handling hide calendar
  	function hideHandler(type,args,obj) {
  		
  		//hide the container
  		YAHOO.audit.calendarContainer.hide();
  		document.getElementById(targetId).value = '';
  		YAHOO.audit.generalCalendar.destroy();
  	}
  	
  	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
  	YAHOO.audit.generalCalendar.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
  	YAHOO.audit.generalCalendar.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
  	
  	YAHOO.audit.generalCalendar.selectEvent.subscribe(selectHandler, YAHOO.audit.generalCalendar, true); 
  	YAHOO.audit.generalCalendar.hideEvent.subscribe(hideHandler, YAHOO.audit.generalCalendar, true); 
  	YAHOO.audit.generalCalendar.render();
  	YAHOO.audit.generalCalendar.show();

  }
 
 /**
  * @author Coni
  * @param url
  * @param container
  * @return
  */
 function manageTimePanel(url, container){
 	 //destroy what is left
 	if (YAHOO.audit.showTime != null) {
 		YAHOO.audit.showTime.destroy();
 		YAHOO.audit.showTime = null;
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
 	if(YAHOO.audit.showTime == null){
 			
 		var width = null;
 			
 		YAHOO.audit.showTime = new YAHOO.widget.Panel("TimePanel", {zIndex: 100, modal: true, x:x, y:y, width:_width, visible: false, autofillheight: "body", overflow: "auto", visible:false, constraintoviewport: true, close: false,
 			draggable: true, effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
 		YAHOO.audit.showTime.setHeader('<span>' + "TIMEPANELTITLE".translate('COMMON') + '</span>');	
 	}
 	
 	//Because responseText in IE, Safari and Chrome is not working I have to render manual
 	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.audit.showTime, document.getElementById(container));
 	
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
	YAHOO.audit.showTime.hide();
}

//=============================================== END OF cs_common.js ====================================