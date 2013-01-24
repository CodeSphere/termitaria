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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLPicture;
import ro.cs.om.entity.Person;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * @author dan.damian
 *
 */
public class PersonRenderPictureController extends RootAbstractController {

	
	private String PERSON_ID 					= "personId";
	private String PICTURE_ID 					= "pictureId";
	private String CMD_DELETE_PICTURE 			= "DELETE";
	private String MODEL_DELETE_CASE 			= "DELETE_CASE";
	
	
	public PersonRenderPictureController() {
		setView("Person_PictureResponse");
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(getView());
		
		int personId = 0;
		
		try {
			personId = Integer.parseInt(request.getParameter(PERSON_ID));
		} catch(NumberFormatException nfex) {
			
		}
		
		if (personId > 0) {
			
			Person person = BLPerson.getInstance().get(new Integer(personId));
			logger.debug("am luat persoana cu id-ul: " + person.getPersonId());
			
			if (request.getParameter(CMD_DELETE_PICTURE) != null) {
				logger.debug("Sterg poza persoanei " + personId);
				BLPicture.getInstance().deleteByPersonId(new Integer(person.getPersonId()));
				mav.addObject(PERSON_ID, person.getPersonId());
				mav.addObject(PICTURE_ID, 0);
				mav.addObject(MODEL_DELETE_CASE, new Boolean(true));
			} else {
				Integer pictureId = BLPicture.getInstance().getByPersonId(person.getPersonId());
				mav.addObject(PERSON_ID, person.getPersonId());
				mav.addObject(PICTURE_ID,pictureId);
				logger.debug("Picture ID: " + pictureId);
			}
			
		} 
		
		
		return mav;
	}
	
	

}
