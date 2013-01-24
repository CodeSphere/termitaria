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

import ro.cs.om.entity.Localization;



/**
 * @author dan.damian
 *
 */
public class GenerateFormHTMLTable {


	private static int commentLineLength = 100;
	private static void generateFormTable(String tab, Class theBean) {

		
		
		Field[] fields = theBean.getDeclaredFields();
		System.out.println("<form:form method=\"post\" commandName=\"personBean\" id=\"personForm\">");
		System.out.println(tab + "<table>");
		for(int i = 0; i < fields.length; i++) {
			System.out.println(tab + tab + getCommentLine(fields[i].getName()));
			System.out.println(tab + tab + "<tr>");
				System.out.println(tab + tab + tab + "<td><spring:message code=\"\"/></td>");
				System.out.println(tab + tab + tab + "<td><form:input path=\"" + fields[i].getName() + "\"/></td>");
			System.out.println(tab + tab + "</tr>");
		}
		
		System.out.println(tab + "</table>");
		System.out.println("</form:form>");
	}
	
	private static String getCommentLine(String name) {
		StringBuffer sb = new StringBuffer("<!-- ");
		
		int lcommentLineLength = commentLineLength;
		
		//Extracting beging and ending of HTML comments
		lcommentLineLength -= (sb.length() + sb.length() - 1);
		
		for(int i =0; i < (lcommentLineLength - name.length()) / 2 - 1; i++ ) {
			sb.append("=");
		}
		
		sb.append(" ").append(name.toUpperCase()).append(" ");
		for(int i =0; i < (lcommentLineLength - name.length()) /2 - 1; i++ ) {
			sb.append("=");
		}
		
		sb.append(" -->");
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		generateFormTable("    ", Localization.class);
	}
}
