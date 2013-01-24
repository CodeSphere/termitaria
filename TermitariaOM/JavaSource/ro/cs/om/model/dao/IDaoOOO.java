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

import java.text.ParseException;
import java.util.List;

import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchOOOBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.entity.OutOfOfficeWeb;

/**
 * @author alu
 *
 */
public interface IDaoOOO {

	/**
	 * Returns a list with out of office profiles for the given person
	 *
	 * @author alu
	 * 
	 * @param personId
	 * @return
	 */
	public List<OutOfOffice> getByPersonID(int personId);
	
	/**
     * Returns a list of persons that figure as replacements in at least one OOO profile
     * Receives as param a list of persons ids to look for
     *
     * @author coni
     * 
     * @param personReplacementId
     */
	public List<Person> getOOOPersonReplacementsFromIds(Integer[] personReplacementId);
	
	/**
     * Return an instance of ooo web for the given ID
     *
     * @author alu
     * 
     * @param oooId
     * @return
     */
    public OutOfOfficeWeb getOOOWebByID(int oooId);
    
    /**
     * Updates given out of office profile
     *
     * @author alu
     * 
     * @param oooWeb
     * @throws BusinessException
     */
    public void updateOOOweb(OutOfOfficeWeb oooWeb);
    
    /**
     * Adds given out of office profile
     *
     * @author alu
     * 
     * @param oooWeb
     * @throws BusinessException
     */
    public void addOOOweb(OutOfOfficeWeb oooWeb);

	/**
	 * Searches for out of office profiles after criterion from searchOOOBean
	 * @author alu
	 * @return A list of ooo beans 
	 * @throws ParseException 
	 */
	public List getOOOBeanFromSearch(SearchOOOBean searchOOOBean, boolean isDeleteAction) throws ParseException;
	
	/**
	 * Deletes ooo profile identified by it's id
	 *
	 * @author alu
	 * 
	 * @param oooId
	 */
	public void delete(int oooId);
		
	
	/**
     * Returns all the ooo profiles
     * 
     * @author Adelina
     * 
     * @return List<OutOfOffice>
     */

	public List<OutOfOffice> getAllOOO();

}
