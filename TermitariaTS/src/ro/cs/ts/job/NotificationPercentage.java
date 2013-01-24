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
package ro.cs.ts.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

/**
 * @author alu
 * @author Adelina
 * 26 Feb 2010
 */

public class NotificationPercentage extends QuartzJobBean {
	
	/** Logger that is available to subclasses */
	private final Log logger = LogFactory.getLog(getClass());
	
	protected MessageSource messageSource = (MessageSource) TSContext.getApplicationContext().getBean("messageSource"); 
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.debug("J O B: NotificationPercentage START");
		
		Float projectBillingPrice;				
		
		try {
									
			// for every opened project
			for (ProjectDetails projectDetails:  BLProjectDetails.getInstance().getAllOpened()) {
				logger.debug("notification status = " + projectDetails.getNotificationStatus());
												
				try{
					Float budget = projectDetails.getBudget();
					logger.debug("budget = " + budget);
					Short notificationPercentage = projectDetails.getNotificationPercentage();
					logger.debug("notificationPercentage = " + notificationPercentage);
															
					// get the project identified by it's projectId
					Project project = BLProject.getInstance().getSimpleProject(projectDetails.getProjectId());				
					logger.debug("project = " + project);
					
					Integer organizationId = project.getOrganizationId();
					logger.debug("organizationId = " + organizationId);
					
					// calculate the billing price
					projectBillingPrice = BLProjectDetails.getInstance().calculateProjectBillingPrice(projectDetails.getProjectDetailId(), projectDetails.getBudgetCurrencyId());
					logger.debug("projectBillingPrice = " + projectBillingPrice);	
					
					// first, check to see if a notification regarding notificationPercentage was already sent
					if ((projectDetails.getNotificationStatus() == null) || (projectDetails.getNotificationStatus() == IConstant.NOM_PROJECT_NOTIFICATION_STATUS_NOT_SENT)) { 
						// second, check to see if I have a budget and if the notification percentage is set, so i can send notification for notification
						if (budget != null && budget > 0 && notificationPercentage != null && notificationPercentage > 0) {
							sendNotificationPercentage(projectBillingPrice, budget, notificationPercentage, project.getManagerId(), projectDetails.getProjectDetailId(), organizationId, project.getName());																					
						}
					}
					
					// second, check to see if a notification regarding budget overflow was already sent
					if((projectDetails.getNotificationStatus() != null) && (projectDetails.getNotificationStatus() != IConstant.NOM_PROJECT_NOTIFICATION_STATUS_BUDGET_OVERFLOW)) {
						// check to see if i have a budget, so i can send notification for budget overflow
						if(budget != null && budget > 0) {
							sendNotificationForBudgetOverflow(projectBillingPrice, budget, project.getManagerId(), projectDetails.getProjectDetailId(), organizationId, project.getName());
						}	
					}
					
				} catch (Exception e) {
					logger.debug("ERROR AT PROJECT");
				}
			}						
													
		} catch (Exception e) {
			
		}
			
