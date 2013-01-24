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
package ro.cs.om.web.controller.root;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.WordUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLJob;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.ApplicationObjectSupport;
import ro.cs.om.common.ConfigParametersProvider;
import ro.cs.om.common.IConstant;
import ro.cs.om.entity.Department;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Localization;
import ro.cs.om.entity.PaginationBean;
import ro.cs.om.entity.Person;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.nom.StringString;
import ro.cs.om.web.common.Month;


/**
 * Singleton used in controllers for different operations
 * 
 * @author matti_joona
 */
public class ControllerUtils extends ApplicationObjectSupport {

    
	//----------------------- ERROR MESSAGES CODES -----------------------------------------
	private String GET_PERSONS_FROM_ORG_AS_JSON_ERROR = "person.json.error";
	private String GET_PERSONS_ACTIVATES_FROM_ORG_AS_JSON_ERROR = "person.activated.json.error";
	private static final String GET_DEPARTMENTS_FROM_ORG_AS_JSON_ERROR = "department.json.error";
	private static final String SETTER    		= "ro.cs.om.entity.Localization";
	private static final String DEPARTMENT_FAKE = "department.fake";
	
	
	
	private static ControllerUtils theInstance = null;
    private ControllerUtils(){};
    static {
        theInstance = new ControllerUtils();
    }
    public static ControllerUtils getInstance() {
        return theInstance;
    }
    
    private SimpleDateFormat sdf = new SimpleDateFormat(ConfigParametersProvider.getConfigString("date.format"));
       
    /**
     * Returns the formatted current time
     *  
     * @author dd 
     * @return String
     */
    public String getFormattedCurrentTime() {
		return sdf.format(new Timestamp(System.currentTimeMillis()));
    }
    
    public List<List> getMonthsAndDaysAndYears(HttpServletRequest request, MessageSource messageSource) {
    	List<List> result = new ArrayList<List>();
    	Locale locale = LocaleContextHolder.getLocale();
		List<Month> months = new ArrayList<Month>();
		List<String> days = new ArrayList<String>();
		List<String> years = new ArrayList<String>();
		Date date = new Date();
		SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
		String year = simpleDateformat.format(date);
		logger.debug("year = " + year);		
				
		for(int i = Integer.parseInt(year); i >= 1900; i--){
			years.add(Integer.toString(i));
		}
		
		for(int i = 0; i < 12; i++) {
			Month month = new Month();
			month.setMonthId(i);
			month.setMonthName(messageSource.getMessage("month.".concat(Integer.toString(i)), null, locale));
			months.add(month);
			days.add(Integer.toString(i+1));
		}
		//the remaining days
		for(int i = 13; i < 32; i++) {
			days.add(Integer.toString(i));
		}
		result.add(days);
		result.add(months);
		result.add(years);
    	return result;
    }
    
	/**
	 * Sets the upper and the lower limit of shown pages in pagination area
	 * 
	 * @author alu
	 * 
	 * @param searchBean
	 */
	public void findPagesLimit(PaginationBean paginationBean, String PAGES){
		logger.debug("findPagesLimit - begin");
		logger.debug("nrOfPages " + paginationBean.getNbrOfPages());
		int pages = Integer.parseInt(ConfigParametersProvider.getConfigString(PAGES));
		logger.debug("Pages "+ pages);
		int upperLimit = 0;
		int lowerLimit = 0;
		if (paginationBean.getNbrOfPages() == 0){
			lowerLimit = 0;
			upperLimit = 0;
		} else if (paginationBean.getNbrOfPages() < pages){
			lowerLimit = 1;
			upperLimit = paginationBean.getNbrOfPages();
		} else if (paginationBean.getCurrentPage() == 1){
			lowerLimit = 1;
			upperLimit = pages;
		} else if (paginationBean.getCurrentPage() + pages > paginationBean.getNbrOfPages()){
			lowerLimit = paginationBean.getNbrOfPages() - pages + 1;
			upperLimit = paginationBean.getNbrOfPages();
		} else {
			lowerLimit = paginationBean.getCurrentPage();
			upperLimit = paginationBean.getCurrentPage() + pages - 1;
		}
		logger.debug("Lower Limit " + lowerLimit);
		logger.debug("Upper Limit " + upperLimit);
		paginationBean.setLowerLimit(lowerLimit);
		paginationBean.setUpperLimit(upperLimit);
		logger.debug("findPagesLimit - end");
	}
	
	
	/**
	 * Returns Organization Id stored on session.
	 * 
	 * @author dan.damian 
	 * 
	 * @return organizationId
	 */
	public Integer getOrganisationIdFromSession(HttpServletRequest request) {
		Object orgId = request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		if (orgId != null && orgId instanceof Integer) {
			return (Integer) orgId;
		} else {
			return new Integer(-1);
		}
	}
	
