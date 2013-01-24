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
 * 
*/

//========================================================================================================
/**
 * JAVASCRIPT BUSINESS ORIENTED METHODS
 */


//========================================================================================================

function testFunction(_url, _divId, _panelTitle) {
	alert('test function');
	var url = (this.url != null ? this.url : _url),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),
		divId = (this.container != null ? this.container : _divId);
	alert('instiating the panel');
	// instantiating Person Panel 
	if (!YAHOO.om.testPanel) {
		alert('YAHOO.om.testPanel nu exista. Il creez.');
		YAHOO.om.testPanel = new YAHOO.widget.Panel("TestPanel", { width:"450px", visible:false, constraintoviewport:true, modal: true, close: true,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );
	} else {
		alert('Exista deja !');
	}
	
//	YAHOO.om.testPanel.hideEvent.subscribe(function() {
//		YAHOO.om.testPanel.hide();
		//YAHOO.om.personPanel.setBody('');
		//refreshing the page with Persons
//	});

	YAHOO.om.testPanel.setHeader('<span>' + panelTitle + '</span>');
	// retrieves Panel content
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.testPanel);
}

//========================================================================================================
/**
 * Displays a panel on Person form, for changing Person's Roles.
 * @author dd
 */


//========================================================================================================
/**
 * Displays a panel on Person form, for changing Person's Departments.
 * 
 * @author dd 
 */


//========================================================================================================
/**
 * Dispalys a panel on Person form, for changing Person's Password.
 * 
 * @author dd 
 */
 function changePersonPassword() {
		
	 function handleSubmit() {
		var password = document.getElementById('changePasswordForm').password.value;
		var passwordConfirm = document.getElementById('changePasswordForm').passwordConfirm.value;
		if (password == "" && passwordConfirm == "") {
			document.getElementById('change_password_error_message_div').innerHTML = 'Empty fields !';
			document.getElementById('change_password_error_message_div').style.display = 'inline';
		}else if (password != passwordConfirm ) {
			document.getElementById('change_password_error_message_div').style.display = 'inline';
		}else {
			submitForm('PersonForm.htm?ACTION=UPDATE_PASSWORD', 'changePasswordForm', 'LOCAL_MESSAGES_CONTENT');
			YAHOO.om.changePasswordPanel.hide();
		}
		 
	 }
	 
	 function handleCancel() {
		 YAHOO.om.changePasswordPanel.hide(); 
	 }
	 
	if (!YAHOO.om.changePasswordPanel) {
		YAHOO.om.changePasswordPanel = new YAHOO.widget.Dialog("ChangePersonsPasswordPanel", { width:"300px", fixedcenter:true, visible:false, constraintoviewport:true, modal: true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},
			buttons : [ { text:this.submitButtonLabel, handler: handleSubmit},
			            { text:this.cancelButtonLabel, handler:handleCancel}]
		});
	}
	YAHOO.om.changePasswordPanel.setHeader('<span>' + this.panelTitle + '</span>');
	returnContentFromUrlToPanelAndShowPannel(this.url,YAHOO.om.changePasswordPanel);
}

//========================================================================================================
 /**
  * Dispalys a panel on Person form, for changing Person's Password.
  * 
  * @author Adelina
  */
  function changePersonMyProfilePassword() {
 		
 	 function handleSubmit() {
 		var password = document.getElementById('changePasswordForm').password.value;
 		var passwordConfirm = document.getElementById('changePasswordForm').passwordConfirm.value;
 		if (password == "" && passwordConfirm == "") {
 			document.getElementById('change_password_error_message_div').innerHTML = 'Empty fields !';
 			document.getElementById('change_password_error_message_div').style.display = 'inline';
 		}else if (password != passwordConfirm ) {
 			document.getElementById('change_password_error_message_div').style.display = 'inline';
 		}else {
 			submitForm('PersonMyProfile.htm?ACTION=UPDATE_PASSWORD', 'changePasswordForm', 'LOCAL_MESSAGES_CONTENT');
 			YAHOO.om.changePasswordPanel.hide();
 		}
 		 
 	 }
 	 
 	 function handleCancel() {
 		 YAHOO.om.changePasswordPanel.hide(); 
 	 }
 	 
 	if (!YAHOO.om.changePasswordPanel) {
 		YAHOO.om.changePasswordPanel = new YAHOO.widget.Dialog("ChangePersonsPasswordPanel", { width:"300px", fixedcenter:true, visible:false, constraintoviewport:true, modal: true, close: false,
 			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},
 			buttons : [ { text:this.submitButtonLabel, handler: handleSubmit},
 			            { text:this.cancelButtonLabel, handler:handleCancel}]
 		});
 	}
 	YAHOO.om.changePasswordPanel.setHeader('<span>' + this.panelTitle + '</span>');
 	returnContentFromUrlToPanelAndShowPannel(this.url,YAHOO.om.changePasswordPanel);
 }

//========================================================================================================
 /**
  * Dispalys a panel on Person form, to reset Person's Password.
  * 
  * @author Adelina
  */
  function resetPersonPassword() {
 			
 	 function handleSubmit() {	 		
 		submitForm('PersonForm.htm?ACTION=RESET_PASSWORD',"personForm", 'LOCAL_MESSAGES_CONTENT');
		YAHOO.om.resetPasswordPanel.hide();
 		 
 	 }
 
 	 function handleCancel() { 		
 		 YAHOO.om.resetPasswordPanel.hide(); 
 	 }
 	 
 	if (!YAHOO.om.resetPasswordPanel) { 		
 		YAHOO.om.resetPasswordPanel = new YAHOO.widget.SimpleDialog("ResetPersonPasswordPanel", { 
 			width:"400px", visible:false, constraintoviewport:true, modal: true,
 			close: false,
 		    fixedcenter: true, 
 		    icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
 			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},
 			buttons : [ { text:this.submitButtonLabel, handler: handleSubmit},
 			            { text:this.cancelButtonLabel, handler: handleCancel}]
 		});
 	}
 	YAHOO.om.resetPasswordPanel.setHeader('<span>' + this.panelTitle + '</span>');
 	YAHOO.om.resetPasswordPanel.setBody(this.confirmationMessage);
 	YAHOO.om.resetPasswordPanel.render(document.body);
 	YAHOO.om.resetPasswordPanel.show();
// 	returnContentFromUrlToPanelAndShowPannel(this.url, YAHOO.om.resetPasswordPanel);
 }
 
   /**
    * Dispalys a panel on Person listing, to reset Admin's Password.
    * 
    * @author Adelina
    */
    function resetAdminPassword(_person) {
   		
 	 var person = (this.person != null ? this.person : _person);
   	 
 	 function handleSubmit() {	
   		submitForm('PersonSearch.htm?action=RESET_PASSWORD&personId=' + person ,"paginationForm", 'LOCAL_MESSAGES_CONTENT');
  		YAHOO.om.resetPasswordPanel.hide();
   		 
   	 }
   	 
   	 function handleCancel() {
   		 YAHOO.om.resetPasswordPanel.hide(); 
   	 }
   	 
   	if (!YAHOO.om.resetPasswordPanel) {
   		YAHOO.om.resetPasswordPanel = new YAHOO.widget.SimpleDialog("ResetPersonPasswordPanel", { 
   			width:"400px", visible:false, constraintoviewport:true, modal: true,
   			close: false,
   		    fixedcenter: true, 
   		    icon: YAHOO.widget.SimpleDialog.ICON_HELP, 
   			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},
   			buttons : [ { text:this.submitButtonLabel, handler: handleSubmit},
   			            { text:this.cancelButtonLabel, handler: handleCancel}]
   		});
   	}
   	YAHOO.om.resetPasswordPanel.setHeader('<span>' + this.panelTitle + '</span>');
   	YAHOO.om.resetPasswordPanel.setBody(this.confirmationMessage);
   	YAHOO.om.resetPasswordPanel.render(document.body);
   	YAHOO.om.resetPasswordPanel.show();
   	//returnContentFromUrlToPanelAndShowPannel(this.url, YAHOO.om.resetPasswordPanel);
   }


//========================================================================================================
/**
 * Dispalys a panel on Person form, for changing Person's Departments.
 * 
 * @author dd 
 */
function changePersonsPhoto(_url, _panelTitle) {
	
	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	if (!YAHOO.om.changePersonsPhotoPanel) {
		YAHOO.om.changePersonsPhotoPanel = new YAHOO.widget.Panel("ChangePersonsPhoto", { width:"300px", visible:false, fixedcenter:true, modal: true, constraintoviewport:true, close: true,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );
	}
	YAHOO.om.changePersonsPhotoPanel.setHeader('<span>' + panelTitle + '</span>');
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.changePersonsPhotoPanel);
}

//========================================================================================================
/**
 * Makes that the PersonForm to display the recent uploade Person's Photo
 * @author dd
 */
function applyChangePersonsPhoto(_url, _container, _pictureId) {
	YAHOO.om.changePersonsPhotoPanel.hide();
	getContentFromUrlDirect(_url, _container);
/*	var pictureIdElement = document.getElementById('pictureId');
	pictureIdElement.value = _pictureId;
	alert(_pictureId);
	alert(pictureIdElement.value);
**/}

//============================================================================================================

/**
 * @Destroys the branches Panel
 * @author mitziuro
 */
function destroyBranchPanel(){
	
	if (YAHOO.om.showBranches != null) {
		YAHOO.om.showBranches.destroy();
		YAHOO.om.showBranches = null;
	}
	
	/* Now we populate the dynamic fields of the form */
	
	//we alredy know the ids of the required fields
	var _formId ='searchForm';
	var _name = 'organisationId';
	var _container = null;
	
	//case personSearchForm
	if(document.getElementById('myDepartmentAutoComplete') != null){
		_container = 'myDepartmentAutoComplete';
	}
	
	// case departmentSearchForm
	if(document.getElementById('myParentDepartmentAutoComplete') != null) {
		_container = 'myParentDepartmentAutoComplete';
	}
	
	//case RoleSearchForm
	if(document.getElementById('MODULES') != null){
		_container = 'MODULES';
	}
	
	elm = document.getElementById(_name);
	
	//setting the nomenclator
	setNomenclators(elm.value, _container);
	
}

//===================================================================================================================

