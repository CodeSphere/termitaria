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
package ro.cs.om.model.dao.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Setting;
import ro.cs.om.model.dao.IDaoSetting;

/**
 * @author matti_joona
 *
 * This class implements all the model methods specifics for a setting
 */
public class DaoSettingImpl extends HibernateDaoSupport implements IDaoSetting{

	/**
	 * Add a setting
	 * 
	 * @author mitziuro
	 */
	public void add(Setting setting) {
		logger.debug("Add setting");
		getHibernateTemplate().save(IModelConstant.settingAllEntity, setting);
		logger.debug("Setting added".concat(" with id=").concat(String.valueOf(setting.getSettingId())));
	}
	
	/**
	 * Get a setting
	 * 
	 * @author mitziuro
	 */
	public Setting get(Integer settingId){
		logger.debug("Getting setting with id=".concat(String.valueOf(settingId)));
		Setting setting = (Setting) getHibernateTemplate().get(IModelConstant.settingAllEntity, new Integer(settingId));
		return setting;
	}
	
	/**
	 * Get a setting by name
	 * 
	 * @author mitziuro
	 */
	public Setting get(String name){
		logger.debug("Getting setting by name...".concat(name));
		String hquery = "from Setting where name='".concat(name).concat("'");
		List<Setting> listSettings = getHibernateTemplate().find(hquery);
		return listSettings.get(0);
	}
	
	/**
	 * 
	 * Get all the settings for an organisation
	 * @author mitziuro
	 * @param organisationId
	 * @return
	 */
	public List<Setting> getByOrganisationId(Integer organisationId){
		logger.debug("Getting settings by organisation...".concat(String.valueOf(organisationId)));
		String hquery = "from Setting where organisationid=".concat(String.valueOf(organisationId));
		List<Setting> listSettings = getHibernateTemplate().find(hquery);
		return listSettings;
	}
	
	
	/**
	 * Search for organisation's parameter setting
	 * 
	 * @author mitziuro
	 */
	private Setting get(int organisationId, String parameter){
		logger.debug("get - START :".concat(" orgId - ").concat(String.valueOf(organisationId)));		
			
		String hquery = "from ".concat(IModelConstant.settingAllEntity).concat(" where parameter='")
			.concat(parameter).concat("' and organisationid=").concat(Integer.toString(organisationId));
		List<Setting> listSettings = getHibernateTemplate().find(hquery);
		
		logger.debug("get - END results size : ".concat(String.valueOf(listSettings.size())));
		
		if (listSettings.size() > 0) {
			return listSettings.get(0);
		} else {
			return null;
		}
		
		
	}
	
	/**
	 * Get value for an organisation and for a parameter
	 * 
	 * @author mitziuro
	 */
	public String getSettingValue(int organisationId, String parameter){
		logger.debug("get - START :".concat(" orgId - ").concat(String.valueOf(organisationId)));		
		Setting setting = get(organisationId, parameter);
		return setting.getValue();
	}
	/**
	 * 
	 * Update setting
	 * @author mihai
	 *
	 * @param setting
	 */
	public void update(Setting setting){
		logger.debug("update - START :".concat(" Id - ").concat(String.valueOf(setting.getSettingId())));	
		getHibernateTemplate().update(IModelConstant.settingEntity,setting);
		logger.debug("update - END :");
	}
	/**
	 * Update the setting for a parameter
	 * 
	 * @author mitziuro
	 */
	public void updateSetting(int organisationId, String parameter, String value){
		logger.debug("updateSetting - START :".concat(" orgId - ").concat(String.valueOf(organisationId)));		
		
		Setting setting = get(organisationId, parameter);
		setting.setValue(value);
		
		getHibernateTemplate().update(IModelConstant.settingAllEntity,setting);
		
		logger.debug("updateSetting - END theme: ".concat(value));
	}

	/**
	 * Delete a list of settings
	 * 
	 * @author matti_joona
	 */
	public void delete(HashSet<Setting> settings){
		logger.debug("Deleting settings!");
		if(settings != null && settings.size() > 0) {
			StringBuffer hql = new StringBuffer();
			hql.append("DELETE FROM ");
			hql.append(IModelConstant.settingEntity);
			hql.append(" WHERE organisationId IN (");
			Iterator<Setting> it = settings.iterator();
			while(it.hasNext()){
				hql.append(it.next().getSettingId());
				hql.append(",");
			}
			//remove the last "," and add the last ")"
			String hqlQuery = hql.substring(0, hql.length()-1).concat(")");
			//execute query against database
			logger.debug("Executing query: ".concat(hqlQuery));
			Session session = getSessionFactory().openSession();
			Query query = session.createQuery(hqlQuery);
			query.executeUpdate();
			session.close();
			logger.debug("Settings has been deleted");	
		} else {
			logger.debug("Settings not present!");
		}
	}
	
	/**
	 * Delete a setting
	 * 
	 * @author mitziuro
	 */
	public void delete(Integer settingId){
		logger.debug("Deleting setting ".concat(String.valueOf(settingId)));
		Setting setting  = (Setting) getHibernateTemplate().get(IModelConstant.settingEntity, settingId);
		getHibernateTemplate().delete(IModelConstant.settingAllEntity, setting);
		logger.debug("Setting deleted");
	}
	
	/**
	 * 
	 * Get the default settings for replicate
	 * @author mitziuro
	 *
	 * @return
	 */
	public List<Setting> getDefaultSettings(){
		logger.debug("getDefaultSettings - START ");		
		
		String hquery = "from ".concat(IModelConstant.settingEntity).concat(" where status = ").concat(String.valueOf(IConstant.SETTING_STATUS_DEFAULT));
		List<Setting> listSettings = getHibernateTemplate().find(hquery);
		
		//we change the status to normal
		for (Setting setting : listSettings){
			setting.setStatus(IConstant.SETTING_STATUS_NORMAL);
		}
		
		logger.debug("get - END results size : ".concat(String.valueOf(listSettings.size())));
		return listSettings;
	}
	
	/**
	 * 
	 * Get the default value for a setting
	 * @author mitziuro
	 *
	 * @param parameter
	 * @return
	 */
	public String getDefaultValue(String parameter){
		logger.debug("getDefaultValue - START PARAMETER: ".concat(parameter));
		
		String hquery = "from ".concat(IModelConstant.settingEntity).concat(" where status = ").concat(String.valueOf(IConstant.SETTING_STATUS_DEFAULT).concat(" and parameter = '")
				.concat(parameter).concat("'"));
		List<Setting> listSettings = getHibernateTemplate().find(hquery);
		logger.debug("getDefaultValue - END");
		if(listSettings.size() > 0){
			return listSettings.get(0).getValue();
		}
		return null;
		
	}
}
