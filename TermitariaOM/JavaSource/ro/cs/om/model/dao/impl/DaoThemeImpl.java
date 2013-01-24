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
import ro.cs.om.entity.Theme;
import ro.cs.om.model.dao.IDaoTheme;

/**
 * @author dd
 *
 */
public class DaoThemeImpl  extends HibernateDaoSupport implements IDaoTheme {

	/**
	 * Get theme
	 */
	public Theme get(Integer id) {
		logger.debug("Getting Theme with id = ".concat(Integer.toString(id)));
		return (Theme) getHibernateTemplate().get(IModelConstant.themeEntity, new Integer(id));
	}

	/**
	 * Add theme
	 * 
	 */
	public void add(Theme theme) {
		logger.debug("Add Theme");
		getHibernateTemplate().save(IModelConstant.themeEntity, theme);
		logger.debug("New Theme Added");
	}

	/**
	 * Update theme
	 * 
	 */
	public void update(Theme picture) {
		logger.debug("Updating theme");
		getHibernateTemplate().update(IModelConstant.themeEntity, picture);
		logger.debug("Theme update");
	}	

	/**
	 * Delete theme
	 * 
	 */
	public void delete(Integer themeId) {
		logger.debug("Deleting Theme with id: ".concat(String.valueOf(themeId)));
		Theme theme = new Theme();
		theme.setThemeId(themeId);
		getHibernateTemplate().delete(IModelConstant.themeEntity, theme);
		logger.debug("Theme is deleted");
	}

	/**
	 * Returns a theme identified by it's code.
	 */
	public Theme getByCode(String code) {
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.themeEntity).append(" where code = ?");
		List<Theme> themes = (List<Theme>)getHibernateTemplate().find(query.toString(), code);
		if (themes.size() > 0) {
			return themes.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns all themes in the data base. 
	 * 
	 * @author dd
	 */
	@SuppressWarnings("unchecked")
	
	public List<Theme> list(){
		logger.debug("List all themes");
		List<Theme> themes = getHibernateTemplate().find("from ".concat(IModelConstant.themeEntity));
		logger.debug(Integer.toString(themes.size()).concat(" themes"));
		return themes;
	}
}
