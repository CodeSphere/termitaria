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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;

import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.entity.SearchPersonBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.om.Person;
import ro.cs.ts.ws.client.cm.CMWebServiceClient;
import ro.cs.ts.ws.client.cm.entity.GetTeamMemberFromSearchResponse;
import ro.cs.ts.ws.client.cm.entity.WSSearchTeamMemberBean;
import ro.cs.ts.ws.client.cm.entity.WSTeamMember;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.GetPersonFromSearchResponse;
import ro.cs.ts.ws.client.om.entity.UserSimple;
import ro.cs.ts.ws.client.om.entity.WSSearchPersonBean;


/**
 * Singleton which expose business methods for Person item
 * 
 * @author Adelina
 *
 */
public class BLPerson extends BusinessLogic {
			
	// singleton implementation
	private static BLPerson theInstance = null;
	
	private BLPerson() {
		
	}
	
	static {
		theInstance = new BLPerson();
	}
	
	public static BLPerson getInstance() {
		return theInstance;
	}
	
	 /**
     * List persons for pagination
     * 
     * @author Adelina
     * @author Coni
     * 
     * @param searchPersonBean
     * @param organizationid
     * @return
     * @throws BusinessException
     */
    public List<Person> listPersonsForPagination(SearchPersonBean searchPersonBean, Integer organizationId, Integer personId, boolean hasPermission, MessageSource messageSource, boolean onlyManager) throws BusinessException {
    	logger.debug("listPersonsForPagination - START");
    	
    	List<Person> persons = new ArrayList<Person>();
    	
    	logger.debug("searchPersonBean =" + searchPersonBean);
    	
    	try {
    		//if the user has selected to search for organization and it has the permission,
    		//i will retrieve all the users from the organization with the specified first and last name 
    		if (searchPersonBean.getProjectId() != null && searchPersonBean.getProjectId().equals(Integer.valueOf(IConstant.NOM_PERSON_SEARCH_IN_ORGANIZATION)) && hasPermission) {
    			logger.debug("Retrieve all the persons from the organization with id: ".concat(String.valueOf(organizationId)));
    			searchPersonBean.setOrganizationId(organizationId);
    			persons = getPersonsFromSearch(searchPersonBean);
    		} else if (searchPersonBean.getProjectId() != null) {
    			logger.debug("Retrieve all the members from the project with id: (0 means all the project)".concat(String.valueOf(searchPersonBean.getProjectId())));
    			persons = getTeamMemberFromSearch(searchPersonBean, personId, hasPermission, true, onlyManager);
    		}    		 
    	} catch (Exception e) {	
			throw new BusinessException(ICodeException.PERSON_LIST, e);
		}
    	
    	logger.debug("listPersonsForPagination - END");
    	
    	return persons;
    }   
    
    /**
     * Retrieves the persons from OM using the specified search criteria, order and pagination
     * @author Coni
     * @param searchPersonBean
     * @return
     * @throws BusinessException
     */
    public List<Person> getPersonsFromSearch(SearchPersonBean searchPersonBean) throws BusinessException {
    	logger.debug("getPersonsFromSearch - START");
    	List<Person> result = new ArrayList<Person>();
    	try {
    		GetPersonFromSearchResponse getPersonFromSearchResponse = OMWebServiceClient.getInstance().getPersonFromSearch(searchPersonBean);
    		//set the pagination searchPersonBean attributes from the corresponding OM bean retrieved from WS
    		WSSearchPersonBean wsSearchPersonBean = getPersonFromSearchResponse.getWsSearchPersonBean();
    		searchPersonBean.setCurrentPage(wsSearchPersonBean.getCurrentPage());
    		searchPersonBean.setLowerLimit(wsSearchPersonBean.getLowerLimit());
    		searchPersonBean.setNbrOfPages(wsSearchPersonBean.getNbrOfPages());
    		searchPersonBean.setNbrOfResults(wsSearchPersonBean.getNbrOfResults());
    		searchPersonBean.setResultsPerPage(wsSearchPersonBean.getResultsPerPage());
    		searchPersonBean.setUpperLimit(wsSearchPersonBean.getUpperLimit());
    		//set the person search results
    		List<UserSimple> persons = getPersonFromSearchResponse.getPersons();
    		if (persons != null && !persons.isEmpty()) {
	    		for (UserSimple pers : persons) {
	    			Person person = new Person();
		    		person.setPersonId(pers.getUserId());
		    		person.setFirstName(pers.getFirstName());
		    		person.setEmail(pers.getEmail());
		    		person.setLastName(pers.getLastName());	 
		    		person.setMemberId(new Integer(-1));	    		
		    		person.setProjectId(new Integer(0));	 	    		
		    		result.add(person);
	    		}
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PERSON_GET_PERSON_FROM_SEARCH, e);
    	}
    	logger.debug("getPersonsFromSearch - END");
    	return result;
    }
    
