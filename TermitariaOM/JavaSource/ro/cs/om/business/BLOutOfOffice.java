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

import java.text.ParseException;
import java.util.List;

import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.entity.SearchOOOBean;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.web.entity.OutOfOfficeWeb;




/**
 * Singleton which expose business methods for OutofOffice item
 *
 * @author matti_joona
 */
public class BLOutOfOffice extends BusinessLogic {

	//singleton implementation
    private static BLOutOfOffice theInstance = null;
    private BLOutOfOffice(){};
    static {
        theInstance = new BLOutOfOffice();
    }
    public static BLOutOfOffice getInstance() {
        return theInstance;
    }
    
    /**
     * Returns a list with out of office profiles for the given person
     *
     * @author alu
     * 
     * @param personId
     * @throws BusinessException
     */
    public List<OutOfOffice> getByPersonID(int personId) throws BusinessException{    	
    	logger.debug("getByPersonID - START - personId:".concat(String.valueOf(personId)));
		List<OutOfOffice> oooList = null;
		try {
			oooList = DaoBeanFactory.getInstance().getDaoOOO().getByPersonID(personId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.OOO_GET_BY_PERSON_ID, e);
		}
		logger.debug("getByPersonID - END");
		return oooList;
    }
    
	/**
     * Returns a list of persons that figure as replacements in at least one OOO profile
     * Receives as param a list of persons ids to look for
     *
     * @author coni
     * 
     * @param personReplacementId
     */
	public List<Person> getOOOPersonReplacementsFromIds(Integer[] personReplacementId) throws BusinessException{
		logger.debug("getOOOByPersonReplacementID - START");
		List<Person> list = null;
		try {
			list = DaoBeanFactory.getInstance().getDaoOOO().getOOOPersonReplacementsFromIds(personReplacementId);
		} catch (Exception e) {
			throw new BusinessException(ICodeException.OOO_GET_BY_PERSON_REPLACEMENT_ID, e);
		}
		logger.debug("getOOOByPersonReplacementID - END");
		return list;
	}
    
    /**
     * Return an instance of ooo web for the given ID
     *
     * @author alu
     * 
     * @param oooId
     * @return
     */
    public OutOfOfficeWeb getOOOWebByID(int oooId) throws BusinessException{
    	logger.debug("getOOOWebByID - START - oooId: ".concat(String.valueOf(oooId)));
    	
    	OutOfOfficeWeb oooWeb = null;
    	try {
			oooWeb = DaoBeanFactory.getInstance().getDaoOOO().getOOOWebByID(oooId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.OOO_GET_OOOWEB_BY_ID, e);
		}
		
    	logger.debug("getOOOWebByID - END");
    	return oooWeb;
    }
    
    /**
     * Updates given out of office profile
     *
     * @author alu
     * 
     * @param oooWeb
     * @throws BusinessException
     */
    public void updateOOOweb(OutOfOfficeWeb oooWeb) throws BusinessException{
    	logger.debug("update - START ");
    	try{
    		DaoBeanFactory.getInstance().getDaoOOO().updateOOOweb(oooWeb);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.OOO_UPDATE, e);
    	}
    	logger.debug("update - END");
    }
    
    /**
     * Adds given out of office profile
     *
     * @author alu
     * 
     * @param oooWeb
     * @throws BusinessException
     */
    public void addOOOweb(OutOfOfficeWeb oooWeb) throws BusinessException{
    	logger.debug("add - START ");
    	try{
    		DaoBeanFactory.getInstance().getDaoOOO().addOOOweb(oooWeb);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.OOO_ADD, e);
    	}
    	logger.debug("add - END");
    }
    
    /**
	 * Searches for out of office profiles after criterion from searchOOOBean
	 * @author alu
	 * @return A list of ooo beans 
	 * @throws ParseException 
	 */
    public List<OutOfOffice> getResultsForSearch(SearchOOOBean searchOOOBean, boolean isDeleteAction) throws BusinessException{
    	logger.debug("getResultsForSearch - START");
    	List<OutOfOffice> res = null;
    	try {
    		res = DaoBeanFactory.getInstance().getDaoOOO().getOOOBeanFromSearch(searchOOOBean, isDeleteAction);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.OOO_GET_RESULTS, e);
    	}
    	logger.debug("getResultsForSearch - END");
    	return res;
    }
    
    /**
	 * Deletes ooo profile identified by it's id
	 *
	 * @author alu
	 * 
	 * @param oooId
	 */
    public void delete(int oooId) throws BusinessException{
    	logger.debug("delete - START");
    	try {
    		DaoBeanFactory.getInstance().getDaoOOO().delete(oooId);
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.OOO_DELETE, e);
    	}
    	logger.debug("delete - END");    	
    }          
    
	/**
     * Returns all the ooo profiles
     * 
     * @author Adelina
     * 
     * @return List<OutOfOffice>
     */
    public List<OutOfOffice> getAllOOO() throws BusinessException {
    	logger.debug("BL getOOO - START - ");
    	
    	List<OutOfOffice> res = null;
    	try {
    		res = DaoBeanFactory.getInstance().getDaoOOO().getAllOOO();
    	} catch(Exception e) {
    		throw new BusinessException(ICodeException.OOO_GET_ALL, e);
    	}
    	
    	logger.debug("BL getOOO - END - ");
    	
    	return res;
    }
      
}