/**
 * @Used to populate an element from the form 
 * @author mitziuro
 */
function setNomenclators(_orgId, _container){
	
	switch(_container){
	case 'myDepartmentAutoComplete': {
		//reseting the department select
		getContentFromUrlDirect('PersonSearchGetDepartmentsSelect.htm?organisationId='.concat(_orgId), _container);
		break;
		}
	case 'MODULES': {
		//reseting the modules select
		getContentFromUrlDirect('RoleGetModulesForOrganisation.htm?org='.concat(_orgId), _container);
		break;
		}
	case 'myParentDepartmentAutoComplete': {
		// reseting the parent department select	
		getContentFromUrlDirect('DepartmentGetParentDepartments.htm?organisationId='.concat(_orgId), _container);
		break;
		}
	}	
	
}

//===================================================================================================================

/**
 * 
 * @Displays all branches for an organisation in a YUI panel
 * @author mitziuro
 */
function manageBranches(_url, _panelTitle, _div, _panel_position_x, _panel_position_y, _sessionOrgId, _sessionOrgName){
 
	var url = (this.url != null ? this.url : _url),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),
		div = (this.div != null ? this.div : _div),
		sessionOrgId = (this.sessionOrgId != null ? this. sessionOrgId : _sessionOrgId),
		sessionOrgName = (this.sessionOrgName != null ? this. sessionOrgName : _sessionOrgName),
		panel_position_x = (this.panel_position_x != null ? this.panel_position_x : _panel_position_x),
		panel_position_y = (this.panel_position_y != null ? this.panel_position_y : _panel_position_y);
		
	
	var parent = useBranch("get", 'organisationId', null, false);
	
	//destroy what is left
	if (YAHOO.om.showBranches != null) {
		YAHOO.om.showBranches.destroy();
		YAHOO.om.showBranches = null;
	}
	
	//creating the panel if it isn't there
	if(YAHOO.om.showBranches == null){
		
		YAHOO.om.showBranches = new YAHOO.widget.Panel("BranchPanel", {width: "330px", x: panel_position_x, y: panel_position_y + 25, modal: true, visible: false, autofillheight: "body", overflow: "auto", visible:false, constraintoviewport: true, close: true,
			draggable: true, effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
		YAHOO.om.showBranches.setHeader('<span>' + panelTitle + '</span>');	
	}
	
	//Because responseText in IE, Safari and Chrome it's not working I have to render manual
	returnContentFromUrlToBranchPanelAndShowBranchPannel(url, YAHOO.om.showBranches, sessionOrgId, sessionOrgName);
	
}

 
//==============================================================================================================================

/**
 * 
 * @author mitziuro
 * Getting and setting the parameters from formId for branches select
 */
function useBranch(_method, _property, _value){
	
	//Setting manual _formId because it's always the same
	var _formId = 'searchForm';
	
	var parent = document.getElementById(_property);
	
	if(_method == "get") {
		return parent.value;
	} else {
		parent.value = _value;
		return _value;
	}
	
}

//==============================================================================================================================
/**
 * Displays a panel on Calendar form, for adding and editing a freeday 
 * @param _url The url from where panel's content is retrieved
 * @param _panelTitle Panel't title
 * @author Adelina 
 */
 
function manageFreeday(_url, _panelTitle){

	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	
	if (YAHOO.om.showFreedayPanel != null) {
		YAHOO.om.showFreedayPanel.destroy();
	}
	
		YAHOO.om.showFreedayPanel = new YAHOO.widget.Panel("FreedayPanel", {xy:[300, 200], width:"340px", visible:false, modal: true, constraintoviewport:true, close: true, draggable:true, 
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
		YAHOO.om.showFreedayPanel.setHeader('<span>' + panelTitle + '</span>');
		YAHOO.om.showFreedayPanel.render(document.body); 
		returnContentFromUrlToPanelAndShowPannel(url, YAHOO.om.showFreedayPanel);
		
}

//==============================================================================================================================
/**
 * Hides the freeday panel
 *  
 * @author Adelina 
 */
 
function hideFreedayPanel(){

	if (YAHOO.om.showFreedayPanel != null) {
		YAHOO.om.showFreedayPanel.hide();		
	}
}

// ==================================================================================================================================
 function manageOOOPeriodTimeHours(){	 	 
	 var hour = document.getElementById('hour');	 
	 return hour.value;
 }
 
function manageOOOPeriodTimeMinutes(){	 	 
	 var minutes = document.getElementById('minutes');
	 return minutes.value;
}

//=======================================================================================================================================
function showCalendarStartWorkTimePanel(_url, _panelTitle){
	
	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);	
	
	if (!YAHOO.om.showCalendarStartWorkTimePanel) {
	YAHOO.om.showCalendarStartWorkTimePanel = new YAHOO.widget.Panel("showCalendarStartWorkTimePanel", { xy:[438, 200], width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	}
	YAHOO.om.showCalendarStartWorkTimePanel.setHeader('<span>' + panelTitle + '</span>');	
	
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.showCalendarStartWorkTimePanel);	
}

//=======================================================================================================================================
function showCalendarEndWorkTimePanel(_url, _panelTitle){
	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	if (!YAHOO.om.showCalendarEndWorkTimePanel) {
		YAHOO.om.showCalendarEndWorkTimePanel = new YAHOO.widget.Panel("CalendarEndWorkTimePanel", { xy:[438, 200], width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );
	}
	YAHOO.om.showCalendarEndWorkTimePanel.setHeader('<span>' + panelTitle + '</span>');	
	
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.showCalendarEndWorkTimePanel);
}

//========================================================================================================
function hideCalendarStartWorkTimePanel(){
	var startInput = document.getElementById('calendarForm_startwork');
	var startHour = document.getElementById('hour');	
	var startMinutes = document.getElementById('minutes');
	startInput.value = startInput.value + ' ' + startHour.value + ':' + startMinutes.value;	
	
	YAHOO.om.showCalendarStartWorkTimePanel.hide();
}

//========================================================================================================
function hideCalendarEndWorkTimePanel(){
	var startInput = document.getElementById('calendarForm_endwork');
	var startHour = document.getElementById('hour');
	var startMinutes = document.getElementById('minutes');
	startInput.value = startInput.value + ' ' + startHour.value + ':' + startMinutes.value;	
	
	YAHOO.om.showCalendarEndWorkTimePanel.hide();
}

//=======================================================================================================================================
function showOOOStartPeriodTimePanel(_url, _panelTitle){

	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	if (!YAHOO.om.showOOOStartPeriodTimePanel) {
		YAHOO.om.showOOOStartPeriodTimePanel = new YAHOO.widget.Panel("OOOStartPeriodTimePanel", { xy:[510, 257], width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );
	}
	YAHOO.om.showOOOStartPeriodTimePanel.setHeader('<span>' + panelTitle + '</span>');
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.showOOOStartPeriodTimePanel);
}


//========================================================================================================
function hideOOOStartPeriodTimePanel(){
	var startInput = document.getElementById('oooForm_startPeriod');
	var startHour = document.getElementById('hour');
	var startMinutes = document.getElementById('minutes');
	startInput.value = startInput.value + ' ' + startHour.value + ':' + startMinutes.value;	
	
	YAHOO.om.showOOOStartPeriodTimePanel.hide();
}


//========================================================================================================
function showOOOEndPeriodTimePanel(_url, _panelTitle){
	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	if (!YAHOO.om.showOOOEndPeriodTimePanel) {
		YAHOO.om.showOOOEndPeriodTimePanel = new YAHOO.widget.Panel("OOOEndPeriodTimePanel", {xy:[510, 283], width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );
	}
	YAHOO.om.showOOOEndPeriodTimePanel.setHeader('<span>' + panelTitle + '</span>');	
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.showOOOEndPeriodTimePanel);
}


//========================================================================================================
function hideOOOEndPeriodTimePanel(){
	var endInput = document.getElementById('oooForm_endPeriod');
	var endHour = document.getElementById('hour');
	var endMinutes = document.getElementById('minutes');
	endInput.value = endInput.value + ' ' + endHour.value + ':' + endMinutes.value;		
	
	YAHOO.om.showOOOEndPeriodTimePanel.hide();
}

// Search --> calendar function
//========================================================================================================
function showOOOSearchStartPeriodTimePanel(_url, _panelTitle){

	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	if (!YAHOO.om.showOOOSearchStartPeriodTimePanel) {
		YAHOO.om.showOOOSearchStartPeriodTimePanel = new YAHOO.widget.Panel("OOOSearchStartPeriodTimePanel", {xy: [580, 205] , width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );
	}
	YAHOO.om.showOOOSearchStartPeriodTimePanel.setHeader('<span>' + panelTitle + '</span>');
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.showOOOSearchStartPeriodTimePanel);
}


//========================================================================================================
function hideOOOSearchStartPeriodTimePanel(){	
	var startInput = document.getElementById('oooSearchForm_startPeriod');
	var hour = document.getElementById('hour');
	var minutes = document.getElementById('minutes');
	startInput.value = startInput.value + ' ' + hour.value + ':' + minutes.value;
	YAHOO.om.showOOOSearchStartPeriodTimePanel.hide();
}


//========================================================================================================
function showOOOSearchEndPeriodTimePanel(_url, _panelTitle){
	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle);
	
	if (!YAHOO.om.showOOOSearchEndPeriodTimePanel) {
		YAHOO.om.showOOOSearchEndPeriodTimePanel = new YAHOO.widget.Panel("OOOSearchEndPeriodTimePanel", {xy: [580, 225], width:"180px", visible:false, modal: true, constraintoviewport:true, close: false,
			effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );
	}
	YAHOO.om.showOOOSearchEndPeriodTimePanel.setHeader('<span>' + panelTitle + '</span>');
	returnContentFromUrlToPanelAndShowPannel(url,YAHOO.om.showOOOSearchEndPeriodTimePanel);
}


//========================================================================================================
function hideOOOSearchEndPeriodTimePanel(){
	var endInput = document.getElementById('oooSearchForm_endPeriod');
	var hour = document.getElementById('hour');
	var minutes = document.getElementById('minutes');
	endInput.value = endInput.value + ' ' + hour.value + ':' + minutes.value;
	YAHOO.om.showOOOSearchEndPeriodTimePanel.hide();
}

//========================================================================================================
/**
 * With options groups !
 * 
 * Moves options form Source Select to Destination Select. The to selects has to be identical:
 * 		- same optgroup id and label
 * 		- same option id and label
 * 
 * Optroups are required !	 
 * 
 * @author dd
 * 
 * @param sourceSelectId Id of the source select
 * @param destSelectBId  Id of the destination select
 * @param isAdminIT  If false the function will handle the admin case, else the adminIT
 * @param remove If false  the function will handle the Add case, else the Remove
 * @return
 */
function moveSelectOptionsOptGroupSourceDest(sourceSelectId, destSelectBId, remove, isAdminIT){	
	
	var i;	
	// Source select
	var sourceSelect = document.getElementById(sourceSelectId);
	// Destination select
	var destSelect = document.getElementById(destSelectBId);
	// HashMap with all options from destination select
	var destSelectHM = new Array();
	for(i = 0; i < destSelect.options.length; i++) {
		destSelectHM[destSelect.options[i].value] = destSelect.options[i].parentNode;
	} 
	var destSelectOptGroupsHM = new Array();
	for(i = 0; i < destSelect.getElementsByTagName('optgroup').length; i++) {
		destSelectOptGroupsHM[destSelect.getElementsByTagName('optgroup')[i].id] = destSelect.getElementsByTagName('optgroup')[i];
	}
	// A temporary Array for storing the user selection from sourceSelect
	var selected = new Array();
	for (i = 0; i < sourceSelect.options.length; i++) 
		if (sourceSelect.options[i].selected) { 
			selected.push({optGroup: sourceSelect.options[i].parentNode, elem: sourceSelect.options[i] });
		}
	// Removing from source no matter if the options will be added or not in the destination select !
	if(remove){
		for(i = 0; i < selected.length; i++) {			
			if(selected[i].elem.innerHTML != "OM_USER"){
				if(!isAdminIT){
					if(selected[i].elem.innerHTML != "OM_ADMIN") {
						selected[i].optGroup.removeChild(selected[i].elem);
					}
				} else {
					selected[i].optGroup.removeChild(selected[i].elem);
				}
			} 			
		}
	}
		
	var newOption;
	var optGroup;
	// For every option selected
	for(i = 0; i < selected.length; i++) {		
		if(selected[i].elem.innerHTML != "OM_USER"){
			if(!isAdminIT) {
				if(selected[i].elem.innerHTML != "OM_ADMIN") {
					if (destSelectHM.length > 0) {
						if (destSelectHM[selected[i].elem.value] == null) {
							// Get option group under which to add the option element
							optGroup = destSelectOptGroupsHM[selected[i].optGroup.id];
							// Create new option
							newOption = document.createElement('option');
							newOption.id = selected[i].elem.id;
							newOption.value = selected[i].elem.value;
							newOption.innerHTML = selected[i].elem.innerHTML;
							optGroup.appendChild(newOption);
							if (!remove){					
								selected[i].optGroup.removeChild(selected[i].elem);				
							}
								
						} else{
							// already present
						}
					}else {
						// Get option group under which to add the option element
						optGroup = destSelectOptGroupsHM[selected[i].optGroup.id];
						// Create new option
						newOption = document.createElement('option');
						newOption.id = selected[i].elem.id;
						newOption.value = selected[i].elem.value;
						newOption.innerHTML = selected[i].elem.innerHTML;
						optGroup.appendChild(newOption);
						if (!remove){
							selected[i].optGroup.removeChild(selected[i].elem);				
						}
					}
				}
			} else if (destSelectHM.length > 0) {			
				if (destSelectHM[selected[i].elem.value] == null) {
					// Get option group under which to add the option element
					optGroup = destSelectOptGroupsHM[selected[i].optGroup.id];
					// Create new option
					newOption = document.createElement('option');
					newOption.id = selected[i].elem.id;
					newOption.value = selected[i].elem.value;
					newOption.innerHTML = selected[i].elem.innerHTML;
					optGroup.appendChild(newOption);
					if (!remove){					
						selected[i].optGroup.removeChild(selected[i].elem);				
					}
						
				} else{
					// already present
				}
			}else {
				// Get option group under which to add the option element
				optGroup = destSelectOptGroupsHM[selected[i].optGroup.id];
				// Create new option
				newOption = document.createElement('option');
				newOption.id = selected[i].elem.id;
				newOption.value = selected[i].elem.value;
				newOption.innerHTML = selected[i].elem.innerHTML;
				optGroup.appendChild(newOption);
				if (!remove){
					selected[i].optGroup.removeChild(selected[i].elem);				
				}
			}		
		}
	}
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
function moveSelectOptionsSourceDest(sourceSelectId, destSelectBId, remove){
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
			if(selected[i].innerHTML != "Organization Management"){			
				if(selected[i].innerHTML != "OM_Basic"){	
					if(selected[i].innerHTML != "DM_Basic"){	
						sourceSelect.removeChild(selected[i]);
					}
				}				
			}			
		}
	}	
	
	var newOption;
	// For every option selected
	for(i = 0; i < selected.length; i++) {
		if(selected[i].innerHTML != "Organization Management") {
			if(selected[i].innerHTML != "OM_Basic") {
				if(selected[i].innerHTML != "DM_Basic") {
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
}
	    
//========================================================================================================

/**
 * Loads one Organization's Organigram
 * @author dan.damian
 */	    
function loadOrganigram(organisationId, container) {
	getContentFromUrlDirect("OrganisationViewHTMLOrganigram.htm?id=" + organisationId, container);
}    

//========================================================================================================
function loadNodeData(node, fnLoadComplete)  {    	
	//We'll load node data based on what we get back when we
    //use Connection Manager topass the text label of the 
    //expanding node to the Yahoo!
    //Search "related suggestions" API.  Here, we're at the 
    //first part of the request -- we'll make the request to the
    //server.  In our success handler, we'll build our new children
    //and then return fnLoadComplete back to the tree.
    
    //Get the node's label and urlencode it; this is the word/s
    //on which we'll search for related words:
    var nodeLabel = encodeURI(node.labelElId);
    //node types
    var companyType = 1;
    var groupCompaniesType = 2;
    var hqType = 3;
    var branchType = 4;
    var personType = 5;
    var departmentType = 6;
    
    var enabledOrgStatus = 1;
    var disabledOrgStatus = 0;
    
    var actionDelete = 2;
    var actionEnable = 1;
    var actionDisable = 0; 
    
    //prepare URL for XHR request:
    var sUrl = "OrganisationTreeGetNode.htm?" + nodeLabel;
    
    //prepare our callback object
    var callback = {
    
        //if our XHR call is successful, we want to make use
        //of the returned data and create child nodes.
        success: function(oResponse) {
        	sessionExpired(oResponse);
            YAHOO.log("XHR transaction was successful.", "info", "example");
            //YAHOO.log(oResponse.responseText);
            var oResults = eval("(" + oResponse.responseText + ")");
            //Nodes
            if((oResults.ResultSet.Nodes) && (oResults.ResultSet.Nodes.length)) {
                    //Result is an array if more than one result, string otherwise
                if(YAHOO.lang.isArray(oResults.ResultSet.Nodes)) {
                    for (var i=0, j=oResults.ResultSet.Nodes.length; i<j; i++) {
                    	//if the node is an organization
 						if (oResults.ResultSet.Nodes[i].type == companyType || oResults.ResultSet.Nodes[i].type == groupCompaniesType || oResults.ResultSet.Nodes[i].type == hqType || oResults.ResultSet.Nodes[i].type == branchType){
							var propertiesObject = {href: "#", organizationStatus: oResults.ResultSet.Nodes[i].organizationStatus, deleteChildOrg: node.data.deleteChildOrg, updateChildOrgStatus: node.data.updateChildOrgStatus,
							disableButtonTooltip: node.data.disableButtonTooltip, enableButtonTooltip: node.data.enableButtonTooltip, deleteButtonTooltip: node.data.deleteButtonTooltip, nodeTooltip: node.data.nodeTooltip};
							var tempNode = new YAHOO.widget.TextNode(propertiesObject, node, false);							
							//html to display actions available for every node of type organization: delete and enable/disable
							
							//If the user has the permission to delete a child org, the delete action button will be displayed
   							if (tempNode.data.deleteChildOrg){
   								var deleteAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDelete + ", " + "\'" + oResults.ResultSet.Nodes[i].labelElId + "\'" + ", " + tempNode.index + ", " + "\'" + tempNode.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\"><img src=\"././images/buttons/action_delete.png\" title=\"" + tempNode.data.deleteButtonTooltip + "\"\"></a>";
   							} else {
   								var deleteAction = "";
   							}
   							//If the user has the permission to update a child org, the update action button will be displayed
   							if (tempNode.data.updateChildOrgStatus) {
   								//I have to display the proper action depending on the organization status
   								if (oResults.ResultSet.Nodes[i].organizationStatus == enabledOrgStatus){
   									var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDisable + ", " + "\'" + oResults.ResultSet.Nodes[i].labelElId + "\'" + ", " + tempNode.index + ", " + "\'" + tempNode.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\"><img src=\"././images/buttons/action_disable.png\" title=\"" + tempNode.data.disableButtonTooltip + "\"\"></a>";
   								} else {
   									var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionEnable + ", " + "\'" + oResults.ResultSet.Nodes[i].labelElId + "\'" + ", " + tempNode.index + ", " + "\'" + tempNode.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\" ><img src=\"././images/buttons/action_enable.png\" title=\"" + tempNode.data.enableButtonTooltip + "\"\"></a>";
   								}
   							} else {
   								var enableDisableAction = "";
   							}
   							var label = "<span title=\"" + tempNode.data.nodeTooltip + "\">" + oResults.ResultSet.Nodes[i].label + "</span>" + deleteAction + enableDisableAction;
							tempNode.setUpLabel(label);
   							//define the properties object used to instantiate a new node
							tempNode.labelStyle = "organisationNode";
						}
						//if the node is a department
						else if (oResults.ResultSet.Nodes[i].type == departmentType){
							//define the properties object used to instantiate a new node
							var propertiesObject = {href: "#", nodeTooltip: node.data.nodeTooltip};
							var tempNode = new YAHOO.widget.TextNode(propertiesObject, node, false); 
							var label = "<span title=\"" + tempNode.data.nodeTooltip + "\">" + oResults.ResultSet.Nodes[i].label + "</span>";											
							tempNode.setUpLabel(label);
							tempNode.labelStyle = "departmentNode";								
							tempNode.data.departmentId = oResults.ResultSet.Nodes[i].departmentId;
						}
						tempNode.labelElId = oResults.ResultSet.Nodes[i].labelElId;
                        tempNode.isLeaf = oResults.ResultSet.Nodes[i].isLeaf;
                        tempNode.title = oResults.ResultSet.Nodes[i].title;
                    }
                } else {
                    //there is only one result; comes as string:

					if (oResults.ResultSet.Nodes.type == companyType || oResults.ResultSet.Nodes.type == groupCompaniesType || oResults.ResultSet.Nodes.type == hqType || oResults.ResultSet.Nodes.type == branchType){
						var propertiesObject = {href: "#", organizationStatus: oResults.ResultSet.Nodes.organizationStatus, deleteChildOrg: node.data.deleteChildOrg, updateChildOrgStatus: node.data.updateChildOrgStatus, 
						disableButtonTooltip: node.data.disableButtonTooltip, enableButtonTooltip: node.data.enableButtonTooltip, deleteButtonTooltip: node.data.deleteButtonTooltip, nodeTooltip: node.data.nodeTooltip};
						var tempNode = new YAHOO.widget.TextNode(propertiesObject, node, false);	
						//html to display actions available for every node of type organization: delete and enable/disable
						//If the user has the permission to delete a child org, the delete action button will be displayed
   						if (tempNode.data.deleteChildOrg){
   							var deleteAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDelete + ", " + "\'" + oResults.ResultSet.Nodes.labelElId + "\'" +  ", " + tempNode.index +  ", " + "\'" + tempNode.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\"><img src=\"././images/buttons/action_delete.png\" title=\"" + tempNode.data.deleteButtonTooltip + "\"\"></a>";
   						} else {
   							var deleteAction = "";
   						}   							
   						//If the user has the permission to update a child org, the update action button will be displayed
   						if (tempNode.data.updateChildOrgStatus) {
   							//I have to display the proper action depending on the organization status
   							if (oResults.ResultSet.Nodes.organizationStatus == enabledOrgStatus){
   								var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDisable + ", " + "\'" + oResults.ResultSet.Nodes.labelElId + "\'" +  ", " + tempNode.index +  ", " + "\'" + tempNode.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\"><img src=\"././images/buttons/action_disable.png\" title=\"" + tempNode.data.disableButtonTooltip + "\"\"></a>";
   							} else {
   								var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionEnable + ", " + "\'" + oResults.ResultSet.Nodes.labelElId + "\'" +  ", " + tempNode.index +  ", " + "\'" + tempNode.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\" ><img src=\"././images/buttons/action_enable.png\" title=\"" + tempNode.data.enableButtonTooltip + "\"\"></a>";
   							}
   						} else {
   							var enableDisableAction = "";
   						}
   						var label = "<span title=\"" + tempNode.data.nodeTooltip + "\">" + oResults.ResultSet.Nodes.label + "</span>" + deleteAction + enableDisableAction;
						tempNode.setUpLabel(label);
						tempNode.labelStyle = "organisationNode";
					}
					else if (oResults.ResultSet.Nodes.type == departmentType){
						var propertiesObject = {href: "#", nodeTooltip: node.data.nodeTooltip};
						var tempNode = new YAHOO.widget.TextNode(propertiesObject, node, false); 
						var label = "<span title=\"" + tempNode.data.nodeTooltip + "\">" + oResults.ResultSet.Nodes[i].label + "</span>";
						tempNode.setUpLabel(label);
						tempNode.labelStyle = "departmentNode";	
						tempNode.data.departmentId = oResults.ResultSet.Nodes.departmentId;
					}
					tempNode.labelElId = oResults.ResultSet.Nodes.labelElId;
                    tempNode.isLeaf = oResults.ResultSet.Nodes.isLeaf;
                    tempNode.title = oResults.ResultSet.Nodes.title;
                }
            }
            //Employess - the following lines should be used when the organisation tree would include employee
            //if((oResults.ResultSet.Employees) && (oResults.ResultSet.Employees.length)) {
            //        //Result is an array if more than one result, string otherwise
            //    if(YAHOO.lang.isArray(oResults.ResultSet.Employees)) {
            //        for (var i=0, j=oResults.ResultSet.Employees.length; i<j; i++) {
            //            var tempNode = new YAHOO.widget.TextNode(oResults.ResultSet.Employees[i].label, node, false);
            //            tempNode.labelElId = oResults.ResultSet.Employees[i].labelElId;
            //            tempNode.labelStyle="employeeNode";
            //            tempNode.isLeaf = true;
            //        }
            //    } else {
            //        //there is only one result; comes as string:
            //        var tempNode = new YAHOO.widget.TextNode(oResults.ResultSet.Employees.label, node, false)
            //        tempNode.labelElId = oResults.ResultSet.Employees.labelElId;
            //       tempNode.labelStyle="employeeNode";
            //        tempNode.isLeaf = true;
            //   }
            //}
            
            //When we're done creating child nodes, we execute the node's
            //loadComplete callback method which comes in via the argument
            //in the response object (we could also access it at node.loadComplete,
            //if necessary):
            oResponse.argument.fnLoadComplete();
        },
        
        //if our XHR call is not successful, we want to
        //fire the TreeView callback and let the Tree
        //proceed with its business.
        failure: function(oResponse) {
        	sessionExpired(oResponse);
            alert("Failed to process XHR transaction.");
            oResponse.argument.fnLoadComplete();
        },
        
        //our handlers for the XHR response will need the same
        //argument information we got to loadNodeData, so
        //we'll pass those along:
        argument: {
            "node": node,
            "fnLoadComplete": fnLoadComplete
        },
        
        //timeout -- if more than 7 seconds go by, we'll abort
        //the transaction and assume there are no children:
        timeout: 7000
    };
    
    //With our callback object ready, it's now time to 
    //make our XHR call using Connection Manager's
    //asyncRequest method:
    YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(sUrl), callback);
}
//end loadNodeData



/**
 * 	@author coni 
 * 	Used to build the organization tree structure first level nodes and enable dinamic node loading
 */   
  
function buildOrganisationTree(firstLevelNodes, treeDivId, viewChildOrgInfo, deleteChildOrg, updateChildOrgStatus, addChildOrgAdmin, addAdminButtonTooltip, disableButtonTooltip, enableButtonTooltip, deleteButtonTooltip, nodeTooltip, panelTitleDelete, panelTitleDisable, panelTitleEnable) {	
		
	//node types
	var companyType = 1;
    var groupCompaniesType = 2;
    var hqType = 3;
    var branchType = 4;
    var personType = 5;
    var departmentType = 6;
    
    var enabledOrgStatus = 1;
    var disabledOrgStatus = 0;
    
    var actionAddAdmin = 3;
    var actionDelete = 2;
    var actionEnable = 1;
    var actionDisable = 0;    
   //create a new tree:
   tree = new YAHOO.widget.TreeView(treeDivId);
   
   tree.setExpandAnim(YAHOO.widget.TVAnim.FADE_IN);
   tree.setCollapseAnim(YAHOO.widget.TVAnim.FADE_OUT);
   
   var labelElIdArray = new Array();
  
   //turn dynamic loading on for entire tree:
   tree.setDynamicLoad(loadNodeData, 0);
   
   //get root node for tree:
   var root = tree.getRoot();
     
   //add child nodes for tree; our top level nodes are all the organization
   //direct children organizations and its departments
   if ( firstLevelNodes!= "" ){
   		var aStates = eval("(" + firstLevelNodes + ")");
   
   		for (var i=0, j=aStates.length; i<j; i++) {
   			//if the node is a department
   			if (aStates[i].type == departmentType){
   				//define the properties object used to instantiate a new node
   				var propertiesObject = {href: "#", nodeTooltip: nodeTooltip};
				var tempNode = new YAHOO.widget.TextNode(propertiesObject, root, false);   	
				var label = "<span title=\"" + nodeTooltip + "\">" + aStates[i].label + "</span>";   								
				tempNode.labelStyle = "departmentNode";	
				tempNode.setUpLabel(label);
				tempNode.data.departmentId = aStates[i].departmentId;
		
			}	
			//if the node is an organization
			else if (aStates[i].type != personType){
				//-href property set to "#" in order not to expand a node when clicking its label
				//-organizationStatus property used for storing the oreganization node status
				//-deleteChildOrg property used in the loadNodeData function in order to verify if the user has the permission to delete an organization node
				//-updateChildOrgStatus property used in the loadNodeData function in order to verify if the user has the permission to update an organization node status 
				var propertiesObject = {href: "#", organizationStatus: aStates[i].organizationStatus, deleteChildOrg: deleteChildOrg, updateChildOrgStatus: updateChildOrgStatus, 
				disableButtonTooltip: disableButtonTooltip, enableButtonTooltip: enableButtonTooltip, deleteButtonTooltip: deleteButtonTooltip, nodeTooltip: nodeTooltip};
				var tempNode = new YAHOO.widget.TextNode(propertiesObject, root, false);
															   			
   				//html to display actions available for every node of type organization: delete and enable/disable
   				
   				//If the user has the permission to delete a child org, the delete action button will be displayed
   				if (deleteChildOrg){
   					var deleteAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDelete + ", " + "\'" + aStates[i].labelElId + "\'" + ", " + tempNode.index + ", " + "\'" + tree.id + "\'" + ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" +  ")" + "\"><img src=\"././images/buttons/action_delete.png\" title=\"" + deleteButtonTooltip + "\"\"></a>";   								
   				} else {
   					var deleteAction = "";
   				}   				
   				//If the user has the permission to add an admin for the org, the add admin button will be displayed
   				if (addChildOrgAdmin && aStates[i].hasAdmin == false){   					
   					var addAdminAction = "<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionAddAdmin + ", " + "\'" + aStates[i].labelElId + "\'" + ", " + tempNode.index + ", " + "\'" + tree.id + "\'" +  ")" + "\"><img src=\"././images/buttons/action_add_admin.gif\" title=\"" + addAdminButtonTooltip + "\"\"></a>";   					
   				} else {
   					var addAdminAction = "";
   				}
   				
   				//If the user has the permission to update a child org, the update action button will be displayed
   				if (updateChildOrgStatus) {
   				   	//I have to display the proper update action depending on the organization status
   					if (aStates[i].organizationStatus == enabledOrgStatus){
   						var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDisable + ", " + "\'" + aStates[i].labelElId + "\'" + ", " + tempNode.index + ", " + "\'" + tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" +  ")" + "\"><img src=\"././images/buttons/action_disable.png\" title=\"" + disableButtonTooltip + "\"\"></a>";   						
   					} else {
   						var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionEnable + ", " + "\'" + aStates[i].labelElId + "\'" + ", " + tempNode.index + ", " + "\'" + tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" +  ")" + "\" ><img src=\"././images/buttons/action_enable.png\" title=\"" + enableButtonTooltip + "\"\"></a>";   						
   					}
   				} else {
   					var enableDisableAction = "";
   				}
   				//define the properties object used to instantiate a new node
   				var label = "<span title=\"" + nodeTooltip + "\">" + aStates[i].label + "</span>" + deleteAction + enableDisableAction + addAdminAction;   				
				tempNode.setUpLabel(label);
				tempNode.labelStyle = "organisationNode";
			}
   			tempNode.labelElId = aStates[i].labelElId;
			tempNode.isLeaf = aStates[i].isLeaf;
			tempNode.title = aStates[i].title;
			tempNode.hasAdmin = aStates[i].hasAdmin;
			tempNode.orgChildId = aStates[i].orgChildId;					
   		}   		   		
   				
   		//put on event labelClick a listener that calls a function used to display a panel with 
   		//an organization general info when the corresponging tree node is clicked
   		tree.subscribe("labelClick", function(node) { 
	   		  var dots = " ...";
	    	  var numberOfChars = 45;
	    	  var panelHeaderName = "";	    	 
   				
   			  //the info listener should be enabled if the user has the permission to view the suborganizations info and only for organization type nodes 
	          if (node.labelStyle == "organisationNode" && viewChildOrgInfo) {
	          		var nodeLabelElId = encodeURI(node.labelElId);
	          		panelHeaderTitle = node.label.substring(node.label.indexOf(">") + 1, node.label.indexOf("</span>"));
	          		//alert(node.label.substring(node.label.indexOf(">") + 1, node.label.indexOf("</span>")));
	          		 if(panelHeaderTitle.length > numberOfChars) {
			    		  panelHeaderName = panelHeaderTitle.substring(0, numberOfChars).concat(dots);
			    	  } else {
			    		  panelHeaderName = panelHeaderTitle;
			    	  }
	          		displayInfoPanel('OrganisationGeneralInfo.htm?' + nodeLabelElId, panelHeaderName, panelHeaderTitle, '400px');
	          } else if(node.labelStyle == "departmentNode") { 	        		        		        	  
	        	  var nodeLabelElId = encodeURI(node.labelElId);
        		  panelHeaderTitle = node.label.substring(node.label.indexOf(">") + 1, node.label.indexOf("</span>"));
        		  if(panelHeaderTitle.length > numberOfChars) {  		 
	        		  panelHeaderName = panelHeaderTitle.substring(0, numberOfChars).concat(dots);
		    	  } else {
		    		  panelHeaderName = panelHeaderTitle;
		    	  }
	        	  displayInfoPanel('DepartmentView.htm?departmentId=' + node.data.departmentId, panelHeaderName, node.label, '400px');
	          }
	          
	    }); 
   		   		    
	    //render tree with these toplevel nodes; all descendants of these nodes
   		//will be generated as needed by the dynamic loader.
   		tree.draw();   		
   	}
}
//end buildTree

//=======================================================================================================
/**
 * Used to perform actions on organizations directly from the tree structure and update the tree displayed 
 * @author coni
 */ 

function manageOrgActionsFromTree(action, labelElId, nodeIndex, treeId, panelTitleDelete, panelTitleDisable, panelTitleEnable) {
	
	var actionAddAdmin = 3;
	var actionDelete = 2;
	var actionEnable = 1;
	var actionDisable = 0;
	var treeId = "treeDiv1";
	confirmDeleteMessageKey = "SUBMIT_DELETE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE";
	confirmEnableMessageKey = "SUBMIT_ENABLE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE";
	confirmDisableMessageKey = "SUBMIT_DISABLE_ORGANIZATION_FROM_TREE_CONFIRM_MESSAGE";
	//get the node on which an action will be performed
	var node = YAHOO.widget.TreeView.getNode(treeId, nodeIndex);
	//prepare the url for asynchronous reqeust 
	var nodeLabel = encodeURI(labelElId);
	if (action == actionDelete){
		var sUrl = "OrganisationTreeStructure.htm?" + nodeLabel + "&action=delete";
		var confirmMessage = confirmDeleteMessageKey.toString().translate('COMMON');  
		var panelTitle = panelTitleDelete;		
	} else if (action == actionEnable){
		var sUrl = "OrganisationTreeStructure.htm?" + nodeLabel + "&action=enable";
		var confirmMessage = confirmEnableMessageKey.toString().translate('COMMON');
		var panelTitle = panelTitleEnable;		
	} else if (action == actionDisable){
		var sUrl = "OrganisationTreeStructure.htm?" + nodeLabel + "&action=disable";
		var confirmMessage = confirmDisableMessageKey.toString().translate('COMMON');
		var panelTitle = panelTitleDisable;
	} else if (action == actionAddAdmin){			
		getContentFromUrlDirect("CreateAdmin.htm?ACTION=NEWADMIN&ORG=" + parseInt(node.orgChildId), "MAIN_CONTENT");		
		return;
	}		     	
			
	//callback used for the asynchrounous request 
	var callback = {
		success: function(oResponse) {
			sessionExpired(oResponse);
			if (oResponse.responseText != undefined){
				if (action == actionDelete){
					//get the parent node
					var parentNode = node.parent;
					//remove the the deleted node from the tree
					node.tree.removeNode(node, true);
					//if the parent node doesn't have any other children, it must become a leaf 
					if (!parentNode.hasChildren(true)){
						parentNode.isLeaf = "false";
					}
					YAHOO.om.smallLoading.hide();
					YAHOO.om.orgTreeUpdating.hide();
					//I have to render the tree with the new structure
					var tree = YAHOO.widget.TreeView.getTree(treeId);
					tree.draw();
				} else if (action == actionEnable){
					//set up the html for the actions of update/delete organizations
					var deleteAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDelete + ", " + "\'" + labelElId  + "\'" + ", " + node.index + ", " + "\'" + node.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\"><img src=\"././images/buttons/action_delete.png\"></a>"
					var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDisable + ", " + "\'" + labelElId + "\'" + ", " + node.index + ", " + "\'" + node.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\" ><img src=\"././images/buttons/action_disable.png\"></a>"
					//get the organization name from the old label
					var label = node.label.substring(0, node.label.indexOf("&nbsp"));
					//set up the new node label
					node.setUpLabel(label + deleteAction + enableDisableAction);
					//I have to set the labelElId of the updated organization node to the previous one, otherwise after calling setUpLabel function, 
					//the labelElId of the node will be changed
					node.labelElId = labelElId;
					YAHOO.om.smallLoading.hide();
					YAHOO.om.orgTreeUpdating.hide();
					var tree = YAHOO.widget.TreeView.getTree(treeId);
					tree.draw();					
				} else if (action == actionDisable){
					var deleteAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionDelete + ", " + "\'" + labelElId  + "\'" + ", " + node.index + ", " + "\'" + node.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\"><img src=\"././images/buttons/action_delete.png\"></a>"
					var enableDisableAction = "&nbsp<a href=\"#\" onClick=\"manageOrgActionsFromTree(" + actionEnable + ", " + "\'" + labelElId + "\'" + ", " + node.index + ", " + "\'" + node.tree.id + "\'" +  ", " + "\'" + panelTitleDelete  + "\'" + ", " + "\'" + panelTitleDisable  + "\'" + ", " + "\'" + panelTitleEnable  + "\'" + ")" + "\" ><img src=\"././images/buttons/action_enable.png\"></a>"
					//get the organization name from the old label
					var label = node.label.substring(0, node.label.indexOf("&nbsp"));
					node.setUpLabel(label + deleteAction + enableDisableAction);
					node.labelElId = labelElId;
					YAHOO.om.smallLoading.hide();
					YAHOO.om.orgTreeUpdating.hide();
					var tree = YAHOO.widget.TreeView.getTree(treeId);
					tree.draw();					
				}
			}
		},
		failure: function(oResponse) {
			sessionExpired(oResponse);
			alert("Failed to process XHR transaction.");
        }
    };
    
    //function used to handle the case when the user chooses from the confirmation panel not to perform the action on the specific organization
    function handleNo(){
		YAHOO.om.confirmation.hide();
		YAHOO.om.confirmation.destroy();
	}
	
    //function used to handle the case when the user chooses from the confirmation panel to perform the action on the specific organization	
	function handleYes() {
	    // Show Small Loading
		YAHOO.om.smallLoading.show();
		handleNo();
		YAHOO.om.orgTreeUpdating.show();
   	 	YAHOO.util.Connect.asyncRequest('GET', makeUniqueRequest(sUrl), callback);
	}
	
	//create and display the confirmation dialog
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
		                          { text:"YES".translate('COMMON'), handler: handleYes},
		                          { text:"NO".translate('COMMON'),  handler:handleNo } ] 
		             } ); 
		
	YAHOO.om.confirmation.setHeader('<span>' + panelTitle + '</span>');	
	YAHOO.om.confirmation.setBody(confirmMessage);
	YAHOO.om.confirmation.render(document.body);
	YAHOO.om.confirmation.show();
	
}


