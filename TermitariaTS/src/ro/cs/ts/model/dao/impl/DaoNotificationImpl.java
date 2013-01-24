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
package ro.cs.ts.model.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.ts.common.IModelConstant;
import ro.cs.ts.entity.Notification;
import ro.cs.ts.entity.SearchNotificationBean;
import ro.cs.ts.model.dao.IDaoNotification;
import ro.cs.ts.utils.MailUtils;

/**
 * Dao class for Notification Entity
 * 
 * @author alu
 * 7 Jan 2010
 */

public class DaoNotificationImpl extends HibernateDaoSupport implements IDaoNotification{
	
	public void add(Notification notification){
		logger.debug("add - START");
		
		getHibernateTemplate().save(IModelConstant.notificationEntity, notification);
		
		logger.debug("add - END");
	}

	public void delete(int notificationId) {
		logger.debug("delete START - Deleting notification id:".concat(String
				.valueOf(notificationId)));
		
		Notification notification = new Notification();
		notification.setNotificationId(notificationId);
		// delete the notification
		getHibernateTemplate().delete(IModelConstant.notificationEntity,
				notification);
	
		logger.debug("delete END");
	}
	
	public void deleteAll(int receiverId){
		logger.debug("deleteAll - START - receiverId:".concat(String.valueOf(receiverId)));	
		
		String hquery = "delete from ".concat(IModelConstant.notificationEntity).concat(" where receiverId=").concat(Integer.toString(receiverId));
		logger.debug("HQL: ".concat(hquery));
		Session session = getHibernateTemplate().getSessionFactory().openSession();
			session.createQuery(hquery).executeUpdate();
			session.close();
		
		logger.debug("deleteAll END");	
	}
	
	public List<Notification> getNotificationsFromSearch(SearchNotificationBean searchNotificationBean, boolean isDeleteAction){
		logger.debug("getNotificationsFromSearch - START ");
		
		/*Once a Projection is being set to a Detached Criteria object, it cannot be removed anymore, so two identical DetachedCriteria objects 
		must be created: 
		-dcCount ( on which the projection is being set )used to retrieve the number of distinct results which is set when 
		the request didn't come from the pagination area and needed further more to set the current page after a delete action; 
		-dc used to retrieve the result set after the current page has been set in case of a delete action
		*/
		
		// set search criterion
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.notificationEntity);
		DetachedCriteria dcCount = DetachedCriteria.forEntityName(IModelConstant.notificationEntity);		
		
		dc.add(Restrictions.eq("receiverId", searchNotificationBean.getReceiverId()));
		dcCount.add(Restrictions.eq("receiverId", searchNotificationBean.getReceiverId()));	
		
		if (searchNotificationBean.getStartDate() != null){
			dc.add(Expression.ge("issuedDate", searchNotificationBean.getStartDate()));		
			dcCount.add(Expression.ge("issuedDate", searchNotificationBean.getStartDate()));
		}
		
		if (searchNotificationBean.getEndDate() != null){
			dc.add(Expression.le("issuedDate", searchNotificationBean.getEndDate()));		
			dcCount.add(Expression.le("issuedDate", searchNotificationBean.getEndDate()));
		}
		
		if (searchNotificationBean.getMessage() != null && !"".equals(searchNotificationBean.getMessage())){
			dc.add(Restrictions.ilike("message", "%".concat(searchNotificationBean.getMessage()).concat("%")));
			dcCount.add(Restrictions.ilike("message", "%".concat(searchNotificationBean.getMessage()).concat("%")));
		}
		
		// check if I have to order the results
		if(searchNotificationBean.getSortParam() != null && !"".equals(searchNotificationBean.getSortParam())) {
			// if I have to, check if I have to order them ascending or descending
			if (searchNotificationBean.getSortDirection() == -1) {
				// ascending
				dc.addOrder(Order.asc(searchNotificationBean.getSortParam()));
			} else {
				// descending
				dc.addOrder(Order.desc(searchNotificationBean.getSortParam()));
			}
		}
			
		// if the request didn't come from the pagination area, 
		// it means that I have to set the number of results and pages
		if (isDeleteAction || searchNotificationBean.getNbrOfResults() == -1){
			boolean isSearch = false;
			if ( searchNotificationBean.getNbrOfResults() == -1 ) {
				isSearch = true;
			}
			// set the countDistinct restriction
			dcCount.setProjection(Projections.distinct(Projections.countDistinct(IModelConstant.notificationId)));
			
			int nbrOfResults = ((Integer)getHibernateTemplate().findByCriteria(dcCount,0,0).get(0)).intValue();
			searchNotificationBean.setNbrOfResults(nbrOfResults);
			logger.debug("NbrOfResults " + searchNotificationBean.getNbrOfResults());
			logger.debug("----> searchOrganisationBean.getResults " + searchNotificationBean.getResultsPerPage());
			// get the number of pages
			if (nbrOfResults % searchNotificationBean.getResultsPerPage() == 0) {
				searchNotificationBean.setNbrOfPages(nbrOfResults / searchNotificationBean.getResultsPerPage());
			} else {
				searchNotificationBean.setNbrOfPages(nbrOfResults / searchNotificationBean.getResultsPerPage() + 1);
			}
			// after a notification is deleted, the same page has to be displayed;
			// only when all the notifications from last page are deleted, the previous page will be shown 
			if ( isDeleteAction && (searchNotificationBean.getCurrentPage() > searchNotificationBean.getNbrOfPages()) ){
				searchNotificationBean.setCurrentPage( searchNotificationBean.getNbrOfPages() );
			} else if ( isSearch ) {
				searchNotificationBean.setCurrentPage(1);
			}
		}
		
		List<Notification> res = getHibernateTemplate().findByCriteria(dc, Long.valueOf((searchNotificationBean.getCurrentPage()-1) * searchNotificationBean.getResultsPerPage()).intValue(), Long.valueOf(searchNotificationBean.getResultsPerPage()).intValue());
		if (res != null) {
			logger.debug("results size : ".concat(String.valueOf(res.size())));
		} else {
			logger.debug("results size: 0");
		}
		
		logger.debug("getNotificationsFromSearch - END");
		return res;
	}
	
	/**
	 * Sends the given notification to the receiver
	 * 
	 * @author alu
	 * 
	 * @param notification
	 * @param receiverMail
	 * @throws Exception
	 */
	public void sendNotification(Notification notification, String receiverMail) throws Exception {
		logger.debug("sendNotification BEGIN");		
		MailUtils.getInstance().sendMail(receiverMail, notification.getSubject(), notification.getMessage());
		logger.debug("sendNotification END");
	}

}
