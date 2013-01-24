/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *  Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *   
 *  Termitaria is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Affero General Public License 
 *  as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.om.model.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Calendar;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.SearchOrganisationBean;
import ro.cs.om.entity.Setting;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.model.dao.IDaoRole;
import ro.cs.om.model.dao.IDaoSetting;
import ro.cs.om.model.dao.IDaoUserGroup;
import ro.cs.tools.Tools;

/**
 * This class implements all the model methods specifics for an organisation
 *
 * @author matti_joona
 */
public class DaoOrganisationImpl extends HibernateDaoSupport implements IDaoOrganisation{

	private static IDaoRole roleDao = DaoBeanFactory.getInstance().getDaoRole();
	private static IDaoSetting settingDao = DaoBeanFactory.getInstance().getDaoSetting();
	private static IDaoUserGroup userGroupDao = DaoBeanFactory.getInstance().getDaoUserGroup();
	
	/**
	 * Add an organisation
	 * 
	 * @author matti_joona
	 */
	
	public void add(Organisation org) {
		logger.debug("Add organisation - START");
		getHibernateTemplate().save(IModelConstant.organisationAllEntity, org);
		logger.debug("Organisation added".concat(" with id=").concat(String.valueOf(org.getOrganisationId())));
	}

	public void addOrganisationWithAll(Organisation org,Calendar calendar, Department dept, List<Setting> listSettings, UserGroup defaultUserGroup)  throws Exception{
		logger.debug("addOrganisationWithAll - START");
	   
		getHibernateTemplate().save(IModelConstant.organisationWithModulesEntity, org);		    
		//session.update(org);
		     
		//add a default calendar for organisation	
		calendar.setOrganisationId(org.getOrganisationId());
	    getHibernateTemplate().save(IModelConstant.calendarEntity, calendar);
	    	 
	    //add the fake department to organisation
	    dept.setOrganisationId(org.getOrganisationId());
	    getHibernateTemplate().save(IModelConstant.departmentSimpleEntity, dept);
	    	 
	    //add the default roles for this organisation
	    for(Module m : org.getModules()) {
	    	roleDao.addDefaultRoles(org.getOrganisationId(), m.getModuleId());
	    }  
	    	
	    //setting the theme's setting organisation
	    for(Setting setting : listSettings){
	    	setting.setOrganisation(org);
	    	getHibernateTemplate().save(IModelConstant.settingAllEntity, setting);
	    }
	    
	    //add the default user group for this organization
	    userGroupDao.addAll(defaultUserGroup);

	    logger.debug("addOrganisationWithAll - END - ".concat(" with id=").concat(String.valueOf(org.getOrganisationId())));
	    
	}

	
	/**
	 * Add an Organization containing only it's simple attributes
	 * 
	 * @author matti_joona
	 */
	public void addOnlyWithSimpleAttrs(Organisation org) {
		logger.debug("addOnlyWithSimpleAttrs - START");
		
		getHibernateTemplate().save(IModelConstant.organisationEntity, org);
		getHibernateTemplate().getSessionFactory().openSession().beginTransaction();
		
		logger.debug("addOnlyWithSimpleAttrs - END - ".concat(" with id=").concat(String.valueOf(org.getOrganisationId())));
	}
	
	/**
	 * Deletes an organization and all its children, returning a map with their names and IDs
	 * @author coni
	 */
	public Map<Integer, String> deleteOrgs(Organisation organisation) throws Exception{
		logger.debug("deleteOrgs - START - with organisationId=".concat(String.valueOf(organisation.getOrganisationId())));
		
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(organisation.getOrganisationId(), organisation.getName());
		Integer parentId = organisation.getOrganisationId();		
		//deleting the organisation
		getHibernateTemplate().delete(IModelConstant.organisationForDeleteEntity, organisation);
		
		logger.debug("deleteOrgs - END");
		return map;
	}

	/**
	 * Update an organisation
	 * 
	 * @author matti_joona
	 */
	public void update(Organisation org) {
		logger.debug("update - START - for organisation with id=".concat(String.valueOf(org.getOrganisationId())));
		
		getHibernateTemplate().update(IModelConstant.organisationEntity, org);
		
		logger.debug("update - END");
	}	
	
