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

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ro.cs.om.common.IModelConstant;
import ro.cs.om.entity.Logo;
import ro.cs.om.model.dao.IDaoLogo;

/**
 * @author dan.damian
 *
 */
public class DaoLogoImpl  extends HibernateDaoSupport implements IDaoLogo {

	/**
	 * Get Logo
	 */
	public Logo get(Integer id) {
		logger.debug("Getting Logo with id = ".concat(Integer.toString(id)));
		return (Logo) getHibernateTemplate().get(IModelConstant.logoEntity, new Integer(id));
	}

	/**
	 * Add Logo
	 * 
	 */
	public void add(Logo logo) {
		logger.debug("Add Logo");
		getHibernateTemplate().save(IModelConstant.logoEntity, logo);
		logger.debug("New Logo added");
	}

	/**
	 * Update Logo
	 * 
	 */
	public void update(Logo logo) {
		logger.debug("Updating Logo");
		getHibernateTemplate().update(IModelConstant.logoEntity, logo);
		logger.debug("Logo update");
	}	

	/**
	 * Delete Logo
	 * 
	 */
	public void delete(Integer logoId) {
		logger.debug("Deleting Logo with id: ".concat(String.valueOf(logoId)));
		Logo logo = new Logo();
		logo.setLogoId(logoId);
		getHibernateTemplate().delete(IModelConstant.logoEntity, logo);
		logger.debug("Logo is deleted");
	}

	/**
	 * Returns a Logo identified by it's organisationId
	 */
	public Logo getByOrganisationId(Integer organisationId) {
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.logoEntity).append(" where organisationId = ?");
		List<Logo> logos = (List<Logo>)getHibernateTemplate().find(query.toString(), organisationId);
		if (logos.size() > 0) {
			return logos.get(0);
		} else {
			return null;
		}
	}

}
