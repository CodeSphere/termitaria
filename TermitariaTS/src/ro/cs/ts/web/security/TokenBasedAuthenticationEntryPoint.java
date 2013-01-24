/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.common.ConfigParametersProvider;

/**
 * TokenBasedAuthenticationEntryPoint Used by the
 * <code>SecurityEnforcementFilter</code> to commence authentication via the
 * {@link BasicProcessingFilter}.
 * <P>
 * Once a user agent is authenticated using BASIC authentication, logout
 * requires that the browser be closed or an unauthorized (401) header be sent.
 * The simplest way of achieving the latter is to call the
 * {@link #commence(ServletRequest, ServletResponse, AuthenticationException)}
 * method below. This will indicate to the browser its credentials are no longer
 * authorized, causing it to prompt the user to login again.
 * </p>
 * 
 * @author dan.damian
 */
public class TokenBasedAuthenticationEntryPoint extends ApplicationObjectSupport implements	AuthenticationEntryPoint, InitializingBean {

	
	private static String ORGANIZATION_MANAGEMENT_MODULE_LOCATION = ConfigParametersProvider.getConfigString("om.url");
	
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException arg2)
			throws IOException, ServletException {
		logger.debug("============ exit ! ============");
		response.getWriter().write("<script>window.location = '".concat(ORGANIZATION_MANAGEMENT_MODULE_LOCATION).concat("';</script>"));
		
	}

}
