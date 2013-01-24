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
package ro.cs.ts.entity;

import java.util.Date;
import java.util.Map;

/**
 * Parameters needed to generate Time Sheet Report
 * @author Coni
 *
 */
public class ReportTimeSheetCriteria {

	//report criteria	
	private int currencyId; // both currency ID and name
	private String currencyName;
	private Character billable; // Y or N
	private Date reportStartDate;
	private Date reportEndDate;
	private Integer priceCompute; //per activity or per resource
	private Integer[] teamMemberIds;
	private Integer[] personIds;
	private Integer organizationId;
	
	private String reportTitle;
	private Integer subtotalPeriod; //from nomenclator (daily, weekly, monthly ...)
	private String reportLanguage;
	private String reportFileType;
	
	private String uniqueId;
	
	//record  columns keys and labels
	private Map <String,String> recordColumns ;
	
	public ReportTimeSheetCriteria() {
		this.billable = ' ';
	}
	
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public Character getBillable() {
		return billable;
	}
	public void setBillable(Character billable) {
		this.billable = billable;
	}
	public Date getReportStartDate() {
		return reportStartDate;
	}
	public void setReportStartDate(Date reportStartDate) {
		this.reportStartDate = reportStartDate;
	}
	public Date getReportEndDate() {
		return reportEndDate;
	}
	public void setReportEndDate(Date reportEndDate) {
		this.reportEndDate = reportEndDate;
	}
	public Integer[] getTeamMemberIds() {
		return teamMemberIds;
	}
	public void setTeamMemberIds(Integer[] teamMemberIds) {
		this.teamMemberIds = teamMemberIds;
	}
	public Integer[] getPersonIds() {
		return personIds;
	}
	public void setPersonIds(Integer[] personIds) {
		this.personIds = personIds;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getPriceCompute() {
		return priceCompute;
	}
	public void setPriceCompute(Integer priceCompute) {
		this.priceCompute = priceCompute;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public Integer getSubtotalPeriod() {
		return subtotalPeriod;
	}
	public void setSubtotalPeriod(Integer subtotalPeriod) {
		this.subtotalPeriod = subtotalPeriod;
	}
	public String getReportLanguage() {
		return reportLanguage;
	}
	public void setReportLanguage(String reportLanguage) {
		this.reportLanguage = reportLanguage;
	}
	public String getReportFileType() {
		return reportFileType;
	}
	public void setReportFileType(String reportFileType) {
		this.reportFileType = reportFileType;
	}
	public Map<String, String> getRecordColumns() {
		return recordColumns;
	}
	public void setRecordColumns(Map<String, String> recordColumns) {
		this.recordColumns = recordColumns;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	

	
}