	/**
	 * Update all info for an organisation
	 * 
	 * @author matti_joona
	 */
	public void updateAll(Organisation org) {
		logger.debug("updateAll - START - for organisation with id=".concat(String.valueOf(org.getOrganisationId())));
		
		 //add the default roles for this organisation
	    for(Module m : org.getModules()) {
	    	logger.debug("module id " + m.getModuleId());
	    	roleDao.addDefaultRolesToOrganisation( m.getModuleId(), org.getOrganisationId());	    
	    }  
		getHibernateTemplate().update(IModelConstant.organisationAllEntity, org);
		
		logger.debug("updateAll - END");
	}	

	/**
	 * Get an organisation
	 * 
	 * @author matti_joona
	 */
	public Organisation getAll(Integer organisationId){
		logger.debug("getAll - START - with id=".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationAllEntity, new Integer(organisationId));	
		
		logger.debug("getAll - END");
		return org;
	}
	

	/**
	 * Get an organisation
	 * 
	 * @author dan.damian
	 */
	public Organisation getForExport(Integer organisationId){		
		logger.debug("getForExport - START - with id=".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationForExpImpEntity, new Integer(organisationId));	
		
		logger.debug("getForExport - END");
		return org;
	}

	/**
	 * Returns all the organisations for classified lists
	 * 
	 * @author alu
	 */
	public List<Organisation> getAllOrganisationsForNom(){
		logger.debug("getAllOrganisationsForNom - START");
		
		StringBuffer sb = new StringBuffer("from ");
		sb.append(IModelConstant.organisationForNomEntity);
		
		List<Organisation> orgs = (List<Organisation>) getHibernateTemplate().find(sb.toString());
		
		logger.debug("getAllOrganisationsForNom - END - size:".concat(orgs != null? String.valueOf(orgs.size()) : ""));		
		return orgs;
	}
	
	/**
	 * Returns all the organisations for hasAudit context map 
	 * 
	 * @author coni
	 */
	public List<Organisation> getAllOrganisationsWithModulesForNom(){
		logger.debug("getAllOrganisationsWithModulesForNom - START");
		
		StringBuffer sb = new StringBuffer("from ");
		sb.append(IModelConstant.organisationForNomEntity);
		
		List<Organisation> orgs = (List<Organisation>) getHibernateTemplate().find(sb.toString());
		
		logger.debug("getAllOrganisationsForNom - END - size:".concat(orgs != null? String.valueOf(orgs.size()) : ""));		
		return orgs;
	}
	
	/**
	 * Get the list of Organisation that has not a calendar defined
	 * 
	 * @author Adelina	
	 * @return List<Organisation>
	 */
	public List<Organisation> getAllOrganisationsForNomWithoutCalendar(){
		logger.debug("getAllOrganisationsForNomWithoutCalendar - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.organisationForNomEntity);			
		dc.addOrder(Order.asc("name"));		
		DetachedCriteria subquery = DetachedCriteria.forEntityName(IModelConstant.calendarEntity);		
		subquery.setProjection(Projections.property("organisation.organisationId"));		
		dc.add(Subqueries.propertyNotIn("organisationId", subquery));		
		
		List organisations = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getAllOrganisationsForNomWithoutCalendar - END - ".concat(String.valueOf(organisations.size())));
		return organisations;
						
	}
		
	/**
	 * Get general info about an organisation
	 * 
	 * @author matti_joona
	 */
	public Organisation get(Integer organisationId){
		logger.debug("get - START - organisation with id=".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationEntity, new Integer(organisationId));	
		
		logger.debug("get - END");
		return org;
	}
	
	/**
	 * Get all info about an organisation
	 * 
	 * @author mitziuro
	 */
	public Organisation getForDelete(Integer organisationId){
		logger.debug("getForDelete - START - organisation with id=".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationForDeleteEntity, new Integer(organisationId));	
		
		logger.debug("getForDelete - END");
		return org;
	}
	
	/**
	 * Get general info and calendar about an organisation
	 * 
	 * @author matti_joona
	 */
	public Organisation getWithCalendar(Integer organisationId){
		logger.debug("getWithCalendar - START - organisation with id=".concat(organisationId.toString()).concat(" with calendar!"));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithCalendarEntity, new Integer(organisationId));
		
		logger.debug("getWithCalendar - END");
		return org;
	}

