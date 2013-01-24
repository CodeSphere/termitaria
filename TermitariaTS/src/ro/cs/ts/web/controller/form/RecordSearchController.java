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

import ro.cs.ts.business.BLActivity;
import ro.cs.ts.business.BLAudit;
import ro.cs.ts.business.BLProject;
import ro.cs.ts.business.BLRecord;
import ro.cs.ts.business.BLTeamMember;
import ro.cs.ts.business.BLUser;
import ro.cs.ts.cm.Project;
import ro.cs.ts.cm.TeamMember;
import ro.cs.ts.common.IConstant;
import ro.cs.ts.common.PermissionConstant;
import ro.cs.ts.context.TSContext;
import ro.cs.ts.entity.Record;
import ro.cs.ts.entity.SearchRecordBean;
import ro.cs.ts.exception.BusinessException;
import ro.cs.ts.thread.NotificationThread;
import ro.cs.ts.utils.DateInterval;
import ro.cs.ts.utils.DateUtils;
import ro.cs.ts.utils.ProjectComparator;
import ro.cs.ts.web.controller.root.ControllerUtils;
import ro.cs.ts.web.controller.root.RootSimpleFormController;
import ro.cs.ts.web.security.UserAuth;
import ro.cs.ts.ws.client.om.OMWebServiceClient;
import ro.cs.ts.ws.client.om.entity.UserSimple;

public class RecordSearchController extends RootSimpleFormController{

	private static final String FORM_VIEW 					= "Record_Search";
	private static final String SUCCESS_VIEW				= "Record_Listing";
	
	//------------------------ATTRIBUTES---------------------------------------------------------------
	private static final String PAGE 						= "page";
	private static final String NEXT 						= "next";
	private static final String PREV 						= "prev";
	private static final String FIRST 						= "first";
	private static final String LAST 						= "last";
	private static final String PAGE_NBR 					= "pagenbr";
	private static final String NUMBER 						= "nbr";
	private static final String RECORD_ID					= "recordId";
	private static final String DELETE_FROM_RECORD_FORM 	= "DELETE_FROM_RECORD_FORM";
	private static final String ACTION 						= "action";
	private static final String DELETEALL 					= "DELETE_ALL";
	private static final String PAGINATION					= "pagination";
	
	//------------------------MESSAGES-----------------------------------------------------------------
	private static final String SEARCH_ERROR 				= "record.search.error";
	private static final String SEARCH_EXCEPTION_ERROR 		= "record.search.exception.error";
	private static final String PAGINATION_ERROR 			= "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String DELETE_ERROR 				= "record.delete.error";
	private static final String DELETE_SUCCESS_MULTIPLE		= "record.delete.success.multiple";
	private static final String DELETE_SUCCESS_SINGLE		= "record.delete.success.single";
	private static final String GENERAL_ERROR				= "record.general.search.error";
	private static final String GET_USER_PROJECTS_ERROR 	= "record.get.user.projects.error";
	private final String GET_PROJECT_ERROR					= "project.get.error";
	
	//------------------------MODEL--------------------------------------------------------------------
	private static final String SEARCH_RESULTS 			= "SEARCH_RESULTS";
	private static final String SEARCH_RECORD_BEAN		= "searchRecordBean";
	private static final String COMMAND					= "command";
	private static final String USER_PROJECTS			= "USER_PROJECTS";
	private static final String DISPLAY_NAME_INPUT		= "DISPLAY_NAME_INPUT";
	private static final String TODAY_DATE				= "TODAY_DATE";
	private static final String USER_ID					= "USER_ID";	
	private static final String IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT = "IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT";
	private static final String LAST_PROJECT			= "lastProject";
	
