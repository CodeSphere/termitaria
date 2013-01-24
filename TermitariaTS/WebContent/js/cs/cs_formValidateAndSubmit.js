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
	} else if(c.indexOf(' validate-time ') != -1 && !t.match(/^(\d{2,4}):[0-5][0-9]$/)){		
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
	this.withContext = true;
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
 * Object used when we want to get the currency panel
 * 
 * @author Adelina
 * 
 * @param currencyUrl
 * @param currencyPanelTitle
 * @return
 */
function ObjSubmitCurrencyPanel(currencyUrl, currencyPanelTitle) {
	this.currencyUrl = currencyUrl;
	this.currencyPanelTitle = currencyPanelTitle;	
}

//========================================================================================================
/**
 * Object used when we want to get the activity panel
 * 
 * @author Adelina
 * 
 * @param activityUrl
 * @param activityPanelTitle
 * @return
 */
function ObjSubmitActivityPanel(activityUrl, activityPanelTitle) {
	this.activityUrl = activityUrl;
	this.activityPanelTitle = activityPanelTitle;	
}

//========================================================================================================		
/**
*	Used to open a panel for adding a currency
*
*	@author Adelina
*/
function displayAddCurrencyPanel(_url, _panelTitle) {
	var url = (this.currencyUrl != null ? this.currencyUrl : _url),
	panelTitle = (this.currencyPanelTitle != null ? this.currencyPanelTitle : _panelTitle);			
	
	if (YAHOO.ts.currencyAdd != null){
		YAHOO.ts.currencyAdd.destroy();
	}
	
	//we have to specify the width size fo IE
	YAHOO.ts.currencyAdd = new YAHOO.widget.Panel("ActivityAddPanel", { fixedcenter:true, width: "330px", visible: false, constraintoviewport: true, close: true, draggable:true,
		 effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},  modal: true, zindex:1, iframe: true});
	YAHOO.ts.currencyAdd.setHeader('<span>' + panelTitle + '</span>'); 
	YAHOO.ts.currencyAdd.render(document.body);
	
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.ts.currencyAdd);
	
}

//========================================================================================================		
/**
*	Used to open a panel for adding an activity
*
*	@author Adelina 
*/
function displayAddActivityPanel(_url, _panelTitle) {
	var url = (this.activityUrl != null ? this.activityUrl : _url),
	panelTitle = (this.activityPanelTitle != null ? this.activityPanelTitle : _panelTitle);			
	
	if (YAHOO.ts.activityAdd != null){
		YAHOO.ts.activityAdd.destroy();
	}
	
	//we have to specify the width size fo IE
	YAHOO.ts.activityAdd = new YAHOO.widget.Panel("CurrencyAddPanel", { fixedcenter:true, width: "450px", visible: false, constraintoviewport: true, close: true, draggable:true,
		 effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},  modal: true, zindex:1, iframe: true});
	YAHOO.ts.activityAdd.setHeader('<span>' + panelTitle + '</span>'); 
	YAHOO.ts.activityAdd.render(document.body);
	
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.ts.activityAdd);
	
}

/**
 * 
 * Object used when we want to get the currency object
 * 
 * @author Adelina
 * 
 * @param url
 * @param formId
 * @param container
 * @param inputNameId
 * @param inputInitialsId
 * @return
 */
function ObjSubmitCurrency(url, formId, divId, inputNameId, inputInitialsId, currency1, currency2, currency3, currency4) {
	this.url = url;
	this.formId = formId;
	this.divId = divId;
	this.inputNameId = inputNameId;
	this.inputInitialsId = inputInitialsId;
	this.currency1 = currency1;
	this.currency2 = currency2;
	this.currency3 = currency3;
	this.currency4 = currency4;
}
 
 /**
  * 
  * Object used when we want to get the currency object
  * 
  * @author Adelina
  * 
  * @param url
  * @param formId
  * @param container
  * @param inputNameId
  * @param inputInitialsId
  * @return
  */
 function ObjSubmitCostSheet(url, formId, divId, isUserAllOrPm) {
 	this.url = url;
 	this.formId = formId;
 	this.divId = divId;
 	this.isUserAllOrPm = isUserAllOrPm; 
 }
 
