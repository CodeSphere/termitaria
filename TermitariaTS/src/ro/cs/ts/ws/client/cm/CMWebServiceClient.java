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
package ro.cs.ts.ws.client.cm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;

import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.SearchPersonBean;
import ro.cs.ts.exception.WSClientException;
import ro.cs.ts.ws.client.cm.entity.CMEndpointExceptionBean;
import ro.cs.ts.ws.client.cm.entity.GetAllProjectsRequest;
import ro.cs.ts.ws.client.cm.entity.GetAllProjectsResponse;
import ro.cs.ts.ws.client.cm.entity.GetClientsForProjectRequest;
import ro.cs.ts.ws.client.cm.entity.GetClientsForProjectResponse;
import ro.cs.ts.ws.client.cm.entity.GetProjectByProjectIdRequest;
import ro.cs.ts.ws.client.cm.entity.GetProjectByProjectIdResponse;
import ro.cs.ts.ws.client.cm.entity.GetProjectsAndTeamMembersByPersonRequest;
import ro.cs.ts.ws.client.cm.entity.GetProjectsAndTeamMembersByPersonResponse;
import ro.cs.ts.ws.client.cm.entity.GetProjectsByManagerRequest;
import ro.cs.ts.ws.client.cm.entity.GetProjectsByManagerResponse;
import ro.cs.ts.ws.client.cm.entity.GetProjectsSimpleByProjectIdsRequest;
import ro.cs.ts.ws.client.cm.entity.GetProjectsSimpleByProjectIdsResponse;
import ro.cs.ts.ws.client.cm.entity.GetTeamMemberFromSearchRequest;
import ro.cs.ts.ws.client.cm.entity.GetTeamMemberFromSearchResponse;
import ro.cs.ts.ws.client.cm.entity.GetTeamMembersByMemberIdsRequest;
import ro.cs.ts.ws.client.cm.entity.GetTeamMembersByMemberIdsResponse;
import ro.cs.ts.ws.client.cm.entity.GetTeamMembersByProjectIdsRequest;
import ro.cs.ts.ws.client.cm.entity.GetTeamMembersByProjectIdsResponse;
import ro.cs.ts.ws.client.cm.entity.ObjectFactory;
import ro.cs.ts.ws.client.cm.entity.WSClient;
import ro.cs.ts.ws.client.cm.entity.WSProject;
import ro.cs.ts.ws.client.cm.entity.WSSearchTeamMemberBean;
import ro.cs.ts.ws.client.cm.entity.WSTeamMember;

/**
 * 
 * Singleton which exposes methods for the CM Web Service Client
 * @author coni
 *
 */
public class CMWebServiceClient extends WebServiceGatewaySupport{

	private static final String cmWebServiceClientBean = "cmWebServiceClient";
	
	private ObjectFactory objectFactory; 
	protected final Log logger = LogFactory.getLog(getClass());
	
	//singleton implementation
	private CMWebServiceClient(){}
	public static CMWebServiceClient getInstance(){
		return (CMWebServiceClient) TSContext.getApplicationContext().getBean(cmWebServiceClientBean);
	}
	