	/**
	 * Returns Organization Name stored on session.
	 * 
	 * @author dan.damian 
	 * 
	 * @return organizationName
	 */
	public String getOrganisationNameFromSession(HttpServletRequest request) {
		String orgName = (String) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_NAME);
		if (orgName != null && orgName instanceof String) {
			return orgName;
		} else {
			return "<none>";
		}
	}
	
	/**
	 * Returns Organisation Status stored on session.
	 * 
	 * @author Adelina
	 * 
	 * return Integer
	 */
	public Integer getOrganisationStatusFromSession(HttpServletRequest request) {
		Integer organisationStatus = (Integer)request.getSession().getAttribute(IConstant.SESS_ORGANISATION_STATUS);
		if(organisationStatus != null && organisationStatus instanceof Integer) {
			return organisationStatus;
		} else {
			return new Integer(-1);
		}
	}
	
	/**
	 * Returns a string (JSON pattern) of all persons from an organization
	 *
	 * @author alu
	 * 
	 * @param orgId
	 * @param locale
	 * @param errorMessages
	 * @return
	 */
	public String getPersonsFromOrgAsJSON(int orgId, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource){
		logger.debug("getPersonsFromOrgAsJSON - START - orgID:".concat(String.valueOf(orgId)));
		JSONArray jsonArray = new JSONArray();		
		try {
			// get all persons from user's organization
			// Tools.getInstance().printList(logger, BLPerson.getInstance().getPersonsByOrganizationId(orgId));
			Iterator<Person> it = BLPerson.getInstance().getPersonsByOrganizationId(orgId).iterator();
			JSONObject jsonObj = new JSONObject();
			while(it.hasNext()){
				Person pers = (Person) it.next();
				
				// add the name and the id
				jsonObj.accumulate("id", pers.getPersonId());
				jsonObj.accumulate("name", pers.getFirstName().concat(" ").concat(pers.getLastName()));
				jsonArray.add(jsonObj);
				jsonObj.clear();
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_FROM_ORG_AS_JSON_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_FROM_ORG_AS_JSON_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		logger.debug("getPersonsFromOrgAsJSON - END - json:".concat(jsonArray.toString()));
		return jsonArray.toString();
	}	
	
	/**
	 * Returns a string (JSON pattern) of all persons from an organization containing the name (firstName + lastName) and
	 * the firstName and the lastName separately
	 *
	 * @author coni
	 * 
	 * @param orgId
	 * @param locale
	 * @param errorMessages
	 * @return
	 */
	public String getPersonsFirstNameLastNameFromOrgAsJSON(int orgId, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource){
		logger.debug("getPersonsFirstNameLastNameFromOrgAsJSON - START - orgID:".concat(String.valueOf(orgId)));
		JSONArray jsonArray = new JSONArray();		
		try {
			Iterator<Person> it = BLPerson.getInstance().getPersonsByOrganizationId(orgId).iterator();
			JSONObject jsonObj = new JSONObject();
			while(it.hasNext()){
				Person pers = (Person) it.next();
				
				// add the name and the id
				jsonObj.accumulate("name", pers.getFirstName().concat(" ").concat(pers.getLastName()));
				jsonObj.accumulate("firstName", pers.getFirstName());
				jsonObj.accumulate("lastName", pers.getLastName());
				jsonArray.add(jsonObj);
				jsonObj.clear();
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_FROM_ORG_AS_JSON_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_FROM_ORG_AS_JSON_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		logger.debug("getPersonsFromOrgAsJSON - END - json:".concat(jsonArray.toString()));
		return jsonArray.toString();
	}
	
	public boolean fromThisUrl(String url, HttpServletRequest request) {
		String requestUri = (request.getRequestURI() != null) ? request.getRequestURI() : ""; 
		logger.debug("Request URL: " + requestUri);
		if (requestUri.indexOf(url) > 0) {
			return true;
		} else return false;
		
	}
	
	/** Adds to model a list with all the accepted application's languages.
	 * 
	 * Also adds previous selected language, found in the client's cookie, 
	 * created in the past by CookieLocaleResolver.
	 * 
	 * @author dd 
	 * @param request
	 */
	public void publishLocales(HttpServletRequest request) {
		logger.debug("Publish Locales");
		// A list with all accepted languages
		List<StringString> languages = new ArrayList<StringString>();
		StringString language = null;
		try{
			// Language index
			int i = 1;
			// Language code prefix
			String code = "accepted.language.code.";
			// Language name prefix
			String name = "accepted.language.name.";
			// Flag indicating the end of this operation
			boolean end = false;
			logger.debug("begin");
			while(!end) {
				logger.debug("-> " + Integer.toString(i));
				language = new StringString();
				language.setValue(ConfigParametersProvider.getConfigStringProtected(
						code.concat(Integer.toString(i))));
				language.setLabel(ConfigParametersProvider.getConfigStringProtected(
						name.concat(Integer.toString(i))));
				if (language.getValue() != null && language.getLabel() != null) {
					// If both key and value are valid (!null), add the language to list
					languages.add(language);
					// Increment index (scanning next entry)
					i++;
				}else {
					// End of this operation reached
					end = true;
				}
			}
			logger.debug("end");
		}catch(Exception ex) {
			logger.error("Exception aroused ! Adding default language...", null);
			// If any exception appeared, I'll add the default language
			languages.add(new StringString("en","English"));
		}
		// Adding to request the Attribute with all accepted languages
		request.setAttribute(IConstant.ACCEPTED_LANGUAGES, languages);
		// Retrieve CookieLocaleResolver
		SessionLocaleResolver localeResolver = (SessionLocaleResolver) RequestContextUtils.getLocaleResolver(request);
		logger.debug("localeResolver: " + localeResolver);
		// Adding to request the Attribute with the selected siteLanguage
		// SiteLanguage is retrieved from a client's cookie (if any present).
		request.setAttribute(IConstant.LANGUAGE_ATTRIBUTE, localeResolver.resolveLocale(request).getLanguage());
	}
	/**
	 * 
	 * Gets description in locale language from localization
	 * @author mitziuro
	 *
	 * @param localization
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	public String getDescriptionForLocale(Localization localization, String locale) throws Exception{
		
		//we use java reflexive to call dynamic method
		Class usedClass = Class.forName(SETTER);
		Class[] partypes = new Class[]{};
		Object[] arglist = new Object[]{};
		
		Method meth = usedClass.getMethod("get".concat(WordUtils.capitalize(locale)), partypes);
		String output = (String) meth.invoke(localization, arglist);
		return output;
	}
	
	/**
	 * Returns a string (JSON pattern) of departments from an organization 
	 * containing the name's of the departments
	 *  
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param locale
	 * @param errorMessages
	 * @param messageSource
	 * @return
	 */
	public String getDepartmentFromOrgAsJSON(Integer organisationId, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource){
		logger.debug("getDepartmentFromOrgAsJSON - START - ORGANISATION ".concat(String.valueOf(organisationId)));
		
		JSONArray jsonArray = new JSONArray();
		
		try {
			Set<Department> departments = BLOrganisation.getInstance().getDepartments(organisationId);
			JSONObject jsonObj = new JSONObject();
			for(Department department : departments) {
				if(department.getStatus() != IConstant.NOM_DEPARTMENT_FAKE){
					jsonObj.accumulate("name", department.getName().concat(" "));
					jsonObj.accumulate("id", department.getDepartmentId());
					jsonArray.add(jsonObj);
					jsonObj.clear();
				}
			}		
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_DEPARTMENTS_FROM_ORG_AS_JSON_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_DEPARTMENTS_FROM_ORG_AS_JSON_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
				
		logger.debug("getDepartmentFromOrgAsJSON - END - json:".concat(jsonArray.toString()));
		return jsonArray.toString();
	}
	
	
	/**
	 * Returns a string (JSON pattern) of all departments, inclusive with fake dept, from an organization 
	 * containing the name's of the departments
	 *  
	 * @author Adelina
	 * 
	 * @param organisationId
	 * @param locale
	 * @param errorMessages
	 * @param messageSource
	 * @return
	 */
	public String getDepartmentsWithFakeFromOrgAsJSON(HttpServletRequest request, Integer organisationId, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource){
		logger.debug("getDepartmentFromOrgAsJSON - START - ORGANISATION ".concat(String.valueOf(organisationId)));
		
		JSONArray jsonArray = new JSONArray();
		
		try {
			Set<Department> departments = BLOrganisation.getInstance().getDepartments(organisationId);
			JSONObject jsonObj = new JSONObject();
			for(Department department : departments) {
				if(department.getStatus() != IConstant.NOM_DEPARTMENT_FAKE){
					jsonObj.accumulate("name", department.getName().concat(" "));
					jsonObj.accumulate("id", department.getDepartmentId());
					jsonArray.add(jsonObj);
					jsonObj.clear();
				} else {
					jsonObj.accumulate("name", messageSource.getMessage(DEPARTMENT_FAKE, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)).concat(" "));
					jsonObj.accumulate("id", department.getDepartmentId());
					jsonArray.add(jsonObj);
					jsonObj.clear();
				}
			}		
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_DEPARTMENTS_FROM_ORG_AS_JSON_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_DEPARTMENTS_FROM_ORG_AS_JSON_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
				
		logger.debug("getDepartmentFromOrgAsJSON - END - json:".concat(jsonArray.toString()));
		return jsonArray.toString();
	}
	
	/**
	 * Gets a list of jobs for the given organizationId
	 *
	 * @author	
	 *
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public JSONArray getJobs(int organizationId) throws BusinessException{
		JSONArray jobs = new JSONArray();
		JSONObject j = new JSONObject();
		
		//List<Job> res = BLJob.getInstance().getActiveJobsByOrganisationId(organizationId);
		List<Job> res = BLJob.getInstance().getJobsByOrganisationId(organizationId);
		
		for(int i=0; i < res.size(); i++) {
			j = new JSONObject();
			j.accumulate("id", Integer.toString(res.get(i).getJobId()));
			j.accumulate("name",res.get(i).getName());
			j.accumulate("status", res.get(i).getStatus());
			jobs.add(j);
		}
		
		return jobs;
	}
	
	
	/**
	 * returns a JSONArray containing:
	 * -the department persons id and name
	 * -the person job name and id in that specific department 
	 * 
	 * @author 
	 * 
	 * @param persons
	 * @param department
	 * @return
	 */
	public JSONArray getDepartmentPersonsJobs(Set<Person> persons, Department department)	{
		JSONArray personsWithJobs = new JSONArray();
		JSONObject pers;
		int size = persons.size();
		Iterator<Person> it = persons.iterator();
		
		for(int i=0; i< size; i++) {
			Person person = (Person) it.next();				
			pers = new JSONObject();
			if(person.getPersonId() > 0){				
				
				if ( person.getDeptWithJob().get(department) != null ){					
					pers.accumulate("jobId", person.getDeptWithJob().get(department).getJobId());	
					pers.accumulate("jobName",person.getDeptWithJob().get(department).getName() );
				}
			}
			pers.accumulate("personId", person.getPersonId());			
			pers.accumulate("personName", person.getFirstName().concat(" ").concat(person.getLastName()));
			personsWithJobs.add(pers);
		}
		return personsWithJobs;
	}
	
	
	/**
	 * Get a list of jobs for the given Person
	 * 
	 * @author
	 * 
	 * @param person
	 * @return
	 */
	public JSONArray getPersonJobs(Person person)	{
		JSONArray jobs = new JSONArray();
		JSONObject j;
		int size = person.getDepts().size();
		Iterator<Department> it = person.getDepts().iterator();
		
		for(int i=0; i< size; i++) {
			Department dept = (Department) it.next();				
			j = new JSONObject();
			if(person.getPersonId() > 0){
				if ( person.getDeptWithJob().get(dept) != null ){
					j.accumulate("id", person.getDeptWithJob().get(dept).getJobId());	
					j.accumulate("name",person.getDeptWithJob().get(dept).getName() );
					j.accumulate("status", person.getDeptWithJob().get(dept).getStatus());
				
				}
			}
			j.accumulate("departmentId", dept.getDepartmentId());			
			j.accumulate("departmentName", dept.getName());
			jobs.add(j);
		}
		return jobs;
	}	
	
	/**
	 * Tokenize a field, that it can fit the display panel
	 * 
	 * @author Adelina
	 * 
	 * @param field, numberOfCharsPerLine
	 * @return String
	 */
	public String tokenizeField(String field, Integer numberOfCharsPerLine) {
		logger.debug("tokenizeField - START");
		String [] words = null;
		String splitPattern = " ";
	    words = field.split(splitPattern);
	    
	    for (int i = 0 ; i < words.length ; i++) {	    	
	        //logger.debug("W = " + words[i]);
	        String word = words[i];	  
	        String temp = "";
	        while(word.length() > numberOfCharsPerLine) {
	        	Integer endIndex = numberOfCharsPerLine - 1;
	        	String newLine = "<br/>";
	        	words[i] = temp + word.substring(0, endIndex) + newLine;
	        	//logger.debug("words[i] = " + words[i]);
	        	word = word.substring(endIndex, word.length());
	        	//logger.debug("word = " + word);
	        	temp = words[i];
	        	//logger.debug("temp = " + temp);
	        }
	   
	        if(word.length() != words[i].length()) {
	        	words[i] = words[i].concat(word);
	        }
	    }
	    String result = "";
	    for(int j = 0; j < words.length; j++) {
	    	result = result.concat(words[j]).concat(splitPattern);
	    } 
	 
	    logger.debug("tokenizeField - END");
	    return result;
	}
	
	/**
	 * Truncate the panel's header name
	 * 
	 * @author Adelina
	 * 
	 * @param name, numberOfChars
	 * @return String
	 */
	public String truncateName(String name, Integer numberOfChars) {
		logger.debug("truncateName - START");
		
		String dots = " ...";
		
		if(name.length() > numberOfChars) {
			name = name.substring(0, numberOfChars).concat(dots);			
		}		
	    return name;
	}
	
	/**
	 * Returns a string (JSON pattern) of all persons from an organization that are not deleted neither disabled
	 *
	 * @author Adelina
	 * 
	 * @param orgId
	 * @param locale
	 * @param errorMessages
	 * @return
	 */
	public String getPersonsActivated(Integer organizationId, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource){
		logger.debug("getPersonsActivated - START - orgID:".concat(String.valueOf(organizationId)));
		JSONArray jsonArray = new JSONArray();		
		try {
			// get all persons from user's organization			
			Iterator<Person> it = BLPerson.getInstance().getPersonsActivated(organizationId).iterator();
			JSONObject jsonObj = new JSONObject();
			while(it.hasNext()){
				Person pers = (Person) it.next();
				
				// add the name and the id
				jsonObj.accumulate("id", pers.getPersonId());
				jsonObj.accumulate("name", pers.getFirstName().concat(" ").concat(pers.getLastName()));
				jsonArray.add(jsonObj);
				jsonObj.clear();
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_ACTIVATES_FROM_ORG_AS_JSON_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_ACTIVATES_FROM_ORG_AS_JSON_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		
		logger.debug("getPersonsActivated - END - json:".concat(jsonArray.toString()));
		return jsonArray.toString();
	}	
	
	 /**
     * Get the number of day by year and by month
     * 
     * @author Adelina
     * 
     * @param year
     * @param monthNum
     * @return
     */
    public Integer getNumOfDays(Integer year, Integer monthNum) {
    	logger.debug("getNumOfDays - START");   
    	logger.debug("year = " + year + ", monthNom = " + monthNum);
    	Integer numDays = null;    	
    	// if the month is february
    	// depending if the year is a leap year
    	// the number of days are different    	
        if (monthNum == 2) {
        	if ( ((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0)) {
        		numDays = 29;
        	} else {
        		numDays = 28;
        	}                                                           
        }
        else if (monthNum == 1 || monthNum == 3 || monthNum == 5 || monthNum == 7 || monthNum == 8  || monthNum == 10 || monthNum == 12) {
        	 numDays = 31;
        } else {
        	 numDays = 30;
        }              
        logger.debug("getNumOfDays - END, nrOfDays = " + numDays);        
    	return numDays;
    }
		
}
