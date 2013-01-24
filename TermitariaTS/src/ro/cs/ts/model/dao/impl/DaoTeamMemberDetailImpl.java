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
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.model.dao.IDaoTeamMemberDetail;

/**
 * 
 * Dao class for TeamMemberDetail entity
 * @author Coni
 *
 */
public class DaoTeamMemberDetailImpl extends HibernateDaoSupport implements IDaoTeamMemberDetail {
	
	/**
	 * Returns a TeamMemberDetail entity for the corresponding teamMemberId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMemberDetail getByTeamMemberId(int teamMemberId) {
		logger.debug("getByTeamMemberId - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberDetailEntity);
		List<TeamMemberDetail> result = null;
		
		dc.add(Restrictions.eq("teamMemberId", teamMemberId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_DELETED));
		result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByTeamMemberId - END");
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Get a team member detail identified by it's member id 
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMemberDetail getByMemberId(Integer memberId) {
		logger.debug("getByMemberId - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberDetailSimpleEntity);
		List<TeamMemberDetail> result = null;
		
		dc.add(Restrictions.eq("teamMemberId", memberId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_DELETED));
		result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByMemberId - END");
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
		
	}
	
	/**
	 * Returns a TeamMemberDetail entity for the corresponding teamMemberId
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberId
	 * 
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMemberDetail getWithAllByTeamMember(int teamMemberId) {
		logger.debug("getWithAllByTeamMember - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberDetailWithAllEntity);
		List<TeamMemberDetail> result = null;
		
		dc.add(Restrictions.eq("teamMemberId", teamMemberId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_DELETED));
		result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getWithAllByTeamMember - END");
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
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
		TeamMemberDetail teamMemberDetail = (TeamMemberDetail)getHibernateTemplate().get(IModelConstant.teamMemberDetailWithAllEntity, teamMemberDetailId);
		logger.debug("get - END ");
		return teamMemberDetail;
	} 
	
	/**
	 * Checks if the team member has details associated
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public boolean hasTeamMemberDetail(Integer memberId) {
		logger.debug("hasTeamMemberDetail - START ");
		
		List<TeamMemberDetail> teamMemberDetails = new ArrayList<TeamMemberDetail>();
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberDetailSimpleEntity);
		dc.add(Restrictions.eq("teamMemberId", memberId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_DELETED));
		
		teamMemberDetails = getHibernateTemplate().findByCriteria(dc);		
		logger.debug("hasTeamMemberDetail - END ");
		if(teamMemberDetails.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Add the teamMemberDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetail
	 * @return
	 */
	public Integer add(TeamMemberDetail teamMemberDetail) {
		logger.debug("add - START");
		Integer teamMemberDetailId = (Integer) getHibernateTemplate().save(IModelConstant.teamMemberDetailEntity, teamMemberDetail);
		logger.debug("add - END");
		return teamMemberDetailId;
	}
	
	/**
	 * Updates the teamMemberDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetail
	 */
	public void update(TeamMemberDetail teamMemberDetail) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.teamMemberDetailForUpdateEntity, teamMemberDetail);
		logger.debug("update - END");
	}	
	
	/**
	 * Returns TeamMemberDetail entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<TeamMemberDetail> getByCurrencyId(int currencyId) {
		logger.debug("getByCurrencyId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberDetailWithCurrenciesEntity);
		
		dc.add(Restrictions.or(Restrictions.or(Restrictions.eq("costPriceCurrencyId", currencyId), Restrictions.eq("billingPriceCurrencyId", currencyId))
				, Restrictions.or(Restrictions.eq("overtimeCostCurrencyId", currencyId), Restrictions.eq("overtimeBillingCurrencyId", currencyId))));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_DELETED));
		
		List<TeamMemberDetail> res = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByCurrencyId - END");
		return res;
	}
	
	/**
	 * To delete a team member detail
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMemberDetail deleteTeamMemberDetail(Integer memberId) {
		logger.debug("deleteTeamMemberDetail - START");
		
		TeamMemberDetail teamMemberDetail = getByMemberId(memberId);
		
		if(teamMemberDetail != null && teamMemberDetail.getTeamMemberDetailId() != -1) {
			teamMemberDetail.setStatus(IConstant.NOM_TEAM_MEMBER_DETAIL_STATUS_DELETED);
			getHibernateTemplate().update(IModelConstant.teamMemberDetailSimpleEntity, teamMemberDetail);
		}
		logger.debug("deleteTeamMemberDetail - END");
		return teamMemberDetail;
	}	

}
