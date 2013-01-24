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
package ro.cs.om.web.controller.form;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.Scheduler;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLOutOfOffice;
import ro.cs.om.business.BLPerson;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.OutOfOffice;
import ro.cs.om.entity.Person;
import ro.cs.om.entity.SearchOOOBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 * 
 */
public class OOOSearchController extends RootSimpleFormController {

	private static final String FORM_VIEW = "OOO_Search";
	private static final String SUCCESS_VIEW = "OOO_Listing";
	private static final String JSON_PERSONS = "JSON_PERSONS";
	private static final String SEARCH_ERROR = "ooo.search.error";
	private static final String DELETE_JOB_ERROR = "ooo.delete.job.error";
	private static final String DELETE_JOB_MESSAGE = "ooo.delete.message";
	private static final String PAGINATION = "pagination";
	private static final String ACTION = "action";
	private static final String OOO_PROFILES = "OOO_PROFILES";
	private static final String PAGES = "ooo.pagination.pages";
	private static final String SEARCH_OOO_BEAN = "searchOOOBean";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";
	private static final String COMMAND = "command";
	private static final String DELETE = "delete";
	private static final String OOO_ID = "oooId";
	private static final String PERSONID = "personId";
	private static final String PAGINATION_ERROR = "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String DELETE_OOO_PROFILE_NOT_ALLOWED = "ooo.modify.profile.not.allowed";
	private static final String USERNAME = "USERNAME";

	public OOOSearchController() {
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
		setCommandName("searchOOOBean");
		setCommandClass(SearchOOOBean.class);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		logger.debug("formBackingObject - START");
		SearchOOOBean searchOOOBean = new SearchOOOBean();

		// set the initial search parameters
		searchOOOBean.setSortDirection(IConstant.ASCENDING);
		searchOOOBean.setSortParam("person.firstName");	

		logger.debug("formBackingObject - END");
		return searchOOOBean;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(
				sdf, true));

