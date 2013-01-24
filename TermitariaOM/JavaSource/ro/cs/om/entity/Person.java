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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import ro.cs.om.common.IConstant;
import ro.cs.tools.Tools;

/**
 * @author matti_joona
 * 
 */
// Exported in XML in a <person></person> element.
@XmlType(name = "person")
public class Person implements IExportImportAble, Comparable<Person>, Serializable {

	private static final long serialVersionUID = 1L;

	protected final Log logger = LogFactory.getLog(getClass());

	private int personId;
	private String xmlID = null;
	private String firstName;
	private String lastName;
	private Character sex;
	private Date birthDate;
	private boolean birthDateUpdate = false;
	private int day;
	private int month;
	private int year;
	private String address;
	private String phone;
	private String email;
	private String observation;
	private String username;
	private String password;
	private String passwordConfirm;
	private Set<OutOfOffice> outOfOffice;
	private Set<Role> roles;
	private Set<Department> depts;
	private Map<Department, Job> deptWithJob;
	private Set<UserGroup> userGroups;
	private Picture picture;
	private int enabled;
	private Byte status;
	private Boolean isAdmin = null;	
	private String jobsFromDepts = "";
	private String panelHeaderName;

	private boolean modify;
	MultipartFile file = null;

	public Person() {
	}

	public Person(boolean initialize) {
		if (initialize) {
			outOfOffice = new HashSet<OutOfOffice>();
			roles = new HashSet<Role>();
			depts = new HashSet<Department>();
		}
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	// Modeling person - departments (on-to-many) relation
	// using a reference to a department. A reference to department by
	// knowing it's xmlID.
	@XmlElement(name = "department")
	@XmlIDREF
	public Set<Department> getDepts() {
		return depts;
	}

	public void setDepts(Set<Department> depts) {
		this.depts = depts;
	}

	// It's not saved in the xml
	@XmlTransient
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Character getSex() {
		return sex;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		if (!birthDateUpdate) {
			birthDateUpdate = true;
			refreshBirthDate();
		}
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		birthDateUpdate = true;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param observation
	 *            the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the userName
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getDay() {
		if (birthDate != null) {
			Calendar gc = GregorianCalendar.getInstance();
			gc.setTime(birthDate);
			return gc.get(Calendar.DAY_OF_MONTH);
		} else {
			return 0;
		}
	}

	public int getMonth() {
		if (birthDate != null) {
			Calendar gc = GregorianCalendar.getInstance();
			gc.setTime(birthDate);
			return gc.get(Calendar.MONTH);
		} else {
			return 0;
		}
	}

	public int getYear() {
		if (birthDate != null) {
			Calendar gc = GregorianCalendar.getInstance();
			gc.setTime(birthDate);
			return gc.get(Calendar.YEAR);
		} else {
			return 0;
		}
	}

	public void setDay(int day) {
		birthDateUpdate = false;
		this.day = day;
	}

	public void setMonth(int month) {
		birthDateUpdate = false;
		this.month = month;
	}

	public void setYear(int year) {
		birthDateUpdate = false;
		this.year = year;
	}

	// It's not saved in the xml
	@XmlTransient
	public Set<OutOfOffice> getOutOfOffice() {
		return outOfOffice;
	}

	public void setOutOfOffice(Set<OutOfOffice> outOfOffice) {
		this.outOfOffice = outOfOffice;
	}

	public void refreshBirthDate() {
		Calendar gc = GregorianCalendar.getInstance();
		gc.set(GregorianCalendar.YEAR, year);
		gc.set(GregorianCalendar.MONTH, month);
		gc.set(GregorianCalendar.DAY_OF_MONTH, day);
		birthDate = new Date(gc.getTime().getTime());
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}	

	/**
	 * @return the jobsFromDepts
	 */
	public String getJobsFromDepts() {
		return jobsFromDepts;
	}

	/**
	 * @param jobsFromDepts the jobsFromDepts to set
	 */
	public void setJobsFromDepts(String jobsFromDepts) {
		this.jobsFromDepts = jobsFromDepts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.cs.om.entity.IExportImportAble#getXmlID()
	 */
	@XmlID
	public String getXmlID() {
		if (xmlID == null) {
			xmlID = Integer.toString(personId);
		}
		return xmlID;
	}

	/**
	 * 
	 * @param deptXjob
	 */
	public void setDeptWithJob(Map<Department, Job> deptWithJob) {
		this.deptWithJob = deptWithJob;
	}

	public Map<Department, Job> getDeptWithJob() {
		return deptWithJob;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		sb.append("personId = ").append(personId).append(", ");	
		sb.append("status = ").append(status).append(", ");	
		//sb.append("picture = ").append(picture.getPictureId()).append(", ");	
		sb.append("isAdmin = ").append(isAdmin).append(", ");
		sb.append("xmlID = ").append(getXmlID()).append(", ");
		sb.append("firstName = ").append(firstName).append(", ");
		sb.append("lastName = ").append(lastName).append(", ");
		// sb.append("pictureId = ") .append(pictureId) .append(", ");
		sb.append("sex = ").append(sex).append(", ");
		sb.append("birthDate = ").append(birthDate).append(", ");
		sb.append("address = ").append(address).append(", ");
		sb.append("phone = ").append(phone).append(", ");
		sb.append("email = ").append(email).append(", ");
		sb.append("observation = ").append(observation).append(", ");
		// sb.append("pictureId = ") .append(pictureId) .append(", ");
		sb.append("username = ").append(username).append(", ");
		sb.append("password = ").append("*****").append("]");

		Tools.getInstance().printSet(logger, roles);
		Tools.getInstance().printHash(logger, deptWithJob);
		Tools.getInstance().printSet(logger, outOfOffice);
		
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Person o) {
		if (personId < o.getPersonId()) {
			return -1;
		} else if (personId == o.getPersonId()) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Person other = (Person) obj;
		if (this.personId != other.personId) {
			return false;
		}
		if ((this.firstName == null) ? (other.firstName != null)
				: !this.firstName.equals(other.firstName)) {
			return false;
		}
		if ((this.lastName == null) ? (other.lastName != null) : !this.lastName
				.equals(other.lastName)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + this.personId;
		hash = 67 * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
		hash = 67 * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
		return hash;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public Picture getPicture() {
		return picture;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	public boolean isModify() {
		return modify;
	}

	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
			
	/**
	 * Checks if a person is admin
	 * 
	 * @author Adelina
	 * 
	 * @return boolean
	 */
	public boolean getIsAdmin() {
		try{
		if (isAdmin != null) {
			return isAdmin.booleanValue();
		} else {
			
			// if it contains a role, that has the name OM_ADMIN, then the person is admin
			if (getRoles() != null && getRoles().size() != 0) {
				Role role = new Role();
				role.setName(IConstant.OM_ADMIN);
				if (getRoles().contains(role)) {
					isAdmin = new Boolean(true);
				} else {
					isAdmin = new Boolean(false);
				}
			} else {
				isAdmin = new Boolean(false);
			}
			logger.debug("isAdmin " + isAdmin);
			return isAdmin.booleanValue();
		}
		} catch(Exception e) {
			logger.error("", e);
		}
		return false;
	}
	
	/**
	 * @return the panelHeaderName
	 */
	public String getPanelHeaderName() {
		return panelHeaderName;
	}

	/**
	 * @param panelHeaderName the panelHeaderName to set
	 */
	public void setPanelHeaderName(String panelHeaderName) {
		this.panelHeaderName = panelHeaderName;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}
	
}
