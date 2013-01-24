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
package ro.cs.cm.ws.server;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;

import ro.cs.cm.context.CMContext;

/**
 *  MessageEndpointInterceptor
 *	
 *  @author dan.damian
 *
 */
public class MessageEndpointInterceptor extends  EndpointInterceptorAdapter {
	
	private   static   final  String soapMessageAttachmentsKey  = "soapMessageAttachments" ;
    @Override
    public   boolean  handleRequest(MessageContext messageContext,Object endpoint) throws  Exception {
    	WSServerThreadLocal.get();
    	logger.debug("============= MESSAGE ENDPOINT INTERCEPTOR =============="); 
    	logger.debug("Endpoint: " + endpoint);
    	AxiomSoapMessage request = (AxiomSoapMessage) messageContext.getRequest();
         Iterator<Attachment> attachments = request.getAttachments() ;
         SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_hh_mm_ss");
         if (attachments != null) {
        	 int i = 0;
        	 while (attachments.hasNext())  {
        		 Attachment attachment = attachments.next();
        		 logger.debug("I: " + (++i));
        		 logger.debug("Content Id: " + attachment.getContentId());
        		 logger.debug("Content Type: " + attachment.getContentType());
        		 logger.debug("Size: " + attachment.getSize());
        		 logger.debug("Writing content to file...");
        		 File f = new File("C:\\File_" + sdf.format(new Date(System.currentTimeMillis())));
        		 FileOutputStream fos = new FileOutputStream(f);
        		 attachment.getDataHandler().writeTo(fos);
        		 fos.close();
        		 logger.debug("Content stored to file !");
        	 } 
         } else {
        	 logger.debug("Null Attachments !");
         }
         
         CMContext.storeOnContext ( soapMessageAttachmentsKey , attachments);
         return   true ;
    }
}
