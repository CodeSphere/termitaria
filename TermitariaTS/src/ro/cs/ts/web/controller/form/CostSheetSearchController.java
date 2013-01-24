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

import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLCostSheet;
import ro.cs.ts.business.BLCurrency;
import ro.cs.ts.business.BLPersonDetail;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLTeamMemberDetail;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.CostSheet;
import ro.cs.ts.entity.Currency;
import ro.cs.ts.entity.SearchCostSheetBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.nom.IntString;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.utils.ProjectComparator;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;


public class CostSheetSearchController extends RootSimpleFormController {

	private static final String FORM_VIEW 					= "CostSheet_Search";
	private static final String SUCCESS_VIEW				= "CostSheet_Listing";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";
	private static final String COST_SHEET_ID				= "costSheetId";
	private static final String DELETE_FROM_COST_SHEET_FORM = "DELETE_FROM_COST_SHEET_FORM";
	private static final String ACTION 						= "action";
	private static final String DELETEALL 					= "DELETE_ALL";
	private static final String PAGINATION					= "pagination";	
	
	//--------------------BACK PARAMETERS-------------------------------------------------------------
	private static final String BACK_URL 					= "BACK_URL";
	private static final String ENCODE_BACK_URL	 			= "ENCODE_BACK_URL";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String SEARCH_ERROR 				= "costsheet.search.error";
	private static final String SEARCH_EXCEPTION_ERROR 		= "costsheet.search.exception.error";
	private static final String PAGINATION_ERROR 			= "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String DELETE_ERROR 				= "costsheet.delete.error";
	private static final String DELETE_SUCCESS_MULTIPLE		= "costsheet.delete.success.multiple";
	private static final String DELETE_SUCCESS_SINGLE		= "costsheet.delete.success.single";
	private static final String GENERAL_ERROR				= "costsheet.general.search.error";
	private static final String GET_USER_PROJECTS_ERROR 	= "costsheet.get.user.projects.error";
	private static final String GET_ORG_CURRENCIES_ERROR 	= "costsheet.get.org.currencies.error";
	private static final String GET_PROJECT_ERROR			= "project.get.error";
	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String SEARCH_RESULTS 			= "SEARCH_RESULTS";
	private static final String SEARCH_COST_SHEET_BEAN	= "searchCostSheetBean";
	private static final String COMMAND					= "command";
	private static final String USER_PROJECTS			= "USER_PROJECTS";
	private static final String PROJECT_ID				= "PROJECT_ID";
	private static final String DISPLAY_NAME_INPUT		= "DISPLAY_NAME_INPUT";
	private static final String ORG_CURRENCIES 			= "ORG_CURRENCIES";
	private static final String TODAY_DATE				= "TODAY_DATE";
	private static final String IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT = "IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT";
	private static final String USER_ID					= "USER_ID";
	private static final String LAST_PROJECT			= "lastProject";
	
	// Number of characters that fit in the panel
	// display header if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 50;
    private static final String PAGES 					= "pagination.pages";
    
