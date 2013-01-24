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
package ro.cs.ts.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import ro.cs.ts.entity.SearchPersonBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;
import ro.cs.ts.ws.client.om.entity.WSGrantedAuthorityImpl;
import ro.cs.ts.ws.client.om.entity.WSGrantedAuthorityImplArray;
import ro.cs.ts.ws.client.om.entity.WSUserAuth;

/**
 *  BLUser
 *	
 *  @author dan.damian
 *
 */
public class BLUser extends BusinessLogic {

	
	
	//singleton implementation
    private static BLUser theInstance = null;
  
    private BLUser(){};
    static {
        theInstance = new BLUser();
    }
    public static BLUser getInstance() {
    	return theInstance;
    }
    
    
    /**
     * Retrieves a User from Organization Management by sending
     * a security token.
     * 
     * @param securityToken
     * @return
     * @throws BusinessException
     */
    public UserAuth getBySecurityToken(String securityToken) throws BusinessException {
    	logger.debug("getBySecurityToken BEGIN");
    	UserAuth userAuth = new UserAuth();
    	try {
    		WSUserAuth wsUserAuth = OMWebServiceClient.getInstance().getUserAuthBySecurityToken(securityToken).getUserAuth();
    		userAuth.setPersonId(wsUserAuth.getPersonId());
    		userAuth.setUsername(wsUserAuth.getUsername());
    		userAuth.setAccountNonExpired(wsUserAuth.isAccountNonExpired());
    		userAuth.setAccountNonLocked(wsUserAuth.isAccountNonLocked());
    		userAuth.setCredentialsNonExpired(wsUserAuth.isCredentialsNonExpired());
    		userAuth.setEnabled(wsUserAuth.isEnabled());
    		userAuth.setEmail(wsUserAuth.getEmail());
    		
    		
    		WSGrantedAuthorityImplArray authoritiesArray = wsUserAuth.getAuthoritiesArray();
    		if (authoritiesArray != null) {
	    		List<WSGrantedAuthorityImpl> authorities = authoritiesArray.getAuthorities();
	    		if (authorities != null) {
	    			List<GrantedAuthority> authoritiesForUser = new ArrayList<GrantedAuthority>();
	    			
		    		for(int i = 0; i < authorities.size(); i++) {
		    			authoritiesForUser.add(new GrantedAuthorityImpl(authorities.get(i).getAuthority()));
		    		}
		    		userAuth.setAuthorities(authoritiesForUser);
	    		}
    		}
    		
    		userAuth.setFirstName(wsUserAuth.getFirstName());
    		userAuth.setLastName(wsUserAuth.getLastName());
    		if ("M".equals(wsUserAuth.getSex())) {
    			userAuth.setSex('M');
    		} else if ("F".equals(wsUserAuth.getSex())) {
    			userAuth.setSex('F');
    		}
    		if (wsUserAuth.getBirthDate() != null) {
    			userAuth.setBirthDate(wsUserAuth.getBirthDate().toGregorianCalendar().getTime());
    		}
    		userAuth.setAddress(wsUserAuth.getAddress());
    		userAuth.setPhone(wsUserAuth.getPhone());
    		userAuth.setObservation(wsUserAuth.getObservation());
    		userAuth.setThemeCode(wsUserAuth.getThemeCode());
    		userAuth.setOrganisationId(wsUserAuth.getOrganisationId());
    		userAuth.setOrganisationName(wsUserAuth.getOrganisationName());
    		userAuth.setOrganisationAddress(wsUserAuth.getOrganisationAddress());
    		userAuth.setAdminIT(wsUserAuth.isAdminIT());
    		userAuth.setOrganisationModulesIds(wsUserAuth.getOrganisationModulesIds());
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.USER_GET_BY_SECURITY_TOKEN, e);
    	}
    	logger.debug("getBySecurityToken END");
    	return userAuth;
    }
    
    public List<UserSimple> getUsersSimpleByOrganizationId(int organizationId, boolean isNotDeleted) throws BusinessException {
    	logger.debug("getUsersSimpleByOrganizationId START");
    	List<UserSimple> users = null;
    	try {
    		users = OMWebServiceClient.getInstance().getUsersSimpleByOrganizationId(organizationId, isNotDeleted);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.USER_GET_BY_ORGANIZATION_ID, e);
    	}
    	logger.debug("getUsersSimpleByOrganizationId END");
    	return users;
    }
    
    /**
     * Returns from Organization Management Module all users that coresponds to 
     * this list of user ids (personId).
     * 
     * @author Coni
     * 
     * @param userIds
     * @return
     * @throws BusinessException 
     */
    public List<UserSimple> getUsersByPersonId(String[] personIds) throws BusinessException {
    	List<UserSimple> users = null;
    	try {
    		users = OMWebServiceClient.getInstance().getUsersSimpleByPersonId(personIds).getUsers();
    	}catch(Exception e) {
    		throw new BusinessException(ICodeException.USER_GET_BY_PERSON_IDS, e);
    	}
    	return users;
    }
    
    /**
     * Gets a list of users by different search criteria
     * @author Coni
     * @param organizationId
     * @param firstName
     * @param lastName
     * @return
     * @throws BusinessException
     */
    public List<UserSimple> getUserSimpleFromSearch(int organizationId, String firstName, String lastName, boolean withDeleted) throws BusinessException {
    	logger.debug("getUserSimpleFromSearch START");
    	List<UserSimple> users = null;
    	try {
			SearchPersonBean spb = new SearchPersonBean();
			spb.setOrganizationId(organizationId);
			spb.setFirstName(firstName);
			spb.setLastName(lastName);
			spb.setWithDeleted(withDeleted);
    		users = OMWebServiceClient.getInstance().getPersonFromSearch(spb).getPersons();
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.USER_GET_FROM_SEARCH, e);
    	}
    	logger.debug("getUserSimpleFromSearch END");
    	return users;
    }
}
