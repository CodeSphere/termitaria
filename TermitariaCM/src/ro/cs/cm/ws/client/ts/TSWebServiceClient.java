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
package ro.cs.cm.ws.client.ts;

import java.io.IOException;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.StopWatch;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;

import ro.cs.cm.business.BLProject;
import ro.cs.cm.business.BLTeamMember;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.context.CMContext;
import ro.cs.cm.entity.Project;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.WSClientException;
import ro.cs.cm.ws.client.ts.entity.GetProjectIdForAbortRequest;
import ro.cs.cm.ws.client.ts.entity.GetProjectIdForDeleteRequest;
import ro.cs.cm.ws.client.ts.entity.GetProjectIdForFinishRequest;
import ro.cs.cm.ws.client.ts.entity.GetProjectIdForOpenRequest;
import ro.cs.cm.ws.client.ts.entity.GetTeamMemberIdForDeleteRequest;
import ro.cs.cm.ws.client.ts.entity.ObjectFactory;
import ro.cs.cm.ws.client.ts.entity.TSEndpointExceptionBean;


/**
 * 
 * Singleton which exposes methods for the TS Web Service Client
 * 
 * @author Adelina
 *
 */
public class TSWebServiceClient extends WebServiceGatewaySupport {

	private static final String tsWebServiceClientBean = "tsWebServiceClient";
	
	private ObjectFactory objectFactory; 
	protected final Log logger = LogFactory.getLog(getClass());
	
	//singleton implementation
	private TSWebServiceClient(){}
	public static TSWebServiceClient getInstance(){
		return (TSWebServiceClient) CMContext.getApplicationContext().getBean(tsWebServiceClientBean);
	}
	
