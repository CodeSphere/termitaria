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
package ro.cs.om.model.dao;

import java.util.HashSet;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.Setting;

/**
 * @author matti_joona
 *
 */
public interface IDaoSetting {

	/**
	 * Add a setting
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(Setting setting);
	
	/**
	 * Get a setting
	 * 
	 * @author matti_joona
	 */
	public Setting get(Integer settingId);

	/**
	 * Get a setting by name
	 * 
	 * @author matti_joona
	 */
	public Setting get(String name);
	
	/**
	 * Delete a list of settings
	 * 
	 * @author matti_joona
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(HashSet<Setting> settings);
	
	/**
	 * update the theme for an organisation. 
	 * 
	 * @author mitziuro
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateSetting(int organisationId, String parameter, String value);
	
	/**
	 * Get the theme for an organisation
	 * 
	 * @author mitziuro
	 */
	public String getSettingValue(int organisationId, String parameter);
	/**
	 * 
	 * Get all the settings for an organisation
	 * @author mitziuro
	 * 
	 */
	public List<Setting> getByOrganisationId(Integer organisationId);
	/**
	 * Update Setting
	 *
	 * @author mihai
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(Setting setting);
	
	/**
	 * Delete a setting by id
	 *
	 * @author mitziuro
	 *
	 * @param settingId
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(Integer settingId);
	
	/**
	 * Adds the default settings for an organisation
	 *
	 * @author mitziuro
	 *
	 * @param organisation
	 */
	public List<Setting> getDefaultSettings();
	
	/**
	 * 
	 * Get thr default value for a parameter
	 * @author mitziuro
	 *
	 * @param parameter
	 * @return
	 */
	public String getDefaultValue(String parameter);
}
	
