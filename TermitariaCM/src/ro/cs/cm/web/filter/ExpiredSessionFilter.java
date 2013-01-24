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
package ro.cs.cm.web.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.cs.cm.common.IConstant;

/**
 * Returns a Response with Javascript redirecting instructions when a User's Session has expired.
 * 
 * @author dan.damian
 */
public class ExpiredSessionFilter implements Filter{
	
	private FilterConfig fc;
	private Log logger = LogFactory.getLog(getClass());
	
	/** Allowed Urls */
	private static Map<String, Object> allowedUrls = new HashMap<String, Object>();
	
	static {
		allowedUrls.put("/".concat(IConstant.APP_CONTEXT).concat("/SignOn.htm"), new Object());
		allowedUrls.put("/".concat(IConstant.APP_CONTEXT).concat("/Main.htm"), new Object());
		
	}
	
	/**Spring Security authenticated user context name*/
	private String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
	
	
	/**Response returned*/
	private static String RESPONSE = "<script>" +
			" if (YAHOO.cm.sessionExpired)" +
			" { YAHOO.cm.sessionExpired.show(); setTimeout('window.location=\\'SignOn.htm\\'', 1500); }" + 
			" </script>";

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) res;
		HttpServletRequest  request  = (HttpServletRequest) req;
		String url = request.getRequestURI();
    			      		
    	if ( allowedUrls.get(url) == null && 
    			request.getSession().getAttribute(SPRING_SECURITY_CONTEXT) == null) {
    		logger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    		logger.debug("\tLAST URL: " + url);
        	logger.debug("\tSession has expired !");
    		logger.debug("\tRedirecting to Sign On Page !");
    		logger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    		
    		response.setContentType("text/plain");
    		response.getWriter().write(RESPONSE);
    		response.getWriter().flush();
    		response.getWriter().close();
    		
    	}
    	
		chain.doFilter(req, response);
	}

	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
	}

	public void destroy() {
		this.fc = null;
	}
}
