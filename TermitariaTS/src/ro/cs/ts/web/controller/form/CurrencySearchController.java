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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.SearchCurrencyBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;

public class CurrencySearchController extends RootSimpleFormController {

	private static final String FORM_VIEW 					= "Currency_Search";
	private static final String SUCCESS_VIEW				= "Currency_Listing";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";
	private static final String CURRENCY_ID					= "currencyId";
	private static final String DELETE_FROM_CURRENCY_FORM 	= "DELETE_FROM_CURRENCY_FORM";
	private static final String ACTION 						= "action";
	private static final String DELETEALL 					= "DELETE_ALL";
	private static final String PAGINATION					= "pagination";	
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String SEARCH_ERROR 				= "currency.search.error";
	private static final String SEARCH_EXCEPTION_ERROR 		= "currency.search.exception.error";
	private static final String PAGINATION_ERROR 			= "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String DELETE_ERROR 				= "currency.delete.error";
	private static final String DELETE_SUCCESS				= "currency.delete.success";
	private static final String GENERAL_ERROR				= "currency.general.search.error";
	private static final String CURRENCY_CANNOT_BE_DELETED  = "currency.not.deleted.is.used.error";
	private static final String CHECK_CURRENCY_IN_USE_ERROR = "currency.in.use.check.error";
	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String SEARCH_RESULTS 				= "SEARCH_RESULTS";
	private static final String SEARCH_CURRENCY_BEAN		= "searchCurrencyBean";
	private static final String COMMAND						= "command";
	
