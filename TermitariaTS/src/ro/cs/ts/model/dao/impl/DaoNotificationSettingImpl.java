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
package ro.cs.ts.model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.NotificationSetting;
import ro.cs.ts.model.dao.IDaoNotificationSetting;


/**
 * Dao class for NotificationSetting Entity
 * 
 * @author alexandru.dobre
 *
 */

public class DaoNotificationSettingImpl extends HibernateDaoSupport implements IDaoNotificationSetting {
	
	public void add(NotificationSetting notificationSetting){
		logger.debug("add - START");
		
		getHibernateTemplate().save(IModelConstant.notificationSettingEntity, notificationSetting);
		
		logger.debug("add - END");
	}
	
	public void delete(int notificationSettingId) {
		logger.debug("delete START - Deleting notificationSetting id:".concat(String
				.valueOf(notificationSettingId)));
		
		NotificationSetting notificationSetting = new NotificationSetting();
		notificationSetting.setNotificationSettingsId(notificationSettingId);
		// delete the notification
		getHibernateTemplate().delete(IModelConstant.notificationSettingEntity,
				notificationSetting);
	
		logger.debug("delete END");
	}
	
	/**
	 * Deletes all the settings for a speciffic user	
	 * @param userId
	 * @author alexandru.dobre
	 */
	public void deleteAllForUser(int userId){
		logger.debug("deleteAll - START - receiverId:".concat(String.valueOf(userId)));	
		
		String hquery = "delete from ".concat(IModelConstant.notificationSettingEntity).concat(" where userId=").concat(Integer.toString(userId));
		logger.debug("HQL: ".concat(hquery));
		Session session = getHibernateTemplate().getSessionFactory().openSession();
			session.createQuery(hquery).executeUpdate();
			session.close();
		
		logger.debug("deleteAll END");	
	}
	
	/**
	 * Updates a notification setting
	 * 
	 * @author alexandru.dobre
	 * 
	 * @param notificationSetting
	 */
	public void update(NotificationSetting notificationSetting) {
		logger.debug("update - START");
		
		getHibernateTemplate().update(IModelConstant.notificationSettingEntity, notificationSetting);
		
		logger.debug("update - END");
	}
	
	/**
	 * Used for updating a language (that is stored with OrganizationId = -1)
	 * @param notifSetting
	 */
	public void updateLanguage (NotificationSetting notifSetting){
		logger.debug("updateLanguage - START");
		String hql = "update ".concat(IModelConstant.notificationSettingEntity)
			.concat(" set setting=").concat(""+notifSetting.getSetting()).concat(" where ")
			.concat(" userId=").concat(""+notifSetting.getUserId()).concat(" and ")
			.concat(" organizationId=").concat(""+notifSetting.getOrganizationId());
			
		logger.debug("HQL: "+hql);
		int updatedNr = getHibernateTemplate().bulkUpdate(hql);
		logger.debug("updateLanguage - END updatedNr: "+updatedNr);
	}
	
	/**
	 * Updates the status for all the settings in the list to the database based on the project detailId, the userId and the OrganizationId
	 * @param notifSettingList
	 */
	public void updateStatusAll (List <NotificationSetting> notifSettingList){
		logger.debug("updateStatusAll - START");
		if (notifSettingList == null )return;
		
		for (NotificationSetting notifSetting : notifSettingList){		
				String hql = "update ".concat(IModelConstant.notificationSettingEntity)
								.concat(" set status=").concat(""+notifSetting.getStatus()).concat(" where ")
								.concat(" setting=").concat(""+notifSetting.getSetting()).concat(" and ")
								.concat(" userId=").concat(""+notifSetting.getUserId()).concat(" and ")
								.concat(" organizationId=").concat(""+notifSetting.getOrganizationId()).concat(" and ")
								.concat(" projectDetailId").concat(notifSetting.getProjectDetailId()==null?" is null":"="+notifSetting.getProjectDetailId());
				logger.debug("HQL: "+hql);
				int updatedNr = getHibernateTemplate().bulkUpdate(hql);
				logger.debug("Updated NR: "+updatedNr);							
		}
					
		logger.debug("updateStatusAll - END");
	}
	
