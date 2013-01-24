/*******************************************************************************
 * This file is part of Termitaria, a project management tool 
 *    Copyright (C) 2008-2013 CodeSphere S.R.L., www.codesphere.ro
 *     
 *    Termitaria is free software; you can redistribute it and/or 
 *    modify it under the terms of the GNU Affero General Public License 
 *    as published by the Free Software Foundation; either version 3 of 
 *    the License, or (at your option) any later version.
 *    
 *    This program is distributed in the hope that it will be useful, 
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *    GNU Affero General Public License for more details.
 *    
 *    You should have received a copy of the GNU Affero General Public License 
 *    along with Termitaria. If not, see  <http://www.gnu.org/licenses/> .
 ******************************************************************************/
package ro.cs.ts.common;

/**
 * 
 * @author Adelina
 *
 */
public class ExceptionConstant {
	
	////////////////////////////////AUTENTICATION ////////////////////////////////

	// the basic permission from the organisation management module
	private final String OM_BAD_CREDENTIALS							= "Bad credentials";
	
	// gives permission to  see all the branches in an organisation
	private final String OM_SESSION_EXCEED							= "Maximum sessions of 1 for this principal exceeded";
	
	// standard Spring Security message when a login exception arise caused by an attempt to login with a disabled user
	private final String OM_USER_DISABLED								= "User is disabled";
	
	//singleton implementation
    private static ExceptionConstant theInstance = null;
    static {
        theInstance = new ExceptionConstant();
    }
    public static ExceptionConstant getInstance() {
        return theInstance;
    }
	
	private ExceptionConstant(){
	}

    // getters(for jsp calls)
	
	/**
	 * @return the oM_BAD_CREDENTIALS
	 */
	public String getOM_BAD_CREDENTIALS() {
		return OM_BAD_CREDENTIALS;
	}

	/**
	 * @return the oM_SESSION_EXCEED
	 */
	public String getOM_SESSION_EXCEED() {
		return OM_SESSION_EXCEED;
	}
	
	/**
	 * @return the oM_SESSION_EXCEED
	 */
	public String getOM_USER_DISABLED() {
		return OM_USER_DISABLED;
	}
		
}
