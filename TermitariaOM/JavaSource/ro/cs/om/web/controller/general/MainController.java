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

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import ro.cs.om.business.BLAuthorization;
import ro.cs.om.business.BLDepartment;
import ro.cs.om.business.BLPerson;
import ro.cs.om.business.BLSetting;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author dan.damian
 *
 */
public class MainController extends RootAbstractController {

	private static String THEME_REDIRECT								= "THEME_REDIRECT";
	private static String ORGANISATION_REDIRECT							= "ORGANISATION_REDIRECT";
	private static final String RESET_REDIRECT							= "RESET_REDIRECT";
	private static final String REDIRECT_SELECT_ORGANIZATION			= "REDIRECT_SELECT_ORGANIZATION";
	private static final String DISPLAY_ORGANIZATION_DELETED_MESSAGE	= "DISPLAY_ORGANIZATION_DELETED_MESSAGE";
	private static final String OM_MODULES 								= "OM_MODULES";
	
	public MainController() {
		setView("OM");
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
		ModelAndView mav = new ModelAndView(getView());
		logger.info("--------------------------------- ORGANIATION MANANGEMENT MODULE  Main Controller begin ---------------------------------------");
		
		StringBuilder omModules = new StringBuilder();
		omModules.append("http://").append(request.getServerName()).append(":").append(request.getServerPort())
		.append("/").append(IConstant.APP_CONTEXT).append("/Modules.htm");
		mav.addObject(OM_MODULES, omModules.toString());

		//if the request came after the adminIT deleted the organization stored on session, I have to remove 
		//from the session all the attributes associated with it 
		if (request.getParameter(REDIRECT_SELECT_ORGANIZATION) != null){
			logger.info("Removing attributes...");
			request.getSession().removeAttribute(IConstant.SESS_ORGANISATION_ID);
			request.getSession().removeAttribute(IConstant.SESS_ORGANISATION_ADDRESS);
			request.getSession().removeAttribute(IConstant.SESS_ORGANISATION_NAME);
			request.getSession().removeAttribute(IConstant.SESS_ORGANISATION_STATUS);
			//parameter bind on the request for OrganisationSearch.htm from OM.jsp 
			mav.addObject(DISPLAY_ORGANIZATION_DELETED_MESSAGE, "true");
			return mav;
		} else {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			logger.info("AUTHORIZE USER...");
			BLAuthorization.getInstance().authorize(userAuth, IConstant.MODULE_ID);
			logger.info("SETTING AUTHENTICATION...");
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(
						userAuth,
						SecurityContextHolder.getContext().getAuthentication().getCredentials(),
						userAuth.getAuthorities())
					);
			logger.info("SETTING IMPORTANT ATTRIBUTES ON SESSION...");
			//Setting some very important attributes on session
			
			if (request.getParameter(THEME_REDIRECT) != null){
				mav.addObject(THEME_REDIRECT,request.getParameter(THEME_REDIRECT));
			
			}
			if (request.getParameter(ORGANISATION_REDIRECT) != null){
				mav.addObject(ORGANISATION_REDIRECT, ORGANISATION_REDIRECT);
			}
			
			if (request.getParameter(RESET_REDIRECT) != null){
				mav.addObject(RESET_REDIRECT,request.getParameter(RESET_REDIRECT));
			
			}
			
			if (!userAuth.isAdminIT()) {
				logger.info("\t USER OF THIS ORGANISATION");
				//If this user is not Admin IT will set it's Organisation Id and Organisation Name
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_ID, new Integer(userAuth.getOrganisationId()));
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_NAME, userAuth.getOrganisationName());
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_ADDRESS, userAuth.getOrganisationAddress());
				request.getSession().setAttribute(IConstant.SESS_ORGANISATION_STATUS, userAuth.getOrganisationStatus());
				//Setting organisation id for view
				mav.addObject(IConstant.SESS_ORGANISATION_ID, request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID));
				//Setting theme for regular user
				userAuth.setThemeCode(BLSetting.getInstance().getSettingValue(userAuth.getOrganisationId(), IConstant.SETTING_THEME));
			} else {
				logger.info("\t USER IS ADMIN_IT");
			}	
			
			logger.info("--------------------------------- ORGANISATION MANANGEMENT MODULE  Main Controller end ---------------------------------------");
			return mav;
		}

	}

	//TODO to be deleted
	private void populate(int howMany)  {
		logger.debug("populate -----------------------------");
		Department dept = null;
		try {
			dept = BLDepartment.getInstance().getFakeForOrganization(105);
		} catch (BusinessException e1) { 
			logger.error("", e1);
		}
		Set<Department> depts = new HashSet<Department>();
		depts.add(dept);
		Person p = null;
		for(int i =0; i < howMany; i ++) {
			int randNo = (int)( (Math.random() * 1000000) % 100000);
			p = new Person();
			p.setUsername("username_" + randNo);
			p.setFirstName("Persoana");
			p.setLastName("No_" + randNo);
			p.setDepts(depts);
			p.setAddress("Str. Aurora Nr. 3");
			p.setEmail("pers.no" + randNo + "@yahoo.com");
			p.setObservation("_" + randNo + "_");
			try {
				BLPerson.getInstance().add(p);
			} catch (BusinessException e) {
				logger.error("Nu am reusit sa introduc persoana.");
				logger.debug("Reincerc!");
			}
			logger.debug("-> " + i);
		}
	}
}
