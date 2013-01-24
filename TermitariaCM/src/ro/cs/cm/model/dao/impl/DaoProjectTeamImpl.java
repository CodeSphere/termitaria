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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.cm.business.BLPerson;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.common.IModelConstant;
import ro.cs.cm.common.Tools;
import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.model.dao.DaoBeanFactory;
import ro.cs.cm.model.dao.IDaoProjectTeam;
import ro.cs.cm.model.dao.IDaoTeamMember;
import ro.cs.cm.om.Person;

/**
 * Dao class for ProjectTeam Entity
 * 
 * @author Coni
 * @author Adelina
 */
public class DaoProjectTeamImpl extends HibernateDaoSupport implements IDaoProjectTeam {
	
	private static IDaoTeamMember teamMemberDao = DaoBeanFactory.getInstance().getDaoTeamMember();
	
	/**
	 * Get the projectTeam identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectTeam get(Integer projectId, boolean withExternalPerson) {
		logger.debug("get - START - project with id = ".concat(Integer.toString(projectId)));
		
		List<ProjectTeam> projectTeams = new ArrayList<ProjectTeam>();
		
		DetachedCriteria dc =  DetachedCriteria.forEntityName(IModelConstant.projectTeamAllEntity);
		dc.add(Restrictions.eq("projectId", projectId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_TEAM_STATUS_DELETED));
		projectTeams = getHibernateTemplate().findByCriteria(dc);
		ProjectTeam projectTeam = null;
		
		if(projectTeams != null && projectTeams.size() > 0) {
			projectTeam = projectTeams.get(0);		
			List<TeamMember> members = new ArrayList<TeamMember>();
			
			DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
			if(!withExternalPerson) {
				dc1.add(Restrictions.ne("personId", new Integer(0)));
			}
			dc1.add(Restrictions.eq("projectTeamId", projectTeam.getProjectTeamId()));
			dc1.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));
			members = getHibernateTemplate().findByCriteria(dc1);			
			Tools.getInstance().printList(logger, members);
					
			logger.debug("projectTeam members =  " + members);
			
			Set<Person> persons = new HashSet<Person>();
			
			Set<TeamMember> teamMembers = new HashSet<TeamMember>();
			
			for(TeamMember teamMember: members) {
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
							Person user = new Person();
							user.setPersonId(teamMember.getPersonId());
							user.setFirstName(teamMember.getFirstName());
							user.setLastName(teamMember.getLastName());
							user.setEmail(teamMember.getEmail());
							persons.add(user);
							logger.debug("teamMember = " + teamMember);
							logger.debug("person = " + user);
							teamMembers.add(teamMember);
						} else {
							if(person != null) {
								teamMemberDao.changeStatus(teamMember, person.getStatus());
							}
						}
					} catch (BusinessException e) {			
						e.printStackTrace();
					}											
				}								
			}
			projectTeam.setPersons(persons);
			projectTeam.setTeamMembers(teamMembers);
			logger.debug("projectTeam persons = " + persons);
		}
		logger.debug("get - END");
		return projectTeam;
	}
			
	/**
	 * Adds a projectTeam to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeam
	 */
	public void add(ProjectTeam projectTeam, List<TeamMember> externalMembers) {
		logger.debug("add - START");
		getHibernateTemplate().save(IModelConstant.projectTeamAllEntity, projectTeam);
		Set<Person> persons = projectTeam.getPersons();
		Set<Person> newPersons = new HashSet<Person>();
		Set<TeamMember> teamMembers = new HashSet<TeamMember>();
		logger.debug("project persons = " + persons);
		if(persons != null && persons.size() > 0) {
			for(Person person : persons) {
				if(person != null) {
//					Person user = null;
//					try {
//						user = BLPerson.getInstance().get(person.getPersonId());
//					} catch (BusinessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					logger.debug("user = " + user);
//					if(user != null && user.getStatus() == IConstant.NOM_PERSON_STATUS_DELETED) {					
//						continue;
//					}
					newPersons.add(person);
					TeamMember member = new TeamMember();
					member.setProjectTeamId(projectTeam.getProjectTeamId());
					member.setPersonId(person.getPersonId());
					member.setFirstName(person.getFirstName());
					member.setLastName(person.getLastName());
					member.setEmail(person.getEmail());
					member.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED);
					teamMembers.add(member);
					projectTeam.setTeamMembers(teamMembers);
					teamMemberDao.add(member);
				}
			}
		}
		
		// set the new persons
		projectTeam.setPersons(newPersons);
		
		logger.debug("externalMembers = " + externalMembers);
		
