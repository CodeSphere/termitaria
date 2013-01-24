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
package ro.cs.ts.web.controller.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLRecord;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootAbstractController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.entity.UserSimple;

/**
 * Used to view info about a record
 * @author Coni
 * @author Adelina
 *
 */
public class RecordViewController extends RootAbstractController {

	//------------------------MESSAGE KEY--------------------------------------------------------------
	private final String GET_ERROR								= "record.get.error";	
	private static final String GENERAL_ERROR					= "record.general.form.error";
	private final String GET_PROJECT_ERROR						= "project.get.error";
	private static final String PRICE_NOT_DEFINED				= "record.price.not.defined";
	
	//------------------------VIEW------------------------------------------------------------------
	private static final String VIEW 							= "RecordView";
	
	//------------------------MODEL------------------------------------------------------------------	
	private static final String RECORD_ID						= "recordId";
	private static final String RECORD							= "RECORD";
	
	private static final String COST_PRICE_ACTIVITY					= "COST_PRICE_ACTIVITY";
	private static final String BILLING_PRICE_ACTIVITY				= "BILLING_PRICE_ACTIVITY";
	private static final String COST_PRICE_TEAM_MEMBER				= "COST_PRICE_TEAM_MEMBER";
	private static final String BILLING_PRICE_TEAM_MEMBER			= "BILLING_PRICE_TEAM_MEMBER";
	private static final String OVERTIME_COST_PRICE_TEAM_MEMBER		= "OVERTIME_COST_PRICE_TEAM_MEMBER";
	private static final String OVERTIME_BILLING_PRICE_TEAM_MEMBER	= "OVERTIME_BILLING_PRICE_TEAM_MEMBER";
	private static final String COST_PRICE_PERSON					= "COST_PRICE_PERSON";
	private static final String OVERTIME_COST_PRICE_PERSON			= "OVERTIME_COST_PRICE_PERSON";
	private static final String IS_MANAGER							= "IS_MANAGER";
	private static final String ACTIVITY							= "ACTIVITY";
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 50;
    
