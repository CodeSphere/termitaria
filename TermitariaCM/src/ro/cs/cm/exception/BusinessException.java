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
package ro.cs.cm.exception;

/**
 * @author matti_joona
 *
 * Exception class in which it is set the exception's cod and message
 * Any specialized exception must extend this class 
 */
public class BusinessException extends Exception{
	
	private String code;
	
	public BusinessException(String code, Throwable cause){
		super(cause);
		this.code = code;
	}
	
	public BusinessException(String code, String message, Throwable cause){
		super(message, cause);
		this.code = code;
	}
	
	public String getMessage() {
		StringBuffer sb = new StringBuffer(((code !=null)?code:" unknow code"));
		sb.append("   ");
		String message = super.getMessage();
		if(message != null){
			sb.append(message.substring(message.indexOf(':') + 1));
		}
		return sb.toString(); 
	}

    public String getCode() {
        return code;
    }

}
