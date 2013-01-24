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

//========================================================================================================
/**
 * JAVASCRIPT BUSINESS ORIENTED METHODS
 */

//========================================================================================================

//==============================================================================================================================
/**
 * Displays a panel on DocumentForm for selecting the file
 * @param _url The url from where panel's content is retrieved
 * @param _panelTitle Panel't title
 * @author mitziuro
 */
 
function manageFileAdd(_url, _panelTitle, _xPosition, _yPosition){

	var url = (this.url != null ? this.url : _url),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),
	xPosition = (this.xPosition != null ? this.xPosition : _xPosition),
	yPosition = (this.yPosition != null ? this.yPosition : _yPosition);
	
	if (YAHOO.ts.showFileSelectPanel != null) {
		YAHOO.ts.showFileSelectPanel.destroy();
	}
	
	var width = null;
	//we test what browser it is for display option
	var browser = whichBrs();
	if (browser == "Internet Explorer") {
		width = "0px";
	}
	
	YAHOO.ts.showFileSelectPanel = new YAHOO.widget.Panel("ShowFileSelectPanel", { width: width, visible:false, x: xPosition, y: yPosition, modal: true, constraintoviewport:true, close: true, draggable:true, 
		effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	YAHOO.ts.showFileSelectPanel.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.ts.showFileSelectPanel.render(document.body);
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.ts.showFileSelectPanel);
		
}

/**
 * Manager the work hours period
 * 
 * @author Adelina
 * 
 * @param _startTimeId
 * @param _endTimeId
 * @param _timeId
 * @param _timeValue
 * @return
 */
function manageWorkHoursPeriod(_startTimeId, _endTimeId, _timeId) {
	var startTimeId = (this.startTimeId != null ? this.startTimeId : _startTimeId), 	
		endTimeId = (this.endTimeId != null ? this.endTimeId : _endTimeId),			
		timeId = (this.timeId != null ? this.timeId : _timeId);
	
	var startTime = document.getElementById(startTimeId).value; 
	var endTime = document.getElementById(endTimeId).value
	
	var time = document.getElementById(timeId).value;
	var savedTime = time;		
			
	if(startTime != '' || endTime != '') {
		document.getElementById("startTimeRequired").style.display = "inline";		
		document.getElementById("endTimeRequired").style.display = "inline";	
	} else {
		document.getElementById("startTimeRequired").style.display = "none";		
		document.getElementById("endTimeRequired").style.display = "none";	
	}
	
	if(startTime != '' && endTime != '') {
			
		var startMonth = startTime.substring(0,2);		
	 	var startDay = startTime.substring(3,5);	 	
	 	var startMonthTemp = startTime.substring(2, 5);	 	
	 		 	
	 	startTime = startTime.replace(startMonth, startDay);	 	
	 	startTime = startTime.replace(startMonthTemp, "/".concat(startMonth));
	 	
		var endMonth = endTime.substring(0,2);		
	 	var endDay = endTime.substring(3,5);	 	
	 	var endMonthTemp = endTime.substring(2, 5);	 	
	 		 	
	 	endTime = endTime.replace(endMonth, endDay);	 	
	 	endTime = endTime.replace(endMonthTemp, "/".concat(endMonth));	
						
		var date1 = new Date(startTime);
		var date2 = new Date(endTime);
		
		var diff = new Date();
		diff.setTime(Math.abs(date1.getTime() - date2.getTime()));
		var timeDiff = diff.getTime();	
		var minutes = Math.floor(timeDiff/(60 * 1000));		
		var hours = (minutes/60) | 0;
		var min = minutes%60;		
					
		if(min == 0) {
			if(hours <=9 ) {
				time = "0".concat(hours).concat(":").concat("00");
			} else {
				time = "".concat(hours).concat(":").concat("00");
			}
		} else if (min <= 9){
			if(hours <=9 ) {
				time = "0".concat(hours).concat(":").concat("0").concat(min);
			} else {
				time = "".concat(hours).concat(":").concat("0").concat(min);
			}
		} else {
			if(hours <=9 ) {
				time = "0".concat(hours).concat(":").concat(min);
			} else {
				time = "".concat(hours).concat(":").concat(min);
			}
		}
	} 
	
	TIME = time;	
	if(savedTime == "") {
		document.getElementById(timeId).value = time;
	}
	
}
 
 /**
  * Manager the work hours period
  * 
  * @author Adelina
  * 
  * @param _startTimeId
  * @param _endTimeId
  * @param _timeId
  * @param _timeValue
  * @return
  */
 function manageOvertimeWorkHoursPeriod(_startTimeId, _endTimeId, _timeId) {
 	var startTimeId = (this.startTimeId != null ? this.startTimeId : _startTimeId), 	
 		endTimeId = (this.endTimeId != null ? this.endTimeId : _endTimeId),			
 		timeId = (this.timeId != null ? this.timeId : _timeId);
 	
 	var startTime = document.getElementById(startTimeId).value;
 	var endTime = document.getElementById(endTimeId).value 	
 	
 	var time = document.getElementById(timeId).value;
 	var savedTime = time;	
 	
 	if(startTime != '' || endTime != '') {
 		document.getElementById("overTimeStartTimeRequired").style.display = "inline";		
 		document.getElementById("overTimeEndTimeRequired").style.display = "inline";	
 	} else {
 		document.getElementById("overTimeStartTimeRequired").style.display = "none";		
 		document.getElementById("overTimeEndTimeRequired").style.display = "none";	
 	}
 	
 	if(startTime != '' && endTime != '') {
 		
 		var startMonth = startTime.substring(0,2);		
	 	var startDay = startTime.substring(3,5);	 	
	 	var startMonthTemp = startTime.substring(2, 5);	 	
	 		 	
	 	startTime = startTime.replace(startMonth, startDay);	 	
	 	startTime = startTime.replace(startMonthTemp, "/".concat(startMonth));
	 	
		var endMonth = endTime.substring(0,2);		
	 	var endDay = endTime.substring(3,5);	 	
	 	var endMonthTemp = endTime.substring(2, 5);	 	
	 		 	
	 	endTime = endTime.replace(endMonth, endDay);	 	
	 	endTime = endTime.replace(endMonthTemp, "/".concat(endMonth));	
 		
 		var date1 = new Date(startTime);	
 		var date2 = new Date(endTime);	
 		
 		var diff = new Date();
 		diff.setTime(Math.abs(date1.getTime() - date2.getTime()));
 		var timeDiff = diff.getTime();	
 		var minutes = Math.floor(timeDiff/(60 * 1000));	
 			
 		var hours = (minutes/60) | 0;
 		var min = minutes%60;
 					
 		if(min == 0) {
 			if(hours <=9 ) {
 				time = "0".concat(hours).concat(":").concat("00");
 			} else {
 				time = "".concat(hours).concat(":").concat("00");
 			}
 		} else if (min <= 9){
 			if(hours <=9 ) {
 				time = "0".concat(hours).concat(":").concat("0").concat(min);
 			} else {
 				time = "".concat(hours).concat(":").concat("0").concat(min);
 			}
 		} else {
 			if(hours <=9 ) {
 				time = "0".concat(hours).concat(":").concat(min);
 			} else {
 				time = "".concat(hours).concat(":").concat(min);
 			}
 		}
 	} 
 	
 	OVERTIME = time;
 	
 	if(savedTime == "") {
		document.getElementById(timeId).value = time;
	} 	
 	
 }
 

//==============================================================================================================================
/**
 * Destroys the select file panel
 * @author mitziuro
 */
	 
function destroyFileAddPanel(){
	//
	if(YAHOO.ts.showFileSelectPanel){
		YAHOO.ts.showFileSelectPanel.destroy();
		YAHOO.ts.showFileSelectPanel = null;
	}
}

//==============================================================================================================================
/**
 * Displays selectable datatable
 * @param _tableId the table containig the information
 * @param _divId the containerId
 * @param _paginationDivId where the paginator will stand
 * @param _field where the final value will be droped
 * @param others - the internationalized column headers 
 * @author mitziuro
 */
 
function manageFileDataTable(_tableId, _divId, _paginationDivId, _field, _fileHeader, _extensionHeader, _sizeHeader, _contentHeader, _previousText, _nextText){

	
	//setting the parameters
	var FileColumnDefs = [{key: _fileHeader, sortable:true}, {key: _extensionHeader, sortable:true}, {key: _sizeHeader, sortable:true},{key: _contentHeader}];
	var FileDataSource = new YAHOO.util.DataSource(YAHOO.util.Dom.get(_tableId));
	FileDataSource.responseType = YAHOO.util.DataSource.TYPE_HTMLTABLE;	
	
    FileDataSource.responseSchema = {fields: [ _fileHeader, _extensionHeader, _sizeHeader, _contentHeader]};       	  
  
    //setting the paginator and it's paramaters (number of pages, results/page)
    var paginator = new YAHOO.widget.Paginator({ containers: _paginationDivId, 
    											rowsPerPage: nbResultsPerPage, 
    											firstPageLinkLabel : "&nbsp&nbsp&nbsp&nbsp",
    										    previousPageLinkLabel : "&nbsp&nbsp&nbsp&nbsp",
    										    nextPageLinkLabel :"&nbsp&nbsp&nbsp&nbsp",
    										    lastPageLinkLabel : "&nbsp&nbsp&nbsp&nbsp",
    											pageLinks: nbOfPagesPerPanel});
    
    var oConfigs = { paginator: paginator};
    
    var fileTable = new YAHOO.widget.DataTable(_divId, FileColumnDefs, FileDataSource, oConfigs);
     
    //setting the no entries message
    fileTable.setAttributeConfig("MSG_EMPTY",{value:""});  
    
    // Subscribe to events for row selection
    //fileTable.subscribe("rowMouseoverEvent", fileTable.onEventHighlightRow);
    fileTable.subscribe("rowMouseoverEvent", function nothing() { return false;});
    fileTable.subscribe("rowMouseoutEvent", fileTable.onEventUnhighlightRow);
    fileTable.subscribe("rowClickEvent", yuiFileTableRowClickEvent); 
    fileTable.subscribe("linkClickEvent", yuiFileTableLinkClickEvent);
      	       
    // Programmatically bring focus to the instance so arrow selection works immediately
    fileTable.focus();       


	/**
	* How the table will work on link click event
	* @param oData
	* @author mitziuro
	*/
	function yuiFileTableLinkClickEvent(oData){
		var rowTarget = oData.target; 
	    return false;
	}
	
	/**
	* How the table will work on row click event
	* @param oData
	* @author mitziuro
	*/
	function yuiFileTableRowClickEvent(oData){
		var rowTarget = oData.target; 
	    var nameData = fileTable.getRecord(rowTarget).getData(_fileHeader); 
	    var extensionData = fileTable.getRecord(rowTarget).getData(_extensionHeader); 
	    if(extensionData != ""){
	    	document.getElementById(_field).value = nameData +'.' + extensionData;
	    } else {
	    	document.getElementById(_field).value = nameData;
	    }
	    
	    destroyFileAddPanel();
	}
}

//========================================================================================================

/**
 * 	Used to build the category tree structure first level nodes and enable dinamic node loading
 * 	@author mitziuro
 * 	
 */   
  
function buildCategoryTree(firstLevelNodes, treeDivId, organisationName) {
   
   //node type
	var categoryType = 0,
		documentType = 1,
	
		actionDelete = 0;
	
   //create a new tree:
   tree = new YAHOO.widget.TreeViewDD(treeDivId);
   YAHOO.ts.tree = tree;
  
   //turn dynamic loading on for entire tree:
   tree.setDynamicLoad(loadNodeData, 0);
   
   //get root node for tree:
   var root = tree.getRoot();
   
   //add child nodes for tree; our top level nodes are all the organization
   //direct children organizations and its departments
   if ( firstLevelNodes!= "" ){
   		var aStates = eval("(" + firstLevelNodes + ")");
   
   		for (var i = 0, j = aStates.length; i < j; i++) {
 
			//-href property set to "#" in order not to expand a node when clicking its label
			
			var propertiesObject = {href: "#",movable: false, type: aStates[i].type, name: aStates[i].label};
			var tempNode = new YAHOO.widget.HTMLNode(propertiesObject, root);
			
			tempNode.expanded = false;
			tempNode.hasIcon = true;
			tempNode.data.nodeId = aStates[i].nodeId;
		
   			//define the properties object used to instantiate a new node
   			var label ="<span>" + aStates[i].label + "</span>";
			
			if(aStates[i].type == categoryType){
				tempNode.labelStyle = "categoryNode";
				delete_title = 'DELETE_TITLE'.translate('COMMON');
				var url = "&nbsp<a href=\"#\" onClick=\"manageCategoryActionsFromTree(" + actionDelete + ",'" + aStates[i].label +"', '" + aStates[i].labelElId + "', '" + tempNode.index + "', '" + tempNode.tree.id + "')" + "\"><img title=\"" + delete_title + "\" src=\"././themes/"+ DM_THEME +"/images/buttons/action_delete.png\"\\></a>";
				label = '<img src=\"themes/' + DM_THEME + '/images/treeview/tree-category.bmp\"/>'+ '&nbsp' + label +url;
				tempNode.data.movable = true;
				tempNode.data.canBeParent = true;
				tempNode.data.url = 'CategoryTreeActions.htm?';
			} else {
				label = '<img src=\"themes/' + DM_THEME + '/images/treeview/tree-document.bmp\"/>'+ '&nbsp' + label;
				tempNode.labelStyle = "documentNode";
				tempNode.data.movable = true;
				tempNode.data.canBeParent = false;
				tempNode.data.url = 'DocumentTreeActions.htm?';
			}
			
			tempNode.html = label;
			
			tempNode.labelElId = aStates[i].labelElId;
			tempNode.title = aStates[i].label;
			tempNode.isLeaf = aStates[i].isLeaf;

   		}
	    //render tree with these toplevel nodes; all descendants of these nodes
  		//will be generated as needed by the dynamic loader.
   		tree.draw();
   }
}
//end buildTree

//========================================================================================================
/**
 * 
 * Function for loading dynamicaly a node when you extend it
 * @author mitziuro
 */
