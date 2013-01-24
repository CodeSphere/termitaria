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

import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;


/**
 * Dao Interface, implemented by DaoTeamMemberDetailImpl
 * @author Coni
 *
 */
public interface IDaoTeamMemberDetail {
	
	/**
	 * Returns a TeamMemberDetail entity for the corresponding teamMemberId
	 * @author Coni
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMemberDetail getByTeamMemberId(int teamMemberId);
	
	/**
	 * Get a team member detail identified by it's member id 
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMemberDetail getByMemberId(Integer memberId);
	
	/**
	 * Returns a TeamMemerDetail entity for the corresponding teamMemberDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetailId
	 * @return
	 */
	public TeamMemberDetail get(Integer teamMemberDetailId);
	
	/**
	 * Checks if the team member has details associated
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public boolean hasTeamMemberDetail(Integer memberId);
	
	/**
	 * Add the teamMemberDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetail
	 * @return
	 */
	public Integer add(TeamMemberDetail teamMemberDetail);
	
	/**
	 * Updates the teamMemberDetail to the database
	 * 
	 * @author Adelina
	 * 
	 * @param teamMemberDetail
	 */
	public void update(TeamMemberDetail teamMemberDetail);
	
	/**
	 * Returns TeamMemberDetail entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<TeamMemberDetail> getByCurrencyId(int currencyId);
	
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
	public TeamMemberDetail getWithAllByTeamMember(int teamMemberId);	
	
	/**
	 * To delete a team member detail
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMemberDetail deleteTeamMemberDetail(Integer memberId);


}
