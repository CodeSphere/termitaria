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
import org.springframework.stereotype.Component;

import ro.cs.om.business.BLOrganisation;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;

@Component
public class OrganisationBeanValidator {
	private static final Logger LOGGER = getLogger(OrganisationBeanValidator.class);
	
	public OrganisationBeanValidator(){
	}
	
	private static boolean testEmail(String email) {
		
		Pattern p = Pattern.compile("^[a-zA-Z]+([_\\.-]?[a-zA-Z0-9]+)*@[a-zA-Z0-9]+([\\.-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,4})+$");
		Matcher m = p.matcher(email);
		boolean matchFound = false;
		matchFound = m.matches();
		return matchFound;
	}
	
	public void validateOrganizationForm(Organisation organisationBean, ValidationContext context) {
		LOGGER.debug("Validate the organisation - START");
		MessageContext messages = context.getMessageContext();
		
		try {
			if(organisationBean.getName().equals(""))
				messages.addMessage(new MessageBuilder().error().source("name").defaultText("required").build());	
			else
				if(BLOrganisation.getInstance().getOrgByName(organisationBean.getName()) != null) 
					messages.addMessage(new MessageBuilder().error().source("name").defaultText("This name is already in use!").build());
			
			if(organisationBean.getAddress().equals(""))
				messages.addMessage(new MessageBuilder().error().source("address").defaultText("required").build());
			
			if(organisationBean.getPhone().equals("")) 
				messages.addMessage(new MessageBuilder().error().source("phone").defaultText("required").build());
			
			if(organisationBean.getEmail().equals(""))
				messages.addMessage(new MessageBuilder().error().source("email").defaultText("required").build());
			else 
			if(!testEmail(organisationBean.getEmail()))
				messages.addMessage(new MessageBuilder().error().source("email").defaultText("incorrect format").build());
			
			if(organisationBean.getIban().equals(""))
				messages.addMessage(new MessageBuilder().error().source("iban").defaultText("required").build());
			
			if(organisationBean.getLocation().equals(""))
				messages.addMessage(new MessageBuilder().error().source("location").defaultText("required").build());
			
			if(organisationBean.getType() == (byte) -1)
				messages.addMessage(new MessageBuilder().error().source("type").defaultText("Choose a type").build());
						
			if(organisationBean.getCui().equals("")) 
				messages.addMessage(new MessageBuilder().error().source("cui").defaultText("required").build());
			
			if(organisationBean.getJ().equals(""))
				messages.addMessage(new MessageBuilder().error().source("j").defaultText("required").build());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	 
		LOGGER.debug("Validate the organisation - END");
	}
}
