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
package ro.cs.om.business;

import java.util.List;

import ro.cs.om.entity.Setting;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoSetting;;


/**
 * Singleton which expose business methods for Setting item
 * 
 * @author matti_joona
 */
public class BLSetting extends BusinessLogic {

	//singleton implementation
    private static BLSetting theInstance = null;
    private IDaoSetting settingDao = DaoBeanFactory.getInstance().getDaoSetting();
    private BLSetting(){};
    static {
        theInstance = new BLSetting();
    }
    public static BLSetting getInstance() {
        return theInstance;
    }
    
    public void add(Setting setting){
    }
    
    public void update(Setting setting) throws BusinessException{
    	try{
    		settingDao.update(setting);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.SETTING_UPDATE, bexc);
    	}
    	
    }
    
    public void delete(int idSetting){
    }
    
    public Setting get(int idSetting) throws BusinessException{
    	Setting setting = null;
    	try{
        	setting = settingDao.get(idSetting);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.SETTING_GET, bexc);
    	}
    	return setting;
    }
    /**
     * Update setting for an organisation and a parameter with value
     *
     * @author mitziuro
     *
     * @param organisationId
     * @param value
     * @throws BusinessException
     */
    public void updateSetting(int organisationId, String parameter, String value) throws BusinessException{
    	logger.debug("updateSetting BL - START");
    	try {
    		
    	settingDao.updateSetting(organisationId, parameter, value);
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.SETTING_UPDATE, e);
		}
    	logger.debug("updateSetting BL - END");
    }
    
    /**
     * Get value for an organisation and a parameter
     *
     * @author mitziuro
     *
     * @param organisationId
     * @return
     * @throws BusinessException
     */
    public String getSettingValue(int organisationId, String parameter) throws BusinessException{
    	logger.debug("getSettingValue BL - START");
    	String setting = null;
    	try {
    		setting = settingDao.getSettingValue(organisationId, parameter);
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.SETTING_GET_VALUE, e);
		}
    	logger.debug("getSettingValue BL - END");
    	return setting;
    }
    
    /**
     * 
     * Get the setting for an organisation
     * @author mihai
     *
     * @param organisationId
     * @return
     * @throws BusinessException
     */
    public List<Setting> getByOrganisationId(Integer organisationId) throws BusinessException{
    	logger.debug("getSettingsByOrganisationId BL - START");
    	List <Setting> settings = null;
    	try {
    		settings = settingDao.getByOrganisationId(organisationId);
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.SETTING_GET_BY_ORGID, e);
		}
    	logger.debug("getSettingsByOrganisationId BL - END");
    	return settings;
    }
    
    /**
     * 
     * Get the default Settings
     * @author mitziuro
     *
     * @param organisationId
     * @return
     * @throws BusinessException
     */
    public List<Setting> getDefaultSettings() throws BusinessException{
    	logger.debug("getDefaultSettings BL - START");
    	List <Setting> settings = null;
    	try {
    		settings = settingDao.getDefaultSettings();
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.SETTING_GET_DEFAULT, e);
		}
    	logger.debug("getSettingsByOrganisationId BL - END");
    	return settings;
    }
    
    /**
     * 
     * Get the Default value for a parameter
     * @author mihai
     *
     * @param parameter
     * @return
     * @throws BusinessException
     */
    public String getDefaultValue(String parameter) throws BusinessException{
    	logger.debug("getDefaultValue BL - START PARAMETER: ".concat(parameter));
    	String value = null;
    	try {
    		 value = settingDao.getDefaultValue(parameter);
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.SETTING_GET_DEFAULT_VALUE, e);
		}
    	
    	logger.debug("getDefaultValue BL - END");
    	return value;
    }
    
}
