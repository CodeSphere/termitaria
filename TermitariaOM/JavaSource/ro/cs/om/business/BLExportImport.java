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
package ro.cs.om.business;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

import org.hibernate.jmx.StatisticsService;
import org.springframework.web.context.support.XmlWebApplicationContext;

import ro.cs.om.entity.Department;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.OrganisationContainer;
import ro.cs.om.entity.OrganisationContainers;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.exception.ICodeException;
import ro.cs.om.model.dao.DaoBeanFactory;
import ro.cs.om.model.dao.IDaoDepartment;
import ro.cs.om.model.dao.IDaoOrganisation;
import ro.cs.om.model.dao.IDaoPerson;
import ro.cs.om.utils.jaxb.JAXBUtils;


/**
 * Singletone exposing functionality for exporting and importing an Organization and 
 * it's subentities.
 *  
 * At this moment it exports only an Organization with it's:
 * 		- departments
 * 		- persons
 *  
 * @author dan.damian
 *
 */
public class BLExportImport extends BusinessLogic {

	
	//singleton implementation
    private static BLExportImport theInstance = null;
    
    private IDaoOrganisation organisationDao = DaoBeanFactory.getInstance().getDaoOrganisation();
    private IDaoDepartment departmentDao = DaoBeanFactory.getInstance().getDaoDepartment();
    private IDaoPerson personDao = DaoBeanFactory.getInstance().getDaoPerson();
    
    private BLExportImport(){};
    private static XmlWebApplicationContext appCtx = null; 
    
    static {
//    	appCtx = new XmlWebApplicationContext();
//		appCtx.setConfigLocation("file:./WebContent/WEB-INF/applicationContext.xml");
//		MockServletContext mockServletContext =	new MockServletContext("");
//		appCtx.setServletContext(mockServletContext);
//		appCtx.refresh();
		
        theInstance = new BLExportImport();
    }
    public static BLExportImport getInstance() {
    	return theInstance;
    }

	
	
	/**
	 * Exports an OrganisationContainers to an XML file.
	 * @throws JAXBException
	 */
	private  void _export(OutputStream  os, OrganisationContainers ocs) throws JAXBException{
		//Retrieving the JAXBContext
		JAXBContext context = JAXBUtils.getInstance().getJAXBContextForExport();
		//Creating the Marshaller
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        //Exporting
        m.marshal(ocs, os);
	}

	/**
	 * Imports an OrganisationContainers form an XML file. 
	 * @throws JAXBException 
	 * @throws JAXBException
	 */
	private  OrganisationContainers _import(InputStream is) throws JAXBException {
		OrganisationContainers orgCs = null;
		//Retrieving the JAXBContext
		JAXBContext context = JAXBUtils.getInstance().getJAXBContextForExport();
        //Creating the Unmarshaller
		Unmarshaller um = context.createUnmarshaller();
        um.setEventHandler(new DefaultValidationEventHandler());
        orgCs = (OrganisationContainers) um.unmarshal(is);
		return orgCs;
		
	}
	
	/**
	 * Import
	 * @throws JAXBException 
	 * @throws BusinessException 
	 */
	private  OrganisationContainers doImport(InputStream is) throws JAXBException {
		logger.info("Unmarshalling...");
		long start = System.currentTimeMillis();
		OrganisationContainers orgCs = null;

		orgCs = _import(is);
		
		logger.info("done in " + (System.currentTimeMillis() - start ) + " ms");
		return orgCs; 
	}
	
	/**
	 * Export
	 * @throws JAXBException 
	 * @throws BusinessException 
	 */
	private  void doExport(OutputStream os, OrganisationContainers orgCs) throws JAXBException {
		logger.info("Marshalling...");
		long start = System.currentTimeMillis();
		_export(os, orgCs);
		logger.info("done in " + (System.currentTimeMillis() - start ) + " ms");
	}
	
