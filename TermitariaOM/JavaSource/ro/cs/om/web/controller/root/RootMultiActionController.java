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
package ro.cs.om.web.controller.root;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import ro.cs.om.common.IConstant;
import ro.cs.tools.Tools;

/**
 * Controller which will be extended by othe controllers of this type
 *
 * @author matti_joona
 */
public abstract class RootMultiActionController extends MultiActionController{
	
	
	protected MessageSource messageSource; 
	
	/**
	 * @param messages the messages to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	protected void setErrors(HttpServletRequest request, List<String> errors) {
		if (Tools.getInstance().voidListCondition(errors)) return;
		
		if (request.getAttribute(IConstant.REQ_ATTR_ERRORS) != null){
    		List<String> oldErrors = (List<String>)request.getAttribute(IConstant.REQ_ATTR_ERRORS);
    		oldErrors.addAll(errors);
    	}else {
    		request.setAttribute(IConstant.REQ_ATTR_ERRORS, errors);
    	}
    }

    protected void setMessages(HttpServletRequest request, List<String> messages) {
    	if (Tools.getInstance().voidListCondition(messages)) return;
    	
    	if (request.getAttribute(IConstant.REQ_ATTR_MSGS) != null){
    		List<String> oldErrors = (List<String>)request.getAttribute(IConstant.REQ_ATTR_MSGS);
    		oldErrors.addAll(messages);
    	}else {
    		request.setAttribute(IConstant.REQ_ATTR_MSGS, messages);
    	}
    }

 
}