//========================================================================================================
 /**
  * Function for submit currency panel from form
  * @author Adelina
  */
  function submitAndValidateCurrencyPanelForm(_url, _formId, _divId, _inputNameId, _inputInitialsId, _currency1, _currency2, _currency3, _currency4){
	    
	var url = (this.url != null ? this.url : _url),
	   formId = (this.formId != null ? this.formId : _formId),
	   divId = (this.divId != null ? this.divId : _divId),
	   currency1 = (this.currency1 != null ? this.currency1 : _currency1),
	   currency2 = (this.currency2 != null ? this.currency2 : _currency2),
	   currency3 = (this.currency3 != null ? this.currency3 : _currency3),
	   currency4 = (this.currency4 != null ? this.currency4 : _currency4),
	   inputNameId = (this.inputNameId != null ? this.inputNameId : _inputNameId),
	   inputInitialsId = (this.inputInitialsId != null ? this.inputInitialsId : _inputInitialsId);
	  	 		 	 	
 	//if the form isn't validated we go back
 	if (!(validateCurrencyNameInput(inputNameId) & validateCurrencyInitialsInput(inputInitialsId))){
 		return;
 	}
 	
 	var AjaxObject = { 
 		handleSuccess:function (o){
 			sessionExpired(o);
 			if(o.responseText !== undefined){
 				//alert(o.responseText);
 				// we check for the presence of valitation errors
 				var myRegExpName = /id="name.errors"/;
 				var myRegExpInitials = /id="initials.errors"/;
 				
 				var matchName = o.responseText.search(myRegExpName);
 				var matchInitials = o.responseText.search(myRegExpInitials);
 				//alert("Match name: "+matchName +" match initials: "+ matchInitials);

 				if(matchName == -1 && matchInitials == -1 ){
 					destroyAddCurrencyPanel();
 					var div = document.getElementById(divId); 
 					div.innerHTML = o.responseText; 
 					var scriptTags = div.getElementsByTagName('script');
 				}
 				else{
 					YAHOO.ts.currencyAdd.body.innerHTML = o.responseText;
 					var scriptTags = YAHOO.ts.currencyAdd.body.getElementsByTagName('script');
 				}
 								
 				YAHOO.ts.smallLoading.hide();
 				
 				//we run the scripts
 				for(var i=0;i<scriptTags.length;i++){
 					eval(scriptTags[i].innerHTML);
 				}
 				
 				if (document.getElementById('CURRENCYFORM_NAME') != null && document.getElementById('CURRENCYFORM_ID') != null){
 					//we get the name and the id for adding
 					var name = document.getElementById('CURRENCYFORM_NAME').value;
 					var id =  document.getElementById('CURRENCYFORM_ID').value;
 				 		 		
 					//if the currency was added we have to put it in select
 					if(id > 0){ 				 						
 						var myselect1=document.getElementById(currency1); 						
 						myselect1.options[myselect1.options.length]=new Option(name, id, false, false);
 						var myselect2=document.getElementById(currency2);
 						if(myselect2 != null) {
 							myselect2.options[myselect2.options.length]=new Option(name, id, false, false);
 						}
 						var myselect3=document.getElementById(currency3);
 						if(myselect3 != null) {
 							myselect3.options[myselect3.options.length]=new Option(name, id, false, false);
 						}
 						var myselect4=document.getElementById(currency4);
 						if(myselect4 != null) {
 							myselect4.options[myselect4.options.length]=new Option(name, id, false, false);
 						}
 					} 
 				}
 			}
 		}, 
 		handleFailure:function (o){
 			sessionExpired(o);
 			YAHOO.ts.smallLoading.hide();
 			alert("Error " + o.status + " : " + o.statusText);
 	    },
 		startRequest:function() {
 			//harvest form data ready to send to the server
 			var form = document.getElementById(formId);
 			YAHOO.util.Connect.setForm(form);
 			//make request
 			YAHOO.util.Connect.asyncRequest('POST', makeUniqueRequest(url), callback); 
 	    }
 	}; 
 	var callback = {
 		success:AjaxObject.handleSuccess, 
 		failure:AjaxObject.handleFailure 
 	};
 	
 	YAHOO.ts.smallLoading.show();
 	
 	//start transaction
 	AjaxObject.startRequest();
 	
 	//closing the panel
 	//destroyAddCurrencyPanel();
 }


//========================================================================================================
 /**
  * Function for submit activity panel from form
  * @author Adelina
  */
 function submitAndValidateActivityPanelForm(_url, _formId, _divId){
 	
	  var url = (this.url != null ? this.url : _url),
	    formId = (this.formId != null ? this.formId : _formId),
	    divId = (this.container != null ? this.container : _divId);	    
 		  	 
	//if the form isn't validated we go back
 	if (!validateForm(formId)){
 		return;
 	}
 	
 	var AjaxObject = { 
 		handleSuccess:function (o){
 			sessionExpired(o);
 			if(o.responseText !== undefined){ 			 				  		
 				var div = document.getElementById(divId);
 				div.innerHTML = o.responseText; 				
 				YAHOO.ts.smallLoading.hide();
 				
 				//we run the scripts
 				var scriptTags = div.getElementsByTagName('script');
 				for(var i=0;i<scriptTags.length;i++){
 					eval(scriptTags[i].innerHTML);
 				}
 				
 				//we get the name and the id for adding
 				var name = document.getElementById('ACTIVITYFORM_NAME').value;
 				var id =  document.getElementById('ACTIVITYFORM_ID').value;
 				var projectId =  document.getElementById('ACTIVITYFORM_PROJECT').value;
 				
 				
 				var myselect2=document.getElementById("recordForm_project");
 				var selectedValue = null;
 				for (var i = 0; i< myselect2.options.length; i++){
 				 if (myselect2.options[i].selected==true){ 					
 					 selectedValue = myselect2.options[i].value;
 					 break;
 				 }
 				}
 				 				 				 		 	
 				//if the currency was added we have to put it in select
 				if(id > 0){ 		 					
 					var myselect1=document.getElementById("recordForm_activity");
 					if(selectedValue == projectId) {
 						myselect1.options[myselect1.options.length]=new Option(name, id, false, false);
 					}
 				} 
 			}
 		}, 
 		handleFailure:function (o){
 			sessionExpired(o);
 			YAHOO.ts.smallLoading.hide();
 			alert("Error " + o.status + " : " + o.statusText);
 	    },
 		startRequest:function() {
 			//harvest form data ready to send to the server
 			var form = document.getElementById(formId);
 			YAHOO.util.Connect.setForm(form);
 			//make request
 			YAHOO.util.Connect.asyncRequest('POST', makeUniqueRequest(url), callback); 
 	    }
 	}; 
 	var callback = {
 		success:AjaxObject.handleSuccess, 
 		failure:AjaxObject.handleFailure 
 	};
 	
 	YAHOO.ts.smallLoading.show();
 	
 	//start transaction
 	AjaxObject.startRequest();
 	
 	//closing the panel
 	destroyAddActivityPanel();
 }

//========================================================================================================
	
/**
 * Destroy the add currency panel
 * @author Adelina
 * @return
 */
function destroyAddCurrencyPanel(){

	if (YAHOO.ts.currencyAdd != null){
		YAHOO.ts.currencyAdd.hide();
	}
}

//========================================================================================================

/**
 * Destroy the add activity panel
 * @author Adelina
 * @return
 */