function loadNodeData(node, fnLoadComplete)  {
    //We'll load node data based on what we get back when we
    //use Connection Manager topass the text label of the 
    //expanding node to the Yahoo!
    //Search "related suggestions" API.  Here, we're at the 
    //first part of the request -- we'll make the request to the
    //server.  In our success handler, we'll build our new children
    //and then return fnLoadComplete back to the tree.
   
    //node type
	var categoryType = 0,
		documentType = 1,
		
		actionDelete = 0;
    
    //Get the node's label and urlencode it; this is the word/s
    //on which we'll search for related words:
    var nodeLabel = encodeURI(node.labelElId);
   
    //prepare URL for XHR request:
    var sUrl = "CategoryTreeNode.htm?" + nodeLabel;
   
    //prepare our callback object
    var callback = {
    
        //if our XHR call is successful, we want to make use
        //of the returned data and create child nodes.
        success: function(oResponse) {
    		sessionExpired(oResponse);	
    		var oResults = eval("(" + oResponse.responseText + ")");
          
        	//Nodes
            if(oResults.length > 0) {
            //Result is an array if more than one result, string otherwise
                if(YAHOO.lang.isArray(oResults)) {  
                	
                    for (var i = 0, j = oResults.length; i < j; i++) {
                    	
 						
                    	//-href property set to "#" in order not to expand a node when clicking its label
            			var propertiesObject = {href: "#",movable: false, type: oResults[i].type, name: oResults[i].label};
            			var tempNode = new YAHOO.widget.HTMLNode(propertiesObject, node);
            			
            			tempNode.expanded = false;
            			tempNode.hasIcon = true;
            			tempNode.data.nodeId = oResults[i].nodeId;
            			
            			//define the properties object used to instantiate a new node
               			var label ="<span>" + oResults[i].label + "</span>";
            			
            			if(oResults[i].type == categoryType){
            				tempNode.labelStyle = "categoryNode";
            				delete_title = 'DELETE_TITLE'.translate('COMMON');
            				var url = "&nbsp<a href=\"#\" onClick=\"manageCategoryActionsFromTree(" + actionDelete + ",'" + oResults[i].label +"', '" + oResults[i].labelElId + "', '" + tempNode.index + "', '" + tempNode.tree.id + "')" + "\"><img title=\"" + delete_title + "\" src=\"././themes/"+ DM_THEME +"/images/buttons/action_delete.png\"\\></a>";
            				label = '<img src=\"themes/' + DM_THEME + '/images/treeview/tree-category.bmp\"/>' + '&nbsp' + label + url;
            				tempNode.data.movable = true;
            				tempNode.data.canBeParent = true;
            				tempNode.data.url = 'CategoryTreeActions.htm?';
            			} else {
            				label ='<img src=\"themes/' + DM_THEME + '/images/treeview/tree-document.bmp\"/>'+ '&nbsp' + label;
            				tempNode.labelStyle = "documentNode";
            				tempNode.data.movable = true;
            				tempNode.data.canBeParent = false;
            				tempNode.data.url = 'DocumentTreeActions.htm?';
            			}
            			
            			tempNode.html = label;
            			
            			tempNode.labelElId = oResults[i].labelElId;
            			tempNode.title = oResults[i].label;
            			tempNode.isLeaf = oResults[i].isLeaf;
            			
						}
                  
                    //sorting the nodes by their type
                    var nodes = node.children;
                    move = true;
              	  	//bubble sort
                    while(move){
              		  move = false;
              		  for(i = 0;i < nodes.length - 1; i++){
              			  if(nodes[i].data.type > nodes[i+1].data.type) {
              				  aux = nodes[i];
              				  nodes[i] = nodes[i+1];
              				  nodes[i+1] = aux;
              				  move = true;
              			  }
              		  }
                    }
                    
                }
                
                //redraw the tree
                node.tree.draw();
                 
            //When we're done creating child nodes, we execute the node's
            //loadComplete callback method which comes in via the argument
            //in the response object (we could also access it at node.loadComplete,
            //if necessary):
            oResponse.argument.fnLoadComplete();
            }
        },
        
        //if our XHR call is not successful, we want to
        //fire the TreeView callback and let the Tree
        //proceed with its business.
        failure: function(oResponse) {
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
    YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
}
//end loadNodeData

//=======================================================================================================
/**
 * Used to perform actions on categories and documents
 * @author mitziuro
 */ 

function manageCategoryActionsFromTree(action, name, labelElId, nodeIndex, treeId) {
	var  actionDelete = 0;
	
	//the div for tree
	var treeId = "treeDiv";

	confirmDeleteMessageKey = "SUBMIT_DELETE_CATEGORY_FROM_TREE_CONFIRM_MESSAGE";
	
	//get the node on which an action will be performed
	var node = YAHOO.widget.TreeView.getNode(treeId, nodeIndex);
	//prepare the url for asynchronous reqeust 
	
	if (action == actionDelete){
		var sUrl = "CategoryTreeActions.htm?&action=delete&" + labelElId + "&name=" + name;
		var confirmMessage = confirmDeleteMessageKey.toString().translate('COMMON');  
	}
	

	//callback used for the asynchrounous request 
	var callback = {
		success: function(oResponse) {
			if (oResponse.responseText != undefined){
				sessionExpired(oResponse);
				div = document.getElementById("TREE_MESSAGES");
				div.innerHTML = oResponse.responseText;
				
				//evaluating the scripts
				var scriptTags = div.getElementsByTagName('SCRIPT');
				for(var i=0;i<scriptTags.length;i++){
					eval(scriptTags[i].innerHTML);
				}
				
				//if there is an error we do nothing
				var messages = document.getElementById("MESSAGES_CONTAINER");
				var errors = document.getElementById("ERRORS_CONTAINER");
				
				if (action == actionDelete && messages != null && errors == null){
					//get the parent node
					var parentNode = node.parent;
					//remove the the deleted node from the tree
					node.tree.removeNode(node, true);
					//if the parent node doesn't have any other children, it must become a leaf 
					if (!parentNode.hasChildren(true)){
						parentNode.isLeaf = "false";
					}
					
					//I have to render the tree with the new structure
					var tree = YAHOO.widget.TreeView.getTree(treeId);
					tree.draw();
				}
			
				YAHOO.ts.smallLoading.hide();
			}
		},
		failure: function(oResponse) {
            alert("Failed to process XHR transaction.");
        }
    };
	
	
    //function used to handle the case when the user chooses from the confirmation panel not to perform the action on the specific organization
    function handleNo(){
		YAHOO.ts.confirmation.hide();
		YAHOO.ts.confirmation.destroy();
	}
	
    //function used to handle the case when the user chooses from the confirmation panel to perform the action on the specific organization	
	function handleYes() {
	    // Show Small Loading
		YAHOO.ts.smallLoading.show();
		handleNo();
   	 	YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
	}
	
	//create and display the confirmation dialog
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
	
	YAHOO.ts.confirmation.setBody(confirmMessage);
	YAHOO.ts.confirmation.render(document.body);
	YAHOO.ts.confirmation.show();
	
}

//=======================================================================================================
/**
 * Used to perform action on organisation name click in tree (collapsing the tree)
 * @author mitziuro
 */ 

function manageCollapseTree() {
	// collapsing the category tree
	YAHOO.ts.tree.getRoot().collapseAll();
}

//=======================================================================================================
/**
 * function for handling json when adding new privilege connections
 * @author mitziuro
 */
function createEntityPrivilegeConnection(_entitySelect, _privilegeSelect, _actionsHandler, _tableId, _globalCheckBoxId){

 var entitySelect = (this.entitySelect != null ? this.entitySelect : _entitySelect), 
 	 privilegeSelect = (this.privilegeSelect != null ? this.privilegeSelect : _privilegeSelect),
 	 actionsHandler = (this.actionsHandler != null ? this.actionsHandler : _actionsHandler),
 	 tableId = (this.tableId != null ? this.tableId : _tableId),
 	 globalCheckBoxId = (this.globalCheckBoxId != null ? this.globalCheckBoxId : _globalCheckBoxId);

 //building the 2 lists 
 var privilegeList = new Array();
 var entityList = new Array();
 
 var privilegeSelection = document.getElementById(privilegeSelect);
 for(i = 0;i < privilegeSelection.length; i++){
	 if(privilegeSelection[i].selected) { 
		 privilegeList[privilegeList.length] = {id: privilegeSelection.options[i].value,name: privilegeSelection.options[i].text};
	 }
 }
 
 var entitySelection = document.getElementById(entitySelect);
 for(i = 0;i < entitySelection.length; i++){
	 if(entitySelection[i].selected) {
		 entityList[entityList.length] = {id: entitySelection.options[i].value,name: entitySelection.options[i].text, type: (entitySelection.options[i].title == 'UserGroup' ? 0 : 1)};
	 }
 }
 
 //adding the connections
 for(t = 0;t < entityList.length; t++){
	 for(j = 0;j < privilegeList.length; j++){
		 if(actionsHandler.add(privilegeList[j].id, privilegeList[j].name, entityList[t].id, entityList[t].name, entityList[t].type)){
				//adding the row in interface		
				var tbody = document.getElementById( actionsHandler.tableId).getElementsByTagName("tbody")[0];
			
				var row = document.createElement("tr");
				
				var num_rows = document.getElementById(actionsHandler.tableId).getElementsByTagName("tr").length;
				
				if(num_rows % 2 == 0){
					row.className = 'odd_row';
				} else {
					row.className = 'even_row';
				}
				//the check box
				var data0 = document.createElement("td");
				data0.setAttribute('align', 'right');
				data0.style.width = '2px';
				
				var checkbox = document.createElement('input');
				checkbox.type = "checkbox";
				checkbox.name = "associationId";
				checkbox.id= "associationId";
				checkbox.onclick = checkBoxEntityPrivilegeConnection;
				data0.appendChild(checkbox);
				
				//the entity name
				var data1 = document.createElement("td");
				data1.appendChild(document.createTextNode(entityList[t].name));
				data1.setAttribute('align', 'center');
				data1.style.width = '345px';
				
				// set the entity id and type
				var data11 = document.createElement('input');
				data11.setAttribute('type', 'hidden');
				data11.setAttribute('value', entityList[t].id);
				
				var data12 = document.createElement('input');
				data12.setAttribute('type', 'hidden');
				data12.setAttribute('value', entityList[t].type);
				
				//the privilege name
				var data2 = document.createElement("td");
				data2.appendChild (document.createTextNode(privilegeList[j].name));
				data2.setAttribute('align', 'center');
				data2.style.width = '117px';
				
				//the privilegeId
				var data21 = document.createElement('input');
				data21.setAttribute('type', 'hidden');
				data21.setAttribute('value', privilegeList[j].id);
				
				//all hidden parameters are the children of the first td element
				data0.appendChild(data11);
				data0.appendChild(data12);
				data0.appendChild(data21);
				
				//we append the visible parameters
				row.appendChild(data0);
				row.appendChild(data1);
				row.appendChild(data2);
				
				//finaly we append the row to the table
				tbody.appendChild(row); 
		 }
			 
	 }
			
 }
	 
}

//=======================================================================================================
/**
 * function for handling json when deleting privilege connections
 * @author mitziuro
 */
 function deleteEntityPrivilegeConnection(_actionsHandler){
	 var actionsHandler = (this.actionsHandler != null ? this.actionsHandler : _actionsHandler);
	 
	 //getting the table from the actionHandler
	 var table = document.getElementById(actionsHandler.tableId);
	 
	 //we get the rows
	 var row_list = table.getElementsByTagName("tr");
	 //we set the rows for delete
	 var row_for_delete = [];
	 //the first td has the hidden paramters with the entityId, entityType, privilegeId
	 for( var a = 0; a < row_list.length; a++){
		 var column_list = row_list[a].getElementsByTagName("td");
		 var attributes = column_list[0].getElementsByTagName("input");
		 	//if the attribute is checked we mark it for delete
		 	if(attributes[0].checked == true) {
		 		//the entityId is the second parameter and the privilegeId is the third parameter
		 		row_for_delete[row_for_delete.length] = {row: row_list[a], entityId: attributes[1].value , privilegeId: attributes[3].value};
		 	}
		 }
	 
	 //we execute the delete action
	 for( a = 0; a <row_for_delete.length; a++){
		 actionsHandler.deleteEntry(row_for_delete[a].entityId, row_for_delete[a].privilegeId);
		 row_for_delete[a].row.parentNode.removeChild(row_for_delete[a].row);
	 }
	 
	 //we uncheck the checkall option
	 checkBoxEntityPrivilegeConnection();
	 
	 //redraw the remaining lines with the appropiate style
	 for( var a = 0; a < row_list.length; a++){
			if(a % 2 == 0){
				row_list[a].className = 'odd_row';
			} else {
				row_list[a].className = 'even_row';
			}
	 }
	 
 }
//=======================================================================================================
 /**
  * function for handling checkbox selection
  * @author mitziuro
  */
 function checkBoxEntityPrivilegeConnection(_tableId, _checkAll){
	  
	//because in ie it's not working we have to hardcode the elements name
	  _tableId = (_tableId != null ? _tableId : 'associations_table');
	  _checkAll = (_checkAll != null ? _checkAll : 'selectAllAssociations');
	//getting the table from the actionHandler
	 var table = document.getElementById(_tableId);
	
	 //we get the rows
	 var row_list = table.getElementsByTagName("tr");
	 
	 //if we select all or none
	 if(_checkAll == true || _checkAll == false) {
		 //the first td has the check box item on position 0 
		 for(var a = 0; a < row_list.length; a++){
			 //the checkbox element
			 var check_attribute = row_list[a].getElementsByTagName("td")[0].getElementsByTagName("input")[0];
			 if(_checkAll == true){
				check_attribute.checked = true;
			 } else if(_checkAll == false){
					check_attribute.checked = false;
			 } 
		 } 
	 } else {
		 //are all the checkboxes selected ?
		 var all_checked = true;
		 //the first td has the check box item on position 0 
		 for(var a = 0; a < row_list.length; a++){
			//the checkbox element
			 var check_attribute = row_list[a].getElementsByTagName("td")[0].getElementsByTagName("input")[0];
			 if(check_attribute.checked == false) {
				 all_checked = false;
			 }
		 }
		 
		 // test if the global condition is selected
		 if(all_checked == true && row_list.length > 0) {
			 document.getElementById(_checkAll).checked = true;
		 } else {
			 document.getElementById(_checkAll).checked = false;
		 }
	 }
	 
 }

//=======================================================================================================

/**
 * Function for handle the association json
 * @author mitziuro
 */
function handleChooseAssociations(association_json, _responseDiv, tableId){
	
	this.association_json = YAHOO.lang.JSON.parse(association_json);
	
	this.deleteEntry = deleteEntry;
	this.display = display;
	this.add = add;
	this.length = this.association_json.length;
	this.tableId = tableId;
	
	//delete a record from json
	function deleteEntry(entityId, privilegeId) {

		for(i = 0;i < this.length; i++){
			if(entityId == this.association_json[i].entityId && privilegeId == this.association_json[i].privilegeId){
				for(j = i; j < this.length - 1; j++) {
					this.association_json[j] = this.association_json[j+1];
				}
				this.association_json[this.length - 1] = null;	
				this.length--;
			}
		}
		
	}
	
	//function for adding an entry in json
	function add(_privilegeId, _privilegeName, _entityId, _entityName, _entityType){
		
		//we search if the record already exists ( no duplicate)
		for(k = 0;k < this.length; k++){
			if(_entityId == this.association_json[k].entityId && _privilegeId == this.association_json[k].privilegeId
					&& _entityType == this.association_json[k].entityType){
				return false;
			}
		}
		
		this.association_json[this.length] = {privilegeId: _privilegeId, privilegeName: _privilegeName, entityId: _entityId, 
				entityName: _entityName, entityType: _entityType};
		this.length++;
		
		return true;
	}
	
	//function for displaying the results
	//and put them on request
	function display() {
			
		//the results will come like user/usergroup-userId/usergroupId-privilegeId
		var oResponse = "";
		for(i = 0;i < this.length; i++){
			var requestParameterName = (this.association_json[i].entityType == 0 ? 'UserGroup-' : 'User-') +  this.association_json[i].entityId + '-' + this.association_json[i].privilegeId;
			var line = '<input  type=\'hidden\' name=\'' + requestParameterName + '\' value=\'true\'>';
			oResponse += line;
		}
		document.getElementById(_responseDiv).innerHTML = oResponse;
		
	}
}

//==============================================================================================================================
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
	
	fakeFileUpload.className = 'fakefile';
	
	fakeFileUpload.appendChild(document.createElement('input'));
	var space = document.createTextNode('\u00a0');
	fakeFileUpload.appendChild(space);
	var button = document.createElement('input');
	button.type = 'button';
	button.className = 'buttonFileBrowseFF';
	
	var browser = whichBrs();
	if (browser == "Internet Explorer") {
		button.className = 'buttonFileBrowseIE';
	}
	
	if (browser == "Safari") {
		button.className = 'buttonFileBrowseSafari';
	}
	
	if (browser == "Chrome") {
		button.className = 'buttonFileBrowseChrome';
	} 
	
	if(browser == "Firefox"){	
		button.className = 'buttonFileBrowseFF';
	}
	
	
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

/**
*  Function used to open a file in new window
*  @author Adelina
*/
function openFileWithName(_fileN){	
	var fileN = (this.fileN != null ? this.fileN : _fileN);
	organigramWindow = window.open('actions/filePrintServlet?file='+fileN,'', 'width=1000, height=1000, toolbar=no, scrollbars=yes');
}



/**
 * General method for opening a file in a new window
 * 
 * @author alu
 * 
 * @param path
 * @param params
 * @param paramsLength
 * @param name
 * @param options
 * @return
 */
function openFileInNewWindow(path, params, paramsLength, name, options){
	var url = path + '?';
	var first = true;
	
	for (i = 0; i < paramsLength; i++) {
		if (first){
			url = url + params[i].key + '=' + params[i].value;
			first = false;
		} else {
			url = url + '&' + params[i].key + '=' + params[i].value;
		}
	}
	window.document.open(url, name, options);
}
 
 /**
  * Color the selected option
  * 
  * @author Adelina
  * 
  * @return
  */
 function selectOption(selectId, selectId1) {		
				
	var elem = document.getElementById(selectId);
	var elem1 = document.getElementById(selectId1);
	var length = elem.options.length;	
	
	for (var j = 0; j < length; j++) {
      if (elem.options[j].selected) {    	 
			var sel = j;				
			break;
	  }
	}	
	
    var x = elem1.children[sel];    
	var color = window.getComputedStyle(x,"color").getPropertyValue("color");      
	
	elem.style.setProperty('color', color, '');		
 }	

function Parameter(key, value){
	this.key = key;
	this.value = value;
}


/**
 * Used to display a html report in a new window
 * @author Coni
 * @param url
 * @param divId
 * @param pageDivId
 * @return
 */
function manageDisplayReport(url, divId, pageDivId){
	var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					sessionExpired(o);
					var div = document.getElementById(divId);
					div.innerHTML = o.responseText;
					pageDiv.style.display="block";
					YAHOO.ts.smallLoading.hide();
					YAHOO.ts.creatingReport.hide();
					var scriptTags = div.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
				}
			}, 
			handleFailure:function (o){
				YAHOO.ts.smallLoading.hide();
				YAHOO.ts.creatingReport.hide();
				alert("Error " + o.status + " : " + o.statusText);
		    },
			startRequest:function() {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', url, callback); 
		    }
		}; 
		var callback = {
			success:AjaxObject.handleSuccess, 
			failure:AjaxObject.handleFailure 
		};
		//hide the page content
		var pageDiv = document.getElementById(pageDivId);
		pageDiv.style.display="none";
		//display the loading message
        YAHOO.ts.smallLoading.show();
        YAHOO.ts.creatingReport.show();
		//start transaction
		AjaxObject.startRequest();
}

//========================================================================================================
/**
 * Manages the selection of an option from the project select in the record search form  
 * @author Coni
 */
