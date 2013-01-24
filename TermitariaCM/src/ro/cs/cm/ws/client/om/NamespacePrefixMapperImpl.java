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
package ro.cs.cm.ws.client.om;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class NamespacePrefixMapperImpl extends NamespacePrefixMapper{
	
	private final static String xsiNamespaceUri = "http://www.w3.org/2001/XMLSchema-instance";
	private final static String omServiceMessagesNamespaceUri = "http://localhost:8080/OM/services/schemas/messages";
	
	private final static String xsiPrefix = "xsi";
	private final static String omServiceMessagesPrefix = "om";
	

    /**
     * Returns a preferred prefix for the given namespace URI.
     * 
     * This method is intended to be overrided by a derived class.
     * 
     * @param namespaceUri
     *      The namespace URI for which the prefix needs to be found.
     *      Never be null. "" is used to denote the default namespace.
     * @param suggestion
     *      When the content tree has a suggestion for the prefix
     *      to the given namespaceUri, that suggestion is passed as a
     *      parameter. Typicall this value comes from the QName.getPrefix
     *      to show the preference of the content tree. This parameter
     *      may be null, and this parameter may represent an already
     *      occupied prefix. 
     * @param requirePrefix
     *      If this method is expected to return non-empty prefix.
     *      When this flag is true, it means that the given namespace URI
     *      cannot be set as the default namespace.
     * 
     * @return
     *      null if there's no prefered prefix for the namespace URI.
     *      In this case, the system will generate a prefix for you.
     * 
     *      Otherwise the system will try to use the returned prefix,
     *      but generally there's no guarantee if the prefix will be
     *      actually used or not.
     * 
     *      return "" to map this namespace URI to the default namespace.
     *      Again, there's no guarantee that this preference will be
     *      honored.
     * 
     *      If this method returns "" when requirePrefix=true, the return
     *      value will be ignored and the system will generate one.
     */
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        // I want this namespace to be mapped to "xsi"
        if (xsiNamespaceUri.equals(namespaceUri)) {
        	return xsiPrefix;
        } else if (omServiceMessagesNamespaceUri.equals(namespaceUri)) {
        	return omServiceMessagesPrefix;
        }
    
        return suggestion;
    }
            
}
