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
 * 
*/

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
	
	if (YAHOO.audit.showFileSelectPanel != null) {
		YAHOO.audit.showFileSelectPanel.destroy();
	}
	
	var width = null;
	//we test what browser it is for display option
	var browser = whichBrs();
	if (browser == "Internet Explorer") {
		width = "0px";
	}
	
	YAHOO.audit.showFileSelectPanel = new YAHOO.widget.Panel("ShowFileSelectPanel", { width: width, visible:false, x: xPosition, y: yPosition, modal: true, constraintoviewport:true, close: true, draggable:true, 
		effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	YAHOO.audit.showFileSelectPanel.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.audit.showFileSelectPanel.render(document.body);
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.audit.showFileSelectPanel);
		
}



//==============================================================================================================================
/**
 * Destroys the select file panel
 * @author mitziuro
 */
	 
function destroyFileAddPanel(){
	//
	if(YAHOO.audit.showFileSelectPanel){
		YAHOO.audit.showFileSelectPanel.destroy();
		YAHOO.audit.showFileSelectPanel = null;
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
   YAHOO.audit.tree = tree;
  
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
			sessionExpired(oResponse);
			if (oResponse.responseText != undefined){
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
			
				YAHOO.audit.smallLoading.hide();
			}
		},
		failure: function(oResponse) {
            alert("Failed to process XHR transaction.");
        }
    };
	
	
    //function used to handle the case when the user chooses from the confirmation panel not to perform the action on the specific organization
    function handleNo(){
		YAHOO.audit.confirmation.hide();
		YAHOO.audit.confirmation.destroy();
	}
	
    //function used to handle the case when the user chooses from the confirmation panel to perform the action on the specific organization	
	function handleYes() {
	    // Show Small Loading
		YAHOO.audit.smallLoading.show();
		handleNo();
   	 	YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
	}
	
	//create and display the confirmation dialog
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
	
	YAHOO.audit.confirmation.setBody(confirmMessage);
	YAHOO.audit.confirmation.render(document.body);
	YAHOO.audit.confirmation.show();
	
}

//=======================================================================================================
/**
 * Used to perform action on organisation name click in tree (collapsing the tree)
 * @author mitziuro
 */ 

function manageCollapseTree() {
	// collapsing the category tree
	YAHOO.audit.tree.getRoot().collapseAll();
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
 * Method used when a audit report's content is viewed in a new window
 * 
 * @author coni
 */
function openReportInNewWindow(url, formId, newWindowName, orientationElementId){
	YAHOO.audit.smallLoading.show();
	//I have to resize the new opened window depending on what page format has been chosen
	var orientation = document.getElementById(orientationElementId);
	if (orientation.value == 'portrait'){
		var options = 'location=no, width=900, height=1000, toolbar=no, menubar=no, scrollbars=yes, status=no, resizeble=yes';	
	} else if (orientation.value == 'landscape'){
		var options = 'location=no, width=1250, height=1000, menubar=no, toolbar=no, scrollbars=yes, status=no, resizeble=yes';
	}
	var AjaxObject = { 
			handleSuccess:function (o){
				sessionExpired(o);
				if(o.responseText !== undefined){
					YAHOO.audit.smallLoading.hide();
					reportWindow = window.open("", newWindowName, options);
					reportWindow.document.write(o.responseText);
					reportWindow.document.close();
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
		//start transaction
		AjaxObject.startRequest();
	YAHOO.audit.smallLoading.hide();
}

/**
 * Method used to open a report in other formats than html and pdf
 * 
 * @author coni
 * @param reportServletPath
 * @param startDate
 * @param endDate
 * @param message
 * @param event
 * @param moduleId
 * @param reportTitle
 * @param orientation
 * @param format
 * @param organisationId
 * @param formId
 * @param userHiddenFormElementId
 * @param newWindowName
 * @return
 */
function openAuditReport(reportServletPath, startDate, endDate, message, event, moduleId, reportTitle, orientation, format, organisationId, formId, userHiddenFormElementId, newWindowName){
	var params = new Parameter(10);

	var parameter = new Parameter('startDate', document.getElementById(startDate).value);
	params[0] = parameter;
	
	parameter = new Parameter('endDate', document.getElementById(endDate).value);
	params[1] = parameter;
		
	parameter = new Parameter('personId', document.getElementById(userHiddenFormElementId).value);
	params[2] = parameter;
		
	parameter = new Parameter('message', document.getElementById(message).value);
	params[3] = parameter;
	
	parameter = new Parameter('event', document.getElementById(event).value);
	params[4] = parameter;
	
	parameter = new Parameter('moduleId', document.getElementById(moduleId).value);
	params[5] = parameter;
		
	parameter = new Parameter('reportTitle', document.getElementById(reportTitle).value);
	params[6] = parameter;
		
	parameter = new Parameter('orientation', document.getElementById(orientation).value);
	params[7] = parameter;
		
	parameter = new Parameter('format', document.getElementById(format).value);
	params[8] = parameter;
		
	if (document.getElementById(organisationId) != null){
		parameter = new Parameter('organisationId', document.getElementById(organisationId).value);
		params[9] = parameter;
		openFileInNewWindow(reportServletPath, params, 10, newWindowName, '');
	} else {
		openFileInNewWindow(reportServletPath, params, 9, newWindowName, '');
	}	
}

function Parameter(key, value){
	this.key = key;
	this.value = value;
}

/**
 * @author coni
 * It is used to open a report in a new window if the chosen format is html or pdf, or to display the open/save browser dialog for other format types
 * @param url
 * @param reportServletPath
 * @param formId
 * @param newWindowName
 * @param userInputAutoCompleteId
 * @param userHiddenFormElementId
 * @param startDate
 * @param endDate
 * @param message
 * @param event
 * @param moduleId
 * @param reportTitle
 * @param orientation
 * @param format
 * @param organisationId
 * @return
 */
function manageOpenReport(url, reportServletPath, formId, newWindowName, userInputAutoCompleteId, userHiddenFormElementId, startDate, endDate, message, event, moduleId, reportTitle, orientation, formatId, organisationId){
	if (validateForm(formId)) {
		//if somebody deleted the text displayed in the user input box auto complete after selecting one, I have to change the personId hidden field value  
		var userInputAutoComplete = document.getElementById(userInputAutoCompleteId);
		if ( userInputAutoComplete.value == "") {
			document.getElementById(userHiddenFormElementId).value = "";
		}
		
		format = document.getElementById(formatId).value;
		if ((format == 'HTML') || (format == 'PDF')) {
			openReportInNewWindow(url, formId, newWindowName, orientation);
		} else {
			openAuditReport(reportServletPath, startDate, endDate, message, event, moduleId, reportTitle, orientation, formatId, organisationId, formId, userHiddenFormElementId, newWindowName);
		}
	}
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
	var pageDiv = document.getElementById(pageDivId);
	var AjaxObject = { 
			handleSuccess:function (o){
				sessionExpired(o);
				if(o.responseText !== undefined){
					var div = document.getElementById(divId);
					div.innerHTML = o.responseText;
					pageDiv.style.display="block";
					YAHOO.audit.smallLoading.hide();
					YAHOO.audit.creatingReport.hide();
					var scriptTags = div.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
				}
			}, 
			handleFailure:function (o){
				YAHOO.audit.smallLoading.hide();
				YAHOO.audit.creatingReport.hide();
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
		pageDiv.style.display="none";
		//display the loading message
        YAHOO.audit.smallLoading.show();
        YAHOO.audit.creatingReport.show();
		//start transaction
		AjaxObject.startRequest();
}

