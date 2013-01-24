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
/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.cs.cm.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionIdentifierAware;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import ro.cs.cm.business.BLUser;
import ro.cs.cm.common.ConfigParametersProvider;

/**
 * Processes a HTTP request's BASIC authorization headers, putting the result
 * into the <code>SecurityContextHolder</code>.
 * 
 * <p>
 * For a detailed background on what this filter is designed to process, refer
 * to <a href="http://www.faqs.org/rfcs/rfc1945.html">RFC 1945, Section
 * 11.1</a>. Any realm name presented in the HTTP request is ignored.
 * 
 * <p>
 * In summary, this filter is responsible for processing any request that has a
 * HTTP request header of <code>Authorization</code> with an authentication
 * scheme of <code>Basic</code> and a Base64-encoded
 * <code>username:password</code> token. For example, to authenticate user
 * "Aladdin" with password "open sesame" the following header would be
 * presented:
 * 
 * <pre>
 * 
 * Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
 * </pre>
 * 
 * <p>
 * This filter can be used to provide BASIC authentication services to both
 * remoting protocol clients (such as Hessian and SOAP) as well as standard user
 * agents (such as Internet Explorer and Netscape).
 * <p>
 * If authentication is successful, the resulting {@link Authentication} object
 * will be placed into the <code>SecurityContextHolder</code>.
 * 
 * <p>
 * If authentication fails and <code>ignoreFailure</code> is <code>false</code>
 * (the default), an {@link AuthenticationEntryPoint} implementation is called
 * (unless the <tt>ignoreFailure</tt> property is set to <tt>true</tt>). Usually
 * this should be {@link BasicProcessingFilterEntryPoint}, which will prompt the
 * user to authenticate again via BASIC authentication.
 * 
 * <p>
 * Basic authentication is an attractive protocol because it is simple and
 * widely deployed. However, it still transmits a password in clear text and as
 * such is undesirable in many situations. Digest authentication is also
 * provided by Spring Security and should be used instead of Basic
 * authentication wherever possible. See
 * {@link org.springframework.security.ui.digestauth.DigestProcessingFilter}.
 * <p>
 * Note that if a {@link RememberMeServices} is set, this filter will
 * automatically send back remember-me details to the client. Therefore,
 * subsequent requests will not need to present a BASIC authentication header as
 * they will be authenticated using the remember-me mechanism.
 * 
 * @author Ben Alex
 * @version $Id: BasicProcessingFilter.java 3184 2008-07-12 17:40:39Z luke_t $
 */
