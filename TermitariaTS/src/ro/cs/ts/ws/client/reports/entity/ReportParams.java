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
package ro.cs.ts.ws.client.reports.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportParams", propOrder = {
})
public class ReportParams {

   	@XmlElementWrapper(name="properties", namespace = "http://localhost:8080/Reports/services/schemas/messages")
	private HashMap<String, Object> properties = new HashMap<String, Object>();

	/*
	 * Holds pairs Key - Value, where Key it's a property name, and value, a
	 * property type;
	 */
	@XmlElementWrapper(name="propertiesTypes", namespace = "http://localhost:8080/Reports/services/schemas/messages")
	private HashMap<String, String> propertiesTypes = new HashMap<String, String>();

   	public HashMap<String, Object> getProperties() {
		return properties;
	}
	public void setProperties(HashMap<String, Object> properties) {
		this.properties = properties;
	}
	public HashMap<String, String> getPropertiesTypes() {
		return propertiesTypes;
	}
	public void setPropertiesTypes(HashMap<String, String> propertiesTypes) {
		this.propertiesTypes = propertiesTypes;
	}
	
	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, String value) {
		properties.put(name, value);
		propertiesTypes.put(name, "java.lang.String");
	}

	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, long value) {
		properties.put(name, value);
		propertiesTypes.put(name, "long");
	}

	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, double value) {
		properties.put(name, value);
		propertiesTypes.put(name, "double");
	}

	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, Date value) {
		properties.put(name, value);
		propertiesTypes.put(name, "java.util.Date");
	}

	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, boolean value) {
		properties.put(name, value);
		propertiesTypes.put(name, "boolean");
	}
	
	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, XMLGregorianCalendar value) {
		properties.put(name, value);
		propertiesTypes.put(name, "javax.xml.datatype.XMLGregorianCalendar");
	}
	
	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, int value) {
		properties.put(name, value);
		propertiesTypes.put(name, "int");
	}
	
	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, Integer value) {
		properties.put(name, value);
		propertiesTypes.put(name, "java.lang.Integer");
	}
	
	/**
	 * Sets or updates a property of this Item.
	 */
	public void setProperty(String name, List<Integer> value) {
		properties.put(name, value);
		propertiesTypes.put(name, "java.util.List<java.lang.Integer>");
	}
}
