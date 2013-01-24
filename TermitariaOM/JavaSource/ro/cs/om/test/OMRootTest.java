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
package ro.cs.om.test;

import org.junit.Before;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import ro.cs.om.model.dao.IDaoAuthorization;
import ro.cs.om.model.dao.IDaoCalendar;
import ro.cs.om.model.dao.IDaoDepartment;
import ro.cs.om.model.dao.IDaoFreeDay;
import ro.cs.om.model.dao.IDaoLogo;
import ro.cs.om.model.dao.IDaoModule;
import ro.cs.om.model.dao.IDaoOOO;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.model.dao.IDaoPermission;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.om.model.dao.IDaoPicture;
import ro.cs.om.model.dao.IDaoRole;
import ro.cs.om.model.dao.IDaoSetting;
import ro.cs.om.model.dao.IDaoTheme;

/**
 * @author dan.damian
 *
 */
public abstract class OMRootTest {

	
	private static XmlWebApplicationContext appCtx = null; 
	

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
	
	
	static {

		appCtx = new XmlWebApplicationContext();
		appCtx.setConfigLocation("file:./WebContent/WEB-INF/applicationContext.xml");
		MockServletContext mockServletContext =	new MockServletContext("");
		appCtx.setServletContext(mockServletContext);
		appCtx.refresh();
		
		
	}
	
	public static IDaoCalendar getDaoCalendar(){
		return (IDaoCalendar)(appCtx.getBean(calendarDaoBean));
	}
	
	public static IDaoFreeDay getDaoFreeDay(){
		return (IDaoFreeDay)(appCtx.getBean(freeDayDaoBean));
	}
	
	public static IDaoOrganisation getDaoOrganisation(){
		return (IDaoOrganisation)appCtx.getBean(organisationDaoBean);
	}
	
	public static IDaoPerson getDaoPerson(){
		return (IDaoPerson)appCtx.getBean(personDaoBean);
	}
	
	public static IDaoAuthorization getDaoAuthorization(){
		return (IDaoAuthorization) appCtx.getBean(authorizationDaoBean);
	}
	
	public static IDaoDepartment getDaoDepartment(){
		return (IDaoDepartment) appCtx.getBean(departmentDaoBean);
	}	
	
	public static IDaoRole getDaoRole(){
		return (IDaoRole) appCtx.getBean(roleDaoBean);
	}
	
	public static IDaoPermission getDaoPermission(){
		return (IDaoPermission) appCtx.getBean(permissionDaoBean);
	}
	
	public static IDaoModule getDaoModule(){
		return (IDaoModule) appCtx.getBean(moduleDaoBean);
	}
	
	public static IDaoSetting getDaoSetting(){
		return (IDaoSetting) appCtx.getBean(settingDaoBean);
	}
	
	public static IDaoPicture getDaoPicture(){
		return (IDaoPicture) appCtx.getBean(pictureDaoBean);
	}
	
	public static IDaoTheme getDaoTheme(){
		return (IDaoTheme) appCtx.getBean(themeDaoBean);
	}
	
	public static IDaoLogo getDaoLogo(){
		return (IDaoLogo) appCtx.getBean(logoDaoBean);
	}
	
	public static IDaoOOO getDaoOOO(){
		return (IDaoOOO) appCtx.getBean(oooDaoBean);
	}
	
	@Before
	public void showMessage() {
		System.out.println("---------------------------------------------------------");
		System.out.println("                    T E S T I N G: " + getClass().getName());
		System.out.println("---------------------------------------------------------");
	}
	
}
