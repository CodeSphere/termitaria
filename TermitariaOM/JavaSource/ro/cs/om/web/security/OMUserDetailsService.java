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
package ro.cs.om.web.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ro.cs.om.business.BLAuthorization;
import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.entity.Module;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoAuthorization;
import ro.cs.om.model.dao.IDaoPerson;

/**
 * Concrete implementation of org.springframework.security.userdetails.UserDetailsService interface.
 * It provides all UserDetails. It's used with Spring Security.
 * 
 * @author dan.damian
 *
 */
public class OMUserDetailsService extends ApplicationObjectSupport implements UserDetailsService {

	
	
	private IDaoPerson personDao = DaoBeanFactory.getInstance().getDaoPerson();
	private IDaoAuthorization authorizationDao = DaoBeanFactory.getInstance().getDaoAuthorization();
	
	/* (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		logger.info("== LOAD USER BY USERNAME ==");
		UserAuth userAuth = null;
		
		try{
			//Get UserAuth form DB
			userAuth = personDao.getUserAuthByUsername(username);
			if (userAuth != null) {
				//Setting Modules to which this user has access to
				if (authorizationDao.isUserAdminIT(userAuth.getPersonId())) {
					logger.debug("User is ADMIN IT");
					userAuth.setAdminIT(true);
					userAuth.setModules(BLAuthorization.getInstance().getAllModules());
				} else {
					logger.debug("User is not ADMIN IT");
					userAuth.setModules(BLAuthorization.getInstance().getAccessModules(userAuth.getPersonId()));
					for(Module m : userAuth.getModules()){
						System.out.println("\t module: " + m.getModuleId() + " ; " + m.getAlt());
					}
				}
				
				GrantedAuthority[] auths = new GrantedAuthority[1];
				auths[0] = new SimpleGrantedAuthority("IS_AUTHENTICATED_ANONYMOUSLY");
				userAuth.setAuthorities(auths); 
			} 
		} catch(Exception ex) {
			logger.error("", ex);
		}
		if (userAuth == null) {
			//Throw exception if user not found in DB
			throw new UsernameNotFoundException("User " +  username + " not found !");
		}
		return userAuth;
	}

}
