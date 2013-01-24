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
package ro.cs.cm.model.dao;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.oxm.XmlMappingException;

import ro.cs.cm.entity.SearchTeamMemberBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.WSClientException;


/**
 * Dao Interface, implemented by DaoTeamMemberImpl
 * @author Coni
 * @author Adelina
 *
 */
public interface IDaoTeamMember {
	
	/**
	 * Get the teamMember identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMember get(Integer personId);
	
	/**
	 * Add the teamMember to the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void add(TeamMember member);
	
	/**
	 * Add the teamMember to the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void update(TeamMember member);
	
	/** Delete the teamMember from the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void delete(TeamMember member);
	
	/**
	 * Get the new external persons
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public List<TeamMember> getNewTeamMembersByProjectTeam(Integer projectTeamId);
	
	/**
	 * Get team member item identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMember getByMemberId(Integer memberId);
	
	/**
	 * Gets the list of team members for a person
	 * @author Coni
	 * @param personId
	 * @return
	 */
	public List<TeamMember> getByPersonId(Integer personId, boolean isNotDeleted);
	
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
	public List<TeamMember> getFromSearchSimple(SearchTeamMemberBean searchTeamMemberBean) throws XmlMappingException, IOException, WSClientException;
	
	/**
	 * Returns the active team members from a project team
	 * 
	 * @author Coni
	 * @param projectTeamId
	 * @return
	 */
	public List<TeamMember> getTeamMembersByProjectTeam(int projectTeamId);
	
	/**
	 * Get team member items identified by their ids
	 * 
	 * @author Coni
	 * 
	 * @param memberIds
	 * @return
	 */
	public List<TeamMember> getByMemberIds(Set<Integer> memberIds, boolean isNotDeleted);
	
	/**
	 * Change the status for the team member to deleted
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void changeStatusToDelete(TeamMember member);
	
	/**
	 * Get team members from projects
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 * @return
	 */
	public List<TeamMember> getTeamMembersByProjectIds(Set<Integer> projectIds, boolean isExternal, boolean isNotDeleted);
	
	/**
	 * Get team member item identified by it's id with id and status if it is not delete
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMember getSimpleByMemberId(Integer memberId);
	
	/**
	 * Get team member by personId and projectTeamId
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @param projectTeamId
	 * @return
	 */
	public TeamMember getMember(Integer personId, Integer projectTeamId);
	
	/**
	 * Change the status for the team member to open
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void changeStatusToOpen(TeamMember member);
	
    /**
     * Retrieves from search a list of team members with basic info and pagination
     * 
     * @author Coni
     * @param searchTeamMemberBean
     * @return
     */
    public List<TeamMember> getFromSearchSimpleWithPagination(SearchTeamMemberBean searchTeamMemberBean) throws XmlMappingException, IOException, WSClientException, BusinessException;
    
    /**
	 * Change the status for the team member
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 * @param status
	 */
	public void changeStatus(TeamMember member, byte status);

}