	/**
	 * Get the 'fake'/'default' department of Organization
	 * 
	 * @author matti_joona
	 */
	public Department getDefaultDepartment(Integer organisationId){
		logger.debug("getDefaultDepartment - START - for organisation with id=".concat(organisationId.toString()));
		
		Department dept = null;
		String hquery = "from ".concat(IModelConstant.departmentEntity).concat(" where organisationId=").concat(Integer.toString(organisationId).concat(" and status=").concat(String.valueOf(IConstant.NOM_DEPARTMENT_FAKE)));
		List<Department> listDept = getHibernateTemplate().find(hquery);
		if(listDept != null && listDept.size() > 0){
			dept = listDept.get(0);	
		}
		
		logger.debug("getDefaultDepartment - END"); 
		return dept;
	}
	
	
	/**
	 * Returns the list of the 'fake'/'default' Departments for each Organization that is a child of this
	 * Parent Organization identified by parentId.
	 *
	 * @param parentId Parent Organization's Id
	 * 
	 * @author dan.damian
	 */
	public List<Department> getDefaultDepartmentsForParentId(Integer parentId){
		logger.debug("getDefaultDepartmentsForParentId - START - for Parent Organization Id=".concat(parentId.toString()));
		
		List<Department> depts = null;
		String hquery = "select d from ".concat(IModelConstant.departmentWithOrganisationEntity).concat(" as d inner join d.organisation as org where ").
			concat("org.parentId = :p1 and d.status = :p2");
		logger.debug("hql: ".concat(hquery));
		List<Department> listDept = getHibernateTemplate().findByNamedParam(hquery, 
				new String[]{"p1", "p2"}, new Object[] { parentId, IConstant.NOM_DEPARTMENT_FAKE});		
		logger.debug("return ".concat((listDept != null ? String.valueOf(listDept.size()) : "null")).concat(" fake Departments"));
		
		logger.debug("getDefaultDepartmentsForParentId - END");
		return listDept;
	}
	
	/**
	 * Get roles of an organisation
	 * 
	 * @author matti_joona
	 */
	public HashSet<Role> getWithRoles(Integer organisationId){
		logger.debug("getWithRoles - START - for organisation with id=".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithRolesEntity, organisationId);
		HashSet<Role> listRoles = null;
		if(org.getRoles() != null){
			listRoles = new HashSet<Role>();
			Iterator<Role> it = org.getRoles().iterator();
			while(it.hasNext()){
				listRoles.add(it.next());
			}
		}
		
		logger.debug("getWithRoles - END");
		return listRoles;
	}

	/**
	 * Get settings of an organisation
	 * 
	 * @author matti_joona
	 */
	public HashSet<Setting> getWithSettings(Integer organisationId){
		logger.debug("getWithSettings - START - for organisation with id=".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithSettingsEntity, organisationId);		
		HashSet<Setting> listSettings = null;
		if(org.getSettings() != null){
			listSettings = new HashSet<Setting>();
			Iterator<Setting> it = org.getSettings().iterator();
			while(it.hasNext()){
				listSettings.add(it.next());
			}
		}
		
		logger.debug("getWithSettings - END");
		return listSettings;
	}
	
	/**
	 * Returns the modules for an organization
	 * 
	 * @author alu
	 */
	public Set<Module> getWithModules(Integer organisationId){
		logger.debug("getWithModules - START - id:".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithModulesEntity, organisationId);
		
		logger.debug("getWithModules - END");
		return org.getModules();
	}
	
	/**
	 * Returns an organisation with credentials and modules
	 * 
	 * @author Adelina
	 */
	
	public Organisation getOrganisationWithModules(Integer organisationId){
		logger.debug("getOrganisationWithModules - START - id:".concat(organisationId.toString()));
		
		Organisation organisation = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithModulesEntity, new Integer(organisationId));
		
