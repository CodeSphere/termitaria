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
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.Scheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLOutOfOffice;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 *
 */
public class OOOListForPersonController extends RootAbstractController{

	private static final String VIEW 					= "OOO_ListForPerson";
	private static final String ERROR 					= "ooo.listForPerson.error";
	private static final String DELETE_JOB_ERROR 		= "ooo.delete.job.error";
	private static final String DELETE_JOB_MESSAGE 		= "ooo.delete.message";
	private static final String ID 						= "personId";
	private static final String NO_ID_MESSAGE 			= "ooo.noId.message";
	private static final String OOO_PROFILES 			= "OOO_PROFILES";	
	private static final String ACTION 					= "action";
	private static final String DELETE 					= "delete";
	private static final String OOO_ID 					= "oooId";	
	private static final String FIRST_NAME 				= "FIRST_NAME";
	private static final String LAST_NAME 				= "LAST_NAME";	;
	private static final String NAME_TITLE 				= "NAME_TITLE";
	private static final String USERNAME				= "USERNAME";
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS				= 20;
	
	public OOOListForPersonController(){
		setView(VIEW);
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START ");
		
		ModelAndView mav = new ModelAndView(getView());
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
			
		// check if I have delete action
		if ((request.getParameter(ACTION) != null) && (DELETE.equals(request.getParameter(ACTION)))){
			handleDelete(request, Integer.parseInt(request.getParameter(OOO_ID)),
					Integer.parseInt(request.getParameter(ID)), errorMessages, infoMessages, RequestContextUtils.getLocale(request));
		}			
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int personId = -1;
		// check if user is adminIT
		if (userAuth.isAdminIT()){ 
			// if it is, check if  I have personId parameter
			if (request.getParameter(ID) != null){
				personId = ServletRequestUtils.getIntParameter(request, ID);
			} else {
				// if I don't have personId on request, show a message!
				errorMessages.add(messageSource.getMessage(NO_ID_MESSAGE, null, RequestContextUtils.getLocale(request)));
				setErrors(request, infoMessages);
				logger.debug("handleRequestInternal - END");
				return mav;
			}
		} else {
			// if user is not an adminIT, get user's id from session
			personId = userAuth.getPersonId();
		}			
		
		mav = handleListing(personId, request);				 
			
		// put on mav the URI of the request							
		mav.addObject(USERNAME, userAuth.getUsername());								
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		logger.debug("handleRequestInternal - END");
		return mav;
	}
	
	private void handleDelete(HttpServletRequest request, int oooId, int personId, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale){
		logger.debug("handleDelete OOOListForPersonController- START");
		try {			
						
			// get person's username
			Person person = BLPerson.getInstance().get(personId);
			String username = person.getUsername();
			// get the scheduler from context
			Scheduler sched = (Scheduler)OMContext.getFromContext(IConstant.SCHEDULER);
			// delete the out of office profile
			BLOutOfOffice.getInstance().delete(oooId);
			// delete the job			
			String jobName = String.valueOf(username).concat("_").concat(String.valueOf(oooId)); 
			sched.deleteJob(jobName, String.valueOf(username).concat("_Group"));					
			
			infoMessages.add(messageSource.getMessage(DELETE_JOB_MESSAGE, null, locale));
			logger.debug("JOB DELETED - job name: ".concat(jobName));
			
			//add the new audit event
			try {
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_OOO_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
						, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_DELETE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_DELETE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
						, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (BusinessException exc){
				logger.error(exc);
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(DELETE_JOB_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(DELETE_JOB_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("handleDelete - END");
	}
	
	
	public ModelAndView handleListing(Integer personId, HttpServletRequest request) {
		
		ArrayList<String> errorMessages = new ArrayList<String>();		
				
		ModelAndView mav = new ModelAndView(getView());		
		// put on mav person's out of office profiles
		try{
			// put on mav person's out of office profiles
			List<OutOfOffice> list = BLOutOfOffice.getInstance().getByPersonID(personId);
			mav.addObject(OOO_PROFILES, list);
			// put on mav person's username
			if (list != null && list.size() != 0){
				// all OOOs should have the same person	
				mav.addObject(FIRST_NAME, ControllerUtils.getInstance().truncateName(((OutOfOffice)list.get(0)).getPerson().getFirstName(), NR_CHARS ));
				mav.addObject(LAST_NAME, ControllerUtils.getInstance().truncateName(((OutOfOffice)list.get(0)).getPerson().getFirstName(), NR_CHARS ));							
				mav.addObject(NAME_TITLE,((OutOfOffice)list.get(0)).getPerson().getFirstName() + " " + ((OutOfOffice)list.get(0)).getPerson().getFirstName());						
			} else {
				Person p = BLPerson.getInstance().get(personId);
				mav.addObject(FIRST_NAME, ControllerUtils.getInstance().truncateName(p.getFirstName(), NR_CHARS));
				mav.addObject(LAST_NAME, ControllerUtils.getInstance().truncateName(p.getLastName(), NR_CHARS));			
				mav.addObject(NAME_TITLE, p.getFirstName() + " " + p.getLastName());				
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		
		return mav;
	}
}
	
