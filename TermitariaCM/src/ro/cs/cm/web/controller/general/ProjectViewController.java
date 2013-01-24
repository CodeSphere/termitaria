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
package ro.cs.cm.web.controller.general;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLProject;
import ro.cs.cm.entity.Project;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootAbstractController;

public class ProjectViewController extends RootAbstractController {
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "project.";		
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "ProjectView";	

	//------------------------MODEL------------------------------------------------------------------	
	private static final String PROJECT_ID						= "projectId";
	private static final String PROJECT							= "PROJECT";	
	
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 40;	   
	
    /**
	 * Contstructor - with the basic settings for the controller.
	 * @author Adelina
	 */
	public ProjectViewController() {
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
		
		Project project = new Project();
		
		try{			
			Integer projectId = ServletRequestUtils.getIntParameter(request, PROJECT_ID);
			logger.debug("ProjectId: ".concat(projectId.toString()));
			
			mav.addObject(PROJECT_ID, projectId);
			
			if(projectId != null) {
				// call the get method from the business layer
				project = BLProject.getInstance().getForView(projectId);										
			
				// tokenize fields
				project.setName(ControllerUtils.getInstance().tokenizeField(project.getName(), NR_CHARS_PER_LINE));
				if(project.getObservation() != null) {
					project.setObservation(ControllerUtils.getInstance().tokenizeField(project.getObservation(), NR_CHARS_PER_LINE));
				}
				if(project.getDescription() != null) {
					project.setDescription(ControllerUtils.getInstance().tokenizeField(project.getDescription(), NR_CHARS_PER_LINE));
				}
				
				mav.addObject(PROJECT, project);
				
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
