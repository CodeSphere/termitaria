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
package ro.cs.logaudit.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;

import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.context.AuditContext;
import ro.cs.logaudit.dao.DaoBeanFactory;
import ro.cs.logaudit.dao.IDaoAuditCm;
import ro.cs.logaudit.dao.IDaoAuditDm;
import ro.cs.logaudit.dao.IDaoAuditOm;
import ro.cs.logaudit.dao.IDaoAuditTs;
import ro.cs.logaudit.entity.AuditCmBean;
import ro.cs.logaudit.entity.AuditDmBean;
import ro.cs.logaudit.entity.AuditOmBean;
import ro.cs.logaudit.entity.AuditTsBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.exception.ICodeException;
import ro.cs.logaudit.ws.client.reports.IReportsWsClientConstant;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;
import ro.cs.logaudit.ws.server.entity.WSAuditCmBean;
import ro.cs.logaudit.ws.server.entity.WSAuditDmBean;
import ro.cs.logaudit.ws.server.entity.WSAuditOmBean;
import ro.cs.logaudit.ws.server.entity.WSAuditTsBean;



/**
 * Singleton which expose business methods for Audit as Reports Data Source
 * 
 * @author coni
 */
public class BLReportsDataSource extends BusinessLogic{

	private IDaoAuditOm auditOmDao = DaoBeanFactory.getInstance().getDaoAuditOm();
	private IDaoAuditDm auditDmDao = DaoBeanFactory.getInstance().getDaoAuditDm();
	private IDaoAuditCm auditCmDao = DaoBeanFactory.getInstance().getDaoAuditCm();
	private IDaoAuditTs auditTsDao = DaoBeanFactory.getInstance().getDaoAuditTs();
	private MessageSource messageSource = (MessageSource) AuditContext.getApplicationContext().getBean("messageSource");
	
	//singleton implementation
    private static BLReportsDataSource theInstance = null;
    private BLReportsDataSource(){};
    static {
        theInstance = new BLReportsDataSource();
    }
    public static BLReportsDataSource getInstance() {
        return theInstance;
    }
    
