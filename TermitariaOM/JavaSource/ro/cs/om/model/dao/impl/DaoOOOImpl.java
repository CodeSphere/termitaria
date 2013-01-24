/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.om.model.dao.impl;

import java.text.ParseException;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchOOOBean;
import ro.cs.om.model.dao.IDaoOOO;
import ro.cs.om.web.entity.OutOfOfficeWeb;

/**
 * @author alu
 *
 */
public class DaoOOOImpl extends HibernateDaoSupport implements IDaoOOO{

	/**
     * Returns a list with out of office profiles for the given person
     *
     * @author alu
     * 
     * @param personId
     * @return
     */
	public List<OutOfOffice> getByPersonID(int personId) {
		logger.debug("getByPersonID - START");
		
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.oooAllEntity).append(" where personId = ?");
		
		List<OutOfOffice> oooList = (List<OutOfOffice>) getHibernateTemplate().find(query.toString(), personId);
		
		logger.debug("getByPersonID - END");
		return oooList;
	}
	
	/**
     * Returns a list of persons that figure as replacements in at least one OOO profile
     * Receives as param a list of persons ids to look for
     * @author coni
     * 
     * @param personReplacementId
     * @return
     */

	public List<Person> getOOOPersonReplacementsFromIds(Integer[] personReplacementId) {
		logger.debug("getByPersonReplacementID - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.oooAllEntity);
		dc.createCriteria("personReplacement").add(Restrictions.in("personId", personReplacementId));
		dc.setProjection(Projections.distinct(Projections.property("personReplacement")));
		List<Person> list= getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByPersonReplacementID - END");
		return list;
	}
	  
	
	/**
     * Return an instance of ooo web for the given ID
     *
     * @author alu
     * 
     * @param oooId
     * @return
     */
    public OutOfOfficeWeb getOOOWebByID(int oooId){
    	logger.debug("getOOOWebByID - START");
    	
    	OutOfOfficeWeb oooWeb = (OutOfOfficeWeb)getHibernateTemplate().get(IModelConstant.oooWebEntity, oooId);
    	
    	logger.debug("getOOOWebByID - END");
    	return oooWeb;
    }
    
    /**
     * Updates given out of office profile
     *
     * @author alu
     * 
     * @param oooWeb
     */
    public void updateOOOweb(OutOfOfficeWeb oooWeb){
    	logger.debug("addOOOweb - START");
		getHibernateTemplate().update(IModelConstant.oooWebEntity, oooWeb);
		logger.debug("addOOOweb - END");
    }
    
    /**
     * Adds given out of office profile
     *
     * @author alu
     * 
     * @param oooWeb
     */
    public void addOOOweb(OutOfOfficeWeb oooWeb){
    	logger.debug("addOOOweb - START");
		getHibernateTemplate().save(IModelConstant.oooWebEntity, oooWeb);
		logger.debug("addOOOweb - END");
    }
    
    /**
	 * Searches for out of office profiles after criterion from searchOOOBean
	 * @author alu
	 * @return A list of ooo beans 
	 * @throws ParseException 
	 */
	public List getOOOBeanFromSearch(SearchOOOBean searchOOOBean, boolean isDeleteAction) throws ParseException{
		logger.debug("getOOOBeanFromSearch - START");
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.oooAllEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.oooAllEntity);
		dc.createAlias("person", "person");
		dcCount.createAlias("person", "person");
		if(searchOOOBean.getOrganisationId() != -1) {
			dc.createCriteria("person.depts").add(Restrictions.eq("organisationId", searchOOOBean.getOrganisationId()));
			dcCount.createCriteria("person.depts").add(Restrictions.eq("organisationId", searchOOOBean.getOrganisationId()));
		}
			
		if (searchOOOBean.getOwnerFirstName() != null && !"".equals(searchOOOBean.getOwnerFirstName())){
			dc.add(Restrictions.eq("person.firstName", searchOOOBean.getOwnerFirstName()));
			dcCount.add(Restrictions.eq("person.firstName", searchOOOBean.getOwnerFirstName()));		
			logger.debug("Owner first name: ".concat(searchOOOBean.getOwnerFirstName()));
		}
		if (searchOOOBean.getOwnerLastName() != null && !"".equals(searchOOOBean.getOwnerLastName())){
			dc.add(Restrictions.eq("person.lastName", searchOOOBean.getOwnerLastName()));
			dcCount.add(Restrictions.eq("person.lastName", searchOOOBean.getOwnerLastName()));
			logger.debug("Owner last name: ".concat(searchOOOBean.getOwnerLastName()));
		}	
		
		if (searchOOOBean.getReplacementFirstName() != null && !"".equals(searchOOOBean.getReplacementFirstName()) &&
				searchOOOBean.getReplacementLastName() != null && !"".equals(searchOOOBean.getReplacementLastName())){
			dc.createCriteria("personReplacement").add(Restrictions.eq("firstName", searchOOOBean.getReplacementFirstName())).add(Restrictions.eq("lastName", searchOOOBean.getReplacementLastName()));	
			dcCount.createCriteria("personReplacement").add(Restrictions.eq("firstName", searchOOOBean.getReplacementFirstName())).add(Restrictions.eq("lastName", searchOOOBean.getReplacementLastName()));	
		}
		
		if (searchOOOBean.getStartPeriod() != null){
			dc.add(Expression.ge("startPeriod", searchOOOBean.getStartPeriod()));		
			dcCount.add(Expression.ge("startPeriod", searchOOOBean.getStartPeriod()));
		}
		
		if (searchOOOBean.getEndPeriod() != null){
			dc.add(Expression.le("endPeriod", searchOOOBean.getEndPeriod()));		
			dcCount.add(Expression.le("endPeriod", searchOOOBean.getEndPeriod()));
		}
		
		dc.setProjection(Projections.id());
		dcCount.setProjection(Projections.id());
		// until here, I've created the subquery
		// now, it's time to retrive all the profiles that are in the list of the subquery
		DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.oooAllEntity);
		dc1.createAlias("person", "person");
		dc1.add(Subqueries.propertyIn("outOfOfficeId", dc));
		
		// check if I have to order the results
		if(searchOOOBean.getSortParam() != null && !"".equals(searchOOOBean.getSortParam())) {
			logger.debug("Add sorting ! 234234");
			// if I have to, check if I have to order them ascending or descending
			if (searchOOOBean.getSortDirection() == -1) {
				// ascending
				dc1.addOrder(Order.asc(searchOOOBean.getSortParam()));
			} else {
				// descending
				dc1.addOrder(Order.desc(searchOOOBean.getSortParam()));
			}
		}

		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchOOOBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if ( searchOOOBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			
			// set the count(*) restriction
			dcCount.setProjection(Projections.countDistinct("outOfOfficeId"));
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchOOOBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchOOOBean.getResultsPerPage() == 0) {
				searchOOOBean.setNbrOfPages(nbrOfResults / searchOOOBean.getResultsPerPage());
			} else {
				searchOOOBean.setNbrOfPages(nbrOfResults / searchOOOBean.getResultsPerPage() + 1);
			}
			// after an ooo profile is deleted, the same page has to be displayed;
			//only when all the ooo profiles from last page are deleted, the previous page will be shown 
			if ( isDeleteAction && (searchOOOBean.getCurrentPage() > searchOOOBean.getNbrOfPages()) ){
				searchOOOBean.setCurrentPage( searchOOOBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchOOOBean.setCurrentPage(1);
			}

		}		
		List res = getHibernateTemplate().findByCriteria(dc1, (searchOOOBean.getCurrentPage()-1) * searchOOOBean.getResultsPerPage(), searchOOOBean.getResultsPerPage());		
		
		logger.debug("getOOOBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;
	}
	
	/**
	 * Deletes ooo profile identified by it's id
	 *
	 * @author alu
	 * 
	 * @param oooId
	 */
	public void delete(int oooId){
		logger.debug("delete - START");
		
		OutOfOffice ooo = new OutOfOffice();
		ooo.setOutOfOfficeId(oooId);
		getHibernateTemplate().delete(IModelConstant.oooEntity, ooo);
		
		logger.debug("delete - END");
	}	
	
	/**
     * Returns all the ooo profiles
     * 
     * @author Adelina
     * 
     * @return List<OutOfOffice>
     */

	public List<OutOfOffice> getAllOOO() {
		logger.debug("Dao getOOO - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.oooAllEntity);		
		List<OutOfOffice> list= getHibernateTemplate().findByCriteria(dc);
		logger.debug("OOO list = " + list.size() + ", " + list);		
		logger.debug("Dao getOOO - END");
		return list;
	}

}