//==================
function pressButton(i) {
	alert(this.url);
}

//========================================================================================================

/**
 * Moves options form Source Select to Destination Select. The two selects has to be identical:
 * 		- same option id and label
 * 
 * It is a particular implementation for the case when a user has to set on Person
 * Departments. When adding/removing a Department it will also have to set a Job in that department.
 * Having this in mind this function interact also with an object of HandleChooseJobInDepartment type.
 * 
 * @author dan.damian
 * 
 * @param sourceSelectId Id of the source select
 * @param destSelectBId  Id of the destination select
 * @param remove If false  the function will handle the Add case, else the Remove
 * @return
 */
function moveSelectOptionsSourceDestDepartment(sourceSelectId, destSelectBId, remove, handleChooseJobInDepartment){
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
				sourceSelect.removeChild(selected[i]);
			}
		}
		var departmentsAdded =  new Array();
		var j = 0;
		
		var newOption;
		// For every option selected
		for(i = 0; i < selected.length; i++) {
			if (destSelectHM.length > 0) {
				if (destSelectHM[selected[i].value] == null) {
					// Create new option
					newOption = document.createElement('option');
					newOption.id = selected[i].id;
					newOption.value = selected[i].value;
					newOption.innerHTML = selected[i].innerHTML;
					destSelect.appendChild(newOption);
					
					//For interacting with jobs
					departmentsAdded[j++] = {id: selected[i].id, name: selected[i].innerHTML};
					
					if (!remove) sourceSelect.removeChild(selected[i]);
				} else{
					// already present
					if(remove){
						departmentsAdded[j++] = {id: selected[i].id, name: selected[i].innerHTML};
					} else {
						sourceSelect.removeChild(selected[i]);
					}
				}
			} else {
				// Create new option
				newOption = document.createElement('option');
				newOption.id = selected[i].id;
				newOption.value = selected[i].value;
				newOption.innerHTML = selected[i].innerHTML;
				destSelect.appendChild(newOption);
				
				//For interacting with jobs
				departmentsAdded[j++] = {id: selected[i].id, name: selected[i].innerHTML};
				
				if (!remove) sourceSelect.removeChild(selected[i]);
			}
		}
		
		//Synchronizing with Person's List of Jobs
		for(var l = 0; l < departmentsAdded.length; l++) {
			if (!remove) {
				handleChooseJobInDepartment.addJob(departmentsAdded[l].id, departmentsAdded[l].name);
			} else {
				handleChooseJobInDepartment.removeJob(departmentsAdded[l].id);
			}
		}
		
		//Refreshing Person's List of Jobs
		handleChooseJobInDepartment.displayPersonJobs();
		
		
	}
 

