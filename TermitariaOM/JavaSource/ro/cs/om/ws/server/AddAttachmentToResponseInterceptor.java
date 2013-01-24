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

import java.util.ArrayList;
import java.util.UUID;

import javax.activation.DataHandler;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;

public class AddAttachmentToResponseInterceptor extends  EndpointInterceptorAdapter{
    
	@SuppressWarnings("unchecked")
	@Override
    public boolean  handleResponse(MessageContext messageContext,Object endpoint) throws  Exception {
		ArrayList<DataHandler> attachments = (ArrayList<DataHandler>) WSServerThreadLocal.get();
		logger.debug("============= ADD ATTACHMENT ENDPOINT INTERCEPTOR =============="); 
		AxiomSoapMessage response = (AxiomSoapMessage) messageContext.getResponse();
		for(DataHandler att : attachments ){
			UUID uuid = UUID.randomUUID();
			response.addAttachment(uuid.toString(), att);
		}
		attachments.clear();
		WSServerThreadLocal.set(null);
		logger.debug("============= END ADD ATTACHMENT ENDPOINT INTERCEPTOR =============="); 
		return true;
	}
}
