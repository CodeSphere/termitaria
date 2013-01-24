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
package ro.cs.ts.model.dao;

import java.util.List;

import ro.cs.ts.entity.NotificationSetting;

/**
 * Dao Interface, implemented by DaoNotificationSettingImpl
 * @author alexandru.dobre
 *
 */

public interface  IDaoNotificationSetting {
	
	public void add(NotificationSetting notificationSetting);
	
	public void delete(int notificationSettingId);
	
	/**
	 * Deletes all the settings for a speciffic user	
	 * @param userId
	 * @author alexandru.dobre
	 */
	public void deleteAllForUser(int userId);
	
	/**
	 * Updates a notification setting
	 * 
	 * @author alexandru.dobre
	 * 
	 * @param notificationSetting
	 */
	public void update(NotificationSetting notificationSetting) ;
	
	/**
	 * Adds all the settings in the list to the database
	 * @param notifSettingList
	 */
	public void addAll (List <NotificationSetting> notifSettingList);
	
	
	/**
     * Checks if there are saved settings in the database for the specific parameters
     * @author alexandru.dobre
     */
	public boolean hasSettings (Integer projectDetailId,Integer userId, Integer organizationId );
	
	/**
	 * Updates the status for all the settings in the list to the database based on the project detailId, the userId and the OrganizationId
	 * @param notifSettingList
	 */
	public void updateStatusAll (List <NotificationSetting> notifSettingList);
	
	/**
     * Returns a list of settings for the specified parameters
     * @author alexandru.dobre
     */
	public List<NotificationSetting> getSettings(Integer projectDetailId, Integer userId, Integer organizationId);
	
	/**
	 * Used for updating a language (that is stored with OrganizationId = -1)
	 * @param notifSetting
	 */
	public void updateLanguage (NotificationSetting notifSetting);
	
	/**
     * Returns the language setting for the specified user or null if one is not yet set
     * @author alexandru.dobre
     * 
     */
	public NotificationSetting getLanguage(Integer userId);

	/**
     * Returns the status for a speciffic setting or null if it has not been set yet
     * @author alexandru.dobre
     */
	public Byte getStatusForSetting(Integer projectDetailId, Integer userId, Integer organizationId, byte setting);
		
	

}