	public CostSheetSearchController() {
		setCommandClass(SearchCostSheetBean.class);
		setCommandName("searchCostSheetBean");
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	/**
	 * @author Coni
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));

		logger.debug("initBinder - END");
	}
	
	/**
	 * @author Coni
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		SearchCostSheetBean searchCostSheetBean = new SearchCostSheetBean();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//we set the initial search parameters
		searchCostSheetBean.setSortDirection(IConstant.DESCENDING);
		searchCostSheetBean.setOrganizationId(userAuth.getOrganisationId());
		searchCostSheetBean.setTeamMemberId(-1);
		searchCostSheetBean.setSortParam("date");
		searchCostSheetBean.setResultsPerPage((byte)5);
		searchCostSheetBean.setCurrentPage(1);
		searchCostSheetBean.setSortDirection(1);

		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer costSheetId = ServletRequestUtils.getIntParameter(request, COST_SHEET_ID);
		
		// deletes a record if the request comes from cost sheet form
		if(action != null && DELETE_FROM_COST_SHEET_FORM.equals(action) && costSheetId != null) {		
			handleDeleteFromCostSheetForm(request, searchCostSheetBean, costSheetId, infoMessages, errorMessages);
		}	
		
		if (request.getParameter("projectId") != null) 
			searchCostSheetBean.setProjectId(Integer.parseInt(request.getParameter("projectId")));


		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject - END");
		return searchCostSheetBean;
	}
	
	/**
	 * @author Coni
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		logger.debug("onSubmit - START");
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();

		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
		
		try {
			boolean isDeleteAction = false;
			
			//check if i have deleteAll action
			if (request.getParameter(ACTION) != null && DELETEALL.equals(request.getParameter(ACTION))) {
				handleDeleteAll(request, command, infoMessages, errorMessages);
				isDeleteAction = true;
			}
			if (request.getParameter(ACTION) != null && PAGINATION.equals(request.getParameter(ACTION))) {
				mav = handlePagination(request, errorMessages, command);
			} else {
				mav = handleSearch(request, command, errorMessages, isDeleteAction);
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR, new Object[] {ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		//setting all messages on response
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	/**
	 * @author Coni
	 * Searches for cost sheets
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request, Object command, ArrayList<String> errorMessages, boolean isDeleteAction) throws BusinessException {
		logger.debug("handeSearch - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		SearchCostSheetBean searchCostSheetBean =  (SearchCostSheetBean) command;
		
		List<CostSheet> res = null;		

		try {
			res = BLCostSheet.getInstance().getResultsForSearch(searchCostSheetBean, isDeleteAction);
			
			//set the costSheet info panel header name
			for (CostSheet costSheet : res) {
				String headerName = costSheet.getActivityName();
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				costSheet.setPanelHeaderName(panelHeaderName);
			}
			mav.addObject(SEARCH_RESULTS, res);
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] { be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_EXCEPTION_ERROR,
					new Object[] { ControllerUtils.getInstance().getFormattedCurrentTime() }, RequestContextUtils.getLocale(request)));
		}
		
		// find the number of pages shown in pagination area		
		ControllerUtils.getInstance().findPagesLimit(searchCostSheetBean, PAGES);		
		
		mav.addObject(SEARCH_COST_SHEET_BEAN, searchCostSheetBean);
		mav.addObject(COMMAND, command);
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		// adding to mav
		mav.addObject(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
		
		logger.debug("handleSearch - END - res.size=".concat(String.valueOf(res.size())));
		
		return mav;
	}
	
	/**
	 * Handles the results pagination
	 * 
	 * @author Coni
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request, ArrayList<String> errorMessages, Object command) throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchCostSheetBean searchCostSheetBean = (SearchCostSheetBean) command;		
		
		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchCostSheetBean.setCurrentPage(searchCostSheetBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchCostSheetBean.setCurrentPage(searchCostSheetBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchCostSheetBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchCostSheetBean.setCurrentPage(searchCostSheetBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))) {
						searchCostSheetBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchCostSheetBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchCostSheetBean.setCurrentPage(-1);
		}	

		List<CostSheet> res = null; 
		try {
									
			res = BLCostSheet.getInstance().getResultsForSearch(searchCostSheetBean, false);
			
			//set the costSheet info panel header name
			for (CostSheet costSheet : res) {
				String headerName = costSheet.getActivityName();
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				costSheet.setPanelHeaderName(panelHeaderName);
			}
			mav.addObject(SEARCH_RESULTS, res);				
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, 
					new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR, 
					new Object[] { ControllerUtils.getInstance().getFormattedCurrentTime() }, 
					RequestContextUtils.getLocale(request)));
		}
		
		
		mav.addObject(SEARCH_RESULTS, res);

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchCostSheetBean, PAGES);

		mav.addObject(SEARCH_COST_SHEET_BEAN, searchCostSheetBean);
		mav.addObject(COMMAND, command);
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		// adding to mav
		mav.addObject(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);

		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Deletes all the cost sheets or a specific one
	 * 
	 * @author coni
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteAll(HttpServletRequest request, Object command, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		logger.debug("handleDeleteAll - START");
		
		SearchCostSheetBean searchCostSheetBean = (SearchCostSheetBean) command;

		logger.debug(searchCostSheetBean);
		logger.debug("start deleting " + searchCostSheetBean.getCostSheetId().length + " cost sheet(s).");
		
		handleDeleteAllSimple(request, searchCostSheetBean, infoMessages, errorMessages);
		
		logger.debug("Results per page " + searchCostSheetBean.getResultsPerPage());
		logger.debug("handleDeleteAll - END");
	}
	
	/**
	 * Deletes cost sheets
	 * 
	 * @author coni
	 * 
	 * @param request
	 * @param searchCostSheetBean
	 * @throws BusinessException
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchCostSheetBean searchCostSheetBean, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		// the organization id from the session
		Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
		int deletedCostSheets = 0;

		for (int i = 0; i < searchCostSheetBean.getCostSheetId().length; i++) {
			logger.debug("Delete cost sheet : " + searchCostSheetBean.getCostSheetId()[i]);	
			CostSheet cost = BLCostSheet.getInstance().getAll(searchCostSheetBean.getCostSheetId()[i]);			
			
			String projectName = null;
			String message = null;			
			
			if(cost != null) {
				if(cost.getProjectDetails() != null && cost.getProjectDetails().getProjectId() != -1) {
					Project project = BLProject.getInstance().get(cost.getProjectDetails().getProjectId(), true);
					if(project != null) {
						projectName = project.getName();
					}					
					message = IConstant.NOTIFICATION_MESSAGE_COST_PROJECT_DELETE;
				} else {
					projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
					message = IConstant.NOTIFICATION_MESSAGE_COST_DELETE;
				}
			}
			
			logger.debug("cost = " + cost);
			
			String costSheetOwnerName = null;
			try {
				CostSheet costSheet = BLCostSheet.getInstance().delete(searchCostSheetBean.getCostSheetId()[i]);
				logger.debug("costSheet = " + costSheet);
				// send notification regarding the deletion of a cost				
				if (costSheet.getProjectDetailId() == null) { 
					String[] personIds = new String[1];
					personIds[0] = BLPersonDetail.getInstance().getWithAll(costSheet.getPersonDetailId()).getPersonId().toString();
					UserSimple costSheetOwner = BLUser.getInstance().getUsersByPersonId(personIds).get(0);
					costSheetOwnerName = costSheetOwner.getFirstName().concat(" ").concat(costSheetOwner.getLastName());
					
					sendNotificationCostDelete(costSheet.getProjectId(),costSheet.getProjectDetailId(),organizationId,
							message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName, userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
							IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_COST_DELETE);
					
					//sendNotificationCostDelete(IConstant.NOM_COST_SHEET_FORM_PROJECT_SELECT_ORG_OPTION, organizationId, messageSource.getMessage(
					//		message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName}, 
					//		new Locale("ro")), messageSource.getMessage(
					//				IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null}, new Locale("ro")));
				} else {
					TeamMember costSheetOwner = BLTeamMember.getInstance().getTeamMember(BLTeamMemberDetail.getInstance().get(costSheet.getTeamMemberDetailId()).getTeamMemberId(), false);
					costSheetOwnerName = costSheetOwner.getFirstName().concat(" ").concat(costSheetOwner.getLastName());
					
					sendNotificationCostDelete(costSheet.getProjectId(),costSheet.getProjectDetailId(),organizationId,
							message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName,userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
							IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null}, IConstant.NOTIFICATION_SETTING_COST_DELETE);
					
					
					//sendNotificationCostDelete(cost.getProjectDetails().getProjectId(), organizationId, messageSource.getMessage(
					//		message, new Object[]{costSheet.getCostSheetId(), projectName, cost.getDate(), costSheetOwnerName},
					//		new Locale("ro")), messageSource.getMessage(
					//				IConstant.NOTIFICATION_SUBJECT_COST_DELETE, new Object[]{null},
					//				new Locale("ro")));
				}
													
				deletedCostSheets++;
												
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (costSheet.getProjectDetailId() == null) { 
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_ORG_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_COST_SHEET_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName, projectName}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_COST_SHEET_FOR_PROJECT_DELETE_MESSAGE, new Object[] {String.valueOf(costSheet.getCostSheetId()), costSheetOwnerName, projectName}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						}
					}
				} catch (Exception exc) {
					logger.error("", exc);
				}
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(DELETE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
			}
		}
		
		if (deletedCostSheets == searchCostSheetBean.getCostSheetId().length && searchCostSheetBean.getCostSheetId().length > 1) {
			infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_MULTIPLE, new Object[] { null }, RequestContextUtils.getLocale(request)));
		} else if (deletedCostSheets == searchCostSheetBean.getCostSheetId().length && searchCostSheetBean.getCostSheetId().length == 1) {
			infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_SINGLE, new Object[] { null }, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("handleDeleteAllSimple - END ");
	}
	
	/**
	 * Deletes a cost sheet for a request that comes from a cost sheet form
	 * 
	 * @author Coni
	 * @param request
	 * @param searchCostSheetBean
	 * @param costSheetId
	 * @throws BusinessException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteFromCostSheetForm(HttpServletRequest request, SearchCostSheetBean searchCostSheetBean, Integer costSheetId, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException{						
		logger.debug("handleDeleteFromRecordForm - START - ");
		
		Integer[] costSheetIds = new Integer[1];
		costSheetIds[0] = costSheetId;			
		searchCostSheetBean.setCostSheetId(costSheetIds);
		handleDeleteAllSimple(request, searchCostSheetBean, infoMessages, errorMessages);
				
		logger.debug("handleDeleteFromRecordForm - END - ");
	}
	
	/**
	 * @author Coni
	 */
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) {
		logger.debug("referenceData - START");
		Map map = new HashMap();
		ArrayList<String> errorMessages  	= new ArrayList<String>();		
		
		// the user that logs in
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		// adding to model
		map.put(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
		
		
		
		try {
			//put the back url
			String backUrl = ServletRequestUtils.getStringParameter(request, BACK_URL);

			logger.debug("BACK_URL = " + backUrl);		
			
			map.put(BACK_URL, backUrl);		
			if (backUrl != null) {
				map.put(ENCODE_BACK_URL, URLEncoder.encode(backUrl, "UTF-8"));	
			}
			
			//adding the projectId
			Integer projectId = ServletRequestUtils.getIntParameter(request, "projectId");
			map.put(PROJECT_ID, projectId);
			
			// adding to model the results per page for search results
			map.put(IConstant.NOM_RESULTS_PER_PAGE, TSContext
					.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
			
			//adding to model the billable nomenclator
			map.put(IConstant.NOM_BILLABLE, TSContext.getFromContext(IConstant.NOM_BILLABLE));
			
			//add the current date
			map.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
		} catch (Exception e){
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		try {
			//adding the user's available project for search
			//if the user has the USER_ALL role, all its organization's projects will be available
			List<Project> projects = null;
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_CostSheetSearchAll())) {
				projects = BLProject.getInstance().getAllProjects(userAuth.getOrganisationId(), true);
				//if the user has the USER_ALL role, i must set DISPLAY_NAME_INPUT to true, in order 
				//to display the first name and last name fields when one of the following two options is
				//chosen in the projects select 
				map.put(DISPLAY_NAME_INPUT, true);
			} else {				
				//if the user hasn't the USER_ALL role, i must set DISPLAY_NAME_INPUT depending on the 
				//user being PM in at least one project, in order to display the first name and last 
				//name fields when one of the following two options is chosen in the projects select 
				projects = BLProject.getInstance().getProjectsByPerson(userAuth.getPersonId());
				boolean isPm = false;
				for (Project pr : projects) {
					if (pr.getManagerId().equals(userAuth.getPersonId())) {
						isPm = true;
					}
				}
				map.put(DISPLAY_NAME_INPUT, isPm);
			}
			logger.debug("projects = " + projects);
			map.put(USER_PROJECTS, projects);
			
			if (projects != null && !projects.isEmpty()) {
				//set the last created available project
				List<Project> projectsOrderedById = new ArrayList<Project>(projects);
				Collections.sort(projectsOrderedById, ProjectComparator.getInstance().projectIdComparator());
				map.put(LAST_PROJECT, projectsOrderedById.get(projectsOrderedById.size() - 1));
			}
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(GET_USER_PROJECTS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		try {
			//add the organization available currencies
			List<Currency> currencies = BLCurrency.getInstance().getByOrganizationId(userAuth.getOrganisationId());
			if (currencies != null) {
				List<IntString> nomCurrencies = new ArrayList<IntString>();
				for (Currency currency : currencies) {
					IntString entry = new IntString();
					entry.setValue(currency.getCurrencyId());
					entry.setLabel(currency.getName());
					nomCurrencies.add(entry);
				}
				map.put(ORG_CURRENCIES, nomCurrencies);
			}
		} catch (BusinessException bexc) {
			logger.error(bexc.getMessage(), bexc);			
			errorMessages.add(messageSource.getMessage(GET_ORG_CURRENCIES_ERROR, new Object[] {bexc.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * Checks if a person is manager for at least one project
	 * 
	 * @author Adelina
	 *  
	 * @param personId
	 * @param request
	 * @param errorMessages
	 * @return
	 */
	private boolean isManagerForAtLeastOneProject(Integer personId, HttpServletRequest request, ArrayList<String> errorMessages) {
		logger.debug("isManagerForAtLeastOneProject - START");
		
		boolean isManagerForProject = false;
				
		List<Project> projects = null; 
		try{
			projects = BLProject.getInstance().getProjectsByManager(personId, true, true);
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_PROJECT_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils
					.getLocale(request)));
		}
		
		if(projects != null && projects.size() > 0) {
			isManagerForProject = true;
		}
		
		logger.debug("isManagerForAtLeastOneProject - END");
		
		return isManagerForProject;
	}
	
	/**
	 * Send the notification when the cost id deleted
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param organizationId
	 */
	public void sendNotificationCostDelete(Integer projectId,Integer projectDetailId, Integer organizationId, String messageCostKey,Object[] messageCostObjects , String subjectCostKey, Object[] subjectCostObjects, Byte setting ) {
		logger.debug("sendNotificationCostDelete - START, projectId = ".concat(String.valueOf(projectId)));
				
		Set<String> userIds = new HashSet<String>();
		Map<String,Boolean> userIdsMap= new HashMap <String,Boolean>();
		
		try{									
			if(projectId != null && projectId != -1) {
				
				// get the project identified by it's projectId
				Project project = BLProject.getInstance().getSimpleProject(projectId);				
				logger.debug("project = " + project);
				Integer managerId = project.getManagerId();
				logger.debug("managerId = " + managerId);
				
				//1. I have to send a notification to the manager of the project					
				userIds.add(String.valueOf(managerId));	
				userIdsMap.put(String.valueOf(managerId), true);
			} 
			
			// 2. I have to send a notification to the users, that have the permission TS_NotificationReceive
			Set<UserSimple> users = OMWebServiceClient.getInstance().getPersonsFromRole(PermissionConstant.getTheInstance().getTS_NotificationReceive(), organizationId);
			logger.debug("users = " + users);
			if(users != null && users.size() > 0) {							
				for(UserSimple user : users) {
					if (userIds.add(String.valueOf(user.getUserId()))){
						userIdsMap.put(String.valueOf(user.getUserId()), false);
					}
				}
			}			
											
			// send the notification										
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageCostKey, messageCostObjects, subjectCostKey, subjectCostObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationCostDelete - END");
	}
	
}
