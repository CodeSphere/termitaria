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

import org.apache.ws.security.util.UUIDGenerator;
import org.springframework.security.core.token.Token;



/**
 *  <strong>SecurityToken</strong>
 *  <p>Security token for allowing cross modules authentication and authorization.</p>
 *	
 *  @author dan.damian
 *
 */
public class SecurityToken implements Token {

	private String username;
	private String key;
	private long keyCreationTime;


	public SecurityToken(String username) {
		key = UUIDGenerator.getUUID();
		keyCreationTime = System.currentTimeMillis();
		this.username = username;
	}
	

	/* (non-Javadoc)
	 * @see org.springframework.security.token.Token#getKey()
	 */
	public String getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.token.Token#getKeyCreationTime()
	 */
	public long getKeyCreationTime() {
		return keyCreationTime;
	}

	
	/* (non-Javadoc)
	 * @see org.springframework.security.token.Token#getExtendedInformation()
	 */
	public String getExtendedInformation() {
		return username;
	}
}
