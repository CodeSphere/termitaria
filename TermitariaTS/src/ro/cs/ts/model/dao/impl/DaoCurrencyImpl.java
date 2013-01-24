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
package ro.cs.ts.model.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.SearchCurrencyBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.IDaoCurrency;

/**
 * Dao class for Currency entity
 * @author Coni
 *
 */
public class DaoCurrencyImpl extends HibernateDaoSupport implements IDaoCurrency {

	/**
	 * Gets all an organization available currencies
	 * @author Coni
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public List<Currency> getByOrganizationId(int organizationId) {
		logger.debug("getByOrganizationId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.currencyEntity);
		
		dc.add(Restrictions.eq("organizationId", organizationId));
		dc.add(Restrictions.ne("status", IConstant.NOM_CURRENCY_STATUS_DELETED));
		
		List<Currency> res = (List<Currency>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByOrganizationId - END");
		return res;
	}
	
	/**
	 * Searches for currencies using the criterion defined in searchCurrencyBean
	 * @author Coni
	 * @param searchCurrencyBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	public List<Currency> getCurrencyBeanFromSearch(SearchCurrencyBean searchCurrencyBean, boolean isDeleteAction) {
		logger.debug("getCurrencyBeanFromSearch - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.currencyEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.currencyEntity);
		
		if (searchCurrencyBean.getName() != null && !"".equals(searchCurrencyBean.getName())) {
			dc.add(Restrictions.ilike("name", "%".concat(searchCurrencyBean.getName()).concat("%")));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchCurrencyBean.getName()).concat("%")));
		}
		
		if (searchCurrencyBean.getInitials() != null && !"".equals(searchCurrencyBean.getInitials())) {
			dc.add(Restrictions.ilike("initials", "%".concat(searchCurrencyBean.getInitials()).concat("%")));
			dcCount.add(Restrictions.ilike("initials", "%".concat(searchCurrencyBean.getInitials()).concat("%")));
		}
		
		if (searchCurrencyBean.getOrganizationId() != null) {
			dc.add(Restrictions.eq("organizationId", searchCurrencyBean.getOrganizationId()));
			dcCount.add(Restrictions.eq("organizationId", searchCurrencyBean.getOrganizationId()));
		}
		
		dc.add(Restrictions.ne("status", IConstant.NOM_CURRENCY_STATUS_DELETED));
		dcCount.add(Restrictions.ne("status", IConstant.NOM_CURRENCY_STATUS_DELETED));
		
		// check if I have to order the results
		if(searchCurrencyBean.getSortParam() != null && !"".equals(searchCurrencyBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchCurrencyBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchCurrencyBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchCurrencyBean.getSortParam()));
			}
		}
 
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchCurrencyBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if (searchCurrencyBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("currencyId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchCurrencyBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchCurrencyBean.getResultsPerPage() == 0) {
				searchCurrencyBean.setNbrOfPages(nbrOfResults / searchCurrencyBean.getResultsPerPage());
			} else {
				searchCurrencyBean.setNbrOfPages(nbrOfResults / searchCurrencyBean.getResultsPerPage() + 1);
			}
			// after a currency is deleted, the same page has to be displayed;
			//only when all the client from last page are deleted, the previous page will be shown 
			if (isDeleteAction && (searchCurrencyBean.getCurrentPage() > searchCurrencyBean.getNbrOfPages()) ){
				searchCurrencyBean.setCurrentPage( searchCurrencyBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchCurrencyBean.setCurrentPage(1);
			}

		}
		
		List<Currency> res = (List<Currency>)getHibernateTemplate().findByCriteria(dc, (searchCurrencyBean.getCurrentPage()-1) * searchCurrencyBean.getResultsPerPage(), searchCurrencyBean.getResultsPerPage());				
		logger.debug("getCurrencyBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;			
	}
	
	/**
	 * Deletes a currency
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException
	 */
	public Currency delete(int currencyId) {
		logger.debug("delete - START");
		Currency currency = getForDelete(currencyId);
		currency.setStatus(IConstant.NOM_CURRENCY_STATUS_DELETED);
		getHibernateTemplate().update(IModelConstant.currencyEntity, currency);
		logger.debug("delete - END");
		return currency;
	}
	