	public RecordViewController() {
		setView(VIEW);
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse result) throws Exception {
		logger.debug("handleRequestInternal - START");
		
		// used for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
				
		ModelAndView mav = new ModelAndView();
		Locale locale = RequestContextUtils.getLocale(request);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Record record = null;
		String costPriceActivity = null;
		String billingPriceActivity = null;
		String costPriceTeamMember = null;
		String billingPriceTeamMember = null;
		String overtimeCostPriceTeamMember = null;
		String overtimeBillingPriceTeamMember = null;
		String costPricePerson = null;
		String overtimeCostPricePerson = null;
		
		
		try {
			Integer recordId = ServletRequestUtils.getIntParameter(request, RECORD_ID);
			logger.debug("Record Id: ".concat(recordId.toString()));
			
			mav.addObject(RECORD_ID, recordId);
			
			if (recordId != null) {
				record = BLRecord.getInstance().getAll(recordId);										
				//set recordOwnerName - the user that sets the record
				if (record.getTeamMemberDetail() != null) {
					record.setTeamMemberDetailId(record.getTeamMemberDetail().getTeamMemberDetailId());
					TeamMember member = BLTeamMember.getInstance().getTeamMember(record.getTeamMemberDetail().getTeamMemberId(), false);
					if (member != null) {
						record.setRecordOwnerName(member.getFirstName().concat(" ").concat(member.getLastName()));
					}
				} else if (record.getPersonDetail() != null) {
					record.setPersonDetailId(record.getPersonDetail().getPersonDetailId());
					String[] personIds = new String[1];
					personIds[0] = String.valueOf(record.getPersonDetail().getPersonId());
					List<UserSimple> users = BLUser.getInstance().getUsersByPersonId(personIds);
					if (users != null) {
						record.setRecordOwnerName(users.get(0).getFirstName().concat(" ").concat(users.get(0).getLastName()));
					}
				}
				if (record.getRecordOwnerName() != null) {
					record.setRecordOwnerName(ControllerUtils.getInstance().tokenizeField(record.getRecordOwnerName(), NR_CHARS_PER_LINE));
				}
				
				//set the projectName
				if (record.getProjectDetails() != null) {
					Project project = BLProject.getInstance().getSimpleProject(record.getProjectDetails().getProjectId());
					if (project != null) {
						record.setProjectName(ControllerUtils.getInstance().tokenizeField(project.getName(), NR_CHARS_PER_LINE));
						
					}
				}

				if (record.getObservation() != null) {
					record.setObservation(ControllerUtils.getInstance().tokenizeField(record.getObservation(), NR_CHARS_PER_LINE));
				}
				
				if (record.getDescription() != null) {
					record.setDescription(ControllerUtils.getInstance().tokenizeField(record.getDescription(), NR_CHARS_PER_LINE));
				}
				
				boolean isManager = false;
				
				if(record.getProjectDetails() != null && record.getProjectDetails().getProjectId() != null && record.getProjectDetails().getProjectId() != -1) {					
					isManager = isManager(record.getProjectDetails().getProjectId(), userAuth.getPersonId(), request, errorMessages);					
				}
				
				if (record.getTime() != null && record.getTime() != "") {
					record.setWorkHoursRecord(true);
				} else {
					record.setWorkHoursRecord(false);
				}
				if (record.getOverTimeTime() != null && record.getOverTimeTime() != "") {
					record.setOvertimeRecord(true);
				} else {
					record.setOvertimeRecord(false);
				}
				
				// put on mav if the user that logs in is the manager of a specific project
				mav.addObject(IS_MANAGER, isManager);		
				
				// if the user has permission or is a manager for a specific project
				if (userAuth.hasAuthority(PermissionConstant.getTheInstance().getTS_RecordViewCosts()) || isManager) {		
					
					// ACTIVITY
					Activity activity = record.getActivity();					
					logger.debug("activity =  " + activity);
										
					mav.addObject(ACTIVITY, activity);
					
					//activity cost price
					if(activity.getCostPrice() != null) {
						Float costWorkWours = new Float(0);
						Float costOverTime = new Float(0);
						if (record.isWorkHoursRecord()) {
							if(record.getStartTime() != null && record.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getStartTime(), record.getEndTime(), record.getTime())) {
								costWorkWours = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getCostPrice(), activity.getCostTimeUnit(), record.getStartTime(), record.getEndTime()));
							} else {
								costWorkWours = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getCostPrice(), activity.getCostTimeUnit(), record.getTime()));
							}
						}
						
