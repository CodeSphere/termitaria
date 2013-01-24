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

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLExchange;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class ExchangeFormController extends RootSimpleFormController {

	public static final String FORM_VIEW					= "ExchangeForm";
	public static final String SUCCESS_VIEW					= "ExchangeForm";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ID 							= "exchangeId";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String GET_ERROR 					= "exchange.get.error";
	private static final String UPDATE_MESSAGE 				= "exchange.update.message";
	private static final String UPDATE_ERROR 				= "exchange.update.error";
	private static final String ADD_MESSAGE					= "exchange.add.message";
	private static final String ADD_ERROR					= "exchange.add.error";
	private static final String GENERAL_ERROR				= "exchange.general.form.error";
	private static final String GET_USER_PROJECTS_ERROR 	= "exchange.get.user.projects.error";
	private static final String GET_ORG_CURRENCIES_ERROR 	= "exchange.get.org.currencies.error";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 					= "BACK_URL";
	private static final String NEXT_BACK_URL				= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 			= "ENCODE_BACK_URL";
	
	//--------------------MODEL-----------------------------------------------------------------------
	private static final String USER_PROJECTS					= "USER_PROJECTS";
	private static final String PROJECT_ID						= "PROJECT_ID";
	private static final String ORG_CURRENCIES 					= "ORG_CURRENCIES";
	private static final String IS_PM_FOR_AT_LEAST_ONE_PROJECT 	= "IS_PM_FOR_AT_LEAST_ONE_PROJECT";
	private static final String EXISTING_EXCHANGE_ID			= "EXISTING_EXCHANGE_ID";
	
	//--------------------PARAMETERS-------------------------------------------------------------
	private static final String GET_FROM_PANEL					= "GET_FROM_PANEL";
	private static final String ONSUBMIT						= "ONSUBMIT";
		
	public ExchangeFormController(){
		setCommandName("exchangeBean");
		setCommandClass(Exchange.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * Registering Custom Editors to this controller for 
	 * better binding request parameters.
	 * (More details on each registering)
	 * 
	 * @author coni
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");
		
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
		df.applyPattern("#########0.0000");
		binder.registerCustomEditor(java.lang.Float.class, "rate", new CustomNumberEditor(java.lang.Float.class, df, false));

		logger.debug("initBinder - END");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		Exchange exchange = null;
		
		try {
			//check if i have to edit the exchange
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION) != null &&
					IConstant.CMD_GET.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION))){
				logger.debug("formBackingObject: GET");
				
				//get the exchange with the specified exchangeId
				exchange = handleGet(ServletRequestUtils.getIntParameter(request, ID).intValue(), errorMessages, request);
				if (exchange == null){
					exchange = new Exchange();
				}
			} else {
				logger.debug("formBackingObject: ADD");
				exchange = new Exchange();
				exchange.setOrganizationId(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
			}
			
			//if the view will be rendered in a panel we display only some fields
			request.setAttribute(GET_FROM_PANEL, request.getParameter(GET_FROM_PANEL));
			
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		if (request.getParameter("projectId") != null)
			exchange.setProjectId(Integer.parseInt(request.getParameter("projectId")));
		
		logger.debug("formBackingObject - END");
		return exchange;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		Exchange exchange = (Exchange) command;
		
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		// check if the exchange has an exchangeId
		if (exchange.getExchangeId() != -1){
			// if I have exchangeId, it means that I have "update" action
			logger.debug("onSubmit - handleUpdate");
			mav =  handleUpdate(exchange, request, locale, errorMessages, infoMessages);
		} else {
			// if I don't have exchangeId, it means that I have "add" action;
			//first check if i have to verify if any exchange exists for the specified project and currencies;
			//if EXCHANGE_ID_TO_UPDATE != null it means i must update an older exchange bean
			if (ServletRequestUtils.getIntParameter(request, EXISTING_EXCHANGE_ID) != null) {
				logger.debug("onSubmit - handleUpdateAlreadyExistent");
				mav = handleUpdateAlreadyExisting(exchange, request, locale, errorMessages, infoMessages, ServletRequestUtils.getIntParameter(request, EXISTING_EXCHANGE_ID));
			} else {
				logger.debug("onSubmit - handleAdd");
				mav =  handleAdd(exchange, request, locale, errorMessages, infoMessages);
			}
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addAllObjects(referenceData(request, exchange, errors));
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * Gets an Exchange with all its components
	 * 
	 * @author Coni
	 * @param exchangeId
	 * @param errorMessages
	 * @param request
	 * @return
	 */
	public Exchange handleGet(int exchangeId, ArrayList<String> errorMessages, HttpServletRequest request) {
		logger.debug("handleGet - START");
		Exchange exchange = null;
		try {
			exchange = BLExchange.getInstance().getAll(exchangeId);
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (exchange != null) {
				if (exchange.getProjectDetail() != null) {
					exchange.setProjectId(exchange.getProjectDetail().getProjectId());
				}
				if (exchange.getFirstCurrency() != null) {
					exchange.setFirstCurrencyId(exchange.getFirstCurrency().getCurrencyId());
				}
				if (exchange.getSecondCurrency() != null) {
					exchange.setSecondCurrencyId(exchange.getSecondCurrency().getCurrencyId());
				}
			}
		} catch (BusinessException be) {
			logger.error("handleGet", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("handleGet - START");
		return exchange;
	}
	
	/**
	 * Updates the exchange with the specified id
	 * 
	 * @author Coni
	 * @param exchange
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleUpdate(Exchange exchange, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages) {
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		try {
			//add the command object
			mav.addObject(getCommandName(), exchange);
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//update the exchange
			BLExchange.getInstance().update(exchange);
			infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, new Object[] {null}, locale));
			
			//send the notification
			String projectName = null;
			String message = null;
			
			if(exchange.getProjectId()!= null && exchange.getProjectId() != -1) {
				Project project = BLProject.getInstance().get(exchange.getProjectId(), true);
				if(project != null) {
					projectName = project.getName();
				}
				message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_PROJECT_UPDATE;
			} else {
				projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
				message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_ORG_UPDATE;
			}
			exchange.setFirstCurrency(BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()));
			exchange.setSecondCurrency(BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()));
			// send a notification regarding the updating of the exchange
			sendNotificationExchange(exchange.getProjectId(),exchange.getProjectDetailId(),userAuth.getOrganisationId(),
					message,new Object[]{exchange.getFirstCurrency().getName(),exchange.getSecondCurrency().getName(), projectName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
					IConstant.NOTIFICATION_SUBJECT_EXCHANGE_UPDATE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_EXCHANGE_UPDATE);
			
			
			//add the new audit event only if the user is not AdminIT
			try {
				if (!userAuth.isAdminIT()){
					if (exchange.getProjectId().equals(IConstant.NOM_EXCHANGE_FORM_PROJECT_SELECT_ORG_OPTION)) { 
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_UPDATE_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName()}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_UPDATE_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName()}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					} else {
						Project project = BLProject.getInstance().get(exchange.getProjectId(), true);
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName(), project.getName()}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName(), project.getName()}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				}
			} catch (Exception exc) {
				logger.error("", exc);
			}
		} catch (BusinessException be) {
			logger.error("Exception while updating exchange with id: ".concat(String.valueOf(exchange.getExchangeId())), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error("Exception while updating exchange with id: ".concat(String.valueOf(exchange.getExchangeId())), e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} 
		
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	/**
	 * Updates the rate of the exchange with the id exchangeIdToUpdate 
	 * in that case when the user chooses to add an exchange
	 * that already exists for a project with the specified currencies 
	 * and wants to update it
	 * 
	 * @author Coni
	 * @param exchange
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @param exchangeIdToUpdate
	 * @return
	 */
	private ModelAndView handleUpdateAlreadyExisting(Exchange exchange, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, int existingExchangeId) {
		logger.debug("handleUpdateAlreadyExisting - START");
		Exchange existingExchange = handleGet(existingExchangeId, errorMessages, request);
		existingExchange.setRate(exchange.getRate());
		ModelAndView mav = handleUpdate(existingExchange, request, locale, errorMessages, infoMessages);
		logger.debug("handleUpdateAlreadyExisting - END");
		return mav;
	}

	
	/**
	 * Adds a new exchange
	 * 
	 * @author Coni
	 * @param exchange
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleAdd(Exchange exchange, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			Exchange oldExchange = null;
			if (exchange.getProjectId().equals(new Integer(IConstant.NOM_EXCHANGE_SEARCH_FOR_ORG))) {
				oldExchange = BLExchange.getInstance().getOrganizationExchangeByCurrencies(exchange.getFirstCurrencyId(), exchange.getSecondCurrencyId(), exchange.getOrganizationId());
			} else if (exchange.getProjectId() > 0) {
				ProjectDetails projectDetail = BLProjectDetails.getInstance().getByProjectId(exchange.getProjectId());
				if (projectDetail != null) {
					oldExchange = BLExchange.getInstance().getProjectExchangeByCurrencies(exchange.getFirstCurrencyId(), exchange.getSecondCurrencyId(), projectDetail.getProjectDetailId());
				}
			}
			if (oldExchange == null) {
				//add a new exchange;
				//setting the new exchange status to enabled
				exchange.setStatus(IConstant.NOM_COST_SHEET_STATUS_ACTIVE);
				exchange = BLExchange.getInstance().add(exchange);
				infoMessages.add(messageSource.getMessage(ADD_MESSAGE, new Object[] {null}, locale));
				
				//send the notification
				String projectName = null;
				String message = null;
				
				if(exchange.getProjectId() != null && exchange.getProjectId() != -1) {
					Project project = BLProject.getInstance().get(exchange.getProjectId(), true);
					if(project != null) {
						projectName = project.getName();
					}
					message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_PROJECT_ADD;
				} else {
					projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
					message = IConstant.NOTIFICATION_MESSAGE_EXCHANGE_ORG_ADD;
				}
				exchange.setFirstCurrency(BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()));
				exchange.setSecondCurrency(BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()));
				// send a notification regarding the adding of the exchange
				sendNotificationExchange(exchange.getProjectId(),exchange.getProjectDetailId(),userAuth.getOrganisationId(),
						message,new Object[]{exchange.getFirstCurrency().getName(),exchange.getSecondCurrency().getName(), projectName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_EXCHANGE_ADD, new Object[]{null}, IConstant.NOTIFICATION_SETTING_EXCHANGE_ADD);
				
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (exchange.getProjectId().equals(IConstant.NOM_EXCHANGE_FORM_PROJECT_SELECT_ORG_OPTION)) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_ADD_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_ORG_ADD_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							Project project = BLProject.getInstance().get(exchange.getProjectId(), true);
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_EXCHANGE_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_ADD_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName(), project.getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_EXCHANGE_FOR_PROJECT_ADD_MESSAGE, new Object[] {BLCurrency.getInstance().getAll(exchange.getFirstCurrencyId()).getName(), BLCurrency.getInstance().getAll(exchange.getSecondCurrencyId()).getName(), project.getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} else {
				mav.addObject(EXISTING_EXCHANGE_ID, oldExchange.getExchangeId());
			}
		} catch (BusinessException be) {
			logger.error("Exception while adding exchange " , be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("Exception while adding exchange " , e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		//add the command object
		mav.addObject(getCommandName(), exchange);
		logger.debug("handleAdd - END");
		return mav;
	}
	
	/**
	 * Adds to model required nomenclators and data
	 * @author Coni
	 */
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
		logger.debug("referenceData - start");
		Map model = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						
		try {
			// adding to model the action from the request
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			model.put(IConstant.REQ_ACTION, action);
			
			//adding the projectId
			Integer projectId = ServletRequestUtils.getIntParameter(request, "projectId");
			model.put(PROJECT_ID, projectId);
			
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
				model.put(ORG_CURRENCIES, nomCurrencies);
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
				projects = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), false);
			} else {
				projects = BLProject.getInstance().getProjectsByManager(userAuth.getPersonId(), true, false);
			}		
			model.put(USER_PROJECTS, projects);
			if (projects != null && projects.size() > 0) {
				model.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, true);
			} else {
				model.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, false);
			}
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(GET_USER_PROJECTS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - end");
		return model;
	}
	
	/**
	 * Sends the notification when the exchange is added or updated
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
	
	public void sendNotificationExchange(Integer projectId,Integer projectDetailId, Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects, Byte setting ) {
		logger.debug("sendNotificationExchange - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
						
		try{										
			if(projectId != null && projectId != -1) {
				
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
		logger.debug("sendNotificationExchange - END");
	}
}
