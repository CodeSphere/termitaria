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

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoTeamMemberDetail;

/**
 * 
 * @author Coni
 *
 */
public class BLTeamMemberDetail extends BusinessLogic {

	//singleton implementation
	private static BLTeamMemberDetail theInstance = null;
	
	private BLTeamMemberDetail(){};
	static {
		theInstance = new BLTeamMemberDetail();
	}
	public static BLTeamMemberDetail getInstance(){
		return theInstance;
	}
	
	private static IDaoTeamMemberDetail teamMemberDetailDao = DaoBeanFactory.getInstance().getDaoTeamMemberDetail();
	
	/**
	 * Returns a TeamMemberDetail entity for the corresponding teamMemberId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMemberDetail getByTeamMemberId(int teamMemberId) throws BusinessException {
		logger.debug("getByTeamMemberId - START");
		TeamMemberDetail res = null;
		try {
			res = teamMemberDetailDao.getByTeamMemberId(teamMemberId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_DETAIL_GET_BY_TEAM_MEMBER_ID, e);
		}
		logger.debug("getByTeamMemberId - END");
		return res;
	}
	
	/**
	 * Returns a TeamMemerDetail entity for the corresponding teamMemberDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetailId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMemberDetail get(Integer teamMemberDetailId) throws BusinessException {
		logger.debug("get - START ");
		TeamMemberDetail teamMemberDetail = null;
		try{
			teamMemberDetail = teamMemberDetailDao.get(teamMemberDetailId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_DETAIL_GET, e);
		}
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
	 * @throws BusinessException 
	 */
	public boolean hasTeamMemberDetail(Integer memberId) throws BusinessException {
		logger.debug("hasTeamMemberDetail - START ");
		
		boolean hasTeamMemberDetail = false;
		try{
			hasTeamMemberDetail = teamMemberDetailDao.hasTeamMemberDetail(memberId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_DETAIL_FOR_MEMBER, e);
		}	
		logger.debug("hasTeamMemberDetail - END ");
		return hasTeamMemberDetail;
		
	}
	
	/**
	 * Add the teamMemberDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetail
	 * @return
	 * @throws BusinessException 
	 */
	public Integer add(TeamMemberDetail teamMemberDetail) throws BusinessException {
		logger.debug("add - START");
		Integer teamMemberDetailId = null;
		try{
			teamMemberDetailId = teamMemberDetailDao.add(teamMemberDetail);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_DETAIL_ADD, e);
		}
		logger.debug("add - END");
		return teamMemberDetailId;
	}
	
	/**
	 * Updates the teamMemberDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetail
	 * @throws BusinessException 
	 */
	public void update(TeamMemberDetail teamMemberDetail) throws BusinessException {
		logger.debug("update - START");
		try{
			teamMemberDetailDao.update(teamMemberDetail);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_DETAIL_UPDATE, e);
		}
		logger.debug("update - END");
	}	
	
	/**
	 * Returns TeamMemberDetail entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<TeamMemberDetail> getByCurrencyId(int currencyId) throws BusinessException {
		logger.debug("getByCurrencyId - START");
		List<TeamMemberDetail> res = null;
		try {
			res = teamMemberDetailDao.getByCurrencyId(currencyId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_GET_BY_CURRENCY_ID, e);
		}
		logger.debug("getByTeamMemberId - END");
		return res;
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
	 * @throws BusinessException 
	 */
	public TeamMemberDetail getWithAllByTeamMember(int teamMemberId) throws BusinessException {
		logger.debug("getWithAllByTeamMember - START");
		
		TeamMemberDetail teamMemberDetail = new TeamMemberDetail();
		
		try{
			teamMemberDetail = teamMemberDetailDao.getWithAllByTeamMember(teamMemberId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMEBER_DETAIL_GET_WITH_ALL, e);
		}		
		logger.debug("getWithAllByTeamMember - END");
		
		return teamMemberDetail;	
	}
	
	/**
	 * To delete a team member detail
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMemberDetail deleteTeamMemberDetail(Integer memberId) throws BusinessException {
		logger.debug("deleteTeamMemberDetail - START");
		
		TeamMemberDetail teamMemberDetail = null;
		try{
			teamMemberDetail = teamMemberDetailDao.deleteTeamMemberDetail(memberId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_DETAIL_DELETE, e);
		}
		
		logger.debug("deleteTeamMemberDetail - END");
		return teamMemberDetail;
	}	
}