	/**
	 * Adds all the settings in the list to the database
	 * @param notifSettingList
	 */
	public void addAll (List <NotificationSetting> notifSettingList){
		logger.debug("addAll - START");
		if (notifSettingList == null )return;
		
		for (NotificationSetting notifSetting : notifSettingList){		
			getHibernateTemplate().save(IModelConstant.notificationSettingEntity,notifSetting);		
		}
		
		logger.debug("addAll - END");
	}
	
	
	/**
     * Checks if there are saved settings in the database for the specific parameters
     * @author alexandru.dobre
     */
	public boolean hasSettings(Integer projectDetailId, Integer userId, Integer organizationId) {
		
		logger.debug("hasSettings - START");
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.notificationSettingEntity);
		
		if (projectDetailId != null) {
			dcCount.add(Restrictions.eq("projectDetailId", projectDetailId));
		}else {
			dcCount.add(Restrictions.isNull("projectDetailId"));
		}
		
		if (userId != null) {
			dcCount.add(Restrictions.eq("userId", userId));
		}
		
		if (organizationId != null) {
			dcCount.add(Restrictions.eq("organizationId", organizationId));
		}
		
		dcCount.setProjection(Projections.countDistinct("notificationSettingsId"));
		
		int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
		
		logger.debug("hasSettings - END results: "+nbrOfResults);
		if (nbrOfResults > 0 )return true;
		return false;
			
	}
	
	/**
     * Returns a list of settings for the specified parameters
     * @author alexandru.dobre
     */
	public List<NotificationSetting> getSettings(Integer projectDetailId, Integer userId, Integer organizationId) {
		
		logger.debug("getSettings - START");
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.notificationSettingEntity);
		
		if (projectDetailId != null) {
			dcCount.add(Restrictions.eq("projectDetailId", projectDetailId));
		}else {
			dcCount.add(Restrictions.isNull("projectDetailId"));
		}
		
		if (userId != null) {
			dcCount.add(Restrictions.eq("userId", userId));
		}
		
		if (organizationId != null) {
			dcCount.add(Restrictions.eq("organizationId", organizationId));
		}
		
		List<NotificationSetting> nsl= getHibernateTemplate().findByCriteria(dcCount,0,0);
		
		logger.debug("getSettings - END result size: "+nsl.size());
		
		return nsl;
		
		
	}
	
	/**
     * Returns the status for a speciffic setting or null if it has not been set yet
     * @author alexandru.dobre
     */
	public Byte getStatusForSetting(Integer projectDetailId, Integer userId, Integer organizationId, byte setting) {
		
		logger.debug("getStatusForSetting - START");
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.notificationSettingEntity);
		
		if (projectDetailId != null) {
			dcCount.add(Restrictions.eq("projectDetailId", projectDetailId));
		}else {
			dcCount.add(Restrictions.isNull("projectDetailId"));
		}
		
		if (userId != null) {
			dcCount.add(Restrictions.eq("userId", userId));
		}
		
		if (organizationId != null) {
			dcCount.add(Restrictions.eq("organizationId", organizationId));
		}
		
		if (organizationId != null) {
			dcCount.add(Restrictions.eq("setting", setting));
		}
		
		List<NotificationSetting> nsl= getHibernateTemplate().findByCriteria(dcCount,0,0);
		
		logger.debug("getStatusForSetting - END result size: "+nsl.size());
		
		if (nsl.size()>0){
			return new Byte(nsl.get(0).getStatus());
		}else {
			return null;
		}		
		
	}
	
	/**
     * Returns the language setting for the specified user or null if one is not yet set
     * @author alexandru.dobre
     * 
     */
	public NotificationSetting getLanguage(Integer userId) {
		
		logger.debug("getLanguage - START");
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.notificationSettingEntity);
				
		dcCount.add(Restrictions.eq("userId", userId));
		

		dcCount.add(Restrictions.eq("organizationId", -1));
		
		
		List<NotificationSetting> nsl= getHibernateTemplate().findByCriteria(dcCount,0,0);
		
		logger.debug("getSettings - END result size: "+nsl.size());
		
		if (nsl.size()>0){
			return nsl.get(0);
		}else {
			return null;
		}
		
		
	}

}
