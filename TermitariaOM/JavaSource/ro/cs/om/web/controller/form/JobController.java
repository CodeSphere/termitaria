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
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import ro.cs.om.business.BLAudit;
import ro.cs.om.business.BLJob;
import ro.cs.om.business.BLOrganisation;
import ro.cs.om.common.IConstant;
import ro.cs.om.common.PermissionConstant;
import ro.cs.om.entity.Organisation;
import ro.cs.om.exception.BusinessException;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.entity.JobWeb;
import ro.cs.om.web.security.UserAuth;

public class JobController extends RootSimpleFormController {

	// Views
	private static final String FORM_VIEW 			= "Job";
	private static final String SUCCESS_VIEW 		= "Job";
	
	//Messages
	private static final String ADD_ERROR 			= "job.add.error";	
	private static final String ADD_SUCCESS 		= "job.add.success";
	private static final String UPDATE_ERROR 		= "job.update.error";	
	private static final String UPDATE_SUCCESS 		= "job.update.success";
	private static final String GET_ERROR 			= "job.get.error";
	
	//Actions
	private static final String ACTION				= "action";
	private static final String ADD					= "add";
	private static final String UPDATE				= "update";
	private static final String GET					= "get";
	
	//Parameters
	private static final String JOBID				= "jobId" ;
	private static final String GET_FROM_PANEL		= "GET_FROM_PANEL";
	private static final String ONSUBMIT			= "ONSUBMIT";
	private static final String GET_ACTION 			= "GET_ACTION";	

	public JobController() {
		setCommandName("jobWebBean");
		setCommandClass(JobWeb.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		logger.debug("formBackingObject - START");
		ArrayList<String> errorMessages = new ArrayList<String>();		
				
		JobWeb jw = new JobWeb();
							
		//check if I have to EDIT a role
		if (ServletRequestUtils.getIntParameter(request, JOBID) != null && 
				ServletRequestUtils.getStringParameter(request, ACTION) != null &&
				GET.equals(ServletRequestUtils.getStringParameter(request, ACTION))){
			logger.debug("formBackingObject: EDIT");		
			try{
				jw = handleGetJob(ServletRequestUtils.getIntParameter(request, JOBID), request);
			} catch (BusinessException be) {
				logger.error("", be);
				errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {
						be.getCode(),
						ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
			} catch (Exception e) {
				logger.error("", e);
				errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {
						null,
						ControllerUtils.getInstance().getFormattedCurrentTime() },
						RequestContextUtils.getLocale(request)));
			}
			logger.debug("formBackingObject - END");
		} else {
			logger.debug("formBackingObject: ADD");
			jw = new JobWeb();			
			//get all the organisation's persons
			logger.debug("formBackingObject - END");	
		}											
					
		//if the view will be rendered in a panel we display only some fields
		request.setAttribute(GET_FROM_PANEL, request.getParameter(GET_FROM_PANEL));
		
		logger.debug("formBackingObject - END");
		return jw;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");

		ModelAndView mav = new ModelAndView();
		JobWeb jobWebBean = (JobWeb) command;

		// setting the organisationId
		Integer organisationId = (Integer) request.getSession().getAttribute(
				IConstant.SESS_ORGANISATION_ID);
		jobWebBean.setOrganisationId(organisationId);

		// setting the locale and the errors/messages
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();

		// we see if we have add or update action
		if (ADD.compareTo(request.getParameter(ACTION)) == 0) {
			mav = handleAdd(jobWebBean, locale, errorMessages, infoMessages, request);
		} else if (UPDATE.compareTo(request.getParameter(ACTION)) == 0) {
			mav = handleUpdate(jobWebBean, locale, errorMessages, infoMessages, request);
		} else if (GET.compareTo(request.getParameter(ACTION)) == 0) {
			mav = handleGet(request, errorMessages, locale);
		}

		setErrors(request, errorMessages);
		setMessages(request, infoMessages);

		mav.addAllObjects(referenceData(request, command, errors));

		logger.debug("onSubmit - END");
		return mav;
	}

