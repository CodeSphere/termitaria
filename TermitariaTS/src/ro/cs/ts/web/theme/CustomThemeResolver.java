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
package ro.cs.ts.web.theme;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.theme.SessionThemeResolver;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.web.security.UserAuth;

/**
 * @author Adelina
 *
 */
public class CustomThemeResolver extends SessionThemeResolver {
	
	
	protected static Log logger = LogFactory.getLog(CustomThemeResolver.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.theme.SessionThemeResolver#resolveThemeName(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String resolveThemeName(HttpServletRequest request) {
		logger.debug("resolveThemeName");
		UserAuth userAuth = null;
		
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
			
		if (userAuth != null) {
			return userAuth.getThemeCode();
		} else {
			return IConstant.STANDARD_THEME;
		} 
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.theme.AbstractThemeResolver#getDefaultThemeName()
	 */
	@Override
	public String getDefaultThemeName() {
		logger.debug("getDefaultThemeName");
		return IConstant.STANDARD_THEME;
	}

}
