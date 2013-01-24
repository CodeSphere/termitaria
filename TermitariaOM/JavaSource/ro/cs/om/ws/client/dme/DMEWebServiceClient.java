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
package ro.cs.om.ws.client.dme;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.ws.client.dme.entity.DMWorkspaces;
import ro.cs.om.ws.client.dme.entity.ObjectFactory;


/**
 * 
 * Singleton which exposes methods for the DME Web Service Client
 * @author coni
 *
 */
public class DMEWebServiceClient extends WebServiceGatewaySupport {

	private static final String dmeWebServiceClientBean = "dmeWebServiceClient";
	
	private ObjectFactory objectFactory;
	protected final Log logger = LogFactory.getLog(getClass());
	
	//singleton implementation
	private DMEWebServiceClient(){}
	
	public static DMEWebServiceClient getInstance(){
		return (DMEWebServiceClient) OMContext.getApplicationContext().getBean(dmeWebServiceClientBean);
	}
	
	//setters and getters
	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}
	
	public void setObjectFactory(ObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
	}
	
	/**
	 * 
	 * Deleting the organisation's workspace
	 * 
	 * @author mitziuro
	 * @param organisationId
	 * @throws Exception
	 */
	public void deleteWorkspace(Integer organisationId) throws Exception{
		logger.debug("START - deleteWorkspace");
		StopWatch sw = new StopWatch();
		sw.start("add");
		
		getWebServiceTemplate().marshalSendAndReceive(objectFactory.createDeleteWorkpaceRequest(IConstant.DME_WORKSPACE_PREFIX.concat(organisationId.toString())));
		
		logger.debug("END - deleteWorkspace");
		sw.stop();
		logger.debug(sw.prettyPrint());

	}
	
	/**
	 * 
	 * Deleting the workspaces
	 * 
	 * @author mitziuro
	 * @param organisationId
	 * @throws Exception
	 */
	public void deleteWorkspaces(List<Integer> organisationIds) throws Exception{
		logger.debug("START - deleteWorkspace");
		StopWatch sw = new StopWatch();
		sw.start("add");
		ArrayList<String> workspaces = new ArrayList<String>();
		
		for(Integer organisation : organisationIds){
			workspaces.add(IConstant.DME_WORKSPACE_PREFIX.concat(String.valueOf(organisation)));
		}
		
		//set the workpspaces on the transport bean
		DMWorkspaces workspacesList = new DMWorkspaces();
		workspacesList.setWorkspaces(workspaces);
		
		getWebServiceTemplate().marshalSendAndReceive(objectFactory.createDeleteWorkpacesRequest(workspacesList));
		
		logger.debug("END - deleteWorkspace");
		sw.stop();
		logger.debug(sw.prettyPrint());

	}
	
}
