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
package ro.cs.logaudit.ws.server;

import java.io.IOException;
import java.util.List;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import ro.cs.logaudit.business.BLAuditCm;
import ro.cs.logaudit.business.BLAuditDm;
import ro.cs.logaudit.business.BLAuditOm;
import ro.cs.logaudit.business.BLAuditTs;
import ro.cs.logaudit.business.BLReportsDataSource;
import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.exception.EndpointException;
import ro.cs.logaudit.exception.ICodeException;
import ro.cs.logaudit.ws.server.entity.AuditEvent;
import ro.cs.logaudit.ws.server.entity.AuditEventRequest;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportDatasouceRequest;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportDatasouceResponse;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;
import ro.cs.logaudit.ws.server.entity.ObjectFactory;

@Endpoint
public class PayloadRootAnnotationMarshallingEndpoint extends GenericEndpoint{
    private ObjectFactory objectFactory;
   
	public PayloadRootAnnotationMarshallingEndpoint(ObjectFactory objectFactory){
		this.objectFactory = objectFactory;
	}
	
	
	/**
	 * @author coni
	 * Endpoint method for add audit events requests
	 */
	@PayloadRoot(localPart = "AuditEventRequest", namespace = IConstant.AUDIT_WS_SERVER_NAMESPACE)
	public void addAuditEvent(AuditEventRequest request) throws IOException, EndpointException {
		logger.debug("addAuditEvent START");
		try {
			//get the audit event from the request
			AuditEvent requestAuditEvent = request.getAuditEvent();
			
			//add the audit event
			if (((Integer) requestAuditEvent.getProperties().get(IConstant.AUDIT_MODULE_ID)).intValue() == IConstant.NOM_MODULE_OM_LABEL_KEY){
				BLAuditOm.getInstance().add(requestAuditEvent.getProperties());
			} else if (((Integer) requestAuditEvent.getProperties().get(IConstant.AUDIT_MODULE_ID)).intValue() == IConstant.NOM_MODULE_DM_LABEL_KEY){
				BLAuditDm.getInstance().add(requestAuditEvent.getProperties());
			} else if (((Integer) requestAuditEvent.getProperties().get(IConstant.AUDIT_MODULE_ID)).intValue() == IConstant.NOM_MODULE_TS_LABEL_KEY){
				BLAuditTs.getInstance().add(requestAuditEvent.getProperties());
			} else if (((Integer) requestAuditEvent.getProperties().get(IConstant.AUDIT_MODULE_ID)).intValue() == IConstant.NOM_MODULE_CM_LABEL_KEY){
				BLAuditCm.getInstance().add(requestAuditEvent.getProperties());
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_ADD_AUDIT_EVENT, e);
		}
		logger.debug("addAuditEvent END");
	}
	
	/**
	 * @author coni
	 * Endpoint method for audit events reports data requests
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "AuditEventsReportDatasouceRequest", namespace = IConstant.AUDIT_WS_SERVER_NAMESPACE)
	public AuditEventsReportDatasouceResponse getAuditEventsReportData(AuditEventsReportDatasouceRequest request) throws EndpointException {
		logger.debug("getAuditEventsReportData START");
		AuditEventsReportDatasouceResponse response = new AuditEventsReportDatasouceResponse();
		try {
			//get the retrieve data criteria from the request
			AuditEventsReportGetDataCriteria getDataCriteria = request.getGetDataCriteria();
			//get the report's data
			List<Object> data = BLReportsDataSource.getInstance().getAuditEventsReportData(getDataCriteria);
			//create the response
			response.setAuditEvent(data);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_AUDIT_EVENTS_REPORT_DATA, e);
		}
		logger.debug("getAuditEventsReportData END");
		return response;
	} 
	
}
