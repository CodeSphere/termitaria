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
package ro.cs.om.model.dao;

import java.util.List;

import ro.cs.om.entity.Theme;

/**
 * @author dd
 *
 */
public interface IDaoTheme {
	
	
	/**
	 * Returns a theme by it's id.
	 */
	public Theme get(Integer id);

	/**
	 * Returns a theme by it's code.
	 */
	public Theme getByCode(String name);
	
	/**
	 * Add theme
	 * 
	 */
	public void add(Theme theme);
	
	/**
	 * Update theme
	 * 
	 */
	public void update(Theme theme);
	
	
	/**
	 * Delete theme
	 * 
	 */
	public void delete(Integer themeId);
	
	/**
	 * Returns all themes in the data base. 
	 * 
	 * @author dd
	 */
	public List<Theme> list();
}
