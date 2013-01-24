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
@XmlType(name = "WSCostSheet", propOrder = {
	})
public class WSCostSheet {
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String costSheetReporterName;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String activityName;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Date date;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Float costPrice;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private String billable;
	
	@XmlElement(namespace = "http://localhost:8080/TS/services/schemas/messages")
	private Float billingPrice;

	public String getCostSheetReporterName() {
		return costSheetReporterName;
	}

	public void setCostSheetReporterName(String costSheetReporterName) {
		this.costSheetReporterName = costSheetReporterName;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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



}
