/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
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
 * 
 * Redirects to a given url.
 */
function redirect(url) {
	window.location=url;
}

//========================================================================================================

/**
 * Object used to addListner on getContentFromUrl function.
 * @author dan.damian
 * 
 */
function ObjGetContentFromUrl(url, container, withConfirmation) {
	this.url = url;
	this.container = container;
	this.withConfirmation = withConfirmation;
}

/**
 * Displays the content received from server into a div.
 * 
 * @author matti_joona
 */
function getContentFromUrl(){
	 
	var url = this.url;
	var divId = this.container;
	if (this.withConfirmation && YAHOO.om.confirmation){
		//The panel it's hard coded as it cannot be
		//passed as a parameter. 
		YAHOO.om.confirmation.hide();
	}	

	var loadObject = { 
		handleSucess:function (o){
			sessionExpired(o);
			if(o.responseText !== undefined){
				var divElem = document.getElementById(divId);
				divElem.innerHTML = o.responseText;
				//this is for executing javascripts elements
				YAHOO.om.smallLoading.hide();
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
			sessionExpired(o);
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.om.smallLoading.hide();

	    },
	    startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(url), callbackForLoad); 
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show Small Loading
	YAHOO.om.smallLoading.show();
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
			sessionExpired(o);
			if(o.responseText !== undefined){
				var divElem = document.getElementById(container);
				divElem.innerHTML = o.responseText; 
				//this is for executing javascripts elements
				var scriptTags = divElem.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				YAHOO.om.smallLoading.hide();
				 
			}
		},
		handleFailure:function (o){
			sessionExpired(o);
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.om.smallLoading.hide();
	    },
	    startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(url), callbackForLoad); 
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show Small Loading
	YAHOO.om.smallLoading.show();
    loadObject.startRequest();
}


/**
 * Adds a parameter with a random value to avoid IE and other browsers caching,
 * by making this request unique.
 * @param url
 * @return
 */
function makeUniqueRequest(url) {
	if (url.indexOf('?',0) > 0) {
		url = url + "&rp="+ Math.random();
	} else  {
		url = url + "?rp="+ Math.random();
	}
	return url;
}

//========================================================================================================
/**
 * Displays the content received from server into the document's body
 * 
 * @author coni
 */
function redirectToPage(url){
	if (this.withContext){
		url = this.url;
	}
	
	var loadObject = { 
		handleSucess:function (o){
			sessionExpired(o);
			if(o.responseText !== undefined){
				document.body.innerHTML = o.responseText;
				//this is for executing javascripts elements
				var scriptTags = document.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				YAHOO.om.smallLoading.hide();
				 
			}
		},
		handleFailure:function (o){
			sessionExpired(o);
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.om.smallLoading.hide();
	    },
	    startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(url), callbackForLoad); 
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show Small Loading
	YAHOO.om.smallLoading.show();
    loadObject.startRequest();
}
//========================================================================================================
/**
 * The same as getContentFromUrlDirect + plus displaying message to user
 * while the organisation tree is being updated 
 * Displays the content received from server into a div.
 * 
 * @author coni
 */
function getContentFromUrlDirectWithUpdatingOrgTreePanel(url, container){
	if (this.withContext){
		url = this.url;
		container = this.container;
	}
	var loadObject = { 
		handleSucess:function (o){
		sessionExpired(o);
			if(o.responseText != undefined){
				var divElem = document.getElementById(container);
				divElem.innerHTML = o.responseText; 
				//this is for executing javascripts elements
				var scriptTags = divElem.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				YAHOO.om.smallLoading.hide();
				YAHOO.om.orgTreeUpdating.hide();

			}
		},
		handleFailure:function (o){
			sessionExpired(o);
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.om.smallLoading.hide();
			YAHOO.om.orgTreeUpdating.hide();
	    },
	    startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(url), callbackForLoad); 
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show Small Loading
	YAHOO.om.smallLoading.show();
	YAHOO.om.orgTreeUpdating.show();
    loadObject.startRequest();
}

//========================================================================================================
/**
 * Checks what browser it is
 * 
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

//========================================================================================================
/**
 * Sets a YUI Panel's body to display the content received form server.
 * 
 * @author dd
 */
function returnContentFromUrlToPanelAndShowPannel(url, panel, div){
	
	//for the job panel we have to pass the json handler
	var handleChooseJobInDepartment = this.handleChooseJobInDepartment;
	
	var loadObject = { 
		handleSucess:function (o){
			sessionExpired(o);
			if(o.responseText !== undefined){
				//alert(o.responseText);		
				panel.setBody(o.responseText);				
				if(div != null){
					panel.render(document.getElementById(div));
				} else {
					panel.render(document.body);
				}
				
				//for the job panel we have to pass the json handler
				this.handleChooseJobInDepartment = handleChooseJobInDepartment;
				
				panel.show();
				
				var browser = whichBrs();
				if (browser == "Internet Explorer" || browser == "Chrome" || browser == "Safari" || browser == "Opera") {
					//----------------------------------------
					//DD: I have added manual evaluation of the scripts, because, starting with second time of 
					//panel appearance, the scripts were not executed.
					//alert(panel.body.innerHTML);
					var scriptTags = panel.body.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
					
				
					
				}
				
				//----------------------------------------
				
				YAHOO.om.smallLoading.hide();
			}
		},
		handleFailure:function (o){
			sessionExpired(o);
			alert("Error " + o.status + " : " + o.statusText);
			YAHOO.om.smallLoading.hide();
	    },
	    startRequest:function() {
			//make request
			YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(url), callbackForLoad); 
			
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	// Show the Panel
	YAHOO.om.smallLoading.show();
    loadObject.startRequest();
}

//========================================================================================================
/**
 * Sets a YUI Panel's body to display the content received from server for the branch panel
 * We make also the initial request
 * 
 * @author mitziuro
 */
function returnContentFromUrlToBranchPanelAndShowBranchPannel(url, panel, sessionOrgId,  sessionOrgName){

	var loadObject = { 
		handleSucess:function (o){
			sessionExpired(o);
			if(o.responseText != undefined){
				
				panel.setBody(o.responseText);
			
				//we render the panel
				panel.render(document.body);
				
				//finaly we show the panel 
				panel.show();
				
				YAHOO.om.smallLoading.hide();
			}
		},
		handleFailure:function (o){
			sessionExpired(o);
			YAHOO.om.smallLoading.hide();
			alert("Error " + o.status + " : " + o.statusText);
			
	    },
	    startRequest:function() {
	    	
	    	YAHOO.om.smallLoading.show();
	    	
	    	//we take the parent
	    	var parent = useBranch("get", 'organisationId', null, false);

	    	//if we use sessionOrgId option means we have the all branches option
	    	var display_option = null;
	    	if(sessionOrgId != null){
	    		display_option = true;
	    	}
	    	
	    	url = url.concat('&ORG=').concat(parent).concat('&ALL_OPTIONS=').concat(display_option);
	    	
			//make request
			YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(url), callbackForLoad); 
			
	    }
	}; 
	var callbackForLoad = {
		success:loadObject.handleSucess, 
		failure:loadObject.handleFailure
	};
	
    loadObject.startRequest();
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
			sessionExpired(response);
			var divElem = document.getElementById(_responseContainer);
			divElem.innerHTML = response.responseText;
		}
	};
	
	YAHOO.util.Connect.asyncRequest('POST', makeUniqueRequest(_url), uploadHandler);
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
 * This function is used for displaying the no modules error message when trying to add or update a role
 *
 * @author alu 
 */
