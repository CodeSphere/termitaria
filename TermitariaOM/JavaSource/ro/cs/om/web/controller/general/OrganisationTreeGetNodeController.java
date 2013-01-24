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
package ro.cs.om.web.controller.general;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.OrgTree;
import ro.cs.om.entity.OrgTreeNode;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * Returns a view containing a Select with All The Children Organisations, Departments and Persons 
 * of a organisation tree node
 * @author coni
 *
 */
public class OrganisationTreeGetNodeController extends RootAbstractController {
	public OrganisationTreeGetNodeController() {
		setView("Organisation_GetTreeNode");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		JSONObject resultSet = new JSONObject();
		JSONArray  jsonArrayNodes = new JSONArray();
		JSONArray  jsonArrayEmployees = new JSONArray();

		//Retrieving Organisation Tree of Departments from App Context 
		Integer rootOrganisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		HashMap<Integer, OrgTree> treesMap = (HashMap<Integer, OrgTree>)OMContext.getOrganisationsTreesMapFromContext(IConstant.ORGANISATION_TREES);
		OrgTree tree = treesMap.get(new Integer(rootOrganisationId));
		if (tree != null) {
			int nodeId = -1;
			byte type = -1;
			int currentOrganisationId = -1;
			try {
				nodeId = Integer.parseInt(request.getParameter("nodeId"));
				type = (byte) Integer.parseInt(request.getParameter("type")); 
				currentOrganisationId = Integer.parseInt(request.getParameter("currentOrganisationId"));
			}catch(NumberFormatException nfex) {
				logger.error("", nfex);
			}
			OrgTreeNode node = null;
			if (nodeId > 0 && type >0 ) {
//				//boolean returnWithEmployees = true;
//				if ( type == IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT ){
//					node = tree.getNodeFromDepartmentHashMap(nodeId);
//				}
//				else if ( type != IConstant.NOM_TREE_NODE_TYPE_PERSON ) {
//					node = tree.getNodeFromOrganisationHashMap(nodeId);
//				}
				node = tree.getNodeFromHashMap(nodeId);
				OrgTreeNode child = null;
				for(int i = 0; i < node.getChildrenNumber(); i++) {
					child = node.getChildren().get(i);
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("label", child.getNodeName());
					//labelId contains the query string sent to back to server when a node from the tree is clicked;
					//currentOrganisationId is used to retrieve all the persons from a department and represents the 
					//organisation the department belongs to
					if ( child.getNodeType() == IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT ){
						jsonObject.accumulate("labelElId", "currentOrganisationId=" + currentOrganisationId + "&nodeId=" + child.getNodeId() + "&type=" + child.getNodeType());				
						jsonObject.accumulate("departmentId", child.getDepartmentId());	
					}
					else if ( node.getNodeType() != IConstant.NOM_TREE_NODE_TYPE_PERSON ){
						jsonObject.accumulate("labelElId", "currentOrganisationId=" + child.getNodeId() + "&nodeId=" + child.getNodeId() + "&type=" + child.getNodeType());
						jsonObject.accumulate("organizationStatus", node.getOrganizationStatus());
					}					
					jsonObject.accumulate("isLeaf", child.getChildrenNumber() == 0 ? true: false);
					jsonObject.accumulate("type", child.getNodeType());
					jsonObject.accumulate("title", child.getTitleInfo());
					
					jsonArrayNodes.add(jsonObject);
				}
				/* code to be used if displaying the persons in the organisation tree is needed
				if (returnWithEmployees && type == IConstant.NOM_TREE_NODE_TYPE_DEPARTMENT) {
					Set<Person> employees = BLDepartment.getInstance().listPersons(nodeId, currentOrganisationId);
					Iterator<Person> it = employees.iterator();
					while(it.hasNext()) {
						Person employee = it.next();
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("label", employee.getFirstName() + " " + employee.getLastName());
						jsonObject.accumulate("labelElId", "");
						jsonObject.accumulate("type", IConstant.NOM_TREE_NODE_TYPE_PERSON);
						
						jsonArrayEmployees.add(jsonObject);
					}
				}
				*/
			}
		}

		JSONObject resultSetObject = new JSONObject();
		resultSetObject.accumulate("Employees", jsonArrayEmployees);
		resultSetObject.accumulate("Nodes", jsonArrayNodes);
		resultSet.accumulate("ResultSet", resultSetObject);
		logger.debug("ResultSet " + resultSet.toString());
		
		response.setContentType("text/x-json");
		response.getWriter().write(resultSet.toString());
		
		return null;
	}

}
