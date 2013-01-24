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
package ro.cs.tools;

import java.lang.reflect.Field;

import ro.cs.om.ws.server.entity.WSUserAuth;

/**
 * 
 * Generates for a Bean its toString method.
 * 
 * @author dan.damian
 *
 */
public class GenerateToString {

	
	
	public static void main(String[] args) {
		Class theBean = WSUserAuth.class;
		
		
		Field[] fields = theBean.getDeclaredFields();
		System.out.println("public String toString() {\n" +
		"\tStringBuffer sb = new StringBuffer(\"[\");\n" + 
		"\tsb.append(this.getClass().getSimpleName());\n" +
		"\tsb.append(\": \");");
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < fields.length; i++) {
			sb.delete(0, sb.length());
			sb.append("\tsb.append(\"");
			sb.append(fields[i].getName());
			sb.append(" = \")");
			for(int j = 0; j < 15 - fields[i].getName().length(); j++) {
				sb.append(" ");
			}
			sb.append(".append(");
			sb.append(fields[i].getName()).append(")");
			for(int j = 0; j < 15 - fields[i].getName().length(); j++) {
				sb.append(" ");
			}			
			if (i == fields.length - 1) {
				sb.append(".append(\"]\");");
			} else {
				sb.append(".append(\", \");");
			}
			System.out.println(sb.toString());
		}
		
		System.out.println("return sb.toString();\n" +
		"}");
	}
}
