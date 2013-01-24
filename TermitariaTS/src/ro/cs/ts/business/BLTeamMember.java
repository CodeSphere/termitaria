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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ro.cs.ts.cm.ProjectTeam;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.entity.SearchPersonBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.ws.client.cm.CMWebServiceClient;
import ro.cs.ts.ws.client.cm.entity.WSTeamMember;

public class BLTeamMember extends BusinessLogic {

	//singleton implementation
	private static BLTeamMember theInstance = null;
	
	private BLTeamMember(){};
	static {
		theInstance = new BLTeamMember();
	}
	public static BLTeamMember getInstance(){
		return theInstance;
	}
	
	/**
	 * Retrieves the result of TeamMember search from CM by a set of search criteria
	 * 
	 * @author Coni
	 * @return
	 * @throws BusinessException 
	 */
	public List<TeamMember> getTeamMemberFromSearch(Integer organizationId, String firstName, String lastName, boolean isNotDeleted) throws BusinessException {
		logger.debug("getTeamMemberFromSearch - START");
		List<TeamMember> teamMembers = new ArrayList<TeamMember>();
		try {
			SearchPersonBean spb = new SearchPersonBean();
			spb.setOrganizationId(organizationId);
			spb.setFirstName(firstName);
			spb.setLastName(lastName);
			spb.setWithDeleted(!isNotDeleted);
			List<WSTeamMember> wsTeamMembers = CMWebServiceClient.getInstance().getTeamMemberFromSearch(spb, null, true, true, false).getTeamMembers();
			if (wsTeamMembers != null) {
				for (WSTeamMember wsTeamMember : wsTeamMembers) {
					TeamMember member = new TeamMember();
					member.setFirstName(wsTeamMember.getFirstName());
					member.setLastName(wsTeamMember.getLastName());
					member.setMemberId(wsTeamMember.getMemberId());
					member.setPersonId(wsTeamMember.getPersonId());
					member.setProjectTeamId(wsTeamMember.getProjectTeamId());
					teamMembers.add(member);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_GET_FROM_SEARCH, e);
		}
		logger.debug("getTeamMemberFromSearch - END");
		return teamMembers;
	}
	
	/**
	 * Retrives a list of TeamMembers by their memberIds
	 * @author Coni
	 * @param teamMemberIds
	 * @return
	 * @throws BusinessException
	 */
	public List<TeamMember> getTeamMembersByMemberIds(HashSet<Integer> teamMemberIds, boolean isNotDeleted) throws BusinessException {
		logger.debug("getTeamMembersByIds - START");
		List<TeamMember> teamMembers = new ArrayList<TeamMember>();
		try {
			List<WSTeamMember> wsTeamMembers = CMWebServiceClient.getInstance().getTeamMembersByMemberIds(teamMemberIds, isNotDeleted);
			if (wsTeamMembers != null) {
				for (WSTeamMember wsTeamMember : wsTeamMembers) {
					TeamMember member = new TeamMember();
					member.setFirstName(wsTeamMember.getFirstName());
					member.setLastName(wsTeamMember.getLastName());
					member.setMemberId(wsTeamMember.getMemberId());
					member.setPersonId(wsTeamMember.getPersonId());
					member.setProjectTeamId(wsTeamMember.getProjectTeamId());
					member.setStatus(wsTeamMember.getStatus());
					teamMembers.add(member);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_GET_BY_IDS, e);
		}
		logger.debug("getTeamMembersByIds - END");
		return teamMembers;
	}
	
	/**
	 * Gets the team members from projects
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 * @return
	 * @throws BusinessException
	 */
	public List<TeamMember> getTeamMembersByProjectIds(HashSet<Integer> projectIds, boolean isExternal, boolean isNotDeleted) throws BusinessException {
		logger.debug("getTeamMembersExternalByProject - START");
		List<TeamMember> teamMembers = new ArrayList<TeamMember>();
		try {
			List<WSTeamMember> wsTeamMembers = CMWebServiceClient.getInstance().getTeamMembersByProjectIds(projectIds, isExternal, isNotDeleted);
			if (wsTeamMembers != null) {
				for (WSTeamMember wsTeamMember : wsTeamMembers) {
					TeamMember member = new TeamMember();
					member.setFirstName(wsTeamMember.getFirstName());
					member.setLastName(wsTeamMember.getLastName());
					member.setMemberId(wsTeamMember.getMemberId());
					member.setPersonId(wsTeamMember.getPersonId());
					member.setProjectTeamId(wsTeamMember.getProjectTeamId());
					member.setStatus(wsTeamMember.getStatus());
					member.setEmail(wsTeamMember.getEmail());
					
					ProjectTeam projectTeam = new ProjectTeam();
					if(wsTeamMember.getProjectTeam() != null) {
						projectTeam.setProjectTeamId(wsTeamMember.getProjectTeam().getProjectTeamId());
						projectTeam.setProjectId(wsTeamMember.getProjectTeam().getProjectId());																						
					}
					
					member.setProjectTeam(projectTeam);						
					teamMembers.add(member);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_EXTERNAL_GET_BY_PROJECT_IDS, e);
		}
		logger.debug("getTeamMembersExternalByProject - END");
		return teamMembers;
	}
	
	/**
	 * Get team member identifed by it's memberId
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 * @throws BusinessException 
	 */
	public TeamMember getTeamMember(Integer memberId, boolean isNotDeleted) throws BusinessException {
		logger.debug("getTeamMember - START ");
		TeamMember teamMember = null;
		try{
			HashSet<Integer> memberIds = new HashSet<Integer>();
			memberIds.add(memberId);
			
			List<TeamMember> members = getTeamMembersByMemberIds(memberIds, isNotDeleted);
			if(members != null && members.size() > 0) {
				teamMember = members.get(0);
			}
		} catch (Exception e) {
			throw new BusinessException(ICodeException.TEAM_MEMBER_GET, e);
		}
		logger.debug("getTeamMember - END ");
		return teamMember;
	}
}
