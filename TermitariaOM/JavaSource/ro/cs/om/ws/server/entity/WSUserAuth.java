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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b52-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.27 at 06:34:15 PM EEST 
//


package ro.cs.om.ws.server.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for WSUserAuth complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WSUserAuth">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="personId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="accountNonExpired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="accountNonLocked" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="credentialsNonExpired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="adminIT" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="authoritiesArray" type="{http://localhost:8080/OM/services/schemas/messages}WSGrantedAuthorityImplArray"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="observation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="themeCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organisationId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="organisationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organisationAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="jobDepartmentPairs" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WSUserAuth", propOrder = {

})
public class WSUserAuth {

    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
    protected int personId;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String username;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
    protected boolean accountNonExpired;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
    protected boolean accountNonLocked;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
    protected boolean credentialsNonExpired;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
    protected boolean enabled;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
    protected boolean adminIT;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected WSGrantedAuthorityImplArray authoritiesArray;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String firstName;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String lastName;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String sex;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected XMLGregorianCalendar birthDate;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String address;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String phone;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String email;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String observation;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String themeCode;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages")
    protected int organisationId;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String organisationName;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String organisationAddress;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
    protected String jobDepartmentPairs;
    @XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private List<String> userGroups;
	@XmlElement(namespace = "http://localhost:8080/OM/services/schemas/messages", required = true)
	private List<Integer> organisationModulesIds;

    
	public List<Integer> getOrganisationModulesIds() {
		return organisationModulesIds;
	}

	public void setOrganisationModulesIds(List<Integer> organisationModulesIds) {
		this.organisationModulesIds = organisationModulesIds;
	}
	
	/**
     * Gets the value of the personId property.
     * 
     */
    public int getPersonId() {
        return personId;
    }

    /**
     * Sets the value of the personId property.
     * 
     */
    public void setPersonId(int value) {
        this.personId = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the accountNonExpired property.
     * 
     */
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Sets the value of the accountNonExpired property.
     * 
     */
    public void setAccountNonExpired(boolean value) {
        this.accountNonExpired = value;
    }

    /**
     * Gets the value of the accountNonLocked property.
     * 
     */
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Sets the value of the accountNonLocked property.
     * 
     */
    public void setAccountNonLocked(boolean value) {
        this.accountNonLocked = value;
    }

    /**
     * Gets the value of the credentialsNonExpired property.
     * 
     */
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * Sets the value of the credentialsNonExpired property.
     * 
     */
    public void setCredentialsNonExpired(boolean value) {
        this.credentialsNonExpired = value;
    }

    /**
     * Gets the value of the enabled property.
     * 
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the value of the enabled property.
     * 
     */
    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    /**
     * Gets the value of the adminIT property.
     * 
     */
    public boolean isAdminIT() {
        return adminIT;
    }

    /**
     * Sets the value of the adminIT property.
     * 
     */
    public void setAdminIT(boolean value) {
        this.adminIT = value;
    }

    /**
     * Gets the value of the authoritiesArray property.
     * 
     * @return
     *     possible object is
     *     {@link WSGrantedAuthorityImplArray }
     *     
     */
    public WSGrantedAuthorityImplArray getAuthoritiesArray() {
        return authoritiesArray;
    }

    /**
     * Sets the value of the authoritiesArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link WSGrantedAuthorityImplArray }
     *     
     */
    public void setAuthoritiesArray(WSGrantedAuthorityImplArray value) {
        this.authoritiesArray = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the sex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSex() {
        return sex;
    }

    /**
     * Sets the value of the sex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSex(String value) {
        this.sex = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthDate(XMLGregorianCalendar value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the observation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Sets the value of the observation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservation(String value) {
        this.observation = value;
    }

    /**
     * Gets the value of the themeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThemeCode() {
        return themeCode;
    }

    /**
     * Sets the value of the themeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThemeCode(String value) {
        this.themeCode = value;
    }

    /**
     * Gets the value of the organisationId property.
     * 
     */
    public int getOrganisationId() {
        return organisationId;
    }

    /**
     * Sets the value of the organisationId property.
     * 
     */
    public void setOrganisationId(int value) {
        this.organisationId = value;
    }

    /**
     * Gets the value of the organisationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * Sets the value of the organisationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganisationName(String value) {
        this.organisationName = value;
    }

    /**
     * Gets the value of the organisationAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganisationAddress() {
        return organisationAddress;
    }

    /**
     * Sets the value of the organisationAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganisationAddress(String value) {
        this.organisationAddress = value;
    }
    
    public String getJobDepartmentPairs() {
		return jobDepartmentPairs;
	}

	public void setJobDepartmentPairs(String jobDepartmentPairs) {
		this.jobDepartmentPairs = jobDepartmentPairs;
	}

	public List<String> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<String> userGroups) {
		this.userGroups = userGroups;
	}

	
}