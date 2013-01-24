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
package ro.cs.om.utils.mail;

import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.util.StringUtils;

import ro.cs.om.common.ApplicationObjectSupport;

/**
 * Class for mail utilities
 * @author Adelina
 *
 */

public class MailUtils extends ApplicationObjectSupport {
	
	private static MailUtils instance = null;
	private static ResourceBundle rb = ResourceBundle.getBundle("config.mail");
	private static final String smtpServer = new String(rb.getString("mail.smtp.server"));
	private static final String user = new String(rb.getString("mail.user"));
	private static final String password = new String(rb.getString("mail.password"));
	private static final String fromMail = new String(rb.getString("mail.fromEmail"));
		
	static{
		instance = new MailUtils();
	}
	
	private MailUtils(){
		
	}
	
	public static MailUtils getInstance(){
		return instance;
	}
	
	
	public int sendMail(String to, String subject, String message) throws Exception{
		try{
			if (!StringUtils.hasLength(to)) {
				throw new Exception("No \"to address\" specified !");
			}
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", "mail.codesphere.ro");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.quitwait", "false");
	         
	        props.put("mail.debug", "true");
	        props.put("mail.smtp.port", "25");
	        
			Session session = Session.getInstance(props, null);
			Transport transport = session.getTransport("smtp");
			transport.connect(smtpServer, user, password);
			
			// create the mail
			Message msg = new MimeMessage(session);
			msg.setFrom(InternetAddress.parse(fromMail, false)[0]);			
			msg.setSentDate(new Date());
			msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to, false));
	        msg.setSubject(subject);
	        msg.setText(message);
	        
	        // send the email
	        transport.sendMessage(msg, msg.getAllRecipients());
			
		} catch(MessagingException me){
			me.getStackTrace();
		}
		
		return 1;
	}

}