//========================================================================================================

/**
 * Moves options form Source Select to Destination Select. The two selects has to be identical:
 * 		- same option id and label
 * 
 * It is a particular implementation for the case when a user has to set on a Department
 * Persons. When adding/removing a Person it will also have to set a Job in the department for that person.
 * Having this in mind this function interact also with an object of HandleChooseDepartmentPersonJob type.
 * 
 * @author coni
 * 
 * @param sourceSelectId Id of the source select
 * @param destSelectBId  Id of the destination select
 * @param remove If false  the function will handle the Add case, else the Remove
 * @return
 */
function moveSelectOptionsSourceDestPerson(sourceSelectId, destSelectBId, remove, handleChooseDepartmentPersonJob){
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
				sourceSelect.removeChild(selected[i]);
			}
		}
		var personsAdded =  new Array();
		var j = 0;
		
		var newOption;
		// For every option selected
		for(i = 0; i < selected.length; i++) {
			if (destSelectHM.length > 0) {
				if (destSelectHM[selected[i].value] == null) {
					// Create new option
					newOption = document.createElement('option');
					newOption.id = selected[i].id;
					newOption.value = selected[i].value;
					newOption.innerHTML = selected[i].innerHTML;
					destSelect.appendChild(newOption);
					
					//For interacting with jobs
					personsAdded[j++] = {id: selected[i].id, name: selected[i].innerHTML};
					
					if (!remove) sourceSelect.removeChild(selected[i]);
				} else{
					// already present
					if(remove){
						personsAdded[j++] = {id: selected[i].id, name: selected[i].innerHTML};
					} else {
						sourceSelect.removeChild(selected[i]);
					}
				}
			} else {
				// Create new option
				newOption = document.createElement('option');
				newOption.id = selected[i].id;
				newOption.value = selected[i].value;
				newOption.innerHTML = selected[i].innerHTML;
				destSelect.appendChild(newOption);
				
				//For interacting with jobs
				personsAdded[j++] = {id: selected[i].id, name: selected[i].innerHTML};
				
				if (!remove) sourceSelect.removeChild(selected[i]);
			}
		}
		
		//Synchronizing with the Department's persons jobs
		for(var l = 0; l < personsAdded.length; l++) {
			if (!remove) {
				handleChooseDepartmentPersonJob.addJob(personsAdded[l].id, personsAdded[l].name);
			} else {
				handleChooseDepartmentPersonJob.removeJob(personsAdded[l].id);
			}
		}
		
		//Refreshing the Department's persons jobs
		handleChooseDepartmentPersonJob.displayDepartmentPersonsJobs();
		
		
	} 


