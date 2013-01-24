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
package ro.cs.cm.model.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.StringUtils;

import ro.cs.cm.business.BLPerson;
import ro.cs.cm.business.BLProject;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.IModelConstant;
import ro.cs.cm.common.Tools;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.SearchTeamMemberBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.WSClientException;
import ro.cs.cm.model.dao.IDaoTeamMember;
import ro.cs.cm.om.Person;

/**
 * Dao class for TeamMember Entity
 * 
 * @author Coni
 * @author Adelina
 */
public class DaoTeamMemberImpl extends HibernateDaoSupport implements IDaoTeamMember {
	
	/**
	 * Get the teamMember identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMember get(Integer personId) {
		logger.debug("get - START - teamMember with person id = ".concat(Integer.toString(personId)));
		List<TeamMember> members = new ArrayList<TeamMember>();
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
		dc.add(Restrictions.eq("personId", personId));		
		members = getHibernateTemplate().findByCriteria(dc);
		logger.debug("get - END");
		return members.get(0);
	}
	
	/**
	 * Get team member item identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMember getByMemberId(Integer memberId) {
		logger.debug("getByMemberId - START ");
		TeamMember member = (TeamMember) getHibernateTemplate().get(IModelConstant.teamMemberEntity, memberId);
		logger.debug("getByMemberId - END");
		return member;
	}
	
	/**
	 * Add the teamMember to the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void add(TeamMember member) {
		logger.debug("add - START");
		getHibernateTemplate().save(IModelConstant.teamMemberEntity, member);
		logger.debug("add - END");
	}
	
	/**
	 * Update the teamMember from the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void update(TeamMember member) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.teamMemberEntity, member);
		logger.debug("update - END");
	}
	
	/**
	 * Delete the teamMember from the database
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void delete(TeamMember member) {
		logger.debug("delete - START");		
		getHibernateTemplate().delete(IModelConstant.teamMemberForDeleteEntity, member);
		logger.debug("delete - END");
	}
	
	/**
	 * Change the status for the team member to deleted
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void changeStatusToDelete(TeamMember member) {
		logger.debug("changeStatus - START");
		member.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_DELETED);
		getHibernateTemplate().update(IModelConstant.teamMemberForDeleteEntity, member);
		logger.debug("changeStatus - END");
	}
	
	/**
	 * Change the status for the team member to open
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 */
	public void changeStatusToOpen(TeamMember member) {
		logger.debug("changeStatus - START");
		member.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED);
		getHibernateTemplate().update(IModelConstant.teamMemberForDeleteEntity, member);	
		logger.debug("changeStatus - END");
	}	
	
	/**
	 * Change the status for the team member
	 * 
	 * @author Adelina
	 * 
	 * @param member
	 * @param status
	 */
	public void changeStatus(TeamMember member, byte status) {
		logger.debug("changeStatus - START");
		member.setStatus(status);
		getHibernateTemplate().update(IModelConstant.teamMemberForDeleteEntity, member);	
		logger.debug("changeStatus - END");
	}

	/**
	 * Get the new external persons
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public List<TeamMember> getNewTeamMembersByProjectTeam(Integer projectTeamId) {
		logger.debug("getTeamMembersByProjectTeam - START");		
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
		dc.add(Restrictions.eq("personId", new Integer(0)));
		dc.add(Restrictions.eq("projectTeamId", projectTeamId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
		List<TeamMember> members = getHibernateTemplate().findByCriteria(dc);
		
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
	 */
	public List<TeamMember> getTeamMembersByProjectIds(Set<Integer> projectIds, boolean isExternal, boolean isNotDeleted) {
		logger.debug("getTeamMembersByProjectIds - START");		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberWithProjectTeamEntity);	
		
		logger.debug("isExternal = " + isExternal);
		if(isExternal) {
			dc.add(Restrictions.eq("personId", new Integer(0)));
		}		
		dc.createCriteria("projectTeam").add(Restrictions.in("projectId", projectIds));
		
		logger.debug("isNotDeleted = " + isNotDeleted);		
		if(isNotDeleted) {
			dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
		}	
		
		List<TeamMember> members = getHibernateTemplate().findByCriteria(dc);
		logger.debug("team members = " + members);
		
		List<TeamMember> teamMembers = new ArrayList<TeamMember>();
		if(members != null && members.size() > 0) {
			for(TeamMember teamMember: members) {	
				logger.debug("team member = " + teamMember);				
				boolean changeStatus = false;
				if(teamMember != null && teamMember.getPersonId() > 0) {
					try {
						Person person = BLPerson.getInstance().get(teamMember.getPersonId());
						logger.debug("person = " + person);
						Byte activated = new Byte(IConstant.NOM_PERSON_STATUS_ACTIVATED);						
						if(person != null && !activated.equals(person.getStatus())) {
							changeStatus = true;
						}
						if(!changeStatus) {									
							logger.debug("teamMember = " + teamMember);					
							teamMembers.add(teamMember);
						} else {
							if(person != null) {
								changeStatus(teamMember, person.getStatus());
							}
						}
					} catch (BusinessException e) {			
						e.printStackTrace();
					}									
				}								
				if(!isNotDeleted) {
					teamMembers.add(teamMember);
				}
			}
		}		
		
		Tools.getInstance().printList(logger, teamMembers);
							
		logger.debug("getTeamMembersByProjectIds - END");
		return teamMembers;		
	}	
	
	/**
	 * Gets the list of team members for a person
	 * @author Coni
	 * @param personId
	 * @return
	 */
	public List<TeamMember> getByPersonId(Integer personId, boolean isNotDeleted) {
		logger.debug("getByPersonId - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberSimpleEntity);
		dc.add(Restrictions.eq("personId", personId));
		List<TeamMember> teamMembers = new ArrayList<TeamMember>();
		List<TeamMember> members = getHibernateTemplate().findByCriteria(dc);
		for(TeamMember member : members) {
			if(member.getStatus() == IConstant.NOM_TEAM_MEMBER_STATUS_DISABLED && member.getPersonId() > 0) {
				if(isNotDeleted) {
					boolean changeStatusToActivate = false;
					try {						
						Person person = BLPerson.getInstance().get(personId);
						logger.debug("person = " + person);
						Byte activated = new Byte(IConstant.NOM_PERSON_STATUS_ACTIVATED);							
						if(person != null && activated.equals(person.getStatus())) {
							changeStatusToActivate = true;
						}
					} catch (BusinessException e) {			
						e.printStackTrace();
					}	
					if(changeStatusToActivate) {
						changeStatusToOpen(member);
						teamMembers.add(member);
					}					
				} else {
					teamMembers.add(member);
				}
			} else {
				teamMembers.add(member);
			}
		}
		logger.debug("getByPersonId - END");
		return teamMembers;
	}
	
	/**
	 * Gets team members by different search criteria
	 * 
	 * @author Coni
	 * @param searchTeamMemberBean
	 * @return
	 * @throws BusinessException 
	 * @throws WSClientException 
	 * @throws IOException 
	 * @throws XmlMappingException 
	 */
	public List<TeamMember> getFromSearchSimple(SearchTeamMemberBean searchTeamMemberBean) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getFromSearchSimple - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberWithProjectTeamAndProjectEntity);
		
		//retrieve all the team members for persons from the organization(which have personId !=  0) or 
		//not(which have firstName and lastName) that correspond to the search criteria
		if (searchTeamMemberBean.getFirstName() != null && !"".equals(searchTeamMemberBean.getFirstName()) && searchTeamMemberBean.getLastName() != null && !"".equals(searchTeamMemberBean.getLastName())) {
			dc.add(Restrictions.and(Restrictions.ilike("firstName", "%".concat(searchTeamMemberBean.getFirstName()).concat("%")),
										   			Restrictions.ilike("lastName", "%".concat(searchTeamMemberBean.getLastName()).concat("%"))));
		} else if (searchTeamMemberBean.getFirstName() != null && !"".equals(searchTeamMemberBean.getFirstName())) {
			dc.add(Restrictions.ilike("firstName", "%".concat(searchTeamMemberBean.getFirstName()).concat("%")));
		} else if (searchTeamMemberBean.getLastName() != null && !"".equals(searchTeamMemberBean.getLastName())) {
			dc.add(Restrictions.ilike("lastName", "%".concat(searchTeamMemberBean.getLastName()).concat("%")));
		}
			
		if(!searchTeamMemberBean.isWithDeleted()) {
			dc.add(Restrictions.eq("status", IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED));
		}
		
		if (searchTeamMemberBean.getProjectId() != null) {
			//if a project is selected, the team members project team must correspond to that project
			dc.createCriteria("projectTeam").add(Restrictions.eq("projectId", searchTeamMemberBean.getProjectId()));
			if (!searchTeamMemberBean.isIncludeFinishedAndAbandonedProjects()) {
				dc.createCriteria("projectTeam").createCriteria("project").add(Restrictions.eq("status", IConstant.NOM_PROJECT_STATUS_OPENED));
			}
		} 
		
		if (searchTeamMemberBean.getOrganizationId() != null) {
			dc.createCriteria("projectTeam").createCriteria("project").add(Restrictions.eq("organizationId", searchTeamMemberBean.getOrganizationId()));
		}
		
		List<TeamMember> members = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getFromSearchSimple - END");
		return members;
	}
	
	
	/**
	 * Returns the active team members from a project team
	 * 
	 * @author Coni
	 * @param projectTeamId
	 * @return
	 */
	public List<TeamMember> getTeamMembersByProjectTeam(int projectTeamId) {
		logger.debug("getTeamMembersByProjectTeam - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberSimpleEntity);
		dc.add(Restrictions.eq("projectTeamId", projectTeamId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
		List<TeamMember> res = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getTeamMembersByProjectTeam - START");
		return res;
	}

	/**
	 * Get team member items identified by their ids
	 * 
	 * @author Coni
	 * 
	 * @param memberIds
	 * @return
	 */
	public List<TeamMember> getByMemberIds(Set<Integer> memberIds, boolean isNotDeleted) {
		logger.debug("getByMemberIds - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberSimpleEntity);
		dc.add(Restrictions.in("memberId", memberIds));
		if(isNotDeleted) {
			dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
		}
		List<TeamMember> res = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByMemberIds - START");
		return res;
	}
	
	
	/**
	 * Get team member item identified by it's id with id and status if it is not delete
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 */
	public TeamMember getSimpleByMemberId(Integer memberId) {
		logger.debug("getSimpleByMemberId - START ");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberForDeleteEntity);
		List<TeamMember> result = null;
		
		dc.add(Restrictions.eq("memberId", memberId));
		dc.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
		result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getSimpleByMemberId - END");
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}	
	}
	
	/**
	 * Get team member by personId and projectTeamId
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @param projectTeamId
	 * @return
	 */
	public TeamMember getMember(Integer personId, Integer projectTeamId) {
		logger.debug("getByPersonId - START ");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
		List<TeamMember> result = null;
		
		dc.add(Restrictions.eq("personId", personId));
		dc.add(Restrictions.eq("projectTeamId", projectTeamId));	
		result = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByPersonId - END");
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}	
	}

    /**
     * Retrieves from search a list of team members with basic info and pagination; search method
     * for TS person search
     * 
     * @author Coni
     * @param searchTeamMemberBean
     * @return
     * @throws WSClientException 
     * @throws IOException 
     * @throws XmlMappingException 
     * @throws BusinessException 
     */
    public List<TeamMember> getFromSearchSimpleWithPagination(SearchTeamMemberBean searchTeamMemberBean) throws XmlMappingException, IOException, WSClientException, BusinessException {
    	logger.debug("getFromSearchSimpleWithPagination - START");
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberWithProjectTeamAndProjectEntity);
    	DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.teamMemberWithProjectTeamAndProjectEntity);
		
		//retrieve all the team members for persons from the organization(which have personId !=  0) or 
		//not(which have firstName and lastName) that correspond to the search criteria
		if (searchTeamMemberBean.getFirstName() != null && !"".equals(searchTeamMemberBean.getFirstName()) && searchTeamMemberBean.getLastName() != null && !"".equals(searchTeamMemberBean.getLastName())) {
			dc.add(Restrictions.and(Restrictions.ilike("firstName", "%".concat(searchTeamMemberBean.getFirstName()).concat("%")),
										   			Restrictions.ilike("lastName", "%".concat(searchTeamMemberBean.getLastName()).concat("%"))));
			dcCount.add(Restrictions.and(Restrictions.ilike("firstName", "%".concat(searchTeamMemberBean.getFirstName()).concat("%")),
		   			Restrictions.ilike("lastName", "%".concat(searchTeamMemberBean.getLastName()).concat("%"))));
		} else if (searchTeamMemberBean.getFirstName() != null && !"".equals(searchTeamMemberBean.getFirstName())) {
			dc.add(Restrictions.ilike("firstName", "%".concat(searchTeamMemberBean.getFirstName()).concat("%")));
			dcCount.add(Restrictions.ilike("firstName", "%".concat(searchTeamMemberBean.getFirstName()).concat("%")));
		} else if (searchTeamMemberBean.getLastName() != null && !"".equals(searchTeamMemberBean.getLastName())) {
			dc.add(Restrictions.ilike("lastName", "%".concat(searchTeamMemberBean.getLastName()).concat("%")));
			dcCount.add(Restrictions.ilike("lastName", "%".concat(searchTeamMemberBean.getLastName()).concat("%")));
		}
			
		if(!searchTeamMemberBean.isWithDeleted()) {
			dc.add(Restrictions.eq("status", IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED));
			dcCount.add(Restrictions.eq("status", IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED));
		}
		
		DetachedCriteria dcOnProject = dc.createCriteria("projectTeam").createCriteria("project");
		DetachedCriteria dcCountOnProject = dcCount.createCriteria("projectTeam").createCriteria("project");
		if (searchTeamMemberBean.getProjectId() != null && !searchTeamMemberBean.getProjectId().equals(Integer.valueOf(IConstant.NOM_TS_PERSON_SEARCH_IN_ORGANIZATION))) {
			if (searchTeamMemberBean.getProjectId().equals(Integer.valueOf(IConstant.NOM_TS_PERSON_SEARCH_ALL_PROJECTS))) {
				//if the used doesn't have the permission to search in all the projects for team members, 
				//the user must see all the persons from the projects where he is PM and only his person
				//for the projects where he isn't PM
				List<Project> userProjects = null;
				if (!searchTeamMemberBean.isHasPermissionToSeeAllProjects()) {
					if (!searchTeamMemberBean.isOnlyManager()) {
						userProjects = BLProject.getInstance().getProjectsByPerson(searchTeamMemberBean.getPersonId(), searchTeamMemberBean.isIncludeFinishedAndAbandonedProjects());
					} else {
						userProjects = BLProject.getInstance().getProjectsByManager(searchTeamMemberBean.getPersonId(), searchTeamMemberBean.isIncludeFinishedAndAbandonedProjects());
					}
				} else {
					userProjects = BLProject.getInstance().getAllProjects(searchTeamMemberBean.getOrganizationId(), searchTeamMemberBean.isIncludeFinishedAndAbandonedProjects());
				}
				if (userProjects != null && !userProjects.isEmpty()) {
					List<Integer> projectIds = new ArrayList<Integer>();
					for (Project prj : userProjects) {
						projectIds.add(prj.getProjectId());
					}
					dcOnProject.add(Restrictions.in("projectId", projectIds));
					dcCountOnProject.add(Restrictions.in("projectId", projectIds));
				} else {
					return new ArrayList<TeamMember>();
				}
			} else {
				//if a project is selected, the team members project team must correspond to that project
				dcOnProject.add(Restrictions.eq("projectId", searchTeamMemberBean.getProjectId()));
				dcCountOnProject.add(Restrictions.eq("projectId", searchTeamMemberBean.getProjectId()));
			}
		}
		
		if (searchTeamMemberBean.getOrganizationId() != null) {
			dcOnProject.add(Restrictions.eq("organizationId", searchTeamMemberBean.getOrganizationId()));
			dcCountOnProject.add(Restrictions.eq("organizationId", searchTeamMemberBean.getOrganizationId()));
		}
		
		// check if I have to order the results
		if(searchTeamMemberBean.getSortParam() != null && StringUtils.hasLength(searchTeamMemberBean.getSortParam()) ) {	
			if (searchTeamMemberBean.getSortParam().equals("projectName")) {
				if (searchTeamMemberBean.getSortDirection() == IConstant.ASCENDING ) {
					dcOnProject.addOrder(Order.asc("name"));
				} else {
					dcOnProject.addOrder(Order.desc("name"));
				}
			} else {
				// if I have to, check if I have to order them ascending or descending
				if (searchTeamMemberBean.getSortDirection() == IConstant.ASCENDING ) {
					// ascending
					dc.addOrder(Order.asc(searchTeamMemberBean.getSortParam()));
				} else {
					// descending
					dc.addOrder(Order.desc(searchTeamMemberBean.getSortParam()));
				}
			}
		}
	
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of result and pages
		if (searchTeamMemberBean.getNbrOfResults() == -1){
			// set the countDistinct restriction
			dcCount.setProjection(Projections.countDistinct("memberId"));			
			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			searchTeamMemberBean.setNbrOfResults(nbrOfResults);
			logger.debug("NbrOfResults " + searchTeamMemberBean.getNbrOfResults());
			logger.debug("----> searchPersonBean.getResults " + searchTeamMemberBean.getResultsPerPage());
			// get the number of pages
			if (nbrOfResults % searchTeamMemberBean.getResultsPerPage() == 0) {
				searchTeamMemberBean.setNbrOfPages(nbrOfResults / searchTeamMemberBean.getResultsPerPage());
			} else {
				searchTeamMemberBean.setNbrOfPages(nbrOfResults / searchTeamMemberBean.getResultsPerPage() + 1);
			}
			searchTeamMemberBean.setCurrentPage(1);
		}
		
		List<TeamMember> res = getHibernateTemplate().findByCriteria(dc, (searchTeamMemberBean.getCurrentPage()-1) * searchTeamMemberBean.getResultsPerPage(), searchTeamMemberBean.getResultsPerPage());
    	
    	logger.debug("getFromSearchSimpleWithPagination - END");
    	return res;
    }
	
}
