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

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.SearchRecordBean;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoRecord;

/**
 * 
 * @author Coni
 *
 */
public class BLRecord extends BusinessLogic {

	//singleton implementation
	private static BLRecord theInstance = null;
	
	private BLRecord(){};
	static {
		theInstance = new BLRecord();
	}
	public static BLRecord getInstance(){
		return theInstance;
	}
	
	private static IDaoRecord recordDao = DaoBeanFactory.getInstance().getDaoRecord();
	
	/**
	 * Searches for records using the criterion defined in searchRecordBean
	 * @author Coni
	 * @param searchRecordBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Record> getResultsForSearch(SearchRecordBean searchRecordBean, boolean isDeleteAction) throws BusinessException {
		logger.debug("getResultsForSearch - START");
		List<Record> result = null;
		try {
			result = recordDao.getResultsForSearch(searchRecordBean, isDeleteAction);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ICodeException.RECORD_SEARCH, e);
		}
		logger.debug("getResultsForSearch - END");
		return result;
	}
	
	/**
	 * Deletes a record
	 * @author Coni
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record delete(int recordId) throws BusinessException {
		logger.debug("delete - START");
		Record record = null;
		try {
			record = recordDao.delete(recordId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_DELETE, e);
		}
		logger.debug("delete - END");
		return record;
	}
	
	/**
	 * Adds a new record
	 * @author Coni
	 * @param record
	 */
	public Record add(Record record) throws BusinessException {
		logger.debug("add - START");
		try {
//			handleAddDepedencies(record);
			Integer recordId = recordDao.add(record);
			record.setRecordId(recordId);
			
//			//check if it's a record for a project 
//			if (record.getProjectDetails() != null){
//				// must add the billing price to the projects billing price
//				addRecordBillingPriceToProject(record, record.getProjectDetailId());
//			}
//		} catch (BusinessException be) {
//			// it means that the exception came from adding the billing price of the record to the billing price of the project
//			// just log it, don't throw it
//			logger.error("Error at adding projects billing price!!!", be);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_ADD, e);
		}
		logger.debug("add - END");
		return record;
	}
	
	/**
	 * Updates an existing record
	 * @author Coni
	 * @param record
	 */
	public void update(Record record) throws BusinessException {
		logger.debug("update - START");
		try {
//			handleAddDepedencies(record);
			recordDao.update(record);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_UPDATE, e);
		}
		logger.debug("update - END");
	}
	
	/**
	 * Adds projectDetails, teamMemberDetail and personDetail corresponding entities for the specified record, if they don't exist
	 * @author Coni
	 * @param record
	 * @return
	 */
	public Record handleAddDepedencies(Record record) throws BusinessException {
		logger.debug("handleAddDepedencies - START");
		try {
			//if the time sheet is recorded per organization (projectId == -1), the personId prop of personDetail should be set to the selected userId, 
			//otherwise the memberId should be set to userId value ; previously, personDetail, teamMemberDetail and projectDetails 
			//entities will be created for the selected personId, memberId or projectId, if they don't exist
			if (record.getProjectId().equals(IConstant.NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION)) {
				PersonDetail personDetail = BLPersonDetail.getInstance().getByPersonId(record.getUserId());
				if (personDetail == null) {
					personDetail = new PersonDetail();
					personDetail.setPersonId(record.getUserId());
					personDetail.setStatus(IConstant.NOM_PERSON_DETAIL_STATUS_ACTIVE);
					Integer personDetailId = BLPersonDetail.getInstance().add(personDetail);
					personDetail.setPersonDetailId(personDetailId);
				}
				record.setPersonDetail(personDetail);
				record.setPersonDetailId(personDetail.getPersonDetailId());
			} else {
				ProjectDetails projectDetails = BLProjectDetails.getInstance().getByProjectId(record.getProjectId());
				if (projectDetails == null) {
					projectDetails = new ProjectDetails();
					projectDetails.setProjectId(record.getProjectId());
					projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);
					Integer projectDetailsId = BLProjectDetails.getInstance().add(projectDetails);
					projectDetails.setProjectDetailId(projectDetailsId);
				}
				record.setProjectDetails(projectDetails);
				record.setProjectDetailId(projectDetails.getProjectDetailId());
				TeamMemberDetail teamMemberDetail = BLTeamMemberDetail.getInstance().getByTeamMemberId(record.getUserId());
				if (teamMemberDetail == null) {
					teamMemberDetail = new TeamMemberDetail();
					teamMemberDetail.setTeamMemberId(record.getUserId());
					teamMemberDetail.setStatus(IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_ACTIVE);
					Integer teamMemberDetailId = BLTeamMemberDetail.getInstance().add(teamMemberDetail);
					teamMemberDetail.setTeamMemberDetailId(teamMemberDetailId);
				}
				record.setTeamMemberDetail(teamMemberDetail);
				record.setTeamMemberDetailId(teamMemberDetail.getTeamMemberDetailId());
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_HANDLE_DEPENDENCIES, e);
		}

		logger.debug("handleAddDepedencies - END");
		return record;
	}
	
	/**
	 * Gets the record identified by the specified recordId
	 * @author Coni
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record getAll(int recordId) throws BusinessException {
		logger.debug("getAll - START");
		Record record = null;
		try {
			record = recordDao.getAll(recordId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_GET_ALL, e);
		} 
		logger.debug("getAll - END");
		return record;
	}
	
	/**
	 * Gets the record identified by the specified recordId
	 * @author Andreea
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record get(int recordId) throws BusinessException {
		logger.debug("get - START");
		Record record = null;
		try {
			record = recordDao.get(recordId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_GET, e);
		} 
		logger.debug("get - END");
		return record;
	}
	
	/**
	 * Gets the record simple identified by the specified recordId
	 * 
	 * @author Adelina
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record getSimple(int recordId) throws BusinessException {
		logger.debug("getSimple - START");
		Record record = null;
		try {
			record = recordDao.getForDelete(recordId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_GET_SIMPLE, e);
		} 
		logger.debug("getSimple - END");
		return record;
	}		
	
	/**
     * Gets the records identified by projectDetailId
     * 
     * @author Adelina
     * 
     * @param projectDetailId
     * @return
	 * @throws BusinessException 
     */
    public List<Record> getSimpleByProjectDetailId(Integer projectDetailId) throws BusinessException {
    	logger.debug("getByProjectDetailId - START");
    	
		List<Record> res = null;
		try{
			res = recordDao.getSimpleByProjectDetailId(projectDetailId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_GET_BY_PROJECT_DETAIL_ID, e);
		}
		
		logger.debug("results = " + res);
				
    	logger.debug("getByProjectDetailId - END, res size = " + res.size());
    	return res;
    }     
    
    /**
     * Deletes a record, by changing the status to deleted
     * 
     * @author Adelina
     * 
     * @param record
     * @return
     * @throws BusinessException 
     */
    public Record deleteSimple(Record record) throws BusinessException {
		logger.debug("delete - START");
		try{
			record = recordDao.deleteSimple(record);
			logger.debug("Record " + record + " has been deleted");
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_DELETE, e);
		}
		logger.debug("delete  - END");
		return record;
	}
    
    /**
     * Returns the billing price of all the records from a project in the project currency
     *
     * @author alu
     * @param projectDetailId
     * @param projectsBudgetCurrencyId
     * @return
     * @throws BusinessException
     */
    public Float calculateRecordsBillingPriceForProject(Integer projectDetailId, Integer projectsBudgetCurrencyId) throws BusinessException {
		logger.debug("calculateRecordsBillingPriceForProject - START");
		
		Float totalBillingPrice = new Float(0);
		try {
			totalBillingPrice = recordDao.calculateRecordsBillingPriceForProject(projectDetailId, projectsBudgetCurrencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.RECORD_CALCULATE_BILLING_PRICE_FOR_PROJECT, e);
		}
		
		logger.debug("calculateRecordsBillingPriceForProject - END");
		return totalBillingPrice;
	}
    