		// external team members
		for(TeamMember teamMember : externalMembers) {
			logger.debug("teamMember = " + teamMember);
			logger.debug("projectTeamId = " + projectTeam.getProjectTeamId());
			teamMember.setProjectTeamId(projectTeam.getProjectTeamId());			
			teamMemberDao.add(teamMember);
		}		
		
		logger.debug("add - END");
	}
		
		
	/**
	 * Updates a projectTeam to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeam
	 */
	public void update(ProjectTeam projectTeam, List<TeamMember> externalMembers) {
		logger.debug("update - START");
						
		Set<Person> persons = projectTeam.getPersons();	
		
		Set<Person> newPersons = new HashSet<Person>();
		
		Set<TeamMember> teamMembers = new HashSet<TeamMember>();
									
		List<TeamMember> members = new ArrayList<TeamMember>();
				
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
		dc.add(Restrictions.eq("projectTeamId", projectTeam.getProjectTeamId()));	
		dc.add(Restrictions.ne("personId", new Integer(0)));
		members = getHibernateTemplate().findByCriteria(dc);
		logger.debug("members = " + members);
		logger.debug("project persons = " + persons);
		
				
		if(persons != null && persons.size() > 0) {
								
			for(Person person: persons) {				
				if(person != null && person.getPersonId() != -1) {
					
//					Person user = null;
//					try {
//						user = BLPerson.getInstance().get(person.getPersonId());
//					} catch (BusinessException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					logger.debug("user = " + user);
//					if(user != null && user.getStatus() == IConstant.NOM_PERSON_STATUS_DELETED) {						
//						continue;
//					}
					
					newPersons.add(person);
					
					TeamMember teamMember = teamMemberDao.getMember(person.getPersonId(), projectTeam.getProjectTeamId());
					logger.debug("teamMember = " + teamMember);
					boolean same = members.contains(teamMember);
					logger.debug("same = " + same);
					if(same) {		
						if(teamMember != null && teamMember.getStatus() != IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED) {							
							teamMemberDao.changeStatusToOpen(teamMember);
						} 
						members.remove(teamMember);						
						teamMembers.add(teamMember);
						logger.debug("members = " + members);
						logger.debug("teamMembers = " + teamMembers);	
						
					} else {								
						TeamMember member = new TeamMember();
						member.setProjectTeamId(projectTeam.getProjectTeamId());							
						member.setPersonId(person.getPersonId());
						member.setFirstName(person.getFirstName());
						member.setLastName(person.getLastName());
						member.setEmail(person.getEmail());
						member.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED);					
						teamMembers.add(member);
						projectTeam.setTeamMembers(teamMembers);
						teamMemberDao.add(member);					
					}
				}
			}	
		}	
		
		// set the persons
		projectTeam.setPersons(newPersons);
		logger.debug("externalMembers = " + externalMembers);
		if(externalMembers != null && externalMembers.size() > 0) {
			for(TeamMember external : externalMembers) {
				logger.debug("external = " + external);
				if(external.getMemberId() > 0) {
					TeamMember teamMember = teamMemberDao.getByMemberId(external.getMemberId());
					if(external.getFirstName().isEmpty() && external.getLastName().isEmpty()) {						
						teamMemberDao.changeStatusToDelete(teamMember);
					} else {
						if(teamMember.compareTo(external) != 0) {							
							teamMemberDao.update(external);
						} 
					}
				} else {
					teamMemberDao.add(external);
				}
			}
		}
		
		// set the team members
		projectTeam.setTeamMembers(new HashSet<TeamMember>(teamMembers));
			
		logger.debug("teamMembers = " + teamMembers);
		
		logger.debug("members = " + members);
		if(members != null && members.size() > 0) {
			for(TeamMember member: members) {					
				teamMemberDao.changeStatusToDelete(member);
			}
		}
									
		getHibernateTemplate().update(IModelConstant.projectTeamAllEntity, projectTeam);
						
		logger.debug("update - END");
	}
	
	/**
	 * Checks if a project has associated a project team
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 */
	public boolean hasProjectTeam(Integer projectId) {
		logger.debug("hasProjectTeam - START");
		List<ProjectTeam> projectTeams = new ArrayList<ProjectTeam>();
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectTeamEntity);
		dc.add(Restrictions.eq("projectId", projectId));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_TEAM_STATUS_DELETED));
		projectTeams = getHibernateTemplate().findByCriteria(dc);
		logger.debug("hasProjectTeam - END");
		if(projectTeams.size() > 0) {
			return true;
		} else {
			return false;
		}			
	}
	
	/**
	 * Get the projectTeam for the delete action
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeamId
	 * @return
	 */
	public ProjectTeam getForDelete(Integer projectTeamId) {
		logger.debug("getForDelete - START ");
		ProjectTeam projectTeam = (ProjectTeam)getHibernateTemplate().get(IModelConstant.projectTeamAllEntity, new Integer(projectTeamId));
		logger.debug("getForDelete - END");
		return projectTeam;
	}
	

	/**
	 * Deletes a project team with all the data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeamId
	 * @return
	 */
	public ProjectTeam deleteAll(Integer projectTeamId) {
		logger.debug("deleteAll - START");
		logger.debug("Deleting projectTeam with id: ".concat(projectTeamId.toString()));
		ProjectTeam projectTeam = getForDelete(projectTeamId);
		projectTeam.setStatus(IConstant.NOM_PROJECT_TEAM_STATUS_DELETED);
		Set<TeamMember> members = projectTeam.getTeamMembers();
		for(TeamMember member : members) {
			member.setStatus(IConstant.NOM_TEAM_MEMBER_STATUS_DELETED);
		}
		logger.debug("Deleting the projectTeam : " + projectTeam);
		getHibernateTemplate().update(IModelConstant.projectTeamAllEntity, projectTeam);
		logger.debug("Project Team " + projectTeam + " has been deleted");
		logger.debug("deleteAll  - END");
		return projectTeam;
	}
	
	
	/**
	 * Gets all the project teams that have as a team member the person with the specified id 
	 * @author Coni
	 * @param personId
	 * @return
	 */
	public List<ProjectTeam> getByPerson(Integer personId) {
		logger.debug("getByTeamMember - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.projectTeamAllEntity);
		List<Integer> projectTeamIds = new ArrayList<Integer>();
		List<TeamMember> members = teamMemberDao.getByPersonId(personId, true);
		for (TeamMember member : members) {
			projectTeamIds.add(new Integer(member.getProjectTeamId()));
		}
		dc.add(Restrictions.in("projectTeamId", projectTeamIds));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_TEAM_STATUS_DELETED));
		List<ProjectTeam> projectTeams = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByTeamMember - END");
		return projectTeams;
	}
	
	/**
	 * Get the projectTeam identified by it's id with team members from inside and outside of the organization
	 * 
	 * @author Coni
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectTeam getWithAllTeamMembers(Integer projectId, boolean isNotDeleted) {
		logger.debug("getWithAllTeamMembers - START - project with id = ".concat(Integer.toString(projectId)));
		
		List<ProjectTeam> projectTeams = new ArrayList<ProjectTeam>();
		
		DetachedCriteria dc =  DetachedCriteria.forEntityName(IModelConstant.projectTeamAllEntity);
		dc.add(Restrictions.eq("projectId", projectId));	
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_TEAM_STATUS_DELETED));
			
		projectTeams = getHibernateTemplate().findByCriteria(dc);
		ProjectTeam projectTeam = null;
		
		if(projectTeams != null && projectTeams.size() > 0) {
			projectTeam = projectTeams.get(0);		
			List<TeamMember> members = new ArrayList<TeamMember>();
			
			DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.teamMemberEntity);
			dc1.add(Restrictions.eq("projectTeamId", projectTeam.getProjectTeamId()));
			logger.debug("isNotDeleted = " + isNotDeleted);
			if(isNotDeleted) {
				dc1.add(Restrictions.ne("status", IConstant.NOM_TEAM_MEMBER_STATUS_DELETED));							
			}
			members = getHibernateTemplate().findByCriteria(dc1);
				
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
							
							logger.debug("changeStatus = " + changeStatus);					
							if(!changeStatus) {									
								logger.debug("teamMember = " + teamMember);					
								teamMembers.add(teamMember);
							} else {
								if(person != null) {
									teamMemberDao.changeStatus(teamMember, person.getStatus());
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
			projectTeam.setTeamMembers(new HashSet<TeamMember>(teamMembers));										
		}
		logger.debug("getWithAllTeamMembers - END");
		return projectTeam;			
	}
	
	/**
	 * Gets a list of project teams (without the team members) identified by the corresponding projectTeamIds
	 * @author Coni
	 * @param projectTeamIds
	 * @return
	 */
	public List<ProjectTeam> getByProjectTeamIds(List<Integer> projectTeamIds) {
		logger.debug("getByProjectTeamIds - START");
		List<ProjectTeam> projectTeams = new ArrayList<ProjectTeam>();
		
		DetachedCriteria dc =  DetachedCriteria.forEntityName(IModelConstant.projectTeamEntity);
		dc.add(Restrictions.in("projectTeamId", projectTeamIds));
		dc.add(Restrictions.ne("status", IConstant.NOM_PROJECT_TEAM_STATUS_DELETED));
		projectTeams = getHibernateTemplate().findByCriteria(dc);
	
		logger.debug("getByProjectTeamIds - END");
		return projectTeams;
	}
	
}
