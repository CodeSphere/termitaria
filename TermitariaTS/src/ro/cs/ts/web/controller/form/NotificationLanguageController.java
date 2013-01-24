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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLNotificationSettings;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.NotificationLanguageBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;

public class NotificationLanguageController extends RootSimpleFormController{
	
	private static final String FORM_VIEW 							= "Notification_Language";
	private static final String SUCCESS_VIEW 						= "Notification_Language";
	private static final String NOTIFICATION_LANGUAGE_BEAN 	= "notificationLanguageBean";
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String GENERAL_ERROR				= "notification.language.general.error";	
	private static final String GET_ERROR					= "notification.langugage.get.error";
	private static final String UPDATE_SUCCESS				= "notification.langugage.update.success";
	private static final String UPDATE_ERROR				= "notification.language.update.error";	
	
	//------------------------MODEL--------------------------------------------------------------------
	
	
	public NotificationLanguageController (){
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(NOTIFICATION_LANGUAGE_BEAN);
		setCommandClass(NotificationLanguageBean.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		NotificationLanguageBean formBean = new NotificationLanguageBean();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
	
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		formBean.setUserId(userAuth.getPersonId());
		
		formBean.setLanguage(handleGetLanguage(request, userAuth, errorMessages));
	
		logger.debug("formBackingObject - END");
		return formBean;
	}
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
	    	
	    logger.debug("referenceData - START");   
	    	
	    Map map = new HashMap();

		//adding supported languages
		map.put(IConstant.NOM_SUPPORTED_LANGUAGES, TSContext.getFromContext(IConstant.NOM_SUPPORTED_LANGUAGES));
										
		logger.debug("referenceData - END");
	    return map;
	 }
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		
		// for model and view
		ModelAndView mav = new ModelAndView(getSuccessView());
		//for locale
		Locale locale = RequestContextUtils.getLocale(request);
		
		//used as a container for info/error messages
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		
		NotificationLanguageBean formBean = (NotificationLanguageBean)command;
		
		mav = handleUpdate(request, response, formBean, errors, errorMessages, infoMessages, locale);
		
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		//Add referenceData objects to model
		mav.addAllObjects(referenceData(request, command, errors));
		logger.debug("onSubmit - END");
		return mav;
	}
	
	private String handleGetLanguage (HttpServletRequest request,UserAuth userAuth, ArrayList<String> errorMessages){
		logger.debug("handleGetLanguage START");
		try{
			String language = BLNotificationSettings.getInstance().getUserLanguage(userAuth.getPersonId());
			if (language == null || language.isEmpty()){
				language = BLNotificationSettings.getInstance().addDefaultLanguage(userAuth.getPersonId());
			}
			logger.debug("handleGetLanguage END return: "+language);
			return language;
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		logger.debug("handleGetLanguage END");
		return "";
	}
	
	/**
	 * Updates the language
	 * 
	 * @author alexandru.dobre
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
	
	private ModelAndView handleUpdate(HttpServletRequest request, HttpServletResponse response, NotificationLanguageBean formBean, BindException errors, ArrayList<String> errorMessages, ArrayList<String> infoMessages, Locale locale) throws Exception {
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.addObject(getCommandName(), formBean);
		
		try {
			
			BLNotificationSettings.getInstance().updateUserLanguage(formBean.getUserId(), formBean.getLanguage());
			infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS,null,locale));
			
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

}
