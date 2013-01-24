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
package ro.cs.cm.web.controller.root;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import ro.cs.cm.common.ApplicationObjectSupport;
import ro.cs.cm.common.ConfigParametersProvider;
import ro.cs.cm.common.IConstant;
import ro.cs.cm.entity.Client;
import ro.cs.cm.entity.PaginationBean;
import ro.cs.cm.web.common.Month;
import ro.cs.cm.web.security.UserAuth;
import ro.cs.cm.ws.client.om.entity.UserSimple;

/**
 * Singleton used in controllers for different operations
 * 
 * @author matti_joona
 */
public class ControllerUtils extends ApplicationObjectSupport{
	
	private static final String FROM_ORGANIZATION 					= "project.from.organization";
	
	//----------------------- ERROR MESSAGES CODES -----------------------------------------
	
    private static ControllerUtils theInstance = null;
    private ControllerUtils(){};
    static {
        theInstance = new ControllerUtils();
    }
    public static ControllerUtils getInstance() {
        return theInstance;
    }
    private SimpleDateFormat sdf = new SimpleDateFormat(ConfigParametersProvider.getConfigString("date.format"));
    
    //---------------------------------------------------------------
    public UserAuth getUserObject(HttpServletRequest request){
    	return (UserAuth) request.getSession().getAttribute(UserAuth.KEY);
    }
    
    public void removeUserObject(HttpServletRequest request){
    	request.getSession().removeAttribute(UserAuth.KEY);
    }
    
    /**
     * Returns the formatted current time
     *  
     * @author dan.damian 
     * @return String
     */
    public String getFormattedCurrentTime() {
		return sdf.format(new Timestamp(System.currentTimeMillis()));
    }
    
   /**
	 * Sets the upper and the lower limit of shown pages in pagination area
	 * 
	 * @author Adelina
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
	 * Returns a string (JSON pattern) of all persons from an organization containing the name (firstName + lastName) and
	 * the firstName and the lastName separately
	 *
	 * @author Adelina
	 * 	
	 * @param locale
	 * @param errorMessages
	 * @return
	 */
	public String getPersonsFirstNameLastNameFromOrgAsJSON(List<UserSimple> users, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource){
		logger.debug("getPersonsFirstNameLastNameFromOrgAsJSON - START");
		JSONArray jsonArray = new JSONArray();		
				
		Iterator<UserSimple> it = users.iterator();
		JSONObject jsonObj = new JSONObject();
		while(it.hasNext()){			
			UserSimple user = (UserSimple)it.next();				
			logger.debug("user = " + user);
			// add the name and the id
			jsonObj.accumulate("name", user.getFirstName().concat(" ").concat(user.getLastName()));
			jsonObj.accumulate("id", user.getUserId());				
			jsonArray.add(jsonObj);
			jsonObj.clear();
		}
		
		logger.debug("getPersonsFromOrgAsJSON - END - json:".concat(jsonArray.toString()));
		
		return jsonArray.toString();
	}
	
	/**
	 * Returns a string (JSON pattern) of all clients from an organization containing 
	 * the name of the company or the name of the person (firstName + lastName) 
	 *
	 * @author Adelina
	 * 	
	 * @param locale
	 * @param errorMessages
	 * @return
	 */
	public String getClientsFromOrgAsJSON(List<Client> clients, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource, boolean forSearch){
		logger.debug("getClientsFromOrgAsJSON - START");
		JSONArray jsonArray = new JSONArray();		
				
		Iterator<Client> it = clients.iterator();
		JSONObject jsonObj = new JSONObject();
		
		if(forSearch) {
			jsonObj.accumulate("name", messageSource.getMessage(FROM_ORGANIZATION, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
			jsonObj.accumulate("id", new Integer(-1));			
			jsonArray.add(jsonObj);
			jsonObj.clear();
		}
							
		while(it.hasNext()){			
			Client client = (Client)it.next();				
			logger.debug("client = " + client);
			// add the name and id
			if(client.getType() == IConstant.NOM_CLIENT_TYPE_FIRM) {
				// the client is a company
				jsonObj.accumulate("name", client.getC_name());				
			} else {
				// the client is a person
				jsonObj.accumulate("name", client.getP_firstName().concat(" ").concat(client.getP_lastName()));				
			}				
			jsonObj.accumulate("id", client.getClientId());				
			jsonArray.add(jsonObj);
			jsonObj.clear();
		}
					
		logger.debug("getClientsFromOrgAsJSON - END - json:".concat(jsonArray.toString()));
		
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
