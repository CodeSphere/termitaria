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
import java.util.List;

import org.springframework.oxm.XmlMappingException;

import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.SearchCostSheetBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.WSClientException;
import ro.cs.ts.ws.server.entity.TSReportGetDataCriteria;

/**
 * Dao Interface, implemented by DaoCostSheetImpl
 * @author Coni
 *
 */
public interface IDaoCostSheet {
	
	/**
	 * Searches for cost sheets using the criterion defined in searchCostSheetBean
	 * @author Coni
	 * @param searchCostSheetBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<CostSheet> getResultsForSearch(SearchCostSheetBean searchCostSheetBean, boolean isDeleteAction) throws BusinessException;
	
	/**
	 * Deletes a cost sheet
	 * @author Coni
	 * @param costSheetId
	 * @return
	 * @throws BusinessException
	 */
	public CostSheet delete(int costSheetId);
	
	/**
	 * Returns the newly added cost sheet id
	 * Adds a new cost sheet
	 * @author Coni
	 * @param costSheet
	 */
	public Integer add(CostSheet costSheet);
	
	/**
	 * Updates an existing cost sheet
	 * @author Coni
	 * @param costSheet
	 */
	public void update(CostSheet costSheet);
	
	/**
	 * Gets the cost sheet identified by the specified costSheetId
	 * @author Coni
	 * @param costSheetId
	 * @return
	 * @throws BusinessException
	 */
	public CostSheet getAll(int costSheetId);
	
	/**
	 * Returns CostSheet entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<CostSheet> getByCurrencyId(int currencyId);
	
	/**
	 * Deletes a cost
	 * 
	 * @author Adelina
	 * 
	 * @param cost
	 * @return
	 */
	public CostSheet deleteSimple(CostSheet cost);
	
	/**
	 * Gets the costs for the projectDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetailId
	 * @return
	 */
	public List<CostSheet> getSimpleByProjectDetailId(Integer projectDetailId);
	
	/**
	 * Gets the cost sheet identified by the specified costSheetId, for the the view
	 * 
	 * @author Adelina
	 * 
	 * @param costSheetId
	 * @return
	 */
	public CostSheet getForView(Integer costSheetId);
	
    /**
     * Gets the Project Report cost sheets
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws BusinessException
     */
    public List<CostSheet> getProjectReportCostSheets(TSReportGetDataCriteria getDataCriteria) throws XmlMappingException, IOException, WSClientException;
    
    /**
     * Returns the list of costs for the given projectDetailId
     *
     * @author alu
     * @param projectDetailId
     * @return
     */
    public List<CostSheet> getByProjectDetailId(Integer projectDetailId);
    
    /**
     * Returns the billing price of all the costs from a project in the project currency
     *
     * @author alu
     * @param projectDetailId
     * @param projectsBudgetCurrencyId
     * @return
     */
    public Float calculateCostsBillingPriceForProject(Integer projectDetailId, Integer projectsBudgetCurrencyId);

}
