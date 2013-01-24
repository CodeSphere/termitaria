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
import ro.cs.logaudit.dao.IDaoAuditDm;
import ro.cs.logaudit.entity.AuditDmBean;
import ro.cs.logaudit.entity.SearchAuditDmBean;
import ro.cs.logaudit.ws.client.reports.IReportsWsClientConstant;
import ro.cs.logaudit.ws.server.entity.AuditEventsReportGetDataCriteria;

/**
 * 
 * @author coni
 *
 */
public class DaoAuditDmImpl extends HibernateDaoSupport implements IDaoAuditDm {

	/**
	 * Searches for audit after criterion from searchBean
	 * 
	 * @author coni
	 * 
	 * @return A list of audit beans 
	 * @throws ParseException 
	 */
	public List<AuditDmBean> getAuditBeanFromSearch(SearchAuditDmBean searchAuditDmBean, boolean isDeleteAction, Locale locale) throws ParseException{
		
		logger.debug("getAuditBeanFromSearch - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.auditdmEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.auditdmEntity);
		
		if (searchAuditDmBean.getOrganisationId() != -1){
			dc.add(Restrictions.eq("organisationId", searchAuditDmBean.getOrganisationId()));
			dcCount.add(Restrictions.eq("organisationId", searchAuditDmBean.getOrganisationId()));			
		}
			
		if (searchAuditDmBean.getEvent() != null && !"-1".equals(searchAuditDmBean.getEvent())){
			dc.add(Restrictions.ilike("event", "%".concat(searchAuditDmBean.getEvent()).concat("%")));
			dcCount.add(Restrictions.ilike("event", "%".concat(searchAuditDmBean.getEvent()).concat("%")));
		}
		if (searchAuditDmBean.getPersonId() != null && !"".equals(searchAuditDmBean.getPersonId())){
			dc.add(Restrictions.eq("personId", searchAuditDmBean.getPersonId()));
			dcCount.add(Restrictions.eq("personId", searchAuditDmBean.getPersonId()));
		}
		if (searchAuditDmBean.getMessage() != null && !"".equals(searchAuditDmBean.getMessage())){
			dc.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditDmBean.getMessage()).concat("%")));
			dcCount.add(Restrictions.ilike("message".concat(locale.toString().toUpperCase()), "%".concat(searchAuditDmBean.getMessage()).concat("%")));
		}
		
		if (searchAuditDmBean.getStartDate() != null){
			dc.add(Expression.ge("date", searchAuditDmBean.getStartDate()));		
			dcCount.add(Expression.ge("date", searchAuditDmBean.getStartDate()));
		}
		
		if (searchAuditDmBean.getEndDate() != null){
			dc.add(Expression.le("date", searchAuditDmBean.getEndDate()));		
			dcCount.add(Expression.le("date", searchAuditDmBean.getEndDate()));
		}		
		
		// check if I have to order the results
		if(searchAuditDmBean.getSortParam() != null && !"".equals(searchAuditDmBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchAuditDmBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchAuditDmBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchAuditDmBean.getSortParam()));
			}
		}
		
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchAuditDmBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if (searchAuditDmBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the count(*) restriction			
			dcCount.setProjection(Projections.countDistinct("auditId"));
			
			//findByCriteria must be called with firstResult and maxResults parameters; the default findByCriteria(DetachedCriteria criteria) implementation
			//sets firstResult and maxResults to -1, which kills the countDistinct Projection			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			logger.debug("search results: ".concat(String.valueOf(nbrOfResults)));
			searchAuditDmBean.setNbrOfResults(nbrOfResults);
			
			// get the number of pages
			if (nbrOfResults % searchAuditDmBean.getResultsPerPage() == 0) {
				searchAuditDmBean.setNbrOfPages(nbrOfResults / searchAuditDmBean.getResultsPerPage());
			} else {
				searchAuditDmBean.setNbrOfPages(nbrOfResults / searchAuditDmBean.getResultsPerPage() + 1);
			}
			// after an audit is deleted, the same page has to be displayed;
			//only when all the roles from last page are deleted, the previous page will be shown 
			if ( isDeleteAction &&(searchAuditDmBean.getCurrentPage() > searchAuditDmBean.getNbrOfPages()) ){
				searchAuditDmBean.setCurrentPage( searchAuditDmBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchAuditDmBean.setCurrentPage(1);
			}

		}
		
		List<AuditDmBean> res = (List<AuditDmBean>)getHibernateTemplate().findByCriteria(dc, (searchAuditDmBean.getCurrentPage()-1) * searchAuditDmBean.getResultsPerPage(), searchAuditDmBean.getResultsPerPage());				
		logger.debug("getAuditBeanFromSearch - END results size : ".concat(String.valueOf(res.size())));
		return res;				
	}
	
	/**
	 * Adds a new DM audit event
	 * 
	 * @author coni
	 * @param auditDmBean
	 */
	public void add(AuditDmBean auditDmBean){
		logger.debug("Adding DM Audit Event");
		getHibernateTemplate().save(IModelConstant.auditdmEntity, auditDmBean);
		logger.debug("New DM Audit Event added");
	}
	
    /**
     * @author coni
     * @param getDataCriteria
     * Gets data for the Audit Events report data source request
     */
	public List<AuditDmBean> getAuditBeanForAuditEventsReportDataSource(AuditEventsReportGetDataCriteria getDataCriteria) {
		logger.debug("getAuditBeanForAuditEventsReportDataSource - START");
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.auditdmReportsEntity);	
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

		List<AuditDmBean> res = (List<AuditDmBean>)getHibernateTemplate().findByCriteria(dc);
		logger.debug("getAuditBeanForAuditEventsReportDataSource - END");
		return res;
	}
	
	/**
	 * Delete AuditDmBean identifed by it's id with all the components
	 * 
	 * Return the AuditDmBean that has been deleted
	 * @author coni
	 */
	
	public AuditDmBean delete(Integer auditId){
		logger.debug("delete - START");
		AuditDmBean auditDmBean = getForDelete(auditId);
		logger.debug("Deleting the audit Dm event : " + auditDmBean);
		getHibernateTemplate().delete(IModelConstant.auditdmEntity, auditDmBean);
		logger.debug("Audit DM Event : " + auditDmBean + " has been deleted");
		logger.debug("delete - END");
		return auditDmBean;
	}
	
	/**
	 * Returns an dm audit event in AuditDmBean format
	 * 
	 * @author coni
	 */
	public AuditDmBean getForDelete(int auditId) {
		logger.debug("getForDelete - START - id=".concat(String.valueOf(auditId)));
		AuditDmBean  auditDmBean = (AuditDmBean) getHibernateTemplate().get(IModelConstant.auditdmForDeleteEntity, auditId);
		logger.debug("getForDelete - END");
		return auditDmBean;
	}
}
