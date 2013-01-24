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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLFreeday;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Calendar;
import ro.cs.om.entity.FreeDay;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;



/*
 * SimpleFormController used for Freeday entity.
 * @author Adelina
 */

public class FreeDayController extends RootSimpleFormController{
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "freeday.";
	
	private final String ADD_SUCCESS							= ROOT_KEY.concat("add.success");			
	private final String UPDATE_SUCCESS							= ROOT_KEY.concat("update.success");
	
	private final String ADD_ERROR								= ROOT_KEY.concat("add.error");		
	private final String ADD_ALREDAY_EXISTS_ERROR				= ROOT_KEY.concat("add.already.exists.error");
	private final String UPDATE_ERROR							= ROOT_KEY.concat("update.error");
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");	
	
	
		
	//------------------------VIEW------------------------------------------------------------------
	private static final String FORM_VIEW 						= "FreeDay";
	private static final String LISTING_VIEW 					= "FreeDayListing";
	private static final String SUCCESS_VIEW 					= "FreeDay";
	private static final String FREEDAYS						= "FREEDAYS";
	private static final String GET_ACTION 						= "GET_ACTION";	
	
	private static final String ACTION						  	= "action";
	private static final String SAVE							= "save";
	private static final String ADD								= "add";
	private static final String EDIT							= "edit";

	private static final String FREEDAY_ID						= "freedayId";
	private static final String CALENDAR_ID						= "calendarId";
	
	
	
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 */
	public FreeDayController() {
		// for use in .jsp
		setCommandName("freedayBean"); 
		setCommandClass(FreeDay.class);
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
		logger.debug("initBinder START - freeDay Controller");
		
		// bindings
		CustomDateEditor dateEditor = new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true);
		binder.registerCustomEditor(Date.class, "day", dateEditor);
		
		logger.debug("initBinder END - freeDay Controller ");		
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
		logger.debug("formBackingObject FREEDAY- START");
		
		// used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		Locale locale = RequestContextUtils.getLocale(request);
		FreeDay freeday = null;
		
		try{
			String action = ServletRequestUtils.getStringParameter(request, ACTION);
			//Integer freedayId = ServletRequestUtils.getIntParameter(request, FREEDAY_ID);
			logger.debug("Action: " + action);
			
			if((action!= null) && (EDIT.equals(action)) && (ServletRequestUtils.getIntParameter(request, FREEDAY_ID) != null)){										
				//Integer freedayId = ServletRequestUtils.getIntParameter(request, FREEDAY_ID);
				//logger.debug("from the request freeday id = " + freedayId);
				freeday = handleGet(request, errorMessages, infoMessages, locale);
				//logger.debug("freeday id = " + freeday.getFreeDayId());	
				//logger.debug("calendarId = " + freeday.getCalendar().getCalendarId());
				return freeday;
			} else{
				if (ADD.equals(ServletRequestUtils.getStringParameter(request, ACTION))){
					freeday = new FreeDay();
					Calendar calendar = new Calendar();
					calendar.setCalendarId((Integer)ServletRequestUtils.getIntParameter(request, CALENDAR_ID));
					freeday.setCalendar(calendar);
					return freeday;
				}
			}				
		} catch(ServletRequestBindingException e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		logger.debug("formBackingObject FREEDAY - END ");
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
		logger.debug("onSubmit START - freeday Controller");
		ModelAndView mav = new ModelAndView();
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
				
		Locale locale = RequestContextUtils.getLocale(request);
		
		FreeDay freeday = (FreeDay) command;
		// the action of the request
		String action = ServletRequestUtils.getStringParameter(request, ACTION);
		logger.debug("Action: " + action);
		
		// check if i have an action
		if(action != null){
			logger.debug("action != null");
			// redirect depending the operation action
			if((SAVE).equals(action)){
				logger.debug("save action");								
				// check if i have freeday Id
				if(freeday.getFreeDayId() > 0){
					logger.debug("freeday id > 0");					
					// if a i have freeday id, it means that i have "update" action
					logger.debug("we have an update action");
					logger.debug("freeday id = " + freeday.getFreeDayId());	
					logger.debug("calendarId = " + freeday.getCalendar().getCalendarId());
					mav = handleUpdate(freeday, locale, errorMessages, infoMessages, request);
					
				} else {		
					logger.debug("freeday id < 0");
					// if i don't have freeday id, it means i have "add" action
					logger.debug("we have an add action");
					mav =  handleAdd(freeday, locale, errorMessages, infoMessages, request);			
				}
			} 
		}					
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		mav.addObject(getCommandName(), freeday);
		
		logger.debug("onsubmit END - freeday Controller");
		return mav;
	}
	
