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
package ro.cs.ts.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ApplicationObjectSupport;

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLNotification;
import ro.cs.ts.business.BLNotificationSettings;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.entity.Notification;
import ro.cs.ts.ws.client.om.entity.UserSimple;

/**
 * @author alu
 * 18 Feb 2010
 */

public class NotificationThread extends ApplicationObjectSupport implements Runnable {
	
	private Integer projectDetailId = null;
	private Map<String, Boolean> userIdsMap = null;
	private Integer organizationId = null;
	private String messageKey = null;	
	private String subjectKey = null;
	private Object[] messageObjects = null;
	private Object[] subjectObjects = null;
	private Byte setting = null;
	private MessageSource messageSource= null;
	
	
	public NotificationThread(Integer projectDetailId,Map<String,Boolean> userIdsMap,Integer organizationId ,String messageKey,Object[] messageObjects,String subjectKey, Object[] subjectObjects,Byte setting, MessageSource messageSource) throws Exception{		
		
		this.projectDetailId = projectDetailId;
		this.userIdsMap = userIdsMap;
		this.organizationId = organizationId;
		this.messageKey = messageKey;	
		this.subjectKey = subjectKey;
		this.messageObjects = messageObjects;
		this.subjectObjects = subjectObjects;
		this.setting = setting;
		this.messageSource = messageSource;
	}
	
	public void run() {			
		logger.debug("Add and send notifications BEGIN");
		try {
			
			//0. Filter users with setting
			List<String> idList = new ArrayList<String>();
			for (Entry<String, Boolean> e:userIdsMap.entrySet()){
				if (BLNotificationSettings.getInstance().getStatusForSetting(projectDetailId, new Integer(e.getKey()), organizationId, setting, e.getValue())){
					idList.add(e.getKey());
					logger.debug("Added userId:"+e.getKey());
				}
			}
			
			if (idList.isEmpty()){
				logger.debug("Add and send notifications END - user list empty");
				return;
			}
			
			//1. Get all users that have to receive this notification
			List<UserSimple> users = BLUser.getInstance().getUsersByPersonId(idList.toArray(new String[idList.size()]));
			
			Locale userLocale = null;
			Notification userNotification = null;
			int i;
			Object[] filteredMessageObjects = messageObjects;
			Object[] filteredSubjectObjects = subjectObjects;
			//2. For every user, add and send the notification
			for(UserSimple us : users) {
				//2.1 get user language 
				userLocale = new Locale(BLNotificationSettings.getInstance().getUserLanguage(us.getUserId()));
				
				//2.2 Check for keys in objects
				i=0;
				for (Object o:messageObjects ){
					if (o instanceof String && ((String)o).startsWith(IConstant.KEY)){
						filteredMessageObjects[i] = messageSource.getMessage(
								((String)o).substring(IConstant.KEY.length()), new Object[]{null}, userLocale); 
					}
					i++;
				}
				i=0;
				for (Object o:subjectObjects ){
					if (o instanceof String && ((String)o).startsWith(IConstant.KEY)){
						filteredSubjectObjects[i] = messageSource.getMessage(
								((String)o).substring(IConstant.KEY.length()), new Object[]{null}, userLocale); 
					}
					i++;
				}
				
				//2.3 build notification messages
				userNotification = new Notification(messageSource.getMessage(messageKey,filteredMessageObjects , userLocale),
													messageSource.getMessage(subjectKey,filteredSubjectObjects , userLocale));
				userNotification.setReceiverId(us.getUserId());
				BLNotification.getInstance().addAndSendNotification(userNotification, us.getEmail());
			}	
							
		} catch(Exception e) {
			logger.error("Exception at adding and sending notifications!", e);
		}
		logger.debug("Add and send notifications END");
	} 
	
	
}
