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
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.SearchCostSheetBean;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoCostSheet;

/**
 * 
 * @author Coni
 *
 */
public class BLCostSheet extends BusinessLogic {

	//singleton implementation
	private static BLCostSheet theInstance = null;
	
	private BLCostSheet(){};
	static {
		theInstance = new BLCostSheet();
	}
	public static BLCostSheet getInstance(){
		return theInstance;
	}
	
	private static IDaoCostSheet costSheetDao = DaoBeanFactory.getInstance().getDaoCostSheet();
	
	/**
	 * Searches for cost sheets using the criterion defined in searchCostSheetBean
	 * @author Coni
	 * @param searchCostSheetBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<CostSheet> getResultsForSearch(SearchCostSheetBean searchCostSheetBean, boolean isDeleteAction) throws BusinessException {
		logger.debug("getResultsForSearch - START");
		List<CostSheet> result = null;
		try {
			result = costSheetDao.getResultsForSearch(searchCostSheetBean, isDeleteAction);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_GET_FROM_SEARCH, e);
		}
		logger.debug("getResultsForSearch - END");
		return result;
	}
	
	/**
	 * Deletes a cost sheet
	 * @author Coni
	 * @param costSheetId
	 * @return
	 * @throws BusinessException
	 */
	public CostSheet delete(int costSheetId) throws BusinessException {
		logger.debug("delete - START");
		CostSheet costSheet = null;
		try {
			costSheet = costSheetDao.delete(costSheetId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_DELETE, e);
		}
		logger.debug("delete - END");
		return costSheet;
	}
	
	/**
	 * Adds a new cost sheet
	 * @author Coni
	 * @param costSheet
	 */
	public CostSheet add(CostSheet costSheet) throws BusinessException {
		logger.debug("add - START");
		try {
			if (costSheet.getCostPriceCurrencyId().equals(-1)) {
				costSheet.setCostPriceCurrencyId(null);
			}
			if (costSheet.getBillingPriceCurrencyId().equals(-1)) {
				costSheet.setBillingPriceCurrencyId(null);
			}
			handleAddDepedencies(costSheet);
			Integer costSheetId = costSheetDao.add(costSheet);
			costSheet.setCostSheetId(costSheetId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_ADD, e);
		}
		logger.debug("add - END");
		return costSheet;
	}
	
