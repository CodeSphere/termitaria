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
	
	if (YAHOO.cm.showFileSelectPanel != null) {
		YAHOO.cm.showFileSelectPanel.destroy();
	}
	
	var width = null;
	//we test what browser it is for display option
	var browser = whichBrs();
	if (browser == "Internet Explorer") {
		width = "0px";
	}
	
	YAHOO.cm.showFileSelectPanel = new YAHOO.widget.Panel("ShowFileSelectPanel", { width: width, visible:false, x: xPosition, y: yPosition, modal: true, constraintoviewport:true, close: true, draggable:true, 
		effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1}} );	
	YAHOO.cm.showFileSelectPanel.setHeader('<span>' + panelTitle + '</span>');
	YAHOO.cm.showFileSelectPanel.render(document.body);
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.cm.showFileSelectPanel);
		
}



//==============================================================================================================================
/**
 * Destroys the select file panel
 * @author mitziuro
 */
	 
function destroyFileAddPanel(){
	//
	if(YAHOO.cm.showFileSelectPanel){
		YAHOO.cm.showFileSelectPanel.destroy();
		YAHOO.cm.showFileSelectPanel = null;
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
   YAHOO.cm.tree = tree;
  
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
			
				YAHOO.cm.smallLoading.hide();
			}
		},
		failure: function(oResponse) {
            alert("Failed to process XHR transaction.");
        }
    };
	
	
    //function used to handle the case when the user chooses from the confirmation panel not to perform the action on the specific organization
    function handleNo(){
		YAHOO.cm.confirmation.hide();
		YAHOO.cm.confirmation.destroy();
	}
	
    //function used to handle the case when the user chooses from the confirmation panel to perform the action on the specific organization	
	function handleYes() {
	    // Show Small Loading
		YAHOO.cm.smallLoading.show();
		handleNo();
   	 	YAHOO.util.Connect.asyncRequest('GET', sUrl, callback);
	}
	
	//create and display the confirmation dialog
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
		                          { text:"Yes", handler: handleYes, isDefault:true },
		                          { text:"No",  handler:handleNo } ] 
		             } ); 
	
	YAHOO.cm.confirmation.setBody(confirmMessage);
	YAHOO.cm.confirmation.render(document.body);
	YAHOO.cm.confirmation.show();
	
}

//=======================================================================================================
/**
 * Used to perform action on organisation name click in tree (collapsing the tree)
 * @author mitziuro
 */ 

