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

/**
 * @author coni
 * EndpointException resolver used to customize the SOAP Fault Message sent after the end point throws such an exception 
 */

import javax.xml.bind.JAXBElement;
import javax.xml.transform.Result;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import ro.cs.om.exception.EndpointException;
import ro.cs.om.ws.server.entity.OMEndpointExceptionBean;
import ro.cs.om.ws.server.entity.ObjectFactory;

public class EndpointExceptionResolver extends SoapFaultMappingExceptionResolver{
	private final static String jaxb2MarshallerContextPath = "ro.cs.om.ws.server.entity";
	
	@Override
	protected void customizeFault(Object endpoint, Exception exception, SoapFault fault)
	{
		// create the corresponding jaxb element of an EndpointException
		if (exception instanceof EndpointException)
		{
			// get the result inside the fault detail to marshal to
			Result result = fault.addFaultDetail().getResult();
			
			//create a new WS entity EndpointException and setting its code and message to be equal to those of actual thrown Exception
			OMEndpointExceptionBean endpointException = new OMEndpointExceptionBean();
			endpointException.setCode(((EndpointException) exception).getCode());
			endpointException.setMessage(exception.getMessage());
			JAXBElement<OMEndpointExceptionBean> soapFaultDetails = new ObjectFactory().createEndpointException(endpointException);
			
			// marshal the EndpointException WS entity to the details tag of a SOAP Fault message
			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller.setContextPath(jaxb2MarshallerContextPath);
			try {
				//When setting the marshaller in applicationContext.xml, Spring calls by default afterPropertiesSet() after setting the 
				//marshaller properties; from Java, I have to do it before marshalling an object
				marshaller.afterPropertiesSet();
			} catch (Exception e) {
				logger.error("Error while setting up Jaxb2Marshaller!");
			}
			marshaller.marshal(soapFaultDetails, result);
		} else {
			super.customizeFault(endpoint, exception, fault);
		}
	}
}
