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
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Exchange;
import ro.cs.ts.entity.SearchCostSheetBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.exception.WSClientException;
import ro.cs.ts.model.dao.DaoBeanFactory;
import ro.cs.ts.model.dao.IDaoCostSheet;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.cm.CMWebServiceClient;
import ro.cs.ts.ws.client.cm.entity.WSTeamMember;
import ro.cs.ts.ws.client.om.entity.UserSimple;
import ro.cs.ts.ws.server.entity.TSReportGetDataCriteria;

/**
 * Dao class for expense entity
 * @author Coni
 *
 */
public class DaoCostSheetImpl extends HibernateDaoSupport implements IDaoCostSheet {
	

	/**
	 * Searches for cost sheets using the criterion defined in searchCostSheetBean
	 * @author Coni
	 * @param searchCostSheetBean
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException 
	 * @throws BusinessException
	 */
	public List<CostSheet> getResultsForSearch(SearchCostSheetBean searchCostSheetBean, boolean isDeleteAction) throws BusinessException {
		logger.debug("getResultsForSearch - START");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.costSheetForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.costSheetForListingEntity);
		
		//COST SHEET ID
		if (searchCostSheetBean.getId() != null) {
			dc.add(Restrictions.eq("costSheetId", searchCostSheetBean.getId()));
			dcCount.add(Restrictions.eq("costSheetId", searchCostSheetBean.getId()));
		}
		
		//ACTIVITY NAME
		if (searchCostSheetBean.getActivityName() != null && !"".equals(searchCostSheetBean.getActivityName())) {
			dc.add(Restrictions.ilike("activityName", "%".concat(searchCostSheetBean.getActivityName().concat("%"))));
			dcCount.add(Restrictions.ilike("activityName", "%".concat(searchCostSheetBean.getActivityName().concat("%"))));
		}
		
		//START DATE
		if (searchCostSheetBean.getStartDate() != null) {
			dc.add(Restrictions.ge("date", searchCostSheetBean.getStartDate()));
			dcCount.add(Restrictions.ge("date", searchCostSheetBean.getStartDate()));
		}
		
		//END DATE
		if (searchCostSheetBean.getEndDate() != null) {
			dc.add(Restrictions.le("date", searchCostSheetBean.getEndDate()));
			dcCount.add(Restrictions.le("date", searchCostSheetBean.getEndDate()));
		}
		
		//BILLABLE
		if (searchCostSheetBean.getBillable() != null) {
			if (searchCostSheetBean.getBillable().equals(IConstant.NOM_BILLABLE_YES) || searchCostSheetBean.getBillable().equals(IConstant.NOM_BILLABLE_NO)) {
				dc.add(Restrictions.eq("billable", searchCostSheetBean.getBillable()));
				dcCount.add(Restrictions.eq("billable", searchCostSheetBean.getBillable()));
			}
		}
		
		//ORGANIZATION ID
		if (searchCostSheetBean.getOrganizationId() != null) {
			dc.add(Restrictions.eq("organizationId", searchCostSheetBean.getOrganizationId()));
			dcCount.add(Restrictions.eq("organizationId", searchCostSheetBean.getOrganizationId()));
		}
		
		//STATUS
		dc.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
		dcCount.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
		
		//COST PRICE MIN
		if (searchCostSheetBean.getCostPriceMin() != null) {
			dc.add(Restrictions.ge("costPrice", searchCostSheetBean.getCostPriceMin()));
			dcCount.add(Restrictions.ge("costPrice", searchCostSheetBean.getCostPriceMin()));
		}
		
		//COST PRICE MAX
		if (searchCostSheetBean.getCostPriceMax() != null) {
			dc.add(Restrictions.le("costPrice", searchCostSheetBean.getCostPriceMax()));
			dcCount.add(Restrictions.le("costPrice", searchCostSheetBean.getCostPriceMax()));
		}
		
		//CURRENCY
		if (searchCostSheetBean.getCostPriceCurrencyId() != null && !searchCostSheetBean.getCostPriceCurrencyId().equals(-1)) {
			dc.createCriteria("costPriceCurrency").add(Restrictions.eq("currencyId", searchCostSheetBean.getCostPriceCurrencyId()));
			dcCount.createCriteria("costPriceCurrency").add(Restrictions.eq("currencyId", searchCostSheetBean.getCostPriceCurrencyId()));
		}
		
