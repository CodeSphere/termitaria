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
package ro.cs.ts.web.controller.root;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Client;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.ApplicationObjectSupport;
import ro.cs.ts.common.ConfigParametersProvider;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.entity.Activity;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.PaginationBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.utils.TimeInterval;
import ro.cs.ts.utils.TimeIntervalComparator;
import ro.cs.ts.web.common.Month;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.entity.UserSimple;

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
     * Gets the number of minutes corresponding the time unit
     * 
     * @author Adelina
     * 
     * @param timeUnit
     * @param year
     * @param monthNum
     * @return
     */
    public Float getTimeUnitInMinutes(Short timeUnit, Integer year, Integer monthNum, boolean hasPeriod) {
    	logger.debug("getTimeUnitInMinutes - START");
    	Float nrOfMinutes = new Float(0);  
    	    	  
    	Integer workDays = new Integer(0); 
    	
    	if(hasPeriod) {	// if we have only the period of time, the standard work days are 21
    		workDays = IConstant.STANDARD_NR_OF_WORK_DAYS;
    	} else { // we calculate the number of work days       	
    		Integer numOfDays = getNumOfDays(year, monthNum);
    		logger.debug("numOfDays = " + numOfDays);
    		
        	Date date1 = new Date();
        	Date date2 = new Date();
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        	
        	String d1 = year.toString().concat("/").concat(monthNum.toString()).concat("/").concat("01");
        	String d2 = year.toString().concat("/").concat(monthNum.toString()).concat("/").concat(numOfDays.toString());
        	
        	try{
        		date1 = sdf.parse(d1);
        		date2 = sdf.parse(d2);
        	} catch (ParseException e) {}
    		    		
    		workDays = getWorkDays(date1, date2) + 1;
    	}    	
    	
    	logger.debug("workDays = " + workDays);
    	  
    	switch (timeUnit) {
			case IConstant.NOM_TIME_UNIT_HOUR:
				nrOfMinutes = new Float(60);
				break;
			case IConstant.NOM_TIME_UNIT_DAY:
				nrOfMinutes = new Float(60*8);
				break;
			case IConstant.NOM_TIME_UNIT_WEEK:
				nrOfMinutes = new Float(60*8*5);
				break;
			case IConstant.NOM_TIME_UNIT_MONTH:				
				nrOfMinutes = new Float(60*8*workDays);
				break;					
		}
    	
    	logger.debug("getTimeUnitInMinutes - END");    	
    	return nrOfMinutes;
    }
              
    /**
     * Get the work days between two dates
     * 
     * @author Adelina
     * 
     * @param startDt
     * @param endDt
     * @return
     */
    public Integer getWorkDays(Date startDt, Date endDt) {
       	logger.debug("getWorkDays - START");
    	
       	Calendar startCal,endCal;
    	startCal = Calendar.getInstance();
    	startCal.setTime(startDt);
    	endCal = Calendar.getInstance();
    	endCal.setTime(endDt);
    	int workDays = 0;

    	//Return 0 if start and end are the same
    	if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
    		return 0;
    	}
    	
    	//Just in case the dates were transposed this prevents infinite loop
    	if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
	    	startCal.setTime(endDt);
	    	endCal.setTime(startDt);
    	}

    	do {
	    	startCal.add(Calendar.DAY_OF_MONTH, 1);
	    	if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	    		++workDays;
	    	}
    	} while (startCal.getTimeInMillis() < endCal.getTimeInMillis() );    
      	
    	logger.debug("getWorkDays - END");
    	return workDays;
    }
    
    /**
     * Get the number of day by month, by year
     * 
     * @author Adelina
     * 
     * @param year
     * @param monthNum
     * @return
     */
    public Integer getNumOfDays(Integer year, Integer monthNum) {
    	logger.debug("getNumOfDays - START");
    	Integer numDays = null;    	
    	// if the month is february
    	// depending if the year is a leap year
    	// the number of days are different
        if (monthNum == 2) {
        	if (((year % 4 == 0) && !(year % 100 == 0)) || (year % 400 == 0)) {
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
        logger.debug("getNumOfDays - END");        
    	return numDays;
    }
    
    /**
     * Get minutes between two dates
     * 
     * @author Adelina
     * 
     * @param date1
     * @param date2
     * @return
     */
    public Long getMinutesBetweenDates(Date date1, Date date2) {
    	logger.debug("getMinutesBetweenDates - START");
    	Long minutes = new Long(TimeUnit.MINUTES.convert(date2.getTime()- date1.getTime(), TimeUnit.MINUTES))/((60*1000));
    	logger.debug("getMinutesBetweenDates - END");
    	return minutes;
    }
    
    /**
     * Checks is the difference between the two dates equls time
     * 
     * @author Adelina
     * 
     * @param date1
     * @param date2
     * @param time
     * @return
     */
    public boolean hasEqualTime(Date date1, Date date2, String time) {
    	logger.debug("hasEqualTime - START");
    	boolean isEqual = false;
    	
    	Long minutes = getMinutesBetweenDates(date1, date2);
    	Long hour = minutes/(long)60;
    	Long min = minutes%(long)60;
    	
    	String delimiter = ":";
    	
    	logger.debug("hour = " + hour);
    	logger.debug("min = " + min);
    	
    	String[] workTime = time.split(delimiter);
    	Long workHour = Long.parseLong(workTime[0]);
    	Long workMinutes = Long.parseLong(workTime[1]);
    	
    	logger.debug("workHour = " + workHour);
    	logger.debug("workMinutes = " + workMinutes);
    	
    	if(hour == workHour && min == workMinutes) {
    		isEqual = true;
    	}
    	    	
    	logger.debug("hasEqualTime - END, isEqual = ".concat(String.valueOf(isEqual)));
    	return isEqual;
    }
    
    /**
     * Calculate the price between two dates
     * If the period between the dates is big, like : months,
     * we calculate the price for each one
     * 
     * @author Adelina
     * 
     * @param cost
     * @param timeUnit
     * @param date1
     * @param date2
     * @return
     */
    public Float getPrice(Float cost, Short timeUnit, Date date1, Date date2){    
    	Float price = new Float(0);    	
		Long minutesN = new Long(0);
		String d1 = new String();
		String d2 = new String();
			
		// get the years, months, days, hours, minutes and seconds by dates
		SimpleDateFormat simpleYearformat=new SimpleDateFormat("yyyy");		
		Integer year1 = Integer.valueOf(simpleYearformat.format(date1));    		    	        	  
    	
      	SimpleDateFormat simpleMonthformat=new SimpleDateFormat("MM");
    	Integer month1 = Integer.valueOf(simpleMonthformat.format(date1));
    	Integer month2 = Integer.valueOf(simpleMonthformat.format(date2));    	   
       	          	
    	SimpleDateFormat simpleDayformat=new SimpleDateFormat("dd");
    	Integer day1 = Integer.valueOf(simpleDayformat.format(date1));
    	Integer day2 = Integer.valueOf(simpleDayformat.format(date2));    	
    	
    	SimpleDateFormat simpleHourformat=new SimpleDateFormat("HH");
    	Integer hour1 = Integer.valueOf(simpleHourformat.format(date1));
    	Integer hour2 = Integer.valueOf(simpleHourformat.format(date2));
    	
    	SimpleDateFormat simpleMinuteformat=new SimpleDateFormat("mm");
    	Integer minute1 = Integer.valueOf(simpleMinuteformat.format(date1));
    	Integer minute2 = Integer.valueOf(simpleMinuteformat.format(date2));    	
    	
    	SimpleDateFormat simpleSecondsformat=new SimpleDateFormat("ss");
    	Integer seconds1 = Integer.valueOf(simpleSecondsformat.format(date1));
    	Integer seconds2 = Integer.valueOf(simpleSecondsformat.format(date2));    	
    	    		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
		// if a month is less than another
    	while(month1 < month2) {        		
    		d1 = year1.toString().concat("/").concat(month1.toString()).concat("/").concat(day1.toString()).concat(" ").concat(hour1.toString()).concat(":").concat(minute1.toString()).concat(":").concat(seconds1.toString());
    		day1 = getNumOfDays(year1, month1);
    		hour1 = 23; minute1 = 59; hour1 = 00; minute1 = 00; seconds1 = 00;        		        	
    		d2 =  year1.toString().concat("/").concat(month1.toString()).concat("/").concat(day1.toString()).concat(" ").concat(hour1.toString()).concat(":").concat(minute1.toString()).concat(":").concat(seconds1.toString());
    		try{
        		date1 = sdf.parse(d1);
        		date2 = sdf.parse(d2);
        	} catch (ParseException e) {}
        	            	            	
        	minutesN = getMinutesBetweenDates(date1, date2);   
        	Float minutes = getTimeUnitInMinutes(timeUnit, year1, month1, false);
        	if(!minutes.equals(new Float(0))) {
        		price += (minutesN*cost)/minutes;  
        	}
    		day1 = 01; hour1 = 00; minute1 = 00; hour1 = 00; minute1 = 00; seconds1 = 00;  
        	month1++;
    	}
    	
    	d1 = year1.toString().concat("/").concat(month1.toString()).concat("/").concat(day1.toString()).concat(" ").concat(hour1.toString()).concat(":").concat(minute1.toString()).concat(":").concat(seconds1.toString());    				        
		d2 = year1.toString().concat("/").concat(month2.toString()).concat("/").concat(day2.toString()).concat(" ").concat(hour2.toString()).concat(":").concat(minute2.toString()).concat(":").concat(seconds2.toString());
		try{
    		date1 = sdf.parse(d1);
    		date2 = sdf.parse(d2);
    	} catch (ParseException e) {}
    	
    	minutesN = getMinutesBetweenDates(date1, date2);   
    	Float minutes = getTimeUnitInMinutes(timeUnit, year1, month2, false);
    	if(!minutes.equals(new Float(0))) {
    		price += (minutesN*cost)/minutes;
    	}
        		    		       
		return price;
    }
    
    
    /**
     * Calculate the price if we have the period of time
     * the difference between two dates
     * 
     * @author Adelina
     * 
     * @param cost
     * @param timeUnit
     * @param date1
     * @param date2
     * @return
     */
    public String calculatePrice(Float cost, Short timeUnit, Date date1, Date date2) {    	
    	logger.debug("calculatePrice - START");    	    
		
    	Float price  = new Float(0);
    	
    	SimpleDateFormat simpleYearformat=new SimpleDateFormat("yyyy");		
		Integer year1 = Integer.valueOf(simpleYearformat.format(date1));    
		Integer year2 = Integer.valueOf(simpleYearformat.format(date2));    	        	
    		
    	if(year1.equals(year2)) {    
    		price = getPrice(cost, timeUnit, date1, date2);
    	} else {   		    	
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		
    		Integer numOfDays = getNumOfDays(year1, 12);
    		String d2 = year1.toString().concat("/").concat("12").concat("/").concat(numOfDays.toString()).concat(" ").concat("23").concat(":").concat("59").concat(":").concat("00");
    		try{
        		date2 = sdf.parse(d2);        	
        	} catch (ParseException e) {}
    		Float price1 = getPrice(cost, timeUnit, date1, date2);
    		numOfDays = getNumOfDays(year1, 1);
    		String d1 = year2.toString().concat("/").concat("01").concat("/").concat(numOfDays.toString()).concat(" ").concat("00").concat(":").concat("00").concat(":").concat("00");
    		try{
        		date1 = sdf.parse(d1);        	
        	} catch (ParseException e) {}
    		Float price2 = getPrice(cost, timeUnit, date1, date2);
    		price = price1 + price2;
    	}
	    	
    	logger.debug("calculatePrice - END , price = " + price);    	    	
    	price = roundFloat(price);
    	if(getNrOfPlaces(price) == 1) {
    		return String.valueOf(price);
    	}
    	return price.toString();
    }
    
    /**
     * Calculate the price for a period of time as a string
     * 
     * @author Adelina
     * 
     * @param cost
     * @param timeUnit
     * @param period
     * @return
     */
    public String calculatePrice(Float cost, Short timeUnit, String period) {
    	logger.debug("calculatePrice - START");
    	Float price = new Float(0);
    	
    	logger.debug("period = " + period);
    	String delimiter = ":";
    	String[] time = period.split(delimiter);

    	Float hoursP = Float.valueOf(time[0]);
    	Float minutesP = Float.valueOf(time[1]);
    	
    	logger.debug("hours = " + hoursP);
    	logger.debug("minutes = " + minutesP);
    	
    	Float min = new Float(0);
    	
    	if(hoursP == 0) {
    		min = minutesP;
    	} else {
    		min = (hoursP * 60) + minutesP;
    	}
    	
    	Float minutes = getTimeUnitInMinutes(timeUnit, null, null, true);
    	if(!minutes.equals(new Float(0))) {
    		price += (min*cost)/minutes;
    	} 
    	
    	logger.debug("calculatePrice - END, price = " + price);
    	price = roundFloat(price);
    	if(getNrOfPlaces(price) == 1) {
    		return String.valueOf(price);
    	}    	
    	return price.toString();    	
    }
    
    /**
     * Round up the float number
     * 
     * @author Adelina
     * 
     * @param price
     * @return
     */
    public Float roundFloat(Float price){      
        int decimalPlace = 2;
        BigDecimal bd = new BigDecimal(price);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_UP);
        logger.debug("bd = " + bd);
        price = bd.floatValue();
        logger.debug("price = " + price);
        return price;
    }
    
    /**
     * Returns the numbers of places after the floating point
     * 
     * @author Adelina
     * 
     * @param price
     * @return
     */
    public Integer getNrOfPlaces(Float price) {
    	 String p = price.toString();
    	 int nrOfPlaces = p.substring(p.indexOf(".")).length() - 1;
    	 return nrOfPlaces;
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
	public String getClientsFromOrgAsJSON(List<Client> clients, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource){
		logger.debug("getClientsFromOrgAsJSON - START");
		JSONArray jsonArray = new JSONArray();		
				
		Iterator<Client> it = clients.iterator();
		JSONObject jsonObj = new JSONObject();
				 	
		jsonObj.accumulate("name", messageSource.getMessage(FROM_ORGANIZATION, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		jsonObj.accumulate("id", new Integer(-1));			
		jsonArray.add(jsonObj);
		jsonObj.clear();
		
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
	 * Used to retrieve a project details team members name, only if the user is PM for that project
	 * @author Coni
	 * @param projectId
	 * @param locale
	 * @param errorMessages
	 * @param messageSource
	 * @param personId
	 * @return
	 */
	public String getProjectTeamMembersAsJSON(Integer projectId, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource, int personId, boolean withoutDeletedPersons, TeamMember teamMember) {
		logger.debug("getProjectTeamMembersAsJSON - START");
		JSONArray jsonArray = new JSONArray();	
		logger.debug("withoutDeletedPersons = " + withoutDeletedPersons);
		logger.debug("teamMember = " + teamMember);
		try {
			Project project = null;			
			project = BLProject.getInstance().get(projectId, withoutDeletedPersons);	
			Set<TeamMember> teamMembers = project.getProjectTeam().getTeamMembers();
			logger.debug("teamMembers = " + teamMembers);			
			// and if the member is deleted but is the responsible of a cost or a record, add to the members, to display 
			if(teamMember != null && (!teamMembers.contains(teamMember))) {
				teamMembers.add(teamMember);
			}
			if (project != null) {
				Iterator<TeamMember> it = teamMembers.iterator();
				JSONObject jsonObj = new JSONObject();
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				//if the user is PM for the selected project or it is USER_ALL, all the projects team members will be displayed
				if (project.getManagerId().equals(personId) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordSearchAll()) 
						|| userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordAddAll()) || userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordUpdateAll())) {
					while (it.hasNext()) {
						TeamMember member = it.next();
						jsonObj.accumulate("name", member.getFirstName().concat(" ").concat(member.getLastName()));
						jsonObj.accumulate("id", member.getMemberId());
						jsonArray.add(jsonObj);
						jsonObj.clear();
					}
				} else {
					while (it.hasNext()) {
						TeamMember member = it.next();
						if (member.getPersonId() == personId) {
							jsonObj.accumulate("name", member.getFirstName().concat(" ").concat(member.getLastName()));
							jsonObj.accumulate("id", member.getMemberId());
							jsonArray.add(jsonObj);
							jsonObj.clear();
						}
					}
				}
			}
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
		}
		
		logger.debug("getProjectTeamMembersAsJSON - END - json:".concat(jsonArray.toString()));
		return jsonArray.toString();
	}
	
	/**
	 * Returns the activities for a project or for an organization
	 * @author Coni
	 * @param projectId
	 * @param locale
	 * @param errorMessages
	 * @param messageSource
	 * @param organizationId
	 * @return
	 */
	public String getProjectActivitiesAsJSON(Integer projectId, Locale locale, ArrayList<String> errorMessages, MessageSource messageSource, int organizationId) {
		logger.debug("getProjectActivitiesAsJSON - START");
		JSONArray jsonArray = new JSONArray();	
		try {
			List<Activity> activities = null;
			if (projectId.equals(IConstant.NOM_RECORD_FORM_PROJECT_SELECT_ORG_OPTION)) {
				activities = BLActivity.getInstance().getByOrganization(organizationId);
			} else {
				activities = BLActivity.getInstance().getByProjectId(projectId);
			}			
			if (activities != null) {
				JSONObject jsonObj = new JSONObject();
				for (Activity activity : activities) {
					jsonObj.accumulate("name", activity.getName());
					jsonObj.accumulate("id", activity.getActivityId());
					jsonArray.add(jsonObj);
					jsonObj.clear();
				}
			}
		} catch (BusinessException e) {
			logger.error(e.getMessage(), e);
		}
		
		logger.debug("getProjectActivitiesAsJSON - END - json:".concat(jsonArray.toString()));
		return jsonArray.toString();
	}
		
	/**
	 * Transform the date in "day.month.year hour:minute:sec"
	 * 
	 * @author mitziuro
	 * @param calendar
	 * @return
	 */
	public String getLocaleDate(Calendar calendar){
		
		StringBuffer result = new StringBuffer("");
		
		//date
		result.append(calendar.get(Calendar.DAY_OF_MONTH) <= 9 ? "0".concat(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))) : calendar.get(Calendar.DAY_OF_MONTH));
		result.append(".");
		result.append((calendar.get(Calendar.MONTH) + 1) <= 9 ? "0".concat(String.valueOf((calendar.get(Calendar.MONTH) + 1))) : (calendar.get(Calendar.MONTH) + 1));
		result.append(".");
		result.append(calendar.get(Calendar.YEAR) <= 9 ? "0".concat(String.valueOf(calendar.get(Calendar.YEAR))) : calendar.get(Calendar.YEAR));
		
		result.append(" ");
		
		//time
		result.append(calendar.get(Calendar.HOUR_OF_DAY) <= 9 ? "0".concat(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) : calendar.get(Calendar.HOUR_OF_DAY));
		result.append(":");
		result.append(calendar.get(Calendar.MINUTE) <= 9 ? "0".concat(String.valueOf(calendar.get(Calendar.MINUTE))) : calendar.get(Calendar.MINUTE));
		result.append(":");
		result.append(calendar.get(Calendar.SECOND) <= 9 ? "0".concat(String.valueOf(calendar.get(Calendar.SECOND))) : calendar.get(Calendar.SECOND));

		return result.toString();
	}
	
	/**
	 * Gets a list of currencies for the given organizationId
	 *
	 * @author Adelina
	 *
	 * @param organizationId
	 * @return
	 * @throws BusinessException
	 */
	public JSONArray getCurrencies(int organizationId) throws BusinessException{
		logger.debug("getCurrencies - START");
		JSONArray currecies = new JSONArray();
		JSONObject j = new JSONObject();
				
		List<Currency> res = BLCurrency.getInstance().getByOrganizationId(organizationId);
		logger.debug("results currencies = " + res);
		
		if(res != null && res.size() > 0) {
			for(int i = 0; i < res.size(); i++) {
				j = new JSONObject();
				j.accumulate("id", Integer.toString(res.get(i).getCurrencyId()));
				j.accumulate("name",res.get(i).getName());		
				currecies.add(j);
			}		
		}
		logger.debug("getCurrencies - END, currencies = ".concat(currecies.toString()));
		return currecies;
	}
	
	/**
	 * Get Today's hours and minutes
	 * 
	 * @author Adelina
	 * 
	 * @param calendar
	 * @return
	 */
	public String getTodayHoursAndMinutes(Calendar calendar) {
		StringBuffer result = new StringBuffer("");
				
		//time
		result.append(calendar.get(Calendar.HOUR_OF_DAY) <= 9 ? "0".concat(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) : calendar.get(Calendar.HOUR_OF_DAY));
		result.append(":");
		result.append(calendar.get(Calendar.MINUTE) <= 9 ? "0".concat(String.valueOf(calendar.get(Calendar.MINUTE))) : calendar.get(Calendar.MINUTE));		

		return result.toString();
	}
	
	/**
	 * Removes spaces from a string
	 * 
	 * @author Adelina
	 * 
	 * @param elem
	 * @return
	 */
	public String removeSpaces(String elem) {
		logger.debug("removeSpaces - START , for elem ".concat(elem));
		StringTokenizer st = new StringTokenizer(elem," ",false);
		String result="";
		while (st.hasMoreElements()) result += st.nextElement();
		logger.debug("removeSpaces - END , result ".concat(result));
		return result;
	}

	/**
	 * Compares two dates, returns the smallest value
	 * 
	 * @author Adelina
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public Date calculateLowerLimit(Date date1, Date date2) {
		logger.debug("calculateLowerLimit - START");
		logger.debug("date1  = " + date1);
		logger.debug("date2 = " + date2);
		Date date = new Date();
		if(date1.before(date2)) {
			date = date1;
		} else if(date1.after(date2)) {
			date = date2;
		} else {
			date = date1;
		}
		logger.debug("calculateLowerLimit - END, date = " + date);		
		return date;
	}
	
	/**
	 * Compares two dates, returns the greatest value
	 * 
	 * @author Adelina
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public Date calculateUperLimit(Date date1, Date date2) {
		logger.debug("calculateUperLimit - START");
		logger.debug("date1  = " + date1);
		logger.debug("date2 = " + date2);
		Date date = new Date();
		if(date1.before(date2)) {
			date = date2;
		} else if(date1.after(date2)) {
			date = date1;
		} else {
			date = date1;
		}
		logger.debug("calculateUperLimit - END, date = " + date);		
		return date;
	}
	
	/**
	 * 
	 * @author Adelina
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public TimeInterval calculateTimeInterval(Date date1, Date date2) {
		logger.debug("calculateHoursForInterval - START");
		Long minutes = getMinutesBetweenDates(date1, date2);		
		logger.debug("minutes = " + minutes);
		Long hours = (minutes / 60) | 0;
		Long min = minutes % 60;
		TimeInterval interval = new TimeInterval(hours, min);
		logger.debug("interval = " + interval);
		logger.debug("calculateHoursForInterval - END, interval = " + interval);
		return interval;
	}
			
	/**
	 * Transform string to TimeInterval
	 * 
	 * @author Adelina 
	 * 
	 * @param time
	 * @return
	 */
	public TimeInterval transformStringToTimeInterval(String time) {
		logger.debug("transformStringToTimeInterval - START");
		String delimiter = ":";
		//hour and minutes			
    	String[] timeArray = time.split(delimiter);
		TimeInterval  timeInterval = new TimeInterval(Long.valueOf(timeArray[0]), Long.valueOf(timeArray[1])) ;
		logger.debug("transformStringToTimeInterval - END");
		return timeInterval;
	}
	
	/**
	 * If exists an overlap
	 * 
	 * @author Adelina
	 * 
	 * @param start1
	 * @param start2
	 * @param startTime
	 * @param end1
	 * @param end2
	 * @param endTime
	 * @return
	 */
	public boolean hasOverlap(Date start1, Date start2, String startTime, Date end1, Date end2, String endTime) {		
		logger.debug("hasOverlap - START");		
		boolean hasOverlap = false;
		
		Date lmin = calculateLowerLimit(start1, end1);
		logger.debug("min limit = " + lmin);
		Date lmax = calculateUperLimit(start2, end2);
		logger.debug("max limit = " + lmax);
		
		TimeInterval lInterval = calculateTimeInterval(lmin, lmax);
		logger.debug("interval limit = " + lInterval);
		
		TimeInterval start = transformStringToTimeInterval(startTime);
		TimeInterval end = transformStringToTimeInterval(endTime);		
		Long hours = start.getHours() + end.getHours() + ((start.getMinutes() + end.getMinutes()) / 60);		
		logger.debug("hours = " + hours);
		Long minutes = (start.getMinutes() + end.getMinutes())%60;
		logger.debug("minutes = " + minutes);
		TimeInterval interval = new TimeInterval(hours, minutes);
		logger.debug("interval = " + interval);
		
		Integer compareHours = TimeIntervalComparator.getInstance().hoursComparator().compare(interval, lInterval);
		logger.debug("compareHours = " + compareHours);
		if(compareHours > 0) {
			hasOverlap = true;
		} else if(compareHours == 0) {
			Integer compareMinutes = TimeIntervalComparator.getInstance().minutesComparator().compare(interval, lInterval);
			logger.debug("compareMinutes = " + compareMinutes);
			if(compareMinutes > 0) {
				hasOverlap = true;
			} else {
				hasOverlap = false;
			}
		}
		logger.debug("hasOverlap - END, hasOverlap = " + hasOverlap);		
		return hasOverlap;
	}

}
