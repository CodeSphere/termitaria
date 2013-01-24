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
package ro.cs.logaudit.web.controller.general;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.logaudit.web.controller.root.ControllerUtils;
import ro.cs.logaudit.web.controller.root.RootAbstractController;

public class GetJsonPersonsController extends RootAbstractController{
	
	private static final String VIEW = "GetJsonPersons";
	private static final String ORGANISATION_ID = "organisationId";
	
	public GetJsonPersonsController(){
		setView(VIEW);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal START");
		
		//used as a container for info/error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		// add all the persons from the selected organization for autoComplete			
		response.setContentType("text/x-json");
		String jsonPersons = ControllerUtils.getInstance().getPersonsFromOrgAsJSON(new Integer(request.getParameter(ORGANISATION_ID)), RequestContextUtils.getLocale(request), errorMessages, messageSource);
		response.getWriter().write(jsonPersons);
		setErrors(request, errorMessages);
		
		logger.debug("handleRequestInternal END");
		return null;
	}
}
