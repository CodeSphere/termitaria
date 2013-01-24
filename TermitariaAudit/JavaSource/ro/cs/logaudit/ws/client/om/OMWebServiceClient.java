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
package ro.cs.logaudit.ws.client.om;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.StopWatch;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;

import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.context.AuditContext;
import ro.cs.logaudit.exception.WSClientException;
import ro.cs.logaudit.ws.client.om.entity.GetAllOrganisationsRequest;
import ro.cs.logaudit.ws.client.om.entity.GetAllOrganisationsResponse;
import ro.cs.logaudit.ws.client.om.entity.GetUserAuthBySecurityTokenRequest;
import ro.cs.logaudit.ws.client.om.entity.GetUserAuthBySecurityTokenResponse;
import ro.cs.logaudit.ws.client.om.entity.GetUsersSimpleRequest;
import ro.cs.logaudit.ws.client.om.entity.GetUsersSimpleResponse;
import ro.cs.logaudit.ws.client.om.entity.OMEndpointExceptionBean;
import ro.cs.logaudit.ws.client.om.entity.ObjectFactory;
import ro.cs.logaudit.ws.client.om.entity.UserSimple;
import ro.cs.logaudit.ws.client.om.entity.WSOrganisation;


/**
 * 
 * Singleton which exposes methods for the OM Web Service Client
 * @author coni
 *
 */
public class OMWebServiceClient extends WebServiceGatewaySupport {

	private static final String omWebServiceClientBean = "omWebServiceClient";
	
	private ObjectFactory objectFactory; 
	protected final Log logger = LogFactory.getLog(getClass());
	
	//singleton implementation
	private OMWebServiceClient(){}
	public static OMWebServiceClient getInstance(){
		return (OMWebServiceClient) AuditContext.getApplicationContext().getBean(omWebServiceClientBean);
	}
	
	//setters and getters
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}	
	
	public List<WSOrganisation> getAllOrganisations() throws XmlMappingException, IOException, WSClientException{
		logger.debug("getAllOrganisations START");
		StopWatch sw = new StopWatch();
		sw.start("getAllOrganisations");
		List<WSOrganisation> allOrganisations = null;
		
		try {
			//create the bean  marshalled into the request
			GetAllOrganisationsRequest getAllOrganisationsRequest = new GetAllOrganisationsRequest();
			//unmarshall the response to an WSOrganisation bean
			GetAllOrganisationsResponse response = (GetAllOrganisationsResponse) getWebServiceTemplate().marshalSendAndReceive(getAllOrganisationsRequest);
			allOrganisations = response.getOrganisations();
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
		logger.debug("getAllOrganisations END");
		sw.stop();
		logger.debug(sw.prettyPrint());
		return allOrganisations;
	}
	
	public GetUserAuthBySecurityTokenResponse getUserAuthBySecurityToken(String securityToken) throws XmlMappingException, IOException, WSClientException{
		logger.debug("getUserAuthBySecurityToken START");
		StopWatch sw = new StopWatch();
		sw.start("getUserAuthBySecurityToken");
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
		sw.stop();
		logger.debug(sw.prettyPrint());
		return getUserAuthBySecurityTokenResponse;
	}
	
	public List<UserSimple> getUsersSimpleByOrganizationId(int organizationId, boolean isNotDeleted) throws XmlMappingException, IOException, WSClientException{
		logger.debug("getUsersSimpleByOrganizationId START");
		StopWatch sw = new StopWatch();
		sw.start("getUsersSimpleByOrganizationId");
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
		sw.stop();
		logger.debug(sw.prettyPrint());
		return users;
	}
	
}
