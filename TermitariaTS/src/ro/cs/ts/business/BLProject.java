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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.ts.cm.Client;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.ProjectTeam;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.SearchProjectBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.om.Person;
import ro.cs.ts.utils.ProjectComparator;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.cm.CMWebServiceClient;
import ro.cs.ts.ws.client.cm.entity.WSClient;
import ro.cs.ts.ws.client.cm.entity.WSProject;
import ro.cs.ts.ws.client.cm.entity.WSTeamMember;

public class BLProject extends BusinessLogic {
	
	//singleton implementation
    private static BLProject theInstance = null;
  
    private BLProject(){};
    static {
        theInstance = new BLProject();
    }
    public static BLProject getInstance() {
    	return theInstance;
    }
    
    /**
	 * Get all the projects by organizationId
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
    public List<Project> getAllProjects(Integer organizationId, boolean isFinishedAndAbandoned) throws BusinessException {
    	logger.debug("getAllProjects - START");
    	List<Project> projects = new ArrayList<Project>();
    	try{
    		List<WSProject> wsProjects = CMWebServiceClient.getInstance().getAllProjects(organizationId, isFinishedAndAbandoned);
    		if(wsProjects != null && wsProjects.size() > 0) {
    			for(WSProject wsProject : wsProjects) {
    				Project project = new Project();
        			project.setProjectId(wsProject.getProjectId());
        			project.setName(wsProject.getName());
        			project.setOrganizationId(wsProject.getOrganizationId());
        			project.setClientId(wsProject.getClientId());
        			project.setManagerId(wsProject.getManagerId());
        			project.setStatus(wsProject.getStatus());  
        			
        			Client client = new Client();
        			
        			if(wsProject.getClient() != null) {
	        			client.setClientId(wsProject.getClient().getClientId());        			
	        			client.setC_name(wsProject.getClient().getC_name());
	        			client.setP_firstName(wsProject.getClient().getP_firstName());
	        			client.setP_lastName(wsProject.getClient().getP_lastName());
	        			client.setType(wsProject.getClient().getType());
	        			client.setStatus(wsProject.getClient().getStatus());	        			
        			}
        			
    				project.setClient(client);
    				
    				Person manager = new Person();
    				
    				if(wsProject.getManager() != null) {
	    				manager.setPersonId(wsProject.getManager().getUserId());
	    				manager.setFirstName(wsProject.getManager().getFirstName());
	    				manager.setLastName(wsProject.getManager().getLastName());
    				}
        			
    				project.setManager(manager);
        			
        			projects.add(project);
    			}    			
    		}
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_ALL_PROJECTS, e);
		}
    	logger.debug("getAllProjects - END");
    	return projects;
    }
    
    /**
   	 * Get all the projects by organizationId
   	 * 
   	 * @author Adelina
   	 * 
   	 * @param organizationId
   	 * @return
   	 */
     public List<Project> getProjectsByUserId(Integer personId, boolean isFinishedAndAbandoned) throws BusinessException {
       	logger.debug("getAllProjects - START");
       	List<Project> projects = new ArrayList<Project>();
       	try{
       		List<WSProject> wsProjects = CMWebServiceClient.getInstance().getProjectsByPerson(personId, true, true, false);
       		if(wsProjects != null && wsProjects.size() > 0) {
       			for(WSProject wsProject : wsProjects) {
       				Project project = new Project();
           			project.setProjectId(wsProject.getProjectId());
           			project.setName(wsProject.getName());
           			project.setOrganizationId(wsProject.getOrganizationId());
           			project.setClientId(wsProject.getClientId());
           			project.setManagerId(wsProject.getManagerId());
           			project.setStatus(wsProject.getStatus());  
           			
           			Client client = new Client();
           			
           			if(wsProject.getClient() != null) {
   	        			client.setClientId(wsProject.getClient().getClientId());        			
   	        			client.setC_name(wsProject.getClient().getC_name());
   	        			client.setP_firstName(wsProject.getClient().getP_firstName());
   	        			client.setP_lastName(wsProject.getClient().getP_lastName());
   	        			client.setType(wsProject.getClient().getType());
   	        			client.setStatus(wsProject.getClient().getStatus());	        			
           			}
           			
       				project.setClient(client);
       				
       				Person manager = new Person();
       				
       				if(wsProject.getManager() != null) {
   	    				manager.setPersonId(wsProject.getManager().getUserId());
   	    				manager.setFirstName(wsProject.getManager().getFirstName());
   	    				manager.setLastName(wsProject.getManager().getLastName());
       				}
           			
       				project.setManager(manager);
           			
           			projects.add(project);
       			}    			
       		}
       	} catch (Exception e) {
   			throw new BusinessException(ICodeException.PROJECT_GET_ALL_PROJECTS, e);
   		}
       	logger.debug("getAllProjects - END");
       	return projects;
       }
    
