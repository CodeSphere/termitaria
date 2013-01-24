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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import ro.cs.ts.common.ConfigParametersProvider;

/**
 * Contains the values necessary for the generation of the Project Report
 * @author alexandru.dobre
 *
 */
public class ReportProjectCriteria {
	
	//report criteria	
	private String reportTitle;
	private int projectId; //both project ID and name
	private String projectName;
	
	private Date reportStartDate;
	private Date reportEndDate;
	private byte priceCompute; //per activity or per resource
	
	
	private byte subtotalPeriod; //from nomenclator (daily, weekly, monthly ...)

	private int currencyId; // both currency ID and name
	private String currencyName;
	private Character billable; // Y or N

	private String reportLanguage;
	private String reportFileType;
		
	
	//record  columns keys and labels
	private Map <String,String> recordColumns ;
	
	//cost  columns keys and labels
	private Map <String,String> costColumns;
	
	// total lables
	
	private String totalCostPrice;
	private String totalBillingPrice;
	
	private String uniqueId; // used for putting and retrieving the bean from context
	
	public ReportProjectCriteria() {
		this.billable = new Character(' ');
	}
	
	
	public String getReportTitle() {
		return reportTitle;
	}



	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
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


	public String getProjectName() {
		return projectName;
	}



	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public int getProjectId() {
		return projectId;
	}



	public void setProjectId(int projectId) {
		this.projectId = projectId;
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


    public String getTotalCostPrice() {
		return totalCostPrice;
	}



	public void setTotalCostPrice(String totalCostPrice) {
		this.totalCostPrice = totalCostPrice;
	}



	public String getTotalBillingPrice() {
		return totalBillingPrice;
	}



	public void setTotalBillingPrice(String totalBillingPrice) {
		this.totalBillingPrice = totalBillingPrice;
	}

	public byte getPriceCompute() {
		return priceCompute;
	}



	public void setPriceCompute(byte priceCompute) {
		this.priceCompute = priceCompute;
	}



	public byte getSubtotalPeriod() {
		return subtotalPeriod;
	}



	public void setSubtotalPeriod(byte subtotalPeriod) {
		this.subtotalPeriod = subtotalPeriod;
	}
	

	public Map<String, String> getRecordColumns() {
		return recordColumns;
	}



	public Character getBillable() {
		return billable;
	}


	public void setBillable(Character billable) {
		this.billable = billable;
	}


	public void setRecordColumns(Map<String, String> recordColumns) {
		this.recordColumns = recordColumns;
	}



	public Map<String, String> getCostColumns() {
		return costColumns;
	}



	public void setCostColumns(Map<String, String> costColumns) {
		this.costColumns = costColumns;
	}
	
	
	
	public String getUniqueId() {
		return uniqueId;
	}



	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}



/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
   @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		SimpleDateFormat sdf = new SimpleDateFormat(ConfigParametersProvider.getConfigString("date.format"));
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("reportTitle = ")          	.append(reportTitle)		    .append(", ");
		sb.append("projectId = ")				.append(projectId)		    	.append(", ");
		sb.append("projectName = ")				.append(projectName)		    .append(", ");
		sb.append("reportStartDate = ")			.append(reportStartDate != null?sdf.format(reportStartDate):"null")		.append(", ");
				
		sb.append("reportEndDate = ")			.append(reportEndDate != null? sdf.format(reportEndDate):"null").append(", ");
				
		sb.append("subtotalPeriod = ")			.append(subtotalPeriod)			.append(", ");
		sb.append("priceCompute = ")     		.append(priceCompute)    		.append(", ");
		sb.append("currencyId = ")				.append(currencyId)		    	.append(", ");
		sb.append("currencyName = ")			.append(currencyName)		    .append(", ");
		sb.append("billable = ")          		.append(billable)				.append(", ");
		sb.append("reportLanguage = ")          .append(reportLanguage)		    .append(", ");
		sb.append("reportFileType = ")          .append(reportFileType)		    .append(", ");
		
		sb.append("Record columns = ");
		if (recordColumns == null || recordColumns.size() == 0) sb.append("empty, ");
		else {
			for (Entry<String, String> entry: recordColumns.entrySet()){
				sb.append("key = "+entry.getKey()+", value: "+entry.getValue()+", ");
			}
		}
		
		sb.append("Cost columns = ");
		if (costColumns == null || costColumns.size() == 0) sb.append("empty, ");
		else {
			for (Entry<String, String> entry: costColumns.entrySet()){
				sb.append("key = "+entry.getKey()+", value: "+entry.getValue()+", ");
			}
		}
		
		
		sb.append("totalCostPrice = ")     		.append(totalCostPrice)    		.append(", ");
		sb.append("totalBillingPrice = ")     	.append(totalBillingPrice)    	.append(", ");
		sb.append("uniqueId = ")     			.append(uniqueId)    			.append("] ");
			
		return sb.toString();
	}       
	

}
