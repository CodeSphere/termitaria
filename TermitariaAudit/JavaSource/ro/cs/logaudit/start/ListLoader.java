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
package ro.cs.logaudit.start;

import java.util.HashMap;
import java.util.Map;

import ro.cs.logaudit.common.ApplicationObjectSupport;
import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.context.AuditContext;


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
         	AuditContext.storeOnContext(IConstant.NOM_RESULTS_PER_PAGE, nom);
         	logger.debug(IConstant.NOM_RESULTS_PER_PAGE.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating results per page classified list!", ex);
		}
	}	
	
	/**
	 * Modules classified list
	 *
	 * @author alu
	 *
	 */
	public void load_nom_module() {
		try{
			Map<Integer, String> nom = new HashMap<Integer, String>();
			nom.put(IConstant.NOM_MODULE_DM_LABEL_KEY, IConstant.NOM_MODULE_DM);
			nom.put(IConstant.NOM_MODULE_OM_LABEL_KEY, IConstant.NOM_MODULE_OM);
         	AuditContext.storeOnContext(IConstant.NOM_MODULES, nom);
         	logger.debug(IConstant.NOM_MODULES.concat(" LOADED"));
		}catch(Exception ex){
			logger.error("Error at creating module classified list!", ex);
		}
	}
	
	/**
	 * @author coni
	 * OM audit events classified list
	 */
	public void load_nom_om_audit_events() {
		try{
			String[] nom = IConstant.AUDIT_EVENTS_OM;
			AuditContext.storeOnContext(IConstant.NOM_AUDIT_EVENTS_OM, nom);
			logger.debug(IConstant.NOM_AUDIT_EVENTS_OM.concat(" LOADED"));
		} catch (Exception e){
			logger.error("Error at creating OM audit events classified list", e);
		}
	}
	
	/**
	 * @author coni
	 * DM audit events classified list
	 */
	public void load_nom_dm_audit_events() {
		try{
			String[] nom = IConstant.AUDIT_EVENTS_DM;
			AuditContext.storeOnContext(IConstant.NOM_AUDIT_EVENTS_DM, nom);
			logger.debug(IConstant.NOM_AUDIT_EVENTS_DM.concat(" LOADED"));
		} catch (Exception e){
			logger.error("Error at creating DM audit events classified list", e);
		}
	}
	
	/**
	 * @author coni
	 * CM audit events classified list
	 */
	public void load_nom_cm_audit_events() {
		try{
			String[] nom = IConstant.AUDIT_EVENTS_CM;
			AuditContext.storeOnContext(IConstant.NOM_AUDIT_EVENTS_CM, nom);
			logger.debug(IConstant.NOM_AUDIT_EVENTS_CM.concat(" LOADED"));
		} catch (Exception e){
			logger.error("Error at creating CM audit events classified list", e);
		}
	}
	
	/**
	 * @author coni
	 * TS audit events classified list
	 */
	public void load_nom_ts_audit_events() {
		try{
			String[] nom = IConstant.AUDIT_EVENTS_TS;
			AuditContext.storeOnContext(IConstant.NOM_AUDIT_EVENTS_TS, nom);
			logger.debug(IConstant.NOM_AUDIT_EVENTS_TS.concat(" LOADED"));
		} catch (Exception e){
			logger.error("Error at creating TS audit events classified list", e);
		}
	}
}