		List<TeamMember> members = null;
		List<UserSimple> users = null;
		//FIRST NAME AND LAST NAME
		if ((searchCostSheetBean.getFirstName() != null && !"".equals(searchCostSheetBean.getFirstName())) ||
				(searchCostSheetBean.getLastName() != null && !"".equals(searchCostSheetBean.getLastName()))) {
			
			List<Integer> memberIds = new ArrayList<Integer>();
			if (searchCostSheetBean.getProjectId() != IConstant.NOM_COST_SHEET_SEARCH_FOR_ORG) {
				List<TeamMember> teamMembers = BLTeamMember.getInstance().getTeamMemberFromSearch(searchCostSheetBean.getOrganizationId(),
						searchCostSheetBean.getFirstName(), searchCostSheetBean.getLastName(), false);
				if (teamMembers != null && !teamMembers.isEmpty()) {
					members = teamMembers;
					for (TeamMember teamMember : teamMembers) {
						memberIds.add(new Integer(teamMember.getMemberId()));
					}
				}
			}				
			
			List<Integer> personIds = new ArrayList<Integer>();
			//search for persons only if the selected option is to search in all or per organization,
			//otherwise only search for team members
			if (searchCostSheetBean.getProjectId() == IConstant.NOM_COST_SHEET_SEARCH_ALL || searchCostSheetBean.getProjectId() == IConstant.NOM_COST_SHEET_SEARCH_FOR_ORG) {
				List<UserSimple> persons = BLUser.getInstance().getUserSimpleFromSearch(searchCostSheetBean.getOrganizationId(), searchCostSheetBean.getFirstName(), searchCostSheetBean.getLastName(), true);

				if (persons != null && !persons.isEmpty()) {
					users = persons;
					for (UserSimple person : persons) {
						personIds.add(person.getUserId());
					}
				}
			}
			
			if (!memberIds.isEmpty() && !personIds.isEmpty()) {
				List<Integer> costSheetIds = new ArrayList<Integer>();
				
				DetachedCriteria teamMemberDc = DetachedCriteria.forEntityName(IModelConstant.costSheetWithTeamMemberDetailEntity);
				teamMemberDc.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", memberIds));
				teamMemberDc.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
				List<CostSheet> resForTeamMembers = (List<CostSheet>) getHibernateTemplate().findByCriteria(teamMemberDc);
				if (resForTeamMembers != null && !resForTeamMembers.isEmpty()) {
					for (CostSheet cost : resForTeamMembers) {
						costSheetIds.add(cost.getCostSheetId());
					}
				}
				DetachedCriteria personDc = DetachedCriteria.forEntityName(IModelConstant.costSheetWithPersonDetailEntity);
				personDc.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
				personDc.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
				List<CostSheet> resForPersons = (List<CostSheet>) getHibernateTemplate().findByCriteria(personDc);
				if (resForPersons != null && !resForPersons.isEmpty()) {
					for (CostSheet cost : resForPersons) {
						costSheetIds.add(cost.getCostSheetId());
					}
				}
				
				if (!costSheetIds.isEmpty()) {
					dc.add(Restrictions.in("costSheetId", costSheetIds));
					dcCount.add(Restrictions.in("costSheetId", costSheetIds));
				}
			} else if (!memberIds.isEmpty()) {
				dc.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", memberIds));
				dcCount.createCriteria("teamMemberDetail").add(Restrictions.in("teamMemberId", memberIds));
			} else if (!personIds.isEmpty()) {
				dc.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
				dcCount.createCriteria("personDetail").add(Restrictions.in("personId", personIds));
			} else {
				return new ArrayList<CostSheet>();
			}
		} 
		
