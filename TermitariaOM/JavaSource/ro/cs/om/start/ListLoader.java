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
package ro.cs.om.start;

import java.util.ArrayList;
import java.util.List;

import ro.cs.om.business.BLModule;
import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.common.IConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Module;
import ro.cs.om.nom.IntString;


/**
 * Clasa folosita pentru incarcarea nomenclatoarelor
 *
 * @author matti_joona
 */
public class ListLoader extends ApplicationObjectSupport {

	private static ListLoader theInstance = null;
	
	static {
		theInstance = new ListLoader();
	}
	private ListLoader() {}
	
	public static ListLoader getInstance() {
		return theInstance;
	}
		
	/**
	 * Modules classified list
	 *
	 * @author alu
	 *
	 */
	public void load_nom_module() {
		try{
			List<Module> nom = BLModule.getInstance().getAllModules();
         	OMContext.storeOnContext(IConstant.NOM_MODULES, nom);
         	logger.debug(IConstant.NOM_MODULES.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating module classified list!", ex);
		}
	}
	
	/**
	 * This is a classified list for results per page(search)
	 *
	 * @author alu
	 *
	 */
	public void load_nom_resultsPerPage() {
		try{
			int[] nom = IConstant.RESULTS_PER_PAGE;
         	OMContext.storeOnContext(IConstant.NOM_RESULTS_PER_PAGE, nom);
         	logger.debug(IConstant.NOM_RESULTS_PER_PAGE.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating results per page classified list!", ex);
		}
	}
		
	/**
	 * This is a classified list for Organisation's Type
	 *
	 * @author Adelina 
	 * 
	 */
	public void load_nom_organisationType() {
		try{
			
         	List<IntString> nom = new ArrayList<IntString>(5);         	
         	IntString el = new IntString(IConstant.NOM_ORGANISATION_TYPE_COMPANY, IConstant.NOM_ORGANISATION_TYPE_COMPANY_LABEL_KEY);
         	nom.add(el);
         	el = new IntString(IConstant.NOM_ORGANISATION_TYPE_GROUP_COMPANIES, IConstant.NOM_ORGANISATION_TYPE_GROUP_COMPANIES_LABEL_KEY);
         	nom.add(el);
         	el = new IntString(IConstant.NOM_ORGANISATION_TYPE_HQ, IConstant.NOM_ORGANISATION_TYPE_HQ_LABEL_KEY);
         	nom.add(el);              	
          	el = new IntString(IConstant.NOM_ORGANISATION_TYPE_BRANCH, IConstant.NOM_ORGANISATION_TYPE_BRANCH_LABEL_KEY);
         	nom.add(el);
         	OMContext.storeOnContext(IConstant.NOM_ORGANISATION_TYPE, nom);
         	logger.debug(IConstant.NOM_ORGANISATION_TYPE.concat(" LOADED"));         	         	
		}catch(Exception ex){
			logger.error("Error at creating Organisation's Type classified list!", ex);
		}
	}
	
	/**
	 * This is a classified list for group of companies Type
	 *
	 * @author Adelina 
	 * 
	 */
	public void load_nom_group_companies_Type() {
		try{
			
         	List<IntString> nom = new ArrayList<IntString>(5);
         	
         	IntString el = new IntString(IConstant.NOM_ORGANISATION_TYPE_COMPANY, IConstant.NOM_ORGANISATION_TYPE_COMPANY_LABEL_KEY);
         	nom.add(el);         	
         	el = new IntString(IConstant.NOM_ORGANISATION_TYPE_HQ, IConstant.NOM_ORGANISATION_TYPE_HQ_LABEL_KEY);
         	nom.add(el);         	         	
         	OMContext.storeOnContext(IConstant.NOM_GROUP_COMPANIES_TYPE, nom);
         	logger.debug(IConstant.NOM_GROUP_COMPANIES_TYPE.concat(" LOADED"));         	         	
		}catch(Exception ex){
			logger.error("Error at creating group of companies Type classified list!", ex);
		}
	}	
}