function showNoModulesMessage(){
	if (document.getElementById('NO_MODULES') != null){
		var msg = "<span class='error_msg'>&nbsp;" + 'NO_MODULES_ERROR'.translate('COMMON') + "</span";
		document.getElementById('MESSAGE').innerHTML = msg;
		return false;
	} else {
		return true;
	}
}

//========================================================================================================
/*
 * Validates and submits out of office form
 */
function validateAndSubmitOOOForm(){
	var b = 'true';	
	if (this.hasPermission == 'true') {
		if (validateOwnerSelection() == "2") {			
			b = 'false';
		}
	}
	if((b == 'true') && (validateReplacementSelection())) {		
		validateAndSubmitFormWithParams(this.url, this.formId, this.container);
	}
	
}

//========================================================================================================
/**
 * Validates replacement selection in out of office form
 */
 function validateReplacementSelection(){
		var div = document.getElementById('replacement_message');
		div.innerHTML = "";
		var input = document.getElementById("oooForm_replacement");
		var inputOwner = document.getElementById("oooForm_owner");	
		if (input.value != null && input.value != ""){
			if (inputOwner.value != null && inputOwner.value != ""){
				if(inputOwner.value != input.value) {
					return true;
				} else {			
					div.innerHTML = "<span class='validationErrorMessage'>" + 'ANOTHER_REPLACEMENT_ERROR'.translate('COMMON') + "</span";
					return false;
				}
			}
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'NO_REPLACEMENT_ERROR'.translate('COMMON') + "</span";
			return false;
		}		
	}

//========================================================================================================
/**
 * Validates owner selection in out of office form
 */
function validateOwnerSelection(){
	var div = document.getElementById('owner_message');
	// we do not have a owner_message div
	var variable = "0";
	if(div != null) {
		div.innerHTML = "";
		var input = document.getElementById("oooForm_owner");	
		if (input.value != null && input.value != ""){
			// we have a owner_message div, and we have a valid imput_value, 
			// that means it is not null and it has characters 			
			variable = "1";
			return variable;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'NO_OWNER_ERROR'.translate('COMMON') + "</span";
			// we have a owner_message div, and but we have a null input or an input with no characters
			variable = "2";
			return variable;
		}
	} else {
		return variable;
	}
}

//========================================================================================================

function instantiatePersonAutoComplete(oDS, inputId, autoCompleteContainer, hiddenFieldId, addToAll){
    // Use a LocalDataSource
    // var oDS = new YAHOO.util.LocalDataSource(${JSON_PERSONS});
    oDS.responseSchema = {fields : ["name", "id"]};
 
    // Instantiate the AutoComplete 
    var oAC = new YAHOO.widget.AutoComplete(inputId, autoCompleteContainer, oDS); 
    
    oAC._bItemSelected = (oAC._elTextbox.value != '');       
    
    oAC.resultTypeList = false; 
    
    // enable string input contained
    oAC.queryMatchContains = true;
    
    // Show more results, scrolling is enabled via CSS
    oAC.maxResultsDisplayed = 100;
    
    // Enable force selection 
	oAC.forceSelection = true;

	//oAC.prehighlightClassName = "yui-ac-prehighlight"; 
    oAC.useShadow = true; 
      
    if(addToAll){
    	document.getElementById("oooForm_owner").focus();
    } else {
    	document.getElementById(inputId).focus();
    }  
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
    oAC.itemSelectEvent.subscribe(myHandler);
    
    YAHOO.util.Event.addListener(inputId, "click", function(){
    	setTimeout(function() { // For IE 
       	    oAC.sendQuery(" "); 
       	},0);
    });
    
}

//========================================================================================================

/**
* It is used for displaying the department autocomplete and setting the name in the form hidden field
*
* @author adelina
*/

function instantiateParentDepartmentAutoComplete(oDS, inputId, autoCompleteContainer, parentDepartmentNameHiddenFieldId){
    // Use a LocalDataSource
    oDS.responseSchema = {fields : ["name", "id"]};
            
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
    var myParentDepartmentHiddenField = YAHOO.util.Dom.get(parentDepartmentNameHiddenFieldId);    
    var myHandler = function(sType, aArgs) { 
        var myAC = aArgs[0]; // reference back to the AC instance 
        var elLI = aArgs[1]; // reference to the selected LI element 
        var oData = aArgs[2]; // object literal of selected item's result data 
         
        // update hidden form field with the selected item's ID
        myParentDepartmentHiddenField.value = oData.id;            
    }; 
    oAC.itemSelectEvent.subscribe(myHandler);
    
    YAHOO.util.Event.addListener(inputId, "click", function(){
    	setTimeout(function() { // For IE 
       	    oAC.sendQuery(" "); 
       	},0);
    });

}

//========================================================================================================

/*
* It is used for displaying the manager autocomplete and setting the firstName and lastName in the form hidden fields
* @author adelina
*/