    /**
     * Retrieves team members from CM using the specified criteria and pagination, order and pagination
     * @author Coni
     * @param searchPersonBean
     * @return
     * @throws BusinessException
     */
    public List<Person> getTeamMemberFromSearch(SearchPersonBean searchPersonBean, Integer personId, boolean hasPermissionToSeeAllProjects, boolean includeFinishedAndAbandonedProjects, boolean onlyManager) throws BusinessException {
    	logger.debug("getPersonsFromSearch - START");
    	List<Person> result = new ArrayList<Person>();
    	try {
    		GetTeamMemberFromSearchResponse getTeamMemberFromSearchResponse = CMWebServiceClient.getInstance().getTeamMemberFromSearch(searchPersonBean, personId, hasPermissionToSeeAllProjects, includeFinishedAndAbandonedProjects, onlyManager);
    		//set the pagination searchPersonBean attributes from the corresponding OM bean retrieved from WS
    		WSSearchTeamMemberBean wsSearchTeamMemberBean = getTeamMemberFromSearchResponse.getWsSearchTeamMemberBean();
    		searchPersonBean.setCurrentPage(wsSearchTeamMemberBean.getCurrentPage());
    		searchPersonBean.setLowerLimit(wsSearchTeamMemberBean.getLowerLimit());
    		searchPersonBean.setNbrOfPages(wsSearchTeamMemberBean.getNbrOfPages());
    		searchPersonBean.setNbrOfResults(wsSearchTeamMemberBean.getNbrOfResults());
    		searchPersonBean.setResultsPerPage(wsSearchTeamMemberBean.getResultsPerPage());
    		searchPersonBean.setUpperLimit(wsSearchTeamMemberBean.getUpperLimit());
    		//set the person search results
    		List<WSTeamMember> teamMembers = getTeamMemberFromSearchResponse.getTeamMembers();
    		if (teamMembers != null && !teamMembers.isEmpty()) {
	    		for (WSTeamMember member : teamMembers) {
	    			Person person = new Person();
		    		person.setPersonId(member.getPersonId());
		    		person.setFirstName(member.getFirstName());
		    		person.setEmail(member.getEmail());
		    		person.setLastName(member.getLastName());	 
		    		person.setMemberId(member.getMemberId());	    		
		    		person.setProjectId(member.getProjectTeam().getParentId());	 
		    		
		    		Project project = new Project();
		    		project.setProjectId(member.getProjectTeam().getProject().getProjectId());
		    		project.setStatus(member.getProjectTeam().getProject().getStatus());
		    		project.setName(member.getProjectTeam().getProject().getName());
		    		project.setManagerId(member.getProjectTeam().getProject().getManagerId());
		    		person.setProject(project);
		    		person.setHasManager(personId == project.getManagerId()?true:false);	
		    		result.add(person);
	    		}
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PERSON_GET_PERSON_FROM_SEARCH, e);
    	}
    	logger.debug("getPersonsFromSearch - END");
    	return result;
    }
    
