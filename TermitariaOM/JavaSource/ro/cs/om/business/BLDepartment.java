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
package ro.cs.om.business;

import java.util.List;
import java.util.Set;

import ro.cs.om.entity.Department;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchDepartmentBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoDepartment;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.tools.Tools;


/**
 * Singleton which expose business methods for Department item
 *
 * @author matti_joona
 */
public class BLDepartment extends BusinessLogic{

	private IDaoDepartment departmentDao = DaoBeanFactory.getInstance().getDaoDepartment();
	private IDaoPerson personDao = DaoBeanFactory.getInstance().getDaoPerson();
	
	//singleton implementation
    private static BLDepartment theInstance = null;
    private BLDepartment(){};
    static {
        theInstance = new BLDepartment();
    }
    public static BLDepartment getInstance() {
        return theInstance;
    }
    
    
    /**
     * Add a department to the organisation.
     * 
     * @author matti_joona
     */
    public void add(Department department) throws BusinessException{
    	logger.debug("add - START");
    	
    	try{
    		departmentDao.add(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_ADD, bexc);
    	}
    	
    	logger.debug("add - END - of department: ".concat(String.valueOf(department.getDepartmentId())));
    }
    
    /**
     * Return a list of Persons, having the highest position in the hierarchy of this Department
     * @author d
     * @param departmentId this Department id
     * @return List of Persons
     * @throws BusinessException
     */
    public Person getDepartmentsManager(int departmentId) throws BusinessException {
    	logger.debug("getDepartmentsManager - START - of department with id ".concat(String.valueOf(departmentId)));
    	
    	Person manager = null;
    	try{
    		manager = personDao.getManagerOfDepartment(departmentId);    		
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.ORGANIGRAM_GET_DEPARTMENTSMANAGER, bexc);
    	}
    	
