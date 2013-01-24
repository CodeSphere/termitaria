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

import java.util.Date;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.ts.entity.Notification;
import ro.cs.ts.entity.SearchNotificationBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.web.security.UserAuth;

/**
 * @author alu
 * 7 Jan 2010
 */

public class BLNotification extends BusinessLogic{
	
	//singleton implementation
    private static BLNotification theInstance = null;
  
    private BLNotification(){};
    static {
        theInstance = new BLNotification();
    }
    public static BLNotification getInstance() {
    	return theInstance;
    }
    
    /**
	 * Adds the given notification
	 *
	 * @author alu
	 * @param notificaiton
     * @throws BusinessException 
	 */
	public void add(Notification notificaiton) throws BusinessException{
		logger.debug("add - START");
		
		try {
			DaoBeanFactory.getInstance().getDaoNotification().add(notificaiton);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_ADD, e);
		}
		logger.debug("add - END");
	}
    
    /**
	 * Returns the notifications that correspond with the search and pagination criterion
	 *
	 * @author alu
	 * @param searchNotificationBean
	 * @param isDeleteAction
	 * @return
     * @throws BusinessException 
	 */
    public List<Notification> getNotificationsFromSearch(SearchNotificationBean searchNotificationBean, boolean isDeleteAction) throws BusinessException {
		logger.debug("getNotificationsFromSearch - START");
		
		List<Notification> notifications = null;
		try {
			//we need the user for it's id in order to bring only the notifications that are intended for the current user
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//set the receiver
			searchNotificationBean.setReceiverId(userAuth.getPersonId());
			
			notifications = DaoBeanFactory.getInstance().getDaoNotification().getNotificationsFromSearch(searchNotificationBean, isDeleteAction);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.NOTIFICATION_SEARCH, e);
		}
		
		logger.debug("getNotificationsFromSearch - END");
		return notifications;
	}
    
    /**
	 * Deletes a notification identified by notificationId
	 *
	 * @author alu
	 * @param notificationId
     * @throws BusinessException 
	 */
    public void delete(List<Integer> notificationIds) throws BusinessException{
    	logger.debug("delete BEGIN ");
    	
    	try {
    		for(Integer id: notificationIds){
    			DaoBeanFactory.getInstance().getDaoNotification().delete(id);
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.NOTIFICATION_DELETE, e);
		}
    	logger.debug("delete END ");
    }
    
	/**
	 * Deletes all notification that have the given receiverId
	 *
	 * @author alu
	 * @param receiverId
	 * @throws BusinessException 
	 */
    public void deleteAll(int receiverId) throws BusinessException {
    	logger.debug("deleteAll BEGIN ");
    	try {
    		DaoBeanFactory.getInstance().getDaoNotification().deleteAll(receiverId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.NOTIFICATION_DELETE_ALL, e);
    	}
    	logger.debug("delete END ");
    }
    
    /**
     * Send through email the given notification to the receiver mail 
     *
     * @author alu
     * 
     * @param notification
     * @param receiverMail
     * @throws BusinessException
     */
    public void sendNotification(Notification notification, String receiverMail) throws BusinessException{
    	logger.debug("sendNotification BEGIN");
    	try {
    		DaoBeanFactory.getInstance().getDaoNotification().sendNotification(notification, receiverMail);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.NOTIFICATION_SEND, e);
    	}
    	logger.debug("sendNotification END");
    }
    
    /**
     * Adds and sends the notification to the receiver
     *
     * @author alu
     * 
     * @param notification
     */
    public void addAndSendNotification(Notification notification, String receiverMail){
    	logger.debug("addAndSendNotification BEGIN");
    	try {
    		// add the issued date
    		notification.setIssuedDate(new Date());
    		// add the notification
    		BLNotification.getInstance().add(notification);
    		// send the notification
			BLNotification.getInstance().sendNotification(notification, receiverMail);
    	} catch (BusinessException be){
    		//don't throw the exception, just log it
    		logger.error("Exception at adding and sending notification!", be);
    	}
    	logger.debug("addAndSendNotification END");
    }
    
}
