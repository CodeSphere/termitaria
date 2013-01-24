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
package ro.cs.ts.web.controller.form;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCalendar;
import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLRecord;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.PersonDetail;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntObj;
import ro.cs.ts.om.Calendar;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class RecordFormController extends RootSimpleFormController {
	
	public static final String FORM_VIEW								= "RecordForm";
	public static final String SUCCESS_VIEW								= "RecordForm";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String ID 										= "recordId";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String GET_ERROR 								= "record.get.error";
	private static final String UPDATE_MESSAGE 							= "record.update.message";
	private static final String UPDATE_ERROR 							= "record.update.error";
	private static final String ADD_MESSAGE								= "record.add.message";
	private static final String ADD_ERROR								= "record.add.error";
	private static final String GENERAL_ERROR							= "record.general.form.error";
	private static final String GET_USER_PROJECTS_ERROR 				= "record.get.user.projects.error";
	private static final String GET_PROJECT_ERROR						= "project.get.error";
	private static final String GET_CALENDAR_ERROR						= "calendar.get.error";
	private static final String PRICE_NOT_DEFINED						= "record.price.not.defined";
	private static final String EXISTS_IDENTICAL_RECORD_TIME			= "record.exists.identical.time";
	private static final String EXISTS_IDENTICAL_RECORD_TIME_OVERTIME	= "record.exists.identical.time.overtime";
	private static final String EXISTS_IDENTICAL_RECORD_TTO				= "record.exists.identical.time.time.overtime";
	private static final String EXISTS_IDENTICAL_RECORD_OVERTIME		= "record.exists.identical.overtime";
	private static final String EXISTS_IDENTICAL_RECORD_OVERTIME_TIME	= "record.exists.identical.overtime.time";
	private static final String EXISTS_IDENTICAL_RECORD_OOT				= "record.exists.identical.overtime.overtime.time";
	private static final String EXISTS_IDENTICAL_HOURS					= "record.exists.identical.hours";		
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 								= "BACK_URL";
	private static final String NEXT_BACK_URL							= "NEXT_BACK_URL";
	private static final String ENCODE_BACK_URL	 						= "ENCODE_BACK_URL";
	
	//--------------------MODEL-----------------------------------------------------------------------
	private static final String USER_PROJECTS							= "USER_PROJECTS";
	private static final String USER_PROJECTS_IS_PM_AND_MEMBERS			= "USER_PROJECTS_IS_PM_AND_MEMBERS";
	private static final String IS_PM_FOR_AT_LEAST_ONE_PROJECT			= "IS_PM_FOR_AT_LEAST_ONE_PROJECT";
	private static final String PERSON_ID								= "PERSON_ID";
	private static final String IS_USER_ALL								= "IS_USER_ALL";
	private static final String SHOW_BILLABLE							= "SHOW_BILLABLE";
	private static final String TODAY_DATE								= "TODAY_DATE";
	private static final String USER_NAME					 			= "USER_NAME";
	private static final String IS_MANAGER								= "IS_MANAGER";
	private static final String ACTIVITY_BILLABLE						= "ACTIVITY_BILLABLE";
	
	private static final String COST_PRICE_ACTIVITY						= "COST_PRICE_ACTIVITY";
	private static final String BILLING_PRICE_ACTIVITY					= "BILLING_PRICE_ACTIVITY";
	private static final String COST_PRICE_TEAM_MEMBER					= "COST_PRICE_TEAM_MEMBER";
	private static final String BILLING_PRICE_TEAM_MEMBER				= "BILLING_PRICE_TEAM_MEMBER";
	private static final String OVERTIME_COST_PRICE_TEAM_MEMBER			= "OVERTIME_COST_PRICE_TEAM_MEMBER";
	private static final String OVERTIME_BILLING_PRICE_TEAM_MEMBER		= "OVERTIME_BILLING_PRICE_TEAM_MEMBER";
	private static final String COST_PRICE_PERSON						= "COST_PRICE_PERSON";
	private static final String OVERTIME_COST_PRICE_PERSON				= "OVERTIME_COST_PRICE_PERSON";
	private static final String ACTIVITY								= "ACTIVITY";
	private static final String SAME_ACTIVITY							= "SAME_ACTIVITY";
		
	public RecordFormController(){
		setCommandName("recordBean");
		setCommandClass(Record.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	
	/**
	 * Registering Custom Editors to this controller for 
	 * better binding request parameters.
	 * (More details on each registering)
	 * 
	 * @author coni
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));

		logger.debug("initBinder - END");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
			
		
		// used as a container for info/error messages
		ArrayList<String> errorMessages = new ArrayList<String>();		
		Record record = null;
		
		try {
			//check if i have to edit the record
			if (ServletRequestUtils.getIntParameter(request, ID) != null && 
					ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION) != null &&
					IConstant.CMD_GET.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION))){
				logger.debug("formBackingObject: GET");				
				//get the record with the specified recordId
				record = handleGet(ServletRequestUtils.getIntParameter(request, ID).intValue(), errorMessages, request);
				if (record == null){
					record = new Record();
				}
			} else if (ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION) != null &&
					IConstant.CMD_ADD.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION))){
				logger.debug("formBackingObject: ADD");
				record = handleGetNew(request, errorMessages);
			} else {
				record = new Record();
			}
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		if (IConstant.CMD_ADD.equals(ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION)) &&
				request.getParameter("projectId") != null &&
				request.getParameter("activityId") != null) {
			record.setActivityId(ServletRequestUtils.getIntParameter(request, "activityId").intValue());
			record.setProjectId(ServletRequestUtils.getIntParameter(request, "projectId").intValue());
		}
		
		logger.debug("formBackingObject - END");
		return record;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)	throws Exception {
		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		Record record = (Record) command;
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> warningMessages = new ArrayList<String>();
		
		//set the start date and end date for work hours and over time to null if not checked
		if (!record.isWorkHoursRecord()) {
			record.setStartTime(null);
			record.setEndTime(null);
			record.setBillable(new Character(' '));
			
		}
		
		if (!record.isOvertimeRecord()) {
			record.setOverTimeStartTime(null);
			record.setOverTimeEndTime(null);
			record.setOverTimeBillable(null);
		}
		
		// check if the record has a recordId
		if (record.getRecordId() != -1){
			// if I have recordId, it means that I have "update" action
			logger.debug("onSubmit - handleUpdate");
			mav =  handleUpdate(record, request, locale, errorMessages, infoMessages, warningMessages);
		} else {
			// if I don't have record, it means that I have "add" action
			logger.debug("onSubmit - handleAdd");
			mav =  handleAdd(record, request, locale, errorMessages, infoMessages, warningMessages);
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		setWarnings(request, warningMessages);
		
		mav.addAllObjects(referenceData(request, record, errors));
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * Gets the new record, for the add action
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	public Record handleGetNew(HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("handleGetNew - START");
		
		Record record = new Record();
		String billable = (String) request.getParameter("billable");
		//if the billable param doesn't have a value, the default 'Y' value must be replaced with ' ' 
		if (billable == null) {
			record.setBillable(new Character(' '));
		}
		try{			
			//setting the organizationId for the new record
			Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			if (organizationId != null){				
				record.setOrganizationId(organizationId);
				
				// get the work program form organization
				Calendar calendar = BLCalendar.getInstance().getCalendarByOrganization(organizationId);				
				
				String delimiter = ":";
				Date start = new Date();
				Date end = new Date();
											
				// start work hour and minutes
				String startWork = calendar.getStartWork();				
		    	String[] startTime = startWork.split(delimiter);
				
		    	String startWorkHour = ControllerUtils.getInstance().removeSpaces(startTime[0]);
				String startWorkMinutes = startTime[1];

				// end work hour and minutes					
				String todayDate = ControllerUtils.getInstance().getTodayHoursAndMinutes(new GregorianCalendar());							
				String[] endTime = todayDate.split(delimiter);
				
		    	String endWorkHour = endTime[0];
				String endWorkMinutes = endTime[1];
				
				Date date = new Date();
				
				// get the years, months, days, hours, minutes and seconds by dates
				SimpleDateFormat simpleYearformat=new SimpleDateFormat("yyyy");		
				Integer year = Integer.valueOf(simpleYearformat.format(date));    		    	        	  
		    	
		      	SimpleDateFormat simpleMonthformat=new SimpleDateFormat("MM");
		    	Integer month = Integer.valueOf(simpleMonthformat.format(date));		    	    	  
		       	          	
		    	SimpleDateFormat simpleDayformat=new SimpleDateFormat("dd");
		    	Integer day = Integer.valueOf(simpleDayformat.format(date)); 
		    	
		    	String startDate = year.toString().concat("/").concat(month.toString()).concat("/").concat(day.toString()).concat(" ").concat(startWorkHour).concat(":").concat(startWorkMinutes);
		    	String endDate = year.toString().concat("/").concat(month.toString()).concat("/").concat(day.toString()).concat(" ").concat(endWorkHour).concat(":").concat(endWorkMinutes);
		    	
		    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		    			   		    		
	    		try{
	        		start = sdf.parse(startDate);
	        		end = sdf.parse(endDate);
	        	} catch (ParseException e) {}
	        		        	
	        	if(Integer.parseInt(startWorkHour) < Integer.parseInt(endWorkHour) || ((Integer.parseInt(startWorkHour) == Integer.parseInt(endWorkHour)) && (Integer.parseInt(startWorkMinutes) <= Integer.parseInt(endWorkMinutes)))) {
	        		//set the start time and end time
		        	record.setStartTime(start);		        	
	        	} 		        			        	       
	        	record.setEndTime(end);
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_CALENDAR_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		logger.debug("handleGetNew - END");
		return record;
	}	
	
	/**
	 * Gets a Record with all its components
	 * 
	 * @author Coni
	 * @param recordId
	 * @param errorMessages
	 * @param request
	 * @return
	 */
	public Record handleGet(int recordId, ArrayList<String> errorMessages, HttpServletRequest request) {
		logger.debug("handleGet - START");
		Record record = null;
		try {
			record = BLRecord.getInstance().getAll(recordId);					
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (record != null) {
				if (record.getProjectDetails() != null) {
					record.setProjectId(record.getProjectDetails().getProjectId());
				}
				if (record.getTeamMemberDetail() != null) {
					record.setUserId(record.getTeamMemberDetail().getTeamMemberId());
					record.setTeamMemberDetailId(record.getTeamMemberDetail().getTeamMemberDetailId());
					TeamMember member = BLTeamMember.getInstance().getTeamMember(record.getTeamMemberDetail().getTeamMemberId(), false);
					record.setRecordOwnerName(member.getFirstName().concat(" ").concat(member.getLastName()));
				} else if (record.getPersonDetail() != null) {
					record.setUserId(record.getPersonDetail().getPersonId());
					record.setPersonDetailId(record.getPersonDetail().getPersonDetailId());
					if (record.getPersonDetail().getPersonId() == userAuth.getPersonId()) {
						record.setRecordOwnerName(userAuth.getFirstName().concat(" ").concat(userAuth.getLastName()));
					} else {
						String[] personIds = new String[1];
						personIds[0] = String.valueOf(record.getPersonDetail().getPersonId());
						List<UserSimple> users = BLUser.getInstance().getUsersByPersonId(personIds);
						record.setRecordOwnerName(users.get(0).getFirstName().concat(" ").concat(users.get(0).getLastName()));
					}
					
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
				
				if (record.getActivity() != null) {
					record.setActivityId(record.getActivity().getActivityId());					
					request.setAttribute(ACTIVITY_BILLABLE, record.getActivity().getBillable());					
				}
			}			
		} catch (BusinessException be) {
			logger.error("handleGet", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		logger.debug("handleGet - START");
		return record;
	}
	
	/**
	 * Updates the record with the specified id
	 * 
	 * @author Coni
	 * @param record
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleUpdate(Record record, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, ArrayList<String> warningMessages){
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getFormView());
		try {
			
			// the organization id from the session
			Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//add the command object
			mav.addObject(getCommandName(), record);
			
			// if the record is added for an activity that belongs directly to the organization
			// the record is not billable
			if(record.getProjectId() == -1) {
				record.setBillable(IConstant.NOM_BILLABLE_NO);
			}			
			logger.debug("record = " + record);	
									
			// handles all the dependencies for the record
			BLRecord.getInstance().handleAddDepedencies(record);			
			
			Record recordIdentical = null;
			
			// get the team member detail
			Integer teamMemberDetailId = null;
			Integer personDetailId = null;
			if (record.getProjectId().equals(IConstant.NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION)) {
				logger.debug("record.getUserId = " + record.getUserId());
				PersonDetail personDetail = BLPersonDetail.getInstance().getByPersonId(record.getUserId());
				logger.debug("personDetail = " + personDetail);
				personDetailId = personDetail.getPersonDetailId();
			} else {
				TeamMemberDetail teamMemberDetail = BLTeamMemberDetail.getInstance().getByTeamMemberId(record.getUserId());	
				teamMemberDetailId = teamMemberDetail.getTeamMemberDetailId();				
			}
			
			if((record.getStartTime() != null && record.getEndTime() != null) || (record.getOverTimeStartTime()!= null && record.getOverTimeEndTime() != null)) {
				recordIdentical = BLRecord.getInstance().hasIdenticalRecordForPerson(record.getRecordId(), teamMemberDetailId, record.getActivityId(), record.getStartTime(), record.getEndTime(), record.getTime(), record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime(), personDetailId);
				logger.debug("record identical = " + recordIdentical);			
			}
			
			String ownerName = null;
			if(record.getRecordOwnerName() == "") {
				ownerName = userAuth.getFirstName().concat(" ").concat(userAuth.getLastName());
			} else {
				ownerName = record.getRecordOwnerName();
			}
			
			// if exists a record that has the same interval or an interleaved interval, give the user a message
			// to select another activity for the same record
			if(recordIdentical != null) {		
				if(recordIdentical.getRecordId() > 0) {			
					logger.debug("record identical = " + recordIdentical.getRecordId());	
										
					boolean time = false;			
					boolean overtime = false;
					
					// if we have work hours
					if(recordIdentical.getStartTime() != null && recordIdentical.getEndTime() != null) {
						if(record.getStartTime() != null && record.getEndTime() != null) {
							if(((record.getStartTime().after(recordIdentical.getStartTime()) || record.getStartTime().equals(recordIdentical.getStartTime())) && (record.getEndTime().before(recordIdentical.getEndTime()) || record.getEndTime().equals(recordIdentical.getEndTime()))) || 
				    				(record.getStartTime().before(recordIdentical.getStartTime()) && record.getEndTime().after(recordIdentical.getStartTime())) ||
				    				(record.getStartTime().before(recordIdentical.getEndTime()) && record.getEndTime().after(recordIdentical.getEndTime())) || 
				    				((record.getStartTime().before(recordIdentical.getStartTime()) || record.getStartTime().equals(recordIdentical.getStartTime())) && (record.getEndTime().after(recordIdentical.getEndTime()) || record.getEndTime().equals(recordIdentical.getEndTime())))) {
								if(ControllerUtils.getInstance().hasOverlap(record.getStartTime(), record.getEndTime(), record.getTime(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getTime())) {
									time = true;									
								}
							}
						}
						if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null) {
							if(((record.getOverTimeStartTime().after(recordIdentical.getStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getStartTime())) && (record.getOverTimeEndTime().before(recordIdentical.getEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getEndTime()))) || 
				    				(record.getOverTimeStartTime().before(recordIdentical.getStartTime()) && record.getOverTimeEndTime().after(recordIdentical.getStartTime())) ||
				    				(record.getOverTimeStartTime().before(recordIdentical.getEndTime()) && record.getOverTimeEndTime().after(recordIdentical.getEndTime())) || 
				    				((record.getOverTimeStartTime().before(recordIdentical.getStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getStartTime())) && (record.getOverTimeEndTime().after(recordIdentical.getEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getEndTime())))) {								
								if(ControllerUtils.getInstance().hasOverlap(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getTime())) {
									overtime = true;									
								}
							}
						}
						logger.debug("time = " + time + ", overtime = " + overtime);
						if(overtime && time) {
							warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_TTO, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
						} else if(overtime) {
							warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_TIME_OVERTIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
						} else if(time){
							warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_TIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
						}
					} else {  				
						
						// if we have overtime hours
						if(recordIdentical.getOverTimeStartTime() != null && recordIdentical.getOverTimeEndTime() != null) {						
							if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null) {
								if(((record.getOverTimeStartTime().after(recordIdentical.getOverTimeStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getOverTimeEndTime().before(recordIdentical.getOverTimeEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getOverTimeEndTime()))) || 
					    				(record.getOverTimeStartTime().before(recordIdentical.getOverTimeStartTime()) && record.getOverTimeEndTime().after(recordIdentical.getOverTimeStartTime())) ||
					    				(record.getOverTimeStartTime().before(recordIdentical.getOverTimeEndTime()) && record.getOverTimeEndTime().after(recordIdentical.getOverTimeEndTime())) || 
					    				((record.getOverTimeStartTime().before(recordIdentical.getOverTimeStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getOverTimeEndTime().after(recordIdentical.getOverTimeEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getOverTimeEndTime())))) {								
									if(ControllerUtils.getInstance().hasOverlap(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getOverTimeTime())) {
										overtime = true;									
									}
								}
							} 
							if(record.getStartTime() != null && record.getEndTime() != null) {
								if(((record.getStartTime().after(recordIdentical.getOverTimeStartTime()) || record.getStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getEndTime().before(recordIdentical.getOverTimeEndTime()) || record.getEndTime().equals(recordIdentical.getOverTimeEndTime()))) || 
						    				(record.getStartTime().before(recordIdentical.getOverTimeStartTime()) && record.getEndTime().after(recordIdentical.getOverTimeStartTime())) ||
						    				(record.getStartTime().before(recordIdentical.getOverTimeEndTime()) && record.getEndTime().after(recordIdentical.getOverTimeEndTime())) || 
						    				((record.getStartTime().before(recordIdentical.getOverTimeStartTime()) || record.getStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getEndTime().after(recordIdentical.getOverTimeEndTime()) || record.getEndTime().equals(recordIdentical.getOverTimeEndTime())))) {									
									if(ControllerUtils.getInstance().hasOverlap(record.getStartTime(), record.getEndTime(), record.getTime(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getOverTimeTime())) {	
										time = true;									
									}
								}
							}
							logger.debug("time = " + time + ", overtime = " + overtime);
							
							if(overtime && time) {
								warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_OOT, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
							} else if(overtime) {
								warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_OVERTIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
							} else if(time) {
								warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_OVERTIME_TIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
							}
						}
					}						
					
				} else {
					warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_HOURS, new Object[] {null}, RequestContextUtils.getLocale(request)));
				}
			} else {
				//update the record
				BLRecord.getInstance().update(record);
				
				String projectName = null;
				String message = null;
				
				if(record.getProjectId() != -1) {
					Project project = BLProject.getInstance().get(record.getProjectId(), true);
					if(project != null) {
						projectName = project.getName();
					}
					message = IConstant.NOTIFICATION_MESSAGE_RECORD_PROJECT_UPDATE;
				} else {
					projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
					message = IConstant.NOTIFICATION_MESSAGE_RECORD_UPDATE;
				}
				
				// send notification regarding the update of a record
				sendNotificationRecord(record.getProjectId(),record.getProjectDetailId(),organizationId,
						message, new Object[]{record.getRecordId(), BLActivity.getInstance().get(record.getActivityId()).getName(), projectName, ownerName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
						IConstant.NOTIFICATION_SUBJECT_RECORD_UPDATE,new Object[]{null},IConstant.NOTIFICATION_SETTING_RECORD_UPDATE);
				//sendNotificationRecord(record.getProjectId(), organizationId, messageSource.getMessage(message, new Object[]{record.getRecordId(), BLActivity.getInstance().get(record.getActivityId()).getName(), projectName, ownerName}, new Locale("ro")), messageSource.getMessage(IConstant.NOTIFICATION_SUBJECT_RECORD_UPDATE, new Object[]{null}, new Locale("ro")));
											
				infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, new Object[] {null}, locale));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (record.getProjectId().equals(IConstant.NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION)) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_RECORD_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_ORG_UPDATE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_ORG_UPDATE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_RECORD_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), projectName, BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_PROJECT_UPDATE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), projectName, BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			}
						
		} catch (BusinessException be) {
			logger.error("Exception while updating record with id: ".concat(String.valueOf(record.getRecordId())), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e) {
			logger.error("Exception while updating record with id: ".concat(String.valueOf(record.getRecordId())), e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} 
		
		logger.debug("handleUpdate - END");
		return mav;
	}
	
	/**
	 * Adds a new record
	 * 
	 * @author Coni
	 * @param record
	 * @param request
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleAdd(Record record, HttpServletRequest request, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, ArrayList<String> warningMessages){
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getFormView());
		try {
			
			// the organization id from the session
			Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
			
			// the user that logs in
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//setting the new record's status to enabled
			record.setStatus(IConstant.NOM_RECORD_STATUS_ACTIVE);
							
			// if the record is added for an activity that belongs directly to the organization
			// the record is not billable
			if(record.getProjectId() == -1) {
				record.setBillable(IConstant.NOM_BILLABLE_NO);
			}
			
			logger.debug("record = " + record);
			
			// handles all the dependencies for the record
			BLRecord.getInstance().handleAddDepedencies(record);
			
			// get the records user Id
			Integer userId = record.getUserId();
			
			// get the team member detail
			Integer teamMemberDetailId = null;
			Integer personDetailId = null;
			if (record.getProjectId().equals(IConstant.NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION)) {
				PersonDetail personDetail = BLPersonDetail.getInstance().getByPersonId(userId);
				personDetailId = personDetail.getPersonDetailId();
			} else {
				TeamMemberDetail teamMemberDetail = BLTeamMemberDetail.getInstance().getByTeamMemberId(userId);	
				teamMemberDetailId = teamMemberDetail.getTeamMemberDetailId();				
			}

			Record recordIdentical = null;
			if((record.getStartTime() != null && record.getEndTime() != null) || (record.getOverTimeStartTime()!= null && record.getOverTimeEndTime() != null)) {
				recordIdentical = BLRecord.getInstance().hasIdenticalRecordForPerson(record.getRecordId(), teamMemberDetailId, record.getActivityId(), record.getStartTime(), record.getEndTime(), record.getTime(), record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime(), personDetailId);
			}
			logger.debug("record identical " + recordIdentical);
			
			String ownerName = null;
			if(record.getRecordOwnerName() == "") {
				ownerName = userAuth.getFirstName().concat(" ").concat(userAuth.getLastName());
			} else {
				ownerName = record.getRecordOwnerName();
			}			
			
			// if exists a record that has the same interval or an interleaved interval, give the user a message
			// to select another activity for the same record
			if(recordIdentical != null) {
				request.setAttribute(SAME_ACTIVITY, true);		
				if(recordIdentical.getRecordId() > 0) {								
					logger.debug("record identical = " + recordIdentical.getRecordId());	
														
					boolean time = false;			
					boolean overtime = false;
					
					// if we have work hours
					if(recordIdentical.getStartTime() != null && recordIdentical.getEndTime() != null) {
						if(record.getStartTime() != null && record.getEndTime() != null) {
							if(((record.getStartTime().after(recordIdentical.getStartTime()) || record.getStartTime().equals(recordIdentical.getStartTime())) && (record.getEndTime().before(recordIdentical.getEndTime()) || record.getEndTime().equals(recordIdentical.getEndTime()))) || 
				    				(record.getStartTime().before(recordIdentical.getStartTime()) && record.getEndTime().after(recordIdentical.getStartTime())) ||
				    				(record.getStartTime().before(recordIdentical.getEndTime()) && record.getEndTime().after(recordIdentical.getEndTime())) || 
				    				((record.getStartTime().before(recordIdentical.getStartTime()) || record.getStartTime().equals(recordIdentical.getStartTime())) && (record.getEndTime().after(recordIdentical.getEndTime()) || record.getEndTime().equals(recordIdentical.getEndTime())))) {
								if(ControllerUtils.getInstance().hasOverlap(record.getStartTime(), record.getEndTime(), record.getTime(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getTime())) {
									time = true;									
								}
							}
						}
						if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null) {
							if(((record.getOverTimeStartTime().after(recordIdentical.getStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getStartTime())) && (record.getOverTimeEndTime().before(recordIdentical.getEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getEndTime()))) || 
				    				(record.getOverTimeStartTime().before(recordIdentical.getStartTime()) && record.getOverTimeEndTime().after(recordIdentical.getStartTime())) ||
				    				(record.getOverTimeStartTime().before(recordIdentical.getEndTime()) && record.getOverTimeEndTime().after(recordIdentical.getEndTime())) || 
				    				((record.getOverTimeStartTime().before(recordIdentical.getStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getStartTime())) && (record.getOverTimeEndTime().after(recordIdentical.getEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getEndTime())))) {								
								if(ControllerUtils.getInstance().hasOverlap(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getTime())) {
									overtime = true;									
								}
							}
						}
						logger.debug("time = " + time + ", overtime = " + overtime);
						if(overtime && time) {
							warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_TTO, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
						} else if(overtime) {
							warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_TIME_OVERTIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
						} else if(time){
							warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_TIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getStartTime(), recordIdentical.getEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
						}
					} else {  				
						
						// if we have overtime hours
						if(recordIdentical.getOverTimeStartTime() != null && recordIdentical.getOverTimeEndTime() != null) {						
							if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null) {
								if(((record.getOverTimeStartTime().after(recordIdentical.getOverTimeStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getOverTimeEndTime().before(recordIdentical.getOverTimeEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getOverTimeEndTime()))) || 
					    				(record.getOverTimeStartTime().before(recordIdentical.getOverTimeStartTime()) && record.getOverTimeEndTime().after(recordIdentical.getOverTimeStartTime())) ||
					    				(record.getOverTimeStartTime().before(recordIdentical.getOverTimeEndTime()) && record.getOverTimeEndTime().after(recordIdentical.getOverTimeEndTime())) || 
					    				((record.getOverTimeStartTime().before(recordIdentical.getOverTimeStartTime()) || record.getOverTimeStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getOverTimeEndTime().after(recordIdentical.getOverTimeEndTime()) || record.getOverTimeEndTime().equals(recordIdentical.getOverTimeEndTime())))) {								
									if(ControllerUtils.getInstance().hasOverlap(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getOverTimeTime())) {
										overtime = true;									
									}
								}
							} 
							if(record.getStartTime() != null && record.getEndTime() != null) {
								if(((record.getStartTime().after(recordIdentical.getOverTimeStartTime()) || record.getStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getEndTime().before(recordIdentical.getOverTimeEndTime()) || record.getEndTime().equals(recordIdentical.getOverTimeEndTime()))) || 
						    				(record.getStartTime().before(recordIdentical.getOverTimeStartTime()) && record.getEndTime().after(recordIdentical.getOverTimeStartTime())) ||
						    				(record.getStartTime().before(recordIdentical.getOverTimeEndTime()) && record.getEndTime().after(recordIdentical.getOverTimeEndTime())) || 
						    				((record.getStartTime().before(recordIdentical.getOverTimeStartTime()) || record.getStartTime().equals(recordIdentical.getOverTimeStartTime())) && (record.getEndTime().after(recordIdentical.getOverTimeEndTime()) || record.getEndTime().equals(recordIdentical.getOverTimeEndTime())))) {									
									if(ControllerUtils.getInstance().hasOverlap(record.getStartTime(), record.getEndTime(), record.getTime(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getOverTimeTime())) {	
										time = true;									
									}
								}
							}
							logger.debug("time = " + time + ", overtime = " + overtime);
							
							if(overtime && time) {
								warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_OOT, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
							} else if(overtime) {
								warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_OVERTIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
							} else if(time) {
								warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_RECORD_OVERTIME_TIME, new Object[] {ownerName, BLActivity.getInstance().get(record.getActivityId()).getName(), recordIdentical.getOverTimeStartTime(), recordIdentical.getOverTimeEndTime(), recordIdentical.getRecordId()}, RequestContextUtils.getLocale(request)));
							}
						}
					}						
										
				} else {					
					warningMessages.add(messageSource.getMessage(EXISTS_IDENTICAL_HOURS, new Object[] {null}, RequestContextUtils.getLocale(request)));
				}
		
			} else {
				//add the record
				record = BLRecord.getInstance().add(record);
				
				String projectName = null;
				String message = null;
				
				if(record.getProjectId() != -1) {
					Project project = BLProject.getInstance().get(record.getProjectId(), true);
					if(project != null) {
						projectName = project.getName();
					}
					message = IConstant.NOTIFICATION_MESSAGE_RECORD_PROJECT_ADD;
				} else {
					projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
					message = IConstant.NOTIFICATION_MESSAGE_RECORD_ADD;
				}
				
				// send notification regarding the add of a record
				sendNotificationRecord(record.getProjectId(),record.getProjectDetailId(),organizationId, 
						message, new Object[]{record.getRecordId(), BLActivity.getInstance().get(record.getActivityId()).getName(), projectName, ownerName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())}, 
						IConstant.NOTIFICATION_SUBJECT_RECORD_ADD,new Object[]{null},IConstant.NOTIFICATION_SETTING_RECORD_ADD);
				
				//sendNotificationRecord(record.getProjectId(), organizationId, messageSource.getMessage(message, new Object[]{record.getRecordId(), BLActivity.getInstance().get(record.getActivityId()).getName(), projectName, ownerName}, new Locale("ro")), messageSource.getMessage(IConstant.NOTIFICATION_SUBJECT_RECORD_ADD, new Object[]{null}, new Locale("ro")));
								
				infoMessages.add(messageSource.getMessage(ADD_MESSAGE, new Object[] {null}, locale));
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (record.getProjectId().equals(IConstant.NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION)) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_RECORD_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_ORG_ADD_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_ORG_ADD_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_RECORD_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_PROJECT_ADD_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), projectName, BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_PROJECT_ADD_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), record.getRecordOwnerName(), projectName, BLActivity.getInstance().get(record.getActivityId()).getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			}
						
		} catch (BusinessException be) {
			logger.error("Exception while adding record" , be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("Exception while adding record" , e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		//add the command object
		mav.addObject(getCommandName(), record);
		logger.debug("handleAdd - END");
		return mav;
	}
	
	/**
	 * Adds to model required nomenclators
	 * @author Coni
	 */
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception{
		logger.debug("referenceData - start");
		
		Map model = new HashMap();

		//used as a container for error messages
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		Locale locale = RequestContextUtils.getLocale(request);
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// the command class
		Record record = (Record) command;		
		logger.debug("record = " + record);	
		
		boolean isManager = false;
		
		// if we have a record (only, for edit action)
		if(record.getRecordId() != -1) {	
			
			// if we have a project, checks if the user is the manager for the project
			if(record.getProjectDetails() != null) {			
				isManager = isManager(record.getProjectDetails().getProjectId(), userAuth.getPersonId(), request, errorMessages);
			}
			
			// put on mav if the user that logs in is the manager of a specific project
			model.put(IS_MANAGER, isManager);
			
			// if the user has the permission or if he/she is the manager for the project in question
			if (userAuth.hasAuthority(PermissionConstant.getTheInstance().getTS_RecordViewCosts()) || isManager) {							
				
				String costPriceActivity = null;
				String billingPriceActivity = null;
				String costPriceTeamMember = null;
				String billingPriceTeamMember = null;
				String overtimeCostPriceTeamMember = null;
				String overtimeBillingPriceTeamMember = null;
				String costPricePerson = null;
				String overtimeCostPricePerson = null;		
								
																									
				// ACTIVITY
				Activity activity = BLActivity.getInstance().getWithProjectDetail(record.getActivityId());				
				logger.debug("activity =  " + activity);
				model.put(ACTIVITY, activity);
				
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
					model.put(COST_PRICE_ACTIVITY, costPriceActivity);		
				} else {
					model.put(COST_PRICE_ACTIVITY, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
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
					model.put(BILLING_PRICE_ACTIVITY, billingPriceActivity);
				} else {
					model.put(BILLING_PRICE_ACTIVITY, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
				}
				
				// if we have a project, we have a team member
				if(record.getProjectDetails() != null && record.getTeamMemberDetail() != null) {
					
					// TEAM MEMBER					
					TeamMemberDetail teamMemberDetail = BLTeamMemberDetail.getInstance().get(record.getTeamMemberDetailId());
						
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
						model.put(COST_PRICE_TEAM_MEMBER, costPriceTeamMember);
					}  else {
						model.put(COST_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
					}
					if (billingPriceTeamMember != null) {
						model.put(BILLING_PRICE_TEAM_MEMBER, billingPriceTeamMember);
					} else {
						model.put(BILLING_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
					}
					if (overtimeCostPriceTeamMember != null) {
						model.put(OVERTIME_COST_PRICE_TEAM_MEMBER, overtimeCostPriceTeamMember);
					} else {
						model.put(OVERTIME_COST_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
					}
					if (overtimeBillingPriceTeamMember != null) {
						model.put(OVERTIME_BILLING_PRICE_TEAM_MEMBER, overtimeBillingPriceTeamMember);	
					} else {
						model.put(OVERTIME_BILLING_PRICE_TEAM_MEMBER, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
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
						model.put(COST_PRICE_PERSON, costPricePerson);
					} else {
						model.put(COST_PRICE_PERSON, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));
					}
					if (overtimeCostPricePerson != null) {
						model.put(OVERTIME_COST_PRICE_PERSON, overtimeCostPricePerson);	
					} else {
						model.put(OVERTIME_COST_PRICE_PERSON, messageSource.getMessage(PRICE_NOT_DEFINED, null, locale));	
					}
				}																		
			}
		}
						
		try {
			// adding to model the action from the request
			String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
			model.put(IConstant.REQ_ACTION, action);
			
			//put the back url
			String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);
			
			String servletPath = request.getServletPath();
			String nextBackUrl = URLEncoder.encode(servletPath.substring(1, servletPath.length()).concat("?").concat(request.getQueryString()), "UTF-8");
			
			logger.debug("BACK_URL = " + backUrl);
			logger.debug("NEXT_BACK_URL = " + nextBackUrl);		
			
			model.put(BACK_URL, backUrl);		
			model.put(NEXT_BACK_URL, nextBackUrl);
			if (backUrl != null) {
				model.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));	
			}
				
			//adding to model the billable nomenclator
			model.put(IConstant.NOM_BILLABLE, TSContext.getFromContext(IConstant.NOM_BILLABLE));
			
			//adding the personId
			model.put(PERSON_ID, userAuth.getPersonId());
			
			//is USER_ALL
			if (userAuth.hasAuthority(PermissionConstant.getTheInstance().getTS_RecordAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordUpdateAll())) {
				model.put(IS_USER_ALL, true);
			} else {
				model.put(IS_USER_ALL, false);
			}
			
			//add the current date
			model.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
		} catch (Exception e) {
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		try {
			//adding the user's available project for recording a time sheet
			//if the user has the USER_ALL role (has the permission to add or update records for all the users and projects)
			//, all its organization's projects will be available
			List<Project> projects = null;
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordUpdateAll())) {
				projects = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), false);
			}
			
			//get the the projects where the user is a team member and the associated team members
			HashMap<Project,TeamMember> projectsAndMembers = BLProject.getInstance().getProjectsAndTeamMembersByPerson(userAuth.getPersonId(), true, false);
			List<Project> userProjects = null;
			if (projectsAndMembers != null && !projectsAndMembers.isEmpty()) {
				userProjects = new ArrayList<Project>(projectsAndMembers.keySet());
			}
			
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordAddAll()) && !userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordUpdateAll())) {
				if (userProjects != null) {
					//if the user is not USER_ALL, the available projects for recording a time sheet are the projects where the user is a team member
					projects = userProjects;
				}
			}

			//needed to display the billable section for edit record action, when the user is the project PM
			//or USER_ALL and the record is for a project (not per organization)
			boolean showBillable = false;
			boolean isManagerForAtLeastOneProject = false;
			
			List<IntObj> projectsIsPm = new ArrayList<IntObj>();
			
			if (projects != null) {
				for (Project project : projects) {										
					IntObj obj = new IntObj();
					obj.setValue(project.getProjectId());
					if (project.getManagerId().equals(userAuth.getPersonId())) {
						obj.setLabel(true);
						isManagerForAtLeastOneProject = true;
					} else {
						obj.setLabel(false);
					}
					projectsIsPm.add(obj);
				}
				
				//if the action is to edit and the record has a projectId > -1 (not per organization record)
				if (record.getProjectId() != null && !record.getProjectId().equals(-1)) {
					//if the user has the USER_ALL role, i must display the billable section, 
					//otherwise only if he is the selected project PM
					if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordUpdateAll())) {
						showBillable = true;
					} else {
						for (IntObj obj : projectsIsPm) {
							if (record.getProjectId().equals(obj.getValue()) && ((Boolean) obj.getLabel()).equals(true)) {
								showBillable = true;
							}
						}
					}
				}
			}
			model.put(USER_PROJECTS, projects);
			model.put(SHOW_BILLABLE, showBillable);
			
			//add additional info about projects
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObj = new JSONObject();
			for (IntObj obj : projectsIsPm) {
				jsonObj.accumulate("projectId", obj.getValue());
				jsonObj.accumulate("isPm", (Boolean) obj.getLabel());
				//set the member id
				if (projectsAndMembers != null) {
					Iterator it = projectsAndMembers.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();
						if (entry.getKey().getProjectId() == obj.getValue()) {
							jsonObj.accumulate("memberId", entry.getValue().getMemberId());
						}
					}
				}
				jsonArray.add(jsonObj);
				jsonObj.clear();
			}
			
			model.put(IS_PM_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeastOneProject);			
			model.put(USER_PROJECTS_IS_PM_AND_MEMBERS, jsonArray.toString());
			model.put(USER_NAME, userAuth.getFirstName().concat(" ").concat(userAuth.getLastName()));
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(GET_USER_PROJECTS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - end");
		return model;
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
		
		// get the project by projectId
		Project project = new Project();
		try{
			logger.debug("projectId = " + projectId);
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
	
	/**
	 * Send the notification when the record is added or updated
	 * 
	 * @author Adelina
	 * 
	 * @param action
	 * @param projectId
	 * @param organizationId
	 */
	public void sendNotificationRecord(Integer projectId,Integer projectDetailId, Integer organizationId, String messageRecordKey,Object[] messageRecordObjects , String subjectRecordKey, Object[] subjectRecordObjects, Byte setting ) {
		logger.debug("sendNotificationRecord - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
		
		try{						
			if(projectId != null && projectId != -1) {
				// get the project identified by it's projectId
				Project project = BLProject.getInstance().getSimpleProject(projectId);				
				logger.debug("project = " + project);
				Integer managerId = project.getManagerId();
				logger.debug("managerId = " + managerId);
								
				//1. I have to send a notification to the manager of the project					
				userIds.add(String.valueOf(managerId));	
				userIdsMap.put(String.valueOf(managerId), true);
			} 				
			// 2. I have to send a notification to the users, that have the permission TS_NotificationReceive
			Set<UserSimple> users = OMWebServiceClient.getInstance().getPersonsFromRole(PermissionConstant.getTheInstance().getTS_NotificationReceive(), organizationId);
			logger.debug("users = " + users);
			if(users != null && users.size() > 0) {							
				for(UserSimple user : users) {
					if (userIds.add(String.valueOf(user.getUserId()))){
						userIdsMap.put(String.valueOf(user.getUserId()), false);
					}
				}
			}			
			
			
			List<String> list = new ArrayList<String>(userIds);					
			String[] notificationIds = list.toArray(new String[0]);
			logger.debug("notificationIds = " + notificationIds.length);
			
			for(int i = 0; i < notificationIds.length; i++) {
				logger.debug("notificationIds[" + i + "] = " + notificationIds[i]);
			}
						 					
																
			// send the notification										
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageRecordKey, messageRecordObjects, subjectRecordKey, subjectRecordObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationRecord - END");
	}

}
