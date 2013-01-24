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
package ro.cs.ts.thread;

import java.util.Date;
import java.util.HashMap;

import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.ws.client.audit.AuditWebServiceClient;
import ro.cs.ts.ws.client.audit.entity.AuditEvent;
import ro.cs.ts.ws.client.om.OMWebServiceClient;

/**
 * 
 * Thread class used to send a new ts audit event to the audit module web service
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
		logger.debug("Start sending TS audit event... ");
		try {
			Boolean hasAudit = OMWebServiceClient.getInstance().organisationHasAuditModule(this.organisationId);
			if (hasAudit) {
				this.auditEvent = new AuditEvent();
				HashMap<String, Object> properties = new HashMap<String, Object>();
				properties.put(IConstant.auditDate, new Date());
				properties.put(IConstant.auditEvent, event);
				properties.put(IConstant.auditFirstName, firstName);
				properties.put(IConstant.auditLastName, lastName);
				properties.put(IConstant.auditMessageEN, messageEN);
				properties.put(IConstant.auditMessageRO, messageRO);
				properties.put(IConstant.auditModuleId, IConstant.MODULE_ID);
				properties.put(IConstant.auditOrganisationId, organisationId);
				properties.put(IConstant.auditPersonId, personId);
				this.auditEvent.setProperties(properties);
				AuditWebServiceClient.getInstance().addAuditEvent(this.auditEvent);
			}
		} catch (Exception exc){
			logger.error("Error while sending the TS audit event", exc);
		}
		logger.debug("End sending TS audit event... ");
	}
}
