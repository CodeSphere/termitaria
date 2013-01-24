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
package ro.cs.cm.web.controller.form;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.cm.business.BLAudit;
import ro.cs.cm.business.BLClient;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.PermissionConstant;
import ro.cs.cm.context.CMContext;
import ro.cs.cm.entity.Client;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.web.controller.root.ControllerUtils;
import ro.cs.cm.web.controller.root.RootSimpleFormController;
import ro.cs.cm.web.security.UserAuth;

public class ClientFormController extends RootSimpleFormController{
	
	public static final String FORM_VIEW		= "ClientForm";
	public static final String SUCCESS_VIEW		= "ClientForm";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ID 				= "clientId";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String GET_ERROR 		= "client.get.error";
	private static final String UPDATE_MESSAGE 	= "client.update.message";
	private static final String UPDATE_ERROR 	= "client.update.error";
	private static final String ADD_MESSAGE		= "client.add.message";
	private static final String ADD_ERROR		= "client.add.error";
	private static final String GENERAL_ERROR	= "client.general.form.error";
	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String CLIENT_TYPE		= "CLIENT_TYPE";
	private final String MODEL_MONTHS 			= "Months";
	private final String MODEL_DAYS 			= "Days";
	private final String MODEL_YEARS			= "Years";
	private final String CURRENT_YEAR			= "CURRENT_YEAR";
	private final String CURRENT_MONTH			= "CURRENT_MONTH";
	private final String CURRENT_DAY			= "CURRENT_DAY";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 				= "BACK_URL";
	private static final String NEXT_BACK_URL			= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 		= "ENCODE_BACK_URL";
	
	
	
