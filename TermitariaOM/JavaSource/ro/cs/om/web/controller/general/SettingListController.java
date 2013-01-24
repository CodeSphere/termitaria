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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLLocalization;
import ro.cs.om.business.BLSetting;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Localization;
import ro.cs.om.entity.Setting;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootAbstractController;
import ro.cs.om.web.security.UserAuth;



/**
 * Used for displaying a view
 * 
 * @author mitziuro
 */
public class SettingListController extends RootAbstractController{
	
	private static final String FORM_VIEW 		= "SettingListing";
	private static final String SETTINGS  		= "SETTINGS";
	private static final String ACTION    		= "action";
	private static final String RESET    		= "reset";
	
	private static final String  RESET_ERROR 	= "setting.resetSetting.error";
	private static final String  RESET_MESSAGE 	= "setting.resetSetting.message";
	
	private static final String  RESET_REDIRECT	= "RESET_REDIRECT";

	/**
	 * Contstructor - Has the basic settings for this controller.
	 */
	public SettingListController(){
		setView(FORM_VIEW);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
logger.debug("handleRequestInternal - START");
		
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView();		
		
		Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		
		Locale local = RequestContextUtils.getLocale(request);
		List <Setting> settings = null;
		try{
			//when we have reset action
			if(request.getParameter(ACTION) != null) {
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
				
				// ****************** Security *******************************
				if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_ChangeSettings())){
					reset(organisationId);
					handleApplyTheme(request, response);
				} else {
					errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
				}
				
			//after redirect from MainController
			} else {
				if(request.getParameter(RESET) != null) {
					infoMessages.add(messageSource.getMessage(RESET_MESSAGE, null, local));
				}
			}
			
			//put the localization 
			settings = BLSetting.getInstance().getByOrganisationId(organisationId);
		
			Localization localization = null; 
		
			for(Setting setting : settings){
				String locale = RequestContextUtils.getLocale(request).getLanguage();
				localization = BLLocalization.getInstance().getByLocale(setting.getLocalizationId(), locale.toUpperCase());

				setting.setDescription(ControllerUtils.getInstance().getDescriptionForLocale(localization, locale));
				
			}
		
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(RESET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, local));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(RESET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, local));
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		mav.addObject(SETTINGS, settings);
		return mav;
       
    }
	/**
	 * 
	 * Reset all the settings 
	 * @author mitziuro
	 * @param orgId
	 * @throws BusinessException
	 */
	private void reset(Integer orgId) throws BusinessException{

		List<Setting> settings = BLSetting.getInstance().getByOrganisationId(orgId);
		for(Setting setting : settings){
			String defaultValue = BLSetting.getInstance().getDefaultValue(setting.getParameter());
			setting.setValue(defaultValue);
			BLSetting.getInstance().update(setting);
		}
	}
	
	/**
	 * 
	 * Redirect in case of reset
	 * @author mitziuro
	 *
	 * @param request
	 * @param response
	 * @param value
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private boolean handleApplyTheme(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//Setting the new theme in table SETTINGS
			Integer organisationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
			
			//Apply theme
			userAuth.setThemeCode(BLSetting.getInstance().getSettingValue(organisationId, IConstant.SETTING_THEME));
			try {
				response.setContentType("text/html");
				response.getWriter().write("<body>\n<!-- Declare empty body to work on IE7 -->\n</body>\n");
				response.getWriter().write("<SCRIPT>\nwindow.location = '" + IConstant.APP_START_PAGE + "?" + RESET_REDIRECT + "=1';\n </SCRIPT>");
				
				logger.debug("Am pus pe response");
			} catch (IOException e) {
				logger.error("", e);
			} finally {
				response.getWriter().close();
			}
			
			return true;
		} 
}
