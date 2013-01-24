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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author Adelina
 *
 */
public class MessageProvider {

private static String RESOURCE_BUNDLE  = "config.messages";
	
	private static ResourceBundle rb = null;
	
	
	public static String getMessage(String key, Locale locale) {
		if (key == null) {
			return "!key=null!";
		}
		try{
			rb = ResourceBundle.getBundle(RESOURCE_BUNDLE + "_" + locale.getLanguage());
			return rb.getString(key);
		}catch(MissingResourceException mse) {
			return "!" + key + "!";
		}
	}
	
	public static String getConfigProperty(String bundle, String key) {
		if (key == null) {
			return "!key=null!";
		}
		try{
			rb = ResourceBundle.getBundle("config." + bundle);
			return rb.getString(key);
		}catch(MissingResourceException mse) {
			return "!" + key + "!";
		}
	}
	
}
