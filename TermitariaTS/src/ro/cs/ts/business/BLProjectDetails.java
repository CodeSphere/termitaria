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

import java.util.List;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.Record;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoProjectDetails;


/**
 * Business Logic for the ProjectDetails entity
 * 
 * @author Coni
 * @author Adelina
 *
 */
public class BLProjectDetails extends BusinessLogic{

	//singleton implementation
	private static BLProjectDetails theInstance = null;
	
	private BLProjectDetails(){};
	static {
		theInstance = new BLProjectDetails();
	}
	public static BLProjectDetails getInstance(){
		return theInstance;
	}
	
	private static IDaoProjectDetails projectDetailsDao = DaoBeanFactory.getInstance().getDaoProjectDetails();
	
	/**
	 * Returns a ProjectDetails entity for the corresponding projectId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectDetails getByProjectId(int projectId) throws BusinessException {
		logger.debug("getByProjectId - START");
		ProjectDetails res = null;
		try {
			res = projectDetailsDao.getByProjectId(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_GET_BY_PROJECT_ID, e);
		}
		logger.debug("getByProjectId - END");
		return res;
	}
	
	/**
	 * Returns a ProjectDetails entity for the corresponding projectDetailsId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectDetails get(int projectDetailsId) throws BusinessException {
		logger.debug("get - START");
		ProjectDetails res = null;
		try {
			res = projectDetailsDao.get(projectDetailsId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_GET_BY_PROJECT_ID, e);
		}
		logger.debug("get - END");
		return res;
	}
	
		/**
	 * Checks if the project has project details associated
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public boolean hasProjectDetails(Integer projectId) throws BusinessException {
		logger.debug("hasProjectDetails - START");
		
		boolean hasProjectDetails = false;
		try{
			hasProjectDetails = projectDetailsDao.hasProjectDetails(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_FOR_PROJECT, e);
		}
				
		logger.debug("hasProjectDetails - END");
		
		return hasProjectDetails;			
	}
	
	/**
	 * Add the projectDetails to the database
	 * 
	 * @author Adelina
	 * @author alu
	 * 
	 * @param projectDetails
	 * @throws BusinessException 
	 * @return
	 */
	public Integer add (ProjectDetails projectDetails) throws BusinessException {
		logger.debug("add - START");
		Integer projectDetailId = null;
		try {
			projectDetailId = projectDetailsDao.add(projectDetails);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_ADD, e);
		}
		logger.debug("add - END");
		return projectDetailId;
	}
	
	/**
	 * Updates the projectDetails to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetails
	 * @throws BusinessException 
	 */
	public void update (ProjectDetails projectDetails) throws BusinessException {
		logger.debug("update - START");
		try{
			projectDetailsDao.update(projectDetails);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_UPDATE, e);
		}
		logger.debug("update - END");
	}	
		
	/**
	 * Deletes the ProjectDetail, identified by a projectId (update the status to deleted);
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectDetails deleteProjectDetails(Integer projectId) throws BusinessException {
		logger.debug("deleteProjectDetails - START id : ".concat(String.valueOf(projectId)));
		
		ProjectDetails projectDetails = null;
		
		try{
			projectDetails = projectDetailsDao.deleteProjectDetails(projectId);
			
			if(projectDetails != null && projectDetails.getProjectDetailId() != -1) {
				
				Integer projectDetailId = projectDetails.getProjectDetailId();
				
				// deletes all the activities that has the projectDetailId
				List<Activity> activities = BLActivity.getInstance().getByProjectDetailId(projectDetailId);
				for(Activity activity : activities) {
					BLActivity.getInstance().delete(activity);
				}
				
				// deletes all the records that has the projectDetailId
				List<Record> records = BLRecord.getInstance().getSimpleByProjectDetailId(projectDetailId);
				for(Record record : records) {
					BLRecord.getInstance().deleteSimple(record);
				}
				
				// deletes all the expenses that has the projectDetailId
				List<CostSheet> costs = BLCostSheet.getInstance().getSimpleByProjectDetailId(projectDetailId);
				for(CostSheet cost : costs) {
					BLCostSheet.getInstance().deleteSimple(cost);
				}
				
				// deletes all the exchanges that has the projectDetailId
				List<Exchange> exchanges = BLExchange.getInstance().getByProjectDetailId(projectDetailId);
				for(Exchange exchange : exchanges) {
					BLExchange.getInstance().delete(exchange);
				}
			}			
			
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_DELETE, e);
		}
		
		logger.debug("deleteProjectDetails - END");
		return projectDetails;
	}
	
	
	/**
	 * Finishes the ProjectDetails, identified by a projectId (update the status to finished)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectDetails finishProjectDetails(Integer projectId) throws BusinessException {
		logger.debug("finishProjectDetails - START id : ".concat(String.valueOf(projectId)));
		
		ProjectDetails projectDetails = null;
		
		try{
			projectDetails = projectDetailsDao.finishProjectDetails(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_FINISH, e);
		}
		
		logger.debug("finishProjectDetails - END");
		return projectDetails;
	}
	
	/**
	 * Abort the ProjectDetails, identified by a projectId (update the status to aborted)
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectDetails abortProjectDetails(Integer projectId) throws BusinessException {
		logger.debug("abortProjectDetails - START id : ".concat(String.valueOf(projectId)));
		
		ProjectDetails projectDetails = null;
		
		try{
			projectDetails = projectDetailsDao.abortProjectDetails(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_ABORT, e);
		}
		
		logger.debug("abortProjectDetails - END");
		return projectDetails;
	}		
	
	/**
	 * Open the ProjectDetails, identified by a projectId (update the status to opened)
	 * 
	 * @author Adelina
	 * @author alu
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectDetails openProjectDetails(Integer projectId) throws BusinessException {
		logger.debug("openProjectDetails - START id : ".concat(String.valueOf(projectId)));
		
		ProjectDetails projectDetails = null;
		
		try {
			projectDetails = projectDetailsDao.openProjectDetails(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_ABORT, e);
		}		
		
		logger.debug("openProjectDetails - END");
		return projectDetails;
	}	
	
	/**
	 * Returns all opened projects details
	 *
	 * @author alu
	 * @return
	 */
	public List<ProjectDetails> getAllOpened() throws BusinessException{
		logger.debug("getAll - START");
		
		List<ProjectDetails> projects = null;		
		
		try{
			projects = projectDetailsDao.getAllOpened();
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_GET_ALL_OPENED, e);
		}	
		
		logger.debug("getAll - END");
		return projects;
	}
	
	/**
	 * Returns the billing price of a project identified by projectDetailId in the project budget's currency
	 *
	 * @author alu
	 * @param projectDetailId
	 * @param projectsBudgetCurrencyId
	 * @return
	 * @throws BusinessException 
	 */
	public Float calculateProjectBillingPrice(Integer projectDetailId, Integer projectsBudgetCurrencyId) throws BusinessException{
		logger.debug("calculateProjectBillingPrice - START projectId: ".concat(String.valueOf(projectDetailId)));

		Float totalCost = new Float(0);
		try {
			//1. get all records and calculate the sum of the billing prices in the project currency
			totalCost = BLRecord.getInstance().calculateRecordsBillingPriceForProject(projectDetailId, projectsBudgetCurrencyId);
			//2. get all costs and calculate the sum of the billing prices in the project currency
			totalCost += BLCostSheet.getInstance().calculateCostsBillingPriceForProject(projectDetailId, projectsBudgetCurrencyId);
			
		} catch(Exception e) {
			throw new BusinessException(ICodeException.PROJECT_DETAILS_CALCULATE_COST, e);
		}
		
		logger.debug("calculateProjectBillingPrice - END cost: ".concat(String.valueOf(totalCost)));
		return totalCost;
	}
	
	/**
	 * Marks that the "notificationPercentage" notification was sent
	 *
	 * @author alu
	 * @param projectDetailsId
	 * @throws BusinessException
	 */
	public void markSendingNotificationPercentage(Integer projectDetailsId) throws BusinessException{
    	logger.debug("markSendingSpecialNotificationPercentage BEGIN");
    	try {
    		// create an instance of ProjectDetails and set the id
    		ProjectDetails projectDetails = new ProjectDetails();
    		projectDetails.setProjectDetailId(projectDetailsId);
    		// set the new notification status
    		projectDetails.setNotificationStatus(IConstant.NOM_PROJECT_NOTIFICATION_STATUS_NOTIFICATION_PERCENTAGE);
    		// update projectDetails
    		projectDetailsDao.updateNotificationStatus(projectDetails);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_DETAILS_MARK_SENDING_NOTIFICATION_PERCENTAGE, e);
    	}
    	logger.debug("markSendingNotificationPercentage END");
    }
	
	/**
	 * Marks that the budget is overflow notification was sent
	 *
	 * @author Adelina
	 * @param projectDetailsId
	 * @throws BusinessException
	 */
	public void markSendingBudgetOverflow(Integer projectDetailsId) throws BusinessException{
    	logger.debug("markSendingBudgetOverflow BEGIN");
    	try {
    		// create an instance of ProjectDetails and set the id
    		ProjectDetails projectDetails = new ProjectDetails();
    		projectDetails.setProjectDetailId(projectDetailsId);
    		// set the new notification status
    		projectDetails.setNotificationStatus(IConstant.NOM_PROJECT_NOTIFICATION_STATUS_BUDGET_OVERFLOW);
    		// update projectDetails
    		projectDetailsDao.updateNotificationStatus(projectDetails);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_DETAILS_MARK_SENDING_BUDGET_OVERFLOW, e);
    	}
    	logger.debug("markSendingBudgetOverflow END");
    }	
	
}
