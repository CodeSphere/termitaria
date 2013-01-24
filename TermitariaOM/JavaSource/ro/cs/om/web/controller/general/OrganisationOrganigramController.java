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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.OrgTree;
import ro.cs.om.utils.organigram.OrganigramUtils;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * Returns a view containing a Select with All The Organizations
 * @author dan.damian
 *
 */
public class OrganisationOrganigramController extends RootAbstractController {

	
	private static String MODEL_JSON_TREE = "JSON_TREE";
	
	public OrganisationOrganigramController() {
		setView("Organisation_Organigram");
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		ModelAndView mav = new ModelAndView(getView());
		int organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		//Retrieving Organisation Tree of Departments from App Context		
		HashMap<Integer, OrgTree> treesMap = (HashMap<Integer, OrgTree>)OMContext.getOrganisationsTreesMapFromContext(IConstant.ORGANISATION_TREES);
		OrgTree tree = treesMap.get(new Integer(organisationId));
 
		if (tree == null) {
			logger.debug("tree null");
			return mav;
		}

		String jsonTree = OrganigramUtils.getInstance().generateJSONTree(tree); 
		if (jsonTree != null) {
			mav.addObject(MODEL_JSON_TREE, jsonTree);
		}
		
		return mav;
	}


	

}
