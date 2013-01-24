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

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisationStructure;
import ro.cs.om.entity.OrgTree;
import ro.cs.om.entity.OrgTreeNode;
import ro.cs.om.entity.Person;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * Returns a view containing a Select with All The Organizations
 * @author dan.damian
 *
 */
public class OrganisationGetSubDepartmentsController extends RootAbstractController {

	
	public OrganisationGetSubDepartmentsController() {
		setView("Organisation_GetSubDepartments");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		JSONObject resultSet = new JSONObject();
		JSONArray  jsonArrayDepts = new JSONArray();
		JSONArray  jsonArrayEmployees = new JSONArray();
		
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		//Retrieving Organisation Tree of Departments from App Context 
		OrgTree tree = BLOrganisationStructure.getInstance().getOrgTree(organisationId);
		if (tree != null) {
			int departmentId = -1;
			try {
				departmentId = Integer.parseInt(request.getParameter("departmentId"));
			}catch(NumberFormatException nfex) {
				logger.error("", nfex);
			}
			if (departmentId > 0) {
				boolean returnWithEmployees = true;
				OrgTreeNode node = tree.getNodeFromHashMap(departmentId);
				OrgTreeNode child = null;
				for(int i = 0; i < node.getChildrenNumber(); i++) {
					child = node.getChildren().get(i);
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.accumulate("label", child.getNodeName());
					jsonObject.accumulate("labelElId", "organisationId=" + tree.getRoot().getOrganizationId() + "&departmentId=" + child.getDepartmentId());
					jsonObject.accumulate("isLeaf", child.getChildrenNumber() == 0 ? true: false);
					
					jsonArrayDepts.add(jsonObject);
				}
				if (returnWithEmployees) {
					Set<Person> employees = BLDepartment.getInstance().listPersons(departmentId, organisationId);
					Iterator<Person> it = employees.iterator();
					while(it.hasNext()) {
						Person employee = it.next();
						JSONObject jsonObject = new JSONObject();
						jsonObject.accumulate("label", employee.getFirstName() + " " + employee.getLastName());
						jsonObject.accumulate("labelElId", "");
						
						jsonArrayEmployees.add(jsonObject);
					}
				}
			}
		}

		JSONObject resultSetObject = new JSONObject();
		resultSetObject.accumulate("Employees", jsonArrayEmployees);
		resultSetObject.accumulate("Depts", jsonArrayDepts);
		resultSet.accumulate("ResultSet", resultSetObject);
		logger.debug("ResultSet " + resultSet.toString());
		
		response.setContentType("text/x-json");
		response.getWriter().write(resultSet.toString());
		
		return null;
	}

}
