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
package ro.cs.cm.ws.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.StopWatch;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import ro.cs.cm.business.BLProject;
import ro.cs.cm.business.BLTeamMember;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.SearchTeamMemberBean;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.EndpointException;
import ro.cs.cm.exception.ICodeException;
import ro.cs.cm.ws.client.om.entity.UserSimple;
import ro.cs.cm.ws.server.entity.GetAllProjectsRequest;
import ro.cs.cm.ws.server.entity.GetAllProjectsResponse;
import ro.cs.cm.ws.server.entity.GetClientsForProjectRequest;
import ro.cs.cm.ws.server.entity.GetClientsForProjectResponse;
import ro.cs.cm.ws.server.entity.GetProjectByProjectIdRequest;
import ro.cs.cm.ws.server.entity.GetProjectByProjectIdResponse;
import ro.cs.cm.ws.server.entity.GetProjectsAndTeamMembersByPersonRequest;
import ro.cs.cm.ws.server.entity.GetProjectsAndTeamMembersByPersonResponse;
import ro.cs.cm.ws.server.entity.GetProjectsByManagerRequest;
import ro.cs.cm.ws.server.entity.GetProjectsByManagerResponse;
import ro.cs.cm.ws.server.entity.GetProjectsSimpleByProjectIdsRequest;
import ro.cs.cm.ws.server.entity.GetProjectsSimpleByProjectIdsResponse;
import ro.cs.cm.ws.server.entity.GetTeamMemberFromSearchRequest;
import ro.cs.cm.ws.server.entity.GetTeamMemberFromSearchResponse;
import ro.cs.cm.ws.server.entity.GetTeamMembersByMemberIdsRequest;
import ro.cs.cm.ws.server.entity.GetTeamMembersByMemberIdsResponse;
import ro.cs.cm.ws.server.entity.GetTeamMembersByProjectIdsRequest;
import ro.cs.cm.ws.server.entity.GetTeamMembersByProjectIdsResponse;
import ro.cs.cm.ws.server.entity.ObjectFactory;
import ro.cs.cm.ws.server.entity.WSClient;
import ro.cs.cm.ws.server.entity.WSProject;
import ro.cs.cm.ws.server.entity.WSProjectTeam;
import ro.cs.cm.ws.server.entity.WSSearchTeamMemberBean;
import ro.cs.cm.ws.server.entity.WSTeamMember;

@Endpoint
public class PayloadRootAnnotationMarshallingEndpoint extends GenericEndpoint{
    private ObjectFactory objectFactory;
    
	public PayloadRootAnnotationMarshallingEndpoint(ObjectFactory objectFactory){
		this.objectFactory = objectFactory;
	}
	
