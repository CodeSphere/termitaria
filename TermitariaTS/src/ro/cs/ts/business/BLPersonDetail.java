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

import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoPersonDetail;

public class BLPersonDetail extends BusinessLogic {

	//singleton implementation
	private static BLPersonDetail theInstance = null;
	
	private BLPersonDetail(){};
	static {
		theInstance = new BLPersonDetail();
	}
	public static BLPersonDetail getInstance(){
		return theInstance;
	}
	
	private static IDaoPersonDetail personDetailDao = DaoBeanFactory.getInstance().getDaoTeamPersonDetail();
	
	/**
	 * Adds a new PersonDetail
	 * 
	 * @author Coni
	 * @param personDetail
	 * @throws BusinessException
	 */
	public Integer add(PersonDetail personDetail) throws BusinessException {
		logger.debug("add - START");
		Integer personDetailId = null;
		try { 
			personDetailId = personDetailDao.add(personDetail);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_DETAIL_ADD, e);
		}
		logger.debug("add - END");
		return personDetailId;
	}
	
	/**
	 * Updates the personDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param personDetail
	 * @throws BusinessException 
	 */
	public void update(PersonDetail personDetail) throws BusinessException {
		logger.debug("update - START");
		try{
			personDetailDao.update(personDetail);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_DETAIL_UPDATE, e);
		}
		logger.debug("update - END");
	}	
	
	/**
	 * Gets the PersonDetail identified by the specified personId
	 * @author Coni
	 * @param personId
	 * @return
	 */
	public PersonDetail getByPersonId(int personId) throws BusinessException {
		logger.debug("getByPersonId - START");
		PersonDetail personDetail = null;
		try {
			personDetail = personDetailDao.getByPersonId(personId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_DETAIL_GET, e);
		}
		logger.debug("getByPersonId - END");
		return personDetail;
	}
	
	/**
	 * Returns PersonDetail entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<PersonDetail> getByCurrencyId(int currencyId) throws BusinessException {
		logger.debug("getByCurrencyId - START");
		List<PersonDetail> res = null;
		try {
			res = personDetailDao.getByCurrencyId(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_DETAIL_GET_BY_CURRENCY_ID, e);
		}
		logger.debug("getByTeamMemberId - END");
		return res;
	}
	
		/**
	 * Checks if the person has details associated
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 * @throws BusinessException 
	 */
	public boolean hasPersonDetail(Integer personId) throws BusinessException {
		logger.debug("hasPersonDetail - START ");
		
		boolean hasPersonDetail = false;
		
		try{
			hasPersonDetail = personDetailDao.hasPersonDetail(personId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_DETAIL_HAS_DETAILS, e);
		}
			
		logger.debug("hasPersonDetail - END ");
		return hasPersonDetail;
	}
	
	/**
	 * Gets the PersonDetail identified by the specified personId, with currencies
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 * @throws BusinessException 
	 */
	public PersonDetail getByPersonWithCurrencies(int personId) throws BusinessException {
		logger.debug("getByPersonWithCurrencies - START");
		
		PersonDetail personDetail = new PersonDetail();
		
		try{
			personDetail = personDetailDao.getByPersonWithCurrencies(personId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_DETAIL_GET_WITH_CURRENCIES, e);
		}
		
		logger.debug("getByPersonWithCurrencies - END");
		return personDetail;
	
	}
	
	/**
	 * Get with all by personDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param personDetailId
	 * @return
	 * @throws BusinessException 
	 */
	public PersonDetail getWithAll(Integer personDetailId) throws BusinessException {
		logger.debug("getWithAll - START ");
		PersonDetail personDetail = null;
		try{
			personDetail = personDetailDao.getWithAll(personDetailId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PERSON_DETAIL_GET_WITH_ALL, e);
		}
		logger.debug("getWithAll - END ");
		return personDetail;
	}
}
