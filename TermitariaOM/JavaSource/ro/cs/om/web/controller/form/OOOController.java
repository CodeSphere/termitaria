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

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
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
import ro.cs.om.exception.BusinessException;
import ro.cs.om.job.CancelOOOJob;
import ro.cs.om.web.controller.root.ControllerUtils;
import ro.cs.om.web.controller.root.RootSimpleFormController;
import ro.cs.om.web.entity.OutOfOfficeWeb;
import ro.cs.om.web.security.UserAuth;

/**
 * @author alu
 *
 */
public class OOOController extends RootSimpleFormController{
	
	public static final String FORM_VIEW = "OOO_OutOfOffice";
	public static final String SUCCESS_VIEW = "Messages";	
	public static final String EDIT_OOO_FROM_LIST_FOR_PERSON = "EDIT_OOO_FROM_LIST_FOR_PERSON";
	public static final String EDIT_OOO_FROM_SEARCH = "EDIT_OOO_FROM_SEARCH";
	public static final String ACTION = "action";
	public static final String OOOID = "oooId";
	public static final String GET_ERROR = "ooo.getById.error";
	public static final String UPDATE_MESSAGE = "ooo.update.message";
	public static final String UPDATE_ERROR = "ooo.update.error";
	public static final String ADD_ERROR = "ooo.add.error";
	public static final String ADD_MESSAGE = "ooo.add.message";
	public static final String JSON_PERSONS = "JSON_PERSONS";
	public static final String GET_REPLACEMENT_ERROR = "ooo.get.replacement_error";
	public static final String REPLACEMENT_NAME = "REPLACEMENT_NAME";
	public static final String OWNER_NAME = "OWNER_NAME";
	public static final String OOO_ID = "OOO_ID";
	public static final String NO_OOO_ERROR = "ooo.no.ooo.error";
	private static final String ERROR = "ooo.listForPerson.error";
	private static final String LIST_VIEW = "OOO_ListForPerson";	
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String LAST_NAME = "LAST_NAME";
	private static final String NAME_TITLE 				= "NAME_TITLE";		
	private static final String OOO_PROFILES = "OOO_PROFILES";
	private static  final String ADD  = "ADD";	
	private static boolean addOOO = false;
	private static final String GET_ACTION = "GET_ACTION";
	
	// Number of characters that fit in the panel display header
    // if there are big words
    public static final Integer NR_CHARS				= 20;
	
	
	public OOOController() {
		setCommandName("oooBean");
		setCommandClass(OutOfOfficeWeb.class);
		setFormView(FORM_VIEW);
		setSuccessView(SUCCESS_VIEW);
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		logger.debug("initBinder - START");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(sdf, true));
		
