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

import ro.cs.ts.entity.Notification;
import ro.cs.ts.entity.SearchNotificationBean;

/**
 * Dao Interface, implemented by DaoNotificationImpl
 * @author alu
 * 7 Jan 2010
 */

public interface IDaoNotification {
	
	/**
	 * Adds the given notification
	 *
	 * @author alu
	 * @param notificaiton
	 */
	public void add(Notification notificaiton);
	
	/**
	 * Returns the notifications that correspond with the search and pagination criterion
	 *
	 * @author alu
	 * @param searchNotificationBean
	 * @param isDeleteAction
	 * @return
	 */
	public List<Notification> getNotificationsFromSearch(SearchNotificationBean searchNotificationBean, boolean isDeleteAction);
	
	/**
	 * Deletes a notification identified by notificationId
	 *
	 * @author alu
	 * @param notificationId
	 */
	public void delete(int notificationId);
	
	/**
	 * Deletes all notification that have the given receiverId
	 *
	 * @author alu
	 * @param receiverId
	 */
	public void deleteAll(int receiverId);
	
	/**
	 * Sends the given notification to the receiver
	 * 
	 * @author alu
	 * 
	 * @param notification
	 * @param receiverMail
	 * @throws Exception
	 */
	public void sendNotification(Notification notification, String receiverMail) throws Exception;

}
