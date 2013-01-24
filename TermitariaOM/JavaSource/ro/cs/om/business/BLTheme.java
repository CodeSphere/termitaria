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

import java.util.List;

import ro.cs.om.entity.Theme;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoTheme;

/**
 * @author dd
 *
 */
public class BLTheme extends BusinessLogic {
	
	//singleton implementation
    private static BLTheme theInstance = null;
    private IDaoTheme themeDao = DaoBeanFactory.getInstance().getDaoTheme();
    
    private BLTheme(){};
    static {
        theInstance = new BLTheme();
    }
    public static BLTheme getInstance() {
    	return theInstance;
    }
    
    /**
     * Returns a theme identified by the it's id
     */
    public Theme get(int id) throws BusinessException{
    	Theme picture = null;
    	try{
    		picture = themeDao.get(id);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.THEME_GET, bexc);
    	}
    	return picture;
    }
    
    /**
     * Returns a theme identified by the it's code
     */
    public Theme getByCode(String code) throws BusinessException{
    	Theme theme = null;
    	try{
    		theme = themeDao.getByCode(code);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.THEME_GET_BY_CODE, bexc);
    	}
    	return theme;
    }
    
    /**
     * Add theme
     */
    public void add(Theme theme) throws BusinessException {
    	try{
    		themeDao.add(theme);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.THEME_ADD, bexc);
    	}
    }
    
    /**
     * Update theme
     */
    public void update(Theme theme) throws BusinessException {
    	try{
    		themeDao.update(theme);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.THEME_UPDATE, bexc);
    	}
    }
    
    /**
     * Delete theme
     */
    public void delete(Integer themeId) throws BusinessException {
    	try{
    		themeDao.delete(themeId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.THEME_DELETE, bexc);
    	}
    }
    
    /**
     * Returns all persons in the the database. 
     * entities.
     * 
     * @author dd
     */
    public List<Theme> list() throws BusinessException{
      	List<Theme> list = null;
    	try{
    		list = themeDao.list();
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.THEME_LIST, bexc);
    	}
    	return list;
    }
    
    
}
