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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootAbstractController;

public class ClientGetNumberOfDaysController extends RootAbstractController {
		
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "ClientGetNumberOfDays";
	
	private static final String MONTH							= "month";
	private static final String YEAR							= "year";
	private static final String DAY								= "day";
	private static final String MODEL_DAYS 						= "Days";
	private static final String SELECTED_DAY					= "SELECTED_DAY";		
	
	
	/**
	 * Contstructor - with the basic settings for the controller.
	 * 
	 * @author Adelina
	 */
		
	public ClientGetNumberOfDaysController() {
		setView(VIEW);		
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START ");		
		
		ModelAndView mav = new ModelAndView(getView());		
		
		List<String> days = new ArrayList<String>();
									
		Integer monthNum = ServletRequestUtils.getIntParameter(request, MONTH) + 1;	
		Integer year = ServletRequestUtils.getIntParameter(request, YEAR);
		Integer day = ServletRequestUtils.getIntParameter(request, DAY);		
		
		logger.debug("day = " + day);
		logger.debug("month = " + monthNum);
		logger.debug("year = " + year);					
				
		mav.addObject(SELECTED_DAY, day);
		
		
		Integer numberOfDays = ControllerUtils.getInstance().getNumOfDays(year, monthNum);
		
		//the remaining days
		for(int i = 1; i <= numberOfDays; i++) {
			days.add(Integer.toString(i));
		}
		
		mav.addObject(MODEL_DAYS, days);		
						
		logger.debug("handleRequestInternal - END ");
		
		return mav;
	}

}