function instantiateManagerAutoComplete(oDS, inputId, autoCompleteContainer, managerFirstNameHiddenFieldId, managerLastNameHiddenFieldId){
    // Use a LocalDataSource
    oDS.responseSchema = {fields : ["name", "firstName", "lastName"]};
 
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
    var myFirstNameHiddenField = YAHOO.util.Dom.get(managerFirstNameHiddenFieldId);
    var myLastNameHiddenField = YAHOO.util.Dom.get(managerLastNameHiddenFieldId);  
    var myHandler = function(sType, aArgs) { 
        var myAC = aArgs[0]; // reference back to the AC instance 
        var elLI = aArgs[1]; // reference to the selected LI element 
        var oData = aArgs[2]; // object literal of selected item's result data 
         
        // update hidden form field with the selected item's ID
        myFirstNameHiddenField.value = oData.firstName;
        myLastNameHiddenField.value = oData.lastName;
    }; 
    oAC.itemSelectEvent.subscribe(myHandler);
    
    YAHOO.util.Event.addListener(inputId, "click", function(){
    	setTimeout(function() { // For IE 
       	    oAC.sendQuery(" "); 
       	},0);
    });

}

//========================================================================================================

/**
* It is used for displaying the department autocomplete and setting the name in the form hidden field
*
* @author adelina
*/

function instantiatePersonDepartmentAutoComplete(oDS, inputId, autoCompleteContainer, departmentHiddenFieldId){
    // Use a LocalDataSource
    oDS.responseSchema = {fields : ["name", "id"]};
            
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
    var departmentHiddenField = YAHOO.util.Dom.get(departmentHiddenFieldId);    
    var myHandler = function(sType, aArgs) { 
        var myAC = aArgs[0]; // reference back to the AC instance 
        var elLI = aArgs[1]; // reference to the selected LI element 
        var oData = aArgs[2]; // object literal of selected item's result data 
         
        // update hidden form field with the selected item's ID
        departmentHiddenField.value = oData.id;            
    }; 
    oAC.itemSelectEvent.subscribe(myHandler);
    
    YAHOO.util.Event.addListener(inputId, "click", function(){
    	setTimeout(function() { // For IE 
       	    oAC.sendQuery(" "); 
       	},0);
    });

}



//========================================================================================================
/*
* It is used for displaying the OOO owner and replacement name autocomplete and setting the owner and replacement
* first and lastname in the form hidden fields
* @author coni
*/
function instantiateOOOSearchPersonAutoComplete(oDS, inputId, autoCompleteContainer, firstNameHiddenFieldId, lastNameHiddenFieldId){   
	
	// Use a LocalDataSource
    // var oDS = new YAHOO.util.LocalDataSource(${JSON_PERSONS});
    oDS.responseSchema = {fields : ["name", "firstName", "lastName"]};
 
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
    
	// Enable an iFrame shim under the container element
    oAC.useIFrame = true;
    
    document.getElementById("myOwnerInput").focus();
     
    // Define an event handler to populate a hidden form field 
    // when an item gets selected 
    var myFirstNameHiddenField = YAHOO.util.Dom.get(firstNameHiddenFieldId);
    var myLastNameHiddenField = YAHOO.util.Dom.get(lastNameHiddenFieldId);  
    var myHandler = function(sType, aArgs) { 
        var myAC = aArgs[0]; // reference back to the AC instance 
        var elLI = aArgs[1]; // reference to the selected LI element 
        var oData = aArgs[2]; // object literal of selected item's result data 
         
        // update hidden form field with the selected item's ID
        myFirstNameHiddenField.value = oData.firstName;
        myLastNameHiddenField.value = oData.lastName;
    }; 
    oAC.itemSelectEvent.subscribe(myHandler);
    
    YAHOO.util.Event.addListener(inputId, "click", function(){
    	setTimeout(function() { // For IE 
       	    oAC.sendQuery(" "); 
       	},0);
    });
    
}


//=======================================================================================================
function instantiateCalendarFreedayCal(){

	YAHOO.om.calendarFreedayCal = new YAHOO.widget.Calendar("calendarFreedayCal","calendarFreedayCalContainer",{title:"Date", close:true}); 
	
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
	YAHOO.om.calendarFreedayCal.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
	YAHOO.om.calendarFreedayCal.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]);
	
	YAHOO.om.calendarFreedayCal.render(document.body);
	//YAHOO.om.calendarFreedayCal.render();
	//YAHOO.util.Event.addListener("freedayForm_day", "click", YAHOO.om.calendarFreedayCal.show, YAHOO.om.calendarFreedayCal, true);
	YAHOO.om.calendarFreedayCal.show();
	
	function handleSelectCalendarFreedayCal(type,args,obj) { 
	    var dates = args[0]; 
	    var date = dates[0]; 
	    var year = date[0], month = date[1], day = date[2]; 
	    
	    var txtDate = document.getElementById("freedayForm_day"); 
	    txtDate.value = day + "-" + month + "-" + year;
	    document.getElementById('calendarFreedayCalContainer').style.display='none';	
	   
	}	 
	YAHOO.om.calendarFreedayCal.selectEvent.subscribe(handleSelectCalendarFreedayCal, YAHOO.om.calendarFreedayCal, true);
}

//=======================================================================================================

var startWorkHours = "";
var startWorkMinutes = "";

var endWorkHours = "";
var endWorkMinutes = "";

var startWork = false;
var endWork = false;