	// Number of characters that fit in the panel
	// display header if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 50;
    private static final String PAGES 					= "pagination.pages";
    
    
	public CurrencySearchController() {
		setCommandClass(SearchCurrencyBean.class);
		setCommandName("searchCurrencyBean");
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> warningMessages = new ArrayList<String>();
		
		SearchCurrencyBean searchCurrencyBean = new SearchCurrencyBean();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//we set the initial search parameters
		searchCurrencyBean.setSortDirection(IConstant.ASCENDING);
		searchCurrencyBean.setSortParam("name");
		searchCurrencyBean.setOrganizationId(userAuth.getOrganisationId());
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer currencyId = ServletRequestUtils.getIntParameter(request, CURRENCY_ID);
		
		// deletes a currency if the request comes from currency form
		if(action != null && DELETE_FROM_CURRENCY_FORM.equals(action) && currencyId != null) {		
			handleDeleteFromCurrencyForm(request, searchCurrencyBean, currencyId, infoMessages, errorMessages, warningMessages);
		}
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		setWarnings(request, warningMessages);
		
		logger.debug("formBackingObject - END");
		return searchCurrencyBean;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> warningMessages = new ArrayList<String>();

		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		try {
			boolean isDeleteAction = false;
			
			//check if i have deleteAll action
			if (request.getParameter(ACTION) != null && DELETEALL.equals(request.getParameter(ACTION))) {
				handleDeleteAll(request, command, infoMessages, errorMessages, warningMessages);
				isDeleteAction = true;
			}
			if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))) {
				mav = handlePagination(request, errorMessages, command);
			} else {
				mav = handleSearch(request, command, errorMessages, isDeleteAction);
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//setting all messages on response
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		setWarnings(request, warningMessages);
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * @author Coni
	 * Searches for currencies
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages, boolean isDeleteAction) throws BusinessException {
		logger.debug("handeSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		SearchCurrencyBean searchCurrencyBean =  (SearchCurrencyBean) command;
		
		List<Currency> res = null;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CurrencySearch())) {

			try {
				res = BLCurrency.getInstance().getResultsForSearch(searchCurrencyBean, isDeleteAction);
				
				//set the currency info panel header name
				for (Currency currency : res) {
					String headerName = currency.getName();
					String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
					currency.setPanelHeaderName(panelHeaderName);
				}
				mav.addObject(SEARCH_RESULTS, res);
			} catch (BusinessException be) {
				logger.error(be.getMessage(), be);
				mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
				errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
						new Object[] { be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
			} catch (Exception e) {
				logger.error("", e);
				mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
				errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR,
						new Object[] { ControllerUtils.getInstance().getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
			}
			
			// find the number of pages shown in pagination area		
			ControllerUtils.getInstance().findPagesLimit(searchCurrencyBean, PAGES);		
			
			mav.addObject(SEARCH_CURRENCY_BEAN, searchCurrencyBean);
			mav.addObject(COMMAND, command);
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("handleSearch - END - res.size=".concat(String.valueOf(res.size())));
		
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 * 
	 * @author Coni
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request, ArrayList<String> errorMessages, Object command) throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchCurrencyBean searchCurrencyBean = (SearchCurrencyBean) command;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CurrencySearch())) {
			try {
				if (request.getParameter(PAGE) != null) {
					if (NEXT.equals(request.getParameter(PAGE))) {
						searchCurrencyBean.setCurrentPage(searchCurrencyBean.getCurrentPage() + 1);
					}
					if (PREV.equals(request.getParameter(PAGE))) {
						searchCurrencyBean.setCurrentPage(searchCurrencyBean.getCurrentPage() - 1);
					}
					if (FIRST.equals(request.getParameter(PAGE))) {
						searchCurrencyBean.setCurrentPage(1);
					}
					if (LAST.equals(request.getParameter(PAGE))) {
						searchCurrencyBean.setCurrentPage(searchCurrencyBean.getNbrOfPages());
					}
					if (NUMBER.equals(request.getParameter(PAGE))) {
						if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))) {
							searchCurrencyBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
						} else {
							// something is wrong
							// I will show the first page
							searchCurrencyBean.setCurrentPage(-1);
						}
					}
				}
			} catch (Exception e) {
				// something is wrong
				// I will show the first page
				logger.error(PAGINATION_ERROR, e);
				searchCurrencyBean.setCurrentPage(-1);
			}	
	
			List<Currency> res = null; 
			try {
										
				res = BLCurrency.getInstance().getResultsForSearch(searchCurrencyBean, false);
				
				//set the currency info panel header name
				for (Currency currency : res) {
						String headerName = currency.getName();
						String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
						currency.setPanelHeaderName(panelHeaderName);
				}
				mav.addObject(SEARCH_RESULTS, res);				
			} catch (BusinessException be) {
				logger.error(be.getMessage(), be);
				mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
				errorMessages.add(messageSource.getMessage(SEARCH_ERROR, 
						new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
			} catch (Exception e) {
				logger.error("", e);
				mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
				errorMessages.add(messageSource.getMessage(SEARCH_ERROR, 
						new Object[] { ControllerUtils.getInstance().getFormattedCurrentTime() }, 
						RequestContextUtils.getLocale(request)));
			}
			
			
			mav.addObject(SEARCH_RESULTS, res);
	
			// find the number of pages shown in pagination area
			ControllerUtils.getInstance().findPagesLimit(searchCurrencyBean, PAGES);
	
			mav.addObject(SEARCH_CURRENCY_BEAN, searchCurrencyBean);
			mav.addObject(COMMAND, command);
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}

		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Deletes all the currencies or a specific one
	 * 
	 * @author coni
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages, ArrayList<String> warningMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		logger.debug("handleDeleteAll - START");
		
		SearchCurrencyBean searchCurrencyBean = (SearchCurrencyBean) command;

		logger.debug(searchCurrencyBean);
		logger.debug("start deleting " + searchCurrencyBean.getCurrencyId().length + " currencies(s).");
		
		handleDeleteAllSimple(request, searchCurrencyBean, infoMessages, errorMessages, warningMessages);
		
		logger.debug("Results per page " + searchCurrencyBean.getResultsPerPage());
		logger.debug("handleDeleteAll - END");
	}
	
	/**
	 * Deletes currencies
	 * 
	 * @author coni
	 * 
	 * @param request
	 * @param searchCurrencyBean
	 * @throws BusinessException
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchCurrencyBean searchCurrencyBean, ArrayList<String> infoMessages, ArrayList<String> errorMessages, ArrayList<String> warningMessages) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Currency currency = null;
		boolean isUsedInProjects = false; 
		
		// ****************** Security *******************************
		if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CurrencyDelete())) {
			for (int i = 0; i < searchCurrencyBean.getCurrencyId().length; i++) {
				logger.debug("Delete currency : " + searchCurrencyBean.getCurrencyId()[i]);	
				boolean isDeleted = true;
				
				try{
					isUsedInProjects = BLCurrency.getInstance().isUsedInProjects(searchCurrencyBean.getCurrencyId()[i]);
					logger.debug("isUsedInAtLeastOneProject = " + isUsedInProjects);
					
				} catch(BusinessException be){
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(CHECK_CURRENCY_IN_USE_ERROR, new Object[] {BLCurrency.getInstance().getAll(searchCurrencyBean.getCurrencyId()[i]).getName(), be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
					isDeleted = false;
				} catch (Exception e){
					logger.error("", e);
					errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
							.getLocale(request)));
				}
				
				if(isUsedInProjects == false && isDeleted == true) {
					try {
						currency = BLCurrency.getInstance().delete(searchCurrencyBean.getCurrencyId()[i]);
					} catch (BusinessException be) {
						logger.error("", be);
						errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
								RequestContextUtils.getLocale(request)));
						isDeleted = false;
					}
					
					if (isDeleted) {
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] { currency.getName() }, RequestContextUtils.getLocale(request)));
						
						//add the new audit event only if the user is not AdminIT
						try {
							if (!userAuth.isAdminIT()){
								BLAudit.getInstance().add(IConstant.AUDIT_EVENT_CURRENCY_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
										messageSource.getMessage(IConstant.AUDIT_EVENT_CURRENCY_DELETE_MESSAGE, new Object[] {currency.getName()}, new Locale("en")),
										messageSource.getMessage(IConstant.AUDIT_EVENT_CURRENCY_DELETE_MESSAGE, new Object[] {currency.getName()}, new Locale("ro")),  
										ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
							}
						} catch (Exception exc) {
							logger.error("", exc);
						}
					}
				} else {
					warningMessages.add(messageSource.getMessage(CURRENCY_CANNOT_BE_DELETED, new Object[] {BLCurrency.getInstance().getAll(searchCurrencyBean.getCurrencyId()[i]).getName()}, RequestContextUtils.getLocale(request)));
				}
			}
		} else {
			errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}
		logger.debug("handleDeleteAllSimple - END ");
	}
	
	/**
	 * Deletes a currency that comes from a currency form
	 * 
	 * @author Coni
	 * @param request
	 * @param searchCurrencyBean
	 * @param currencyId
	 * @throws BusinessException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteFromCurrencyForm(HttpServletRequest request, SearchCurrencyBean searchCurrencyBean, Integer currencyId, ArrayList<String> infoMessages, ArrayList<String> errorMessages, ArrayList<String> warningMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException{						
		logger.debug("handleDeleteFromCurrencyForm - START - ");
		
		Integer[] currencyIds = new Integer[1];
		currencyIds[0] = currencyId;			
		searchCurrencyBean.setCurrencyId(currencyIds);
		handleDeleteAllSimple(request, searchCurrencyBean, infoMessages, errorMessages, warningMessages);
				
		logger.debug("handleDeleteFromCurrencyForm - END - ");
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) {
		logger.debug("referenceData - START");
		Map map = new HashMap();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// adding to model the results per page for search results
			map.put(IConstant.NOM_RESULTS_PER_PAGE, TSContext
					.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
			} catch (Exception e){
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}

}
