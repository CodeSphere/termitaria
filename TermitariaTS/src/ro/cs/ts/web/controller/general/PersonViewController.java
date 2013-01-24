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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class PersonViewController extends RootAbstractController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final static String ROOT_KEY								= "person.";		
	private final static String GET_ERROR								= ROOT_KEY.concat("get.error");		
	private final static String ADD_ERROR								= "person.detail.add.error";
	private final static String GENERAL_ERROR							= "person.detail.general.error";
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 									= "PersonView";	

	//------------------------MODEL------------------------------------------------------------------	
	private static final String PERSON_ID								= "personId";	
	private static final String PERSON									= "PERSON";	
	private static final String PERSON_DETAIL							= "PERSON_DETAIL";	
	
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 40;	   
	
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
		
		UserSimple person = new UserSimple();
		
		PersonDetail personDetail = new PersonDetail();
					
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try{
			Integer personId = ServletRequestUtils.getIntParameter(request, PERSON_ID);			
			logger.debug("Person Id = ".concat(personId.toString()));
						
			if(personId != null && personId > 0) {
				// call the get method from the business layer
				String[] personIds = new String[1];
				personIds[0] = String.valueOf(personId);
				person = BLUser.getInstance().getUsersByPersonId(personIds).get(0);	
				
				personDetail = BLPersonDetail.getInstance().getByPersonWithCurrencies(personId);
				logger.debug("personDetail = " + personDetail);
				if(personDetail == null) {
					personDetail = handleAdd(personId, request, errorMessages);
					logger.debug("personDetail = " + personDetail);
				}				
				
				person.setFirstName(ControllerUtils.getInstance().tokenizeField(person.getFirstName(), NR_CHARS_PER_LINE));
				person.setLastName(ControllerUtils.getInstance().tokenizeField(person.getLastName(), NR_CHARS_PER_LINE));	
				person.setEmail(ControllerUtils.getInstance().tokenizeField(person.getEmail(), NR_CHARS_PER_LINE));				
				
				if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_PersonAdvancedView())){	
					if(personDetail != null) {
						if(personDetail.getObservation() != null) {
							personDetail.setObservation(ControllerUtils.getInstance().tokenizeField(personDetail.getObservation(), NR_CHARS_PER_LINE));
						}
						
						mav.addObject(PERSON_DETAIL, personDetail);
					}
				}
				
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
	
	/**
	 * Adds the person details
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	private PersonDetail handleAdd(Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleAdd - START");
				
		Integer personDetailId = null;
		PersonDetail personDetail = null;
									
		try{	
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					
			// if the user has permission TS_PersonDetailAddUpdate, can add the project's team member's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_PersonDetailAddUpdate())){				
				personDetail = new PersonDetail();
				personDetail.setPersonId(personId);
				personDetail.setStatus(IConstant.NOM_PERSON_DETAIL_STATUS_ACTIVE);
				// adding the person details to the database
				personDetailId = BLPersonDetail.getInstance().add(personDetail);		
				personDetail.setPersonDetailId(personDetailId);
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
									        	
		logger.debug("handleAdd - END");
		return personDetail;
	}

}
