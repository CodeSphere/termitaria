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
package ro.cs.om.signupWizard;

import static org.apache.log4j.Logger.getLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

import ro.cs.om.business.BLPerson;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;

public class PersonBeanValidator {
	private static final Logger LOGGER = getLogger(PersonBeanValidator.class);
	
	public PersonBeanValidator(){
	}
	
	private static boolean testEmail(String email) {
		
		Pattern p = Pattern.compile("^[a-zA-Z]+([_\\.-]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]+([\\.-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,4})+$");
		Matcher m = p.matcher(email);
		boolean matchFound = false;
		matchFound = m.matches();
		return matchFound;
	}
	
	public void validateAdminForm(Person personBean, ValidationContext context) {
		LOGGER.debug("Validate the admin - START");
		MessageContext messages = context.getMessageContext();
		
	
			if(personBean.getFirstName().equals(""))
				messages.addMessage(new MessageBuilder().error().source("firstName").defaultText("required").build());	
			
			if(personBean.getLastName().equals(""))
				messages.addMessage(new MessageBuilder().error().source("lastName").defaultText("required").build());	
			
			if(personBean.getEmail().equals(""))
				messages.addMessage(new MessageBuilder().error().source("email").defaultText("required").build());	
			else 
				if(!testEmail(personBean.getEmail()))
					messages.addMessage(new MessageBuilder().error().source("email").defaultText("incorrect form").build());	
			
		try {	
			if(BLPerson.getInstance().getByUsername(personBean.getUsername()) != null)
				messages.addMessage(new MessageBuilder().error().source("username").defaultText("Username already exists!").build());	
		} catch (BusinessException e) {
			e.printStackTrace();
		} 
	 
		LOGGER.debug("Validate the organisation - END");
	}
}
