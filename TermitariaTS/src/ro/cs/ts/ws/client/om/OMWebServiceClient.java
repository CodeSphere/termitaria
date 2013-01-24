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
package ro.cs.ts.ws.client.om;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.StopWatch;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;

import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.ws.client.om.entity.GetLogoRequest;
import ro.cs.ts.ws.client.om.entity.GetLogoResponse;
import ro.cs.ts.ws.client.om.entity.WSLogo;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.SearchPersonBean;
import ro.cs.ts.exception.WSClientException;
import ro.cs.ts.ws.client.om.entity.GetCalendarByOrganizationRequest;
import ro.cs.ts.ws.client.om.entity.GetCalendarByOrganizationResponse;
import ro.cs.ts.ws.client.om.entity.GetPersonFromSearchRequest;
import ro.cs.ts.ws.client.om.entity.GetPersonFromSearchResponse;
import ro.cs.ts.ws.client.om.entity.GetPersonsByRoleRequest;
import ro.cs.ts.ws.client.om.entity.GetPersonsByRoleResponse;
import ro.cs.ts.ws.client.om.entity.GetUserAuthBySecurityTokenRequest;
import ro.cs.ts.ws.client.om.entity.GetUserAuthBySecurityTokenResponse;
import ro.cs.ts.ws.client.om.entity.GetUsersSimpleByPersonIdRequest;
import ro.cs.ts.ws.client.om.entity.GetUsersSimpleByPersonIdResponse;
import ro.cs.ts.ws.client.om.entity.GetUsersSimpleRequest;
import ro.cs.ts.ws.client.om.entity.GetUsersSimpleResponse;
import ro.cs.ts.ws.client.om.entity.OMEndpointExceptionBean;
import ro.cs.ts.ws.client.om.entity.ObjectFactory;
import ro.cs.ts.ws.client.om.entity.OrganisationHasAuditModuleRequest;
import ro.cs.ts.ws.client.om.entity.OrganisationHasAuditModuleResponse;
import ro.cs.ts.ws.client.om.entity.UserSimple;
import ro.cs.ts.ws.client.om.entity.WSCalendar;
import ro.cs.ts.ws.client.om.entity.WSSearchPersonBean;


/**
 * 
 * Singleton which exposes methods for the OM Web Service Client
 * @author coni
 * @author Adelina
 *
 */
public class OMWebServiceClient extends WebServiceGatewaySupport {

	private static final String omWebServiceClientBean = "omWebServiceClient";
	
	private ObjectFactory objectFactory; 
	protected final Log logger = LogFactory.getLog(getClass());
	
	//singleton implementation
	private OMWebServiceClient(){}
	public static OMWebServiceClient getInstance(){
		return (OMWebServiceClient) TSContext.getApplicationContext().getBean(omWebServiceClientBean);
	}
	
