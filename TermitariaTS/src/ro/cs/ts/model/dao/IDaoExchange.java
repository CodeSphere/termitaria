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

import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.SearchExchangeBean;
import ro.cs.ts.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoExchangeImpl
 * @author Coni
 *
 */
public interface IDaoExchange {
	
	/**
	 * Returns Exchange entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 */
	public List<Exchange> getByCurrencyId(int currencyId);
	
	/**
	 * Searches for exchanges using the criterion defined in searchExchangeBean
	 * @author Coni
	 * @param searchCostSheetBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Exchange> getExchangeBeanFromSearch(SearchExchangeBean searchExchangeBean, boolean isDeleteAction) throws BusinessException;
	
	/**
	 * Deletes an exchange
	 * @author Coni
	 * @param exchangeId
	 * @return
	 */
	public Exchange delete(int exchangeId) throws Exception;
	
	/**
	 * 
	 * @author Coni
	 * @param firstCurrencyId
	 * @param secondCurrencyId
	 * @param projectDetailId
	 * @return
	 */
	public List<Exchange> getProjectExchangeByCurrencies(int firstCurrencyId, int secondCurrencyId, int projectDetailId);
	
	/**
	 * 
	 * @author Coni
	 * @param firstCurrencyId
	 * @param secondCurrencyId
	 * @param organizationId
	 * @return
	 */
	public List<Exchange> getOrganizationExchangeByCurrencies(int firstCurrencyId, int secondCurrencyId, int organizationId);
	
	/**
	 * Gets the exchange identified by the specified exchangeId
	 * @author Coni
	 * @param exchangeId
	 * @return
	 * @throws BusinessException
	 */
	public Exchange getAll(int exchangeId);
	
	/**
	 * Updates an existing exchange and its reverse exchange from a project
	 * @author Coni
	 * @param costSheet
	 */
	public void update(Exchange exchange);
	
	/**
	 * Adds a new exchange and its reverse for a project
	 * @author Coni
	 * @param exchange
	 */
	public Integer add(Exchange exchange);
	
	/**
	 * Deletes an exchange
	 * 
	 * @author Adelina
	 * 
	 * @param exchange
	 * @return
	 */
	public Exchange delete(Exchange exchange);
	
	/**
	 * Get a list of exchanges by projectDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetailId
	 * @return
	 */
	public List<Exchange> getByProjectDetailId(Integer projectDetailId);

}
