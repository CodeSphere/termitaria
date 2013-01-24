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
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.ProjectDetails;
import ro.cs.ts.entity.SearchExchangeBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoExchange;

public class BLExchange extends BusinessLogic {

	//singleton implementation
	private static BLExchange theInstance = null;
	
	private BLExchange(){};
	static {
		theInstance = new BLExchange();
	}
	public static BLExchange getInstance(){
		return theInstance;
	}
	
	private static IDaoExchange exchangeDao = DaoBeanFactory.getInstance().getDaoExchange();
	
	/**
	 * Returns Exchange entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Exchange> getByCurrencyId(int currencyId) throws BusinessException {
		logger.debug("getByCurrencyId - START");
		List<Exchange> res = null;
		try {
			res = exchangeDao.getByCurrencyId(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_GET_BY_CURRENCY_ID, e);
		}
		logger.debug("getByTeamMemberId - END");
		return res;
	}
	
	/**
	 * Searches for exchanges using the criterion defined in searchExchangeBean
	 * @author Coni
	 * @param searchCostSheetBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Exchange> getResultsForSearch(SearchExchangeBean searchExchangeBean, boolean isDeleteAction) throws BusinessException {
		logger.debug("getResultsForSearch - START");
		List<Exchange> result = null;
		try {
			result = exchangeDao.getExchangeBeanFromSearch(searchExchangeBean, isDeleteAction);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_GET_FROM_SEARCH, e);
		}
		logger.debug("getResultsForSearch - END");
		return result;
	}
	
	/**
	 * Deletes an exchange
	 * @author Coni
	 * @param exchangeId
	 * @return
	 * @throws BusinessException
	 */
	public Exchange delete(int exchangeId) throws BusinessException {
		logger.debug("delete - START");
		Exchange exchange = null;
		try {
			exchange = exchangeDao.delete(exchangeId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_DELETE, e);
		}
		logger.debug("delete - END");
		return exchange;
	}
	
	/**
	 * Gets the only exchange entity for a project with the specified currencies
	 * @author Coni
	 * @param firstCurrencyId
	 * @param secondCurrencyId
	 * @param projectDetailId
	 * @return
	 * @throws BusinessException 
	 */
	public Exchange getProjectExchangeByCurrencies(int firstCurrencyId, int secondCurrencyId, int projectDetailId) throws BusinessException {
		logger.debug("getProjectExchangeByCurrencies - START");
		Exchange exchange = null;
		try {
			List<Exchange> res = exchangeDao.getProjectExchangeByCurrencies(firstCurrencyId, secondCurrencyId, projectDetailId);
			if (res != null && res.size() == 1) {
				exchange = res.get(0);
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_GET_PROJECT_EXCHANGE_BY_CURRENCIES, e);
		}
		logger.debug("getProjectExchangeByCurrencies - END");
		return exchange;
	}
	
	/**
	 * Gets the only exchange entity for an organization with the specified currencies
	 * @author Coni
	 * @param firstCurrencyId
	 * @param secondCurrencyId
	 * @param projectDetailId
	 * @return
	 * @throws BusinessException 
	 */
	public Exchange getOrganizationExchangeByCurrencies(int firstCurrencyId, int secondCurrencyId, int organizationId) throws BusinessException {
		logger.debug("getOrganizationExchangeByCurrencies - START");
		Exchange exchange = null;
		try {
			List<Exchange> res = exchangeDao.getOrganizationExchangeByCurrencies(firstCurrencyId, secondCurrencyId, organizationId);
			if (res != null && res.size() == 1) {
				exchange = res.get(0);
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_GET_ORGANIZATION_EXCHANGE_BY_CURRENCIES, e);
		}
		logger.debug("getOrganizationExchangeByCurrencies - END");
		return exchange;
	}
	
	/**
	 * Gets the exchange identified by the specified exchangeId
	 * @author Coni
	 * @param exchangeId
	 * @return
	 * @throws BusinessException
	 */
	public Exchange getAll(int exchangeId) throws BusinessException {
		logger.debug("getAll - START");
		Exchange exchange = null;
		try {
			exchange = exchangeDao.getAll(exchangeId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_GET_ALL, e);
		} 
		logger.debug("getAll - END");
		return exchange;
	}
	