function manageProjectSelectInRecordSearchForm(option, firstNameInputId, lastNameInputId, teamMemberInputId, personRecordOwnerClass, teamMemberRecordOwnerClass, displayNameInput, teamMemberAutoCompleteContainer, teamMemberIdHiddenFieldId, isUserAll, activityId) {
	isUserAll = eval ("(" + isUserAll + ")");
	
	var teamMemberRecordOwnerElements = getElementsByClass(teamMemberRecordOwnerClass, null, null);;
	var personRecordOwnerElements = getElementsByClass(personRecordOwnerClass, null, null);
	
	//empty previously entered/selected values
	if (document.getElementById(teamMemberIdHiddenFieldId) != null) {
		document.getElementById(teamMemberIdHiddenFieldId).value = -1;
	}
	if (document.getElementById(teamMemberInputId) != null) {
		document.getElementById(teamMemberInputId).value = "";
	}
	if (document.getElementById(firstNameInputId) != null) {
		document.getElementById(firstNameInputId).value = "";
	}
	if (document.getElementById(lastNameInputId) != null) {
		document.getElementById(lastNameInputId).value = "";
	}
	
	//populate the activity select with the ones for the selected project
	activitiesSelect = document.getElementById("recordSearchForm_activity");
	var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					sessionExpired(o);
					oResults = eval("(" + o.responseText + ")");
					var theSelect = '';
					theSelect += '<option title=\''+ 'RECORD_CHOOSE_ACTIVITY'.translate('COMMON') + '\' value=\'-1\'>' + 'RECORD_CHOOSE_ACTIVITY'.translate('COMMON') + '</option>' ;

					for (i = 0; i < oResults.length; i++) {
						theSelect += '<option title=\'' + oResults[i].name + '\' value=\'' + oResults[i].id + '\'';
						if (activityId == oResults[i].id) {								
							theSelect += ' selected=\'selected\' >';
						} else {
							theSelect +=' >' ;
						}
						theSelect += oResults[i].name + '</option>';	
					}
					
					activitiesSelect.innerHTML = theSelect;
					document.getElementById("recordSearchForm_activity_titleDiv").title = 'RECORD_CHOOSE_ACTIVITY'.translate('COMMON');
				}
			}, 
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
		    },
			startRequest:function() {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', 'GetProjectActivities.htm?projectId='+option, callback); 
		    }
		}; 
	var callback = {
		success:AjaxObject.handleSuccess, 
		failure:AjaxObject.handleFailure 
	};
		
		
	//the selected option is to search in all the records ->
	//the first name and last name inputs must be displayed
	if (option == -2) {
		for (i=0; i < teamMemberRecordOwnerElements.length; i++){
			teamMemberRecordOwnerElements[i].style.display="none";
		}
		for (i=0; i < personRecordOwnerElements.length; i++){
			if (personRecordOwnerElements[i].tagName.toLowerCase() == "tr") {
				personRecordOwnerElements[i].style.display="table-row";
			} else if (personRecordOwnerElements[i].tagName.toLowerCase() == "td") {
				personRecordOwnerElements[i].style.display="table-cell";
			}
		}
	} else if (option == -1) { //search for records per organization -> the first name and last name must be displayed only if the user has the USER_ALL role
		for (i=0; i < teamMemberRecordOwnerElements.length; i++){
			teamMemberRecordOwnerElements[i].style.display="none";
		}
		if (isUserAll) {
			for (i=0; i < personRecordOwnerElements.length; i++){
				if (personRecordOwnerElements[i].tagName.toLowerCase() == "tr") {
					personRecordOwnerElements[i].style.display="table-row";
				} else if (personRecordOwnerElements[i].tagName.toLowerCase() == "td") {
					personRecordOwnerElements[i].style.display="table-cell";
				}
			}
		} else {
			for (i=0; i < personRecordOwnerElements.length; i++){
				personRecordOwnerElements[i].style.display="none";
			}
		}
		
		//start transaction for activities select
		AjaxObject.startRequest();
	} else if (option == 0) {		
		for (i=0; i < teamMemberRecordOwnerElements.length; i++){
			teamMemberRecordOwnerElements[i].style.display="none";
		}
		if (displayNameInput) {
			for (i=0; i < personRecordOwnerElements.length; i++){
				if (personRecordOwnerElements[i].tagName.toLowerCase() == "tr") {
					personRecordOwnerElements[i].style.display="table-row";
				} else if (personRecordOwnerElements[i].tagName.toLowerCase() == "td") {
					personRecordOwnerElements[i].style.display="table-cell";
				}
			}
		}
	} else {
		for (i=0; i < personRecordOwnerElements.length; i++){
			personRecordOwnerElements[i].style.display="none";
		}
		for (i=0; i < teamMemberRecordOwnerElements.length; i++){
			if (teamMemberRecordOwnerElements[i].tagName.toLowerCase() == "tr") {
				teamMemberRecordOwnerElements[i].style.display="table-row";
			} else if (teamMemberRecordOwnerElements[i].tagName.toLowerCase() == "td") {
				teamMemberRecordOwnerElements[i].style.display="table-cell";
			}
		}
		
		//instantiate the user auto complete
		instantiateProjectTeamMembersAutoCompleteForRecordSearch(option, teamMemberInputId, teamMemberAutoCompleteContainer, teamMemberIdHiddenFieldId);
		
		//start transaction for activities select
		AjaxObject.startRequest();
	} 
	
	//if any project or per organization options aren't selected, the activities list will be emptied

	if (option == -2 || option == 0) {
		//empty the activities select list
		activitiesSelect = document.getElementById("recordSearchForm_activity");
		var theSelect = '';
		theSelect += '<option title=\'' + 'RECORD_CHOOSE_PROJECT'.translate('COMMON') + ' \' value=\'-1\'>';
		theSelect += 'RECORD_CHOOSE_PROJECT'.translate('COMMON') + '</option>' ;
		activitiesSelect.innerHTML = theSelect;
		document.getElementById("recordSearchForm_activity_titleDiv").title = 'RECORD_CHOOSE_PROJECT'.translate('COMMON');
	}

	
}

//========================================================================================================
/**
 * @author Coni
 */
function manageProjectSelectInRecordForm(projectId, userInputId, userAutoCompleteContainer, userIdHiddenFieldId, projectsIsPm, personId, isUserAll, activityId, userName, forEdit, recordId) {
	isUserAll = eval ("(" + isUserAll + ")");
	var showBillable = false;
	var isCurrentProjectPm = false;		
	//if any option is selected
	if (projectId != -2) {
		//in case of an edit for a record set per organization, there is no projectId in the command object, so no project will be selected 
		// in the project select; so I have to select by default the 'per organization' option from the select (the second option)
		if (projectId == -1) {
			document.getElementById("recordForm_project").selectedIndex = 1;
		}
		
		
		
		//populate the activity select
		var AjaxObject = { 
				handleSuccess:function (o){
					if(o.responseText !== undefined){
						sessionExpired(o);
						oResults = eval("(" + o.responseText + ")");
						//activitiesSelect = document.getElementById("recordForm_activity");
						var theSelect = '';
						var label = '';
						theSelect += '<option title=\'' + 'RECORD_CHOOSE_ACTIVITY'.translate('COMMON') + '\' value=\'-1\'>' + 'RECORD_CHOOSE_ACTIVITY'.translate('COMMON') + '</option>' ;
						for (i = 0; i < oResults.length; i++) {
							theSelect += '<option title=\'' + oResults[i].name + '\' value=\'' + oResults[i].id + '\' >';
							
							if (activityId != null && activityId == oResults[i].id) 								
								label += oResults[i].name;				
								
							theSelect += oResults[i].name + '</option>';	
						}
						if (activityId == null) {
							
							activitiesSelect = document.getElementById("recordForm_activity");
							activitiesSelect.innerHTML = theSelect;
						}
						else {
							activitiesSelect = document.getElementById("label_activity");
							activitiesSelect.innerHTML = label;
						}
							
					}

				}, 
				handleFailure:function (o){
					alert("Error " + o.status + " : " + o.statusText);
			    },
				startRequest:function() {
					//make request
					YAHOO.util.Connect.asyncRequest('POST', 'GetProjectActivities.htm?projectId='+projectId, callback); 
			    }
			}; 
			var callback = {
				success:AjaxObject.handleSuccess, 
				failure:AjaxObject.handleFailure 
			};
			//start transaction
			AjaxObject.startRequest();
			projectsIsPm = eval("(" + projectsIsPm + ")");
			if (projectId > -1) {
				if (!isUserAll) {				
					if (projectsIsPm != null) {
						for (i = 0; i < projectsIsPm.length; i++) {
							if (projectsIsPm[i].projectId == projectId && projectsIsPm[i].isPm) {
								showBillable = true;
								isCurrentProjectPm = true;
							}
						}
					}					
					var userInputTds = getElementsByClass("recordUser", null, null);
					var userNotDisplayed = true;
					for (i = 0; i < userInputTds.length; i++) {			
						if (userInputTds[i].style.display == "table-cell" || userInputTds[i].style.display == "table-row") {
							userNotDisplayed = false;
						}
					}
					
					
				} else {
					showBillable = true;
					isCurrentProjectPm = true;
				}
			}
			//instantiate the user auto complete for the selected project
			instantiateUserAutoCompleteForRecordForm(projectId, userInputId, userAutoCompleteContainer, userIdHiddenFieldId, isUserAll, personId, isCurrentProjectPm, userName, projectsIsPm, forEdit, recordId);
	} else {
		//empty the user input, hide the user input and set the userId value to nothing
		document.getElementById("recordForm_user").value = "";
		var userInputTds = getElementsByClass("recordUser", null, null);
		for (i = 0; i < userInputTds.length; i++){		
			userInputTds[i].style.display = "none";
		}
		document.getElementById(userIdHiddenFieldId).value = "";
		
		//no activities to select
		activitiesSelect = document.getElementById("recordForm_activity");
		var theSelect = '';
		theSelect += '<option title=\'' + 'RECORD_CHOOSE_PROJECT'.translate('COMMON') + ' \' value=\'-1\'>';
		theSelect += 'RECORD_CHOOSE_PROJECT'.translate('COMMON') + '</option>' ;
		activitiesSelect.innerHTML = theSelect;
	}
	//display or hide the billable section, depending if the user is the selected project PM
	var billableTds = getElementsByClass("billable", null, null);
	
	if (showBillable) {		
		
		for (i = 0; i < billableTds.length; i++) {
			billableTds[i].style.display = "table-cell";
		}
		//check the billable option yes for work hours and overtime for a new project if they are not disabled
		if (document.recordForm.recordForm_workHoursRecord.checked == true) {
			if(document.recordForm.recordForm_billable_yes.checked == false && document.recordForm.recordForm_billable_no.checked == false) {			
				document.recordForm.recordForm_billable_yes.checked = true;
			}
		}
		if (document.recordForm.recordForm_overtimeRecord.checked == true) {
			if(document.recordForm.recordForm_overTime_billable_yes.checked == false && document.recordForm.recordForm_overTime_billable_no.checked == false) {			
				document.recordForm.recordForm_overTime_billable_yes.checked = true;
			}
		}
		//increment the tabIndex property for the elements that follow the billable selection
		document.getElementById("save").tabIndex += 4;
		document.getElementById("cancel").tabIndex += 4;
	} else {
		for (i = 0; i < billableTds.length; i++) {
			billableTds[i].style.display = "none";
		}
		document.getElementById("recordForm_billable_yes").checked = false;
		document.getElementById("recordForm_billable_no").checked = false;
		document.getElementById("recordForm_overTime_billable_yes").checked = false;
		document.getElementById("recordForm_overTime_billable_no").checked = false;
		//dencrement the tabIndex property for the elements that follow the billable selection
		document.getElementById("save").tabIndex -= 4;
		document.getElementById("cancel").tabIndex -= 4;
	}
}




/**
 * Instantiates the user auto complete for project select from the record form
 * @author Coni
 * @param projectId
 * @param userInputId
 * @param userAutoCompleteContainer
 * @param userIdHiddenFieldId
 * @param isUserAll
 * @param userPersonId
 * @param isCurrentProjectPm
 * @param userName
 * @param projectsIsPm
 * @return
 */
 function instantiateUserAutoCompleteForRecordForm(projectId, userInputId, userAutoCompleteContainer, userIdHiddenFieldId, isUserAll, userPersonId, isCurrentProjectPm, userName, projectsIsPm, forEdit, recordId) {
		var userInputTds = getElementsByClass("recordUser", null, null);
		isCurrentProjectPm = eval ("(" + isCurrentProjectPm + ")");
		
		if (isUserAll) {
			//always display the user input if it is USER_ALL
			for (i = 0; i < userInputTds.length; i++){
				if (userInputTds[i].tagName.toLowerCase() == "td") {
					userInputTds[i].style.display = "table-cell";
				} else if (userInputTds[i].tagName.toLowerCase() == "tr") {
					userInputTds[i].style.display = "table-row";
				}
			}
			//if the selected option is per organization, i will display by default the user name and set the userId value to the user personId
			if (projectId == -1) {
				if(!forEdit) {
					document.getElementById(userIdHiddenFieldId).value = userPersonId;
					document.getElementById("recordForm_user").value = userName;
				}
			} else {
				// only for the select action
				if(!forEdit) {
					//I will display by default the user name and set the userId value to the user personId, only if he is a member of the project
					if (projectsIsPm != null) {
						var userDisplayed = false;				
						for (i = 0; i < projectsIsPm.length; i++) {					
							if (projectId == projectsIsPm[i].projectId && projectsIsPm[i].memberId != null) {
								userDisplayed = true;
								document.getElementById("recordForm_user").value = userName;
								document.getElementById(userIdHiddenFieldId).value = projectsIsPm[i].memberId;
							}
						}				
					} else {
						userDisplayed = true;
					}
					
					if (userDisplayed == false) {
						document.getElementById("recordForm_user").value = "";
						document.getElementById(userIdHiddenFieldId).value = "";
					}		
				}
			}
		} else {
			//if the selected option is per organization, i won't display the user input and set the userId value to the user personId
			if (projectId == -1) {
				for (i = 0; i < userInputTds.length; i++){
					userInputTds[i].style.display = "none";
				}
				document.getElementById(userIdHiddenFieldId).value = userPersonId;
			} else {				
				//if the user is not the selected project PM, i won't display the user input and set the userId to the memberId for the selected project
				if (isCurrentProjectPm == false) {
					for (i = 0; i < userInputTds.length; i++){
						userInputTds[i].style.display = "none";
					}
					if(!forEdit) {
						if (projectsIsPm != null) {
							for (i = 0; i < projectsIsPm.length; i++) {
								if (projectId == projectsIsPm[i].projectId && projectsIsPm[i].memberId != null) {
									document.getElementById(userIdHiddenFieldId).value = projectsIsPm[i].memberId;
								}
							}
						}
					}
				} else {
					//if the user is the selected project PM, i will display the user input and set the userId to the memberId for the selected project
					for (i = 0; i < userInputTds.length; i++){
						if (userInputTds[i].tagName.toLowerCase() == "td") {
							userInputTds[i].style.display = "table-cell";
						} else if (userInputTds[i].tagName.toLowerCase() == "tr") {
							userInputTds[i].style.display = "table-row";
						}
					}
					if(!forEdit) {
						if (projectsIsPm != null) {
							for (i = 0; i < projectsIsPm.length; i++) {
								if (projectId == projectsIsPm[i].projectId && projectsIsPm[i].memberId != null) {
									document.getElementById("recordForm_user").value = userName;
									document.getElementById(userIdHiddenFieldId).value = projectsIsPm[i].memberId;
								}
							}
						}
					}
				}		
			}
		}
		
		var AjaxObject = { 
				handleSuccess:function (o){
					if(o.responseText !== undefined){
						sessionExpired(o);
						oResults = eval("(" + o.responseText + ")");
						oResults.sort(function (obj1, obj2) {
							return obj1.name < obj2.name ? -1 :
							(obj1.name > obj2.name ? 1 : 0);
							});
						if (YAHOO.ts.recordFormUsersAutoComplete) {
							YAHOO.ts.recordFormUsersAutoComplete.destroy();
						}
						oDS = new YAHOO.util.LocalDataSource(oResults);
						if (oDS != null){
							// Use a LocalDataSource
							oDS.responseSchema = {fields : ["name", "id"]};
							// Instantiate the AutoComplete 
							YAHOO.ts.recordFormUsersAutoComplete = new YAHOO.widget.AutoComplete(userInputId, userAutoCompleteContainer, oDS); 
							YAHOO.ts.recordFormUsersAutoComplete._bItemSelected = (YAHOO.ts.recordFormUsersAutoComplete._elTextbox.value != '');       
					    
							YAHOO.ts.recordFormUsersAutoComplete.resultTypeList = false; 
					    
							// enable string input contained
							YAHOO.ts.recordFormUsersAutoComplete.queryMatchContains = true;
						    
							// Show more results, scrolling is enabled via CSS
							YAHOO.ts.recordFormUsersAutoComplete.maxResultsDisplayed = 100;
					    
							// Enable force selection 
							YAHOO.ts.recordFormUsersAutoComplete.forceSelection = true;

							//oAC.prehighlightClassName = "yui-ac-prehighlight"; 
							YAHOO.ts.recordFormUsersAutoComplete.useShadow = true; 
					      
							//document.getElementById(userInputId).focus();
						 
							// Define an event handler to populate a hidden form field 
							// when an item gets selected 
							var myHiddenField = YAHOO.util.Dom.get(userIdHiddenFieldId); 
							var myHandler = function(sType, aArgs) { 
								var myAC = aArgs[0]; // reference back to the AC instance 
								var elLI = aArgs[1]; // reference to the selected LI element 
								var oData = aArgs[2]; // object literal of selected item's result data 
						         
								// update hidden form field with the selected item's ID
								myHiddenField.value = oData.id;
							};     
							YAHOO.ts.recordFormUsersAutoComplete.itemSelectEvent.subscribe(myHandler);
						   
							YAHOO.util.Event.addListener(userInputId, "click", function(){
								setTimeout(function() { // For IE 
									YAHOO.ts.recordFormUsersAutoComplete.sendQuery(" "); 
								},0);
							});
						}
					}
				}, 
				handleFailure:function (o){
					alert("Error " + o.status + " : " + o.statusText);
			    },
				startRequest:function() {
					//make request
					YAHOO.util.Connect.asyncRequest('POST', 'GetProjectTeamMembers.htm?projectId='+projectId+'&ACTION=GET_FROM_RECORD_FORM&recordId='+recordId, callback); 
			    }
			}; 
			var callback = {
				success:AjaxObject.handleSuccess, 
				failure:AjaxObject.handleFailure 
			};
			//start transaction
			AjaxObject.startRequest();
	}
