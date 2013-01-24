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

import org.springframework.web.servlet.ModelAndView;

import ro.cs.logaudit.web.controller.root.RootAbstractController;

/**
 * @author matti_joona
 * 
 * Folosit pentru afisarea unui view
 */
public class GeneralController extends RootAbstractController{
	
	private String theView = null;

    public void setTheView(String theView) {
        this.theView = theView;
    }
       
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse response) throws Exception {
        
    	if (theView == null) {
            throw new IllegalArgumentException("viewName is required");
        }
        logger.debug("Returnez view ul " + theView);
        return new ModelAndView(theView);
    }
    
    protected String getTheView(){
    	return theView;
    }
    
}