		List<Project> projects = null;
		//PROJECT AND TEAM MEMBER
		//if no project is selected, we use a subquery to retrieve a list of cost sheet ids from which we will search for cost sheets
		//using the other search criteria (including first name and last name)
		//if a project is selected, firstName or lastName won't exist as search criteria, only teamMemberId must be taken into account 
		if (searchCostSheetBean.getTeamMemberId() == -1 && searchCostSheetBean.getProjectId() == IConstant.NOM_COST_SHEET_SEARCH_ALL) { 		//search in all the cost sheets
			
			//if the user doesn't have the permission to search in all the cost sheets, the projectId must be 
			//included in the list of ids for the projects in which the user is a team member
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetSearchAll())) {
								
				HashMap<Project, TeamMember> projectsAndTeamMembers = BLProject.getInstance().getProjectsAndTeamMembersByPerson(userAuth.getPersonId(), true, true);
				if (projectsAndTeamMembers != null && !projectsAndTeamMembers.isEmpty()) {
					projects = new ArrayList<Project>(projectsAndTeamMembers.keySet());
					List<Integer> costSheetIds = new ArrayList<Integer>();
					Iterator it = projectsAndTeamMembers.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();
						//the subquery is used to retrieve the list of cost sheet ids retrieved for the teamMemberId and projectId search criteria;
						DetachedCriteria subquery = null;
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery = DetachedCriteria.forEntityName(IModelConstant.costSheetWithProjectAndTeamMemberDetailsEntity);
						} else {
							subquery = DetachedCriteria.forEntityName(IModelConstant.costSheetWithProjectDetailsEntity);
						}
						subquery.createCriteria("projectDetails").add(Restrictions.eq("projectId", entry.getKey().getProjectId()));
						subquery.setProjection(Projections.id());
						subquery.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
						//if the user is the PM for this project, we set a restriction only on projectId,
						//otherwise the restriction is on teamMemberId
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", entry.getValue().getMemberId()));
						}
						List<Integer> costSheetIdsTemp = (List<Integer>) getHibernateTemplate().findByCriteria(subquery);
						if (costSheetIdsTemp != null) {
							for (Integer id : costSheetIdsTemp){
								costSheetIds.add(id);
							}
						}
					}

					//search for cost sheets entered by the user for the organization (no project selected)
					DetachedCriteria subquery = DetachedCriteria.forEntityName(IModelConstant.costSheetWithPersonDetailEntity);
					subquery.createCriteria("personDetail").add(Restrictions.eq("personId", userAuth.getPersonId()));
					subquery.setProjection(Projections.id());
					subquery.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
					List<Integer> costSheetIdsTemp = (List<Integer>) getHibernateTemplate().findByCriteria(subquery);
					if (costSheetIdsTemp != null) {
						for (Integer id : costSheetIdsTemp){
							costSheetIds.add(id);
						}
					}	
					
					if (!costSheetIds.isEmpty()) {
						dc.add(Restrictions.in("costSheetId", costSheetIds));
						dcCount.add(Restrictions.in("costSheetId", costSheetIds));
					} else {
						return new ArrayList<CostSheet>();
					}
				}
			}
		
		} else if (searchCostSheetBean.getTeamMemberId() == -1 && searchCostSheetBean.getProjectId() == IConstant.NOM_COST_SHEET_SEARCH_FOR_ORG) {
			//if the user doesn't have the permission to search in all the cost sheets per organization, i must set the personId property 
			//to the user correspondent personId
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetSearchAll())) {
				dc.createCriteria("personDetail").add(Restrictions.eq("personId", userAuth.getPersonId()));
				dcCount.createCriteria("personDetail").add(Restrictions.eq("personId", userAuth.getPersonId()));
			} else {
				//if the user has the permission to search in all the cost sheets for organization, i must set the condition that the personDetail property
				//mustn't be null
				dc.add(Restrictions.isNotNull("personDetail"));
				dcCount.add(Restrictions.isNotNull("personDetail"));
			}
		} else if (searchCostSheetBean.getTeamMemberId() == -1 && searchCostSheetBean.getProjectId() == IConstant.NOM_COST_SHEET_SEARCH_IN_ALL_PROJECTS) { 		//search in the cost sheets for all the projects
			
			//if the user doesn't have the permission to search in all the cost sheets, the projectId must be 
			//included in the list of ids for the projects in which the user is a team member
			if (!userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetSearchAll())) {
				HashMap<Project, TeamMember> projectsAndTeamMembers = BLProject.getInstance().getProjectsAndTeamMembersByPerson(userAuth.getPersonId(), true, true);
				if (projectsAndTeamMembers != null && !projectsAndTeamMembers.isEmpty()) {
					projects = new ArrayList<Project>(projectsAndTeamMembers.keySet());
					List<Integer> costSheetIds = new ArrayList<Integer>();
					Iterator it = projectsAndTeamMembers.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<Project, TeamMember> entry = (Map.Entry<Project, TeamMember>) it.next();
						//the subquery is used to retrieve the list of cost sheet ids retrieved for the teamMemberId and projectId search criteria;
						DetachedCriteria subquery = null;
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery = DetachedCriteria.forEntityName(IModelConstant.costSheetWithProjectAndTeamMemberDetailsEntity);
						} else {
							subquery = DetachedCriteria.forEntityName(IModelConstant.costSheetWithProjectDetailsEntity);
						}
						subquery.createCriteria("projectDetails").add(Restrictions.eq("projectId", entry.getKey().getProjectId()));
						subquery.setProjection(Projections.id());
						subquery.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
						//if the user is the PM for this project, we set a restriction only on projectId,
						//otherwise the restriction is on teamMemberId
						if (!entry.getKey().getManagerId().equals(userAuth.getPersonId())) {
							subquery.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", entry.getValue().getMemberId()));
						}
						List<Integer> costSheetIdsTemp = (List<Integer>) getHibernateTemplate().findByCriteria(subquery);
						if (costSheetIdsTemp != null) {
							for (Integer id : costSheetIdsTemp){
								costSheetIds.add(id);
							}
						}
					}
					if (!costSheetIds.isEmpty()) {
						dc.add(Restrictions.in("costSheetId", costSheetIds));
						dcCount.add(Restrictions.in("costSheetId", costSheetIds));
					} else {
						return new ArrayList<CostSheet>();
					}
				}
			} else {
				//the cost sheet must be entered for a specific project; cost sheets per organisation are excluded
				dc.add(Restrictions.isNotNull("projectDetails"));
				dcCount.add(Restrictions.isNotNull("projectDetails"));
			}
		} else if (searchCostSheetBean.getTeamMemberId() == -1 && searchCostSheetBean.getProjectId() > 0) {		//search in a project
			Project project = BLProject.getInstance().get(searchCostSheetBean.getProjectId(), true);
			if (project != null) {
				projects = new ArrayList<Project>();
				projects.add(project);
				//if the user is not PM for the selected project nor USER_ALL, it will see only its cost sheets
				if (!project.getManagerId().equals(userAuth.getPersonId()) && !userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetSearchAll())) {
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
			dc.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchCostSheetBean.getProjectId()));
			dcCount.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchCostSheetBean.getProjectId()));
		} else if (searchCostSheetBean.getTeamMemberId() != -1 && searchCostSheetBean.getProjectId() > 0) { 		//search in a project and for a specific teamMemberDetail
			//set the selected projectId and teamMemberId as search criteria
			dc.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchCostSheetBean.getProjectId()));
			dc.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", searchCostSheetBean.getTeamMemberId()));
			dcCount.createCriteria("projectDetails").add(Restrictions.eq("projectId", searchCostSheetBean.getProjectId()));
			dcCount.createCriteria("teamMemberDetail").add(Restrictions.eq("teamMemberId", searchCostSheetBean.getTeamMemberId()));
		}
		
		//the results list
		List<CostSheet> res = null;
		
		//the order can be done while retrieving the data from the database only if the sort parameter is the activity name, costSheetId, date, billable;
		//this cannot be done if the results must be ordered by cost sheet owner name or project name
		if (searchCostSheetBean.getSortParam() != null && !"".equals(searchCostSheetBean.getSortParam()) && !"costSheetReporterName".equals(searchCostSheetBean.getSortParam()) && !"projectName".equals(searchCostSheetBean.getSortParam())) {
			// check if I have to order the results
			// if I have to, check if I have to order them ascending or descending
			if (searchCostSheetBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchCostSheetBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchCostSheetBean.getSortParam()));
			}
			
			// if the request didn't come from the pagination area, 
			// it means that I have to set the number of results and pages
			if (isDeleteAction || searchCostSheetBean.getNbrOfResults() == -1){
				boolean isSearch = false;
				if (searchCostSheetBean.getNbrOfResults() == -1 ) {
					isSearch = true;
				}
				// set the count(*) restriction			
				dcCount.setProjection(Projections.countDistinct("costSheetId"));
				
				//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
				//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
				int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
				logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
				searchCostSheetBean.setNbrOfResults(nbrOfResults);
				
				// get the number of pages
				if (nbrOfResults % searchCostSheetBean.getResultsPerPage() == 0) {
					searchCostSheetBean.setNbrOfPages(nbrOfResults / searchCostSheetBean.getResultsPerPage());
				} else {
					searchCostSheetBean.setNbrOfPages(nbrOfResults / searchCostSheetBean.getResultsPerPage() + 1);
				}
				// after a cost sheet is deleted, the same page has to be displayed;
				//only when all the client from last page are deleted, the previous page will be shown 
				if (isDeleteAction && (searchCostSheetBean.getCurrentPage() > searchCostSheetBean.getNbrOfPages()) ){
					searchCostSheetBean.setCurrentPage( searchCostSheetBean.getNbrOfPages() );
				} else if ( isSearch ) {
					searchCostSheetBean.setCurrentPage(1);
				}
			}
			
			res = (List<CostSheet>) getHibernateTemplate().findByCriteria(dc, (searchCostSheetBean.getCurrentPage()-1) * searchCostSheetBean.getResultsPerPage(), searchCostSheetBean.getResultsPerPage());
		} 	

		//setting the cost sheet reporter name 
		if (res != null && res.size() > 0) {
			if (members == null) {
				HashSet<Integer> teamMemberIds = new HashSet<Integer>();
				for (CostSheet cost : res) {
					if (cost.getTeamMemberDetail() != null) {
						teamMemberIds.add(new Integer(cost.getTeamMemberDetail().getTeamMemberId()));
					}
				}
				if (teamMemberIds.size() > 0) {
					members = BLTeamMember.getInstance().getTeamMembersByMemberIds(teamMemberIds, false);
				}
			}
			if (users == null) {
				HashSet<Integer> personIds = new HashSet<Integer>();
				for (CostSheet cost : res) {
					if (cost.getPersonDetail() != null) {
						personIds.add(new Integer(cost.getPersonDetail().getPersonId()));
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
				for (CostSheet cost : res) {
					if (cost.getProjectDetails() != null) {
						projectIds.add(new Integer(cost.getProjectDetails().getProjectId()));
					}
				}
				if (projectIds.size() > 0) {
					projects = BLProject.getInstance().getProjectsSimpleByProjectIds(projectIds);
				}
			}
			
			for (CostSheet cost : res) {
				if (cost.getTeamMemberDetail() != null && members != null) {
					for (TeamMember member : members) {
						if (cost.getTeamMemberDetail().getTeamMemberId().equals(member.getMemberId())) {
							cost.setCostSheetReporterName(member.getFirstName().concat(" ").concat(member.getLastName()));
							break;
						}
					}
				} else if (cost.getPersonDetail() != null && users != null) {
					for (UserSimple user : users) {
						if (cost.getPersonDetail().getPersonId() == user.getUserId()) {
							cost.setCostSheetReporterName(user.getFirstName().concat(" ").concat(user.getLastName()));
							break;
						}
					}
				}
				
				//setting the project name
				if (cost.getProjectDetails() != null && cost.getProjectDetails().getProjectId() != null && projects != null) {
					for (Project project : projects) {						
						if (cost.getProjectDetails().getProjectId().equals(project.getProjectId())) {
							cost.setProjectName(project.getName());							
							cost.setProjectManagerId(project.getManagerId());
							break;
						}
					}
				}
			}
		}
		
		logger.debug("getResultsForSearch - END");
		return res;
	}
	
	/**
	 * Deletes a cost sheet
	 * @author Coni
	 * @param costSheetId
	 * @return
	 * @throws BusinessException
	 */
	public CostSheet delete(int costSheetId) {
		logger.debug("delete - START");
		CostSheet costSheet = getForDelete(costSheetId);
		costSheet.setStatus(IConstant.NOM_COST_SHEET_STATUS_DELETED);
		getHibernateTemplate().update(IModelConstant.costSheetSimpleEntity, costSheet);
		logger.debug("delete - END");
		return costSheet;
	}
	
	/**
	 * Returns a cost sheet
	 * 
	 * @author coni
	 */
	public CostSheet getForDelete(int costSheetId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(costSheetId)));
		CostSheet costSheet = (CostSheet) getHibernateTemplate().get(IModelConstant.costSheetSimpleEntity, costSheetId);
		logger.debug("getForDelete - START");
		return costSheet;
	}
	
	/**
	 * Adds a new cost sheet
	 * @author Coni
	 * @param costSheet
	 */
	public Integer add(CostSheet costSheet) {
		logger.debug("add - START");
		Integer costSheetId = (Integer) getHibernateTemplate().save(IModelConstant.costSheetEntity, costSheet);
		logger.debug("add - END");
		return costSheetId;
	}
	
	/**
	 * Updates an existing cost sheet
	 * @author Coni
	 * @param costSheet
	 */
	public void update(CostSheet costSheet) {
		logger.debug("update - START");
		getHibernateTemplate().update(IModelConstant.costSheetForUpdateEntity, costSheet);
		logger.debug("update - END");
	}
	
	/**
	 * Gets the cost sheet identified by the specified costSheetId
	 * @author Coni
	 * @param costSheetId
	 * @return
	 * @throws BusinessException
	 */
	public CostSheet getAll(int costSheetId) {
		logger.debug("getAll - START");
		CostSheet costSheet = (CostSheet) getHibernateTemplate().get(IModelConstant.costSheetWithAllEntity, costSheetId);
		logger.debug("getAll - END");
		return costSheet;
	}
	
	/**
	 * Gets the cost sheet identified by the specified costSheetId, for the the view
	 * 
	 * @author Adelina
	 * 
	 * @param costSheetId
	 * @return
	 */
	public CostSheet getForView(Integer costSheetId) {
		logger.debug("getForView - START");
		CostSheet costSheet = (CostSheet) getHibernateTemplate().get(IModelConstant.costSheetForListingEntity, costSheetId);
		logger.debug("getForView - END");
		return costSheet;
	}
	
	/**
	 * Returns CostSheet entities that use the Currency with the id currencyId
	 * @author Coni
	 * @param currencyId
	 * @return
	 * @throws BusinessException 
	 */
	public List<CostSheet> getByCurrencyId(int currencyId) {
		logger.debug("getByCurrencyId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.costSheetWithCurrenciesEntity);
		
		dc.add(Restrictions.or(Restrictions.eq("costPriceCurrencyId", currencyId), Restrictions.eq("billingPriceCurrencyId", currencyId)));
		dc.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
		List<CostSheet> res = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getByCurrencyId - END");
		return res;
	}
	
	/**
	 * Deletes a cost
	 * 
	 * @author Adelina
	 * 
	 * @param cost
	 * @return
	 */
	public CostSheet deleteSimple(CostSheet cost) {
		logger.debug("deleteSimple -  START");		
		logger.debug("Deleting activity with id: ".concat(String.valueOf(cost.getCostSheetId())));
		
		cost.setStatus(IConstant.NOM_COST_SHEET_STATUS_DELETED);
		
		logger.debug("Deleting the cost : " + cost);
		getHibernateTemplate().update(IModelConstant.costSheetSimpleEntity, cost);
		logger.debug("Cost " + cost + " has been deleted");
		logger.debug("deleteSimple  - END");
		return cost;
	}	
	
	/**
	 * Gets the costs for the projectDetailId
	 * 
	 * @author Adelina
	 * 
	 * @param projectDetailId
	 * @return
	 */
	public List<CostSheet> getSimpleByProjectDetailId(Integer projectDetailId) {
		logger.debug("getByProjectDetailId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.costSheetSimpleEntity);		
		
		dc.add(Restrictions.eq("projectDetailId", projectDetailId));
		dc.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
		
		List<CostSheet> res = (List<CostSheet>) getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByProjectDetailId - END");
		return res;
	}
	
	
    /**
     * Gets the Project Report cost sheets
     * @author Coni
     * @param getDataCriteria
     * @return
     * @throws WSClientException 
     * @throws IOException 
     * @throws XmlMappingException 
     * @throws BusinessException
     */
    public List<CostSheet> getProjectReportCostSheets(TSReportGetDataCriteria getDataCriteria) throws XmlMappingException, IOException, WSClientException {
    	logger.debug("getProjectReportCostSheets - START");
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.costSheetForReportsEntity);
    	
    	//PROJECT
    	Integer projectId = (Integer) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_PROJECT_ID);
    	if (projectId != null && projectId > 0) {
    		dc.createCriteria("projectDetails").add(Restrictions.eq("projectId", projectId));
    	}
    	
		//START DATE
    	if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_START_DATE) != null) {
	    	Date startDate = (Date) ((XMLGregorianCalendar) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_START_DATE)).toGregorianCalendar().getTime();
			if (startDate != null) {
				dc.add(Restrictions.ge("date", startDate));
			}
    	}
		
		//END DATE
    	if (getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_END_DATE) != null) {
	    	Date endDate = (Date) ((XMLGregorianCalendar) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_END_DATE)).toGregorianCalendar().getTime();
			if (endDate != null) {
				dc.add(Restrictions.le("date", endDate));
			}
    	}
		
		//BILLABLE
		String billable = (String) getDataCriteria.getProperties().get(IConstant.TS_PROJECT_REPORT_SEARCH_CRITERIA_BILLABLE);
		if (billable != null) {
			if (billable.equals(IConstant.NOM_BILLABLE_YES.toString()) || billable.equals(IConstant.NOM_BILLABLE_NO.toString())) {
				dc.add(Restrictions.eq("billable", billable));
			}
		}
    	
		//STATUS
		dc.add(Restrictions.ne("status", IConstant.NOM_COST_SHEET_STATUS_DELETED));
		
		List<CostSheet> result = getHibernateTemplate().findByCriteria(dc);
		
		if (result != null) {
			List<WSTeamMember> members = null;
			
			HashSet<Integer> teamMemberIds = new HashSet<Integer>();
			for (CostSheet cost : result) {
				if (cost.getTeamMemberDetail() != null) {
					teamMemberIds.add(new Integer(cost.getTeamMemberDetail().getTeamMemberId()));
				}
			}
			if (teamMemberIds.size() > 0) {
				members = CMWebServiceClient.getInstance().getTeamMembersByMemberIds(teamMemberIds, false);
			}
			
			for (CostSheet cost : result) {
				if (members != null && !members.isEmpty()) {
					for (WSTeamMember member : members) {
						if (cost.getTeamMemberDetail().getTeamMemberId().equals(member.getMemberId())) {
							cost.setCostSheetReporterName(member.getFirstName().concat(" ").concat(member.getLastName()));
						}
					}
				}
			}
		}
    	
    	logger.debug("getProjectReportCostSheets - END");
    	
    	return result;
    }
    
    /**
     * Returns the list of costs for the given projectDetailId
     *
     * @author alu
     * @param projectDetailId
     * @return
     */
    public List<CostSheet> getByProjectDetailId(Integer projectDetailId){
    	logger.debug("getByProjectId - START");
    	
    	StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.costSheetEntity).append(" where projectDetailId = ? and status <> ?");
		
		Object[] o = {projectDetailId, IConstant.NOM_COST_SHEET_STATUS_DELETED};		
		List<CostSheet> costSheetList = (List<CostSheet>) getHibernateTemplate().find(query.toString(), o);
		
		logger.debug("getByProjectId - END");
		return costSheetList;
    }
    
    /**
     * Returns the billing price of all the costs from a project in the project currency
     *
     * @author alu
     * @param projectDetailId
     * @param projectsBudgetCurrencyId
     * @return
     */
    public Float calculateCostsBillingPriceForProject(Integer projectDetailId, Integer projectsBudgetCurrencyId){
    	logger.debug("calculateCostsBillingPriceForProject - START projectDetailId: ".concat(String.valueOf(projectDetailId)));
    	
    	if (projectsBudgetCurrencyId != null) {
	    	Float totalBillingPrice = new Float(0);
	    	List<Exchange> exchangeRate = null;
	    	
	    	List<CostSheet> costs = getByProjectDetailId(projectDetailId);
	    	for (CostSheet cost: costs){
	    		
	    		// check if I need the exchange rate between budget currency and cost currency
				if ((!(projectsBudgetCurrencyId.equals(cost.getBillingPriceCurrencyId()))) && (cost.getBillingPriceCurrencyId() != null)){
					exchangeRate = DaoBeanFactory.getInstance().getDaoExchange().getProjectExchangeByCurrencies(cost.getBillingPriceCurrencyId(), projectsBudgetCurrencyId, projectDetailId);
				}
				if ((cost.getBillingPrice() != null) && (exchangeRate != null) && !exchangeRate.isEmpty() && (exchangeRate.get(0).getRate() != null)) {
					totalBillingPrice += cost.getBillingPrice() * exchangeRate.get(0).getRate();
				}
	    	}    	
	    	
	    	logger.debug("calculateCostsBillingPriceForProject - END totalBillingPrice: ".concat(String.valueOf(totalBillingPrice)));
	    	return totalBillingPrice;
    	}
    	
    	logger.debug("calculateCostsBillingPriceForProject - END - no project budget currency id");
		return new Float(0);
    }
}
