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
package ro.cs.om.ws.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.StopWatch;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import ro.cs.om.business.BLAuthorization;
import ro.cs.om.business.BLCalendar;
import ro.cs.om.business.BLFreeday;
import ro.cs.om.business.BLLogo;
import ro.cs.om.business.BLModule;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLRole;
import ro.cs.om.business.BLUserGroup;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Calendar;
import ro.cs.om.entity.FreeDay;
import ro.cs.om.entity.Logo;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchPersonBean;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.EndpointException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.utils.security.SecurityTokenManager;
import ro.cs.om.web.security.UserAuth;
import ro.cs.om.ws.server.entity.GetAllOrganisationsRequest;
import ro.cs.om.ws.server.entity.GetAllOrganisationsResponse;
import ro.cs.om.ws.server.entity.GetCalendarByOrganizationRequest;
import ro.cs.om.ws.server.entity.GetCalendarByOrganizationResponse;
import ro.cs.om.ws.server.entity.GetDeletedUsersAndGroupsByOrganizationRequest;
import ro.cs.om.ws.server.entity.GetFreeDaysRequest;
import ro.cs.om.ws.server.entity.GetFreeDaysResponse;
import ro.cs.om.ws.server.entity.GetLogoRequest;
import ro.cs.om.ws.server.entity.GetLogoResponse;
import ro.cs.om.ws.server.entity.GetOrganisationSimpleRequest;
import ro.cs.om.ws.server.entity.GetOrganisationSimpleResponse;
import ro.cs.om.ws.server.entity.GetOrganisationsByModuleRequest;
import ro.cs.om.ws.server.entity.GetOrganisationsByModuleResponse;
import ro.cs.om.ws.server.entity.GetPersonFromSearchRequest;
import ro.cs.om.ws.server.entity.GetPersonFromSearchResponse;
import ro.cs.om.ws.server.entity.GetPersonSimpleRequest;
import ro.cs.om.ws.server.entity.GetPersonSimpleResponse;
import ro.cs.om.ws.server.entity.GetPersonsByRoleRequest;
import ro.cs.om.ws.server.entity.GetPersonsByRoleResponse;
import ro.cs.om.ws.server.entity.GetUserAuthBySecurityTokenRequest;
import ro.cs.om.ws.server.entity.GetUserAuthBySecurityTokenResponse;
import ro.cs.om.ws.server.entity.GetUserAuthRequest;
import ro.cs.om.ws.server.entity.GetUserAuthResponse;
import ro.cs.om.ws.server.entity.GetUserGroupByIdRequest;
import ro.cs.om.ws.server.entity.GetUserGroupByIdResponse;
import ro.cs.om.ws.server.entity.GetUserGroupsByUserRequest;
import ro.cs.om.ws.server.entity.GetUserGroupsByUserResponse;
import ro.cs.om.ws.server.entity.GetUsersAndGroupsByOrganizationRequest;
import ro.cs.om.ws.server.entity.GetUsersAndGroupsByOrganizationResponse;
import ro.cs.om.ws.server.entity.GetUsersByOrganizationRequest;
import ro.cs.om.ws.server.entity.GetUsersByOrganizationResponse;
import ro.cs.om.ws.server.entity.GetUsersSimpleByGroupRequest;
import ro.cs.om.ws.server.entity.GetUsersSimpleByGroupResponse;
import ro.cs.om.ws.server.entity.GetUsersSimpleByPersonIdRequest;
import ro.cs.om.ws.server.entity.GetUsersSimpleByPersonIdResponse;
import ro.cs.om.ws.server.entity.GetUsersSimpleRequest;
import ro.cs.om.ws.server.entity.GetUsersSimpleResponse;
import ro.cs.om.ws.server.entity.GetUsersWithRoleByOrganisationRequest;
import ro.cs.om.ws.server.entity.GetUsersWithRoleByOrganisationResponse;
import ro.cs.om.ws.server.entity.ObjectFactory;
import ro.cs.om.ws.server.entity.OrganisationHasAuditModuleRequest;
import ro.cs.om.ws.server.entity.OrganisationHasAuditModuleResponse;
import ro.cs.om.ws.server.entity.OrganisationSimple;
import ro.cs.om.ws.server.entity.UserSimple;
import ro.cs.om.ws.server.entity.WSCalendar;
import ro.cs.om.ws.server.entity.WSGrantedAuthorityImpl;
import ro.cs.om.ws.server.entity.WSGrantedAuthorityImplArray;
import ro.cs.om.ws.server.entity.WSLogo;
import ro.cs.om.ws.server.entity.WSOrganisation;
import ro.cs.om.ws.server.entity.WSSearchPersonBean;
import ro.cs.om.ws.server.entity.WSUser;
import ro.cs.om.ws.server.entity.WSUserAuth;
import ro.cs.om.ws.server.entity.WSUserOrGroup;
import ro.cs.om.ws.server.entity.WidgetUserAuthenticationRequest;
import ro.cs.om.ws.server.entity.WidgetUserAuthenticationResponse;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

@Endpoint
public class PayloadRootAnnotationMarshallingEndpoint extends GenericEndpoint{
    private ObjectFactory objectFactory;
    
	public PayloadRootAnnotationMarshallingEndpoint(ObjectFactory objectFactory){
		this.objectFactory = objectFactory;
	}
	
