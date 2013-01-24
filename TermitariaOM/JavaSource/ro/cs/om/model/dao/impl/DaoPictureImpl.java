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
import ro.cs.om.entity.Picture;
import ro.cs.om.model.dao.IDaoPicture;

/**
 * @author dan.damian
 *
 */
public class DaoPictureImpl  extends HibernateDaoSupport implements IDaoPicture {

	/**
	 * Get picture
	 */
	public Picture get(Integer id) {
		logger.debug("Getting Picture with id = ".concat(Integer.toString(id)));
		return (Picture) getHibernateTemplate().get(IModelConstant.pictureEntity, new Integer(id));
	}

	/**
	 * Add picture
	 * 
	 */
	public void add(Picture picture) {
		logger.debug("Add Picture");
		getHibernateTemplate().save(IModelConstant.pictureEntity, picture);
		logger.debug("New Picture added");
	}

	/**
	 * Update picture
	 * 
	 */
	public void update(Picture picture) {
		logger.debug("Updating picture");
		getHibernateTemplate().update(IModelConstant.pictureEntity, picture);
		logger.debug("Picture update");
	}	

	/**
	 * Delete picture
	 * 
	 */
	public void delete(Integer pictureId) {
		logger.debug("Deleting picture with id: ".concat(String.valueOf(pictureId)));
		Picture picture = new Picture();
		picture.setPictureId(pictureId);
		getHibernateTemplate().delete(IModelConstant.pictureEntity, picture);
		logger.debug("Picture is deleted");
	}

	/**
	 * Returns a picture identified by it's name.
	 */
	public Picture getByName(String name) {
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.pictureEntity).append(" where name = ?");
		List<Picture> pictures = (List<Picture>)getHibernateTemplate().find(query.toString(), name);
		if (pictures.size() > 0) {
			return pictures.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Get picture by personId
	 */
	public Integer getByPersonId(Integer personId) {
		logger.debug("Getting Picture with personId = ".concat(Integer.toString(personId)));
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.pictureEntity).append(" where personId = ?");
		List<Picture> pictures = (List<Picture>)getHibernateTemplate().find(query.toString(), personId);
		if (pictures.size() > 0) {
			return pictures.get(0).getPictureId();
		} else {
			return null;
		}
	}
	
	/**
	 * Delete picture by personId
	 * 
	 */
	public void deleteByPersonId(Integer personId) {
		logger.debug("Deleting picture, for person with id: ".concat(String.valueOf(personId)));
		Picture picture = getPictureByPersonId(personId);
		logger.debug("picture = " + picture);
		if(picture != null) {
			getHibernateTemplate().delete(IModelConstant.pictureEntity, picture);
			logger.debug("Picture is deleted");
		}
		
	}
	
	/**
	 * Get picture entity by personId
	 */
	public Picture getPictureByPersonId(Integer personId) {
		logger.debug("Getting Picture with personId = ".concat(Integer.toString(personId)));
		StringBuffer query = new StringBuffer("from ");
		query.append(IModelConstant.pictureEntity).append(" where personId = ?");
		List<Picture> pictures = (List<Picture>)getHibernateTemplate().find(query.toString(), personId);
		if (pictures.size() > 0) {
			return pictures.get(0);
		} else {
			return null;
		}
	}
}
