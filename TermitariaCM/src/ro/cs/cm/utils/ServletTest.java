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
package ro.cs.cm.utils;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Servlet for text
 * 
 * Gets the parameters names and values
 * 
 * @author Adelina
 *
 */
public class ServletTest extends HttpServlet {
	  
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			  
		Log logger = LogFactory.getLog(getClass());
		  
	    response.setContentType("text/html");	    
	    
	    Enumeration paramNames = request.getParameterNames();
	    
	    while(paramNames.hasMoreElements()) {
	      String paramName = (String)paramNames.nextElement();
	      logger.debug("paramName = " + paramName);
	      
	      String[] paramValues = request.getParameterValues(paramName);
	      if (paramValues.length == 1) {
	        String paramValue = paramValues[0];
	        if (paramValue.length() == 0) {
	        	logger.debug("no value");
	        } else {
	          logger.debug("paramValue = " + paramValue);
	        }
	      } else {	        
	        for(int i = 0; i < paramValues.length; i++) {	         
	          logger.debug("paramValues = " + paramValues[i]);
	        }	      
	      }
	    }	    
	  }

	  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  doGet(request, response);
	  }
}
