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
package ro.cs.cm.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import ro.cs.cm.common.IConstant;

/**
 * 
 * @author Coni
 *
 */
public class Client {
	
	private int clientId = -1;
	private int organizationId;
	private byte type;
	private String address;
	private String phone;
	private String email;
	private String fax;
	private String observation;
	private byte status;
	private String description;
	private String p_firstName;
	private String p_lastName;
	private Character p_sex;
	private Date p_birthDate;
	private String c_name;
	private String c_cui;
	private String c_iban;
	private String c_capital;
	private String c_location;
	private int day;
	private int month;
	private int year;
	private boolean birthDateUpdate = false;
	private String panelHeaderName;
	private Set<Project> projects;
	
	public boolean isBirthDateUpdate() {
		return birthDateUpdate;
	}
	public void setBirthDateUpdate(boolean birthDateUpdate) {
		this.birthDateUpdate = birthDateUpdate;
	}
	public String getPanelHeaderName() {
		return panelHeaderName;
	}
	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getP_firstName() {
		return p_firstName;
	}
	public void setP_firstName(String pFirstName) {
		p_firstName = pFirstName;
	}
	public String getP_lastName() {
		return p_lastName;
	}
	public void setP_lastName(String pLastName) {
		p_lastName = pLastName;
	}
	public Character getP_sex() {
		return p_sex;
	}
	public void setP_sex(Character pSex) {
		p_sex = pSex;
	}
	
	/**
	 * @return the p_birthDate
	 */
	public Date getP_birthDate() {
		if (!birthDateUpdate && type == IConstant.NOM_CLIENT_TYPE_PERSON) {
			birthDateUpdate = true;
			refreshBirthDate();
		}
		return p_birthDate;
	}
	
	/**
	 * @param birthDate
	 * the birthDate to set
	 */
	public void setP_birthDate(Date birthDate) {
		this.p_birthDate = birthDate;
		birthDateUpdate = true;
	}
	
	public int getDay() {
		if (p_birthDate != null) {
			Calendar gc = GregorianCalendar.getInstance();
			gc.setTime(p_birthDate);
			return gc.get(Calendar.DAY_OF_MONTH);
		} else {
			return 0;
		}
	}
	public void setDay(int day) {
		birthDateUpdate = false;
		this.day = day;
	}
	public int getMonth() {
		if (p_birthDate != null) {
			Calendar gc = GregorianCalendar.getInstance();
			gc.setTime(p_birthDate);
			return gc.get(Calendar.MONTH);
		} else {
			return 0;
		}
	}
	public void setMonth(int month) {
		birthDateUpdate = false;
		this.month = month;
	}
	public int getYear() {
		if (p_birthDate != null) {
			Calendar gc = GregorianCalendar.getInstance();
			gc.setTime(p_birthDate);
			return gc.get(Calendar.YEAR);
		} else {
			return 0;
		}
	}
	public void setYear(int year) {
		birthDateUpdate = false;
		this.year = year;
	}
	
	public void refreshBirthDate() {
		Calendar gc = GregorianCalendar.getInstance();
		gc.set(GregorianCalendar.YEAR, year);
		gc.set(GregorianCalendar.MONTH, month);
		gc.set(GregorianCalendar.DAY_OF_MONTH, day);
		p_birthDate = new Date(gc.getTime().getTime());
	}
	
	public String getC_name() {
		return c_name;
	}
	public void setC_name(String cName) {
		c_name = cName;
	}
	public String getC_cui() {
		return c_cui;
	}
	public void setC_cui(String cCui) {
		c_cui = cCui;
	}
	public String getC_iban() {
		return c_iban;
	}
	public void setC_iban(String cIban) {
		c_iban = cIban;
	}
	public String getC_capital() {
		return c_capital;
	}
	public void setC_capital(String cCapital) {
		c_capital = cCapital;
	}
	public String getC_location() {
		return c_location;
	}
	public void setC_location(String cLocation) {
		c_location = cLocation;
	}
	
	/**
	 * @return the projects
	 */
	public Set<Project> getProjects() {
		return projects;
	}
	/**
	 * @param projects the projects to set
	 */
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	@Override
	public String toString() {
		return "Client [address=" + address + ", c_capital=" + c_capital
				+ ", c_cui=" + c_cui + ", c_iban=" + c_iban + ", c_location="
				+ c_location + ", c_name=" + c_name + ", clientId=" + clientId
				+ ", description=" + description + ", email=" + email
				+ ", fax=" + fax + ", observation=" + observation
				+ ", organizationId=" + organizationId + ", p_birthDate="
				+ p_birthDate + ", p_firstName=" + p_firstName
				+ ", p_lastName=" + p_lastName + ", p_sex=" + p_sex
				+ ", phone=" + phone + ", status=" + status + ", type=" + type
				+ "]";
	}
	
	
	
}
