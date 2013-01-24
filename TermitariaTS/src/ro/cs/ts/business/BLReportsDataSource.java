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
package ro.cs.ts.business;

import java.util.ArrayList;
import java.util.List;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.Record;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoCostSheet;
import ro.cs.ts.model.dao.IDaoRecord;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.ws.server.entity.TSReportGetDataCriteria;
import ro.cs.ts.ws.server.entity.WSCostSheet;
import ro.cs.ts.ws.server.entity.WSCostSheets;
import ro.cs.ts.ws.server.entity.WSRecord;
import ro.cs.ts.ws.server.entity.WSRecords;

/**
 * Singleton which expose business methods for TS as Reports Data Source
 * 
 * @author coni
 */
public class BLReportsDataSource extends BusinessLogic {
	
	private static IDaoRecord recordDao = DaoBeanFactory.getInstance().getDaoRecord();
	private static IDaoCostSheet costSheetDao = DaoBeanFactory.getInstance().getDaoCostSheet();
	
	//singleton implementation
    private static BLReportsDataSource theInstance = null;
    private BLReportsDataSource(){};
    static {
        theInstance = new BLReportsDataSource();
    }
    public static BLReportsDataSource getInstance() {
        return theInstance;
    }

    
    /**
     * Gets the Project Report records
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws BusinessException
     */
    public WSRecords getProjectReportRecords(TSReportGetDataCriteria getDataCriteria) throws BusinessException {
    	logger.debug("getProjectReportRecords - START");
    	WSRecords result = new WSRecords();
    	try {
			//the prices can be computed per activity or per resource
			Integer priceComputeType = (Integer) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE);
			//the report currency id
			Integer reportCurrencyId = (Integer) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_CURRENCY_ID);
			
