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
package ro.cs.cm.start;

import java.util.ArrayList;
import java.util.List;

import ro.cs.cm.common.ApplicationObjectSupport;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.context.CMContext;
import ro.cs.cm.nom.IntString;


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
	 * This is a classified list for results per page(search)
	 *
	 * @author alu
	 *
	 */
	public void load_nom_resultsPerPage() {
		try{
			int[] nom = IConstant.RESULTS_PER_PAGE;
         	CMContext.storeOnContext(IConstant.NOM_RESULTS_PER_PAGE, nom);
         	logger.debug(IConstant.NOM_RESULTS_PER_PAGE.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating results per page classified list!", ex);
		}
	}	
	
	/**
	 * This is a classified list for Project's status
	 *
	 * @author Adelina 
	 * 
	 */
	public void load_nom_projectStatus() {
		try{
			List<IntString> nom = new ArrayList<IntString>(3);
			IntString el = new IntString(IConstant.NOM_PROJECT_STATUS_OPENED, IConstant.NOM_PROJECT_STATUS_OPENED_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_PROJECT_STATUS_CLOSED, IConstant.NOM_PROJECT_STATUS_CLOSED_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_PROJECT_STATUS_ABORTED, IConstant.NOM_PROJECT_STATUS_ABORTED_LABEL_KEY);
			nom.add(el);
			CMContext.storeOnContext(IConstant.NOM_PROJECT_STATUS, nom);
			logger.debug(IConstant.NOM_PROJECT_STATUS.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating Project's Status classified list!", ex);
		}
	}
	
	/**
	 * This is a classified list for Project Team's status
	 *
	 * @author Adelina 
	 * 
	 */
	public void load_nom_projectTeamStatus() {
		try{
			List<IntString> nom = new ArrayList<IntString>(2);
			IntString el = new IntString(IConstant.NOM_PROJECT_TEAM_STATUS_ACTIVATED, IConstant.NOM_PROJECT_TEAM_STATUS_ACTIVATED_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_PROJECT_TEAM_STATUS_DELETED, IConstant.NOM_PROJECT_TEAM_STATUS_DELETED_LABEL_KEY);
			nom.add(el);
			CMContext.storeOnContext(IConstant.NOM_PROJECT_TEAM_STATUS, nom);
			logger.debug(IConstant.NOM_PROJECT_TEAM_STATUS.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating Project Team's Status classified list!", ex);
		}
	}
	
	/**
	 * This is a classified list for Team member's status
	 *
	 * @author Adelina 
	 * 
	 */
	public void load_nom_teamMemberStatus() {
		try{
			List<IntString> nom = new ArrayList<IntString>(2);
			IntString el = new IntString(IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED, IConstant.NOM_TEAM_MEMBER_STATUS_ACTIVATED_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_TEAM_MEMBER_STATUS_DELETED, IConstant.NOM_TEAM_MEMBER_STATUS_DELETED_LABEL_KEY);
			nom.add(el);
			CMContext.storeOnContext(IConstant.NOM_TEAM_MEMBER_STATUS, nom);
			logger.debug(IConstant.NOM_TEAM_MEMBER_STATUS.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating Team member's Status classified list!", ex);
		}
	}
	
	/**
	 * Used to load the list for client's types 
	 * 
	 * @author Coni
	 */
	public void load_nom_clientType() {
		try{
			List<IntString> nom = new ArrayList<IntString>();
			IntString el = new IntString(IConstant.NOM_CLIENT_TYPE_FIRM, IConstant.NOM_CLIENT_TYPE_FIRM_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_CLIENT_TYPE_PERSON, IConstant.NOM_CLIENT_TYPE_PERSON_LABEL_KEY);
			nom.add(el);
			CMContext.storeOnContext(IConstant.NOM_CLIENT_TYPE, nom);
			logger.debug(IConstant.NOM_CLIENT_TYPE.concat(" LOADED"));
		} catch (Exception ex) {
			logger.error("Error at creating the client's types list", ex);
		}
	}
	
	/**
	 * Used to load the list for client's status 
	 * 
	 * @author Coni
	 */
	public void load_nom_clientStatus() {
		try{
			List<IntString> nom = new ArrayList<IntString>();
			IntString el = new IntString(IConstant.NOM_CLIENT_STATUS_ACTIVE, IConstant.NOM_CLIENT_STATUS_ACTIVE_LABEL_KEY);
			nom.add(el);
			el = new IntString(IConstant.NOM_CLIENT_STATUS_DELETED, IConstant.NOM_CLIENT_STATUS_DELETED_LABEL_KEY);
			nom.add(el);
			CMContext.storeOnContext(IConstant.NOM_CLIENT_STATUS, nom);
			logger.debug(IConstant.NOM_CLIENT_STATUS.concat(" LOADED"));
		} catch (Exception ex) {
			logger.error("Error at creating the client's status list", ex);
		}
	}

}