	/**
	 * Updates an existing cost sheet
	 * @author Coni
	 * @param costSheet
	 */
	public void update(CostSheet costSheet) throws BusinessException {
		logger.debug("update - START");
		try {
			if (costSheet.getCostPriceCurrencyId().equals(-1)) {
				costSheet.setCostPriceCurrencyId(null);
			}
			if (costSheet.getBillingPriceCurrencyId().equals(-1)) {
				costSheet.setBillingPriceCurrencyId(null);
			}
			handleAddDepedencies(costSheet);
			costSheetDao.update(costSheet);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_UPDATE, e);
		}
		logger.debug("update - END");
	}
	
	/**
	 * Adds projectDetails, teamMemberDetail and personDetail corresponding entities for the specified cost sheet, if they don't exist
	 * @author Coni
	 * @param costSheet
	 * @return
	 */
	public CostSheet handleAddDepedencies(CostSheet costSheet) throws BusinessException {
		logger.debug("handleAddDepedencies - START");
		try {
			//if the cost sheet is recorded per organization (projectId == -1), the personId prop of personDetail should be set to the selected userId, 
			//otherwise the memberId should be set to userId value ; previously, personDetail, teamMemberDetail and projectDetails 
			//entities will be created for the selected personId, memberId or projectId, if they don't exist
			if (costSheet.getProjectId() == -1) {
				PersonDetail personDetail = BLPersonDetail.getInstance().getByPersonId(costSheet.getUserId());
				if (personDetail == null) {
					personDetail = new PersonDetail();
					personDetail.setPersonId(costSheet.getUserId());
					personDetail.setStatus(IConstant.NOM_PERSON_DETAIL_STATUS_ACTIVE);
					Integer personDetailId = BLPersonDetail.getInstance().add(personDetail);
					personDetail.setPersonDetailId(personDetailId);
				}
				costSheet.setPersonDetail(personDetail);
				costSheet.setPersonDetailId(personDetail.getPersonDetailId());
			} else {
				ProjectDetails projectDetails = BLProjectDetails.getInstance().getByProjectId(costSheet.getProjectId());
				if (projectDetails == null) {
					projectDetails = new ProjectDetails();
					projectDetails.setProjectId(costSheet.getProjectId());
					projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);
					Integer projectDetailsId = BLProjectDetails.getInstance().add(projectDetails);
					projectDetails.setProjectDetailId(projectDetailsId);
				}
				costSheet.setProjectDetails(projectDetails);
				costSheet.setProjectDetailId(projectDetails.getProjectDetailId());
				TeamMemberDetail teamMemberDetail = BLTeamMemberDetail.getInstance().getByTeamMemberId(costSheet.getUserId());
				if (teamMemberDetail == null) {
					teamMemberDetail = new TeamMemberDetail();
					teamMemberDetail.setTeamMemberId(costSheet.getUserId());
					teamMemberDetail.setStatus(IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_ACTIVE);
					Integer teamMemberDetailId = BLTeamMemberDetail.getInstance().add(teamMemberDetail);
					teamMemberDetail.setTeamMemberDetailId(teamMemberDetailId);
				}
				costSheet.setTeamMemberDetail(teamMemberDetail);
				costSheet.setTeamMemberDetailId(teamMemberDetail.getTeamMemberDetailId());
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_HANDLE_DEPENDENCIES, e);
		}

		logger.debug("handleAddDepedencies - END");
		return costSheet;
	}
	
	/**
	 * Gets the cost sheet identified by the specified costSheetId
	 * @author Coni
	 * @param costSheetId
	 * @return
	 * @throws BusinessException
	 */
	public CostSheet getAll(int costSheetId) throws BusinessException {
		logger.debug("getAll - START");
		CostSheet costSheet = null;
		try {
			costSheet = costSheetDao.getAll(costSheetId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_GET_ALL, e);
		} 
		logger.debug("getAll - END");
		return costSheet;
	}
	
	/**
	 * Returns CostSheet entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<CostSheet> getByCurrencyId(int currencyId) throws BusinessException {
		logger.debug("getByCurrencyId - START");
		List<CostSheet> res = null;
		try {
			res = costSheetDao.getByCurrencyId(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_GET_BY_CURRENCY_ID, e);
		}
		logger.debug("getByTeamMemberId - END");
		return res;
	}
	
	/**
	 * Deletes a cost
	 * 
	 * @author Adelina
	 * 
	 * @param cost
	 * @return
	 * @throws BusinessException 
	 */
	public CostSheet deleteSimple(CostSheet cost) throws BusinessException {
		logger.debug("deleteSimple -  START");		
		logger.debug("Deleting activity with id: ".concat(String.valueOf(cost.getCostSheetId())));
		
		try{
			cost = costSheetDao.deleteSimple(cost);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_DELETE, e);
		}
		
		logger.debug("Cost " + cost + " has been deleted");
		logger.debug("deleteSimple  - END");
		return cost;
	}	
	
	/**
	 * Gets the costs for the projectDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetailId
	 * @return
	 * @throws BusinessException 
	 */
	public List<CostSheet> getSimpleByProjectDetailId(Integer projectDetailId) throws BusinessException {
		logger.debug("getByProjectDetailId - START");
		
		List<CostSheet> res = null;
		try{
			res = costSheetDao.getSimpleByProjectDetailId(projectDetailId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_GET_BY_PROJECT_DETAIL_ID, e);
		}
		logger.debug("getByProjectDetailId - END");
		return res;
	}
	
	
	/**
	 * Gets the cost sheet identified by the specified costSheetId, for the the view
	 * 
	 * @author Adelina
	 * 
	 * @param costSheetId
	 * @return
	 * @throws BusinessException 
	 */
	public CostSheet getForView(Integer costSheetId) throws BusinessException {
		logger.debug("getForView - START");
		CostSheet costSheet = null;
		try{
			costSheet = costSheetDao.getForView(costSheetId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_GET_FOR_VIEW, e);
		}
		logger.debug("getForView - END");
		return costSheet;
	}
	
	 /**
     * Returns the billing price of all the costs from a project in the project currency
     *
     * @author alu
     * @param projectDetailId
     * @param projectsBudgetCurrencyId
     * @return
     * @throws BusinessException
     */
    public Float calculateCostsBillingPriceForProject(Integer projectDetailId, Integer projectsBudgetCurrencyId) throws BusinessException {
		logger.debug("calculateCostsBillingPriceForProject - START");
		
		Float totalBillingPrice = new Float(0);
		try {
			totalBillingPrice = costSheetDao.calculateCostsBillingPriceForProject(projectDetailId, projectsBudgetCurrencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.COST_SHEET_CALCULATE_BILLING_PRICE_FOR_PROJECT, e);
		}
		
		logger.debug("calculateCostsBillingPriceForProject - END");
		return totalBillingPrice;
	}
	
}