public class TokenBasedAuthenticationFilter extends GenericFilterBean implements
		InitializingBean {

	private static String PARAM_SECURITY_TOKEN = "securityToken";
	private static String CONCURRENT_SESSION_CONTROLLER_BEAN = "_concurrentSessionController";

	// ~ Instance fields
	// ================================================================================================
	private AuthenticationDetailsSource authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private AuthenticationEntryPoint authenticationEntryPoint;
	private AuthenticationManager authenticationManager;
	private RememberMeServices rememberMeServices = new NullRememberMeServices();
	private boolean ignoreFailure = false;
	private String credentialsCharset = "UTF-8";
	private static String OM_URL = ConfigParametersProvider
			.getConfigString("om.url");
	private static String LOG_OUT_URL = ConfigParametersProvider
			.getConfigString("cm.logout.url");

	/** Response returned when session is expired */
	private static String RESPONSE = "<body></body><script>if(typeof(YAHOO) != 'undefined'){YAHOO.cm.sessionExpired.show();setTimeout('window.location=\""
			+ OM_URL
			+ "\"', 1500);} else {window.location=\""
			+ OM_URL
			+ "\"}</script>";
	private static String RESPONSE_SIGN_OUT = "<body></body><script>window.location=\""
			+ OM_URL + "\"</script>";

	// ~ Methods
	// ========================================================================================================

	public void afterPropertiesSet() {
		Assert.notNull(this.authenticationManager,
				"An AuthenticationManager is required");

		if (!isIgnoreFailure()) {
			Assert.notNull(this.authenticationEntryPoint,
					"An AuthenticationEntryPoint is required");
		}
	}

	/**
	 * @author tekin 12.03.2012 - Commented the ConcurrentSessionController
	 *         functionality.
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */

	public void doFilter(ServletRequest req,
			ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		logger.debug("TokenBasedAuthenticationFilter...");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String securityToken = request.getParameter(PARAM_SECURITY_TOKEN);

		if (logger.isDebugEnabled()) {
			logger.debug("Security token: " + securityToken);
		}

		logger.debug("Authentication : "
				+ SecurityContextHolder.getContext().getAuthentication());

		if (securityToken != null) {

			Authentication authResult;

			try {
				// authResult = authenticationManager.authenticate(authRequest);
				UserAuth userAuth = BLUser.getInstance().getBySecurityToken(
						securityToken);

				authResult = new UsernamePasswordAuthenticationToken(userAuth,
						userAuth.getPassword(), userAuth.getAuthorities());

				// Checking if a user didn't overcome the maximum number of
				// allowed sessions
				// -------------------------------------------------------------------------------------
				// Because 'authenticationManager.authenticate(authRequest)' was
				// checking also for
				// the maximum number of sessions/user, we are now doing it
				// manually:
				// - setting the Details (from here the controller will extract
				// the sessionId)
				((UsernamePasswordAuthenticationToken) authResult)
						.setDetails(new SessionIdentifierAwareImpl(request
								.getSession().getId()));
				// - obtaining ConcurrentSessionController bean from Spring
				// Application Context
				// ConcurrentSessionController csc =
				// (ConcurrentSessionController) CMContext.
				// getApplicationContext().getBean(CONCURRENT_SESSION_CONTROLLER_BEAN);
				// - making the verification
				// csc.checkAuthenticationAllowed(authResult);
				// --------------------------------------------------------------------------------------

			} catch (Exception failed) {
				logger.error("Authentication failed", failed);
				// Authentication failed
				if (logger.isDebugEnabled()) {
					logger.debug("Authentication request for token: "
							+ securityToken + " failed: " + failed.toString());
				}

				SecurityContextHolder.getContext().setAuthentication(null);

				rememberMeServices.loginFail(request, response);

				if (ignoreFailure) {
					chain.doFilter(request, response);
				} else {
					authenticationEntryPoint.commence(
							request,
							response,
							new AuthenticationServiceException(failed
									.getMessage(), failed));
				}

				return;
			}

			// Authentication success
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication success: " + authResult.toString());
			}

			SecurityContextHolder.getContext().setAuthentication(authResult);

			rememberMeServices.loginSuccess(request, response, authResult);

		}

		// check if we have sign out or the session has expired
		if (securityToken == null
				&& SecurityContextHolder.getContext().getAuthentication() == null) {
			String url = request.getRequestURI();
			logger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			logger.debug("\tLAST URL: " + url);
			logger.debug("\tSession has expired !");
			logger.debug("\tRedirecting to Sign On Page !");
			logger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			response.setContentType("text/html");

			// test if session has expired or we have log out
			if (url.equals(LOG_OUT_URL)) {
				response.getWriter().write(RESPONSE_SIGN_OUT);
			} else {
				response.getWriter().write(RESPONSE);
			}
			response.getWriter().write(RESPONSE);
			response.getWriter().flush();

		} else {
			// chain do filter
			chain.doFilter(request, response);
		}
	}

	protected AuthenticationEntryPoint getAuthenticationEntryPoint() {
		return authenticationEntryPoint;
	}

	public void setAuthenticationEntryPoint(
			AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	protected AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		logger.debug("23902 " + authenticationManager);
		this.authenticationManager = authenticationManager;
	}

	protected boolean isIgnoreFailure() {
		return ignoreFailure;
	}

	public void setIgnoreFailure(boolean ignoreFailure) {
		this.ignoreFailure = ignoreFailure;
	}

	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource,
				"AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void setRememberMeServices(RememberMeServices rememberMeServices) {
		Assert.notNull(rememberMeServices, "rememberMeServices cannot be null");
		this.rememberMeServices = rememberMeServices;
	}

	public void setCredentialsCharset(String credentialsCharset) {
		Assert.hasText(credentialsCharset,
				"credentialsCharset cannot be null or empty");
		this.credentialsCharset = credentialsCharset;
	}

	protected String getCredentialsCharset(HttpServletRequest httpRequest) {
		return credentialsCharset;
	}

	/**
	 * @author tekin
	 * Commented getOrder() method 
	 */
	// public int getOrder() {
	// return FilterChainOrder.BASIC_PROCESSING_FILTER;
	// }

	private boolean authenticationIsRequired(String username) {
		// Only reauthenticate if username doesn't match SecurityContextHolder
		// and user isn't authenticated
		// (see SEC-53)
		Authentication existingAuth = SecurityContextHolder.getContext()
				.getAuthentication();

		if (existingAuth == null || !existingAuth.isAuthenticated()) {
			return true;
		}

		// Limit username comparison to providers which use usernames (ie
		// UsernamePasswordAuthenticationToken)
		// (see SEC-348)

		if (existingAuth instanceof UsernamePasswordAuthenticationToken
				&& !existingAuth.getName().equals(username)) {
			return true;
		}

		// Handle unusual condition where an AnonymousAuthenticationToken is
		// already present
		// This shouldn't happen very often, as BasicProcessingFitler is meant
		// to be earlier in the filter
		// chain than AnonymousProcessingFilter. Nevertheless, presence of both
		// an AnonymousAuthenticationToken
		// together with a BASIC authentication request header should indicate
		// reauthentication using the
		// BASIC protocol is desirable. This behaviour is also consistent with
		// that provided by form and digest,
		// both of which force re-authentication if the respective header is
		// detected (and in doing so replace
		// any existing AnonymousAuthenticationToken). See SEC-610.
		if (existingAuth instanceof AnonymousAuthenticationToken) {
			return true;
		}

		return false;
	}
}

class SessionIdentifierAwareImpl implements SessionIdentifierAware {

	private String sessionId = null;

	public SessionIdentifierAwareImpl(String sessionId) {
		this.sessionId = sessionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.concurrent.SessionIdentifierAware#getSessionId
	 * ()
	 */
	public String getSessionId() {
		return sessionId;
	}
}
