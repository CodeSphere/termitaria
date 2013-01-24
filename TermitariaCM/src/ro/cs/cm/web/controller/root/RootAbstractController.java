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
package ro.cs.cm.web.controller.root;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.Tools;

/**
 * @author matti_joona
 * @author Adelina
 *
 * Controller de baza pentru controllere abstracte care se vor crea de acum in modul
 */
public abstract class RootAbstractController extends AbstractController{
	
	protected MessageSource messageSource; 
	
	
	private String view = null;
	
	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

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
    
    public ModelAndView handleRequest(HttpServletRequest arg0,
    		HttpServletResponse arg1) throws Exception {
		if (view == null) {
            throw new IllegalArgumentException(getClass().toString().concat(": View is not set !"));
        }
    	return handleRequestInternal(arg0, arg1);
    }

}
