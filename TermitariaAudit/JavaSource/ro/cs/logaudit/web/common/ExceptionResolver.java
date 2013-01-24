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
package ro.cs.logaudit.web.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * @author matti_joona
 * 
 * Controller: Returneaza un view setat din DISPATCHER-servlet.xml 
 * in cazul in care unul din controllerele definite in DISPATCHER 
 * arunca o exceptie.
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver{

	private static Log logger = LogFactory.getLog(ExceptionResolver.class);
    /**
    
     * Returneaza view-ul setat din DISPATCHER-servlet.xml Aici se pot face si
     * alte operatii custom, inainte de a returna view-ul.
     */
    public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object object, Exception exception) {

    	logger.error("--------------------------- EXCEPTIE -----BEGIN----------------------------------");
        logger.error("EXCEPTIE", exception);
        logger.error("--------------------------- EXCEPTIE -----END------------------------------------");
    	return super.resolveException(request, response, object, exception);
    }
	
}