        logger.debug("initBinder - END");
	}
		
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("formBackingObject - START");		
		OutOfOfficeWeb oooWeb = null;
		ArrayList<String> errorMessages = new ArrayList<String>();	
						
		try {
			// check if I have to edit an ooo profile
			if ((request.getParameter(ACTION) != null) && ((EDIT_OOO_FROM_LIST_FOR_PERSON.equals(request.getParameter(ACTION)) || (EDIT_OOO_FROM_SEARCH.equals(request.getParameter(ACTION)))))){
				oooWeb = BLOutOfOffice.getInstance().getOOOWebByID(ServletRequestUtils.getIntParameter(request, OOOID));
				addOOO = false;
				request.setAttribute(GET_ACTION, true);
				if (oooWeb == null){
					// it means that the ooo profile doesn't exist
					errorMessages.add(messageSource.getMessage(NO_OOO_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
					oooWeb = new OutOfOfficeWeb();
					addOOO = true;
				}
			} else {
				oooWeb = new OutOfOfficeWeb();
				addOOO = true;
			}
			request.setAttribute(ADD, addOOO);
		} catch (BusinessException be) {
			logger.error("formBackingObject", be);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("formBackingObject", e);
			errorMessages.add(messageSource.getMessage(GET_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		logger.debug("formBackingObject - END");
		
		return oooWeb;
	}
	
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {	
		
		logger.debug("referenceData - START");
		
		Map map = new HashMap();
		ArrayList<String> errorMessages = new ArrayList<String>();
		
		String action = ServletRequestUtils.getStringParameter(request, "action");				
		map.put(IConstant.REQ_ACTION, action);		
						
		// add all the persons from user's organization for autoComplete 
		map.put(JSON_PERSONS, 
				ControllerUtils.getInstance().
					getPersonsFromOrgAsJSON(ControllerUtils.getInstance().getOrganisationIdFromSession(request), RequestContextUtils.getLocale(request), errorMessages, messageSource));
		
		setErrors(request, errorMessages);
		logger.debug("referenceData - END");
		return map;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		logger.debug("onSubmit - START");
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		Locale locale = RequestContextUtils.getLocale(request);
		ArrayList<String> errorMessages = new ArrayList<String>();
		ArrayList<String> infoMessages = new ArrayList<String>();
		
		OutOfOfficeWeb oooWeb = (OutOfOfficeWeb) command;
		
		UserAuth userAuth = (UserAuth)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		if (!userAuth.hasAuthority(PermissionConstant.getInstance().getOM_OutOfOfficeAddToAll())){
			// if user is not adminIT, set ooo's person id from session
			oooWeb.setPersonId(userAuth.getPersonId());
		}
		if(oooWeb.getOutOfOfficeId() != -1){
			// it means that I have update action
			logger.debug("=====================update===========================");
			handleUpdate(oooWeb, locale, errorMessages, infoMessages, request);
		} else {
			// it means that I have add action
			logger.debug("=====================add===========================");
			mav = handleAdd(oooWeb, locale, errorMessages, infoMessages, request);								
		}
		
		setErrors(request, errorMessages);
		setMessages(request, infoMessages);
		
		logger.debug("onSubmit - END");
		return mav;
	}
	
	private void handleUpdate(OutOfOfficeWeb oooWeb, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages, HttpServletRequest  request){
		logger.debug("handleUpdate - START");
										
		try{					
			BLOutOfOffice.getInstance().updateOOOweb(oooWeb);
			createJobForOOO(oooWeb,true);			
			infoMessages.add(messageSource.getMessage(UPDATE_MESSAGE, null, locale));
			

			//add the new audit event
			try {
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
				Person person = BLPerson.getInstance().get(oooWeb.getPersonId()) ;
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_OOO_UPDATE_TYPE, userAuth.getFirstName(), userAuth.getLastName()
						, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_UPDATE_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
						, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (BusinessException exc){
				logger.error(exc);
			}
			
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(UPDATE_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
			
		
		logger.debug("handleUpdate - END");
				
	}
	
	private ModelAndView handleAdd(OutOfOfficeWeb oooWeb, Locale locale, ArrayList<String> errorMessages, ArrayList<String> infoMessages,  HttpServletRequest request) throws BusinessException{
		logger.debug("handleAdd - START");	
						
		ModelAndView mav = new ModelAndView();		
		
		try{			
								
			BLOutOfOffice.getInstance().addOOOweb(oooWeb);
			infoMessages.add(messageSource.getMessage(ADD_MESSAGE, null, locale));
			// create job for the newly created ooo profile
			createJobForOOO(oooWeb,false);	
						
			//add the new audit event
			try {
				UserAuth userAuth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Person person = BLPerson.getInstance().get(oooWeb.getPersonId()) ;
				if (!userAuth.isAdminIT()){
					BLAudit.getInstance().add(IConstant.AUDIT_EVENT_OOO_ADD_TYPE, userAuth.getFirstName(), userAuth.getLastName()
						, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_ADD_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("en"))
						, messageSource.getMessage(IConstant.AUDIT_EVENT_OOO_ADD_MESSAGE, new Object[] {person.getFirstName().concat(" ").concat(person.getLastName())}, new Locale("ro"))
						, ControllerUtils.getInstance().getOrganisationIdFromSession(request), userAuth.getPersonId());
				}
			} catch (BusinessException exc){
				logger.error(exc);
			}
									
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(ADD_ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, locale));
		}
		logger.debug("handleAdd - END");			
		
		// listing the ooo profiles
		if(errorMessages.size() == 0){		
			mav = handleListing(oooWeb.getPersonId(), request);			
			return mav;
						
		}
		return mav;
				
	}
	
	public ModelAndView handleListing(Integer personId, HttpServletRequest request) {
		
		ArrayList<String> errorMessages = new ArrayList<String>();		
				
		ModelAndView mav = new ModelAndView(LIST_VIEW);		
		// put on mav person's out of office profiles
		try{
			// put on mav person's out of office profiles
			List<OutOfOffice> list = BLOutOfOffice.getInstance().getByPersonID(personId);
			mav.addObject(OOO_PROFILES, list);
			// put on mav person's username
			if (list != null && list.size() != 0){
				// all OOOs should have the same person
				mav.addObject(FIRST_NAME, ControllerUtils.getInstance().truncateName(((OutOfOffice)list.get(0)).getPerson().getFirstName(), NR_CHARS ));
				mav.addObject(LAST_NAME, ControllerUtils.getInstance().truncateName(((OutOfOffice)list.get(0)).getPerson().getFirstName(), NR_CHARS ));							
				mav.addObject(NAME_TITLE,((OutOfOffice)list.get(0)).getPerson().getFirstName() + " " + ((OutOfOffice)list.get(0)).getPerson().getFirstName());
			} else {				
				Person p = BLPerson.getInstance().get(personId);
				mav.addObject(FIRST_NAME, ControllerUtils.getInstance().truncateName(p.getFirstName(), NR_CHARS));
				mav.addObject(LAST_NAME, ControllerUtils.getInstance().truncateName(p.getLastName(), NR_CHARS));			
				mav.addObject(NAME_TITLE, p.getFirstName() + " " + p.getLastName());			
			}
		} catch (BusinessException be) {
			logger.error("", be);
			errorMessages.add(messageSource.getMessage(ERROR, new Object[] {be.getCode(), ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		} catch (Exception e){
			logger.error("", e);
			errorMessages.add(messageSource.getMessage(ERROR, new Object[] {null, ControllerUtils.getInstance().getFormattedCurrentTime()}, RequestContextUtils.getLocale(request)));
		}
		
		setErrors(request, errorMessages);
		
		return mav;
	}
	
	private void createJobForOOO(OutOfOfficeWeb oooWeb, boolean isDeleteAction) throws SchedulerException,BusinessException{
		// Initiate JobDetail with job name(username + "_" + oooId), job group(username + "_" + "Group"),
		// and executable job class
		logger.debug("createJobForOOO - START");
		String username = null;
		UserAuth userAuth =  (UserAuth)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if(!userAuth.isAdminIT()){
			username = userAuth.getUsername();
		} else{
			username = BLPerson.getInstance().get(oooWeb.getPersonId()).getUsername();
		}
	
		Scheduler sched = (Scheduler)OMContext.getFromContext(IConstant.SCHEDULER);		
		
		JobDetail jobDetail = 
        			new JobDetail(String.valueOf(username).concat("_").concat(String.valueOf(oooWeb.getOutOfOfficeId())), 
        					username.concat("_Group"), CancelOOOJob.class);
        jobDetail.getJobDataMap().put(OOO_ID, oooWeb.getOutOfOfficeId());              
        
        // Initiate SimpleTrigger with its name("Trigger_" + oooId) and group name(username + "_Group")
        SimpleTrigger simpleTrigger = new SimpleTrigger("Trigger".concat(String.valueOf(oooWeb.getOutOfOfficeId())), 
        									username.concat("_Group"));
        // set its start up time
        simpleTrigger.setStartTime(oooWeb.getEndPeriod());
        // set the interval, how often the job should run (1 second here) - must be set 
        simpleTrigger.setRepeatInterval(1000);
        // set the number of execution of this job, set to 1 time. 
        // It will run 1 time and exhaust.
        simpleTrigger.setRepeatCount(0);

        if(isDeleteAction){
        	String jobName = String.valueOf(username).concat("_").concat(String.valueOf(oooWeb.getOutOfOfficeId()));
        	sched.deleteJob(jobName, String.valueOf(username).concat("_Group"));
        }
        sched.scheduleJob(jobDetail, simpleTrigger);
        logger.debug("createJobForOOO - END");
    }
}


