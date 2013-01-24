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
package ro.cs.om.web.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Module;
import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.entity.Permission;
import ro.cs.om.entity.Role;
import ro.cs.om.entity.Setting;
import ro.cs.om.web.controller.root.ControllerUtils;

/**
 * Bean that encapsulates all the information needed for a User Session.
 * 
 * @dd
 * 
 */
public class UserAuth implements UserDetails {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	//************ User Details specific attributes
	private int personId;
	private String username;
	private String password;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private byte status;
	private boolean adminIT = false;
	private HashMap<String, Object> authoritiesHash;
	private Collection<? extends GrantedAuthority> authorities;
	private String[] userGroups;	
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS			= 20;
	
	
	//************ End User Details specific attributes
	
	
	//************ Business Attributes
	private String 	firstName;
	private String 	lastName;
	private char 	sex;
	private Date 	birthDate;
	private String 	address;
	private String 	phone;
	private String 	email;
	private String 	observation;
	private String 	themeCode = IConstant.STANDARD_THEME;
	private int organisationId;
	private String organisationName;
	private String organisationAddress;
	private int organisationStatus;
	

	private List<Module> modules;
	private Set<Department> departments;
	private List<Role> roles;
	private List<Permission> permissions;
	private List<Setting> settings;
	private Set<OutOfOffice> outOfOffice;
	private Map<Department,Job> deptWithJob;
	private Set<Department> depts;
	
	//************ End Business Attributes
	
	
	/**
	 * @return the deptWithJob
	 */
	public Map<Department, Job> getDeptWithJob() {
		return deptWithJob;
	}
	/**
	 * @param deptWithJob the deptWithJob to set
	 */
	public void setDeptWithJob(Map<Department, Job> deptWithJob) {
		this.deptWithJob = deptWithJob;
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
	
	public boolean hasAuthority(String authority) {
		Object o = authoritiesHash.get(authority);
		return (o != null ? true :false);
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
	 * @return the organisationStatus
	 */
	public Integer getOrganisationStatus() {
		return organisationStatus;
	}
	/**
	 * @param organisationStatus the organisationStatus to set
	 */
	public void setOrganisationStatus(Integer organisationStatus) {
		this.organisationStatus = organisationStatus;
	}
	/**
	 * @return the permissions
	 */
	public List<Permission> getPermissions() {
		return permissions;
	}
	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
	/**
	 * @return the settings
	 */
	public List<Setting> getSettings() {
		return settings;
	}
	/**
	 * @param settings the settings to set
	 */
	public void setSettings(List<Setting> settings) {
		this.settings = settings;
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
	 * @return the userName
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param userName the userName to set
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the modules
	 */
	public List<Module> getModules() {
		return modules;
	}
	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	/**
	 * @return the departments
	 */
	public Set<Department> getDepartments() {
		return departments;
	}
	/**
	 * @param departments the departments to set
	 */
	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
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
	 * @return the outOfOffice
	 */
	public Set<OutOfOffice> getOutOfOffice() {
		return outOfOffice;
	}
	/**
	 * @param outOfOffice the outOfOffice to set
	 */
	public void setOutOfOffice(Set<OutOfOffice> outOfOffice) {
		this.outOfOffice = outOfOffice;
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
	
	
	public boolean isAdminIT() {
		return adminIT;
	}
	
	public void setAdminIT(boolean adminIT) {
		this.adminIT = adminIT;
	}	

	public String getFirstNameTruncate() {
		String firstName = ControllerUtils.getInstance().truncateName(getFirstName(), NR_CHARS);
		return firstName;
	}
	
	public String getLastNameTruncate() {
		String lastName = ControllerUtils.getInstance().truncateName(getLastName(), NR_CHARS);
		return lastName;
	}
	
	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}
	
	public String getJobDepartmentPairs() {
		StringBuffer sb = new StringBuffer();
		if (deptWithJob == null) {
			return "-";
		} else {
			int i = 0;
			Set<Department> departments = deptWithJob.keySet();
			for(Department d : departments) {
				if (i > 0) {
					sb.append(" | ");
				}
				sb.append(deptWithJob.get(d).getName().concat(" - ").concat(d.getName()));
				i++;
			}
			
			return sb.toString();
		}
	}
		
	/**
	 * Function that truncates the paris of job-departments
	 * 
	 * @author Adelina
	 * 
	 * @return
	 */
	public String getJobDepartmentPairsTruncate() {
		
		logger.debug("getJobDepartmentPairsTruncate - START");
		
		StringBuffer sb = new StringBuffer();
		StringBuffer sbNew = new StringBuffer();
		
		String dots = " ...";
		int length = 107;	
		
		Integer personNameLength = getFirstName().length() + getLastName().length() + 2;	
		
		if (deptWithJob == null) {
			logger.debug("getJobDepartmentPairsTruncate - END");
			return "-";
		} else {
			int i = 0;
			boolean hasMoreJobs = false;
			Set<Department> departments = deptWithJob.keySet();
			if(departments != null && departments.size() > 0) {
				Iterator<Department> iter = departments.iterator();				
				while(iter.hasNext()) {
					i++;
					if(i > 1) {
						hasMoreJobs = true;
						break;
					}
					Department dept = iter.next();	
					sb.append(deptWithJob.get(dept).getName().concat(" - ").concat(dept.getName()));					
					
					String jobDeptPair = deptWithJob.get(dept).getName().concat(" - ").concat(dept.getName());					
					int remainingCharacters = length - personNameLength;
					
					if(remainingCharacters - jobDeptPair.length() < 0) {										
						sbNew = sb.delete(length - dots.length(), jobDeptPair.length() - 1);						
						sbNew.append(dots);	
						return sbNew.toString();
					} 				
				}
				if(hasMoreJobs) {
					sb.append(dots);
				}				
			}	
			logger.debug("getJobDepartmentPairsTruncate - END");			
			return sb.toString();
		}		
	}
				
	public String[] getUserGroups() {
		return userGroups;
	}
	public void setUserGroups(String[] userGroups) {
		this.userGroups = userGroups;
	}
	public void setDepts(Set<Department> depts) {
		this.depts = depts;
	}
	public Set<Department> getDepts() {
		return depts;
	}	
	
	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(byte status) {
		this.status = status;
	}
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
		sb.append("groups = ");
		if (userGroups != null) {
			for(int i =0; i < userGroups.length; i++) {
				sb.append(userGroups[i]).append(", ");
			}
		} else {
			sb.append("0");
		}

		sb.append("organisationId = ") .append(organisationId) .append("]");
	return sb.toString();
	}
	
}
