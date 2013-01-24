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
package ro.cs.logaudit.web.controller.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.logaudit.web.controller.root.RootAbstractController;

/**
 * @author alu
 * @author Adelina
 * @author coni
 * 
 */
public class ShowTimeController extends RootAbstractController {

	private static final String VIEW = "Show_Time";
	private static final String PANEL = "panel";
	private static final String START_HOUR = "start_h";
	private static final String START_MINUTES = "start_m";
	private static final String END_HOUR = "end_h";
	private static final String END_MINUTES = "end_m";
	
	private static final String SHOW_ALL_MINUTES = "showAllMinutes";
	private static final String CALENDAR_PAIR_NO = "pairNr";

	private static final String MODEL_HOURS = "hours";
	private static final String MODEL_MINUTES = "minutes";

	public ShowTimeController() {
		setView(VIEW);
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START");

		ModelAndView mav = new ModelAndView(getView());
		mav.addObject(PANEL, ServletRequestUtils.getStringParameter(request,
				PANEL));

		if (request.getParameter(START_HOUR) != null) {
			mav.addObject(START_HOUR, ServletRequestUtils.getStringParameter(
					request, START_HOUR));
		} else {
			mav.addObject(START_HOUR, 0);
		}
		
		if (request.getParameter(END_HOUR) != null) {
			mav.addObject(END_HOUR, ServletRequestUtils.getStringParameter(
					request, END_HOUR));
		} else {
			mav.addObject(END_HOUR, 23);
		}

		if (request.getParameter(START_MINUTES) != null) {
			mav.addObject(START_MINUTES, ServletRequestUtils
					.getStringParameter(request, START_MINUTES));
		} else {
			mav.addObject(START_MINUTES, 0);
		}
		
		if (request.getParameter(END_MINUTES) != null) {
			mav.addObject(END_MINUTES, ServletRequestUtils
					.getStringParameter(request, END_MINUTES));
		} else {
			if (request.getParameter(SHOW_ALL_MINUTES) != null && "true".equals(request.getParameter(SHOW_ALL_MINUTES))){
				mav.addObject(END_MINUTES, 59);
			} else {
				mav.addObject(END_MINUTES, 45);
			}
			
		}

		String[] hours = new String[] { "00", "01", "02", "03", "04", "05",
				"06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
				"16", "17", "18", "19", "20", "21", "22", "23" };
		
		String[] minutes = null;
		
		// check if I have to show all minutes from 00 to 59 or {00, 15, 30, 45}
		if (request.getParameter(SHOW_ALL_MINUTES) != null && "true".equals(request.getParameter(SHOW_ALL_MINUTES))){
			minutes = new String[60] ;
			for (int i = 0; i < 60; i++) {
				if (i < 10) {
					minutes[i] = "0".concat(String.valueOf(i)); 
				} else {
					minutes[i] = String.valueOf(i);
				}
			} 
		} else {
			minutes = new String[] { "00", "15", "30", "45" };
		}
		
		mav.addObject(MODEL_HOURS, hours);
		mav.addObject(MODEL_MINUTES, minutes);
		mav.addObject(SHOW_ALL_MINUTES, request.getParameter(SHOW_ALL_MINUTES));
		mav.addObject(CALENDAR_PAIR_NO, request.getParameter(CALENDAR_PAIR_NO));
		
		logger.debug("handleRequestInternal - END");
		return mav;
	}	
}
