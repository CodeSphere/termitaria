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
package ro.cs.ts.model.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.oxm.XmlMappingException;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.common.Tools;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.SearchRecordBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.WSClientException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoRecord;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.cm.CMWebServiceClient;
import ro.cs.ts.ws.client.cm.entity.WSProject;
import ro.cs.ts.ws.client.cm.entity.WSTeamMember;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;
import ro.cs.ts.ws.server.entity.TSReportGetDataCriteria;


/**
 * Dao class for Record Entity
 * 
 * @author Coni
 */
public class DaoRecordImpl extends HibernateDaoSupport implements IDaoRecord {

	/**
	 * Searches for records using the criterion defined in searchRecordBean
	 * @author Coni
	 * @param searchClientBean
	 * @param isDeleteAction
	 * @return
	 * @throws WSClientException 
	 * @throws IOException 
	 * @throws XmlMappingException 
	 * @throws BusinessException 
	 */
	public List<Record> getResultsForSearch(SearchRecordBean searchRecordBean, boolean isDeleteAction) throws XmlMappingException, IOException, WSClientException, BusinessException {
		logger.debug("getResultsForSearch - START");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.recordForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.recordForListingEntity);
		
		//RECORD ID
		if (searchRecordBean.getId() != null) {
			dc.add(Restrictions.eq("recordId", searchRecordBean.getId()));
			dcCount.add(Restrictions.eq("recordId", searchRecordBean.getId()));
		}
		
		//ACTIVITY
		if (searchRecordBean.getActivityId() != -1) {
			dc.createCriteria("activity").add(Restrictions.eq("activityId", searchRecordBean.getActivityId()));
			dcCount.createCriteria("activity").add(Restrictions.eq("activityId", searchRecordBean.getActivityId()));
		}
		
		//START DATE
		if (searchRecordBean.getStartDate() != null) {
			//the search must retrieve: all the records for activities that started and haven't ended before the selected startDate 
			//and records for activities that will begin after the selected startDate; the conditions are applied for work hours and over time
			dc.add(Restrictions.or(Restrictions.or(Restrictions.ge("startTime", searchRecordBean.getStartDate()), 
													Restrictions.and(Restrictions.le("startTime", searchRecordBean.getStartDate()), Restrictions.ge("endTime", searchRecordBean.getStartDate()))), 
								   Restrictions.or(Restrictions.ge("overTimeStartTime", searchRecordBean.getStartDate()), 
										   			Restrictions.and(Restrictions.le("overTimeStartTime", searchRecordBean.getStartDate()), Restrictions.ge("overTimeEndTime", searchRecordBean.getStartDate())))));
			dcCount.add(Restrictions.or(Restrictions.or(Restrictions.ge("startTime", searchRecordBean.getStartDate()), 
										Restrictions.and(Restrictions.le("startTime", searchRecordBean.getStartDate()), Restrictions.ge("endTime", searchRecordBean.getStartDate()))), 
					   Restrictions.or(Restrictions.ge("overTimeStartTime", searchRecordBean.getStartDate()), 
							   			Restrictions.and(Restrictions.le("overTimeStartTime", searchRecordBean.getStartDate()), Restrictions.ge("overTimeEndTime", searchRecordBean.getStartDate())))));
		}
		
		//END DATE
		if (searchRecordBean.getEndDate() != null) {
			//the search must retrieve: all the records for activities that started before the selected endDate, no matter they are ended or not;
			//; the conditions are applied for work hours and over time
			dc.add(Restrictions.or(Restrictions.le("startTime", searchRecordBean.getEndDate()), Restrictions.le("overTimeStartTime", searchRecordBean.getEndDate())));
			dcCount.add(Restrictions.or(Restrictions.le("startTime", searchRecordBean.getEndDate()), Restrictions.le("overTimeStartTime", searchRecordBean.getEndDate())));
		}
		
		//BILLABLE
		if (searchRecordBean.getBillable() != null) {
			if (searchRecordBean.getBillable().equals(IConstant.NOM_BILLABLE_YES) || searchRecordBean.getBillable().equals(IConstant.NOM_BILLABLE_NO)) {
				dc.add(Restrictions.or(Restrictions.eq("billable", searchRecordBean.getBillable()), Restrictions.eq("overTimeBillable", searchRecordBean.getBillable())));
				dcCount.add(Restrictions.or(Restrictions.eq("billable", searchRecordBean.getBillable()), Restrictions.eq("overTimeBillable", searchRecordBean.getBillable())));
			}
		}
		
		//ORGANIZATION ID
		if (searchRecordBean.getOrganizationId() != null) {
			dc.add(Restrictions.eq("organizationId", searchRecordBean.getOrganizationId()));
			dcCount.add(Restrictions.eq("organizationId", searchRecordBean.getOrganizationId()));
		}
		
		//STATUS
		dc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
		dcCount.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
		
		List<TeamMember> members = null;
		List<UserSimple> users = null;
		//FIRST NAME AND LAST NAME
		if ((searchRecordBean.getFirstName() != null && !"".equals(searchRecordBean.getFirstName())) ||
				(searchRecordBean.getLastName() != null && !"".equals(searchRecordBean.getLastName()))) {
			
			List<Integer> memberIds = new ArrayList<Integer>();
			if (searchRecordBean.getProjectId() != IConstant.NOM_RECORD_SEARCH_FOR_ORG) {
				List<TeamMember> teamMembers = BLTeamMember.getInstance().getTeamMemberFromSearch(searchRecordBean.getOrganizationId(),
						searchRecordBean.getFirstName(), searchRecordBean.getLastName(), false);			
				if (teamMembers != null && !teamMembers.isEmpty()) {
					members = teamMembers;					
					for (TeamMember teamMember : teamMembers) {
						memberIds.add(new Integer(teamMember.getMemberId()));
					}
				}
			}
			
			//search for persons only if the selected option is to search in all or per organization,
			//otherwise only search for team members
			List<Integer> personIds = new ArrayList<Integer>();
			if (searchRecordBean.getProjectId() == IConstant.NOM_RECORD_SEARCH_ALL || searchRecordBean.getProjectId() == IConstant.NOM_RECORD_SEARCH_FOR_ORG) {
				List<UserSimple> persons = BLUser.getInstance().getUserSimpleFromSearch(searchRecordBean.getOrganizationId(), searchRecordBean.getFirstName(), searchRecordBean.getLastName(), true);
				if (persons != null && !persons.isEmpty()) {
					users = persons;
					for (UserSimple person : persons) {
						personIds.add(person.getUserId());
					}
				}
			}
			
			if (!memberIds.isEmpty() && !personIds.isEmpty()) {
				List<Integer> recordIds = new ArrayList<Integer>();
				
				DetachedCriteria teamMemberDc = DetachedCriteria.forEntityName(IModelConstant.recordWithTeamMemberDetailEntity);
				teamMemberDc.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", memberIds));
				teamMemberDc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
				List<Record> resForTeamMembers = (List<Record>) getHibernateTemplate().findByCriteria(teamMemberDc);
				if (resForTeamMembers != null && !resForTeamMembers.isEmpty()) {
					for (Record rec : resForTeamMembers) {
						recordIds.add(rec.getRecordId());
					}
				}
				DetachedCriteria personDc = DetachedCriteria.forEntityName(IModelConstant.recordWithPersonDetailEntity);
				personDc.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
				personDc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
				List<Record> resForPersons = (List<Record>) getHibernateTemplate().findByCriteria(personDc);
				if (resForPersons != null && !resForPersons.isEmpty()) {
					for (Record rec : resForPersons) {
						recordIds.add(rec.getRecordId());
					}
				}
				
				if (!recordIds.isEmpty()) {
					dc.add(Restrictions.in("recordId", recordIds));
					dcCount.add(Restrictions.in("recordId", recordIds));
				}
			} else if (!memberIds.isEmpty()) {
				dc.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", memberIds));
				dcCount.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", memberIds));
			} else if (!personIds.isEmpty()) {
				dc.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
				dcCount.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
			} else  {
				return new ArrayList<Record>();
			}
		} 
		