    /**
     * @author coni
     * @param getDataCriteria
     * Gets data for the Audit Events report data source request
     * @throws BusinessException 
     */
    public List getAuditEventsReportData(AuditEventsReportGetDataCriteria getDataCriteria) throws BusinessException {
    	logger.debug("getAuditEventsReportData START");
    	List result = null; 
    	try{
    		int moduleId = (Integer) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MODULE_ID_PARAM);
    		if (moduleId == IConstant.NOM_MODULE_OM_LABEL_KEY) {
        		List<AuditOmBean> auditEvents = auditOmDao.getAuditBeanForAuditEventsReportDataSource(getDataCriteria);
        		//create the returned result list containing WSAuditOmBean object created from the retrieved list
        		result = new ArrayList<WSAuditOmBean>(); 
        		for (AuditOmBean auditOmBean : auditEvents){
        			WSAuditOmBean wsAuditOmBean = new WSAuditOmBean();
        			
        			wsAuditOmBean.setDate(auditOmBean.getDate());
        			
        			wsAuditOmBean.setEvent(messageSource.getMessage(auditOmBean.getEvent(), null, new Locale(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)))));
        			
        			//invoke either getMessageRO(), either getMessageEN() methods on auditOmBean, depending on the locale specified in the report data source request
					Method method = Class.forName(AuditOmBean.class.getName()).getDeclaredMethod("getMessage"
							.concat(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)).toUpperCase()) 
							, new Class[]{});
					String auditMessage = (String) method.invoke(auditOmBean, new Object[] {});
        			wsAuditOmBean.setMessage(auditMessage);
        			
        			wsAuditOmBean.setPersonName(auditOmBean.getFirstName().concat(" ").concat(auditOmBean.getLastName()));
        			result.add(wsAuditOmBean);
        		}
    		} else if (moduleId == IConstant.NOM_MODULE_DM_LABEL_KEY) {
        		List<AuditDmBean> auditEvents = auditDmDao.getAuditBeanForAuditEventsReportDataSource(getDataCriteria);
        		//create the returned result list containing WSAuditCmBean object created from the retrieved list
        		result = new ArrayList<WSAuditDmBean>(); 
        		for (AuditDmBean auditDmBean : auditEvents){
        			WSAuditDmBean wsAuditDmBean = new WSAuditDmBean();
        			
        			wsAuditDmBean.setDate(auditDmBean.getDate());
        			
        			wsAuditDmBean.setEvent(messageSource.getMessage(auditDmBean.getEvent(), null, new Locale(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)))));
        			
        			//invoke either getMessageRO(), either getMessageEN() methods on auditDmBean, depending on the locale specified in the report data source request
					Method method = Class.forName(AuditDmBean.class.getName()).getDeclaredMethod("getMessage"
							.concat(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)).toUpperCase()) 
							, new Class[]{});
					String auditMessage = (String) method.invoke(auditDmBean, new Object[] {});
					wsAuditDmBean.setMessage(auditMessage);
        			
					wsAuditDmBean.setPersonName(auditDmBean.getFirstName().concat(" ").concat(auditDmBean.getLastName()));
        			result.add(wsAuditDmBean);
        		}
    		} else if (moduleId == IConstant.NOM_MODULE_CM_LABEL_KEY) {
        		List<AuditCmBean> auditEvents = auditCmDao.getAuditBeanForAuditEventsReportDataSource(getDataCriteria);
        		//create the returned result list containing WSAuditCmBean object created from the retrieved list
        		result = new ArrayList<WSAuditCmBean>(); 
        		for (AuditCmBean auditCmBean : auditEvents){
        			WSAuditCmBean wsAuditCmBean = new WSAuditCmBean();
        			
        			wsAuditCmBean.setDate(auditCmBean.getDate());
        			
        			wsAuditCmBean.setEvent(messageSource.getMessage(auditCmBean.getEvent(), null, new Locale(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)))));
        			
        			//invoke either getMessageRO(), either getMessageEN() methods on auditCmBean, depending on the locale specified in the report data source request
					Method method = Class.forName(AuditCmBean.class.getName()).getDeclaredMethod("getMessage"
							.concat(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)).toUpperCase()) 
							, new Class[]{});
					String auditMessage = (String) method.invoke(auditCmBean, new Object[] {});
					wsAuditCmBean.setMessage(auditMessage);
        			
					wsAuditCmBean.setPersonName(auditCmBean.getFirstName().concat(" ").concat(auditCmBean.getLastName()));
        			result.add(wsAuditCmBean);
        		}
    		} else if (moduleId == IConstant.NOM_MODULE_TS_LABEL_KEY) {
        		List<AuditTsBean> auditEvents = auditTsDao.getAuditBeanForAuditEventsReportDataSource(getDataCriteria);
        		//create the returned result list containing WSAuditTsBean object created from the retrieved list
        		result = new ArrayList<WSAuditTsBean>(); 
        		for (AuditTsBean auditTsBean : auditEvents){
        			WSAuditTsBean wsAuditTsBean = new WSAuditTsBean();
        			
        			wsAuditTsBean.setDate(auditTsBean.getDate());
        			
        			wsAuditTsBean.setEvent(messageSource.getMessage(auditTsBean.getEvent(), null, new Locale(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)))));
        			
        			//invoke either getMessageRO(), either getMessageEN() methods on auditTsBean, depending on the locale specified in the report data source request
					Method method = Class.forName(AuditTsBean.class.getName()).getDeclaredMethod("getMessage"
							.concat(((String) getDataCriteria.getProperties().get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)).toUpperCase()) 
							, new Class[]{});
					String auditMessage = (String) method.invoke(auditTsBean, new Object[] {});
					wsAuditTsBean.setMessage(auditMessage);
        			
					wsAuditTsBean.setPersonName(auditTsBean.getFirstName().concat(" ").concat(auditTsBean.getLastName()));
        			result.add(wsAuditTsBean);
        		}
    		}
    		
    	} catch (Exception bexc) {
    		throw new BusinessException(ICodeException.REPORTS_DATASOURCE_AUDIT_EVENTS_REPORT, bexc);
    	}
    	logger.debug("getAuditEventsReportData END");
    	return result;
    }
}