	public ClientFormController(){
		setCommandName("clientBean");
		setCommandClass(Client.class);
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));

		logger.debug("initBinder - END");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		Client client = null;
		
		try {
			//check if i have to edit the client
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION) != null &&
					IConstant.CMD_GET.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION))){
				logger.debug("formBackingObject: GET");
				
				//get the client with the specified clientId
				client = handleGet(ServletRequestUtils.getIntParameter(request, ID).intValue(), errorMessages, request);
				if (client == null){
					client = new Client();
				}
			} else {
				logger.debug("formBackingObject: ADD");
				client = new Client();
				
				//setting the organizationId for the new client
				Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
				if (organizationId != null){
					client.setOrganizationId(organizationId);
				}
			}
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("formBackingObject - END");
		return client;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);;
		Client client = (Client) command;
		
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		// check if the client has a clientId
		if (client.getClientId() != -1){
			// if I have clientId, it means that I have "update" action
			logger.debug("onSubmit - handleUpdate");
			mav =  handleUpdate(client, request, locale, errorMessages, infoMessages);
		} else {
			// if I don't have clientId, it means that I have "add" action
			logger.debug("onSubmit - handleAdd");
			mav =  handleAdd(client, request, locale, errorMessages, infoMessages);
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addAllObjects(referenceData(request, command, errors));
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * Gets a client with all its components
	 * 
	 * @author Coni
	 * @param clientId
	 * @param errorMessages
	 * @param request
	 * @return
	 */
	public Client handleGet(int clientId, ArrayList<String> errorMessages, HttpServletRequest request) {
		logger.debug("handleGet - START");
		Client client = null;
		try {
			client = BLClient.getInstance().getAll(clientId);
		} catch (BusinessException be) {
			logger.error("handleGet", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("handleGet - START");
		return client;
	}
	
	
	/**
	 * Updates the client with the specified id
	 * 
	 * @author Coni
	 * @param client
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleUpdate(Client client, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getFormView());
		try {
			//add the command object
			mav.addObject(getCommandName(), client);
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getCM_ClientUpdate())) {
				//update the client
				BLClient.getInstance().update(client);
				String clientNameMessage = null;
				if (client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					clientNameMessage = client.getC_name();
				} else if (client.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
					clientNameMessage = client.getP_firstName().concat(" ").concat(client.getP_lastName());
				}
				clientNameMessage = ControllerUtils.getInstance().tokenizeField(clientNameMessage, IConstant.NR_CHARS_PER_LINE_MESSAGE);			
				infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, new Object[] {clientNameMessage}, locale));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_CLIENT_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_CLIENT_UPDATE_MESSAGE, new Object[] {clientNameMessage}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_CLIENT_UPDATE_MESSAGE, new Object[] {clientNameMessage}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
		} catch (BusinessException be) {
			logger.error("Exception while updating client with id: ".concat(String.valueOf(client.getClientId())), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error("Exception while updating client with id: ".concat(String.valueOf(client.getClientId())), e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} 
		
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	
	/**
	 * Adds a new client
	 * 
	 * @author Coni
	 * @param client
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleAdd(Client client, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getFormView());
		
		try {
			//add the command object
			mav.addObject(getCommandName(), client);
			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getCM_ClientAdd())) {
				//setting the new client's status to enabled
				client.setStatus(IConstant.NOM_CLIENT_STATUS_ACTIVE);
				
				BLClient.getInstance().add(client);
				String clientNameMessage = null;
				if (client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
					clientNameMessage = client.getC_name();
				} else if (client.getType() == IConstant.NOM_CLIENT_TYPE_PERSON) {
					clientNameMessage = client.getP_firstName().concat(" ").concat(client.getP_lastName());
				}
				clientNameMessage = ControllerUtils.getInstance().tokenizeField(clientNameMessage, IConstant.NR_CHARS_PER_LINE_MESSAGE);
				infoMessages.add(messageSource.getMessage(ADD_MESSAGE, new Object[] {clientNameMessage}, locale));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_CLIENT_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
								messageSource.getMessage(IConstant.AUDIT_EVENT_CLIENT_ADD_MESSAGE, new Object[] {clientNameMessage}, new Locale("en")),
								messageSource.getMessage(IConstant.AUDIT_EVENT_CLIENT_ADD_MESSAGE, new Object[] {clientNameMessage}, new Locale("ro")),  
								ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
		} catch (BusinessException be) {
			logger.error("Exception while adding client" , be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("Exception while adding client" , e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("handleAdd - END");
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
		ArrayList<String> errorMessages  	= new ArrayList<String>();
						
		try {
			// adding to model the action from the request
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			model.put(IConstant.REQ_ACTION, action);
			
			Client client = (Client) command;
			
			//put the back url
			String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
			
			Date date = new Date();
			SimpleDateFormat simpleDateYearFormat = new SimpleDateFormat("yyyy");	
			SimpleDateFormat simpleDateMonthFormat = new SimpleDateFormat("MM");	
			SimpleDateFormat simpleDateDayFormat = new SimpleDateFormat("dd");
				
			if(client.getClientId() == -1) {		
				model.put(CURRENT_YEAR, Integer.valueOf(simpleDateYearFormat.format(date)));
				model.put(CURRENT_MONTH, new Integer(0));
				model.put(CURRENT_DAY, new Integer(1));
			} else if(client.getType() == IConstant.NOM_CLIENT_TYPE_PERSON){					
				model.put(CURRENT_YEAR,	Integer.valueOf(simpleDateYearFormat.format(client.getP_birthDate())));
				model.put(CURRENT_MONTH, Integer.valueOf(simpleDateMonthFormat.format(client.getP_birthDate())) - 1);
				model.put(CURRENT_DAY, Integer.valueOf(simpleDateDayFormat.format(client.getP_birthDate())));
			}
			
			String servletPath = request.getServletPath();
			String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");
			
			logger.debug("BACK_URL = " + backUrl);
			logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
			
			model.put(BACK_URL, backUrl);		
			model.put(NEXT_BACK_URL, nextBackUrl);
			model.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));		
			
			//adding to model the clients types
			model.put(CLIENT_TYPE, CMContext.getFromContext(IConstant.NOM_CLIENT_TYPE));
			
			//adding the days, months and years lists used to select a person client's birthdate
			List<List> result = ControllerUtils.getInstance().getMonthsAndDaysAndYears(request, messageSource);
	
			model.put(MODEL_DAYS, result.get(0));
			model.put(MODEL_MONTHS, result.get(1));	
			model.put(MODEL_YEARS, result.get(2));
		} catch (Exception e) {
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - end");
		return model;
	}
}
