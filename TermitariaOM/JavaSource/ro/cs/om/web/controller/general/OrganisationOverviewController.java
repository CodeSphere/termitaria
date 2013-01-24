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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLSetting;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;

/**
 * Returns an Overview of this Organization
 * @author dan.damian
 *
 */
public class OrganisationOverviewController extends RootAbstractController {

	
	
	//------------------------ATTRIBUTES--------------------------------------------------------------
	private static String ORGANISATION_ID = "organisationId";
	
	//------------------------MODEL-------------------------------------------------------------------
	private static String FIRST_LEVEL_NODES 			= "FIRST_LEVEL_NODES";
	private static String MODEL_ORGANIZATION			= "ORGANIZATION";
	private static String FLAG_ORGANIZATION_CHANGED		= "organizationChanged";
	private static String ORGANISATION_PARENT			= "ORGANISATION_PARENT";
	private static String ORGANISATION_MODULES			= "ORGANISATION_MODULES";
	private final String HAS_ADMIN						= "HAS_ADMIN";
	
	//----------------- SESSION ATTRIBUTES -----------------------
	private static String ORGANISATION_DEPARTMENTS		 = "COUNT_DEPARTMENTS";
	private static String ORGANISATION_EMPLOYEES 		 = "COUNT_EMPLOYEES";	
	private static String CEO							 = "CEO";
	
	//----------------- REDIRECT ATTRIBUTES -----------------------
	
	private static String ORGANISATION_REDIRECT 		= "ORGANISATION_REDIRECT";
	
	public OrganisationOverviewController() {
		setView("Organisation_Overview");
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse arg1) throws Exception {
		ModelAndView mav = new ModelAndView(getView());
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer organisationId = null;
		
		//Handling two special Case:
		if (userAuth.isAdminIT()) {
			//When User is Admin IT and we have organisattionId
			if(request.getParameter(ORGANISATION_ID) != null){
				organisationId = ServletRequestUtils.getIntParameter(request, ORGANISATION_ID);
			//we take it from session
			} else {
				organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			}
			
		} else {
			//and when user isn't adminIT
			organisationId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		}
		
		logger.debug("Organisation Id: " + organisationId);
		
		
		//If this user is Admin IT
		if (userAuth.isAdminIT()) {
			//Retrieving old setted Organisation Id
			int sessionOrgId = ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			logger.debug("Old Organisation Id: " + sessionOrgId);
			logger.debug("Just selected Organisation Id: " + organisationId);
			//If ids differ
			if (organisationId != sessionOrgId) {
				//Retrieving Organisation just for setting it's Name as a Session Attribute
				Organisation org  = BLOrganisation.getInstance().get(organisationId);
				//Setting new selected Organisation Id
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_ID, new Integer(organisationId));
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_NAME, org.getName());
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_ADDRESS, org.getAddress());
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_STATUS, org.getStatus());
				
				mav.addObject(FLAG_ORGANIZATION_CHANGED, new Object());
				//If the new theme isn't this one, change the theme
				String theme = BLSetting.getInstance().getSettingValue(organisationId, IConstant.SETTING_THEME);
				   if (!(userAuth.getThemeCode().compareTo(theme) == 0 )) { 
							userAuth.setThemeCode(theme);
							handleRedirect(arg1);
							logger.debug("****************************************************");
							return null;
				   }
			}						
			
			Organisation organisation = BLOrganisation.getInstance().get(organisationId);
			// if we have an organisation 
			if(organisation.getOrganisationId() > 0){				
				
				boolean hasAdmin = BLPerson.getInstance().hasAdminByOrganisation(organisationId);
				
				logger.debug("hasAdmin = " + hasAdmin);
				
				if(hasAdmin){					
					mav.addObject(HAS_ADMIN, "1");
					logger.debug("avem un user cu rolul de administrator");
				} else {
					mav.addObject(HAS_ADMIN, "0");
					logger.debug("nu avem un administrator");
				}
			}
		}
		
		try{
			// put on view, the number of departments from the organisation
			int countDep = BLDepartment.getInstance().getCountDepartments(organisationId);
			mav.addObject(ORGANISATION_DEPARTMENTS, countDep);
			
			// put on view the number of employess from the organisation
			long countEmpl = BLPerson.getInstance().getPersonsCountByOrganization(organisationId);
			mav.addObject(ORGANISATION_EMPLOYEES, countEmpl);		
			
			// put on view the modules from the organisation
			Organisation organisation = BLOrganisation.getInstance().getOrganisationWithModules(organisationId);
			mav.addObject(ORGANISATION_MODULES, organisation.getModules());
			
			Integer parentId = organisation.getParentId();
			Organisation parent = BLOrganisation.getInstance().get(parentId);
			mav.addObject(ORGANISATION_PARENT, parent);
			
			// put on view the Chief Executive Officer
			Department department = BLDepartment.getInstance().getFakeForOrganization(organisationId);	
			Person ceo = null;
			if(department != null) {
				Integer ceoId = department.getManagerId();				
				if(ceoId != null && ceoId > 0) {
					ceo = BLPerson.getInstance().get(ceoId);				
					logger.debug("ceo = " + ceo.getFirstName() + " " + ceo.getLastName());
				}
			}
			mav.addObject(CEO, ceo);
			
			/*Department department = BLDepartment.getInstance()
				.getFakeForOrganization(
						ControllerUtils.getInstance()
								.getOrganisationIdFromSession(request));
		logger.debug("department manager = " + department.getManagerId());*/
									
		} catch(BusinessException be){
			logger.error(be.getMessage(), be);			
		} catch(Exception e){
			logger.error("", e);			
		}
		
		mav.addObject(MODEL_ORGANIZATION, BLOrganisation.getInstance().get(organisationId));
	
		return mav;
		
	}
	
	/**
	 * 
	 * Redirect to start page for changing the theme
	 * @author mitziuro
	 *
	 * @param response
	 * @throws IOException
	 */
	private void handleRedirect(HttpServletResponse response) throws IOException{
		logger.debug("handleRedirect - START");
		try {
			response.setContentType("text/html");
			response.getWriter().write("<body>\n<!-- Declare empty body to work on IE7 -->\n</body>\n");
			response.getWriter().write("<SCRIPT>\nwindow.location = '" + IConstant.APP_START_PAGE + "?"+ ORGANISATION_REDIRECT + "=1';\n </SCRIPT>");
			
			logger.debug("Am pus pe response");
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			response.getWriter().close();
		}
		logger.debug("handleRedirect - END");
	}
}
