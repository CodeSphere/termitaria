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
package ro.cs.om.model.dao.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Localization;
import ro.cs.om.model.dao.IDaoLocalization;

/**
 * 
 * @author Adelina
 *
 */
public class DaoLocalizationImpl extends HibernateDaoSupport implements IDaoLocalization {

	/**
	 * Add a Localization
	 * 
	 * @author Adelina
	 */
	public void add(Localization locale){
		logger.debug("Add Localization");
		getHibernateTemplate().save(IModelConstant.localizationEntity, locale);
		logger.debug("Localization added".concat(" with id=").concat(String.valueOf(locale.getLocalizationId())));
	}
	
	/**
	 * Update an existing Localization
	 * 
	 * @author Adelina
	 * @param Localization
	 */
	public void update(Localization locale){
		logger.debug("Updating Localization");
		getHibernateTemplate().update(IModelConstant.localizationEntity, locale);
		logger.debug("Localization updated, id:".concat(String.valueOf(locale.getLocalizationId())));
	}
	
	/**
	 * Get a localization
	 * 
	 * @author Adelina
	 */
	public Localization get(Integer localizationId){
		logger.debug("Getting localization with id=".concat(String.valueOf(localizationId)));
		Localization locale = (Localization)getHibernateTemplate().get(IModelConstant.localizationEntity, new Integer(localizationId));				
		return locale;
	}
	
	/**
	 * Deletes a localization identified by its id
     *
	 * @author Adelina
	 */
	public void delete(int localizationId){
		logger.debug("delete - START id: ".concat(String.valueOf(localizationId)));
		Localization locale = new Localization();
		locale.setLocalizationId(localizationId);		
		getHibernateTemplate().delete(IModelConstant.localizationEntity, locale);
		logger.debug("delete - END");
	}
	
	/**
	 * Deletes a localization 
     *
	 * @author mitziuro
	 */
	public void delete(Localization localization){
		logger.debug("delete - START  ");		
		getHibernateTemplate().delete(IModelConstant.localizationEntity, localization);
		logger.debug("delete - END");
	}
	
	/**
	 * 
	 * Get localization just for locale 
	 * 
	 * @author mitziuro
	 * @param locale
	 * @param localizationId
	 * @return
	 */
	public Localization getByLocale(String locale, Integer localizationId){
		logger.debug("getByLocale - START locale: ".concat(locale));
		Localization localization = (Localization)getHibernateTemplate().get(IModelConstant.localizationEntity.concat(locale.toUpperCase()), localizationId);
		return localization;
	}
	
}
