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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import ro.cs.ts.business.BLExchange;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.SearchExchangeBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class ExchangeSearchController extends RootSimpleFormController {

	private static final String FORM_VIEW 					= "Exchange_Search";
	private static final String SUCCESS_VIEW				= "Exchange_Listing";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";
	private static final String EXCHANGE_ID					= "exchangeId";
	private static final String DELETE_FROM_EXCHANGE_FORM 	= "DELETE_FROM_EXCHANGE_FORM";
	private static final String ACTION 						= "action";
	private static final String DELETEALL 					= "DELETE_ALL";
	private static final String PAGINATION					= "pagination";
	private static final String SORT						= "sort";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String SEARCH_ERROR 				= "exchange.search.error";
	private static final String SEARCH_EXCEPTION_ERROR 		= "exchange.search.exception.error";
	private static final String PAGINATION_ERROR 			= "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String DELETE_ERROR 				= "exchange.delete.error";
	private static final String DELETE_SUCCESS				= "exchange.delete.success";
	private static final String GENERAL_ERROR				= "exchange.general.search.error";
	private static final String GET_USER_PROJECTS_ERROR 	= "exchange.get.user.projects.error";
	private static final String GET_ORG_CURRENCIES_ERROR 	= "exchange.get.org.currencies.error";
	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String SEARCH_RESULTS 					= "SEARCH_RESULTS";
	private static final String SEARCH_EXCHANGE_BEAN			= "searchExchangeBean";
	private static final String COMMAND							= "command";
	private static final String ORG_CURRENCIES 					= "ORG_CURRENCIES";
	private static final String USER_PROJECTS					= "USER_PROJECTS";
	private static final String IS_PM_FOR_AT_LEAST_ONE_PROJECT 	= "IS_PM_FOR_AT_LEAST_ONE_PROJECT";
	
	// Number of characters that fit in the panel
	// display header if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 50;
    private static final String PAGES 					= "pagination.pages";
    
    
	public ExchangeSearchController() {
		setCommandClass(SearchExchangeBean.class);
		setCommandName("searchExchangeBean");
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		SearchExchangeBean searchExchangeBean = new SearchExchangeBean();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//we set the initial search parameters
		searchExchangeBean.setSortDirection(IConstant.ASCENDING);
		searchExchangeBean.setSortParam("projectName");
		searchExchangeBean.setOrganizationId(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer exchangeId = ServletRequestUtils.getIntParameter(request, EXCHANGE_ID);
		
		// deletes an exchange if the request comes from exchange form
		if(action != null && DELETE_FROM_EXCHANGE_FORM.equals(action) && exchangeId != null) {		
			handleDeleteFromExchangeForm(request, searchExchangeBean, exchangeId, infoMessages, errorMessages);
		}		
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject - END");
		return searchExchangeBean;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();

		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		try {
			boolean isDeleteAction = false;
			
			//check if i have deleteAll action
			if (request.getParameter(ACTION) != null && DELETEALL.equals(request.getParameter(ACTION))) {
				handleDeleteAll(request, command, infoMessages, errorMessages);
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
		
		mav.addAllObjects(referenceData(request, command, errors));
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * @author Coni
	 * Searches for exchanges
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
		
		SearchExchangeBean searchExchangeBean =  (SearchExchangeBean) command;
		
		List<Exchange> res = null;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DecimalFormat dfReal = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
		dfReal.applyPattern("# ### ### ##0.0000");
		DecimalFormat dfInt = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
		dfInt.applyPattern("# ### ### ###");

		try {
			res = BLExchange.getInstance().getResultsForSearch(searchExchangeBean, isDeleteAction);
			
			//set the exchange info panel header name
			for (Exchange exchange : res) {
				String headerName = exchange.getFirstCurrency().getInitials().concat(" - ").concat(exchange.getSecondCurrency().getInitials());
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				exchange.setPanelHeaderName(panelHeaderName);
				if ((exchange.getRate() * 10) % 10 != 0) {
					exchange.setFormattedRate(dfReal.format(exchange.getRate()));
				} else {
					exchange.setFormattedRate(dfInt.format(exchange.getRate()));
				}
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
		ControllerUtils.getInstance().findPagesLimit(searchExchangeBean, PAGES);		
		
		mav.addObject(SEARCH_EXCHANGE_BEAN, searchExchangeBean);
		mav.addObject(COMMAND, command);
		
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
		SearchExchangeBean searchExchangeBean = (SearchExchangeBean) command;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchExchangeBean.setCurrentPage(searchExchangeBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchExchangeBean.setCurrentPage(searchExchangeBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchExchangeBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchExchangeBean.setCurrentPage(searchExchangeBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))) {
						searchExchangeBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchExchangeBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchExchangeBean.setCurrentPage(-1);
		}	

		List<Exchange> res = null; 
		try {
									
			res = BLExchange.getInstance().getResultsForSearch(searchExchangeBean, false);
			
			DecimalFormat dfReal = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
			dfReal.applyPattern("# ### ### ##0.0000");
			DecimalFormat dfInt = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
			dfInt.applyPattern("# ### ### ###");
			
			//set the exchange info panel header name
			for (Exchange exchange : res) {
					String headerName = exchange.getFirstCurrency().getInitials().concat(" - ").concat(exchange.getSecondCurrency().getInitials());
					String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
					exchange.setPanelHeaderName(panelHeaderName);
					if ((exchange.getRate() * 10) % 10 != 0) {
						exchange.setFormattedRate(dfReal.format(exchange.getRate()));
					} else {
						exchange.setFormattedRate(dfInt.format(exchange.getRate()));
					}
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
		ControllerUtils.getInstance().findPagesLimit(searchExchangeBean, PAGES);
	
		mav.addObject(SEARCH_EXCHANGE_BEAN, searchExchangeBean);
		mav.addObject(COMMAND, command);
		

		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Deletes all the exchanges or a specific one
	 * 
	 * @author coni
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		logger.debug("handleDeleteAll - START");
		
		SearchExchangeBean searchExchangeBean = (SearchExchangeBean) command;

		logger.debug(searchExchangeBean);
		logger.debug("start deleting " + searchExchangeBean.getExchangeId().length + " exchange(s).");
		
		handleDeleteAllSimple(request, searchExchangeBean, infoMessages, errorMessages);
		
		logger.debug("Results per page " + searchExchangeBean.getResultsPerPage());
		logger.debug("handleDeleteAll - END");
	}
	
	/**
	 * Deletes exchanges
	 * 
	 * @author coni
	 * 
	 * @param request
	 * @param searchExchangeBean
	 * @throws BusinessException
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchExchangeBean searchExchangeBean, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Exchange exchange = null;
		
		for (int i = 0; i < searchExchangeBean.getExchangeId().length; i++) {
			logger.debug("Delete exchange : " + searchExchangeBean.getExchangeId()[i]);	
			boolean isDeleted = true;
 		
			try {
				exchange = BLExchange.getInstance().delete(searchExchangeBean.getExchangeId()[i]);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
				isDeleted = false;
			}
					
			if (isDeleted) {
				//send notification
				String projectName = null;
				String message = null;	
				Integer projectId = null;
				
				if(exchange != null) {
					if(exchange.getProjectDetail()!=  null ) {
						Project project = BLProject.getInstance().get(exchange.getProjectDetail().getProjectId(), true);
						projectId = exchange.getProjectDetail().getProjectId();
						if(project != null) {
							projectName = project.getName();
						}					
						message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_PROJECT_DELETE;
					} else {
						projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
						message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_ORG_DELETE;
					}
				}
				
				// send a notification regarding the deleting of the exchange
				sendNotificationExchangeDelete(projectId,exchange.getProjectDetailId(),userAuth.getOrganisationId(),
						message,new Object[]{exchange.getFirstCurrency().getName(),exchange.getSecondCurrency().getName(), projectName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_EXCHANGE_DELETE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_EXCHANGE_DELETE);
				
				
				infoMessages.add(messageSource.getMessage(DELETE_SUCCESS, new Object[] { exchange.getFirstCurrency().getInitials().concat(" - ").concat(exchange.getSecondCurrency().getInitials()) }, RequestContextUtils.getLocale(request)));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (exchange.getProjectDetailId() == null) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							Project project = BLProject.getInstance().get(exchange.getProjectId(), true);
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName(), project.getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_DELETE_MESSAGE, new Object[] {exchange.getFirstCurrency().getName(), exchange.getSecondCurrency().getName(), project.getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			}
		}
		logger.debug("handleDeleteAllSimple - END ");
	}
	
	/**
	 * Deletes an exchange that comes from an exchange form
	 * 
	 * @author Coni
	 * @param request
	 * @param searchExchangeBean
	 * @param exchangeId
	 * @throws BusinessException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteFromExchangeForm(HttpServletRequest request, SearchExchangeBean searchExchangeBean, Integer exchangeId, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException{						
		logger.debug("handleDeleteFromExchangeForm - START - ");
		
		Integer[] exchangeIds = new Integer[1];
		exchangeIds[0] = exchangeId;			
		searchExchangeBean.setExchangeId(exchangeIds);
		handleDeleteAllSimple(request, searchExchangeBean, infoMessages, errorMessages);
				
		logger.debug("handleDeleteFromExchangeForm - END - ");
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
			
		try {
			//add the organization available currencies
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			if (currencies != null) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for (Currency currency : currencies) {
					IntString entry = new IntString();
					entry.setValue(currency.getCurrencyId());
					entry.setLabel(currency.getName());
					nomCurrencies.add(entry);
				}
				map.put(ORG_CURRENCIES, nomCurrencies);
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);			
			errorMessages.add(messageSource.getMessage(GET_ORG_CURRENCIES_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		try {
			//adding the user's available project for search;
			//if the user has the USER_ALL role, all its organization's projects will be available
			List<Project> projects = null;
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ExchangeSearchAll())) {
				projects = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), true);
			} else {
				projects = BLProject.getInstance().getProjectsByManager(userAuth.getPersonId(), true, true);
			}			
			map.put(USER_PROJECTS, projects);
			if (projects != null && projects.size() > 0) {
				map.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, new Boolean(true));
			} else {
				map.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, new Boolean (false));
			}
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(GET_USER_PROJECTS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * Sends the notification when the exchange is deleted
	 * @author alexandru.dobre
	 * 
	 * @param projectId
	 * @param projectDetailId
	 * @param organizationId
	 * @param messageCostKey
	 * @param messageCostObjects
	 * @param subjectCostKey
	 * @param subjectCostObjects
	 * @param setting
	 */
	
	public void sendNotificationExchangeDelete(Integer projectId,Integer projectDetailId, Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects, Byte setting ) {
		logger.debug("sendNotificationExchangeDelete - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
						
		try{										
			if(projectId != null) {
				
				// get the project identified by it's projectId
				Project project = BLProject.getInstance().getSimpleProject(projectId);				
				logger.debug("project = " + project);
				Integer managerId = project.getManagerId();
				logger.debug("managerId = " + managerId);
				
				//1. I have to send a notification to the manager of the project					
				userIds.add(String.valueOf(managerId));	
				userIdsMap.put(String.valueOf(managerId), true);
			} 	
			
			// 2. I have to send a notification to the users, that have the permission TS_NotificationReceive
			Set<UserSimple> users = OMWebServiceClient.getInstance().getPersonsFromRole(PermissionConstant.getTheInstance().getTS_NotificationReceive(), organizationId);
			logger.debug("users = " + users);
			if(users != null && users.size() > 0) {							
				for(UserSimple user : users) {
					if (userIds.add(String.valueOf(user.getUserId()))){
						userIdsMap.put(String.valueOf(user.getUserId()), false);
					}
				}
			}													
											
			// send the notification										
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageKey, messageObjects, subjectKey, subjectObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationExchangeDelete - END");
	}
}
