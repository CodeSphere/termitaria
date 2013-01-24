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
package ro.cs.ts.entity;

public class NotificationSetting {
	
	private Integer notificationSettingsId ;
	private Integer projectDetailId;
	private Integer userId;
	private Integer organizationId;
	private byte setting;
	private byte status;
	
	public NotificationSetting (){
		notificationSettingsId = null;
		projectDetailId = null;
		userId = null;
		organizationId = null;
		setting = 0;
		status = 0;
	}
	
	public NotificationSetting (Integer notificationSettingsId ,Integer projectDetailId, Integer userId, Integer organizationId, byte setting, byte status){
		this.notificationSettingsId = notificationSettingsId;
		this.projectDetailId = projectDetailId;
		this.userId = userId;
		this.organizationId = organizationId;
		this.setting = setting;
		this.status = status;
	}
	
	public Integer getNotificationSettingsId() {
		return notificationSettingsId;
	}
	public void setNotificationSettingsId(Integer notificationSettingsId) {
		this.notificationSettingsId = notificationSettingsId;
	}
	public Integer getProjectDetailId() {
		return projectDetailId;
	}
	public void setProjectDetailId(Integer projectDetailId) {
		this.projectDetailId = projectDetailId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public byte getSetting() {
		return setting;
	}
	public void setSetting(byte setting) {
		this.setting = setting;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	
	
}
