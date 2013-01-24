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
package ro.cs.om.ws.client.dme;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.TransformerObjectSupport;

/**
 * SoapEnvelopeLoggingInterceptor for client
 * 
 * @author dan.damian
 * 
 */
public class SoapEnvelopeLoggingInterceptor extends TransformerObjectSupport implements ClientInterceptor {

	protected static final Log logger = LogFactory.getLog(SoapEnvelopeLoggingInterceptor.class);
	
	protected void logMessageSource(String logMessage, Source source)
			throws TransformerException {
		if (source != null) {
			Transformer transformer = createNonIndentingTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(source, new StreamResult(writer));
			String message = logMessage + writer.toString();
			logMessage(message);
		}
	}

	protected void logMessage(String message) {
		logger.debug(message);
	}

	private Transformer createNonIndentingTransformer()
			throws TransformerConfigurationException {
		Transformer transformer = createTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		return transformer;
	}

	/* (non-Javadoc)
	 * @see org.springframework.ws.client.support.interceptor.ClientInterceptor#handleFault(org.springframework.ws.context.MessageContext)
	 */
	public boolean handleFault(MessageContext messageContext)
			throws WebServiceClientException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.ws.client.support.interceptor.ClientInterceptor#handleRequest(org.springframework.ws.context.MessageContext)
	 */
	public boolean handleRequest(MessageContext messageContext)
			throws WebServiceClientException {
		SoapMessage message = (SoapMessage) messageContext.getRequest();
		try {
			logMessageSource("<<Client: request >>", message.getPayloadSource());
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.ws.client.support.interceptor.ClientInterceptor#handleResponse(org.springframework.ws.context.MessageContext)
	 */
	public boolean handleResponse(MessageContext messageContext)
			throws WebServiceClientException {
		SoapMessage message = (SoapMessage) messageContext.getResponse();
		try {
			logMessageSource("<<Client: response >>", message.getPayloadSource());
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return true;
	}
}
