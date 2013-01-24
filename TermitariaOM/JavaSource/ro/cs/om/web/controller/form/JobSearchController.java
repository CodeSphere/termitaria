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
/*
 author mitziuro
 */
package ro.cs.om.web.controller.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLJob;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.business.BLRole;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.context.OMContext;
import ro.cs.om.entity.Job;
import ro.cs.om.entity.Organisation;
import ro.cs.om.entity.SearchJobBean;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.security.UserAuth;

public class JobSearchController extends RootSimpleFormController {

	private static final String FORM_VIEW = "Job_Search";
	private static final String SUCCESS_VIEW = "Job_Listing";
	private static final String ORGANISATIONS = "ORGANISATIONS";
	private static final String ACTION = "action";
	private static final String DELETE_ALL = "DELETE_ALL";
	private static final String UPDATESTATUS = "updatestatus";
	private static final String ID = "id";
	private static final String PAGINATION = "pagination";
	private static final String PAGE = "page";
	private static final String NEXT = "next";
	private static final String PREV = "prev";
	private static final String FIRST = "first";
	private static final String LAST = "last";
	private static final String PAGE_NBR = "pagenbr";
	private static final String NUMBER = "nbr";
	private static final String PAGES = "job.pagination.pages";
	private static final String PAGINATION_ERROR = "PAGINATION ERROR!!!!!!!!!!!!!!";
	private static final String COMMAND = "command";
	private static final String SEARCH_RESULTS = "JOBS";
	private static final String SEARCH_JOB_BEAN = "searchJobBean";
	private static final String SEARCH_ERROR = "job.search.error";
	private static final String DELETE_ERROR = "job.delete.error";
	private static final String DELETE_SUCCESS = "job.delete.success";
	private static final String UPDATE_ERROR = "job.update.error";
	private static final String UPDATE_EXCEPTION_ERROR = "job.update.exception";	
	private static final String SEARCH_BRANCH_ERROR = "organisation.search.branch.error";
	private static final String SEARCH_BRANCH_EXCEPTION_ERROR = "organisation.search.branch.exception.error";
	private static final String DELETE_FROM_JOB_FORM = "DELETE_FROM_JOB_FORM";
	private static final String JOB_ID				 = "jobId";
	private static final String HAS_ASSOCIATED_PERSON 		= "job.has.associated.person";
	private static final String DELETE_NOT_JOB 				= "job.not.delete";
	private static final String CHANGE_STATUS_ENABLE_SUCCESS = "job.enable.success";
	private static final String CHANGE_STATUS_DISABLE_SUCCESS = "job.disable.success";

	// --------------------------OTHER
	// PARAMETERS-------------------------------------
	private static final String ALL_ACTIONS = "ALL_ACTIONS";
	private static final String BRANCH_DISPLAY = "BRANCH_DISPLAY";
	
	// Number of characters that fit in a line, for the display panel,
    // if there are big words
    public static final Integer NR_CHARS_PER_LINE				= 28;


	public JobSearchController() {
		setCommandName("searchJobBean");
		setCommandClass(SearchJobBean.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		logger.debug("formBackingObject - START");
		SearchJobBean sjb = new SearchJobBean();

		// we set the initial search parameters
		sjb.setSortDirection(IConstant.ASCENDING);
		sjb.setSortParam("name");

		// put the initial branch (the organisation that is on the session)
		sjb.setBranch(String.valueOf(request.getSession().getAttribute(
				IConstant.SESS_ORGANISATION_NAME)));

		try {

			// check if user is not AdminIT
			UserAuth userAuth = (UserAuth) (SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal());
			if (userAuth.isAdminIT()) {
				request.setAttribute(ORGANISATIONS, BLOrganisation
						.getInstance().getAllOrganisationsForNom());
				logger.debug("ORAGANISATIONS CLASSIFIED LIST LOADED");
			}
		} catch (BusinessException be) {
			logger
					.error(
							"Exception at getting all the organisations for nomenclator!!!",
							be);
		}

		request.setAttribute(IConstant.NOM_RESULTS_PER_PAGE, OMContext
				.getFromContext(IConstant.NOM_RESULTS_PER_PAGE));
		logger.debug("RESULTS PER PAGE CLASSIFIED LIST LOADED");

		Integer organisationId = ControllerUtils.getInstance()
				.getOrganisationIdFromSession(request);

		if (organisationId != null) {
			logger.debug("Setting Organisation Id " + organisationId);
			sjb.setOrganisationId(organisationId);
		}
		
		String action = ServletRequestUtils.getStringParameter(request, IConstant.REQ_ACTION);
		Integer jobId = ServletRequestUtils.getIntParameter(request, JOB_ID);
		
		// deletes a role if the request comes from role form
		if(action != null && DELETE_FROM_JOB_FORM.equals(action) && jobId != null) {		
			handleDeleteFromJobForm(request, sjb, jobId);
		}

		logger.debug("formBackingObject - END");
		return sjb;
	}

	/**
	 * @author mitziuro
	 */
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");

		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		ModelAndView mav = new ModelAndView(IConstant.FORM_VIEW_MESSAGES);

	
		// check if user is not AdminIT
		UserAuth userAuth = (UserAuth) (SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal());
		if (!userAuth.isAdminIT()) {
			// set user's organisationId
			((SearchJobBean) command).setOrganisationId(userAuth
					.getOrganisationId());
		}
		boolean isChangeAction = false;
		// check if i have deleteAll action
		if (request.getParameter(ACTION) != null
				&& DELETE_ALL.equals(request.getParameter(ACTION))) {
			handleDeleteAll(request, command, infoMessages, errorMessages);
			isChangeAction = true;
		}
		if (request.getParameter(ACTION) != null
				&& UPDATESTATUS.equals(request.getParameter(ACTION))) {
			if (request.getParameter(ID) != null) {
				handleUpdateStatus(request, errorMessages, infoMessages);
				isChangeAction = true;
			}
		}
		// if I have sort action or delete or updateStatus action or search
		// action, I have to make a search(handleSearch)
		if (request.getParameter(ACTION) != null
				&& PAGINATION.equals(request.getParameter(ACTION))) {
			mav = handlePagination(request, errorMessages, command);
		} else {
			mav = handleSearch(request, errorMessages, command, isChangeAction);
		}

		setErrors(request, errorMessages);
		setMessages(request, infoMessages);

		logger.debug("onSubmit - END");
		return mav;

	}

