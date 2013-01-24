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
package ro.cs.om.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * Controller: Return a view setted in dispatcher-servlet 
 * 
 * @author matti_joona
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver{

	private Log logger = LogFactory.getLog(getClass());
	
    /**
     * This method is used for different operations before returning the view
     * 
     * @author matti_joona
     */
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object object, Exception exception) {

    	logger.error("--------------------------- EXCEPTIE -----BEGIN----------------------------------");
        logger.error("EXCEPTION:", exception);
        logger.error("--------------------------- EXCEPTIE -----END------------------------------------");
    	return super.resolveException(request, response, object, exception);
    }
	
}
