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
package ro.cs.om.utils.security;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.token.Token;

import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;

/**
 *  SecurityTokenUtils
 *	
 *  @author dan.damian
 *
 */
public class SecurityTokenManager {

	
	private static SecurityTokenManager theInstance;
	
	private SecurityTokenManager() {
		tokenRepository = (ConcurrentHashMap<String, Token>) 
			OMContext.getFromContext(IConstant.SECURITY_TOKEN_REPOSITORY);
	}
	
	static {
		theInstance = new SecurityTokenManager();
	}
	
	public  static SecurityTokenManager getInstance() {
		return theInstance;
	}
	
	private ConcurrentHashMap<String, Token> tokenRepository;
	
	
	public String generateSecurityToken(String username) {
		Token t = new SecurityToken(username);
		tokenRepository.put(t.getKey(), t);
		return t.getKey();
	}
	
	public String getUsernameForToken(String securityToken) {
		Token t = tokenRepository.get(securityToken);
		return t.getExtendedInformation();
	}
	
	public void invalidateToken(String securityToken) {
		tokenRepository.remove(securityToken);
	}
}
