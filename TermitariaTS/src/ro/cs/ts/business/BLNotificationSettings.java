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
package ro.cs.ts.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.entity.NotificationSetting;
import ro.cs.ts.entity.NotificationSettingsFormBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoNotificationSetting;

public class BLNotificationSettings extends BusinessLogic {
	
	//singleton implementation
    private static BLNotificationSettings theInstance = null;
  
    private BLNotificationSettings(){};
    static {
        theInstance = new BLNotificationSettings();
    }
    public static BLNotificationSettings getInstance() {
    	return theInstance;
    }
    
    private static IDaoNotificationSetting notificationSettingDao = DaoBeanFactory.getInstance().getDaoNotificationSetting();
    
    private static Map<Byte, Byte> pmDefaultSettings = buildPmDefaultSettings();
    private static Map<Byte, Byte> permissionDefaultSettings = buildPermissionDefaultSettings();
    private static Map<Byte, Byte> orgDefaultSettings = buildOrgDefaultSettings();
    
    
    /**
     * Returns the status for a speciffic setting, if the status is not yet set it returns the default value
     * @author alexandru.dobre
     */
	public Boolean getStatusForSetting(Integer projectDetailId, Integer userId, Integer organizationId, byte setting, boolean isPm) throws BusinessException{
		logger.debug("getStatusForSetting START");
		
		try {
			Byte b = notificationSettingDao.getStatusForSetting(projectDetailId, userId, organizationId, setting);
			logger.debug("getStatusForSetting END");
			
			if (b== null){
				logger.debug("No setting found returning default");
				if (isPm){
					return pmDefaultSettings.get(setting) == 1? true:false;
				}else if (projectDetailId ==null){
					return orgDefaultSettings.get(setting) == 1? true:false;
				}else {
					return permissionDefaultSettings.get(setting) == 1? true:false;
				}
				
			}else if(b== IConstant.NOTIFICATION_SETTING_TRUE){
				return true;
			}else if(b== IConstant.NOTIFICATION_SETTING_FALSE){
				return false;
			}
			return false;
			
		}catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_GET_STATUS_FOR_SETTING, e);
		}
		
	}
		
	
    
    /**
     * Returns the notification language code for the desired user  
     * if the user has not yet set a preference it returns the default language
     * @param userId the id of the user
     * @return the language code in uppercase letters
     */
    
    public String getUserLanguage (Integer userId)throws BusinessException {
    	logger.debug("getUserLanguage START");
    	
    	try{
    		
    		NotificationSetting ns = notificationSettingDao.getLanguage(userId);
    		if (ns!= null){
    			return languageSettingToString(ns.getSetting());
    		}else {
    			return addDefaultLanguage(userId);
    		}
    	}catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_LANGUAGE_GET, e);
		}
    	
    }
    
    /**
     * Looks at the language constants and translates the byte value to a language code 
     * 
     * @param setting
     * @return the language code or null if no code is found
     */
    
    private String languageSettingToString (byte setting){
    	switch (setting) {
		case IConstant.NOTIFICATION_LANGUAGE_RO: return "RO";
		case IConstant.NOTIFICATION_LANGUAGE_EN: return "EN";	
		default: return null;		
		}
    }
    
    /**
     * Transforms the language code into it-s byte value
     * @param language
     * @return the byte value corresponding to the language code
     */
    
    private byte languageStringToSetting (String language){
    	
    	if (language.equals("RO")) return IConstant.NOTIFICATION_LANGUAGE_RO;
    	if (language.equals("EN")) return IConstant.NOTIFICATION_LANGUAGE_EN;
    	return 0;
    }
    
    /**
     * Updates the user language for notifications
     * @param userId the id of the user
     * @param language the language code 
     */
    
    public void updateUserLanguage (Integer userId, String language)throws BusinessException {
    	logger.debug("updateUserLanguage START");
    	language = language.trim().toUpperCase();
    	
    	NotificationSetting notifSetting = new NotificationSetting(null, null, userId, -1, languageStringToSetting(language), (byte)0);
    	
    	try{
    		notificationSettingDao.updateLanguage(notifSetting);
    	}catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_LANGUAGE_UPDATE, e);
		}
    	
    	logger.debug("updateUserLanguage END");
    }
    
    /**
     * Adds the user language for notifications
     * @param userId the id of the user
     * @param language the language code 
     */
    
    public void addUserLanguage (Integer userId, String language)throws BusinessException {
    	logger.debug("addUserLanguage START");
    	language = language.trim().toUpperCase();
    	
    	NotificationSetting notifSetting = new NotificationSetting(null, null, userId, -1, languageStringToSetting(language), (byte)0);
    	try{
    		notificationSettingDao.add(notifSetting);
    	}catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_LANGUAGE_ADD, e);
		}
    	
    	logger.debug("addUserLanguage END");
    }
    
    /**
     * Adds the default language as the user language for notifications
     * @param userId the id of the user
     * @return the default language that has been added
     */
    
    public String addDefaultLanguage (Integer userId)throws BusinessException {
    	
    	addUserLanguage(userId, "RO");
    	
    	return "RO";
    }
    
    
    
    /**
     * Checks if there are saved settings in the database for the specific parameters
     * @param projectDetailId
     * @param userId
     * @param organizationId
     * @return
     * @throws BusinessException
     */
    public boolean hasSettings (Integer projectDetailId,Integer userId, Integer organizationId ) throws BusinessException {
    	
    	logger.debug("hasSettings - START");
		boolean hasSettings = false;
		try {
			hasSettings = notificationSettingDao.hasSettings(projectDetailId, userId, organizationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_SETTINGS_HAS_SETTINGS, e);
		}
		
		logger.debug("hasSettings - END");
    	return hasSettings;
    }
    
    
    /**
     * Adds the default settings for a user (all true) 
     * The method is used when a user has not yet set his notification preferences.
     * If projectDetailId then the settings are for the organization 
     * 
     * @param projectDetailId
     * @param userId
     * @param organizationId
     * @return
     * @throws BusinessException
     */
    public void addDefaultSettings (Integer projectDetailId,Integer userId, Integer organizationId, boolean isPm ) throws BusinessException {
    	logger.debug("addDefaultSettings - START");
    	
    	List<NotificationSetting> defaultNotificationSettingList = new ArrayList<NotificationSetting>();
    	
    	Map<Byte,Byte> settingsMap = null;
    	if (isPm ){
    		settingsMap = pmDefaultSettings;
    	}else if (projectDetailId == null){
    		settingsMap = orgDefaultSettings;
    	}else {
    		settingsMap = permissionDefaultSettings;
    	}
    	
    	for (Entry<Byte,Byte> e:settingsMap.entrySet()){
    		defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
        			e.getKey(), e.getValue()));
    	}
    	
    	
    	/*
    	defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
    			IConstant.NOTIFICATION_SETTING_RECORD_ADD, IConstant.NOTIFICATION_SETTING_TRUE));
    	defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
    			IConstant.NOTIFICATION_SETTING_RECORD_DELETE, IConstant.NOTIFICATION_SETTING_TRUE));
    	defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
    			IConstant.NOTIFICATION_SETTING_RECORD_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE));
    	defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
    			IConstant.NOTIFICATION_SETTING_COST_ADD, IConstant.NOTIFICATION_SETTING_TRUE));
    	defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
    			IConstant.NOTIFICATION_SETTING_COST_DELETE, IConstant.NOTIFICATION_SETTING_TRUE));
    	defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
    			IConstant.NOTIFICATION_SETTING_COST_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE));
    	
    	if (projectDetailId != null){
    		//for a project 
    		defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
        			IConstant.NOTIFICATION_SETTING_TEAM_MEMBER_DETAIL_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE));
    		defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
        			IConstant.NOTIFICATION_SETTING_BUDGET_OVERFLOW, IConstant.NOTIFICATION_SETTING_TRUE));
    		defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
        			IConstant.NOTIFICATION_SETTING_PERCENTAGE_OVERFLOW, IConstant.NOTIFICATION_SETTING_TRUE));
    	}else {
    		//for the organization
    		defaultNotificationSettingList.add(new NotificationSetting(null,projectDetailId,userId,organizationId,
        			IConstant.NOTIFICATION_SETTING_PERSON_DETAIL_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE));
    	}
    	*/
    	
    	try {
			 notificationSettingDao.addAll(defaultNotificationSettingList);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_SETTINGS_ADD_DEFAULT, e);
		}
    	
    	logger.debug("addDefaultSettings - END");
    }
    
    /**
     * Updates the status for the settings in a project
     * @param formBean
     */
    
   public void updateSettings (NotificationSettingsFormBean formBean)throws BusinessException {
	   logger.debug("updateSettings - END");
	   List<NotificationSetting> nsl = new ArrayList<NotificationSetting>();
	   
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   			IConstant.NOTIFICATION_SETTING_RECORD_ADD,(byte)(formBean.isRecordAdd()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   			IConstant.NOTIFICATION_SETTING_RECORD_DELETE, (byte)(formBean.isRecordDelete()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   			IConstant.NOTIFICATION_SETTING_RECORD_UPDATE, (byte)(formBean.isRecordUpdate()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   			IConstant.NOTIFICATION_SETTING_COST_ADD, (byte)(formBean.isCostAdd()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   			IConstant.NOTIFICATION_SETTING_COST_DELETE, (byte)(formBean.isCostDelete()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   			IConstant.NOTIFICATION_SETTING_COST_UPDATE, (byte)(formBean.isCostUpdate()?1:0)));
	   
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
	   		IConstant.NOTIFICATION_SETTING_ACTIVITY_ADD, (byte)(formBean.isActivityAdd()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
	   		IConstant.NOTIFICATION_SETTING_ACTIVITY_DELETE, (byte)(formBean.isActivityDelete()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
	   		IConstant.NOTIFICATION_SETTING_ACTIVITY_UPDATE, (byte)(formBean.isActivityUpdate()?1:0)));
	 
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
		   	IConstant.NOTIFICATION_SETTING_EXCHANGE_ADD, (byte)(formBean.isExchangeAdd()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
		   	IConstant.NOTIFICATION_SETTING_EXCHANGE_DELETE, (byte)(formBean.isExchangeDelete()?1:0)));
	   nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
		   	IConstant.NOTIFICATION_SETTING_EXCHANGE_UPDATE, (byte)(formBean.isExchangeUpdate()?1:0)));
	   
   	
   		if (formBean.getProjectDetailId() != null){
   			//for a project 
   			nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
       			IConstant.NOTIFICATION_SETTING_TEAM_MEMBER_DETAIL_UPDATE, (byte)(formBean.isTeamMemberDetailUpdate()?1:0)));
   			nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
       			IConstant.NOTIFICATION_SETTING_BUDGET_OVERFLOW, (byte)(formBean.isBudgetOverflow()?1:0)));
   			nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
       			IConstant.NOTIFICATION_SETTING_PERCENTAGE_OVERFLOW, (byte)(formBean.isPercentageOverflow()?1:0)));
   			nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   	       		IConstant.NOTIFICATION_SETTING_PROJECT_DETAIL_UPDATE, (byte)(formBean.isProjectDetailUpdate()?1:0)));
   		}else {
   			//for the organization
   			nsl.add(new NotificationSetting(null,formBean.getProjectDetailId(),formBean.getUserId(),formBean.getOrganizationId(),
   	       			IConstant.NOTIFICATION_SETTING_PERSON_DETAIL_UPDATE, (byte)(formBean.isPersonDetailUpdate()?1:0)));
   		}
   		
   		try {
			 notificationSettingDao.updateStatusAll(nsl);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_SETTINGS_UPDATE_SETTINGS, e);
		}
		logger.debug("updateSettings - END");
	   
   }
    
   /**
    * Populates the form bean with the settings from the database based 
    * on the ProjectDetailId, userId and organizationId
    * @param projectDetailId
    * @param userId
    * @param organizationId
    * @return
    * @throws BusinessException
    */
    public void getNotificationSettings(NotificationSettingsFormBean formBean) throws BusinessException{
    	logger.debug("getNotificationSettings - START");
    	
    	try {
    		List <NotificationSetting> nsl = notificationSettingDao.getSettings(formBean.getProjectDetailId(), formBean.getUserId(), formBean.getOrganizationId());
    		HashMap<Byte, Byte> statusMap = new HashMap<Byte, Byte>();
    		for (NotificationSetting ns:nsl){
    			statusMap.put(ns.getSetting(), ns.getStatus());
    		}
    		
    		//setting the status
    		formBean.setRecordAdd(statusMap.get(IConstant.NOTIFICATION_SETTING_RECORD_ADD)==1?true:false);
    		formBean.setRecordDelete(statusMap.get(IConstant.NOTIFICATION_SETTING_RECORD_DELETE)==1?true:false);
    		formBean.setRecordUpdate(statusMap.get(IConstant.NOTIFICATION_SETTING_RECORD_UPDATE)==1?true:false);
    		formBean.setCostAdd(statusMap.get(IConstant.NOTIFICATION_SETTING_COST_ADD)==1?true:false);
    		formBean.setCostDelete(statusMap.get(IConstant.NOTIFICATION_SETTING_COST_DELETE)==1?true:false);
    		formBean.setCostUpdate(statusMap.get(IConstant.NOTIFICATION_SETTING_COST_UPDATE)==1?true:false);
    		
    		formBean.setActivityAdd(statusMap.get(IConstant.NOTIFICATION_SETTING_ACTIVITY_ADD)==1?true:false);
    		formBean.setActivityDelete(statusMap.get(IConstant.NOTIFICATION_SETTING_ACTIVITY_DELETE)==1?true:false);
    		formBean.setActivityUpdate(statusMap.get(IConstant.NOTIFICATION_SETTING_ACTIVITY_UPDATE)==1?true:false);
    		
    		formBean.setExchangeAdd(statusMap.get(IConstant.NOTIFICATION_SETTING_EXCHANGE_ADD)==1?true:false);
    		formBean.setExchangeDelete(statusMap.get(IConstant.NOTIFICATION_SETTING_EXCHANGE_DELETE)==1?true:false);
    		formBean.setExchangeUpdate(statusMap.get(IConstant.NOTIFICATION_SETTING_EXCHANGE_UPDATE)==1?true:false);
    		
    		if (formBean.getProjectDetailId() != null){
    			formBean.setTeamMemberDetailUpdate(statusMap.get(IConstant.NOTIFICATION_SETTING_TEAM_MEMBER_DETAIL_UPDATE)==1?true:false);	
    			formBean.setBudgetOverflow(statusMap.get(IConstant.NOTIFICATION_SETTING_BUDGET_OVERFLOW)==1?true:false);
    			formBean.setPercentageOverflow(statusMap.get(IConstant.NOTIFICATION_SETTING_PERCENTAGE_OVERFLOW)==1?true:false);
    			formBean.setProjectDetailUpdate(statusMap.get(IConstant.NOTIFICATION_SETTING_PROJECT_DETAIL_UPDATE)==1?true:false);	
    		}else {
    			formBean.setPersonDetailUpdate(statusMap.get(IConstant.NOTIFICATION_SETTING_PERSON_DETAIL_UPDATE)==1?true:false);
    		}
    	
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_SETTINGS_GET, e);
		}
    	
    	 
    	
    	logger.debug("getNotificationSettings - END"); 	
    }
    
    /**
     * Builds the default notification settings for a project manager
     * @return
     */
    private	static Map<Byte,Byte> buildPmDefaultSettings(){
    	Map<Byte,Byte> defaultMap = new TreeMap<Byte, Byte> ();
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_TEAM_MEMBER_DETAIL_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_BUDGET_OVERFLOW, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_PERCENTAGE_OVERFLOW, IConstant.NOTIFICATION_SETTING_TRUE);
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_PROJECT_DETAIL_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	  	
    	return defaultMap;
    }
    
    /**
     * Builds the default notification settings for a user with permission 
     * to receive notifications
     * @return
     */
    private	static Map<Byte,Byte> buildPermissionDefaultSettings(){
    	Map<Byte,Byte> defaultMap = new TreeMap<Byte, Byte> ();
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_ADD, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_DELETE, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_UPDATE, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_ADD, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_DELETE, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_UPDATE, IConstant.NOTIFICATION_SETTING_FALSE);
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_TEAM_MEMBER_DETAIL_UPDATE, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_BUDGET_OVERFLOW, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_PERCENTAGE_OVERFLOW, IConstant.NOTIFICATION_SETTING_FALSE);
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_ADD, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_DELETE, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_UPDATE, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_ADD, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_DELETE, IConstant.NOTIFICATION_SETTING_FALSE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_UPDATE, IConstant.NOTIFICATION_SETTING_FALSE);
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_PROJECT_DETAIL_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	 
    	  	
    	return defaultMap;
    }
    
    /**
     * Builds the default notification settings for events that are for the 
     * organization and not from a project
     * @return
     */
    private	static Map<Byte,Byte> buildOrgDefaultSettings(){
    	Map<Byte,Byte> defaultMap = new TreeMap<Byte, Byte> ();
    	
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_RECORD_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_COST_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
 
		defaultMap.put(IConstant.NOTIFICATION_SETTING_PERSON_DETAIL_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
		
		defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_ACTIVITY_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_ADD, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_DELETE, IConstant.NOTIFICATION_SETTING_TRUE);
    	defaultMap.put(IConstant.NOTIFICATION_SETTING_EXCHANGE_UPDATE, IConstant.NOTIFICATION_SETTING_TRUE);
    	
    	return defaultMap;
    	
    }
    

}
