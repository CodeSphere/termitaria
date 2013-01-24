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
package ro.cs.om.utils.jaxb;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.OrganisationContainer;
import ro.cs.om.entity.OrganisationContainers;
import ro.cs.om.entity.Person;

/**
 * @author dan.damian
 *
 */
public class JAXBUtils extends ApplicationObjectSupport {

	
	
	private static JAXBUtils theInstance = null;
	static{
		theInstance = new JAXBUtils();
	}
	private JAXBUtils() {}
	
	public static JAXBUtils getInstance() {
		return theInstance;
	}
	
	/**
	 * Returns a JAXBContext instance, configured for exporting
	 * an Organization.
	 * 
	 * @author dan.damian
	 */
	public  JAXBContext getJAXBContextForExport() throws JAXBException {
        //Initializing the JAXBContext with all the entities
		//that it will know to export.
		return JAXBContext.newInstance(
                OrganisationContainers.class,
        		OrganisationContainer.class,
                Organisation.class, Department.class, Person.class);
    }
	
	
	public void generateSchemaForOM(String baseFileName, String suggestedFileName) throws IOException, JAXBException {
		
		JAXBContext context = JAXBUtils.getInstance().getJAXBContextForExport();
		context.generateSchema(new MySchemaOutputResolver(baseFileName, suggestedFileName));
	}
	
	
	public static void main(String[] args) throws JAXBException {
	
		
		JAXBContext context = null; 
		try {
			JAXBUtils.getInstance().generateSchemaForOM(".", "schema.xsd");
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	

	
	
	
}

class MySchemaOutputResolver extends SchemaOutputResolver {
    
	private String baseName;
	
	private String suggestedFileName;
	
	public MySchemaOutputResolver(String baseName, String suggestedFileName) {
		this.baseName = baseName;
		this.suggestedFileName = suggestedFileName;
	}
	
	public Result createOutput(String namespaceUri, String suggestedFileName ) throws IOException {
        return new StreamResult(new File(baseName, suggestedFileName));
    }
}
