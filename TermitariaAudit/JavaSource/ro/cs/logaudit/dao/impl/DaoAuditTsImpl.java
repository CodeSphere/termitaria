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
import ro.cs.logaudit.dao.IDaoAuditTs;
import ro.cs.logaudit.entity.AuditTsBean;
import ro.cs.logaudit.entity.SearchAuditTsBean;
import ro.cs.logaudit.ws.client.reports.IReportsWsClientConstant;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;

/**
 * 
 * @author Coni
 *
 */
public class DaoAuditTsImpl extends HibernateDaoSupport implements IDaoAuditTs {
	
	/**
	 * Adds a new TS audit event
	 * 
	 * @author coni
	 * @param auditTsBean
	 */
	public void add(AuditTsBean auditTsBean){
		logger.debug("Adding TS Audit Event");
		getHibernateTemplate().save(IModelConstant.audittsEntity, auditTsBean);
		logger.debug("New TS Audit Event added");
	}

	/**
	 * Searches for audit after criterion from searchBean
	 * 
	 * @author Coni
	 * 
	 * @return A list of audit beans 
	 * @throws ParseException 
	 */
	public List<AuditTsBean> getAuditBeanFromSearch(SearchAuditTsBean searchAuditTsBean, boolean isDeleteAction, Locale locale) throws ParseException{
		
		logger.debug("getAuditBeanFromSearch - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.audittsEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.audittsEntity);
		
		if (searchAuditTsBean.getOrganisationId() != -1){
			dc.add(Restrictions.eq("organisationId", searchAuditTsBean.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisationId", searchAuditTsBean.getOrganisationId()));			
		}
			
		if (searchAuditTsBean.getEvent() != null && !"-1".equals(searchAuditTsBean.getEvent())){
			dc.add(Restrictions.ilike("event", "%".concat(searchAuditTsBean.getEvent()).concat("%")));
			dcCount.add(Restrictions.ilike("event", "%".concat(searchAuditTsBean.getEvent()).concat("%")));
		}
		if (searchAuditTsBean.getPersonId() != null && !"".equals(searchAuditTsBean.getPersonId())){
			dc.add(Restrictions.eq("personId", searchAuditTsBean.getPersonId()));
			dcCount.add(Restrictions.eq("personId", searchAuditTsBean.getPersonId()));
		}
		if (searchAuditTsBean.getMessage() != null && !"".equals(searchAuditTsBean.getMessage())){
			dc.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditTsBean.getMessage()).concat("%")));
			dcCount.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditTsBean.getMessage()).concat("%")));
		}
		
		if (searchAuditTsBean.getStartDate() != null){
			dc.add(Expression.ge("date", searchAuditTsBean.getStartDate()));		
			dcCount.add(Expression.ge("date", searchAuditTsBean.getStartDate()));
		}
		
		if (searchAuditTsBean.getEndDate() != null){
			dc.add(Expression.le("date", searchAuditTsBean.getEndDate()));		
			dcCount.add(Expression.le("date", searchAuditTsBean.getEndDate()));
		}		
		
		// check if I have to order the results
		if(searchAuditTsBean.getSortParam() != null && !"".equals(searchAuditTsBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchAuditTsBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchAuditTsBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchAuditTsBean.getSortParam()));
			}
		}
		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchAuditTsBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if (searchAuditTsBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("auditId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchAuditTsBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchAuditTsBean.getResultsPerPage() == 0) {
				searchAuditTsBean.setNbrOfPages(nbrOfResults / searchAuditTsBean.getResultsPerPage());
			} else {
				searchAuditTsBean.setNbrOfPages(nbrOfResults / searchAuditTsBean.getResultsPerPage() + 1);
			}
			// after an audit is deleted, the same page has to be displayed;
			//only when all the audit events from last page are deleted, the previous page will be shown 
			if (isDeleteAction && (searchAuditTsBean.getCurrentPage() > searchAuditTsBean.getNbrOfPages()) ){
				searchAuditTsBean.setCurrentPage( searchAuditTsBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchAuditTsBean.setCurrentPage(1);
			}

		}
		
		List<AuditTsBean> res = (List<AuditTsBean>)getHibernateTemplate().findByCriteria(dc, (searchAuditTsBean.getCurrentPage()-1) * searchAuditTsBean.getResultsPerPage(), searchAuditTsBean.getResultsPerPage());				
		logger.debug("getAuditBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;				
	}
	
    /**
     * @author coni
     * @param getDataCriteria
     * Gets data for the Audit Events report data source request
     */
	public List<AuditTsBean> getAuditBeanForAuditEventsReportDataSource(AuditEventsReportGetDataCriteria getDataCriteria) {
		logger.debug("getAuditBeanForAuditEventsReportDataSource - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.audittsReportsEntity);	
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

		List<AuditTsBean> res = (List<AuditTsBean>)getHibernateTemplate().findByCriteria(dc);
		logger.debug("getAuditBeanForAuditEventsReportDataSource - END");
		return res;
	}
	
	/**
	 * Delete AuditTsBean identified by it's id with all the components
	 * 
	 * Return the AuditTsBean that has been deleted
	 * @author coni
	 */
	
	public AuditTsBean delete(Integer auditId){
		logger.debug("delete - START");
		AuditTsBean auditTsBean = getForDelete(auditId);
		logger.debug("Deleting the audit ts event : " + auditTsBean);
		getHibernateTemplate().delete(IModelConstant.audittsEntity, auditTsBean);
		logger.debug("Audit TS Event : " + auditTsBean + " has been deleted");
		logger.debug("delete - END");
		return auditTsBean;
	}
	
	/**
	 * Returns a ts audit event in AuditTsBean format
	 * 
	 * @author coni
	 */
	public AuditTsBean getForDelete(int auditId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(auditId)));
		
		AuditTsBean  auditTsBean = (AuditTsBean) getHibernateTemplate().get(IModelConstant.audittsForDeleteEntity, auditId);
		
		logger.debug("getForDelete - END");
		return auditTsBean;
	}
	
}
