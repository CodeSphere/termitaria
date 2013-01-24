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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLFreeday;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.FreeDay;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;



/*
 * @author Adelina
 */

public class FreeDayListingController extends RootAbstractController{
		
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String ROOT_KEY								= "freeday.";
	private final String DELETE_MESSAGE							= ROOT_KEY.concat("delete.message");	
	
	private final String GET_ERROR								= ROOT_KEY.concat("get.error");
	private final String DELETE_ERROR							= ROOT_KEY.concat("delete.error");
	private final String DELETE_EXCEPTION						= ROOT_KEY.concat("delete.exception");
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "FreeDayListing";	
	private static final String FREEDAYS						= "FREEDAYS";
	
	
	private static final String ACTION						  	= "action";
	private static final String GET							  	= "get";
	private static final String DELETE							= "delete";
	
	private static final String CALENDAR_ID						="calendarId";
	private static final String FREEDAY_ID						="freedayId";
	
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 * @author Adelina
	 */
	public FreeDayListingController() {
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
		
		try{
							
			String action = ServletRequestUtils.getStringParameter(request, ACTION);		
			logger.debug("Action: " + action);
			
			Integer calendarId = ServletRequestUtils.getIntParameter(request, CALENDAR_ID);
			logger.debug("CalendarId: ".concat(calendarId.toString()));
			
			mav.addObject(CALENDAR_ID, calendarId);
						
			if((action != null) && (calendarId != null) && (GET.equals(action))){					
				logger.debug("are calendar_id");						
				List<FreeDay> freeDays = BLFreeday.getInstance().getFreeDaysByCalendar(calendarId);									
				mav.addObject(FREEDAYS, freeDays);
			} else if((action != null) && (DELETE.equals(action))){
				Integer freedayId = ServletRequestUtils.getIntParameter(request, FREEDAY_ID);
				if(freedayId != null){
					logger.debug("FreedayId: " + freedayId.toString());							
					handleDelete(request, errorMessages, infoMessages);
					List<FreeDay> freeDays = BLFreeday.getInstance().getFreeDaysByCalendar(calendarId);									
					mav.addObject(FREEDAYS, freeDays);
				}
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
	
	private void handleDelete(HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleDelete - START");
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			

			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_FreedayDelete())){			
				int freedayId = ServletRequestUtils.getIntParameter(request, FREEDAY_ID);
				BLFreeday.getInstance().delete(freedayId);			
				infoMessages.add(messageSource.getMessage(DELETE_MESSAGE, null, RequestContextUtils.getLocale(request)));
				
				//add the new audit event
				try {
					Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_FREEDAY_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_FREEDAY_DELETE_MESSAGE, new Object[] {org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_FREEDAY_DELETE_MESSAGE, new Object[] {org.getName()}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
			}
			
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(DELETE_EXCEPTION, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("handleDelete - END");
	}	
}
