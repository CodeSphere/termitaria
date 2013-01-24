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

import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.SearchCurrencyBean;
import ro.cs.ts.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoCurrencyImpl
 * @author Coni
 *
 */
public interface IDaoCurrency {
	
	/**
	 * Gets all an organization available currencies
	 * @author Coni
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public List<Currency> getByOrganizationId(int organizationId);
	
	/**
	 * Searches for currencies using the criterion defined in searchCurrencyBean
	 * @author Coni
	 * @param searchCurrencyBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Currency> getCurrencyBeanFromSearch(SearchCurrencyBean searchCurrencyBean, boolean isDeleteAction);
	
	/**
	 * Deletes a currency
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException
	 */
	public Currency delete(int currencyId);
	
	/**
	 * Adds a new currency
	 * @author Coni
	 * @param currency
	 */
	public Integer add(Currency currency);
	
	/**
	 * Updates an existing currency
	 * @author Coni
	 * @param currency
	 */
	public void update(Currency currency);
	
	/**
	 * Gets the currency identified by the specified currencyId
	 * @author Coni
	 * @param currencyId
	 * @return the currency
	 */
	public Currency getAll(int currencyId);
	
	/**
	 * Gets the currency identified by the name and organisationId
	 * @author alexandru.dobre
	 * @param name
	 * @param currencyId
	 * @return the currency 
	 */
	public Currency getByName(String name, Integer organisationId);
	
	/**
	 * Gets the currency identified by the initials and organisationId
	 * @author alexandru.dobre
	 * @param initials 
	 * @param currencyId
	 * @return the currency 
	 */
	public Currency getByInitials(String initials, Integer organisationId);
	
	/**
	 * Gets the currencies that have either the name or the initials specified within the same organisation
	 * @param name
	 * @param intitals
	 * @param organisationId
	 * @return
	 */
	public List<Currency> getByNameOrInitials (String name, String initials,Integer organisationId);
	
}
