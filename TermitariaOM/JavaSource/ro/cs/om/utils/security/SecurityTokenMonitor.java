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

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.token.Token;

import ro.cs.om.common.ConfigParametersProvider;
import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;

/**
 *  <strong>SecurityTokenMonitor</strong>
 *  <p>Monitors all generated tokens. All expired tokens are removed.</p>
 *	
 *  @author dan.damian
 *
 */
public class SecurityTokenMonitor extends Thread {

	private Log logger = LogFactory.getLog(getClass());
	
	private static SecurityTokenMonitor theInstance;
	
	private SecurityTokenMonitor() {
		setName("Security Token Monitor");
		tokenRepository = (ConcurrentHashMap<String, Token>) 
					OMContext.getFromContext(IConstant.SECURITY_TOKEN_REPOSITORY);
	}
	
	static {
		theInstance = new SecurityTokenMonitor();
	}
	
	public static SecurityTokenMonitor getInstance() {
		return theInstance;
	}
	
	private static long AVAILABILITY = ConfigParametersProvider.getConfigStringAsInt(IConstant.SECURITY_TOKEN_AVAILABILITY);
	
	private ConcurrentHashMap<String, Token> tokenRepository;
	
	//1 Minute
	private long sleepDuration = 60 * 1000;  
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		super.start();
		logger.debug(getName().concat(" started..."));
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		Token t = null;
		try {
			while(true) {
				Thread.currentThread().sleep(sleepDuration);
				Enumeration<String> keys = tokenRepository.keys();
				while(keys.hasMoreElements()) {
					t = tokenRepository.get(keys.nextElement());
					if ( (System.currentTimeMillis() - t.getKeyCreationTime()) > AVAILABILITY) {
						tokenRepository.remove(t.getKey());
						logger.debug("\ttoken ".concat(t.getKey().concat(" removed!")));
					}
				}
			}
		} catch(Exception e) {
			logger.error("", e);
		}
	}
}