//=========================================================================================================
/**
 * Instantiates the team member auto complete from the record search form when a project is selected
 * @author Coni
 */
function instantiateProjectTeamMembersAutoCompleteForRecordSearch(projectId, inputId, autoCompleteContainer, hiddenFieldId) {
	var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					sessionExpired(o);
					oResults = eval("(" + o.responseText + ")");
					if (YAHOO.ts.projectTeamMembersAutoComplete) {
						YAHOO.ts.projectTeamMembersAutoComplete.destroy();
					}
					oDS = new YAHOO.util.LocalDataSource(oResults);
					if (oDS != null){
						// Use a LocalDataSource
						oDS.responseSchema = {fields : ["name", "id"]};
						
						oResults.sort(function (obj1, obj2) {
							return obj1.name < obj2.name ? -1 :
							(obj1.name > obj2.name ? 1 : 0);
							});
						
						// Instantiate the AutoComplete 
						YAHOO.ts.projectTeamMembersAutoComplete = new YAHOO.widget.AutoComplete(inputId, autoCompleteContainer, oDS); 
				    
						YAHOO.ts.projectTeamMembersAutoComplete._bItemSelected = (YAHOO.ts.projectTeamMembersAutoComplete._elTextbox.value != '');       
				    
						YAHOO.ts.projectTeamMembersAutoComplete.resultTypeList = false; 
				    
						// enable string input contained
						YAHOO.ts.projectTeamMembersAutoComplete.queryMatchContains = true;
				    
						// Show more results, scrolling is enabled via CSS
						YAHOO.ts.projectTeamMembersAutoComplete.maxResultsDisplayed = 100;
				    
						// Enable force selection 
						YAHOO.ts.projectTeamMembersAutoComplete.forceSelection = true;

						//oAC.prehighlightClassName = "yui-ac-prehighlight"; 
						YAHOO.ts.projectTeamMembersAutoComplete.useShadow = true; 
				      
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
						YAHOO.ts.projectTeamMembersAutoComplete.itemSelectEvent.subscribe(myHandler);
				    
						YAHOO.util.Event.addListener(inputId, "click", function(){
							setTimeout(function() { // For IE 
								YAHOO.ts.projectTeamMembersAutoComplete.sendQuery(" "); 
							},0);
						});
					}
				}
			}, 
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
		    },
			startRequest:function() {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', 'GetProjectTeamMembers.htm?projectId='+projectId+'&ACTION=GET_FROM_SEARCH', callback); 
		    }
		}; 
		var callback = {
			success:AjaxObject.handleSuccess, 
			failure:AjaxObject.handleFailure 
		};
		//start transaction
		AjaxObject.startRequest();
}

/**
 * Used to ask when you click on add or back
 * 
 * @author Adelina
 * 
 * @param _url
 * @param _divId
 * @return
 */
function check_add(_url, _divId) {
	
	var url = this.url != null ? this.url : _url;
	var divId = this.container != null ? this.container: _divId;
	
	var confirmMessage = 'CONFIRM_ADD'.translate('COMMON');	
	
	if (document.getElementById('ERRORS_CONTAINER') != null) {
		getContentFromUrlDirect(url, divId);
		return;
	}
	
	
	//check if something has changed
	if(ENTITY.equals(OLD_ENTITY)){
		getContentFromUrlDirect(url, divId);
		return;
	}
	
   //function used to handle the case when the user chooses from the confirmation panel not to perform the action on the specific organization
   function handleNo(){
   	YAHOO.ts.confirmationForAdd.hide();
   	YAHOO.ts.confirmationForAdd.destroy();
	}
	
   //function used to handle the case when the user chooses from the confirmation panel to perform the action on the specific organization	
	function handleYes() {
		handleNo();
		getContentFromUrlDirect(url, divId);
	}
	
	//create and display the confirmation dialog
	YAHOO.ts.confirmationForAdd =  
		    new YAHOO.widget.SimpleDialog("confirmationForAdd",  
		             { width: "300px", 
		               fixedcenter: true, 
		               visible: false, 
		               draggable: true,
		               modal: true,
		               close: false,
		               //text: dialogText,
		               icon: YAHOO.widget.SimpleDialog.ICON_WARN, 
		               constraintoviewport: true,
		               buttons: [ 
		                          { text:'YES'.translate('COMMON'), handler: handleYes, isDefault:true },
		                          { text:'NO'.translate('COMMON'),  handler:handleNo } ] 
		             } ); 
	
	YAHOO.ts.confirmationForAdd.setBody(confirmMessage);
	YAHOO.ts.confirmationForAdd.render(document.body);
	YAHOO.ts.confirmationForAdd.show();
	
}
 
 /**
  * An activity element
  * 
  * @author Adelina
  * 
  * @param activityId
  * @param name
  * @param projectId
  * @param billable
  * @param costPrice
  * @param costPriceCurrency
  * @param costTimeUnit
  * @param billingPrice
  * @param billingPriceCurrency
  * @param billingTimeUnit
  * @param description
  * @return
  */
function activityElementWithAll(activityId, name, projectId, billable, costPrice, costPriceCurrency, costTimeUnit, billingPrice, billingPriceCurrency, billingTimeUnit, description) {
	
	this.activityId = activityId;
	this.name = name;
	this.projectId = projectId; 
	this.billable = billable;
	this.costPrice = costPrice;
	this.costPriceCurrency = costPriceCurrency;	
	this.costTimeUnit = costTimeUnit;	
	this.billingPrice = billingPrice; 
	this.billingPriceCurrency = billingPriceCurrency;
	this.billingTimeUnit = billingTimeUnit;	
	this.description = description;	
	
	this.equals = equals;	

	/**
	 * Test if the parameters of one activity equals to another one
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @return
	 */
	function equals(activity) {		
		if(this.activityId != activity.activityId) return false;
		if(this.name != activity.name) return false;
		if(this.projectId != activity.projectId) return false;		
		if(this.billable != activity.billable) return false;
		if(this.costPrice != activity.costPrice) return false;
		if(this.costPriceCurrency != activity.costPriceCurrency) return false;		
		if(this.costTimeUnit != activity.costTimeUnit) return false;
		if(this.billingPrice != activity.billingPrice) return false;
		if(this.billingPriceCurrency != activity.billingPriceCurrency) return false;
		if(this.billingTimeUnit != activity.billingTimeUnit) return false;		
		if(this.description != activity.description) return false;		
		
		return true;
	}
}
  
 /**
  * An activity element
  * 
  * @author Adelina
  * 
  * @param activityId
  * @param name
  * @param projectId
  * @param billable
  * @param costPrice
  * @param costPriceCurrency
  * @param costTimeUnit
  * @param description
  * @return
  */
 function activityElement(activityId, name, projectId, billable, costPrice, costPriceCurrency, costTimeUnit, description) {
		
		this.activityId = activityId;
		this.name = name;
		this.projectId = projectId; 
		this.billable = billable;
		this.costPrice = costPrice;
		this.costPriceCurrency = costPriceCurrency;	
		this.costTimeUnit = costTimeUnit;			
		this.description = description;	
		
		this.equals = equals;	

		/**
	 * Test if the parameters of one activity equals to another one
	 * 
	 * @author Adelina
	 * 
	 * @param activity
	 * @return
	 */
	function equals(activity) {		
		if(this.activityId != activity.activityId) return false;
		if(this.name != activity.name) return false;
		if(this.projectId != activity.projectId) return false;		
		if(this.billable != activity.billable) return false;
		if(this.costPrice != activity.costPrice) return false;
		if(this.costPriceCurrency != activity.costPriceCurrency) return false;		
		if(this.costTimeUnit != activity.costTimeUnit) return false;		
		if(this.description != activity.description) return false;		
		
		return true;
	}
}



/**
 * A project detail element
 * 
 * @author Adelina
 * 
 * @param projectDetailId
 * @param projectId
 * @param budget
 * @param notificationPercentage
 * @param completenessPercentage
 * @param observation
 * @return
 */
function projectDetailElement(projectDetailId, projectId, budget, budgetCurrency, notificationPercentage, completenessPercentage, observation) {
	
	this.projecDetailId = projectDetailId;
	this.projectId = projectId; 
	this.budget = budget;
	this.budgetCurrency = budgetCurrency;
	this.notificationPercentage = notificationPercentage;
	this.completenessPercentage = completenessPercentage;
	this.observation = observation;	
	
	this.equals = equals;	

	/**
	 * Test if the parameters of one project detail equals to another one
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetail
	 * @return
	 */
	function equals(projectDetial) {		
		if(this.projecDetailId != projectDetial.projecDetailId) return false;
		if(this.projectId != projectDetial.projectId) return false;
		if(this.budget != projectDetial.budget) return false;
		if(this.budgetCurrency != projectDetial.budgetCurrency) return false;
		if(this.notificationPercentage != projectDetial.notificationPercentage) return false;
		if(this.completenessPercentage != projectDetial.completenessPercentage) return false;
		if(this.observation != projectDetial.observation) return false;		
		
		return true;
	}
}
 
/**
 * A team member detail element
 * 
 * @author Adelina
 * 
 * @param teamMemberDetailId
 * @param teamMemberId
 * @param costPrice
 * @param costPriceCurrency
 * @param billingPrice
 * @param billingPriceCurrency
 * @param costTimeUnit
 * @param billingTimeUnit
 * @param overtimeCostPrice
 * @param overtimeCostCurrency
 * @param overtimeCostTimeUnit
 * @param overtimeBillingPrice
 * @param overtimeBillingCurrency
 * @param overtimeBillingTimeUnit
 * @param observation
 * @return
 */
function teamMemberDetailElement(teamMemberDetailId, teamMemberId, costPrice, costPriceCurrency, billingPrice, billingPriceCurrency, costTimeUnit, billingTimeUnit, 
								 overtimeCostPrice, overtimeCostCurrency, overtimeCostTimeUnit, overtimeBillingPrice, overtimeBillingCurrency, overtimeBillingTimeUnit, observation) {
	 
	this.teamMemberDetailId = teamMemberDetailId;
	this.teamMemberId = teamMemberId; 
	this.costPrice = costPrice;
	this.costPriceCurrency = costPriceCurrency;
	this.costTimeUnit = costTimeUnit;
	
	this.billingPrice = billingPrice;	
	this.billingPriceCurrency = billingPriceCurrency;	
	this.billingTimeUnit = billingTimeUnit;	
	
	this.overtimeCostPrice = overtimeCostPrice;
	this.overtimeCostCurrency = overtimeCostCurrency;
	this.overtimeCostTimeUnit = overtimeCostTimeUnit;
	
	this.overtimeBillingPrice = overtimeBillingPrice;
	this.overtimeBillingCurrency = overtimeBillingCurrency;
	this.overtimeBillingTimeUnit = overtimeBillingTimeUnit;
	this.observation = observation;	
	

	this.equals = equals;
	
	/**
	 * Test if the parameters of one team member detail equals to another one
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetail
	 * @return
	 */
	function equals(teamMemberDetial) {		
		
		if(this.teamMemberDetailId != teamMemberDetial.teamMemberDetailId) return false;
		if(this.teamMemberId != teamMemberDetial.teamMemberId) return false;
		
		if(this.costPrice != teamMemberDetial.costPrice) return false;		
		if(this.costPriceCurrency != teamMemberDetial.costPriceCurrency) return false;
		if(this.costTimeUnit != teamMemberDetial.costTimeUnit) return false;
		
		if(this.billingPrice != teamMemberDetial.billingPrice) return false;
		if(this.billingPriceCurrency != teamMemberDetial.billingPriceCurrency) return false;			
		if(this.billingTimeUnit != teamMemberDetial.billingTimeUnit) return false;
		
		if(this.overtimeCostPrice != teamMemberDetial.overtimeCostPrice) return false;
		if(this.overtimeCostCurrency != teamMemberDetial.overtimeCostCurrency) return false;
		if(this.overtimeCostTimeUnit != teamMemberDetial.overtimeCostTimeUnit) return false;
		
		if(this.overtimeBillingPrice != teamMemberDetial.overtimeBillingPrice) return false;
		if(this.overtimeBillingCurrency != teamMemberDetial.overtimeBillingCurrency) return false;
		if(this.overtimeBillingTimeUnit != teamMemberDetial.overtimeBillingTimeUnit) return false;
		
		if(this.observation != teamMemberDetial.observation) return false;		
		
		return true;
	}
}
 
/**
 * A person detail element
 * 
 * @author Adelina
 * 
 * @param personDetailId
 * @param personId
 * @param costPrice
 * @param costPriceCurrencyId
 * @param costTimeUnit
 * @param overtimeCostPrice
 * @param overtimeCostCurrencyId
 * @param overtimeCostTimeUnit
 * @param observation
 * @return
 */
 function personDetailElement(personDetailId, personId, costPrice, costPriceCurrencyId, costTimeUnit, overtimeCostPrice, overtimeCostCurrencyId, overtimeCostTimeUnit, observation) {
 	
 	this.personDetailId = personDetailId;
 	this.personId = personId; 
 	this.costPrice = costPrice;
 	this.costPriceCurrencyId = costPriceCurrencyId;
 	this.costTimeUnit = costTimeUnit;
 	this.overtimeCostPrice = overtimeCostPrice;	
 	this.overtimeCostCurrencyId = overtimeCostCurrencyId;
 	this.overtimeCostTimeUnit = overtimeCostTimeUnit;
 	this.observation = observation;	
 	 	
 	this.equals = equals;
 	
 	/**
 	 * Test if the parameters of one person detail equals to another one
 	 * 
 	 * @author Adelina
 	 * 
 	 * @param personDetail
 	 * @return
 	 */
 	function equals(personDetail) {		
 		if(this.personDetailId != personDetail.personDetailId) return false;
 		if(this.personId != personDetail.personId) return false;
 		if(this.costPrice != personDetail.costPrice) return false;
 		if(this.costPriceCurrency != personDetail.costPriceCurrency) return false;
 		if(this.costTimeUnit != personDetail.costTimeUnit) return false;
 		if(this.overtimeCostPrice != personDetail.overtimeCostPrice) return false;
 		if(this.overtimeCostCurrencyId != personDetail.overtimeCostCurrencyId) return false;	 		
 		if(this.overtimeCostTimeUnit != personDetail.overtimeCostTimeUnit) return false;
 		if(this.observation != personDetail.observation) return false;		
 		
 		return true;
 	}
 }


/**
* Function that takes the project detail metadata and returns a new object
* 
* @author Adelina`
* 
* @return
*/
function getProjectDetailData() {
	return new projectDetailElement(document.getElementById('projectDetailId').value, document.getElementById('projectId').value, document.getElementById('projectDetailsForm_budget').value, 
									document.getElementById('projectDetailsForm_budgetCurrency').value,document.getElementById('projectDetailsForm_notificationPercentage').value,
									document.getElementById('projectDetailsForm_completenessPercentage').value, document.getElementById('projectDetailsForm_observation').value);
									
}

/**
* Function that takes the team member detail metadata and returns a new object
* 
* @author Adelina
* 
* @return
*/
function getTeamMemberDetailData() {
	return new teamMemberDetailElement(document.getElementById('teamMemberDetailId').value, document.getElementById('teamMemberId').value, 
									   document.getElementById('teamMemberDetailForm_costPrice').value, document.getElementById('teamMemberDetailForm_costPriceCurrency').value, 
									   document.getElementById('teamMemberDetailForm_billingPrice').value, document.getElementById('teamMemberDetailForm_billingPriceCurrency').value,
									   document.getElementById('teamMemberDetailForm_costTimeUnit').value, document.getElementById('teamMemberDetailForm_billingTimeUnit').value,									   
									   document.getElementById('teamMemberDetailForm_overtimeCostPrice').value, document.getElementById('teamMemberDetailForm_overtimeCostCurrency').value, 
									   document.getElementById('teamMemberDetailForm_overtimeCostTimeUnit').value, document.getElementById('teamMemberDetailForm_overtimeBillingPrice').value,									   
									   document.getElementById('teamMemberDetailForm_overtimeBillingCurrency').value,  document.getElementById('teamMemberDetailForm_overtimeBillingTimeUnit').value,
									   document.getElementById('teamMemberDetailForm_observation').value);
}