	/**
	 * Deletes all the jobs or a specific job
	 * 
	 * @author Adelina
	 */
	private void handleDeleteAll(HttpServletRequest request, Object command,
			ArrayList<String> infoMessages, ArrayList<String> errorMessages)
			throws BusinessException {
		logger.debug("handleDeleteAll - START");
		
		SearchJobBean searchJobBean = (SearchJobBean) command;

		logger.debug(searchJobBean);
		logger.debug("start deleting " + searchJobBean.getJobId().length
				+ " job.");
		
		handleDeleteAllSimple(request, searchJobBean);
		
		logger.debug("Results per page " + searchJobBean.getResultsPerPage());
		logger.debug("handleDeleteAll - END");
	}
	
	/**
	 * Deletes jobs
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchJobBean
	 * @throws BusinessException
	 */
	private void handleDeleteAllSimple(HttpServletRequest request, SearchJobBean searchJobBean) throws BusinessException {
		
		logger.debug("handleDeleteAllSimple - START -");
		
		ArrayList<String> infoMessages  = new ArrayList<String>();
		ArrayList<String> errorMessages  = new ArrayList<String>();
		
		UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
		.getAuthentication().getPrincipal();
		
		Job job = null;
		String hasAssociatedPerson = null;

		if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_JobDelete())) {
			for (int i = 0; i < searchJobBean.getJobId().length; i++) {
				logger.debug("Delete job : " + searchJobBean.getJobId()[i]);	
				
				try{
					hasAssociatedPerson = BLJob.getInstance().hasJobAssociatePerson(searchJobBean.getJobId()[i]);
					logger.debug("hasAssociatedPerson = " + hasAssociatedPerson);
				} catch(BusinessException be) {
					logger.error("", be);
					errorMessages.add(messageSource.getMessage(HAS_ASSOCIATED_PERSON, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
				}
				
				boolean isDeleted = true;
				
				if(hasAssociatedPerson == null) {
				
					try {
						job = BLJob.getInstance().deleteAll(
								searchJobBean.getJobId()[i]);
						
						//add the new audit event
						try {
							if (!userAuth.isAdminIT()){
								Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
								BLAudit.getInstance().add(IConstant.AUDIT_EVENT_JOB_DELETE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
									, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_DELETE_MESSAGE, new Object[] {job.getName(), org.getName()}, new Locale("en"))
									, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_DELETE_MESSAGE, new Object[] {job.getName(), org.getName()}, new Locale("ro"))
									, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
							}
						} catch (BusinessException exc){
							logger.error(exc);
						}
						
					} catch (BusinessException be) {
						logger.error("", be);
						errorMessages.add(messageSource.getMessage(DELETE_ERROR,
								new Object[] {
										be.getCode(),
										ControllerUtils.getInstance()
												.getFormattedCurrentTime() },
								RequestContextUtils.getLocale(request)));
						isDeleted = false;
					}
					if (isDeleted) {
						String jobNameMessage = ControllerUtils.getInstance().tokenizeField(job.getName(), IConstant.NR_CHARS_PER_LINE_MESSAGE);
						infoMessages.add(messageSource.getMessage(DELETE_SUCCESS,
								new Object[] { jobNameMessage }, RequestContextUtils
										.getLocale(request)));
					}
				} else {
					errorMessages.add(messageSource.getMessage(DELETE_NOT_JOB, new Object[] {BLJob.getInstance().get(searchJobBean.getJobId()[i]).getName() ,hasAssociatedPerson}, RequestContextUtils.getLocale(request)));
				}
			}
		} else {
			errorMessages.add(messageSource.getMessage(
					IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
		}

		
		//setting all messages on response
		setMessages(request, infoMessages);
		setErrors(request, errorMessages);
		
		logger.debug("handleDeleteAllSimple - END -");
	}

	/**
	 * 
	 * Change the status of a job
	 * 
	 * @author mitziuro
	 * 
	 * @param request
	 * @param errorMessages
	 * @param infoMessages
	 */
	private void handleUpdateStatus(HttpServletRequest request,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages) {
		logger.debug("handleUpdate - START");
		
		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance().getOM_JobChangeStatus())) {
				
				int jobId = ServletRequestUtils.getIntParameter(request, ID);				
				Job job = BLJob.getInstance().get(jobId);				
				BLJob.getInstance().updateStatus(jobId);
				
				String jobNameMessage = ControllerUtils.getInstance().tokenizeField(job.getName(),  IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				if(job.getStatus() == IConstant.NOM_JOB_INACTIVE) {
					infoMessages.add(messageSource.getMessage(CHANGE_STATUS_ENABLE_SUCCESS, new Object[] { jobNameMessage },
							RequestContextUtils.getLocale(request)));
				} else {
					infoMessages.add(messageSource.getMessage(CHANGE_STATUS_DISABLE_SUCCESS, new Object[] { jobNameMessage },
							RequestContextUtils.getLocale(request)));
				}
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
						if (job.getStatus() == IConstant.NOM_JOB_INACTIVE) {
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_JOB_ENABLE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_ENABLE_MESSAGE, new Object[] {job.getName(), org.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_ENABLE_MESSAGE, new Object[] {job.getName(), org.getName()}, new Locale("ro"))
								, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
						} else {
							BLAudit.getInstance().add(IConstant.AUDIT_EVENT_JOB_DISABLE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
								, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_DISABLE_MESSAGE, new Object[] {job.getName(), org.getName()}, new Locale("en"))
								, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_DISABLE_MESSAGE, new Object[] {job.getName(), org.getName()}, new Locale("ro"))
								, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
						}
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(
						IConstant.SECURITY_NO_RIGHTS, null, RequestContextUtils.getLocale(request)));
			}
			
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_EXCEPTION_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}
		logger.debug("handleUpdate - END");
	}

	/**
	 * 
	 * Search for the requested parameters
	 * 
	 * @author mitziuro
	 * 
	 * @param request
	 * @param command
	 * @param isChangeAction
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleSearch(HttpServletRequest request, ArrayList<String> errorMessages,
			Object command, boolean isChangeAction) throws BusinessException {
		logger.debug("handleSearch - START");

		ModelAndView mav = new ModelAndView(getSuccessView());
		SearchJobBean searchJobBean = (SearchJobBean) command;

		// if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchJobBean, request));

		List<Job> res = null;

		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance()
					.getOM_JobSearch())) {
				res = BLJob.getInstance().getResultsForSearch(searchJobBean,
						isChangeAction);
				for(Job job: res) {
					job.setPanelConfirmationName(ControllerUtils.getInstance().tokenizeField(job.getName(), NR_CHARS_PER_LINE));
				}
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
		mav.addObject(SEARCH_RESULTS, res);
		
		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchJobBean, PAGES);
		

		mav.addObject(SEARCH_JOB_BEAN, searchJobBean);
		mav.addObject(COMMAND, command);
		
		return mav;
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
		SearchJobBean searchJobBean = (SearchJobBean) command;

		try {
			if (request.getParameter(PAGE) != null) {
				if (NEXT.equals(request.getParameter(PAGE))) {
					searchJobBean
							.setCurrentPage(searchJobBean.getCurrentPage() + 1);
				}
				if (PREV.equals(request.getParameter(PAGE))) {
					searchJobBean
							.setCurrentPage(searchJobBean.getCurrentPage() - 1);
				}
				if (FIRST.equals(request.getParameter(PAGE))) {
					searchJobBean.setCurrentPage(1);
				}
				if (LAST.equals(request.getParameter(PAGE))) {
					searchJobBean.setCurrentPage(searchJobBean.getNbrOfPages());
				}
				if (NUMBER.equals(request.getParameter(PAGE))) {
					if (request.getParameter(PAGE_NBR) != null
							&& !"".equals(request.getParameter(PAGE_NBR))) {
						searchJobBean.setCurrentPage(Integer.parseInt(request
								.getParameter(PAGE_NBR)));
					} else {
						// something is wrong
						// I will show the first page
						searchJobBean.setCurrentPage(-1);
					}
				}
			}
		} catch (Exception e) {
			// something is wrong
			// I will show the first page
			logger.error(PAGINATION_ERROR, e);
			searchJobBean.setCurrentPage(-1);
		}
		
		List<Job> res = null;
		
		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if (userAuth.hasAuthority(PermissionConstant.getInstance()
					.getOM_JobSearch())) {
				res = BLJob.getInstance().getResultsForSearch(searchJobBean,
						false);
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
		
		mav.addObject(SEARCH_RESULTS, res);

		// find the number of pages shown in pagination area
		ControllerUtils.getInstance().findPagesLimit(searchJobBean, PAGES);

		// if we have all the actions we put them on mav
		mav.addObject(ALL_ACTIONS, manageActions(searchJobBean, request));

		mav.addObject(SEARCH_JOB_BEAN, searchJobBean);
		mav.addObject(COMMAND, command);
		logger.debug("handlePagination - END");
		return mav;
	}

	/**
	 * @author mitziuro
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		logger.debug("referenceData - START");
		Map map = new HashMap();

		// used as a container for error messages
		ArrayList<String> errorMessages = new ArrayList<String>();

		try {
			// test if we have branches for displaying the branch select
			map.put(BRANCH_DISPLAY, manageBranches(request));
		} catch (BusinessException be) {
			logger.error(be.getMessage(), be);
			errorMessages.add(messageSource.getMessage(SEARCH_BRANCH_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() },
					RequestContextUtils.getLocale(request)));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(
					SEARCH_BRANCH_EXCEPTION_ERROR,
					new Object[] { ControllerUtils.getInstance()
							.getFormattedCurrentTime() }, RequestContextUtils
							.getLocale(request)));
		}

		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;

	}

	/**
	 * 
	 * if we have all the actions for a department to use search for a branch
	 * will not display this actions
	 * 
	 * @author mitziuro
	 * 
	 * @param searchPersonBean
	 * @param request
	 * @return
	 */
	private boolean manageActions(SearchJobBean searchJobBean,
			HttpServletRequest request) {
		logger.debug("manageActions START");
		boolean actions;
		Integer organisationId = (Integer) request.getSession().getAttribute(
				IConstant.SESS_ORGANISATION_ID);

		// if we search for the organisation that is on the context we have all
		// the options
		if (organisationId.intValue() == searchJobBean.getOrganisationId()) {
			actions = true;
		} else {
			actions = false;
		}

		logger.debug("manageActions END actions: ".concat(String
				.valueOf(actions)));
		return actions;
	}

	/**
	 * See if the current organisation has branches. If it has the we display
	 * the branch select
	 * 
	 * @author mitziuro
	 * @param request
	 * @return
	 * @throws BusinessException
	 */
	private boolean manageBranches(HttpServletRequest request)
			throws BusinessException {
		logger.debug("manageBranches START");
		boolean display = false;

		Integer organisationId = (Integer) request.getSession().getAttribute(
				IConstant.SESS_ORGANISATION_ID);

		Organisation organisation = BLOrganisation.getInstance().get(
				organisationId);
		// If the organisation is company there are no branches
		if (organisation.getType() == IConstant.NOM_ORGANISATION_TYPE_COMPANY) {
			display = false;
		} else {
			List<Organisation> branchList = BLOrganisation.getInstance()
					.getAllOrganisationsForParentId(organisationId);
			if (branchList.size() > 0) {
				display = true;
			}
		}

		logger.debug("manageBranches END display:".concat(String
				.valueOf(display)));
		return display;

	}
	
	/**
	 * Deletes a job that comes from a job form
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param searchJobBean
	 * @param jobId
	 * @throws BusinessException
	 */
	private void handleDeleteFromJobForm(HttpServletRequest request, SearchJobBean searchJobBean, Integer jobId) throws BusinessException{						
		logger.debug("handleDeleteFromJobForm - START - ");
		
		Integer[] jobs = new Integer[1];
		jobs[0] = jobId;			
		searchJobBean.setJobId(jobs);
		handleDeleteAllSimple(request, searchJobBean);
				
		logger.debug("handleDeleteFromJobForm - END - ");
	}
	
	/* (non-Javadoc)
	 * @see ro.cs.om.web.controller.root.RootSimpleFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException, java.util.Map)
	 */
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {	
		return super.showForm(request, errors, getFormView(), controlModel);
	}

}