	/**
	 * Method for add freeDay
	 *
	 * @author Adelina
	 * @param freeday	 
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return ModelAndView
	 */	
	
	@SuppressWarnings("unused")
	private ModelAndView handleAdd(FreeDay freeday, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request) throws Exception {
		logger.debug("handleAdd - START");		
		ModelAndView mav = new ModelAndView(LISTING_VIEW);
		boolean isEqual = false;
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_FreedayAdd())){
				// method add from business layer
				isEqual = searchExistentFreeDay(freeday);
				if(isEqual == false){
					BLFreeday.getInstance().add(freeday);
					infoMessages.add(messageSource.getMessage(ADD_SUCCESS, new Object[] {freeday.getFreeDayId()}, locale));
					
					//add the new audit event
					try {
						Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
						if (!userAuth.isAdminIT()){
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_FREEDAY_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_FREEDAY_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_FREEDAY_ADD_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
								, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
						}
					} catch (BusinessException exc){
						logger.error(exc);
					}
				} else {
					errorMessages.add(messageSource.getMessage(ADD_ALREDAY_EXISTS_ERROR, new Object[] {freeday.getFreeDayId()}, locale));
				}
				List<FreeDay> freeDays = BLFreeday.getInstance().getFreeDaysByCalendar(freeday.getCalendar().getCalendarId());
				mav.addObject(FREEDAYS, freeDays);
				mav.addObject(CALENDAR_ID, freeday.getCalendar().getCalendarId());
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("handleAdd - END");		
		
		return mav;
	}
		
	/**
	 * Method for update request
	 *
	 * @author Adelina
	 * @param freeday	 
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return ModelAndView
	 */	
	@SuppressWarnings("unused")
	private ModelAndView handleUpdate(FreeDay freeday, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request) throws Exception {
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(LISTING_VIEW);
		boolean isEqual = false;
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_FreedayUpdate())){
				//method add from business layer
				isEqual = searchExistentFreeDay(freeday);
				if(isEqual == false){
					BLFreeday.getInstance().update(freeday);
					infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {freeday.getFreeDayId()}, locale));
					
					//add the new audit event
					try {
						Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
						if (!userAuth.isAdminIT()){
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_FREEDAY_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_FREEDAY_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_FREEDAY_UPDATE_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
								, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
						}
					} catch (BusinessException exc){
						logger.error(exc);
					}
				} else {
					errorMessages.add(messageSource.getMessage(ADD_ALREDAY_EXISTS_ERROR, new Object[] {freeday.getFreeDayId()}, locale));
				}
				
				List<FreeDay> freeDays = BLFreeday.getInstance().getFreeDaysByCalendar(freeday.getCalendar().getCalendarId());									
				mav.addObject(FREEDAYS, freeDays);
				mav.addObject(CALENDAR_ID, freeday.getCalendar().getCalendarId());
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);	
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("handleUpdate - END");
		
		return mav;
	}
	
	
	
	/**
	 * Get the freeday
	 * 
	 * @author Adelina
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return Freeday
	 */
	@SuppressWarnings("unused")
	private FreeDay handleGet(HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START-");
		FreeDay freeday = null;			
		try{
			// call the business layer	
			if(request.getParameter(FREEDAY_ID) != null){
				Integer freedayId = ServletRequestUtils.getIntParameter(request, FREEDAY_ID);
				logger.debug("freedayId: ".concat(freedayId.toString()));
				freeday = BLFreeday.getInstance().get(freedayId);				
			}
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch(ServletRequestBindingException e){
			logger.error(e.getMessage(), e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {"", ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));		
		}
		request.setAttribute(GET_ACTION, true);
		logger.debug("handleGet - END-");
		
		return freeday;
	}
	/**
	 * 
	 * Checks if a freeday already exists
	 * @author mihai
	 * @param freeday
	 * @return
	 * @throws BusinessException
	 */
	private boolean searchExistentFreeDay(FreeDay freeday) throws BusinessException{
		boolean isEqual = false;
		List<FreeDay> freeDays = BLFreeday.getInstance().getFreeDaysByCalendar(freeday.getCalendar().getCalendarId());
		logger.debug("freeday date : " + freeday.getDay());
		for(FreeDay fd : freeDays){ 
			logger.debug("day : " + fd.getDay());
			if((fd.getDay().compareTo(freeday.getDay())) != 0){					
				isEqual = false;			
			} else {
				if(fd.getFreeDayId() != freeday.getFreeDayId()) {
					isEqual = true;
					break;
				} 
			}
		}
		return isEqual;
	}
		
}