/**
* Function that takes the person detail metadata and returns a new object
* 
* @author Adelina
* 
* @return
*/
function getPersonDetailData() {
	return new personDetailElement(document.getElementById('personDetailId').value, document.getElementById('personId').value, 
									   document.getElementById('personDetailForm_costPrice').value, document.getElementById('personDetailForm_costPriceCurrency').value, 
									   document.getElementById('personDetailForm_costTimeUnit').value, document.getElementById('personDetailForm_overtimeCostPrice').value, 
									   document.getElementById('personDetailForm_overtimeCostCurrency').value, document.getElementById('personDetailForm_overtimeCostTimeUnit').value,
									   document.getElementById('personDetailForm_observation').value);
}

/**
* Function that takes the activity metadata and returns a new object
* 
* @author Adelina
* 
* @return
*/
function getActivityData() {
	if(document.getElementById('activityForm_billingPrice') == null) {
		return new activityElement(document.getElementById('activityId').value, document.getElementById('activityForm_name').value, 
									   document.getElementById('activityForm_project').value, document.getElementById('activityForm_billable').value, 
									   document.getElementById('activityForm_costPrice').value, document.getElementById('activityForm_costPriceCurrency').value,
									   document.getElementById('activityForm_costTimeUnit').value, document.getElementById('activityForm_description').value);
	} else {
		return new activityElementWithAll(document.getElementById('activityId').value, document.getElementById('activityForm_name').value, 
				   document.getElementById('activityForm_project').value, document.getElementById('activityForm_billable').value, 
				   document.getElementById('activityForm_costPrice').value, document.getElementById('activityForm_costPriceCurrency').value,
				   document.getElementById('activityForm_costTimeUnit').value, document.getElementById('activityForm_billingPrice').value,
				   document.getElementById('activityForm_billingPriceCurrency').value, document.getElementById('activityForm_billingTimeUnit').value, 
				   document.getElementById('activityForm_description').value);
	}
}

/**
 * Function for managing the check/uncheck event on the work hours record form checkbox
 * @author Coni
 * @param e
 * @param dateInputsClass
 * @param toBeChecked
 * @return
 */
function manageWorkHoursInputDisplay(e, dateInputsClass, toBeChecked, todayDate){
	var dateElements = getElementsByClass(dateInputsClass, null, null);
	if (toBeChecked != null) {
		if (toBeChecked) {
			e.checked = true;
		} else {
			e.checked = false;
		}
	}
	
	function manageClickInRecordFormStartDate() {		
		document.getElementById('startCalendar').style.zIndex = 1;
		instantiateStartEndCalendars('startCalendar','recordForm_startTime', todayDate, 'startTime', true, 'recordFormStartWorkHours', document.getElementById('recordForm_endTime').value, true, '1');
		
	};
	function manageClickInRecordFormEndDate() {
		document.getElementById('endCalendar').style.zIndex = 1;
		instantiateStartEndCalendars('endCalendar','recordForm_endTime', todayDate, 'endTime', false, 'recordFormEndWorkHours', document.getElementById('recordForm_startTime').value, true, '1');
	};
	
	// for show input time hint
	var inputs = document.getElementById("recordForm_time");
	
	if (e.checked) {		
		
		// show input time hint 
		// test to see if the hint span exists first
		if (inputs.parentNode.getElementsByTagName("span")[0]) {			
			// the span exists!  on focus, show the hint
			inputs.onfocus = function () {										
				this.parentNode.getElementsByTagName("span")[0].style.display = "inline";
			}
			// when the cursor moves away from the field, hide the hint
			inputs.onblur = function () {
				this.parentNode.getElementsByTagName("span")[0].style.display = "none";
			}
		}
		
		for (var i = 0; i < dateElements.length; i++ ) {
			var cssClass = dateElements[i].className;
			var classes = cssClass.split(' ');
			dateElements[i].className = '';
			for (var j = 0; j < classes.length; j++) {
				if (classes[j] != 'inactive' && classes[j] != 'labelTdInactive') {
					if (dateElements[i].className == '') {
						dateElements[i].className = classes[j];
					} else {
						dateElements[i].className = dateElements[i].className + ' ' + classes[j];
					}
				}
			}
		}
		YAHOO.util.Event.addListener("recordForm_startTime", "click", manageClickInRecordFormStartDate, null, false);
		YAHOO.util.Event.addListener("recordForm_endTime", "click", manageClickInRecordFormEndDate, null, false);
					
		//show the required red stars			
		document.getElementById("timeRequired").style.display = "inline";
		
		if(document.getElementById("recordForm_startTime").value == '') {
			document.getElementById("startTimeRequired").style.display = "none";
		}
		if(document.getElementById("recordForm_endTime").value == '') {
			document.getElementById("endTimeRequired").style.display = "none";
		}
		
		//check the default billable value (yes)
		document.getElementById("recordForm_billable_yes").disabled = false;
		document.getElementById("recordForm_billable_no").disabled = false;	
		document.getElementById("recordForm_billable_yes").checked = true;
		document.getElementById("recordForm_time").readOnly = false;		
		
	} else {
		
		// hide input time hint 
		// test to see if the hint span exists first
		if (inputs.parentNode.getElementsByTagName("span")[0]) {			
			// the span exists!  on focus, show the hint
			inputs.onfocus = function () {										
				this.parentNode.getElementsByTagName("span")[0].style.display = "none";
			}
			// when the cursor moves away from the field, hide the hint
			inputs.onblur = function () {
				this.parentNode.getElementsByTagName("span")[0].style.display = "none";
			}
		}
				
		for (var i = 0; i < dateElements.length; i++ ) {
			//set the greyed out class for label td
			if (dateElements[i].className.indexOf('labelTd') != -1 && dateElements[i].className.indexOf('labelTdInactive') == -1) {
				dateElements[i].className = dateElements[i].className + ' ' + 'labelTdInactive';
			}
			//set the greyed out class for input td
			if (dateElements[i].className.indexOf('labelTd') == -1 && dateElements[i].className.indexOf('inactive') == -1) {
				dateElements[i].className = dateElements[i].className + ' ' + 'inactive';
			}
		}
		//remove the event listener on the date inputs
		YAHOO.util.Event.removeListener("recordForm_startTime", "click");
		YAHOO.util.Event.removeListener("recordForm_endTime", "click");
		document.getElementById("recordForm_startTime").value='';
		document.getElementById("recordForm_endTime").value='';
		document.getElementById("recordForm_time").value='';
		document.getElementById("recordForm_time").readOnly=true;
		
		//hide the required red stars		
		document.getElementById("timeRequired").style.display = "none";		
		document.getElementById("startTimeRequired").style.display = "none";				
		document.getElementById("endTimeRequired").style.display = "none";		
		
		//when the check box is unchecked i must hide the previously js error messages displayed for the date empty inputs after trying to save the record
		document.getElementById("startTime_message").innerHTML = "";
		document.getElementById("endTime_message").innerHTML = "";
		document.getElementById("time_message").innerHTML = "";
		
		//uncheck any billable option
		document.getElementById("recordForm_billable_yes").checked = false;
		document.getElementById("recordForm_billable_no").checked = false;
		
		document.getElementById("recordForm_billable_yes").disabled = true;
		document.getElementById("recordForm_billable_no").disabled = true;		
	}
}

/**
 * Function for managing the check/uncheck event on the overtime record form checkbox
 * @author Coni
 * @param e
 * @param dateInputsClass
 * @param toBeChecked
 * @return
 */
function manageOverTimeInputDisplay(e, dateInputsClass, toBeChecked, todayDate){
	var dateElements = getElementsByClass(dateInputsClass, null, null);
	if (toBeChecked != null) {
		if (toBeChecked) {
			e.checked = true;
		} else {
			e.checked = false;
		}
	}
	function manageClickInRecordFormOverTimeStartDate() {
		document.getElementById('overTimeStartCalendar').style.zIndex = 1;
		instantiateStartEndCalendars('overTimeStartCalendar','recordForm_overTimeStartTime', todayDate, 'overTimeStartTime', true, 'recordFormStartOverTime', document.getElementById('recordForm_overTimeEndTime').value, true, '2');
	};
	function manageClickInRecordFormOverTimeEndDate() {
		document.getElementById('overTimeEndCalendar').style.zIndex = 1;
		instantiateStartEndCalendars('overTimeEndCalendar','recordForm_overTimeEndTime', todayDate, 'overTimeEndTime', false, 'recordFormEndOverTime', document.getElementById('recordForm_overTimeStartTime').value, true, '2');
	};
	
	// for show input time hint
	var inputs = document.getElementById("recordForm_overtimeTime");
	
	if (e.checked) {
		
		// test to see if the hint span exists first
		if (inputs.parentNode.getElementsByTagName("span")[0]) {			
			// the span exists!  on focus, show the hint
			inputs.onfocus = function () {										
				this.parentNode.getElementsByTagName("span")[0].style.display = "inline";
			}
			// when the cursor moves away from the field, hide the hint
			inputs.onblur = function () {
				this.parentNode.getElementsByTagName("span")[0].style.display = "none";
			}
		}
						
		for (var i = 0; i < dateElements.length; i++ ) {
			var cssClass = dateElements[i].className;
			var classes = cssClass.split(' ');
			dateElements[i].className = '';
			for (var j = 0; j < classes.length; j++) {
				if (classes[j] != 'inactive' && classes[j] != 'labelTdInactive') {
					if (dateElements[i].className == '') {
						dateElements[i].className = classes[j];
					} else {
						dateElements[i].className = dateElements[i].className + ' ' + classes[j];
					}
				}
			}
		}
		YAHOO.util.Event.addListener("recordForm_overTimeStartTime", "click", manageClickInRecordFormOverTimeStartDate, null, false);
		YAHOO.util.Event.addListener("recordForm_overTimeEndTime", "click", manageClickInRecordFormOverTimeEndDate, null, false);
						
		//show the required red stars
		document.getElementById("overTimeTimeRequired").style.display = "inline";
		
		if(document.getElementById("recordForm_overTimeStartTime").value == '') {
			document.getElementById("overTimeStartTimeRequired").style.display = "none";
		}
		if(document.getElementById("recordForm_overTimeEndTime").value == '') {
			document.getElementById("overTimeEndTimeRequired").style.display = "none";
		}
					
		document.getElementById("recordForm_overtimeTime").readOnly = false;		
		
		//check the default billable value (yes)
		document.getElementById("recordForm_overTime_billable_yes").disabled = false;
		document.getElementById("recordForm_overTime_billable_no").disabled = false;
		document.getElementById("recordForm_overTime_billable_yes").checked = true;
	} else {
		
		// test to see if the hint span exists first
		if (inputs.parentNode.getElementsByTagName("span")[0]) {			
			// the span exists!  on focus, show the hint
			inputs.onfocus = function () {										
				this.parentNode.getElementsByTagName("span")[0].style.display = "none";
			}
			// when the cursor moves away from the field, hide the hint
			inputs.onblur = function () {
				this.parentNode.getElementsByTagName("span")[0].style.display = "none";
			}
		}
				
		for (var i = 0; i < dateElements.length; i++ ) {
			//set the greyed out class for label td
			if (dateElements[i].className.indexOf('labelTd') != -1 && dateElements[i].className.indexOf('labelTdInactive') == -1) {
				dateElements[i].className = dateElements[i].className + ' ' + 'labelTdInactive';
			}
			//set the greyed out class for input td
			if (dateElements[i].className.indexOf('labelTd') == -1 && dateElements[i].className.indexOf('inactive') == -1) {
				dateElements[i].className = dateElements[i].className + ' ' + 'inactive';
			}
		}
		YAHOO.util.Event.removeListener("recordForm_overTimeStartTime", "click");
		YAHOO.util.Event.removeListener("recordForm_overTimeEndTime", "click");
		document.getElementById("recordForm_overTimeStartTime").value='';
		document.getElementById("recordForm_overTimeEndTime").value='';
		document.getElementById("recordForm_overtimeTime").value='';
		document.getElementById("recordForm_overtimeTime").readOnly = true;
		
		//hide the required red stars
		document.getElementById("overTimeStartTimeRequired").style.display = "none";
		document.getElementById("overTimeEndTimeRequired").style.display = "none";
		document.getElementById("overTimeTimeRequired").style.display = "none";
		
		//when the check box is unchecked i must hide the previously js error messages displayed for the date empty inputs after trying to save the record
		document.getElementById("overTimeStartTime_message").innerHTML = "";
		document.getElementById("overTimeEndTime_message").innerHTML = "";
		document.getElementById("overTimeTime_message").innerHTML = "";
		
		//uncheck any billable option
		document.getElementById("recordForm_overTime_billable_yes").checked = false;
		document.getElementById("recordForm_overTime_billable_no").checked = false;
		
		document.getElementById("recordForm_overTime_billable_yes").disabled = true;
		document.getElementById("recordForm_overTime_billable_no").disabled = true;
	}
}

//========================================================================================================
/**
 * Record element
 * @author Coni
 */
function recordElement(recordId, userId, projectId, recordOwnerName, activityId, billable_yes, billable_no, workHoursRecord, overTimeRecord, startTime, endTime, overTimeStartTime, overTimeEndTime, observation, description) {
	this.recordId = recordId;
	this.userId = userId;
	this.projectId = projectId;
	this.recordOwnerName = recordOwnerName;
	this.activityId = activityId;
	this.billable_yes = billable_yes;
	this.billable_no = billable_no;
	this.workHoursRecord = workHoursRecord;
	this.overTimeRecord = overTimeRecord;
	this.startTime = startTime;
	this.endTime = endTime;
	this.overTimeStartTime = overTimeStartTime;
	this.overTimeEndTime = overTimeEndTime;
	this.observation = observation;
	this.description = description;
	
	this.equals = equals;
	
	/**
	 * Tests if the parameters of the record equals to those of another one
	 * @author Coni
	 * @param record
	 * @return
	 */
	function equals(record) {
		if (this.recordId != record.recordId) return false;
		if (this.userId != record.userId) return false;
		if (this.projectId != record.projectId) return false;
		if (this.recordOwnerName != record.recordOwnerName) return false;
		if (this.activityId != record.activityId) return false;
		if (this.billable_yes != record.billable_yes) return false;
		if (this.billable_no != record.billable_no) return false;
		if (this.workHoursRecord != record.workHoursRecord) return false;
		if (this.overTimeRecord != record.overTimeRecord) return false;
		if (this.startTime != record.startTime) return false;
		if (this.endTime != record.endTime) return false;
		if (this.overTimeStartTime != record.overTimeStartTime) return false;
		if (this.overTimeEndTime != record.overTimeEndTime) return false;
		if (this.observation != record.observation) return false;
		if (this.description != record.description) return false;
		
		return true;
	}
}

function getRecordData() {
	return new recordElement(document.getElementById("recordId").value, document.getElementById("userId").value, document.getElementById("recordForm_project").selectedIndex
			, document.getElementById("recordForm_user").value, document.getElementById("recordForm_activity").value, document.getElementById("recordForm_billable_yes").checked
			, document.getElementById("recordForm_billable_no").checked, document.getElementById("recordForm_workHoursRecord").checked, document.getElementById("recordForm_overtimeRecord").checked, document.getElementById("recordForm_startTime").value
			, document.getElementById("recordForm_endTime").value, document.getElementById("recordForm_overTimeStartTime").value, document.getElementById("recordForm_overTimeEndTime").value, document.getElementById("recordForm_observation").value 
			, document.getElementById("recordForm_description").value);

}

//========================================================================================================
/**
 * Manages the selection of an option from the project select in the record search form  
 * @author Coni
 */
