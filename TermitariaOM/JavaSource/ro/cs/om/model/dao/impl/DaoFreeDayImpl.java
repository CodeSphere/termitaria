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
/*Created on 05.03.2008*/
package ro.cs.om.model.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.FreeDay;
import ro.cs.om.model.dao.IDaoFreeDay;

/*
 * @author Adelina
 */
public class DaoFreeDayImpl extends HibernateDaoSupport implements IDaoFreeDay {

	
	/**
	 * Add a FreeDay
	 * 
	 * @author Adelina
	 */
	public void add(FreeDay freeday){
		logger.debug("Add freeday");
		getHibernateTemplate().save(IModelConstant.freeDayEntity, freeday);
		logger.debug("Freeday added".concat(" with id=").concat(String.valueOf(freeday.getFreeDayId())));
	}
	
	/**
	 * Update an existing FreeDay
	 * 
	 * @author Adelina
	 * @param FreeDay
	 */
	public void update(FreeDay freeday){
		logger.debug("Updating freeday");
		getHibernateTemplate().update(IModelConstant.freeDayEntity, freeday);
		logger.debug("Freeday updated, id:".concat(String.valueOf(freeday.getFreeDayId())));
	}
	
		
		
	/**
	 * Get a freeday
	 * 
	 * @author Adelina
	 */
	public FreeDay get(Integer freedayId){
		logger.debug("Getting freeday with id=".concat(String.valueOf(freedayId)));
		FreeDay freeday = (FreeDay) getHibernateTemplate().get(IModelConstant.freeDayEntity, new Integer(freedayId));		
		return freeday;
	}
	
	/**
	 * Deletes a freeday identified by its id
     *
	 * @author Adelina
	 */
	public void delete(int freedayId){
		logger.debug("delete - START id: ".concat(String.valueOf(freedayId)));
		FreeDay freeday = new FreeDay();
		freeday.setFreeDayId(freedayId);
		getHibernateTemplate().delete(IModelConstant.freeDayEntity, freeday);
		logger.debug("delete - END");
	}
	
		
	/**
	 * Gets the list of free days for a calendar with a calendarId
	 * 
	 * @author Adelina
	 * @parm calendarId
	 * @return List<FreeDay>
	 */
	@SuppressWarnings("unchecked")
	public List<FreeDay> getFreeDaysByCalendar(Integer calendarId) {
		logger.debug("getFreeDaysByCalendar DAO IMPL - START - ");
		
		DetachedCriteria dc = DetachedCriteria.forEntityName(IModelConstant.freeDayEntity);		
		dc.createCriteria("calendar").add(Restrictions.eq("calendarId", calendarId));		
		List freeDays = getHibernateTemplate().findByCriteria(dc);
		logger.debug("getFreeDaysByCalendar DAO IMPL - END - ".concat(String.valueOf(freeDays.size())));		
								
		return freeDays;	
	}	
}
