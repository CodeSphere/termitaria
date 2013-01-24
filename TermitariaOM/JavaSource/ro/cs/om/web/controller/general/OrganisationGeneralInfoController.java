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

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;


/**
 * Controller used to retrieve and display an organization general information when clicking on a 
 * tree node of type organization
 * @author coni
 *
 */
public class OrganisationGeneralInfoController extends RootAbstractController{
	
	//------------------------ATTRIBUTES--------------------------------------------------------------
	private static String ORGANIZATION_ID = "currentOrganisationId";
	
	//------------------------MODEL-------------------------------------------------------------------
	private static String MODEL_ORGANIZATION			= "ORGANIZATION";
	private static String ORGANISATION_PARENT			= "ORGANISATION_PARENT";
	private static String ORGANISATION_MODULES			= "ORGANISATION_MODULES";
	private static String ORGANISATION_DEPARTMENTS		= "COUNT_DEPARTMENTS";
	private static String ORGANISATION_EMPLOYEES 		= "COUNT_EMPLOYEES";	
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE			= 40;
	
	public OrganisationGeneralInfoController(){
		setView("OrganisationGeneralInfo");
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("OrganisationGeneralInfoController - handleRequestInternal - START");
		ModelAndView mav= new ModelAndView(getView());
		
		Integer organizationId = null;
	
		try{
			//retrieve from the request attributes the organization id for which to display general info
			organizationId = ServletRequestUtils.getRequiredIntParameter(request, ORGANIZATION_ID);
			
			//retrieve the organization with organizationId from the database
			Organisation organization = BLOrganisation.getInstance().getOrganisationWithModules(organizationId);
			
			organization.setName(ControllerUtils.getInstance().tokenizeField(organization.getName(), NR_CHARS_PER_LINE));		
			organization.setAddress(ControllerUtils.getInstance().tokenizeField(organization.getAddress(), NR_CHARS_PER_LINE));	
			organization.setEmail(ControllerUtils.getInstance().tokenizeField(organization.getEmail(), NR_CHARS_PER_LINE));	
			if(organization.getObservation() != null) {
				organization.setObservation(ControllerUtils.getInstance().tokenizeField(organization.getObservation(), NR_CHARS_PER_LINE));
			}
			organization.setJ(ControllerUtils.getInstance().tokenizeField(organization.getJ(), NR_CHARS_PER_LINE));	
			organization.setCui(ControllerUtils.getInstance().tokenizeField(organization.getCui(), NR_CHARS_PER_LINE));	
			organization.setIban(ControllerUtils.getInstance().tokenizeField(organization.getIban(), NR_CHARS_PER_LINE));	
			organization.setLocation(ControllerUtils.getInstance().tokenizeField(organization.getLocation(), NR_CHARS_PER_LINE));	
			
			// put on view the number of departments from the organization
			int countDep = BLDepartment.getInstance().getCountDepartments(organizationId);
			mav.addObject(ORGANISATION_DEPARTMENTS, countDep);
			
			// put on view the number of employees from the organization
			long countEmpl = BLPerson.getInstance().getPersonsCountByOrganization(organizationId);
			mav.addObject(ORGANISATION_EMPLOYEES, countEmpl);		
			
			// put on view the modules from the organization
			mav.addObject(ORGANISATION_MODULES, organization.getModules());
			
			//put on view the organization parent
			Integer parentId = organization.getParentId();
			Organisation parent = BLOrganisation.getInstance().get(parentId);
			mav.addObject(ORGANISATION_PARENT, parent);
			
			//put on view the requested organization
			mav.addObject(MODEL_ORGANIZATION, organization);
									
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);			
		} catch(Exception e){
			logger.error("Error at retrieving the organization general info", e);			
		}
		
		logger.debug("OrganisationGeneralInfoController - handleRequestInternal - END");		
		return mav;
	}
}
