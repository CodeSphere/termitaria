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
package ro.cs.ts.ws.client.reports;

import java.util.ResourceBundle;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPMessage;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.exception.WSClientException;




/**
 * 
 * @author coni
 * 
 */
public class Axis2Utils extends ApplicationObjectSupport{
	
	private static ResourceBundle rb = ResourceBundle.getBundle("config.config");
	private static final String leafReportsWsUri = rb.getString("reports.ws.client.uri");
	private final String axis2AttachmentCacheDir = rb.getString("reports.ws.client.attachment.cache.dir");
	private static final String wsSecurityInterceptorBean = "wsClientsSecurityInterceptor";
	
	private static Axis2Utils theInstance = null;
	static{
		theInstance = new Axis2Utils();
	}
	private Axis2Utils() {}
	
	public static Axis2Utils getInstance() {
		return theInstance;
	}
	/**
	 * Returns the Axis 2 Service Client having the options set for sending requests to Reports WS
	 * @throws AxisFault 
	 * @throws WSClientException 
	 */
	public ServiceClient getLeafReportsAxis2ServiceClient() throws AxisFault, WSClientException{
		logger.debug("getLeafReportsAxis2ServiceClient START");
		ServiceClient leafReportsAxis2ServiceClient = null;
		try {
			//Setting the Axis 2 client options  
			org.apache.axis2.client.Options options = new org.apache.axis2.client.Options();
			//Enable SOAP with Attachments method
			options.setProperty(Constants.Configuration.ENABLE_SWA,Constants.VALUE_TRUE);
			options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
			// Increase the time out when sending large attachments
			options.setTimeOutInMilliSeconds(10000000);
			//set the endpoint URI
			options.setTo(new EndpointReference(leafReportsWsUri));
			//set the attachment cache
		    options.setProperty(Constants.Configuration.CACHE_ATTACHMENTS,Constants.VALUE_TRUE);
		    options.setProperty(Constants.Configuration.ATTACHMENT_TEMP_DIR,axis2AttachmentCacheDir);
		    options.setProperty(Constants.Configuration.FILE_SIZE_THRESHOLD, "4000");

		    leafReportsAxis2ServiceClient = new ServiceClient();
			leafReportsAxis2ServiceClient.setOptions(options);
		} catch (Exception exc) {
			throw new WSClientException(ICodeException.REPORTS_WSCLIENT_CREATE_AXIS_CLIENT, exc);
		}
		logger.debug("getLeafReportsAxis2ServiceClient END");
		return leafReportsAxis2ServiceClient;
	}
	
	/**
	 * Signs the Axis 2 envelope using Spring Wss4jSecurityInterceptor defined in applicationContext-security.xml
	 * @throws WSClientException 
	 */
	public SOAPEnvelope signSoapEnvelope(SOAPEnvelope envelope) throws WSClientException{
		logger.debug("signSoapEnvelope START");
		try {
			//create SOAP factory used to generate SOAPMessages and AxiomSoapMessages
			SOAPFactory soapFactory = OMAbstractFactory.getSOAP11Factory();
			SOAPMessage apacheAxiomSoapMessage = soapFactory.createSOAPMessage();
			AxiomSoapMessage springAxiomSoapMessage = new AxiomSoapMessage(soapFactory);
			
			//set the apacheAxiomSoapMessage envleope
			apacheAxiomSoapMessage.setSOAPEnvelope(envelope);
			springAxiomSoapMessage.setAxiomMessage(apacheAxiomSoapMessage);
			
			//create a new Spring WS Message Context with the request message builded from the axis 2 envelope 
			DefaultMessageContext springMessageContext = new DefaultMessageContext(springAxiomSoapMessage, new AxiomSoapMessageFactory());
			//get the Wss4jSecurityInterceptor and sign the request message
			Wss4jSecurityInterceptor wss4jSecurityInterceptor = (Wss4jSecurityInterceptor) TSContext.getApplicationContext().getBean(wsSecurityInterceptorBean);
			wss4jSecurityInterceptor.handleRequest(springMessageContext);
			springAxiomSoapMessage = (AxiomSoapMessage) springMessageContext.getRequest();
			envelope = springAxiomSoapMessage.getAxiomMessage().getSOAPEnvelope();	
			logger.debug("signSoapEnvelope END");
			return envelope;
		} catch (Exception exc) {
			throw new WSClientException(ICodeException.REPORTS_WSCLIENT_SIGN_REQUEST_MESSAGE, exc);
		}

	}
}