	//setters and getters
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}	
	
	public WSLogo getLogo(int organizationId) throws BusinessException, XmlMappingException, IOException, ro.cs.ts.exception.WSClientException{
		logger.debug("getLogo START");
		StopWatch sw = new StopWatch();
		sw.start("getLogo");
		
		WSLogo logo = null;
		try {
			//create the bean  marshalled into the request
			GetLogoRequest getLogoRequest = new GetLogoRequest();
			getLogoRequest.setOrganisationId(organizationId);
			//unmarshall the response to an OrganisationSimple bean
			GetLogoResponse jaxbLogo = (GetLogoResponse) getWebServiceTemplate().marshalSendAndReceive(getLogoRequest);
			logger.debug("-------------------------------------------------------------------------------------------------");
			logger.debug(jaxbLogo);
			logger.debug("-------------------------------------------------------------------------------------------------");
			logo = jaxbLogo.getLogo();
		} catch (SoapFaultClientException soapFault) {
			
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<OMEndpointExceptionBean> omEndpointExceptionBean = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(omEndpointExceptionBean.getValue().getCode(),omEndpointExceptionBean.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("getLogo END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return logo;
	}
	public GetUserAuthBySecurityTokenResponse getUserAuthBySecurityToken(String securityToken) throws XmlMappingException, IOException, WSClientException{
		logger.debug("getUserAuthBySecurityToken START");
		GetUserAuthBySecurityTokenResponse getUserAuthBySecurityTokenResponse = null;
		try {
			GetUserAuthBySecurityTokenRequest getUserBySecurityTokenRequest = new GetUserAuthBySecurityTokenRequest();
						
			getUserBySecurityTokenRequest.setSecurityToken(securityToken);
			getUserBySecurityTokenRequest.setModule(IConstant.MODULE_ID);
			//unmarshall the response to an OrganisationSimple bean
			getUserAuthBySecurityTokenResponse = (GetUserAuthBySecurityTokenResponse) getWebServiceTemplate().
					marshalSendAndReceive(getUserBySecurityTokenRequest);
			logger.debug(getUserAuthBySecurityTokenResponse.getUserAuth());
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<OMEndpointExceptionBean> endpointException = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("getUserAuthBySecurityToken END");
		return getUserAuthBySecurityTokenResponse;
	}
	
	public List<UserSimple> getUsersSimpleByOrganizationId(int organizationId, boolean isNotDeleted) throws XmlMappingException, IOException, WSClientException{
		logger.debug("getUsersSimpleByOrganizationId START");
		List<UserSimple> users = null;
		try {
			//create the bean  marshalled into the request
			GetUsersSimpleRequest getUsersSimpleRequest = new GetUsersSimpleRequest();
			getUsersSimpleRequest.setOrganizationId(organizationId);
			getUsersSimpleRequest.setNotDeleted(isNotDeleted);
			//unmarshall the response to an OrganisationSimple bean
			users = ((GetUsersSimpleResponse) getWebServiceTemplate().marshalSendAndReceive(getUsersSimpleRequest)).getUsers();
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<OMEndpointExceptionBean> endpointException = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("getUsersSimpleByOrganizationId END");
		return users;
	}
	
	public GetPersonFromSearchResponse getPersonFromSearch(SearchPersonBean searchPersonBean) throws XmlMappingException, IOException, WSClientException{
		logger.debug("getPersonFromSearch START");
		GetPersonFromSearchResponse response = null;
		try {
			//create the bean  marshalled into the request
			GetPersonFromSearchRequest getPersonFromSearchRequest = new GetPersonFromSearchRequest();
			WSSearchPersonBean wsSearchPersonBean = new WSSearchPersonBean();
			wsSearchPersonBean.setCurrentPage(searchPersonBean.getCurrentPage());
			wsSearchPersonBean.setFirstName(searchPersonBean.getFirstName());
			wsSearchPersonBean.setLastName(searchPersonBean.getLastName());
			wsSearchPersonBean.setLowerLimit(searchPersonBean.getLowerLimit());
			wsSearchPersonBean.setNbrOfPages(searchPersonBean.getNbrOfPages());
			wsSearchPersonBean.setNbrOfResults(searchPersonBean.getNbrOfResults());
			wsSearchPersonBean.setOrganisationId(searchPersonBean.getOrganizationId());
			wsSearchPersonBean.setResultsPerPage(searchPersonBean.getResultsPerPage());
			wsSearchPersonBean.setSortDirection(searchPersonBean.getSortDirection());
			wsSearchPersonBean.setSortParam(searchPersonBean.getSortParam());
			wsSearchPersonBean.setUpperLimit(searchPersonBean.getUpperLimit());
			wsSearchPersonBean.setWithDeleted(searchPersonBean.isWithDeleted());
			getPersonFromSearchRequest.setWsSearchPersonBean(wsSearchPersonBean);
			//unmarshall the response to a list of persons
			response = (GetPersonFromSearchResponse) getWebServiceTemplate().marshalSendAndReceive(getPersonFromSearchRequest);
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<OMEndpointExceptionBean> endpointException = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("getPersonFromSearch END");
		return response;
	}
	
	public GetUsersSimpleByPersonIdResponse getUsersSimpleByPersonId(String[] personIds) throws XmlMappingException, IOException, WSClientException{
		logger.debug("START - getUsersSimpleByPersonId");
		GetUsersSimpleByPersonIdResponse usersSimpleResponse = null;
		try {
			//create the bean  marshalled into the request
			GetUsersSimpleByPersonIdRequest getUsersSimpleRequest = new GetUsersSimpleByPersonIdRequest();
			//if the array containing the person ids is null, it means all the users must be retrieved; in order to do
			//that, we set the findAllUsers property to true
			if (personIds != null) {
				getUsersSimpleRequest.setPersonIds(personIds);
			} else {
				getUsersSimpleRequest.setFindAllUsers(true);
			}
			//unmarshall the response to an GetUsersSimpleByPersonIdResponse bean
			usersSimpleResponse = (GetUsersSimpleByPersonIdResponse) getWebServiceTemplate().
					marshalSendAndReceive(getUsersSimpleRequest);
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<OMEndpointExceptionBean> endpointException = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("END - getUsersSimpleByPersonId");
		return usersSimpleResponse;
	}
	
	/**
	 * Get the calendar from organization
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public WSCalendar getCalendarByOrganization(Integer organizationId) throws XmlMappingException, IOException, WSClientException{
		logger.debug("START - getCalendarByOrganization");
		
		WSCalendar response = null;	
		
		try {
			//create the bean  marshalled into the request
			GetCalendarByOrganizationRequest request = new GetCalendarByOrganizationRequest();			
			request.setOrganizationId(organizationId);
			//unmarshall the response to an GetCalendarByOrganizationResponse bean
			response = ((GetCalendarByOrganizationResponse) getWebServiceTemplate().marshalSendAndReceive(request)).getCalendar();				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<OMEndpointExceptionBean> endpointException = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("END - getCalendarByOrganization");
		return response;
	}
	
	/**
	 * Checks if an organization has the audit module
	 *
	 * @author coni
	 * 
	 * @param moduleId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public Boolean organisationHasAuditModule(int organisationId) throws XmlMappingException, IOException, WSClientException{
		logger.debug("organisationHasAuditModule START");
		Boolean hasAudit;
		
		try {
			//create the bean  marshalled into the request
			OrganisationHasAuditModuleRequest request = new OrganisationHasAuditModuleRequest();
			request.setOrganisationId(organisationId);
			
			//unmarshall the response to an OrganisationSimple bean
			OrganisationHasAuditModuleResponse response = (OrganisationHasAuditModuleResponse) getWebServiceTemplate().
					marshalSendAndReceive(request);
			
			hasAudit = response.getHasAuditModule();
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named endpointException
				JAXBElement<OMEndpointExceptionBean> endpointException = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the OMEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("organisationHasAuditModule END");
		return hasAudit;
	}
	
		
	/**
	 * Get the persons from role
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param permissionName
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public Set<UserSimple> getPersonsFromRole(String permissionName, Integer organizationId) throws XmlMappingException, IOException, WSClientException{
		logger.debug("START - getPersonsFromRole");
		
		Set<UserSimple> response = null;	
		
		try {
			//create the bean  marshalled into the request
			GetPersonsByRoleRequest request = new GetPersonsByRoleRequest();
			logger.debug("organizationId  = " + organizationId);
			logger.debug("permissionName  = " + permissionName);
			request.setOrganizationId(organizationId);
			request.setPermissionString(permissionName);			
			//unmarshall the response to an GetCalendarByOrganizationResponse bean
			response = ((GetPersonsByRoleResponse) getWebServiceTemplate().marshalSendAndReceive(request)).getPersons();			
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<OMEndpointExceptionBean> endpointException = (JAXBElement<OMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("END - getPersonsFromRole");
		return response;
	}
}
