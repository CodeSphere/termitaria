/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.web.controller.general;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;

public class ActivityBillingInformationsController extends RootAbstractController {
		
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "Activity_BillingInformations";	
	
	//------------------------OTHER PARAMETERS------------------------------------------------------
	private static final String ORG_CURRENCIES 				= "ORG_CURRENCIES";
	private static final String GET_FROM_PANEL				= "GET_FROM_PANEL";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY 					= "activity.";
	private static final String GET_ORG_CURRENCIES_ERROR 	= ROOT_KEY.concat("get.org.currencies.error");

	
	/**
	 * Contstructor - with the basic settings for the controller.
	 * 
	 * @author Adelina
	 */
	public ActivityBillingInformationsController() {
		setView(VIEW);		
	}
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("handleRequestInternal - START ");
		
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(getView());	
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// adding to model the time unit nomenclator
		mav.addObject(IConstant.NOM_TIME_UNIT, TSContext.getFromContext(IConstant.NOM_TIME_UNIT));
		
		try {
			//add the organization available currencies
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			if (currencies != null && currencies.size() > 0) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for (Currency currency : currencies) {
					IntString entry = new IntString();
					entry.setValue(currency.getCurrencyId());
					entry.setLabel(currency.getName());
					nomCurrencies.add(entry);
				}
				mav.addObject(ORG_CURRENCIES, nomCurrencies);
			}
			
			//if the view will be rendered in a panel we display only some fields
			request.setAttribute(GET_FROM_PANEL, request.getParameter(GET_FROM_PANEL));
			
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);			
			errorMessages.add(messageSource.getMessage(GET_ORG_CURRENCIES_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
				
		setErrors(request, errorMessages);
		
		logger.debug("handleRequestInternal - END ");
		
		return mav;
	}

}
