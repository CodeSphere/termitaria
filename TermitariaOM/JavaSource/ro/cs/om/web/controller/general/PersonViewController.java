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
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLPicture;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Picture;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 *  Controller to view the infos for a person
 * @author Adelina
 *
 */
public class PersonViewController extends RootAbstractController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "person.";		
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "PersonView";	

	//------------------------MODEL------------------------------------------------------------------	
	private static final String PERSON_ID						= "personId";
	private static final String PERSON							= "PERSON";	
	private static final String PERSON_JOBS						= "PERSON_JOBS";
	private static final String DEPT_JOBS						= "DEPT_JOBS";
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 50;
	
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 * @author Adelina
	 */
	public PersonViewController() {
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
		
		Person person = null;
		
		try{						
			Integer personId = ServletRequestUtils.getIntParameter(request, PERSON_ID);
			logger.debug("PersonId: ".concat(personId.toString()));
			
			mav.addObject(PERSON_ID, personId);
						
			if(personId != null){					
				logger.debug("are personId");		
				person = BLPerson.getInstance().getAll(personId);
				Picture picture = BLPicture.getInstance().getPictureByPersonId(personId);
				person.setPicture(picture);
				BLPerson.getInstance().excludeFakeDepartment(person);
				
				person.setFirstName(ControllerUtils.getInstance().tokenizeField(person.getFirstName(), NR_CHARS_PER_LINE));		
				person.setLastName(ControllerUtils.getInstance().tokenizeField(person.getLastName(), NR_CHARS_PER_LINE));	
				
				if(person.getAddress() != null) {
					person.setAddress(ControllerUtils.getInstance().tokenizeField(person.getAddress(), NR_CHARS_PER_LINE));
				}
				if(person.getObservation() != null) {
					person.setObservation(ControllerUtils.getInstance().tokenizeField(person.getObservation(), NR_CHARS_PER_LINE));
				}
				
				List<String> jobs = new ArrayList<String>();
				StringBuffer sb = new StringBuffer();	
				Map<Department,Job> deptWithJob = person.getDeptWithJob();
				if(deptWithJob == null) {
					sb.append("-");
					jobs.add(sb.toString());	
				} else {					
					Set<Department> departments = deptWithJob.keySet();
					for(Department d : departments) {
						sb = new StringBuffer();
						sb.append(deptWithJob.get(d).getName().concat(" - ").concat(d.getName()).concat(" "));
						jobs.add(sb.toString());						
					}
				}
				
				mav.addObject(DEPT_JOBS, jobs);	
				
				//Exclude the default user group from the list of user groups
				BLPerson.getInstance().excludeDefaultUserGroup(person);
				
				mav.addObject(PERSON_JOBS,ControllerUtils.getInstance().getPersonJobs(person));
				mav.addObject(PERSON, person);
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
