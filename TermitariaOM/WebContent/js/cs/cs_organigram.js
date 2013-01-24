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
	 * 
	 */
	function drawOrganigramCompleete(jsonTree, orgPicture, deptPicture, viewEmployeesButtonLabel, printToPdfButtonLabel, refreshButtonLabel) {
		var jg = new jsGraphics("OrganigramCanvas");
		jg.setFont("verdana","10px",Font.PLAIN);  

		jg.drawString("<a href=\"#\" onClick=\"window.location = 'OrganisationOrganigramToPdf.htm'\" title=\"" + printToPdfButtonLabel + "\"><img src='images/buttons/action_print_to_pdf.png'/></a>", -30, 10);
		jg.drawString("<a href=\"#\" onClick=\"window.location.reload(true)\" title=\"" + refreshButtonLabel + "\"><img src='images/buttons/action_refresh.png'/></a>", -10, 10);
		

		
		var windowWidth = jsonTree.organigramWidth + 300;
		var windowHeight = jsonTree.organigramHeight + 200;

		if (windowWidth > 1250) {
			windowWidth = 1250;
		}	

		if (windowHeight > 700)  {
			windowHeight = 700;
		}
	
		self.resizeTo(windowWidth, windowHeight);
		var vspace = jsonTree.vspace;
		var hspace = jsonTree.hspace;
		var nodeWidth = jsonTree.nodeWidth;
		var nodeHeight = jsonTree.nodeHeight;
		drawNode(jg, jsonTree, nodeWidth, nodeHeight, vspace, true, orgPicture, deptPicture, viewEmployeesButtonLabel);
		
		jg.paint();
	}

//========================================================================================================
	/**
	 * 
	 */
	function displayToolTip(msg){
		new Tips(msg, {maxOpacity: 0.9, maxTitleChars: 25});
	}
	
	
//========================================================================================================
	/**
	 * Draws a node on the Organigram
	 */
	function drawNode(jg, node, nodeWidth, nodeHeight, vspace, root, orgPicture, deptPicture, viewEmployeesButtonLabel) {
	drawNodeInformation(jg, node, nodeWidth, nodeHeight, orgPicture, deptPicture, viewEmployeesButtonLabel);
		if (!root) {
			jg.setStroke(3);
			jg.drawLine(node.xPosition + (nodeWidth / 2), node.yPosition - (vspace / 2), node.xPosition + (nodeWidth / 2), node.yPosition - 2);
			jg.setStroke(1);
		}

		if ( (node.children) && (node.children.length)) {
			jg.setStroke(3);
			jg.drawLine(node.xPosition + (nodeWidth / 2), node.yPosition + nodeHeight, node.xPosition + (nodeWidth / 2), node.yPosition + nodeHeight + vspace /  2);
			jg.drawLine(
				node.children[0].xPosition + (nodeWidth / 2), node.yPosition + nodeHeight + (vspace / 2),
				node.children[node.children.length - 1].xPosition + (nodeWidth / 2), node.yPosition + nodeHeight + (vspace / 2)
					);
			jg.setStroke(1);
			for(var i = 0; i < node.children.length; i++) {
				drawNode(jg, node.children[i], nodeWidth, nodeHeight, vspace, false, orgPicture, deptPicture, viewEmployeesButtonLabel);
			}
		} else if (node.children) {
			jg.setStroke(3);
			jg.drawLine(node.xPosition + (nodeWidth / 2), node.yPosition + nodeHeight, node.xPosition + (nodeWidth / 2), node.yPosition + nodeHeight + (vspace / 2) );
			jg.setStroke(1);
			drawNode(jg, node.children, nodeWidth, nodeHeight, vspace, false, orgPicture, deptPicture, viewEmployeesButtonLabel);
		}
	
		
	}
	
	
