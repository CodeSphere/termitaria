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

import org.springframework.transaction.annotation.Transactional;

import ro.cs.om.entity.Localization;

public interface IDaoLocalization {
	

	/**
	 * Add a Localization
	 * 
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(Localization locale);
	
	/**
	 * Update an existing Localization
	 * 
	 * @author Adelina
	 * @param Localization
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(Localization freeday);
	
	/**
	 * Get a localization
	 * 
	 * @author Adelina
	 */
	public Localization get(Integer localizationId);
	
	/**
	 * Deletes a localization identified by its id
     *
	 * @author Adelina
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(int localizationId);
	/**
	 * Get localization just for locale 
	 * 
	 * @author mitziuro
	 */
	public Localization getByLocale(String locale, Integer localizationId);
	/**
	 * Deletes a localization 
     *
	 * @author mitziuro
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(Localization localization);
}
