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

import ro.cs.om.entity.FreeDay;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoCalendar;
import ro.cs.om.model.dao.IDaoFreeDay;

/**
 * Singleton which expose business methods for Freeday item
 *
 * @author matti_joona
 */
public class BLFreeday extends BusinessLogic {

	private static IDaoFreeDay freeDayDao = DaoBeanFactory.getInstance().getDaoFreeDay();
	private static IDaoCalendar calendarDao = DaoBeanFactory.getInstance().getDaoCalendar();
	//singleton implementation
	private static BLFreeday theInstance = null;
	private BLFreeday(){};
	static {
		theInstance = new BLFreeday();
	}
	public static BLFreeday getInstance() {
		return theInstance;
	}
	
	/**
	 * Add a FreeDay
	 * 
	 * @author Adelina
	 * @param FreeDay
	 * @throws BusinessException 
	 */
	public void add(FreeDay freeday)throws BusinessException{
    	logger.debug("add - START ");
    	try{
    		freeDayDao.add(freeday);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.FREEDAY_ADD, e);
    	}
    	logger.debug("add - END");
    }
	
	/**
	 * Update an existing FreeDay
	 * 
	 * @author Adelina
	 * @param FreeDay
	 * @throws BusinessException 
	 */
	public void update(FreeDay freeday) throws BusinessException{
		logger.debug("update - START ");
    	try{
    		freeDayDao.update(freeday);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.FREEDAY_UPDATE, e);
    	}
    	logger.debug("update - END");
	}
		
	
	/**
	 * Get a freeday
	 * 
	 * @author Adelina
	 * @param freedayId
	 * @throws BusinessException 
	 */
	public FreeDay get(Integer freedayId)throws BusinessException{
    	logger.debug("get - START - freedayId: ".concat(String.valueOf(freedayId)));
    	FreeDay freeday = null;
    	try{
        	freeday = freeDayDao.get(freedayId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.FREEDAY_GET, e);
    	}
    	logger.debug("get - END");
    	return freeday;
    }
		
	/**
	 * Deletes a freeday identified by its id
     *
	 * @author Adelina
	 * @param freedayId
	 * @throws BusinessException
	 */
	 public void delete(int freedayId) throws BusinessException{
    	logger.debug("delete - START freedayId:".concat(String.valueOf(freedayId)));
    	try{
    		freeDayDao.delete(freedayId);
    	} catch(Exception e){
    		throw new BusinessException(ICodeException.FREEDAY_DELETE, e);
    	}
    	logger.debug("delete - END");
    }
	
	/**
	 * Gets the list of free days for a calendar with a calendarId
	 * 
	 * @author Adelina
	 * @parm calendarId
	 * @return List<FreeDay>
	 * @throws BusinessException 
	 */
	public List<FreeDay> getFreeDaysByCalendar(Integer calendarId) throws BusinessException {
		logger.debug("getFreeDaysByCalendar - START");
		
		List<FreeDay> freeDays = null;		
		try{		
			freeDays = freeDayDao.getFreeDaysByCalendar(calendarId);			
		}catch(Exception e){
			throw new BusinessException(ICodeException.FREEDAY_GET_BY_CALENDAR, e);
		}		
		logger.debug("getFreeDaysByCalendar - END");
		return freeDays;
				
	}	
	 
	/**
	 * Retrieves the list of the freedays by organisation
	 *
	 * @author mitziuro
	 * 
	 * @param organisationId
	 * @return
	 * @throws BusinessException
	 */
    public List<FreeDay> getByOrganisation(int organisationId) throws BusinessException{
    	logger.debug("getByOrganisation - START");
		
    	Integer calendarId = null;
		List<FreeDay> freeDays = null;		
		try{		
			calendarId = calendarDao.getCalendarByOrganisation(organisationId).getCalendarId();
			freeDays = freeDayDao.getFreeDaysByCalendar(calendarId);			
		}catch(Exception e){
			throw new BusinessException(ICodeException.FREEDAY_GET_BY_ORGANISATION, e);
		}	
		
		logger.debug("getByOrganisation - END");
		return freeDays;
    }
}