function instantiateCalendarStartWork(timePanelTitle){
		
	// time panel 	
	YAHOO.om.showCalendarStartWorkTimePanel = new YAHOO.widget.Panel("showCalendarStartWorkTimePanel", { xy:[528, 208], width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	
	YAHOO.om.showCalendarStartWorkTimePanel.setHeader('<span>' + timePanelTitle + '</span>');
	YAHOO.om.showCalendarStartWorkTimePanel.render(document.body); 	
		
	function handleSelectCalendarStartWork(type,args,obj) {
		
		var txtStartTime = document.getElementById("calendarForm_startwork");				  
		
		var txtEndTime = document.getElementById("calendarForm_endwork"); 	  
		
    	if(txtEndTime != null) {
    		var temp = new Array();
    		temp = txtEndTime.value.split(':');
    		var end_h = temp[0];
    		var end_min = temp[1];
    	}
    	
    	endWorkHours = end_h;
    	endWorkMinutes = end_min;
    	
    	txtStartTime.value = null;	     	
    	endWork = false;
    	startWork = true;      	
    	// time panel call	   	       	        	
    	showCalendarStartWorkTimePanel('CalendarShowTimeController.htm?panel=start&start_h=' + 0 + '&end_h='+(parseFloat(endWorkHours)), timePanelTitle);    	
    	
	}	

	YAHOO.util.Event.addListener("calendarForm_startwork", "click", handleSelectCalendarStartWork, YAHOO.om.showCalendarStartWorkTimePanel, true);

}

//=======================================================================================================



function instantiateCalendarEndWork(timePanelTitle){
	
	
	
	// time panel	
	YAHOO.om.showCalendarEndWorkTimePanel = new YAHOO.widget.Panel("showCalendarEndWorkTimePanel", { xy:[528, 233], width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	
	YAHOO.om.showCalendarEndWorkTimePanel.setHeader('<span>' + timePanelTitle + '</span>');
	YAHOO.om.showCalendarEndWorkTimePanel.render(document.body); 	
		
	function handleSelectCalendarEndWork(type,args,obj) { 
		
		var txtStartTime = document.getElementById("calendarForm_startwork");
		
		if(txtStartTime != null) {
			var temp = new Array();
			temp = txtStartTime.value.split(':');
			var start_h = temp[0];
			var start_min = temp[1];			
		}
		
		startWorkHours = start_h;
		startWorkMinutes = start_min;								
		
		var txtEndTime = document.getElementById("calendarForm_endwork"); 	  
		
    	if(txtEndTime != null) {
    		var temp = new Array();
    		temp = txtEndTime.value.split(':');
    		var end_h = temp[0];
    		var end_min = temp[1];    
    	}
    	
    	endWorkHours = end_h;
    	endWorkMinutes = end_min;
    	       
    	txtEndTime.value = null;    	    
    	startWork = false;
    	endWork = true;    	
	    // time panel call	       	
    	showCalendarEndWorkTimePanel('CalendarShowTimeController.htm?panel=end&start_h=' + (parseFloat(startWorkHours)) + '&start_m='+ (parseInt(startWorkMinutes/15)) , timePanelTitle);    	
    	
    
	}	
	
	YAHOO.util.Event.addListener("calendarForm_endwork", "click", handleSelectCalendarEndWork, YAHOO.om.showCalendarStartWorkTimePanel, true);
}


//=======================================================================================================
var startDate="";
var endDate="";

var startHours="";
var startMinutes="";
function instantiateOOOStartCal(timePanelTitle){
			
	var endDateTemp="";
	var startDateTemp="";
	
	startHours="";
	startMinutes="";	
	
	var x = 0, y = 0;
	
	var containerDivId = 'oooStartCalContainer';
		
	var element = containerDivId;
	element = document.getElementById(element);
	element.style.zIndex = 2;
			
	//get the offset
	while(element){
		x += element.offsetLeft;
		y += element.offsetTop;
		element = element.offsetParent;					
	}	

	//set the offset
	YAHOO.om.oooStartPanelContainer.moveTo(x, y);

	if(whichBrs() == 'Internet Explorer'){
		YAHOO.om.oooStartPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + containerDivId + '_oooStartPanelContainer" />');
	} else {
		YAHOO.om.oooStartPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + containerDivId + '_oooStartPanelContainer" />');
	}
	YAHOO.om.oooStartPanelContainer.render(document.body);		
	
	
	// START PERIOD
	if(endDate != ""){			
		YAHOO.om.oooStartCal = new YAHOO.widget.Calendar("oooStartCal", containerDivId + '_oooStartPanelContainer', {maxdate:endDate, mindate:new Date(), title:"Date", close:true });
		endDateTemp = endDate;
    } else {    	
  		YAHOO.om.oooStartCal = new YAHOO.widget.Calendar("oooStartCal", containerDivId + '_oooStartPanelContainer', {mindate:new Date(), title:"Date", close:true });
    }
	
	 //show the container		  	
	 YAHOO.util.Event.addListener("oooForm_startPeriod", "click", YAHOO.om.oooStartPanelContainer.show, YAHOO.om.oooStartPanelContainer, true);
	 			
	function handleSelectOOOStartCal(type,args,obj) { 
	    var dates = args[0]; 
	    var date = dates[0]; 
	    var year = date[0], month = date[1], day = date[2]; 
	 	if (month < 10){
	    	month = "0" + month;
	    }
	 	if (day < 10){
	    	day = "0" + day;
	    }
	    
	    var txtDate1 = document.getElementById("oooForm_startPeriod"); 
	    txtDate1.value = day + "-" + month + "-" + year;
	    
	    // startDate	    
	    startDate = month + "/" + day + "/" + year;		 
	    startDateTemp = startDate;
	   	  	    
	    instantiateOOOEndCal(timePanelTitle);	    
	 	
	    endDate ="";
	 	startDate="";
	 	
		//hide the container
 		YAHOO.om.oooStartPanelContainer.hide(); 	  	    

 		//document.getElementById('oooStartCalContainer').style.display='none';	    
	    	    	    
	    // time panel call	   
	    showOOOStartPeriodTimePanel('OOOShowTimeController.htm?panel=start', timePanelTitle);		   	  
	    if(startDateTemp == endDateTemp){	    	
	    	startHours = manageOOOPeriodTimeHours();
	    	startMinutes = manageOOOPeriodTimeMinutes();		 
	    }	   	   
	}
	
	//function for handling hide calendar
	function hideHandler(type,args,obj) {
		//hide the container		
		YAHOO.om.oooStartPanelContainer.hide();				
		YAHOO.om.oooStartCal.show();
		
	} 	
		
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
	YAHOO.om.oooStartCal.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
	YAHOO.om.oooStartCal.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
	 	
	YAHOO.om.oooStartCal.selectEvent.subscribe(handleSelectOOOStartCal, YAHOO.om.oooStartCal, true);	
	YAHOO.om.oooStartCal.hideEvent.subscribe(hideHandler, YAHOO.om.oooStartCal, true); 
	YAHOO.om.oooStartCal.render();		
	YAHOO.om.oooStartCal.show();	
}

//=======================================================================================================
var endHours = "";
var endMinutes = "";

function instantiateOOOEndCal(timePanelTitle){
			
	var startDateTemp="";
	var endDateTemp="";	
	
	endHours = "";
	endMinutes = "";
	
	var x = 0, y = 0;
	
	var containerDivId = 'oooEndCalContainer';
		
	var element = containerDivId;
	element = document.getElementById(element);
	element.style.zIndex = 1;
		
	//get the offset
	while(element){
		x += element.offsetLeft;
		y += element.offsetTop;
		element = element.offsetParent;			
	}	
	
	//set the offset
	YAHOO.om.oooEndPanelContainer.moveTo(x, y);
	
	if(whichBrs() == 'Internet Explorer'){
		YAHOO.om.oooEndPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + containerDivId + '_oooEndPanelContainer" />');
	} else {
		YAHOO.om.oooEndPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + containerDivId + '_oooEndPanelContainer" />');
	}
	
	YAHOO.om.oooEndPanelContainer.render(document.body);	
	
	 // END PERIOD		
	if(startDate != ""){		
		YAHOO.om.oooEndCal = new YAHOO.widget.Calendar("oooEndCal", containerDivId + '_oooEndPanelContainer', {mindate:startDate, title:"Date", close:true });
		startDateTemp = startDate;
   	} else {   		
   		YAHOO.om.oooEndCal = new YAHOO.widget.Calendar("oooEndCal", containerDivId + '_oooEndPanelContainer', {mindate:new Date(), title:"Date", close:true });   	
    }	
		
	YAHOO.util.Event.addListener("oooForm_endPeriod", "click", YAHOO.om.oooEndPanelContainer.show, YAHOO.om.oooEndPanelContainer, true);	
		
	function handleSelectOOOEndCal(type,args,obj) { 
	    var dates = args[0]; 
	    var date = dates[0];
	    var year = date[0], month = date[1], day = date[2]; 
	 	if (month < 10){
	    	month = "0" + month;
	    }
	 	if (day < 10){
	    	day = "0" + day;
	    }
	    
	    var txtDate2 = document.getElementById("oooForm_endPeriod"); 
	    txtDate2.value = day + "-" + month + "-" + year;
	    
	    // endDate
	    endDate = month + "/" + day + "/" + year;
	    endDateTemp = endDate;
	   	    	    
	    instantiateOOOStartCal(timePanelTitle);	    	    
	    
	    startDate ="";
	    endDate ="";
	    
	   //hide the container
 		YAHOO.om.oooEndPanelContainer.hide(); 	  	  
	    
	    //document.getElementById('oooEndCalContainer').style.display='none';
	    	   
	    // time panel call		    
	    if(startDateTemp == endDateTemp){	    
	    	endHours = manageOOOPeriodTimeHours(); 
	    	endMinutes = manageOOOPeriodTimeMinutes(); 	
	    	
	    	if(new Date().getMonth() + 1 <= 9){	    		
	    		if(new Date().getDate() <= 9){
	    			dateNow = "0" + (new Date().getMonth() + 1) + "/" + ("0" + (new Date().getDate())) + "/" + new Date().getFullYear();
	    		} else {
	    			dateNow = "0" + (new Date().getMonth() + 1) + "/" + new Date().getDate() + "/" + new Date().getFullYear();
	    		}
	    	} else {	    		
	    		if(new Date().getDate() <= 9){
	    			dateNow = new Date().getMonth() + 1 + "/" + ("0" + (new Date().getDate())) + "/" + new Date().getFullYear();
	    		} else {
	    			dateNow = (new Date().getMonth() + 1) + "/" + new Date().getDate() + "/" + new Date().getFullYear();
	    		}
	    	}	    	    	
	    	minutesNow = new Date().getMinutes();
	    		    	
	    	if(startDateTemp == dateNow){	    			    	
	    		if(minutesNow >= 0 && minutesNow < 15 ){	    			
	    			showOOOEndPeriodTimePanel('OOOShowTimeController.htm?panel=end&start_h=' + (parseInt(new Date().getHours())) + '&start_m='+ 1, timePanelTitle);
	    		} else if(minutesNow >= 15 && minutesNow < 30){	    			
	    			showOOOEndPeriodTimePanel('OOOShowTimeController.htm?panel=end&start_h=' + (parseInt(new Date().getHours())) + '&start_m='+ 2, timePanelTitle);
	    		} else if(minutesNow >= 30 && minutesNow < 45){	    			
	    			showOOOEndPeriodTimePanel('OOOShowTimeController.htm?panel=end&start_h=' + (parseInt(new Date().getHours())) + '&start_m='+ 3, timePanelTitle);
	    		} else if(minutesNow > 45){	    			
	    			showOOOEndPeriodTimePanel('OOOShowTimeController.htm?panel=end&start_h=' + (parseInt(new Date().getHours() + 1)) + '&start_m='+ 0, timePanelTitle);
	    		}
	    	} else {	    	
		    	if(endMinutes == "45"){
		    	} else {
		    		showOOOEndPeriodTimePanel('OOOShowTimeController.htm?panel=end&start_h=' + endHours + '&start_m='+ (parseInt(endMinutes/15) + 1), timePanelTitle);		
		    	}
	    	}
	    } else {
	    	showOOOEndPeriodTimePanel('OOOShowTimeController.htm?panel=end', timePanelTitle);	    	
	    }
	}	 
	
	//function for handling hide calendar
	function hideHandler(type,args,obj) {
		//hide the container		
		YAHOO.om.oooEndPanelContainer.hide();				
		YAHOO.om.oooEndCal.show();
	} 	
		
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
	YAHOO.om.oooEndCal.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
	YAHOO.om.oooEndCal.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]); 
	 	
	YAHOO.om.oooEndCal.selectEvent.subscribe(handleSelectOOOEndCal, YAHOO.om.oooEndCal, true);	
	YAHOO.om.oooEndCal.hideEvent.subscribe(hideHandler, YAHOO.om.oooEndCal, true); 
	YAHOO.om.oooEndCal.render();		
	YAHOO.om.oooEndCal.show();
	
	
	
}
/**
 * To populate minutes on hour change for out of office
 * @return
 */
