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

import ro.cs.om.entity.Logo;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoLogo;

/**
 * @author dd
 *
 */
public class BLLogo extends BusinessLogic {
	
	//singleton implementation
    private static BLLogo theInstance = null;
    private IDaoLogo logoDao = DaoBeanFactory.getInstance().getDaoLogo();
    
    private BLLogo(){};
    static {
        theInstance = new BLLogo();
    }
    public static BLLogo getInstance() {
    	return theInstance;
    }

    
    /**
     * Returns a logo identified by the it's id
     */
    public Logo get(int id) throws BusinessException{
    	Logo logo = null;
    	try{
    		logo = logoDao.get(id);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.LOGO_GET, bexc);
    	}
    	return logo;
    }
    
    /**
     * Returns a logo identified by the it's name
     */
    public Logo getByOrganisationId(Integer organisationId) throws BusinessException{
    	Logo logo = null;
    	try{
    		logo = logoDao.getByOrganisationId(organisationId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.LOGO_GET_BY_ORGANISATION_ID, bexc);
    	}
    	return logo;
    }
    
    /**
     * Add logo
     */
    public void add(Logo logo) throws BusinessException {
    	try{
    		logoDao.add(logo);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.LOGO_ADD, bexc);
    	}
    }
    
    /**
     * Update logo
     */
    public void update(Logo logo) throws BusinessException {
    	try{
    		logoDao.update(logo);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.LOGO_UPDATE, bexc);
    	}
    }
    
    /**
     * Delete logo
     */
    public void delete(Integer logoId) throws BusinessException {
    	try{
    		logoDao.delete(logoId);
    	} catch(Exception bexc){
    		throw new BusinessException(ICodeException.LOGO_DELETE, bexc);
    	}
    }
    
}