	/**
	 * Updates an existing exchange and its reverse exchange from a project
	 * @author Coni
	 * @param costSheet
	 */
	public void update(Exchange exchange) throws BusinessException {
		logger.debug("update - START");
		try {
			//if projectId > 0, it means it is an exchange for a project; otherwise it is for the organization
			if (exchange.getProjectId() != null && exchange.getProjectId() > 0) {
				handleAddDepedencies(exchange);
			}
			Exchange reverseExchange = null;
			if (exchange.getProjectDetail() != null) {
				reverseExchange = getProjectExchangeByCurrencies(exchange.getSecondCurrencyId(), exchange.getFirstCurrencyId(), exchange.getProjectDetail().getProjectDetailId());
			} else {
				reverseExchange = getOrganizationExchangeByCurrencies(exchange.getSecondCurrencyId(), exchange.getFirstCurrencyId(), exchange.getOrganizationId());
			}
			if (reverseExchange != null) {
				exchangeDao.update(exchange);
				
				Float reverseRate = new Float(Math.ceil((1.0/exchange.getRate()) * 10000.0)/10000.0);
				reverseExchange.setRate(reverseRate);
				exchangeDao.update(reverseExchange);
			} else {
				throw new BusinessException(ICodeException.EXCHANGE_UPDATE, new Exception());
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_UPDATE, e);
		}
		logger.debug("update - END");
	}
	
	/**
	 * Adds a new exchange and its reverse for a project
	 * @author Coni
	 * @param exchange
	 */
	public Exchange add(Exchange exchange) throws BusinessException {
		logger.debug("add - START");
		try {
			//if projectId > 0, it means it is an exchange for a project; otherwise it is for the organization
			if (exchange.getProjectId() > 0) {
				handleAddDepedencies(exchange);
			}
			Integer exchangeId = exchangeDao.add(exchange);
			exchange.setExchangeId(exchangeId);
			
			Exchange reverseExchange = new Exchange();
			reverseExchange.setOrganizationId(exchange.getOrganizationId());
			reverseExchange.setFirstCurrencyId(exchange.getSecondCurrencyId());
			reverseExchange.setSecondCurrencyId(exchange.getFirstCurrencyId());
			if (exchange.getProjectDetailId() != null) {
				reverseExchange.setProjectDetailId(exchange.getProjectDetailId());
			}
			Float reverseRate = new Float(Math.ceil((1.0/exchange.getRate()) * 10000.0)/10000.0);
			reverseExchange.setRate(reverseRate);
			reverseExchange.setStatus(IConstant.NOM_EXCHANGE_STATUS_ACTIVE);
			exchangeDao.add(reverseExchange);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_ADD, e);
		}
		logger.debug("add - END");
		return exchange;
	}
	
	/**
	 * Adds the projectDetails corresponding entity for the specified exchange, if it doesn't exist
	 * @author Coni
	 * @param exchange
	 * @return
	 */
	public Exchange handleAddDepedencies(Exchange exchange) throws BusinessException {
		logger.debug("handleAddDepedencies - START");
		try {
			ProjectDetails projectDetails = BLProjectDetails.getInstance().getByProjectId(exchange.getProjectId());
			if (projectDetails == null) {
				projectDetails = new ProjectDetails();
				projectDetails.setProjectId(exchange.getProjectId());
				projectDetails.setStatus(IConstant.NOM_PROJECT_DETAILS_STATUS_OPENED);
				Integer projectDetailsId = BLProjectDetails.getInstance().add(projectDetails);
				projectDetails.setProjectDetailId(projectDetailsId);
			}
			exchange.setProjectDetail(projectDetails);
			exchange.setProjectDetailId(projectDetails.getProjectDetailId());
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_HANDLE_DEPENDENCIES, e);
		}
		logger.debug("handleAddDepedencies - END");
		return exchange;
	}
	
	/**
	 * Deletes an exchange
	 * 
	 * @author Adelina
	 * 
	 * @param exchange
	 * @return
	 * @throws BusinessException 
	 */
	public Exchange delete(Exchange exchange) throws BusinessException {
		logger.debug("delete - START");
		logger.debug("Deleting exchange with id: ".concat(String.valueOf(exchange.getExchangeId())));
			
		try{
			exchange = exchangeDao.delete(exchange);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_DELETE, e);
		}
		
		logger.debug("Exchange " + exchange + " has been deleted");
		logger.debug("delete  - END");
		return exchange;
	}		
	
	/**
	 * Get a list of exchanges by projectDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetailId
	 * @return
	 * @throws BusinessException 
	 */
	public List<Exchange> getByProjectDetailId(Integer projectDetailId) throws BusinessException {
		logger.debug("getByProjectDetailId - START");
				
		List<Exchange> res = null;
		try{
			res = exchangeDao.getByProjectDetailId(projectDetailId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.EXCHANGE_GET_BY_PROJECT_DETAIL_ID, e);
		}		
		
		logger.debug("getByProjectDetailId - END");
		return res;
	}	
}

