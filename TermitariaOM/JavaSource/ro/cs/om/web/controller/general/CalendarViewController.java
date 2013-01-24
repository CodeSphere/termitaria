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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLCalendar;
import ro.cs.om.business.BLFreeday;
import ro.cs.om.entity.Calendar;
import ro.cs.om.entity.FreeDay;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * Controller to view the infos about the calendar
 * @author Adelina
 *
 */

public class CalendarViewController extends RootAbstractController {
	
	private static final String VIEW 				= "CalendarView";
		
	private static final String CALENDAR 			= "CALENDAR";
	private static final String FREEDAYS 			= "FREEDAYS";
	
	/**
	 * Constructor, with the basic settings for the controller
	 * Setting the view for the controller
	 * @author Adelina
	 */
	public CalendarViewController(){
		setView(VIEW);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.debug("handleRequestInternal - START");
		
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView();		
		Calendar calendar = null;
		List<FreeDay> freeDays = null;
		Integer organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);	
		
		
		if(organisationId != null){
			logger.debug("OrganisationId :".concat(organisationId.toString()));
			calendar = BLCalendar.getInstance().getCalendarForOrganisation(organisationId);
			if(calendar != null){
				mav.addObject(CALENDAR, calendar);
				freeDays = BLFreeday.getInstance().getFreeDaysByCalendar(calendar.getCalendarId());
				mav.addObject(FREEDAYS, freeDays);
			}		
		}
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);				
		logger.debug("handleRequestInternal - END");
		
		return mav;
	}
}



