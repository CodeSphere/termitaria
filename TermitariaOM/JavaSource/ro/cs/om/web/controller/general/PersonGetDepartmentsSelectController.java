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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * @author dan.damian
 *
 */
public class PersonGetDepartmentsSelectController extends RootAbstractController {
		
	//----------------------------REQUEST ATTRIBUTES-----------------
	private final String ORGANISATION_ID				= "organisationId";
	
	//----------------------------MODEL ATTRIBUTES-------------------
	private static final String JSON_DEPARTMENTS 		= "JSON_DEPARTMENTS";		
	
	public PersonGetDepartmentsSelectController() {
		setView("Person_SearchDepartmentsSelect");
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(getView());		
		
		List<String> errorMessages = new ArrayList<String>();
		
		Integer organizationId  = ServletRequestUtils.getIntParameter(request, ORGANISATION_ID);	
		
		String departments = ControllerUtils.getInstance().getDepartmentsWithFakeFromOrgAsJSON(request, organizationId, RequestContextUtils.getLocale(request), (ArrayList<String>)errorMessages, messageSource);
			
		if(organizationId != null && organizationId > 0) {	
			
			//Only if we have something to display we put it on mav
			if(departments != null && departments.length() > 2) {
				mav.addObject(JSON_DEPARTMENTS, departments);		
			} else {				
				mav.addObject(JSON_DEPARTMENTS, null);
			}
		}
		
		setErrors(request, errorMessages);
		return mav;
	}
		
}