//========================================================================================================
/**
 * This class handles Adding Jobs in for a person in a certain department
 * @author coni
 */
function HandleChooseDepartmentPersonJob(containerId, DEPARTMENT_PERSONS, ALL_AVAILABLE_JOBS, jobs_container, jobs_title) {
	
	//Parameter name that is sent on the request as a form input
	var requestParameterName = 'personJob';
	var elem;
	var keyMsg;
	
	this.jobs_container = jobs_container;
	this.jobs_title = jobs_title;
	
	//All the Jobs from the current Organization
	this.ALL_AVAILABLE_JOBS = ALL_AVAILABLE_JOBS;
	//This Department persons
	this.DEPARTMENT_PERSONS = DEPARTMENT_PERSONS;

	//Defining Functions
	this.displayDepartmentPersonsJobs = displayDepartmentPersonsJobs;
	this.addJob = addJob;
	this.removeJob = removeJob;
	this.addSelection = addSelection;

	/**
	 * Displays the Department's Persons Jobs
	 */
	function displayDepartmentPersonsJobs() {
		if (this.DEPARTMENT_PERSONS == null) {
			return;
		}
		
		//displaying the jobs title
		if(this.DEPARTMENT_PERSONS.length > 0) {
			document.getElementById(this.jobs_container).innerHTML = this.jobs_title;
		} else {
			document.getElementById(this.jobs_container).innerHTML = '';
		}
		
		var container = document.getElementById(containerId);
		var toDisplay  = "<table>";
		for(var i = 0; i < this.DEPARTMENT_PERSONS.length; i++) {
			toDisplay +='<tr><td>' +  this.DEPARTMENT_PERSONS[i].personName + '</td>';
			var theSelect = '<td>';
			theSelect += '<select id=\'' + requestParameterName + this.DEPARTMENT_PERSONS[i].personId + '\' name=\'' + requestParameterName + this.DEPARTMENT_PERSONS[i].personId + '\' class=\'validate-not-first\'>';
			theSelect += '<option value=\'-1\'>' + 'DEPARTMENTFORM_NO_JOB_SELECTED'.translate('COMMON') + '</option>';
			for(var j = 0; j < this.ALL_AVAILABLE_JOBS.length; j++) {
				theSelect += '<option value=\'' + this.ALL_AVAILABLE_JOBS[j].id + '\'';
				if (this.DEPARTMENT_PERSONS[i].jobId  == this.ALL_AVAILABLE_JOBS[j].id) {
					theSelect += ' selected=\'selected\'>';
				} else {
					theSelect +=' >' ;
				}
				theSelect += this.ALL_AVAILABLE_JOBS[j].name + '</option>';
				
			}
			theSelect += '</td>';
			toDisplay += theSelect;
			
			}
		toDisplay +='</table>';
		container.innerHTML = toDisplay;
		
		// add event listeners
		for(var l = 0; l < this.DEPARTMENT_PERSONS.length; l++) { 
			//put title for all the jobs
			elem = document.getElementById(requestParameterName + this.DEPARTMENT_PERSONS[l].personId);
			keyMsg = 'PERSON_JOBS_SELECT_ERROR'.translate('COMMON');
			elem.setAttribute('title', keyMsg);
			YAHOO.util.Event.addListener(requestParameterName + this.DEPARTMENT_PERSONS[l].personId, 'change',
					addSelection, {DEPARTMENT_PERSONS: this.DEPARTMENT_PERSONS, form: requestParameterName + this.DEPARTMENT_PERSONS[l].personId, personId: this.DEPARTMENT_PERSONS[l].personId}, true);
		}
	}
		
	/**
	 * Remove on of this department person's job
	 */
	function removeJob(personId) {
		var newJobs = new Array();
		var j = 0;
		for(var i = 0; i < this.DEPARTMENT_PERSONS.length; i++) {
			if(personId != this.DEPARTMENT_PERSONS[i].personId) {
				newJobs[j++] = {jobId : this.DEPARTMENT_PERSONS[i].jobId, jobName: this.DEPARTMENT_PERSONS[i].jobName, 
						personName: this.DEPARTMENT_PERSONS[i].personName, personId: this.DEPARTMENT_PERSONS[i].personId};
			}
	     }
		
		this.DEPARTMENT_PERSONS = newJobs;
	}
	
	/**
	 * Adds job to this department persons jobs 
	 */
	function addJob(personId, personName) {
		if (!this.DEPARTMENT_PERSONS) {
			this.DEPARTMENT_PERSONS = new Array();
			this.DEPARTMENT_PERSONS[0] = {jobId: null, jobName: null, personId: personId, personName: personName};
		} else {
			this.DEPARTMENT_PERSONS[this.DEPARTMENT_PERSONS.length] =  {jobId: null, jobName: null, personId: personId, personName: personName};
		}
	}
	
	/**
	 * Sets in person's job in DEPARTMENT_PERSONS
	 * 
	 * @param selectName
	 * @param jobIndex
	 * @return
	 */
	function addSelection(){	
		var sel = document.getElementById(this.form);
		for(var i = 0; i < this.DEPARTMENT_PERSONS.length; i++) {
			if(this.personId == this.DEPARTMENT_PERSONS[i].personId) {
				this.DEPARTMENT_PERSONS[i].jobId = sel.options[sel.selectedIndex].value;
			}
	     }
	}

}	

