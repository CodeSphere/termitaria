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
package ro.cs.logaudit.web.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ro.cs.logaudit.entity.Role;

/**
 * @author matti_joona
 * @author alu
 * @author Adelina
 *
 * Bean that encapsulates all the information needed for a User Session.
 */
public class UserAuth implements UserDetails {
	
	public static final String KEY = "UserInfo";
	
	//************ User Details specific attributes
	private int personId;	   
    private String password;
    private String username;
    private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
    private boolean enabled;
    private boolean adminIT = false;
    private HashMap<String, Object> authoritiesHash;
	private Collection<? extends GrantedAuthority> authorities;
   
    //************ Business Attributes
    private String firstName;
    private String lastName;
    private char 	sex;
	private Date 	birthDate;
	private String 	address;
	private String 	phone;
	private String 	email;
	private String 	observation;	
	private String 	themeCode;
    private int organisationId;   
    private String organisationName;
    private String organisationAddress;
   
	private Set<Role> roles;
	private String displayRole;
	private String roleName;
	private List<Integer> organisationModulesIds;
		
	public List<Integer> getOrganisationModulesIds() {
		return organisationModulesIds;
	}

	public void setOrganisationModulesIds(List<Integer> organisationModulesIds) {
		this.organisationModulesIds = organisationModulesIds;
	}

	/**
	 * @return the personId
	 */
	public int getPersonId() {
		return personId;
	}

	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the adminIT
	 */
	public boolean isAdminIT() {
		return adminIT;
	}

	/**
	 * @param adminIT the adminIT to set
	 */
	public void setAdminIT(boolean adminIT) {
		this.adminIT = adminIT;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the sex
	 */
	public char getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(char sex) {
		this.sex = sex;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
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
	 * @param phone the phone to set
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
	 * @param email the email to set
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
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the themeCode
	 */
	public String getThemeCode() {
		return themeCode;
	}

	/**
	 * @param themeCode the themeCode to set
	 */
	public void setThemeCode(String themeCode) {
		this.themeCode = themeCode;
	}

	/**
	 * @return the organisationId
	 */
	public int getOrganisationId() {
		return organisationId;
	}

	/**
	 * @param organisationId the organisationId to set
	 */
	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the displayRole
	 */
	public String getDisplayRole() {
		return displayRole;
	}

	/**
	 * @param displayRole the displayRole to set
	 */
	public void setDisplayRole(String displayRole) {
		this.displayRole = displayRole;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	/**
	 * @return the organisationName
	 */
	public String getOrganisationName() {
		return organisationName;
	}
	/**
	 * @param organisationName the organisationName to set
	 */
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	
	

	/**
	 * @return the accountNonExpired
	 */
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	/**
	 * @param accountNonExpired the accountNonExpired to set
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * @return the accountNonLocked
	 */
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * @param accountNonLocked the accountNonLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @return the credentialsNonExpired
	 */
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 * @param credentialsNonExpired the credentialsNonExpired to set
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * @return the authoritiesHash
	 */
	public HashMap<String, Object> getAuthoritiesHash() {
		return authoritiesHash;
	}

	/**
	 * @param authoritiesHash the authoritiesHash to set
	 */
	public void setAuthoritiesHash(HashMap<String, Object> authoritiesHash) {
		this.authoritiesHash = authoritiesHash;
	}

	/**
	 * @return the authorities
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	/**
	 * "overloaded" version of getAuthorities()
	 */
	public GrantedAuthority[] getArrayOfAuthorities(){
		return (GrantedAuthority[])getAuthorities().toArray();
	}
	
	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		if (authorities == null) return;
		this.authorities =  authorities;
		if (authoritiesHash != null) {
			authoritiesHash.clear();
		} else {
			authoritiesHash = new HashMap<String, Object>();
		}
		for(GrantedAuthority ga: authorities) {
			authoritiesHash.put(ga.getAuthority(), new Object());
		}
	}
	
	/**
	 * @overloaded method (to acces an array of GrantedAuthority s)
	 */
	public void setAuthorities(GrantedAuthority[] authorities) {
		setAuthorities(Arrays.asList(authorities));
	}
	
	/**
	 * @return the organisationAddress
	 */
	public String getOrganisationAddress() {
		return organisationAddress;
	}

	/**
	 * @param organisationAddress the organisationAddress to set
	 */
	public void setOrganisationAddress(String organisationAddress) {
		this.organisationAddress = organisationAddress;
	}
	
	public boolean hasAuthority(String authority) {
		Object o = authoritiesHash.get(authority);
		return (o != null ? true :false);
	}

	@Override
	public String toString() {		
		StringBuffer sb = new StringBuffer("[");
		
		sb.append(this.getClass().getSimpleName());
		sb.append(": ");
		sb.append("personId = ")       .append(personId)       .append(", ");
		sb.append("username = ")       .append(username)       .append(", ");
		sb.append("accountNonExpired = ").append(accountNonExpired).append(", ");
		sb.append("accountNonLocked = ").append(accountNonLocked).append(", ");
		sb.append("credentialsNonExpired = ").append(credentialsNonExpired).append(", ");
		sb.append("enabled = ")        .append(enabled)        .append(", ");
		sb.append("adminIT = ")        .append(adminIT)        .append(", ");
		sb.append("firstName = ")      .append(firstName)      .append(", ");
		sb.append("lastName = ")       .append(lastName)       .append(", ");
		sb.append("sex = ")            .append(sex)            .append(", ");
		sb.append("birthDate = ")      .append(birthDate)      .append(", ");
		sb.append("address = ")        .append(address)        .append(", ");
		sb.append("phone = ")          .append(phone)          .append(", ");
		sb.append("email = ")          .append(email)          .append(", ");
		sb.append("observation = ")    .append(observation)    .append(", ");
		sb.append("themeCode = ")      .append(themeCode)      .append(", ");
		sb.append("authorities = ")    .append((authorities !=null ? authorities.size() : "null")).append(", ");
		sb.append("organisationId = ") .append(organisationId) .append("]");
		
		return sb.toString();
	}
}
