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
package ro.cs.om.thread;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.ws.client.audit.AuditWebServiceClient;
import ro.cs.om.ws.client.audit.entity.AuditEvent;

/**
 * 
 * Thread class used to send a new om audit event to the audit module web service
 * @author coni
 *
 */
public class AuditThread extends ApplicationObjectSupport implements Runnable {
	
	private AuditEvent auditEvent;
	private String event;
	private String firstName;
	private String lastName;
	private String messageEN;
	private String messageRO;
	private int organisationId;
	private int personId;

	public AuditEvent getAuditEvent() {
		return auditEvent;
	}

	public void setAuditEvent(AuditEvent auditEvent) {
		this.auditEvent = auditEvent;
	}
	
	public AuditThread(AuditEvent auditEvent){
		this.auditEvent = auditEvent;
	}
	
	public AuditThread(String event, String firstName, String lastName, String messageEN, String messageRO, int organisationId, int personId){
		this.event = event;
		this.firstName = firstName;
		this.lastName = lastName;
		this.messageEN = messageEN;
		this.messageRO = messageRO;
		this.organisationId = organisationId;
		this.personId = personId;
	}
	
	public void run() {
		logger.debug("Start sending om audit event... ");
		try {
			Boolean hasAudit = ((Map<Integer, Boolean>) OMContext.getFromContext(IConstant.HAS_AUDIT_CONTEXT_MAP)).get(this.organisationId);
			if (hasAudit) {
				this.auditEvent = new AuditEvent();
				HashMap<String, Object> properties = new HashMap<String, Object>();
				properties.put(IModelConstant.auditDate, new Date());
				properties.put(IModelConstant.auditEvent, event);
				properties.put(IModelConstant.auditFirstName, firstName);
				properties.put(IModelConstant.auditLastName, lastName);
				properties.put(IModelConstant.auditMessageEN, messageEN);
				properties.put(IModelConstant.auditMessageRO, messageRO);
				properties.put(IModelConstant.auditModuleId, IConstant.MODULE_ID);
				properties.put(IModelConstant.auditOrganisationId, organisationId);
				properties.put(IModelConstant.auditPersonId, personId);
				this.auditEvent.setProperties(properties);
				AuditWebServiceClient.getInstance().addAuditEvent(this.auditEvent);
			}
		} catch (Exception exc){
			logger.error("Error while sending the OM audit event", exc);
		}
		logger.debug("End sending om audit event... ");
	}
}
