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
 * Loads all the JavaScriptu
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
	if (this.withConfirmation && YAHOO.cm.confirmation){
		//The panel it's hard coded as it cannot be
		//passed as a parameter. 
		YAHOO.cm.confirmation.hide();
	}

	var loadObject = { 
		handleSucess:function (o){
			if(o.responseText !== undefined){
				var divElem = document.getElementById(divId);
				divElem.innerHTML = o.responseText;
				//this is for executing javascripts elements
				YAHOO.cm.smallLoading.hide();
				
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
			YAHOO.cm.smallLoading.hide();

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
	YAHOO.cm.smallLoading.show();
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
//				alert("url = " + url);
//				alert("container = " + container);
//				alert("document.getElementById(container) = " + document.getElementById(container).innerHTML);
				var divElem = document.getElementById(container);
				divElem.innerHTML = o.responseText; 
				//this is for executing javascripts elements
				var scriptTags = divElem.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				YAHOO.cm.smallLoading.hide();
					 
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.cm.smallLoading.hide();
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
	YAHOO.cm.smallLoading.show();
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
					
									
				YAHOO.cm.smallLoading.hide();
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.cm.smallLoading.hide();
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
	YAHOO.cm.smallLoading.show();
    loadObject.startRequest();
}

function returnContentFromUrlToPanelAndShowPannelForUpdateExternalPerson(url, panel, memberId) {
	var loadObject = { 
			handleSucess:function (o){
				if(o.responseText !== undefined){
					panel.setBody(o.responseText);
					panel.render(document.body);   				
					var browser = whichBrs();
					if (browser == "Internet Explorer" || browser == "Chrome" || browser == "Safari" || browser == "Opera") {
						//for some browsers we need to evaluate the response
						var scriptTags = panel.body.getElementsByTagName('script');
						for(var i=0;i<scriptTags.length;i++){
							eval(scriptTags[i].innerHTML);
						}
					}
//					alert("ID_JSON_EXTERNAL_MEMEBER = " + ID_JSON_EXTERNAL_MEMEBER);
//					alert("JSON_STR = " + JSON_STR);
//					alert("memberId  = " + memberId);
					if(memberId > 0) {
						var endText = '"' + ',"' + 'memberId' + '"' + ' : ' + '"' + memberId + '"';
		   				var end = JSON_STR.indexOf(endText);
		   				var string = JSON_STR.substring(0, end);	   			
		   				var start = string.lastIndexOf('"');	   				
		   				var id = JSON_STR.substring(start + 1, end);	   				
		   				//alert("id = " + id);
		   				if(id > 0) {
		   					ID_JSON_EXTERNAL_MEMEBER = id;
		   				} else {
		   					ID_JSON_EXTERNAL_MEMEBER = null;
		   				}
					}
					
					if(ID_JSON_EXTERNAL_MEMEBER == null && memberId > 0) {
						addTeamMember();	
					} else {
						if(memberId > 0) {
							//alert("id = " + ID_JSON_EXTERNAL_MEMEBER + ", memberId = " + memberId);				
							
				   			var idString = '{ "' + 'id' + '"' + ' : ' + '"' + ID_JSON_EXTERNAL_MEMEBER + '"';   			
				   			
				   			// search for the idString, and gets the index 
				   			var indexId = JSON_STR.search(idString);		   			
				   			    			   			
				   			// create a substring with all the data from the specific indexId
				   			var text = JSON_STR.substring(indexId, JSON_STR.length);	   		
				   			// in this text, find the next }   			
				   			var indexNextId = text.indexOf('}');
				   			
				   			var element = text.substring(0, indexNextId + 2);
				   			//alert("element =  " + element);
				   			 
				   			var oForm = document.personForm;
				   			
				   			// get the element's ids from the form
				   			elements = showElements(oForm);
				   			
				   			// get the elements's names from the form   			
				   			elementsName = showElementsName(oForm);
				   			
				   			for(var i = 1, j = 1; i < elementsName.length - 1, j < elements.length - 1; i++, j++) {
				   				pattern = '"' + elementsName[i] + '"' + ' : '+ '"';   			
				   				var start = element.indexOf(pattern);
				   				var temp = element.substring(start+pattern.length, element.length);
				   				var finish = temp.indexOf('"');
				   				// get the element's value
				   				var elementValue = element.substring(start + pattern.length,  start + pattern.length + finish);
				   				
				   				//alert("elementValue = " + elementValue);
				   				
				   				// set the value for every form element
				   				document.getElementById(elements[j]).value =  elementValue;   			   					   					
				   				
				   			}
						} else {
							ID_JSON_EXTERNAL_MEMEBER = null;
						}
					}
					panel.show();					
					YAHOO.cm.smallLoading.hide();			
				}
			},
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
				YAHOO.cm.smallLoading.hide();
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
		YAHOO.cm.smallLoading.show();
	   loadObject.startRequest();
}

/**
* Sets a YUI Panel's body to display the content
* 
* @author Adelina
*/
function returnContentFromUrlToPanelAndShowPannelForExternalPerson(url, panel, id, memberId){
	var loadObject = { 
		handleSucess:function (o){
			if(o.responseText !== undefined){
				panel.setBody(o.responseText);
				panel.render(document.body);   				
				var browser = whichBrs();
				if (browser == "Internet Explorer" || browser == "Chrome" || browser == "Safari" || browser == "Opera") {
					//for some browsers we need to evaluate the response
					var scriptTags = panel.body.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
				}
					   	
				//alert("id = " + id + ", memberId = " + memberId);				
								
				// save the id of the external member in a global variable
				ID_JSON_EXTERNAL_MEMEBER = id;
				
	   			var idString = '{ "' + 'id' + '"' + ' : ' + '"' + id + '"';   			
	   			
	   			// search for the idString, and gets the index 
	   			var indexId = JSON_STR.search(idString);		   			
	   			    			   			
	   			// create a substring with all the data from the specific indexId
	   			var text = JSON_STR.substring(indexId, JSON_STR.length);	   		
	   			// in this text, find the next }   			
	   			var indexNextId = text.indexOf('}');
	   			
	   			var element = text.substring(0, indexNextId + 2);
	   			//alert("element =  " + element);
	   			 
	   			var oForm = document.personForm;
	   			
	   			// get the element's ids from the form
	   			elements = showElements(oForm);
	   			
	   			// get the elements's names from the form   			
	   			elementsName = showElementsName(oForm);
	   			
	   			for(var i = 1, j = 1; i < elementsName.length - 1, j < elements.length - 1; i++, j++) {
	   				pattern = '"' + elementsName[i] + '"' + ' : '+ '"';   			
	   				var start = element.indexOf(pattern);
	   				var temp = element.substring(start+pattern.length, element.length);
	   				var finish = temp.indexOf('"');
	   				// get the element's value
	   				var elementValue = element.substring(start + pattern.length,  start + pattern.length + finish);
	   				
	   				//alert("elementValue = " + elementValue);
	   				
	   				// set the value for every form element
	   				document.getElementById(elements[j]).value =  elementValue;   			   					   					
	   				
	   			}	
									   			
				panel.show();					
				YAHOO.cm.smallLoading.hide();			
			}
		},
		handleFailure:function (o){
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.cm.smallLoading.hide();
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
	YAHOO.cm.smallLoading.show();
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
	var paginator = YAHOO.cm.notificationPaginator;
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
	
	YAHOO.cm.availabilityCalendar = new YAHOO.widget.Calendar("availabiltyCalendar", _divId ,{title: "DOCUMENTFORM_CALENDAR_TITLE".translate('COMMON') ,close:true, mindate: month + "/" + day + "/" + year});
	
	//function for handling select date
	function selectHandler(type,args,obj) {
		var date = args[0][0];
		document.getElementById(targetId).value = date[2] + '-' +date[1] + '-' + date[0];
		YAHOO.cm.availabilityCalendar.hide();
	};
	
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
	YAHOO.cm.availabilityCalendar.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
	YAHOO.cm.availabilityCalendar.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
	
	YAHOO.cm.availabilityCalendar.selectEvent.subscribe(selectHandler, YAHOO.cm.availabilityCalendar, true); 
	YAHOO.cm.availabilityCalendar.render();
	YAHOO.cm.availabilityCalendar.show();
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

/**
 *  
 * @author Adelina
 * 
 * @param isChecked
 * @return
 */
function populateProjectStatus(isChecked) { 	 
	 if (document.getElementById('status')) { 
		 document.getElementById('status').value=(isChecked==true)?'2':'1'; 	  
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
 
//========================================================================================================

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
 
//========================================================================================================
 /**
  * Restricts the user to input only numbers, spaces, '.', '+', and '-'.
  * 
  * @author Adelina
  * 
  * @param e
  * @return
  */
 function restrictCharactersForPhone(e)
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
 	numcheck = new RegExp(/[a-zA-Z!@#$%^&*~`()\[\]=<>{}?:_,;/\"'\\|]/);
 	return !numcheck.test(keychar);
 }
 //========================================================================================================
 /**
 * Restricts the user to input only characters, numbers, spaces, '.', '-', "," .
 * 
 * @author Adelina
 * 
 * @param e
 * @return
 */
 function restrictCharactersForAddress(e)
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
 	numcheck = new RegExp(/[!@#$%^&*~`()\[\]+=<>{}?:;/\"'\\|]/);
 	return !numcheck.test(keychar);
 }
 //========================================================================================================
 /**
 * Restricts the user to input only characters, numbers, spaces, '.', '/'.
 * 
 * @author Adelina
 * 
 * @param e
 * @return
 */
 function restrictCharactersForIdentificationNumbers(e)
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
 	check = new RegExp(/[!@#$%^&*~`()\[\]+=<>{}\-?_:,;\"'\\|]/);
 	
 	return !check.test(keychar);
 }
 //========================================================================================================
 /**
 * Restricts the user to input only characters, numbers and spaces.
 * 
 * @author Adelina
 * 
 * @param e
 * @return
 */
 function restrictCharactersForAccount(e)
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
 	check = new RegExp(/[!@#$%^&*~`()\[\]+=<>{}?_\-:,.;/\"'\\|]/);
 	return !check.test(keychar);
 }
 //========================================================================================================
 /**
 * Restricts the user to input only characters, numbers and spaces and '.' .
 * 
 * @author Adelina
 * 
 * @param e
 * @return
 */
 function restrictCharactersForCapital(e)
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
 	check = new RegExp(/[!@#$%^&*~`()\[\]+=<>{}?_\-:,;/\"'\\|]/);
 	return !check.test(keychar);
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
	  
   var oConfigs = {
		   useShadow: true,
		   queryDelay: 0,
		   minQueryLength: 0,
		   animVert: .01
		   };
   
      // Instantiate the AutoComplete 
      var oAC = new YAHOO.widget.AutoComplete(inputId, autoCompleteContainer, oDS, oConfigs); 
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
         	},1);
      });     
  }
  
  /*
   * It is used for displaying the manager autocomplete and setting the firstName and lastName in the form hidden fields
   * @author adelina
   */
   
   function instantiateClientAutoComplete(oDS, inputId, autoCompleteContainer, clientNameHiddenFieldId, clientName){       	  
	   // Use a LocalDataSource
       oDS.responseSchema = {fields : ["name", "id"]};  	 
       // Instantiate the AutoComplete 
	   
	   if(clientName != null) {
			document.getElementById(inputId).value = clientName;
	   }
	  	  
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
   	   oAC.forceSelection = true;
   	   
       oAC.useShadow = true; 
                 
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
   
   //========================================================================================================
   /**
    * Validates the manager of the project in the project form
    */
   function validateProjectManagerSelection(){	
   	var div = document.getElementById('manager_message');
   	// we do not have a responsabile_message div	
   	if(div != null) {
   		div.innerHTML = "";
   		var input = document.getElementById("projectForm_manager");	
   		if (input.value != null && input.value != ""){
   			// we have a manager_message div, and we have a valid imput_value, 
   			// that means it is not null and it has characters 						
   			return true;
   		} else {
   			div.innerHTML = "<span class='validationErrorMessage'>" + 'PROJECTFORM_NO_MANAGER_ERROR'.translate('COMMON') + "</span";
   			// we have a manager_message div, and but we have a null input or an input with no characters		 			
   			return false;
   		}
   	} else { 		
   		return false;
   	}
  }	

 
 //========================================================================================================
   /**
    * Validates the client of the project in the project form
    */
   function validateClientSelection(){	
   	var div = document.getElementById('client_message');    	
   	var clientChecked = document.getElementById("projectForm_projectNoClient");   	
   	// we do not have a responsabile_message div	
   	if(div != null) {
   		div.innerHTML = "";
   		var returnFalse = false;
   		if(!clientChecked.checked) {
	   		var input = document.getElementById("projectForm_client");	
	   		if (input.value != null && input.value != ""){
	   			// we have a client_message div, and we have a valid imput_value, 
	   			// that means it is not null and it has characters 						
	   			returnFalse = true;
	   		} else {
	   			div.innerHTML = "<span class='validationErrorMessage'>" + 'PROJECTFORM_NO_CLIENT_ERROR'.translate('COMMON') + "</span";
	   			// we have a client_message div, and but we have a null input or an input with no characters		 			
	   			returnFalse = false;
	   		}
	   		return returnFalse;
   		}
   		return true;
   	} else { 	
   		alert('false');
   		return false;
   	}
  }
 
 
 /*var returnFalse = false;
				//verify if both the start time and end time are selected
				if (timeCheck.checked) {
					startTime = document.getElementById("recordForm_startTime").value;
					endTime = document.getElementById("recordForm_endTime").value;
					time = document.getElementById("recordForm_time").value;
					
					if( time == "" && (startTime != "" || endTime != "")) {
						if (startTime == null || startTime == "") {
							startTimeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_START_TIME_ERROR'.translate('COMMON') + "</span>";							
							returnFalse = true;
						}					
						
						if (endTime == null || endTime == "") {
							endTimeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_END_TIME_ERROR'.translate('COMMON') + "</span>";
							returnFalse = true;
						}
							
					}														
					if (time == null || time == '') {
						timeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_TIME_REQUIRED_ERROR'.translate('COMMON') + "</span>";
						returnFalse = true;
					} else {				
						if(!(FIC_checkField("formfield validate-time ", document.getElementById("recordForm_time")))) {									
							timeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_TIME_ERROR'.translate('COMMON') + "</span>";
							returnFalse = true;
						}
					}
				}		*/
 
 
 //========================================================================================================
   /**
    * Validates name input in project form
    */
   function validateProjectNameInput(){
   	var div = document.getElementById('name_message');
   	// we do not have a owner_message div	
   	if(div != null) {
   		div.innerHTML = "";
   		var input = document.getElementById("projectForm_name");	
   		if (input.value != null && input.value != ""){
   			// we have a name_message div, and we have a valid imput_value, 
   			// that means it is not null and it has characters 						
   			return true;
   		} else {
   			div.innerHTML = "<span class='validationErrorMessage'>" + 'PROJECTFORM_NAME_ERROR'.translate('COMMON') + "</span>";
   			// we have a name_message div, and but we have a null input or an input with no characters		
   			return false;
   		}
   	} else {
   		return false;
   	}
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
 
 //========================================================================================================
   /**
    * Dispalys a panel on Project form, to close a project
    * 
    * @author Adelina
    */
    function closeProject() {
   			
   	 function handleSubmit() {	 		
   		submitForm('Project.htm?ACTION=CLOSE_PROJECT',"projectForm", 'LOCAL_MESSAGES_CONTENT');
  		YAHOO.cm.closePanel.hide();
   		 
   	 }
   	 
   	 function handleCancel() { 		
   		YAHOO.cm.closePanel.hide(); 
   	 }
   	 
   	if (!YAHOO.cm.closePanel) { 		
   		YAHOO.cm.closePanel = new YAHOO.widget.SimpleDialog("ClosePanel", { 
   			width:"400px", visible:false, constraintoviewport:true, modal: true,
   			close: false,
   		    fixedcenter: true, 
   		    icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
   			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},
   			buttons : [ { text:this.submitButtonLabel, handler: handleSubmit},
   			            { text:this.cancelButtonLabel, handler: handleCancel}]
   		});
   	}
   	YAHOO.cm.closePanel.setHeader('<span>' + this.panelTitle + '</span>');
   	YAHOO.cm.closePanel.setBody(this.confirmationMessage);
   	YAHOO.cm.closePanel.render(document.body);
   	YAHOO.cm.closePanel.show();

   }
 
//==============================================================================================================================
/**
 * Displays a panel on ProjectTeam form, for adding a team Member
 * @param _url The url from where panel's content is retrieved
 * @param _panelTitle Panel't title
 * @author Adelina 
 */
 
function manageTeamMember(_url, _panelTitle, memberId){

	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	
	if (YAHOO.cm.showPersonPanel != null) {
		YAHOO.cm.showPersonPanel.destroy();
	}		
	
	YAHOO.cm.showPersonPanel = new YAHOO.widget.Panel("TeamMemberPanel", {xy:[300, 200], width:"340px", visible:false, modal: true, constraintoviewport:true, close: true, draggable:true, 
		effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	YAHOO.cm.showPersonPanel.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.cm.showPersonPanel.render(document.body); 	
	returnContentFromUrlToPanelAndShowPannelForUpdateExternalPerson(url, YAHOO.cm.showPersonPanel, memberId);
		
}

//==============================================================================================================================
/**
 * Displays a panel on ProjectTeam form, for adding an external team Member, when the project is first added
 * @param _url The url from where panel's content is retrieved
 * @param _panelTitle Panel't title
 * 
 * @author Adelina 
 */
 
function manageExternalTeamMember(_url, _panelTitle, id, memberId, member){	
	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	   	   	
	if (YAHOO.cm.showPersonPanel != null) {
		YAHOO.cm.showPersonPanel.destroy();
	}		
	
	YAHOO.cm.showPersonPanel = new YAHOO.widget.Panel("TeamMemberPanel", {xy:[300, 200], width:"340px", visible:false, modal: true, constraintoviewport:true, close: true, draggable:true, 
		effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	YAHOO.cm.showPersonPanel.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.cm.showPersonPanel.render(document.body); 	   
	
	returnContentFromUrlToPanelAndShowPannelForExternalPerson(url, YAHOO.cm.showPersonPanel, id, memberId, member);		
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
*	Used to open a panel containing a project
*	@author Adelina
*/
function displayInfoPanel(_url, _panelName, _panelTitle, _width_size) {
	var url = (this.url != null ? this.url : _url),
	panelName = (this.panelName != null ? this.panelName : _panelName),	
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),	
	width_size = (this.width_size != null ? this.width_size : _width_size);		
	
	if (YAHOO.cm.infoPanel != null){		
		YAHOO.cm.infoPanel.hide();
	}
		
	YAHOO.cm.infoPanel = new YAHOO.widget.Panel("InfoPanel", { fixedcenter:true, width: width_size, visible: false, constraintoviewport: true, close: true, draggable:true,
		 effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},  modal: true, zindex:1, iframe: true});
	YAHOO.cm.infoPanel.setHeader('<span title=\'' + panelTitle +'\'>' + panelName + '</span>'); 	
	
	YAHOO.cm.infoPanel.render(document.body);
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.cm.infoPanel);
	
} 

/**
 * Show the element's id from a form
 * 
 * @author Adelina
 * 
 * @param oForm
 * @return
 */
 function showElements(oForm) {
   var elements = new Array();	   
   for (i = 0; i < oForm.length; i++) {
	   elements[i] = oForm.elements[i].id;		 
   }
   return elements;
 }
 
 /**
  * Show the element's name from a form
  * 
  * @author Adelina
  * 
  * @param oForm
  * @return
  */
  function showElementsName(oForm) {
    var elements = new Array();	   
    for (i = 0; i < oForm.length; i++) {
 	   elements[i] = oForm.elements[i].name;		 
    }
    return elements;
  }
  
  /**
  * Sort the options from a select
  * 
  * @author Adelina
  
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