    /**
     * Gets persons from organization and external persons (those that belong to projects by
     * are not in the organization)
     * 
     * @author Adelina
     * 
     * @param projectIds
     * @param organizationId
     * @param isExternal
     * @return
     */
    public List<Person> getPersonsFromOrganizationAndExternal(Integer personId, HashSet<Integer> projectIds, Integer organizationId, boolean withDeleted) throws BusinessException {
    	logger.debug("getPersonsFromOrganizationAndExternal - START");
    	
    	List<Person> persons = new ArrayList<Person>();
    	
    	// users from organization
    	
    	List<UserSimple> users = null;
    	
    	if(withDeleted) {
    		users = BLUser.getInstance().getUsersSimpleByOrganizationId(organizationId, false);
    	} else {
    		users = BLUser.getInstance().getUsersSimpleByOrganizationId(organizationId, true);
    	}
    	logger.debug("users by organization = " + users);
    	
    	if(users != null && users.size() > 0) {
	    	for(UserSimple user : users) {
	    		// list of projects the person belongs to, as a member of the project
	    		List<Project> projects = new ArrayList<Project>();	 
	    		HashMap<Project, TeamMember> projectTeamMember = new HashMap<Project, TeamMember>();
	    		if(withDeleted) {
	    			projectTeamMember = BLProject.getInstance().getProjectsAndTeamMembersByPerson(user.getUserId(), false, true);
	    		} else {
	    			projectTeamMember = BLProject.getInstance().getProjectsAndTeamMembersByPerson(user.getUserId(), true, true);
	    		}
	    		
	    		Iterator it = projectTeamMember.entrySet().iterator();
	    		while (it.hasNext()) {
					Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();
					Project project = new Project();
					project.setProjectId(entry.getKey().getProjectId());
					project.setStatus(entry.getKey().getStatus());
					project.setManagerId(entry.getKey().getManagerId());
					project.setName(entry.getKey().getName());
					
					projects.add(project);					
				}
	    		logger.debug("projects = " + projects);
	    		if(projects != null && projects.size() > 0) {
		    		for(Project prjct : projects) {
			    		Person person = new Person();
			    		person.setPersonId(user.getUserId());
			    		person.setFirstName(user.getFirstName());
			    		person.setLastName(user.getLastName());	  
			    		person.setEmail(user.getEmail());
		    				    				    			    					    					    			    					    
			    		if(projectTeamMember.get(prjct) != null) {			    		
				    		person.setMemberId(projectTeamMember.get(prjct).getMemberId());
			    		} else {
			    			person.setMemberId(new Integer(-1));
			    		}			    		
			    		person.setProjectId(prjct.getProjectId());
			    		person.setProject(prjct);
			    		persons.add(person);
		    		}
	    		} 	    			
	    		Person person = new Person();
	    		person.setPersonId(user.getUserId());
	    		person.setFirstName(user.getFirstName());
	    		person.setEmail(user.getEmail());
	    		person.setLastName(user.getLastName());	 
	    		person.setMemberId(new Integer(-1));	    		
	    		person.setProjectId(new Integer(0));	 	    		
	    		persons.add(person);
	    	}
    	}
    	
    	logger.debug("persons size = " + persons.size() + " persons = " + persons);
    	    	    	
    	// the external persons
    	if(projectIds != null && projectIds.size() > 0) {
    		List<TeamMember> members = null;
    		if(withDeleted) {
    			members = BLTeamMember.getInstance().getTeamMembersByProjectIds(projectIds, true, false);
    		} else {
    			members = BLTeamMember.getInstance().getTeamMembersByProjectIds(projectIds, true, true);
    		}
    		List<Project> projects = BLProject.getInstance().getProjectsSimpleByProjectIds(projectIds);
	    	logger.debug("members = " + members);
	    	
	    	if(members != null && members.size() > 0) {
	    		for(TeamMember member : members) {    			
	    			Person person = new Person();
	    			person.setPersonId(member.getPersonId());
		    		person.setMemberId(member.getMemberId());
		    		person.setFirstName(member.getFirstName());
		    		person.setLastName(member.getLastName());		    	
		    		person.setEmail(member.getEmail());
		    		Integer projectId = member.getProjectTeam().getProjectId();
//		    		Project project = BLProject.getInstance().getSimpleProject(projectId);
		    		person.setProjectId(projectId);
		    		for (Project prj : projects) {
		    			if (projectId.equals(prj.getProjectId())) {
				    		person.setProject(prj);	    		
				    		person.setHasManager(personId == prj.getManagerId()?true:false);	
		    			}
		    		}
		    		persons.add(person);
	    		}
	    	}
    	}
    	logger.debug("persons size = " + persons.size() + " persons = " + persons);
    	
    	logger.debug("getPersonsFromOrganizationAndExternal - END");
    	return persons;
    }
    
    /**
     * Gets persons from projects
     * 
     * @author Adelina
     * 
     * @param projectIds
     * @param isExternal
     * @return
     * @throws BusinessException
     */
    public List<Person> getPersonsFromProjects(Integer personId, HashSet<Integer> projectIds, boolean isExternal, boolean isNotDeleted) throws BusinessException {
    	logger.debug("getPersonsFromProjects - START");
    	
    	ArrayList<Person> persons = new ArrayList<Person>();
    	if(projectIds != null && projectIds.size() > 0) {    	    	
	    	List<TeamMember> members = BLTeamMember.getInstance().getTeamMembersByProjectIds(projectIds, isExternal, isNotDeleted);
	    	List<Project> projects = BLProject.getInstance().getProjectsSimpleByProjectIds(projectIds);
	    	logger.debug("members = " + members);
	    	if(members != null && members.size() > 0) {
	    		for(TeamMember member : members) {  
	    			Person person = new Person();
		    		person.setPersonId(member.getPersonId());
		    		person.setMemberId(member.getMemberId());
		    		person.setFirstName(member.getFirstName());
		    		person.setLastName(member.getLastName());	
		    		person.setEmail(member.getEmail());
		    		Integer projectId = member.getProjectTeam().getProjectId();
//		    		Project project = BLProject.getInstance().getSimpleProject(projectId);
		    		person.setProjectId(projectId);
		    		for (Project prj : projects) {
		    			if (projectId.equals(prj.getProjectId())) {
				    		person.setProject(prj);	    		
				    		person.setHasManager(personId == prj.getManagerId()?true:false);	
		    			}
		    		}
		    		persons.add(person);	    		
		    		logger.debug("persons =  " + persons);
	    		}
	    	}
    	}
    	    	    	
    	logger.debug("getPersonsFromProjects - END");
    	return persons;
    }      	
}
