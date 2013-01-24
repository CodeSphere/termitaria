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

import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.entity.SearchCurrencyBean;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoCurrency;

/**
 * 
 * @author Coni
 *
 */
public class BLCurrency extends BusinessLogic {

	//singleton implementation
	private static BLCurrency theInstance = null;
	
	private BLCurrency(){};
	static {
		theInstance = new BLCurrency();
	}
	public static BLCurrency getInstance(){
		return theInstance;
	}
	
	private static IDaoCurrency currencyDao = DaoBeanFactory.getInstance().getDaoCurrency();
	
	/**
	 * Gets all an organization available currencies
	 * @author Coni
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public List<Currency> getByOrganizationId(int organizationId) throws BusinessException {
		logger.debug("getByOrganizationId - START");
		List<Currency> currencies = null;
		try {
			currencies = currencyDao.getByOrganizationId(organizationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_GET_BY_ORGANIZATION_ID, e);
		}
		logger.debug("getByOrganizationId - END");
		return currencies;
	}
	
	/**
	 * Searches for currencies using the criterion defined in searchCurrencyBean
	 * @author Coni
	 * @param searchCurrencyBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Currency> getResultsForSearch(SearchCurrencyBean searchCurrencyBean, boolean isDeleteAction) throws BusinessException{
		logger.debug("getResultsForSearch - START");
		List<Currency> result = null;
		try {
			result = currencyDao.getCurrencyBeanFromSearch(searchCurrencyBean, isDeleteAction);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_GET_FROM_SEARCH, e);
		}
		logger.debug("getResultsForSearch - END");
		return result;
	}
	
	/**
	 * Deletes a currency
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException
	 */
	public Currency delete(int currencyId) throws BusinessException {
		logger.debug("delete - START");
		Currency currency = null;
		try {
			currency = currencyDao.delete(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_DELETE, e);
		}
		logger.debug("delete - END");
		return currency;
	}
	
	/**
	 * Adds a new currency
	 * @author Coni
	 * @param currency
	 */
	public Currency add(Currency currency) throws BusinessException {
		logger.debug("add - START");
		try {
			Integer currencyId = currencyDao.add(currency);
			currency.setCurrencyId(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_ADD, e);
		}
		logger.debug("add - END");
		return currency;
	}
	
	/**
	 * Updates an existing currency
	 * @author Coni
	 * @param currency
	 */
	public void update(Currency currency) throws BusinessException {
		logger.debug("update - START");
		try {
			currencyDao.update(currency);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_UPDATE, e);
		}
		logger.debug("update - END");
	}
	
	/**
	 * Gets the currency identified by the specified currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException
	 */
	public Currency getAll(int currencyId) throws BusinessException {
		logger.debug("getAll - START");
		Currency currency = null;
		try {
			currency = currencyDao.getAll(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_GET_ALL, e);
		} 
		logger.debug("getAll - END");
		return currency;
	}
	
	/**
	 * Get's a currency identified by it's name and organisation
	 * @param name
	 * @param organisationId
	 * @return the currency
	 * @throws BusinessException
	 */
	
	public Currency getByName(String name,Integer organisationId)throws BusinessException{
		logger.debug("getByName - START");
		Currency currency = null;
		try {
			currency = currencyDao.getByName(name, organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_GET_BY_NAME, e);
		} 
		logger.debug("getByName - END");
		return currency;	
	}
	
	/**
	 * Get's a currency identified by it's initials and organisation
	 * @param initials
	 * @param organisationId
	 * @return the currency
	 * @throws BusinessException
	 */
	
	public Currency getByInitials(String initials,Integer organisationId)throws BusinessException{
		logger.debug("getByInitials - START");
		Currency currency = null;
		try {
			currency = currencyDao.getByInitials(initials, organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_GET_BY_INITIALS, e);
		} 
		logger.debug("getByInitials - END");
		return currency;	
	}
	
	/**
	 * Get's a currency identified by it's name or initials in the organisation
	 * @param name
	 * @param initials
	 * @param organisationId
	 * @return the currency list
	 * @throws BusinessException
	 */
	
	public List<Currency> getByNameOrInitials(String name,String initials,Integer organisationId)throws BusinessException{
		logger.debug("getByNameOrInitials - START");
		List<Currency> currencyList = null;
		try {
			currencyList = currencyDao.getByNameOrInitials(name,initials, organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_GET_BY_NAME_OR_INITIALS, e);
		} 
		logger.debug("getByNameOrInitials - END");
		return currencyList;	
	}
	
	/**
	 * Verifies if a currency is in use in at least one project
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public boolean isUsedInProjects(int currencyId) throws BusinessException {
		logger.debug("getAll - START");
		try {
			List<TeamMemberDetail> members = BLTeamMemberDetail.getInstance().getByCurrencyId(currencyId);
			if (!members.isEmpty()) {
				return true;
			}
			List<PersonDetail> persons = BLPersonDetail.getInstance().getByCurrencyId(currencyId);
			if (!persons.isEmpty()) {
				return true;
			}
			List<CostSheet> costs = BLCostSheet.getInstance().getByCurrencyId(currencyId);
			if (!costs.isEmpty()) {
				return true;
			}
			List<Activity> activities = BLActivity.getInstance().getByCurrencyId(currencyId);
			if (!activities.isEmpty()) {
				return true;
			}
			List<Exchange> exchanges = BLExchange.getInstance().getByCurrencyId(currencyId);
			if (!exchanges.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.CURRENCY_IS_USED_IN_PROJETS, e);
		}
		logger.debug("getAll - END");
		return false;
	}
	
}
