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
package ro.cs.ts.web.common;

import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.TextTable;

/**
 * Afiseaza atribute/parametrii de pe request/sesiune
 * 
 * @author dd
 * @author matti_joona

 */
public class HttpInspector {
	
    private static ResourceBundle rb = ResourceBundle.getBundle("config.httpinterceptor");

    private final static boolean AFISEAZA_ATRIBUTE_SPECIALE_REQUEST = new Boolean(rb.getString(IConstant.HTTP_INSPECTOR_SPECIAL_ATRIBUTE_REQUEST)).booleanValue();
    
    private final static boolean AFISEAZA_ATRIBUTE_REQUEST = new Boolean(rb.getString(IConstant.HTTP_INSPECTOR_ATRIBUTE_REQUEST)).booleanValue();

    private final static boolean AFISEAZA_PARAMETRII_REQUEST = new Boolean(rb.getString(IConstant.HTTP_INSPECTOR_PARAMETRII_REQUEST)).booleanValue();

    private final static boolean AFISEAZA_ATRIBUTE_SESIUNE = new Boolean(rb.getString(IConstant.HTTP_INSPECTOR_ATRIBUTE_SESSION)).booleanValue();

    private final static int MAX_LENGTH = 1000;

    public static String inspect(HttpServletRequest request) {
    	
    	TextTable sRequestAttributes = new TextTable("Special [R] Attributes#Value", TextTable.ALIGN_CENTER);
    	TextTable ttRequestAttributes = new TextTable("Attr  [R]#Value", TextTable.ALIGN_CENTER);
    	TextTable ttRequestParameters = new TextTable("Param [R]#Value", TextTable.ALIGN_CENTER);
    	TextTable ttSessionAttributes = new TextTable("Attr  [S]#Value", TextTable.ALIGN_CENTER);
    	StringBuffer sb = new StringBuffer();
    	Enumeration<String> enumElemente = null;
    	String value = null;
    	String valueOriginal = null;
    	String attr = null;
    	
    	// AFISEAZA ATRIBUTELE SPECIALE ALE REQUESTULUI
		if (AFISEAZA_ATRIBUTE_SPECIALE_REQUEST) {
			sRequestAttributes.addRow("LOCALE".concat("#").concat(RequestContextUtils.getLocale(request).toString()), TextTable.ALIGN_LEFT);
			sRequestAttributes.addRow("URL".concat("#").concat(request.getRequestURI()), TextTable.ALIGN_LEFT);
			sRequestAttributes.addRow("QS".concat("#").concat((request.getQueryString() != null?request.getQueryString():"")), TextTable.ALIGN_LEFT);
			sRequestAttributes.addRow("METHOD".concat("#").concat(request.getMethod()), TextTable.ALIGN_LEFT);
			sb.append(sRequestAttributes.getTable());
		}
    	
    	// AFISEAZA ATRIBUTELE REQUEST
		if (AFISEAZA_ATRIBUTE_REQUEST) {
			enumElemente = request.getAttributeNames();
		    while (enumElemente.hasMoreElements()) {
		        attr = enumElemente.nextElement();
		        valueOriginal = request.getAttribute(attr).toString();
		        if(valueOriginal.length() > MAX_LENGTH){
		        	value = valueOriginal.substring(0, MAX_LENGTH);	
		        } else {
		        	value = valueOriginal;
		        }
                ttRequestAttributes.addRow(attr.concat("#").concat(value), TextTable.ALIGN_LEFT);
		    }
		    sb.append(ttRequestAttributes.getTable());
		}
		
		// AFISEAZA PARAMETRII REQUEST
		if (AFISEAZA_PARAMETRII_REQUEST) {
			String[] valuesOriginal = null;
			enumElemente = request.getParameterNames();
		    while (enumElemente.hasMoreElements()) {
		        attr = (String) enumElemente.nextElement();
		        valuesOriginal = request.getParameterValues(attr);
	            //daca sunt mai multe cu acelasi nume
	            if (valuesOriginal != null) {
	                for (int i = 0; i < valuesOriginal.length; i++) {
	                	if(valuesOriginal[i].length() > MAX_LENGTH){
	                		value = valuesOriginal[i].substring(0, MAX_LENGTH);	
	                	} else {
	                		value = valuesOriginal[i];
	                	}
	                    ttRequestParameters.addRow(attr.concat("#").concat(value), TextTable.ALIGN_LEFT);
	                }
	            } else {
	                valueOriginal = request.getParameter(attr).toString();
			        if(valueOriginal.length() > MAX_LENGTH){
			        	value = valueOriginal.substring(0, MAX_LENGTH);	
			        } else {
			        	value = valueOriginal;
			        }

                	ttRequestParameters.addRow(attr.concat("#").concat(value), TextTable.ALIGN_LEFT);	
	            }
		    }
		    sb.append(ttRequestParameters.getTable());
		}
		
		// AFISEAZA ATRIBUTELE SESIUNII
		if (AFISEAZA_ATRIBUTE_SESIUNE) {
		    HttpSession ses = request.getSession();
		    enumElemente = ses.getAttributeNames();
	        while (enumElemente.hasMoreElements()) {
	            attr = (String) enumElemente.nextElement();
	            valueOriginal = request.getSession().getAttribute(attr).toString();
		        if(valueOriginal.length() > MAX_LENGTH){
		        	value = valueOriginal.substring(0, MAX_LENGTH);	
		        } else {
		        	value = valueOriginal;
		        }
	            ttSessionAttributes.addRow(attr.concat("#").concat(value), TextTable.ALIGN_LEFT);
	        }
	        ttSessionAttributes.addRow("SESSION ID ".concat("#").concat(request.getSession().getId()), TextTable.ALIGN_LEFT);
		    sb.append(ttSessionAttributes.getTable());
		}
		return sb.toString();
	}	
}