	// Number of characters that fit in the panel
	// display header if there are big words
    public static final Integer NR_CHARS_PANEL_HEADER	= 50;
    private static final String PAGES 					= "pagination.pages";
    
    
	public RecordSearchController() {
		setCommandClass(SearchRecordBean.class);
		setCommandName("searchRecordBean");
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));

		logger.debug("initBinder - END");
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("formBackingObject - START");
		
		// used as a container for info/error messages
		ArrayList<String> infoMessages = new ArrayList<String>();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		SearchRecordBean searchRecordBean = new SearchRecordBean();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//we set the initial search parameters
		searchRecordBean.setSortDirection(IConstant.DESCENDING);
		searchRecordBean.setOrganizationId(userAuth.getOrganisationId());
		searchRecordBean.setTeamMemberId(-1);
		searchRecordBean.setSortParam("recordId");
		searchRecordBean.setWeek(-1);
		searchRecordBean.setMonth(-1);
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer recordId = ServletRequestUtils.getIntParameter(request, RECORD_ID);
		
		// deletes a record if the request comes from record form
		if(action != null && DELETE_FROM_RECORD_FORM.equals(action) && recordId != null) {		
			handleDeleteFromRecordForm(request, searchRecordBean, recordId, infoMessages, errorMessages);
		}
		
		if (request.getParameter("activityId") != null)
			searchRecordBean.setActivityId(Integer.parseInt(request.getParameter("activityId")));
			
		
		if (request.getParameter("projectId") != null)
			searchRecordBean.setProjectId(Integer.parseInt(request.getParameter("projectId")));
		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("formBackingObject - END");
		return searchRecordBean;
	}
	
	@Override
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
	 * Searches for records
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
		
		SearchRecordBean searchRecordBean =  (SearchRecordBean) command;
		List<Record> res = null;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		//added to set date interval for the week selected
		if (searchRecordBean.getWeek() != -1 && searchRecordBean.getWeek() != null) {
			DateInterval weekDateInterval = DateUtils.getInstance().getDateIntervalForWeek(searchRecordBean.getWeek());
			searchRecordBean.setStartDate(weekDateInterval.getStartDate());
			searchRecordBean.setEndDate(weekDateInterval.getEndDate());
		}
		
		//added to set date interval for the month selected
		if (searchRecordBean.getMonth() != -1 && searchRecordBean.getMonth() != null) {
			DateInterval monthDateInterval = DateUtils.getInstance().getDateIntervalForMonth(searchRecordBean.getMonth());
			searchRecordBean.setStartDate(monthDateInterval.getStartDate());
			searchRecordBean.setEndDate(monthDateInterval.getEndDate());
		
		}
		
		try {
			res = BLRecord.getInstance().getResultsForSearch(searchRecordBean, isDeleteAction);

			//set the record info panel header name
			for (Record record : res) {
				String headerName = BLActivity.getInstance().get(record.getActivity().getActivityId()).getName();
				String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
				record.setPanelHeaderName(panelHeaderName);
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
		ControllerUtils.getInstance().findPagesLimit(searchRecordBean, PAGES);		
		
		mav.addObject(SEARCH_RECORD_BEAN, searchRecordBean);
		mav.addObject(COMMAND, command);
				
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		// adding to mav
		mav.addObject(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
		
		if (res != null) {
			logger.debug("handleSearch - END - res.size=".concat(String.valueOf(res.size())));
		}
		
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
		SearchRecordBean searchRecordBean = (SearchRecordBean) command;
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchRecordBean.setCurrentPage(searchRecordBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchRecordBean.setCurrentPage(searchRecordBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchRecordBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchRecordBean.setCurrentPage(searchRecordBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null && !"".equals(request.getParameter(PAGE_NBR))) {
						searchRecordBean.setCurrentPage(Integer.parseInt(request.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchRecordBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchRecordBean.setCurrentPage(-1);
		}	

		List<Record> res = null; 
		try {
									
			res = BLRecord.getInstance().getResultsForSearch(searchRecordBean, false);
			
			//set the record info panel header name
			for (Record record : res) {
				if (record.getActivityId() != null){
					String headerName = BLActivity.getInstance().get(record.getActivityId()).getName();
					String panelHeaderName = ControllerUtils.getInstance().truncateName(headerName, NR_CHARS_PANEL_HEADER);
					record.setPanelHeaderName(panelHeaderName);
				}
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
		ControllerUtils.getInstance().findPagesLimit(searchRecordBean, PAGES);

		mav.addObject(SEARCH_RECORD_BEAN, searchRecordBean);
		mav.addObject(COMMAND, command);
				
		mav.addObject(USER_ID, userAuth.getPersonId());	
		
		// checks if the user that logs in is the manager for at least one project
		boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
		
		// adding to mav
		mav.addObject(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);

		logger.debug("handlePagination - END");
		return mav;
	}
	
	/**
	 * Deletes all the records or a specific one
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
		
		SearchRecordBean searchRecordBean = (SearchRecordBean) command;

		logger.debug(searchRecordBean);
		logger.debug("start deleting " + searchRecordBean.getRecordId().length + " record(s).");
		
		handleDeleteAllSimple(request, searchRecordBean, infoMessages, errorMessages);
		
		logger.debug("Results per page " + searchRecordBean.getResultsPerPage());
		logger.debug("handleDeleteAll - END");
	}
	
	/**
	 * Deletes records
	 * 
	 * @author coni
	 * 
	 * @param request
	 * @param searchRecordBean
	 * @throws BusinessException
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchRecordBean searchRecordBean, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		logger.debug("handleDeleteAllSimple - START ");
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// the organization id from the session
		Integer organizationId = (Integer) ControllerUtils.getInstance().getOrganisationIdFromSession(request);
				
		int deletedRecords = 0;

		for (int i = 0; i < searchRecordBean.getRecordId().length; i++) {
			logger.debug("Delete record : " + searchRecordBean.getRecordId()[i]);	
			Record record = BLRecord.getInstance().getAll(searchRecordBean.getRecordId()[i]);				
			logger.debug("record = " + record);
			try {
				BLRecord.getInstance().delete(searchRecordBean.getRecordId()[i]);
				
				String projectName = null;
				String message = null;
				Integer projectId = -1;
				
				if(record != null) {
					if(record.getProjectDetails() != null && record.getProjectDetails().getProjectId() != -1) {
						Project project = BLProject.getInstance().get(record.getProjectDetails().getProjectId(), true);
						if(project != null) {
							projectName = project.getName();
						}
						projectId = project.getProjectId();
						message = IConstant.NOTIFICATION_MESSAGE_RECORD_PROJECT_DELETE;
					} else {
						projectName = IConstant.KEY.concat(IConstant.FROM_ORGANIZATION);
						message = IConstant.NOTIFICATION_MESSAGE_RECORD_DELETE;
					}
				}
				
				if (record.getProjectDetails() == null) {
					String[] personIds = new String[1];
					personIds[0] = String.valueOf(record.getPersonDetail().getPersonId());
					UserSimple recordOwner = BLUser.getInstance().getUsersByPersonId(personIds).get(0);
					// send notification regarding the deletion of a record		
					sendNotificationRecordDelete(projectId, null, organizationId, 
							message, new Object[]{record.getRecordId(), record.getActivity().getName(), projectName, recordOwner.getFirstName().concat(" ").concat(recordOwner.getLastName()), userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
							IConstant.NOTIFICATION_SUBJECT_RECORD_DELETE, new Object[]{null},IConstant.NOTIFICATION_SETTING_RECORD_DELETE);
					//sendNotificationRecordDelete(projectId, organizationId, messageSource.getMessage(message, new Object[]{record.getRecordId(), record.getActivity().getName(), projectName, recordOwner.getFirstName().concat(" ").concat(recordOwner.getLastName())}, new Locale("ro")), messageSource.getMessage(IConstant.NOTIFICATION_SUBJECT_RECORD_DELETE, new Object[]{null}, new Locale("ro")));
				} else {
					TeamMember recordOwner = BLTeamMember.getInstance().getTeamMember(record.getTeamMemberDetail().getTeamMemberId(), false);
					logger.debug("recordOwner = " + recordOwner);
					sendNotificationRecordDelete(projectId, record.getProjectDetails().getProjectDetailId(), organizationId, 
							message, new Object[]{record.getRecordId(), record.getActivity().getName(), projectName, recordOwner.getFirstName().concat(" ").concat(recordOwner.getLastName()), userAuth.getFirstName().concat(" ").concat(userAuth.getLastName())},
							IConstant.NOTIFICATION_SUBJECT_RECORD_DELETE, new Object[]{null},IConstant.NOTIFICATION_SETTING_RECORD_DELETE);
					//sendNotificationRecordDelete(projectId, organizationId, messageSource.getMessage(message, new Object[]{record.getRecordId(), record.getActivity().getName(), projectName, recordOwner.getFirstName().concat(" ").concat(recordOwner.getLastName())}, new Locale("ro")), messageSource.getMessage(IConstant.NOTIFICATION_SUBJECT_RECORD_DELETE, new Object[]{null}, new Locale("ro")));
				}										
												
				deletedRecords++;
				
				//add the new audit event only if the user is not AdminIT
				try {
					if (!userAuth.isAdminIT()){
						if (record.getProjectDetails() == null) {
							String[] personIds = new String[1];
							personIds[0] = record.getPersonDetail().getPersonId().toString();
							UserSimple recordOwner = BLUser.getInstance().getUsersByPersonId(personIds).get(0);
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_RECORD_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_ORG_DELETE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), recordOwner.getFirstName().concat(" ").concat(recordOwner.getLastName()), record.getActivity().getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_ORG_DELETE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), recordOwner.getFirstName().concat(" ").concat(recordOwner.getLastName()), record.getActivity().getName()}, new Locale("ro")),  
									ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
						} else {
							TeamMember recordOnwer = BLTeamMember.getInstance().getTeamMember(record.getTeamMemberDetail().getTeamMemberId(), true);
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_RECORD_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName(), 
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_PROJECT_DELETE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), recordOnwer.getFirstName().concat(" ").concat(recordOnwer.getLastName()), projectName, record.getActivity().getName()}, new Locale("en")),
									messageSource.getMessage(IConstant.AUDIT_EVENT_RECORD_FOR_PROJECT_DELETE_MESSAGE, new Object[] {String.valueOf(record.getRecordId()), recordOnwer.getFirstName().concat(" ").concat(recordOnwer.getLastName()), projectName, record.getActivity().getName()}, new Locale("ro")),  
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
		
		if (deletedRecords == searchRecordBean.getRecordId().length && searchRecordBean.getRecordId().length > 1) {
			infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_MULTIPLE, new Object[] { null }, RequestContextUtils.getLocale(request)));
		} else if (deletedRecords == searchRecordBean.getRecordId().length && searchRecordBean.getRecordId().length == 1) {
			infoMessages.add(messageSource.getMessage(DELETE_SUCCESS_SINGLE, new Object[] { null }, RequestContextUtils.getLocale(request)));
		}
		
		logger.debug("handleDeleteAllSimple - END ");
	}
	
	/**
	 * Deletes a record that comes from a record form
	 * 
	 * @author Coni
	 * @param request
	 * @param searchRecordBean
	 * @param recordId
	 * @throws BusinessException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	private void handleDeleteFromRecordForm(HttpServletRequest request, SearchRecordBean searchRecordBean, Integer recordId, ArrayList<String> infoMessages, ArrayList<String> errorMessages) throws BusinessException, SecurityException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException{						
		logger.debug("handleDeleteFromRecordForm - START - ");
		
		Integer[] recordIds = new Integer[1];
		recordIds[0] = recordId;			
		searchRecordBean.setRecordId(recordIds);
		handleDeleteAllSimple(request, searchRecordBean, infoMessages, errorMessages);
				
		logger.debug("handleDeleteFromRecordForm - END - ");
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) {
		logger.debug("referenceData - START");
		Map map = new HashMap();
		ArrayList<String> errorMessages  	= new ArrayList<String>();
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try {
			// adding to model the results per page for search results
			map.put(IConstant.NOM_RESULTS_PER_PAGE, TSContext
					.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
			
			//adding to model the billable nomenclator
			map.put(IConstant.NOM_BILLABLE, TSContext.getFromContext(IConstant.NOM_BILLABLE));
			
			//add the current date
			map.put(TODAY_DATE, ControllerUtils.getInstance().getLocaleDate(new GregorianCalendar()));
			
			// checks if the user that logs in is the manager for at least one project
			boolean isManagerForAtLeasOneProject = isManagerForAtLeastOneProject(userAuth.getPersonId(), request, errorMessages);	
			
			// adding to mav
			map.put(IS_MANAGER_FOR_AT_LEAST_ONE_PROJECT, isManagerForAtLeasOneProject);
			
		} catch (Exception e){
			logger.error("referenceData", e);
			errorMessages.add(messageSource.getMessage(GENERAL_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		try {
			//adding the user's available project for search
			//if the user has the USER_ALL role, all its organization's projects will be available
			List<Project> projects = null;
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getTS_RecordSearchAll())) {
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
			map.put(USER_PROJECTS, projects);
			
			//set the last created available project
			if (projects != null && !projects.isEmpty()) {
				List<Project> projectsOrderedById = new ArrayList<Project>(projects);
				Collections.sort(projectsOrderedById, ProjectComparator.getInstance().projectIdComparator());
				map.put(LAST_PROJECT, projectsOrderedById.get(projectsOrderedById.size() - 1));
			}
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);			
			errorMessages.add(messageSource.getMessage(GET_USER_PROJECTS_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
	
	/**
	 * Send the notification when the record id deleted
	 * 
	 * @author Adelina
	 * 
	 * @param projectId
	 * @param organizationId
	 */
	public void sendNotificationRecordDelete(Integer projectId,Integer projectDetailId, Integer organizationId, String messageRecordKey,Object[] messageRecordObjects , String subjectRecordKey, Object[] subjectRecordObjects, Byte setting ) {
		logger.debug("sendNotificationRecordDelete - START, projectId = ".concat(String.valueOf(projectId)));
				
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
			
			
			List<String> list = new ArrayList<String>(userIds);					
			String[] notificationIds = list.toArray(new String[0]);
			logger.debug("notificationIds = " + notificationIds.length);
			
			for(int i = 0; i < notificationIds.length; i++) {
				logger.debug("notificationIds[" + i + "] = " + notificationIds[i]);
			}
			
			
										
			// send the notification										
			Thread thread = new Thread(new NotificationThread(projectDetailId,userIdsMap, organizationId , messageRecordKey, messageRecordObjects, subjectRecordKey, subjectRecordObjects,setting,messageSource));
			thread.start();				
			
		} catch (Exception e) {
			logger.error(e);
		}
		logger.debug("sendNotificationRecordDelete - END");
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

}
