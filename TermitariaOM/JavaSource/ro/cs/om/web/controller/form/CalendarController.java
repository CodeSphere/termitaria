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
package ro.cs.om.web.controller.form;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLCalendar;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Calendar;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * 
 * SimpleFormController used for Calendar entity.
 * 
 * @author Adelina
 *
 */
public class CalendarController extends RootSimpleFormController {
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "calendar.";
	private final String UPDATE_SUCCESS							= ROOT_KEY.concat("update.success");
	
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");
	private final String UPDATE_ERROR							= ROOT_KEY.concat("update.error");

	//------------------------VIEW------------------------------------------------------------------
	private static final String FORM_VIEW 						= "Calendar";
	private static final String SUCCESS_VIEW 					= "Calendar";	
	
	private static final String ACTION						  	= "action";	
	private static final String EDIT							= "edit";
	private static final String SAVE							= "save";
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 */
	public CalendarController() {
		// for use in .jsp
		setCommandName("calendarBean"); 
		setCommandClass(Calendar.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * initBinder - for bindings request
	 * 
	 * @author Adelina
	 * @param request
	 * @param binder
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception{		
		// logger debug
		logger.debug("initBinder START - calendar Controller");
		// no bindings 
		logger.debug("initBinder END - calendar Controller ");		
	}
	
	/**
	 * formBackingObject - for every request
	 * 
	 * @author Adelina 
	 * @param request
	 */	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception{
		// logger debug
		logger.debug("formBackingObject - START");
		
		// used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		Locale locale = RequestContextUtils.getLocale(request);
		Calendar calendar = null;
		
		try{		
			String action = ServletRequestUtils.getStringParameter(request, ACTION);
			logger.debug("Action: " + action);
			if((action != null) && (EDIT.equals(action))){					
				logger.debug("are organisation_id");					
				calendar = handleGetByOrganisation(request, errorMessages, infoMessages, locale);													
				return calendar;
			} 
		} catch(ServletRequestBindingException e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		logger.debug("formBackingObject - END ");
		return super.formBackingObject(request);		
	}
	
	
	/**
	 * onSubmit - method which is called when submitting the form
	 *
	 * @author Adelina
	 * @param request
	 * @param response
	 * @param command
	 * @param errors 
	 * @return ModelAndView
	 */	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit START - calendar Controller");
		ModelAndView mav = new ModelAndView();
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
				
		Locale locale = RequestContextUtils.getLocale(request);
		
		Calendar calendar = (Calendar) command;
		// the action of the request
		String action = ServletRequestUtils.getStringParameter(request, ACTION);
		logger.debug("Action: " + action);
		
		// check if i have an action
		if(action != null){
			logger.debug("action != null");
			// redirect depending the operation action
			if((SAVE).equals(action)){				
				// check if i have calendar Id
				if(calendar.getCalendarId() != -1){
					logger.debug("calendar id != -1");
					// if a i have calendar id, it means that i have "update" action
					mav = handleUpdate(calendar, locale, errorMessages, infoMessages, request);
				} 
			}
		}					
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		mav.addObject(getCommandName(), calendar);
		
		logger.debug("onsubmit END - calendar Controller");
		return mav;
	}
		
	/**
	 * Method for update request
	 *
	 * @author Adelina
	 * @param calendar	 
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return ModelAndView
	 */	
	@SuppressWarnings("unused")
	private ModelAndView handleUpdate(Calendar calendar, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request) throws Exception {
		logger.debug("handleUpdate - START");
		
		ModelAndView mav = new ModelAndView();
		
		try{
			//method add from business layer	
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_CalendarUpdate())){
				Organisation organisation = new Organisation();
				Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
				organisation.setOrganisationId(organisationId);
				calendar.setOrganisationId(organisationId);
				BLCalendar.getInstance().update(calendar);
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {calendar.getCalendarId()}, locale));
				
				//add the new audit event
				try {
					Organisation org = BLOrganisation.getInstance().get(organisationId);
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_CALENDAR_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_CALENDAR_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_CALENDAR_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
							, organisationId , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}			
		
		logger.debug("handleUpdate - END");
		
		return mav;
	}
		
	/**
	 * Get the calendar by organisation
	 * 
	 * @author Adelina
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return Calendar
	 */
	@SuppressWarnings("unused")
	private Calendar handleGetByOrganisation(HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START-");
		
		Calendar calendar = null;		
		
		ModelAndView mav = new ModelAndView();
		
		try{
			// call the business layer	
			Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			logger.debug("organisationId: ".concat(organisationId.toString()));
			calendar = BLCalendar.getInstance().getCalendarForOrganisation(organisationId);			
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}		
		logger.debug("handleGet - END-");
		
		return calendar;
	}
}
