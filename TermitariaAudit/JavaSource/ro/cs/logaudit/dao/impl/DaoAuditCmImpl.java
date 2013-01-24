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
package ro.cs.logaudit.dao.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.logaudit.common.IModelConstant;
import ro.cs.logaudit.dao.IDaoAuditCm;
import ro.cs.logaudit.entity.AuditCmBean;
import ro.cs.logaudit.entity.SearchAuditCmBean;
import ro.cs.logaudit.ws.client.reports.IReportsWsClientConstant;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;

public class DaoAuditCmImpl extends HibernateDaoSupport implements IDaoAuditCm {
	
	/**
	 * Adds a new CM audit event
	 * 
	 * @author coni
	 * @param auditCmBean
	 */
	public void add(AuditCmBean auditCmBean){
		logger.debug("Adding CM Audit Event");
		getHibernateTemplate().save(IModelConstant.auditcmEntity, auditCmBean);
		logger.debug("New CM Audit Event added");
	}
	
	/**
	 * Searches for audit after criterion from searchBean
	 * 
	 * @author Coni
	 * 
	 * @return A list of audit beans 
	 * @throws ParseException 
	 */
	public List<AuditCmBean> getAuditBeanFromSearch(SearchAuditCmBean searchAuditCmBean, boolean isDeleteAction, Locale locale) throws ParseException{
		
		logger.debug("getAuditBeanFromSearch - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.auditcmEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.auditcmEntity);
		
		if (searchAuditCmBean.getOrganisationId() != -1){
			dc.add(Restrictions.eq("organisationId", searchAuditCmBean.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisationId", searchAuditCmBean.getOrganisationId()));			
		}
			
		if (searchAuditCmBean.getEvent() != null && !"-1".equals(searchAuditCmBean.getEvent())){
			dc.add(Restrictions.ilike("event", "%".concat(searchAuditCmBean.getEvent()).concat("%")));
			dcCount.add(Restrictions.ilike("event", "%".concat(searchAuditCmBean.getEvent()).concat("%")));
		}
		if (searchAuditCmBean.getPersonId() != null && !"".equals(searchAuditCmBean.getPersonId())){
			dc.add(Restrictions.eq("personId", searchAuditCmBean.getPersonId()));
			dcCount.add(Restrictions.eq("personId", searchAuditCmBean.getPersonId()));
		}
		if (searchAuditCmBean.getMessage() != null && !"".equals(searchAuditCmBean.getMessage())){
			dc.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditCmBean.getMessage()).concat("%")));
			dcCount.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditCmBean.getMessage()).concat("%")));
		}
		
		if (searchAuditCmBean.getStartDate() != null){
			dc.add(Expression.ge("date", searchAuditCmBean.getStartDate()));		
			dcCount.add(Expression.ge("date", searchAuditCmBean.getStartDate()));
		}
		
		if (searchAuditCmBean.getEndDate() != null){
			dc.add(Expression.le("date", searchAuditCmBean.getEndDate()));		
			dcCount.add(Expression.le("date", searchAuditCmBean.getEndDate()));
		}		
		
		// check if I have to order the results
		if(searchAuditCmBean.getSortParam() != null && !"".equals(searchAuditCmBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchAuditCmBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchAuditCmBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchAuditCmBean.getSortParam()));
			}
		}
		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchAuditCmBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if (searchAuditCmBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("auditId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchAuditCmBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchAuditCmBean.getResultsPerPage() == 0) {
				searchAuditCmBean.setNbrOfPages(nbrOfResults / searchAuditCmBean.getResultsPerPage());
			} else {
				searchAuditCmBean.setNbrOfPages(nbrOfResults / searchAuditCmBean.getResultsPerPage() + 1);
			}
			// after an audit is deleted, the same page has to be displayed;
			//only when all the audit events from last page are deleted, the previous page will be shown 
			if (isDeleteAction && (searchAuditCmBean.getCurrentPage() > searchAuditCmBean.getNbrOfPages()) ){
				searchAuditCmBean.setCurrentPage( searchAuditCmBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchAuditCmBean.setCurrentPage(1);
			}

		}
		
		List<AuditCmBean> res = (List<AuditCmBean>)getHibernateTemplate().findByCriteria(dc, (searchAuditCmBean.getCurrentPage()-1) * searchAuditCmBean.getResultsPerPage(), searchAuditCmBean.getResultsPerPage());				
		logger.debug("getAuditBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;				
	}
	
    /**
     * @author coni
     * @param getDataCriteria
     * Gets data for the Audit Events report data source request
     */
	public List<AuditCmBean> getAuditBeanForAuditEventsReportDataSource(AuditEventsReportGetDataCriteria getDataCriteria) {
		logger.debug("getAuditBeanForAuditEventsReportDataSource - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.auditcmReportsEntity);	
		HashMap<String, Object> criteria = getDataCriteria.getProperties();
		
		String event = (String) criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_EVENT_PARAM);
		if (event != null && !"-1".equals(event)){
			dc.add(Restrictions.ilike("event", "%".concat(event).concat("%")));
		}
		
		String message = (String) criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_MESSAGE_PARAM);
		if (message != null && !"".equals(message)){
			String locale = ((String) criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_LOCALE_PARAM)).toUpperCase();
			dc.add(Restrictions.ilike("message".concat(locale), "%".concat(message).concat("%")));
		}
		
		Integer personId = (Integer) criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_PERSON_ID_PARAM);
		if (personId != null && !"".equals(personId)){
			dc.add(Restrictions.eq("personId", personId));
		}
		
		if (criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_START_DATE_PARAM) != null){	
			Date startDate = ((XMLGregorianCalendar) criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_START_DATE_PARAM)).toGregorianCalendar().getTime();
			dc.add(Expression.ge("date", startDate));		
		}
		
		if (criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_END_DATE_PARAM) != null){
			Date endDate = ((XMLGregorianCalendar) criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_END_DATE_PARAM)).toGregorianCalendar().getTime();
			dc.add(Expression.le("date", endDate));		
		}
		
		Integer organizationId = (Integer) criteria.get(IReportsWsClientConstant.AUDIT_EVENTS_REPORT_ORGANISATION_ID_PARAM);
		if (organizationId != null){
			dc.add(Restrictions.eq("organisationId", organizationId));
		}

		List<AuditCmBean> res = (List<AuditCmBean>)getHibernateTemplate().findByCriteria(dc);
		logger.debug("getAuditBeanForAuditEventsReportDataSource - END");
		return res;
	}
	
	/**
	 * Delete AuditCmBean identifed by it's id with all the components
	 * 
	 * Return the AuditCmBean that has been deleted
	 * @author coni
	 */
	
	public AuditCmBean delete(Integer auditId){
		logger.debug("delete - START");
		AuditCmBean auditCmBean = getForDelete(auditId);
		logger.debug("Deleting the audit cm event : " + auditCmBean);
		getHibernateTemplate().delete(IModelConstant.auditcmEntity, auditCmBean);
		logger.debug("Audit CM Event : " + auditCmBean + " has been deleted");
		logger.debug("delete - END");
		return auditCmBean;
	}
	
	/**
	 * Returns a cm audit event in AuditCmBean format
	 * 
	 * @author coni
	 */
	public AuditCmBean getForDelete(int auditId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(auditId)));
		
		AuditCmBean  auditCmBean = (AuditCmBean) getHibernateTemplate().get(IModelConstant.auditcmForDeleteEntity, auditId);
		
		logger.debug("getForDelete - END");
		return auditCmBean;
	}

}