function manageCollapseTree() {
	// collapsing the category tree
	YAHOO.cm.tree.getRoot().collapseAll();
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
 * Method used when a cm report's content is viewed in a new window
 * 
 * @author coni
 */
function openReportInNewWindow(url, formId, newWindowName, orientationElementId){
	YAHOO.cm.smallLoading.show();
	//I have to resize the new opened window depending on what page format has been chosen
	var orientation = document.getElementById(orientationElementId);
	if (orientation.value == 'portrait'){
		var options = 'location=no, width=800, height=1000, toolbar=no, menubar=no, scrollbars=yes, status=no, resizeble=yes';	
	} else if (orientation.value == 'landscape'){
		var options = 'location=no, width=1250, height=1000, menubar=no, toolbar=no, scrollbars=yes, status=no, resizeble=yes';
	}
	var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					YAHOO.cm.smallLoading.hide();
					reportWindow = window.open("", newWindowName, options);
					reportWindow.document.write(o.responseText);
					reportWindow.document.close();
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
		//start transaction
		AjaxObject.startRequest();
	YAHOO.cm.smallLoading.hide();
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
function opencmReport(reportServletPath, startDate, endDate, message, event, moduleId, reportTitle, orientation, format, organisationId, formId, userHiddenFormElementId, newWindowName){
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
			opencmReport(reportServletPath, startDate, endDate, message, event, moduleId, reportTitle, orientation, formatId, organisationId, formId, userHiddenFormElementId, newWindowName);
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
	var AjaxObject = { 
			handleSuccess:function (o){
				if(o.responseText !== undefined){
					var div = document.getElementById(divId);
					div.innerHTML = o.responseText;
					pageDiv.style.display="block";
					YAHOO.cm.smallLoading.hide();
					YAHOO.cm.creatingReport.hide();
					var scriptTags = div.getElementsByTagName('script');
					for(var i=0;i<scriptTags.length;i++){
						eval(scriptTags[i].innerHTML);
					}
				}
			}, 
			handleFailure:function (o){
				YAHOO.cm.smallLoading.hide();
				YAHOO.cm.creatingReport.hide();
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
        YAHOO.cm.smallLoading.show();
        YAHOO.cm.creatingReport.show();
		//start transaction
		AjaxObject.startRequest();
}

/**
 * Function used to display the proper fields for a client depending on the client's type
 * 
 * @author Coni
 * @param type
 * @param firmClientInfoClass
 * @param personClientInfoClass
 * @return
 */
function displayPersonOrFirmClientFields(type, firmClientInfoClass, personClientInfoClass) {	 
	var firmClientElements = getElementsByClass(firmClientInfoClass, null, null);
	var personClientElements = getElementsByClass(personClientInfoClass, null, null);	
	//if the client is a firm (type 1), the name, cui, iban, capital and location will be displayed
	if (type == 1 || type == -1){
		for (i=0; i < firmClientElements.length; i++){
			if (firmClientElements[i].tagName.toLowerCase() == "tr") {
				firmClientElements[i].style.display="table-row";
			} else {
				firmClientElements[i].style.display="table-cell";
			}
		}
		for (i=0; i < personClientElements.length; i++){
			personClientElements[i].style.display="none";
			if (personClientElements[i].tagName.toLowerCase() == "input" || personClientElements[i].tagName.toLowerCase() == "textarea"){			
				personClientElements[i].value="";
			}
		}
	} else if (type == 2){		
		//if the client is a person (type 2), firstName, lastName, birth date and sex will be displayed
		for (i=0; i < personClientElements.length; i++){
			if (personClientElements[i].tagName.toLowerCase() == "tr") {
				personClientElements[i].style.display="table-row";
			} else {
				personClientElements[i].style.display="table-cell";
			}
		}
				
		for (i=0; i < firmClientElements.length; i++){
			firmClientElements[i].style.display="none";
			if (firmClientElements[i].tagName.toLowerCase() == "input" || firmClientElements[i].tagName.toLowerCase() == "textarea"){			
				firmClientElements[i].value="";
			}
		}
	}

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
	
	if (YAHOO.cm.infoPanel != null){		
		YAHOO.cm.infoPanel.destroy();
		YAHOO.cm.infoPanel = null;
		
	}
		
	YAHOO.cm.infoPanel = new YAHOO.widget.Panel("InfoPanel", { fixedcenter:true, width: width_size, visible: false, constraintoviewport: true, close: true, draggable:true,
		 effect: {effect:YAHOO.widget.ContainerEffect.FADE,duration:0.1},  modal: true, zindex:1, iframe: true});
	YAHOO.cm.infoPanel.setHeader('<span title=\'' + panelTitle +'\'>' + panelName + '</span>'); 	
	
	YAHOO.cm.infoPanel.render(document.body);	
	returnContentFromUrlToPanelAndShowPannel(url, YAHOO.cm.infoPanel);	
}

/**
* Used to ask when you click on add 
* or back
* @author mitziuro
*/ 

function check_add(_url, _divId) {
	
	var url = this.url != null ? this.url : _url;
	var divId = this.container != null ? this.container: _divId;
	
	var confirmMessage = 'CONFIRM_ADD'.translate('COMMON');		
	
	//check if something has changed
	if(ENTITY.equals(OLD_ENTITY)){
		getContentFromUrlDirect(url, divId);
		return;
	}
	
   //function used to handle the case when the user chooses from the confirmation panel not to perform the action on the specific item
   function handleNo(){
   	YAHOO.cm.confirmationForAdd.hide();
   	YAHOO.cm.confirmationForAdd.destroy();
	}
	
   //function used to handle the case when the user chooses from the confirmation panel to perform the action on the specific item	
	function handleYes() {
		handleNo();
		getContentFromUrlDirect(url, divId);
	}
	
	//create and display the confirmation dialog
	YAHOO.cm.confirmationForAdd =  
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
	
	YAHOO.cm.confirmationForAdd.setBody(confirmMessage);
	YAHOO.cm.confirmationForAdd.render(document.body);
	YAHOO.cm.confirmationForAdd.show();
	
}

/**
* Used to ask when you click on add or back
* @param panel the panel for hiding
* @author mitziuro
*/ 

function check_cancel_panel(panel) {
	
	var confirmMessage = 'CONFIRM_ADD'.translate('COMMON');	
	
	//check if something has changed
	if(ENTITY.equals(OLD_ENTITY)){		
		panel.hide();
		panel.destroy();
		destroyAll();
		return;
	}
	
   //function used to handle the case when the user chooses from the confirmation panel not to perform the action on the specific item
   function handleNo(){
   	YAHOO.cm.confirmationForAdd.hide();
   	YAHOO.cm.confirmationForAdd.destroy();
	}
	
   //function used to handle the case when the user chooses from the confirmation panel to perform the action on the specific item	
	function handleYes() {
		handleNo();
		panel.hide();
		panel.destroy();
		destroyAll();
	}
	
   //function for destroying all panels
    function destroyAll(){
    	YAHOO.cm.projectPanel = null;    
    }
   
	//create and display the confirmation dialog
	YAHOO.cm.confirmationForAdd =  
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
	
	YAHOO.cm.confirmationForAdd.setBody(confirmMessage);
	YAHOO.cm.confirmationForAdd.render(document.body);
	YAHOO.cm.confirmationForAdd.show();	
}

/**
 * A project element
 * 
 * @author Adelina
 * 
 * @param projectId
 * @param name
 * @param clientId
 * @param managerId
 * @param observation
 * @param description
 * @return
 */
function projectElement(projectId, name, clientId, managerId, observation, description) {
	this.projectId = projectId; 
	this.name = name;
	this.clientId = clientId;
	this.managerId = managerId;
	this.observation = observation;
	this.description = description;
	
	this.equals = equals;

	/**
	 * Test if the parameters of one project equals to another one
	 * 
	 * @author Adelina
	 * 
	 * @param project
	 * @return
	 */
	function equals(project) {		
		if(this.projectId != project.projectId) return false;
		if(this.name != project.name) return false;
		if(this.clientId != project.clientId) return false;
		if(this.managerId != project.managerId) return false;
		if(this.observation != project.observation) return false;
		if(this.description != project.description) return false;
		
		return true;
	}
}
/**
 * A project team element
 * 
 * @author Adelina
 * 
 * @param projectTeamId
 * @param name
 * @param observation
 * @param description
 * @return
 */
function projectTeamElement(projectTeamId, observation, description) {
	this.projectTeamId = projectTeamId; 	
	this.observation = observation;
	this.description = description;
	
	this.equals = equals;
	
	/**
	 * Test if the parameters of one project team equals to another one
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeam
	 * @return
	 */
	function equals(projectTeam) {		
		if(this.projectTeamId != projectTeam.projectTeamId) return false;			
		if(this.observation != projectTeam.observation) return false;
		if(this.description != projectTeam.description) return false;
		
		return true;
	}
}

/**
 * Function that takes the project metadata and returns a new object
 * 
 * @author Adelina
 * 
 * @return
 */
function getProjectData() {
	return new projectElement(document.getElementById('projectId').value, document.getElementById('projectForm_name').value, document.getElementById('clientId').value, document.getElementById('managerId').value, document.getElementById('projectForm_description').value, document.getElementById('projectForm_observation').value);
}
 
 /**
  * Function that takes the project team metadata and returns a new object
  * 
  * @author Adelina
  * 
  * @return
  */
 function getProjectTeamData() {
 	return new projectTeamElement(document.getElementById('projectTeamId').value, document.getElementById('projectTeamForm_description').value, document.getElementById('projectTeamForm_observation').value);
 }
  
  
/**
 * Client person element
 * 
 * @author Adelina
 * 
 * @param clientId
 * @param type
 * @param phone
 * @param email
 * @param fax
 * @param address
 * @param description
 * @param observation
 * @param p_lastName
 * @param p_sex_m
 * @param p_sex_f
 * @param p_birthDate_day
 * @param p_birthDate_month
 * @param p_birthDate_year
 * @return
 */ 
function clientPersonElement(clientId, type, phone, email, fax, address, description, observation, p_lastName, p_sex_m, p_sex_f, p_birthDate_day, p_birthDate_month, p_birthDate_year) {
	this.clientId = clientId;
	this.type = type;
	this.phone = phone;
	this.email = email;
	this.fax = fax;
	this.address = address;
	this.description = description;
	this.observation = observation;	
	this.p_lastName = p_lastName;
	this.p_sex_m = p_sex_m;
	this.p_sex_f = p_sex_f;
	this.p_birthDate_day = p_birthDate_day;
	this.p_birthDate_month = p_birthDate_month;
	this.p_birthDate_year = p_birthDate_year;
	
	this.equals = equals;
	
	/**
	 * Tests if the parameters of the client equals to those of another one
	 * @author Adelina
	 * @param client
	 * @return
	 */
	function equals(client) {
		if (this.clientId != client.clientId) return false;
		if (this.type != client.type) return false;
		if (this.phone != client.phone) return false;
		if (this.email != client.email) return false;
		if (this.fax != client.fax) return false;
		if (this.address != client.address) return false;
		if (this.description != client.description) return false;
		if (this.observation != client.observation) return false;		
		if (this.p_firstName != client.p_firstName) return false;
		if (this.p_lastName != client.p_lastName) return false;
		if (this.p_sex_m != client.p_sex_m) return false;
		if (this.p_sex_f != client.p_sex_f) return false;
		if (this.p_birthDate_day != client.p_birthDate_day) return false;
		if (this.p_birthDate_month != client.p_birthDate_month) return false;
		if (this.p_birthDate_year != client.p_birthDate_year) return false;

		return true;
	}
		
}

/**
 * Client firm element
 * 
 * @author Adelina
 * 
 * @param clientId
 * @param type
 * @param phone
 * @param email
 * @param fax
 * @param address
 * @param description
 * @param observation
 * @param c_name
 * @param c_cui
 * @param c_capital
 * @param c_iban
 * @param c_location
 * @return
 */
function clientFirmElement(clientId, type, phone, email, fax, address, description, observation, c_name, c_cui, c_capital, c_iban, c_location) {
	this.clientId = clientId;
	this.type = type;
	this.phone = phone;
	this.email = email;
	this.fax = fax;
	this.address = address;
	this.description = description;
	this.observation = observation;
	this.c_name = c_name;
	this.c_cui = c_cui;
	this.c_capital = c_capital;
	this.c_iban = c_iban;
	this.c_location = c_location;	
	
	this.equals = equals;
	
	/**
	 * Tests if the parameters of the client equals to those of another one
	 * @author Adelina
	 * @param client
	 * @return
	 */
	function equals(client) {
		if (this.clientId != client.clientId) return false;
		if (this.type != client.type) return false;
		if (this.phone != client.phone) return false;
		if (this.email != client.email) return false;
		if (this.fax != client.fax) return false;
		if (this.address != client.address) return false;
		if (this.description != client.description) return false;
		if (this.observation != client.observation) return false;
		if (this.c_name != client.c_name) return false;
		if (this.c_cui != client.c_cui) return false;
		if (this.c_capital != client.c_capital) return false;
		if (this.c_iban != client.c_iban) return false;
		if (this.c_location != client.c_location) return false;	

		return true;
	}
}

/**
 * Function that takes the client metadata and returns a new object
 * 
 * @author Adelina
 * 
 * @return
 */
function getClientData() {	
	var clientType = document.getElementById("clientForm_type").options[document.getElementById("clientForm_type").selectedIndex].value;
	var FIRM = 1;
	var PERSON = 2;	
	
	if(clientType == FIRM) {
		return new clientFirmElement(document.getElementById("clientId").value, document.getElementById("clientForm_type").options[document.getElementById("clientForm_type").selectedIndex].value, 
									 document.getElementById("clientForm_phone").value, document.getElementById("clientForm_email").value, document.getElementById("clientForm_fax").value, 
									 document.getElementById("clientForm_address").value, document.getElementById("clientForm_description").value, document.getElementById("clientForm_observation").value, 
									 document.getElementById("clientForm_c_name").value, document.getElementById("clientForm_c_cui").value, document.getElementById("clientForm_c_capital").value, 
									 document.getElementById("clientForm_c_iban").value, document.getElementById("clientForm_c_location").value);
	} else {
		return new clientPersonElement(document.getElementById("clientId").value, document.getElementById("clientForm_type").options[document.getElementById("clientForm_type").selectedIndex].value, 
									   document.getElementById("clientForm_phone").value, document.getElementById("clientForm_email").value, document.getElementById("clientForm_fax").value, 
									   document.getElementById("clientForm_address").value, document.getElementById("clientForm_description").value, document.getElementById("clientForm_observation").value, 
									   document.getElementById("clientForm_p_firstName").value, document.getElementById("clientForm_p_lastName").value, document.getElementById("clientForm_p_sex_m").value,
									   document.getElementById("clientForm_p_sex_f").value, document.getElementById("clientForm_p_birthdate_day").options[document.getElementById("clientForm_p_birthdate_day").selectedIndex].value, 
									   document.getElementById("clientForm_p_birthdate_month").options[document.getElementById("clientForm_p_birthdate_month").selectedIndex].value, 
									   document.getElementById("clientForm_p_birthdate_year").options[document.getElementById("clientForm_p_birthdate_year").selectedIndex].value);
	}
}

/**
* Function for managing the check/uncheck event on the projectNoClient form checkbox
* 
* @author Adelina
* 
* @param e
* @param dateInputsClass
* @param toBeChecked
* @return
*/
function manageClientInputDisplay(e, dateInputsClass, toBeChecked, JSON_CLIENTS, projectStatus){
	var dateElements = getElementsByClass(dateInputsClass, null, null);
	if (toBeChecked != null) {
		if (toBeChecked) {
			e.checked = true;
		} else {
			e.checked = false;
		}
	}
		
	if (e.checked) {						
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
		document.getElementById("projectForm_client").value='';		
		document.getElementById("projectForm_client").readOnly=true;
		
		//hide the required red stars			
		document.getElementById("clientRequired").style.display = "none";
		
		document.getElementById("client_message").innerHTML = "";
		
		if(projectStatus == 1) {			
			YAHOO.util.Event.purgeElement("projectForm_client", true);			
		}
		
	} else {				
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
		
		if(projectStatus == 1) {	
			YAHOO.util.Event.addListener("projectForm_client", "click", function manageClickInClientInput() {				
				instantiateClientAutoComplete(new YAHOO.util.LocalDataSource(JSON_CLIENTS), 'projectForm_client', 'autoCompleteClientContainer', 'clientId', null);			
			}, null, true);
		}
					
		//show the required red stars			
		document.getElementById("clientRequired").style.display = "inline";	
		document.getElementById("projectForm_client").readOnly = false;	
		
	}
}
 