	/**
	 * Returns a currency
	 * 
	 * @author coni
	 */
	public Currency getForDelete(int currencyId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(currencyId)));
		Currency currency = (Currency) getHibernateTemplate().get(IModelConstant.currencyEntity, currencyId);
		logger.debug("getForDelete - START");
		return currency;
	}
	
	/**
	 * Adds a new currency
	 * @author Coni
	 * @param currency
	 */
	public Integer add(Currency currency) {
		logger.debug("add - START");
		Integer currencyId = (Integer) getHibernateTemplate().save(IModelConstant.currencyEntity, currency);
		logger.debug("add - END");
		return currencyId;
	}
	
	/**
	 * Updates an existing currency
	 * @author Coni
	 * @param currency
	 */
	public void update(Currency currency) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.currencyForUpdateEntity, currency);
		logger.debug("update - END");
	}
	
	/**
	 * Gets the currency identified by the specified currencyId
	 * @author Coni
	 * @param currencyId
	 * @return the currency
	 */
	public Currency getAll(int currencyId) {
		logger.debug("getAll - START");
		Currency currency = (Currency) getHibernateTemplate().get(IModelConstant.currencyEntity, currencyId);
		logger.debug("getAll - END");
		return currency;
	}

	/**
	 * Gets the currency identified by the name and organisationId
	 * @author alexandru.dobre
	 * @param name
	 * @param currencyId
	 * @return the currency 
	 */
	public Currency getByName(String name, Integer organisationId) {
		
		logger.debug("getByName - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.currencyEntity);
		
		if (name != null && !"".equals(name)) {
			dc.add(Restrictions.eq("name", name));
		}
			
		if (organisationId != null) {
			dc.add(Restrictions.eq("organizationId", organisationId));
		}
		
		dc.add(Restrictions.ne("status", IConstant.NOM_CURRENCY_STATUS_DELETED));
		
		List<Currency> res = (List<Currency>)getHibernateTemplate().findByCriteria(dc);				
		logger.debug("getByName - END results size : ".concat(String.valueOf(res.size())));
		
		if (res.size()>0){
			return res.get(0);
		}
		
		return null;
	}
	
	/**
	 * Gets the currency identified by the initials and organisationId
	 * @author alexandru.dobre
	 * @param initials 
	 * @param currencyId
	 * @return the currency 
	 */
	public Currency getByInitials(String initials, Integer organisationId){
		
		logger.debug("getByInitials - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.currencyEntity);
		
		if (initials != null && !"".equals(initials)) {
			dc.add(Restrictions.eq("initials", initials));
		}
			
		if (organisationId != null) {
			dc.add(Restrictions.eq("organizationId", organisationId));
		}
		
		dc.add(Restrictions.ne("status", IConstant.NOM_CURRENCY_STATUS_DELETED));
		
		List<Currency> res = (List<Currency>)getHibernateTemplate().findByCriteria(dc);				
		logger.debug("getByInitials - END results size : ".concat(String.valueOf(res.size())));
		
		if (res.size()>0){
			return res.get(0);
		}
		
		return null;
	}
	
	/**
	 * Gets the currencies that have either the name or the initials specified within the same organisation
	 * @param name
	 * @param intitals
	 * @param organisationId
	 * @return
	 */
	public List<Currency> getByNameOrInitials (String name, String initials,Integer organisationId){
		
		logger.debug("getByNameOrInitials - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.currencyEntity);
		
		if (initials != null && !"".equals(initials) && name != null && !"".equals(name)) {
			dc.add(Restrictions.or(Restrictions.eq("name", name), Restrictions.eq("initials", initials)));
		}
			
		if (organisationId != null) {
			dc.add(Restrictions.eq("organizationId", organisationId));
		}
		
		dc.add(Restrictions.ne("status", IConstant.NOM_CURRENCY_STATUS_DELETED));
		
		List<Currency> res = (List<Currency>)getHibernateTemplate().findByCriteria(dc);				
		logger.debug("getByNameOrInitials - END results size : ".concat(String.valueOf(res.size())));
		
		return res;
		
	}
}
