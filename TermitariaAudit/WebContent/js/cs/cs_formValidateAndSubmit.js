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
 * Function for validating and submitting a form
 * 
 * @author alu
 */
function validateAndSubmitFormWithParams(url, formId, container){
	if (validateForm(formId)){
		submitForm(url, formId, container);
	}
}

//========================================================================================================
/**
 * Function for validating and submitting an audit search form, verifying if any person name has been entered
 * @author coni
 */
function validateAndSubmitAuditSearchForm(){
	var userInputAutoComplete = document.getElementById(this.userInputAutoCompleteId);
	if ( userInputAutoComplete.value == "") {
		document.getElementById(this.userHiddenFormElementId).value = "";
	}
	if (validateForm(this.formId)){
		submitForm(this.url, this.formId, this.container, true);
	}
}
/**
 * Object used when we want to submit a form with confirmation
 * @author dan.damian
 */
function ObjSubmitWithConfirmation(url, formId, container, confirmationMessage) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.confirmationMessage = confirmationMessage;
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
 * It uses YAHOO.audit.confirmation panel for confirmation dialog !
 * 
 * @author dan.damian
 */
function submitFormWithConfirmation(_url, _formId, _container, _confirmationMessage){
	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage);
	
	function handleNo(){
		YAHOO.audit.confirmation.hide();
		YAHOO.audit.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.audit.smallLoading.show();
		handleNo();
		submitForm(url, formId, container, true);
		YAHOO.audit.smallLoading.hide();
		
	}
	
	YAHOO.audit.confirmation =  
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
	
	YAHOO.audit.confirmation.setBody(confirmationMessage);
	YAHOO.audit.confirmation.render(document.body);
	YAHOO.audit.confirmation.show();
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
		if (this.withConfirmation && YAHOO.audit.confirmation){
			//The panel it's hard coded as it cannot be
			//passed as a parameter. 
			YAHOO.audit.confirmation.hide();
		}
	}
	
	var AjaxObject = { 
		handleSuccess:function (o){
			
			if(o.responseText !== undefined){
				sessionExpired(o);
				var div = document.getElementById(divId);
				div.innerHTML = o.responseText;
				YAHOO.audit.smallLoading.hide();
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
			}
		}, 
		handleFailure:function (o){
			YAHOO.audit.smallLoading.hide();
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
	YAHOO.audit.smallLoading.show();
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
 			

 	div.style.display='block';		
 			
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
 				
 	//toggleDisabled(div);
 	div.style.display='none';
 	
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
 	//alert("showInfoOnKeyUpInField");
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
  * It uses YAHOO.om.confirmation panel for confirmation dialog !
  * 
  * @author coni
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
 		YAHOO.audit.confirmation.hide();
 		YAHOO.audit.confirmation.destroy();
 	}
 	
 	function handleYes() {
 		YAHOO.audit.smallLoading.show();
 		handleNo();
		submitForm(url, formId, container);
 		YAHOO.audit.smallLoading.hide();
 	}
 	
 	function handleOk() {
 		YAHOO.audit.selectItemsAlert.hide();
 		YAHOO.audit.selectItemsAlert.destroy();
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
 	if (anyElementChecked || !selectionRequired) {
 		YAHOO.audit.confirmation =  
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
 		                          { text:"Yes", handler: handleYes, isDefault:true },
 		                          { text:"No",  handler:handleNo } ] 
 		             } ); 
 	
 		YAHOO.audit.confirmation.setBody(confirmationMessage);
 		YAHOO.audit.confirmation.render(document.body);
 		YAHOO.audit.confirmation.show();
 	} else {
 		YAHOO.audit.selectItemsAlert = 
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
 		YAHOO.audit.selectItemsAlert.setBody(noSelectedItemsMessage);
 		YAHOO.audit.selectItemsAlert.render(document.body);
 		YAHOO.audit.selectItemsAlert.show();
 	}        
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
		 	sessionExpired(o);
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
 			 			 								
			YAHOO.audit.messagesContainer.setBody('<div class="infoMessage">'.concat(message).concat("</div>"));
										
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
				YAHOO.audit.smallLoading.hide();
				
				//evaluate the script
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
	 		}
		},
			
		startRequest:function() {
			//harvest form data ready to send to the server
			YAHOO.audit.smallLoading.show();
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


 