	/**
	 * 
	 * Add a job
	 * 
	 * @author mitziuro
	 * 
	 * @param jobWeb
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleAdd(JobWeb jobWeb, Locale locale,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request) {
		logger.debug("handleAdd - START");
		ModelAndView mav = new ModelAndView(getSuccessView());

		try {			
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_JobAdd())){
				mav.addObject(getCommandName(), jobWeb);
				//we add the job
				BLJob.getInstance().add(jobWeb);
				
				String jobNameMessage = ControllerUtils.getInstance().tokenizeField(jobWeb.getName(),  IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource
						.getMessage(ADD_SUCCESS, new Object[] { jobNameMessage }, locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_JOB_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_ADD_MESSAGE, new Object[] {jobWeb.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_ADD_MESSAGE, new Object[] {jobWeb.getName(), org.getName()}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}
		
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {
					be.getCode(),
					ControllerUtils.getInstance().getFormattedCurrentTime() },
					locale));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {
					null,
					ControllerUtils.getInstance().getFormattedCurrentTime() },
					locale));
		}
		logger.debug("handleAdd - END");
		

		//the view came from onsubmit
		//we need this for displaying only the messages when adding the job
		mav.addObject(ONSUBMIT, true);
		return mav;
	}

	/**
	 * 
	 * Update a job
	 * 
	 * @author mitziuro
	 * 
	 * @param jobWeb
	 * @param locale
	 * @param errorMessages
	 * @param infoMessages
	 * @return
	 */
	private ModelAndView handleUpdate(JobWeb jobWeb, Locale locale,
			ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest request) {
		logger.debug("handleUpdate - START");
		ModelAndView mav = new ModelAndView(getSuccessView());

		try {
			UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// ****************** Security *******************************
			if(userAuth.hasAuthority(PermissionConstant.getInstance().getOM_JobUpdate())){	
				mav.addObject(getCommandName(), jobWeb);
				// we update the job
				BLJob.getInstance().update(jobWeb);
				
				String jobNameMessage = ControllerUtils.getInstance().tokenizeField(jobWeb.getName(),  IConstant.NR_CHARS_PER_LINE_MESSAGE);
				
				infoMessages.add(messageSource.getMessage(UPDATE_SUCCESS, new Object[] { jobNameMessage },
						locale));
				
				//add the new audit event
				try {
					if (!userAuth.isAdminIT()){
						Organisation org = BLOrganisation.getInstance().get(ControllerUtils.getInstance().getOrganisationIdFromSession(request));
						BLAudit.getInstance().add(IConstant.AUDIT_EVENT_JOB_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
							, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_UPDATE_MESSAGE, new Object[] {jobWeb.getName(), org.getName()}, new Locale("en"))
							, messageSource.getMessage(IConstant.AUDIT_EVENT_JOB_UPDATE_MESSAGE, new Object[] {jobWeb.getName(), org.getName()}, new Locale("ro"))
							, ControllerUtils.getInstance().getOrganisationIdFromSession(request) , userAuth.getPersonId());
					}
				} catch (BusinessException exc){
					logger.error(exc);
				}
			} else {
				errorMessages.add(messageSource.getMessage(IConstant.SECURITY_NO_RIGHTS, null, locale));
			}

		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR,
					new Object[] {
							be.getCode(),
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, locale));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR,
					new Object[] {
							null,
							ControllerUtils.getInstance()
									.getFormattedCurrentTime() }, locale));
		}
		logger.debug("handleUpdate - END");
		return mav;
	}

	/**
	 * 
	 * Get the selected job
	 * 
	 * @author mitziuro
	 * 
	 * @param request
	 * @param errorMessages
	 * @param locale
	 * @return
	 * @throws BusinessException
	 */
	private ModelAndView handleGet(HttpServletRequest request,
			ArrayList<String> errorMessages, Locale locale)
			throws BusinessException {
		logger.debug("handleGet - START");
		ModelAndView mav = new ModelAndView(getSuccessView());

		JobWeb jw = null;
		try {
			// we get the job
			Integer jobId = Integer.valueOf(request.getParameter(JOBID));
			jw = BLJob.getInstance().getJobWeb(jobId);

		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {
					be.getCode(),
					ControllerUtils.getInstance().getFormattedCurrentTime() },
					locale));
		} catch (Exception e) {
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {
					null,
					ControllerUtils.getInstance().getFormattedCurrentTime() },
					locale));
		}

		// we put the bean on mav
		mav.addObject(getCommandName(), jw);
		logger.debug("handleGet - END");
		return mav;
	}
	
	
	/**
	 * 
	 * Get the job
	 * 
	 * @author Adelina
	 * 
	 * @param request
	 * @param jobId
	 * @return
	 * @throws BusinessException, ServletRequestBindingException
	 */
	private JobWeb handleGetJob(Integer jobId, HttpServletRequest request) throws ServletRequestBindingException, BusinessException {
		logger.debug("handleGetJob - START");							
		request.setAttribute(GET_ACTION, true);
		JobWeb jw = BLJob.getInstance().getJobWeb(jobId);			
		logger.debug("handleGetJob - END");
		return jw;
	}
}
