/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.web.controller.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;

public class ActivityBillableController extends RootAbstractController {
		
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "ActivityBillable";
	
	private static final String PROJECT_ID						= "projectId";
	
	private static final String IS_MANAGER_FOR_PROJECT			= "IS_MANAGER_FOR_PROJECT";
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 * 
	 * @author Adelina
	 */
		
	public ActivityBillableController() {
		setView(VIEW);		
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START ");		
		
		ModelAndView mav = new ModelAndView(getView());				
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Integer userId = userAuth.getPersonId();
		
		Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);	
		
		logger.debug("ProjectId  = " + projectId);
		
		boolean isManagerForProject = false;
		
		if(projectId > 0) {
			Project project = BLProject.getInstance().get(projectId, true);
			if(project != null && (project.getManagerId() == userId || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ActivityAdvancedSearch()))) {
				isManagerForProject = true;
			}
		}
		
		logger.debug("isManagerForProject = " + isManagerForProject);
		
		mav.addObject(IS_MANAGER_FOR_PROJECT, isManagerForProject);		
						
		logger.debug("handleRequestInternal - END ");
		
		return mav;
	}

}
