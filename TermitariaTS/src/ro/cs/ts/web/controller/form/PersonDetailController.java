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
import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class PersonDetailController extends RootSimpleFormController {
	
	private static final String SUCCESS_VIEW 				= "PersonDetail";
	private static final String FORM_VIEW 					= "PersonDetail";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY 					= "person.detail.";	
	private final String ADD_ERROR							= ROOT_KEY.concat("add.error");
	private final String GET_ERROR							= ROOT_KEY.concat("get.error");		
	private final String UPDATE_SUCCESS						= ROOT_KEY.concat("update.success");
	private final String UPDATE_ERROR						= ROOT_KEY.concat("update.error");
	private final String GET_ORG_CURRENCIES_ERROR			= ROOT_KEY.concat("get.org.currencies.error");
	private final String GENERAL_ERROR						= ROOT_KEY.concat("general.error");			
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private final String PERSON_ID							= "personId";	
	private final String PROJECT_ID							= "PROJECT_ID";
	private final String HAS_PERSON_DETAIL					= "hasPersonDetail";		
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------
	private final String MODEL_PERSON_NAME					= "PERSON_NAME";	
	private final static String ORG_CURRENCIES				= "ORG_CURRENCIES";
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 				= "BACK_URL";
	private static final String ENCODE_BACK_URL	 		= "ENCODE_BACK_URL";
	
	public PersonDetailController() {
		setCommandName("personDetailBean");
		setCommandClass(PersonDetail.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * It runs at every cycle
	 * 
	 * @author Adelina
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
				
		// used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		// locale 
		Locale locale = RequestContextUtils.getLocale(request);
		
		// backing object
		PersonDetail personDetail = new PersonDetail();		
						
		try{
			// backing object id
			Integer personId = ServletRequestUtils.getIntParameter(request, PERSON_ID);					
			logger.debug("personId = ".concat(String.valueOf(personId)));
			
			Boolean hasPersonDetail = ServletRequestUtils.getBooleanParameter(request, HAS_PERSON_DETAIL);
			logger.debug("hasPersonDetail = ".concat(String.valueOf(hasPersonDetail)));	
									
			// redirecting regarding the operation
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			logger.debug("action = ".concat(action));
									
			if(action != null) {
				// check if i have to edit a person
				if(IConstant.CMD_GET.equals(action)) {
					logger.debug("formBackingObject: Get");									
					personDetail = handleGet(personId, hasPersonDetail, request, errorMessages, infoMessages, locale);					
				} 
			}
		} catch (Exception e) {
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject END command ");
		
		return personDetail;
	}
	
	/**
	 * It runs on every submit (request Method = POST)
	 * It detects the case by action parameter and dispatch to and appropriate handler.
	 * 
	 * @author Adelina
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		
		// for model and view
		ModelAndView mav = new ModelAndView();
		//for locale
		Locale locale = RequestContextUtils.getLocale(request);
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		// the command class
		PersonDetail personDetail = (PersonDetail) command;
		logger.debug("personDetail = " + personDetail);		
		
		// redirecting regarding the operation
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		logger.debug("action = ".concat(action));
		
		if(action != null) {
			if(IConstant.CMD_SAVE.equals(action)) {
				if(personDetail.getPersonDetailId() > 0) {
					mav = handleUpdate(request, response, command, errors, errorMessages, infoMessages, locale);
				} 
			} 
		}
				
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		//Add referenceData objects to model
		mav.addAllObjects(referenceData(request, command, errors));
		
		logger.debug("onSubmit - END");
		
		return mav;
	}
	

	/**
	 * Adds the person details
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	private Integer handleAdd(Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleAdd - START");
				
		Integer personDetailId = null;
									
		try{	
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					
			// if the user has permission TS_PersonDetailAddUpdate, can add the project's team member's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_PersonDetailAddUpdate())){
				PersonDetail personDetail = handleGetNew(personId);
				// adding the person details to the database
				personDetailId = BLPersonDetail.getInstance().add(personDetail);										
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
									        	
		logger.debug("handleAdd - END");
		return personDetailId;
	}
	
	/**
	 * Updates the person details
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleUpdate - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
									
		try{
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();		
			
			// the organization id from the session
			Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
									
			//****************** Security *******************************			
			// if the user has permission TS_PersonDetailAddUpdate, can update the project's team member's details
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_PersonDetailAddUpdate())){
				logger.debug("asdfasdfasdfasf");
				PersonDetail personDetail = (PersonDetail) command;
				mav.addObject(getCommandName(), personDetail);				
				
				// updating the team member details to the database			
				BLPersonDetail.getInstance().update(personDetail);
															
				UserSimple user = handleGetPerson(personDetail.getPersonId(), request, errorMessages);
																	
				String personName = ControllerUtils.getInstance().tokenizeField(user.getFirstName().concat(" ").concat(user.getLastName()), IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				String fromOrganization = messageSource.getMessage(IConstant.PROJECT_FROM_ORGANIZATION, new Object[]{null}, new Locale("ro"));
				
				// send the notification regarding the update of the person's details
				sendNotificationPersonDetailUpdate(organizationId, 
						IConstant.NOTIFICATION_MESSAGE_PERSON_DETAIL_UPDATE, 
						new Object[]{personName, fromOrganization, userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())}, 
						IConstant.NOTIFICATION_SUBJECT_PERSON_DETAIL_UPDATE, new Object[]{null});
				
				//sendNotificationPersonDetailUpdate(organizationId, messageSource.getMessage(IConstant.NOTIFICATION_MESSAGE_PERSON_DETAIL_UPDATE, new Object[]{personName, fromOrganization}, new Locale("ro")), messageSource.getMessage(IConstant.NOTIFICATION_SUBJECT_PERSON_DETAIL_UPDATE, new Object[]{null}, new Locale("ro")));
				
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] {personName}, locale));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_PERSON_DETAIL_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_DETAIL_UPDATE_MESSAGE, new Object[] {personName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_PERSON_DETAIL_UPDATE_MESSAGE, new Object[] {personName}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleUpdate - END");
		
		return mav;
	}
	
	
	/**
	 * Gets the details for a person
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 * @param locale
	 * @return
	 */
	private PersonDetail handleGet(Integer personId,  Boolean hasPersonDetail, HttpServletRequest request, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) {
		logger.debug("handleGet - START");
						
		if(!hasPersonDetail) {			
			logger.debug("add the person detail");
			handleAdd(personId, request, errorMessages);
		}
		
		PersonDetail personDetail = null;			
		
		try{
			// call the get method from the business layer
			personDetail = BLPersonDetail.getInstance().getByPersonId(personId);						
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleGet - END");
		return personDetail;
	}	
	
	/**
	 * Gets a new person detail
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	private PersonDetail handleGetNew(Integer personId) {
		logger.debug("handleGetNew - START");
		
		PersonDetail personDetail = new PersonDetail();
		personDetail.setPersonId(personId);
		personDetail.setStatus(IConstant.NOM_PERSON_DETAIL_STATUS_ACTIVE);
			
		logger.debug("handleGetNew - END");
		return personDetail;
	}	
	
	
	/**
	 * Get a person, identified by the personId
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	private UserSimple handleGetPerson(Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleGetPerson - START");
					
		String[] personIds = new String[1];
		personIds[0] = personId.toString();
		
		UserSimple user = new UserSimple();
											
		try{
			user = BLUser.getInstance().getUsersByPersonId(personIds).get(0);
			logger.debug("user = " + user);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		logger.debug("handleGetPerson - END, with person = " + user);
		return user;		
	}
	
	
	
					
	/*/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		logger.debug("referenceData - START");
		
		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Map map = new HashMap();	
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// backing object id
		Integer personId = ServletRequestUtils.getIntParameter(request, PERSON_ID);
		logger.debug("personId = " + personId);		
		
		//adding the projectId
		Integer projectId = ServletRequestUtils.getIntParameter(request, "projectId");
		map.put(PROJECT_ID, projectId);
								
		// adding to model the action from the request
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		map.put(IConstant.REQ_ACTION, action);		
						
		//put the back url
		String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);			
		logger.debug("BACK_URL = " + backUrl);		
		
		map.put(BACK_URL, backUrl);				
		map.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));
		
		//TeamMember teamMember = handleGetTeamMember(personId, request, errorMessages);
		UserSimple user = handleGetPerson(personId, request, errorMessages);
		
		// adding to model the person's name
		if(user != null && user.getUserId() > 0) {
			map.put(MODEL_PERSON_NAME, user.getFirstName().concat(" ").concat(user.getLastName()));
		}
						
		// adding to model the time unit nomenclator
		map.put(IConstant.NOM_TIME_UNIT, TSContext.getFromContext(IConstant.NOM_TIME_UNIT));
		
		// add the available currencies
		try{
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			if(currencies != null && currencies.size() > 0) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for(Currency currency : currencies) {
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
							
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * Send the notification when the person detail is updated
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 */
	public void sendNotificationPersonDetailUpdate(Integer organizationId, String messageKey,Object[] messageObjects , String subjectKey, Object[] subjectObjects ) {
		logger.debug("sendNotificationPersonUpdate - START");
						
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
		
		try{
															
			// I have to send a notification to the users, that have the permission TS_NotificationReceive
			Set<UserSimple> users = OMWebServiceClient.getInstance().getPersonsFromRole(PermissionConstant.getTheInstance().getTS_NotificationReceive(), organizationId);
			logger.debug("users = " + users);
			if(users != null && users.size() > 0) {							
				for(UserSimple user : users) {
					userIdsMap.put(String.valueOf(user.getUserId()), false);
				}
			}
											
			Thread thread = new Thread(new NotificationThread(null,userIdsMap, organizationId , messageKey, messageObjects, subjectKey, subjectObjects,IConstant.NOTIFICATION_SETTING_PERSON_DETAIL_UPDATE,messageSource));
			thread.start();	
			
		}catch (Exception e) {
			logger.error(e);
		}
		
		logger.debug("sendNotificationPersonUpdate - END");
	}
		
}



