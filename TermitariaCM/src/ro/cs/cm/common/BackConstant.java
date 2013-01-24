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
package ro.cs.cm.common;

/**
 * For back constants
 * 
 * @author Adelina
 *
 */

public class BackConstant {

	
	//------------------------------CLIENT----------------------------------
	
	private final String CM_Back_AddClient							= "ADD";
	
	private final String CM_Back_GetClient							= "GET";	
	
	private final String CM_Back_SaveClient							= "SAVE";
	
	//------------------------------PROJECT----------------------------------

	private final String CM_Back_AddProject							= "ADD";
	
	private final String CM_Back_GetProject							= "GET";	
	
	private final String CM_Back_SaveProject						= "SAVE";
	
	private final String CM_BackGetProjectsFromClientListing		= "GET_PROJECTS_FROM_CLIENT_LISTING";
	
	private final String CM_BackGetProjectsFromClientForm			= "GET_PROJECTS_FROM_CLIENT_FORM";
	
	private final String CM_BackAddProjectFromClientForm			= "ADD_PROJECT_FROM_CLIENT_FORM";
	
				
	//------------------------------PROJECTTEAM----------------------------------
	
	private final String CM_Back_AddProjectTeam						= "ADD";
	
	private final String CM_Back_GetProjectTeam						= "GET";	
	
	private final String CM_Back_GetProjectTeamFromProjectListing   = "GET_PROJECT_TEAM_FROM_PROJECT_LISTING";
	
	private final String CM_Back_GetProjectTeamFromProjectForm		= "GET_PROJECT_TEAM_FROM_PROJECT_FORM";
	
	private final String CM_Back_GetProjectTeamFromProjectList		= "GET_PROJECT_TEAM_FROM_PROJECT_LIST";
		
			
	// getters(for jsp calls)    
								
	//singleton implementation
    private static BackConstant theInstance = null;
    
    static {
        theInstance = new BackConstant();
    }
    public static BackConstant getInstance() {
        return theInstance;
    }
    
    private BackConstant() {
    	
    }

	/**
	 * @return the cM_Back_AddProject
	 */
	public String getCM_Back_AddProject() {
		return CM_Back_AddProject;
	}

	/**
	 * @return the cM_Back_GetProject
	 */
	public String getCM_Back_GetProject() {
		return CM_Back_GetProject;
	}

	/**
	 * @return the cM_Back_SaveProject
	 */
	public String getCM_Back_SaveProject() {
		return CM_Back_SaveProject;
	}

	/**
	 * @return the cM_BackGetProjectsFromClientListing
	 */
	public String getCM_BackGetProjectsFromClientListing() {
		return CM_BackGetProjectsFromClientListing;
	}

	/**
	 * @return the cM_BackGetProjectsFromClientForm
	 */
	public String getCM_BackGetProjectsFromClientForm() {
		return CM_BackGetProjectsFromClientForm;
	}

	/**
	 * @return the cM_BackAddProjectFromClientForm
	 */
	public String getCM_BackAddProjectFromClientForm() {
		return CM_BackAddProjectFromClientForm;
	}

	/**
	 * @return the cM_Back_AddProjectTeam
	 */
	public String getCM_Back_AddProjectTeam() {
		return CM_Back_AddProjectTeam;
	}

	/**
	 * @return the cM_Back_GetProjectTeam
	 */
	public String getCM_Back_GetProjectTeam() {
		return CM_Back_GetProjectTeam;
	}

	/**
	 * @return the cM_Back_GetProjectTeamFromProjectListing
	 */
	public String getCM_Back_GetProjectTeamFromProjectListing() {
		return CM_Back_GetProjectTeamFromProjectListing;
	}

	/**
	 * @return the cM_Back_GetProjectTeamFromProjectForm
	 */
	public String getCM_Back_GetProjectTeamFromProjectForm() {
		return CM_Back_GetProjectTeamFromProjectForm;
	}

	/**
	 * @return the cM_Back_GetProjectTeamFromProjectList
	 */
	public String getCM_Back_GetProjectTeamFromProjectList() {
		return CM_Back_GetProjectTeamFromProjectList;
	}

	public String getCM_Back_AddClient() {
		return CM_Back_AddClient;
	}

	public String getCM_Back_GetClient() {
		return CM_Back_GetClient;
	}

	public String getCM_Back_SaveClient() {
		return CM_Back_SaveClient;
	}	
		
}
