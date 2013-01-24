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
package ro.cs.om.web.interceptor;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ro.cs.om.common.IConstant;
import ro.cs.om.web.common.HttpInspector;


/**
 * Folosita pentru afisarea informatiilor atribute/parametrii de pe request/sesiune
 * la fiecare request
 * 
 * @author dd
 */
public class HttpInterceptor extends HandlerInterceptorAdapter{
	
    private Log logger = LogFactory.getLog(getClass());

    private ResourceBundle rb = ResourceBundle.getBundle("config.httpinterceptor");

    private boolean ON = new Boolean(rb.getString(IConstant.HTTP_INTERCEPTOR_ON)).booleanValue();

    public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1, Object arg2) throws Exception {
    	if (ON) {
            logger.debug(HttpInspector.inspect(request));
        }
    	return true;
    }
}
