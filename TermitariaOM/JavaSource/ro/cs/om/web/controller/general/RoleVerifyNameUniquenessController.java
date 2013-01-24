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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ro.cs.om.business.BLRole;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Role;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.security.UserAuth;

/**
 * @author mitziuro
 * Verify uniqueness of this role name in the actual organisation
 */
public class RoleVerifyNameUniquenessController extends AbstractController{

	private static String CMD_VERIFY_UNIQUENESS_OF_ROLE_NAME 			= 	"198341";
	private static String CMD_VERIFY_UNIQUENESS_OF_ROLE_ORGANISATION 	= 	"198342";
	private static String ID											 = 	"id";
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getParameter(CMD_VERIFY_UNIQUENESS_OF_ROLE_NAME) != null) {
			verifyRoleNameUniqueness(request, response); 
		}
		return null;
	}

	private void verifyRoleNameUniqueness(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String name = request.getParameter(CMD_VERIFY_UNIQUENESS_OF_ROLE_NAME);		
		String organisation = request.getParameter(CMD_VERIFY_UNIQUENESS_OF_ROLE_ORGANISATION);		
		logger.debug("Role Name: ".concat(name));
		logger.debug("OrganisationId: ".concat(organisation));
		
		Role role = null;
		Integer organisationId =null;
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(userAuth.isAdminIT()){
			organisationId  = Integer.valueOf(organisation);
		} else {
			organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		}
		try {
			 role = BLRole.getInstance().getRoleByNameAndOrg(organisationId, name);
			 if (request.getParameter(ID) != null && role != null) {
				 if(Integer.valueOf(request.getParameter(ID)) == role.getRoleId()) {
					 role = null;
				 }
			 }
			
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			response.getOutputStream().write("<p>ERROR</p>".getBytes());
			return;
		}
		
		if (role!=null) {
			response.getOutputStream().write("<p>YES</p>".getBytes());
		} else {
			response.getOutputStream().write("<p>NO</p>".getBytes());
		}
		
	}
}
