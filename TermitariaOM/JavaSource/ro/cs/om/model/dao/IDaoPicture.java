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

import ro.cs.om.entity.Picture;

/**
 * @author dd
 *
 */
public interface IDaoPicture {
	
	
	/**
	 * Returns a picture identified by it's id.
	 */
	public Picture get(Integer id);

	/**
	 * Returns a picture identified by it's name.
	 */
	public Picture getByName(String name);
	
	/**
	 * Add picture
	 * 
	 */
	@Transactional (rollbackFor=Exception.class)
	public void add(Picture picture);
	
	/**
	 * Update picture
	 * 
	 */
	@Transactional (rollbackFor=Exception.class)
	public void update(Picture picture);
	
	
	/**
	 * Delete picture
	 * 
	 */
	@Transactional (rollbackFor=Exception.class)
	public void delete(Integer pictureId);
	
	/**
	 * Get picture by personId
	 */
	
	public Integer getByPersonId(Integer personId); 
	
	/**
	 * Delete picture by personId
	 * 
	 */
	@Transactional (rollbackFor=Exception.class)
	public void deleteByPersonId(Integer personId);
	/**
	 * 
	 * Get Picture Entity By Owner
	 * @author mihai
	 * @param personId
	 * @return
	 */
	public Picture getPictureByPersonId(Integer personId);
}


