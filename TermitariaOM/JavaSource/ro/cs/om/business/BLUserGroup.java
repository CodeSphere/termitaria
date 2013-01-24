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

import java.util.ArrayList;
import java.util.List;

import ro.cs.om.entity.SearchUserGroupBean;
import ro.cs.om.entity.UserGroup;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoUserGroup;

/**
 * Singleton which expose business methods for UserGroup item
 * 
 * @author coni
 */
public class BLUserGroup extends BusinessLogic {
	
	private IDaoUserGroup userGroupDao = DaoBeanFactory.getInstance().getDaoUserGroup();
	
	//singleton implementation
	private static BLUserGroup theInstance = null;
	public BLUserGroup(){};
	static {
		theInstance = new BLUserGroup();
	}
	public static BLUserGroup getInstance(){
		return theInstance;
	}
	
	
	/**
	 * Returns an user group with all its components
	 * @param userGroupId
	 * @author coni
	 */
    public UserGroup getAll(int userGroupId) throws BusinessException{
		logger.debug("getAll - START getting user group with id: ".concat(String.valueOf(userGroupId)));
		UserGroup userGroup = null;
		try{
			userGroup = userGroupDao.getAll(userGroupId);
		} catch (Exception e){
			throw new BusinessException(ICodeException.USERGROUP_GET_ALL, e);
		}
		logger.debug("getAll - END getting user group with id: ".concat(String.valueOf(userGroupId)));
		return userGroup;
    }
    
	/**
	 * Returns an user group with only its basic components
	 * @param userGroupId
	 * @author coni
	 */
    public UserGroup get(int userGroupId) throws BusinessException{
		logger.debug("get - START getting user group with id: ".concat(String.valueOf(userGroupId)));
		UserGroup userGroup = null;
		try{
			userGroup = userGroupDao.get(userGroupId);
		} catch (Exception e){
			throw new BusinessException(ICodeException.USERGROUP_GET, e);
		}
		logger.debug("get - END getting user group with id: ".concat(String.valueOf(userGroupId)));
		return userGroup;
    }
    
    
	/**
	 * Deletes the user group specified by userGroupId 
	 * Returns the user group deleted
	 * @param userGroupId
	 * @author coni
	 */
	public UserGroup deleteAll(int userGroupId) throws BusinessException{
		logger.debug("deleteAll - START deleting user group with id: ".concat(String.valueOf(userGroupId)));
		UserGroup userGroup = null;
		try {
			userGroup = userGroupDao.deleteAll(userGroupId);
		} catch (Exception bexc){
			throw new BusinessException(ICodeException.USERGROUP_DELETE, bexc);
		}
		logger.debug("deleteAll - END deleting user group with id: ".concat(String.valueOf(userGroupId)));
		return userGroup;
	}
	
	/**
	 * Gets the results for search
	 * @param searchUserGroupBean
	 * @throws BusinessException
	 * @author coni
	 */
	public List<UserGroup> getResultsForSearch(SearchUserGroupBean searchUserGroupBean, boolean isDeleteAction) throws BusinessException{
		logger.debug("getResultsForSearch - START");
		List<UserGroup> res = null;
		try {
			res = userGroupDao.getUserGroupBeanFromSearch(searchUserGroupBean, isDeleteAction);
		} catch(Exception bexc){
			throw new BusinessException(ICodeException.USERGROUP_GET_RESULTS, bexc);
		}
		logger.debug("getResultsForSearch - END");
		return res;
	}

	/**
	 * updates the group of users
	 * @param userGroup
	 * @author coni
	 */
	public void updateUserGroup(UserGroup userGroup) throws BusinessException{
		logger.debug("updateUserGroup - START");
		try {
			userGroupDao.updateAll(userGroup);
		} catch (Exception bexc) {
			throw new BusinessException(ICodeException.USERGROUP_UPDATE, bexc);
		}
		logger.debug("updateUserGroup - END");
	}
	
	/**
	 * adds the specified group of users
	 * @param userGroup
	 * @author coni
	 */
	public void addUserGroup(UserGroup userGroup) throws BusinessException{
		logger.debug("addUserGroup - START");
		try{
			userGroupDao.addAll(userGroup);
		} catch (Exception bexc) {
			throw new BusinessException(ICodeException.USERGROUP_ADD, bexc);
		}
		logger.debug("addUserGroup - END");
	}
	
	/**
	 * returns the organization default user group containing the basic info
	 * @author coni
	 */
	public UserGroup getDefaultUserGrup(int organizationId) throws BusinessException{
		logger.debug("getDefaultUserGrup - START");
		UserGroup defaultUserGroup = null;
		try{
			defaultUserGroup = userGroupDao.getDefaultUserGrup(organizationId);
		} catch (Exception bexc) {
			throw new BusinessException(ICodeException.USERGROUP_GET_DEFAULT_USER_GROUP, bexc);
		}
		logger.debug("getDefaultUserGrup - END");
		return defaultUserGroup;
	}
	
	/**
	 * Returns all active organization's UserGroups except the default one
	 * @author coni
	 */
	public List<UserGroup> getAllOrganizationUserGroups(int organizationId) throws BusinessException{
		logger.debug("getAllOrganizationUserGroups - START");
		List<UserGroup> orgUserGroups = new ArrayList<UserGroup>();
		try{
			orgUserGroups =  userGroupDao.getAllOrganizationUserGroups(organizationId);
		} catch (Exception bexc) {
			throw new BusinessException(ICodeException.USERGROUP_GET_ALL_ORGANIZATION_USER_GROUPS, bexc);
		}
		return orgUserGroups;
	}
	
	/**
	 * Return a list with the user groups from the given organization, without 
	 * those that belongs to a given person
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param personId
	 * @return List<UserGroup>
	 * @throws BusinessException 
	 */
	public List<UserGroup> getUserGroupByOrganizationAndPerson(Integer organizationId, Integer personId) throws BusinessException {
		logger.debug("getUserGroupByOrganizationAndPerson - START - ");
		List<UserGroup> userGroups = new ArrayList<UserGroup>();
		try{
			userGroups = userGroupDao.getUserGroupByOrganizationAndPerson(organizationId, personId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.USERGROUP_GET_FROM_PERSON,e);
		}
		logger.debug("getUserGroupByOrganizationAndPerson - END - ");
		return userGroups;
	}
	
	/**
	 * Returns an usergroup with the given name
	 * 
	 * @author Adelina
	 * 
	 * @param name
	 * @return UserGroup
	 * @throws BusinessException 
	 */
	public UserGroup getUserGroupByNameAndOrg(String name, Integer organisationId) throws BusinessException {
		logger.debug("getUserGroupByName - START");
		
		UserGroup userGroup = new UserGroup();
		try{
			userGroup = userGroupDao.getUserGroupByNameAndOrg(name, organisationId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.USERGROUP_GET_BY_NAME_AND_ORG, e);
		}
		
		logger.debug("getUserGroupByName - END");
		return userGroup;
	}
	
	/**
	 * Returns a list of user groups that contain the user with the specified id
	 * 
	 * @author Coni
	 * 
	 * @param userId
	 * @return
	 * @throws BusinessException 
	 */
	public List<UserGroup> getUserGroupsByUser(int userId) throws BusinessException {
		logger.debug("getUserGroupsByUser - START");
		List<UserGroup> userGroups = null;
		try {
			userGroups = userGroupDao.getUserGroupsByUser(userId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.USERGROUP_GET_BY_USER, e);
		}
		logger.debug("getUserGroupsByUser - END");
		return userGroups;
	}
	
	
}
