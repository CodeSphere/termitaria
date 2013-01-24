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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchDepartmentBean;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoDepartment;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.tools.Tools;

/**
 * Implements methods for model layer working with department
 * 
 * @author matti_joona
 */
public class DaoDepartmentImpl extends HibernateDaoSupport implements IDaoDepartment{

	/**
	 * Add a department
	 * 
	 * @author matti_joona
	 */
	public int add(Department dept) {
		logger.debug("add - START");
		
		getHibernateTemplate().save(IModelConstant.departmentSimpleEntity, dept);
		
		logger.debug("add - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));
		return dept.getDepartmentId();	
	}
	/**
	 * Add all data for a department
	 * 
	 * @author Adelina 
	 */
	public void addAll(Department dept){
		logger.debug("addAll - START");				
		
		getHibernateTemplate().save(IModelConstant.departmentAllEntity, dept);
		logger.debug("dept person's length = " + dept.getPersons().size());
		for(Person person: dept.getPersons()){
			logger.debug("person id = " + person.getPersonId());
		}
		
		logger.debug("addAll - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));
	}
		
	/**
	 * Add a department that had just been imported.
	 * References:
	 * Has to: Parent Department
	 * Has not to: Persons, Department Manager 
	 * 
	 * @author dan.damian
	 */
	public int addFromImport(Department dept) {
		logger.debug("addFromImport - START");
		
		getHibernateTemplate().save(IModelConstant.departmentForExpImp1Entity, dept);
		
		logger.debug("addFromImport - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));	
		return dept.getDepartmentId();	
	}
	
	
	/** 
	 * Adds Fake Department when it's Imported from an XML.
	 * References:
	 * Has to: Organization
	 * Has not to: Parent Department, Persons, Department Manager
	 * 
	 * @author dan.damian
	 */
	public int addFakeFromImport(Department dept) {
		logger.debug("addFakeFromImport - START");
		
		getHibernateTemplate().save(IModelConstant.departmentFakeFromImp1Entity, dept);
		
		logger.debug("addFakeFromImport - END of department: ".concat(String.valueOf(dept.getDepartmentId())));
		return dept.getDepartmentId();	
	}
	
	/**
	 * Update a department
	 * 
	 * @author matti_joona 
	 */
	public void update(Department dept) {
		logger.debug("update - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		IDaoPerson personDao = DaoBeanFactory.getInstance().getDaoPerson();
		Integer lista[] = new Integer[1];
		lista[0] = dept.getDepartmentId();
		//get all old persons from department
		List<Person> allPersonsDepartment = personDao.getFromDepartments(lista);
		
		Department fakeDept = getFakeForOrganisation(dept.getOrganisationId());
		Map<Person, Job> personWithJobTemp = new HashMap<Person, Job>();
		//check new structure for persons in old structure
		//if person is in old structure copy person & job
		//else add person in departement with job -1
		for (Person pers : dept.getPersons()){
			Person persTemp = personDao.getWithDepartments(pers.getPersonId());
			if (persTemp.getDeptWithJob().get(dept) != null){
				personWithJobTemp.put(pers, persTemp.getDeptWithJob().get(dept));
			} else {
				Job newJob = new Job();
				newJob.setJobId(-1);
				personWithJobTemp.put(pers, newJob);
			}
			//remove persons which are in old structure to see who was moved out of department
			allPersonsDepartment.remove(pers);
			//if person added in this depatment then remove from fake dept if needed
			if ((persTemp.getDepts() != null) && (persTemp.getDepts().contains(fakeDept))) {
				persTemp.getDepts().remove(fakeDept);
				persTemp.getDeptWithJob().remove(fakeDept);
				personDao.updateWithDepartments(persTemp);
			}
		}
		//update department
		dept.setPersonWithJob(personWithJobTemp);
		getHibernateTemplate().update(IModelConstant.departmentForUpdateEntity, dept);
		//add persons who where excluded from department to fake if needed
		if (allPersonsDepartment.size() > 0){
			for (Person pers : allPersonsDepartment){
				if (pers.getDepts().size() == 1) {
					Department newDept = new Department();
					newDept.setDepartmentId(fakeDept.getDepartmentId());
					pers.getDepts().clear();
					pers.getDepts().add(newDept);
					personDao.updateWithDepartments(pers);
				}
			}
		}
		
		
		logger.debug("update - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));		
	}
	
	/**
	 * Update a department manager id
	 * 
	 * @author Adelina
	 */
	public void updateManagerId(Department dept) {
		logger.debug("update - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentForUpdateManagerEntity, dept);
		
		logger.debug("update - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));		
	}
		
	
	/**
	 * Updates a Department that has just been imported.
	 * References:
	 * Has to: Organization, Parent Department, Persons, Department Manager
	 * 
	 * @author dan.damian
	 */
	public void updateFromImport(Department dept) {
		logger.debug("updateFromImport - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentForExpImpEntity, dept);
		
		logger.debug("updateFromImport - END of department: ".concat(String.valueOf(dept.getDepartmentId())));	
	}
	
	
	/**
	 * Update a department that had just been imported.
	 * References:
	 * Has to: Parent Department
	 * Has not to: Persons, Department Manager
	 * 
	 * @author dan.damian
	 */
	public void updateFromImport1(Department dept) {
		logger.debug("updateFromImport1 - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentForExpImp1Entity, dept);
		
		logger.debug("updateFromImport1  - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));	
	}
	
	/**
	 * Updates a Fake Department that has just been imported.
	 * References:
	 * Has to: Persons, Department Mananger
	 * Has not to: Parent Department
	 * 
	 * @author dan.damian
	 */
	public void updateFakeFromImport(Department dept) {
		logger.debug("updateFakeFromImport - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentFakeFromImp2Entity, dept);
		
		logger.debug("updateFakeFromImport - END of department: ".concat(String.valueOf(dept.getDepartmentId())));	
	}
	
	/**
	 * Update all the data for a department
	 * 
	 * @author matti_joona 
	 */
	public void updateAll(Department dept) {
		logger.debug("updateAll - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentAllEntity, dept);
		
		logger.debug("updateAll - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));	
	}
	
	/**
	 * Update a persons list.
	 * 
	 * @author matti_joona 
	 * @author Adelina
	 */
	public void updatePersons(Department dept, Set<Person> persons) {
		logger.debug("updatePersons - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		for (Person pers : persons){							
			DaoBeanFactory.getInstance().getDaoPerson().update(pers);			
		}
				
		getHibernateTemplate().update(IModelConstant.departmentWithPersonsAndJobsEntity, dept);
		
		logger.debug("updatePersons - END - of department: ".concat(String.valueOf(dept.getDepartmentId())));	
	}

	/**
	 * Update the manager of the department
	 * 
	 * @author matti_joona 
	 */
	public void updateManager(Department dept) {
		logger.debug("updateManager - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentWithManagerEntity, dept);
		
		logger.debug("updateManager - END - with manager of department: ".concat(String.valueOf(dept.getDepartmentId())));	
	}
	
	/**
	 * Update the CEO manager
	 * 
	 * @author Adelina
	 */
	public void updateCEO(Department department){
		logger.debug("updateCEO - START - of department with id ".concat(String.valueOf(department.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentSimpleEntity, department);
		
		logger.debug("updateCEO - END - with manager of department: ".concat(String.valueOf(department.getDepartmentId())));
	}
	
	/**
	 * Update the parent department of the department
	 * 
	 * @author matti_joona 
	 */
	public void updateParent(Department dept) {
		logger.debug("updateParent - START - of department with id ".concat(String.valueOf(dept.getDepartmentId())));
		
		getHibernateTemplate().update(IModelConstant.departmentWithParentEntity, dept);
		
		logger.debug("updateParent - END - with parent of department: ".concat(String.valueOf(dept.getDepartmentId())));	
	}

	
	/**
	 * Returns a department itentified by it's id.
	 * 
	 * @author matti_joona
	 */
	public Department get(Integer departmentId) {
		logger.debug("get - START - department with id = ".concat(Integer.toString(departmentId)));
		return (Department) getHibernateTemplate().get(IModelConstant.departmentSimpleEntity, new Integer(departmentId));
	}
	
	/**
	 * Returns a department(with persons) itentified by it's id.
	 * 
	 * @author Adelina
	 */
	public Department getWithPersons(Integer departmentId) {
		logger.debug("getWithPersons - START - department with id = ".concat(Integer.toString(departmentId)));
		return (Department) getHibernateTemplate().get(IModelConstant.departmentSimpleWithPersonsEntity, new Integer(departmentId));
	}
	
	/**
	 * Returns all data related to a department itentified by it's id.
	 * 
	 * @author matti_joona
	 */
	public Department getAll(Integer departmentId) {
		logger.debug("getAll - START -  with id = ".concat(Integer.toString(departmentId)));
		return (Department) getHibernateTemplate().get(IModelConstant.departmentAllEntity, departmentId);
	}
	
	/**
	 * Returns data related to a department itentified by it's id, including manager and persons list
	 * 
	 * @author dan.damian
	 */
	public Department getWithPersonsOnly(Integer departmentId) {
		logger.debug("getWithPersonsOnly - START - with id = ".concat(Integer.toString(departmentId)));
		return (Department) getHibernateTemplate().get(IModelConstant.departmentWithPersonsEntity, departmentId);
	}
	
	/**
	 * Delete a department.
	 * Deletes all the links with persons in it.
	 * 
	 * @author dan.damian 
	 */
	public Department delete(Integer departmentId) {
		logger.debug("delete - START - with id = ".concat(Integer.toString(departmentId)));
		
		Department department = getWithPersonsOnly(departmentId);		
		logger.debug("Deleting department with id: ".concat(departmentId.toString()));
		logger.debug("Department to be Deleted \n"  + department);
		getHibernateTemplate().delete(IModelConstant.departmentWithPersonsEntity, department);
		
		logger.debug("delete - END");
		return department;
	}
	
	/**
     * Returns a Department knowing it's Manager 
     * @author dd
     */
	public List<Department> getForManager(Integer managerPersonId) {
		logger.debug("getForManager - START - with id = ".concat(Integer.toString(managerPersonId)));
		
		List<Department> departments  = null;
		logger.debug("getDepartmentForManager. Manger Id: ".concat(managerPersonId.toString()));
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.departmentSimpleEntity);
		hql.append(" where managerId = :p1 and status = :p2");
		departments = (List<Department>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1", "p2"}, new Object[] {managerPersonId, IConstant.NOM_DEPARTMENT_ACTIVE});
		logger.debug(Integer.toString(departments.size()).concat(" Departments"));
		
		logger.debug("delete - END");
		return departments;
	}


	/**
     * Returns Fake Department for one Organization
     * @author dd
     */
    public Department getFakeForOrganisation(Integer organisationId) {
    	logger.debug("getFakeForOrganisation - START -  Organisation Id: ".concat(organisationId.toString()));
    	
    	List<Department> fakes  = new ArrayList<Department>();		
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.departmentSimpleEntity);
		hql.append(" where organisationId = :p1 and status = :p2");
		fakes = (List<Department>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1", "p2"}, new Object[] {organisationId, IConstant.NOM_DEPARTMENT_FAKE});
		logger.debug(Integer.toString(fakes.size()).concat(" Fakes"));
		
		logger.debug("getFakeForOrganisation - END");
		if (fakes.size() == 1) {
			return fakes.get(0);
		} else {
			return null;
		}
    }
    
    /**
	 * Searches for Departments after criterion from searchOrganisationBean.
	 * 
	 * @author alu
	 * @author dan.damian
	 */
	public List<Department> getFromSearch(SearchDepartmentBean searchDepartmentBean, boolean isDeleteAction) throws ParseException {
		logger.debug("getFromSearch - START");
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
				
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.departmentAllEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.departmentAllEntity);
		
		dc.createAlias("manager", "manager", Criteria.LEFT_JOIN);		
		dcCount.createAlias("manager", "manager", Criteria.LEFT_JOIN);		
		
		if (Tools.getInstance().stringNotEmpty(searchDepartmentBean.getName())) {
			dc.add(Restrictions.ilike("name", "%".concat(searchDepartmentBean.getName()).concat("%")));
			dcCount.add(Restrictions.ilike("name", "%".concat(searchDepartmentBean.getName()).concat("%")));
			logger.debug("name: " + searchDepartmentBean.getName());
		}	
				
		if (searchDepartmentBean.getOrganisationId() != -1) {
			dc.add(Restrictions.eq("organisation.organisationId", searchDepartmentBean.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisation.organisationId", searchDepartmentBean.getOrganisationId()));
			logger.debug("Organisation Id: " + searchDepartmentBean.getOrganisationId());
		}		
						
		if(searchDepartmentBean.getManagerFirstName() != null && !"".equals(searchDepartmentBean.getManagerFirstName())) {
			dc.add(Restrictions.eq("manager.firstName", searchDepartmentBean.getManagerFirstName()));
			dcCount.add(Restrictions.eq("manager.firstName", searchDepartmentBean.getManagerFirstName()));	
			logger.debug("Manager first name: ".concat(searchDepartmentBean.getManagerFirstName()));
		}
		
		if(searchDepartmentBean.getManagerLastName() != null && !"".equals(searchDepartmentBean.getManagerLastName())) {
			dc.add(Restrictions.eq("manager.lastName", searchDepartmentBean.getManagerLastName()));
			dcCount.add(Restrictions.eq("manager.lastName", searchDepartmentBean.getManagerLastName()));		
			logger.debug("Manager last name: ".concat(searchDepartmentBean.getManagerLastName()));
		}
								
		if ( Tools.getInstance().stringNotEmpty(searchDepartmentBean.getParentDepartmentName())) {
			dc.createCriteria("parentDepartment").
			add(Restrictions.ilike("name", "%".concat(searchDepartmentBean.getParentDepartmentName()).concat("%")));
			dcCount.createCriteria("parentDepartment").
			add(Restrictions.ilike("name", "%".concat(searchDepartmentBean.getParentDepartmentName()).concat("%")));
			logger.debug("Parent Department Name: " + searchDepartmentBean.getParentDepartmentName());
		}
		
		if (searchDepartmentBean.getParentDepartmentId() != -1) {					
			dc.add(Restrictions.eq("parentDepartment.departmentId", searchDepartmentBean.getParentDepartmentId()));
			dcCount.add(Restrictions.eq("parentDepartment.departmentId", searchDepartmentBean.getParentDepartmentId()));
			logger.debug("Parent Department Id: " + searchDepartmentBean.getParentDepartmentId());
		}	
					
		dc.add(Restrictions.eq("status", IConstant.NOM_DEPARTMENT_ACTIVE));
		dcCount.add(Restrictions.eq("status", IConstant.NOM_DEPARTMENT_ACTIVE));					
		
										
		// check if I have to order the results
		if(searchDepartmentBean.getSortParam() != null && !"".equals(searchDepartmentBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchDepartmentBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchDepartmentBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchDepartmentBean.getSortParam()));
			}
		}
								
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of result and pages
		if (isDeleteAction || searchDepartmentBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if ( searchDepartmentBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the countDistinct restriction
			dcCount.setProjection(Projections.distinct(Projections.countDistinct("departmentId")));			
			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			searchDepartmentBean.setNbrOfResults(nbrOfResults);
			logger.debug("NbrOfResults " + searchDepartmentBean.getNbrOfResults());
			logger.debug("----> searchOrganisationBean.getResults " + searchDepartmentBean.getResultsPerPage());
			// get the number of pages
			if (nbrOfResults % searchDepartmentBean.getResultsPerPage() == 0) {
				searchDepartmentBean.setNbrOfPages(nbrOfResults / searchDepartmentBean.getResultsPerPage());
			} else {
				searchDepartmentBean.setNbrOfPages(nbrOfResults / searchDepartmentBean.getResultsPerPage() + 1);
			}
			// after a department is deleted, the same page has to be displayed;
			//only when all the departments from last page are deleted, the previous page will be shown 
			if ( isDeleteAction && (searchDepartmentBean.getCurrentPage() > searchDepartmentBean.getNbrOfPages()) ){
				searchDepartmentBean.setCurrentPage( searchDepartmentBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchDepartmentBean.setCurrentPage(1);
			}
		}	
		
		List<Department> res = getHibernateTemplate().findByCriteria(dc, (searchDepartmentBean.getCurrentPage()-1) * searchDepartmentBean.getResultsPerPage(), searchDepartmentBean.getResultsPerPage());		
		
		logger.debug("Res " + res.size());
		logger.debug("getFromSearch - END - results size : ".concat(String.valueOf(res.size())));
		return res;

	}
	
	/**
	 * Gets the number of departments for an organisation (without the fake departments)
	 * 
	 * @author Adelina
	 * @parm organisationId
	 * @return number of departments from an organisation
	 */
	@SuppressWarnings("unchecked")
	public int getCountDepartments(Integer organisationId) {
		logger.debug("getCountDepartments - START");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.departmentEntity);		
		dc.setProjection(Projections.count("departmentId"));		
		dc.createCriteria("organisation").add(Restrictions.eq("organisationId", organisationId));
		dc.add(Restrictions.ne("status", (byte)2));
		List departaments = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getCountDepartments - END - ".concat(String.valueOf(departaments.size())));
		
		if(departaments != null){
			int count = ((Integer)departaments.get(0)).intValue();
			logger.debug("count " + count);
			return count;
		} else {
			return 0;
		}
	}


	/**
	 * List of persons from a specified department.
	 * 
	 * @author matti_joona 
	 */
	public Set<Person> listPersons(Integer departmentId, Integer organisationId) {
		logger.debug("listPersons - START");
		logger.debug("Getting persons from department with id: ".concat(departmentId.toString()));
		
		Set<Person> listPersons = null;
		Department dept = (Department)getHibernateTemplate().get(IModelConstant.departmentWithPersonsEntity, departmentId);
		if(dept != null){			
			logger.debug("department = " + dept);
			listPersons = dept.getPersons();
			if(dept.getPersons() != null && dept.getPersons().size() > 0) {
				listPersons = new HashSet<Person>();
				for(Person person : dept.getPersons()) {
					if(person.getStatus() != IConstant.NOM_PERSON_STATUS_DELETED) {
						listPersons.add(person);
					}
				}
			}			
			logger.debug("listPersons = " + dept.getPersons());
		} else {
			listPersons = new HashSet<Person>();
		}
		
		logger.debug("listPersons - END -");
		return listPersons;
	}


	/**
	 *  Lists all active departments within an organization. A department contains only the general information attributes.
	 *  
	 *  @author dd
	 */
	public List<Department> listByOrganisation(Integer organisationId) {
		logger.debug("listByOrganisation - START - Organisation Id: ".concat(organisationId.toString()));
		
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.departmentEntity);
		hql.append(" where organisationId = :p1 and status = :p2");
		List<Department> depts = (List<Department>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1", "p2"}, new Object[] {organisationId, IConstant.NOM_DEPARTMENT_ACTIVE});
		logger.debug(Integer.toString(depts.size()).concat(" departments "));
		
		logger.debug("listByOrganisation - END");		
		return depts;
	}


	/**
	 * Lists all Root Departments of this Organization. A department contains only the general info attributes.
	 * @param organisationId this Organization id 
	 * @author dd
	 */
	public List<Department> listRootByOrganisation(Integer organisationId) {
		logger.debug("listRootByOrganisation - START - Organisation Id: ".concat(organisationId.toString()));
		
		List<Department> roots  = new ArrayList<Department>();		
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.departmentWithOrganisationEntity);
		hql.append(" where organisationId = :p1 and status = :p2");
		roots  = (List<Department>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1", "p2"}, new Object[] {organisationId, IConstant.NOM_DEPARTMENT_FAKE});
		logger.debug(Integer.toString(roots.size()).concat(" root departments "));
		
		logger.debug("listRootByOrganisation - END");		
		return roots; 
	}


	/**
	 * List all SubDepartments of this Department 
	 * 
	 * @author dd
	 * @param departmentId this Department's id 
	 * @return
	 */
	public List<Department> listSubDepartments(Integer departmentId) {
		logger.debug("listSubDepartments - START - department Id: ".concat(departmentId.toString()));
		
		List<Department> subDepartments  = new ArrayList<Department>();		
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.departmentWithManagerEntity);
		hql.append(" where parentDepartmentId = :p1 and status = :p2");
		subDepartments = (List<Department>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1", "p2"}, new Object[] {departmentId, IConstant.NOM_DEPARTMENT_ACTIVE});
		logger.debug(Integer.toString(subDepartments.size()).concat(" SubDepartments"));
		
		logger.debug("listSubDepartments - END");
		return subDepartments; 
	}

	/**
	 * List the first level SubDepartments of this Department, each of them containing only the basic information 
	 * 
	 * @author coni
	 * @param departmentId this Department's id 
	 * @return
	 */
	public List<Department> listFirstLevelSubDepartments(Integer departmentId) {
		logger.debug("listFirstLevelSubDepartments - START - for departmentId: ".concat(departmentId.toString()));
		
		List<Department> subDepartments  = new ArrayList<Department>();		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.departmentSimpleEntity);
		dc.add(Restrictions.eq("parentDepartmentId", departmentId));
		//dc.add(Restrictions.eq("organisationId", organisationId));
		subDepartments = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("listFirstLevelSubDepartments - END - for departmentId: ".concat(departmentId.toString()));
		return subDepartments;
	}
	
	/**
	 * Lists all Potential Parent Departments for this Department.
	 * Potential Parents are found by discriminating from all Organization's Departments
	 * those that are part from this Department's subtree of departments.
	 * @author dan.damian 
	 */
	public List<Department> listPotentialParentDepartments(Integer departmentId, Integer organisationId) {
		logger.debug("listPotentialParentDepartments - START - departmentId ".concat(departmentId.toString()).concat(
				"organisation Id ").concat(organisationId.toString()));
		
		List<Department> potentialParentDepartments = new ArrayList<Department>();
		List<Integer> allSubDepartmentsIds = listAllSubDepartmentsIds(departmentId, organisationId);
		
		Tools.getInstance().printList(logger, allSubDepartmentsIds);
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.departmentSimpleEntity);		
    	
		allSubDepartmentsIds.add(departmentId);
		dc.add(Restrictions.not(Restrictions.in("departmentId", allSubDepartmentsIds)));
		
		dc.add(Restrictions.eq("organisationId", organisationId));
		dc.add(Restrictions.eq("status", IConstant.NOM_DEPARTMENT_ACTIVE));
		
		potentialParentDepartments = getHibernateTemplate().findByCriteria(dc);
		Tools.getInstance().printList(logger, potentialParentDepartments);
		
		logger.debug("listPotentialParentDepartments - END - results " + potentialParentDepartments.size());
		return potentialParentDepartments;
	}
	
	/**
	 * Returns all Sub Departments of this Departments.
	 * Returns entire Department's subtree of this Departments. 
	 * @author dan.damian 
	 * @return
	 */
	public List<Department> listAllSubDepartments(Integer departmentId) {		
		logger.debug("listAllSubDepartments - START - departmentId ".concat(departmentId.toString()));
		
		//Acumulate all SubDepartments in this list
		List<Department> allSubDepartments = new ArrayList<Department>();
		//First Level SubDepartments. From here I'll start the search.
		List<Department> firstLevelSubDepartments = listSubDepartments(departmentId);
		//Append them to total
		allSubDepartments.addAll(firstLevelSubDepartments);
		for(Department d : firstLevelSubDepartments) {
			listAllSubDepartmentsExplore(allSubDepartments, d.getDepartmentId());
		}
		
		logger.debug("listAllSubDepartments - END");
		return allSubDepartments;
	}
	
	/**
	 * Private function. Its used in listAllSubDepartments
	 * @author dan.damian 
	 */
	private void listAllSubDepartmentsExplore(List allSubDepartments, Integer departmentId) {
		logger.debug("listAllSubDepartmentsExplore - START - departmentId ".concat(departmentId.toString()));
		
		List<Department> firstLevelSubDepartments = listSubDepartments(departmentId);
		if (firstLevelSubDepartments.size() == 0) {
			 logger.debug("reached end. return.");
			 logger.debug("listAllSubDepartmentsExplore - END"); 
			 return; 
		} else {
			 allSubDepartments.addAll(firstLevelSubDepartments);
			 for(Department d : firstLevelSubDepartments) {
				 listAllSubDepartmentsExplore(allSubDepartments, d.getDepartmentId());
			 }
		}
		
		logger.debug("listAllSubDepartmentsExplore - END");
	}
	
	
	/**
	 * Private function. Its used in listPotentialParentDepartments
	 * @author dan.damian 
	 */
	private List<Integer> listAllSubDepartmentsIds(Integer departmentId, Integer organisationId) {
		logger.debug("listAllSubDepartmentsIds - START");
		
		//Acumulate all SubDepartmentsIds in this list
		List<Integer> allSubDepartmentsIds = new ArrayList<Integer>();
		//First Level SubDepartments. From here I'll start the search.
		List<Integer> firstLevelSubDepartmentsIds = listSubDepartmentsIds(departmentId, organisationId);
		//Append them to total
		allSubDepartmentsIds.addAll(firstLevelSubDepartmentsIds);
		for(Integer d : firstLevelSubDepartmentsIds) {
			listAllSubDepartmentsIdsExplore(allSubDepartmentsIds, d, organisationId);
		
		}
		logger.debug("listAllSubDepartmentsIds - END");
		return allSubDepartmentsIds;
	}
	
	/**
	 * Private function. Its used in listAllSubDepartmentsIds.
	 * 
	 * @author dan.damian 
	 */
	private void listAllSubDepartmentsIdsExplore(List<Integer> allSubDepartmentsIds, Integer departmentId, Integer organisationId) {
		logger.debug("listAllSubDepartmentsIdsExplore - START - id " + departmentId);
		
		List<Integer> firstLevelSubDepartmentsIds = listSubDepartmentsIds(departmentId, organisationId);
		if (firstLevelSubDepartmentsIds.size() == 0) {
			 logger.debug("reached end. return.");
			 return; 
		} else {
			 allSubDepartmentsIds.addAll(firstLevelSubDepartmentsIds);
			 for(Integer d : firstLevelSubDepartmentsIds) {
				 listAllSubDepartmentsIdsExplore(allSubDepartmentsIds, d, organisationId);
			 }
		}
		
		logger.debug("listAllSubDepartmentsIdsExplore - END - id " + departmentId);
	}
	
	/**
	 * Private function. Used in listAllSubDepartmentsIds
	 * @author dan.damian 
	 */
	private List<Integer> listSubDepartmentsIds(Integer departmentId, Integer organisationId) {
		logger.debug("listSubDepartments - START - Department Id: ".concat(departmentId.toString()));
		
		List<Integer> subDepartmentsIds  = new ArrayList<Integer>();		
		
		StringBuffer hql = new StringBuffer("select departmentId from ");
		hql.append(IModelConstant.departmentWithManagerEntity);
		hql.append(" where parentDepartmentId = :p1 and organisationId = :p2 and status = :p3");
		subDepartmentsIds = (List<Integer>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1", "p2", "p3"}, new Object[] {departmentId, organisationId, IConstant.NOM_DEPARTMENT_ACTIVE});
		logger.debug(Integer.toString(subDepartmentsIds.size()).concat(" SubDepartments"));
		
		logger.debug("listSubDepartments - END");
		return subDepartmentsIds; 
	}
	
	/**
	 * Return a list with the department from the given organization, without 
	 * those that belongs to a given person
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param personId
	 * @return List<Department>
	 */
	public List<Department> getDepartmentByOrganizationAndPerson(Integer organizationId, Integer personId){		
		logger.debug("getDepartmentByOrganizationAndPerson - START - organizationId:".concat(String.valueOf(organizationId)).concat("personId = ").concat(String.valueOf(personId)));		
		List<Department> depts = null;
		
		StringBuffer hql = new StringBuffer("select distinct d from ");		
		hql.append(IModelConstant.departmentSimpleWithPersonsEntity);
		hql.append(" as d where d.organisationId=");
		hql.append(organizationId);
		hql.append(" and d.status !=");
		hql.append(IConstant.NOM_DEPARTMENT_FAKE);
		hql.append(" and d not in (select d.departmentId from d as dp inner join dp.persons as p where p.personId=");
		hql.append(personId).append(")");
				
		logger.debug(hql.toString());
		depts = (List<Department>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, depts);
		
		logger.debug("getDepartmentByOrganizationAndPerson - END - size:".concat(depts != null? String.valueOf(depts.size()) : "null"));
		return depts;
	}	
}
