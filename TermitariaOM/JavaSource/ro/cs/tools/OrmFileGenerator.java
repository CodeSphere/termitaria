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

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import ro.cs.om.entity.Localization;

/**
 * Generates a preliminary Hibernate Mapping file (hbm.xml file) for
 * a list of Entity Beans
 * 
 * @author dd
 */
public class OrmFileGenerator  {

	/**
	 * Constant for FileName
	 */
	private String FILE_NAME = "ORM.hbm.xml";
	
	/**
	 * Writes the hbm.xml file
	 * @author dd
	 * @param fileLocation Location where the file will be generated
	 * @param tableBeans A list with TableBean objects
	 */
	public void generate(String fileLocation, List tableBeans) {
		System.out.print("Generating file: ");
		File f = new File(fileLocation + "\\" + FILE_NAME);
		FileWriter fw = null;
		boolean ok = true;
		try {
			fw = new FileWriter(f);
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE hibernate-mapping PUBLIC \"");
			sb.append("-//Hibernate/Hibernate Mapping DTD//EN\"\n");
			sb.append("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n");
			sb.append("\n\n");
			sb.append("<hibernate-mapping auto-import=\"true\" default-lazy=\"false\">\n");
			fw.write(sb.toString());
			for(int i = 0; i < tableBeans.size(); i++) {
				String str = generateForTableBean((TableBean)tableBeans.get(i));
				fw.write(str);
			}
			fw.write("</hibernate-mapping>");
		} catch (IOException e) {
			ok = false;
			e.printStackTrace();
		}finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
					ok = false;
				}
			}
		}
		if (ok) {
			System.out.println("\nFile generated.");
		}else {
			System.out.println("\nProblems generating file...");
		}
		
	}
	/**
	 * Generates the xml snippet for a single Bean
	 * @author dd
	 * @param tb TableBean object
	 * @return String
	 */
	private String generateForTableBean(TableBean tb){
		StringBuilder sb = new StringBuilder();
		PropertyDescriptor[] pd = BeanUtils.getPropertyDescriptors(tb.getBeanClass());
		sb.append("\n\t<class name=\"").append(tb.getBeanClass().getCanonicalName());
		sb.append("\"").append(" table=\"").append(tb.getTable()).append("\">").append("\n");
		sb.append("\t\t<id name=\"\" column=\"\">\n\t\t\t<generator class=\"assigned\"/>\n\t\t</id>\n");
		for(int i = 0; i < pd.length; i++) {
			if (!"CLASS".equalsIgnoreCase(pd[i].getName())) {
				sb.append("\t\t<property name=\"").append(pd[i].getName()).append("\"");
				sb.append(" column=\"").append(pd[i].getName().toUpperCase()).append("\"/>\n");
			}
		}
		sb.append("\t</class>\n");
		System.out.print(".");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
		List<TableBean> tableBeans = new ArrayList<TableBean>();
//		tableBeans.add(new TableBean("organisation", Organisation.class));
//		tableBeans.add(new TableBean("calendar", Calendar.class));
//		tableBeans.add(new TableBean("department", Department.class));
//		tableBeans.add(new TableBean("freeday", FreeDay.class));
//		tableBeans.add(new TableBean("job", Job.class));
//		tableBeans.add(new TableBean("outofoffice", OutOfOffice.class));
//		tableBeans.add(new TableBean("permission", Permission.class));
//		tableBeans.add(new TableBean("person", Person.class));
//		tableBeans.add(new TableBean("role", Role.class));
//		tableBeans.add(new TableBean("setting", Setting.class));
//		tableBeans.add(new TableBean("person_picture", Picture.class));
		tableBeans.add(new TableBean("localization", Localization.class));
		OrmFileGenerator ofg = new OrmFileGenerator();
		ofg.generate("d:", tableBeans);
		
	}
}
