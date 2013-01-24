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
package ro.cs.cm.model.dao;

import ro.cs.cm.context.CMContext;



/**
  * @author coni
 *
 * Factory for getting a spring bean from context
 */
public class DaoBeanFactory {
	
	//singleton implementation
	private static DaoBeanFactory theInstance = null;
	private DaoBeanFactory(){};
	
	static {		
		theInstance = new DaoBeanFactory();
	}
	public static DaoBeanFactory getInstance() {
		return theInstance;
	}
	
	private static final String clientDaoBean = "clientDao";
	private static final String projectDaoBean = "projectDao";
	private static final String projectTeamDaoBean = "projectTeamDao";
	private static final String teamMemberDaoBean = "teamMemberDao";
	
	public IDaoClient getDaoClient(){
		return (IDaoClient)(CMContext.getApplicationContext().getBean(clientDaoBean));
	}
	
	public IDaoProject getDaoProject() {
		return (IDaoProject) CMContext.getApplicationContext().getBean(projectDaoBean);
	}	
	
	public IDaoProjectTeam getDaoProjectTeam() {
		return (IDaoProjectTeam) CMContext.getApplicationContext().getBean(projectTeamDaoBean);
	}
	
	public IDaoTeamMember getDaoTeamMember() {
		return (IDaoTeamMember) CMContext.getApplicationContext().getBean(teamMemberDaoBean);
	}
	
}