						if (record.isOvertimeRecord()) {
							if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime())) {
								costOverTime = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getCostPrice(), activity.getCostTimeUnit(), record.getOverTimeStartTime(), record.getOverTimeEndTime()));
							} else {
								costOverTime = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getCostPrice(), activity.getCostTimeUnit(), record.getOverTimeTime()));
							}
						}
						costPriceActivity = String.valueOf(costWorkWours + costOverTime).concat(" ").concat(activity.getCostPriceCurrency().getInitials());
						mav.addObject(COST_PRICE_ACTIVITY, costPriceActivity);		
					} else {
						mav.addObject(COST_PRICE_ACTIVITY, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
					}
					
					// activity billing price
					if(activity.getBillingPrice() != null) {
						Float billingWorkWours = new Float(0);
						Float billingOverTime = new Float(0);
						if (record.isWorkHoursRecord()) {
							if(record.getStartTime() != null && record.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getStartTime(), record.getEndTime(), record.getTime())) {
								billingWorkWours = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getBillingPrice(), activity.getBillingTimeUnit(), record.getStartTime(), record.getEndTime()));
							} else {
								billingWorkWours = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getBillingPrice(), activity.getBillingTimeUnit(), record.getTime()));
							}	
						}
						
						if (record.isOvertimeRecord()) {
							if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime())) {
								billingOverTime = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getBillingPrice(), activity.getBillingTimeUnit(), record.getOverTimeStartTime(), record.getOverTimeEndTime()));
							} else {
								billingOverTime = Float.valueOf(ControllerUtils.getInstance().calculatePrice(activity.getBillingPrice(), activity.getBillingTimeUnit(), record.getOverTimeTime()));
							}
						}
						billingPriceActivity = String.valueOf(billingWorkWours + billingOverTime).concat(" ").concat(activity.getBillingPriceCurrency().getInitials());
						mav.addObject(BILLING_PRICE_ACTIVITY, billingPriceActivity);
					} else {
						mav.addObject(BILLING_PRICE_ACTIVITY, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
					}
					
					// if we have a project, we have a team member
					if(record.getProjectDetails() != null && record.getTeamMemberDetail() != null) {
						
						// TEAM MEMBER					
						TeamMemberDetail teamMemberDetail = BLTeamMemberDetail.getInstance().get(record.getTeamMemberDetail().getTeamMemberDetailId());
							
						// billing price
						if(record.isWorkHoursRecord()) {						
							// team member billing price
							if(teamMemberDetail.getBillingPrice() != null && record.getTime() != null) {
								if(record.getStartTime() != null && record.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getStartTime(), record.getEndTime(), record.getTime())) {
									billingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getBillingPrice(), teamMemberDetail.getBillingTimeUnit(), record.getStartTime(), record.getEndTime()).concat(" ").concat(teamMemberDetail.getBillingPriceCurrency().getInitials());
								} else {
									billingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getBillingPrice(), teamMemberDetail.getBillingTimeUnit(), record.getTime()).concat(" ").concat(teamMemberDetail.getBillingPriceCurrency().getInitials());
								}
							}
									
							// cost price
							if(teamMemberDetail.getCostPrice() != null && record.getTime() != null) {
								if(record.getStartTime() != null && record.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getStartTime(), record.getEndTime(), record.getTime())) {
									costPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getCostPrice(), teamMemberDetail.getCostTimeUnit(), record.getStartTime(), record.getEndTime()).concat(" ").concat(teamMemberDetail.getCostPriceCurrency().getInitials());
								} else {
									costPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getCostPrice(), teamMemberDetail.getCostTimeUnit(), record.getTime()).concat(" ").concat(teamMemberDetail.getCostPriceCurrency().getInitials());
								}
							}
						}
						
						if(record.isOvertimeRecord()) {		
							// team member overtime billing price
							if(teamMemberDetail.getOvertimeBillingPrice() != null && record.getOverTimeTime() != null) {
								if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime())) {
									overtimeBillingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getOvertimeBillingPrice(), teamMemberDetail.getOvertimeBillingTimeUnit(), record.getOverTimeStartTime(), record.getOverTimeEndTime()).concat(" ").concat(teamMemberDetail.getOvertimeBillingCurrency().getInitials());
								} else {
									overtimeBillingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getOvertimeBillingPrice(), teamMemberDetail.getOvertimeBillingTimeUnit(), record.getOverTimeTime()).concat(" ").concat(teamMemberDetail.getOvertimeBillingCurrency().getInitials());
								}
							}
						
							
							// overtime cost price
							if(teamMemberDetail.getOvertimeCostPrice() != null && record.getOverTimeTime() != null) {
								if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime())) {
									overtimeCostPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getOvertimeCostPrice(), teamMemberDetail.getOvertimeCostTimeUnit(), record.getOverTimeStartTime(), record.getOverTimeEndTime()).concat(" ").concat(teamMemberDetail.getOvertimeCostCurrency().getInitials());
								} else {
									overtimeCostPriceTeamMember = ControllerUtils.getInstance().calculatePrice(teamMemberDetail.getOvertimeCostPrice(), teamMemberDetail.getOvertimeCostTimeUnit(), record.getOverTimeTime()).concat(" ").concat(teamMemberDetail.getOvertimeCostCurrency().getInitials());
								}
							}
						}
							
																																
						
						// put on mav the team member's costs
						if (costPriceTeamMember != null) {
							mav.addObject(COST_PRICE_TEAM_MEMBER, costPriceTeamMember);
						}  else {
							mav.addObject(COST_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
						}
						if (billingPriceTeamMember != null) {
							mav.addObject(BILLING_PRICE_TEAM_MEMBER, billingPriceTeamMember);
						} else {
							mav.addObject(BILLING_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
						}
						if (overtimeCostPriceTeamMember != null) {
							mav.addObject(OVERTIME_COST_PRICE_TEAM_MEMBER, overtimeCostPriceTeamMember);
						} else {
							mav.addObject(OVERTIME_COST_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
						}
						if (overtimeBillingPriceTeamMember != null) {
							mav.addObject(OVERTIME_BILLING_PRICE_TEAM_MEMBER, overtimeBillingPriceTeamMember);	
						} else {
							mav.addObject(OVERTIME_BILLING_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
						}
																			
					} else if(record.getPersonDetailId() != null && record.getPersonDetailId() != -1) { // else, we have a person from organization
						
						// PERSON
						PersonDetail personDetail = BLPersonDetail.getInstance().getWithAll(record.getPersonDetailId());
						
						if(record.isWorkHoursRecord()) {
							// cost price
							if(personDetail.getCostPrice() != null && record.getTime() != null) {
								if(record.getStartTime() != null && record.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getStartTime(), record.getEndTime(), record.getTime())) {
									costPricePerson = ControllerUtils.getInstance().calculatePrice(personDetail.getCostPrice(), personDetail.getCostTimeUnit(), record.getStartTime(), record.getEndTime()).concat(" ").concat(personDetail.getCostPriceCurrency().getInitials());
								} else {
									costPricePerson = ControllerUtils.getInstance().calculatePrice(personDetail.getCostPrice(), personDetail.getCostTimeUnit(), record.getTime()).concat(" ").concat(personDetail.getCostPriceCurrency().getInitials());
								}
							}								
						}
						
						if(record.isOvertimeRecord()) {
							// overtime cost price
							if(personDetail.getOvertimeCostPrice() != null && record.getOverTimeTime() != null) {
								if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime())) {
									overtimeCostPricePerson = ControllerUtils.getInstance().calculatePrice(personDetail.getOvertimeCostPrice(), personDetail.getOvertimeCostTimeUnit(), record.getOverTimeStartTime(), record.getOverTimeEndTime()).concat(" ").concat(personDetail.getOvertimeCostCurrency().getInitials());
								} else {
									overtimeCostPricePerson = ControllerUtils.getInstance().calculatePrice(personDetail.getOvertimeCostPrice(), personDetail.getOvertimeCostTimeUnit(), record.getOverTimeTime()).concat(" ").concat(personDetail.getOvertimeCostCurrency().getInitials());
								}
							}											
						}	
						
						// put on mav the person's costs	
						if (costPricePerson != null) {
							mav.addObject(COST_PRICE_PERSON, costPricePerson);
						} else {
							mav.addObject(COST_PRICE_PERSON, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
						}
						if (overtimeCostPricePerson != null) {
							mav.addObject(OVERTIME_COST_PRICE_PERSON, overtimeCostPricePerson);	
						} else {
							mav.addObject(OVERTIME_COST_PRICE_PERSON, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));	
						}
					}
				}
			}
			
			// put on mav the record
			mav.addObject(RECORD, record);					
			
		} catch(ServletRequestBindingException e){
			logger.error("handleRequestInternal", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch(BusinessException be) {
			logger.error("handleRequestInternal", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//Publish messages/errors
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);	
		
		logger.debug("handleRequestInternal - END");
		
		return mav;
	}
	
	/**
	 * Checks if a specific person is the manager for a specific project
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param personId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	boolean isManager(Integer projectId, Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("isManager - START");
		
		boolean isManager = false;
		
		logger.debug("personId = ".concat(String.valueOf(personId)));
		logger.debug("projectId = ".concat(String.valueOf(projectId)));
		
		// get the project by projectId
		Project project = new Project();
		try{
		
			project = BLProject.getInstance().getSimpleProject(projectId);
		
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		if(personId == project.getManagerId()) {
			isManager = true;
		}
		
		logger.debug("isManager - END, with isManager = " + isManager);
		return isManager;		

	}

}
