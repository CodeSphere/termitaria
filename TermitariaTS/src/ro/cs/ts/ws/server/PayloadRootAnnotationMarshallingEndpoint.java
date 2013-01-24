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
package ro.cs.ts.ws.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLProjectDetails;
import ro.cs.ts.business.BLRecord;
import ro.cs.ts.business.BLRecordSession;
import ro.cs.ts.business.BLReportsDataSource;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.business.BLWidgetSession;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.RecordSession;
import ro.cs.ts.entity.TeamMemberDetail;
import ro.cs.ts.entity.WidgetSession;
import ro.cs.ts.exception.EndpointException;
import ro.cs.ts.exception.ICodeException;
import ro.cs.ts.utils.TimeInterval;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.ws.server.entity.GetActivitiesByProjectIdRequest;
import ro.cs.ts.ws.server.entity.GetActivitiesByProjectIdResponse;
import ro.cs.ts.ws.server.entity.GetProjectIdForAbortRequest;
import ro.cs.ts.ws.server.entity.GetProjectIdForDeleteRequest;
import ro.cs.ts.ws.server.entity.GetProjectIdForFinishRequest;
import ro.cs.ts.ws.server.entity.GetProjectIdForOpenRequest;
import ro.cs.ts.ws.server.entity.GetRecordDetailsBySessionIdRequest;
import ro.cs.ts.ws.server.entity.GetRecordDetailsBySessionIdResponse;
import ro.cs.ts.ws.server.entity.GetTeamMemberIdForDeleteRequest;
import ro.cs.ts.ws.server.entity.ObjectFactory;
import ro.cs.ts.ws.server.entity.SendStopTimeSheetRequest;
import ro.cs.ts.ws.server.entity.SendStopTimeSheetResponse;
import ro.cs.ts.ws.server.entity.TSReportGetDataCriteria;
import ro.cs.ts.ws.server.entity.TimeSheetReportDatasourceRequest;
import ro.cs.ts.ws.server.entity.TimeSheetReportDatasourceResponse;
import ro.cs.ts.ws.server.entity.WSActivity;
import ro.cs.ts.ws.server.entity.WSRecords;

@Endpoint
public class PayloadRootAnnotationMarshallingEndpoint extends GenericEndpoint{
    private ObjectFactory objectFactory;
   
	public PayloadRootAnnotationMarshallingEndpoint(ObjectFactory objectFactory){
		this.objectFactory = objectFactory;
	}
	
	
	
