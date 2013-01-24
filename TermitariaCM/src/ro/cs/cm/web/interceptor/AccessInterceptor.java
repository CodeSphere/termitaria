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
package ro.cs.cm.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import ro.cs.cm.common.IConstant;
import ro.cs.cm.web.security.UserAuth;

/**
 * @author matti_joona
 *
 * Folosit pentru redirectarea in pagina de autentificare daca utilizatorul nu are credentialele pe sesiune
 */
public class AccessInterceptor extends HandlerInterceptorAdapter{

	private static Log logger = LogFactory.getLog(AccessInterceptor.class);
	private static String SIGNON_VIEW = "SignOn.htm";
	private static String SIGNON_REDIRECT_VIEW = "SignOnRedirect";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String url = request.getServletPath();
    	logger.debug("URL: ".concat(url));
    	UserAuth userAuth = (UserAuth) WebUtils.getSessionAttribute(request, UserAuth.KEY);
        if (userAuth != null) {
        	logger.debug("Session with user.");
             return true;
        } else {
            if (IConstant.APP_START_PAGE.equals(url)) {
            	logger.debug("Start page");
            	ModelAndView modelAndView = new ModelAndView(SIGNON_VIEW);
        		throw new ModelAndViewDefiningException(modelAndView);
            }
        	logger.debug("Session without user!");
            logger.debug("Redirecting to SignOn page...");
            ModelAndView modelAndView = new ModelAndView(SIGNON_REDIRECT_VIEW);
            		throw new ModelAndViewDefiningException(modelAndView);
        }
    }
}