	//setters and getters
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}
	
	/**
	 * Get all projects
	 * 
	 * @author Adelina
	 * 
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public List<WSProject> getAllProjects(Integer organizationId, boolean isFinishedAndAbandoned) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getAllProjects - START");
		List<WSProject> projects = null;
		try{
			// create the bean marshalled into the request
			GetAllProjectsRequest getAllProjectsRequest = new GetAllProjectsRequest();
			getAllProjectsRequest.setOrganizationId(organizationId);	
			getAllProjectsRequest.setFinishedAndAbandoned(isFinishedAndAbandoned);
			
			// unmarshall the response
			projects = ((GetAllProjectsResponse) getWebServiceTemplate().marshalSendAndReceive(getAllProjectsRequest)).getProjects();				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getAllProjects  - END");
		return projects;				
	}
	
	/**
	 * Get those projects that has the manager identified by the managerId
	 * 
	 * @author Adelina
	 * 
	 * @param managerId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public List<WSProject> getProjectsByManager(Integer managerId, boolean onlyManager, boolean isFinishedAndAbandoned) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getProjectsByManager - START");
		
		
		List<WSProject> projects = null;
		try{
			// create the bean marshalled into the request
			GetProjectsByManagerRequest getProjectsByManagerRequest = new GetProjectsByManagerRequest();
			getProjectsByManagerRequest.setManagerId(managerId);
			getProjectsByManagerRequest.setOnlyManager(onlyManager);
			getProjectsByManagerRequest.setFinishedAndAbandoned(isFinishedAndAbandoned);
			// unmarshall the response
			projects = ((GetProjectsByManagerResponse) getWebServiceTemplate().marshalSendAndReceive(getProjectsByManagerRequest)).getProjects();				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getProjectsByManager  - END");
		return projects;				
	}	
	
	/**
	 * Get those projects and teamMemebers for a person identified by personId
	 * 
	 * @author Coni
	 * 
	 * @param personId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public HashMap<WSProject, WSTeamMember> getProjectsAndTeamMembersByPerson(Integer personId, boolean isNotDeleted, boolean isFinishedAndAbandoned) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getProjectsAndTeamMembersByPerson - START");
		
		
		HashMap<WSProject, WSTeamMember> projectsAndTeamMemebers = null;
		try{
			// create the bean marshalled into the request
			GetProjectsAndTeamMembersByPersonRequest getProjectsAndTeamMembersByPersonRequest = new GetProjectsAndTeamMembersByPersonRequest();
			getProjectsAndTeamMembersByPersonRequest.setPersonId(personId);		
			getProjectsAndTeamMembersByPersonRequest.setNotDeleted(isNotDeleted);
			getProjectsAndTeamMembersByPersonRequest.setFinishedAndAbandoned(isFinishedAndAbandoned);
			// unmarshall the response
			projectsAndTeamMemebers = ((GetProjectsAndTeamMembersByPersonResponse) getWebServiceTemplate().marshalSendAndReceive(getProjectsAndTeamMembersByPersonRequest)).getProjectsAndTeamMembers();				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getProjectsAndTeamMembersByPerson  - END");
		return projectsAndTeamMemebers;				
	}
	
	/**
	 * Get the clients for the project
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public List<WSClient> getClientsForProject(Integer organizationId) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getClientsForProject - START");
		
		List<WSClient> clients = null;
		
		try{
			// create the bean marshalled into the request
			GetClientsForProjectRequest getClientsForProjectRequest = new GetClientsForProjectRequest();
			getClientsForProjectRequest.setOrganizationId(organizationId);
			
			// unmarshall the response
			clients = ((GetClientsForProjectResponse)getWebServiceTemplate().marshalSendAndReceive(getClientsForProjectRequest)).getClients();
			
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getClientsForProject  - END");
		return clients;		
	}	
	
	/**
	 * Gets a Project by its projectId
	 * 
	 * @author Coni
	 * 
	 * @param projectId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public WSProject getProjectByProjectId(Integer projectId, boolean isNotDeleted) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getProjectByProjectId - START");
		
		WSProject project = null;
		try{
			// create the bean marshalled into the request
			GetProjectByProjectIdRequest getProjectByProjectIdRequest = new GetProjectByProjectIdRequest();
			getProjectByProjectIdRequest.setProjectId(projectId);
			getProjectByProjectIdRequest.setNotDeleted(isNotDeleted);
			// unmarshall the response
			project = ((GetProjectByProjectIdResponse) getWebServiceTemplate().marshalSendAndReceive(getProjectByProjectIdRequest)).getProject();				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getProjectByProjectId  - END");
		return project;				
	}		
	
	/**
	 * Get a team member by its member id
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public List<WSTeamMember> getTeamMembersByMemberIds(HashSet<Integer> memberIds, boolean isNotDeleted) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getTeamMembersByMemberIds - START");
		
		
		List<WSTeamMember> teamMembers = new ArrayList<WSTeamMember>();
		try{
			// create the bean marshalled into the request
			GetTeamMembersByMemberIdsRequest getTeamMembersByMemberIdsRequest = new GetTeamMembersByMemberIdsRequest();
			getTeamMembersByMemberIdsRequest.setMemberIds(memberIds);
			getTeamMembersByMemberIdsRequest.setNotDeleted(isNotDeleted);
			
			// unmarshall the response
			teamMembers = ((GetTeamMembersByMemberIdsResponse) getWebServiceTemplate().marshalSendAndReceive(getTeamMembersByMemberIdsRequest)).getTeamMembers();
			
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getTeamMembersByMemberIds - END");
		
		return teamMembers;		
	}
			
	/**
	 * Get the team members from projects
	 * 
	 * @author Adelina
	 * 
	 * @param projectIds
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public List<WSTeamMember> getTeamMembersByProjectIds(HashSet<Integer> projectIds, boolean isExternal, boolean isNotDeleted) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getTeamMembersByProjectIds - START");
		
		
		List<WSTeamMember> teamMembers = new ArrayList<WSTeamMember>();
		
		try{
			// create the bean marshalled into the request
			GetTeamMembersByProjectIdsRequest getTeamMembersByProjectIdsRequest = new GetTeamMembersByProjectIdsRequest();
			getTeamMembersByProjectIdsRequest.setProjectIds(projectIds);
			getTeamMembersByProjectIdsRequest.setExternal(isExternal);
			getTeamMembersByProjectIdsRequest.setNotDeleted(isNotDeleted);
			// unmarshall the response
			teamMembers = ((GetTeamMembersByProjectIdsResponse) getWebServiceTemplate().marshalSendAndReceive(getTeamMembersByProjectIdsRequest)).getTeamMembers();
			
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getTeamMembersByProjectIds - END");
		
		return teamMembers;		
	}
	
	/**
	 * Gets team members from the specified criteria with order and pagination, if needed
	 * 
	 * @author Coni
	 * 
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public GetTeamMemberFromSearchResponse getTeamMemberFromSearch(SearchPersonBean searchPersonBean, Integer personId, boolean hasPermissionToSeeAllProjects, boolean includeFinishedAndAbandonedProjects, boolean onlyManager) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getTeamMemberFromSearch - START");
		
		GetTeamMemberFromSearchResponse response = null;
		try{
			// create the bean marshalled into the request
			GetTeamMemberFromSearchRequest getTeamMemberFromSearchRequest = new GetTeamMemberFromSearchRequest();
			WSSearchTeamMemberBean wsSearchTeamMemberBean = new WSSearchTeamMemberBean();
			wsSearchTeamMemberBean.setFirstName(searchPersonBean.getFirstName());
			wsSearchTeamMemberBean.setLastName(searchPersonBean.getLastName());
			wsSearchTeamMemberBean.setOrganisationId(searchPersonBean.getOrganizationId());
			wsSearchTeamMemberBean.setProjectId(searchPersonBean.getProjectId());
			wsSearchTeamMemberBean.setWithDeleted(searchPersonBean.isWithDeleted());
			wsSearchTeamMemberBean.setCurrentPage(searchPersonBean.getCurrentPage());
			wsSearchTeamMemberBean.setLowerLimit(searchPersonBean.getLowerLimit());
			wsSearchTeamMemberBean.setNbrOfPages(searchPersonBean.getNbrOfPages());
			wsSearchTeamMemberBean.setNbrOfResults(searchPersonBean.getNbrOfResults());
			wsSearchTeamMemberBean.setResultsPerPage(searchPersonBean.getResultsPerPage());
			wsSearchTeamMemberBean.setSortDirection(searchPersonBean.getSortDirection());
			wsSearchTeamMemberBean.setSortParam(searchPersonBean.getSortParam());
			wsSearchTeamMemberBean.setUpperLimit(searchPersonBean.getUpperLimit());
			wsSearchTeamMemberBean.setPersonId(personId);
			wsSearchTeamMemberBean.setIncludeFinishedAndAbandonedProjects(includeFinishedAndAbandonedProjects);
			wsSearchTeamMemberBean.setHasPermissionToSeeAllProjects(hasPermissionToSeeAllProjects);
			wsSearchTeamMemberBean.setOnlyManager(onlyManager);
			getTeamMemberFromSearchRequest.setWsSearchTeamMemberBean(wsSearchTeamMemberBean);
			// unmarshall the response
			response = (GetTeamMemberFromSearchResponse) getWebServiceTemplate().marshalSendAndReceive(getTeamMemberFromSearchRequest);				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getTeamMemberFromSearch  - END");
		return response;				
	}
	
	/**
	 * Get the projects for a person identified by personId
	 * 
	 * @author Coni
	 * 
	 * @param personId
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public List<WSProject> getProjectsByPerson(Integer personId, boolean onlyProjects, boolean isNotDeleted, boolean isFinishedAndAbandoned) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getProjectsByPerson - START");
		
		
		List<WSProject> projects = null;
		try{
			// create the bean marshalled into the request
			GetProjectsAndTeamMembersByPersonRequest getProjectsAndTeamMembersByPersonRequest = new GetProjectsAndTeamMembersByPersonRequest();
			getProjectsAndTeamMembersByPersonRequest.setPersonId(personId);
			getProjectsAndTeamMembersByPersonRequest.setOnlyProjects(onlyProjects);
			getProjectsAndTeamMembersByPersonRequest.setNotDeleted(isNotDeleted);
			getProjectsAndTeamMembersByPersonRequest.setFinishedAndAbandoned(isFinishedAndAbandoned);
			// unmarshall the response
			projects = ((GetProjectsAndTeamMembersByPersonResponse) getWebServiceTemplate().marshalSendAndReceive(getProjectsAndTeamMembersByPersonRequest)).getProjects();				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getProjectsByPerson  - END");
		return projects;				
	}
	
	/**
	 * Gets a list of projects by their project ids
	 * @author Coni
	 * @param projectIds
	 * @return
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 */
	public List<WSProject> getProjectsSimpleByProjectIds(Set<Integer> projectIds) throws XmlMappingException, IOException, WSClientException {
		logger.debug("getProjectsSimpleByProjectIds - START");
		
		
		List<WSProject> projects = null;
		try{
			// create the bean marshalled into the request
			GetProjectsSimpleByProjectIdsRequest getProjectsSimpleByProjectIdsRequest = new GetProjectsSimpleByProjectIdsRequest();
			getProjectsSimpleByProjectIdsRequest.setProjectIds(projectIds);
			// unmarshall the response
			projects = ((GetProjectsSimpleByProjectIdsResponse) getWebServiceTemplate().marshalSendAndReceive(getProjectsSimpleByProjectIdsRequest)).getProjects();				
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<CMEndpointExceptionBean> endpointException = (JAXBElement<CMEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		
		logger.debug("getProjectsSimpleByProjectIds  - END");
		return projects;				
	}
	
}
