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
package ro.cs.om.entity;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * @author dan.damian
 *
 */
public class SchemaGenerator {
	
	
	public static void main(String[] args) throws JAXBException, IOException {
		
		

		

		JAXBContext context = JAXBContext.newInstance(Calendar.class, Department.class, FreeDay.class, 
				Job.class, Module.class, Organisation.class, Person.class, Role.class, Permission.class);
		System.out.println("Begin generating schema...");
		context.generateSchema(new MySchemaOutputResolver());
		System.out.println("Work done !");
	}
}

class MySchemaOutputResolver extends SchemaOutputResolver {
    
	File baseDir = new File(".");
	
	public Result createOutput( String namespaceUri, String suggestedFileName ) throws IOException {
        return new StreamResult(new File(baseDir, "organisationManagement.xsd"));
    }
}