	/**
	 * @author Andreea
	 * Endpoint method for activities requests
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "GetActivitiesByProjectIdRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public GetActivitiesByProjectIdResponse getActivitiesByProjectId(GetActivitiesByProjectIdRequest request) throws EndpointException {
		logger.debug("getActivitiesByProjectId START");
		GetActivitiesByProjectIdResponse response = new GetActivitiesByProjectIdResponse();
		try {
			//get the criteria from the request, used to retrieve activities
			int getProjectId = request.getProjectId();
			int projectDetailId = BLProjectDetails.getInstance().getByProjectId(getProjectId).getProjectDetailId();
			//get the activities
			List<Activity> activities = BLActivity.getInstance().getByProjectDetailId(projectDetailId);
			
			List<WSActivity> wsActivities = new ArrayList<WSActivity>();
			for (int i = 0; i < activities.size(); i++) {
				WSActivity wsActivity = new WSActivity();
				wsActivity.setActivityId(activities.get(i).getActivityId());
				wsActivity.setName(activities.get(i).getName());
				wsActivity.setProjectDetailId(activities.get(i).getProjectDetailId());
				wsActivity.setBillable(activities.get(i).getBillable());
				wsActivity.setOrganizationId(activities.get(i).getOrganizationId());
				wsActivity.setStatus(activities.get(i).getStatus());
				wsActivities.add(wsActivity);
			}
				
			//create the response
			response.setActivities(wsActivities);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_ACTIVITIES, e);
		}
		logger.debug("getActivitiesByProjectId END");
		return response;
	}
	
	
	/**
	 * @author Andreea
	 * Endpoint method for record details requests
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "GetRecordDetailsBySessionIdRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public GetRecordDetailsBySessionIdResponse getRecordIdBySessionId(GetRecordDetailsBySessionIdRequest request) throws EndpointException {
		logger.debug("GetRecordDetailsBySessionId START");
		GetRecordDetailsBySessionIdResponse response = new GetRecordDetailsBySessionIdResponse();
		try {
			//get the criteria from the request, used to retrieve widgetSessions 
			int userId = request.getUserId();

			//get the widgetSessions 
			List<WidgetSession> widgetSessions = BLWidgetSession.getInstance().getByUserId(userId);
			String sessionId = null;

		
			for (int i = 0; i < widgetSessions.size(); i++) 
				if (widgetSessions.get(i).getSessionId().equals(request.getSessionId()))
					sessionId = widgetSessions.get(i).getSessionId();
			
			WidgetSession wSession = new WidgetSession();
			
			try {
				if (sessionId != null) {
					
					wSession = BLWidgetSession.getInstance().getBySessionId(sessionId);
					int recordId = BLRecordSession.getInstance().getBySessionId(sessionId).getRecordId();
					Date expireDate = wSession.getExpireDate();
					
					Calendar date = Calendar.getInstance();
					Date currentDate = date.getTime();
					
					if (ControllerUtils.getInstance().calculateLowerLimit(currentDate, expireDate).equals(currentDate)) {
						response.setRecordId(recordId);
						response.setValidSession(true);
						response.setStartTime(wSession.getCreationDate());
						
						int activityId = BLRecord.getInstance().get(recordId).getActivityId();
						int projectId = BLRecord.getInstance().get(recordId).getProjectDetailId();
						
						response.setActivityId(activityId);
						response.setProjectId(projectId);
					}
					
					else {
						response.setValidSession(false);
					}
	
				}
				else {
	
					int teamMemberId = request.getTeamMemberId();
					TeamMemberDetail teamMemberDetail = new TeamMemberDetail();
					teamMemberDetail.setTeamMemberId(teamMemberId);
					BLTeamMemberDetail.getInstance().add(teamMemberDetail);
					
					int teamMemberDetailId = BLTeamMemberDetail.getInstance().getByTeamMemberId(teamMemberId).getTeamMemberDetailId();
					int projectId = request.getProjectId();
					int projectDetailId = BLProjectDetails.getInstance().getByProjectId(projectId).getProjectDetailId();
					int activityId = request.getActivityId();
					Date startTime = request.getStartTime();
					Character billable = request.getBillable();
					byte status = request.getStatus();
					int organizationId = request.getOrganizationId();
					
					Calendar currentDate = Calendar.getInstance();
					currentDate.add(Calendar.HOUR_OF_DAY, 14);
					Date expireDateSession = currentDate.getTime();
					
					
					Record record = new Record();
					record.setTeamMemberDetailId(teamMemberDetailId);
					record.setProjectDetailId(projectDetailId);
					record.setActivityId(activityId);
					record.setStartTime(startTime);
					record.setBillable(billable);
					record.setStatus(status);
					//record.setPersonDetailId(userId);
					record.setDescription("");
					record.setTitle("");
					record.setOrganizationId(organizationId);
					Record addedRecord = BLRecord.getInstance().add(record);
					int recordId = addedRecord.getRecordId();
					
					wSession.setSessionId(request.getSessionId());
					wSession.setUserId(userId);
					wSession.setCreationDate(startTime);
					wSession.setExpireDate(expireDateSession);
					BLWidgetSession.getInstance().add(wSession);
					
					RecordSession recordSession = new RecordSession();
					recordSession.setRecordId(recordId);
					recordSession.setSessionId(request.getSessionId());
					BLRecordSession.getInstance().add(recordSession);
	
					//create the response
					response.setRecordId(recordId);
					response.setValidSession(true);
					response.setStartTime(startTime);
				}
			}
			catch (Exception ex) {
				response.setValidSession(false);
				logger.error("", ex);
			}
				

		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_RECORD_DETAILS, e);
		}
		logger.debug("GetRecordDetailsBySessionId END");
		return response;
	}
	
	
	/**
	 * @author Andreea
	 * Endpoint method for saving the timesheet requests
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "SendStopTimeSheetRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public SendStopTimeSheetResponse sendStopTimeSheet(SendStopTimeSheetRequest request) throws EndpointException {
		logger.debug("sendStopTimeSheet START");
		SendStopTimeSheetResponse response = new SendStopTimeSheetResponse();
		try {
			//get the criteria from the request, used to retrieve the record
			int recordId = request.getRecordId();
			//get the record 
			Record record = BLRecord.getInstance().get(recordId);
			
			//get the endTime
			Date end = request.getEndTime();
			record.setEndTime(end);
			
			//get the start time
			Date start = record.getStartTime();
			TimeInterval time = ControllerUtils.getInstance().calculateTimeInterval(start, end);
			
			//calculate the time interval, using the startTime and the endTime
			Long hours = time.getHours();
			String hoursS = null;
			if (hours < 10)
				hoursS = "0" + hours;
			else
				hoursS = "" + hours;
			
			Long minutes = time.getMinutes();
			String minutesS = null;
			if (minutes < 10)
				minutesS = "0" + minutes;
			else
				minutesS = "" + minutes;
			
			String timeString = hoursS + ":" + minutesS;

			record.setTime(timeString);
			record.setDescription(request.getDescription());
			record.setObservation(request.getObservation());
			record.setTitle(request.getTitle());

			
			int recordUpdated = -1;
			try {
				BLRecord.getInstance().update(record);
				recordUpdated = recordId;
			} catch (Exception e) {
				
			}
			
			//create the response
			response.setRecordId(recordUpdated);

		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_SET_RECORD_TIMESHEET, e);
		}
		logger.debug("sendStopTimeSheet END");
		return response;
	}
	
	/**
	 * @author coni
	 * Endpoint method for time sheet type report data requests
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "TimeSheetReportDatasourceRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public TimeSheetReportDatasourceResponse getTimeSheetReportData(TimeSheetReportDatasourceRequest request) throws EndpointException {
		logger.debug("getTimeSheetReportData START");
		TimeSheetReportDatasourceResponse response = new TimeSheetReportDatasourceResponse();
		try {
			//get the criteria from the request, used to retrieve the report data 
			TSReportGetDataCriteria getDataCriteria = request.getGetDataCriteria();
			//get the report's data
			WSRecords wsRecords = BLReportsDataSource.getInstance().getTimeSheetReportRecords(getDataCriteria);
			
			//create the response
			response.setRecords(wsRecords);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_GET_TIME_SHEET_REPORT_DATA, e);
		}
		logger.debug("getTimeSheetReportData END");
		return response;
	}
	
	/**
	 * Deletes a ProjectDetails
	 * 
	 * @author Adelina
	 *  
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "GetProjectIdForDeleteRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public void deleteProjectDetails(GetProjectIdForDeleteRequest request) throws EndpointException {
		logger.debug("deleteProject START");		
		try {
			//get the project id from the request, 
			Integer projectId = request.getProjectId();				
			//used to delete a project details
			BLProjectDetails.getInstance().deleteProjectDetails(projectId);								
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_DELETE_PROJECT_DETAILS, e);
		}
		logger.debug("deleteProject END");		
	}	
	
	/**
	 * Deletes a Team Member Detail
	 * 
	 * @author Adelina
	 *  
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "GetTeamMemberIdForDeleteRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public void deleteTeamMemberDetails(GetTeamMemberIdForDeleteRequest request) throws EndpointException {
		logger.debug("deleteTeamMemberDetails START");		
		try {
			//get the member id from the request, 
			Integer memberId = request.getTeamMemberId();
			
			//used to delete the team member detail
			BLTeamMemberDetail.getInstance().deleteTeamMemberDetail(memberId);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_DELETE_TEAM_MEMBER_DETAIL, e);
		}
		logger.debug("deleteTeamMemberDetails END");		
	}
	
	/**
	 * Finishes a ProjectDetails
	 * 
	 * @author Adelina
	 *  
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "GetProjectIdForFinishRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public void finishProjectDetails(GetProjectIdForFinishRequest request) throws EndpointException {
		logger.debug("finishProjectDetails START");		
		try {
			//get the project id from the request, 
			Integer projectId = request.getProjectId();	
			
			//used to finish a project details
			BLProjectDetails.getInstance().finishProjectDetails(projectId);		
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_FINISH_PROJECT_DETAILS, e);
		}
		logger.debug("finishProjectDetails END");		
	}
	
	/**
	 * Aborts a ProjectDetails
	 * 
	 * @author Adelina
	 *  
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "GetProjectIdForAbortRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public void abortProjectDetails(GetProjectIdForAbortRequest request) throws EndpointException {
		logger.debug("abortProjectDetails START");		
		try {
			//get the project id from the request, 
			Integer projectId = request.getProjectId();	
			
			//used to abort a project details
			BLProjectDetails.getInstance().abortProjectDetails(projectId);		
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_ABORT_PROJECT_DETAILS, e);
		}
		logger.debug("abortProjectDetails END");		
	}
	
	/**
	 * Aborts a ProjectDetails
	 * 
	 * @author Adelina
	 *  
	 * @throws EndpointException 
	 */
	@PayloadRoot(localPart = "GetProjectIdForOpenRequest", namespace = IConstant.TS_WS_SERVER_NAMESPACE)
	public void openProjectDetails(GetProjectIdForOpenRequest request) throws EndpointException {
		logger.debug("abortProjectDetails START");		
		try {
			//get the project id from the request, 
			Integer projectId = request.getProjectId();	
			
			//used to open a project details
			BLProjectDetails.getInstance().openProjectDetails(projectId);
		} catch (Exception e) {
			logger.error("", e);
			throw new EndpointException(ICodeException.ENDPOINT_OPEN_PROJECT_DETAILS, e);
		}
		logger.debug("abortProjectDetails END");		
	}
}
