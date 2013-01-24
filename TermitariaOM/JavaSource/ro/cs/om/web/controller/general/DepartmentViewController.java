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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.entity.Department;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 *  Controller to view the infos for a department
 * @author Adelina
 *
 */
public class DepartmentViewController extends RootAbstractController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "department.";		
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "DepartmentView";	

	//------------------------MODEL------------------------------------------------------------------	
	private static final String DEPARTMENT_ID					= "departmentId";
	private static final String DEPARTMENT						= "DEPARTMENT";
	
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 40;	   
	
    /**
	 * Contstructor - with the basic settings for the controller.
	 * @author Adelina
	 */
	public DepartmentViewController() {
		setView(VIEW);		
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse result) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		// used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
				
		ModelAndView mav = new ModelAndView();
		
		Department department = null;
		
		try{						
			Integer departmentId = ServletRequestUtils.getIntParameter(request, DEPARTMENT_ID);
			logger.debug("DepartmentId: ".concat(departmentId.toString()));
			
			mav.addObject(DEPARTMENT_ID, departmentId);
						
			if(departmentId != null){					
				logger.debug("are departmentId");						
				department = BLDepartment.getInstance().getAll(departmentId);				
				department.setName(ControllerUtils.getInstance().tokenizeField(department.getName(), NR_CHARS_PER_LINE));		
				if(department.getObservation() != null) {
					department.setObservation(ControllerUtils.getInstance().tokenizeField(department.getObservation(), NR_CHARS_PER_LINE));
				}
				mav.addObject(DEPARTMENT, department);				
			}			
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}	
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);		
		
		logger.debug("handleRequestInternal - END");
		
		return mav;
	}			
}
