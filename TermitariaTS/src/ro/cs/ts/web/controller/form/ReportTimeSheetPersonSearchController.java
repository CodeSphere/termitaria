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
package ro.cs.ts.web.controller.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLPerson;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.SearchPersonBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.om.Person;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;

public class ReportTimeSheetPersonSearchController extends RootSimpleFormController {
	//------------------------VIEW------------------------------------------------------------------	
	private static final String FORM_VIEW						= "Reports_TS_PersonSearch";
	private static final String SUCCESS_VIEW					= "Reports_TS_PersonListing";
	
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY						= "person.";		
	private static final String GENERAL_ERROR					= ROOT_KEY.concat("general.error");	
	private static final String SEARCH_ERROR 					= ROOT_KEY.concat("search.error");		
	private static final String GET_PROJECTS_FROM_ORG_ERROR		= ROOT_KEY.concat("get.projects.from.organisation.error");	
	private static final String GET_PROJECTS_FOR_MANGER_ERROR	= ROOT_KEY.concat("get.projects.for.manager.error");	
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------	
	private static final String SEARCH_RESULTS 					= "SEARCH_RESULTS";
	private static final String SEARCH_RESULTS_JSON 			= "SEARCH_RESULTS_JSON";
	private static final String PAGES 							="pagination.pages";		
	private static final String PAGINATION_ERROR 				= ROOT_KEY.concat("pagination.error");
	private static final String SEARCH_PERSON_BEAN 				= "searchPersonBean";		
	private static final String PROJECTS						= "PROJECTS";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String PAGINATION 					= "pagination";
	private static final String ACTION 						= "action";
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";		
						
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 45;

			
	public ReportTimeSheetPersonSearchController() {
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(SEARCH_PERSON_BEAN);
		setCommandClass(SearchPersonBean.class);
	}
	
	/**
	 * @author Adelina
	 * @author Coni
	 */
	protected Object formBackingObject(HttpServletRequest request) {
		logger.debug("formBackingObject - START");
		SearchPersonBean searchPersonBean = new SearchPersonBean();		
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//we set the initial search parameters
		searchPersonBean.setSortDirection(IConstant.ASCENDING);
		searchPersonBean.setSortParam("lastName");		
		searchPersonBean.setOrganizationId(userAuth.getOrganisationId());
	
				
		logger.debug("formBackingObject - END");
		return searchPersonBean;		
	}
	
	/**
	 * @author Adelina
	 * @author Coni
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		logger.debug("onSubmit - START");
		
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		try {		
			if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))){
				mav = handlePagination(request, command, errorMessages);
			} else {
				mav = handleSearch(request, command, errorMessages);
			}	
			
			
		} catch(Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//Publish Info/Error messages
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("onSubmit - END");
		
		return mav;
	}
	
	/**
	 * 
	 * Handle the search person operation
	 * 
	 * @author Adelina
	 * @author Coni
	 * 
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @return
	 */
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages) {
		logger.debug("handleSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());		
		SearchPersonBean searchPersonBean = (SearchPersonBean) command;		
		
		List<Person> res = null;
		
		try{		
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			Integer personId = userAuth.getPersonId();
			boolean hasPermission = userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ReportsView());			
			res  = BLPerson.getInstance().listPersonsForPagination(searchPersonBean, userAuth.getOrganisationId(), personId, hasPermission, messageSource, true);		
		} catch (BusinessException be) {
			logger.error("", be);			
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));			
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, res);
		
		mav.addObject(SEARCH_RESULTS_JSON, buildSearchResultsJsonArray(res));
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchPersonBean, PAGES);
		
		// add the search bean on command
		mav.addObject(SEARCH_PERSON_BEAN, searchPersonBean);				
				
		logger.debug("handleSearch - END ");
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 * 
	 * @author Adelina
	 * @author Coni
	 *
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @return
	 */
	private ModelAndView handlePagination(HttpServletRequest request, Object command, ArrayList<String> errorMessages) {
		logger.debug("handlePagination - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchPersonBean searchPersonBean = (SearchPersonBean) command;		
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(searchPersonBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(searchPersonBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchPersonBean.setCurrentPage(searchPersonBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchPersonBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchPersonBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR,e);
			searchPersonBean.setCurrentPage(-1);		
		}
	
		List<Person> res = null;
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
			Integer personId = userAuth.getPersonId();
			boolean hasPermission = userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ReportsView());			
			res  = BLPerson.getInstance().listPersonsForPagination(searchPersonBean, userAuth.getOrganisationId(), personId, hasPermission, messageSource, true);	
			for(Person person : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(person.getFirstName().concat(" ").concat(person.getLastName()), NR_CHARS_PANEL_HEADER);
				person.setPanelHeaderName(panelHeaderName);
			}	
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));		
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
					
		mav.addObject(SEARCH_RESULTS, res);
		
		mav.addObject(SEARCH_RESULTS_JSON, buildSearchResultsJsonArray(res));		
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchPersonBean, PAGES);
		
		// add the command object on mav
		mav.addObject(SEARCH_PERSON_BEAN, searchPersonBean);
				
		logger.debug("handlePagination - END");
		return mav;
	}	
	
	/**
	 * @author Adelina
	 * @author Coni
	 */
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception {
		logger.debug("referenceData - START");
		//used as a container for error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		Map map = new HashMap();		
		
		//load the list of possible results per page
		map.put(IConstant.NOM_RESULTS_PER_PAGE, TSContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");	
				
		Integer organizationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		logger.debug("organizationId = " + organizationId);			
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();				
		Integer personId = userAuth.getPersonId();
 
		//get the projects from organisation		
		List<Project> projects = new ArrayList<Project>();				
		
		if(userAuth.hasAuthority(PermissionConstant.getInstance().getTS_ReportsView())) {	
			logger.debug("getAllProjects");
			try{
				projects = BLProject.getInstance().getAllProjects(organizationId, true);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_PROJECTS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}
		} else {
			logger.debug("getProjectsByManager");
			try{
				projects = BLProject.getInstance().getProjectsByManager(personId, true, true);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_PROJECTS_FOR_MANGER_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			} catch (Exception e){
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
						.getLocale(request)));
			}
		}
		
		// adding to model the projects from organization
		map.put(PROJECTS, projects);	
						
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}	

	/**
	 * @author Coni
	 * @param res
	 * @return
	 */
	private String buildSearchResultsJsonArray(List<Person> res) {
		logger.debug("buildSearchResultsJsonArray - START");
		//build the json array for the search results in order to use them in javascript
		JSONArray jsonArray = new JSONArray();
		for (Person pers : res) {
			JSONObject person = new JSONObject();
			person.accumulate("personId", pers.getPersonId());
			person.accumulate("firstName", pers.getFirstName());
			person.accumulate("lastName", pers.getLastName());
			person.accumulate("projectId", pers.getProjectId());
			person.accumulate("memberId", pers.getMemberId());
			if (pers.getProject()!=null){
			JSONObject project = new JSONObject();
				project.accumulate("projectId", pers.getProject().getProjectId());
				project.accumulate("name", pers.getProject().getName());
				person.accumulate("project", project);
			}else {
				person.accumulate("project", null);
			}
			jsonArray.add(person);
		}
		logger.debug("buildSearchResultsJsonArray - END");
		return(jsonArray.toString());
	}
}
