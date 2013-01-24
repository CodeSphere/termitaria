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
package ro.cs.cm.common;


/**
 * @author coni
 * @author Adelina
 *
 */
public class PermissionConstant {
	
	
	////////////////////////////////CM////////////////////////////////
	
	// the basic permission from the CM module
	private final String CM_Basic							= "CM_Basic";
			
	//gives the permission to add a client
	private final String CM_ClientAdd						= "CM_ClientAdd";
	
	//gives the permission to edit a client					
	private final String CM_ClientUpdate					= "CM_ClientUpdate";
	
	//gives the permission to delete a client
	private final String CM_ClientDelete					= "CM_ClientDelete";
	
	// gives the permission to advanced add a project
	private final String CM_ProjectAdvancedAdd				= "CM_ProjectAdvancedAdd";
	
	// gives the permission to advanced search a project
	private final String CM_ProjectAdvancedSearch			= "CM_ProjectAdvancedSearch";
	
	//gives the permission to delete projects
	private final String CM_ProjectAdvancedDelete			= "CM_ProjectAdvancedDelete";
		
	/////////////////////////////// SUPER //////////////////////////////
	private final String _Super								= "Super";

	//singleton implementation
    private static PermissionConstant theInstance = null;
    static {
        theInstance = new PermissionConstant();
    }
    public static PermissionConstant getInstance() {
        return theInstance;
    }
    
    // getters(for jsp calls)
	public String get_Super() {
		return _Super;
	}

	public String getCM_Basic() {
		return CM_Basic;
	}

	public static PermissionConstant getTheInstance() {
		return theInstance;
	}

	public String getCM_ClientAdd() {
		return CM_ClientAdd;
	}

	public String getCM_ClientUpdate() {
		return CM_ClientUpdate;
	}

	public String getCM_ClientDelete() {
		return CM_ClientDelete;
	}

	/**
	 * @return the cM_ProjectAdvancedAdd
	 */
	public String getCM_ProjectAdvancedAdd() {
		return CM_ProjectAdvancedAdd;
	}

	/**
	 * @return the cM_ProjectAdvancedSearch
	 */
	public String getCM_ProjectAdvancedSearch() {
		return CM_ProjectAdvancedSearch;
	}

	public String getCM_ProjectAdvancedDelete() {
		return CM_ProjectAdvancedDelete;
	}
				 
}
