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

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;




/**
 * @author matti_joona
 * @author dan.damian
 *
 */
//Exported in XML in a <organisation></organisation> element.
@XmlType(name = "organisation")
public class Organisation implements IExportImportAble, Serializable {

	private static final long serialVersionUID = 1L;
	private int organisationId;
	private String xmlID = null;
	private String name;
	private String address;
	private String phone;
	private String fax;
	private String email;
	private String observation;
	private String j;
	private String cui;
	private String iban;
	private String capital;
	private String location;
	private Calendar calendar;
	private Set<Role> roles;
	private Set<Module> modules;
	private List<Integer> moduleIDs;
	private Set<Department> departments;
	private Set<Setting> settings;
	private Set<UserGroup> usergroups;
	private Set<Job> jobs;
	private int status = 1;
	private byte type;
	private int parentId;
	private Organisation parent;
	private Logo logo;
	private Boolean hasBranches;
	private Set<Organisation> organisations;
	private String panelConfirmationName;
	
	
	public Organisation(){
		this.organisationId = -1;		
		this.status = 1;
	}
	
	public Set<Setting> getSettings() {
		return settings;
	}
	public void setSettings(Set<Setting> settings) {
		this.settings = settings;
	}
	
	//It's not saved in the xml
	@XmlTransient
	public int getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public void setJ(String j) {
		this.j = j;
	}
	public String getJ() {
		return j;
	}
	public void setCui(String cui) {
		this.cui = cui;
	}
	public String getCui() {
		return cui;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getIban() {
		return iban;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getCapital() {
		return capital;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public Set<Module> getModules() {
		return modules;
	}
	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}
	
	//Modeling organization - departments (on-to-many) relation
	//using a reference to department. A reference to department by 
	//knowing it's xmlID.
	@XmlElement(name="department")
	@XmlIDREF
	public Set<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public void setStatus(int status){
		this.status = status;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void setType(byte type){
		this.type = type;
	}
	
	public byte getType(){
		return type;
	}
	
	public void setParentId(int parentId){
		this.parentId = parentId;
	}
	
	public int getParentId(){
		return parentId;
	}
	
	public void setParent(Organisation parent){
		this.parent = parent;
	}
	
	public Organisation getParent(){
		return parent;
	}
		
	
	/**
	 * @return the panelConfirmationName
	 */
	public String getPanelConfirmationName() {
		return panelConfirmationName;
	}

	/**
	 * @param panelConfirmationName the panelConfirmationName to set
	 */
	public void setPanelConfirmationName(String panelConfirmationName) {
		this.panelConfirmationName = panelConfirmationName;
	}

	/*
	 * (non-Javadoc)
	 * @see ro.cs.om.entity.IExportImportAble#getXmlID()
	 */
	@XmlID
	public String getXmlID() {
		if (xmlID == null) {
			xmlID = Integer.toString(organisationId);
		}
		return xmlID;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see ro.cs.om.entity.IExportImportAble#setXmlID(java.lang.String)
	 */
	public void setXmlID(String xmlID) {
		this.xmlID = xmlID;
	}
	
	
	/**
	 * @author dd
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("organisationId = ")	.append(organisationId)	.append(", ");
		sb.append("xmlID = ")			.append(getXmlID())		.append(", ");
		sb.append("name = ")			.append(name)			.append(", ");
		sb.append("address = ")			.append(address)		.append(", ");
		sb.append("phone = ")			.append(phone)			.append(", ");
		sb.append("fax = ")				.append(fax)			.append(", ");
		sb.append("email = ")			.append(email)			.append(", ");
		sb.append("observation = ")		.append(observation)	.append(", ");
		sb.append("roles = ")			.append((roles != null? roles.size():"null")).append(", ");
		sb.append("modules = ")			.append((modules != null? modules.size():"null")).append(", ");
		sb.append("departments = ")		.append((departments != null? departments.size():"null")).append(", ");
		sb.append("usergroups = ")		.append((usergroups != null? usergroups.size():"null")).append(", ");
		sb.append("status = ")			.append(status)			.append(", ");
		sb.append("type = ")			.append(type)			.append(", ");
		sb.append("parentId = ")		.append(parentId)		.append(", ");
		sb.append("settings = ")		.append((settings != null? settings.size():"null"))	.append("]");
		return sb.toString();
	}

	public void setLogo(Logo logo) {
		this.logo = logo;
	}

	public Logo getLogo() {
		return logo;
	}

	public void setHasBranches(Boolean hasBranches) {
		this.hasBranches = hasBranches;
	}

	public Boolean getHasBranches() {
		return hasBranches;
	}

	public void setJobs(Set<Job> jobs) {
		this.jobs = jobs;
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	public void setUsergroups(Set<UserGroup> usergroups) {
		this.usergroups = usergroups;
	}

	public Set<UserGroup> getUsergroups() {
		return usergroups;
	}

	public void setOrganisations(Set<Organisation> organisations) {
		this.organisations = organisations;
	}

	public Set<Organisation> getOrganisations() {
		return organisations;
	}

	public List<Integer>  getModuleIDs() {
		return moduleIDs;
	}

	public void setModuleIDs(List<Integer>  moduleIDs) {
		this.moduleIDs = moduleIDs;
	}

}
