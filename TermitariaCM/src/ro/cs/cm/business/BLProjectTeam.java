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
package ro.cs.cm.business;

import java.util.List;

import ro.cs.cm.entity.ProjectTeam;
import ro.cs.cm.entity.TeamMember;
import ro.cs.cm.exception.BusinessException;
import ro.cs.cm.exception.ICodeException;
import ro.cs.cm.model.dao.DaoBeanFactory;
import ro.cs.cm.model.dao.IDaoProjectTeam;


public class BLProjectTeam extends BusinessLogic{

	private IDaoProjectTeam projectTeamDao = DaoBeanFactory.getInstance().getDaoProjectTeam();
	
	// singleton implementation 
	private static BLProjectTeam theInstance = null;
	
	private BLProjectTeam() {};
	
	static {
		theInstance = new BLProjectTeam();
	}
	
	public static BLProjectTeam getInstance() {
		return theInstance;
	}
	
	/**
	 * Get the projectTeam identified by it's id
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectTeam get(Integer projectId, boolean withExternalPerson) throws BusinessException {
		logger.debug("get - START - project with id = ".concat(Integer.toString(projectId)));
		ProjectTeam projectTeam = null;
		try{
			projectTeam = projectTeamDao.get(projectId, withExternalPerson);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECTTEAM_GET, e);
		}
		logger.debug("get - END");
		return projectTeam;
	}
	
	/**
	 * Adds a projectTeam to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeam
	 * @throws BusinessException 
	 */
	public void add(ProjectTeam projectTeam, List<TeamMember> externalMembers) throws BusinessException {
		logger.debug("add - START");
		try{
			projectTeamDao.add(projectTeam, externalMembers);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECTTEAM_ADD, e);
		}
		logger.debug("add - END");
	}
		
	/**
	 * Updates a projectTeam to the database
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeam
	 * @throws BusinessException 
	 */
	public void update(ProjectTeam projectTeam, List<TeamMember> externalMembers) throws BusinessException {
		logger.debug("update - START");
		try{
			projectTeamDao.update(projectTeam, externalMembers);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECTTEAM_UPDATE, e);
		}
		logger.debug("update - END");
	}
	

	/**
	 * Checks if a project has associated a project team
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @return
	 * @throws BusinessException 
	 */
	public boolean hasProjectTeam(Integer projectId) throws BusinessException {
		logger.debug("hasProjectTeam - START");
		boolean hasProjectTeam = false;
		try{
			hasProjectTeam = projectTeamDao.hasProjectTeam(projectId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECTTEAM_FOR_PROJECT, e);
		}
				
		logger.debug("hasProjectTeam - END");
		return hasProjectTeam;
	}
	
	/**
	 * Deletes a project team with all the data necessary
	 * 
	 * @author Adelina
	 * 
	 * @param projectTeamId
	 * @return
	 * @throws BusinessException 
	 */
	public ProjectTeam deleteAll(Integer projectTeamId) throws BusinessException {
		logger.debug("deleteAll - START");		
		ProjectTeam projectTeam = null;
		
		try{
			projectTeam = projectTeamDao.deleteAll(projectTeamId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.PROJECTTEAM_DELETE, e);
		}
		
		logger.debug("deleteAll  - END");
		return projectTeam;
	}	
}
