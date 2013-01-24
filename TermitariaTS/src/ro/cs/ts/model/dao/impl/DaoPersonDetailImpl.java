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
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.IDaoPersonDetail;

/**
 * Dao class for PersonDetails Entity
 * 
 * @author Coni
 */
public class DaoPersonDetailImpl extends HibernateDaoSupport implements IDaoPersonDetail {
	
	/**
	 * Adds a new PersonDetail
	 * 
	 * @author Coni
	 * @param personDetail
	 * @throws BusinessException
	 */
	public Integer add(PersonDetail personDetail) {
		logger.debug("add - START");
		Integer personDetailId = (Integer) getHibernateTemplate().save(IModelConstant.personDetailEntity, personDetail);
		logger.debug("add - END");
		return personDetailId;
	}
	
	/**
	 * Updates the personDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param personDetail
	 */
	public void update(PersonDetail personDetail) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.personDetailForUpdateEntity, personDetail);
		logger.debug("update - END");
	}	
	
	/**
	 * Get with all by personDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param personDetailId
	 * @return
	 */
	public PersonDetail getWithAll(Integer personDetailId) {
		logger.debug("getWithAll - START ");
		PersonDetail personDetail = (PersonDetail)getHibernateTemplate().get(IModelConstant.personDetailWithCurrencyEntity, personDetailId);
		logger.debug("getWithAll - END ");
		return personDetail;
	}
	
	/**
	 * Returns a TeamMemerDetail entity for the corresponding teamMemberDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetailId
	 * @return
	 */
	public TeamMemberDetail get(Integer teamMemberDetailId) {
		logger.debug("get - START ");
		TeamMemberDetail teamMemberDetail = (TeamMemberDetail)getHibernateTemplate().get(IModelConstant.teamMemberDetailEntity, teamMemberDetailId);
		logger.debug("get - END ");
		return teamMemberDetail;
	}
	
	
	/**
	 * Gets the PersonDetail identified by the specified personId
	 * @author Coni
	 * @param personId
	 * @return
	 */
	public PersonDetail getByPersonId(int personId) {
		logger.debug("getByPersonId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personDetailEntity);
		dc.add(Restrictions.eq("personId", personId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_DETAIL_STATUS_DELETED));
		
		List<PersonDetail> res = (List<PersonDetail>) getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getByPersonId - END");
		
		if (res != null && res.size() > 0 ) {
			return res.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Checks if the person has details associated
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public boolean hasPersonDetail(Integer personId) {
		logger.debug("hasPersonDetail - START ");
		
		List<PersonDetail> personDetails = new ArrayList<PersonDetail>();
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personDetailSimpleEntity);
		dc.add(Restrictions.eq("personId", personId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_DETAIL_STATUS_DELETED));
		
		personDetails = getHibernateTemplate().findByCriteria(dc);		
		logger.debug("hasPersonDetail - END ");
		if(personDetails.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns PersonDetail entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<PersonDetail> getByCurrencyId(int currencyId) {
		logger.debug("getByCurrencyId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personDetailEntity);
		
		dc.add(Restrictions.eq("costPriceCurrencyId", currencyId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_DETAIL_STATUS_DELETED));
		List<PersonDetail> res = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByCurrencyId - END");
		return res;
	}
	
	/**
	 * Gets the PersonDetail identified by the specified personId, with currencies
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public PersonDetail getByPersonWithCurrencies(int personId) {
		logger.debug("getByPersonWithCurrencies - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personDetailWithCurrencyEntity);
		dc.add(Restrictions.eq("personId", personId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_DETAIL_STATUS_DELETED));
		
		List<PersonDetail> res = (List<PersonDetail>) getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getByPersonWithCurrencies - END");
		
		if (res != null && res.size() > 0 ) {
			return res.get(0);
		} else {
			return new PersonDetail();
		}
	}

}