	//setters and getters
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}	
	
	/**
	 * Deletes the project details
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 * @throws BusinessException
	 */
	public void deleteProjectDetails(Integer projectId) throws XmlMappingException, IOException, WSClientException, BusinessException{
		logger.debug("deleteProjectDetails START");
		StopWatch sw = new StopWatch();
		sw.start("deleteProjectDetails");	
		try {
			//create the bean  marshalled into the request
			GetProjectIdForDeleteRequest getProjectIdForDeleteRequest = new GetProjectIdForDeleteRequest();
			getProjectIdForDeleteRequest.setProjectId(projectId);			
			//unmarshall the response 
			Project project = BLProject.getInstance().getWithStatus(projectId);
			if(project == null) {
				getWebServiceTemplate().marshalSendAndReceive(getProjectIdForDeleteRequest);
			}
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<TSEndpointExceptionBean> endpointException = (JAXBElement<TSEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("deleteProjectDetails END");
		sw.stop();
		logger.debug(sw.prettyPrint());		
	}
	

	/**
	 * Finish the project details
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 * @throws BusinessException
	 */
	public void finishProjectDetails(Integer projectId) throws XmlMappingException, IOException, WSClientException, BusinessException{
		logger.debug("finishProjectDetails START");
		StopWatch sw = new StopWatch();
		sw.start("finishProjectDetails");	
		try {
			//create the bean  marshalled into the request
			GetProjectIdForFinishRequest getProjectIdForFinishRequest = new GetProjectIdForFinishRequest();
			getProjectIdForFinishRequest.setProjectId(projectId);			
			// unmarshall the response 
			Project project = BLProject.getInstance().getWithStatus(projectId);					
			if(project != null) {				
				if(project.getStatus() == IConstant.NOM_PROJECT_STATUS_CLOSED) {
					getWebServiceTemplate().marshalSendAndReceive(getProjectIdForFinishRequest);
				}
			}
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<TSEndpointExceptionBean> endpointException = (JAXBElement<TSEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("finishProjectDetails END");
		sw.stop();
		logger.debug(sw.prettyPrint());		
	}
	
	/**
	 * Abort the project details
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 * @throws BusinessException
	 */
	public void abortProjectDetails(Integer projectId) throws XmlMappingException, IOException, WSClientException, BusinessException{
		logger.debug("abortProjectDetails START");
		StopWatch sw = new StopWatch();
		sw.start("abortProjectDetails");	
		try {
			//create the bean  marshalled into the request
			GetProjectIdForAbortRequest getProjectIdForAbortRequest = new GetProjectIdForAbortRequest();
			getProjectIdForAbortRequest.setProjectId(projectId);			
			// unmarshall the response 
			Project project = BLProject.getInstance().getWithStatus(projectId);
			logger.debug("project status xxxxxxxxx = " + project.getStatus());
			if(project != null) {
				if(project.getStatus() == IConstant.NOM_PROJECT_STATUS_ABORTED) {
					logger.debug("yyyyyyyyyyyyyyyyy");
					getWebServiceTemplate().marshalSendAndReceive(getProjectIdForAbortRequest);
				}
			}
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<TSEndpointExceptionBean> endpointException = (JAXBElement<TSEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("abortProjectDetails END");
		sw.stop();
		logger.debug(sw.prettyPrint());		
	}
	
	/**
	 * Open the project details
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 * @throws BusinessException
	 */
	public void openProjectDetails(Integer projectId) throws XmlMappingException, IOException, WSClientException, BusinessException{
		logger.debug("openProjectDetails START");
		StopWatch sw = new StopWatch();
		sw.start("openProjectDetails");	
		try {
			//create the bean  marshalled into the request
			GetProjectIdForOpenRequest getProjectIdForOpenRequest = new GetProjectIdForOpenRequest();
			getProjectIdForOpenRequest.setProjectId(projectId);			
			// unmarshall the response 
			Project project = BLProject.getInstance().getWithStatus(projectId);
			if(project != null) {
				if(project.getStatus() == IConstant.NOM_PROJECT_STATUS_OPENED) {
					getWebServiceTemplate().marshalSendAndReceive(getProjectIdForOpenRequest);
				}
			}
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<TSEndpointExceptionBean> endpointException = (JAXBElement<TSEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("openProjectDetails END");
		sw.stop();
		logger.debug(sw.prettyPrint());		
	}
		
	/**
	 * Deletes the team member detail
	 * 
	 * @author Adelina
	 * 
	 * @param memberId
	 * @throws XmlMappingException
	 * @throws IOException
	 * @throws WSClientException
	 * @throws BusinessException
	 */
	public void deleteTeamMemberDetails(Integer memberId) throws XmlMappingException, IOException, WSClientException, BusinessException{
		logger.debug("deleteTeamMemberDetails START");
		StopWatch sw = new StopWatch();
		sw.start("deleteTeamMemberDetails");	
		try {
			//create the bean  marshalled into the request
			GetTeamMemberIdForDeleteRequest getTeamMemberIdForDeleteRequest = new GetTeamMemberIdForDeleteRequest();
			getTeamMemberIdForDeleteRequest.setTeamMemberId(memberId);
			// unmarshall the response 
			TeamMember teamMember = BLTeamMember.getInstance().getSimpleByMemberId(memberId);
				if(teamMember == null) {			
					getWebServiceTemplate().marshalSendAndReceive(getTeamMemberIdForDeleteRequest);
				}
			
		} catch (SoapFaultClientException soapFault) {
			SoapFaultDetail soapFaultDetail = soapFault.getSoapFault().getFaultDetail();
			//if the soap fault detail field is empty, it means another type of exception than EndpointException has been thrown
			if (soapFaultDetail == null) {
				throw new WSClientException(soapFault.getFaultCode().toString(), soapFault.getFaultStringOrReason(), soapFault);
			//soap fault detail field not empty means the Web Service has thrown an EndpointException
			} else {
				SoapFaultDetailElement soapFaultDetailElement = (SoapFaultDetailElement) soapFaultDetail.getDetailEntries().next();
				//unmarshall the soap fault detail element to a WS specific bean named dmeEndpointExceptionBean
				JAXBElement<TSEndpointExceptionBean> endpointException = (JAXBElement<TSEndpointExceptionBean>) getWebServiceTemplate().getUnmarshaller().unmarshal(soapFaultDetailElement.getSource());
				//throw a new WSClientException with the code and message of the DMEEndpointExceptionBean retrieved previously
				throw new WSClientException(endpointException.getValue().getCode(),endpointException.getValue().getMessage(), soapFault);
			}
		}
		logger.debug("deleteTeamMemberDetails END");
		sw.stop();
		logger.debug(sw.prettyPrint());		
	}
	
}
