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
package ro.cs.om.web.controller.general;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLUserGroup;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * Controller used to view an user group's info
 * @author coni
 *
 */
public class UserGroupViewController extends RootAbstractController {

	//-------------------------------------------VIEW----------------------------------------
	private static final String VIEW						= "UserGroupView";
	
	//-------------------------------------------MODEL---------------------------------------
	private static final String USER_GROUP_ID				= "userGroupId";
	private static final String USER_GROUP					= "USER_GROUP";
	
	//-------------------------------------------MESSAGE-------------------------------------
	private static final String GET_ERROR					= "usergroup.get.error";
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE			= 25;
	
	public UserGroupViewController() {
		setView(VIEW);
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("UserGroupViewController - handleRequestInternal - START");
		
		//used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView();
		
		UserGroup userGroup = null;
		
		try{						
			Integer userGroupId = ServletRequestUtils.getRequiredIntParameter(request, USER_GROUP_ID);
			logger.debug("User Group Id: ".concat(userGroupId.toString()));
			
			if(userGroupId != null){							
				//retrieve the user group with all its info
				userGroup = BLUserGroup.getInstance().getAll(userGroupId);
				//add to mav the retrieved user group
				userGroup.setName(ControllerUtils.getInstance().tokenizeField(userGroup.getName(), NR_CHARS_PER_LINE));		
				if(userGroup.getDescription() != null) {
					userGroup.setDescription(ControllerUtils.getInstance().tokenizeField(userGroup.getDescription(), NR_CHARS_PER_LINE));
				}
				mav.addObject(USER_GROUP, userGroup);				
			}			
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal error", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}	
				
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);		
		
		logger.debug("UserGroupViewController - handleRequestInternal - END");
		
		return mav;
	}
		
}
