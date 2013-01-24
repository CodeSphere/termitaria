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
package ro.cs.logaudit.common;


/**
 * @author coni
 *
 */
public class PermissionConstant {
	
	
	////////////////////////////////AUDIT////////////////////////////////
	
	// the basic permission from the audit module
	private final String AUDIT_Basic						= "AUDIT_Basic";
	
	//------------------------------AUDIT OM--------------------------------
	//gives the permission to delete OM audit events
	private final String AUDIT_Delete						= "AUDIT_Delete";
	
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
	public String getAUDIT_Basic() {
		return AUDIT_Basic;
	}
	public String get_Super() {
		return _Super;
	}
	public String getAUDIT_Delete() {
		return AUDIT_Delete;
	}
		    
}