		logger.debug("getOrganisationWithModules - END");
		return organisation;
	}
		
	
	/**
	 * Get all persons from departments of an organization.
	 * 
	 * @author matti_joona
	 */
	public HashSet<Person> getWithPersons(Integer organisationId){
		logger.debug("getWithPersons - START - from organisation with id=".concat(organisationId.toString()));
		
		HashSet<Person> listPersons = null;
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithPersonsEntity, organisationId);
		logger.debug("Organisation ID: ".concat(organisationId.toString()));
		if(org != null && org.getDepartments() != null){
			Iterator<Department> itDept = org.getDepartments().iterator();
			listPersons = new HashSet<Person>();
			Department dept = null;
			Iterator<Person> itPerson = null;
			while(itDept.hasNext()){
				dept = itDept.next();
				itPerson = dept.getPersons().iterator();
				while(itPerson.hasNext()){
					listPersons.add(itPerson.next());
				}					
			}
			logger.debug("No. of persons from organisation: ".concat(String.valueOf(listPersons.size())));
		}
		
		logger.debug("getWithPersons - END"); 
		return listPersons;
	}
	
	/**
	 * Get all departments for an organisation
	 * 
	 * @author matti_joona
	 */
	public HashSet<Department> getWithDepartments(Integer organisationId){
		logger.debug("getWithDepartments - START - from organisation with id=".concat(organisationId.toString()));
		
		Organisation org = (Organisation) getHibernateTemplate().get(IModelConstant.organisationWithDepartmentsEntity, organisationId);
		HashSet<Department> listDepts = null;
		if(org.getDepartments() != null){
			Iterator<Department> itDept = org.getDepartments().iterator();
			listDepts = new HashSet<Department>();
			while(itDept.hasNext()){
				listDepts.add(itDept.next());
			}
		}
		
		logger.debug("getWithDepartments - END");
		return listDepts;
	}

	/**
	 * Get no of depts for a organisation without
	 * retrieving entities
	 * 
	 * @author matti_joona
	 */
	public int getNoOfDepartments(Integer organisationId) {
		logger.debug("getNoOfDepartments - START - for organisation with id: ".concat(String.valueOf(organisationId)));
		
		StringBuffer query = new StringBuffer();
		query.append("select count(*) from ");
		query.append(IModelConstant.departmentEntity);
		query.append(" where organisationId=");
		query.append(organisationId.toString());
		List result = getHibernateTemplate().find(query.toString());
		
		logger.debug("getNoOfDepartments - END");
		if(result != null){
			return result.size();
		} else {
			return 0;
		}
	}
	
	
	/**
	 * Returns all the organizations from the database
	 * 
	 * @author dd
	 */
	public List<Organisation> list() {
		logger.debug("list - START");
		
		StringBuffer sb = new StringBuffer("from ");
		sb.append(IModelConstant.organisationEntity);
		
		logger.debug("list - END");
		return  (List<Organisation>) getHibernateTemplate().find(sb.toString());
	}
	
	/**
	 * Searches for organisations after criterion from searchOrganisationBean.
	 * 
	 * @author alu
	 * @author dan.damian
	 * @author Adelina
	 * 
	 * @param searchOrganisationBean
	 * @param isChanged
	 * @param typeIds
	 * @return A list of log beans
	 * @throws ParseException
	 */	
	public List<Organisation> getFromSearch(SearchOrganisationBean searchOrganisationBean, boolean isChanged, Set<Byte> typeIds) throws ParseException{
		
		logger.debug("getFromSearch - START");
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.organisationEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.organisationEntity);
		
		if ( Tools.getInstance().stringNotEmpty(searchOrganisationBean.getName())) {
			dc.add(Restrictions.ilike("name", "%".concat(searchOrganisationBean.getName()).concat("%")));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchOrganisationBean.getName()).concat("%")));
			logger.debug("name: " + searchOrganisationBean.getName());
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchOrganisationBean.getAddress()) ) {
			dc.add(Restrictions.ilike("address", "%".concat(searchOrganisationBean.getAddress()).concat("%")));
			dcCount.add(Restrictions.ilike("address", "%".concat(searchOrganisationBean.getAddress()).concat("%")));
			logger.debug("lastName: " + searchOrganisationBean.getAddress());
		}				
						
		if ( Tools.getInstance().stringNotEmpty(searchOrganisationBean.getEmail()) ) {
			dc.add(Restrictions.ilike("email", "%".concat(searchOrganisationBean.getEmail()).concat("%")));
			dcCount.add(Restrictions.ilike("email", "%".concat(searchOrganisationBean.getEmail()).concat("%")));
			logger.debug("email: " + searchOrganisationBean.getEmail());
		}
			
		if (searchOrganisationBean.getType() != -1) {
			dc.add(Restrictions.eq("type", searchOrganisationBean.getType()));
			dcCount.add(Restrictions.eq("type", searchOrganisationBean.getType()));		
		} else {			
			dc.add(Restrictions.in("type", typeIds));
			dcCount.add(Restrictions.in("type", typeIds));
		}
		
		// check if I have to order the results
		if(searchOrganisationBean.getSortParam() != null && !"".equals(searchOrganisationBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchOrganisationBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchOrganisationBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchOrganisationBean.getSortParam()));
			}
		}
				
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of result and pages
		if (isChanged || searchOrganisationBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if ( searchOrganisationBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the countDistinct restriction
			dcCount.setProjection(Projections.countDistinct("organisationId"));			
			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			searchOrganisationBean.setNbrOfResults(nbrOfResults);
			logger.debug("NbrOfResults " + searchOrganisationBean.getNbrOfResults());
			logger.debug("----> searchOrganisationBean.getResults " + searchOrganisationBean.getResultsPerPage());
			// get the number of pages
			if (nbrOfResults % searchOrganisationBean.getResultsPerPage() == 0) {
				searchOrganisationBean.setNbrOfPages(nbrOfResults / searchOrganisationBean.getResultsPerPage());
			} else {
				searchOrganisationBean.setNbrOfPages(nbrOfResults / searchOrganisationBean.getResultsPerPage() + 1);
			}
			// after an organisation is deleted, the same page has to be displayed;
			//only when all the organisations from last page are deleted, the previous page will be shown 
			if ( isChanged && (searchOrganisationBean.getCurrentPage() > searchOrganisationBean.getNbrOfPages()) ){
				searchOrganisationBean.setCurrentPage( searchOrganisationBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchOrganisationBean.setCurrentPage(1);
			}
		}	
		List<Organisation> res = getHibernateTemplate().findByCriteria(dc, (searchOrganisationBean.getCurrentPage()-1) * searchOrganisationBean.getResultsPerPage(), searchOrganisationBean.getResultsPerPage());
		logger.debug("Res " + res.size());
		logger.debug("getFromSearch - END - results size : ".concat(String.valueOf(res.size())));
		return res;
	}
	
	/**
	 * Changes the status for the organisation.
	 * If the status is active = all the persons from the organisation are active
	 * If the status is inactive = all the persons from the organisation are inactive
	 * 
	 * @param Integer organisationId
	 * @param List<Person> persons
	 * @author Adelina
	 */
	
	public void updateStatus(Integer organisationId, List<Person> persons){
		logger.debug("updateStatus - START id: ".concat(String.valueOf(organisationId)));
		
		Organisation organisation = get(organisationId);	
		for(Person person : persons){
			logger.debug("person from organisation = " + organisationId + " ---- " + person.getFirstName() + " " + person.getLastName());
		}
		
		if(organisation.getStatus() == IConstant.NOM_ORGANISATION_ACTIVE){
			logger.debug("organisation status = " + organisation.getStatus());			
			organisation.setStatus(IConstant.NOM_ORGANISATION_INACTIVE);			
			for(Person person : persons){				
				person.setEnabled(IConstant.NOM_PERSON_INACTIVE);
				getHibernateTemplate().update(IModelConstant.personForListingEntity, person);
			}
		} else {
			logger.debug("organisation status = " + organisation.getStatus());
			organisation.setStatus(IConstant.NOM_ORGANISATION_ACTIVE);
			for(Person person : persons){			
					person.setEnabled(IConstant.NOM_PERSON_ACTIVE);
					getHibernateTemplate().update(IModelConstant.personForListingEntity, person);
			}
		}
		getHibernateTemplate().update(IModelConstant.organisationEntity, organisation);
		
		logger.debug("updateStatus - END");
	}

	/**
	 * 
	 * Returns an organisation with the given name
	 * @author mitziuro
	 * @param name
	 * @return
	 */
	public Organisation getOrgByName(String name){
		logger.debug("getOrgByName - START - organisation by name=".concat(name));
		
		String hquery = "from Organisation where name='".concat(name).concat("'");
		List<Organisation> orgs = getHibernateTemplate().find(hquery);
		
		logger.debug("getOrgByName - END");
		if(orgs.size() > 0){
			return orgs.get(0);	
		}
		else {
			return null;
		}
	}
	
	/**
	 * Get the list of Organisation for a parentId
	 * 
	 * @author mitziuro
	 * @return List<Organisation>
	 */
	public List<Organisation> getAllOrganisationsForParentId(Integer parentId){
		logger.debug("getAllOrganisationsForForParentId - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.organisationForDeleteEntity);			
		dc.add(Restrictions.eq("parentId", parentId));		
		List<Organisation> organisations = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getAllOrganisationsForParentId - END - ".concat(String.valueOf(parentId)));
		return organisations;
						
	}
	
	/**
	 * Get the list of Organisation for a parentId for listing
	 * 
	 * @author mitziuro
	 * @return List<Organisation>
	 */
	public List<Organisation> getAllSimpleOrganisationsForParentId(Integer parentId){
		logger.debug("getAllOrganisationsForForParentId - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.organisationEntity);			
		dc.add(Restrictions.eq("parentId", parentId));		
		List<Organisation> organisations = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getAllSimpleOrganisationsForParentId - END - ".concat(String.valueOf(parentId)));
		return organisations;
						
	}
	
	/**
	 * Get the list of Organisation for a parentId
	 * 
	 * @author coni
	 * @return List<Organisation>
	 */
	public List<Organisation> getAllOrganisationsByParentIdForOrgTree(Integer parentId){
		logger.debug("getAllOrganisationsByParentIdForOrgTree - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.organisationEntity);			
		dc.add(Restrictions.eq("parentId", parentId));		
		List<Organisation> organisations = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getAllOrganisationsByParentIdForOrgTree - END - ".concat(String.valueOf(parentId)));
		return organisations;
						
	}
	
	/**
	 * 
	 * Get all branches Id for an organisation
	 * @author mitziuro
	 * @param parentId
	 * @return
	 */
	public List<Integer> getAllBranchIdsForOrganisation(Integer parentId){
		logger.debug("getAllOrganisationsByParentIdForOrgTree - START for organisation with id:".concat(String.valueOf(parentId)));
		
		List<Integer> res = new ArrayList<Integer>();
		res.add(parentId);
		List<Organisation> branch  = getAllSimpleOrganisationsForParentId(parentId);
		
		//test if we have results
		if(branch != null){
			for(Organisation org : branch){
				res.addAll(getAllBranchIdsForOrganisation(org.getOrganisationId()));
			}
		}
		
		logger.debug("getAllBranchIdsForOrganisation - END SIZE: ".concat(String.valueOf(res.size())));
		return res;
	}	
	
	/**
	 * 
	 * Get all orgainsationIds that have a module identified by id
	 * @author mitziuro
	 * @param moduleId
	 * @return
	 */
	public List<Integer> getOrganisationIdsByModule(Integer moduleId){
		logger.debug("getOrgainsationIdsByModule - START for organisation for moduleId:".concat(String.valueOf(moduleId)));
		
		List<Integer> res = new ArrayList<Integer>();
	
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.organisationWithModulesEntity);			
		dc.createCriteria("modules").add(Restrictions.eq("moduleId", moduleId));	
		List<Organisation> organisations = getHibernateTemplate().findByCriteria(dc);

		for(Organisation organisation : organisations){
			res.add(organisation.getOrganisationId());
		}
		
		logger.debug("getOrgainsationIdsByModule - END");
		return res;
	}	
}