function populateMinutes()
{
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
 * To populate minutes on change hour for the calendar
 * @return
 */

function populateMinutesOnChangeHour()
{	
	var hourlist=document.getElementById("hour");
	var hour = hourlist.options[hourlist.selectedIndex].text;	
	var minuteslist = document.getElementById("minutes");
	
	var myMinutes = new Array();
	myMinutes[0] = "00";
    myMinutes[1] = "15";
    myMinutes[2] = "30";
    myMinutes[3] = "45";
     
    var minlistsize = minuteslist.options.length;
    var b = false;
               
    if(startWork && !endWork){    
    	startWorkHours = "";
    	startWorkMinutes = ""
    }
                            
    if(hour == parseFloat(startWorkHours)){        		    
    	if(hour == parseFloat(endWorkHours)){
    		startWorkHours = "";
    		startWorkMinutes = ""
    	}
    	
		// startWorkMinutes			
    	if(startWorkMinutes == myMinutes[0]){			
			for(var j = 0; j < 1; j++){
				minuteslist.remove(0);				
			}	
			return;
		
		}    	
		if(startWorkMinutes == myMinutes[1]){					
			for(var j = 0; j < 1; j++){
				minuteslist.remove(0);				
			}	
			return;
		
		}
		if(startWorkMinutes == myMinutes[2]){		
			for(var j = 0; j < 2; j++){
				minuteslist.remove(0);				
			}
			return;
		
		}
		if(startWorkMinutes == myMinutes[3]){				
			for(var j = 0; j < 3; j++){
				minuteslist.remove(0);				
			}	
			return;
		}
    } else {        	
    	b = true;
    	if(hour != parseFloat(endWorkHours)) {
    		
		    var i = 0;
		    // delete all from list
		    for (i = 0; i < minlistsize; i++) {
		        minuteslist.remove(0);
		    }
		    for (i = 0; i < myMinutes.length; i++) {
		        minuteslist.options[i] = new Option(myMinutes[i], myMinutes[i]);
		    }		    
		    return;
    	}    	

    }
     	     
	if(hour == parseFloat(endWorkHours)){				
		if(b && !startWork && endWork ) {			
			var i = 0;
		    // delete all from list
		    for (i = 0; i < minlistsize; i++) {
		        minuteslist.remove(0);
		    }
		    for (i = 0; i < myMinutes.length; i++) {
		        minuteslist.options[i] = new Option(myMinutes[i], myMinutes[i]);
		    }  
		} else {							
			if(hour == parseFloat(startWorkHours)){
				endWorkHours = "";
				endWorkMinutes = ""
	    	}
			
			// endWorkMinutes
			if(endWorkMinutes == myMinutes[0]){						
				for(var j = 3; j > 0; j--){
					minuteslist.remove(j);				
				}
				return;
				
			}
			
			if(endWorkMinutes == myMinutes[1]){					
				for(var j = 3; j > 1; j--){					
					minuteslist.remove(j);
				}			
				return;
			}
			
			if(endWorkMinutes == myMinutes[2]){				
				for(var j = 3; j > 2; j--){
					minuteslist.remove(j);
				}
				return;
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

//=======================================================================================================
 var startDateSearch="";
 var endDateSearch="";

 function instantiateOOOSearchStartCal(timePanelTitle){	 	
	var endDateSearchTemp="";
 	var startDateSearchTemp="";
 	
 	startHours="";
 	startMinutes="";
 	 		
	var x = 0, y = 0;
	
	var containerDivId = 'oooSearchStartCalContainer';
		
	var element = containerDivId;
	element = document.getElementById(element);
	element.style.zIndex = 2;
		
	//get the offset
	while(element){
		x += element.offsetLeft;
		y += element.offsetTop;
		element = element.offsetParent;				
	}	

	//set the offset
	YAHOO.om.oooSearchStartPanelContainer.moveTo(x, y);	
	
	if(whichBrs() == 'Internet Explorer'){
		YAHOO.om.oooSearchStartPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + containerDivId + '_oooSearchStartPanelContainer" />');
	} else {
		YAHOO.om.oooSearchStartPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + containerDivId + '_oooSearchStartPanelContainer" />');
	}
	
	YAHOO.om.oooSearchStartPanelContainer.render(document.body);
	
	
	// START PERIOD
 	if(endDateSearch != ""){		
 		YAHOO.om.oooSearchStartCal = new YAHOO.widget.Calendar("oooSearchStartCal", containerDivId + '_oooSearchStartPanelContainer', {maxdate:endDateSearch, title:"Date", close:true });
 		endDateSearchTemp = endDateSearch;		
     } else {    	
   		YAHOO.om.oooSearchStartCal = new YAHOO.widget.Calendar("oooSearchStartCal", containerDivId + '_oooSearchStartPanelContainer', {title:"Date", close:true });  		
     }	
	
	 //show the container		  	
	 YAHOO.util.Event.addListener("oooSearchForm_startPeriod", "click", YAHOO.om.oooSearchStartPanelContainer.show, YAHOO.om.oooSearchStartPanelContainer, true);
 	 	 	  
 	function handleSelectOOOSearchStartCal(type,args,obj) { 
 	    var dates = args[0]; 
 	    var date = dates[0]; 
 	    var year = date[0], month = date[1], day = date[2]; 
 	    if (month < 10){
 	    	month = "0" + month;
 	    }
 	 	if (day < 10){
 	    	day = "0" + day;
 	    }
 	    
 	    var txtDate1 = document.getElementById("oooSearchForm_startPeriod"); 
 	    txtDate1.value = day + "-" + month + "-" + year;
 	    
 	    // startDateSearch	    
 	    startDateSearch = month + "/" + day + "/" + year;	
 	    startDateSearchTemp = startDateSearch;
 	    
 	    instantiateOOOSearchEndCal(timePanelTitle);	   
 	    
 	    startDateSearch="";
 		endDateSearch="";
 		
 		//hide the container
 		YAHOO.om.oooSearchStartPanelContainer.hide(); 	  	    
 	    
 	    // time panel call
 	    showOOOSearchStartPeriodTimePanel('OOOShowTimeController.htm?panel=startSearch', timePanelTitle);
 	    if(startDateSearchTemp == endDateSearchTemp){	    	
 	    	startHours = manageOOOPeriodTimeHours();  
 	    	startMinutes = manageOOOPeriodTimeMinutes();		 
 	    }	   	   	    
 	}	
 	
 	//function for handling hide calendar
	function hideHandler(type,args,obj) {
		//hide the container		
		YAHOO.om.oooSearchStartPanelContainer.hide();				
		YAHOO.om.oooSearchStartCal.show();
	} 	
	
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
 	YAHOO.om.oooSearchStartCal.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
 	YAHOO.om.oooSearchStartCal.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]);  	
 	
 	YAHOO.om.oooSearchStartCal.selectEvent.subscribe(handleSelectOOOSearchStartCal, YAHOO.om.oooSearchStartCal, true);
 	YAHOO.om.oooSearchStartCal.hideEvent.subscribe(hideHandler, YAHOO.om.oooSearchStartCal, true); 
 	YAHOO.om.oooSearchStartCal.render();
 	YAHOO.om.oooSearchStartCal.show();
 	
 }


//=======================================================================================================	
function instantiateOOOSearchEndCal(timePanelTitle){
	
	var startDateSearchTemp="";
	var endDateSearchTemp="";
	
	endHours="";
	endMinutes="";
	
	var x = 0, y = 0;
	
	var containerDivId = 'oooSearchEndCalContainer';
		
	var element = containerDivId;
	element = document.getElementById(element);
	element.style.zIndex = 1;	
	
	//get the offset
	while(element){
		x += element.offsetLeft;
		y += element.offsetTop;
		element = element.offsetParent;			
	}	

	//set the offset
	YAHOO.om.oooSearchEndPanelContainer.moveTo(x, y);
	
	if(whichBrs() == 'Internet Explorer'){
		YAHOO.om.oooSearchEndPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px;width:150px;" id="' + containerDivId + '_oooSearchEndPanelContainer" />');
	} else {
		YAHOO.om.oooSearchEndPanelContainer.setBody('<div style="position:relative;left:-9px;top:-10px; width:150px;" id="' + containerDivId + '_oooSearchEndPanelContainer" />');
	}	
	YAHOO.om.oooSearchEndPanelContainer.render(document.body);
	
	// END PERIOD
	if(startDateSearch != ""){		
		YAHOO.om.oooSearchEndCal = new YAHOO.widget.Calendar("oooSearchEndCal", containerDivId + '_oooSearchEndPanelContainer', {mindate:startDateSearch, title:"Date", close:true });
		startDateSearchTemp = startDateSearch;
   	} else {   		
   		YAHOO.om.oooSearchEndCal = new YAHOO.widget.Calendar("oooSearchEndCal", containerDivId + '_oooSearchEndPanelContainer', { title:"Date", close:true }); 
    }

	//show the container		  		
	YAHOO.util.Event.addListener("oooSearchForm_endPeriod", "click", YAHOO.om.oooSearchEndPanelContainer.show, YAHOO.om.oooSearchEndPanelContainer, true);
		
	function handleSelectOOOSearchEndCal(type,args,obj) { 	
	    var dates = args[0]; 
	    var date = dates[0]; 
	    var year = date[0], month = date[1], day = date[2]; 
	 	if (month < 10){
	    	month = "0" + month;
	    }
	 	if (day < 10){
	    	day = "0" + day;
	    }
	 
	    var txtDate2 = document.getElementById("oooSearchForm_endPeriod"); 
	    txtDate2.value = day + "-" + month + "-" + year;
	    	    
	    // endDateSearch
	    endDateSearch = month + "/" + day + "/" + year;
	    endDateSearchTemp = endDateSearch;
	    
	    instantiateOOOSearchStartCal(timePanelTitle);
	    
	    startDateSearch ="";
	    endDateSearch ="";
	    
		//hide the container
 		YAHOO.om.oooSearchEndPanelContainer.hide(); 	  
	    	      
	    // time panel call
	    if(startDateSearchTemp == endDateSearchTemp){
	    	endHours = manageOOOPeriodTimeHours();
	    	endMinutes = manageOOOPeriodTimeMinutes();	    		    
	    	if(endMinutes == "45"){
	    		showOOOSearchEndPeriodTimePanel('OOOShowTimeController.htm?panel=endSearch&start_h=' + (parseInt(endHours) + 1) + '&start_m='+ (endMinutes - 45), timePanelTitle);
	    	} else {
	    		showOOOSearchEndPeriodTimePanel('OOOShowTimeController.htm?panel=endSearch&start_h=' + endHours + '&start_m='+ (parseInt(endMinutes/15) + 1), timePanelTitle);
	    	}	    	
	    } else {
	    	showOOOSearchEndPeriodTimePanel('OOOShowTimeController.htm?panel=endSearch', timePanelTitle);
	    }	    	  
	}
	
	//function for handling hide calendar
	function hideHandler(type,args,obj) {
		//hide the container		
		YAHOO.om.oooSearchEndPanelContainer.hide();				
		YAHOO.om.oooSearchEndCal.show();
	} 	
		
	var setting_list = getCalendarParametersForLocale("LOCALE".translate('COMMON'));
	YAHOO.om.oooSearchEndCal.cfg.setProperty("MONTHS_LONG", setting_list[0]); 
	YAHOO.om.oooSearchEndCal.cfg.setProperty("WEEKDAYS_SHORT", setting_list[1]);
	
	YAHOO.om.oooSearchEndCal.selectEvent.subscribe(handleSelectOOOSearchEndCal, YAHOO.om.oooSearchEndCal, true);
	YAHOO.om.oooSearchEndCal.hideEvent.subscribe(hideHandler, YAHOO.om.oooSearchEndCal, true); 
 	YAHOO.om.oooSearchEndCal.render();
 	YAHOO.om.oooSearchEndCal.show();	
}

//=======================================================================================================
/**
* Sets the value of a given input("inputId") with the name of a person from the "persons" list having "personId"
*/
function setPersonNameForID(persons, personId, inputId) {
	var personsArray = eval(persons);
	for (i = 0; i < personsArray.length; i++){
		if (personsArray[i].id == personId) {
			document.getElementById(inputId).value = personsArray[i].name;
		}
	}
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

/**
 * Changes the class name of the menu buttons in order to display them
 * 
 * @author alu
 * 
*/
function displayMenuOptions() {
	var elements = YAHOO.util.Dom.getElementsByClassName('menuDisplayNone');
	for (el in elements){
		elements[el].className = "menuDisplayTableCell";
	}
}

 
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
	this.ro_months[8]  = "Septembrie";
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
			formattedDate = this.en_dayOfTheWeek[c_dayOfWeek] + " " + this.en_months[c_month] + " " + c_dayOfMonth  + " " + c_year;
		} else if (this.locale.toUpperCase() == "RO") {
			formattedDate = this.ro_dayOfTheWeek[c_dayOfWeek] + " " + c_dayOfMonth + " " + this.ro_months[c_month] + " " + c_year;
		}
		
		var time = formattedDate;
		if (this.showClock) {
			time = time + " " + formattedTime;
		}
		
		document.getElementById(this.divId).innerHTML = time; 
	}
} 

 /**
  * Verifies and displays all listeners added on an element.
  * 
  * @param elementId
  * @param event
  * @return
  */
