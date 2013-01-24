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
package ro.cs.logaudit.web.controller.general;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.logaudit.business.BLOrganisation;
import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.context.AuditContext;
import ro.cs.logaudit.web.controller.root.ControllerUtils;
import ro.cs.logaudit.web.controller.root.RootAbstractController;
import ro.cs.logaudit.web.security.UserAuth;

/**
 * 
 * @author coni
 *
 */
public class AuditOmReportsController extends RootAbstractController{

	private static final String VIEW = "AuditOmReports";
	private static final String ORGANIZATIONS_ALL = "ORGANIZATIONS_ALL";
	private static final String MODULE_ID = "moduleId";
	private static final String IS_ADMIN_IT = "IS_ADMIN_IT";
	private static final String AUDIT_OM_REPORTS_SHOW_CRITERIA = "audit.om.report.show.criteria";
	private static final String JSON_PERSONS = "JSON_PERSONS";
	
	public AuditOmReportsController(){
		setView(VIEW);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
				
		logger.debug("handleRequestInternal START");
		
		ModelAndView mav = new ModelAndView(getView());
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		try {
			// add all organizations
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			mav.addObject(IS_ADMIN_IT, userAuth.isAdminIT());
			if (userAuth.isAdminIT()){
				mav.addObject(ORGANIZATIONS_ALL, BLOrganisation.getInstance().getAllOrganisations());
			}
		
			//add the om module id
			mav.addObject(MODULE_ID, IConstant.NOM_MODULE_OM_LABEL_KEY);
		
			//add all report format orientations available
			mav.addObject(IConstant.NOM_AUDIT_REPORT_ORIENTATIONS, IConstant.AUDIT_REPORT_ORIENTATIONS);
		
			//add all report formats available
			mav.addObject(IConstant.NOM_AUDIT_REPORT_FORMATS, IConstant.AUDIT_REPORT_FORMATS);
		
			mav.addObject(IConstant.NOM_AUDIT_EVENTS_OM, AuditContext.getFromContext(IConstant.NOM_AUDIT_EVENTS_OM));
			
			// add all the persons from user's organization for autoComplete
			if (!userAuth.isAdminIT()){
				mav.addObject(JSON_PERSONS, 
				ControllerUtils.getInstance().getPersonsFromOrgAsJSON((ControllerUtils.getInstance().getOrganisationIdFromSession(request)), RequestContextUtils.getLocale(request), errorMessages, messageSource));
			}
		} catch (Exception exc) {
			errorMessages.add(messageSource.getMessage(AUDIT_OM_REPORTS_SHOW_CRITERIA, new Object[] {}, RequestContextUtils.getLocale(request)));
		}

		setErrors(request, errorMessages);
		logger.debug("handleRequestInternal END");
		return mav;
	}
}
