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
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLOrganisation;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;

/**
 * Form controller which operates on 'Organization' entity
 * 
 * @author mitziuro
 */
public class OrganisationBranchController extends RootAbstractController {


	private static final String SEARCH_ORGS 					= "SEARCH_ORGS";
	private static final String INITIAL_BRANCH_GET_ERROR		= "organisation.initial.branch.error";
	private static final String ORG								= "ORG";
	private static final String PARENT							= "PARENT";
	private static final String PARENT_NAME						= "PARENT_NAME";
	private static final String ALL_OPTIONS						= "ALL_OPTIONS";
	private static final String ROOT							= "ROOT";
	private static final String ROOT_NAME						= "ROOT_NAME";
	private static final int    ROOT_ORG						= -1;

	/**
	 * Constructor for setting of the form
	 *
	 * @author mitziuro
	 */
	public OrganisationBranchController(){
		setView("OrganisationBranch");
	}
	

		
	/**
	 * Method which is called at every request
	 *
	 * @author mitziuro
	 */		
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView(getView());
		Locale locale =  RequestContextUtils.getLocale(request);
			
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
	
		try{
			
			List<Organisation> orgs = manageBranches(request, errorMessages);
				
			//The resulting branches have branches
			manageSubBranches(orgs);
			request.setAttribute(SEARCH_ORGS, orgs);
				
			//we put the all branches search option 
			request.setAttribute(ALL_OPTIONS, request.getParameter(ALL_OPTIONS));
				
		} catch (Exception e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(INITIAL_BRANCH_GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		
		setErrors(request, errorMessages);
		logger.debug("onSubmit - END");

		return mav;
		
	}
	

	/**
	 * 
	 * Check if the listed branches have subbranches
	 * @author mitziuro
	 *
	 * @param orgs
	 * @throws BusinessException
	 */
	private void manageSubBranches(List<Organisation> orgs) throws BusinessException{
		
		for(Organisation o : orgs){
			List<Organisation> branches =  BLOrganisation.getInstance().getAllOrganisationsForParentId(o.getOrganisationId());
			//If it has branches we set the branch flag
			if(branches.size() > 0) {
				o.setHasBranches(true);
			} else {
				o.setHasBranches(false);
			}
		}
	}
	
	/**
	 * Get the initial Branches
	 *
	 * @author mitziuro
	 * @param request
	 * @param errorMessages
	 * @return
	 * @throws BusinessException
	 */
	private List<Organisation> manageBranches(HttpServletRequest request, ArrayList<String> errorMessages) throws BusinessException{
		
		Organisation organisation = null;
		
		Integer organisationId = Integer.valueOf(request.getParameter(ORG));
		
		//if the request comes for the parent of the organisation from session we 
		//send a list containig only the organisation
		if(organisationId == ROOT_ORG) {
			List<Organisation> orgs = new ArrayList();
			organisation = BLOrganisation.getInstance().get((Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID));
			orgs.add(organisation);
			manageSubBranches(orgs);
			return orgs;
		}
		
		organisation = BLOrganisation.getInstance().get(Integer.valueOf(request.getParameter(ORG)));
		
		//we get the branches
		List<Organisation> orgs = BLOrganisation.getInstance().getAllOrganisationsForParentId(organisationId);
		//handleGetBranches(request, errorMessages, organisationId);	
		Integer hq = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		Integer parent = organisation.getParentId();
		String parentName = null;
		
		//Condition for showing the return(back) option
		if(organisationId.intValue() != hq) {
			
			
			//If we don't have branches in the curent organisation we go back to the parent
			
			if(orgs.size() == 0) {
				organisation = BLOrganisation.getInstance().get(parent);
				organisationId = organisation.getOrganisationId();
				parent = organisation.getParentId();
				//We search again for the branch organisations
				orgs =  BLOrganisation.getInstance().getAllOrganisationsForParentId(organisationId);
				
			}
			
			//we set the parent name and id
			Organisation parentOrg = BLOrganisation.getInstance().get(parent);
			//we get the parent
			if(parentOrg != null){
				parentName = parentOrg.getName();
			}
			
		}
		
		//Check again if we put ...
		if(organisationId.intValue() != hq) {
			request.setAttribute(PARENT, parent);
			request.setAttribute(PARENT_NAME, parentName);
			
		} else {
			request.setAttribute(ROOT, hq.intValue());
			request.setAttribute(ROOT_NAME, request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME));
		}
		
		//we return the branches list
		return orgs;
	}
	
}