function verifyListenersOnElement(elementId, event) {
	var listeners;
	if (event != null) {
		 listeners = YAHOO.util.Event.getListeners(document.getElementById(elementId), event);
	}	else {
		 listeners = YAHOO.util.Event.getListeners(document.getElementById(elementId));
	}
		
	alert('listeners: ' + (listeners != null ? listeners.length : 'null'));
}

/**
 * Removes all listeners added on an element.
 * 
 * @param elementId
 * @param event
 * @return
 */
function removeListenersFromElement(elementId, event) {
	if (event != null) {
		 listeners = YAHOO.util.Event.purgeElement(document.getElementById(elementId), false, event);
	}	else {
		 listeners = YAHOO.util.Event.purgeElement(document.getElementById(elementId));
	}
	
}
 
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
 * Handles the exired session event
 * @author mitziuro
 * @return
 */
function sessionExpired(oResponse){
	 if (YAHOO.om.sessionExpired && oResponse.responseText == SESSION_EXPIRED){
		YAHOO.om.sessionExpired.show();
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

/**
 * The function will accept a CSS selector (classname, divname, etc)
 * and if it exists will pass back the style object.
 * 
 * @author Adelina
 * 
 * @return
 */
function getCSSRule(ruleName, deleteFlag) {               // Return requested style obejct
   	ruleName=ruleName.toLowerCase();                       // Convert test string to lower case.	    
	if (document.styleSheets) {                            // If browser can play with stylesheets
      for (var i=0; i<document.styleSheets.length; i++) { // For each stylesheet
         var styleSheet=document.styleSheets[i];          // Get the current Stylesheet
         var ii=0;                                        // Initialize subCounter.
         var cssRule=false;                               // Initialize cssRule. 
         do {                                             // For each rule in stylesheet
            if (styleSheet.cssRules) {                    // Browser uses cssRules?
               cssRule = styleSheet.cssRules[ii];         // Yes --Mozilla Style
            } else {                                      // Browser usses rules?
               cssRule = styleSheet.rules[ii];            // Yes IE style. 
            }                                             // End IE check.
            if (cssRule)  {                               // If we found a rule...
               if (cssRule.selectorText.toLowerCase()==ruleName) { //  match ruleName?
                  if (deleteFlag=='delete') {             // Yes.  Are we deleteing?
                     if (styleSheet.cssRules) {           // Yes, deleting...
                        styleSheet.deleteRule(ii);        // Delete rule, Moz Style
                     } else {                             // Still deleting.
                        styleSheet.removeRule(ii);        // Delete rule IE style.
                     }                                    // End IE check.
                     return true;                         // return true, class deleted.
                  } else {                                // found and not deleting.	                	 
                	  return cssRule;                      // return the style object.
                  }                                       // End delete Check
               }                                          // End found rule name
            }                                             // end found cssRule
            ii++;                                         // Increment sub-counter
         } while (cssRule);                               // end While loop
      }                                                   // end For loop
   }                                                      // end styleSheet ability check
   return false;                                          // we found NOTHING!
}                                                         // end getCSSRule 

/**
 * It basically accepts a rule name, creates an empty ruleset in the first stylesheet, 
 * and then returns the stylesheet object it created.
 * 
 * @author Adelina
 * 
 * @return
 */
function addCSSRule(ruleName) {                           // Create a new css rule
   if (document.styleSheets) {                            // Can browser do styleSheets?
      if (!getCSSRule(ruleName)) {                        // if rule doesn't exist...
         if (document.styleSheets[0].addRule) {           // Browser is IE?
            document.styleSheets[0].addRule(ruleName, null,0);      // Yes, add IE style
         } else {                                         // Browser is IE?
            document.styleSheets[0].insertRule(ruleName+' { }', 0); // Yes, add Moz style.
         }                                                // End browser check
      }                                                   // End already exist check.
   }                                                      // End browser ability check.
   return getCSSRule(ruleName);                           // return rule we just created.
} 

/**
 * It is applied to those browsers that accepts CSS3,
 * eventually acepts word breaking and word wraping
 * 
 * @author Adelina
 * 
 * @return
 */
function wordWrap() {
	
	// add css rule to the .list_results td so that
	// it can accept wrapping
	var list_results = addCSSRule('.list_results td');						
	list_results.style.wordBreak = 'break-all';
	list_results.style.maxWidth = '0';
	list_results.style.wordWrap = 'break-word';
	
	var action_table = addCSSRule('.actionsTable td');						
	action_table.style.wordBreak = 'none';
	action_table.style.maxWidth = 'none';
	action_table.style.wordWrap = 'none';
}
	

/**
 * If the firefox version is greater or equal to 3.5.x, if the browser is IE, or other browser than Opera
 * then it accepts wordwrapping, and to the style is added those properties that can do so. 
 * 
 * @Adelina
 *
 * @return
 */
function whichBrsVersion() {

	if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){  //test for Firefox/x.x or Firefox x.x (ignoring remaining digits);		
		var ffversion=new Number(RegExp.$1) // capture x.x portion and store as a number	
		if (ffversion>=3.5){			
			wordWrap();
		} 	
	} else if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {  //test for MSIE x.x;		
		wordWrap();
	} else if (/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {  //test for Opera/x.x or Opera x.x (ignoring remaining decimal places);
		 	
	} else {
		wordWrap();
	}
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