function manageProjectSelectInCostSheetSearchForm(option, firstNameInputId, lastNameInputId, teamMemberInputId, personCostSheetReporterClass, teamMemberCostSheetReporterClass, displayNameInput, teamMemberAutoCompleteContainer, teamMemberIdHiddenFieldId, isUserAll) {
	isUserAll = eval ("(" + isUserAll + ")");
	
	var teamMemberCostSheetReporterElements = getElementsByClass(teamMemberCostSheetReporterClass, null, null);;
	var personCostSheetReporterElements = getElementsByClass(personCostSheetReporterClass, null, null);
	
	//empty previously entered/selected values
	if (document.getElementById(teamMemberIdHiddenFieldId) != null) {
		document.getElementById(teamMemberIdHiddenFieldId).value = -1;
	}
	if (document.getElementById(teamMemberInputId) != null) {
		document.getElementById(teamMemberInputId).value = "";
	}
	if (document.getElementById(firstNameInputId) != null) {
		document.getElementById(firstNameInputId).value = "";
	}
	if (document.getElementById(lastNameInputId) != null) {
		document.getElementById(lastNameInputId).value = "";
	}
	
	//the selected option is to search in all the cost sheets ->
	//the first name and last name inputs must be displayed
	if (option == -2) {
		for (i=0; i < teamMemberCostSheetReporterElements.length; i++){
			teamMemberCostSheetReporterElements[i].style.display="none";
		}
		for (i=0; i < personCostSheetReporterElements.length; i++){
			if (personCostSheetReporterElements[i].tagName.toLowerCase() == "tr") {
				personCostSheetReporterElements[i].style.display="table-row";
			} else if (personCostSheetReporterElements[i].tagName.toLowerCase() == "td") {
				personCostSheetReporterElements[i].style.display="table-cell";
			}
		}
	} else if (option == -1) { 		//search for records per organization -> the first name and last name must be displayed only if the user has the USER_ALL role
		for (i=0; i < teamMemberCostSheetReporterElements.length; i++){
			teamMemberCostSheetReporterElements[i].style.display="none";
		}
		if (isUserAll) {
			for (i=0; i < personCostSheetReporterElements.length; i++){
				if (personCostSheetReporterElements[i].tagName.toLowerCase() == "tr") {
					personCostSheetReporterElements[i].style.display="table-row";
				} else if (personCostSheetReporterElements[i].tagName.toLowerCase() == "td") {
					personCostSheetReporterElements[i].style.display="table-cell";
				}
			}
		} else {
			for (i=0; i < personCostSheetReporterElements.length; i++){
				personCostSheetReporterElements[i].style.display="none";
			}
		}
	} else if (option == 0) {		
		for (i=0; i < teamMemberCostSheetReporterElements.length; i++){
			teamMemberCostSheetReporterElements[i].style.display="none";
		}
		if (displayNameInput) {
			for (i=0; i < personCostSheetReporterElements.length; i++){
				if (personCostSheetReporterElements[i].tagName.toLowerCase() == "tr") {
					personCostSheetReporterElements[i].style.display="table-row";
				} else if (personCostSheetReporterElements[i].tagName.toLowerCase() == "td") {
					personCostSheetReporterElements[i].style.display="table-cell";
				}
			}
		}
	} else {
		for (i=0; i < personCostSheetReporterElements.length; i++){
			personCostSheetReporterElements[i].style.display="none";
		}
		for (i=0; i < teamMemberCostSheetReporterElements.length; i++){
			if (teamMemberCostSheetReporterElements[i].tagName.toLowerCase() == "tr") {
				teamMemberCostSheetReporterElements[i].style.display="table-row";
			} else if (teamMemberCostSheetReporterElements[i].tagName.toLowerCase() == "td") {
				teamMemberCostSheetReporterElements[i].style.display="table-cell";
			}
		}
		
		//instantiate the user auto complete
		instantiateProjectTeamMembersAutoCompleteForCostSheetSearch(option, teamMemberInputId, teamMemberAutoCompleteContainer, teamMemberIdHiddenFieldId);
		
	} 
	
}

//=========================================================================================================
/**
 * Instantiates the team member auto complete from the cost sheet search form when a project is selected
 * @author Coni
 */
function instantiateProjectTeamMembersAutoCompleteForCostSheetSearch(projectId, inputId, autoCompleteContainer, hiddenFieldId) {
	var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					sessionExpired(o);
					oResults = eval("(" + o.responseText + ")");
					if (YAHOO.ts.projectTeamMembersAutoComplete) {
						YAHOO.ts.projectTeamMembersAutoComplete.destroy();
					}
					oDS = new YAHOO.util.LocalDataSource(oResults);
					if (oDS != null){
						// Use a LocalDataSource
						oDS.responseSchema = {fields : ["name", "id"]};
						
						oResults.sort(function (obj1, obj2) {
							return obj1.name < obj2.name ? -1 :
							(obj1.name > obj2.name ? 1 : 0);
							});
						
						// Instantiate the AutoComplete 
						YAHOO.ts.projectTeamMembersAutoComplete = new YAHOO.widget.AutoComplete(inputId, autoCompleteContainer, oDS); 
				    
						YAHOO.ts.projectTeamMembersAutoComplete._bItemSelected = (YAHOO.ts.projectTeamMembersAutoComplete._elTextbox.value != '');       
				    
						YAHOO.ts.projectTeamMembersAutoComplete.resultTypeList = false; 
				    
						// enable string input contained
						YAHOO.ts.projectTeamMembersAutoComplete.queryMatchContains = true;
				    
						// Show more results, scrolling is enabled via CSS
						YAHOO.ts.projectTeamMembersAutoComplete.maxResultsDisplayed = 100;
				    
						// Enable force selection 
						YAHOO.ts.projectTeamMembersAutoComplete.forceSelection = true;

						//oAC.prehighlightClassName = "yui-ac-prehighlight"; 
						YAHOO.ts.projectTeamMembersAutoComplete.useShadow = true; 
				      
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
						YAHOO.ts.projectTeamMembersAutoComplete.itemSelectEvent.subscribe(myHandler);
				    
						YAHOO.util.Event.addListener(inputId, "click", function(){
							setTimeout(function() { // For IE 
								YAHOO.ts.projectTeamMembersAutoComplete.sendQuery(" "); 
							},0);
						});
					}
				}
			}, 
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
		    },
			startRequest:function() {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', 'GetProjectTeamMembers.htm?projectId='+projectId+'&ACTION=GET_FROM_SEARCH', callback); 				
		    }
		}; 
		var callback = {
			success:AjaxObject.handleSuccess, 
			failure:AjaxObject.handleFailure 
		};
		//start transaction
		AjaxObject.startRequest();
}

//========================================================================================================
/**
 * @author Coni
 */
function manageProjectSelectInCostSheetForm(projectId, userInputId, userAutoCompleteContainer, userIdHiddenFieldId, projectsIsPm, personId, isUserAll, userName, forEdit, costId) {
	isUserAll = eval ("(" + isUserAll + ")");
	var userInputTds = getElementsByClass("costSheetUser", null, null);
	var showBillable = false;
	var isCurrentProjectPm = false;
	//if any option is selected
	if (projectId != -2) {
		//in case of an edit for a cost sheet set per organization, there is no projectId in the command object, so no project will be selected 
		// in the project select; so I have to select by default the 'per organization' option from the select (the second option)
		if (projectId == -1) {
			document.getElementById("costSheetForm_project").selectedIndex = 1;
		}
		
		projectsIsPm = eval("(" + projectsIsPm + ")");
		if (projectId > -1) {
			if (!isUserAll) {
				if (projectsIsPm != null) {
					for (i = 0; i < projectsIsPm.length; i++) {
						if (projectsIsPm[i].projectId == projectId && projectsIsPm[i].isPm) {
							showBillable = true;
							isCurrentProjectPm = true;
						}
					}
				}
			} else {
				showBillable = true;
				isCurrentProjectPm = true;
			}
		}
		//instantiate the user auto complete for the selected project
		instantiateUserAutoCompleteForCostSheetForm(projectId, userInputId, userAutoCompleteContainer, userIdHiddenFieldId, isUserAll, personId, isCurrentProjectPm, userName, projectsIsPm, forEdit, costId);
	} else {
		//empty the user input, hide the user input and set the userId value to nothing
		document.getElementById("costSheetForm_user").value = "";
		var userInputTds = getElementsByClass("costSheetUser", null, null);
		for (i = 0; i < userInputTds.length; i++){
			userInputTds[i].style.display = "none";
		}
		document.getElementById(userIdHiddenFieldId).value = "";
	}
	
	//display or hide the billable section, depending if the user is the selected project PM
	var billableTds = getElementsByClass("billable", null, null);
	if (showBillable) {		
		if(document.costSheetForm.costSheetForm_billable_yes.checked == false && document.costSheetForm.costSheetForm_billable_no.checked == false) {			
			document.costSheetForm.costSheetForm_billable_yes.checked = true
		}				
		for (i = 0; i < billableTds.length; i++) {
			billableTds[i].style.display = "table-cell";			
		}
		
		//increment the tabIndex property for the elements that follow the billable selection
		var billableTabIndex = 6;
		if (userInputTds[0].style.display != "none") {
			document.getElementById("costSheetForm_user").tabIndex = 5;
			billableTabIndex = 7;
		} else {
			document.getElementById("costSheetForm_user").tabIndex = "";
		}
		document.getElementById("costSheetForm_activityName").tabIndex = billableTabIndex;
		document.getElementById("costSheetForm_billable_yes").tabIndex = billableTabIndex + 1;
		document.getElementById("costSheetForm_billable_no").tabIndex = billableTabIndex + 2;
		document.getElementById("costSheetForm_billingPrice").tabIndex = billableTabIndex + 3;
		document.getElementById("costSheetForm_billingPriceCurrency").tabIndex = billableTabIndex + 4;		
		document.getElementById("costSheetForm_description").tabIndex = billableTabIndex + 5;
		document.getElementById("costSheetForm_observation").tabIndex = billableTabIndex + 6;
		document.getElementById("save").tabIndex = billableTabIndex + 7;
		if (isUserAll) {
			document.getElementById("currencyPanel").tabIndex = billableTabIndex + 8;
		}
		document.getElementById("cancel").tabIndex = billableTabIndex + 9;

	} else {
		for (i = 0; i < billableTds.length; i++) {
			billableTds[i].style.display = "none";
		}		
		document.getElementById("costSheetForm_billable_yes").checked = false;
		document.getElementById("costSheetForm_billable_no").checked = false;
		document.getElementById("costSheetForm_billingPrice").value = '';
		document.getElementById("costSheetForm_billingPriceCurrency").selectedIndex = 0;
		
		//decrement the tabIndex property for the elements that follow the billable selection
		document.getElementById("costSheetForm_billable_yes").tabIndex = "";
		document.getElementById("costSheetForm_billable_no").tabIndex = "";
		document.getElementById("costSheetForm_billingPrice").tabIndex = "";
		document.getElementById("costSheetForm_billingPriceCurrency").tabIndex = "";
		var activityIndex = 5;
		if (userInputTds[0].style.display != "none") {
			document.getElementById("costSheetForm_user").tabIndex = 5;
			activityIndex = 6;
		} else {
			document.getElementById("costSheetForm_user").tabIndex = "";
		}
		document.getElementById("costSheetForm_activityName").tabIndex = activityIndex;
		document.getElementById("costSheetForm_description").tabIndex = activityIndex + 1;
		document.getElementById("costSheetForm_observation").tabIndex = activityIndex + 2;
		document.getElementById("save").tabIndex = activityIndex + 3;
		if (isUserAll) {
			document.getElementById("currencyPanel").tabIndex = activityIndex + 4;
		}
		document.getElementById("cancel").tabIndex = activityIndex + 5;
	}
}

/**
 * Instantiates the user auto complete for project select from the cost sheet form
 * @author Coni
 * @param projectId
 * @param userInputId
 * @param userAutoCompleteContainer
 * @param userIdHiddenFieldId
 * @param displayUserInput
 * @param isUserAll
 * @param userPersonId
 * @param isCurrentProjectPm
 * @return
 */
function instantiateUserAutoCompleteForCostSheetForm(projectId, userInputId, userAutoCompleteContainer, userIdHiddenFieldId, isUserAll, userPersonId, isCurrentProjectPm, userName, projectsIsPm, forEdit, costId) {
	var userInputTds = getElementsByClass("costSheetUser", null, null);
	isCurrentProjectPm = eval ("(" + isCurrentProjectPm + ")");
	
	if (isUserAll) {
		//always display the user input if it is USER_ALL
		for (i = 0; i < userInputTds.length; i++){
			userInputTds[i].style.display = "table-row";
		}
		//if the selected option is per organization, i will display by default the user name and set the userId value to the user personId
		if (projectId == -1) {
			if(!forEdit) {
				document.getElementById(userIdHiddenFieldId).value = userPersonId;
				document.getElementById("costSheetForm_user").value = userName;
			}
		} else {			
			if(!forEdit) {
				//I will display by default the user name and set the userId value to the user personId, only if he is a member of the project
				if (projectsIsPm != null) {			
					var userDisplayed = false;
					for (i = 0; i < projectsIsPm.length; i++) {														
						if (projectId == projectsIsPm[i].projectId && projectsIsPm[i].memberId != null) {
							userDisplayed = true;						
							document.getElementById("costSheetForm_user").value = userName;						
							document.getElementById(userIdHiddenFieldId).value = projectsIsPm[i].memberId;
						} 
					}				
				} else {				
					userDisplayed = true;
				}
				if (userDisplayed == false) {
					document.getElementById("costSheetForm_user").value = "";
					document.getElementById(userIdHiddenFieldId).value = "";
				}
			}
		}
	} else {
		//if the selected option is per organization, i won't display the user input and set the userId value to the user personId
		if (projectId == -1) {
			for (i = 0; i < userInputTds.length; i++){
				userInputTds[i].style.display = "none";
			}
			document.getElementById(userIdHiddenFieldId).value = userPersonId;
		} else {			
			//if the user is not the selected project PM, i won't display the user input and set the userId to the memberId for the selected project
			if (isCurrentProjectPm == false) {
				for (i = 0; i < userInputTds.length; i++){
					userInputTds[i].style.display = "none";
				}
				if(!forEdit) {
					if (projectsIsPm != null) {
						for (i = 0; i < projectsIsPm.length; i++) {
							if (projectId == projectsIsPm[i].projectId && projectsIsPm[i].memberId != null) {
								document.getElementById(userIdHiddenFieldId).value = projectsIsPm[i].memberId;
							}
						}
					}
				}
			} else {
				//if the user is the selected project PM, i will display the user input and set the userId to the memberId for the selected project
				for (i = 0; i < userInputTds.length; i++){
					userInputTds[i].style.display = "table-row";
				}
				if(!forEdit) {
					if (projectsIsPm != null) {
						for (i = 0; i < projectsIsPm.length; i++) {
							if (projectId == projectsIsPm[i].projectId && projectsIsPm[i].memberId != null) {
								document.getElementById("costSheetForm_user").value = userName;
								document.getElementById(userIdHiddenFieldId).value = projectsIsPm[i].memberId;
							}
						}
					}
				}
			}			
		}
	}

	var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					sessionExpired(o);
					oResults = eval("(" + o.responseText + ")");
					if (YAHOO.ts.costSheetFormUsersAutoComplete) {
						YAHOO.ts.costSheetFormUsersAutoComplete.destroy();
					}
					oDS = new YAHOO.util.LocalDataSource(oResults);
					if (oDS != null){
						// Use a LocalDataSource
						oDS.responseSchema = {fields : ["name", "id"]};
						
						oResults.sort(function (obj1, obj2) {
							return obj1.name < obj2.name ? -1 :
							(obj1.name > obj2.name ? 1 : 0);
							});
						
						// Instantiate the AutoComplete 
						YAHOO.ts.costSheetFormUsersAutoComplete = new YAHOO.widget.AutoComplete(userInputId, userAutoCompleteContainer, oDS); 
				    
						YAHOO.ts.costSheetFormUsersAutoComplete._bItemSelected = (YAHOO.ts.costSheetFormUsersAutoComplete._elTextbox.value != '');       
				    
						YAHOO.ts.costSheetFormUsersAutoComplete.resultTypeList = false; 
				    
						// enable string input contained
						YAHOO.ts.costSheetFormUsersAutoComplete.queryMatchContains = true;
					    
						// Show more results, scrolling is enabled via CSS
						YAHOO.ts.costSheetFormUsersAutoComplete.maxResultsDisplayed = 100;
					    
						// Enable force selection 
						YAHOO.ts.costSheetFormUsersAutoComplete.forceSelection = true;
	
						//oAC.prehighlightClassName = "yui-ac-prehighlight"; 
						YAHOO.ts.costSheetFormUsersAutoComplete.useShadow = true; 
					      
						//document.getElementById(userInputId).focus();
					 
						// Define an event handler to populate a hidden form field 
						// when an item gets selected 
						var myHiddenField = YAHOO.util.Dom.get(userIdHiddenFieldId); 
						var myHandler = function(sType, aArgs) { 
							var myAC = aArgs[0]; // reference back to the AC instance 
							var elLI = aArgs[1]; // reference to the selected LI element 
							var oData = aArgs[2]; // object literal of selected item's result data 
					         
							// update hidden form field with the selected item's ID
							myHiddenField.value = oData.id;
						};     
						YAHOO.ts.costSheetFormUsersAutoComplete.itemSelectEvent.subscribe(myHandler);
					    
						YAHOO.util.Event.addListener(userInputId, "click", function(){
							setTimeout(function() { // For IE 
								YAHOO.ts.costSheetFormUsersAutoComplete.sendQuery(" "); 
							},0);
						});
					}
				}
			}, 
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
		    },
			startRequest:function() {
				//make request
				YAHOO.util.Connect.asyncRequest('POST', 'GetProjectTeamMembers.htm?projectId='+projectId+'&ACTION=GET_FROM_COST_FORM&costId='+costId, callback);
		    }
		}; 
		var callback = {
			success:AjaxObject.handleSuccess, 
			failure:AjaxObject.handleFailure 
		};
		//start transaction
		AjaxObject.startRequest();

}

//========================================================================================================
/**
 * CostSheet element
 * @author Coni
 */