function destroyAddActivityPanel(){

	if (YAHOO.ts.activityAdd != null){
		YAHOO.ts.activityAdd.hide();
	}
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
  * Object used for validate the field
  * 
  * @author Adelina
  */
  function ObjFieldPrice(fieldId, currencyRequired, timeUnitRequired, currencyId, timeUnitId) {
 	this.fieldId = fieldId; 
	this.currencyRequired = currencyRequired; 
	this.timeUnitRequired = timeUnitRequired;  	
	this.currencyId = currencyId;
	this.timeUnitId = timeUnitId;
 }

  function ObjFieldTime(fieldId, startTimeId, endTimeId) {
	  this.fieldId = fieldId; 
	  this.startTimeId = startTimeId;
	  this.endTimeId = endTimeId;
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
 * Submits a form but only after confirmation.
 * It uses YAHOO.ts.confirmation panel for confirmation dialog !
 * 
 * @author dan.damian
 */
function submitFormWithConfirmation(_url, _formId, _container, _confirmationMessage){
	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage);
	
	function handleNo(){
		YAHOO.ts.confirmation.hide();
		YAHOO.ts.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.ts.smallLoading.show();
		handleNo();
		submitForm(url, formId, container, true);
		YAHOO.ts.smallLoading.hide();
		
	}
	
	YAHOO.ts.confirmation =  
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
	
	YAHOO.ts.confirmation.setBody(confirmationMessage);
	YAHOO.ts.confirmation.render(document.body);
	YAHOO.ts.confirmation.show();
}


//========================================================================================================

/**
 * Submits a form with delete action but only after confirmation and verifying that at least one item is selected
 * It uses YAHOO.ts.confirmation panel for confirmation dialog !
 * 
 * @author Adelina
 */
function submitWithConfirmation(_url, _formId, _container, _confirmationMessage, _selectedForDeletionElementsName, _noSelectedItemsMessage, _panelTitle){
 	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
		selectedForDeletionElementsName = (this.selectedForDeletionElementsName != null ? this.selectedForDeletionElementsName : _selectedForDeletionElementsName),
		noSelectedItemsMessage = (this.noSelectedItemsMessage != null ? this.noSelectedItemsMessage : _noSelectedItemsMessage),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	function handleNo(){
		YAHOO.ts.confirmation.hide();
		YAHOO.ts.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.ts.smallLoading.show();
		handleNo();
		submitForm(url, formId, container);
		YAHOO.ts.smallLoading.hide();
	}	
	
	function handleOk() {
		YAHOO.ts.selectItemsAlert.hide();
		YAHOO.ts.selectItemsAlert.destroy();
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
		YAHOO.ts.confirmation =  
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
	
		YAHOO.ts.confirmation.setHeader('<span>' + panelTitle + '</span>');
		YAHOO.ts.confirmation.setBody(confirmationMessage);		
		YAHOO.ts.confirmation.render(document.body);
		YAHOO.ts.confirmation.show();
		
	} else {
		YAHOO.ts.selectItemsAlert = 
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
		YAHOO.ts.selectItemsAlert.setHeader('<span>' + panelTitle + '</span>');
		YAHOO.ts.selectItemsAlert.setBody(noSelectedItemsMessage);
		YAHOO.ts.selectItemsAlert.render(document.body);
		YAHOO.ts.selectItemsAlert.show();
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
function submitForm(url, formId, divId, displayLocalMessages){
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
		if (this.withConfirmation && YAHOO.ts.confirmation){
			//The panel it's hard coded as it cannot be
			//passed as a parameter. 
			YAHOO.ts.confirmation.hide();
		}
	}
	var AjaxObject = { 
		handleSuccess:function (o){
			if(o.responseText !== undefined){
				sessionExpired(o);
				var div = document.getElementById(divId);				
				div.innerHTML = o.responseText;				
				YAHOO.ts.smallLoading.hide();
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
			}
		}, 
		handleFailure:function (o){
			YAHOO.ts.smallLoading.hide();
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
	YAHOO.ts.smallLoading.show();
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
  * @author Adelina
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
  * Function for getting the information from a field after inputing   
  *   
  * @author Adelina
  */
 function hideOnBlurInField(_fieldId, _currencyRequired, _timeUnitRequired, _currencyId, _timeUnitId) {
 	
 	var fieldId = (this.fieldId != null ? this.fieldId : _fieldId), 	
 		currencyRequired = (this.currencyRequired != null ? this.currencyRequired : _currencyRequired),
 		timeUnitRequired = (this.timeUnitRequired != null ? this.timeUnitRequired : _timeUnitRequired),
 		currencyId = (this.currencyId != null ? this.currencyId : _currencyId),
 		timeUnitId = (this.timeUnitId != null ? this.timeUnitId : _timeUnitId);
 	
 	var theField = document.getElementById(fieldId).value; 	
 	
 	if(theField != '') { 
 		document.getElementById(currencyRequired).style.display='inline'; 		
 		document.getElementById(currencyId).className = "formField validate-not-first";
 		
 		if(timeUnitRequired != null && timeUnitId != null) { 		
 			document.getElementById(timeUnitRequired).style.display='inline';
 			document.getElementById(timeUnitId).className = "formField validate-not-first"; 
 		}
						
 	} else {
 		document.getElementById(currencyRequired).style.display='none';		
		document.getElementById(currencyId).className = "formField"; 
		
		if(timeUnitRequired != null && timeUnitId != null) { 
			document.getElementById(timeUnitId).className = "formField";
			document.getElementById(timeUnitRequired).style.display='none';	
		}
 	}	
 }

//========================================================================================================
 /**
  * Function for getting the information from a field after inputing   
  *   
  * @author Adelina
  */
 function hideOnBlurInFieldTime(_fieldId, _startTimeId, _endTimeId) {
 	
 	var fieldId = (this.fieldId != null ? this.fieldId : _fieldId), 	
 		startTimeId = (this.startTimeId != null ? this.startTimeId : _startTimeId), 	
 		endTimeId = (this.endTimeId != null ? this.endTimeId : _endTimeId);
 	  	 	 
 	var theField = document.getElementById(fieldId).value;
 	
 	if(fieldId == "recordForm_time") {

 		var timeSplit = TIME.split(":");
 		var timeHour = parseInt(timeSplit[0]);
 		var timeMin = parseInt(timeSplit[1]);

 		var fieldSplit = theField.split(":");
 		var fieldHour = parseInt(fieldSplit[0]);
 		var fieldMin = parseInt(fieldSplit[1]);
 		 		 
 		if((timeHour < fieldHour) || ((timeHour == fieldHour) && (timeMin < fieldMin))) {
	 		document.getElementById(startTimeId).value = '';
	 		document.getElementById(endTimeId).value = '';
	 		document.getElementById("startTimeRequired").style.display = "none";				
			document.getElementById("endTimeRequired").style.display = "none";		
	 	}
 	}
 	
 	if(fieldId == "recordForm_overtimeTime") {
 		
 		var timeSplit = OVERTIME.split(":");
 		var timeHour = parseInt(timeSplit[0]);
 		var timeMin = parseInt(timeSplit[1]);

 		var fieldSplit = theField.split(":");
 		var fieldHour = parseInt(fieldSplit[0]);
 		var fieldMin = parseInt(fieldSplit[1]);
 		
 		if((timeHour < fieldHour) || ((timeHour == fieldHour) && (timeMin < fieldMin))) {
	 		document.getElementById(startTimeId).value = '';
	 		document.getElementById(endTimeId).value = '';
	 		document.getElementById("overTimeStartTimeRequired").style.display = "none";				
			document.getElementById("overTimeEndTimeRequired").style.display = "none";		
	 	}
 	} 	
 }
 

//========================================================================================================
 /**
  * Function for hidding info about how much characters remains until the end of 
  * the field.
  * 
  * @author matti_joona
  * @author Adelina
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
  * @author Adelina
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
  * Submits a form with delete action but only after confirmation and verifying that at least one item is selected(is selectionRequired is enabled)
  * It uses YAHOO.ts.confirmation panel for confirmation dialog !
  * 
  * @author coni
  * @author alu
  */
 function submitDeleteWithConfirmation(_url, _formId, _container, _confirmationMessage, _selectedForDeletionElementsName, _noSelectedItemsMessage, _selectionRequired){
	var url = (this.url != null ? this.url : _url),
 		formId = (this.formId != null ? this.formId : _formId),
 		container = (this.container != null ? this.container : _container),
 		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
 		selectedForDeletionElementsName = (this.selectedForDeletionElementsName != null ? this.selectedForDeletionElementsName : _selectedForDeletionElementsName),
 		noSelectedItemsMessage = (this.noSelectedItemsMessage != null ? this.noSelectedItemsMessage : _noSelectedItemsMessage),
 		selectionRequired = (this.selectionRequired != null ? this.selectionRequired : _selectionRequired);

	function handleNo(){
 		YAHOO.ts.confirmation.hide();
 		YAHOO.ts.confirmation.destroy();
 	}
 	
 	function handleYes() {
 		YAHOO.ts.smallLoading.show();
 		handleNo();
		submitForm(url, formId, container);
 	}
 	
 	function handleOk() {
 		YAHOO.ts.selectItemsAlert.hide();
 		YAHOO.ts.selectItemsAlert.destroy();
 	}
 		              	
 	var selectedElements = document.getElementsByName(selectedForDeletionElementsName);
 	var anyElementChecked = false;
 	//check if at least an element is selected for deletion
 	for (i=0; i<selectedElements.length; i++) {
 		if (selectedElements[i].checked) {
 			anyElementChecked = true;
 		} 
 	}
 	
 	//if at least one element is selected or selectionRequired is not enabled, I have to display the confirmation message, 
 	//otherwise a message requesting to select at least one element is displayed
 	if (anyElementChecked || !selectionRequired) {
 		YAHOO.ts.confirmation =  
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
 	
 		YAHOO.ts.confirmation.setBody(confirmationMessage);
 		YAHOO.ts.confirmation.render(document.body);
 		YAHOO.ts.confirmation.show();
 	} else {
 		YAHOO.ts.selectItemsAlert = 
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
 		YAHOO.ts.selectItemsAlert.setBody(noSelectedItemsMessage);
 		YAHOO.ts.selectItemsAlert.render(document.body);
 		YAHOO.ts.selectItemsAlert.show();
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
 			 			 								
			YAHOO.ts.messagesContainer.setBody('<div class="infoMessage">'.concat(message).concat("</div>"));
										
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

function handleChooseCurrencies() {
		
	var currency = document.getElementById("activityForm_costPriceCurrency");
	for (i=0; i < myselect.options.length; i++){
		 alert("Selected Option's index: " + i)
		currency.options[currency.options.length] = new Option("Movies", "moviesvalue", false, false);
	}

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
				YAHOO.ts.smallLoading.hide();
				
				//evaluate the script
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
	 		}
		},
			
		startRequest:function() {
			//harvest form data ready to send to the server
			YAHOO.ts.smallLoading.show();
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
  * Function for submit activity search form
  * 
  * When withContext is true, it means the function is not called with parameters
  * but without, and another Object defines it's context (It's something specific to YUI).
  * 
  * @author adelina
  */
function submitActivityForm(url, formId, divId) {
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
	} 
	
	submitForm(url, formId, divId);
}
//========================================================================================================
 /**
  * Function for submit person search form
  * 
  * When withContext is true, it means the function is not called with parameters
  * but without, and another Object defines it's context (It's something specific to YUI).
  * 
  * @author adelina
  */
  function submitPersonForm(url, formId, divId) {
	  if (this.withContext) {
			url = this.url;
			formId = this.formId;
			divId = this.container;
		} 
		
		submitForm(url, formId, divId);
  }
  
//========================================================================================================
  /**
   * Function for submit person search form
   * 
   * When withContext is true, it means the function is not called with parameters
   * but without, and another Object defines it's context (It's something specific to YUI).
   * 
   * @author coni
   */
   function submitTimeSheetReportPersonForm(url, formId, divId, panelDiv) {
	   alert("submitTimeSheetReportPersonForm");
 	  if (this.withContext) {
 			url = this.url;
 			formId = this.formId;
 			divId = this.container;
 			panelDiv = this.panelDiv;
 		} 
 		
 	  //first remove all listeners from the panel results div - won't work if the results listing view is rendered
 	  //twice in the panel, hence trying to add twice the same listener
 	  YAHOO.util.Event.removeListener("reportSortLastNameLinkId", "click");
 	  YAHOO.util.Event.removeListener("reportSortFirstNameLinkId", "click");
 	  YAHOO.util.Event.removeListener("reportSortProjectNameLinkId", "click");
 	  YAHOO.util.Event.removeListener("addAll", "click");
 	  YAHOO.util.Event.removeListener("selectAllPersons", "click");
 	  
 	  //submit form
 	  var AjaxObject = {
 			handleSuccess:function (o){
 		  		if(o.responseText !== undefined){
 				sessionExpired(o);
 				var div = document.getElementById(divId);	
 				div.innerHTML = o.responseText;		
 				if (document.getElementById(panelDiv).offsetHeight  >= 365) {
 	 			 	document.getElementById(panelDiv).className = "tsReportPersSearchScrollingDiv";
 				} else {
 					document.getElementById(panelDiv).className = "";
 				}
 				YAHOO.ts.smallLoading.hide();
 				var scriptTags = div.getElementsByTagName('script');
 					for(var i=0;i<scriptTags.length;i++){
 						eval(scriptTags[i].innerHTML);
 					}
 				}
 			}, 
 			handleFailure:function (o){
 				YAHOO.ts.smallLoading.hide();
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
 		YAHOO.ts.smallLoading.show();
 		//start transaction
 		AjaxObject.startRequest();
   }
  
//========================================================================================================
   /**
    * Submits the form for saving the project details
    * @author Adelina
    */
   function submitSaveProjectDetails(_url, _formId, _container) {
    	
   	var url = (this.url != null ? this.url : _url),
       formId = (this.formId != null ? this.formId : _formId),
       container = (this.container != null ? this.container : _container);         	      	
   	
   	if(validateForm(formId)) {
   		submitForm(url, formId, container);
   	}
   		   		     		    
   }
   
   /**
	 * Submits the form for saving the team member details
	 * 
	 * @author Adelina
	 * 
	 * @param _url
	 * @param _formId
	 * @param _container
	 * @return
	 */
	function submitSaveTeamMemberDetail(_url, _formId, _container) {
		var url = (this.url != null ? this.url : _url),
	       formId = (this.formId != null ? this.formId : _formId),
	       container = (this.container != null ? this.container : _container);         	      	
	   	
	   	if(validateForm(formId)) {
	   		submitForm(url, formId, container);
	   	}
	}
	
	/**
	 * Submits the form for saving the person details
	 * 
	 * @author Adelina
	 * 
	 * @param _url
	 * @param _formId
	 * @param _container
	 * @return
	 */
	function submitSavePersonDetail(_url, _formId, _container) {
		var url = (this.url != null ? this.url : _url),
	       formId = (this.formId != null ? this.formId : _formId),
	       container = (this.container != null ? this.container : _container);         	      	
	   	
	   	if(validateForm(formId)) {
	   		submitForm(url, formId, container);
	   	}
	}


	/**
	 * Submits the form for saving activity
	 *
	 * @author Adelina
	 * 
	 * @param _url
	 * @param _formId
	 * @param _container
	 * @return
	 */

	function submitSaveActivity(_url, _formId, _container, _isManager) {
		 var url = (this.url != null ? this.url : _url),
	     formId = (this.formId != null ? this.formId : _formId),
	     container = (this.container != null ? this.container : _container);     	      
	 	 
		 if(validateForm(formId)) {	
			 submitForm(url, formId, container);
		 } 	
	}
	

//=========================================================================================================
function ObjValidateAndSubmitRecordSearch(url, formId, container, teamMemberInput, teamMemberInputHiddenFieldId) {
	 this.url = url;
	 this.formId = formId;
	 this.container = container;
	 this.teamMemberInput = teamMemberInput;
	 this.teamMemberInputHiddenFieldId = teamMemberInputHiddenFieldId;
	 this.withContext = true;
}



	/**
    * Function for validating and submitting record search form
    * It sets the teamMemberId hidden fields values to -1 before submittting the form 
    * when the user removes the name input text after a previous search
    *
    * When withContext is true, it means the function is not called with parameters
    * but without, and another Object defines it's context (It's something specific to YUI).
    * 
    * @author Coni
    */ 
function validateAndSubmitRecordSearchForm(url, formId, divId, teamMemberInput, teamMemberInputHiddenFieldId) {
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
		teamMemberInput = this.teamMemberInput;
		teamMemberInputHiddenFieldId = this.teamMemberInputHiddenFieldId;
	}
	
	if (YAHOO.util.Dom.get(teamMemberInput).value == ""){	
		YAHOO.util.Dom.get(teamMemberInputHiddenFieldId).value = -1; 		
	}
   	if(validateForm(formId)) {
   		//alert("hei");
   		submitForm(url, formId, divId);
   	}
}

	/**
	 * Submits the form for saving a Record
	 * @author Coni
	 */
	function submitSaveRecord(_url, _formId, _container) {
		var url = (this.url != null ? this.url : _url),
		    formId = (this.formId != null ? this.formId : _formId),
		    container = (this.container != null ? this.container : _container);
		if (validateRecordActivitySelect() & validateRecordProjectSelect() & validateRecordTimeSelect() & validateRecordUserInput()
				& validateRecordTitleInput() & validateRecordDescriptionInput()) {		
			submitForm(url, formId, container);
		}
	}
	
	/**
	 * Submits the form for saving a Record without Activity and Project Validation
	 * @author Andreea
	 */
	function submitSaveRecordWithoutActivityAndProjectValidation(_url, _formId, _container) {
		var url = (this.url != null ? this.url : _url),
		    formId = (this.formId != null ? this.formId : _formId),
		    container = (this.container != null ? this.container : _container);
		if (validateRecordTimeSelect() & validateRecordUserInput() & validateRecordTitleInput() & validateRecordDescriptionInput()) {	
			submitForm(url, formId, container);
		}
	}
	
	/**
	 * Validates the record form project select
	 * @author Coni
	 * @return
	 */     
	function validateRecordProjectSelect() {
		var div = document.getElementById("project_message");
		//verify if a message div exists
		if (div != null) {
			div.innerHTML = "";
			var select = document.getElementById("recordForm_project");
			if (select.selectedIndex > 0) {
				//the record project selected option it's not the first one
				return true;
			} else {
				div.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_PROJECT_ERROR'.translate('COMMON') + "</span>";
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Validates the record form activity select
	 * @author Coni
	 * @return
	 */     
	function validateRecordActivitySelect() {
		var div = document.getElementById("activity_message");
		//verify if a message div exists
		if (div != null) {
			div.innerHTML = "";
			var select = document.getElementById("recordForm_activity");
			if (select.selectedIndex > 0) {
				//the record activity selected option it's not the first one
				return true;
			} else {
				div.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_ACTIVITY_ERROR'.translate('COMMON') + "</span>";
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Validates the record form title input
	 * @author Andreea
	 * @return
	 */     
	function validateRecordTitleInput() {
		var div = document.getElementById("title_message");
		//verify if a message div exists
		if (div != null) {
			div.innerHTML = "";
			var input = document.getElementById("recordForm_title");
			if (input.value != null && input.value != "") {
				//the cost sheet user input is not empty
				return true;
			} else {
				div.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_TITLE_ERROR'.translate('COMMON') + "</span>";
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Validates the record form description input
	 * @author Andreea
	 * @return
	 */     
	function validateRecordDescriptionInput() {
		var div = document.getElementById("description_message");
		//verify if a message div exists
		if (div != null) {
			div.innerHTML = "";
			var input = document.getElementById("recordForm_description");
			if (input.value != null && input.value != "") {
				//the cost sheet user input is not empty
				return true;
			} else {
				div.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_DESCRIPTION_ERROR'.translate('COMMON') + "</span>";
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Validates the record form user input
	 * @author Coni
	 * @return
	 */     
	function validateRecordUserInput() {
		var div = document.getElementById("user_message");
		//verify if a message div exists
		if (div != null) {
			div.innerHTML = "";
			var input = document.getElementById("recordForm_user");
			if (input.value != null && input.value != "") {
				//the record user input is not empty
				return true;
			} else {
				var userInputTds = getElementsByClass("recordUser", null, null);
				var userNotDisplayed = true;
				for (i = 0; i < userInputTds.length; i++) {			
					if (userInputTds[i].style.display == "table-cell" || userInputTds[i].style.display == "table-row") {
						userNotDisplayed = false;
					}
				}
				if (userNotDisplayed) {
					return true;
				} else {
					div.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_USER_ERROR'.translate('COMMON') + "</span>";
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Validates the selection of time periods for a record: 
	 * at least work hours or overtime selected and always 
	 * start and end time selected for the checked option
	 * @author Coni
	 * @return
	 */
	function validateRecordTimeSelect() {
		var timeCheckMessageDiv = document.getElementById("workHoursRecord_message");
		timeCheckMessageDiv.innerHTML = '';
		var overTimeCheckMessageDiv = document.getElementById("overtimeRecord_message");
		overTimeCheckMessageDiv.innerHTML ='';
		var startTimeMessageDiv = document.getElementById("startTime_message");
		startTimeMessageDiv.innerHTML = '';
		var endTimeMessageDiv = document.getElementById("endTime_message");
		endTimeMessageDiv.innerHTML = '';
		var overTimeStartTimeMessageDiv = document.getElementById("overTimeStartTime_message");
		overTimeStartTimeMessageDiv.innerHTML = ''; 
		var overTimeEndTimeMessageDiv = document.getElementById("overTimeEndTime_message");
		overTimeEndTimeMessageDiv.innerHTML = '';
		
		var timeMessageDiv = document.getElementById("time_message");
		timeMessageDiv.innerHTML = '';
		var overTimeTimeMessageDiv = document.getElementById("overTimeTime_message");
		overTimeTimeMessageDiv.innerHTML = '';
		
		//verify if message divs exist
		if (timeCheckMessageDiv != null && overTimeCheckMessageDiv != null && timeMessageDiv != null && overTimeTimeMessageDiv != null && startTimeMessageDiv != null && endTimeMessageDiv != null && overTimeStartTimeMessageDiv != null && overTimeEndTimeMessageDiv != null) {
			//verify if at least one of the row check boxes is checked
			timeCheck = document.getElementById("recordForm_workHoursRecord");
			overTimeCheck = document.getElementById("recordForm_overtimeRecord");
			if (!timeCheck.checked && !overTimeCheck.checked) {
				timeCheckMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_TIME_CHECK_AT_LEAST_ONE_ERROR'.translate('COMMON') + "</span>";
				return false;
			} else {
				var returnFalse = false;
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
							var time = document.getElementById("recordForm_time").value
							var timeHoursMin = time.split(":");
							var hours = parseInt(timeHoursMin[0]);
							var minutes = parseInt(timeHoursMin[1]);
							
							var isValid = false;
													
							if(!isNaN(minutes) && !isNaN(hours)) {							
								 if(timeHoursMin[1].length == 2 && timeHoursMin[0].length >= 2) {
									 isValid = true;
								 }
							}
																				
							if((startTime != "" || endTime != "") && isValid) {
								timeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_TIME_RANGE_EXCEED_ERROR'.translate('COMMON') + "</span>";
							} else {
								timeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_TIME_ERROR'.translate('COMMON') + "</span>";
							}
							returnFalse = true;
						}
					}
				}					
				
				//verify if both overtime start time and end time are selected
				if (overTimeCheck.checked) {
					overTimeStartTime = document.getElementById("recordForm_overTimeStartTime").value;
					overTimeEndTime = document.getElementById("recordForm_overTimeEndTime").value;
					overtTimeTime = document.getElementById("recordForm_overtimeTime").value;
					
					if( overtTimeTime == "" && (overTimeStartTime != "" || overTimeEndTime != "")) {
						if (overTimeStartTime == null || overTimeStartTime == "") {
							overTimeStartTimeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_OVERTIME_START_TIME_ERROR'.translate('COMMON') + "</span>";
							returnFalse = true;
						}
						if (overTimeEndTime == null || overTimeEndTime == "") {
							overTimeEndTimeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_OVERTIME_END_TIME_ERROR'.translate('COMMON') + "</span>";
							returnFalse = true;
						}
					}
					
					if (overtTimeTime == null || overtTimeTime == "") {
						overTimeTimeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_OVERTIME_TIME_REQUIRED_ERROR'.translate('COMMON') + "</span>";
						returnFalse = true;
					}  else {
						if(!(FIC_checkField("formfield validate-time ",document.getElementById("recordForm_overtimeTime")))) {							
							var overtime = document.getElementById("recordForm_overtimeTime").value
							var overtimeHoursMin = overtime.split(":");
							var overtimeHours = parseInt(overtimeHoursMin[0]);
							var overtimeMinutes = parseInt(overtimeHoursMin[1]);
							
							var isValid = false;
													
							if(!isNaN(overtimeMinutes) && !isNaN(overtimeHours)) {							
								 if(overtimeHoursMin[1].length == 2 && overtimeHoursMin[0].length >= 2) {
									 isValid = true;
								 }
							}											
							
							if((overTimeStartTime != "" || overTimeEndTime != "") && isValid) {
								overTimeTimeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_OVERTIME_TIME_RANGE_EXCEED_ERROR'.translate('COMMON') + "</span>";
							} else {
								overTimeTimeMessageDiv.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_OVERTIME_TIME_ERROR'.translate('COMMON') + "</span>";
							}												
							returnFalse = true;
						}
					}
				}
				if (returnFalse) {
					return false;
				}
			}
		}
		return true;
	}
	
	 /**
 * Deletes but only after confirmation.
 * It uses YAHOO.ts.confirmation panel for confirmation dialog !
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
 		YAHOO.ts.confirmation.hide();
 		YAHOO.ts.confirmation.destroy();
 	}
 	
 	function handleYes() {
 		YAHOO.ts.smallLoading.show();
 		handleNo();
 		getContentFromUrlDirect(url, container);   		          	
 	}	
 	YAHOO.ts.confirmation =  
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

	YAHOO.ts.confirmation.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.ts.confirmation.setBody(confirmationMessage);	
	YAHOO.ts.confirmation.render(document.body);
	YAHOO.ts.confirmation.show();
 }
 
 /**
  * @author Coni
  * @param url
  * @param formId
  * @param container
  * @param teamMemberInput
  * @param teamMemberInputHiddenFieldId
  * @return
  */
 function ObjValidateAndSubmitCostSheetSearch(url, formId, container, teamMemberInput, teamMemberInputHiddenFieldId) {
	 this.url = url;
	 this.formId = formId;
	 this.container = container;
	 this.teamMemberInput = teamMemberInput;
	 this.teamMemberInputHiddenFieldId = teamMemberInputHiddenFieldId;
	 this.withContext = true;
 }
 
//=========================================================================================================
 /**
  * Function for validating and submitting cost sheet search form
  * It sets the teamMemberId hidden fields values to -1 before submittting the form 
  * when the user removes the name input text after a previous search
  *
  * When withContext is true, it means the function is not called with parameters
  * but without, and another Object defines it's context (It's something specific to YUI).
  * 
  * @author Coni
  */ 
function validateAndSubmitCostSheetSearchForm(url, formId, divId, teamMemberInput, teamMemberInputHiddenFieldId) {
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
		teamMemberInput = this.teamMemberInput;
		teamMemberInputHiddenFieldId = this.teamMemberInputHiddenFieldId;
	}
	
	if (YAHOO.util.Dom.get(teamMemberInput).value == ""){	
		YAHOO.util.Dom.get(teamMemberInputHiddenFieldId).value = -1; 		
	}
 	if(validateForm(formId)) {
 		submitForm(url, formId, divId);
 	}
}

/**
 * Submits the form for saving a CostSheet
 * @author Coni
 */
function submitSaveCostSheet(_url, _formId, _divId, _isUserAllOrPm) {
	var url = (this.url != null ? this.url : _url),
	    formId = (this.formId != null ? this.formId : _formId),
	    divId = (this.divId != null ? this.divId : _divId),
	    isUserAllOrPm = (this.isUserAllOrPm != null ? this.isUserAllOrPm : _isUserAllOrPm);
		
	if(isUserAllOrPm) {	
		
		if(validateCostSheetActivityNameInput() /*& validateCostSheetProjectSelect()*/ & validateCostSheetDateInput() /*& validateCostSheetUserInput()*/ & validateCostSheetCostPrice() & validateCostSheetBillingPrice()) {			
			IS_MANAGER = false;
			submitForm(url, formId, divId);
		}		
	} else {	
		if(validateCostSheetActivityNameInput() /*& validateCostSheetProjectSelect()*/ & validateCostSheetDateInput() & validateCostSheetCostPrice()) {			
			IS_MANAGER = false;
			submitForm(url, formId, divId);
		}		
	}			
}

function validateCostSheetBillingPrice() {
	var validate = true;	
	var billableTds = getElementsByClass("billable", null, null);	
	//i verify the billing price currency select only if the billable section is displayed
	if (billableTds[0].style.display != "none") {
		var billingPrice = document.getElementById("costSheetForm_billingPrice");
		if(billingPrice != null) {		
			if (billingPrice.value != "") {
				div = document.getElementById("billingPriceCurrency_message");
				//verify if a message div exists
				if (div != null) {
					div.innerHTML = "";
					var select = document.getElementById("costSheetForm_billingPriceCurrency");
					if (select.selectedIndex > 0) {
						//the record project selected option it's not the first one
						validate = true;						
					} else {
						div.innerHTML = "<span class='validationErrorMessage'>" + 'COSTSHEETFORM_BILLING_PRICE_CURRENCY_ERROR'.translate('COMMON') + "</span>";
						validate = false;						
					}
				} else {	
					validate = false;						
				}
			}
		} else {			
			validate = false;			
		}	
	}
	return validate;
}

/**
 * Validates the cost price and cost price currency required options from the cost sheet form
 * @author Coni
 * @return
 */
function validateCostSheetCostPrice() {
	var returnedValue = true;
	var div = document.getElementById("costPrice_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("costSheetForm_costPrice");
		if (input.value == null || input.value == "") {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'COSTSHEETFORM_COST_PRICE_ERROR'.translate('COMMON') + "</span>";
			returnedValue = false;
		}
	} else {
		returnedValue = false;
	}
	
	div = document.getElementById("costPriceCurrency_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var select = document.getElementById("costSheetForm_costPriceCurrency");
		if (select.selectedIndex == 0) {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'COSTSHEETFORM_COST_PRICE_CURRENCY_ERROR'.translate('COMMON') + "</span>";
			returnedValue = false;
		}
	} else {
		returnedValue = false;
	}
	return returnedValue;
}

/**
 * Validates the cost sheet form project select
 * @author Coni
 * @return
 */     
function validateCostSheetProjectSelect() {
	var div = document.getElementById("project_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var select = document.getElementById("costSheetForm_project");
		if (select.selectedIndex > 0) {
			//the record project selected option it's not the first one
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'COSTSHEETFORM_PROJECT_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the cost sheet form user input
 * @author Coni
 * @return
 */     
function validateCostSheetUserInput() {
	var div = document.getElementById("user_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("costSheetForm_user");
		var inputTrElement = getElementsByClass("costSheetUser", null, null); //get the input tr element
		if (inputTrElement[0].style.display != "none" && input.value != null && input.value != "") {
			//the cost sheet user input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'COSTSHEETFORM_USER_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the cost sheet form activity name input
 * @author Coni
 * @return
 */     
function validateCostSheetActivityNameInput() {
	var div = document.getElementById("activityName_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("costSheetForm_activityName");
		if (input.value != null && input.value != "") {
			//the cost sheet user input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'RECORDFORM_DESCRIPTION_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the cost sheet form date input
 * @author Coni
 * @return
 */     
function validateCostSheetDateInput() {
	var div = document.getElementById("date_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("costSheetForm_date");
		if (input.value != null && input.value != "") {
			//the cost sheet date input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'COSTSHEETFORM_DATE_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Submits the form for saving a Currency
 * @author Coni
 */
function submitSaveCurrency(_url, _formId, _divId, _inputNameId, _inputInitialsId) {
    
	var url = (this.url != null ? this.url : _url),
	    formId = (this.formId != null ? this.formId : _formId),
	    divId = (this.divId != null ? this.divId : _divId),
	    inputNameId = (this.inputNameId != null ? this.inputNameId : _inputNameId),
	    inputInitialsId = (this.inputInitialsId != null ? this.inputInitialsId : _inputInitialsId);
	
	if (validateCurrencyNameInput(inputNameId) & validateCurrencyInitialsInput(inputInitialsId)) {
		
		
		submitForm(url, formId, divId);
	}
}

/**
 * Validates the currency form initials input
 * @author Coni
 * @return
 */     
function validateCurrencyInitialsInput(initialsId) {
	var div = document.getElementById("initials_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById(initialsId);
		if (input.value != null && input.value != "") {
			//the currency initials input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CURRENCYFORM_INITIALS_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the currency form name input
 * @author Coni
 * @return
 */     
function validateCurrencyNameInput(nameId) {
	var div = document.getElementById("name_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById(nameId);
		if (input.value != null && input.value != "") {
			//the currency name input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'CURRENCYFORM_NAME_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Submits the form for saving an Exchange
 * @author Coni
 */
function submitSaveExchange(_url, _formId, _container) {
	var url = (this.url != null ? this.url : _url),
	    formId = (this.formId != null ? this.formId : _formId),
	    container = (this.container != null ? this.container : _container);

	if (/*validateExchangeProjectSelect() &*/ validateExchangeRateInput() & validateExchangeCurrencies()) {
		submitForm(url, formId, container);
	}
}

/**
 * Validates the exchange form rate input
 * @author Coni
 * @return
 */     
function validateExchangeRateInput() {
	var div = document.getElementById("rate_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var input = document.getElementById("exchangeForm_rate");
		if (input.value != null && input.value != "" && parseFloat(input.value) != 0) {
			//the exchange rate input is not empty
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'EXCHANGEFORM_RATE_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the exchange form project select
 * @author Coni
 * @return
 */     
function validateExchangeProjectSelect() {
	var div = document.getElementById("project_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var select = document.getElementById("exchangeForm_project");
		if (select.selectedIndex > 0) {
			//the exchange project selected option it's not the first one
			select.disabled = false;
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'EXCHANGEFORM_PROJECT_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the exchange form first currency select
 * @author Coni
 * @return
 */     
function validateExchangeFirstCurrencySelect() {
	var div = document.getElementById("firstCurrency_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var select = document.getElementById("exchangeForm_firstCurrency");
		if (select.selectedIndex > 0) {
			//the exchange first currency selected option it's not the first one
			select.disabled = false;
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'EXCHANGEFORM_FIRSTCURRENCY_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the exchange form second currency select
 * @author Coni
 * @return
 */     
function validateExchangeSecondCurrencySelect() {
	var div = document.getElementById("secondCurrency_message");
	//verify if a message div exists
	if (div != null) {
		div.innerHTML = "";
		var select = document.getElementById("exchangeForm_secondCurrency");
		if (select.selectedIndex > 0) {
			//the exchange second currency selected option it's not the first one
			select.disabled = false;
			return true;
		} else {
			div.innerHTML = "<span class='validationErrorMessage'>" + 'EXCHANGEFORM_SECONDCURRENCY_ERROR'.translate('COMMON') + "</span>";
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Validates the currency selection: both currencies selected and not the same
 * @author Coni
 * @return
 */
function validateExchangeCurrencies() {
	var firstCurrencySelected = validateExchangeFirstCurrencySelect();
	var secondCurrencySelected = validateExchangeSecondCurrencySelect();
	var div = document.getElementById("secondCurrency_message");
	//verify if a message div exists

	if (firstCurrencySelected == true && secondCurrencySelected == true) {
		if (div != null) {
			div.innerHTML = "";
			var select1 = document.getElementById("exchangeForm_firstCurrency");
			var select2 = document.getElementById("exchangeForm_secondCurrency");
			if (select1.options[select1.selectedIndex].value != select2.options[select2.selectedIndex].value) {
				//the exchange second currency selected it's not the same as the first one
				select1.disabled = false;
				select2.disabled = false;
				return true;
			} else {
				div.innerHTML = "<span class='validationErrorMessage'>" + 'EXCHANGEFORM_SECONDCURRENCY_EQUAL_FIRSTCURRENCY_ERROR'.translate('COMMON') + "</span>";
				return false;
			}
		} else {
			return false;
		}
	} else {
		return false;
	}		
}

/**
 * Used to interogate the controller
 *  
 * @param _url
 * @param _callbackForLoad
 * @return
 * @author mitziuro
 */
function interogate(_url, _callbackForLoad) {
	YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(_url), _callbackForLoad);
}