//    public void addRecordBillingPriceToProject(Record record, Integer projectDetailsId) throws BusinessException{
//    	logger.debug("addRecordBillingPriceToProject - START");
//    	
//    	try {
//    		// get the billing price of the project that is on context
//    		Float projectBillingPrice = BLProjectDetails.getInstance().getProjectBillingPrice(projectDetailsId);
//    		// get the billing price of the record
//    		Float recordBillingPrice = calculateRecordsBillingPriceForProject(projectDetailsId, BLProjectDetails.getInstance().getBudgetCurrencyId(projectDetailsId));
//    		// store the new billing price for project
//    		BLProjectDetails.getInstance().storeProjectBillingPrice(projectDetailsId, projectBillingPrice + recordBillingPrice);
//    	} catch (Exception e) {
//    		throw new BusinessException(ICodeException.RECORD_ADD_RECORD_BILLING_PRICE_TO_PROJECT, e);
//		}
//    	
//    	logger.debug("addRecordBillingPriceToProject - END");
//    }
    
    /**
     * Checks if exists a record for a person, for a specific project, activity, with the same work hours or overtime hours range
     * 
     * @author Adelina
     * 
     * @param recordId
     * @param teamMemberDetailId
     * @param activityId
     * @param startTime
     * @param endTime
     * @param time
     * @param overTimeStartTime
     * @param overTimeEndTime
     * @param overtimeTime
     * @param personDetailId
     * @return
     */
    public Record hasIdenticalRecordForPerson(Integer recordId, Integer teamMemberDetailId, Integer activityId, Date startTime, Date endTime, String time, Date startOvertimeWork, Date endOvertimeWork, String overtimeTime, Integer personDetailId) throws BusinessException {
    	logger.debug("hasIdenticalRecordForPerson - START");
    	Record record = null;
    	try{
    		record = recordDao.hasIdenticalRecordForPerson(recordId, teamMemberDetailId, activityId, startTime, endTime, time, startOvertimeWork, endOvertimeWork, overtimeTime, personDetailId);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.RECORD_HAS_IDENTICAL_FOR_PERSON, e);
		}
    	
    	logger.debug("hasIdenticalRecordForPerson - END, record = " + record);    	
    	return record;
    	    	  
    }    
    	
}