		logger.debug("J O B: NotificationPercentage END");
		
	}
	
	/**
	 * Send notification when the billings price for a specific projects reaches notificationPercentage(%) of the entire budget
	 * 
	 * @author Adelina
	 * 
	 * @param projectBillingPrice
	 * @param budget
	 * @param notificationPercentage
	 * @param managerId
	 * @param projectDetailId
	 */
	public void sendNotificationPercentage(Float projectBillingPrice, Float budget, Short notificationPercentage, Integer managerId, Integer projectDetailId, Integer organizationId, String projectName) {
		logger.debug("sendNotificationPercentage - START");
		
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
		
		try{
			logger.debug("projectBillingPrice = ".concat(String.valueOf(projectBillingPrice)));
			logger.debug("budget percentage = ".concat(String.valueOf(budget * notificationPercentage / 100)));			
			// check to see if I have to send a notification
			if (projectBillingPrice >= budget * notificationPercentage / 100) {
				
				logger.debug("managerId = ".concat(String.valueOf(managerId)));
				
				//1. I have to send a notification to the manager of the project											
				userIds.add(String.valueOf(managerId));
				userIdsMap.put(String.valueOf(managerId), true);
				
				// 2. I have to send a notification to the users, that have the permission TS_NotificationReceive
				Set<UserSimple> users = OMWebServiceClient.getInstance().getPersonsFromRole(PermissionConstant.getTheInstance().getTS_NotificationReceive(), organizationId);
				logger.debug("users = " + users);
				if(users != null && users.size() > 0) {					
					for(UserSimple user : users) {						
						if (userIds.add(String.valueOf(user.getUserId()))){
							userIdsMap.put(String.valueOf(user.getUserId()), false);
						}				
						
					}
				}
				
				List<String> list = new ArrayList<String>(userIds);					
				String[] notificationPercentageIds = list.toArray(new String[0]);
				logger.debug("notificationIds = " + notificationPercentageIds.length);
				
				for(int i = 0; i < notificationPercentageIds.length; i++) {
					logger.debug("notificationPercentageIds[" + i + "] = " + notificationPercentageIds[i]);
				}
				
				/*
				String notificationMessage = MessageProvider.getMessage(IConstant.NOTIFICATION_MESSAGE_NOTIFICATION_PERCENTAGE, new Locale("ro"));
				logger.debug("notificationMessage = ".concat(notificationMessage));
				notificationMessage = notificationMessage.replace(IConstant.NOTIFICATION_PROJECT_NAME, projectName);
				logger.debug("notificationMessage = ".concat(notificationMessage));
				notificationMessage = notificationMessage.replace(IConstant.NOTIFICATION_PROJECT_PERCENTAGE, String.valueOf(notificationPercentage));
				logger.debug("notificationMessage = ".concat(notificationMessage));
				
				// create and send the notification								
				Notification notification = new Notification(notificationMessage, MessageProvider.getMessage(IConstant.NOTIFICATION_SUBJECT_NOTIFICATION_PERCENTAGE, new Locale("ro")));
				*/
				Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , 
						IConstant.NOTIFICATION_MESSAGE_NOTIFICATION_PERCENTAGE, new Object[] {projectName,String.valueOf(notificationPercentage)}, 
						IConstant.NOTIFICATION_SUBJECT_NOTIFICATION_PERCENTAGE, new Object[] {null},IConstant.NOTIFICATION_SETTING_PERCENTAGE_OVERFLOW,messageSource));
				thread.start();							
				
				// mark that the percentage notification was sent for this project
				BLProjectDetails.getInstance().markSendingNotificationPercentage(projectDetailId);								
			}			
		} catch (Exception e) {
			logger.debug("sendNotificationPercentage - ERROR");
		}
		logger.debug("sendNotificationPercentage - END");
	}
	
	/**
	 * Send notification for budget overflow
	 * 
	 * @author Adelina
	 * 
	 * @param projectBillingPrice
	 * @param budget
	 * @param managerId
	 * @param projectDetailId
	 */
	public void sendNotificationForBudgetOverflow(Float projectBillingPrice, Float budget, Integer managerId, Integer projectDetailId, Integer organizationId, String projectName) {
		logger.debug("sendNotificationForBudgetOverflow - START");
		
		Set<String> userIds = new HashSet<String>();	
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
		
		logger.debug("projectBillingPrice = ".concat(String.valueOf(projectBillingPrice)));
		logger.debug("budget = ".concat(String.valueOf(budget)));
		
		try{
			if(projectBillingPrice > budget) {
				
				logger.debug("managerId = ".concat(String.valueOf(managerId)));
				// 1. I have to send a notification to the manager of the project							
				userIds.add(String.valueOf(managerId));
				userIdsMap.put(String.valueOf(managerId), true);
								
				// 2. I have to send a notification to the users, that have the permission TS_NotificationReceive
				Set<UserSimple> users = OMWebServiceClient.getInstance().getPersonsFromRole(PermissionConstant.getTheInstance().getTS_NotificationReceive(), organizationId);
				logger.debug("users = " + users);
				if(users != null && users.size() > 0) {					
					for(UserSimple user : users) {						
						if (userIds.add(String.valueOf(user.getUserId()))){
							userIdsMap.put(String.valueOf(user.getUserId()), false);
						}
					}
				}
				
				List<String> list = new ArrayList<String>(userIds);					
				String[] budgetOverflowPercentageIds = list.toArray(new String[0]);
				logger.debug("notificationIds = " + budgetOverflowPercentageIds.length);
				
				for(int i = 0; i < budgetOverflowPercentageIds.length; i++) {
					logger.debug("budgetOverflowPercentageIds[" + i + "] = " + budgetOverflowPercentageIds[i]);
				}
				/*
				String notificationMessage = MessageProvider.getMessage(IConstant.NOTIFICATION_MESSAGE_BUDGET_OVERFLOW, new Locale("ro"));
				logger.debug("notificationMessage = ".concat(notificationMessage));
				notificationMessage = notificationMessage.replace(IConstant.NOTIFICATION_PROJECT_NAME, projectName);
				logger.debug("notificationMessage = ".concat(notificationMessage));				
				
				// create the notification object				
				Notification notification = new Notification(notificationMessage, MessageProvider.getMessage(IConstant.NOTIFICATION_SUBJECT_BUDGET_OVERFLOW, new Locale("ro")));
				*/
				Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , 
						IConstant.NOTIFICATION_MESSAGE_BUDGET_OVERFLOW, new Object[] {projectName},
						IConstant.NOTIFICATION_SUBJECT_BUDGET_OVERFLOW, new Object[] {null},IConstant.NOTIFICATION_SETTING_BUDGET_OVERFLOW,messageSource));
				// start the thread
				thread.start();
				
				// mark that the percentage completeness was sent for this project
				BLProjectDetails.getInstance().markSendingBudgetOverflow(projectDetailId);
			}
		} catch (Exception e) {
			logger.debug("sendNotificationForBudgetOverflow - ERROR");
		}
		logger.debug("sendNotificationForBudgetOverflow - END");
	}
}
