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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.entity.Person;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * Organigram Get Departments Persons Controller returns a JSON object containing
 * all Persons form this Department
 * 
 * @author dan.damian
 */
public class OrganigramGetDepartmentsPersonsController extends RootAbstractController {
	
	
	private static String DEPARTMENT_ID = "departmentId";
	
	
	public OrganigramGetDepartmentsPersonsController() {
		setView("");
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		JSONArray employees = new JSONArray();
		JSONObject employee = null;
		
		int departmentId = -1;
		
		try {
			departmentId = Integer.parseInt(request.getParameter(DEPARTMENT_ID));
		} catch(NumberFormatException nfex) {
			logger.error("", nfex);
		}
		
		if (departmentId > 0) {
			Set<Person> persons = BLDepartment.getInstance().listPersons(departmentId, organisationId);
			for(Person p : persons) {
				employee = new JSONObject();
				employee.accumulate("name", p.getFirstName() + " " + p.getLastName());
				employees.add(employee);
			}
		
		}
		
		response.getWriter().print(employees.toString());
		
		return null;
	}
	
}
