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
package ro.cs.om.web.controller.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLSetting;
import ro.cs.om.business.BLTheme;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Setting;
import ro.cs.om.entity.SettingNom;
import ro.cs.om.entity.Theme;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author mitziuro
 *
 */
public class SettingController extends RootSimpleFormController{
	

	private static final String FORM_VIEW		= "Setting";
	private static final String SUCCESS_VIEW	= "Setting";
	private static final String OPTIONS			= "OPTIONS";
	private static final String SELECTED		= "SEL";
	private static final String THEME_REDIRECT	= "THEME_REDIRECT";
	
	private static final String ID 				= "id";
	private static final String ID_REDIR 		= "id_redir";
	
	private static final String GET_ERROR 		= "setting.getSettingId.error";
	private static final String UPDATE_ERROR 	= "setting.updateSetting.error";
	private static final String UPDATE_MESSAGE 	= "setting.updateSetting.message";
	
	
	public SettingController(){
		setCommandName("settingBean");
		setCommandClass(Setting.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
		
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages 	= new ArrayList<String>();
		ArrayList<String> infoMessages  	= new ArrayList<String>();
		
		Setting setting = null;
		try {
			if(request.getParameter(ID) != null) {
				
				setting = handleGet(request,ID); 
				setOptions(setting.getParameter(), request);
				return setting;
			//if is redirect from theme
			} else {
				if(request.getParameter(ID_REDIR) != null) {
				
					setting = handleGet(request,ID_REDIR); 
					Locale locale = RequestContextUtils.getLocale(request);
					setOptions(setting.getParameter(),request);
					infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, null, locale));
					setMessages(request, infoMessages);
					return setting;
				}
			}
			
		} catch (BusinessException be) {
			logger.error("formBackingObject", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		
		setErrors(request, errorMessages);
		logger.debug("formBackingObject - END");
		
		return new Setting();
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ModelAndView mav = new ModelAndView();
		Setting setting = (Setting) command;
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		mav =  handleUpdate(setting, request, response, locale, errorMessages, infoMessages);
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		
		logger.debug("onSubmit - END");
		
		return mav;
	}
	
	private ModelAndView handleUpdate(Setting setting, HttpServletRequest request, HttpServletResponse response, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages){
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getFormView());	
		try{	
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			

			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ChangeSettings())){
				mav.addObject(getCommandName(), setting);
				
				BLSetting.getInstance().update(setting);
				infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, null, locale));
				
				//if we want to change the theme
				if(IConstant.SETTING_THEME.compareTo(setting.getParameter()) == 0){
					handleApplyTheme(request, response, setting.getValue(), setting.getSettingId());
				}
				
				// if I have the update action,
				setOptions(setting.getParameter(), request);
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
				
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}			
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	/**
	 * 
	 * get the setting by Id
	 * @author mitziuro
	 *
	 * @param request
	 * @param Id
	 * @return
	 * @throws ServletRequestBindingException
	 * @throws BusinessException
	 */
	private Setting handleGet(HttpServletRequest request, String Id) throws ServletRequestBindingException, BusinessException{
		logger.debug("handleGet - START");
		int settingId = Integer.parseInt(request.getParameter(Id));
		
		Setting setting = BLSetting.getInstance().get(settingId);
		
		logger.debug("Setting id = " + setting.getSettingId());
		
		logger.debug("handleGet - END");
		return setting;
	}
	
	/**
	 * 
	 * redirect in case of change theme
	 * @author mitziuro
	 *
	 * @param request
	 * @param response
	 * @param value
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private boolean handleApplyTheme(HttpServletRequest request, HttpServletResponse response, String value, Integer id) throws Exception {
		
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//Setting the new theme in table SETTINGS
			Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
			
			//Apply theme
			userAuth.setThemeCode(value);
			try {
				response.setContentType("text/html");
				response.getWriter().write("<body>\n<!-- Declare empty body to work on IE7 -->\n</body>\n");
				response.getWriter().write("<SCRIPT>\nwindow.location = '" + IConstant.APP_START_PAGE + "?" + THEME_REDIRECT + "="+ id +"';\n </SCRIPT>");
				
				logger.debug("Am pus pe response");
			} catch (IOException e) {
				logger.error("", e);
			} finally {
				response.getWriter().close();
			}
			
			return true;
		} 
	/**
	 * 
	 * set the OPTION on request
	 * @author mitziuro
	 *
	 * @param parameter
	 * @param request
	 * @throws BusinessException
	 */
	private void setOptions(String parameter, HttpServletRequest request) throws BusinessException{
		List<SettingNom> options = null;
		String selected = null;
		if(parameter.compareTo(IConstant.SETTING_THEME) == 0) {
			//put the options
			options = getThemeNomenclator();
			
			//put the selected value
			Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
			selected  = BLSetting.getInstance().getSettingValue(organisationId, parameter);
			
		} else {
			//TO DO
		}
		request.setAttribute(OPTIONS, options);
		request.setAttribute(SELECTED, selected);
	}
	
	/**
	 * create theme nomenclator
	 *
	 * @author mitziuro
	 * @return
	 * @throws BusinessException
	 */
	private List<SettingNom> getThemeNomenclator() throws BusinessException{
		List<SettingNom> nom = new ArrayList<SettingNom>();
		List<Theme> themes = null;
		SettingNom settingNom = null;
		themes = BLTheme.getInstance().list();
		for(Theme theme : themes) {
			settingNom = new SettingNom();
			settingNom.setCode(theme.getCode());
			settingNom.setValue(theme.getName());
			nom.add(settingNom);
		}
		return nom;
	}
}