    /**
     * Get clients for project
     * 
     * @author Adelina
     * 
     * @param organizationId
     * @return
     * @throws BusinessException
     */
    public List<Client> getClientsForProject(Integer organizationId) throws BusinessException {
    	logger.debug("getClientsForProject - START");
    	
    	List<Client> clients = new ArrayList<Client>();
    	
    	try{
    		List<WSClient> wsClients = CMWebServiceClient.getInstance().getClientsForProject(organizationId);
    		if(wsClients != null && wsClients.size()  > 0) {
    			for(WSClient wsClient : wsClients) {
    				Client client = new Client();
    				client.setClientId(wsClient.getClientId());
    				client.setC_name(wsClient.getC_name());
    				client.setP_firstName(wsClient.getP_firstName());
    				client.setP_lastName(wsClient.getP_lastName());
    				client.setType(wsClient.getType());
    				client.setStatus(wsClient.getStatus());
    				
    				clients.add(client);
    			}
    		}
    		
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_GET_ALL_CLIENTS, e);
		}
    	
    	logger.debug("getClientsForProject - END");
    	return clients;
    }
    
    /**
     * Get projects by manager
     * 
     * @author Adelina
     * 
     * @param managerId
     * @return
     * @throws BusinessException
     */
    public List<Project> getProjectsByManager(Integer managerId, boolean onlyManager, boolean isFinishedAndAbandoned) throws BusinessException {
    	logger.debug("getProjectsByManager - START");
    	
    	List<Project> projects = new ArrayList<Project>();
    	
    	try{
    		List<WSProject> wsProjects = CMWebServiceClient.getInstance().getProjectsByManager(managerId, onlyManager, isFinishedAndAbandoned);
    		if(wsProjects != null && wsProjects.size() > 0) {
    			for(WSProject wsProject : wsProjects) {
    				Project project = new Project();
        			project.setProjectId(wsProject.getProjectId());
        			project.setName(wsProject.getName());
        			project.setOrganizationId(wsProject.getOrganizationId());
        			project.setClientId(wsProject.getClientId());
        			project.setManagerId(wsProject.getManagerId());
        			project.setStatus(wsProject.getStatus());   
        			
        			Client client = new Client();
        			
        			if(wsProject.getClient() != null) {
	        			client.setClientId(wsProject.getClient().getClientId());        			
	        			client.setC_name(wsProject.getClient().getC_name());
	        			client.setP_firstName(wsProject.getClient().getP_firstName());
	        			client.setP_lastName(wsProject.getClient().getP_lastName());
	        			client.setType(wsProject.getClient().getType());
	        			client.setStatus(wsProject.getClient().getStatus());
        			}
        			
    				project.setClient(client);
    				
    				Person manager = new Person();
    				
    				if(wsProject.getManager() != null) {
	    				manager.setPersonId(wsProject.getManager().getUserId());
	    				manager.setFirstName(wsProject.getManager().getFirstName());
	    				manager.setLastName(wsProject.getManager().getLastName());
    				}
        			
    				project.setManager(manager);
        			
        			projects.add(project);
    			}    			
    		}    	
    	} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECT_GET_BY_MANAGER, e);
		}
    	logger.debug("getProjectsByManager - END");
    	return projects;
    }
    
    /**
     * List projects for pagination
     * 
     * @author Adelina
     * 
     * @param searchProjectBean
     * @param managerId
     * @return
     * @throws BusinessException
     */
    public List<Project> listProjectsForPagination(SearchProjectBean searchProjectBean, Integer managerId, Integer organizationId, MessageSource messageSource) throws BusinessException {
    	logger.debug("listProjectsForPagination - START");
    	
    	List<Project> projects = new ArrayList<Project>();
    	
    	int i = 0;
    	
    	// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	try{
    		List<Project> results = new ArrayList<Project>();
    		
    		if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAdvancedSearch())){			
    			results = getAllProjects(organizationId, true); 
    			System.out.println("all projects");
    		} else {
    			results = getProjectsByManager(managerId, false, true);
    			System.out.println("bymanager");
    		}    		    		
    	
    		List<Project> entries = new ArrayList<Project>();
    		    		    		
    		List<Project> byStatus = new ArrayList<Project>();
    		
    		List<Project> byClient = new ArrayList<Project>();
    		
    		List<Project> byManger = new ArrayList<Project>();
    		
    		// search for name query (lower case)
    		System.out.println("result: " + results.size());
    		for(i = 0; i < results.size(); i++) {
    			System.out.println(results.get(i).getName());		
    			if(results.get(i).getName().toLowerCase().contains(searchProjectBean.getName().toLowerCase())) {     				
    				entries.add(results.get(i));
    			}    				
    				
    			   				
				if(searchProjectBean.getManagerId() != null && searchProjectBean.getManagerId() != -1) {    													
					if(results.get(i).getManagerId().equals(searchProjectBean.getManagerId())) {						
						byManger.add(results.get(i));
					}  else {
						byManger.add(new Project());
	    			}  	    				    									
	    		} 
    	
				
				if(searchProjectBean.getClientId() != null && searchProjectBean.getClientId() != 0) {
	    			if(results.get(i).getClientId().equals(searchProjectBean.getClientId())) {	    				
	    				byClient.add(results.get(i));
	    			}  else {
	    				byClient.add(new Project());
	    			}
	    		}
												
				if(searchProjectBean.getStatus() != -1) {					
	    			if(results.get(i).getStatus() == searchProjectBean.getStatus()) {
	    				byStatus.add(results.get(i));
	    			} else {
	    				byStatus.add(new Project());
	    			}
	    		}
    				
    		}    	
    		    		    		
    		if(byManger.size() > 0) {
	    		if(byClient.size() > 0) {
	    			byManger.retainAll(byClient);	    		
	    		}
	    		
	    		if(byStatus.size() > 0) {
	    			byManger.retainAll(byStatus);	    			
	    		}    		    		    	
    			entries.retainAll(byManger);
    		} else {
    			if(byClient.size() > 0) {
    				entries.retainAll(byClient);	    		
	    		}
    			if(byStatus.size() > 0) {
    				entries.retainAll(byStatus);	    		
	    		}
    		}
    		
    		logger.debug("Entries = " + entries);       			    		    	
    		
    		//------sort the list
			if(searchProjectBean.getSortParam().equals("name")){
				Collections.sort(entries, ProjectComparator.getInstance().nameComparator());
			}
			
			if(searchProjectBean.getSortParam().equals("clientName")){
				Collections.sort(entries, ProjectComparator.getInstance().clientNameComparator(messageSource));
			}
			
			if(searchProjectBean.getSortParam().equals("status")){			
				Collections.sort(entries, ProjectComparator.getInstance().statusComparator());
			}

	
			//ascending or descending
			if(searchProjectBean.getSortDirection() == IConstant.DESCENDING){
				Collections.reverse(entries);
			}
				
			//-----end sorting
    		
			searchProjectBean.setNbrOfResults(entries.size());

			int nbrOfPages = (entries.size() / searchProjectBean.getResultsPerPage()) + (entries.size() % searchProjectBean.getResultsPerPage() > 0 ? 1 : 0); 
			
			searchProjectBean.setNbrOfPages(nbrOfPages);
			
			if(searchProjectBean.getCurrentPage() > nbrOfPages){
				searchProjectBean.setCurrentPage(nbrOfPages);
			}
							
			//if we have search for the first time
			if(searchProjectBean.getCurrentPage() <= 0){
				searchProjectBean.setCurrentPage(1);
			}
			long start = (searchProjectBean.getCurrentPage() - 1) * searchProjectBean.getResultsPerPage();
			long end  = searchProjectBean.getCurrentPage() * searchProjectBean.getResultsPerPage();
			
			Project project = null;
			
			// go over the entries				
			for(i = (int)start ;i < end; i++) {
				if(i >= entries.size()) {
					continue;
				}
				System.out.println(i);
				project = entries.get(i);				
				projects.add(project);
			}
	
			logger.debug("Projects size = " + projects.size() + ", " + projects);
			
    		
    	} catch (Exception e) {	
			throw new BusinessException(ICodeException.PROJECT_LIST, e);
		}
    	
    	logger.debug("listProjectsForPagination - END");
    	return projects;
    } 
    
    
    /**
     * List projects for pagination
     * 
     * @author Adelina
     * 
     * @param searchProjectBean
     * @param managerId
     * @return
     * @throws BusinessException
     */
    public List<Project> listProjects(SearchProjectBean searchProjectBean, Integer managerId, Integer organizationId, MessageSource messageSource) throws BusinessException {
    	logger.debug("listProjectsForPagination - START");
    	
    	List<Project> projects = new ArrayList<Project>();
    	
    	
    	// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	try{
    		
    		//if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ProjectAdvancedSearch()))			
    			projects = getProjectsByUserId(userAuth.getPersonId(), false); 
   		    		
    	
    		
    		
    		logger.debug("Projects = " + projects);       			    		    	
    		
    		

    		
			
			logger.debug("Projects size = " + projects.size() + ", " + projects);
			
    		
    	} catch (Exception e) {	
			throw new BusinessException(ICodeException.PROJECT_LIST, e);
		}
    	
    	logger.debug("listProjectsForPagination - END");
    	return projects;
    }    
    
    /**
     * @author Coni
     * @param personId
     * @return
     * @throws BusinessException
     */
    public HashMap<Project, TeamMember> getProjectsAndTeamMembersByPerson(Integer personId, boolean isNotDeleted, boolean isFinishedAndAbandoned) throws BusinessException {
    	logger.debug("getProjectsAndTeamMembersByPerson - START");
    	HashMap<Project, TeamMember> result = new HashMap<Project, TeamMember>();
    	try {
    		HashMap<WSProject, WSTeamMember> projectsAndTeamMembers = CMWebServiceClient.getInstance().getProjectsAndTeamMembersByPerson(personId, isNotDeleted, isFinishedAndAbandoned);
    		if (projectsAndTeamMembers != null) {
	    		Iterator it = projectsAndTeamMembers.entrySet().iterator();
	    		while (it.hasNext()) {
	    			Map.Entry<WSProject, WSTeamMember> entry = (Map.Entry<WSProject, WSTeamMember>) it.next();
	    			Project project = new Project();
	    			project.setName(entry.getKey().getName());
	    			project.setManagerId(entry.getKey().getManagerId());
	    			project.setOrganizationId(entry.getKey().getOrganizationId());
	    			project.setProjectId(entry.getKey().getProjectId());
	    			project.setStatus(entry.getKey().getStatus());
	    			project.setClientId(entry.getKey().getClientId());
	    			
	    			TeamMember member = new TeamMember();
	    			member.setFirstName(entry.getValue().getFirstName());
	    			member.setLastName(entry.getValue().getLastName());
	    			member.setMemberId(entry.getValue().getMemberId());
	    			member.setPersonId(entry.getValue().getPersonId());
	    			member.setStatus(entry.getValue().getStatus());
	    			logger.debug("member = " + member);
	    			result.put(project, member);
	    		}
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON, e);
    	}
    	logger.debug("getProjectsAndTeamMembersByPerson - START");
    	return result;
    }
    
    /**
     * Get a simple project
     * 
     * @author Adelina
     * 
     * @param projectId
     * @return
     * @throws BusinessException
     */
    public Project getSimpleProject(Integer projectId) throws BusinessException {
    	logger.debug("getSimpleProject - START");
    	Project project = null;
    	try{
    		WSProject wsProject = CMWebServiceClient.getInstance().getProjectByProjectId(projectId, true);
    		if (wsProject != null) {
	    		project = new Project();
				project.setProjectId(wsProject.getProjectId());
				project.setName(wsProject.getName());
				project.setOrganizationId(wsProject.getOrganizationId());
				project.setClientId(wsProject.getClientId());
				project.setManagerId(wsProject.getManagerId());
				project.setStatus(wsProject.getStatus());  
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_GET_SIMPLE, e);
    	}
    	logger.debug("getSimpleProject - END");
    	return project;
    }
   
    /**
     * Gets a list of projects with basic info, by their project ids
     * @author Coni
     * @param projectIds
     * @return
     * @throws BusinessException
     */
    public List<Project> getProjectsSimpleByProjectIds(Set<Integer> projectIds) throws BusinessException {
    	logger.debug("getProjectsSimpleByProjectIds - START");
    	List<Project> projects = new ArrayList<Project>();
    	try {
    		List<WSProject> wsProjects = CMWebServiceClient.getInstance().getProjectsSimpleByProjectIds(projectIds);
    		if (wsProjects != null) {
	    		for (WSProject wsProject : wsProjects) {
	    			Project project = new Project();
	    			project.setManagerId(wsProject.getManagerId());
	    			project.setName(wsProject.getName());
	    			project.setStatus(wsProject.getStatus());  
	    			project.setClientId(wsProject.getClientId());
	    			project.setProjectId(wsProject.getProjectId());
	    			project.setOrganizationId(wsProject.getOrganizationId());
	    			projects.add(project);
	    		}
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_GET_SIMPLE_BY_IDS, e);
    	}
    	logger.debug("getProjectsSimpleByProjectIds - END");
    	return projects;
    }
    
    /**
     * Gets a specific Project
     * @param projectId
     * @return
     * @throws BusinessException
     */
    public Project get(int projectId, boolean isNotDeleted) throws BusinessException {
    	logger.debug("get - START");
    	Project project = null;
    	try {
    		WSProject wsProject = CMWebServiceClient.getInstance().getProjectByProjectId(projectId, isNotDeleted);
    		if (wsProject != null) {
	    		project = new Project();
				project.setProjectId(wsProject.getProjectId());
				project.setName(wsProject.getName());
				project.setOrganizationId(wsProject.getOrganizationId());
				project.setClientId(wsProject.getClientId());
				project.setManagerId(wsProject.getManagerId());
				project.setStatus(wsProject.getStatus());  
				
				Client client = new Client();
				
				client.setClientId(wsProject.getClient().getClientId());        			
				client.setC_name(wsProject.getClient().getC_name());
				client.setP_firstName(wsProject.getClient().getP_firstName());
				client.setP_lastName(wsProject.getClient().getP_lastName());
				client.setType(wsProject.getClient().getType());
				client.setStatus(wsProject.getClient().getStatus());
				
				project.setClient(client);
				
				Person manager = new Person();
				
				manager.setPersonId(wsProject.getManager().getUserId());
				manager.setFirstName(wsProject.getManager().getFirstName());
				manager.setLastName(wsProject.getManager().getLastName());
				manager.setStatus(wsProject.getManager().getStatus());
				
				project.setManager(manager);
				logger.debug("projectTeam = " + wsProject.getProjectTeam());						
				if(wsProject.getProjectTeam() != null) {
					ProjectTeam projectTeam = new ProjectTeam();					
					projectTeam.setProjectTeamId(wsProject.getProjectTeam().getProjectTeamId());
					
					HashSet<TeamMember> members = new HashSet<TeamMember>();
					logger.debug("projecteTeam teamMembers = " + wsProject.getProjectTeam().getTeamMembers());
					if(wsProject.getProjectTeam().getTeamMembers() != null) {
						Iterator<WSTeamMember> it = wsProject.getProjectTeam().getTeamMembers().iterator();
						while (it.hasNext()) {		
							WSTeamMember wsTeamMember = it.next();
							TeamMember member = new TeamMember();					
							member.setFirstName(wsTeamMember.getFirstName());
							member.setLastName(wsTeamMember.getLastName());
							member.setMemberId(wsTeamMember.getMemberId());
							member.setPersonId(wsTeamMember.getPersonId());
							members.add(member);
						}
					}
					projectTeam.setTeamMembers(members);
					project.setProjectTeam(projectTeam);
				}
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_GET, e);
    	}
    	logger.debug("get - END");
    	return project;
    }
    
    /**
     * 
     * @param personId
     * @return
     * @throws BusinessException
     */
    public List<Project> getProjectsByPerson(Integer personId) throws BusinessException {
    	logger.debug("getProjectsByPerson - START");
    	List<Project> projects = new ArrayList<Project>();
    	try {
    		List<WSProject> wsProjects = CMWebServiceClient.getInstance().getProjectsByPerson(personId, true, true, true);
    		if (wsProjects != null) {
	    		for (WSProject wsProject : wsProjects) {
	    			Project project = new Project();
	    			project.setManagerId(wsProject.getManagerId());
	    			project.setName(wsProject.getName());
	    			project.setProjectId(wsProject.getProjectId());
	    			project.setClientId(wsProject.getClientId());
	    			project.setStatus(wsProject.getStatus());
	    			projects.add(project);
	    		}
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_GET_PROJECTS_BY_PERSON, e);
    	}
    	logger.debug("getProjectsByPerson - END");
    	return projects;
    }          
        
    /**
     * Get the projectsIds for projects (by organization, or by personId)
     * if it has the persmission
     * 
     * @author Adelina
     * 
     * @param organizationId
     * @param personId
     * @param hasPermission
     * @return
     * @throws BusinessException
     */
    public HashSet<Integer> getProjectIds(Integer organizationId, Integer personId, boolean hasPermission, boolean onlyManager) throws BusinessException {
    	logger.debug("getProjectIds - START");
    	
    	List<Project> projects = new ArrayList<Project>();		
    	HashSet<Integer> projectIds = new HashSet<Integer>();
    	
    	if(hasPermission){			
    		projects = getAllProjects(organizationId, true);    			 		
    	} else {
    		projects = getProjectsByManager(personId, onlyManager, true);     				
    	}  
    	
    	logger.debug("projects = " + projects);
    	
    	if(projects != null && projects.size() > 0) {
    		for(Project project : projects) {
    			projectIds.add(project.getProjectId());
    		}
    	}
    	
    	logger.debug("getProjectIds - END , projectIds = " + projectIds);
    	return projectIds;
    }
    
    public Project getProjectWithAll(Integer projectId) throws BusinessException{
    	logger.debug("getProjectWithAll - START");
    	Project project = null;
    	try{
    		WSProject wsProject = CMWebServiceClient.getInstance().getProjectByProjectId(projectId, true);
    		if (wsProject != null) {
	    		project = new Project();
				project.setProjectId(wsProject.getProjectId());
				project.setName(wsProject.getName());
				project.setOrganizationId(wsProject.getOrganizationId());
				project.setClientId(wsProject.getClientId());
				project.setManagerId(wsProject.getManagerId());
				project.setStatus(wsProject.getStatus());  
				
				Client client = new Client();
				client.setClientId(wsProject.getClient().getClientId());        			
    			client.setC_name(wsProject.getClient().getC_name());
    			client.setP_firstName(wsProject.getClient().getP_firstName());
    			client.setP_lastName(wsProject.getClient().getP_lastName());
    			client.setType(wsProject.getClient().getType());
    			client.setStatus(wsProject.getClient().getStatus());
    			
    			project.setClient(client);
				
				Person manager = new Person();
				
				manager.setPersonId(wsProject.getManager().getUserId());
				manager.setFirstName(wsProject.getManager().getFirstName());
				manager.setLastName(wsProject.getManager().getLastName());
    			manager.setStatus(wsProject.getManager().getStatus());
				project.setManager(manager);
				
				ProjectTeam projectTeam = new ProjectTeam();
				projectTeam.setProjectId(wsProject.getProjectId());
				
				Set<TeamMember> teamMembers = new HashSet<TeamMember>();
				Set<WSTeamMember> wsTeamMembers = wsProject.getProjectTeam().getTeamMembers();
				for(WSTeamMember wsTeamMember : wsTeamMembers) {
					TeamMember teamMember = new TeamMember();
					teamMember.setFirstName(wsTeamMember.getFirstName());
					teamMember.setLastName(wsTeamMember.getLastName());
					teamMember.setMemberId(wsTeamMember.getMemberId());
					System.out.println("email: " + wsTeamMember.getEmail());
					teamMember.setEmail(wsTeamMember.getEmail());
					teamMembers.add(teamMember);
				}
				projectTeam.setTeamMembers(teamMembers);
				project.setProjectTeam(projectTeam);
    		}
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.PROJECT_GET_WITH_ALL, e);
    	}
    	logger.debug("getProjectWithAll - END");
    	return project;
    }          
	
} 
