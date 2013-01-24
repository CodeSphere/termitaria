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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.oxm.XmlMappingException;

import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.SearchRecordBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.WSClientException;
import ro.cs.ts.ws.server.entity.TSReportGetDataCriteria;


/**
 * Dao Interface, implemented by DaoRecordImpl
 * @author Coni
 *
 */
public interface IDaoRecord {

	/**
	 * Searches for records using the criterion defined in searchRecordBean
	 * @author Coni
	 * @param searchRecordBean
	 * @param isDeleteAction
	 * @return
	 * @throws WSClientException 
	 * @throws IOException 
	 * @throws XmlMappingException 
	 * @throws BusinessException 
	 */
	public List<Record> getResultsForSearch(SearchRecordBean searchRecordBean, boolean isDeleteAction) throws XmlMappingException, IOException, WSClientException, BusinessException;
	
	/**
	 * Adds a new record
	 * @author Coni
	 * @param record
	 */
	public Integer add(Record record);
	
	/**
	 * Updates an existing record
	 * @author Coni
	 * @param record
	 */
	public void update(Record record);
	
	/**
	 * Gets the record identified by the specified recordId
	 * @author Coni
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record getAll(int recordId);
	
	/**
	 * Gets the record identified by the specified recordId
	 * @author Andreea
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record get(int recordId);
	
	/**
	 * Deletes a record
	 * @author Coni
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record delete(int recordId);
	
	
	/**
     * Gets the records identified by projectDetailId
     * 
     * @author Adelina
     * 
     * @param projectDetailId
     * @return
     */
    public List<Record> getSimpleByProjectDetailId(Integer projectDetailId);
    
    /**
     * Deletes a record, by changing the status to deleted
     * 
     * @author Adelina
     * 
     * @param record
     * @return
     */
    public Record deleteSimple(Record record);
	
    /**
     * Gets the Project Report records
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws BusinessException
     */
    public List<Record> getProjectReportRecords(TSReportGetDataCriteria getDataCriteria) throws XmlMappingException, IOException, WSClientException;
    
    /**
     * Gets the Time Sheet Report records
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws BusinessException
     */
    public List<Record> getTimeSheetReportRecords(TSReportGetDataCriteria getDataCriteria) throws XmlMappingException, IOException, WSClientException;
    
    /**
     * Returns the list of record for the given projectDetailId
     *
     * @author alu
     * @param projectDetailId
     * @return
     */
    public List<Record> getByProjectDetailId(Integer projectDetailId);
    
    /**
     * Returns the billing price of all the records from a project in the project currency
     *
     * @author alu
     * @param projectDetailId
     * @param projectsBudgetCurrencyId
     * @return
     */
    public Float calculateRecordsBillingPriceForProject(Integer projectDetailId, Integer projectsBudgetCurrencyId);
    
    /**
	 * Returns a record
	 * 
	 * @author coni
	 */
	public Record getForDelete(int recordId);
    
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
    public Record hasIdenticalRecordForPerson(Integer recordId, Integer teamMemberDetailId, Integer activityId, Date startTime, Date endTime, String time, Date overTimeStartTime, Date overTimeEndTime, String overtimeTime, Integer personDetailId);
}
