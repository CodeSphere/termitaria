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
package ro.cs.om.utils.common;

import ro.cs.om.common.ApplicationObjectSupport;

public class StringUtils extends ApplicationObjectSupport{

	private static StringUtils theInstance = null;
    private StringUtils(){};
    static {
        theInstance = new StringUtils();
    }
    public static StringUtils getInstance() {
        return theInstance;
    }
    
    /**
	 * Tokenize a string
	 * 
	 * @author Adelina
	 * 
	 * @param field, numberOfCharsPerLine
	 * @return String
	 */
	private String tokenizeString(String field, Integer numberOfCharsPerLine) {
		if (field == null || field.equals("") ) return ""; 
		String [] words = null;
		String splitPattern = " ";
	    words = field.split(splitPattern);
	    
	    for (int i = 0 ; i < words.length ; i++) {	    	
	        //logger.debug("W = " + words[i]);
	        String word = words[i];	  
	        String temp = "";
	        while(word.length() > numberOfCharsPerLine) {
	        	Integer endIndex = numberOfCharsPerLine - 1;
	        	String newLine = "\n";
	        	words[i] = temp + word.substring(0, endIndex) + newLine;
	        	//logger.debug("words[i] = " + words[i]);
	        	word = word.substring(endIndex, word.length());
	        	//logger.debug("word = " + word);
	        	temp = words[i];
	        	//logger.debug("temp = " + temp);
	        }
	   
	        if(word.length() != words[i].length()) {
	        	words[i] = words[i].concat(word);
	        }
	    }
	    String result = "";
	    for(int j = 0; j < words.length; j++) {
	    	result = result.concat(words[j]).concat(splitPattern);
	    } 
	 
	    return result;
	}
	
	 /**
	 * Truncate a string
	 * 
	 * @author Adelina
	 * 
	 * @param name, numberOfChars
	 * @return String
	 */
	private String truncateString(String name, Integer numberOfLines) {
		if (name == null || name.equals("") ) return ""; 
		String dots = "...";
		StringBuffer sb = new StringBuffer();
		
		//if the string has more than the number of lines
		for(int i=0; i<name.length() && numberOfLines > 0; i++) {
			if(name.charAt(i) == '\n'){
				if(numberOfLines == 1) {
					sb.append(dots);
				} else {
					sb.append(name.charAt(i));
				}
				
				numberOfLines --;
			} else {
				sb.append(name.charAt(i));
			}
		}

	    return sb.toString();
	}
	
	/**
	 * Replaces the '\n' with '<br\>'
	 * 
	 * @author mitziuro
	 * @param field
	 * @return
	 */
	private String insertBR(String field){
		StringBuffer sb = new StringBuffer();
		
		//we manualy parse the string and replace the '\n' with '<br\>'
		for(int i=0; i<field.length(); i++){
			if(field.charAt(i) == '\n'){
				sb.append("<br/>");
			} else {
				sb.append(field.charAt(i));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Tokenize and truncate an element
	 * 
	 * @author mitiuro
	 * @param field
	 * @param numberOfCharsPerLine
	 * @param totalNumberOfChars
	 * @return
	 */
	private String tokenizeAndTruncateString(String field, Integer numberOfCharsPerLine, Integer numberOfLines){
		return truncateString(tokenizeString(field, numberOfCharsPerLine), numberOfLines);
	}
	
	/**
	 * Tokenize a field
	 * 
	 * @author mitziuro
	 * @param field
	 * @param numberOfCharsPerLine
	 * @return
	 */
	public String tokenizedString(String field,  Integer numberOfCharsPerLine){
		return insertBR(tokenizeString(field, numberOfCharsPerLine));
	}
	
	/**
	 * Truncate and tokenize a field
	 * 
	 * @author mitziuro
	 * @param field
	 * @param numberOfCharsPerLine
	 * @param totalNumberOfChars
	 * @return
	 */
	public String truncatedString(String field, Integer numberOfCharsPerLine, Integer numberOfLines){
		return insertBR(truncateString(tokenizeString(field, numberOfCharsPerLine), numberOfLines));
	}
	
}
