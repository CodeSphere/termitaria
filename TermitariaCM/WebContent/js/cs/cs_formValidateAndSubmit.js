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
 * METHODS FOR SUBMIT AND VALIDATE
 */

//========================================================================================================
String.prototype.trim=function(){
return this.replace(/^\s*|\s*$/g,'');
}

//========================================================================================================
String.prototype.ltrim=function(){
return this.replace(/^\s*/g,'');
}

//========================================================================================================
String.prototype.rtrim=function(){
return this.replace(/\s*$/g,'');
}

//========================================================================================================
/**
 * Validates a form
 *	
 * @author matti_joona
 * 
 * Apply these class names to form elements:
 * 	required (not blank)
 * 	validate-number (a valid number)
 * 	validate-digits (digits only, spaces allowed.)
 * 	validate-alpha (letters only)
 *  validate-alpha-with-space (letters with space)
 * 	validate-alphanum (only letters and numbers)
 *  validate-alphanum-with-space ( only letters, digits and spaces)
 *  validate-alphanum-with-special-chars (letters, digits, spaces, and other special characters)
 * 	validate-date (a valid date value)
 * 	validate-email (a valid email address)
 * 	validate-url (a valid URL)
 * 	validate-date-au (a date formatted as; dd/mm/yyyy)
 * 	validate-currency-dollar (a valid dollar value)
 * 	validate-one-required (At least one checkbox/radio element must be selected in a group)
 * 	validate-not-first (Selects only, must choose an option other than the first)
 * 	validate-not-empty (Selects only, must choose an option with a value that is not empty)
 *  validate-length (Multiple selects only, must have at least one option) 
 * 	validate-regex (requires the element to have a 'regex=' attribute applied)
 * 	Also, you can specify this attribute for text, password and textarea elements:
 * 	minlength="x" (where x is the minimum number of characters)
 * 
*/
function validateForm(formId) {
	var errs = new Array();
	var all_valid = true;
	var valid;
	var cname;
	var elm = document.getElementById(formId);
	
	var f_in = elm.getElementsByTagName('input');
	var f_sl = elm.getElementsByTagName('select');
	var f_ta = elm.getElementsByTagName('textarea');
	//check inputs
	try{
		for (i=0;i<f_in.length;i++) {
			if (f_in[i].type.toLowerCase() != 'submit' && f_in[i].type.toLowerCase() != 'button' && f_in[i].type.toLowerCase() != 'hidden') {
				if (isVisible(f_in[i])) {
					cname = ' '+f_in[i].className+' ';
					cname = cname.toLowerCase();
					var inv = f_in[i].value.trim();
					var t = f_in[i].type.toLowerCase();
					var cext = '';
					if (t == 'text' || t == 'password') {
						//text box
						valid = FIC_checkField(cname,f_in[i]);
					} else if(t == 'radio' || t == 'checkbox'){
						// radio or checkbox
						valid = FIC_checkRadCbx(cname,f_in[i],f_in);
						cext = '-cr';
					} else {
						valid = true;
					}
					
					if (valid) {
						removeClassName(f_in[i],'validation-failed'+cext);
						addClassName(f_in[i],'validation-passed'+cext);
						if(f_in[i].id != null){
							removeMessage(f_in[i].id);
						}	
					} else {
						removeClassName(f_in[i],'validation-passed'+cext);
						addClassName(f_in[i],'validation-failed'+cext);

						errs[errs.length] = (f_in[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON');
						displayMessage(f_in[i].id, (f_in[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON'));
						
						all_valid = false;
					}
				}
			}
		} //end for i
		//check textareas
		for (i=0;i<f_ta.length;i++) {
			if (isVisible(f_ta[i])) {
				cname = ' '+f_ta[i].className+' ';
				cname = cname.toLowerCase();
				valid = FIC_checkField(cname,f_ta[i]);
				if (valid) {
					removeClassName(f_ta[i],'validation-failed');
					addClassName(f_ta[i],'validation-passed');
					if(f_ta[i].id != null){
						removeMessage(f_ta[i].id);
					}
				} else {
					removeClassName(f_ta[i],'validation-passed');
					addClassName(f_ta[i],'validation-failed');
					
					errs[errs.length] = (f_ta[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON');
					displayMessage(f_ta[i].id, (f_ta[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON'));
					
					all_valid = false;
				}
			}
		} //end for i
		//check selects
		for (i=0;i<f_sl.length;i++) {
			
			if (isVisible(f_sl[i])) {
				cname = ' '+f_sl[i].className+' ';
				cname = cname.toLowerCase();
				valid = FIC_checkSel(cname,f_sl[i]);
				if (valid) {
					removeClassName(f_sl[i],'validation-failed-sel');
					addClassName(f_sl[i],'validation-passed-sel');
					
					if(f_sl[i].id != null && f_sl[i].id != ""){
						removeMessage(f_sl[i].id);
					}
				} else {
					removeClassName(f_sl[i],'validation-passed-sel');
					addClassName(f_sl[i],'validation-failed-sel');
					
					errs[errs.length] = (f_sl[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON');
					displayMessage(f_sl[i].id, (f_sl[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON'));
						
					all_valid = false;
				}
			}
		} //end for i
	}catch(err)
	  {
	  txt="There was an error on this page.\n\n";
	  txt+= (err.number & 0xFFFF) + " " + err.description; // Prints 5009.
	  txt+= "\n\n";
	  txt+="Click OK to continue.\n\n";
	  alert(txt);
	  }
	

	if (!all_valid) {
		YAHOO.util.Event.stopEvent(elm);
	}
return all_valid;
} // end validateForm

function FIC_checkField(c,e) {
	var valid = true;
	var t = e.value.trim();
	
	//search for required
	if (c.indexOf(' required ') != -1 && t.length == 0) {
		//required found, and not filled in
		valid = false;
	}
	
	//check length
	if (c.indexOf(' required ') != -1){
		//check for minlength.
		var m = e.getAttribute('minlength');
		if (m && Math.abs(m) > 0){
			if (e.value.length < Math.abs(m)){
				valid = false;
			}
		}
	}
	
	//search for validate-
	if (c.indexOf(' validate-number ') != -1 && isNaN(t) && t.match(/[^\d]/)) {
		//number bad
		valid = false;
	} else if (c.indexOf(' validate-digits ') != -1 && t.replace(/ /,'').match(/[^\d]/)) {
		//digit bad
		valid = false;
	} else if (c.indexOf(' validate-alpha ') != -1 && !t.match(/^[a-zA-Z]+$/)) {
		//alpha bad
		valid = false;
	}  else if(c.indexOf(' validate-alpha-with-space ') != -1 && !t.match(/^[a-zA-Z]+$\S/)) {
		// alpha with space bad
		valis = false;
	}  else if (c.indexOf(' validate-alphanum ') != -1 && t.match(/\W/)) {
		//alphanum  bad
		valid = false;
	} else if (c.indexOf(' validate-alphanum-with-space ') != -1 && (t.match(/[^0-9a-zA-Z\s\-\.\_]/))) {
		//alphanum with space bad
		valid = false;															
	} else if (c.indexOf(' validate-date ') != -1) {               
		var date = t.split('-');
		if (date.length == 3){
			if ((date[2].length != 4) || (date[2] < 1970)){
				valid = false;
			} else {
				// months in javascript are in range of 0-11
				var month = date[1] - 1;
				var testDate = new Date(date[2],month,date[0]);
				if (isNaN(testDate)){
					valid = false;
				} else {
				// check if my newly created testDate is the same with the one entered
				if ((testDate.getFullYear() != date[2]) || (testDate.getMonth() != month) || (testDate.getDate() != date[0]))
					valid = false;
				}
			}
		} else {
			valid = false;
		}		
	} else if (c.indexOf(' validate-email ') != -1 && !t.match(/^[a-zA-Z]+([_\.-]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]+([\.-]?[a-zA-Z0-9]+)*(\.[a-zA-Z]{2,4})+$/)) {
		//email bad
		valid = false;
		if (c.indexOf(' required ') == -1 && t.length == 0) {
			valid = true;
		}
	} else if (c.indexOf(' validate-url ') != -1 && !t.match(/^(http|https|ftp):\/\/(([A-Z0-9][A-Z0-9_-]*)(\.[A-Z0-9][A-Z0-9_-]*)+)(:(\d+))?\/?/i)) {
		//url bad
		valid = false;
	} else if (c.indexOf(' validate-date-au ') != -1 && !t.match(/^(\d{2})\/(\d{2})\/(\d{4})$/)) {
		valid = false;
	} else if (c.indexOf(' validate-currency-dollar ') != -1 && !t.match(/^\$?\-?([1-9]{1}[0-9]{0,2}(\,[0-9]{3})*(\.[0-9]{0,2})?|[1-9]{1}\d*(\.[0-9]{0,2})?|0(\.[0-9]{0,2})?|(\.[0-9]{1,2})?)$/)) {
		valid = false;
	} else if (c.indexOf(' validate-regex ') != -1) {
	    var r = RegExp(e.getAttribute('regex'));
	    if (r && ! t.match(r)) {
	        valid = false;
	    }
	} else if(c.indexOf(' validate-time ') != -1 && !t.match(/^(2[0-3])|[01][0-9]:[0-5][0-9]$/)){
		valid = false;
	} else if(c.indexOf(' validate-date-and-time ') != -1){
		var array = t.split(" ");
		if (!(array.length == 2)){
			valid = false;
		} else {
			// check if date is in correct format
			
			var date = array[0].split('-');
			if (date.length == 3){
				if ((date[2].length != 4) || (date[2] < 1970)){
					valid = false;
				} else {
					// months in javascript are in range of 0-11
					var month = date[1] - 1;
					var testDate = new Date(date[2],month,date[0]);
					if (isNaN(testDate)){
						valid = false;
					} else {
					// check if my newly created testDate is the same with the one entered
					if ((testDate.getFullYear() != date[2]) || (testDate.getMonth() != month) || (testDate.getDate() != date[0]))
						valid = false;
					}
				}
			} else {
				valid = false;
			}
			/*
			if (!array[0].match(/^(\d{2})-(\d{2})-(\d{4})$/)){
				valid = false;
			}*/
			// check if time is in correct format
			if (!array[1].match(/^(2[0-3])|[01][0-9]:[0-5][0-9]$/)){
				valid = false;
			}
		}
	}
	return valid;
}

//========================================================================================================
/**
 * FIC_checkRadCbx
 * c = className
 * e = this element, radio or checkbox
 * f = input fields dom element
 * 
 * @author matti_joona
 */
function FIC_checkRadCbx(c,e,f){
	var valid = true;
	
	//search for required
	if (c.indexOf(' validate-one-required ') != -1) {
		//required found
		//check if other checkboxes or radios have been selected.
		valid = false;
		for (var i=0;i<f.length;i++){
			if(f[i].name.toLowerCase() == e.name.toLowerCase() && f[i].checked){
				valid = true;
				break;
			}
		}
	}
	return valid;
}

//========================================================================================================
/**
 * FIC_checkRadCbxOneField
 * c = className
 * e = this element, radio or checkbox
 * 
 * @author matti_joona
 */
function FIC_checkRadCbxOneField(c,e){
	var valid = true;
	//search for required
	if (c.indexOf(' validate-one-required ') != -1) {
		valid = false;
		if(e.checked){
			valid = true;
		}
	}	
	return valid;
}

//========================================================================================================
/**
 * FIC_checkSel
 * c = className
 * e = this select element
 * 
 * @author matti_joona
 */
function FIC_checkSel(c,e){
	var valid = true;
	//search for validate-
	if (c.indexOf(' validate-not-first ') != -1 && e.selectedIndex == 0) {
		//bad
		valid = false;
	} else if (c.indexOf(' validate-not-empty ') != -1 && e.options[e.selectedIndex].value.length == 0) {
		//bad
		valid = false;
	} else if (c.indexOf(' validate-length ') != -1 && e.length == 0) {
		//bad
		valid = false;
	}
	return valid;
}

//========================================================================================================
/**
 * Add ClassName
 * 
 * @author matti_joona
 */
function addClassName(e,t) {
	if (typeof e == "string") {
		e = xGetElementById(e);
	}
	//code to change and replace strings
	var ec = ' ' + e.className + ' ';
	var nc = ec;
	t = t.replace(/^\s*|\s*$/g,'');
	//check if not already there
	if (ec.indexOf(' '+t+' ') == -1) {
		//not found, add it
		nc = ec + t;
	}
	//return the changed text!
	e.className = nc; //trimmed whitespace
	return true;
}

//========================================================================================================
/**
 * Remove ClassName
 * 
 * @author matti_joona
 */
function removeClassName(e,t) {
	if (typeof e == "string") {
		e = xGetElementById(e);
	}
	//code to change and replace strings
	var ec = ' ' + e.className + ' ';
	var nc = ec;
	t = t.replace(/^\s*|\s*$/g,'');
	//check if not already there
	if (ec.indexOf(' '+t+' ') != -1) {
		//found, so lets remove it
		nc = ec.replace(' ' + t + ' ',' ');
	}
	//return the changed text!
	e.className = nc; //trimmed whitespace
	return true;
}

//========================================================================================================
/**
 * isVisible
 * 
 * @author matti_joona
 */
function isVisible(e) {
	//returns true is should be visible to user.
	if (typeof e == "string") {
		e = xGetElementById(e);
	}
	while (e.nodeName.toLowerCase() != 'body' && e.style.display.toLowerCase() != 'none' && e.style.visibility.toLowerCase() != 'hidden') {
		e = e.parentNode;
	}
	if (e.nodeName.toLowerCase() == 'body') {
		return true;
	} else{
		return false;
	}
}

//========================================================================================================
/**
 * Function used for displaying error messages next to field
 * 
 * @author matti_joona
 */
function displayMessage(fieldId, errorMessage){
	var em = document.getElementById(fieldId);
	//build a div to be added to the field
	var newDiv = document.createElement("div");
	
	/* 
	 * @author mitziuro
	 * We can't set newDiv.setAttribute("class", "javaScriptValidate"); 
	 * because in IE isn't working
	 */
	newDiv.style.display = 'inline';
	newDiv.style.color = 'red';
	newDiv.setAttribute("id", fieldId.concat("DIV"));
	var elem = document.getElementById(fieldId.concat("DIV"));
	//verify if the button is pressed twice
	if(elem == null){
	    var newP=document.createElement("span");
	    newP.appendChild(document.createTextNode(errorMessage))
	    newDiv.appendChild(newP);
	    em.parentNode.appendChild(newDiv);		
	} else {
		elem.innerHTML = "<span>".concat(errorMessage).concat("</span>");
	}
}

//========================================================================================================

//========================================================================================================
/**
 * Function used for displaying error messages next to field
 * 
 * @author matti_joona
 */
function removeMessage(fieldId){
	var em = document.getElementById(fieldId);	
	var newDiv = document.getElementById(fieldId.concat("DIV"));
	if(newDiv != null){
		//em.parentNode.removeChild(newDiv);	
		newDiv.innerHTML='';
	}
}	

//========================================================================================================
/**
 * searchUp
 * 
 * @author matti_joona
 */
function searchUp(elm,findElm,debug) {
	//this function searches the dom tree upwards for the findElm node starting from elm.
	//check if elm is reference
	
	if(typeof(elm) == 'string') {
		elm = xGetElementById(elm);
	}
	//search up
	//get the parent findElm
	while (elm && elm.parentNode && elm.nodeName.toLowerCase() != findElm && elm.nodeName.toLowerCase() != 'body') {
		elm = elm.parentNode;
	}
	return elm;
}

//========================================================================================================
/**
 * xGetElementById
 * 
 * @author matti_joona
 */
function xGetElementById(e) {
	if(typeof(e)!='string') return e;
	if(document.getElementById) e=document.getElementById(e);
	else if(document.all) e=document.all[e];
	else e=null;
	return e;
}

//========================================================================================================
/**
 * Object used for submit info
 * 
 * @author matti_joona
 */
function ObjSubmit(url, formId, container, panelTitle, dimensionW, dimensionH) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.panelTitle = panelTitle;
	this.dimensionW = dimensionW;
	this.dimensionH = dimensionH;
}

//========================================================================================================
/**
 * Function used for validating a specified field
 * 
 * @author matti_joona
 */
function validateField(){
	var elm = this;
	var cname = ' ' + elm.className + ' ';
	cname = cname.toLowerCase();
	var type = elm.type.toLowerCase();
	var cext = '';
	var valid = true;
	if (type == 'text' || type == 'password') {
		//text box
		valid = FIC_checkField(cname,elm);
	} else if(type == 'radio' || type == 'checkbox'){
		// radio or checkbox
		valid = FIC_checkRadCbxOneField(cname,elm);
		cext = '-cr';
	} else if(type == 'select'){
		valid = FIC_checkSel(cname,elm);
	} else {
		valid = true;
	}
	
	if (valid) {
		removeClassName(elm,'validation-failed' + cext);
		addClassName(elm,'validation-passed' + cext);
		if(elm.id != null){
			removeMessage(elm.id);
		}
	} else {
		removeClassName(elm,'validation-passed'+cext);
		addClassName(elm,'validation-failed'+cext);
		if (elm.getAttribute('title')){
			displayMessage(elm.id, elm.getAttribute('title'));
		}
	}
}

//========================================================================================================
/**
 * Function for submit form
 * 
 * @author matti_joona
 */
function validateAndSubmitForm(){
	if (validateForm(this.formId)){
		submitForm(this.url, this.formId, this.container, true);
	}
}

//========================================================================================================
/**
 * Function for submit project search form
 * It sets the firstname and lastname hidden fields values to "" before submittting the form 
 * when the user removes the name input text after a previous search
 *
 * When withContext is true, it means the function is not called with parameters
 * but without, and another Object defines it's context (It's something specific to YUI).
 * 
 * @author adelina
 */
function submitProjectForm(url, formId, divId){	
		 
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
	} 
	
	if(!this.isManager) {
		if (YAHOO.util.Dom.get(this.managerInput).value == ""){
			YAHOO.util.Dom.get(this.managerNameHiddenFieldId).value = -1; 		
		}
	}
		
	if (YAHOO.util.Dom.get(this.clientInput).value == ""){
		YAHOO.util.Dom.get(this.clientNameHiddenFieldId).value = 0; 		
	}
	 	
	submitForm(url, formId, divId);
}

//========================================================================================================
/**
 * Function for validating and submitting a form
 * 
 * @author alu
 */
function validateAndSubmitFormWithParams(url, formId, container){
	if (validateForm(formId)){
		submitForm(url, formId, container);
	}
}

/**
* Object used when we want to submit a form with confirmation
* @author Adelina
*/
function ObjSubmitWithConfirmation(url, formId, container, confirmationMessage, panelTitle) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.confirmationMessage = confirmationMessage;
	this.panelTitle = panelTitle;
}

//========================================================================================================
 /**
  * Object used for validate number of characters from a field
  * 
  * @author matti_joona
  */
 function ObjFieldRemaining(fieldId, message, maxChars, div) {
 	this.fieldId = fieldId;
 	this.message = message;
 	this.maxChars = maxChars;
 	this.div = div;
 }

//========================================================================================================
/**
 * Submits a form but only after confirmation.
 * It uses YAHOO.cm.confirmation panel for confirmation dialog !
 * 
 * @author dan.damian
 */
function submitFormWithConfirmation(_url, _formId, _container, _confirmationMessage){
	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage);
	
	function handleNo(){
		YAHOO.cm.confirmation.hide();
		YAHOO.cm.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.cm.smallLoading.show();
		handleNo();
		submitForm(url, formId, container, true);
		YAHOO.cm.smallLoading.hide();
		
	}
	
	YAHOO.cm.confirmation =  
			    new YAHOO.widget.SimpleDialog("confirmation",  
			             { width: "300px", 
			               fixedcenter: true, 
			               visible: false, 
			               draggable: true,
			               modal: true,
			               close: true, 
			               //text: dialogText,
			               icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
			               constraintoviewport: true,
			               buttons: [ 
			                          { text:"Yes", handler: handleYes, isDefault:true },
			                          { text:"No",  handler:handleNo } ] 
			             } ); 
	
	YAHOO.cm.confirmation.setBody(confirmationMessage);
	YAHOO.cm.confirmation.render(document.body);
	YAHOO.cm.confirmation.show();
}

//========================================================================================================
/**
 * Function for submit form
 * 
 * When withContext is true, it means the function is not called with parameters
 * but without, and another Object defines it's context (It's something specific to YUI).
 * 
 * @author matti_joona
 */
function submitForm(url, formId, divId, displayLocalMessages){
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
		if (this.withConfirmation && YAHOO.cm.confirmation){
			//The panel it's hard coded as it cannot be
			//passed as a parameter. 
			YAHOO.cm.confirmation.hide();
		}
	}
	
	var AjaxObject = { 
		handleSuccess:function (o){
			if(o.responseText !== undefined){
				var div = document.getElementById(divId);
				div.innerHTML = o.responseText;						
				YAHOO.cm.smallLoading.hide();
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
			}
		}, 
		handleFailure:function (o){
			YAHOO.cm.smallLoading.hide();
			alert("Error " + o.status + " : " + o.statusText);
	    },
		startRequest:function() {
			//harvest form data ready to send to the server
			var form = document.getElementById(formId);
			YAHOO.util.Connect.setForm(form);
			//make request
			YAHOO.util.Connect.asyncRequest('POST', url, callback); 
	    }
	}; 
	var callback = {
		success:AjaxObject.handleSuccess, 
		failure:AjaxObject.handleFailure 
	};
	// Show Small Loading
	YAHOO.cm.smallLoading.show();
	//start transaction
	AjaxObject.startRequest();
}
 
 function submitAdvancedForm(url, formId, divId, displayLocalMessages){
		if (this.withContext) {
			url = this.url;
			formId = this.formId;
			divId = this.container;
			if (this.withConfirmation && YAHOO.cm.confirmation){
				//The panel it's hard coded as it cannot be
				//passed as a parameter. 
				YAHOO.cm.confirmation.hide();
			}
		}
		
		var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					var div = document.getElementById(divId);
					div.innerHTML = o.responseText;	
					var projectId = document.getElementById('projectId').value					
					alert("projectId = " + projectId);
					submitSaveProjectTeam('ProjectTeam.htm?ACTION=SAVE&projectId='+projectId, 'projectTeamForm', 'MAIN_CONTENT', 'changePersonsForm_select2');
					YAHOO.cm.smallLoading.hide();
					var scriptTags = div.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
				} return projectId;
			}, 
			handleFailure:function (o){
				YAHOO.cm.smallLoading.hide();
				alert("Error " + o.status + " : " + o.statusText);
		    },
			startRequest:function() {
				//harvest form data ready to send to the server
				var form = document.getElementById(formId);
				YAHOO.util.Connect.setForm(form);
				//make request
				YAHOO.util.Connect.asyncRequest('POST', url, callback); 
		    }
		}; 
		var callback = {
			success:AjaxObject.handleSuccess, 
			failure:AjaxObject.handleFailure 
		};
		// Show Small Loading
		YAHOO.cm.smallLoading.show();
		//start transaction
		AjaxObject.startRequest();
	}
 
//========================================================================================================
 /**
  * Function for limiting the characters for an input field.
  * 
  * @author matti_joona
  */
 function limitField(limitField, message, countDiv, limitNumber) {
 	var remains = limitNumber;
 	if (limitField.value.length > limitNumber) {
 		limitField.value = limitField.value.substring(0, limitNumber);
 	} else {
 		remains = limitNumber - limitField.value.length;
 		countDiv.innerHTML = message.concat(remains).concat("/").concat(limitNumber);
 	}
 }

 //========================================================================================================
 /**
  * Function for showing how much characters remains until the end of the field.
  * 
  * @author matti_joona
  * @author mitziuro
  */
 function showInfoOnClickInField(_fieldId, _message, _maxChars, _div){
  	var fieldId = (this.fieldId != null ? this.fieldId : _fieldId),
 		message = (this.message != null ? this.message : _message),
 		maxChars = (this.maxChars != null ? this.maxChars : _maxChars),
 		div = (this.div != null ? this.div : _div);
 	
 	//if we have a specified div use it, else use a default one
 	if(div){
 		div = document.getElementById(div);			
 	} else {
 		div = document.getElementById('showRemainingCharacters');		
 	}
 			
 	showMessageDiv(div.parentNode.className);
 			
 	var theField = document.getElementById(fieldId);
 	limitField(theField, message, div, maxChars);	
 }

 //========================================================================================================
 /**
  * Function for hidding info about how much characters remains until the end of 
  * the field.
  * 
  * @author matti_joona
  */
 function hideInfoOnBlurInField(_fieldId, _message, _maxChars, _div) {
 	
 	var fieldId = (this.fieldId != null ? this.fieldId : _fieldId),
 		message = (this.message != null ? this.message : _message),
 		maxChars = (this.maxChars != null ? this.maxChars : _maxChars),
 		div = (this.div != null ? this.div : _div);
 	
 	//if we have a specified div use it, else use a default one
 	if(div){
 		div = document.getElementById(div);
 	} else {
 		div = document.getElementById('showRemainingCharacters');
 	}
 				
 	hideMessageDiv(div.parentNode.className);
 }

 function toggleDisabled(el) {
 	try {
 		el.disabled = el.disabled ? false : true;			
 		
 	} catch(E){
 	
 	}
 	if (el.childNodes && el.childNodes.length > 0) {
 		for (var x = 0; x < el.childNodes.length; x++) {
 			toggleDisabled(el.childNodes[x]);
 		}
 	}
 }


 //========================================================================================================
 /**
  * Function for showing how much characters remains until the end of the field.
  * 
  * @author matti_joona
  */
 function showInfoOnKeyUpInField(_fieldId, _message, _maxChars, _div){
 	var fieldId = (this.fieldId != null ? this.fieldId : _fieldId),
 	message = (this.message != null ? this.message : _message),
 	maxChars = (this.maxChars != null ? this.maxChars : _maxChars),
 	div = (this.div != null ? this.div : _div);
 	
 	//if we have a specified div use it, else use a default one
 	if(div){
 		div = document.getElementById(div);
 	} else {
 		div = document.getElementById('showRemainingCharacters');
 	}	
 		
 	var theField = document.getElementById(fieldId);
 	limitField(theField, message, div, maxChars);

 }

//========================================================================================================

 /**
  * Submits a form with delete action but only after confirmation and verifying that at least one item is selected
  * It uses YAHOO.cm.confirmation panel for confirmation dialog !
  * 
  * @author Adelina
  */
 function submitDeleteWithConfirmation(_url, _formId, _container, _confirmationMessage, _selectedForDeletionElementsName, _noSelectedItemsMessage, _panelTitle){
  	var url = (this.url != null ? this.url : _url),
 		formId = (this.formId != null ? this.formId : _formId),
 		container = (this.container != null ? this.container : _container),
 		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
 		selectedForDeletionElementsName = (this.selectedForDeletionElementsName != null ? this.selectedForDeletionElementsName : _selectedForDeletionElementsName),
 		noSelectedItemsMessage = (this.noSelectedItemsMessage != null ? this.noSelectedItemsMessage : _noSelectedItemsMessage),
 		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
 	
 	function handleNo(){
 		YAHOO.cm.confirmation.hide();
 		YAHOO.cm.confirmation.destroy();
 	}
 	
 	function handleYes() {
 		YAHOO.cm.smallLoading.show();
 		handleNo();
 		submitForm(url, formId, container);
 	}	
 	
 	function handleOk() {
 		YAHOO.cm.selectItemsAlert.hide();
 		YAHOO.cm.selectItemsAlert.destroy();
 	}
 		             
 	var selectedElements = document.getElementsByName(selectedForDeletionElementsName);
 	var anyElementChecked = false;
 	//check if at least an element is selected for deletion
 	for (i=0; i<selectedElements.length; i++) {
 		if (selectedElements[i].checked) {
 			anyElementChecked = true;
 		} 
 	}
 	
 	//if at least one element is selected, I have to display the confirmation message, 
 	//otherwise a message requesting to select at least one element is displayed
 	if (anyElementChecked) {
 		YAHOO.cm.confirmation =  
 		    new YAHOO.widget.SimpleDialog("confirmation",  
 		             { width: "320px", 
 		               fixedcenter: true, 
 		               visible: false, 
 		               draggable: true,
 		               modal: true,
 		               close: false,
 		               //text: dialogText,
 		               icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
 		               constraintoviewport: true,
 		               buttons: [ 
 		                          { text:"YES".translate('COMMON'), handler: handleYes, isDefault:true },
 		                          { text:"NO".translate('COMMON'),  handler:handleNo } ] 
 		             } ); 
 	
 		YAHOO.cm.confirmation.setHeader('<span>' + panelTitle + '</span>');
 		YAHOO.cm.confirmation.setBody(confirmationMessage);		
 		YAHOO.cm.confirmation.render(document.body);
 		YAHOO.cm.confirmation.show();
 		
 	} else {
 		YAHOO.cm.selectItemsAlert = 
 			new YAHOO.widget.SimpleDialog("selectItemsAlert",
 					{ width: "300px", 
 		               fixedcenter: true, 
 		               visible: false, 
 		               draggable: true,
 		               modal: true,
 		               close: false,
 		               //text: dialogText,
 		               icon: YAHOO.widget.SimpleDialog.ICON_WARN, 
 		               constraintoviewport: true,
 		               buttons: [ { text:"OK", handler: handleOk} ]
 		             });
 		YAHOO.cm.selectItemsAlert.setHeader('<span>' + panelTitle + '</span>');
 		YAHOO.cm.selectItemsAlert.setBody(noSelectedItemsMessage);
 		YAHOO.cm.selectItemsAlert.render(document.body);
 		YAHOO.cm.selectItemsAlert.show();
 	}
 	
 }

//========================================================================================================
 /**
  * Function for submit form
  * 
  * When withContext is true, it means the function is not called with parameters
  * but without, and another Object defines it's context (It's something specific to YUI).
  * 
  * @author matti_joona
  */
 function submitFormWithData(url, formId, divId, data){
 	if (this.withContext) {
 		url = this.url;
 		formId = this.formId;
 		divId = this.container;
 		if (this.withConfirmation && YAHOO.cm.confirmation){
 			//The panel it's hard coded as it cannot be
 			//passed as a parameter. 
 			YAHOO.cm.confirmation.hide();
 		}
 	}
 	
 	var AjaxObject = { 
 		handleSuccess:function (o){
 			if(o.responseText !== undefined){
 				var div = document.getElementById(divId);
 				div.innerHTML = o.responseText;						
 				YAHOO.cm.smallLoading.hide();
 				var scriptTags = div.getElementsByTagName('script');
 				for(var i=0;i<scriptTags.length;i++){
 					eval(scriptTags[i].innerHTML);
 				}
 			}
 		}, 
 		handleFailure:function (o){
 			YAHOO.cm.smallLoading.hide();
 			alert("Error " + o.status + " : " + o.statusText);
 	    },
 		startRequest:function(data) {
 			//harvest form data ready to send to the server
 			var form = document.getElementById(formId);
 			YAHOO.util.Connect.setForm(form);
 			//make request
 			YAHOO.util.Connect.asyncRequest('POST', url, callback ,data); 
 	    }
 	}; 
 	var callback = {
 		success:AjaxObject.handleSuccess, 
 		failure:AjaxObject.handleFailure 
 	};
 	// Show Small Loading
 	YAHOO.cm.smallLoading.show();
 	//start transaction
 	AjaxObject.startRequest(data);
 }


//========================================================================================================
 /**
  * Function for import files and display messages
  * 
  * @author Adelina
  */
  
function submitFormWithImport(url, formId, divId) {
	  
	 url = this.url,
	 formId = this.formId,
	 divId = this.container;
	 
	 submitForm(url, formId, divId, true);
	 
	 
	 /*
	  * AjaxObject is a hypothetical object that encapsulates the transaction
	  *     request and callback logic.
	  *
	  * handleSuccess( ) provides success case logic
	  * handleFailure( ) provides failure case logic
	  * processResult( ) displays the results of the response from both the
	  * success and failure handlers
	  * call( ) calling this member starts the transaction request.
	  */

	 var AjaxObject = {

	 	handleSuccess:function(o){
	 		// This member handles the success response
	 		// and passes the response object o to AjaxObject's
	 		// processResult member.
	 		this.processResult(o);
	 	},

	 	handleFailure:function(o){
	 		alert("Error " + o.status + " : " + o.statusText);
	 	},

	 	processResult:function(o){
	 			 			 			 			 			 			 	
	 		var length = 0;	 			 	
	 		
	 		// get the JSON object from request	 		
	 		try{
	 			var values = YAHOO.lang.JSON.parse(o.responseText);	 			
	 		} catch (e) {	 			
	 			return;
	 		}
	 		
	 		for(length = 0; values[length] != null; length++);	 		 			
	 		
	 		var message = ""; 
	 		var m = "";
 			for(var i = 0; i < length-1; i++) { 				
 				m = values[i].message; 		
 				message = message.concat(m).concat("<br/>");			 				
 			}
 			
 			var v = "0"
 			if(length - 1 > 0) {
 				v = values[length-1].value;
 			} 			
 			 	
 			div = document.getElementById("messagesContainer");
 			div.style.visibility = "visible";
 			 			 								
			YAHOO.cm.messagesContainer.setBody('<div class="infoMessage">'.concat(message).concat("</div>"));
										
 			if(parseInt(v) != -1) {	 		  				
 				// Initiate the HTTP POST request.				
 				YAHOO.util.Connect.asyncRequest('POST', 'CollectionZoneMessage.htm', callback); 				
 			}
 			
	 	},

	 	startRequest:function() {	
	 			 			 			 			 			
	 		// entryPoint is the base URL
		    var entryPoint = 'CollectionZoneMessage.htm';
			
			// Initiate the HTTP POST request.
		    YAHOO.util.Connect.asyncRequest('POST', entryPoint, callback);		   
	 	}
	 		 
	 };

	 /*
	  * Define the callback object for success and failure
	  * handlers as well as object scope.
	  */
	 var callback =
	 {
	 	success:AjaxObject.handleSuccess,
	 	failure:AjaxObject.handleFailure,
	 	scope: AjaxObject
	 };
		 
	 // Start the transaction.
	 AjaxObject.startRequest();	 	
}
	

//========================================================================================================
 /**
  * Function for submit form with file upload
  * 
  * @author mitziuro
  */
 function submitFormWithFile(url, formId, divId) {
	 
	 url = this.url,
	 formId = this.formId,
	 divId = this.container;	
	  var AjaxObject = {
			
		//handle upload case. This function will be invoked after file upload is finished.
		handleUpload: function(o) {
			
			if(o.responseText != undefined){
				var div = document.getElementById(divId);
				div.innerHTML = o.responseText;
				YAHOO.cm.smallLoading.hide();
				
				//evaluate the script
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
	 		}
		},
			
		startRequest:function() {
			//harvest form data ready to send to the server
			YAHOO.cm.smallLoading.show();
			var form = document.getElementById(formId);
			YAHOO.util.Connect.setForm(form, true);
			//make request
			YAHOO.util.Connect.asyncRequest('POST', url, callback);
		 }

		
	 };
		
	 var callback = {
	 	 	upload:AjaxObject.handleUpload
	 };
	 	
	 //start transaction
	 AjaxObject.startRequest();
	

 }

 //========================================================================================================
//========================================================================================================

 /**
  * Setting parameters for a YUI Panel
  * @author mitziuro
  */
 function ObjSubmitPanel(_url, _panelTitle, _xPosition, _yPosition){
	 this.url = _url;
	 this.panelTitle = _panelTitle;
	 this.xPosition = _xPosition;
	 this.yPosition = _yPosition;
 }


//========================================================================================================
 /**
  * Test if a tab has all the requirements
  * @author mitziuro
  */
 function validateTab(_formId) {
	formId = (this.formId != null ? this.formId : _formId);
	if(validateForm(formId)) {
		return true;
	} else {
		return false;
	}
}
 
//========================================================================================================
	
 /**
  * submit Object for PrivilegeConnections Click
  * @author mitziuro
  */
  function ObjSubmitPrivilege(entitySelect, privilegeSelect, actionsHandler, tableId, globalCheckBoxId) {
	this.entitySelect = entitySelect;
	this.privilegeSelect = privilegeSelect;
	this.actionsHandler = actionsHandler;
	this.tableId = tableId;
	this.globalCheckBoxId = globalCheckBoxId;
	
}
	 
//========================================================================================================
	
 /**
  * Object used for submit documentForm
  * 
  * @author mitziuro
  */
 function ObjSubmitDocument(url, formId, container, actionHandler) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.actionHandler = actionHandler;
	}
//========================================================================================================
 /**
  * Function for submit form
  * 
  * @author mitziuro
  */
  function validateAndSubmitDocumentForm(){
	if (validateForm(this.formId)){
		this.actionHandler.display();
		submitForm(this.url, this.formId, this.container, true);
	}
}
  
//========================================================================================================
  /**
   * Object used to pass parameters to submitSaveClient function
   *
   * @author Coni	 
   */
   
   function ObjSubmitSaveClient(url, formId, container) {
   	this.url = url;
   	this.formId = formId;
   	this.container = container;
}
   
//========================================================================================================
   /**
    * Submits the form for saving a Client
    * @author Coni
    */
   function submitSaveClient(_url, _formId, _container) {
		var url = (this.url != null ? this.url : _url),
	    formId = (this.formId != null ? this.formId : _formId),
	    container = (this.container != null ? this.container : _container);
		
		clientType = document.getElementById("clientForm_type").options[document.getElementById("clientForm_type").selectedIndex].value;
		var validEmail = validateClientEmailInput(); 
		var validAddress = validateClientAddressInput();
		var validPhone = validateClientPhoneInput();
		
		//if the client it's a firm
		if (clientType == 1) {
			var validC_name = validateClientC_nameInput();
			var validC_cui = validateClientC_cuiInput();
			var validC_iban = validateClientC_ibanInput();
			var validC_location = validateClientC_locationInput();
			if (validC_name && validC_cui && validC_iban && validC_location && validPhone && validEmail && validAddress) {
				submitForm(url, formId, container);
			}
		} else if (clientType == 2) {
			var validP_firstName = validateClientP_firstNameInput();
			var validP_lastName = validateClientP_lastNameInput();
			if (validP_firstName && validP_lastName && validEmail && validAddress && validPhone) {
				submitForm(url, formId, container);
			}
		}

   }

  /**
   * Object used to pass parameters to  function
   *
   * @author Adelina	 
   */
   
   function ObjSubmitSaveProject(url, formId, container, personsSelectId) {
    	this.url = url;
    	this.formId = formId;
    	this.container = container;
    	this.personsSelectId = personsSelectId;
    } 
   
   /**
    * Object used to pass parameters to  function
    *
    * @author Adelina	 
    */
    
    function ObjSubmitSaveTeamMember(url, formId, container) {
 	   this.url = url;
    	   this.formId = formId;
    	   this.container = container;   	 
    } 
   
   
   
 //========================================================================================================
   /**
    * Submits the form for saving a Project
    * @author Adelina
    */
    function submitSaveProject(_url, _formId, _container, _personsSelectId) {
    	
       	var url = (this.url != null ? this.url : _url),
        formId = (this.formId != null ? this.formId : _formId),       
        container = (this.container != null ? this.container : _container),
        personsSelectId = (this.personsSelectId != null ? this.personsSelectId : _personsSelectId);     	
    	    	  
		if ((validateProjectNameInput() || validateProjectManagerSelection() || validateClientSelection())) {  
			if ((validateProjectNameInput() && validateProjectManagerSelection() && validateClientSelection())) {				
				var persons = document.getElementById(personsSelectId);
				if(persons) {
	     			for(var i = 0; i < persons.options.length; i++) {	     			
	     				persons.options[i].selected = 'selected';
	     			}
	     		}				
				submitFormWithData(url, formId, container, 'members='+JSON_STR+'&nrOfMembers='+TEAM_MEMBER);									
			}   		  
		}       	
       }
 
 //========================================================================================================
   /**
    * Submits the form for saving a TeamMember
    * @author Adelina
    */
   function submitSaveTeamMember(_url, _formId, _container) {
   //alert("submitSaveTeamMember"); 
   var elements = new Array();
   var elementsValues = new Array();
    	
   	var url = (this.url != null ? this.url : _url),
       formId = (this.formId != null ? this.formId : _formId),
       container = (this.container != null ? this.container : _container);      
   	      		 
   		if(validateForm(formId)) {
			 submitForm(url, formId, container);
			 YAHOO.cm.showPersonPanel.hide();   		   			 
		} 	     		  
   }
         
 /**
  * Submits the data with POST
  * 
  * @author Adelina
  * 
  * @param url
  * @param container
  * @param data
  * @return
  */
   function submitFormExternalTeamMember(url, container, data){	
		var loadObject = { 
			handleSucess:function (o){	
														
				if(o.responseText !== undefined){
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
			startRequest:function(data) {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', url, callbackForLoad, data);
			}
		}; 
		var callbackForLoad = {
			success:loadObject.handleSucess, 
			failure:loadObject.handleFailure
		};
		// Show Small Loading
		YAHOO.cm.smallLoading.show();
		loadObject.startRequest(data);
	}
    
   /**
    * Specific function to add or update external team member when the project is not yet saved
    * @author Adelina
    * @author alexandru.dobre
    */
  function submitAddTeamMember(formId, container, action, type, projectId, memberId) {  		  
   	if(validateForm(formId)) {   
   		var actionType= null;   		
	   		if(action != "ADD") {
	   			//alert("UPDATE");
	   			if(type == true) {	   					   				  			
	   				var endText = '"' + ',"' + 'memberId' + '"' + ' : ' + '"' + memberId + '"';
	   				var end = JSON_STR.indexOf(endText);
	   				var string = JSON_STR.substring(0, end);	   			
	   				var start = string.lastIndexOf('"');	   				
	   				var id = JSON_STR.substring(start + 1, end);	   				
	   				ID_JSON_EXTERNAL_MEMEBER = id;
	   			}	   				
	   			//alert("ID_JSON_EXTERNAL_MEMEBER = " + ID_JSON_EXTERNAL_MEMEBER);
	   			JSON_STR = updateJSONStringForTeamMember(formId, ID_JSON_EXTERNAL_MEMEBER);	   			
	   			actionType="UPDATE";
	   		} else {   		
	   			//alert("ADD");
	   			JSON_STR = createJSONStringForTeamMember(formId);
	   			actionType="ADD";	   			
	   		}
	   		
	   		var firstName= document.getElementById(formId).elements[1].value;
	   		var lastName= document.getElementById(formId).elements[2].value;
	   		//alert("actionType: "+ actionType + " firstName: "+ firstName+" lastName: "+lastName ); 
	   		//alert("JSON_STR = " + JSON_STR);
	   		submitFormExternalTeamMember('TeamMemberListing.htm', 'TEAM_MEMBERS', 'ACTION=GET_FROM_ADD&ACTION_TYPE='+actionType+'&firstName='+firstName+'&lastName='+lastName+'&members='+JSON_STR+'&projectId='+projectId+'&nrOfMembers='+TEAM_MEMBER);	   		
	   		YAHOO.cm.showPersonPanel.hide();   	
   		}    		   		   		   
    }
    
    /**
     * Add a team member element to JSON, when update a project, and we want to update the team member in question
     * 
     * @author Adelina
     * @return
     */
    function addTeamMember() {
    	//alert("addTeamMember");
    	JSON_STR = createJSONStringForTeamMember("personForm");
    	//alert("JSON_Str = " + JSON_STR);
    }
    
    /**
     * Update a team member element from JSON, when update a project, and we want to delete the team member in question
     * The new updated (=deleted) team member has an empty firstName and lastName
     * 
     * @author Adelina
     * 
     * @param memberId
     * @param id
     * @return
     */
    function updateTeamMemberForDelete(memberId, id) {    		
		 // the new json element
		 var updateJsonStr= '{ ';		 
		 updateJsonStr += '"' + 'id' + '"' + ' : ' + '"' + id + '",';	
		 
		 for(var i = 0; i < 8; i++) {  
			switch(i)
			{
				case 0:		
					updateJsonStr += '"' + 'memberId' + '"' + ' : ' + '"' + memberId + '"';
					break;
				case 1:	
					updateJsonStr += '"' + 'firstName' + '"' + ' : '  + '"' + '"';
					break;
				case 2:		
					updateJsonStr += '"' + 'lastName' + '"' + ' : '  + '"' + '"';
					break;
				case 3:			  
					updateJsonStr += '"' + 'email' + '"' + ' : '  + '"' + '"';
					break;
				case 4:			  
					updateJsonStr +='"' + 'address' + '"' + ' : '  + '"' + '"';
					break;
				case 5:			  
					updateJsonStr += '"' + 'phone' + '"' + ' : '  + '"' + '"';
					break;
				case 6:			  
					updateJsonStr += '"' + 'observation' + '"' + ' : '  + '"' + '"';
					break;
				case 7:			  
					updateJsonStr += '"' + 'description' + '"' + ' : '  + '"' + '"';
					break;				
			
			}
							
			if(i < 7) {
				updateJsonStr += ',';
			}					
		 }
		 		 
		 updateJsonStr += ' }';
 	   	 	   
 	   	 var idString = '{ "' + 'id' + '"' + ' : ' + '"' + id + '"';
 	   	 var indexNextId = -1;  	   	
 	   	 var indexId = JSON_STR.search(idString);	  	   
	 
 	   	 var element = "";
	 
 	   	 var text = JSON_STR.substring(indexId, JSON_STR.length);  	   	
 	   	 indexNextId = text.indexOf('}');
 	   	 element = text.substring(0, indexNextId+1);
 	  
 	   	 //alert("a = " + element);
	 	 	  	   	 	 	
 	   	 var string = JSON_STR;  	   	
 	   	 // the string that needs to be replaced, with the new updated string element
 	   	 var substr = string.substring(indexId, indexId + element.length);
 	   	 // alert("to be deleted = " + substr);
 	   	 // the new updates JSON 
 	   	 JSON_STR = JSON_STR.replace(substr,updateJsonStr);  	  
		 	 		
 	   	 //alert('updateJSONStringForTeamMember END - JSON_STR = ' + JSON_STR);		
 		 return JSON_STR;			
    }
    
    /**
     *  Add a team member element to JSON, when delete a project, and we want to delete the team member in question
     *  
     *  @author Adelina
     * 
     * @param memberId
     * @return
     */
    function addTeamMemberForDelete(memberId) {    	    	    	   
    	// if we have empty JSON
		if(JSON_STR == "{}"){			
			JSON_STR = "";
			DELETED_MEMBER_ID = 0;
		}
	   	  		
		// if we have at least one element
		if(TEAM_MEMBER > 0) {			
			JSON_STR = JSON_STR.substring(0, JSON_STR.length - 2);
			JSON_STR += ","
		} else {  // else, the JSON is empty, we have to create the string	
			JSON_STR+= '{ ' + '"' + "members" + '"' + ': [';
		}
		
		// increase the number of elements
		TEAM_MEMBER++; 
					
		// create the JSON
		JSON_STR+= '{ ';
		
		// add the id property
		// if is no item that was deleted, then, the next id, is the number of elements
		if(DELETED_MEMBER_ID == 0) {
			JSON_STR += '"' + 'id' + '"' + ' : ' + '"' + TEAM_MEMBER + '",';
		} else { // else, the next element, is added with the previous deleted id			
			var idString = '{ "' + 'id' + '"' + ' : ' + '"' + DELETED_MEMBER_ID + '"';
			var indexId = JSON_STR.search(idString);
			var tempId = TEAM_MEMBER-1;
			do{				
				//alert("tempId = " + tempId);
				tempId++;
				//alert("indexId = " + indexId);
				if(indexId != -1) {
					//alert("different");
					var exists = true;
					idString = '{ "' + 'id' + '"' + ' : ' + '"' + tempId + '"';
					indexId = JSON_STR.search(idString);					
				}
			} while(indexId != -1);
			
			if(exists) {
				JSON_STR += '"' + 'id' + '"' + ' : ' + '"' + tempId + '",';
			} else {
				JSON_STR += '"' + 'id' + '"' + ' : ' + '"' + DELETED_MEMBER_ID + '",';
			}			
		}
		
		// add the other properties like : form_element_name : form_element_value
		for(var i = 0; i < 8; i++) {  
			switch(i)
			{
				case 0:		
					JSON_STR += '"' + 'memberId' + '"' + ' : ' + '"' + memberId + '"';
					break;
				case 1:	
					JSON_STR += '"' + 'firstName' + '"' + ' : ' + '"' + '"';
					break;
				case 2:		
					JSON_STR += '"' + 'lastName' + '"' + ' : '  + '"' + '"';
					break;
				case 3:			  
					JSON_STR += '"' + 'email' + '"' + ' : '  + '"' + '"';
					break;
				case 4:			  
					JSON_STR += '"' + 'address' + '"' + ' : '  + '"' + '"';
					break;
				case 5:			  
					JSON_STR += '"' + 'phone' + '"' + ' : '  + '"' + '"';
					break;
				case 6:			  
					JSON_STR += '"' + 'observation' + '"' + ' : ' + '"' + '"';
					break;
				case 7:			  
					JSON_STR += '"' + 'description' + '"' + ' : ' + '"' + '"';
					break;				
			
			}
							
			if(i < 7) {
				JSON_STR += ',';
			}					
		}
		// end of the creation of JSON_STR
		JSON_STR += ' }]}';
				
		return JSON_STR;			    	    
    }
    
   /**
    * Create a JSON String for team member form, from the element's from
    * 
    * @author Adelina
    * 
    * @param formId
    * @return
    */
   function createJSONStringForTeamMember(formId) { 
    	//alert('createJSONStringForTeamMember START - JSON = ' + JSON_STR);    	
    	var elements = new Array();
    	var elementsValues = new Array();
	   
    	// if we have empty JSON
		if(JSON_STR == "{}"){			
			JSON_STR = "";
			DELETED_MEMBER_ID = 0;
		}
	   
	    var oForm = document.forms[formId]; 	 
	    // get the element's ids from the form
		elements = showElements(oForm);		
		// get the elements's names from the form   			
		elementsName = showElementsName(oForm);
		
		// if we have at least one element
		if(TEAM_MEMBER > 0) {			
			JSON_STR = JSON_STR.substring(0, JSON_STR.length - 2);
			JSON_STR += ","
		} else {  // else, the JSON is empty, we have to create the string	
			JSON_STR+= '{ ' + '"' + "members" + '"' + ': [';
		}
		
		// increase the number of elements
		TEAM_MEMBER++; 
		
		//alert("DELETED_MEMBER_ID = " + DELETED_MEMBER_ID);

		// create the JSON
		JSON_STR+= '{ ';
		
		// add the id property
		// if is no item that was deleted, then, the next id, is the number of elements
		if(DELETED_MEMBER_ID == 0) {
			JSON_STR += '"' + 'id' + '"' + ' : ' + '"' + TEAM_MEMBER + '",';
		} else { // else, the next element, is added with the previous deleted id			
			var idString = '{ "' + 'id' + '"' + ' : ' + '"' + DELETED_MEMBER_ID + '"';
			var indexId = JSON_STR.search(idString);
			var tempId = TEAM_MEMBER-1;
			do{				
				//alert("tempId = " + tempId);
				tempId++;
				//alert("indexId = " + indexId);
				if(indexId != -1) {
					//alert("different");
					var exists = true;
					idString = '{ "' + 'id' + '"' + ' : ' + '"' + tempId + '"';
					indexId = JSON_STR.search(idString);					
				}
			} while(indexId != -1);
			
			if(exists) {
				JSON_STR += '"' + 'id' + '"' + ' : ' + '"' + tempId + '",';
			} else {
				JSON_STR += '"' + 'id' + '"' + ' : ' + '"' + DELETED_MEMBER_ID + '",';
			}			
		}
	
		// add the other properties like : form_element_name : form_element_value
		for(var i = 0, j = 0; i < elements.length - 1, j < elementsName.length - 1; i++, j++) {   			
			var el = document.getElementById(elements[i]);   			   				
			elementsValues[i] = el.value;   			
			//alert("name = " +  elementsName[j] + ", value = " + elementsValues[i]);  								
			JSON_STR += '"' + elementsName[j] + '"' + ' : ' + '"' + elementsValues[i] + '"';                                       
			if(i < elements.length - 2) {
				JSON_STR += ',';
			}					
		}
		// end of the creation of JSON_STR
		JSON_STR += ' }]}';
		
		//alert('createJSONStringForTeamMember END - JSON = ' + JSON_STR);
		return JSON_STR;			
   }
   
    /**
     * Deletes a JSON String element, identified by it's id 
     * 
     * @author Adelina
     * 
     * @param JSON_STR
     * @param id
     * @return
     */
   function deleteJSONStringElementForTeamMemmber(id) {
	 //alert('deleteJSONStringElementForTeamMemmber START - id = ' + id + " JSON = " + JSON_STR);
     
	 var idString = '{ "' + 'id' + '"' + ' : ' + '"' + id + '"';
	 var indexNextId = -1;	 	 
	 // search for the idString, and gets the index 
	 var indexId = JSON_STR.search(idString);		
	 //alert("indexId = " + indexId);
	 
	 // decrease the number of team members	 
	 TEAM_MEMBER--;	 
	 
	 var element = "";
	 
	 // create a substring with all the data from the specific indexId
	 var text = JSON_STR.substring(indexId, JSON_STR.length);
	 //	 alert("text" + text);
	 
	 // gets the next index, for "},"
	 indexNextId = text.indexOf('},');
	 // gets the substring, that represents the JSON element that needs to be deleted
	 element = text.substring(0, indexNextId + 2);
	 
	 // if id doesn't exists "},", it means the element that needs to be deletes is the last
	 if(indexNextId == -1) {		
		 indexNextId =  text.indexOf('}');
		 // gets the element with the new index
		 element = text.substring(0, indexNextId + 2);
		 // decrease the index
		 indexId--;		 
		 		 
		 var idFirst = '[' + '{ "' + 'id' + '"' + ' : ' + '"' + id + '"';
		 //alert("idFirst = " + idFirst);
		 var index = JSON_STR.indexOf(idFirst);		
		 
		 // if the element is the first element, and there is either no element after that,
		 // if we delete this element, it means the JSON is empty
		 if((indexNextId != -1) && (index != -1)) {
			 //alert('empty');
			 JSON_STR = JSON_STR.replace(JSON_STR,"{}");		
			 return JSON_STR;
		 }
	 }	
	 
	 //alert("a = " + element);	 
	 	 	
	 //alert("indexNextId = " + indexNextId);
	 	 
	 var string = JSON_STR;
	 // get the string that needs to be deleted
	 var substr = string.substring(indexId, indexId + element.length);
	 //alert("to be deleted = " + substr);
	 
	 JSON_STR = JSON_STR.replace(substr,"");	
	 			 
	 //alert('deleteJSONStringElementForTeamMemmber END - JSON_STR = ' + JSON_STR);
	 return JSON_STR;
   }
    
     /**
      * Create a JSON String for team member form, from the element's from
      * 
      * @author Adelina
      * 
      * @param formId
      * @return
      */
     function updateJSONStringForTeamMember(formId, id) {
    	 //alert('updateJSONStringForTeamMember END - id = ' + id);
  	  
  	   	 var elements = new Array();
  	   	 var elementsValues = new Array();
  	     	   	 	
  	   	 var oForm = document.forms[formId]; 	 
	     // get the element's ids from the form
		 elements = showElements(oForm);
		
		 // get the elements's names from the form   			
		 elementsName = showElementsName(oForm);
		
		 // the new json element
		 var updateJsonStr= '{ ';
		 
		 updateJsonStr += '"' + 'id' + '"' + ' : ' + '"' + id + '",';	
		 
		 for(var i = 0, j = 0; i < elements.length - 1, j < elementsName.length - 1; i++, j++) {   			
			var el = document.getElementById(elements[i]);   			   				
			elementsValues[i] = el.value;   								   		
			updateJsonStr += '"' + elementsName[j] + '"' + ' : ' + '"' + elementsValues[i] + '"';                                       
			if(i < elements.length - 2) {
				updateJsonStr += ',';
			}					
		 }
		
		 updateJsonStr += ' }';
  	   
		 //alert("updateStr = " + updateJsonStr);
  	   
  	   	 var idString = '{ "' + 'id' + '"' + ' : ' + '"' + id + '"';
  	   	 var indexNextId = -1;  	   	
  	   	 var indexId = JSON_STR.search(idString);	  	   
	 
  	   	 var element = "";
	 
  	   	 var text = JSON_STR.substring(indexId, JSON_STR.length);  	   	
  	   	 indexNextId = text.indexOf('}');
  	   	 element = text.substring(0, indexNextId+1);
  	  
  	   	 //alert("a = " + element);
	 	 	  	   	 	 	
  	   	 var string = JSON_STR;  	   	
  	   	 // the string that needs to be replaced, with the new updated string element
  	   	 var substr = string.substring(indexId, indexId + element.length);
  	   	 // alert("to be deleted = " + substr);
  	   	 // the new updates JSON 
  	   	 JSON_STR = JSON_STR.replace(substr,updateJsonStr);  	  
		 	 		
  	   	 //alert('updateJSONStringForTeamMember END - JSON_STR = ' + JSON_STR);		
  		 return JSON_STR;			
     }     
  
 //========================================================================================================
   /**
    * Moves options form Source Select to Destination Select. The two selects has to be identical:
    * 		- same option id and label
    * 
    * @author dd
    * 
    * @param sourceSelectId Id of the source select
    * @param destSelectBId  Id of the destination select
    * @param remove If false  the function will handle the Add case, else the Remove
    * @return
    */

   function moveSelectOptionsSourceDest(sourceSelectId, destSelectBId, managerId, remove){    
   	 var i;	
   	// Source select
   	var sourceSelect = document.getElementById(sourceSelectId);
   	// Destination select
   	var destSelect = document.getElementById(destSelectBId);
   	// HashMap with all options from destination select
   	var destSelectHM = new Array();
   	for(i = 0; i < destSelect.options.length; i++) {
   		destSelectHM[destSelect.options[i].value] = 'x'; // something != null
   	} 
   	// A temporary Array for storing the user selection from sourceSelect
   	var selected = new Array();
   	for (i = 0; i < sourceSelect.options.length; i++) 
   		if (sourceSelect.options[i].selected) { 
   			selected.push(sourceSelect.options[i]);
   		}
   	// Removing from source no matter if the options will be added or not in the destination select !
   	if (remove) {
   		for(i = 0; i < selected.length; i++) {	 
   			if(managerId != -1) {
	   			if(selected[i].value != managerId) {   				
	   				sourceSelect.removeChild(selected[i]);   				
	   			}
   			}
   			
   		}			
   	}	  
   	var newOption;
   	// For every option selected
   	for(i = 0; i < selected.length; i++) {
   		if(managerId != -1) {
   		if(selected[i].value != managerId) {   			
	   		if (destSelectHM.length > 0) {
	   			if (destSelectHM[selected[i].value] == null) {
	   				// Create new option
	   				newOption = document.createElement('option');
	   				newOption.id = selected[i].id;
	   				newOption.value = selected[i].value;
	   				newOption.innerHTML = selected[i].innerHTML;
	   				//---
	   				newOption.title = selected[i].title;
	   				//---
	   				destSelect.appendChild(newOption);
	   				if (!remove) sourceSelect.removeChild(selected[i]);
	   			} else{
	   				// already present
	   			}
	   		} else {
	   			// Create new option
	   			newOption = document.createElement('option');
	   			newOption.id = selected[i].id;
	   			newOption.value = selected[i].value;
	   			newOption.innerHTML = selected[i].innerHTML;
	   			//---
	   			newOption.title = selected[i].title;
	   			//---
	   			
	   			destSelect.appendChild(newOption);
	   			if (!remove) sourceSelect.removeChild(selected[i]);
	   		}   		
	   	}
   	}	
   	}
   }
   
   
     /**
     * Deletes but only after confirmation.
     * It uses YAHOO.cm.confirmation panel for confirmation dialog !
     * 
     * @author adelina
     */
     function deleteWithConfirmation(_url, _formId, _container, _confirmationMessage, _panelTitle){
     	var url = (this.url != null ? this.url : _url),
     	formId = (this.formId != null ? this.formId : _formId),
     	container = (this.container != null ? this.container : _container),
     	confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
     	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);	     	
     	
     	function handleNo(){
     		YAHOO.cm.confirmation.hide();
     		YAHOO.cm.confirmation.destroy();
     	}
     	
     	function handleYes() {
     		YAHOO.cm.smallLoading.show();
     		handleNo();
     		getContentFromUrlDirect(url, container);  		          	
     	}	
     	YAHOO.cm.confirmation =  
		    new YAHOO.widget.SimpleDialog("confirmation",  
		             { width: "300px", 
		               fixedcenter: true, 
		               visible: false, 
		               draggable: true,
		               modal: true,
		               close: false,
		               //text: dialogText,
		               icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
		               constraintoviewport: true,
		               buttons: [ 
		                          { text:"YES".translate('COMMON'), handler: handleYes, isDefault:true },
		                          { text:"NO".translate('COMMON'),  handler:handleNo } ] 
		             } ); 

		YAHOO.cm.confirmation.setHeader('<span>' + panelTitle + '</span>');
		YAHOO.cm.confirmation.setBody(confirmationMessage);	
		YAHOO.cm.confirmation.render(document.body);
		YAHOO.cm.confirmation.show();
     }
     
     /**
      * Specific delete with confirmation for deleting external team members from a project form when the 
      * project has not yet been saved
      * Deletes but only after confirmation.
      * It uses YAHOO.cm.confirmation panel for confirmation dialog !
      * 
      * @author adelina
      * @author alexandru.dobre
      */
      function deleteWithConfirmationForAddExternal(firstName, lastName, id, projectId, memberId, container, confirmationMessage, panelTitle){      	      	
    	    	    	
      	function handleNo(){
      		YAHOO.cm.confirmation.hide();
      		YAHOO.cm.confirmation.destroy();
      	}
      	
      	function handleYes() {
      		YAHOO.cm.smallLoading.show();
      		handleNo();
      		
      		if(memberId > 0) {      		
      			var endText = '"' + ',"' + 'memberId' + '"' + ' : ' + '"' + memberId + '"';
   				var end = JSON_STR.indexOf(endText);
   				var string = JSON_STR.substring(0, end);	   			
   				var start = string.lastIndexOf('"');	   				
   				var idul = JSON_STR.substring(start + 1, end);	   				   			   				
      			
      			if(id == 0) {      				
	      			JSON_STR = addTeamMemberForDelete(memberId);
	      			if(idul > 0) {
	   					id = idul;
	   				}
      			} else {      				
      				JSON_STR = updateTeamMemberForDelete(memberId, idul);
      			}
      			//alert("JSON_STR = " + JSON_STR);
			} else {       	      		      	
	      		//alert("JSON_STR = " + JSON_STR);
	      		DELETED_MEMBER_ID = id;			  			   	
			   	JSON_STR = deleteJSONStringElementForTeamMemmber(id);			   				   
			}
		   	
		   	//alert("deleteWithConfirmationForAddExternal = " + JSON_STR);
		   	
		   	submitFormExternalTeamMember('TeamMemberListing.htm', 'TEAM_MEMBERS', 'ACTION=GET_FROM_ADD&ACTION_TYPE=DELETE&firstName='+firstName+'&lastName='+lastName+'&members='+JSON_STR+'&projectId='+projectId+'&nrOfMembers='+TEAM_MEMBER);
      		   		
      		YAHOO.cm.smallLoading.hide();     		          	
      	}	
      	YAHOO.cm.confirmation =  
 		    new YAHOO.widget.SimpleDialog("confirmation",  
 		             { width: "300px", 
 		               fixedcenter: true, 
 		               visible: false, 
 		               draggable: true,
 		               modal: true,
 		               close: false,
 		               //text: dialogText,
 		               icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
 		               constraintoviewport: true,
 		               buttons: [ 
 		                          { text:"YES".translate('COMMON'), handler: handleYes, isDefault:true },
 		                          { text:"NO".translate('COMMON'),  handler:handleNo } ] 
 		             } ); 

 		YAHOO.cm.confirmation.setHeader('<span>' + panelTitle + '</span>');
 		YAHOO.cm.confirmation.setBody(confirmationMessage);	
 		YAHOO.cm.confirmation.render(document.body);
 		YAHOO.cm.confirmation.show();
      }
     
     /**
     * Submits the job form but only after confirmation.
     * It uses YAHOO.cm.confirmation panel for confirmation dialog !
     * 
     * @author adelina
     */
     function submitProjectFormWithConfirmation(_url, _formId, _container, _confirmationMessage, _projectSelectedId, _panelTitle){
     	var url = (this.url != null ? this.url : _url),
     		formId = (this.formId != null ? this.formId : _formId),
     		container = (this.container != null ? this.container : _container),
     		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
     		projectSelectedId = (this.projectSelectedId != null ? this.projectSelectedId : _projectSelectedId),
     		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
     		
     	function handleNo(){
     		YAHOO.cm.confirmation.hide();
     		YAHOO.cm.confirmation.destroy();
     	}
     	
     	function handleYes() {
     		YAHOO.cm.smallLoading.show();
     		handleNo();
     		submitForm(url, formId, container);
     		YAHOO.cm.smallLoading.hide();
     		
     	}
     	
     	YAHOO.cm.confirmation =  
     			    new YAHOO.widget.SimpleDialog("confirmation",  
     			             { width: "300px", 
     			               fixedcenter: true, 
     			               visible: false, 
     			               draggable: true,
     			               modal: true,
     			               close: false,
     			               //text: dialogText,
     			               icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
     			               constraintoviewport: true,
     			               buttons: [ 
     			                          { text:"YES".translate('COMMON'), handler: handleYes, isDefault:true },
     			                          { text:"NO".translate('COMMON'),  handler:handleNo } ] 
     			             } ); 
     		
     	YAHOO.cm.confirmation.setHeader('<span>' + panelTitle + '</span>');	
     	
     	confirmationMessage = confirmationMessage.substr(0, confirmationMessage.length - 1) + " " + projectSelectedId + confirmationMessage.substr(confirmationMessage.length - 1) + " ";
     	YAHOO.cm.confirmation.setBody(confirmationMessage);
     	YAHOO.cm.confirmation.render(document.body);	
     	YAHOO.cm.confirmation.show();			
     }

/**
 * Validates the client form c_name input
 * @author Coni
 * @return
 */     
function validateClientC_nameInput() {
	var div = document.getElementById("c_name_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_c_name");
		if (input.value != null && input.value != "") {
			//the client c_name input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_C_NAME_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the client form c_cui input
 * @author Coni
 * @return
 */     
function validateClientC_cuiInput() {
	var div = document.getElementById("c_cui_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_c_cui");
		if (input.value != null && input.value != "") {
			//the client c_cui input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_C_CUI_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the client form c_iban input
 * @author Coni
 * @return
 */     
function validateClientC_ibanInput() {
	var div = document.getElementById("c_iban_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_c_iban");
		if (input.value != null && input.value != "") {
			//the client c_iban input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_C_IBAN_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the client form c_location input
 * @author Coni
 * @return
 */     
function validateClientC_locationInput() {
	var div = document.getElementById("c_location_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_c_location");
		if (input.value != null && input.value != "") {
			//the client c_location input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_C_LOCATION_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the client form p_firstName input
 * @author Coni
 * @return
 */     
function validateClientP_firstNameInput() {
	var div = document.getElementById("p_firstName_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_p_firstName");
		if (input.value != null && input.value != "") {
			//the client p_firstName input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_P_FIRSTNAME_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the client form p_lastName input
 * @author Coni
 * @return
 */     
function validateClientP_lastNameInput() {
	var div = document.getElementById("p_lastName_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_p_lastName");
		if (input.value != null && input.value != "") {
			//the client p_lastName input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_P_LASTNAME_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}
 
/**
 * Validates the client form phone input
 * @author Coni
 * @return
 */     
function validateClientPhoneInput() {
	var div = document.getElementById("phone_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_phone");
		if (input.value != null && input.value != "") {
			//the client phone input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_PHONE_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the client form address input
 * @author Coni
 * @return
 */     
function validateClientAddressInput() {
	var div = document.getElementById("address_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_address");
		if (input.value != null && input.value != "") {
			//the client address input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_ADDRESS_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the client form email input
 * @author Coni
 * @return
 */     
function validateClientEmailInput() {
	var div = document.getElementById("email_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("clientForm_email");
		if (input.value != null && input.value != "") {
			//the client address input is not empty
			var t = input.value.trim();
			t = t.toLowerCase();
			if (t.match(/^[a-zA-Z]+([_\.-]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]+([\.-]?[a-zA-Z0-9]+)*(\.[a-zA-Z]{2,4})+$/)) {
				return true;
			} else {
				div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_EMAIL_FORMAT_ERROR'.translate('COMMON') + "</span>";
				return false;
			}
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CLIENTFORM_EMAIL_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}