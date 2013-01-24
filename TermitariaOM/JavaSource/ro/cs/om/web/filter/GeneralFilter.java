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
package ro.cs.om.web.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

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
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import ro.cs.om.common.IConstant;

/**
 * Keep general settings on request/session and delete attributes with limited
 * scope
 * 
 * @author matti_joona
 */
public class GeneralFilter implements Filter {

	private Log logger = LogFactory.getLog(getClass());

	private FilterConfig fc;

	private static final String CHAR_ENCODING = "UTF-8";

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		logger.debug("doFilter - START - xxxxx ");
		HttpServletResponse response = (HttpServletResponse) res;

		// set the provided HTTP response parameters
		for (Enumeration<String> e = fc.getInitParameterNames(); e
				.hasMoreElements();) {
			String headerName = (String) e.nextElement();
			response.addHeader(headerName, fc.getInitParameter(headerName));
		}

		interceptLanguagePreference((HttpServletRequest) req);

		log(req);
		// setam enconding pentru request
		req.setCharacterEncoding(CHAR_ENCODING);
		// setam si encoding pentru response
		res.setCharacterEncoding(CHAR_ENCODING);
		chain.doFilter(req, response);
		logger.debug("doFilter - END - xxxxx ");
	}

	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
	}

	public void destroy() {
		this.fc = null;
	}

	private void log(ServletRequest req) {
		logger.debug("-------------------------------------------------------------");
		logger.debug(req.getRemoteHost() + "(" + req.getRemoteAddr() + "):"
				+ req.getRemotePort());
	}

	/**
	 * Intercepts the selected language
	 * 
	 * @author Adelina
	 * @param request
	 */
	private void interceptLanguagePreference(HttpServletRequest request) {
		logger.debug("interceptLanguagePreference - START - ");
		if (request.getParameter(IConstant.LANGUAGE_ATTRIBUTE) != null) {
			logger.debug("siteLanguage: " + request.getParameter(IConstant.LANGUAGE_ATTRIBUTE));
			// put on session
			request.getSession().setAttribute(
					SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
					new Locale(request
							.getParameter(IConstant.LANGUAGE_ATTRIBUTE)));
		}
		logger.debug("interceptLanguagePreference - END - ");
	}
}