	/**
	 * Save a Department to DB.
	 * To avoid Hibernate restrictions this Department's references are
	 * temporarily deleted.
	 */
	private  void saveWithoutReferences(Department d, Department fakeParent) {
		//Get references
		Department parentDep = d.getParentDepartment();
		//Deleting references
		d.setParentDepartment(fakeParent);
		//Saving to DB
		departmentDao.addFromImport(d);
		//Restoring references
		d.setParentDepartment(parentDep);
	}
	
	/**
	 * Returns fake Department for a List of Departments
	 */
	private  Department findFakeDepartment(List<Department> depts) {
		Department fake = null;
		int i = 0;
		while(fake == null) {
			if (depts.get(i).getStatus() == 2) {
				fake = depts.get(i);
			}
			i++;
		}
		return fake;
	}

	
	/**
	 * Exports to a File
	 */
	public  void exportToFile(OrganisationContainers orgCs, String fileName) throws BusinessException {
		logger.info("Export to file: ".concat(fileName));
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileName);
			doExport(fos, orgCs);
		} catch (FileNotFoundException bexc) {
			throw new BusinessException(ICodeException.EXPORT_IMPORT_EXPORT, bexc);
		} catch (JAXBException bexc) {
			throw new BusinessException(ICodeException.EXPORT_IMPORT_EXPORT, bexc);
		}
		
	}

	/**
	 * Imports from file
	 */
	public  void importFromFile(String fileName) throws BusinessException{
		logger.info("Import from file: ".concat(fileName));
		try {
			FileInputStream fis = new FileInputStream(fileName);
			OrganisationContainers orgCs = doImport(fis);
			int i = 1;
			for(OrganisationContainer orgC : orgCs.getOrgCs()) {
				
				logger.debug("Saving Organisation Container <" + (i++) + ">");
				//Retrieving Organization
				Organisation org = orgC.getOrganisation();
				
				organisationDao.addOnlyWithSimpleAttrs(org);
				//First I'll find the Fake Department
				List<Department> depts = orgC.getDepartments();
				Department fake = findFakeDepartment(depts);
				//Save Fake Department
				departmentDao.addFakeFromImport(fake);
				//Save all other Departments supposing that their Parent is always Fake Department
				logger.debug("----------------- Saving Departments (without references)");
				for(Department d: depts) {
					if (d.getStatus() != 2) {
						saveWithoutReferences(d, fake);
						logger.debug(d);
					}
				}
				//Updating the hole hierarchy of Departments after all Departments are persistent
				logger.debug("----------------- Updating realtion: [Department - Parent Department]");
				for(Department d: depts) {
					if (d.getStatus() != 2) {
						departmentDao.updateFromImport1(d);
						logger.debug(d);
					}
				}
				//Saving all Persons without references
				logger.debug("----------------- Saving Persons (without references)");
				List<Person> persons = orgC.getPersons();
				for(Person p : persons) {
					personDao.addFromImport(p);
					logger.debug(p);
				}
				
				//Updating again Departments
				logger.debug("----------------- Updating Departments relation: [Department - Persons]");
				for(Department d: depts) {
					if (d.getStatus() != 2) {
						departmentDao.updateFromImport(d);
					} else {
						departmentDao.updateFakeFromImport(d);
					}
				}
				
				logger.info("Import succedded !");
			}
		}catch(Exception bexc) {
			throw new BusinessException(ICodeException.EXPORT_IMPORT_IMPORT, bexc);
		}
		
	}


	public static void main(String[] args) throws FileNotFoundException {
			
			
			String fileName = "export_import\\result1.xml";
			
			StatisticsService ss = (StatisticsService) appCtx.getBean("hibernateStats");
			
			OrganisationContainers orgCs = new OrganisationContainers();
			Organisation org = DaoBeanFactory.getInstance().getDaoOrganisation().getForExport(35);
			OrganisationContainer orgC = new OrganisationContainer();
			orgC.setOrganisation(org);
			List<OrganisationContainer> orgContainers = new ArrayList<OrganisationContainer>();
			orgContainers.add(orgC);
			orgCs.setOrgCs(orgContainers);
			try {
				//BLExportImport.getInstance().exportToFile(orgCs,  fileName);
				BLExportImport.getInstance().importFromFile(fileName);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mainImport(fileName);
			
			
		}
	
	
}
