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
package ro.cs.logaudit.web.controller.root;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.MessageSource;
import ro.cs.logaudit.business.BLUser;
import ro.cs.logaudit.common.ApplicationObjectSupport;
import ro.cs.logaudit.common.ConfigParametersProvider;
import ro.cs.logaudit.common.IConstant;
import ro.cs.logaudit.entity.PaginationBean;
import ro.cs.logaudit.exception.BusinessException;
import ro.cs.logaudit.web.security.UserAuth;
import ro.cs.logaudit.ws.client.om.entity.UserSimple;

/**
 * Singleton used in controllers for different operations
 * 
 * @author matti_joona
 */
public class ControllerUtils extends ApplicationObjectSupport{
	
	//----------------------- ERROR MESSAGES CODES -----------------------------------------
	private String GET_PERSONS_FROM_ORG_AS_JSON_ERROR = "user.json.error";

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
	 * Returns a string (JSON pattern) of all persons from an organization
	 *
	 * @author coni
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
			Iterator<UserSimple> it = BLUser.getInstance().getUsersSimpleByOrganizationId(orgId, false).iterator();
			JSONObject jsonObj = new JSONObject();
			while(it.hasNext()){
				UserSimple user = (UserSimple) it.next();
				
				// add the name and the id
				jsonObj.accumulate("id", user.getUserId());
				jsonObj.accumulate("name", user.getFirstName().concat(" ").concat(user.getLastName()));
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
	
}
