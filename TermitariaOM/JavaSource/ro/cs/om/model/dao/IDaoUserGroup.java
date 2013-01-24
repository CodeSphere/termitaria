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
package ro.cs.om.model.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.SearchUserGroupBean;
import ro.cs.om.entity.UserGroup;

/*
 *  @author coni
 */
public interface IDaoUserGroup {
	
	/*
	 * Returns an user group with all its components
	 * @author coni
	 */
	public UserGroup getAll(int userGroupId);
	
	/*
	 * Deletes the user group specified by userGroupId 
	 * Returns the user group deleted
	 * @author coni
	 */
	@Transactional (rollbackFor=Exception.class)
	public UserGroup deleteAll(int userGroupId);


	/*
	 * Gets the results for search
	 * @param searchUserGroupBean
	 * @author coni
	 */

	public List<UserGroup> getUserGroupBeanFromSearch(SearchUserGroupBean searchUserGroupBean, boolean isDeleteAction);
	
	/*
	 * updates the group of users
	 * @param userGroup
	 * @author coni
	 */
	@Transactional (rollbackFor=Exception.class)
	public void updateAll(UserGroup userGroup);
	
	/*
	 * adds the specified group of users
	 * @param userGroup
	 * @author coni
	 */
	@Transactional (rollbackFor=Exception.class)
	public void addAll(UserGroup userGroup);
	
	/*
	 * Returns an user group with only its basic components
	 * @param userGroupId
	 * @author coni
	 */
    public UserGroup get(int userGroupId);
    
	/*
	 * returns the default user group for all organizations
	 * @author coni
	 */
	public UserGroup getDefaultUserGrup(int organizationId);
	
	/*
	 * returns all an organization user groups except the default one
	 * @author coni
	 */
	public List<UserGroup> getAllOrganizationUserGroups(int organizationId);
	
	/**
	 * 
	 * Delete all the usergroups for an organisation
	 * @author mitziuro
	 *
	 * @param organisationId
	 */
	@Transactional (rollbackFor=Exception.class)
	public void deleteByOrganisationId(int organisationId);
	
	/**
	 * Return a list with the user groups from the given organization, without 
	 * those that belongs to a given person
	 * 
	 * @author Adelina
	 * 
	 * @param organizationId
	 * @param personId
	 * @return List<UserGroup>
	 */
	public List<UserGroup> getUserGroupByOrganizationAndPerson(Integer organizationId, Integer personId);
	
	/**
	 * Returns an usergroup with the given name
	 * 
	 * @author Adelina
	 * 
	 * @param name
	 * @return
	 */
	public UserGroup getUserGroupByNameAndOrg(String name, Integer organisationId);
	
	/**
	 * Returns a list of user groups that contain the user with the specified id
	 * 
	 * @author Coni
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserGroup> getUserGroupsByUser(int userId);
}
