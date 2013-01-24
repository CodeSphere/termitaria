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

import ro.cs.om.entity.Localization;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoLocalization;

/**
 * Singleton which expose business methods for Localization item
 *
 * @author Adelina
 */

public class BLLocalization extends BusinessLogic {

	private static IDaoLocalization localizationDao = DaoBeanFactory.getInstance().getDaoLocalization();
	//singleton implementation
	private static BLLocalization theInstance = null;
	private BLLocalization(){};
	static {
		theInstance = new BLLocalization();
	}
	public static BLLocalization getInstance() {
		return theInstance;
	}
	
	/**
	 * Add a Localization
	 * 
	 * @author Adelina
	 * @throws BusinessException 
	 */
	public void add(Localization locale) throws BusinessException{
		logger.debug("Add Localization BL - START");
		try{
			localizationDao.add(locale);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.LOCALIZATION_ADD, e);
		}
		logger.debug("Add Localization BL - END");
		logger.debug("Localization added".concat(" with id=").concat(String.valueOf(locale.getLocalizationId())));
	}

	
	/**
	 * Update an existing Localization
	 * 
	 * @author Adelina
	 * @param Localization
	 * @throws BusinessException 
	 */
	public void update(Localization locale) throws BusinessException{
		logger.debug("Updating Localization BL - START");
		try{
			localizationDao.update(locale);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.LOCALIZATION_UPDATE, e);
		}
		logger.debug("Updating Localization BL - END");		
		logger.debug("Localization updated, id:".concat(String.valueOf(locale.getLocalizationId())));
	}
	
	/**
	 * Get a localization
	 * 
	 * @author Adelina
	 * @throws BusinessException 
	 */
	public Localization get(Integer localizationId) throws BusinessException{
		logger.debug("Getting localization with id=".concat(String.valueOf(localizationId)) + " - START");
		Localization locale = null;
		try{
			locale = localizationDao.get(localizationId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.LOCALIZATION_GET, e);
		}
		logger.debug("Getting localization - END");
		return locale;
		
	}
	
	/**
	 * Deletes a localization identified by its id
     *
	 * @author Adelina
	 * @throws BusinessException 
	 */
	public void delete(int localizationId) throws BusinessException{
		logger.debug("Delete - START id: ".concat(String.valueOf(localizationId)));
		try{
			localizationDao.delete(localizationId);
		} catch(Exception e) {
			throw new BusinessException(ICodeException.LOCALIZATION_DELETE, e);
		}
		logger.debug("Delete Localization BL - END");
		logger.debug("delete - END");
	}
	
	/**
	 * Get a localization by a locale
	 * 
	 * @author mitziuro
	 * @throws BusinessException 
	 */
	public Localization getByLocale(Integer localizationId,String locale) throws BusinessException{
		logger.debug("Getting localization with locale=".concat(locale) + " - START");
		Localization localization = null;
		try{
			localization = localizationDao.getByLocale(locale, localizationId);
		} catch(Exception e){
			throw new BusinessException(ICodeException.LOCALIZATION_GET, e);
		}
		logger.debug("Getting localization - END");
		return localization;
		
	}
	
}