//========================================================================================================
	/**
	 * Draws node Informations
	 */
	function drawNodeInformation(jg, node, nodeWidth, nodeHeight, orgPicture, deptPicture, viewEmployeesButtonLabel){
		var nodeClass = "nodeDept";
		if (node.type != 6) {
			jg.drawImage(orgPicture, node.xPosition, node.yPosition, nodeWidth, nodeHeight );
			nodeClass = "nodeOrg";
		} else {
			jg.drawImage(deptPicture, node.xPosition, node.yPosition, nodeWidth, nodeHeight );
		}
		var maxTitleLength = 18;
		if (node.label.length > maxTitleLength) {
			var newLabel = node.label.substring(0, maxTitleLength);
			newLabel = newLabel + '...';
			jg.drawStringRect("<div class=\"" + nodeClass + "\" title=\"" + node.label + "\">" + newLabel + "</div>", node.xPosition + 5, node.yPosition + 5, nodeWidth - 5, "center");
		} else {
			jg.drawStringRect("<div class=\"" + nodeClass + "\"> " + node.label + "</div>", node.xPosition + 5, node.yPosition + 5, nodeWidth - 5, "center");
		}
		
		jg.setFont("verdana","10px",Font.ITALIC);  
		
		if (node.manager.length > 25) {
			var newManagerName = node.manager.substring(0, 22);
			jg.drawStringRect("<div class=\"nodeManager\" title=\"" + node.manager + "\">" + newManagerName + "</div>", node.xPosition - 20, node.yPosition + 27, nodeWidth + 20, "left");
		} else {
			jg.drawStringRect("<div class=\"nodeManager\">" + node.manager + "</div>", node.xPosition - 20, node.yPosition + 27, nodeWidth + 20, "left");
		}	
		
		jg.drawStringRect("<a class=\"viewEmployeesLink\" href=\"#\" onClick=\"myFunction('" + node.label + "'," +node.departmentId+ ")\">" + viewEmployeesButtonLabel + "</a>", node.xPosition - 20, node.yPosition + 43, nodeWidth + 20, "left");
		jg.setFont("verdana","10px",Font.PLAIN);
		jg.setColor("#000000");
	}

//========================================================================================================
	/**
	 * 
	 */
	function myFunction(departmentName, departmentId) {
		displayDepartmentsEmployees(departmentName, departmentId);
	}

//========================================================================================================
	/**
	 * 
	 */
	function displayTip(text) {
		Tip(text, ABOVE, true, BGCOLOR, '#FFFFFF', BORDERCOLOR, '#000000');
	}
	
//========================================================================================================
	/**
	 * Displays one Department's Employees in the Organigram.
	 * (It's used in the Organigram).
	 * @author dan.damian 
	 */
	function displayDepartmentsEmployees(departmentName, departmentId){		
		var url = 'OrganigramGetDepartmentsPersons.htm?departmentId=' + departmentId;		
		var loadObject = { 
			handleSucess:function (o){
				var oData = eval( "(" + o.responseText + ")");
				var text = "<div><table width=\"300\">";				
				var cssClass;
				for(var i = 0;  i < oData.length; i++) {
					text += "<tr><td style='padding-left: 2px'>" + (i + 1) + ". " + oData[i].name + "</td></tr>";
				}
				text += "</table></div>";
				
				Tip(text, TITLE, departmentName, TITLEBGCOLOR, '#DFDFDF',TITLEFONTCOLOR, '#000000', 
						BGCOLOR, '#FFFFFF', WIDTH, 300, HEIGHT, 100, BORDERCOLOR, '#DFDFDF', BORDERWIDTH, 1, TITLEPADDING, 5,
						CLOSEBTN, true, CLOSEBTNCOLORS, ['', '#424242', 'white', '#000000'], STICKY, true, SHADOW, true, CLICKSTICKY, true);
				
				
			},
			handleFailure:function (o){
				alert("Error " + o.status + " : " + o.statusText);
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
		loadObject.startRequest();

	}
