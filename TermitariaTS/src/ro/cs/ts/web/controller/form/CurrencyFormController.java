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
package ro.cs.ts.web.controller.form;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;

public class CurrencyFormController extends RootSimpleFormController {

	public static final String FORM_VIEW					= "CurrencyForm";
	public static final String SUCCESS_VIEW					= "CurrencyForm";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ID 							= "currencyId";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String GET_ERROR 					= "currency.get.error";
	private static final String UPDATE_MESSAGE 				= "currency.update.message";
	private static final String UPDATE_ERROR 				= "currency.update.error";
	private static final String ADD_MESSAGE					= "currency.add.message";
	private static final String ADD_ERROR					= "currency.add.error";
	private static final String GENERAL_ERROR				= "currency.general.form.error";
	
	//--------------------PARAMETERS-------------------------------------------------------------
	private static final String GET_FROM_PANEL				= "GET_FROM_PANEL";
	private static final String ONSUBMIT					= "ONSUBMIT";
	private static final String FROM_EXCHANGE				= "FROM_EXCHANGE";
	private static final String FROM_COST_SHEET				= "FROM_COST_SHEET";
	private static final String FROM_PERSON_DETAIL			= "FROM_PERSON_DETAIL";
	private static final String FROM_TEAM_MEMBER_DETAIL		= "FROM_TEAM_MEMBER_DETAIL";
	private static final String FROM_PROJECT_DETAIL			= "FROM_PROJECT_DETAIL";
	private static final String FROM_ACTIVITY_PANEL			= "FROM_ACTIVITY_PANEL";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 					= "BACK_URL";
	private static final String NEXT_BACK_URL				= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 			= "ENCODE_BACK_URL";
		
	public CurrencyFormController(){
		setCommandName("currencyBean");
		setCommandClass(Currency.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		Currency currency = null;
		
		try {
			//check if i have to edit the currency
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION) != null &&
					IConstant.CMD_GET.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION))){
				logger.debug("formBackingObject: GET");
				
				//get the currency with the specified currencyId
				currency = handleGet(ServletRequestUtils.getIntParameter(request, ID).intValue(), errorMessages, request);
				if (currency == null){
					currency = new Currency();
				}
			} else {
				logger.debug("formBackingObject: ADD");
				currency = new Currency();
				
				//setting the organizationId for the new record
				Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
				if (organizationId != null){
					currency.setOrganizationId(organizationId);
				}
			}
			
			//if the view will be rendered in a panel we display only some fields
			request.setAttribute(GET_FROM_PANEL, request.getParameter(GET_FROM_PANEL));
			request.setAttribute(FROM_EXCHANGE, request.getParameter(FROM_EXCHANGE));
			request.setAttribute(FROM_COST_SHEET, request.getParameter(FROM_COST_SHEET));
			request.setAttribute(FROM_TEAM_MEMBER_DETAIL, request.getParameter(FROM_TEAM_MEMBER_DETAIL));
			request.setAttribute(FROM_PERSON_DETAIL, request.getParameter(FROM_PERSON_DETAIL));
			request.setAttribute(FROM_PROJECT_DETAIL, request.getParameter(FROM_PROJECT_DETAIL));			
			request.setAttribute(FROM_ACTIVITY_PANEL, request.getParameter(FROM_ACTIVITY_PANEL));		
								
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("formBackingObject - END");
		return currency;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);	
		Currency currency = (Currency) command;

		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		// check if the currency has a currencyId
		if (currency.getCurrencyId() != -1){
			// if I have currencyId, it means that I have "update" action
			logger.debug("onSubmit - handleUpdate");
			mav =  handleUpdate(currency, request, locale, errorMessages, infoMessages);
		} else {
			// if I don't have currency, it means that I have "add" action
			logger.debug("onSubmit - handleAdd");
			mav =  handleAdd(currency, request, locale, errorMessages, infoMessages);
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addAllObjects(referenceData(request, currency, errors));
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * Gets a Currency with all its components
	 * 
	 * @author Coni
	 * @param currencyId
	 * @param errorMessages
	 * @param request
	 * @return
	 */
	public Currency handleGet(int currencyId, ArrayList<String> errorMessages, HttpServletRequest request) {
		logger.debug("handleGet - START");
		Currency currency = null;
		try {
			currency = BLCurrency.getInstance().getAll(currencyId);
		} catch (BusinessException be) {
			logger.error("handleGet", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}

		logger.debug("handleGet - START");
		return currency;
	}
	
	/**
	 * Updates the currency with the specified id
	 * 
	 * @author Coni
	 * @param currency
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleUpdate(Currency currency, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getFormView());
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			//add the command object
			mav.addObject(getCommandName(), currency);

			//update the currency
			BLCurrency.getInstance().update(currency);
			infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, new Object[] {null}, locale));
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_CURRENCY_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
							messageSource.getMessage(IConstant.AUDIT_EVENT_CURRENCY_UPDATE_MESSAGE, new Object[] {currency.getName()}, new Locale("en")),
							messageSource.getMessage(IConstant.AUDIT_EVENT_CURRENCY_UPDATE_MESSAGE, new Object[] {currency.getName()}, new Locale("ro")),  
							ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("Exception while updating currency with id: ".concat(String.valueOf(currency.getCurrencyId())), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error("Exception while updating record with id: ".concat(String.valueOf(currency.getCurrencyId())), e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} 
		
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	/**
	 * Adds a new currency
	 * 
	 * @author Coni
	 * @param currency
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleAdd(Currency currency, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getFormView());
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			//setting the new currency's status to enabled
			currency.setStatus(IConstant.NOM_CURRENCY_STATUS_ACTIVE);
			
			currency = BLCurrency.getInstance().add(currency);
			infoMessages.add(messageSource.getMessage(ADD_MESSAGE, new Object[] {null}, locale));
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_CURRENCY_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
							messageSource.getMessage(IConstant.AUDIT_EVENT_CURRENCY_ADD_MESSAGE, new Object[] {currency.getName()}, new Locale("en")),
							messageSource.getMessage(IConstant.AUDIT_EVENT_CURRENCY_ADD_MESSAGE, new Object[] {currency.getName()}, new Locale("ro")),  
							ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("Exception while adding currency" , be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("Exception while adding currency" , e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		//add the command object
		mav.addObject(getCommandName(), currency);
		logger.debug("handleAdd - END");
		
		//the view came from onsubmit
		//we need this for displaying only the messages when adding the currency
		mav.addObject(ONSUBMIT, true);
		
		return mav;
	}
	
	/**
	 * Adds to model required nomenclators
	 * @author Coni
	 */
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
		logger.debug("referenceData - start");
		Map model = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
						
		try {
			// adding to model the action from the request
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			model.put(IConstant.REQ_ACTION, action);
			
			//put the back url
			String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
			
			String servletPath = request.getServletPath();
			String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");
			
			logger.debug("BACK_URL = " + backUrl);
			logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
			
			model.put(BACK_URL, backUrl);		
			model.put(NEXT_BACK_URL, nextBackUrl);
			if (backUrl != null) {
				model.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));	
			}
		} catch (Exception e) {
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}		
		setErrors(request, errorMessages);
		logger.debug("referenceData - end");
		return model;
	}
}
