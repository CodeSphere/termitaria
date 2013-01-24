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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * @author alu
 * @author Adelina
 *
 */
public class OOOShowTimeController extends RootAbstractController{
	
	private static final String VIEW = "OOO_ShowTime";
	private static final String PANEL = "panel";
	private static final String START_HOUR = "start_h";
	private static final String START_MINUTES = "start_m";
	
	private static final String MODEL_HOURS = "hours";
	private static final String MODEL_MINUTES = "minutes";
	
	public OOOShowTimeController(){
		setView(VIEW);
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		ModelAndView mav = new ModelAndView(getView());
		mav.addObject(PANEL, ServletRequestUtils.getStringParameter(request, PANEL));
		
		if (request.getParameter(START_HOUR) != null) {
			mav.addObject(START_HOUR, ServletRequestUtils.getStringParameter(request, START_HOUR));
		} else {
			mav.addObject(START_HOUR, 0);
		}
		
		if (request.getParameter(START_MINUTES) != null) {
			mav.addObject(START_MINUTES, ServletRequestUtils.getStringParameter(request, START_MINUTES));
		} else {
			mav.addObject(START_MINUTES, 0);
		}
		
		String[] hours = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
				"12", "13","14", "15","16", "17","18", "19","20", "21","22", "23"};
		String[] minutes = new String[] {"00", "15", "30", "45"};
		
		mav.addObject(MODEL_HOURS, hours);
		mav.addObject(MODEL_MINUTES, minutes);
		
		logger.debug("handleRequestInternal - END");
		return mav;
	}	
}
