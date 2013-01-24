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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Client;
import ro.cs.ts.cm.Project;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.SearchProjectBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class ProjectSearchController extends RootSimpleFormController{
	
	//------------------------VIEW------------------------------------------------------------------	
	private static final String FORM_VIEW						= "Project_Search";
	private static final String SUCCESS_VIEW					= "Project_Listing";
	
	
	//------------------------MESSAGE KEY--------------------------------------------------------------
	private static final String ROOT_KEY						= "project.";		
	private static final String GENERAL_ERROR					= ROOT_KEY.concat("general.error");	
	private static final String SEARCH_ERROR 					= ROOT_KEY.concat("search.error");		
	private String GET_CLIENTS_FROM_ORG_ERROR					= ROOT_KEY.concat("get.clients.by.organisation.error");
	private String GET_PERSONS_FROM_ORG_ERROR					= ROOT_KEY.concat("get.persons.by.organisation.error");	
	
	//------------------------OTHER PARAMETERS---------------------------------------------------------	
	private static final String SEARCH_RESULTS 					= "SEARCH_RESULTS";
	private static final String PAGES 							="pagination.pages";		
	private static final String PAGINATION_ERROR 				= ROOT_KEY.concat("pagination.error");
	private static final String SEARCH_PROJECT_BEAN 			= "searchProjectBean";	
	private static final String JSON_PERSONS 					= "JSON_PERSONS";
	private static final String JSON_CLIENTS					= "JSON_CLIENTS";
	private static final String USER_ID							= "USER_ID";	
	private static final String PROJECT_STATUS					= "PROJECT_STATUS";
	
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

			
	public ProjectSearchController() {
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName(SEARCH_PROJECT_BEAN);
		setCommandClass(SearchProjectBean.class);
	}
	
	/**
	 * @author Adelina
	 */
	protected Object formBackingObject(HttpServletRequest request) {
		logger.debug("formBackingObject - START");
		SearchProjectBean spb = new SearchProjectBean();
		
		//we set the initial search parameters
		spb.setSortDirection(IConstant.ASCENDING);
		spb.setSortParam("name");
				
		logger.debug("formBackingObject - END");
		return spb;
		
	}
	
	/**
	 * @author Adelina
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
	 * Handle the search projects operation
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @return
	 */
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages) {
		logger.debug("handleSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());		
		SearchProjectBean searchProjectBean = (SearchProjectBean) command;
		
		List<Project> res = null;
		
		try{
		
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Integer userId = userAuth.getPersonId();
			Integer organizationId = userAuth.getOrganisationId();
			res  = BLProject.getInstance().listProjectsForPagination(searchProjectBean, userId, organizationId, messageSource);		
			
			for(Project project : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(project.getName(), NR_CHARS_PANEL_HEADER);
				project.setPanelHeaderName(panelHeaderName);
			}	
			
		} catch (BusinessException be) {
			logger.error("", be);			
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));			
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		mav.addObject(SEARCH_RESULTS, res);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchProjectBean, PAGES);
		
		// add the search bean on command
		mav.addObject(SEARCH_PROJECT_BEAN, searchProjectBean);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		logger.debug("handleSearch - END ");
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 * 
	 * @author Adelina
	 *
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @return
	 */
	private ModelAndView handlePagination(HttpServletRequest request, Object command, ArrayList<String> errorMessages) {
		logger.debug("handlePagination - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchProjectBean searchProjectBean = (SearchProjectBean) command;
		
		try {
			if (request.getParameter(PAGE) != null){
				if (NEXT.equals(request.getParameter(PAGE))){
					searchProjectBean.setCurrentPage(searchProjectBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))){
					searchProjectBean.setCurrentPage(searchProjectBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))){
					searchProjectBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))){
					searchProjectBean.setCurrentPage(searchProjectBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))){
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))){
						searchProjectBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchProjectBean.setCurrentPage(-1);
					}
				}
			}
		} catch(Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR,e);
			searchProjectBean.setCurrentPage(-1);		
		}
	
		List<Project> res = null;
		
		try{
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Integer userId = userAuth.getPersonId();
			Integer organizationId = userAuth.getOrganisationId();
			res  = BLProject.getInstance().listProjectsForPagination(searchProjectBean, userId, organizationId, messageSource);	
			
			for(Project project : res) {
				String panelHeaderName = ControllerUtils.getInstance().truncateName(project.getName(), NR_CHARS_PANEL_HEADER);
				project.setPanelHeaderName(panelHeaderName);
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));		
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
			
			
		mav.addObject(SEARCH_RESULTS, res);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchProjectBean, PAGES);
		
		// add the command object on mav
		mav.addObject(SEARCH_PROJECT_BEAN, searchProjectBean);
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		logger.debug("handlePagination - END");
		return mav;
	}
	
	
	
	/**
	 * @author Adelina
	 */
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) {
		logger.debug("referenceData - START");
		//used as a container for error messages
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		Map map = new HashMap();		
		
		//load the list of possible results per page
		map.put(IConstant.NOM_RESULTS_PER_PAGE, TSContext.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");	
				
		Integer organizationId = (Integer) request.getSession().getAttribute(IConstant.SESS_ORGANISATION_ID);
		logger.debug("organizationId = " + organizationId);		
		
		// list the persons for the organization that is project manager
		List<UserSimple> users = new ArrayList<UserSimple>();
		try{
			users = BLUser.getInstance().getUsersSimpleByOrganizationId(organizationId, true);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PERSONS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
			
		map.put(JSON_PERSONS, ControllerUtils.getInstance().
				getPersonsFirstNameLastNameFromOrgAsJSON(users, RequestContextUtils.getLocale(request), 
						errorMessages, messageSource));					
		
		// Adding to model the issue's status
		map.put(PROJECT_STATUS, TSContext.getFromContext(IConstant.NOM_PROJECT_STATUS));
		
		// get the clients for the certain organization
		
		List<Client> clients = new ArrayList<Client>();
		try{
			clients = BLProject.getInstance().getClientsForProject(organizationId);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_CLIENTS_FROM_ORG_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		logger.debug("reference data clients = " + clients.size() + ", " + clients);
		
		map.put(JSON_CLIENTS, ControllerUtils.getInstance().
		getClientsFromOrgAsJSON(clients, RequestContextUtils.getLocale(request), 
				errorMessages, messageSource));	
		
		
		setErrors(request, errorMessages);
		
		logger.debug("referenceData - END");
		return map;
	}

}