    	logger.debug("getDepartmentsManager - END");
    	return manager;
    }
    
    /**
     * Add all data to a department.
     * 
     * @author Adelina
     */
    public void addAll(Department department) throws BusinessException{
    	logger.debug("addAll - START - of department with id ".concat(String.valueOf(department.getDepartmentId())));
    	
    	try{
    		departmentDao.addAll(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_ADD, bexc);
    	}
    	
    	logger.debug("addAll - END");
    }
    
    /**
     * Update data for a department
     * 
     * @author matti_joona
     */
    public void update(Department department) throws BusinessException{	
    	logger.debug("update - START - of department with id ".concat(String.valueOf(department.getDepartmentId())));
    	
    	try{
    		departmentDao.update(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_UPDATE, bexc);
    	}
    	
    	logger.debug("update - END");
    }
    
    /**
     * Update data for a department
     * 
     * @author matti_joona
     */
    public void updateMangerId(Department department) throws BusinessException{	
    	logger.debug("updateMangerId - START - of department with id ".concat(String.valueOf(department.getDepartmentId())));
    	
    	try{
    		departmentDao.updateManagerId(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_UPDATE_MANAGER_ID, bexc);
    	}
    	
    	logger.debug("updateMangerId - END");
    }
        
    /**
     * Update all the data from a department
     * 
     * @author Adelina
     */
    public void updateAll(Department department) throws BusinessException{	
    	logger.debug("updateAll - START - of department with id ".concat(String.valueOf(department.getDepartmentId())));
    	
    	try{
    		departmentDao.updateAll(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_UPDATE, bexc);
    	}
    	
    	logger.debug("updateAll - END");
    }
    
    
    /**
     * Delete a department
     * 
     * @author matti_joona
     */
    public Department delete(Integer departmentId) throws BusinessException{
    	logger.debug("delete  - START");
    	
    	Department department = null;
    	try{

    		logger.debug("list All Sub Departments begin :");
    		logger.debug("-----------------------------------------------------------------------------------");
    		List<Department> subDepartments = BLDepartment.getInstance().listAllSubDepartments(departmentId);
    		logger.debug("-----------------------------------------------------------------------------------");
    		logger.debug("list All Sub Departments end");
    		
    		logger.debug("-----------------------------------------------------------------------------------");
    		for(Department subD : subDepartments) {
    			logger.debug("[ " + subD.getName() + "]");
    		}
    		logger.debug("-----------------------------------------------------------------------------------");
    		
    		logger.debug("Delete this department begin");
    		logger.debug("-----------------------------------------------------------------------------------");
    		department = departmentDao.delete(departmentId);
    		logger.debug("-----------------------------------------------------------------------------------");
    		logger.debug("Delete this department end");
    		for(Department d : subDepartments) {
    			logger.debug("Delete child department " + d.getName());
        		logger.debug("-----------------------------------------------------------------------------------");
    			departmentDao.delete(d.getDepartmentId());
    			logger.debug("-----------------------------------------------------------------------------------");
    			logger.debug("Delete child department " + d.getName() + " end");
    		}
    		
    		
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_DEL, bexc);
    	}
    	
    	logger.debug("delete  - END");
    	return department;
    }
    
    /**
     * Get general data for a department
     * 
     * @author matti_joona
     */
    public Department get(Integer departmentId) throws BusinessException{
    	logger.debug("get - START");
    	
    	Department department = null;
    	try{
    		department = departmentDao.get(departmentId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_GET, bexc);
    	}
    	
    	logger.debug("get - END");
    	return department;
    }
    
    /**
     * Get general data for a department(with persons)
     * 
     * @author Adelina
     */
    public Department getWithPersons(Integer departmentId) throws BusinessException{
    	logger.debug("getWithPersons - START");
    	
    	Department department = null;
    	try{
    		department = departmentDao.getWithPersons(departmentId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_GET, bexc);
    	}
    	
    	logger.debug("getWithPersons - END");
    	return department;
    }
    
    
    /**
     * Get all info for a department
     * 
     * @author matti_joona
     */
    public Department getAll(Integer departmentId) throws BusinessException{
    	logger.debug("getAll - START");
    	
    	Department department = null;
    	try{
    		department = departmentDao.getAll(departmentId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_GET, bexc);
    	}
    	
    	logger.debug("getAll - END");
    	return department;
    }
        
    /**
     * Change the manager of the department.
     * 
     * @author matti_joona
     */
    public void updateManager(Department department) throws BusinessException{
    	logger.debug("updateManager - START");
    	
    	try{
    		departmentDao.updateManager(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_UPDATEMANAGER, bexc);
    	}
    	
    	logger.debug("updateManager - END");
    }
    
    /**
	 * Update the CEO manager
	 * 
	 * @author Adelina
     * @throws BusinessException 
	 */
	public void updateCEO(Department department) throws BusinessException{
		logger.debug("updateCEO - START");
		
		try{
    		departmentDao.updateCEO(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_UPDATEMANAGER, bexc);
    	}
    	
    	logger.debug("updateCEO - END");
	}

    /**
     * Change the parent of the department.
     * 
     * @author matti_joona
     */
    public void updateParent(Department department) throws BusinessException{
    	logger.debug("updateParent - START");
    	
    	try{
    		departmentDao.updateParent(department);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_UPDATEMANAGER, bexc);
    	}
    	
    	logger.debug("updateParent - END");
    }
    
    
    /**
     * Update person list of a department.
     * 
     * @author matti_joona
     */
    public void updatePersons(Department department,Set<Person> persons) throws BusinessException{
    	logger.debug("updatePersons - START");
    	
    	try{
    		departmentDao.updatePersons(department, persons);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_UPDATEPERSONS, bexc);
    	}
    	
    	logger.debug("updatePersons - END");
    }

    /**
     * Get Department knowing it's Manager Id
     * 
     * @author dd
     */
    public List<Department> getForManager(Integer managerPersonId) throws BusinessException{    	
    	logger.debug("getForManager - START");
    	
    	List<Department> departments = null;
    	try{
    		departments = departmentDao.getForManager(managerPersonId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_GET_FOR_MANAGER, bexc);
    	}
    	
    	logger.debug("getForManager - END");
    	return departments;
    }

    
    public Department getFakeForOrganization(Integer organizationId) throws BusinessException {
    	logger.debug("getFakeForOrganization - START");
    	
    	Department fake = null;
    	try{
    		fake = departmentDao.getFakeForOrganisation(organizationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.DEPARTMENT_GET_FAKE_FOR_ORGANIZATION, bexc);
    	}
    	
    	logger.debug("getFakeForOrganization - END");
    	return fake;
    }
    
    
    /**
     * Searches for Department after criterion from searchDepartmentBean
     * @author alu
     * @author dan.damian
     * 
     * @param searchDepartmentBean - Bean that contains the search criterion
     * @return A list of Department beans
     * @throws BusinessException
     */    
    public List<Department> getFromSearch(SearchDepartmentBean searchDepartmentBean, boolean isDeleteAction) throws BusinessException{
    	logger.debug("getFromSearch - START");
    	
    	List<Department> res = null;    	
    	try {    		
			res = departmentDao.getFromSearch(searchDepartmentBean, isDeleteAction);  
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.ORG_SEARCH, e);
    	}
    	
    	logger.debug("getFromSearch - END");
    	return res;
    }
    
    /**
	 * Gets the number of departments for an organisation (without the fake departments)
	 * 
	 * @author Adelina
	 * @parm organisationId
	 * @return number of departments from an organisation
     * @throws BusinessException 
	 */
	public int getCountDepartments(Integer organisationId) throws BusinessException {		
		logger.debug("getCountDepartments - START");
    	
		int countDep = 0;
    	try {
    		countDep = departmentDao.getCountDepartments(organisationId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.DEPARTAMENT_COUNT_FOR_ORGANISATION, e);
    	}
    	
    	logger.debug("getCountDepartments - END");
    	return countDep;		
	}


	/**
	 * Get persons from a department
	 * 
	 * @author matti_joona
	 */
	public Set<Person> listPersons(Integer departmentId, Integer organisationId) throws BusinessException{
		logger.debug("listPersons - START");
		
		Set<Person> listPersons = null;
		try{
			listPersons = departmentDao.listPersons(departmentId, organisationId);
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.DEPARTMENT_GET, bexc);
		}
		
		logger.debug("listPersons - END");
		return listPersons;
	}


	/**
	 * Lists all SubDepartments for one Department
	 * 
	 * @author matti_joona
	 */
	public List<Department> listSubDepartments(Integer departmentId) throws BusinessException{
		logger.debug("listSubDepartments - START");
		
		List<Department> list = null;
		try{
			list = departmentDao.listSubDepartments(departmentId);
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.DEPARTMENT_LIST_SUBDEPARTMENTS, bexc);
		}
		
		logger.debug("listSubDepartments - END");
		return list;
	}


	/**
	 * Lists all Departments within an Organization that could be this department Parent Department.
	 * 
	 * @author dan.damian
	 */
	public List<Department> listPotentialParenDepartments(Integer departmentId, Integer organisationId) throws BusinessException{
		logger.debug("listPotentialParenDepartments - START");
		
		List<Department> list = null;
		try{
			list = departmentDao.listPotentialParentDepartments(departmentId, organisationId);
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.DEPARTMENT_LIST_POTENTIAL_PARENT_DEPARTMENTS, bexc);
		}
		
		logger.debug("listPotentialParenDepartments - END");
		return list;
	}


	/**
	 * Lists all Sub Departments of this Department.
	 * 
	 * @author dan.damian
	 */
	public List<Department> listAllSubDepartments(Integer departmentId) throws BusinessException{
		logger.debug("listAllSubDepartments - START");
		
		List<Department> list = null;
		try{
			list = departmentDao.listAllSubDepartments(departmentId);
			Tools.getInstance().printList(logger, list);
			
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.DEPARTMENT_LIST_ALL_SUBDEPARTMENTS, bexc);
		}
		
		logger.debug("listAllSubDepartments - END");
		return list;
	}


	/**
	 * Lists all Departments within an Organsiation. An Department contains only the General Information Attributes.
	 * 
	 * @author dd
	 */
	public List<Department> listByOrganisation(Integer organisationId) throws BusinessException{
		logger.debug("listByOrganisation - START");
		
		List<Department> list = null;
		try{
			list = departmentDao.listByOrganisation(organisationId);
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.DEPARTMENT_LIST_BY_ORGANISATION, bexc);
		}
		
		logger.debug("listByOrganisation - END");
		return list;
	}
	
	/**
	 * List the first level SubDepartments of this Department, each of them containing only the basic information 
	 * 
	 * @author coni
	 * @param departmentId this Department's id 
	 * @return
	 */
	public List<Department> listFirstLevelSubDepartments(Integer departmentId) throws BusinessException{
		logger.debug("listFirstLevelSubDepartments - START");
		
		List<Department> list = null;
		try{
			list = departmentDao.listFirstLevelSubDepartments(departmentId);
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.DEPARTMENT_LIST_SUBDEPARTMENTS, bexc);
		}
		
		logger.debug("listFirstLevelSubDepartments - END");
		return list;
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
	public List<Department> getDepartmentByOrganizationAndPerson(Integer organizationId, Integer personId) throws BusinessException {
		logger.debug("getDepartmentByOrganizationAndPerson - START");
		
		List<Department> depts = null;
		try{
			depts = departmentDao.getDepartmentByOrganizationAndPerson(organizationId, personId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.DEPARTMENT_GET_FROM_PERSON, e);
		}
		
		logger.debug("getDepartmentByOrganizationAndPerson - END");
		return depts;
	}
    
}