	/**
	 * @author Andreea
	 * Endpoint method for getting user authentication details
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "WidgetUserAuthenticationRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public WidgetUserAuthenticationResponse widgetUserAuthenticationRequest(WidgetUserAuthenticationRequest requestElement) throws IOException, EndpointException {
		logger.debug("widgetUserAuthenticationRequest START");

		WidgetUserAuthenticationResponse response = new WidgetUserAuthenticationResponse();
		try {
			UserAuth userAuth = BLPerson.getInstance().getUserAuthByUsername(requestElement.getUsername());
			if (userAuth.getPassword().equals(requestElement.getPassword())) {
				response.setAuthenticated(true);
				response.setPersonId(userAuth.getPersonId());
				response.setFirstName(userAuth.getFirstName());
				response.setLastName(userAuth.getLastName());
			}	
			else
				response.setAuthenticated(false);
			
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERAUTH_BY_USERNAME, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERAUTH_BY_USERNAME, e);
		}
		
		logger.debug("widgetUserAuthenticationRequest END");
		return response;
	}
	
	//-------------------------------WEB SERVICE TEST START---------------------------------------------------------
	
	@PayloadRoot(localPart = "GetOrganisationSimpleRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetOrganisationSimpleResponse getOrganisationSimple(GetOrganisationSimpleRequest requestElement) throws IOException, EndpointException {
		logger.debug("getOrganisationSimple START");
		StopWatch sw = new StopWatch();
		sw.start("getOrganisationSimple");
		/**
		OrganisationSimple organisationSimple = null;
		try {
			Integer organisationId = requestElement.getOrganisationId();
			Organisation organisation = BLOrganisation.getInstance().get(organisationId);
			organisationSimple = new OrganisationSimple();
			if (organisation != null){
				organisationSimple.setName(organisation.getName());
				organisationSimple.setOrganisationId(organisation.getOrganisationId());
			} else {
				organisationSimple.setName("Organization not found!!");
				organisationSimple.setOrganisationId(1);
			}
			
			//Adding the attachment to the current thread local value
			//List attachments =  new ArrayList();
			//FileDataSource fileDataSource = new FileDataSource("C:\\Users\\Koni\\Desktop\\arhiva.rar");
			//DataHandler dataHandler = new DataHandler(fileDataSource);
			//attachments.add(dataHandler);
			//WSServerThreadLocal.set(attachments);

		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_ORG_GET, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_ORG_GET, e);
		}
		*/
		GetOrganisationSimpleResponse getOrganisationSimpleResponse = new GetOrganisationSimpleResponse();
		List<OrganisationSimple> response = new ArrayList<OrganisationSimple>();
		for(int i=0; i<5000; i++){
			OrganisationSimple org = new OrganisationSimple();
			org.setName("TEST");
			org.setOrganisationId(100);
			response.add(org);
		}
		getOrganisationSimpleResponse.setOrganisation(response);
		logger.debug("getOrganisationSimple END"); 
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getOrganisationSimpleResponse;
	}
	
	//-------------------------------WEB SERVICE TEST START---------------------------------------------------------
	
	@PayloadRoot(localPart = "GetLogoRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetLogoResponse getLogo(GetLogoRequest requestElement) throws IOException, EndpointException {
		logger.debug(" getLogo START");
		Logo logo = null;
		StopWatch sw = new StopWatch();
		sw.start("getLogo");
		GetLogoResponse getLogoResponse = new GetLogoResponse(); 
		try {
			Integer organisationId = requestElement.getOrganisationId();
			logo = BLLogo.getInstance().getByOrganisationId(organisationId);
			getLogoResponse.setLogo(new WSLogo());
			
			//transform the entity : content and extension
			if(logo != null) {
				getLogoResponse.getLogo().setPicture(logo.getPicture());
				getLogoResponse.getLogo().setExtension(logo.getExtension());
			}
		} catch (BusinessException bexc) {
			logger.error("", bexc);
		 new EndpointException(ICodeException.ENDPOINT_LOGO_GET, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_LOGO_GET, e);
		}
		
		logger.debug("getLogo END"); 
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getLogoResponse;
	}
	/*
	@PayloadRoot(localPart = "GetDeactivatedUsersSimpleRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersSimpleResponse GetDeactivatedUsersSimple(GetDeactivatedUsersSimpleRequest requestElement) throws IOException, EndpointException {
		logger.debug("getDeactivatedUsersSimple START");
		StopWatch sw = new StopWatch();
		sw.start("getDeactivatedUsersSimple");
		GetUsersSimpleResponse usersSimpleResponse = null;
		try {
			
			List<Person> persons = null;
			//if (requestElement.getUserIds() != null) {
				 //persons = BLPerson.getInstance().getByUsername(requestElement.getUserIds(), requestElement.isNotDeleted());
			//} else 
			if (requestElement.getOrganizationId() > 0) {
				 persons = BLPerson.getInstance().getDeactivatedPersonsSimpleByOrganizationId(requestElement.getOrganizationId());
			}
			
			List<UserSimple> usersSimple = new ArrayList<UserSimple>();
			UserSimple  us = null;
			for(Person p: persons) {
				us = new UserSimple();
				us.setUserId(p.getPersonId());
				us.setUsername(p.getUsername());
				us.setFirstName(p.getFirstName());
				us.setLastName(p.getLastName());
				us.setEmail(p.getEmail());
				logger.debug("user = " + us);
				usersSimple.add(us);
			}
			
			usersSimpleResponse = new GetUsersSimpleResponse();
			usersSimpleResponse.setUsers(usersSimple);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_SIMPLE, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_SIMPLE, e);
		}
		logger.debug("getUsersSimple END");
		logger.debug(sw.prettyPrint());
		return usersSimpleResponse;
	}
	
*/
	@PayloadRoot(localPart = "GetUsersSimpleRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersSimpleResponse getUsersSimple(GetUsersSimpleRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUsersSimple START");
		StopWatch sw = new StopWatch();
		sw.start("getUsersSimple");
		GetUsersSimpleResponse usersSimpleResponse = null;
		try {
			List<Person> persons = null;
			if (requestElement.getUserIds() != null) {
				 persons = BLPerson.getInstance().getByUsername(requestElement.getUserIds(), requestElement.isNotDeleted());
			} else if (requestElement.getOrganizationId() > 0) {
				 persons = BLPerson.getInstance().getPersonsSimpleByOrganizationId(requestElement.getOrganizationId(), requestElement.isNotDeleted());
			}
			List<UserSimple> usersSimple = new ArrayList<UserSimple>();
			UserSimple  us = null;
			for(Person p: persons) {
				us = new UserSimple();
				us.setUserId(p.getPersonId());
				us.setUsername(p.getUsername());
				us.setFirstName(p.getFirstName());
				us.setLastName(p.getLastName());
				us.setEmail(p.getEmail());
				logger.debug("user = " + us);
				usersSimple.add(us);
			}
			
			usersSimpleResponse = new GetUsersSimpleResponse();
			usersSimpleResponse.setUsers(usersSimple);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_SIMPLE, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_SIMPLE, e);
		}
		logger.debug("getUsersSimple END");
		logger.debug(sw.prettyPrint());
		return usersSimpleResponse;
	}
	
	@PayloadRoot(localPart = "GetUserAuthRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUserAuthResponse getUserAuthByUsername(GetUserAuthRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUserAuthByUsername START");
		StopWatch sw = new StopWatch();
		sw.start("getUserAuthByUsername");
		GetUserAuthResponse userAuthResponse = null;
		try {
			UserAuth userAuth = BLPerson.getInstance().getUserAuthByUsername(requestElement.getUsername());
			userAuthResponse = new GetUserAuthResponse();
			userAuthResponse.setUserAuth(userAuth);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERAUTH_BY_USERNAME, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERAUTH_BY_USERNAME, e);
		}
		
		logger.debug("getUserAuthByUsername END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return userAuthResponse;
	}
	
	
	@PayloadRoot(localPart = "GetUserAuthBySecurityTokenRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUserAuthBySecurityTokenResponse getUserAuthBySecurityTokenRequest(GetUserAuthBySecurityTokenRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUserAuthBySecurityTokenRequest START");
		StopWatch sw = new StopWatch();
		sw.start("getUserAuthBySecurityTokenRequest");
		GetUserAuthBySecurityTokenResponse getUserAuthBySecurityTokenResponse = new GetUserAuthBySecurityTokenResponse();
		try {
			String securityToken = requestElement.getSecurityToken();
			String username = SecurityTokenManager.getInstance().getUsernameForToken(securityToken);
			SecurityTokenManager.getInstance().invalidateToken(securityToken);
			
			
			UserAuth userAuth = BLPerson.getInstance().getUserAuthByUsername(username);
			userAuth.setAdminIT(BLAuthorization.getInstance().isUserAdminIT(userAuth.getPersonId()));
			
			BLAuthorization.getInstance().authorize(userAuth, requestElement.getModule());
			logger.debug("UserAuth: " + userAuth);
			//---------------- transfering data from Business bean to WS bean ------
			WSUserAuth wsUserAuth = new WSUserAuth();
			
			wsUserAuth.setPersonId(userAuth.getPersonId());
			wsUserAuth.setUsername(userAuth.getUsername());
			wsUserAuth.setAccountNonExpired(userAuth.isAccountNonExpired());
			wsUserAuth.setAccountNonLocked(userAuth.isAccountNonLocked());
			wsUserAuth.setCredentialsNonExpired(userAuth.isCredentialsNonExpired());
			wsUserAuth.setEnabled(userAuth.isEnabled());
			wsUserAuth.setAdminIT(userAuth.isAdminIT());
			wsUserAuth.setPersonId(userAuth.getPersonId());
			wsUserAuth.setJobDepartmentPairs(userAuth.getJobDepartmentPairs());
			
			//creating the usergorups
			wsUserAuth.setUserGroups(new ArrayList<String>());
			for(String userGroup: userAuth.getUserGroups()){
				wsUserAuth.getUserGroups().add(userGroup.toString());
			}
			
			//----------------Authorities
			if (userAuth.getAuthorities() != null) {
				WSGrantedAuthorityImplArray authoritiesArray = new WSGrantedAuthorityImplArray();
				List<WSGrantedAuthorityImpl> authorities = new ArrayList<WSGrantedAuthorityImpl>();
				for(int i = 0; i < userAuth.getArrayOfAuthorities().length; i++) {
					WSGrantedAuthorityImpl authorityImpl = new WSGrantedAuthorityImpl();
					authorityImpl.setAuthority(userAuth.getArrayOfAuthorities()[i].getAuthority());
					authorities.add(authorityImpl);
				}
				authoritiesArray.setAuthorities(authorities);
				wsUserAuth.setAuthoritiesArray(authoritiesArray);
			}
			
			//----------------Business properties
			wsUserAuth.setFirstName(userAuth.getFirstName());
			wsUserAuth.setLastName(userAuth.getLastName());
			wsUserAuth.setSex("M");
			//----------------Setting birthdate
			GregorianCalendar gregCal = new GregorianCalendar();
			gregCal.setTime(userAuth.getBirthDate());
			XMLGregorianCalendarImpl birthDate = new XMLGregorianCalendarImpl(gregCal);
			wsUserAuth.setBirthDate(birthDate);
		
			wsUserAuth.setAddress(userAuth.getAddress());
			wsUserAuth.setPhone(userAuth.getPhone());
			wsUserAuth.setEmail(userAuth.getEmail());
			wsUserAuth.setObservation(userAuth.getObservation());
			wsUserAuth.setThemeCode(userAuth.getThemeCode());
			wsUserAuth.setOrganisationId(userAuth.getOrganisationId());
			wsUserAuth.setOrganisationName(userAuth.getOrganisationName());
			wsUserAuth.setOrganisationAddress(userAuth.getOrganisationAddress());
			
			//set the user's organisation modules ids
			wsUserAuth.setOrganisationModulesIds(BLModule.getInstance().listModulesIdsByOrganisation(userAuth.getOrganisationId()));
			
			logger.debug("------------------------------------------------");
			logger.debug(wsUserAuth);
			logger.debug("------------------------------------------------");
			
			getUserAuthBySecurityTokenResponse.setUserAuth(wsUserAuth);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERAUTH_BY_SECURITY_TOKEN, e);
		}
		logger.debug("getUserAuthBySecurityTokenRequest END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getUserAuthBySecurityTokenResponse;
	}
	

	@PayloadRoot(localPart = "GetUsersAndGroupsByOrganizationRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersAndGroupsByOrganizationResponse getUsersAndGroupsByOrganization(GetUsersAndGroupsByOrganizationRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUsersAndGroupsByOrganization START");
		StopWatch sw = new StopWatch();
		sw.start("getUsersAndGroupsByOrganization");
		GetUsersAndGroupsByOrganizationResponse getUsersAndGroupsByOrganizationResponse = new GetUsersAndGroupsByOrganizationResponse();
		try {
			int organizationId = requestElement.getOrganizationId();
			int moduleId = requestElement.getModuleId();
			logger.debug("organizationId = " + organizationId);
			//If I have an OrganizationId
			if (organizationId > 0) {
				
				List<WSUserOrGroup> users = new ArrayList<WSUserOrGroup>();
				List<WSUserOrGroup> groups = new ArrayList<WSUserOrGroup>();
				
				//Populate with Users ?
				if (requestElement.isWithUsers()) {
					List<Integer> persons = null;
					List<Person> allPersons = null;
					Entry entry = null;
					if(moduleId > 0){
						//Get all persons from this Organization
						entry = BLPerson.getInstance().getPersonsSimpleByOrganizationIdAndModule(organizationId, moduleId);
					}
					
					persons = (List<Integer>)entry.getKey();
					allPersons = (List<Person>)entry.getValue();
					
					// If it's all ok add them to the list to be marshalled
					if (allPersons != null) {
						WSUserOrGroup user = null;
						Person p = null;
						for(int i =0; i < allPersons.size(); i++) {
							user = new WSUserOrGroup();
							p = allPersons.get(i);
							user.setId(p.getPersonId());
							user.setName(p.getUsername());
							user.setFirstName(p.getFirstName());
							user.setLastName(p.getLastName());
							user.setEmail(p.getEmail());
							user.setGroup(false);
							user.setEnabled((p.getEnabled() == IConstant.NOM_PERSON_ACTIVE));
							user.setHasModule(persons.contains(p.getPersonId()));
							// add it to the list
							users.add(user);
						}
					}
				}
				//------------------------------------------
				//Populate with Groups ?
				if (requestElement.isWithGroups()) {
					//Get all UserGroups from this Organization
					List<UserGroup> userGroups = BLUserGroup.getInstance().getAllOrganizationUserGroups(organizationId);
					// If it's all ok add them to the list to be marshalled
					if (userGroups != null) {
						UserGroup userGroup = null;
						WSUserOrGroup group = null;
						for(int i =0; i < userGroups.size(); i++) {
							userGroup = userGroups.get(i);
							group = new WSUserOrGroup();
							group.setId(userGroup.getUserGroupId());
							group.setName(userGroup.getName());
							
							group.setFirstName("");
							group.setLastName("");
							group.setEmail("");
							group.setGroup(true);
							//UserGroups cannot be deactivated
							group.setEnabled(true);
							
							// add it to the list
							groups.add(group);
						}
					}
					
				}
				//------------------------------------------
			List<WSUserOrGroup> usersAndGroups = new ArrayList<WSUserOrGroup>();
			usersAndGroups.addAll(users);
			usersAndGroups.addAll(groups);
			logger.debug("Sending " + usersAndGroups.size() + " entities !");
			getUsersAndGroupsByOrganizationResponse.setUsersAndGroups(usersAndGroups);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_AND_GROUPS_BY_ORGANIZATION_ID, e);
		}
		logger.debug("getUsersAndGroupsByOrganization END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getUsersAndGroupsByOrganizationResponse;
	}
	
	@PayloadRoot(localPart = "GetDeletedUsersAndGroupsByOrganizationRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersAndGroupsByOrganizationResponse getDeletedUsersAndGroupsByOrganization(GetDeletedUsersAndGroupsByOrganizationRequest requestElement) throws IOException, EndpointException {
		logger.debug("getDeletedUsersAndGroupsByOrganization START");
		StopWatch sw = new StopWatch();
		sw.start("getDeletedUsersAndGroupsByOrganization");
		GetUsersAndGroupsByOrganizationResponse getUsersAndGroupsByOrganizationResponse = new GetUsersAndGroupsByOrganizationResponse();
		try {
			int organizationId = requestElement.getOrganizationId();
			int moduleId = requestElement.getModuleId();
			logger.debug("organizationId = " + organizationId);
			//If I have an OrganizationId
			if (organizationId > 0) {
				
				List<WSUserOrGroup> users = new ArrayList<WSUserOrGroup>();
				List<WSUserOrGroup> groups = new ArrayList<WSUserOrGroup>();
				
				//Populate with Users ?
				if (requestElement.isWithUsers()) {
					List<Integer> persons = null;
					List<Person> allPersons = null;
					Entry entry = null;
					if(moduleId > 0){
						//Get all persons from this Organization
						entry = BLPerson.getInstance().getDeletedPersonsSimpleByOrganizationIdAndModule(organizationId, moduleId);
					}
					
					persons = (List<Integer>)entry.getKey();
					allPersons = (List<Person>)(entry.getValue());
					
					// If it's all ok add them to the list to be marshalled
					if (allPersons != null) {
						WSUserOrGroup user = null;
						Person p = null;
						for(int i =0; i < allPersons.size(); i++) {
							user = new WSUserOrGroup();
							System.out.println("xxxxxxxxxxxxxxx "+allPersons.get(i));
							p = allPersons.get(i);
							user.setId(p.getPersonId());
							user.setName(p.getUsername());
							user.setFirstName(p.getFirstName());
							user.setLastName(p.getLastName());
							user.setEmail(p.getEmail());
							user.setGroup(false);
							user.setEnabled((p.getEnabled() == IConstant.NOM_PERSON_ACTIVE));
							user.setHasModule(persons.contains(p.getPersonId()));
							// add it to the list
							users.add(user);
						}
					}
				}
				//------------------------------------------
				//Populate with Groups ?
				if (requestElement.isWithGroups()) {
					//Get all UserGroups from this Organization
					List<UserGroup> userGroups = BLUserGroup.getInstance().getAllOrganizationUserGroups(organizationId);
					// If it's all ok add them to the list to be marshalled
					if (userGroups != null) {
						UserGroup userGroup = null;
						WSUserOrGroup group = null;
						for(int i =0; i < userGroups.size(); i++) {
							userGroup = userGroups.get(i);
							group = new WSUserOrGroup();
							group.setId(userGroup.getUserGroupId());
							group.setName(userGroup.getName());
							
							group.setFirstName("");
							group.setLastName("");
							group.setEmail("");
							group.setGroup(true);
							//UserGroups cannot be deactivated
							group.setEnabled(true);
							
							// add it to the list
							groups.add(group);
						}
					}
					
				}
				//------------------------------------------
			List<WSUserOrGroup> usersAndGroups = new ArrayList<WSUserOrGroup>();
			usersAndGroups.addAll(users);
			usersAndGroups.addAll(groups);
			logger.debug("Sending " + usersAndGroups.size() + " entities !");
			getUsersAndGroupsByOrganizationResponse.setUsersAndGroups(usersAndGroups);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_AND_GROUPS_BY_ORGANIZATION_ID, e);
		}
		logger.debug("getDeletedUsersAndGroupsByOrganization END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getUsersAndGroupsByOrganizationResponse;
	}
	
	@PayloadRoot(localPart = "GetAllOrganisationsRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetAllOrganisationsResponse getAllOrganisations(GetAllOrganisationsRequest requestElement) throws IOException, EndpointException {
		logger.debug("getAllOrganisations START");
		StopWatch sw = new StopWatch();
		sw.start("getAllOrganisations");
		GetAllOrganisationsResponse getAllOrganisationsResponse = new GetAllOrganisationsResponse();
		try {
			List<Organisation> allOrganisations = BLOrganisation.getInstance().getAllOrganisationsForNom();
			List<WSOrganisation> organisations = new ArrayList<WSOrganisation>();
			for (Organisation org : allOrganisations){
				WSOrganisation wsOrganisation = new WSOrganisation();
				wsOrganisation.setOrganisationId(org.getOrganisationId());
				wsOrganisation.setOrganisationName(org.getName());
				organisations.add(wsOrganisation);
			}
			getAllOrganisationsResponse.setOrganisations(organisations);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_ALL_ORGANISATIONS, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_ALL_ORGANISATIONS, e);
		}
		logger.debug("getAllOrganisations END"); 
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getAllOrganisationsResponse;
	}
	
		
	/**
	 * Send a list with all the freedays
	 *
	 * @author mitziuro
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetFreeDaysRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetFreeDaysResponse getFreeDaysByOrganisation(GetFreeDaysRequest requestElement) throws IOException, EndpointException {
		logger.debug("getFreeDaysByOrganisation START");
		
		List<Date> freeDays = new ArrayList<Date>();
		
		StopWatch sw = new StopWatch();
		sw.start("getFreeDaysByOrganisation");
		try{
			
			List<FreeDay> freeDaysList = BLFreeday.getInstance().getByOrganisation(requestElement.getOrganisationId());
			
			//create the list
			if(freeDaysList != null){
				for(FreeDay freeDay : freeDaysList){
					freeDays.add(freeDay.getDay());
				}
			}
			
		}catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_FREEDAYS, e);
		}
		GetFreeDaysResponse response = new GetFreeDaysResponse();
		response.setFreedays(freeDays);
		
		logger.debug("getFreeDaysByOrganisation END");
		logger.debug(sw.prettyPrint());
		return response;
	}
	
	/**
	 * Send a list with all organisations Id that have a module
	 *
	 * @author mitziuro
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetOrganisationsByModuleRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetOrganisationsByModuleResponse getOrganisationsByModule(GetOrganisationsByModuleRequest requestElement) throws IOException, EndpointException {
		logger.debug("getOrganisationsByModule START");
		
		List<Integer> ids = new ArrayList<Integer>();
		
		StopWatch sw = new StopWatch();
		sw.start("getOrganisationsByModule");
		try{
			
			//we get the list
			ids = BLOrganisation.getInstance().getOrganisationIdsByModule(requestElement.getModuleId());
			
		}catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_ORGANISATIONS_BY_MODULE, e);
		}
		GetOrganisationsByModuleResponse response = new GetOrganisationsByModuleResponse();
		response.setOrganisationIds(ids);
		
		logger.debug("getOrganisationsByModule END");
		logger.debug(sw.prettyPrint());
		return response;
	}
	
	
	@PayloadRoot(localPart = "GetUsersByOrganizationRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersByOrganizationResponse getUsersByOrganization(GetUsersByOrganizationRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUsersByOrganization START");
		StopWatch sw = new StopWatch();
		sw.start("getUsersOrganization");
		GetUsersByOrganizationResponse getUsersByOrganizationResponse = new GetUsersByOrganizationResponse();
		try {
			int organizationId = requestElement.getOrganizationId();
			int moduleId = requestElement.getModuleId();
			logger.debug("organizationId = " + organizationId);
			//If I have an OrganizationId
			if (organizationId > 0) {
				
				List<WSUser> users = new ArrayList<WSUser>();
										
				//Populate with Users ?
				if (requestElement.isWithUsers()) {
					List<Person> persons = null;
					Entry entry = null;
					if(moduleId > 0){
						//Get all persons from this Organization
						entry = BLPerson.getInstance().getPersonsSimpleByOrganizationIdAndModule(organizationId, moduleId);
						persons = (List<Person>)entry.getValue();
					} else {
						//Get all persons from this Organization with the desired module
						persons = BLPerson.getInstance().getPersonsSimpleByOrganizationId(organizationId, true);
					}
					
					// If it's all ok add them to the list to be marshalled
					if (persons != null) {
						WSUser user = null;
						Person p = null;
						for(int i =0; i < persons.size(); i++) {
							user = new WSUser();
							p = persons.get(i);
							user.setId(p.getPersonId());
							user.setName(p.getUsername());
							user.setFirstName(p.getFirstName());
							user.setLastName(p.getLastName());					
							user.setEnabled((p.getEnabled() == IConstant.NOM_PERSON_ACTIVE));

							// add it to the list
							users.add(user);
						}
					}
				}				
			List<WSUser> newUsers = new ArrayList<WSUser>();
			newUsers.addAll(users);			
			logger.debug("Sending " + newUsers.size() + " entities !");
			getUsersByOrganizationResponse.setUsers(newUsers);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_AND_GROUPS_BY_ORGANIZATION_ID, e);
		}
		logger.debug("getUsersAndGroupsByOrganization END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getUsersByOrganizationResponse;
	}
	
	/**
	 * Get the list of persons that have a given role name and belongs
	 * to a certain organisation.
	 * 
	 * @author Adelina
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetUsersWithRoleByOrganisationRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersWithRoleByOrganisationResponse getUsersWithRoleByOrganisation(GetUsersWithRoleByOrganisationRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUsersWithRoleByOrganisation - START");
		StopWatch sw = new StopWatch();
		sw.start("getUsersWithRoleByOrgansiation");
		GetUsersWithRoleByOrganisationResponse getUsersWithRoleByOrganisationResponse = new GetUsersWithRoleByOrganisationResponse();
		
		try{
			Integer organizationId = requestElement.getOrganizationId();
			String roleName = requestElement.getRoleName();
			logger.debug("organizationId = " + organizationId);
			logger.debug("roleName = " + roleName);
			
			// if we have an organisationId
			if(organizationId > 0) {
				List<WSUser> users = new ArrayList<WSUser>();
				
				// then populate the users
				if(requestElement.isWithUsers()) {
					List<Person> persons = BLPerson.getInstance().getPersonsWithRoleByOrganisation(organizationId, roleName);
					logger.debug("persons = " + persons);
					
					// if it's all ok add them to the list to be marshalled
					if(persons != null && persons.size() > 0) {
						WSUser user = null;
						Person p = null;
						for(int i =0; i < persons.size(); i++) {
							user = new WSUser();
							p = persons.get(i);
							user.setId(p.getPersonId());
							user.setName(p.getUsername());
							user.setFirstName(p.getFirstName());
							user.setLastName(p.getLastName());					
							user.setEnabled((p.getEnabled() == IConstant.NOM_PERSON_ACTIVE));

							// add it to the list
							users.add(user);
						}
					}
				}
				
				List<WSUser> newUsers = new ArrayList<WSUser>();
				newUsers.addAll(users);			
				logger.debug("Sending " + newUsers.size() + " entities !");
				getUsersWithRoleByOrganisationResponse.setUsers(newUsers);				
			}
							
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_BY_ROLE_NAME_AND_ORGANISATION, e);
		}
		
		logger.debug("getUsersWithRoleByOrganisation - END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getUsersWithRoleByOrganisationResponse;
	}
	
	
	/**
	 * Verifies if an organisation has the audit module 
	 *
	 * @author coni
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "OrganisationHasAuditModuleRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public OrganisationHasAuditModuleResponse organisationHasAuditModule(OrganisationHasAuditModuleRequest requestElement) throws IOException, EndpointException {
		logger.debug("organisationHasAuditModule START");
		
		Boolean hasAuditModule;
		try{
			hasAuditModule = BLOrganisation.getInstance().hasAuditModule(requestElement.getOrganisationId());
		}catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_ORGANISATION_HAS_AUDIT_MODULE, e);
		}
		OrganisationHasAuditModuleResponse response = new OrganisationHasAuditModuleResponse();
		response.setHasAuditModule(hasAuditModule);

		logger.debug("organisationHasAuditModule END");
		return response;
	}
	
	/**
	 * Get person identified by the person's id
	 * 
	 * @author Adelina
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetPersonSimpleRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetPersonSimpleResponse getPersonSimple(GetPersonSimpleRequest requestElement) throws IOException, EndpointException {
		logger.debug("getPersonSimple - START");
		StopWatch sw = new StopWatch();
		sw.start("getPersonSimple");
		
		GetPersonSimpleResponse getPersonSimpleResponse = new GetPersonSimpleResponse();
		
		try{
			Integer personId = requestElement.getPersonId();
			logger.debug("personId = " + personId);
			// if we have personId
			if(personId > 0) {
				WSUser user = new WSUser();
				Person person = BLPerson.getInstance().get(personId);
				logger.debug("person = " + person);
				
				user.setId(person.getPersonId());
				user.setName(person.getUsername());
				user.setFirstName(person.getFirstName());
				user.setLastName(person.getLastName());					
				user.setEnabled((person.getEnabled() == IConstant.NOM_PERSON_ACTIVE));
				user.setStatus(person.getStatus());
				user.setEmail(person.getEmail());
				getPersonSimpleResponse.setPerson(user);
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PERSON_BY_PERSONID, e);
		}
		
		logger.debug("getPersonSimple - END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		
		return getPersonSimpleResponse;
		
	}
	
	/**
	 * Returns the user group with the specified id
	 *
	 * @author coni
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetUserGroupByIdRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUserGroupByIdResponse getUserGroupById(GetUserGroupByIdRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUserGroupSimpleById START");
		GetUserGroupByIdResponse response = new GetUserGroupByIdResponse();
		try{
			WSUserOrGroup wsUserGroup = new WSUserOrGroup();
			UserGroup userGroup = BLUserGroup.getInstance().get(new Integer(requestElement.getUserGroupId()));
			wsUserGroup.setId(userGroup.getUserGroupId());
			wsUserGroup.setName(userGroup.getName());
			wsUserGroup.setGroup(true);
			response.setUserGroup(wsUserGroup);
		}catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USER_GROUP_BY_ID, e);
		}
		logger.debug("getUserGroupSimpleById END");
		return response;
	}
	
	/**
	 * Returns all the user groups that contain the user with the specified id
	 *
	 * @author coni
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetUserGroupsByUserRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUserGroupsByUserResponse getUserGroupsByUser(GetUserGroupsByUserRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUserGroupsByUser - START");
		GetUserGroupsByUserResponse response = new GetUserGroupsByUserResponse();
		try{
			ArrayList<UserGroup> userGroups = (ArrayList<UserGroup>) BLUserGroup.getInstance().getUserGroupsByUser(new Integer(requestElement.getUserId()));
			List<WSUserOrGroup> wSUserOrGroups = new ArrayList<WSUserOrGroup>();
			for (UserGroup userGroup: userGroups) {
				WSUserOrGroup wsUserGroup = new WSUserOrGroup();
				wsUserGroup.setId(userGroup.getUserGroupId());
				wsUserGroup.setName(userGroup.getName());
				wsUserGroup.setGroup(true);
				wSUserOrGroups.add(wsUserGroup);
			}
			response.setUserGroups(wSUserOrGroups);
		}catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USER_GROUPS_BY_USER, e);
		}
		logger.debug("getUserGroupsByUser -  END");
		return response;
	}
	
	/**
	 * Returns all the users from the group with the specified id
	 *
	 * @author coni
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetUsersSimpleByGroupRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersSimpleByGroupResponse getUsersSimpleByGroup(GetUsersSimpleByGroupRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUsersSimpleByGroup - START");
		GetUsersSimpleByGroupResponse response = new GetUsersSimpleByGroupResponse();
		try{
			ArrayList<Person> persons = (ArrayList<Person>) BLPerson.getInstance().getByUserGroupId(new Integer(requestElement.getUserGroupId()));
			List<UserSimple> usersSimple = new ArrayList<UserSimple>();
			for (Person person: persons) {
				UserSimple userSimple = new UserSimple();
				userSimple.setUserId(person.getPersonId());
				userSimple.setFirstName(person.getFirstName());
				userSimple.setLastName(person.getLastName());
				userSimple.setEmail(person.getEmail());
				usersSimple.add(userSimple);
			}
			response.setUsers(usersSimple);
		}catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_BY_USER_GROUP, e);
		}
		logger.debug("getUsersSimpleByGroup -  END");
		return response;
	}
	
	@PayloadRoot(localPart = "GetUsersSimpleByPersonIdRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetUsersSimpleByPersonIdResponse getUsersSimpleByPersonId(GetUsersSimpleByPersonIdRequest requestElement) throws IOException, EndpointException {
		logger.debug("getUsersSimpleByPersonId START");
		StopWatch sw = new StopWatch();
		sw.start("getUsersSimpleByPersonId");
		GetUsersSimpleByPersonIdResponse usersSimpleResponse = null;
		List<Person> persons = null;
		
		//if the property findAllUsers is true, we retrieve all the users from the database,
		//otherwise only the users with the specified personIds
		try {
			if (requestElement.isFindAllUsers()) {
				persons = BLPerson.getInstance().list();
			} else {
				Integer[] personIds = new Integer[requestElement.getPersonIds().length];
				for (int i=0; i<requestElement.getPersonIds().length; i++){
					personIds[i] = Integer.parseInt(requestElement.getPersonIds()[i]);
				}
				persons = BLPerson.getInstance().getByPersonId(personIds);
			}
			
			List<UserSimple> usersSimple = new ArrayList<UserSimple>();
			UserSimple  us = null;
			for(Person p: persons) {
				us = new UserSimple();
				us.setUserId(p.getPersonId());
				us.setUsername(p.getUsername());
				us.setFirstName(p.getFirstName());
				us.setLastName(p.getLastName());
				us.setEmail(p.getEmail());
				usersSimple.add(us);
			}
			
			usersSimpleResponse = new GetUsersSimpleByPersonIdResponse();
			usersSimpleResponse.setUsers(usersSimple);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_SIMPLE, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_USERS_SIMPLE, e);
		}
		logger.debug("getUsersSimpleByPersonId END");
		logger.debug(sw.prettyPrint());
		return usersSimpleResponse;
	}
	
	@PayloadRoot(localPart = "GetPersonFromSearchRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetPersonFromSearchResponse getPersonFromSearch(GetPersonFromSearchRequest requestElement) throws IOException, EndpointException {
		logger.debug("getPersonFromSearch START");
		StopWatch sw = new StopWatch();
		sw.start("getPersonFromSearch");
		GetPersonFromSearchResponse response = new GetPersonFromSearchResponse();
		List<Person> persons = null;
		
		try {
			WSSearchPersonBean wsSearchPersonBean = requestElement.getWsSearchPersonBean();
			SearchPersonBean spb = new SearchPersonBean();
			spb.setCurrentPage(wsSearchPersonBean.getCurrentPage());
			spb.setFirstName(wsSearchPersonBean.getFirstName());
			spb.setLastName(wsSearchPersonBean.getLastName());
			spb.setLowerLimit(wsSearchPersonBean.getLowerLimit());
			spb.setNbrOfPages(wsSearchPersonBean.getNbrOfPages());
			spb.setNbrOfResults(wsSearchPersonBean.getNbrOfResults());
			spb.setOrganisationId(wsSearchPersonBean.getOrganisationId());
			spb.setResultsPerPage(wsSearchPersonBean.getResultsPerPage());
			spb.setSortDirection(wsSearchPersonBean.getSortDirection());
			spb.setSortParam(wsSearchPersonBean.getSortParam());
			spb.setUpperLimit(wsSearchPersonBean.getUpperLimit());
			spb.setDepartmentId(wsSearchPersonBean.getDepartmentId());
			spb.setSex(wsSearchPersonBean.getSex());
			spb.setUsername(wsSearchPersonBean.getUsername());
			Boolean withDeleted = wsSearchPersonBean.isWithDeleted();
			
			//if the the attribute resultsPerPage > 0, it means that request needs pagination; otherwise
			//all the results will be retrieved
			if (spb.getResultsPerPage() > 0) {
				persons = BLPerson.getInstance().getFromSearchSimpleWithPagination(spb, withDeleted);
				//set the new pagination attributes resulted from search for the returned WSSearchPersonBean
				wsSearchPersonBean.setCurrentPage(spb.getCurrentPage());
				wsSearchPersonBean.setLowerLimit(spb.getLowerLimit());
				wsSearchPersonBean.setNbrOfPages(spb.getNbrOfPages());
				wsSearchPersonBean.setNbrOfResults(spb.getNbrOfResults());
				wsSearchPersonBean.setResultsPerPage(spb.getResultsPerPage());
				wsSearchPersonBean.setUpperLimit(spb.getUpperLimit());
			} else {
				persons = BLPerson.getInstance().getFromSearchSimple(spb, withDeleted);
			}			
			
			List<UserSimple> usersSimple = new ArrayList<UserSimple>();
			UserSimple  us = null;
			for(Person p: persons) {
				us = new UserSimple();
				us.setUserId(p.getPersonId());
				us.setFirstName(p.getFirstName());
				us.setLastName(p.getLastName());
				us.setEmail(p.getEmail());
				us.setUsername(p.getUsername());
				usersSimple.add(us);
			}
			response.setPersons(usersSimple);
			response.setWsSearchPersonBean(wsSearchPersonBean);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PERSON_FROM_SEARCH, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PERSON_FROM_SEARCH, e);
		}
		logger.debug("getPersonFromSearch END");
		logger.debug(sw.prettyPrint());
		return response;
	}
	
	/**
	 * Gets the calendar for an organization
	 * 
	 * @author Adelina 
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetCalendarByOrganizationRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetCalendarByOrganizationResponse getCalendarByOrganization(GetCalendarByOrganizationRequest requestElement)  throws IOException, EndpointException {
		logger.debug("getCalendarByOrganization START");
		StopWatch sw = new StopWatch();
		sw.start("getCalendarByOrganization");
		
		GetCalendarByOrganizationResponse response = new GetCalendarByOrganizationResponse();
		
		try{			
			Calendar calendar = BLCalendar.getInstance().getCalendarForOrganisation(requestElement.getOrganizationId());
			WSCalendar wsCalendar = new WSCalendar();
			wsCalendar.setCalendarId(calendar.getCalendarId());
			wsCalendar.setOrganisationId(calendar.getOrganisationId());
			wsCalendar.setStartWork(calendar.getStartWork());
			wsCalendar.setEndWork(calendar.getEndWork());
			response.setCalendar(wsCalendar);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_CALENDAR_BY_ORGANIZATION, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_CALENDAR_BY_ORGANIZATION, e);
		}
		
		
		logger.debug("getCalendarByOrganization END");
		logger.debug(sw.prettyPrint());
		return response;
	}
	
	/**
	 * Gets the persons by role
	 * 
	 * @author Adelina 
	 * 
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetPersonsByRoleRequest", namespace = "http://localhost:8080/OM/services/schemas/messages")
	public GetPersonsByRoleResponse getPersonsByRole(GetPersonsByRoleRequest requestElement)  throws IOException, EndpointException {
		logger.debug("getPersonsByRole START");
		StopWatch sw = new StopWatch();
		sw.start("getPersonsByRole");
		
		GetPersonsByRoleResponse response = new GetPersonsByRoleResponse();
		
		try{			
			Set<Person> persons = BLRole.getInstance().getPersonsByRole(requestElement.getPermissionString(), requestElement.getOrganizationId());
			Set<UserSimple> users = new HashSet<UserSimple>();
			if(persons != null && persons.size() > 0) {
				for(Person person : persons) {
					UserSimple user = new UserSimple();
					user.setUserId(person.getPersonId());
					users.add(user);
				}
			}
			response.setPersons(users);
					
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PERSONS_BY_ROLE, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PERSONS_BY_ROLE, e);
		}
		
		
		logger.debug("getPersonsByRole END");
		logger.debug(sw.prettyPrint());
		return response;
	}
}
