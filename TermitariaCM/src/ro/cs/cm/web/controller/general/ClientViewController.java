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
package ro.cs.cm.web.controller.general;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLClient;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.entity.Client;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootAbstractController;

/**
 * Controller used to view info for a client
 * @author Coni
 *
 */
public class ClientViewController extends RootAbstractController{

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String GET_ERROR								= "client.get.error";	
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "ClientView";
	
	//------------------------MODEL------------------------------------------------------------------	
	private static final String CLIENT_ID						= "clientId";
	private static final String CLIENT							= "CLIENT";
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 50;
    
	public ClientViewController() {
		setView(VIEW);
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse result) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		// used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
				
		ModelAndView mav = new ModelAndView();
		
		Client client = null;
		
		try {
			Integer clientId = ServletRequestUtils.getIntParameter(request, CLIENT_ID);
			logger.debug("Client Id: ".concat(clientId.toString()));
			
			mav.addObject(CLIENT_ID, clientId);
			
			if (clientId != null) {
				client = BLClient.getInstance().getAll(clientId);
				if (client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					client.setC_name(ControllerUtils.getInstance().tokenizeField(client.getC_name(), NR_CHARS_PER_LINE));
					if (client.getC_location() != null) {
						client.setC_location(ControllerUtils.getInstance().tokenizeField(client.getC_location(), NR_CHARS_PER_LINE));
					}
				} else if (client.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
					client.setP_firstName(ControllerUtils.getInstance().tokenizeField(client.getP_firstName(), NR_CHARS_PER_LINE));
					client.setP_lastName(ControllerUtils.getInstance().tokenizeField(client.getP_lastName(), NR_CHARS_PER_LINE));
				}
				if (client.getAddress() != null) {
					client.setAddress(ControllerUtils.getInstance().tokenizeField(client.getAddress(), NR_CHARS_PER_LINE));
				}
				if (client.getDescription() != null) {
					client.setDescription(ControllerUtils.getInstance().tokenizeField(client.getDescription(), NR_CHARS_PER_LINE));
				}
				if (client.getObservation() != null) {
					client.setObservation(ControllerUtils.getInstance().tokenizeField(client.getObservation(), NR_CHARS_PER_LINE));
				}
			}
			
			mav.addObject(CLIENT, client);
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(BusinessException be) {
			logger.error("handleRequestInternal", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);	
		
		logger.debug("handleRequestInternal - END");
		
		return mav;
	}
	
}
