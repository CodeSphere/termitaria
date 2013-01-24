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
package ro.cs.cm.web.security;

import java.lang.reflect.Method;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ro.cs.cm.common.ApplicationObjectSupport;


public class CMUserDetailsService extends ApplicationObjectSupport implements UserDetailsService {

	
	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		logger.info("== LOAD USER BY USERNAME ==");
		UserAuth userAuth = null;
		try{
			logger.debug("userAuth - start");
			userAuth = getUserAuthByName(username);
			logger.debug("userAuth - end");
		} catch(Exception ex) {
			logger.error("", ex);
		}
		if (userAuth == null) {
			//Throw exception if user not found in DB
			throw new UsernameNotFoundException("User " +  username + " not found !");
		}
		return userAuth;
	}


	private UserAuth getUserAuthByName(String username) {
		
		UserAuth userAuth = null;
		
		try {
			Method method = Class.forName(this.getClass().getName()).getDeclaredMethod("_" + username.toUpperCase(), new Class[]{});
			userAuth = (UserAuth) method.invoke(this, new Object[] {});
			logger.debug(userAuth);
		} catch (Exception e) {
			logger.error("", e);
		}
		
		if (userAuth != null) {
			userAuth.setAccountNonExpired(true);
			userAuth.setAccountNonLocked(true);
			userAuth.setCredentialsNonExpired(true);
			userAuth.setEnabled(true);
			userAuth.setThemeCode("standard");
		}
		
		return userAuth;
	}
	
	
	private UserAuth _USER01() {
		logger.debug("_USER01");
		UserAuth userAuth = new UserAuth();
		userAuth.setPersonId(2);
		userAuth.setUsername("user01");
		userAuth.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
		userAuth.setFirstName("Emilian");
		userAuth.setLastName("Bok");
				
		return userAuth;
	}


	private UserAuth _USER02() {
		logger.debug("_USER02");
		UserAuth userAuth = new UserAuth();
		userAuth.setUsername("user02");
		userAuth.setPersonId(3);
		userAuth.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
		userAuth.setFirstName("Andrei");
		userAuth.setLastName("Vidreanu");
		
		//Permisiuni
		GrantedAuthority[] authories = new SimpleGrantedAuthority[7];
		authories[0] = new SimpleGrantedAuthority("SUPER");
		authories[1] = new SimpleGrantedAuthority("DM_BASIC");
		authories[2] = new SimpleGrantedAuthority("DM_CategoryDelete");
		authories[3] = new SimpleGrantedAuthority("DM_CategoryAdd");
		authories[4] = new SimpleGrantedAuthority("DM_CategoryUpdate");
		authories[5] = new SimpleGrantedAuthority("DM_DocumentAdd");
		authories[6] = new SimpleGrantedAuthority("DM_CollectionZoneView");
		
		userAuth.setAuthorities(authories);
		
		return userAuth;
	}


	private UserAuth _USER03() {
		logger.debug("_USER03");
		UserAuth userAuth = new UserAuth();
		userAuth.setUsername("user03");
		userAuth.setPersonId(3);
		userAuth.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
		userAuth.setFirstName("Ilie");
		userAuth.setLastName("Nastasescu");
		
		//Permisiuni
		GrantedAuthority[] authories = new SimpleGrantedAuthority[1];
		authories[0] = new SimpleGrantedAuthority("AUDIT_BASIC");
		
		userAuth.setAuthorities(authories);
		
		return userAuth;
	}

	
	private UserAuth _USER04() {
		logger.debug("_USER04");
		UserAuth userAuth = new UserAuth();
		userAuth.setUsername("user04");
		userAuth.setPersonId(4);
		userAuth.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
		userAuth.setFirstName("Grabriel");
		userAuth.setLastName("Contabitza");
		
		//Permisiuni
		GrantedAuthority[] authories = new SimpleGrantedAuthority[1];
		authories[0] = new SimpleGrantedAuthority("DM_BASIC");
		
		userAuth.setAuthorities(authories);
		
		return userAuth;
	}
	
	private UserAuth _USER05() {
		logger.debug("_USER05");
		UserAuth userAuth = new UserAuth();
		userAuth.setUsername("user05");
		userAuth.setPersonId(5);
		userAuth.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
		userAuth.setFirstName("Mircea");
		userAuth.setLastName("Badescu");

		//Permisiuni
		GrantedAuthority[] authories = new SimpleGrantedAuthority[1];
		authories[0] = new SimpleGrantedAuthority("DM_BASIC");
		
		userAuth.setAuthorities(authories);
		
		return userAuth;
	}


	private UserAuth _ADMIN() {
		logger.debug("ADMIN");
		UserAuth userAuth = new UserAuth();
		userAuth.setPersonId(1);
		userAuth.setUsername("admin");
		userAuth.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
		userAuth.setFirstName("Traian");
		userAuth.setLastName("Besescu");
		userAuth.setOrganisationId(27);
		userAuth.setOrganisationName("SIAD");
		
		//Permisiuni
		GrantedAuthority[] authories = new SimpleGrantedAuthority[12];
		authories[0] = new SimpleGrantedAuthority("SUPER");
		authories[1] = new SimpleGrantedAuthority("DM_BASIC");
		authories[2] = new SimpleGrantedAuthority("DM_CategoryDelete");
		authories[3] = new SimpleGrantedAuthority("DM_CategoryAdd");
		authories[4] = new SimpleGrantedAuthority("DM_CategoryUpdate");
		authories[5] = new SimpleGrantedAuthority("DM_DocumentAdd");
		authories[6] = new SimpleGrantedAuthority("DM_CollectionZoneView");
		authories[7] = new SimpleGrantedAuthority("AUDIT_OMDelete");
		authories[8] = new SimpleGrantedAuthority("AUDIT_DMDelete");
		authories[9] = new SimpleGrantedAuthority("AUDIT_OMView");
		authories[10] = new SimpleGrantedAuthority("AUDIT_DMView");
		authories[11] = new SimpleGrantedAuthority("AUDIT_Basic");
		
		userAuth.setAuthorities(authories);

		return userAuth;
	}
	
	private UserAuth _ADMINIT() {
		logger.debug("ADMIN_IT");
		UserAuth userAuth = new UserAuth();
		userAuth.setPersonId(1);
		userAuth.setUsername("adminIT");
		userAuth.setPassword("7c4a8d09ca3762af61e59520943dc26494f8941b");
		userAuth.setFirstName("Gica");
		userAuth.setLastName("Popescu");		
		userAuth.setAdminIT(true);
		
		//Permisiuni
		GrantedAuthority[] authories = new SimpleGrantedAuthority[9];
		authories[0] = new SimpleGrantedAuthority("SUPER");
		authories[1] = new SimpleGrantedAuthority("DM_BASIC");
		authories[2] = new SimpleGrantedAuthority("DM_CategoryDelete");
		authories[3] = new SimpleGrantedAuthority("DM_CategoryAdd");
		authories[4] = new SimpleGrantedAuthority("DM_CategoryUpdate");
		authories[5] = new SimpleGrantedAuthority("DM_DocumentAdd");
		authories[6] = new SimpleGrantedAuthority("DM_CollectionZoneView");
		authories[7] = new SimpleGrantedAuthority("AUDIT_OMView");
		authories[8] = new SimpleGrantedAuthority("AUDIT_DMView");
		authories[9] = new SimpleGrantedAuthority("AUDIT_Basic");
		
		userAuth.setAuthorities(authories);

		return userAuth;
	}
}
