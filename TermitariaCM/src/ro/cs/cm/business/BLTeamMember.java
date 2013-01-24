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
package ro.cs.cm.business;

import java.util.List;
import java.util.Set;

import ro.cs.cm.entity.SearchTeamMemberBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.ICodeException;
import ro.cs.cm.model.dao.DaoBeanFactory;
import ro.cs.cm.model.dao.IDaoTeamMember;


public class BLTeamMember extends BusinessLogic {
	
	private IDaoTeamMember teamMemberDao = DaoBeanFactory.getInstance().getDaoTeamMember();
	
	// singleton implementation 
	private static BLTeamMember theInstance = null;
	
	private BLTeamMember() {};
	
	static {
		theInstance = new BLTeamMember();
	}
	
	public static BLTeamMember getInstance() {
		return theInstance;
	}
	
	/**
	 * Get the teamMember identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMember get(Integer personId) throws BusinessException {
		logger.debug("get - START - teamMember with id = ".concat(Integer.toString(personId)));
		TeamMember member = null;
		try{
			member = teamMemberDao.get(personId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_GET, e);
		}
		logger.debug("get - END");
		return member;
	}
	
	/**
	 * Add the teamMember to the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 * @throws BusinessException 
	 */
	public void add(TeamMember member) throws BusinessException {
		logger.debug("add - START");
		try{
			teamMemberDao.add(member);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_ADD, e);
		}
		logger.debug("add - END");	
	}
	
	/**
	 * Updates the teamMember to the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 * @throws BusinessException 
	 */
	public void update(TeamMember member) throws BusinessException {
		logger.debug("update - START");
		try{
			teamMemberDao.update(member);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_UPDATE, e);
		}
		logger.debug("update - END");
	}
	
	/**
	 * Get the new external persons
	 * 
	 * @author Adelina
	 * 
	 * @return
	 * @throws BusinessException 
	 */
	public List<TeamMember> getNewTeamMembersByProjectTeam(Integer projectTeamId) throws BusinessException {
		logger.debug("getTeamMembersByProjectTeam - START");		
				
		List<TeamMember> members = null;
		try{
			members = teamMemberDao.getNewTeamMembersByProjectTeam(projectTeamId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_GET_NEW_FOR_PROJECT_TEAM, e);
		}
		
		logger.debug("getTeamMembersByProjectTeam - END");
		
		return members;
	}
	
	/**
	 * Get team members from projects
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 * @return
	 * @throws BusinessException 
	 */
	public List<TeamMember> getTeamMembersByProjectIds(Set<Integer> projectIds, boolean isExternal, boolean isNotDeleted) throws BusinessException {
		logger.debug("getTeamMembersByProjectIds - START");		
		List<TeamMember> members = null;
		try{
			members = teamMemberDao.getTeamMembersByProjectIds(projectIds, isExternal, isNotDeleted);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_GET_EXTERNAL_FOR_PROJECT, e);
		}
		logger.debug("getTeamMembersByProjectIds - END");
		return members;
	}	
		
	/**
	 * Delete the teamMember from the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 * @throws BusinessException 
	 */
	public void delete(TeamMember member) throws BusinessException {
		logger.debug("delete - START");
		try{
			teamMemberDao.delete(member);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_DELETE, e);
		}
		logger.debug("delete - END");
	}
	
	/**
	 * Get team member item identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMember getByMemberId(Integer memberId) throws BusinessException {
		logger.debug("getByMemberId - START ");
		TeamMember member = null;
		try{
			member = teamMemberDao.getByMemberId(memberId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_GET_BY_ID, e);
		}
		logger.debug("getByMemberId - END");
		return member;
	}
	
	/**
	 * Gets team members by different search criteria
	 * 
	 * @author Coni
	 * @param organizationId
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws BusinessException
	 */
	public List<TeamMember> getFromSearchSimple(SearchTeamMemberBean searchTeamMemberBean) throws BusinessException {
		logger.debug("getFromSearchSimple - START");
		List<TeamMember> members = null;
		try {
			members = teamMemberDao.getFromSearchSimple(searchTeamMemberBean);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_GET_FROM_SEARCH_SIMPLE, e);
		}
		logger.debug("getFromSearchSimple - END");
		return members;
	}
	
    /**
     * Retrieves from search a list of team members with basic info and pagination
     * 
     * @author Coni
     * @param searchTeamMemberBean
     * @return
     */
    public List<TeamMember> getFromSearchSimpleWithPagination(SearchTeamMemberBean searchTeamMemberBean) throws BusinessException{
    	logger.debug("getFromSearchSimpleWithPagination - START");
    	List<TeamMember> members = null;
    	try {
    		members = teamMemberDao.getFromSearchSimpleWithPagination(searchTeamMemberBean);
    	} catch(Exception bexc) {
    		throw new BusinessException(ICodeException.TEAMMEMBER_GET_FROM_SEARCH_SIMPLE_WITH_PAGINATION, bexc);
    	}
    	logger.debug("getFromSearchSimpleWithPagination - END");
    	return members;
    }
	
	/**
	 * Get team member items identified by their ids
	 * 
	 * @author Coni
	 * 
	 * @param memberIds
	 * @return
	 * @throws BusinessException 
	 */
	public List<TeamMember> getByMemberIds(Set<Integer> memberIds, boolean isNotDeleted) throws BusinessException {
		logger.debug("getByMemberIds - START ");
		List<TeamMember> members = null;
		try{
			if (memberIds != null) {
				members = teamMemberDao.getByMemberIds(memberIds, isNotDeleted);
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_GET_BY_IDS, e);
		}
		logger.debug("getByMemberIds - END");
		return members;
	}
	
	/**
	 * Change the status for the team member to deleted
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 * @throws BusinessException 
	 */
	public void changeStatusToDelete(TeamMember member) throws BusinessException {
		logger.debug("changeStatus - START");
		try{
			teamMemberDao.changeStatusToDelete(member);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.TEAMMEMBER_CHANGE_STATUS_TO_DELETE, e);
		}
		logger.debug("changeStatus - END");
	}
	
	/**
	 * Get team member item identified by it's id with id and status if it is not delete
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMember getSimpleByMemberId(Integer memberId) throws BusinessException {
		logger.debug("getSimpleByMemberId - START ");
		TeamMember member = null;
		try{
			member = teamMemberDao.getSimpleByMemberId(memberId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAMEMBER_GET_SIMPLE_BY_MEMBER_ID, e);
		}
		logger.debug("getSimpleByMemberId - END");
		return member;
	}
	
	

}
