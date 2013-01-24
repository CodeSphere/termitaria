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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author dan.damian
 * @author matti_joona
 *
 */
public class ConfigParametersProvider {
	
	public static final String ROOT_LOCATION = "/config";
	private static final String BUNDLE_ROOT = "config."; 

	private static ResourceBundle RESOURCE_BUNDLE = null;

	/*
	 * @author matti_joona
	 * 
	 * Get a config string from config.properties
	 */
	public static String getConfigString(String key) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_ROOT.concat("config"));
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
			
	public static String getString(String BUNDLE_NAME, String key) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_ROOT.concat(BUNDLE_NAME));
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
