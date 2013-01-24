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
package ro.cs.om.common;

import java.awt.Color;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

/**
 * Provides access to config parameters defined in *.properties files in config package.
 * @author matti_joona
 */
public class ConfigParametersProvider {
	
	static Log logger = LogFactory.getLog(ConfigParametersProvider.class);
	
	public static final String ROOT_LOCATION = "/config";
	private static final String BUNDLE_ROOT = "config."; 

	private static ResourceBundle RESOURCE_BUNDLE = null;

	/**
	 * @author matti_joona
	 * 
	 * Get a config string from config.properties
	 */
	public static String getConfigString(String key) {
		return getString("config", key);
	}
	
	/**
	 * Returns a String value read from a config file
	 * @return
	 */
	public static String getString(String BUNDLE_NAME, String key) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_ROOT.concat(BUNDLE_NAME));
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	/**
	 * Returns a config string value by it's key, and null if that key it's
	 * not present.
	 *
	 * @author dd 
	 */
	public static String getConfigStringProtected(String key) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_ROOT.concat("config"));
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
	
	/**
	 * Returns a Color value read from THE config file.
	 * The Color must be in RGB form.  
	 * @author dan.damian 
	 */
	public static Color getConfigStringAsColor(String key) {
		return getStringAsColor("config", key);
	}
		
	/**
	 * Returns a Color value read from a config file.
	 * The Color must be in RGB form.  
	 * @author dan.damian 
	 */
	public static Color getStringAsColor(String _RESOURCE_BUNDLE, String key) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_ROOT.concat(_RESOURCE_BUNDLE));
		Color c = null;
		try {
			String value = RESOURCE_BUNDLE.getString(key);
			String[] numbers = StringUtils.delimitedListToStringArray(value, ",", ")][(");
			int R = Integer.parseInt(numbers[0].trim());
			int G = Integer.parseInt(numbers[1].trim());
			int B = Integer.parseInt(numbers[2].trim());
			c = new Color(R, G, B);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		return c;
	}
	
	/**
	 * Returns an int value read from THE config file.
	 * @author dan.damian 
	 */
	public static int getConfigStringAsInt(String key) {
		return getStringAsInt("config", key);
	}
	
	/**
	 * Returns an int value read from a config file.
	 * @author dan.damian 
	 */
	public static int getStringAsInt(String _RESOURCE_BUNDLE, String key) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_ROOT.concat(_RESOURCE_BUNDLE));
		int value = -1;
		try {
			value = Integer.parseInt(RESOURCE_BUNDLE.getString(key));
		} catch (Exception e) {}
		
		return value;
	}

}