function costSheetElement(costSheetId, userId, projectId, costSheetReporterName, activityName, billable_yes, billable_no, observation, description, date, costPrice, costPriceCurrency, billingPrice, billingPriceCurrency) {
	this.costSheetId = costSheetId;
	this.userId = userId;
	this.projectId = projectId;
	this.costSheetReporterName = costSheetReporterName;
	this.activityName = activityName;
	this.billable_yes = billable_yes;
	this.billable_no = billable_no;
	this.observation = observation;
	this.description = description;
	this.date = date;
	this.costPrice = costPrice;
	this.costPriceCurrency = costPriceCurrency;
	this.billingPrice = billingPrice;
	this.billingPriceCurrency = billingPriceCurrency
	
	this.equals = equals;
	
	/**
	 * Tests if the parameters of the cost sheet equals to those of another one
	 * @author Coni
	 * @param costSheet
	 * @return
	 */
	function equals(costSheet) {
		if (this.costSheetId != costSheet.costSheetId) return false;
		if (this.userId != costSheet.userId) return false;
		if (this.projectId != costSheet.projectId) return false;
		if (this.costSheetReporterName != costSheet.costSheetReporterName) return false;
		if (this.activityName != costSheet.activityName) return false;
		if (this.billable_yes != costSheet.billable_yes) return false;
		if (this.billable_no != costSheet.billable_no) return false;
		if (this.observation != costSheet.observation) return false;
		if (this.description != costSheet.description) return false;
		if (this.date != costSheet.date) return false;
		if (this.costPrice != costSheet.costPrice) return false;
		if (this.costPriceCurrency != costSheet.costPriceCurrency) return false;
		if (this.billingPrice != costSheet.billingPrice) return false;
		if (this.billingPriceCurrency != costSheet.billingPriceCurrency) return false;
		
		return true;
	}
}

/**
 * @author Coni
 * @return
 */
function getCostSheetData() {
	return new costSheetElement(document.getElementById("costSheetId").value, document.getElementById("userId").value, document.getElementById("costSheetForm_project").selectedIndex
			, document.getElementById("costSheetForm_user").value, document.getElementById("costSheetForm_activityName").value, document.getElementById("costSheetForm_billable_yes").checked, document.getElementById("costSheetForm_billable_no").checked
			, document.getElementById("costSheetForm_observation").value, document.getElementById("costSheetForm_description").value, document.getElementById("costSheetForm_date").value, document.getElementById("costSheetForm_costPrice").value
			, document.getElementById("costSheetForm_costPriceCurrency").selectedIndex, document.getElementById("costSheetForm_billingPrice").value, document.getElementById("costSheetForm_billingPriceCurrency").selectedIndex);

}

/**
 * @author Coni
 * @param currencyId
 * @param name
 * @param initials
 * @return
 */
function currencyElement(currencyId, name, initials) {
	this.currencyId = currencyId;
	this.name = name;
	this.initials = initials;
	
	this.equals = equals;
	
	/**
	 * Tests if the parameters of the currency equals to those of another one
	 * @author Coni
	 * @param currency
	 * @return
	 */
	function equals(currency) {
		if (this.currencyId != currency.currencyId) return false;
		if (this.name != currency.name) return false;
		if (this.initials != currency.initials) return false;
		
		return true;
	}
}

/**
 * @author Coni
 * @return
 */
function getCurrencyData() {
	return new currencyElement(document.getElementById("currencyId").value, document.getElementById("currencyForm_name").value, document.getElementById("currencyForm_initials").value);
}

/**
 * @author Coni
 * @param exchangeId
 * @param projectId
 * @param firstCurrencyId
 * @param secondCurrencyId
 * @param rate
 * @return
 */
function exchangeElement(exchangeId, projectId, firstCurrencyId, secondCurrencyId, rate) {
	this.exchangeId = exchangeId;
	this.projectId = projectId;
	this.firstCurrencyId = firstCurrencyId;
	this.secondCurrencyId = secondCurrencyId;
	this.rate = rate;
	
	this.equals = equals;
	
	function equals(exchange) {
		if (this.exchangeId != exchange.exchangeId) return false;
		if (this.projectId != exchange.projectId) return false;
		if (this.firstCurrencyId != exchange.firstCurrencyId) return false;
		if (this.secondCurrencyId != exchange.secondCurrencyId)	return false;
		if (this.rate != exchange.rate) return false;
		
		return true;
	}
}


function getExchangeData() {
	return new exchangeElement(document.getElementById("exchangeId").value, document.getElementById("exchangeForm_project").selectedIndex, document.getElementById("exchangeForm_firstCurrency").selectedIndex
			, document.getElementById("exchangeForm_secondCurrency").selectedIndex, document.getElementById("exchangeForm_rate").value);
}

//========================================================================================================
/**
 * Notification Settings Element
 * @author alexandru.dobre
 */
function notificationSettingsElement(recordAddId, recordDeleteId, recordUpdateId, costAddId, costDeleteId, costUpdateId, teamMemberDetailUpdateId, percentageOverflowId, budgetOverflowId, personDetailUpdateId,
									activityAddId, activityDeleteId, activityUpdateId, exchangeAddId, exchangeDeleteId, exchangeUpdateId,projectDetailUpdateId){
	
	this.recordAddId	= recordAddId;
	this.recordDeleteId	= recordDeleteId;
	this.recordUpdateId = recordUpdateId;
	this.costAddId = costAddId;
	this.costDeleteId = costDeleteId;
	this.costUpdateId = costUpdateId; 
	this.teamMemberDetailUpdateId = teamMemberDetailUpdateId;
	this.percentageOverflowId = percentageOverflowId;
	this.budgetOverflowId = budgetOverflowId;
	this.personDetailUpdateId = personDetailUpdateId; 
	
	this.activityAddId	= activityAddId;
	this.activityDeleteId	= activityDeleteId;
	this.activityUpdateId = activityUpdateId;
	this.exchangeAddId = exchangeAddId;
	this.exchangeDeleteId = exchangeDeleteId;
	this.exchangeUpdateId = exchangeUpdateId;
	
	this.projectDetailUpdateId = projectDetailUpdateId;
	
	this.equals = equals;
	
	/**
	 * Tests if the parameters of the Notification Settings Element equals to those of another one
	 * @author alexandru.dobre
	 * @param notificationSettings
	 * @return
	 */
	function equals(notificationSettings) {
	
		if (this.recordAddId	!= notificationSettings.recordAddId) return false;
		if (this.recordDeleteId	!= notificationSettings.recordDeleteId) return false;
		if (this.recordUpdateId != notificationSettings.recordUpdateId) return false;
		if (this.costAddId != notificationSettings.costAddId) return false;
		if (this.costDeleteId != notificationSettings.costDeleteId) return false;
		if (this.costUpdateId != notificationSettings.costUpdateId) return false;
		
		if (this.teamMemberDetailUpdateId  != notificationSettings.teamMemberDetailUpdateId) return false;
		if (this.percentageOverflowId != notificationSettings.percentageOverflowId) return false;
		if (this.budgetOverflowId != notificationSettings.budgetOverflowId) return false;
		
		if (this.personDetailUpdateId != notificationSettings.personDetailUpdateId) return false;
		
		if (this.activityAddId	!= notificationSettings.activityAddId) return false;
		if (this.activityDeleteId	!= notificationSettings.activityDeleteId) return false;
		if (this.activityUpdateId != notificationSettings.activityUpdateId) return false;
		if (this.exchangeAddId != notificationSettings.exchangeAddId) return false;
		if (this.exchangeDeleteId != notificationSettings.exchangeDeleteId) return false;
		if (this.exchangeUpdateId != notificationSettings.exchangeUpdateId) return false;
		
		if (this.projectDetailUpdateId != notificationSettings.projectDetailUpdateId) return false;	
		
		return true;
	}
}

function getNotificationSettingsData() {
	return new notificationSettingsElement( 
			document.getElementById("notificationSettingsForm_recordAdd")!=null?document.getElementById("notificationSettingsForm_recordAdd").checked:null,
			document.getElementById("notificationSettingsForm_recordDelete")!=null?document.getElementById("notificationSettingsForm_recordDelete").checked:null,
			document.getElementById("notificationSettingsForm_recordUpdate")!=null?document.getElementById("notificationSettingsForm_recordUpdate").checked:null,
			document.getElementById("notificationSettingsForm_costAdd")!=null?document.getElementById("notificationSettingsForm_costAdd").checked:null,
			document.getElementById("notificationSettingsForm_costDelete")!=null?document.getElementById("notificationSettingsForm_costDelete").checked:null,
			document.getElementById("notificationSettingsForm_costUpdate")!=null?document.getElementById("notificationSettingsForm_costUpdate").checked:null,
			document.getElementById("notificationSettingsForm_teamMemberDetailUpdate")!=null?document.getElementById("notificationSettingsForm_teamMemberDetailUpdate").checked:null,
			document.getElementById("notificationSettingsForm_percentageOverflow")!=null?document.getElementById("notificationSettingsForm_percentageOverflow").checked:null,
			document.getElementById("notificationSettingsForm_budgetOverflow")!=null?document.getElementById("notificationSettingsForm_budgetOverflow").checked:null,
			document.getElementById("notificationSettingsForm_personDetailUpdate")!=null?document.getElementById("notificationSettingsForm_personDetailUpdate").checked:null,
					
			document.getElementById("notificationSettingsForm_activityAdd")!=null?document.getElementById("notificationSettingsForm_activityAdd").checked:null,
			document.getElementById("notificationSettingsForm_activityDelete")!=null?document.getElementById("notificationSettingsForm_activityDelete").checked:null,
			document.getElementById("notificationSettingsForm_activityUpdate")!=null?document.getElementById("notificationSettingsForm_activityUpdate").checked:null,
			
			document.getElementById("notificationSettingsForm_exchangeAdd")!=null?document.getElementById("notificationSettingsForm_exchangeAdd").checked:null,
			document.getElementById("notificationSettingsForm_exchangeDelete")!=null?document.getElementById("notificationSettingsForm_exchangeDelete").checked:null,
			document.getElementById("notificationSettingsForm_exchangeUpdate")!=null?document.getElementById("notificationSettingsForm_exchangeUpdate").checked:null,
					
			document.getElementById("notificationSettingsForm_projectDetailUpdate")!=null?document.getElementById("notificationSettingsForm_projectDetailUpdate").checked:null);	
							

}



function computeReverseRate(rate, reverseRateId) {
	if (rate != "" && parseFloat(rate) != 0) {
		var reverseRate = new Number(1.0/rate);
		reverseRate = Math.ceil(reverseRate * 10000.0)/10000.0;
		reverseRate = reverseRate.toString();
		if (reverseRate.indexOf(".") != -1) {
			numberOfFractionDigits = reverseRate.length - reverseRate.substr(0, (reverseRate.indexOf("."))+1).length;
			for (var i = 0; i < (4 - numberOfFractionDigits); i++) {
				reverseRate += "0";
			}
		}
		document.getElementById(reverseRateId).value = reverseRate;
	}
}

function showExistingExchangePanel(_url, _formId, _divId) {
	var url = this.url != null ? this.url : _url;
	var divId = this.container != null ? this.container: _divId;
	var formId = this.container != null ? this.container: _formId;
	
	var confirmMessage = 'EXCHANGEFORM_CONFIRM_UPDATE_EXISTING_EXCHANGE'.translate('COMMON');		
	
   //function used to handle the case when the user chooses from the confirmation panel not to overwrite the existing exchange
   function handleNo(){
   	YAHOO.ts.confirmationForOverwriteExistingExchange.hide();
   	YAHOO.ts.confirmationForOverwriteExistingExchange.destroy();
	}
	
   //function used to handle the case when the user chooses from the confirmation panel to overwrite the existing exchange
	function handleYes() {
		handleNo();
		submitForm(url, formId, divId);
	}
	
	//create and display the confirmation dialog
	YAHOO.ts.confirmationForOverwriteExistingExchange =  
		    new YAHOO.widget.SimpleDialog("confirmationForOverwriteExistingExchange",  
		             { width: "300px", 
		               fixedcenter: true, 
		               visible: false, 
		               draggable: true,
		               modal: true,
		               close: false,
		               //text: dialogText,
		               icon: YAHOO.widget.SimpleDialog.ICON_WARN, 
		               constraintoviewport: true,
		               buttons: [ 
		                          { text:'YES'.translate('COMMON'), handler: handleYes, isDefault:true },
		                          { text:'NO'.translate('COMMON'),  handler:handleNo } ] 
		             } ); 
	
	YAHOO.ts.confirmationForOverwriteExistingExchange.setBody(confirmMessage);
	YAHOO.ts.confirmationForOverwriteExistingExchange.render(document.body);
	YAHOO.ts.confirmationForOverwriteExistingExchange.show();
}

/**
* Restricts the user to input only numbers with at most 4 decimals, 
* 
* @author Coni
* 
* @param e
* @return
*/
function restrictCharactersForExchangeRate(rateInputId) {
	var inputRateTillNow = document.getElementById(rateInputId).value;
	if (inputRateTillNow != "" && !inputRateTillNow.match(/^[0-9]{1,10}([.][0-9]{0,4})?$/)) {
		document.getElementById(rateInputId).value = OLD_RATE;
	} else {
		OLD_RATE = inputRateTillNow;
	}
}

/**
 * @author Coni
 * @param billable
 * @param overTimeBillable
 * @return
 */
function manageCheckBillableForRecordBean(billable, overTimeBillable) {
	//only for edit case
	if (billable != null) {
		if (billable == 'Y') {
			document.getElementById("recordForm_billable_yes").checked = true;
		} else if (billable == 'N') {
			document.getElementById("recordForm_billable_no").checked = true;
		}
	} 
	
	if (overTimeBillable != null) {
		if (overTimeBillable == 'Y') {
			document.getElementById("recordForm_overTime_billable_yes").checked = true;
		} else if (overTimeBillable == 'N') {
			document.getElementById("recordForm_overTime_billable_no").checked = true;
		}
	} 

}

/**
 * Appends a number of 0 characters at the end of the rate value in order to 
 * set a 4 fraction digits real number
 * @author Coni
 * @param rateInputId
 * @return
 */
function setExchangeRateFormat(rateInputId) {
	var rate = document.getElementById(rateInputId).value;
	if ((rate * 10) % 10 != 0) {
		numberOfFractionDigits = rate.length - rate.substr(0, (rate.indexOf("."))+1).length;
		for (var i = 0; i < (4 - numberOfFractionDigits); i++) {
			document.getElementById(rateInputId).value += "0";
		}
	} else {
		document.getElementById(rateInputId).value = rate * 10 / 10;
	}
}

/**
 * Populates the elements on the Project Report form with the appropriate default values
 * @param defValuesJSON
 * @param language
 * @return
 */


function populateDefaultValuesProjectReport (defValuesJSON,language){
	//display the default selected columns and the report title
	populateDefaultValuesForReportParams(defValuesJSON, language);
	//we display the default columns
	displayColumnTextInputs ('recordColumnsForm_select2', 'recordInputs',true);
	displayColumnTextInputs ('costColumnsForm_select2', 'costInputs',true);
}


/**
 * @author Coni
 * @param defValuesJSON
 * @param language
 * @return
 */
function populateDefaultValuesTimeSheetReport(defValuesJSON, language) {
	//display the default selected columns and the report title
	populateDefaultValuesForReportParams(defValuesJSON, language);
	//we display the column names inputs for the selected columns
	displayColumnTextInputs ('reportTimeSheetForm_select2', 'recordsColumnNamesInputs',true);
}

/**
 * Populates the default values for 
 * @author Coni
 * @param defValuesJSON
 * @param language
 * @return
 */
function populateDefaultValuesForReportParams(defValuesJSON, language) {
	var langPacks = YAHOO.lang.JSON.parse(defValuesJSON);
	
	for (var i = 0; i < langPacks.length; i++){
		if (langPacks[i].language.toLowerCase()== language.toLowerCase()){
			for (var j = 0; j < langPacks[i].defaultValues.length; j++){
				var kvPair = langPacks[i].defaultValues[j];
				var domObject = document.getElementById(kvPair.key);
				
				if (domObject != null){
					domObject.value= kvPair.value;
					domObject.title= kvPair.value;
					if (domObject.type != 'text') {
						domObject.innerHTML=kvPair.value;
					}
				}					
			}
			break;
		}
	}
}

/**
 * Moves options form Source Select to Destination Select. The two selects has to be identical:
 * 		- same option id and label
 * 
 * It is a particular implementation for the case when a user has to set a default column value
 * 
 * @author alexandru.dobre
 * 
 * @param sourceSelectId Id of the source select
 * @param destSelectBId  Id of the destination select
 * @param remove If false  the function will handle the Add case, else the Remove
 * @return
 */
function moveSelectOptionsSourceDestColumn(sourceSelectId, destSelectBId, remove, inputsContainerId){
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
		var columnsAdded =  new Array();
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
					
					if (!remove) sourceSelect.removeChild(selected[i]);
				} else{
					// already present
					if(!remove){
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
						
				if (!remove) sourceSelect.removeChild(selected[i]);
			}
		}
				
		
		if (!remove) {
			displayColumnTextInputs(destSelect.id,inputsContainerId, false);
		} else {
			displayColumnTextInputs(sourceSelect.id,inputsContainerId, false);
		}			
		
} 
//========================================================================================================
/**
 * It looks at the options present in a multiple select then for each option generates a text input
 * inside the designated container. If the input already exists with a custom value then the value is kept 
 * @author alexandru.dobre
 * @param selectId the select object to inspect
 * @param inputContainerId the input container to write in
 * @param overWriteCustom ignore custom values
 */


