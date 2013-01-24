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

import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.exception.BusinessException;

/**
 * Dao Interface, implemented by DaoPersonDetailImpl
 * @author Coni
 * @author Adelina
 *
 */
public interface IDaoPersonDetail {
	
	/**
	 * Adds a new PersonDetail
	 * 
	 * @author Coni
	 * @param personDetail
	 * @throws BusinessException
	 */
	public Integer add(PersonDetail personDetail);
	
	/**
	 * Gets the PersonDetail identified by the specified personId
	 * @author Coni
	 * @param personId
	 * @return
	 */
	public PersonDetail getByPersonId(int personId);
	
	/**
	 * Returns PersonDetail entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<PersonDetail> getByCurrencyId(int currencyId);
	
		/**
	 * Updates the personDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param personDetail
	 */
	public void update(PersonDetail personDetail);
	
	/**
	 * Checks if the person has details associated
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public boolean hasPersonDetail(Integer personId);
	
	/**
	 * Gets the PersonDetail identified by the specified personId, with currencies
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public PersonDetail getByPersonWithCurrencies(int personId);
	
	/**
	 * Get with all by personDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param personDetailId
	 * @return
	 */
	public PersonDetail getWithAll(Integer personDetailId);

}
