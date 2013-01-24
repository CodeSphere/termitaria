/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.model.dao;


import ro.cs.ts.context.TSContext;

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
	
	private static final String recordDaoBean = "recordDao";
	private static final String activityDaoBean = "activityDao";
	private static final String projectDetailsDaoBean = "projectDetailsDao";
	private static final String teamMemberDetailDaoBean = "teamMemberDetailDao";
	private static final String personDetailDaoBean = "personDetailDao";
	private static final String notificationDaoBean = "notificationDao";
	private static final String notificationSettingDaoBean = "notificationSettingDao";
	private static final String costSheetDaoBean = "costSheetDao";
	private static final String currencyDaoBean = "currencyDao";
	private static final String exchangeDaoBean = "exchangeDao";
	private static final String widgetSessionDaoBean = "widgetSessionDao";
	private static final String recordSessionDaoBean = "recordSessionDao";
	
	public IDaoRecord getDaoRecord(){
		return (IDaoRecord)(TSContext.getApplicationContext().getBean(recordDaoBean));
	}
	
	public IDaoActivity getDaoActivity(){
		return (IDaoActivity)(TSContext.getApplicationContext().getBean(activityDaoBean));
	}
	
	public IDaoProjectDetails getDaoProjectDetails(){
		return (IDaoProjectDetails)(TSContext.getApplicationContext().getBean(projectDetailsDaoBean));
	}
	
	public IDaoTeamMemberDetail getDaoTeamMemberDetail(){
		return (IDaoTeamMemberDetail)(TSContext.getApplicationContext().getBean(teamMemberDetailDaoBean));
	}
	
	public IDaoPersonDetail getDaoTeamPersonDetail(){
		return (IDaoPersonDetail)(TSContext.getApplicationContext().getBean(personDetailDaoBean));
	}
	
	public IDaoNotification getDaoNotification(){
		return (IDaoNotification)(TSContext.getApplicationContext().getBean(notificationDaoBean));
	}
	
	public IDaoNotificationSetting getDaoNotificationSetting(){
		return (IDaoNotificationSetting)(TSContext.getApplicationContext().getBean(notificationSettingDaoBean));
	}
	
	public IDaoCostSheet getDaoCostSheet(){
		return (IDaoCostSheet)(TSContext.getApplicationContext().getBean(costSheetDaoBean));
	}
	
	public IDaoCurrency getDaoCurrency(){
		return (IDaoCurrency)(TSContext.getApplicationContext().getBean(currencyDaoBean));
	}
	
	public IDaoExchange getDaoExchange(){
		return (IDaoExchange)(TSContext.getApplicationContext().getBean(exchangeDaoBean));
	}
	
	public IDaoWidgetSession getDaoWidgetSession(){
		return (IDaoWidgetSession)(TSContext.getApplicationContext().getBean(widgetSessionDaoBean));
	}
	
	public IDaoRecordSession getDaoRecordSession(){
		return (IDaoRecordSession)(TSContext.getApplicationContext().getBean(recordSessionDaoBean));
	}
}
