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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.StringUtils;

import ro.cs.om.business.BLDepartment;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.IModelConstant;
import ro.cs.om.common.RandomDataProvider;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchPersonBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoDepartment;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.om.utils.encryption.EncryptionUtils;
import ro.cs.om.web.security.UserAuth;
import ro.cs.tools.Tools;



/**
 * Dao class for Person Entity
 * 
 * @author dd
 */
public class DaoPersonImpl extends HibernateDaoSupport implements IDaoPerson {
		
	private static IDaoDepartment departmentDao = DaoBeanFactory.getInstance().getDaoDepartment();
	
	/**
	 * Returns a person itentified by it's id. Person contains general info attributes
	 * 
	 * @author dd
	 */
	public Person get(Integer personId) {
		logger.debug("Getting person with id = ".concat(Integer.toString(personId)));
		return (Person) getHibernateTemplate().get(IModelConstant.personEntity, new Integer(personId));
	}
	
	
	/**
	 * Returns a Persons for Athorisation purposes
	 *
	 * @author dan.damian 
	 */
	public Person getForAuthorization(Integer personId) {
		logger.debug("Getting person with id = ".concat(Integer.toString(personId)));
		return (Person) getHibernateTemplate().get(IModelConstant.personForAuthorizationEntity, new Integer(personId));
	}
	
	
	/**
	 * Returns a person itentified by it's username. Person contains general info attributes.
	 * 
	 * @author dd
	 */
	@SuppressWarnings("unchecked")
	public Person getByUsername(String username) {
		logger.debug("Getting person with username = ".concat(username));
		StringBuffer query = new StringBuffer("from ");		
		query.append(IModelConstant.personEntity).append(" where username = ").append("'").append(username).append("'").append(" and status = ").append(IConstant.NOM_PERSON_STATUS_ACTIVATED);
		List<Person> persons = (List<Person>)getHibernateTemplate().find(query.toString());
		if (persons.size() > 0) {
			return persons.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns a list of Persons identified by their's user name. 
	 * Person contains general info attributes.
	 * 
	 * @author dd
	 */
	@SuppressWarnings("unchecked")
	public List<Person> getByUsername(String[] usernames, boolean isNotDeleted) {
		logger.debug("Getting persons for the following usernames = ".concat(usernames.toString()));
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.personEntity).append(" where username in (");
		if (usernames.length > 1) {
			for(int i = 0; i < usernames.length - 1; i++) {
				query.append("'").append(usernames[i]).append("', ");
			}
			query.append("'").append(usernames[usernames.length - 1]).append("'");
		} else if (usernames.length == 1){
			query.append("'").append(usernames[0]).append("'");
		}
		if(isNotDeleted) {
			query.append(")").append(" and status = ").append(IConstant.NOM_PERSON_STATUS_ACTIVATED);		
		} else {
			query.append(")");
		}
		logger.debug("Query: ".concat(query.toString()));
					
		List<Person> persons = (List<Person>)getHibernateTemplate().find(query.toString());
		
		return persons;
	}
	
	/**
	 * Returns a person itentified by it's id. Person contains general info attributes and it's roles.
	 * 
	 * @author dd
	 */
	public Person getWithRoles(Integer personId) {
		logger.debug("Getting person with id = ".concat(Integer.toString(personId)));
		return (Person) getHibernateTemplate().get(IModelConstant.personWithRolesEntity, new Integer(personId));
	}
	
	/**
	 * Returns a person identified by it's id. Person contains general info attributes and 
	 * all related entities.
	 * 
	 * @author dd
	 */
	public Person getAll(Integer personId) {
		logger.debug("Getting All person with id = ".concat(Integer.toString(personId)));
		Person person = (Person) getHibernateTemplate().get(IModelConstant.personAllEntity, new Integer(personId));
		return person;  
	}
	
	/**
	 * Returns a person identified by it's id. Person constains only the status.
	 * 
	 * @author Adelina
	 * 
	 * @param personId
	 * @return
	 */
	public Person getWithStatus(Integer personId) {
		logger.debug("getWithStatus - START");
		Person person = (Person) getHibernateTemplate().get(IModelConstant.personChageStatusEntity, new Integer(personId));
		logger.debug("getWithStatus - END , person = " + person);
		return person;
	}
			
	/**
	 * Add person.
	 * 
	 * @author dd
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	
	
	public void add(Person person) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		logger.debug("Adding Person with Departments");
		person.setPassword(EncryptionUtils.getInstance().getSHA1Hash(
					person.getPassword()));
		getHibernateTemplate().save(IModelConstant.personForAddEntity, person);
		logger.debug("New person added");
	}
	
	/**
	 * Add a Person that has just been imported.
	 * References:
	 * Has not to: Departments
	 * 
	 * @author dan.damian
	 */
	public void addFromImport(Person person) {
		logger.debug("Adding Person (ForExportImport)");
		getHibernateTemplate().save(IModelConstant.personForExpImp1Entity, person);
		logger.debug("New person added");
	}
	
	/**
	 * Update person.
	 * 
	 * @author dd
	 */
	public void updateSimple(Person person) {
		logger.debug("Updating person");
		logger.debug("BirthDate: ".concat(person.getBirthDate().toString()));
		getHibernateTemplate().update(IModelConstant.personSimpleEntity, person);
		logger.debug("Person updated");
	}
	
	/**
	 * Update person's credentials.
	 * 
	 * @author dd
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void updateCredentials(Person person) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		logger.debug("Updating person's credentials");		
		if (person.getPassword() != null && person.getPassword().equals(person.getPasswordConfirm())) {
			person.setPassword(EncryptionUtils.getInstance().getSHA1Hash(person.getPassword()));
		}
		getHibernateTemplate().update(IModelConstant.personWithCredentialsEntity, person);
		logger.debug("Person updated");
	}

	/**
	 * Update person's credentials by reseting the password
	 * @author Adelina
	 * @param Person
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public String resetPassword(Person person) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String newPassword = null;
		logger.debug("resetPassword DAO IMPL - START");
		logger.debug("Person id :".concat(Integer.toString(person.getPersonId())));
		if(person.getPassword() != null){
			logger.debug("old password = ".concat(person.getPassword()));
			newPassword = RandomDataProvider.getInstance().generatePassword();
			logger.debug("new password = ".concat(newPassword));
			person.setPassword(EncryptionUtils.getInstance().getSHA1Hash(newPassword));
			logger.debug("crypted password = ".concat(person.getPassword()));
		}
		getHibernateTemplate().update(IModelConstant.personWithCredentialsEntity, person);
		logger.debug("after update crypted password = ".concat(person.getPassword()));
		logger.debug("resetPassword DAO IMPL - END");
		return newPassword;
	}
	
	/**
	 * Update person. Person with roles.
	 * 
	 * @author dd
	 */
	public void updateWithRoles(Person person) {
		logger.debug("Updating Person with Roles");
		getHibernateTemplate().update(IModelConstant.personWithRolesEntity, person);
		logger.debug("Person updated");
	}

	/**
	 * Update person. Person with departments.
	 * 
	 * @author dd
	 */
	public void updateWithDepartments(Person person) {
		logger.debug("Updating Person with Departments");
		getHibernateTemplate().update(IModelConstant.personWithDepartmentsEntity, person);
		logger.debug("Person updated");
	}
	
	/**
	 * Update person.
	 * 
	 * @author dd
	 */
	public void update(Person person) {
		logger.debug("Updating person");
		logger.debug("Roles : ");
/*		if(person.getRoles().size() > 0){
			logger.debug(person.getRoles().size());
			for(Role role : person.getRoles()){
				logger.debug(" - " + role.getName());		
			}
		} else {
			person.setRoles(new HashSet<Role>());
		}
**/		
	
		getHibernateTemplate().update(IModelConstant.personForUpdateEntity, person);
		logger.debug("Person updated");
	}
	
	/**
	 * Delete person identified by it's id.
     *
     * Returns the Person that has been deleted.
     * 
     * @author Adelina
	 * @author dd
	 */
	public Person delete(Integer personId, Integer organizationId) {
		
		logger.debug("Deleting person with id: ".concat(String.valueOf(personId)));
		logger.debug(personId);
		if (personId instanceof Integer) {
			logger.debug("Is Integer");
		} else {
			personId.getClass();
		}
		Person person = getAll(personId);		
		person.setStatus(IConstant.NOM_PERSON_STATUS_DELETED);
		Set<Department> depts = new HashSet<Department>();		
		depts.add(departmentDao.getFakeForOrganisation(organizationId));
		person.setDepts(depts);			
		person.setRoles(null);
		person.setUserGroups(null);
								
		logger.debug("Deleting the person : ".concat(String.valueOf(personId)));
		logger.debug("Person ==> " + person);
		getHibernateTemplate().update(IModelConstant.personAllEntity, person);
										
		logger.debug("Person " + person + " has just been deleted");
		return person;		
	}
		

	/**
	 * Returns all persons in the data base. 
	 * all related entities.
	 * 
	 * @author dd
	 */
	@SuppressWarnings("unchecked")
	public List<Person> list(){
		logger.debug("List all persons");
		// set criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personEntity);
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_STATUS_DELETED));
		List<Person> persons = getHibernateTemplate().findByCriteria(dc);			
		logger.debug(Integer.toString(persons.size()).concat(" persons"));		
		return persons;
	}

	/**
	 *  Returns a person itentified by it's id. Person contains general info attributes and it's departments.
	 * 
	 * @author dd
	 */
	public Person getWithDepartments(Integer personId) {
		logger.debug("Getting person with id = ".concat(Integer.toString(personId)));
		return (Person) getHibernateTemplate().get(IModelConstant.personWithDepartmentsEntity, new Integer(personId));
	}

	
	/**
	 * Searches for persons after criterion from searchPersonBean.
	 * 
	 * @author alu
	 * 
	 * @return A list of log beans 
	 * @throws ParseException 
	 */
	public List<Person> getFromSearch(SearchPersonBean searchPersonBean, boolean isDeleteAction, List<Integer> orgs) throws ParseException{
		logger.debug("getFromSearch - START");
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personForListingEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.personForListingEntity);
		
		// Status
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_STATUS_DELETED));
		dcCount.add(Restrictions.ne("status", IConstant.NOM_PERSON_STATUS_DELETED));
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getFirstName()) ) {
			dc.add(Restrictions.ilike("firstName", "%".concat(searchPersonBean.getFirstName()).concat("%")));
			dcCount.add(Restrictions.ilike("firstName", "%".concat(searchPersonBean.getFirstName()).concat("%")));			
			logger.debug("firstName: " + searchPersonBean.getFirstName());
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getLastName()) ) {
			dc.add(Restrictions.ilike("lastName", "%".concat(searchPersonBean.getLastName()).concat("%")));
			dcCount.add(Restrictions.ilike("lastName", "%".concat(searchPersonBean.getLastName()).concat("%")));
			logger.debug("lastName: " + searchPersonBean.getLastName());
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getUsername()) ) {
			dc.add(Restrictions.ilike("username", "%".concat(searchPersonBean.getUsername()).concat("%")));
			dcCount.add(Restrictions.ilike("username", "%".concat(searchPersonBean.getUsername()).concat("%")));
			logger.debug("username: " + searchPersonBean.getUsername());
		}
		
		if (searchPersonBean.getDepartmentId() > 0){
			dc.createCriteria("depts").add(Restrictions.eq("departmentId", new Integer(searchPersonBean.getDepartmentId())));
			dcCount.createCriteria("depts").add(Restrictions.eq("departmentId", new Integer(searchPersonBean.getDepartmentId())));
			logger.debug("departmentId: " + searchPersonBean.getDepartmentId());
		
		} else if (searchPersonBean.getOrganisationId() > 0){
			 	//if the search is in all branches we use it
				if(orgs != null){ 
					dc.createCriteria("depts").add(Restrictions.in("organisationId", orgs));
					dcCount.createCriteria("depts").add(Restrictions.in("organisationId", orgs));
					logger.debug("number of branches: " + orgs.size());
				} else {
					dc.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(searchPersonBean.getOrganisationId())));
					dcCount.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(searchPersonBean.getOrganisationId())));
					logger.debug("organisationId: " + searchPersonBean.getOrganisationId());
				}
			
		}
						
		if (searchPersonBean.getSex() != null){
			if(searchPersonBean.getSex().equals(IConstant.NOM_PERSON_SEX_F) || searchPersonBean.getSex().equals(IConstant.NOM_PERSON_SEX_M)) {
				dc.add(Restrictions.eq("sex", searchPersonBean.getSex()));
				dcCount.add(Restrictions.eq("sex", searchPersonBean.getSex()));
				logger.debug("sex: " + searchPersonBean.getSex());
			}			
		}
					
		dc.setProjection(Projections.id());
		dcCount.setProjection(Projections.id());
		// until here, I've created the subquery
		// now, it's time to retrive all the persons that are in the list of the subquery
		DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.personForListingEntity);
		dc1.add(Subqueries.propertyIn("personId", dc));
		
		
		// check if I have to order the results
		if(searchPersonBean.getSortParam() != null && StringUtils.hasLength(searchPersonBean.getSortParam()) ) {			
			// if I have to, check if I have to order them ascending or descending
			if (searchPersonBean.getSortDirection() == IConstant.ASCENDING ) {
				// ascending
				dc1.addOrder(Order.asc(searchPersonBean.getSortParam()));
			} else {
				// descending
				dc1.addOrder(Order.desc(searchPersonBean.getSortParam()));
			}
		}
	
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of result and pages
		if (isDeleteAction || searchPersonBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if ( searchPersonBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the countDistinct restriction
			dcCount.setProjection(Projections.countDistinct("personId"));			
			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			searchPersonBean.setNbrOfResults(nbrOfResults);
			logger.debug("NbrOfResults " + searchPersonBean.getNbrOfResults());
			logger.debug("----> searchPersonBean.getResults " + searchPersonBean.getResultsPerPage());
			// get the number of pages
			if (nbrOfResults % searchPersonBean.getResultsPerPage() == 0) {
				searchPersonBean.setNbrOfPages(nbrOfResults / searchPersonBean.getResultsPerPage());
			} else {
				searchPersonBean.setNbrOfPages(nbrOfResults / searchPersonBean.getResultsPerPage() + 1);
			}
			// after a person is deleted, the same page has to be displayed;
			//only when all the persons from last page are deleted, the previous page will be shown 
			if ( isDeleteAction && (searchPersonBean.getCurrentPage() > searchPersonBean.getNbrOfPages()) ){
				searchPersonBean.setCurrentPage( searchPersonBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchPersonBean.setCurrentPage(1);
			}
		}
		
		List<Person> res = getHibernateTemplate().findByCriteria(dc1, (searchPersonBean.getCurrentPage()-1) * searchPersonBean.getResultsPerPage(), searchPersonBean.getResultsPerPage());
		
		logger.debug("Res " + res.size());
		logger.debug("getFromSearch - END - results size : ".concat(String.valueOf(res.size())));
		return res;
	}
	
	
	/**
	 * Returns a Person being Manager of this Department
	 * @author dd 
	 * @param departmentId this Department id
	 * @return a Person
	 */
	public Person getManagerOfDepartment(Integer departmentId) {
		List<Department> departments  = new ArrayList<Department>();
		logger.debug("getManagerOfDepartment. Department Id: ".concat(departmentId.toString()));
		StringBuffer hql = new StringBuffer("from ");
		hql.append(IModelConstant.departmentWithManagerEntity);
		hql.append(" where departmentId = :p1");
		logger.debug(hql.toString());
		departments = (List<Department>)getHibernateTemplate().
			findByNamedParam(hql.toString(),
				new String[]{"p1"}, new Object[] {departmentId});
		logger.debug(Integer.toString(departments.size()).concat(" Managers"));
		if (departments != null && departments.size() >=1) {
			return departments.get(0).getManager();
		} else {
			return null;
		}
	}

	
	/**
	 * Returns a list of Persons within those Departments.
	 * @author dd
	 */
	public List<Person> getFromDepartments(Integer[] departmentIds) {
		List<Person> persons  = new ArrayList<Person>();
		StringBuffer forLogging = new StringBuffer("getFromDepartments. Department Ids( ");
		for(int i =0; i < departmentIds.length - 1; i++) {
			forLogging.append(departmentIds[i]).append(",");
		}
		forLogging.append(departmentIds[departmentIds.length - 1]);
		forLogging.append(")");
		logger.debug(forLogging);
		StringBuffer hql = new StringBuffer("select distinct(p) from ");
		hql.append(IModelConstant.personWithDepartmentsEntity);
		hql.append(" as p inner join p.depts as d where d.status = ");
		hql.append(IConstant.NOM_DEPARTMENT_ACTIVE);
		hql.append(" and d.departmentId in (");
		for(int i =0; i < departmentIds.length - 1; i++) {
			hql.append(departmentIds[i]).append(",");
		}
		hql.append(departmentIds[departmentIds.length - 1]);
		hql.append(")");
		hql.append(" and p.status != ");
		hql.append(IConstant.NOM_PERSON_STATUS_DELETED);
		logger.debug(hql.toString());
		persons = (List<Person>)getHibernateTemplate().find(hql.toString());
		logger.debug(Integer.toString(persons.size()).concat(" Persons"));
		return persons;
	}

	/**
	 * It is used in OMUserDetails - a Spring Security Component
	 * 
	 * @author dan.damian
	 * 
	 * @param username
	 * @return
	 */
	public UserAuth getUserAuthByUsername(String username) {
		logger.debug("Getting OMUserDetails with username = ".concat(username));
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.personForUserDetailsEntity).append(" where username = ").append("'").append(username).append("'").append(" and status != ").append(IConstant.NOM_PERSON_STATUS_DELETED);
		List<UserAuth> persons = (List<UserAuth>) getHibernateTemplate().find(query.toString());
		if (persons != null && persons.size() > 0) {
			return persons.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns a UserAuth by it's personId
	 * 
	 * @author dan.damian
	 * 
	 * @param username
	 * @return
	 */
	public UserAuth getUserAuthByPersonId(int personId) {
		logger.debug("getUserAuthByPersonId with personId = ".concat(String.valueOf(personId)));
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.personForUserDetailsEntity).append(" where personId = ").append(new Integer(personId)).append(" and status != ").append(IConstant.NOM_PERSON_STATUS_DELETED);
		List<UserAuth> persons = (List<UserAuth>) getHibernateTemplate().find(query.toString());
		if (persons.size() > 0) {
			return persons.get(0);
		} else {
			return null;
		}
		
	}
	
	/**
	 * Updates the UserAuth attributes form Person
	 * 
	 * @author dan.damian
	 * 
	 * @param username
	 * @return
	 */
	public void updateUserAuth(UserAuth userAuth) {
		logger.debug("Updating UserAuth");
		getHibernateTemplate().update(IModelConstant.personForUserDetailsEntity, userAuth);
		logger.debug("UserAuth updated");
		
	}
	
	/**
	 * Return a list with all the persons from the given organisation
	 * 
	 * @author alu
	 */
	public List<Person> getPersonsByOrganizationId(int organizationId){
		logger.debug("getPersonsByOrganizationId - START - organizationId:".concat(String.valueOf(organizationId)));
		List<Person> persons = null;
		
		StringBuffer hql = new StringBuffer("select distinct p from ");
		hql.append(IModelConstant.personForListingEntity);
		hql.append(" as p inner join p.depts as d where d.organisationId=");
		hql.append(organizationId);
		hql.append(" and p.status != ");
		hql.append(IConstant.NOM_PERSON_STATUS_DELETED);
		
		logger.debug(hql.toString());
		persons = (List<Person>)getHibernateTemplate().find(hql.toString());
		
		logger.debug("getPersonsByOrganizationId - END - size:".concat(persons != null? String.valueOf(persons.size()) : "null"));
		return persons;
	}
	
	
	/**
	 * Returns a list with all the persons from the given organisation that are active( without those that are deleted, and those that are disabled)
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param departmentId
	 * @return
	 */
	public List<Person> getPersonsActivated(Integer organizationId){
		
		logger.debug("getPersonsActivated - START - organizationId:".concat(String.valueOf(organizationId)));
		List<Person> persons = null;
		
		StringBuffer hql = new StringBuffer("select distinct p from ");
		hql.append(IModelConstant.personForListingEntity);
		hql.append(" as p inner join p.depts as d where d.organisationId=");
		hql.append(organizationId);
		hql.append(" and p.status = ");
		hql.append(IConstant.NOM_PERSON_STATUS_ACTIVATED);
				
		logger.debug(hql.toString());
		persons = (List<Person>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, persons);			
		
		logger.debug("getPersonsActivated - END - size:".concat(persons != null? String.valueOf(persons.size()) : "null"));
		return persons;
	}	
	
	
	
	/**
	 * Return a list with all the Persons from the given Organization
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<Person> getPersonsSimpleByOrganizationId(int organizationId, boolean isNotDeleted){
		logger.debug("getPersonsFromOrganization START: organizationId = ".concat(String.valueOf(organizationId)));
		System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFF "+ isNotDeleted);
		List<Person> persons = null;		
		StringBuffer hql = new StringBuffer("select distinct p from ");
		hql.append(IModelConstant.personFromOrganisationEntity);
		
		if(isNotDeleted) {
			hql.append(" as p inner join p.depts as d where d.organisationId = :p1 and p.status = :p2");
					
			logger.debug(hql.toString());
			persons = (List<Person>)getHibernateTemplate().findByNamedParam(
					hql.toString(), new String[] {"p1", "p2"}, new Object[] {organizationId, IConstant.NOM_PERSON_STATUS_ACTIVATED});
		} else {
			hql.append(" as p inner join p.depts as d where d.organisationId = :p1");
			
			logger.debug(hql.toString());
			persons = (List<Person>)getHibernateTemplate().findByNamedParam(
					hql.toString(), new String[] {"p1"}, new Object[] {organizationId});
		}
		logger.debug("getPersonsFromOrganization END: size = ".concat(persons != null? String.valueOf(persons.size()) : "null"));
		return persons;
	}
	/**
	 * Return a list with all the Persons from the given Organization
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @return
	 
	public List<Person> getDeactivatedPersonsSimpleByOrganizationId(int organizationId){
		logger.debug("getDeactivatedPersonsFromOrganization START: organizationId = ".concat(String.valueOf(organizationId)));
		//System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFF "+ isNotDeleted);
		List<Person> persons = null;		
		StringBuffer hql = new StringBuffer("select distinct p from ");
		hql.append(IModelConstant.personFromOrganisationEntity);
		
		//if(isNotDeleted) {
			hql.append(" as p inner join p.depts as d where d.organisationId = :p1 and p.status = :p2");
					
			logger.debug(hql.toString());
			persons = (List<Person>)getHibernateTemplate().findByNamedParam(
					hql.toString(), new String[] {"p1", "p2"}, new Object[] {organizationId, IConstant.NOM_PERSON_STATUS_DELETED});
		//} else {
			//hql.append(" as p inner join p.depts as d where d.organisationId = :p1");
			
			logger.debug(hql.toString());
		//	persons = (List<Person>)getHibernateTemplate().findByNamedParam(
		//			hql.toString(), new String[] {"p1"}, new Object[] {organizationId});
		//}/
		logger.debug("getDeactivatedPersonsFromOrganization END: size = ".concat(persons != null? String.valueOf(persons.size()) : "null"));
		return persons;
	}
	*/
	
	/**
	 * Return a list with all the Persons from the given Organization and Module and all the persons from organization
	 * 
	 * @author mitziuro
	 * 
	 * @param organizationId
	 * @param moduleId
	 * @return
	 */
	public Entry<List<Integer>, List<Person>> getPersonsIdsByOrganizationIdAndModuleId(int organizationId, int moduleId){
		logger.debug("getPersonsIdsByOrganizationIdAndModuleId START: organizationId = ".concat(String.valueOf(organizationId).concat(" moduleId = ").concat(String.valueOf(moduleId))));
		
		List<Integer> persons = null;		
		List<Person> allPersons = null;
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personForListingEntity);
		//we search for organisation id
		
		dc.createCriteria("depts").setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).add(Restrictions.eq("organisationId", new Integer(organizationId)));
		
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_STATUS_DELETED));
		
		//we search
		allPersons = getHibernateTemplate().findByCriteria(dc);
		
		DetachedCriteria dcp = DetachedCriteria.forEntityName(IModelConstant.personForListingEntity);
		//we search for organisation id
		dcp.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(organizationId)));
		dcp.add(Restrictions.ne("status", IConstant.NOM_PERSON_STATUS_DELETED));
		
		
		//distinct results
		dcp.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("personId"), "personId")));
		//we search for module id
		dcp.createCriteria("roles").add(Restrictions.eq("moduleId", new Integer(moduleId)));
		
		
		//we search
		persons = getHibernateTemplate().findByCriteria(dcp);
		
		logger.debug("getPersonsIdsByOrganizationIdAndModuleId END: size = ".concat(persons != null? String.valueOf(allPersons.size()) : "null"));
		
		return new SimpleEntry(persons, allPersons);
	}
	
	
	/**
	 * Return a list with all the deactivated Persons from the given Organization and Module and all the persons from organization
	 * 
	 * @author liviu
	 * 
	 * @param organizationId
	 * @param moduleId
	 * @return
	 */
	public Entry<List<Integer>, List<Person>> getDeletedPersonsIdsByOrganizationIdAndModuleId(int organizationId, int moduleId){
		logger.debug("getDeletedPersonsIdsByOrganizationIdAndModuleId START: organizationId = ".concat(String.valueOf(organizationId).concat(" moduleId = ").concat(String.valueOf(moduleId))));
		
		List<Integer> persons = null;		
		List<Person> allPersons = null;
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personForListingEntity);
		//we search for organisation id
		dc.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(organizationId)));
		dc.add(Restrictions.eq("status", IConstant.NOM_PERSON_STATUS_DELETED));
		
		//we search
		allPersons = getHibernateTemplate().findByCriteria(dc);
		
		//we search for module id
		dc.createCriteria("roles").add(Restrictions.eq("moduleId", new Integer(moduleId)));
		
		//distinct results
		dc.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("personId"), "personId")));
		
		//we search
		persons = getHibernateTemplate().findByCriteria(dc);
		
		logger.debug("getDeletedPersonsIdsByOrganizationIdAndModuleId END: size = ".concat(persons != null? String.valueOf(persons.size()) : "null"));
		
		return new SimpleEntry(persons, allPersons);
	}
	
	/**
	 * Return the number of persons from the given organisation
	 * 
	 * @author Adelina
	 */
	@SuppressWarnings("unchecked")
	public long getPersonsCountByOrganization(Integer organizationId){		
		logger.debug("getPersonsCountByOrganization DAO IMPL - START - ");
		
		List persons = null;
		long count = 0;
		
		StringBuilder hql = new StringBuilder("select count(distinct p.personId) from ");
		hql.append(IModelConstant.personForListingEntity);
		hql.append(" as p inner join p.depts as d where d.organisationId = ");	
		hql.append(organizationId);
		hql.append(" and p.status != ");
		hql.append(IConstant.NOM_PERSON_STATUS_DELETED);
		
		logger.debug(hql.toString());
		persons = getHibernateTemplate().find(hql.toString());
		
		logger.debug("getPersonsCountByOrganization DAO IMPL - END - ".concat(String.valueOf(persons.size())));
		
		if(persons != null){
			count = ((Long)persons.get(0)).longValue();			
			return count;
		} else {
			return 0;
		}		
	}
	
	/** 
	 * Checks if a given organisation has an admin
	 * 
	 * @author Adelina
	 */
	@SuppressWarnings("unchecked")	
	public boolean hasAdminByOrganisation(Integer organizationId){
		logger.debug("hasAdminByOrganisation DAO IMPL - START - ");
		
		List persons = null;	
		boolean hasAdmin = false;
		
		StringBuilder hql = new StringBuilder("select r.roleId from ");
		hql.append(IModelConstant.roleForAdminEntity);
		hql.append(" as r inner join r.persons as pr where r.name=".concat("'").concat(IConstant.OM_ADMIN).concat("'").concat(" and r.organisation.organisationId="));		
		hql.append(organizationId);			
		
		logger.debug(hql.toString());
		persons = getHibernateTemplate().find(hql.toString());
		
		logger.debug("hasAdminByOrganisation DAO IMPL - END - ".concat(String.valueOf(persons.size())));
		
		if(persons != null && persons.size() != 0){
			hasAdmin = true;
		} else {
			hasAdmin = false;
		}
		return hasAdmin;		
	}
	
	/**
	 * Return a list with the persons from the given organization, without 
	 * those that are in the given user group
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param userGroupId
	 * @return
	 */
	public List<Person> getPersonsByOrganizationAndUserGroup(int organizationId, int userGroupId){
		
		logger.debug("getPersonsByOrganizationAndUserGroup - START - organizationId:".concat(String.valueOf(organizationId)).concat("usergroupId = ").concat(String.valueOf(userGroupId)));
		List<Person> persons = null;
		
		StringBuffer hql = new StringBuffer("select distinct p from ");		
		hql.append(IModelConstant.personAllEntity);
		hql.append(" as p inner join p.depts as d where d.organisationId=");
		hql.append(organizationId);
		hql.append(" and p not in (select pu.personId from p as pu inner join pu.userGroups as u where u.userGroupId=");
		hql.append(userGroupId).append(")");
		hql.append(" and p.status != ");
		hql.append(IConstant.NOM_PERSON_STATUS_DELETED);
		
		logger.debug(hql.toString());
		persons = (List<Person>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, persons);
		
		logger.debug("getPersonsByOrganizationAndUserGroup - END - size:".concat(persons != null? String.valueOf(persons.size()) : "null"));
		return persons;
	}
	
	/**
	 * Return a list of persons that belongs to a given department, 
	 * and not other departments
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param departmentId
	 * @return List<Person>
	 */
	public List<Person> getPersonByOnlyOneDepartment(Integer organisationId, Integer departmentId) {
		
		logger.debug("getPersonByOnlyOneDepartment - START - departmentId:".concat(String.valueOf(departmentId)));
		List<Person> persons = null;
		
		StringBuffer hql = new StringBuffer("select distinct p from ");
		hql.append(IModelConstant.personAllEntity);
		hql.append(" as p inner join p.depts as d where d.departmentId=");
		hql.append(departmentId);
		hql.append(" and p not in (select pu.personId from p as pu inner join pu.depts as pd where pd.departmentId !=");
		hql.append(departmentId).append(")");
		hql.append(" and p.status != ");
		hql.append(IConstant.NOM_PERSON_STATUS_DELETED);
	
		logger.debug(hql.toString());
		persons = (List<Person>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, persons);
		
		Department fake = new Department();
		try {
			fake = BLDepartment.getInstance().getFakeForOrganization(organisationId);
		} catch (BusinessException e) {		
			e.printStackTrace();
		}
		logger.debug("fake = " + fake);
		Set<Department> depts = new HashSet<Department>();
		depts.add(fake);
		
		for(Person person: persons) {
			person.setDepts(depts);
			getHibernateTemplate().update(IModelConstant.personWithDepartmentsEntity, person);
		}
						
		logger.debug("getPersonByOnlyOneDepartment - END - size:".concat(persons != null? String.valueOf(persons.size()) : "null"));
		return persons;
	}
	
	
	/**
	 * Return a list with the persons from the given organization, without 
	 * those that are in the given department
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param departmentId
	 * @return
	 */
	public List<Person> getPersonsByOrganizationAndDepartment(Integer organizationId, Integer departmentId){
		
		logger.debug("getPersonsByOrganizationAndDepartment - START - organizationId:".concat(String.valueOf(organizationId)).concat("departmentId = ").concat(String.valueOf(departmentId)));
		List<Person> persons = null;
		
		StringBuffer hql = new StringBuffer("select distinct p from ");		
		hql.append(IModelConstant.personForListingEntity);
		hql.append(" as p inner join p.depts as d where d.organisationId=");
		hql.append(organizationId);
		hql.append(" and p not in (select p.personId from p as pd inner join pd.depts as d where d.departmentId=");
		hql.append(departmentId).append(")");
		hql.append(" and p.status != ");
		hql.append(IConstant.NOM_PERSON_STATUS_DELETED);
				
		logger.debug(hql.toString());
		persons = (List<Person>)getHibernateTemplate().find(hql.toString());
		Tools.getInstance().printList(logger, persons);			
		
		logger.debug("getPersonsByOrganizationAndUserGroup - END - size:".concat(persons != null? String.valueOf(persons.size()) : "null"));
		return persons;
	}			
	
	/**
	 * Returns a person itentified by it's id. Person contains general info attributes and picture
	 * 
	 * @author Adelina
	 *
	 * @param personId
	 * @return
	 */
	public Person getWithPicture(Integer personId) {
		logger.debug("Getting person with id = ".concat(Integer.toString(personId)));
		return (Person) getHibernateTemplate().get(IModelConstant.personWithPictureEntity, new Integer(personId));
	}
		
	/**
	 * Return a list of person from a given organization,
	 * that has a certain role
	 * 
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param roleName
	 * @return
	 */
	public List<Person> getPersonsWithRoleByOrganisation(Integer organisationId, String roleName) {
		logger.debug("getPersonsWithRoleByOrganisation - START: organizationId = ".concat(String.valueOf(organisationId)));
		
		List<Person> persons = null;
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personForListingEntity);
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_STATUS_DELETED));
		
		// we search for the organizationId
		dc.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(organisationId)));
		
		// and roleName
		dc.createCriteria("roles").add(Restrictions.eq("name", roleName));
		
		// the search
		persons = (List<Person>)getHibernateTemplate().findByCriteria(dc);		
		logger.debug("getPersonsWithRoleByOrganisation - END");
		return persons;
	}	
	
	
	/**
	 * Returns a list of the persons from the user group with the specified id
	 * 
	 * @author Coni
	 * 
	 * @param userGroupId
	 * @return
	 */
	public List<Person> getByUserGroupId(Integer userGroupId) {
		logger.debug("getByUserGroupId - START - user group id: ".concat(userGroupId.toString()));
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personWithUserGroupsEntity);
		dc.add(Restrictions.ne("status", IConstant.NOM_PERSON_STATUS_DELETED));
		dc.createCriteria("userGroups").add(Restrictions.eq("userGroupId", userGroupId));
		
		List<Person> persons = (List<Person>)getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByUserGroupId - START");
		return persons;
	}
	
    /**
     * Returns persons identified by personId. Person contains general info attributes.
     * 
     * @author Coni
     */
	
	public List<Person> getByPersonId(Integer[] personIds){
		logger.debug("getByPersonId - START");
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personEntity);
		
		dc.add(Restrictions.in("personId", personIds));
		
		List<Person> persons = (List<Person>)getHibernateTemplate().findByCriteria(dc);
		logger.debug("getByPersonId - END");
		return persons;
	}
	
    /**
     * Retrieves from search a list of person with basic info 
     * 
     * @author Coni
     * @param searchPersonBean
     * @return
     */
    public List<Person> getFromSearchSimple(SearchPersonBean searchPersonBean, boolean withDeleted) {
    	logger.debug("getFromSearchSimple - START");
    	
    	DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personFromOrganisationEntity);
    	
		// Status; in all the other modules the disabled persons are treated the same as the deleted persons
		if (!withDeleted) {
			dc.add(Restrictions.eq("status", IConstant.NOM_PERSON_STATUS_ACTIVATED));
		}
    	    	    	
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getFirstName()) ) {
			dc.add(Restrictions.ilike("firstName", "%".concat(searchPersonBean.getFirstName()).concat("%")));
			logger.debug("firstName: " + searchPersonBean.getFirstName());
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getLastName()) ) {
			dc.add(Restrictions.ilike("lastName", "%".concat(searchPersonBean.getLastName()).concat("%")));
			logger.debug("lastName: " + searchPersonBean.getLastName());
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getUsername()) ) {
			dc.add(Restrictions.ilike("username", "%".concat(searchPersonBean.getUsername()).concat("%")));
			logger.debug("username: " + searchPersonBean.getUsername());
		}
		
		if (searchPersonBean.getOrganisationId() > 0) {
			dc.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(searchPersonBean.getOrganisationId())));
			logger.debug("organizationId: " + searchPersonBean.getOrganisationId());
		}
		
		List<Person> res = getHibernateTemplate().findByCriteria(dc);
    	
    	logger.debug("getFromSearchSimple - END");
    	return res;
    }
    
    /**
     * Disables all persons from all organisations
     *
     * @author alu
     */
    public void disableAll(){
    	logger.debug("disableAll - START");
    	
    	// create the hql that will change the status of all persons to disabled
    	String hql = "update ".concat(IModelConstant.personFromOrganisationEntity).concat(" set enabled = :enabled");
    	
        Query query = getSession().createQuery(hql);
        query.setInteger("enabled", IConstant.NOM_PERSON_STATUS_DISABLED);
        int rowCount = query.executeUpdate();
        logger.debug("Rows affected: " + rowCount);
    	
    	logger.debug("disableAll - END");
    }  
    
    /**
     * Retrieves from search a list of person with basic info and pagination
     * 
     * @author Coni
     * @param searchPersonBean
     * @return
     */
    public List<Person> getFromSearchSimpleWithPagination(SearchPersonBean searchPersonBean, boolean withDeleted) {
		logger.debug("getFromSearch - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.personFromOrganisationEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.personFromOrganisationEntity);
		
		// Status; in all the other modules the disabled persons are treated the same as the deleted persons
		if (!withDeleted) {
			dc.add(Restrictions.eq("status", IConstant.NOM_PERSON_STATUS_ACTIVATED));
			dcCount.add(Restrictions.eq("status", IConstant.NOM_PERSON_STATUS_ACTIVATED));
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getFirstName()) ) {
			dc.add(Restrictions.ilike("firstName", "%".concat(searchPersonBean.getFirstName()).concat("%")));
			dcCount.add(Restrictions.ilike("firstName", "%".concat(searchPersonBean.getFirstName()).concat("%")));			
			logger.debug("firstName: " + searchPersonBean.getFirstName());
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getLastName()) ) {
			dc.add(Restrictions.ilike("lastName", "%".concat(searchPersonBean.getLastName()).concat("%")));
			dcCount.add(Restrictions.ilike("lastName", "%".concat(searchPersonBean.getLastName()).concat("%")));
			logger.debug("lastName: " + searchPersonBean.getLastName());
		}
		
		if ( Tools.getInstance().stringNotEmpty(searchPersonBean.getUsername()) ) {
			dc.add(Restrictions.ilike("username", "%".concat(searchPersonBean.getUsername()).concat("%")));
			dcCount.add(Restrictions.ilike("username", "%".concat(searchPersonBean.getUsername()).concat("%")));
			logger.debug("username: " + searchPersonBean.getUsername());
		}
		
		if (searchPersonBean.getDepartmentId() > 0){
			dc.createCriteria("depts").add(Restrictions.eq("departmentId", new Integer(searchPersonBean.getDepartmentId())));
			dcCount.createCriteria("depts").add(Restrictions.eq("departmentId", new Integer(searchPersonBean.getDepartmentId())));
			logger.debug("departmentId: " + searchPersonBean.getDepartmentId());
		
		} else if (searchPersonBean.getOrganisationId() > 0){
		 	//if the search is in all branches we use it
			dc.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(searchPersonBean.getOrganisationId())));
			dcCount.createCriteria("depts").add(Restrictions.eq("organisationId", new Integer(searchPersonBean.getOrganisationId())));
			logger.debug("organisationId: " + searchPersonBean.getOrganisationId());
		}
						
		if (searchPersonBean.getSex() != null){
			if(searchPersonBean.getSex().equals(IConstant.NOM_PERSON_SEX_F) || searchPersonBean.getSex().equals(IConstant.NOM_PERSON_SEX_M)) {
				dc.add(Restrictions.eq("sex", searchPersonBean.getSex()));
				dcCount.add(Restrictions.eq("sex", searchPersonBean.getSex()));
				logger.debug("sex: " + searchPersonBean.getSex());
			}			
		}
					
		dc.setProjection(Projections.id());
		dcCount.setProjection(Projections.id());
		// until here, I've created the subquery
		// now, it's time to retrive all the persons that are in the list of the subquery
		DetachedCriteria dc1 = DetachedCriteria.forEntityName(IModelConstant.personFromOrganisationEntity);
		dc1.add(Subqueries.propertyIn("personId", dc));
		
		
		// check if I have to order the results
		if(searchPersonBean.getSortParam() != null && StringUtils.hasLength(searchPersonBean.getSortParam()) ) {			
			// if I have to, check if I have to order them ascending or descending
			if (searchPersonBean.getSortDirection() == IConstant.ASCENDING ) {
				// ascending
				dc1.addOrder(Order.asc(searchPersonBean.getSortParam()));
			} else {
				// descending
				dc1.addOrder(Order.desc(searchPersonBean.getSortParam()));
			}
		}
	
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of result and pages
		if (searchPersonBean.getNbrOfResults() == -1){
			// set the countDistinct restriction
			dcCount.setProjection(Projections.countDistinct("personId"));			
			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			searchPersonBean.setNbrOfResults(nbrOfResults);
			logger.debug("NbrOfResults " + searchPersonBean.getNbrOfResults());
			logger.debug("----> searchPersonBean.getResults " + searchPersonBean.getResultsPerPage());
			// get the number of pages
			if (nbrOfResults % searchPersonBean.getResultsPerPage() == 0) {
				searchPersonBean.setNbrOfPages(nbrOfResults / searchPersonBean.getResultsPerPage());
			} else {
				searchPersonBean.setNbrOfPages(nbrOfResults / searchPersonBean.getResultsPerPage() + 1);
			}
			searchPersonBean.setCurrentPage(1);
		}
		
		List<Person> res = getHibernateTemplate().findByCriteria(dc1, (searchPersonBean.getCurrentPage()-1) * searchPersonBean.getResultsPerPage(), searchPersonBean.getResultsPerPage());
		
		logger.debug("Res " + res.size());
		logger.debug("getFromSearch - END - results size : ".concat(String.valueOf(res.size())));
		return res;
    }
}