//========================================================================================================
/**
* This class is the omologue Map from Java
* 
* @author Adelina
*/
function Map() {
    
	// members
    this.keyArray = new Array(); // Keys
    this.valArray = new Array(); // Values
        
    // methods
    this.put = put;
    this.get = get;
    this.size = size;    
    this.keySet = keySet;
    this.valSet = valSet;
    this.showMe = showMe;   // returns a string with all keys and values in map.
    this.findIt = findIt;
}

/**
* This class adds to the map the pairs (key, val)
* 
* @author Adelina
*/
function put( key, val ) {
    
	var elementIndex = this.findIt( key );
    
    if( elementIndex == (-1) )
    {
        this.keyArray.push( key );
        this.valArray.push( val );
    }
    else
    {
        this.valArray[ elementIndex ] = val;
    }
}

/**
* This class gets the value identified by a given key
* 
* @author Adelina
*/
function get( key ) {
    
	var result = null;
    var elementIndex = this.findIt( key );

    if( elementIndex != (-1) )
    {   
        result = this.valArray[ elementIndex ];
    }  
    
    return result;
}

/**
* This class removes a pair identified by a given key
* 
* @author Adelina
*/
function remove( key ) {
    
	var result = null;
    var elementIndex = this.findIt( key );

    if( elementIndex != (-1) )
    {
        this.keyArray = this.keyArray.removeAt(elementIndex);
        this.valArray = this.valArray.removeAt(elementIndex);
    }  
    
    return ;
}