	@PayloadRoot(localPart = "GetAllProjectsRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetAllProjectsResponse getAllProjects(GetAllProjectsRequest requestElement) throws IOException, EndpointException {
		logger.debug("getAllProjects - START");
		StopWatch  sw = new StopWatch();
		sw.start("getAllProjects");
		GetAllProjectsResponse getAllProjectsResponse = new GetAllProjectsResponse();
		try{
			List<WSProject> projects = new ArrayList<WSProject>();
			
			List<Project> allProjects = BLProject.getInstance().getAllProjects(requestElement.getOrganizationId(), requestElement.isFinishedAndAbandoned());
					
			for(Project project : allProjects ) {
				WSProject wsProject = new WSProject();
				wsProject.setProjectId(project.getProjectId());
				wsProject.setName(project.getName());
				wsProject.setManagerId(project.getManagerId());				
				wsProject.setStatus(project.getStatus());
				wsProject.setClientId(project.getClientId());
				
				WSClient wsClient = new WSClient();
				if(project.getClient() != null) {
					wsClient.setClientId(project.getClient().getClientId());
					wsClient.setC_name(project.getClient().getC_name());
					wsClient.setP_firstName(project.getClient().getP_firstName());
					wsClient.setP_lastName(project.getClient().getP_lastName());
					wsClient.setType(project.getClient().getType());
					wsClient.setStatus(project.getClient().getStatus());			
										
				}
				
				wsProject.setClient(wsClient);
				
				UserSimple userSimple = new UserSimple();
				
				if(project.getManager() != null) {
					userSimple.setUserId(project.getManager().getPersonId());
					userSimple.setFirstName(project.getManager().getFirstName());
					userSimple.setLastName(project.getManager().getLastName());
				}
				
				wsProject.setManager(userSimple);
				
				projects.add(wsProject);
			}			
			getAllProjectsResponse.setProjects(projects);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_ALL_PROJECTS, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_ALL_PROJECTS, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		logger.debug("getAllProjects - END");
		return getAllProjectsResponse;
	}
	
	@PayloadRoot(localPart = "GetProjectsByManagerRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetProjectsByManagerResponse getProjectsByManager(GetProjectsByManagerRequest requestElement) throws IOException, EndpointException {
		logger.debug("getProjectsByManager - START");
		StopWatch  sw = new StopWatch();
		sw.start("getProjectsByManager");
		GetProjectsByManagerResponse getProjectsByManagerResponse = new GetProjectsByManagerResponse();		
		try{
			
			List<Project> projects = null;
			if (requestElement.isOnlyManager()) {
				projects = BLProject.getInstance().getProjectsByManager(requestElement.getManagerId(), requestElement.isFinishedAndAbandoned());
			} else {
				projects = BLProject.getInstance().getProjectsByPerson(requestElement.getManagerId(), requestElement.isFinishedAndAbandoned());
			}
			
			List<WSProject> projectsForManager = new ArrayList<WSProject>();
			
			for(Project project : projects) {
				WSProject wsProject = new WSProject();
				wsProject.setProjectId(project.getProjectId());
				wsProject.setName(project.getName());
				wsProject.setStatus(project.getStatus());
				wsProject.setManagerId(project.getManagerId());
				wsProject.setClientId(project.getClientId());
				
				WSClient wsClient = new WSClient();
				
				if(project.getClient() != null) {
					wsClient.setClientId(project.getClient().getClientId());
					wsClient.setC_name(project.getClient().getC_name());
					wsClient.setP_firstName(project.getClient().getP_firstName());
					wsClient.setP_lastName(project.getClient().getP_lastName());
					wsClient.setType(project.getClient().getType());
					wsClient.setStatus(project.getClient().getStatus());	
				}
				
				wsProject.setClient(wsClient);
				
				UserSimple userSimple = new UserSimple();
				
				if(project.getManager() != null) {
					userSimple.setUserId(project.getManager().getPersonId());
					userSimple.setFirstName(project.getManager().getFirstName());
					userSimple.setLastName(project.getManager().getLastName());
				}
				
				wsProject.setManager(userSimple);
				
				projectsForManager.add(wsProject);
			}
			
			getProjectsByManagerResponse.setProjects(projectsForManager);
			
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PROJECTS_BY_MANAGER, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PROJECTS_BY_MANAGER, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		logger.debug("getProjectsByManager - END");
		return getProjectsByManagerResponse;
	}
	
	@PayloadRoot(localPart = "GetProjectsAndTeamMembersByPersonRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetProjectsAndTeamMembersByPersonResponse getProjectsAndTeamMembersByPerson(GetProjectsAndTeamMembersByPersonRequest requestElement) throws IOException, EndpointException {
		logger.debug("getProjectsByTeamMember - START");
		StopWatch  sw = new StopWatch();
		sw.start("getProjectsByTeamMember");
		GetProjectsAndTeamMembersByPersonResponse getProjectsAndTeamMembersByPersonResponse = new GetProjectsAndTeamMembersByPersonResponse();
		HashMap<WSProject, WSTeamMember> wsProjectsAndTeamMembers = null;
		List<WSProject> wsProjects = null;
		try{
			if (requestElement.isOnlyProjects()) {
				wsProjects = new ArrayList<WSProject>();
			} else {
				wsProjectsAndTeamMembers = new HashMap<WSProject, WSTeamMember>();
			}
			HashMap<Project, TeamMember> projectsAndTeamMembers = BLProject.getInstance().getProjectsAndTeamMembersByPerson(requestElement.getPersonId(), requestElement.isNotDeleted(), requestElement.isFinishedAndAbandoned());
			Iterator it = projectsAndTeamMembers.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();
				WSProject wsProject = new WSProject();
				wsProject.setProjectId(entry.getKey().getProjectId());
				wsProject.setStatus(entry.getKey().getStatus());
				wsProject.setManagerId(entry.getKey().getManagerId());
				wsProject.setName(entry.getKey().getName());
				wsProject.setClientId(entry.getKey().getClientId());
				
				if (requestElement.isOnlyProjects()) {
					wsProjects.add(wsProject);
				} else {
					WSProjectTeam wsProjectTeam = new WSProjectTeam();
					wsProjectTeam.setProjectTeamId(entry.getKey().getProjectTeam().getProjectTeamId());
					wsProject.setProjectTeam(wsProjectTeam);
					
					WSTeamMember wsTeamMember = new WSTeamMember();
					wsTeamMember.setFirstName(entry.getValue().getFirstName());
					wsTeamMember.setLastName(entry.getValue().getLastName());
					wsTeamMember.setMemberId(entry.getValue().getMemberId());
					wsTeamMember.setPersonId(entry.getValue().getPersonId());
					wsTeamMember.setStatus(entry.getValue().getStatus());
					wsProjectsAndTeamMembers.put(wsProject, wsTeamMember);
				}
			}
			
			if (requestElement.isOnlyProjects()) {
				getProjectsAndTeamMembersByPersonResponse.setProjects(wsProjects);
			} else {
				getProjectsAndTeamMembersByPersonResponse.setProjectsAndTeamMembers(wsProjectsAndTeamMembers);
			}
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PROJECTS_AND_TEAM_MEMBERS_BY_PERSON, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		logger.debug("getProjectsByTeamMember - END");
		return getProjectsAndTeamMembersByPersonResponse;
	}
	
	@PayloadRoot(localPart = "GetProjectByProjectIdRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetProjectByProjectIdResponse getProjectByProjectId(GetProjectByProjectIdRequest requestElement) throws IOException, EndpointException {
		logger.debug("getProjectByProjectId - START");
		StopWatch  sw = new StopWatch();
		sw.start("getProjectByProjectId");
		GetProjectByProjectIdResponse getProjectByProjectIdResponse = new GetProjectByProjectIdResponse();
		try{
			Project project = BLProject.getInstance().getWithProjectTeamByProjectId(requestElement.getProjectId(), requestElement.isNotDeleted());
			logger.debug("project id = " + requestElement.getProjectId());		
			logger.debug("project = " + project);
			WSProject wsProject = new WSProject();
			wsProject.setProjectId(project.getProjectId());
			wsProject.setStatus(project.getStatus());
			wsProject.setManagerId(project.getManagerId());
			wsProject.setName(project.getName());
			wsProject.setClientId(project.getClientId());
			wsProject.setOrganizationId(project.getOrganizationId());
			
			WSClient wsClient = new WSClient();
			if(project.getClient() != null) {
				wsClient.setClientId(project.getClient().getClientId());
				wsClient.setC_name(project.getClient().getC_name());
				wsClient.setP_firstName(project.getClient().getP_firstName());
				wsClient.setP_lastName(project.getClient().getP_lastName());
				wsClient.setType(project.getClient().getType());
				wsClient.setStatus(project.getClient().getStatus());	
			}
			
			wsProject.setClient(wsClient);
			
			UserSimple userSimple = new UserSimple();
			
			if(project.getManager() != null) {
				userSimple.setUserId(project.getManager().getPersonId());
				userSimple.setFirstName(project.getManager().getFirstName());
				userSimple.setLastName(project.getManager().getLastName());
				userSimple.setStatus(project.getManager().getStatus());
			}
			
			wsProject.setManager(userSimple);
			logger.debug("project.getProjectTeam() = " + project.getProjectTeam());					
			if(project.getProjectTeam() != null) {
				WSProjectTeam wsProjectTeam = new WSProjectTeam();
				
				wsProjectTeam.setProjectTeamId(project.getProjectTeam().getProjectTeamId());			
				logger.debug("projecteTeam teamMembers = " + project.getProjectTeam().getTeamMembers());
				Iterator<TeamMember> it = project.getProjectTeam().getTeamMembers().iterator();
				HashSet<WSTeamMember> wsTeamMembers = new HashSet<WSTeamMember>();
			
				while (it.hasNext()) {
					TeamMember teamMember = it.next();			
					WSTeamMember wsTeamMember = new WSTeamMember();
					wsTeamMember.setMemberId(teamMember.getMemberId());
					wsTeamMember.setPersonId(teamMember.getPersonId());
					wsTeamMember.setProjectTeamId(teamMember.getProjectTeamId());
					wsTeamMember.setStatus(teamMember.getStatus());
					wsTeamMember.setFirstName(teamMember.getFirstName());
					wsTeamMember.setLastName(teamMember.getLastName());
					wsTeamMembers.add(wsTeamMember);
				}
				wsProjectTeam.setTeamMembers(wsTeamMembers);
				
				wsProject.setProjectTeam(wsProjectTeam);
			}			
			
			getProjectByProjectIdResponse.setProject(wsProject);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PROJECTS_BY_MANAGER, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_PROJECTS_BY_MANAGER, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		logger.debug("getProjectByProjectId - END");
		return getProjectByProjectIdResponse;
	}
	
	/**
	 * Gets team members using the specified search criteria with order and pagination if needed 
	 * @author Coni
	 * @param requestElement
	 * @return
	 * @throws IOException
	 * @throws EndpointException
	 */
	@PayloadRoot(localPart = "GetTeamMemberFromSearchRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetTeamMemberFromSearchResponse getTeamMemberFromSearch(GetTeamMemberFromSearchRequest requestElement) throws IOException, EndpointException {
		logger.debug("getTeamMemberFromSearch - START");
		GetTeamMemberFromSearchResponse response = new GetTeamMemberFromSearchResponse();
		try {
			List<TeamMember> teamMembers = null;
			WSSearchTeamMemberBean wsSearchTeamMemberBean = requestElement.getWsSearchTeamMemberBean();
			SearchTeamMemberBean stmb = new SearchTeamMemberBean();
			stmb.setFirstName(wsSearchTeamMemberBean.getFirstName());
			stmb.setLastName(wsSearchTeamMemberBean.getLastName());
			stmb.setOrganizationId(wsSearchTeamMemberBean.getOrganisationId());
			stmb.setProjectId(wsSearchTeamMemberBean.getProjectId());
			stmb.setPersonId(wsSearchTeamMemberBean.getPersonId());
			stmb.setHasPermissionToSeeAllProjects(wsSearchTeamMemberBean.getHasPermissionToSeeAllProjects());
			stmb.setIncludeFinishedAndAbandonedProjects(wsSearchTeamMemberBean.getHasPermissionToSeeAllProjects());
			stmb.setCurrentPage(wsSearchTeamMemberBean.getCurrentPage());
			stmb.setLowerLimit(wsSearchTeamMemberBean.getLowerLimit());
			stmb.setNbrOfPages(wsSearchTeamMemberBean.getNbrOfPages());
			stmb.setNbrOfResults(wsSearchTeamMemberBean.getNbrOfResults());
			stmb.setResultsPerPage(wsSearchTeamMemberBean.getResultsPerPage());
			stmb.setSortDirection(wsSearchTeamMemberBean.getSortDirection());
			stmb.setSortParam(wsSearchTeamMemberBean.getSortParam());
			stmb.setUpperLimit(wsSearchTeamMemberBean.getUpperLimit());
			stmb.setWithDeleted(wsSearchTeamMemberBean.getWithDeleted());
			stmb.setOnlyManager(wsSearchTeamMemberBean.getOnlyManager());
			
			//if the the attribute resultsPerPage > 0, it means that request needs pagination; otherwise
			//all the results will be retrieved
			if (stmb.getResultsPerPage() > 0) {
				teamMembers = BLTeamMember.getInstance().getFromSearchSimpleWithPagination(stmb);
				//set the new pagination attributes resulted from search for the returned WSSearchTeamMemberBean
				wsSearchTeamMemberBean.setCurrentPage(stmb.getCurrentPage());
				wsSearchTeamMemberBean.setLowerLimit(stmb.getLowerLimit());
				wsSearchTeamMemberBean.setNbrOfPages(stmb.getNbrOfPages());
				wsSearchTeamMemberBean.setNbrOfResults(stmb.getNbrOfResults());
				wsSearchTeamMemberBean.setResultsPerPage(stmb.getResultsPerPage());
				wsSearchTeamMemberBean.setUpperLimit(stmb.getUpperLimit());
			} else {
				teamMembers = BLTeamMember.getInstance().getFromSearchSimple(stmb);
			}			
			
			List<WSTeamMember> wsTeamMembers = new ArrayList<WSTeamMember>();
			for(TeamMember member: teamMembers) {
				WSTeamMember wsTeamMember = new WSTeamMember();
				wsTeamMember.setFirstName(member.getFirstName());
				wsTeamMember.setLastName(member.getLastName());
				wsTeamMember.setMemberId(member.getMemberId());
				wsTeamMember.setPersonId(member.getPersonId());
				wsTeamMember.setEmail(member.getEmail());
				wsTeamMember.setStatus(member.getStatus());
				wsTeamMember.setProjectTeamId(member.getProjectTeam().getProjectTeamId());
				
				if (stmb.getResultsPerPage() > 0) { 
					WSProject wsProject = new WSProject();
					wsProject.setProjectId(member.getProjectTeam().getProject().getProjectId());
					wsProject.setStatus(member.getProjectTeam().getProject().getStatus());
					wsProject.setName(member.getProjectTeam().getProject().getName());
					wsProject.setManagerId(member.getProjectTeam().getProject().getManagerId());
					
					WSProjectTeam wsProjectTeam = new WSProjectTeam();
					wsProjectTeam.setProjectTeamId(member.getProjectTeam().getProjectTeamId());
					wsProjectTeam.setProjectId(member.getProjectTeam().getProject().getProjectId());
					wsProjectTeam.setProject(wsProject);
					
					wsTeamMember.setProjectTeam(wsProjectTeam);
				}
				wsTeamMembers.add(wsTeamMember);
			}
			response.setTeamMembers(wsTeamMembers);
			response.setWsSearchTeamMemberBean(wsSearchTeamMemberBean);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_TEAM_MEMBER_FROM_SEARCH, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_TEAM_MEMBER_FROM_SEARCH, e);
		}		
		logger.debug("getTeamMemberFromSearch - END");
		return response;
	}
	
	@PayloadRoot(localPart = "GetTeamMembersByMemberIdsRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetTeamMembersByMemberIdsResponse getTeamMembersByMembersId(GetTeamMembersByMemberIdsRequest requestElement) throws IOException, EndpointException {
		logger.debug("getTeamMembersByMembersId - START ");
		
		StopWatch  sw = new StopWatch();
		sw.start("getTeamMembersByMembersId");
		
		GetTeamMembersByMemberIdsResponse getTeamMembersByMemberIdsResponse = new GetTeamMembersByMemberIdsResponse();
		
		try{
			
			List<TeamMember> teamMembers = BLTeamMember.getInstance().getByMemberIds(requestElement.getMemberIds(), requestElement.isNotDeleted());
			
			if (teamMembers != null) {
				ArrayList<WSTeamMember> wsTeamMembers = new ArrayList<WSTeamMember>();
				for (TeamMember teamMember : teamMembers) {
					WSTeamMember wsTeamMember = new WSTeamMember();
					wsTeamMember.setMemberId(teamMember.getMemberId());
					wsTeamMember.setStatus(teamMember.getStatus());
					wsTeamMember.setFirstName(teamMember.getFirstName());
					wsTeamMember.setLastName(teamMember.getLastName());
					wsTeamMember.setPersonId(teamMember.getPersonId());
					wsTeamMember.setProjectTeamId(teamMember.getProjectTeamId());
					wsTeamMembers.add(wsTeamMember);
				}
				getTeamMembersByMemberIdsResponse.setTeamMembers(wsTeamMembers);	
			}
			
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_TEAM_MEMBER_BY_MEMBER_ID, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_TEAM_MEMBER_BY_MEMBER_ID, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		
		logger.debug("getTeamMembersByMembersId - END");
		
		return getTeamMembersByMemberIdsResponse;
	}
	
	@PayloadRoot(localPart = "GetTeamMembersByProjectIdsRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetTeamMembersByProjectIdsResponse getTeamMembersByProjectIds(GetTeamMembersByProjectIdsRequest requestElement) throws IOException, EndpointException {
		logger.debug("getTeamMembersByProjectIds - START ");
		
		StopWatch  sw = new StopWatch();
		sw.start("getTeamMembersByProjectIds");
		
		GetTeamMembersByProjectIdsResponse getTeamMembersByProjectIdsResponse = new GetTeamMembersByProjectIdsResponse();
		
		try{
			
			List<TeamMember> teamMembers = BLTeamMember.getInstance().getTeamMembersByProjectIds(requestElement.getProjectIds(), requestElement.isExternal(), requestElement.isNotDeleted());				
			
			if (teamMembers != null) {
				ArrayList<WSTeamMember> wsTeamMembers = new ArrayList<WSTeamMember>();
				for (TeamMember teamMember : teamMembers) {				
					WSTeamMember wsTeamMember = new WSTeamMember();
					wsTeamMember.setMemberId(teamMember.getMemberId());
					wsTeamMember.setStatus(teamMember.getStatus());
					wsTeamMember.setFirstName(teamMember.getFirstName());
					wsTeamMember.setLastName(teamMember.getLastName());
					wsTeamMember.setPersonId(teamMember.getPersonId());
					wsTeamMember.setProjectTeamId(teamMember.getProjectTeamId());
					wsTeamMember.setEmail(teamMember.getEmail());
					
					WSProjectTeam wsProjectTeam = new WSProjectTeam();
					wsProjectTeam.setProjectTeamId(teamMember.getProjectTeam().getProjectTeamId());
					wsProjectTeam.setProjectId(teamMember.getProjectTeam().getProjectId());
					wsTeamMember.setProjectTeam(wsProjectTeam);
					
					wsTeamMembers.add(wsTeamMember);
				}	
				getTeamMembersByProjectIdsResponse.setTeamMembers(wsTeamMembers);	
			}
			
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_EXTERNAL_TEAM_MEMBER_BY_PROJECT_IDS, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_EXTERNAL_TEAM_MEMBER_BY_PROJECT_IDS, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		
		logger.debug("getTeamMembersByProjectIds - END");
		
		return getTeamMembersByProjectIdsResponse;
	}
	
		
	@PayloadRoot(localPart = "GetClientsForProjectRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetClientsForProjectResponse getClientsForProject(GetClientsForProjectRequest requestElement) throws IOException, EndpointException {
		logger.debug("getClientsForProject - START");
		StopWatch  sw = new StopWatch();
		sw.start("getClientsForProject");
		GetClientsForProjectResponse getClientsForProjectResponse = new GetClientsForProjectResponse();
		try{
			
			List<Client> clients = BLProject.getInstance().getClientsForProjects(requestElement.getOrganizationId());
			
			List<WSClient> wsClients = new ArrayList<WSClient>();
			
			if(clients != null && clients.size() > 0) {
				for(Client client : clients) {
					WSClient wsClient = new WSClient();
					wsClient.setClientId(client.getClientId());
					wsClient.setC_name(client.getC_name());
					wsClient.setP_firstName(client.getP_firstName());
					wsClient.setP_lastName(client.getP_lastName());
					wsClient.setType(client.getType());
					wsClient.setStatus(client.getStatus());
					
					wsClients.add(wsClient);
					
				}
			}
			
			getClientsForProjectResponse.setClients(wsClients);			
			
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_CLIENTS_FOR_PROJECT, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_CLIENTS_FOR_PROJECT, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		logger.debug("getClientsForProject - END");
		return getClientsForProjectResponse;
	}
	
	@PayloadRoot(localPart = "GetProjectsSimpleByProjectIdsRequest", namespace = "http://localhost:8080/CM/services/schemas/messages")
	public GetProjectsSimpleByProjectIdsResponse getProjectsSimpleByProjectIds(GetProjectsSimpleByProjectIdsRequest requestElement) throws IOException, EndpointException {
		logger.debug("getProjectsSimpleByProjectIds - START");
		StopWatch  sw = new StopWatch();
		sw.start("getProjectsSimpleByProjectIds");
		GetProjectsSimpleByProjectIdsResponse getProjectsSimpleByProjectIdsResponse = new GetProjectsSimpleByProjectIdsResponse();
		try{
			List<Project> projects = BLProject.getInstance().getSimpleByProjectIds(requestElement.getProjectIds());
			ArrayList<WSProject> wsProjects = new ArrayList<WSProject>();
			for (Project project : projects) {
				WSProject wsProject = new WSProject();
				wsProject.setProjectId(project.getProjectId());
				wsProject.setStatus(project.getStatus());
				wsProject.setManagerId(project.getManagerId());
				wsProject.setName(project.getName());
				wsProject.setClientId(project.getClientId());
				wsProject.setOrganizationId(project.getOrganizationId());
				wsProjects.add(wsProject);
			}
			getProjectsSimpleByProjectIdsResponse.setProjects(wsProjects);
		} catch (BusinessException bexc) {
			logger.error("", bexc);
			throw new EndpointException(ICodeException.ENDPOINT_GET_BY_PROJECT_IDS, bexc);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_BY_PROJECT_IDS, e);
		}		
		sw.stop();
		logger.debug(sw.prettyPrint());
		logger.debug("getProjectsSimpleByProjectIds - END");
		return getProjectsSimpleByProjectIdsResponse;
	}
}