function displayColumnTextInputs(selectId, inputContainerId, overWriteCustom){
	
	var select = document.getElementById(selectId);

	var toDisplay  = "<table style=\"width:880px\">";
	
	for (i = 0; i < select.options.length; i++) {
		var optionId = select.options[i].id;
		var optionLabel = select.options[i].value;
		var optionText = null;
		//we need to check if the text input associated with the option is already present
		if (document.getElementById("columnInput_"+optionId)!= null && !overWriteCustom){
			//we retain the text inside the input 
			optionText = document.getElementById("columnInput_"+optionId).value;
		}else{
			//we get the def value 
			optionText = select.options[i].value;
		}
		
		//build the text input HTML
		var cells='<td class=\'labelTd\' style=\'width:190px\'> <span id=\''+"columnLabel_"+optionId+ '\'>' +  optionLabel + '</span></td>';
		cells +='<td><input id=\'' +  "columnInput_"+optionId + '\' name=\''+  "columnInput_"+optionId +'\' value=\'' +optionText+'\' title=\''+optionText+ '\' tabindex=\'' + (select.tabIndex + i + 1) + '\' class=\'formField required\' style=\'width:230px\' maxlength=\'40\'/></td>';
		if (i%2 == 0 ){//add first couple of cells
			toDisplay += '<tr>'+ cells;
		}else {//add second couple of cells
			toDisplay += cells+'</tr>';
		}
		
	}
	if (select.options.length %2 == 1){//add 2 empty cells at the end
		toDisplay +='<td></td><td></td></tr>';
	}
	toDisplay+='</table>';
	
	document.getElementById(inputContainerId).innerHTML = toDisplay;
		
}

/**
 * It returns a virtually unique identifier across clients appending the current time in milisecodns 
 * with a random number between 0- 10 000
 * @return a string UID with time_randomNR
 */

function generateUniqueId(){
	var newDate  = new Date;
	var concat = newDate.getTime().toString();
	var randomNr = Math.floor(Math.random()*10001);
	concat += '_'+randomNr;
	
	return concat;
	
}

//========================================================================================================

/**
 * Function for validating and submittig a report form
 * @param url
 * @param formId
 * @param divId
 * @param hiddenId the ID for the hidden form paramater storing the UID of the command object
 * @return
 */
function manageOpenReport(url,reportServletPath, formId, divId,hiddenId,formatId,reportType,newWindowName){	
	if (validateForm(formId)){
		//first we create a UID and add it to the hidden form value
		var uniqueId = generateUniqueId();
		document.getElementById(hiddenId).value = uniqueId;
		
		var format = document.getElementById(formatId).value;
		if (( format == 'HTML') || (format == 'PDF')) {
			openReportInNewWindow(url, formId, newWindowName);
		} else {
			submitReportNoResponse (url, formId, reportServletPath,uniqueId,reportType,newWindowName);
			//openReport(reportServletPath,uniqueId,reportType,newWindowName);
		}
		
	}
	
}

/**
 * Method used to open a report in other formats than html and pdf
 * 
 * @author coni
 * @param reportServletPath
 * @param uid
 * @param newWindowName
 * @return
 */
function openReport(reportServletPath,uid,reportType,newWindowName){
	var params = new Parameter(3);
	
	var parameter = new Parameter('REPORT_UID', uid);
	params[0] = parameter;
	
	var parameter = new Parameter('ATTACHMENT', 'attachment');
	params[1] = parameter;
	
	var parameter = new Parameter('REPORT_TYPE', reportType);
	params[2] = parameter;
	
		openFileInNewWindow(reportServletPath, params, 3, newWindowName, '');
}	

//========================================================================================================
/**
 * Submits a report form to the controller so that the ReportParams may be loaded on the context
 * 
 * @author alexandru.dobre
 */
function submitReportNoResponse(url, formId, reportServletPath,uniqueId,reportType,newWindowName){
	YAHOO.ts.smallLoading.show();
	
	var AjaxObject = { 
			handleSuccess:function (o){
				sessionExpired(o);	
				//alert("Callback handle succes");
				openReport(reportServletPath,uniqueId,reportType,newWindowName);
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
		//start transaction
		AjaxObject.startRequest();
	YAHOO.ts.smallLoading.hide();
}


//========================================================================================================
/**
 * Method used when a report's content is viewed in a new window
 * 
 * @author coni
 * @author alexandru.dobre
 */
function openReportInNewWindow(url, formId, newWindowName){
	YAHOO.ts.smallLoading.show();
	//I have to resize the new opened window depending on what page format has been chosen
	//var orientation = document.getElementById(orientationElementId);
	//if (orientation.value == 'portrait'){
		//var options = 'location=no, width=800, height=1000, toolbar=no, menubar=no, scrollbars=yes, status=no, resizeble=yes';	
	//} else if (orientation.value == 'landscape'){
	var options = 'location=no, width=1250, height=1000, menubar=no, toolbar=no, scrollbars=yes, status=no, resizeble=yes';
	//}
	var AjaxObject = { 
			handleSuccess:function (o){
				sessionExpired(o);
				if(o.responseText !== undefined){
					sessionExpired(o);
					YAHOO.ts.smallLoading.hide();
					reportWindow = window.open("", newWindowName, options);
					reportWindow.document.write(o.responseText);
					reportWindow.document.close();
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
		//start transaction
		AjaxObject.startRequest();
	YAHOO.ts.smallLoading.hide();
}

function assignValue  (elementId, value){
	document.getElementById(elementId).value = value;
	//alert ("Assigned value: "+elementId+" = "+document.getElementById(elementId).value);
}

/**
 * Used in the Time sheet Report form in order to populate the persons select
 * @author Coni
 * @param _personsSelectId
 * @param _teamMembersDiv
 * @param _personsDiv
 * @param _noSelectedItemsMessage
 * @param _checkBoxes
 * @param _memberId
 * @param _personId
 * @param _forOrganizationLabel
 * @param _searchResults
 * @param _panelTitle
 * @param _teamMembersHiddenInputsName
 * @param _personsHiddenInputsName
 * @return
 */
function manageAddPersons(_personsSelectId, _teamMembersDiv, _personsDiv, _noSelectedItemsMessage, _checkBoxes, _memberId, _personId, _forOrganizationLabel, _searchResults, _panelTitle, _teamMembersHiddenInputsName, _personsHiddenInputsName) {
	var personsSelectId = (this.personsSelectId != null ? this.personsSelectId : _personsSelectID), 
		teamMembersDiv = (this.teamMembersDiv != null ? this.teamMembersDiv : _teamMembersDiv),
		personsDiv = (this.personsDiv != null ? this.personsDiv : _personsDiv),
		noSelectedItemsMessage = (this.noSelectedItemsMessage != null ? this.noSelectedItemsMessage : _noSelectedItemsMessage),
		memberId = (this.memberId != null ? this.memberId : _memberId),
		personId = (this.personId != null ? this.personId : _personId),
		forOrganizationLabel = (this.forOrganizationLabel != null ? this.forOrganizationLabel : _forOrganizationLabel),
		searchResults = (this.searchResults != null ? this.searchResults : _searchResults),
		panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),
		teamMembersHiddenInputsName = (this.teamMembersHiddenInputsName != null ? this.teamMembersHiddenInputsName : _teamMembersHiddenInputsName),
		personsHiddenInputsName = (this.personsHiddenInputsName != null ? this.personsHiddenInputsName : _personsHiddenInputsName),
		checkBoxes = (this.checkBoxes != null ? this.checkBoxes : _checkBoxes);
	
	var persons = YAHOO.lang.JSON.parse(searchResults);
	
	var anyElementChecked = false;
	//check if at least an element is selected for add
	if (checkBoxes != null) {
		if (checkBoxes.length != undefined) {
			for (i = 0; i < checkBoxes.length; i++) {
				if (checkBoxes[i].checked) {
					anyElementChecked = true;
				} 
			}
		} else {
			if (checkBoxes.checked) {
				anyElementChecked = true;
			} 
		}
	}
	
	if (anyElementChecked) { //if at least one person is selected, i must add it do the persons select
		var personsSelect = document.getElementById(personsSelectId);
		
		//Array with the selected persons
		var selectedPersons = new Array();
		var allPersons = document.getElementsByName(personId);
		if (allPersons != null && allPersons.length > 0) {
			var persNr = 0;
			if (allPersons.length != undefined) {
				for (i = 0; i < allPersons.length; i++) {
					if (allPersons[i].checked == true) {
						selectedPersons[persNr] = allPersons[i];
						persNr++;
					}
				}
			}
		}
			
		//Array with selected team members
		var selectedTeamMembers = new Array();
		var allTeamMembers = document.getElementsByName(memberId);
		if (allTeamMembers != null && allTeamMembers.length > 0) {
			var membNr = 0;
			if (allTeamMembers.length != undefined) {
				for (i = 0; i < allTeamMembers.length; i++) {
					if (allTeamMembers[i].checked == true) {
						selectedTeamMembers[membNr] = allTeamMembers[i];
						membNr++;
					}
				}
			}
		}
		
		//HashMap with the select existing optgroup tags
		var optGroupsHM = new Array();
		for (i = 0; i < personsSelect.getElementsByTagName('optgroup').length; i++) {
			optGroupsHM[personsSelect.getElementsByTagName('optgroup')[i].id] = personsSelect.getElementsByTagName('optgroup')[i];
		}
		
		//HashMap with the selected team members - all the options that aren't included in the optgroup with id -1
		var membersSelectHM = new Array();
		for(i = 0; i < personsSelect.options.length; i++) {
			if (personsSelect.options[i].parentNode.id != -1) {
				membersSelectHM[personsSelect.options[i].value] = personsSelect.options[i].parentNode;
			}
		}
		
		//HashMap with the selected persons - all the options that aren't included in the optgroup with id -1
		var personsSelectHM = new Array();
		for(i = 0; i < personsSelect.options.length; i++) {
			if (personsSelect.options[i].parentNode.id == -1) {
				personsSelectHM[personsSelect.options[i].value] = personsSelect.options[i].parentNode;
			}
		}
		
		
		var newOption;
		var optGroup;
		var hidden;

		//add the selected persons
		if (selectedPersons != null && selectedPersons.length > 0) {
			//if the For Organization optgroup, doesn't exist it must be created being the first one in the select (with the id -1)
			if (optGroupsHM[-1] == null) {
				optGroup = document.createElement('optgroup');
				optGroup.id = -1;
				optGroup.label = forOrganizationLabel;
				personsSelect.insertBefore(optGroup, personsSelect.firstChild);
				optGroupsHM[-1] = optGroup;
			}
			for (i = 0; i < selectedPersons.length; i++) {
				//if the selected person doesn't exist, i must add it
				if (personsSelectHM[selectedPersons[i].value] == null) {
					//create the new option
					newOption = document.createElement('option');
					newOption.id = selectedPersons[i].name;
					newOption.value = selectedPersons[i].value;
					//set the inner HTML as the person's name
					for (j = 0; j < persons.length; j++) {
						if (persons[j].memberId == -1 && persons[j].personId == selectedPersons[i].value) {
							newOption.innerHTML = persons[j].lastName + " " + persons[j].firstName;
						}
					}
					//get the "From organization" optgroup and add the new option
					optGroupsHM[-1].appendChild(newOption);
					//add the hidden input with the selected personId
					hidden = document.createElement("input");
					hidden.setAttribute("type", "hidden");
					hidden.setAttribute("name", personsHiddenInputsName);
					hidden.setAttribute("id", personsHiddenInputsName + selectedPersons[i].value);
					hidden.setAttribute("value", selectedPersons[i].value);
					document.getElementById(personsDiv).appendChild(hidden);
				}
 			}
		}
		
		//add the selected team members
		if (selectedTeamMembers != null && selectedTeamMembers.length > 0) {
			for (i = 0; i < selectedTeamMembers.length; i++) {
//				if (membersSelectHM.length > 0) {
					//if the selected team member doesn't exist, i have to add it
					if (membersSelectHM[selectedTeamMembers[i].value] == null) {
						//get the current team member specific info
						var projectName;
						var projectId;
						var personName;
						for (j = 0; j < persons.length; j++) {
							if (persons[j].memberId == selectedTeamMembers[i].value) {
								projectName = persons[j].project.name;
								projectId = persons[j].projectId;
								personName = persons[j].lastName + " " + persons[j].firstName;
							}
						}
						//check if the optgroup with the team member's project already exists; if not, add it
						if (optGroupsHM[projectId] == null) {
							optGroup = document.createElement('optgroup');
							optGroup.id = projectId;
							optGroup.label = projectName;
							personsSelect.appendChild(optGroup);
							optGroupsHM[projectId] = optGroup;
						}
						newOption = document.createElement('option');
						newOption.id = selectedTeamMembers[i].name;
						newOption.value = selectedTeamMembers[i].value;
						newOption.innerHTML = personName;
						optGroupsHM[projectId].appendChild(newOption);
						//add the hidden input with the selected teamMemberId
						hidden = document.createElement("input");
						hidden.setAttribute("type", "hidden");
						hidden.setAttribute("name", teamMembersHiddenInputsName);
						hidden.setAttribute("id", teamMembersHiddenInputsName + selectedTeamMembers[i].value);
						hidden.setAttribute("value", selectedTeamMembers[i].value);
						document.getElementById(teamMembersDiv).appendChild(hidden);
					}
//				} else {
//				
//				}
			}
		}
	} else { //show message to select at least one person
		
		function handleOk() {
			YAHOO.ts.selectItemsAlert.hide();
			YAHOO.ts.selectItemsAlert.destroy();
		}
		
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

/**
 * Used in the Time Sheet Report form in order to remove the selected persons
 * @author Coni
 * @param _selectId
 * @param _teamMembersDiv
 * @param _pesonsDiv
 * @param _teamMembersHiddenInputsName
 * @param _personsHiddenInputsName
 * @return
 */
function manageDeletePersons(_selectId, _teamMembersDiv, _personsDiv, _teamMembersHiddenInputsName, _personsHiddenInputsName, _noSelectedItemsMessage, _panelTitle) {
	var selectId = (this.selectId != null ? this.selectId : _selectId),
	teamMembersDiv = (this.teamMembersDiv != null ? this.teamMembersDiv : _teamMembersDiv),
	personsDiv = (this.personsDiv != null ? this.personsDiv : _personsDiv),
	teamMembersHiddenInputsName = (this.teamMembersHiddenInputsName != null ? this.teamMembersHiddenInputsName : _teamMembersHiddenInputsName),
	noSelectedItemsMessage = (this.noSelectedItemsMessage != null ? this.noSelectedItemsMessage : _noSelectedItemsMessage),
	panelTitle = (this.panelTitle != null ? this.panelTitle : _panelTitle),
	personsHiddenInputsName = (this.personsHiddenInputsName != null ? this.personsHiddenInputsName : _personsHiddenInputsName);
	
	var personsSelect = document.getElementById(selectId);
	
	// A temporary Array for storing the user selection for removing
	var selected = new Array();
	for (i = 0; i < personsSelect.options.length; i++) {
		if (personsSelect.options[i].selected) { 
			selected.push({optGroup: personsSelect.options[i].parentNode, elem: personsSelect.options[i] });
		}
	}
	
	if (selected.length > 0) {
		// Removing from the select all the selected options
		for(i = 0; i < selected.length; i++) {			
			//remove the hidden input
			var hiddenInputId;
			if (selected[i].optGroup.id == -1) {
				hiddenInputId = personsHiddenInputsName + selected[i].elem.value;
				document.getElementById(personsDiv).removeChild(document.getElementById(hiddenInputId));
			} else {
				hiddenInputId = teamMembersHiddenInputsName + selected[i].elem.value;
				document.getElementById(teamMembersDiv).removeChild(document.getElementById(hiddenInputId));
			}

			selected[i].optGroup.removeChild(selected[i].elem);
			//remove the optgroup if no child option elements are present
			if (selected[i].optGroup.getElementsByTagName('option').length == 0) {
				personsSelect.removeChild(selected[i].optGroup);
			}
		}
	} else {
		//show message to select at least one person
		function handleOk() {
			YAHOO.ts.selectItemsAlert.hide();
			YAHOO.ts.selectItemsAlert.destroy();
		}
		
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

/**
* Restricts the user to input a file name without the forbidden characters in Windows:
* no /\:*?<>| and no space or . as the file name first character
* 
* @author Coni
* 
* @param e
* @return
*/
function restrictCharactersForFileName(inputId) {
	var nameTillNow = document.getElementById(inputId).value;
	if (nameTillNow != "" && !nameTillNow.match(/^[^/\\:*?"<>|. ]{1}[^/\\:*?"<>|]*$/)) {
		document.getElementById(inputId).value = OLD_FILE_NAME;
	} else {
		OLD_FILE_NAME = nameTillNow;
	}
}

//========================================================================================================



/**
* Clears the Date inputs and the Select with the selectId
* from Record Search Form
* 
* @author Andreea
* 
* @param selectId
* @return
*/
function clearDateInputsAndSelect(selectId) {
	var startDate = document.getElementById("recordSearchForm_startDate");
	startDate.value = "";
	
	var endDate = document.getElementById("recordSearchForm_endDate");
	endDate.value = "";
	
	var elem = document.getElementById(selectId);
	elem.options.selectedIndex = 0;
	
}


/**
* Clears the Week and Month Select from Record Search Form
* 
* @author Andreea
* 
* @param 
* @return
*/
function clearWeekAndMonthSelect() {
	
	var week = document.getElementById("recordSearchForm_week");
	week.options.selectedIndex = 0;
	
	var month = document.getElementById("recordSearchForm_month");
	month.options.selectedIndex = 0;
	
}


