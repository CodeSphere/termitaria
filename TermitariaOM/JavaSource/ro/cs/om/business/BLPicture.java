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
package ro.cs.om.business;

import ro.cs.om.entity.Picture;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoPicture;

/**
 * @author dd
 *
 */
public class BLPicture extends BusinessLogic {
	
	//singleton implementation
    private static BLPicture theInstance = null;
    private IDaoPicture personPictureDao = DaoBeanFactory.getInstance().getDaoPicture();
    
    private BLPicture(){};
    static {
        theInstance = new BLPicture();
    }
    public static BLPicture getInstance() {
    	return theInstance;
    }

    
    /**
     * Returns a picture identified by the it's id
     */
    public Picture get(int id) throws BusinessException{
    	Picture picture = null;
    	try{
    		picture = personPictureDao.get(id);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_GET, bexc);
    	}
    	return picture;
    }
    
    /**
     * Returns a picture identified by the it's name
     */
    public Picture getByName(String name) throws BusinessException{
    	Picture picture = null;
    	try{
    		picture = personPictureDao.getByName(name);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_GET_BY_NAME, bexc);
    	}
    	return picture;
    }
    
    /**
     * Add picture
     */
    public void add(Picture picture) throws BusinessException {
    	try{
    		personPictureDao.add(picture);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_ADD, bexc);
    	}
    }
    
    /**
     * Update picture
     */
    public void update(Picture picture) throws BusinessException {
    	try{
    		personPictureDao.update(picture);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_UPDATE, bexc);
    	}
    }
    
    /**
     * Delete picture
     */
    public void delete(Integer pictureId) throws BusinessException {
    	try{
    		personPictureDao.delete(pictureId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_DELETE, bexc);
    	}
    }
    
    /**
     * Returns a picture identified by the it's personId
     */
    public Integer getByPersonId(Integer personId) throws BusinessException{
    	Integer pictureId = null;
    	try{
    		pictureId = personPictureDao.getByPersonId(personId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_GET_ID_BY_ID, bexc);
    	}
    	return pictureId;
    }
    
    
    /**
     * Returns a picture identified by the it's personId
     */
    public Picture getPictureByPersonId(Integer personId) throws BusinessException{
    	Picture picture = null;
    	try{
    		picture = personPictureDao.getPictureByPersonId(personId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_GET_BY_ID, bexc);
    	}
    	return picture;
    }
    
    /**
     * Delete picture by personId
     */
    public void deleteByPersonId(Integer personId) throws BusinessException {
    	try{
    		personPictureDao.deleteByPersonId(personId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.PICTURE_DELETE_PERSON_ID, bexc);
    	}
    }
}
