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

import ro.cs.om.context.OMContext;


/**
 * Factory for getting a spring bean from context
 *
 * @author matti_joona
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
	
	private static final String calendarDaoBean 				= "calendarDao";
	private static final String freeDayDaoBean					= "freeDayDao";
	private static final String organisationDaoBean 			= "organisationDao";
	private static final String personDaoBean 					= "personDao";
	private static final String authorizationDaoBean 			= "authorizationDao";
	private static final String departmentDaoBean 				= "departmentDao";
	private static final String roleDaoBean 					= "roleDao";
	private static final String permissionDaoBean 				= "permissionDao";
	private static final String moduleDaoBean 					= "moduleDao";
	private static final String settingDaoBean 					= "settingDao";
	private static final String pictureDaoBean					= "pictureDao";
	private static final String themeDaoBean					= "themeDao";
	private static final String logoDaoBean						= "logoDao";
	private static final String oooDaoBean						= "oooDao";	
	private static final String jobDaoBean						= "jobDao";
	private static final String localizationDaoBean 			= "localizationDao";
	private static final String userGroupDaoBean				= "userGroupDao";

	
	public IDaoCalendar getDaoCalendar(){
		return (IDaoCalendar)(OMContext.getApplicationContext().getBean(calendarDaoBean));
	}
	
	public IDaoFreeDay getDaoFreeDay(){
		return (IDaoFreeDay)(OMContext.getApplicationContext().getBean(freeDayDaoBean));
	}
	
	public IDaoOrganisation getDaoOrganisation(){
		return (IDaoOrganisation)OMContext.getApplicationContext().getBean(organisationDaoBean);
	}
	
	public IDaoPerson getDaoPerson(){
		return (IDaoPerson)OMContext.getApplicationContext().getBean(personDaoBean);
	}
	
	public IDaoAuthorization getDaoAuthorization(){
		return (IDaoAuthorization) OMContext.getApplicationContext().getBean(authorizationDaoBean);
	}
	
	public IDaoDepartment getDaoDepartment(){
		return (IDaoDepartment) OMContext.getApplicationContext().getBean(departmentDaoBean);
	}	
	
	public IDaoRole getDaoRole(){
		return (IDaoRole) OMContext.getApplicationContext().getBean(roleDaoBean);
	}
	
	public IDaoPermission getDaoPermission(){
		return (IDaoPermission) OMContext.getApplicationContext().getBean(permissionDaoBean);
	}
	
	public IDaoModule getDaoModule(){
		return (IDaoModule) OMContext.getApplicationContext().getBean(moduleDaoBean);
	}
	
	public IDaoJob getDaoJob(){
		return (IDaoJob) OMContext.getApplicationContext().getBean(jobDaoBean);
	}
	
	public IDaoPicture getDaoPicture(){
		return (IDaoPicture) OMContext.getApplicationContext().getBean(pictureDaoBean);
	}
	
	public IDaoTheme getDaoTheme(){
		return (IDaoTheme) OMContext.getApplicationContext().getBean(themeDaoBean);
	}
	
	public IDaoSetting getDaoSetting(){
		return (IDaoSetting) OMContext.getApplicationContext().getBean(settingDaoBean);
	}
	
	public IDaoLogo getDaoLogo(){
		return (IDaoLogo) OMContext.getApplicationContext().getBean(logoDaoBean);
	}
	
	public IDaoOOO getDaoOOO(){
		return (IDaoOOO) OMContext.getApplicationContext().getBean(oooDaoBean);
	}
	
	public IDaoLocalization getDaoLocalization(){
		return (IDaoLocalization)OMContext.getApplicationContext().getBean(localizationDaoBean);
	}
	
	public IDaoUserGroup getDaoUserGroup(){
		return (IDaoUserGroup)OMContext.getApplicationContext().getBean(userGroupDaoBean);
	}
}
