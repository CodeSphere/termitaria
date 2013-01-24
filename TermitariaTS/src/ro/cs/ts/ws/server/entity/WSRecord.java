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
package ro.cs.ts.ws.server.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSRecord", propOrder = {
	})
public class WSRecord {
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String recordOwnerName; // the user that sets the record
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String activityName;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Integer time;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Date startTime;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Date endTime;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Integer overTimeTime;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Date overTimeStartTime;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Date overTimeEndTime;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Float costPrice;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String billable;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Float billingPrice;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Float overTimeCostPrice;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String overTimeBillable;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Float overTimeBillingPrice;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String projectName;

	public String getRecordOwnerName() {
		return recordOwnerName;
	}

	public void setRecordOwnerName(String recordOwnerName) {
		this.recordOwnerName = recordOwnerName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getOverTimeTime() {
		return overTimeTime;
	}

	public void setOverTimeTime(int overTimeTime) {
		this.overTimeTime = overTimeTime;
	}

	public Date getOverTimeStartTime() {
		return overTimeStartTime;
	}

	public void setOverTimeStartTime(Date overTimeStartTime) {
		this.overTimeStartTime = overTimeStartTime;
	}

	public Date getOverTimeEndTime() {
		return overTimeEndTime;
	}

	public void setOverTimeEndTime(Date overTimeEndTime) {
		this.overTimeEndTime = overTimeEndTime;
	}

	public Float getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Float costPrice) {
		this.costPrice = costPrice;
	}

	public String getBillable() {
		return billable;
	}

	public void setBillable(String billable) {
		this.billable = billable;
	}

	public Float getBillingPrice() {
		return billingPrice;
	}

	public void setBillingPrice(Float billingPrice) {
		this.billingPrice = billingPrice;
	}

	public Float getOverTimeCostPrice() {
		return overTimeCostPrice;
	}

	public void setOverTimeCostPrice(Float overTimeCostPrice) {
		this.overTimeCostPrice = overTimeCostPrice;
	}

	public String getOverTimeBillable() {
		return overTimeBillable;
	}

	public void setOverTimeBillable(String overTimeBillable) {
		this.overTimeBillable = overTimeBillable;
	}

	public Float getOverTimeBillingPrice() {
		return overTimeBillingPrice;
	}

	public void setOverTimeBillingPrice(Float overTimeBillingPrice) {
		this.overTimeBillingPrice = overTimeBillingPrice;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
}