		logger.debug("initBinder - END");
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		logger.debug("onSubmit - START");
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);

		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		int userId;
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (userAuth.isAdminIT()) {
			userId = -1;
		} else {
			userId = userAuth.getPersonId();
		}
		// check if I have delete action
		boolean isDeleteAction = false;
		if ((request.getParameter(ACTION) != null)
				&& (DELETE.equals(request.getParameter(ACTION)))) {
			mav = handleDelete(Integer.parseInt(request.getParameter(OOO_ID)),
					Integer.parseInt(request.getParameter(PERSONID)),
					errorMessages, infoMessages, RequestContextUtils
							.getLocale(request), userId, userAuth
							.isAdminIT(), request);
			isDeleteAction = true;
		}

		if (request.getParameter(ACTION) != null
				&& PAGINATION.equals(request.getParameter(ACTION))) {
			mav = handlePagination(request, errorMessages, command);
		} else {
			mav = handleSearch(request, command, errorMessages, isDeleteAction);
		}
		mav.addObject(USERNAME, userAuth.getUsername());
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		logger.debug("onSubmit - END");
		return mav;

	}

	/**
	 * 
	 * @param request
	 * @param command
	 * @param errorMessages
	 * @param isDeleteAction
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request,
			Object command,ArrayList<String> errorMessages, boolean isDeleteAction) throws BusinessException {
		logger.debug("handleSearch - START");

		ModelAndView mav = new ModelAndView(getSuccessView());

		List<OutOfOffice> res = null;

		SearchOOOBean searchOOOBean = (SearchOOOBean) command;

		searchOOOBean.setOrganisationId((ControllerUtils.getInstance()
				.getOrganisationIdFromSession(request)));
		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance()
					.getOM_OutOfOfficeSearch())) {
				res = BLOutOfOffice.getInstance().getResultsForSearch(
						searchOOOBean, isDeleteAction);
				mav.addObject(OOO_PROFILES, res);
			} else {
				errorMessages.add(messageSource.getMessage(
						IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils
								.getLocale(request)));
			}

		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchOOOBean, PAGES);

		mav.addObject(SEARCH_OOO_BEAN, searchOOOBean);
		mav.addObject(COMMAND, command);

		logger.debug("handleSearch - END ");
		return mav;
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {

		logger.debug("referenceData - START");

		Map map = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();			

		map.put(IConstant.NOM_RESULTS_PER_PAGE, OMContext
				.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");

		// add all the persons from user's organization for autoComplete
		map.put(JSON_PERSONS, ControllerUtils.getInstance()
				.getPersonsFirstNameLastNameFromOrgAsJSON(
						ControllerUtils.getInstance()
								.getOrganisationIdFromSession(request),
						RequestContextUtils.getLocale(request), errorMessages,
						messageSource));

		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}

	/**
	 * Handles the results pagination
	 * 
	 * @author alu
	 * 
	 * @param request
	 * @param command
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handlePagination(HttpServletRequest request, ArrayList<String> errorMessages,
			Object command) throws BusinessException {
		logger.debug("handlePagination - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchOOOBean searchOOOBean = (SearchOOOBean) command;


		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchOOOBean
							.setCurrentPage(searchOOOBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchOOOBean
							.setCurrentPage(searchOOOBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchOOOBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchOOOBean.setCurrentPage(searchOOOBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null
							&& !"".equals(request.getParameter(PAGE_NBR))) {
						searchOOOBean.setCurrentPage(Integer.parseInt(request
								.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchOOOBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchOOOBean.setCurrentPage(-1);
		}
		// isDeleteAction - false

		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		List<OutOfOffice> res = null; 
		try {
			

			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance()
					.getOM_OutOfOfficeSearch())) {
				res = BLOutOfOffice.getInstance().getResultsForSearch(
						searchOOOBean, false);
				mav.addObject(OOO_PROFILES, res);
			} else {
				errorMessages.add(messageSource.getMessage(
						IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils
								.getLocale(request)));
			}

		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);
			errorMessages.add(messageSource.getMessage(SEARCH_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		
		
		mav.addObject(OOO_PROFILES, res);

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchOOOBean, PAGES);

		mav.addObject(SEARCH_OOO_BEAN, searchOOOBean);
		mav.addObject(COMMAND, command);

		logger.debug("handlePagination - END");
		return mav;
	}

	private ModelAndView handleDelete(int oooId, int personId,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages,
			Locale locale, int userId, boolean isAdmin, HttpServletRequest request) {
		logger.debug("handleDelete OOOSearchController - START");
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		try {
			// if the logged on user is not adminIT and is not the owner of the
			// ooo profile it tries to delete, an error
			// message should be displayed
			if (!isAdmin && userId != personId) {
				errorMessages.add(messageSource.getMessage(
						DELETE_OOO_PROFILE_NOT_ALLOWED, new Object[] {
								null,
								ControllerUtils.getInstance()
										.getFormattedCurrentTime() }, locale));
			} else {				
				// get person's username
				String username = BLPerson.getInstance().get(personId)
						.getUsername();
				// get the scheduler from context
				Scheduler sched = (Scheduler) OMContext
						.getFromContext(IConstant.SCHEDULER);
				// delete the out of office profile
				BLOutOfOffice.getInstance().delete(oooId);
				// delete the job
				String jobName = String.valueOf(username).concat("_")
						.concat(String.valueOf(oooId));
				sched.deleteJob(jobName, String.valueOf(username).concat(
						"_Group"));
				infoMessages.add(messageSource.getMessage(
						DELETE_JOB_MESSAGE, null, locale));
				logger.debug("JOB DELETED - job name: ".concat(jobName));	
				
				//add the new audit event
				try {
					UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Person person = BLPerson.getInstance().get(personId) ;
					if (!userAuth.isAdminIT()){
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_OOO_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_DELETE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_DELETE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}

			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(DELETE_JOB_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, locale));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(DELETE_JOB_ERROR,
					new Object[] {
							null,
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, locale));
		}
		logger.debug("handleDelete - END");
		return mav;
	}

}
