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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Generate random data
 *
 * @author matti_joona 
 */
public class RandomDataProvider {

	private final Log logger = LogFactory.getLog(getClass());
	
	private static char[] chars = new char[62];
	
	//singleton implementation
    private static RandomDataProvider theInstance = null;
    private RandomDataProvider(){};
    static{
    	theInstance = new RandomDataProvider();
    	//initializare ale caracterelor alphanumerice
        int i = 0;
        //This is for lower case characters only
        for(char c = 'a'; c <= 'z';c++) {
            chars[i++] = c;
        }
        //This is for upper case characters only
        for(char c = 'A'; c <= 'Z';c++) {
            chars[i++] = c;
        }
        for(char c = '0'; c <= '9';c++) {
            chars[i++] = c;
        }
    }
    public static RandomDataProvider getInstance() {
        return theInstance;
    }
	
    /**
     * @author matti_joona
     *
     * Generate random usernames
     */
    public String generateUserName(){
    	return generateData();    }

    /**
     * @author matti_joona
     *
     * Generate random passwords
     */
    public String generatePassword(){
    	return generateData();
    }
    
    
    
    /*
     * @author matti_joona
     * 
     * Generate data using all alpha numeric characters
     */
    public String generateData(){
    	return generateData(8);
    }
    
    public String generateData(int length) {
    	String code = new String();
	    for(int i = 0; i < length; i++) {
	        char c = chars[(int)(Math.random() * chars.length)];
	        code = code.concat(String.valueOf(c));
	    }
	    logger.debug("Generated data: ".concat(code));
	    return code;
    }
    
}
