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
import ro.cs.logaudit.dao.IDaoAuditOm;
import ro.cs.logaudit.entity.AuditOmBean;
import ro.cs.logaudit.entity.SearchAuditOmBean;
import ro.cs.logaudit.ws.client.reports.IReportsWsClientConstant;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;

/**
 * @author alu
 * @author Adelina
 *
 */
public class DaoAuditOmImpl extends HibernateDaoSupport implements IDaoAuditOm {
		
	/**
	 * Searches for audit after criterion from searchBean
	 * 
	 * @author Adelina
	 * @author Coni
	 * 
	 * @return A list of audit beans 
	 * @throws ParseException 
	 */
	public List<AuditOmBean> getAuditBeanFromSearch(SearchAuditOmBean searchAuditOmBean, boolean isDeleteAction, Locale locale) throws ParseException{
		
		logger.debug("getAuditBeanFromSearch - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.auditomEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.auditomEntity);
		
		if (searchAuditOmBean.getOrganisationId() != -1){
			dc.add(Restrictions.eq("organisationId", searchAuditOmBean.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisationId", searchAuditOmBean.getOrganisationId()));			
		}
			
		if (searchAuditOmBean.getEvent() != null && !"-1".equals(searchAuditOmBean.getEvent())){
			dc.add(Restrictions.ilike("event", "%".concat(searchAuditOmBean.getEvent()).concat("%")));
			dcCount.add(Restrictions.ilike("event", "%".concat(searchAuditOmBean.getEvent()).concat("%")));
		}
		if (searchAuditOmBean.getPersonId() != null && !"".equals(searchAuditOmBean.getPersonId())){
			dc.add(Restrictions.eq("personId", searchAuditOmBean.getPersonId()));
			dcCount.add(Restrictions.eq("personId", searchAuditOmBean.getPersonId()));
		}
		if (searchAuditOmBean.getMessage() != null && !"".equals(searchAuditOmBean.getMessage())){
			dc.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditOmBean.getMessage()).concat("%")));
			dcCount.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditOmBean.getMessage()).concat("%")));
		}
		
		if (searchAuditOmBean.getStartDate() != null){
			dc.add(Expression.ge("date", searchAuditOmBean.getStartDate()));		
			dcCount.add(Expression.ge("date", searchAuditOmBean.getStartDate()));
		}
		
		if (searchAuditOmBean.getEndDate() != null){
			dc.add(Expression.le("date", searchAuditOmBean.getEndDate()));		
			dcCount.add(Expression.le("date", searchAuditOmBean.getEndDate()));
		}		
		
		// check if I have to order the results
		if(searchAuditOmBean.getSortParam() != null && !"".equals(searchAuditOmBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchAuditOmBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchAuditOmBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchAuditOmBean.getSortParam()));
			}
		}
		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchAuditOmBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if (searchAuditOmBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("auditId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchAuditOmBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchAuditOmBean.getResultsPerPage() == 0) {
				searchAuditOmBean.setNbrOfPages(nbrOfResults / searchAuditOmBean.getResultsPerPage());
			} else {
				searchAuditOmBean.setNbrOfPages(nbrOfResults / searchAuditOmBean.getResultsPerPage() + 1);
			}
			// after an audit is deleted, the same page has to be displayed;
			//only when all the audit events from last page are deleted, the previous page will be shown 
			if (isDeleteAction && (searchAuditOmBean.getCurrentPage() > searchAuditOmBean.getNbrOfPages()) ){
				searchAuditOmBean.setCurrentPage( searchAuditOmBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchAuditOmBean.setCurrentPage(1);
			}

		}
		
		List<AuditOmBean> res = (List<AuditOmBean>)getHibernateTemplate().findByCriteria(dc, (searchAuditOmBean.getCurrentPage()-1) * searchAuditOmBean.getResultsPerPage(), searchAuditOmBean.getResultsPerPage());				
		logger.debug("getAuditBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;				
	}
	
	/**
	 * Adds a new OM audit event
	 * 
	 * @author coni
	 * @param auditOmBean
	 */
	public void add(AuditOmBean auditOmBean){
		logger.debug("Adding OM Audit Event");
		getHibernateTemplate().save(IModelConstant.auditomEntity, auditOmBean);
		logger.debug("New OM Audit Event added");
	}
	
    /**
     * @author coni
     * @param getDataCriteria
     * Gets data for the Audit Events report data source request
     */
	public List<AuditOmBean> getAuditBeanForAuditEventsReportDataSource(AuditEventsReportGetDataCriteria getDataCriteria) {
		logger.debug("getAuditBeanForAuditEventsReportDataSource - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.auditomReportsEntity);	
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

		List<AuditOmBean> res = (List<AuditOmBean>)getHibernateTemplate().findByCriteria(dc);
		logger.debug("getAuditBeanForAuditEventsReportDataSource - END");
		return res;
	}
	
	/**
	 * Delete AuditOmBean identifed by it's id with all the components
	 * 
	 * Return the AuditOmBean that has been deleted
	 * @author coni
	 */
	
	public AuditOmBean delete(Integer auditId){
		logger.debug("delete - START");
		AuditOmBean auditOmBean = getForDelete(auditId);
		logger.debug("Deleting the audit om event : " + auditOmBean);
		getHibernateTemplate().delete(IModelConstant.auditomEntity, auditOmBean);
		logger.debug("Audit OM Event : " + auditOmBean + " has been deleted");
		logger.debug("delete - END");
		return auditOmBean;
	}
	
	/**
	 * Returns an om audit event in AuditOmBean format
	 * 
	 * @author coni
	 */
	public AuditOmBean getForDelete(int auditId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(auditId)));
		
		AuditOmBean  auditOmBean = (AuditOmBean) getHibernateTemplate().get(IModelConstant.auditomForDeleteEntity, auditId);
		
		logger.debug("getForDelete - END");
		return auditOmBean;
	}
}
