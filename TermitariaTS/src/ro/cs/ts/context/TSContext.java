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
package ro.cs.ts.context;

import java.util.HashMap;

import org.springframework.context.ApplicationContext;

/**
 * Business context which store accessed objects from different layers
 *
 * @author Coni
 */

public class TSContext {
	
	public static String BUSINESS_BUNDLE				= "business";
	
	
	/**
	 * Injected from the class "ApplicationContextProvider" which is automatically
     * loaded during Spring-Initialization.
     */  
	private static ApplicationContext ctx;  
	
    /**
     * Web Archive Application Business Context
     */
	private static HashMap<String, Object> appContext = new HashMap<String, Object>();

	
	public static void storeOnContext(String name, Object value){
		appContext.put(name, value);
	}
	
	public static Object getFromContext(String name){
		return appContext.get(name);
	}

	public static void removeFromContext(String name){
		appContext.remove(name);
	}
	
	public static void clearContext(){
		appContext.clear();
	}
	
	public static HashMap<String, Object> getContext(){
		return appContext;
	}
	
    /**
     * Injected from the class "ApplicationContextProvider" which is automatically
     * loaded during Spring-Initialization.
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    /**
     * Get access to the Spring ApplicationContext from everywhere in your Application.
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return ctx;
    }
	
}
