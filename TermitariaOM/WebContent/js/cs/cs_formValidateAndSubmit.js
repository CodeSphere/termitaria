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

/**
 * author tekin
 */
function validateAndSubmitForm2(formId, submitId){
	var allGood = true;
	allGood = validateForm(formId);
	
	if(allGood == true) {
		document.getElementById(submitId).click();
		document.getElementById(submitId).disabled = true;
	}
}

// deactivate the enter key
function stopRKey(evt) { 
  var evt = (evt) ? evt : ((event) ? event : null); 
  var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
  if ((evt.keyCode == 13) && (node.type=="text"))  {return false;} 
} 

function selectAll(selectId){
	var modules = document.getElementById(selectId);
	if (modules) {
		for(var i =0; i < modules.options.length; i++) {
			modules.options[i].selected = 'selected';
		}
	}
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
 * 	validate-date (a valid date value)
 * 	validate-email (a valid email address)
 *  validate-phone(digits, spaces, points, hyphen)
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
							//if is file we do nothing
							if(t != 'file') {
								removeClassName(f_in[i],'validation-failed'+cext);
								addClassName(f_in[i],'validation-passed'+cext);
								if(f_in[i].id != null){
									removeMessage(f_in[i].id);
								}	
							}
					} else {
						removeClassName(f_in[i],'validation-passed'+cext);
						addClassName(f_in[i],'validation-failed'+cext);
						//display error message
						errs[errs.length] = (f_in[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON');
						displayMessage(f_in[i].id, (f_in[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON'));
						/*try to get title
						if (f_in[i].getAttribute('title')){
							errs[errs.length] = f_in[i].getAttribute('title');
							displayMessage(f_in[i].id, f_in[i].getAttribute('title'));
						}
						*/
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
					//display error message
					errs[errs.length] = (f_ta[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON');
					displayMessage(f_ta[i].id, (f_ta[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON'));
					/*try to get title
					if (f_ta[i].getAttribute('title')){
						errs[errs.length] = f_ta[i].getAttribute('title');
						displayMessage(f_ta[i].id, f_ta[i].getAttribute('title'));
					}
					*/
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
					//display error message; each JobInDepartment select id from PersonForm contains the department id, so a general 
					//error message id cannot be defined ; 
					//the same happens when selecting the department persons and their jobs
					if (f_sl[i].id.indexOf("jobInDept") == 0 || f_sl[i].id.indexOf("personJob") == 0){
						var tempErrorId = "PERSONFORM_NO_JOB_SELECTED"
						errs[errs.length] = tempErrorId.translate('COMMON');
						displayMessage(f_sl[i].id, tempErrorId.translate('COMMON'));
					}
					else{
						errs[errs.length] = (f_sl[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON');
						displayMessage(f_sl[i].id, (f_sl[i].id.toUpperCase().concat('_ERROR')).toString().translate('COMMON'));
					}
					/*try to get title
					if (f_sl[i].getAttribute('title')){
						errs[errs.length] = f_sl[i].getAttribute('title');
						displayMessage(f_sl[i].id, f_sl[i].getAttribute('title'));
					}
					*/
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

//========================================================================================================
/**
 * FIC_checkField
 * c = className
 * e = the element
 *
 * @author matti_joona
 */
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
		valid = false;
	}  else if (c.indexOf(' validate-alphanum ') != -1 && t.match(/\W/)) {
		//alphanum  bad
		valid = false;
	} else if (c.indexOf(' validate-alphanum-with-space ') != -1 && !t.match(/[^0-9a-zA-Z\s\-\.\_]/)) {
		//alphanum with space bad
		valid = false;	
	} else if(c.indexOf(' validate-phone ') != -1 && (t.match(/[^\d\s\-\.]/))) {
		//phone bad
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
 * Listner added for forms
 * 
 * @author matti_joona
 */
function addListnerToForm() {
	//search dom for all forms
	var frms = document.getElementsByTagName('form');
	for(var i=0;i<frms.length;i++) {
		YAHOO.util.Event.addListener(this.elementId, this.eventName, validateForm);
	}
}

//========================================================================================================
/**
 * Function used for adding listeners to the fields that will be single validated
 * 
 * @author matti_joona
 */
function addListenersToFields(){
	var field;
	for (var i=0; i<this.length; ++i) {
		field = document.getElementById(this[i].elementId);
		YAHOO.util.Event.addListener(this[i].elementId, this[i].eventName, validateField, field, true);
	}
}

//========================================================================================================
/**
 * Object used for storing elementIds of the form fields and events on fields
 * 
 * @author matti_joona
 */
function ObjValidation(elementId, eventName) {
	this.elementId = elementId;
	this.eventName = eventName; 
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
 * Object used for validate number of characters from a field
 * 
 * @author Adelina
 */
function ObjFieldRestriction(fieldId) {
	this.fieldId = fieldId;
}

//========================================================================================================
/**
 * Object used to pass parameters to a submit function
 * 
 * @author matti_joona
 */
function ObjSubmitFromPanel(url, formId, container, panel) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.panel = panel;
}

/**
* Object used to pass parameters to submitSaveAdminIT function
* 
* @author Adelina
* 
*/
function ObjSubmitSaveAdminIt(url, formId, container, usernameUnique, passwordId, passwordConfirmId, passwordDontMatchDidvId) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.usernameUnique = usernameUnique; 
	this.passwordId = passwordId;
	this.passwordConfirmId = passwordConfirmId;
	this.passwordDontMatchDidvId = passwordDontMatchDidvId;
}

/**
 * Submits the form for saving a Person
 * @author dd
 */
function submitSaveAdminIt(_url, _formId, _container, _usernameUnique, _passwordId, _passwordConfirmId, _passwordDontMatchDidvId) {
	 	 
	var url = (this.url != null ? this.url : _url),
	    formId = (this.formId != null ? this.formId : _formId),
	    container = (this.container != null ? this.container : _container),
	    usernameUnique = (this.usernameUnique != null ? this.usernameUnique : _usernameUnique),
	    passwordId = (this.passwordId != null ? this.passwordId : _passwordId),
	    passwordConfirmId = (this.passwordConfirmId != null ? this.passwordConfirmId : _passwordConfirmId),
	    passwordDontMatchDidvId = (this.passwordDontMatchDidvId != null ? this.passwordDontMatchDidvId : _passwordDontMatchDidvId);
	
	if (validateForm(formId)){
		 var passwordInput = document.getElementById(passwordId);
		 var passwordConfirmInput = document.getElementById(passwordConfirmId);
		 if (passwordInput != null && passwordConfirmInput != null) {
			if (passwordInput.value != passwordConfirmInput.value) {
				 document.getElementById(passwordDontMatchDidvId).style.display = "block";
				 return false;
			}
		 }
				
		var usernameUniqueInput = document.getElementById('personForm_usernameDIV2');
		
		var username = null;
		var id = null;
		var elm = document.getElementById(formId);
		var f_in = elm.getElementsByTagName('input');
		for (i=0;i<f_in.length;i++) {
			if(f_in[i].name == "username") {
				username = f_in[i].value;
			} else {
				if(f_in[i].name == "personId") {
					id = f_in[i].value;
				}
			}
		}
		
		if (usernameUniqueInput) {
			interogate('PersonVerifyUsernameUniqueness.htm?198341='.concat(username).concat('&id=').concat(id), {	
				//Function triggerd on succes
				success:function(o){
					if ("<p>NO</p>" == o.responseText) {
						
						submitFormWithFile(url, formId, container);
					} else { 
						if ("<p>YES</p>" == o.responseText) {
							document.getElementById('personForm_usernameDIV2').innerHTML = '<font color="red">'.concat(usernameUnique.toUpperCase().concat('_ERROR').translate('COMMON')).concat('</font>');
						} else {
							if ("<p>ERROR</p>" == o.responseText) {
								document.getElementById('personForm_usernameDIV2').innerHTML = '<font color="red">'.concat(usernameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
							} 
						}
					}
				},
				failure:function(o){ 
					document.getElementById('personForm_usernameDIV2').innerHTML = '<font color="red">'.concat(usernameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
				}
				})
		} else {
			
			submitFormWithFile(url, formId, container);
		}
	}
}
/**
 * Object used to pass parameters to submitSavePerson function
 * 
 * @author dan.damian
 * 
 */
function ObjSubmitSavePerson(url, formId, container, usernameUnique, departmentsSelectId, rolesSelectId) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.usernameUnique = usernameUnique;
	this.departmentsSelectId = departmentsSelectId;
	this.rolesSelectId = rolesSelectId;
}
 

 
/**
 * Object used to pass parameters to submitSaveOrganisation function
 *
 * @author Adelina	 
 */
 
 function ObjSubmitSaveOrganisation(url, formId, container, modulesSelectId, nameUnique) {
 	this.url = url;
 	this.formId = formId;
 	this.container = container;
 	this.modulesSelectId = modulesSelectId;
 	this.nameUnique = nameUnique;
 }
 
 /**
  *
  * Object for request branches
  * 
  * @author mitziuro
  *
  */
 function ObjBranch(url, panelTitle, div, panel_position_x, panel_position_y, sessionOrgId, sessionOrgName){
	 this.url = url;
	 this.panelTitle = panelTitle;
	 this.div = div;
	 this.sessionOrgId = sessionOrgId;
	 this.sessionOrgName = sessionOrgName;
	 this.panel_position_x = panel_position_x;
	 this.panel_position_y = panel_position_y;
 }
 
 /**
 * Object used to pass parameters to submitSaveDepartment function
 *
 * @author Adelina	 
 */
 
 function ObjSubmitSaveDepartment(url, formId, container, personsSelectId) {
 	this.url = url;
 	this.formId = formId;
 	this.container = container;
 	this.personsSelectId = personsSelectId;
 }
 
 
/**
 * Object used to pass parameters to submitSaveRole function
 *
 * @author Adelina	 
 */
 
  function ObjSubmitSaveRole(url, formId, container, rolesSelectId, nameUnique, organisationId) {
 	this.url = url;
 	this.formId = formId;
 	this.container = container;
 	this.rolesSelectId = rolesSelectId;
 	this.nameUnique = nameUnique;
 	this.organisationId = organisationId;
 	
 }
 
 /**
 * Object used to pass parameters to submitSaveFreeday function
 *
 * @author Adelina	 
 */
 
  function ObjSubmitFreeday(url, formId, container, freeday) {
 	this.url = url;
 	this.formId = formId;
 	this.container = container; 
 	this.freeday = freeday;
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
/**
 * Function used for displaying error messages next to field
 * 
 * @author matti_joona
 */
function removeMessage(fieldId){
	var em = document.getElementById(fieldId);	
	var newDiv = document.getElementById(fieldId.concat("DIV"));
	if(newDiv != null){	
		newDiv.innerHTML='';
	}
}	

/**
 * Function for submit form
 * 
 * @author matti_joona
 */
function validateAndSubmitForm(){
	if (validateForm(this.formId)){
		submitForm(this.url, this.formId, this.container);
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
function ObjSubmitWithConfirmation(url, formId, container, confirmationMessage, panelTitle) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.confirmationMessage = confirmationMessage;
	this.panelTitle = panelTitle;
}

//========================================================================================================

/**
 * Object used when we want to get the job panel
 * @author mitziuro
 */
function ObjSubmitJobPanel(jobUrl, jobPanelTitle, handleChooseJobInDepartment) {
	this.jobUrl = jobUrl;
	this.jobPanelTitle = jobPanelTitle;
	this.handleChooseJobInDepartment = handleChooseJobInDepartment;

}	

//========================================================================================================

/**
 * Object used when we want to get the job form from the jobPanel
 * @author mitziuro
 */
 
function ObjSubmitJob(url, formId, container,  handleChooseJobInDepartment) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.handleChooseJobInDepartment = handleChooseJobInDepartment;
}	

//========================================================================================================

	/**
 * Submits a form but only after confirmation.
 * It uses YAHOO.om.confirmation panel for confirmation dialog !
 * 
 * @author dan.damian
 */
function submitFormWithConfirmation(_url, _formId, _container, _confirmationMessage, _panelTitle){
	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	function handleNo(){
		YAHOO.om.confirmation.hide();
		YAHOO.om.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.om.smallLoading.show();
		handleNo();
		submitForm(url, formId, container);
		YAHOO.om.smallLoading.hide();
		
	}	
	YAHOO.om.confirmation =  
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
	
	YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.om.confirmation.setBody(confirmationMessage);
	YAHOO.om.confirmation.render(document.body);
	YAHOO.om.confirmation.show();
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
		YAHOO.om.confirmation.hide();
		YAHOO.om.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.om.smallLoading.show();
		handleNo();
		submitForm(url, formId, container);
		YAHOO.om.smallLoading.hide();
	}	
	
	function handleOk() {
		YAHOO.om.selectItemsAlert.hide();
		YAHOO.om.selectItemsAlert.destroy();
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
		YAHOO.om.confirmation =  
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
	
		YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');
		YAHOO.om.confirmation.setBody(confirmationMessage);		
		YAHOO.om.confirmation.render(document.body);
		YAHOO.om.confirmation.show();
		
	} else {
		YAHOO.om.selectItemsAlert = 
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
		YAHOO.om.selectItemsAlert.setHeader('<span>' + panelTitle + '</span>');
		YAHOO.om.selectItemsAlert.setBody(noSelectedItemsMessage);
		YAHOO.om.selectItemsAlert.render(document.body);
		YAHOO.om.selectItemsAlert.show();
	}
	
}

//========================================================================================================

/**
 * Submits a form with delete action but only after confirmation and verifying that at least one item is selected
 * It uses YAHOO.om.confirmation panel for confirmation dialog !
 * 
 * @author Adelina
 */
function submitDepartmentDeleteWithConfirmation(_url, _formId, _container, _confirmationMessage1, _confirmationMessage2, _confirmationMessage3, _selectedForDeletionElementsName, _noSelectedItemsMessage, _panelTitle, _hasPersons){
 	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage1 = (this.confirmationMessage1 != null ? this.confirmationMessage1 : _confirmationMessage1),
		confirmationMessage2 = (this.confirmationMessage2 != null ? this.confirmationMessage2 : _confirmationMessage2),
		confirmationMessage3 = (this.confirmationMessage3 != null ? this.confirmationMessage3 : _confirmationMessage3),
		selectedForDeletionElementsName = (this.selectedForDeletionElementsName != null ? this.selectedForDeletionElementsName : _selectedForDeletionElementsName),
		noSelectedItemsMessage = (this.noSelectedItemsMessage != null ? this.noSelectedItemsMessage : _noSelectedItemsMessage),
		hasPersons = (this.hasPersons != null ? this.hasPersons : _hasPersons),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
 	
 	
	function handleNo(){
		YAHOO.om.confirmation.hide();
		YAHOO.om.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.om.smallLoading.show();
		handleNo();
		submitForm(url, formId, container);
		YAHOO.om.smallLoading.hide();
	}	
	
	function handleOk() {
		YAHOO.om.selectItemsAlert.hide();
		YAHOO.om.selectItemsAlert.destroy();
	}
		             
	var selectedElements = document.getElementsByName(selectedForDeletionElementsName);
	var selectedHasPersons	 = document.getElementsByName(hasPersons);
	
	var hasPerson = new Array();
	
	for (i=0; i<selectedHasPersons.length; i++) {
		if (selectedElements[i].checked) {
			hasPerson.push(selectedHasPersons[i].value);
			//alert("hasPerson = " + hasPerson);
		}
		
	}
	
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
		YAHOO.om.confirmation =  
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
	
		var isTrue;
		YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');
		if(hasPerson.length == 1 && hasPerson[0] == 'false') {
			YAHOO.om.confirmation.setBody(confirmationMessage1);
		} else if(hasPerson.length == 1 && hasPerson[0] == 'true') { 
			YAHOO.om.confirmation.setBody(confirmationMessage2);
		} else if(hasPerson.length > 1) {
			for (i=0; i<hasPerson.length; i++) {
				if (hasPerson[i] == 'true') {
					isTrue = true;
					break;
				} 
			}
			
			if(isTrue) {
				YAHOO.om.confirmation.setBody(confirmationMessage3);
			} else {
				YAHOO.om.confirmation.setBody(confirmationMessage1);
			}
			
		
		}
				
		YAHOO.om.confirmation.render(document.body);
		YAHOO.om.confirmation.show();
		
	} else {
		YAHOO.om.selectItemsAlert = 
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
		YAHOO.om.selectItemsAlert.setHeader('<span>' + panelTitle + '</span>');
		YAHOO.om.selectItemsAlert.setBody(noSelectedItemsMessage);
		YAHOO.om.selectItemsAlert.render(document.body);
		YAHOO.om.selectItemsAlert.show();
	}
	
}


/**
* Deletes a person but only after confirmation.
* It uses YAHOO.dmc.confirmation panel for confirmation dialog !
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
		YAHOO.om.confirmation.hide();
		YAHOO.om.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.om.smallLoading.show();
		handleNo();
		getContentFromUrlDirect(url, container);
		YAHOO.om.smallLoading.hide();
		
	}	
	YAHOO.om.confirmation =  
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
	
	YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.om.confirmation.setBody(confirmationMessage);	
	YAHOO.om.confirmation.render(document.body);
	YAHOO.om.confirmation.show();
}

//========================================================================================================

 /**
 * Deletes a freeday but only after confirmation.
 * It uses YAHOO.om.confirmation panel for confirmation dialog !
 * 
 * @author adelina
 */
 function deleteFreedayWithConfirmation(_url, _formId, _container, _confirmationMessage, _panelTitle){
 	var url = (this.url != null ? this.url : _url),
 		formId = (this.formId != null ? this.formId : _formId),
 		container = (this.container != null ? this.container : _container),
 		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
 		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle); 				
 		
 	function handleNo(){
 		YAHOO.om.confirmation.hide();
 		YAHOO.om.confirmation.destroy();
 	}
 	
 	function handleYes() {
 		YAHOO.om.smallLoading.show();
 		handleNo();
 		submitForm(url, formId, container);
 		YAHOO.om.smallLoading.hide();
 		
 	}
 	
 	YAHOO.om.confirmation =  
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
 		
 	YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');
 	confirmationMessage = confirmationMessage.substr(0, confirmationMessage.length);
 	YAHOO.om.confirmation.setBody(confirmationMessage);
 	YAHOO.om.confirmation.render(document.body);	
 	YAHOO.om.confirmation.show();
 }



//========================================================================================================

/**
* Submits the organisation form but only after confirmation.
* It uses YAHOO.om.confirmation panel for confirmation dialog !
* 
* @author adelina
*/
function submitOrganisationFormWithConfirmation(_url, _formId, _container, _confirmationMessage, _organisationSelectedId, _panelTitle){
	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
		organisationSelectedId = (this.organisationSelectedId != null ? this.organisationSelectedId : _organisationSelectedId),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
		
	function handleNo(){
		YAHOO.om.confirmation.hide();
		YAHOO.om.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.om.smallLoading.show();
		handleNo();
		submitForm(url, formId, container);
		YAHOO.om.smallLoading.hide();
		
	}
	
	YAHOO.om.confirmation =  
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
		
	YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');	
	
	confirmationMessage = confirmationMessage.substr(0, confirmationMessage.length - 1) + " " + organisationSelectedId + confirmationMessage.substr(confirmationMessage.length - 1) + " ";
	YAHOO.om.confirmation.setBody(confirmationMessage);
	YAHOO.om.confirmation.render(document.body);	
	YAHOO.om.confirmation.show();			
}


/**
* Submits the job form but only after confirmation.
* It uses YAHOO.om.confirmation panel for confirmation dialog !
* 
* @author adelina
*/
function submitJobFormWithConfirmation(_url, _formId, _container, _confirmationMessage, _jobSelectedId, _panelTitle){
	var url = (this.url != null ? this.url : _url),
		formId = (this.formId != null ? this.formId : _formId),
		container = (this.container != null ? this.container : _container),
		confirmationMessage = (this.confirmationMessage != null ? this.confirmationMessage : _confirmationMessage),
		jobSelectedId = (this.jobSelectedId != null ? this.jobSelectedId : _jobSelectedId),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
		
	function handleNo(){
		YAHOO.om.confirmation.hide();
		YAHOO.om.confirmation.destroy();
	}
	
	function handleYes() {
		YAHOO.om.smallLoading.show();
		handleNo();
		submitForm(url, formId, container);
		YAHOO.om.smallLoading.hide();
		
	}
	
	YAHOO.om.confirmation =  
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
		
	YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');	
	
	confirmationMessage = confirmationMessage.substr(0, confirmationMessage.length - 1) + " " + jobSelectedId + confirmationMessage.substr(confirmationMessage.length - 1) + " ";
	YAHOO.om.confirmation.setBody(confirmationMessage);
	YAHOO.om.confirmation.render(document.body);	
	YAHOO.om.confirmation.show();			
}




//========================================================================================================
/**
 * Object used to pass parameters to submitSavePerson function
 * 
 * @author dan.damian
 * 
 */
function ObjSubmitSavePerson(url, formId, container, usernameUnique, passwordId, passwordConfirmId, 
		passwordDontMatchDidvId, departmentsSelectId, rolesSelectId, userGroupsSelectId) {
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.usernameUnique = usernameUnique;
	this.passwordId = passwordId;
	this.passwordConfirmId = passwordConfirmId;
	this.passwordDontMatchDidvId = passwordDontMatchDidvId;
	this.departmentsSelectId = departmentsSelectId;
	this.rolesSelectId = rolesSelectId;
	this.userGroupsSelectId = userGroupsSelectId;
}


/**
 * Submits the form for saving a Person
 * @author dd
 */
function submitSavePerson(_url, _formId, _container, _usernameUnique, _passwordId, _passwordConfirmId, _passwordDontMatchDidvId,  _departmentsSelectId, _rolesSelectId, _userGroupsSelectId) {
	 	 	
	var url = (this.url != null ? this.url : _url),
	    formId = (this.formId != null ? this.formId : _formId),
	    container = (this.container != null ? this.container : _container),
	    usernameUnique = (this.usernameUnique != null ? this.usernameUnique : _usernameUnique),
	    passwordId = (this.passwordId != null ? this.passwordId : _passwordId),
	    passwordConfirmId = (this.passwordConfirmId != null ? this.passwordConfirmId : _passwordConfirmId),
	    passwordDontMatchDidvId = (this.passwordDontMatchDidvId != null ? this.passwordDontMatchDidvId : _passwordDontMatchDidvId),
	    departmentsSelectId = (this.departmentsSelectId != null ? this.departmentsSelectId : _departmentsSelectId),
	    rolesSelectId = (this.rolesSelectId != null ? this.rolesSelectId : _rolesSelectId);
	    userGroupsSelectId = (this.userGroupsSelectId != null ? this.userGroupsSelectId : _userGroupsSelectId);
	
	if (validateForm(formId)){
		 var passwordInput = document.getElementById(passwordId);
		 var passwordConfirmInput = document.getElementById(passwordConfirmId);
		 if (passwordInput != null && passwordConfirmInput != null) {
			if (passwordInput.value != passwordConfirmInput.value) {
				 document.getElementById(passwordDontMatchDidvId).style.display = "block";
				 return false;
			}
		 }
		
		//select all departments in select
		var depts = document.getElementById(departmentsSelectId);
		if (depts) {
			for(var i =0; i < depts.options.length; i++) {
				depts.options[i].selected = 'selected';
			}
		}
		
		//select all roles in select
		var roles = document.getElementById(rolesSelectId);
		if(roles){
			for(i =0; i < roles.options.length; i++) {
				roles.options[i].selected = 'selected';
			}
		}
		
		//select all user groups in select
		var userGroups = document.getElementById(userGroupsSelectId);
		if (userGroups){
			for (i = 0; i < userGroups.options.length; i++){
				userGroups.options[i].selected = 'selected';
			}
		}
		var usernameUniqueInput = document.getElementById('personForm_usernameDIV2');
		
		var username = null;
		var id = null;
		var elm = document.getElementById(formId);
		var f_in = elm.getElementsByTagName('input');
		for (i=0;i<f_in.length;i++) {
			if(f_in[i].name == "username") {
				username = f_in[i].value;
			} else {
				if(f_in[i].name == "personId") {
					id = f_in[i].value;
				}
			}
		}
		
		if (usernameUniqueInput) {
			interogate('PersonVerifyUsernameUniqueness.htm?198341='.concat(username).concat('&id=').concat(id), {	
				//Function triggerd on succes
				success:function(o){
					if ("<p>NO</p>" == o.responseText) {
						
						submitFormWithFile(url, formId, container);
					} else { 
						if ("<p>YES</p>" == o.responseText) {
							document.getElementById('personForm_usernameDIV2').innerHTML = '<font color="red">'.concat(usernameUnique.toUpperCase().concat('_ERROR').translate('COMMON')).concat('</font>');
						} else {
							if ("<p>ERROR</p>" == o.responseText) {
								document.getElementById('personForm_usernameDIV2').innerHTML = '<font color="red">'.concat(usernameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
							} 
						}
					}
				},
				failure:function(o){ 
					document.getElementById('personForm_usernameDIV2').innerHTML = '<font color="red">'.concat(usernameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
				}
				})
		} else {
			
			submitFormWithFile(url, formId, container);
		}
	}
}
 
 
 


//========================================================================================================
/**
 * Submits the form for updating the department persons and their jobs in the
 * specific department
 * @author coni
 */
 function submitUpdateDepartmentPersons( _url, _formId, _container, _changePersonsContainerId, _personsSelectId) {
 	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container),
    changePersonsContainerId = (this.changePersonsContainerId != null ? this.changePersonsContainerId : _changePersonsContainerId),
    personsSelectId = (this.personsSelectId != null ? this.personsSelectId : _personsSelectId);
    
   	//select all persons from the select
	var persons = document.getElementById(personsSelectId);
	if (persons) {
		for(var i =0; i < persons.options.length; i++) {
			persons.options[i].selected = 'selected';
		}
	}
	if (validateForm(formId)){
		submitForm(url, formId, container);
		document.getElementById(changePersonsContainerId).innerHTML = '';
	}
 }
//========================================================================================================
/**
 * Submits the form for saving an Organisation
 * @author Adelina
 */
function submitSaveOrganisation(_url, _formId, _container, _modulesSelectId, _nameUnique) {

	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container),	   
    modulesSelectId = (this.modulesSelectId != null ? this.modulesSelectId : _modulesSelectId),
	nameUnique = (this.nameUnique != null ? this.nameUnique : _nameUnique);

	if (validateForm(formId)){
	
		//select all modules in select
		
		selectAll(modulesSelectId);

		var name = null;
		var id = null;
		var elm = document.getElementById(formId);
		var f_in = elm.getElementsByTagName('input');
		for (i=0;i<f_in.length;i++) {
			if(f_in[i].name == "name") {
				name = f_in[i].value;
			} else {
				if(f_in[i].name == "organisationId") {
					id = f_in[i].value;
				}
			}
		}
		
		interogate('OrganisationVerifyNameUniqueness.htm?198341='.concat(name).concat('&id=').concat(id), {	
		//Function triggerd on succes
			success:function(o){
				if ("<p>NO</p>" == o.responseText) {
					submitForm(url, formId, container);
				} else { 
					if ("<p>YES</p>" == o.responseText) {
						document.getElementById('organisationForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.toUpperCase().concat('_ERROR').translate('COMMON')).concat('</font>');
					} else {
							if ("<p>ERROR</p>" == o.responseText) {
								document.getElementById('organisationForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
							} 
					}
				}
			},
			failure:function(o){ 
				document.getElementById('organisationForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
			}
		});		
	}	
}

//========================================================================================================
/**
 * Submits the form for saving a Department
 * @author Adelina
 */
function submitSaveDepartment(_url, _formId, _container, _personsSelectId) {

	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container),	   
    personsSelectId = (this.personsSelectId != null ? this.personsSelectId : _personsSelectId);

	if (validateForm(formId)){
		//select all persons in select
		var persons = document.getElementById(personsSelectId);
		if(persons){
			for(var i =0; i < persons.options.length; i++) {
				persons.options[i].selected = 'selected';	
			}
		}
		if (document.getElementById("managerAutoComplete").value == "" ){				
			document.getElementById("managerId").value = null;	 	
	 	} 
		
		submitForm(url, formId, container);	
	}
}


//========================================================================================================
/**
 * Submits the form for saving a Role
 * @author Adelina
 */
function submitSaveRole(_url, _formId, _container, _rolesSelectId, _nameUnique, _organisationId) {

	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container),	   
    rolesSelectId = (this.rolesSelectId != null ? this.rolesSelectId : _rolesSelectId);
	nameUnique = (this.nameUnique != null ? this.nameUnique : _nameUnique),
	organisationId = (this.organisationId != null ? this.organisationId : _organisationId);			
	
	if (validateForm(formId) && showNoModulesMessage()){
		//select all permissions in select
		var roles = document.getElementById(rolesSelectId);
		if (roles) {
			for(var i =0; i < roles.options.length; i++) {
				roles.options[i].selected = 'selected';
			}
		}
		
		var name = null;
		var id = null;
		var orgId = null;
		var elm = document.getElementById(formId);
		var f_in = elm.getElementsByTagName('input');
		for (i=0;i<f_in.length;i++) {
			if(f_in[i].name == "name") {
				name = f_in[i].value;
			} else {
				if(f_in[i].name == "roleId") {
					id = f_in[i].value;
				} 
			}
		}
		
		interogate('RoleVerifyNameUniqueness.htm?198341='.concat(encodeURIComponent(name)).concat('&198342=').concat(organisationId).concat('&id=').concat(id), {	
		//Function triggerd on succes
			success:function(o){
				if ("<p>NO</p>" == o.responseText) {
					submitForm(url, formId, container);
				} else { 
					if ("<p>YES</p>" == o.responseText) {
						document.getElementById('roleForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.toUpperCase().concat('_ERROR').translate('COMMON')).concat('</font>');
					} else {
						if ("<p>ERROR</p>" == o.responseText) {
							document.getElementById('roleForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
						} 
					}
				}
			},
			failure:function(o){ 
				document.getElementById('roleForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
			}
		})
	}
}

//========================================================================================================
/**
 * Submits the form for saving a group of users
 * @author Coni
 */
function submitSaveUserGroup(_url, _formId, _container, _personsSelectId, _nameUnique, _organisationId) {

	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container),	   
    personsSelectId = (this.personsSelectId != null ? this.personsSelectId : _personsSelectId);
	nameUnique = (this.nameUnique != null ? this.nameUnique : _nameUnique);
	organisationId = (this.organisationId != null ? this.organisationId : _organisationId);			
	
   	if (validateForm(formId)) {
		//select all persons in select
		var persons = document.getElementById(personsSelectId);
		if (persons) {
			for(var i =0; i < persons.options.length; i++) {
				persons.options[i].selected = 'selected';
			}
		}		
		var name = null;
		var id = null;
		var elm = document.getElementById(formId);
		var f_in = elm.getElementsByTagName('input');
		for (i = 0; i < f_in.length; i++) {
			if(f_in[i].name == "name") {
				name = f_in[i].value;
			} else {
				if(f_in[i].name == "userGroupId") {
					id = f_in[i].value;
				}
			}
		}
		
		interogate('UserGroupVerifyNameUniqueness.htm?198341='.concat(encodeURIComponent(name)).concat('&198342=').concat(organisationId).concat('&id=').concat(id), {	
		//Function triggerd on succes
			success:function(o){			
				if ("<p>NO</p>" == o.responseText) {
					submitForm(url, formId, container);
				} else { 
					if ("<p>YES</p>" == o.responseText) {
						document.getElementById('userGroupForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.toUpperCase().concat('_ERROR').translate('COMMON')).concat('</font>');
					} else {
							if ("<p>ERROR</p>" == o.responseText) {
								document.getElementById('userGroupForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
							} 
					}
				}
			},
			failure:function(o){ 
				document.getElementById('userGroupForm_nameDIV2').innerHTML = '<font color="red">'.concat(nameUnique.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
			}
		})		
	}
	
}

//========================================================================================================
/*
 * Object used to pass parameters to submitSaveUserGroup function
 * @author Coni
 */		
function ObjSubmitSaveUserGroup(url, formId, container, personsSelectId, nameUnique, organisationId){
	this.url = url;
	this.formId = formId;
	this.container = container;
	this.personsSelectId = personsSelectId;
	this.nameUnique = nameUnique;
	this.organisationId = organisationId;
}

/**
 * Validates and submits the form for saving a form
 * @author Adelina
 */
function submitValidateAndSaveForm(_url, _formId, _container) {

	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container);	      
	validateAndSubmitFormWithParams(url, formId, container);
}

 /**
  * Validates and submits the ceo for the organisation
  * 
  * @author Adelina
  * 
  * @param _url
  * @param _formId
  * @param _container
  * @return
  */
function submitSaveCeo(_url, _formId, _container) {

	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container);	      
	if(validateCeoSelection()) {	
		validateAndSubmitFormWithParams(url, formId, container);
	}
}

/**
 * Validates the Ceo Selection, if there is no ceo selection and
 * it tries to submits the form, a message is displayed
 * 
 * @author Adelina
 * @return
 */
function validateCeoSelection() {	
	
	var div = document.getElementById('ceo_message');
	
	var input = document.getElementById("managerAutoComplete");	
	if (input.value != null && input.value != ""){
		// we have a valid imput_value, that means it is not null and it has characters 			
		return true;
	} else {
		div.innerHTML = "<span class='validationErrorMessage'>" + 'DEPARTMENTFORM_MANAGERID_ERROR'.translate('COMMON') + "</span";
		// we have a null input or an input with no characters
		return false;
	}		
}
 

//========================================================================================================
/**
 * Submits the form for deleting a Freeday
 * @author Adelina
 */
function submitDeleteFreeday(_url, _formId, _container) {	
	var url = (this.url != null ? this.url : _url),
    formId = (this.formId != null ? this.formId : _formId),
    container = (this.container != null ? this.container : _container);	
	submitForm(url, formId, container);	
}

//========================================================================================================
/**
 * Submits the form for saving a freeday
 * @author Adelina
 */
function submitSaveFreeday(_url, _formId, _container, _freeday) {
	var url = (this.url != null ? this.url : _url);
	var formId = (this.formId != null ? this.formId : _formId);
	var container = (this.container != null ? this.container : _container);
	var freeday = (this.freeday != null ? this.freeday : _freeday);
	
	if (validateForm(formId)){		
		var day = document.getElementById(freeday);
		if (day.value) {			
			submitForm(url, formId, container);		
			YAHOO.om.showFreedayPanel.hide();
		} else{		
			alert("Would you please enter some text?");
		}
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

/**
 * Function used to determine if a record already exists in the database
 * 
 * 
 * @param _url
 * @param _div
 * @param _responseInput
 * @param _name
 * @param _orgId
 * @param _existentName
 * @param _existentOrgId
 * @return
 * 
 * @author mitziuro
 */
function verifyUniqueness(_url, _div, _responseInput, _name, _orgId, _existentName, _existentOrgId) {
			
	//conditions for update 
	if(_existentOrgId == null && _existentName != null) {
		if(_name == _existentName) {
			document.getElementById(_div.concat('DIV2')).innerHTML = '';
			return;
		}
	} else {
		if(_existentName != null) {
			if(_name == _existentName && _orgId == _existentOrgId){
				document.getElementById(_div.concat('DIV2')).innerHTML = '';
				return;
			}
		}
	}
	
	document.getElementById(_div.concat('DIV2')).innerHTML = '<img src=\"images/ajaxLoader/ajax-loader_unicity_snake.gif\"/>';
	setTimeout(function(){},1000);
	interogate(_url, {	
		//Function triggerd on succes
		success:function(o){
		    if (o.responseText == "<p>YES</p>") {
		    	removeMessage(_div);
				document.getElementById(_div.concat('DIV2')).innerHTML = '<font color="red">'.concat(_responseInput.toUpperCase().concat('_ERROR').translate('COMMON')).concat('</font>');
			} else if ("<p>NO</p>" == o.responseText) {
				document.getElementById(_div.concat('DIV2')).innerHTML = '';
			} else if ("<p>ERROR</p>" == o.responseText) {
				document.getElementById(_div.concat('DIV2')).innerHTML = '<font color="red">'.concat(_responseInput.substring(0,0).concat('SERVER_ERROR').translate('COMMON')).concat('</font>');
			}
		}, 
						
		//Function triggerd on failure
		failure:function(o){
			document.getElementById(_div.concat('DIV2')).innerHTML = '';
			alert('Error ! (' + o.status + ' - ' + o.statusText + ')');
		}
	})
	
}

//========================================================================================================
/**
 * It's used on the YAHOO.om.changeRolesPanel.
 * Selects all roles under person's roles select, and 
 * then submits the form.
 *  
 * @author dd
 * @return
 */
function submitChangeRoles(_url, _formId, _container, _panel, _rolesSelectId) {
	var roles = document.getElementById(_rolesSelectId);
	for(var i =0; i < roles.options.length; i++) {
		roles.options[i].selected = 'true';
	}
	submitForm(_url, _formId, _container);
	_panel.destroy();
}

//========================================================================================================
/**
 * It's used on the YAHOO.om.changeDepartmentsPanel.
 * Selects all departments under person's departments select, and 
 * then submits the form.
 * 
 * @author dd
 */
function submitChangeDepartments(_url, _formId, _container, _panel, _departmentsSelectId) {
	var depts = document.getElementById(_departmentsSelectId);
	for(var i =0; i < depts.options.length; i++) {
		depts.options[i].selected = 'selected';
	}
	submitForm(_url, _formId, _container);
	_panel.hide();
}

//========================================================================================================
/**
 * It's used on the YAHOO.om.changeRolesPermissionsPanel.
 * Selects all permissions under role's permissions select, and 
 * then submits the form.
 * 
 * @author alu
 */
function submitChangeRolesPermissions(_url, _formId, _container, _panel, _permissionSelectId) {
	var permissions = document.getElementById(_permissionSelectId);
	for(var i =0; i < permissions.options.length; i++) {
		permissions.options[i].selected = 'selected';
	}
	submitForm(_url, _formId, _container);
	_panel.hide();
}

//========================================================================================================
/**
 * It's used on the YAHOO.om.changePasswordPanel.
 * Submits the form.
 * 
 * @author dd
 * @return
 */
function submitChangePassword(_url, _formId, _container, _panel, _errorMessageDiv) {
	var password = document.getElementById(_formId).password.value;
	var passwordConfirm = document.getElementById(_formId).passwordConfirm.value;
	if (password == "" && passwordConfirm == "") {
		document.getElementById(_errorMessageDiv).innerHTML = 'Empty fields !';
		document.getElementById(_errorMessageDiv).style.display = 'inline';
	}else if (password != passwordConfirm ) {
		document.getElementById(_errorMessageDiv).style.display = 'inline';
	}else {
		submitForm(_url, _formId, _container);
		_panel.hide();
	}
}


//========================================================================================================
/**
 * It's used on the YAHOO.om.changeDepartmentsPanel.
 * Selects all departments under person's departments select, and 
 * then submits the form.
 * 
 * @author dd
 */
function submitUploadPhoto(_url, _formId, _container, _panel) {
	submitForm(_url, _formId, _container);
	_panel.hide();
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
function submitForm(url, formId, divId){
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
		/**  ToDo: De sters daca nu mai e folosit
		if (this.withConfirmation && YAHOO.om.confirmation){
			//The panel it's hard coded as it cannot be
			//passed as a parameter. 
			YAHOO.om.confirmation.hide();
		}
		*/
	} 
	
	var AjaxObject = { 
		handleSuccess:function (o){
			if(o.responseText != undefined){
				sessionExpired(o);
				var div = document.getElementById(divId);
				div.innerHTML = o.responseText;
				YAHOO.om.smallLoading.hide();
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
			}
		}, 
		handleFailure:function (o){
			sessionExpired(o);
			YAHOO.om.smallLoading.hide();
			alert("Error " + o.status + " : " + o.statusText);
	    },
		startRequest:function() {
			//harvest form data ready to send to the server
			var form = document.getElementById(formId);
			YAHOO.util.Connect.setForm(form);
			var el = document.getElementById('departmentForm_observation');		
			//make request
			YAHOO.util.Connect.asyncRequest('POST', makeUniqueRequest(url), callback); 
	    }
	}; 
	var callback = {
		success:AjaxObject.handleSuccess, 
		failure:AjaxObject.handleFailure 
	};
	
	YAHOO.om.smallLoading.show();
	
	//start transaction
	AjaxObject.startRequest();
}

//========================================================================================================
 /**
  * Function for submit form and populate a panel
  *
  * @author mitziuro
  */
 function submitAndShowInBranchPannel(url, formId, panel){
 	
 	var AjaxObject = { 
 		handleSuccess:function (o){
 			if(o.responseText != undefined){
 				sessionExpired(o);
 				panel.setBody(o.responseText);
 				YAHOO.om.smallLoading.hide();
 				
 			}
 		}, 
 		handleFailure:function (o){
 			sessionExpired(o);
 			YAHOO.om.smallLoading.hide();
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
 	
 	YAHOO.om.smallLoading.show();
 	
 	//start transaction
 	AjaxObject.startRequest();
 }
 
//========================================================================================================
 /**
  * Function for submit form with file upload
  * 
  * @author mitziuro
  */
 function submitFormWithFile(url, formId, divId){

	  var AjaxObject = {
			
		//handle upload case. This function will be invoked after file upload is finished.
		handleUpload: function(o) {
			sessionExpired(o);
			if(o.responseText != undefined){
				var div = document.getElementById(divId);
				div.innerHTML = o.responseText;
				YAHOO.om.smallLoading.hide();
				
				//evaluate the script
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
	 		}
		},
			
		startRequest:function() {
			//harvest form data ready to send to the server
			YAHOO.om.smallLoading.show();
			var form = document.getElementById(formId);
			YAHOO.util.Connect.setForm(form, true);
			//make request
			YAHOO.util.Connect.asyncRequest('POST', makeUniqueRequest(url), callback);
		 }

		
	 };
		
	 var callback = {
	 	 	upload:AjaxObject.handleUpload
	 };
	 	
	 //start transaction
	 AjaxObject.startRequest();
	

 }
 
//========================================================================================================
/**
 * Function for submit form and prelucrate the response using JSON
 * 
 * @author matti_joona
 */
function submitFormAndUsingJSON(url, formId, divId, jsonFunction){
	var AjaxObject = { 
		handleSuccess:function (o){
			sessionExpired(o);
			if(o.responseText !== undefined){
				var div = document.getElementById(divId);
				getJSON(div, o.responseText);
				div.innerHTML = o.responseText;
				var scriptTags = div.getElementsByTagName('SCRIPT');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
			}
		}, 
		handleFailure:function (o){
			sessionExpired(o);
			alert("Error " + o.status + " : " + o.statusText);
	    },
	    processResult:function (obj){ 
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
		failure:AjaxObject.handleFailure, 
		scope: AjaxObject
	};
	//start transaction
	AjaxObject.startRequest();
}

//========================================================================================================
/**
 * Function for submit OOO form
 * It sets the firstname and lastname hidden fields values to "" before submittting the form 
 * when the user removes the name input text after a previous search
 *
 * When withContext is true, it means the function is not called with parameters
 * but without, and another Object defines it's context (It's something specific to YUI).
 * 
 * @author coni
 */
function submitOOOForm(url, formId, divId){
	if (this.withContext) {
		url = this.url;
		formId = this.formId;
		divId = this.container;
		}
	if ( YAHOO.util.Dom.get(this.ownerInput).value == "" ){
		YAHOO.util.Dom.get(this.ownerFirstNameHiddenFieldId).value = "";
		YAHOO.util.Dom.get(this.ownerLastNameHiddenFieldId).value = "";		
	}
	if ( YAHOO.util.Dom.get(this.replacementInput).value == "" ){
		YAHOO.util.Dom.get(this.replacementFirstNameHiddenFieldId).value = "";
		YAHOO.util.Dom.get(this.replacementLastNameHiddenFieldId).value = "";		
	}
	submitForm(url, formId, divId);
}
 
//========================================================================================================
 /**
  * Function for submit department form
  * It sets the firstname and lastname hidden fields values to "", and parentDepartment
  * field to -1 before submittting the form 
  * when the user removes the name input text after a previous search
  *
  * When withContext is true, it means the function is not called with parameters
  * but without, and another Object defines it's context (It's something specific to YUI).
  * 
  * @author adelina
  */
 function submitDepartmentForm(url, formId, divId){
 	if (this.withContext) {
 		url = this.url;
 		formId = this.formId;
 		divId = this.container;
 	}
 	if (YAHOO.util.Dom.get(this.managerInput).value == "" ){
 		YAHOO.util.Dom.get(this.managerFirstNameHiddenFieldId).value = "";
 		YAHOO.util.Dom.get(this.managerLastNameHiddenFieldId).value = "";		
 	} 	
 	
 	if (YAHOO.util.Dom.get(this.parentDepartmentInput).value == ""){
 		YAHOO.util.Dom.get(this.parentDepartmentNameHiddenFieldId).value = -1; 		
 	} 	 	 
 	
 	submitForm(url, formId, divId);
 }
  
//========================================================================================================
 /**
  * Function for submit person form
  * It sets the department field values to "" before submittting the form 
  * when the user removes the name input text after a previous search
  *
  * When withContext is true, it means the function is not called with parameters
  * but without, and another Object defines it's context (It's something specific to YUI).
  * 
  * @author adelina
  */
 function submitPersonForm(url, formId, divId){
 	if (this.withContext) {
 		url = this.url;
 		formId = this.formId;
 		divId = this.container;
 	}
 	
 	if (YAHOO.util.Dom.get(this.departmentInput).value == ""){
 		YAHOO.util.Dom.get(this.departmentHiddenFieldId).value = 0; 		
 	} 	
 	
 	submitForm(url, formId, divId);
 }
  
//========================================================================================================
/**
 * Used for parsing JSON info from server
 * 
 * @author matti_joona
 */
function getJSON(div, response){
	//used in case of changing manager for a department
	var returnedInfo = YAHOO.lang.JSON.parse(response);
	if(div.id == "MANAGER_CONTENT"){
		
	}
}

//========================================================================================================
/**
 * Function for submit module form
 * 
 * @author matti_joona
 */
function submitModuleForm(formId) {
	document.getElementById(formId).submit();
}
 
//========================================================================================================
/**
 * Adds the fade effect on a certain div.
 * The effects takes place in duration ms.
 * 
 * @author dd
 */
function addFadeEffectOnBox(boxId, duration) {
//	var box = $(boxId);
//	if (box != null) {
//		box.style.display="block";
//		var fx = box.effects({duration: duration, transition: Fx.Transitions.Quart.easeOut});
//		fx.start({	
//		}).chain(function() {
//			this.start.delay(5000, this, {'opacity': 0});
//		}).chain(function() {
//			box.style.display="none";
//			this.start.delay(0001, this, {'opacity': 1});
//			box.innerHTML = "";
//		});
//	}
	var mainDivElement = document.getElementById(boxId);
	var handleAnimEnd = function() {
		mainDivElement.innerHTML = "";
	}
	
	var fadeOutAnim = new YAHOO.util.Anim(mainDivElement, { opacity: {to: 0} }, duration );
	
	fadeOutAnim.onComplete.subscribe(handleAnimEnd);
	fadeOutAnim.animate();
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
 * Function for submit job panel from form
 * @author mitziuro
 */
function submitAndValidateJobPanelForm(_url, _formId, _divId){
	
	var url = (this.url != null ? this.url : _url),
	formId = (this.formId != null ? this.formId : _formId),
	divId = (this.container != null ? this.container : _divId);
		
	//we get manual the handleChooseJobInDepartment;
	var handleChooseJobInDepartment = this.handleChooseJobInDepartment;
	
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
				YAHOO.om.smallLoading.hide();
				
				//we run the scripts
				var scriptTags = div.getElementsByTagName('script');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				
				//we get the name and the id for adding
				var name = document.getElementById('JOBFORM_NAME').value;
				var id = document.getElementById('JOBFORM_ID').value;
				
				//if the job was added we have to put it in ALL_AVAILABLE_JOBS_LIST
				if(id > 0){
					handleChooseJobInDepartment.addGeneralJob(id, name);
					handleChooseJobInDepartment.displayPersonJobs();
				}
			}
		}, 
		handleFailure:function (o){
			sessionExpired(o);
			YAHOO.om.smallLoading.hide();
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
	
	YAHOO.om.smallLoading.show();
	
	//start transaction
	AjaxObject.startRequest();
	
	//closing the panel
	destroyAddJobPanel();
}

//=============================================== END OF cs_formValidateAndSubmit.js =====================