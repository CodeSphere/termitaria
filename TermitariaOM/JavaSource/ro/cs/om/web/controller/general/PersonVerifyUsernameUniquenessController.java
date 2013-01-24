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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ro.cs.om.business.BLPerson;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;

/**
 * @author dd
 * Verify uniqueness of this username
 */
public class PersonVerifyUsernameUniquenessController extends AbstractController{

	private static String CMD_VERIFIY_UNIQUENESS_OF_USER_NAME = "198341";
	private static String ID = "id";
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter(CMD_VERIFIY_UNIQUENESS_OF_USER_NAME) != null) {
			verifiyUserNameUniqueness(request, response); 
		}
		return null;
	}

	
	private void verifiyUserNameUniqueness(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter(CMD_VERIFIY_UNIQUENESS_OF_USER_NAME);
		logger.debug("User Name: ".concat(username));
		Person p = null;
		
		try {
			 p = BLPerson.getInstance().getByUsername(username);
			 if (request.getParameter(ID) != null && p != null) {
				 if(Integer.valueOf(request.getParameter(ID)) == p.getPersonId()) {
					 p = null;
				 }
			 }
			
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			response.getOutputStream().write("<p>ERROR</p>".getBytes());
			return;
		}
		
		if (p!=null) {
			response.getOutputStream().write("<p>YES</p>".getBytes());
		} else {
			response.getOutputStream().write("<p>NO</p>".getBytes());
		}
		
	}
}
