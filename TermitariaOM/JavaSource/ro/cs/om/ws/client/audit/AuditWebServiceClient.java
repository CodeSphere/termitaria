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
package ro.cs.om.ws.client.audit;

import java.io.IOException;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;

import ro.cs.om.context.OMContext;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.WSClientException;
import ro.cs.om.ws.client.audit.entity.AuditEndpointExceptionBean;
import ro.cs.om.ws.client.audit.entity.AuditEvent;
import ro.cs.om.ws.client.audit.entity.AuditEventRequest;
import ro.cs.om.ws.client.audit.entity.ObjectFactory;


/**
 * 
 * Singleton which exposes methods for the OM Web Service Client
 * @author coni
 *
 */
public class AuditWebServiceClient extends WebServiceGatewaySupport {

	private static final String auditWebServiceClientBean = "auditWebServiceClient";
	
	private ObjectFactory objectFactory; 
	protected final Log logger = LogFactory.getLog(getClass());
	
	//singleton implementation
	private AuditWebServiceClient(){}
	public static AuditWebServiceClient getInstance(){
		return (AuditWebServiceClient) OMContext.getApplicationContext().getBean(auditWebServiceClientBean);
	}
	
	//setters and getters
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}	
	
	public void addAuditEvent(AuditEvent auditEvent) throws BusinessException, XmlMappingException, IOException, ro.cs.om.exception.WSClientException{
		logger.debug("addAuditEvent START");
		try {
			//create the request bean that will be marshalled into the request payload
			AuditEventRequest auditEventRequest = new AuditEventRequest();		
			auditEventRequest.setAuditEvent(auditEvent);
			
			//send the request
			getWebServiceTemplate().marshalSendAndReceive(auditEventRequest);
		} catch (SoapFaultClientException soapFault) {
			
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<AuditEndpointExceptionBean> auditEndpointExceptionBean = (JAXBElement<AuditEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(auditEndpointExceptionBean.getValue().getCode(),auditEndpointExceptionBean.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("addAuditEvent END");
	}

}
