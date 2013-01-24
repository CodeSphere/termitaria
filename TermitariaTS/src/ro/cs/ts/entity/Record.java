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

import ro.cs.ts.common.IConstant;


/**
 * 
 * @author Coni
 * @author Adelina
 *
 */
public class Record {

	private int recordId;
	private Integer teamMemberDetailId;
	private Integer projectDetailId;
	private Integer activityId;
	private Date startTime;
	private Date endTime;
	private String time;
	private Character billable;
	private String title;
	private String observation;
	private String description;
	private Date overTimeStartTime;
	private Date overTimeEndTime;
	private String overTimeTime;
	private Character overTimeBillable;
	private byte status;	
	private Integer personDetailId;
	private PersonDetail personDetail;
	private int organizationId;
	private ProjectDetails projectDetails;
	private TeamMemberDetail teamMemberDetail;
	private Activity activity;
	private String panelHeaderName;	// header name for the info panel
	private String recordOwnerName; // the user that sets the record
	private String projectName;
	private boolean workHoursRecord;
	private boolean overtimeRecord;
	private Integer userId;
	private Integer projectId;
	private boolean isBillable; //true if billable or overtimeBillable are true	
	private Integer projectManagerId;

	public Record(){
		this.recordId = -1;
		this.status = IConstant.NOM_RECORD_STATUS_ACTIVE;		
		this.billable = new Character(IConstant.BILLABLE_YES);
	}

	public boolean getIsBillable() {
		return this.isBillable;
	}
	
	public void setIsBillable() {
		if ((this.getBillable() != null && IConstant.BILLABLE_YES.equals(this.getBillable())) || (this.getOverTimeBillable() != null && IConstant.BILLABLE_YES.equals(this.getOverTimeBillable()))) {
			this.isBillable = true;
		} else {
			this.isBillable = false;
		}
	}
	
	public int getRecordId() {
		return recordId;
	}
	

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public Integer getTeamMemberDetailId() {
		return teamMemberDetailId;
	}

	public void setTeamMemberDetailId(Integer teamMemberDetailId) {
		this.teamMemberDetailId = teamMemberDetailId;
	}

	public Integer getProjectDetailId() {
		return projectDetailId;
	}

	public void setProjectDetailId(Integer projectDetailId) {
		this.projectDetailId = projectDetailId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
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

	public Character getBillable() {
		return billable;
	}

	public void setBillable(Character billable) {
		this.billable = billable;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public Character getOverTimeBillable() {
		return overTimeBillable;
	}

	public void setOverTimeBillable(Character overTimeBillable) {
		this.overTimeBillable = overTimeBillable;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Integer getPersonDetailId() {
		return personDetailId;
	}

	public void setPersonDetailId(Integer personDetailId) {
		this.personDetailId = personDetailId;
	}

	public PersonDetail getPersonDetail() {
		return personDetail;
	}

	public void setPersonDetail(PersonDetail personDetail) {
		this.personDetail = personDetail;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public ProjectDetails getProjectDetails() {
		return projectDetails;
	}

	public void setProjectDetails(ProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}

	public TeamMemberDetail getTeamMemberDetail() {
		return teamMemberDetail;
	}

	public void setTeamMemberDetail(TeamMemberDetail teamMemberDetail) {
		this.teamMemberDetail = teamMemberDetail;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getPanelHeaderName() {
		return panelHeaderName;
	}

	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}

	public String getRecordOwnerName() {
		return recordOwnerName;
	}

	public void setRecordOwnerName(String recordOwnerName) {
		this.recordOwnerName = recordOwnerName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public boolean isWorkHoursRecord() {
		return workHoursRecord;
	}

	public void setWorkHoursRecord(boolean workHoursRecord) {
		this.workHoursRecord = workHoursRecord;
	}

	public boolean isOvertimeRecord() {
		return overtimeRecord;
	}

	public void setOvertimeRecord(boolean overtimeRecord) {
		this.overtimeRecord = overtimeRecord;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
		
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the overTimeTime
	 */
	public String getOverTimeTime() {
		return overTimeTime;
	}

	/**
	 * @param overTimeTime the overTimeTime to set
	 */
	public void setOverTimeTime(String overTimeTime) {
		this.overTimeTime = overTimeTime;
	}		

	/**
	 * @return the projectManagerId
	 */
	public Integer getProjectManagerId() {
		return projectManagerId;
	}

	/**
	 * @param projectManagerId the projectManagerId to set
	 */
	public void setProjectManagerId(Integer projectManagerId) {
		this.projectManagerId = projectManagerId;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("recordId = ")					.append(recordId)				.append(", ");
		sb.append("startTime = ")					.append(startTime)				.append(", ");		
		sb.append("endTime = ")						.append(endTime)				.append(", ");				
		sb.append("overtimeStartTime = ")			.append(overTimeStartTime)		.append(", ");			
		sb.append("overtimeEndTime = ")				.append(overTimeEndTime)		.append(", ");
		sb.append("projectDetailId = ")				.append(projectDetailId)		.append(", ");		
		sb.append("recordOwnerName = ")				.append(recordOwnerName)		.append(", ");				
		sb.append("activityId = ")					.append(activityId)				.append(", ");			
		sb.append("personDetailId = ")				.append(personDetailId)			.append(", ");
		sb.append("teamMemerDetailId = ")			.append(teamMemberDetailId)		.append(", ");		
		sb.append("projectDetails = ")				.append(projectDetails)			.append(", ");	
		sb.append("teamMemberDetail = ")			.append(teamMemberDetail)		.append(", ");	
		sb.append("activity = ")					.append(activity)				.append(", ");	
		sb.append("personDetail = ")				.append(personDetail)			.append(", ");	
		sb.append("time = ")						.append(time)					.append(", ");	
		sb.append("overtimeTime = ")				.append(overTimeTime)			.append(", ");	
		sb.append("billable  = ")					.append(billable)				.append(", ");	
		sb.append("overTimeBillable = ")			.append(overTimeBillable)		.append(", ");	
		sb.append("workHoursRecord = ")				.append(workHoursRecord)		.append(", ");	
		sb.append("overtimeRecord = ")				.append(overtimeRecord)			.append(", ");	
		sb.append("projectId = ")					.append(projectId)				.append(" ] ");		
		
		return sb.toString();
	}

	
}
