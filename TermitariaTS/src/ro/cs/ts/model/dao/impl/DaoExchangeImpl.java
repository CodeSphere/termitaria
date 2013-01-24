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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.SearchExchangeBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.IDaoExchange;
import ro.cs.ts.utils.ExchangeComparator;
import ro.cs.ts.web.security.UserAuth;

/**
 * Dao class for Exchange entity
 * @author Coni
 *
 */
public class DaoExchangeImpl extends HibernateDaoSupport implements IDaoExchange {

	/**
	 * Returns Exchange entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 */
	public List<Exchange> getByCurrencyId(int currencyId) {
		logger.debug("getByCurrencyId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.exchangeEntity);
		
		dc.add(Restrictions.or(Restrictions.eq("firstCurrencyId", currencyId), Restrictions.eq("secondCurrencyId", currencyId)));
		dc.add(Restrictions.ne("status", IConstant.NOM_EXCHANGE_STATUS_DELETED));
		List<Exchange> res = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getByCurrencyId - END");
		return res;		
	}
	
	/**
	 * Searches for exchanges using the criterion defined in searchExchangeBean
	 * @author Coni
	 * @param searchCostSheetBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException 
	 * 
	 */
	public List<Exchange> getExchangeBeanFromSearch(SearchExchangeBean searchExchangeBean, boolean isDeleteAction) throws BusinessException {
		logger.debug("getExchangeBeanFromSearch - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.exchangeForListingEntity);
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//PROJECT
		List<Project> projects = null;
		if (searchExchangeBean.getProjectId() != null && searchExchangeBean.getProjectId().equals(new Integer(IConstant.NOM_EXCHANGE_SEARCH_ALL))) { //search in all
			//get the user available projects
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ExchangeSearchAll())) {
				//if the user is USER_ALL, i will search for exchanges registered for its organization or for its organization's projects; hence
				//only the organizationId restriction it is required; otherwise i will search only for exchanges registered only for the projects
				//to whom the user is PM

				projects = BLProject.getInstance().getProjectsByManager(userAuth.getPersonId(), true, true);

				if (projects != null && !projects.isEmpty()) {
					List<Integer> projectIds = new ArrayList<Integer>();
					for (Project project : projects) {
						projectIds.add(project.getProjectId());
					}
					dc.createCriteria("projectDetail").add(Restrictions.in("projectId", projectIds));
				} else {
					// if the user it is not PM for at least one project, no results must be displayed
					return new ArrayList<Exchange>();
				}
			}
		} if (searchExchangeBean.getProjectId() != null && searchExchangeBean.getProjectId().equals(new Integer(IConstant.NOM_EXCHANGE_SEARCH_FOR_ORG))) {  //exchanges from organization
			//if the user isn't USER_ALL, i won't show any exchanges; otherwise the projectId must be null
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ExchangeSearchAll())) {
				return new ArrayList<Exchange>();
			} else {
				dc.add(Restrictions.isNull("projectDetail"));
			}
		} else if (searchExchangeBean.getProjectId() != null && searchExchangeBean.getProjectId() > 0) {
			dc.createCriteria("projectDetail").add(Restrictions.eq("projectId", searchExchangeBean.getProjectId()));
		}
		
		//FIRST CURRENCY
		if (searchExchangeBean.getFirstCurrencyId() != null && !searchExchangeBean.getFirstCurrencyId().equals(-1)) {
			dc.createCriteria("firstCurrency").add(Restrictions.eq("currencyId", searchExchangeBean.getFirstCurrencyId()));
		}
		
		//SECOND CURRENCY
		if (searchExchangeBean.getSecondCurrencyId() != null && !searchExchangeBean.getSecondCurrencyId().equals(-1)) {
			dc.createCriteria("secondCurrency").add(Restrictions.eq("currencyId", searchExchangeBean.getSecondCurrencyId()));
		}
		
		//STATUS
		dc.add(Restrictions.ne("status", IConstant.NOM_EXCHANGE_STATUS_DELETED));
		
		//ORGANIZATIONID
		if (searchExchangeBean.getOrganizationId() != null) {
			dc.add(Restrictions.eq("organizationId", searchExchangeBean.getOrganizationId()));
		}
		
		List<Exchange> res = (List<Exchange>) getHibernateTemplate().findByCriteria(dc);
		
		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchExchangeBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if (searchExchangeBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			int nbrOfResults = res.size();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchExchangeBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchExchangeBean.getResultsPerPage() == 0) {
				searchExchangeBean.setNbrOfPages(nbrOfResults / searchExchangeBean.getResultsPerPage());
			} else {
				searchExchangeBean.setNbrOfPages(nbrOfResults / searchExchangeBean.getResultsPerPage() + 1);
			}
			// after an exchange is deleted, the same page has to be displayed;
			//only when all the exchanges from last page are deleted, the previous page will be shown 
			if (isDeleteAction && (searchExchangeBean.getCurrentPage() > searchExchangeBean.getNbrOfPages()) ){
				searchExchangeBean.setCurrentPage( searchExchangeBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchExchangeBean.setCurrentPage(1);
			}
		}
		
		//the returned list of exchanges
		List<Exchange> exchanges = new ArrayList<Exchange>();
		
		if (res != null && !res.isEmpty()) {
			if (projects == null) {
				HashSet<Integer> projectIds = new HashSet<Integer>();
				for (Exchange exchange : res) {
					if (exchange.getProjectDetail() != null) {
						projectIds.add(new Integer(exchange.getProjectDetail().getProjectId()));
					}
				}
				if (projectIds.size() > 0) {
					projects = BLProject.getInstance().getProjectsSimpleByProjectIds(projectIds);
				}
			}
			
			for (Exchange exchange : res) {
				//setting the project name
				if (exchange.getProjectDetail() != null && exchange.getProjectDetail().getProjectId() != null && projects != null) {
					for (Project project : projects) {
						if (exchange.getProjectDetail().getProjectId().equals(project.getProjectId())) {
							exchange.setProjectName(project.getName());
							break;
						}
					}
				}
			}
		
			//sorting the exchanges list
			//------sort the list
			if(searchExchangeBean.getSortParam().equals("projectName")){

				List<Exchange> exchangesForProject = new ArrayList<Exchange>();
				List<Exchange> exchangesForOrg = new ArrayList<Exchange>();
				for (Exchange exchange : res) {
					if (exchange.getProjectDetail() != null && exchange.getProjectDetail().getProjectId() != null) {
						exchangesForProject.add(exchange);
					} else {
						exchangesForOrg.add(exchange);
					}
				}
				Collections.sort(exchangesForProject, ExchangeComparator.getInstance().exchangeProjectNameComparator());
				exchangesForOrg.addAll(exchangesForProject);
				res = exchangesForOrg;
			}
	
			//ascending or descending
			if(searchExchangeBean.getSortDirection() == IConstant.DESCENDING){
				Collections.reverse(res);
			}
			
			long start = (searchExchangeBean.getCurrentPage() - 1) * searchExchangeBean.getResultsPerPage();
			long end  = searchExchangeBean.getCurrentPage() * searchExchangeBean.getResultsPerPage();
			
			
			// go over the entries				
			for(int i = (int)start ;i < end; i++) {
				if(i >= res.size()) {
					continue;
				}
				exchanges.add(res.get(i));
			}
		}
		logger.debug("getExchangeBeanFromSearch - END");
		return exchanges;
	}
	
	/**
	 * Deletes an exchange
	 * @author Coni
	 * @param exchangeId
	 * @return
	 * @throws Exception 
	 */
	public Exchange delete(int exchangeId) throws Exception {
		logger.debug("delete - START");
		Exchange exchange = getForDelete(exchangeId);
		
		if (exchange.getStatus() == IConstant.NOM_EXCHANGE_STATUS_ACTIVE) {
			//delete the reverse exchange for the two currencies
			List<Exchange> reverseExchange = null;
			if (exchange.getProjectDetail() != null) {
				reverseExchange = getProjectExchangeByCurrencies(exchange.getSecondCurrency().getCurrencyId(), exchange.getFirstCurrency().getCurrencyId(), exchange.getProjectDetail().getProjectDetailId());
			} else {
				reverseExchange = getOrganizationExchangeByCurrencies(exchange.getSecondCurrency().getCurrencyId(), exchange.getFirstCurrency().getCurrencyId(), exchange.getOrganizationId());
			}
			if (reverseExchange.size() == 1) {
				Exchange reverse = reverseExchange.get(0);
				reverse.setStatus(IConstant.NOM_EXCHANGE_STATUS_DELETED);
				getHibernateTemplate().update(IModelConstant.exchangeForDeleteEntity, reverse);
			} else {
				logger.error("Error while retrieving the reverse exchange; number of results: ".concat(String.valueOf(reverseExchange.size())));
				throw new Exception();
			}
		
		exchange.setStatus(IConstant.NOM_EXCHANGE_STATUS_DELETED);
		getHibernateTemplate().update(IModelConstant.exchangeForDeleteEntity, exchange);
		}
		
		logger.debug("delete - END");
		return exchange;
	}
	
	/**
	 * 
	 * @author Coni
	 * @param firstCurrencyId
	 * @param secondCurrencyId
	 * @param projectDetailId
	 * @return
	 */
	public List<Exchange> getProjectExchangeByCurrencies(int firstCurrencyId, int secondCurrencyId, int projectDetailId) {
		logger.debug("getProjectExchangeByCurrencies - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.exchangeEntity);
		
		dc.add(Restrictions.eq("projectDetailId", projectDetailId));
		dc.add(Restrictions.eq("firstCurrencyId", firstCurrencyId));
		dc.add(Restrictions.eq("secondCurrencyId", secondCurrencyId));
		dc.add(Restrictions.ne("status", IConstant.NOM_EXCHANGE_STATUS_DELETED));
		
		List<Exchange> res = (List<Exchange>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getProjectExchangeByCurrencies - END");
		return res;		
	}
	
	/**
	 * 
	 * @author Coni
	 * @param firstCurrencyId
	 * @param secondCurrencyId
	 * @param organizationId
	 * @return
	 */
	public List<Exchange> getOrganizationExchangeByCurrencies(int firstCurrencyId, int secondCurrencyId, int organizationId) {
		logger.debug("getOrganizationExchangeByCurrencies - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.exchangeEntity);
		
		dc.add(Restrictions.isNull("projectDetailId"));
		dc.add(Restrictions.eq("firstCurrencyId", firstCurrencyId));
		dc.add(Restrictions.eq("secondCurrencyId", secondCurrencyId));
		dc.add(Restrictions.eq("organizationId", organizationId));
		dc.add(Restrictions.ne("status", IConstant.NOM_EXCHANGE_STATUS_DELETED));
		
		List<Exchange> res = (List<Exchange>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getOrganizationExchangeByCurrencies - END");
		return res;	
	}
	
	/**
	 * Returns an exchange
	 * 
	 * @author coni
	 */
	public Exchange getForDelete(int exchangeId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(exchangeId)));
		Exchange exchange = (Exchange) getHibernateTemplate().get(IModelConstant.exchangeForListingEntity, exchangeId);
		logger.debug("getForDelete - START");
		return exchange;
	}
	
	/**
	 * Gets the exchange identified by the specified exchangeId
	 * @author Coni
	 * @param exchangeId
	 * @return
	 * @throws BusinessException
	 */
	public Exchange getAll(int exchangeId) {
		logger.debug("getAll - START");
		Exchange exchange = (Exchange) getHibernateTemplate().get(IModelConstant.exchangeForListingEntity, exchangeId);
		logger.debug("getAll - END");
		return exchange;
	}
	
	/**
	 * Updates an existing exchange and its reverse exchange from a project
	 * @author Coni
	 * @param costSheet
	 */
	public void update(Exchange exchange) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.exchangeForUpdate, exchange);
		logger.debug("update - END");
	}
	
	/**
	 * Adds a new exchange and its reverse for a project
	 * @author Coni
	 * @param exchange
	 */
	public Integer add(Exchange exchange) {
		logger.debug("add - START");
		Integer exchangeId = (Integer) getHibernateTemplate().save(IModelConstant.exchangeEntity, exchange);
		logger.debug("add - END");
		return exchangeId;
	}
	
	/**
	 * Deletes an exchange
	 * 
	 * @author Adelina
	 * 
	 * @param exchange
	 * @return
	 */
	public Exchange delete(Exchange exchange) {
		logger.debug("delete - START");
		logger.debug("Deleting exchange with id: ".concat(String.valueOf(exchange.getExchangeId())));
			
		exchange.setStatus(IConstant.NOM_EXCHANGE_STATUS_DELETED);
		
		logger.debug("Deleting the exchange : " + exchange);
		getHibernateTemplate().update(IModelConstant.exchangeSimpleEntity, exchange);
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
	 */
	public List<Exchange> getByProjectDetailId(Integer projectDetailId) {
		logger.debug("getByProjectDetailId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.exchangeEntity);		
		
		dc.add(Restrictions.eq("projectDetailId", projectDetailId));
		dc.add(Restrictions.ne("status", IConstant.NOM_EXCHANGE_STATUS_DELETED));
		
		List<Exchange> res = (List<Exchange>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByProjectDetailId - END");
		return res;
	}	
}