		List<Project> projects = null;
		//PROJECT AND TEAM MEMBER
		//if no project is selected, we use a subquery to retrieve a list of record ids from which we will search for records
		//using the other search criteria (including first name and last name)
		//if a project is selected, firstName or lastName won't exist as search criteria, only teamMemberId must be taken into account 
		if (searchRecordBean.getTeamMemberId() == -1 && searchRecordBean.getProjectId() == IConstant.NOM_RECORD_SEARCH_ALL) { 		//search in all the records
			
			//if the user doesn't have the permission to search in all the records, the projectId must be 
			//included in the list of ids for the projects in which the user is a team member
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordSearchAll())) {
								
				HashMap<Project, TeamMember> projectsAndTeamMembers = BLProject.getInstance().getProjectsAndTeamMembersByPerson(userAuth.getPersonId(), true, true);
				if (projectsAndTeamMembers != null && !projectsAndTeamMembers.isEmpty()) {
					projects = new ArrayList<Project>(projectsAndTeamMembers.keySet());
					List<Integer> recordIds = new ArrayList<Integer>();
					Iterator it = projectsAndTeamMembers.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();
						//the subquery is used to retrieve the list of records ids retrieved for the teamMemberId and projectId search criteria;
						DetachedCriteria subquery = null;
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery = DetachedCriteria.forEntityName(IModelConstant.recordWithProjectAndTeamMemberDetailsEntity);
						} else {
							subquery = DetachedCriteria.forEntityName(IModelConstant.recordWithProjectDetailsEntity);
						}
						subquery.createCriteria("projectDetails").add(Restrictions.eq("projectId", entry.getKey().getProjectId()));
						subquery.setProjection(Projections.id());
						//if the user is the PM for this project, we set a restriction only on projectId,
						//otherwise the restriction is on teamMemberId
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", entry.getValue().getMemberId()));
						}
						subquery.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
						List<Integer> recordIdsTemp = (List<Integer>) getHibernateTemplate().findByCriteria(subquery);
						if (recordIdsTemp != null) {
							for (Integer id : recordIdsTemp){
								recordIds.add(id);
							}
						}
					}

					//search for records entered by the user for the organization (no project selected)
					DetachedCriteria subquery = DetachedCriteria.forEntityName(IModelConstant.recordWithPersonDetailEntity);
					subquery.createCriteria("personDetail").add(Restrictions.eq("personId", userAuth.getPersonId()));
					subquery.setProjection(Projections.id());
					subquery.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
					List<Integer> recordIdsTemp = (List<Integer>) getHibernateTemplate().findByCriteria(subquery);
					if (recordIdsTemp != null) {
						for (Integer id : recordIdsTemp){
							recordIds.add(id);
						}
					}				
					
					if (!recordIds.isEmpty()) {
						dc.add(Restrictions.in("recordId", recordIds));
						dcCount.add(Restrictions.in("recordId", recordIds));
					} else {
						return new ArrayList<Record>();
					}
				}
			}
		
		} else if (searchRecordBean.getTeamMemberId() == -1 && searchRecordBean.getProjectId() == IConstant.NOM_RECORD_SEARCH_FOR_ORG) {
			//if the user doesn't have the permission to search in all the records per organization, i must set the personId property 
			//to the user correspondent personId
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordSearchAll())) {
				dc.createCriteria("personDetail").add(Restrictions.eq("personId", userAuth.getPersonId()));
				dcCount.createCriteria("personDetail").add(Restrictions.eq("personId", userAuth.getPersonId()));
			} else {
				//if the user has the permission to search in all the records for organization, i must set the condition that the personDetail property
				//mustn't be null
				dc.add(Restrictions.isNotNull("personDetail"));
				dcCount.add(Restrictions.isNotNull("personDetail"));
			}
		} else if (searchRecordBean.getTeamMemberId() == -1 && searchRecordBean.getProjectId() == IConstant.NOM_RECORD_SEARCH_IN_ALL_PROJECTS) { 		//search in the records for all the projects
			
			//if the user doesn't have the permission to search in all the records, the projectId must be 
			//included in the list of ids for the projects in which the user is a team member
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordSearchAll())) {
				HashMap<Project, TeamMember> projectsAndTeamMembers = BLProject.getInstance().getProjectsAndTeamMembersByPerson(userAuth.getPersonId(), true, true);
				if (projectsAndTeamMembers != null && !projectsAndTeamMembers.isEmpty()) {
					projects = new ArrayList<Project>(projectsAndTeamMembers.keySet());
					List<Integer> recordIds = new ArrayList<Integer>();
					Iterator it = projectsAndTeamMembers.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();
						//the subquery is used to retrieve the list of records ids retrieved for the teamMemberId and projectId search criteria;
						DetachedCriteria subquery = null;
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery = DetachedCriteria.forEntityName(IModelConstant.recordWithProjectAndTeamMemberDetailsEntity);
						} else {
							subquery = DetachedCriteria.forEntityName(IModelConstant.recordWithProjectDetailsEntity);
						}
						subquery.createCriteria("projectDetails").add(Restrictions.eq("projectId", entry.getKey().getProjectId()));
						subquery.setProjection(Projections.id());
						//if the user is the PM for this project, we set a restriction only on projectId,
						//otherwise the restriction is on teamMemberId
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", entry.getValue().getMemberId()));
						}
						subquery.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
						List<Integer> recordIdsTemp = (List<Integer>) getHibernateTemplate().findByCriteria(subquery);
						if (recordIdsTemp != null) {
							for (Integer id : recordIdsTemp){
								recordIds.add(id);
							}
						}
					}
						
					if (!recordIds.isEmpty()) {
						dc.add(Restrictions.in("recordId", recordIds));
						dcCount.add(Restrictions.in("recordId", recordIds));
					} else {
						return new ArrayList<Record>();
					}
				}
			} else {
				//the record must be entered for a specific project; records per organisation are excluded
				dc.add(Restrictions.isNotNull("projectDetails"));
				dcCount.add(Restrictions.isNotNull("projectDetails"));
			}
		} else if (searchRecordBean.getTeamMemberId() == -1 && searchRecordBean.getProjectId() > 0) {		//search in a project
			Project project = BLProject.getInstance().get(searchRecordBean.getProjectId(), false);
			if (project != null) {
				projects = new ArrayList<Project>();
				projects.add(project);
				//if the user is not PM for the selected project nor USER_ALL, it will see only its records
				if (!project.getManagerId().equals(userAuth.getPersonId()) && !userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordSearchAll())) {
					Iterator<TeamMember> it = project.getProjectTeam().getTeamMembers().iterator();
					while (it.hasNext()) {
						TeamMember member = it.next();
						if (member.getPersonId() == userAuth.getPersonId()) {
							dc.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", member.getMemberId()));
							dcCount.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", member.getMemberId()));
						}
					}
				}
			}
			//set the selected projectId as search criteria
			dc.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchRecordBean.getProjectId()));
			dcCount.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchRecordBean.getProjectId()));

		} else if (searchRecordBean.getTeamMemberId() != -1 && searchRecordBean.getProjectId() > 0) { 		//search in a project and for a specific teamMemberDetail
			//set the selected projectId and teamMemberId as search criteria
			dc.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchRecordBean.getProjectId()));
			dc.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", searchRecordBean.getTeamMemberId()));
			dcCount.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchRecordBean.getProjectId()));
			dcCount.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", searchRecordBean.getTeamMemberId()));
		}
		
		//the results list
		List<Record> res = null;
		
		//the order can be done while retrieving the data from the database only if the sort parameter is the activity name or the recordId;
		//this cannot be done if the results must be ordered by record owner name, project name or isBillable
		if (searchRecordBean.getSortParam() != null && ("activityName".equals(searchRecordBean.getSortParam()) || "recordId".equals(searchRecordBean.getSortParam()))) {
			// check if I have to order the results
			if("activityName".equals(searchRecordBean.getSortParam())) {
				// if I have to, check if I have to order them ascending or descending
				if (searchRecordBean.getSortDirection() == -1) {
					// ascending
					dc.createCriteria("activity").addOrder(Order.asc("name"));
				} else {
					// descending
					dc.createCriteria("activity").addOrder(Order.desc("name"));
				}
			} else if ("recordId".equals(searchRecordBean.getSortParam())) {
				if (searchRecordBean.getSortDirection() == -1) {
					// ascending
					dc.addOrder(Order.asc("recordId"));
				} else {
					// descending
					dc.addOrder(Order.desc("recordId"));
				}
			}
			
			// if the request didn't come from the pagination area, 
			// it means that I have to set the number of results and pages
			if (isDeleteAction || searchRecordBean.getNbrOfResults() == -1){
				boolean isSearch = false;
				if (searchRecordBean.getNbrOfResults() == -1 ) {
					isSearch = true;
				}
				// set the count(*) restriction			
				dcCount.setProjection(Projections.countDistinct("recordId"));
				
				//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
				//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
				int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
				logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
				searchRecordBean.setNbrOfResults(nbrOfResults);
				
				// get the number of pages
				if (nbrOfResults % searchRecordBean.getResultsPerPage() == 0) {
					searchRecordBean.setNbrOfPages(nbrOfResults / searchRecordBean.getResultsPerPage());
				} else {
					searchRecordBean.setNbrOfPages(nbrOfResults / searchRecordBean.getResultsPerPage() + 1);
				}
				// after a record is deleted, the same page has to be displayed;
				//only when all the client from last page are deleted, the previous page will be shown 
				if (isDeleteAction && (searchRecordBean.getCurrentPage() > searchRecordBean.getNbrOfPages()) ){
					searchRecordBean.setCurrentPage( searchRecordBean.getNbrOfPages() );
				} else if ( isSearch ) {
					searchRecordBean.setCurrentPage(1);
				}
			}
			
			res = (List<Record>) getHibernateTemplate().findByCriteria(dc, (searchRecordBean.getCurrentPage()-1) * searchRecordBean.getResultsPerPage(), searchRecordBean.getResultsPerPage());
		} 
		
		//setting the record owner name 
		if (res != null && res.size() > 0) {
			if (members == null) {
				HashSet<Integer> teamMemberIds = new HashSet<Integer>();
				for (Record record : res) {
					if (record.getTeamMemberDetail() != null) {						
						teamMemberIds.add(new Integer(record.getTeamMemberDetail().getTeamMemberId()));
					}
				}						
				if (teamMemberIds.size() > 0) {
					members = BLTeamMember.getInstance().getTeamMembersByMemberIds(teamMemberIds, false);							
				}
			}
			if (users == null) {
				HashSet<Integer> personIds = new HashSet<Integer>();
				for (Record record : res) {
					if (record.getPersonDetail() != null) {
						personIds.add(new Integer(record.getPersonDetail().getPersonId()));
					}
				}
				if (personIds.size() > 0) {
					String[] ids = new String[personIds.size()];
					int i = 0;
					for (Integer personId : personIds) {
						ids[i++] = personId.toString();
					}
					users = BLUser.getInstance().getUsersByPersonId(ids);
				}
			}
			
			if (projects == null) {
				HashSet<Integer> projectIds = new HashSet<Integer>();
				for (Record record : res) {
					if (record.getProjectDetails() != null) {
						projectIds.add(new Integer(record.getProjectDetails().getProjectId()));
					}
				}
				if (projectIds.size() > 0) {
					projects = BLProject.getInstance().getProjectsSimpleByProjectIds(projectIds);
				}
			}
			
			for (Record record : res) {								
				if (record.getTeamMemberDetail() != null && members != null) {						
					for (TeamMember member : members) {											
						if (record.getTeamMemberDetail().getTeamMemberId().equals(member.getMemberId())) {														
							record.setRecordOwnerName(member.getFirstName().concat(" ").concat(member.getLastName()));							
							break;
						}
					}
				} else if (record.getPersonDetail() != null && users != null) {
					for (UserSimple user : users) {
						if (record.getPersonDetail().getPersonId() == user.getUserId()) {
							record.setRecordOwnerName(user.getFirstName().concat(" ").concat(user.getLastName()));						
							break;
						}
					}
				}
				
				//setting the project name
				if (record.getProjectDetails() != null && record.getProjectDetails().getProjectId() != null) {
					if (projects != null) {
						for (Project project : projects) {
							if (record.getProjectDetails().getProjectId().equals(project.getProjectId())) {
								record.setProjectName(project.getName());
								record.setProjectManagerId(project.getManagerId());								
								break;
							}
						}
					}
				}
				record.setIsBillable();
			}
		}
				
		logger.debug("getResultsForSearch - END");
		return res;
	}
	
	/**
	 * Adds a new record
	 * @author Coni
	 * @param record
	 */
	public Integer add(Record record) {
		logger.debug("add - START");
		Integer recordId = (Integer) getHibernateTemplate().save(IModelConstant.recordEntity, record);
		logger.debug("add - END");
		return recordId;
	}
	
	/**
	 * Updates an existing record
	 * @author Coni
	 * @param record
	 */
	public void update(Record record) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.recordForUpdateEntity, record);
		logger.debug("update - END");
	}
	
	/**
	 * Gets the record identified by the specified recordId
	 * @author Coni
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record getAll(int recordId) {
		logger.debug("getAll - START");
		Record record = (Record) getHibernateTemplate().get(IModelConstant.recordForListingEntity, recordId);
		logger.debug("getAll - END");
		return record;
	}
	
	/**
	 * Returns a record
	 * 
	 * @author Andreea
	 */
	public Record get(int recordId) {
		logger.debug("get - START - id=".concat(String.valueOf(recordId)));
		Record record = (Record) getHibernateTemplate().get(IModelConstant.recordEntity, recordId);
		logger.debug("get - START");
		return record;
	}
	
	/**
	 * Deletes a record
	 * @author Coni
	 * @param recordId
	 * @return
	 * @throws BusinessException
	 */
	public Record delete(int recordId) {
		logger.debug("delete - START");
		Record record = getForDelete(recordId);
		record.setStatus(IConstant.NOM_RECORD_STATUS_DELETED);
		getHibernateTemplate().update(IModelConstant.recordSimpleEntity, record);
		logger.debug("delete - END");
		return record;
	}
	
	/**
	 * Returns a record
	 * 
	 * @author coni
	 */
	public Record getForDelete(int recordId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(recordId)));
		Record record = (Record) getHibernateTemplate().get(IModelConstant.recordSimpleEntity, recordId);
		logger.debug("getForDelete - START");
		return record;
	}
	
	/**
     * Gets the records identified by projectDetailId
     * 
     * @author Adelina
     * 
     * @param projectDetailId
     * @return
     */
    public List<Record> getSimpleByProjectDetailId(Integer projectDetailId) {
    	logger.debug("getByProjectDetailId - START");
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.recordSimpleEntity);
    	
    	dc.add(Restrictions.eq("projectDetailId", projectDetailId));
		dc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
    	
		List<Record> res = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("results = " + res);
				
    	logger.debug("getByProjectDetailId - END, res size = " + res.size());
    	return res;
    }     
    
    /**
     * Deletes a record, by changing the status to deleted
     * 
     * @author Adelina
     * 
     * @param record
     * @return
     */
    public Record deleteSimple(Record record) {
		logger.debug("deleteSimple - START");
		logger.debug("Deleting record with id: ".concat(String.valueOf(record.getRecordId())));
			
		record.setStatus(IConstant.NOM_ACTIVITY_STATUS_DELETED);
		
		logger.debug("Deleting the record : " + record);
		getHibernateTemplate().update(IModelConstant.recordSimpleEntity, record);
		logger.debug("Record " + record + " has been deleted");
		logger.debug("deleteSimple  - END");
		return record;
	}		
	
    /**
     * Gets the Project Report records
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws WSClientException 
     * @throws IOException 
     * @throws XmlMappingException 
     * @throws BusinessException
     */
    public List<Record> getProjectReportRecords(TSReportGetDataCriteria getDataCriteria) throws XmlMappingException, IOException, WSClientException {
    	logger.debug("getProjectReportRecords - START");
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.recordForReportsEntity);
    	
		//the prices can be computed per activity or per resource
		Integer priceComputeType = (Integer) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE);

    	
    	//PROJECT
    	Integer projectId = (Integer) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_ID);
    	if (projectId != null && projectId > 0) {
    		dc.createCriteria("projectDetails").add(Restrictions.eq("projectId", projectId));
    	}
    	
		//START DATE
    	if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_START_DATE) != null) {
    		Date startDate = (Date) ((XMLGregorianCalendar) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_START_DATE)).toGregorianCalendar().getTime();
			if (startDate != null) {
				//the search must retrieve: all the records for activities that started and haven't ended before the selected startDate 
				//and records for activities that will begin after the selected startDate; the conditions are applied for work hours and over time
				dc.add(Restrictions.or(Restrictions.or(Restrictions.ge("startTime", startDate), 
														Restrictions.and(Restrictions.le("startTime", startDate), Restrictions.ge("endTime", startDate))), 
									   Restrictions.or(Restrictions.ge("overTimeStartTime", startDate), 
											   			Restrictions.and(Restrictions.le("overTimeStartTime", startDate), Restrictions.ge("overTimeEndTime", startDate)))));
			}
    	}
		
		//END DATE
    	if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_END_DATE) != null) {
			Date endDate = (Date) ((XMLGregorianCalendar) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_END_DATE)).toGregorianCalendar().getTime();
			if (endDate != null) {
				//the search must retrieve: all the records for activities that started before the selected endDate, no matter they are ended or not;
				//; the conditions are applied for work hours and over time
				dc.add(Restrictions.or(Restrictions.le("startTime", endDate), Restrictions.le("overTimeStartTime", endDate)));
			}
    	}
		
		//BILLABLE
		//if the prices must be computed per activities, the billable search criteria it is applied to the record activity billable property
		String billable = (String) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_BILLABLE);
		if (billable != null) {
			if (billable.equals(IConstant.NOM_BILLABLE_YES.toString()) || billable.equals(IConstant.NOM_BILLABLE_NO.toString())) {
				if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE)) {
					dc.add(Restrictions.or(Restrictions.eq("billable", billable), Restrictions.eq("overTimeBillable", billable)));
				} else if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
					dc.createCriteria("activity").add(Restrictions.eq("billable", billable));
				}
			}
		}
		
		//STATUS
		dc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
		
		List<Record> result = getHibernateTemplate().findByCriteria(dc);
		
		if (result != null) {
			List<WSTeamMember> members = null;
			
			HashSet<Integer> teamMemberIds = new HashSet<Integer>();
			for (Record record : result) {
				if (record.getTeamMemberDetail() != null) {
					teamMemberIds.add(new Integer(record.getTeamMemberDetail().getTeamMemberId()));
				}
			}
			if (teamMemberIds.size() > 0) {
				members = CMWebServiceClient.getInstance().getTeamMembersByMemberIds(teamMemberIds, false);
			}
			
			for (Record record : result) {
				if (members != null && !members.isEmpty()) {
					for (WSTeamMember member : members) {
						if (record.getTeamMemberDetail().getTeamMemberId().equals(member.getMemberId())) {
							record.setRecordOwnerName(member.getFirstName().concat(" ").concat(member.getLastName()));
						}
					}
				}
				//if the prices must be computed per activities, i must set the records billable and overTimeBillable properties depending on their corresponding activity 
				//billable property value
				if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
					if (record.getTime() != null && !record.getTime().equals("")) {
						record.setBillable(record.getActivity().getBillable());
					} else {
						record.setBillable(null);
					}
					if (record.getOverTimeTime() != null && !record.getOverTimeTime().equals("")) {
						record.setOverTimeBillable(record.getActivity().getBillable());
					} else {
						record.setOverTimeBillable(null);
					}
				}
			}
		}
    	
    	logger.debug("getProjectReportRecords - END");
    	return result;
    }
    
    /**
     * Gets the Time Sheet Report records
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws WSClientException 
     * @throws IOException 
     * @throws XmlMappingException 
     * @throws BusinessException
     */
    public List<Record> getTimeSheetReportRecords(TSReportGetDataCriteria getDataCriteria) throws XmlMappingException, IOException, WSClientException {
    	logger.debug("getTimeSheetReportRecords - START");
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.recordForReportsEntity);
    	
		//the prices can be computed per activity or per resource
		Integer priceComputeType = (Integer) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_RECORD_PRICE_COMPUTE_TYPE);

    	
    	//TEAM MEMBER ID AND PERSON ID
		List<Integer> teamMemberIds = null;
		String teamMemberIdsAsString = (String) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_TEAM_MEMBER_IDS);
		if (teamMemberIdsAsString != null) {
			teamMemberIds = (List<Integer>) Arrays.asList(Tools.getInstance().castStringToIntegerArray(teamMemberIdsAsString.split(",")));
		}
		List<Integer> personIds = null;
    	String personIdsAsString = (String) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_PERSON_IDS);
		if (personIdsAsString != null) {
			personIds = (List<Integer>) Arrays.asList(Tools.getInstance().castStringToIntegerArray(personIdsAsString.split(",")));
		}
		
    	if (teamMemberIds != null && !teamMemberIds.isEmpty() && personIds != null && !personIds.isEmpty()) {
			List<Integer> recordIds = new ArrayList<Integer>();
			
			DetachedCriteria teamMemberDc = DetachedCriteria.forEntityName(IModelConstant.recordWithTeamMemberDetailEntity);
			teamMemberDc.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", teamMemberIds));
			teamMemberDc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
			teamMemberDc.setProjection(Projections.id());
			List<Integer> resForTeamMembers = (List<Integer>) getHibernateTemplate().findByCriteria(teamMemberDc);
			recordIds.addAll(resForTeamMembers);

			DetachedCriteria personDc = DetachedCriteria.forEntityName(IModelConstant.recordWithPersonDetailEntity);
			personDc.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
			personDc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
			personDc.setProjection(Projections.id());
			List<Integer> resForPersons = (List<Integer>) getHibernateTemplate().findByCriteria(personDc);
			recordIds.addAll(resForPersons);
			
			if (!recordIds.isEmpty()) {
				dc.add(Restrictions.in("recordId", recordIds));
			}
		} else if (teamMemberIds!= null && !teamMemberIds.isEmpty()) {
			dc.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", teamMemberIds));
		} else if (personIds!= null && !personIds.isEmpty()) {
			dc.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
		}
    	
		//START DATE
    	if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_START_DATE) != null) {
	    	Date startDate = (Date) ((XMLGregorianCalendar) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_START_DATE)).toGregorianCalendar().getTime();
			if (startDate != null) {
				//the search must retrieve: all the records for activities that started and haven't ended before the selected startDate 
				//and records for activities that will begin after the selected startDate; the conditions are applied for work hours and over time
				dc.add(Restrictions.or(Restrictions.or(Restrictions.ge("startTime", startDate), 
														Restrictions.and(Restrictions.le("startTime", startDate), Restrictions.ge("endTime", startDate))), 
									   Restrictions.or(Restrictions.ge("overTimeStartTime", startDate), 
											   			Restrictions.and(Restrictions.le("overTimeStartTime", startDate), Restrictions.ge("overTimeEndTime", startDate)))));
			}
    	}
		
		//END DATE
    	if (getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_END_DATE) != null) {
			Date endDate = (Date) ((XMLGregorianCalendar) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_END_DATE)).toGregorianCalendar().getTime();
			if (endDate != null) {
				//the search must retrieve: all the records for activities that started before the selected endDate, no matter they are ended or not;
				//; the conditions are applied for work hours and over time
				dc.add(Restrictions.or(Restrictions.le("startTime", endDate), Restrictions.le("overTimeStartTime", endDate)));
			}
    	}
		
		//BILLABLE
		//if the prices must be computed per activities, the billable search criteria it is applied to the record activity billable property
		String billable = (String) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_BILLABLE);
		if (billable != null) {
			if (billable.equals(IConstant.NOM_BILLABLE_YES.toString()) || billable.equals(IConstant.NOM_BILLABLE_NO.toString())) {
				if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_RESOURCE)) {
					dc.add(Restrictions.or(Restrictions.eq("billable", billable), Restrictions.eq("overTimeBillable", billable)));
				} else if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
					dc.createCriteria("activity").add(Restrictions.eq("billable", billable));
				}
			}
		}
		
		//ORGANIZATION ID
		Integer organizationId = (Integer) getDataCriteria.getProperties().get(IConstant.TS_TIME_SHEET_REPORT_SEARCH_CRITERIA_ORGANIZATION_ID);
		if (organizationId != null) {
			dc.add(Restrictions.eq("organizationId", organizationId));
		}
		
		//STATUS
		dc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
		
		List<Record> res = getHibernateTemplate().findByCriteria(dc);
		
		if (res != null) {
			List<WSTeamMember> members = null;
			List<UserSimple> users = null;
			List<WSProject> projects = null;
			
			HashSet<Integer> memberIds = new HashSet<Integer>();
			for (Record record : res) {
				if (record.getTeamMemberDetail() != null) {
					memberIds.add(new Integer(record.getTeamMemberDetail().getTeamMemberId()));
				}
			}
			if (memberIds.size() > 0) {
				members = CMWebServiceClient.getInstance().getTeamMembersByMemberIds(memberIds, false);
			}

			HashSet<Integer> userIds = new HashSet<Integer>();
			for (Record record : res) {
				if (record.getPersonDetail() != null) {
					userIds.add(new Integer(record.getPersonDetail().getPersonId()));
				}
			}
			if (userIds.size() > 0) {
				String[] ids = new String[userIds.size()];
				int i = 0;
				for (Integer userId : userIds) {
					ids[i++] = userId.toString();
				}
				users = OMWebServiceClient.getInstance().getUsersSimpleByPersonId(ids).getUsers();
			}
			
			HashSet<Integer> projectIds = new HashSet<Integer>();
			for (Record record : res) {
				if (record.getProjectDetails() != null) {
					projectIds.add(new Integer(record.getProjectDetails().getProjectId()));
				}
			}
			if (projectIds.size() > 0) {
				projects = CMWebServiceClient.getInstance().getProjectsSimpleByProjectIds(projectIds);
			}

			for (Record record : res) {
				if (record.getTeamMemberDetail() != null && members != null) {
					for (WSTeamMember member : members) {
						if (record.getTeamMemberDetail().getTeamMemberId().equals(member.getMemberId())) {
							record.setRecordOwnerName(member.getFirstName().concat(" ").concat(member.getLastName()));
							break;
						}
					}
				} else if (record.getPersonDetail() != null && users != null) {
					for (UserSimple user : users) {
						if (record.getPersonDetail().getPersonId() == user.getUserId()) {
							record.setRecordOwnerName(user.getFirstName().concat(" ").concat(user.getLastName()));
							break;
						}
					}
				}
				
				//setting the project name
				if (record.getProjectDetails() != null && record.getProjectDetails().getProjectId() != null) {
					if (projects != null) {
						for (WSProject project : projects) {
							if (record.getProjectDetails().getProjectId().equals(project.getProjectId())) {
								record.setProjectName(project.getName());
								break;
							}
						}
					}
				}
				//if the prices must be computed per activities, i must set the records billable and overTimeBillable properties depending on their corresponding activity 
				//billable property value
				if (priceComputeType.equals(IConstant.NOM_RECORD_PRICES_COMPUTE_TYPE_FOR_ACTIVITY)) {
					if (record.getTime() != null && !record.getTime().equals("")) {
						record.setBillable(record.getActivity().getBillable());
					} else {
						record.setBillable(null);
					}
					if (record.getOverTimeTime() != null && !record.getOverTimeTime().equals("")) {
						record.setOverTimeBillable(record.getActivity().getBillable());
					} else {
						record.setOverTimeBillable(null);
					}
				}
			}
		}
    	
    	logger.debug("getTimeSheetReportRecords - END");
    	return res;
    }       
    
    /**
     * Returns the list of record for the given projectDetailId
     *
     * @author alu
     * @param projectDetailId
     * @return
     */
    public List<Record> getByProjectDetailId(Integer projectDetailId){
    	logger.debug("getByProjectId - START");
    	
    	StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.recordForPriceCalculation).append(" where projectDetailId = ? and status <> ?");
		
		Object[] o = {projectDetailId, IConstant.NOM_RECORD_STATUS_DELETED};		
		List<Record> recordList = (List<Record>) getHibernateTemplate().find(query.toString(), o);
		
		logger.debug("getByProjectId - END");
		return recordList;
    }
    
    /**
     * Returns the billing price of all the records from a project in the project currency
     *
     * @author alu
     * @param projectDetailId
     * @param projectsBudgetCurrencyId
     * @return
     */
    public Float calculateRecordsBillingPriceForProject(Integer projectDetailId, Integer projectsBudgetCurrencyId){
    	logger.debug("calculateRecordBillingPriceForProject - START projectDetailId: ".concat(String.valueOf(projectDetailId)));
    	
    	if (projectsBudgetCurrencyId != null) {
	    	Float totalBillingPrice = new Float(0);
	    	String billingPriceTeamMember = null;
	    	String overtimeBillingPriceTeamMember = null;
	    	Float workHoursExchangeRate = null;
	    	Float overtimeHoursExchangeRate = null;
	    	List<Exchange> exchangeRates = null;
	    	
	    	List<Record> records = getByProjectDetailId(projectDetailId);
	    	for (Record record: records){
	    		logger.debug("recordId: ".concat(String.valueOf(record.getRecordId())));
	    		
	    		workHoursExchangeRate = new Float(1);
		    	overtimeHoursExchangeRate = new Float(1);
	    		
		    	
	    		// for every record, first see if the record contains work hours and/or overtime hours
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
	    		
				if(record.isWorkHoursRecord() && IConstant.BILLABLE_YES.equals(record.getBillable())) {
					// calculate the billing price for work hours(for team member billing price)
					if(record.getTeamMemberDetail().getBillingPrice() != null && record.getTime() != null) {
						if(record.getStartTime() != null && record.getEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getStartTime(), record.getEndTime(), record.getTime())) {
							billingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(record.getTeamMemberDetail().getBillingPrice(), record.getTeamMemberDetail().getBillingTimeUnit(), record.getStartTime(), record.getEndTime());
						} else {
							billingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(record.getTeamMemberDetail().getBillingPrice(), record.getTeamMemberDetail().getBillingTimeUnit(), record.getTime());
						}
						logger.debug("billingPriceTeamMember = " + billingPriceTeamMember);
					}
					// check if I need the exchange rate between budget currency and team member detail currency
					if ((record.getTeamMemberDetail().getBillingPriceCurrencyId() != null) && (!(projectsBudgetCurrencyId.equals(record.getTeamMemberDetail().getBillingPriceCurrencyId())))){
						exchangeRates = DaoBeanFactory.getInstance().getDaoExchange().getProjectExchangeByCurrencies(record.getTeamMemberDetail().getBillingPriceCurrencyId(), projectsBudgetCurrencyId, projectDetailId);
						if ((exchangeRates != null) && (exchangeRates.size() > 0)){
							workHoursExchangeRate = exchangeRates.get(0).getRate();
						} else {
							// it means that I have to convert from a currency to another, but I don't have the exchange
							// don't add it to the total billing price
							workHoursExchangeRate = null;
						}
						logger.debug("workHoursExchangeRate = " + workHoursExchangeRate);
					}
					
					if (billingPriceTeamMember != null && workHoursExchangeRate != null){
						// add the billing price for work hours in the project currency
						totalBillingPrice += Float.valueOf(billingPriceTeamMember) * workHoursExchangeRate;
					}				
					logger.debug("totalBillingPrice: ".concat(String.valueOf(totalBillingPrice)));
				}
				
				if(record.isOvertimeRecord() && IConstant.BILLABLE_YES.equals(record.getOverTimeBillable())) {
					// calculate the billing price for overtime hours(for team member overtime billing price)
					if(record.getTeamMemberDetail().getOvertimeBillingPrice() != null && record.getOverTimeTime() != null) {
						if(record.getOverTimeStartTime() != null && record.getOverTimeEndTime() != null && ControllerUtils.getInstance().hasEqualTime(record.getOverTimeStartTime(), record.getOverTimeEndTime(), record.getOverTimeTime())) {
							overtimeBillingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(record.getTeamMemberDetail().getOvertimeBillingPrice(), record.getTeamMemberDetail().getOvertimeBillingTimeUnit(), record.getOverTimeStartTime(), record.getOverTimeEndTime());
						} else {
							overtimeBillingPriceTeamMember = ControllerUtils.getInstance().calculatePrice(record.getTeamMemberDetail().getOvertimeBillingPrice(), record.getTeamMemberDetail().getOvertimeBillingTimeUnit(), record.getOverTimeTime());
						}
						logger.debug("overtimeBillingPriceTeamMember = " + overtimeBillingPriceTeamMember);
					}
					// check if I need the exchange rate between budget currency and team member detail overtime currency
					if ((record.getTeamMemberDetail().getOvertimeBillingCurrencyId() != null) && (!(projectsBudgetCurrencyId.equals(record.getTeamMemberDetail().getOvertimeBillingCurrencyId())))){
						exchangeRates = DaoBeanFactory.getInstance().getDaoExchange().getProjectExchangeByCurrencies(record.getTeamMemberDetail().getOvertimeBillingCurrencyId(), projectsBudgetCurrencyId, projectDetailId);
						if ((exchangeRates != null) && (exchangeRates.size() > 0)){
							overtimeHoursExchangeRate = exchangeRates.get(0).getRate();
						} else {
							// it means that I have to convert from a currency to another, but I don't have the exchange
							// don't add it to the total billing price
							overtimeHoursExchangeRate = null;
						}
						logger.debug("overtimeHoursExchangeRate = " + overtimeHoursExchangeRate);
					}
					
					if (overtimeBillingPriceTeamMember != null && overtimeHoursExchangeRate != null){
						// add the billing price for overtime hours in the project currency
						totalBillingPrice += Float.valueOf(overtimeBillingPriceTeamMember) * overtimeHoursExchangeRate;
					}				
					logger.debug("totalBillingPrice: ".concat(String.valueOf(totalBillingPrice)));
				}			
	    		
	    	}
	    	
	    	logger.debug("calculateRecordBillingPriceForProject - END totalBillingPrice: ".concat(String.valueOf(totalBillingPrice)));
	    	return totalBillingPrice;
    	}
	
    	logger.debug("calculateRecordBillingPriceForProject - END - no project budget currency id");
		return new Float(0);
    }
    
    /**
     * Checks if exists a record for a person, for a specific project, activity, with the same work hours or overtime hours range
     * 
     * @author Adelina
     * 
     * @param recordId
     * @param teamMemberDetailId
     * @param activityId
     * @param startTime
     * @param endTime
     * @param time
     * @param overTimeStartTime
     * @param overTimeEndTime
     * @param overtimeTime
     * @param personDetailId
     * @return
     */
    public Record hasIdenticalRecordForPerson(Integer recordId, Integer teamMemberDetailId, Integer activityId, Date startTime, Date endTime, String time, Date overTimeStartTime, Date overTimeEndTime, String overtimeTime, Integer personDetailId) {
    	logger.debug("hasIdenticalRecordForPerson - START");    	
    	
    	// if we have a startTime and an endTime
    	if(startTime != null && endTime != null) {
    		    		
    		logger.debug("startTime = " + startTime);
    		logger.debug("endTime = " + endTime);
    		
    		boolean existRecord = false;
    		
    		// else, find if there is another record with the same conditions
     		DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.recordEntity);			    	
			if (teamMemberDetailId != null) {
				dc1.add(Restrictions.eq("teamMemberDetailId", teamMemberDetailId));
			} else if (personDetailId != null) {
				dc1.add(Restrictions.eq("personDetailId", personDetailId));
			}
	    	
	    	dc1.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
	    	dc1.add(Restrictions.eq("activityId", activityId));
	    	
	    	if(recordId != null && recordId > 0) {
	    		dc1.add(Restrictions.ne("recordId", recordId));		    		
	    	}
    		
    		dc1.add(Restrictions.or(Restrictions.or(Restrictions.or(Restrictions.and(Restrictions.ge("startTime", startTime), Restrictions.le("endTime", endTime)), Restrictions.and(Restrictions.lt("startTime", startTime), Restrictions.gt("endTime", startTime))), 
					Restrictions.or(Restrictions.and(Restrictions.lt("startTime", endTime), Restrictions.gt("endTime", endTime)), Restrictions.and(Restrictions.le("startTime", startTime), Restrictions.gt("endTime", endTime)))),
					Restrictions.or(Restrictions.or(Restrictions.and(Restrictions.ge("overTimeStartTime", startTime), Restrictions.le("overTimeEndTime", endTime)), Restrictions.and(Restrictions.lt("overTimeStartTime", startTime), Restrictions.gt("overTimeEndTime", startTime))), 
		    		Restrictions.or(Restrictions.and(Restrictions.lt("overTimeStartTime", endTime), Restrictions.gt("overTimeEndTime", endTime)), Restrictions.and(Restrictions.le("overTimeStartTime", startTime), Restrictions.gt("overTimeEndTime", endTime))))));
	    		    	
	    		    		    	
	    	List<Record> tempRecords = (List<Record>) getHibernateTemplate().findByCriteria(dc1);	    		    	
	    	Record record  = null;	    	
	    	if(tempRecords != null && tempRecords.size() > 0) {	   
	    		logger.debug("tempRecords size = " + tempRecords.size() + ", " + tempRecords);
	    		for(Record rec : tempRecords) {
	    			logger.debug("record = " + rec);
	    			if(rec.getStartTime() != null && rec.getEndTime() != null) {
	    				boolean hasOverlap = ControllerUtils.getInstance().hasOverlap(startTime, endTime, time, rec.getStartTime(), rec.getEndTime(), rec.getTime());
	    				logger.debug("hasOverlap = " + hasOverlap);
		    			if(hasOverlap) {
		    				existRecord = true;
		    				record = rec;
		    				break;
		    			}
	    			}
	    			if(rec.getOverTimeStartTime() != null && rec.getOverTimeEndTime() != null) {
	    				if(ControllerUtils.getInstance().hasOverlap(startTime, endTime, time, rec.getOverTimeStartTime(), rec.getOverTimeEndTime(), rec.getOverTimeTime())) {
	    					existRecord = true;
	    					record = rec;
	    					break;
		    			}
	    			}
	    		}
	    	}
	    	
	    	// if we have also an overtimeStartTime and an overtimeEndTime
    		if(overTimeStartTime != null && overTimeEndTime != null) {  
	    		
	    		logger.debug("overTimeStartTime = " + overTimeStartTime);
	    		logger.debug("overTimeEndTime = " + overTimeEndTime);
	    		
	    		// if the range for the work hours is the same or is an interval from the overtime hours
	    		// we can't add or update the record
	    		if(((startTime.after(overTimeStartTime) || startTime.equals(overTimeStartTime)) && (endTime.before(overTimeEndTime) || endTime.equals(overTimeEndTime))) || 
	    				(startTime.before(overTimeStartTime) && endTime.after(overTimeStartTime)) ||
	    				(startTime.before(overTimeEndTime) && endTime.after(overTimeEndTime)) || 
	    				((startTime.before(overTimeStartTime) || startTime.equals(overTimeStartTime)) && (endTime.after(overTimeEndTime) || endTime.equals(overTimeEndTime)))) {	
	    			logger.debug("hasIdenticalRecordForPerson - END");   
	    			if(ControllerUtils.getInstance().hasOverlap(startTime, endTime, time, overTimeStartTime, overTimeEndTime, overtimeTime)) {
	    				return new Record();
	    			} else {
	    				return null;
	    			}
	    		} else {   		
	    			if(existRecord == true) {
	    				logger.debug("hasIdenticalRecordForPerson - END, tempRecords = " + tempRecords + ", size = " + tempRecords.size());   
		    	    	return record;
		    		}
		    	}	 	    	    		   
    		} else {
    			if(existRecord == true) {
    				logger.debug("hasIdenticalRecordForPerson - END, tempRecords = " + tempRecords + ", size = " + tempRecords.size());   
	    	    	return record;
	    		}
    		}
    	}
    	
    	// also, for the overtime
    	if(overTimeStartTime != null && overTimeEndTime != null) {    		
    		
    		logger.debug("overTimeStartTime = " + overTimeStartTime);
    		logger.debug("overTimeEndTime = " + overTimeEndTime);
    		
    		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.recordEntity);
        	
        	if (teamMemberDetailId != null) {
        		dc.add(Restrictions.eq("teamMemberDetailId", teamMemberDetailId));
        	} else if (personDetailId != null) {
        		dc.add(Restrictions.eq("personDetailId", personDetailId));
        	}
        	dc.add(Restrictions.ne("status", IConstant.NOM_RECORD_STATUS_DELETED));
        	dc.add(Restrictions.eq("activityId", activityId));
        	if(recordId != null && recordId > 0) {
        		dc.add(Restrictions.ne("recordId", recordId));
        	}
        	    	 
    		dc.add(Restrictions.or(Restrictions.or(Restrictions.or(Restrictions.and(Restrictions.ge("startTime", overTimeStartTime), Restrictions.le("endTime", overTimeEndTime)), Restrictions.and(Restrictions.lt("startTime", overTimeStartTime), Restrictions.gt("endTime", overTimeStartTime))), 
					Restrictions.or(Restrictions.and(Restrictions.lt("startTime", overTimeEndTime), Restrictions.gt("endTime", overTimeEndTime)), Restrictions.and(Restrictions.le("startTime", overTimeStartTime), Restrictions.gt("endTime", overTimeEndTime)))),
					Restrictions.or(Restrictions.or(Restrictions.and(Restrictions.ge("overTimeStartTime", overTimeStartTime), Restrictions.le("overTimeEndTime", overTimeEndTime)), Restrictions.and(Restrictions.lt("overTimeStartTime", overTimeStartTime), Restrictions.gt("overTimeEndTime", overTimeStartTime))), 
		    				Restrictions.or(Restrictions.and(Restrictions.lt("overTimeStartTime", overTimeEndTime), Restrictions.gt("overTimeEndTime", overTimeEndTime)), Restrictions.and(Restrictions.le("overTimeStartTime", overTimeStartTime), Restrictions.gt("overTimeEndTime", overTimeEndTime))))));
    		
    		List<Record> records = (List<Record>) getHibernateTemplate().findByCriteria(dc);
	    	
        	logger.debug("hasIdenticalRecordForPerson - END, records = " + records + ", size = " + records.size());    	
        	
        	if(records != null && records.size() > 0) {
        		for(Record rec : records) {
        			logger.debug("rec = " + rec);        			
        			if(rec.getStartTime() != null && rec.getEndTime() != null) {
		    			if(ControllerUtils.getInstance().hasOverlap(overTimeStartTime, overTimeEndTime, overtimeTime, rec.getStartTime(), rec.getEndTime(), rec.getTime())) {
		    				return rec;
		    			}
	    			} else if(rec.getOverTimeStartTime() != null && rec.getOverTimeEndTime() != null) {
	    				if(ControllerUtils.getInstance().hasOverlap(overTimeStartTime, overTimeEndTime, overtimeTime, rec.getOverTimeStartTime(), rec.getOverTimeEndTime(), rec.getOverTimeTime())) {
	    					return rec;
		    			}
	    			}
        			
        		}
        		return null;        	        
        	}        	        
    	}    	
    	return null;
    }      
}