    		List<Record> records = recordDao.getProjectReportRecords(getDataCriteria);
    		List<WSRecord> wsRecords = new ArrayList<WSRecord>();
    		if (records != null && !records.isEmpty()) {
    			for (Record rec : records) {
    				WSRecord wsRecord = new WSRecord();
    				//if one column label property is missing, it means the user doesn't want to display the column, so I must not add
    				//the corresponding info to the records list
    				
    				//only the recordOwnerName, activity and startTime are required for sorting the results 
   					wsRecord.setRecordOwnerName(rec.getRecordOwnerName());
   					wsRecord.setActivityName(rec.getActivity().getName());

   					if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_TIME) != null) {
    					//the time is in HH:mm format
    					String time = rec.getTime();
    					if (time != null && !time.equals("")) {
    						wsRecord.setTime(Integer.parseInt(time.substring(0, time.indexOf(":"))) * 60 + Integer.parseInt(time.substring(time.indexOf(":") + 1, time.length())));
    					}
    				}
    				//the startTime is needed to group the records by it; all the records with no startTime will be grouped at the beginning of the records table 
    				if (rec.getStartTime() != null) {
    					wsRecord.setStartTime(rec.getStartTime());
    				} 
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_END_TIME) != null) {
    					wsRecord.setEndTime(rec.getEndTime());
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_TIME) != null) {
    					//the time is in HH:mm format
    					String overTimeTime = rec.getOverTimeTime();
    					if (overTimeTime != null && !overTimeTime.equals("")) {
    						wsRecord.setOverTimeTime(Integer.parseInt(overTimeTime.substring(0, overTimeTime.indexOf(":"))) * 60 + Integer.parseInt(overTimeTime.substring(overTimeTime.indexOf(":") + 1, overTimeTime.length())));
    					}
    				}
    				if (rec.getOverTimeStartTime() != null) {
    					wsRecord.setOverTimeStartTime(rec.getOverTimeStartTime());
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_END_TIME) != null) {
    					wsRecord.setOverTimeEndTime(rec.getOverTimeEndTime());
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_COST_PRICE) != null) {
    					handleAddWSRecordCostPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_BILLABLE) != null) {
    					if (rec.getBillable() != null) {
    						wsRecord.setBillable(rec.getBillable().toString());
    					}
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE) != null) {
    					handleAddWSRecordBillingPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE) != null) {
    					handleAddWSRecordOverTimeCostPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLABLE) != null) {
    					if (rec.getOverTimeBillable() != null) {
    						wsRecord.setOverTimeBillable(rec.getOverTimeBillable().toString());
    					}	
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE) != null) {
    					handleAddWSRecordOverTimeBillingPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				wsRecords.add(wsRecord);
    			}
    		}
    		result.setWsRecords(wsRecords);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.REPORTS_DS_PROJECT_REPORT_RECORDS, e);
    	}
    	logger.debug("getProjectReportRecords - END");
    	return result;
    }
    
    /**
     * Gets the Project Report cost sheets
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws BusinessException
     */
    public WSCostSheets getProjectReportCostSheets(TSReportGetDataCriteria getDataCriteria) throws BusinessException {
    	logger.debug("getProjectReportCostSheets - START");
    	WSCostSheets result = new WSCostSheets();
    	try {
			//the report currency id
			Integer reportCurrencyId = (Integer) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_CURRENCY_ID);
			//the project id
			Integer projectId = (Integer) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_ID);
			
    		List<CostSheet> costSheets = costSheetDao.getProjectReportCostSheets(getDataCriteria);
    		List<WSCostSheet> wsCostSheets = new ArrayList<WSCostSheet>();
    		if (costSheets != null && !costSheets.isEmpty()) {
    			for (CostSheet cost : costSheets) {
    				WSCostSheet wsCostSheet = new WSCostSheet();
    				//if one column label property is missing, it means the user doesn't want to display the column, so I must not add
    				//the corresponding info to the cost sheets list
    				
    				//only the costSheetReporterName, activity and date required for sorting the results in the table
   					wsCostSheet.setCostSheetReporterName(cost.getCostSheetReporterName());
   					wsCostSheet.setActivityName(cost.getActivityName());
   					wsCostSheet.setDate(cost.getDate());

    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_BILLABLE) != null) {
    					if (cost.getBillable() != null) {	
    						wsCostSheet.setBillable(cost.getBillable().toString());
    					}
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_COST_PRICE) != null) {
    					Float costPrice = cost.getCostPrice();
    					Integer costPriceCurrencyId = cost.getCostPriceCurrencyId();
    					if (!reportCurrencyId.equals(costPriceCurrencyId)) {
    						Exchange exchange = BLExchange.getInstance().getProjectExchangeByCurrencies(costPriceCurrencyId, reportCurrencyId, BLProjectDetails.getInstance().getByProjectId(projectId).getProjectDetailId());
    						if (exchange != null) {
    							costPrice = costPrice * exchange.getRate();
    							wsCostSheet.setCostPrice(costPrice);
    						} else {
    							//if there is no exchange defined for the two currencies, i set the cost price value to -1, in order to inform the user in the report
    							wsCostSheet.setCostPrice(new Float(-1));
    						}
    					} else {
    						wsCostSheet.setCostPrice(costPrice);
    					}
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_COLUMN_LABEL_COST_SHEET_BILLING_PRICE) != null) {
    					Float billingPrice = cost.getBillingPrice();
    					Integer billingPriceCurrencyId = cost.getBillingPriceCurrencyId();
    					if (billingPriceCurrencyId != null && billingPrice != null) {
	    					if (!reportCurrencyId.equals(billingPriceCurrencyId)) {
	    						Exchange exchange = BLExchange.getInstance().getProjectExchangeByCurrencies(billingPriceCurrencyId, reportCurrencyId, BLProjectDetails.getInstance().getByProjectId(projectId).getProjectDetailId());
	    						if (exchange != null) {
	    							billingPrice = billingPrice * exchange.getRate();
	    							wsCostSheet.setBillingPrice(billingPrice);
	    						} else {
	    							//if there is no exchange defined for the two currencies, i set the billing price value to -1, in order to inform the user in the report
	    							wsCostSheet.setBillingPrice(new Float(-1));
	    						}
	    					} else {
	    						wsCostSheet.setBillingPrice(billingPrice);
	    					}
    					} else {
    						//if there is no billing price defined, i set the billing price value to -1, in order to inform the user in the report
    						wsCostSheet.setBillingPrice(new Float(-1));
    					}
    				}
    				wsCostSheets.add(wsCostSheet);
    			}
    		}
    		result.setWsCostSheets(wsCostSheets);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.REPORTS_DS_PROJECT_REPORT_COST_SHEETS, e);
    	}
    	logger.debug("getProjectReportCostSheets - END");
    	return result;
    }
    
    /**
     * Gets the Time Sheet Report records
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws BusinessException
     */
    public WSRecords getTimeSheetReportRecords(TSReportGetDataCriteria getDataCriteria) throws BusinessException {
    	logger.debug("getTimeSheetReportRecords - START");
    	WSRecords result = new WSRecords();
    	try {
			//the prices can be computed per activity or per resource
			Integer priceComputeType = (Integer) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE);
			//the report currency id
			Integer reportCurrencyId = (Integer) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_CURRENCY_ID);
			
    		List<Record> records = recordDao.getTimeSheetReportRecords(getDataCriteria);
    		List<WSRecord> wsRecords = new ArrayList<WSRecord>();
    		if (records != null && !records.isEmpty()) {
    			for (Record rec : records) {
    				WSRecord wsRecord = new WSRecord();
    				
    				//if one column label property is missing, it means the user doesn't want to display the column, so I must not add
    				//the corresponding info to the records list
    				
    				//only the recordOwnerName, activity and startTime are required for sorting the results in the report table
   					wsRecord.setRecordOwnerName(rec.getRecordOwnerName());
   					wsRecord.setActivityName(rec.getActivity().getName());

   					if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_TIME) != null) {
    					//the time is in HH:mm format
    					String time = rec.getTime();
    					if (time != null && !time.equals("")) {
    						wsRecord.setTime(Integer.parseInt(time.substring(0, time.indexOf(":"))) * 60 + Integer.parseInt(time.substring(time.indexOf(":") + 1, time.length())));
    					}
    				}
    				//the startTime is needed to group the records by it; all the records with no startTime will be grouped at the beginning of the records table
    				if (rec.getStartTime() != null) {
    					wsRecord.setStartTime(rec.getStartTime());
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_END_TIME) != null) {
    					wsRecord.setEndTime(rec.getEndTime());
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_TIME) != null) {
    					//the time is in HH:mm format
    					String overTimeTime = rec.getOverTimeTime();
    					if (overTimeTime != null && !overTimeTime.equals("")) {
    						wsRecord.setOverTimeTime(Integer.parseInt(overTimeTime.substring(0, overTimeTime.indexOf(":"))) * 60 + Integer.parseInt(overTimeTime.substring(overTimeTime.indexOf(":") + 1, overTimeTime.length())));
    					}
    				}
    				if (rec.getOverTimeStartTime() != null) {
    					wsRecord.setOverTimeStartTime(rec.getOverTimeStartTime());
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_END_TIME) != null) {
    					wsRecord.setOverTimeEndTime(rec.getOverTimeEndTime());
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_COST_PRICE) != null) {
    						handleAddWSRecordCostPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_BILLABLE) != null) {
    					if (rec.getBillable() != null) {
    						wsRecord.setBillable(rec.getBillable().toString());
    					}
    				}
    				//only if the record isn't for organization i will set the billing price 
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_BILLING_PRICE) != null && rec.getTeamMemberDetail() != null) {
    					handleAddWSRecordBillingPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_COST_PRICE) != null) {
    					handleAddWSRecordOverTimeCostPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLABLE) != null) {
    					if (rec.getOverTimeBillable() != null) {
    						wsRecord.setOverTimeBillable(rec.getOverTimeBillable().toString());
    					}
    				}
    				//only if the record isn't for organization i will set the billing price 
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_OVER_TIME_BILLING_PRICE) != null && rec.getTeamMemberDetail() != null) {
    					handleAddWSRecordOverTimeBillingPrice(priceComputeType, reportCurrencyId, rec, wsRecord);
    				}
    				//set the project name only if the record isn't for organization
    				if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_COLUMN_LABEL_RECORD_PROJECT_NAME) != null && rec.getTeamMemberDetail() != null) {
    					wsRecord.setProjectName(rec.getProjectName());
    				}
    				wsRecords.add(wsRecord);
    			}
    		}
    		result.setWsRecords(wsRecords);
    	} catch (Exception e) {
    		throw new BusinessException(ICodeException.REPORTS_DS_TIME_SHEET_REPORT_RECORDS, e);
    	}
    	logger.debug("getTimeSheetReportRecords - END");
    	return result;
    }
    
    /**
     * Computes one record cost price and sets it to the wsRecord entity 
     * @author Coni
     * @param priceComputeType
     * @param reportCurrencyId
     * @param rec
     * @param wsRecord
     * @throws BusinessException
     */
    public void handleAddWSRecordCostPrice(Integer priceComputeType, Integer reportCurrencyId, Record rec, WSRecord wsRecord) throws BusinessException {
    	logger.debug("handleAddWSRecordCostPrice - START");
		if (rec.getTime() != null && !rec.getTime().equals("")) {
			Float costPrice = null;
			Short timeUnit = null;
			Integer costPriceCurrencyId = null;
			boolean isSetToZero = false;
			if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
				costPrice = rec.getActivity().getCostPrice();
				timeUnit = rec.getActivity().getCostTimeUnit();
				if (rec.getActivity().getCostPriceCurrency() != null) {
					costPriceCurrencyId = rec.getActivity().getCostPriceCurrency().getCurrencyId();
				}
			} else if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE)) {
				if (rec.getTeamMemberDetail() != null) {
					costPrice = rec.getTeamMemberDetail().getCostPrice();
					timeUnit = rec.getTeamMemberDetail().getCostTimeUnit();
					if (rec.getTeamMemberDetail().getCostPriceCurrency() != null) {
						costPriceCurrencyId = rec.getTeamMemberDetail().getCostPriceCurrency().getCurrencyId();
					}
				} else if (rec.getPersonDetail() != null) {
					costPrice = rec.getPersonDetail().getCostPrice();
					timeUnit = rec.getPersonDetail().getCostTimeUnit();
					if (rec.getPersonDetail().getCostPriceCurrency() != null) {
						costPriceCurrencyId = rec.getPersonDetail().getCostPriceCurrency().getCurrencyId();
					}
				}
			}
			if (costPrice != null && costPriceCurrencyId != null) {
				//if the report currency is different from the activity cost one, i have to make the exchange 
				if (!costPriceCurrencyId.equals(reportCurrencyId)) {
					Exchange exchange = null;
					//if the record is for a team member i must retrieve the exchange for the selected currencies and the member's project,
					//otherwise for the person's organization
					if (rec.getTeamMemberDetail() != null) {
						exchange = BLExchange.getInstance().getProjectExchangeByCurrencies(costPriceCurrencyId, reportCurrencyId, rec.getProjectDetails().getProjectDetailId());
					} else if (rec.getPersonDetail() != null) {
						exchange = BLExchange.getInstance().getOrganizationExchangeByCurrencies(costPriceCurrencyId, reportCurrencyId, rec.getOrganizationId());
					}
					if (exchange != null) {
						costPrice = costPrice * exchange.getRate();
					} else {
						//if there is no exchange defined for the two currencies, i set the cost price value to -1, in order to inform the user in the report
						wsRecord.setCostPrice(new Float(-1));
						isSetToZero = true;
					}
				}
			} else {
				//if the activity cost price isn't defined, i set the cost price value to -1, in order to inform the user in the report
				wsRecord.setCostPrice(new Float(-1));
				isSetToZero = true;
			}
			if (!isSetToZero) {
				if (rec.getStartTime() != null && rec.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(rec.getStartTime(), rec.getEndTime(), rec.getTime())) {
					wsRecord.setCostPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(costPrice, timeUnit, rec.getStartTime(), rec.getEndTime())));
				} else {
					wsRecord.setCostPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(costPrice, timeUnit, rec.getTime())));
				}
			}
		}
    	logger.debug("handleAddWSRecordCostPrice - END");
    }
    
    /**
     * Computes one record billing price and sets it to the wsRecord entity 
     * @author Coni
     * @param priceComputeType
     * @param reportCurrencyId
     * @param rec
     * @param wsRecord
     * @throws BusinessException
     */
    public void handleAddWSRecordBillingPrice(Integer priceComputeType, Integer reportCurrencyId, Record rec, WSRecord wsRecord) throws BusinessException {
    	logger.debug("handleAddWSRecordBillingPrice - START");
		if (rec.getTime() != null && !rec.getTime().equals("")) {
			Float billingPrice = null;
			Short timeUnit = null;
			Integer billingPriceCurrencyId = null;
			boolean isSetToZero = false;
			if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
				billingPrice = rec.getActivity().getBillingPrice();
				timeUnit = rec.getActivity().getBillingTimeUnit();
				if (rec.getActivity().getBillingPriceCurrency() != null) {
					billingPriceCurrencyId = rec.getActivity().getBillingPriceCurrency().getCurrencyId();
				}
			} else if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE)) {
				if (rec.getTeamMemberDetail() != null) {
					billingPrice = rec.getTeamMemberDetail().getBillingPrice();
					timeUnit = rec.getTeamMemberDetail().getBillingTimeUnit();
					if (rec.getTeamMemberDetail().getBillingPriceCurrency() != null) {
						billingPriceCurrencyId = rec.getTeamMemberDetail().getBillingPriceCurrency().getCurrencyId();
					}
				} else if (rec.getPersonDetail() != null) {
					//if the record is for a personDetail, it means it is for own organization and it is not billable
					wsRecord.setBillingPrice(null);
					isSetToZero = true;
				}
			}
			if (billingPrice != null && billingPriceCurrencyId != null && !isSetToZero) {
				//if the report currency is different from the activity cost one, i have to make the exchange 
				if (!billingPriceCurrencyId.equals(reportCurrencyId)) {
					Exchange exchange = BLExchange.getInstance().getProjectExchangeByCurrencies(billingPriceCurrencyId, reportCurrencyId, rec.getProjectDetails().getProjectDetailId());
					if (exchange != null) {
						billingPrice = billingPrice * exchange.getRate();
					} else {
						//if there is no exchange defined for the two currencies, i set the cost price value to -1, in order to inform the user in the report
						wsRecord.setBillingPrice(new Float(-1));
						isSetToZero = true;
					}
				}
			} else {
				//if the activity cost price isn't defined, i set the cost price value to -1, in order to inform the user in the report
				wsRecord.setBillingPrice(new Float(-1));
				isSetToZero = true;
			}
			if (!isSetToZero) {
				if (rec.getStartTime() != null && rec.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(rec.getStartTime(), rec.getEndTime(), rec.getTime())) {
					wsRecord.setBillingPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(billingPrice, timeUnit, rec.getStartTime(), rec.getEndTime())));
				} else {
					wsRecord.setBillingPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(billingPrice, timeUnit, rec.getTime())));
				}
			}
		}
    	logger.debug("handleAddWSRecordBillingPrice - END");
    }
    
    /**
     * Computes one record over time cost price and sets it to the wsRecord entity 
     * @author Coni
     * @param priceComputeType
     * @param reportCurrencyId
     * @param rec
     * @param wsRecord
     * @throws BusinessException
     */
    public void handleAddWSRecordOverTimeCostPrice(Integer priceComputeType, Integer reportCurrencyId, Record rec, WSRecord wsRecord) throws BusinessException {
    	logger.debug("handleAddWSRecordOverTimeCostPrice - START");
		if (rec.getOverTimeTime() != null && !rec.getOverTimeTime().equals("")) {
			Float costPrice = null;
			Short timeUnit = null;
			Integer costPriceCurrencyId = null;
			boolean isSetToZero = false;
			if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
				costPrice = rec.getActivity().getCostPrice();
				timeUnit = rec.getActivity().getCostTimeUnit();
				if (rec.getActivity().getCostPriceCurrency() != null) {
					costPriceCurrencyId = rec.getActivity().getCostPriceCurrency().getCurrencyId();
				}
			} else if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE)) {
				if (rec.getTeamMemberDetail() != null) {
					costPrice = rec.getTeamMemberDetail().getOvertimeCostPrice();
					timeUnit = rec.getTeamMemberDetail().getOvertimeCostTimeUnit();
					if (rec.getTeamMemberDetail().getOvertimeCostCurrency() != null) {
						costPriceCurrencyId = rec.getTeamMemberDetail().getOvertimeCostCurrency().getCurrencyId();
					}
				} else if (rec.getPersonDetail() != null) {
					costPrice = rec.getPersonDetail().getOvertimeCostPrice();
					timeUnit = rec.getPersonDetail().getOvertimeCostTimeUnit();
					if (rec.getPersonDetail().getOvertimeCostCurrency() != null) {
						costPriceCurrencyId = rec.getPersonDetail().getOvertimeCostCurrency().getCurrencyId();
					}
				}
			}
			if (costPrice != null && costPriceCurrencyId != null) {
				//if the report currency is different from the activity cost one, i have to make the exchange 
				if (!costPriceCurrencyId.equals(reportCurrencyId)) {
					Exchange exchange = null;
					if (rec.getTeamMemberDetail() != null) {
						exchange = BLExchange.getInstance().getProjectExchangeByCurrencies(costPriceCurrencyId, reportCurrencyId, rec.getProjectDetails().getProjectDetailId());
					} else if (rec.getPersonDetail() != null) {
						exchange = BLExchange.getInstance().getOrganizationExchangeByCurrencies(costPriceCurrencyId, reportCurrencyId, rec.getOrganizationId());
					}
					if (exchange != null) {
						costPrice = costPrice * exchange.getRate();
					} else {
						//if there is no exchange defined for the two currencies, i set the cost price value to -1, in order to inform the user in the report
						wsRecord.setOverTimeCostPrice(new Float(-1));
						isSetToZero = true;
					}
				}
			} else {
				//if the activity cost price isn't defined, i set the cost price value to -1, in order to inform the user in the report
				wsRecord.setOverTimeCostPrice(new Float(-1));
				isSetToZero = true;
			}
			if (!isSetToZero) {
				if (rec.getOverTimeStartTime() != null && rec.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(rec.getOverTimeStartTime(), rec.getOverTimeEndTime(), rec.getOverTimeTime())) {
					wsRecord.setOverTimeCostPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(costPrice, timeUnit, rec.getOverTimeStartTime(), rec.getOverTimeEndTime())));
				} else {
					wsRecord.setOverTimeCostPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(costPrice, timeUnit, rec.getOverTimeTime())));
				}
			}
		}
    	logger.debug("handleAddWSRecordOverTimeCostPrice - END");
    }
    
    /**
     * Computes one record over time billing price and sets it to the wsRecord entity 
     * @author Coni
     * @param priceComputeType
     * @param reportCurrencyId
     * @param rec
     * @param wsRecord
     * @throws BusinessException
     */
    public void handleAddWSRecordOverTimeBillingPrice(Integer priceComputeType, Integer reportCurrencyId, Record rec, WSRecord wsRecord) throws BusinessException {
    	logger.debug("handleAddWSRecordOverTimeBillingPrice - START");
		if (rec.getOverTimeTime() != null && !rec.getOverTimeTime().equals("")) {
			Float billingPrice = null;
			Short timeUnit = null;
			Integer billingPriceCurrencyId = null;
			boolean isSetToZero = false;
			if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
				billingPrice = rec.getActivity().getBillingPrice();
				timeUnit = rec.getActivity().getBillingTimeUnit();
				if (rec.getActivity().getBillingPriceCurrency() != null) {
					billingPriceCurrencyId = rec.getActivity().getBillingPriceCurrency().getCurrencyId();
				}
			} else if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE)) {
				if (rec.getTeamMemberDetail() != null) {
					billingPrice = rec.getTeamMemberDetail().getOvertimeBillingPrice();
					timeUnit = rec.getTeamMemberDetail().getOvertimeBillingTimeUnit();
					if (rec.getTeamMemberDetail().getOvertimeBillingCurrency() != null) { 
						billingPriceCurrencyId = rec.getTeamMemberDetail().getOvertimeBillingCurrency().getCurrencyId();
					}
				} else if (rec.getPersonDetail() != null) {
					//if the record is for a personDetail, it means it is for own organization and it is not billable
					wsRecord.setOverTimeBillingPrice(null);
					isSetToZero = true;
				}
			}
			if (billingPrice != null && billingPriceCurrencyId != null && !isSetToZero) {
				//if the report currency is different from the activity cost one, i have to make the exchange 
				if (!billingPriceCurrencyId.equals(reportCurrencyId)) {
					Exchange exchange = BLExchange.getInstance().getProjectExchangeByCurrencies(billingPriceCurrencyId, reportCurrencyId, rec.getProjectDetails().getProjectDetailId());
					if (exchange != null) {
						billingPrice = billingPrice * exchange.getRate();
					} else {
						//if there is no exchange defined for the two currencies, i set the cost price value to -1, in order to inform the user in the report
						wsRecord.setOverTimeBillingPrice(new Float(-1));
						isSetToZero = true;
					}
				}
			} else {
				//if the activity cost price isn't defined, i set the cost price value to -1, in order to inform the user in the report
				wsRecord.setOverTimeBillingPrice(new Float(-1));
				isSetToZero = true;
			}
			if (!isSetToZero) {
				if (rec.getOverTimeStartTime() != null && rec.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(rec.getOverTimeStartTime(), rec.getOverTimeEndTime(), rec.getOverTimeTime())) {
					wsRecord.setOverTimeBillingPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(billingPrice, timeUnit, rec.getOverTimeStartTime(), rec.getOverTimeEndTime())));
				} else {
					wsRecord.setOverTimeBillingPrice(Float.valueOf(ControllerUtils.getInstance().calculatePrice(billingPrice, timeUnit, rec.getOverTimeTime())));
				}
			}
		}
    	logger.debug("handleAddWSRecordOverTimeBillingPrice - END");
    }

    
}