/**
* This class returns the size of the map
* 
* @author Adelina
*/
function size(){
    
	return (this.keyArray.length);  
}

/**
* This class returns the keySet
* 
* @author Adelina
*/
function keySet() {
    
	return (this.keyArray);
}

/**
* This class returns the valSet
* 
* @author Adelina
*/
function valSet() {
    
	return (this.valArray);   
}

/**
* This class returns a String with all key and values in map
* 
* @author Adelina
*/  
function showMe() {
    
	var result = "";
    
    for( var i = 0; i < this.keyArray.length; i++ )
    {
        result += "Key: " + this.keyArray[ i ] + "\tValues: " + this.valArray[ i ] + "\n";
    }
    return result;
}

/**
* This class returns the value to find
* 
* @author Adelina
*/ 
function findIt( key ) {
    
	var result = (-1);

    for( var i = 0; i < this.keyArray.length; i++ )
    {
        if( this.keyArray[ i ] == key )
        {
            result = i;
            break;
        }
    }
    return result;
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


var ALL_JOBS; 
var JOBS_DEPTS = new Map();

//========================================================================================================
/**
 * This class display the status for a job when a  given job option is selected
 * @author Adelina
 */
function getJobStatus(JOB, deptId){		
	 	
	var requestParameterName = 'jobInDeptDiv';	
	var jobsDivId = 'jobsDivId';
	var divId = requestParameterName + deptId;
	
	document.getElementById(jobsDivId).title = JOB;
		
	for(var i = 0; i < ALL_JOBS.length; i++) {						
		if(ALL_JOBS[i].name == JOB) {			
			if(ALL_JOBS[i].status == 0) {																			
					var displayStatus = 'PERSONFORM_JOB_DISABLED_ERROR'.translate('COMMON');											
					document.getElementById(divId).innerHTML = displayStatus;						
			} else {								
					document.getElementById(divId).innerHTML = '';		
			}			
			return;
		}		
	}
}

function getStyleTextColor(select){
	var sel = document.getElementById(select);		
    var elem= sel.children[selectedIndex];    
	var theCSSprop = window.getComputedStyle(elem,"color").getPropertyValue("color");         
    return theCSSprop;
}
/**
 * Color the selected option
 * 
 * @author Adelina
 * 
 * @return
 */
function selectOption() {		
	for(var i = 0; i < SELECT_DEPT_JOB.length; i++) {									
		var length = document.getElementById(SELECT_DEPT_JOB[i]).options.length;		
		for (var j = 0; j < length; j++) {
	      if (document.getElementById(SELECT_DEPT_JOB[i]).options[j].selected) {
				var sel = j;			
				break;
		  }
		}	
		var color = document.getElementById(SELECT_DEPT_JOB[i]).options[sel].style.color;		
		document.getElementById(SELECT_DEPT_JOB[i]).style.setProperty('color', color, '');		
	}	

} 


//========================================================================================================
/**
 * This class handles Adding Jobs in Department for a certain Person
 * @author dan.damian
 */
function HandleChooseJobInDepartment(containerId, JOBS, ALL_AVAILABLE_JOBS, jobs_container, jobs_title) {
	
	//Parameter name that is sent on the request as a form input
	var requestParameterName = 'jobInDept';
	var elem;
	var keyMsg;
	
	this.jobs_container = jobs_container;
	this.jobs_title = jobs_title;
	
	//All System Available Jobs
	this.ALL_AVAILABLE_JOBS = ALL_AVAILABLE_JOBS;
	
	ALL_JOBS = this.ALL_AVAILABLE_JOBS;
	
	//This Person Jobs
	this.JOBS = JOBS;	
	
	//Defining Functions
	this.displayPersonJobs = displayPersonJobs;
	this.addJob = addJob;
	this.removeJob = removeJob;
	this.addSelection = addSelection;
	this.addGeneralJob = addGeneralJob;

	/**
	 * Displays this Person's Jobs
	 */
	function displayPersonJobs() {
		if (this.JOBS == null) {
			return;
		}
		
		//displaying the jobs title
		if(this.JOBS.length > 0) {
			document.getElementById(this.jobs_container).innerHTML = this.jobs_title;
		} else {
			document.getElementById(this.jobs_container).innerHTML = '';
		}
		
		//and the selects
		var container = document.getElementById(containerId);
		var toDisplay  = "<table>";
		var colorInactive = "color:red";
		var color = "color:#000000";
						
		for(var i = 0; i < this.JOBS.length; i++) {
			SELECT_DEPT_JOB[i] = requestParameterName + this.JOBS[i].departmentId;			
			JOBS_DEPTS.put(this.JOBS[i].name, this.JOBS[i].departmentId);		
			var deptId = this.JOBS[i].departmentId;
			var tabIndex = 25 + i;
			toDisplay +='<tr><td>' +  this.JOBS[i].departmentName + '</td>';
			var theSelect = '<td>';								
			if(this.JOBS[i].name == null) {
				theSelect += '<div id=\'' + 'jobsDivId' +'\' title=\'' + 'PERSONFORM_NO_JOB_SELECTED'.translate('COMMON') + '\'>';			
				theSelect += '<select id=\'' + requestParameterName + this.JOBS[i].departmentId + '\' tabindex=\'' + tabIndex +'\' name=\'' + requestParameterName + this.JOBS[i].departmentId + '\' class=\'formField validate-not-first\' onChange=\'' + 'getJobStatus('+ 'this.options[selectedIndex].text' +',' + deptId + ');' + 'this.style.color = this.options[selectedIndex].style.color;\'>';							
				theSelect += '<option title=\'' + 'PERSONFORM_NO_JOB_SELECTED'.translate('COMMON') + '\' style=\'' + color + '\' value=\'-1\'>' + 'PERSONFORM_NO_JOB_SELECTED'.translate('COMMON') + '</option>';
				for(var j = 0; j < this.ALL_AVAILABLE_JOBS.length; j++) {											
					if(this.ALL_AVAILABLE_JOBS[j].status == 0){
						theSelect += '<option title=\'' + this.ALL_AVAILABLE_JOBS[j].name + '\' style=\'' + colorInactive + '\' value=\'' + this.ALL_AVAILABLE_JOBS[j].id + '\'';						
					} else {
						theSelect += '<option title=\'' + this.ALL_AVAILABLE_JOBS[j].name + '\' style=\'' + color + '\' value=\'' + this.ALL_AVAILABLE_JOBS[j].id + '\'';
					}
					if (this.JOBS[i].id  == this.ALL_AVAILABLE_JOBS[j].id) {
						theSelect += ' selected=\'selected\'>';							
					} else {
						theSelect +=' >' ;
					}
					theSelect += this.ALL_AVAILABLE_JOBS[j].name + '</option>';					
				}			
				theSelect += '</td>';
				theSelect += '</div>';
			} else {  
				theSelect += '<div id=\'' + 'jobsDivId' +'\' title=\'' + this.JOBS[i].name  + '\'>';			
				theSelect += '<select id=\'' + requestParameterName + this.JOBS[i].departmentId + '\' style=\'' + color + '\' tabindex=\'' + tabIndex +'\' name=\'' + requestParameterName + this.JOBS[i].departmentId + '\' class=\'formField validate-not-first\' onChange=\'' + 'getJobStatus('+ 'this.options[selectedIndex].text' +',' + deptId + ');' + 'this.style.color = this.options[selectedIndex].style.color;\'>';		
				theSelect += '<option title=\'' + 'PERSONFORM_NO_JOB_SELECTED'.translate('COMMON') + '\' style=\'' + color + '\' value=\'-1\'>' + 'PERSONFORM_NO_JOB_SELECTED'.translate('COMMON') + '</option>';
				for(var j = 0; j < this.ALL_AVAILABLE_JOBS.length; j++) {											
					if(this.ALL_AVAILABLE_JOBS[j].status == 0){
						theSelect += '<option title=\'' + this.ALL_AVAILABLE_JOBS[j].name + '\' style=\'' + colorInactive + '\' value=\'' + this.ALL_AVAILABLE_JOBS[j].id + '\'';					
					} else {
						theSelect += '<option title=\'' + this.ALL_AVAILABLE_JOBS[j].name + '\' style=\'' + color + '\' value=\'' + this.ALL_AVAILABLE_JOBS[j].id + '\'';
					}
					if (this.JOBS[i].id  == this.ALL_AVAILABLE_JOBS[j].id) {
						theSelect += ' selected=\'selected\'>';							
					} else {
						theSelect +=' >' ;
					}
					theSelect += this.ALL_AVAILABLE_JOBS[j].name + '</option>';					
				}			
				theSelect += '</td>';
				theSelect += '</div>';
			}
			toDisplay += theSelect;	
			
			var divStatusId = requestParameterName + 'Div' + this.JOBS[i].departmentId;			
			toDisplay += '<td class= \'' + 'inactiveJob' + '\'>';						
			
			if(this.JOBS[i].status == 0) {							
				toDisplay += '<div  id = \'' + divStatusId + '\'>' + 'PERSONFORM_JOB_DISABLED_ERROR'.translate('COMMON')  + '</div>';							
			} else {
				toDisplay += '<div id = \'' + divStatusId + '\'>' + '</div>';
			}
			toDisplay += '</td>';
					
		}		
		
		toDisplay +='</table>';
		container.innerHTML = toDisplay;	
		
		// add event listeners
		for(var l = 0; l < this.JOBS.length; l++) { 
			//put title for all the jobs
//			elem = document.getElementById(requestParameterName + this.JOBS[l].departmentId);
//			keyMsg = 'PERSON_JOBS_SELECT_ERROR'.translate('COMMON');
//			elem.setAttribute('title', keyMsg);
			YAHOO.util.Event.addListener(requestParameterName + this.JOBS[l].departmentId, 'change',
					addSelection, {JOBS: this.JOBS, form: requestParameterName + this.JOBS[l].departmentId, departmentId: this.JOBS[l].departmentId}, true);
			
		}
	}
		
	/**
	 * Remove on of this Person's job
	 */
	function removeJob(departmentId) {
		var newJobs = new Array();
		var j = 0;
		for(var i = 0; i < this.JOBS.length; i++) {
			if(departmentId != this.JOBS[i].departmentId) {
				newJobs[j++] = {id : this.JOBS[i].id, name: this.JOBS[i].name, 
						departmentName: this.JOBS[i].departmentName, departmentId: this.JOBS[i].departmentId};
			}
	     }
		
		this.JOBS = newJobs;
	}
	
	/**
	 * Adds job to this Person jobs  
	 */
	function addJob(departmentId, departmentName) {
		if (!this.JOBS) {
			this.JOBS = new Array();
			this.JOBS[0] = {id: null, name: null, departmentId: departmentId, departmentName: departmentName};
		} else {
			this.JOBS[this.JOBS.length] =  {id: null, name: null, departmentId: departmentId, departmentName: departmentName};
		}
	}
	
	 /**
	  * Adds job in job's nomenclator
	  */
	function addGeneralJob(id, name) {
		if (!this.ALL_AVAILABLE_JOBS) {
			this.ALL_AVAILABLE_JOBS = new Array();
			this.ALL_AVAILABLE_JOBS[0] = {id: id, name: name};
		} else {
			this.ALL_AVAILABLE_JOBS[this.ALL_AVAILABLE_JOBS.length] =  {id: id, name: name};
		}
	}
		
	/**
	 * Sets in department's job in JOBS
	 * 
	 * @param selectName
	 * @param jobIndex
	 * @return
	 */
	function addSelection(){
		var sel = document.getElementById(this.form);
		for(var i = 0; i < this.JOBS.length; i++) {
			if(this.departmentId == this.JOBS[i].departmentId) {
				this.JOBS[i].id = sel.options[sel.selectedIndex].value;
			}
	     }
	}
	
	/**
	 * Displays a Select control to allow the user to change 
	 * a job.
	 */
	
	function showSelectWithAllJobs() {
		var div = document.getElementById(this.divId); 
		var theSelect = '<select name=\'' + requestParameterName + this.departmentId+'\'>';
		for(var i = 0; i < this.jobHandler.ALL_AVAILABLE_JOBS.length; i++) {
			theSelect += '<option value=\'' + this.jobHandler.ALL_AVAILABLE_JOBS[i].id + '\'';
			if (this.jobId == this.jobHandler.ALL_AVAILABLE_JOBS[i].id) {
				theSelect += ' selected=\'selected\'>';
			} else {
				theSelect +=' >' ;
			}
			theSelect += this.jobHandler.ALL_AVAILABLE_JOBS[i].name + '</option>';
		}
		theSelect += '</option>';
		div.innerHTML = theSelect;
	}
}

	/**
	*  Function used to open the organigram if the session hasn't expired
	*  @author coni
	*/
	function openOrganigram(_orgId){
		var orgId = ( this.orgId != null ? this.orgId : _orgId);
		var substringToCheck = "sessionExpired";
		var loadObject = { 
			handleSucess:function (o){
				if(o.responseText !== undefined){
					//if the response string contains the "sessionExpired" substring, it means that the session has expired
					//and the proper panel must be displayed; the organigram window shouldn't be opened anymore
					if ( o.responseText.indexOf(substringToCheck) != -1){
						YAHOO.om.smallLoading.hide();
						
						var mainContentDiv = document.getElementById('MAIN_CONTENT');
						mainContentDiv.innerHTML = o.responseText;
						
						var scriptTags = mainContentDiv.getElementsByTagName('script');
						for(var i=0;i<scriptTags.length;i++){
							eval(scriptTags[i].innerHTML);
						}
					} else {
						YAHOO.om.smallLoading.hide();
						organigramWindow = window.open('OrganisationOrganigram.htm?id='+orgId,'', 'width=1, height=1, toolbar=no, scrollbars=yes');
					}
				}
			},
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
				YAHOO.om.smallLoading.hide();
	    	},
	    	startRequest:function() {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', makeUniqueRequest('OrganisationOrganigram.htm?id='+orgId), callbackForLoad); 
		    }
		}; 
		var callbackForLoad = {
			success:loadObject.handleSucess, 
			failure:loadObject.handleFailure
		};
		// Show Small Loading
		YAHOO.om.smallLoading.show();
		//in order to test if the session has expired, I have to make a request for the organization organigram
    	loadObject.startRequest();

	}

	/**
	*	Used to open a panel containing a person, department or organization's information
	*	@author coni & mitziuro
	*/
	function displayInfoPanel(_url, _panelName, _panelTitle, _width_size) {
		var url = (this.url != null ? this.url : _url),
		panelName = (this.panelName != null ? this.panelName : _panelName),	
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),	
		width_size = (this.width_size != null ? this.width_size : _width_size);		
		
		if (YAHOO.om.infoPanel != null){		
			YAHOO.om.infoPanel.hide();
		}
			
		YAHOO.om.infoPanel = new YAHOO.widget.Panel("InfoPanel", { fixedcenter:true, width: width_size, visible: false, constraintoviewport: true, close: true, draggable:true,
			 effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},  modal: true, zindex:1, iframe: true});
		YAHOO.om.infoPanel.setHeader('<span title=\'' + panelTitle +'\'>' + panelName + '</span>'); 	
		
		YAHOO.om.infoPanel.render(document.body);
		returnContentFromUrlToPanelAndShowPannel(url, YAHOO.om.infoPanel);
		
	}
	
	//========================================================================================================
	
	/**
	 * Destroy the add job panel
	 * @author mitziuro
	 * @return
	 */
	function destroyAddJobPanel(){
	
		if (YAHOO.om.jobAdd != null){
			YAHOO.om.jobAdd.hide();
		}
	}
	
	//========================================================================================================	
	
		/**
	*	Used to open a panel for adding a job
	*	@author mitziuro
	*/
	function displayAddJobPanel(_url, _panelTitle, _width_size, _handleChooseJobInDepartment) {
		var url = (this.jobUrl != null ? this.jobUrl : _url),
		panelTitle = (this.jobPanelTitle != null ? this.jobPanelTitle : _panelTitle),
		handleChooseJobInDepartment = (this.handleChooseJobInDepartment != null ? this.handleChooseJobInDepartment : _handleChooseJobInDepartment);
	
		if (YAHOO.om.jobAdd != null){
			YAHOO.om.jobAdd.destroy();
		}
		
		//we have to specify the width size fo IE
		YAHOO.om.jobAdd = new YAHOO.widget.Panel("JobAddPanel", { fixedcenter:true, width: "330px", visible: false, constraintoviewport: true, close: true, draggable:true,
			 effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},  modal: true, zindex:1, iframe: true});
		YAHOO.om.jobAdd.setHeader('<span>' + panelTitle + '</span>'); 
		YAHOO.om.jobAdd.render(document.body);
		
		returnContentFromUrlToPanelAndShowPannel(url, YAHOO.om.jobAdd);
		
	}

	 
	 /**
	  * Function for drawing a button near the file upload
	  * @author mitziuro
	  */
	 function initFileUploads(message) {
	 	//if the browser do not suprt this we display the normal select
	 	if (!(document.createElement && document.getElementsByTagName)) {
	 		return;
	 	}
	 	
	 	var fakeFileUpload = document.createElement('div');
	 	var browser = whichBrs();
		if (browser == "Internet Explorer") {
			fakeFileUpload.className = 'fakefileIE';
		} else if (browser == "Chrome") {
			fakeFileUpload.className = 'fakefileChrome';
		} else {	
				fakeFileUpload.className = 'fakefileFF';
		}
	 	fakeFileUpload.appendChild(document.createElement('input'));
	 	var space = document.createTextNode('\u00a0');
	 	fakeFileUpload.appendChild(space);
	 	
	 	var button = document.createElement('input');
	 	button.type = 'button';
	 	button.className = 'buttonFileBrowse';
	 	button.value = message;
	 	button.setAttribute('readonly', true);	 
	 	fakeFileUpload.appendChild(button);
	 	var x = document.getElementsByTagName('input');
	 	for (var i=0;i<x.length;i++) {
	 		//if is file
	 		if (x[i].type != 'file') {
	 			continue;
	 		}
	 		//if is in a div
	 		if (x[i].parentNode.className != 'fileinputs') {
	 			continue;
	 		}
	 		
	 		//we hide and create a fake
	 		var clone = fakeFileUpload.cloneNode(true);
	 		x[i].parentNode.appendChild(clone);
	 		x[i].relatedElement = clone.getElementsByTagName('input')[0];
	 		x[i].onchange = x[i].onmouseout = function () {
	 			this.relatedElement.value = this.value;
	 		}
	 		x[i].className = 'file hidden';
	 	}
	 }
	  
	//========================================================================================================
   /**
    * Moves options form Source Select to Destination Select. The two selects has to be identical:
    * 		- same option id and label
    * 
    * @author Adelina
    * 
    * @param sourceSelectId Id of the source select
    * @param destSelectBId  Id of the destination select
    * @param managerId Id the manager that doesn't need to be deleted
    * @param remove If false  the function will handle the Add case, else the Remove
    * @return
    */

   function moveSelectOptionsSourcePersonsFromDepartment(sourceSelectId, destSelectBId, managerId, remove){    
	   	//alert("managerId  = " + managerId);
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
//=============================================== END OF cs_business.js ==================================